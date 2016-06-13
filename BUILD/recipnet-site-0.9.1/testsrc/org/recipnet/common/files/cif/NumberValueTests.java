/*
 * Reciprocal Net Project
 *
 * NumberValueTests.java
 * 
 * 06-Apr-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import junit.framework.TestCase;


/**
 * A JUnit TestCase that exercises the behavior of the NumberValue class.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class NumberValueTests extends TestCase {
    
    /**
     * Expected accuracy of double-precision comparisons
     */
    private final static double DELTA = 1e-10;

    /*
     * These tests assume that NumberValue applies a rule of 19 with respect to
     * standard uncertainties.  
     */
    
    /**
     * Initializes a new {@code NumberValueTests} to run the named test
     * 
     * @param  testName the name of the test to run 
     */
    public NumberValueTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the (String, String, String) constructor when the
     * first argument is {@code null}; a {@code NullPointerException} is
     * expected.
     */
    public void testConstructor_nullString_String_String() {
        try {
            new NumberValue(null, "E-3", "2");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the (String, String, String) constructor when the
     * first argument is malformed; a {@code NumberFormatException} is
     * expected.
     */
    public void testConstructor_malformedString_String_String() {
        String[] badValues = new String[] {"1.533E-3", "1.533d", "NaN",
                "Infinity", "0x0000", "0.1.2", "-+1", "0.0+1", "", "  ", "-"};
        
        for (String testString : badValues) {
            try {
                new NumberValue(testString, null, null);
                fail("Expected a NumberFormatException for significand '"
                     + testString + "'");
            } catch (NumberFormatException nfe) {
                // the expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the (String, String, String) constructor when the
     * second argument is malformed; a {@code NumberFormatException} is
     * expected.
     */
    public void testConstructor_String_malformedString_String() {
        String[] badValues = new String[] {"E", "F", "D", "P", "x00", "A",
                "b", "c", "Infinity", "NaN", "-", "+", "01f", "01d",
                "01p", "0-", " ", "0 ", ""};
                                   
        for (String testString : badValues) {
            try {
                new NumberValue("0", testString, null);
                fail("Expected a NumberFormatException for exponent '"
                     + testString + "'");
            } catch (NumberFormatException nfe) {
                // the expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the (String, String, String) constructor when the
     * third argument is malformed; a {@code NumberFormatException} is
     * expected.
     */
    public void testConstructor_String_String_malformedString() {
        String[] badSUs = new String[] {"0x00", "a", "e-01", "E-01", "d",
                "D", "f", "F", "Infinity", "NaN", "-", "+", "2f", "2d",
                "1p", "2-", " ", "2 ", "", "0.1"};
                                                              
        for (String testString : badSUs) {
            try {
                new NumberValue("0", testString, null);
                fail("Expected a NumberFormatException for exponent '"
                     + testString + "'");
            } catch (NumberFormatException nfe) {
                // the expected case
            }
        }
    }

    /**
     * Tests the behavior of the (double) constructor when its argument is
     * NaN; an {@code IllegalArgumentException} is expected.
     */
    public void testConstructor_NaNdouble() {
        try {
            new NumberValue(Double.NaN);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double) constructor when its argument is
     * positive or negative infinity; an {@code IllegalArgumentException} is
     * expected.
     */
    public void testConstructor_Infdouble() {
        try {
            new NumberValue(Double.POSITIVE_INFINITY);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new NumberValue(Double.NEGATIVE_INFINITY);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double, double) constructor when its first
     * argument is NaN; an {@code IllegalArgumentException} is expected.
     */
    public void testConstructor_NaNdouble_double() {
        try {
            new NumberValue(Double.NaN, 1.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double, double) constructor when its first
     * argument is positive or negative infinity; an
     * {@code IllegalArgumentException} is expected.
     */
    public void testConstructor_Infdouble_double() {
        try {
            new NumberValue(Double.POSITIVE_INFINITY, 1.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new NumberValue(Double.NEGATIVE_INFINITY, 1.0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double, double) constructor when its
     * second argument is NaN; an {@code IllegalArgumentException} is expected.
     */
    public void testConstructor_double_NaNdouble() {
        try {
            new NumberValue(1.0, Double.NaN);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double, double) constructor when its
     * second argument is positive or negative infinity; an
     * {@code IllegalArgumentException} is expected.
     */
    public void testConstructor_double_Infdouble() {
        try {
            new NumberValue(1.0, Double.POSITIVE_INFINITY);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        try {
            new NumberValue(1.0, Double.NEGATIVE_INFINITY);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the (double, double) constructor when its
     * second argument is negative; an {@code IllegalArgumentException} is
     * expected.
     */
    public void testConstructor_double_negative() {
        try {
            new NumberValue(1.0, -0.2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }
    
    /**
     * Tests NumberValues initialized via the one-argument constructor to verify
     * that they have the expected properties when no truncation or scientific
     * notation is expected
     */
    public void testConstructor_double__noExponent() {
        assertNumberValue(new NumberValue(0.), 0.0, 0.0, "0");
        assertNumberValue(new NumberValue(1.), 1.0, 0.0, "1");
        assertNumberValue(new NumberValue(10.), 10.0, 0.0, "10");
        assertNumberValue(new NumberValue(12.3), 12.3, 0.0, "12.3");
        assertNumberValue(new NumberValue(-12.3), -12.3, 0.0, "-12.3");
        assertNumberValue(new NumberValue(1000.), 1000.0, 0.0, "1000");
        assertNumberValue(new NumberValue(0.00001), 0.00001, 0.0, "0.00001");
        assertNumberValue(new NumberValue(-0.00001), -0.00001, 0.0, "-0.00001");
        assertNumberValue(new NumberValue(1234.56789), 1234.56789, 0.0,
                          "1234.56789");
        assertNumberValue(new NumberValue(1000. - Math.ulp(1000.0)),
                          1000. - Math.ulp(1000.0), 0.0, "1000");
    }
    
    /**
     * Tests NumberValues initialized via the two-argument constructor with su
     * 0, to verify that they have the expected properties when no truncation or
     * scientific notation is expected
     */
    public void testConstructor_double_zero__noExponent() {
        assertNumberValue(new NumberValue(-0., 0.), 0.0, 0.0, "0");
        assertNumberValue(new NumberValue(1., 0.), 1.0, 0.0, "1");
        assertNumberValue(new NumberValue(-1., 0.), -1.0, 0.0, "-1");
        assertNumberValue(new NumberValue(10., 0.), 10.0, 0.0, "10");
        assertNumberValue(new NumberValue(12.3, 0.), 12.3, 0.0, "12.3");
        assertNumberValue(new NumberValue(1000., 0.), 1000.0, 0.0, "1000");
        assertNumberValue(new NumberValue(-1000., 0.), -1000.0, 0.0, "-1000");
        assertNumberValue(new NumberValue(0.00001, 0.),
                          0.00001, 0.0, "0.00001");
        assertNumberValue(new NumberValue(1234.56789, 0.), 1234.56789, 0.0,
                          "1234.56789");
        assertNumberValue(new NumberValue(1000. - Math.ulp(1000.0), 0.0),
                          1000. - Math.ulp(1000.0), 0.0, "1000");
    }
    
    /**
     * Tests NumberValues initialized via the three-argument constructor without
     * exponent or su, to verify that they have the expected properties when no
     * truncation or scientific notation is expected
     */
    public void testConstructor_String_null_null() {
        assertNumberValue(new NumberValue("0.0", null, null), 0.0, 0.0, "0.0");
        assertNumberValue(new NumberValue("1.0", null, null), 1.0, 0.0, "1.0");
        assertNumberValue(new NumberValue("10.", null, null), 10.0, 0.0, "10.");
        assertNumberValue(new NumberValue("-10.", null, null),
                          -10.0, 0.0, "-10.");
        assertNumberValue(new NumberValue("12.3", null, null),
                          12.3, 0.0, "12.3");
        assertNumberValue(new NumberValue("1000.", null, null),
                          1000.0, 0.0, "1000.");
        assertNumberValue(new NumberValue("0.00001", null, null),
                          0.00001, 0.0, "0.00001");
        assertNumberValue(new NumberValue("1234.56789", null, null),
                          1234.56789, 0.0, "1234.56789");
    }
    
    /**
     * Tests NumberValues initialized via the three-argument constructor with
     * exponent but no su, to verify that they have the expected properties when
     * no truncation or scientific notation is expected
     */
    public void testConstructor_String_String_null() {
        assertNumberValue(new NumberValue("0.", "0", null), 0.0, 0.0, "0.e0");
        assertNumberValue(new NumberValue("0.", "+10", null),
                          0.0, 0.0, "0.e+10");
        assertNumberValue(new NumberValue("0", "-1", null), 0.0, 0.0, "0e-1");
        assertNumberValue(new NumberValue("1.", "0", null), 1.0, 0.0, "1.e0");
        assertNumberValue(new NumberValue("1.", "+0", null), 1.0, 0.0, "1.e+0");
        assertNumberValue(new NumberValue("1.", "-0", null), 1.0, 0.0, "1.e-0");
        assertNumberValue(new NumberValue(".1", "+01", null),
                          1.0, 0.0, ".1e+01");
        assertNumberValue(new NumberValue("-1.", "+0", null),
                          -1.0, 0.0, "-1.e+0");
        assertNumberValue(new NumberValue("-1.", "-0", null),
                          -1.0, 0.0, "-1.e-0");
        assertNumberValue(new NumberValue("10.", "-1", null),
                          1.0, 0.0, "10.e-1");
        assertNumberValue(new NumberValue("10.0", "0", null),
                          10.0, 0.0, "10.0e0");
        assertNumberValue(new NumberValue("1.", "1", null),
                          10.0, 0.0, "1.e1");
        assertNumberValue(new NumberValue("1000.", "-2", null),
                          10.0, 0.0, "1000.e-2");
        assertNumberValue(new NumberValue("12.3", "0", null),
                          12.3, 0.0, "12.3e0");
        assertNumberValue(new NumberValue("123", "-1", null),
                          12.3, 0.0, "123e-1");
        assertNumberValue(new NumberValue("1.23", "1", null),
                          12.3, 0.0, "1.23e1");
        assertNumberValue(new NumberValue(".123", "02", null),
                          12.3, 0.0, ".123e02");
        assertNumberValue(new NumberValue(".0123", "03", null),
                          12.3, 0.0, ".0123e03");
        assertNumberValue(new NumberValue("1000", "0", null),
                          1000.0, 0.0, "1000e0");
        assertNumberValue(new NumberValue("1000.", "0", null),
                          1000.0, 0.0, "1000.e0");
        assertNumberValue(new NumberValue("1.", "3", null),
                          1000.0, 0.0, "1.e3");
        assertNumberValue(new NumberValue("0.1000", "4", null),
                          1000.0, 0.0, "0.1000e4");
        assertNumberValue(new NumberValue("100000.", "-02", null),
                          1000.0, 0.0, "100000.e-02");
        assertNumberValue(new NumberValue("0.00001", "-000", null),
                          0.00001, 0.0, "0.00001e-000");
        assertNumberValue(new NumberValue("01", "-05", null),
                          0.00001, 0.0, "01e-05");
        assertNumberValue(new NumberValue("0.001", "-2", null),
                          0.00001, 0.0, "0.001e-2");
        assertNumberValue(new NumberValue("0.0000001", "+02", null),
                          0.00001, 0.0, "0.0000001e+02");
        assertNumberValue(new NumberValue("1234.56789", "+0000", null),
                          1234.56789, 0.0, "1234.56789e+0000");
        assertNumberValue(new NumberValue("1.23456789", "+03", null),
                          1234.56789, 0.0, "1.23456789e+03");
        assertNumberValue(new NumberValue(".0123456789", "005", null),
                          1234.56789, 0.0, ".0123456789e005");
        assertNumberValue(new NumberValue("1234567.89", "-3", null),
                          1234.56789, 0.0, "1234567.89e-3");
    }
    
    /**
     * Tests NumberValues initialized via the two-argument constructor with su
     * nonzero, to verify that they have the expected properties when no
     * truncation or scientific notation is expected
     */
    public void testConstructor_double_double__noExponent() {
        assertNumberValue(new NumberValue(0., 0.1), 0.0, 0.1, "0.00(10)");
        assertNumberValue(new NumberValue(0., 0.2), 0.0, 0.2, "0.0(2)");
        assertNumberValue(new NumberValue(0., 1.0), 0.0, 1.0, "0.0(10)");
        assertNumberValue(new NumberValue(0., 2.0), 0.0, 2.0, "0(2)");
        assertNumberValue(new NumberValue(1., 0.1), 1.0, 0.1, "1.00(10)");
        assertNumberValue(new NumberValue(1., 0.193), 1.0, 0.193, "1.00(19)");
        assertNumberValue(new NumberValue(1., 0.196), 1.0, 0.196, "1.0(2)");
        assertNumberValue(new NumberValue(1., 2.), 1.0, 2.0, "1(2)");
        assertNumberValue(new NumberValue(1., 1.6), 1.0, 1.6, "1.0(16)");
        assertNumberValue(new NumberValue(1., 1.0), 1.0, 1.0, "1.0(10)");
        assertNumberValue(new NumberValue(10., 1.0), 10.0, 1.0, "10.0(10)");
        assertNumberValue(new NumberValue(10., 2.0), 10.0, 2.0, "10(2)");
        assertNumberValue(new NumberValue(10., 10.0), 10.0, 10.0, "10(10)");
        assertNumberValue(new NumberValue(537.212, 0.003),
                          537.212, 0.003, "537.212(3)");
        assertNumberValue(new NumberValue(537.212, 0.001),
                          537.212, 0.001, "537.2120(10)");
        assertNumberValue(new NumberValue(-12., 3.0), -12.0, 3.0, "-12(3)");
        assertNumberValue(new NumberValue(-12., 0.04),
                          -12.0, 0.04, "-12.00(4)");
    }
    
    /**
     * Tests NumberValues initialized via the two-argument constructor with su
     * nonzero, to verify that they have the expected properties when scientific
     * notation but not truncation is expected
     */
    public void testConstructor_double_double__exponent() {
        assertNumberValue(new NumberValue(0., 10.0), 0.0, 10.0, "0.0e1(10)");
        assertNumberValue(new NumberValue(0., 20.0), 0.0, 20.0, "0e1(2)");
        assertNumberValue(new NumberValue(0., 5000.0), 0.0, 5000.0, "0e3(5)");
        assertNumberValue(new NumberValue(100., 20.0), 100.0, 20.0, "1.0e2(2)");
        assertNumberValue(new NumberValue(150., 20.0), 150.0, 20.0, "1.5e2(2)");
        assertNumberValue(new NumberValue(-100., 20.0),
                          -100.0, 20.0, "-1.0e2(2)");
        assertNumberValue(new NumberValue(1000., 100.0),
                          1000.0, 100.0, "1.00e3(10)");
        assertNumberValue(new NumberValue(1000., 193.0),
                          1000.0, 193.0, "1.00e3(19)");
        assertNumberValue(new NumberValue(1000., 196.0),
                          1000.0, 196.0, "1.0e3(2)");
        assertNumberValue(new NumberValue(-1000., 196.0),
                          -1000.0, 196.0, "-1.0e3(2)");
        assertNumberValue(new NumberValue(1000. - Math.ulp(1000.0), 193.0),
                          1000. - Math.ulp(1000.0), 193.0, "1.00e3(19)");
    }
    
    /**
     * Tests NumberValues initialized via the three-argument constructor with su
     * non-null and exponent null, to verify that they have the expected
     * properties when no truncation is expected
     */
    public void testConstructor_String_null_String() {
        assertNumberValue(new NumberValue("0.", null, "1"), 0.0, 1.0, "0.(1)");
        assertNumberValue(new NumberValue("0", null, "1"), 0.0, 1.0, "0(1)");
        assertNumberValue(new NumberValue("0.", null, "2"), 0.0, 2.0, "0.(2)");
        assertNumberValue(new NumberValue("0", null, "2"), 0.0, 2.0, "0(2)");
        assertNumberValue(new NumberValue("1.0", null, "1"),
                          1.0, 0.1, "1.0(1)");
        assertNumberValue(new NumberValue("1.000", null, "193"),
                          1.0, 0.193, "1.000(193)");
        assertNumberValue(new NumberValue("1.000", null, "196"),
                          1.0, 0.196, "1.000(196)");
        assertNumberValue(new NumberValue("1.", null, "2"), 1.0, 2.0, "1.(2)");
        assertNumberValue(new NumberValue("1", null, "2"), 1.0, 2.0, "1(2)");
        assertNumberValue(new NumberValue("1.0", null, "16"),
                          1.0, 1.6, "1.0(16)");
        assertNumberValue(new NumberValue("1.00", null, "160"),
                          1.0, 1.6, "1.00(160)");
        assertNumberValue(new NumberValue("1.", null, "1"),
                          1.0, 1.0, "1.(1)");
        assertNumberValue(new NumberValue("1", null, "1"),
                          1.0, 1.0, "1(1)");
        assertNumberValue(new NumberValue("10.", null, "1"),
                          10.0, 1.0, "10.(1)");
        assertNumberValue(new NumberValue("10.", null, "2"),
                          10.0, 2.0, "10.(2)");
        assertNumberValue(new NumberValue("10", null, "2"),
                          10.0, 2.0, "10(2)");
        assertNumberValue(new NumberValue("10.", null, "10"),
                          10.0, 10.0, "10.(10)");
        assertNumberValue(new NumberValue("10", null, "10"),
                          10.0, 10.0, "10(10)");
        assertNumberValue(new NumberValue("537.212", null, "3"),
                          537.212, 0.003, "537.212(3)");
        assertNumberValue(new NumberValue("537.212", null, "1"),
                          537.212, 0.001, "537.212(1)");
        assertNumberValue(new NumberValue("123", null, "0"),
                          123.0, 0.0, "123(0)");
        assertNumberValue(new NumberValue("123.", null, "0"),
                          123.0, 0.0, "123.(0)");
        assertNumberValue(new NumberValue("123.0", null, "0"),
                          123.0, 0.0, "123.0(0)");
        assertNumberValue(new NumberValue("123.400", null, "0"),
                          123.4, 0.0, "123.400(0)");
    }
    
    /**
     * Tests NumberValues initialized via the three-argument constructor with su
     * and exponent non-null, to verify that they have the expected properties
     * when no truncation is expected
     */
    public void testConstructor_String_String_String() {
        assertNumberValue(new NumberValue("0.", "1", "1"),
                          0.0, 10.0, "0.e1(1)");
        assertNumberValue(new NumberValue("0", "1", "1"),
                          0.0, 10.0, "0e1(1)");
        assertNumberValue(new NumberValue("1.", "1", "1"),
                          10.0, 10.0, "1.e1(1)");
        assertNumberValue(new NumberValue("1", "1", "1"),
                          10.0, 10.0, "1e1(1)");
        assertNumberValue(new NumberValue("10.0", "0", "14"),
                          10.0, 1.4, "10.0e0(14)");
        assertNumberValue(new NumberValue("10.0", "-00", "14"),
                          10.0, 1.4, "10.0e-00(14)");
        assertNumberValue(new NumberValue("0.025", "+00", "14"),
                          0.025, 0.014, "0.025e+00(14)");
        assertNumberValue(new NumberValue("0.025", "+02", "14"),
                          2.5, 1.4, "0.025e+02(14)");
        assertNumberValue(new NumberValue("0.025", "-03", "14"),
                          0.000025, 0.000014, "0.025e-03(14)");
    }
    
    /**
     * Tests NumberValues initialized via the one-argument constructor to verify
     * that they have the expected properties relative to rounding to
     * {@code NumberValue.MAX_SIGNIFICANT_DIGITS} digits, with and without
     * scientific notation
     */
    public void testConstructor_double__truncation() {
        assertNumberValue(new NumberValue(1.), 1.0, 0.0, "1");
        assertNumberValue(new NumberValue(1.2), 1.2, 0.0, "1.2");
        assertNumberValue(new NumberValue(1.23), 1.23, 0.0, "1.23");
        assertNumberValue(new NumberValue(1.23456789),
                          1.23456789, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(1.234567891),
                          1.234567891, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(1.234567986),
                          1.234567986, 0.0, "1.23456799");
        assertNumberValue(new NumberValue(1.23456789123),
                          1.23456789123, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(0.00123456789123),
                          0.00123456789123, 0.0, "1.23456789e-3");
        assertNumberValue(new NumberValue(1.23456789123e-7),
                          1.23456789123e-7, 0.0, "1.23456789e-7");
        assertNumberValue(new NumberValue(1.23456789123e15),
                          1.23456789123e15, 0.0, "1.23456789e15");
    }
    
    /**
     * Tests NumberValues initialized via the two-argument constructor with
     * a zero second argument, to verify that they have the expected properties
     * relative to rounding to {@code NumberValue.MAX_SIGNIFICANT_DIGITS}
     * digits, with and without scientific notation
     */
    public void testConstructor_double_zero__truncation() {
        assertNumberValue(new NumberValue(1.0, 0.0), 1.0, 0.0, "1");
        assertNumberValue(new NumberValue(1.2, 0.0), 1.2, 0.0, "1.2");
        assertNumberValue(new NumberValue(1.23, 0.0), 1.23, 0.0, "1.23");
        assertNumberValue(new NumberValue(1.23456789, 0.0),
                          1.23456789, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(1.234567891, 0.0),
                          1.234567891, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(1.234567986, 0.0),
                          1.234567986, 0.0, "1.23456799");
        assertNumberValue(new NumberValue(1.23456789123, 0.0),
                          1.23456789123, 0.0, "1.23456789");
        assertNumberValue(new NumberValue(0.00123456789123, 0.0),
                          0.00123456789123, 0.0, "1.23456789e-3");
        assertNumberValue(new NumberValue(1.23456789123e-7, 0.0),
                          1.23456789123e-7, 0.0, "1.23456789e-7");
        assertNumberValue(new NumberValue(1.23456789123e15, 0.0),
                          1.23456789123e15, 0.0, "1.23456789e15");
    }
    
    /**
     * Tests NumberValues initialized via the two-argument constructor with
     * a positive second argument, to verify that they have the expected
     * properties relative to rounding to the number of digits implied by the
     * su, or to {@code NumberValue.MAX_SIGNIFICANT_DIGITS} digits, whichever is
     * smaller, with and without scientific notation
     */
    public void testConstructor_double_double__truncation() {
        assertNumberValue(new NumberValue(1.0, 0.2), 1.0, 0.2, "1.0(2)");
        assertNumberValue(new NumberValue(1.0, 0.00002),
                          1.0, 0.00002, "1.00000(2)");
        assertNumberValue(new NumberValue(1.0, 0.00000002),
                          1.0, 0.00000002, "1.00000000(2)");
        assertNumberValue(new NumberValue(1.0, 0.000000002),
                          1.0, 0.000000002, "1");
        assertNumberValue(new NumberValue(1.0, 0.00000012),
                          1.0, 0.00000012, "1.00000000(12)");
        assertNumberValue(new NumberValue(1.0, 0.00000016),
                          1.0, 0.00000016, "1.00000000(16)");
        assertNumberValue(new NumberValue(1.0, 0.000000012),
                          1.0, 0.000000012, "1.00000000(1)");
        assertNumberValue(new NumberValue(1.0, 0.000000016),
                          1.0, 0.000000016, "1.00000000(2)");
        assertNumberValue(new NumberValue(1.0e-2, 0.0000000002),
                          1.0e-2, 0.0000000002, "1.00000000e-2(2)");
        assertNumberValue(new NumberValue(1.0e-2, 0.00000000002),
                          1.0e-2, 0.00000000002, "0.01");
        assertNumberValue(new NumberValue(1.0e-2, 0.0000000012),
                          1.0e-2, 0.0000000012, "1.00000000e-2(12)");
        assertNumberValue(new NumberValue(1.0e-2, 0.0000000016),
                          1.0e-2, 0.0000000016, "1.00000000e-2(16)");
        assertNumberValue(new NumberValue(1.0e-2, 0.00000000012),
                          1.0e-2, 0.00000000012, "1.00000000e-2(1)");
        assertNumberValue(new NumberValue(1.0e-2, 0.00000000016),
                          1.0e-2, 0.00000000016, "1.00000000e-2(2)");
    }
    
    /**
     * Asserts that the specified NumberValue has the specified properties and
     * String representation
     * 
     * @param  nv the {@code NumberValue} to test
     * @param  val the expected {@code double} value of {@code nv}
     * @param  su the expected {@code double} standard uncertainty of {@code nv}
     * @param  s the expected string value of {@code nv}
     */
    private void assertNumberValue(NumberValue nv, double val, double su,
                                   String s) {
        assertEquals("Wrong value;", val, nv.getValue(), Math.abs(val) * DELTA);
        assertEquals("Wrong su;", su, nv.getSU(), Math.abs(su) * DELTA);
        assertEquals("Wrong string representation;", s, nv.toString());
    }
    
    /**
     * Tests the mostSignificantDigit(double) method to verify that it returns
     * the correct value for all representable powers of 10 in the range 10e-300
     * to 10e300
     */
    public void testMethod_MostSignificantDigit_positiveDouble() {
        for (int i = -300; i < 301; i++) {
            
            /*
             * Build it via a String representation to ensure that we get
             * exactly the double value we want (or the closest representable
             * value to it)  
             */
            StringBuffer sb = new StringBuffer("1e");
            sb.append(i);
            double d = Double.parseDouble(sb.toString());
            
            assertEquals("Incorrect msd for " + d + ": ", i,
                         NumberValue.mostSignificantDigit(d));
        }
    }
}
