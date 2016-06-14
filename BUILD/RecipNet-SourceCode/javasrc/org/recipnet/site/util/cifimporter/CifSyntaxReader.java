/*
 * Reciprocal Net project
 * 
 * CifSyntaxReader.java
 *
 * 07-Jun-2002: jobollin wrote skeleton
 * 11-Jun-2002: jobollin wrote first draft
 * 20-Oct-2003: jobollin performed minor fixup related to recovery from line
 *              length errors and error reporting
 * 16-Feb-2004: jobollin tidied up read()'s state tracking
 * 16-Feb-2004: jobollin fixed bug #1119 regarding backslashes in the input
 * 09-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.cifimporter;

import java.io.FilterReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

/**
 * a {@code FilterReader} subclass that preprocesses CIF data from the
 * underlying {@code Reader} to check CIF structural and syntactic
 * constraints and render the stream more suitable for tokenization.
 * {@code CifSyntaxReader} instances can be configured to be more (the
 * default) or less strict about the syntax constraints they enforce, and to
 * indicate syntax errors either by throwing an exception or by logging a
 * message to a suitable location.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 */
public class CifSyntaxReader extends FilterReader {

    /* ASCII control codes used by CIF */
    private final static int TAB = 9;
    private final static int LF = 10;
    private final static int VT = 11;
    private final static int FF = 12;
    private final static int CR = 13;

    /* A value added to characters to escape them */
    public final static int ESCAPE_CODE = 128;

    /* Bit masks used to interpret stored state information inside read() */
    private final static int WHITESPACE_BIT = 0x1;
    private final static int LINE_TERM_BIT = 0x2;
    private final static int CR_BIT = 0x4;
    private final static int IS_ORDINARY_CHAR = 0;
    private final static int IS_WHITESPACE = WHITESPACE_BIT;
    private final static int IS_LINE_TERM = WHITESPACE_BIT | LINE_TERM_BIT;
    private final static int IS_CR = WHITESPACE_BIT | LINE_TERM_BIT | CR_BIT;

    /*
     * The maximum legal length for a CIF line, including trailing whitespace
     * except for line terminators
     */
    private final static int MAX_LINE_LENGTH = 80;

    /**
     * the character number with which {@code CifSyntaxReader}s replace
     * all non-whitespace symbols that delimit text (e.g. ", ', ;); not a
     * valid character in CIF
     */
    public final static int QUOTE_METACHAR = 17;

    /* The size of the internal read-ahead / pushback buffer */
    private final static int BUFFER_SIZE = 8;

    /* the delimiter character of the current text block */
    private int blockQuoteDelim;

    /*
     * indicates whether a semicolon-delimited text block is currently being
     * processed
     */
    private boolean blockQuoteFlag;

    /* the number of characters in the current line so far */
    private int charsInLine;

    /* indicates whether an inline comment is currently being processed */
    private boolean commentFlag;

    /**
     * a {@code List} in which to record errors
     */
    List<? super CifError> errorList;

    /**
     * a {@link java.io.PrintWriter PrintWriter} to which error conditions
     * may be reported
     */
    PrintWriter errorOutput;

    /*
     * a flag to indicate whether to filter out characters not legal in CIF
     * (instead of throwing an exception if one is encountered)
     */
    private boolean filterIllegalChars;

    /*
     * a flag to indicate whether to handle square-bracket text values; no
     * way to turn this on yet, because there is no implementation of the
     * feature
     */
    private boolean handleSBText;

    /* a flag to disable (if true) or enable (if false) line length checking */
    private boolean ignoreLineLength;

    /*
     * the character or character type read immediately before the current one
     */
    private int lastCharMask;

    /** a count of the number of lines read so far */
    protected int lineCount;

    /* the delimiter of the current line quote, if any */
    private int lineQuoteDelim;

    /*
     * a flag to indicate whether a quote-delimited inline string is currently
     * being read
     */
    private boolean lineQuoteFlag;

    /*
     * a read-ahead cache used to disambiguate some syntactic
     * constructs and to aid recovery from certain syntax errors
     */
    private int[] nextChars = new int[BUFFER_SIZE];

    /*
     * The next available position in the buffer
     */
    private int bufferPosition = 0;

    /* a flag to indicate whether to attempt to continue after an error */
    private boolean nonFatalErrors;

    /**
     * constructs a {@code CifSyntaxReader} that obtains its input from
     * another {@link java.io.Reader Reader}.  The initial configuration is
     * set for strict syntax checking and "fatal" errors (syntax errors cause
     * an exception to be thrown.
     *
     * @param r a {@code Reader} from which this
     * {@code CifSyntaxReader} will obtain its input
     */
    public CifSyntaxReader(Reader r) {
        super(r);
        blockQuoteFlag = false;
        charsInLine = 0;
        commentFlag = false;
        errorOutput = null;
        filterIllegalChars = false;
        handleSBText = false;
        ignoreLineLength = false;
        lastCharMask = IS_LINE_TERM;
        lineCount = 1;
        lineQuoteFlag = false;
        nonFatalErrors = false;
    }

    /**
     * handles an error condition by throwing an exception or logging a
     * message, depending on this instance's configuration
     *
     * @param error a {@code CifError} describing the error condition;
     * used to construct the exception or log message
     *
     * @throws IOException if {@code errorOutput}'s {@code println}
     * method does or if this {@code CifSyntaxReader} is configured to
     * throw exceptions in response to syntax errors.  In the latter case,
     * the runtime type of the exception is in fact {@link CifSyntaxException
     * CifSyntaxException}, an IOException subclass.
     */
    protected void errorDetected(CifError error) throws IOException {
        if (error == null) {
            return;
        } else if (nonFatalErrors) {
            if (errorList != null) {
                errorList.add(error);
            }
            if (errorOutput != null) {
                errorOutput.println(error.message());
            }
        } else {
            throw new CifSyntaxException(error);
        }
    }

    /**
     * returns whether this {@code CifSyntaxReader} is filtering out
     * illegal characters or considering them syntax errors
     *
     * @return true if illegal characters are (silently) filtered out; false
     * if they constitute syntax errors
     */
    public boolean filteringIllegalChars() {
        return filterIllegalChars;
    }

    /**
     * returns the number of lines and partial lines processed by this
     * {@code CifSyntaxReader} so far
     *
     * @return the number of lines
     */
    public int getLineCount() {
        return lineCount;
    }

    /**
     * returns the current position in the line at which this reader is
     * reading
     *
     * @return the current position in the current line
     */
    public int getLinePosition() {
        return charsInLine;
    }

    /**
     * Encapsulates the operation of obtaining the next character to process,
     * which may in fact come either from the underlying stream or from
     * internal cache; the character returned is the one at the <em>largest</em>
     * used index in the buffer
     *
     * @return the next character to process
     *
     * @throws IOException if a read of the underlying stream results in such
     *         an exception
     */
    private int getNextChar() throws IOException {
        return ((bufferPosition > 0) ? nextChars[--bufferPosition]
                                     : super.read());
    }

    /**
     * Inserts the specified character as the next to be read from the internal
     * buffer, pushing back any other characters already present
     *
     * @param  c the character to buffer
     *
     * @throws IllegalStateException if the buffer is full
     */
    private void pushChar(int c) {

        /*
         * Note: the buffer is read from highest occupied position to lowest
         */
        if (bufferPosition >= BUFFER_SIZE) {
            throw new IllegalStateException("Internal buffer is full");
        } else {
            nextChars[bufferPosition++] = c;
        }
     }

   /**
     * returns whether this {@code CifSyntaxReader} is configured to
     * process square-bracketed text values
     *
     * @return true if this {@code CifSyntaxReader} will process
     * square-bracketed text values as quoted text; false (the default)
     * otherwise
     */
    public boolean handlingSBText() {
        /*
         * implementation note: There is not yet a way to turn this option
         * on, nor is there support in read()
         */
        return handleSBText;
    }

    /**
     * returns whether this {@code CifSyntaxReader} will allow arbitrary
     * length input lines or will consider a lines extending beyond 80
     * characters to constitute syntax errors
     *
     * @return false (the default) if long lines generate syntax errors; true
     * otherwise
     */
    public boolean ignoringLineLength() {
        return ignoreLineLength;
    }

    /**
     * overrides mark() in class java.io.FilterReader. mark functionality is
     * not supported
     *
     * @param  readAheadLimit a limit on the number of characters that may be
     *         read while preserving the mark
     *
     * @throws IOException every time this method is invoked, because this
     *         class does not support the mark() operation.
     */
    public void mark (@SuppressWarnings("unused") int readAheadLimit)
            throws IOException {
        throw new IOException("mark() not supported by CifSyntaxReader");
    }

    /**
     * indicates whether the {@code mark()} operation is supported by
     * this {@code Reader}, which it is not.
     *
     * @return always returns {@code false}
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * Determines whether the specified character is a delimiter for inline
     * quotations
     *
     * @param  c the character to test
     *
     * @return {@code true} if {@code c} is an inline quotation
     *         delimiter; {@code false} otherwise
     */
    protected static boolean isBlockQuoteDelim(int c) {
        return (c == ';');
    }

    /**
     * Determines whether the specified character is a delimiter for inline
     * quotations
     *
     * @param  c the character to test
     *
     * @return {@code true} if {@code c} is an inline quotation
     *         delimiter; {@code false} otherwise
     */
    protected static boolean isLineQuoteDelim(int c) {
        return ((c == '\'') || (c == '"'));
    }

    /**
     * Determines whether the specified character is a legal non-control
     * character in CIF
     *
     * @param  c the character to test
     *
     * @return {@code true} if {@code c} is a valid CIF character
     *         not among the accepted control characters; {@code false}
     *         otherwise
     */
    protected static boolean isValidChar(int c) {
        return ((c >= 0x20) && (c <= 0x7e));
    }

    /**
     * <p>
     * returns one character from the underlying {@code Reader}.  This
     * method filters the input data by detecting and removing comments,
     * detecting quoted text and converting all text quotation delimiters to
     * {@code QUOTE_METACHAR}, converting all unquoted line breaks
     * to blanks, and disguising block quoted line breaks and backslashes by
     * adding 128 to their character numbers.  These manipulations take into
     * account the various context dependencies of the different commenting and
     * quoting mechanisms, and account for the unusual line-termination property
     * of form feed characters in CIF.  This method also detects various syntax
     * errors, in particular conditionally and unconditionally illegal
     * characters, illegally long lines, and unterminated quoted text strings
     * (both inline and block); its behavior upon detecting such errors depends
     * on this {@code CifSyntaxReader}'s configuration.
     * </p><p>
     * Because of this method's comment stripping and optional illegal
     * character filtering, its invocation may cause more than one character
     * to be read from the underlying stream.
     * </p>
     *
     * @return the next character in the view of the input provided by this
     * {@code CifSyntaxReader}, or -1 if the end of the stream has been
     * reached
     *
     * @throws IOException if {@code FilterReader.read()} throws this
     * exception, or, depending on this {@code CifSyntaxReader}'s
     * configuration, if a syntax error is detected.  In the latter case the
     * actual runtime class of the exception is a subclass of IOException,
     * CifSyntaxException.
     *
     * @see #read(char[], int, int)
     * @see #read(char[])
     */
    public int read() throws IOException {
        int c = getNextChar();
        int thisCharMask = IS_ORDINARY_CHAR;

        /*
         * tc is normally a copy of c, but if any translation is applied it
         * is done to tc instead of to c; thus c is always the character
         * read, but tc is the character used internally.
         */
        int tc = c;

        if ( (c == ' ') || (c == TAB) || (c == VT) ) {
            thisCharMask = IS_WHITESPACE;
        } else if ((c == LF) || (c == -1)) {
            thisCharMask = IS_LINE_TERM;
        } else if (c == CR) {
            thisCharMask = IS_CR;
        } else if (c == FF) {
            thisCharMask = IS_LINE_TERM;
            tc = CR;
        }

        /*
         * Handle line termination, including EOF
         */
        if ((thisCharMask & LINE_TERM_BIT) != 0) {

            /*
             * Operations and tests that could result in a CifSyntaxException
             * at EOL / EOF should go in this try block.
             */
            try {

                /*
                 * lines shouldn't be longer than 80 characters; this is only
                 * checked once at EOL/EOF. Extra characters are passed through
                 * regardless of the value of ignoreLineLength, however.
                 */
                if ((charsInLine > MAX_LINE_LENGTH) && ( !ignoreLineLength ) ) {

                    /* current line length is zeroed in the finally clause */

                    errorDetected(
                            new CifError(CifError.LINE_LENGTH, lineCount));
                }

                /* handle unterminated quoted text */
                if (lineQuoteFlag) {
                    pushChar(c);
                    pushChar(lineQuoteDelim);
                    errorDetected(
                        new CifError(CifError.UNTERMINATED_QSTRING,
                                     lineCount - 1) );
                } else if (blockQuoteFlag) {
    
                    /* EOL is valid inside a block quote, but EOF isn't */
                    if (c == -1) {
                        pushChar(c);
                        pushChar(';');
                        pushChar(CR);
                        errorDetected(
                            new CifError(CifError.UNTERMINATED_TEXT,
                                         lineCount - 1) );
    
                    /*
                     * Block quoted EOL is disguised so as to not interfere with
                     * tokenization.
                     */
                    } else {
                        tc += ESCAPE_CODE;
                    }

                /* Unquoted EOL is translated to a space */
                } else if (c != -1) {
                    tc = ' ';
                }
            } finally {

                /* reset the number of characters in the current line */
                charsInLine = 0;

                /*
                 * count lines, collapsing CR LF into a single line terminator
                 */
                if (((lastCharMask & CR_BIT) != 0) || (c != LF)) {
                    lineCount++;
                }

                /* comments end at EOL (or EOF) */
                commentFlag = false;
            }

            /* compensate for increment later on */
            charsInLine--;

        /*
         * Handle illegal (in CIF / STAR) characters.
         * Other than the control characters handled above, CIF allows only
         * the characters from the printable 7-bit ASCII set
         */
        } else if ( (thisCharMask == 0) && !isValidChar(tc) ) {
            charsInLine++;
            lastCharMask = IS_ORDINARY_CHAR;  // for successful continuation
            if (!filterIllegalChars) {
                errorDetected(
                    new CifError(CifError.ILLEGAL_CHAR, lineCount, "\\" + tc)
                );
            }
            return read();

        /*
         * Remove comments
         */
        } else if (commentFlag) {
            charsInLine++;
            lastCharMask = IS_ORDINARY_CHAR;
            return read();
        } else if ( (tc == '#')
                    && ((lastCharMask & WHITESPACE_BIT) != 0)
                    && !lineQuoteFlag
                    && !blockQuoteFlag ) {
            commentFlag = true;
            charsInLine++;
            lastCharMask = IS_ORDINARY_CHAR;
            return read();

        /*
         * Handle semicolon-delimited text blocks.  The semicolon must follow
         * a line terminator to be a delimiter.
         */
        } else if ( isBlockQuoteDelim(tc) && (charsInLine == 0) ) {
            if (!blockQuoteFlag) {
                blockQuoteFlag = true;
                blockQuoteDelim = tc;
                tc = QUOTE_METACHAR;
            } else if (blockQuoteDelim == tc) {
                blockQuoteFlag = false;
                tc = QUOTE_METACHAR;
            }

        /*
         * Handle single and double quote delimited strings
         */
        } else if ( isLineQuoteDelim(tc) && !blockQuoteFlag ) {

            /*
             * If we are processing an inline quoted string, then we need to
             * check whether this is the correct closing delimiter.
             */
            if (lineQuoteFlag) {
                if (tc == lineQuoteDelim) {
                    int d = getNextChar();

                    pushChar(d);
                    if ((d == ' ') || (d == -1) || ((d <= CR) && (d >= TAB))) {
                        lineQuoteFlag = false;
                        tc = QUOTE_METACHAR;
                    }
                }

            /*
             * If we're not already processing a quoted string, then this
             * character starts one only after a blank or a line terminator.
             */
            } else if ((lastCharMask & WHITESPACE_BIT) != 0) {
                lineQuoteFlag = true;
                lineQuoteDelim = tc;
                tc = QUOTE_METACHAR;
            }

        /*
         * Inside a quoted string, the backslash character is escaped to avoid
         * its interpretation is the introduction of a character escape
         */
        } else if ( (tc == '\\') && (lineQuoteFlag || blockQuoteFlag) ) {
            tc += ESCAPE_CODE;

        /*
         * Unquoted text cannot begin with a '$', to maintain compatability
         * with STAR.  This CifSyntaxReader will emit an error message or
         * throw an exception if a violation is detected.  The '$' will
         * always be filtered out (for consistent behavior regardless of
         * the setting of the fatal errors flag).
         */
        } else if ( (tc == '$') && !lineQuoteFlag && !blockQuoteFlag
                    && ((lastCharMask & WHITESPACE_BIT) != 0) ) {
            charsInLine++;
            lastCharMask = IS_ORDINARY_CHAR;
            errorDetected(
                new CifError(CifError.BEGINS_WITH_DOLLAR, lineCount)
            );
            return read();
        }

        /*
         * If we get here then increment the character counter, set lastCharMask
         * equal to thisCharMask, and return the translated character.
         */
        charsInLine++;
        lastCharMask = thisCharMask;

        return tc;
    }

    /**
     * reads up to {@code len} characters into the supplied buffer
     * {@code cbuf}, starting at position {@code off}.  This method
     * uses {@code read()} to obtain its characters.
     *
     * @param cbuf a {@code char[]} buffer in which the characters should
     *             be stored
     * @param off  the position in {@code cbuf} at which to start storing
     *             characters
     * @param len  the maximum number of characters to read
     *
     * @return the number of characters stored into {@code cbuf}, or -1
     *         if the end of the stream has been reached
     *
     * @throws IOException if {@code read()} throws that exception
     *
     * @see #read()
     * @see #read(char[])
     */
    public int read(char[] cbuf, int off, int len) throws IOException {
        int i;

        for (i = off; i < off + len; i++) {
            int c = read();

            if (c < 0) {
                return ((i - off) - 1);
            } else {
                cbuf[i] = (char) c;
            }
        }
        return len;
    }

    /**
     * reads characters into the {@code cbuf}, up to the length of
     * {@code cbuf}.  This method is shorthand for
     * {@code read(cbuf, 0, cbuf.length)}.
     *
     * @param cbuf a {@code char[]} buffer in which the characters should
     *             be stored
     *
     * @return the number of characters stored into {@code cbuf}, or -1
     *         if the end of the stream has been reached
     *
     * @throws IOException if {@code read()} throws that exception
     *
     * @see #read()
     * @see #read(char[], int, int)
     */
    public int read(char[] cbuf) throws IOException {
        return read(cbuf, 0, cbuf.length);
    }

    /**
     * resets the stream.  Not supported on this Reader, and therefore throws
     * an IOException.
     *
     * @throws IOException whenever this method is invoked
     */
    public void reset() throws IOException {
        throw new IOException(
                "reset() not supported on CifSyntaxReader objects");
    }

    /**
     * sets a {@code List} into which future syntax errors should be
     * placed
     *
     * @param  l a {@code List} into which to place {@code CifError}
     *         objects representing syntax errors detected
     */
    public void setErrorList(List<? super CifError> l) {
        errorList = l;
    }

    /**
     * sets whether or not this Reader should filter out illegal characters
     * (all characters other than ASCII 9-13 and 32-126 decimal).  If not
     * filtering these out then this {@code read()} will throw an
     * {@code IOException} (actually a <code CifSyntaxException}
     * upon encountering one of them; whether filtering them or not they count
     * against line lengths.
     *
     * @param b true to configure this {@code CifSyntaxReader} to
     *          silently filter illegal characters; false to configure it to
     *          noisily reject them
     */
    public void setFilterIllegalChars(boolean b) {
        filterIllegalChars = b;
    }

    /**
     * sets whether or not this {@code CifSyntaxReader} should ignore CIF
     * line length restrictions.  If the restrictions are heeded then
     * {@code read()} will throw an {@code IOException} (more
     * specifically a {@code CifSyntaxException}) upon encountering a
     * line with more than 80 characters, and the extra characters will not
     * be processed; otherwise {@code read()} will treat the
     * 81<sup>st</sup> and following characters on each line the same as the
     * earlier ones.  Line termination characters are not in any case counted
     * toward the 80 character limit.
     *
     * @param b true to accept long lines; false to adhere strictly to the CIF
     *          specification
     */
    public void setIgnoreLineLength(boolean b) {
        ignoreLineLength = b;
    }

    /**
     * sets a PrintWriter to which messages should be logged.  In the current
     * implementation there are no messages other than those indicating syntax
     * errors.
     *
     * @param pw a {@link java.io.PrintWriter PrintWriter} to be used to log
     *           messages generated by this {@code CifSyntaxReader}.  If
     *           this is {@code null} then message logging is disabled.
     */
    public void setLog(PrintWriter pw) {
        errorOutput = pw;
    }

    /**
     * sets whether or not CIF syntax errors detected in this
     * {@code CifSyntaxReader} should be fatal.  "Fatal" means they cause
     * the {@code read()} method to throw an IOException (more
     * specifically, a {@code CifSyntaxException}).  Non-fatal means they
     * cause a message to be logged (if a logging {@code PrintWriter}
     * has been set) and this {@code CifSyntaxReader} will attempt to
     * continue.  Even if this {@code CifSyntaxReader} throws an exception
     * in response to a syntax error, it may still be possible to resume
     * reading the input stream by invoking one of the {@code read}
     * methods again.
     * <p>
     * The {@code nonFatalErrors} setting controlled by this method
     * interacts with the {@code filterIllegalChars} setting in a perhaps
     * non-intuitive way.  If {@code nonFatalErrors} is set
     * {@code true} then illegal characters are <em>always</em> filtered;
     * in that case the {@code filterIllegalCharacters} setting simply
     * controls whether encountering an illegal character is treated as an
     * error (and thus logged) or not.
     * <p>
     * The {@code ignoreLineLength} setting interacts with the
     * {@code nonFatalErrors} setting similarly, but that is somewhat
     * more intuitive.
     *
     * @param b {@code false} if syntax errors should cause the
     *          {@code read} methods to throw exceptions;
     *          {@code true} otherwise
     */
    public void setNonFatalErrors(boolean b) {
        nonFatalErrors = b;
    }

    /*
     * Inherits close(), ready(), and skip() from FilterReader
     */
}

