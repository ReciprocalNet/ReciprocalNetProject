/*
 * Reciprocal Net Project
 *
 * AbstractCoordinates.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * An abstract base class for geometric entities characterized by Cartesian
 * coordinates
 * 
 * @author jobollin
 * @version 0.9.0
 */
public abstract class AbstractCoordinates {

    private final double[] coordinates;

    /**
     * Initializes a new {@code AbstractCoordinates} with the specified
     * array as its coordinates.  A reference to the provided array is stored --
     * i.e. the array is not copied.
     * 
     * @param  coords the {@code double[]} to use for this object's coordinates;
     *         must have length 3.
     * 
     * @throws IllegalArgumentException if the length of the coordinate array is
     *         not 3
     */
    protected AbstractCoordinates(double[] coords) {
        if (coords.length != 3) {
            throw new IllegalArgumentException("Wrong number of coordinates: "
                                               + coords.length);
        }
        
        coordinates = coords;
    }
    
    /**
     * Returns a copy of the coordinates of this {@code AbstractCoordinates}
     * that can be safely modified
     * 
     * @return a {@code double[]} containing the three Cartesian coordinates of
     *         this {@code AbstractCoordinates}; modifications to the array do
     *         not affect this {@code AbstractCoordinates}
     */
    public double[] getCoordinates() {
        return coordinates.clone();
    }
    
    /**
     * Returns a reference to this {@code AbstractCoordinates}'s internal
     * coordinate array.  This array should not be exposed outside the class and
     * its subclasses, and should never be modified once the containing object
     * has been exposed outside this class and its subclasses.  This method is
     * provided to reduce the need for cloning the coordinate array.
     * 
     * @return a reference to the internal {@code double[]} holding this
     *         {@code AbstractCoordinates}'s coordinates
     */
    final double[] getInternalCoordinates() {
        return coordinates;
    }
}
