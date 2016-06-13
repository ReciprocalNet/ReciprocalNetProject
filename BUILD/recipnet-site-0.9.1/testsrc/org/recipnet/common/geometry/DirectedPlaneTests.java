/*
 * Reciprocal Net Project
 *
 * DirectedPlaneTests.java
 *
 * Dec 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.geometry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code DirectedPlane} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class DirectedPlaneTests extends TestCase {

    /**
     * Initializes a new {@code DirectedPlaneTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public DirectedPlaneTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the Point/Vector constructor and the
     * {@code getNormal()} method
     */
    public void testConstructor__Point_Vector() {
        Point p = new Point(-1, -5, 7);
        Vector v = new Vector(11, 2, 3);
        DirectedPlane plane = new DirectedPlane(p, v);
        
        assertEquals("The plane does not contain its reference point", 0.0,
                plane.distanceTo(p), 1e-10);
        assertEquals("The plane's normal is incorrect", 0.0,
                plane.getNormal().angleWith(v), 1e-10);
        assertEquals("The plane's normal has the wrong length", 1.0,
                plane.getNormal().length(), 1e-10);
    }

    /**
     * Tests the behavior of the Point/Vector constructor when the first
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullPoint_Vector() {
        try {
            new DirectedPlane((Point) null, new Vector(1, 0, 0));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Point/Vector constructor when the second
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullVector() {
        try {
            new DirectedPlane(Point.ORIGIN, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the Point/Vector/Vector constructor
     */
    public void testConstructor__Point_Vector_Vector() {
        Point p = new Point(2, 3, 1);
        Vector v1 = new Vector(5, 11, 2);
        Vector v2 = new Vector(7, 3, 23);
        DirectedPlane plane = new DirectedPlane(p, v1, v2);
        
        assertEquals("The plane does not contain its reference point", 0.0,
                plane.distanceTo(p), 1e-10);
        assertEquals("The plane does not contain the first vector", 0.0,
                plane.angleWith(v1), 1e-10);
        assertEquals("The plane does not contain the second vector", 0.0,
                plane.angleWith(v2), 1e-10);
        assertEquals("The plane's normal has the wrong length", 1.0,
                plane.getNormal().length(), 1e-10);
    }

    /**
     * Tests the behavior of the Point/Vector/Vector constructor when the first
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullPoint_Vector_Vector() {
        try {
            new DirectedPlane(null, new Vector(1, 0, 0), new Vector(0, 1, 0));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Point/Vector/Vector constructor when the second
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullVector_Vector() {
        try {
            new DirectedPlane(Point.ORIGIN, null, new Vector(0, 1, 0));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Point/Vector/Vector constructor when the third
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_Vector_nullVector() {
        try {
            new DirectedPlane(Point.ORIGIN, new Vector(0, 1, 0), null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal operation of the Linear/Point constructor
     */
    public void testConstructor__Linear_Point() {
        Point p = new Point(3, 11, 7);
        Linear l = new CoordinateLine(
                new Point(-1, -2, 2), new Point(2, -3, 3));
        DirectedPlane plane = new DirectedPlane(l, p);
        
        assertEquals("The plane does not contain its reference point", 0.0,
                plane.distanceTo(p), 1e-10);
        assertEquals("The plane does not contain the line's reference point",
                0.0, plane.distanceTo(l.getReferencePoint()), 1e-10);
        assertEquals("The plane is not parallel to the line", 0.0,
                plane.angleWith(l.getDirection()), 1e-10);
        assertEquals("The plane's normal has the wrong length", 1.0,
                plane.getNormal().length(), 1e-10);
    }

    /**
     * Tests the behavior of the Linear/Point constructor when the first
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullLinear_Point() {
        try {
            new DirectedPlane(null, Point.ORIGIN);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the Linear/Point constructor when the second
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Linear_nullPoint() {
        try {
            new DirectedPlane(
                    new CoordinateLine(Point.ORIGIN, new Point(1, 0, 0)), null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code distanceTo(Point)} method
     */
    public void testMethod__distanceTo_Point() {
        Point p0 = new Point(-3, -5, -7);
        Vector v1 = new Vector(2, 2, 5);
        Vector v2 = new Vector(7, -1, 2);
        DirectedPlane plane = new DirectedPlane(p0, v1, v2);
        Vector norm = plane.getNormal();
        Point p;
        
        p = v1.scale(2.5).sum(v2.scale(-1)).endPoint(p0);
        assertEquals("Wrong distance to a point", 3.25,
                plane.distanceTo(norm.scale(3.25).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 3.25,
                plane.distanceTo(norm.scale(-3.25).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.25,
                plane.distanceTo(norm.scale(0.25).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.25,
                plane.distanceTo(norm.scale(-0.25).endPoint(p)), 1e-10);
        
        p = v1.scale(-7.125).sum(v2.scale(-3)).endPoint(p0);
        assertEquals("Wrong distance to a point", 3.75,
                plane.distanceTo(norm.scale(3.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 3.75,
                plane.distanceTo(norm.scale(-3.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.75,
                plane.distanceTo(norm.scale(0.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.75,
                plane.distanceTo(norm.scale(-0.75).endPoint(p)), 1e-10);
        
        p = v1.scale(2).sum(v2.scale(5)).endPoint(p0);
        assertEquals("Wrong distance to a point", 1.75,
                plane.distanceTo(norm.scale(1.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 1.75,
                plane.distanceTo(norm.scale(-1.75).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.5,
                plane.distanceTo(norm.scale(0.5).endPoint(p)), 1e-10);
        assertEquals("Wrong distance to a point", 0.5,
                plane.distanceTo(norm.scale(-0.5).endPoint(p)), 1e-10);
    }

    /**
     * Tests the behavior of the {@code distanceTo(Point)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__distanceTo_nullPoint() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.distanceTo(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code angleWith(Vector)} method
     */
    public void testMethod__angleWith_Vector() {
        DirectedPlane plane
                = new DirectedPlane(new Point(2, 17, 7), new Vector(-3, 5, 13));
        Vector normal = plane.getNormal();
        double halfPi = Math.PI / 2;
        Vector v;
        
        v = new Vector(1, 0, 0);
        assertEquals("Wrong plane-to-vector angle",
                Math.abs(halfPi - normal.angleWith(v)), plane.angleWith(v),
                1e-10);
        v = new Vector(0, 1, 0);
        assertEquals("Wrong plane-to-vector angle",
                Math.abs(halfPi - normal.angleWith(v)), plane.angleWith(v),
                1e-10);
        v = new Vector(0, 0, 1);
        assertEquals("Wrong plane-to-vector angle",
                Math.abs(halfPi - normal.angleWith(v)), plane.angleWith(v),
                1e-10);
        v = new Vector(5, 1, 11);
        assertEquals("Wrong plane-to-vector angle",
                Math.abs(halfPi - normal.angleWith(v)), plane.angleWith(v),
                1e-10);
        v = new Vector(3, -5, -13);
        assertEquals("Wrong plane-to-vector angle",
                halfPi, plane.angleWith(v), 1e-10);
    }

    /**
     * Tests the behavior of the {@code angleWith(Vector)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__angleWith_nullVector() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.angleWith(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code nearestPoint(Point)} method
     */
    public void testMethod__nearestPoint_Point() {
        Point p0 = new Point(-3, -5, -7);
        Vector v1 = new Vector(2, 2, 5);
        Vector v2 = new Vector(7, -1, 2);
        DirectedPlane plane = new DirectedPlane(p0, v1, v2);
        Vector norm = plane.getNormal();
        Point p;
        
        p = v1.scale(2.5).sum(v2.scale(-1)).endPoint(p0);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(3.25).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-3.25).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(0.25).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-0.25).endPoint(p))),
                1e-10);
        
        p = v1.scale(-7.125).sum(v2.scale(-3)).endPoint(p0);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(3.75).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-3.75).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(0.75).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-0.75).endPoint(p))),
                1e-10);
        
        p = v1.scale(2).sum(v2.scale(5)).endPoint(p0);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(1.75).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-1.75).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(0.5).endPoint(p))),
                1e-10);
        assertEquals("Wrong nearest point", 0,
                p.distanceTo(plane.nearestPoint(norm.scale(-0.5).endPoint(p))),
                1e-10);
    }

    /**
     * Tests the behavior of the {@code nearestPoint(Point)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__nearestPoint_nullPoint() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.nearestPoint(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code signedDistanceTo(Point)} method
     */
    public void testMethod__signedDistanceTo_Point() {
        Point p0 = new Point(-3, -5, -7);
        Vector v1 = new Vector(2, 2, 5);
        Vector v2 = new Vector(7, -1, 2);
        DirectedPlane plane = new DirectedPlane(p0, v1, v2);
        Vector norm = plane.getNormal();
        Point p;
        
        p = v1.scale(2.5).sum(v2.scale(-1)).endPoint(p0);
        assertEquals("Wrong (signed) distance to a point", 3.25,
                plane.signedDistanceTo(norm.scale(3.25).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -3.25,
                plane.signedDistanceTo(norm.scale(-3.25).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", 0.25,
                plane.signedDistanceTo(norm.scale(0.25).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -0.25,
                plane.signedDistanceTo(norm.scale(-0.25).endPoint(p)), 1e-10);
        
        p = v1.scale(-7.125).sum(v2.scale(-3)).endPoint(p0);
        assertEquals("Wrong (signed) distance to a point", 3.75,
                plane.signedDistanceTo(norm.scale(3.75).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -3.75,
                plane.signedDistanceTo(norm.scale(-3.75).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", 0.75,
                plane.signedDistanceTo(norm.scale(0.75).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -0.75,
                plane.signedDistanceTo(norm.scale(-0.75).endPoint(p)), 1e-10);
        
        p = v1.scale(2).sum(v2.scale(5)).endPoint(p0);
        assertEquals("Wrong (signed) distance to a point", 1.75,
                plane.signedDistanceTo(norm.scale(1.75).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -1.75,
                plane.signedDistanceTo(norm.scale(-1.75).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", 0.5,
                plane.signedDistanceTo(norm.scale(0.5).endPoint(p)), 1e-10);
        assertEquals("Wrong (signed) distance to a point", -0.5,
                plane.signedDistanceTo(norm.scale(-0.5).endPoint(p)), 1e-10);
    }

    /**
     * Tests the behavior of the {@code signedDistanceTo(Point)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__signedDistanceTo_nullPoint() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.signedDistanceTo(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code signedAngleWith(Vector)} method
     */
    public void testMethod__signedAngleWith_Vector() {
        DirectedPlane plane
                = new DirectedPlane(new Point(2, 17, 7), new Vector(-3, 5, 13));
        Vector normal = plane.getNormal();
        double halfPi = Math.PI / 2;
        Vector v;
        
        v = new Vector(1, 0, 0);
        /*
        return (Math.PI / 2) - getNormal().angleWith(v);
         */
        assertEquals("Wrong plane-to-vector angle",
                halfPi - normal.angleWith(v), plane.signedAngleWith(v), 1e-10);
        v = new Vector(0, 1, 0);
        assertEquals("Wrong plane-to-vector angle",
                halfPi - normal.angleWith(v), plane.signedAngleWith(v), 1e-10);
        v = new Vector(0, 0, 1);
        assertEquals("Wrong plane-to-vector angle",
                halfPi - normal.angleWith(v), plane.signedAngleWith(v), 1e-10);
        v = new Vector(5, 1, 11);
        assertEquals("Wrong plane-to-vector angle",
                halfPi - normal.angleWith(v), plane.signedAngleWith(v), 1e-10);
        v = new Vector(3, -5, -13);
        assertEquals("Wrong plane-to-vector angle",
                -halfPi, plane.signedAngleWith(v), 1e-10);
    }

    /**
     * Tests the behavior of the {@code signedAngleWith(Vector)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__signedAngleWith_nullVector() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.signedAngleWith(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code isDirectedToward(Point)} method
     */
    public void testMethod__isDirectedToward_Point() {
        Point p0 = new Point(-3, -5, -7);
        Vector v1 = new Vector(2, 2, 5);
        Vector v2 = new Vector(7, -1, 2);
        DirectedPlane plane = new DirectedPlane(p0, v1, v2);
        Vector norm = plane.getNormal();
        Point p;
        
        p = v1.scale(2.5).sum(v2.scale(-1)).endPoint(p0);
        assertTrue("Plane not directed correctly", 
                plane.isDirectedToward(norm.scale(3.25).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-3.25).endPoint(p)));
        assertTrue("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(0.25).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-0.25).endPoint(p)));
        
        p = v1.scale(-7.125).sum(v2.scale(-3)).endPoint(p0);
        assertTrue("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(3.75).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-3.75).endPoint(p)));
        assertTrue("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(0.75).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-0.75).endPoint(p)));
        
        p = v1.scale(2).sum(v2.scale(5)).endPoint(p0);
        assertTrue("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(1.75).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-1.75).endPoint(p)));
        assertTrue("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(0.5).endPoint(p)));
        assertFalse("Plane not directed correctly",
                plane.isDirectedToward(norm.scale(-0.5).endPoint(p)));

    }

    /**
     * Tests the behavior of the {@code isDirectedToward(Point)} method when the
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__isDirectedToward_nullPoint() {
        DirectedPlane plane
                = new DirectedPlane(Point.ORIGIN, new Vector(1, 0, 0));
        
        try {
            plane.isDirectedToward(null);
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
        TestSuite tests = new TestSuite(DirectedPlaneTests.class);
        
        tests.setName("DirectedPlane Tests");
        
        return tests;
    }
}
