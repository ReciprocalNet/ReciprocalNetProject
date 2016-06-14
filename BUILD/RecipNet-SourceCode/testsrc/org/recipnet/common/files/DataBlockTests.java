/*
 * Reciprocal Net Project
 *
 * DataBlockTests.java
 * 
 * 30-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A JUnit TestCase that exercises the behavior of the {@code CifFile.SaveFrame}
 * class; most of its tests are inherited from its abstract superclass,
 * {@code DataCellTests} 
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class DataBlockTests extends DataCellTests {
    
    private final static String BLOCK_NAME = "block";
    
    private CifFile.DataBlock block;

    /**
     * Initializes a new {@code SaveFrameTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public DataBlockTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this test case to run a test; concrete subclasses should
     * delegate to this method after performing their own set up
     */
    @Override
    public void setUp() {
        block = new CifFile.DataBlock(BLOCK_NAME);
        super.setUp();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.files.DataCellTests#getTestSubject()
     */
    @Override
    public CifFile.DataBlock getTestSubject() {
        return block;
    }

    /**
     * {@inheritDoc}
     * 
     * @see DataCellTests#testConstructor_String__null()
     */
    @Override
    public void testConstructor_String__null() {
        try {
            new CifFile.DataBlock(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see DataCellTests#testConstructor_String()
     */
    @Override
    public void testConstructor_String() {
        assertEquals("Wrong block Code", BLOCK_NAME, block.getName());
    }
    
    /**
     * Tests the behavior of the {@code addSaveFrame(CifFile.SaveFrame)} method
     * when its argument is {@code null}; the expected behavior is a
     * {@code NullPointerException}
     */
    public void testMethod_addSaveFrame__null() {
        try {
            block.addSaveFrame(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code addSaveFrame} method when another frame
     * with an equivalent name is already in the {@code DataBlock} under test.
     * The expected behavior is {@code IllegalArgumentException}
     */
    public void testMethod_addSaveFrame__duplicateName() {
        CifFile.SaveFrame frame = new CifFile.SaveFrame("fRAme");
        
        block.addSaveFrame(frame);
        assertCantReadd(frame);
        
        block.addSaveFrame(new CifFile.SaveFrame("a"));
        assertCantReadd(frame);
        
        block.removeSaveFrame(frame.getName());
        block.addSaveFrame(new CifFile.SaveFrame("b"));
        block.addSaveFrame(frame);
        block.addSaveFrame(new CifFile.SaveFrame("c"));
        assertCantReadd(frame);
    }
    
    /**
     * Verifies that neither the specified save frame nor a distinct one with
     * an equivalent name can be successfully added to the DataBlock instance
     * under test 
     * 
     * @param  frame the {@code CifFile.SaveFrame} to test
     */
    void assertCantReadd(CifFile.SaveFrame frame) {
        // Test re-adding a block
        try {
            block.addSaveFrame(frame);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        
        // Test adding a different block with the same name
        try {
            frame = new CifFile.SaveFrame(frame.getName());
            block.addSaveFrame(frame);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        
        // Test adding a different block with an equivalent name
        try {
            frame = new CifFile.SaveFrame(frame.getName().toUpperCase());
            block.addSaveFrame(frame);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        try {
            frame = new CifFile.SaveFrame(frame.getName().toLowerCase());
            block.addSaveFrame(frame);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
    }

    /**
     * Tests the behavior of the {@code getSaveFrameForName(String)} method
     * when its argument is {@code null}; the expected behavior is a
     * {@code NullPointerException}
     */
    public void testMethod_getSaveFrameForName__null() {
        try {
            block.getSaveFrameForName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code removeSaveFrame(String)} method
     * when its argument is {@code null}; the expected behavior is a
     * {@code NullPointerException}
     */
    public void testMethod_removeSaveFrame__null() {
        try {
            block.removeSaveFrame(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code removeSaveFrame(String)} method when the
     * specified name does not match the name of any save frame in the
     * {@code CifFile.DataBlock} under test.  The method is expected to return
     * {@code null} 
     */
    public void testMethod_removeSaveFrame__noMatch() {
        block.addSaveFrame(new CifFile.SaveFrame("food"));
        block.addSaveFrame(new CifFile.SaveFrame("_foo"));
        block.addSaveFrame(new CifFile.SaveFrame("foo_"));
        assertNull("Returned a save frame for a nonexistent name",
                   block.removeSaveFrame("foo"));
    }

    /**
     * Tests the {@code Iterator} returned by the {@code saveFrameIterator()}
     * method; it is already tested indirectly by various other tests, but this
     * method tests specifically that the iterator supports removal and that it
     * throws the correct exception upon attempts to iterate past its end 
     */
    public void testMethod_saveFrameIterator() {
        CifFile.SaveFrame frame = new CifFile.SaveFrame("frame1");
        Iterator<CifFile.SaveFrame> frames;
        
        block.addSaveFrame(frame);
        
        // Should support element removal
        frames = block.saveFrameIterator();
        assertTrue("Iterator has no elements", frames.hasNext());
        assertEquals("Iterator returned the wrong block", frame, frames.next());
        frames.remove();
        assertNull("Save frame not removed",
                   block.getSaveFrameForName(frame.getName()));

        // should throw NoSuchElementException on attempt to iterate past the end
        block.addSaveFrame(frame);
        frames = block.saveFrameIterator();
        frames.next();
        try {
            frames.next();
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException nsee) {
            // the expected case
        }
        
        // try it again to make sure the behavior sticks
        try {
            frames.next();
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException nsee) {
            // the expected case
        }
    }
    
    /**
     * Tests the general save frame addition feature, by invoking the
     * {@code addSaveFrame(CifFile.SaveFrame} method and verifying the results
     * via the {@code getSaveFrameForName(String)} and
     * {@code saveFrameIterator()} methods
     */
    public void testFeature_saveFrameAddition() {
        List<CifFile.SaveFrame> frameList = new ArrayList<CifFile.SaveFrame>();
        CifFile.SaveFrame frame;
        CifFile.SaveFrame frame2;
        
        // Test adding a (first) frame
        frame = new CifFile.SaveFrame("frame1");
        block.addSaveFrame(frame);
        frameList.add(frame);
        frame2 = block.getSaveFrameForName("frame1");
        assertNotNull("Retrieved a null frame", frame2);
        assertEquals("Retrieved the wrong frame", frame, frame2);
        assertEquals("The block iterator returned wrong frames", frameList,
                     makeList(block.saveFrameIterator()));

        // Test adding a second frame
        frame = new CifFile.SaveFrame("frame2");
        block.addSaveFrame(frame);
        frameList.add(frame);
        frame2 = block.getSaveFrameForName("frame2");
        assertNotNull("Retrieved a null frame", frame2);
        assertEquals("Retrieved the wrong frame", frame, frame2);
        assertEquals("The block iterator returned wrong blocks",
                     new HashSet<CifFile.SaveFrame>(frameList),
                     new HashSet<CifFile.SaveFrame>(
                             makeList(block.saveFrameIterator())));
        
        // Test adding a third block
        frame = new CifFile.SaveFrame("frame3");
        block.addSaveFrame(frame);
        frameList.add(frame);
        frame2 = block.getSaveFrameForName("frame3");
        assertNotNull("Retrieved a null frame", frame2);
        assertEquals("Retrieved the wrong frame", frame, frame2);
        assertEquals("The save frame iterator returned wrong frames",
                     new HashSet<CifFile.SaveFrame>(frameList),
                     new HashSet<CifFile.SaveFrame>(
                             makeList(block.saveFrameIterator())));
    }

    /**
     * Tests the general save frame removal feature, by adding save frames to a
     * {@code CifFile.DataBlock}, then making sure that they are correctly
     * removed by {@code removeSaveFrame(String)} 
     */
    public void testFeature_saveFrameRemoval() {
        Set<CifFile.SaveFrame> frameSet = new HashSet<CifFile.SaveFrame>();
        CifFile.SaveFrame frame1 = new CifFile.SaveFrame("frAme1");
        CifFile.SaveFrame frame2 = new CifFile.SaveFrame("fraMe2");
        CifFile.SaveFrame frame3 = new CifFile.SaveFrame("Frame3");
        CifFile.SaveFrame frame4 = new CifFile.SaveFrame("fRamE4");
        
        block.addSaveFrame(frame1);
        performFrameRemovalTest(frame1, frameSet);
        
        block.addSaveFrame(frame1);
        block.addSaveFrame(frame2);
        frameSet.add(frame2);
        performFrameRemovalTest(frame1, frameSet);
        frameSet.clear();
        performFrameRemovalTest(frame2, frameSet);

        block.addSaveFrame(frame1);
        block.addSaveFrame(frame2);
        block.addSaveFrame(frame3);
        block.addSaveFrame(frame4);
        frameSet.add(frame1);
        frameSet.add(frame3);
        frameSet.add(frame4);
        performFrameRemovalTest(frame2, frameSet);
        frameSet.remove(frame4);
        performFrameRemovalTest(frame4, frameSet);
        frameSet.remove(frame3);
        performFrameRemovalTest(frame3, frameSet);
        frameSet.remove(frame1);
        performFrameRemovalTest(frame1, frameSet);
        
        assertFalse("Frame Iterator contains frames",
                    block.saveFrameIterator().hasNext());
    }
    
    private void performFrameRemovalTest(CifFile.SaveFrame testFrame,
            Set<CifFile.SaveFrame> testSet) {
        CifFile.SaveFrame frame = block.removeSaveFrame(testFrame.getName());
        Set<CifFile.SaveFrame> frameSet;
        
        assertNotNull("Null save frame returned", frame);
        assertNull("Frame still retrievable",
                   block.getSaveFrameForName(testFrame.getName()));
        frameSet = new HashSet<CifFile.SaveFrame>(
                makeList(block.saveFrameIterator()));
        assertFalse("Frame still iterated", frameSet.contains(testFrame));
        assertEquals("Other frames were disturbed", testSet, frameSet);
        
        block.addSaveFrame(testFrame);
        frame = block.removeSaveFrame(testFrame.getName().toUpperCase());
        assertNotNull("Null save frame returned", frame);
        assertNull("Frame still retrievable",
                   block.getSaveFrameForName(testFrame.getName()));
        frameSet = new HashSet<CifFile.SaveFrame>(
                makeList(block.saveFrameIterator()));
        assertFalse("Frame still iterated", frameSet.contains(testFrame));
        assertEquals("Other frames were disturbed", testSet, frameSet);
        
        block.addSaveFrame(testFrame);
        frame = block.removeSaveFrame(testFrame.getName().toLowerCase());
        assertNotNull("Null save frame returned", frame);
        assertNull("Frame still retrievable",
                   block.getSaveFrameForName(testFrame.getName()));
        frameSet = new HashSet<CifFile.SaveFrame>(
                makeList(block.saveFrameIterator()));
        assertFalse("Frame still iterated", frameSet.contains(testFrame));
        assertEquals("Other frames were disturbed", testSet, frameSet);
    }

    /**
     * A generic method that returns a {@code List} (with the appropriate type
     * parameter) of the elements from the specified {@code Iterator}.  The
     * {@code Iterator} is read to its end by this method
     * 
     * @param  <T> the type of the elements returned by the specified
     *         {@code Iterator}, and of the elements of the returned
     *         {@code List}
     * @param  it the {@code Iterator} from which to draw the list elements
     * 
     * @return a {@code List} of the elements from {@code it}
     */
    static <T> List<T> makeList(Iterator<T> it) {
        List<T> rval = new ArrayList<T>();
        
        while (it.hasNext()) {
            rval.add(it.next());
        }
        
        return rval;
    }
}
