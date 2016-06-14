/*
 * Reciprocal Net Project
 *
 * DirectedPlane.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * <p>
 * A class representing a plane in 3D Cartesian space.  The main behaviors
 * provided by this class are computation of the perpendicular distance to a
 * specified point, and computation of the angle formed with a specified
 * vector.
 * </p><p>
 * Instances are oriented, in the sense that their
 * {@link #signedDistanceTo(Point)} methods return a signed value, with sign
 * depending on the disposition of the point relative to the plane and its
 * chosen normal vector.
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class DirectedPlane implements Planar {
    
    /**
     * A unit vector normal to this plane, defining the orientation of this
     * plane
     */
    private final Vector normal;
    
    /**
     * The signed, perpendicular distance from the Cartesian origin to this
     * plane; useful in determining the distance from any point to this plane
     */
    private final double intersectionParameter;
    
    /**
     * Initializes a new {@code DirectedPlane} passing through the specified
     * point and normal to the specified vector.  The specific vector chosen
     * from among any particular family of colinear vectors matters only in that
     * it affects the signs of the distances returned from the
     * {@link #signedDistanceTo(Point)} method; the plane is not well-defined if
     * the vector is of length zero.
     * 
     * @param  p a {@code Point} representing a point in this plane
     * @param  norm a {@code Vector} representing the normal to this plane
     * 
     * @throws IllegalArgumentException if {@code normal} is a zero vector
     */
    public DirectedPlane(Point p, Vector norm) {
        if (norm.length() == 0.0) {
            throw new IllegalArgumentException("Plane normal has length zero");
        }
        
        normal = norm.normalize();
        intersectionParameter = normal.dotProduct(new Vector(Point.ORIGIN, p));
    }

    /**
     * Initializes a new {@code DirectedPlane} representing the plane described
     * by the specified point and two vectors; its normal N is directed so that
     * v1, v2, and N form a right-handed sequence of vectors.  The plane is not
     * well defined if the two vectors are parallel or if either one is of
     * length zero.
     * 
     * @param  p a {@code Point} representing a point in the plane
     * @param  v1 a {@code Vector} representing one of two non-colinear vectors
     *         defining the desired plane 
     * @param  v2 a {@code Vector} representing the second of two non-colinear
     *         vectors defining the desired plane
     *         
     * @throws IllegalArgumentException if {@code v1} and {@code v2} are
     *         colinear
     */
    public DirectedPlane(Point p, Vector v1, Vector v2) {
        this(p, v1.crossProduct(v2));
    }
    
    /**
     * Initializes a new {@code DirectedPlane} representing the plane containing
     * the specified linear entity and point; the plane is not well-defined if
     * the point is on the line containing the linear entity 
     * 
     * @param  l a {@code Linear} representing a line contained in this plane
     * @param  p a {@code Point} representing a point contained in this plane,
     *         but not on line {@code l} 
     */
    public DirectedPlane(Linear l, Point p) {
        this(l.getReferencePoint(), l.getDirection(),
             new Vector(l.getReferencePoint(), p));
    }
    
    /**
     * Returns the normal vector for this plane, which is guaranteed to have
     * length 1.0.  For any particular plane, there are two distinct normal
     * vectors of length 1; this method will always return the same one, but
     * which one that is depends on the way in which this {@code DirectedPlane}
     * was initialized.
     * 
     * @return returns the normal vector as a {@code Vector}
     */
    public Vector getNormal() {
        return normal;
    }
    
    /**
     * Computes the shortest distance from the specified point to this plane
     * (which is the distance as measured perpendicular to this plane)
     * 
     * @param  p the {@code Point} to which the shortest distance is requested
     * 
     * @return the shortest distance from the specified point to this plane, as
     *         a {@code double}
     */
    public double distanceTo(Point p) {
        return Math.abs(signedDistanceTo(p));
    }

    /**
     * {@inheritDoc}
     * 
     * @see Planar#angleWith(Vector)
     */
    public double angleWith(Vector v) {
        return Math.abs(signedAngleWith(v));
    }

    /**
     * Returns the point in this plane nearest to the specified point, which
     * is the specified point itself if it is exactly in the plane 
     * 
     * @param  p the {@code Point} for which the nearest point in this plane is
     *         requested
     * 
     * @return a {@code Point} representing the point in this plane nearest to
     *         {@code p}
     */
    public Point nearestPoint(Point p) {
        double distance = signedDistanceTo(p);
        
        return (distance == 0.0) ? p : getNormal().scale(-distance).endPoint(p);
    }

    /**
     * Computes the signed perpendicular distance between this plane and the
     * specified point.  The distance is positive if the point is on the side
     * of the plane pointed to by the plane's chosen normal vector, negative if
     * it is on the other side, or zero if the point is on the plane 
     * 
     * @param  p the {@code Point} to which the distance should be computed
     *  
     * @return the distance
     */
    public double signedDistanceTo(Point p) {
        return normal.dotProduct(new Vector(Point.ORIGIN, p))
                - intersectionParameter;
    }
    
    /**
     * Computes and returns the angle between this plane and the specified
     * vector (or more precisely, <em>&pi;</em>/2 minus the angle between the
     * vector and this plane's normal).  The value is signed, with sign
     * determined in a manner similar to that used by
     * {@link #signedDistanceTo(Point)}
     * 
     * @param v
     * @return the angle
     */
    public double signedAngleWith(Vector v) {
        return (Math.PI / 2) - getNormal().angleWith(v);
    }

    /**
     * Determines whether this plane's normal is directed toward the side of the
     * plane on which the specified point is found
     * 
     * @param  p the {@code Point} against which the direction of this plane is
     *         to be compared
     *         
     * @return {@code true} if this plane's normal is directed toward the side
     *         of this plane on which {@code p} is found or if {@code p} is
     *         exactly on this plane; otherwise {@code false} 
     */
    public boolean isDirectedToward(Point p) {
        return (signedDistanceTo(p) >= 0.0);
    }
    
}
