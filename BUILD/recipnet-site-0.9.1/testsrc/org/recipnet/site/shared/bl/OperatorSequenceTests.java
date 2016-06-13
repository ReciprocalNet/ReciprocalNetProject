/*
 */

package org.recipnet.site.shared.bl;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OperatorSequenceTests extends TestCase {

    private static final Set<String> FIRST_CHARS = new LinkedHashSet<String>(
            Arrays.asList(new String[] {"-", "\u2212", "1", "2", "3", "4", "5",
                    "6", "a", "b", "c", "d", "m", "n"}));

    public OperatorSequenceTests(String testName) {
        super(testName);
    }

    public void testConstructor__null() {
        try {
            new SpaceGroupSymbolBL.OperatorSequence(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    static void assertEndOfSequence(Enumeration<?> seq) {
        assertFalse("Sequence erroneously reports having more elements",
                    seq.hasMoreElements());
        try {
            seq.nextElement();
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException nsee) {
            // The expected case
        }
    }

    static Object assertHasAnotherElement(Enumeration<?> seq) {
        assertTrue("Sequence erroneously reports having no more elements",
                    seq.hasMoreElements());
        return seq.nextElement();
    }
   
    public void testCondition__noOperators() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(new ArrayList<Object>()));

        assertEndOfSequence(seq);
    }

    public void testCondition__invalidOperator1() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(Collections.enumeration(
                        Arrays.asList(new String[] {"q", "m", "m"})));

        assertEndOfSequence(seq);
    }

    public void testCondition__invalidOperator2() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(Collections.enumeration(
                        Arrays.asList(new String[] {"m", "q", "m"})));

        assertHasAnotherElement(seq);
        assertEndOfSequence(seq);
    }

    public void testCondition__invalidOperator3() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(Collections.enumeration(
                        Arrays.asList(new String[] {"m", "m", "q"})));

        assertHasAnotherElement(seq);
        assertHasAnotherElement(seq);
        assertEndOfSequence(seq);
    }

    public void testCondition__manyOperators() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(Collections.enumeration(
                        Arrays.asList(new String[] {"m", "m", "m", "q"})));

        assertHasAnotherElement(seq);
        assertHasAnotherElement(seq);
        assertHasAnotherElement(seq);
        assertEndOfSequence(seq);

        seq = new SpaceGroupSymbolBL.OperatorSequence(Collections.enumeration(
                Arrays.asList(new String[] {"m", "m", "m", "m"})));

        assertHasAnotherElement(seq);
        assertHasAnotherElement(seq);
        assertHasAnotherElement(seq);
        assertEndOfSequence(seq);
    }

    public void testCondition__parentheses() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("2(1)")));

        assertEquals("Parentheses incorrectly stripped", "21",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("2((1))")));
        assertEquals("Parentheses incorrectly stripped", "21",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("2(1))")));
        assertEquals("Parentheses incorrectly stripped", "21",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("m(oose))")));
        assertEquals("Parentheses incorrectly stripped", "moose",
                     assertHasAnotherElement(seq));

    }

    public void testCondition__capitals() {
        SpaceGroupSymbolBL.OperatorSequence seq =
                new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("M")));

        assertEquals("Case conversion incorrect", "m",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("BAR")));
        assertEquals("Case conversion incorrect", "bar",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("3bAr")));
        assertEquals("Case conversion incorrect", "3bar",
                     assertHasAnotherElement(seq));

        seq = new SpaceGroupSymbolBL.OperatorSequence(
                        Collections.enumeration(
                                Collections.singletonList("6/F0O")));
        assertEquals("Case conversion incorrect", "6/f0o",
                     assertHasAnotherElement(seq));

    }

    private static Test createGoodFirstCharsTests() {
        TestSuite ts = new TestSuite();

        for (Iterator<String> it = FIRST_CHARS.iterator(); it.hasNext(); ) {
            final String s = it.next();

            ts.addTest(
                new TestCase("testGoodFirstChar_" + s) {
                    @Override
                    public void runTest() throws Throwable {
                        SpaceGroupSymbolBL.OperatorSequence seq =
                            new SpaceGroupSymbolBL.OperatorSequence(
                                Collections.enumeration(
                                    Collections.singletonList(s)));

                        assertHasAnotherElement(seq);
                        assertEndOfSequence(seq);
                    }
                });
        }

        return ts;
    }

    private static Test createBadFirstCharsTests() {
        TestSuite ts = new TestSuite();

        for (char c = '\0'; c < 256; c++) {
            final String tString = "" + c;
            String name =
                    ((c > 0x20) && (c < 0x7f)) ? tString : ("0x" + (int) c);

            if (FIRST_CHARS.contains(tString.toLowerCase())) {
                continue;
            }

            ts.addTest(
                new TestCase("testBadFirstChar_" + name) {
                    @Override
                    public void runTest() throws Throwable {
                        SpaceGroupSymbolBL.OperatorSequence seq =
                            new SpaceGroupSymbolBL.OperatorSequence(
                                Collections.enumeration(
                                    Collections.singletonList(tString)));

                        assertEndOfSequence(seq);
                    }
                });
        }

        return ts;
    }

    public static Test suite() {
        TestSuite ts = new TestSuite(OperatorSequenceTests.class);

        ts.addTest(createGoodFirstCharsTests());
        ts.addTest(createBadFirstCharsTests());

        return ts;
    }
}

