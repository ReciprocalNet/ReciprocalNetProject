/*
 * Reciprocal Net project
 * @(#)CifError.java
 *
 * 23-Jul-2002: jobollin wrote first draft
 * 20-Oct-2003: jobollin corrected a bug in the two-arg constructor and a typo
 *              in messageForCode
 */

package org.recipnet.site.util.cifimporter;

/**
 * a class abstracting a CIF syntax or semantic error.  Despite the name,
 * this class does not descend from Error.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 */
public final class CifError {

    /* error code bitmasks */
    private static final int SYNTAX_SUBJECT_MINOR_MASK = 0x01;
    private static final int SYNTAX_SUBJECT_MAJOR_MASK = 0x06;
    private static final int SYNTAX_SPECIAL_MASK =       0x01;
    private static final int SYNTAX_QUOTING_MASK =       0x02;
    private static final int SYNTAX_CHARACTER_MASK =     0x04;
    private static final int DATA_NAME_MASK =            0x01;
    private static final int DATA_VALUE_MASK =           0x02;
    private static final int STRUCTURE_MASK =            0x04;
    private static final int PARSE_SUBJECT_MASK =        0x07;
    private static final int ERROR_IN_LOOP_MASK =        0x08;
    private static final int ERROR_AT_EOF_MASK =         0x10;
    private static final int ERROR_WITH_BLOCK_MASK =     0x20;
    private static final int PARSE_ERROR_MASK =          0x40;

    /**
     * error code for no error
     */
    public static final int NO_ERROR = 0;

    /* Syntax errors */

    /**
     * error code for a generic syntax error
     */
    public static final int SYNTAX_ERROR =
            SYNTAX_SPECIAL_MASK;

    /**
     * error code for an unterminated in-line quoted string
     */
    public static final int UNTERMINATED_QSTRING =
            SYNTAX_QUOTING_MASK;

    /**
     * error code an unterminated text block
     */
    public static final int UNTERMINATED_TEXT =
            SYNTAX_QUOTING_MASK | SYNTAX_SPECIAL_MASK;

    /**
     * error code for an illegal character
     */
    public static final int ILLEGAL_CHAR =
            SYNTAX_CHARACTER_MASK;

    /**
     * error code for line too long
     */
    public static final int LINE_LENGTH =
            SYNTAX_CHARACTER_MASK | SYNTAX_SPECIAL_MASK;

    /**
     * error code for an unquoted data value beginning with '$'
     */
    public static final int BEGINS_WITH_DOLLAR =
            SYNTAX_CHARACTER_MASK | SYNTAX_QUOTING_MASK | SYNTAX_SPECIAL_MASK;


    /* parse errors */

    /**
     * error code for a generic parse error
     */
    public static final int PARSE_ERROR =
            PARSE_ERROR_MASK;

    /**
     * error code for a missing data name
     */
    public static final int DATA_NAME_MISSING =
            PARSE_ERROR_MASK | DATA_NAME_MASK;

    /**
     * error code for a missing data value
     */
    public static final int DATA_VALUE_MISSING =
            PARSE_ERROR_MASK | DATA_VALUE_MASK;

    /**
     * error code for a loop header with no data names
     */
    public static final int LOOP_EMPTY_HEADER =
            ERROR_IN_LOOP_MASK | DATA_NAME_MISSING;

    /**
     * error code for an extra "loop_" keyword
     */
    public static final int EXTRA_LOOP =
            LOOP_EMPTY_HEADER | STRUCTURE_MASK;

    /**
     * error code for a loop with no data values
     */
    public static final int LOOP_NO_DATA =
            ERROR_IN_LOOP_MASK | DATA_VALUE_MISSING;

    /**
     * error code for a loop with an incomplete data record
     */
    public static final int LOOP_PARTIAL_RECORD =
            ERROR_IN_LOOP_MASK | DATA_VALUE_MISSING | STRUCTURE_MASK;

    /**
     * error code for a missing data value at the end of the file (file may be
     * truncated)
     */
    public static final int DATA_VALUE_MISSING_EOF =
            DATA_VALUE_MISSING | ERROR_AT_EOF_MASK;

    /**
     * error code for a loop header with no data names at the end of the
     * file (file may be truncated)
     */
    public static final int LOOP_EMPTY_HEADER_EOF =
            LOOP_EMPTY_HEADER | ERROR_AT_EOF_MASK;

    /**
     * error code for a loop header with no data values at the end of the
     * file (file may be truncated)
     */
    public static final int LOOP_NO_DATA_EOF =
            LOOP_NO_DATA | ERROR_AT_EOF_MASK;

    /**
     * error code for a loop with an incomplete data record at the end of the
     * file (file may be truncated)
     */
    public static final int LOOP_PARTIAL_RECORD_EOF =
            LOOP_PARTIAL_RECORD | ERROR_AT_EOF_MASK;

    /**
     * error code for a duplicate data name in a data block
     */
    public static final int DUPLICATE_DATA_NAME =
            PARSE_ERROR_MASK | DATA_NAME_MASK | STRUCTURE_MASK;

    /**
     * error code for an unquoted reserved word appearing as a data value
     */
    public static final int RESERVED_WORD =
            PARSE_ERROR_MASK | DATA_VALUE_MASK | STRUCTURE_MASK;

    /**
     * error code for a duplicate data block code in the same file
     */
    public static final int DUPLICATE_BLOCK_CODE =
            PARSE_ERROR_MASK | ERROR_WITH_BLOCK_MASK;

    /**
     * error code for a data name appearing outside a data block
     */
    public static final int DATA_NAME_NO_BLOCK =
            PARSE_ERROR_MASK | ERROR_WITH_BLOCK_MASK | DATA_NAME_MASK;

    /**
     * error code for a data value appearing outside a data block
     */
    public static final int VALUE_NO_BLOCK =
            PARSE_ERROR_MASK | ERROR_WITH_BLOCK_MASK | DATA_VALUE_MASK;

    /**
     * error code for a "loop_" keyword appearing outside a data block
     */
    public static final int LOOP_HEADER_NO_BLOCK =
            PARSE_ERROR_MASK | ERROR_WITH_BLOCK_MASK | ERROR_IN_LOOP_MASK;


    /* state variables */
    private int errorCode;
    private int location;
    private String context;
    private String errorMessage;

    /**
     * constructs a generic <code>CifError</code> encoding no error and
     * without location or context
     */
    public CifError() {
        this(NO_ERROR, -1, null);
    }

    /**
     * constructs a <code>CifError</code> with the specified error code
     * and location but no context
     */
    public CifError(int code, int location) {
        this(code, location, null);
    }

    /**
     * constructs a <code>CifError</code> with the specified error code,
     * location, and context
     */
    public CifError(int code, int location, String context) {
        errorCode = code;
        this.location = location;
        this.context = context;
        errorMessage = ((errorCode == NO_ERROR) ? "no error" : null);
    }

    /**
     * returns this object's error code
     *
     * @return the error code
     */
    public int getCode() {
        return errorCode;
    }

    /**
     * returns this object's context string
     *
     * @return the context as a <code>String</code>
     */
    public String getContext() {
        return context;
    }

    /**
     * returns this object's location value
     *
     * @return the location
     */
    public int getLocation() {
        return location;
    }

    /**
     * returns a full, formatted error message appropriate for this
     * <code>CifError</code>.  The message is more complete than that
     * produced by {@link #messageForCode(int) messageForCode}.
     *
     * @return a <code>String</code> containing a formatted error message
     *         corresponding to this <code>CifError</code>
     */
    public String message() {
        if (errorMessage == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("CIF ");
            sb.append( ((errorCode & PARSE_ERROR_MASK) == 0)
                       ? "syntax " : "parse " );
            sb.append("error");
            if (location > 0) {
                sb.append(" at line ").append(location);
            }
            if (context != null) {
                sb.append(", near '").append(context).append('\'');
            }
            sb.append(": ").append(messageForCode(errorCode));
            errorMessage = sb.toString();
        }
        return errorMessage;
    }

    /**
     * converts error codes into messages.  A future version may make use
     * of the bitmask structure of the error codes, but this version does
     * not.
     *
     * @param  code the error code
     *
     * @return a <code>String</code> containing a brief text description of
     *         the error corresponding to <code>code</code>
     */
    public static String messageForCode(int code) {
        switch (code) {
        case SYNTAX_ERROR:
            return "syntax error";
        case LINE_LENGTH:
            return "line too long";
        case UNTERMINATED_QSTRING:
            return "unterminated quoted data value";
        case UNTERMINATED_TEXT:
            return "unterminated text block";
        case ILLEGAL_CHAR:
            return "illegal character";
        case BEGINS_WITH_DOLLAR:
            return "unquoted data values may not begin with '$'";
        case PARSE_ERROR:
            return "parse error";
        case DATA_NAME_MISSING:
            return "data name missing";
        case DATA_VALUE_MISSING:
            return "data value missing";
        case LOOP_EMPTY_HEADER:
            return "empty loop header";
        case EXTRA_LOOP:
            return "duplicate loop_ keyword";
        case LOOP_NO_DATA:
            return "loop without data values";
        case LOOP_PARTIAL_RECORD:
            return "incomplete loop data record";
        case DATA_VALUE_MISSING_EOF:
            return "data value missing at end of file";
        case LOOP_EMPTY_HEADER_EOF:
            return "extraneous loop_ keyword at end of file";
        case LOOP_NO_DATA_EOF:
            return "loop without data values at end of file";
        case LOOP_PARTIAL_RECORD_EOF:
            return "incomplete loop data record at end of file";
        case DUPLICATE_DATA_NAME:
            return "duplicate data name";
        case RESERVED_WORD:
            return "illegal use of a reserved word";
        case DUPLICATE_BLOCK_CODE:
            return "duplicate data block code";
        case DATA_NAME_NO_BLOCK:
            return "data name outside any data block";
        case VALUE_NO_BLOCK:
            return "data value outside any data block";
        case LOOP_HEADER_NO_BLOCK:
            return "loop header outside any data block";
        default:
            return "unrecognized or non-CIF error";
        }
    }

    /**
     * returns a <code>String</code> representation of this object.  The
     * representation used in this implementation is the same as that
     * returned by this object's {@link #message() message} method.
     *
     * @return a <code>String</code> representation of this
     *         <code>CifError</code>
     */
    public String toString() {
        return message();
    }

}

