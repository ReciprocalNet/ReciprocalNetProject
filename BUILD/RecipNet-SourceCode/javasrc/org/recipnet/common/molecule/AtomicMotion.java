/*
 * Reciprocal Net Project
 *
 * AtomicMotion.java
 *
 * 28-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;


/**
 * Represents the thermal motion of an atom in space, as comprised by some
 * molecular models.  Instances can be obtained based on any of the four
 * CIF-supported forms of thermal motion description, but all produce equivalent
 * internal representations.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomicMotion {
    
    /**
     * The multiplicative scale factor appropriate for converting thermal
     * parameters (isotropic or anisotropic) from the "U" convention provided
     * by this class to the alternative "B" convention also supported by CIF.
     * Numerically, its value is 8&pi;<sup>2</sup>.
     */
    public final static double U_TO_B_SCALE_FACTOR
            = 1 / AtomicMotionFactory.B_TO_U_SCALE_FACTOR;
    
    /**
     * The lower triangle of the thermal motion tensor
     */
    private final double[][] anisoU;
    
    /**
     * The mean squared amplitude of thermal motion
     */
    private final double isoU;
    
    /**
     * A flag indicating whether this thermal motion is (explicitly) anisotropic
     */
    private final boolean anisotropic;
    
    /**
     * Initializes a {@code AtomicMotion} with the specified parameters.  This
     * private constructor is invoked by each of this class' four static factory
     * methods
     * 
     * @param  uComponents a {@code double[][]} containing the lower triangle of
     *         the thermal motion tensor in "U" format; a reference to this
     *         array is maintained by the new {@code AtomicMotion}
     *         (<i>i.e.</i> the array is not copied); may be {@code null},
     *         provided that the {@code anisotropic} flag is {@code false};
     *         if not {@code null} then all elements must be non-{@code null}
     *         and of sufficient length
     * @param  u the mean squared amplitude of the thermal motion described by
     *         this {@code AtomicMotion}; should be consistent with the thermal
     *         motion tensor if that is provided
     * @param  anisotropic {@code true} if this {@code AtomicMotion} is
     *         explicitly anisotropic, in which case an appropriate thermal
     *         motion tensor should be provided as {@code uComponents};
     *         {@code false} otherwise
     *
     * @throws IllegalArgumentException if anisotropic <b>U</b> components are
     *         specified but are not of an appropriate shape to represent the
     *         lower triangle of the 3 by 3 <b>U</b> tensor
     */
    AtomicMotion(double[][] uComponents, double u, boolean anisotropic) {
        
        // Verify the arguments
        if (uComponents == null) {
            if (anisotropic) {
                throw new NullPointerException(
                        "Anisotropic components not specified");
            }
        } else if (uComponents.length != 3) {
            throw new IllegalArgumentException("Wrong number of U tensor rows");
        } else {
            for (int i = 0; i < uComponents.length; i++) {
                if (uComponents[i] == null) {
                    throw new NullPointerException("Null U tensor row");
                } else if (uComponents[i].length < (i + 1)) {
                    throw new IllegalArgumentException(
                            "U tensor row is too short");
                }
            }
        }
        
        // set the member variables
        this.anisoU = uComponents;
        this.isoU = u;
        this.anisotropic = anisotropic;
    }
    
    /**
     * Returns the value of this {@code AtomicMotion}'s anisotropy flag.  If
     * that flag is {@code true} then a thermal motion tensor will be available
     * from {@link #getAnisotropicU()}; otherwise a thermal motion tensor might
     * or might not be available.
     * 
     * @return {@code true} if this {@code AtomicMotion} is explicitly
     *         anisotropic; {@code false} if it is formally isotropic (though
     *         a thermal motion tensor may nevertheless be available)
     */
    public boolean isAnisotropic() {
        return anisotropic;
    }

    /**
     * Returns the isotropic "U" parameter describing this thermal motion; this
     * is the overall mean square amplitude of vibration, whether originally
     * specified in that form or computed from a thermal motion tensor
     * 
     * @return the isotropic "U" parameter as a {@code double}
     */
    public double getIsotropicU() {
        return isoU;
    }
    
    /**
     * Returns the anisotropic atomic motion tensor represented by this atomic
     * motion, if available.  The atomic motion tensor is always available if
     * this atomic motion is anisotropic; it may also be available if the
     * motion is isotropic, depending on whether a reference unit cell was
     * specified at instantiation.  The resulting array may be freely
     * manipulated without effect on this motion.
     * 
     * @return a {@code double[][]} containing the lower triangle of the thermal
     *         motion tensor, or {@code null} if none is available
     */
    public double[][] getAnisotropicU() {
        if (anisoU == null) {
            return null;
        } else {
            double[][] rval = new double[anisoU.length][];
            
            for (int i = 0; i < anisoU.length; i++) {
                rval[i] = anisoU[i].clone();
            }
            
            return rval;
        }
    }
}
