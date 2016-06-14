/*
 * Reciprocal Net Project
 *
 * SymmetryContextTests.java
 *
 * Dec 23, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.recipnet.common.SymmetryMatrix;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code SymmetryContext} class
 * 
 * @author jobollin
 * @version 1.0
 */
public class SymmetryContextTests extends TestCase {

    /*
     * Note: some error behaviors are not well (or at all) tested, especially
     * those of getSymmetryMatrix() and getSymmetryCode()
     */
    
    /**
     * Initializes a new {@code SymmetryContextTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public SymmetryContextTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the constructor with various lists of
     * non-redundant symmetry matrices; the resulting contexts'
     * {@code getBaseOperations()} and {@code getOperationCount()} methods
     * should accurately reflect the matrices with which the contexts were
     * constructed
     */
    public void testConstructor__CollectionSymmetryMatrix() {
        int[] iTranslation = new int[] {6, 6, 6};
        
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY);
        performConstructorTest("1_475",
                SymmetryMatrix.IDENTITY.plus(new int[] {12, -24, 0}, false));
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY,
                SymmetryMatrix.INVERSION);
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 1}}));
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}})
                );
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                SymmetryMatrix.INVERSION,
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}})
                );
        performConstructorTest("1_555", SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(iTranslation),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}},
                        iTranslation),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}},
                        iTranslation),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}},
                        iTranslation)
                );
        performConstructorTest("5_475",
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                SymmetryMatrix.INVERSION,
                SymmetryMatrix.IDENTITY.plus(new int[] {12, -24, 0}, false),
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}})
                );
    }

    /**
     * Performs the details of the constructor test, verifying that a
     * {@SymmetryContext} initialized with the specified matrices in fact uses
     * those matrices in the specified order as its base operations
     * 
     * @param  identityCode the {@code String} value of the expected symmetry
     *         code for the identity operation
     * @param  matrices the {@code SymmetryMatrix} objects with which to
     *         initialize the context
     */
    private void performConstructorTest(String identityCode,
            SymmetryMatrix... matrices) {
        List<SymmetryMatrix> matrixList = Arrays.asList(matrices);
        SymmetryContext context = new SymmetryContext(matrixList);
        List<SymmetryMatrix> operations = context.getBaseOperations();
        
        assertEquals("Wrong number of operations", matrices.length,
                context.getOperationCount());
        assertEquals("Wrong base operations", matrixList, operations);
        assertEquals("Wrong identity code", identityCode,
                context.getIdentityCode().toString());
    }
    
    /**
     * Tests the behavior of the constructor when its argument is {@code null};
     * the constructor is expected to complete normally, initializing the
     * context with the identity as its sole operation
     */
    public void testConstructor__nullCollection() {
        SymmetryContext context = new SymmetryContext(null);
        List<SymmetryMatrix> operations = context.getBaseOperations();
        
        assertEquals("Wrong number of operations", 1,
                context.getOperationCount());
        assertEquals("Wrong number of base operations", 1, operations.size());
        assertTrue("Wrong base operation",
                operations.contains(SymmetryMatrix.IDENTITY));
        assertEquals("Wrong identity code", "1_555",
                context.getIdentityCode().toString());
    }
    
    /**
     * Tests the constructor's behavior when it must autogenerate an identity
     * operation
     */
    public void testConstructor__Collection__autoIdentity() {
        int[] iTranslation = new int[] {6, 6, 6};
        
        performAutoIdentityTest();
        performAutoIdentityTest(SymmetryMatrix.INVERSION);
        performAutoIdentityTest(
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 1}}));
        performAutoIdentityTest(
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}})
                );
        performAutoIdentityTest(
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                SymmetryMatrix.INVERSION,
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}})
                );
        performAutoIdentityTest(
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(iTranslation),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}},
                        iTranslation),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}},
                        iTranslation),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}},
                        iTranslation)
                );
        performAutoIdentityTest(
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                SymmetryMatrix.INVERSION,
                new SymmetryMatrix(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}})
                );
    }
    
    /**
     * Performs the details of the constructor test for automatic identity
     * generation, verifying that a {@SymmetryContext} initialized with the
     * specified matrices in fact uses those matrices in the specified order,
     * plus the identity matrix, as its base operations
     * 
     * @param  matrices the {@code SymmetryMatrix} objects with which to
     *         initialize the context
     */
    private void performAutoIdentityTest(SymmetryMatrix... matrices) {
        List<SymmetryMatrix> matrixList
                = new ArrayList<SymmetryMatrix>(Arrays.asList(matrices));
        
        matrixList.add(SymmetryMatrix.IDENTITY);
        
        SymmetryContext context = new SymmetryContext(matrixList);
        List<SymmetryMatrix> operations = context.getBaseOperations();
        
        assertEquals("Wrong number of operations", matrixList.size(),
                context.getOperationCount());
        assertEquals("Wrong base operations", matrixList, operations);
        assertEquals("Wrong identity code", matrixList.size() + "_555",
                context.getIdentityCode().toString());
    }
    
    /**
     * Tests the constructor's behavior when there is a redundant symmetry
     * matrix among its matrices
     */
    public void testConstructor__Collection_redundantMatrix() {
        List<SymmetryMatrix> matrixList = new ArrayList<SymmetryMatrix>();
        
        Collections.addAll(matrixList, SymmetryMatrix.INVERSION,
                SymmetryMatrix.INVERSION);
        try {
            new SymmetryContext(matrixList);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        matrixList.clear();
        Collections.addAll(matrixList, SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[] {12, 0, 0}));
        try {
            new SymmetryContext(matrixList);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        matrixList.clear();
        Collections.addAll(matrixList, SymmetryMatrix.IDENTITY,
                new SymmetryMatrix(new int[][] {{-1, 1, 0}, {1, 0, 0}, {0, 0, 1}},
                        new int[] {3, 6, 9}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, -1, 0}, {0, 0, 1}},
                        new int[] {6, 9, 0}),
                        new SymmetryMatrix(new int[][] {{-1, 1, 0}, {1, 0, 0}, {0, 0, 1}},
                                new int[] {3, 6, 21}, false)
                );
        try {
            new SymmetryContext(matrixList);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code getSymmetryCode(SymmetryMatrix)}
     * method to verify that it returns correct symmetry codes an a variety of
     * specific contexts
     */
    public void testMethod__getSymmetryCode_SymmetryMatrix() {
        performGetSymmetryCodeTests(new SymmetryContext(null));
        performGetSymmetryCodeTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        SymmetryMatrix.INVERSION
                })));
        performGetSymmetryCodeTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        new SymmetryMatrix(new int[][] {{-1, 1, 0}, {1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 4}),
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {-1, 1, 0}, {0, 0, 1}},
                                new int[] {0, 0, 8})
                })));
        performGetSymmetryCodeTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        SymmetryMatrix.INVERSION,
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                })));
    }
    
    /**
     * Performs an exhaustive test of the specified symmetry context, verifying
     * that it provides the correct symmetry code for every symmetry matrix
     * for which it should be able to provide a code
     * 
     * @param  context the {@code SymmetryContext} to test
     */
    private void performGetSymmetryCodeTests(SymmetryContext context) {
        List<SymmetryMatrix> matrices = context.getBaseOperations();
        
        for (int i = 0; i < matrices.size(); i++) {
            for (int dx = -4; dx < 5; dx++) {
                for (int dy = -4; dy < 5; dy++) {
                    for (int dz = -4; dz < 5; dz++) {
                        SymmetryCode expectedCode = new SymmetryCode(
                                i + 1, new int[] {dx + 5, dy + 5, dz + 5});
                        SymmetryMatrix matrix = matrices.get(i).plus(
                                new int[] {12 * dx, 12 * dy, 12 * dz}, false);
                        
                        assertEquals("Wrong symmetry code returned",
                                expectedCode, context.getSymmetryCode(matrix));
                    }
                }
            }
        }
    }

    /**
     * Tests the behavior of the {@code getSymmetryCode(SymmetryMatrix)}
     * method when its argument is {@code null}; a {@code NullPointerException}
     * is expected
     */
    public void testMethod__getSymmetryCode_nullSymmetryMatrix() {
        SymmetryContext context = new SymmetryContext(null);
        
        try {
            context.getSymmetryCode(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code getSymmetryMatrix(SymmetryCode)}
     * method to verify that it returns matrices correctly corresponding to
     * the specified symmetry codes
     */
    public void testMethod__getSymmetryMatrix_SymmetryCode() {
        performGetSymmetryMatrixTests(new SymmetryContext(null));
        performGetSymmetryMatrixTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        SymmetryMatrix.INVERSION
                })));
        performGetSymmetryMatrixTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        new SymmetryMatrix(new int[][] {{-1, 1, 0}, {1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 4}),
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {-1, 1, 0}, {0, 0, 1}},
                                new int[] {0, 0, 8})
                })));
        performGetSymmetryMatrixTests(
                new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                        new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}},
                                new int[] {0, 0, 0}),
                        SymmetryMatrix.INVERSION,
                        new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                        new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}},
                                new int[] {0, 0, 0}),
                })));
    }

    /**
     * Performs an exhaustive test of the specified symmetry context, verifying
     * that it provides the correct symmetry matrix for every symmetry code
     * for which it should be able to provide one
     * 
     * @param  context the {@code SymmetryContext} to test
     */
    private void performGetSymmetryMatrixTests(SymmetryContext context) {
        List<SymmetryMatrix> matrices = context.getBaseOperations();
        
        for (int i = 0; i < matrices.size(); i++) {
            for (int dx = -4; dx < 5; dx++) {
                for (int dy = -4; dy < 5; dy++) {
                    for (int dz = -4; dz < 5; dz++) {
                        SymmetryCode code = new SymmetryCode(
                                i + 1, new int[] {dx + 5, dy + 5, dz + 5});
                        SymmetryMatrix expectedMatrix = matrices.get(i).plus(
                                new int[] {12 * dx, 12 * dy, 12 * dz}, false);
                        
                        assertEquals("Wrong symmetry matrix returned",
                                expectedMatrix, context.getSymmetryMatrix(code));
                    }
                }
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code getSymmetryMatrix(SymmetryCode)}
     * method when its argument is {@code null}; a {@code NullPointerExcpetion}
     * is expected
     */
    public void testMethod__getSymmetryMatrix_nullSymmetryCode() {
        SymmetryContext context = new SymmetryContext(null);
        
        try {
            context.getSymmetryMatrix(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code hasOperation(SymmetryMatrix)}
     * method
     */
    public void testMethod__HasOperation_SymmetryMatrix() {
        SymmetryContext context;
        
        context = new SymmetryContext(null);
        performPositiveHasOperationTest(context);
        performNegativeHasOperationTest(context, SymmetryMatrix.INVERSION,
                SymmetryMatrix.IDENTITY.plus(new int[] {6, 0, 0}, false),
                SymmetryMatrix.IDENTITY.plus(new int[] {0, 11, 0}, false),
                SymmetryMatrix.IDENTITY.plus(new int[] {0, 0, 1}, false));
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                SymmetryMatrix.INVERSION
        }));
        performPositiveHasOperationTest(context);
        performNegativeHasOperationTest(context,
                SymmetryMatrix.IDENTITY.plus(new int[] {6, 0, 0}, false),
                SymmetryMatrix.IDENTITY.plus(new int[] {0, -11, 0}, false),
                SymmetryMatrix.IDENTITY.plus(new int[] {0, 0, 1}, false),
                SymmetryMatrix.INVERSION.plus(new int[] {-6, 0, 0}, false),
                SymmetryMatrix.INVERSION.plus(new int[] {0, 11, 0}, false),
                SymmetryMatrix.INVERSION.plus(new int[] {0, 0, -1}, false));
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                SymmetryMatrix.INVERSION,
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
        }));
        performPositiveHasOperationTest(context);
        performNegativeHasOperationTest(context,
                new SymmetryMatrix(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                SymmetryMatrix.IDENTITY.plus(new int[] {16, 12, -3}, false),
                SymmetryMatrix.INVERSION.plus(new int[] {120, 0, -1}, false));
    }
    
    /**
     * Performs a rather exhaustive test of the specified symmetry context to
     * verify that it reports itself containing a wide variety of translated
     * versions of each of its base operations, including some for which no
     * symmetry code can be provided
     * 
     * @param  context the {@code SymmetryContext} to test
     */
    private void performPositiveHasOperationTest(SymmetryContext context) {
        for (SymmetryMatrix base: context.getBaseOperations()) {
            for (int dx = -6 * 12; dx <= 6 * 12; dx += 2 * 12) {
                for (int dy = -6 * 12; dy <= 6 * 12; dy += 2 * 12) {
                    for (int dz = -6 * 12; dz <= 6 * 12; dz += 2 * 12) {
                        SymmetryMatrix matrix
                                = base.plus(new int[] {dx, dy, dz}, false);
                        
                        assertTrue("Context does not have an expected operation",
                                context.hasOperation(matrix));
                    }
                }
            }
        }
    }

    /**
     * Performs the details of a test to verify that the specified symmetry
     * context does not report having the specified symmetry matrices among its
     * operations
     * 
     * @param  context the {@code SymmetryContext} to test
     * @param  matrices the {@code SymmetryMatrix} objects to test against the
     *         context's {@code hasOperation(SymmetryMatrix)} method
     */
    private void performNegativeHasOperationTest(SymmetryContext context,
            SymmetryMatrix... matrices) {
        for (SymmetryMatrix matrix : matrices) {
            assertFalse("Context contains a wrong operation",
                    context.hasOperation(matrix));
        }
    }
    
    /**
     * Tests the behavior of the {@code isComplete()} method
     */
    public void testMethod__isComplete() {
        SymmetryContext context;
        
        context = new SymmetryContext(null);
        assertTrue("Context unexpectedly incomplete", context.isComplete());
        
        context = new SymmetryContext(
                Collections.singletonList(SymmetryMatrix.INVERSION));
        assertTrue("Context unexpectedly incomplete", context.isComplete());
        
        context = new SymmetryContext(Collections.singletonList(
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}})));
        assertTrue("Context unexpectedly incomplete", context.isComplete());
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}})
                }));
        assertTrue("Context unexpectedly incomplete", context.isComplete());
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                SymmetryMatrix.INVERSION,
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}})
                }));
        assertTrue("Context unexpectedly incomplete", context.isComplete());
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}})
                }));
        assertFalse("Context unexpectedly complete", context.isComplete());
        
        context = new SymmetryContext(Arrays.asList(new SymmetryMatrix[] {
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}),
                new SymmetryMatrix(new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][] {{1, 0, 0}, {0, 1, 0}, {0, 0, -1}}),
                new SymmetryMatrix(new int[][] {{0, 1, 0}, {-1, 0, 0}, {0, 0, -1}})
                }));
        assertFalse("Context unexpectedly complete", context.isComplete());
    }

    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(SymmetryContextTests.class);
        
        tests.setName("SymmetryContext Tests");
        
        return tests;
    }
}
