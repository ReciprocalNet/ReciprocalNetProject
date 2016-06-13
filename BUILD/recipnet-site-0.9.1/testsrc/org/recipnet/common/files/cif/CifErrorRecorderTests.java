/*
 * Reciprocal Net Project
 *
 * CifErrorRecorderTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * A JUnit {@code TestCase} excercising the behavior of the
 * {@code CifErrorRecorder} class.
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifErrorRecorderTests extends TestCase {
    
    private CifErrorRecorder recorder;
    
    /**
     * Initializes a new {@code CifErrorRecorderTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifErrorRecorderTests(String testName) {
        super(testName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        recorder = new CifErrorRecorder();
    }
    
    /**
     * Tests the behavior of the {@code getErrors()} method when no errors have
     * yet been handled -- should return an empty {@code List}
     */
    public void testMethod_getErrors__initial() {
        List<CifError> list = recorder.getErrors();
        
        assertNotNull("Error list is null", list);
        assertTrue("Error list is not empty", list.isEmpty());
    }

    /**
     * Tests the behavior of the {@code clearErrors()} method when one error has
     * been handled -- should clear the internal list
     */
    public void testMethod_clearErrors__oneError() {
        List<CifError> list;

        recorder.handleError(new CifError());
        recorder.clearErrors();
        list = recorder.getErrors();
        assertNotNull("Error list is null", list);
        assertTrue("Error list is not empty", list.isEmpty());
    }

    /**
     * Tests the behavior of the {@code clearErrors()} method when ten errors
     * have been handled -- should clear the internal list
     */
    public void testMethod_clearErrors__tenErrors() {
        List<CifError> list;

        for (int i = 0; i < 10; i++) {
            recorder.handleError(new CifError());
        }
        recorder.clearErrors();
        list = recorder.getErrors();
        assertNotNull("Error list is null", list);
        assertTrue("Error list is not empty", list.isEmpty());
    }

    /**
     * Tests the behavior of the {@code handleError(CifError)} method.  Errors
     * handled by the method should be recorded, in order, in the internal list,
     * and should be retrieved upon request by {@code getErrors()}.  An
     * intervening {@code clearErrors()} should not cause the process to fail.
     */
    public void testMethod_handleError() {
        List<CifError> reference = new ArrayList<CifError>();
        CifError error;
        
        for (int i = 0; i < 5; i++) {
            Iterator<CifError> it;
            List<CifError> list;
            
            error = new CifError();
            reference.add(error);
            recorder.handleError(error);
            it = reference.iterator();
            
            list = recorder.getErrors();
            assertEquals("Wrong number of errors", reference.size(),
                         list.size());
            for (CifError err : list) {
                assertTrue("Error list doesn't match", err == it.next());
            }
        }
        
        // It should continue to work after being cleared
        recorder.clearErrors();
        reference.clear();
        for (int i = 0; i < 5; i++) {
            Iterator<CifError> it;
            List<CifError> list;
            
            error = new CifError();
            reference.add(error);
            recorder.handleError(error);
            it = reference.iterator();
            
            list = recorder.getErrors();
            assertEquals("Wrong number of errors", reference.size(),
                         list.size());
            for (CifError err : list) {
                assertTrue("Error list doesn't match", err == it.next());
            }
        }
    }

}
