/*
 * Reciprocal Net Project
 *
 * CoordinateLineTests.java
 *
 * Dec 15, 2005: jobollin wrote first draft
 */
package org.recipnet.common.geometry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code CoordinateLine} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CoordinateLineTests extends TestCase {

    /**
     * Initializes a new {@code CoordinateLineTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CoordinateLineTests(String testName) {
        super(testName);
    }

    /**
     * Tests the Point/Vector constructor to verify its correct normal
     * operation, as well as the correct operation of the
     * {@code getReferencePoint()}, {@code getDirection()}, and
     * {@code getunitLength()} methods.
     */
    public void testConstructor__Point_Vector() {
        Point p = new Point(2, 5, 3);
        Vector v = new Vector(-11, 1, 7);
        CoordinateLine line = new CoordinateLine(p, v);
        
        assertTrue("Reference point not stored", line.getReferencePoint() == p);
        assertEquals("Line has wrong direction",
                0, line.getDirection().angleWith(v), 1e-10);
        assertEquals("Line has wrong unit length", v.length(),
                line.getUnitLength(), 1e-10);
        assertEquals("Line has wrong coordinate for its origin", 0.0,
                line.coordinateOf(p), 1e-10);
    }

    /**
     * Tests the behavior of the Point/Vector constructor when its first
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullPoint_Vector() {
        try {
            new CoordinateLine(null, new Vector(1, 0, 0));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Point/Vector constructor when its second
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullVector() {
        try {
            new CoordinateLine(Point.ORIGIN, (Vector) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the Point/Point constructor to verify its correct normal
     * operation, as well as the correct operation of the
     * {@code getReferencePoint()}, {@code getDirection()}, and
     * {@code getunitLength()} methods.
     */
    public void testConstructor__Point_Point() {
        Point p1 = new Point(-7, 1, 5);
        Point p2 = new Point(7, 3, 7);
        CoordinateLine line = new CoordinateLine(p1, p2);
        Vector v = new Vector(p1, p2);
        
        assertTrue("Reference point not stored",
                line.getReferencePoint() == p1);
        assertEquals("Line has wrong direction",
                0, line.getDirection().angleWith(v), 1e-10);
        assertEquals("Line has wrong unit length", v.length(),
                line.getUnitLength(), 1e-10);
        assertEquals("Line has wrong coordinate for its origin", 0.0,
                line.coordinateOf(p1), 1e-10);
        assertEquals("Line has wrong coordinate for its second point", 1.0,
                line.coordinateOf(p2), 1e-10);
    }

    /**
     * Tests the behavior of the Point/Point constructor when its first
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullPoint_Point() {
        try {
            new CoordinateLine(null, Point.ORIGIN);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Point/Point constructor when its second
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullPoint() {
        try {
            new CoordinateLine(Point.ORIGIN, (Point) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code coordinateOf(Point)} method
     */
    public void testMethod__coordinateOf_Point() {
        Point p = new Point(13, -2, -3);
        Vector v = new Vector(-1, -11, 17);
        Vector vp1 = new Vector(0, 17, 11);
        Vector vp2 = new Vector(-17, 0, -1);
        Vector vp3 = new Vector(11, -1, 0);
        CoordinateLine line = new CoordinateLine(p, v);
        Point p2;
        
        p2 = v.endPoint(p);
        assertEquals("Wrong coordinate", 1.0, line.coordinateOf(p2), 1e-10);
        assertEquals("Wrong coordinate", 1.0,
                line.coordinateOf(vp1.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", 1.0,
                line.coordinateOf(vp2.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", 1.0,
                line.coordinateOf(vp3.endPoint(p2)), 1e-10);
        
        p2 = v.scale(3).endPoint(p);
        assertEquals("Wrong coordinate", 3.0, line.coordinateOf(p2), 1e-10);
        assertEquals("Wrong coordinate", 3.0,
                line.coordinateOf(vp1.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", 3.0,
                line.coordinateOf(vp2.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", 3.0,
                line.coordinateOf(vp3.endPoint(p2)), 1e-10);
        
        p2 = v.scale(-2.5).endPoint(p);
        assertEquals("Wrong coordinate", -2.5, line.coordinateOf(p2), 1e-10);
        assertEquals("Wrong coordinate", -2.5,
                line.coordinateOf(vp1.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", -2.5,
                line.coordinateOf(vp2.endPoint(p2)), 1e-10);
        assertEquals("Wrong coordinate", -2.5,
                line.coordinateOf(vp3.endPoint(p2)), 1e-10);
    }

    /**
     * Tests the normal behavior of the {@code coordinateOf(Point)} method
     * when its argument is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testMethod__coordinateOf_nullPoint() {
        CoordinateLine line
                = new CoordinateLine(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            line.coordinateOf(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the {@code pointAt(double) method} to verify that it produces
     * correct results
     */
    public void testMethod__pointAt_double() {
        Point p1 = new Point(13, -2, -3);
        Point p2 = new Point(11, 7, 2);
        double diff = p1.distanceTo(p2);
        CoordinateLine line = new CoordinateLine(p1, p2);
        Point p;
        
        p = line.pointAt(2.25);
        assertEquals("The result point is not on the line", 0.0,
                line.distanceTo(p), 1e-10);
        assertEquals("The result point is at the wrong position",
                2.25, line.coordinateOf(p), 1e-10);
        assertEquals("The distance to the origin is wrong", diff * 2.25,
                p1.distanceTo(p), 1e-10);
        assertEquals("The distance to the second point is wrong", diff * 1.25,
                p2.distanceTo(p), 1e-10);
        
        p = line.pointAt(-5.675);
        assertEquals("The result point is not on the line", 0.0,
                line.distanceTo(p), 1e-10);
        assertEquals("The result point is at the wrong position",
                -5.675, line.coordinateOf(p), 1e-10);
        assertEquals("The distance to the origin is wrong", diff * 5.675,
                p1.distanceTo(p), 1e-10);
        assertEquals("The distance to the second point is wrong", diff * 6.675,
                p2.distanceTo(p), 1e-10);
        
        p = line.pointAt(0.0);
        assertEquals("The distance to the origin is wrong", 0.0,
                p1.distanceTo(p), 1e-10);
        
        p = line.pointAt(1.0);
        assertEquals("The distance to the second point is wrong", 0.0,
                p2.distanceTo(p), 1e-10);
    }

    /**
     * Tests the normal behavior of the {@code nearestPoint(Point)} method to
     * verify that it produces correct results
     */
    public void testMethod__nearestPoint_Point() {
        Point p0 = new Point(2, 0, -13);
        Vector v = new Vector(-3, 1, 7);
        Vector vp1 = new Vector(0, -7, 1);
        Vector vp2 = new Vector(7, 0, 3);
        Vector vp3 = new Vector(-1, -3, 0);
        CoordinateLine line = new CoordinateLine(p0, v);
        Point p;
        
        p = line.pointAt(3);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp1.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp2.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp3.endPoint(p))), 1e-10);
        
        p = line.pointAt(-2.5);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp1.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp2.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp3.endPoint(p))), 1e-10);

        p = p0;
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp1.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp2.endPoint(p))), 1e-10);
        assertEquals("Nearest point not computed correctly", 0.0,
                p.distanceTo(line.nearestPoint(vp3.endPoint(p))), 1e-10);
    }
    
    /**
     * Tests the behavior of the {@code nearestPoint(Point)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__nearestPoint_nullPoint() {
        CoordinateLine line
                = new CoordinateLine(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            line.nearestPoint(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        
    }

    /**
     * Tests the normal operation of the {@code distanceTo(Point)} method
     */
    public void testMethod__distanceTo_Point() {
        Point p0 = new Point(17, 23, 5);
        Vector v = new Vector(-3, 2, 5);
        Vector vp1 = new Vector(0, -5, 2).normalize();
        Vector vp2 = new Vector(5, 0, 3).normalize();
        Vector vp3 = new Vector(-2, -3, 0).normalize();
        CoordinateLine line = new CoordinateLine(p0, v);
        Point p;
        
        p = line.pointAt(27);
        assertEquals("Wrong distance computed", 2.5,
                line.distanceTo(vp1.scale(2.5).endPoint(p)), 1e-10);
        assertEquals("Wrong distance computed", 1.5,
                line.distanceTo(vp2.scale(1.5).endPoint(p)), 1e-10);
        assertEquals("Wrong distance computed", 1.5,
                line.distanceTo(vp3.scale(-1.5).endPoint(p)), 1e-10);
        
        p = line.pointAt(-3);
        assertEquals("Wrong distance computed", 3.25,
                line.distanceTo(vp1.scale(3.25).endPoint(p)), 1e-10);
        assertEquals("Wrong distance computed", 1.75,
                line.distanceTo(vp2.scale(-1.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance computed", .5,
                line.distanceTo(vp3.scale(.5).endPoint(p)), 1e-10);
    }

    /**
     * Tests the behavior of the {@code distanceTo(Point)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__distanceTo_nullPoint() {
        CoordinateLine line
                = new CoordinateLine(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            line.distanceTo(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
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
        TestSuite tests = new TestSuite(CoordinateLineTests.class);
        
        tests.setName("CoordinateLine Tests");
        
        return tests;
    }
}

