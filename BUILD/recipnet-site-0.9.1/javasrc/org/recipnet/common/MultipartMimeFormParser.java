/*
 * Reciprocal Net project
 * 
 * MultipartMimeFormParser.java
 *
 * 10-Jun-2003: midurbin wrote the first draft
 * 22-Jul-2003: ekoperda fixed bug #988 in readBytes()
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package
 *              to org.recipnet.common; removed dependency on 
 *              OperationFailedException
 * 15-May-2006: jobollin added support for multiline headers in message parts;
 *              made character set handling more correct, especially in honoring
 *              per-field charsets specified by the sender and in parsing
 *              headers strictly as US-ASCII (per specs); reformatted the code
 */

package org.recipnet.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * This class parses data recieved from a POST when the encoding type is
 * 'multipart/form-data'. A form containing a FileUpload element must use this
 * encoding type, and it cannot be handled in the default way due to the
 * potential size of the attached file.
 * </p><p>
 * This class allows for the sequential reading of form fields from the posted
 * {@code InputStream} that can be taken from the {@code HttpServletRequest}
 * object. Form fields should be laid out in the order in which they need to be
 * accessed. Non-file form fields are parsed completely and their values are
 * stored until {@link #moveNextFormField()} is invoked, at which
 * point they are overwritten by the next form field. When a {@code file}
 * form type is encountered, its headers and attributes are read, but parsing is
 * stalled at the beginning of the file data. The file data must be read in
 * portions as it is received on the {@code InputStream}.
 * </p><p>
 * File types are recognized by the inclusion of a "filename" parameter in their
 * Content-Disposition headers; this is a kludge because clients are not
 * required to provide such a parameter.
 * </p>
 */
public class MultipartMimeFormParser {

    /**
     * This sets an approximate limit for number of bytes that should be read by
     * a call to readLine() before an exception is thrown. readLine() is
     * intended to read header lines and lines inputted into text fields on a
     * form and therefore reading large pieces of data is inappropriate.
     */
    private static final int MAX_READ_SIZE = 1048576;
    
    private final static int DEFAULT_READ_SIZE = 4096;
    
    /**
     * The default Charset assumed for text content without a specified
     * charset
     */
    private static final Charset DEFAULT_CHARSET
            = Charset.forName("ISO-8859-1");

    /** a MIME delimiter */
    private static final byte[] CRLF = new byte[] { 13, 10 };

    /**
     * The stream containing the POST data, taken from the HttpServletRequest
     * object during the constructor.
     */
    private final InputStream inputStream;
    
    /**
     * The Charset assumed for text content without a specified charset
     */
    private final Charset charset;

    /**
     * Multipart encoding defines a boundary at the beginning and that boundary
     * is then used to delimit the form field value. It is stipulated that the
     * boundary is chosen so that it is not contained within any of the data
     * fields. {@code fieldDelimiter} is set to be the MIME boundary during the
     * constructor and is used to determine the end of the bytes that comprise
     * the current form field value.
     */
    private final byte[] fieldDelimiter;

    /**
     * This {@code Map} contains values for the <i>current form field</i>. It
     * maps header names and attribute names to their respective values. For
     * non-file form fields, it also contains a mapping of "value" to the value
     * of the {@code current form field}, parsed as a UTF-8-encoded String.
     * This Map is populated at each successful call to
     * {@code moveNextFormField()}.
     */
    private Map<String, String> currentFieldAttributes;

    /**
     * This variables is set to false at the beginning of
     * {@code moveNextFormField()} and is set to true when the
     * {@code fieldDelimiter} has been reached.
     */
    private boolean currentFieldNoMoreBytes;

    /**
     * Is set to true when the boundary signalling the end of posted data has
     * been found.
     */
    private boolean endOfFormReached;
    
    private boolean atBeginningOfStream;

    /**
     * Initializes a {@code MultipartMimeFormParser} object to parse a specified
     * {@code HttpServletRequest}.
     * 
     * @param request the HttpServletRequest object from which the inputStream
     *        is taken and the boundary is extracted. Once this constructor is
     *        called, the InputStream from the request object is relinquished to
     *        MultipartMimeFormParser.
     * @throws IllegalArgumentException if the current request does not have a
     *         parsable encoding type.
     * @throws IOException if an I/O error occurs while reading the general
     *         metadata of the request entity
     */
    public MultipartMimeFormParser(HttpServletRequest request) 
            throws IOException {
        this(request, DEFAULT_CHARSET);
    }
    
    /**
     * Initializes a {@code MultipartMimeFormParser} object to parse a specified
     * {@code HttpServletRequest}.
     * 
     * @param request the HttpServletRequest object from which the inputStream
     *        is taken and the boundary is extracted. Once this constructor is
     *        called, the InputStream from the request object is relinquished to
     *        MultipartMimeFormParser.
     * @param  charset the {@code Charset} to use as the default for form fields
     *         that do not otherwise specify one
     * @throws IllegalArgumentException if the current request does not have a
     *         parsable encoding type.
     * @throws IOException if an I/O error occurs while reading the general
     *         metadata of the request entity
     */
    public MultipartMimeFormParser(HttpServletRequest request, Charset charset)
            throws IOException {
        if (!isParsable(request)) {
            throw new IllegalStateException();
        }
        this.inputStream = new BufferedInputStream(request.getInputStream());

        /*
         * Details of content (media) types in HTTP are provided in section 3.7
         * of the HTTP spec, RFC 2616.  The details of the multipart/form-data
         * media type are described in RFC 2388, and the general characteristics
         * of multipart media types are described in RFC 2046.  In brief,
         * however, the multipart/form-data media type takes one required
         * parameter: a multipart boundary delimiter. The Content-Type header
         * will therefore look something like this:
         * 
         * Content-Type: multipart/form-data; boundary=exampleboundary
         *
         * The remainder of the form post will be divided using delimiters
         * constructed from the given boundary in the following manner:
         * 
         * CRLF + "--" + boundary
         * 
         * With the last part having a variant closing delimiter constructed so:
         * 
         * CRLF + "--" + boundary + "--"
         * 
         * In either case, the remainder of the line is expected to be empty,
         * though whitespace between the delimiter and the CRLF or EOF is
         * ignored.
         */

        String contentType = request.getHeader("Content-Type");
        Map<String, String> contentTypeParams;
        String boundaryString;
        
        if (contentType == null) {
            throw new IOException(
            "No Content-Type specified; expected multipart/form-data");
        }
        contentTypeParams
                = parseContentType(contentType, "multipart/form-data");
        boundaryString = contentTypeParams.get("boundary");
        
        try {
            if (boundaryString.length() == 0) {
                throw new IOException("Multipart boundary is empty");
            }
        } catch (NullPointerException npe) {
            throw new IOException("No multipart boundary specified");
        }
        
        ByteArrayOutputStream boundaryBuilder = new ByteArrayOutputStream();
        
        // The beginning of the boundary is CRLF:
        boundaryBuilder.write(CRLF);
        
        // next is a mandatory pair of hyphens
        boundaryBuilder.write((byte) '-');
        boundaryBuilder.write((byte) '-');
        
        /*
         * finally the boundary string; it should be all 7-bit ASCII, but we
         * encode with UTF-8 to be sure it doesn't fail
         */
        boundaryBuilder.write(boundaryString.getBytes("UTF-8"));

        fieldDelimiter = boundaryBuilder.toByteArray();
        currentFieldAttributes = null;
        endOfFormReached = false;
        atBeginningOfStream = true;
        currentFieldNoMoreBytes = false;
        this.charset = charset;
    }

    /**
     * This function can be used to determine whether a particular request
     * posted data using "multipart/form-data" encoding. If so, it must be
     * decoded using this class.
     * 
     * @param request the HttpServletRequest object for the servlet or JSP is
     *        required but is not corrupted or altered.
     * @return true if the current request used multipart encoding and can be
     *         parsed by this class, otherwise false.
     */
    public static boolean isParsable(HttpServletRequest request) {
        String content = request.getHeader("Content-Type");
        
        if (content == null) {
            return false;
        } else {
            return (content.toLowerCase().indexOf("multipart/form-data") >= 0);
        }
    }

    /**
     * Reads the data from {@code inputStream} until it reaches the next
     * boundary, storing the information within the {@code currentFieldAttribute
     * Map}. If the data is from an embedded file, parsing stops until calls are
     * made to {@code getFileBytes()}.
     * 
     * @return true when the POSTed data for the next form field has been
     *         parsed; false if the end of the POST data has been reached.
     * @throws IllegalStateException if there is currently an incompletely
     *         retrieved file or if the constructor failed.
     * @throws IOException if there is an error parsing Mime header fields or an
     *         error reading the POST data.
     */
    public boolean moveNextFormField() throws IllegalStateException,
            IOException {
        if (!currentFieldNoMoreBytes) {
            /*
             * Beginning of stream, or a file field that has not been [fully]
             * read; drain the data up to and including the field delimiter
             */
            while (readBytes(inputStream, DEFAULT_READ_SIZE, fieldDelimiter,
                    true) != null) {
                // do nothing
            }
        } else if (endOfFormReached) {
            return false;
        }

        /*
         * Read the rest of the latest boundary-containing line and determine
         * whether it was a final boundary The signal for the end of the form is
         * the boundary followed by a pair of hyphens and a CRLF.
         */
        String end = readLine(CRLF, "US-ASCII");
        
        if (end.startsWith("--")) {
            endOfFormReached = true;
            currentFieldAttributes = null;
            return false;
        } else if (end.trim().length() > 0) {
            throw new IOException("Extraneous data on multipart boundary line");
        } else {
            currentFieldAttributes = new HashMap<String, String>();
        }

        /*
         * Read the body part headers, accounting for multiline headers, and
         * record the data of interest in the currentFieldAttributes map
         */
        for (String currentLine = readLine(CRLF, "US-ASCII").trim(), nextLine;
                !currentLine.equals("");
                currentLine = nextLine.trim()) {
            nextLine = readLine(CRLF, "US-ASCII");
            
            while (nextLine.startsWith(" ") && (nextLine.trim().length() > 0)) {
                currentLine = currentLine.concat(" ").concat(nextLine.trim());
                nextLine = readLine(CRLF, "US-ASCII");
            }
            
            processHeader(Header.parse(currentLine));
        }

        /*
         * If not a file, read the part's body into an attribute named "value",
         * assuming UTF-8 encoding
         */
        if (getFileName() == null) {
            String fieldCharset = currentFieldAttributes.get("charset");
            String value = readLine(fieldDelimiter, 
                    (fieldCharset != null) ? fieldCharset.toUpperCase()
                            : charset.name());
            
            currentFieldAttributes.put("value", value);
            currentFieldNoMoreBytes = true;
        } else {
            currentFieldNoMoreBytes = false;
        }
        
        return true;
    }

    /**
     * @return a {@code String} containing the MIME content-type of the last
     *         form field read by moveNextFormField().
     * @throws IllegalStateException if there have been no calls to
     *         moveNextFormField() of if the last such call returned false.
     */
    public String getCurrentFieldContentType() {
        if (currentFieldAttributes == null) {
            throw new IllegalStateException();
        } else {
            String type = currentFieldAttributes.get("content-type");
            
            return ((type == null) ? "text/plain" : type);
        }
    }

    /**
     * @return the name of the field for the form whose data was posted
     * @throws IllegalStateException if there have been no calls to
     *         moveNextFormField() or if the last such call returned false.
     */
    public String getCurrentFieldName() {
        if (currentFieldAttributes == null) {
            throw new IllegalStateException();
        }
        return currentFieldAttributes.get("name");
    }

    /**
     * @return the value posted into the current input field if it was not an
     *         input field of type {@code file}.
     * @throws IllegalStateException if there have been no calls to
     *         moveNextFormField() or if the last such call returned false.
     */
    public String getCurrentFieldValue() {
        if (getFileName() != null) {
            throw new IllegalStateException();
        }
        return currentFieldAttributes.get("value");
    }

    /**
     * @return the original filename of a POSTed file, or null if the current
     *         form field is not of type 'file'.
     * @throws IllegalStateException if there have been no calls to
     *         moveNextFormField() or if the last such call returned false.
     */
    public String getFileName() {
        if (currentFieldAttributes == null) {
            throw new IllegalStateException();
        }
        return currentFieldAttributes.get("filename");
    }

    /**
     * Get a portion of an attached file. The bytes returned are decoded into
     * those of the original file.
     * 
     * @param maxLength sets the limit for the maximum number of bytes to return
     *        from this call.
     * @return an array of bytes or null to signify that there are no bytes to
     *         be read.
     * @throws IllegalStateException if the current form field is not a file or
     *         if there have been no successful calls to moveNextFormField()
     * @throws IOException on I/O error
     */
    public byte[] readCurrentFieldValueAsBytes(int maxLength)
            throws IOException {
        if (getFileName() == null) {
            throw new IllegalStateException();
        } else if (currentFieldNoMoreBytes) {
            return null;
        } else {
            byte[] returnValue
                    = readBytes(inputStream, maxLength, fieldDelimiter, false);
            
            if (returnValue == null) {
                currentFieldNoMoreBytes = true;
            }
            
            return returnValue;
        }
    }

    /**
     * An internal function that returns a a String interpretation of the bytes
     * from the configured input stream, up to the next occurrence of the
     * user-specified delimiter (which is not included in the result). The
     * stream is advanced to the byte following the delimiter.
     * 
     * @param delimiter a byte sequence marking the end of the desired input
     * @param cs the name of the charset with which to decode the bytes
     * @return a String representation of the bytes on the inputStream up to the
     *         next CRLF.
     * @throws IOException if the end of the stream is detected or if a large
     *         amount of bytes has been read
     */
    private String readLine(byte[] delimiter, String cs) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte buffer[];
        int blockSize = 256;
        
        for (;;) {
            buffer = readBytes(inputStream, blockSize, delimiter, true);
            
            if (buffer == null) {
                break;
            } else {
                os.write(buffer);
                blockSize *= 2;
                if (blockSize > (MAX_READ_SIZE / 2)) {
                    throw new IOException();
                }
            }
        }
        
        return os.toString(cs);
    }

    /**
     * A general function to read available bytes from an {@code InputStream}.
     * Bytes are read until the delimiter is detected, at which point all bytes
     * read up to the delimiter are returned and the delimiter is skipped.
     * 
     * @param is the InputStream from which to read; must support marking
     *        (see {@code InputStream.markSupported()})
     * @param maxBytesToRead limits the number of bytes to be read (and
     *        returned), or if set to {@code NO_LIMIT} indicates that all
     *        available bytes up to the delimiter (or premature EOF) should be
     *        read.
     * @param delimiter when the delimiter is detected, a
     *        {@code ByteArrayOutputStream} containing the bytes up to, but not
     *        including the delimiter is returned.
     * @param blockUntilLimitReached indicates that the function should not
     *        return until all of the requested bytes are returned or the end of
     *        stream is detected (may have to block waiting for input).
     * @return a {@code byte[]} containing the bytes that were read, or
     *         {@code null} after the delimiter has been detected (and consumed)
     * @throws IOException if there is an exception while reading from
     *         {@code inputStream}, or if end-of-stream is reached before the
     *         delimiter is encountered.
     */
    private byte[] readBytes(InputStream is, int maxBytesToRead,
            byte delimiter[], boolean blockUntilLimitReached)
            throws IOException {
        if (delimiter.length < 1) {
            // the delmiter must be at least 1 byte long
            throw new IllegalArgumentException();
        }
        if (maxBytesToRead < delimiter.length) {
            // must request enough bytes to fully read the delimiter
            throw new IllegalArgumentException();
        }
        if (!is.markSupported()) {
            // we depend on mark()ing and reset()ing the stream
            throw new IllegalArgumentException(
                    "The InputStream does not support marking");
        }
    
        /*
         * The currentBlockSize is set to the desired number of bytes to read
         * or the number of available bytes. (and must be at least as long as
         * the delimiter, for convenience)
         */
        int currentBlockSize = Math.max(delimiter.length,
                (blockUntilLimitReached ? maxBytesToRead
                        : Math.min(maxBytesToRead, is.available())));
        byte currentBlock[] = new byte[currentBlockSize];
        int countBytesRead = 0;
    
        is.mark(currentBlockSize);
    
        /*
         * Read the minimum number of bytes from the input stream, blocking as
         * necessary. The minimum number of bytes to read is equal to the length
         * of the field delimiter. We enforce this restriction to guarantee that
         * field delimiters are detected properly, even when they otherwise
         * might have straddled a block boundary.
         */
        do {
            int bytesReadThisTime = is.read(currentBlock, countBytesRead,
                    delimiter.length - countBytesRead);
            if (bytesReadThisTime == -1) {
                /*
                 * End-of-stream reached unexpectedly -- we should have at least
                 * been able to read delimiter.length bytes.
                 */
                throw new IOException();
            }
            countBytesRead += bytesReadThisTime;
        } while (countBytesRead < delimiter.length);
    
        /*
         * Read more bytes from the input stream, up to currentBlockSize.
         * Because currentBlockSize was selected carefully, this loop
         * will not block unless blockUntilLimitReached was true.
         */
        do {
            int bytesReadThisTime = is.read(currentBlock, countBytesRead,
                    currentBlockSize - countBytesRead);
            if (bytesReadThisTime == -1) {
                // End-of-stream reached; stop reading.
                break;
            }
            countBytesRead += bytesReadThisTime;
        } while (countBytesRead < currentBlockSize);
    
        /*
         * Scan the buffer for a field delimiter and return as much of the
         * buffer as possible to the caller.
         */
        if (atBeginningOfStream) {
            atBeginningOfStream = false;

            if (delimiter == fieldDelimiter) {
                
                /*
                 * test for the field delimiter appearing at the very from of
                 * the stream, without a leading CRLF
                 */
                byte[] altDelimiter = new byte[delimiter.length - 2];
                System.arraycopy(delimiter, 2, altDelimiter, 0,
                        altDelimiter.length);
                
                if (firstPossibleMatch(currentBlock, altDelimiter.length,
                        altDelimiter) == 0) {
                    is.reset();
                    is.skip(altDelimiter.length);
                    return null;
                }
            }
        }
        
        /*
         * Handle the usual case
         */
        int match = firstPossibleMatch(currentBlock, countBytesRead, delimiter);
        
        if (match == -1) {
            // no match, return all bytes read.
            if (countBytesRead == currentBlockSize) {
                
                /*
                 * We read exactly the number of bytes expected; return the
                 * whole currentBlock's buffer.
                 */
                return currentBlock;
            } else {
                
                /*
                 * We read fewer bytes than expected; return a subset of the
                 * currentBlock's buffer.
                 */
                byte buf[] = new byte[countBytesRead];
                
                System.arraycopy(currentBlock, 0, buf, 0, countBytesRead);
                
                return buf;
            }
        } else if (match == 0) {
            
            /*
             * The bytes we read began with the field delimiter. Signal
             * end-of-field to the caller and position the inputstream's pointer
             * at the start of the next field. Do not return any data this
             * go-round, since the inputstream's pointer had already been
             * positioned at the end-of-field at the time we were invoked.
             */
            is.reset();
            is.skip(delimiter.length);
            
            return null;
        } else {
            
            /*
             * A possibly incomplete delimiter has been detected, which might
             * indicate that we've read the last data in the current field.
             * Return all bytes prior to the (possible) delimiter and position
             * the inputstream's pointer at its start. A subsequent call to this
             * function will determine whether the delimiter is genuine (i.e.
             * complete) or not.
             */
            byte upToMatch[] = new byte[match];
            
            is.reset();
            is.skip(match);
            System.arraycopy(currentBlock, 0, upToMatch, 0, match);
            
            return upToMatch;
        }
    }

    /**
     * A static helper function that returns the index of the first instance of
     * the delimiter within the buffer whether or not the buffer ended before
     * the entire delimiter was matched.
     * 
     * @param buffer a buffer of bytes
     * @param length the number of bytes in the buffer that are valid for
     *        comparison.
     * @param delimiter the array of bytes being searched for within the buffer
     * @return the index where the match was found, or -1 if no match was found
     */
    private int firstPossibleMatch(byte buffer[], int length,
            byte delimiter[]) {
        assert (length <= buffer.length);
        
        for (int b = 0; b < length; b++) {
            boolean possibleMatchDetected = true;
            
            for (int d = 0; (d < delimiter.length) && (d < (length - b)); d++) {
                if (buffer[b + d] != delimiter[d]) {
                    possibleMatchDetected = false;
                    break;
                }
            }
            if (possibleMatchDetected) {
                return b;
            }
        }
        
        // no match detected
        return -1;
    }

    /**
     * Processes a body part header parsed from the multipart response,
     * possibly updating the {@link #currentFieldAttributes} with some of the
     * results
     * 
     * @param header the {@code Header} containing the information to process
     * @throws IOException if an invalid header value is detected
     */
    private void processHeader(Header header) throws IOException {
        /*
         * 
         */
        if ("content-disposition".equals(header.getName())) {
            StringBuilder sb = new StringBuilder(header.getValue());
            String simpleValue = parseToken(sb).toLowerCase();
            
            if ("form-data".equals(simpleValue)) {
                Map<String, String> parameters = parseParameters(sb);
                String fieldName = parameters.get("name");
                String fileName = parameters.get("filename");
                
                if ((fieldName == null) || (fieldName.length() == 0)) {
                    throw new IOException("empty or missing field name");
                } else {
                    currentFieldAttributes.put("name", fieldName);
                }
                
                if (fileName != null) {
                    currentFieldAttributes.put("filename", fileName);
                    
                    if (!currentFieldAttributes.containsKey("content-type")) {
                        currentFieldAttributes.put(
                                "content-type", "application/octet-stream");
                    }
                }
            } else {
                throw new IOException("Expected content disposition form-data,"
                        + " got " + simpleValue);
            }
        } else if ("content-type".equals(header.getName())) {
            String fieldCharset
                    = parseContentType(header.getValue(), null).get("charset");

            if (fieldCharset != null) {
                currentFieldAttributes.put("charset", fieldCharset);
            }
            currentFieldAttributes.put("content-type", header.getValue());
        }
    }
    
    /**
     * <p>
     * Parses the value and parameters of a Content-Type header, verifying that
     * the content-type is multipart/form-data and providing a Map from
     * parameter names to their values. This method recognizes the full grammar
     * specified for this header by RFC 2616.
     * </p><p>
     * Note: RFC 2045 (MIME Part 1) defines the Content-Type header such that it
     * explicitly may contain comments in the format specified by RFC 822,
     * whereas RFC 2616 (HTTP 1.1) provides an explicit grammar for Content-Type
     * headers used in conjunction with HTTP, and this grammar does not
     * accommodate comments. Even though multipart form bodies may be carried
     * over HTTP, however, the internal body part headers they carry are not
     * HTTP entity headers, hence not subject to RFC 2616. Moreover, supporting
     * comments even in HTTP headers is more likely to accommodate buggy clients
     * than to break correct ones, so this method parses and ignores comments in
     * content-type values, BUT ONLY WHERE WHITESPACE IS OTHERWISE EXPECTED /
     * ALLOWED.
     * </p>
     * 
     * @param contentTypeValue the full, unfolded field value of a
     *        Content-Type header
     * @param expectedType the expected base content type (e.g. text/plain), or
     *        {@code null} if any type is permissible
     * @return a Map from String parameter names to corresponding parameter
     *         values. Per RFC 2616, all characters in both Strings will be
     *         representable in 7-bit ASCII (they're even more restricted than
     *         that, in fact). Parameter names are case-insensitive; this class
     *         converts them to lowercase during parsing.
     * @throws IOException if the specified content type is syntactically
     *         invalid or isn't a parameterization of the expected content type
     */
    private Map<String, String> parseContentType(String contentTypeValue,
            String expectedType)
            throws IOException {
        StringBuilder buf = new StringBuilder(contentTypeValue);
        
        // Read and test the content-type (should be multipart/form-data) 
        consumeWhitespace(buf);
        if (buf.length() == 0) {
            throw new IOException("Empty Content-Type");
        } else {
            String type = parseToken(buf).toLowerCase();
            
            if (type.length() == 0) {
                throw new IOException("Invalid Content-Type");
            } else if ((buf.length() < 2) || (buf.charAt(0) != '/')
                    || isSeparator(buf.charAt(1))) {
                throw new IOException("No Content-Type subtype");
            } else {
                String subtype;
                
                buf.deleteCharAt(0);
                subtype = parseToken(buf).toLowerCase();
                type = type + '/' + subtype;
                
                if (subtype.length() == 0) {
                    throw new IOException("No Content-Type subtype");
                } else if ((expectedType != null)
                        && !expectedType.equalsIgnoreCase(type)) {
                    throw new IOException("Wrong Content-Type ("
                            + type + "); expected " + expectedType);
                }
            }
        }
        
        /*
         * parse out all the parameters; typically, though, there is only one:
         * the multipart boundary
         */
        return parseParameters(buf);
    }
    
    /**
     * Parses the provided text as a sequence of parameters, each one (including
     * the first) precided by a semicolon and having the form
     * <i>name</i>=<i>value</i>.  The value may or may not be enclosed in
     * quotation marks.
     * 
     * @param buf a {@code StringBuilder} containing the parameters; its content
     *        is consumed by this method 
     * @return a {@code Map} from the parsed parameter names to their associated
     *         values
     * @throws IOException
     */
    private Map<String, String> parseParameters(StringBuilder buf)
            throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        
        /*
         * parse out all the parameters; typically, though, there is only one:
         * the multipart boundary
         */
        for (consumeWhitespace(buf); buf.length() > 0; consumeWhitespace(buf)) {
            if (buf.charAt(0) != ';') {
                throw new IOException("Malformed parameter list -- missing a "
                        + "parameter delimiter");
            } else {
                String name;
                String value;
                
                buf.deleteCharAt(0);
                consumeWhitespace(buf);
                name = parseToken(buf);

                if ((buf.length() == 0) || (buf.charAt(0) != '=')) {
                    throw new IOException("Malformed Content-Type parameter;"
                            + " expected an equals sign (=)");
                } else {
                    buf.deleteCharAt(0);
                }
                
                if ((buf.length() > 0) && (buf.charAt(0) == '"')) {
                    buf.deleteCharAt(0);
                    value = parseQString(buf);
                } else {
                    value = parseToken(buf);
                }
                
                paramMap.put(name.toLowerCase(), value);
                consumeWhitespace(buf);
            }
        }

        // Return the parameter name/value Map
        return paramMap;
    }
    
    private String parseToken(StringBuilder chars) {
        StringBuilder sb = new StringBuilder();
        int pos;
        
        for (pos = 0; pos < chars.length(); pos++) {
            char c = chars.charAt(pos);
            
            if ((c < '\u0020') || (c > '\u007e') || isSeparator(c)) {
                break;
            } else { 
                sb.append(c);
            }
        }
        
        chars.delete(0, pos);
        
        return sb.toString();
    }
    
    private boolean isSeparator(char c) {
        switch (c) {
            case '(':
            case ')':
            case '<':
            case '>':
            case '@':
            case ',':
            case ';':
            case ':':
            case '\\':
            case '"':
            case '/':
            case '[':
            case ']':
            case '?':
            case '=':
            case '{':
            case '}':
            case ' ':
            case '\t':
                return true;
            default:
                return false;
        }
    }
    
    private String parseQString(StringBuilder chars) throws IOException {
        StringBuilder sb = new StringBuilder();
        
        scan_qstring:
        for (int pos = 0; pos < chars.length(); pos++) {
            char c = chars.charAt(pos);
            
            if ((c < '\u0020') || (c > '\u007e')) {
                throw new IOException("Invalid character in header token");
            }
            switch (c) {
                case '"':
                    chars.delete(0, pos + 1);
                    break scan_qstring;
                case '\\':
                    try {
                        sb.append(chars.charAt(++pos));
                    } catch (IndexOutOfBoundsException ioobe) {
                        throw new IOException("Incomplete quoted pair in "
                                + "Content-Type value");
                    }
                    break;
                default:
                    sb.append(c);
            }
        }
        
        return sb.toString();
    }
    
    private void consumeWhitespace(StringBuilder chars) {
        int commentDepth = 0;
        
        for (int pos = 0; pos < chars.length(); pos++) {
            switch (chars.charAt(pos)) {
                case '(':  // An opening comment delimiter
                    commentDepth++;
                    break;
                case ')':  // A closing comment delimiter -- when in a comment
                    if (commentDepth > 0) {
                        commentDepth--;
                        break;
                    } else {
                        
                        // The current character is the first non-whitespace
                        chars.delete(0, pos);
                        return;
                    }
                case ' ':   // standard whitespace
                case '\t':  // standard whitespace
                    break;
                case '\\':  // the quoting character -- when in a comment
                    if (commentDepth > 0) {
                        pos++; // ignore the next character (if any)
                        break;
                    }
                    
                    // fall through
                default:
                    if (commentDepth <= 0) {
                        
                        // The current character is the first non-whitespace
                        chars.delete(0, pos);
                        return;
                    }
            }
        }
        
        // The text was all whitespace
        chars.setLength(0);
    }
    
    private static class Header {
        private final static Pattern HEADER_PATTERN
                = Pattern.compile("([\u0021-\u007e&&[^:]]+):" +
                        "([\u0000-\u0009\u000b\u000c\u000e-\u007f]*)");
        private final String name;
        private final String value;
        
        /**
         * Initializes a {@code Header} with the specified name and value
         * 
         * @param name
         * @param rawValue
         */
        Header(String name, String rawValue) {
            this.name = name;
            this.value = rawValue;
        }
        
        /**
         * @return the {@code name} {@code String}
         */
        public String getName() {
            return name;
        }

        /**
         * @return the {@code value} {@code String}
         */
        public String getValue() {
            return value;
        }

        /**
         * Parses the specified string as a header field, per the general syntax
         * specified by RFC 822, section 3.1.2.  The header name is
         * canonicalized to lowercase, and any leading or trailing whitespace is
         * removed from the header value 
         * 
         * @param headerString the {@code String} to parse
         * @return the parsed {@code Header}
         * @throws IOException if the specified string does not conform to the
         *         RFC 822 format
         */
        public static Header parse(String headerString) throws IOException {
            Matcher m = HEADER_PATTERN.matcher(headerString);
            
            if (m.matches()) {
                return new Header(m.group(1).toLowerCase(), m.group(2).trim());
            } else {
                throw new IOException("malformed header");
            }
        }
    }
}
