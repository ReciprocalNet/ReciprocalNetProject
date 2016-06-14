/*
 * Reciprocal Net project
 * 
 * SerialNumber.java
 *
 * 21-Jun-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 07-Oct-2002: ekoperda changed comments to mention that this class was 
 *              thread-safe.
 * 24-Apr-2006: jobollin reformatted the source and updated documentation
 */

package org.recipnet.site.core.util;

/**
 * <p>
 * This is a simple utility class whose instances collectively, with very high
 * probability and within the scope of a single consistent clock, provide unique
 * numbers every time asked.
 * </p><p>
 * The serial number is of type {@code long}. The first serial number in the
 * sequence is chosen as follows:
 * <table>
 * <tr><td>40 bits</td><td>time the object was created in seconds since
 * 1-Jan-1970</td></tr>
 * <tr><td>24 bits</td><td>equals 0 (LSB)</td></tr>
 * </table>
 * Each number obtained from a particular instance is one greater than the
 * previous one, unless the previous one was {@code Long.MAX_VALUE}, in which
 * case the next one is {@code Long.MIN_VALUE}.
 * </p><p>
 * An instance of this class might return a number previously returned by a
 * different instance of this class if
 * <ul>
 * <li>the two instances were created within one second of one another, or</li>
 * <li>more than 2<sup>24</sup> serial numbers were requested of the older
 * instance, or</li>
 * <li>the current year as reported by the system clock exceeds 32871.</li>
 * </ul>
 * </p>
 */
public class SerialNumber {
    
    private long num;

    /**
     * Initializes a new {@code SerialNumber} based on the current system time
     */
    public SerialNumber() {
        /* 
         * Seed the counter with the current system time, in seconds since
         * 1-Jan-1970, 00:00:00.
         */
        num = (System.currentTimeMillis() / 1000) << 24;
    }

    /**
     * Retrieves the next serial number in the sequence provided by this object
     * 
     * @return the next serial number, as a {@code long}
     */
    public synchronized long get() {
        return num++;
    }
}
