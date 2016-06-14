/*
 * Reciprocal Net Project
 *
 * CifFileBuilder.java
 *
 * 30-Sep-2005: jobollin extracted this class from CifParser
 * 28-Apr-2006: jobollin added additional error handling throughout so that
 *              IllegalArgumentExceptions should no longer be propagated from
 *              CifFile and its associated classes.  Instead CifErrors are
 *              generated and dispatched, or (rarely) CifParseExceptions are
 *              thrown directly
 */
package org.recipnet.common.files.cif;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.recipnet.common.files.CifFile;

/**
 * <p>
 * A {@code CifTokenHandler} implementation that provides a full-fledged,
 * non-validating CIF parser.  Instances are intended to parse entire CIFs (not
 * CIF fragments).  Multiple input CIFs may be parsed into a combined
 * {@code CifFile} object, with the caveat that data block names must in that
 * case be unique across all such CIFs (as opposed to merely within each CIF
 * individually).
 * </p><p>
 * This class exists seperate from {@link CifParser} primarily so as to provide
 * an avenue for parsing CIFs into {@link CifFile}s with use of non-default
 * scanner configurations and/or fundamentally different error-handling
 * semantics.  For most purposes it will probably be easier to use a
 * {@code CifParser} than to use this class directly.
 * </p>
 *   
 * @author John C. Bollinger
 * @version 0.9.0
 */    
public class CifFileBuilder implements CifTokenHandler {

    /**
     * An {@code enum} of the possible parsing states a {@code CifFileBuilder}
     * may have as it parses its input
     */
    private enum ParseState {

        /**
         * Represents the parsing state before a data block header has been
         * parsed
         */
        PREBLOCK_STATE,

        /**
         * Represents the parsing state when an (unlooped) data name can be
         * accepted
         */
        EXPECT_NAME_STATE,

        /**
         * Represents the parsing state when an (unlooped) data value is
         * required
         */
        EXPECT_VALUE_STATE,

        /**
         * Represents the parsing state when a loop header is being parsed
         */
        LOOP_HEADER_STATE,

        /**
         * Represents the parsing state when looped data values are being parsed
         */
        LOOP_VALUE_STATE
    }

    /**
     * A Pattern that matches numbers in the family of formats recognized by
     * CIF: an integer or floating point number, with optional initial sign,
     * optional exponent, and optional parenthesized standard uncertainty 
     */
    private final Pattern NUMBER_PATTERN =
            Pattern.compile(
                    // optional sign; start of first capturing group
                    "([-+]?"
                    // optional digits (posessive)
                    + "\\d*+"
                    // fraction part, optional if digits have already
                    // been matched; end of the first group
                    + "(?:(?:\\.\\d+)|(?:(?<=\\d)\\.?)))"
                    // optional exponent; second capturing group
                    + "(?:[eE]([-+]?\\d+))?"
                    // optional standard uncertainty; has a third group
                    + "(?:\\((\\d+)\\))?");
    
    /**
     * A {@code Pattern} describing the character sequence splitting the two
     * or more parts of a folded text block line (c.f. section 26 of the
     * <a href=
     * "http://www.iucr.org/iucr-top/cif/spec/version1.1/cifsemantics.html"
     * >CIF 1.1 "Common Semantic Features" document</a>
     */
    private final Pattern FOLDED_LINE_PATTERN =
        /*
         * a literal backslash, followed by any amount of inline whitespace,
         * and ending with a CIF line terminator
         */ 
        Pattern.compile("\\\\[ \t\u000b]*(?:(?:\\r\\n?)|\\n|\\f)");

    /**
     * The {@code CifErrorHandler} to which parse errors should be reported
     */
    private final CifErrorHandler errorHandler;
    
    /**
     * A flag that determines whether the CIF 1.1 line-folding convention
     * will be recognized and automatically unfolded by this object
     */
    private boolean unfoldingLines;
    
    /** The code for the current parsing state */
    private ParseState currentState;
    
    /** Flags whether or not a save frame is currently being parsed */
    private boolean inSaveFrame;
    
    /** The {@code CifFile} under construction */
    private CifFile currentCif;
    
    /** The {@code CifFile.DataBlock} under construction */
    private CifFile.DataBlock currentBlock;
    
    /** The {@code CifFile.DataCell} under construction */
    private CifFile.DataCell currentCell;
    
    /** The {@code CifFile.DataLoop} under construction */
    private CifFile.DataLoop currentLoop;
    
    /** The values so far parsed for the loop record under construction */
    private List<CifFile.CifValue> currentLoopRecord;
    
    /** The data name most recently parsed */
    private String currentName;

    /**
     * Initializes a new {@code CifFileBuilder} with the specified error
     * handler to which parse errors can be delegated
     * 
     * @param  handler the {@code CifErrorHandler} to which parse errors
     *         should be directed, or {@code null} if parse errors should be
     *         ignored (and error recovery automatically attempted).  Error
     *         recovery will be attempted in any case in the event of any error
     *         for which this handler does not throw a {@link CifParseException}. 
     */
    public CifFileBuilder(CifErrorHandler handler) {
        errorHandler = handler;
        currentState = ParseState.PREBLOCK_STATE;
        inSaveFrame = false;
        currentCif = new CifFile();
    }

    /**
     * Returns the value of the {@code unfoldingLines} flag, which indicates
     * whether or not the CIF 1.1 line folding protocol will be interpreted by
     * this parser.  The setting of this flag affects the parsed values of text
     * blocks that make use of the protocol, but not the well-formedness of any
     * CIF file. 
     * 
     * @return {@code true} if this parser is set to interpret the text block
     *         line-folding protocol; {@code false} if it is set to not
     *         interpret the protocol
     */
    public boolean isUnfoldingLines() {
        return unfoldingLines;
    }
    
    /**
     * Sets the value of the {@code unfoldingLines} flag, which indicates
     * whether or not the CIF 1.1 line folding protocol will be interpreted by
     * this parser.  The setting of this flag affects the parsed values of text
     * blocks that make use of the protocol, but not the well-formedness of any
     * CIF file. 
     * 
     * @param unfold the new value for the {@code unfoldingLines} flag
     */
    public void setUnfoldingLines(boolean unfold) {
        this.unfoldingLines = unfold;
    }

    /**
     * Recognizes and notifies about error conditions that may occur at the
     * end of the input, and returns the parsed CIF data.  Once this method has
     * been invoked this {@code CifFileBuilder} should be discarded. 
     * 
     * @param  state a {@code ScanState} representing the current state
     *         of the underlying scanner
     * 
     * @return a {@code CifFile} representing the data parsed since
     *         the creation of this handler
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    @SuppressWarnings("incomplete-switch")
    public CifFile handleEndOfFile(ScanState state) throws CifParseException {
    
        // parse errors at EOF:
        switch (currentState) {
            case EXPECT_VALUE_STATE:
                handleError(new CifError(CifError.DATA_VALUE_MISSING,
                                         state));
                /*
                 * recover by ignoring the final loop record, and the whole
                 * loop if there were no other records
                 */
                break;
            case LOOP_HEADER_STATE:
                handleError(
                        new CifError((currentLoop.getRecordSize() == 0)
                                        ? CifError.LOOP_EMPTY_HEADER
                                        : CifError.LOOP_NO_DATA,
                                     state));
                // recover by ignoring the empty loop
                break;
            case LOOP_VALUE_STATE:
                if (currentLoopRecord.size() != 0) {
                    handleError(new CifError(CifError.LOOP_PARTIAL_RECORD,
                                             state));
                    // recover by ignoring the partial record
                }
                break;
        }
        
        if (inSaveFrame) {
            handleError(new CifError(CifError.UNTERMINATED_FRAME, state));
            // Recover by ignoring the error
        }
        
        /*
         * Any error that may have been detected was handled without aborting
         * the parse.  Return the now-complete CifFile:
         */
        return currentCif;
    }
    
    /**
     * Handles a block header in a manner appropriate for the current
     * internal parsing state
     * 
     * @param  blockName the scanned data block name 
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    public void handleBlockHeader(String blockName, ScanState state)
            throws CifParseException {

        // Check for (and possibly clean up) various parse errors 
        handleCellStart(state);
    
        // Set up the new data block
        currentBlock = new CifFile.DataBlock(blockName);
        currentCell = currentBlock;
        try {
            currentCif.addDataBlock(currentBlock);
        } catch (IllegalArgumentException iae) {
            handleError(new CifError(CifError.DUPLICATE_BLOCK_CODE, state));
            
            /*
             * recover by effectively ignoring all data in this new block
             */
            
        } finally {
            currentState = ParseState.EXPECT_NAME_STATE;
        }
    }

    /**
     * Handles a save frame header.
     * 
     * @param  frameName the name of the save frame
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if the configured error handler chooses to
     *         abort the parse
     */
    public void handleSaveFrameHeader(String frameName, ScanState state)
            throws CifParseException {
        CifFile.SaveFrame saveFrame;

        if (currentState == ParseState.PREBLOCK_STATE) {
        
            // This parser flags save frames 
            handleError(new CifError(CifError.RESERVED_WORD, state));
            
            // A save frame is any any case not legal here
            handleError(new CifError(CifError.FRAME_NO_BLOCK, state));

            /*
             * recover by ignoring the save frame header (frame contents may
             * induce an error cascade)
             */
            
        } else {
            
            // Check for (and possibly clean up) various parse errors 
            handleCellStart(state);
            
            // This parser flags save frames 
            handleError(new CifError(CifError.RESERVED_WORD, state));

            /*
             * Attempting to parse anyway; recover by setting up to parse the
             * save frame contents
             */
            saveFrame = new CifFile.SaveFrame(frameName);
            
            try {
                currentBlock.addSaveFrame(saveFrame);
            } catch (IllegalArgumentException iae) {
                handleError(new CifError(CifError.DUPLICATE_FRAME_CODE, state));
                
                /*
                 * The CIF is *really* messed up, but we don't have to give up
                 * parsing.  The save frame we just started won't show up in the
                 * parsed CifFile, however.
                 */ 
            }
            currentCell = saveFrame;
            inSaveFrame = true;
        }
    }

    /**
     * Performs common error handling and state fixup at the beginning of
     * data blocks and save frames
     * 
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if the configured error handler chooses to
     *         abort the parse
     */
    @SuppressWarnings("incomplete-switch")
    private void handleCellStart(ScanState state) throws CifParseException {

        // Parse errors:                
        switch (currentState) {
            case EXPECT_VALUE_STATE:
                handleError(new CifError(CifError.DATA_VALUE_MISSING, state));
                    
                /*
                 * recover by ignoring the previous data name (i.e. no
                 * special action)
                 */
                break;
            case LOOP_HEADER_STATE:
                handleError(
                        new CifError((currentLoop.getRecordSize() == 0)
                                        ? CifError.LOOP_EMPTY_HEADER
                                        : CifError.LOOP_NO_DATA,
                                     state));
                                 
                // recover by dropping the incomplete loop
                break;
            case LOOP_VALUE_STATE:
                if (currentLoopRecord.size() > 0) {
                    handleError(
                            new CifError(CifError.LOOP_PARTIAL_RECORD, state));
                        
                    /*
                     * recover by filling the incomplete loop record with
                     * the unknown value
                     */
                     finishLoopRecord();
                }
                break;
        }
    
        if (inSaveFrame) {
            handleError(new CifError(CifError.UNTERMINATED_FRAME, state));
        
            // Recover by closing the frame implicitly
            inSaveFrame = false;
        }
    }

    /**
     * Handles a save frame terminator by signaling a grammar error (this
     * parser does not support save frames).
     * 
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if the configured error handler chooses to
     *         abort the parse
     */
    public void handleSaveFrameEnd(ScanState state) throws CifParseException {
    
        /*
         * This parser flags save frames, but no need to the end if we already
         * flagged the beginning
         */
        if (!inSaveFrame) {  
            handleError(new CifError(CifError.RESERVED_WORD, state));
        }
    
        // recover by restoring the surrounding data block context (if any)
        currentCell = currentBlock;
        inSaveFrame = false;
    }

    /**
     * Handles a loop start keyword in a manner appropriate for the current
     * internal parsing state
     * 
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    @SuppressWarnings("incomplete-switch")
    public void handleLoopStart(ScanState state) throws CifParseException {
    
        // handle parse errors:
        switch (currentState) {
            case PREBLOCK_STATE:
                handleError(new CifError(CifError.LOOP_HEADER_NO_BLOCK,
                                         state));
                    
                // recover by ignoring it
                return;
            case EXPECT_VALUE_STATE:
                handleError(new CifError(CifError.DATA_VALUE_MISSING,
                                         state));
                    
                // recover by ignoring the previous data name
                break;
            case LOOP_HEADER_STATE:
                handleError(new CifError(CifError.LOOP_EMPTY_HEADER,
                                         state));
                                 
                // recover by dropping the incomplete loop
                break;
            case LOOP_VALUE_STATE:
                if (currentLoopRecord.size() > 0) {
                    handleError(
                            new CifError(CifError.LOOP_PARTIAL_RECORD,
                                         state));
                        
                    /*
                     * recover by filling the incomplete loop record with
                     * the unknown value
                     */
                    finishLoopRecord();
                }
                break;
        }

        /*
         * Any parse error detected was handled without aborting the parse;
         * handle the new loop start:
         */
        currentLoop = new CifFile.DataLoop();
        currentState = ParseState.LOOP_HEADER_STATE;
    }

    /**
     * Handles a scanned data name in a manner appropriate for the current
     * internal parsing state
     * 
     * @param  name the data name
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    @SuppressWarnings("incomplete-switch")
    public void handleDataName(String name, ScanState state)
            throws CifParseException {
        // parse errors:
        switch (currentState) {
            case PREBLOCK_STATE:
                handleError(new CifError(CifError.DATA_NAME_NO_BLOCK, state));
                    
                // recover by ignoring the data name
                return;
            case EXPECT_VALUE_STATE:
                handleError(new CifError(CifError.DATA_VALUE_MISSING, state));
                    
                // recover by ignoring the previous data name
                break;
            case LOOP_HEADER_STATE:
                if(currentLoop.containsName(name)) {
                    handleError(
                            new CifError(CifError.DUPLICATE_DATA_NAME, state));

                    // recover by ignoring the duplicate name
                    return;
                }
                break;
            case LOOP_VALUE_STATE:
                if (currentLoopRecord.size() > 0) {
                    handleError(
                            new CifError(CifError.LOOP_PARTIAL_RECORD, state));
                        
                    /*
                     * recover by filling the incomplete loop record with
                     * the unknown value
                     */
                    finishLoopRecord();
                }
                break;
        }
        if (currentCell.containsName(name)) {
            handleError(new CifError(CifError.DUPLICATE_DATA_NAME, state));
            
            // Manufacture a unique name (note: may exceed length limit)
            do {
                name = name + '_';
            } while (currentCell.getDataNames().contains(name));
        }
    
        /*
         * Any parse error detected was handled without aborting the parse;
         * handle the data name:
         */
        if (currentState == ParseState.LOOP_HEADER_STATE) {
            try {
                currentLoop.addName(name);
            } catch (IllegalArgumentException iae) {
                
                /*
                 * The name probably contains an illegal character that was
                 * not fixed up by the error handler.  We have no alternative
                 * but to throw an exception.
                 */
                CifParseException cpe = new CifParseException(iae.getMessage());
                
                cpe.initCause(iae);
                throw cpe;
            }
        } else {
            currentName = name;
            currentState = ParseState.EXPECT_VALUE_STATE;
        }
    }

    /**
     * Handles a quoted data value in a manner appropriate for the current
     * internal parsing state
     * 
     * @param  value the scanned value (without the delimiting quote
     *         characters)
     * @param  delim the delimiter character observed by the scanner
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    public void handleQuotedValue(String value, char delim, ScanState state)
            throws CifParseException {
        StringValue sv;
        
        if (isUnfoldingLines() && (delim == ';')) {
            Matcher m = FOLDED_LINE_PATTERN.matcher(value);
            
            if (m.lookingAt()) {
                sv = new StringValue(m.replaceAll(""));
                handleError(new CifWarning("CIF 1.0 compatability warning:" +
                        " text block lines unfolded per the CIF 1.1 line" +
                        " (un)folding convention in text block at line "
                        + state.getLineNumber()));
            } else {
                sv = new StringValue(value);
            }
        } else {
            sv = new StringValue(value);
        }
        handleValue(sv, state);
    }

    /**
     * Handles an unquoted data value in a manner appropriate for the
     * current internal parsing state
     * 
     * @param  value the unquoted value (a String) scanned from the input 
     * @param  state a {@code ScanState} representing current scanner
     *         state
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    public void handleUnquotedValue(String value, ScanState state)
            throws CifParseException {
        CifFile.CifValue cifValue;
            
        // Construct the appropriate value
        if (value.equals(".")) {
            cifValue = NAValue.instance;
        } else if (value.equals("?")) {
            cifValue = UnknownValue.instance;
        } else {
            Matcher numberMatcher = NUMBER_PATTERN.matcher(value);
        
            if (numberMatcher.matches()) {
                cifValue = new NumberValue(numberMatcher.group(1),
                        numberMatcher.group(2), numberMatcher.group(3));
            } else {
                cifValue = new StringValue(value);
            }
        }

        handleValue(cifValue, state);
    }

    /**
     * Performs relevant parse state checks and then handles the (typed)
     * value corresponding to a quoted or unquoted value scanned from the
     * input
     * 
     * @param  value a {@code CifFile.CifValue} representing the value
     *         scanned from the input CIF
     * @param  state a {@code ScanState} representing the current state
     *         of the underlying scanner
     * @param  <V> the type of CifValue to handle 
     * 
     * @throws CifParseException if a syntax, grammar, or semantic error is
     *         detected and the configured error handler chooses to abort
     *         the parse
     */
    private <V extends CifFile.CifValue> void handleValue(
            V value, ScanState state) throws CifParseException {
    
        switch (currentState) {
            case PREBLOCK_STATE:
                handleError(new CifError(CifError.VALUE_NO_BLOCK, state));
                    
                // recover by ignoring the data name
                return;
            case EXPECT_NAME_STATE:
                handleError(new CifError(CifError.DATA_NAME_MISSING,
                                         state));

                // recover by ignoring the data value
                return;
            case LOOP_HEADER_STATE:
                if (currentLoop.getRecordSize() == 0) {
                    handleError(
                            new CifError(CifError.LOOP_EMPTY_HEADER,
                                         state));
                    handleError(
                            new CifError(CifError.DATA_NAME_MISSING,
                                         state));
                        
                    // recover by ignoring the value and dropping the loop
                    currentState = ParseState.EXPECT_NAME_STATE;
                    return;
                } else {
                    
                    // This case is OK; start recording a loop record:
                    currentLoopRecord = new ArrayList<CifFile.CifValue>();
                    currentState = ParseState.LOOP_VALUE_STATE;
                
                    // fall through
                }
            case LOOP_VALUE_STATE:
                
                // This case is OK; add the value to the current loop record: 
                currentLoopRecord.add(value);
                if (currentLoopRecord.size() == currentLoop.getRecordSize()) {
                    finishLoopRecord();
                }
                // no state change
                break;
            case EXPECT_VALUE_STATE:
                
                // This case is OK; add a new scalar item to the CifFile:
                CifFile.ScalarData<V> scalar;
                
                try {
                    scalar = new CifFile.ScalarData<V>(currentName, value);
                } catch (IllegalArgumentException iae) {
                    
                    /*
                     * Probably an illegal name; Nothing to do but throw.
                     */
                    CifParseException cpe
                            = new CifParseException(iae.getMessage());
                    
                    cpe.initCause(iae);
                    throw cpe;
                }
                
                // Duplicate names should already have been fixed up
                currentCell.addScalar(scalar);
                currentState = ParseState.EXPECT_NAME_STATE;
                break;
            default:
                
                /*
                 * This cannot occur because the above cases cover all the
                 * alternatives
                 */
                assert false;
        }
    }

    /**
     * Handles finishing off the current loop record, whether complete or 
     * not.  The record is filled out with {@code UnknownValue}s if
     * necessary and added to the current loop; if it is the first record
     * then the loop is thereafter added to the current data cell
     */
    private void finishLoopRecord() {
        assert currentLoopRecord.size() != 0;
    
        for (int i = currentLoop.getRecordSize() - currentLoopRecord.size();
             i > 0; i--) {
            currentLoopRecord.add(UnknownValue.instance);
        }
        currentLoop.addRecord(currentLoopRecord);
        currentLoopRecord.clear();
        if (currentLoop.getRecordCount() == 1) {
            currentCell.addLoop(currentLoop);
        }
    }
    
    /**
     * Handles a {@code code CifError} by delegating to the configured error
     * handler.
     * 
     * @param  error the {@code CifError} to handle; should not be
     *         {@code null}
     * 
     * @throws CifParseException if the configured error handler throws it
     * 
     * @see CifErrorHandler#handleError(CifError) 
     */
    private void handleError(CifError error) throws CifParseException {
        if (errorHandler != null) {
            errorHandler.handleError(error);
        }
    }
}
