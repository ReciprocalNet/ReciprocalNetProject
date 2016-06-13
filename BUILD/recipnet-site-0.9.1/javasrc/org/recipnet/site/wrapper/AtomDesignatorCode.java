/*
 * Reciprocal Net Project
 *
 * AtomDesignatorCode.java
 *
 * 10-Dec-2005: jobollin wrote first draft
 * 30-Aug-2006: jobollin added the static valueOf(String) factory method and
 *              field accessors instances remain immutable
 */

package org.recipnet.site.wrapper;

import org.recipnet.common.molecule.SymmetryCode;

/**
 * A class representing an ORTEP "atom designator code", which compactly
 * describes to ORTEP a precise atom site (base atom + symmetry) in a crystal.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class AtomDesignatorCode {

    /**
     * The atom number of this {@code AtomDesignatorCode}, a one-based index
     * into the atom list provided as part of a particular ORTEP input file
     */
    private final int atomNumber;

    /**
     * The unit cell translations of this {@code AtomDesignatorCode}, three
     * five-based counts of translations along the axial directions.  Being
     * five-based (5 means no translation, 6 means 1 unit in the positive
     * direction, 4 means one in the negative direction, etc.) allows negative
     * counts to be encoded in compact yet human-readable form
     */
    private final int[] translations;

    /**
     * The symmetry operation number of this {@code AtomDesignatorCode}, a
     * one-based index into the symmetry operation list provided as part of a
     * particular ORTEP input file; ORTEP requires at least one symmetry card
     * but limits the total number to 96.  Instances have a string
     * representation conformant with the one expected by ORTEP, and they are
     * immutable.
     */
    private final int symmetryNumber;

    /**
     * Initializes a {@code AtomDesignatorCode} with the specified atom number
     * and symmetry code
     * 
     * @param  atom the one-based index of the base atom of this
     *         {@code AtomDesignatorCode}
     * @param  symmetry a {@code SymmetryCode} representing the symmetry
     *         operation and translation parts of the desired
     *         {@code AtomDesignatorCode}
     *
     * @throws IllegalArgumentException if the atom number is less than one
     *         or the symmetry code's operation number is too large
     */
    public AtomDesignatorCode(int atom, SymmetryCode symmetry) {
        this(atom, symmetry.getTranslations(), symmetry.getOperationIndex());
    }

    /**
     * Initializes a {@code AtomDesignatorCode} with the specified atom number,
     * translation codes, and symmetry operation number
     * 
     * @param  atom the one-based index of the base atom of this
     *         {@code AtomDesignatorCode}
     * @param  translations an {@code int[]} of length 3 containing the
     *         five-based unit cell translation codes for this
     *         {@code AtomDesignatorCode}
     * @param  symmNumber the one-based symmetry operation number for this
     *         {@code AtomDesignatorCode}
     * 
     * @throws IllegalArgumentException if the atom number is less than one,
     *         the translations array is the wrong length, the symmetry
     *         operation number is too large, or any of the translation codes
     *         are out of the range 1 - 9.
     */
    public AtomDesignatorCode(int atom, int[] translations, int symmNumber) {
        if (atom < 1) {
            throw new IllegalArgumentException("Atom number must be positive");
        } else if (translations.length < 3) {
            throw new IllegalArgumentException("too few translations");
        }
        if (symmNumber > OrtepInputMaker.ORTEP_MAX_SYMMETRY) {
            throw new IllegalArgumentException(
                    "The symmetry operation index is too large: " + symmNumber);
        } else {
            for (int i = 0; i < 3; i++) {
                if ((translations[i] < 1) || (translations[i] > 9)) {
                    throw new IllegalArgumentException(
                            "translation code is out of range: "
                                    + translations[i]);
                }
            }
        }

        this.atomNumber = atom;
        this.translations = translations.clone();
        this.symmetryNumber = symmNumber;
    }

    /**
     * Returns the atom number component of this atom designator code
     *
     * @return the atom number as an int
     */
    public int getAtomNumber() {
        return atomNumber;
    }

    /**
     * Returns the symmetry number component of this atom designator code
     *
     * @return the symmetry number as an int
     */
    public int getSymmetryNumber() {
        return symmetryNumber;
    }

    /**
     * Returns the translation components of this atom designator code
     *
     * @return the translations as an int[]; the array can be freely modified
     *         without affecting this {@code AtomDesignatorCode}
     */
    public int[] getTranslations() {
        return translations.clone();
    }

    /**
     * Obtains a string representation of this {@code AtomDesignatorCode}.  The
     * particular represenation chosen is conformant with ORTEP expectations,
     * with the caveat that standard ORTEP limits ADCs' atom number component to
     * a maximum of 999 but this class places no such restriction.
     * 
     * @return the {@code String} representation of this
     *         {@code AtomDesignatorCode}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(atomNumber);
        for (int i = 0; i < translations.length; i++) {
            sb.append(translations[i]);
        }
        if (symmetryNumber < 10) {
            sb.append('0');
        }
        sb.append(symmetryNumber);

        return sb.toString();
    }
    
    /**
     * Parses an {@code AtomDesignatorCode} from a string formatted as by
     * the {@link #toString()} method
     *
     * @param  adcString the {@code String} to parse
     * 
     * @return an {@code AtomDesignatorCode} corresponding to the specified
     *         string
     * 
     * @throws IllegalArgumentException if the specified string is not a valid
     *         representation of an ADC
     */
    public static AtomDesignatorCode valueOf(String adcString) {
        adcString = adcString.trim();
        try {
            int len = adcString.length();
            String symNum = adcString.substring(len - 2);
            char zTran = adcString.charAt(len - 3);
            char yTran = adcString.charAt(len - 4);
            char xTran = adcString.charAt(len - 5);
            String atomNum = adcString.substring(0, len - 5);
            
            return new AtomDesignatorCode(
                    Integer.parseInt(atomNum),
                    new int[] {Character.digit(xTran, 10),
                        Character.digit(yTran, 10), Character.digit(zTran, 10)},
                    Integer.parseInt(symNum));
        } catch (IndexOutOfBoundsException ioobe) {
            // Will throw later
        } catch (IllegalArgumentException iae) {
            // will throw later
        }
        
        throw new IllegalArgumentException("'" + adcString
                + "' is not a valid ORTEP atom designator code");
    }
}