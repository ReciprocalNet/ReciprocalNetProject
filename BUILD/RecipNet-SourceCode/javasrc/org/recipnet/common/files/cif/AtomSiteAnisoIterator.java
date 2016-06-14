/*
 * IUMSC Reciprocal Net Project
 *
 * AtomSiteAnisoIterator.java
 *
 * 02-Nov-2006: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.CifFile.DataLoop;

/**
 * An iterator over the atom site anisotropic displacement records represented
 * in a CIF data block; adapts the underlying atom site loop's record structure
 * (or lack thereof) to producing {@code AtomSiteAnisoRecord} items.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomSiteAnisoIterator implements Iterator<AtomSiteAnisoRecord> {

    /**
     * The underlying iterator of the atom site {@code CifFile.DataLoop} that is
     * being iterated
     */
    private final Iterator<List<CifValue>> loopIterator;

    /**
     * The index of the label column in the loop structure
     */
    private final int labelIndex;
    
    private final DisplacementParameterExtractor uExtractor;
    private final DisplacementParameterExtractor bExtractor;

    public AtomSiteAnisoIterator(DataBlock block) {
        DataLoop loop = CifFileUtil.findCifLoop(block, "_atom_site_aniso_label");
        List<String> names;
        
        if (loop == null) {
            throw new IllegalArgumentException(
                    "No looped _atom_site_aniso_label found in data block");
        }
        
        names = loop.getDataNames();
        labelIndex = names.indexOf("_atom_site_aniso_label");
        uExtractor = new DisplacementParameterExtractor('U', loop);
        bExtractor = new DisplacementParameterExtractor('B', loop);
        
        if (uExtractor.isFullyConfigured() || bExtractor.isFullyConfigured()) {
            loopIterator = loop.recordIterator();
        } else {
            loopIterator = Collections.<List<CifValue>>emptyList().iterator();
        }
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
     * Implementation method of the {@code Iterator&lt;AtomSiteAnisoRecord&gt;}
     * interface; returns the next element in this iteration
     * 
     * @return the next {@code AtomSiteAnisoRecord} in this iteration
     * 
     * @throws NoSuchElementException if there are no more elements in the
     *         iteration
     *         
     * @see java.util.Iterator#next()
     */
    public AtomSiteAnisoRecord next() {
        List<CifValue> values = loopIterator.next();
        
        return new AtomSiteAnisoRecord(
                CifFileUtil.getCifString(values.get(labelIndex), null, false),
                uExtractor.getDisplacementParameters(values),
                bExtractor.getDisplacementParameters(values));
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
    
    /**
     * A helper class that extracts the anisotropic displacement parameters of
     * a particular type from a CIF loop record and organizes then into an
     * array of {@code double}s in the order mandated by
     * {@link AtomSiteAnisoRecord} 
     *
     * @author jobollin
     * @version 1.0
     */
    private static class DisplacementParameterExtractor {
        
        /**
         * A cross-reference table between the expected indices of the elements
         * of the arrays returned by this extractor, and the indices of the
         * corresponding elements in the CIF loop records that will be processed
         */
        private final int[] indexTable;
        
        /**
         * Initializes a new {@code DisplacementParameterExtractor} to extract
         * displacement parameters of the specified type from a CIF data loop
         * arranged in the same manner as the one provided
         *
         * @param displacementType the type of displacement parameter to
         *        extract, case-insensitive; the only values likely to be useful
         *        in practice are 'U' and 'B' (and their lower-case
         *        counterparts)
         * @param sourceLoop a CIF {@code DataLoop} defining the data names
         *        and data name order for the loop records to be processed by
         *        this extractor 
         */
        public DisplacementParameterExtractor(char displacementType,
                DataLoop sourceLoop) {
            String baseName
                    = ("_atom_site_aniso_" + displacementType + "_").toLowerCase();
            List<String> names = sourceLoop.getLowerCaseNames(); 
            int[] tempTable = new int[6];
            
            for (int i = 1; i < 4; i++) {
                for (int j = i; j < 4; j++) {
                    String dataName = baseName + String.valueOf(i)
                            + String.valueOf(j);
                    int targetIndex = AtomSiteAnisoRecord.index(i, j);
                    int sourceIndex = names.indexOf(dataName);

                    if (sourceIndex < 0) {
                        indexTable = null;
                        return;
                    } else {
                        tempTable[targetIndex] = sourceIndex;
                    }
                }
            }
            
            indexTable = tempTable;
        }
        
        /**
         * Extracts the anisotropic displacement parameters for which this
         * class is configured from among the specified CIF data values
         *
         * @param values a {@code List} of the CIF data values from which to
         *        extract the parameters; expected to be in the sequence
         *        defined when this object was initialized
         *        
         * @return the displacement parameters in the form of a
         *        {@code double[]}, in the sequence expected by
         *        {@code AtomSiteAnisoRecord}, or {@code null} if none are
         *        available
         */
        public double[] getDisplacementParameters(List<CifValue> values) {
            if (indexTable == null) {
                return null;
            } else {
                double[] rval = new double[indexTable.length];
                
                for (int i = 0; i < indexTable.length; i++) {
                    int index = indexTable[i];
                    
                    rval[i] = CifFileUtil.getCifDouble(
                            values.get(index), Double.NaN);
                }
                
                return rval;
            }
        }
        
        /**
         * Indicates whether this object is 'fully configured', in which case
         * it can be expected to return non-{@code null} values from its
         * {@link #getDisplacementParameters(List)} method.  This object would
         * fail to be fully configured if it was unable to find all the
         * necessary CIF data items in the loop with which it was configured.
         *
         * @return {@code true} if this object is fully configured,
         *         {@code false} if not
         */
        public boolean isFullyConfigured() {
            return (indexTable != null);
        }
    }
}
