/*
 * Reciprocal Net Project
 *
 * ShelxOperatorEnumerationTests.java
 */

package org.recipnet.site.shared.bl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ShelxOperatorEnumerationTests extends TestCase {

    public ShelxOperatorEnumerationTests(String testName) {
        super(testName);
    }

    private static void assertEnumerationContents(Enumeration<?> e,
            List<?> expected) {
        assertEquals("The Enumeration had the wrong contents", expected,
                     Collections.list(e));
    }

    public void testConstructor__null() {
        try {
            new SpaceGroupSymbolBL.ShelxOperatorEnumeration(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    public void testCondition__empty() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration(""),
                Arrays.asList(new String[0]));
    }

    public void testAction__SeperateMirrors() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("mdzQ"),
                Arrays.asList(new String[] {"m", "d", "z", "Q"}));
    }

    public void testAction__SeperateRotations() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("7120"),
                Arrays.asList(new String[] {"7", "1", "2", "0"}));
    }

    public void testAction__SeperateScrews() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration(
                        "7(3)1(2)2(1)0(0)"),
                Arrays.asList(new String[] {"7(3)", "1(2)", "2(1)", "0(0)"}));
    }

    public void testAction__SeperateRotoInversions() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration(
                        "-7-1-2-0"),
                Arrays.asList(new String[] {"-7", "-1", "-2", "-0"}));
    }

    public void testAction__SeperateCompoundOps() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("7/m1/n2/d0/q"),
                Arrays.asList(new String[] {"7/m", "1/n", "2/d", "0/q"}));
    }

    public void testAction__SeperateMixedOps() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("7/mn2(3)/d-5"),
                Arrays.asList(new String[] {"7/m", "n", "2(3)/d", "-5"}));
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("47m3/n"),
                Arrays.asList(new String[] {"4", "7", "m", "3/n"}));
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("4/g-5(7)/ae-2"),
                Arrays.asList(new String[] {"4/g", "-5(7)/a", "e", "-2"}));
    }

    public void testAction__SeperateJunk() {
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("(3)m"),
                Arrays.asList(new String[] {"(3)m"}));
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("4(1)/acd(foo)"),
                Arrays.asList(new String[] {"4(1)/a", "c", "d", "(foo)"}));
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("3/m-n2/d"),
                Arrays.asList(new String[] {"3/m", "-n2/d"}));
        assertEnumerationContents(
                new SpaceGroupSymbolBL.ShelxOperatorEnumeration("ab2[1]"),
                Arrays.asList(new String[] {"a", "b", "2", "[1]"}));
    }

    public static Test createSingleOpTest(String baseName,
                                          final String tString) {
        return new TestCase(baseName + tString) {
            @Override
            public void runTest() throws Throwable {
                Enumeration<String> e =
                        new SpaceGroupSymbolBL.ShelxOperatorEnumeration(
                                tString);

                assertEnumerationContents(e, Collections.singletonList(tString));
            }
        };

    }

    public static Test createMirrorOpTests() {
        TestSuite ts = new TestSuite();

        for (char c = 'A'; c <= 'z'; c++) {
            if ((c > 'Z') && (c < 'a')) {
                continue;
            }
            ts.addTest(createSingleOpTest("testMirrorOp_", String.valueOf(c)));
        }

        return ts;
    }

    public static Test createRotationOpTests() {
        TestSuite ts = new TestSuite();

        for (int i = 0; i < 10; i++) {
            String iStr = String.valueOf(i);

            ts.addTest(createSingleOpTest("testRotationOp_", iStr));
            ts.addTest(createSingleOpTest("testRotationOp_", "-" + iStr));
            ts.addTest(createSingleOpTest("testRotationOp_", iStr + "/q"));
            ts.addTest(
                    createSingleOpTest("testRotationOp_", "-" + iStr + "/X"));

            for (int j = 0; j < 10; j++) {
                String ijStr = iStr + "(" + j + ")";

                ts.addTest(createSingleOpTest("testRotationOp_", ijStr));
                ts.addTest(createSingleOpTest("testRotationOp_", "-" + ijStr));
                ts.addTest(createSingleOpTest("testRotationOp_", ijStr + "/z"));
                ts.addTest(createSingleOpTest("testRotationOp_",
                                              "-" + ijStr + "/G"));
            }
        }

        return ts;
    }

    public static Test suite() {
        TestSuite ts = new TestSuite(ShelxOperatorEnumerationTests.class);

        ts.addTest(createMirrorOpTests());
        ts.addTest(createRotationOpTests());

        return ts;
    }
}

