/*
 * Reciprocal Net Project
 * 
 * StringValue.java
 * 
 * 02-02-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

import java.util.regex.Pattern;
import org.recipnet.common.files.CifFile;

/**
 * <p>
 * A <code>CifFile.CifValue</code> implementation that represents a value of
 * CIF type "char"; i.e. a quoted or unquoted string.
 * </p><p>
 * Instances of this class are immutable.  Among other useful results, this
 * guarantees that they may be shared among threads without concern about thread
 * safety.
 * </p>
 * 
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public final class StringValue implements CifFile.CifValue {
    
    /** A cached copy of the system-dependant line separator string */
    private final static String LINE_SEPARATOR =
            System.getProperty("line.separator");
            
    /**
     * A pattern matching string values that need to be enclosed in quotes in
     * CIF -- specifically, a lone question mark character; a string starting
     * with a digit, decimal point, plus symbol, minus symbol, or a single or
     * double quote character; or a string containing CIF whitespace
     */
    private final static Pattern NEEDS_QUOTES_PATTERN = Pattern.compile(
            "\\?|(?:loop_)|(?:global_)|(?:stop_)"
            +  "|(?:(?:(?:data_)|(?:save_)"
            +                  "|(?:[-+.0-9_#$'\";\\[\\]])"
            +                  "|(?:.*[ \t\u0009])"
            +       ").*"
            +    ")"
        );
    
    /**
     * A pattern matching string values that need to be enclosed in a text block
     * in CIF.  These are strings that contain internal line termination
     * characters.
     */
    private final static Pattern NEEDS_BLOCK_PATTERN = Pattern.compile(
            ".*[\r\n\f].*"                                                                       
        );
    
    /**
     * A <code>String</code> containing the characters of the underlying CIF
     * value represented by this object 
     */
    private final String value;

    /**
     * Initializes a new <code>StringValue</code> with the specified string
     * underlying value
     * 
     * @param  s a <code>String</code> containing the characters of the
     *         underlying CIF value represented by this object; may not be null
     */
    public StringValue(String s) {
        if (s == null) {
            throw new NullPointerException("The value string may not be null");
        } else {
            value = s;
        }
    }
    
    /**
     * Returns the bare value represented by this {@code StringValue}
     * 
     * @return the {@code String} value
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Returns the <code>String</code> representation of this
     * <code>StringValue</code>; the result is enclosed in text block delimiters
     * if it contains line termination characters, and in single quotes if it
     * otherwise requires quotation.  The underlying value is not modified in
     * any way -- in particular, it is never wrapped to multiple lines and
     * embedded line termination characters are left as-is
     * 
     * @return a <code>String</code> containing an appropriate CIF
     *         representation of this <code>CifFile.CifValue</code>'s data
     * 
     * @throws IllegalStateException if this {@code StringValue}'s value is not
     *         representable in CIF, which is the case for values that contain
     *         both "' " and "\" " (Java syntax) as substrings
     */
    @Override
    public String toString() {
        if (NEEDS_BLOCK_PATTERN.matcher(value).matches()) {
            StringBuffer sb = new StringBuffer(value.length() + 6);
            
            sb.append(StringValue.LINE_SEPARATOR).append(';');
            sb.append(value);
            sb.append(StringValue.LINE_SEPARATOR).append(';');
            return sb.toString();
        } else if ((value.length() == 0)
                   || NEEDS_QUOTES_PATTERN.matcher(value).matches()) {
            StringBuffer sb = new StringBuffer(value.length() + 2);
            if (value.indexOf("' ") >= 0) {
                if (value.indexOf("\" ") >= 0) {
                    throw new IllegalStateException(
                            "Value has no CIF representation");
                } else {
                    sb.append('"').append(value).append('"');
                }
            } else {
                sb.append('\'').append(value).append('\'');
            }
            
            return sb.toString();           
        } else {
            return value;
        }
    }
}
