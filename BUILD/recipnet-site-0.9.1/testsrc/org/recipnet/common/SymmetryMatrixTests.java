/*
 * IUMSC Reciprocal Net Project
 *
 * SymmetryMatrixTests.java
 */

package org.recipnet.common;

import junit.framework.TestCase;

public class SymmetryMatrixTests extends TestCase {

    public SymmetryMatrixTests(String testName) {
        super(testName);
    }

    public void testConstructor_intAA__null() {
        try {
            new SymmetryMatrix((int[][]) null);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_intA__null() {
        try {
            new SymmetryMatrix((int[]) null);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_intAA_intA__null_intA() {
        try {
            new SymmetryMatrix(null, new int[3]);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_intAA_intA__intAA_null() {
        try {
            new SymmetryMatrix(new int[3][3], null);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_intAA_intA_boolean__null_intA_boolean() {
        try {
            new SymmetryMatrix(null, new int[3], false);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
        try {
            new SymmetryMatrix(null, new int[3], true);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_intAA_intA_boolean__intAA_null_boolean() {
        try {
            new SymmetryMatrix(new int[3][3], null, false);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
        try {
            new SymmetryMatrix(new int[3][3], null, true);

            fail("NullPointerException expected");
        } catch (Exception e) {
            assertTrue("Wrong exception: " + e,
                       e instanceof NullPointerException);
        }
    }

    /**
     * Verifies by [JUnit] assertion that its arguments are non-null arrays of
     * three each ints, such that the int[]s are different objects, but have
     * equal elements at all positions
     */
    private void verifyTranslations(int[] v1, int[] v2) {
        assertNotNull("The first vector is null", v1);
        assertNotNull("The second vector is null", v2);
        assertFalse("Vectors are the same object", v1 == v2);
        assertEquals("First vector has wrong number of columns", 3, v1.length);
        assertEquals("Second vector has wrong number of columns", 3, v2.length);

        for (int i = 0; i < 3; i++) {
            assertEquals("Vectors differ at position" + i, v1[i], v2[i]);
        }
    }

    /**
     * Verifies by [JUnit] assertion that its arguments are non-null arrays of
     * three each int[] objects, such that the int[][]s are different objects
     * and their corresponding rows are different objects, but the two have
     * equal elements at all positions
     */
    private void verifyRotations(int[][] m1, int [][] m2) {
        assertNotNull("The first matrix is null", m1);
        assertNotNull("The second matrix is null", m2);
        assertFalse("Matrices are the same object", m1 == m2);
        assertEquals("First matrix has wrong number of rows", 3, m1.length);
        assertEquals("Second matrix has wrong number of rows", 3, m2.length);

        for (int i = 0; i < 3; i++) {
            assertNotNull("Row " + i + " of the first matrix is null", m1[i]);
            assertNotNull("Row " + i + " of the second matrix is null", m2[i]);
            assertEquals("First matrix has wrong number of columns", 3,
                         m1[i].length);
            assertEquals("Second matrix has wrong number of columns", 3,
                         m2[i].length);
            assertFalse("Matrices share row arrays", m1[i] == m2[i]);
            for (int j = 0; j < 3; j++) {
                assertEquals("Matrices differ at row " + i + " column " + j,
                m1[i][j], m2[i][j]);
            }
        }
    }

    public void testConstructor_intAA() {
        int[][] testMatrix = new int[][] {{1, -1, 0}, {1, 1, 1}, {-1, 0, -1}};
        SymmetryMatrix sm = 
                new SymmetryMatrix(testMatrix);
        int[][] matrix = sm.getRotationMatrix();
        int[] vector = sm.getTranslationVector();
        
        verifyRotations(testMatrix, matrix);
        verifyTranslations(new int[3], vector);
    }

    public void testConstructor_intA() {
        int[] testVector = new int[] {5, 7, 11};
        SymmetryMatrix sm = 
                new SymmetryMatrix(testVector);
        int[][] matrix = sm.getRotationMatrix();
        int[] vector = sm.getTranslationVector();
       
        verifyRotations(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}, matrix);
        verifyTranslations(testVector, vector);
    }

    public void testConstructor_intAA_intA() {
        int[][] testMatrix = new int[][] {{1, -1, 0}, {1, 1, 1}, {-1, 0, -1}};
        int[] testVector = new int[] {5, 7, 11};
        SymmetryMatrix sm = 
                new SymmetryMatrix(testMatrix, testVector);
        int[][] matrix = sm.getRotationMatrix();
        int[] vector = sm.getTranslationVector();
       
        verifyRotations(testMatrix, matrix);
        verifyTranslations(testVector, vector);
    }

    public void testConstructor_intAA_intA_boolean__alreadyNormalized() {
        int[][] testMatrix = new int[][] {{1, -1, 0}, {1, 1, 1}, {-1, 0, -1}};
        int[] testVector = new int[] {5, 7, 11};
        SymmetryMatrix sm = 
                new SymmetryMatrix(testMatrix, testVector, true);
        int[][] matrix = sm.getRotationMatrix();
        int[] vector = sm.getTranslationVector();
       
        verifyRotations(testMatrix, matrix);
        verifyTranslations(testVector, vector);

        sm = new SymmetryMatrix(testMatrix, testVector, false);
        matrix = sm.getRotationMatrix();
        vector = sm.getTranslationVector();
       
        verifyRotations(testMatrix, matrix);
        verifyTranslations(testVector, vector);
    }

    public void testConstructor_intAA_intA_boolean__notNormalized() {
        int[][] testMatrix = new int[][] {{1, -1, 0}, {1, 1, 1}, {-1, 0, -1}};
        int[] initVector = new int[] {-1, 3, 17};
        int[] testVector = new int[] {11, 3, 5};
        SymmetryMatrix sm = 
                new SymmetryMatrix(testMatrix, initVector, true);
        int[][] matrix = sm.getRotationMatrix();
        int[] vector = sm.getTranslationVector();
       
        verifyRotations(testMatrix, matrix);
        verifyTranslations(testVector, vector);

        sm = new SymmetryMatrix(testMatrix, initVector, false);
        matrix = sm.getRotationMatrix();
        vector = sm.getTranslationVector();
       
        verifyRotations(testMatrix, matrix);
        verifyTranslations(initVector, vector);
    }

    public void testMethod__getType__twofolds() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, -1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {-1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {0, -1, 0}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {0, -1, 0}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 0, 1}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 0, -1}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, -1, 0}, {0, -1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {0, 1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {-1, 1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {1, -1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.TWOFOLD, sm.getType());
    }

    public void testMethod__getType__mirrors() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {-1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {0, 1, 0}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {0, 1, 0}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, 0, -1}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, 0, 1}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {0, 1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, -1, 0}, {0, -1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {1, -1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {-1, 1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated",
                SymmetryMatrix.Type.REFLECTION, sm.getType());
    }

    public void testMethod__getType__translations() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(new int[3]);
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.TRANSLATION, sm.getType());

        sm = new SymmetryMatrix(
                new int[] {1, 2, 3});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.TRANSLATION, sm.getType());

        sm = new SymmetryMatrix(
                new int[] {5, 7, 11});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.TRANSLATION, sm.getType());

        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.TRANSLATION,
                SymmetryMatrix.IDENTITY.getType());
    }
    
    public void testMethod__getType__inversion() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, -1}},
                new int[] {1, 3, 11});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.INVERSION, sm.getType());

        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.INVERSION,
                SymmetryMatrix.INVERSION.getType());
    }

    public void testMethod__getType__threefolds() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {0, 0, 1}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {-1, 0, 0}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {0, 0, -1}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {1, 0, 0}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {0, 0, 1}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {-1, 0, 0}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {1, -1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {-1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREEFOLD, sm.getType());
     }

    public void testMethod__getType__threebars() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {-1, 0, 0}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {0, 0, -1}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR,  sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {1, 0, 0}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {0, 0, 1}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {-1, 0, 0}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {0, 0, -1}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {1, 0, 0}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {0, 0, 1}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {-1, 1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, -1, 0}, {1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.THREE_BAR, sm.getType());
    }

    public void testMethod__getType__fourfolds() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {0, 1, 0}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {0, 1, 0}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, 0, -1}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {0, 0, 1}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOURFOLD, sm.getType());
    }

    public void testMethod__getType__fourbars() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {0, -1, 0}, {-1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {0, -1, 0}, {1, 0, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 0, 1}, {0, -1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 0, -1}, {0, 1, 0}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.FOUR_BAR, sm.getType());
    }

    public void testMethod__getType__sixfolds() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{1, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.SIXFOLD, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {-1, 1, 0}, {0, 0, 1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.SIXFOLD, sm.getType());
    }

    public void testMethod__getType__sixbars() {
        SymmetryMatrix sm;

        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {-1, 0, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.SIX_BAR, sm.getType());

        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {1, -1, 0}, {0, 0, -1}});
        assertEquals("Wrong operation type indicated", 
                SymmetryMatrix.Type.SIX_BAR, sm.getType());
    }

    public void testMethod__times_SymmetryMatrix() {
        SymmetryMatrix sm1 =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix sm2 =
                new SymmetryMatrix(
                        new int[][] {{1, 1, 0}, {-1, -1, 0}, {1, 1, 1}},
                        new int[] {10, 2, 6});
        SymmetryMatrix result =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {0, 0, 1}, {1, 1, 1}},
                        new int[] {-15, 2, 27}, false);
        SymmetryMatrix copy1 = new SymmetryMatrix(sm1.getRotationMatrix(),
                sm1.getTranslationVector(), false);
        SymmetryMatrix copy2 = new SymmetryMatrix(sm2.getRotationMatrix(),
                sm2.getTranslationVector(), false);

        assertEquals("Matrix product is incorrect", result,
                     copy1.times(copy2));
        assertEquals("Left matrix factor was modified", sm1, copy1);
        assertEquals("Right matrix factor was modified", sm2, copy2);
    }

    public void testMethod__times_SymmetryMatrix_boolean() {
        SymmetryMatrix sm1 =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9}, true);
        SymmetryMatrix sm2 =
                new SymmetryMatrix(
                        new int[][] {{1, 1, 0}, {-1, -1, 0}, {1, 1, 1}},
                        new int[] {10, 2, 6}, true);
        SymmetryMatrix result =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {0, 0, 1}, {1, 1, 1}},
                        new int[] {-15, 2, 27}, true);
        SymmetryMatrix copy1 = new SymmetryMatrix(sm1.getRotationMatrix(),
                sm1.getTranslationVector(), false);
        SymmetryMatrix copy2 = new SymmetryMatrix(sm2.getRotationMatrix(),
                sm2.getTranslationVector(), false);

        assertEquals("Matrix product is incorrect", result,
                     copy1.times(copy2, true));
        assertEquals("Left matrix factor was modified", sm1, copy1);
        assertEquals("Right matrix factor was modified", sm2, copy2);

        result = new SymmetryMatrix(
                new int[][] {{-1, -1, -1}, {0, 0, 1}, {1, 1, 1}},
                new int[] {-15, 2, 27}, false);
        assertEquals("Matrix product is incorrect", result,
                     copy1.times(copy2, false));
        assertEquals("Left matrix factor was modified", sm1, copy1);
        assertEquals("Right matrix factor was modified", sm2, copy2);
    }
    
    public void testMethod__plus_intA_boolean() {
        SymmetryMatrix sm1 = new SymmetryMatrix(
                new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                new int[] {3, 6, 9}, false);
        SymmetryMatrix sm2 = new SymmetryMatrix(
                new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                new int[] {6, -6, 3}, false);
        SymmetryMatrix sm3 = new SymmetryMatrix(
                new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                new int[] {9, 6, 0}, false);
        SymmetryMatrix copy1 = new SymmetryMatrix(sm1.getRotationMatrix(),
                sm1.getTranslationVector(), false);
        SymmetryMatrix result = copy1.plus(new int[] {3, -12, -6}, false);
        
        assertEquals("Vector addition produced the wrong result",
                sm2, result);
        assertEquals("Vector addition modified the original matrix",
                sm1, copy1);
        
        result = copy1.plus(new int[] {-6, 12, 3}, true);
        assertEquals("Vector addition produced the wrong result",
                sm3, result);
        assertEquals("Vector addition modified the original matrix",
                sm1, copy1);
    }
    
    public void testMethod__plus_nullintA_boolean() {
        SymmetryMatrix sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 0, 1}, {0, -1, 0}},
                new int[] {3, 6, 9}, true);
        
        try {
            sm.plus(null, false);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            sm.plus(null, true);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    public void testMethod__inverse() {
        SymmetryMatrix sm;
        
        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, -1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{1, 0, 0}, {1, -1, 0}, {0, 0, 1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[] {1, 2, 3});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, -1}},
                new int[] {1, 3, 11});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {-1, 0, 0}, {0, 0, 1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {1, 0, 0}, {0, 1, 0}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, 0, -1}, {0, -1, 0}, {1, 0, 0}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {-1, 1, 0}, {0, 0, 1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{-1, 1, 0}, {-1, 0, 0}, {0, 0, -1}});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
        sm = new SymmetryMatrix(
                new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}},
                new int[] {0, 6, 3});
        assertTrue("SymmetryMatrix has the wrong inverse",
                sm.times(sm.inverse(), false).equals(SymmetryMatrix.IDENTITY));
    }

    public void testMethod__equals_nullObject() {
        SymmetryMatrix sm =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});

        assertFalse("Matrix reports itself equal to null", sm.equals(null));
    }

    public void testMethod__equals_Object() {
        SymmetryMatrix sm =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});

        assertFalse("Matrix reports itself equal to a plain Object",
                    sm.equals(new Object()));
    }

    public void testMethod__equals_SymmetryMatrix() {
        SymmetryMatrix sm =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix equal =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix diff1 =
                new SymmetryMatrix(
                        new int[][] {{-1, 1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix diff2 =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 10});
        SymmetryMatrix diff3 =
                new SymmetryMatrix(
                        new int[3][3], new int[3]);
        SymmetryMatrix diff4 =
                new SymmetryMatrix(
                        new int[][] {{-1, 0, 1}, {1, 1, 1}, {-1, -1, -1}},
                        new int[] {3, 6, 9});

        assertTrue("Matrix reports itself inequal to a matching matrix",
                    sm.equals(equal));
        assertFalse("Matrix reports itself equal to a differing matrix",
                    sm.equals(diff1));
        assertFalse("Matrix reports itself equal to a differing matrix",
                    sm.equals(diff2));
        assertFalse("Matrix reports itself equal to a differing matrix",
                    sm.equals(diff3));
        assertFalse("Matrix reports itself equal to a differing matrix",
                    sm.equals(diff4));
    }

    public void testMethod__hashCode() {
        SymmetryMatrix sm =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix equal =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix diff1 =
                new SymmetryMatrix(
                        new int[][] {{-1, 1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        SymmetryMatrix diff2 =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 10});
        SymmetryMatrix diff3 =
                new SymmetryMatrix(
                        new int[3][3], new int[3]);
        SymmetryMatrix diff4 =
                new SymmetryMatrix(
                        new int[][] {{-1, 0, 1}, {1, 1, 1}, {-1, -1, -1}},
                        new int[] {3, 6, 9});

        assertEquals("Equal matrices have different hash codes",
                     sm.hashCode(), equal.hashCode());

        /*
         * The remaining assertions test a property of OperatorMatrix that is
         * not in general required by the contract of Object.hashCode():  all
         * OperatorMatrices constructed with normalization turned on have
         * distinct hash codes unless they are equal to each other.
         */
        assertFalse("Inequal matrices erroneously have the same hash code",
                     sm.hashCode() == diff1.hashCode());
        assertFalse("Inequal matrices erroneously have the same hash code",
                     sm.hashCode() == diff2.hashCode());
        assertFalse("Inequal matrices erroneously have the same hash code",
                     sm.hashCode() == diff3.hashCode());
        assertFalse("Inequal matrices erroneously have the same hash code",
                     sm.hashCode() == diff4.hashCode());
    }

    public void testMethod__clone() {
        SymmetryMatrix sm =
                new SymmetryMatrix(
                        new int[][] {{-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}},
                        new int[] {3, 6, 9});
        Object c = sm.clone();

        assertNotNull("The clone is null", c);
        assertTrue("The clone is not an OperatorMatrix",
                   c instanceof SymmetryMatrix);
        assertEquals("The clone is not equal to the original", sm, c);
        assertFalse("The clone is the same object as the original", sm == c);
    }
}

