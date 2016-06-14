/*
 * Reciprocal Net Project
 *
 * SimpleMatrixTests.java
 *
 * Dec 16, 2005: jobollin wrote first draft
 */

package org.recipnet.common.algebra;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code SimpleMatrix} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SimpleMatrixTests extends TestCase {

    /**
     * Initializes a new {@code SimpleMatrixTests} to run the named test.
     * 
     * @param  testName the name of the test to run
     */
    public SimpleMatrixTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal operation of the {@code double[][]} constructor, along
     * with the {@code getRowCount()}, {@code getColumnCount()}, and
     * {@code getElements()} methods
     */
    public void testConstructor__doubleArrayArray() {
        performConstructorTest(new double[][] {{1}});
        performConstructorTest(new double[][] {{1, 2}});
        performConstructorTest(new double[][] {{1}, {2}});
        performConstructorTest(new double[][] {{1, 2}, {3, 4}});
        performConstructorTest(new double[][] {{1, 2, 3}, {4, 5, 6}});
        performConstructorTest(new double[][] {{1, 2}, {3, 4}, {5, 6}});
    }

    /**
     * Tests the behavior of the {@code int[][]} constructor when its argument
     * or one of its argument's elements is {@code null}; a
     * {@code NullPointerException} is expected 
     */
    public void testConstructor__nulldoubleArrayArray() {
        try {
            new SimpleMatrix((double[][]) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {null, {1, 2}});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {{1, 2, 3}, null});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {{1, 3}, null, {5, 6}});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Performs the matrix constructor test for a specific {@code double[][]}
     * of matrix elements
     * 
     * @param  ds a {@code double[][]} of the matrix elements to test with
     */
    private void performConstructorTest(double[][] ds) {
        SimpleMatrix matrix = new SimpleMatrix(ds);
        double[][] elements = matrix.getElements();
        
        assertEquals("Matrix has wrong number of rows", ds.length,
                matrix.getRowCount());
        assertEquals("Matrix has wrong number of columns", ds[0].length,
                matrix.getColumnCount());
        assertTrue("Matrix elements are incorrect",
                Arrays.deepEquals(ds, elements));
        assertTrue("Element array was stored, not copied",
                ds != elements);
        for (int i = 0; i < ds.length; i++) {
            assertTrue("Element array row was stored, not copied",
                    ds[i] != elements[i]);
        }
    }

    /**
     * Tests the normal operation of the {@code int[][]} constructor, along
     * with the {@code getRowCount()}, {@code getColumnCount()}, and
     * {@code getElements()} methods
     */
    public void testConstructor__intArrayArray() {
        performConstructorTest(new int[][] {{1}});
        performConstructorTest(new int[][] {{1, 2}});
        performConstructorTest(new int[][] {{1}, {2}});
        performConstructorTest(new int[][] {{1, 2}, {3, 4}});
        performConstructorTest(new int[][] {{1, 2, 3}, {4, 5, 6}});
        performConstructorTest(new int[][] {{1, 2}, {3, 4}, {5, 6}});
    }

    /**
     * Tests the behavior of the {@code int[][]} constructor when its argument
     * or one of its argument's elements is {@code null}; a
     * {@code NullPointerException} is expected 
     */
    public void testConstructor__nullintArrayArray() {
        try {
            new SimpleMatrix((int[][]) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new int[][] {null, {1, 2}});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new int[][] {{1, 2, 3}, null});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
        
        try {
            new SimpleMatrix(new int[][] {{1, 3}, null, {5, 6}});
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Performs the matrix constructor test for a specific {@code double[][]}
     * of matrix elements
     * 
     * @param  is a {@code int[][]} of the matrix elements to test with
     */
    private void performConstructorTest(int[][] is) {
        SimpleMatrix matrix = new SimpleMatrix(is);
        double[][] elements = matrix.getElements();
        double[][] doubles = new double[is.length][is[0].length];

        for (int i = 0; i < is.length; i++) {
            for (int j = 0; j < is[0].length; j++) {
                doubles[i][j] = is[i][j];
            }
        }
        
        assertEquals("Matrix has wrong number of rows", is.length,
                matrix.getRowCount());
        assertEquals("Matrix has wrong number of columns", is[0].length,
                matrix.getColumnCount());
        assertTrue("Matrix elements are incorrect",
                Arrays.deepEquals(doubles, elements));
    }

    /**
     * Tests the normal operation of the {@code SimpleMatrix.times(double)}
     * method
     */
    public void testMethod__times_double() {
        double[][] elements = new double[][] {{1, 2}, {3, 5}, {7, 11}};
        SimpleMatrix matrix = new SimpleMatrix(elements);
        SimpleMatrix product = matrix.times(2);
        
        assertTrue("Original matrix was modified",
                Arrays.deepEquals(elements, matrix.getElements()));
        assertTrue("Product matrix is incorrect",
                Arrays.deepEquals(new double[][] {{2, 4}, {6, 10}, {14, 22}},
                        product.getElements()));

        elements = new double[][] {{1, 2, 3, 5}};
        matrix = new SimpleMatrix(elements);
        product = matrix.times(-3);
        assertTrue("Original matrix was modified",
                Arrays.deepEquals(elements, matrix.getElements()));
        assertTrue("Product matrix is incorrect",
                Arrays.deepEquals(new double[][] {{-3, -6, -9, -15}},
                        product.getElements()));

        elements = new double[][] {{-2}, {-3}, {-5}};
        matrix = new SimpleMatrix(elements);
        product = matrix.times(7);
        assertTrue("Original matrix was modified",
                Arrays.deepEquals(elements, matrix.getElements()));
        assertTrue("Product matrix is incorrect",
                Arrays.deepEquals(new double[][] {{-14}, {-21}, {-35}},
                        product.getElements()));
    }

    /**
     * Tests the behavior of the {@code times(double[])} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__times_nulldoubleArray() {
        SimpleMatrix matrix = new SimpleMatrix(
                new double[][] {{1, 2, 3}, {4, 6, 8}, {9, 12, 15}});
        
        try {
            matrix.times((double[]) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code times(double[])} method when its
     * argument has the wrong length for the matrix's shape; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod__times_doubleArray__wrongLength() {
        try {
            new SimpleMatrix(new double[][] {{1}}).times(new double[] {3, 2});
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {{1}, {2}, {3}}).times(
                    new double[] {3, 2});
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {{1, 2, 3}}).times(
                    new double[] {3, 2});
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        try {
            new SimpleMatrix(new double[][] {{1, 2, 3}, {4, 5, 6}}).times(
                    new double[] {3, 2});
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal operation of the {@code times(double[])} method
     */
    public void testMethod__times_doubleArray() {
        performTimesArrayTest(new double[][] {{3}},
                new double[] {5}, new double[] {15});
        performTimesArrayTest(new double[][] {{3, 2, 5}},
                new double[] {2, 5, 7}, new double[] {51});
        performTimesArrayTest(new double[][] {{3}, {2}, {5}},
                new double[] {3}, new double[] {9, 6, 15});
        performTimesArrayTest(new double[][] {{1, 2, 3}, {4, 5, 6}},
                new double[] {-2, 3, 1}, new double[] {7, 13});
        performTimesArrayTest(new double[][] {{1, 2}, {3, 5}, {7, 11}},
                new double[] {9, 13}, new double[] {35, 92, 206});
    }

    /**
     * Performs the details of the test of the {@code times(double[])} method's
     * behavior
     * 
     * @param  matrixElements the elements of the matrix factor
     * @param  vectorElements the elements of the vector factor
     * @param  productElements the expected elements of the product
     */
    private void performTimesArrayTest(double[][] matrixElements,
            double[] vectorElements, double[] productElements) {
        SimpleMatrix matrix = new SimpleMatrix(matrixElements);
        double[] vector = vectorElements.clone();
        double[] product = matrix.times(vector);
        
        assertTrue("Matrix was modified",
                Arrays.deepEquals(matrixElements, matrix.getElements()));
        assertTrue("Vector was modified",
                Arrays.equals(vectorElements, vector));
        assertTrue("Product was incorrect",
                Arrays.equals(productElements, product));
    }

    /**
     * Tests the normal behavior of the {@code times(SimpleMatrix)} method
     */
    public void testMethod__times_SimpleMatrix() {
        double[][] identity2 = new double[][] {{1, 0}, {0, 1}};
        double[][] identity3 = new double[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        double[][] factor22a = new double[][] {{2, 3}, {7, 5}};
        double[][] factor22b = new double[][] {{-1, 3}, {2, 11}};
        double[][] factor32 = new double[][] {{7, 2}, {-3, 5}, {1, -2}};
        double[][] factor23 = new double[][] {{7, 2, -3}, {5, 1, -2}};
        double[][] factor33a
                = new double[][] {{1, 2, 3}, {-2, 5, 3}, {0, 7, -2}};
        double[][] factor33b
                = new double[][] {{-2, 5, -1}, {3, 8, 1}, {2, -1, 7}};
        
        performTimesMatrixTest(identity2, identity2, identity2);
        performTimesMatrixTest(identity2, factor22a, factor22a);
        performTimesMatrixTest(identity2, factor22b, factor22b);
        performTimesMatrixTest(identity2, factor23, factor23);
        performTimesMatrixTest(identity3, identity3, identity3);
        performTimesMatrixTest(identity3, factor33a, factor33a);
        performTimesMatrixTest(identity3, factor33b, factor33b);
        performTimesMatrixTest(identity3, factor32, factor32);
        performTimesMatrixTest(factor22a, factor22b,
                new double[][] {{4, 39}, {3, 76}});
        performTimesMatrixTest(factor22a, factor23,
                new double[][] {{29, 7, -12}, {74, 19, -31}});
        performTimesMatrixTest(factor33a, factor33b,
                new double[][] {{10, 18, 22}, {25, 27, 28}, {17, 58, -7}});
        performTimesMatrixTest(factor33a, factor32,
                new double[][] {{4, 6}, {-26, 15}, {-23, 39}});
        performTimesMatrixTest(factor23, factor32,
                new double[][] {{40, 30}, {30, 19}});
        performTimesMatrixTest(factor32, factor23,
                new double[][] {{59, 16, -25}, {4, -1, -1}, {-3, 0, 1}});
    }

    /**
     * Performs the details of the matrix multiplication tests; specifically,
     * verifies that the multiplication does not cause either factor to be
     * modified, and that the product has the specified elements
     * 
     * @param  leftElements the elements of the left matrix factor
     * @param  rightElements the elements of the right matrix factor
     * @param  productElements the expected elements of the product
     */
    private void performTimesMatrixTest(double[][] leftElements,
            double[][] rightElements, double[][] productElements) {
        SimpleMatrix leftFactor = new SimpleMatrix(leftElements);
        SimpleMatrix rightFactor = new SimpleMatrix(rightElements);
        SimpleMatrix product = leftFactor.times(rightFactor);

        assertTrue("Left factor was modified",
                Arrays.deepEquals(leftElements, leftFactor.getElements()));
        assertTrue("Right factor was modified",
                Arrays.deepEquals(rightElements, rightFactor.getElements()));
        assertTrue("Product is incorrect", 
                Arrays.deepEquals(productElements, product.getElements()));
    }

    /**
     * Tests the behavior of the {@code times(SimpleMatrix)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void test__times_nullSimpleMatrix() {
        try {
            new SimpleMatrix(new double[][] {{1, 2}, {3, 4}}).times(
                    (SimpleMatrix) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code times(SimpleMatrix)} method when the
     * argument does not have an appropriate shape; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod__times_SimpleMatrix__wrongShape() {
        SimpleMatrix leftFactor = new SimpleMatrix(
                new double[][] {{1, 2}, {3, 4}, {5, 6}});
        SimpleMatrix rightFactor = new SimpleMatrix(
                new double[][] {{1, 2}, {3, 4}, {5, 6}});
        
        try {
            leftFactor.times(rightFactor);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        rightFactor = SimpleMatrix.createIdentityMatrix(3);
        try {
            leftFactor.times(rightFactor);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code plus(SimpleMatrix)} method
     */
    public void testMethod__plus_SimpleMatrix() {
        performAdditionTest(new double[][] {{1}}, new double[][] {{-2}},
                new double[][] {{-1}});
        performAdditionTest(new double[][] {{1, 2}}, new double[][] {{-2, 3}},
                new double[][] {{-1, 5}});
        performAdditionTest(new double[][] {{1, 2}, {-1, 1}},
                new double[][] {{-2, 3}, {7, 1}},
                new double[][] {{-1, 5}, {6, 2}});
        performAdditionTest(new double[][] {{1, 2}, {-1, 1}, {-8, 3}},
                new double[][] {{-2, 3}, {7, 1}, {2, 0}},
                new double[][] {{-1, 5}, {6, 2}, {-6, 3}});
    }
    
    /**
     * Performs the details of the matrix addition tests; specifically,
     * verifies that the addition does not cause either sumand to be
     * modified, and that the sum has the specified elements
     * 
     * @param  leftElements the elements of the left matrix sumand
     * @param  rightElements the elements of the right matrix sumand
     * @param  sumElements the expected elements of the sum
     */
    private void performAdditionTest(double[][] leftElements,
            double[][] rightElements, double[][] sumElements) {
        SimpleMatrix leftMatrix = new SimpleMatrix(leftElements);
        SimpleMatrix rightMatrix = new SimpleMatrix(rightElements);
        SimpleMatrix sumMatrix = leftMatrix.plus(rightMatrix);

        assertTrue("Left sumand was modified",
                Arrays.deepEquals(leftElements, leftMatrix.getElements()));
        assertTrue("Right sumand was modified",
                Arrays.deepEquals(rightElements, rightMatrix.getElements()));
        assertTrue("Sum is incorrect", 
                Arrays.deepEquals(sumElements, sumMatrix.getElements()));
    }

    /**
     * Tests the behavior of the {@code times(SimpleMatrix)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void test__plus_nullSimpleMatrix() {
        try {
            new SimpleMatrix(new double[][] {{1, 2}, {3, 4}}).plus(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code times(SimpleMatrix)} method when the
     * argument does not have an appropriate shape; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod__plus_SimpleMatrix__wrongShape() {
        SimpleMatrix leftSumand = new SimpleMatrix(
                new double[][] {{1, 2}, {3, 4}, {5, 6}});
        SimpleMatrix rightSumand = new SimpleMatrix(
                new double[][] {{1, 2, 3}, {4, 5, 6}});
        
        try {
            leftSumand.plus(rightSumand);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        rightSumand = SimpleMatrix.createIdentityMatrix(2);
        try {
            leftSumand.plus(rightSumand);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        rightSumand = SimpleMatrix.createIdentityMatrix(3);
        try {
            leftSumand.plus(rightSumand);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code getTranspose()} method
     */
    public void testMethod__getTranspose() {
        performTranspositionTest(new double[][] {{3}});
        performTranspositionTest(new double[][] {{3, 2, 5}});
        performTranspositionTest(new double[][] {{3, 2, 5}, {1, 7, 13}});
        performTranspositionTest(new double[][] {{3, 2}, {5, 1}, {7, 13}});
        performTranspositionTest(
                new double[][] {{3, 2, -1}, {5, 1, 11}, {7, 13, -5}});
    }

    /**
     * Performs the details of the transposition test; specifically, tests that
     * the operation does not cause the original matrix to be modified, that
     * the transpose has the correct elements, and that the transposition caches
     * are used correctly
     * 
     * @param  elements a {@code double[][]} containing the elements of the
     *         original matrix
     */
    private void performTranspositionTest(double[][] elements) {
        SimpleMatrix matrix = new SimpleMatrix(elements);
        SimpleMatrix transpose = matrix.getTranspose();
        double[][] transposeElements = transpose.getElements();
        
        assertTrue("The original matrix was modified",
                Arrays.deepEquals(elements, matrix.getElements()));
        assertEquals("Transpose has wrong number of rows",
                matrix.getColumnCount(), transpose.getRowCount());
        assertEquals("Transpose has wrong number of columns",
                matrix.getRowCount(), transpose.getColumnCount());
        
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                assertEquals("Transpose has the wrong element at ("
                        + j + ", " + i + ")", elements[i][j],
                        transposeElements[j][i]);
            }
        }
        
        assertTrue("The transpose was not returned from cache",
                matrix.getTranspose() == transpose);
        assertTrue("The transpose's transposition cache was not set",
                transpose.getTranspose() == matrix);
    }

    /**
     * Tests the behavior of the {@code getInverse()} method for invertible
     * matrices
     */
    public void testMethod__getInverse() {
        performInversionTest(new double[][] {{1}});
        performInversionTest(new double[][] {{4}});
        performInversionTest(new double[][] {{1, 2}, {-1, 1}});
        performInversionTest(
                new double[][] {{1, 2, -2}, {3, 1, 1}, {-2, 3, 5}});
    }
    
    /**
     * Performs the details of the matrix inversion test; specifically,
     * verifies that the original matrix is not modified during inversion, that
     * the product of the original matrix and its computed inverse is an
     * identity matrix, and that the matrices' inverse caches are set and used
     * correctly
     * 
     * @param  elements a {@code double[][]} containing the elements of the
     *         matrix on which to test
     */
    private void performInversionTest(double[][] elements) {
        SimpleMatrix matrix = new SimpleMatrix(elements);
        SimpleMatrix inverse = matrix.getInverse();
        SimpleMatrix product = matrix.times(inverse);
        double[][] productElements = product.getElements();
        
        assertTrue("The base matrix was modified",
                Arrays.deepEquals(elements, matrix.getElements()));
        for (int i = 0; i < product.getColumnCount(); i++) {
            for (int j = 0; j < product.getRowCount(); j++) {
                assertEquals("The inverse is incorrect", (i == j) ? 1.0 : 0.0,
                        productElements[i][j], 1e-10);
            }
        }
        
        assertTrue("The inverse was not returned from cache",
                matrix.getInverse() == inverse);
        assertTrue("The inverse's inverse cache was not set",
                inverse.getInverse() == matrix);
    }

    /**
     * Tests the behavior of the {@code getInverse()} method when it is invoked
     * on a singular matrix; an {@code IllegalStateException} is expected
     */
    public void testMethod__getInverse__singular() {
        try {
            new SimpleMatrix(new double[][] {{0}}).getInverse();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        try {
            new SimpleMatrix(
                    new double[][] {{1, 2, 3}, {-2, 1, 7}, {-.5, 1.5, 5}}
                    ).getInverse();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code getInverse()} method when invoked on a
     * non-square matrix; an {@code UnsupportedOperationException} is expected
     */
    public void testMethod__getInverse__nonSquare() {
        try {
            SimpleMatrix.createRowMatrix(new double[] {1, 2}).getInverse();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        try {
            SimpleMatrix.createColumnMatrix(new double[] {1, 2}).getInverse();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        try {
            new SimpleMatrix(
                    new double[][] {{1, 2}, {3, 4}, {5, 6}}).getInverse();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }

        try {
            new SimpleMatrix(
                    new double[][] {{1, 2, 3}, {4, 5, 6}}).getInverse();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code createRowMatrix(double[])} method
     */
    public void testMethod__createRowMatrix_doubleArray() {
        double[] elements = new double[] {2.1};
        SimpleMatrix matrix = SimpleMatrix.createRowMatrix(elements);
        
        assertEquals("Row matrix has wrong number of rows",
                1, matrix.getRowCount());
        assertEquals("Row matrix has wrong number of columns",
                elements.length, matrix.getColumnCount());
        assertTrue("Matrix has wrong elements",
                Arrays.equals(elements, matrix.getElements()[0]));
        assertTrue("Element array was not copied",
                elements != matrix.getElements()[0]);
        
        elements = new double[] {2, 3, 5};
        matrix = SimpleMatrix.createRowMatrix(elements);
        assertEquals("Row matrix has wrong number of rows",
                1, matrix.getRowCount());
        assertEquals("Row matrix has wrong number of columns",
                elements.length, matrix.getColumnCount());
        assertTrue("Matrix has wrong elements",
                Arrays.equals(elements, matrix.getElements()[0]));
        assertTrue("Element array was not copied",
                elements != matrix.getElements()[0]);
    }

    /**
     * Tests the behavior of the {@code createRowMatrix(double[])} method
     * when its argument is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testMethod__createRowMatrix__nullArgument() {
        try {
            SimpleMatrix.createRowMatrix(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the {@code createRowMatrix(double[])} method
     * when its argument is a zero-length array; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod__createRowMatrix__zeroLengthArgument() {
        try {
            SimpleMatrix.createRowMatrix(new double[0]);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code createColumnMatrix(double[])}
     * method
     */
    public void testMethod__createColumnMatrix_doubleArray() {
        double[] elements = new double[] {2.1};
        SimpleMatrix matrix = SimpleMatrix.createColumnMatrix(elements);
        
        assertEquals("Row matrix has wrong number of rows",
                elements.length, matrix.getRowCount());
        assertEquals("Row matrix has wrong number of columns",
                1, matrix.getColumnCount());
        assertTrue("Matrix has wrong elements",
                Arrays.equals(elements,
                        matrix.getTranspose().getElements()[0]));
        
        elements = new double[] {2, 3, 5};
        matrix = SimpleMatrix.createColumnMatrix(elements);
        assertEquals("Row matrix has wrong number of rows",
                elements.length, matrix.getRowCount());
        assertEquals("Row matrix has wrong number of columns",
                1, matrix.getColumnCount());
        assertTrue("Matrix has wrong elements",
                Arrays.equals(elements,
                        matrix.getTranspose().getElements()[0]));
    }

    /**
     * Tests the behavior of the {@code createColumnMatrix(double[])} method
     * when its argument is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testMethod__createColumnMatrix__nullArgument() {
        try {
            SimpleMatrix.createColumnMatrix(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the {@code createColumnMatrix(double[])} method
     * when its argument is a zero-length array; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod__createColumnMatrix__zeroLengthArgument() {
        try {
            SimpleMatrix.createColumnMatrix(new double[0]);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code createIdentityMatrix(int)} method
     */
    public void testMethod__createIdentityMatrix_int() {
        SimpleMatrix identity = SimpleMatrix.createIdentityMatrix(1);
        double[][] elements = identity.getElements();
        
        // Test an order-1 matrix
        assertEquals("Matrix has wrong number of rows", 1,
                identity.getRowCount());
        assertEquals("Matrix has wrong number of columns", 1,
                identity.getColumnCount());
        
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                assertEquals("The matrix is incorrect", (i == j) ? 1.0 : 0.0,
                        elements[i][j]);
            }
        }
        assertTrue("The transpose cache was not set",
                identity == identity.getTranspose());
        assertTrue("The inversion cache was not set",
                identity == identity.getInverse());
        
        // Test an order-4 matrix
        identity = SimpleMatrix.createIdentityMatrix(4);
        elements = identity.getElements();
        
        assertEquals("Matrix has wrong number of rows", 4,
                identity.getRowCount());
        assertEquals("Matrix has wrong number of columns", 4,
                identity.getColumnCount());
        
        for (int i = 0; i < elements.length; i++) {
            for (int j = 0; j < elements[0].length; j++) {
                assertEquals("The matrix is incorrect", (i == j) ? 1.0 : 0.0,
                        elements[i][j]);
            }
        }
        assertTrue("The transpose cache was not set",
                identity == identity.getTranspose());
        assertTrue("The inversion cache was not set",
                identity == identity.getInverse());
    }

    /**
     * Tests the behavior of the {@code createIdentityMatrix(int)} method when
     * its argument is non-positive; an {@code IllegalArgumentException} is
     * expected
     */
    public void testMethod__createIdentityMatrix_nonPositiveInt() {
        try {
            SimpleMatrix.createIdentityMatrix(0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            SimpleMatrix.createIdentityMatrix(-1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }
    
    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(SimpleMatrixTests.class);
        
        tests.setName("SimpleMatrix Tests");
        
        return tests;
    }
}
