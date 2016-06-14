/*
 * Reciprocal Net Project
 *
 * CoordinateSystemTests.java
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
 * {@code CoordinateSystem} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CoordinateSystemTests extends TestCase {

    /**
     * Initializes a new {@code CoordinateSystemTests} to run the named test
     * @param  testName the name of the test to run
     */
    public CoordinateSystemTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the {@code Point/Vector[]} constructor,
     * along with the {@code getOrigin()}, {@code getVectors()}, and
     * {@code getCoordinatePlanes()} methods.
     */
    public void testConstructor__Point_VectorArray() {
        
        // Test an arbitrary triclinic system
        Point origin = new Point(2, 5, 7);
        Vector[] vectors = new Vector[] { new Vector(-1, 19, 5),
                new Vector(3, 3, 23), new Vector(17, 1, -3) };
        CoordinateSystem system = new CoordinateSystem(origin, vectors);
        
        assertEquals("Coordinate system has the wrong origin", origin,
                system.getOrigin());
        assertTrue("Coordinate system has the wrong vectors",
                Arrays.equals(vectors, system.getVectors()));

        performConsistencyTest(system);
        
        // Test an orthorhombic system
        origin = new Point(-3, 8, 5);
        vectors = new Vector[] { new Vector(17, 2, 3),
                new Vector(-2, 17, 0), null };
        vectors[2] = vectors[0].crossProduct(vectors[1]);
        
        system = new CoordinateSystem(origin, vectors);
        
        assertEquals("Coordinate system has the wrong origin", origin,
                system.getOrigin());
        assertTrue("Coordinate system has the wrong vectors",
                Arrays.equals(vectors, system.getVectors()));

        performConsistencyTest(system);
        
    }

    /**
     * Tests the behavior of the {@code Point/Vector[]} constructor when the
     * first argument is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testConstructor__nullPoint_VectorArray() {
        try {
            new CoordinateSystem(null, new Vector[] { new Vector(1, 0, 0),
                    new Vector(0, 1, 0), new Vector(0, 0, 1) });
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the {@code Point/Vector[]} constructor when the
     * second argument is {@code null} or contains a {@code null} element;
     * a {@code NullPointerException} is expected
     */
    public void testConstructor__Point_nullVectorArray() {
        try {
            new CoordinateSystem(Point.ORIGIN, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    null, new Vector(0, 1, 0), new Vector(0, 0, 1)
            });
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    new Vector(1, 0, 0), null, new Vector(0, 0, 1)
            });
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    new Vector(1, 0, 0), new Vector(0, 1, 0), null
            });
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code Point/Vector[]} constructor when the
     * provided vectors are coplanar or in left-handed sequence; an
     * {@code IllegalArgumentException} is expected
     */
    public void testConstructor__Point_badVectorArray() {
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    new Vector(1, 0, 0), new Vector(0, 1, 0),
                    new Vector(-1, -1, 0) });
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    new Vector(1, 0, 0), new Vector(0, 0, 1),
                    new Vector(0, 1, 0) });
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            new CoordinateSystem(Point.ORIGIN, new Vector[] {
                    new Vector(3, -2, 53), new Vector(-1, 37, 7),
                    new Vector(23, 1, 5) });
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the hexary constructor,
     * along with the {@code getOrigin()}, {@code getVectors()}, and
     * {@code getCoordinatePlanes()} methods.
     */
    public void testConstructor__double_double_double_double_double_double() {
        double a, b, c, cosAlpha, cosBeta, cosGamma;
        
        // Test a general triclinic system
        CoordinateSystem system = new CoordinateSystem(
                a = 5.25, b = 7.875, c = 11.1625,
                cosAlpha = -0.1, cosBeta = -0.2, cosGamma = -0.15);
        Vector[] vectors = system.getVectors();
        
        assertEquals("Coordinate system has the wrong origin", 0.0,
                Point.ORIGIN.distanceTo(system.getOrigin()), 1e-10);
        assertEquals("Coordinate system has wrong a axis length",
                a, vectors[0].length(), 1e-10);
        assertEquals("Coordinate system has wrong b axis length",
                b, vectors[1].length(), 1e-10);
        assertEquals("Coordinate system has wrong c axis length",
                c, vectors[2].length(), 1e-10);
        assertEquals("Coordinate system has wrong alpha angle",
                cosAlpha, Math.cos(vectors[1].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong beta angle",
                cosBeta, Math.cos(vectors[0].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong gamma angle",
                cosGamma, Math.cos(vectors[0].angleWith(vectors[1])), 1e-10);
        
        performConsistencyTest(system);
        
        // Test a monoclinic system
        system = new CoordinateSystem(
                a = 12.25, b = 3.875, c = 17.1625,
                cosAlpha = 0, cosBeta = .125, cosGamma = 0);
        vectors = system.getVectors();
        
        assertEquals("Coordinate system has the wrong origin", 0.0,
                Point.ORIGIN.distanceTo(system.getOrigin()), 1e-10);
        assertEquals("Coordinate system has wrong a axis length",
                a, vectors[0].length(), 1e-10);
        assertEquals("Coordinate system has wrong b axis length",
                b, vectors[1].length(), 1e-10);
        assertEquals("Coordinate system has wrong c axis length",
                c, vectors[2].length(), 1e-10);
        assertEquals("Coordinate system has wrong alpha angle",
                cosAlpha, Math.cos(vectors[1].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong beta angle",
                cosBeta, Math.cos(vectors[0].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong gamma angle",
                cosGamma, Math.cos(vectors[1].angleWith(vectors[2])), 1e-10);
        
        performConsistencyTest(system);
        
        // Test an orthorhombic system
        system = new CoordinateSystem(
                a = 7.25, b = 3.875, c = 4.1625,
                cosAlpha = 0, cosBeta = 0, cosGamma = 0);
        vectors = system.getVectors();
        
        assertEquals("Coordinate system has the wrong origin", 0.0,
                Point.ORIGIN.distanceTo(system.getOrigin()), 1e-10);
        assertEquals("Coordinate system has wrong a axis length",
                a, vectors[0].length(), 1e-10);
        assertEquals("Coordinate system has wrong b axis length",
                b, vectors[1].length(), 1e-10);
        assertEquals("Coordinate system has wrong c axis length",
                c, vectors[2].length(), 1e-10);
        assertEquals("Coordinate system has wrong alpha angle",
                cosAlpha, Math.cos(vectors[1].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong beta angle",
                cosBeta, Math.cos(vectors[0].angleWith(vectors[2])), 1e-10);
        assertEquals("Coordinate system has wrong gamma angle",
                cosGamma, Math.cos(vectors[1].angleWith(vectors[2])), 1e-10);
        
        performConsistencyTest(system);
    }

    /**
     * Tests the behavior of the hexary constructor when one of the axial
     * lengths is less than or equal to zero; an
     * {@code IllegalArgumentException} is expected
     */
    public void testConstructor__6double__illegalAxis() {
        try {
            new CoordinateSystem(0, 5, 6, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(-1, 5, 6, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(7, 0, 6, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(7, -2, 6, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(7, 5, 0, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(7, 5, -1.5, 0, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the hexary constructor when one of the angle
     * cosines is out of range or if they are collectively too large; an
     * {@code IllegalArgumentException} is expected
     */
    public void testConstructor__6double__illegalAngle() {
        
        // cosAlpha out of range
        try {
            new CoordinateSystem(3, 5, 7, 1.1, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 1, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -1, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -1.1, 0, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        
        // cosBeta out of range
        try {
            new CoordinateSystem(3, 5, 7, 0, 1.1, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, 1, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, -1, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, -1.1, 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        
        // cosGamma out of range
        try {
            new CoordinateSystem(3, 5, 7, 0, 0, 1.1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, 0, 1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, 0, -1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, 0, 0, -1.1);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        
        // angles collectively too large
        try {
            new CoordinateSystem(3, 5, 7, -.5, -.5, -.51);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -.5, -.51, -.5);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -.51, -.5, -.5);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -.342, -.342, -.866);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -.342, -.866, -.342);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new CoordinateSystem(3, 5, 7, -.866, -.342, -.342);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the insternal consistency of a {@code CoordinateSystem} object;
     * specifically, tests that its coordinate planes each contain the
     * origin and the appropriate of the axial vectors, and that their normals
     * are directed to the correct side
     * 
     * @param  system a the {@code CoordinateSystem} to test
     */
    private void performConsistencyTest(CoordinateSystem system) {
        Point origin = system.getOrigin();
        Vector[] vectors = system.getVectors();
        Planar[] planes = system.getCoordinatePlanes();
        
        for (int i = 0; i < 3; i++) {
            assertEquals("Coordinate plane " + i
                    + " does not contain the origin", 0.0,
                    planes[i].nearestPoint(origin).distanceTo(origin),
                    1e-10);
            for (int j = 1; j < 3; j++) {
                assertEquals("Coordinate plane " + i
                        + " is not parallel to coordinate vector " + j,
                        0.0, planes[i].angleWith(vectors[(i + j) % 3]), 1e-10);
            }
            assertTrue("Coordinate plane " + i
                   + " has normal directed away from vector " + i,
                   planes[i].getNormal().angleWith(vectors[i]) < (Math.PI / 2));
        }
    }

    /**
     * Tests method for 'org.recipnet.common.geometry.CoordinateSystem.getReciprocal()'
     */
    public void testMethod__getReciprocal() {
        
        // Test a general triclinic system
        performReciprocalCoordinateTest(
                new CoordinateSystem(5.25, 7.875, 11.1625, -0.1, -0.2, -0.15));
        
        // Test a monoclinic system
        performReciprocalCoordinateTest(
                new CoordinateSystem(12.25, 3.875, 17.1625, 0, .125, 0));
        
        // Test an orthorhombic system
        performReciprocalCoordinateTest(
                new CoordinateSystem(7.25, 3.875, 4.1625, 0, 0, 0));
        
        // Test a system with a general origin (and triclinic vectors) 
        performReciprocalCoordinateTest(new CoordinateSystem(
                new Point(-1, 3, 2),
                new Vector[] { new Vector(2, 7, 31), new Vector(17, 5, 0),
                        new Vector(-2, 19, 3) }));
    }

    /**
     * Tests the correct operation of the {@code getReciprocal()} method by
     * verifying that the resulting coordinate system has the same origin as the
     * original and that all the dot product relationships between coordinate
     * vectors are correct
     * 
     * @param  system the {@code CoordinateSystem} to test
     */
    private void performReciprocalCoordinateTest(CoordinateSystem system) {
        CoordinateSystem reciprocal = system.getReciprocal();
        Vector[] vectors = system.getVectors();
        Vector[] recipVectors = reciprocal.getVectors();

        assertEquals("Reciprocal cell origin is incorrect", 0.0,
                system.getOrigin().distanceTo(reciprocal.getOrigin()), 1e-10);
        for (int i = 0; i < vectors.length; i++) {
            for (int j = 0; j < vectors.length; j++) {
                assertEquals("Wrong product of real vector " + i
                        + " with reciprocal vector " + j, (i == j) ? 1 : 0,
                        vectors[i].dotProduct(recipVectors[j]), 1e-10);
            }
        }
    }

    /**
     * Tests the behavior of the {@code coordinatesOf(Point)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__coordinatesOf_nullPoint() {
        try {
            new CoordinateSystem(5, 6, 7, .1, .2, .3).coordinatesOf(
                    (Point) null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests {@code CoordinateSystem}'s facility for managing relative point
     * coordinates
     */
    public void testFeature__PointCoordinates() {
        
        // Test a general triclinic system
        performPointCoordinateTest(
                new CoordinateSystem(5.25, 7.875, 11.1625, -0.1, -0.2, -0.15));
        
        // Test a monoclinic system
        performPointCoordinateTest(
                new CoordinateSystem(12.25, 3.875, 17.1625, 0, .125, 0));
        
        // Test an orthorhombic system
        performPointCoordinateTest(
                new CoordinateSystem(7.25, 3.875, 4.1625, 0, 0, 0));
        
        // Test a system with a general origin (and triclinic vectors) 
        performPointCoordinateTest(new CoordinateSystem(
                new Point(-1, 3, 2),
                new Vector[] { new Vector(2, 7, 31), new Vector(17, 5, 0),
                        new Vector(-2, 19, 3) }));
    }

    /**
     * Performs several variations of a test that the specified coordinate
     * system's {@code pointFor(double, double, double)} and
     * {@code coordinatesOf(Point)} methods are mutually consistent
     *  
     * @param  system the {@code CoordinateSystem} to test
     */
    private void performPointCoordinateTest(CoordinateSystem system) {
        double x, y, z;
        double[] coords;
        
        coords = system.coordinatesOf(system.pointFor(x = 3, y = -5, z = 2));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(system.pointFor(x = 2.5, y = 0, z = 1));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(system.pointFor(x = 0, y = 0, z = 0));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(
                system.pointFor(x = 13, y = 11, z = 1e-17));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);
    }
    
    /**
     * Tests {@code CoordinateSystem}'s facility for managing relative vector
     * coordinates
     */
    public void testFeature__VectorCoordinates() {
        
        // Test a general triclinic system
        performVectorCoordinateTest(
                new CoordinateSystem(5.25, 7.875, 11.1625, -0.1, -0.2, -0.15));
        
        // Test a monoclinic system
        performVectorCoordinateTest(
                new CoordinateSystem(12.25, 3.875, 17.1625, 0, .125, 0));
        
        // Test an orthorhombic system
        performVectorCoordinateTest(
                new CoordinateSystem(7.25, 3.875, 4.1625, 0, 0, 0));
        
        // Test a system with a general origin (and triclinic vectors) 
        performVectorCoordinateTest(new CoordinateSystem(
                new Point(-1, 3, 2),
                new Vector[] { new Vector(2, 7, 31), new Vector(17, 5, 0),
                        new Vector(-2, 19, 3) }));
    }

    /**
     * Performs several variations of a test that the specified coordinate
     * system's {@code pointFor(double, double, double)} and
     * {@code coordinatesOf(Point)} methods are mutually consistent
     *  
     * @param  system the {@code CoordinateSystem} to test
     */
    private void performVectorCoordinateTest(CoordinateSystem system) {
        double x, y, z;
        double[] coords;
        
        coords = system.coordinatesOf(system.vectorFor(x = 3, y = -5, z = 2));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(system.vectorFor(x = 2.5, y = 0, z = 1));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(system.vectorFor(x = 0, y = 0, z = 0));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);

        coords = system.coordinatesOf(
                system.vectorFor(x = 13, y = 11, z = 1e-17));
        assertEquals("x Coordinate not recovered correctly",
                x, coords[0], 1e-10);
        assertEquals("y Coordinate not recovered correctly",
                y, coords[1], 1e-10);
        assertEquals("z Coordinate not recovered correctly",
                z, coords[2], 1e-10);
    }

    /**
     * Tests the behavior of the {@code coordinatesOf(Vector)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__coordinatesOf_nullVector() {
        try {
            new CoordinateSystem(5, 6, 7, .1, .2, .3).coordinatesOf(
                    (Vector) null);
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
        TestSuite tests = new TestSuite(CoordinateSystemTests.class);
        
        tests.setName("CoordinateSystem Tests");
        
        return tests;
    }
}
