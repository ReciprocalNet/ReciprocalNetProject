/*
 * Reciprocal Net Project
 *
 * BondTests.java
 *
 * Dec 19, 2005: jobollin wrote first draft
 */
package org.recipnet.common.molecule;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.recipnet.common.Element;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code Bond}
 * class
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class BondTests extends TestCase {

    /**
     * Initializes a new {@code BondTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public BondTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the binary (also the only) constructor
     */
    public void testConstructor__Atom_Atom() {
        Atom atom1 = new Atom("A", Element.CARBON, 0, 0, 0, null);
        Atom atom2 = new Atom("B", Element.HYDROGEN, 1, 1, 1, null);
        Bond<Atom> bond = new Bond<Atom>(atom1, atom2);
        
        assertEquals("The wrong first atom was reported", atom1,
                bond.getAtom1());
        assertEquals("The wrong second atom was reported", atom2,
                bond.getAtom2());
        
        bond = new Bond<Atom>(atom2, atom1);
        assertEquals("The wrong first atom was reported", atom2,
                bond.getAtom1());
        assertEquals("The wrong second atom was reported", atom1,
                bond.getAtom2());
    }

    /**
     * Tests the behavior of the binary (also the only) constructor when its
     * first argument is {@code null}; an {@code IllegalArgumentException} is
     * expected.
     */
    public void testConstructor__nullAtom_Atom() {
        try {
            new Bond<Atom>(
                    null, new Atom("Foo", Element.ALUMINUM, 0, 0, 0, "a"));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the binary (also the only) constructor when its
     * second argument is {@code null}; an {@code IllegalArgumentException} is
     * expected.
     */
    public void testConstructor__Atom_nullAtom() {
        try {
            new Bond<Atom>(
                    new Atom("Foo", Element.ALUMINUM, 0, 0, 0, "a"), null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the binary (also the only) constructor when its
     * arguments are the same object; an {@code IllegalArgumentException} is
     * expected
     */
    public void testConstructor__Atom_Atom__equalAtoms() {
        Atom atom = new Atom("Bar", null, 0, 0, 0, null);
        
        try {
            new Bond<Atom>(atom, atom);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code includesHydrogen} method, which should
     * return true if and only if one of the {@code Bond} on which it is invoked
     * has an atom with hydrogen as its element
     */
    public void testMethod__includesHydrogen() {
        Bond<Atom> bond;
        Atom atom1;
        
        for (Element el1 : Element.values()) {
            atom1 = new Atom("Atom 1", el1, 0, 0, 0, "Foo");
            for (Element el2 : Element.values()) {
                Atom atom2 = new Atom("Atom 2", el2, 0, 0, 0, "Bar");
                
                bond = new Bond<Atom>(atom1, atom2);
                assertEquals("Hydrogen inclusion not reported correctly",
                        (el1 == Element.HYDROGEN) || (el2 == Element.HYDROGEN),
                        bond.includesHydrogen());
            }
            
            bond = new Bond<Atom>(
                    atom1, new Atom("Atom 2", null, 0, 0, 0, "Baz"));
            assertEquals("Hydrogen inclusion not reported correctly",
                    (el1 == Element.HYDROGEN), bond.includesHydrogen());
        }
        
        atom1 = new Atom("Atom 1", null, 0, 0, 0, "Ni!");
        for (Element el2 : Element.values()) {
            Atom atom2 = new Atom("Atom 2", el2, 0, 0, 0, "Bat");
            
            bond = new Bond<Atom>(atom1, atom2);
            assertEquals("Hydrogen inclusion not reported correctly",
                    el2 == Element.HYDROGEN, bond.includesHydrogen());
        }
        
        bond = new Bond<Atom>(
                atom1, new Atom("Atom 2", null, 0, 0, 0, "Shebang"));
        assertFalse("Hydrogen inclusion not reported correctly",
                bond.includesHydrogen());
    }

    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(BondTests.class);
        
        tests.setName("Bond Tests");
        
        return tests;
    }
}
