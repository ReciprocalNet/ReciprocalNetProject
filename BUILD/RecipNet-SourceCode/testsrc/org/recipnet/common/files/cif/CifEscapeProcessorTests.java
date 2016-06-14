/*
 * Reciprocal Net Project
 *
 * CifEscapeProcessorTests.java
 * 
 * Jul 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * TODO: Write documentation
 * 
 * @author jobollin
 * @version 1.0
 */
public class CifEscapeProcessorTests {
    
    private static final String[] translationData = new String[] {
        "\\a", "\u03b1",
        "\\b", "\u03b2",
        "\\c", "\u03c7",
        "\\d", "\u03b4",
        "\\e", "\u03b5",
        "\\f", "\u03c6",
        "\\g", "\u03b3",
        "\\h", "\u03b7",
        "\\i", "\u03b9",
        "\\k", "\u03ba",
        "\\l", "\u03bb",
        "\\m", "\u03bc",
        "\\n", "\u03bd",
        "\\o", "\u03bf",
        "\\p", "\u03c0",
        "\\q", "\u03b8",
        "\\r", "\u03c1",
        "\\s", "\u03c3",
        "\\t", "\u03c4",
        "\\u", "\u03c5",
        "\\w", "\u03c9",
        "\\x", "\u03be",
        "\\y", "\u03c8",
        "\\z", "\u03b6",
        "\\A", "\u0391",
        "\\B", "\u0392",
        "\\C", "\u03a7",
        "\\D", "\u0394",
        "\\E", "\u0395",
        "\\F", "\u03a6",
        "\\G", "\u0393",
        "\\H", "\u0397",
        "\\I", "\u0399",
        "\\K", "\u039a",
        "\\L", "\u039b",
        "\\M", "\u039c",
        "\\N", "\u039d",
        "\\O", "\u039f",
        "\\P", "\u03a0",
        "\\Q", "\u0398",
        "\\R", "\u03a1",
        "\\S", "\u03a3",
        "\\T", "\u03a4",
        "\\U", "\u03a5",
        "\\W", "\u03a9",
        "\\X", "\u039e",
        "\\Y", "\u03a8",
        "\\Z", "\u0396",
        "\\%",  "\u00b0",
        "\\?i", "\u0131",
        "\\&s", "\u00df",
        "\\\\sim", "~",
        "\\\\", "\\",
        "\\\\simeq", "\u2245",
        "\\\\infty", "\u221e",
        "\\\\times", "\u00d7",
        "\\\\square", "\u00b2",
        "\\\\neq", "\u2260",
        "\\\\rangle", "\u232a",
        "\\\\langle", "\u2329",
        "\\\\rightarrow", "\u2192",
        "\\\\leftarrow", "\u2190"
    };
    
    private final static String[] combiningChars = new String[] {
        "\\'", "\u0301",                                                           
        "\\`", "\u0300",
        "\\^", "\u0302",
        "\\,", "\u0327",
        "\\\"", "\u0308",
        "\\~", "\u0303",
        "\\;", "\u0328",
        "\\>", "\u030b",
        "\\=", "\u0305",
        "\\.", "\u0307",
        "\\<", "\u030C",
        "\\(", "\u0306",
        "\\/", "\u0337"
    };

    private final static String[] specialCases = new String[] {
        "\\%a", "a\u030a",
        "\\%A", "A\u030a",
        "\\'\\=\\%a", "a\u0301\u0305\u030a",
        "\\%b", "\u00b0b",
        "\\%B", "\u00b0B",
        "\\?",  "?",
        "\\?a", "?a",
        "\\?A", "?A",
        "\\?I", "I",
        "\\&",  "&",
        "\\&S", "SS",
        "\\'\\&S", "S\u0301S",
        "\\&A", "&A",
        "\\&q", "&q",
        "--", "\u2014",
        "+-", "\u00b1",
        "-+", "\u2213",
        "---", "---",
        "--+", "--+",
        "-+-", "-+-",
        "+--", "+--",
        "-++", "-++",
        "+-+", "+-+",
        "++-", "++-",
        "foo-- +-bar", "foo\u2014 \u00b1bar"
    };
    
    private final static String[] fragments =
            new String[] {"Where's", "the", "beef?", "spam", "penguins"};
    
    private static Test createOneCharTests() {
        TestSuite ts = new TestSuite("One-character Translations");
        
        for (int i = 0; i < translationData.length; i += 2) {
            ts.addTest(new EPTestCase(translationData[i],
                                      translationData[i + 1]));
        }
        
        return ts;
    }
    
    private static Test createCombiningTranslationTests() {
        TestSuite ts = new TestSuite("Combining-character Translations");
        
        for (int i = 0; i < combiningChars.length; i += 2) {
            ts.addTest(new EPTestCase(combiningChars[i],
                                      " " + combiningChars[i + 1]));
        }
        
        return ts;
    }
    
    private static Test createSimpleCombiningTests() {
        TestSuite ts = new TestSuite("Combining / Base Character Combinations");
        
        for (int i = 0; i < combiningChars.length; i += 2) {
            for (char c = '\u0032'; c < '\u001f'; c++) {
                ts.addTest(new EPTestCase(combiningChars[i] + c,
                                          c + combiningChars[i + 1]));
            }
            for (int j = 0; j < translationData.length; j += 2) {
                ts.addTest(new EPTestCase(
                        combiningChars[i] + translationData[j],
                        translationData[j + 1] + combiningChars[i + 1]));
            }
        }
        
        return ts;
    }
    
    private static Test createComplexCombiningTests() {
        TestSuite ts = new TestSuite("Complex Combinations");
        
        for (int key = 0; key < 1024; key++) {
            StringBuffer input = new StringBuffer();
            StringBuffer expected = new StringBuffer();
            int fragIndex = key % fragments.length;
            
            input.append(fragments[fragIndex]);
            expected.append(fragments[fragIndex]);
            
            for (int nfrag = 0; nfrag <= ((key + 2) % 3); nfrag++) {
                fragIndex = (fragIndex + 1) % fragments.length;
                expected.append(fragments[fragIndex].charAt(0));
                for (int i = 0; i < (key % 3) + 1; i++) {
                    int c = ((key + i) % (combiningChars.length / 2)) * 2;
                    input.append(combiningChars[c]);
                    expected.append(combiningChars[c + 1]);
                }
                input.append(fragments[fragIndex]);
                expected.append(fragments[fragIndex].substring(1));
            }
            
            ts.addTest(new EPTestCase(input.toString(), expected.toString()));
        }
        
        return ts;
    }
    
    private static Test createSpecialCaseTests() {
        TestSuite ts = new TestSuite("Special Case Translations");
        
        for (int i = 0; i < specialCases.length; i += 2) {
            ts.addTest(new EPTestCase(specialCases[i], specialCases[i + 1]));
        }
        
        return ts;
    }
    
    /**
     * Creates and returns a {@code Test} with which to evaluate several test
     * datum (input / output) pairs 
     * 
     * @return a {@code Test} suitable for evaluating the defined test cases
     */
    public static Test suite() {
        TestSuite ts = new TestSuite("Escape Processing Tests");
        
        ts.addTest(createOneCharTests());
        ts.addTest(createCombiningTranslationTests());
        ts.addTest(createSpecialCaseTests());
        ts.addTest(createSimpleCombiningTests());
        ts.addTest(createComplexCombiningTests());
        
        return ts;
    }
    
    /**
     * A {@code TestCase} subclass specifically for testing runs of the
     * {@code CifEscapeProcessor} class.
     * 
     * @author jobollin
     * @version 0.9
     */
    private static class EPTestCase extends TestCase {
        
        private final String input;
        private final String expected;
        
        /**
         * Initializes a new {@code EPTestCase} that will use a
         * {@code CifEscapeProcessor} to unescape the specified input and assert
         * that the result is equal to the specified output
         * 
         * @param  input a {@code String} containing the input
         * @param  expected a {@code String} containing the expected
         *         unescaped result
         */
        public EPTestCase(String input, String expected) {
            super("test_escaping: <" + input + ">");
            
            this.input = input;
            this.expected = expected;
        }
        
        /**
         * {@inheritDoc}.  This version creates a {@code CifEscapeProcessor},
         * feeds the provided input to it, and asserts that the result matches
         * the provided expected output.
         * 
         * @see junit.framework.TestCase#runTest()
         */
        @Override
        public void runTest() {
            CifEscapeProcessor proc = new CifEscapeProcessor();
            assertEquals("Escape processing produced incorrect results",
                         expected, proc.unescapeCifValue(input));
        }
    }
}
