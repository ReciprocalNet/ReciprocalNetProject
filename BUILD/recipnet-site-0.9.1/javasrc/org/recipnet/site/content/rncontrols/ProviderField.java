/*
 * Reciprocal Net project
 * 
 * ProviderField.java
 * 
 * 16-Jun-2004: cwestnea wrote first draft based on UserField
 * 21-Jun-2004: cwestnea clarified javadocs
 * 25-Jun-2004: cwestnea removed NO_ERROR_REPORTED constant and changed some 
 *              style in referring to error constants
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 15-Nov-2004: midurbin fixed bug #1461 in onFetchingPhaseBeforeBody() and
 *              onProcessingPhaseAfterBody()
 * 16-Nov-2004: midurbin added an ExtraHtmlAttributeAccepter implementation
 * 23-Nov-2004: midurbin fixed bug #1473 in onProcessingPhaseAfterBody()
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractTextHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.ExtraHtmlAttributeAccepter;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LabelHtmlControl;
import org.recipnet.common.controls.TextareaHtmlControl;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.shared.db.ProviderInfo;

/**
 * This Tag, coupled with an implementation of {@code ProviderContext}, allows
 * fields from a {@code ProviderInfo} object to be exposed on a JSP. This tag is
 * responsible for determining the control type, validation rules and some other
 * properties for the field it is assigned to expose. The field is indicated by
 * the {@code fieldCode} parameter.
 */
public class ProviderField extends HtmlPageElement implements ErrorSupplier,
        ExtraHtmlAttributeAccepter {

    /**
     * Possible field codes, representing data within {@code ProviderInfo}
     */
    public static final int ID = 1;

    public static final int NAME = 2;

    public static final int HEAD_CONTACT = 3;

    public static final int COMMENTS = 4;

    /**
     * Error flags defined for an {@code ErrorSupplier} implementation. These
     * codes match those on the textbox and textarea controls.
     */
    public static final int REQUIRED_VALUE_IS_MISSING = 1 << 0;

    public static final int VALUE_IS_TOO_LONG = 1 << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return VALUE_IS_TOO_LONG;
    }

    /**
     * Indicates which field of the {@code ProviderInfo} object this control
     * represents. This variable is initialized to {@code NAME} by
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
     * This is a reference to the owned {@code HtmlControl}. Its type is
     * derived from the {@code fieldCode} and it is initialized during the
     * {@code REGISTRATION_PHASE}.
     */
    private HtmlControl control;

    /**
     * The {@code ProviderContext} that this control uses to obtain its data.
     * Initialized in {@code reset()} and discovered in
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private ProviderContext providerContext;

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
        this.fieldCode = ProviderField.NAME;
        this.editable = false;
        this.displayAsLabel = true;
        this.control = null;
        this.providerContext = null;
        this.extraAttributes = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * Sets the fieldCode. This attribute should come before 'editable' or
     * 'displayAsLabel' as it will set default values based on the fieldCode. By
     * default, all fieldCodes result in 'displayAsLabel' being set to true.
     * 
     * @param fieldCode one of the static field codes defined on
     *        {@code ProviderField}, indicating which field within the
     *        {@code ProviderInfo} object this tag will expose.
     * @throws IllegalStateException if an unknown fieldCode has been given.
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case ProviderField.ID:
            case ProviderField.NAME:
            case ProviderField.HEAD_CONTACT:
            case ProviderField.COMMENTS:
                break;
            default:
                throw new IllegalArgumentException();
        }
        if ((this.control != null) && (this.fieldCode != fieldCode)) {
            /*
             * don't let the user try to change the type of field after it has
             * already been set
             */
            throw new IllegalStateException();
        }
        this.fieldCode = fieldCode;
        if (fieldCode == ProviderField.ID) {
            this.setDisplayAsLabel(true);
        }
    }

    /**
     * @return one of the static field codes defined on {@code ProviderField},
     *         indicating which field within the {@code ProviderInfo} object
     *         this tag will expose.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * Sets the 'editable' property, indicating whether this field may be
     * edited. If set to true, {@code displayAsLabel} is set to false.
     * 
     * @param editable whether or not this field should be editable
     * @throws IllegalArgumentException if this {@code ProviderField} has been
     *         assigned a {@code fieldCode} that may not be editable and this
     *         call attempts to set {@code editable} to true or if
     *         {@code displayAsLabel} is true. These fields include {@code ID}
     *         and {@code CREATION_DATE}
     */
    public void setEditable(boolean editable) {
        if (editable && (this.fieldCode == ProviderField.ID)) {
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
        if (this.control instanceof AbstractTextHtmlControl) {
            /*
             * Note: restriction to testing for required values only on text
             * controls is preserved from previous behavior. As of version 0.9.0
             * of the site software, however, the only field not subject to such
             * testing is the ID field.
             */
            if ((this.control.getErrorCode()
                    & HtmlControl.REQUIRED_VALUE_IS_MISSING) != 0) {
                setErrorFlag(REQUIRED_VALUE_IS_MISSING);
            }
            if ((this.control.getErrorCode()
                    & AbstractTextHtmlControl.VALUE_IS_TOO_LONG) != 0) {
                setErrorFlag(VALUE_IS_TOO_LONG);
            }
        }
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * {@inheritDoc}; this version instantiates its 'owned' element, finds the
     * nearest {@code ProviderContext} and delegates back to the superclass.
     * 
     * @param pageContext the current {@code PageContext}; may be useful to
     *        superclass or 'owned' elements.
     * @return the return code is taken from the superclass' implementation of
     *         this method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class, or if this tag is not within a
     *         {@code ProviderContext}.
     * @throws JspException if an exception is encountered during processing for
     *         this phase.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        TextboxHtmlControl textboxControl;
        TextareaHtmlControl textareaControl;

        this.providerContext = findRealAncestorWithClass(this,
                ProviderContext.class);
        if (this.providerContext == null) {
            // This tag can't be used without ProviderContext
            throw new IllegalStateException();
        }
        switch (this.fieldCode) {
            case ProviderField.ID:
                this.control = new LabelHtmlControl();
                break;
            case ProviderField.NAME:
                textboxControl = new TextboxHtmlControl();
                textboxControl.setMaxLength(64);
                textboxControl.setSize(64);
                textboxControl.setRequired(this.editable);
                this.control = textboxControl;
                break;
            case ProviderField.HEAD_CONTACT:
                textboxControl = new TextboxHtmlControl();
                textboxControl.setMaxLength(64);
                textboxControl.setSize(64);
                textboxControl.setRequired(false);
                this.control = textboxControl;
                break;
            case ProviderField.COMMENTS:
                textareaControl = new TextareaHtmlControl();
                textareaControl.setRows(10);
                textareaControl.setColumns(50);
                textareaControl.setRequired(false);
                this.control = textareaControl;
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        this.control.setEditable(this.editable);
        this.control.setDisplayAsLabel(this.displayAsLabel);
        if (this.extraAttributes != null) {
            // add any extra attributes
            for (Entry<String, String> entry : this.extraAttributes.entrySet()) {
                this.control.addExtraHtmlAttribute(entry.getKey(),
                        entry.getValue());
            }
            this.extraAttributes = null;
        }
        super.registerOwnedElement(this.control);
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the {@code ProviderInfo} from the most
     * immediate {@code ProviderContext}, then uses the value of its field that
     * corresponds to {@code fieldCode} to try to update the 'owned' field's
     * value with a priority that would override default values but not user
     * inputted values. Finally the return value from the superclass's method
     * (invoked at the BEGINNING of this method) is returned.
     * 
     * @return the return value of the superclass's implementation of this
     *         method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class, or if the {@code ProviderContext}
     *         has no {@code ProviderInfo}.
     * @throws JspException if any other error is encountered while executing
     *         this method.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        ProviderInfo providerInfo = this.providerContext.getProviderInfo();
        
        if (providerInfo == null) {
            /*
             * this tag is nested in a null context, simply return without
             * updating the value
             */
            return rc;
        }
        switch (this.fieldCode) {
            case ProviderField.ID:
                this.control.setValue(String.valueOf(providerInfo.id),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case ProviderField.NAME:
                this.control.setValue(providerInfo.name,
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case ProviderField.HEAD_CONTACT:
                this.control.setValue(providerInfo.headContact,
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case ProviderField.COMMENTS:
                this.control.setValue(providerInfo.comments,
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the {@code ProviderInfo} from the most
     * immediate {@code ProviderContext}, then attempts to update the value of
     * its member variable that corresponds to {@code fieldCode} based on the
     * current value of the owned {@code HtmlControl}. Then it delegates back
     * to the superclass's implementation. At this point, the value of the owned
     * control has been validated, and if not editable, the value is not the
     * parsed value.
     * 
     * @return the return value of the superclass' implementation of this
     *         method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class, or if the {@code ProviderContext}
     *         has no {@code ProviderInfo}.
     * @throws JspException if any other error is encountered while executing
     *         this method.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        ProviderInfo providerInfo = this.providerContext.getProviderInfo();
        
        if (providerInfo == null) {
            /*
             * this tag is nested within a null context, simply delegate to the
             * superclass's implementation without processing the value
             */
            return super.onProcessingPhaseAfterBody(pageContext);
        }
        /*
         * Don't need to worry about the control's value. Worst case is the
         * existing db value.
         */
        switch (this.fieldCode) {
            case ProviderField.ID:
                // this field may never be editable from the webapp
                break;
            case ProviderField.NAME:
                /*
                 * we know that the value of 'control' is a String because we
                 * set it to a TextboxHtmlControl in the registration phase
                 */
                providerInfo.name = (String) this.control.getValue();
                break;
            case ProviderField.HEAD_CONTACT:
                /*
                 * we know that the value of 'control' is a String because we
                 * set it to a TextboxHtmlControl in the registration phase
                 */
                providerInfo.headContact = (String) this.control.getValue();
                break;
            case ProviderField.COMMENTS:
                /*
                 * we know that the value of 'control' is a String because we
                 * set it to a TextareaHtmlControl in the registration phase
                 */
                providerInfo.comments = (String) this.control.getValue();
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
        ProviderField src = (ProviderField) source;
        
        super.copyTransientPropertiesFrom(source);
        /*
         * to ensure that values are propagated to owned elements, each setter
         * method has to be called rather than directly setting its variable
         */
        this.setEditable(src.editable);
        this.setDisplayAsLabel(src.displayAsLabel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ProviderField dc = (ProviderField) super.generateCopy(newId, map);
        
        dc.control = (HtmlControl) map.get(this.control);
        dc.providerContext = (ProviderContext) map.get(this.providerContext);
        
        return dc;
    }
}
