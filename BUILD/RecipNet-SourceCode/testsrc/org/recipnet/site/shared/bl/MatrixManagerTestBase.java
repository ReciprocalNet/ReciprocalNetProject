/*
 * IUMSC Reciprocal Net Project
 *
 * MatrixManagerTestBase.java
 */

package org.recipnet.site.shared.bl;

import junit.framework.TestCase;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.site.InvalidDataException;

abstract public class MatrixManagerTestBase extends TestCase {

    public MatrixManagerTestBase(String testName) {
        super(testName);
        getClass().getClassLoader().setPackageAssertionStatus(
                "org.recipnet", true);
    }

    /**
     * Returns a SpaceGroupSymbolBL.MatrixManager instance to test; must be
     * overridden by test subclasses to test different
     * SpaceGroupSymbolBL.MatrixManager subclasses
     */
    abstract protected SpaceGroupSymbolBL.MatrixManager getTestSubject();

    abstract protected int getMaximumOrder();
    abstract protected char[] getValidGlides();

    public void testMethod_generateCenteringOps__nullGroup() {
        try {
            getTestSubject().generateCenteringOps(null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; caught "
                       + e.getClass(),
                       e instanceof NullPointerException);
        }
    }

    public void testMethod_generateCenteringOps__badCenter() {
        SpaceGroupSymbolBL.OperatorList ol =
                new SpaceGroupSymbolBL.OperatorList(
                        new SpaceGroupSymbolBL.Operator[] {
                            new SpaceGroupSymbolBL.Operator(1, false, 0, ' ')});
        SpaceGroupSymbolBL.SpaceGroup sg = new SpaceGroupSymbolBL.SpaceGroup(
                new SpaceGroupSymbolBL.SpaceGroupSymbol('X', ol),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);

        try {
            getTestSubject().generateCenteringOps(sg);
            fail("Expected an AssertionError");
        } catch (Throwable t) {
            assertTrue("Expected an AssertionError; caught " + t.getClass(),
                       t instanceof AssertionError);
        }
    }

    private void assertGroupOK(SpaceGroupSymbolBL.SpaceGroup sg, int nops) {
        assertEquals("Wrong number of operations", nops,
                     sg.getMatrixSet().size());
        try {
            sg.testValidity();
        } catch (InvalidDataException ide) {
            fail("Group not valid: " + ide.getMessage());
        }
    }

    public void testMethod_generateCenteringOps__good()
            throws InvalidDataException {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        SpaceGroupSymbolBL.SpaceGroup sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("P 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);

        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 1);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("A 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 2);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("B 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 2);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("C 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 2);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("I 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 2);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("F 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 4);

        sg = new SpaceGroupSymbolBL.SpaceGroup(
                SpaceGroupSymbolBL.digestSymbol("R 1"),
                SpaceGroupSymbolBL.LaueClass.TRICLINIC);
        mm.generateCenteringOps(sg);
        assertGroupOK(sg, 3);
    }

    public void testMethod_createRotationMatrix__badRotoinversion() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        try {
            mm.createRotationMatrix(2, 0, 0, true);
            fail("Expected an IllegalArgumentException");
        } catch (Exception e) {
            assertTrue("Expected a IllegalArgumentException; caught "
                       + e.getClass(),
                       e instanceof IllegalArgumentException);
        }
        try {
            mm.createRotationMatrix(getMaximumOrder(), 0, 2, true);
            fail("Expected an IllegalArgumentException");
        } catch (Exception e) {
            assertTrue("Expected a IllegalArgumentException; caught "
                       + e.getClass(),
                       e instanceof IllegalArgumentException);
        }
    }

    public void testMethod_createReflectionMatrix__invalidMirror() {
        try {
            getTestSubject().createReflectionMatrix(0, 'q');
            fail("Expected an IllegalArgumentException");
        } catch (Exception e) {
            assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                       + e.getClass(),
                       e instanceof IllegalArgumentException);
        }
    }

    public void testMethod_createReflectionMatrix__perpendicularGlide() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = 0; i < 3; i++) {
            try {
                mm.createReflectionMatrix(i, (char) ('a' + i));
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }

    public void testMethod_determineOperator__null() {
        try {
            getTestSubject().determineOperator(null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Wrong exception; expected NullPointerException, caught "
                       + e.getClass(),
                       e instanceof NullPointerException);
        }
    }

    public void testMethod_determineOperator__badMatrix() {
        try {
            getTestSubject().determineOperator(
                    new SymmetryMatrix(
                            new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
            fail("Expected an IllegalArgumentException");
        } catch (Exception e) {
            assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                       + e.getClass(),
                       e instanceof IllegalArgumentException);
        }
    }

    public void testMethod_determineOperator__onefold() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        SymmetryMatrix matrix;
        SpaceGroupSymbolBL.Operator op;

        /* determineOperator does not support the identity operator */

        matrix = mm.createRotationMatrix(1, -1, 0, true);
        op = mm.determineOperator(matrix);
        assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined",
                     new SpaceGroupSymbolBL.Operator(1, true, 0, ' ', -1),
                     op);
        assertEquals("Operator not assigned a correct matrix",
                     matrix, op.getMatrix());
    }
     
    public void testMethod_determineOperator__twofold() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        SymmetryMatrix matrix;
        SpaceGroupSymbolBL.Operator op;

        for (int direction = 0; direction < 5; direction++) {
            int screwLimit = (direction < 3) ? 2 : 1;

            if (direction == SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                continue;
            }
            for (int screw = 0; screw < screwLimit; screw++) {
                matrix = mm.createRotationMatrix(2, direction, screw, false);
                op = mm.determineOperator(matrix);
                assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined"
                             + " for direction " + direction + ";",
                             new SpaceGroupSymbolBL.Operator(2, false, screw, ' ', direction),
                             op);
                assertEquals("Operator not assigned a correct matrix",
                             matrix, op.getMatrix());
            }
        }
    }

    public void testMethod_determineOperator__reflection() {
        char[] reflections = getValidGlides();
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        SymmetryMatrix matrix;
        SpaceGroupSymbolBL.Operator op;

        for (int direction = 0; direction < 5; direction++) {
            if (direction == SpaceGroupSymbolBL.DIRECTION_BODY_DIAG) {
                continue;
            }
            for (int rindex = 0; rindex < reflections.length; rindex++) {

                // Screen out invalid direction / glide combinations:
                if ((reflections[rindex] == (char) ('a' + direction))
                    || ((direction > 2)
                        && ((reflections[rindex] == 'a')
                            || (reflections[rindex] == 'b')))) {
                    continue;
                } 

                matrix = mm.createReflectionMatrix(direction,
                                                   reflections[rindex]);
                op = mm.determineOperator(matrix);
                assertEquals("Incorrect SpaceGroupSymbolBL.Operator determined"
                             + " for direction " + direction,
                             new SpaceGroupSymbolBL.Operator(0, false, 0,
                                     reflections[rindex], direction),
                             op);
                assertEquals("Operator not assigned a correct matrix",
                             matrix, op.getMatrix());
            }
        }
    }

    public void testMethod_determineTwofoldDirection__nullMatrix() {
        try {
            getTestSubject().determineTwofoldDirection(null, true);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; caught "
                       + e.getClass(),
                       e instanceof NullPointerException);
        }
        try {
            getTestSubject().determineTwofoldDirection(null, false);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; caught "
                       + e.getClass(),
                       e instanceof NullPointerException);
        }
    }

    public void testMethod_determineMirrorType__nullVector() {
        try {
            getTestSubject().determineMirrorType(0, null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; caught "
                       + e.getClass(),
                       e instanceof NullPointerException);
        }
    }

    public void testMethod_determineMirrorType__m() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int dir = 0; dir < 6; dir++) {
            int[] vector = mm.createTranslationElements(dir, 'm');

            assertEquals("Wrong mirror type determined", 'm',
                         mm.determineMirrorType(dir, vector));
        }
    }

    public void testMethod_createRotationElements__badDirection() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = -2; i < 8; i++) {
            if ((i >= 0) && (i <= 5)) {
                continue;
            }
            try {
                mm.createRotationElements(2, i);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }

    public void testMethod_createReflectionElements__badDirection() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = -2; i < 8; i++) {
            if ((i >= 0) && (i <= 5)
                    && (i != SpaceGroupSymbolBL.DIRECTION_BODY_DIAG)) {
                continue;
            }
            try {
                mm.createReflectionElements(i);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }

    public void testMethod_createTranslationElements__badDirection() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();

        for (int i = -2; i < 8; i++) {
            if ((i >= 0) && (i <= 5)) {
                continue;
            }
            try {
                mm.createTranslationElements(i, 'm');
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }

    public void testMethod_createTranslationElements__badType() {
        SpaceGroupSymbolBL.MatrixManager mm = getTestSubject();
        char[] badTypes = new char[]
                {'e', 'g', 'l', 'o', '/', '(', ')', '-', '0', '5', '7', 'Q', 'P'};

        for (int i = 0; i < badTypes.length; i++) {
            try {
                mm.createTranslationElements(2, badTypes[i]);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue("Wrong exception; expected IllegalArgumentException, caught "
                           + e.getClass(),
                           e instanceof IllegalArgumentException);
            }
        }
    }
}

