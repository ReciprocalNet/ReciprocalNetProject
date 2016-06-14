/*
 * Reciprocal Net project
 * 
 * UserPreferences.java
 *
 * 26-Apr-2005: midurbin wrote first draft
 * 26-May-2005: midurbin updated references to UserActionBL
 * 21-Apr-2006: jobollin rewrote most of this class, with slight changes to the
 *              details of hash and equality computations
 */

package org.recipnet.site.shared;

import java.io.Serializable;

import org.recipnet.site.UnexpectedExceptionException;

/**
 * Mostly-raw information about a user's preferences with respect to web
 * application behavior and presentation.  Every {@code UserInfo} contains an
 * instance of this class, but instances may also be used independently of other
 * user information, such as in the context of an unauthenticated session web
 * session.
 */
public class UserPreferences implements Cloneable, Serializable {

    /**
     * All of the preferences, stored as bits in a long.  {@link
     * org.recipnet.site.shared.bl.UserBL UserBL} has
     * various methods to meaningfully interact with this.
     */
    public long preferences;

    /** 
     * Initializes a new {@code UserPreferences} with default preference values
     */
    public UserPreferences() {
        this.preferences = 0L;
    }

    /**
     * Initializes a new {@code UserPreferences} with the specified initial
     * preference values
     * 
     * @param initialPreferences the initial preference values, encoded as a
     *        {@code long}; {@code UserBL} defines the encoding
     */
    public UserPreferences(long initialPreferences) {
        this.preferences = initialPreferences;
    }

    /**
     * Determines whether the specified object is equal to this one
     * 
     * @param obj the object to compare with this one for equality
     * 
     * @return {@code true} if {@code obj} is a {@code UserPreferences} instance
     *         having the same preference values as this one; {@code false}
     *         otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof UserPreferences) {
            return this.preferences == ((UserPreferences) obj).preferences;
        } else {
            return false;
        }
    }

    /**
     * Computes a hash code for this {@code UserPreferences} based on its
     * preference values.  This hash computation is consistent with
     * {@link #equals(Object) equals()}
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return (int) ((preferences >> 32) ^ preferences);
    }

    /**
     * Creates a new {@code UserPreferences} that is distinct from this one but
     * {@link #equals(Object) equal} to it.
     * 
     * @return a clone of this {@code UserPreferences}
     */
    @Override
    public UserPreferences clone() {
        
        /*
         * Though this method currently relies exclusively on Object.clone(),
         * it may need in the future to correctly perform deep copies of
         * complex member variables (once such members are added to the class).
         * Clients that rely on this method to copy instances will not need to
         * be modified to accommodate (this aspect of) the change.
         */
        try {
            return (UserPreferences) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Can't happen -- this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }
}
