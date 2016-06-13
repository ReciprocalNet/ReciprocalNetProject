/*
 * Reciprocal Net Project
 *
 * SymmetryContext.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.files.cif;

/**
 * A class representing data content of one record from a CIF atom site loop.
 * Instances of this class can essentially be considered data transfer objects,
 * with no logic.
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class AtomSiteRecord {
    
    /**
     * The label for this site
     */
    private final String label;
    
    /**
     * The site type symbol for this site; typically (but not necessarilly) the
     * symbol of the chemical element occupying the site; may be {@code null}
     */
    private final String typeSymbol;
    
    /**
     * The fractional X coordinate of this site
     */
    private final double fractX;
    
    /**
     * The fractional Y coordinate of this site
     */
    private final double fractY;
    
    /**
     * The fractional Z coordinate of this site
     */
    private final double fractZ;
    
    /**
     * The isotropic (or equivalent) atomic mean square displacement parameter,
     * according to the U convention
     */
    private final double isoU;
    
    /**
     * The isotropic (or equivalent) atomic mean square displacement parameter,
     * according to the B convention
     */
    private final double isoB;
    
    /**
     * Initializes an {@code AtomSiteRecord} with the specified parameters
     * 
     * @param  label the label for this atom site
     * @param  typeSymbol the type symbol for this atom site
     * @param  fractX the fractional X coordinate for this atom site
     * @param  fractY the fractional Y coordinate for this atom site
     * @param  fractZ the fractional Z coordinate for this atom site
     * @param  u the isotropic atomic mean square displacement parameter for
     *         this atom site, in the U convention
     * @param  b the isotropic atomic mean square displacement parameter for
     *         this atom site, in the U convention
     */
    public AtomSiteRecord(String label, String typeSymbol, double fractX,
            double fractY, double fractZ, double u, double b) {
        this.label = label;
        this.typeSymbol = typeSymbol;
        this.fractX = fractX;
        this.fractY = fractY;
        this.fractZ = fractZ;
        this.isoU = u;
        this.isoB = b;
    }
    
    /**
     * Returns the label of this atom site
     * 
     * @return the label {@code String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the type symbol of this atom site
     * 
     * @return the type symbol {@code String}
     */
    public String getTypeSymbol() {
        return typeSymbol;
    }

    /**
     * Returns the fractional X coordinate for this site
     * 
     * @return the fractional X coordinate as a {@code double}
     */
    public double getFractX() {
        return fractX;
    }
    
    /**
     * Returns the fractional Y coordinate for this site
     * 
     * @return the fractional Y coordinate as a {@code double}
     */
    public double getFractY() {
        return fractY;
    }
    
    /**
     * Returns the fractional Z coordinate for this site
     * 
     * @return the fractional Z coordinate as a {@code double}
     */
    public double getFractZ() {
        return fractZ;
    }
    
    /**
     * Returns the isotropic (or equivalent isotropic) atomic mean square
     * displacement parameter for this site, according to the U convention
     *  
     * @return the {@code isoU} {@code double}
     */
    public double getIsoU() {
        return isoU;
    }

    /**
     * Returns the isotropic (or equivalent isotropic) atomic mean square
     * displacement parameter for this site, according to the B convention
     * 
     * @return the {@code isoB} {@code double}
     */
    public double getIsoB() {
        return isoB;
    }

    /**
     * Determines whether this site record contains enough data to be valid; for
     * this to be the case, the label must be non-empty, and all the fractional
     * coordinates must have finite {@code double} values 
     * 
     * @return {@code true} if this record is valid; {@code false} if it isn't
     */
    public boolean isValid() {
        return ((label != null) && (!label.trim().equals(""))
                && !Double.isNaN(fractX) && !Double.isInfinite(fractX)
                && !Double.isNaN(fractY) && !Double.isInfinite(fractY)
                && !Double.isNaN(fractZ) && !Double.isInfinite(fractZ));
    }
}