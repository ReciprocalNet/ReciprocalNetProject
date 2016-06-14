/*
 * Reciprocal Net Project
 *
 * CifWarningTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code CifWarning} class.
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifWarningTests extends TestCase {
    
    /**
     * Initializes a new {@code CifWarningTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifWarningTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the {@code String}-arg constructor when its
     * argument is {@code null}.  The resulting {@code CifWarning} should
     * contain a non-{@code null} default message and a zero error code.
     */
    public void testConstructor_nullString() {
        CifWarning warn = new CifWarning((String) null);
        
        assertNotNull("Message is null", warn.getMessage());
        assertEquals("Wrong error code", 0, warn.getCode());
    }

    /**
     * Tests the behavior of the {@code String}-arg constructor when its
     * argument is non-{@code null}.  The resulting {@code CifWarning} should
     * have a non-{@code null} message containing the given message, and should
     * have a zero error code.
     */
    public void testConstructor_String() {
        String testString = "Foo!";
        CifWarning warn = new CifWarning(testString);
        
        assertTrue("Message is not null",
                   warn.getMessage().indexOf(testString) >= 0);
        assertEquals("Wrong error code", 0, warn.getCode());
    }

    /**
     * Tests the behavior of the {@code CifError}-arg constructor when its
     * argument is {@code null}.  The resulting {@code CifWarning} should
     * have a non-{@code null} message (of unspecified content), and should have
     * a zero error code.
     */
    public void testConstructor_nullCifError() {
        CifWarning warning = new CifWarning((CifError) null);
        
        assertEquals("Wrong error code", 0, warning.getCode());
        assertNotNull("Message is null", warning.getMessage());
    }

    /**
     * Tests the behavior of the {@code CifError}-arg constructor when its
     * argument is non-{@code null}.  The resulting {@code CifWarning} should
     * have a non-{@code null} message containing the {@code CifError}'s
     * message, and should have the same error code as the {@code CifError}.
     */
    public void testConstructor_CifError() {
        CifError error = new CifError(CifError.DUPLICATE_FRAME_CODE, null);
        CifWarning warning = new CifWarning(null, error);
        
        assertEquals("Wrong error code", error.getCode(), warning.getCode());
        assertTrue("Message does not contain correct root cause",
                   warning.getMessage().indexOf(error.getMessage()) >= 0);
    }

    /**
     * Tests the behavior of the two-arg constructor when its first argument is
     * non-{@code null}.  The resulting {@code CifWarning} should have a
     * non-{@code null} message containing the {@code CifError}'s message, and
     * should have the same error code as the {@code CifError}.
     */
    public void testConstructor_nullString_CifError() {
        CifError error = new CifError(CifError.DUPLICATE_FRAME_CODE, null);
        CifWarning warning = new CifWarning(null, error);
        
        assertEquals("Wrong error code", error.getCode(), warning.getCode());
        assertTrue("Message does not contain correct root cause",
                   warning.getMessage().indexOf(error.getMessage()) >= 0);
    }

    /**
     * Tests the behavior of the two-arg constructor when its second argument is
     * {@code null}.  The resulting {@code CifWarning} should have a
     * non-{@code null} message containing the given message, and should
     * have a zero error code.
     */
    public void testConstructor_String_nullCifError() {
        String testString = "Bar?";
        CifWarning warn = new CifWarning(testString);
        
        assertTrue("Message is not null",
                   warn.getMessage().indexOf(testString) >= 0);
        assertEquals("Wrong error code", 0, warn.getCode());
    }

    /**
     * Tests the behavior of the two-arg constructor when both arguments are
     * {@code null}.  The resulting {@code CifWarning} should have a
     * non-{@code null} message (of unspecified content), and should have a zero
     * error code.
     */
    public void testConstructor_nullString_nullCifError() {
        CifWarning warn = new CifWarning(null, null);
        
        assertNotNull("Message is null", warn.getMessage());
        assertEquals("Wrong error code", 0, warn.getCode());
    }

    /**
     * Tests the behavior of the two-arg constructor when neither argument is
     * {@code null}.  The resulting {@code CifWarning} should have a
     * message containing the string argument and the error's message, and
     * should have the same error code as the {@code CifError}.
     */
    public void testConstructor_String_CifError() {
        CifError error = new CifError(CifError.LOOP_EMPTY_HEADER, null);
        String testString = "Flying Fish";
        CifWarning warning = new CifWarning(testString, error);
        
        assertEquals("Wrong error code", error.getCode(), warning.getCode());
        assertTrue("Message does not contain correct root cause",
                   warning.getMessage().indexOf(error.getMessage()) >= 0);
        assertTrue("Message does not contain specified string (" + testString
                   + ")", warning.getMessage().indexOf(testString) >= 0);
    }
}
