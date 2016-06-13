/*
 * Reciprocal Net Project
 *
 * Point.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * A class representing a point in 3D Cartesian space.  Instances are immutable.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public final class Point extends AbstractCoordinates {
    
    /*
     * Note: immutability of instances is, strictly speaking, a promise of the
     * package, not the class, because the AbstractCoordinates base class
     * provides package-private access to the internal coordinate array.  Client
     * code can never cause an existing Point to change state, directly or
     * indirectly, but this class may sometimes create a new Point and mutate it
     * one or more times before handing it off to external code. 
     */
    
    /** a {@code Point} representing the origin of the 3-D coordinate system */ 
    public final static Point ORIGIN = new Point(0.0, 0.0, 0.0);
    
    /**
     * Initializes a new {@code Point} with all coordinates zero
     */
    private Point() {
        this(0.0, 0.0, 0.0);
    }
    
    /**
     * Initializes a new {@code Point} with the coordinates from the specified
     * array
     * 
     * @param  coordinates a {@code double[]} of length 3 containing the
     *         coordinates for this {@code Point}; no reference to this array
     *         is retained by this object
     * 
     * @throws IllegalArgumentException if the length of the coordinate array is
     *         not 3
     */
    public Point(double[] coordinates) {
        
        // Array length check provided by the superclass
        
        super(coordinates.clone());
    }
    
    /**
     * Initializes a new {@code Point} with the specified coordinates
     * 
     * @param  x the x coordinate for this {@code Point}
     * @param  y the y coordinate for this {@code Point}
     * @param  z the z coordinate for this {@code Point}
     */
    public Point(double x, double y, double z) {
        super(new double[] {x, y, z});
    }
    
    /**
     * Computes the midpoint (unweighted centroid) of the specified points and
     * returns a {@code Point} representation of it
     * 
     * @param  points one or more {@code Point}s for which to find a midpoint
     *         
     * @return a Point representing the unweighted centroid of the specified
     *         points; behavior is undefined when the number of points specified
     *         is zero
     */
    public static Point midPoint(Point... points) {
        Point rval = new Point();
        
        // need to modify this array:
        double[] c = rval.getInternalCoordinates();
        
        for (Point point : points) {
            
            // must not modify this array:
            double[] c1 = point.getInternalCoordinates();
        
            for (int i = 0; i < 3; i++) {
                c[i] += c1[i];
            }
        }

        // Will produce NaNs if there were no points; this is natural:
        for (int i = 0; i < 3; i++) {
            c[i] /= points.length;
        }
        
        return rval;
    }
    
    /**
     * Computes the Cartesian distance between this point and the specified one
     * 
     * @param  p  the {@code Point} to which to compute the distance
     *  
     * @return the distance between this point and the specified one
     */
    public double distanceTo(Point p) {
        return Math.sqrt(dSqrTo(p));
    }
    
    /**
     * Computes the square of the Cartesian distance between this {@code Point}
     * and the specified one.  This computation involves only arithmetic
     * (no transcendental functions).
     * 
     * @param  p the {@code Point} to which to compute the square of the
     *         distance
     *         
     * @return the square of the Cartesian distance from this point to the
     *         specified one
     */
    public double dSqrTo(Point p) {
        double accum = 0.0;
        
        // must not modify this array:
        double[] c1 = this.getInternalCoordinates();
        
        // must not modify this array:
        double[] c2 = p.getInternalCoordinates();
        
        for (int i = 0; i < 3; i++) {
            double t = (c1[i] - c2[i]);
            
            accum += (t * t);
        }
        
        return accum;
    }
}
