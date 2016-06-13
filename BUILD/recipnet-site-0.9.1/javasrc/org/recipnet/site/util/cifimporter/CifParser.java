/*
 * Reciprocal Net project
 * 
 * CifParser.java
 *
 * 06-Jun-2002: jobollin wrote skeleton
 * 10-Jun-2002: jobollin wrote first draft
 * 20-Oct-2003: jobollin performed minor fixup regarding configuration of the
 *              underlying CifSyntaxReader
 * 16-Feb-2004: jobollin sorted imports and changed the type of the internal
 *              Map to LinkedHashMap
 * 16-Feb-2004: jobollin fixed bug #1119; the real fix is in CifSyntaxReader,
 *              but CifParser must be prepared to handle the (slight) difference
 *              in CifSyntaxReaders' data streams
 * 19-Feb-2004: jobollin wrote code to interpret CIF escape codes in data values
 * 19-Feb-2004: jobollin wrote code to translate all whitespace in quoted data
 *              values into spaces
 * 23-Feb-2004: jobollin wrote code for handling most CIF markup codes
 * 09-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.cifimporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * a class that knows how to create CIF data objects from data obtained from an
 * {@link java.io.InputStream InputStream} or {@link java.io.Reader Reader}.
 * {@code CifParser} objects represent a CIF data block as a {@link
 * java.util.Map Map} associating data names with the corresponding data
 * values.  Data names that occur inside CIF loop structures are mapped to
 * appropriate {@link CifLoop CifLoop} objects that model the corresponding
 * loop structures.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 */
public class CifParser {

    /**
     * the key used to store the CIF data block's identifier in the
     * {@code Map} returned by {@code getNextDataBlock()}
     */
    public final static String BLOCK_NAME_KEY =
            "org.recipnet.site.util.cifimporter.CifParser.BLOCKNAME";

    /**
     * the key used to store the list of errors (as a {@code List})
     * in the {@code Map} returned by {@code getNextDataBlock()}
     */
    public final static String ERRORS_KEY =
            "org.recipnet.site.util.cifimporter.CifParser.ERRORS";

    /* Constants describing this object's current parsing state. */
    private final static int PREBLOCK_STATE = 0;
    private final static int EXPECT_NAME_STATE = 1;
    private final static int LOOP_HEADER_STATE = 2;
    private final static int EXPECT_VALUE_STATE = 3;
    private final static int LOOP_VALUE_STATE = 4;

    /*
     * See http://iucr.sdsc.edu/iucr-top/cif/spec/version1.1/cifsemantics.html#markup
     * for a description of the CIF character markup conventions implemented
     * here.  Most, but not all, of the markup described is implemented.
     */

    /**
     * A table of the translations of CIF escape codes.  Note that some of
     * these escapes code for Unicode combining characters, but that in CIF
     * they occur before the character to which they apply (the "base character"
     * in Unicode-speak) instead of after it.
     * <p>
     * The table is indexed by the ASCII code for the character being
     * translated; for instance, "\m" is translated to translationTable['m'].
     * Where the table contains character 0 there is no translation defined.
     */

    /*
     * Where a translation is defined the corresponding ASCII character is
     * indicated in a comment above the table entry.
     */
    private final static char[] translationTable = new char[] {
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
    private final static Map<String, String> translationMap
            = new HashMap<String, String>();

    static {
        translationMap.put("\\\\simeq", "\u2248");
        translationMap.put("\\\\sim", "~");
        translationMap.put("\\\\infty", "\u221e");
        translationMap.put("\\\\times", "\u00d7");
        translationMap.put("\\\\square", "\u00b2");
        translationMap.put("\\\\neq", "\u2260");
        translationMap.put("\\\\rangle", "\u232a");
        translationMap.put("\\\\langle", "\u2329");
        translationMap.put("\\\\rightarrow", "\u2192");
        translationMap.put("\\\\leftarrow", "\u2190");

        /*
         * CIF 1.1 also defines some translations that are not currently
         * implemented, but could be:
         *     translationMap.put("--", "\u2014");
         *     translationMap.put("+-", "\u00b1");
         *     translationMap.put("-+", "\u2213");
         * They are unimplemented because their meaning is clear without
         * translation and their inclusion would make the translation code
         * even more complicated than it already is.
         */
    }

    private final static Pattern CIF_WS_PATTERN =
            Pattern.compile("[\u0001-\u001f]");

    /**
     * a {@link java.io.StreamTokenizer StreamTokenizer} that will return
     * the CIF stream's tokens one by one. In this implementation the tokenizer
     * gets its character data via a {@link CifSyntaxReader CifSyntaxReader}
     * object that wraps the supplied {@code Reader} or
     * {@code InputStream}.  The {@code CifSyntaxReader} preprocesses
     * the CIF stream into a tokenization-friendly form.
     */
    protected StreamTokenizer cifTokenizer;

    /**
     * a {@link CifSyntaxReader CifSyntaxReader} to wrap the input CIF stream
     * and preprocess the CIF syntax into something more readily tokenizable
     */
    protected CifSyntaxReader input;

    /**
     * a {@link java.io.PrintWriter PrintWriter} to which output messages
     * should be printed.  No output messages are generated if
     * {@code output} is {@code null}.
     */
    protected PrintWriter output;

    /**
     * A {@code Set} containing names of CIF data blocks encountered
     * so far.
     */
    protected Set<String> blockNames;

    /**
     * A base constructor containing code that all constructors must provide;
     * at construction time {@link #setReader} should be invoked as well,
     * and optionally {@link #setWriter}.
     */
    private CifParser() {
        blockNames = new HashSet<String>();
    }

    /**
     * a constructor for creating a {@code CifParser} based on an
     * {@code InputStream}.  This constructor initializes
     * {@code output} to {@code null}, but {@code output} may
     * be set later by a call to {@code setWriter}.
     *
     * @param in an {@link java.io.InputStream InputStream} that will provide
     *        the data that this {@code CifParser} will parse; the byte
     *        stream is converted to characters according to the ISO-8859-1
     *        charset
     */
    public CifParser(InputStream in) {
        this();

        try {
            setReader(new InputStreamReader(in, "ISO-8859-1"));
        } catch (UnsupportedEncodingException uee) {

            /*
             * This should never happen because all Java implementations are
             * required to support ISO-8859-1
             */
            throw new RuntimeException("Unexpected exception", uee);
        }
    }

    /**
     * a constructor for creating a {@code CifParser} based on a
     * {@code Reader}.  This constructor initializes
     * {@code output} to {@code null}, but {@code output} may
     * be set later by a call to {@code setWriter}.
     *
     * @param r a {@link java.io.Reader Reader} that will provide
     *        the character data that this {@code CifParser} will parse
     */
    public CifParser(Reader r) {
        this();

        setReader(r);
    }

    /**
     * a constructor for creating a {@code CifParser} based on a
     * {@code Reader} while initializing {@code output} to refer
     * to the same {@code PrintWriter} referred to by {@code p}.
     * {@code output} may later be set to a different
     * {@code PrintWriter} by a call to {@code setWriter}.
     *
     * @param r a {@link java.io.Reader Reader} that will provide
     *        the character data that this {@code CifParser} will parse
     * @param p a {@link java.io.PrintWriter PrintWriter} to which output
     *        messages should be written
     */
    public CifParser(Reader r, PrintWriter p) {
        this();

        setReader(r);
        setWriter(p);
    }

    /**
     * Sets the Reader for this CifParser; intended for use by constructors,
     * and not safe to invoke while in the middle of parsing a stream
     *
     * @param  r the {@code Reader} from which to obtain the raw CIF data;
     *         will be wrapped in a BufferedReader and CifSyntaxReader
     */
    private void setReader(Reader r) {
        input = new CifSyntaxReader(new BufferedReader(r));
        input.setNonFatalErrors(true);
        cifTokenizer = new StreamTokenizer(input);
        cifTokenizer.resetSyntax();
        cifTokenizer.lowerCaseMode(false);
        cifTokenizer.eolIsSignificant(false);
        cifTokenizer.slashSlashComments(false);
        cifTokenizer.slashStarComments(false);
        cifTokenizer.quoteChar(CifSyntaxReader.QUOTE_METACHAR);
        cifTokenizer.whitespaceChars(9, 13);
        cifTokenizer.whitespaceChars(' ', ' ');
        cifTokenizer.wordChars(33, 126);
     }

    /**
     * sets the {@code PrintWriter} to which error messages are output
     *
     * @param p a {@link java.io.PrintWriter PrintWriter} to which future
     *        error messages generated by this {@code CifParser} should
     *        be written.  If {@code null} then future error messages
     *        will be suppressed.
     */
    public void setWriter(PrintWriter p) {
        output = p;
        input.setLog(p);
    }

    /**
     * <p>
     * returns a {@code Map} object based on the next CIF data
     * block available from {@code input}, or {@code null} if no
     * more data blocks are available.  The {@code Map} associates data
     * names with their values for non-looped data names, and with an
     * appropriate {@link CifLoop CifLoop} object for looped data names.
     * The returned {@code Map} also contains the data block identifier
     * (accessible with key {@code BLOCK_NAME_KEY}) and a
     * {@code List} of any errors that were detected during parsing
     * (accessible with key {@code ERRORS_KEY}).
     * </p><p>
     * This processor folds all CIF whitespace characters inside quoted data
     * values into space characters and processes most CIF escape codes that
     * represent character data (as opposed to style / typesetting).  Data
     * values returned by this parser may therefore contain non-ASCII characters
     * </p>
     *
     * @return a {@link java.util.Map Map} containing the data from the next
     *         CIF data block from {@code input}.
     */
    public Map<String, Object> getNextDataBlock() {
        Map<String, Object> cifMap = null;
        CifLoop thisLoop = null;
        String thisName = null;
        String thisVal = null;
        boolean quoted = true;
        int state = PREBLOCK_STATE;
        List errorList;

        if (cifTokenizer == null) {
            return null;
        }

        dataBlock:
        while (true) {
            try {
                tokenLoop:
                for (cifTokenizer.nextToken(); ; cifTokenizer.nextToken()) {
                    thisVal = null;
                    quoted = true;

                    /* action depends on the token type */
                    switch (cifTokenizer.ttype) {
                    case StreamTokenizer.TT_NUMBER:
                    case StreamTokenizer.TT_EOL:

                        /*
                         * these should never be encountered because
                         * cifTokenizer is configured so as not to be able to
                         * produce them
                         */
                        cifTokenizer = null;
                        System.err.println(
        "CifParser: internal error -- inappropriate token type received");
                        System.err.flush();
                        return null;
                    case StreamTokenizer.TT_EOF:

                        /* normal end of file */
                        switch (state) {
                        case PREBLOCK_STATE:
                        case EXPECT_NAME_STATE:

                            /* valid cases */
                            break;
                        case LOOP_HEADER_STATE:
                            if ( thisLoop.checkValidity()
                                 == CifLoop.NO_DATA_NAMES) {
                                reportError( cifMap,
        new CifError(CifError.LOOP_EMPTY_HEADER_EOF, input.getLineCount()) );
                            } else {
                                reportError( cifMap,
        new CifError(CifError.LOOP_NO_DATA_EOF, input.getLineCount()) );
                            }
                            break;
                        case LOOP_VALUE_STATE:
                            if (thisLoop.checkValidity() != CifLoop.VALID) {
                                reportError( cifMap,
        new CifError(CifError.LOOP_PARTIAL_RECORD_EOF, input.getLineCount()) );
                            }

                            /* otherwise a valid case */
                            break;
                        case EXPECT_VALUE_STATE:
                            reportError( cifMap,
        new CifError(CifError.DATA_VALUE_MISSING_EOF, input.getLineCount()) );
                            break;
                        }
                        cifTokenizer = null;
                        break dataBlock;
                    case StreamTokenizer.TT_WORD:

                        /*
                         * Word tokens should usually be the most common.
                         * They may be data names, unquoted data values, or
                         * CIF keywords.
                         */
                        if ( "data_".equalsIgnoreCase(cifTokenizer.sval)
                             || "global_".equalsIgnoreCase(cifTokenizer.sval)
                             || "save_".equalsIgnoreCase(cifTokenizer.sval)
                             || "stop_".equalsIgnoreCase(cifTokenizer.sval) ) {
                            /* excluded reserved words */
                            reportError( cifMap,
        new CifError(CifError.RESERVED_WORD, input.getLineCount(), cifTokenizer.sval) );
                            /* no state change */
                            continue tokenLoop;
                        } else if ( (cifTokenizer.sval.length() > 5)
        && cifTokenizer.sval.substring(0,5).equalsIgnoreCase("data_") ) {

                            /* a data block header */
                            String blockName =
                                cifTokenizer.sval.substring(5).toLowerCase();

                            if (state == PREBLOCK_STATE) {

                                /* This the first block header encountered */
                                cifMap = new LinkedHashMap<String, Object>();
                                cifMap.put(BLOCK_NAME_KEY, blockName);
                                errorList = new LinkedList();
                                cifMap.put(ERRORS_KEY, errorList);
                                input.setErrorList(errorList);
                                state = EXPECT_NAME_STATE;
                                if ( !blockNames.add(blockName) ) {
                                    reportError( cifMap,
        new CifError(CifError.DUPLICATE_BLOCK_CODE, input.getLineCount(), blockName) );
                                }
                                continue tokenLoop;
                            } else {

                                /* This starts the next block -- defer */
                                cifTokenizer.pushBack();
                                break dataBlock;
                            }
                        } else if (
        "loop_".equalsIgnoreCase(cifTokenizer.sval) ) {

                            /* a loop header */
                            switch (state) {
                            case EXPECT_NAME_STATE:

                                /* valid */
                                break;
                            case LOOP_VALUE_STATE:

                                /*
                                 * valid unless the current loop record is
                                 * incomplete
                                 */
                                if (thisLoop.checkValidity() != CifLoop.VALID) {
                                    reportError( cifMap,
        new CifError(CifError.LOOP_PARTIAL_RECORD, input.getLineCount(),
                     cifTokenizer.sval) );
                                }
                                break;
                            case PREBLOCK_STATE:
                                reportError( cifMap,
        new CifError(CifError.LOOP_HEADER_NO_BLOCK, input.getLineCount()) );

                                /*
                                 * Only from PREBLOCK_STATE does a loop_
                                 * token NOT cause a transition to (or
                                 * continuation of) LOOP_HEADER_STATE
                                 */
                                continue tokenLoop;
                            case LOOP_HEADER_STATE:
                                if ( thisLoop.checkValidity()
                                     == CifLoop.NO_DATA_NAMES) {
                                    reportError( cifMap,
        new CifError(CifError.EXTRA_LOOP, input.getLineCount(), "loop_") );
                                } else {
                                    reportError( cifMap,
        new CifError(CifError.LOOP_NO_DATA, input.getLineCount(), "loop_") );
                                }
                                break;
                            case EXPECT_VALUE_STATE:
                                reportError( cifMap,
        new CifError(CifError.DATA_VALUE_MISSING, input.getLineCount(), "loop_") );
                                break;
                            }
                            thisLoop = new CifLoop();
                            state = LOOP_HEADER_STATE;
                            continue tokenLoop;
                        } else if (cifTokenizer.sval.startsWith("_")) {

                            /* a data name */
                            thisName = cifTokenizer.sval.toLowerCase();
                            if (cifMap != null
                                && cifMap.containsKey(thisName)) {
                                reportError( cifMap,
        new CifError(CifError.DUPLICATE_DATA_NAME, input.getLineCount(), thisName) );
                            }
                            switch (state) {
                            case LOOP_HEADER_STATE:
                                thisLoop.addDataName(thisName);
                                cifMap.put(thisName, thisLoop);

                                /* state not changed */
                                break;
                            case LOOP_VALUE_STATE:
                                if (thisLoop.checkValidity() != CifLoop.VALID) {
                                    reportError( cifMap,
        new CifError(CifError.LOOP_PARTIAL_RECORD, input.getLineCount(), thisName) );
                                }

                                /* fall through */
                            case EXPECT_NAME_STATE:
                                state = EXPECT_VALUE_STATE;
                                break;
                            case PREBLOCK_STATE:
                                reportError( cifMap,
        new CifError(CifError.DATA_NAME_NO_BLOCK, input.getLineCount(), thisName) );

                                /* state not changed */
                                break;
                            case EXPECT_VALUE_STATE:
                                reportError( cifMap,
        new CifError(CifError.DATA_VALUE_MISSING, input.getLineCount(), thisName) );

                                /* state not changed */
                                break;
                            }
                            continue tokenLoop;
                        }

                        /*
                         * control should reach here only for an unquoted text
                         * string; fall through
                         */
                        quoted = false;

                        /* fall through */

                    case CifSyntaxReader.QUOTE_METACHAR:

                        /*
                         * Quoted and unquoted text strings can be treated
                         * the same, and their values can be obtained from
                         * cifTokenizer.sval.
                         */
                        thisVal = cifTokenizer.sval;
                        if (quoted) {
                            thisVal = unescapeString(thisVal);
                            thisVal = CIF_WS_PATTERN.matcher(
                                    thisVal).replaceAll(" ");
                        } else if (thisVal.equals("?") || thisVal.equals(".")) {

                            /*
                             * bare tokens ? and . are placeholders with no real
                             * value.  The value is set to null for these.
                             */
                            thisVal = null;
                        }

                        /* fall through */

                    default: // Control should only reach here by fall through

                        /*
                         * Syntactically, we have a data value (referred to by
                         * thisVal).  We treat all data values the same.
                         */
                        switch (state) {
                        case EXPECT_VALUE_STATE:
                            if (thisVal != null) {
                                cifMap.put(thisName,
                                           processCifEscapes(thisVal));
                            }
                            state = EXPECT_NAME_STATE;
                            break;
                        case LOOP_HEADER_STATE:
                            if ( thisLoop.checkValidity()
                                 == CifLoop.NO_DATA_NAMES ) {
                                reportError( cifMap,
        new CifError(CifError.DATA_NAME_MISSING, input.getLineCount(), thisVal) );
                                reportError( cifMap,
        new CifError(CifError.LOOP_EMPTY_HEADER, input.getLineCount()) );
                                state = EXPECT_NAME_STATE;

                                /* the value is dropped */
                                continue tokenLoop;
                            }
                            state = LOOP_VALUE_STATE;

                            /* fall through */

                        case LOOP_VALUE_STATE:

                            /* the data value is added even if null */
                            thisLoop.addDataValue(processCifEscapes(thisVal));

                            /* state not changed */
                            break;
                        case EXPECT_NAME_STATE:
                                reportError( cifMap,
        new CifError(CifError.DATA_NAME_MISSING, input.getLineCount(), thisVal) );
                            /* state not changed */
                            break;
                        case PREBLOCK_STATE:
                                reportError( cifMap,
        new CifError(CifError.VALUE_NO_BLOCK, input.getLineCount(), thisVal) );

                            /* state not changed */
                            break;
                        }
                        break;
                    }
                } // End of tokenLoop loop
            } catch (CifSyntaxException cse) {
                CifError error = cse.getCifError();

                if (error == null) {
                    reportError( cifMap,
        new CifError(CifError.SYNTAX_ERROR, input.getLineCount()) );
                } else {
                    reportError(cifMap, error);
                }

                /* Attempts to resume parsing after a syntax error */

            } catch (IOException ioe) {
                reportError( cifMap,
        new CifError(CifError.NO_ERROR, input.getLineCount(), ioe.toString()) );
                cifTokenizer = null;
                break dataBlock;
            }
        } // End of dataBlock loop

        return cifMap;
    }

    /**
     * reports an error message to an appropriate resource and updates the
     * error list in the provided {@code Map}.  {@code msg} is
     * the error message and {@code m} is the {@code Map}; either or
     * both of these may be {@code null}, in which case they will not be
     * output or updated, respectively.  This method is intended to operate
     * on a working version of the {@code Map} produced by
     * @param m a {@link java.util.Map Map} in which the number of errors
     *          detected will be updated
     * @param cerror a {@code CifError} containing the error message
     */
    protected void reportError(Map m, CifError cerror) {
        if (cerror != null) {
            if (m != null) {
                List<CifError> errorList = (List<CifError>) m.get(ERRORS_KEY);
                errorList.add(cerror);
            }
            if (output != null) {
                output.println("CIF parsing error: " + cerror);
                output.flush();
            }
        }
    }

    /**
     * Unescapes characters escaped by a CifSyntaxReader (which CifSyntaxReaders
     * do by adding a constant to the numeric value of the character) in a
     * quoted string.  Returns an unescaped version of the input String.
     *
     * @param  s the input String, possibly containing one or more escaped
     *         characters
     *
     * @return a version of {@code s} containing only unescaped characters
     */
    protected static String unescapeString(String s) {
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > (CifSyntaxReader.ESCAPE_CODE - 1)) {
                chars[i] = (char) (chars[i] - CifSyntaxReader.ESCAPE_CODE);
            }
        }

        return new String(chars);
    }

    /**
     * Processes a CIF data value, interpreting the somewhat baroque CIF
     * escape codes.  This method does not interpret all CIF markup codes;
     * in particular it does not process the subscript and superscript delimiter
     * codes, and in general it does not interpret codes that do not begin
     * with a '\' character
     *
     * @param  s the String to process for CIF escape codes
     *
     * @return a copy of {@code s} in which the CIF escapes have been
     *         replaced by the characters they represent.  If any escapes were
     *         present in {@code s} then the returned String will generally
     *         contain one or more non-ASCII characters.
     */
    protected final String processCifEscapes(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder inputChars = new StringBuilder(s);
        StringBuilder outputChars = new StringBuilder();
        StringBuilder hold = new StringBuilder();

        while (inputChars.length() > 0) {
            translateNextChars(inputChars, outputChars, hold);
        }

        if (hold.length() > 0) {
            outputChars.append(' ').append(hold);
        }

        return outputChars.toString();
    }

    /**
     * Translates the next one or more characters available from the input
     * buffer, putting the results on the output or hold buffer as appropriate;
     * successfully processed characters (always at least one if any are
     * available) are removed from the input buffer.  The input is expected to
     * be drawn from valid CIF content, and thus contains only 7-bit ASCII
     * characters
     *
     * @param  inputChars a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    protected void translateNextChars(StringBuilder inputChars,
            StringBuilder outputChars, StringBuilder hold) {
        if (inputChars.length() > 0) {
            char c = inputChars.charAt(0);
            int consumed;    // intentionally not initialized here

            if ((c != '\\') || (inputChars.length() < 2)) {
                outputBaseChar(c, outputChars, hold);
                consumed = 1;
            } else {
                // look at the next char
                char c2 = inputChars.charAt(1);

                switch(c2) {
                case '\\':
                    consumed = translateNamedEscape(inputChars, outputChars, hold);
                    break;
                case '%':
                    consumed = translateRingAbove(inputChars, outputChars, hold);
                    break;
                case '?':
                    consumed = translateModifiedI(inputChars, outputChars, hold);
                    break;
                case '&':
                    consumed = translateGermanSS(inputChars, outputChars, hold);
                    break;
                default:
                    consumed = translateFromTable(c2, outputChars, hold);
                    break;
                }
            }

            inputChars.delete(0, consumed);
        }
    }

    /**
     * Processes a character subsequence from the input that looks like a
     * named character (starts with two backslashes)
     *
     * @param  inputChars a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be backslashes
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateNamedEscape(StringBuilder inputChars,
            StringBuilder outputChars, StringBuilder hold) {

        if (inputChars.length() > 2) {

            /*
             * Extract everything from the first "\\" up to (but
             * excluding) the next CIF whitespace character (\u0009 is
             * vertical tab)
             */
            StringTokenizer st =
                new StringTokenizer(inputChars.substring(0), " \t\r\n\f\u0009");
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
                outputBaseChar(translation.charAt(0), outputChars, hold);

                return token.length();
            }
        }

        /*
         * If no translation was found then a single backslash will
         * be output and the parsing will resume immediately after
         * the second backslash (no need to set anything for this to
         * happen
         */
        outputBaseChar('\\', outputChars, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that should be either
     * a degree symbol or a combining ring above (starts with "\%")
     *
     * @param  inputChars a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\%"
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateRingAbove(StringBuilder inputChars,
            StringBuilder outputChars, StringBuilder hold) {

        // Can only be a ring above if a character to modify follows
        if (inputChars.length() > 2) {

            // The next character is the one to modify
            char c3 = inputChars.charAt(2);

            // Only lower- and uppercase latin A are modified
            if ((c3 == 'a') || (c3 == 'A')) {

                // output the modified character
                outputBaseChar(c3, outputChars, hold.append('\u030a'));

                return 3;
            }
        }

        // Output the degree symbol
        outputBaseChar('\u00b0', outputChars, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that looks like it might
     * indicate a dotless I (starts with "\?")
     *
     * @param  inputChars a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\?"
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateModifiedI(StringBuilder inputChars,
            StringBuilder outputChars, StringBuilder hold) {

        /*
         * Can only be a dotless i if followed by an i or I;
         * otherwise echo the question mark
         */
        if (inputChars.length() > 2) {
            switch (inputChars.charAt(2)) {
                case 'i':

                    // Replace the sequence with a dotless 'i'
                    outputBaseChar('\u0131', outputChars, hold);

                    return 3;

                case 'I':

                    // Replace the sequence with a [dotless] 'I'
                    outputBaseChar('I', outputChars, hold);

                    return 3;
            }
        }

        // Translate the first two characters of the sequence to a '?'
        outputBaseChar('?', outputChars, hold);

        return 2;
    }

    /**
     * Processes a character subsequence from the input that looks like it might
     * indicate a German double-S ligature (starts with "\&amp;")
     *
     * @param  inputChars a StringBuilder containing the tail of the input character
     *         sequence that is yet to be translated; the first two characters
     *         are expected to be "\&amp;"
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateGermanSS(StringBuilder inputChars,
            StringBuilder outputChars, StringBuilder hold) {

        /*
         * Can only be a double-'s' ligature if followed by an s or
         * S; otherwise echo the ampersand
         */
        if (inputChars.length() > 2) {
            switch (inputChars.charAt(2)) {
                case 's':

                    /*
                     * Replace the sequence with a lowercase double 's' ligature
                     */
                    outputBaseChar('\u00df', outputChars, hold);

                    return 3;
                case 'S':

                    // The uppercase version is plain "SS"
                    outputBaseChar('S', outputChars, hold.append('S'));

                    return 3;
            }
        }

        // Replace the sequence with an '&'
        outputBaseChar('&', outputChars, hold);

        return 2;
    }

    /**
     * Processes a simple character escape by table lookup
     *
     * @param  c the table key, typically picked out from the input as the
     *         character following a backslash
     * @param  outputChars a StringBuilder containing the sequence of characters
     *         successfully translated so far
     * @param  hold a StringBuilder containing characters (typically Unicode
     *         combining characters) that have been processed from the input
     *         but not yet written to the output
     *
     * @throws NullPointerException if any of the arguments are null
     */
    private int translateFromTable(char c, StringBuilder outputChars,
                                         StringBuilder hold) {
        assert (c < 0x80);
        char trans = translationTable[c];

        if (trans == '\u0000') {

            // No valid translation; output the whole thing as-is
            outputBaseChar('\\', outputChars, hold);
            outputChars.append(c);
        } else if (isCombiningCharacter(trans)) {

            /*
             * Combining characters must be appended to their
             * base character.  There may be more than one
             * combining character for one base character.
             */
            hold.append(trans);
        } else {

            // other characters are simply output
            outputBaseChar(trans, outputChars, hold);
        }

        return 2;
    }

    /**
     * Determines whether a character is a Unicode "combining character"
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

    /**
     * Appends the specified character to the provided output buffer, followed
     * by any characters in the hold buffer, and clears the hold buffer.
     *
     * @param  c the base character to append to the output buffer; should not
     *         be a Unicode combining character
     * @param  outputChars the StringBuilder into which to place the results
     * @param  hold a StringBuilder containing additional characters that should
     *         be appended to the output; this buffer is cleared during the
     *         course of the method execution
     *
     * @throws NullPointerException if any of the arguments is null
     */
    private void outputBaseChar(char c, StringBuilder outputChars,
                                StringBuilder hold) {
        /*
         * This is a utility function used frequently in the CIF escape
         * processing code; putting it here standardizes it and protects against
         * mistakes.
         */
        outputChars.append(c).append(hold);
        hold.setLength(0);
    }
}

