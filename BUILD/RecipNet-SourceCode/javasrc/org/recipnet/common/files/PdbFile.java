/*
 * Reciprocal Net Project
 *
 * PdbFile.java
 *
 * 07-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;

/**
 * <p>
 * A class representing the data content of a PDB file, and providing a method
 * to write that data in PDB format (but not, in this version, to read it).
 * This version supports a fairly restrictive portion of the content of a PDB
 * file -- essentially restricted to that which can be derived from a CRT file.
 * </p><p>
 * The <a
 * href="http://www.rcsb.org/pdb/docs/format/pdbguide2.2/guide2.2_frame.html"
 * >output format represented by this class</a> is described in detail on the
 * <a href="http://www.rcsb.org/pdb/index.html">PDB web site</a>.
 * </p> 
 * 
 * @param <A> the type of atom characterizing an instance
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class PdbFile <A extends Atom> {
    
    /**
     * The compound name, which may be null
     */
    private String compoundName;
    
    /**
     * A molecular model of the structure represented by this PDB file
     */
    private final MolecularModel<A, Bond<A>> model;
    
    /**
     * A DecimalFormat used internally by the file writing code to format
     * real numbers for output; uses of this object must be synchronized on it 
     */
    private final DecimalFormat format;
    
    /**
     * Initializes a {@code PdbFile} with the specified model and compound name
     * 
     * @param  model the {@code MolecularModel<? extends Atom, ? extends Bond>}
     *         to be used by this PDB file to represent its constituent
     *         molecular structure; external modifications to this model will
     *         affect the PDB file; this argument should not be null
     * @param  name the name of this compound, or {@code null} if none is known
     */
    public PdbFile(MolecularModel<A, Bond<A>> model, String name) {
        if (model == null) {
            throw new NullPointerException("The model must not be null");
        }
        this.compoundName = name;
        this.model = model;
        
        format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        format.setGroupingUsed(false);
        format.setMaximumIntegerDigits(Integer.MAX_VALUE);
        format.setMinimumIntegerDigits(1);
    }
    
    /**
     * Gets the compound name assigned to this PDB file
     *  
     * @return the compound name {@code String}, which may be {@code null}
     */
    public String getCompoundName() {
        return compoundName;
    }

    /**
     * Sets the compound name assigned to this PDB file
     * 
     * @param  name the {@code String} to set as the compound name; may be
     *         {@code null}
     */
    public void setCompoundName(String name) {
        this.compoundName = name;
    }

    /**
     * Returns a reference to the molecular model object representing the
     * molecular structure described by this PDB file
     * 
     * @return the {@code MolecularModel<? extends Atom,? extends Bond>} of
     *         this PDB file; will not be {@code null}
     */
    public MolecularModel<A, Bond<A>> getModel() {
        return model;
    }

    /**
     * Writes the content of this PDB file to the specified {@code Writer} in
     * PDB format.  The writer will be flushed by this method, but not closed.
     * 
     * @param  writer the {@code Writer} to which the output should be directed;
     *         should not be {@code null}
     *         
     * @throws IOException if an I/O error occurs while writing the data
     */
    public void writeTo(Writer writer) throws IOException {
        PrintWriter pw = new PrintWriter(writer);
        PdbWriter out = new PdbWriter(pw);
        
        writeTitleSection(out);
        writeModelSection(out);
        writeConnectivitySection(out);
        writeBookkeepingSection(out);
        if (pw.checkError()) {  // flushes the stream, too
            throw new IOException("I/O error while writing PDB data");
        }
    }
    
    /**
     * Writes the "Title Section" of this PDB file to the specified writer.  At
     * this version, most of the data in the title section are unavailable,
     * therefore "NULL" placeholders are inserted in the corresponding spots in
     * the mandatory records
     * 
     * @param  out a {@code PdbWriter} to which to direct the output
     */
    private void writeTitleSection(PdbWriter out) {
        
        /*
         * Most of the data in this section are unavailable to this class.
         * Records for which no meaningful data can be provided are omitted
         * where possible, but many such records are mandatory. 
         */
        
        StringBuilder sb = new StringBuilder(80);
        
        sb.append("HEADER");
        setField(sb, "NULL", 11, 50);  // Classification
        setField(sb, "NULL", 51, 59);  // PDB deposition date
        setField(sb, "NULL", 63, 66);  // PDB ID code
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("TITLE");
        setField(sb, "NULL", 11, 70);
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("COMPND");
        setField(sb, " ", 7, 10);
        setField(sb, "MOLECULE: " + escapeString(getCompoundName()), 11, 70);
        
        /*
         * Considerably more (structured) information is supported by this
         * field, but none of it is available in this version of this class  
         */
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("SOURCE");
        setField(sb, "NULL", 11, 70);  // Structured compound source information
        out.writeRecord(sb);

        sb.setLength(0);
        sb.append("KEYWDS");
        setField(sb, "NULL", 11, 70);  // keywords
        out.writeRecord(sb);

        sb.setLength(0);
        sb.append("EXPDTA");
        setField(sb, "NULL", 11, 70);  // Experimental technique (fixed values) 
        out.writeRecord(sb);

        sb.setLength(0);
        sb.append("AUTHOR");
        setField(sb, "NULL", 11, 70);  // Structured author names
        out.writeRecord(sb);

        sb.setLength(0);
        sb.append("REVDAT");
        setField(sb, "  1", 8, 10);    // Designates a newly-created entry
        setField(sb, "NULL", 14, 22);  // Modification date
        setField(sb, "NULL", 24, 28);  // PDB internal identifier
        setField(sb, "0", 32, 32);     // Designates a newly-created entry
        out.writeRecord(sb);
        
        /*
         * Data resolution structured remarks (mandatory):
         */
        
        sb.setLength(0);
        sb.append("REMARK");
        setField(sb, "2", 10, 10);
        out.writeRecord(sb);
        
        sb.setLength(10);
        setField(sb, "RESOLUTION.", 12, 22);
        setField(sb, "NULL", 23, 27);  // Diffraction Resolution 
        setField(sb, "ANGSTROMS", 29, 38);
        out.writeRecord(sb);
        
        /*
         * Refinement information structured remarks (mandatory):
         */
        
        sb.setLength(6);
        setField(sb, "3", 10, 10);
        out.writeRecord(sb);
        
        sb.setLength(10);
        setField(sb, "REFINEMENT.", 12, 22);
        out.writeRecord(sb);
        
        sb.setLength(10);
        setField(sb, "PROGRAM", 14, 20);
        setField(sb, ":", 26, 26);
        setField(sb, "NULL", 28, 31);
        out.writeRecord(sb);
        
        setField(sb, "AUTHORS", 14, 20);
        out.writeRecord(sb);
        
        sb.setLength(10);
        out.writeRecord(sb);
    
        setField(sb, "NULL", 12, 15);
        out.writeRecord(sb);
        
        /*
         * Format compliance structured remarks: 
         */
        
        sb.setLength(6);
        setField(sb, "4", 10, 10);
        out.writeRecord(sb);
        
        sb.setLength(10);
        setField(sb, "NULL", 12, 15);
        setField(sb, "COMPLIES WITH FORMAT V. 2.2, 16-DEC-1999", 17, 56);
        out.writeRecord(sb);
        
        /*
         * User-specified structured remarks: 
         */
        
        sb.setLength(6);
        setField(sb, "6", 10, 10);
        out.writeRecord(sb);
        
        sb.setLength(10);
        setField(sb, "Auto-generated by the Reciprocal Net Site software", 12,
                80);
        out.writeRecord(sb);
    }
    
    /**
     * Writes the "Model Section" of this PDB file to the specified writer.  At
     * this version, most of the data in the model section are unavailable,
     * but meaningful dummy values can nevertheless be invented 
     * 
     * @param  out a {@code PdbWriter} to which to direct the output
     */
    private void writeModelSection(PdbWriter out) {
        StringBuilder sb = new StringBuilder(80);
        
        sb.append("CRYST1");  // Unit cell, space group, and Z
        setField(sb, formatReal(1.0, 9, 3), 7, 15);
        setField(sb, formatReal(1.0, 9, 3), 16, 24);
        setField(sb, formatReal(1.0, 9, 3), 25, 33);
        setField(sb, formatReal(90.0, 7, 2), 34, 40);
        setField(sb, formatReal(90.0, 7, 2), 41, 47);
        setField(sb, formatReal(90.0, 7, 2), 48, 54);
        setField(sb, "P 1", 56, 66);
        setField(sb, "1", 70, 70);
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("ORIGX1");  // Submitted --> current coordinate transformation
        setField(sb, formatReal(1.0, 10, 6), 11, 20);
        setField(sb, formatReal(0.0, 10, 6), 21, 30);
        setField(sb, formatReal(0.0, 10, 6), 31, 40);
        setField(sb, formatReal(0.0, 10, 5), 46, 55);
        out.writeRecord(sb);
        
        setField(sb, "ORIGX2", 1, 6);  // coordinate transformation, continued
        setField(sb, formatReal(0.0, 10, 6), 11, 20);
        setField(sb, formatReal(1.0, 10, 6), 21, 30);
        out.writeRecord(sb);
        
        setField(sb, "ORIGX3", 1, 6);  // coordinate transformation, continued
        setField(sb, formatReal(0.0, 10, 6), 21, 30);
        setField(sb, formatReal(1.0, 10, 6), 31, 40);
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("SCALE1");  // Orthogonal --> fractional transformation
        setField(sb, formatReal(1.0, 10, 6), 11, 20);
        setField(sb, formatReal(0.0, 10, 6), 21, 30);
        setField(sb, formatReal(0.0, 10, 6), 31, 40);
        setField(sb, formatReal(0.0, 10, 5), 46, 55);
        out.writeRecord(sb);
        
        setField(sb, "SCALE2", 1, 6);  // transformation, continued
        setField(sb, formatReal(0.0, 10, 6), 11, 20);
        setField(sb, formatReal(1.0, 10, 6), 21, 30);
        out.writeRecord(sb);
        
        setField(sb, "SCALE3", 1, 6);  // transformation, continued
        setField(sb, formatReal(0.0, 10, 6), 21, 30);
        setField(sb, formatReal(1.0, 10, 6), 31, 40);
        out.writeRecord(sb);
        
        /*
         * The atomic coordinates
         */
        
        sb.setLength(0);
        sb.append("ATOM");
        
        int serial = 1;
        for (Atom atom : model.getAtoms()) {
            double[] coords = atom.getPosition().getCoordinates();
            
            setField(sb, formatInteger(serial++, 5), 7, 11);
            setField(sb, atom.getElement().getSymbol().toUpperCase(), 13, 16);
            setField(sb, "A", 22, 22);
            setField(sb, "   1", 23, 26);
            setField(sb, formatReal(coords[0], 8, 3), 31, 38);
            setField(sb, formatReal(coords[1], 8, 3), 39, 46);
            setField(sb, formatReal(coords[2], 8, 3), 47, 54);
            setField(sb, formatReal(1.0, 6, 2), 55, 60);
            setField(sb, formatReal(0.0, 6, 2), 61, 66);
            setField(sb, 
                    adjustRight(atom.getElement().getSymbol().toUpperCase(), 2),
                    77, 78);
            out.writeRecord(sb);
        }
        
        sb.setLength(0);
        sb.append("TER");
        setField(sb, formatInteger(serial++, 5), 7, 11);
        setField(sb, "A", 22, 22);
        setField(sb, "   1", 23, 26);
        out.writeRecord(sb);
    }
    
    /**
     * Writes the "Connectivity Section" of this PDB file to the specified
     * writer.  The connectivity records written by this method are entirely
     * derived from this PdbFile's molecular model. 
     * 
     * @param  out a {@code PdbWriter} to which to direct the output
     */
    private void writeConnectivitySection(PdbWriter out) {
        StringBuilder sb = new StringBuilder(80);
        LinkedHashMap<Atom, List<Atom>> connectivityTable
                = new LinkedHashMap<Atom, List<Atom>>();
        final Map<Atom, Integer> atomSequenceMap = new HashMap<Atom, Integer>();
        int index;
        final Comparator<Atom> sequenceNumberOrder =  new Comparator<Atom>() {
            public int compare(Atom atom1, Atom atom2) {
                Integer seq1 = atomSequenceMap.get(atom1);
                Integer seq2 = atomSequenceMap.get(atom2);
                
                assert ((seq1 != null) && (seq2 != null));
                
                return seq1.compareTo(seq2);
            }
        };

        /*
         * Build the sequence number map and establish the sequence of the
         * connectivity table; because atomSequenceMap is a LinkedHashMap, its
         * iteration order is the same as its insertion order
         */
        index = 1;
        for (Atom atom : model.getAtoms()) {
            connectivityTable.put(atom, new ArrayList<Atom>());
            atomSequenceMap.put(atom, index++);
        }
        
        /*
         * Populate the connectivity table 
         */
        for (Bond<? extends Atom> bond : model.getBonds()) {
            List<Atom> connections;
            Atom atom1 = bond.getAtom1();
            Atom atom2 = bond.getAtom2();
            
            connections = connectivityTable.get(atom1);
            assert (connections != null);
            if (!connections.contains(atom2)) {
                connections.add(atom2);
            }
            
            connections = connectivityTable.get(atom2);
            assert (connections != null);
            if (!connections.contains(atom1)) {
                connections.add(atom1);
            }
        }
        
        sb.append("CONECT");
        
        /*
         * Output connection records for each atom
         */
        for (Map.Entry<Atom, List<Atom>> connections
                : connectivityTable.entrySet()) {
            String sourceNumber = formatInteger(
                    atomSequenceMap.get(connections.getKey()), 5);
            List<Atom> targets = connections.getValue();
            
            Collections.sort(targets, sequenceNumberOrder);
            setField(sb, sourceNumber, 7, 11);
            for (int i = 0; i < targets.size(); i++) {
                int fieldStart = ((i % 4) * 5) + 12;
                int targetNumber = atomSequenceMap.get(targets.get(i));
                
                setField(sb, formatInteger(targetNumber, 5), fieldStart,
                        fieldStart + 4);
                if ((i % 4) == 3) {
                    out.writeRecord(sb);
                    sb.setLength(11);
                }
            }
            
            if (sb.length() > 11) {
                out.writeRecord(sb);
            }
        }
    }
    
    /**
     * Writes the "Bookkeeping Section" of this PDB file to the specified
     * writer.  This section is 100% correct and to-spec, consisting principally
     * of counts of various record types and an end of data mark. 
     * 
     * @param  out a {@code PdbWriter} to which to direct the output
     */
    private void writeBookkeepingSection(PdbWriter out) {
        StringBuilder sb = new StringBuilder(80);
        int sum;
        
        sb.append("MASTER");
        setField(sb, formatInteger(out.getRecordCount("REMARK"), 5), 11, 15);
        setField(sb, "    0", 16, 20);
        setField(sb, formatInteger(out.getRecordCount("HET   "), 5), 21, 25);
        setField(sb, formatInteger(out.getRecordCount("HELIX "), 5), 26, 30);
        setField(sb, formatInteger(out.getRecordCount("SHEET "), 5), 31, 35);
        setField(sb, formatInteger(out.getRecordCount("TURN  "), 5), 36, 40);
        setField(sb, formatInteger(out.getRecordCount("SITE  "), 5), 41, 45);
        
        sum = out.getRecordCount("ORIGX1") + out.getRecordCount("SCALE1")
                + out.getRecordCount("MTRIX1");
        sum += out.getRecordCount("ORIGX2") + out.getRecordCount("SCALE2")
                + out.getRecordCount("MTRIX2");
        sum += out.getRecordCount("ORIGX3") + out.getRecordCount("SCALE3")
                + out.getRecordCount("MTRIX3");
        setField(sb, formatInteger(sum, 5), 46, 50);
        
        sum = out.getRecordCount("ATOM  ") + out.getRecordCount("HETATM");
        setField(sb, formatInteger(sum, 5), 51, 55);
        
        setField(sb, formatInteger(out.getRecordCount("TER   "), 5), 56, 60);
        setField(sb, formatInteger(out.getRecordCount("CONECT"), 5), 61, 65);
        setField(sb, formatInteger(out.getRecordCount("SEQRES"), 5), 66, 70);
        
        out.writeRecord(sb);
        
        sb.setLength(0);
        sb.append("END");
        out.writeRecord(sb);
    }
    
    /**
     * Updates the provided {@code StringBuilder} by filling the specified
     * field with the specified String, truncating / filling as necessary.  This
     * operation may leave the builder containing ASCII null characters in the
     * specified field and / or ahead of it, but that is accommodated by
     * {@link PdbWriter}s.  The field position is specified in terms of
     * one-based starting and ending column numbers for direct correspondance
     * with the definitions in the PDB file format specification.
     * 
     * @param  sb the {@code StringBuilder} to update
     * @param  value the {@code String} value to insert
     * @param  firstColumn the one-based first column of the field to update
     * @param  lastColumn the one-based last column of the field to update
     */
    private void setField(StringBuilder sb, String value, int firstColumn,
            int lastColumn) {
        int fieldOffset = firstColumn - 1;
        int fieldWidth = (lastColumn - fieldOffset);
        int padOffset = Math.min(lastColumn, fieldOffset + value.length());

        // Extend the StringBuilder if necessary to accommodate the field
        if (sb.length() < lastColumn) {
            sb.setLength(lastColumn);
        }
        
        // Truncate the value if it is too long
        if (value.length() > fieldWidth) {
            value = value.substring(0, fieldWidth);
        }
        
        // Insert the value into the field
        sb.replace(fieldOffset, padOffset, value);
        
        // Pad the field with blanks as necessary
        for (int i = padOffset; i < lastColumn; i++) {
            sb.setCharAt(i, ' ');
        }
    }

    /**
     * Prepares a string for insertion into a PDB file by escaping those
     * characters that PDB demands be escaped (commas, colons, and semicolons),
     * and by replacing null strings with the String "NULL"
     * 
     * @param  s the {@code String} to escape
     * 
     * @return the escaped version of the input string, as a {@code String}
     */
    private String escapeString(String s) {
        return (s == null) ? "NULL" : s.replaceAll("[;,:]", "\\$0");
    }

    /**
     * Formats an integer value according to the standard PDB style -- decimal
     * representation with no decimal point, right-justified in a field of the
     * specified width.
     * 
     * @param  value the value to format
     * @param  width the field width
     * 
     * @return the formatted value as a {@code String}.  The string's length
     *         will be {@code width}, unless the specified value is too large to
     *         fit in that width, in which case the minimum number of characters
     *         sufficient to represent the value is used 
     */
    private String formatInteger(int value, int width) {
        StringBuilder sb = new StringBuilder(width);
        
        sb.append(value);
        while (sb.length() < width) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }
    
    /**
     * Formats a real number into a string according to the specified formatting
     * characteristics.
     * 
     * @param  value the {@code double} value ot format
     * @param  width the desired field width
     * @param  precision the number of digits that should appear after the
     *         decimal point
     *         
     * @return the specified value, formatted as a {@code String}.  It will be
     *         right-justified in a {@code width}-character string, or will
     *         completely fill a longer string if one is required to represent
     *         the value
     */
    private String formatReal(double value, int width, int precision) {
        StringBuilder sb = new StringBuilder(width);
        
        synchronized (format) {
            format.setMinimumFractionDigits(precision);
            format.setMaximumFractionDigits(precision);
            
            sb.append(format.format(value));
        }
        while (sb.length() < width) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }
    
    /**
     * Returns a copy of the input string with its content right-aligned in a
     * string as long as the specified field width
     *   
     * @param  s the {@code String} to adjust; leading and trailing spaces are
     *         insignificant 
     * @param  width the desired field width
     * 
     * @return a String contain {@code s.trim()} as a trailing substring, padded
     *         on the left with enough space characters to make the overall
     *         length equal to the field width.  If the trimmed input string is
     *         longer than {@code width} then it is returned (without any
     *         padding) 
     */
    private String adjustRight(String s, int width) {
        s = s.trim();
        if (s.length() >= width) {
            return s;
        } else {
            StringBuilder sb = new StringBuilder(width);
            
            for (int i = s.length(); i < width; i++ ) {
                sb.append(' ');
            }
            sb.append(s);
            
            return sb.toString();
        }
    }
}

/**
 * A utility class that assists PdbFile in outputting valid PDB-format files.
 * It wraps a {@code PrintWriter} with which the actual output is performed, and
 * it tracks the number of records of each type that are written for use in
 * creating a correct "MASTER" record.  Users should note that despite its name,
 * this class is not a subclass of {@code java.io.Writer}.
 *  
 * @author jobollin
 * @version 0.9.0
 */
class PdbWriter {
    
    /**
     * An internal map from PDB record type labels to record counts
     */
    private final Map<String, Integer> recordCounts
            = new HashMap<String, Integer>();
    
    /**
     * The {@code PrintWriter} to which this class will direct output
     */
    private final PrintWriter out;
    
    /**
     * Initializes a {@code PdbWriter} with the specified writer
     * 
     * @param  writer the {@code PrintWriter} to which this class should direct
     *         its output; this object is used directly (i.e. not wrapped in
     *         another writer) and all output is sent directly to it (i.e. not
     *         buffered inside this {@code PdbWriter})
     */
    public PdbWriter(PrintWriter writer) {
        if (writer == null) {
            throw new NullPointerException("null writer");
        } else {
            out = writer;
        }
    }
    
    /**
     * Writes a record represented by the specified {@code StringBuilder} to the
     * output.  The record is first cropped and / or expanded to length 80 if
     * necessary, and any ASCII NULL characters are replaced by spaces.  The
     * record type label from the first six characters is used to keep a running
     * count of the number of records of each type that are written.
     *    
     * @param  record a {@code StringBuilder} containing the record text
     */
    public void writeRecord(StringBuilder record) {
        String recordString;
        
        tidyRecord(record);
        recordString = record.toString();
        countRecord(recordString.substring(0, 6));
        out.println(recordString);
    }
    
    /**
     * Returns the current count of records having the specified record label
     * that have been written by this writer
     *  
     * @param  recordName the record name / label {@code String} for which a
     *         count is requested; the label is the first six characters of each
     *         record
     *         
     * @return the count of records of the specified label
     */
    public int getRecordCount(String recordName) {
        Integer count = recordCounts.get(recordName);
        
        return ((count == null) ? 0 : count.intValue());
    }
    
    /**
     * An internal method that "tidies" a {@code StringBuilder} intended for use
     * as a PDB record.  It sets the builder's length to 80, then replaces all
     * ASCII NULL characters anywhere in the record with spaces
     * 
     * @param  sb a {@code StringBuilder} containing the PDB record data to tidy 
     */
    private void tidyRecord(StringBuilder sb) {
        if (sb.length() != 80) {
            sb.setLength(80);
        }
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '\0') {
                sb.setCharAt(i, ' ');
            }
        }
    }
   
    /**
     * Increments the record counter for the specified record type
     * 
     * @param  recordName the record type whose counter should be incremented,
     *         in the form of the {@code String} record type label
     *         
     * @see #getRecordCount(String)
     */
    private void countRecord(String recordName) {
        Integer count = recordCounts.get(recordName);
        
        if (count == null) {
            recordCounts.put(recordName, Integer.valueOf(1));
        } else {
            recordCounts.put(recordName, Integer.valueOf(count + 1));
        }
    }
}
