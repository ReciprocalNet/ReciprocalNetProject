/*
 * Reciprocal Net Project
 *
 * AtomDesignatorCode.java
 *
 * 10-Dec-2002: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

import java.util.Map;

/**
 * A helper class for {@link SiteCodeBasedADCGenerator} that records information
 * needed to translate between different symmetry representations on the same
 * model.  Such translations are sometimes necessary because ORTEP does not
 * directly support space groups having more than 96 symmetry operations (the
 * only ones affected being face-centered, high-symmetry, centrosymmetric groups
 * in the cubic system).  Instances of this class are immutable.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class SymopContext {

    /**
     * A map from atom site labels to atom indices; this mapping may depend on
     * the symmetry operation because groups with too many symmetry operations
     * are handled by adding atoms to the model while removing symmetry
     * operations.
     */
    private final Map<String, Integer> labelIndices;

    /**
     * The symmetry operation index used by this symmetry operation context;
     * this index may not be unique to this context: if symmetry reduction has
     * been applied then multiple contexts will map to the same symmetry
     * operation index
     */
    private final int operationIndex;

    /**
     * Translation offsets that apply to this symmetry operation context; these
     * come into play when symmetry reduction has been performed, but otherwise
     * are all zero
     */
    private final int[] translations;

    /**
     * Initializes a {@code SymopContext} with the specified symmetry operation
     * index and label-to-index map, assuming zero translations
     * 
     * @param  operationIndex the symmetry operation index relevant to this
     *         context
     * @param  labelIndices the {@code Map&lt;String, Integer&gt;} from atom
     *         site labels to atom indices that this context should use; this
     *         context keeps a reference to this map (does not copy it), but
     *         never modifies it
     */
    public SymopContext(int operationIndex, Map<String, Integer> labelIndices) {
        this.operationIndex = operationIndex;
        this.labelIndices = labelIndices;
        translations = new int[] { 0, 0, 0 };
    }

    /**
     * Initializes a {@code SymopContext} with the specified symmetry operation
     * index, translation offsets, and label-to-index map
     * 
     * @param  operationIndex the symmetry operation index relevant to this
     *         context
     * @param  translations an {@code int[]} of length 3 containing the unit
     *         cell translation offsets that this context should apply
     * @param  labelIndices the {@code Map&lt;String, Integer&gt;} from atom
     *         site labels to atom indices that this context should use; this
     *         context keeps a reference to this map (does not copy it), but
     *         never modifies it
     */
    public SymopContext(int operationIndex, int[] translations,
            Map<String, Integer> labelIndices) {
        this.operationIndex = operationIndex;
        this.labelIndices = labelIndices;
        this.translations = translations.clone();
    }

    /**
     * Obtains the symmetry operation index used by this context
     * 
     * @return the symmetry operation index with which this context is
     *         configured
     */
    public int getOperationIndex() {
        return operationIndex;
    }

    /**
     * Modifies the specified array of translation codes by adding this
     * context's configured corresponding translation offset to each element
     * 
     * @param  baseTranslations an {@code int[]} of length 3 of translation
     *         codes to adjust
     *         
     * @throws IndexOutOfBoundsException if {@code baseTranslations} has length
     *         less than 3
     */
    public void adjustTranslations(int[] baseTranslations) {
        for (int i = 0; i < translations.length; i++) {
            baseTranslations[i] += translations[i];
        }
    }

    /**
     * Determines the atom index appropriate for an atom having the specified
     * label in this symmetry operation context
     * 
     * @param  atomLabel the atom label for which an atom index is requested
     * 
     * @return the appropriate atom index
     * 
     * @throws IllegalArgumentException if no index corresponds to the specified
     *         atom label in this context
     */
    public int getAtomIndex(String atomLabel) {
        Integer index = labelIndices.get(atomLabel);

        if (index == null) {
            throw new IllegalArgumentException(
                    "Unknown atom label: " + atomLabel);
        } else {
            return index.intValue();
        }
    }
}