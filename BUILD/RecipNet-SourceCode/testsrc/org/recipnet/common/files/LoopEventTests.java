/*
 * Reciprocal Net Project
 *
 * LoopEventTests.java
 * 
 * 30-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import junit.framework.TestCase;

/**
 * A JUnit {@code TestCase} the exercises the behavior of the
 * {@code CifFile.LoopEvent} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class LoopEventTests extends TestCase {
    
    private CifFile.DataLoop loop;
    private CifFile.LoopEvent.EventCode code;

    /**
     * Initializes a {@code LoopEventTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public LoopEventTests(String testName) {
        super(testName);
    }
    
    /**
     * Prepares this {@code TestCase} to run a test
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        loop = new CifFile.DataLoop();
        code = CifFile.LoopEvent.EventCode.NAME_ADDED;
    }

    /**
     * Tests the behavior of the four-arg constructor when the
     * {@code CifFile.LoopEvent.EventCode} argument is {@code null}.  A
     * {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_String_int__nullCode() {
        try {
            new CifFile.LoopEvent(null, loop, "_foo", 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the four-arg constructor when the
     * {@code CifFile.DataLoop} argument is {@code null}.  A
     * {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_String_int__nullDataLoop() {
        try {
            new CifFile.LoopEvent(code, null, "_foo", 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the four-arg constructor when all arguments are
     * valid.  The arguments should be recoverable from the constructed object
     */
    public void testConstructor_EventCode_DataLoop_String_int() {
        CifFile.LoopEvent event;
        
        event = new CifFile.LoopEvent(code, loop, "_foo", 0);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertEquals("Wrong data name", "_foo", event.getDataName());
        assertEquals("Wrong record number", 0, event.getRecordNumber());

        event = new CifFile.LoopEvent(code, loop, null, 0);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong data name", event.getDataName());
        assertEquals("Wrong record number", 0, event.getRecordNumber());

        event = new CifFile.LoopEvent(code, loop, "_foo", -1);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertEquals("Wrong data name", "_foo", event.getDataName());
        assertEquals("Wrong record number", -1, event.getRecordNumber());
        
        event = new CifFile.LoopEvent(code, loop, null, Integer.MIN_VALUE);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong data name", event.getDataName());
        assertEquals("Wrong record number", Integer.MIN_VALUE,
                     event.getRecordNumber());
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop, String)}
     * constructor when the {@code CifFile.LoopEvent.EventCode} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_String__nullCode() {
        try {
            new CifFile.LoopEvent(null, loop, "_foo");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop, String)}
     * constructor when the {@code CifFile.DataLoopEvent} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_String__nullDataLoop() {
        try {
            new CifFile.LoopEvent(code, null, "_foo");
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the  {@code LoopEvent(EventCode, DataLoop, String)}
     * constructor when all arguments are valid.  The arguments and default
     * values should be recoverable from the constructed object
     */
    public void testConstructor_EventCode_DataLoop_String() {
        CifFile.LoopEvent event;
        
        event = new CifFile.LoopEvent(code, loop, "_foo");
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertEquals("Wrong data name", "_foo", event.getDataName());
        assertTrue("Wrong default record number", event.getRecordNumber() < 0);

        event = new CifFile.LoopEvent(code, loop, null);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong data name", event.getDataName());
        assertTrue("Wrong default record number", event.getRecordNumber() < 0);
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop, int)}
     * constructor when the {@code CifFile.LoopEvent.EventCode} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_int__nullCode() {
        try {
            new CifFile.LoopEvent(null, loop, 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop, int)}
     * constructor when the {@code CifFile.DataLoopEvent} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop_int__nullDataLoop() {
        try {
            new CifFile.LoopEvent(code, null, 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the  {@code LoopEvent(EventCode, DataLoop, int)}
     * constructor when all arguments are valid.  The arguments and default
     * values should be recoverable from the constructed object
     */
    public void testConstructor_EventCode_DataLoop_int() {
        CifFile.LoopEvent event;
        
        event = new CifFile.LoopEvent(code, loop, 0);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong default data name", event.getDataName());
        assertEquals("Wrong record number", 0, event.getRecordNumber());

        event = new CifFile.LoopEvent(code, loop, -1);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong default data name", event.getDataName());
        assertEquals("Wrong record number", -1, event.getRecordNumber());
        
        event = new CifFile.LoopEvent(code, loop, Integer.MIN_VALUE);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong default data name", event.getDataName());
        assertEquals("Wrong record number", Integer.MIN_VALUE,
                     event.getRecordNumber());
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop)}
     * constructor when the {@code CifFile.LoopEvent.EventCode} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop__nullCode() {
        try {
            new CifFile.LoopEvent(null, loop);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code LoopEvent(EventCode, DataLoop)}
     * constructor when the {@code CifFile.DataLoopEvent} argument is
     * {@code null}.  A {@code NullPointerException} is expected
     */
    public void testConstructor_EventCode_DataLoop__nullDataLoop() {
        try {
            new CifFile.LoopEvent(code, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the  {@code LoopEvent(EventCode, DataLoop)}
     * constructor when all arguments are valid.  The arguments and default
     * values should be recoverable from the constructed object
     */
    public void testConstructor_EventCode_DataLoop() {
        CifFile.LoopEvent event;
        
        event = new CifFile.LoopEvent(code, loop);
        assertEquals("Wrong event code", code, event.getEventCode());
        assertEquals("Wrong source", loop, event.getSource());
        assertNull("Wrong default data name", event.getDataName());
        assertTrue("Wrong default record number", event.getRecordNumber() < 0);
    }
}
