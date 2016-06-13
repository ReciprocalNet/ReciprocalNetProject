/*
 * IUMSC Reciprocal Net Project
 *
 * AtomSiteAnisoRecord.java
 *
 * 02-Nov-2006: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

/**
 * A class representing data content of one record from a CIF atom site aniso
 * loop.  Instances of this class can essentially be considered data transfer
 * objects, with no logic.
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class AtomSiteAnisoRecord {
    
    /**
     * The site label for this record; set in the constructor and will not be
     * {@code null}
     */
    private final String label;
    
    /**
     * The elements of the anisotropic U tensor, or {@code null} if none were
     * provided.  This class relies on the user to provide these in this order:
     * u11, u22, u33, u12, u13, u23 
     */
    private final double[] anisoU; 
    
    /**
     * The elements of the anisotropic B tensor, or {@code null} if none were
     * provided.  This class relies on the user to provide these in this order:
     * b11, b22, b33, b12, b13, b23 
     */
    private final double[] anisoB;
    
    /**
     * Initializes a new {@code AtomSiteAnisoRecord} with the specified label
     * and parameters
     *
     * @param siteLabel the site label, as from CIF item
     *        {@code _atom_site_aniso_label}; must not be {@code null}
     * @param uij a {@code double[]} containing the anisotropic U parameters,
     *        as from CIF items {@code _atom_site_aniso_U_*}, in this order:
     *        u11, u22, u33, u12, u13, u23; either this parameter or {@code bij}
     *        must be non-{@code null}
     * @param bij a {@code double[]} containing the anisotropic B parameters,
     *        as from CIF items {@code _atom_site_aniso_B_*}, in this order:
     *        b11, b22, b33, b12, b13, b23; either this parameter or {@code uij}
     *        must be non-{@code null}
     */
    public AtomSiteAnisoRecord(String siteLabel, double[] uij,
            double[] bij) {
        if (siteLabel == null) {
            throw new NullPointerException(
                    "The aniso site label must not be null");
        } else if ((uij == null) && (bij == null)) {
            throw new IllegalArgumentException(
                    "At least one of uij and bij must not be null");
        } else if ((uij != null) && (uij.length != 6)) {
            throw new IllegalArgumentException(
                    "Wrong number of anisotropic U elements");
        } else if ((bij != null) && (bij.length != 6)) {
            throw new IllegalArgumentException(
                    "Wrong number of anisotropic B elements");
        }
        
        label = siteLabel;
        anisoU = ((uij == null) ? null : uij.clone());
        anisoB = ((bij == null) ? null : bij.clone());
    }

    /**
     * Returns the label of this record (CIF {@code _atom_site_aniso_label})
     *
     * @return the label as a {@code String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the anisotropic displacement parameters in B format, if they
     * were specified
     *
     * @return a {@code double[]} containing the anisotropic B parameters, if
     *         present; otherwise {@code null}.  Parameters are ordered b11,
     *         b22, b33, b12, b13, b23.
     */
    public double[] getAnisoB() {
        return ((anisoB == null) ? null : anisoB.clone());
    }

    /**
     * Returns the anisotropic displacement parameters in U format, if they
     * were specified
     *
     * @return a {@code double[]} containing the anisotropic U parameters, if
     *         present; otherwise {@code null}.  Parameters are ordered u11,
     *         u22, u33, u12, u13, u23.
     */
    public double[] getAnisoU() {
        return ((anisoU == null) ? null : anisoU.clone());
    }
    
    /**
     * Converts two-dimensional <i>U<sub>ij</sub></i> and <i>B<sub>ij</sub></i>
     * indices <i>i</i> and <i>j</i> to the corresponding index into the flat
     * arrays of displacement parameters expected and returned by this class.
     * For example, (1, 1) is converted to 0; (3, 3) is converted to 2, and
     * (1, 3) is converted to 4. 
     *
     * @param i the <em>one-based</em> index of the row of the desired element
     *        of the displacement tensor; behavior is undefined if this argument
     *        is less than 1 or greater than 3
     * @param j the <em>one-based</em> index of the column of the desired
     *        element of the displacement tensor; behavior is undefined if this
     *        argument is less than 1 or greater than 3
     *        
     * @return the <em>zero-based</em> array index into the Java arrays handled
     *        by this class at which the specified element will be found 
     */
    public static int index(int i, int j) {
        if (i == j) {
            return i - 1;
        } else {
            return (i + j);
        }
    }
}
