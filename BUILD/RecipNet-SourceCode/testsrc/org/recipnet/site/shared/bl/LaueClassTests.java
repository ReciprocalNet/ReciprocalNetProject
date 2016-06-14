/*
 * IUMSC Reciprocal Net Project
 *
 * LaueClassTests.java
 */

package org.recipnet.site.shared.bl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import junit.framework.TestCase;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.Operator;

public class LaueClassTests extends TestCase {

    public LaueClassTests(String testName) {
        super(testName);
        getClass().getClassLoader().setPackageAssertionStatus(
                "org.recipnet", true);
    }

    public void testConstructor__null_intArray_String() {
        try {
            new SpaceGroupSymbolBL.LaueClass(null, new int[0], "");
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testConstructor__intArray_null_String() {
        try {
            new SpaceGroupSymbolBL.LaueClass(new int[0], null, "");
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testConstructor__intArray_intArray_null() {
        try {
            new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testConstructor__invalidDirection() {
        try {
            new SpaceGroupSymbolBL.LaueClass(
                    new int[] {7}, new int[] {2}, null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
        try {
            new SpaceGroupSymbolBL.LaueClass(
                    new int[] {-1}, new int[] {2}, null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testConstructor__mismatchedLengths() {
        try {
            new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 1}, new int[] {5}, "P");
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
        try {
            new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 1}, new int[] {5, 6, 7}, "P");
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testConstructor__arguments() {
        int[] dirs = new int[] {2, 1, 5};
        int[] orders = new int[] {3, 4, 5};
        String centers = "PFM";

        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(dirs, orders, centers);
        assertEquals("Wrong number of symmetry directions", dirs.length,
                     lc.getNumDirections());
        for (char c = 0; c < 65535; c++) {
            assertTrue("Accepted centers did not match",
                lc.allowsCentering(c) == (centers.indexOf(c) >= 0));
        }
        for (int i = 0; i < dirs.length; i++) {
            assertEquals("Wrong direction returned", dirs[i],
                    lc.getSymmetryDirection(i));
            assertEquals("Wrong expected order returned", orders[i],
                    lc.getExpectedOrder(dirs[i]));
        }
        assertEquals("Wrong order for non-symmetry axis", 1,
                lc.getExpectedOrder(0));
        assertEquals("Wrong order for non-symmetry axis", 1,
                lc.getExpectedOrder(3));
        assertEquals("Wrong order for non-symmetry axis", 1,
                lc.getExpectedOrder(4));

        // Check default representative directions:
        for (int i = -1; i < 6; i++) {
            assertEquals("Symmetry direction not representative of itself", i,
                         lc.getRepresentativeDirection(i));
        }

        // Test the exact classes of the utility objects:
        assertEquals("Wrong Operator Comparator class",
                     SpaceGroupSymbolBL.OperatorComparator.class,
                     lc.getOperatorComparator('P').getClass());
        assertEquals("Wrong SymbolAnalyzer class",
                     SpaceGroupSymbolBL.BasicSymbolAnalyzer.class,
                     lc.getSymbolAnalyzer().getClass());
        assertEquals("Wrong MatrixManager class",
                     SpaceGroupSymbolBL.MatrixManager.class,
                     lc.getMatrixManager().getClass());
    }

    public void testAccessors_representativeDirection__good() {
        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(new int[] {0, 1, 2, 3, 4, 5},
                        new int[] {1, 1, 1, 1, 1, 1}, "P");

        for (int dir = 0; dir < 6; dir++) {
            for (int rep = 0; rep < 6; rep++) {
                lc.setRepresentativeDirection(dir, rep);
                assertEquals("Wrong representative direction", rep,
                             lc.getRepresentativeDirection(dir));
            }
        }
    }

    public void testAccessors_representativeDirection__notRep() {
        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(new int[] {0, 1, 2},
                        new int[] {1, 1, 1}, "P");

        for (int dir = 0; dir < 6; dir++) {
            for (int rep = 3; rep < 6; rep++) {
                try {
                    lc.setRepresentativeDirection(dir, rep);
                    fail("Expected an AssertionError");
                } catch (Exception e) {
                    fail("Wrong type of throwable: " + e
                         + "; expected an AssertionError");
                } catch (AssertionError ae) {
                    // the expected case
                }
            }
        }
    }

    public void testMethod_setOperatorComparator__null() {
        try {
            SpaceGroupSymbolBL.LaueClass lc =
                    new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0],
                                                     "P");
            lc.setOperatorComparator(null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testAccessors_OperatorComparator() {
        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], "P");
        Comparator<Operator> dc = lc.getOperatorComparator('P');

        // A Comparator rather sure to be unequal to the LaueClass' default one:
        Comparator<Operator> c = new Comparator<Operator>() {
            public int compare(Operator o1, Operator o2) {
                return (o1.hashCode() - o2.hashCode());
            }
        };

        lc.setOperatorComparator('F', c);

        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('F'));
        assertEquals("Wrong OperatorComparator returned", dc,
                     lc.getOperatorComparator('I'));

        lc.setOperatorComparator(c);
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('P'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('A'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('B'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('C'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('I'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('R'));
        assertEquals("Wrong OperatorComparator returned", c,
                     lc.getOperatorComparator('F'));
    }

    public void testMethod_setSymbolAnalyzer__null() {
        try {
            SpaceGroupSymbolBL.LaueClass lc =
                    new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0],
                                                     "P");
            lc.setSymbolAnalyzer(null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testAccessors_SymbolAnalyzer() {
        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], "P");

        /*
         * An AbstractSymbolAnalyzer rather certain to be unequal to the
         * LaueClass' default one:
         */
        SpaceGroupSymbolBL.AbstractSymbolAnalyzer sa =
                new SpaceGroupSymbolBL.AbstractSymbolAnalyzer(lc) {
                    @Override
                    public List<SymmetryMatrix> getGenerators(
                            SpaceGroupSymbolBL.SpaceGroupSymbol s) {
                        return new ArrayList<SymmetryMatrix>();
                    }
        };

        lc.setSymbolAnalyzer(sa);
        assertEquals("Wrong AbstractSymbolAnalyzer returned", sa,
                     lc.getSymbolAnalyzer());
    }

    public void testMethod_setMatrixManager__null() {
        try {
            SpaceGroupSymbolBL.LaueClass lc =
                    new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0],
                                                     "P");
            lc.setMatrixManager(null);
            fail("Expected an AssertionError");
        } catch (Exception e) {
            fail("Wrong type of throwable: " + e
                 + "; expected an AssertionError");
        } catch (AssertionError ae) {
            // the expected case
        }
    }

    public void testAccessors_MatrixManager() {
        SpaceGroupSymbolBL.LaueClass lc =
                new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], "P");

        /*
         * A MatrixManager rather certain to be unequal to the LaueClass'
         * default one:
         */
        SpaceGroupSymbolBL.MatrixManager mm =
                new SpaceGroupSymbolBL.MatrixManager() { /* empty */ };

        lc.setMatrixManager(mm);
        assertEquals("Wrong MatrixManager returned", mm,
                     lc.getMatrixManager());
    }

    public void testMethod_requiresFullFirstOperator() {
        SpaceGroupSymbolBL.LaueClass lc;

        /*
         * No symmetry direction
         */
        lc = new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        /*
         * One symmetry sirection
         */
        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {0}, new int[] {1}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {1}, new int[] {2}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {5}, new int[] {3}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2}, new int[] {5}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        /*
         * Two symmetry directions
         */
        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {0, 1}, new int[] {1, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 4}, new int[] {3, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {4, 1}, new int[] {4, 3}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {3, 2}, new int[] {2, 6}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {4, 3}, new int[] {5, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 1}, new int[] {6, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        /*
         * Three symmetry directions
         */
        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {4, 2, 2}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {4, 0, 3}, new int[] {5, 2, 2}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 1, 4}, new int[] {3, 2, 2}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {0, 3, 5}, new int[] {6, 2, 4}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {1, 2, 3}, new int[] {3, 1, 1}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {1, 2, 3}, new int[] {3, 1, 6}, "P");
        assertTrue("LaueClass fails to require a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {6, 3, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {2, 2, 2}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {2, 2, 1}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {1, 2, 1}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {2, 1, 1}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());

        lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2, 0, 1}, new int[] {5, 4, 1}, "P");
        assertFalse("LaueClass wrongly expects a full first operator",
                    lc.requiresFullFirstOperator());
    }

    public void testMethod_generateGroup__null() {
        SpaceGroupSymbolBL.LaueClass lc = 
                new SpaceGroupSymbolBL.LaueClass(new int[0], new int[0], "P");

        try {
            lc.generateGroup(null);
            fail("Expected a NullPointerException");
        } catch (Throwable t) {
            assertTrue("Wrong type of throwable: " + t
                       + "; expected a NullPointerException",
                       t instanceof NullPointerException);
        }
    }

    public void testMethod_generateGroup__centering() throws Exception {
        String allCentering = "PABCIF";
        String centering = "PACF";
        SpaceGroupSymbolBL.LaueClass lc = 
                new SpaceGroupSymbolBL.LaueClass(new int[] {2}, new int[] {2},
                        centering);

        for (int i = 0; i < allCentering.length(); i++) {
            char c = allCentering.charAt(i);
            SpaceGroupSymbolBL.SpaceGroupSymbol sym =
                    SpaceGroupSymbolBL.digestSymbol(c + " 2");

            /*
             * There should be an InvalidDataException if and only if char c is
             * not in String centering
             */
            try {
                lc.generateGroup(sym);
                assertTrue("Expected an InvalidDataException",
                           centering.indexOf(c) >= 0);
            } catch (Throwable t) {
                assertTrue("Wrong type of throwable: " + t
                       + "; expected an InvalidDataException",
                       t instanceof InvalidDataException);
                assertTrue("No exception expected", centering.indexOf(c) < 0);
            }
        }
    }

    public void testMethod_generateGroup__operation() throws Exception {
        SpaceGroupSymbolBL.LaueClass lc = new SpaceGroupSymbolBL.LaueClass(
                new int[] {2}, new int[] {2}, "PABCIF");
        final boolean[] saCallFlag = new boolean[1];
        final boolean[] mmCallFlag = new boolean[1];
        final SpaceGroupSymbolBL.SpaceGroup[] lastGroup =
                new SpaceGroupSymbolBL.SpaceGroup[1];
        final SpaceGroupSymbolBL.SpaceGroupSymbol[] testSymbol =
                new SpaceGroupSymbolBL.SpaceGroupSymbol[1];
        SpaceGroupSymbolBL.SpaceGroup sg;

        lc.setSymbolAnalyzer(
            new SpaceGroupSymbolBL.BasicSymbolAnalyzer(lc) {
                @Override
                List<SymmetryMatrix> getGenerators(
                        SpaceGroupSymbolBL.SpaceGroupSymbol sym) {
                    saCallFlag[0] = true;
                    assertEquals("Wrong symbol analyzed", testSymbol[0], sym);
                    return new ArrayList<SymmetryMatrix>();
                }
            }
        );

        lc.setMatrixManager(
            new SpaceGroupSymbolBL.MatrixManager() {
                @Override
                void generateCenteringOps(SpaceGroupSymbolBL.SpaceGroup sg) {
                    mmCallFlag[0] = true;
                    assertEquals("Generating ops for group with wrong symbol",
                                 testSymbol[0], sg.getSymbol());
                    lastGroup[0] = sg;
                }
            }
        );

        saCallFlag[0] = false;
        mmCallFlag[0] = false;
        lastGroup[0] = null;
        testSymbol[0] = SpaceGroupSymbolBL.digestSymbol("P 2");
        sg = lc.generateGroup(testSymbol[0]);
        assertTrue("Symbol analyzer not used", saCallFlag[0]);
        assertTrue("Matrix manager not used", mmCallFlag[0]);
        assertEquals("Operations generated for different group or not at all",
                     lastGroup[0], sg);
    }

}

