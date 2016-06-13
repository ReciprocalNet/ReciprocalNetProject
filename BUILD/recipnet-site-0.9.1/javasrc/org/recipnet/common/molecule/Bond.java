/*
 * Reciprocal Net Project
 *
 * Bond.java
 *
 * 07-Nov-2005: jobollin extracted this class from CrtFile; formatted the source
 *              according to typical Reciprocal Net conventions; added comments;
 *              converted to extending AbstractCoordinates instead of
 *              Point; made members private with accessors
 */

package org.recipnet.common.molecule;

/**
 * A class representing a (two-center) chemical bond in a molecular model
 * 
 * @param  <A> the type of atoms connected by a {@code Bond} instance
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class Bond<A extends Atom> {
    
    /**
     * One atom participating in the bond
     */
    private final A atom1;

    /**
     * One atom participating in the bond
     */
    private final A atom2;

    /**
     * Initializes a {@code Bond} between the specified two atoms, which must be
     * distinct
     * 
     * @param  atom1 the first atom in the bond; must not be {@code null}
     * @param  atom2 the second atom in the bond; must not be {@code null}
     * 
     * @throws IllegalArgumentException if atom1 and atom2 are the same object
     */
    public Bond(A atom1, A atom2) {
        if (atom1 == atom2) {
            throw new IllegalArgumentException(
                    "Bonds must link distinct atoms");
        } else if (atom1 == null || atom2 == null) {
            throw new NullPointerException("A bond's atoms must not be null");
        }
        this.atom1 = atom1;
        this.atom2 = atom2;
    }

    /**
     * Returns the "first" atom in this bond
     * 
     * @return the first {@code Atom}
     */
    public A getAtom1() {
        return atom1;
    }

    /**
     * Returns the "second" atom in this bond
     * 
     * @return the second {@code Atom}
     */
    public A getAtom2() {
        return atom2;
    }

    /**
     * Determines whether either atom connected by this bond is a hydrogen atom
     * 
     * @return {@code true} if either atom is a hydrogen atom, {@code false} if
     *         not
     */
    public boolean includesHydrogen() {
        return (atom1.isHydrogen() || atom2.isHydrogen());
    }
}
