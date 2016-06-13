/*
 * Reciprocal Net project
 * 
 * PreferenceField.java
 *
 * 18-May-2005: midurbin wrote first draft
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 19-Aug-2005: midurbin added support for new VALIDATE_SPACE_GROUP preference
 * 28-Oct-2005: midurbin added support for various new preference options,
 *              added an ErrorSupplier implementation and fixed the behavior of
 *              this tag in the case where it relates to the current user
 * 03-Apr-2006: jobollin added default labels for the various preferrence types;
 *              reformatted the code
 * 13-Jun-2006: jobollin corrected multiple compiler warnings
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.CheckboxHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.IntegerTextboxHtmlControl;
import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A custom tag that exposes an individual user preference. The value of the
 * preference is exposed through the value of the 'owned' control, which may be
 * a {@code CheckboxHtmlControl}, {@code ListboxHtmlControl},
 * {@code IntegerTextboxHtmlControl} or whichever control is most appropriate.
 * This tag determines which user's preferences to expose by locating the
 * nearest enclosing {@code UserContext}. If the nearest enclosing
 * {@code UserContext} provides the {@code UserInfo} for the currently logged-in
 * user, the preferences associated with the current session will overrule the
 * preferences in the fetched {@code UserInfo}.
 */
public class PreferenceField extends HtmlPageElement implements ErrorSupplier {

    /**
     * An error flag corresponding to one possible on an 'owned'
     * {@code IntegerTextboxHtmlControl} when a non-number is entered.
     */
    public static final int VALUE_IS_NOT_A_NUMBER = 1;

    /**
     * An error flag corresponding to one possible on an 'owned'
     * {@code IntegerTextboxHtmlControl} when the entered number is too high.
     */
    public static final int VALUE_IS_TOO_HIGH = 1 << 1;

    /**
     * An error flag corresponding to one possible on an 'owned'
     * {@code IntegerTextboxHtmlControl} when the entered number is too low.
     */
    public static final int VALUE_IS_TOO_LOW = 1 << 2;
    
    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return VALUE_IS_TOO_LOW;
    }

    /** The {@code UserContext} in which this tag must be nested. */
    private UserContext userContext;

    /** The 'owned' control. */
    private HtmlControl control;

    /**
     * A required property that must be set to one of the preference type
     * constant values.
     */
    private PrefType preferenceType;

    /** used to implement {@code ErrorSupplier} */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.userContext = null;
        this.control = null;
        this.preferenceType = PrefType.INVALID;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * @param prefType the PrefType that this {@code PreferenceField} should
     *        expose
     */
    public void setPreferenceType(PrefType prefType) {
        this.preferenceType = prefType;
    }

    /**
     * @return the PrefType that this {@code PreferenceField} should expose
     */
    public PrefType getPreferenceType() {
        return this.preferenceType;
    }

    /**
     * Implements {@code ErrorSupplier}.
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     */
    public int getErrorCode() {
        int compiledCode = this.errorCode;
        if (this.control instanceof IntegerTextboxHtmlControl) {
            int ownedErrorCode = this.control.getErrorCode();

            if ((ownedErrorCode
                    & IntegerTextboxHtmlControl.VALUE_IS_NOT_A_NUMBER) != 0) {
                compiledCode |= VALUE_IS_NOT_A_NUMBER;
            }
            if ((ownedErrorCode
                    & IntegerTextboxHtmlControl.VALUE_IS_TOO_HIGH) != 0) {
                compiledCode |= VALUE_IS_TOO_HIGH;
            }
            if ((ownedErrorCode
                    & IntegerTextboxHtmlControl.VALUE_IS_TOO_LOW) != 0) {
                compiledCode |= VALUE_IS_TOO_LOW;
            }
        }
        return compiledCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * {@inheritDoc}; this version looks up the applicable {@code UserContext}
     * and initializes the 'owned' control to represent the {@code HtmlControl}
     * subclass most suited for the 'preferenceType'.
     * 
     * @throws IllegalStateException if a required context isn't found
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        if (this.userContext == null) {
            throw new IllegalStateException();
        }
        this.control = this.preferenceType.generateControl();
        this.registerOwnedElement(this.control);
        this.preferenceType.initializeControl(this.control);

        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the 'owned' control's value from the
     * {@code UserContext}'s {@code UserInfo}'s preferences.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        if (this.userContext.getUserInfo().id
                == ((UserInfo) pageContext.getSession().getAttribute(
                        "userInfo")).id) {
            /*
             * If the user in question is the currently logged in user, use the
             * preferences for the session as they may be different from those
             * in the recently fetched UserInfo.
             */
            UserPreferences prefs
                    = (UserPreferences) pageContext.getSession().getAttribute(
                            "preferences");
            this.preferenceType.updateControl(this.control, prefs);
        } else {
            /*
             * Otherwise, simply use the preferences associated with the
             * arbitrary user.
             */
            UserPreferences prefs = this.userContext.getUserInfo().preferences;
            this.preferenceType.updateControl(this.control, prefs);
        }
        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the {@code UserInfo} object with the
     * current value of the 'owned' control if no validation error occurred.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);
        // don't waste time updating the container if the value is invalid
        if (!control.getFailedValidation()) {
            UserPreferences prefs = this.userContext.getUserInfo().preferences;
            this.preferenceType.updatePrefs(this.control, prefs);
        }
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        PreferenceField dc = (PreferenceField) super.generateCopy(newId, map);
        dc.control = (HtmlControl) map.get(this.control);
        dc.userContext = (UserContext) map.get(this.userContext);
        return dc;
    }

    /** Enumeration that describes the various preference types. */
    public static enum PrefType {
        
        /**
         * A {@code PrefType} representing an invalid preference
         */
        INVALID(null) {
            @Override
            HtmlControl generateControl() {
                throw new IllegalArgumentException();
            }

            @Override
            void initializeControl(@SuppressWarnings("unused")
            HtmlControl control) {
                throw new IllegalArgumentException();
            }

            @Override
            void updateControl(@SuppressWarnings("unused")
            HtmlControl control, @SuppressWarnings("unused")
            UserPreferences prefs) {
                throw new IllegalArgumentException();
            }

            @Override
            void updatePrefs(@SuppressWarnings("unused")
            HtmlControl control, @SuppressWarnings("unused")
            UserPreferences prefs) {
                throw new IllegalArgumentException();
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred visualization applet
         */
        APPLET("JaMM applet") {
            @Override
            HtmlControl generateControl() {
                return new ListboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ListboxHtmlControl listbox = (ListboxHtmlControl) control;
                listbox.addOption(true, "miniJaMM", "miniJaMM");
                listbox.addOption(true, "JaMM1", "JaMM1");
                listbox.addOption(true, "JaMM2", "JaMM2");
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                UserBL.AppletPref appletPref = UserBL.getAppletPreference(prefs);
                if (appletPref == UserBL.AppletPref.MINIJAMM) {
                    control.setValue("miniJaMM",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else if (appletPref == UserBL.AppletPref.JAMM1) {
                    control.setValue("JaMM1",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else if (appletPref == UserBL.AppletPref.JAMM2) {
                    control.setValue("JaMM2",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                }
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue().equals("miniJaMM")) {
                    UserBL.setAppletPreference(prefs,
                            UserBL.AppletPref.MINIJAMM);
                } else if (control.getValue().equals("JaMM1")) {
                    UserBL.setAppletPreference(prefs, UserBL.AppletPref.JAMM1);
                } else if (control.getValue().equals("JaMM2")) {
                    UserBL.setAppletPreference(prefs, UserBL.AppletPref.JAMM2);
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred sample view page
         */
        SAMPLE_VIEW("Sample view") {
            @Override
            HtmlControl generateControl() {
                return new ListboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ((ListboxHtmlControl) control).addOption(true,
                        "Sample details", "showsample");
                ((ListboxHtmlControl) control).addOption(true, "Edit sample",
                        "editsample");
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                if (UserBL.getSampleViewPreference(prefs)
                        == UserBL.SampleViewPref.SHOWSAMPLE) {
                    control.setValue("showsample",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    control.setValue("editsample",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                }
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue().equals("showsample")) {
                    UserBL.setSampleViewPreference(prefs,
                            UserBL.SampleViewPref.SHOWSAMPLE);
                } else if (control.getValue().equals("editsample")) {
                    UserBL.setSampleViewPreference(prefs,
                            UserBL.SampleViewPref.SAMPLE);
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred mode for showsample.jsp
         */
        SHOWSAMPLE_VIEW("ShowSample view") {
            @Override
            HtmlControl generateControl() {
                return new ListboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ((ListboxHtmlControl) control).addOption(true, "basic", "basic");
                ((ListboxHtmlControl) control).addOption(true, "detailed",
                        "detailed");
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                if (UserBL.getShowsampleViewPreference(prefs)
                        == UserBL.ShowsampleViewPref.BASIC) {
                    control.setValue("basic",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    control.setValue("detailed",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                }
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue().equals("basic")) {
                    UserBL.setShowsampleViewPreference(prefs,
                            UserBL.ShowsampleViewPref.BASIC);
                } else if (control.getValue().equals("detailed")) {
                    UserBL.setShowsampleViewPreference(prefs,
                            UserBL.ShowsampleViewPref.DETAILED);
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred number of days to
         * summarize on the laboratory summary
         */
        SUMMARY_DAYS("Summary days") {
            @Override
            HtmlControl generateControl() {
                return new IntegerTextboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ((IntegerTextboxHtmlControl) control).setSize(3);
                ((IntegerTextboxHtmlControl) control).setMaxValue(511);
                ((IntegerTextboxHtmlControl) control).setMinValue(0);
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Integer.valueOf(UserBL.getPreference(
                        UserBL.Pref.SUMMARY_DAYS, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue() != null) {
                    UserBL.setPreference(UserBL.Pref.SUMMARY_DAYS, prefs,
                            control.getValueAsInt());
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred maximum number of
         * samples to summarize on the laboratory summary
         */
        SUMMARY_SAMPLES("Summary samples") {
            @Override
            HtmlControl generateControl() {
                return new IntegerTextboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ((IntegerTextboxHtmlControl) control).setSize(3);
                ((IntegerTextboxHtmlControl) control).setMaxValue(255);
                ((IntegerTextboxHtmlControl) control).setMinValue(1);
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Integer.valueOf(UserBL.getPreference(
                        UserBL.Pref.SUMMARY_SAMPLES, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue() != null) {
                    UserBL.setPreference(UserBL.Pref.SUMMARY_SAMPLES, prefs,
                            control.getValueAsInt());
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of blank
         * fields on sample.jsp
         */
        SUPPRESS_BLANK_FIELD("Blank fields") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_BLANK, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_BLANK, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of comments
         * on sample.jsp
         */
        SUPPRESS_COMMENTS("Comments") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_COMMENT, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_COMMENT, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of empty file
         * actions on sample.jsp
         */
        SUPPRESS_EMPTY_FILE_ACTIONS("Empty file actions") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_EMPTY_FILE, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_EMPTY_FILE, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of empty
         * correction actions on sample.jsp
         */
        SUPPRESS_EMPTY_CORRECTION_ACTIONS("Empty correction actions") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_EMPTY_CORRECTION, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_EMPTY_CORRECTION,
                        prefs, control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of empty
         * non-file, non-correction actions on sample.jsp
         */
        SUPPRESS_EMPTY_OTHER_ACTIONS("Other empty actions") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_EMPTY_OTHER, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_EMPTY_OTHER, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of actions
         * skipped by reversion on sample.jsp
         */
        SUPPRESS_SKIPPED_ACTIONS("Actions skipped by reversion") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_SKIPPED, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_SKIPPED, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of file
         * descriptions on sample.jsp
         */
        SUPPRESS_FILE_DESCRIPTIONS("File descriptions") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_DESCRIPTIONS, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_DESCRIPTIONS, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of blank
         * file listings on sample.jsp
         */
        SUPPRESS_BLANK_FILE_LISTINGS("Blank lists of file names") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_BLANK_FILE_LISTINGS,
                        prefs, control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred handling of
         * suppression option display on sample.jsp
         */
        SUPPRESS_SUPPRESSION("These suppression options") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SUPPRESS_SUPPRESSION, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SUPPRESS_SUPPRESSION, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred default sample
         * overwriting option
         */
        DEFAULT_FILE_OVERWRITE("Default file overwrite") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.DEFAULT_FILE_OVERWRITE, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.DEFAULT_FILE_OVERWRITE, prefs,
                        control.getValueAsBoolean());
            }
        },

        
        /**
         * A {@code PrefType} representing the preferred default file upload
         * mechanism
         */
        FILE_UPLOAD_MECHANISM("File upload mechanism") {
            @Override
            HtmlControl generateControl() {
                return new ListboxHtmlControl();
            }

            @Override
            void initializeControl(HtmlControl control) {
                ((ListboxHtmlControl) control).addOption(true, "form-based",
                        "form-based");
                ((ListboxHtmlControl) control).addOption(true, "drag-and-drop",
                        "drag-and-drop");
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                if (UserBL.getUploadMechanismPreference(prefs)
                        == UserBL.UploadMechanismPref.FORM_BASED) {
                    control.setValue("form-based",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    control.setValue("drag-and-drop",
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                }
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                if (control.getValue().equals("form-based")) {
                    UserBL.setUploadMechanismPreference(prefs,
                            UserBL.UploadMechanismPref.FORM_BASED);
                } else if (control.getValue().equals("drag-and-drop")) {
                    UserBL.setUploadMechanismPreference(prefs,
                            UserBL.UploadMechanismPref.DRAG_AND_DROP);
                }
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred default for whether to
         * restrict searches to non-retracted samples
         */
        SEARCH_NON_RETRACTED("Search non-retracted") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_NON_RETRACTED, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SEARCH_NON_RETRACTED, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred default for whether to
         * restrict searches to finished samples
         */
        SEARCH_FINISHED("Search finished") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_FINISHED, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SEARCH_FINISHED, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred default for whether to
         * restrict searches to local samples
         */
        SEARCH_LOCAL("Search local") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_LOCAL, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SEARCH_LOCAL, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferred default for whether to
         * restrict searches to the user's own provider
         */
        SEARCH_MY_PROVIDER("Search my provider") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_MY_PROVIDER, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.SEARCH_MY_PROVIDER, prefs,
                        control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the preferrence for whether to
         * allow implicit preferrence changes
         */
        ALLOW_IMPLICIT_PREF_CHANGES("Allow implicit preferrence changes") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.ALLOW_IMPLICIT_PREF_CHANGES, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.ALLOW_IMPLICIT_PREF_CHANGES,
                        prefs, control.getValueAsBoolean());
            }
        },
        
        /**
         * A {@code PrefType} representing the user's preference with respect to
         * validating space group symbols (as opposed to not validating them)
         */
        VALIDATE_SPACE_GROUP("Validate space groups") {
            @Override
            HtmlControl generateControl() {
                return new CheckboxHtmlControl();
            }

            @Override
            void updateControl(HtmlControl control, UserPreferences prefs) {
                control.setValue(Boolean.valueOf(UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.VALIDATE_SPACE_GROUP, prefs)),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
            }

            @Override
            void updatePrefs(HtmlControl control, UserPreferences prefs) {
                UserBL.setPreference(UserBL.Pref.VALIDATE_SPACE_GROUP, prefs,
                        control.getValueAsBoolean());
            }
        };

        private final String defaultLabel;

        private PrefType(String label) {
            defaultLabel = label;
        }

        /**
         * Returns the default label configured for this preferrence type
         * 
         * @return the default label string for this preferrence type
         */
        public String getDefaultLabel() {
            return defaultLabel;
        }

        /**
         * Generates an {@code HtmlControl} to contain the value of the
         * preference type indicated by this particular {@code PrefType}.
         * 
         * @return an {@code HtmlControl} appropriate for presenting and
         *         accepting changes to a preference of this type
         */
        abstract HtmlControl generateControl();

        /**
         * Sets the initial property values of the provided control.
         * 
         * @param control a {@code HtmlControl} returned by
         *        {@code generateControl()} that has been registered as an
         *        'owned' element (via a call to
         *        {@link #registerOwnedElement(HtmlPageElement)
         *        registerOwnedElement() }).
         */
        void initializeControl(@SuppressWarnings("unused")
        HtmlControl control) {
            // this version does nothing
        }

        /**
         * Updates the provided control to reflect the preference value in the
         * {@code UserPreferences} object. The priority used in the attempt to
         * set the value of the control will be {@link
         * org.recipnet.common.controls.HtmlControl#EXISTING_VALUE_PRIORITY
         * EXISTING_VALUE_PRIORITY}.
         * 
         * @param control the {@code HtmlControl} returned by
         *        {@code generateControl()}
         * @param prefs the {@code UserPreferences} object from which to
         *        determine the value for the control
         */
        abstract void updateControl(HtmlControl control, UserPreferences prefs);

        /**
         * Updates the 'prefs' to reflect the specified preference value
         * curretly stored in the control for this {@code PrefType}.
         * 
         * @param control the {@code HtmlControl} returned by
         *        {@code generateControl()}
         * @param prefs the {@code UserPreferences} object that should be
         *        updated to reflect the preference value in the control
         */
        abstract void updatePrefs(HtmlControl control, UserPreferences prefs);
    }
}
