/*
 * Reciprocal Net Project
 *
 * NumericStringComparator.java
 *
 * 11-Nov-2002: jobollin wrote first draft
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package 
 *              to org.recipnet.common
 * 07-Nov-2005: jobollin added an appropriate type argument to the class'
 *              implements clause; removed the flag for selecting descending
 *              order instead of ascending order (it was not used, and
 *              Collections.reverseOrder(Comparator) provides a general-purpose
 *              solution to the problem); removed the ClassCastException
 *              behavior of the compare() method, as it no longer makes sense
 *              in combination with generics
 */

package org.recipnet.common;

import java.util.Comparator;

/**
 * A {@code Comparator} implementation that orders numeric (integer)
 * strings in ascending order by the value they represent.  All other strings
 * are sorted after numeric ones, without any defined relative ordering.
 */
public class NumericStringComparator implements Comparator<String> {

    /**
     * An implementation method of the {@code Comparator} interface; determines
     * the relative order of the two specified strings based on the value of the
     * integers they represent
     * 
     * @param  s1 the first / left {@code String} to compare; should be
     *         parseable as an integer via {@link Integer#parseInt(String)}
     * @param  s2 the second / right {@code String} to compare; should be
     *         parseable as an integer via {@link Integer#parseInt(String)}
     * 
     * @return an integer less than, equal to, or greater than zero depending on
     *         whether the number represented by {@code s1} is less than, equal
     *         to, or greater than the one represented by {@code s2}
     */
    public int compare(String s1, String s2) {
        Integer i1;
        Integer i2;
        
        // Parse s1 into an Integer
        try {
            i1 = Integer.valueOf(s1);
        } catch (NumberFormatException nfe) {
            i1 = null;
        }
        
        // Parse s2 into an Integer
        try {
            i2 = Integer.valueOf(s2);
        } catch (NumberFormatException nfe) {
            i2 = null;
        }
        
        /*
         * Integral strings are ordered according to their integer value; all
         * others are ordered after, with no defined relative order among them
         */
        if (i1 == null) {
            return (i2 == null) ? 0 : 1;
        } else {
            return (i2 == null) ? -1 : i1.compareTo(i2);
        }
    }
}

