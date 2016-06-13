/*
 * Reciprocal Net Project
 *
 * AtomicMotionTests.java
 *
 * Dec 20, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code AtomicMotion} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomicMotionTests extends TestCase {

    /**
     * Initializes an {@code AtomicMotionTests} to perform the named test
     * 
     * @param  testName the name of the test to run
     */
    public AtomicMotionTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the constructor when an anisotropic motion
     * is specified
     */
    public void testConstructor_doubleArrayArray_double_boolean__anisotropic() {
        double[][] coords
                = new double[][] {{.05}, {.01, .06}, {.005, .01, .05}};
        double u = .055;
        AtomicMotion motion = new AtomicMotion(coords, u, true);
        
        assertTrue("Motion tensor set incorrectly",
                Arrays.deepEquals(coords, motion.getAnisotropicU()));
        assertFalse("The tensor was not copied on return",
                coords == motion.getAnisotropicU());
        assertEquals("Isotropic U set incorrectly", u, motion.getIsotropicU(),
                0);
        assertTrue("Isotropic flag set incorrectly", motion.isAnisotropic());
    }

    /**
     * Tests the normal behavior of the constructor when an isotropic motion is
     * specified along with a U tensor
     */
    public void testConstructor_doubleArrayArray_double_boolean__isotropic() {
        double[][] coords
                = new double[][] {{.05}, {.01, .06}, {.005, .01, .05}};
        double u = .055;
        AtomicMotion motion = new AtomicMotion(coords, u, false);
        
        assertTrue("Motion tensor set incorrectly",
                Arrays.deepEquals(coords, motion.getAnisotropicU()));
        assertFalse("The tensor was not copied on return",
                coords == motion.getAnisotropicU());
        assertEquals("Isotropic U set incorrectly", u, motion.getIsotropicU(),
                0);
        assertFalse("Isotropic flag set incorrectly", motion.isAnisotropic());
    }

    /**
     * Tests the normal behavior of the constructor when an isotropic motion is
     * specified along with a {@code null} U tensor
     */
    public void testConstructor_nulldoubleArrayArray_double_boolean__isotropic() {
        double u = .055;
        AtomicMotion motion = new AtomicMotion(null, u, false);
        
        assertNull("Motion tensor set incorrectly", motion.getAnisotropicU());
        assertEquals("Isotropic U set incorrectly", u, motion.getIsotropicU(),
                0);
        assertFalse("Isotropic flag set incorrectly", motion.isAnisotropic());
    }

    /**
     * Tests the constructor behavior when the U tensor is {@code null} and the
     * anisotropy flag is {@code true}; a {@code NullPointerException} is
     * expected
     */
    public void testConstructor_nulldoubleArrayArray_double_boolean__anisotropic() {
        try {
            new AtomicMotion(null, 0.05, true);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests constructor behavior when a U tensor having a {@code null} row is
     * specified (for both isotropic and anisotropic cases); a
     * {@code NullPointerException} is expected
     */
    public void testConstructor_doubleArraynullArray_double_boolean() {
        double[][] tensor = new double[][] {null, {0.1, 0.1}, {0.1, 0.1, 0.1}};
        
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        
        tensor[0] = new double[] {0.1};
        tensor[1] = null;
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        
        tensor[1] = new double[] {0.1, 0.1};
        tensor[2] = null;
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the constructor when a U tensor of inappropriate
     * shape is specified (for both anisotropic and isotropic cases); an
     * {@code IllegalArgumentException} is expected
     */
    public void testConstructor_doubleArrayArray_double_boolean__badShape() {
        double[][] tensor;
        
        tensor = new double[][] {{0.1}, {0.0, 0.1}};
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        
        tensor = new double[][] {{}, {0.0, 0.1}, {0.0, 0.0, 0.1}};
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        
        tensor = new double[][] {{0.1}, {0.0}, {0.0, 0.0, 0.1}};
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        
        tensor = new double[][] {{0.1}, {0.0, 0.1}, {0.0, 0.1}};
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        
        tensor = new double[][] {{0.1}, {0.0, 0.1}, {0.0, 0.0, 0.1},
            {0.0, 0.0, 0.0, 0.1}};
        try {
            new AtomicMotion(tensor, 0.1, true);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new AtomicMotion(tensor, 0.1, false);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
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
        TestSuite tests = new TestSuite(AtomicMotionTests.class);
        
        tests.setName("AtomicMotion Tests");
        
        return tests;
    }
}
