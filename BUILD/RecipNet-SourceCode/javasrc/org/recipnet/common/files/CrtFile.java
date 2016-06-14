/*
 * Reciprocal Net Project
 * 
 * CrtFile.java
 *
 * 31-Dec-2003: ekoperda wrote first draft, borrowing concepts from 
 *              Rotate3DModel
 * 07-Nov-2005: jobollin lifted internal Atom, Bond, and Point classes into
 *              seperate files; added type arguments to internal collections;
 *              reformatted source to get rid of tabs and bring all line widths
 *              within 80 columns; switched to use of a MolecularModel instead
 *              of distinct atom and bond lists, including moving many
 *              model-specific functions into the MolecularModel class; removed
 *              unused imports; made the file reading code more forgiving; added
 *              missing Javadoc comments; added support for a model name read
 *              from the CARTESIAN header or set manually; removed stream
 *              buffering from the (InputStream) constructor; made that
 *              constructor stop reading (and ignoring) the CRT tail; removed
 *              the nullary constructor (near-useless with the chosen type
 *              parameterization); added a constructor that takes a
 *              MolecularModel argument
 * 22-Nov-2005: jobollin added method writeTo(Writer) and supporting private
 *              methods; added reading support for new CRT sections to parallel
 *              the writing support for those sections; replaced the
 *              (InputStream) constructor with new static method
 *              readFrom(Reader)
 */

package org.recipnet.common.files;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.recipnet.common.Element;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.algebra.LinearTransformation;
import org.recipnet.common.algebra.SimpleMatrix;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Point;
import org.recipnet.common.geometry.Vector;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;

/**
 * <p>
 * Encapsulates all the crystallographic information normally contained within a
 * .crt file. This includes a 3-D representation of atoms in a molecule and the
 * bonds between them. See the "Format specification for .crt files" section of
 * the <i>User Guide</i> for details about the file format.  For the most part,
 * this class serves as an I/O and wrapper object for a {@link MolecularModel}.
 * </p><p>
 * This class is not thread-safe.
 * </p>
 * 
 * @param  <A> the type of Atom in a {@code CrtFile}'s model
 */
public class CrtFile<A extends Atom> {
    
    /**
     * A {@code double} constant representing how close a computed value that is
     * expected to be integral must in fact be to an integer
     */
    private final static double INTEGER_TOLERANCE = 0.01;
    
    /**
     * The name / identifier listed in the CRT data
     */
    private String name;
    
    /**
     * A {@code MolecularModel} representing the bulk of the data content of
     * this {@code CrtFile}
     */
    private final MolecularModel<A, Bond<A>> model;

    /**
     * Initializes a {@code CrtFile} with the specified molecular model
     * 
     * @param  sourceModel a
     *         {@code MolecularModel&lt;A,
     *         ? extends Bond&lt;? extends A&gt;&gt;} that this
     *         {@code CrtFile} should use for its model (without copying), thus
     *         creating a {@code CrtFile} view of the specified model; should
     *         not be {@code null}
     */
    public CrtFile(MolecularModel<A, Bond<A>> sourceModel) {
        if (sourceModel == null) {
            throw new NullPointerException("Null model");
        }
        this.name = "";
        this.model = sourceModel;
    }
    
    /**
     * Returns the model name of this CRT file
     * 
     * @return the model name {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the model name of this CRT file
     * 
     * @param  name the {@code String} to set as the model name; should not be
     *         {@code null}
     */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("Null name not permitted");
        }
        this.name = name;
    }

    /**
     * Returns the molecular model represented by this CRT file
     * 
     * @return the model as a {@code MolecularModel&lt;A,
     *         ? extends Bond&lt;? extends A&gt;&gt;}
     */
    public MolecularModel<A, Bond<A>> getModel() {
        return model;
    }

    /**
     * Creates and returns a {@code CrtFile} based on the CRT-format data from
     * the specified character stream
     * 
     * @param  crtData the {@code Reader} from which CRT-format data should be
     *         read; the data from this stream will be read and consumed up to
     *         end-of-stream
     *         
     * @return a {@code CrtFile} corresponding to the CRT-format data on the
     *         specified stream 
     * 
     * @throws IOException if an error occurs while reading the stream
     * @throws ParseException if the input data do not constitute a valid
     *         instance of the CRT format. The {@code errorOffeset} field of the
     *         thrown exception object indicates the offending line number of
     *         the input file.
     */
    public static CrtFile<Atom> readFrom(Reader crtData)
            throws IOException, ParseException {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        CrtFile<Atom> crt;
        String name;

        // Set up a stream tokenizer to read crtData
        StreamTokenizer st = new StreamTokenizer(crtData);
        
        st.ordinaryChars('\u0021', '\uffff');
        st.wordChars('\u0021', '\uffff');
        st.eolIsSignificant(true);
        st.commentChar('#');
        st.parseNumbers();

        // Read the CARTESIAN section of the file.
        name = readCartesianHeader(st);
        readAtoms(st, model);
        readBonds(st, model);

        /* 
         * Read any remaining sections that we understand.  The readBonds()
         * method and all reader methods for optional sections are expected to
         * leave the tokenizer on *either* the EOL / EOF at the end of their
         * section or on the first token of the next section  
         */
        while (st.ttype != StreamTokenizer.TT_EOF) {
            
            // Consume an EOL if present
            if (st.ttype == StreamTokenizer.TT_EOL) {
                st.nextToken();
            }
            
            // Determine which section is next, and read it
            if ("CELL".equals(st.sval)) {
                
                // a CELL section; initialize the model unit cell
                readCell(st, model);
            } else if ("SYMMETRY".equals(st.sval)) {
                
                // a SYMMETRY section; initialize the model symmetry operations
                readSymmetry(st, model);
            } else {
                
                // Unrecognized line or section; read and discard it
                readToEOL(st);
            }
        }
        
        crt = new CrtFile<Atom>(model);
        crt.setName(name);
        
        return crt;
    }
    
    /**
     * Writes the data represented by this {@code CrtFile} to the specified
     * {@code Writer} in .CRT format
     * 
     * @param  writer the {@code Writer} to which the output should be directed
     * 
     * @throws IOException if an I/O error occurs
     */
    public void writeTo(Writer writer) throws IOException {
        
        // CRT formatting conventions correspond to the United States locale 
        Formatter formatter = new Formatter(writer, Locale.US);
        
        CoordinateSystem cell = model.getCell();
        Iterable<SymmetryMatrix> symmetryOps = model.getSymmetryOperations();
        IOException formatterException;
        
        // Write the CRT header
        writeCartesianHeader(formatter);
        
        // Write the atoms
        writeAtoms(formatter);
        
        // Write the bonds
        writeBonds(formatter);
        
        // Write the unit cell, origin, and symmetry if available
        if (cell != null) {
            writeCell(formatter, cell);
            
            // Write symmetry operations if available
            if (symmetryOps != null) {
                writeSymmetry(formatter, cell, symmetryOps);
            }
        }
        
        /*
         * Clean up; we can't close the Formatter because that would also close
         * the underlying Writer, which doesn't belong to us
         */ 
        formatter.flush();
        
        // propagate any exception that was previously trapped by the formatter
        formatterException = formatter.ioException();
        if (formatterException != null) {
            throw formatterException;
        }
    }
    
    /**
     * Helper function used when parsing the "CARTESIAN" section of a .crt file
     * to handle the section header.
     * 
     * @param  st a {@code StreamTokenizer} from which to read the CRT file
     *         contents
     *         
     * @return the model name {@code String} from the header
     *         
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream from the tokenizer cannot be
     *         parsed as valid CRT content
     */
    private static String readCartesianHeader(StreamTokenizer st)
            throws IOException, ParseException {
        StringBuilder sb = new StringBuilder();
        
        // Read the CATRESIAN marker.
        st.nextToken();
        assertWord(st, "CARTESIAN expected");
        if (!st.sval.equals("CARTESIAN")) {
            throw new ParseException("CARTESIAN expected", st.lineno());
        }

        // Read the atom count.  (advisory only)
        st.nextToken();
        assertNumber(st, "atom count expected");

        // Read the bond count.  (advisory only)
        st.nextToken();
        assertNumber(st, "bond count expected");

        // Any remaining tokens on the first line are taken as the model name
        while (st.nextToken() != StreamTokenizer.TT_EOL) {
            if (st.ttype == StreamTokenizer.TT_EOF) {
                throw new ParseException("CARTESIAN section incomplete",
                        st.lineno());
            } else {
                sb.append(st.sval).append(' ');
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * Reads the atoms from the "CARTESIAN" section of a .crt file and populates
     * the specified molecular model object with them
     *  
     * @param  st a {@code StreamTokenizer} from which to read the CRT file
     *         contents
     * @param  model a {@code MolecularModel<Atom, Bond>} to which the atoms
     *         should be added  
     *         
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream from the tokenizer cannot be
     *         parsed as valid CRT content
     */
    private static void readAtoms(StreamTokenizer st,
            MolecularModel<Atom, Bond<Atom>> model)
            throws IOException, ParseException {
        String label;
        double[] coordinates = new double[3];
        Element el;
        String tag;
        
        // Read each atom description.
        for (st.nextToken(); !st.sval.equals("ENDATOMS"); st.nextToken()) {

            // Read the atom's label.
            assertWord(st, "atom label expected");
            label = st.sval;

            // Read the atomic coordinates
            readCoordinates(st, coordinates);

            // Read the atom's atomic number.
            st.nextToken();
            assertNumber(st, "atomic number expected");
            el = Element.forAtomicNumber((int) st.nval);
            if (el == null) {
                throw new ParseException(
                        "Not a valid atomic number: " + st.nval, st.lineno());
            }

            st.nextToken();
            assertNotEOF(st, "Premature EOF");
            if (!(st.ttype == StreamTokenizer.TT_EOL)) {
                tag = st.sval;
            } else {
                tag = null;
            }
            
            // Consume remaining tokens until end-of-line.
            readToEOL(st);
            assertNotEOF(st, "Premature EOF");
            
            // Add the atom to the provided model
            model.addAtom(new Atom(label, el, new Point(coordinates), tag));
        }
        
        // Consume remaining tokens on the ENDATOMS line until end-of-line.
        readToEOL(st);
        assertNotEOF(st, "Premature EOF");
    }

    /**
     * Reads the bonds from the "CARTESIAN" section of a .crt file and populates
     * the specified molecular model object with them; atom indices specified by
     * the bond descriptions are expected to correspond with the atoms already
     * in the provided model, after accounting for the fact that the former are
     * one-based. 
     *  
     * @param  st a {@code StreamTokenizer} from which to read the CRT file
     *         contents
     * @param  model a {@code MolecularModel<Atom, Bond>} to which the atoms
     *         should be added  
     *         
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream from the tokenizer cannot be
     *         parsed as valid CRT content
     */
    private static void readBonds(StreamTokenizer st,
            MolecularModel<Atom, Bond<Atom>> model)
            throws IOException, ParseException {
        List<Atom> atoms = model.getAtoms();

        // Read each bond description.
        for (st.nextToken(); (st.ttype == StreamTokenizer.TT_NUMBER);
                st.nextToken()) {
            Atom atom1;
            Atom atom2;
            int index;

            // Read the first atom index.
            index = (int) st.nval;
            try {
                atom1 = atoms.get(index - 1);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new ParseException(
                        "bad atom index " + index, st.lineno());
            }
            
            // Read the second atom index.
            st.nextToken();
            assertNumber(st, "atom index expected");
            index = (int) st.nval;
            try {
                atom2 = atoms.get(index - 1);
            } catch (IndexOutOfBoundsException ioobe) {
                throw new ParseException(
                        "bad atom index " + index, st.lineno());
            }

            // Consume remaining tokens until end-of-line.
            readToEOL(st);
            assertNotEOF(st, "Premature EOF");
            
            model.addBond(new Bond<Atom>(atom1, atom2));
        }
        
        assertWord(st, "Missing bond section terminator");
        if (!st.sval.equals("ENDBONDS")) {
            throw new ParseException("Unexpected keyword in bond section: "
                    + st.sval, 0);
        }
        
        // Consume remaining tokens on the ENDBONDS line until end-of-line.
        readToEOL(st);
    }
    
    /**
     * Reads the "CELL" section of a .crt file and assigns a corresponding
     * {@code CoordinateSystem} object to this {@code CrtFile}'s model
     * 
     * @param  tokenizer the {@code StreamTokenizer} from which to obtain the
     *         unit cell data
     *         
     * @param  model the {@code MolecularModel} to update with a unit cell;
     *         should not already have had a unit cell assigned
     *         
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream cannot be parsed as valid CRT
     *         content
     */
    private static void readCell(StreamTokenizer tokenizer,
            MolecularModel<?, ?> model)
            throws IOException, ParseException {
        double[] coords = new double[3];
        Vector[] vectors = new Vector[3];
        Point origin;
        
        assert ("CELL".equals(tokenizer.sval));
        
        if (model.getCell() != null) {
            throw new IllegalStateException("Duplicate CELL sections");
        }
        
        readToEOL(tokenizer);
        assertNotEOF(tokenizer,
                "Premature end of file while reading the CELL section");
        
        readCoordinates(tokenizer, coords);
        origin = new Point(coords);
        tokenizer.nextToken();
        assertEOL(tokenizer, "Malformed CELL section");
        
        for (int i = 0; i < vectors.length; i++) {
            assertNotEOF(tokenizer,
                    "Premature end of file while reading the CELL section");
            readCoordinates(tokenizer, coords);
            vectors[i] = new Vector(coords);
            tokenizer.nextToken();
            assertEOL(tokenizer, "Malformed CELL section");
        }
        
        model.setCell(new CoordinateSystem(origin, vectors));
    }
    
    /**
     * Reads the "SYMMETRY" section of a .crt file and adds the symmetry
     * rotation matrices and translation vectors to the specified lists.  (These
     * cannot be converted to {@code SymmetryMatrix} objects without unit cell
     * data, which may not be available at this point.)
     * 
     * @param  tokenizer the {@code StreamTokenizer} from which to obtain the
     *         symmetry operation data
     * @param  model the {@code MolecularModel} to which symmetry operations
     *         should be added 
     *         
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream cannot be parsed as valid CRT
     *         content
     */
    private static void readSymmetry(StreamTokenizer tokenizer,
            MolecularModel<?, ?> model)
            throws IOException, ParseException {
        
        /*
         * In addition to reading the CRT-format symmetry operation data, this
         * method must perform a coordinate transformation on them (from the
         * model's Cartesian coordinates to crystallographic coordinates based
         * on the unit cell).  The actual transformation is delegated to the
         * addSymmetryMatrices() method. 
         */
        
        List<SimpleMatrix> rotations = new ArrayList<SimpleMatrix>();
        List<SimpleMatrix> translations = new ArrayList<SimpleMatrix>();
        
        double[][] matrix = new double[3][3];
        double[] vector = new double[3];
        
        assert ("SYMMETRY".equals(tokenizer.sval));
        
        tokenizer.nextToken();
        assertNumber(tokenizer, "No symmetry operation count");
        readToEOL(tokenizer);
        assertNotEOF(tokenizer, "Premature EOF while reading symmetry");
        
        for (tokenizer.nextToken(); !("ENDSYMM".equals(tokenizer.sval));
                tokenizer.nextToken()) {
            tokenizer.pushBack();
            for (int row = 0; row < 3; row++) {
                readCoordinates(tokenizer, matrix[row]);
                tokenizer.nextToken();
                assertEOL(tokenizer, "Malformed SYMMETRY section");
                assertNotEOF(tokenizer, "Premature EOF while reading symmetry");
            }
            readCoordinates(tokenizer, vector);
            tokenizer.nextToken();
            assertEOL(tokenizer, "Malformed SYMMETRY section");
            assertNotEOF(tokenizer, "Premature EOF while reading symmetry");
            
            rotations.add(new SimpleMatrix(matrix));
            translations.add(SimpleMatrix.createColumnMatrix(vector));
        }
        
        addSymmetryMatrices(rotations, translations, model);
    }

    /**
     * A low-level helper method for reading a set of three coordinates from the
     * specified tokenizer
     * 
     * @param  st the {@code StreamTokenizer} from which to obtain the
     *         coordinate data
     * @param  coordinates a {@code double[]} into which to store the coordinate
     *         data read from the stream
     *
     * @throws IOException if one is thrown by the tokenizer
     * @throws ParseException if the token stream cannot be parsed as valid CRT
     *         content
     */
    private static void readCoordinates(StreamTokenizer st,
            double[] coordinates)
            throws IOException, ParseException {
        
        // Read the x coordinate.
        st.nextToken();
        assertNumber(st, "x coordinate expected");
        coordinates[0] = st.nval;

        // Read the y coordinate.
        st.nextToken();
        assertNumber(st, "y coordinate expected");
        coordinates[1] = st.nval;

        // Read the z coordinate.
        st.nextToken();
        assertNumber(st, "z coordinate expected");
        coordinates[2] = st.nval;
    }

    /**
     * Adds symmetry matrices to this {@code CrtFile}'s model, based on the
     * supplied lists of corresponding rotation matrices and translation
     * vectors.  The matrices in the supplied lists are expected to be referred
     * to the same Cartesian coordinate system that the model's unit cell is
     * referred to; the unit cell is used to convert them into matrices for the
     * crystallographic (fractional) coordinate system.  The identity operation
     * is always added, and is not expected to be present in the list.
     *  
     * @param  symmRotations a {@code List<SimpleMatrix>} of the 3 by 3
     *         matrices representing the rotation components of the symmetry
     *         operations to add
     * @param  symmTranslations a {@code List<SimpleMatrix>} of the 1 by 3
     *         (column) matrices representing the translation components of the
     *         operations to add
     * @param  model the {@code MolecularModel} to which symmetry operations
     *         should be added  
     *          
     * @throws ParseException if any of the specified 
     */
    private static void addSymmetryMatrices(List<SimpleMatrix> symmRotations,
            List<SimpleMatrix> symmTranslations,
            MolecularModel<?, ?> model) throws ParseException {
        CoordinateSystem cell = model.getCell();
        
        assert (symmRotations != null);
        assert (symmTranslations != null);
        assert (symmRotations.size() == symmTranslations.size());
        
        // There is always the identity operation
        model.addSymmetryOperation(SymmetryMatrix.IDENTITY);
        
        // Add other operations if the data are available
        if ((cell != null) && (symmRotations.size() > 0)) {
            SimpleMatrix cartInverse = getCartesianRotation(cell).getInverse();
            LinearTransformation toCrystal = new LinearTransformation(
                    cartInverse,
                    cartInverse.times(getCartesianTranslation(cell)).times(-1)
                    );
            
            // Create and add all the matrices
            for (int i = 0; i < symmRotations.size(); i++) {
                LinearTransformation symmTransform = new LinearTransformation(
                        symmRotations.get(i), symmTranslations.get(i));
                LinearTransformation convertedSymmetry
                        = toCrystal.convertTransformation(symmTransform);
                
                model.addSymmetryOperation(createSymmetryOperation(
                        convertedSymmetry.getRotation().getElements(),
                        convertedSymmetry.getTranslation().getElements()));
            }
        }
    }
    
    /**
     * A helper method for converting symmetry matrices expressed as arrays of
     * doubles into corresponding {@code SymmetryMatrix} objects.
     * 
     * @param  rotationElements a {@code double[][]} containing the rotation
     *         matrix elements.  Though of type {@code double}, the elements
     *         should have very near integer values, as appropriate for a
     *         symmetry rotation matrix referred to the model's crystallographic
     *         coordinate system 
     * @param  translationElements a {@code double[][]} containing the
     *         translation vector components as the zero<sup>th</sup> element of
     *         each element.  Each component should be very near an integral
     *         number of twelfths (i.e. value * 12 should be very near an
     *         integer), as appropriate for a symmetry translation vector
     *         referred to the model's crystrallographic coordinate system
     * 
     * @return a {@code SymmetryMatrix} representing the operation described by
     *         the specified rotation matrix and translation vector
     * 
     * @throws ParseException if the specified rotation and translation elements
     *         cannot be interpreted as a {@code SymmetryMatrix}, including if
     *         their elements are too far away from the allowed values, or if
     *         they are recognized as not designating a valid symmetry
     *         operation. (See
     *         {@link SymmetryMatrix#SymmetryMatrix(int[][], int[], boolean)}.)  
     */
    private static SymmetryMatrix createSymmetryOperation(
            double[][] rotationElements, double[][] translationElements)
            throws ParseException {
        int[][] rotation = new int[3][3];
        int[] translation = new int[3];
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rotation[i][j] = roundIntegralDouble(rotationElements[i][j]);
            }
            translation[i]
                        = roundIntegralDouble(translationElements[i][0] * 12.0);
        }
        
        try {
            return new SymmetryMatrix(rotation, translation, false);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getMessage(), 0);
        }
    }
    
    /**
     * Rounds the specified {@code double} value to an {@code int}, ensuring
     * that the resulting value is equal to the argument within tolerance
     *  
     * @param  d the {@code double} value to round
     * 
     * @return the rounded value as an {@code int}
     * 
     * @throws ParseException if {@code d} is not integral (within tolerance)
     */
    private static int roundIntegralDouble(double d) throws ParseException {
        int result = (int) Math.round(d);
        
        if (Math.abs(d - result) > INTEGER_TOLERANCE) {
            throw new ParseException("Double value " + d
                    + " is not an integer (within tolerance)", 0);
        }
        
        return result;
    }

    /**
     * Reads tokens from the specified tokenizer until an EOL or EOF token is
     * read
     * 
     * @param  st the {@code StreamTokenizer} from which to read tokens
     * 
     * @throws IOException if one is thrown by the tokenizer
     */
    private static void readToEOL(StreamTokenizer st) throws IOException {
        for (; (st.ttype != StreamTokenizer.TT_EOL)
                && (st.ttype != StreamTokenizer.TT_EOF);
                st.nextToken()) {
            // do nothing
        }
    }
    
    /**
     * Writes a CRT header appropriate for this {@code CrtFile}
     * 
     * @param  formatter the {@code Formatter} with which to write the output
     */
    private void writeCartesianHeader(Formatter formatter) {
        formatter.format("CARTESIAN %9d %9d      %s%n", 
                model.getAtoms().size(), model.getBonds().size(),
                getName());
    }
    
    /**
     * Writes the atoms of this {@code CrtFile} in CRT format
     * 
     * @param  formatter the {@code Formatter} with which to write the output
     */
    private void writeAtoms(Formatter formatter) {
        
        // Write each atom
        for (Atom atom : model.getAtoms()) {
            double[] coords = atom.getPosition().getCoordinates();
            
            formatter.format("%-8s  %9.5f %9.5f %9.5f %3d %s%n",
                    atom.getLabel(), coords[0], coords[1], coords[2],
                    atom.getElement().getAtomicNumber(),
                    ((atom.getSiteTag() == null) ? "" : atom.getSiteTag()));
        }
        
        // Write the section terminator
        formatter.format("ENDATOMS%n");
    }
    
    /**
     * Writes the bonds of this {@code CrtFile} in CRT format
     * 
     * @param  formatter the {@code Formatter} with which to write the output
     */
    private void writeBonds(Formatter formatter) {
        
        // dump the bonds to a List for sorting
        List<Bond<? extends Atom>> bonds
                = new ArrayList<Bond<? extends Atom>>(model.getBonds());
        
        // Cache the one-based atom indices to avoid repreated lookups
        final Map<Atom, Integer> atomIndices = new HashMap<Atom, Integer>();
        
        int index = 1;
        for (Atom atom : model.getAtoms()) {
            atomIndices.put(atom, index++);
        }
        
        // Sort the bonds according to the indices of the constituent atoms
        Collections.sort(bonds, new Comparator<Bond<? extends Atom>>() {

            public int compare(
                    Bond<? extends Atom> b0, Bond<? extends Atom> b1) {
                Integer atomIndex0 = atomIndices.get(b0.getAtom1());
                Integer atomIndex1 = atomIndices.get(b1.getAtom1());
                int result = atomIndex0.compareTo(atomIndex1);
                
                if (result == 0) {
                    atomIndex0 = atomIndices.get(b0.getAtom2());
                    atomIndex1 = atomIndices.get(b1.getAtom2());
                    result = atomIndex0.compareTo(atomIndex1);
                }
                
                return result;
            }
            
        });
        
        // Write the bond records
        for (Bond<? extends Atom> bond : bonds) {
            Integer atomIndex1 = atomIndices.get(bond.getAtom1());
            Integer atomIndex2 = atomIndices.get(bond.getAtom2());
            
            formatter.format("%4d %3d%n", atomIndex1, atomIndex2);
        }
        
        // Write the section terminator
        formatter.format("ENDBONDS%n");
    }
    
    /**
     * Writes the specified unit cell in CRT format
     * 
     * @param  formatter the {@code Formatter} with which to write the output
     * @param  cell the {@code CoordinateSystem} to write
     */
    private void writeCell(Formatter formatter, CoordinateSystem cell) {
        double[] coords = cell.getOrigin().getCoordinates();
        
        // Write the section header
        formatter.format("CELL%n");
        
        // Write the origin coordinates
        formatter.format("%12.5f %11.5f %11.5f%n",
                coords[0], coords[1], coords[2]);
        
        // Write the three unit cell vectors
        for (Vector vector : cell.getVectors()) {
            coords = vector.getCoordinates();
            formatter.format("%12.5f %11.5f %11.5f%n",
                    coords[0], coords[1], coords[2]);
        }
    }
    
    /**
     * Writes the symmetry operations in CRT format, based on the coordinates
     * defined by the specified unit cell
     * 
     * @param  formatter the {@code Formatter} with which to write the output
     * @param  cell a {@code CoordinateSystem} defining the coordinate system in
     *         which the symmetry operations should be expressed
     * @param  symmetryOps an {@code Iterable<SymmetryMatrix>} of symmetry
     *         matrices representing the symmetry operations to write 
     */
    private void writeSymmetry(Formatter formatter, CoordinateSystem cell,
            Iterable<SymmetryMatrix> symmetryOps) {
        
        // Construct the crystal-to-Cartesian coordinate transform
        LinearTransformation toCart = new LinearTransformation(
                getCartesianRotation(cell), getCartesianTranslation(cell));
        
        // Construct LinearTransformations for the symmops that should be output
        List<LinearTransformation> symmetryList
                = new ArrayList<LinearTransformation>();
        
        for (SymmetryMatrix matrix : symmetryOps) {
            if (!matrix.normalize().equals(SymmetryMatrix.IDENTITY)) {
                int[] translation = matrix.getTranslationVector();
                 
                symmetryList.add(new LinearTransformation(
                        new SimpleMatrix(matrix.getRotationMatrix()),
                        SimpleMatrix.createColumnMatrix(
                                new double[] {translation[0] / 12.0,
                                        translation[1] / 12.0,
                                        translation[2] / 12.0})));
            }
        }
        
        // Output the section header
        formatter.format("SYMMETRY %9d%n", symmetryList.size());
        
        // Output the symmetry operations
        for (LinearTransformation symmTransform : symmetryList) {
            LinearTransformation convertedSymmetry
                    = toCart.convertTransformation(symmTransform);
            double[][] translationElements
                    = convertedSymmetry.getTranslation().getElements(); 
            
            // Format the results
            for (double[] row : convertedSymmetry.getRotation().getElements()) {
                formatter.format(
                        "%12.5f %11.5f %11.5f%n", row[0], row[1], row[2]);
            }
            formatter.format("%12.5f %11.5f %11.5f%n", translationElements[0][0],
                    translationElements[1][0], translationElements[2][0]);
        }
        
        // Output the section trailer
        formatter.format("ENDSYMM%n");
    }
    
    /**
     * Computes the rotation matrix appropriate for transforming
     * crystallographic (fractional) coordinates referred to the specified unit
     * cell into Cartesian coordinates in a system parallel to the one to which
     * the specified unit cell is referred.  An origin shift may be required to
     * bring the coordinates resulting from such a transformation into the
     * Cartesian system to which the unit cell is referred. 
     *  
     * @param  cell a {@code CoordinateSystem} defining the two coordinate
     *         systems for which the transformation rotation is desired
     *         
     * @return a {@code SimpleMatrix} suitable for left-multiplying a column
     *         vector of fractional coordinates to obtain corresponding
     *         Cartesian coordinates, albeit not necessarilly coordinates
     *         referred to the same origin as the specified unit cell is
     *
     * @see #getCartesianTranslation(CoordinateSystem)
     */
    private static SimpleMatrix getCartesianRotation(CoordinateSystem cell) {
        Vector[] vectors = cell.getVectors();
        double[][] transformElements = new double[][] {
                vectors[0].getCoordinates(),
                vectors[1].getCoordinates(),
                vectors[2].getCoordinates()};
        
        return new SimpleMatrix(transformElements).getTranspose();
    }
    
    /**
     * Computes the translation vector appropriate for transforming coordinates
     * to the Cartesian system to which the specified unit cell is referred,
     * from a parallel Cartesian system having its origin at the origin of the
     * crystallographic coordinate system defined by the unit cell.
     *   
     * @param  cell a {@code CoordinateSystem} defining the coordinates of the
     *         crystallographic origin relative to the Cartseian one; the
     *         returned vector will produce an origin shift from the former to
     *         the latter
     *         
     * @return the origin-shift translation, as the 3 by 1 (column) matrix that
     *         should be added to a 3 by 1 coordinate matrix to effect the
     *         coordinate transformation
     * 
     * @see #getCartesianRotation(CoordinateSystem)
     */
    private static SimpleMatrix getCartesianTranslation(CoordinateSystem cell) {
        return SimpleMatrix.createColumnMatrix(
                cell.getOrigin().getCoordinates());
    }
    
    /**
     * Asserts that the current token available from the specified tokenizer is
     * of type WORD; if this is not the case then a ParseException is thrown
     * 
     * @param  st the {@code StreamTokenizer} to test 
     * @param  exceptionMessage the detail message for the exception in the case
     *         that the assertion fails
     *         
     * @throws ParseException if the current token available from the tokenizer
     *         is not of type {@code StreamTokenizer.TT_WORD}
     */
    private static void assertWord(StreamTokenizer st, String exceptionMessage)
            throws ParseException {
        if (st.ttype != StreamTokenizer.TT_WORD) {
            throw new ParseException(exceptionMessage, st.lineno());
        }
    }

    /**
     * Asserts that the current token available from the specified tokenizer is
     * of type NUMBER; if this is not the case then a ParseException is thrown
     * 
     * @param  st the {@code StreamTokenizer} to test 
     * @param  exceptionMessage the detail message for the exception in the case
     *         that the assertion fails
     *         
     * @throws ParseException if the current token available from the tokenizer
     *         is not of type {@code StreamTokenizer.TT_NUMBER}
     */
    private static void assertNumber(StreamTokenizer st,
            String exceptionMessage) throws ParseException {
        if (st.ttype != StreamTokenizer.TT_NUMBER) {
            throw new ParseException(exceptionMessage, st.lineno());
        }
    }

    /**
     * Asserts that the current token available from the specified tokenizer is
     * of type EOL; if this is not the case then a ParseException is thrown
     * 
     * @param  st the {@code StreamTokenizer} to test 
     * @param  exceptionMessage the detail message for the exception in the case
     *         that the assertion fails
     *         
     * @throws ParseException if the current token available from the tokenizer
     *         is not of type {@code StreamTokenizer.TT_EOL}
     */
    private static void assertEOL(StreamTokenizer st, String exceptionMessage)
            throws ParseException {
        if (st.ttype != StreamTokenizer.TT_EOL) {
            throw new ParseException(exceptionMessage, st.lineno());
        }
    }

    /**
     * Asserts that the current token available from the specified tokenizer is
     * <em>not</em> of type EOF; if this is not the case then a ParseException
     * is thrown
     * 
     * @param  st the {@code StreamTokenizer} to test 
     * @param  exceptionMessage the detail message for the exception in the case
     *         that the assertion fails
     *         
     * @throws ParseException if the current token available from the tokenizer
     *         is of type {@code StreamTokenizer.TT_EOF}
     */
    private static void assertNotEOF(StreamTokenizer st,
            String exceptionMessage) throws ParseException {
        if (st.ttype == StreamTokenizer.TT_EOF) {
            throw new ParseException(exceptionMessage, st.lineno());
        }
    }
}
