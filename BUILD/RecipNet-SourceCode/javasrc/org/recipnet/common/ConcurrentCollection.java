/*
 * Reciprocal Net Project
 *
 * ConcurrentList.java
 * 
 * 04-Apr-2005: jobollin wrote first draft
 * 15-Jun-2006: jobollin updated docs
 */

package org.recipnet.common;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>
 * A modifiable {@code Collection} implementation whose iterators do not throw
 * exceptions in the event of collection modifications during the course of
 * iteration.  This behavior is in contrast to the "fail-fast" behavior
 * guaranteed by the platform library's standard collections.
 * </p><p>
 * This collection has {@code List}-like semantics in that it supports duplicate
 * elements and maintains a consistent internal order of its elements.  It
 * differs, however, in that it supports only unidirectional iteration, does not
 * provide indexed access to elements, and adds new elements to the beginning of
 * the iteration order (rather than to the end).
 * </p><p>
 * <strong>Iteration behavior:</strong> this class' iterator's {@code next()}
 * methods never return elements added to the collection after the iterator was
 * created, nor do those methods ever return elements not in the collection at
 * the time they are invoked (even if an element is removed while an iterator
 * over the collection is live).  It is possible for an iterator's
 * {@code hasNext()} method to return {@code true} and then subsequently
 * return {@code false} without that iterator's {@code next()} method being
 * invoked, if all remaining elements in the iteration are removed from the
 * collection between the two invocations.  Once an iterator's {@code hasNext()}
 * method returns {@code false}, however, it will never thereafter return
 * {@code true}.   
 * </p><p>
 * <strong>This class is not inherently thread-safe.</strong>  It's name refers
 * to the guarantees provided when it is used within a single thread; access to
 * an instance shared among multiple threads must be properly synchronized for
 * behavior to be deterministic.
 * </p>
 * 
 * @param  <E> the type of elements of this collection
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class ConcurrentCollection<E> extends AbstractCollection<E> {

    /*
     * This Collection is based on a linked list with explicit connection
     * objects linking nodes (as opposed to of a more typical linked list with
     * nodes having direct references one to another).  The connections provide
     * delegation behavior so that when a particular Connection no longer
     * represents the current structure of the collection, it can be assigned a
     * delegate that does.  The collection's iterator keeps its position by
     * tracking its current connection object instead of an index or its
     * previous and/or next node.
     */
    
    /**
     * An internal size counter to avoid having to iterate to find the size
     */
    int size = 0;
    
    /**
     * The logical first node of the internal linked list in which the values
     * are stored.  This node serves as a fence-post and a handle on the
     * internal list; it does not hold any value
     */
    final Node headNode;
    
    /**
     * The logical last node of the internal linked list in which the values
     * are stored.  This node serves as a fence-post; it does not hold any value
     */
    final Node tailNode;
    
    /**
     * Initializes a new, empty {@code ConcurrentCollection}
     */
    public ConcurrentCollection() {
        Connection con;
        
        headNode = new Node();
        tailNode = new Node();
        con = new Connection(headNode, tailNode);
        headNode.setNextConnection(con);
        tailNode.setPreviousConnection(con);
    }

    /**
     * Initializes a new {@code ConcurrentCollection} initially containing the
     * elements of the provided collection
     * 
     * @param  c a {@code Collection} containing the initial elements for this
     *         collection
     */
    public ConcurrentCollection(Collection<? extends E> c) {
        this();
        addAll(c);
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @Override
    public boolean add(E value) {
        Node newNode = new Node(value);
        
        // insert the new node at the beginning
        Connection firstConnection = headNode.getNextConnection();
        Node nextNode = firstConnection.getNodeAfter();
        
        // The connections to and from the new node
        Connection previousCon = new Connection(headNode, newNode);
        Connection nextCon = new Connection(newNode, nextNode);
        
        newNode.setPreviousConnection(previousCon);
        newNode.setNextConnection(nextCon);
        
        /*
         * The new node was inserted between the nodes connected by
         * the "firstConnection" Connection, so this Connection must delegate
         * to one of the two new Collections; the choice will affect existing
         * iterators currently at the beginning of their iteration.  The chosen
         * behavior is that such iterators will not see the new element, so that
         * all iterators are consistent in this regard. 
         */ 
        firstConnection.setDelegate(nextCon);
        
        // Assign the new connections to the existing nodes
        headNode.setNextConnection(previousCon);
        nextNode.setPreviousConnection(nextCon);
        
        // Increment the size counter
        size++;
        
        return true;
    }
    
    /**
     * {@inheritDoc}.  The returned iterator will never throw a
     * {@code ConcurrentModificationException}; it will not return elements
     * added to the underlying collection after the iterator was created, and it
     * will never return an element that is not a member of this collection
     * 
     * @see java.util.Collection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return new NodeIterator();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return size;
    }
    
    /**
     * A class representing a node in ConcurrentCollection's internal linked
     * list.  It can be assigned next and / or previous Connection objects, and
     * provides a flag by which it can be marked as canDelete
     * 
     * @author  jobollin
     * @version 0.9.0
     */
    private class Node {
        
        /** the value object assigned to this {@code Node} */
        private final E value;
        
        /** The Connection to the next Node in the list */
        private Connection toNext;
        
        /** The Connection to the previous Node in the list */
        private Connection toPrevious;
        
        /** the deletion flag */
        private boolean deleted;
        
        /**
         * Initializes a new {@code Node} with a {@code null} value
         */
        Node() {
            this(null);
        }

        /**
         * Initializes a new {@code Node} with the specified value
         * 
         * @param  val the value to be stored in this {@code Node}; may be
         *         {@code null}, in which case this constructor is equivalent to
         *         the nullary one
         */
        Node(E val) {
            value = val;
            toNext = null;
            toPrevious = null;
            deleted = false;
        }
        
        /**
         * Returns the value object assigned to this {@code Node}
         * 
         * @return the value assigned to this {@code Node}
         */
        E getValue() {
            return value;
        }
        
        /**
         * Returns the {@code Connection} (if any) to the next {@code Node} in
         * the list 
         * 
         * @return the {@code Connection} (if any) to the next {@code Node} in
         *         the list; may be {@code null} if there is no connection
         *         because either this node is not in a list or it is the last
         *         node in its list
         */
        public Connection getNextConnection() {
            return toNext;
        }

        /**
         * Sets the Connection to the next node in this node's list
         * 
         * @param  toNext the {@code Connection} to set; may be {@code null}
         */
        public void setNextConnection(Connection toNext) {
            this.toNext = toNext;
        }

        /**
         * Returns the {@code Connection} (if any) to the previous {@code Node}
         * in* the list 
         * 
         * @return the {@code Connection} (if any) to the previous {@code Node}
         *         in the list; may be {@code null} if there is no connection
         *         because either this node is not in a list or it is the first
         *         node in its list
         */
        public Connection getPreviousConnection() {
            return toPrevious;
        }
        
        /**
         * Sets the Connection to the previous node in this node's list
         * 
         * @param  toPrevious the {@code Connection} to set; may be {@code null}
         */
        public void setPreviousConnection(Connection toPrevious) {
            this.toPrevious = toPrevious;
        }
        
        /**
         * Returns the value of this node's deletion flag
         * 
         * @return {@code true} if this node has been marked canDelete; otherwise
         *         {@code false}
         */
        public boolean isDeleted() {
            return deleted;
        }
        
        /**
         * Marks this node as having been canDelete.  This is an irrevocable
         * action
         */
        public void markDeleted() {
            deleted = true;
        }
    }

    /**
     * A class representing a connection (i.e. a link) between two nodes in a
     * linked list.  Embodying the links in an object permits changing their
     * characteristics without changing the nodes themselves.  This class goes
     * one step farther, however: instead of providing for the previous and next
     * nodes to be modified directly, this class provides a mechanism for an
     * instance to delegate to a different instance; clients thus can access
     * both the original and the effective current connection state. 
     * 
     * @author  jobollin
     * @version 0.9.0
     */
    private class Connection {
        
        /** The (original) node directly before this connection */
        private final Node before;
        
        /** The (original) node directly after this connection */
        private final Node after;
        
        /** This connection's delegate, if any */
        private Connection delegate;
        
        /**
         * Initializes a new {@code Connection} between the specified
         * {@code Node}s
         * 
         * @param  nodeBefore the {@code Node} before this connection;
         *         may not be {@code null}
         * @param  nodeAfter the {@code Node} after this connection;
         *         may not be {@code null}
         */
        Connection(Node nodeBefore, Node nodeAfter) {
            if ((nodeBefore == null) || (nodeAfter == null)) {
                throw new NullPointerException(
                        "connection ends may not be null");
            }
            before = nodeBefore;
            after = nodeAfter;
            delegate = null;
        }
        
        /**
         * Returns the ultimate delegate in the delegate chain starting with
         * this {@code Connection}.  The ultimate delegate is this connection
         * if this connection has no delegate; otherwise it is this connection's
         * delegate's ultimate delegate
         * 
         * @return the {@code Connection} that is the ultimate delegate of this
         *         connection
         */
        Connection getDelegate() {
            return ((delegate == null) ? this : delegate.getDelegate());
        }
        
        /**
         * Sets this {@code Connection}'s direct delegate (the next
         * {@code Connection} in the delegate chain).  Only valid when this
         * Connection has no direct delegate
         * 
         * @param  del the {@code Connection} to serve as this one's direct
         *         delegate; may not be this {@code Connection} itself
         */
        void setDelegate(Connection del) {
            if (del == this) {
                throw new IllegalArgumentException(
                        "A connection cannot delegate to itself");
            } else if (delegate != null) {
                throw new IllegalStateException("delegate already set");
            } else {
                delegate = del;
            }
        }

        /**
         * Returns the {@code Node} directly after this connection relative to
         * the <em>current</em> state of the list; this will be the same as the
         * result of {@code getOriginalNodeAfter()} if this connection has no
         * delegate, but otherwise it may be different
         * 
         * @return the {@code Node} directly after this connection
         */
        Node getNodeAfter() {
            return getDelegate().getOriginalNodeAfter();
        }

        /**
         * Returns the {@code Node} directly before this connection relative to
         * the <em>current</em> state of the list; this will be the same as the
         * result of {@code getOriginalNodeBefore()} if this connection has no
         * delegate, but otherwise it may be different
         * 
         * @return the {@code Node} directly before this connection
         */
        Node getNodeBefore() {
            return getDelegate().getOriginalNodeBefore();
        }
        
        /**
         * Returns the {@code Node} directly after this connection, as set when
         * this {@code Connection} was initialized; this value never changes
         * 
         * @return the original {@code Node} directly after this connection
         */
        Node getOriginalNodeAfter() {
            return after;
        }
        
        /**
         * Returns the {@code Node} directly before this connection, as set when
         * this {@code Connection} was initialized; this value never changes
         * 
         * @return the original {@code Node} directly before this connection
         */
        Node getOriginalNodeBefore() {
            return before;
        }
    }
    
    /**
     * An {@code Iterator} implementation that iterates over the linked list
     * maintained by the containing {@code ConcurrentCollection}.  None of this
     * class' methods ever throws {@code ConcurrentModificationException}.
     * 
     * @author  jobollin
     * @version 0.9.0
     */
    private class NodeIterator implements Iterator<E> {

        /**
         * The {@code Connection} defining this iterator's current position
         * within the iteration 
         */
        private Connection currentConnection = headNode.getNextConnection();
        
        /**
         * A flag indicating whether this iterator thinks it can delete the
         * {@code Node} it most recently provided; this variable is set
         * {@code true} by {@code next()}, and {@code false} by {@code remove()}
         */
        private boolean canDelete = false;
        
        /**
         * {@inheritDoc}
         * 
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return (currentConnection.getNodeAfter()
                        != ConcurrentCollection.this.tailNode);
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.util.Iterator#next()
         */
        public E next() {
            Node next = currentConnection.getNodeAfter();
            
            if (next == ConcurrentCollection.this.tailNode) {
                throw new NoSuchElementException("No more elements");
            } else {
                currentConnection = next.getNextConnection();
                canDelete = true;
                return next.getValue();
            }
        }

        /**
         * {@inheritDoc}.  Removes the most recent element returned by this
         * Iterator, even if the underlying Collection has been modified since
         * that element was returned.
         * 
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            /*
             * There are two failure cases here:
             * (1) This Iterator has no previous element because next() has not
             *     been invoked on it since its creation or the most recent
             *     invocation of remove() on it
             * (2) This iterator has no previous element because its erstwhile
             *     previous element was removed by some other agency
             * The first fails noisily (IllegalStateException), whereas the
             * second fails silently (no discernable effect)
             */
            
            if (!canDelete) {
                throw new IllegalStateException("No element to remove");
            } else {
                Node node = currentConnection.getOriginalNodeBefore();
                
                canDelete = false;

                if (!node.isDeleted()) {
                    node.markDeleted();
                    
                    Connection previousConnection = node.getPreviousConnection();
                    Node newPreviousNode = previousConnection.getNodeBefore();
                    Node nextNode = currentConnection.getNodeAfter();
                    Connection newConnection
                            = new Connection(newPreviousNode, nextNode);
                    
                    previousConnection.setDelegate(newConnection);
                    newPreviousNode.setNextConnection(newConnection);
                    
                    currentConnection.setDelegate(newConnection);
                    nextNode.setPreviousConnection(newConnection);
                    
                    ConcurrentCollection.this.size--;
                }
            }
        }
    }
}
