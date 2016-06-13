/*
 * Reciprocal Net Project
 * 
 * UnionCollection.java
 * 
 * 14-02-2005: jobollin wrote first draft
 * 05-Jul-2006: jobollin performed minor cleanup
 */
 
package org.recipnet.common;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * An (externally) unmodifiable Collection view of the union of zero or more
 * {@code Collections}s. The contents of this collection will vary with the
 * contents of the constituent collections; element order is not guaranteed, and
 * duplicate elements may be present. This {@code Collection} supports
 * {@code null} elements in the constituent collections, which, per this class'
 * general specifications, result in {@code null} elements in this
 * {@code Collection}.
 * 
 * @author    John C. Bollinger
 * @version   0.9.0
 * 
 * @param <E> the element type of this {@code Collection}
 */
public class UnionCollection<E> extends AbstractCollection<E> {

    /** An array containing the {@code Collection}s in this union. */    
    private final Collection<? extends E>[] componentCols;

    /**
     * Initializes a new {@code UnionCollection}with the specified
     * {@code Collections}s as its components.
     * 
     * @param  collections zero or more {@code Collection}s having element types
     *         compatible with this {@code UnionCollection}'s, and whose
     *         elements will consitute the elements of this
     *         {@code UnionCollection} (or an array containing the same);
     *         {@code null}s are not permitted
     */    
    public UnionCollection(Collection<? extends E>... collections) {
        if (Arrays.asList(collections).contains(null)) {
            throw new NullPointerException("null collections not permitted");
        }
        componentCols = collections.clone();
    }
    
    /**
     * Implementation method of the {@code Collection} interface; determines
     * whether this {@code Collection} contains the specified object. This
     * version overrides the iterator-based version of
     * {@code AbstractCollection} with one that is no less efficient in the
     * general case, but which may be considerably more efficient for
     * constituent collections that themselves have efficient {@code contains()}
     * methods.
     * 
     * @param  o the object to be tested for membership
     * 
     * @return {@code true} if {@code o} is a member of this {@code Collection},
     *         which is the case if it is a member of any of the constituent
     *         collections
     *         
     * @see java.util.Collection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        for (Collection<?> col: componentCols) {
            if (col.contains(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Implementation method of the {@code Collection} interface; returns the
     * combined size of all the constituent {@code Collection}s
     * 
     * @return the number of elements in this {@code Collection}
     * 
     * @see java.util.Collection#size()
     */
    @Override
    public int size() {
        int totalSize = 0;
        
        for (Collection<?> col: componentCols) {
            totalSize += col.size();
        }
        
        return totalSize;
    }

    /**
     * Implementation method of the {@code Collection} interface; returns an
     * {@code Iterator} over the elements of this {@code Collection}
     * 
     * @return an {@code Iterator} over the elements of this {@code Collection}
     *        (which are all the elements of the constituent collections)
     *        
     * @see java.util.Collection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new UnionIterator<E>(componentCols);
    }
}
