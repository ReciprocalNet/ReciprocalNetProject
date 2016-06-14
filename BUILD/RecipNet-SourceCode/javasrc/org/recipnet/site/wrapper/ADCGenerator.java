/*
 * Reciprocal Net Project
 *
 * ADCGenerator.java
 *
 * 06-Jan-2006: jobollin wrote first draft
 * 30-Aug-2006: jobollin updated docs
 */

package org.recipnet.site.wrapper;

import java.util.List;
import java.util.Map;

import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.MolecularModel;

/**
 * An interface defining objects that produce atom designator codes
 * corresponding to atoms.  
 * 
 * @author jobollin
 * @version 0.9.0
 */
interface ADCGenerator {
    
    /**
     * Returns the molecular model to which the ADCs provided by this
     * generator refer.  In particular, the atom numbers in the ADCs provided
     * by this generator correspond to the sequence of atoms in the model's
     * atom list (though ADC atom numbers are one-based)
     * 
     * @return the {@code MolecularModel&lt;FractionalAtom,
     *         Bond&lt;FractionalAtom&gt;&gt;} on which this generator's
     *         ADCs are based 
     */
    MolecularModel<FractionalAtom, Bond<FractionalAtom>> getBaseModel();
    
    /**
     * Determines whether the ADCs provided by this generator make use of
     * the model's symmetry operations
     * 
     * @return {@code true} if this generator's ADCs may refer to model
     *         symmetry operations, {@code false} if they will not
     */
    boolean usesModelSymmetry();
    
    /**
     * Creates and returns a map associating the atoms of the input list
     * with unique atom designator codes
     * 
     * @param  atoms a {@code List&lt;Atom&gt;} of atoms for which ADCs are
     *         requested; the returned mappings may (or may not) be
     *         sensitive to the order of the atoms on this list
     *         
     * @return a {@code Map&lt;Atom, AtomDesignatorCode&gt;} of the ADC for
     *         each input atom
     */
    Map<Atom, AtomDesignatorCode> createADCMap(List<? extends Atom> atoms);
}