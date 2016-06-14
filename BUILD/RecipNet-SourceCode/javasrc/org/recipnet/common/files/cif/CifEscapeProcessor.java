/*
 * Reciprocal Net project
 *
 * CifEscapeProcessor.java
 *
 * 27-Jan-2005: jobollin separated this class from CifParser
 * 19-Jul-2005: jobollin expanded commentary of several methods
 * 30-Sep-2005: jobollin implemented support for codes "--", "+-", and "-+"
 * 30-Sep-2005: jobollin converted the code to use StringBuilder instead of
 *              StringBuffer in most places
 * 30-Sep-2005: jobollin added some missing javadoc tags
 */

package org.recipnet.common.files.cif;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing utility objects that process text to resolve CIF escape
 * codes to the characters they represent.  See 
 * <a href="http://www.iucr.org/iucr-top/cif/spec/version1.1/cifsemantics.html#markup">IUCr's
 * definitions of CIF markup conventions</a> for a description of the CIF
 * character markup codes supported by this class.  Most, but not all, of the
 * markup described is implemented.  (In particular, some "named" CIF codes do
 * not have corresponding Unicode characters or character sequences, and no
 * typographic style codes are implemented (HTML or otherwise).)  
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifEscapeProcessor {

    /**
     * <p>
     * A table of the translations of CIF escape codes.  Note that some of
     * these escapes code for Unicode combining characters, but that in CIF
     * they occur before the character to which they apply (the "base character"
     * in Unicode-speak) instead of after it as in Unicode.
     * </p><p>
     * The table is indexed by the ASCII code for the character being
     * translated; for instance, "\m" is translated to translationTable['m']
     * (which is a lowercase Greek mu character).  Where the table contains
     * character 0 there is no translation defined.
     * <p>
     */
    private final static char[] translationTable = new char[] {

        /*
         * Where a translation is defined, the corresponding ASCII character is
         * indicated in a comment above the table entry.
         */
'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000',
'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000',
'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000',
'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000',
//                     "                             %                   '
'\u0000', '\u0000', '\u0308', '\u0000', '\u0000', '\u030a', '\u0000', '\u0301',
// (                                       ,                   .         /
'\u0306', '\u0000', '\u0000', '\u0000', '\u0327', '\u0000', '\u0307', '\u0337',
'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000',
//                               ;         <         =         >
'\u0000', '\u0000', '\u0000', '\u0328', '\u030c', '\u0305', '\u030b', '\u0000',
//           A         B         C         D         E         F         G
'\u0000', '\u0391', '\u0392', '\u03a7', '\u0394', '\u0395', '\u03a6', '\u0393',
// H         I                   K         L         M         N         O
'\u0397', '\u0399', '\u0000', '\u039a', '\u039b', '\u039c', '\u039d', '\u039f',
// P         Q         R         S         T         U                   W
'\u03a0', '\u0398', '\u03a1', '\u03a3', '\u03a4', '\u03a5', '\u0000', '\u03a9',
// X         Y         Z                                       ^
'\u039e', '\u03a8', '\u0396', '\u0000', '\u0000', '\u0000', '\u0302', '\u0000',
//           a         b         c         d         e         f         g
'\u0300', '\u03b1', '\u03b2', '\u03c7', '\u03b4', '\u03b5', '\u03c6', '\u03b3',
// h         i                   k         l         m         n         o
'\u03b7', '\u03b9', '\u0000', '\u03ba', '\u03bb', '\u03bc', '\u03bd', '\u03bf',
// p         q         r         s         t         u                   w
'\u03c0', '\u03b8', '\u03c1', '\u03c3', '\u03c4', '\u03c5', '\u0000', '\u03c9',
// x         y         z                                       ~
'\u03be', '\u03c8', '\u03b6', '\u0000', '\u0000', '\u0000', '\u0303', '\u0000'
    };

    /* A map of some of the CIF string -> character escapes */
    private final static Map<String, String> translationMap =
            new HashMap<String, String>();
    
    static {
        translationMap.put("\\\\simeq", "\u2245");
        translationMap.put("\\\\sim", "~");
        translationMap.put("\\\\infty", "\u221e");
        translationMap.put("\\\\times", "\u00d7");
        translationMap.put("\\\\square", "\u00b2");
        translationMap.put("\\\\neq", "\u2260");
        translationMap.put("\\\\rangle", "\u232a");
        translationMap.put("\\\\langle", "\u2329");
        translationMap.put("\\\\rightarrow", "\u2192");
        translationMap.put("\\\\leftarrow", "\u2190");
        translationMap.put("--", "\u2014");
        translationMap.put("+-", "\u00b1");
        translationMap.put("-+", "\u2213");

        /*
         * CIF 1.1 also defines a few named codes (for various chemical bond
         * symbols) for which there are no corresponding Unicode characters.
         * These are not translated
         */
    }

    /*
     * Every string matched (in its entirety) by this pattern must be mapped
     * to an appropriate translation in the translationMap:
     */
    private final static Pattern PLUS_MINUS_PATTERN =
            Pattern.compile("(?<![+-])((?:--)|(?:\\+-)|(?:-\\+))(?![+-])");
        

    /**
     * Processes a CIF data value, interpreting the somewhat baroque CIF
     * escape codes.  This method does not interpret all CIF markup codes;
     * in particular it does not process the subscript and superscript delimiter
     * codes, and in general it does not interpret codes that do not begin
     * with a '\' character.
     *
     * @param  s the String to process for CIF escape codes
     *
     * @return a copy of {@code s} in which the CIF escapes have been
     *         replaced by the characters they represent.  If any escapes were
     *         present in {@code s} then the returned String will generally
     *         contain one or more non-ASCII characters.
     */
    public String unescapeCifValue(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder input = new StringBuilder(s);
        StringBuilder output = new StringBuilder(s.length());
        StringBuilder hold = new StringBuilder();
        StringBuffer output2 = new StringBuffer(s.length());
        Matcher matcher;

        while (input.length() > 0) {
            translateNextChars(input, output, hold);
        }

        if (hold.length() > 0) {
            output.append(' ').append(hold);
        }
        
        matcher = PLUS_MINUS_PATTERN.matcher(output);
        
        while (matcher.find()) {
            matcher.appendReplacement(output2,
                    translationMap.get(matcher.group()));
        }
        matcher.appendTail(output2);
        
        /*
         * TODO: consider canonicalizing the result (into Unicode normalization
         * form NFKD, most likely); code would probably go into
         * outputBaseCharacter()
         */

        return output2.toString();
    }

    // TODO: add method escapeCifValue(String)

    /**
     * Translates the next one or more characters available from the input
     * buffer, putting the results on the output or hold buffer as appropriate;
     * successfully processed characters (always at least one if any are
     * available) are removed from the input buffer.  The input is expected to
     * be drawn from valid CIF content, and thus may contain only 7-bit ASCII
     * characters
     *
     * @param  input a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    protected void translateNextChars(StringBuilder input, StringBuilder output,
                                      StringBuilder hold) {
                                          
        // The last character can never be the start of an escape
        if (input.length() == 1) {
            outputBaseChar(input.charAt(0), output, hold);
            input.setLength(0);
        } else if (input.length() > 1) {
            int slashPos = input.indexOf("\\");

            // Looking at a possible escape start
            if (slashPos == 0) {
                int consumed;
                char c2 = input.charAt(1);

                switch(c2) {
                case '\\':
                    consumed = translateNamedEscape(input, output, hold);
                    break;
                case '%':
                    consumed = translateRingAbove(input, output, hold);
                    break;
                case '?':
                    consumed = translateModifiedI(input, output, hold);
                    break;
                case '&':
                    consumed = translateGermanSS(input, output, hold);
                    break;
                default:
                    consumed = translateFromTable(c2, output, hold);
                    break;
                }

                input.delete(0, consumed);
                
            // Transfer everything up to the next potential escape start
            } else {
                outputBaseChar(input.charAt(0), output, hold);
                if (slashPos < 0) {
                    output.append(input.substring(1));
                    input.setLength(0);
                } else {
                    output.append(input.substring(1, slashPos));
                    input.delete(0, slashPos);
                }
            }
        }
    }

    /**
     * Processes a character subsequence from the input that looks like a
     * named character (starts with two backslashes).  If a matching named
     * translation is found then that is placed in the output buffer and the
     * entire escape sequence is consumed; otherwise only the two backslashes
     * are consumed (to produce a single backslash on the output)
     *
     * @param  input a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be backslashes
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *         
     * @return the number of input characters consumed 
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateNamedEscape(StringBuilder input,
            StringBuilder output, StringBuilder hold) {

        if (input.length() > 2) {

            /*
             * Extract everything from the first "\\" up to (but
             * excluding) the next CIF whitespace character (\u0009 is
             * vertical tab)
             */
            StringTokenizer st =
                new StringTokenizer(input.substring(0), " \t\r\n\f\u0009");
            String token = st.nextToken();

            // Get the corresponding translation, if any, from the map
            String translation = translationMap.get(token);

            if (translation != null) {
                assert translation.length() == 1;

                /*
                 * The character to output is the first and only one in
                 * the translation, and processing should resume after
                 * the end of the escape sequence
                 */
                outputBaseChar(translation.charAt(0), output, hold);

                return token.length();
            }
        }

        /*
         * If no translation was found then a single backslash will
         * be output and the parsing will resume immediately after
         * the second backslash (no need to set anything for this to
         * happen
         */
        outputBaseChar('\\', output, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that should be either
     * a degree symbol or a combining ring above (starts with "\%"); the
     * ring above is produced only if the next character is an upper- or
     * lower-case Latin 'a'.
     *
     * @param  input a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\%"
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *         
     * @return the number of input characters consumed 
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateRingAbove(StringBuilder input,
            StringBuilder output, StringBuilder hold) {

        // Can only be a ring above if a character to modify follows
        if (input.length() > 2) {

            // The next character is the one to modify
            char c3 = input.charAt(2);

            // Only lower- and uppercase latin A are modified
            if ((c3 == 'a') || (c3 == 'A')) {

                // output the modified character
                outputBaseChar(c3, output, hold.append('\u030a'));

                return 3;
            }
        }

        // Output the degree symbol
        outputBaseChar('\u00b0', output, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that looks like it might
     * indicate a dotless I (starts with "\?").  If the next character is a
     * lowercase Latin 'i' then three characters are consumed and the output is
     * a lowercase dotless 'i' (\u0131); if the next character is an uppercase
     * Latin 'I' then three characters are consumed and the output is an
     * uppercase (normal) Latin 'I'; otherwise, two characters are consumed and
     * the output is a question mark (?).
     *
     * @param  input a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\?"
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *         
     * @return the number of input characters consumed 
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateModifiedI(StringBuilder input,
            StringBuilder output, StringBuilder hold) {

        /*
         * Can only be a dotless i if followed by an i or I;
         * otherwise echo the question mark
         */
        if (input.length() > 2) {
            switch (input.charAt(2)) {
                case 'i':

                    // Replace the sequence with a dotless 'i'
                    outputBaseChar('\u0131', output, hold);

                    return 3;

                case 'I':

                    // Replace the sequence with a [dotless] 'I'
                    outputBaseChar('I', output, hold);

                    return 3;
            }
        }

        // Translate the first two characters of the sequence to a '?'
        outputBaseChar('?', output, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that looks like it might
     * indicate a German double-S ligature (starts with "\&amp;").  If the next
     * character is a lowercase Latin 's' then three characters are consumed and
     * the double-S ligature (\u00df) is output; if the next character is an
     * uppercase Latin 'S' then three characters are consumed and two capital
     * 'S' characters are output, with any held combining characters attached to
     * the first; otherwise two characters are consumed and an ampersand (&amp;)
     * is output. 
     *
     * @param  input a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\&amp;"
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *         
     * @return the number of input characters consumed 
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateGermanSS(StringBuilder input,
            StringBuilder output, StringBuilder hold) {

        /*
         * Can only be a double-'s' ligature if followed by an s or
         * S; otherwise echo the ampersand
         */
        if (input.length() > 2) {
            switch (input.charAt(2)) {
                case 's':

                    /*
                     * Replace the sequence with a lowercase double 's' ligature
                     */
                    outputBaseChar('\u00df', output, hold);

                    return 3;
                case 'S':

                    // The uppercase version is plain "SS"
                    outputBaseChar('S', output, hold.append('S'));

                    return 3;
            }
        }

        // Replace the sequence with an '&'
        outputBaseChar('&', output, hold);

        return 2;
    }

    /**
     * Processes a simple character escape by table lookup; if a translation
     * is listed in the table then it is held or output, depending on whether
     * or not it is a combining character (as determined by {@link
     * #isCombiningCharacter(char)}); otherwise the whole sequence is output,
     * including the backslash
     *
     * @param  c the table key, typically picked out from the input as the
     *         character following a backslash; assumed to be in the range of
     *         the translation table (0 - 0x7f)
     * @param  output a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *         
     * @return the number of input characters consumed 
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateFromTable(char c, StringBuilder output,
                                         StringBuilder hold) {
        assert (c < 0x80);
        char trans = translationTable[c];

        if (trans == '\u0000') {

            // No valid translation; output the whole thing as-is
            outputBaseChar('\\', output, hold);
            outputBaseChar(c, output, hold);
        } else if (isCombiningCharacter(trans)) {

            /*
             * Combining characters must be appended to their
             * base character.  There may be more than one
             * combining character for one base character.
             */
            hold.append(trans);
        } else {

            // other characters are simply output
            outputBaseChar(trans, output, hold);
        }

        return 2;
    }

    /**
     * Appends the specified character to the provided output buffer, followed
     * by any characters in the hold buffer, and clears the hold buffer.
     *
     * @param  c the base character to append to the output buffer; should not
     *         be a Unicode combining character
     * @param  output the StringBuilder into which to place the results
     * @param  hold a StringBuilder containing additional characters that should
     *         be appended to the output; this buffer is cleared during the
     *         course of the method execution
     *
     * @throws NullPointerException if any of the arguments is null
     */
    private void outputBaseChar(char c, StringBuilder output,
                                StringBuilder hold) {
        output.append(c).append(hold);
        hold.setLength(0);
    }

    /**
     * Determines whether a character is a Unicode "combining character", which
     * is rendered as part of a preceding base character
     *
     * @param  c the character to test
     *
     * @return {@code true} if {@code c} is in Unicode general class
     *         M; {@code false} otherwise
     *
     */
    private static boolean isCombiningCharacter(char c) {
        int type = Character.getType(c);

        return (type == Character.COMBINING_SPACING_MARK) 
               || (type == Character.ENCLOSING_MARK)
               || (type == Character.NON_SPACING_MARK);
    }
}

