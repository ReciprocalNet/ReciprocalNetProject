/*
 * Reciprocal Net Project
 *
 * StringValueTests.java
 * 
 * 06-Apr-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;

// TODO: getValue() tests

/**
 * A JUnit TestCase that exercises the behavior of the StringValue class to
 * verify that it behaves as expected 
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class StringValueTests extends TestCase {

    /**
     * Initializes a new {@code StringValueTests} to run the named test
     * 
     * @param  testName the name of the test to run 
     */
    public StringValueTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the StringValue constructor when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__null() {
        try {
            new StringValue(null);
            fail("expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the StringValue constructor when its argument is
     * "simple", meaning that it is not expected to trigger any quoting behavior
     * in toString().
     */
    public void testConstructor_String__simple() {
        String[] strings = new String[] {"a", "xyz", "loop_1", "global_foo",
                "do_not_stop_", "bar[none]", "noname.txt", "don't",
                "James'", "quote(\")", "quoted_\"", "mid#hash", "endhash#",
                "anti-hyphenation", "(+)-glycine", "semi;colon", "?que"};
        
        for (String testString : strings) {
            assertEquals("Wrong result returned by toString()", testString,
                         new StringValue(testString).toString());
        }
    }
    
    /**
     * Tests the behavior of the StringValue constructor when its argument is
     * "complex", meaning that it is expected to trigger inline quoting
     * behavior in toString().
     */
    public void testConstructor_String__complex() {
        String[] strings = new String[] {"stop_", "loop_", "global_", 
            "save_", "data_", ".", "?"};
        String[] startStrings = new String[] {"save_", "data_", "'", "\"", "-",
            "+", ".", "_", "[", "]", "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "#", "$", ";"};
        String[] wsStrings = new String[] {" ", "\t", "\u0009"};
        
        for (String testString : strings) {
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());
        }
        
        for (String startString : startStrings) {
            String testString = startString + "foo";
            
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());
        }
        
        for (String wsString : wsStrings) {
            String testString = wsString;
            
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());
            
            testString = wsString + "bar";
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());

            testString = "foo" + wsString + "bar";
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());

            testString = "foo" + wsString;
            assertEquals("Wrong result returned by toString()",
                         "'" + testString + "'",
                         new StringValue(testString).toString());
        }
        
        String testString = "' ";
        assertEquals("Wrong result returned by toString()",
                     "\"" + testString + "\"",
                     new StringValue(testString).toString());
        
        testString = "foo' ";
        assertEquals("Wrong result returned by toString()",
                     "\"" + testString + "\"",
                     new StringValue(testString).toString());
        
        testString = "' bar";
        assertEquals("Wrong result returned by toString()",
                     "\"" + testString + "\"",
                     new StringValue(testString).toString());
        
        testString = "foo' bar";
        assertEquals("Wrong result returned by toString()",
                     "\"" + testString + "\"",
                     new StringValue(testString).toString());
        
        testString = "foo' bar's \"ball\"";
        assertEquals("Wrong result returned by toString()",
                     "\"" + testString + "\"",
                     new StringValue(testString).toString());
    }

    /**
     * Tests the behavior of the StringValue constructor when its argument is
     * "multiline", meaning that it is expected to trigger text block quoting
     * behavior in toString().
     */
    public void testConstructor_String__multiline() {
        String separator = System.getProperty("line.separator") + ';';
        String[] linebreakStrings = new String[] {"\n", "\r", "\f"};
        
        for (String lbString : linebreakStrings) {
            String testString = lbString;
            
            assertEquals("Wrong result returned by toString()",
                         separator + testString + separator,
                         new StringValue(testString).toString());
            
            testString = lbString + "bar";
            assertEquals("Wrong result returned by toString()",
                         separator + testString + separator,
                         new StringValue(testString).toString());

            testString = "foo" + lbString + "bar";
            assertEquals("Wrong result returned by toString()",
                         separator + testString + separator,
                         new StringValue(testString).toString());

            testString = "foo" + lbString;
            assertEquals("Wrong result returned by toString()",
                         separator + testString + separator,
                         new StringValue(testString).toString());
        }
    }

    /**
     * Tests the behavior of the {@code toString()} method on a {@code CifValue}
     * whose 
     */
    public void testMethod_toString__illegalState() {
        String[] unrepresentableStrings = new String[] {
            "' \" ", "\" ' ", "foo' \" ", "' bar\" ", "foo' bar\" "
        };
        
        for (String s : unrepresentableStrings) {
            StringValue sv = new StringValue(s);
            try {
                sv.toString();
                fail("Expected an IllegalStateException");
            } catch (IllegalStateException ise) {
                // The expected case
            }
        }
    }
}
