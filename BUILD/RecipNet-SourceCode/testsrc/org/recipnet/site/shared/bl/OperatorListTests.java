/*
 * Reciprocal Net Project
 *
 * OperatorListTests.java
 */

package org.recipnet.site.shared.bl;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.Operator;

import junit.framework.TestCase;

public class OperatorListTests extends TestCase {

    public OperatorListTests(String testName) {
        super(testName);
    }

    public void testConstructor_nullArray() {
        try {
            new SpaceGroupSymbolBL.OperatorList(
                    (SpaceGroupSymbolBL.Operator[]) null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; got " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_nullCollection() {
        try {
            new SpaceGroupSymbolBL.OperatorList((Collection<Operator>) null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Expected a NullPointerException; got " + e,
                       e instanceof NullPointerException);
        }
    }

    public void testConstructor_OperatorA() {
        SpaceGroupSymbolBL.Operator op0 =
            new SpaceGroupSymbolBL.Operator(2, false, 0, 'm');
        SpaceGroupSymbolBL.Operator op1 =
            new SpaceGroupSymbolBL.Operator(0, false, 0, 'n');
        SpaceGroupSymbolBL.OperatorList ol;

        ol = new SpaceGroupSymbolBL.OperatorList(
                new SpaceGroupSymbolBL.Operator[] {op0});
        assertEquals("OperatorList has the wrong size", 1, ol.size());
        assertEquals("OperatorList has wrong 0th element", op0, ol.get(0));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(0), ol.getOperator(0));

        ol = new SpaceGroupSymbolBL.OperatorList(
                new SpaceGroupSymbolBL.Operator[] {op0, op1, op1});
        assertEquals("OperatorList has the wrong size", 3, ol.size());
        assertEquals("OperatorList has wrong 0th element", op0, ol.get(0));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(0), ol.getOperator(0));
        assertEquals("OperatorList has wrong 1st element", op1, ol.get(1));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(1), ol.getOperator(1));
        assertEquals("OperatorList has wrong 2nd element", op1, ol.get(2));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(2), ol.getOperator(2));
    }

    public void testConstructor_Collection() {
        SpaceGroupSymbolBL.Operator op0 =
            new SpaceGroupSymbolBL.Operator(2, false, 0, 'm');
        SpaceGroupSymbolBL.Operator op1 =
            new SpaceGroupSymbolBL.Operator(0, false, 0, 'n');
        SpaceGroupSymbolBL.OperatorList ol;

        ol = new SpaceGroupSymbolBL.OperatorList(
                Collections.unmodifiableCollection(Arrays.asList(
                        new SpaceGroupSymbolBL.Operator[] {op0})));
        assertEquals("OperatorList has the wrong size", 1, ol.size());
        assertEquals("OperatorList has wrong 0th element", op0, ol.get(0));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(0), ol.getOperator(0));

        ol = new SpaceGroupSymbolBL.OperatorList(
                Collections.unmodifiableCollection(Arrays.asList(
                        new SpaceGroupSymbolBL.Operator[] {op0, op1, op1})));
        assertEquals("OperatorList has the wrong size", 3, ol.size());
        assertEquals("OperatorList has wrong 0th element", op0, ol.get(0));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(0), ol.getOperator(0));
        assertEquals("OperatorList has wrong 1st element", op1, ol.get(1));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(1), ol.getOperator(1));
        assertEquals("OperatorList has wrong 2nd element", op1, ol.get(2));
        assertEquals("OperatorLists get methods disagree",
                     ol.get(2), ol.getOperator(2));
    }


}

