/*
 * Reciprocal Net project
 *
 * CifError.java
 *
 * 23-Jul-2002: jobollin wrote first draft
 * 20-Oct-2003: jobollin corrected a bug in the two-arg constructor and a typo
 *              in messageForCode
 * 26-Jan-2005: jobollin copied this class from package
 *              org.recipnet.site.util.cifimporter
 */

package org.recipnet.common.files.cif;

/**
 * A class representing a CIF syntax or semantic error.  Despite the name,
 * this class does not descend from Error.
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifError {

    /*
     * Note: int-valued error codes were chosen for this class over codes
     * expressed by a Java enum so as to allow codes not defined by this class
     * to be used by subclasses and / or by client code (e.g. in client error
     * handlers).  It may be reasonable to revisit this decision in the future,
     * but those considerations should be borne in mind when doing so. 
     */
    
    /*
     * error code bitmasks
     */
    
    /* subject codes */
    private static final int CHARACTER_MASK =    0x0001;
    private static final int DATA_NAME_MASK =    0x0002;
    private static final int DATA_VALUE_MASK =   0x0004;
    private static final int LOOP_MASK =         0x0008;
    private static final int FRAME_MASK =        0x0010;
    private static final int DATABLOCK_MASK =    0x0020;
    
    /* predicate codes */
    private static final int QUOTING_MASK =      0x0100;
    private static final int LENGTH_MASK =       0x0200;
    private static final int UNIQUENESS_MASK =   0x0400;
    private static final int MISSING_MASK =      0x0800;
    private static final int STRUCTURE_MASK =    0x1000;
    
    /* type flag */
    private static final int PARSE_ERROR_MASK =  0x8000;

    /**
     * error code for no error
     */
    public static final int NO_ERROR = 0;

    /**
     * error code for an illegal character
     */
    public static final int ILLEGAL_CHAR =
            CHARACTER_MASK;

    /**
     * error code for (detected) missing whitespace.  This can happen in the
     * CIF 1.1 grammar if the closing delimiter of a text block is not followed
     * by whitespace, but other instances of missing whitespace are not likely
     * to be recognized by a scanner (they will generally cause parsing errors
     * instead of lexical ones, as what were supposed to be two tokens will be
     * interpreted as one)
     */
    public static final int MISSING_WHITESPACE =
            CHARACTER_MASK | MISSING_MASK;

    /**
     * error code for an unquoted data value beginning with a disallowed char
     * ('$', '[', ']')
     */
    public static final int ILLEGAL_VALUE_START =
            CHARACTER_MASK | QUOTING_MASK;

    /**
     * error code for an unterminated in-line quoted string
     */
    public static final int UNTERMINATED_QSTRING =
            DATA_VALUE_MASK | QUOTING_MASK;

    /**
     * error code an unterminated text block
     */
    public static final int UNTERMINATED_TEXT =
            DATA_VALUE_MASK | QUOTING_MASK | STRUCTURE_MASK;

    /**
     * error code for line too long
     */
    public static final int LINE_LENGTH =
            LENGTH_MASK;

    /**
     * error code for data name too long
     */
    public static final int DATA_NAME_LENGTH =
            DATA_NAME_MASK | LENGTH_MASK;

    /**
     * error code for save frame name too long
     */
    public static final int FRAME_NAME_LENGTH =
            FRAME_MASK | LENGTH_MASK;

    /**
     * error code for data block name too long
     */
    public static final int BLOCK_NAME_LENGTH =
            DATABLOCK_MASK | LENGTH_MASK;

    /**
     * error code for a missing data name
     */
    public static final int DATA_NAME_MISSING =
            PARSE_ERROR_MASK | DATA_NAME_MASK | MISSING_MASK;

    /**
     * error code for a missing data value
     */
    public static final int DATA_VALUE_MISSING =
            PARSE_ERROR_MASK | DATA_VALUE_MASK | MISSING_MASK;

    /**
     * error code for a loop header with no data names (and therefore no data
     * values)
     */
    public static final int LOOP_EMPTY_HEADER =
            LOOP_MASK | DATA_NAME_MISSING;

    /**
     * error code for a loop with no data values
     */
    public static final int LOOP_NO_DATA =
            LOOP_MASK | DATA_VALUE_MISSING;

    /**
     * error code for a loop with an incomplete data record
     */
    public static final int LOOP_PARTIAL_RECORD =
            LOOP_MASK | DATA_VALUE_MISSING | STRUCTURE_MASK;

    /**
     * error code for an unquoted reserved word appearing as a data value
     */
    public static final int RESERVED_WORD =
            PARSE_ERROR_MASK | DATA_VALUE_MASK;

    /**
     * error code for a duplicate data name in a data block
     */
    public static final int DUPLICATE_DATA_NAME =
            PARSE_ERROR_MASK | DATA_NAME_MASK | UNIQUENESS_MASK;

    /**
     * error code for a duplicate frame code in a data block
     */
    public static final int DUPLICATE_FRAME_CODE =
            PARSE_ERROR_MASK | FRAME_MASK | UNIQUENESS_MASK;

    /**
     * error code for a duplicate data block code in the same file
     */
    public static final int DUPLICATE_BLOCK_CODE =
            PARSE_ERROR_MASK | DATABLOCK_MASK | UNIQUENESS_MASK;

    /**
     * error code for a save frame that is not terminated before a new
     * save frame or data block is started
     */            
    public static final int UNTERMINATED_FRAME =
            PARSE_ERROR_MASK | FRAME_MASK | STRUCTURE_MASK;

    /**
     * error code for a data name appearing outside a data block
     */
    public static final int DATA_NAME_NO_BLOCK =
            PARSE_ERROR_MASK | DATABLOCK_MASK | DATA_NAME_MASK;

    /**
     * error code for a data value appearing outside a data block
     */
    public static final int VALUE_NO_BLOCK =
            PARSE_ERROR_MASK | DATABLOCK_MASK | DATA_VALUE_MASK;

    /**
     * error code for a "loop_" keyword appearing outside a data block
     */
    public static final int LOOP_HEADER_NO_BLOCK =
            PARSE_ERROR_MASK | DATABLOCK_MASK | LOOP_MASK;

    /**
     * error code for a save frame that appears outside the scope of any data
     * block
     */
    public static final int FRAME_NO_BLOCK =
            PARSE_ERROR_MASK | DATABLOCK_MASK | FRAME_MASK;

    /**
     * The integer error code for this {@code CifError}
     */
    private final int errorCode;
    
    /**
     * A {@code ScanState} representing the context of this {@code CifError};
     * may be {@code null} 
     */
    private final ScanState scanState;
    
    /**
     * A diagnostic message constructed at need based on this CifError's error
     * code and scan state
     */
    private String errorMessage;

    /**
     * constructs a generic {@code CifError} encoding no error
     */
    public CifError() {
        this(NO_ERROR, null);
    }

    /**
     * constructs a {@code CifError} with the specified error code and
     * scan state
     * 
     * @param  code the error code for this {@code CifError}
     * @param  state a {@code ScanState} describing the scanner state when the
     *         error condition was detected; may be {@code null}
     */
    public CifError(int code, ScanState state) {
        errorCode = code;
        errorMessage = ((errorCode == NO_ERROR) ? "no error" : null);
        scanState = ((state == null) ? null : new ScanStateSnapshot(state));
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
     * returns a full, formatted error message appropriate for this
     * {@code CifError}.  The message is more complete than that
     * produced by {@link #messageForCode(int) messageForCode}.
     *
     * @return a {@code String} containing a formatted error message
     *         corresponding to this {@code CifError}
     */
    public String getMessage() {
        if (errorMessage == null) {
            StringBuffer sb = new StringBuffer();
            sb.append("CIF ");
            sb.append( ((errorCode & PARSE_ERROR_MASK) == 0)
                       ? "syntax" : "structural" );
            sb.append(" error");
            if (scanState != null) {
                String token = scanState.getCurrentToken();

                sb.append(" at line ");
                sb.append(scanState.getLineNumber());
                sb.append(", character ");
                sb.append(scanState.getCharacterNumber());
                if (token.length() > 0) {
                    sb.append(", near '");
                    sb.append(token);
                    sb.append('\'');
                }
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
     * @return a {@code String} containing a brief text description of
     *         the error corresponding to {@code code}
     */
    public static String messageForCode(int code) {
        switch (code) {
        case NO_ERROR:
            return "no error";
        case LINE_LENGTH:
            return "line too long";
        case BLOCK_NAME_LENGTH:
            return "data block name too long";
        case FRAME_NAME_LENGTH:
            return "save frame name too long";
        case DATA_NAME_LENGTH:
            return "data name too long";
        case UNTERMINATED_QSTRING:
            return "unterminated quoted data value";
        case UNTERMINATED_TEXT:
            return "unterminated text block";
        case ILLEGAL_CHAR:
            return "illegal character";
        case ILLEGAL_VALUE_START:
            return "illegal first character for an unquoted data value";
        case MISSING_WHITESPACE:
            return "required whitespace missing";
        case DATA_NAME_MISSING:
            return "data name missing";
        case DATA_VALUE_MISSING:
            return "data value missing";
        case LOOP_EMPTY_HEADER:
            return "empty loop header";
        case LOOP_NO_DATA:
            return "loop with no data values";
        case LOOP_PARTIAL_RECORD:
            return "incomplete loop data record";
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
        case DUPLICATE_FRAME_CODE:
            return "duplicate frame code within a data block";
        case UNTERMINATED_FRAME:
            return "unterminated save frame";
        case FRAME_NO_BLOCK:
            return "save frame outside any data block";
        default:
            return "unknown error";
        }
    }

    /**
     * Returns a ScanState representing the scanner state at the point when
     * this {@code CifError} was flagged.
     * 
     * @return  a {@code ScanState} describing the scanner state at the
     *          point when this {@code CifError} was flagged.
     */
    public ScanState getScanState() {
        return scanState;
    }

    /**
     * returns a {@code String} representation of this object.  The
     * representation used in this implementation is the same as that
     * returned by this object's {@link #getMessage()} method.
     *
     * @return a {@code String} representation of this
     *         {@code CifError}
     */
    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * A {@code ScanState} implementation that captures a copy of the
     * externally-visible properties of another {@code ScanState}, so as
     * to preserve the details of that state in the event of future changes to
     * it.
     *
     * @author John C. Bollinger
     * @version 0.9.0
     */
    private static class ScanStateSnapshot implements ScanState {

        /** the current character as an {@code int} */
        private int currentChar;

        /** the previous character as an {@code int} */
        private int lastChar;

        /** the current line number in the input */
        private int lineNumber;

        /** the current character number on the line */
        private int characterNumber;

        /** the current token, to this point in the input */
        private String currentToken;
        
        /**
         * the ScanState of which this is one is a copy; used to enable
         * setting the current char by the error handler -- provided that the
         * original state still matches this snapshot
         */
        private final ScanState originalState;

        /**
         * Initializes a new {@code ScanStateSnapshot} with the details of
         * the specified {@code ScanState}
         *
         * @param  state the {@code ScanState} to be copied
         */
        public ScanStateSnapshot(ScanState state) {
            currentChar = state.getCurrentChar();
            lastChar = state.getLastChar();
            lineNumber = state.getLineNumber();
            characterNumber = state.getCharacterNumber();
            currentToken = state.getCurrentToken();
            originalState = state;
        }

        /**
         * Returns the current character being processed by the scanner (as of
         * the time of this snapshot)
         *
         * @return {@inheritDoc}
         */
        public int getCurrentChar() {
            return currentChar;
        }

        /**
         * Sets the current character being processed by the scanner; only
         * permitted if the saved scan position still matches the current
         * position of the original scan state (i.e. the scanner has not yet
         * moved on), and throws {@code IllegalStateException} otherwise
         *
         * @param  newChar {@inheritDoc}
         *
         * @throws IllegalStateException if the scanner is no longer at the
         *         position represented by this {@code ScanState}
         */
        public void setCurrentChar(int newChar) {
            if ((originalState.getCharacterNumber() == characterNumber)
                    && (originalState.getLineNumber() == lineNumber)) {
                currentChar = newChar;
                originalState.setCurrentChar(newChar);
            } else {
                throw new IllegalStateException(
                        "Saved scan state is not current");
            }
        }

        /**
         * Returns the last character processed by the scanner before the one
         * currently being considered (as of the time of this snapshot)
         *
         * @return {@inheritDoc}
         */
        public int getLastChar() {
            return lastChar;
        }

        /**
         * Returns the (1-based) number of the line being scanned (as of the
         * time of this snapshot)
         *
         * @return {@inheritDoc}
         */
        public int getLineNumber() {
            return lineNumber;
        }

        /**
         * Returns the (1-based) number of the character being scanned (as of
         * the time of this snapshot), relative to the beginning of the line
         *
         * @return {@inheritDoc}
         */
        public int getCharacterNumber() {
            return characterNumber;
        }

        /**
         * Returns a {@code String} containing the characters so far
         * accepted for the token currently being scanned (as of the time of
         * this snapshot); the "current" character has not yet been accepted
         *
         * @return {@inheritDoc}
         */
        public String getCurrentToken() {
            return currentToken;
        }
    }
}

