/*
 * Reciprocal Net Project
 *
 * Utf8UrlEncoder.java
 *
 * 23-Jun-2006: jobollin wrote first draft
 */
package org.recipnet.common;

import java.io.UnsupportedEncodingException;

/**
 * A class providing static methods for encoding URLs acording to the scheme
 * described in RFC 2396 and assuming that the intermediate character encoding
 * should be UTF-8.  This class is source-compatible with Java 1.1.
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class Utf8UrlEncoder {
    
    /**
     * An array of the encoded representations of all 256 bytes, precomputed so
     * as to avoid a vast number of recomputations during string transcoding
     */
    private final static String[] ENCODINGS = new String[256];
    
    // Initialize the array of encodings
    static {
        
        /*
         * This initial blanket computation is a bit wasteful, as roughly 25%
         * of the computed array elements are going to be replaced.
         * Nevertheless, it's a small, one-time cost, and it makes the rest of
         * the code rather simpler and much faster
         */
        
        // Make sure to insert a leading zero where necessary
        for (int i = 0; i < 0x10; i++) {
            ENCODINGS[i] = "%0" + Integer.toHexString(i).toUpperCase();
        }
        
        for (int i = 0x10; i < ENCODINGS.length; i++) {
            ENCODINGS[i] = '%' + Integer.toHexString(i).toUpperCase();
        }

        // The space character has a special, one-character encoding
        ENCODINGS[' '] = "+";
        
        /*
         * The upper- and lower-case letters, the digits, and a few select marks
         * are encoded as themselves
         */
        for (char c = '0'; c <= '9'; c++) {
            ENCODINGS[c] = String.valueOf(c);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            ENCODINGS[c] = String.valueOf(c);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            ENCODINGS[c] = String.valueOf(c);
        }
        ENCODINGS['.'] = ".";
        ENCODINGS['-'] = "-";
        ENCODINGS['*'] = "*";
        ENCODINGS['_'] = "_";
        
        /*
         * A few characters that RFC 2396 permits to be encoded as themselves
         * are nonetheless encoded using the three-character hex string format;
         * the documented behavior of Java's URLEncoder class is followed here.
         */
    }

    /**
     * Encodes the specified string by first encoding it into a byte sequence
     * via UTF-8, then choosing either a one- or a three-byte representation
     * for each byte according to the convention of RFC 2396, then decoding
     * the result to a string via US-ASCII
     *  
     * @param s the {@code String} to encode
     * 
     * @return the encoded {@code String}
     */
    public static String encode(String s) {
        StringBuffer sb = new StringBuffer();
        byte[] bytes;
        
        try { 
            bytes = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException uee) {
            // Can't happen; all Java implementations must support UTF-8
            throw new RuntimeException(uee.toString());
        }
        
        for (int i = 0; i < bytes.length; i++) {
            sb.append(ENCODINGS[bytes[i] & 0xff]);
        }
        
        return sb.toString();
    }
}
