/*
 * Reciprocal Net Project
 *
 * FractionalAtom.java
 *
 * 23-Nov-2005: jobollin extracted this class from ModelBuilder.java
 */

package org.recipnet.common.molecule;

import org.recipnet.common.Element;
import org.recipnet.common.geometry.Point;
import org.recipnet.common.geometry.CoordinateSystem;

/**
 * An {@code Atom} that can be expressed and manipulated in terms of
 * fractional coordinates relative to a configured (fixed) coordinate system
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class FractionalAtom extends Atom {
    
    /**
     * The {@code CoordinateSystem} to which the fractional coordinates of this atom are
     * referred
     */
    private final CoordinateSystem referenceCell;
    
    /**
     * The fractional coordinates of this atom, cached here to avoid
     * recomputation.  The superclass' position {@code Point} is authoritative
     * in this regard, and can be used together with this atom's reference cell
     * to recompute these coordinates at need.
     */
    private transient double[] fractionalCoords;
    
    /**
     * An {@code AtomicMotion} object representing the static and / or dynamic
     * motion of the atom in its medium
     */
    private AtomicMotion atomicMotion;
    
    /**
     * Initializes a {@code FractionalAtom} with the specified parameters
     * 
     * @param  label the atom label; should not be {@code null}
     * @param  el the chemical {@code Element} of this atom ; may be
     *         {@code null}
     * @param  fractionalX the fractional x coordinate of this atom, as a
     *         {@code double}
     * @param  fractionalY the fractional y coordinate of this atom, as a
     *         {@code double}
     * @param  fractionalZ the fractional z coordinate of this atom, as a
     *         {@code double}
     * @param  unitCell the {@code CoordinateSystem} to which the fractional
     *         coordiates are referred; should not be {@code null}
     * @param  tag the site tag {@code String} for this atom; may be
     *         {@code null} 
     */
    public FractionalAtom(String label, Element el, double fractionalX,
            double fractionalY, double fractionalZ, CoordinateSystem unitCell,
            String tag) {
        super(label, el, getCartesianPosition(unitCell, fractionalX,
                fractionalY, fractionalZ), tag);
        
        referenceCell = unitCell;
        fractionalCoords = new double[] {fractionalX, fractionalY, fractionalZ};
    }

    /**
     * Initializes a {@code FractionalAtom} with the specified parameters
     * 
     * @param  label the atom label; should not be {@code null}
     * @param  el the chemical {@code Element} of this atom ; may be
     *         {@code null}
     * @param  fractionalCoords the fractional coordinates of this atom, as a
     *         {@code double[]} of length 3; should not be {@code null}
     * @param  unitCell the {@code CoordinateSystem} to which the fractional
     *         coordiates are referred; should not be {@code null}
     * @param  tag the site tag {@code String} for this atom; may be
     *         {@code null} 
     * 
     * @throws IndexOutOfBoundsException if the length of
     *         {@code fractionalCoords} is less than 3
     */
    public FractionalAtom(String label, Element el, double[] fractionalCoords,
            CoordinateSystem unitCell, String tag) {
        this(label, el, fractionalCoords[0], fractionalCoords[1],
                fractionalCoords[2], unitCell, tag);
    }
    
    /**
     * Returns the fractional coordinates of this atom
     * 
     * @return the fractional coordinates as a {@code double[]} of length 3
     */
    public double[] getFractionalCoordinates() {
        if (fractionalCoords == null) {
            fractionalCoords = referenceCell.coordinatesOf(getPosition());
        }
        
        return fractionalCoords;
    }
    
    /**
     * Gets the unit cell to which this atom's fractional coordinates are
     * referred
     * 
     * @return the reference {@code CoordinateSystem}
     */
    public CoordinateSystem getReferenceCell() {
        return referenceCell;
    }

    /**
     * Gets the atomic motion object assigned to this atom
     * 
     * @return the atomic motion as {@code AtomicMotion}
     */
    public AtomicMotion getAtomicMotion() {
        return atomicMotion;
    }

    /**
     * Assigns an atomic motion object to this atom
     * 
     * @param  atomicMotion the {@code AtomicMotion} to set as the atomic
     *         motion; may be {@code null} to clear any previously assigned
     *         motion
     */
    public void setAtomicMotion(AtomicMotion atomicMotion) {
        this.atomicMotion = atomicMotion;
    }

    /**
     * Changes the coordinates of this atom, similar to
     * {@link Atom#moveTo(Point)}.  This method, however, accepts the target
     * location in the form of fractional coordinates
     *  
     * @param  coords the target coordinates as a {@code double[]} of length 3
     */
    public void moveToFractionalCoords(double[] coords) {
        if (coords.length != 3) {
            throw new IllegalArgumentException(
                    "3D fractional coordinates are required");
        } else {
            moveToFractionalCoords(coords[0], coords[1], coords[2]);
        }
    }

    /**
     * Changes the coordinates of this atom, similar to
     * {@link Atom#moveTo(Point)}.  This method, however, accepts the target
     * location in the form of fractional coordinates
     * 
     * @param  fractionalX the fractional X coordinate of the target position
     * @param  fractionalY the fractional Y coordinate of the target position
     * @param  fractionalZ the fractional Z coordinate of the target position
     */
    public void moveToFractionalCoords(double fractionalX, double fractionalY,
            double fractionalZ) {
        moveTo(getCartesianPosition(referenceCell, fractionalX, fractionalY,
                fractionalZ));
    }
    
    /**
     * {@inheritDoc}.  This version delegates to the superclass for the movement
     * itself, but ensures that fractional coordinate record keeping is
     * performed so that the fractional coordinates are updated
     * 
     * @see Atom#moveTo(Point)
     */
    @Override
    public void moveTo(Point newPosition) {
        super.moveTo(newPosition);
        fractionalCoords = null;
    }

    /**
     * A utility method that computes the Cartesian coordinates of a point
     * specified in terms of fractional coordinates referred to a given unit
     * cell
     * 
     * @param  cell the {@code CoordinateSystem} to which the specified fractional
     *         coordinates are referred
     * @param  fractionalX the fractional X coordinate of the desired point
     * @param  fractionalY the fractional Y coordinate of the desired point
     * @param  fractionalZ the fractional Z coordinate of the desired point
     * 
     * @return the Cartesian coordinates of the specified point, in the form of
     *         a {@code Point}
     */
    private static Point getCartesianPosition(CoordinateSystem cell, double fractionalX,
            double fractionalY, double fractionalZ) {
        return cell.pointFor(fractionalX, fractionalY, fractionalZ);
    }
}