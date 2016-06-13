/*
 * Reciprocal Net Project
 *
 * SymmetryCode.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.molecule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing a CIF-style symmetry code.  Instances' string values
 * exactly match the CIF codes, but they maintain internal structure
 * describing the <em>meaning</em> of the codes so as to avoid repeatedly
 * parsing them. 
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SymmetryCode {
    
    /**
     * A Pattern matching CIF-style symmetry operation codes and capturing the
     * individual components into groups; used in the {@link #valueOf(String)}
     * method 
     */
    private static Pattern SYMMCODE_PATTERN
            = Pattern.compile("([0-9]+)[ _]([1-9])([1-9])([1-9])");

    /**
     * The (one-based) index of the base operation to which this code refers
     */
    private final int operationIndex;
    
    /**
     * An array of the unit translations (relative to the base operation)
     * represented by this code 
     */
    private final int[] translations;
    
    /**
     * The string value of this code
     */
    private final String stringValue;
    
    /**
     * Initializes a {@code SymmetryCode} with the specified parameters
     * 
     * @param  index the (one-based) index into the host context's base
     *         symmetry code list of the base operation for this symmetry
     *         code
     * @param  trans the unit cell translations associated with this code
     *         relative to the base matrix, as an {@code int[]}.  Each
     *         element is 5-based, and this array reference is stored as-is
     *         -- no copy of the array is made
     * 
     * @throws IllegalArgumentException if the specified index is
     *         nonpositive, if the array has length different from 3, or
     *         if any element of {@code trans} is less than 1 or greater
     *         than 9 
     */
    public SymmetryCode(int index, int[] trans) {
        StringBuilder sb;
        
        // Validate arguments
        if (index < 1) {
            throw new IllegalArgumentException(
                    "Nonpositive symmetry operation index: " + index);
        } else if (trans.length != 3) {
            throw new IllegalArgumentException(
                    "Translation array length is not 3");
        }
        
        // initialize the instance variables
        operationIndex = index;
        translations = trans;
        
        // construct the string representation
        sb = new StringBuilder();
        sb.append(index).append('_');
        for (int i = 0; i < 3; i++) {
            if ((translations[i] < 1) || (translations[i] > 9)) {
                throw new IllegalArgumentException(
                        "Translation component is out of range: "
                        + translations[i]);
            } else {
                sb.append(translations[i]);
            }
        }
        
        stringValue = sb.toString();
    }
    
    /**
     * Returns the (one-based) index of the base operation to which this
     * symmetry code refers
     * 
     * @return the operation index
     */
    public int getOperationIndex() {
        return operationIndex;
    }

    /**
     * Returns a copy of this code's translation vector
     * 
     * @return a copy of the translation vector as an {@code int[]}
     */
    public int[] getTranslations() {
        return translations.clone();
    }

    /**
     * Returns a {@code String} representation of this symmetry code
     * 
     * @return a {@code String} representation of this symmetry code, having
     *         the form "n_xyz", where n is a positive number (possibly
     *         having more than one digit) referencing a base symmetry
     *         operation, and x, y, and z are each single decimal digits
     *         encoding an additional translation along the corresponding
     *         unit cell axis 
     */
    @Override
    public String toString() {
        return stringValue;
    }
    
    /**
     * Determines whether this symmetry code is equal to the specified object
     * 
     * @param  o the object to compare to this one
     * 
     * @return {@code true} if {@code o} is a {@code SymmetryCode} with the same
     *         string value; otherwise, {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof SymmetryCode) {
            return this.stringValue.equals(((SymmetryCode) o).stringValue); 
        } else {
            return false;
        }
    }
    
    /**
     * Returns a hash code for this object; this {@code hashCode()} method is
     * consistent with {@link #equals(Object)}
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return stringValue.hashCode();
    }

    /**
     * Parses a string into a symmetry code.  The string is expected to have
     * form "n_xyz", where <i>n</i> is any positive integer designating a
     * one-based symmetry operation number, and <i>x</i>, <i>y</i>, and
     * <i>z</i> are decimal digits greater than zero representing numbers of
     * unit translations in their corresponding directions.  The translations
     * are referred to 5 as representing no translation, so 6 represents a
     * translation of one unit in the positive direction, and 3 represents a
     * translation of two units in the negative direction.
     * 
     * @param  codeString the {@code String} to parse
     * 
     * @return a {@code SymmetryCode} corresponding to the specified string
     * 
     * @throws IllegalArgumentException if the specified string does not conform
     *         to the expected format
     */
    public static SymmetryCode valueOf(String codeString) {
        Matcher matcher = SYMMCODE_PATTERN.matcher(codeString);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid symmetry code: " + codeString);
        } else {
            int baseOperation = Integer.parseInt(matcher.group(1));
            int[] translations = new int[] {
                    Integer.parseInt(matcher.group(2)),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))
            };
            
            return new SymmetryCode(baseOperation, translations);
        }
    }
}