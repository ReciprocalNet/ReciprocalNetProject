/*
 */

package org.recipnet.site.shared.bl;

import junit.framework.TestCase;

public class OperatorTests extends TestCase {

    public OperatorTests(String testName) {
        super(testName);
    }

    public void testConstructor_int_boolean_int_char__order() {
        SpaceGroupSymbolBL.Operator op =
            new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertEquals("Wrong rotation order returned", 0,
                     op.getRotationOrder());
        assertEquals("Wrong order returned", 1, op.getOrder());

        for (int i = 1; i < 8; i++) {
            op = new SpaceGroupSymbolBL.Operator(i, false, 0, ' ');
            assertEquals("Wrong rotation order returned", i,
                         op.getRotationOrder());
            assertEquals("Wrong order returned", i, op.getOrder());
        }
    }

    public void testConstructor_int_boolean_int_char__rotoinversion() {
        SpaceGroupSymbolBL.Operator op =
            new SpaceGroupSymbolBL.Operator(1, false, 0, ' ');

        assertFalse("Wrong rotoinversion flag returned; expected false",
                     op.isRotoInversion());
        op = new SpaceGroupSymbolBL.Operator(1, true, 0, ' ');
        assertTrue("Wrong rotoinversion flag returned; expected true",
                     op.isRotoInversion());
    }

    public void testConstructor_int_boolean_int_char__screw() {
        for (int i = 0; i < 8; i++) {
            SpaceGroupSymbolBL.Operator op =
                    new SpaceGroupSymbolBL.Operator(7, false, i, ' ');

            assertEquals("Wrong screw translation returned", i,
                         op.getScrewTranslation());
        }
    }

    public void testConstructor_int_boolean_int_char__mirror() {
        char[] mirrors = (" mabcnd1q!/".toCharArray());

        for (int i = 0; i < mirrors.length; i++) {
            SpaceGroupSymbolBL.Operator op =
                    new SpaceGroupSymbolBL.Operator(0, false, 0, mirrors[i]);

            assertEquals("Wrong mirror char returned", mirrors[i],
                         op.getMirrorComponent());
            assertTrue("Wrong value of hasMirrorComponent()",
                       op.hasMirrorComponent() == (mirrors[i] != ' '));
        }
    }

    public void testConstructor_int_boolean_int_char__direction() {
        SpaceGroupSymbolBL.Operator op =
                new SpaceGroupSymbolBL.Operator(4, false, 0, 'm');

        assertEquals("Wrong direction assigned",
                     SpaceGroupSymbolBL.DIRECTION_NONE, op.getDirection());
    }

    public void testConstructor_int_boolean_int_char_int__order() {
        SpaceGroupSymbolBL.Operator op =
            new SpaceGroupSymbolBL.Operator(0, false, 0, ' ',
                    SpaceGroupSymbolBL.DIRECTION_NONE);

        assertEquals("Wrong rotation order returned", 0,
                     op.getRotationOrder());
        assertEquals("Wrong order returned", 1, op.getOrder());

        for (int i = 1; i < 8; i++) {
            op = new SpaceGroupSymbolBL.Operator(i, false, 0, ' ',
                    SpaceGroupSymbolBL.DIRECTION_NONE);
            assertEquals("Wrong rotation order returned", i,
                         op.getRotationOrder());
            assertEquals("Wrong order returned", i, op.getOrder());
        }
    }

    public void testConstructor_int_boolean_int_char_int__rotoinversion() {
        SpaceGroupSymbolBL.Operator op =
            new SpaceGroupSymbolBL.Operator(1, false, 0, ' ',
                    SpaceGroupSymbolBL.DIRECTION_NONE);

        assertFalse("Wrong rotoinversion flag returned; expected false",
                     op.isRotoInversion());
        op = new SpaceGroupSymbolBL.Operator(1, true, 0, ' ',
                    SpaceGroupSymbolBL.DIRECTION_NONE);
        assertTrue("Wrong rotoinversion flag returned; expected true",
                     op.isRotoInversion());
    }

    public void testConstructor_int_boolean_int_char_int__screw() {
        for (int i = 0; i < 8; i++) {
            SpaceGroupSymbolBL.Operator op =
                    new SpaceGroupSymbolBL.Operator(7, false, i, ' ',
                    SpaceGroupSymbolBL.DIRECTION_NONE);

            assertEquals("Wrong screw translation returned", i,
                         op.getScrewTranslation());
        }
    }

    public void testConstructor_int_boolean_int_char_int__mirror() {
        char[] mirrors = (" mabcnd1q!/".toCharArray());

        for (int i = 0; i < mirrors.length; i++) {
            SpaceGroupSymbolBL.Operator op =
                    new SpaceGroupSymbolBL.Operator(0, false, 0, mirrors[i],
                    SpaceGroupSymbolBL.DIRECTION_NONE);

            assertEquals("Wrong mirror char returned", mirrors[i],
                         op.getMirrorComponent());
            assertTrue("Wrong value of hasMirrorComponent()",
                       op.hasMirrorComponent() == (mirrors[i] != ' '));
        }
    }

    public void testConstructor_int_boolean_int_char_int__direction() {
        for (int i = 0; i < 8; i++) {
            SpaceGroupSymbolBL.Operator op =
                    new SpaceGroupSymbolBL.Operator(0, false, 0, ' ', i);

            assertEquals("Wrong direction translation returned", i,
                         op.getDirection());
        }
    }

    public void testMethod_setMirrorComponent() {
        SpaceGroupSymbolBL.Operator op =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertEquals("Wrong initial mirror component", ' ',
                     op.getMirrorComponent());
        op.setMirrorComponent('m');
        assertEquals("Mirror component not correctly set", 'm',
                     op.getMirrorComponent());
        op.setMirrorComponent('z');
        assertEquals("Mirror component not correctly set", 'z',
                     op.getMirrorComponent());
        op.setMirrorComponent('\u3301');
        assertEquals("Mirror component not correctly set", '\u3301',
                     op.getMirrorComponent());
    }

    public void testMethod_hasMirrorComponent() {
        SpaceGroupSymbolBL.Operator op =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertFalse("Operator incorrectly reports a mirror component",
                     op.hasMirrorComponent());
        op.setMirrorComponent('m');
        assertTrue("Operator incorrectly reports no mirror component",
                     op.hasMirrorComponent());
        op.setMirrorComponent('z');
        assertTrue("Operator incorrectly reports no mirror component",
                     op.hasMirrorComponent());
        op.setMirrorComponent('\u3301');
        assertTrue("Operator incorrectly reports no mirror component",
                     op.hasMirrorComponent());
    }

    public void testMethod_equals__null() {
        SpaceGroupSymbolBL.Operator op =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertFalse("Operator is equal to null", op.equals(null));
    }

    public void testMethod_equals__Object() {
        SpaceGroupSymbolBL.Operator op =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertFalse("Operator is equal to a generic Object",
                    op.equals(new Object()));
    }

    public void testMethod_equals__Operator() {
        SpaceGroupSymbolBL.Operator op1 =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');
        SpaceGroupSymbolBL.Operator op2 =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertEquals("Expected Operators to be equal", op1, op2);
        op1 = new SpaceGroupSymbolBL.Operator(1, false, 0, ' ');
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
        op2 = new SpaceGroupSymbolBL.Operator(1, true, 0, ' ');
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 0, ' ');
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
        op1 = new SpaceGroupSymbolBL.Operator(2, false, 1, ' ');
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c');
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
        op1 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c', 1);
        assertFalse("Expected Operators to be inequal", op1.equals(op2));
    }

    public void testMethod_hashCode() {
        SpaceGroupSymbolBL.Operator op1 =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');
        SpaceGroupSymbolBL.Operator op2 =
                new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');

        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(1, false, 0, ' ');
        op2 = new SpaceGroupSymbolBL.Operator(1, false, 0, ' ');
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(1, true, 0, ' ');
        op2 = new SpaceGroupSymbolBL.Operator(1, true, 0, ' ');
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(2, false, 0, ' ');
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 0, ' ');
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(2, false, 1, ' ');
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 1, ' ');
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c');
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c');
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());

        op1 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c', 1);
        op2 = new SpaceGroupSymbolBL.Operator(2, false, 1, 'c', 1);
        assertEquals("Equal Operators have distinct hash codes",
                     op1.hashCode(), op2.hashCode());
    }

    public void testMethod_toString() {
        SpaceGroupSymbolBL.Operator op;

        op = new SpaceGroupSymbolBL.Operator(0, false, 0, ' ');
        assertEquals("Wrong string representation", "", op.toString());
        op = new SpaceGroupSymbolBL.Operator(1, false, 0, ' ');
        assertEquals("Wrong string representation", "1", op.toString());
        op = new SpaceGroupSymbolBL.Operator(5, false, 0, ' ');
        assertEquals("Wrong string representation", "5", op.toString());
        op = new SpaceGroupSymbolBL.Operator(2, true, 0, ' ');
        assertEquals("Wrong string representation", "-2", op.toString());
        op = new SpaceGroupSymbolBL.Operator(4, true, 0, ' ');
        assertEquals("Wrong string representation", "-4", op.toString());
        op = new SpaceGroupSymbolBL.Operator(3, false, 1, ' ');
        assertEquals("Wrong string representation", "31", op.toString());
        op = new SpaceGroupSymbolBL.Operator(5, false, 7, ' ');
        assertEquals("Wrong string representation", "57", op.toString());
        op = new SpaceGroupSymbolBL.Operator(2, true, 1, ' ');
        assertEquals("Wrong string representation", "-21", op.toString());
        op = new SpaceGroupSymbolBL.Operator(6, true, 5, ' ');
        assertEquals("Wrong string representation", "-65", op.toString());
        op = new SpaceGroupSymbolBL.Operator(1, false, 0, 'm');
        assertEquals("Wrong string representation", "1/m", op.toString());
        op = new SpaceGroupSymbolBL.Operator(4, false, 0, 'q');
        assertEquals("Wrong string representation", "4/q", op.toString());
        op = new SpaceGroupSymbolBL.Operator(3, false, 4, 'd');
        assertEquals("Wrong string representation", "34/d", op.toString());
        op = new SpaceGroupSymbolBL.Operator(5, false, 3, 'n');
        assertEquals("Wrong string representation", "53/n", op.toString());
        op = new SpaceGroupSymbolBL.Operator(2, true, 1, 'b');
        assertEquals("Wrong string representation", "-21/b", op.toString());
        op = new SpaceGroupSymbolBL.Operator(4, true, 4, 'c');
        assertEquals("Wrong string representation", "-44/c", op.toString());
        op = new SpaceGroupSymbolBL.Operator(7, true, 0, ':');
        assertEquals("Wrong string representation", "-7/:", op.toString());
        op = new SpaceGroupSymbolBL.Operator(2, true, 0, 'v');
        assertEquals("Wrong string representation", "-2/v", op.toString());
    }

}

