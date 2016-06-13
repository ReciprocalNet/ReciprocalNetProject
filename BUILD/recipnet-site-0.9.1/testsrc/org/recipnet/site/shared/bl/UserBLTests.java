/*
 * IUMSC Reciprocal Net Project
 *
 * UserBLTests.java
 */

package org.recipnet.site.shared.bl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.db.UserInfo;

public class UserBLTests extends TestCase {

    private final static Class<UserBL> testClass = UserBL.class;
    private final static List<BasePreferenceTester> testerList;

    static {
        List<BasePreferenceTester> tList
                = new ArrayList<BasePreferenceTester>();

        try {
            tList.add(new EnumerationPreferenceTester(
                    "applet", UserBL.Pref.APPLET, UserBL.AppletPref.class));
        } catch (Exception e) {
            System.err.println(
                    "Exception while initializing applet preference tests: "
                    + e);
        }
        try {
            tList.add(new EnumerationPreferenceTester(
                    "sampleView", UserBL.Pref.SAMPLE_VIEW,
                    UserBL.SampleViewPref.class));
        } catch (Exception e) {
            System.err.println(
                    "Exception while initializing sample view preference tests: "
                    + e);
        }
        try {
            tList.add(new EnumerationPreferenceTester(
                    "showsampleView", UserBL.Pref.SHOWSAMPLE_VIEW,
                    UserBL.ShowsampleViewPref.class));
        } catch (Exception e) {
            System.err.println(
                    "Exception while initializing showsample view preference tests: "
                    + e);
        }
        try {
            tList.add(new EnumerationPreferenceTester(
                    "uploadMechanism", UserBL.Pref.FILE_UPLOAD_MECHANISM,
                    UserBL.UploadMechanismPref.class));
        } catch (Exception e) {
            System.err.println(
                    "Exception while initializing upload mechanism preference tests: "
                    + e);
        }
        tList.add(new BasePreferenceTester(
                "summaryDays", UserBL.Pref.SUMMARY_DAYS));
        tList.add(new BasePreferenceTester(
                "summarySamples", UserBL.Pref.SUMMARY_SAMPLES));
        tList.add(new BooleanPreferenceTester("suppressBlankFields",
                UserBL.Pref.SUPPRESS_BLANK));
        tList.add(new BooleanPreferenceTester("suppressComments",
                UserBL.Pref.SUPPRESS_COMMENT));
        tList.add(new BooleanPreferenceTester("suppressEmptyFileBoxes",
                UserBL.Pref.SUPPRESS_EMPTY_FILE));
        tList.add(new BooleanPreferenceTester("suppressEmptyCorrectionBoxes",
                UserBL.Pref.SUPPRESS_EMPTY_CORRECTION));
        tList.add(new BooleanPreferenceTester("suppressEmptyOtherBoxes",
                UserBL.Pref.SUPPRESS_EMPTY_OTHER));
        tList.add(new BooleanPreferenceTester("suppressDescriptions",
                UserBL.Pref.SUPPRESS_DESCRIPTIONS));
        tList.add(new BooleanPreferenceTester("suppressBlankFileListings",
                UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS));
        tList.add(new BooleanPreferenceTester("overwriteFiles",
                UserBL.Pref.DEFAULT_FILE_OVERWRITE));
        tList.add(new BooleanPreferenceTester("searchNonRetracted",
                UserBL.Pref.SEARCH_NON_RETRACTED));
        tList.add(new BooleanPreferenceTester("searchFinished",
                UserBL.Pref.SEARCH_FINISHED));
        tList.add(new BooleanPreferenceTester("searchLocal",
                UserBL.Pref.SEARCH_LOCAL));
        tList.add(new BooleanPreferenceTester("searchMyProvider",
                UserBL.Pref.SEARCH_MY_PROVIDER));
        tList.add(new BooleanPreferenceTester("suppressSuppressionOptions",
                UserBL.Pref.SUPPRESS_SUPPRESSION));
        tList.add(new BooleanPreferenceTester("allowImplicitPrefChanges",
                UserBL.Pref.ALLOW_IMPLICIT_PREF_CHANGES));
        tList.add(new BooleanPreferenceTester("validateSpaceGroup",
                UserBL.Pref.VALIDATE_SPACE_GROUP));

        testerList = Collections.unmodifiableList(tList);
    }

    private static class BasePreferenceTester {
        final String prefName;
        final int fullValue;
        final UserBL.Pref preference;

        BasePreferenceTester(String pName, UserBL.Pref pref) {
            int fullMask = 0;

            for (int width = pref.width; width > 0; width--) {
                fullMask = (fullMask << 1) | 0x1;
            }
            fullValue = fullMask;
            prefName = pName;
            preference = pref;
        }

        int getFullValue() {
            return fullValue;
        }
        
        void set(UserInfo user, int prefValue) {
            UserBL.setPreference(preference, user.preferences, prefValue);
        }

        int get(UserInfo user) {
            return UserBL.getPreference(preference, user.preferences);
        }

        void fillBitfield(UserInfo user) {
            set(user, getFullValue());
        }

        void clearBitfield(UserInfo user) {
            set(user, 0);
        }

        boolean isBitfieldFilled(UserInfo user) {
            return get(user) == getFullValue();
        }

        boolean isBitfieldClear(UserInfo user) {
            return get(user) == 0;
        }

        void testGetterSetter(UserInfo user) {
            int bound = getFullValue();
            for (int i = 0; i <= bound; i++) {
                set(user, i);
                assertEquals(
                        "Bitfield not successfully filled / read back",
                        i, get(user));
            }
        }

        void testOverlap(UserInfo user) {
            fillBitfield(user);
            for (BasePreferenceTester tester : testerList) {

                if (tester == this) {
                    continue;
                }
                tester.clearBitfield(user);
            }
            assertTrue("Bitfield not successfully filled / read back",
                       isBitfieldFilled(user));
            clearBitfield(user);
            for (BasePreferenceTester tester : testerList) {

                if (tester == this) {
                    continue;
                }
                tester.fillBitfield(user);
            }
            assertTrue("Bitfield not successfully cleared / read back",
                       isBitfieldClear(user));
        }

        public void testOverflowArgument(UserInfo user) {
            try {
                set(user, getFullValue() << 1);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue(
                        "Wrong exception: expected IllegalArgumentException;"
                                + " caught " + e.getClass().getName(),
                        e instanceof IllegalArgumentException);
            }
            try {
                set(user, -1);
                fail("Expected an IllegalArgumentException");
            } catch (Exception e) {
                assertTrue(
                        "Wrong exception: expected IllegalArgumentException;"
                                + " caught " + e.getClass().getName(),
                        e instanceof IllegalArgumentException);
            }
        }

        Test createTestSuite(final UserInfo user) {
            TestSuite suite = new TestSuite();

            suite.addTest(new TestCase("test Getter / Setter: " + prefName) {
                @Override
                public void runTest() {
                    testGetterSetter(user);
                }
            });
            suite.addTest(new TestCase("test Overlap: " + prefName) {
                @Override
                public void runTest() {
                    testOverlap(user);
                }
            });
            suite.addTest(new TestCase("test overflow value: " + prefName) {
                @Override
                public void runTest() {
                    testOverflowArgument(user);
                }
            });
            
            return suite;
        }
    }

    public static class BooleanPreferenceTester
            extends BasePreferenceTester {

        BooleanPreferenceTester(String prefName, UserBL.Pref pref) {
            super(prefName, pref);
        }

        void testBooleanGetterSetter(UserInfo user) {
            UserBL.setPreference(preference, user.preferences, false);
            assertTrue("Bitfield not successfully filled / read back",
                       !UserBL.getPreferenceAsBoolean(
                               preference, user.preferences));
            UserBL.setPreference(preference, user.preferences, true);
            assertTrue("Bitfield not successfully filled / read back",
                       UserBL.getPreferenceAsBoolean(
                               preference, user.preferences));
            UserBL.setPreference(preference, user.preferences, false);
            assertTrue("Bitfield not successfully filled / read back",
                       !UserBL.getPreferenceAsBoolean(
                               preference, user.preferences));
        }

        @Override
        Test createTestSuite(final UserInfo user) {
            TestSuite ts = new TestSuite();
            
            ts.addTest(super.createTestSuite(user));
            ts.addTest(new TestCase("test boolean Getter / Setter: " + prefName) {
                @Override
                public void runTest() {
                    testGetterSetter(user);
                }
            });
            
            return ts;
        }
    }

    public static class EnumerationPreferenceTester
            extends BasePreferenceTester {

        final List<Enum<?>> values;
        final Class<? extends Enum<?>> enumType;
        final Method getter;
        final Method setter;
        
        /**
         * Initializes a new {@code EnumerationPreferenceTester} with the specified parameters
         *
         * @param prefName the name of the preference
         * @param enumType the {@code Class} representing the type of the
         *        enumeration values
         */
        public EnumerationPreferenceTester(String prefName,
                UserBL.Pref preferrence,
                Class<? extends Enum<?>> enumType) throws Exception {
            super(prefName, preferrence);
            
            Method valuesMethod = enumType.getMethod("values", new Class[0]);
            
            values = Arrays.asList(
                    (Enum<?>[]) valuesMethod.invoke(null, new Object[0]));
            getter = findGetter(prefName);
            setter = findSetter(prefName, enumType);
            this.enumType = enumType;
        }

        private static Method findGetter(String prefName) {
            try {
                return UserBLTests.testClass.getMethod(
                        "get" + prefName.substring(0, 1).toUpperCase()
                        + prefName.substring(1) + "Preference",
                        new Class[] { UserPreferences.class });
            } catch (NoSuchMethodException nsme) {
                fail(nsme.getMessage());
                return null;
            }
        }

        private static Method findSetter(String prefName, Class<?> type) {
            try {
                return UserBLTests.testClass.getMethod(
                        "set" + prefName.substring(0, 1).toUpperCase()
                        + prefName.substring(1) + "Preference",
                        new Class[] { UserPreferences.class, type });
            } catch (NoSuchMethodException nsme) {
                fail(nsme.getMessage());
                return null;
            }
        }

        Enum<?> getEnum(UserInfo user)
                throws InvocationTargetException, IllegalAccessException {
            return enumType.cast(
                    getter.invoke(null, new Object[] {user.preferences}));
        }
        
        void setEnum(UserInfo user, Enum<?> value)
                throws InvocationTargetException, IllegalAccessException {
            setter.invoke(null, new Object[] { user.preferences, value });
        }
        
        void testEnumGetterSetter(UserInfo user)
                throws InvocationTargetException, IllegalAccessException {
            for (Enum<?> value : values) {
                setEnum(user, value);
                assertEquals("Bitfield not successfully filled / read back",
                        value, getEnum(user));
            }
        }

        void testGetterIllegalCode(UserInfo user) {
            if (values.size() <= fullValue) {
                /*
                 * This test assumes that the preference codes for this
                 * preference are assigned in ascending order, starting at 0,
                 * with no gaps.  Thus, if there are any illegal codes then the
                 * one that has all bits in the bitfield on will be one of them. 
                 */
                set(user, fullValue);
                
                try {
                    getEnum(user);
                } catch (InvocationTargetException ite) {
                    Throwable e = ite.getTargetException();

                    assertTrue("expected IllegalStateException, caught "
                               + e.getClass().getName(),
                               e instanceof IllegalStateException);
                } catch (Exception e) {
                    fail("expected IllegalStateException, caught "
                         + e.getClass().getName());
                }
            } // otherwise there are no illegal codes for this preference
        }
        
        void testGetterNullPreferences() {
            try {
                getter.invoke(null, new Object[] { null });
                fail("Expected a NullPointerException");
            } catch (InvocationTargetException ite) {
                Throwable e = ite.getTargetException();

                assertTrue("expected NullPointerException, caught "
                           + e.getClass().getName(),
                           e instanceof NullPointerException);
            } catch (Exception e) {
                fail("expected NullPointerException, caught "
                     + e.getClass().getName());
            }
        }

        void testSetterNullPreferences() {
            try {
                setter.invoke(null, new Object[] { null, values.get(0) });
                fail("Expected a NullPointerException");
            } catch (InvocationTargetException ite) {
                Throwable e = ite.getTargetException();

                assertTrue("expected NullPointerException, caught "
                           + e.getClass().getName(),
                           e instanceof NullPointerException);
            } catch (Exception e) {
                fail("expected NullPointerException, caught "
                     + e.getClass().getName());
            }
        }

        void testSetterNullPreference(UserInfo user) {
            try {
                setEnum(user, null);
                fail("Expected a NullPointerException");
            } catch (InvocationTargetException ite) {
                Throwable e = ite.getTargetException();

                assertTrue("expected NullPointerException, caught "
                           + e.getClass().getName(),
                           e instanceof NullPointerException);
            } catch (Exception e) {
                fail("expected NullPointerException, caught "
                     + e.getClass().getName());
            }
        }

        @Override
        Test createTestSuite(final UserInfo user) {
            TestSuite ts = new TestSuite();

            ts.addTest(super.createTestSuite(user));
            ts.addTest(new TestCase("test enum Getter / Setter: " + prefName) {
                @Override
                public void runTest()
                        throws InvocationTargetException, IllegalAccessException {
                    testEnumGetterSetter(user);
                }
            });
            ts.addTest(new TestCase("test enum Getter with illegal preference code: "
                    + prefName) {
                @Override
                public void runTest() {
                    testGetterIllegalCode(user);
                }
            });
            ts.addTest(new TestCase("test Getter with null preferences: "
                    + prefName) {
                @Override
                public void runTest() {
                    testGetterNullPreferences();
                }
            });
            ts.addTest(new TestCase("test Setter with null preferences: "
                    + prefName) {
                @Override
                public void runTest() {
                    testSetterNullPreferences();
                }
            });
            ts.addTest(new TestCase("test Setter with null preference object: "
                    + prefName) {
                @Override
                public void runTest() {
                    testSetterNullPreference(user);
                }
            });

            return ts;
        }
    }
            
    public UserBLTests(String testName) {
        super(testName);
    }
    
    public void testDeactivateUser() {
        UserInfo user = new UserInfo();
        
        user.isActive = true;
        UserBL.deactivateUser(user);
        assertFalse("User's isActive flag was not lowered", user.isActive);
        assertNotNull("User's inactivity date was not set", user.inactiveDate);
        assertTrue("User's inactivity date was set in the future",
                user.inactiveDate.compareTo(new Date()) <= 0);
    }
    
    public void testDeactivateNullUser() {
        try {
            UserBL.deactivateUser(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // the expected result
        } catch (Exception e) {
            fail("Expected a NullPointerException, got a "
                    + e.getClass().getName());
        }
    }
    
    public void testSetCheckPassword() {
        UserInfo user = new UserInfo();
        String[] passwords = new String[] {"password", "pa$$W0rD", "pass WORD",
                "", "\u03c0as\u03c3word", "pass\u03c9\u235frd", "passw\u0000rd"}; 
        
        // The initial password is null; this value should be handled without
        // exception
        for (String s : passwords) {
            assertFalse("Password check failed for null password and test string = '"
                    + s + "'", UserBL.checkPassword(user, s));
        }
        
        for (String password : passwords) {
            UserBL.setPassword(user, password);
            for (String s : passwords) {
                assertFalse("Password check failed for password='" + password
                        + "' and test string = '" + s + "'",
                        UserBL.checkPassword(user, s) ^ password.equals(s));
            }
        }
    }
    
    public void testSetNullPassword() {
        try {
            UserBL.setPassword(new UserInfo(), null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // the expected result
        } catch (Exception e) {
            fail("Expected a NullPointerException, got a "
                    + e.getClass().getName());
        }
    }
    
    public void testCheckNullPassword() {
        UserInfo user = new UserInfo();
        
        // The password is initially null, but should not match null
        assertFalse("Null passwords were found to match",
                UserBL.checkPassword(user, null));
        
        // A non-null password should not match null either
        UserBL.setPassword(user, "password");
        assertFalse("A null password matched a non-null one",
                UserBL.checkPassword(user, null));
    }
    
    public void testHavePreferencesBeenInitialized() {
        assertFalse("Fresh UserPreferences are found to be initialized",
                UserBL.havePreferencesBeenInitialized(new UserPreferences()));
    }

    public void testHavePreferencesBeenInitialized__null() {
        try {
            UserBL.havePreferencesBeenInitialized(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException e) {
            // the expected result
        } catch (Exception e) {
            fail("Expected a NullPointerException, got a "
                    + e.getClass().getName());
        }
    }
    
    /*
     * The master method by which all the test cases represented / created by
     * this class are accessed.
     */
    public static Test suite() {
        TestSuite ts = new TestSuite(UserBLTests.class);

        for (BasePreferenceTester tester : testerList) {

            ts.addTest(tester.createTestSuite(new UserInfo()));
        }

        return ts;
    }

}

