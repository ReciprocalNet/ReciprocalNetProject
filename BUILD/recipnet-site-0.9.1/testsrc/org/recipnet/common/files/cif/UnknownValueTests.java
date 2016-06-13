/*
 * Reciprocal Net Project
 *
 * UnknownValueTests.java
 * 
 * Apr 6, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * Tests the behavior of the {@code UnknownValue} class to verify that it is as
 * documented
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class UnknownValueTests extends TestCase {

    /**
     * Initializes this {@code UnknownValueTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public UnknownValueTests(String testName) {
        super(testName);
    }
    
    /**
     * Tests that the single exposed instance of the {@code UnknownValue} class
     * is not {@code null}
     */
    public void testFeature_instance() {
        assertNotNull("The instance is null", UnknownValue.instance);
    }

    /**
     * Tests that the single exposed instance of the {@code UnknownValue} class
     * has the correct string representation (a single question mark, "?")
     */
    public void testMethod_toString() {
        assertEquals("The instance has the wrong String representation",
                     "?", UnknownValue.instance.toString());
    }
}
