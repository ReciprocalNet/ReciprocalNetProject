/*
 * Reciprocal Net Project
 *
 * SymmetryContext.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.files.cif;

/**
 * A class representing data content of one record from a CIF
 * {@code _geom_bond_[]} loop.  Instances of this class can essentially be
 * considered data transfer objects, with (almost) no logic.
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class GeomBondRecord {

    /**
     * The first / left site label for this bond record
     */
    private final String siteLabel1;

    /**
     * The second / right site label for this bond record
     */
    private final String siteLabel2;

    /**
     * The first / left symmetry code for this bond record
     */
    private final String siteSymm1;

    /**
     * The second / right symmetry code for this bond record
     */
    private final String siteSymm2;

    /**
     * Initializes a {@code GeomBondRecord} with the specified parameters
     * 
     * @param  label1 the first / left atom site label for this bond record
     * @param  label2 the second / right atom site label for this bond record
     * @param  symm1 the first / left symmetry code for this bond record
     * @param  symm2 the second / right symmetry code for this bond record
     */
    public GeomBondRecord(String label1, String label2, String symm1,
            String symm2) {
        siteLabel1 = label1;
        siteLabel2 = label2;
        siteSymm1 = (symm1 == null) ? null : symm1.replace(' ', '_');
        siteSymm2 = (symm2 == null) ? null : symm2.replace(' ', '_');
    }

    /**
     * Returns the first site label for this bond record
     * 
     * @return the first site label {@code String}
     */
    public String getSiteLabel1() {
        return siteLabel1;
    }

    /**
     * Returns the second site label for this bond record
     * 
     * @return the second site label {@code String}
     */
    public String getSiteLabel2() {
        return siteLabel2;
    }

    /**
     * Returns the first site symmetry code for this bond record
     * 
     * @return the first symmetry code {@code String}
     */
    public String getSiteSymm1() {
        return siteSymm1;
    }

    /**
     * Returns the second site symmetry code for this bond record
     * 
     * @return the second site symmetry code {@code String}
     */
    public String getSiteSymm2() {
        return siteSymm2;
    }
    
    /**
     * Determines whether this bond record contains enough data to be valid; for
     * this to be the case, the labels must both be non-empty 
     * 
     * @return {@code true} if this record is valid; {@code false} if it isn't
     */
    public boolean isValid() {
        return ((siteLabel1 != null) && (!siteLabel1.trim().equals(""))
                && (siteLabel2 != null) && (!siteLabel2.trim().equals("")));
    }
}