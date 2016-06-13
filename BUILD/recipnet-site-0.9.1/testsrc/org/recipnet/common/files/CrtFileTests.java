/*
 * Reciprocal Net Project
 *
 * CrtFileTests.java
 *
 * Dec 23, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;

import junit.framework.TestCase;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code CrtFile}
 * class
 * 
 * @author jobollin
 * @version 1.0
 */
public class CrtFileTests extends TestCase {

    /**
     * Initializes a new {@code CrtFileTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CrtFileTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the constructor; it should assign the
     * specified model to the new {@code CrtFile}, and should give it an
     * empty name
     */
    public void testConstructor__MolecularModel() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        CrtFile<Atom> crt = new CrtFile<Atom>(model);
        
        assertTrue("The model was copied", crt.getModel() == model);
        assertEquals("The CRT was not assigned an empty name", "",
                crt.getName());
    }
    
    /**
     * Tests the behavior of the constructor when its argument is {@code null};
     * a {@code NullPointerException} is expected
     */
    public void testConstructor__nullMolecularModel() {
        try {
            new CrtFile<Atom>(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the joint behavior of the {@code setName(String)} and
     * {@code getName()} methods; the latter should always return a string
     * equal to the one most recently set by the former
     */
    public void testFeature__getName__setName_String() {
        CrtFile<Atom> crt = new CrtFile<Atom>(
                new MolecularModel<Atom, Bond<Atom>>());
        String s;
        
        crt.setName(s = "Farvegnugen");
        assertEquals("Wrong CRT name", s, crt.getName());
        
        crt.setName(s = "blitzkrieg");
        assertEquals("Wrong CRT name", s, crt.getName());
    }
    
    /**
     * Tests the behavior of the {@code setName(String)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__setName_nullString() {
        CrtFile<Atom> crt = new CrtFile<Atom>(
                new MolecularModel<Atom, Bond<Atom>>());
        
        try {
            crt.setName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
}
