/*
 * IUMSC Reciprocal Net Project
 *
 * AnhydrogenousModel.java
 *
 * 30-Aug-2006: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.recipnet.common.Element;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.geometry.CoordinateSystem;

/**
 * A wrapper around a MolecularModel that presents a view of it without hydrogen
 * atoms or bonds to hydrogen atoms.  Modifications of the underlying model are
 * reflected in this one and vise versa, except inasmuch as this model excludes
 * hydrogen atoms. 
 *
 * @author jobollin
 * @version 1.0
 */
public class AnhydrogenousModel<A extends Atom, B extends Bond<A>>
        extends MolecularModel<A, B> {
    
    /**
     * The {@code MolecularModel} on which this one is based
     */
    private final MolecularModel<A, B> baseModel;
    
    /**
     * Initializes a new {@code AnhydrogenousModel} based on the specified
     * {@code MolecularModel}
     *
     * @param baseModel
     */
    public AnhydrogenousModel(MolecularModel<A, B> baseModel) {
        if (baseModel == null) {
            throw new NullPointerException("Null base model");
        } else {
            this.baseModel = baseModel;
        }
    }

    /**
     * Adds an atom to this model, provided that it is not a hydrogen atom
     * 
     * @param atom the {@code A} to add
     * 
     * @throws IllegalArgumentException if the specified atom is a hydrogen atom
     * 
     * @see MolecularModel#addAtom(Atom)
     */
    @Override
    public void addAtom(A atom) {
        if (atom.getElement() == Element.HYDROGEN) {
            throw new IllegalArgumentException(
                    "Cannot add hydrogen atoms to an anhydrogenous model");
        } else {
            baseModel.addAtom(atom);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#addBond(Bond)
     */
    @Override
    public void addBond(B bond) {
        if ((bond.getAtom2().getElement() == Element.HYDROGEN)
                || (bond.getAtom2().getElement() == Element.HYDROGEN)) {
            throw new IllegalArgumentException(
                    "Bond references an unmodeled atom");
        } else {
            baseModel.addBond(bond);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see MolecularModel#addSymmetryOperation(SymmetryMatrix)
     */
    @Override
    public void addSymmetryOperation(SymmetryMatrix operation) {
        baseModel.addSymmetryOperation(operation);
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#getAtoms()
     */
    @Override
    public List<A> getAtoms() {
        List<A> atomList = new ArrayList<A>(baseModel.getAtoms());
        
        for (Iterator<A> atomIt = atomList.iterator(); atomIt.hasNext(); ) {
            if (atomIt.next().getElement() == Element.HYDROGEN) {
                atomIt.remove();
            }
        }
        
        return Collections.unmodifiableList(atomList);
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#getBonds()
     */
    @Override
    public Collection<B> getBonds() {
        List<B> bondList = new ArrayList<B>(baseModel.getBonds());
        
        for (Iterator<B> bondIt = bondList.iterator(); bondIt.hasNext(); ) {
            B bond = bondIt.next();
            
            if ((bond.getAtom1().getElement() == Element.HYDROGEN)
                    || (bond.getAtom2().getElement() == Element.HYDROGEN)) {
                bondIt.remove();
            }
        }
        
        return Collections.unmodifiableList(bondList);
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#getCell()
     */
    @Override
    public CoordinateSystem getCell() {
        return baseModel.getCell();
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#getSymmetryOperations()
     */
    @Override
    public List<SymmetryMatrix> getSymmetryOperations() {
        return baseModel.getSymmetryOperations();
    }

    /**
     * {@inheritDoc}
     * 
     * @see MolecularModel#setCell(CoordinateSystem)
     */
    @Override
    public void setCell(CoordinateSystem cell) {
        baseModel.setCell(cell);
    }

}
