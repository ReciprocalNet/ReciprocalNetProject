/*
 * Reciprocal Net project
 * 
 * ObjectCache.java
 *
 * 19-Jun-2002: ekoperda wrote first draft
 * 12-Jul-2002: ekoperda added constructor from String
 * 31-Jul-2002: ekoperda fixed bug #207 by modifying code for 3-param
 *              constructor in ObjectCacheItem
 * 16-Aug-2002: ekoperda did a major rewrite for greater efficiency under
 *              harsh, high-load conditions
 * 26-Aug-2002: ekoperda added getAll() method
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 08-Oct-2002: eisiorho fixed bug #476
 * 07-Jun-2004: midurbin moved class to the org.recipnet.common package; used
 *              to be in the org.recipnet.site.core.util package
 * 10-Apr-2006: jobollin made this class generic; reformatted the source;
 *              organized imports; updated some docs, added variant getAll()
 *              method; suppressed the "smart" purging algorithm
 * 05-Jun-2006: jobollin removed access logging and the "smart" purging
 *              algorithm -- they didn't make statistical sense, and in 
 *              practice weren't used
 * 05-Jul-2006: jobollin performed minor cleanup
 * 04-Jan-2008: ekoperda fixed bug #1860 in prune()
 */

package org.recipnet.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Map.Entry;

/**
 * This is a simple utility class that manages a cache of java Objects. It
 * might be particularly useful when implementing look-aside caching for
 * database reads in one of the core modules. This class is thread-safe.
 */
public class ObjectCache<T> {

    /**
     * Main cache - maps ObjectCacheKey's to Objects
     */
    private final Map<ObjectCacheKey, ObjectCacheItem<T>> map;

    /**
     * Secondary cache - maps String's to Objects. Contains all the objects 
     * from the first map for which the secondary key is not null. This value
     * is always null if secondaryKeyEnabled is false.
     */
    private final Map<String, ObjectCacheItem<T>> map2;

    /**
     * The maximum number of items to cache at once
     */
    private final int maxSize;

    /**
     * The maximum time, in milliseconds, that items should be retained in the
     * cache
     */
    private final long maxAge;

    /**
     * The minimum number of free entries to be made available when this cache
     * flushes old and / or excess items
     */
    private final int chunkSizeHint;

    /** Configuration parameter set at creation time */
    private final boolean secondaryKeyEnabled;

    /**
     * Status flag used to support the hasEverLostItem() feature. Set to true
     * if any method within this object has removed an item from the cache.
     */
    private boolean everDeleted;

    /**
     * Initializes a new {@code ObjectCache} with the specified parameters
     * 
     * @param maxSize is the maximum number of Objects the cache will hold.
     *        Putting items into the cache once the cache size reaches this
     *        limit will result in existing cache entries being pruned via an
     *        intelligent removal algorithm.
     * @param logSize nominally, the maximum number of cache hits to keep
     *        records of in a "history log", for the purpose of intelligent
     *        pruning; in practice, the history logging feature has been 
     *        removed
     * @param chunkSizeHint the suggested number of free cache slots that
     *        should be available at the completion of each pruning cycle. 
     *        Setting this value too low or too high might have negative
     *        performance implications.
     * @param maxAge is the maximum number of milliseconds an item may reside
     *        in this cache before it is considered to have expired. Expired
     *        items cannot be retrieved and are eligible for automatic removal.
     *        Set this value to 0 to disable aging.
     * @param logDecayInterval nominally, the number of milliseconds between
     *        successive removal operations from the "history log"; in
     *        practice, the history logging feature has been removed
     * @param randomFactorWeight nominally, a number between 0 and 1 that
     *        indicates the extent to which the pruning algorithm should take a
     *        randomly- generated number into consideration when comparing the
     *        relative scores of two cache items; in practice, this parameter
     *        is unused
     * @param secondaryKeyEnabled is set to true if the cache should be
     *        searchable by a String-type secondary key, in addition to the
     *        usual int-type primary key. Set this to false if this feature is
     *        not required.
     */
    public ObjectCache(int maxSize, @SuppressWarnings("unused") int logSize,
            int chunkSizeHint, long maxAge,
            @SuppressWarnings("unused") long logDecayInterval,
            @SuppressWarnings("unused") double randomFactorWeight,
            boolean secondaryKeyEnabled) {
        this.maxSize = maxSize;
        this.chunkSizeHint = chunkSizeHint;
        this.maxAge = maxAge;
        this.secondaryKeyEnabled = secondaryKeyEnabled;
        everDeleted = false;
        map = new HashMap<ObjectCacheKey, ObjectCacheItem<T>>(maxSize);
        map2 = (secondaryKeyEnabled
                ? new HashMap<String, ObjectCacheItem<T>>(maxSize) : null);
    }

    /**
     * Creates a cache whose parameters are specified by a comma-separated
     * string. Format is 'maxSize,logSize,chunkSizeHint,maxAge,
     * logDecayInterval, randomFactorWeight,secondaryKeyEnabled'
     * 
     * @param <S> the type of the items to be cached in the requested cache
     * @param params the cache parameters as a comma-delemited string, as
     *        described above
     * @return an ObjectCache corresponding to the specified parameters
     * @throws IllegalArgumentException if the string is not parsable according
     *         to the described format
     */
    public static <S> ObjectCache<S> newInstance(String params) {
        try {
            StringTokenizer t = new StringTokenizer(params, ",");

            return new ObjectCache<S>(Integer.parseInt(t.nextToken()),
                    Integer.parseInt(t.nextToken()),
                    Integer.parseInt(t.nextToken()),
                    Long.parseLong(t.nextToken()),
                    Long.parseLong(t.nextToken()),
                    Double.parseDouble(t.nextToken()),
                    Boolean.parseBoolean(t.nextToken()));
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Too few cache parameters");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Malformed cache parameters");
        }
    }

    /**
     * Store the specified Object in the cache, removing other objects if
     * necessary to make room. Efficient searching on the secondary key (a
     * string) will not be possible for this object.
     */
    synchronized public void put(int key, T value) {
        put(key, null, value);
    }

    /**
     * Store the specified Object in the cache, removing other objects if
     * necessary to make room.
     */
    synchronized public void put(int key, String key2, T value) {
        ObjectCacheKey cacheKey;
        
        if (!secondaryKeyEnabled && (key2 != null)) {
            throw new IllegalStateException("Secondary key mode not enabled");
        }

        if (map.size() >= maxSize) {
            prune();
        }
        
        cacheKey = new ObjectCacheKey(key); 
        if (get(key) != null) {
            invalidateRaw(cacheKey);
        }
        ObjectCacheItem<T> item = new ObjectCacheItem<T>(key, key2, value);
        map.put(cacheKey, item);
        if (key2 != null) {
            map2.put(key2, item);
        }
    }

    /**
     * Retrieves the object from the cache that has the specified key. If no
     * matching object is found, return null.
     */
    synchronized public T get(int key) {
        return extractValue(map.get(new ObjectCacheKey(key)));
    }

    /**
     * Retrieves the object from the cache that has the specified secondary key
     * (a string). If no matching object is found, return null.
     */
    synchronized public T get(String key2) {
        if (!secondaryKeyEnabled) {
            throw new IllegalStateException("Secondary key mode not enabled");
        } else {
            return extractValue(map2.get(key2));
        }
    }

    private T extractValue(ObjectCacheItem<T> item) {
        if (item == null) {
            return null;
        } else {
            long currentTime = System.currentTimeMillis();

            if ((maxAge > 0)
                    && ((currentTime - item.getCreationTime()) > maxAge)) {
                
                // The retrieved object is too old; invalidate it
                invalidate(item.getKey());

                return null;
            } else {

                // Update this item's hit counter
                item.recordHit(currentTime);

                // return the item's associated object
                return item.getObject();
            }
        }
    }

    /**
     * Retrieves an array that contains every (valid) object currently in the
     * cache.
     * 
     * @return an {@code Object[]} containing every valid item currently in
     *         this cache
     */
    synchronized public Object[] getAll() {
        Object[] items = new Object[map.size()];

        loadItemArray(items);

        return items;
    }

    /**
     * Retrieves an array that contains every (valid) object currently in the
     * cache, and whose component type is {@code T}
     * 
     * @param clazz a {@code Class} object representing the class corresponding
     *        to {@code T}
     * @return an array of the component type described by {@code clazz},
     *         containing every valid item currently in this cache
     */
    @SuppressWarnings("unchecked")
    synchronized public T[] getAll(Class<T> clazz) {
        T[] items = (T[]) Array.newInstance(clazz, map.size());

        loadItemArray(items);

        return items;
    }

    /**
     * Loads an array with every (valid) object currently in the cache.
     * 
     * @param items the array to load; must be long enough to hold all the
     *        current items, and must have a runtime component type the with
     *        which this cache's item type is assignment compatible.
     */
    private void loadItemArray(Object[] items) {
        int i = 0;

        for (ObjectCacheItem<T> cacheItem : map.values()) {
            items[i++] = cacheItem.getObject();
        }
    }

    /**
     * Create additional space in the cache by removing old entries. It may be
     * desirable to call this function periodically, during periods of 
     * otherwise low CPU usage, in order to improve performance in subsequent
     * calls to put(). There will be at most maxSize-chunkSizeHint entries in
     * the cache when this method exits.
     */
    public void prune() {

        /*
         * if we're enforcing a maximum age then scan the map and remove all
         * items that have expired
         */ 
        int toRemove;

        if (maxAge > 0) {
            long bound = System.currentTimeMillis() - maxAge;
            
            for (Iterator<Entry<ObjectCacheKey, ObjectCacheItem<T>>> it
                    = map.entrySet().iterator(); it.hasNext();) {
                ObjectCacheItem<?> item = it.next().getValue();
                
                if (bound > item.getCreationTime()) {
                    it.remove();
                    if (secondaryKeyEnabled && item.getKey2() != null) {
                        map2.remove(item.getKey2());
                    }
                    everDeleted = true;
                }
            }
        }

        toRemove = chunkSizeHint - (maxSize - map.size());

        // No need to prune further if we now have adequate unused space
        if (toRemove <= 0) {
            return;
        }

        // Create an ordered list to hold all the cache items
        List<ObjectCacheItem<T>> list = new ArrayList<ObjectCacheItem<T>>(
                map.values());

        // Sort the item list into order by decreasing removal priority
        Collections.sort(list, new LastAccessItemComparator());

        /*
         * Remove an appropriate number of the items prioritized highest for
         * removal
         */
        for (ObjectCacheItem<?> item : list.subList(0, toRemove)) {
            ObjectCacheItem<?> removed
                    = map.remove(new ObjectCacheKey(item.getKey()));

            assert (removed == item);
            if (secondaryKeyEnabled && item.getKey2() != null) {
                removed = map2.remove(item.getKey2());
                assert (removed == item);
            }
        }

        /*
         * We wouldn't have gotten to this point if there weren't any items to
         * delete
         */
        everDeleted = true;
    }

    /**
     * Removes from the cache the object, if any, associated with the specified
     * key
     */
    synchronized public void invalidate(int key) {
        invalidateRaw(new ObjectCacheKey(key));
    }

    /** Remove all entries from the cache. */
    synchronized public void flush() {
        if (!map.isEmpty()) {
            everDeleted = true;
        }
        map.clear();
        map2.clear();
    }

    /** Returns the number of entries currently in the cache. */
    synchronized public int size() {
        return map.size();
    }

    /**
     * Returns the maximum number of milliseconds items may live in this cache,
     * as specified at creation time.
     */
    synchronized public long getMaxAge() {
        return maxAge;
    }

    /**
     * Returns the maximum number of items that may be cached in this cache, as
     * specified at creation time.
     */
    synchronized public int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns true if this cache has ever lost an item due to old-age
     * expiration, insufficient space pruning, or explicit invalidation.
     */
    synchronized public boolean hasEverLostItem() {
        return everDeleted;
    }

    /** Removes the item with the specified key from both maps, if it exists */
    synchronized private void invalidateRaw(ObjectCacheKey key) {
        ObjectCacheItem<?> item = map.remove(key);

        if (item != null) {
            String key2 = item.getKey2();

            if (key2 != null) {
                map2.remove(key2);
            }
            everDeleted = true;
        }
    }
}

/**
 * This class is internal to the ObjectCache; it's the datatype that is stored
 * in the underlying map.
 */
class ObjectCacheItem<T> {
    private final int key;

    private final String key2;

    private final T obj;

    private final long creationTime;

    private long accessTime;

    private int hits;

    ObjectCacheItem(int key, T obj) {
        this(key, null, obj);
    }

    ObjectCacheItem(int key, String key2, T obj) {
        this.key = key;
        this.key2 = key2;
        this.obj = obj;
        creationTime = System.currentTimeMillis();
        accessTime = creationTime;
        hits = 1;
    }

    public int getKey() {
        return key;
    }

    public String getKey2() {
        return key2;
    }

    public T getObject() {
        return obj;
    }

    public void recordHit(long timeOfHit) {
        hits++;
        accessTime = timeOfHit;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return accessTime;
    }
}

/**
 * This class is internal to the ObjectCache; it's the datatype that is stored
 * as the key for Objects in the underlying map. Immutable once created.
 */
class ObjectCacheKey {
    private final int key;

    private final int hashValue;

    ObjectCacheKey(int key) {
        this.key = key;
        hashValue = String.valueOf(key).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ObjectCacheKey) {
            ObjectCacheKey x = (ObjectCacheKey) o;
            
            return (this.key == x.key);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashValue;
    }
}

/**
 * A {@code Comparator} for {@code ObjectCacheItem}s that makes use of last
 * access times for the specified items to determine their relative ranking.
 * Items that are more preferred for deletion compare less than items that are
 * less preferred for deletion.
 */
class LastAccessItemComparator implements Comparator<ObjectCacheItem<?>> {

    /**
     * Compares the two items to evaluate which is more preferred for deletion
     * from its cache
     * 
     * @param a the first item to compare
     * @param b the second item to compare
     * @return an integer less than, equal to, or greater than zero, depending
     *         on whether item {@code a} is more, equally, or less preferred
     *         for deletion (respectively) than {@code b}
     */
    public int compare(ObjectCacheItem<?> a, ObjectCacheItem<?> b) {
        long aScore = a.getLastAccessTime();
        long bScore = b.getLastAccessTime();

        if (aScore < bScore) {
            return -1;
        } else if (aScore > bScore) {
            return 1;
        } else {
            return 0;
        }
    }
}
