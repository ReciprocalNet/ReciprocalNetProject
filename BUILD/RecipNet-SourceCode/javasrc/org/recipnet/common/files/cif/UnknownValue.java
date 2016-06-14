/*
 * Reciprocal Net Project
 * 
 * UnknownValue.java
 * 
 * 02-02-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

import org.recipnet.common.files.CifFile;

/**
 * A <code>CifFile.CifValue</code> implementation representing the special CIF
 * "unknown" value, represented in a CIF file by an unquoted question mark
 * character
 * 
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public final class UnknownValue implements CifFile.CifValue {
    
    /**
     * The only instance of this class.  Only one is ever needed, because there
     * is only one distinct CIF unknown value
     */
    public static final UnknownValue instance = new UnknownValue();

    /**
     * This class' only constructor is private so that the only instances will
     * be those created by this class itself. 
     */
    private UnknownValue() { /* nothing to do */ }

    /**
     * Returns the <code>String</code> representation of this
     * <code>CifFile.CifValue</code>, which is always "?"
     * 
     * @return the <code>String</code> "?"
     */
    @Override
    public String toString() {
        return "?";
    }
}