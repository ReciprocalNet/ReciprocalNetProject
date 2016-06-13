/*
 * Reciprocal Net Project
 *
 * CoordinateLine.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * A class representing a coordinate line in three-dimensional Cartesian space:
 * a line with a fixed origin, unit length, and positive sense relative to which
 * coordinates both on and off the line can be computed
 * 
 * @author jobollin
 * @version 0.9.0
 */
public final class CoordinateLine implements Linear {
    
    /**
     * The {@code Point} that serves as the origin of this line's coordinate
     * system
     */
    private final Point origin;
    
    /**
     * A unit {@code Vector} definining the positive direction of this line 
     */
    private final Vector direction;
    
    /**
     * The unit length of this line; will be positive
     */
    private final double unitLength;

    /**
     * Initializes a new {@code CoordinateLine} with the specified reference
     * point and direction
     * 
     * @param  p a {@code Point} representing this line's reference point;
     *         serves also as the origin for this line's relative coordinates
     * @param  dir a {@code Vector} representing this line's direction
     */
    public CoordinateLine(Point p, Vector dir) {
        if (p == null) {
            throw new NullPointerException("Null origin");
        }
        origin = p;
        unitLength = dir.length();
        direction = dir.normalize();
    }
    
    /**
     * Initializes a new {@code CoordinateLine} having the specified points at
     * coordinates 0.0 and 1.0, respectively
     * 
     * @param  p1 a {@code Point} representing one point on the line; this one
     *         is used as the line's reference point and coordinate origin
     * @param  p2 a second {@code Point} representing a different point on the
     *         line; the vector from {@code p1} to {@code p2} defines the
     *         direction and unit length of this line
     */
    public CoordinateLine(Point p1, Point p2) {
        this(p1, new Vector(p1, p2));
    }
    
    /**
     * Returns the reference origin for this linear entity
     * 
     * @return a {@code Point} representing the reference origin of this linear
     *         entity 
     */
    public Point getReferencePoint() {
        return origin;
    }
    
    /**
     * Returns the direction for this linear entity
     * 
     * @return a {@code Vector} of length 1.0 representing the direction of this
     *         linear entity 
     */
    public Vector getDirection() {
        return direction;
    }
    
    /**
     * Returns the unit length of this {@code CoordinateLine}
     * 
     * @return the unit length of this {@code CoordinateLine}, as a
     *         {@code double}
     */
    public double getUnitLength() {
        return unitLength;
    }

    /**
     * Returns the coordinate of the specified point along this linear entity.
     * The coordinate is the signed Cartesian distance from this line's
     * reference point to the point on this line nearest to the specified point,
     * divided by this line's unit length.
     * 
     * @param p a {@code Point} representing the origin for which a coordinate
     *        is desired
     * @return the coordinate of {@code p} relative to this entity's direction
     *         and origin
     */
    public double coordinateOf(Point p) {
        return direction.dotProduct(new Vector(origin, p)) / unitLength;
    }
    
    /**
     * Computes and returns the point along this line line that corresponds to
     * the specified coordinate (relative to this line's reference origin and
     * unit length)
     * 
     * @param coord the {@code double} coordinate of the desired point
     * @return a {@code Point} representing the point at the requested
     *         coordinate
     */
    public Point pointAt(double coord) {
        return direction.scale(coord * unitLength).endPoint(origin);
    }
    
    /**
     * Returns a {@code Point} representing the point on this line closest to
     * the specified point
     * 
     * @param  p a {@code Point} representing the point for which the nearest
     *         point on this line is requested
     *         
     * @return a {@code Point} representing the point on this line closest to
     *         {@code p}
     */
    public Point nearestPoint(Point p) {
        return pointAt(coordinateOf(p));
    }

    /**
     * Computes the shortest distance from the specified point to this line 
     * 
     * @param  p the {@code Point} to which distance should be computed
     *  
     * @return the distance as a {@code double}
     */
    public double distanceTo(Point p) {
        return nearestPoint(p).distanceTo(p);
    }
}
