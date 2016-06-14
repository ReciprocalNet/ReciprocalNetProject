/*
 * Reciprocal Net Project
 *
 * CifErrorTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit {@code TestCase} that exercises the behavior of the {@code CifError}
 * class.
 * 
 * @author jobollin
 * @version 0.9
 */
public class CifErrorTests extends TestCase {

    /**
     * Initializes a new {@code CifErrorTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifErrorTests(String testName) {
        super(testName);
    }

    /**
     * Tests the no-arg constructor.  An instance initialized via this
     * constructor should have a non-null message, a zero error code, and a
     * {@code null} {@code ScanState}.
     */
    public void testConstructor() {
        CifError error = new CifError();
        
        assertNotNull("Error message is null", error.getMessage());
        assertEquals("Wrong error code", 0, error.getCode());
        assertNull("ScanState is not null", error.getScanState());
    }
    
    /**
     * Tests the two-arg constructor when the {@code ScanState} argument is
     * {@code null}; the {@code CifError} initialized this way should have a
     * non-null error message, the specified error code, and a {@code null}
     * {@code ScanState}
     */
    public void testConstructor_int_nullScanState() {
        int testCode = 1066;
        CifError error = new CifError(testCode, null);
        
        assertNotNull("Error message is null", error.getMessage());
        assertEquals("Wrong error code", testCode, error.getCode());
        assertNull("ScanState is not null", error.getScanState());
    }

    /**
     * Asserts that the two specified {@code ScanState}s are equal with respect
     * to their exposed properties
     * 
     * @param  state1 the first {@code ScanState} to compare
     * @param  state2 the second {@code ScanState} to compare
     */
    private void assertStatesEqual(ScanState state1, ScanState state2) {
        assertEquals("State copy has wrong current char",
                     state1.getCurrentChar(), state2.getCurrentChar());
        assertEquals("State copy has wrong last char",
                     state1.getLastChar(), state2.getLastChar());
        assertEquals("State copy has wrong line number",
                     state1.getLineNumber(), state2.getLineNumber());
        assertEquals("State copy has wrong line number",
                     state1.getCharacterNumber(), state2.getCharacterNumber());
        assertEquals("State copy has wrong token number",
                     state1.getCurrentToken(), state2.getCurrentToken());
    }
    
    /**
     * Tests the two-arg constructor when the {@code ScanState} is not
     * {@code null}; the {@code CifError} initialized this way should have a
     * non-null error message, the specified error code, and a {@code ScanState}
     * that is a copy of the constructor argument (and not the same object).
     * Moreover, it should be possible to set the current character of the
     * {@code ScanState} argument via the copy, provided that the original
     * remains at the same position.
     */
    public void testConstructor_int_ScanState() {
        int underscore = '_';
        int testCode = CifError.DATA_NAME_MISSING;
        TestScanState state = new TestScanState();
        CifError error = new CifError(testCode, state);
        ScanState stateCopy;
        
        assertNotNull("Error message is null", error.getMessage());
        assertEquals("Wrong error code", testCode, error.getCode());
        stateCopy = error.getScanState();
        assertNotNull("ScanState is null", stateCopy);
        assertTrue("Scan state not copied to a new object", state != stateCopy);
        assertStatesEqual(state, stateCopy);
        
        stateCopy.setCurrentChar(underscore);
        assertEquals("Current char not updated correctly", underscore,
                     stateCopy.getCurrentChar());
        assertStatesEqual(state, stateCopy);
        
        state.lineNumber++;
        try {
            stateCopy.setCurrentChar(' ');
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals("Copy's line number changed", state.lineNumber - 1,
                         stateCopy.getLineNumber());
        }
        state.lineNumber--;
        stateCopy.setCurrentChar(' ');

        state.characterNumber++;
        try {
            stateCopy.setCurrentChar('"');
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            assertEquals("Copy's character number changed",
                         state.characterNumber - 1,
                         stateCopy.getCharacterNumber());
        }
        state.characterNumber--;
        stateCopy.setCurrentChar('"');
        
        state.lastChar = ';';
        stateCopy.setCurrentChar(' ');
    }

    /**
     * Tests the {@code toString()} method.  It should return a string equal to
     * the one returned by {@code getMessage()}
     */
    public void testMethod_toString() {
        CifError error = new CifError();
        
        assertEquals("String value and message do not match",
                     error.getMessage(), error.toString());
        
        error = new CifError(CifError.DATA_NAME_LENGTH, null);
        assertEquals("String value and message do not match",
                     error.getMessage(), error.toString());
        
        error = new CifError(CifError.DATA_VALUE_MISSING, new TestScanState());
        assertEquals("String value and message do not match",
                     error.getMessage(), error.toString());
    }

    /**
     * A lightweight {@code ScanState} implementation used for testing purposes
     * 
     * @author jobollin
     * @version 0.9
     */
    private static class TestScanState implements ScanState {
        
        /** the value returned by {@code getLastChar()} */
        int lastChar = 'r';
        
        /** the value returned by {@code getCurrentChar()} */
        int currentChar = ' ';
        
        /** the value returned by {@code getLineNumber()} */
        int lineNumber = 1;
        
        /** the value returned by {@code getCharacterNumber()} */
        int characterNumber = 2;
        
        /** the value returned by {@code getCurrentToken()} */
        String currentToken = "";

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#getCurrentChar()
         */
        public int getCurrentChar() {
            return currentChar;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#setCurrentChar(int)
         */
        public void setCurrentChar(int newChar) {
            currentChar = newChar;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#getLastChar()
         */
        public int getLastChar() {
            return lastChar;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#getLineNumber()
         */
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#getCharacterNumber()
         */
        public int getCharacterNumber() {
            return characterNumber;
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.files.cif.ScanState#getCurrentToken()
         */
        public String getCurrentToken() {
            return currentToken;
        }
        
    }
}
