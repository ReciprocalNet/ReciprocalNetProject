/*
 * Reciprocal Net Project
 *
 * ScalarDataTests.java
 * 
 * 30-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code CifFile.ScalarData} class
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class ScalarDataTests extends TestCase {
    
    private List<String> legalNames = new ArrayList<String>();
    private List<String> illegalNames = new ArrayList<String>();
    
    /**
     * Initializes a new {@code ScalarDataTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public ScalarDataTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this {@code TestCase} to run a test
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        Collections.addAll(legalNames,
                           "_a", "_foo", "_17", "__", "_x_y_z", "_@#%@#$^@$&");
        Collections.addAll(illegalNames,
                           "", "_", "_ ", "_\t", "_\n", " _foo", "_f\tg",
                           "_I contain spaces", "_x\0080", "_xyz\u0101");
    }
    
    /**
     * Tests the constructor's behavior when the {@code String} argument is
     * {@code null}.  A {@code NullPointerException} is expected. 
     */
    public void testConstructor_String_CifValue__nullName() {
        try {
            new CifFile.ScalarData<TestCifValue>(null, new TestCifValue());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the constructor's behavior when the {@code CifFile.CifValue}
     * argument is {@code null}.  A {@code NullPointerException} is expected. 
     */
    public void testConstructor_String_CifValue__nullValue() {
        try {
            new CifFile.ScalarData<CifFile.CifValue>("_name", null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the constructor's behavior when the {@code String} argument does
     * not contain a legal CIF data name.  An {@code IllegalArgumentException}
     * is expected. 
     */
    public void testConstructor_String_CifValue__illegalName() {
        CifFile.CifValue val = new TestCifValue();
        
        for (String name : illegalNames) {
            try {
                new CifFile.ScalarData<CifFile.CifValue>(name, val);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // the expected case
            }
        }
    }

    /**
     * Tests the constructor's behavior with valid arguments.  The arguments
     * should be recoverable from the new instance.
     */
    public void testConstructor_String_CifValue() {
        CifFile.CifValue val = new TestCifValue();
        
        for (String name : legalNames) {
            CifFile.ScalarData<? extends CifFile.CifValue> scalar
                    = new CifFile.ScalarData<CifFile.CifValue>(name, val);

            assertEquals("Wrong name", name, scalar.getName());
            assertEquals("Wrong value", val, scalar.getValue());
        }
    }
}
