/*
 * Reciprocal Net Project
 *
 * SymmetryContext.java
 *
 * 29-Nov-2005: jobollin extracted this class from ModelBuilder
 */

package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.recipnet.common.SymmetryMatrix;

/**
 * A helper class that maintains symmetry information, such as known symmetry
 * operations and symmetry code to symmetry operation mappings.  Instances can
 * perform useful symmetry-based computations.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SymmetryContext {
    
    /**
     * A {@code Map} from symmetry matrices to the corresponding CIF-style
     * symmetry codes, used to identify known codes from their matrices, and
     * secondarily to reduce the number of distinct but equivalent symmetry
     * codes created
     */
    private final Map<SymmetryMatrix, SymmetryCode> symmcodeMap;
    
    /**
     * A map from {@code SymmetryCode}s to {@code SymmetryMatrix} objects; the
     * reverse of {@code symmcodeMap} above  
     */
    private final Map<SymmetryCode, SymmetryMatrix> symmMatrixMap;
    
    /**
     * A {@code Map} from <em>normalized</em> symmetry matrices to the index of
     * the corresponding entry in the matrix array 
     */
    private final Map<SymmetryMatrix, Integer> indexMap;
    
    /**
     * An array of the base {@code SymmetryMatrix} objects that define this
     * symmetry context.  For full function of this context, the normalized
     * versions of these matrices should form a mathematical group under
     * normalized symmetry matrix multiplication
     */
    private final SymmetryMatrix[] matrixArray;
    
    /**
     * A {@code SymmetryCode} for the identity operation in this context
     */
    private final SymmetryCode identityCode;
    
    /**
     * Initializes a {@code SymmetryContext} with the specified collection of
     * {@code SymmetryMatrix} instances.  The specified matrices serve represent
     * the "base" symmetry operations to which all symmetry codes used by this
     * instance will refer.  They should be representative of the full symmetry
     * if a common space group, including exactly one pure unit translation
     * (normally the identity operation), though an identity operation will be
     * provided automatically if no suitable operation is included.
     * 
     * @param  matrices a {@code Collection<SymmetryMatrix>} representing the
     *         base symmetry operations for this context, or {@code null} for a
     *         context containing only the identity operation.
     * 
     * @throws IllegalArgumentException if the provided matrices include any
     *         that are related to each other by one or more unit translations
     */
    public SymmetryContext(Collection<SymmetryMatrix> matrices) {
        boolean haveIdentity = false;
        
        indexMap = new HashMap<SymmetryMatrix, Integer>();
        symmcodeMap = new HashMap<SymmetryMatrix, SymmetryCode>();
        symmMatrixMap = new HashMap<SymmetryCode, SymmetryMatrix>();
        
        if (matrices == null) {
            matrices = Collections.singleton(SymmetryMatrix.IDENTITY);
        }
        
        for (SymmetryMatrix matrix : matrices) {
            SymmetryMatrix normalized = matrix.normalize();
            SymmetryCode code = nextBaseSymmcode();
            
            if (indexMap.containsKey(normalized)) {
                throw new IllegalArgumentException(
                        "Redundant symmetry operations");
            } else if (normalized.equals(SymmetryMatrix.IDENTITY)) {
                haveIdentity = true;
            }
            
            indexMap.put(normalized, code.getOperationIndex() - 1);
            symmcodeMap.put(matrix, code);
            symmMatrixMap.put(code, matrix);
        }
        
        if (!haveIdentity) {
            SymmetryCode code = nextBaseSymmcode();
            
            indexMap.put(SymmetryMatrix.IDENTITY, code.getOperationIndex() - 1);
            symmcodeMap.put(SymmetryMatrix.IDENTITY, code);
            symmMatrixMap.put(code, SymmetryMatrix.IDENTITY);
            matrices = new ArrayList<SymmetryMatrix>(matrices);
            matrices.add(SymmetryMatrix.IDENTITY);
        }
        matrixArray = matrices.toArray(new SymmetryMatrix[matrices.size()]);
        
        identityCode = getSymmetryCode(SymmetryMatrix.IDENTITY);
    }
    
    /**
     * A private helper method used during instance initialization that
     * constructs a symmetry code appropriate for representing the next base
     * operation to be added to this context, based on the current size of the
     * base index map
     *  
     * @return a {@code SymmetryCode} corresponding the the specified matrix
     *         as a base symmetry operation
     */
    private SymmetryCode nextBaseSymmcode() {
        return new SymmetryCode(indexMap.size() + 1, new int[] {5, 5, 5});
    }

    /**
     * Returns a list of the base operations of this symmetry context, in the
     * order to which symmetry codes returned by this context are referred
     * 
     * @return an {@code List&lt;SymmetryMatrix&gt;} of the base symmetry
     *         operations defining the symmetry managed by this context 
     */
    public List<SymmetryMatrix> getBaseOperations() {
        return Collections.unmodifiableList(Arrays.asList(matrixArray));
    }

    /**
     * Returns the symmetry code for the identity operation in this context
     * 
     * @return the {@code SymmetryCode} for the identity operation in this
     *         context, or {@code null} if the identity operation is not
     *         represented
     */
    public SymmetryCode getIdentityCode() {
        return identityCode;
    }

    /**
     * Computes the symmetry code corresponding to the specified symmetry matrix
     * in this symmetry context
     *  
     * @param  matrix the {@code SymmetryMatrix} for which a symmetry code is
     *         requested
     *         
     * @return a {@code SymmetryCode} object representing the symmetry code
     *         corresponding to the specified matrix
     *         
     * @throws IllegalArgumentException if the specified matrix has no code in
     *         this context, either because it doesn't correspond to any of the
     *         base symmetry operations or because it involves one or more
     *         translations of more than four unit cells
     */
    public SymmetryCode getSymmetryCode(SymmetryMatrix matrix) {
        SymmetryCode code;
        
        if (symmcodeMap.containsKey(matrix)) {
            code = symmcodeMap.get(matrix);
        } else {
            
            /*
             * Determine the base operation index:
             * Get the zero-based index of the corresponding base matrix from
             * the indexMap, use it to retrieve the base matrix from the base
             * matrix array, then increment it into the one-based operation
             * index for the desired symmetry code 
             */
            SymmetryMatrix normalized = matrix.normalize();
            
            if (!indexMap.containsKey(normalized)) {
                throw new IllegalArgumentException("Unrecognized matrix");
            }
                
            int opIndex = indexMap.get(normalized);
            SymmetryMatrix baseMatrix = matrixArray[opIndex++];

            /*
             * Determine the translations represented by the supplied matrix
             * relative to the base matrix 
             */
            int[] baseVector = baseMatrix.getTranslationVector();
            int[] vector = matrix.getTranslationVector();
            int[] translationCodes = new int[3];
            
            for (int i = 0; i < 3; i++) {
                int shift = vector[i] - baseVector[i];
                
                /*
                 * This is the wrong base symmcode if the relative shifts
                 * are not multiples of twelve
                 */
                assert ((shift % 12) == 0);
                
                translationCodes[i] += (shift / 12) + 5;
            }

            // Construct the code
            code = new SymmetryCode(opIndex, translationCodes);
            symmMatrixMap.put(code, matrix);
            symmcodeMap.put(matrix, code);
        }
        return code;
    }
    
    /**
     * Returns the symmetry matrix corresponding to the specified symmetry
     * code.  A cached matrix will be returned if one is available; otherwise
     * the appropriate matrix is computed (and cached).
     * 
     * @param  code the {@code SymmetryCode} for which a corresponding matrix is
     *         requested
     *         
     * @return the {@code SymmetryMatrix} corresponding to the specified code
     * 
     * @throws IllegalArgumentException if the specified code refers to a
     *         base symmetry operation that is not known to this context
     */
    public SymmetryMatrix getSymmetryMatrix(SymmetryCode code) {
        if (code.getOperationIndex() > matrixArray.length) {
            throw new IllegalArgumentException("Incompatible symmetry code");
        } else if (symmMatrixMap.containsKey(code)) {
            return symmMatrixMap.get(code);
        } else {
            int[] translations = code.getTranslations();
            SymmetryMatrix matrix;
            
            for (int i = 0; i < translations.length; i++) {
                translations[i] = (translations[i] - 5) * 12;
            }
            matrix = matrixArray[code.getOperationIndex() - 1].plus(
                    translations, false);
            
            symmMatrixMap.put(code, matrix);
            symmcodeMap.put(matrix, code);
            
            return matrix;
        }
    }
    
    /**
     * Returns the number of base symmetry operations in this symmetry context
     * 
     * @return the number of base symmetry operations in this symmetry context
     */
    public int getOperationCount() {
        return matrixArray.length;
    }
    
    /**
     * Determines whether this context contains symmetry operations of the type
     * described by the specified matrix, which is the case if its normalized
     * version is the same as the normalized version of one of the base symmetry
     * operations.  Affirmative response from this method does not guarantee
     * that this context can provide a symmetry code for the specified matrix,
     * however: it is possible that too many unit cell translations relative to
     * the corresponding base operation would be required.
     * 
     * @param  matrix a {@code SymmetryMatrix} representing the operation to be
     *         sought 
     *         
     * @return {@code true} if this context contains operations of the specified
     *         type, {@code false} if it does not
     */
    public boolean hasOperation(SymmetryMatrix matrix) {
        return indexMap.containsKey(matrix.normalize());
    }
    
    /**
     * Tests whether this context is "complete", which in this sense means that
     * it represents the full symmetry of some space group, exclusive of pure
     * unit cell translations; this method is comparatively expensive,
     * especially when the context contains a large number of base operations
     *  
     * @return {@code true} if this context is complete, {@code false} if it
     *         isn't
     */
    public boolean isComplete() {
        
        /*
         * At this version, the test simply involves verifying that the
         * normalized product of each pair of base symmetry operations is
         * equal to a normalized base symmetry operation of this context, and
         * that the normalized inverse of each matrix is equal to a normalized
         * base symmetry operation of this context
         */
        
        Set<SymmetryMatrix> matrices = new HashSet<SymmetryMatrix>();
        
        for (SymmetryMatrix matrix : matrixArray) {
            try {
                if (!indexMap.containsKey(matrix.inverse().normalize())) {
                    return false;
                }
            } catch (IllegalStateException ise) {
                return false;
            }
            matrices.add(matrix.normalize());
        }
        for (SymmetryMatrix leftMatrix : matrixArray) {
            for (SymmetryMatrix rightMatrix : matrixArray) {
                if (!matrices.contains(leftMatrix.times(rightMatrix, true))) {
                    return false;
                }
            }
        }
        
        return true;
    }
}