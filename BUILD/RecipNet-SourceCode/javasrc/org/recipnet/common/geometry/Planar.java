/*
 * Reciprocal Net Project
 *
 * Planar.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * An interface defining the behavior of objects that represent geometric
 * planes or parts of planes (e.g. discs, half-planes, etc.).  Implementations
 * have normal vectors, can compute angles to arbitrary vectors, and can compute
 * the point on their surface nearest to any arbitrary point.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public interface Planar {
    
    /**
     * Returns the normal vector for this plane, which is guaranteed to have
     * length 1.0.  For any particular plane, there are two distinct normal
     * vectors of length 1; this method will always return the same one, but
     * which one that is is an implementation detail.
     * 
     * @return returns the normal vector as a {@code Vector}
     */
    public Vector getNormal();
    
    /**
     * Computes and returns the (unsigned) angle between this plane and the
     * specified vector, as a value between zero and <em>&pi;</em>/2
     * 
     * @param  v the {@code Vector} for which to compute an angle
     * 
     * @return the angle as a {@code double}
     */
    public double angleWith(Vector v);

    /**
     * Returns the point in this plane nearest to the specified point (which
     * is the specified point itself if it is exactly in this plane) 
     * 
     * @param  p the {@code Point} for which the nearest point in this plane is
     *         requested
     * 
     * @return a {@code Point} representing the point in this plane nearest to
     *         {@code p}
     */
    public Point nearestPoint(Point p);
}
