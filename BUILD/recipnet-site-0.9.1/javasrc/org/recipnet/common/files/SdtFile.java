/*
 * Reciprocal Net Project
 *
 * SdtFile.java
 *
 * 29-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.recipnet.common.Element;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.MolecularModel;
import org.recipnet.common.molecule.AtomicMotion;
import org.recipnet.common.molecule.AtomicMotionFactory;

/**
 * <p>
 * A class representing the data content of an SDT file, and providing a method
 * to read that data in SDT format (but not, in this version, to write it).  SDT
 * is a local format used in the IUMSC.
 * </p> 
 * 
 * @param  <A> the type of atom by which an instance is characterized
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class SdtFile <A extends FractionalAtom> {

    /**
     * A {@code Pattern} matching an initial alphabetic portion of an atom
     * label for the purpose of extracting an element symbol from it
     */
    private final static Pattern LABEL_PATTERN
            = Pattern.compile("([A-Z])([A-Za-z]?)");

    /**
     * The title of this {@code SdtFile}
     */
    private String title;

    /**
     * A {@code MolecularModel} representing the atoms and symmetry elements of
     * this {@code SdtFile}.
     */
    private final MolecularModel<A, Bond<A>> model;
    
    /**
     * Initializes an {@code SdtFile} with the specified model and title
     * 
     * @param  model a {@code MolecularModel<? extends FractionalAtom, ?>}
     *         representing the molecular model described by this
     *         {@code SdtFile}.  The specified model object is stored in this
     *         SDT file object.  The SDT format does not represent bonds, so
     *         any bonds in the model are ignored.  This argument must not be
     *         {@code null}; it need not contain any atoms, but it should
     *         contain any relevant symmetry operations.  Any atoms it does
     *         contain should have {@code AtomicMotion} objects assigned.
     * @param  title the title for this {@code SdtFile}
     */
    public SdtFile(MolecularModel<A, Bond<A>> model, String title) {
        if (model == null) {
            throw new NullPointerException("null model");
        }
        
        this.model = model;
        this.title = title;
    }
    
    /**
     * Returns the title of this {@code SdtFile}
     * 
     * @return the title of this {@code SdtFile}, a {@code String}; may be
     *         {@code null}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this {@code SdtFile}
     * 
     * @param  title the {@code String} to set as the title; may be {@code null}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the molecular model represented by this {@code SdtFile}.
     * 
     * @return the model represented by this SDT file, as a
     *         {@code MolecularModel<? extends FractionalAtom,?>}.  SDT does not
     *         represent bonds, so clients should not expect this model to
     *         contain any (though it may do, depending on how this
     *         {@code SdtFile} was created)
     */
    public MolecularModel<A, Bond<A>> getModel() {
        return model;
    }

    /**
     * Creates an {@code SdtFile} object from data read from the specified
     * {@code Reader}.  The data should be in SDT format.  Reading stops after
     * the end-of-atoms sentry line, before reading the scale factor or optional
     * extinction parameter.
     * 
     * @param  input the {@code Reader} from which the SDT-format input data are
     *         to be read
     * 
     * @return an {@code SdtFile} representing the SDT-format content read from
     *         the reader
     * 
     * @throws IOException if an I/O error occurs while reading the data
     * @throws ParseException if the data are not in SDT format
     */
    public static SdtFile<FractionalAtom> readFrom(Reader input)
            throws IOException, ParseException {
        BufferedReader in = new BufferedReader(input);
        MolecularModel<FractionalAtom, Bond<FractionalAtom>> model
                = new MolecularModel<FractionalAtom, Bond<FractionalAtom>>();
        String title;
        CoordinateSystem unitCell;
        int numElements;
        int numSymmetry;
        Map<Integer, Element> scatFacMap;
        String line;
        
        // Read the title
        title = nextLine(in);
        if (title.length() == 0) {
            title = nextLine(in);
        }

        // Read the cell parameters
        unitCell = parseUnitCell(nextLine(in));  // cell parameters
        model.setCell(unitCell);
        
        // Skip the parameter errors and space group symbol
        nextLine(in);         // parameter errors
        nextLine(in);         // space group symbol
        
        // Read the control integers
        line = nextLine(in);  // control integers
        numElements = parseInt(line, 0, 4);
        numSymmetry = parseInt(line, 4, 4);
        
        // Read the scattering factor cards
        scatFacMap = readScatteringFactors(in, numElements);
        if (scatFacMap == null) {
            scatFacMap = new HashMap<Integer, Element>();
            
            /*
             * Read the empirical formula cards and populate the scattering
             * factor map
             */
            readFormula(in, numElements, scatFacMap);
        } else {
            
            // Read and ignore the empirical formula cards
            readFormula(in, numElements, null);
        }
        
        // Skip the LASL operators
        nextLine(in);         // LASL line 1
        nextLine(in);         // LASL line 2
        
        // Read the symmetry cards
        readSymmetry(in, numSymmetry, model);
        
        // Read the atoms and thermal parameters
        readAtoms(in, unitCell, scatFacMap, model);
        
        return new SdtFile<FractionalAtom>(model, title);
    }
    
    /**
     * Reads the specified number of scattering factor cards from the specified
     * SDT-format character stream, and formulates and returns a corresponding
     * map from one-based scattering factor number to corresponding element
     * 
     * @param  in a {@code BufferedReader} from which to read the scattering
     *         factor data
     * @param  numElements the expected number of scattering factors
     * 
     * @return a {@code Map<Integer, Element>} describing the relationship
     *         from scattering factor indices and elements; {@code null} if any
     *         of the scattering factor cards has a missing or incomplete
     *         element label 
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the scattering factors do not conform to SDT
     *         conventions -- specifically, if any are non-blank but do not
     *         contain a valid chemical element symbol in the field designated
     *         for that purpose
     */
    private static Map<Integer, Element> readScatteringFactors(
            BufferedReader in, int numElements)
            throws IOException, ParseException {
        Map<Integer, Element> scatFacMap = new HashMap<Integer, Element>();
        Map<Integer, Element> workingMap = scatFacMap;
        
        scattering:
        for (int i = 1; i <= numElements; i++) {
            String symbol;
            String line = nextLine(in); // first line for this scattering factor
            
            nextLine(in);               // second line (ignored)
            
            if (line.trim().length() < 4) {
                // At least one scattering factor card had no atomic symbol
                scatFacMap = null;
                continue scattering;
            }
            
            if (line.startsWith("CVAL")) {
                symbol = "C";
            } else {
                String rawSymbol = parseString(line, 0, 4);
                Element el;
                
                switch (rawSymbol.length()) {
                    case 1:
                        symbol = rawSymbol.toUpperCase();
                        break;
                    case 2:
                        symbol = rawSymbol.substring(0, 1).toUpperCase()
                                + rawSymbol.substring(1, 2).toLowerCase();
                        break;
                    default:
                        throw new ParseException(
                                "Invalid scattering factor card: " + line, 0);
                }
                
                el = Element.forSymbol(symbol);
                
                if (el == null) {
                    throw new ParseException(
                            "Invalid scattering factor card: " + line, 0);
                } else {
                    workingMap.put(i, el);
                }
            }
        }
        
        return scatFacMap;
    }

    /**
     * Reads the empirical formula line(s) from the input; if the provided map
     * is non-{@code null} then the sequence of atomic numbers on these
     * cards is assumed to correspond to the scattering factor number sequence,
     * and is used to populate the map.  
     * 
     * @param  in a {@code BufferedReader} from which to read the formula data;
     *         should not be {@code null}
     * @param  numElements the number of elements for which formula data are
     *         expected
     * @param  scatFacMap a {@code Map<Integer, Element>} intended for recording
     *         the association between scattering factor numbers and elements;
     *         if {@code null} then this argument is ignored, otherwise it is
     *         populated with mappings based on the sequence of atomic numbers
     *         in the empirical formula line(s) 
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the empirical formula lines do not adhere to
     *         valid SDT syntax
     */
    private static void readFormula(BufferedReader in, int numElements,
            Map<Integer, Element> scatFacMap)
            throws IOException, ParseException {
        
        // Read the empirical formula cards
        for (int sfBase = 0; sfBase < numElements; sfBase += 8) {
            String line = nextLine(in);  // one line of the empirical formula
            
            if (scatFacMap != null) {
                int lineElements = Math.min(8, numElements - (8 * sfBase));
                
                for (int index = 0; index < lineElements; index++) {
                    int offset = index * 8;
                    int atomicNumber = parseInt(line, offset, 4);
                    Element el = Element.forAtomicNumber(atomicNumber);
    
                    if (el == null) {
                        throw new ParseException(
                                "Invalid empirical formula card: " + line,
                                offset);
                    } else { 
                        scatFacMap.put(sfBase * 8 + offset + 1, el);
                    }
                }
            }
        }
    }

    /**
     * Reads the specified number of symmetry descriptions from the SDT-format
     * data in the provided reader, and populates the provided molecular model
     * with them. 
     * 
     * @param  in a {@code BufferedReader} from which to read the symmetry
     *         operation lines
     * @param  numSymmetry the number of symmetry operations to read
     * @param  model a {@code MolecularModel<FractionalAtom, ?>} to which the
     *         symmetry operations should be added (in the order read)
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the symmetry operation lines do not conform to
     *         SDT syntax
     */
    private static void readSymmetry(BufferedReader in, int numSymmetry,
            MolecularModel<?, ?> model)
            throws IOException, ParseException {
        int[][] rotation = new int[3][3];
        int[] translation = new int[3];
        
        for (int n = 0; n < numSymmetry; n++) {
            String line = nextLine(in);  // One symmetry operation
            
            for (int i = 0; i < 3; i++) {
                int base = 24 * i;
                
                translation[i] =
                        (int) Math.round(parseDouble(line, base, 15) * 12);
                for (int j = 0; j < 3; j++) {
                    rotation[i][j] = (int) Math.round(
                            parseDouble(line, base + 15 + 3 * j, 3));
                }
            }
            
            model.addSymmetryOperation(
                    new SymmetryMatrix(rotation, translation));
        }
    }

    /**
     * Reads atom descriptions from the specified character stream in SDT
     * format, and populates the provided {@code MolecularModel} with
     * corresponding {@code FractionalAtom}s.
     * 
     * @param  in the {@code BufferedReader} from which to read the atom
     *         descriptions
     * @param  unitCell a {@code CoordinateSystem} representing the unit cell to
     *         which the fractional atomic coordinates are referred
     * @param  scatFacMap a {@code Map} from scattering factor indices to the
     *         corresponding {@code Element}, for use in assigning elements to
     *         atoms  
     * @param  model the {@code MolecularModel<FractionalAtom, ?>} to populate
     *         with the atoms read
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the data read deviate from SDT format
     */
    private static void readAtoms(BufferedReader in, CoordinateSystem unitCell,
            Map<Integer, Element> scatFacMap,
            MolecularModel<FractionalAtom, ?> model)
            throws IOException, ParseException {
        AtomicMotionFactory motionFactory = new AtomicMotionFactory(unitCell);
        
        atoms:
        for (;;) {
            String line = nextLine(in);  // first line of an atom
            
            if (parseInt(line, 8, 2) == 1) {
                
                // The end-of-atoms sentry card
                break atoms;
            } else {
                String label = parseString(line, 0, 8);
                int sfac = parseInt(line, 10, 3);
                boolean isotropic = (parseInt(line, 18, 1) == 0);
                double x = parseDouble(line, 40, 10);
                double y = parseDouble(line, 50, 10);
                double z = parseDouble(line, 60, 10);
                Element el = (scatFacMap.containsKey(sfac)
                                ? scatFacMap.get(sfac)
                                : extractElementFromLabel(label));
                FractionalAtom atom = new FractionalAtom(
                        label, el, x, y, z, unitCell, null);
                AtomicMotion motion;
                
                line = nextLine(in);  // second (thermal parameter) line
                if (isotropic) {
                    motion = motionFactory.motionForIsotropicB(
                            parseDouble(line, 0, 10));
                } else {
                    motion = motionFactory.motionForAnisotropicBeta(
                            parseDouble(line,  0, 10),
                            parseDouble(line, 10, 10),
                            parseDouble(line, 20, 10),
                            parseDouble(line, 30, 10),
                            parseDouble(line, 40, 10),
                            parseDouble(line, 50, 10)
                            );
                }
                
                atom.setAtomicMotion(motion);
                model.addAtom(atom);
            }
        }
    }

    /**
     * Obtains the next line available from the specified
     * {@code BufferedReader}, with the expectation that one is in fact present
     * 
     * @param  reader the {@code BufferedReader} from which to read a line
     * 
     * @return a {@code String} containing the characters read from the reader,
     *         up to but not including the line terminator (which is read and
     *         discarded).
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the reader indicates that it had already
     *         reached the end of its data 
     */
    private static String nextLine(BufferedReader reader)
            throws IOException, ParseException {
        String line = reader.readLine();
        
        if (line == null) {
            throw new ParseException("Premature end of file", 0);
        } else {
            return line;
        }
    }

    /**
     * Parses a string formatted according to the conventions for SDT unit cell
     * parameters, and returns a {@code CoordinateSystem} object representing
     * the unit cell described by the coordinates found
     * 
     * @param  line a {@code String} containing the SDT-format unit cell data
     * 
     * @return a {@code CoordinateSystem} object representing the
     *         crystallographic coordinate system corresponding to the unit cell
     *         parameters found on the specified line
     * 
     * @throws ParseException if the specified line does not conform to the
     *         SDT format for the unit cell parameters
     */
    private static CoordinateSystem parseUnitCell(String line)
            throws ParseException {
        double a = parseDouble(line, 0, 10);
        double b = parseDouble(line, 10, 10);
        double c = parseDouble(line, 20, 10);
        double cosAlpha = parseDouble(line, 30, 10);
        double cosBeta = parseDouble(line, 40, 10);
        double cosGamma = parseDouble(line, 50, 10);
        
        return new CoordinateSystem(a, b, c, cosAlpha, cosBeta, cosGamma);
    }

    /**
     * Parses a portion of the specified string to obtain a {@code double}
     * value, accommodating partially or wholly missing fields in a manner
     * consistent with Fortran formatted I/O.  In particular, fields that extend
     * across the end of the string are handled by implicitly narrowing them,
     * those wholly beyond the end of the string are treated as if they
     * contained "0", and those containing only blanks are also treated as if
     * they contained "0".
     * 
     * @param  s a {@code String} containing the field to parse; should not be
     *         {@code null}
     * @param  offset the offset in the specified string of the first character
     *         of the field to parse
     * @param  width the width of the field to parse
     * 
     * @return the {@code double} value parsed from the specified field
     * 
     * @throws ParseException if the specified field cannot be parsed as a
     *         {@code double}
     */
    private static double parseDouble(String s, int offset, int width)
            throws ParseException {
        assert (offset >= 0);
        assert (width > 0);
        
        int fieldEnd = Math.min(s.length(), offset + width);
        
        if (fieldEnd <= offset) {
            return 0.0;
        } else {
            String field = s.substring(offset, fieldEnd).trim();
            
            try {
                return ((field.length() > 0) ? Double.parseDouble(field) : 0.0);
            } catch (NumberFormatException nfe) {
                throw new ParseException(nfe.getMessage(), offset);
            }
        }
    }
    
    /**
     * Parses a portion of the specified string to obtain an {@code int}
     * value, accommodating partially or wholly missing fields in a manner
     * consistent with Fortran formatted I/O.  In particular, fields that
     * extend across the end of the string are handled by implicitly narrowing
     * them, those wholly beyond the end of the string are treated as if they
     * contained "0", and those containing only blanks are also treated as if
     * they contained "0".
     * 
     * @param  s a {@code String} containing the field to parse; should not be
     *         {@code null}
     * @param  offset the offset in the specified string of the first character
     *         of the field to parse
     * @param  width the width of the field to parse
     * 
     * @return the {@code double} value parsed from the specified field
     * 
     * @throws ParseException if the specified field cannot be parsed as a
     *         {@code double}
     */
    private static int parseInt(String s, int offset, int width)
            throws ParseException {
        assert (offset >= 0);
        assert (width > 0);
        
        int fieldEnd = Math.min(s.length(), offset + width);
        
        if (fieldEnd <= offset) {
            return 0;
        } else {
            String field = s.substring(offset, fieldEnd).trim();
            
            try {
                return ((field.length() > 0) ? Integer.parseInt(field) : 0);
            } catch (NumberFormatException nfe) {
                throw new ParseException(nfe.getMessage(), offset);
            }
        }
    }
    
    /**
     * Parses a portion of the specified string to obtain a logical substring,
     * accommodating partially or wholly missing fields.  The returned string
     * is automatically trimmed to remove leading and trailing blanks.  Any
     * characters of the field that would otherwise be beyond the end of the
     * string are treated as if they were blanks.
     * 
     * @param  s a {@code String} containing the field to parse; should not be
     *         {@code null}
     * @param  offset the offset in the specified string of the first character
     *         of the field to parse
     * @param  width the width of the field to parse
     * 
     * @return the {@code String} parsed from the specified field; may be
     *         empty, but will never be {@code null}
     */
    private static String parseString(String s, int offset, int width) {
        assert (offset >= 0);
        assert (width > 0);
        
        int fieldEnd = Math.min(s.length(), offset + width);
        
        return (fieldEnd <= offset) ? "" : s.substring(offset, fieldEnd).trim();
    }
    
    /**
     * A helper method that attempts to discern the chemical element to which an
     * atom line applies by analysis of the atom label.  Up to two leading
     * alphabetic characters are considered.  If if two are available then they
     * are considered is a possible two-character symbol.  If they do not
     * constitute a valid symbol, or if only one character is available, then
     * the first character by itself is considered as a possible symbol.
     * 
     * @param  label the atomic label to examine
     * 
     * @return the {@code Element} corresponding to the leading chemical element
     *         symbol in the label, if any, or {@code null} if there is no such
     *         element
     */
    private static Element extractElementFromLabel(String label) {
        Matcher labelMatcher = LABEL_PATTERN.matcher(label);
        Element el;
        
        if (labelMatcher.lookingAt()) {
            String symbol = labelMatcher.group(1).toUpperCase()
                    + labelMatcher.group(2).toLowerCase();
            
            el = Element.forSymbol(symbol);
            if ((el == null) && (symbol.length() > 1)) {
                el = Element.forSymbol(symbol.substring(0, 1));
            }
        } else {
            el = null;
        }
        
        return el;
    }
}
