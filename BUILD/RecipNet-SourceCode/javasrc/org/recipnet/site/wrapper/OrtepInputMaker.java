/*
 * Reciprocal Net Project
 *
 * OrtepInputMaker.java
 *
 * 23-Oct-2002: jobollin wrote first draft
 * 17-Dec-2002: Fixed off-by-one and data format bugs in JammSupport's ortep
 *              stub creator
 * 07-Jan-2003: Task #681: Fixed bug in ortep stub generator wherein only the
 *              first 821 instruction in each set was converted to an 811
 * 21-Feb-2003: jobollin made consistent with changes to Rotate3DModel applied
 *              as part of task #682
 * 20-Mar-2003: jobollin made createOrtStub and makeOrtepInput throw exceptions
 *              instead of eating them as part of task #629
 * 21-Mar-2003: jobollin made createOrtStub and makeOrtepInput not throw any
 *              IOExceptions (UnexpectedExceptionExceptions are thrown instead;
 *              part of task #629)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper; also changed package
 *              references due to source tree reorganization; also made class
 *              and most methods public
 * 09-Jan-2004: ekoperda modified createOrtStub() and createBondCards() in
 *              order to utilize new CrtFile class
 * 05-May-2004: jobollin fixed bug #1206 in createBondCards(); createOrtStub()
 *              was modified to support the changes to createBondCards
 * 14-May-2004: jobollin fixed bug #1221 in createBondCards()
 * 14-Dec-2005: jobollin updated createOrtStub to use the nullary constructor
 *              of NumericStringComparator in conjunction with
 *              Collections.reverseOrder()
 * 14-Dec-2005: jobollin updated the source for compatibility with changes to
 *              CrtFile and to use the new OrtFile and SdtFile; added type
 *              arguments in various places; switched to use of a Formatter for
 *              formatting output; updated createOrtStub() to take a
 *              MolecularModel object instead of raw CRT data and changed its
 *              name to createOrtepInstructions; made makeOrtepInput() take an
 *              OrtFile instead of raw ORT data, added a Appendable argument,
 *              removed the SDT data argument, and changed its name to
 *              appendOrtepInput(); updated the class for non-static usage, so
 *              that instances retain symmetry and symmetry code mapping
 *              information about particular models; accommodated API changes to
 *              the ImageParameters class; removed unusued imports
 * 20-Nov-2006: jobollin updated javadoc tags where javadoc didn't understand
 *              references to nested classes
 */

package org.recipnet.site.wrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.recipnet.common.Element;
import org.recipnet.common.IteratorFilter;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.files.OrtFile;
import org.recipnet.common.files.SdtFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.ortep.ExtraParameterCard;
import org.recipnet.common.files.ortep.NewInstructionCard;
import org.recipnet.common.files.ortep.OrtepInstructionCard;
import org.recipnet.common.files.ortep.ParameterBearingCard;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Vector;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.AtomicMotion;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.ModelBuilder;
import org.recipnet.common.molecule.MolecularModel;

/**
 * <p>
 * A class of objects that can construct full, standard ORTEP-III input files.
 * This class supports both the classic style of ORTEP input building -- all
 * model atoms from an SDT and instructions from an external ORT, with some
 * filtering -- as well as a newer protocol based on all information coming from
 * a CRT file and CIF file, with optional use of an ORT file for user
 * customization. The new protocol maps atom site information encoded in the CRT
 * file to CIF atoms and symmetry elements according to information maintained
 * by instances.
 * </p><p>
 * Once initialized, instances are thread-safe inasmuch as their instance data
 * do not change. They do manipulate externally-provided objects in ways that
 * may not be safe for shared objects without external synchronization, however.
 * </p>
 */
public class OrtepInputMaker {
    
    /**
     * The ORTEP instruction code for beginning a plot
     */
    private final static int BEGIN_PLOT = 201;

    /**
     * The ORTEP instruction code for ending a plot
     */
    private final static int END_PLOT = 202;

    /**
     * A deprecated ORTEP instruction code for ending a plot
     */
    private final static int DEPRECATED_END_PLOT = 210;

    /**
     * The ORTEP instruction code for configuring page parameters
     */
    private final static int PAGE_SIZE = 301;

    /**
     * The ORTEP instruction code for adding atom sites to those eligible for
     * drawing
     */
    private final static int ADD_ATOMS = 401;
    
    /**
     * The ORTEP instruction code for removing atom sites from those eligible
     * for drawing
     */
    private final static int REMOVE_ATOMS = 411;

    /**
     * The ORTEP instruction code for configuring the reference Cartesian axes
     * to which the view is referred
     */
    private final static int REFERENCE_AXES = 501;

    /**
     * The ORTEP instruction code for rotating the "working system" of Cartesian
     * coordinates relative to the reference system; the working system defines
     * the coordinates to which the model is referred, so rotating this system
     * rotates the model relative to the viewing plane
     */
    private final static int AXIS_ROTATIONS = 502;

    /**
     * The ORTEP instruction code for manual specification of image center and
     * scale
     */
    private final static int MANUAL_POSITION_AND_SCALE = 601;

    /**
     * The ORTEP instruction code for manual specification of image center and
     * automatic scale
     */
    private final static int MANUAL_POSITION = 602;

    /**
     * The ORTEP instruction code for automatic determination of image center
     * and manual scale
     */
    private final static int MANUAL_SCALE = 603;

    /**
     * The ORTEP instruction code for automatic determination of image center
     * and scale
     */
    private final static int AUTO_POSITION_AND_SCALE = 604;

    /**
     * The ORTEP instruction code for incremental scaling and positioning of the
     * model
     */
    private final static int INCREMENT_POSITION_AND_SCALE = 611;

    /**
     * The ORTEP instruction code for incremental scaling of the model
     */
    private final static int INCREMENT_SCALE = 613;

    /**
     * The ORTEP instruction code for drawing atoms with principal elipses,
     * axes, and octant shading
     */
    private final static int DRAW_DEFAULT_ATOM = 711;

    /**
     * The ORTEP instruction code for drawing atoms with manually configured
     * style parameters
     */
    private final static int DRAW_GENERAL_ATOM = 715;

    /**
     * The ORTEP instruction code for computing and storing overlap information
     * for bonds
     */
    private final static int STORE_BOND_OVERLAPS = 821;

    /**
     * The ORTEP instruction code for drawing bonds that is preferred by this
     * class
     */
    private final static int DRAW_BONDS = 811;

    /**
     * An alternative ORTEP instruction code for drawing bonds.  This version
     * produces diagnostic output to the program's output file in addition to
     * performing the drawing itself (to a different output file).
     */
    private final static int DRAW_BONDS_ALT = 801;

    /*
     * NOTE: instruction codes 802, 812, 803, and 813 are also represent
     * bond-drawing instructions, but this class doesn't use or refer to these 
     */
    
    /**
     * The ORTEP instruction code for storing outline information for overlap
     * removal
     */
    private final static int STORE_OUTLINES = 1001;

    /**
     * A deprecated ORTEP instruction code for storing outline information for
     * overlap removal
     */
    private final static int DEPRECATED_STORE_OUTLINES = 511;

    /**
     * The ORTEP instruction code for the end of an instruction list
     */
    private final static int END_INSTRUCTIONS = -1;

    /**
     * A pre-configured parameter card describing the default page parameters
     */
    private final static NewInstructionCard DEFAULT_PAGE_SIZE
            = new NewInstructionCard(PAGE_SIZE,
                    "      8.5     11.0       0.      0.5");

    /**
     * A pre-configured parameter card describing the default reference axes;
     * these axes have a known orientation relative to those generated by a
     * {@link CoordinateSystem} object or by the external "shortep" program, so
     * as to produce ORTEP drawings with the expected orientation
     */
    private final static NewInstructionCard DEFAULT_REFERENCE_AXES
            = new NewInstructionCard(REFERENCE_AXES,
                    "   155501   155501   165501   155501   155401");

    /**
     * The maximum number of instruction cards that may be used for a single
     * ORTEP instruction, including the main instruction card and all associated
     * trailer cards
     */
    private final static int MAX_BLOCK_SIZE = 20;

    /**
     * A pre-configured parameter card describing the default bond parameters
     * for bonds not involving hydrogen
     */
    private final static ExtraParameterCard DEFAULT_BOND_PARAMETERS
            = new ExtraParameterCard(2, padLeft("4", 15) + padLeft("0.04", 18));

    /**
     * A pre-configured parameter card describing the default bond parameters
     * for bonds involving hydrogen
     */
    private final static ExtraParameterCard DEFAULT_HBOND_PARAMETERS
            = new ExtraParameterCard(2, padLeft("2", 15) + padLeft("0.02", 18));

    /**
     * The maximum number of symmetry elements supported by ORTEP; referenced
     * by other classes in the package, so not private
     */
    final static int ORTEP_MAX_SYMMETRY = 96;

    /**
     * The length of each individual parameter field on a type 1 parameter card
     */
    private final static int ORTEP_PARAM_LENGTH = 9;
    
    /**
     * The number of ADC runs to output on a type 0 or type 1 card
     */
    private final static int RUNS_PER_CARD = 3;
    
    /**
     * The maximum number of parameters to output on a type 0 or type 1 card
     */
    private final static int MAX_PARAMS_PER_CARD = 7;
    
    /**
     * The lower bounds of the three ADC translation indices used for creating
     * atom removal cards
     */
    private final static int[] LOWER_BOUNDS = new int[] { 1, 1, 1 };

    /**
     * The upper bounds of the three ADC translation indices used for creating
     * atom removal cards
     */
    private final static int[] UPPER_BOUNDS = new int[] { 9, 9, 9 };

    /**
     * The maximum total length of the parameter section of an instruction card
     */
    private final static int ORTEP_MAX_PARAMS_LENGTH
            = MAX_PARAMS_PER_CARD * ORTEP_PARAM_LENGTH;

    /**
     * The maximum atom number supported by ORTEP
     */
    private final static int ORTEP_MAX_ATOM_NUMBER = 999;

    /**
     * The increment between ORTEP major instruction numbers (i.e. the
     * 100-series instructions, 200-series instructions, etc.); integer
     * division by this number extracts the instruction series number (1, 2,
     * <i>etc</i>.).
     */
    private final static int MAJOR_NUMBER_INCREMENT = 100;
    
    /**
     * The ORTEP instruction series index for labelling instructions
     */
    private final static int LABEL_MAJOR_NUMBER = 9;
    
    /**
     * The ORTEP minor instruction number within the labelling series that
     * specifies atom labelling
     */
    private final static int ATOM_LABEL_MINOR = 1;
    
    /**
     * The ORTEP minor instruction number within the labelling series that
     * specifies title labelling
     */
    private final static int TITLE_LABEL_MINOR = 2;
    
    /**
     * The ORTEP minor instruction number within the labelling series that
     * specifies an alternative form of title labelling
     */
    private final static int TITLE_LABEL_MINOR_ALT = 3;
    
    private final ADCGenerator adcGenerator;

    /**
     * Initializes a new {@code OrtepInputMaker} to use the specified ADC
     * generator
     * 
     * @param generator the {@code ADCGenerator} this {@code OrtepInputMaker}
     *        should use
     */
    private OrtepInputMaker(ADCGenerator generator) {
        this.adcGenerator = generator;
    }
    
    /**
     * Creates ORTEP instructions suitable for use in rendering the provided
     * model
     * 
     * @param  <A> the type of Atom used by the specified model; this can always
     *         be inferred from the model argument if <i>its</i> type
     *         arguments are known to the compiler
     * @param  targetModel the {@code MolecularModel} for which ORTEP rendering
     *         instructions are requested; this determines the atoms and
     *         bonds that should appear in a drawing.  It should be derived
     *         from the target model specified for obtaining this
     *         {@code OrtepInputMaker}, but it does not need to be the same one
     *
     * @return an {@code OrtFile} comprising the requested drawing instructions
     */
    public <A extends Atom> OrtFile createOrtepInstructions(
            MolecularModel<A, Bond<A>> targetModel) {
        OrtFile instructions = new OrtFile();
        Map<? extends Atom, AtomDesignatorCode> adcMap
                = adcGenerator.createADCMap(targetModel.getAtoms());

        /*
         * Initialize a sorted set for the elements represented in the model,
         * in descending atomic number order.
         */
        Set<Element> elementsPresent = new TreeSet<Element>(
                new Comparator<Element>() {
                    public int compare(Element e0, Element e1) {
                        return (e1.getAtomicNumber() - e0.getAtomicNumber());
                    }
                });
        
        for (Atom atom : targetModel.getAtoms()) {
            Element el = atom.getElement();
            
            elementsPresent.add((el == null) ? Element.CARBON : el);
        }

        instructions.addInstruction(new NewInstructionCard(BEGIN_PLOT, ""));
        instructions.addInstruction(DEFAULT_PAGE_SIZE);
        instructions.addInstruction(DEFAULT_REFERENCE_AXES);
        instructions.addInstruction(new NewInstructionCard(AXIS_ROTATIONS,
                "       1.       0.       2.       0.       3.       0."));

        if (!adcGenerator.usesModelSymmetry()) {
            int lastAdc = -(adcMap.size() * 100000 + 55501);

            instructions.addInstruction(new NewInstructionCard(ADD_ATOMS,
                    "   155501" + padLeft(String.valueOf(lastAdc), 9)));
        } else {
            List<AtomDesignatorCode> adcList
                    = new ArrayList<AtomDesignatorCode>(adcMap.values());
            
            Collections.sort(adcList, new Comparator<AtomDesignatorCode>(){
                private long adcToLong(AtomDesignatorCode adc) {
                    int[] translations = adc.getTranslations();
                    long rval = adc.getAtomNumber();
                    
                    for (int translation : translations) {
                        rval *= 10;
                        rval += translation;
                    }
                    rval *= 1000;
                    rval += adc.getSymmetryNumber();
                    
                    return rval;
                }
                
                public int compare(AtomDesignatorCode adc1,
                        AtomDesignatorCode adc2) {
                    long n1 = adcToLong(adc1);
                    long n2 = adcToLong(adc2);
                    
                    if (n1 < n2) {
                        return -1;
                    } else if (n1 > n2) {
                        return 1;
                    } else {
                        return 0;
                    }
                }});
            for (ParameterBearingCard card : createAtomCards(adcList)) {
                instructions.addInstruction(card);
            }
        }

        instructions.addInstruction(new NewInstructionCard(
                AUTO_POSITION_AND_SCALE,
                "        0        0      0.8     1.54"));
        instructions.addInstruction(
                new NewInstructionCard(INCREMENT_SCALE, ""));
        instructions.addInstruction(new NewInstructionCard(STORE_OUTLINES, ""));

        List<ParameterBearingCard> bondCards = createBondCards(adcMap,
                DEFAULT_BOND_PARAMETERS,
                new IteratorFilter<Bond<? extends Atom>>(
                        targetModel.getBonds().iterator()) {
                    @Override
                    public boolean shouldPass(Bond<? extends Atom> b) {
                        return !b.includesHydrogen();
                    }
                });
        List<ParameterBearingCard> hbondCards = createBondCards(adcMap,
                DEFAULT_HBOND_PARAMETERS,
                new IteratorFilter<Bond<? extends Atom>>(
                        targetModel.getBonds().iterator()) {
                    @Override
                    public boolean shouldPass(Bond<? extends Atom> b) {
                        return b.includesHydrogen();
                    }
                });

        for (ParameterBearingCard card : bondCards) {
            instructions.addInstruction(card);
        }

        for (ParameterBearingCard card : hbondCards) {
            instructions.addInstruction(card);
        }

        for (ParameterBearingCard card : makeInstructionBlocks(bondCards,
                DRAW_BONDS, MAX_BLOCK_SIZE)) {
            instructions.addInstruction(card);
        }

        for (ParameterBearingCard card : makeInstructionBlocks(hbondCards,
                DRAW_BONDS, MAX_BLOCK_SIZE)) {
            instructions.addInstruction(card);
        }

        /*
         * This color change instruction is not strictly necessary; it is more a
         * place-holder for a the instructions to be used by a future colored
         * ORTEP feature
         */
        instructions.addInstruction(new NewInstructionCard(204, "        0"));

        // Atom drawing instructions
        for (Element el : elementsPresent) {
            String numberString
                    = padLeft(String.valueOf(el.getAtomicNumber()), 9);
            String parameters
                    = padLeft(numberString + numberString + "        1", 45);

            if (el == Element.HYDROGEN) {
                instructions.addInstruction(
                        new NewInstructionCard(DRAW_GENERAL_ATOM, "        1"));
            } else {
                instructions.addInstruction(
                        new NewInstructionCard(DRAW_DEFAULT_ATOM, null));
            }

            instructions.addInstruction(new ExtraParameterCard(1, parameters));
        }

        // End of drawing
        instructions.addInstruction(new NewInstructionCard(END_PLOT, ""));
        instructions.addInstruction(
                new NewInstructionCard(END_INSTRUCTIONS, ""));

        return instructions;
    }
    
    /**
     * A helper method that creates ORTEP instruction cards appropriate for
     * including all the specified atom designator codes in an ORTEP drawing.
     * This version simply specifies each ADC individually in a series of one or
     * more code 401 cards and type 1 trailer cards.
     * 
     * @param  adcs a {@code Collection} of all the {@code AtomDesignatorCode}s
     *         to include
     *         
     * @return a {@code List} of {@code ParameterBearingCard} objects
     *         representing the requested instructions
     */
    private List<ParameterBearingCard> createAtomCards(
            Collection<AtomDesignatorCode> adcs) {
        List<ExtraParameterCard> cards = new ArrayList<ExtraParameterCard>();
        StringBuilder sb = new StringBuilder(ORTEP_MAX_PARAMS_LENGTH);
        int count = 0;

        for (AtomDesignatorCode adc : adcs) {
            sb.append(padLeft(adc.toString(), ORTEP_PARAM_LENGTH));
            if ((++count % MAX_PARAMS_PER_CARD) == 0) {
                cards.add(new ExtraParameterCard(1, sb.toString()));
                sb.setLength(0);
            }
        }

        if ((sb.length() > 0) || (cards.size() == 0)) {
            cards.add(new ExtraParameterCard(1, sb.toString()));
        }

        return makeInstructionBlocks(cards, ADD_ATOMS, MAX_BLOCK_SIZE);
    }

    /**
     * Creates a series of ORTEP explicit bond overlap cards for the bonds
     * specified collectively by the provided iterator and atom-to-ADC map,
     * using the specified trailer card as the last in each block.
     * 
     * @param  adcMap a {@code Map&lt;? extends Atom, AtomDesignatorCode&gt;}
     *         associating the atoms involved in the provided bonds with the
     *         atom designator codes to be used to reference them in the
     *         requested bond cards
     * @param  trailer an {@code ExtraParameterCard} to be used as the last in
     *         every block of bond cards; intended to be used to deliver the
     *         bond rendering parameters
     * @param  bondIterator an
     *         {@code Iterator&lt;Bond&lt;? extends Atom&gt;&gt;} from which the
     *         bonds to be represented on the resulting instructions should be
     *         drawn; all bonds in the iteration will be used
     * 
     * @return a {@code List&lt;ParameterBearingCard&gt;} whose elements are
     *         collectively suitable for direct use as the ORTEP bond overlap
     *         cards for the specified bonds
     *
     * @throws IllegalArgumentException if any atom involved in any of the
     *         specified bonds is not mapped to an ADC by the specified map
     */
    private List<ParameterBearingCard> createBondCards(
            Map<? extends Atom, AtomDesignatorCode> adcMap,
            ExtraParameterCard trailer,
            Iterator<Bond<? extends Atom>> bondIterator) {
        List<ExtraParameterCard> cards = new ArrayList<ExtraParameterCard>();
        StringBuilder sb = new StringBuilder(ORTEP_MAX_PARAMS_LENGTH);
        final int bondsPerCard = 3;
        final int bondsPerBlock = (bondsPerCard * (MAX_BLOCK_SIZE - 1));
        int count = 0;

        while (bondIterator.hasNext()) {
            Bond<? extends Atom> bond = bondIterator.next();
            AtomDesignatorCode adc1 = adcMap.get(bond.getAtom1());
            AtomDesignatorCode adc2 = adcMap.get(bond.getAtom2());

            if (adc1 == null) {
                throw new IllegalArgumentException(
                        "No ADC for atom " + bond.getAtom1().getLabel());
            }
            if (adc2 == null) {
                throw new IllegalArgumentException(
                        "No ADC for atom " + bond.getAtom2().getLabel());
            }
            
            
            sb.append(padLeft(adc1.toString(), 9));
            sb.append(padLeft(adc2.toString(), 9));
            if ((++count % bondsPerCard) == 0) {
                cards.add(new ExtraParameterCard(1, sb.toString()));
                sb.setLength(0);
            }
            if ((count % bondsPerBlock) == 0) {
                cards.add(trailer);
            }
        }

        if ((sb.length() > 0) || (cards.size() == 0)) {
            cards.add(new ExtraParameterCard(1, sb.toString()));
            cards.add(trailer);
        } else if ((count % bondsPerBlock) != 0) {
            cards.add(trailer);
        }

        return makeInstructionBlocks(
                cards, STORE_BOND_OVERLAPS, MAX_BLOCK_SIZE);
    }

    /**
     * Creates a series of ORTEP instruction cards matching the provided one
     * except for having the first card and each subsequent
     * {@code cardsPerBlock}<sup>th</sup> replaced by a
     * {@link NewInstructionCard} with the same parameters and the specified
     * instruction code.  This is useful for assembling parameters into
     * instruction blocks of appropriate length for ORTEP to handle.
     * 
     * @param  cards a {@code List&lt;? extends ParameterBearingCard&gt;} of
     *         the cards containing the desired parameters
     * @param  instruction the instruction code to be used on the
     *         {@code NewInstructionCards} created by this method
     * @param  cardsPerBlock the total number of cards to include in each block
     * 
     * @return a {@code List&lt;ParameterBearingCard&gt;} of the resulting
     *         cards; some elements of this list may be present in the input
     *         list as well
     */
    private List<ParameterBearingCard> makeInstructionBlocks(
            List<? extends ParameterBearingCard> cards, int instruction,
            int cardsPerBlock) {
        List<ParameterBearingCard> rval = new ArrayList<ParameterBearingCard>();
        List<? extends ParameterBearingCard> cardTail = cards;

        while (cardTail.size() > 0) {
            int blockSize = Math.min(cardTail.size(), cardsPerBlock);

            rval.add(new NewInstructionCard(
                    instruction, cardTail.get(0).getParameters()));
            rval.addAll(cardTail.subList(1, blockSize));
            cardTail = cardTail.subList(blockSize, cardTail.size());
        }

        return rval;
    }

    /**
     * Appends an ORTEP-III format input file to the specified appendable
     * object, based on the provided model instructions and image parameters,
     * and on the configured base model
     *  
     * @param  out the {@code Appendable} to which the output should be
     *         directed; should not be {@code null}
     * @param  instructions an {@code OrtFile} containing model instructions
     *         for the requested ORTEP file; these will be filtered to insert
     *         appropriate image parameters and to improve compliance with
     *         ORTEP-III conventions; should not be {@code null}, and should
     *         correspond to the same base model and ADC style as are configured
     *         on this {@code OrtepInputMaker}
     * @param  parameters an {@code ImageParameters} describing the requested
     *         ORTEP image; the specified x-, y-, and z-rotations are inserted
     *         into the generated ORTEP input file; should not be {@code null}
     *         
     * @throws IOException if an I/O error occurs while writing to {@code out}
     */
    public void appendOrtepInput(Appendable out,
            OrtFile instructions, ImageParameters parameters)
            throws IOException {
        Formatter formatter;
        IOException exception;
        
        if ((out == null) || (instructions == null) || (parameters == null)) {
            throw new NullPointerException("Null argument");
        }
        
        formatter = new Formatter(out, Locale.US);
        
        // Output a blank title
        formatter.format("%n");
        
        // Output the model
        formatModel(adcGenerator.getBaseModel(), formatter,
                adcGenerator.usesModelSymmetry());
        
        /*
         * Apply any modifications to the instructions that cannot be handled
         * in streaming mode by filterInstructions()
         */
        instructions = adjustInstructions(instructions, parameters);
        
        // Output the instructions
        filterInstructions(instructions, parameters).formatTo(formatter);
        
        // Finish up
        formatter.flush();
        exception = formatter.ioException();
        if (exception != null) {
            throw exception;
        }
        
        /*
         * The Formatter is not closed here because it is not our responsibility
         * to close the underlying Appendable
         */
    }
    
    /**
     * Outputs the model section of an ORTEP input file via the specified
     * formatter.
     * 
     * @param model a MolecularModel&lt;? extends FractionalAtom, ?&gt;
     *            representing the model to be output
     * @param formatter the formatter with which to output the model information
     * @param includeModelSymmetry {@code true} to output the symmetry
     *            operations of the model, {@code false} to output only an
     *            identity operation.
     */
    private void formatModel(MolecularModel<? extends FractionalAtom, ?> model,
            Formatter formatter, boolean includeModelSymmetry) {
        Vector[] cellVectors = model.getCell().getVectors();
        List<SymmetryMatrix> symmetryOperations;
    
        // Determine the symmetry operations to output
        if (model.getSymmetryOperations().isEmpty() || !includeModelSymmetry) {
            symmetryOperations
                    = Collections.singletonList(SymmetryMatrix.IDENTITY);
        } else {
            symmetryOperations = model.getSymmetryOperations();
        }
    
        // Output the unit cell parameters
        formatter.format(" %8.5f%9.5f%9.5f%9.4f%9.4f%9.4f%n",
                cellVectors[0].length(),
                cellVectors[1].length(),
                cellVectors[2].length(),
                Math.toDegrees(cellVectors[1].angleWith(cellVectors[2])),
                Math.toDegrees(cellVectors[2].angleWith(cellVectors[0])),
                Math.toDegrees(cellVectors[0].angleWith(cellVectors[1])));
    
        // Output the symmetry cards
        for (Iterator<SymmetryMatrix> it = symmetryOperations.iterator();
                it.hasNext();) {
            SymmetryMatrix matrix = it.next();
            int[][] rotations = matrix.getRotationMatrix();
            int[] translations = matrix.getTranslationVector();
    
            formatter.format(
                    "%s%14.10f%3d%3d%3d%15.10f%3d%3d%3d%15.10f%3d%3d%3d%n",
                    (it.hasNext() ? " " : "1"),
                    translations[0] / 12.0d,
                    rotations[0][0], rotations[0][1], rotations[0][2],
                    translations[1] / 12.0d,
                    rotations[1][0], rotations[1][1], rotations[1][2],
                    translations[2] / 12.0d,
                    rotations[2][0], rotations[2][1], rotations[2][2]);
        }
    
        // Output the atoms
        for (Iterator<? extends FractionalAtom> it
                = model.getAtoms().iterator(); it.hasNext();) {
            FractionalAtom atom = it.next();
            AtomicMotion motion = atom.getAtomicMotion();
            String label = atom.getLabel();
            double[] coords = atom.getFractionalCoordinates();
            Element element = atom.getElement();
    
            /*
             * If no element is listed then treat it as a carbon atom
             */
            if (element == null) {
                element = Element.CARBON;
            }
            
            /*
             * Remove extraneous label characters and center the label in a
             * six-character field
             */
            label.replaceAll("[()]+", "");
            if (label.length() >= 6) {
                label = label.substring(0, 6);
            } else {
                StringBuilder sb = new StringBuilder(label);
    
                for (int toPad = (6 - label.length()) / 2; toPad > 0; toPad--) {
                    sb.insert(0, ' ');
                }
    
                label = sb.toString();
            }
    
            // Output the label and coordinates line
            formatter.format("%6s   %9d%9s%9.5f%9.5f%9.5f        0%n",
                    label, element.getAtomicNumber(), "",
                    coords[0], coords[1], coords[2]);
    
            // Output the thermal parameter line
    
            // end-of-list sentinel
            formatter.format("%1d", (it.hasNext() ? 0 : 1));
    
            // dummy, anisotropic, or isotropic thermal parameters
            if ((element == Element.HYDROGEN) || (motion == null)) {
                formatter.format("     0.1%45s        7%n", "");
            } else if (motion.isAnisotropic()) {
                double[][] u = motion.getAnisotropicU();
    
                formatter.format("%8.5f%9.5f%9.5f%9.5f%9.5f%9.5f        8%n",
                        u[0][0], u[1][1], u[2][2], u[1][0], u[2][0], u[2][1]);
            } else {
                formatter.format("%8.5f%53s6%n", motion.getIsotropicU()
                        * AtomicMotion.U_TO_B_SCALE_FACTOR, "");
            }
        }
    }

    /**
     * <p>
     * Returns an OrtFile reflecting any image parameters that cannot be
     * accommodated on the fly by
     * {@link #filterInstructions(OrtFile, ImageParameters)}. This is only
     * necessary when look-ahead would be required to determine whether or where
     * to insert additional cards or modify or remove existing cards.
     * </p><p>
     * Note: It is not sufficient to do this only when we create new cards,
     * because we must account for the case where the user has provided his own
     * ORTEP instructions.
     * </p>
     * 
     * @param originalInstructionsFile an {@code OrtFile} containing the
     *        instructions to adjust
     * @param parameters the {@code ImageParameters} governing what adjustments,
     *        if any, should be made
     * 
     * @return an {@code OrtFile} adjusted as necessary to satisfy the image
     *         parameters; may or may not be the {@code originalInstructions}
     *         object
     */
    private OrtFile adjustInstructions(OrtFile originalInstructionsFile,
            ImageParameters parameters) {
        if (!parameters.shouldIncludeHydrogen()) {
            List<OrtepInstructionCard> instructions
                    = new ArrayList<OrtepInstructionCard>(
                            originalInstructionsFile.getInstructions());
            OrtFile newInstructionsFile = new OrtFile();
            Set<Integer> hydrogenIndices = new HashSet<Integer>();
            List<NumberRun> hydrogenRuns = new ArrayList<NumberRun>();
            NumberRun currentRun = null;
            int index = 0;
            
            /*
             * Determine which atom numbers correspond to hydrogen atoms;
             * construct a hash table of them for quick individual lookup, and
             * a list of ranges for range analysis  
             */
            for (Atom atom : adcGenerator.getBaseModel().getAtoms()) {
                ++index;
                
                if (atom.getElement() == Element.HYDROGEN) {
                    hydrogenIndices.add(index);
                    if (currentRun == null) {
                        currentRun = new NumberRun(index);
                        hydrogenRuns.add(currentRun);
                    } else {
                        currentRun.setEnd(index);
                    }
                } else {
                    currentRun = null;
                }
            }
            
            removeExplicitBonds(hydrogenRuns, instructions);
            removeLabels(hydrogenRuns, instructions);
            insertRemovalCards(createRemovalCards(hydrogenRuns), instructions);
            
            for (OrtepInstructionCard card : instructions) {
                newInstructionsFile.addInstruction(card);
            }
            
            return newInstructionsFile;
        } else {
            return originalInstructionsFile;
        }
    }
    
    /**
     * Removes from among the provided ORTEP instruction cards all the explicit
     * bonds and bond overlaps referencing atoms from the specified number runs. 
     *
     * @param numberRuns a {@code List<NumberRun>} of the number runs containing
     *        the atom numbers whose bonds should be removed
     * @param instructions a {@code List<OrtepInstructionCard>} of the ORTEP
     *        instructions from which explicit bonds matching the number runs
     *        should be removed
     */
    private void removeExplicitBonds(List<NumberRun> numberRuns,
            List<OrtepInstructionCard> instructions) {
        
        // Find instances of the affected instructions
        for (int instructionIndex = 0; instructionIndex < instructions.size();
                instructionIndex++) {
            OrtepInstructionCard card = instructions.get(instructionIndex);
            
            instructions:
            if (card instanceof NewInstructionCard) {
                NewInstructionCard instruction = (NewInstructionCard) card;
                int instructionNumber = instruction.getInstruction();
                int adcCount = 0;
                
                if ((instructionNumber == DRAW_BONDS)
                        || (instructionNumber == DRAW_BONDS_ALT)
                        || (instructionNumber == STORE_BOND_OVERLAPS)) {
                    List<AtomDesignatorCode> adcList
                            = new ArrayList<AtomDesignatorCode>();
                    int trailer2Index = instructionIndex + extractADCParameters(
                            instructions.subList(instructionIndex,
                                    instructions.size()),
                            adcList);

                    /*
                     * The instructions are malformed if there is no type 2
                     * trailer
                     */ 
                    if ((trailer2Index >= instructions.size())
                            || (instructions.get(trailer2Index).getCardType()
                                    != 2)){
                        // malformed input; give up
                        break instructions;
                    }
                    
                    /*
                     * Analyze ADCs in pairs to remove pairs that refer to
                     * hydrogen atoms
                     */
                    
                    adcCount = adcList.size();
                    
                    bonds:
                    for (int bondIndex = 0; bondIndex + 1 < adcList.size(); ) {
                        List<AtomDesignatorCode> bond
                                = adcList.subList(bondIndex, bondIndex + 2);
                        int atomNumber0 = bond.get(0).getAtomNumber();
                        int atomNumber1 = bond.get(1).getAtomNumber();
                        
                        for (NumberRun run : numberRuns) {
                            if (run.contains(atomNumber0)
                                    || run.contains(atomNumber1)) {
                                
                                // Note: reduces adcList.size():
                                bond.clear();
                                continue bonds;
                            }
                        }
                        
                        // Only if the current bond was retained:
                        bondIndex += 2;
                    }

                    // Were any ADCs removed?
                    if (adcList.size() != adcCount) {
                        
                        // Are any ADCs left?
                        if (adcList.size() > 0) {
                            List<OrtepInstructionCard> thisInstruction
                                    = instructions.subList(
                                            instructionIndex, trailer2Index);

                            /*
                             * Remove the new instruction card and its type 1
                             * trailer cards from the list
                             */
                            thisInstruction.clear();
                            
                            /*
                             * Pack the surviving ADCs as tightly as possible
                             * into a new instruction card and associated type 1
                             * trailer cards, inserting them in place of the
                             * original cards.  By using the tightest possible
                             * packing we are certain to not exceed the number
                             * of cards originally used.
                             */
                            
                            thisInstruction.add(new NewInstructionCard(
                                    instructionNumber,
                                    extractToBondParameters(adcList)));
                            
                            while (!adcList.isEmpty()) {
                                thisInstruction.add(new ExtraParameterCard(1,
                                        extractToBondParameters(adcList)));
                            }
                        } else {
                            
                            /*
                             * No ADCs are left.  Remove the entire instruction,
                             * including all trailer cards
                             */
                            
                            instructions.subList(
                                    instructionIndex, trailer2Index + 1).clear();
                            
                            /*
                             * The next instruction to consider will be at the
                             * same index that this one was; counter the
                             * post-loop increment.
                             */
                            instructionIndex--;
                        }
                    }
                }  // end card type
            }  // end new instruction card
        }  // end all cards
    }

    /**
     * Reads ORTEP instruction cards from the specified list, extracting from
     * them the ADCs specified as card parameters, until the first type 2
     * trailer card is encountered or any new instruction card is encountered
     * except for the initial card.
     *
     * @param instructions a {@code List<? extends OrtepInstructionCard>}
     *        containing the cards to examine
     * @param adcList a {@code List<? super AtomDesignatorCode>} to which the
     *        extracted ADCs should be added
     * 
     * @return the number of cards from which ADCs were extracted
     */
    private int extractADCParameters(
            List<? extends OrtepInstructionCard> instructions,
            List<? super AtomDesignatorCode> adcList) {
        boolean isFirst = true;
        int cardsHandled = 0;
        
        // Collect bond endpoint ADCs and count the ADC-bearing cards
        cards:
        for (OrtepInstructionCard card : instructions) {
            if ((card.getCardType() > 1)
                    || ((card.getCardType() == 0) && !isFirst)) {
                break cards;
            } else {
                String parameters
                        = ((ParameterBearingCard) card).getParameters();
                
                cardsHandled++;
                
                if (parameters != null) {
                    
                    // trim parameter string
                    if (parameters.length() > ORTEP_MAX_PARAMS_LENGTH) {
                        parameters = parameters.substring(
                                0, ORTEP_MAX_PARAMS_LENGTH);
                    }
                    
                    // parse out individual parameters
                    while (parameters.length() > 0) {
                        String parameter = parameters.substring(0,
                                Math.min(ORTEP_PARAM_LENGTH,
                                        parameters.length()));
                        
                        parameters = parameters.substring(
                                parameter.length());
                        
                        // Ignore blank parameter slots
                        if (parameter.trim().length() == 0) {
                            continue;
                        }
                        
                        try {
                            adcList.add(AtomDesignatorCode.valueOf(parameter));
                        } catch (IllegalArgumentException iae) {
                            // Skip the rest of the card after any syntax error
                            continue cards;
                        }
                    }
                    
                }
            }
        }
    
        return cardsHandled;
    }

    /**
     * Extracts up to seven elements of the specified list and formats them into
     * a String suitable for use as the parameters on an ORTEP instruction card
     * or type 1 trailer card
     *
     * @param adcList a {@code List<AtomDesignatorCode>} of the ADCs to format;
     *        the elements used are removed from this list
     *        
     * @return a {@code String} containing the formatted parameters
     */
    private String extractToBondParameters(List<AtomDesignatorCode> adcList) {
        List<AtomDesignatorCode> adcsToUse = adcList.subList(0,
                Math.min(adcList.size(), MAX_PARAMS_PER_CARD));
        StringBuilder params
                = new StringBuilder(ORTEP_PARAM_LENGTH * MAX_PARAMS_PER_CARD);
        
        for (AtomDesignatorCode adc : adcsToUse) {
            StringBuilder sb = new StringBuilder(adc.toString());
            
            while (sb.length() < ORTEP_PARAM_LENGTH) {
                sb.insert(0, ' ');
            }
            
            params.append(sb, 0, ORTEP_PARAM_LENGTH);
        }
        
        adcsToUse.clear();
        
        return params.toString();
    }

    /**
     * Removes from among the provided ORTEP instruction cards all the bond and
     * atom label instructions referencing atoms from the specified number runs. 
     *
     * @param numberRuns a {@code List<NumberRun>} of the number runs describing
     *        the atoms whose labels are to be removed
     * @param instructions a {@code List<OrtepInstructionCard>} of the ORTEP
     *        instructions from which labels matching the number runs should be
     *        removed
     */
    private void removeLabels(List<NumberRun> numberRuns,
            List<OrtepInstructionCard> instructions) {
        boolean deleteMode = false;
        
        // Find instances of the affected instructions
        instructions:
        for (Iterator<OrtepInstructionCard> cardIterator = instructions.iterator();
                cardIterator.hasNext(); ) {
            OrtepInstructionCard card = cardIterator.next();
            
            if (card instanceof NewInstructionCard) {
                NewInstructionCard instruction = (NewInstructionCard) card;
                int instructionNumber = instruction.getInstruction();
                int minorNumber = instructionNumber % 10;
                
                // Default to not deleting this (new) instruction
                deleteMode = false;

                // Is it a label-plotting instruction that references atoms?
                if (((instructionNumber / MAJOR_NUMBER_INCREMENT)
                        == LABEL_MAJOR_NUMBER)
                        && (minorNumber != TITLE_LABEL_MINOR)
                        && (minorNumber != TITLE_LABEL_MINOR_ALT)) {
                    String parameters = instruction.getParameters();

                    // Does it have parameters (probably an error if it doesn't)
                    if (parameters != null) {
                        
                        // Compare one or two ADCs to the number runs?
                        int toCompare
                                = ((minorNumber == ATOM_LABEL_MINOR) ? 1 : 2);
                        
                        // For each ADC to compare ...
                        compare_loop:
                        for (int i = 0; i < toCompare; i++) {
                            int atomNumber;
                            
                            // Extract the parameter
                            String parameter = parameters.substring(0,
                                    Math.min(ORTEP_PARAM_LENGTH,
                                            parameters.length()));
                        
                            parameters = parameters.substring(parameter.length());
                            
                            // Get the atom number from the (assumed) ADC
                            try {
                                AtomDesignatorCode adc = 
                                        AtomDesignatorCode.valueOf(parameter);
                                
                                atomNumber = adc.getAtomNumber();
                            } catch (IllegalArgumentException iae) {
                                continue instructions;
                            }

                            /*
                             * Test whether any of the number runs contains the
                             * atom number
                             */
                            for (NumberRun run : numberRuns) {
                                if (run.contains(atomNumber)) {
                                    // This is one we should delete
                                    deleteMode = true;
                                    break compare_loop;
                                }
                            }
                        }  // end compare_loop
                    }
                }
            }

            /*
             * Is the current card is part of an instruction we're deleting? 
             */
            if (deleteMode) {
                cardIterator.remove();
            }
        }  // end instructions loop
    }

    /**
     * Creates a collection of ORTEP atom removal cards appropriate for removing
     * all atoms having any of the specified atom numbers from the atoms list.
     *
     * @param atomNumberRuns a {@code List} of {@code NumberRun} objects
     *        collectively representing the atom numbers of all the atoms to
     *        remove
     *        
     * @return a {@code List} of {@code OrtepInstructionCard}s appropriate for
     *        instructing ORTEP to perform the removal
     */
    private List<OrtepInstructionCard> createRemovalCards(
            List<NumberRun> atomNumberRuns) {
        int symmetryCount = Math.max(1, 
                adcGenerator.getBaseModel().getSymmetryOperations().size());
        List<OrtepInstructionCard> cards
                = new ArrayList<OrtepInstructionCard>();
        Formatter formatter = null;
        int runCount = 0;
        
        runs:
        for (NumberRun run : atomNumberRuns) {
            AtomDesignatorCode adcStart = new AtomDesignatorCode(
                    run.getStart(), LOWER_BOUNDS, 1);
            
            /*
             * The end atom number is clipped to the maximum supported by
             * ORTEP, partly to ensure that the ADCs do not overrun their format
             */
            AtomDesignatorCode adcEnd = new AtomDesignatorCode(
                    Math.min(run.getEnd(), ORTEP_MAX_ATOM_NUMBER),
                    UPPER_BOUNDS, symmetryCount);
            
            /*
             * Ignore degenerate runs. There are various reasons that such might
             * appear; one of them is the clipping (above) of the run's ending
             * atom number.
             */
            if (adcStart.getAtomNumber() > adcEnd.getAtomNumber()) {
                continue runs;
            }
            
            /*
             * Add this run to the current parameter list, creating a new one
             * if necessary 
             */
            
            if (formatter == null) {
                formatter = new Formatter(
                        new StringBuilder(ORTEP_MAX_PARAMS_LENGTH));
            }
            formatter.format("%9s%9s", adcStart.toString(),
                    "-" + adcEnd.toString());
            
            /*
             * Increment the run count, and output the card if it's full
             */
            runCount++;
            if ((runCount % RUNS_PER_CARD) == 0) {
                if (((runCount / RUNS_PER_CARD) % MAX_BLOCK_SIZE) == 1) {
                    
                    /*
                     * Need a new instruction card; the previous one (if any)
                     * is full
                     */
                    cards.add(new NewInstructionCard(
                            REMOVE_ATOMS, formatter.toString()));
                } else {
                    
                    /*
                     * We can use a continuation card; this may substantially
                     * reduce the total number of distinct instructions issued
                     */
                    cards.add(new ExtraParameterCard(1, formatter.toString()));
                }
                
                formatter = null;
            }
        }
        if (formatter != null) {
            
            /*
             * There are parameters left over that were not yet written to ORTEP
             * cards; we don't want to lose them.
             * 
             * We could use a continuation card instead of a new instruction
             * card in some cases, but a new instruction card is always OK.
             */
            cards.add(new NewInstructionCard(
                    REMOVE_ATOMS, formatter.toString()));
        }
        
        // All done; return the list of cards
        return cards;
    }

    /**
     * Inserts the contents of the specified list of source ORTEP instruction
     * cards into the target list, immediately after each complete sequence of
     * atom list (400-series) instructions 
     *
     * @param removalCards a {@code List} of the atom removal cards to insert
     * @param instructions a {@code List} of the cards into which the removal
     *        cards should be inserted
     */
    private void insertRemovalCards(List<OrtepInstructionCard> removalCards,
            List<OrtepInstructionCard> instructions) {
        boolean atomListInstruction = false;
        
        for (int i = 0; i < instructions.size(); i++) {
            OrtepInstructionCard card = instructions.get(i);
            
            if (card instanceof NewInstructionCard) {
                NewInstructionCard instruction = (NewInstructionCard) card;
                boolean nextIsAtomListInstruction =
                        ((instruction.getInstruction() / 100) == 4);
                
                if (atomListInstruction && !nextIsAtomListInstruction) {
                    instructions.addAll(i, removalCards);
                    i += removalCards.size();
                }
                
                atomListInstruction = nextIsAtomListInstruction;
            }
        }
    }

    /**
     * Processes an {@code OrtFile} to produce a new one into which the
     * specified image parameters have been appropriately inserted, and which
     * conforms to the requirements and recommended conventions of ORTEP-III.
     * This method is especially intended to work with ORTEP instructions as
     * generated by {@link #createOrtepInstructions(MolecularModel)}, but it
     * also will work with hand-created instruction sets and edited versions of
     * instruction sets created by {@code createOrtepInstructions()}. It will
     * handle any {@code OrtFile}, but it is not guaranteed to correct all
     * possible malformations. The resulting instructions are designed to be
     * used directly as input to the site software's on-line ORTEP III engine.
     * 
     * @param ort an {@code OrtFile} containing the instructions to filter
     * @param parameters an {@code ImageParameters} object containing
     *        user-specified parameters of the desired drawing; at this version,
     *        only the rotation angles from this object are used
     * 
     * @return an {@code OrtFile} containing the filtered instructions
     */
    private OrtFile filterInstructions(OrtFile ort,
            ImageParameters parameters) {
        OrtFile filtered = new OrtFile();

        // process the instructions
        boolean read202 = false;
        boolean readEnd = false;

        for (OrtepInstructionCard instruction : ort.getInstructions()) {
            if (instruction instanceof NewInstructionCard) {
                NewInstructionCard card = (NewInstructionCard) instruction;

                switch (card.getInstruction()) {
                    case END_INSTRUCTIONS:  // Make sure there is an END_PLOT
                        if (!read202) {
                            filtered.addInstruction(
                                    new NewInstructionCard(END_PLOT, null));
                        }
                        readEnd = true;
                        break;
                    case DEPRECATED_END_PLOT:  // Use the new END_PLOT
                        instruction = new NewInstructionCard(END_PLOT,
                                card.getParameters());
                        /* fall through */
                    case END_PLOT:
                        read202 = card.getParameters().trim().equals("");
                        break;
                    case PAGE_SIZE:  // Substitute the default page size
                        instruction = DEFAULT_PAGE_SIZE;
                        break;
                    case REFERENCE_AXES:  // Substitute the default axes
                        instruction = DEFAULT_REFERENCE_AXES;
                        break;
                    case AXIS_ROTATIONS:  // Insert the rotations
                        Formatter f = new Formatter(
                                new StringBuilder(54), Locale.US);

                        f.format("        1%9.2f        2%9.2f        3%9.2f",
                                parameters.getXRotation(),
                                parameters.getYRotation(),
                                parameters.getZRotation());
                        f.flush();

                        instruction = new NewInstructionCard(
                                AXIS_ROTATIONS, f.out().toString());
                        break;
                    case DEPRECATED_STORE_OUTLINES:  // Substitute the new STORE_OUTLINES
                        instruction = new NewInstructionCard(
                                STORE_OUTLINES, card.getParameters());
                        break;
                    case MANUAL_POSITION_AND_SCALE:
                        /* fall through */
                    case MANUAL_POSITION:
                        /* fall through */
                    case MANUAL_SCALE:  // Always use AUTO_POSITION_AND_SCALE
                        instruction = new NewInstructionCard(
                                AUTO_POSITION_AND_SCALE, card.getParameters());
                        break;
                    case INCREMENT_POSITION_AND_SCALE:
                        /* fall through */
                    case INCREMENT_SCALE:  // insert deltaScale1 if missing
                        String cardParameters = card.getParameters();
                        int paramsLength = cardParameters.length();
                        int deltaStart = Math.min(18, paramsLength);
                        int tailStart = Math.min(27, paramsLength);
                        String deltaScale = cardParameters.substring(
                                deltaStart, tailStart);
                        String tail = cardParameters.substring(tailStart);
                        
                        if (deltaScale.trim().length() == 0) {
                            instruction = new NewInstructionCard(
                                    card.getInstruction(),
                                    padRight(cardParameters.substring(0, deltaStart), 18)
                                            + "      0.9"
                                            + tail);
                        }
                        break;
                }
            }
            filtered.addInstruction(instruction);
        }

        if (!readEnd) {  // Make sure there's an END_INSTRUCTIONS
            if (!read202) {  // Make sure there's an END_PLOT
                filtered.addInstruction(new NewInstructionCard(END_PLOT, null));
            }
            filtered.addInstruction(
                    new NewInstructionCard(END_INSTRUCTIONS, null));
        }

        return filtered;
    }

    /**
     * returns a version of the input string padded on the left with blanks to
     * the specified length
     * 
     * @param s the {@code String} to be padded
     * @param len the length requested of the padded string
     * @return a {@code String} ending with {@code s}, of length at least
     *         {@code len}; if the length of {@code s} is less than {@code len}
     *         then the result starts with enough blanks to make its length
     *         {@code len}
     */
    private static String padLeft(String s, int len) {
        StringBuilder sb = new StringBuilder((s == null) ? "" : s);
        sb.ensureCapacity(len);
        while (sb.length() < len) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }

    /**
     * returns a version of the input string padded on the right with blanks to
     * the specified length
     * 
     * @param s the {@code String} to be padded; {@code null} is treated the
     *            same as an empty string
     * @param len the length requested of the padded string
     * 
     * @return a {@code String} starting with {@code s}, of length at least
     *         {@code len}; if the length of {@code s} is less than {@code len}
     *         then the result ends with enough blanks to make its length
     *         {@code len}
     */
    private static String padRight(String s, int len) {
        if ((s != null) && (s.length() >= len)) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder((s == null) ? "" : s);

            sb.ensureCapacity(len);
            while (sb.length() < len) {
                sb.append(' ');
            }
            return sb.toString();
        }
    }
    
    /**
     * Creates an {@code OrtepInputMaker} using atomic coordinate, unit cell,
     * and symmetry information from the specified SDT.  Such an input maker is
     * then suitable for creating ORTEP inputs for models whose atoms correspond
     * exactly to those of the SDT model, but unsuitable for other models.
     * 
     * @param  sdt an {@code SdtFile&lt;FractionalAtom&gt;} from which to
     *         derive this input maker's base model
     *         
     * @return an {@code OrtepInputMaker} based on model information from the
     *         specified SDT
     */
    public static OrtepInputMaker forSDT(SdtFile<FractionalAtom> sdt) {
        return OrtepInputMaker.forModel(sdt.getModel(), false);
    }
    
    /**
     * Creates an {@code OrtepInputMaker} using atomic coordinate, unit cell,
     * and symmetry information from the specified CIF data block.  Such an
     * input maker is then suitable for creating ORTEP inputs for models whose
     * atomic site codes correspond to the atom site labels and model symmetry
     * of the data block, or whose atom labels can be mapped to those of the
     * CIF model by removing whitespace and parentheses.
     * 
     * @param  cifDataBlock a {@code DataBlock} from which to derive this input
     *         maker's base model
     *         
     * @return an {@code OrtepInputMaker} based on model information from the
     *         specified SDT
     *         
     * @throws IllegalArgumentException if the specified data block does not
     *         contain enough information -- the unit cell parameters, atom
     *         coordinates, and bond list are all required
     */
    public static OrtepInputMaker forCIF(DataBlock cifDataBlock) {
        return OrtepInputMaker.forModel(
                new ModelBuilder(cifDataBlock).buildSimpleModel(), true);
    }
    
    /**
     * Creates an {@code OrtepInputMaker} using atomic coordinate, unit cell,
     * and symmetry information from the specified molecular model.  The
     * required characteristics of models that the returned input maker can use
     * depends on the {@code useSiteCodes} parameter; if {@code false} then the
     * requirements for compatible models correspond to those described under
     * {@link #forSDT(SdtFile)}; otherwise they correspond to those described
     * under {@link #forCIF(org.recipnet.common.files.CifFile.DataBlock)
     * forCif(DataBlock)}.
     * 
     * @param  model a {@code MolecularModel&lt;FractionalAtom,
     *         Bond&lt;FractionalAtom&gt;&gt;} from which to obtain base model
     *         parameters
     * @param  useSiteCodes {@code true} if the returned input maker should be
     *         configured to use symmetry information and site codes when
     *         interpreting models to make input files; {@code false} if it
     *         should simply match model atoms to base atoms in sequence
     *         
     * @return an {@code OrtepInputMaker} configured according to the specified
     *         base model and input-file-making strategy
     */
    public static OrtepInputMaker forModel(
            MolecularModel<FractionalAtom, Bond<FractionalAtom>> model,
            boolean useSiteCodes) {
        return new OrtepInputMaker(useSiteCodes
                ? SiteCodeBasedADCGenerator.forModel(model)
                : new SequenceBasedADCGenerator(model));
    }
}
