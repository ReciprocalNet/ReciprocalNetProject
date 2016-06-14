/*
 * Reciprocal Net Project
 *
 * CifScannerTests.java
 * 
 * Jun 15, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;


/**
 * TODO: Write documentation
 * 
 * @author jobollin
 * @version 1.0
 * 
 */
public class CifScannerTests extends TestCase {

    private TestHandler defaultHandler;
    
    /**
     * Initializes this {@code CifScannerTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public CifScannerTests(String testName) {
        super(testName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        defaultHandler = new TestHandler();
    }
    
    public void testConstructor() {
        CifScanner scanner = new CifScanner();
    }

    public void testSetTokenHandler() {
    }

    public void testSetErrorHandler() {
    }

    public void testSetWhitespaceHandler() {
    }

    private void assertMaxLineLength(int length, CifScanner scanner) {
        StringBuffer cifData = new StringBuffer(length * 3 + 3);
        
        for (int i = 0; i < length - 1; i++) {
            cifData.append(' ');
        }
        cifData.append('\n');
        
        for (int i = 0; i < length; i++) {
            cifData.append(' ');
        }
        cifData.append('\n');
        
        for (int i = 0; i < length + 1; i++) {
            cifData.append(' ');
        }
        cifData.append('\n');
    
        setDefaultHandler(scanner);
        defaultHandler.clearEvents();
        try {
            scanner.scan(new StringReader(cifData.toString()));
        } catch (CifParseException cpe) {
            fail("Caught an unexpected CifParseException: " + cpe);
        } catch (IOException ioe) {
            fail("caught an unexpected IOException:" + ioe);
        }

        List<TestHandler.EventInfo> events = defaultHandler.getEvents();
        
        assertEquals("Wrong number of events", 2, events.size());
        
        TestHandler.EventInfo errorEvent = events.get(1);
        
        assertEquals("Wrong event type", TestHandler.Event.ERROR,
                     errorEvent.getEvent());
        
        CifError error = errorEvent.getError();
        
        assertNotNull("Null CifError", error);
        assertEquals("Error on wrong line number", 3,
                     error.getScanState().getLineNumber());
        assertEquals("Error at wrong character number", length + 1,
                     error.getScanState().getCharacterNumber());
    }
    
    public void testSetMaximumLineLength() {
    }

    private void assertMaxBlockNameLength(int length, CifScanner scanner) {
        StringBuffer cifData = new StringBuffer(length * 3 + 20);
        List<TestHandler.Event> sequence = new ArrayList<TestHandler.Event>();
        
        cifData.append("data_");
        for (int i = 0; i < length - 1; i++) {
            cifData.append('a');
        }
        sequence.add(TestHandler.Event.BLOCK_HEADER);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
        
        cifData.append("data_");
        for (int i = 0; i < length; i++) {
            cifData.append('b');
        }
        sequence.add(TestHandler.Event.BLOCK_HEADER);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
        
        cifData.append("data_");
        for (int i = 0; i < length + 1; i++) {
            cifData.append('c');
        }
        sequence.add(TestHandler.Event.ERROR);
        sequence.add(TestHandler.Event.BLOCK_HEADER);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
    
        setDefaultHandler(scanner);
        defaultHandler.clearEvents();
        try {
            scanner.scan(new StringReader(cifData.toString()));
        } catch (CifParseException cpe) {
            fail("Caught an unexpected CifParseException: " + cpe);
        } catch (IOException ioe) {
            fail("caught an unexpected IOException:" + ioe);
        }

        List<TestHandler.EventInfo> events = defaultHandler.getEvents();
        Iterator<TestHandler.Event> expectations = sequence.iterator();
        
        assertEquals("Wrong number of events", sequence.size(), events.size());
        for (TestHandler.EventInfo info : defaultHandler.getEvents()) {
            TestHandler.Event expected = expectations.next();
            
            assertEquals("Wrong event type", expected, info.getEvent());
            if (expected == TestHandler.Event.ERROR) {
                CifError error = info.getError();
                
                assertNotNull("Null CifError", error);
                assertEquals("Error on wrong line number", 3,
                             error.getScanState().getLineNumber());
            }
        }
    }
    
    public void testSetMaximumBlockNameLength() {
    }

    private void assertMaxFrameNameLength(int length, CifScanner scanner) {
        StringBuffer cifData = new StringBuffer(length * 3 + 30);
        List<TestHandler.Event> sequence = new ArrayList<TestHandler.Event>();
        
        cifData.append("data_test\n");
        sequence.add(TestHandler.Event.BLOCK_HEADER);
        sequence.add(TestHandler.Event.WHITESPACE);
        cifData.append("save_");
        for (int i = 0; i < length - 1; i++) {
            cifData.append('a');
        }
        sequence.add(TestHandler.Event.SAVE_FRAME_HEADER);
        cifData.append(" save_");
        sequence.add(TestHandler.Event.WHITESPACE);
        sequence.add(TestHandler.Event.SAVE_FRAME_END);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
        
        cifData.append("save_");
        for (int i = 0; i < length; i++) {
            cifData.append('b');
        }
        sequence.add(TestHandler.Event.SAVE_FRAME_HEADER);
        cifData.append(" save_");
        sequence.add(TestHandler.Event.WHITESPACE);
        sequence.add(TestHandler.Event.SAVE_FRAME_END);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
        
        cifData.append("save_");
        for (int i = 0; i < length + 1; i++) {
            cifData.append('c');
        }
        sequence.add(TestHandler.Event.ERROR);
        sequence.add(TestHandler.Event.SAVE_FRAME_HEADER);
        cifData.append(" save_");
        sequence.add(TestHandler.Event.WHITESPACE);
        sequence.add(TestHandler.Event.SAVE_FRAME_END);
        cifData.append('\n');
        sequence.add(TestHandler.Event.WHITESPACE);
    
        setDefaultHandler(scanner);
        defaultHandler.clearEvents();
        try {
            scanner.scan(new StringReader(cifData.toString()));
        } catch (CifParseException cpe) {
            fail("Caught an unexpected CifParseException: " + cpe);
        } catch (IOException ioe) {
            fail("caught an unexpected IOException:" + ioe);
        }

        List<TestHandler.EventInfo> events = defaultHandler.getEvents();
        Iterator<TestHandler.Event> expectations = sequence.iterator();
        
        assertEquals("Wrong number of events", sequence.size(), events.size());
        for (TestHandler.EventInfo info : defaultHandler.getEvents()) {
            TestHandler.Event expected = expectations.next();
            
            assertEquals("Wrong event type", expected, info.getEvent());
            if (expected == TestHandler.Event.ERROR) {
                CifError error = info.getError();
                
                assertNotNull("Null CifError", error);
                assertEquals("Error on wrong line number", 4,
                             error.getScanState().getLineNumber());
            }
        }
    }
    
    public void testSetMaximumFrameNameLength() {
    }

    public void testSetMaximumDataNameLength() {
    }

    public void testSetSquareBracketSpecial() {
    }

    public void testSetVTAllowed() {
    }

    public void testSetFFAllowed() {
    }

    public void testUseCif10Rules() {
    }

    public void testUseCif11Rules() {
    }

    public void testScan() {
    }

    public void testCheckAttributes() {
    }

    private void setDefaultHandler(CifScanner scanner) {
        scanner.setTokenHandler(defaultHandler);
        scanner.setWhitespaceHandler(defaultHandler);
        scanner.setErrorHandler(defaultHandler);
    }
    
    private static class TestHandler implements CifTokenHandler,
            CifWhitespaceHandler, CifErrorHandler {
        
        public enum Event {
            BLOCK_HEADER,
            SAVE_FRAME_HEADER,
            SAVE_FRAME_END,
            LOOP_START,
            DATA_NAME,
            QUOTED_VALUE,
            UNQUOTED_VALUE,
            WHITESPACE,
            COMMENT,
            ERROR
        }
        
        public static class EventInfo {
            private final Event event;
            private final String token;
            private final ScanState state;
            private final char delim;
            private final CifError error;
            
            public EventInfo(CifError error) {
                this(Event.ERROR, null, null, '\0', error);
            }
            
            public EventInfo(Event event, ScanState state) {
                this(event, null, state);
            }
            
            public EventInfo(Event event, String token, ScanState state) {
                this(event, token, state, '\0');
            }
            
            public EventInfo(Event event, String token, ScanState state,
                    char delim) {
                this(event, token, state, delim, null);
            }
            
            public EventInfo(Event event, String token, ScanState state,
                    char delim, CifError error) {
                this.event = event;
                this.token = token;
                this.state = state;
                this.delim = delim;
                this.error = error;
            }
            
            /**
             * 
             * TODO: Write documentation
             * 
             * @return this {@code EventInfo}'s {@code Event}
             */
            public Event getEvent() {
                return event;
            }
            
            /**
             * TODO: write documentation
             * 
             * @return Returns the delim.
             */
            public char getDelim() {
                return delim;
            }

            /**
             * TODO: write documentation
             * 
             * @return Returns the error.
             */
            public CifError getError() {
                return error;
            }

            /**
             * TODO: write documentation
             * 
             * @return Returns the state.
             */
            public ScanState getState() {
                return state;
            }

            /**
             * TODO: write documentation
             * 
             * @return Returns the token.
             */
            public String getToken() {
                return token;
            }
        }

        private final List<EventInfo> events = new ArrayList<EventInfo>();
        
        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleBlockHeader(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleBlockHeader(String blockName, ScanState state) {
            events.add(new EventInfo(Event.BLOCK_HEADER, blockName, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleSaveFrameHeader(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleSaveFrameHeader(String frameName, ScanState state) {
            events.add(new EventInfo(Event.SAVE_FRAME_HEADER, frameName,
                                     state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleSaveFrameEnd(org.recipnet.common.files.cif.ScanState)
         */
        public void handleSaveFrameEnd(ScanState state) {
            events.add(new EventInfo(Event.SAVE_FRAME_END, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleLoopStart(org.recipnet.common.files.cif.ScanState)
         */
        public void handleLoopStart(ScanState state) {
            events.add(new EventInfo(Event.LOOP_START, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleDataName(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleDataName(String name, ScanState state) {
            events.add(new EventInfo(Event.DATA_NAME, name, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleQuotedValue(java.lang.String, char, org.recipnet.common.files.cif.ScanState)
         */
        public void handleQuotedValue(String value, char delimiter, 
                ScanState state) {
            events.add(new EventInfo(Event.QUOTED_VALUE, value, state,
                                     delimiter));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifTokenHandler#handleUnquotedValue(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleUnquotedValue(String value, ScanState state) {
            events.add(new EventInfo(Event.UNQUOTED_VALUE, value, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifWhitespaceHandler#handleWhitespace(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleWhitespace(String s, ScanState state) {
            events.add(new EventInfo(Event.WHITESPACE, s, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifWhitespaceHandler#handleComment(java.lang.String, org.recipnet.common.files.cif.ScanState)
         */
        public void handleComment(String comment, ScanState state) {
            events.add(new EventInfo(Event.COMMENT, comment, state));
        }

        /**
         * {@inheritDoc}
         * 
         * TODO: write additional documentation
         * 
         * @see org.recipnet.common.files.cif.CifErrorHandler#handleError(org.recipnet.common.files.cif.CifError)
         */
        public void handleError(CifError error) {
            events.add(new EventInfo(error));
        }
        
        /**
         * 
         * TODO: Write documentation
         *
         */
        public void clearEvents() {
            events.clear();
        }
        
        /**
         * TODO: write documentation
         * 
         * @return Returns the events.
         */
        public List<EventInfo> getEvents() {
            return events;
        }
    }

    private final static String testCif1 =
        "# a comment\ndata_foo save_bar _baz 1 save_ _bat 'fudge'\n"
        + "loop_ _data_name_1\t_data_name_2 1 '2' buckle\r\n;my shoe\r; "
        + "? . \"shut the\" door _five_six pick _up 'sticks'";
}
