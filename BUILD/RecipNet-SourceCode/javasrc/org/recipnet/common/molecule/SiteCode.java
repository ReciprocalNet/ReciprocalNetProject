/*
 * Reciprocal Net Project
 *
 * SiteCode.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.molecule;

/**
 * A class representing the combination of an atom site label and a symmetry
 * operation code; this is closely analogous to an ORTEP atom designator code
 * (ADC).  Instances represent specific positions in a crystal (the result of
 * applying the specified symmetry operation to the coordinates of the atom
 * identified by the specified label).
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SiteCode {
    
    /**
     * The label / symmetry code delimiter used in this class' string
     * representation
     */
    public final static char DELIM = '|';
    
    /** the atom site label {@code String} of this site code */
    private final String siteLabel;
    
    /** the {@code SymmetryCode} of this site code*/
    private final SymmetryCode symmetry;
    
    /** the pre-computed {@code String} value of this site code */
    private final String stringValue;

    /**
     * Initializes a {@code SiteCode} with the specified site label and
     * symmetry code
     * 
     * @param  label the atom site label {@code String} for this
     *         {@code SiteCode}
     * @param  symmetry the {@code SymmetryCode} for this {@code SiteCode}
     */
    public SiteCode(String label, SymmetryCode symmetry) {
        if (label == null || symmetry == null) {
            throw new NullPointerException(
                    "Neither the label nor the symmetry code may be null");
        }
        this.siteLabel = label;
        this.symmetry = symmetry;
        this.stringValue = label + DELIM + symmetry;
    }

    /**
     * Returns the atom site label associated with this code
     * 
     * @return the site label {@code String}
     */
    public String getSiteLabel() {
        return siteLabel;
    }

    /**
     * Returns the symmetry code associated with this site code
     * 
     * @return the {@code SymmetryCode} of this site code
     */
    public SymmetryCode getSymmetryCode() {
        return symmetry;
    }
    
    /**
     * @return the {@code stringValue} {@code String}
     */
    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Determines whether this site code is equal to the specified object
     * 
     * @param  o the object to compare to this one
     * 
     * @return {@code true} if {@code o} is a {@code SiteCode} with the same
     *         string value; otherwise, {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SiteCode) {
            return this.stringValue.equals(((SiteCode) o).stringValue);
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
     * Returns a {@code SiteCode} corresponding to the specified string
     * representation, which should be formatted as if by the
     * {@link #toString()} method of this class
     *  
     * @param  s the {@code String} value of the desired site code
     * @param  defaultSymmetry the {@code SymmetryCode} to use if the specified
     *         string does not contain one; must not be {@code null} if the
     *         specified string does not express a symmetry code part
     * 
     * @return a {@code SiteCode} corresponding to the specified string value
     * 
     * @throws IllegalArgumentException if the specified string contains a
     *         fragmentary or invalid symmetry code, or if it contains a
     *         label / symmetry delimiter without also containing a symmetry
     *         code
     */
    public static SiteCode valueOf(String s, SymmetryCode defaultSymmetry) {
        int delimIndex = s.lastIndexOf(DELIM);
        
        if (delimIndex < 1) {
            return new SiteCode(s, defaultSymmetry);
        } else {
            return new SiteCode(s.substring(0, delimIndex),
                    SymmetryCode.valueOf(s.substring(delimIndex + 1)));
        }
    }
}