/*
 * IUMSC Reciprocal Net Project
 *
 * FilenameValidatorTests.java
 *
 * 01-Nov-2006: jobollin wrote first draft
 */

package org.recipnet.site.shared.validation;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests the behavior of the FilenameValidator
 *
 * @author jobollin
 * @version 0.9.0
 */
public class FilenameValidatorTests extends TestCase {

    private final static String[] validStrings = new String[] {
        "filename", "filename1", "file_name_1", "file-NAME_1_2-3",
        "test_name_5", ".-._.._--.", "12345", ".foo", "..bar", "..."
    };
    
    private final static String[] invalidStrings = new String[] {
        ".", "..", "", " ", ">foo", "$bar", "rm *", "rm\\ *", "rm\\ -f\\ .",
        "blarney stone", "nohup&"
    };
    
    /**
     * Initializes a new {@code FilenameValidatorTests} to run the named test
     *
     * @param testName the name of the test to run
     */
    public FilenameValidatorTests(String testName) {
        super(testName);
    }

    public void testMethod_isValid__null() {
        try {
            new FilenameValidator().isValid(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected result
        } catch (Exception e) {
            fail("Expected a NullPointerException, got " + e.getClass().getName());
        }
    }
    
    public void testMethod_isValid__nonString() {
        assertFalse("Found a plain Object valid",
                new FilenameValidator().isValid(new Object()));
        assertFalse("Found an Integer valid",
                new FilenameValidator().isValid(Integer.valueOf(12345)));
    }
    
    public static Test suite() {
        TestSuite ts = new TestSuite(FilenameValidatorTests.class);

        ts.setName("FilenameValidator Tests");
        for (final String validString : validStrings) {
            ts.addTest(new TestCase("Test valid filename: '" + validString + "'"
            ) {
                @Override
                public void runTest() {
                    assertTrue("The FilenameValidator rejected a valid object",
                            new FilenameValidator().isValid(validString));
                }
            });
        }
        for (final String validString : invalidStrings) {
            ts.addTest(new TestCase("Test invalid filename: '" + validString + "'"
            ) {
                @Override
                public void runTest() {
                    assertFalse("The FilenameValidator accepted an invalid object",
                            new FilenameValidator().isValid(validString));
                }
            });
        }
        
        return ts;
    }
}
