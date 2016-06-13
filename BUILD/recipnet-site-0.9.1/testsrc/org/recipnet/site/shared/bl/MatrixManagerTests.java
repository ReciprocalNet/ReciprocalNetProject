/*
 * IUMSC Reciprocal Net Project
 *
 * MatrixManagerTests.java
 */

package org.recipnet.site.shared.bl;

import java.util.Arrays;

import org.recipnet.common.SymmetryMatrix;

public class MatrixManagerTests extends MatrixManagerTestBase {

    public MatrixManagerTests(String testName) {
        super(testName);
    }

    /**
     * Returns a SpaceGroupSymbolBL.MatrixManager instance to test
     */
    @Override
    protected SpaceGroupSymbolBL.MatrixManager getTestSubject() {
        return new SpaceGroupSymbolBL.MatrixManager();
    }

    @Override
    protected int getMaximumOrder() {
        return 4;
    }

    @Override
    protected char[] getValidGlides() {
        return new char[] {'m', 'a', 'b', 'c', 'n', 'd'};
    }

    public void testMethod_createRotationMatrix_delegation() {
        // For parent class only

        final int[][] relements = new int[3][3];
        final int[] telements = new int[3];
        final int[] args = new int[3];
        final boolean[] methodFlags = new boolean[2];
        SymmetryMatrix result;

        SpaceGroupSymbolBL.MatrixManager mm = new SpaceGroupSymbolBL.MatrixManager() {
            @Override
            int[][] createRotationElements(int order, int direction) {
                assertFalse("createRotationElements invoked twice",
                            methodFlags[0]);
                methodFlags[0] = true;
                assertEquals(
                        "Argument 1 of 2 to createRotationElements is wrong",
                        args[0], order);
                assertEquals(
                        "Argument 2 of 2 to createRotationElements is wrong",
                        args[1], direction);

                int[][] rval = new int[3][];

                for (int i = 0; i < 3; i++) {
                    rval[i] = relements[i].clone();
                }

                return rval;
            }

            @Override
            int[] createTranslationElements(int direction, char type) {
                assertFalse("createTranslationElements invoked twice",
                            methodFlags[1]);
                methodFlags[1] = true;
                assertEquals(
                        "Argument 1 of 2 to createTranslationElements is wrong",
                        args[1], direction);
                assertEquals(
                        "Argument 2 of 2 to createTranslationElements is wrong",
                        args[0], type - '0');
                return telements.clone();
            }
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                relements[i][j] = ((i + j + 2) % 3) - 1;
            }
            telements[i] = 4 * i + 1;
        }

        args[0] = 17;
        args[1] = -42;
        args[2] = 0;
        Arrays.fill(methodFlags, false);

        result = mm.createRotationMatrix(args[0], args[1], args[2], false);
        assertTrue("createRotationElements not invoked", methodFlags[0]);
        assertFalse("createTranslationElements erroneously invoked",
                    methodFlags[1]);
        assertEquals("OperatorMatrix result is incorrect",
                     new SymmetryMatrix(relements), result);

        Arrays.fill(methodFlags, false);

        result = mm.createRotationMatrix(args[0], args[1], args[2], true);
        assertTrue("createRotationElements not invoked", methodFlags[0]);
        assertFalse("createTranslationElements erroneously invoked",
                    methodFlags[1]);
        assertEquals("OperatorMatrix result is incorrect",
                     new SymmetryMatrix(relements).times(
                             SymmetryMatrix.INVERSION, true),
                     result);

        args[1] = 0;
        args[2] = 1;
        Arrays.fill(methodFlags, false);

        result = mm.createRotationMatrix(args[0], args[1], args[2], false);
        assertTrue("createRotationElements not invoked", methodFlags[0]);
        assertTrue("createTranslationElements not invoked", methodFlags[1]);
        assertEquals("OperatorMatrix result is incorrect",
                     new SymmetryMatrix(relements, telements),
                     result);
    }

    public void testMethod_createReflectionMatrix__delegation() {
        // For parent class only

        final int[][] relements = new int[3][3];
        final int[] telements = new int[3];
        final int[] args = new int[2];
        final boolean[] methodFlags = new boolean[2];
        SymmetryMatrix result;
        char[] mirrors = new char[] {'a', 'b', 'c', 'n', 'd'};

        SpaceGroupSymbolBL.MatrixManager mm = new SpaceGroupSymbolBL.MatrixManager() {
            @Override
            int[][] createReflectionElements(int direction) {
                assertFalse("createReflectionElements invoked twice",
                            methodFlags[0]);
                methodFlags[0] = true;
                assertEquals(
                        "Argument to createReflectionElements is wrong",
                        args[0], direction);
                return relements;
            }

            @Override
            int[] createTranslationElements(int direction, char type) {
                assertFalse("createTranslationElements invoked twice",
                            methodFlags[1]);
                methodFlags[1] = true;
                assertEquals(
                        "Argument 1 of 2 to createTranslationElements is wrong",
                        args[0], direction);
                assertEquals(
                        "Argument 2 of 2 to createTranslationElements is wrong",
                        args[1], type);
                return telements;
            }
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                relements[i][j] = ((i + j + 2) % 3) - 1;
            }
            telements[i] = 0;
        }

        args[0] = 1;
        args[1] = 'm';
        Arrays.fill(methodFlags, false);
        result = mm.createReflectionMatrix(args[0], (char) args[1]);
        
        assertTrue("createReflectionElements not invoked", methodFlags[0]);
        assertEquals("OperatorMatrix result is incorrect",
                     new SymmetryMatrix(relements), result);

        for (int i = 0; i < 3; i++) {
            telements[i] = 4 * i + 1;
        }

        for (int i = 0; i < mirrors.length; i++) {
            args[0] = i;
            args[1] = mirrors[(i + 1) % mirrors.length];
            Arrays.fill(methodFlags, false);
            result = mm.createReflectionMatrix(args[0], (char) args[1]);
            assertTrue("createReflectionElements not invoked", methodFlags[0]);
            assertTrue("createTranslationElements not invoked", methodFlags[1]);
            assertEquals("OperatorMatrix result is incorrect",
                         new SymmetryMatrix(relements, telements), result);
        }
    }

    private void cycleMatrix(int[][] matrix) {
        int[] t = matrix[0];

        for (int i = 0; i < 2; i++) {
            matrix[i] = matrix[i + 1];
        }
        matrix[2] = t;

        for (int i = 0; i < 3; i++) {
            int ti = matrix[i][0];

            for (int j = 0; j < 2; j++) {
                matrix[i][j] = matrix[i][j + 1];
            }
            matrix[i][2] = ti;
        }
    }

    public void testMethod_determineTwofoldDirection__OK() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 6; dir++) {
            if (dir == SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                continue;
            }

            int[][] matrix;

            matrix = mm.createReflectionElements(dir);
            for (int i = 0; i < 3; i++) {
                assertEquals("Wrong direction determined;", dir,
                             mm.determineTwofoldDirection(matrix, false));
                if (dir < 4) {
                    break;
                }
                cycleMatrix(matrix);
            }

            matrix = mm.createRotationElements(2, dir);
            for (int i = 0; i < 3; i++) {
                assertEquals("Wrong direction determined;", dir,
                             mm.determineTwofoldDirection(matrix, true));
                if (dir < 4) {
                    break;
                }
                cycleMatrix(matrix);
            }
        }
    }

    public void testMethod_determineOperator__reflection_delegation() {
        // base class only
        final int[][] rmat = getTestSubject().createReflectionElements(1);
        final int[] tvec = new int[] {5, 7, 9};
        final int dir = 21;
        final boolean[] methodFlags = new boolean[2];
        final char type = 'w';
        SymmetryMatrix matrix = new SymmetryMatrix(rmat, tvec);
        final int[] tvec2 = matrix.times(matrix, false).getTranslationVector();

        SpaceGroupSymbolBL.MatrixManager mm = new SpaceGroupSymbolBL.MatrixManager() {
            @Override
            int determineTwofoldDirection(int[][] rotation, boolean proper) {
                assertFalse("determineTwofoldDirection invoked twice",
                            methodFlags[0]);
                methodFlags[0] = true;
                for (int i = 0; i < 3; i++) {
                    assertTrue("Wrong rotation matrix delegated",
                               Arrays.equals(rmat[i], rotation[i]));
                }
                assertFalse("on delegation, operation is flagged proper",
                            proper);
                return dir;
            }

            @Override
            char determineMirrorType(int d, int[] trans) {
                assertFalse("determineMirrorType invoked twice",
                            methodFlags[1]);
                methodFlags[1] = true;
                assertEquals("Wrong direction specified", dir, d);
                assertTrue("Wrong rotation matrix delegated",
                           Arrays.equals(tvec2, trans));
                return type;
            }
        };

        SpaceGroupSymbolBL.Operator op = mm.determineOperator(matrix);

        assertTrue("determineTwofoldDirection not invoked", methodFlags[0]);
        assertTrue("determineMirrorType not invoked", methodFlags[1]);
        assertEquals("Operator not determined correctly",
                     new SpaceGroupSymbolBL.Operator(0, false, 0, type, dir), op);
        assertEquals("Matrix not assigned to operator", matrix, op.getMatrix());
    }

    public void testMethod_determineOperator__twofold_delegation() {
        // base class only
        final int[][] rmat = getTestSubject().createRotationElements(2, 1);
        final int[] tvec = new int[] {5, 7, 9};
        final int dir = 21;
        final boolean[] methodFlags = new boolean[1];
        SymmetryMatrix matrix = new SymmetryMatrix(rmat, tvec);

        SpaceGroupSymbolBL.MatrixManager mm = new SpaceGroupSymbolBL.MatrixManager() {
            @Override
            int determineTwofoldDirection(int[][] rotation, boolean proper) {
                assertFalse("determineTwofoldDirection invoked twice",
                            methodFlags[0]);
                methodFlags[0] = true;
                for (int i = 0; i < 3; i++) {
                    assertTrue("Wrong rotation matrix delegated",
                               Arrays.equals(rmat[i], rotation[i]));
                }
                assertTrue("on delegation, operation is not flagged proper",
                            proper);
                return dir;
            }
        };

        SpaceGroupSymbolBL.Operator op = mm.determineOperator(matrix);

        assertTrue("determineTwofoldDirection not invoked", methodFlags[0]);
        assertEquals("Operator not determined correctly",
                     new SpaceGroupSymbolBL.Operator(2, false, 1, ' ', dir), op);
        assertEquals("Matrix not assigned to operator", matrix, op.getMatrix());
    }

    public void testMethod_determineOperator__threefold() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        int direction = SpaceGroupSymbolBL.DIRECTION_BODY_DIAG;
        SymmetryMatrix matrix;
        SpaceGroupSymbolBL.Operator op;

        for (int screw = 0; screw < 3; screw++) {
            try {
                matrix = mm.createRotationMatrix(3, direction , screw, false);
                op = mm.determineOperator(matrix);
                if (screw > 0) {
                    fail("Expected an IllegalArgumentException");
                } else {
                    assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined",
                                 new SpaceGroupSymbolBL.Operator(3, false,
                                         screw, ' ', direction),
                                 op);
                    assertEquals("Operator not assigned a correct matrix",
                                 matrix, op.getMatrix());
                }
            } catch (Exception e) {
                if (screw == 0) {
                    fail("Caught unexpected exception: " + e);
                } else {
                    assertTrue("Expected an IllegalArgumentException; caught "
                               + e, e instanceof IllegalArgumentException);
                }
            }
        }
        matrix = mm.createRotationMatrix(3, direction , 0, true);
        op = mm.determineOperator(matrix);
        assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined",
                     new SpaceGroupSymbolBL.Operator(3, true, 0, ' ', direction),
                     op);
        assertEquals("Operator not assigned a correct matrix",
                     matrix, op.getMatrix());
    }

    public void testMethod_determineOperator__fourfold() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        SymmetryMatrix matrix;
        SpaceGroupSymbolBL.Operator op;

        for (int direction = 0; direction < 3; direction++) {
            for (int screw = 0; screw < 4; screw++) {
                matrix = mm.createRotationMatrix(4, direction, screw, false);
                op = mm.determineOperator(matrix);
                assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined",
                             new SpaceGroupSymbolBL.Operator(4, false, screw, ' ', direction),
                             op);
                assertEquals("Operator not assigned a correct matrix",
                             matrix, op.getMatrix());
            }
            matrix = mm.createRotationMatrix(4, direction , 0, true);
            op = mm.determineOperator(matrix);
            assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined",
                         new SpaceGroupSymbolBL.Operator(4, true, 0, ' ', direction),
                         op);
            assertEquals("Operator not assigned a correct matrix",
                         matrix, op.getMatrix());
        }
    }

    public void testMethod_determineMirrorType__a() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 6; dir++) {
            int[] vector = mm.createTranslationElements(dir, 'a');

            for (int i = 0; i < vector.length; i++) {
                vector[i] *= 2;
            }

            assertEquals("Wrong mirror type determined", 'a',
                         mm.determineMirrorType(dir, vector));
        }
    }

    public void testMethod_determineMirrorType__b() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 6; dir++) {
            int[] vector = mm.createTranslationElements(dir, 'b');

            for (int i = 0; i < vector.length; i++) {
                vector[i] *= 2;
            }

            assertEquals("Wrong mirror type determined", 'b',
                         mm.determineMirrorType(dir, vector));
        }
    }

    public void testMethod_determineMirrorType__c() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 6; dir++) {
            int[] vector = mm.createTranslationElements(dir, 'c');

            for (int i = 0; i < vector.length; i++) {
                vector[i] *= 2;
            }

            assertEquals("Wrong mirror type determined", 'c',
                         mm.determineMirrorType(dir, vector));
        }
    }

    public void testMethod_determineMirrorType__n() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 5; dir++) {
            if (dir == SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                continue;
            }
            int[] vector = mm.createTranslationElements(dir, 'n');

            for (int i = 0; i < vector.length; i++) {
                vector[i] *= 2;
            }

            assertEquals("Wrong mirror type determined", 'n',
                         mm.determineMirrorType(dir, vector));
        }
    }

    private void assertDiamondVector(int[] vector, int dir, int index) {
        if (index > 2) {
            assertEquals("Wrong mirror type determined for direction " + dir
                         + ", vector " + vector[0] + " " + vector[1]
                         + " " + vector[2] + ";", 'd',
                         getTestSubject().determineMirrorType(dir, vector));
        } else if (dir == index) {
            vector[index] = 0;
            assertDiamondVector(vector, dir, index + 1);
        } else {
            for (int t = 6; t < 24; t += 12) {
                vector[index] = t;
                assertDiamondVector(vector, dir, index + 1);
            }
        }
    }

    public void testMethod_determineMirrorType__d() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 5; dir++) {
            if (dir == SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                continue;
            }

            int[] vector = mm.createTranslationElements(dir, 'd');

            for (int i = 0; i < vector.length; i++) {
                vector[i] *= 2;
            }

            assertEquals("Wrong mirror type determined", 'd',
                         mm.determineMirrorType(dir, vector));

            assertDiamondVector(vector, dir, 0);
        }
    }

    public void testMethod_createRotationElements__badArgCombination() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = 0; i < 5; i++) {
            if (i != SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                try {
                    mm.createRotationElements(3, i);
                    fail("Expected an IllegalArgumentException");
                } catch (Exception e) {
                    assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                               + e.getClass(),
                               e instanceof IllegalArgumentException);
                }
            }
            if (i > 2) {
                try {
                    mm.createRotationElements(4, i);
                    fail("Expected an IllegalArgumentException");
                } catch (Exception e) {
                    assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                               + e.getClass(),
                               e instanceof IllegalArgumentException);
                }
            }
        }
    }

    public void testMethod_createRotationElements__delegation() {
        // For parent class only

        final int[][] relements =
                new int[][] {{1, 2, 3}, {6, 5, 4}, {-8, -7, -9}};
        final int dir = 2;
        final boolean[] methodFlags = new boolean[1];
        int[][] result;

        SpaceGroupSymbolBL.MatrixManager mm = new SpaceGroupSymbolBL.MatrixManager() {
            @Override
            int[][] createReflectionElements(int direction) {
                assertFalse("createReflectionElements invoked twice",
                            methodFlags[0]);
                methodFlags[0] = true;
                assertEquals(
                        "Argument to createReflectionElements is wrong",
                        dir, direction);

                int[][] rval = new int[3][];

                for (int i = 0; i < 3; i++) {
                    rval[i] = relements[i].clone();
                }

                return rval;
            }
        };

        methodFlags[0] = false;
        result = mm.createRotationElements(2, dir);
        assertTrue("createReflectionElements not invoked", methodFlags[0]);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals("Returned element [" + i + "][" + j
                                 + "] is incorrect",
                             -relements[i][j],
                             result[i][j]);
            }
        }
    }

    public void testMethod_createRotationElements__badOrder() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = -2; i < 8; i++) {
            if ((i > 0) && (i < 5)) {
                continue;
            }
            try {
                mm.createRotationElements(i, SpaceGroupSymbolBL.DIRECTION_C);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }
}

