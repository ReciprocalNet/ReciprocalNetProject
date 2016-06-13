/*
 * Reciprocal Net Project
 *
 * CifWhitespaceHandler.java
 *
 * 26-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

/**
 * Defines the contract of an object that should be notified about whitespace
 * and comments encountered during the course of analyzing a CIF (typically a
 * CIF parser).  For each type unit of semantically void content differentiated
 * by a CIF scanner there is a distinct {@code handleXXX()} method that the
 * scanner will invoke on any registered token handler to notify it of the
 * token.  Any handler method may cause the scan (and parse) to be halted by
 * throwing a {@code CifParseException}, or allows it continue by not doing
 * so.
 *  
 * @author John C. Bollinger
 * @version 0.9.0
 * 
 * @see CifScanner
 * @see CifTokenHandler
 * @see CifErrorHandler
 */
public interface CifWhitespaceHandler {
    
    /**
     * The handler method for contiguous sequences of whitespace (which may
     * include line separators); invoked when such a sequence is scanned by the
     * scanner with which this handler is registered.  This method may be
     * invoked two or more times in a row by a scanner (i.e. it is not
     * guaranteed that all available whitespace be reported at once, although
     * scanners are encouraged to behave this way as much as possible).
     *  
     * @param  s the whitespace, as a {@code String}; not empty, and not
     *         {@code null}
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         it would be atypical for an implementation ever to throw this
     *         exception, but it is permitted
     */
    void handleWhitespace(String s, ScanState state) throws CifParseException;
                          
    /**
     * The handler method for CIF comments (such as "# this is a comment<eol>");
     * invoked when such a sequence is scanned by the scanner with which this
     * handler is registered.  Comments are by definition confined to a single
     * line, and multiple consecutive comment lines will be reported seperately
     * via multiple invocations of this method.
     *  
     * @param  comment the comment, less the initial flag character ('#'); may
     *         be empty but not {@code null}, and may contain whitespace
     *         but not line termination sequences recognized by the scanner
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         it would be atypical for an implementation ever to throw this
     *         exception, but it is permitted
     */
    void handleComment(String comment, ScanState state)
            throws CifParseException;
}

