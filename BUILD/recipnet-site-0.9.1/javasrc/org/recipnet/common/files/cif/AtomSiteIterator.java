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
import java.util.NoSuchElementException;

import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.CifFile.DataLoop;

/**
 * An iterator over the atom site records represented in a CIF data block;
 * adapts the structure of the underlying atom site loop's record structure
 * (or lack thereof) to producing {@code AtomSiteRecord} items.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomSiteIterator implements Iterator<AtomSiteRecord> {

    /**
     * The underlying iterator of the atom site {@code CifFile.DataLoop} that is
     * being iterated
     */
    private final Iterator<List<CifValue>> loopIterator;
    
    /**
     * The index of the label column in the loop structure
     */
    private final int labelIndex;
    
    /**
     * The index of the type column in the loop structure
     */
    private final int typeIndex;
    
    /**
     * The index of the fractional x coordinate column in the loop structure
     */
    private final int xIndex;
    
    /**
     * The index of the fractional y coordinate column in the loop structure
     */
    private final int yIndex;
    
    /**
     * The index of the fractional z coordinate column in the loop structure
     */
    private final int zIndex;
    
    /**
     * The index of the isotropic U column in the loop structure
     */
    private final int uIndex;
    
    /**
     * The index of the isotropic B column in the loop structure
     */
    private final int bIndex;
    
    /**
     * Initializes an {@code AtomSiteIterator} based on the specified CIF data
     * block
     * 
     * @param  block the data block with which to configure this iterator, and
     *         from which this iterator's elements will be drawn
     * 
     * @throws IllegalArgumentException if the block does not contain a useable
     *         list of items from the CIF {@code _atom_site_[]} category,
     *         including if it contains a list for that category that does not
     *         contain fractional coordinates 
     */
    public AtomSiteIterator(DataBlock block) {
        DataLoop loop = CifFileUtil.findCifLoop(block, "_atom_site_label");
        List<String> names;
        int temp;
        
        if (loop == null) {
            throw new IllegalArgumentException(
                    "No looped _atom_site_label found in data block");
        }
        
        names = loop.getDataNames();
        labelIndex = names.indexOf("_atom_site_label");
        temp = names.indexOf("_atom_site_type_symbol");
        typeIndex = (temp >= 0) ? temp
                : names.indexOf("_atom_site_label_component_0");
        
        xIndex = names.indexOf("_atom_site_fract_x");
        if (xIndex < 0) {
            throw new IllegalArgumentException("No _atom_site_fract_x in loop");
        }
        yIndex = names.indexOf("_atom_site_fract_y");
        if (yIndex < 0) {
            throw new IllegalArgumentException("No _atom_site_fract_y in loop");
        }
        zIndex = names.indexOf("_atom_site_fract_z");
        if (zIndex < 0) {
            throw new IllegalArgumentException("No _atom_site_fract_z in loop");
        }
        uIndex = names.indexOf("_atom_site_U_iso_or_equiv");
        bIndex = names.indexOf("_atom_site_B_iso_or_equiv");
        
        loopIterator = loop.recordIterator();
    }
    
    /**
     * Implementation method of the {@code Iterator&lt;E&gt;} interface;
     * determines whether there are any more elements in this iteration
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return loopIterator.hasNext();
    }

    /**
     * Implementation method of the {@code Iterator&lt;AtomSiteRecord&gt;}
     * interface; returns the next element in this iteration
     * 
     * @return the next {@code AtomSiteRecord} in this iteration
     * 
     * @throws NoSuchElementException if there are no more elements in the
     *         iteration
     *         
     * @see java.util.Iterator#next()
     */
    public AtomSiteRecord next() {
        List<CifValue> values = loopIterator.next();
        CifValue uValue
                = ((uIndex < 0) ? UnknownValue.instance : values.get(uIndex));
        CifValue bValue
                = ((bIndex < 0) ? UnknownValue.instance : values.get(bIndex));
        
        return new AtomSiteRecord(
                CifFileUtil.getCifString(values.get(labelIndex), null, false),
                ((typeIndex < 0) ? null
                : CifFileUtil.getCifString(values.get(typeIndex), null, false)),
                CifFileUtil.getCifDouble(values.get(xIndex), Double.NaN),
                CifFileUtil.getCifDouble(values.get(yIndex), Double.NaN),
                CifFileUtil.getCifDouble(values.get(zIndex), Double.NaN),
                CifFileUtil.getCifDouble(uValue, Double.NaN),
                CifFileUtil.getCifDouble(bValue, Double.NaN));
    }

    /**
     * Implementation method of the {@code Iterator&lt;E&gt;} interface; this
     * version always throws {@code UnsupportedOperationException} because this
     * iterator does not support element removal
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException(
                "Element removal not supported by this iterator");
    }
}