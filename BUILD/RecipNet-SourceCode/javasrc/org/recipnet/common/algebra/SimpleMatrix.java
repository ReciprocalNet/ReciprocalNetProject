/*
 * Reciprocal Net Project
 *
 * SimpleMatrix.java
 *
 * 22-Nov-2005: jobollin wrote first draft
 * 24-May-2006: jobollin made a minor modification to times(double)
 */

package org.recipnet.common.algebra;

/**
 * A class representing a general mathematical matrix and supporting a variety
 * of standard matrix operations: scalar product, matrix-vector product, matrix
 * product, matrix sum, transpose, and inverse.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SimpleMatrix {

    /**
     * A constant used to judge whether a double value is significantly
     * different from zero.  This is important when attempting to compute an
     * inverse matrix in particular.
     */
    private static final double SINGULARITY_LIMIT = 1e-100;
    
    /**
     * A 2-D array containing the elements of this matrix
     */
    private double[][] elements;
    
    /**
     * A {@code SimpleMatrix} representing the inverse of this matrix, if it
     * has previously been computed
     */
    private transient SimpleMatrix cachedInverse;
    
    /**
     * A {@code SimpleMatrix} representing the transpose of this matrix, if it
     * has previously been computed
     */
    private transient SimpleMatrix cachedTranspose;
    
    /**
     * Initializes a {@code SimpleMatrix} with the specified elements
     * 
     * @param  elements a {@code double[][]} containing the elements for this
     *         matrix; should be rectangular (i.e. not ragged-edged).  The
     *         elements are copied into internal storage; no reference is
     *         retained to this array or any of its element arrays
     *
     * @throws IllegalArgumentException if the {@code elements} array is ragged,
     *         or if it has no rows or no columns
     */
    public SimpleMatrix(double[][] elements) {
        
        // Check the element array
        if (elements.length < 1) {
            throw new IllegalArgumentException("No rows");
        } else {
            int rowLength = elements[0].length;
            
            if (rowLength < 1) {
                throw new IllegalArgumentException("No columns");
            } else {
                for (int i = 1; i < elements.length; i++) {
                    if (elements[i].length != rowLength) {
                        throw new IllegalArgumentException(
                                "Unequal row lengths");
                    }
                }
            }
        }
        
        // Copy the element array to internal storage
        this.elements = new double[elements.length][];
        for (int i = 0; i < elements.length; i++) {
            this.elements[i] = elements[i].clone();
        }
    }
    
    /**
     * Initializes a {@code SimpleMatrix} with the specified elements 
     * 
     * @param  elements an {@code int[][]} containing the elements for this
     *         matrix; should be rectangular (i.e. not ragged-edged).  The
     *         elements are copied into internal storage; no reference is
     *         retained to this array or any of its element arrays
     *
     * @throws IllegalArgumentException if the {@code elements} array is ragged,
     *         or if it has no rows or no columns
     */
    public SimpleMatrix(int[][] elements) {
        
        // Check the element array
        if (elements.length < 1) {
            throw new IllegalArgumentException("No rows");
        } else {
            int rowLength = elements[0].length;
            
            if (rowLength < 1) {
                throw new IllegalArgumentException("No columns");
            } else {
                for (int i = 1; i < elements.length; i++) {
                    if (elements[i].length != rowLength) {
                        throw new IllegalArgumentException(
                                "Unequal row lengths");
                    }
                }
            }
        }
        
        // Copy the element array to internal storage
        this.elements = new double[elements.length][elements[0].length];
        for (int row = 0; row < elements.length; row++) {
            for (int col = 0; col < elements[0].length; col++) {
                this.elements[row][col] = elements[row][col];
            }
        }
    }
    
    /**
     * Initializes a {@code SimpleMatrix} without initalizing the internal
     * element array; used internally by methods that will manually set the
     * element array after construction (and thus avoid an unnecessary array
     * copy)
     */
    private SimpleMatrix() {
        // For internal use only, by methods that will set the elements matrix
        // after construction.  This constructor does nothing.
    }
    
    /**
     * Returns the number of rows in this {@code SimpleMatrix}
     * 
     * @return the number of rows in this matrix
     */
    public int getRowCount() {
        return elements.length;
    }
    
    /**
     * Returns the number of columns in this {@code SimpleMatrix}
     * 
     * @return the number of columns in this matrix
     */
    public int getColumnCount() {
        return elements[0].length;
    }
    
    /**
     * Returns a copy of this matrix's underlying 2-D element array
     * 
     * @return a copy of the {@code double[][]} containing this matrix's
     *         elements
     */
    public double[][] getElements() {
        double[][] result = new double[getRowCount()][];
        
        for (int i = 0; i < result.length; i++) {
            result[i] = elements[i].clone();
        }
        
        return result;
    }

    /**
     * Returns the scalar product of this matrix and the specified scalar
     * factor; this matrix is not modified
     * 
     * @param  factor the {@code double} factor
     * 
     * @return a {@code SimpleMatrix} representing the result of the scalar
     *         product of this matrix with the specified factor
     */
    public SimpleMatrix times(double factor) {
        SimpleMatrix scaledMatrix = new SimpleMatrix();
        double[][] scaledElements = getElements();  // a copy of the elements
        
        for (double[] row : scaledElements) {
            for (int col = 0; col < row.length; col++) {
                row[col] *= factor;
            }
        }
        scaledMatrix.elements = scaledElements;
        
        return scaledMatrix;
    }

    /**
     * A convenience method for computing a matrix * vector product without
     * explicitly representing the vector as a column matrix.  Neither factor is
     * modified.
     *  
     * @param  rightFactor a {@code double[]} containing the coordinates of the
     *         vector factor; must have length equal to the number of columns in
     *         this matrix
     * 
     * @return a new {@code double[]} containing the coordinates of the vector
     *         result; will have the same number of elements as this matrix has
     *         rows
     *         
     * @throws IllegalArgumentException if the factor array does not have
     *         exactly the same number of elements as this matrix has columns
     */
    public double[] times(double[] rightFactor) {
        
        /*
         * This implementation computes the product as the transpose of the
         * vector times the transpose of the matrix, transposed.  This makes
         * use of the lack of column vs. row character of raw double[]
         * instances (so that two of the three transposes are only notional),
         * and it leverages this matrix's transpose-caching behavior to make
         * the remaining transpose a one-time cost.  The resulting approach
         * has simpler code, and requires fewer operations per invocation (after
         * the first invocation) than a more straightforward approach would
         * require.  
         */
        
        SimpleMatrix factorMatrix = new SimpleMatrix();
        
        factorMatrix.elements = new double[][] { rightFactor };
        
        return factorMatrix.times(this.getTranspose()).elements[0];
    }
    
    /**
     * Computes the product of this matrix (as the left factor) with the
     * specifed matrix factor (as the right factor).  Neither factor is
     * modified.
     * 
     * @param  rightFactor a {@code SimpleMatrix} representing the right-hand
     *         factor in the requested matrix multiplication; must have the same
     *         number of rows as this matrix has columns
     *         
     * @return a new {@code SimpleMatrix} representing the product of this
     *         matrix and the specified one.
     *
     * @throws IllegalArgumentException if the right factor does not have the
     *         same number of rows as this matrix has columns
     */
    public SimpleMatrix times(SimpleMatrix rightFactor) {
        double[][] productElements;
        double[][] leftElements = this.elements;
        double[][] rightElements = rightFactor.elements;
        int commonCount = this.getColumnCount();
        SimpleMatrix result;
        
        // Check for compatible matrix shapes
        if (rightFactor.getRowCount() != commonCount) {
            throw new IllegalArgumentException("Incompatible matrix factors");
        }
        
        // Compute the elements of the product matrix
        productElements
                = new double[this.getRowCount()][rightFactor.getColumnCount()];
        
        for (int row = 0; row < productElements.length; row++) {
            for (int col = 0; col < productElements[0].length; col++) {
                for (int i = 0; i < commonCount; i++) {
                    productElements[row][col]
                            += leftElements[row][i] * rightElements[i][col]; 
                }
            }
        }
        
        // Create and return the result matrix (without copying its elements)
        result = new SimpleMatrix();
        result.elements = productElements;
        
        return result;
    }
    
    /**
     * Computes the matrix (element-wise) sum of this matrix and the specified
     * addend matrix; neither this matrix nor the addend matrix is modified.
     * 
     * @param  addend a {@code SimpleMatrix} representing the matrix to add to
     *         this one; must have the same shape as this matrix
     *         
     * @return a new {@code SimpleMatrix} representing the matrix sum of this
     *         matrix and the addend
     *         
     * @throws IllegalArgumentException if the addend has a different shape than
     *         this matrix does
     */
    public SimpleMatrix plus(SimpleMatrix addend) {
        SimpleMatrix sumMatrix;
        double[][] sumElements;
        
        // Check that the addend has the same shape as this matrix
        if ((this.getColumnCount() != addend.getColumnCount())
                || (this.getRowCount() != addend.getRowCount())) {
            throw new IllegalArgumentException("Incompatible matrices");
        }
        
        sumElements = addend.getElements();  // A copy of addend's elements
        
        // Add this matrix's elements
        for (int row = 0; row < sumElements.length; row++) {
            for (int col = 0; col < sumElements[0].length; col++) {
                sumElements[row][col] += elements[row][col];
            }
        }
        
        // Create and return the sum matrix (without copying its elements)
        sumMatrix = new SimpleMatrix();
        sumMatrix.elements = sumElements;
        
        return sumMatrix;
    }
    
    /**
     * Computes and returns the transpose of this matrix, or returns the cached
     * result of a previous transposition; this matrix is not modified
     *  
     * @return a {@code SimpleMatrix} representing the transpose of this matrix
     */
    public SimpleMatrix getTranspose() {
        if (cachedTranspose == null) {
            SimpleMatrix transpose = new SimpleMatrix();
            
            double[][] transposeElements
                    = new double[getColumnCount()][getRowCount()];
            
            for (int i = 0; i < elements.length; i++) {
                for (int j = 0; j < elements[0].length; j++) {
                    transposeElements[j][i] = elements[i][j];
                }
            }
            
            transpose.elements = transposeElements;
            transpose.cachedTranspose = this;
            cachedTranspose = transpose;
        }
        
        return cachedTranspose;
    }
    
    /**
     * Computes the multiplicative inverse of this matrix and returns it, or
     * returns the cached result of a previous inverse computation; this matrix
     * is not modified.  Only square matrices -- and not all of those -- have
     * inverses, and this method will throw an exception if invoked on a matrix
     * that is not invertible.
     * 
     * @return a {@code SimpleMatrix} representing the multiplicative inverse
     *         of this matrix
     *         
     * @throws UnsupportedOperationException if this matrix is not square
     * @throws IllegalStateException if this matrix is square but not invertible
     *         (i.e. it is singular)
     */
    public SimpleMatrix getInverse() {
        int dimension = getRowCount();
        
        if (getColumnCount() != dimension) {
            throw new UnsupportedOperationException(
                    "Non-square matrix cannot be inverted");
        } else if (cachedInverse == null) {
            double[][] work = new double[dimension][dimension * 2];
            double[][] inverseElements;
            SimpleMatrix result;
            
            // Work will hold the adjoined matrix elements
            for (int row = 0; row < dimension; row++) {
                System.arraycopy(elements[row], 0, work[row], 0, dimension);
                work[row][dimension + row] = 1.0;
            }
            
            /*
             * We compute the inverse elements via Gauss-Jordan with partial
             * (rows only) pivoting.  Use of partial instead of full pivoting
             * represents a stability vs. speed and simplicity trade-off:
             * full pivoting would provide a slightly more stable algorithm,
             * but pivoting columns requires a loop with multiple swaps, plus
             * a more extensive search for the element to pivot on; row
             * pivoting uses a simpler search and requires only a single
             * swap per pivot, yet still provides good stability.
             */
            
            for (int col = 0; col < dimension; col++) {
                
                // Find the maximum-magnitude element
                int maxRow = col;
                double maxMagnitude = Math.abs(work[maxRow][col]);
                
                for (int testRow = col + 1; testRow < dimension; testRow++) {
                    double magnitude = Math.abs(work[testRow][col]);
                    
                    if (magnitude > maxMagnitude) {
                        maxMagnitude = magnitude;
                        maxRow = testRow;
                    }
                }
                
                // Test for a singular matrix
                if (maxMagnitude < SINGULARITY_LIMIT) {
                    throw new IllegalStateException("This matrix is singular");
                
                /*
                 * Exchange rows if necessary to put the maximum-magnitude
                 * element onto the main diagonal
                 */
                } else if (maxRow != col) {
                    double[] temp = work[col];
                    
                    work[col] = work[maxRow];
                    work[maxRow] = temp;
                    maxRow = col;
                }
                
                // Cancel the current column in the other rows
                for (int workRow = 0; workRow < dimension; workRow++) {
                    if (workRow != maxRow) {
                        double factor = -work[workRow][col] / work[maxRow][col];
                        
                        for (int workCol = col + 1;
                                workCol < work[workRow].length; workCol++) {
                            work[workRow][workCol] +=
                                factor * work[maxRow][workCol];
                        }
                    }
                }
                
                // scale the working row
                for (int workCol = col + 1; workCol < work[maxRow].length;
                        workCol++) {
                    work[maxRow][workCol] /= work[maxRow][col];
                }
            }
            
            // Read out the inverse elements
            inverseElements = new double[dimension][dimension];
            for (int row = 0; row < dimension; row++) {
                System.arraycopy(work[row], dimension,
                        inverseElements[row], 0, dimension);
            }
            
            // Put the inverse elements into SimpleMatrix form
            result = new SimpleMatrix();
            result.elements = inverseElements;
            result.cachedInverse = this;
            
            cachedInverse = result;
        }
        
        return cachedInverse;
    }
    
    /**
     * Creates a {@code SimpleMatrix} containing a single row, the elements of
     * which being the same as those of the specified double array
     * 
     * @param  elements a {@code double[]} containing the elements of the
     *         single matrix row
     *         
     * @return a {@code SimpleMatrix} representing the desired row matrix
     * 
     * @throws IllegalArgumentException if the {@code elements} array has
     *         length less than 1
     */
    public static SimpleMatrix createRowMatrix(double[] elements) {
        if (elements.length < 1) {
            throw new IllegalArgumentException(
                    "At least one element must be specified");
        }
        SimpleMatrix matrix = new SimpleMatrix();
        double[][] rowElements = new double[1][];
        
        rowElements[0] = elements.clone();
        matrix.elements = rowElements;
        
        return matrix;
    }
    
    /**
     * Creates a {@code SimpleMatrix} containing a single column, the elements
     * of which being the same as those of the specified double array
     * 
     * @param  elements a {@code double[]} containing the elements of the
     *         single matrix column
     *         
     * @return a {@code SimpleMatrix} representing the desired column matrix
     * 
     * @throws IllegalArgumentException if the {@code elements} array has
     *         length less than 1
     */
    public static SimpleMatrix createColumnMatrix(double[] elements) {
        if (elements.length < 1) {
            throw new IllegalArgumentException(
                    "At least one element must be specified");
        }
        SimpleMatrix matrix = new SimpleMatrix();
        double[][] columnElements = new double[elements.length][1];
        
        for (int i = 0; i < elements.length; i++) {
            columnElements[i][0] = elements[i];
        }
        matrix.elements = columnElements;
        
        return matrix;
    }
    
    /**
     * Creates and returns a {@code SimpleMatrix} representing an identity
     * matrix of the specified order (i.e. a matrix having {@code order} rows
     * and {@code order} columns, with the value one at every position on the
     * main diagonal and the value zero everywhere else) 
     * 
     * @param  order the order of the requested identity matrix; must be at
     *         least 1
     * 
     * @return a {@code SimpleMatrix} representing an identity matrix of the
     *         specified order
     *
     * @throws IllegalArgumentException if {@code order} is less than 1
     */
    public static SimpleMatrix createIdentityMatrix(int order) {
        if (order < 1) {
            throw new IllegalArgumentException(
                    "The identity matrix order must be positive");
        }
        SimpleMatrix identity = new SimpleMatrix();
        double[][] elements = new double[order][order];
        
        for (int i = 0; i < order; i++) {
            elements[i][i] = 1;
        }
        identity.elements = elements;
        identity.cachedInverse = identity;
        identity.cachedTranspose = identity;
        
        return identity;
    }
}
