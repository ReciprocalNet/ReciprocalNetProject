/*
 * Reciprocal Net Project
 *
 * UnionCollectionTests.java
 * 
 * 24-Feb-2005: jobollin wrote first draft
 */

package org.recipnet.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;


/**
 * A JUnit TestCase exercising the behavior of the UnionCollection class
 * 
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public class UnionCollectionTests extends TestCase {

    private List<Integer> intList1;
    private List<Integer> intList2;
    
    /**
     * Initializes a new UnionCollectionTests configured to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public UnionCollectionTests(String testName) {
        super(testName);
    }

    /**
     * Prepares this TestCase to run a test
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        intList1 = new ArrayList<Integer>(10);
        for (int i = 0; i < 10; i++) {
            intList1.add(i);
        }
        intList2 = new ArrayList<Integer>(10);
        for (int i = 10; i < 20; i++) {
            intList2.add(i);
        }
    }
    
    /**
     * Tests the construction and behavior of a UnionCollection with no
     * component collections.  Initialization should complete successfully, 
     * the collection should report size zero, and its iterator should have no
     * elements
     */
    public void testNoComponents() {
        Collection<?> c = new UnionCollection<Object>();
        
        assertEquals("Collection is not empty", 0, c.size());
        assertFalse("Iterator claims to have an element",
                    c.iterator().hasNext());
    }

    /**
     * Tests the construction and behavior of a UnionCollection with a
     * single, empty, component collection.  Initialization should complete
     * successfully, the collection should report size zero, and its iterator
     * should have no elements
     */
    public void testEmptyComponent() {
        Collection<?> c = new UnionCollection<Object>(new ArrayList<Object>());
        
        assertEquals("Collection is not empty", 0, c.size());
        assertFalse("Iterator claims to have an element",
                    c.iterator().hasNext());
    }

    /**
     * Tests the construction and behavior of a UnionCollection with two
     * component collections, both empty.  Initialization should complete
     * successfully, the collection should report size zero, and its iterator
     * should have no elements
     */
    public void testTwoEmptyComponents() {
        Collection<?> c = new UnionCollection<Object>(
                new ArrayList<Object>(), new HashSet<Object>());
        
        assertEquals("Collection is not empty", 0, c.size());
        assertFalse("Iterator claims to have an element",
                    c.iterator().hasNext());
    }
    
    /**
     * Tests the construction and behavior of a UnionCollection with a
     * single, nonempty, component collection.  Initialization should complete
     * successfully, the collection should report same size as the base
     * collection, and its iteration order should match that of the base
     * collection, including if the base collection's iteration order changes
     * after construction of the UnionCollection
     */
    public void testNonemptyComponent() {
        Collection<Number> c = new UnionCollection<Number>(intList1);
        
        assertEquals("Collection is the wrong size", intList1.size(), c.size());
        Iterator<? extends Number> i1, i2;
        
        for (i1 = intList1.iterator(), i2 = c.iterator(); i1.hasNext(); ) {
            assertTrue("Iteration order disagrees", i1.next() == i2.next());
        }
        
        Collections.shuffle(intList1);
        for (i1 = intList1.iterator(), i2 = c.iterator(); i1.hasNext(); ) {
            assertTrue("Iteration order disagrees after shuffling", 
                       i1.next() == i2.next());
        }
    }

    /**
     * Tests the construction and behavior of a UnionCollection with a variety
     * of empty and nonempty component collections.  Initialization should
     * complete successfully, the collection should report a size equal to the
     * sum of the sizes of the component collections, and its iteration order
     * should match the combined iteration sequence of the components, taken
     * in the order in which they are specified to the constructor
     */
    public void testMultipleComponents() {
        Set<Integer> intSet = new HashSet<Integer>();
        
        for (int i = 1; i < 30; i += 2) {
            intSet.add(i);
        }
        
        Collection<Integer> c = new UnionCollection<Integer>(intList1, intSet,
                Collections.unmodifiableCollection(intList2),
                new HashSet<Integer>(), intList2);
        
        List<Integer> unionList = new ArrayList<Integer>();
        Iterator<Integer> i1, i2;
        
        unionList.addAll(intList1);
        unionList.addAll(intSet);
        unionList.addAll(intList2);
        unionList.addAll(intList2);
        
        assertEquals("Collection is the wrong size", unionList.size(),
                     c.size());
        
        for (i1 = unionList.iterator(), i2 = c.iterator(); i1.hasNext(); ) {
            assertTrue("Iteration order disagrees", i1.next() == i2.next());
        }
    }
    
    /**
     * Tests the {@code UnionCollection} contstructor with a single {@code null}
     * argument, cast as both as a {@code Collection[]} and as a
     * {@code Collection}.  In each case the constructor should throw a
     * {@code NullPointerException} 
     */
    public void testConstructor__null() {
        try {
            new UnionCollection<Object>((Collection<? extends Object>[]) null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Wrong exception thrown; expected NullPointerException, got "
                       + e.getClass().getName(), e instanceof NullPointerException);
        }
        try {
            new UnionCollection<Object>((Collection<? extends Object>) null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Wrong exception thrown; expected NullPointerException, got "
                       + e.getClass().getName(), e instanceof NullPointerException);
        }
    }
    
    /**
     * Tests the {@code UnionCollection} contstructor with two arguments, one
     * of them {@code null}.  The constructor should throw a
     * {@code NullPointerException} whether the {@code null} is first or second
     * in the argument list.
     */
    public void testConstructor__vararg_null() {
        try {
            new UnionCollection<Object>(new ArrayList<Object>(), null);
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Wrong exception thrown; expected NullPointerException, got "
                       + e.getClass().getName(), e instanceof NullPointerException);
        }
        try {
            new UnionCollection<Object>(null, new ArrayList<Object>());
            fail("Expected a NullPointerException");
        } catch (Exception e) {
            assertTrue("Wrong exception thrown; expected NullPointerException, got "
                       + e.getClass().getName(), e instanceof NullPointerException);
        }
    }
}
