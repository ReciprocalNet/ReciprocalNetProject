/*
 * Reciprocal Net Project
 *
 * SequenceBasedADCGenerator.java
 *
 * 06-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.MolecularModel;
import org.recipnet.common.molecule.SymmetryCode;

/**
 * An ADCGenerator implementation that matches input atoms to model atoms
 * strictly by means of corresponding indices.  This mode supports the
 * original ORTEP-generation behavior of the Site Software.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class SequenceBasedADCGenerator implements ADCGenerator {

    /**
     * The base model for this ADC generator
     */
    private final MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            baseModel;
    
    /**
     * Initializes a {@code SequenceBasedADCGenerator} with the specified
     * base model
     * 
     * @param  model a {@code MolecularModel&lt;FractionalAtom,
     *         Bond&lt;FractionalAtom&gt;&gt;} that will be used to create
     *         this generator's base model.  This model itself will not be
     *         modified or retained.
     */
    public SequenceBasedADCGenerator(
            MolecularModel<FractionalAtom, Bond<FractionalAtom>> model) {
        baseModel = new MolecularModel<FractionalAtom, Bond<FractionalAtom>>(
                model.getAtoms(), model.getBonds());
        baseModel.setCell(model.getCell());
        baseModel.addSymmetryOperation(SymmetryMatrix.IDENTITY);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see ADCGenerator#getBaseModel()
     */
    public MolecularModel<FractionalAtom, Bond<FractionalAtom>> getBaseModel() {
        return baseModel;
    }
    
    /**
     * {@inheritDoc}.  This version always returns {@code false}.
     * 
     * @see ADCGenerator#usesModelSymmetry()
     */
    public boolean usesModelSymmetry() {
        return false;
    }
    
    /**
     * {@inheritDoc}.  This version maps the input atoms to a sequential
     * list of atom designator codes, all based on the <i>default</i>
     * identity operation (operation #1, translations 5, 5, 5).
     * 
     * @see ADCGenerator#createADCMap(List)
     */
    public Map<Atom, AtomDesignatorCode> createADCMap(
            List<? extends Atom> atoms) {
        Map<Atom, AtomDesignatorCode> adcMap
                = new HashMap<Atom, AtomDesignatorCode>();
        SymmetryCode identityCode
                = new SymmetryCode(1, new int[] { 5, 5, 5 });
        int index = 1;
        
        for (Atom atom : atoms) {
            adcMap.put(atom, new AtomDesignatorCode(index++, identityCode));
        }

        return adcMap;
    }
}