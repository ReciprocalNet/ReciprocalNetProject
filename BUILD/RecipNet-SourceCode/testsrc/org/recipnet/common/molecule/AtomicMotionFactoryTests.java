/*
 * Reciprocal Net Project
 *
 * AtomicMotionFactoryTests.java
 *
 * Dec 20, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.Arrays;

import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} exercising the behavior of the
 * {@code AtomicMotionFactory} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomicMotionFactoryTests extends TestCase {

    /**
     * Initializes an {@code AtomicMotionFactoryTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public AtomicMotionFactoryTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the constructor when its argument is {@code null};
     * a {@code NullPointerException} is expected
     */
    public void testConstructor__nullCoordinateSystem() {
        try {
            new AtomicMotionFactory(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code motionForIsotropicU(double)}
     * method; specifically, tests that it returns a motion flagged isotropic,
     * having the specified isotropic U, and having anisotropic motion
     * coefficients that correctly describe the isotropic motion
     */
    public void testMethod__motionForIsotropicU_double() {
        performIsotropicUTest(new CoordinateSystem(8, 8, 8, 0, 0, 0), 13);
        performIsotropicUTest(new CoordinateSystem(8, 13, 17, 0.0, 0, 0), 13);        
        performIsotropicUTest(
                new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0), 13);        
        performIsotropicUTest(
                new CoordinateSystem(8, 8, 8, -.05, -.05, -.05), 13);
        performIsotropicUTest(
                new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3), 13);
    }
    
    /**
     * Performs the detailed testing of the {@code AtomicMotionFactory}'s
     * {@code motionForIsotropicU(double)} method for the specified unit cell
     * shape
     * 
     * @param  unitCell a {@code CoordinateSystem} describing the unit cell
     *         shape to test
     * @param  u the isotropic parameter to use in the test
     */
    private void performIsotropicUTest(CoordinateSystem unitCell, double u) {
        CoordinateSystem reciprocalCell = unitCell.getReciprocal();
        Vector[] recipVectors = reciprocalCell.getVectors();
        CoordinateSystem unitRecipCell;
        AtomicMotionFactory factory;
        AtomicMotion isoU;
        double[][] anisoU;

        factory = new AtomicMotionFactory(unitCell);        
        isoU = factory.motionForIsotropicU(u);
        assertEquals("Isotropic U not stored correctly", u,
                isoU.getIsotropicU(), 1e-10);
        assertFalse("Motion not marked isotropic", isoU.isAnisotropic());
        
        anisoU = isoU.getAnisotropicU();
        for (int i = 0; i < recipVectors.length; i++) {
            recipVectors[i] = recipVectors[i].normalize();
        }
        unitRecipCell = new CoordinateSystem(
                reciprocalCell.getOrigin(), recipVectors);
        
        for (int i = -2; i < 3; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = -2; k < 3; k++) {
                    if ((i == 0) && (j == 0) && (k == 0)) {
                        continue;
                    }
                    assertMeanDisplacement(
                            unitRecipCell.coordinatesOf(
                                    new Vector(i, j, k).normalize()),
                            anisoU, u);
                }
            }
        }
    }
    
    /**
     * Tests that the mean square atomic displacement jointly described by the
     * specified reciprocal vector and anisotropic displacement coefficients
     * is equal to the specified value
     * 
     * @param  coords a {@code double[]} containing the coordinates (referred
     *         to a system parallel to the reciprocal cell, but having
     *         axes of unit-length
     * @param  anisoU a {@code double[][]} containing the anisotropic
     *         displacement coefficients
     * @param  expectedU the expected mean square displacement 
     */
    private void assertMeanDisplacement(double[] coords,
            double[][] anisoU, double expectedU) {
        double u = 0;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < i; j++) {
                u += 2 * anisoU[i][j] * coords[i] * coords[j];
            }
            u += anisoU[i][i] * coords[i] * coords[i];
        }
        
        assertEquals("Computed displacement is incorrect", expectedU, u, 1e-10);
    }

    /**
     * Tests the joint behavior of the {@code motionForIsotropicU(double)}
     * and {@code motionForAnisotropicU(double, double, double, double, double,
     * double)} methods; specifically that when the anisotropic parameters
     * computed by the former are passed to the latter, the equivalent isotropic
     * parameter computation regenerates the original isotropic parameter.
     */
    public void testFeature__motionForIsotropicU__motionForAnisotropicU() {
        AtomicMotionFactory factory;
        AtomicMotion anisoU;
        double[][] fromU;

        factory = new AtomicMotionFactory(
                new CoordinateSystem(8, 8, 8, 0, 0, 0));        
        fromU = factory.motionForIsotropicU(13).getAnisotropicU();
        anisoU = factory.motionForAnisotropicU(fromU[0][0], fromU[1][1],
                fromU[2][2], fromU[1][0], fromU[2][0], fromU[2][1]);
        assertEquals("Anisotropic parameters not consistent with isotropic U",
                13, anisoU.getIsotropicU(), 1e-10);
        
        factory = new AtomicMotionFactory(
                new CoordinateSystem(8, 13, 17, 0.0, 0, 0));        
        fromU = factory.motionForIsotropicU(13).getAnisotropicU();
        anisoU = factory.motionForAnisotropicU(fromU[0][0], fromU[1][1],
                fromU[2][2], fromU[1][0], fromU[2][0], fromU[2][1]);
        assertEquals("Anisotropic parameters not consistent with isotropic U",
                13, anisoU.getIsotropicU(), 1e-10);
        
        factory = new AtomicMotionFactory(
                new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0));        
        fromU = factory.motionForIsotropicU(13).getAnisotropicU();
        anisoU = factory.motionForAnisotropicU(fromU[0][0], fromU[1][1],
                fromU[2][2], fromU[1][0], fromU[2][0], fromU[2][1]);
        assertEquals("Anisotropic parameters not consistent with isotropic U",
                13, anisoU.getIsotropicU(), 1e-10);
        
        factory = new AtomicMotionFactory(
                new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3));
        fromU = factory.motionForIsotropicU(13).getAnisotropicU();
        anisoU = factory.motionForAnisotropicU(fromU[0][0], fromU[1][1],
                fromU[2][2], fromU[1][0], fromU[2][0], fromU[2][1]);
        assertEquals("Anisotropic parameters not consistent with isotropic U",
                13, anisoU.getIsotropicU(), 1e-10);
    }

    /**
     * Tests the behavior of the
     * {@code motionForIsotropicB(double, double, double, double, double,
     * double)} method <i>vs</i>. that of the parallel
     * {@code motionForIsotropicU()} method as a standard.  The results of the
     * former should be related to the results of the latter by the
     * AtomicMotionFactory.B_TO_U_SCALE_FACTOR scale factor.
     */
    public void testMethod__motionForIsotropicB_double() {
        AtomicMotionFactory factory;
        AtomicMotion isoB;
        AtomicMotion isoU;
        double[][] fromB;
        double[][] fromU;

        factory = new AtomicMotionFactory(
                new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3));
        isoB = factory.motionForIsotropicB(13);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForIsotropicU(13);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertFalse("Motion not marked isotropic", isoB.isAnisotropic());

        factory = new AtomicMotionFactory(
                new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0));        
        isoB = factory.motionForIsotropicB(13);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForIsotropicU(13);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertFalse("Motion not marked isotropic", isoB.isAnisotropic());
        
        factory = new AtomicMotionFactory(
                new CoordinateSystem(8, 13, 17, 0.0, 0, 0));        
        isoB = factory.motionForIsotropicB(13);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForIsotropicU(13);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertFalse("Motion not marked isotropic", isoB.isAnisotropic());
    }

    /**
     * Tests the normal behavior of the {@code motionForAnisotropicU(double,
     * double, double, double, double, double)} method; specifically, verifies
     * that the returned motion is flagged anisotropic and has the specified
     * atomic motion coefficients set correctly.
     */
    public void testMethod__motionForAnisotropicU_6double() {
        performAnisotropicUTest(new CoordinateSystem(8, 8, 8, 0, 0, 0));
        performAnisotropicUTest(new CoordinateSystem(8, 13, 17, 0.0, 0, 0));        
        performAnisotropicUTest(
                new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0));        
        performAnisotropicUTest(
                new CoordinateSystem(8, 8, 8, -.05, -.05, -.05));
        performAnisotropicUTest(
                new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3));
    }
    
    /**
     * Performs the details of the anisotropic U creation test for the specified
     * coordinate system
     * 
     * @param  unitCell a {@code CoordinateSystem} representing the unit cell
     *         for which to test the creation of anisotropic atomic motions
     */
    private void performAnisotropicUTest(CoordinateSystem unitCell) {
        AtomicMotionFactory factory = new AtomicMotionFactory(unitCell);
        double[][] anisoU;
        AtomicMotion motion;
        
        anisoU = new double[][] {{1}, {2, 5}, {3, 7, 11}};
        motion = factory.motionForAnisotropicU(anisoU[0][0], anisoU[1][1],
                anisoU[2][2], anisoU[1][0], anisoU[2][0], anisoU[2][1]);
        assertTrue("Motion not flagged anisotropic", motion.isAnisotropic());
        assertTrue("Motion has wrong displacement parameters",
                Arrays.deepEquals(anisoU, motion.getAnisotropicU()));
        
        anisoU = new double[][] {{1}, {0, 1}, {0, 0, 1}};
        motion = factory.motionForAnisotropicU(anisoU[0][0], anisoU[1][1],
                anisoU[2][2], anisoU[1][0], anisoU[2][0], anisoU[2][1]);
        assertTrue("Motion not flagged anisotropic", motion.isAnisotropic());
        assertTrue("Motion has wrong displacement parameters",
                Arrays.deepEquals(anisoU, motion.getAnisotropicU()));
    }

    /**
     * Tests the behavior of the
     * {@code motionForAnisotropicB(double, double, double, double, double,
     * double)} method <i>vs</i>. that of the parallel
     * {@code motionForAnisotropicU()} method as a standard.  The results of the
     * former should be related to the results of the latter by the
     * AtomicMotionFactory.B_TO_U_SCALE_FACTOR scale factor.
     */
    public void testMethod__motionForAnisotropicB_6double() {
        AtomicMotionFactory factory;
        AtomicMotion isoB;
        AtomicMotion isoU;
        double[][] fromB;
        double[][] fromU;

        factory = new AtomicMotionFactory(
                new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3));
        isoB = factory.motionForAnisotropicB(5, 7, 11, 1, 2, 3);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForAnisotropicU(5, 7, 11, 1, 2, 3);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertTrue("Motion not marked anisotropic", isoB.isAnisotropic());

        factory = new AtomicMotionFactory(
                new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0));        
        isoB = factory.motionForAnisotropicB(5, 7, 11, 1, 2, 3);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForAnisotropicU(5, 7, 11, 1, 2, 3);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertTrue("Motion not marked anisotropic", isoB.isAnisotropic());
        
        factory = new AtomicMotionFactory(
                new CoordinateSystem(8, 13, 17, 0.0, 0, 0));        
        isoB = factory.motionForAnisotropicB(5, 7, 11, 1, 2, 3);
        fromB = isoB.getAnisotropicU();
        isoU = factory.motionForAnisotropicU(5, 7, 11, 1, 2, 3);
        fromU = isoU.getAnisotropicU();
        for (int row = 0; row < fromB.length; row++) {
            for (int col = 0; col < fromB[row].length; col++) {
                assertEquals("B parameters don't match U parameters",
                        fromB[row][col]
                                   / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                        fromU[row][col], 1e-10);
            }
        }
        assertEquals("Isotropic B parameter doesn't match U parameter",
                isoB.getIsotropicU() / AtomicMotionFactory.B_TO_U_SCALE_FACTOR,
                isoU.getIsotropicU(), 1e-10);
        assertTrue("Motion not marked anisotropic", isoB.isAnisotropic());
    }

    /**
     * Tests the normal behavior of the {@code motionForAnisotropicBeta(double,
     * double, double, double, double, double)} method; specifically, verifies
     * that the resulting motions are flagged anisotropic, and that their U
     * parameters have the correct relationships with the original beta
     * parameters
     */
    public void testMethod__motionForAnisotropicBeta_6double() {
        CoordinateSystem cell;
        double[][] beta1 = new double[][] {{1}, {2, 5}, {3, 7, 11}};
        double[][] beta2 = new double[][] {{1}, {0, 1}, {0, 0, 1}};
        
        cell = new CoordinateSystem(8, 8, 8, 0, 0, 0);
        performAnisotropicBetaTest(cell, beta1);
        performAnisotropicBetaTest(cell, beta2);
        
        cell = new CoordinateSystem(8, 13, 17, 0.0, 0, 0);        
        performAnisotropicBetaTest(cell, beta1);
        performAnisotropicBetaTest(cell, beta2);
        
        cell = new CoordinateSystem(11, 7, 13, 0.0, -0.1, 0);        
        performAnisotropicBetaTest(cell, beta1);
        performAnisotropicBetaTest(cell, beta2);
        
        cell = new CoordinateSystem(8, 8, 8, -.05, -.05, -.05);
        performAnisotropicBetaTest(cell, beta1);
        performAnisotropicBetaTest(cell, beta2);
        
        cell = new CoordinateSystem(5, 6, 12, 0.1, 0.05, 0.3);
        performAnisotropicBetaTest(cell, beta1);
        performAnisotropicBetaTest(cell, beta2);
    }

    private void performAnisotropicBetaTest(CoordinateSystem unitCell,
            double[][] beta) {
        AtomicMotionFactory factory = new AtomicMotionFactory(unitCell);
        CoordinateSystem reciprocalCell = unitCell.getReciprocal();
        Vector[] reciprocalVectors = reciprocalCell.getVectors();
        double[] recipAxes = new double[] {
                reciprocalVectors[0].length(),
                reciprocalVectors[1].length(),
                reciprocalVectors[2].length()
        };
        final double scaleFactor = 2 * Math.PI * Math.PI;
        double[][] anisoU;
        double[][] expectedU;
        AtomicMotion motion;
        
        beta = new double[][] {{1}, {2, 5}, {3, 7, 11}};
        expectedU = new double[3][];
        for (int i = 0; i < 3; i++) {
            expectedU[i] = new double[i + 1];
            for (int j = 0; j <= i; j++) {
                expectedU[i][j] = beta[i][j]
                        / (scaleFactor * recipAxes[i] * recipAxes[j]); 
            }
        }
        motion = factory.motionForAnisotropicBeta(beta[0][0], beta[1][1],
                beta[2][2], beta[1][0], beta[2][0], beta[2][1]);
        assertTrue("Motion not flagged anisotropic", motion.isAnisotropic());
        anisoU = motion.getAnisotropicU();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <= i; j++) {
                assertEquals("Displacement parameter is incorrect",
                        expectedU[i][j], anisoU[i][j], 1e-10);
            }
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
        TestSuite tests = new TestSuite(AtomicMotionFactoryTests.class);
        
        tests.setName("AtomicMotionFactory Tests");
        
        return tests;
    }
}
