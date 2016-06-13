/*
 * Reciprocal Net Project
 *
 * ParsedDate.java
 *
 * 18-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.common;

import java.util.Date;

/**
 * Represents a {@code Date} with a granularity code that identifies the
 * granularity to which the date was expressed.  Instances of this class are
 * immutable.
 * 
 * @author jobollin
 * @version 1.0
 */
public class ParsedDate {
    
    /**
     * An enum of the date granularities supported by {@code ParsedDate}
     */
    public enum Granularity {
        
        /**
         * A wild card {@code Granularity} compatible with any other granularity  
         */
        ANY_GRANULARITY,
        
        /**
         * A {@code Granularity} representing precision to units of days
         */
        DAY_GRANULARITY,
        
        /**
         * A {@code Granularity} representing precision to units of seconds
         */
        SECOND_GRANULARITY
    }

    /** the parsed date */
    private final Date date;

    /** the granularity code for this ParsedDate */
    private final Granularity granularity;

    /**
     * Initializes a new {@code ParsedDate} with the specified date and code
     *
     * @param  d the {@code Date} for this {@code ParsedDate} to represent
     * @param  g the granularity for this ParsedDate to represent
     */
    public ParsedDate(Date d, Granularity g) {
        date = d;
        granularity = g;
    }

    /**
     * Returns a copy of the Date represented by this ParsedDate
     *
     * @return a Date that is a copy of the Date represented by this
     *         ParsedDate.  It may be modified without affecting this
     *         ParsedDate.
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    /**
     * Returns the granularity code for this ParsedDate
     *
     * @return the granularity code for this ParsedDate
     */
    public Granularity getGranularity() {
        return granularity;
    }

    /**
     * Determines whether this ParsedDate and the specified ParsedDate are
     * compatible in the sense of being legal to use together for selective
     * harvesting by datestamp.  No relative order is assumed.
     *
     * @param  pd the ParsedDate to test for compatibility with this one
     *
     * @return true if and only if <code>pd</code> is compatible with this
     *         ParsedDate
     */
    public boolean isCompatibleWith(ParsedDate pd) {
        return ((this.granularity == Granularity.ANY_GRANULARITY)
                || (pd.granularity == Granularity.ANY_GRANULARITY)
                || (this.granularity == pd.granularity));
    }
}
