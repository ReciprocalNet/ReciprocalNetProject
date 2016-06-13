/*
 * Reciprocal Net Project
 * 
 * UnionIterator.java
 * 
 * 24-Feb-2005: jobollin extracted this class from UnionCollection to make it a
 *              top-level class 
 */

package org.recipnet.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A special-purpose {@code Iterator} that combines multiple {@code Iterator}s
 * to provide one combined iteration 
 * 
 * @author  John C. Bollinger
 * @version 0.9.0
 * 
 * @param  <E> The formal type of the elements returned by this {@code Iterator}
 */
class UnionIterator<E> implements Iterator<E> {
    
    /** An {@code Iterator} over the component {@code Iterator}s */
    private final Iterator<Iterator<? extends E>> componentIterator;
    
    /** The {@code Iterator} currently supplying objects for this iteration */
    private Iterator<? extends E> currentIterator;
    
    /**
     * A flag indicating whether the next object in the iteration has already
     * been determined (in which case it is stored in {@code nextObject})
     */
    private boolean nextIsCached = false;

    /**
     * if {@code nextIsCached} is {@code true}, the next element of this
     * iteration; otherwise undefined
     */
    private E nextObject;

    /**
     * Initializes a new {@code UnionIterator} that will draw its elements from
     * the specified iterators, in the order that they are specified
     * 
     * @param  iterators zero or more {@code Iterator}s, or an array of such,
     *         from which the elements of this {@code Iterator} will be drawn
     */
    public UnionIterator(Iterator<? extends E>... iterators) {
        componentIterator = Arrays.asList(iterators.clone()).iterator();
        currentIterator = nextIterator();
    }
    
    /**
     * Initializes a new {@code UnionIterator} that will draw its elements from
     * the specified {@code Collection}s, in the order that they are specified,
     * and within each one, in the order defined by its {@code Iterator}
     * 
     * @param  collections zero or more {@code Collection}s, or an array of
     *         such, from which the elements of this {@code Iterator} will be
     *         drawn
     */
    public UnionIterator(Collection<? extends E>... collections) {
        List<Iterator<? extends E>> iteratorList =
                new ArrayList<Iterator<? extends E>>(collections.length);
        
        for (Collection<? extends E> col : collections) {
            iteratorList.add(col.iterator());
        }
        
        componentIterator = iteratorList.iterator();
        currentIterator = nextIterator();
    }
    
    /**
     * Returns the next iterator from which to draw elements for the
     * iteration, or {@code null} if there are no more
     * 
     * @return the next iterator from which to draw elements for the
     *         iteration
     */
    private Iterator<? extends E> nextIterator() {
        return (componentIterator.hasNext() ? componentIterator.next() : null);
    }
    
    /**
     * Implementation method of the {@code Iterator} interface; indicates
     * whether there are any more elements in this iteration
     * 
     * @return {@code true} if there is at least one more element in this
     *         iteration, otherwise {@code false}
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (!nextIsCached) {
            while ((currentIterator != null) && !currentIterator.hasNext()) {
                currentIterator = nextIterator();
            }
            if (currentIterator != null) {
                nextObject = currentIterator.next();
                nextIsCached = true;
            }
        }
        
        return nextIsCached;
    }

    /**
     * Implementation method of the {@code Iterator} interface; returns the next
     * element of this iteration
     * 
     * @return the next element of this iteration, if there is any
     * 
     * @throws NoSuchElementException if there are no more elements in this
     *         iteration
     *         
     * @see java.util.Iterator#next()
     */            
    public E next() {
        if (hasNext()) {
            nextIsCached = false;
            return nextObject;
        } else {
            throw new NoSuchElementException("No more elements");
        }
    }
    
    /**
     * Implementation method of the {@code Iterator} interface; always throws
     * {@code UnsupportedOperationException} because this iterator does not
     * support element removal
     * 
     * @throws UnsupportedOperationException whenever invoked
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove() {
            throw new UnsupportedOperationException("Removal not supported");
    }
   
}