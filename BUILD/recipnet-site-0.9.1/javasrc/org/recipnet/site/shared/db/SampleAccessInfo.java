/*
 * Reciprocal Net project
 * 
 * SampleAccessInfo.java
 *
 * 03-Jun-2002: leqian wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 09-Jul-2002: ekoperda added 2-param constructor
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 27-Oct-2005: jobollin added an appropriate equals(Object) method and matching
 *              hashCode() method; made clone() use a covariant return type to
 *              declare that it returns a SampleAccessInfo
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.recipnet.site.UnexpectedExceptionException;

/**
 * SampleAccessInfo is a container class that maps onto the database table named
 * 'sampleAcl'.
 */
public class SampleAccessInfo implements Serializable, Cloneable {

    public static final int INVALID_SAMPLE_ACCESS_ID = -1;

    /** Constants used for accessLevel property. */
    public static final int INVALID_ACCESS = 0;

    public static final int READ_ONLY = 100;

    public static final int READ_WRITE = 200;

    /**
     * The unique identifying number of this {@code SampleAccessInfo}
     */
    public int id;

    /**
     * The sample ID to which this {@code SampleAccessInfo} pertains
     */
    public int sampleId;

    /**
     * ID of a row in the users table that identifies the user account that is
     * been granted access to this sample
     */
    public int userId;

    /**
     * The desired access level of this user on this particular sample. Possible
     * values are defined above.
     */
    public int accessLevel;

    /** Create an empty object */
    public SampleAccessInfo() {
        id = INVALID_SAMPLE_ACCESS_ID;
        sampleId = SampleInfo.INVALID_SAMPLE_ID;
        userId = UserInfo.INVALID_USER_ID;
        accessLevel = INVALID_ACCESS;
    }

    /** Create a new record with a user id and access level */
    public SampleAccessInfo(int userId, int accessLevel) {
        id = INVALID_SAMPLE_ACCESS_ID;
        sampleId = SampleInfo.INVALID_SAMPLE_ID;
        this.userId = userId;
        this.accessLevel = accessLevel;
    }

    @Override
    public SampleAccessInfo clone() {
        try {
            return (SampleAccessInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {

            // Cannot happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }

    /**
     * Determines whether the specified object is equal to this one, which is
     * the case if it is also a {@code SampleAccessInfo} and its instance fields
     * all match this one's
     * 
     * @param o the {@code Object} to compare for equality with this one
     * @return {@code true} if the specified object is equal to this one,
     *         {@code false} if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof SampleAccessInfo) {
            SampleAccessInfo sai = (SampleAccessInfo) o;

            return ((this.accessLevel == sai.accessLevel)
                    && (this.id == sai.id)
                    && (this.sampleId == sai.id)
                    && (this.userId == sai.userId));
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this method that is consistent with its equals()
     * method. That is, any other {@code SampleAccessInfo} that is equal to this
     * one will have the same hash code. This hash code reflects the internal
     * state of this object, therefore it will change if this object is
     * modified. That makes instances potentially unsafe for use in
     * {@code HashSet}s or as keys to {@code HashMap}s.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return ((accessLevel * 37) ^ id ^ sampleId ^ (userId << 16));
    }

    /*
     * TODO: create a new class called CoreSampleAccessInfo that extends
     * SampleAccessInfo. Then, take everything that appears below this line and
     * move it to that class. Only Sample Manager needs access to the variables
     * and methods found below.
     */
    /**
     * Initializes a new SampleAccessInfo with values from the current row of
     * the specified {@code ResultSet}
     * 
     * @param rs the {@code ResultSet} from which to obtain this object's
     *        attributes
     */
    public SampleAccessInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        sampleId = rs.getInt("sample_id");
        userId = rs.getInt("user_id");
        accessLevel = rs.getInt("accessLevel");
    }

    /**
     * Stores the attributes of {@code SampleAccessInfo} in the columns of the
     * current row of the provided result set
     * 
     * @param rs the {@code ResultSet} into which this objects attributes should
     *        be written
     */
    public void dbStore(ResultSet rs) throws SQLException {
        if (id != INVALID_SAMPLE_ACCESS_ID) {
            rs.updateInt("id", id);
        } else {
            rs.updateNull("id");
        }
        rs.updateInt("sample_id", sampleId);
        rs.updateInt("user_id", userId);
        rs.updateInt("accessLevel", accessLevel);
    }
}
