/*
 * IUMSC Reciprocal Net Project
 *
 * SpaceGroupSymbolBLTests.java
 */

package org.recipnet.site.shared.bl;

import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.LaueClass;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.Operator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SpaceGroupSymbolBLTests extends TestCase {

    static {
        SpaceGroupSymbolBLTests.class.getClassLoader().setPackageAssertionStatus(
                "org.recipnet", true);

    }

    private static final String[] formattedCanonicalSymbols = new String[] {
            "P 1",        "P -1",       "P 2",        "P 21",       "C 2",
            "P m",        "P c",        "C m",        "C c",        "P 2/m",
            "P 21/m",     "C 2/m",      "P 2/c",      "P 21/c",     "C 2/c",
            "P 2 2 2",    "P 2 2 21",   "P 21 21 2",  "P 21 21 21", "C 2 2 21",
            "C 2 2 2",    "F 2 2 2",    "I 2 2 2",    "I 21 21 21", "P m m 2",
            "P m c 21",   "P c c 2",    "P m a 2",    "P c a 21",   "P n c 2",
            "P m n 21",   "P b a 2",    "P n a 21",   "P n n 2",    "C m m 2",
            "C m c 21",   "C c c 2",    "A m m 2",    "A b m 2",    "A m a 2",
            "A b a 2",    "F m m 2",    "F d d 2",    "I m m 2",    "I b a 2",
            "I m a 2",    "P m m m",    "P n n n",    "P c c m",    "P b a n",
            "P m m a",    "P n n a",    "P m n a",    "P c c a",    "P b a m",
            "P c c n",    "P b c m",    "P n n m",    "P m m n",    "P b c n",
            "P b c a",    "P n m a",    "C m c m",    "C m c a",    "C m m m",
            "C c c m",    "C m m a",    "C c c a",    "F m m m",    "F d d d",
            "I m m m",    "I b a m",    "I b c a",    "I m m a",    "P 4",
            "P 41",       "P 42",     /*"P 43",*/     "I 4",        "I 41",
            "P -4",       "I -4",       "P 4/m",      "P 42/m",     "P 4/n",
            "P 42/n",     "I 4/m",      "I 41/a",     "P 4 2 2",    "P 4 21 2",
            "P 41 2 2",   "P 41 21 2",  "P 42 2 2",   "P 42 21 2",/*"P 43 2 2",
            "P 43 21 2",*/"I 4 2 2",    "I 41 2 2",   "P 4 m m",    "P 4 b m",
            "P 42 c m",   "P 42 n m",   "P 4 c c",    "P 4 n c",    "P 42 m c",
            "P 42 b c",   "I 4 m m",    "I 4 c m",    "I 41 m d",   "I 41 c d",
            "P -4 2 m",   "P -4 2 c",   "P -4 21 m",  "P -4 21 c",  "I -4 m 2",
            "P -4 c 2",   "P -4 b 2",   "P -4 n 2",   "P -4 m 2",   "I -4 c 2",
            "I -4 2 m",   "I -4 2 d",   "P 4/m m m",  "P 4/m c c",  "P 4/n b m",
            "P 4/n n c",  "P 4/m b m",  "P 4/m n c",  "P 4/n m m",  "P 4/n c c",
            "P 42/m m c", "P 42/m c m", "P 42/n b c", "P 42/n n m", "P 42/m b c",
            "P 42/m n m", "P 42/n m c", "P 42/n c m", "I 4/m m m",  "I 4/m c m",
            "I 41/a m d", "I 41/a c d", "P 3",        "P 31",     /*"P 32",*/
            "R 3",        "P -3",       "R -3",       "P 3 1 2",    "P 3 2 1",
            "P 31 1 2",   "P 31 2 1", /*"P 32 1 2",   "P 32 2 1",*/ "R 3 2",
            "P 3 m 1",    "P 3 1 m",    "P 3 c 1",    "P 3 1 c",    "R 3 m",
            "R 3 c",      "P -3 1 m",   "P -3 1 c",   "P -3 m 1",   "P -3 c 1",
            "R -3 m",     "R -3 c",     "P 6",        "P 61",     /*"P 65",*/
            "P 62",     /*"P 64",*/     "P 63",       "P -6",       "P 6/m",
            "P 63/m",     "P 6 2 2",    "P 61 2 2", /*"P 65 2 2",*/ "P 62 2 2",
          /*"P 64 2 2",*/ "P 63 2 2",   "P 6 m m",    "P 6 c c",    "P 63 c m",
            "P 63 m c",   "P -6 m 2",   "P -6 c 2",   "P -6 2 m",   "P -6 2 c",
            "P 6/m m m",  "P 6/m c c",  "P 63/m c m", "P 63/m m c", "P 2 3",
            "F 2 3",      "I 2 3",      "P 21 3",     "I 21 3",     "P m -3",
            "P n -3",     "F m -3",     "F d -3",     "I m -3",     "P a -3",
            "I a -3",     "P 4 3 2",    "P 42 3 2",   "F 4 3 2",    "F 41 3 2",
            "I 4 3 2",  /*"P 43 3 2",*/ "P 41 3 2",   "I 41 3 2",   "P -4 3 m",
            "F -4 3 m",   "I -4 3 m",   "P -4 3 n",   "F -4 3 c",   "I -4 3 d",
            "P m -3 m",   "P n -3 n",   "P m -3 n",   "P n -3 m",   "F m -3 m",
            "F m -3 c",   "F d -3 m",   "F d -3 c",   "I m -3 m",   "I a -3 d"
    };

    private static final Map<String, String> enantiomorphicPairMap
            = new LinkedHashMap<String, String>();

    static {
        enantiomorphicPairMap.put("P 43", "P 41");
        enantiomorphicPairMap.put("P 43 2 2", "P 41 2 2");
        enantiomorphicPairMap.put("P 43 21 2", "P 41 21 2");
        enantiomorphicPairMap.put("P 32", "P 31");
        enantiomorphicPairMap.put("P 32 2 1", "P 31 2 1");
        enantiomorphicPairMap.put("P 32 1 2", "P 31 1 2");
        enantiomorphicPairMap.put("P 64", "P 62");
        enantiomorphicPairMap.put("P 65", "P 61");
        enantiomorphicPairMap.put("P 64 2 2", "P 62 2 2");
        enantiomorphicPairMap.put("P 65 2 2", "P 61 2 2");
        enantiomorphicPairMap.put("P 43 3 2", "P 41 3 2");
    }

    private static final Map<LaueClass, String[]> laueClassMap
            = new LinkedHashMap<LaueClass, String[]>();
    private static final Set<String> pointGroups = new HashSet<String>();

    static {
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TRICLINIC,
                new String[] {"1", "-1"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.MONOCLINIC,
                new String[] {"2", "m", "2/m", "2 1 1", "1 2 1" /*,"1 1 2"*/,
                              "m 1 1", "1 m 1", /*"1 1 m",*/
                              "2/m 1 1", "1 2/m 1" /*, "1 1 m"*/ });
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.ORTHORHOMBIC,
                new String[] {"2 2 2", "m m 2", "m 2 m", "2 m m", "m m m", 
                              "2/m 2/m 2", "2/m 2 2/m", "2 2/m 2/m",
                              "2/m 2/m 2/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TETRAGONAL_LOW,
                new String[] {"4", "4/m", "-4"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TETRAGONAL_HIGH,
                new String[] {"4 2 2", "4 2 m", "4 m 2", "4 m m", "4/m m m",
                              "4/m m 2", "4/m 2 m", "4/m 2 2", "4 2 2/m",
                              "4 2/m 2", "4 2/m 2/m", "4/m 2/m 2/m",
                              "4/m 2/m 2", "4/m 2 2/m", "4 m 2/m", "4 2/m m",
                              "4/m m 2/m", "4/m 2/m m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.RHOMBOHEDRAL_LOW,
                new String[] {"3", "-3"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.RHOMBOHEDRAL_HIGH,
                new String[] {"3 2", "3 m", "-3 2", "-3 m", "3 2/m", "-3 2/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TRIGONAL_LOW,
                new String[] {"3", "-3", "3/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TRIGONAL_HIGH_1,
                new String[] {"3 2 1", "3 m 1", "-3 2 1", "-3 m 1", "3 2/m 1",
                              "-3 2/m 1"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.TRIGONAL_HIGH_2,
                new String[] {"3 1 2", "3 1 m", "-3 1 2", "-3 1 m", "3 1 2/m",
                              "-3 1 2/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.HEXAGONAL_LOW,
                new String[] {"6", "-6", "6/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.HEXAGONAL_HIGH,
                new String[] {"6 2 2", "6 m 2", "6 2 m", "6 m m", "6/m m m",
                              "6/m m 2", "6/m 2 m", "6/m 2 2", "6 2/m 2",
                              "6 2 2/m", "6 2/m m", "6 m 2/m", "6 2/m 2/m",
                              "6/m 2/m m", "6/m m 2/m", "6/m 2/m 2",
                              "6/m 2 2/m", "6/m 2/m 2/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.CUBIC_LOW,
                new String[] {"2 3", "m 3", "2 -3", "m -3", "2 3", "2/m 3",
                              "2 -3", "2/m -3"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.CUBIC_HIGH_SPECIAL,
                new String[] {"4 3 2", "4 3 m", "-4 3 2", "-4 3 m", "4 -3 2",
                              "4 -3 m", "-4 -3 2", "-4 -3 m", "4 3 2/m",
                              "-4 3 2/m", "4 -3 2/m", "-4 -3 2/m"});
        laueClassMap.put(SpaceGroupSymbolBL.LaueClass.CUBIC_HIGH,
                new String[] {"m 3 2", "m 3 m", "m -3 2", "m -3 m", "4/m 3 2",
                              "4/m 3 m", "4/m -3 2", "4/m -3 m", "m 3 2/m",
                              "m -3 2/m", "4/m 3 2/m", "4/m -3 2/m"});

        for (String[] groups : laueClassMap.values()) {
            Collections.addAll(pointGroups, groups);
        }
        pointGroups.add("1 1 2");
        pointGroups.add("1 1 m");
    }

    public SpaceGroupSymbolBLTests(String testName) {
        super(testName);
    }

    /*
     * These specific cases were problems at one time or another
     */

    public void testPb3bar() throws Exception {
        assertEquals("Wrong canonical symbol", "P a -3",
                     SpaceGroupSymbolBL.createCanonicalSymbol("P b -3"));
    }

    public void testCcmm() throws Exception {
        assertEquals("Wrong canonical symbol", "C m c m",
                     SpaceGroupSymbolBL.createCanonicalSymbol("C c m m"));
    }

    public void testCcma() throws Exception {
        assertEquals("Wrong canonical symbol", "C m c a",
                     SpaceGroupSymbolBL.createCanonicalSymbol("C c m a"));
    }

    public void testPcam() throws Exception {
        assertEquals("Wrong canonical symbol", "P b c m",
                     SpaceGroupSymbolBL.createCanonicalSymbol("P c a m"));
    }

    public void testPcan() throws Exception {
        assertEquals("Wrong canonical symbol", "P b c n",
                     SpaceGroupSymbolBL.createCanonicalSymbol("P c a n"));
    }

    /*
     * this test is written up apart from the auto-generated ones because it
     * depends on a non-primitive centering symbol
     */
    public void testDetermineMonoclinicLaueClass() throws Exception {
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("P 1 1 2"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("P 1 1 2/m"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("P 1 1 m"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("B 1 1 2"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("B 1 1 2/m"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        assertLaueClass(SpaceGroupSymbolBL.digestSymbol("B 1 1 m"),
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
    }

    /*
     * Standard test methods 
     */

    public void testMethod_createFormattedSymbol__null() {
        try {
            SpaceGroupSymbolBL.createFormattedSymbol(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    public void testMethod_createFormattedSymbol__empty() {
        assertEquals("Wrong formatted symbol", "",
                     SpaceGroupSymbolBL.createFormattedSymbol(""));
        assertEquals("Wrong formatted symbol", "",
                     SpaceGroupSymbolBL.createFormattedSymbol("  "));
    }

    public void testMethod_createFormatedSymbol__wsConversion() {
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 23/m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X  1   d  23/m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X\t1   d  23/m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("\t X1 d\r23/m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X1 d 23/m\n"));
    }

    public void testMethod_createFormattedSymbol__delimAndParen() {
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 23 /m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 23/ m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 23 / m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 2(3)/m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 2(3) /m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 2(3)/ m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 2(3) / m"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 2(3) /  m"));
    }

    public void testMethod_createFormattedSymbol__delimitedBar() {
        assertEquals("Wrong formatted symbol", "P -1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P 1bar"));
        assertEquals("Wrong formatted symbol", "P -1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P 1Bar"));
        assertEquals("Wrong formatted symbol", "P -1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P 1baR"));
        assertEquals("Wrong formatted symbol", "P -1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P 1BAR"));
        assertEquals("Wrong formatted symbol", "P -5 -4 -3",
                     SpaceGroupSymbolBL.createFormattedSymbol(
                             " P 5bar 4bar 3bar"));
    }

    public void testMethod_createFormattedSymbol__ManyOperators() {
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 d 23/m b w"));
        assertEquals("Wrong formatted symbol", "X 1 d 23/m",
                     SpaceGroupSymbolBL.createFormattedSymbol("X1d2(3)/mbw"));
        assertEquals("Wrong formatted symbol", "X n a a",
                     SpaceGroupSymbolBL.createFormattedSymbol("Xnaabar"));
    }

    public void testMethod_createFormattedSymbol__DelimitedOdd() {
        assertEquals("Wrong formatted symbol", "X",
                     SpaceGroupSymbolBL.createFormattedSymbol(" \r\tX\n"));
        assertEquals("Wrong formatted symbol", "X 1 -d",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 -d"));
        assertEquals("Wrong formatted symbol", "X 1 -32",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 -32"));
        assertEquals("Wrong formatted symbol", "X 1 -32/n",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 -32 / n"));
        assertEquals("Wrong formatted symbol", "X 1 - n",
                     SpaceGroupSymbolBL.createFormattedSymbol("X 1 - n"));
        assertEquals("Wrong formatted symbol", "[ 1 2 n",
                     SpaceGroupSymbolBL.createFormattedSymbol("[ 1 2 n"));
        assertEquals("Wrong formatted symbol", "Q 2/",
                     SpaceGroupSymbolBL.createFormattedSymbol("Q 2/"));
        assertEquals("Wrong formatted symbol", "Q m 2/",
                     SpaceGroupSymbolBL.createFormattedSymbol("Q m 2 / "));
        assertEquals("Wrong formatted symbol", "Q m 2/3",
                     SpaceGroupSymbolBL.createFormattedSymbol("Q m 2/3"));
        assertEquals("Wrong formatted symbol", "Q 2 a/b",
                     SpaceGroupSymbolBL.createFormattedSymbol("Q 2 a/b"));
        assertEquals("Wrong formatted symbol", "P nma center at",
                     SpaceGroupSymbolBL.createFormattedSymbol(
                             "P nma (center at nma)"));
    }

    public void testMethod_createFormattedSymbol__Undelimited() {
        assertEquals("Wrong formatted symbol", "P 1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P1"));
        assertEquals("Wrong formatted symbol", "P 1",
                     SpaceGroupSymbolBL.createFormattedSymbol("P1 "));
        assertEquals("Wrong formatted symbol", "P 1",
                     SpaceGroupSymbolBL.createFormattedSymbol(" P1\t\r\n"));
        assertEquals("Wrong formatted symbol", "L",
                     SpaceGroupSymbolBL.createFormattedSymbol("L"));
        assertEquals("Wrong formatted symbol", "L 2 3 3",
                     SpaceGroupSymbolBL.createFormattedSymbol("L233"));
        assertEquals("Wrong formatted symbol", "L 23 3",
                     SpaceGroupSymbolBL.createFormattedSymbol("L2(3)3"));
        assertEquals("Wrong formatted symbol", "L 23/b 3",
                     SpaceGroupSymbolBL.createFormattedSymbol("L2(3)/b3"));
        assertEquals("Wrong formatted symbol", "L -2 -3 b",
                     SpaceGroupSymbolBL.createFormattedSymbol("L-2-3b"));
    }

    public void testMethod_createFormattedSymbol__UndelimitedOdd() {
        assertEquals("Wrong formatted symbol", "K -23 3",
                     SpaceGroupSymbolBL.createFormattedSymbol("K-2(3)3"));
        assertEquals("Wrong formatted symbol", "K -2/b 3 d",
                     SpaceGroupSymbolBL.createFormattedSymbol("K-2/b3d"));
        assertEquals("Wrong formatted symbol", "K 5/a 3 d",
                     SpaceGroupSymbolBL.createFormattedSymbol("K5/a3d"));
        assertEquals("Wrong formatted symbol", "K",
                     SpaceGroupSymbolBL.createFormattedSymbol("K/a"));
        assertEquals("Wrong formatted symbol", "K m",
                     SpaceGroupSymbolBL.createFormattedSymbol("Km/a"));
    }

    public void testMethod_createFormattedSymbol__UndelimitedBar() {
        assertEquals("Wrong formatted symbol", "P 1 b a",
                     SpaceGroupSymbolBL.createFormattedSymbol("P1BAR"));
    }

    public void testMethod_digestSymbol__null() throws Exception {
        try {
            SpaceGroupSymbolBL.digestSymbol(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    public void testMethod_digestSymbol__empty() {
        try {
            SpaceGroupSymbolBL.digestSymbol("");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
    }

    public void testMethod_digestSymbol__badCenter_noOps() {
        try {
            SpaceGroupSymbolBL.digestSymbol("D");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("!");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("Q");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("a");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("g");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
    }

    public void testMethod_digestSymbol__badCenter_oneOp() {
        try {
            SpaceGroupSymbolBL.digestSymbol("D 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("! 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("Q 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("a 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("g 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // the expected case
        }
    }

    public void testMethod_digestSymbol__goodCenter_noOps() throws Exception {
        SpaceGroupSymbolBL.SpaceGroupSymbol symbol;

        symbol = SpaceGroupSymbolBL.digestSymbol("P");
        assertEquals("Wrong centering symbol", 'P', symbol.getCentering());
        assertEquals("Wrong number of operators", 0,
                     symbol.getOperators().size());

        symbol = SpaceGroupSymbolBL.digestSymbol("C");
        assertEquals("Wrong centering symbol", 'C', symbol.getCentering());
        assertEquals("Wrong number of operators", 0,
                     symbol.getOperators().size());

        symbol = SpaceGroupSymbolBL.digestSymbol("F");
        assertEquals("Wrong centering symbol", 'F', symbol.getCentering());
        assertEquals("Wrong number of operators", 0,
                     symbol.getOperators().size());
    }

    public void testMethod_digestSymbol__op0Space() {

        // Centering symbol not followed by space

        try {
            SpaceGroupSymbolBL.digestSymbol("P1");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P222");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("Pm 3");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__interopSpace() {

        // operators not space delimited

        try {
            SpaceGroupSymbolBL.digestSymbol("P mm m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1bar");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("R 3m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 31m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F d dd");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__parentheses() {

        // parentheses in symbol

        try {
            SpaceGroupSymbolBL.digestSymbol("P 2(1)");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 6(3)/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 2(1) 1");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m -3 6(2)");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__overNothing() {

        // over nothing

        try {
            SpaceGroupSymbolBL.digestSymbol("P 2/");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 6(3)/");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 2(1)/ 1");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m -3 2/");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__nothingOver() {

        // nothing over

        try {
            SpaceGroupSymbolBL.digestSymbol("P /m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P m /n n");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 1 /c");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__inversionMirror() {

        // -digit/reflection

        try {
            SpaceGroupSymbolBL.digestSymbol("P -4/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 4 -3/m 2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 -6/a 3");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__inversionScrew() {

        // -digitdigit

        try {
            SpaceGroupSymbolBL.digestSymbol("P -41");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 -31");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m -62");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__invalidRotationOrder() {

        try {
            SpaceGroupSymbolBL.digestSymbol("P 0");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P -0");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 5");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 -5");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 53");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m 7/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m 72/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P -2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 1 -2 1");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__invalidScrew() {

        try {
            SpaceGroupSymbolBL.digestSymbol("P 11");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 20");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m 49");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("I 23");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__invalidMirror() {

        try {
            SpaceGroupSymbolBL.digestSymbol("P o");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 2/o");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P m r");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P m 4/r");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m e n");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F 2/m 2/e 2/n");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("I n a g");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("I n a 21/g");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__mOverM() {

        try {
            SpaceGroupSymbolBL.digestSymbol("P a/b");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 3 c/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m n/m");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    public void testMethod_digestSymbol__digitOverDigit() {

        try {
            SpaceGroupSymbolBL.digestSymbol("P 2/2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("P 3 2/3");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
        try {
            SpaceGroupSymbolBL.digestSymbol("F m m 4/2");
            fail("Expected an InvalidDataException");
        } catch (InvalidDataException ide) {
            // The expected case
        }
    }

    /*
     * Methods used to semi-automatically synthesize test cases
     */

    static void assertLaueClass(SpaceGroupSymbolBL.SpaceGroupSymbol symbol,
                                SpaceGroupSymbolBL.LaueClass expectedClass)
            throws Exception {
        assertTrue("Wrong Laue class generated", expectedClass ==
                   SpaceGroupSymbolBL.determineLaueClass(symbol));
    }

    static TestSuite createCanonicalSymbolTests() {
        TestSuite ts = new TestSuite();

        for (int i = 0; i < formattedCanonicalSymbols.length; i++) {
            ts.addTest(new TestCase("test_canonical: "
                    + formattedCanonicalSymbols[i]) {
                @Override
                public void runTest() throws Throwable {
                    String symbol = getName().substring(16);
                    assertEquals(
                            "Canonical symbol mangled by canonicalization",
                            symbol,
                            SpaceGroupSymbolBL.createCanonicalSymbol(
                                    symbol));
                }
            });
        }

        return ts;
    }

    static TestSuite createEnantiomorphicSymbolTests() {
        TestSuite ts = new TestSuite();

        for (String key : enantiomorphicPairMap.keySet()) {
            ts.addTest(new TestCase("test_enantiomorphic: " + key) {
                @Override
                public void runTest() throws Throwable {
                    String symbol = getName().substring(21);
                    
                    assertEquals(
                            "Enantiomorphic symbol not generated",
                            enantiomorphicPairMap.get(symbol),
                            SpaceGroupSymbolBL.createCanonicalSymbol(symbol));
                }
            });
        }

        return ts;
    }

    static TestSuite createGoodDetermineLaueClassTests() {
        TestSuite ts = new TestSuite();
        char[] centers = new char[] {'P', 'A', 'B', 'C', 'I', 'F', 'R'};

        for (Entry<LaueClass, String[]> entry : laueClassMap.entrySet()) {
            final SpaceGroupSymbolBL.LaueClass laueClass = entry.getKey();
            String[] pgroups = entry.getValue();

            for (int pgx = 0; pgx < pgroups.length; pgx++) {
                for (int cx = 0; cx < centers.length; cx++) {
                    if (!laueClass.allowsCentering(centers[cx])) {
                        continue;
                    }
                    final String symbol = centers[cx] + " " + pgroups[pgx];

                    ts.addTest(
                        new TestCase("test_determineLaueClass_Good: " + symbol) {
                            @Override
                            public void runTest() throws Throwable {
                                assertLaueClass(
                                        SpaceGroupSymbolBL.digestSymbol(symbol),
                                        laueClass);
                            }
                        });
                }
            }
        }

        return ts;
    }

    private static Test createBadLaueClassTestCase(String pgString) {
        final String sgString = "P " + pgString;

        return new TestCase("test_determineLaueClass_Bad: " + pgString) {
            @Override
            public void runTest() throws Throwable {
                try {
                    SpaceGroupSymbolBL.determineLaueClass(
                            SpaceGroupSymbolBL.digestSymbol(sgString));

                    fail("Expected an InvalidDataException");
                } catch (InvalidDataException ide) {
                    // the expected case
                }
            }
        };
    }

    static TestSuite createBadDetermineLaueClassTests() {
        TestSuite ts = new TestSuite();
        char[] chars = new char[] {'1', '2', '3', '4', '5', '6', 'm'};
        
        for (int i1 = 0; i1 < chars.length; i1++) {
            String s1 = String.valueOf(chars[i1]);

            if (!pointGroups.contains(s1)) {
                ts.addTest(createBadLaueClassTestCase(s1));
            }
            for (int i2 = 0; i2 < chars.length; i2++) {
                String s2 = s1 + " " + chars[i2];

                if (!pointGroups.contains(s2)) {
                    ts.addTest(createBadLaueClassTestCase(s2));
                }
                for (int i3 = 0; i3 < chars.length; i3++) {
                    String s3 = s2 + " " + chars[i3];

                    if (!pointGroups.contains(s3)) {
                        ts.addTest(createBadLaueClassTestCase(s3));
                    }
                }
            }
        }

        return ts;
    }

    static Test createDigestSymbolTest(final char expectedCenter,
                                       List<Operator> ops) {
        StringBuilder buf = new StringBuilder();

        buf.append(expectedCenter);
        for (Operator op : ops) {
            buf.append(' ').append(op.toString());
        }

        final String symbol = buf.toString();
        final List<Operator> expectedOps = new ArrayList<Operator>(ops);

        return new TestCase("testMethod_digestSymbol(" + symbol + ")") {
            @Override
            public void runTest() throws Throwable {
                SpaceGroupSymbolBL.SpaceGroupSymbol dsymbol =
                        SpaceGroupSymbolBL.digestSymbol(symbol);

                assertEquals("Wrong centering", expectedCenter,
                             dsymbol.getCentering());
                assertEquals("Wrong operator list", expectedOps,
                             dsymbol.getOperators());
            }
        };
    }

    static TestSuite createGoodDigestSymbolTests() {
        TestSuite ts = new TestSuite();
        SpaceGroupSymbolBL.Operator[] opArray =
                new SpaceGroupSymbolBL.Operator[] { 
                        new SpaceGroupSymbolBL.Operator(2, false, 0, ' '),
                        new SpaceGroupSymbolBL.Operator(0, false, 0, 'a'),
                        new SpaceGroupSymbolBL.Operator(2, false, 0, 'a'),
                        new SpaceGroupSymbolBL.Operator(3, true, 0, ' '),
                        new SpaceGroupSymbolBL.Operator(4, false,  1, ' '),
                        new SpaceGroupSymbolBL.Operator(4, false, 2, 'b')
                };

        for (int i = 0; i < opArray.length; i++) {
            List<Operator> list1 = new ArrayList<Operator>();

            list1.add(opArray[i]);
            ts.addTest(createDigestSymbolTest('P', list1));
            ts.addTest(createDigestSymbolTest('F', list1));

            for (int j = 0; j < opArray.length; j++) {
                List<Operator> list2 = new ArrayList<Operator>(list1);

                list2.add(opArray[j]);
                ts.addTest(createDigestSymbolTest('P', list2));
                ts.addTest(createDigestSymbolTest('F', list2));

                for (int k = 0; k < opArray.length; k++) {
                    List<Operator> list3 = new ArrayList<Operator>(list2);

                    list3.add(opArray[k]);
                    ts.addTest(createDigestSymbolTest('P', list3));
                    ts.addTest(createDigestSymbolTest('F', list3));
                }
            }
        }

        return ts;
    }

    /*
     * The master method by which all the test cases represented / created by
     * this class are accessed.
     */
    public static Test suite() {
        TestSuite ts = new TestSuite(SpaceGroupSymbolBLTests.class);

        ts.addTest(createCanonicalSymbolTests());
        ts.addTest(createEnantiomorphicSymbolTests());
        ts.addTest(createGoodDetermineLaueClassTests());
        ts.addTest(createBadDetermineLaueClassTests());

        return ts;
    }

}

