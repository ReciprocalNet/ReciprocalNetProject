/*
 * Reciprocal Net Project
 *
 * CifTokenHandler.java
 *
 * 26-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

/**
 * Defines the contract of an object that should be notified about lexical
 * tokens encountered during the course of analyzing a CIF (typically a CIF
 * parser).  For each type of (non-whitespace) token differentiated by a CIF
 * scanner there is a distinct {@code handleXXX()} method that the scanner
 * will invoke on any registered token handler to notify it of the token.  Any
 * handler method may cause the scan (and parse) to be halted by throwing a
 * {@code CifParseException}, or allows it continue by not doing so.
 *  
 * @author John C. Bollinger
 * @version 0.9.0
 * 
 * @see CifScanner
 * @see CifWhitespaceHandler
 * @see CifErrorHandler
 */
public interface CifTokenHandler {
    
    /**
     * The handler method for CIF data block headers (such as "data_foo");
     * invoked when a data block header is scanned by the scanner with which
     * this handler is registered
     *  
     * @param  blockName the data block name (the token tail after "data_"); not
     *         empty, and not {@code null}.  By the definition of the data
     *         block header, the name cannot include whitespace
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably because the data block name is a duplicate
     */
    void handleBlockHeader(String blockName,
                           ScanState state) throws CifParseException;
                           
    /**
     * The handler method for CIF save frame headers (such as "save_foo");
     * invoked when a save frame header is scanned by the scanner with which
     * this handler is registered
     *  
     * @param  frameName the save frame name (the token tail after "save_"); not
     *         empty, and not {@code null}.  By the definition of the save
     *         frame header, the name cannot include whitespace
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably either because the save frame name is a duplicate
     *         or because the handler refuses to accept save frames at all
     */
    void handleSaveFrameHeader(String frameName,
                               ScanState state) throws CifParseException;
                               
    /**
     * The handler method for CIF save frame terminators ("save_");
     * invoked when a save frame terminator is scanned by the scanner with which
     * this handler is registered
     *  
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably either because no save frame was being parsed or
     *         because the handler refuses to accept save frames at all
     */
    void handleSaveFrameEnd(ScanState state) throws CifParseException;
                               
    /**
     * The handler method for CIF loop header beginning keywords ("loop_");
     * invoked when the loop begin keyword is scanned by the scanner with which
     * this handler is registered
     *  
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably either because a data value was expected instead
     *         or because of two (or more) consecutive loop begin keywords
     */
    void handleLoopStart(ScanState state) throws CifParseException;
    
    /**
     * The handler method for CIF data names (such as "_foo"); invoked when a
     * data name is scanned by the scanner with which this handler is registered
     *  
     * @param  name the data name (the whole token scanned); at least two
     *         characters long, with the first being an underscore ('_').  By
     *         the definition of data names, the name cannot include whitespace
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably either because of a duplicate data name or because
     *         a data value was expected instead 
     */
    void handleDataName(String name,
                        ScanState state) throws CifParseException;
                        
    /**
     * The handler method for quoted CIF data values; invoked when such a data
     * value is scanned by the scanner with which this handler is registered.
     * This method is used for all quoted values, regardless of quote style or
     * delimiter
     *  
     * @param  value the data value (the quoted content, not including the
     *         delimiters); may be empty but not null.  May contain line
     *         separator character sequences for the host platform or for other
     *         platforms.
     * @param  delimiter the delimiter character observed for this quoted value
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably because a data name was expected instead
     */
    void handleQuotedValue(String value, char delimiter,
                           ScanState state) throws CifParseException;
                           
    /**
     * The handler method for unquoted CIF data values; invoked when such a data
     * value is scanned by the scanner with which this handler is registered.
     * This method is used for all unquoted values, regardless of implied data
     * type 
     *  
     * @param  value the data value (the entire token scanned); will not be
     *         empty or {@code null}, and will not contain whitespace.
     * @param  state a {@code ScanState} describing the current state of
     *         the scanner invoking this method; details such as the position
     *         in the input can be extracted from this object, and it can be
     *         used in constructing a {@code CifParseException} if that
     *         should be necessary
     *  
     * @throws CifParseException if the scan and parse should be interrupted;
     *         normally this would imply a parse error of some sort -- in this
     *         case probably because a data name was expected instead
     */
    void handleUnquotedValue(String value,
                             ScanState state) throws CifParseException;
}
