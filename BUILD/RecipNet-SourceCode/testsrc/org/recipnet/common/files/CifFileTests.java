/*
 * Reciprocal Net Project
 *
 * CifFileTests.java
 * 
 * 29-Mar-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

/**
 * A JUnit TestCase that exercises the behavior of the CifFile class
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class CifFileTests extends TestCase {
    
    private CifFile cifFile;
    
    private List<String> validDataNames = new ArrayList<String>();
    private List<String> invalidDataNames = new ArrayList<String>();

    /**
     * Initializes a new CifFileTests for performing the named test.
     * 
     * @param  testName the name of the test to run
     */
    public CifFileTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this CifFileTests to run a test
     */
    @Override
    public void setUp() {
        cifFile = new CifFile();
        Collections.addAll(validDataNames, "__", "_data", "_loop", "_global",
                           "_stop", "_save",
                           "_~`!@#$%^&*()_-+=[]{}\\|:;\"'<,>.?/",
                           "_AbcYse985*()_-+_frob''|:;\"'<,>.?/");
        Collections.addAll(invalidDataNames, "", "foo", "_", "_ ", "_foo ",
                           "_ foo", "_ foo ", "_ ", "_foo_bar_baz_bat.ELEPHANT ",
                           "_\tfoo\t", "_\t", "_foo_bar_baz_bat.ELEPHANT\t");
        for (int i = 0; i < 0x20; i++) {
            invalidDataNames.add("_" + (char) i);
            invalidDataNames.add("_foo" + (char) i);
            invalidDataNames.add("_" + ((char) i) + "foo");
        }
    }
    
    /**
     * Tests the {@code CifFile()} constructor to verify that it does not throw
     * any exception, and that the newly-constructed instance does not contain
     * any data blocks. 
     */
    public void testConstructor() {
        CifFile cifFile = new CifFile();
        
        assertTrue("Block names present",
                   cifFile.getDataBlockNames().isEmpty());
        assertFalse("Block Iterator has elements",
                    cifFile.blockIterator().hasNext());
    }
    
    /**
     * Tests the behavior of the {@code isValidDataName(String)} method when its
     * argument is {@code null}.  The expected result is a
     * {@code NullPointerException}
     */
    public void testMethod_isValidDataName__null() {
        try {
            CifFile.isValidDataName(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }

    /**
     * Tests the behavior of the {@code isValidDataName(String)} method on a
     * variety of valid data names
     */
    public void testMethod_isValidDataName__valid() {
        for (String name : validDataNames) {
            assertValidDataName(name, true);
        }
    }

    /**
     * Tests the behavior of the {@code isValidDataName(String)} method on a
     * variety of invalid data names
     */
    public void testMethod_isValidDataName__invalid() {
        for (String name : invalidDataNames) {
            assertValidDataName(name, false);
        }
    }

    /**
     * Verifies that the specified data name is (in)valid as judged by the
     * {@code CifFile.isValidDataName(String)} method
     * 
     * @param  name the name to test
     * @param  sense {@code true} to test that the name is valid, {@code false}
     *         to test that it is invalid
     */
    void assertValidDataName(String name, boolean sense) {
        assertTrue("'" + name + "' is " + (sense ? "not " : "") + "validated",
                   (CifFile.isValidDataName(name) == sense));
    }
    
    /**
     * Tests the behavior of the {@code addDataBlock(CifFile.DataBlock)} method
     * when its argument is {@code null}.  The expected result is a
     * {@code NullPointerException}
     */
    public void testMethod_addDataBlock__null() {
        try {
            cifFile.addDataBlock(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code addDataBlock(DataBlock)} method when
     * the argument has the same name as a data block already in the CifFile
     */
    public void testMethod_addDataBlock__duplicate() {
        CifFile.DataBlock block = new CifFile.DataBlock("BloCk");
        
        cifFile.addDataBlock(block);
        assertCantReadd(block);
        
        cifFile.addDataBlock(new CifFile.DataBlock("a"));
        assertCantReadd(block);
        
        cifFile.removeDataBlock(block.getName());
        cifFile.addDataBlock(new CifFile.DataBlock("b"));
        cifFile.addDataBlock(block);
        cifFile.addDataBlock(new CifFile.DataBlock("c"));
        assertCantReadd(block);
    }
    
    /**
     * Verifies that neither the specified data block nor a distinct one with
     * an equivalent name can be successfully added to the CifFile instance
     * under test 
     * 
     * @param  block the {@code CifFile.DataBlock} to test
     */
    void assertCantReadd(CifFile.DataBlock block) {
        // Test re-adding a block
        try {
            cifFile.addDataBlock(block);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        
        // Test adding a different block with the same name
        try {
            block = new CifFile.DataBlock(block.getName());
            cifFile.addDataBlock(block);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        
        // Test adding a different block with an equivalent name
        try {
            block = new CifFile.DataBlock(block.getName().toUpperCase());
            cifFile.addDataBlock(block);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
        try {
            block = new CifFile.DataBlock(block.getName().toLowerCase());
            cifFile.addDataBlock(block);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // the expected case;
        }
    }
    
    /**
     * Tests the behavior of the {@code getDataBlock(String)} method
     * when its argument is {@code null}.  The expected result is a
     * {@code NullPointerException}
     */
    public void testMethod_getDataBlock__null() {
        try {
            cifFile.getDataBlock(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code getDataBlock(String)} method when the
     * specified name does not match the name of any data block in the
     * {@code CifFile} under test.  The method is expected to return
     * {@code null} 
     */
    public void testMethod_getDataBlock__noMatch() {
        cifFile.addDataBlock(new CifFile.DataBlock("food"));
        cifFile.addDataBlock(new CifFile.DataBlock("_foo"));
        cifFile.addDataBlock(new CifFile.DataBlock("foo_"));
        assertNull("Returned a datablock for a nonexistent name",
                   cifFile.getDataBlock("foo"));
    }
    
    /**
     * Tests the behavior of the {@code removeDataBlock(String)} method
     * when its argument is {@code null}.  The expected result is a
     * {@code NullPointerException}
     */
    public void testMethod_removeDataBlock__null() {
        try {
            cifFile.removeDataBlock(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // the expected case
        }
    }
    
    /**
     * Tests the behavior of the {@code removeDataBlock(String)} method when the
     * specified name does not match the name of any data block in the
     * {@code CifFile} under test.  The method is expected to return
     * {@code null} 
     */
    public void testMethod_removeDataBlock__noMatch() {
        cifFile.addDataBlock(new CifFile.DataBlock("food"));
        cifFile.addDataBlock(new CifFile.DataBlock("_foo"));
        cifFile.addDataBlock(new CifFile.DataBlock("foo_"));
        assertNull("Returned a datablock for a nonexistent name",
                   cifFile.removeDataBlock("foo"));
    }
    
    /**
     * Tests the general data block addition feature, by invoking the
     * {@code addDataBlock(CifFile.DataBlock} method and verifying the results
     * via the {@code getDataBlock(String)} and {@code blockIterator()} methods
     */
    public void testFeature_dataBlockAddition() {
        List<CifFile.DataBlock> blockList = new ArrayList<CifFile.DataBlock>();
        CifFile.DataBlock block;
        CifFile.DataBlock block2;
        
        // Test adding a (first) block
        block = new CifFile.DataBlock("block1");
        cifFile.addDataBlock(block);
        blockList.add(block);
        block2 = cifFile.getDataBlock("block1");
        assertNotNull("Retrieved a null block", block2);
        assertEquals("Retrieved the wrong block", block, block2);
        assertEquals("The block iterator returned wrong blocks", blockList,
                     makeList(cifFile.blockIterator()));

        // Test adding a second block
        block = new CifFile.DataBlock("block2");
        cifFile.addDataBlock(block);
        blockList.add(block);
        block2 = cifFile.getDataBlock("block2");
        assertNotNull("Retrieved a null block", block2);
        assertEquals("Retrieved the wrong block", block, block2);
        assertEquals("The block iterator returned wrong blocks",
                     new HashSet<CifFile.DataBlock>(blockList),
                     new HashSet<CifFile.DataBlock>(
                             makeList(cifFile.blockIterator())));
        
        // Test adding a third block
        block = new CifFile.DataBlock("block3");
        cifFile.addDataBlock(block);
        blockList.add(block);
        block2 = cifFile.getDataBlock("block3");
        assertNotNull("Retrieved a null block", block2);
        assertEquals("Retrieved the wrong block", block, block2);
        assertEquals("The block iterator returned wrong blocks",
                     new HashSet<CifFile.DataBlock>(blockList),
                     new HashSet<CifFile.DataBlock>(
                             makeList(cifFile.blockIterator())));
    }

    /**
     * Tests the general data block removal feature, by adding data blocks to a
     * {@code CifFile}, then making sure that they are correctly removed by
     * {@code removeDataBlock()} 
     */
    public void testFeature_dataBlockRemoval() {
        Set<CifFile.DataBlock> blockSet = new HashSet<CifFile.DataBlock>();
        CifFile.DataBlock block1 = new CifFile.DataBlock("blOck1");
        CifFile.DataBlock block2 = new CifFile.DataBlock("bloCk2");
        CifFile.DataBlock block3 = new CifFile.DataBlock("Block3");
        CifFile.DataBlock block4 = new CifFile.DataBlock("bLocK4");
        
        cifFile.addDataBlock(block1);
        performBlockRemovalTest(block1, blockSet);
        
        cifFile.addDataBlock(block1);
        cifFile.addDataBlock(block2);
        blockSet.add(block2);
        performBlockRemovalTest(block1, blockSet);
        blockSet.clear();
        performBlockRemovalTest(block2, blockSet);

        cifFile.addDataBlock(block1);
        cifFile.addDataBlock(block2);
        cifFile.addDataBlock(block3);
        cifFile.addDataBlock(block4);
        blockSet.add(block1);
        blockSet.add(block3);
        blockSet.add(block4);
        performBlockRemovalTest(block2, blockSet);
        blockSet.remove(block4);
        performBlockRemovalTest(block4, blockSet);
        blockSet.remove(block3);
        performBlockRemovalTest(block3, blockSet);
        blockSet.remove(block1);
        performBlockRemovalTest(block1, blockSet);
        
        assertFalse("Block Iterator contains blocks",
                    cifFile.blockIterator().hasNext());
    }
    
    private void performBlockRemovalTest(CifFile.DataBlock testBlock,
            Set<CifFile.DataBlock> testSet) {
        CifFile.DataBlock block = cifFile.removeDataBlock(testBlock.getName());
        Set<CifFile.DataBlock> blockSet;
        
        assertNotNull("Null data block returned", block);
        assertNull("Block still retrievable",
                   cifFile.getDataBlock(testBlock.getName()));
        blockSet = new HashSet<CifFile.DataBlock>(
                makeList(cifFile.blockIterator()));
        assertFalse("Block still iterated", blockSet.contains(testBlock));
        assertEquals("Other blocks were disturbed", testSet, blockSet);
        
        cifFile.addDataBlock(testBlock);
        block = cifFile.removeDataBlock(testBlock.getName().toUpperCase());
        assertNotNull("Null data block returned", block);
        assertNull("Block still retrievable",
                   cifFile.getDataBlock(testBlock.getName()));
        blockSet = new HashSet<CifFile.DataBlock>(
                makeList(cifFile.blockIterator()));
        assertFalse("Block still iterated", blockSet.contains(testBlock));
        assertEquals("Other blocks were disturbed", testSet, blockSet);
        
        cifFile.addDataBlock(testBlock);
        block = cifFile.removeDataBlock(testBlock.getName().toLowerCase());
        assertNotNull("Null data block returned", block);
        assertNull("Block still retrievable",
                   cifFile.getDataBlock(testBlock.getName()));
        blockSet = new HashSet<CifFile.DataBlock>(
                makeList(cifFile.blockIterator()));
        assertFalse("Block still iterated", blockSet.contains(testBlock));
        assertEquals("Other blocks were disturbed", testSet, blockSet);
    }

    /**
     * Tests the {@code Iterator} returned by the {@code blockIterator} method;
     * it is already tested indirectly by various other tests, but this method
     * tests specifically that the iterator does not support removal and that
     * it throws the correct exception upon attempts to iterate past its end 
     */
    public void testMethod_blockIterator() {
        CifFile.DataBlock block = new CifFile.DataBlock("block1");
        Iterator<CifFile.DataBlock> blocks;
        
        cifFile.addDataBlock(block);
        
        // Should not support element removal
        blocks = cifFile.blockIterator();
        assertTrue("Iterator has no elements", blocks.hasNext());
        assertEquals("Iterator returned the wrong block", block, blocks.next());
        try {
            blocks.remove();
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException uoe) {
            // the expected case
        }

        // should throw NoSuchElementException on attempt to iterate past the end
        blocks = cifFile.blockIterator();
        blocks.next();
        try {
            blocks.next();
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException nsee) {
            // the expected case
        }
        
        // try it again to make sure the behavior sticks
        try {
            blocks.next();
            fail("Expected a NoSuchElementException");
        } catch (NoSuchElementException nsee) {
            // the expected case
        }
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
