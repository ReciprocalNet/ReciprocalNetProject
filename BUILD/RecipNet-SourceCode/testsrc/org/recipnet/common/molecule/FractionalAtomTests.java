/*
 * Reciprocal Net Project
 *
 * FractionalAtomTests.java
 *
 * Dec 20, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import org.recipnet.common.Element;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Point;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} exercising the behavior of the
 * {@code FractionalAtom} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class FractionalAtomTests extends TestCase {
    
    private CoordinateSystem unitCell;
    private FractionalAtom testAtom;
    private AtomicMotionFactory factory;

    /**
     * Initializes a new {@code FractionalAtomTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public FractionalAtomTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this {@code TestCase} to run a test
     * 
     * @throws  Exception if the superclass' {@code setUp()} method throws one
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        unitCell = new CoordinateSystem(7, 8, 11, 0.05, 0.125, 0.0625);
        testAtom = new FractionalAtom("Test", Element.CARBON, .1, .2, .3,
                unitCell, "test|1_555");
        factory = new AtomicMotionFactory(unitCell);
    }
    
    /**
     * Tests the normal behavior of seven-argument constructor; along with that
     * of the property accessor methods
     */
    public void testConstructor__String_Element_3double_CoordinateSystem_String() {
        String label, siteTag;
        Element el;
        double x, y, z;
        CoordinateSystem cell;
        FractionalAtom atom;
        
        atom = new FractionalAtom(label = "Atom 1", el = Element.AMERICIUM,
                x = 0.2, y = 0.3, z = 0.4, cell = unitCell, siteTag = "elephant");
        verifyAtom(atom, label, el, new double[] {x, y, z}, cell, siteTag);
        
        atom = new FractionalAtom(label = "Monkey Wrench",
                el = Element.BERKELIUM,
                x = 0.2, y = 0.3, z = 0.4,
                cell = new CoordinateSystem(17, 12, 19, 0, 0.03, 0),
                siteTag = "Smallberries");
        verifyAtom(atom, label, el, new double[] {x, y, z}, cell, siteTag);
        
        atom = new FractionalAtom(label = "Bigboote",
                el = Element.CALIFORNIUM,
                x = -0.25, y = 3.7, z = 0.1,
                cell = new CoordinateSystem(8, 10, 13, 0, 0.0, 0),
                siteTag = "Yaya");
        verifyAtom(atom, label, el, new double[] {x, y, z}, cell, siteTag);
        
        atom = new FractionalAtom(label = "Penny",
                el = null,
                x = 1, y = 2, z = 3,
                cell = new CoordinateSystem(8, 10, 13, -.1, -.05, -.075),
                siteTag = "spark");
        verifyAtom(atom, label, el, new double[] {x, y, z}, cell, siteTag);
        
        atom = new FractionalAtom(label = "Bonzai",
                el = Element.CHROMIUM,
                x = 1, y = 2, z = 3,
                cell = new CoordinateSystem(6, 11, 15, -.2, -.15, 0),
                siteTag = null);
        verifyAtom(atom, label, el, new double[] {x, y, z}, cell, siteTag);
    }

    /**
     * Tests the normal behavior of seven-argument constructor; along with that
     * of the property accessor methods
     */
    public void testConstructor__String_Element_doubleArray_CoordinateSystem_String() {
        String label, siteTag;
        Element el;
        double[] coords;
        CoordinateSystem cell;
        FractionalAtom atom;
        
        atom = new FractionalAtom(label = "Cook", el = Element.IRON,
                coords = new double[] {0.95, 1, -12.2}, cell = unitCell,
                siteTag = "guitar");
        verifyAtom(atom, label, el, coords, cell, siteTag);
        
        atom = new FractionalAtom(label = "Field",
                el = Element.COPPER,
                coords = new double[] {0.5, 0.6, 0.7},
                cell = new CoordinateSystem(12, 12, 12, 0.03, 0.03, 0.03),
                siteTag = "fruit salad");
        verifyAtom(atom, label, el, coords, cell, siteTag);
        
        atom = new FractionalAtom(label = "Fatt",
                el = Element.MOLYBDENUM,
                coords = new double[] {1.0, 0, -1},
                cell = new CoordinateSystem(13, 13, 13, 0, 0.0, 0),
                siteTag = "pillow");
        verifyAtom(atom, label, el, coords, cell, siteTag);
        
        atom = new FractionalAtom(label = "Dorothy",
                el = Element.NICKEL,
                coords = new double[] {2, .75, -.1},
                cell = new CoordinateSystem(11, 12, 3, -.12, -.32, .2),
                siteTag = null);
        verifyAtom(atom, label, el, coords, cell, siteTag);
        
        atom = new FractionalAtom(label = "Wags",
                el = null,
                coords = new double[] {.1, .3, .65},
                cell = new CoordinateSystem(5, 4, 7, 0, .03, -.05),
                siteTag = "bone");
        verifyAtom(atom, label, el, coords, cell, siteTag);
    }

    /**
     * Verifies that the specified {@code FractionalAtom} has the specified
     * attributes
     * 
     * @param  atom the {@code FractionalAtom} to test
     * @param  label the expected atom label {@code String}
     * @param  el the expected {@code Element} for the atom (possibly
     *         {@code null})
     * @param  coords a {@code double[]} of the fractional atomic coordinates
     * @param  cell the {@code CoordinateSystem} serving as the reference unit
     *         cell for the atom's fractional coordinates
     * @param  siteTag the expected site tag {@code String} (possibly
     *         {@code null})
     */
    private void verifyAtom(FractionalAtom atom, String label, Element el,
            double[] coords, CoordinateSystem cell, String siteTag) {
        assertEquals("Wrong atom label", label, atom.getLabel());
        assertEquals("Wrong element", el, atom.getElement());
        assertEquals("Wrong fractional x coordinate", coords[0],
                atom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", coords[1],
                atom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", coords[2],
                atom.getFractionalCoordinates()[2], 1e-10);
        assertEquals("Wrong reference cell", cell, atom.getReferenceCell());
        assertEquals("Wrong site tag", siteTag, atom.getSiteTag());
        assertEquals("Atomic position deviation", 0.0,
                cell.pointFor(coords[0], coords[1], coords[2]).distanceTo(
                        atom.getPosition()), 1e-10);
    }

    /**
     * Tests the {@code getAtomicMotion()} and
     * {@code setAtomicMotion(AtomicMotion)} methods to ensure that the motion
     * set via the former is correctly read back via the latter
     */
    public void testFeature__getAtomicMotion__setAtomicMotion() {
        AtomicMotion motion = factory.motionForIsotropicU(0.05);
        
        assertFalse("Motion already set",
                motion.equals(testAtom.getAtomicMotion()));
        testAtom.setAtomicMotion(motion);
        assertEquals("Motion not set correctly", motion,
                testAtom.getAtomicMotion());
        testAtom.setAtomicMotion(null);
        assertNull("Motion not nulled correctly", testAtom.getAtomicMotion());
    }

    /**
     * Tests the joint behavior of the {@code moveToFractionalCoords(double[])},
     * {@code getFractionalCoordinates()}, and {@code getPosition()} methods
     */
    public void testFeature__moveToFractionalCoords_doubleArray__getFractionalCoordinates__getPosition() {
        double[] coords = new double[] {.67, .85, .2};
        
        testAtom.moveToFractionalCoords(coords);
        assertEquals("Wrong fractional x coordinate", coords[0],
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", coords[1],
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", coords[2],
                testAtom.getFractionalCoordinates()[2], 1e-10);
        assertEquals("Position deviation", 0.0,
                unitCell.pointFor(coords[0], coords[1], coords[2]).distanceTo(
                        testAtom.getPosition()), 1e-10);
        
        testAtom.moveToFractionalCoords(coords = new double[] {0, -3.5, 1.4});
        assertEquals("Wrong fractional x coordinate", coords[0],
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", coords[1],
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", coords[2],
                testAtom.getFractionalCoordinates()[2], 1e-10);
        assertEquals("Position deviation", 0.0,
                unitCell.pointFor(coords[0], coords[1], coords[2]).distanceTo(
                        testAtom.getPosition()), 1e-10);
    }

    /**
     * Tests the behavior of the {@code moveToFractionalCoords(double[])} method
     * when its argument is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testMethod__moveToFractionalCoords_nulldoubleArray() {
        try {
            testAtom.moveToFractionalCoords(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the joint behavior of the
     * {@code moveToFractionalCoords(double, double, double)},
     * {@code getFractionalCoordinates()}, and {@code getPosition()} methods
     */
    public void testFeature__moveToFractionalCoords_3double__getFractionalCoordinates__getPosition() {
        double x, y, z;
        
        testAtom.moveToFractionalCoords(x = .33, y = 0, z = -1.7);
        assertEquals("Wrong fractional x coordinate", x,
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", y,
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", z,
                testAtom.getFractionalCoordinates()[2], 1e-10);
        assertEquals("Position deviation", 0.0,
                unitCell.pointFor(x, y, z).distanceTo(testAtom.getPosition()),
                1e-10);
        
        testAtom.moveToFractionalCoords(x = 7.1, y = -3.2, z = 0.5);
        assertEquals("Wrong fractional x coordinate", x,
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", y,
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", z,
                testAtom.getFractionalCoordinates()[2], 1e-10);
        assertEquals("Position deviation", 0.0,
                unitCell.pointFor(x, y, z).distanceTo(testAtom.getPosition()),
                1e-10);
    }

    /**
     * Tests the joint behavior of the {@code moveTo(Point)},
     * {@code getPosition()}, and {@code getFractionalCoordinates()} methods
     */
    public void testFeature__moveTo_Point__getPosition__getFractionalCoordinates() {
        Point p;
        double[] coords;
        
        testAtom.moveTo(p = new Point(73, -12, 8));
        coords = testAtom.getReferenceCell().coordinatesOf(p);
        assertEquals("Position deviation", 0.0,
                p.distanceTo(testAtom.getPosition()), 1e-10);
        assertEquals("Wrong fractional x coordinate", coords[0],
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", coords[1],
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", coords[2],
                testAtom.getFractionalCoordinates()[2], 1e-10);
        
        testAtom.moveTo(p = new Point(13, 0, -5));
        coords = testAtom.getReferenceCell().coordinatesOf(p);
        assertEquals("Position deviation", 0.0,
                p.distanceTo(testAtom.getPosition()), 1e-10);
        assertEquals("Wrong fractional x coordinate", coords[0],
                testAtom.getFractionalCoordinates()[0], 1e-10);
        assertEquals("Wrong fractional y coordinate", coords[1],
                testAtom.getFractionalCoordinates()[1], 1e-10);
        assertEquals("Wrong fractional z coordinate", coords[2],
                testAtom.getFractionalCoordinates()[2], 1e-10);
    }

    /**
     * Tests the behavior of the {@code moveTo(Point)} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__moveTo_nullPoint() {
        try {
            testAtom.moveTo(null);
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
        TestSuite tests = new TestSuite(FractionalAtomTests.class);
        
        tests.setName("FractionalAtom Tests");
        
        return tests;
    }
}
