/*
 * Reciprocal Net Project
 * @(#) UserBL.java
 *
 * 05-Jan-2005: jobollin wrote first draft
 * 26-Apr-2005: midurbin updated class to operate on UserPreferences objects,
 *              added a helpful diagram of the bit layout and added the method
 *              parsePreferenceString()
 * 04-May-2005: midurbin added support for showsample page mode preferences
 * 10-Jun-2005: midurbin renamed class as UserBL, consolidated constants
 *              into the Preferences enum and added deactivateUser()
 * 18-Aug-2005: midurbin added reserved initialization bit,
 *              havePreferencesBeenInitialized() and code to
 *              parsePreferenceString() to cause the returned UserPreferences
 *              to be flagged as initialized.
 * 19-Aug-2005: midurbin added VALIDATE_SPACE_GROUP preference
 * 16-Sep-2005: midurbin moved checkPassword(), setPassword(), generateHash()
 *              and bytesToHexString() to this class from UserInfo
 * 28-Oct-2005: midurbin added various new preference options
 */

package org.recipnet.site.shared.bl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.db.UserInfo;

/**
 * This class defines all the business logic related to user actions and the
 * manipulation of {@link org.recipnet.site.shared.db.UserInfo UserInfo} or
 * their nested {@link org.recipnet.site.shared.UserPreferences
 * UserPreferences} objects.  Included is an enumeration of user preferences
 * which contain code to determine or modify a <code>UserPreferences</code>
 * object.
 */
public final class UserBL {

    /** An enumeration of applet preference settings. */
    public static enum AppletPref {
        MINIJAMM (0),
        JAMM1    (1),
        JAMM2    (2);

        public final int code;

        AppletPref(int code) {
            this.code = code;
        }
    }

    /** An enumeration of sample page view preference settings. */
    public static enum SampleViewPref {
        SHOWSAMPLE (0),
        SAMPLE     (1);

        public final int code;

        SampleViewPref(int code) {
            this.code = code;
        }
    }

    /** An enumeration of showsample page views. */
    public static enum ShowsampleViewPref {
        BASIC    (0),
        DETAILED (1);

        public final int code;

        ShowsampleViewPref(int code) {
            this.code = code;
        }
    }

    /** An enumeration of file upload mechanisms. */
    public static enum UploadMechanismPref {
        FORM_BASED    (0),
        DRAG_AND_DROP (1);

        public final int code;

        UploadMechanismPref(int code) {
            this.code = code;
        }
    }

    /**
     * overrides the default constructor so that this class cannot be
     * instantiated
     */
    private UserBL() { /* do nothing */ }

    /**
     * Deactivates the specified user.
     * @param user the <code>UserInfo</code> for the user to be deactivated
     */
    public static void deactivateUser(UserInfo user) {
        user.isActive = false;
        user.inactiveDate = new Date();
    }

    /**
     * Checks whether the provided password string matches the password
     * string for the provided user.
     *
     * @param user the user whose password is being checked
     * @param password a <code>String</code> to attempt to match against
     *     the users password
     * @return <code>true</code> if the passwords match; <code>false</code>
     *     otherwise.  <code>false</code> is always returned if this
     *     <code>UserInfo</code>'s password variable is <code>null</code>.
     *     (<em>I.e.</em> a <code>null</code> password is not considered
     *     to match anything, even another <code>null</code>.)
     *
     */
    public static boolean checkPassword(UserInfo user, String password) {
        try {
            return ( generateHash(password).equals(user.password) );
        } catch (NullPointerException e) {
            /*
             * the password argument was null; return false even if
             * user.password is also null.  Note that this behavior is not
             * congruent with setPassword, which will throw a
             * NullPointerException if its password argument is null.
             */
            return false;
        }
    }

    /**
     * Sets the password for the provided user.  An MD5 hash of the
     * supplied password string is stored for the user.  No password policy is
     * enforced by this method
     *
     * @param user the user whose password is to be set
     * @param password a <code>String</code> containing the new password
     *     in plaintext; should not be <code>null</code>
     * @throws NullPointerException if <code>password</code> is
     *         <code>null</code>
     */
    public static void setPassword(UserInfo user, String password) {
        if (password == null) {
            throw new NullPointerException();
        }
        user.password = generateHash(password);
    }

    /**
     * Generates an MD5 hash of the UTF-8 encoding of a <code>String</code>.
     *
     * @param  plaintext a <code>String</code> containing the text to hash
     * @return the MD5 hash of the UTF-8 encoding of the argument, as a
     *         <code>String</code> of hexadecimal digits
     * @throws UnexpectedExceptionException if the MD5 algorithm or UTF-8
     *     encoding is not available.  Either would indicate a major
     *     nonstandardism or flaw in the JRE.
     */
    private static final String generateHash(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plaintext.getBytes("UTF-8"));
            return bytesToHexString(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            // can't happen because MD5 is supported by all JVMs
            throw new UnexpectedExceptionException(ex);
        } catch (UnsupportedEncodingException ex) {
            // can't happen because UTF-8 is supported by all JVMs
            throw new UnexpectedExceptionException(ex);
        }
    }

    /**
     * This function transfers an array of bytes to a string of hex numbers and
     * is useful in the hash generation for user's passwords.
     */
    private static final String bytesToHexString(byte[] bytes) {
        String s = "";
        for (int i = 0; i < bytes.length; i ++) {
            if ((bytes[i] < 16) && (bytes[i] >= 0)) {
                s = s + "0" + Integer.toHexString(bytes[i]);
            } else {
                s = s + Integer.toHexString(bytes[i] < 0 ? bytes[i] + 256
                                                         : bytes[i] );
            }
        }
        return s;
    }

    /**
     * Checks whether the <code>UserPreferences</code> have been initialized.
     * This is useful because uninitialized preferences should be initialized
     * to default values, possibly by a call to
     * <code>parsePreferenceString()</code>.
     */
    public static boolean havePreferencesBeenInitialized(
            UserPreferences prefs) {
        return (prefs.preferences % 2 == 1);
    }

    /**
     * Sets the bits associated with the given 'prefType' on the
     * <code>UserPreferences</code> object to the provided value.
     *
     * @param  prefType the <code>Pref</code> indicating the preference type
     *         to be modified
     * @param  prefs the UserPreferences for which preference data is
     *         to be modified
     * @param  value the value to set in the specified preference bitfield;
     *         the <code>width</code> lowest-order bits are used
     *
     * @throws IllegalArgumentException if <code>value</code> is out of
     *         range
     */
    public static void setPreference(Pref prefType, UserPreferences prefs,
                              int value) {
        /*
         * Note: will throw an IndexOutOfBoundsException if mask is not a
         * valid value (0 - 9, currently).  This situation is not
         * anticipated to actually ever occur because this method is not
         * exposed outside this class.
         */
            
        int mask = (1 << prefType.width) - 1;

        if ((value & mask) != value) {
            throw new IllegalArgumentException("Preference value " + value
                     + "out of range for a " + prefType.width + "-bit field");
        }
            
        long bits = (((long) value) << prefType.shift);
        long reverseMask = ~(((long) (0xffffffff & mask)) << prefType.shift);
            
        prefs.preferences &= reverseMask;
        prefs.preferences |= bits;
    }

    /** Sets a boolean preference.  */
    public static void setPreference(Pref prefType, UserPreferences prefs,
                              boolean value) {
        setPreference(prefType, prefs, value ? 1 : 0);
    }

    /** Sets the applet preference. */
    public static void setAppletPreference(UserPreferences prefs,
                                     AppletPref appletPref) {
        setPreference(Pref.APPLET, prefs,  appletPref.code);
    }

    /** Sets the sample page view preference. */
    public static void setSampleViewPreference(UserPreferences prefs,
                              SampleViewPref sampleViewPref) {
        setPreference(Pref.SAMPLE_VIEW, prefs,  sampleViewPref.code);
    }

    /** Sets the show sample view preference. */
    public static void setShowsampleViewPreference(UserPreferences prefs,
                              ShowsampleViewPref showsampleViewPref) {
        setPreference(Pref.SHOWSAMPLE_VIEW, prefs, showsampleViewPref.code);
    }

    /** Sets the upload mechanism preference. */
    public static void setUploadMechanismPreference(UserPreferences prefs,
            UploadMechanismPref uploadMechPref) {
        setPreference(Pref.FILE_UPLOAD_MECHANISM, prefs, uploadMechPref.code);
    }

    /**
     * Extracts a bitfield of the specified preference from the provided
     * <code>UserPreferences</code>, and returns it as an int
     *
     * @param  prefType the <code>Pref</code> indicating the preference type
     *         to be extracted
     * @param  prefs the UserPreferences from which to extract preference data
     * @return the value of the bitfield (with its least significant bit at
     *         bit 0)
     */
    public static int getPreference(Pref prefType, UserPreferences prefs) {
        
        /*
         * Note: will throw an IndexOutOfBoundsException if mask is not a
         * valid value (0 - 9, currently).  This situation is not
         * anticipated to actually ever occur because this method is not
         * exposed outside this class.
         */

        return (((int) (prefs.preferences >> prefType.shift))
                & ((1 << prefType.width) - 1));
        }

    /**
     * Gets a boolean preference.
     * @throws IllegalArgumentException if the 'prefType' is not a boolean
     *     preference type
     */
    public static boolean getPreferenceAsBoolean(Pref prefType,
                                          UserPreferences prefs) {
        if (prefType.width != 1) {
            throw new IllegalArgumentException();
        }
        return (getPreference(prefType, prefs) != 0);
    }

    /** Gets the applet preference. */
    public static AppletPref getAppletPreference(UserPreferences prefs) {
        int prefCode = getPreference(Pref.APPLET, prefs);
        for (AppletPref appletPref: AppletPref.values()) {
            if (appletPref.code == prefCode) {
                return appletPref;
            }
        }
        throw new IllegalStateException();
    }

    /** Gets the sample view preference. */
    public static SampleViewPref getSampleViewPreference(
            UserPreferences prefs) {
        int prefCode = getPreference(Pref.SAMPLE_VIEW, prefs);
        for (SampleViewPref svPref : SampleViewPref.values()) {
            if (svPref.code == prefCode) {
                return svPref;
            }
        }
        throw new IllegalStateException();
    }

    /** Gets the showsample view preference. */
    public static ShowsampleViewPref getShowsampleViewPreference(
            UserPreferences prefs) {
        int prefCode = getPreference(Pref.SHOWSAMPLE_VIEW, prefs);
        for (ShowsampleViewPref svPref : ShowsampleViewPref.values()) {
            if (svPref.code == prefCode) {
                return svPref;
            }
        }
        throw new IllegalStateException();
    }

    /** Gets the upload mechanism preference. */
    public static UploadMechanismPref getUploadMechanismPreference(
            UserPreferences prefs) {
        int prefCode = getPreference(Pref.FILE_UPLOAD_MECHANISM, prefs);
        for (UploadMechanismPref umPref : UploadMechanismPref.values()) {
            if (umPref.code == prefCode) {
                return umPref;
            }
        }
        throw new IllegalStateException();
    }

    /**
     * A helper method that creates a fully initialized
     * <code>UserPreference</code> object from a specifically formatted
     * <code>String</code>.  The string represents each of the individual
     * preference values separated by commas in the order listed in this
     * class' javadocs.  Whitespace is ignored before and after commas.
     * Decimal integer values are valid for all fields, but for some fields,
     * more intuative values are parsible.  Single bit fields that represent a
     * boolean are parsed so that '0' and 'false' result in the bit being set
     * to zero and all other values result in it being set to one.  The first
     * preference may also be "minijamm", "jamm1" or "jamm2" as well as their
     * decimal constant values.  The second preference may be "showsample" or
     * "sample" as well as their decimal constant values.  The preference
     * represeting the preferred showsample page view may be either "basic" or
     * "detailed" or the corresponding integer constants.  The preference 
     * repferesnting the preferred file upload mechanism may be either
     * "form-based" or "drag-and-drop" or the corresponding integer constants.
     * @param prefString a formatted string indicating preference values
     *     separated by commas
     * @return a UserPrefernces object containing all the preferences from
     *      the 'prefString'
     * @throws IllegalArgumentException if 'prefString' cannot be parsed
     */
    public static UserPreferences parsePreferenceString(String prefString) {
        StringTokenizer st = new StringTokenizer(prefString, ",");
        UserPreferences prefs = new UserPreferences();

        // the following line sets the first bit to one indicating that the
        // preferences have been initialized
        prefs.preferences |= 1;

        try {
            String cur = st.nextToken().trim().toLowerCase();
            // parse Applet preference
            if (cur.equals("minijamm")
                    || cur.equals(String.valueOf(AppletPref.MINIJAMM.code))) {
                setAppletPreference(prefs, AppletPref.MINIJAMM);
            } else if (cur.equals("jamm1")
                    || cur.equals(String.valueOf(AppletPref.JAMM1.code))) {
                setAppletPreference(prefs, AppletPref.JAMM1);
            } else if (cur.equals("jamm2")
                    || cur.equals(String.valueOf(AppletPref.JAMM2.code))) {
                setAppletPreference(prefs, AppletPref.JAMM2);
            }

            // parse preferred sample view
            cur = st.nextToken().trim().toLowerCase();
            if (cur.equals(String.valueOf(SampleViewPref.SHOWSAMPLE.code))
                    || cur.equals("showsample")) {
                setSampleViewPreference(prefs, SampleViewPref.SHOWSAMPLE);
            } else if (cur.equals(String.valueOf(SampleViewPref.SAMPLE.code))
                    || cur.equals("sample")) {
                setSampleViewPreference(prefs, SampleViewPref.SAMPLE);
            }

            // parse showsample mode
            cur = st.nextToken().trim().toLowerCase();
            if (cur.equals(String.valueOf(ShowsampleViewPref.BASIC.code))
                    || cur.equals("basic")) {
                setShowsampleViewPreference(prefs, ShowsampleViewPref.BASIC);
            } else if (cur.equals(String.valueOf(ShowsampleViewPref.DETAILED))
                    || cur.equals("detailed")) {
                setShowsampleViewPreference(prefs,
                        ShowsampleViewPref.DETAILED);
            }

            //parse days included in summary
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUMMARY_DAYS, prefs, Integer.parseInt(cur));

            //parse samples included in summary
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUMMARY_SAMPLES, prefs, Integer.parseInt(cur));

            // parse suppress blank fields
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_BLANK, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress comments
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_COMMENT, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress empty file boxes
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_EMPTY_FILE, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress empty correction boxes
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_EMPTY_CORRECTION, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress other empty boxes
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_EMPTY_OTHER, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress actions skipped by reversion
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_SKIPPED, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress file descriptions
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_DESCRIPTIONS, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress blank file listings
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_BLANK_FILE_LISTINGS, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse suppress suppression box 
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SUPPRESS_SUPPRESSION, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse default file overwrite behavior
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.DEFAULT_FILE_OVERWRITE, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse upload mechanism
            cur = st.nextToken().trim().toLowerCase();
            if (cur.equals(String.valueOf(UploadMechanismPref.FORM_BASED.code))
                    || cur.equals("form-based")) {
                setUploadMechanismPreference(prefs,
                        UploadMechanismPref.FORM_BASED);
            } else if (cur.equals(String.valueOf(
                            UploadMechanismPref.DRAG_AND_DROP.code))
                    || cur.equals("drag-and-drop")) {
                setUploadMechanismPreference(prefs,
                        UploadMechanismPref.DRAG_AND_DROP);
            }

            // parse restrict searches to non-retracted samples 
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SEARCH_NON_RETRACTED, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse restrict searches to finished samples 
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SEARCH_FINISHED, prefs,
                !(cur.equals("0") || cur.equals("false")));

            // parse restrict searches to local samples 
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SEARCH_LOCAL, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse restrict searches to searcher's provider 
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.SEARCH_MY_PROVIDER, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse allow implicit preference changes
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.ALLOW_IMPLICIT_PREF_CHANGES, prefs,
                    !(cur.equals("0") || cur.equals("false")));

            // parse validate space group
            cur = st.nextToken().trim().toLowerCase();
            setPreference(Pref.VALIDATE_SPACE_GROUP, prefs,
                    !(cur.equals("0") || cur.equals("false")));

        } catch (NoSuchElementException ex) {
            throw new IllegalStateException();
        } catch (NullPointerException npe) {
            // a token was null, therefore there weren't enough parameters
            throw new IllegalStateException();
        }
        return prefs;
    }


    /**
     * An enumeration of user preference types with methods to affect the
     * settings on a {@link org.recipnet.site.shared.UserPreferences
     * UserPreferences} object.  The internal representation of the preferences
     * are encapulated by this enumeration and are as follows:<p>
     *
     * Bit Mappings:
     * <table border="5" cellspacing="2" cellpadding="4">
     *   <tr>
     *     <th>Use</th>
     *     <th>Bit Index</th>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">RESERVED to indicate whether preferences have been
     *     initialized:</td>
     *     <td>0</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="2">Applet preference:</td>
     *     <td>1</td>
     *   </tr>
     *   <tr>
     *     <td>2</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Preferred sample view:</td>
     *     <td>3</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Showsample page mode:</td>
     *     <td>4</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="9">Days included in summary:</td>
     *     <td>5</td>
     *   </tr>
     *   <tr>
     *     <td>6</td>
     *   </tr>
     *   <tr>
     *     <td>7</td>
     *   </tr>
     *   <tr>
     *     <td>8</td>
     *   </tr>
     *   <tr>
     *     <td>9</td>
     *   </tr>
     *   <tr>
     *     <td>10</td>
     *   </tr>
     *   <tr>
     *     <td>11</td>
     *   </tr>
     *   <tr>
     *     <td>12</td>
     *   </tr>
     *   <tr>
     *     <td>13</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="8">Samples included in summary:</td>
     *     <td>14</td>
     *   </tr>
     *   <tr>
     *     <td>15</td>
     *   </tr>
     *   <tr>
     *     <td>16</td>
     *   </tr>
     *   <tr>
     *     <td>17</td>
     *   </tr>
     *   <tr>
     *     <td>18</td>
     *   </tr>
     *   <tr>
     *     <td>19</td>
     *   </tr>
     *   <tr>
     *     <td>20</td>
     *   </tr>
     *   <tr>
     *     <td>21</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress blank fields:</td>
     *     <td>22</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress comments:</td>
     *     <td>23</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress empty file actions:</td>
     *     <td>24</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress empty correction actions:</td>
     *     <td>25</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress other empty actions:</td>
     *     <td>26</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress actions skipped by reversion:</td>
     *     <td>27</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress file descriptions:</td>
     *     <td>28</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">
     *       Suppress file lists for actions during which no files were
     *       modified:
     *     </td>
     *     <td>29</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Suppress suppression box:</td>
     *     <td>30</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Overwrite existing files by default:</td>
     *     <td>31</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">File upload mechanism:</td>
     *     <td>32</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Restrict search to non-retracted:</td>
     *     <td>33</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Restrict search to finished:</td>
     *     <td>34</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Restrict search to local:</td>
     *     <td>35</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Restrict search to searcher's provider:</td>
     *     <td>36</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Allow implicit preference changes:</td>
     *     <td>37</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="1">Validate space group:</td>
     *     <td>38</td>
     *   </tr>
     *   <tr>
     *     <td rowspan="3">Currently unused:</td>
     *     <td>39</td>
     *   </tr>
     *   <tr>
     *     <td>.<br/>.<br />.
     *   </tr>
     *   <tr>
     *     <td>63</td>
     *   </tr>
     * </table>
     */
    public static enum Pref {
        APPLET                          (2, null),
        SAMPLE_VIEW                     (1, APPLET),
        SHOWSAMPLE_VIEW                 (1, SAMPLE_VIEW),
        SUMMARY_DAYS                    (9, SHOWSAMPLE_VIEW),
        SUMMARY_SAMPLES                 (8, SUMMARY_DAYS),
        SUPPRESS_BLANK                  (1, SUMMARY_SAMPLES),
        SUPPRESS_COMMENT                (1, SUPPRESS_BLANK),
        SUPPRESS_EMPTY_FILE             (1, SUPPRESS_COMMENT),
        SUPPRESS_EMPTY_CORRECTION       (1, SUPPRESS_EMPTY_FILE),
        SUPPRESS_EMPTY_OTHER            (1, SUPPRESS_EMPTY_CORRECTION),
        SUPPRESS_SKIPPED                (1, SUPPRESS_EMPTY_OTHER),
        SUPPRESS_DESCRIPTIONS           (1, SUPPRESS_SKIPPED),
        SUPPRESS_BLANK_FILE_LISTINGS    (1, SUPPRESS_DESCRIPTIONS),
        SUPPRESS_SUPPRESSION            (1, SUPPRESS_BLANK_FILE_LISTINGS),
        DEFAULT_FILE_OVERWRITE          (1, SUPPRESS_SUPPRESSION),
        FILE_UPLOAD_MECHANISM           (1, DEFAULT_FILE_OVERWRITE),
        SEARCH_NON_RETRACTED            (1, FILE_UPLOAD_MECHANISM),
        SEARCH_FINISHED                 (1, SEARCH_NON_RETRACTED),
        SEARCH_LOCAL                    (1, SEARCH_FINISHED),
        SEARCH_MY_PROVIDER              (1, SEARCH_LOCAL),
        ALLOW_IMPLICIT_PREF_CHANGES     (1, SEARCH_MY_PROVIDER),
        VALIDATE_SPACE_GROUP            (1, ALLOW_IMPLICIT_PREF_CHANGES);

        /** the number of bits needed to represent this preference type */
        final int width;

        /** the offset at which this preference type's bits are stored */
        final int shift;

        /**
         * Initializes a Pref.  The offset is calculated to reflect the size
         * of all bits used by preceeding preferences plus the single reserved
         * bit (which is explicitly handled by the first preference).
         */
        Pref(int width, Pref previous) {
            this.width = width;
            this.shift = (previous == null
                    ? 1 : previous.width + previous.shift);
        }
    }
}

