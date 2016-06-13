/*
 * Reciprocal Net Project
 *
 * AtomTests.java
 *
 * Dec 19, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.recipnet.common.Element;
import org.recipnet.common.geometry.Point;

/**
 * A JUnit {@code TestCase} exercising the behavior of the {@code Atom} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class AtomTests extends TestCase {

    /**
     * Initializes a new {@code AtomTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public AtomTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior six-argument constructor, along with the
     * various getXXX() methods
     */
    public void testConstructor__String_Element_3double_String() {
        Element el;
        String label, siteTag;
        double x, y, z;
        Atom atom;

        atom = new Atom(label = "Atom 1", el = Element.ANTIMONY, x = 1,
                y = 2, z = 3, siteTag = "foo|1_555");
        assertEquals("Wrong atom label", label, atom.getLabel());
        assertEquals("Wrong element", el, atom.getElement());
        assertTrue("Wrong position", Arrays.equals(new double[] {x, y, z},
                atom.getPosition().getCoordinates()));
        assertEquals("Wrong site tag", siteTag, atom.getSiteTag());
        
        atom = new Atom(label = "Atom 2", null, x = 1.1, y = 0, z = -3, null);
        assertEquals("Wrong atom label", label, atom.getLabel());
        assertNull("Wrong element", atom.getElement());
        assertTrue("Wrong position", Arrays.equals(new double[] {x, y, z},
                atom.getPosition().getCoordinates()));
        assertNull("Wrong site tag", atom.getSiteTag());
    }

    /**
     * Tests the behavior of the six-argument constructor when its first
     * argument is {@code null}
     */
    public void testConstructor__nullString_Element_3double_String() {
        try {
            new Atom(null, Element.CADMIUM, 0, 0, 0, "Foo");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior four-argument constructor, along with the
     * various getXXX() methods
     */
    public void testConstructor__String_Element_Point_String() {
        Element el;
        String label, siteTag;
        Point p;
        Atom atom;

        atom = new Atom(label = "Atom 1", el = Element.ANTIMONY,
                p = new Point(1, 2, 3), siteTag = "foo|1_555");
        assertEquals("Wrong atom label", label, atom.getLabel());
        assertEquals("Wrong element", el, atom.getElement());
        assertEquals("Wrong position", 0, p.distanceTo(atom.getPosition()),
                1e-10);
        assertEquals("Wrong site tag", siteTag, atom.getSiteTag());
        
        atom = new Atom(label = "Atom 2", null, p = new Point(1.1, 0, -3),
                null);
        assertEquals("Wrong atom label", label, atom.getLabel());
        assertNull("Wrong element", atom.getElement());
        assertEquals("Wrong position", 0, p.distanceTo(atom.getPosition()),
                1e-10);
        assertNull("Wrong site tag", atom.getSiteTag());
    }

    /**
     * Tests the behavior of the four-argument constructor when its first
     * argument is {@code null}
     */
    public void testConstructor__nullString_Element_Point_String() {
        try {
            new Atom(null, Element.CADMIUM, new Point(0, 0, 0), "Foo");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the four-argument constructor when its third
     * argument is {@code null}
     */
    public void testConstructor__String_Element_nullPoint_String() {
        try {
            new Atom("Pizza", Element.CADMIUM, null, "Foo");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code setSiteTag(String)} and
     * {@code getSiteTag()} methods
     */
    public void testFeature__getSiteTag__setSiteTag() {
        Atom atom = new Atom("Foo", Element.ARGON, 0, 0, 0, null);
        String tag;
        
        atom.setSiteTag(tag = "Hassenfeffer");
        assertEquals("Wrong site tag", tag, atom.getSiteTag());
        
        atom.setSiteTag(tag = "Sheboygan");
        assertEquals("Wrong site tag", tag, atom.getSiteTag());
        
        atom.setSiteTag(null);
        assertNull("Wrong site tag", atom.getSiteTag());
    }

    /**
     * Tests the behavior of the {@code isHydrogen()} method for all elements
     * and for {@code null}
     */
    public void testMethod__isHydrogen() {
        for (Element el : Element.values()) {
            Atom atom = new Atom("Atom", el, 0, 0, 0, null);
            
            assertEquals("Atom hydrogeneity incorrect",
                    (el == Element.HYDROGEN), atom.isHydrogen());
        }
        assertFalse("Atom with null element indicates it is hydrogen",
                new Atom("Atom", null, 0, 0, 0, null).isHydrogen());
    }

    /**
     * Tests the normal behavior of the {@code moveTo(Point)} method
     */
    public void testMethod__moveTo_Point() {
        Atom atom = new Atom("Atom", null, 0, 0, 0, null);
        Point p;
        
        atom.moveTo(p = new Point(3, 5, 7));
        assertEquals("Atom not correctly moved", 0,
                p.distanceTo(atom.getPosition()), 1e-10);
        
        atom.moveTo(p = new Point(-1, 2, 0));
        assertEquals("Atom not correctly moved", 0,
                p.distanceTo(atom.getPosition()), 1e-10);
    }

    /**
     * Tests the normal behavior of the {@code moveTo(Point)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__moveTo_nullPoint() {
        Atom atom = new Atom("Atom", null, 0, 0, 0, null);
        
        try {
            atom.moveTo(null);
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
        TestSuite tests = new TestSuite(AtomTests.class);
        
        tests.setName("Atom Tests");
        
        return tests;
    }
}
