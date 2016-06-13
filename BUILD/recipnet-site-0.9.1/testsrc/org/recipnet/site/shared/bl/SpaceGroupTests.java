/*
 * IUMSC Reciprocal Net Project
 *
 * SpaceGroupTests.java
 */

package org.recipnet.site.shared.bl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.recipnet.common.SymmetryMatrix;

public class SpaceGroupTests extends TestCase {

    private SpaceGroupSymbolBL.SpaceGroupSymbol symbol;

    public SpaceGroupTests(String testName) {
        super(testName);
        getClass().getClassLoader().setPackageAssertionStatus(
                "org.recipnet", true);
    }

    @Override
    public void setUp() {
        symbol = new SpaceGroupSymbolBL.SpaceGroupSymbol('C',
                new SpaceGroupSymbolBL.OperatorList(
                    new SpaceGroupSymbolBL.Operator[] {
                        new SpaceGroupSymbolBL.Operator(2, false, 0, ' ')}));
    }

    public void testConstructor_null_LaueClass() {
        try {
            new SpaceGroupSymbolBL.SpaceGroup(null,
                    SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
            fail("Expected an AssertionError");
        } catch (Throwable t) {
            assertTrue("Wrong type of throwable: " + t
                       + "; expected an AssertionError",
                       t instanceof AssertionError);
        }
    }

    public void testConstructor_SpaceGroupSymbol_null() {
        try {
            new SpaceGroupSymbolBL.SpaceGroup(symbol, null);
            fail("Expected an AssertionError");
        } catch (Throwable t) {
            assertTrue("Wrong type of throwable: " + t
                       + "; expected an AssertionError",
                       t instanceof AssertionError);
        }
    }

    public void testConstructor_SpaceGroupSymbol_LaueClass() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group =
                new SpaceGroupSymbolBL.SpaceGroup(symbol,
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        Set<SymmetryMatrix> matrixSet = group.getMatrixSet();
        Iterator<SymmetryMatrix> it = matrixSet.iterator();

        assertEquals("Wrong symbol returned", symbol, group.getSymbol());
        assertEquals("Wrong LaueClass returned",
                     SpaceGroupSymbolBL.LaueClass.MONOCLINIC,
                     group.getLaueClass());

        assertNotNull("The matrix set is null", matrixSet);
        assertEquals("The matrix set has the wrong number of elements", 1,
                     matrixSet.size());
        assertEquals("The matrix set does not contain the identity matrix",
                     SymmetryMatrix.IDENTITY, it.next()); 
    }

    public void testMethod_applyMatrix_null() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group =
                new SpaceGroupSymbolBL.SpaceGroup(symbol,
                        SpaceGroupSymbolBL.LaueClass.MONOCLINIC);

        try {
            group.applyMatrix(null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; got " + e,
                       e instanceof NullPointerException);
        }        
    }

    public void testMethods_applyMatrix_getMatrixSet_order2() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group = new SpaceGroupSymbolBL.SpaceGroup(
                symbol, SpaceGroupSymbolBL.LaueClass.MONOCLINIC);
        SymmetryMatrix twofold =
                new SymmetryMatrix(
                        new int[][] {{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        SymmetryMatrix mirror =
                new SymmetryMatrix(
                        new int[][] {{1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        SymmetryMatrix cCenter =
                new SymmetryMatrix(new int[] {6, 6, 0});
        Set<SymmetryMatrix> matrixSet;

        /*
         * If we apply a matrix it should show up in the matrix set because that
         * set must always already contain the identity matrix
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(twofold));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 2, matrixSet.size());
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));

        /*
         * If we apply a matrix that is already there then no change should be
         * observed
         */
        assertFalse("applyMatrix returned true", group.applyMatrix(twofold));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 2, matrixSet.size());
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));

        /*
         * Not only should each new matrix appear, but ALL products of that
         * matrix with those already present should appear
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(mirror));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 4, matrixSet.size());
        assertTrue("Matrix set does not contain the mirror",
                   matrixSet.contains(mirror));
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
        assertTrue("Matrix set does not contain an inversion through 0 0 0 ",
                   matrixSet.contains(SymmetryMatrix.INVERSION));

        /*
         * Pure translations should work the same as any other operation
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(cCenter));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 8, matrixSet.size());
        assertTrue("Matrix set does not contain the centering translation",
                   matrixSet.contains(cCenter));
        assertTrue("Matrix set does not contain the mirror",
                   matrixSet.contains(mirror));
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
        assertTrue("Matrix set does not contain an inversion through 0 0 0 ",
                   matrixSet.contains(SymmetryMatrix.INVERSION));

    }

    public void testMethods_applyMatrix_getMatrixSet_order3() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group = new SpaceGroupSymbolBL.SpaceGroup(
                symbol, SpaceGroupSymbolBL.LaueClass.RHOMBOHEDRAL_LOW);
        SymmetryMatrix threePlus =
                new SymmetryMatrix(
                        new int[][] {{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        SymmetryMatrix threeMinus =
                threePlus.times(threePlus, true);
        Set<SymmetryMatrix> matrixSet;

        // Verify our assumptions:
        assertFalse("Positive and negative threefolds are equal",
                    threePlus.equals(threeMinus));

        /*
         * If we apply a matrix it should show up in the matrix set because that
         * set must always already contain the identity matrix; if it represents
         * an operation of order greater than two then all distinct powers of
         * the matrix are applied
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(threePlus));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 3, matrixSet.size());
        assertTrue("Matrix set does not contain the positive threefold",
                   matrixSet.contains(threePlus));
        assertTrue("Matrix set does not contain the negative threefold",
                   matrixSet.contains(threeMinus));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
    }

    public void testMethods_applyMatrix_getMatrixSet_order4() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group = new SpaceGroupSymbolBL.SpaceGroup(
                symbol, SpaceGroupSymbolBL.LaueClass.TETRAGONAL_LOW);
        SymmetryMatrix fourPlus =
                new SymmetryMatrix(
                        new int[][] {{0, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        SymmetryMatrix twofold
                = fourPlus.times(fourPlus, true);
        SymmetryMatrix fourMinus
                = fourPlus.times(twofold, true);
        Set<SymmetryMatrix> matrixSet;

        // verify our assumptions
        matrixSet = new HashSet<SymmetryMatrix>();
        matrixSet.add(fourPlus);
        matrixSet.add(twofold);
        matrixSet.add(fourMinus);
        matrixSet.add(SymmetryMatrix.IDENTITY);

        assertEquals("Test matrices are not distinct", 4, matrixSet.size());

        /*
         * If we apply a matrix it should show up in the matrix set because that
         * set must always already contain the identity matrix; if it represents
         * an operation of order greater than two then all distinct powers of
         * the matrix are applied
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(fourPlus));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 4, matrixSet.size());
        assertTrue("Matrix set does not contain the positive fourfold",
                   matrixSet.contains(fourPlus));
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the negative fourfold",
                   matrixSet.contains(fourMinus));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
    }

    public void testMethods_applyMatrix_getMatrixSet_order6() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group = new SpaceGroupSymbolBL.SpaceGroup(
                symbol, SpaceGroupSymbolBL.LaueClass.HEXAGONAL_LOW);
        SymmetryMatrix sixPlus =
                new SymmetryMatrix(
                        new int[][] {{1, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        SymmetryMatrix threePlus
                = sixPlus.times(sixPlus, true);
        SymmetryMatrix twofold
                = sixPlus.times(threePlus, true);
        SymmetryMatrix threeMinus
                = sixPlus.times(twofold, true);
        SymmetryMatrix sixMinus
                = sixPlus.times(threeMinus, true);
        Set<SymmetryMatrix> matrixSet;

        // verify our assumptions
        matrixSet = new HashSet<SymmetryMatrix>();
        matrixSet.add(sixPlus);
        matrixSet.add(threePlus);
        matrixSet.add(twofold);
        matrixSet.add(threeMinus);
        matrixSet.add(sixMinus);
        matrixSet.add(SymmetryMatrix.IDENTITY);

        assertEquals("Test matrices are not distinct", 6, matrixSet.size());

        /*
         * If we apply a matrix it should show up in the matrix set because that
         * set must always already contain the identity matrix; if it represents
         * an operation of order greater than two then all distinct powers of
         * the matrix are applied
         */
        assertTrue("applyMatrix returned false", group.applyMatrix(sixPlus));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 6, matrixSet.size());
        assertTrue("Matrix set does not contain the positive sixfold",
                   matrixSet.contains(sixPlus));
        assertTrue("Matrix set does not contain the positive threefold",
                   matrixSet.contains(threePlus));
        assertTrue("Matrix set does not contain the twofold",
                   matrixSet.contains(twofold));
        assertTrue("Matrix set does not contain the negative threefold",
                   matrixSet.contains(threeMinus));
        assertTrue("Matrix set does not contain the negative sixfold",
                   matrixSet.contains(sixMinus));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
    }

    public void testMethods_applyMatrix_getMatrixSet_threeBar() throws Exception {
        SpaceGroupSymbolBL.SpaceGroup group = new SpaceGroupSymbolBL.SpaceGroup(
                symbol, SpaceGroupSymbolBL.LaueClass.RHOMBOHEDRAL_LOW);
        SymmetryMatrix threebarPlus =
                new SymmetryMatrix(
                        new int[][] {{0, 0, -1}, {-1, 0, 0}, {0, -1, 0}});
        SymmetryMatrix threeMinus =
                threebarPlus.times(threebarPlus, true);
        SymmetryMatrix inversion =
                threebarPlus.times(threeMinus, true);
        SymmetryMatrix threePlus =
                threebarPlus.times(inversion, true);
        SymmetryMatrix threebarMinus =
                threebarPlus.times(threePlus, true);
        Set<SymmetryMatrix> matrixSet;

        // verify our assumptions
        matrixSet = new HashSet<SymmetryMatrix>();
        assertEquals("Wrong type for matrix -3(+)",
                SymmetryMatrix.Type.THREE_BAR, threebarPlus.getType());
        matrixSet.add(threebarPlus);
        assertEquals("Wrong type for matrix 3(+)",
                SymmetryMatrix.Type.THREEFOLD, threePlus.getType());
        matrixSet.add(threePlus);
        assertEquals("Wrong matrix -1",
                SymmetryMatrix.INVERSION, inversion);
        matrixSet.add(inversion);
        assertEquals("Wrong type for matrix 3(-)",
                SymmetryMatrix.Type.THREEFOLD, threeMinus.getType());
        matrixSet.add(threeMinus);
        assertEquals("Wrong type for matrix -3(-)",
                SymmetryMatrix.Type.THREE_BAR, threebarMinus.getType());
        matrixSet.add(threebarMinus);
        matrixSet.add(SymmetryMatrix.IDENTITY);

        assertEquals("Test matrices are not distinct", 6, matrixSet.size());

        /*
         * If we apply a matrix it should show up in the matrix set because that
         * set must always already contain the identity matrix; if it represents
         * an operation of order greater than two then all distinct powers of
         * the matrix are applied
         */
        assertTrue("applyMatrix returned false",
                   group.applyMatrix(threebarPlus));
        matrixSet = group.getMatrixSet();
        assertEquals("Wrong number of matrices", 6, matrixSet.size());
        assertTrue("Matrix set does not contain the positive threebar",
                   matrixSet.contains(threebarPlus));
        assertTrue("Matrix set does not contain the positive threefold",
                   matrixSet.contains(threePlus));
        assertTrue("Matrix set does not contain the inversion",
                   matrixSet.contains(inversion));
        assertTrue("Matrix set does not contain the negative threefold",
                   matrixSet.contains(threeMinus));
        assertTrue("Matrix set does not contain the negative threebar",
                   matrixSet.contains(threebarMinus));
        assertTrue("Matrix set does not contain the identity",
                   matrixSet.contains(SymmetryMatrix.IDENTITY));
    }
}

