/*
 * Reciprocal Net project
 * 
 * Base64InputStream.java
 *
 * 24-Sep-2002: ekoperda wrote first draft
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package to
 *              the org.recipnet.common package
 * 25-Apr-2006: jobollin rewrote parts of this class to take its input from a
 *              Reader instead of from an InputStream; reformatted the source;
 *              updated docs; changed available() to a more sensible (i.e. less
 *              broken) implementation; updated reset() to throw IOException as
 *              specified by InputStream for subclasses that do not support
 *              marking; renamed method 'getStore()' to 'decodeBytes()' and
 *              reworked some of its details
 */

package org.recipnet.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * <p>
 * A {@code FilterInputStream} that reads BASE64-encoded binary data from an
 * underlying character stream and decodes it on the fly. This class handles the
 * flavor of BASE64 described by MIME; see <a
 * href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>, section 6.8, for
 * more details. As an extension, this version doesn't care about line lengths,
 * and (as specified by MIME) it ignores line terminators, so it equally well
 * handles BASE64 data that don't comply with MIME's limit of 76 characters per
 * line.
 * </p><p>
 * As allowed by the specification, the decoder in this class interprets equal
 * sign characters ('=') in the input as signaling the end of the encoded data,
 * though it will attempt to read a second equal sign where BASE64 indicates one
 * should be expected. It reads the required four data bytes at a time from the
 * underlying stream (plus any intervening bytes not in the BASE64 alphabet,
 * which, per specification, are ignored), and does not itself internally buffer
 * any additional data.  This means that an instance can be attached to an
 * underlying character stream to decode a (well-formed) BASE64-encoded segment,
 * then discarded without fear that any of the characters past the end of the
 * BASE64 segment will be lost.  On the other hand, for best performance it is
 * recommended that the {@code Reader} underlying this stream provide
 * memory buffering of some sort.
 * </p>
 */
public class Base64InputStream extends InputStream {

    /**
     * The {@code Reader} from which characters to be decoded should be read
     */
    private final Reader inputChars;
    
    /**
     * A buffer holding up to three decoded bytes at a time; this array is
     * updated by {@link #decodeBytes()} and read by {@link #read()}, and by
     * {@link #read(byte[], int, int)}
     */
    private final byte[] decodedBytes = new byte[3];

    /**
     * The index of the next byte to return from the {@link #decodedBytes}
     * buffer; this variable is initially zero, and is updated by
     * {@link #decodeBytes()}, by {@link #read()}, and by
     * {@link #read(byte[], int, int)}
     */
    private int pos = 0;

    /**
     * The number of valid bytes in the 3-byte {@link #decodedBytes} array; this
     * variable is initially zero, and is updated by {@link #decodeBytes()}
     */
    private int nos = 0;

    /**
     * A flag indicating whether the end of the underlying {@code Reader} has
     * been detected; this variable is initially {@code false}, and is updated
     * by {@link #decodeBytes()}.  Once toggled to {@code true}, the value of
     * this variable will not subsequently change.
     */
    private boolean endOfStream = false;

    /**
     * Initializes a new {@code Base64InputStream} with the specified underlying
     * stream.
     * 
     * @param  in the {@code Reader} from which this stream will obtain
     *         its data (which are assumed to be BASE64 encoded)
     * @throws NullPointerException if {@code in} is null
     */
    public Base64InputStream(Reader in) {
        if (in == null) {
            throw new NullPointerException();
        } else {
            inputChars = in;
        }
    }

    /**
     * {@inheritDoc}.  This version returns the number of bytes (<= 3) available
     * from the internal buffer of decoded bytes.  It may be possible to read
     * more than this without blocking on I/O, depending on how many and which
     * bytes are currently available from the underlying Reader without
     * blocking, but that can't be determined without actually attempting the
     * read.
     */
    @Override
    public int available() {
        return (nos - pos);
    }
    
    /**
     * {@inheritDoc}.  This version closes the underlying {@code Reader} and
     * discards any bytes remaining in the internal buffer
     */
    @Override
    public void close() throws IOException {
        pos = nos;
        inputChars.close();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int read() throws IOException {
        if (pos >= nos) {
            decodeBytes();  // will reset pos as approprate
        } 
        if (endOfStream) {
            // no additional bytes will have been read
            return -1;
        }
        
        return decodedBytes[pos++];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] target) throws IOException {
        return read(target, 0, target.length);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int read(byte[] target, int start, int length) throws IOException {
        int read;
        int toCopy;
        
        if ((start < 0) || (length < 0) || ((start + length) > target.length)) {
            throw new IndexOutOfBoundsException();
        } else if (length == 0) {
            return 0;
        } else if (endOfStream) {
            return -1;
        }
        
        // Start with the bytes already available in the buffer
        toCopy = Math.max(Math.min(length, nos - pos), 0);
        System.arraycopy(decodedBytes, pos, target, start, toCopy);
        pos += toCopy;
        read = toCopy;

        // Read as many full buffers of data as needed (possibly 0)
        read_by_full_buffer:
        while ((length - read) >= decodedBytes.length) {
            
            // The decoded byte buffer is always exhausted at this point
            
            decodeBytes();

            if (endOfStream) {
                // no additional bytes will have been read by decodeBytes()
                break read_by_full_buffer;
            }

            System.arraycopy(decodedBytes, 0, target, (start + read), nos);
            read += nos;
            pos = nos;
        }

        // If needed, read one more buffer of data to find the last bytes
        if (!endOfStream && (read < length)) {
            
            // will reset pos as a side effect:
            decodeBytes();
            
            if (!endOfStream) {
                toCopy = Math.min(length - read, nos);
                System.arraycopy(
                        decodedBytes, 0, target, (start + read), toCopy);
                pos += toCopy;
                read += toCopy;
            }
        }

        return (read > 0) ? read : -1;
    }

    /**
     * Reads a new packet of up to three encoded bytes (represented as four
     * characters of meaningful data, plus any intervening whitespace or filler
     * on the underlying character stream) and decodes them into the
     * {@link #decodedBytes} buffer, setting {@link #nos} to the number of valid
     * decoded bytes, and resetting {@link #pos} to zero. This method should
     * only be invoked when all the valid bytes of {@code decodedBytes} have
     * been consumed; otherwise data will be lost.
     * 
     * @throws IOException if the underlying stream throws this exception, or if
     *         the end of the underlying stream is encountered in the middle of
     *         a BASE64 packet
     */
    private void decodeBytes() throws IOException {
        int buffer = 0;
        int charsRead = 0;

        assert (pos >= nos) : "Data loss detected";
        
        // Reset the reading position in the decoded byte buffer
        pos = 0;
        
        /*
         * Get four bytes of encoded data, decode them, and pack the resulting
         * 6-bit values into the lower 24 bits of an int.
         */
        read_stream:
        while (charsRead < 4) {
            // Read one byte from the input stream
            int rawData = inputChars.read();
            int data;
            
            if (rawData == -1) {
                endOfStream = true;
                
                if (charsRead > 0) {
                    throw new IOException("Truncated BASE64 stream");
                } else {
                    nos = 0;
                    return;
                }
            }

            // Decode the byte
            data = decodeInt(rawData);
            if (data == -1) {
                // whitespace / illegal character read; nothing to do
            } else if (data == -2) {
                
                // padding read; this is valid only for the last 1 or 2 bytes
                
                if (charsRead < 2) {
                    throw new IOException(
                            "Invalid placement of '=' in the stream");
                } else if (charsRead == 2) {
                    
                    /*
                     * Find and consume the expected second padding byte
                     */
                    do {
                        rawData = inputChars.read();
                        if (rawData == -1) {
                            throw new IOException("Truncated BASE64 stream");
                        } else {
                            data = decodeInt(rawData);
                            if (data >= 0) {
                                throw new IOException(
                                        "Invalid placement of '=' in the stream");
                            }
                        }
                    } while (data != -2);
                }
                // nothing special to do if charsRead == 3
                
                break read_stream;
            } else {
                assert ((0 <= data) && (data < 64));
                
                /*
                 * A normal character. Take these six decoded bits and insert
                 * them on the least-significant side of our buffer.
                 */
                buffer = (buffer << 6) | data;
                charsRead++;
            }
        }
        
        /*
         * Break the 24 bits of the buffer int into three bytes, or possibly
         * fewer.
         */
        
        // compute the number of bytes to be decoded
        nos = (charsRead - 1);
        
        // shift off any padding bits
        buffer >>= ((charsRead * 6) - (nos * 8));
                
        // extract the bytes
        switch (nos) {
            case 3:
                decodedBytes[2] = (byte) (buffer & 0xFF);
                buffer >>= 8;
                
                // fall through
            case 2:
                decodedBytes[1] = (byte) (buffer & 0xFF);
                buffer >>= 8;
                
                // fall through
            case 1:
                decodedBytes[0] = (byte) (buffer & 0xFF);
                break;
            default:
                assert false;
        }
    }

    /**
     * Decodes one input char (expressed as an int) into its zero-based index in
     * the BASE64 alphabet, or into a code for a padding character or for a
     * whitespace / extraneous character. Returns an int whose 6 least
     * significant bits are the data decoded from this char.
     * 
     * @param src the character to decode, as an int
     * @return an int containing the six decoded bits as its lowest-order bits,
     *         or -1 to indicate an ignorable character, or -2 to indicate an
     *         end-of-message padding character
     */
    private int decodeInt(int src) {
        if ((src >= 'A') && (src <= 'Z')) {
            return (src - 'A');
        }
        if ((src >= 'a') && (src <= 'z')) {
            return ((src - 'a') + 26);
        }
        if ((src >= '0') && (src <= '9')) {
            return ((src - '0') + 52);
        }
        if (src == '+') {
            return 62;
        }
        if (src == '/') {
            return 63;
        }
        if (src == '=') {
            return -2;
        }
        return -1;
    }
}
