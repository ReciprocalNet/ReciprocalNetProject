/*
 * Reciprocal Net project
 * 
 * Base64OutputStream.java
 *
 * 24-Sep-2002: ekoperda wrote first draft
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package
 *              to org.recipnet.common
 * 25-Apr-2006: jobollin reworked the class to direct its output to a Writer
 *              instead of an OutputStream; reformatted the source; updated
 *              docs; changed the internal CRLF sequence to really represent
 *              CRLF; removed unnecessary method write(byte[]); factored some
 *              duplicate code out into new method writeChars()
 */

package org.recipnet.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * <p>
 * An output stream that produces a BASE64-encoded version of the data written
 * to it, directed to a provided character stream.  The output character stream
 * can be encoded via the US-ASCII charset (or a superset of that charset such
 * as UTF-8 or any of the ISO-8859 family of charsets) to obtain a byte
 * representation of the original bytes that is suitable for transmission via a
 * 7-bit ASCII medium such as SMTP.
 * </p><p>
 * This class is designed to support usage scenarios where only a portion of
 * some larger output needs to be BASE64 encoded.  This is a bit tricky,
 * however, because instances must buffer input bytes into triples in order to
 * encode them.  The mechanisms employed to handle this situation are described
 * in the documentation of the {@link #flush()} method.
 * </p> 
 */
public class Base64OutputStream extends OutputStream {
    
    /**
     * A table of the characters of the BASE64 alphabet
     */
    private static final char [] BASE64CHARS = {
        	'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    /** the padding byte as defined by BASE64 */
    private static final char PAD = '=';
	
    /** the linebreak sequence as defined by BASE64 */
    private static final char[] CRLF = { (char) 13, (char) 10 };

    /**
     * The {@code Writer} to which the encoded output data should be directed
     */
    private final Writer outputChars;
    
    /** The current "flushing mode" - true if flush() implies end-of-stream  */
    private boolean flushIsEnd = false;
	
    /** 32-bit buffer that store bytes before encoding */
    private int buffer = 0; 
	
    /** the number of bytes currently in the buffer, waiting to be encoded */
    private int inWait = 0;
    
    /** number of chars written to the output since the most recent linebreak */
    private int charsWritten = 0;

    /** a byte array to store the encoded bytes */
    private final char[] encodedBytes = new char[4];

    /**
     * Initializes a new {@code Base64OutputStream} with the specified
     * {@code Writer} to which the output should be directed and the specified
     * "flushing mode" flag.
     * 
     * @param out a {@code Writer} to which the output should be directed
     * @param isFlushEnd {@code true} if {@link #flush()} should signal this
     *        {@code Base64OutputStream} to finish its encoding in addition to
     *        flushing the underlying {@code Writer}; {@code false} if
     *        {@code flush()} should just flush the {@code Writer} (leaving up
     *        to two unwritten bytes in the encoding buffer)
     * @throws NullPointerException if {@code out} is {@code null}
     * 
     * @see #flush()
     */
    public Base64OutputStream(Writer out, boolean isFlushEnd) {
        if (out == null) {
            throw new NullPointerException();
        }
        
        outputChars = out;
        flushIsEnd = isFlushEnd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(int b) throws IOException {
        buffer = (buffer << 8) + (b & 0xFF);
        if (++inWait == 3) {
            writeBytes();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void write(byte[] b, int start, int length) throws IOException {
        if ((start < 0) || (length < 0) || ((start + length) > b.length)) {
            throw new IndexOutOfBoundsException();
        }
        
        if (inWait > 0) {
            // Start at the end of the current (nonempty) buffer
            while ((inWait < 3) && (length > 0)) {
                buffer = (buffer << 8) + (b[start] & 0xff);
                start++;
                inWait++;
                length--;
            }
            if (inWait == 3) {
                writeBytes();
            }
        }
        
        // write as many complete buffers full as can be formed
        for (; length >= 3; length -= 3) {
            
            // inWait is always zero at this point
            
            buffer =  ((b[start]     & 0xff) << 16)
                    + ((b[start + 1] & 0xff) <<  8)
                    +  (b[start + 2] & 0xff);
            start += 3;
            writeBytes();
        }
        
        // Write the last one or two bytes if necessary
        for (; length > 0; length--) {
            buffer = (buffer << 8) + (b[start] & 0xff);
            start++;
            inWait++;
            
            assert (inWait < 3);
        }
    }

    /**
     * {@inheritDoc}. This version's behavior with respect to unwritten bytes
     * in its encoding buffer differs based on the current value of the
     * {@code flushIsEnd} property. If that property has the value {@code true}
     * then any unwritten bytes accumulated in the encoding buffer will be
     * appropriately padded and written to the underlying {@code Writer} before
     * that {@code Writer} is flushed; if it has the value {@code false} then
     * the current encoding buffer is bypassed (and the underlying
     * {@code Writer} is flushed). In the former case, all bytes written to this
     * stream are assured of being represented on the underlying {@code Writer},
     * but no more data should be written to this stream. In the latter case, up
     * to two bytes previously written to this stream may remain cached in this
     * stream, but this stream may continue to be used normally. This behavior
     * allows the BASE64 encoding process to be completed correctly without
     * closing the underlying {@code Writer}, if that should be desired.  Users
     * should be aware, however, that the end of the BASE64 data is not
     * necessarilly inherently terminated in any way that would be recognized
     * by a BASE64 decoder. 
     */
    @Override
    public void flush() throws IOException {
        if (flushIsEnd) {
            completeEncoding();
        }
        outputChars.flush();
    }

    /**
     * {@inheritDoc}.  This version also closes the underlying {@code Writer}.
     * Additional bytes may be written to the {@code Writer} before its closure.
     */
    @Override
    public void close() throws IOException {
        completeEncoding();
        outputChars.close();
    }

    /**
     * Gets the value of this {@code Base64OutputStream}'s 'flushIsEnd'
     * property, indicating whether or not flushing this stream should cause it
     * to assume that the end of the output has been reached.
     * 
     * @return {@code true} if flushing this stream is interpreted as signaling
     *         the end of the output; {@code false} if not
     *         
     * @see #flush()
     */
    public boolean getFlushIsEnd() {
        return flushIsEnd;
    }

    /**
     * Sets the value of this {@code Base64OutputStream}'s 'flushIsEnd'
     * property, indicating whether or not flushing this stream should cause it
     * to assume that the end of the output has been reached.
     * 
     * @param flushIsEnd {@code true} if flushing this stream should be
     *        interpreted as signaling the end of the output; {@code false} if
     *        not
     * @see #flush()
     */
    public void setFlushIsEnd(boolean flushIsEnd) {
        this.flushIsEnd = flushIsEnd;
    }

    /**
     * Re-encodes the three bytes packed into the lower-order 24 bits of the
     * {@link #buffer} into four characters according to the BASE64 encoding,
     * and writes the resulting characters to the underlying {@code Writer}.
     * Resets the count of buffered characters and clears the buffer.
     * 
     * @throws IOException if the underlying {@code Writer} throws this
     *         exception
     */
    private void writeBytes() throws IOException {
        encodedBytes[3] = BASE64CHARS[ buffer &  0x3F];
        encodedBytes[2] = BASE64CHARS[(buffer & (0x3F <<  6)) >>>  6];
        encodedBytes[1] = BASE64CHARS[(buffer & (0x3F << 12)) >>> 12];
        encodedBytes[0] = BASE64CHARS[(buffer & (0x3F << 18)) >>> 18];
        
        writeChars();
    }

    /**
     * Finishes the encoding by padding any bytes currently held in the buffer
     * (as necessary) and outputting the character representation of the result.
     * Also resets the count of buffered characters to zero, though the user
     * <em>shouldn't</em> write any more data via this stream.  No effect if
     * there aren't currently any buffered bytes.
     * 
     * @throws IOException if the underlying {@code Writer} throws this
     *         exception
     */
    private void completeEncoding() throws IOException {
        encodedBytes[3] = PAD;
        
        switch (inWait) {
            case 0:
                return;
            case 1:
                encodedBytes[2] = PAD;
                buffer <<= 4;
                break;
            case 2:
                encodedBytes[2] = BASE64CHARS[(buffer << 2) & 0x3F];
                buffer >>= 4;
                break;
            default:
                assert false : "Invalid number of buffered bytes";
        }
        
        encodedBytes[1] = BASE64CHARS[ buffer &  0x3F];
        encodedBytes[0] = BASE64CHARS[(buffer & (0x3F << 6)) >> 6];
        
        writeChars();
    }
    
    /**
     * Writes the base64-encoded data currently buffered in the
     * {@link #encodedBytes} array to the underlying {@code Writer}, and
     * outputs a CR/LF sequence if the number of characters written on the
     * current line of output (including those written during the current
     * invocation of this method) is at least 76.
     * 
     * @throws IOException if the write to the {@code Writer} throws this
     *         exception
     */
    private void writeChars() throws IOException {
        inWait = 0;
        buffer = 0;
        outputChars.write(encodedBytes);
        
        charsWritten += 4;
        if (charsWritten >= 76) {
            outputChars.write(CRLF);
            charsWritten = 0;
        }
    }
}
