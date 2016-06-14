/*
 * Reciprocal Net Project
 *
 * SiteCodeTests.java
 *
 * Dec 19, 2005: jobollin wrote first draft
 */
package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code SiteCode}
 * class.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class SiteCodeTests extends TestCase {

    /**
     * Initializes a {@code SiteCodeTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public SiteCodeTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the constructor, the {@code getSiteLabel()}
     * method, and the {@code getSymmetryCode()} method
     */
    public void testConstructor__String_SymmetryCode() {
        SymmetryCode symm = new SymmetryCode(17, new int[] {3, 5, 8});
        String label = "Mumphord";
        SiteCode code = new SiteCode(label, symm);
        
        assertEquals("The site code has the wrong label",
                label, code.getSiteLabel());
        assertEquals("The site code has the wrong symmetry code",
                symm, code.getSymmetryCode());
    }

    /**
     * Tests the behavior of the constructor when its first argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullString_SymmetryCode() {
        try {
            new SiteCode(null, SymmetryCode.valueOf("1_555"));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // Throw new NullPointerException
        }
    }
    
    /**
     * Tests the behavior of the constructor when its first argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__String_nullSymmetryCode() {
        try {
            new SiteCode("Foo", null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // Throw new NullPointerException
        }
    }
    
    /**
     * Tests the {@code equals()} method with a variety of objects expected to
     * be unequal to the target site code
     */
    public void testMethod__equals_Object__unequal() {
        SiteCode site = new SiteCode("Site", SymmetryCode.valueOf("3_555"));
    
        assertFalse("SiteCodes are unexpectedly equal",
                site.equals(new SiteCode("Sit", site.getSymmetryCode())));
        assertFalse("SiteCodes are unexpectedly equal", site.equals(
                new SiteCode("Site", SymmetryCode.valueOf("2_555"))));
        assertFalse("SiteCodes are unexpectedly equal", site.equals(
                new SiteCode("Site", SymmetryCode.valueOf("3_565"))));
        try {
            assertFalse("SiteCode is equal to null", site.equals(null));
        } catch (NullPointerException npe) {
            fail("NullPointerException while testing equals(null)");
        }
        assertFalse("SymmetryCode is equal to a random object",
                site.equals(new Object()));
        assertFalse("SymmetryCode is equal to an object with the same hash",
                site.equals(Integer.valueOf(site.hashCode())));
    }

    /**
     * Tests the {@code hashCode()} and {@code equals()} methods for
     * consistency
     */
    public void testFeature__hashCode__equals() {
        List<String> labels = new ArrayList<String>();
        List<SymmetryCode> codes = new ArrayList<SymmetryCode>();
        
        Collections.addAll(labels, "Larry", "Moe", "Curly");
        Collections.addAll(codes, 
                SymmetryCode.valueOf("1_555"),
                SymmetryCode.valueOf("17_346"),
                SymmetryCode.valueOf("192_191"));
        
        for (String label : labels) {
            for (SymmetryCode symm : codes) {
                SiteCode site1 = new SiteCode(label, symm);
                SiteCode site2 = new SiteCode(label, symm);
                
                assertEquals("SiteCodes unexpectedly unequal", site1, site2);
                assertEquals("Equal SiteCodes have unequal hash codes",
                        site1.hashCode(), site2.hashCode());
            }
        }
    }

    /**
     * Tests the normal behavior of the {@code valueOf(String, SymmetryCode)}
     * method
     */
    public void testMethod__valueOf_String_SymmetryCode() {
        SiteCode site = new SiteCode("Pphtthhh", SymmetryCode.valueOf("2_565"));
        
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Pphtthhh|2_565", null));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Pphtthhh|2_565",
                        SymmetryCode.valueOf("1_656")));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Pphtthhh", site.getSymmetryCode()));
        
        site = new SiteCode("Foo|1_555", SymmetryCode.valueOf("54_321"));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Foo|1_555|54_321", null));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Foo|1_555|54_321",
                        SymmetryCode.valueOf("1_656")));
        
        site = new SiteCode("Foo|", SymmetryCode.valueOf("12_345"));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Foo||12_345", null));
        assertEquals("SiteCode incorrectly decoded",
                site, SiteCode.valueOf("Foo||12_345",
                        SymmetryCode.valueOf("1_656")));
    }
    
    /**
     * Tests the behavior of the {@code valueOf(String, SymmetryCode)}
     * method when its first argument is {@code null}; expected a
     * {@code NullPointerException}
     */
    public void testMethod__valueOf_nullString_SymmetryCode() {
        try {
            SiteCode.valueOf(null, SymmetryCode.valueOf("1_555"));
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code valueOf(String, SymmetryCode)}
     * method when its second argument is needed but {@code null}; expected a
     * {@code NullPointerException}
     */
    public void testMethod__valueOf_String_nullSymmetryCode() {
        try {
            SiteCode.valueOf("Atom", null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code valueOf(String, SymmetryCode)}
     * method when its first argument contains an invalid symmetry code or
     * symmetry code fragment; an {@code IllegalArgumentException} is expected
     */
    public void testMethod__valueOf_badString_SymmetryCode() {
        SymmetryCode symmetry = SymmetryCode.valueOf("1_555");
        
        try {
            SiteCode.valueOf("Foo|0_555", symmetry);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SiteCode.valueOf("Foo|", symmetry);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SiteCode.valueOf("Foo|1_550", symmetry);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SiteCode.valueOf("Foo|1_5555", symmetry);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the interoperability of the {@code toString()} and
     * {@code valueOf(String, SymmetryCode)} methods
     */
    public void testFeature__toString__valueOf() {
        List<String> labels = new ArrayList<String>();
        List<SymmetryCode> codes = new ArrayList<SymmetryCode>();
        
        Collections.addAll(labels, "Larry", "Moe", "Curly");
        Collections.addAll(codes, 
                SymmetryCode.valueOf("1_555"),
                SymmetryCode.valueOf("17_346"),
                SymmetryCode.valueOf("192_191"));
        
        for (String label : labels) {
            for (SymmetryCode symm : codes) {
                SiteCode site = new SiteCode(label, symm);
                
                assertEquals("valueOf() does not reverse toString()",
                        site, SiteCode.valueOf(site.toString(), null));
            }
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
        TestSuite tests = new TestSuite(SiteCodeTests.class);
        
        tests.setName("SiteCode Tests");
        
        return tests;
    }
}
