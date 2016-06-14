/*
 * Reciprocal Net project
 * 
 * CifParser.java
 *
 * 31-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.recipnet.common.files.CifFile;

/**
 * <p>
 * {@code CifParser} is a class that knows how to create {@code CifFile} objects
 * from data obtained via an {@link java.io.InputStream InputStream} or a
 * {@link java.io.Reader Reader}.  This parser by default attempts to continue
 * to parse to the end of the data under any circumstances.  This behavior can
 * be modified by setting a {@code CifErrorHandler}; the supplied handler will
 * be notified of all error conditions detected by the parser and underlying
 * scanner, and will have the option to abort the scan in response to any such
 * event by throwing a {@code CifParseException}.  This class automatically
 * configures a {@link CifScanner} with which to scan its input; for this
 * purpose it configures a scanner that enforces the most restrictive possible
 * combination of CIF 1.0 and CIF 1.1 syntax.  It also, however, uses an error
 * handler that wraps CIF 1.0 / CIF 1.1 compatibility errors as
 * {@link CifWarning warnings}, so that user error handlers can distinguish
 * these fairly benign and easy to interpret cases from true syntax errors.
 * Most of this class' actual parsing is delegated to a {@link CifFileBuilder}.
 * </p><p>
 * This class is thread-safe inasmuch as one instance can simultaneously parse
 * multiple distinct inputs into different CifFile objects, though the same
 * error handler (if any is set) will be advised of errors on all files.  The
 * class is not safe relative to other threads accessing the stream(s) from
 * which it is obtaining input, however.
 * </p> 
 *
 * @author John C. Bollinger
 * @version 0.9.0
 * 
 * @see org.recipnet.common.files.CifFile CifFile
 */
public class CifParser {
    
    /**
     * The CifErrorHandler to which the decision of whether or not to abort
     * the parse is delegated
     */ 
    private CifErrorHandler errorDelegate = new CifErrorIgnorer();
    
    /**
     * Parses CIF format data from the supplied input and returns the result
     * in the form of a {@code CifFile}.  The input is handled as if it
     * were a character stream encoded via a one-byte encoding congruent with
     * the first 256 Unicode code points.  If the stream contains non-ASCII
     * characters then whether or not the resulting lexical errors report the
     * "correct" illegal character depends on the real encoding of the stream.
     * For best performance, the stream should be buffered. 
     * 
     * @param  input the {@code InputStream} from which to obtain the data;
     *         will be read to its logical end if the parse comletes normally
     * 
     * @return a {@code CifFile} representing the parsed CIF data
     * 
     * @throws CifParseException if the configured error handler chooses to
     *         abort the parse in response to a lexical or grammatical error
     * @throws IOException if one is encountered while reading the input
     */
    public CifFile parseCif(InputStream input)
            throws CifParseException, IOException {
        return parseCif(new NonDecodingReader(input));
    }
    
    /**
     * <p>
     * Parses CIF format data from the supplied input and returns the result
     * in the form of a {@code CifFile}.  For best performance the input
     * should be buffered.
     * </p><p>
     * <strong>Note:</strong> default {@code Reader} behavior upon encountering
     * non-decodable byte sequences is to translate them into the '?' character.
     * The character sequence resulting from such a transformation may have
     * different semantics in CIF than would either the correctly-decoded
     * sequence or the raw byte stream.   
     * </p>
     * 
     * @param  input the {@code Reader} from which to obtain the data;
     *         will be read to its end if the parse comletes normally
     * 
     * @return a {@code CifFile} representing the parsed CIF data
     * 
     * @throws CifParseException if the configured error handler chooses to
     *         abort the parse in response to a lexical or grammatical error
     * @throws IOException if one is encountered while reading the input
     */
    public CifFile parseCif(Reader input) throws CifParseException, IOException {
        ErrorHandler errorHandler = new ErrorHandler();
        CifFileBuilder handler = new CifFileBuilder(errorHandler);
        CifScanner scanner = new CifScanner();
        
        scanner.setTokenHandler(handler);
        scanner.setErrorHandler(errorHandler);
        
        /*
         * Configure the scanner for the most restrictive mix of CIF 1.0 and
         * CIF 1.1 requirements:
         */
        scanner.useCif11Rules();
        scanner.setMaximumLineLength(
                CifScanner.CIF_1_0_MAX_LINE_LENGTH);
        scanner.setMaximumBlockNameLength(
                CifScanner.CIF_1_0_MAX_BLOCK_NAME_LENGTH);
        
        // Support the CIF 1.1 line folding convention
        handler.setUnfoldingLines(true);

        // Perform the scan / parse
        return handler.handleEndOfFile(scanner.scan(input));
    }

    /**
     * Sets an external error handler to be notified of lexical and grammatical
     * errors encountered during a parse.  The handler then has an opportunity
     * to abort the parse (by throwing a CifParseException) and / or otherwise
     * to respond to the error.
     * 
     * @param  handler the {@code CifErrorHandler} to notify of any
     *         errors encountered
     * 
     * @see CifErrorAlerter
     * @see CifErrorIgnorer
     * @see CifErrorRecorder
     */    
    public void setErrorHandler(CifErrorHandler handler) {
        errorDelegate = handler;
    }
    
    /**
     * Handles a {@code code CifError} by delegating to the externally
     * configured error handler.  Reported errors corresponding to differences
     * between CIF 1.0 and CIF 1.1 are recast as warnings by wrapping them in
     * {@code CifWarning} objects
     * 
     * @param  error the {@code CifError} to handle; should not be
     *         {@code null}
     * 
     * @throws CifParseException if the configured error handler throws it
     * 
     * @see CifErrorHandler#handleError(CifError) 
     */
    void handleError(CifError error) throws CifParseException {
        CifError revisedError = error;
        int currentChar;
        String currentToken;
        
        switch (error.getCode()) {
            case CifError.LINE_LENGTH:
                if (error.getScanState().getCharacterNumber()
                        < CifScanner.CIF_1_1_MAX_LINE_LENGTH) {
                    revisedError =
                            new CifWarning("CIF 1.0 compliance warning", error);
                }
                break;
            case CifError.BLOCK_NAME_LENGTH:
                if (error.getScanState().getCharacterNumber()
                        <= CifScanner.CIF_1_1_MAX_BLOCK_NAME_LENGTH) {
                    revisedError =
                            new CifWarning("CIF 1.0 compliance warning", error);
                }
                break;
            case CifError.DATA_NAME_LENGTH:
                if (error.getScanState().getCharacterNumber()
                        <= CifScanner.CIF_1_0_MAX_DATA_NAME_LENGTH) {
                    revisedError =
                            new CifWarning("CIF 1.1 compliance warning", error);
                }
                break;
            case CifError.ILLEGAL_CHAR:
                ScanState state = error.getScanState();
                
                currentChar = state.getCurrentChar();
                if ((currentChar == '\u000b') || (currentChar == '\u000c')) {
                    revisedError =
                            new CifWarning("CIF 1.1 compliance warning", error);
                    state.setCurrentChar(
                            (currentChar == '\u000b') ? ' ' : '\n');
                }
                break;
            case CifError.ILLEGAL_VALUE_START:
                currentChar = error.getScanState().getCurrentChar();
                if ((currentChar == '[') || (currentChar == ']')) {
                    revisedError =
                            new CifWarning("CIF 1.1 compliance warning", error);
                }
                break;
            case CifError.RESERVED_WORD:
                currentToken = error.getScanState().getCurrentToken();
                if (currentToken.toLowerCase().startsWith("save_")
                        && (currentToken.length() > "save_".length())) {
                    revisedError =
                            new CifWarning("CIF 1.0 compliance warning", error);
                }
                break;
        }
        errorDelegate.handleError(revisedError);
    }

    /**
     * A CifErrorHandler implementation that delegates to the containing
     * {@code CifParser}'s internal error-handling implementation.
     *   
     * @author John C. Bollinger
     * @version 0.9.0
     */    
    private class ErrorHandler implements CifErrorHandler {
        
        /**
         * {@inheritDoc}.  This implementation dispatches the error to the
         * containing {@code CifParser}'s {@code handleError(CifError)} method
         */
        public void handleError(CifError error) throws CifParseException {
            CifParser.this.handleError(error);
        }
    }
    
    /**
     * A {@code Reader} that translates bytes from an underlying
     * {@code InputStream} into {@code char}s having the same numeric value.
     * This is equivalent to decoding characters according to a 1-byte charset
     * congruent with the first 256 Unicode code points, and it is almost never
     * the right thing to do.  The {@code CifParser}, however, does it in order
     * to avoid the decoding error behavior associated with use of a charset
     * (i.e. translation of the bad byte(s) into a character designated for that
     * purpose, typically '?').  This is different from using one of the
     * available one-byte charsets, such as one of the ISO-8859 variants,
     * because it supports all possible input byte values and doesn't mangle
     * any non-ASCII codes into characters greater than 0xff (although that
     * last hardly matters for ASCII-only CIF).
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private class NonDecodingReader extends Reader {
        
        /**
         * The underlying {@code InputStream} from which this {@code Reader}
         * obtains bytes 
         */
        private final InputStream stream;
        
        /**
         * Initializes a new {@code NonDecodingReader} to wrap the specified
         * {@code InputStream}
         * 
         * @param  is the {@code InputStream} to serve as the byte source for
         *         this {@code Reader}; may not be {@code null} 
         */
        public NonDecodingReader(InputStream is) {
            if (is == null) {
                throw new NullPointerException("Null input stream");
            }
            stream = is;
        }

        /**
         * {@inheritDoc}  This version just returns the next byte from the
         * underlying {@code InputStream} (as an {@code int})
         * 
         * @see java.io.Reader#read()
         */
        @Override
        public int read() throws IOException {
            return stream.read();
        }
        
        /**
         * {@inheritDoc}  This version obtains its characters as individual
         * bytes from the underlying {@code InputStream}, and simply casts them
         * as {@code char}s
         * 
         * @see java.io.Reader#read(char[], int, int)
         */
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int counter;
            
            if (len == 0) {
                return 0;
            } else {
                for (counter = 0; counter < len; counter++) {
                    int c = stream.read();
                    
                    if (c >= 0) {
                        cbuf[off + counter] = (char) c;
                    } else {
                        break;
                    }
                }
                
                return (counter == 0) ? -1 : counter;
            }
        }

        /**
         * {@inheritDoc}  This version simply closes the underlying
         * {@code InputStream}, therefore it shares the closure behavior of
         * that stream
         * 
         * @see java.io.Closeable#close()
         */
        @Override
        public void close() throws IOException {
            stream.close();
        }
    }
}

