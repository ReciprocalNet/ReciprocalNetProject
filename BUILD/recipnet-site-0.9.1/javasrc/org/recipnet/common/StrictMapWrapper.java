/*
 * Reciprocal Net project
 * 
 * StrictMapWrapper.java
 * 
 * 28-Jun-2005: ekoperda wrote first draft
 * 28-Mar-2006: jobollin reformatted the code and added type parameterization
 */

package org.recipnet.common;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A {@code Map} implementation that wraps another {@code Map} to apply a strict
 * key-checking policy for object retrieval. When this class's implementation of
 * {@link #get(Object)} is invoked, it requires that the key object either be
 * {@code null} or be among the map's current keys; otherwise it throws an
 * exception.  This implementation at all times projects a mapping from the
 * {@code null} key to a {@code null} value onto the underlying {@code Map}.
 * This behavior is intended to be useful for trapping programming errors.
 * 
 * @param <K> the type of the keys to this {@code Map}
 * @param <V> the type of the values in this {@code Map}
 * @deprecated this class, as conceived, cannot comply with the {@code Map}
 *             contract that it purports to implement. The main issues revolve
 *             around the {@code get(Object)} method automatically supplying the
 *             value {@code null} for {@code null} keys, and what that implies
 *             about the correct behavior of the <em>other</em> methods. Some
 *             of the other methods can be readily patched up for consistency,
 *             but others (especially those that generate the collection views)
 *             are much harder, and some, such as {@link #clear()}, admit no
 *             possible implementation that is consistent with the chosen
 *             implementation of {@code get(Object)}.  The <em>other</em>
 *             variant behavior of {@code get(Object)} (throwing an exception
 *             for keys that are not present in the map) is also inconsistent
 *             with the {@code Map} interface, but this behavior is at least
 *             localized. 
 */
@Deprecated
public class StrictMapWrapper<K, V> implements Map<K, V> {
    
    private Map<K, V> map;

    /**
     * Constructs a new wrapper around the caller-specified underlying map.
     * 
     * @param underlyingMap the map that will back this wrapper. The caller may
     *        continue to accecss this underlying map directly if he wishes.
     */
    public StrictMapWrapper(Map<K, V> underlyingMap) {
        this.map = underlyingMap;
    }

    /**
     * Removes all mappings from this map
     */
    public void clear() {
        map.clear();
    }

    /**
     * Determines whether this map contains a mapping for the specified key
     * 
     * @param  key the key object to be tested
     * @return {@code true} if the specified key is present in this map,
     *         {@code false} if not
     */
    public boolean containsKey(Object key) {
        return ((key == null) || map.containsKey(key));
    }

    /**
     * Determines whether this map contains a mapping to the specified value
     * 
     * @param  value the value object to test for
     * @return {@code true} if the specified value is present in this map,
     *         {@code false} if not
     */
    public boolean containsValue(Object value) {
        return ((value == null) || map.containsValue(value));
    }

    /**
     * Returns a {@code Set} view of this map's entries; the view is backed by
     * this map, so that removing an element or changing its value object
     * removes or modifies the corresponding mapping
     * 
     * @return this map's entry set
     */
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    /**
     * Determines whether this map is equal to the specified object, which is
     * true exactly when it is equal to the underlying map
     * 
     * @param  o the {@code Object} to test for equality with this one
     * @return {@code true} if the specified object is equal to this one,
     *         {@code false} if not 
     */
    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    /**
     * Retrieves the value in this {@code Map} that is referenced by the
     * specified key, except that {@code null} is always returned if the key is
     * {@code null}, and otherwise an exception is thrown if the specified key
     * is not mapped to any value.
     * 
     * @return the value to which the underlying map maps the specified key, or
     *         {@code null} if the key is {@code null}
     * @param key key whose associated value is to be returned; may be
     *        {@code null}
     * @throws ClassCastException if the key is of an inappropriate type for the
     *         underlying map (optional)
     * @throws IllegalStateException if the specified key is not {@code null}
     *         and is not present in the underlying map.
     */
    public V get(Object key) {
        if (key == null) {
            return null;
        } else if (!map.containsKey(key)) {
            throw new IllegalStateException("Desired key '" + key
                    + "' not present in map");
        } else {
            return map.get(key);
        }
    }

    /**
     * Returns a hash code for this map.
     * 
     * @return a hash code for this map; it is equal to the hash code of the
     *         underlying map
     */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /**
     * Determines whether this map is empty, which is the case if and only if
     * the underlying map is empty
     * 
     * @return {@code true} if this map is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Creates and returns a {@code Set} view of this map's keys
     * 
     * @return a {@code Set} containing this map's keys, in this map's iteration
     *         order; removing elements will remove the corresponding mappings
     *         from this map
     */
    public Set<K> keySet() {
        return map.keySet();
    }

    /**
     * Inserts the specified key/value mapping into this map
     * 
     * @param key the key object
     * @param value the value object
     * @return the previous value (possibly {@code null}) associated with the
     *         specified key, or {@code null} if there is none
     */
    public V put(K key, V value) {
        if (key == null) {
            if (value == null) {
                return null;
            } else {
                throw new NullPointerException(
                        "The null key may only be mapped to a null value");
            }
        }
        
        return map.put(key, value);
    }

    /**
     * Adds all the mappings from the specified map to this one
     * 
     * @param t the {@code Map} from which to copy mappings into this map
     */
    public void putAll(Map<? extends K, ? extends V> t) {
        map.putAll(t);
    }

    /**
     * Removes any mapping for the specified key from this map
     * 
     * @param key the key for the mapping to remove
     * @return the value that was mapped to the specified key, or {@code null}
     *         if there was none
     */
    public V remove(Object key) {
        if (key == null) {
            throw new NullPointerException("The null key may not be removed");
        } else {
            return map.remove(key);
        }
    }

    /**
     * Returns the number of mappings in this map
     * 
     * @return the number of mappings in this map
     */
    public int size() {
        return map.size() + (map.containsKey(null) ? 0 : 1);
    }

    /**
     * Returns a collection view of the values in this map
     * 
     * @return a {@code Collection} of the values in this map; it is backed by
     *         this map, so that removing an element also removes the
     *         corresponding mapping 
     */
    public Collection<V> values() {
        return map.values();
    }
}
