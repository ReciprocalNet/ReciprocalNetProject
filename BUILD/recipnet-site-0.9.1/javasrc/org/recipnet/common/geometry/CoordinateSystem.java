/*
 * Reciprocal Net Project
 *
 * CoordinateSystem.java
 *
 * 09-Nov-2005: jobollin adopted this class from general-purpose IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * A class representing an arbitrary three-dimensional coordinate system
 * expressed in terms of a Cartesian coordinate system over the same space
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CoordinateSystem {

    /**
     * The origin of this coordinate system in Cartesian space
     */
    private final Point origin;

    /**
     * The coordinate system basis vectors expressed in the underlying Cartesian
     * coordinates
     */
    private final Vector[] vectors;

    /**
     * The three distinct planes passing through the origin that each contain
     * two of this coordinate system's basis vectors; used in coordinate
     * computations
     */
    private final DirectedPlane[] coordinatePlanes;

    /**
     * <p>
     * The (perpendicular) distances between the three pairs of parallel planes
     * defined by the origin and basis vectors; used in coordinate computations.
     * </p><p> 
     * Each pair of basis vectors defines a plane passing through the coordinate
     * system origin.  The image of each such plane under the out-of-plane basis
     * vector is a parallel plane.  The {@code planeDistances} array contains
     * the distances between these pairs of planes, arranged in the same order
     * as the out-of-plane basis vectors corresponding to the distances.
     * </p>
     */
    private final double[] planeDistances;

    /**
     * Initializes a new {@code CoordinateSystem} with the specified origin and
     * translation vectors
     * 
     * @param  p a {@code Point} representing the origin of this coordinate
     *         system
     * @param  v a {@code Vector[]} of length 3 containing the basis vectors for
     *         this coordinate system; they should not be mutually coplanar, and
     *         they should be in right-handed sequence
     *
     * @throws IllegalArgumentException if the provided vectors are coplanar or
     *         in left-handed sequence, or if any has a coordinate that is
     *         infinite or not-a-number
     */
    public CoordinateSystem(Point p, Vector[] v) {
        if (p == null) {
            throw new NullPointerException("null origin");
        } else if (v.length != 3) {  // also ensures v != null
            throw new IllegalArgumentException("wrong number of vectors: "
                    + v.length);
        } else {
            for (Vector vector : v) {
                for (double d : vector.getCoordinates()) {
                    if (Double.isInfinite(d) || Double.isNaN(d)) {
                        throw new IllegalArgumentException(
                                "non-finite vector coordinate");
                    }
                }
            }
        }

        if ((v[0].crossProduct(v[1])).dotProduct(v[2]) <= 0.0) {
            throw new IllegalArgumentException(
                    "Coplanar or left-handed vectors");
        }

        origin = p;
        vectors = v.clone();

        // Compute boundary planes and interplanar distances
        coordinatePlanes = new DirectedPlane[3];
        planeDistances = new double[3];

        for (int i = 0; i < 3; i++) {
            Vector vx = vectors[(i + 1) % 3];
            Vector vy = vectors[(i + 2) % 3];

            coordinatePlanes[i] = new DirectedPlane(p, vx, vy);
            planeDistances[i] = coordinatePlanes[i].getNormal().dotProduct(
                    vectors[i]);
        }
    }

    /**
     * Initializes a new {@code CoordinateSystem} with the specified dimensions
     * and shape, and its origin at the Cartesian origin
     * 
     * @param  a the length of the first basis vector; should be positive
     * @param  b the length of the second basis vector; should be positive
     * @param  c the length of the third basis vector; should be positive
     * @param  cosAlpha the angle between the second and third vectors; should
     *         be between -1 and 1 (inclusive), and {@code Math.acos(cosAlpha) +
     *         Math.acos(cosBeta) + Math.acos(cosGamma)} should be less than
     *         2&pi;
     * @param  cosBeta the angle between the first and third vectors; should be
     *         between -1 and 1 (inclusive)
     * @param  cosGamma the angle between the first and second vectors; should
     *         be between -1 and 1 (inclusive)
     *         
     * @throws IllegalArgumentException if any of the arguments is out of range,
     *         or if the angle cosines are collectively impossible (i.e. they
     *         correspond to angles whose sum is >= 2<i>&pi;</i>) 
     */
    public CoordinateSystem(double a, double b, double c, double cosAlpha,
            double cosBeta, double cosGamma) {
        this(Point.ORIGIN,
                computeVectors(a, b, c, cosAlpha, cosBeta, cosGamma));
    }

    /**
     * Computes Cartesian representations of three vectors having the specified
     * lengths and intervector angles
     * 
     * @param  a the length of the first vector; should be positive
     * @param  b the length of the second vector; should be positive
     * @param  c the length of the third vector; should be positive
     * @param  cosAlpha the angle between the second and third vectors; should
     *         be between -1 and 1 (exclusive), and {@code Math.acos(cosAlpha) +
     *         Math.acos(cosBeta) + Math.acos(cosGamma)} should be less than
     *         2&pi;
     * @param  cosBeta the angle between the first and third vectors; should be
     *         between -1 and 1 (exclusive)
     * @param  cosGamma the angle between the first and second vectors; should
     *         be between -1 and 1 (exclusive)
     * @return a {@code Vector[]} containing the three vectors
     */
    private static Vector[] computeVectors(double a, double b, double c,
            double cosAlpha, double cosBeta, double cosGamma) {
        
        if ((a <= 0) || (b <= 0) || (c <= 0)) {
            throw new IllegalArgumentException("Non-positive axis length");
        } else if ((cosAlpha <= -1) || (cosAlpha >= 1)
                || (cosBeta <= -1)  || (cosBeta >= 1)
                || (cosGamma <= -1) || (cosGamma >= 1)) {
            throw new IllegalArgumentException("Out-of-range angle cosine");
        }
        
        // Choosing the positive square root places beta between zero and pi:
        final double sinBeta = Math.sqrt(1.0 - cosBeta * cosBeta);
        
        return new Vector[] {
                
                /*
                 * The first vector is chosen along the x axis
                 */
                new Vector(a, 0, 0),
                
                /*
                 * The second vector is chosen so as to form the correct angles
                 * with the other two vectors; of the two possible choices, the
                 * one that makes the coordinate system right-handed is chosen
                 * (by using the positive square root in the third coordinate).
                 */
                new Vector(b * cosGamma,
                        b * Math.sqrt(1.0 + 2.0 * cosAlpha * cosBeta * cosGamma
                                - (cosAlpha * cosAlpha) - (cosBeta * cosBeta)
                                - (cosGamma * cosGamma)) / sinBeta,
                        b * (cosAlpha - cosBeta * cosGamma) / sinBeta),
                                
                /*
                 * The third vector is chosen in the x/z plane so that it makes
                 * the correct angle with the first vector
                 */
                new Vector(c * cosBeta, 0, c * sinBeta)
                        
        };
    }

    /**
     * Returns the origin of this coordinate system
     * 
     * @return the origin as a {@code Point}
     */
    public Point getOrigin() {
        return origin;
    }

    /**
     * Returns an array containing the basis vectors of this coordinate system
     * 
     * @return the basis vectors as a {@code Vector[]}
     */
    public Vector[] getVectors() {
        return vectors.clone();
    }
    
    /**
     * Returns an array containing the three planes defined by this coordinate
     * system's origin and pairs of its basis vectors.  The plane at index 0
     * contains the vectors at indices 1 and 2 (as returned by
     * {@link #getVectors()}), and so forth, and the normal of the plane at
     * index i makes an angles less than <i>&pi;</i>/2 with the vector of the
     * same index.
     * 
     * @return a {@code Planar[]} containing the three coordinate planes
     */
    public Planar[] getCoordinatePlanes() {
        return coordinatePlanes.clone();
    }

    /**
     * Returns this {@code CoordinateSystem}'s reciprocal coordinate system
     *  
     * @return a {@code CoordinateSystem} representing the reciprocal to this
     *         coordinate system
     */
    public CoordinateSystem getReciprocal() {
        return new CoordinateSystem(origin, new Vector[] {
                coordinatePlanes[0].getNormal().scale(1.0 / planeDistances[0]),
                coordinatePlanes[1].getNormal().scale(1.0 / planeDistances[1]),
                coordinatePlanes[2].getNormal().scale(1.0 / planeDistances[2])
        });
    }

    /**
     * Computes and returns the coordinates for the specified Cartesian point
     * relative to this {@code CoordinateSystem}
     * 
     * @param  p the {@code Point} for which coordinates are requested
     * 
     * @return a {@code double[]} of length 3 containing the {@code x},
     *         {@code y}, and {@code z} coordinates of {@code p} relative to
     *         this coordinate system
     */
    public double[] coordinatesOf(Point p) {
        double[] rval = new double[3];

        for (int i = 0; i < 3; i++) {
            rval[i] = coordinatePlanes[i].signedDistanceTo(p)
                    / planeDistances[i];
        }

        return rval;
    }

    /**
     * Computes and returns the coordinates for the specified Cartesian vector
     * relative to this {@code CoordinateSystem}'s basis vectors
     * 
     * @param  v the {@code Vector} for which coordinates are requested
     * 
     * @return a {@code double[]} of length 3 containing the {@code x},
     *         {@code y}, and {@code z} coordinates of {@code v} relative to
     *         this coordinate system's basis vectors
     */
    public double[] coordinatesOf(Vector v) {
        return coordinatesOf(v.endPoint(getOrigin()));
    }
    
    /**
     * Computes a Cartesian point specified relative to this coordinate system's
     * origin in terms of coefficients of this coordinate system's basis
     * vectors.
     *  
     * @param  x the first coordinate of the desired point
     * @param  y the second coordinate of the desired point
     * @param  z the third coordinate of the desired point
     * 
     * @return a {@code Point} representing the specified point
     */
    public Point pointFor(double x, double y, double z) {
        return vectorFor(x, y, z).endPoint(getOrigin());
    }
    
    /**
     * Computes a Cartesian vector specified in terms of coefficients of this
     * coordinate system's basis vectors.
     *  
     * @param  x the first coordinate of the desired vector
     * @param  y the second coordinate of the desired vector
     * @param  z the third coordinate of the desired vector
     * 
     * @return a {@code Vector} representing the specified vector
     */
    public Vector vectorFor(double x, double y, double z) {
        return vectors[0].scale(x).sum(
                vectors[1].scale(y)).sum(
                vectors[2].scale(z));
    }
}
