/*
 * Reciprocal Net project
 * 
 * UserInfo.java
 *
 * 03-Jun-2002: hclin wrote skeleton
 * 20-Jun-2002: leqian coded checkPassword()
 * 21-Jun-2002: ekoperda added db access code
 * 25-Jun-2002: ekoperda added serialization code
 * 19-Jul-2002: jobollin added setPassword(String) and modified
 *              checkPassword(String)
 * 13-Aug-2002: ekoperda fixed date-recording bug in UserInfo() and dbStore()
 * 15-Nov-2002: adharurk added Comparable interface
 * 15-Nov-2002: eisiorho added clonable interface
 * 21-Feb-2003: ekoperda added two 1-param constructors and implemented
 *              ContainerObject interface, exceptionized generateHash()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db
 * 16-Nov-2004: midurbin added comments indicating the default search order
 * 17-Nov-2004: eisiorho added database variable 'preferences'
 * 26-Apr-2005: midurbin changed type of 'preferences' member
 * 10-Jun-2005: midurbin added SUBMITTING_PROVIDER_ACCESS
 * 16-Sep-2005: midurbin moved checkPassword(), setPassword(), generateHash()
 *              and bytesToHexString() to UserBL from this class
 * 21-Apr-2006: jobollin converted to generics, reformatted the source, and
 *              removed unused imports
 * 31-May-2006: jobollin performed minor additional cleanup
 */

package org.recipnet.site.shared.db;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.io.Serializable;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.UserPreferences;

/**
 * a container class that maps very cleanly onto the database table named
 * 'users'.  It contains basic information about a particular user in
 * Reciprocal Net.  The sort order for {@code UserInfo} objects is
 * guaranteed to be alphabetical by {@code fullname}.
 */
public class UserInfo implements Cloneable, Comparable<UserInfo>,
        ContainerObject, Serializable {

    /** Default value for an invalid user id.  */
    public static final int INVALID_USER_ID = -1;

    /**
     * a bit mask to select the scientific user bit from the
     * globalAccessLevel
     */
    public static final int LAB_SCIENTIFIC_USER_ACCESS = 0x01;

    /**
     * a bit mask to select the lab administrator bit from the
     * globalAccessLevel
     */
    public static final int LAB_ADMIN_ACCESS =    0x02;

    /**
     * a bit mask to select the site administrator bit from the
     * globalAccessLevel
     */
    public static final int SITE_ADMIN_ACCESS =   0x04;

    /**
     * a bit mask to select the submitting provider bit from the
     * globalAccessLevel
     */
    public static final int SUBMITTING_PROVIDER_ACCESS =   0x08;

    /** A centrally-assigned, globally unique identifier for the user. */
    public int id;

    /**
     * labId identifies the lab this user account is associated with.
     * labId should equal LabInfo.INVALID_LAB_ID if this user account belongs
     * to a sample provider.
     */
    public int labId;

    /**
     * providerId identifies the sample provider this user account is
     * associated with {@code providerId} should equal to
     * {@code ProviderInfo.INVALID_PROVIDER_ID} if this user account
     * belongs to a lab.
     */
    public int providerId;

    /** The date and time at which the account was first created. */
    public Date creationDate;

    /** The data and time at which the account was deactivated. */
    public Date inactiveDate;

    /** true if the user account should able to log in, false otherwide. */
    public boolean isActive;

    /**
     * Full name of the person who uses this account in last name, first name
     * order. If the account is assigned to a whole sample provider, not a
     * particular person, then this value should be the sample provider's name.
     */
    public String fullName;

    /** The name of the acount, used when logging in. */
    public String username;

    /**
     * A textual-hexadecimal representation of an MD5 hash of the password
     * associated with this account.
     */
    public String password;

    /**
     * An object which contains various preferences concerning web application
     * behavior.  This object is initialized by the constructor and is
     * guaranteed to never be null.
     */
    public UserPreferences preferences;

    /** Indicates this account's appropriate access level. */
    public int globalAccessLevel;

    /**
     * A default constructor is necessary for this class to be passed by
     * RMI
     */
    public UserInfo() {
        id = INVALID_USER_ID;
        labId = LabInfo.INVALID_LAB_ID;
        providerId = ProviderInfo.INVALID_PROVIDER_ID;
        creationDate = null;
        inactiveDate = null;
        isActive = false;
        fullName = null;
        username = null;
        password = null;
        preferences = new UserPreferences();
        globalAccessLevel = 0;
    }

    /**
     * One-param constructor that sets the id field as specified and sets the
     * other fields to their default (invalid) values.
     */
    public UserInfo(int id) {
        this();
        this.id = id;
    }

    /**
     * One-param constructor that sets the {@code username} field as
     * specified and sets the other fields to their default (invalid) values.
     */
    public UserInfo(String username) {
        this();
        this.username = username;
    }

    /**
     * <p>
     * from interface Comparable; sorts these objects in alphabetical order by
     * {@code fullName}.
     * </p><p>
     * <strong>NOTE: the natural order defined by this method is inconsistent
     * with equals().</strong> This class does not override
     * {@code Object.equals()}, therefore two distinct {@code UserInfo} objects
     * with equal {@code fullName} fields will be unequal to each other, yet
     * this method will return 0 when comparing one to the other. Even if
     * this class <em>were</em> to override {@code equals()}, an an
     * implementation with which this natural order would be consistent
     * would probably not be appropriate.
     * </p>
     * 
     * @param otherUser the {@code UserInfo} with which to compare this one
     * @return -1, 0, or 1 as this {@code UserInfo} is less than, equal to, or
     *         greater than {@code otherUser} with respect to their natural
     *         order
     */
    public int compareTo(UserInfo otherUser) {
        return (this.fullName.compareTo(otherUser.fullName));
    }

    /**
     * <p>
     * Creates a distinct copy of this {@code UserInfo} that has equivalent
     * values for all fields. The clone will be independent of this
     * {@code UserInfo}, so it and its members can be freely modified without
     * effect on this {@code UserInfo}.
     * </p><p>
     * <strong>NOTE:</strong> Because this class does not override
     * {@code Object.equals()}, instances and their clones will be inequal
     * despite (initially) having the same values for all fields.
     * </p>
     */
    @Override
    public UserInfo clone() {
        try {
            UserInfo x = (UserInfo) super.clone();
            
            if (this.creationDate != null) {
                x.creationDate = new Date(this.creationDate.getTime());
            }
            if (this.inactiveDate != null) {
                x.inactiveDate = new Date(this.inactiveDate.getTime());
            }
            x.preferences = this.preferences.clone();
            
            return x;
        } catch (CloneNotSupportedException cnse) {
            throw new UnexpectedExceptionException();
        }
    }

    /** Unique serial number used for optimistic locking */
    public long ts;

    /**
     * Constructor to create this object from the current record in a db
     *    resultset
     */
    public UserInfo(ResultSet rs) throws SQLException {
        id = rs.getInt("id");
        labId = rs.getInt("lab_id");
        if (rs.wasNull()) {
            labId = LabInfo.INVALID_LAB_ID;
        }
        providerId = rs.getInt("provider_id");
        if (rs.wasNull()) {
            providerId = ProviderInfo.INVALID_PROVIDER_ID;
        }
        isActive = rs.getBoolean("active");
        creationDate = rs.getTimestamp("creationDate");
        inactiveDate = rs.getTimestamp("inactiveDate");
        fullName = rs.getString("fullname");
        username = rs.getString("username");
        password = rs.getString("password");
        preferences = new UserPreferences(rs.getLong("preferences"));
        globalAccessLevel = rs.getInt("globalAccessLevel");
        ts = rs.getLong("ts");
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("id", id);
        if (labId != LabInfo.INVALID_LAB_ID) {
            rs.updateInt("lab_id", labId);
        } else {
            rs.updateNull("lab_id");
        }
        if (providerId != ProviderInfo.INVALID_PROVIDER_ID) {
            rs.updateInt("provider_id", providerId);
        } else {
            rs.updateNull("provider_id");
        }
        rs.updateBoolean("active", isActive);
        assert (creationDate != null) : "No user creation date assigned";
        rs.updateTimestamp(
                "creationDate", new Timestamp(creationDate.getTime()));
        if (inactiveDate != null) {
            rs.updateTimestamp(
                    "inactiveDate", new Timestamp(inactiveDate.getTime()));
        } else {
            rs.updateNull("inactiveDate");
        }
        rs.updateString("fullname", fullName);
        rs.updateString("username", username);
        rs.updateString("password", password);
        rs.updateLong("preferences", preferences.preferences);
        rs.updateInt("globalAccessLevel", globalAccessLevel);
        rs.updateLong("ts", ts);
    }
}
