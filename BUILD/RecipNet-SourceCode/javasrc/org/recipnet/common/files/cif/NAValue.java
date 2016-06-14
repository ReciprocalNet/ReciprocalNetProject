/*
 * Reciprocal Net Project
 * 
 * NAValue.java
 * 
 * 02-02-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

import org.recipnet.common.files.CifFile;

/**
 * A <code>CifFile.CifValue</code> implementation representing the "not
 * applicable" value (expressed in CIF as an unquoted, lone period (.))  Only
 * one instance is ever needed; this class therefore provides a static instance
 * but not an accessible constructor.
 *   
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public final class NAValue implements CifFile.CifValue {
    
    /** The one instance */
    public static final NAValue instance = new NAValue();

    /**
     * Initializes a new <code>NAValue</code>; there is nothing to do, but
     * providing this constructor prevents the automatic creation of an
     * accessible default constructor
     */
    private NAValue() { /* nothing to do */ }

    /**
     * Returns a <code>String</code> representation of this
     * <code>NAValue</code>, which, conformant with the specifications of the
     * <code>CifFile.CifValue</code> interface, matches the CIF representation
     * of this value
     * 
     * @return the <code>String</code> "."
     */
    @Override
    public String toString() {
        return ".";
    }
}
