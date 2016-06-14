/*
 * Reciprocal Net Project
 *
 * ConcurrentCollectionTests.java
 * 
 * 05-Apr-2005: jobollin wrote first draft
 */

package org.recipnet.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * Exercises the behavior of the {@code ConcurrentCollectionTests} class to
 * verify that it operates as expected
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class ConcurrentCollectionTests extends TestCase {
    
    /**
     * Initializes a new {@code ConcurrentCollectionTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public ConcurrentCollectionTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the nullary constructor; a new
     * {@code ConcurrentCollection} thereby initialized should be empty, and
     * this is verified by multiple checks
     */
    public void testConstructor() {
        ConcurrentCollection<Object> col = new ConcurrentCollection<Object>();
        
        assertTrue("New Collection is not empty", col.isEmpty());
        assertEquals("New Collection has the wrong size", 0, col.size());
        assertFalse("New Collection's iterator has elements",
                    col.iterator().hasNext());
    }
    
    /**
     * Tests the behavior of the {@code Collection} constructor; a new
     * {@code ConcurrentCollection} thereby initialized should have the same
     * size and elements as the constructor argument
     */
    public void testConstructor_Collection() {
        Collection<String> original = new ArrayList<String>();
        Collection<String> copy;
        
        Collections.addAll(original, "Foo", null, "");
        copy = new ConcurrentCollection<String>(original);
        
        assertEquals("New Collection has the wrong size", original.size(),
                     copy.size());
        assertTrue("New Collection is missing some elements",
                   copy.containsAll(original));
        assertTrue("New Collection has extra elements",
                   original.containsAll(copy));
    }
    
    /**
     * Tests the behavior of the {@code add(Object)} method; upon object
     * addition, the size of the collection should increase by one, and the
     * collection should report that it contains the added object.  It should
     * not lose any previously added object
     */
    public void testMethod_add() {
        Collection<Object> col = new ConcurrentCollection<Object>();
        Collection<Object> ref = new ArrayList<Object>();
        
        for (int i = 0; i < 5; i++) {
            Object o = new Object();
            
            assertTrue("Collection refused to add an object", col.add(o));
            assertTrue("Collection does not contain added object",
                   col.contains(o));
            assertFalse("Collection thinks it is empty", col.isEmpty());
            assertEquals("Collection reports the wrong size", i + 1, col.size());
            
            ref.add(o);
            assertTrue("Collection lost an element", col.containsAll(ref));
            assertTrue("Collection has an extra element", ref.containsAll(col));
        }
    }
    
    /**
     * Tests the behavior of the {@code remove(Object)} method; upon element
     * removal, the size of the collection should decrease by one, and the
     * collection should report (correctly) that it removed the object.
     * Non-elements should not be successfully removed.
     */
    public void testMethod_remove() {
        Collection<Object> col = new ConcurrentCollection<Object>();
        Collection<Object> ref = new HashSet<Object>(); // pseudo-random order
        
        for (int i = 0; i < 5; i++) {
            Object o = new Object();
            
            col.add(o);
            ref.add(o);
        }
        
        col.add(null);
        ref.add(null);
        
        assertEquals("Collection has wrong initial size", ref.size(),
                     col.size());
        assertTrue("Collection doesn't contain the expected elements",
                   col.containsAll(ref));
        assertTrue("Collection contains unexpected elements",
                   ref.containsAll(col));
        for (Object o : ref) {
            assertTrue("Collection does not contain an expected element",
                       col.contains(o));
            assertTrue("Collection failed to remove an element", col.remove(o));
            assertFalse("Collection still contains a removed element",
                        col.contains(o));
            assertFalse("Collection removed a non-member",
                        col.remove(new Object()));
        }
        assertEquals("Collection has wrong size", 0, col.size());
        assertTrue("Collection is not empty", col.isEmpty());
        assertFalse("Collection's iterator has an element",
                    col.iterator().hasNext());
    }
    
    /**
     * Tests the concurrency behavior of the ConcurrentCollection class;
     * specifically, verifies that the collection's iterators are insensitive to
     * elements added to the collection after they are created
     */
    public void testFeature_concurrency__addition() {
        ConcurrentCollection<Object> col = new ConcurrentCollection<Object>();
        List<Pair<Object>> pairs = new ArrayList<Pair<Object>>();
        List<Object> elements = new ArrayList<Object>();
        Iterator<Object> it;
        Object o;

        it = col.iterator();
        // 0
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(elements)));
        
        o = new Object();
        col.add(o);
        elements.add(0, o);
        it = col.iterator();
        // 1
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(elements)));
        it = col.iterator();
        it.next();
        // 2
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(1, elements.size()))));
        
        o = new Object();
        col.add(o);
        elements.add(0, o);
        // 3
        pairs.add(new Pair<Object>(col.iterator(), new ArrayList<Object>(elements)));
        it = col.iterator();
        it.next();
        // 4
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(1, elements.size()))));
        it = col.iterator();
        it.next();
        it.next();
        // 5
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(2, elements.size()))));
        
        o = new Object();
        col.add(o);
        elements.add(0, o);
        // 6
        pairs.add(new Pair<Object>(col.iterator(), new ArrayList<Object>(elements)));
        it = col.iterator();
        it.next();
        // 7
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(1, elements.size()))));
        it = col.iterator();
        it.next();
        it.next();
        // 8
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(2, elements.size()))));
        it = col.iterator();
        it.next();
        it.next();
        it.next();
        // 9
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(3, elements.size()))));
        
        for (Pair<Object> pair : pairs) {
            pair.verify();
        }
    }
    
    /**
     * Tests the concurrency behavior of the ConcurrentCollection class;
     * specifically, verifies that the collection's iterators respond correctly
     * to element deletion -- they should be insensitive to elements deleted
     * from among those they have already iterated, and they should never return
     * an element that has been deleted from the underlying collection.
     * {@code Iterator}s' {@code hasNext()} methods should provide correct
     * responses, including when the last element in their iteration orders is
     * deleted before they have iterated it
     */
    public void testFeature_concurrency__removal() {
        ConcurrentCollection<Object> col = new ConcurrentCollection<Object>();
        List<Pair<Object>> pairs = new ArrayList<Pair<Object>>();
        List<Object> elements = new ArrayList<Object>();
        Iterator<Object> it;
        Object o = new Object();

        for (int i = 0; i < 5; i++) {
            o = new Object();
            col.add(o);
            elements.add(0, o);
        }
        
        it = col.iterator();
        while (it.hasNext()) {
            it.next();
        }
        // 0
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(elements.size(), elements.size()))));
        
        o = elements.get(4);
        elements.remove(o);
        it = col.iterator();
        for (int i = 0; i < elements.size(); i++) {
            it.next();
        }
        // 1
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(elements.size(), elements.size()))));
        col.remove(o);

        o = elements.get(1);
        elements.remove(o);
        it = col.iterator();
        // 2
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(elements)));
        it = col.iterator();
        it.next();
        // 3
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(1, elements.size()))));
        it = col.iterator();
        it.next();
        it.next();
        // 4
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(
                elements.subList(1, elements.size()))));
        col.remove(o);
        
        col = new ConcurrentCollection<Object>(elements);
        Collections.reverse(elements);
        o = elements.get(0);
        elements.remove(o);
        it = col.iterator();
        // 5
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(elements)));
        it = col.iterator();
        it.next();
        // 6
        pairs.add(new Pair<Object>(it, new ArrayList<Object>(elements)));
        col.remove(o);
        
        for (Pair<Object> pair : pairs) {
            pair.verify();
        }
    }
    
    /**
     * Tests the concurrency behavior of the ConcurrentCollection class;
     * specifically, verifies that the collection's iterators correctly remove
     * elements in the event of other element removals.  Tests that
     * iterators throw exceptions when asked to remove an element twice, or
     * asked to remove an element before they have iterated one, and that they
     * do not throw exceptions or modify the collection if asked to remove an
     * element that has already been removed by some other means.
     */
    public void testFeature_concurrency__iterator_removal() {
        ConcurrentCollection<Object> col = new ConcurrentCollection<Object>();
        Iterator<Object> it;
        Iterator<Object> it2;
        
        it = col.iterator();
        try {
            it.remove();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        col.add(new Object());
        try {
            it.remove();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        assertEquals("Object inappropriately removed", 1, col.size());
        
        it = col.iterator();
        it.next();
        it2 = col.iterator();
        it2.next();
        it.remove();
        it2.remove();  // should not throw an exception
        assertEquals("Object not removed", 0, col.size());

        col.add(new Object());
        col.add(new Object());
        col.add(new Object());
        col.add(new Object());
        it = col.iterator();
        it.next();
        it.next();
        it.remove();
        try {
            it.remove();
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException ise) {
            // The expected case
        }
        
        it = col.iterator();
        it.next();
        it.next();
        it2 = col.iterator();
        it2.next();
        it2.next();
        it.remove();
        it2.remove();  // should not throw an exception
        assertEquals("Extra object removed", 2, col.size());
    }
    
    /**
     * A class representing an {@code Iterator} and {@code Collection} pair,
     * such that the latter contains exactly those elements expected to be
     * provided by the former
     * 
     * @author  jobollin
     * @version 0.9.0
     * 
     * @param <E> the type of the elements of the collection and iteration
     */
    private static class Pair<E> {
        private final Iterator<? extends E> iterator;
        private final Collection<? extends E> collection;
        
        /**
         * Initializes a new {@code Pair} containing the specified
         * {@code Iterator} and {@code Collection}
         * 
         * @param  it the {@code Iterator} of this pair
         * @param  col the {@code Collection} of this pair
         */
        Pair(Iterator<? extends E> it, Collection<? extends E> col) {
            iterator = it;
            collection = col;
        }
        
        /**
         * Verifies via assertion that the {@code Iterator} returns exactly
         * those elements in the {@code Collection}
         */
        void verify() {
            List<E> list = new ArrayList<E>();
            
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            
            assertEquals("Iterator returned the wrong number of elements",
                       collection.size(), list.size());
            assertTrue("Iterator returned incorrect elements",
                       collection.containsAll(list));
            assertTrue("Iterator missed elements",
                       list.containsAll(collection));
        }
    }
}
