/*
 * Reciprocal Net Project
 *
 * MolecularModel.java
 *
 * 07-Nov-2005: jobollin wrote first draft
 * 30-Aug-2006: jobollin updated computeRadius() and computeCentroid() to access
 *              the atoms via getAtoms() instead of directly
 */
package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Point;

/**
 * <p>
 * A class representing a three-dimensional molecular model, such as is
 * represented by a CRT, SCN, or PDB file, or as is partially represented by a
 * typical small-molecule crystallographic model file such as an SDT or RES.
 * Instances maintain an ordered collection of atoms and a collection of bonds,
 * and additional atoms and bonds may be added to these collections after a
 * model is initialized by use of the {@link #addAtom(Atom) addAtom(A)} and
 * {@link #addBond(Bond) addBond(B)} methods.
 * </p><p>
 * Instances may also have information about a relevant crystallographic
 * coordinate system and / or crystallographic symmtry operations, and they
 * provide convenience methods 
 * </p>
 * 
 * @param <A> the type of {@code Atom} used in this model
 * @param <B> the type of {@code Bond} used in this model
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class MolecularModel<A extends Atom, B extends Bond<A>> {

    /*
     * TODO: in the future it may be useful to be able to remove atoms and/or
     * bonds from a MolecularModel. 
     */
    
    /** a list of the atoms in this model */
    private final List<A> atoms;
    
    /** the bonds in this model */
    private final Collection<B> bonds;
    
    /** the unit cell of this model, if any */
    private CoordinateSystem cell;
    
    /**
     * a {@code List&lt;SymmetryMatrix&gt;} of the symmetry operations
     * applicable to this model
     */
    private final List<SymmetryMatrix> symmetryOperations
            = new ArrayList<SymmetryMatrix>();
    
    /**
     * Initializes a {@code MolecularModel} without any atoms or bonds
     */
    public MolecularModel() {
        atoms = new ArrayList<A>();
        bonds = new ArrayList<B>();
    }

    /**
     * Initializes a {@code MolecularModel} with the specified atoms and bonds
     * 
     * @param atoms a {@code Collection&lt;A&gt;} of the atoms that should
     *        initially be part of this model; they will appear in the atom list
     *        in the iteration order of this collection, before any atoms
     *        subsequently added via {@link #addAtom(Atom) addAtom(A)}; should
     *        not be {@code null}
     * @param bonds a {@code Collection&lt;B&gt;} of the bonds that should
     *        initially be part of this model; should not be {@code null}
     */
    public MolecularModel(
            Collection<? extends A> atoms, Collection<? extends B> bonds) {
        this.atoms = new ArrayList<A>(atoms);
        this.bonds = new ArrayList<B>(bonds);
    }
    
    /**
     * Adds an atom to this model.  Atoms are internally maintained in the
     * sequence in which they were added.
     * 
     * @param  atom the {@code A} to add to this model
     */
    public void addAtom(A atom) {
        if (atom == null) {
            throw new NullPointerException("Cannot add a null atom");
        } else {
            atoms.add(atom);
        }
    }
    
    /**
     * Adds a bond to this model.  It is expected (but not required) that the
     * user will avoid adding duplicate bonds to the model, where one bond is
     * considered a duplicate of another if they involve the same two atoms,
     * regardless of order. 
     * 
     * @param  bond the {@code B} to add to this model
     * 
     * @throws IllegalArgumentException if either of the specified bond's atoms
     *         is not in this {@code MolecularModel}'s atom list
     */
    public void addBond(B bond) {
        if (atoms.contains(bond.getAtom1())
                && atoms.contains(bond.getAtom2())) {
            bonds.add(bond);
        } else {
            throw new IllegalArgumentException(
                    "Bond references an unmodeled atom");
        }
    }

    /**
     * Returns an unmodifiable view of the list of atoms in this model
     * 
     * @return an unmodifiable {@code List&lt;A&gt;} of the atoms in this model
     */
    public List<A> getAtoms() {
        return Collections.unmodifiableList(atoms);
    }

    /**
     * Returns an unmodifiable view of the list of bonds in this model
     * 
     * @return an unmodifiable {@code Collection&lt;B&gt;} of the bonds in this
     *         model
     */
    public Collection<B> getBonds() {
        return Collections.unmodifiableCollection(bonds);
    }
    
    /**
     * Returns the unit cell, if any, assigned to this model
     * 
     * @return the {@code CoordinateSystem} of this model, or {@code null} if
     *         none has been assigned
     */
    public CoordinateSystem getCell() {
        return cell;
    }

    /**
     * Sets the unit cell for this model;
     * 
     * @param  cell the {@code CoordinateSystem} to set for this model
     */
    public void setCell(CoordinateSystem cell) {
        this.cell = cell;
    }

    /**
     * Adds the specified symmetry operation to this model.  This is a
     * bookkeeping function: only the symmetry operation records are affected;
     * the atom and bond lists are not changed.
     * 
     * @param  operation a {@code SymmetryMatrix} representing a symmetry
     *         operation of this model; should not be {@code null}
     */
    public void addSymmetryOperation(SymmetryMatrix operation) {
        if (operation == null) {
            throw new NullPointerException("Null symmetry operation");
        } else {
            symmetryOperations.add(operation);
        }
    }
    
    /**
     * Returns an iterable object whose elements are the symmetry matrices
     * defined for this model
     * 
     * @return an {@code Iterable<SymmetryMatrix>} whose elements are this
     *         model's symmetry matrices
     */
    public List<SymmetryMatrix> getSymmetryOperations() {
        return Collections.unmodifiableList(symmetryOperations);
    }

    /**
     * Computes the radius of the smallest sphere centered at the specified
     * point that contains all the atoms of this model either in its interior or
     * on its surface; equivalently, computes the greatest distance from the
     * specified point to any atomic position in this model
     * 
     * @param  center the center of the sphere of enclosure, as a {@code Point}
     * 
     * @return the radius of the smallest sphere of enclosure centered at the
     *         specified point; may be zero
     */
    public double computeRadius(Point center) {
        double maxDistance = 0.0;
        
        for (A atom : getAtoms()) {
            maxDistance = Math.max(maxDistance,
                    center.distanceTo(atom.getPosition()));
        }
        
        return maxDistance;
    }
    
    /**
     * Computes and returns the unweighted centroid of the atoms in this model
     * 
     * @return the model centroid as a {@code Point}
     */
    public Point computeCentroid() {
        Collection<A> atoms = getAtoms();
        
        if (atoms.size() == 0) {
            throw new IllegalStateException("The model contains no atoms");
        } else {
            List<Point> positions = new ArrayList<Point>(atoms.size());
            
            for (A atom : atoms) {
                positions.add(atom.getPosition());
            }
            
            return Point.midPoint(
                    positions.toArray(new Point[positions.size()]));
        }
    }
}
