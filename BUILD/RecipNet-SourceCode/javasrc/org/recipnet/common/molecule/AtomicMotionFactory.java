/*
 * Reciprocal Net Project
 *
 * AtomicMotionFactory.java
 *
 * 30-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import org.recipnet.common.algebra.SimpleMatrix;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Planar;
import org.recipnet.common.geometry.Vector;

/**
 * A factory for {@link AtomicMotion} objects referred to a specific coordinate
 * system / unit cell.  Some comparatively complex computations that depend only
 * on the coordinate system are performed once by each instance of this class
 * and cached, thus avoiding recomputation for each {@code AtomicMotion} object
 * created.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomicMotionFactory {

    /**
     * The multiplicative scale factor appropriate for converting thermal
     * parameters (isotropic or anisotropic) from the "B" convention provided by
     * this class to the alternative "U" convention also supported by CIF.
     * Numerically, its value is 1/(8<i>&pi;</i><sup>2</sup>).
     */
    final static double B_TO_U_SCALE_FACTOR = 1 / (8 * Math.PI * Math.PI);
    
    /**
     * A multiplicative scale factor used internally in converting anisotropic
     * thermal parameters from the beta convention of the IUMSC XTEL library to
     * the "U" convention defined by CIF.  Numerically, its value is
     * 1/(2<i>&pi;</i><sup>2</sup>).
     */
    private final static double BETA_SCALE_FACTOR = 1 / (2 * Math.PI * Math.PI);

    /**
     * The lower triangle of a "skewing tensor" appropriate for the coordinate
     * system with which this factory was configured; used to account for the
     * coordinate system shape in computing anisotropic representations of
     * isotropic thermal parameters 
     */
    private final double[][] skewingTensor;

    /**
     * The "weighting tensor" used in computing an isotropic displacement
     * parameter representing the same degree of motion as described by a
     * particular set of anisotropic displacement parameters.
     */
    private final double[][] weightingTensor;
    
    /**
     * The lengths of the three axis vectors of the reciprocal coordinates of
     * the coordinate system with which this factory was configured; used in
     * building atomic motions from the "beta" form of motion tensor description
     */
    private final double[] reciprocalLengths;

    /**
     * Initializes a {@code AtomicMotionFactory} for use in creating
     * {@code AtomicMotion} objects referred to the specified coordinate
     * system.
     * 
     * @param  unitCell a {@code CoordinateSystem} representing the coordinate
     *         system to which the {@code AtomicMotion} objects created by this
     *         factory are referred; no reference to this object is retained by
     *         the factory instance
     */
    public AtomicMotionFactory(CoordinateSystem unitCell) {
        Vector[] reciprocalVectors = unitCell.getReciprocal().getVectors();
        
        reciprocalLengths = new double[] {
                reciprocalVectors[0].length(), reciprocalVectors[1].length(),
                reciprocalVectors[2].length()
        };
        skewingTensor = computeSkewingTensor(unitCell);
        weightingTensor = computeWeightingTensor(unitCell);
    }

    /**
     * Creates and returns a new {@code AtomicMotion} for the isotropic motion
     * described by the specified "U" parameter, in the unit cell (if any)
     * described by the specified coordinate system.
     * 
     * @param  isoU the isotropic U parameter as a {@code double}, as defined by
     *         the CIF core dictionary for <a href=
     *         "http://www.iucr.org/iucr-top/cif/cifdic_html/1/cif_core.dic/Iatom_site_U_iso_or_equiv.html"
     *         >_atom_site_U_iso_or_equiv</a>
     * @return a {@code AtomicMotion} instance representing the specified
     *         isotropic motion
     */
    public AtomicMotion motionForIsotropicU(double isoU) {
        double[][] aniso;

        aniso = new double[3][];
        for (int i = 0; i < 3; i++) {
            aniso[i] = skewingTensor[i].clone();
            for (int j = 0; j <= i; j++) {
                aniso[i][j] *= isoU;
            }
        }

        return new AtomicMotion(aniso, isoU, false);
    }

    /**
     * Creates and returns a new {@code AtomicMotion} for the isotropic motion
     * described by the specified "B" parameter, in the unit cell (if any)
     * described by the specified coordinate system.
     * 
     * @param  isoB the isotropic B parameter as a {@code double}, as defined by
     *         the CIF core dictionary for <a href=
     *         "http://www.iucr.org/iucr-top/cif/cifdic_html/1/cif_core.dic/Iatom_site_B_iso_or_equiv.html"
     *         >_atom_site_B_iso_or_equiv</a>
     * @return a {@code AtomicMotion} instance representing the specified
     *         isotropic motion
     */
    public AtomicMotion motionForIsotropicB(double isoB) {
        return motionForIsotropicU(isoB * B_TO_U_SCALE_FACTOR);
    }

    /**
     * Creates and returns a new {@code AtomicMotion} for the anisotropic
     * motion described by the specified "U<sub>ij</sub>" parameters, <a
     * href="http://www.iucr.org/iucr-top/cif/cifdic_html/1/cif_core.dic/Iatom_site_aniso_U_.html">as
     * described in the CIF Core dictionary</a>.
     * 
     * @param  u11 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row one, column one, corresponding to CIF data name
     *         {@code _atom_site_aniso_U_11}
     * @param  u22 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row two, column two, corresponding to CIF data name
     *         {@code _atom_site_aniso_U_22}
     * @param  u33 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row three, column three, corresponding to CIF data name
     *         {@code _atom_site_aniso_U_33}
     * @param  u12 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row one, column two (and row two, column one), corresponding
     *         to CIF data name {@code _atom_site_aniso_U_12}
     * @param  u13 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row one, column three (and row three, column one),
     *         corresponding to CIF data name {@code _atom_site_aniso_U_13}
     * @param  u23 the element of the <b>U</b><sub><i>ij</i></sub> tensor in
     *         row two, column three (and row three, column two),
     *         corresponding to CIF data name {@code _atom_site_aniso_U_23}
     * @return a {@code AtomicMotion} instance representing the specified
     *         anisotropic motion
     */
    public AtomicMotion motionForAnisotropicU(double u11, double u22,
            double u33, double u12, double u13, double u23) {
        double[][] aniso = new double[][] { { u11 }, { u12, u22 },
                { u13, u23, u33 } };

        return new AtomicMotion(aniso, computeEqivalentU(aniso), true);
    }

    /**
     * Creates and returns a new {@code AtomicMotion} for the anisotropic
     * motion described by the specified "B<sub>ij</sub>" parameters, <a
     * href="http://www.iucr.org/iucr-top/cif/cifdic_html/1/cif_core.dic/Iatom_site_aniso_B_.html">as
     * described in the CIF Core dictionary</a>.
     * 
     * @param  b11 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row one, column one, corresponding to CIF data name
     *         {@code _atom_site_aniso_B_11}
     * @param  b22 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row two, column two, corresponding to CIF data name
     *         {@code _atom_site_aniso_B_22}
     * @param  b33 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row three, column three, corresponding to CIF data name
     *         {@code _atom_site_aniso_B_33}
     * @param  b12 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row one, column two (and row two, column one), corresponding
     *         to CIF data name {@code _atom_site_aniso_B_12}
     * @param  b13 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row one, column three (and row three, column one),
     *         corresponding to CIF data name {@code _atom_site_aniso_B_13}
     * @param  b23 the element of the <b>B</b><sub><i>ij</i></sub> tensor in
     *         row two, column three (and row three, column two),
     *         corresponding to CIF data name {@code _atom_site_aniso_B_23}
     * @return a {@code AtomicMotion} instance representing the specified
     *         anisotropic motion
     */
    public AtomicMotion motionForAnisotropicB(double b11, double b22,
            double b33, double b12, double b13, double b23) {
        return motionForAnisotropicU(b11 * B_TO_U_SCALE_FACTOR, b22
                * B_TO_U_SCALE_FACTOR, b33 * B_TO_U_SCALE_FACTOR, b12
                * B_TO_U_SCALE_FACTOR, b13 * B_TO_U_SCALE_FACTOR, b23
                * B_TO_U_SCALE_FACTOR);
    }

    /**
     * Creates and returns a new {@code AtomicMotion} for the anisotropic
     * motion described by the specified "&beta;<sub>ij</sub>" parameters, as
     * used in the IUMSC XTEL library.  These correspond to ORTEP type 0 thermal
     * parameters.
     * 
     * @param  beta11 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row one, column one
     * @param  beta22 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row two, column two
     * @param  beta33 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row three, column three
     * @param  beta12 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row one, column two (and row two, column one)
     * @param  beta13 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row one, column three (and row three, column one)
     * @param  beta23 the element of the <b>&beta;</b><sub><i>ij</i></sub>
     *         tensor in row two, column three (and row three, column two)
     *         
     * @return a {@code AtomicMotion} instance representing the specified
     *         anisotropic motion
     */
    public AtomicMotion motionForAnisotropicBeta(double beta11, double beta22,
            double beta33, double beta12, double beta13, double beta23) {
        return motionForAnisotropicU(
                (BETA_SCALE_FACTOR * beta11)
                        / (reciprocalLengths[0] * reciprocalLengths[0]),
                (BETA_SCALE_FACTOR * beta22)
                        / (reciprocalLengths[1] * reciprocalLengths[1]),
                (BETA_SCALE_FACTOR * beta33)
                        / (reciprocalLengths[2] * reciprocalLengths[2]),
                (BETA_SCALE_FACTOR * beta12)
                        / (reciprocalLengths[0] * reciprocalLengths[1]),
                (BETA_SCALE_FACTOR * beta13)
                        / (reciprocalLengths[0] * reciprocalLengths[2]),
                (BETA_SCALE_FACTOR * beta23)
                        / (reciprocalLengths[1] * reciprocalLengths[2])
        );
    }
    
    /**
     * Computes and returns the "skewing tensor" for the specified coordinate
     * system's reciprocal. If <i>u<sub>i</sub></i> (<i>i</i>=1,3) are unit
     * vectors along the three axial directions of the reciprocal coordinate
     * system, then the skewing tensor can be described as
     * (<b>T</b><sub><i>ij</i></sub>)&nbsp;=&nbsp;(<i>u<sub>i</sub>&nbsp;&middot;&nbsp;<i>u<sub>j</sub></i>),
     * where "&middot;" designates a dot product (inner product) of two vectors.
     * Because this tensor is symmetric, only the lower triangle need be
     * explicitly computed or returned.
     * 
     * @param  unitCell the {@code CoordinateSystem} for which a reciprocal
     *         skewing tensor is requested; typically one describing the unit
     *         cell to which a crystal structure is referred
     * @return a {@code double[][]} containing the lower triangle of the skewing
     *         tensor (those <b>T</b><sub><i>ij</i></sub> for which
     *         <i>j</i>&nbsp;&le;&nbsp;<i>i</i>)
     */
    private double[][] computeSkewingTensor(CoordinateSystem unitCell) {
        Planar[] directPlanes = unitCell.getCoordinatePlanes();
        Vector[] reciprocalDirections = new Vector[directPlanes.length];
        double[][] rval = new double[3][];

        for (int i = 0; i < 3; i++) {
            reciprocalDirections[i] = directPlanes[i].getNormal();
        }
        for (int i = 0; i < 3; i++) {
            rval[i] = new double[i + 1];
            rval[i][i] = 1.0;
            for (int j = 0; j < i; j++) {
                rval[i][j] = reciprocalDirections[i].dotProduct(
                        reciprocalDirections[j]);
            }
        }

        return rval;
    }

    /**
     * Computes the "weighting tensor" used by
     * {@link #computeEqivalentU(double[][])} to compute equivalent isotropic
     * thermal parameters from anisotropic ones, taking the coordinate system
     * into account.  In principle, the weighting tensor has the form 
     * (<b>W</b><sub><i>ij</i></sub>)&nbsp;=&nbsp;(1/3)(&Sigma;<sub><i>k</i></sub>U<sub><i>ij</i></sub>t<sub><i>ik</i></sub>t<sub><i>jk</i></sub>),
     * where the t<sub><i>ij</i></sub> are the elements of matrix T that
     * transforms Cartesian coordinates into a coordinate system with
     * unit-length basis vectors parallel with the reciprocal axis vectors.
     * For computational convenience, only the lower triangle is returned,
     * and the off-diagonal elements are scaled by two.
     *  
     * @param  unitCell the {@code CoordinateSystem} for which a weighting
     *         tensor is requested, normally representing the unit cell of some
     *         crystal structure in which thermal motions have been modeled
     *         
     * @return a {@code double[][]} containing the lower triangle of the
     *         weighting tensor (those <b>W</b><sub><i>ij</i></sub> for which
     *         <i>j</i>&nbsp;&le;&nbsp;<i>i</i>)
     */
    private double[][] computeWeightingTensor(CoordinateSystem unitCell) {
        CoordinateSystem reciprocalCell = unitCell.getReciprocal();
        Vector[] reciprocalVectors = reciprocalCell.getVectors();
        SimpleMatrix recipToCartMatrix = new SimpleMatrix(
                new double[][] {
                        reciprocalVectors[0].normalize().getCoordinates(),
                        reciprocalVectors[1].normalize().getCoordinates(),
                        reciprocalVectors[2].normalize().getCoordinates()
                }).getTranspose();
        double[][] cartToRecip = recipToCartMatrix.getInverse().getElements();
        double[][] rval = new double[3][];
        
        for (int i = 0; i < 3; i++) {
            rval[i] = new double[i + 1];
            for (int j = 0; j <= i; j++) {
                for (int k = 0; k < 3; k++) {
                    rval[i][j] += (cartToRecip[i][k] * cartToRecip[j][k]);
                }
                if (i != j) {
                    rval[i][j] *= 2;
                }
                rval[i][j] /= 3;
            }
        }
        
        return rval;
    }

    /**
     * Computes the space-averaged mean square atomic displacement parameter
     * corresponding to the specified anisotropic displacement parameters
     * 
     * @param  aniso the lower triangle of the anisotropic displacement
     *         parameter tensor, expressed according to the "U" convention
     *         
     * @return the space-averaged mean square atomic displacement parameter
     *         corresponding to the specified anisotropic displacement
     *         parameters; informally, the "equivalent isotropic U"
     */
    private double computeEqivalentU(double[][] aniso) {
        double u = 0.0;

        for (int i = 0; i < aniso.length; i++) {
            for (int j = 0; j <= i; j++) {
                u += aniso[i][j] * weightingTensor[i][j];
            }
        }

        return u;
    }
}
