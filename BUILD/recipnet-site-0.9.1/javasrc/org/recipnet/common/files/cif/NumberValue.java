/*
 * Reciprocal Net Project
 * 
 * NumberValue.java
 * 
 * 02-02-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

import java.util.regex.Pattern;

import org.recipnet.common.files.CifFile;

/**
 * <p>
 * A {@code CifFile.CifValue} implementation representing a numeric value and,
 * optionally, a standard uncertainty for that value.  Instances may be
 * initialized either with numbers for value and (optionally) standard
 * uncertainty, or with string representations of decimal significand, optional
 * ten's exponent, and optional standard uncertainty digits for the last places
 * of the significand.  Whichever mode of initialization is used, the instance
 * will accurately reflect the initialization parameters:
 * </p><ul>
 * <li>if initialized with numbers, the {@code getValue()} and {@code getSU()}
 * methods will return the initialization values, even though they may have more
 * precision than is reflected in the result of {@code toString()};</li>
 * <li>if initialized with {@code String}s, the {@code toString()} method will
 * return a corresponding {@code String}, even if the components have excess
 * precision or are otherwise not formatted conventionally.</li>
 * </ul><p>
 * In the case of numeric initialization, the precision of the value displayed
 * by {@code toString()} is controlled by the specified standard uncertainty
 * (if any), a convention for standard uncertainty precision reflected by
 * {@code SU_RULE}, and by a maximum number of significant digits
 * ({@code MAX_SIGNIFICANT_DIGITS}).  Scientific notation will be produced if
 * there would otherwise be at least {@code MIN_MSD} leading zeroes, or if the
 * value is greater than zero and the units digit of the value is not among the
 * significant digits.
 * </p><p>
 * Instances of this class are immutable.  Among other useful results, this
 * guarantees that they may be shared among threads without concern about thread
 * safety.
 * </p>
 *  
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public final class NumberValue implements CifFile.CifValue {

    /**
     * The maximum number of significant digits used by this class to represent
     * values when an instance is constructed from a {@code double} value
     */
    /*
     * This number should be less than the fewest number of digits of decimal
     * precision offered by any finite, representable double value 
     */
    public final static int MAX_SIGNIFICANT_DIGITS = 9;
    
    /**
     * The maximum scaled standard uncertainty value; used in determining the
     * least significant digit of a value from its standard uncertainty.  The
     * scaled standard uncertainty is the standard uncertainty times ten to
     * the power of the index of the least significant digit of the associated
     * value, rounded to the nearest integer.  A convention for the precision of
     * the standard uncertainty is necessary to complete that definition; that's
     * the role of {@code SU_RULE}.
     */
    /*
     * SU_RULE should not have more decimal digits than MAX_SIGNIFICANT_DIGITS,
     * and it should not be less than nine 
     */
    public final static int SU_RULE = 19;
    
    /**
     * The minimum most significant digit index of the decimal representation of
     * a value presented without use of scientific notation
     */
    public final static int MIN_MSD = -5;
    
    /**
     * A Pattern describing strings containing only characters that can appear
     * in a decimal number.  Strings matching this pattern are not necessarily
     * parseable as numbers.
     */
    private final static Pattern DECIMAL_CHARACTERS =
        Pattern.compile("[-+.0-9]+");
    
    /**
     * A {@code String} representation of the value and standard uncertainty, in
     * CIF format
     */
    private final String string;

    /**
     * A {@code double} representation of the value
     */
    private final double doubleValue;
    
    /**
     * A {@code double} representation of the standard uncertainty
     */
    private final double doubleSU;

    /**
     * Initializes a new {@code NumberValue} with the specified value, exponent
     * and standard uncertainties, expressed as {@code String}s.  NumberValues
     * initialized via this constructor have string values constructed from
     * the provided Strings, so that they preserve the original formatting.  If
     * the specified value and exponent indicate a value outside the range of
     * representable {@code double}s then the results are undefined.
     * 
     * @param  valBase a {@code String} containing the number's value, in
     *         decimal, less any scientific notation exponent or type indicator
     *         suffix; should be parseable as a {@code double} according to Java
     *         rules   
     * @param  exponent a {@code String}, either {@code null} (signifying no
     *         exponent) or containing the number's decimal scientific notation
     *         exponent, excluding the leading 'e' or 'E'; if not {@code null}
     *         then should be parseable as an {@code int} according to Java
     *         rules
     * @param  su a {@code String}, either {@code null} (signifying no standard
     *         uncertainty) or containing the number's standard uncertainty in
     *         its least significant digits; if not {@code null} then should be
     *         parseable as a non-negative {@code int} according to Java rules
     *
     * @throws NumberFormatException if any of the arguments is not formatted
     *         as described
     * 
     * @see java.lang.Double#parseDouble(String)
     * @see java.lang.Integer#parseInt(String)
     */
    public NumberValue(String valBase, String exponent, String su) {
        StringBuffer sb = new StringBuffer(valBase);
        
        if (!DECIMAL_CHARACTERS.matcher(valBase).matches()) {
            throw new NumberFormatException();
        } else if (exponent != null) {
            if (!DECIMAL_CHARACTERS.matcher(exponent).matches()) {
                throw new NumberFormatException();
            } else {
                sb.append('e').append(exponent);
            }
        }
        this.doubleValue = Double.parseDouble(sb.toString());
        
        if (su == null) {
            this.doubleSU = 0.0;
        } else {
            // parse su as int (for validation), assign the result to double:
            double temp = Integer.parseInt(su);
            int exp;
            
            if (temp < 0) {
                throw new NumberFormatException("su may not be negative");
            }
            if (exponent != null) {
                exp = Integer.parseInt(
                        (exponent.charAt(0) == '+') ? exponent.substring(1)
                                                    : exponent);
            } else {
                exp = 0;
            }
            int place = valBase.indexOf('.') + 1;
                
            if (place > 0) {
                exp += (place - valBase.length());
            }
            
            sb.append('(').append(su).append(')');
            this.doubleSU = scale(temp, exp);
        }
        
        this.string = sb.toString();
    }

    /**
     * Initializes a new {@code NumberValue} with the specified value and no
     * standard uncertainty
     * 
     * @param  val the {@code double} value represented by this
     *         {@code NumberValue}
     *         
     * @throws IllegalArgumentException if the argument is NaN or infinite
     */
    public NumberValue(double val) {
        this(val, 0.0); 
    }

    /**
     * Initializes a new {@code NumberValue} with the specified value and
     * standard uncertainty
     * 
     * @param  val the {@code double} value represented by this
     *         {@code NumberValue}
     * @param  su the {@code double} standard uncertainty of the value
     *         represented by this {@code NumberValue}
     * 
     * @throws IllegalArgumentException if {@code su} is negative or if either
     *         argument is NaN or infinite
     */
    public NumberValue(double val, double su) {
        StringBuffer valbuf;
        double suReduced;
        int msd;
        int lsd;
        
        if (Double.isNaN(val) || Double.isInfinite(val) ) {
            throw new IllegalArgumentException(
                    "Illegal numeric value: " + val);
        } else if (Double.isNaN(su) || Double.isInfinite(su) || (su < 0.0)) {
            throw new IllegalArgumentException(
                    "Illegal standard uncertainty: " + su);
        } else if (su == 0.0) {
            if (val == 0.0) {
                msd = 0;
                lsd = 0;
                valbuf = new StringBuffer("0");
            } else {
                
                // create a representation with MAX_SIGNIFICANT_DIGITS digits 
                msd = mostSignificantDigit(val);
                lsd = 1 + msd - MAX_SIGNIFICANT_DIGITS;
                valbuf = new StringBuffer(String.valueOf(
                        (long) Math.abs(Math.rint(scale(val, -lsd)))));
            }
            suReduced = 0.0;
        } else {
            // the index of the most significant digit of the s.u.  
            int suMsd = mostSignificantDigit(su);
            
            // the index of the most significant digit of the value / s.u. pair
            msd = Math.max(suMsd, mostSignificantDigit(val));
            
            /*
             * Determine the least significant digit of the value based on the
             * standard uncertainty, the SU rule in effect (e.g. "rule of 19"),
             * and the maximum supported number of significant digits
             */
            for (lsd = suMsd; Math.rint(scale(su, 1 - lsd)) <= SU_RULE; lsd--) {
                // does nothing (else)
            }
            if ((msd - lsd) >= MAX_SIGNIFICANT_DIGITS) {
                lsd = 1 + msd - MAX_SIGNIFICANT_DIGITS;
            } 
            
            // round to the correct number of significant digits
            suReduced = Math.rint(scale(su, -lsd));
            
            /*
             * construct a text representation, inserting leading zeroes as
             * necessary to fill all the digit positions from lsd to msd
             */
            valbuf = new StringBuffer(String.valueOf(
                    (long) Math.abs(Math.rint(scale(val, -lsd)))));
            while (valbuf.length() < (1 + msd - lsd)) {
                valbuf.insert(0, '0');
            }
        }
        
        // Fix up the text representation (at this point a bare digit string)

        if (suReduced == 0.0) {
            // remove trailing zeroes after the decimal point
            while ((lsd < 0)
                   && (valbuf.charAt(valbuf.length() - 1) == '0')) {
                lsd++;
                valbuf.deleteCharAt(valbuf.length() - 1);
            }
        }
        
        if (((1 - lsd) < MAX_SIGNIFICANT_DIGITS) && (msd >= MIN_MSD)) {
            while (msd < 0) {
                valbuf.insert(0, '0');
                msd++;
            }
        }
        
        if ((lsd > 0) || (msd < 0) || ((msd > 0) && (valbuf.charAt(0) == '0'))) {
            // need to use scientific notation:
            if (lsd != msd) {
                valbuf.insert(1, '.');
            }
            valbuf.append('e').append(msd);
        } else {
            while (msd < 0) {
                valbuf.insert(0, '0');
                msd++;
            }
            if (lsd < 0) {
                
                // insert a decimal point where appropriate
                valbuf.insert(valbuf.length() + lsd, '.');
            }
        }
        if (val < 0) {
            
            // insert a minus sign for negative values
            valbuf.insert(0, '-');
        }

        if (suReduced > 0.0) {
            valbuf.append('(').append((int) suReduced).append(')');
        }
        
        // Initialize this object's members
        
        this.doubleValue = val;
        this.doubleSU = su;
        this.string = valbuf.toString();
    }

    /**
     * Returns the numeric value represented by this {@code NumberValue},
     * as a {@code double}
     * 
     * @return the numeric value as a {@code double}
     */
    public double getValue() {
        return doubleValue;
    }

    /**
     * Returns the standard uncertainty of the value represented by this
     * {@code NumberValue}, as a {@code double}
     * 
     * @return the standard uncertainty as a {@code double}
     */
    public double getSU() {
        return doubleSU;
    }

    /**
     * Returns a {@code String} representation of this object; per the
     * specifications of the {@code CifValue} interface, this representation
     * corresponds to the CIF syntax for representing the value (and optional
     * standard uncertainty) represented by this object 
     *
     * @return the {@code String} representation of this object
     *  
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return string;
    }

    /**
     * Determines the index of the most significant digit of the argument,
     * defined as the greatest integer {@code i} such that
     * {@code 10<sup>i</sup>} is less than or equal to the absolute value of
     * the argument
     * 
     * @param  d the {@code double} to evaluate
     * 
     * @return the index of the most significant digit of {@code d}
     */
    static int mostSignificantDigit(double d) {
        return (d == 0.0) ? 0 : (int) Math.floor(Math.log10(Math.abs(d)));
    }
    
    /**
     * Computes a value of the provided {@code double} scaled (multiplied) by
     * the specified power of ten
     * 
     * @param  d the {@code double} value to scale
     * @param  power10 the power of ten by which to scale
     * 
     * @return the scaled value
     */
    static double scale(double d, int power10) {
        return (d * Math.pow(10, power10));
    }
}