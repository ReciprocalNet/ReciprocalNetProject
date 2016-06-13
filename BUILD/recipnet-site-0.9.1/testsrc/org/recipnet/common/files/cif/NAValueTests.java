/*
 * Reciprocal Net Project
 *
 * NAValueTests.java
 * 
 * Apr 6, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;

/**
 * Tests the behavior of the {@code NAValue} class to verify that it is as
 * documented
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class NAValueTests extends TestCase {

    /**
     * Initializes this {@code NAValueTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public NAValueTests(String testName) {
        super(testName);
    }
    
    /**
     * Tests that the single exposed instance of the {@code NAValue} class
     * is not {@code null}
     */
    public void testFeature_instance() {
        assertNotNull("The instance is null", NAValue.instance);
    }

    /**
     * Tests that the single exposed instance of the {@code NAValue} class
     * has the correct string representation (a single period, ".")
     */
    public void testMethod_toString() {
        assertEquals("The instance has the wrong String representation",
                     ".", NAValue.instance.toString());
    }
}
