/*
 * Reciprocal Net Project
 *
 * SymmetryCodeTests.java
 *
 * Dec 19, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code SymmetryCode} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SymmetryCodeTests extends TestCase {

    /**
     * Initializes a {@code SymmetryCodeTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public SymmetryCodeTests(String testName) {
        super(testName);
    }

    /**
     * Tests the normal behavior of the constructor
     */
    public void testConstructor__int_intArray() {
        for (int opIndex = 1; opIndex < 200; opIndex += 10) {
            for (int x = 4; x < 7; x++) {
                for (int y = 4; y < 7; y++) {
                    for (int z = 4; z < 7; z++) {
                        int[] trans = new int[] {x, y, z};
                        SymmetryCode code = new SymmetryCode(opIndex, trans);
                        
                        assertEquals(
                                "The SymmetryCode has the wrong operation index",
                                opIndex, code.getOperationIndex());
                        assertTrue(
                                "The symmetry code has the wrong translations",
                                Arrays.equals(trans, code.getTranslations()));
                    }
                }
            }
        }
    }

    /**
     * Tests the behavior of the constructor when its second argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__int_nullintArray() {
        try {
            new SymmetryCode(1, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the constructor when its first argument is
     * nonpositive; an {@code IllegalArgumentException} is expected
     */
    public void testConstructor__badint_intArray() {
        try {
            new SymmetryCode(0, new int[] {5, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(-1, new int[] {5, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the constructor when its second argument is
     * the wrong length; an {@code IllegalArgumentException} is expected
     */
    public void testConstructor__int_badintArray() {
        try {
            new SymmetryCode(1, new int[] {5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 5, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the constructor when its second argument contains
     * an invalid value; an {@code IllegalArgumentException} is expected
     */
    public void testConstructor__int_intArray__badTranslation() {
        try {
            new SymmetryCode(1, new int[] {-1, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {0, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {10, 5, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, -1, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 0, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 10, 5});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 5, -1});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 5, 0});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
        try {
            new SymmetryCode(1, new int[] {5, 5, 10});
            fail("Expected a IllegalArgumentException");
        } catch (IllegalArgumentException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code equals()} method when its argument is
     * unequal to the invocation target; these are the case when the method
     * should return {@code false}
     */
    public void testMethod__equals__unequal() {
        SymmetryCode code = new SymmetryCode(13, new int[] {7, 5, 4});
        
        for (int opIndex = 7; opIndex < 200; opIndex += 5) {
            for (int x = 3; x < 8; x += 2) {
                for (int y = 3; y < 8; y += 2) {
                    for (int z = 3; z < 8; z += 2) {
                        int[] trans = new int[] {x, y, z};
                        SymmetryCode code2 = new SymmetryCode(opIndex, trans);
                        
                        assertFalse("SymmetryCode objects unexpectedly equal",
                                code.equals(code2));
                    }
                }
            }
        }
        
        try {
            assertFalse("SymmetryCode is equal to null", code.equals(null));
        } catch (NullPointerException npe) {
            fail("NullPointerException while testing equals(null)");
        }
        assertFalse("SymmetryCode is equal to a random object",
                code.equals(new Object()));
        assertFalse("SymmetryCode is equal to an object with the same hash",
                code.equals(Integer.valueOf(code.hashCode())));
    }
    
    /**
     * Tests the {@code hashCode()} and {@code equals()} methods to verify that
     * equal {@code SymmetryCode} objects have the same hash code 
     */
    public void testFeature__hashCode__equals() {
        for (int opIndex = 5; opIndex < 200; opIndex += 10) {
            for (int x = 3; x < 8; x += 2) {
                for (int y = 3; y < 8; y += 2) {
                    for (int z = 3; z < 8; z++) {
                        int[] trans = new int[] {x, y, z};
                        SymmetryCode code1 = new SymmetryCode(opIndex, trans);
                        SymmetryCode code2 = new SymmetryCode(opIndex, trans);
                        
                        assertEquals("SymmetryCode objects unequal",
                                code1, code2);
                        assertEquals(
                                "Equal SymmetryCodes had different hash codes",
                                code1.hashCode(), code2.hashCode());
                    }
                }
            }
        }
    }

    /**
     * Tests the behavior of the {@code valueOf()} method when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__valueOf_nullString() {
        try {
            SymmetryCode.valueOf(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code valueOf()} method when its argument is
     * not a valid {@code String} representation of a {@code SymmetryCode}
     */
    public void testMethod__valueOf_badString() {
        try {
            SymmetryCode.valueOf("1_550");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("1_5a5");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("0_545");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("-1_545");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("1__545");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("|1_545");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf(" 1_545");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("1_545 ");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            SymmetryCode.valueOf("15_45");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code toString()} and {@code valueOf()}
     * methods; particularly that they form a pair such that each one produces
     * the input of the other from the corresponding out from the other
     */
    public void testFeature__toString__valueOf() {
        for (int opIndex = 1; opIndex < 200; opIndex += 10) {
            for (int x = 4; x < 9; x++) {
                for (int y = 4; y < 9; y += 2) {
                    for (int z = 4; z < 9; z +=2) {
                        int[] trans = new int[] {x, y, z};
                        SymmetryCode code = new SymmetryCode(opIndex, trans);
                        StringBuilder sb = new StringBuilder();
                        String sv;
                        
                        sb.append(opIndex).append('_');
                        sb.append(x).append(y).append(z);
                        sv = sb.toString();
                        assertEquals(
                                "The SymmetryCode has the wrong string value",
                                sv, code.toString());
                        assertEquals(
                                "The SymmetryCode was not parsed correctly",
                                code, SymmetryCode.valueOf(sv));
                    }
                }
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
        TestSuite tests = new TestSuite(SymmetryCodeTests.class);
        
        tests.setName("SymmetryCode Tests");
        
        return tests;
    }
}
