/*
 * Reciprocal Net Project
 *
 * SymmetryContext.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.files.cif;

import java.util.Iterator;
import java.util.List;

import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.CifFile.DataLoop;

/**
 * An iterator over the bond records represented in a CIF data block;
 * adapts the structure of the underlying atom site loop's record structure
 * (or lack thereof) to producing {@code GeomBondRecord} items.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class GeomBondIterator implements Iterator<GeomBondRecord> {
    
    /**
     * The index of the label1 column in the loop structure
     */
    private final int label1Index;
    
    /**
     * The index of the label2 column in the loop structure
     */
    private final int label2Index;
    
    /**
     * The index of the symmetry1 column in the loop structure
     */
    private final int symm1Index;
    
    /**
     * The index of the symmetry2 column in the loop structure
     */
    private final int symm2Index;
    
    /**
     * The underlying iterator over {@code List<CifValue>} records representing
     * the bonds in this iteration 
     */
    private final Iterator<List<CifValue>> loopIterator;
    
    /**
     * Initializes a {@code GeomBondIterator} based on the specified CIF data
     * block
     * 
     * @param  block the data block with which to configure this iterator, and
     *         from which this iterator's elements will be drawn
     * 
     * @throws IllegalArgumentException if the block does not contain a useable
     *         list of items from the CIF {@code _geom_bond_[]} category
     */
    public GeomBondIterator(DataBlock block) {
        List<String> names;
        DataLoop loop = CifFileUtil.findCifLoop(block,
                "_geom_bond_atom_site_label_1");
        
        if (loop == null) {
            throw new IllegalArgumentException(
                    "No looped _geom_bond_atom_site_label_1 found in data block"
                    );
        }
        
        names = loop.getDataNames();
        label1Index = names.indexOf("_geom_bond_atom_site_label_1");
        label2Index = names.indexOf("_geom_bond_atom_site_label_2");
        if (label2Index < 0) {
            throw new IllegalArgumentException(
                    "No _geom_bond_atom_site_label_2 in loop");
        }
        
        // The symmetry codes are optional
        symm1Index = names.indexOf("_geom_bond_site_symmetry_1");
        symm2Index = names.indexOf("_geom_bond_site_symmetry_2");
        
        loopIterator = loop.recordIterator();
    }

    /**
     * Implementation method of the {@code Iterator} interface; determines
     * whether this iteration has any more elements
     * 
     * @return {@code true} if this iteration has more elements, otherwise
     *         {@code false} 
     * 
     * @see Iterator#hasNext()
     */
    public boolean hasNext() {
        return loopIterator.hasNext();
    }

    /**
     * Implementation method of the {@code Iterator<GeomBondRecord>} interface;
     * returns the next element in this iteration
     * 
     * @return the next {@code GeomBondRecord} in this iteration
     * 
     * @see Iterator#next()
     */
    public GeomBondRecord next() {
        List<CifValue> values = loopIterator.next();
        
        return new GeomBondRecord(
                CifFileUtil.getCifString(values.get(label1Index), null, false),
                CifFileUtil.getCifString(values.get(label2Index), null, false),
                ((symm1Index < 0) ? null : CifFileUtil.getCifString(
                        values.get(symm1Index), null, false)),
                ((symm2Index < 0) ? null : CifFileUtil.getCifString(
                        values.get(symm2Index), null, false)));
    }

    /**
     * Implementation record of the {@code Iterator} interface; always throws
     * an exception because this operation is not supported by this iterator
     * 
     * @throws UnsupportedOperationException every time
     * @see Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException(
                "Element removal not supported by this iterator");
    }
}