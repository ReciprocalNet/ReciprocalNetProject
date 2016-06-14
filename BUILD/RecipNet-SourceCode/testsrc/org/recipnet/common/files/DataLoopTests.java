/*
 * Reciprocal Net Project
 *
 * DataLoopTests.java
 * 
 * Mar 31, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.recipnet.common.files.CifFile.DataLoop;
import org.recipnet.common.files.CifFile.LoopEvent;
import org.recipnet.common.files.cif.UnknownValue;

import junit.framework.TestCase;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code CifFile.DataLoop} class
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class DataLoopTests extends TestCase {
    
    private CifFile.DataLoop testLoop;
    private List<String> dataNameList;
    private List<String> invalidNames;

    /**
     * Initializes a new {@code DataLoopTests} to perform the named test
     * 
     * @param  testName the name of the test to run
     */
    public DataLoopTests(String testName) {
        super(testName);
    }
    
    /**
     * Prepares this {@code TestCase} to run a test
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() {
        testLoop = new CifFile.DataLoop();
        dataNameList = new ArrayList<String>();
        Collections.addAll(dataNameList, "_a", "_b", "_c");
        invalidNames = new ArrayList<String>();
        Collections.addAll(invalidNames, "", "_", "foo", "_b ar",
                           "_ baz", "_m\u0005mble", "_bat\r");
    }
    
    /**
     * Tests the behavior of the nullary constructor.  An instance created via
     * that constructor should initially be empty
     */
    public void testConstructor() {
        assertEquals("Wrong record count", 0, testLoop.getRecordCount());
        assertEquals("Wrong record size", 0, testLoop.getRecordSize());
        assertTrue("Data name list is not empty",
                   testLoop.getDataNames().isEmpty());
        assertTrue("Lowercase name list is not empty",
                   testLoop.getLowerCaseNames().isEmpty());
    }
    
    /**
     * Tests the behavior of the two-arg constructor when its second argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor_List_nullList() {
        try {
            new CifFile.DataLoop(dataNameList, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor when its first argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor_nullList_List() {
        try {
            new CifFile.DataLoop(null, new ArrayList<CifFile.CifValue>());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor when the number of elements
     * in the second {@code List} is not a multiple of the number in the first;
     * an {@code IllegalArgumentException} is expected
     */
    public void testConstructor_List_List__badValueCount() {
        try {
            new CifFile.DataLoop(dataNameList,
                    Collections.nCopies(dataNameList.size() + 1,
                                        new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor when the first {@code List}
     * contains an invalid data name; an {@code IllegalArgumentException} is
     * expected
     */
    public void testConstructor_List_List__invalidName() {
        List<String> names = new ArrayList<String>();
        
        names.add("");
        performConstructorInvalidNameTest(names);
        
        names.clear();
        names.add("_");
        performConstructorInvalidNameTest(names);
        
        names.clear();
        names.add("bad");
        performConstructorInvalidNameTest(names);
        
        names.clear();
        names.add("_foo");
        names.add("bad");
        performConstructorInvalidNameTest(names);
        
        names.clear();
        names.add("bad");
        names.add("_foo");
        performConstructorInvalidNameTest(names);
        
        names.clear();
        names.add("_bar");
        names.add("bad");
        names.add("_foo");
        performConstructorInvalidNameTest(names);
    }
    
    /**
     * Attempts to construct a {@CifFile.DataLoop} containing an invalid data
     * name, and assserts that an {@code IllegalArgumentException} is thrown
     * 
     * @param  names a {@code List<String>} containing the data names to use
     */
    private void performConstructorInvalidNameTest(List<String> names) {
        try {
            new CifFile.DataLoop(names, new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor when the first {@code List}
     * contains a duplicate data name; an {@code IllegalArgumentException} is
     * expected
     */
    public void testConstructor_List_List__duplicateName() {
        List<String> redundantNames = new ArrayList<String>();
        
        Collections.addAll(redundantNames, "_foo", "_bar", "_baz", "_bat",
                           "_dingle", "_baz", "_berry");
        try {
            new CifFile.DataLoop(redundantNames, Collections.nCopies(
                    redundantNames.size(), new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor when the first {@code List}
     * is empty; an {@code IllegalArgumentException} is expected
     */
    public void testConstructor_emptyList_List() {
        List<String> names = new ArrayList<String>();

        try {
            new CifFile.DataLoop(names, new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }

        try {
            new CifFile.DataLoop(names,
                                 Collections.nCopies(2, new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the two-arg constructor, specifically that it
     * correctly stores the provided data names and values, correctly determines
     * the list of lowercase data names, and that the initial record size and
     * count are correct
     */
    public void testConstructor_List_List() {
        List<String> names = new ArrayList<String>();
        List<CifFile.CifValue> values = new ArrayList<CifFile.CifValue>();
        List<CifFile.CifValue> novalues = new ArrayList<CifFile.CifValue>();
        
        for (int i = 0; i < 12; i++) {
            values.add(new TestCifValue());
        }
        
        // Record size 1
        names.add("_UPPERlower_0");
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
        verifyLoop(new CifFile.DataLoop(names, novalues), names, novalues);
        
        // Record size 2
        names.add("_UPPERlower_1");
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
        verifyLoop(new CifFile.DataLoop(names, novalues), names, novalues);
        
        // Record size 3
        names.add("_UPPERlower_2");
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
        verifyLoop(new CifFile.DataLoop(names, novalues), names, novalues);
        
        // Record size 4
        names.add("_UPPERlower_3");
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
        verifyLoop(new CifFile.DataLoop(names, novalues), names, novalues);
        
        // Record size 6
        names.add("_UPPERlower_4");
        names.add("_UPPERlower_5");
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
        
        // Record size 12
        for (int i = 6; i < 12; i++) {
            names.add("_UPPERlower_" + i);
        }
        verifyLoop(new CifFile.DataLoop(names, values), names, values);
    }

    /**
     * Tests the behavior of {@code addName} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_addName_nullString() {
        try {
            testLoop.addName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addName} when its argument is not a valid
     * CIF data name; a {@code NullPointerException} is expected
     */
    public void testMethod_addName_invalidString() {
        for (String name : invalidNames) {
            try {
                testLoop.addName(name);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code addName} when invoked on a loop that has
     * been emptied and not reset; an {@code IllegalStateException} is expected
     */
    public void testMethod_addName_String__notReset() {
        testLoop.addName("_name");
        testLoop.removeName("_name");
        
        try {
            testLoop.addName("_name");
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addName} when invoked with a valid data
     * name that matches one already in the target loop; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_duplicateString() {
        for (String name : dataNameList) {
            testLoop.addName(name);
        }
        for (String name : dataNameList) {
            try {
                testLoop.addName(name);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the normal behavior of {@code addName(String)}; the added name
     * should appear at the end of the name list, and the {@code UnknownValue}
     * should be inserted at its position into all existing loop records
     */
    public void testMethod_addName_String() {
        testLoop.addName("_lowerUPPER_0");
        verifyLoop(testLoop, Collections.singletonList("_lowerUPPER_0"),
                   new ArrayList<CifFile.CifValue>());
        
        for (int nrecord : new int[] {0, 1, 4}) {
            for (int nfield = 1; nfield < 5; nfield++) {
                List<String> names = new ArrayList<String>();
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                List<CifFile.CifValue> values = new ArrayList<CifFile.CifValue>();
                String name = "_lowerUpper_" + nfield;

                names.add(name);
                assertFalse("The loop already contains name '" + name + "'",
                            loop.containsName(name));
                loop.addName(name);
                assertTrue("The loop does not contain name '" + name + "'",
                            loop.containsName(name));
                for (List<CifFile.CifValue> record : records) {
                    values.addAll(record);
                    values.add(UnknownValue.instance);
                }
                
                verifyLoop(loop, names, values);
            }
        }
    }

    /**
     * Tests the normal behavior of {@code addName(String)} with respect to
     * firing loop events; all name additions should be submitted for approval
     * to all registered listeners, and should be cancelled if any rejects it 
     */
    public void testMethod_addName_String__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            CifFile.LoopEvent event = new CifFile.LoopEvent( 
                    CifFile.LoopEvent.EventCode.NAME_ADDED, loop, "_new_name");
            
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            loop.addName("_new_name");
            assertCorrectEvents(listeners, Collections.singletonList(event), true);
            assertCorrectEvents(listeners, Collections.singletonList(event), false);
            
            listeners.get(nlistener / 2).approve = false;
            try {
                loop.addName("_another_name");
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
            assertFalse(loop.containsName("_another_name"));
            for (DummyLoopListener listener : listeners) {
                assertEquals("Wrong number of events", 1,
                             listener.changeEvents.size());
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int)} method when the
     * specified string is {@code null}; a {@code NullPointerException} is
     * expected
     */
    public void testMethod_addName_nullString_int() {
        try {
            testLoop.addName(null, 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int)} method when the
     * specified string is not a valid CIF data name;
     * an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_invalidString_int() {
        for (String name : invalidNames) {
            try {
                testLoop.addName(name, 0);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int)} method when the
     * specified string matches a data name already in the loop;
     * an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_duplicateString_int() {
        for (String name : dataNameList) {
            testLoop.addName(name);
        }
        for (String name : dataNameList) {
            try {
                testLoop.addName(name, 0);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int)} method when the
     * index for addition is less than zero or greater than the loop's record
     * size; an {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_addName_String_badint() {
        try {
            testLoop.addName("_name", -1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        
        testLoop.addName("_f");
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        
        testLoop.addName("_g");
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int)} method when the
     * loop has been emptied and not reset; an {@code IllegalStateException} is
     * expected
     */
    public void testMethod_addName_String_int__notReset() {
        testLoop.addName("_name");
        testLoop.removeName("_name");
        
        try {
            testLoop.addName("_name", 0);
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of {@code addName(String, int)}; the added name
     * should appear at the specified position in the name list, and the
     * {@code UnknownValue} should be inserted at that position into all
     * existing loop records
     */
    public void testMethod_addName_String_int() {
        testLoop.addName("_lowerUPPER_0", 0);
        verifyLoop(testLoop, Collections.singletonList("_lowerUPPER_0"),
                   new ArrayList<CifFile.CifValue>());
        
        for (int nrecord : new int[] {0, 1, 4}) {
            for (int nfield = 1; nfield < 5; nfield++) {
                for (int pos = 0; pos <= nfield; pos++) {
                    List<String> names = new ArrayList<String>();
                    List<CifFile.CifValue>[] records = new List[nrecord];
                    CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                    List<CifFile.CifValue> values
                            = new ArrayList<CifFile.CifValue>();
                    String name = "_lowerUpper_" + nfield;

                    names.add(pos, "_lowerUpper_" + nfield);
                    assertFalse("The loop already contains name '" + name + "'",
                                loop.containsName(name));
                    loop.addName("_lowerUpper_" + nfield, pos);
                    assertTrue("The loop does not contain name '" + name + "'",
                                loop.containsName(name));
                    
                    for (List<CifFile.CifValue> record : records) {
                        values.addAll(record.subList(0, pos));
                        values.add(UnknownValue.instance);
                        values.addAll(record.subList(pos, record.size()));
                    }
                    
                    verifyLoop(loop, names, values);
                }
            }
        }
    }
    
    /**
     * Tests the normal behavior of {@code addName(String, int)} with respect to
     * firing loop events; all name additions should be submitted for approval
     * to all registered listeners, and should be cancelled if any rejects it 
     */
    public void testMethod_addName_String_int__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            CifFile.LoopEvent event = new CifFile.LoopEvent( 
                    CifFile.LoopEvent.EventCode.NAME_ADDED, loop, "_new_name");
            
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            loop.addName("_new_name", 0);
            assertCorrectEvents(listeners, Collections.singletonList(event), true);
            assertCorrectEvents(listeners, Collections.singletonList(event), false);
            
            listeners.get(nlistener / 2).approve = false;
            try {
                loop.addName("_another_name", 0);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
            assertFalse(loop.containsName("_another_name"));
            for (DummyLoopListener listener : listeners) {
                assertEquals("Wrong number of events", 1,
                             listener.changeEvents.size());
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the specified string is {@code null}; a {@code NullPointerException}
     * is expected
     */
    public void testMethod_addName_nullString_int_CifValue() {
        try {
            testLoop.addName(null, 0, new TestCifValue());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the specified string is not a valid CIF data name; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_invalidString_int_CifValue() {
        for (String name : invalidNames) {
            try {
                testLoop.addName(name, 0, new TestCifValue());
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the target loop already contains a data name matching the specified
     * one; an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_duplicateString_int_CifValue() {
        for (String name : dataNameList) {
            testLoop.addName(name);
        }
        for (String name : dataNameList) {
            try {
                testLoop.addName(name, 0, new TestCifValue());
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the index for addition is less than zero or greater than the loop's
     * record size; an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addName_String_badint_CifValue() {
        try {
            testLoop.addName("_name", -1, new TestCifValue());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1,
                             new TestCifValue());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        
        testLoop.addName("_f");
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1,
                             new TestCifValue());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        
        testLoop.addName("_g");
        try {
            testLoop.addName("_name", testLoop.getRecordSize() + 1,
                             new TestCifValue());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the {@code CifValue} is {@code null}; a {@code NullPointerException}
     * is expected
     */
    public void testMethod_addName_String_int_nullCifValue() {
        try {
            testLoop.addName("_name", 0, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addName(String, int, CifValue)} method
     * when the loop has been cleared and not reset; an
     * {@code IllegalStateException} is expected
     */
    public void testMethod_addName_String_int_CifValue__notReset() {
        testLoop.addName("_name");
        testLoop.removeName("_name");
        
        try {
            testLoop.addName("_name", 0, new TestCifValue());
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of {@code addName(String, int, CifValue)}; the
     * added name should appear at the specified position in the name list, and
     * the specified value should be inserted at that position into all
     * existing loop records
     */
    public void testMethod_addName_String_int_CifValue() {
        testLoop.addName("_lowerUPPER_0", 0, new TestCifValue());
        verifyLoop(testLoop, Collections.singletonList("_lowerUPPER_0"),
                   new ArrayList<CifFile.CifValue>());
        
        for (int nrecord : new int[] {0, 1, 4}) {
            for (int nfield = 1; nfield < 5; nfield++) {
                for (int pos = 0; pos <= nfield; pos++) {
                    List<String> names = new ArrayList<String>();
                    List<CifFile.CifValue>[] records = new List[nrecord];
                    CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                    List<CifFile.CifValue> values
                            = new ArrayList<CifFile.CifValue>();
                    CifFile.CifValue val = new TestCifValue();
                    String name = "_lowerUpper_" + nfield;

                    names.add(pos, "_lowerUpper_" + nfield);
                    assertFalse("The loop already contains name '" + name + "'",
                                loop.containsName(name));
                    loop.addName("_lowerUpper_" + nfield, pos, val);
                    assertTrue("The loop does not contain name '" + name + "'",
                                loop.containsName(name));
                    
                    for (List<CifFile.CifValue> record : records) {
                        values.addAll(record.subList(0, pos));
                        values.add(val);
                        values.addAll(record.subList(pos, record.size()));
                    }
                    
                    verifyLoop(loop, names, values);
                }
            }
        }
    }

    /**
     * Tests the normal behavior of {@code addName(String, int, CifValue)} with
     * respect to firing loop events; all name additions should be submitted for
     * approval to all registered listeners, and should be cancelled if any
     * rejects it 
     */
    public void testMethod_addName_String_int_CifValue__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            CifFile.LoopEvent event = new CifFile.LoopEvent( 
                    CifFile.LoopEvent.EventCode.NAME_ADDED, loop, "_new_name");
            
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            loop.addName("_new_name", 0, new TestCifValue());
            assertCorrectEvents(listeners, Collections.singletonList(event), true);
            assertCorrectEvents(listeners, Collections.singletonList(event), false);
            
            listeners.get(nlistener / 2).approve = false;
            try {
                loop.addName("_another_name", 0, new TestCifValue());
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
            assertFalse(loop.containsName("_another_name"));
            for (DummyLoopListener listener : listeners) {
                assertEquals("Wrong number of events", 1,
                             listener.changeEvents.size());
            }
        }
    }
    
    /**
     * Tests the behavior of {@code containsName} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_containsName_nullString() {
        try {
            testLoop.containsName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of the {@code containsName(String)} method; it should
     * return true if the loop contains the data name (case insensitive test),
     * and false if the loop does not
     */
    public void testMethod_containsName_String() {
        String name = "_nAMe3*#_kl.Jq$";
        CifFile.DataLoop loop;
        
        loop = prepareLoop(3, 0);
        assertFalse("the loop already contains the test name",
                    loop.containsName(name));
        assertFalse("the loop already contains the test name (lowercase)",
                    loop.containsName(name.toLowerCase()));
        assertFalse("the loop already contains the test name (uppercase)",
                    loop.containsName(name.toUpperCase()));
        loop.addName(name);
        assertTrue("the loop does not contain the test name",
                    loop.containsName(name));
        assertTrue("the loop does not contain the test name (lowercase)",
                    loop.containsName(name.toLowerCase()));
        assertTrue("the loop does not contain the test name (uppercase)",
                    loop.containsName(name.toUpperCase()));
        
        loop = prepareLoop(1, 5);
        assertFalse("the loop already contains the test name",
                    loop.containsName(name));
        assertFalse("the loop already contains the test name (lowercase)",
                    loop.containsName(name.toLowerCase()));
        assertFalse("the loop already contains the test name (uppercase)",
                    loop.containsName(name.toUpperCase()));
        loop.addName(name);
        assertTrue("the loop does not contain the test name",
                    loop.containsName(name));
        assertTrue("the loop does not contain the test name (lowercase)",
                    loop.containsName(name.toLowerCase()));
        assertTrue("the loop does not contain the test name (uppercase)",
                    loop.containsName(name.toUpperCase()));
    }

    /**
     * Tests the behavior of {@code removeName} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_removeName_nullString() {
        try {
            testLoop.removeName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the behavior of {@code removeName(String)} when its argument is
     * does not match any name in the loop; an {@code IllegalArgumentException}
     * is expected
     */
    public void testMethod_removeName_String__noMatch() {
        try {
            testLoop.removeName("_name");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
        testLoop.addName("_nam");
        testLoop.addName("_named");
        testLoop.addName("_rename");
        testLoop.addName("_renamed");
        try {
            testLoop.removeName("_name");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        
    }

    /**
     * Tests the behavior of {@code removeName(String)} under a variety of
     * normal conditions
     */
    public void testMethod_removeName_String() {
        for (int nfields = 1; nfields < 5; nfields++) {
            for (int nrecords = 0; nrecords < 4; nrecords++) {
                for (int toremove = 0; toremove < nfields; toremove++) {
                    List<String> names = new ArrayList<String>();
                    List<CifFile.CifValue>[] records = new List[nrecords];
                    CifFile.DataLoop loop = prepareLoop(names, nfields, records);
                    List<CifFile.CifValue> values
                            = new ArrayList<CifFile.CifValue>();
                    String name = getStandardName(toremove);

                    names.remove(toremove);
                    for (List<CifFile.CifValue> record : records) {
                        values.addAll(record.subList(0, toremove));
                        values.addAll(record.subList(toremove + 1, record.size()));
                    }
                    assertTrue("The loop does not contain name '" + name + "'",
                               loop.containsName(name));
                    loop.removeName(name);
                    assertFalse("The loop still contains name '" + name + "'",
                               loop.containsName(name));
                    verifyLoop(loop, names, values);
                }
            }
        }
    }

    /**
     * Tests the normal behavior of {@code removeName(String)} with respect to
     * firing loop events; all name removals should trigger events fired to all
     * listeners; and removal of the last name should trigger a
     * {@code LOOP_EMPTIED} event
     */
    public void testMethod_removeName_String__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
                
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            for (int i : new int[] {1, 1, 0}) {
                String name = loop.getDataNames().get(i);
                
                events.add(new CifFile.LoopEvent( 
                        CifFile.LoopEvent.EventCode.NAME_REMOVED, loop, name));
                loop.removeName(name);
            }
            events.add(new CifFile.LoopEvent(
                    CifFile.LoopEvent.EventCode.LOOP_EMPTIED, loop));
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the behavior of {@code getDataNames}, specifically that the
     * returned {@code List} is unmodifiable
     */
    public void testMethod_getDataNames__isUnmodifiable() {
        testLoop.addName("_foo");
        testLoop.addName("_bar");
        List<String> dataNames = testLoop.getDataNames();
        
        try {
            dataNames.add("_baz");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        try {
            dataNames.remove("_foo");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        for (Iterator<String> it = dataNames.iterator(); it.hasNext(); ) {
            it.next();
            try {
                it.remove();
                fail("Expected an UnsupportedOperationException");
            } catch (UnsupportedOperationException uoe) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code getDataNames}, specifically that the
     * returned {@code List} tracks changes to the underlying loop
     */
    public void testMethod_getDataNames__tracksContents() {
        List<String> dataNames = testLoop.getDataNames();

        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_foo"));
        testLoop.addName("_foo");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_foo"));
        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_bar"));
        testLoop.addName("_bar");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_bar"));
        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_baz"));
        testLoop.addName("_baz");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_baz"));
        testLoop.removeName("_bar");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_bar"));
        testLoop.removeName("_foo");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_foo"));
        testLoop.removeName("_baz");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_baz"));
    }
    
    /**
     * Tests the behavior of {@code getLowerCaseNames}, specifically that the
     * returned {@code List} is unmodifiable
     */
    public void testMethod_getLowerCaseNames__isUnmodifiable() {
        testLoop.addName("_foo");
        testLoop.addName("_bar");
        List<String> dataNames = testLoop.getLowerCaseNames();
        
        try {
            dataNames.add("_baz");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        try {
            dataNames.remove("_foo");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // The expected case
        }
        
        for (Iterator<String> it = dataNames.iterator(); it.hasNext(); ) {
            it.next();
            try {
                it.remove();
                fail("Expected an UnsupportedOperationException");
            } catch (UnsupportedOperationException uoe) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code getLowerCaseNames}, specifically that the
     * returned {@code List} tracks changes to the underlying loop
     */
    public void testMethod_getLowerCaseNames__tracksContents() {
        List<String> dataNames = testLoop.getLowerCaseNames();

        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_foo"));
        testLoop.addName("_foo");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_foo"));
        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_bar"));
        testLoop.addName("_bar");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_bar"));
        assertFalse("dataNames already contains the chosen name",
                    dataNames.contains("_baz"));
        testLoop.addName("_baz");
        assertTrue("dataNames does not contain the chosen name",
                    dataNames.contains("_baz"));
        testLoop.removeName("_bar");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_bar"));
        testLoop.removeName("_foo");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_foo"));
        testLoop.removeName("_baz");
        assertFalse("dataNames still contains the chosen name",
                    dataNames.contains("_baz"));
    }

    /**
     * Tests the behavior of {@code addRecord(List)} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_addRecord_nullList() {
        try {
            testLoop.addRecord(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(List)} when its argument is
     * the wrong length; an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addRecord_List__wrongLength() {
        addDummyLoopHeader();
        try {
            testLoop.addRecord(Collections.nCopies(dataNameList.size() + 1,
                                                   new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            testLoop.addRecord(Collections.nCopies(dataNameList.size() - 1,
                                                   new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            testLoop.addRecord(new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(List)} when invoked on a loop
     * having no data names; an {@code IllegalStateException} is expected
     */
    public void testMethod_addRecord_List__empty() {
        try {
            testLoop.addRecord(new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addRecord(List)} method to verify that
     * it increases the record count by one, that it does not change the record
     * size, and that after invoking it, the record values can be retrieved as
     * the last record of the loop ({@code getRecordValues()})
     */
    public void testMethod_addRecord_List() {
        List<CifFile.CifValue> record = new ArrayList<CifFile.CifValue>();
        
        for (int nfield = 1; nfield < 5; nfield++) {
            record.add(new TestCifValue());
            for (int nrecord = 0; nrecord < 4; nrecord++) {
                CifFile.DataLoop loop = prepareLoop(nfield, nrecord);

                assertEquals("Wrong initial record count", nrecord,
                             loop.getRecordCount());
                assertEquals("Wrong initial record size", nfield,
                             loop.getRecordSize());
                loop.addRecord(record);
                assertEquals("Wrong final record count", nrecord + 1,
                             loop.getRecordCount());
                assertEquals("Wrong final record size", nfield,
                             loop.getRecordSize());
                assertEquals("Cannot retrieve record", record,
                             loop.getRecordValues(nrecord));
            }
        }
    }

    /**
     * Tests the normal behavior of {@code addRecord(List)} with respect to
     * firing loop events; all record additions should trigger events fired to
     * all listeners
     */
    public void testMethod_addRecord_List__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
            List<CifFile.CifValue> record = loop.getRecordValues(0);
            
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }

            events.add(new CifFile.LoopEvent( 
                    CifFile.LoopEvent.EventCode.RECORD_ADDED, loop,
                    loop.getRecordCount()));
            loop.addRecord(record);
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(int, List)} when its int argument
     * is less than zero or greater than current number of loop records; a
     * {@code NullPointerException} is expected
     */
    public void testMethod_addRecord_badint_List() {
        List<TestCifValue> values
                = Collections.nCopies(dataNameList.size(), new TestCifValue());

        addDummyLoopHeader();
        
        try {
            testLoop.addRecord(-1, values);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.addRecord(1, values);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(int, List)} when its List argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_addRecord_int_nullList() {
        try {
            testLoop.addRecord(0, null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(int, List)} when its List argument
     * is the wrong length; an {@code IllegalArgumentException} is expected
     */
    public void testMethod_addRecord_int_List__wrongLength() {
        addDummyLoopHeader();
        try {
            testLoop.addRecord(0, Collections.nCopies(
                    testLoop.getRecordSize() + 1, new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            testLoop.addRecord(0, Collections.nCopies(
                    testLoop.getRecordSize() - 1, new TestCifValue()));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
        try {
            testLoop.addRecord(0, new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code addRecord(int, List)} when invoked on a loop
     * having no data names; an {@code IllegalStateException} is expected
     */
    public void testMethod_addRecord_int_List__empty() {
        try {
            testLoop.addRecord(0, new ArrayList<CifFile.CifValue>());
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addRecord(int, List)} method to verify
     * that it increases the record count by one, that it does not change the
     * record size, and that after invoking it, the record values can be
     * retrieved as the specified record of the loop ({@code getRecordValues()})
     */
    public void testMethod_addRecord_int_List() {
        List<CifFile.CifValue> record = new ArrayList<CifFile.CifValue>();
        
        for (int nfield = 1; nfield < 5; nfield++) {
            record.add(new TestCifValue());
            for (int nrecord = 0; nrecord < 4; nrecord++) {
                for (int pos = 0; pos <= nrecord; pos++) {
                    CifFile.DataLoop loop = prepareLoop(nfield, nrecord);

                    assertEquals("Wrong initial record count", nrecord,
                                 loop.getRecordCount());
                    assertEquals("Wrong initial record size", nfield,
                                 loop.getRecordSize());
                    loop.addRecord(pos, record);
                    assertEquals("Wrong final record count", nrecord + 1,
                                 loop.getRecordCount());
                    assertEquals("Wrong final record size", nfield,
                                 loop.getRecordSize());
                    assertEquals("Cannot retrieve record", record,
                                 loop.getRecordValues(pos));
                }
            }
        }
    }

    /**
     * Tests the normal behavior of {@code addRecord(List, int)} with respect to
     * firing loop events; all record additions should trigger events fired to
     * all listeners
     */
    public void testMethod_addRecord_List_int__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
            List<CifFile.CifValue> record = loop.getRecordValues(0);
            
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }

            for (int pos : new int[] {0, 2, 1}) {
                events.add(new CifFile.LoopEvent( 
                        CifFile.LoopEvent.EventCode.RECORD_ADDED, loop, pos));
                loop.addRecord(pos, record);
            }
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the behavior of {@code getRecordValues(int)} when invoked with a
     * an invalid record number; an {@code IndexOutOfBoundsException} is
     * expected
     */
    public void testMethod_getRecordValues_badint() {
        performGetBadRecordIndexTest();

        addDummyLoopHeader();
        performGetBadRecordIndexTest();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performGetBadRecordIndexTest();
        }
    }

    /**
     * Invokes {@code testLoop.getRecordValues(int)} with several different
     * invalid record numbers, asserting in each case that the method throws an
     * IndexOutOfBoundsException
     */
    private void performGetBadRecordIndexTest() {
        try {
            testLoop.getRecordValues(Integer.MIN_VALUE);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getRecordValues(-1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getRecordValues(testLoop.getRecordCount());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getRecordValues(Integer.MAX_VALUE);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        } catch (IllegalArgumentException iae) {
            // Workaround a bug in J2SE 1.5.0
            // TODO: remove this catch block when the JRE is debugged
        }
    }

    /**
     * Tests the behavior of the {@code getRecordValues(int)} method when its
     * argument is valid, specifically that its return value is mutable and
     * independent of the loop; other details of this method's normal behavior
     * are exercised by the tests for other methods
     */
    public void testMethod_getRecordValues_int() {
        for (int nfield = 1; nfield < 4; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                for (int pos = 0 ; pos < nrecord; pos++) {
                    CifFile.DataLoop loop = prepareLoop(nfield, nrecord);
                    List<CifFile.CifValue> values
                            = new ArrayList<CifFile.CifValue>();
                    List<CifFile.CifValue> values1;
                    List<CifFile.CifValue> values2;

                    values = loop.getRecordValues(pos);
                    values1 = new ArrayList<CifFile.CifValue>(values);
                    
                    values.add(new TestCifValue());
                    values2 = loop.getRecordValues(pos);
                    assertEquals("Loop values changed", values1, values2);
                    
                    values = loop.getRecordValues(pos);
                    values.remove(0);
                    values2 = loop.getRecordValues(pos);
                    assertEquals("Loop values changed", values1, values2);
                }
            }
        }
    }
    
    /**
     * Tests the behavior of {@code removeRecord(int)} when invoked with a
     * an invalid record number; an {@code IndexOutOfBoundsException} is
     * expected
     */
    public void testMethod_removeRecord_badint() {
        performRemoveBadRecordIndexTest();
        
        addDummyLoopHeader();
        performRemoveBadRecordIndexTest();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performRemoveBadRecordIndexTest();
        }
    }
    
    /**
     * Invokes {@code testLoop.removeRecord(int)} with several different
     * invalid record numbers, asserting in each case that the method throws an
     * IndexOutOfBoundsException
     */
    private void performRemoveBadRecordIndexTest() {
        try {
            testLoop.removeRecord(Integer.MIN_VALUE);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.removeRecord(-1);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.removeRecord(testLoop.getRecordCount());
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.removeRecord(Integer.MAX_VALUE);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        } catch (IllegalArgumentException iae) {
            // Workaround for a bug in the J2SE 1.5.0 library
            // TODO: remove this catch block when the JRE is debugged
        }
    }

    /**
     * Tests the normal behavior of {@code removeRecord(int)}; the loop record
     * designated by the specified index should be removed without disturbing
     * other records or the data names
     */
    public void testMethod_removeRecord_int() {
        for (int nfield = 1; nfield < 3; nfield++) {
            for (int nrecord = 1; nrecord < 4; nrecord++) {
                List<CifFile.CifValue>[] records = new List[nrecord];
                
                for (int toRemove = 0; toRemove < nrecord; toRemove++) {
                    List<String> names = new ArrayList<String>();
                    CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                    List<CifFile.CifValue> values
                            = new ArrayList<CifFile.CifValue>();

                    for (int i = 0; i < nrecord; i++) {
                        if (i == toRemove) {
                            continue;
                        }
                        values.addAll(records[i]);
                    }
                    loop.removeRecord(toRemove);
                    verifyLoop(loop, names, values);
                }
            }
        }
    }

    /**
     * Tests the normal behavior of {@code removeRecord(int)} with respect to
     * firing loop events; all removals should trigger events fired to all
     * listeners; and removal of the last record should trigger a
     * {@code LOOP_EMPTIED} event
     */
    public void testMethod_removeRecord_int__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
                
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            for (int pos : new int[] {1, 1, 0}) {
                events.add(new CifFile.LoopEvent( 
                        CifFile.LoopEvent.EventCode.RECORD_REMOVED, loop, pos));
                loop.removeRecord(pos);
            }
            events.add(new CifFile.LoopEvent(
                    CifFile.LoopEvent.EventCode.LOOP_EMPTIED, loop));
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the behavior of {@code getValuesForName} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_getValuesForName_nullString() {
        try {
            testLoop.getValuesForName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} exhibits the correct behavior when the specified
     * data name is not in the loop; the expected behavior is that the supported
     * methods (of this unmodifiable {@code List}) throw
     * {@code IllegalStateException}
     */
    public void testFeature_getValuesForName_String__noMatch() {
        for (int nfield = 0; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                CifFile.DataLoop loop = prepareLoop(nfield, nrecord);
                
                assertIllegalLoopValueState(loop.getValuesForName("_foo"));
                assertIllegalLoopValueState(loop.getValuesForName(""));
                assertIllegalLoopValueState(loop.getValuesForName(" "));
                assertIllegalLoopValueState(loop.getValuesForName("bar"));
                assertIllegalLoopValueState(loop.getValuesForName("_baz\n42"));
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} contains the correct values when the specified
     * (case insensitive) data name is in the loop
     */
    public void testFeature_getValuesForName_String() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                List<String> names = new ArrayList<String>();
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                
                for (int field = 0; field < nfield; field++) {
                    String name = names.get(field);
                    
                    assertCorrectValues(loop.getValuesForName(name), field,
                                        records);
                    assertCorrectValues(loop.getValuesForName(name.toUpperCase()),
                                        field, records);
                    assertCorrectValues(loop.getValuesForName(name.toLowerCase()),
                                        field, records);
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to the subject data name being
     * removed from the loop.  After the removal, the list's supported operations
     * should throw {@code IllegalStateException}.
     */
    public void testFeature_getValuesForName_String__removeSubject() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                List<String> names = new ArrayList<String>();
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                
                for (int field = 0; field < nfield; field++) {
                    String name = names.get(field);
                    List<CifFile.CifValue> values = loop.getValuesForName(name);
                    
                    assertCorrectValues(values, field, records);
                    loop.removeName(name);
                    assertIllegalLoopValueState(values);
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to the subject data name being
     * added to the loop.  After the addition, the list's supported operations
     * should return the correct value list.
     */
    public void testFeature_getValuesForName_String__addSubject() {
        final String toAdd = "_not_in_the_loop";
        final CifFile.CifValue val = new TestCifValue();
        
        for (int nfield = 0; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < ((nfield == 0) ? 1 : 5); nrecord++) {
                for (int pos = 0; pos <= nfield; pos++) {
                    List<String> names = new ArrayList<String>();
                    List<CifFile.CifValue>[] records = new List[nrecord];
                    CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                    List<CifFile.CifValue> values = loop.getValuesForName(toAdd);
                    
                    assertIllegalLoopValueState(values);
                    loop.addName(toAdd, pos, val);
                    for (List<CifFile.CifValue> record : records) {
                        record.add(pos, val);
                    }
                    assertCorrectValues(values, pos, records);
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to another data name being
     * removed from the loop.  After the removal, the list's supported
     * operations should (still) return the correct value list.
     */
    public void testFeature_getValuesForName_String__removeOther() {
        for (int nfield = 2; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                for (int testField = 0; testField < nfield; testField++) {
                    for (int removeField = 0; removeField < nfield; removeField++) {
                        if (removeField == testField) {
                            continue;
                        }
                        
                        List<String> names = new ArrayList<String>();
                        List<CifFile.CifValue>[] records = new List[nrecord];
                        CifFile.DataLoop loop
                                = prepareLoop(names, nfield, records);
                        List<CifFile.CifValue> values
                                = loop.getValuesForName(names.get(testField));

                        loop.removeName(names.get(removeField));
                        for (List<CifFile.CifValue> record : records) {
                            record.remove(removeField);
                        }
                        assertCorrectValues(values,
                                testField - ((removeField < testField) ? 1 : 0),
                                records);
                    }
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to another data name being
     * added to the loop.  After the addition, the list's supported
     * operations should (still) return the correct value list.
     */
    public void testFeature_getValuesForName_String__addOther() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                for (int testField = 0; testField < nfield; testField++) {
                    for (int addPos = 0; addPos <= nfield; addPos++) {
                        List<String> names = new ArrayList<String>();
                        List<CifFile.CifValue>[] records = new List[nrecord];
                        CifFile.DataLoop loop
                                = prepareLoop(names, nfield, records);
                        List<CifFile.CifValue> values
                                = loop.getValuesForName(names.get(testField));

                        loop.addName("_Foo_BAR_baz", addPos);
                        for (List<CifFile.CifValue> record : records) {
                            record.add(addPos, null);  // don't care about the value
                        }
                        assertCorrectValues(values,
                                testField + ((addPos <= testField) ? 1 : 0),
                                records);
                    }
                }
            }
        }
    }
    
    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to a record being added to the
     * loop.  After the addition, the list's supported operations should
     * return the correct (updated) value list.
     */
    public void testFeature_getValuesForName_String__addData() {
        for (int nfield = 1; nfield < 5; nfield++) {
            List<CifFile.CifValue> newRecord = new ArrayList<CifFile.CifValue>();
            
            for (int i = 0; i < nfield; i++) {
                newRecord.add(new TestCifValue());
            }
    
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                for (int testField = 0; testField < nfield; testField++) {
                    for (int addPos = 0; addPos <= nrecord; addPos++) {
                        List<String> names = new ArrayList<String>();
                        List<CifFile.CifValue>[] records = new List[nrecord];
                        CifFile.DataLoop loop
                                = prepareLoop(names, nfield, records);
                        List<CifFile.CifValue> values
                                = loop.getValuesForName(names.get(testField));
                        List<List<CifFile.CifValue>> recordList
                                = new ArrayList<List<CifFile.CifValue>>(Arrays.asList(records));
                        
                        loop.addRecord(addPos, newRecord);
                        recordList.add(addPos, newRecord);
                        records = recordList.toArray(records);
                        assertCorrectValues(values, testField, records);
                    }
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to a value being modified in the
     * loop.  After the modification, the list's supported operations should
     * return the correct (updated) value list.
     */
    public void testFeature_getValuesForName_String__modifyData() {
        CifFile.CifValue val = new TestCifValue();
        
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                for (int testField = 0; testField < nfield; testField++) {
                    for (int testRec = 0; testRec < nrecord; testRec++) {
                        for (int modField = 0; modField < nfield; modField++) {
                            List<String> names = new ArrayList<String>();
                            List<CifFile.CifValue>[] records = new List[nrecord];
                            CifFile.DataLoop loop
                                    = prepareLoop(names, nfield, records);
                            List<CifFile.CifValue> values
                                    = loop.getValuesForName(names.get(testField));
                            
                            loop.setValue(modField, testRec, val);
                            records[testRec].set(modField, val);
                            assertCorrectValues(values, testField, records);
                        }
                    }
                }
            }
        }
    }

    /**
     * Tests the {@code getValuesForName(String)} method to verify that the
     * returned {@code List} correctly responds to a record being removed from
     * the loop.  After the addition, the list's supported operations should
     * return the correct (updated) value list.
     */
    public void testFeature_getValuesForName_String__removeData() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                for (int testField = 0; testField < nfield; testField++) {
                    for (int removePos = 0; removePos < nrecord; removePos++) {
                        List<String> names = new ArrayList<String>();
                        List<CifFile.CifValue>[] records = new List[nrecord];
                        CifFile.DataLoop loop
                                = prepareLoop(names, nfield, records);
                        List<CifFile.CifValue> values
                                = loop.getValuesForName(names.get(testField));
                        List<List<CifFile.CifValue>> recordList
                                = new ArrayList<List<CifFile.CifValue>>(Arrays.asList(records));
                        
                        loop.removeRecord(removePos);
                        recordList.remove(removePos);
                        records = recordList.toArray(new List[nrecord - 1]);
                        assertCorrectValues(values, testField, records);
                    }
                }
            }
        }
    }

    /**
     * Tests the {@code List} returned by the {@code getValuesForName(String}}
     * method of the supplied loop to verify that it exhibits the correct
     * behavior for the case where the loop does not contain the specified name
     * 
     * @param  values the {@code List} to test
     */
    private void assertIllegalLoopValueState(List<CifFile.CifValue> values) {
        
        assertNotNull("null value list", values);
        try {
            values.size();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        try {
            values.iterator();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        try {
            values.get(0);
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
    }
    
    /**
     * Tests the {@code List} returned by the {@code getValuesForName(String}}
     * method of the supplied loop to verify that it contains the correct values
     * for the specified name
     * 
     * @param  values the {@code List<CifFile.CifValue>} to test
     * @param  field the field index of the specified name in the loop
     * @param  records an array containing the supposed values of the loop records
     */
    private void assertCorrectValues(List<CifFile.CifValue> values,
                                     int field, List<CifFile.CifValue>[] records) {
        List<CifFile.CifValue> expected = new ArrayList<CifFile.CifValue>();
        
        for (List<CifFile.CifValue> record : records) {
            expected.add(record.get(field));
        }
        
        assertEquals("Wrong values returned", expected, values);
    }
    
    /**
     * Tests the behavior of {@code getValue(String, int} when its String
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_getValue_nullString_int() {
        addDummyLoopHeader();
        addDummyLoopRecord();
        try {
            testLoop.getValue(null, 0);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }

        addDummyLoopRecord();
        addDummyLoopRecord();
        try {
            testLoop.getValue(null, 1);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code getValue(String, int} when its int argument
     * is invalid; an {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_getValue_String_badint() {
        addDummyLoopHeader();
        performGetValueBadIntTest();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performGetValueBadIntTest();
        }
    }
    
    /**
     * Invokes {@code testLoop.getValue(String, int)} with several different
     * invalid record numbers, asserting in each case that the method throws an
     * IndexOutOfBoundsException
     */
    private void performGetValueBadIntTest() {
        for (String name : dataNameList) {
            try {
                testLoop.getValue(name, -1);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(name, testLoop.getRecordCount());
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(name, Integer.MIN_VALUE);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(name, Integer.MAX_VALUE);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code getValue(String, int)} when its String
     * argument does not match any data name in the loop; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod_getValue_String_int__noMatch() {
        addDummyLoopHeader();
        addDummyLoopRecord();
        for (String missingName : invalidNames) {
            try {
                testLoop.getValue(missingName, 0);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
        try {
            testLoop.getValue("_i_am_a_valid_name_not_in_the_loop", 0);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of {@code getValue(String, int)}; the correct
     * value should be retrieved from the loop
     */
    public void testMethod_getValue_String_int() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                List<String> names = new ArrayList<String>();
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                
                for (int fieldNum = 0; fieldNum < names.size(); fieldNum++) {
                    String name = names.get(fieldNum);
                    
                    for (int recordNum = 0; recordNum < records.length;
                            recordNum++) {
                        assertEquals("Wrong value retrieved",
                                     records[recordNum].get(fieldNum),
                                     loop.getValue(name, recordNum));
                    }
                }
            }
        }
    }

    /**
     * Tests the behavior of {@code getValue(int, int} when its first argument
     * is less than zero or greater than or equal to the loop record size; an
     * {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_getValue_badint_int() {
        addDummyLoopHeader();
        addDummyLoopRecord();
        try {
            testLoop.getValue(-1, 0);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getValue(testLoop.getRecordSize(), 0);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getValue(Integer.MIN_VALUE, 0);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.getValue(Integer.MAX_VALUE, 0);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code getValue(int, int)} when its second int
     * argument is invalid; an {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_getValue_int_badint() {
        addDummyLoopHeader();
        performGetValueBadInt2Test();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performGetValueBadInt2Test();
        }
    }
    
    /**
     * Invokes {@code testLoop.getValue(int, int)} with several different
     * invalid record numbers, asserting in each case that the method throws an
     * IndexOutOfBoundsException
     */
    private void performGetValueBadInt2Test() {
        for (int i = 0; i < testLoop.getRecordSize(); i++) {
            try {
                testLoop.getValue(i, -1);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(i, testLoop.getRecordCount());
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(i, Integer.MIN_VALUE);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.getValue(i, Integer.MAX_VALUE);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
        }
    }
    
    /*
     * Tests for normal operation of getValue(int, int) are incorporated into
     * other tests
     */
    
    /**
     * Tests the behavior of {@code setValue(String, int, CifValue)} when its
     * String argument is null; a {@code NullPointerException} is expected
     */
    public void testMethod_setValue_nullString_int_CifValue() {
        addDummyLoopHeader();
        addDummyLoopRecord();
        try {
            testLoop.setValue(null, 0, new TestCifValue());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
        
        addDummyLoopRecord();
        addDummyLoopRecord();
        try {
            testLoop.setValue(null, 1, new TestCifValue());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code setValue(String, int, CifValue)} when its
     * int argument is not a valid record index; an
     * {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_setValue_String_badint_CifValue() {
        addDummyLoopHeader();
        performSetValueBadIntTest();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performSetValueBadIntTest();
        }
    }

    /**
     * Invokes {@code testLoop.getValue(String, int)} with several different
     * invalid record numbers, asserting in each case that the method throws an
     * IndexOutOfBoundsException
     */
    private void performSetValueBadIntTest() {
        CifFile.CifValue val = new TestCifValue();
        
        for (String name : dataNameList) {
            try {
                testLoop.setValue(name, -1, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(name, testLoop.getRecordCount(), val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(name, Integer.MIN_VALUE, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(name, Integer.MAX_VALUE, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code setValue(String, int, CifValue)} when its
     * CifValue argument is null; a {@code NullPointerException} is expected
     */
    public void testMethod_setValue_String_int_nullCifValue() {
        addDummyLoopHeader();
        
        for (int lastRecord = 0; lastRecord < 5; lastRecord++) {
            addDummyLoopRecord();
            for (String name : testLoop.getDataNames()) {
                for (int record = 0; record < testLoop.getRecordCount(); record++) {
                    try {
                        testLoop.setValue(name, record, null);
                        fail("Expected a NullPointerException");
                    } catch (NullPointerException npe) {
                        // The expected case
                    }
                }
            }
        }
    }
    
    /**
     * Tests the behavior of {@code setValue(String, int, CifValue)} when its
     * String argument does not match any data name in the loop; an
     * {@code IllegalArgumentException} is expected
     */
    public void testMethod_setValue_String_int_CifValue__noMatch() {
        CifFile.CifValue val = new TestCifValue();
        
        addDummyLoopHeader();
        addDummyLoopRecord();
        for (String missingName : invalidNames) {
            try {
                testLoop.setValue(missingName, 0, val);
                fail("Expected an IllegalArgumentException");
            } catch (IllegalArgumentException iae) {
                // The expected case
            }
        }
        try {
            testLoop.setValue("_i_am_a_valid_name_not_in_the_loop", 0, val);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of {@code setValue(String, int, CifValue)}; the
     * value set should be read back correctly via a corresponding
     * {@code getValue(int, int)} invocation
     */
    public void testMethod_setValue_String_int_CifValue() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                List<String> names = new ArrayList<String>();
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(names, nfield, records);
                
                for (int fieldNum = 0; fieldNum < names.size(); fieldNum++) {
                    String name = names.get(fieldNum);
                    
                    for (int recordNum = 0; recordNum < records.length;
                            recordNum++) {
                        CifFile.CifValue val = new TestCifValue();
                        
                        assertFalse("Value already in place",
                                loop.getValue(fieldNum, recordNum).equals(val));
                        loop.setValue(name, recordNum, val);
                        assertEquals("Value not correctly set", val,
                                     loop.getValue(fieldNum, recordNum));
                    }
                }
            }
        }
    }
    
    /**
     * Tests the normal behavior of {@code setValue(String, int, CifValue)} with
     * respect to firing loop events; all data changes should trigger events
     * fired to all registered listeners 
     */
    public void testMethod_setValue_String_int_CifValue__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<String> names = loop.getDataNames();
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
                
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            for (String name : names) {
                for (int record = 0; record < loop.getRecordCount(); record++) {
                    events.add(new CifFile.LoopEvent( 
                            CifFile.LoopEvent.EventCode.DATUM_MODIFIED, loop,
                            name, record));
                    loop.setValue(name, record, new TestCifValue());
                }
            }
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the behavior of {@code getValue(int, int, CifValue)} when its first
     * argument is less than zero or greater than or equal to the loop record
     * size; an {@code IndexOutOfBoundsException} is expected
     */
    public void testMethod_setValue_badint_int_CifValue() {
        CifFile.CifValue val = new TestCifValue();

        addDummyLoopHeader();
        addDummyLoopRecord();
        try {
            testLoop.setValue(-1, 0, val);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.setValue(testLoop.getRecordSize(), 0, val);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.setValue(Integer.MIN_VALUE, 0, val);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
        try {
            testLoop.setValue(Integer.MAX_VALUE, 0, val);
            fail("Expected an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ioobe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code setValue(int, int, CifValue)} when its
     * second int argument is invalid; an {@code IndexOutOfBoundsException} is
     * expected
     */
    public void testMethod_setValue_int_badint_CifValue() {
        addDummyLoopHeader();
        performSetValueBadInt2Test();
        
        for (int i = 0; i < 5; i++) {
            addDummyLoopRecord();
            performSetValueBadInt2Test();
        }
    }
    
    /**
     * Invokes {@code testLoop.setValue(int, int, CifValue)} with several
     * different invalid record numbers, asserting in each case that the method
     * throws an IndexOutOfBoundsException
     */
    private void performSetValueBadInt2Test() {
        CifFile.CifValue val = new TestCifValue();
        
        for (int i = 0; i < testLoop.getRecordSize(); i++) {
            try {
                testLoop.setValue(i, -1, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(i, testLoop.getRecordCount(), val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(i, Integer.MIN_VALUE, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
            try {
                testLoop.setValue(i, Integer.MAX_VALUE, val);
                fail("Expected an IndexOutOfBoundsException");
            } catch (IndexOutOfBoundsException ioobe) {
                // The expected case
            }
        }
    }
    
    /**
     * Tests the behavior of {@code setValue(int, int, CifValue)} when its
     * CifValue argument is null; a {@code NullPointerException} is expected
     */
    public void testMethod_setValue_int_int_nullCifValue() {
        addDummyLoopHeader();
        
        for (int lastRecord = 0; lastRecord < 5; lastRecord++) {
            addDummyLoopRecord();
            for (int field = 0; field < testLoop.getRecordSize(); field++) {
                for (int record = 0; record < testLoop.getRecordCount(); record++) {
                    try {
                        testLoop.setValue(field, record, null);
                        fail("Expected a NullPointerException");
                    } catch (NullPointerException npe) {
                        // The expected case
                    }
                }
            }
        }
    }
    
    /**
     * Tests the normal behavior of {@code setValue(int, int, CifValue)}; the
     * value set should be read back correctly via a corresponding
     * {@code getValue(int, int)} invocation
     */
    public void testMethod_setValue_int_int_CifValue() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                CifFile.DataLoop loop = prepareLoop(nfield, nrecord);
                
                for (int fieldNum = 0; fieldNum < nfield; fieldNum++) {
                    for (int recordNum = 0; recordNum < nrecord;
                            recordNum++) {
                        CifFile.CifValue val = new TestCifValue();
                        
                        assertFalse("Value already in place",
                                loop.getValue(fieldNum, recordNum).equals(val));
                        loop.setValue(fieldNum, recordNum, val);
                        assertEquals("Value not correctly set", val,
                                     loop.getValue(fieldNum, recordNum));
                    }
                }
            }
        }
    }

    /**
     * Tests the normal behavior of {@code setValue(int, int, CifValue)} with
     * respect to firing loop events; all data changes should trigger events
     * fired to all registered listeners 
     */
    public void testMethod_setValue_int_int_CifValue__listeners() {
        for (int nlistener = 1; nlistener < 4; nlistener++) {
            CifFile.DataLoop loop = prepareLoop(3, 3);
            List<DummyLoopListener> listeners
                    = new ArrayList<DummyLoopListener>();
            List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
                
            for (int i = 0; i < nlistener; i++) {
                DummyLoopListener listener = new DummyLoopListener();
                
                listeners.add(listener);
                loop.addLoopListener(listener);
            }
            
            for (int field = 0; field < loop.getRecordSize(); field++) {
                String name = loop.getDataNames().get(field);
                
                for (int record = 0; record < loop.getRecordCount(); record++) {
                    events.add(new CifFile.LoopEvent( 
                            CifFile.LoopEvent.EventCode.DATUM_MODIFIED, loop,
                            name, record));
                    loop.setValue(name, record, new TestCifValue());
                }
            }
            assertCorrectEvents(listeners, events, false);
        }
    }
    
    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that the records it returns match those in the loop 
     */
    public void testfeature_recordIterator__iteration() {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 0; nrecord < 5; nrecord++) {
                List<CifFile.CifValue>[] records = new List[nrecord];
                CifFile.DataLoop loop = prepareLoop(new ArrayList<String>(),
                                                    nfield, records);
                Iterator<List<CifFile.CifValue>> it = loop.recordIterator();
                
                for (int recNum = 0 ; recNum < nrecord; recNum++) {
                    assertEquals("Records don't match", records[recNum],
                                 it.next());
                }
                assertFalse("Iterator has extra records", it.hasNext());
            }
        }
    }
    
    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it correctly removes records when called upon to
     * do so 
     */
    public void testfeature_recordIterator__removal() {
        List<DummyLoopListener> listeners = new ArrayList<DummyLoopListener>(3);
        List<CifFile.LoopEvent> events = new ArrayList<CifFile.LoopEvent>();
        
        for (int i = 0; i < 3; i++) {
            listeners.add(new DummyLoopListener());
        }
        
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                for (int toRemove = 0; toRemove < nrecord; toRemove++) {
                    List<CifFile.CifValue>[] records = new List[nrecord];
                    CifFile.DataLoop loop = prepareLoop(new ArrayList<String>(),
                                                        nfield, records);
                    Iterator<List<CifFile.CifValue>> it = loop.recordIterator();
                    
                    for (CifFile.LoopListener listener : listeners) {
                        loop.addLoopListener(listener);
                    }
                    
                    for (int recNum = 0 ; recNum < nrecord; recNum++) {
                        if (recNum == toRemove) {
                            events.add(new CifFile.LoopEvent( 
                                    CifFile.LoopEvent.EventCode.RECORD_REMOVED,
                                    loop, recNum));
                            it.next();
                            it.remove();
                            if (nrecord == 1) {
                                events.add(new CifFile.LoopEvent( 
                                        CifFile.LoopEvent.EventCode.LOOP_EMPTIED,
                                        loop));
                            }
                        } else {
                            assertEquals("Records don't match", records[recNum],
                                         it.next());
                        }
                    }
                    assertFalse("Iterator has extra records", it.hasNext());
                    
                    for (int recNum = toRemove; (recNum < (nrecord - 1)); recNum++) {
                        records[recNum] = records[recNum + 1];
                    }
                    it = loop.recordIterator();
                    for (int recNum = 0 ; (recNum < (nrecord - 1)); recNum++) {
                        assertEquals("Records don't match", records[recNum],
                                     it.next());
                    }
                    assertFalse("Iterator has extra records", it.hasNext());
                }
            }
        }
        assertCorrectEvents(listeners, events, false);
    }
    
    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the {@code addName(String)}
     * method
     */
    public void testfeature_recordIterator__failfast__addName() {
        performFailfastTest(new LoopOperation() {
            public void perform(DataLoop loop) {
                loop.addName("_new_name");
            }});
    }
    
    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the {@code removeName(String)}
     * method
     */
    public void testfeature_recordIterator__failfast__removeName() {
        performFailfastTest(new LoopOperation() {
            public void perform(DataLoop loop) {
                loop.removeName(loop.getDataNames().get(0));
            }});
    }

    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the {@code addRecord(List)}
     * method
     */
    public void testfeature_recordIterator__failfast__addRecord() {
        performFailfastTest(new LoopOperation() {
            public void perform(DataLoop loop) {
                List<CifFile.CifValue> record = new ArrayList<CifFile.CifValue>();

                for (int i = 0; i < loop.getRecordSize(); i++) {
                    record.add(new TestCifValue());
                }
                loop.addRecord(record);
            }});
    }

    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the {@code removeRecord(int)}
     * method
     */
    public void testfeature_recordIterator__failfast__removeRecord() {
        performFailfastTest(new LoopOperation() {
            public void perform(DataLoop loop) {
                loop.removeRecord(0);
            }});
    }

    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the
     * {@code setValue(int, int CifValue)} method
     */
    public void testfeature_recordIterator__failfast__setValue() {
        performFailfastTest(new LoopOperation() {
            public void perform(DataLoop loop) {
                loop.setValue(0, 0, new TestCifValue());
            }});
    }

    /**
     * Tests the {@code Iterator} returned by the {@code recordIterator()}
     * method to verify that it exhibits fail-fast behavior relative to
     * modification of the underlying loop via the method represented by the
     * specified {@code LoopOperation}
     * 
     * @param  op a {@code LoopOperation} that modifies the provided loop; used
     *         to verify the loop's fail-fast behavior
     */
    private void performFailfastTest(LoopOperation op) {
        for (int nfield = 1; nfield < 5; nfield++) {
            for (int nrecord = 1; nrecord < 5; nrecord++) {
                for (int iterations = 0; iterations < nrecord; iterations++) {
                    CifFile.DataLoop loop = prepareLoop(nfield, nrecord);
                    Iterator<List<CifFile.CifValue>> it = loop.recordIterator();
                    
                    for (int iteration = 0; iteration < iterations; iteration++) {
                        it.next();
                    }
                    
                    op.perform(loop);
                    try {
                        it.hasNext();
                        fail("Expected a ConcurrentModificationException");
                    } catch (ConcurrentModificationException cme) {
                        // the expected case
                    }
                    try {
                        it.next();
                        fail("Expected a ConcurrentModificationException");
                    } catch (ConcurrentModificationException cme) {
                        // the expected case
                    }
                    try {
                        it.remove();
                        fail("Expected a ConcurrentModificationException");
                    } catch (ConcurrentModificationException cme) {
                        // the expected case
                    }
                }
            }
        }
    }
    
    /**
     * Tests the behavior of {@code addLoopListener} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_addLoopListener_nullListener() {
        try {
            testLoop.addLoopListener(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of {@code fireLoopEvent} when its argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod_fireLoopEvent_nullLoopEvent() {
        try {
            testLoop.fireLoopEvent(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests that removing loop listeners indeed causes them to cease receiving
     * events, without interfering with other listeners continuing to receive
     * them
     */
    public void testFeature_loopListeners__removing() {
        CifFile.DataLoop loop = prepareLoop(3,3);
        List<DummyLoopListener> listeners = new ArrayList<DummyLoopListener>();
        Map<CifFile.LoopListener, List<CifFile.LoopEvent>> eventMap
                = new HashMap<CifFile.LoopListener, List<CifFile.LoopEvent>>();
        List<DummyLoopListener> currentListeners;
        CifFile.LoopEvent event;
        CifFile.LoopListener toRemove;
        
        for (int i = 0; i < 3; i++) {
            DummyLoopListener listener = new DummyLoopListener();
            
            listeners.add(listener);
            loop.addLoopListener(listener);
            eventMap.put(listener, new ArrayList<CifFile.LoopEvent>());
        }
        currentListeners = new ArrayList<DummyLoopListener>(listeners);
        
        event = new CifFile.LoopEvent(CifFile.LoopEvent.EventCode.NAME_ADDED,
                                      loop, "_added_name");
        loop.addName("_added_name");
        for (CifFile.LoopListener listener : currentListeners) {
            eventMap.get(listener).add(event);
        }
        
        event = new CifFile.LoopEvent(CifFile.LoopEvent.EventCode.RECORD_REMOVED,
                                      loop, 0);
        loop.removeRecord(0);
        for (CifFile.LoopListener listener : currentListeners) {
            eventMap.get(listener).add(event);
        }
        
        toRemove = currentListeners.get(1);
        loop.removeLoopListener(toRemove);
        currentListeners.remove(toRemove);

        String name = loop.getDataNames().get(0);
        event = new CifFile.LoopEvent(CifFile.LoopEvent.EventCode.NAME_REMOVED,
                                      loop, name);
        loop.removeName(name);
        for (CifFile.LoopListener listener : currentListeners) {
            eventMap.get(listener).add(event);
        }
        
        event = new CifFile.LoopEvent(CifFile.LoopEvent.EventCode.RECORD_REMOVED,
                                      loop, 1);
        loop.removeRecord(1);
        for (CifFile.LoopListener listener : currentListeners) {
            eventMap.get(listener).add(event);
        }
        
        toRemove = currentListeners.get(1);
        loop.removeLoopListener(toRemove);
        currentListeners.remove(toRemove);

        event = new CifFile.LoopEvent(CifFile.LoopEvent.EventCode.DATUM_MODIFIED,
                                      loop, "_added_name", 0);
        loop.setValue("_added_name", 0, new TestCifValue());
        for (CifFile.LoopListener listener : currentListeners) {
            eventMap.get(listener).add(event);
        }
        
        toRemove = currentListeners.get(0);
        loop.removeLoopListener(toRemove);
        
        loop.removeRecord(0);
        
        for (DummyLoopListener listener : listeners) {
            assertCorrectEvents(Collections.singletonList(listener),
                                eventMap.get(listener), false);
        }
    }

    private CifFile.DataLoop prepareLoop(int nFields, int nRecords) {
        return prepareLoop(new ArrayList<String>(), nFields, new List[nRecords]);
    }
    
    private CifFile.DataLoop prepareLoop(List<String> names, int nFields,
            List<CifFile.CifValue>[] records) {
        if (nFields == 0) {
            return new CifFile.DataLoop();
        } else {
            List<CifFile.CifValue> values = new ArrayList<CifFile.CifValue>();
            
            for (int i = 0; i < nFields; i++) {
                names.add(getStandardName(i));
            }
            for (int i = 0; i < records.length; i++) {
                records[i] = new ArrayList<CifFile.CifValue>();
                for (int j = 0; j < nFields; j++) {
                    records[i].add(new TestCifValue());
                }
                values.addAll(records[i]);
            }
            
            return new CifFile.DataLoop(names, values);
        }
    }
    
    /**
     * Adds the names of {@code dataNameList} to the test loop as data names
     */
    private void addDummyLoopHeader() {
        for (String name : dataNameList) {
            testLoop.addName(name);
        }
    }
    
    /**
     * Adds a dummy record to the test loop
     */
    private void addDummyLoopRecord() {
        List<TestCifValue> record =
            Collections.nCopies(testLoop.getRecordSize(), new TestCifValue());
        testLoop.addRecord(record);
    }

    /**
     * Examines the specifics of a {@code DataLoop} to verify that it corresponds
     * to the expected characteristics for one newly constructed from the two
     * provided lists 
     * 
     * @param  loop the {@code CifFile.DataLoop} to examine
     * @param  names the {@code List<String>} of data names
     * @param  values  the {@code List<? extends CifFile.CifValue>} of data
     *         values
     */
    private void verifyLoop(CifFile.DataLoop loop,
            List<String> names, List<? extends CifFile.CifValue> values) {
        List<String> lcNames = new ArrayList<String>();
        int nRecords = (names.isEmpty() ? 0 : (values.size() / names.size()));
        
        for (String name : names) {
            lcNames.add(name.toLowerCase());
        }
        assertEquals("Loop names are wrong", names, loop.getDataNames());
        assertEquals("Loop lowercase names are wrong", lcNames,
                     loop.getLowerCaseNames());
        assertEquals("Wrong record size", names.size(), loop.getRecordSize());
        assertEquals("Wrong number of records", nRecords, loop.getRecordCount());
        
        for (int record = 0; record < nRecords; record++) {
            for (int field = 0; field < names.size(); field++) {
                assertEquals("Loop values do not match",
                             values.get(record * names.size() + field),
                             loop.getValue(field, record));
            }
        }
    }
    
    /**
     * Checks that each of the provided {@code DummyLoopListener}s has an event
     * list matching the provided one 
     * 
     * @param  listeners the {@code List<DummyLoopListener>} of listeners to
     *         examine
     * @param  protoEvents the {@code List<CifFile.LoopEvent>} of events to
     *         compare to the listeners' recorded events
     * @param  approveList {@code true} to use the listeners' approval lists;
     *         otherwise their change lists are used
     */
    private void assertCorrectEvents(List<DummyLoopListener> listeners,
            List<CifFile.LoopEvent> protoEvents, boolean approveList) {
        for (DummyLoopListener listener : listeners) {
            List<CifFile.LoopEvent> elist = (approveList
                    ? listener.approveEvents : listener.changeEvents);
            
            assertEquals("Wrong number of events", protoEvents.size(),
                         elist.size());
            for (int i = 0; i < protoEvents.size(); i++) {
                CifFile.LoopEvent protoEvent = protoEvents.get(i);
                CifFile.LoopEvent event = elist.get(i);
                
                assertEquals("Wrong event type", protoEvent.getEventCode(),
                             event.getEventCode());
                assertEquals("Wrong event source", protoEvent.getSource(),
                             event.getSource());
                assertEquals("Wrong event data name", protoEvent.getDataName(),
                             event.getDataName());
                assertEquals("Wrong event record number",
                             protoEvent.getRecordNumber(),
                             event.getRecordNumber());
            }
        }
    }
    
    /**
     * Creates and returns a String containing a canonical name for the ith
     * field of a loop
     * 
     * @param  i the zero-based index of the field for which a name is desired
     * 
     * @return the name
     */
    private String getStandardName(int i) {
        return "_lowerUPPER_" + i;
    }
}

/**
 * A stub LoopListener implementation for testing purposes 
 * 
 * @author jobollin
 * @version 1.0
 */
class DummyLoopListener implements CifFile.LoopListener {
    
    /** records all the events supplied to {@code loopChanged(LoopEvent)} */
    List<LoopEvent> changeEvents = new ArrayList<LoopEvent>();
    
    /** records all the events supplied to {@code approveLoopChange(LoopEvent)} */
    List<LoopEvent> approveEvents = new ArrayList<LoopEvent>();
    
    /** the boolean value returned by {@code approveLoopChange(LoopEvent)} */
    boolean approve = true;
    
    /**
     * {@inheritDoc}
     * 
     * @see CifFile.LoopListener#loopChanged(CifFile.LoopEvent)
     */
    public void loopChanged(LoopEvent event) { 
        changeEvents.add(event);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see CifFile.LoopListener#approveLoopChange(CifFile.LoopEvent)
     */
    public boolean approveLoopChange(LoopEvent event) {
        approveEvents.add(event);
        return approve;
    }
}

/**
 * An interface representing objects that perform (class-defined) operations on
 * {@code CifFile.DataLoop} objects 
 * 
 * @author jobollin
 * @version 0.9.0
 */
interface LoopOperation {
    
    /**
     * Performs the operation defined by this {@code LoopOperation} on the
     * specified loop
     * 
     * @param  loop the {@code CifFile.DataLoop} on which to perform the
     *         operation
     */
    void perform(CifFile.DataLoop loop);
}
