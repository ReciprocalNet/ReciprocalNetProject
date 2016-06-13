/*
 * Reciprocal Net Project
 *
 * DataCellTests.java
 * 
 * 30-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * An abstract JUnit TestCase that exercises the behavior of the
 * {@code CifFile.DataCell} class; intended as a superclass for TestCases for
 * concrete DataCell subclasses 
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public abstract class DataCellTests extends TestCase {

    /** the data name {@code Collection} of the {@code DataCell} under test */
    protected Collection<String> dataNames;
    
    /**
     * Initializes a new {@code DataCellTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public DataCellTests(String testName) {
        super(testName);
    }
    
    /**
     * Prepares this test case to run a test; concrete subclasses should
     * delegate to this method after performing their own set up
     */
    @Override
    public void setUp() {
        dataNames = getTestSubject().getDataNames();
    }
    
    /**
     * Returns the {@code DataCell} instance currently under test.  The same
     * object should be returned at every invocation.
     * 
     * @return the {@code DataCell} instance currently under test
     */
    public abstract CifFile.DataCell getTestSubject();
    
    /**
     * Tests the behavior of the one-arg constructor when its argument is
     * {@code null}; NullPointerException is expected
     */
    public abstract void testConstructor_String__null();
    
    /**
     * Tests the behavior of the one-arg constructor with a non-null string
     * argument; the {@code getName()} method should return a {@code String}
     * equal to the one passed to the constructor, and the new data cell should
     * initially contain no data 
     */
    public abstract void testConstructor_String();

    /**
     * Tests the behavior of the {@code addScalar(CifFile.ScalarData)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_AddScalar__null() {
        try {
            getTestSubject().addScalar(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests several variations of attempting to add a scalar data item to a
     * data cell when the data name is already in use in the cell.  These cases
     * should all result in {@code IllegalArgumentException}
     */
    public void testMethod_AddScalar__duplicateName() {
        CifFile.DataCell cell = getTestSubject();
        CifFile.DataLoop loop = new CifFile.DataLoop();
        
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_unnAmED", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmEr", new TestCifValue()));
        loop.addName("_loopName1");
        loop.addName("_loopName2");
        loop.addRecord(Arrays.asList(
                new CifFile.CifValue[] {new TestCifValue(), new TestCifValue()}));
        cell.addLoop(loop);
        
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_name", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_NAME", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_unnAmED", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_unnamed", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_UNNAMED", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_nAmEr", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_namer", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_NAMER", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_loopName1", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_loopname1", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_LOOPNAME1", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_loopName2", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_loopname2", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_LOOPNAME2", new TestCifValue()));
    }

    /**
     * Attempts to add the specified {@code CifFile.ScalarData} to the current
     * test subject, asserting that the operation will fail with an
     * {@code IllegalArgumentException}
     * 
     * @param  scalar the {@code CifFile.ScalarData} to attempt to add
     */
    void assertAddScalarFails(CifFile.ScalarData<?> scalar) {
        try {
            getTestSubject().addScalar(scalar);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }
    
    /**
     * Tests the {@code addScalar} method to ensure that normal additions are
     * handled correctly.  After addition scalars should be retrievable by
     * (case insensitive) name, their case-insensitive names should be reported
     * as being contained by the cell (but not in a loop), and the cell's data
     * name collection should contain the lowercase version of the name.  
     */
    public void testMethod_AddScalar() {
        assertAddScalarSucceeds(
                new CifFile.ScalarData<TestCifValue>("_nAmE", new TestCifValue()));
        assertAddScalarSucceeds(
                new CifFile.ScalarData<TestCifValue>("_1#&_", new TestCifValue()));
        assertAddScalarSucceeds(
                new CifFile.ScalarData<TestCifValue>("_nAmEd", new TestCifValue()));
        assertAddScalarSucceeds(
                new CifFile.ScalarData<TestCifValue>("_unnAmEd", new TestCifValue()));
        assertAddScalarSucceeds(
                new CifFile.ScalarData<TestCifValue>("_Am", new TestCifValue()));
    }

    /**
     * Attempts to add the specified {@code CifFile.ScalarData} to the current
     * test subject, verifying as well as possible that the cell reflects the
     * addition afterward 
     * 
     * @param  scalar the {@code CifFile.ScalarData} to add
     */
    void assertAddScalarSucceeds(CifFile.ScalarData<?> scalar) {
        CifFile.DataCell cell = getTestSubject();
        CifFile.ScalarData<?> scalar2;
        
        assertFalse("Scalar already present in cell",
                    cell.containsName(scalar.getName()));
        assertFalse("Scalar already present in cell",
                    cell.containsName(scalar.getName().toLowerCase()));
        cell.addScalar(scalar);
        assertTrue("Name not added",
                    cell.containsName(scalar.getName()));
        assertTrue("Name not addded",
                    cell.containsName(scalar.getName().toLowerCase()));
        assertFalse("Name reported as looped",
                    cell.containsNameInLoop(scalar.getName()));
        assertFalse("Name reported as looped",
                    cell.containsNameInLoop(scalar.getName().toLowerCase()));
        assertTrue("Name not recognized by data names collection",
                   dataNames.contains(scalar.getName().toLowerCase()));
        scalar2 = cell.getScalarForName(scalar.getName());
        assertNotNull("Could not retrieve scalar", scalar2);
        assertEquals("Wrong scalar retrieved", scalar, scalar2);
        scalar2 = cell.getScalarForName(scalar.getName().toLowerCase());
        assertNotNull("Could not retrieve scalar", scalar2);
        assertEquals("Wrong scalar retrieved", scalar, scalar2);
        scalar2 = cell.getScalarForName(scalar.getName().toUpperCase());
        assertNotNull("Could not retrieve scalar", scalar2);
        assertEquals("Wrong scalar retrieved", scalar, scalar2);
    }
    
    /**
     * Tests the behavior of the {@code addLoop(CifFile.LoopData)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_AddLoop__null() {
        try {
            getTestSubject().addLoop(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addLoop(CifFile.LoopData)} method
     * when its argument is an empty loop.  An {@code IllegalArgumentException}
     * is expected.
     */
    public void testMethod_AddLoop_empty() {
        try {
            getTestSubject().addLoop(new CifFile.DataLoop());
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code addLoop(CifFile.LoopData)} method
     * when its argument contains data names already present in the cell.  An
     * {@code IllegalArgumentException} is expected.
     */
    public void testMethod_AddLoop__duplicateName() {
        CifFile.DataCell cell = getTestSubject();
        CifFile.DataLoop loop1 = new CifFile.DataLoop();
        CifFile.DataLoop loop2;

        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_n", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_loopdata", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_FOO", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_d1", new TestCifValue()));
        loop1.addName("_loopData1");
        loop1.addName("_N2");
        loop1.addRecord(Arrays.asList(new CifFile.CifValue[]
                {new TestCifValue(), new TestCifValue()}));
        cell.addLoop(loop1);
        
        loop2 = new CifFile.DataLoop();
        loop2.addName("_n");
        loop2.addRecord(Collections.singletonList(
                (CifFile.CifValue) new TestCifValue()));
        try {
            cell.addLoop(loop2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
        
        loop2.addName("_bar");
        try {
            cell.addLoop(loop2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }

        loop2.removeName("_n");
        loop2.addName("_loopdata1");
        try {
            cell.addLoop(loop2);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case
        }
    }

    /**
     * Tests the operation of the {@code addLoop} method to verify that it
     * correctly adds its loop argument without disrupting other data in the
     * {@code DataCell}
     */
    public void testMethod_AddLoop() {
        CifFile.DataCell cell = getTestSubject();
        CifFile.DataLoop loop1;
        CifFile.DataLoop loop2;
        CifFile.DataLoop testLoop;
        
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_n", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_loopdata", new TestCifValue()));
        
        loop1 = new CifFile.DataLoop();
        loop1.addName("_loopData1");
        loop1.addName("_N2");
        loop1.addRecord(Arrays.asList(new CifFile.CifValue[]
                {new TestCifValue(), new TestCifValue()}));
        cell.addLoop(loop1);
        assertTrue("Loop names not added", cell.containsName("_loopData1"));
        assertTrue("Loop names not added", cell.containsName("_N2"));
        assertTrue("Loop names not flagged as looped",
                   cell.containsNameInLoop("_loopData1"));
        assertTrue("Loop names not flagged as looped",
                   cell.containsNameInLoop("_N2"));
        assertTrue("Data name collection doe snot reflect addition",
                   dataNames.contains("_loopdata1"));
        assertTrue("Data name collection doe snot reflect addition",
                   dataNames.contains("_n2"));
        
        loop2 = new CifFile.DataLoop();
        loop2.addName("_loopData2");
        loop2.addName("_N3");
        loop2.addRecord(Arrays.asList(new CifFile.CifValue[]
                {new TestCifValue(), new TestCifValue()}));
        cell.addLoop(loop2);
        assertTrue("Loop names not added", cell.containsName("_loopData2"));
        assertTrue("Loop names not added", cell.containsName("_N3"));
        assertTrue("Loop names not flagged as looped",
                   cell.containsNameInLoop("_loopData2"));
        assertTrue("Loop names not flagged as looped",
                   cell.containsNameInLoop("_N3"));
        assertTrue("Data name collection doe snot reflect addition",
                   dataNames.contains("_loopdata2"));
        assertTrue("Data name collection doe snot reflect addition",
                   dataNames.contains("_n3"));
        
        testLoop = cell.getLoopForName("_loopData1");
        assertEquals("Wrong loop retrieved", loop1, testLoop);
        testLoop = cell.getLoopForName("_LOOPData1");
        assertEquals("Wrong loop retrieved", loop1, testLoop);
        testLoop = cell.getLoopForName("_n2");
        assertEquals("Wrong loop retrieved", loop1, testLoop);
        testLoop = cell.getLoopForName("_N2");
        assertEquals("Wrong loop retrieved", loop1, testLoop);
        
        testLoop = cell.getLoopForName("_loopData2");
        assertEquals("Wrong loop retrieved", loop2, testLoop);
        testLoop = cell.getLoopForName("_LOOPData2");
        assertEquals("Wrong loop retrieved", loop2, testLoop);
        testLoop = cell.getLoopForName("_n3");
        assertEquals("Wrong loop retrieved", loop2, testLoop);
        testLoop = cell.getLoopForName("_N3");
        assertEquals("Wrong loop retrieved", loop2, testLoop);
        
        assertTrue("Scalars displaced", cell.containsName("_name"));
        assertFalse("Scalars displaced", cell.containsNameInLoop("_name"));
        assertTrue("Scalars displaced", dataNames.contains("_name"));
        assertTrue("Scalars displaced", cell.containsName("_n"));
        assertFalse("Scalars displaced", cell.containsNameInLoop("_n"));
        assertTrue("Scalars displaced", dataNames.contains("_n"));
        assertTrue("Scalars displaced", cell.containsName("_loopdata"));
        assertFalse("Scalars displaced", cell.containsNameInLoop("_loopdata"));
        assertTrue("Scalars displaced", dataNames.contains("_loopdata"));
        
        assertTrue("Loop names displaced", cell.containsName("_loopData1"));
        assertTrue("Loop names displaced", cell.containsName("_N2"));
        assertTrue("Loop names de-flagged",
                   cell.containsNameInLoop("_loopData1"));
        assertTrue("Loop names de-flagged", cell.containsNameInLoop("_N2"));
        assertTrue("Data name collection damaged",
                   dataNames.contains("_loopdata1"));
        assertTrue("Data name collection damaged", dataNames.contains("_n2"));
    }

    /**
     * Tests the behavior of the {@code containsName(String)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_ContainsName__null() {
        try {
            getTestSubject().containsName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code containsNameInLoop(String)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_ContainsNameInLoop__null() {
        try {
            getTestSubject().containsNameInLoop(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code getScalarForName(String)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_GetScalarForName__null() {
        try {
            getTestSubject().getScalarForName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code getLoopForName(String)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_GetLoopForName__null() {
        try {
            getTestSubject().getLoopForName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code removeDataForName(String)} method
     * when its argument is {@code null}.  A {@code NullPointerException} is
     * expected.
     */
    public void testMethod_RemoveDataForName__null() {
        try {
            getTestSubject().removeDataForName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests several variations of attempting to remove data from a data cell
     * when the data name is not specified in the cell.  These cases
     * should all result in a {@code false} return value from
     * {@code removeDataForName(String)}
     */
    public void testMethod_RemoveDataForName__noMatch() {
        CifFile.DataCell cell = getTestSubject();
        CifFile.DataLoop loop = new CifFile.DataLoop();
        
        // no data present
        assertRemoveDataWorks("", false);
        assertRemoveDataWorks("_", false);
        assertRemoveDataWorks("_foo", false);
        
        // various data present
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));

        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_name", new TestCifValue()));
        assertAddScalarFails(new CifFile.ScalarData<TestCifValue>(
                "_NAME", new TestCifValue()));

        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_unnAmED", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmEr", new TestCifValue()));
        loop.addName("_loopName1");
        loop.addName("_loopName2");
        loop.addRecord(Arrays.asList(
                new CifFile.CifValue[] {new TestCifValue(), new TestCifValue()}));
        
        assertRemoveDataWorks("", false);
        assertRemoveDataWorks("_", false);
        assertRemoveDataWorks("_foo", false);
        assertRemoveDataWorks("_nAm", false);
        assertRemoveDataWorks("nAmE", false);
        assertRemoveDataWorks("_nAmErs", false);
        assertRemoveDataWorks("_loopName3", false);
        assertRemoveDataWorks("loopName1", false);
        assertRemoveDataWorks("_loopName", false);
    }
    
    /**
     * Tests the {@code removeDataForName(String)} method in various cases
     * where data are expected to be removed to ensure that the data are
     * removed correctly 
     */
    public void testMethod_RemoveDataForName() {
        CifFile.DataCell cell = getTestSubject();
        CifFile.DataLoop loop = new CifFile.DataLoop();

        // data to remove
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmE", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_unnAmED", new TestCifValue()));
        cell.addScalar(new CifFile.ScalarData<TestCifValue>(
                "_nAmEr", new TestCifValue()));
        loop.addName("_loopName1");
        loop.addName("_loopName2");
        loop.addName("_loopName3");
        loop.addRecord(Arrays.asList(new CifFile.CifValue[] {new TestCifValue(),
                new TestCifValue(), new TestCifValue()}));
        loop.addRecord(Arrays.asList(new CifFile.CifValue[] {new TestCifValue(),
                new TestCifValue(), new TestCifValue()}));
        loop.addRecord(Arrays.asList(new CifFile.CifValue[] {new TestCifValue(),
                new TestCifValue(), new TestCifValue()}));
        cell.addLoop(loop);
        
        assertRemoveDataWorks("_name", true);
        assertRemoveDataWorks("_LOOPNAME2", true);
        assertFalse("Data name removed from cell but not from loop",
                    loop.containsName("_loopName2"));
        assertRemoveDataWorks("_uNnameD", true);
        assertRemoveDataWorks("_loopName1", true);
        assertFalse("Data name removed from cell but not from loop",
                    loop.containsName("_loopName1"));
        assertRemoveDataWorks("_loopname3", true);
        assertFalse("Data name removed from cell but not from loop",
                    loop.containsName("_loopname3"));
        assertRemoveDataWorks("_nAmEr", true);
        
        // verify that the loop itself was removed
        loop.reset();
        loop.addName("_loopName4");
        loop.addRecord(Collections.singletonList(
                (CifFile.CifValue) new TestCifValue()));
        assertFalse("Loop not removed when emptied",
                    cell.containsName("_loopName4"));
        assertFalse("Loop not completely removed when emptied",
                    cell.containsNameInLoop("_loopName4"));
        assertFalse("Data names still associated with removed loop",
                    dataNames.contains("_loopname4"));
    }

    /**
     * Attempts to remove the data associated with the specified name from the
     * current test subject, asserting that the operation will return the
     * specified expected result and that the test subject will afterward
     * contain no data associated with the name 
     * 
     * @param  name the name of the data to attempt to remove
     * @param  expected the expected {@code boolean} return value for
     *         {@code removeDataForName(String)}
     */
    void assertRemoveDataWorks(String name, boolean expected) {
        CifFile.DataCell cell = getTestSubject();
        
        assertEquals("Wrong result value", expected,
                     cell.removeDataForName(name));
        assertFalse("Name still present", cell.containsName(name));
        assertFalse("Name still present (looped)", cell.containsNameInLoop(name));
        assertFalse("Data name collection still contains the name",
                    dataNames.contains(name.toLowerCase()));
        assertNull("Retrieved a scalar for a removed name",
                   cell.getScalarForName(name));
        assertNull("Retrieved a loop for a removed name",
                   cell.getLoopForName(name));
    }
}
