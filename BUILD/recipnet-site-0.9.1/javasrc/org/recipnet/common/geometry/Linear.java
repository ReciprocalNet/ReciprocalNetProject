/*
 * Reciprocal Net Project
 *
 * Linear.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * An interface defining common behaviors for objects representing
 * one-dimensional objects (lines, line segments, rays, etc.)
 * 
 * @author jobollin
 * @version 0.9.0
 */
public interface Linear {

    /**
     * Returns a point on this {@code Linear}
     * 
     * @return a {@code Point} representing a point on this line 
     */
    public Point getReferencePoint();
    
    /**
     * Returns the direction of this {@code Linear}
     * 
     * @return a {@code Vector} of length 1.0 representing the direction of this
     *         line; of the two possible (in some cases) such vectors, this line
     *         will always return the same one
     */
    public Vector getDirection();

    /**
     * Returns a {@code Point} representing the point on this {@code Linear}
     * closest to the specified point
     * 
     * @param  p a {@code Point} representing the point for which the nearest
     *         point on this line is requested
     *         
     * @return a {@code Point} representing the point on this {@code Linear}
     *         closest to {@code p}
     */
    public Point nearestPoint(Point p);
}
