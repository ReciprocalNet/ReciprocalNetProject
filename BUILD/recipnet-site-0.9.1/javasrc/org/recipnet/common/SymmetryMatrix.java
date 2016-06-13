/*
 * Reciprocal Net Project
 *
 * SymmetryMatrix.java
 *
 * 14-Nov-2005: jobollin extracted this class from SpaceGroupSymbolBL and
 *              renamed it from OperatorMatrix to SymmetryMatrix
 * 22-Dec-2005: jobollin fixed bugs in the inverse() and plus(int[], boolean)
 *              methods, and made changes to the times(SymmetryMatrix, boolean)
 *              method and to two constructors aimed at reducing the number of
 *              array copies created
 */

package org.recipnet.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * The matrix representation of a crystallographic symmetry operation as
 * applicable to positions expressed in fractional coordinates.  A symmetry
 * operation consists of a rotation part and a translation part; both are
 * represented by this class.  This class provides for composing operators by
 * means of matrix multiplication, and for adding translation components after
 * construction (e.g. to effect a relocation of the corresponding symmetry
 * element).  This class also provides equals() and hashCode() implementations
 * that render matrices with the same elements equal to each other.
 */
public class SymmetryMatrix implements Cloneable {

    /**
     * An enumeration of the types of crystallographic symmetry operation that a
     * {@code SymmetryMatrix} may represent.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public enum Type {
        
        /** A type code for matrices representing sixfold rotoinversions */
        SIX_BAR(-6),
        
        /** A type code for matrices representing fourfold rotoinversions */
        FOUR_BAR(-4),
        
        /** A type code for matrices representing threefold rotoinversions */
        THREE_BAR(-3),
        
        /** A type code for matrices representing reflections */
        REFLECTION(-2),
        
        /** A type code for matrices representing inversions */
        @SuppressWarnings("hiding") INVERSION(-1),
        
        /** A type code for matrices representing translations */
        TRANSLATION(1),
        
        /** A type code for matrices representing twofold rotations */
        TWOFOLD(2),
        
        /** A type code for matrices representing threefold rotations */
        THREEFOLD(3),
        
        /** A type code for matrices representing fourfold rotations */
        FOURFOLD(4),
        
        /** A type code for matrices representing sixfold rotations */
        SIXFOLD(6);
        
        /**
         * A map from operation type codes to {@code Type}s, used by the
         * {@link #forOrder(int, boolean)} method
         */
        private static Map<Integer, Type> codeMap;
        
        /**
         * The operation type code for this type; its magnitude is the formal
         * order of this operation type, and its sign indicates whether it is a
         * proper operation (if positive) or an improper one (if negative)
         */
        private final int code;
        
        /**
         * The multiplicity of this operation; for most types this is the same
         * as the operation's formal order as encoded in {@link #code}, but for
         * odd-order, improper operations and non-trivial translations it is
         * twice the order.  
         */
        private final int multiplicity;
        
        /**
         * Initializes a {@code Type} with the specified operation type code
         * 
         * @param code the operation type code; its magnitude is the formal
         *        order of this operation type, and its sign indicates
         *        whether it is a proper operation (if positive) or an
         *        improper one (if negative)
         */
        private Type(int code) {
            
            /*
             * Determine the multiplicity of this type of matrix; odd
             * rotoinversions -1 and -3 must be applied more times than
             * their order would otherwise suggest.  Also, pure
             * translations' true order (assumed 2) is not reflected by
             * their code, which is based only on the rotation part.
             */
            int temp = Math.abs(code);
            
            if ((code < 2) && ((temp % 2) == 1)) {
                temp *= 2;
            }
            multiplicity = temp;
            this.code = code;
        }
        
        /**
         * Returns the multiplicity of the symmetry operation type represented
         * by this {@code Type}
         * 
         * @return the multiplicity of this symmetry operation type; that is,
         *         the lowest positive power to which an operation of this type
         *         must be raised to produce a pure unit cell translation
         *         (possibly a zero translation)
         */
        public int getMultiplicity() {
            return multiplicity;
        }
        
        /**
         * Obtains the {@code Type} corresponding to the specified formal order
         * and propriety
         * 
         * @param  order the formal order of the desired operation type
         * @param  isProper {@code true} if the desired type represents a
         *         proper operation of the specified order, {@code false} if it
         *         represents an improper one
         *         
         * @return the {@code Type} corresponding to the specified order and
         *         propriety, or {@code null} if none exists
         */
        public static Type forOrder(int order, boolean isProper) {
            if (codeMap == null) {
                Map<Integer, Type> map = new HashMap<Integer, Type>();
                
                for (Type type : values()) {
                    map.put(type.code, type);
                }
                
                codeMap = map;
            }
            
            return codeMap.get(order * (isProper ? 1 : -1));
        }
    }
    
    /**
     * A {@code SymmetryMatrix} instance representing the identity operation;
     * should never be mutated, else Bad Things will happen
     */
    public final static SymmetryMatrix IDENTITY = new SymmetryMatrix(
            new int[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } },
            new int[3]);

    /**
     * A {@code SymmetryMatrix} instance representing an inversion through the
     * origin; should never be mutated, else Bad Things will happen
     */
    public final static SymmetryMatrix INVERSION = new SymmetryMatrix(
            new int[][] { { -1, 0, 0 }, { 0, -1, 0 }, { 0, 0, -1 } },
            new int[3]);

    /**
     * An internal table used by the {@link #getType()} method to determine the
     * order of the operations represented by class instances
     */
    private static final int[] ORDER_TABLE = new int[] { 2, 3, 4, 6, 1 };

    /**
     * An int[][] containing the rotation matrix part of this SymmetryMatrix
     */
    private final int[][] matrix;

    /**
     * an int[] containing the translation vector part of this
     * SymmetryMatrix
     */
    private final int[] vector;

    /**
     * Transient storage for the computed hash code of this object; prevents
     * redundant computation of the hash
     */
    private transient int savedHash = Integer.MIN_VALUE;
    
    /**
     * Transient storage for the computed type of this {@code SymmetryMatrix};
     * prevents redundant computation of the type
     */
    private transient Type savedType = null;

    /**
     * Transient storage for the computed inverse of this symmetry matrix;
     * multiplication of this matrix by {@code inverse} or of {@code inverse}
     * by this matrix always produces an identity matrix (but not THE
     * {@link #IDENTITY identity matrix}).
     */
    private transient SymmetryMatrix inverse = null;

    /**
     * Initializes a new SymmetryMatrix with the specified rotation matrix
     * and a zero translation vector.  The argument is copied; no
     * references to it or its components are retained by the new
     * SymmetryMatrix instance.
     *
     * @param  matrix an int[][] of dimension [3][3], containing the
     *         rotation matrix elements
     *
     * @throws IllegalArgumentException if the dimensions of the matrix
     *         are incorrect, or if any of the elements of the matrix
     *         are other than -1, 0, or 1
     */
    public SymmetryMatrix(int[][] matrix) {
        this(matrix, IDENTITY.vector, false);
    }

    /**
     * Initializes a new {@code SymmetryMatrix} with the specified translation
     * vector (not normalized) and a unit rotation matrix. The argument is
     * copied (unmodified); no reference to it is retained by the new
     * {@code SymmetryMatrix} instance.
     * 
     * @param  vector an {@code int[]} of length 3, containing the elements of
     *         the translation vector in units of twelfths
     * @throws IllegalArgumentException if the dimension of the vector is
     *         incorrect
     */
    public SymmetryMatrix(int[] vector) {
        this(IDENTITY.matrix, vector, false);
    }

    /**
     * Initializes a new {@code SymmetryMatrix} with the specified rotation
     * matrix and translation vector, without normalization. The arguments are
     * copied (unmodified); no references to them or their components are
     * retained by the new {@code SymmetryMatrix} instance.
     * 
     * @param  matrix an int[][] of dimension [3][3], containing the rotation
     *         matrix elements
     * @param  vector an {@code int[]} of length 3, containing the elements of
     *         the translation vector in units of twelfths
     * @throws IllegalArgumentException if the dimensions of the matrix or
     *         vector are incorrect, or if any of the elements of the matrix
     *         are other than -1, 0, or 1
     */
    public SymmetryMatrix(int[][] matrix, int[] vector) {
        this(matrix, vector, false);
    }

    /**
     * Initializes a new SymmetryMatrix with the specified rotation matrix
     * and translation vector; if requested, translations are "normalized"
     * in the sense that enough full-unit translations are added or
     * subtracted along each axis to bring the elements between 0 and
     * 11 (inclusive, units of twelfths).  The arguments are copied; no
     * references to them or their components are retained by the new
     * SymmetryMatrix instance.
     *
     * @param  matrix an int[][] of dimension [3][3], containing the
     *         rotation matrix elements
     * @param  vector an {@code int[]} of length 3, containing
     *         the elements of the translation vector in units of twelfths
     * @param  normalize {@code true} if the translation vector should
     *         be normalized
     *
     * @throws IllegalArgumentException if the dimensions of the matrix or
     *         vector are incorrect, or if any of the elements of the matrix
     *         are other than -1, 0, or 1
     */
    public SymmetryMatrix(int[][] matrix, int[] vector, boolean normalize) {
        if (matrix.length != 3) {
            throw new IllegalArgumentException("Wrong matrix dimensions");
        } else if (vector.length != 3) {
            throw new IllegalArgumentException("Wrong vector length");
        }

        this.matrix = new int[3][];
        this.vector = new int[3];

        for (int i = 0; i < 3; i++) {
            if (matrix[i].length != 3) {
                throw new IllegalArgumentException(
                        "Wrong matrix dimensions");
            }
            for (int j = 0; j < 3; j++) {
                if ((matrix[i][j] > 1) || (matrix[i][j] < -1)) {
                    throw new IllegalArgumentException(
                            "Illegal matrix element: M[" + i + "," + j
                            + "] == " + matrix[i][j]);
                }
            }
            this.matrix[i] = matrix[i].clone();
            if (normalize) {
                this.vector[i] = vector[i] % 12;
                if (this.vector[i] < 0) {
                    this.vector[i] += 12;
                }
            } else {
                this.vector[i] = vector[i];
            }
        }
    }
    
    /**
     * Returns a copy of the rotation matrix part of this SymmetryMatrix
     *
     * @return an {@code int[][]} containing a copy of the rotation
     *         matrix of this {@code SymmetryMatrix}; can be modified
     *         without effect on this {@code SymmetryMatrix}
     */
    public int[][] getRotationMatrix() {
        int[][] rval = new int[3][];

        for (int i = 0; i < matrix.length; i++) {
            rval[i] = matrix[i].clone();
        }

        return rval;
    }

    /**
     * Returns a copy of the translation vector part of this
     * {@code SymmetryMatrix}
     *
     * @return an {@code int[]} containing a copy of the translation
     *         vector (in units of twelfths); can be modified without effect on
     *         this {@code SymmetryMatrix}
     */
    public int[] getTranslationVector() {
        return vector.clone();
    }

    /**
     * Returns the type of this SymmetryMatrix, which corresponds to the
     * order of the corresponding symmetry element and to whether it is
     * proper or improper
     *
     * @return the type; one of the TYPE_* codes defined by this class
     */
    public Type getType() {
        
        /*
         * This method is idempotent; it may be invoked concurrently without
         * synchronization
         */
         
        if (savedType == null) {
            int det = det();
    
            try {
                savedType = Type.forOrder(
                        ORDER_TABLE[1 + (trace() * det)], (det > 0));
            } catch (IndexOutOfBoundsException ioobe) {
                IllegalStateException ise = new IllegalStateException(
                        "Operator Matrix has incorrect trace or determinant");
    
                ise.initCause(ioobe);
                throw ise;
            }
        }
    
        return savedType;
    }

    /**
     * Computes the trace of the rotation part of this {@code SymmetryMatrix}
     *
     * @return the trace of the rotation part of this {@code SymmetryMatrix}
     */
    private int trace() {
        return (matrix[0][0] + matrix[1][1] + matrix[2][2]);
    }

    /**
     * Computes the determinant of the rotation part of this
     * {@code SymmetryMatrix}; used in determining the type
     *
     * @return the determinant of the rotation part of this
     *         {@code SymmetryMatrix}
     */
    private int det() {
        return ((matrix[0][0] * matrix[1][1] * matrix[2][2])
                + (matrix[1][0] * matrix[2][1] * matrix[0][2])
                + (matrix[2][0] * matrix[0][1] * matrix[1][2])
                - (matrix[0][2] * matrix[1][1] * matrix[2][0])
                - (matrix[1][2] * matrix[2][1] * matrix[0][0])
                - (matrix[2][2] * matrix[0][1] * matrix[1][0]));
    }

    /**
     * Computes a new symmetry matrix by adding the specified translation vector
     * to a copy of this matrix
     * 
     * @param  translation the translation vector as an {@code int[]}, in units
     *         of twelfths
     * @param  normalize {@code true} if the result should be normalized,
     *         {@code false} if not
     *         
     * @return the new {@code SymmetryMatrix}
     */
    public SymmetryMatrix plus(int[] translation, boolean normalize) {
        SymmetryMatrix mat
                = new SymmetryMatrix(this.matrix, this.vector, false);

        if (translation.length != 3) {
            throw new IllegalArgumentException(
                    "Translation vector is not length 3");
        } else {
            for (int i = 0; i < 3; i++) {
                mat.vector[i] += translation[i];
            }
            mat.savedType = this.savedType;
        }
        
        return (normalize ? mat.normalize() : mat);
    }
    
    /**
     * Computes and returns the product of this {@code SymmetryMatrix}
     * and the specified one; translation components of the product will not be
     * normalized; neither this {@code SymmetryMatrix} nor the other
     * is modified
     *
     * @param  factor the {@code SymmetryMatrix} to multiply by this
     *         one
     *
     * @return a new {@code SymmetryMatrix} representing the product of
     *         this one (on the left) and {@code factor} (on the right)
     * 
     * @see #times(SymmetryMatrix, boolean)
     */
    public SymmetryMatrix times(SymmetryMatrix factor) {
        return this.times(factor, false);
    }

    /**
     * Computes and returns the product of this {@code SymmetryMatrix}
     * and the specified one; translation components of the product will be
     * normalized if and only if so specified; neither this
     * {@code SymmetryMatrix} nor the other is modified.  When
     * normalization is not the desired behavior the
     * {@link #times(SymmetryMatrix)} method provides a more convenient
     * interface.
     *
     * @param  factor the {@code SymmetryMatrix} to multiply by this
     *         one
     * @param  normalize {@code true} if the translation components
     *         of the product should be normalized, false otherwise
     *
     * @return a new {@code SymmetryMatrix} representing the product of
     *         this one (on the left) and {@code factor} (on the right)
     */
    public SymmetryMatrix times(SymmetryMatrix factor, boolean normalize) {
        SymmetryMatrix product = new SymmetryMatrix(this.vector);

        for (int i = 0; i < 3; i++) {
            product.matrix[i][i] = 0;
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    product.matrix[i][k]
                            += this.matrix[i][j] * factor.matrix[j][k];
                }
                product.vector[i] += this.matrix[i][j] * factor.vector[j];
            }
        }

        return (normalize ? product.normalize() : product);
    }

    /**
     * Computes the transformation under the symmetry operation represented by
     * this matrix of the specified fractional coordinates, considered as the
     * coordinates of a point.  This transformation is distinguished from the
     * one performed by {@link #transformVector(double[])} is that this one
     * includes the translation components of this matrix, whereas the other
     * does not.
     * 
     * @param  fractionalCoords a double[] of length 3, containing the
     *         fractional coordinates of the point to transform
     *         
     * @return a double[] of length 3 containing the coordinates of the
     *         transformed point
     * 
     * @throws IllegalArgumentException if the argument does not have length 3
     */
    public double[] transformPoint(double[] fractionalCoords) {
        try {
            double[] product = new double[3];

            for (int i = 0; i < 3; i++) {
                product[i] = this.vector[i] / 12.0;

                for (int j = 0; j < 3; j++) {
                    product[i] += this.matrix[i][j] * fractionalCoords[j];
                }
            }
            return product;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalArgumentException(
                    "The coordinate array must have length 3");
        }
    }
    
    /**
     * Computes the transformation under the symmetry operation represented by
     * this matrix of the specified fractional coordinates, considered as the
     * coordinates of a vector.  This transformation is distinguished from the
     * one performed by {@link #transformPoint(double[])} is that this one
     * does not include the translation components of this matrix, whereas the
     * other does.
     * 
     * @param  fractionalCoords a double[] of length 3, containing the
     *         fractional coordinates of the vector to transform
     *         
     * @return a double[] of length 3 containing the coordinates of the
     *         transformed point
     * 
     * @throws IllegalArgumentException if the argument does not have length 3
     */
    public double[] transformVector(double[] fractionalCoords) {
        try {
            // Automatically initialized to zeroes:
            double[] product = new double[3];

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    product[i] += this.matrix[i][j] * fractionalCoords[j];
                }
            }
            return product;
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalArgumentException(
                    "The coordinate array must have length 3");
        }
    }
    
    /**
     * Returns a symmetry matrix with a rotation component equal to this
     * one's and a normalized translation component
     * 
     * @return a {@code SymmetryMatrix} with the same rotation component 
     *         as this one and a normalized translation component; may be this
     *         {@code SymmetryMatrix} itself if it is already normalized
     * 
     * @see #SymmetryMatrix(int[][], int[], boolean)
     */
    public SymmetryMatrix normalize() {
        for (int i : vector) {
            if ((i < 0) || (i > 11)) {
                return new SymmetryMatrix(matrix, vector, true);
            }
        }
        
        return this;
    }

    /**
     * Returns the inverse of this symmetry matrix, computing it if necessary
     * 
     * @return a {@code SymmetryMatrix} that represents the multiplicative
     *         inverse of this matrix
     *         
     * @throws IllegalStateException if this matrix does not have determinant
     *         1 or -1
     */
    public SymmetryMatrix inverse() {
        
        /*
         * Note: without synchronization, this method does not guarantee to
         * prevent multiple distinct copies of the inverse being computed and
         * used.  This is mostly harmless, as all copies will be equal to each
         * other and have the same hash code, and can be used interchangeably
         * for most purposes.  
         */
        
        if (inverse == null) {
            
            // Compute the inverse
            
            int determinant = det();
            int[][] newMatrix = new int[3][3];
            int[] newVector = new int[3];
            
            /*
             * The matrix is not a *symmetry* matrix if the determinant is
             * not one
             */
            if (Math.abs(determinant) != 1) {
                throw new IllegalStateException("Invalid symmetry matrix");
            }
            
            // Compute the rotation matrix of the inverse
            for (int row = 0; row < 3; row++) {
                int c1 = (row + 1) % 3;
                int c2 = (row + 2) % 3;
                
                for (int col = 0; col < 3; col++) {
                    int r1 = (col + 1) % 3;
                    int r2 = (col + 2) % 3;
                    
                    /*
                     * This formula would normally require division by the
                     * determinant; we can multiply instead because we know the
                     * determinant is either 1 or -1
                     */
                    newMatrix[row][col] = determinant * 
                            (matrix[r1][c1] * matrix[r2][c2]
                            - matrix[r1][c2] * matrix[r2][c1]);
                }
            }
            
            // Compute the translation vector of the inverse
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    newVector[row] -= newMatrix[row][col] * vector[col];
                }
            }
            
            // Construct the inverse from the pieces we've built 
            inverse = new SymmetryMatrix(newMatrix, newVector, false);
            
            /*
             * We can save time and memory in case the inverse's inverse is
             * ever requested:
             */ 
            inverse.inverse = this;
        }
        
        return inverse;
    }

    /**
     * Determines whether this {@code SymmetryMatrix} is equal to the
     * specified object, which is the case if and only if the object is an
     * {@code SymmetryMatrix} with the same rotation matrix and
     * translation vector components
     *
     * @param  o the {@code Object} to compare to this one
     *
     * @return {@code true} if this object is equal to the other,
     *         {@code false} otherwise
     */
    @Override
    final public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof SymmetryMatrix) {
            SymmetryMatrix m = (SymmetryMatrix) o;

            for (int i = 0; i < 3; i++) {
                if (!Arrays.equals(this.matrix[i], m.matrix[i])) {
                    return false;
                }
            }

            return Arrays.equals(this.vector, m.vector);
        } else {
            return false;
        }
    }

    /**
     * Returns the hash code for this object, which is computed from all the
     * elements of the rotation matrix and translation vector.
     *
     * @return the hash code
     */
    @Override
    final public int hashCode() {

        /*
         * This hash code uses the lowest-order 30 bits; it has the
         * property that two SymmetryMatrix objects *with normalized
         * translation components* have the same hash code if and only if
         * they are equal (per equals() above).  Unequal SymmetryMatrix objects
         * may have the same hash code, however, if one or both are not
         * normalized.
         */

        if (savedHash == Integer.MIN_VALUE) {
            int work = 0;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    work = (work << 2) + (matrix[i][j] & 0x3);
                }
                work = (work << 4) + (vector[i] & 0xf);
            }
            savedHash = work;
        }

        return savedHash;
    }

    /**
     * Returns a clone of this {@code SymmetryMatrix} that does not
     * share mutable, non-transient data with the original.
     * 
     * @return a new {@code SymmetryMatrix} equal to this one but distinct from
     *         it 
     */
    @Override
    public SymmetryMatrix clone() {
        
        /*
         * NOTE: this version of SymmetryMatrix has no mutable data that needs
         * protection 
         */
        
        try {
            return (SymmetryMatrix) super.clone();
        } catch (CloneNotSupportedException cnse) {

            // should never happen because this class implements Cloneable
            throw new RuntimeException(cnse);
        }
    }
}

