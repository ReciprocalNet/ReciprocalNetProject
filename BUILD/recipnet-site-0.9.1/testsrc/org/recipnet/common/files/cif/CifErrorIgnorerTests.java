/*
 * Reciprocal Net Project
 *
 * CifErrorIgnorerTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit {@code TestCase} the exercises the behavior of the
 * {@code CifErrorIgnorer} class
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifErrorIgnorerTests extends TestCase {

    /**
     * Initializes a new {@code CifErrorAlerterTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifErrorIgnorerTests(String testName) {
        super(testName);
    }
    
    /**
     * Tests the behavior of the handleError() method.  It should never do
     * anything externally observable
     */
    public void testHandleError() {
        CifErrorIgnorer ignorer = new CifErrorIgnorer();

        for (int i = 0; i < 1023; i++) {
            ignorer.handleError(new CifError(i, null));
        }
    }
}
