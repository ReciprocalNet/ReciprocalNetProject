/*
 * Reciprocal Net Project
 *
 * PointTests.java
 *
 * Dec 15, 2005: jobollin wrote first draft
 */
package org.recipnet.common.geometry;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code Point}
 * class.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class PointTests extends TestCase {

    /**
     * Initializes a new {@code PointTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public PointTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the unary constructor to verify that it
     * causes its argument to be copied (correctly) to the point's internal
     * coordinates
     */
    public void testConstructor__doubleArray() {
        double[] coords = new double[] {1.0, -2.1, Math.PI};
        Point p = new Point(coords);
        
        assertFalse("Submitted coordinates were not cloned",
                p.getInternalCoordinates() == coords);
        assertTrue("Submitted coordinates were incorrectly cloned",
                Arrays.equals(coords, p.getInternalCoordinates()));
    }

    /**
     * Tests the behavior of the unary constructor when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__doubleArray_null() {
        try {
            new Point(null);
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
        Point testPoint = new Point(x, y, z);
        
        assertTrue("Coordinates not correctly stored",
                Arrays.equals(new double[] {x, y, z},
                testPoint.getCoordinates()));
    }

    /**
     * Tests the behavior of the {@code midPoint(Point...)} method
     */
    public void testMethod__midPoint() {
        double[] coords;
        
        // Test without one point
        coords = new double[] {1.1, -2.5, Math.PI};
        assertTrue("Trivial midpoint returns the wrong coordinates",
                Arrays.equals(coords,
                        Point.midPoint(new Point(coords)).getCoordinates()));
        
        // Test with several points; relies on exact FP math and exactly
        // representable values, which should be OK as chosen
        coords = new double[] {0.5, 0.5, 0.5};
        assertTrue("Four-point midpoint returns the wrong coordinates",
                Arrays.equals(coords, 
                        Point.midPoint(
                                new Point(1, 0, 0), new Point(0, 1, 0),
                                new Point(0, 0, 1), new Point(1, 1, 1)
                                ).getCoordinates()));
    }

    /**
     * Tests the normal behavior of the {@code distanceTo()} method
     */
    public void testMethod__distanceTo() {
        assertEquals("Incorrect distance computation",
                5.0, new Point(1, 3, 2).distanceTo(new Point(1, 0, 6)), 1e-10);
        assertEquals("Incorrect distance computation",
                5.0, new Point(1, 0, 2).distanceTo(new Point(-3, 0, 5)), 1e-10);
        assertEquals("Incorrect distance computation",
                5.0, new Point(2, 1, 1).distanceTo(new Point(5, -3, 1)), 1e-10);
    }

    /**
     * Tests the normal behavior of the {@code dSqrTo()} method
     */
    public void testMethod__dSqrTo() {
        assertEquals("Incorrect distance computation",
                3.0, new Point(1, 3, 2).dSqrTo(new Point(2, 2, 3)), 1e-10);
        assertEquals("Incorrect distance computation",
               14.0, new Point(1, 0, 2).dSqrTo(new Point(-1, 3, 1)), 1e-10);
    }

    /**
     * Tests the behavior of the getCoordinates() method; specifically, verifies
     * that it returns a correct copy of the insternal array, rather than the
     * internal array itself or an incorrect copy of it.
     */
    public void testMethod__getCoordinates() {
        double[] coords = new double[] {1.0, -2.1, Math.PI};
        Point testPoint = new Point(coords);
        
        assertTrue("Coordinates not correctly copied",
                Arrays.equals(coords, testPoint.getCoordinates()));
        assertFalse("Submitted coordinates were publicly exposed",
                testPoint.getCoordinates() == coords);
        assertFalse("The same coordinate array was returned twice",
                testPoint.getCoordinates()
                == testPoint.getCoordinates());
        assertFalse("The internal coordinate array was returned",
                testPoint.getInternalCoordinates()
                == testPoint.getCoordinates());
    }
    
    /**
     * Tests {@code Point}'s facility for modifying its underlying
     * coordinates; specifically, tests that modifications to that array are
     * reflected in the return value of the regular {@code getCoordinates}
     * method.
     */
    public void testFeature__modifyInternalCoordinates() {
        Point testPoint = new Point(new double[] {1.0, -2.1, Math.PI});
        double[] coords = testPoint.getInternalCoordinates();
        
        coords[0] = Double.NaN;
        coords[1] = -Math.E;
        coords[2] = 42;
        assertTrue("Internal coordinate changes not visible",
                Arrays.equals(coords, testPoint.getCoordinates()));
    }

    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(PointTests.class);
        
        tests.setName("Point Tests");
        
        return tests;
    }
}
