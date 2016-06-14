/*
 * Reciprocal Net project
 * 
 * UserField.java
 * 
 * 18-May-2004: midurbin wrote first draft
 * 21-Jun-2004: cwestnea made miscellaneous changes to conform to conventions
 *              for Fields
 * 25-Jun-2004: cwestnea removed NO_ERROR_REPORTED constant and changed some 
 *              style in referring to error constants
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 16-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 16-Nov-2004: midurbin added an ExtraHtmlAttributeAccepter implementation
 * 17-Aug-2004: midurbin added 'redundantPassword' property, fixed support
 *              for editing password in onFetchingPhaseBeforeBody() and
 *              onProcessingPhaseAfterBody(), and added a new error flag
 * 23-Nov-2004: midurbin fixed bug #1473 in onProcessingPhaseAfterBody()
 * 30-Nov-2004: midurbin added INACTIVE_DATE field code
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 10-Jun-2005: midurbin added 'SUBMITTING_PROVIDER_TOGGLE' fieldCode
 * 16-Sep-2005: midurbin replaced a call to UserInfo.setPassword() with a call
 *              to UserBL.setPassword()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractTextHtmlControl;
import org.recipnet.common.controls.CheckboxHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.ExtraHtmlAttributeAccepter;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LabelHtmlControl;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;

/**
 * This Tag, coupled with an implementation of {@code UserContext}, allows
 * fields from a {@code UserInfo} object to be exposed on a JSP. This tag is
 * responsible for determining the control type, validation rules and some other
 * properties for the field it is assigned to expose. The field is indicated by
 * the {@code fieldCode} parameter.
 */
public class UserField extends HtmlPageElement implements ErrorSupplier,
        ExtraHtmlAttributeAccepter {

    /** Possible field codes, representing data within {@code UserInfo} */
    public static final int ID = 1;

    public static final int FULL_NAME = 2;

    public static final int USER_NAME = 3;

    public static final int PASSWORD = 4;

    public static final int CREATION_DATE = 5;

    public static final int INACTIVE_DATE = 6;

    public static final int LAB_ADMIN_TOGGLE = 7;

    public static final int SCIENTIFIC_USER_TOGGLE = 8;

    public static final int SITE_ADMIN_TOGGLE = 9;

    public static final int SUBMITTING_PROVIDER_TOGGLE = 10;

    /** Error flags defined for an {@code ErrorSupplier} implementation */
    public static final int REQUIRED_VALUE_IS_MISSING = 1 << 0;

    public static final int VALUE_IS_TOO_LONG = 1 << 1;

    public static final int PASSWORD_DOES_NOT_MATCH = 1 << 2;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return PASSWORD_DOES_NOT_MATCH;
    }

    /**
     * Indicates which field of the {@code UserInfo} object this control
     * represents. This variable is initialized to {@code FULL_NAME} by
     * {@code reset()} and may be set by its 'setter' method,
     * {@code setFieldCode()}. Valid values for this method are limited to
     * those static field codes defined by this class. This variable may not
     * change from phase to phase.
     */
    private int fieldCode;

    /**
     * Indicates whether this field may be edited. This variable is initialized
     * by {@code reset()} and may be set by its 'setter' method,
     * {@code setEditable()}, or altered when {@code fieldCode} is set. This
     * variable is overruled by {@code displayAsLabel}. This variable is
     * 'transient' in that its value may change from phase to phase and may be
     * updated by {@code copyTransientPropertiesFrom()}.
     */
    private boolean editable;

    /**
     * Indicates that this field may not be editable and should be displayed as
     * plain text. This variable is initialized by {@code reset()} and may be
     * set by its 'setter' method, {@code setDisplayAsLabel()} or altered when
     * {@code fieldCode} is set. This variable is 'transient' in that its value
     * may change from phase to phase and may be updated by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean displayAsLabel;

    /**
     * An optional property that is only valid when 'fieldCode' has been set to
     * {@code UserField.PASSWORD}. This is a reference to a
     * {@code TextboxHtmlControl} whose value must match the password entered
     * into this {@code UserField} or a validation failure is reported and the
     * {@code PASSWORD_DOES_NOT_MATCH} error flag is set.
     */
    private TextboxHtmlControl redundantPassword;

    /**
     * This is a reference to the owned {@code HtmlControl}. Its type is
     * derived from the {@code fieldCode} and it is initialized during the
     * {@code REGISTRATION_PHASE}.
     */
    private HtmlControl control;

    /**
     * The {@code UserContext} which this control sits in. Initialized in
     * reset() and discovered in {@code onRegistrationPhaseBeforeBody()}.
     */
    private UserContext userContext;

    /**
     * A mapping of attribute name {@code String}s to attribute value
     * {@code String}s representing extra attributes that should be added to
     * the 'owned' control when it is instantiated. This is used to implement
     * the {@code ExtraHtmlAttributeAccepter} interface.
     */
    private Map<String, String> extraAttributes;

    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = UserField.FULL_NAME;
        this.editable = false;
        this.displayAsLabel = true;
        this.redundantPassword = null;
        this.control = null;
        this.userContext = null;
        this.extraAttributes = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * Sets the fieldCode. This attribute should come before 'editable' or
     * 'displayAsLabel' as it will set default values based on the fieldCode. By
     * default, all fieldCodes result in 'displayAsLabel' being set to true
     * except for {@code LAB_ADMIN_TOGGLE}, {@code SCIENTIFIC_USER_TOGGLE},
     * {@code SITE_ADMIN_TOGGLE} and {@code SUBMITTING_PROVIDER_TOGGLE}, which
     * will almost never be used as a label.
     * 
     * @param fieldCode one of the static field codes defined on
     *        {@code UserField}, indicating which field within the
     *        {@code UserInfo} object this tag will expose.
     * @throws IllegalArgumentException if an unknown fieldCode has been given.
     * @throws IllegalStateException if the control is already been set and the
     *         user tries to change the field code
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case UserField.ID:
            case UserField.CREATION_DATE:
            case UserField.INACTIVE_DATE:
            case UserField.FULL_NAME:
            case UserField.USER_NAME:
            case UserField.PASSWORD:
                this.setDisplayAsLabel(true);
                break;
            case UserField.LAB_ADMIN_TOGGLE:
            case UserField.SCIENTIFIC_USER_TOGGLE:
            case UserField.SITE_ADMIN_TOGGLE:
            case UserField.SUBMITTING_PROVIDER_TOGGLE:
                this.setEditable(true);
                break;
            default:
                throw new IllegalArgumentException();
        }
        if ((this.control != null) && (this.fieldCode != fieldCode)) {
            // don't let the user try to change the type of field after it
            // has already been set
            throw new IllegalStateException();
        }
        this.fieldCode = fieldCode;
        if ((fieldCode == UserField.ID) || (fieldCode == UserField.CREATION_DATE)) {
            this.setDisplayAsLabel(true);
        }
    }

    /**
     * @return one of the static field codes defined on {@code UserField},
     *         indicating which field within the {@code UserInfo} object this
     *         tag will expose.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * Sets the 'editable' property, indicating whether this field may be
     * edited. If set to true, {@code displayAsLabel} is set to false.
     * 
     * @param editable whether or not this field should be editable
     * @throws IllegalArgumentException if this {@code UserField} has been
     *         assigned a {@code fieldCode} that may not be editable and this
     *         call attempts to set {@code editable} to true or if
     *         {@code displayAsLabel} is true. These fields include {@code ID},
     *         {@code CREATION_DATE} and {@code INACTIVE_DATE}.
     */
    public void setEditable(boolean editable) {
        if (editable
                && ((this.fieldCode == UserField.ID)
                        || (this.fieldCode == UserField.CREATION_DATE)
                        || (this.fieldCode == UserField.INACTIVE_DATE))) {
            throw new IllegalArgumentException();
        }
        this.editable = editable;
        this.displayAsLabel = false;
        if (this.control != null) {
            this.control.setEditable(editable);
        }
    }

    /** @return a boolean indicating whether or not this field is editable */
    public boolean getEditable() {
        return this.editable;
    }

    /**
     * Sets the 'displayAsLabel' property, indicating whether this field will be
     * displayed as a label as opposed to some sort of input control. If this
     * method is used to set 'displayAsLabel' to true, editable is implicitly
     * set to false.
     * 
     * @param displayAsLabel indicates whether this field will be displayed in
     *        plain text as a label or as some sort of input field.
     */
    public void setDisplayAsLabel(boolean displayAsLabel) {
        this.displayAsLabel = displayAsLabel;
        if (displayAsLabel == true) {
            this.editable = false;
        }
        if (this.control != null) {
            this.control.setDisplayAsLabel(displayAsLabel);
        }
    }

    /**
     * @return whether or not this field will be displayed as a label or an
     *         input field
     */
    public boolean getDisplayAsLabel() {
        return this.displayAsLabel;
    }

    /**
     * @param textbox a {@code TextboxHtmlControl} with which this tag compares
     *        the entered password
     * @throws IllegalArgumentException if 'fieldCode' is not set to
     *         {@code UserField.PASSWORD}.
     */
    public void setRedundantPassword(TextboxHtmlControl textbox) {
        if ((this.fieldCode != UserField.PASSWORD) && (textbox != null)) {
            throw new IllegalArgumentException();
        }
        this.redundantPassword = textbox;
    }

    /**
     * @return the {@code TextboxHtmlControl} with which this tag compares the
     *         entered password
     */
    public TextboxHtmlControl getRedundantPassword() {
        return this.redundantPassword;
    }

    /**
     * Implements {@code ExtraHtmlAttributeAccepter} by either passing them to
     * the owned control's implementation or putting them in the
     * {@code extraAttributes} map so that they may be added to the 'owned'
     * control when it is instnatiated.
     * 
     * @param name the name of the attribute
     * @param value the value of the attribute
     */
    public void addExtraHtmlAttribute(String name, String value) {
        if (this.control != null) {
            this.control.addExtraHtmlAttribute(name, value);
        } else {
            if (this.extraAttributes == null) {
                this.extraAttributes = new HashMap<String, String>();
            }
            this.extraAttributes.put(name, value);
        }
    }

    /** Implements {@code ErrorSupplier}. */
    public int getErrorCode() {
        if ((this.control != null)
                && (this.control instanceof TextboxHtmlControl)) {
            if ((this.control.getErrorCode()
                    & HtmlControl.REQUIRED_VALUE_IS_MISSING) != 0) {
                setErrorFlag(REQUIRED_VALUE_IS_MISSING);
            }
            if ((this.control.getErrorCode()
                    & AbstractTextHtmlControl.VALUE_IS_TOO_LONG) != 0) {
                setErrorFlag(VALUE_IS_TOO_LONG);
            }
        }

        return errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * {@inheritDoc}; this version instantiates its 'owned' element, finds the
     * nearest {@code UserContext} and delegates back to the superclass.
     * 
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class or if this tag is not within a
     *         {@code UserContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        TextboxHtmlControl textboxControl;

        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        if (this.userContext == null) {
            // This tag can't be used without UserContext
            throw new IllegalStateException();
        }

        switch (this.fieldCode) {
            case UserField.ID:
            case UserField.CREATION_DATE:
            case UserField.INACTIVE_DATE:
                this.control = new LabelHtmlControl();
                break;
            case UserField.FULL_NAME:
                textboxControl = new TextboxHtmlControl();
                textboxControl.setMaxLength(32);
                textboxControl.setSize(32);
                textboxControl.setRequired(true);
                this.control = textboxControl;
                break;
            case UserField.USER_NAME:
                textboxControl = new TextboxHtmlControl();
                textboxControl.setMaxLength(8);
                textboxControl.setSize(8);
                textboxControl.setRequired(true);
                this.control = textboxControl;
                break;
            case UserField.PASSWORD:
                textboxControl = new TextboxHtmlControl();
                textboxControl.setMaxLength(8);
                textboxControl.setSize(8);
                textboxControl.setMaskInput(true);
                textboxControl.setRequired(true);
                this.control = textboxControl;
                break;
            case UserField.LAB_ADMIN_TOGGLE:
            case UserField.SCIENTIFIC_USER_TOGGLE:
            case UserField.SITE_ADMIN_TOGGLE:
            case UserField.SUBMITTING_PROVIDER_TOGGLE:
                this.control = new CheckboxHtmlControl();
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        this.control.setEditable(this.editable);
        this.control.setDisplayAsLabel(this.displayAsLabel);
        if (this.extraAttributes != null) {
            // add any extra attributes
            for (Map.Entry<String, String> entry
                    : this.extraAttributes.entrySet()) {
                this.control.addExtraHtmlAttribute(entry.getKey(),
                        entry.getValue());
            }
            this.extraAttributes = null;
        }

        super.registerOwnedElement(this.control);

        return rc;
    }

    /**
     * {@inheritDoc}; in this version, if {@code redundantPassword} is
     * non-{@code null} then its value is compared to that of the 'owned'
     * control. If they do not match then {@code setFailedValidation()} is
     * invoked on the 'owned' element, the {@code PASSWORD_DOES_NOT_MATCH}
     * error flag is set on this {@code UserField}, and both fields are cleared.
     */
    @Override
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {
        if ((this.redundantPassword != null)
                && ((this.control.getValue() == null)
                        || !(this.control.getValue().equals(
                                this.redundantPassword.getValue())))) {
            /*
             * set 'failedValidation' so that a validation error will be
             * reported
             */
            this.control.setFailedValidation(true);

            // set the error flag
            setErrorFlag(UserField.PASSWORD_DOES_NOT_MATCH);
        }

        return super.onParsingPhaseAfterBody(request);
    }

    /**
     * {@inheritDoc}; this version gets the {@code UserInfo} from the most
     * immediate {@code UserContext}, then uses the value of its field that
     * corresponds to {@code fieldCode} to try to update the 'owned' field's
     * value, subject to a priority that will override default values but not
     * user input. The return value from the superclass's version (invoked at
     * the BEGINNING of this method) is returned.
     * 
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        UserInfo userInfo = this.userContext.getUserInfo();

        if (userInfo == null) {
            /*
             * if this UserContext refers to no UserInfo, control can't be
             * updated
             */
            return rc;
        }
        switch (this.fieldCode) {
            case UserField.ID:
                this.control.setValue(String.valueOf(userInfo.id),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.FULL_NAME:
                this.control.setValue(userInfo.fullName,
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.USER_NAME:
                this.control.setValue(userInfo.username,
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.PASSWORD:
                if (this.editable) {
                    /*
                     * it doesn't make sense to edit an existing password, so
                     * don't display anything and have the user enter a
                     * completely new password
                     */
                    this.control.setValue(null,
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    this.control.setValue(null,
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                }
                break;
            case UserField.LAB_ADMIN_TOGGLE:
                this.control.setValue(
                        Boolean.valueOf(
                                (userInfo.globalAccessLevel
                                        & UserInfo.LAB_ADMIN_ACCESS) != 0),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.SCIENTIFIC_USER_TOGGLE:
                this.control.setValue(
                        Boolean.valueOf((userInfo.globalAccessLevel
                                & UserInfo.LAB_SCIENTIFIC_USER_ACCESS) != 0),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.SITE_ADMIN_TOGGLE:
                this.control.setValue(
                        Boolean.valueOf(
                                (userInfo.globalAccessLevel
                                        & UserInfo.SITE_ADMIN_ACCESS) != 0),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.SUBMITTING_PROVIDER_TOGGLE:
                this.control.setValue(
                        Boolean.valueOf((userInfo.globalAccessLevel
                                & UserInfo.SUBMITTING_PROVIDER_ACCESS) != 0),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case UserField.CREATION_DATE:
                if (userInfo.creationDate != null) {
                    this.control.setValue(
                            DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                    DateFormat.SHORT, Locale.US).format(
                                    userInfo.creationDate),
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    this.control.setValue(null);
                }
                break;
            case UserField.INACTIVE_DATE:
                if (userInfo.inactiveDate != null) {
                    this.control.setValue(
                            DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                    DateFormat.SHORT, Locale.US).format(
                                    userInfo.inactiveDate),
                            HtmlControl.EXISTING_VALUE_PRIORITY);
                } else {
                    this.control.setValue(null);
                }
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the {@code UserInfo} from the most
     * immediate {@code UserContext}, then attempts to update the value of its
     * member variable that corresponds to {@code fieldCode} based on the
     * current value of the owned {@code HtmlControl}. It ultimately delegates
     * back to the superclass's version.
     * 
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        UserInfo userInfo = this.userContext.getUserInfo();

        if (userInfo == null) {
            // there is no userInfo to modify
            return super.onProcessingPhaseAfterBody(pageContext);
        }

        /*
         * Don't need to worry about the control's value. Worst case is the
         * existing db value.
         */
        switch (this.fieldCode) {
            case UserField.ID:
            case UserField.CREATION_DATE:
            case UserField.INACTIVE_DATE:
                // these fields may never be editable from the webapp
                break;
            case UserField.FULL_NAME:
                /*
                 * we know that the value of 'control' is a String because we
                 * set it to a TextboxHtmlControl in the registration phase
                 */
                userInfo.fullName = (String) this.control.getValue();
                break;
            case UserField.USER_NAME:
                /*
                 * we know that the value of 'control' is a String because we
                 * set it to a TextboxHtmlControl in the registration phase
                 */
                userInfo.username = (String) this.control.getValue();
                break;
            case UserField.PASSWORD:
                /*
                 * UserInfo.password is set to a hash of the value from the
                 * TextboxHtmlControl (which is guaranteed to be a String)
                 */
                if (this.control.getValue() == null) {
                    /*
                     * do nothing, because password is required, we can be sure
                     * that the value has been flagged as invalid and wont be
                     * used
                     */
                    assert this.control.getFailedValidation();
                } else {
                    UserBL.setPassword(userInfo,
                            (String) this.control.getValue());
                }
                break;
            case UserField.LAB_ADMIN_TOGGLE:
                if (this.control.getValueAsBoolean()) {
                    // set LAB_ADMIN_ACCESS flag without altering other bits
                    userInfo.globalAccessLevel |= UserInfo.LAB_ADMIN_ACCESS;
                } else {
                    // unset LAB_ADMIN_ACCESS flag without altering other bits
                    userInfo.globalAccessLevel &= ~UserInfo.LAB_ADMIN_ACCESS;
                }
                break;
            case UserField.SCIENTIFIC_USER_TOGGLE:
                if (this.control.getValueAsBoolean()) {
                    /*
                     * set LAB_SCIENTIFIC_USER_ACCESS flag without altering
                     * other bits
                     */
                    userInfo.globalAccessLevel
                            |= UserInfo.LAB_SCIENTIFIC_USER_ACCESS;
                } else {
                    /*
                     * unset LAB_SCIENTIFIC_USER_ACCESS flag without altering
                     * other bits
                     */
                    userInfo.globalAccessLevel
                            &= ~UserInfo.LAB_SCIENTIFIC_USER_ACCESS;
                }
                break;
            case UserField.SITE_ADMIN_TOGGLE:
                if (this.control.getValueAsBoolean()) {
                    // set SITE_ADMIN_ACCESS flag without altering other bits
                    userInfo.globalAccessLevel |= UserInfo.SITE_ADMIN_ACCESS;
                } else {
                    // unset SITE_ADMIN_ACCESS flag without altering other bits
                    userInfo.globalAccessLevel &= ~UserInfo.SITE_ADMIN_ACCESS;
                }
                break;
            case UserField.SUBMITTING_PROVIDER_TOGGLE:
                if (this.control.getValueAsBoolean()) {
                    /*
                     * set SUBMITTING_PROVIDER_ACCESS flag without altering
                     * other bits
                     */
                    userInfo.globalAccessLevel
                            |= UserInfo.SUBMITTING_PROVIDER_ACCESS;
                } else {
                    /*
                     * unset SUBMITTING_PROVIDER_ACCESS flag without altering
                     * other bits
                     */
                    userInfo.globalAccessLevel
                            &= ~UserInfo.SUBMITTING_PROVIDER_ACCESS;
                }
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        UserField src = (UserField) source;

        super.copyTransientPropertiesFrom(source);
        /*
         * to ensure that values are propagated to owned elements, each setter
         * method has to be called rather than directly setting its variable
         */
        this.setRedundantPassword(src.redundantPassword);
        this.setEditable(src.editable);
        this.setDisplayAsLabel(src.displayAsLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UserField dc = (UserField) super.generateCopy(newId, map);

        dc.control = (HtmlControl) map.get(this.control);
        dc.userContext = (UserContext) map.get(this.userContext);
        if (this.extraAttributes != null) {
            dc.extraAttributes = new HashMap<String, String>(
                    this.extraAttributes);
        }

        return dc;
    }
}
