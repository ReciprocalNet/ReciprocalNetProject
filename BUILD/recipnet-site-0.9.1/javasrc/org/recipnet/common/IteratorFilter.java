/*
 * Reciprocal Net Project
 *
 * IteratorFilter.java
 *
 * 04-May-2004: jobollin wrote first draft as part of a fix for bug #1206
 * 07-Nov-2005: jobollin made this class generic and reformatted many of the
 *              documentation comments
 * 10-May-2006: jobollin performed minor documentation tweaks
 */

package org.recipnet.common;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * An abstract {@code Iterator} implementation that wraps another iterator and
 * iterates over only those elements that satisfy the criterion defined by the
 * {@link #shouldPass(Object)} method method. It preserves the relative order of
 * the elements it returns, as defined by the internal {@code Iterator} from
 * which they are obtained.
 * </p><p>
 * Concrete subclasses generally need only provide an implementation of
 * {@code shouldPass()} appropriate for their specific purposes. Users should be
 * cautious about {@code shouldPass()} implementations that depend on mutable
 * contents of their environment, however: although every object returned by
 * this {@code Iterator} will have been accepted by {@code shouldPass()}, the
 * exact timing of that acceptance is not guaranteed, and in any particular case
 * (depending on usage) it may have been a long time prior to the actual return
 * of the object from {@code next()}.
 * </p><p>
 * This class is not thread-safe. If an instance is shared among multiple
 * threads, then accesses to it (and to its underlying {@code Iterator} and that
 * {@code Iterator}'s underlying data source) must be appropriately
 * synchronized. Also, this {@code Iterator} may not fail as quickly in the
 * event of a concurrent modification as the underlying {@code Iterator} does:
 * one extra invocation of {@code next()} or {@code hasNext()} may be required
 * before the underlying {@code Iterator}'s own behavior manifests.
 * </p>
 * 
 * @param   <T> the type of the elements returned by this iterator
 * @author  John C. Bollinger
 * @version 0.9.0
 */
public abstract class IteratorFilter<T> implements Iterator<T> {

    /**
     * The Iterator from which this one obtains its elements
     */
    private final Iterator<? extends T> internalIterator;

    /**
     * Holds the next reference to be returned by this {@code Iterator}; only
     * valid if {@code isBuffered} is {@code true}
     */
    private T bufferedObject = null;

    /**
     * {@code true} when {@code bufferedObject} holds a valid reference read
     * from the internal iterator, accepted by {@code shouldPass}, and not yet
     * given out by {@code next()}; {@code false} otherwise
     */
    private boolean isBuffered = false;

    /**
     * Constructs a new {@code IteratorFilter} that draws its elements from the
     * specified source {@code Iterator}.
     *
     * @param  source the {@code Iterator} from which this one draws its
     *         elements; may not be {@code null}
     *
     * @throws NullPointerException if {@code source} is {@code null}
     */
    public IteratorFilter(Iterator<? extends T> source) {
        if (source == null) {
            throw new NullPointerException(
                    "The source Iterator may not be null");
        }
        
        internalIterator = source;
    }
    
    /**
     * Returns {@code true} if this {@code Iterator} has any more elements to
     * disburse; <i>i.e.</i> if invocation of {@code next()} will not throw an
     * exception. Invocation of this method may cause one or more elements of
     * the underlying {@code Iterator} to be read.
     * 
     * @return {@code true} if this iterator has at least one more element;
     *         {@code false} otherwise
     */
    final public boolean hasNext() {
        while (!isBuffered && internalIterator.hasNext()) {
            bufferedObject = internalIterator.next();
            isBuffered = shouldPass(bufferedObject);
        }
        return isBuffered;
    }

    /**
     * Returns the next element of the iteration, if there is one; this element
     * will have been extracted from the underlying {@code Iterator} and
     * accepted by this {@code IteratorFilter}'s
     * {@link #shouldPass(Object) shouldPass(T)} method some time prior to its
     * being returned by this method.
     * 
     * @return the next object in the iteration
     */
    final public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        isBuffered = false;

        return bufferedObject;
    }

    /**
     * Nominally, removes the last element returned by {@code next()} from the
     * underlying data source.  This optional behavior is not supported by this
     * {@code Iterator} implementation, so this method always throws an
     * exception.
     *
     * @throws UnsupportedOperationException any time this method is invoked
     */
    final public void remove() {
        throw new UnsupportedOperationException(
                "remove() not supported by IteratorFilter");
    }

    /**
     * Defines the acceptance criterion for this class.  Any object returned by
     * {@code next()} will have previously been passed to this method with a
     * result of {@code true}.  Subclasses must implement this method to define
     * their filtering behavior.
     * 
     * @param  o a {@code T} to test for inclusion in the iteration
     * 
     * @return {@code true} if the specified object should be included in this
     *         iteration
     */
    public abstract boolean shouldPass(T o);
}

