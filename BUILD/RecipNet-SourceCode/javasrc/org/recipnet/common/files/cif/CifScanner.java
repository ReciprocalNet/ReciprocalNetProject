/*
 * Reciprocal Net Project
 *
 * CifScanner.java
 *
 * 24-Jan-2005: jobollin wrote first draft
 * 06-Jun-2006: jobollin added warning-suppression annotations
 */

package org.recipnet.common.files.cif;

import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.EnumSet;

/**
 * <p>
 * A class representing a lexical scanner for the Crystallographic Information
 * File (CIF) format.  Detailed Information about CIF, including an annotated
 * formal grammar, can be found at <a
 * href="http://www.iucr.org/iucr-top/cif/spec/">the IUCr's CIF specification
 * pages</a>.  The default rules for scanners of this class are appropriate for
 * use with version 1.1 of CIF (released late 2004).  Scanner instances are
 * configurable, however, with built-in rulesets for both CIF 1.0 and CIF 1.1,
 * and with methods for tweaking the rules to account for (some) variants.
 * </p><p>
 * This scanner is designed along lines similar to the SAX API.  Scanner
 * clients register zero or more "handlers" for lexical events of several kinds,
 * providing callback methods by which the scanner notifies its client about
 * the details of the input as it scans it.  This general idea was described by
 * Peter Murray-Rust at the CrystalGrid Collaboratory Foundation Workshop in
 * September, 2004.  Murray-Rust has an implementation, but this scanner is
 * completely separate from Murray-Rust's design and implementation, produced
 * without reference to it or any detailed knowledge of it.
 * </p><p>
 * The use of error handlers in particular permits the scanner client to
 * specify an error-handling policy, in that the error handler can (indeed must)
 * decide for each error detected whether or not to stop the scan.  If it
 * elects to <em>not</em> stop the scan then it has the opportunity to change
 * the current character under consideration; this option should only be
 * exercised for illegal input characters.  This scanner exhibits the following
 * error recovery behaviors:
 * <dl>
 * <dt>illegal character</dt>
 * <dd>attempts to continue to scan, which may cause additional errors to be
 * signaled.  The likelihood of cascading <em>lexical</em> errors depends on
 * whether or not the current character was corrected by the error handler, and
 * if so then on the corrected char.  The likelihood of related parsing errors
 * depends on the correction, if any, and on the character's context.
 * </dd>
 * <dt>illegal character for start of an unquoted token<dt>
 * <dd>attempts to scan anyway, choosing whether to scan as whitespace or as
 *     an unquoted token depending on the character or replacement character.
 *     This error is likely after an illegal input character that is not
 *     suitably corrected by the error handler
 * </dd>
 * <dt>unterminated inline quote</dt>
 * <dd>acts as if the correct closing delimiter had appeared at the end of the
 *     line
 * </dd>
 * <dt>unterminated text block</dt>
 * <dd>acts as if the correct closing delimiter had appeared at the end of the
 *     file
 * </dd>
 * <dt>line too long</dt>
 * <dd>ignores the length violation and continues to scan as if there were no
 *     line length limit
 * </dd>
 * <dt>data name, save frame name, or data block name too long</dt>
 * <dd>ignores the length violation and provides the whole token to the token
 *     handler</dd>
 * <dt>unquoted reserved word ("data_", "stop_", or "global_")</dt>
 * <dd>ignores the token</dd>
 * <dt>missing whitespace between tokens (where recognized)</dt>
 * <dd>continues the scan as if the required whitespace had been present (but
 *     does not notify the configured whitespace handler)
 * </dd>
 * </dl>
 * Other types of errors are left to the parser to diagnose.
 * </p><p>
 * This scanner does not modify its input.  In particular, it leaves it to the
 * discretion of the token handler whether to interpret CIF escape codes, and
 * although it recognizes <CR>, <LF>, and <CR><LF> as line terminators (and
 * also <FF> under CIF 1.0 rules), it does not normalize them.  It also does not
 * introduce synthetic line termination characters at end of file.
 * </p><p>
 * Scanner instances may be reused for additional input files, in one or many
 * threads, but they have mutable configuration including exactly one set of
 * event handlers, so they are not safe for concurrent use by multiple threads.
 * </p>
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifScanner {

    /*
     *  ASCII control codes used by CIF
     */
    
    /** ASCII / UTF-8 code for horizontal tab */
    private final static int TAB = 9;
    
    /** ASCII / UTF-8 code for linefeed (newline) */
    private final static int LF = 10;
    
    /** ASCII / UTF-8 code for vertical tab */
    private final static int VT = 11;
    
    /** ASCII / UTF-8 code for form feed */
    private final static int FF = 12;
    
    /** ASCII / UTF-8 code for carriage return */
    private final static int CR = 13;

    /** ASCII / UTF-8 code for a control-D character, the UNIX EOF marker */
    private final static int CNTL_D = 4;
    
    /** ASCII / UTF-8 code for a control-Z character, the DOS EOF marker */
    private final static int CNTL_Z = 26;
    
    /*
     * Ruleset parameters for CIF 1.0
     */
     
    /** The maximum line length permitted by CIF 1.0 */
    public final static int CIF_1_0_MAX_LINE_LENGTH = 80;
    
    /** The maximum block name length permitted by CIF 1.0 */
    public final static int CIF_1_0_MAX_BLOCK_NAME_LENGTH = 32;
    
    /** The maximum save frame name length permitted by CIF 1.0 */
    public final static int CIF_1_0_MAX_FRAME_NAME_LENGTH = Integer.MAX_VALUE;
    
    /** The maximum data name length permitted by CIF 1.0 */
    public final static int CIF_1_0_MAX_DATA_NAME_LENGTH = Integer.MAX_VALUE;
    
    /*
     * Ruleset parameters for CIF 1.1
     */
     
    /** The maximum line length permitted by CIF 1.1 */
    public final static int CIF_1_1_MAX_LINE_LENGTH = 2048;
    
    /** The maximum block name length permitted by CIF 1.1 */
    public final static int CIF_1_1_MAX_BLOCK_NAME_LENGTH = 75;
    
    /** The maximum data name length permitted by CIF 1.1 */
    public final static int CIF_1_1_MAX_FRAME_NAME_LENGTH = 75;
    
    /** The maximum save frame name length permitted by CIF 1.1 */
    public final static int CIF_1_1_MAX_DATA_NAME_LENGTH = 75;

    /*
     * character attribute codes
     */
    enum CharacterAttribute {
    
        /** a legal character */
        LEGAL,
        
        /** a whitespace character */
        WHITESPACE,
        
        /** a line terminator character */
        LINE_TERMINATOR,
        
        /** an inline quote delimiter */
        INLINE_QUOTE,
        
        /** a block quote delimiter */
        BLOCK_QUOTE,
        
        /** a character that flags the beginning of a comment */
        COMMENT_BEGIN,
        
        /** a character that may begin a name */
        NAME_BEGIN,
        
        /** a character that may begin a value */
        VALUE_BEGIN,
        
        /** an end of file mark character */
        EOF
    }
    
    /** size of the default character attribute table*/
    private final static int ATTR_TABLE_SIZE = 127;

    /*
     * Note: character 0x7f is a control character, not a printable one
     */
    
    /**
     * The Unicode code point of the space character, which is the character
     * with the numerically least code point among printable characters 
     */
    private final static int MIN_PRINTABLE_CHARACTER = 32;
    
    /**
     * the default character attribute table, conforming to the rules for
     * CIF 1.1
     */
    private final static EnumSet<CharacterAttribute>[] CIF_1_1_CHAR_ATTRIBUTES;

    /* attribute table initialization */
    static {
        // This assignment produces a type safety warning, but it's OK:
        CIF_1_1_CHAR_ATTRIBUTES = new EnumSet[ATTR_TABLE_SIZE];
        
        for (int c = 0; c < MIN_PRINTABLE_CHARACTER; c++) {
            CIF_1_1_CHAR_ATTRIBUTES[c] =
                    EnumSet.noneOf(CharacterAttribute.class);
        }

        // Accepted control characters:
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[TAB],
                CharacterAttribute.LEGAL, CharacterAttribute.WHITESPACE);
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[LF],
                CharacterAttribute.LEGAL, CharacterAttribute.WHITESPACE,
                CharacterAttribute.LINE_TERMINATOR);
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[CR],
                CharacterAttribute.LEGAL, CharacterAttribute.WHITESPACE,
                CharacterAttribute.LINE_TERMINATOR);
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[CNTL_D],
                CharacterAttribute.LEGAL, CharacterAttribute.WHITESPACE,
                CharacterAttribute.EOF);
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[CNTL_Z],
                CharacterAttribute.LEGAL, CharacterAttribute.WHITESPACE,
                CharacterAttribute.EOF);

        // Rejected control characters (for error recovery in CIF 1.1 mode): 
        CIF_1_1_CHAR_ATTRIBUTES[VT].add(CharacterAttribute.WHITESPACE);
        Collections.addAll(CIF_1_1_CHAR_ATTRIBUTES[FF],
                CharacterAttribute.WHITESPACE,
                CharacterAttribute.LINE_TERMINATOR);
        
        // other legal characters; initially all are assigned the VALUE_BEGIN
        // attribute, but this will be turned back off for some characters
        for (int i = MIN_PRINTABLE_CHARACTER; i < ATTR_TABLE_SIZE; i++) {
            CIF_1_1_CHAR_ATTRIBUTES[i] = EnumSet.of(
                    CharacterAttribute.LEGAL,
                    CharacterAttribute.VALUE_BEGIN);
        }

        // The "plain" space character
        CIF_1_1_CHAR_ATTRIBUTES[' '].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES[' '].add(CharacterAttribute.WHITESPACE);

        // The semicolon is special because whether or not it serves as a
        // quoting character depends on its position on the line
        CIF_1_1_CHAR_ATTRIBUTES[';'].add(CharacterAttribute.BLOCK_QUOTE);

        // "Inline" quotation chars
        CIF_1_1_CHAR_ATTRIBUTES['"'].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['"'].add(CharacterAttribute.INLINE_QUOTE);
        CIF_1_1_CHAR_ATTRIBUTES['\''].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['\''].add(CharacterAttribute.INLINE_QUOTE);

        // Comment and data name initial characters:
        CIF_1_1_CHAR_ATTRIBUTES['#'].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['#'].add(CharacterAttribute.COMMENT_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['_'].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['_'].add(CharacterAttribute.NAME_BEGIN);

        // Other legal characters not permitted to start unquoted data values
        CIF_1_1_CHAR_ATTRIBUTES['$'].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES['['].remove(CharacterAttribute.VALUE_BEGIN);
        CIF_1_1_CHAR_ATTRIBUTES[']'].remove(CharacterAttribute.VALUE_BEGIN);
    }

    /**
     * an instance of a CifTokenHandler implementation that ignores every event;
     * used as a default
     */
    private final static CifTokenHandler DEFAULT_TOKEN_HANDLER =
        new CifTokenHandler() {
            public void handleBlockHeader(
                    @SuppressWarnings("unused") String blockName,
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleSaveFrameHeader(
                    @SuppressWarnings("unused") String frameName,
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleSaveFrameEnd(
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleLoopStart(
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleDataName(
                    @SuppressWarnings("unused") String name,
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleQuotedValue(
                    @SuppressWarnings("unused") String value,
                    @SuppressWarnings("unused") char delim,
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleUnquotedValue(
                    @SuppressWarnings("unused") String value,
                    @SuppressWarnings("unused") ScanState state) {}
        };

    /**
     * an instance of a CifWhitespaceHandler implementation that ignores every
     * event; used as a default
     */
    private final static CifWhitespaceHandler DEFAULT_WHITESPACE_HANDLER =
        new CifWhitespaceHandler() {
            public void handleWhitespace(@SuppressWarnings("unused") String s,
                    @SuppressWarnings("unused") ScanState state) {}
            public void handleComment(@SuppressWarnings("unused") String s,
                    @SuppressWarnings("unused") ScanState state) {}
        };

    /**
     * an instance of a CifErrorHandler implementation that ignores all errors;
     * used as a default
     */
    private final static CifErrorHandler DEFAULT_ERROR_HANDLER =
        new CifErrorIgnorer();

    /** The character attribute table used by this scanner instance */
    private EnumSet<CharacterAttribute>[] charAttributes;

    /** The line length limit used by this scanner instance */
    int lineLimit;

    /** The data block name length limit used by this scanner instance */
    private int blockNameLimit;

    /** The save frame name length limit used by this scanner instance */
    private int frameNameLimit;

    /** The data name length limit used by this scanner instance */
    private int dataNameLimit;

    /** The token handler for this scanner instance */
    private CifTokenHandler tokenHandler;

    /** The whitespace handler for this scanner instance */
    private CifWhitespaceHandler wsHandler;

    /** The error handler for this scanner instance */
    CifErrorHandler errorHandler;

    /**
     * Initializes a new CifScanner.  By default the new scanner instance will
     * apply the CIF 1.1 rules, and will employ token, whitespace, and error
     * handlers that ignore every event.
     */
    @SuppressWarnings("unchecked")
    public CifScanner() {
        charAttributes = new EnumSet[CIF_1_1_CHAR_ATTRIBUTES.length];
        for (int i = 0; i < CIF_1_1_CHAR_ATTRIBUTES.length; i++) {
            charAttributes[i] = EnumSet.copyOf(CIF_1_1_CHAR_ATTRIBUTES[i]);
        }
        lineLimit = CIF_1_1_MAX_LINE_LENGTH;
        blockNameLimit = CIF_1_1_MAX_BLOCK_NAME_LENGTH;
        frameNameLimit = CIF_1_1_MAX_FRAME_NAME_LENGTH;
        dataNameLimit = CIF_1_1_MAX_DATA_NAME_LENGTH;
        tokenHandler = DEFAULT_TOKEN_HANDLER;
        wsHandler = DEFAULT_WHITESPACE_HANDLER;
        errorHandler = DEFAULT_ERROR_HANDLER;
    }

    /**
     * Sets the token handler to be used by this scanner.  Most clients will
     * want to set a non-default handler so as to be able to parse the token
     * stream
     *
     * @param  handler the {@code CifTokenHandler} that should be advised
     *         of tokens scanned by this scanner; {@code null} for the
     *         default handler (that silently ignores all tokens)
     */
    public void setTokenHandler(CifTokenHandler handler) {
        tokenHandler = ((handler == null) ? DEFAULT_TOKEN_HANDLER : handler);
    }

    /**
     * Sets the error handler to be used by this scanner.  Clients should set
     * a non-default handler if they want to be notified about lexical errors
     * in the input, and / or if they want to be able to interrupt the scan
     * when one occurs
     *
     * @param  handler the {@code CifErrorHandler} that should be advised
     *         of errors detected by this scanner; {@code null} for the
     *         default handler (that silently absorbs all errors)
     */
    public void setErrorHandler(CifErrorHandler handler) {
        errorHandler = ((handler == null) ? DEFAULT_ERROR_HANDLER : handler);
    }

    /**
     * Sets the whitespace handler to be used by this scanner.  Clients should
     * set a non-default handler if they want to collect or analyze comments in
     * the CIF (such as the structured version compliance comment defined by
     * CIF 1.1) or if they want to be able to create an exact replica of the
     * input (without losing comments and spacing)
     *
     * @param  handler the {@code CifWhitespaceHandler} that should be
     *         advised of comments and whitespace scanned by this scanner;
     *         {@code null} for the default handler (that silently ignores
     *         all whitespace and comments)
     */
    public void setWhitespaceHandler(CifWhitespaceHandler handler) {
        wsHandler = ((handler == null) ? DEFAULT_WHITESPACE_HANDLER : handler);
    }

    /**
     * Sets the number of consecutive non-line-terminators that can be read by
     * this scanner before it signals an error via the configured error handler.
     * A line length error is signaled at most once per line, and the configured
     * error handler determines whether to interrupt the scan (by throwing an
     * exception) or to continue despite the problem
     *
     * @param  max the legal maximum number of characters on a line of input;
     *         less than or equal to zero for no limit
     */
    public void setMaximumLineLength(int max) {
        lineLimit = max;
    }

    /**
     * Sets the maximum legal length of data block names read by this scanner.
     * If a longer data block name is encountered then this scanner will signal
     * an error via the configured error handler, in which case the configured
     * error handler determines whether to interrupt the scan (by throwing an
     * exception) or to continue despite the problem (accepting the full data
     * block name)
     *
     * @param  max the maximum legal number of characters in a data block name
     */
    public void setMaximumBlockNameLength(int max) {
        blockNameLimit = max;
    }

    /**
     * Sets the maximum legal length of save frame names read by this scanner.
     * If a longer save frame name is encountered then this scanner will signal
     * an error via the configured error handler, in which case the configured
     * error handler determines whether to interrupt the scan (by throwing an
     * exception) or to continue despite the problem (accepting the full save
     * frame name)
     *
     * @param  max the maximum legal number of characters in a save frame name
     */
    public void setMaximumFrameNameLength(int max) {
        frameNameLimit = max;
    }

    /**
     * Sets the maximum legal length of data names read by this scanner.
     * If a longer data name is encountered then this scanner will signal
     * an error via the configured error handler, in which case the configured
     * error handler determines whether to interrupt the scan (by throwing an
     * exception) or to continue despite the problem (accepting the full data
     * name)
     *
     * @param  max the maximum legal number of characters in a data name
     */
    public void setMaximumDataNameLength(int max) {
        dataNameLimit = max;
    }

    /**
     * Sets whether or not opening and closing square brackets are "special" to
     * this scanner in the sense specified by CIF 1.1 (but not CIF 1.0), which
     * reserves their use as the initial character of an unquoted data value.
     * (A future CIF revision may employ them as a new type of quoted value
     * delimeter.)  When square brackets are special, their appearance as the
     * initial character of an unquoted data value will cause an error to be
     * signaled to the configured error handler, which determines whether to
     * interrupt the scan (by throwing an exception) or to continue despite the
     * problem (accepting the data value)
     *
     * @param  special {@code true} if square brackets should be considered
     *         special, {@code false} if they should not
     */
    public void setSquareBracketSpecial(boolean special) {
        EnumSet<CharacterAttribute> beginAttribute =
                EnumSet.of(CharacterAttribute.VALUE_BEGIN);
        
        if (special) {
            clearAttributes(beginAttribute, '[');
            clearAttributes(beginAttribute, ']');
        } else {
            setAttributes(beginAttribute, '[');
            setAttributes(beginAttribute, ']');
        }
    }

    /**
     * Sets whether or not the vertical tab character, ASCII 11 (decimal), is
     * permitted as whitespace by this scanner (per CIF 1.0) or is an illegal
     * character for this scanner (per CIF 1.1)
     *
     * @param  allowed {@code true} if vertical tab characters should be
     *         legal for this scanner, {@code false} if not
     */
    public void setVTAllowed(boolean allowed) {
        if (allowed) {
            setAttributes(EnumSet.of(CharacterAttribute.LEGAL), VT);
        } else {
            clearAttributes(EnumSet.of(CharacterAttribute.LEGAL), VT);
        }
    }

    /**
     * Sets whether or not the form feed character, ASCII 12 (decimal), is
     * permitted as a line termination character by this scanner (per CIF 1.0)
     * or is an illegal character for this scanner (per CIF 1.1)
     *
     * @param  allowed {@code true} if form feed characters should be
     *         legal for this scanner, {@code false} if not
     */
    public void setFFAllowed(boolean allowed) {
        if (allowed) {
            setAttributes(EnumSet.of(CharacterAttribute.LEGAL), FF);
        } else {
            clearAttributes(EnumSet.of(CharacterAttribute.LEGAL), FF);
        }
    }

    /**
     * Configures this scanner to use scanning and tokenizing rules consistent
     * with the CIF specification version 1.0.  These rules differ from the 1.1
     * rules in several, mostly minor ways:
     * <ul>
     *   <li>The maximum legal line length is 80 characters (as opposed to
     *       2048)</li>
     *   <li>The maximum legal data block name length is 32 characters (as
     *       opposed to 75)</li>
     *   <li>There is no specified maximum save frame name length (per STAR)
     *       because save frames are not permitted by CIF 1.0</li>
     *   <li>There is no specified maximum data name length, though data name
     *       length is effectively limited by the maximum line length</li>
     *   <li>Square brackets are permitted as the first character of an unquoted
     *       data value</li>
     *   <li>vertical tab characters are permitted and considered
     *       whitespace</li>
     *   <li>form feed characters are permitted and considered line
     *       terminators</li>
     * </ul>
     */
    public void useCif10Rules() {
        setMaximumLineLength(CIF_1_0_MAX_LINE_LENGTH);
        setMaximumBlockNameLength(CIF_1_0_MAX_BLOCK_NAME_LENGTH);
        setMaximumFrameNameLength(CIF_1_0_MAX_FRAME_NAME_LENGTH);
        setMaximumDataNameLength(CIF_1_0_MAX_DATA_NAME_LENGTH);
        setSquareBracketSpecial(false);
        setVTAllowed(true);
        setFFAllowed(true);
    }

    /**
     * Configures this scanner to use scanning and tokenizing rules consistent
     * with the CIF specification version 1.1.  These rules differ from the 1.0
     * rules in several, mostly minor ways:
     * <ul>
     *   <li>The maximum legal line length is 2048 characters (as opposed to
     *       80)</li>
     *   <li>The maximum legal data block name length is 75 characters (as
     *       opposed to 32)</li>
     *   <li>Save frames are permitted, and the maximum legal length of their
     *       names is 75 characters</li>
     *   <li>The maximum legal data name length is 75 characters (as opposed to
     *       no specified limit)</li>
     *   <li>Square brackets are not permitted as the first character of an
     *       unquoted data value</li>
     *   <li>vertical tab characters are not permitted</li>
     *   <li>form feed characters are not permitted</li>
     * </ul>
     */
    public void useCif11Rules() {
        setMaximumLineLength(CIF_1_1_MAX_LINE_LENGTH);
        setMaximumBlockNameLength(CIF_1_1_MAX_BLOCK_NAME_LENGTH);
        setMaximumFrameNameLength(CIF_1_1_MAX_FRAME_NAME_LENGTH);
        setMaximumDataNameLength(CIF_1_1_MAX_DATA_NAME_LENGTH);
        setSquareBracketSpecial(true);
        setVTAllowed(false);
        setFFAllowed(false);
    }

    /**
     * Performs a lexical scan of the characters from the provided
     * {@code Reader}, notifying the configured handlers of relevant
     * events until either logical end of file on the {@code Reader} or one of
     * the handlers interrupts the scan by throwing an exception
     *
     * @param  charSource the {@code Reader} from which to obtain the CIF
     *         data to scan; should not be {@code null}.  This scanner will read
     *         the input one character at a time without any buffering of its
     *         own
     *
     * @return a {@code ScanState} representing the scanner state at the end
     *         of the scan
     *         
     * @throws CifParseException if one of the configured handlers interrupts
     *         the scan by throwing it
     * @throws IOException if one is encountered while reading the input
     */
    public ScanState scan(Reader charSource)
            throws CifParseException, IOException {
        InternalScanState state = new InternalScanState(charSource);

        for (boolean wasWhitespace = true; !state.isOnEOF(); ) {
            if (state.isOnWhitespace()) {
                wasWhitespace = true;
                scanWhitespace(state);
            } else {
                if (!wasWhitespace) {
                    
                    /*
                     * Oops -- tokens not seperated by whitespace.  This can
                     * only happen when the previous token was a block-quoted
                     * value because all other tokens break only at whitespace.
                     */
                    
                    errorHandler.handleError(
                        new CifError(CifError.MISSING_WHITESPACE, state));
                    
                    // recover by assuming the missing whitespace and continuing
                }
                wasWhitespace = false;
                
                if (state.isOnComment()) {
                    scanComment(state);
                } else if (state.isOnInlineQuote()) {
                    scanInlineQuote(state);
                } else if (state.isOnBlockQuote()) {
                    scanBlockQuote(state);
                } else if (state.isOnUnquotedToken()) {
                    scanUnquotedToken(state);
                } else {
                    
                    // The character is illegal in the current context
                    
                    char currentChar;

                    errorHandler.handleError(
                            new CifError(CifError.ILLEGAL_VALUE_START, state));

                    /*
                     * recover by accepting the character (or its replacement)
                     * anyway
                     */

                    currentChar = (char) state.getCurrentChar();
                    if (Character.isWhitespace(currentChar)
                            || Character.isISOControl(currentChar)) {
                        wasWhitespace = true;
                        scanWhitespace(state);
                    } else {
                        scanUnquotedToken(state);
                    }
                }
            }
        }
        
        return state;
    }

    /**
     * Scans contiguous whitespace starting at the current character, and
     * notifies the configured whitespace handler when done
     *
     * @param  state the {@code InternalScanState} encapsulating the
     *         details of the current point in the scan
     * 
     * @throws CifParseException if the configured whitespace handler rejects
     *         the whitspace string
     * @throws IOException if one is encountered while reading the input
     */
    private void scanWhitespace(InternalScanState state)
            throws CifParseException, IOException {
        state.startNewToken();
        do {
            state.readNextChar();
        } while (state.isOnWhitespace() && !state.isOnEOF());

        wsHandler.handleWhitespace(state.getCurrentToken(), state);
    }

    /**
     * Scans a comment starting at the current character, and notifies the
     * configured whitespace handler when done
     *
     * @param  state the {@code InternalScanState} encapsulating the
     *         details of the current point in the scan
     * 
     * @throws CifParseException if the configured whitespace handler rejects
     *         the comment string
     * @throws IOException if one is encountered while reading the input
     */
    private void scanComment(InternalScanState state)
            throws CifParseException, IOException {
        state.startNewToken();
        do {
            state.readNextChar();
        } while (!state.isOnEOL());

        wsHandler.handleComment(state.getCurrentToken().substring(1), state);
    }

    /**
     * Scans an inline quote starting at the current character (the delimiter),
     * and notifies the configured token handler when done
     *
     * @param  state the {@code InternalScanState} encapsulating the
     *         details of the current point in the scan
     * 
     * @throws CifParseException if the quoted value is unterminated and the
     *         error handler interrupts the scan via exception, or if the
     *         configured token handler rejects the quoted value's content
     * @throws IOException if one is encountered while reading the input
     */
    private void scanInlineQuote(InternalScanState state)
            throws CifParseException, IOException {
        int delimiter = state.getCurrentChar();

        // consume the starting delimiter
        state.readNextChar();
        state.startNewToken();

        // scan in the remaining characters of the quoted value
        while (true) {
            boolean sawDelimiter = (state.getCurrentChar() == delimiter);

            state.readNextChar();  // previous char is provisionally accepted
            if (sawDelimiter) {
                if (state.isOnWhitespace()) {
                    String token = state.getCurrentToken();

                    tokenHandler.handleQuotedValue(
                            token.substring(0, token.length() - 1),
                            (char) delimiter, state);
                    return;
                }
            } else if (state.isOnEOL()) {
                errorHandler.handleError(
                        new CifError(CifError.UNTERMINATED_QSTRING, state));

                // recover by assuming the missing delimiter
                tokenHandler.handleQuotedValue(state.getCurrentToken(),
                                               (char) delimiter, state);
                return;
            }
        }
    }

    /**
     * Scans a block quote starting at the current character (the delimiter),
     * and notifies the configured token handler when done
     *
     * @param  state the {@code InternalScanState} encapsulating the
     *         details of the current point in the scan
     * 
     * @throws CifParseException if the quoted value is unterminated and the
     *         error handler interrupts the scan via exception, or if the
     *         configured token handler rejects the quoted value's content
     * @throws IOException if one is encountered while reading the input
     */
    private void scanBlockQuote(InternalScanState state)
            throws CifParseException, IOException {
        int delimiter = state.getCurrentChar();
        String token;

        state.readNextChar();  // advance to the first data character
        state.startNewToken();
        
        do {
            state.readNextChar();
            if (state.isOnEOF()) {
                errorHandler.handleError(
                        new CifError(CifError.UNTERMINATED_TEXT, state));

                // recover by assuming the missing delimiter
                tokenHandler.handleQuotedValue(state.getCurrentToken(),
                                               (char) delimiter, state);
                break;
            }
        } while ((state.getCurrentChar() != delimiter)
                 || (state.getCharacterNumber() > 1));
        state.readNextChar();  // advance past the closing delimiter

        token = state.getCurrentToken();
        
        // Remove trailing delimiter, including line terminator
        token = token.substring(0,
                token.length() - (token.endsWith("\r\n;") ? 3 : 2));

        tokenHandler.handleQuotedValue(token, (char) delimiter, state);
    }

    /**
     * Scans an unquoted token (data value or keyword) starting at the current
     * character, and notifies the configured token handler when done
     *
     * @param  state the {@code InternalScanState} encapsulating the
     *         details of the current point in the scan
     * 
     * @throws CifParseException if any lexical error is encountered and the
     *         configured error handler interrupts the scan via exception, or
     *         if the configured token handler rejects the scanned token
     * @throws IOException if one is encountered while reading the input
     */
    private void scanUnquotedToken(InternalScanState state)
            throws CifParseException, IOException {
        String token;
        String ltoken;

        // scan in the token
        state.startNewToken();
        do {
            state.readNextChar();
        } while (!state.isOnWhitespace());

        // determine what to do with the token

        token = state.getCurrentToken();
        ltoken = token.toLowerCase();

        if (checkAttribute(CharacterAttribute.NAME_BEGIN, ltoken.charAt(0))) {
            if (ltoken.length() > dataNameLimit) {
                errorHandler.handleError(
                        new CifError(CifError.DATA_NAME_LENGTH, state));
                // recover by accepting it anyway
            }
            tokenHandler.handleDataName(token, state);
        } else if (ltoken.startsWith("data_")) {
            String blockName = token.substring("data_".length());

            if (blockName.length() > 0) {
                if (blockName.length() > blockNameLimit) {
                    errorHandler.handleError(
                            new CifError(CifError.BLOCK_NAME_LENGTH, state));
                    // recover by accepting it anyway
                }
                tokenHandler.handleBlockHeader(blockName, state);
            } else {
                errorHandler.handleError(
                        new CifError(CifError.RESERVED_WORD, state));
                // recover by ignoring it
            }
        } else if (ltoken.equals("loop_")) {
            tokenHandler.handleLoopStart(state);
        } else if (ltoken.equals("global_")) {
            errorHandler.handleError(
                    new CifError(CifError.RESERVED_WORD, state));
            // recover by ignoring it
        } else if (ltoken.startsWith("save_")) {
            String frameName = token.substring("save_".length());

            if (frameName.length() > 0) {
                if (frameName.length() > frameNameLimit) {
                    errorHandler.handleError(
                            new CifError(CifError.FRAME_NAME_LENGTH, state));
                    // recover by accepting it anyway
                }
                tokenHandler.handleSaveFrameHeader(frameName, state);
            } else {
                tokenHandler.handleSaveFrameEnd(state);
            }
        } else if (ltoken.equals("stop_")) {
            errorHandler.handleError(
                    new CifError(CifError.RESERVED_WORD, state));
            // recover by ignoring it
        } else {
            tokenHandler.handleUnquotedValue(token, state);
        }
    }

    /**
     * Determines whether the specified character is assigned the specified
     * attribute; attributes are considered undefined for all illegal
     * characters, so this method always returns false for such (whether
     * they are in-range for the attribute table or not)
     *
     * @param  attribute the {@code CharacterAttribute} to test for
     * @param  c the character to test (as an int)
     *
     * @return {@code true} if the specified character is legal according
     *         to this scanner's configuration and is assigned the specified
     *         attribute; otherwise {@code false}
     */
    boolean checkAttribute(CharacterAttribute attribute, int c) {
        try {
            return (charAttributes[c].contains(attribute)
                    && charAttributes[c].contains(CharacterAttribute.LEGAL));
        } catch (IndexOutOfBoundsException ioobe) {
            return false;
        }
    }

    /**
     * Clears the specified attributes for the specified character, whether or
     * not they are already set, provided that {@code c} is among those
     * characters represented in the attribute table
     *
     * @param  attributes an {@code EnumSet<CharacterAttribute>} of the
     *         attributes that should be cleared for the specified character
     * @param  c the character whose attributes should be modified (as an int)
     *
     * @throws IllegalArgumentException if {@code c} is not among the
     *         characters for which attributes are supported (which include
     *         all characters legal in CIF, plus a few more)
     */
    private void clearAttributes(EnumSet<CharacterAttribute> attributes, int c) {
        try {
            charAttributes[c].removeAll(attributes);
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalArgumentException(
                    "Cannot clear attributes for character #" + c);
        }
    }

    /**
     * Sets the specified attributes for the specified character, whether or
     * not they are already set, provided that {@code c} is among those
     * characters represented in the attribute table
     *
     * @param  attributes an {@code EnumSet<CharacterAttribute>} of the
     *         attributes that should be set for the specified character
     * @param  c the character whose attributes should be modified (as an int)
     *
     * @throws IllegalArgumentException if {@code c} is not among the
     *         characters for which attributes are supported (which include
     *         all characters legal in CIF, plus a few more)
     */
    private void setAttributes(EnumSet<CharacterAttribute> attributes, int c) {
        try {
            charAttributes[c].addAll(attributes);
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalArgumentException(
                    "Cannot set attributes for character #" + c);
        }
    }

    /**
     * A ScanState implementation used by this scanner to maintain, query, and
     * expose its current scanning state for a particular scan.
     *
     * @author John C. Bollinger
     * @version 0.9.0
     */
    private class InternalScanState implements ScanState {
        
        /** The {@code Reader} from which to obtain input characters */
        private Reader charSource;

        /** The current line on which the scanner is reading */
        private int lineCount;

        /** The position of the current character on the current line */
        private int charCount;

        /** The last character read before the current one */
        private int lastChar;

        /** The current character being considered by the scanner */
        private int currentChar;

        /** The token currently under construction */
        private StringBuffer currentToken;

        /**
         * Initializes a new {@code InternalScanState} to read from the
         * specified {@code Reader}
         *
         * @param  r the {@code Reader} from which this
         *         {@code InternalScanState} will obtain input characters
         *
         * @throws IOException if reading the first character causes such an
         *         exception
         * @throws CifParseException if the first character is not legal
         *         according to the scanner's rules, and the error handler
         *         decides to interrupt the scan as a result
         */
        InternalScanState(Reader r) throws IOException, CifParseException {
            charSource = r;
            currentChar = LF;
            lineCount = 0;
            charCount = 0;
            startNewToken();
            readNextChar();
        }

        /**
         * <p>
         * Advances the scanner to the next input character, accepting the
         * current character (if any) into the current token.  Line and
         * character counters are updated to reflect the new position.  This is
         * the principal method for changing the scan state of the enclosing
         * {@code CifScanner}
         * </p><p>
         * This method will not read past logical end-of-file (ASCII character
         * 4 or 26 decimal), nor will it attempt to read past physical
         * end-of-file.  After it encounters either condition, subsequent
         * invocations of this method have no effect beyond setting
         * the lastChar attribute (and even that has visible effect only on the
         * first invocation after EOF is encountered). 
         * </p>
         *
         * @throws IOException if reading a character from the input causes one
         * @throws CifParseException if (1) an illegal character is read or (2)
         *         the line length limit is exceeded, and (in either case) the
         *         configured error handler chooses to interrupt the scan
         */
        final void readNextChar() throws IOException, CifParseException {

            lastChar = currentChar;

            if (!isOnEOF()) {
                currentToken.append((char) currentChar);
                currentChar = charSource.read();

                if (isOnEOF()) {
                    return;
                } else if (!checkAttribute(CharacterAttribute.LEGAL, currentChar)) {
                    errorHandler.handleError(
                            new CifError(CifError.ILLEGAL_CHAR, this));

                    // The error handler may have fixed up the illegal
                    // character; recover by attempting to continue

                    // An EOF character may have been set by the error handler
                    if (isOnEOF()) {
                        return;
                    }
                }

                if ((lastChar == CR) && (currentChar == LF)) {

                    // The LF of a CR LF pair does not advance the line count
                    // (the CR already did that)
                    readNextChar();

                    // If this happens, it means that a CR LF pair was broken
                    // across a token boundary:
                    assert (currentToken.length() > 1);

                } else if (checkAttribute(CharacterAttribute.LINE_TERMINATOR,
                                          lastChar)) {

                    // The first character on each line triggers the line count
                    // to advance and the character count to be reset
                    lineCount++;
                    charCount = 1;

                } else {

                    // otherwise the character counter is advanced
                    charCount++;

                    // if already at the line limit then signal an error (max
                    // once per line)
                    if ((charCount == (lineLimit + 1)) && !isOnEOL()) {
                        errorHandler.handleError(
                                new CifError(CifError.LINE_LENGTH, this));
                        // recover by ignoring the line length limit
                    }
                }
            }
        }

        /**
         * Causes this {@code InternalScanState} to start a new token with
         * the current character; the previous token is forgotten
         */
        final void startNewToken() {
            currentToken = new StringBuffer();
        }

        /**
         * {@inheritDoc}
         */
        public String getCurrentToken() {
            return currentToken.toString();
        }

        /**
         * {@inheritDoc}
         */
        public int getCurrentChar() {
            return currentChar;
        }

        /**
         * {@inheritDoc}
         *
         * @param  newChar {@inheritDoc}
         * @throws IllegalArgumentException {@inheritDoc}
         */
        public void setCurrentChar(int newChar) {
            if (newChar > Character.MAX_VALUE) {
                throw new IllegalArgumentException("Illegal character value ("
                        + newChar + ")");
            } else {
                currentChar = newChar;
            }
        }

        /**
         * {@inheritDoc}
         */
        public int getLastChar() {
            return lastChar;
        }

        /**
         * {@inheritDoc}
         */
        public int getLineNumber() {
            return lineCount;
        }

        /**
         * {@inheritDoc}
         */
        public int getCharacterNumber() {
            return charCount;
        }

        /**
         * Determines whether the scanner is currently at (logical) end of
         * file
         *
         * @return {@code true} if at EOF; {@code false} otherwise
         */
        boolean isOnEOF() {
            return ((currentChar < 0)
                    || checkAttribute(CharacterAttribute.EOF, currentChar));
        }

        /**
         * Determines whether the scanner is currently at the end of a line
         * (i.e. the current character is a recognized line terminator, or the
         * end of the file has been reached)
         *
         * @return {@code true} if at EOL; {@code false} otherwise
         */
        boolean isOnEOL() {
            return (isOnEOF()
                    || checkAttribute(CharacterAttribute.LINE_TERMINATOR,
                                      currentChar));
        }

        /**
         * Determines whether the scanner is currently on whitespace
         * (i.e. the current character is a recognized whitespace character,
         * including if it is a line terminator or at end of file)
         *
         * @return {@code true} if on whitespace; {@code false}
         *         otherwise
         */
        boolean isOnWhitespace() {
            return (isOnEOL()
                    || checkAttribute(CharacterAttribute.WHITESPACE, currentChar));
        }

        /**
         * Determines whether the scanner is currently on a comment start
         * character.  Whether or not the character actually begins a comment
         * depends on its context
         *
         * @return {@code true} if on a comment begin character;
         *         {@code false} otherwise
         */
        boolean isOnComment() {
            return checkAttribute(CharacterAttribute.COMMENT_BEGIN, currentChar);
        }

        /**
         * Determines whether the scanner is currently on an inline quote
         * delimiter.  Whether or not the character actually delimits an inline
         * quote depends on its context
         *
         * @return {@code true} if on an inline quote delimiter;
         *         {@code false} otherwise
         */
        boolean isOnInlineQuote() {
            return checkAttribute(CharacterAttribute.INLINE_QUOTE, currentChar);
        }

        /**
         * Determines whether the scanner is currently on a block quote
         * delimiter.  Whether or not the character actually delimits a block
         * quote depends on its context, though some context (position on the
         * line) is encompassed in this check
         *
         * @return {@code true} if on a block quote delimiter;
         *         {@code false} otherwise
         */
        boolean isOnBlockQuote() {
            return ((charCount == 1)
                    && checkAttribute(CharacterAttribute.BLOCK_QUOTE, currentChar));
        }

        /**
         * Determines whether the scanner is currently on a legal first
         * character for an unquoted value, data name, or keyword.  Whether
         * or not the character actually starts such a token depends on its
         * context
         *
         * @return {@code true} if on a legal first character for an
         *         unquoted token; {@code false} otherwise
         */
        boolean isOnUnquotedToken() {
            return (checkAttribute(CharacterAttribute.VALUE_BEGIN, currentChar)
                    || checkAttribute(CharacterAttribute.NAME_BEGIN, currentChar));
        }
    }
}

