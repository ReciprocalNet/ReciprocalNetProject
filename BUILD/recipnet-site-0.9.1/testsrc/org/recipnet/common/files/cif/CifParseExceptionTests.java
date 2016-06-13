/*
 * Reciprocal Net Project
 *
 * CifParseExceptionTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit {@code TestCase} excercising the behavior of the
 * {@code CifParseException} class
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifParseExceptionTests extends TestCase {

    /**
     * Initializes a new {@code CifParseExceptionTests} to run the named test.
     * 
     * @param testName the name of the test to run
     */
    public CifParseExceptionTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the nullary constructor.  The exception initialized
     * by this constructor should have {@code null} message, cause, and
     * attached {@code CifError}. 
     */
    public void testConstructor() {
        CifParseException cpe = new CifParseException();
        
        assertNull(cpe.getMessage());
        assertNull(cpe.getCause());
        assertNull(cpe.getCifError());
    }

    /**
     * Tests the behavior of the {@code String}-arg constructor.  The exception
     * initialized by this constructor should have a detail message equal to
     * the string it was initialized with, and {@code null} cause, and attached
     * {@code CifError}. 
     */
    public void testConstructor_String() {
        String testString = "I'm a little teapot";
        CifParseException cpe = new CifParseException(testString);
        
        assertEquals("Wrong test message", testString, cpe.getMessage());
        assertNull(cpe.getCause());
        assertNull(cpe.getCifError());
    }

    /**
     * Tests the behavior of the {@code CifError}-arg constructor.  The
     * exception initialized by this constructor should have a detail message
     * equal to the error message of the {@code CifError} argument, a
     * {@code null} cause, and the {@code CifError} argument as its attached
     * {@code CifError}.
     */
    public void testConstructor_CifError() {
        CifError error = new CifError();
        CifParseException cpe = new CifParseException(error);
        
        assertEquals("Wrong test message", error.getMessage(),
                     cpe.getMessage());
        assertNull(cpe.getCause());
        assertTrue("Wrong CifError", error == cpe.getCifError());
    }
}
