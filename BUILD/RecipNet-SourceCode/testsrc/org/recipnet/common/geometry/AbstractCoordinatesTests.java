/*
 * Reciprocal Net Project
 *
 * AbstractCoordinatesTests.java
 *
 * Dec 15, 2005: jobollin wrote first draft
 */
package org.recipnet.common.geometry;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} exercising the behavior of the
 * {@code AbstractCoordinateTests} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AbstractCoordinatesTests extends TestCase {

    /**
     * Initializes an {@code AbstractCoordinatesTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public AbstractCoordinatesTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the {@code AbstractCoordinates} constructor;
     * specifically, verifies that the submitted array is stored internally
     * rather than copied
     */
    public void testConstructor() {
        double[] coords = new double[] {1.0, -2.1, Math.PI};
        AbstractCoordinates testCoordinates = new TestCoordinates(coords);
        
        assertTrue("Submitted coordinates were cloned",
                testCoordinates.getInternalCoordinates() == coords);
    }

    /**
     * Tests the behavior of the {@code AbstractCoordinates} constructor when
     * its argument is {@code null}; a {@code NullPointerException} is
     * expected.
     */
    public void testConstructor__null() {
        try {
            new TestCoordinates(null);
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
        AbstractCoordinates testCoordinates = new TestCoordinates(coords);
        
        assertTrue("Coordinates not correctly copied",
                Arrays.equals(coords, testCoordinates.getCoordinates()));
        assertFalse("Submitted coordinates were publicly exposed",
                testCoordinates.getCoordinates() == coords);
        assertFalse("The same coordinate array was returned twice",
                testCoordinates.getCoordinates()
                == testCoordinates.getCoordinates());
    }
    
    /**
     * Tests {@code AbstractCoordinates}' facility for modifying its underlying
     * coordinates; specifically, tests that modifications to that array are
     * reflected in the return value of the regular {@code getCoordinates}
     * method.
     */
    public void testFeature__modifyInternalCoordinates() {
        AbstractCoordinates testCoordinates
                = new TestCoordinates(new double[] {1.0, -2.1, Math.PI});
        double[] coords = testCoordinates.getInternalCoordinates();
        
        coords[0] = Double.NaN;
        coords[1] = -Math.E;
        coords[2] = 42;
        assertTrue("Internal coordinate changes not visible",
                Arrays.equals(coords, testCoordinates.getCoordinates()));
    }

    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(AbstractCoordinatesTests.class);
        
        tests.setName("AbstractCoordinates Tests");
        
        return tests;
    }
    
    /**
     * A concrete {@code AbstractCoordinates} subclass for use in testing its
     * superclass.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class TestCoordinates extends AbstractCoordinates {
        
        /**
         * Initializes a {@code TestCoordinates} with the specified coordinate
         * array; simply passes the specified array to the superclass'
         * constructor
         * 
         * @param  coords a {@code double[]} to use as the coordinates
         */
        public TestCoordinates(double[] coords) {
            super(coords);
        }
    }
}
