/*
 * Reciprocal Net Project
 *
 * MultiTypeCache.java
 *
 * 20-Oct2005: jobollin wrote first draft
 */

package org.recipnet.common;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * A dual-key map where one of the keys is the {@code Class} object representing
 * the declared type of the associated value.  This class is especially intended
 * for caching multiple object types in the same cache object, with type safety
 * and protection against key collision between objects of different types.
 * This class makes considerable use of hashing, so it is advisable that the key
 * type selected have a good {@code hashCode()}.
 * </p><p>
 * This class permits {@code null} keys and values, but value types must not be
 * specified as {@code null}.
 * </p>
 *
 * @param  <K> the (common) key type for the cache 
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class MultiTypeCache <K> {
    
    /**
     * An internal {@code Map} associating {@code Class} objects with
     * {@code Map}s from keys of type {@code K} to objects of the type
     * represented by that Class
     */
    private final Map<Class<?>, Map<K, ?>> masterMap =
            new HashMap<Class<?>, Map<K, ?>>();
    
    /**
     * Puts an object of the specified declared type into this cache, associated
     * with the specified key
     *  
     * @param  <V> the declared type of the object to cache
     * @param  key the key with which to associate the object
     * @param  valueType the {@code Class} object representing the declared
     *         type of the value 
     * @param  value the value (of type {@code V}) to cache
     * 
     * @return the previous value of type {@code V} associated with the
     *         specified key, if any, otherwise {@code null}
     */
    public <V> V put(K key, Class<V> valueType, V value) {
        return getMapFor(valueType).put(key, value);
    }
    
    /**
     * Determines whether this cache contains a mapping from the specified key
     * to an object of the specified type
     * 
     * @param  key the key to which the inquiry pertains
     * @param  valueType a {@code Class} representing the declared type by which
     *         the inquiry is scoped
     *         
     * @return {@code true} if the cache contains a mapping satisfying the
     *         parameters, {@code false} otherwise
     */
    public boolean containsKey(K key, Class<?> valueType) {
        return getMapFor(valueType).containsKey(key);
    }
    
    /**
     * Retrieves the cached item of the specifed type that corresponds to the
     * specified key
     * 
     * @param  <V> the declared type of the value
     * @param  key the key associated with the desired value
     * @param  valueType the {@code Class} representing the declared type of the
     *         desired value
     *         
     * @return the value of the specified type associated with the specified
     *         key, if any, otherwise {@code null}
     */
    public <V> V get(K key, Class<V> valueType) {
        return getMapFor(valueType).get(key);
    }
    
    /**
     * Removes from this cache the item of the specifed type that corresponds to
     * the specified key, if such an item is present
     * 
     * @param  <V> the declared type of the value
     * @param  key the key associated with the desired value
     * @param  valueType the {@code Class} representing the declared type of the
     *         desired value
     *         
     * @return the item that was removed, if any, otherwise {@code null}
     */
    public <V> V remove(K key, Class<V> valueType) {
        return getMapFor(valueType).remove(key);
    }

    /**
     * Removes all values of the specified declared type from this cache
     * 
     * @param  valueType the {@code Class} representing the type of objects to
     *         remove
     */
    public void clear(Class<?> valueType) {
        masterMap.remove(valueType);
    }
    
    /**
     * Removes all items from this cache
     */
    public void clearAll() {
        masterMap.clear();
    }
    
    /**
     * Returns the inner {@code Map} used for caching objects of the specified
     * type, creating one if necessary
     * 
     * @param  <V> the type of the values in the returned map 
     * @param  valueType the {@code Class} representing the declared type of the
     *         values in the returned map
     *         
     * @return the {@code Map<K, V>} used for storage of items of type {@code V}
     */
    @SuppressWarnings("unchecked")
    private <V> Map<K, V> getMapFor(Class<V> valueType) {
        if (valueType == null) {
            throw new NullPointerException("The value type must not be null");
        }
        Map<K, V> map = (Map<K, V>) masterMap.get(valueType);
        
        if (map == null) {
            map = new HashMap<K, V>();
            masterMap.put(valueType, map);
        }
        
        return map;
    }
}
