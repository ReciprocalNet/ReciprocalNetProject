/*
 * Reciprocal Net Project
 *
 * Atom.java
 *
 * 07-Nov-2005: jobollin extracted this class from CrtFile; formatted the
 *              source according to Reciprocal Net conventions; added comments;
 *              converted to having a Point rather than being one; made
 *              members private with accessors
 */

package org.recipnet.common.molecule;

import org.recipnet.common.Element;
import org.recipnet.common.geometry.Point;

/**
 * A class representing an atom in a molecular model
 */
public class Atom {
    
    /**
     * The label of this atom
     */
    private final String label;
    
    /**
     * The location of this atom in a three-dimensional Cartesian space
     */
    private Point position;

    /**
     * The {@code Element} represented by this atom
     */
    private final Element element;
    
    /**
     * The site tag of this atom
     */
    private String siteTag;

    /**
     * Initializes an {@code Atom} with the specified parameters
     * 
     * @param  label the label {@code String} for this atom; should not be
     *         {@code null}
     * @param  element the {@code Element} represented by this atom; may be
     *         {@code null}
     * @param  x the Cartesian x coordinate of this atom as a {@code double}
     * @param  y the Cartesian y coordinate of this atom as a {@code double}
     * @param  z the Cartesian z coordinate of this atom as a {@code double}
     * @param  tag the site tag {@code String} for this atom; may be
     *         {@code null} 
     */
    public Atom(String label, Element element, double x,
            double y, double z, String tag) {
        this(label, element, new Point(x, y, z), tag);
    }
    
    /**
     * Initializes a {@code Atom} with the specified parameters
     * 
     * @param  label the label {@code String} for this atom; should not be
     *         {@code null}
     * @param  element the {@code Element} represented by this atom; may be
     *         {@code null}
     * @param  position the position of this atom as a {@code Point}; should not
     *         be {@code null}
     * @param  tag the site tag {@code String} for this atom; may be
     *         {@code null} 
     */
    public Atom(String label, Element element, Point position, String tag) {
        if ((label == null) || (position == null)) {
            throw new NullPointerException(
                    "Neither the label nor the position may be null");
        }
        this.label = label;
        this.position = position;
        this.element = element;
        this.siteTag = tag;
    }

    /**
     * Returns the {@code Element} represented by this atom
     * 
     * @return the {@code Element}
     */
    public Element getElement() {
        return element;
    }

    /**
     * Returns this atom's label
     * 
     * @return the label {@code String}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the position of this atom
     * 
     * @return the position of this atom as a {@code Point}
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Returns this atom's site tag
     * 
     * @return the site tag {@code String}
     */
    public String getSiteTag() {
        return siteTag;
    }

    /**
     * Sets the site tag for this atom
     * 
     * @param tag the site tag {@code String} for this atom; may be {@code null}
     */
    public void setSiteTag(String tag) {
        siteTag = tag;
    }
    
    /**
     * Determines whether this atom is a hydrogen atom
     * 
     * @return {@code true} if this atom's element is {@code Element.HYDROGEN},
     *         {@code false} if not
     */
    public boolean isHydrogen() {
        return (element == Element.HYDROGEN);
    }
 
    /**
     * Changes this atom's position to the specified one
     * 
     * @param  newPosition a {@code Point} representing the new position for
     *         this atom; should not be {@code null}
     */
    public void moveTo(Point newPosition) {
        if (newPosition == null) {
            throw new NullPointerException("The new position must not be null");
        } else {
            position = newPosition;
        }
    }
}
