/*
 * Reciprocal Net Project
 *
 * CifErrorAlerterTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit {@code TestCase} the exercises the behavior of the
 * {@code CifErrorAlerter} class
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifErrorAlerterTests extends TestCase {

    /**
     * Initializes a new {@code CifErrorAlerterTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifErrorAlerterTests(String testName) {
        super(testName);
    }
    
    /**
     * Tests the behavior of the handleError() method.  It should always throw
     * a {@code CifParseException} with the supplied {@code CifError} attached.
     */
    public void testHandleError() {
        CifError error = new CifError();
        CifErrorAlerter alerter = new CifErrorAlerter();
        
        try {
            alerter.handleError(error);
            fail("Expected a CifParseException");
        } catch (CifParseException cpe) {
            assertTrue("Exception has wrong CifError",
                       error == cpe.getCifError());
        }
    }
}
