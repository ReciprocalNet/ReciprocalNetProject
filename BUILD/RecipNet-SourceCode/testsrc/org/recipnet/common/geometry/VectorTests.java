/*
 * Reciprocal Net Project
 *
 * VectorTests.java
 *
 * Dec 15, 2005: jobollin wrote first draft
 */
package org.recipnet.common.geometry;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code Vector}
 * class.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class VectorTests extends TestCase {

    /**
     * Initializes a new {@code VectorTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public VectorTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the unary constructor to verify that it
     * causes its argument to be copied (correctly) to the point's internal
     * coordinates
     */
    public void testConstructor__doubleArray() {
        double[] coords = new double[] {1.0, -2.1, Math.PI};
        Vector vec = new Vector(coords);
        
        assertFalse("Submitted coordinates were not cloned",
                vec.getInternalCoordinates() == coords);
        assertTrue("Submitted coordinates were incorrectly cloned",
                Arrays.equals(coords, vec.getInternalCoordinates()));
    }

    /**
     * Tests the behavior of the unary constructor when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__doubleArray_null() {
        try {
            new Vector(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the trinary constructor; specifically, verifies
     * that it correctly records the specified coordinates
     */
    public void testConstructor__double_double_double() {
        double x = Double.NaN, y = Math.E, z = -0.0;
        Vector testVector = new Vector(x, y, z);
        
        assertTrue("Coordinates not correctly stored",
                Arrays.equals(new double[] {x, y, z},
                testVector.getCoordinates()));
    }

    /**
     * Tests the behavior of the binary constructor to verify that it creates
     * a vector having the correct coordinates
     */
    public void testConstructor__Point_Point() {
        Point p1 = new Point(1, 2, -3);
        Point p2 = new Point(42, 17, 21);
        
        assertTrue("Incorrect coordinates", Arrays.equals(
                new double[] {41., 15., 24.},
                new Vector(p1, p2).getCoordinates()));
        assertTrue("Incorrect coordinates", Arrays.equals(
                new double[] {-41., -15., -24.},
                new Vector(p2, p1).getCoordinates()));
    }

    /**
     * Tests the behavior of the binary constructor when its first argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullPoint_Point() {
        try {
            new Vector(null, Point.ORIGIN);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the binary constructor when its second argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullPoint() {
        try {
            new Vector(Point.ORIGIN, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the {@code length()} method
     */
    public void testMethod__length() {
        assertEquals("Incorrect vector length",
                5.0, new Vector(3, 4, 0).length(), 1e-10);
        assertEquals("Incorrect distance computation",
                5.0, new Vector(0, 3, 4).length(), 1e-10);
        assertEquals("Incorrect distance computation",
                5.0, new Vector(4, 0, 3).length(), 1e-10);
    }

    /**
     * Tests the behavior of the {@code scale(double)}; specifically, tests that
     * it both returns a correctly-scaled result and does not modify the vector
     * it is invoked on
     */
    public void testMethod__scale_double() {
        Vector testVector = new Vector(1, -2, 7);
        Vector scaled;
        
        // Test scaling by 1
        scaled = testVector.scale(1.0);
        assertTrue("Trivial scaling returns incorrect result",
                Arrays.equals(new double[] {1., -2., 7.},
                        scaled.getCoordinates()));
        
        // Test scaling by 3
        scaled = testVector.scale(3.0);
        assertTrue("Tripling returns incorrect result",
                Arrays.equals(new double[] {3., -6., 21.},
                        scaled.getCoordinates()));
        assertTrue("Scaling modified the original vector",
                Arrays.equals(new double[] {1., -2., 7.},
                        testVector.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code normalize()} method to verify that it
     * produces a result that has length very near 1.0 and is parallel to the
     * original vector, and that it does not modify the original vector
     */
    public void testMethod__normalize() {
        Vector testVector = new Vector(5, -17, 7);
        Vector normalized = testVector.normalize();
        
        assertEquals("Normalized vector does not have length 1",
                1.0, normalized.length(), 1e-10);
        assertEquals("Normalized vector is not parallel to the original vector",
                0.0, testVector.angleWith(normalized), 1e-10);
        assertTrue("The original vector was modified",
                Arrays.equals(new double[] {5, -17, 7},
                        testVector.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code dotProduct()} method; in particular,
     * verifies that it produces the expected results and does not modify either
     * its target or its argument
     */
    public void testMethod__dotProduct_Vector() {
        Vector v1 = new Vector(7, 19, 31);
        Vector v2 = new Vector(2, 3, 5);
        Vector v3 = new Vector(13, 7, -11);
        
        assertEquals("Zero vector produced a non-zero dot product",
                0.0, Vector.ZERO.dotProduct(v1), 1e-10);
        assertEquals("Nonzero vectors produced an incorrect dot product",
                -8., v2.dotProduct(v3), 1e-10);
        assertTrue("Dot product modified the left factor",
                Arrays.equals(new double[]{2, 3, 5}, v2.getCoordinates()));
        assertTrue("Dot product modified the right factor",
                Arrays.equals(new double[]{13, 7, -11}, v3.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code dotProduct()} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__dotProduct_nullVector() {
        try {
            new Vector(7, 19, 31).dotProduct(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the {@code crossProduct(Vector)} method; in particular, verifies
     * that the result has the correct length, correct angles with the method
     * target and its argument, and is directed to correctly
     */
    public void testMethod__crossProduct_Vector() {
        Vector v1 = new Vector(13, 2, 3);
        Vector v2 = new Vector(5, 31, 1);
        Vector result = v1.crossProduct(v2);
        
        assertEquals(
                "Cross product result not perpendicular to the left factor",
                0., v1.dotProduct(result), 1e-10);
        assertEquals(
                "Cross product result not perpendicular to the right factor",
                0., v2.dotProduct(result), 1e-10);
        assertTrue("Cross product result is directed incorrectly",
                result.getCoordinates()[2] > 0);
        assertEquals("Cross product result has the wrong length",
                v1.length() * v2.length() * Math.sin(v1.angleWith(v2)),
                result.length(), 1e-10);
        assertTrue("Cross product modified the left factor",
                Arrays.equals(new double[] {13, 2, 3}, v1.getCoordinates()));
        assertTrue("Cross product modified the right factor",
                Arrays.equals(new double[] {5, 31, 1}, v2.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code crossProduct()} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__crossProduct_nullVector() {
        try {
            new Vector(7, 19, 31).crossProduct(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the {@code sum(Vector)} method; specifically, verifies that it
     * produces a correct result and does not modify the method target or
     * argument
     */
    public void testMethod__sum_Vector() {
        Vector v1 = new Vector(1, -11, 7);
        Vector v2 = new Vector(-5, 31, 17);
        
        assertTrue("Vector sum produced incorrect result",
                Arrays.equals(new double[]{-4, 20, 24},
                        v1.sum(v2).getCoordinates()));
        assertTrue("Vector sum modified the left sumand",
                Arrays.equals(new double[]{1, -11, 7}, v1.getCoordinates()));
        assertTrue("Vector sum modified the right sumand",
                Arrays.equals(new double[]{-5, 31, 17}, v2.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code sum()} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__sum_nullVector() {
        try {
            new Vector(7, 19, 31).sum(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the {@code endPoint(Point)} method; specifically, verifies that it
     * produces a correct result and does not modify the method target or
     * argument
     */
    public void testMethod__endPoint_Point() {
        Vector v = new Vector(1, -11, 7);
        Point p = new Point(-5, 31, 17);
        
        assertTrue("Vector endpoint produced incorrect result",
                Arrays.equals(new double[]{-4, 20, 24},
                        v.endPoint(p).getCoordinates()));
        assertTrue("Vector endpoint modified the vector",
                Arrays.equals(new double[]{1, -11, 7}, v.getCoordinates()));
        assertTrue("Vector endpoint modified the start point",
                Arrays.equals(new double[]{-5, 31, 17}, p.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code endPoint()} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__endPoint_nullPoint() {
        try {
            new Vector(7, 19, 31).endPoint(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the {@code angleWith(Vector)} method to verify that it produces
     * correct results and does not modify its arguments
     */
    public void testMethod__angleWith_Vector() {
        performAngleTest(1, 0, 0, 1, 0, 0, 0);
        performAngleTest(1, 0, 0, 0, 1, 0, Math.PI / 2);
        performAngleTest(1, 0, 0, 0, 0, 1, Math.PI / 2);
        performAngleTest(0, 1, 0, 0, Math.cos(1), Math.sin(1), 1);
        performAngleTest(0, 1, 0, 0, Math.cos(2), Math.sin(2), 2);
        performAngleTest(3, 0, 0, 2, 0, 0, 0);
        performAngleTest(1, 0, 0, 0, 5, 0, Math.PI / 2);
        performAngleTest(7, 0, 0, 0, 0, 2, Math.PI / 2);
        performAngleTest(0, 4, 0, 0, Math.cos(1), Math.sin(1), 1);
        performAngleTest(0, 5, 0, 0, Math.cos(2), Math.sin(2), 2);
    }

    /**
     * A helper method that performs the details of testing the angleWith()
     * test
     * 
     * @param x1 the x coordinate of the first vector
     * @param y1 the y coordinate of the first vector
     * @param z1 the z coordinate of the first vector
     * @param x2 the x coordinate of the second vector
     * @param y2 the y coordinate of the second vector
     * @param z2 the z coordinate of the second vector
     * @param radians the expected angle between the two vectors
     */
    private void performAngleTest(double x1, double y1, double z1,
            double x2, double y2, double z2, double radians) {
        Vector v1 = new Vector(x1, y1, z1);
        Vector v2 = new Vector(x2, y2, z2);
        
        assertEquals("Angle computation returns incorrect result",
                v1.angleWith(v2), radians, 1e-10);
        assertTrue("Angle computation modified the left vector",
                Arrays.equals(new double[] {x1, y1, z1}, v1.getCoordinates()));
        assertTrue("Angle computation modified the right vector",
                Arrays.equals(new double[] {x2, y2, z2}, v2.getCoordinates()));
        assertEquals("Reverse-order angle computation returns incorrect result",
                v2.angleWith(v1), radians, 1e-10);
        assertEquals("(Left) scaled angle computation returns incorrect result",
                v1.scale(2.5).angleWith(v2), radians, 1e-10);
        assertEquals("(Right) scaled angle computation returns incorrect result",
                v1.angleWith(v2.scale(3)), radians, 1e-10);
        assertEquals("Scaled angle computation returns incorrect result",
                v1.scale(7).angleWith(v2.scale(3)), radians, 1e-10);
    }

    /**
     * Tests the behavior of the {@code angleWith()} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__angleWith_nullVector() {
        try {
            new Vector(7, 19, 31).angleWith(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the getCoordinates() method; specifically, verifies
     * that it returns a correct copy of the insternal array, rather than the
     * internal array itself or an incorrect copy of it.
     */
    public void testMethod__getCoordinates() {
        double[] coords = new double[] {1.0, -2.1, Math.PI};
        Vector testVector = new Vector(coords);
        
        assertTrue("Coordinates not correctly copied",
                Arrays.equals(coords, testVector.getCoordinates()));
        assertFalse("Submitted coordinates were publicly exposed",
                testVector.getCoordinates() == coords);
        assertFalse("The same coordinate array was returned twice",
                testVector.getCoordinates()
                == testVector.getCoordinates());
        assertFalse("The internal coordinate array was returned",
                testVector.getInternalCoordinates()
                == testVector.getCoordinates());
    }
    
    /**
     * Tests {@code Vector}'s facility for modifying its underlying
     * coordinates; specifically, tests that modifications to that array are
     * reflected in the return value of the regular {@code getCoordinates()}
     * method.
     */
    public void testFeature__modifyInternalCoordinates() {
        Vector testVector = new Vector(new double[] {1.0, -2.1, Math.PI});
        double[] coords = testVector.getInternalCoordinates();
        
        coords[0] = Double.NaN;
        coords[1] = -Math.E;
        coords[2] = 42;
        assertTrue("Internal coordinate changes not visible",
                Arrays.equals(coords, testVector.getCoordinates()));
    }

    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(VectorTests.class);
        
        tests.setName("Vector Tests");
        
        return tests;
    }
}
