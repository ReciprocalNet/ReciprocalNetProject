/*
 * Reciprocal Net project
 *
 * SampleField.java
 *
 * 11-Jun-2004: midurbin wrote first draft
 * 21-Jun-2004: midurbin fixed bug #1253 in setFieldCode() and
 *              onFetchingPhase()
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 26-Aug-2004: midurbing added getDefaultCopyrightNoticeFromLabContext
 *              attribute
 * 30-Aug-2004: added code to utilize SampleTextContext.getTextType() to
 *              generateHtmlControlForFieldCode()
 * 27-Sep-2004: midurbin added 'prohibited' and 'required' properties
 * 28-Sep-2004: midurbin added an ErrorSupplier implementation
 * 01-Oct-2004: midurbin fixed bug #1406 in copyTransientPropertiesFrom(),
 *              onProcessingPhaseAfterBody() and
 *              generateHtmlControlForFieldCode()
 * 19-Oct-2004: midurbin added 'treatUnknownAsAnnotationInsteadOfAttribute'
 *              property
 * 16-Nov-2004: midurbin added an ExtraHtmlAttributeAccepter implementation
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 26-Jan-2005: midurbin fixed bug #1520 in onProcessingPhaseAfterBody()
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 10-Mar-2005: midurbin fixed bug #1642 in onRenderingPhaseAfterBody()
 * 10-Mar-2005: midurbin added support for the PREFERRED_FIELD annotation
 * 23-Mar-2005: midurbin updated onFetchingPhaseBeforeBody() to account for
 *              SampleTextBL changes
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 08-Apr-2005: midurbin added modifyHtmlControlForFieldCode() so that
 *              formatters may be attached to the 'owned' control for certain
 *              field codes once it is known
 * 12-Apr-2005: midurbin fixed bug #1565 in translateObjectFromContainer()
 * 12-Apr-2005: midurbin added support for RAW_DATA_URL fieldCode
 * 06-Jun-2005: midurbin fixed bug #1606 in onFetchingPhaseBeforeBody()
 * 10-Jun-2005: midurbin added 'displayOnly' property, renamed
 *              GET_FIELD_CODE_FROM_SAMPLE_TEXT_CONTEXT to
 *              AUTO_DETECT_FIELD_CODE
 * 30-Jun-2005: midurbin made use of a ProviderSelector when the fieldCode is
 *              PROVIDER, added validator for the local lab id and added
 *              VALIDATOR_REJECTED_VALUE error flag
 * 07-Jul-2005: midurbin fixed bug #1626 in onProcessingPhaseAfterBody()
 * 11-Jul-2005: midurbin added validation, formatting for SPGP_FIELD values
 * 26-Jul-2005: midurbin split 'owned' control instantiation from 'owned'
 *              control configuration by replacing
 *              generateHtmlControlForFieldCode() with
 *              getNewHtmlControlForField() and moving property-setting code to
 *              modifyHtmlControlForFieldCode()
 * 05-Aug-2005: midurbin removed the code that output the field units
 * 15-Aug-2005: midurbin added 'formatFieldForSearchResults' and
 * 15-Aug-2005: midurbin added removeValidator()
 *              'styleClassForSearchMatch' properties
 * 19-Aug-2005: midurbin added escaping of field values when 'displayValueOnly'
 *              is set to true and added code to ensure that multi-valued
 *              fields are not displayed in 'displayValueOnly' mode
 * 06-Oct-2005: midurbin added support for new
 *              SampleInfo.MOST_RECENT_PROVIDER_ID,
 *              SampleDataInfo.PROVIDER_ID fields
 * 11-Nov-2005: midurbin fixed bug #1694 in onProcessingPhaseAfterBody; added
 *              'overrideSpecificValidationUnlessParamNameMatches' property
 * 12-Nov-2005: midurbin added MOIETY_FORMULA_VALIDATOR,
 *              EMPIRICAL_FORMULA_VALIDATOR and
 *              EMPIRICAL_FORMULA_SINGLE_ION_VALIDATOR
 * 11-Jan-2006: jobollin updated javadocs, inserted override annotations, and
 *              organized imports
 * 21-Feb-2006: jobollin added support for ValuePriorityOverrideContext, added
 *              a max field length to a few field types that needed it but
 *              didn't have it
 * 01-Mar-2006: jobollin added support for late binding of fieldCode-specific
 *              owned control types; fixed assertion failure in
 *              onProcessingPhaseAfterBody()
 * 14-Mar-2006: jobollin added an ownedElementId property
 * 17-Mar-2006: jobollin added width and height properties
 * 27-Mar-2006: jobollin removed invocations of resetOwnedElement()
 * 29-Dec-2007: ekoperda removed extraneous debugging text going to System.out
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.Validator;
import org.recipnet.common.controls.AbstractTextHtmlControl;
import org.recipnet.common.controls.DoubleTextboxHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.ExtraHtmlAttributeAccepter;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.IntegerTextboxHtmlControl;
import org.recipnet.common.controls.LabelHtmlControl;
import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.common.controls.MultiboxHtmlControl;
import org.recipnet.common.controls.TextareaHtmlControl;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.common.controls.ValidationContext;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.SampleFieldRecord;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.bl.ChemicalFormulaBL;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.search.FieldMatchFormatter;
import org.recipnet.site.shared.search.FieldMatchInfo;
import org.recipnet.site.shared.search.SearchMatches;
import org.recipnet.site.shared.validation.EmpiricalFormulaIonValidator;
import org.recipnet.site.shared.validation.EmpiricalFormulaValidator;
import org.recipnet.site.shared.validation.FilenameValidator;
import org.recipnet.site.shared.validation.MoietyFormulaValidator;
import org.recipnet.site.shared.validation.SpaceGroupSymbolValidator;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * This is a custom tag that represents the value of a sample attribute, sample
 * annotation, sample data info field, or local tracking attribute.  The field
 * could be displayed as plain text or as an editbox.  This tag MUST be nested
 * within a tag that implements {@code SampleContext} and when
 * {@code getDefaultCopyrightNoticeFromLabContext} is 'true' it must also
 * be nested within tge {@code LabContext} for the sample's originating
 * lab.
 * </p><p>
 * To display the units associated with the value output by this that, a
 * {@code SampleFieldUnits} tag with the same 'fieldCode' should be
 * included immediately after this tag.
 * </p>
 */
public class SampleField extends HtmlPageElement
        implements ErrorSupplier, ExtraHtmlAttributeAccepter {

    /**
     * An {@code HtmlFormatter} that will be attached to the 'owned'
     * control whenever the fieldCode represents a chemical formula.
     */
    private static final HtmlFormatter CHEMICAL_FORMULA_FORMATTER
            = new ChemicalFormulaHtmlFormatter();

    /**
     * An {@code HtmlFormatter} that will be attached to the 'owned'
     * control whenever the fieldCode represents an IUPAC name.
     */
    private static final HtmlFormatter IUPAC_NAME_WRAP_FORMATTER
            = new IupacNameWrapHtmlFormatter();

    /**
     * An {@code HtmlFormatter} that will be attached to the 'owned'
     * control whenever the fieldCode represents data that should be
     * interprited as a URL.
     */
    private static final HtmlFormatter URL_FORMATTER
            = new UrlHtmlFormatter();

    /**
     * An {@code HtmlFormatter} that will be attached to the 'owned'
     * control whenver the fieldCode represents a space group symbol.
     */
    private static final HtmlFormatter SPGP_FORMATTER
            = new SpaceGroupSymbolHtmlFormatter();

    /**
     * A {@code Validator} that will validate the 'owned' control's value
     * if it represents a space group symbol.
     */
    private static final Validator SPGP_VALIDATOR
            = new SpaceGroupSymbolValidator();

    /**
     * The same type of validator used by core to validate the 'owned' control
     * when it represents a {@code LOCAL_LAB_ID} .
     */
    private static final Validator LOCAL_LAB_ID_VALIDATOR
            = new FilenameValidator();

    /**
     * The same type of validator used by core to validate the 'owned' control
     * when it represents a {@code MOIETY_FORMULA} .
     */
    private static final Validator MOIETY_FORMULA_VALIDATOR
            = new MoietyFormulaValidator();

    /**
     * The same type of validator used by core to validate the 'owned' control
     * when it represents a {@code EMPIRICAL_FORMULA} .
     */
    private static final Validator EMPIRICAL_FORMULA_VALIDATOR
            = new EmpiricalFormulaValidator();

    /**
     * The same type of validator used by core to validate the 'owned' control
     * when it represents a {@code EMPIRICAL_FORMULA_SINGLE_ION} .
     */
    private static final Validator EMPIRICAL_FORMULA_SINGLE_ION_VALIDATOR
            = new EmpiricalFormulaIonValidator();

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * this field's 'required' attribute is true but no value has been entered.
     */
    public static final int REQUIRED_VALUE_IS_MISSING = 1 << 0;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * this field's 'prohibited' attribute is true and a value has been
     * entered.
     */
    public static final int PROHIBITED_VALUE_IS_PRESENT = 1 << 1;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * there is a maximum length associated with this field's text type and
     * input that exceeds it.
     */
    public static final int VALUE_IS_TOO_LONG = 1 << 2;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * a value has been entered for this field, but for some reason the context
     * that provides the {@code SampleInfo} or {@code SampleTextInfo}
     * to be modified returns null.
     */
    public static final int VALUE_ENTERED_FOR_NULL_CONTEXT = 1 << 3;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * this field is meant to be a number (in which case the 'owned' control is
     * either an {@code IntegerTextboxHtmlControl} or a
     * {@code DoubleTextboxHtmlControl}) but the value entered cannot be
     * parsed into a number.
     */
    public static final int VALUE_IS_NOT_A_NUMBER = 1 << 4;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * this field is meant to be a number (in which case the 'owned' control is
     * either an {@code IntegerTextboxHtmlControl} or a
     * {@code DoubleTextboxHtmlControl}) but the value is lower than the
     * minimum value allowed for that particular text type.
     */
    public static final int VALUE_IS_TOO_LOW = 1 << 5;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * this field is meant to be a number (in which case the 'owned' control is
     * either an {@code IntegerTextboxHtmlControl} or a
     * {@code DoubleTextboxHtmlControl}) but the value is higher than the
     * maximum value allowed for that particular text type.
     */
    public static final int VALUE_IS_TOO_HIGH = 1 << 6;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * the fieldCode is {@code SampleTextBL.PREFERRED_NAME} yet no names
     * have been entered from the sample.  In such cases this field will output
     * nothing and it might be useful for page authors to display some sort of
     * message explaining the situation.
     */
    public static final int NO_SAMPLE_NAMES = 1 << 7;

    /**
     * An error flag for the ErrorSupplier implementation that will be set if
     * the 'owned' control has the
     * {@code HtmlControl.VALIDATOR_REJECTED_VALUE} error code.
     */
    public static final int VALIDATOR_REJECTED_VALUE = 1 << 8;

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return VALIDATOR_REJECTED_VALUE;
    }

    /**
     * This value, when used as the {@code fieldCode} for this tag
     * indicates that this field representss a single annotation or attribute
     * value that is fetched from the most immediate
     * {@code SampleTextContext} during the {@code FETCHING_PHASE}.
     */
    public static final int AUTO_DETECT_FIELD_CODE = -1;

    /**
     * This value, when used as the {@code resolvedFieldCode} for this tag
     * indicates that this the fieldCode has not yet been resolved from the
     * {@code SampleTextContext} or {@code SampleFieldContext}.  If
     * such a value persists after the {@code FETCHING_PHASE} there is 
     * little chance that it will ever be resolved and most likely this tag
     * is nested within contexts that supply no useful data.
     */
    public static final int UNRESOLVED = -1;

    /**
     * A reference to the most immediate {@code SampleContext}, acquired
     * during the {@code REGISTRATION_PHASE} and later used to get the
     * {@code SampleInfo} object for the sample about which this
     * {@code SampleField} pertains.
     */
    private SampleContext sampleContext;

    /**
     * A reference to the most immediate {@code SampleFieldContext},
     * acquired during the {@code REGISTRATION_PHASE} if the 'fieldCode'
     * is {@code AUTO_DETECT_FIELD_CODE}.  If this context is found,
     * the {@code SampleTextContext} will not be sought to resolve the
     * 'fieldCode'.
     */
    private SampleFieldContext sampleFieldContext;

    /**
     * A reference to the most immediate {@code ValidationContext}.  When
     * this this context exists and reports that not all nested fields are
     * valid, this tag will not update the {@code SampleInfo}.
     */
    private ValidationContext validationContext;
    
    /**
     * If {@code fieldCode} is {@code AUTO_DETECT_FIELD_CODE} and no
     * {@code SampleFieldContext} could be found, this serves as a
     * reference to the most immediate {@code SampleTextContext}, acquired
     * during the {@code REGISTRATION_PHASE} and later used to get the
     * {@code SampleTextInfo} object for the sample about which this
     * {@code SampleField} pertains.
     */
    private SampleTextContext sampleTextContext;

    /**
     * If {@code getDefaultCopyrightNoticeFromLabContext} is set, then
     * this maintains a reference to the most immediate {@code LabContext}
     * that is determined by {@code onRegistrationPhaseBeforeBody()} and
     * used in {@code onFetchingPhaseBeforeBody()}.
     */
    private LabContext labContext;

    /**
     * A mapping of attribute name {@code String}s to attribute value
     * {@code String}s representing extra attributes that should be added
     * to the 'owned' control when it is instantiated.  This is used to
     * implement the {@code ExtraHtmlAttributeAccepter} interface.
     */
    private Map<String, String> extraAttributes;

    /**
     * Indicates what field within a SampleInfo container this control exposes.
     * This attribute is initialized by {@code reset()} and may be altered
     * by its 'setter' method, {@code setFieldCode()} but must not be
     * altered after the {@code REGISTRATION_PHASE}.  This value defaults
     * to {@code AUTO_DETECT_FIELD_CODE} which requires that this field be
     * nested within a {@code SampleTextContext} that represents the field
     * to be exposed.
     */
    private int fieldCode;

    /**
     * When 'fieldCode' is {@code AUTO_DETECT_FIELD_CODE} this variable
     * will hold the resolved field code (from the
     * {@code SampleTextContext} or {@code SampleFieldContext}) from
     * the earliest point when it can be established.  Before that point it's
     * value is {@code AUTO_DETECT_FIELD_CODE}.
     */
    private int resolvedFieldCode;

    /**
     * Indicates that this {@code SampleField} is meant simply to display
     * the value, not to allow it to be edited.  This variable is initialized
     * by {@code reset()} and altered by its setter method,
     * {@code setDisplayValueOnly()}.  This property may not be set to
     * true when the 'fieldCode' property is an annotation or attribute because
     * there is currently no support to display a multi-valued field's value.
     */
    private boolean displayValueOnly;

    /**
     * Indicates that this {@code SampleField} is meant simply to display
     * and persist the value as a hidden form field, not to allow it to be
     * edited.  This variable is initialized by {@code reset()} and
     * altered by its setter method, {@code setDisplayAsLabel()}.
     */
    private boolean displayAsLabel;

    /**
     * This optional property represents the name of a parameter whose value,
     * if present, is interprited as an int code that indicates which fields
     * are to be uneditable (as determined by a call to <code>{@link
     * SampleWorkflowBL#isFieldCorrectable(int, int)
     * SampleWorkflowBl.isFieldCorrectable()}</code>).  This value defaults to
     * 'uncorrectableFields'.
     */
    private String uneditableFieldsParamName;

    /**
     * An optional transient property that indicates whether this field may be
     * left blank or not.  This variable defaults to {@code false}.
     */
    private boolean required;

    /**
     * An optional transient property that indicates whether entering a value
     * into this field is prohibited or not.  This variable defaults to
     *{@code false}.
     */
    private boolean prohibited;

    /**
     * An optional attribute that indicates whether
     * {@code LabInfo.defaultCopyrightNotice} can be used as a default
     * value for this field if it represents a
     * {@code SampleTextInfo.COPYRIGHT_NOTICE}.  This attribute is only
     * valid if {@code fieldCode} is set to
     * {@code SampleTextInfo.COPYRIGHT_NOTICE}.  If this value is set to
     * true the most immediate {@code LabContext} will be determined by
     * {@code onRegistrationPhaseBeforeBody()} and if there is no
     * higher priority value, the lab's default copyright notice will be used.
     */
    private boolean getDefaultCopyrightNoticeFromLabContext;

    /**
     * An optional attribute that defaults to false.  This attribute is only
     * useful when 'fieldCode' is set to {@code AUTO_DETECT_FIELD_CODE}
     * and {@code SampleTextContext.getTextType()} does not provide the
     * text type during the {@code REGISTRATION_PHASE}, because it defines
     * what type of 'owned' control this tag will created assuming that the
     * text type is not known early.  If this is set to 'false' the call to
     * {@code generateHtmlControlForFieldCode()} will return a
     * {@code TextboxHtmlControl}.  On the other hand, if this property is
     * set to 'true' the same method will return a
     * {@code TextareaHtmlControl}.
     */
    private boolean useTextareaInsteadOfTextboxForUnknownCode;

    /**
     * A private variable that keeps track of whether
     * {@code overrideSpecificValidation()} has been called.  If such a
     * call has been made, no type-specific {@code Validator} may be
     * attached to the 'owned' control.
     */
    private boolean overrideSpecificValidation;

    /**
     * An optional property that when set will prevent specific field
     * validation unless a parameter exists whose name matches the regular
     * expression indicated by this {@code String}.  This allows multiple
     * controls to trigger full validation of this field.  A likely use case
     * would be to set this property to match the name of a button that was
     * meant to save the {@code SampleInfo} to the database, in which case
     * specific validation would be overridden until the 'save' button was
     * clicked.
     */
    private String overrideSpecificValidationUnlessParamNameMatches;

    /**
     * An optional property, that when set to true, indicates that this field
     * should have formatting applied to it to indicate whether it matched the
     * search criteria.  If this property is set to true, it must be nested
     * within a {@code SearchResultPage} tag.  This property defaults to
     * false.
     */
    private boolean formatFieldForSearchResults;

    /**
     * The name of a CSS class that should be used to format the field (or part
     * of a field) that matches the search constraints.  This property is
     * required whenever 'formatFieldForSearchResults' is indicated.
     */
    private String styleClassForSearchMatch;
    
    /**
     * The ID that should be assigned to the owned element
     */
    private String ownedElementId;

    /**
     * A tag attribute representing the width of the owned control; the default
     * is dependent on field type
     */
    private int width;

    /**
     * A tag attribute representing the height of the owned control, if
     * applicable; the default is dependent on field type
     */
    private int height;
    
    /** Used to implement {@code ErrorSupplier}.  */
    private int errorCode;

    /**
     * The internally-maintained, 'owned' control.  Its type is based on
     * {@code fieldCode} and it is initialized during the
     * {@code REGISTRATION_PHASE}.  If 'displayValueOnly' is set, this
     * {@code HtmlControl} will be null.
     */
    private HtmlControl control;
    
    private DoubleTextboxHtmlControl doubleAlternativeControl;
    
    private IntegerTextboxHtmlControl intAlternativeControl;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();

        // recognizable contexts
        this.sampleContext = null;
        this.sampleTextContext = null;
        this.validationContext = null;
        this.sampleFieldContext = null;
        this.labContext = null;

        // field-defining properties
        this.fieldCode = SampleField.AUTO_DETECT_FIELD_CODE;
        this.resolvedFieldCode = SampleField.UNRESOLVED;
        this.control = null;
        this.doubleAlternativeControl = null;
        this.intAlternativeControl = null;
        
        // special purpose properties
        this.extraAttributes = null;
        this.getDefaultCopyrightNoticeFromLabContext = false;
        this.useTextareaInsteadOfTextboxForUnknownCode = false;
        this.styleClassForSearchMatch = null;
        this.overrideSpecificValidationUnlessParamNameMatches = null;
        this.ownedElementId = null;

        // display mode determining properties
        this.displayAsLabel = false;
        this.displayValueOnly = false;
        this.uneditableFieldsParamName = "uncorrectableFields";
        this.formatFieldForSearchResults = false;
        this.width = -1;
        this.height = -1;
        
        // validation requirement properties
        this.required = false;
        this.prohibited = false;

        // ErrorSupplier implementation
        this.errorCode = NO_ERROR_REPORTED;

        // private indicators
        this.overrideSpecificValidation = false;
    }

    /**
     * Setter for the 'overrideSpecificValidationUnlessParamNameMatches'
     * property.
     */
    public void setOverrideSpecificValidationUnlessParamNameMatches(
            String regExp) {
        this.overrideSpecificValidationUnlessParamNameMatches = regExp;
    }

    /**
     * Getter for the 'overrideSpecificValidationUnlessParamNameMatches'
     * property.
     */
    public String getOverrideSpecificValidationUnlessParamNameMatches() {
        return this.overrideSpecificValidationUnlessParamNameMatches;
    }

    /**
     * @param fieldCode a constant value indicating which element of the
     * {@code SampleInfo} this {@code SampleField} represents.
     * @throws IllegalArgumentException if the fieldcode is not valid or if
     *     'useTextareaInsteadOfTextboxForUnknownCode' has been set and
     *     'fieldCode' is not being set to {@code AUTO_DETECT_FIELD_CODE}.
     */
    public void setFieldCode(int fieldCode) {
        if (!SampleTextBL.isAttribute(fieldCode)
                && !SampleTextBL.isAnnotation(fieldCode)
                && !SampleDataInfo.isDataField(fieldCode)
                && !SampleInfo.isSampleField(fieldCode)
                && (fieldCode != SampleField.AUTO_DETECT_FIELD_CODE)) {
            // invalid fieldCode
            throw new IllegalArgumentException();
        }
        if (this.useTextareaInsteadOfTextboxForUnknownCode && (fieldCode
                != SampleField.AUTO_DETECT_FIELD_CODE)) {
            throw new IllegalStateException();
        }
        this.fieldCode = fieldCode;
    }

    /**
     * @return a constant value indicating which element of the
     * {@code SampleInfo} this {@code SampleField} represents.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * @param displayAsLabel indicates whether this field should be represented
     *     by text and a hidden form field
     */
    public void setDisplayAsLabel(boolean displayAsLabel) {
        this.displayAsLabel = displayAsLabel;
        if (this.control != null) {
            this.control.setDisplayAsLabel(displayAsLabel);
        } else {
            // this variable will be set on the owned control when it is
            // instantiated
        }
    }

    /**
     * @return a boolean that indicates whether this field should be
     *     represented by text and a hidden form field
     */
    public boolean getDisplayAsLabel() {
        return this.displayAsLabel;
    }

    /**
     * @param displayValueOnly indicates whether this field should be
     *     represented by text without persisting its value in a form field
     */
    public void setDisplayValueOnly(boolean displayValueOnly) {
        this.displayValueOnly = displayValueOnly;
    }

    /**
     * @return a boolean that indicates whether this field should be
     *     represented by text without persisting its value in a form field
     */
    public boolean getDisplayValueOnly() {
        return this.displayValueOnly;
    }

    /**
     * @param required indicates whether this field must have a value or may
     *     be left blank.
     * @throws IllegalStateException if {@code prohibited} is true and
     *     this call attempts to set {@code required} to true as well
     */
    public void setRequired(boolean required) {
        if (this.prohibited && required) {
            throw new IllegalStateException();
        }
        this.required = required;
        if (this.control != null) {
            this.control.setRequired(required);
        }
    }

    /**
     * @return a boolean that indicates whether this field must have a value or
     *     may be left blank
     */
    public boolean getRequired() {
        return this.required;
    }

    /**
     * @param prohibited indicates whether the user may enter a value for this
     *     field
     * @throws IllegalStateException if {@code required} is true and
     *     this call attempts to set {@code prohibited} to true as well

     */
    public void setProhibited(boolean prohibited) {
        if (this.required && prohibited) {
            throw new IllegalStateException();
        }
        this.prohibited = prohibited;
        if (this.control != null) {
            this.control.setProhibited(prohibited);
        }
    }

    /**
     * @return a boolean that indicates whether the user may enter a value for
     *     this field
     */
    public boolean getProhibited() {
        return this.prohibited;
    }

    /**
     * @param bool indicates whether the resolved field code will be an
     *     annotation or an attribute
     * @throws IllegalStateException if 'fieldCode' has been set to something
     *     other than {@code AUTO_DETECT_FIELD_CODE}
     */
    public void setUseTextareaInsteadOfTextboxForUnknownCode(boolean bool) {
        if (this.fieldCode != AUTO_DETECT_FIELD_CODE) {
            throw new IllegalStateException();
        }
        this.useTextareaInsteadOfTextboxForUnknownCode = bool;
    }

    /**
     * @return a boolean that indicates whether the resolved field code should
     *     resolve to an annotation or an attribute
     */
    public boolean getUseTextareaInsteadOfTextboxForUnknownCode() {
        return this.useTextareaInsteadOfTextboxForUnknownCode;
    }

    /**
     * A method that may be called by peer tags to ensure that no type-specific
     * {@code Validator} is attached to validate the 'owned' control. 
     * This call will remove any type-specific validator that has already been
     * attached.
     */
    public void overrideSpecificValidation() {
        // FIXME: temporary debugging statement:
        pageContext.getServletContext().log("overriding specific validation");
        this.overrideSpecificValidation = true;
        if (this.control != null) {
            this.control.setValidator(null);
            if (this.control instanceof MultiboxHtmlControl) {
                ((MultiboxHtmlControl) this.control).setBoxValidator(null);
            }
        }
    }

    /**
     * @param bool indicates whether this {@code SampleField} should use
     *     the {@code Lab}'s default copyright notice as its default
     *     value.  This shouldn't be set if {@code fieldCode} is not
     *     {@code SampleTextInfo.COPYRIGHT_NOTICE}.
     */
    public void setGetDefaultCopyrightNoticeFromLabContext(boolean bool) {
        this.getDefaultCopyrightNoticeFromLabContext = bool;
    }

    /** Setter for the 'formatFieldForSearchResults' property. */
    public void setFormatFieldForSearchResults(boolean format) {
        this.formatFieldForSearchResults = format;
    }

    /** Getter for the 'formatFieldForSearchResults' property. */
    public boolean getFormatFieldForSearchResults() {
        return this.formatFieldForSearchResults;
    }

    /** Setter for the 'styleClassForSearchMatch' property. */
    public void setStyleClassForSearchMatch(String styleClass) {
        this.styleClassForSearchMatch = styleClass;
    }

    /** Getter for the 'styleClassForSearchMatch' property. */
    public String getStyleClassForSearchMatch() {
        return this.styleClassForSearchMatch;
    }

    /**
     * Gets the ID that will be assigned to the owned element
     * 
     * @return the ID that will be assigned to the owned element, as a
     *         {@code String}, or {@code null} if an ID will be automatically
     *         generated
     */
    public String getOwnedElementId() {
        return ownedElementId;
    }

    /**
     * Sets an ID that should be assigned to the owned element
     * 
     * @param  ownedElementId the {@code String} that should be used as the
     *         owned element's ID, or {@code null} if an ID should be generated
     *         automatically
     */
    public void setOwnedElementId(String ownedElementId) {
        this.ownedElementId = ownedElementId;
    }

    /**
     * Gets the assigned height of the owned control.  The effect of this
     * parameter on the field's presentation depends on the field code and other
     * settings.
     * 
     * @return the overriding height to assign to the owned control, or -1 if
     *         no overriding height has been assigned.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets a height to assign to the owned control
     * 
     * @param  height the height to assign; units depend on field code, but
     *         typically are lines of text when the owned control recognizes a
     *         height parameter at all
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the assigned width of the owned control.  The effect of this
     * parameter on the field's presentation depends on the field code and other
     * settings.
     * 
     * @return the overriding width to assign to the owned control, or -1 if
     *         no overriding width has been assigned.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets a width to assign to the owned control
     * 
     * @param  width the width to assign; units depend on field code, but
     *         typically are characters when the owned control recognizes a
     *         width parameter at all
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Implements {@code ExtraHtmlAttributeAccepter} by either passing
     * them to the owned control's implementation or putting them in the
     * {@code extraAttributes} map so that they may be added to the
     * 'owned' control when it is instantiated.
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

    /**
     * {@inheritDoc}.  This version obtains references to the
     * relevant containing contexts and creates the owned {@code control}; if
     * necessary, it also creates alternative controls of different types to
     * accommodate an as-yet unknown field code
     * 
     * @throws IllegalArgumentException if fieldCode is invalid, if
     *     'formatFieldForSearchResults' is set but no
     *     'styleClassForSearchMatch' is indicated or if 'displayValueOnly' is
     *     set to true but the 'fieldCode' is an annotation or attribute
     *     (possibly multivalued type)
     * @throws IllegalStateException if this tag is not nested within the body
     *     of a {@code SampleContext} or if 'fieldCode' has been set to
     *     {@code AUTO_DETECT_FIELD_CODE} and no
     *     {@code SampleTextContext} or {@code SampleFieldContext}
     *     can be found, or if
     *     {@code getDefaultCopyrightNoticeFromLabContext} is 'true' and
     *     {@code fieldCode} is not
     *     {@code SampleTextBL.COPYRIGHT_NOTICE} or if
     *     {@code formatFieldForSearchResults} is set to true and this
     *     tag's page tag is not a {@code SearchResultsPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.formatFieldForSearchResults) {
            if (!(getPage() instanceof SearchResultsPage)) {
                throw new IllegalStateException();
            } else if (this.styleClassForSearchMatch == null) {
                throw new IllegalArgumentException();
            }
        }

        if (this.displayValueOnly && (SampleTextBL.isAnnotation(this.fieldCode)
                || SampleTextBL.isAttribute(this.fieldCode))) {
            // FIXME: why isn't this allowed?
            throw new IllegalArgumentException();
        }

        // find the SampleContext
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            // Can't find the required context from ancestry
            throw new IllegalStateException();
        }

        // find the ValidationContext
        this.validationContext
                = findRealAncestorWithClass(this, ValidationContext.class);

        if (this.fieldCode == SampleField.AUTO_DETECT_FIELD_CODE) {
            
            // find the most specific context from which the fieldCode may be 
            // automatically detected
            
            this.sampleFieldContext
                    = findRealAncestorWithClass(this, SampleFieldContext.class);
            if (this.sampleFieldContext == null) {
                this.sampleTextContext = findRealAncestorWithClass(
                                this, SampleTextContext.class);
                if (this.sampleTextContext == null) {
                    // Can't find the required context from ancestry
                    throw new IllegalStateException();
                }
            } else {
                // ensure that sampleTextContext is valid
                this.sampleTextContext = this.sampleFieldContext;
            }
        }

        if (this.getDefaultCopyrightNoticeFromLabContext) {
            if (this.fieldCode != SampleTextBL.COPYRIGHT_NOTICE) {
                throw new IllegalStateException();
            }
            // find the LabContext
            this.labContext
                    = findRealAncestorWithClass(this, LabContext.class);
            if (this.labContext == null) {
                // Can't find the required context from ancestry
                throw new IllegalStateException();
            }
        }

        // create the 'owned' control and set it up
        this.control = getNewHtmlControlForFieldCode(this.fieldCode);
        this.control.setId(getOwnedElementId());
        registerOwnedElement(this.control);
        
        // Set properties common to all control types
        if (this.displayAsLabel || this.displayValueOnly) {
            this.control.setDisplayAsLabel(true);
            this.control.setEditable(false);
        } else if (this.fieldCode != AUTO_DETECT_FIELD_CODE) {
            if ((pageContext.getRequest().getParameter(
                        this.uneditableFieldsParamName) != null)
                    && !SampleWorkflowBL.isFieldCorrectable(this.fieldCode,
                            Integer.parseInt(
                                    pageContext.getRequest().getParameter(
                                            this.uneditableFieldsParamName)))) {
                /* 
                 * The page is in correction mode and the field is
                 * uncorrectable: make it uneditable
                 */
                this.control.setEditable(false);
            } else {
                
                // The field is an editable input box
                this.control.setEditable(true);
                this.control.setRequired(this.required);
                this.control.setProhibited(this.prohibited);
            }
        } // else we don't know yet how to set the properties

        // In autodetect mode we don't know yet what type of control we need
        if (this.fieldCode == SampleField.AUTO_DETECT_FIELD_CODE) {
            createAlternativeControls();
        }

        return rc;
    }

    /**
     * <p>
     * Instantiates an {@code HtmlControl} that is appropriate for the
     * given 'fieldCode'.  This control type is consistent with the type
     * expected by other methods in class.  When its 'value' type differs from
     * that expected by {@link
     * org.recipnet.site.shared.db.SampleInfo#extractValue(int)
     * SampleInfo.extractValue()}, {@code translateObjectFromContainer()}
     * must account for the differences. Likewise when its 'value' type differs
     * from {@link org.recipnet.site.shared.db.SampleInfo#updateValue(int,
     * java.lang.Object) SampleInfo.updateValue()},
     * {@code translateObjectFromControl()} must account for the differences.
     * </p><p>
     * This method does the minimum amount of set-up that is required during
     * the {@code REGISTRATION_PHASE} and will be invoked before that
     * phase is complete.  Setting of properties to affect validation and
     * display is handled by {@link
     * SampleField#modifyHtmlControlForFieldCode(HtmlControl, int)
     * modifyHtmlControlForFieldCode()} which must be called before the
     * {@code PROCESSING_PHASE} begins.
     * </p>
     * 
     * @param  fieldCode the field code for the sample field type for which an
     *         appropriate control is requested
     *         
     * @return an {@code HtmlControl} of a type suitable for use displaying and
     *         modifying a sample field of the specified type; may require
     *         configuration of size, formatting parameters, etc.
     */
    protected HtmlControl getNewHtmlControlForFieldCode(int fieldCode) {
        HtmlControl newControl;
        
        switch (fieldCode) {
            // SampleDataInfo fields
            case SampleDataInfo.A_FIELD:
            case SampleDataInfo.B_FIELD:
            case SampleDataInfo.C_FIELD:
            case SampleDataInfo.ALPHA_FIELD:
            case SampleDataInfo.BETA_FIELD:
            case SampleDataInfo.GAMMA_FIELD:
            case SampleDataInfo.DCALC_FIELD:
            case SampleDataInfo.T_FIELD:
            case SampleDataInfo.V_FIELD:
            case SampleDataInfo.RF_FIELD:
            case SampleDataInfo.RWF_FIELD:
            case SampleDataInfo.RF2_FIELD:
            case SampleDataInfo.RWF2_FIELD:
            case SampleDataInfo.GOOF_FIELD:
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                newControl = new DoubleTextboxHtmlControl();
                ((DoubleTextboxHtmlControl) newControl).setGroupingUsed(false);
                break;
            case SampleDataInfo.SPGP_FIELD:
            case SampleDataInfo.COLOR_FIELD:
                newControl = new TextboxHtmlControl();
                break;
            case SampleDataInfo.Z_FIELD:
                newControl = new IntegerTextboxHtmlControl();
                break;
            case SampleDataInfo.SUMMARY_FIELD:
                newControl = new TextareaHtmlControl();
                break;
            case SampleDataInfo.PROVIDER_ID_FIELD:
                ProviderSelector ps = new ProviderSelector();
                
                ps.setRestrictToUsersProvider(true);
                ps.setOnlyActive(true);
                
                newControl = ps;
                break;
    
            // annotations and attributes -- that should be in a textbox
            case SampleTextBL.EMPIRICAL_FORMULA:
            case SampleTextBL.STRUCTURAL_FORMULA:
            case SampleTextBL.MOIETY_FORMULA:
            case SampleTextBL.EMPIRICAL_FORMULA_DERIVED:
            case SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION:
            case SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT:
            case SampleTextBL.PROVIDER_REFERENCE_NUMBER:
            case SampleTextBL.CSD_REFCODE:
            case SampleTextBL.ICSD_COLLECTION_CODE:
            case SampleTextBL.PDB_ENTRY_NUMBER:
            case SampleTextBL.TEXT_CONTRIBUTOR:
            case SampleTextBL.SHORT_DESCRIPTION:
            case SampleTextBL.CAS_REGISTRY_NUMBER:
            case SampleTextBL.COMMON_NAME:
            case SampleTextBL.TRADE_NAME:
            case SampleTextBL.KEYWORD:
            case SampleTextBL.CRYSTALLOGRAPHER_NAME:
            case SampleTextBL.SAMPLE_PROVIDER_NAME:
            case SampleTextBL.RAW_DATA_URL:
            case SampleTextBL.SMILES_FORMULA:
            case SampleTextBL.IUPAC_NAME:
            case SampleTextBL.MISC_COMMENTS:
            case SampleTextBL.CHANGE_TO_DATA_FILES_IN_REPOSITORY:
                MultiboxHtmlControl multitextbox = new MultiboxHtmlControl();
                
                multitextbox.setBoxType(MultiboxHtmlControl.TEXTBOX);
                newControl = multitextbox;
                
                break;
    
            // annotations and attributes -- that should be in a textarea
            case SampleTextBL.OTHER:
            case SampleTextBL.CITATION_OF_A_PUBLICATION:
            case SampleTextBL.SUPERSEDED_BY_ANOTHER_SAMPLE:
            case SampleTextBL.SUPERSEDES_ANOTHER_SAMPLE:
            case SampleTextBL.DUPLICATE_STRUCTURE_OF_ANOTHER_SAMPLE:
            case SampleTextBL.MISC_REFERENCE_TO_ANOTHER_SAMPLE:
            case SampleTextBL.DETERMINATION_PROCEDURE:
            case SampleTextBL.DATA_QUALITY:
            case SampleTextBL.TWIN_EXPLANATION:
            case SampleTextBL.LAYMANS_EXPLANATION:
            case SampleTextBL.INCOMPLETENESS_EXPLANATION:
            case SampleTextBL.INELIGIBLE_FOR_RELEASE:
            case SampleTextBL.COPYRIGHT_NOTICE:
                MultiboxHtmlControl multitextarea = new MultiboxHtmlControl();

                multitextarea.setBoxType(MultiboxHtmlControl.TEXTAREA);
                newControl = multitextarea;
                
                break;
    
            // SampleInfo fields
            case SampleInfo.ID:
            case SampleInfo.LAB_ID:
            case SampleInfo.STATUS:
            case SampleInfo.MOST_RECENT_STATUS:
            case SampleInfo.HISTORY_ID:
            case SampleInfo.MOST_RECENT_HISTORY_ID:
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
                newControl = new LabelHtmlControl();
                break;
            case SampleInfo.LOCAL_LAB_ID:
                newControl = new TextboxHtmlControl();
                break;
    
            // special annotation
            case SampleTextBL.PREFERRED_NAME:
                // during the FETCHING_PHASE this listbox will be populated
                // with the possible sample names
                newControl = new ListboxHtmlControl();
                break;
    
            // The text type is unknown, but because it's coming from a 
            // SampleTextContext it is not multi-valued.  Determine whether it
            // is a textbox or a multibox based on information provided by
            // SampleTextContext.getTextType() or by the
            // 'useTextareaInsteadOfTextboxForUnknownCode' property
            case SampleField.AUTO_DETECT_FIELD_CODE:
                if (this.sampleTextContext == null) {
                    newControl = null;
                    assert false;
                } else if (this.sampleTextContext.getTextType()
                        == SampleTextBL.INVALID_TYPE) {
                    // it is unclear what type of field this represents,
                    // chose a textbox or textarea based on the
                    // useTextareaInsteadOfTextboxForUnknownCode property
                    newControl = (this.useTextareaInsteadOfTextboxForUnknownCode
                            ? new TextareaHtmlControl()
                            : new TextboxHtmlControl());
                } else {
                    // the text type can be determined and we can figure out
                    // what type of box WOULD be used if it were multi-valued
                    MultiboxHtmlControl multivalVersion
                         = (MultiboxHtmlControl) getNewHtmlControlForFieldCode(
                                this.sampleTextContext.getTextType());
                    // return a single-valued equivalent control
                    newControl = (multivalVersion.getBoxType()
                            == MultiboxHtmlControl.TEXTBOX
                                    ? new TextboxHtmlControl()
                                    : new TextareaHtmlControl());
                }
                break;

           default:
               throw new IllegalArgumentException("fieldCode=" + fieldCode);
        }
        
        return newControl;
    }

    /**
     * Creates and registers additional owned controls that may be used instead
     * of the main control.  This becomes important when the field type is
     * initially unknown.  This method should be used in the
     * {@code REGISTRATION_PHASE} if used at all.  If it is used, then one
     * of {@link #releaseAlternativeControls()}, {@link #useDoubleControl()},
     * or {@link #useIntControl()} should be invoked at the beginning of the
     * {@code FETCHING_PHASE}.
     */
    private void createAlternativeControls() {
        String controlId = this.control.getId();
        
        this.doubleAlternativeControl = new DoubleTextboxHtmlControl();
        this.doubleAlternativeControl.setId(controlId);
        this.doubleAlternativeControl.setDisplayAsLabel(
                this.control.getDisplayAsLabel());
        this.doubleAlternativeControl.setEditable(this.control.getEditable());
        this.doubleAlternativeControl.setGroupingUsed(false);
        registerOwnedElement(doubleAlternativeControl);
        
        this.intAlternativeControl = new IntegerTextboxHtmlControl();
        this.intAlternativeControl.setId(controlId);
        this.intAlternativeControl.setDisplayAsLabel(
                this.control.getDisplayAsLabel());
        this.intAlternativeControl.setEditable(this.control.getEditable());
        registerOwnedElement(intAlternativeControl);
    }
    
    /**
     * {@inheritDoc}; this version resolves the field code if necessary,
     * configures the owned control for the field code, gets the value of this
     * field from the {@code SampleInfo} object from the most immediate
     * {@code SampleContext}, {@code SampleFieldContext}, or
     * {@code SampleTextContext} as appropriate, and updates the 'owned'
     * control to reflect that value (unless the 'owned' control has a higher
     * priority value).
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        int effectiveFieldCode;

        // resolve field code if needed and possible
        if (this.fieldCode != AUTO_DETECT_FIELD_CODE) {
            effectiveFieldCode = this.fieldCode;
        } else {
            assert (this.resolvedFieldCode == UNRESOLVED);
            
            if (this.sampleFieldContext != null) {
                if (this.sampleFieldContext.getSampleField() != null) {
                    this.resolvedFieldCode
                            = this.sampleFieldContext.getSampleField().getFieldCode();
                }
            } else {
                
                // the following condition must have been satisfied so as not to
                // throw an exception during the registration phase
                assert (this.sampleTextContext != null);
                
                if (this.sampleTextContext.getTextType()
                        != SampleTextBL.INVALID_TYPE) {
                    this.resolvedFieldCode
                            = this.sampleTextContext.getTextType();
                }
            }
            
            chooseControl(this.resolvedFieldCode);
            effectiveFieldCode = this.resolvedFieldCode;
            
            // Configure editability, if possible, though it's a little late
            if (this.resolvedFieldCode != UNRESOLVED) {
                String uneditableFieldsParam
                        = pageContext.getRequest().getParameter(
                                this.uneditableFieldsParamName);
                
                if ((uneditableFieldsParam != null)
                        && !SampleWorkflowBL.isFieldCorrectable(
                                effectiveFieldCode,
                                Integer.parseInt(uneditableFieldsParam))) {
                    /* 
                     * The page is in correction mode and the field is
                     * uncorrectable: ensure that it is uneditable
                     */
                    this.control.setEditable(false);
                } else if (this.control.getEditable()) {
                    
                    /*
                     * The field is an editable input box; editability was
                     * determined at registration time, and not counterindicated
                     * by the correctability analysis above
                     */
                    
                    this.control.setRequired(this.required);
                    this.control.setProhibited(this.prohibited);
                }
            } // else don't know what to do, so do nothing
        }

        // determine whether full validation is required before we invoke
        // modifyHtmlControlForFieldCode()
        if (this.overrideSpecificValidationUnlessParamNameMatches != null) {
            Pattern pattern = Pattern.compile(
                    this.overrideSpecificValidationUnlessParamNameMatches);
            Enumeration names = pageContext.getRequest().getParameterNames();
            boolean match = false;
            
            while (names.hasMoreElements()) {
                if (pattern.matcher((String) names.nextElement()).matches()) {
                    match = true;
                    break;
                }
            }
            if (match == false) {
                overrideSpecificValidation();
            }
        }

        // Add any extra attributes
        if (this.extraAttributes != null) {
            for (Entry<String, String> entry : this.extraAttributes.entrySet()) {
                this.control.addExtraHtmlAttribute(
                        entry.getKey(), entry.getValue());
            }
            this.extraAttributes = null;
        }

        // Set all fieldCode-specific properties (including formatters and such)
        modifyHtmlControlForFieldCode(this.control, effectiveFieldCode);
        
        // Update the owned control's value based on fetched results
        
        ValuePriorityOverrideContext overrideContext
                = findRealAncestorWithClass(
                            this, ValuePriorityOverrideContext.class);
        
        try {
            int valuePriority = ((overrideContext == null)
                    ? HtmlControl.EXISTING_VALUE_PRIORITY
                    : overrideContext.getFetchedValuePriority(this.control));
            int nullValuePriority = ((overrideContext == null)
                    ? HtmlControl.DEFAULT_VALUE_PRIORITY
                    : overrideContext.getFetchedNullPriority(this.control));
            
            // Throws NPE if the context returns a null container:
            Object suggestedValue = translateObjectFromContainer(
                    getValueFromContainerContext(), effectiveFieldCode);
            
            /*
             * The failedValidation flag of the owned control should never be
             * set true before the control's fetching-phase evaluation, which
             * hasn't happened yet
             */
            assert !this.control.getFailedValidation();
            
            // Set the fetched value, if its priority is sufficient
            if (this.control.setValue(
                    suggestedValue, valuePriority, nullValuePriority)) {
                
                // Clear any parsing failure flag if we successfully set a value
                this.control.setUnparseable(false);
            }
        } catch (NullPointerException npe) {
            
            // The context provided a null container object.
            
            if (this.control.getValue() == null) {
                /*
                 * No field information is available for modification,
                 * but because the user has not entered any value,
                 * there is no problem.  This may occur during an HTTP
                 * GET when the SampleTextContext or SampleFieldContext
                 * is dependent upon another field whose value is
                 * unavailable until POSTed.
                 */
            } else {
                /*
                 * a value has been entered for a SampleTextContext or
                 * SampleFieldContext that does not provide field
                 * information -- this is an error case
                 */
                this.control.setFailedValidation(true);
                setErrorFlag(VALUE_ENTERED_FOR_NULL_CONTEXT);
                if (validationContext != null) {
                    validationContext.reportValidationError();
                }
            }
        }

        // Special Handling: to set the default value for the copyright notice
        if (this.getDefaultCopyrightNoticeFromLabContext
                && (this.fieldCode == SampleTextBL.COPYRIGHT_NOTICE)) {
            this.control.setValue(new String[] {
                    labContext.getLabInfo().defaultCopyrightNotice },
                    HtmlControl.DEFAULT_VALUE_PRIORITY);
        }

        // Special Handling: to include options for the preferred name listbox
        if (this.fieldCode == SampleTextBL.PREFERRED_NAME) {
            ((ListboxHtmlControl) this.control).addOption(true, "", "");
            if (this.sampleContext.getSampleInfo() == null) {
                this.setErrorFlag(NO_SAMPLE_NAMES);
                this.control.setVisible(false);
            } else {
                Collection<SampleTextInfo> sampleNames
                        = SampleTextBL.getSampleNames(
                                this.sampleContext.getSampleInfo(), true);
                if (sampleNames.size() == 0) {
                    this.setErrorFlag(NO_SAMPLE_NAMES);
                    this.control.setVisible(false);
                } else {
                    for (SampleTextInfo nextName : sampleNames) {
                        ((ListboxHtmlControl) this.control).addOption(
                                true, nextName.value, nextName.value);
                    }
                }
            }
            ((ListboxHtmlControl) this.control).setInitialValue("");
        }
        
        // done
        return rc;
    }

    /**
     * Chooses which, if either, of the alternative controls to use for this
     * sample field; invokes {@link #releaseAlternativeControls()},
     * {@link #useDoubleControl()}, or {@link #useIntControl()} as appropriate
     * for the specified field code.  This method is intended for use when
     * alternative controls have been created to support late binding of the
     * owned control (normally in a field code autodetection situation).
     * 
     * @param  effectiveFieldCode the field code effective for this sample
     *         field; typically the {@code resolvedFieldCode} when this field
     *         is autodetecting the field code
     */
    private void chooseControl(int effectiveFieldCode) {
        switch (effectiveFieldCode) {
            case SampleDataInfo.A_FIELD:
            case SampleDataInfo.B_FIELD:
            case SampleDataInfo.C_FIELD:
            case SampleDataInfo.ALPHA_FIELD:
            case SampleDataInfo.BETA_FIELD:
            case SampleDataInfo.GAMMA_FIELD:
            case SampleDataInfo.DCALC_FIELD:
            case SampleDataInfo.T_FIELD:
            case SampleDataInfo.V_FIELD:
            case SampleDataInfo.RF_FIELD:
            case SampleDataInfo.RWF_FIELD:
            case SampleDataInfo.RF2_FIELD:
            case SampleDataInfo.RWF2_FIELD:
            case SampleDataInfo.GOOF_FIELD:
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                useDoubleControl();
                break;
            case SampleDataInfo.Z_FIELD:
                useIntControl();
                break;
            default:
                releaseAlternativeControls();
                break;
        }
    }

    private void releaseAlternativeControls() {
        unregisterOwnedElement(this.intAlternativeControl);
        unregisterOwnedElement(this.doubleAlternativeControl);
        this.intAlternativeControl = null;
        this.doubleAlternativeControl = null;
    }

    private void useDoubleControl() {
        if (getOwnedElementId() == null) {
            allowIdUpdate(this.doubleAlternativeControl);
        }
        unregisterOwnedElement(this.intAlternativeControl);
        unregisterOwnedElement(this.control);
        this.control = this.doubleAlternativeControl;
        this.intAlternativeControl = null;
        this.doubleAlternativeControl = null;
    }

    private void useIntControl() {
        if (getOwnedElementId() == null) {
            allowIdUpdate(this.intAlternativeControl);
        }
        unregisterOwnedElement(this.doubleAlternativeControl);
        unregisterOwnedElement(this.control);
        this.control = this.intAlternativeControl;
        this.intAlternativeControl = null;
        this.doubleAlternativeControl = null;
    }

    /**
     * A method that should be called during the {@code FETCHING_PHASE}
     * after the field code is determined to finalize all properties on the
     * 'owned' {@code HtmlControl} that are based on the field code.  This
     * method must be called before 'owned' controls complete the
     * {@code FETCHING_PHASE} because at that point they've already
     * validated any parsed values.
     * 
     * @param control the 'owned' control (or null, in which case this method
     *     does nothing).  This may be either the multi-valued control or the
     *     single-valued control when 'fieldCode' indicates a possibly
     *     multi-valued field.
     * @param fieldCode the field code; must not be
     *     {@code AUTO_DETECT_FIELD_CODE} but instead should be whatever
     *     the resolved field code is
     */
    protected void modifyHtmlControlForFieldCode(HtmlControl control,
            int fieldCode) {
        if (control == null) {
            return;
        }
        // the use of NA (not applicable) makes it easier to read the parameter
        // lists for initializeHtmlControl
        final int NA = -1;
    
        switch (fieldCode) {
            case SampleDataInfo.A_FIELD:
            case SampleDataInfo.B_FIELD:
            case SampleDataInfo.C_FIELD:
                initializeNumericHtmlControl(control, 8, 3, 1, null);
                break;
            case SampleDataInfo.ALPHA_FIELD:
            case SampleDataInfo.BETA_FIELD:
            case SampleDataInfo.GAMMA_FIELD:
                initializeNumericHtmlControl(control, 8, 2, 1, 178);
                break;
            case SampleDataInfo.SPGP_FIELD:
                initializeHtmlControl(control, 12, NA, NA, 13);
                if (!this.overrideSpecificValidation) {
                    control.setValidator(SampleField.SPGP_VALIDATOR);
                }
                break;
            case SampleDataInfo.DCALC_FIELD:
                initializeNumericHtmlControl(control, 8, 3, 0, null);
                break;
            case SampleDataInfo.COLOR_FIELD:
                initializeHtmlControl(control, 12, NA, NA, 20);
                break;
            case SampleDataInfo.Z_FIELD:
                initializeNumericHtmlControl(control, 4, NA, 1, null);
                break;
            case SampleDataInfo.T_FIELD:
                initializeNumericHtmlControl(control, 8, 1, -273.2, null);
                break;
            case SampleDataInfo.V_FIELD:
                initializeNumericHtmlControl(control, 12, 2, 0, null);
                break;
            case SampleDataInfo.RF_FIELD:
            case SampleDataInfo.RWF_FIELD:
            case SampleDataInfo.RF2_FIELD:
            case SampleDataInfo.RWF2_FIELD:
                initializeNumericHtmlControl(control, 6, 4, 0, null);
                break;
            case SampleDataInfo.GOOF_FIELD:
                initializeNumericHtmlControl(control, 6, 3, 0, null);
                break;
            case SampleDataInfo.SUMMARY_FIELD:
                initializeHtmlControl(control, 40, 2, NA, 80);
                break;
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                initializeNumericHtmlControl(control, 10, 3, 1, null);
                break;
            case SampleDataInfo.PROVIDER_ID_FIELD:
                break;
            case SampleTextBL.STRUCTURAL_FORMULA:
            case SampleTextBL.PROVIDER_REFERENCE_NUMBER:
                initializeHtmlControl(control, 32, 1, 1, 128);
                break;
            case SampleTextBL.MOIETY_FORMULA:
                initializeHtmlControl(control, 32, 1, 1, 128);
                if (!this.overrideSpecificValidation) {
                    if (control instanceof MultiboxHtmlControl) {
                        ((MultiboxHtmlControl) control).setBoxValidator(
                                SampleField.MOIETY_FORMULA_VALIDATOR);
                    } else {
                        control.setValidator(
                                SampleField.MOIETY_FORMULA_VALIDATOR);
                    }
                }
                break;
            case SampleTextBL.EMPIRICAL_FORMULA:
            case SampleTextBL.EMPIRICAL_FORMULA_DERIVED:
            case SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT:
                initializeHtmlControl(control, 32, 1, 1, 128);
                if (!this.overrideSpecificValidation) {
                    if (control instanceof MultiboxHtmlControl) {
                        ((MultiboxHtmlControl) control).setBoxValidator(
                                SampleField.EMPIRICAL_FORMULA_VALIDATOR);
                    } else {
                        control.setValidator(
                                SampleField.EMPIRICAL_FORMULA_VALIDATOR);
                    }
                }
                break;
            case SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION:
                initializeHtmlControl(control, 32, 1, 1, 128);
                if (!this.overrideSpecificValidation) {
                    if (control instanceof MultiboxHtmlControl) {
                        ((MultiboxHtmlControl) control).setBoxValidator(
                          SampleField.EMPIRICAL_FORMULA_SINGLE_ION_VALIDATOR);
                    } else {
                        control.setValidator(
                          SampleField.EMPIRICAL_FORMULA_SINGLE_ION_VALIDATOR);
                    }
                }
                break;
            case SampleTextBL.CSD_REFCODE:
            case SampleTextBL.ICSD_COLLECTION_CODE:
            case SampleTextBL.PDB_ENTRY_NUMBER:
                initializeHtmlControl(control, 12, 1, 1, 128);
                break;
            case SampleTextBL.TEXT_CONTRIBUTOR:
            case SampleTextBL.SHORT_DESCRIPTION:
            case SampleTextBL.CAS_REGISTRY_NUMBER:
                initializeHtmlControl(control, 32, 1, 1, 128);
                break;
            case SampleTextBL.COMMON_NAME:
            case SampleTextBL.TRADE_NAME:
            case SampleTextBL.KEYWORD:
            case SampleTextBL.CRYSTALLOGRAPHER_NAME:
            case SampleTextBL.SAMPLE_PROVIDER_NAME:
                initializeHtmlControl(
                        control, 32, 1, MultiboxHtmlControl.NO_LIMIT, 128);
                break;
            case SampleTextBL.OTHER:
                initializeHtmlControl(
                        control, 32, 4, MultiboxHtmlControl.NO_LIMIT, 128);
                break;
            case SampleTextBL.CITATION_OF_A_PUBLICATION:
                initializeHtmlControl(control, 64, 2, 1, NA);
                break;
            case SampleTextBL.RAW_DATA_URL:
                initializeHtmlControl(control, 72, 1, 1, NA);
                break;
            case SampleTextBL.SUPERSEDED_BY_ANOTHER_SAMPLE:
            case SampleTextBL.SUPERSEDES_ANOTHER_SAMPLE:
                initializeHtmlControl(control, 32, 2, 1, NA);
                break;
            case SampleTextBL.DUPLICATE_STRUCTURE_OF_ANOTHER_SAMPLE:
            case SampleTextBL.MISC_REFERENCE_TO_ANOTHER_SAMPLE:
            case SampleTextBL.DETERMINATION_PROCEDURE:
            case SampleTextBL.DATA_QUALITY:
            case SampleTextBL.TWIN_EXPLANATION:
                initializeHtmlControl(control, 72, 4, 1, NA);
                break;
            case SampleTextBL.LAYMANS_EXPLANATION:
            case SampleTextBL.INCOMPLETENESS_EXPLANATION:
                initializeHtmlControl(
                        control, 72, 4, MultiboxHtmlControl.NO_LIMIT, NA);
                break;
            case SampleTextBL.INELIGIBLE_FOR_RELEASE:
                initializeHtmlControl(
                        control, 72, 4, MultiboxHtmlControl.NO_LIMIT, NA);
                break;
            case SampleTextBL.SMILES_FORMULA:
            case SampleTextBL.IUPAC_NAME:
                initializeHtmlControl(control, 32, 1, 1, NA);
                break;
            case SampleTextBL.MISC_COMMENTS:
                initializeHtmlControl(control, 20, 1, 1, NA);
                break;
            case SampleTextBL.COPYRIGHT_NOTICE:
                initializeHtmlControl(
                        control, 72, 4, MultiboxHtmlControl.NO_LIMIT, NA);
                break;
            case SampleTextBL.CHANGE_TO_DATA_FILES_IN_REPOSITORY:
                initializeHtmlControl(control, 32, 1, 1, NA);
                break;
            case SampleInfo.ID:
                initializeHtmlControl(control, 12, NA, NA, NA);
                break;
            case SampleInfo.LAB_ID:
                initializeHtmlControl(control, 12, NA, NA, NA);
                break;
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
                break;
            case SampleInfo.LOCAL_LAB_ID:
                initializeHtmlControl(control, 32, NA, NA, 32);
                if (!this.overrideSpecificValidation) {
                    control.setValidator(SampleField.LOCAL_LAB_ID_VALIDATOR);
                }
                break;
            case SampleInfo.STATUS:
                initializeHtmlControl(control, NA, NA, NA, NA);
                break;
            case SampleInfo.MOST_RECENT_STATUS:
                initializeHtmlControl(control, NA, NA, NA, NA);
                break;
            case SampleInfo.HISTORY_ID:
                initializeHtmlControl(control, 12, NA, NA, NA);
                break;
            case SampleInfo.MOST_RECENT_HISTORY_ID:
                initializeHtmlControl(control, 12, NA, NA, NA);
                break;
            case SampleTextBL.PREFERRED_NAME:
                break;
            case SampleField.UNRESOLVED:
                if (this.useTextareaInsteadOfTextboxForUnknownCode) {
                    initializeHtmlControl(control, 32, 2, 1, NA);
                } else {
                    initializeHtmlControl(control, 32, NA, 1, 128);
                }
                break;
            default:
                if (SampleTextBL.isLocalAttribute(fieldCode)) {
                    // LocalTrackingAttributes, like other attributes, are a
                    // multibox with a limit of one box
                    initializeHtmlControl(control, 32, 1, 1, 128);
                    break;
                }
                throw new IllegalArgumentException("fieldCode=" + fieldCode);
        }
    }

    /**
     * A private helper method that updates the provided numerically-oriented
     * {@code HtmlControl} to reflect the provided property values.
     * 
     * @param control the {@code HtmlControl} to configure
     * @param width width of field (maps to size for
     *     {@code TextboxHtmlControl} subclasses)
     * @param fractionalDigits the number of fractional digits for the control
     *     to display if it is a {@code DoubleTextboxHtmlControl}
     * @param minValue a {@code Number} that, if non-{@code null} represents
     *        the minimum value that the control may take 
     * @param maxValue a {@code Number} that, if non-{@code null} represents
     *        the maximum value that the control may take 
     */
    private void initializeNumericHtmlControl(HtmlControl control,
            int width, int fractionalDigits, Number minValue, Number maxValue) {
        /*
         * the use of NA (not applicable) makes it easier to read the parameter
         * list for initializeHtmlControl, and is consistent with other method
         * implementations
         */
        final int NA = -1;
        
        initializeHtmlControl(control, width, NA, 1, NA);
        if (control instanceof DoubleTextboxHtmlControl) {
            DoubleTextboxHtmlControl dControl =
                    (DoubleTextboxHtmlControl) control;
            
            if (minValue != null) {
                dControl.setMinValue(minValue.doubleValue());
            }
            if (maxValue != null) {
                dControl.setMaxValue(maxValue.doubleValue());
            }
            dControl.setMaxFractionalDigits(fractionalDigits);
            dControl.setMinFractionalDigits(fractionalDigits);
        } else if (control instanceof IntegerTextboxHtmlControl) {
            IntegerTextboxHtmlControl iControl =
                (IntegerTextboxHtmlControl) control;
        
            if (minValue != null) {
                iControl.setMinValue(minValue.intValue());
            }
            if (maxValue != null) {
                iControl.setMaxValue(maxValue.intValue());
            }
        } else {
            throw new IllegalArgumentException("Non-numeric control");
        }
    }

    /**
     * A private helper method that updates the provided
     * {@code HtmlControl} to reflect the provided property values.  If
     * maxLength is less than zero, it will be interpreted as meaning that
     * there should be no limit.
     * 
     * @param control the {@code HtmlControl} to configure
     * @param width the desired width of the field (maps to size for
     *     {@code TextboxHtmlControl}; maps to columns for
     *     {@code TextareaHtmlControl} and
     *     {@code MultiboxHtmlControl})
     * @param height the desired height of the field (maps to rows for
     *     {@code TextareaHtmlControl} and
     *     {@code MultiboxHtmlControl})
     * @param maxBoxes only used for {@code MultiboxHtmlControl}
     * @param maxLength maxLength of a field
     */
    private void initializeHtmlControl(HtmlControl control,
            int width, int height, int maxBoxes, int maxLength) {
        
        // Handle height and width overrides
        if (this.width > 0) {
            width = this.width;
        }
        if (this.height > 0) {
            height = this.height;
        }
        
        // Set properties appropriately for the control type
        if (control instanceof AbstractTextHtmlControl) {
            if (maxLength > 0) {
                ((AbstractTextHtmlControl) control).setMaxLength(maxLength);
            }
            if (control instanceof TextboxHtmlControl) {
                ((TextboxHtmlControl) control).setSize(width);
            } else if (control instanceof TextareaHtmlControl) {
                ((TextareaHtmlControl) control).setColumns(width);
                ((TextareaHtmlControl) control).setRows(height);
            }
        } else if (control instanceof MultiboxHtmlControl) {
            MultiboxHtmlControl mbControl = (MultiboxHtmlControl) control;
            
            if (height > 1) {
                mbControl.setRows(height);
            }
            mbControl.setColumns(width);
            mbControl.setMaxBoxCount(maxBoxes);
            if (maxLength > 0) {
                mbControl.setMaxLength(maxLength);
            }
        }
    }

    /**
     * Gets the value of the field indicated by 'fieldCode' from the most
     * specific context available.
     * 
     * @return the field value as an {@code Object} of appropriate class
     *
     * @throws NullPointerException if the container context returns
     *         {@code null}
     */
    private Object getValueFromContainerContext() {
        Object valueFromContainer = null;
        
        if (this.sampleFieldContext != null) {
            SampleFieldRecord sfr = this.sampleFieldContext.getSampleField();
            
            if (sfr.getValue() instanceof SampleTextInfo) {
                valueFromContainer = ((SampleTextInfo) sfr.getValue()).value;
            } else {
                valueFromContainer = sfr.getValue();
            }

        } else if (this.sampleTextContext != null) {
            valueFromContainer
                    = this.sampleTextContext.getSampleTextInfo().value;
        } else if (this.sampleContext != null) {
            valueFromContainer
                    = this.sampleContext.getSampleInfo().extractValue(
                            this.fieldCode);
        }
        
        return valueFromContainer;
    }

    /**
     * Performs any special display-side wrapping.  For some fields, the value
     * is stored differently than displayed, as in the case of field values
     * that are stored as integer contstants.  If a field altered by this
     * method could ever be editable, there must be symmetric code within
     * {@code translateObjectFromControl()}.
     * 
     * @param objectFromContainer an object, returned from a call to
     *     {@code SampleInfo.extractField()} using the provided
     *     {@code fieldCode} or a {@code String} from a single
     *     {@code SampleTextInfo} if 'considerValueAsSingleTextInfo' is
     *     true.
     * @param fieldCode the fieldCode representing the type of field whose
     *     value is given as the 'objectFromContainer'.  In the event that this
     *     fieldCode represents an annotation or attribute,
     *     'considerValueAsSingleTextInfo' will indicate whether this is a
     *     single {@code String} or a {@code String[]} (array).
     * @return a version of 'objectFromContainer' that is of the type of the
     *     'value' property of the 'owned' control.  Whenever the
     *     'considerValueAsSingleTextInfo' parameter is true and the
     *     'fieldCode' represents an annotation or attribute, the return
     *     value is a {@code String}.  In the case where
     *     'considerValueAsSingleTextInfo' is false and the 'fieldCode'
     *     represents an annotation or attribute, an array of
     *     {@code String}'s is returned.  In other cases the value
     *     returned (as well as the value supplied) is the complex Object type
     *     for the java primitive used to store the particular field within a
     *     {@code SampleInfo} or {@code SampleDataInfo} object.  In
     *     cases where the 'owned' control cannot have a value of {@code null},
     *     this method should return an empty string instead.
     */
    // FIXME: docs are incorrect about translation of attributes and annotations
    private Object translateObjectFromContainer(Object objectFromContainer,
            int fieldCode) throws JspException {
        switch (fieldCode) {
            case SampleInfo.ID:
            case SampleInfo.LAB_ID:
            case SampleInfo.HISTORY_ID:
            case SampleInfo.MOST_RECENT_HISTORY_ID:
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
                // because none of these fields are meant to be editable as
                // SampleFields, they use LabelHtmlControl objects even though
                // their values are actually Integers.
                return (objectFromContainer == null
                        ? "" : objectFromContainer.toString());
            case SampleInfo.STATUS:
            case SampleInfo.MOST_RECENT_STATUS:
                // status codes should be displayed as localized strings
                try {
                    return LanguageHelper.extract(
                            super.pageContext.getServletContext()
                                   ).getStatusString(
                                   ((Integer) objectFromContainer).intValue(),
                                   super.pageContext.getRequest().getLocales(),
                                   true);
                } catch (IOException ex) {
                    throw new JspException(ex);
                } catch (ResourceNotFoundException ex) {
                    throw new JspException(ex);
                }
            case SampleDataInfo.PROVIDER_ID_FIELD:
                if (!this.displayValueOnly) {
                    // This String representation of an Integer will be fed to
                    // a ProviderSelector control
                    return String.valueOf(objectFromContainer);
                } else if (objectFromContainer != null) {
                    // The returned value will be displayed; return the
                    // provider's name
                    ProviderInfo provider
                            = RequestCache.getProviderInfo(
                                  pageContext.getRequest(),
                                  ((Integer) objectFromContainer).intValue());
                    if (provider == null) {
                        CoreConnector cc = CoreConnector.extract(
                                pageContext.getServletContext());
                        try {
                            provider = cc.getSiteManager().getProviderInfo(
                                   ((Integer) objectFromContainer).intValue());
                        } catch (OperationFailedException ex) {
                            throw new JspException(ex);
                        } catch (RemoteException ex) {
                            cc.reportRemoteException(ex);
                            throw new JspException(ex);
                        }
                    }
                    return provider.name;
                } else {
                    return "";  // FIXME: Should this be an error case?
                }
            case SampleTextBL.PREFERRED_NAME:
                
                /*
                 * preferred name is special in that it's control expects a
                 * value of type String no matter how many values are present
                 * (though only one is ever expected).
                 * 
                 * Depending on how this field obtained the value, it might be
                 * either a String or a String[] (or null).
                 */
                if (objectFromContainer == null) {
                    
                    // the empty string represents listbox options that when
                    // selected indicate that no selection has been made:
                    return "";
                } else if (objectFromContainer instanceof String[]) {
                    String[] objectAsArray = (String[]) objectFromContainer;

                    return ((objectAsArray.length == 0)
                            ? "" : objectAsArray[0]);
                } else {
                    return objectFromContainer;
                }
            default:
                return objectFromContainer;
        }
    }

    /**
     * {@inheritDoc}; this version gets the value stored in the 'owned' control
     * and copies it to the {@code SampleInfo} object recieved from the most
     * immediate {@code SampleContext}.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.control.getFailedValidation()
                || !getPage().areAllFieldsValid()) {
            // do nothing, some field is invalid and therefore it's
            // inappropriate to modify the container object
        } else if ((this.fieldCode == AUTO_DETECT_FIELD_CODE)
                && (this.resolvedFieldCode == UNRESOLVED)) {
            // field is unknown, do nothing
        } else if (this.control.getEditable()) {
            
            // This is a valid, editable, persisted field; apply any changes
            
            if (this.fieldCode == AUTO_DETECT_FIELD_CODE) {
                if ((this.sampleFieldContext != null)
                        && (this.sampleFieldContext.getSampleField() != null)) {
                    this.sampleFieldContext.getSampleField().setValue(
                            translateObjectFromControl(
                                    this.control.getValue(),
                                    this.resolvedFieldCode, true));
                } else if ((this.sampleTextContext != null)
                        && (this.sampleTextContext.getTextType()
                                != SampleTextBL.INVALID_TYPE)) {
                    SampleTextInfo info
                            = this.sampleTextContext.getSampleTextInfo();
                    
                    if (info != null) {
                        info.value = (String) this.translateObjectFromControl(
                                    this.control.getValue(),
                                    this.resolvedFieldCode, true);
                    }
                } else {
                    // in order for the fieldCode to have been resolved the
                    // SampleFieldContext must have provided a non-null value
                    // or the SampleTextContext must have provided a valid
                    // text type during the fetching phase.
                    assert (this.resolvedFieldCode == UNRESOLVED);
                }
            } else if (this.sampleContext.getSampleInfo() != null) {
                // update the value of the SampleInfo field referenced by
                // fieldCode
                this.sampleContext.getSampleInfo().updateValue(this.fieldCode,
                        this.translateObjectFromControl(
                                this.control.getValue(), this.fieldCode,
                                false));
            }
            
            /*
             * Note: there is a fall-through case in which the control is
             * configured editable, but no context is available to update; that
             * case is silently ignored in this version.
             */
        }
        
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * Performs any special storage-side wrapping.  For some fields, the value
     * is displayed differently than it is stored, as in the case of field
     * values that are stored as integer constants.
     * 
     * @param objectFromField the object returned by the call
     *     {@code getValue()} on the 'owned' control
     * @param fieldCode the fieldCode for this {@code SampleField}
     * @param considerValueAsSingleTextInfo a boolean to indicate whether a
     *     'fieldCode' for an attribute/annotation is to refer to a single
     *     {@code String} or an array of {@code String}s.
     *     
     * @return an object of the type expected by the container for the given
     *     fieldCode.  In the event that the fieldCode refers to an
     *     annotation/attribute, whether a single {@code String} or an
     *     array of {@code String}s is returned is based on
     *     {@code considerValueAsSingleTextInfo}.
     */
    private Object translateObjectFromControl(Object objectFromField,
            int fieldCode, boolean considerValueAsSingleTextInfo)
            throws JspException {
        switch (fieldCode) {
            case SampleDataInfo.PROVIDER_ID_FIELD:
                // A String representation of the provider's id is made
                // available by the ProviderSelector
                if (objectFromField == null) {
                    return new Integer(ProviderInfo.INVALID_PROVIDER_ID);
                } else {
                    return new Integer((String) objectFromField);
                }
            case SampleTextBL.PREFERRED_NAME:
                // the value of the control for a PREFERRED_NAME will always be
                // a single string, but sometimes an array of strings is
                // expected by the container
                if ("".equals(this.control.getValue())) {
                    // the empty string represents listbox options that when
                    // selected indicates that no selection has been made (ie,
                    // "choose a sample name..." or "No names available")
                    return null;
                } else {
                    return (considerValueAsSingleTextInfo
                            ? objectFromField
                            : new String[] { (String) objectFromField });
                }
            case SampleTextBL.EMPIRICAL_FORMULA:
            case SampleTextBL.MOIETY_FORMULA:
            case SampleTextBL.EMPIRICAL_FORMULA_DERIVED:
            case SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION:
            case SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT:
                if (objectFromField == null) {
                    return null;
                }
                if (considerValueAsSingleTextInfo) {
                    try {
                        return ChemicalFormulaBL.getFormulaString(
                                ChemicalFormulaBL.getCanonicalFormula(
                                        ChemicalFormulaBL.parseFormula(
                                              (String) objectFromField)));
                    } catch (Exception ex) {
                        // formla is not parsible, store what the user entered
                        // and rely on other validation code to possibly
                        // prevent storage
                        return objectFromField;
                    }
                } else {
                    String[] formulae = (String[]) objectFromField;
                    String[] formattedFormulae = new String[formulae.length];
                    for (int i = 0; i < formulae.length; i ++) {
                        try {
                            formattedFormulae[i]
                                    = ChemicalFormulaBL.getFormulaString(
                                         ChemicalFormulaBL.getCanonicalFormula(
                                            ChemicalFormulaBL.parseFormula(
                                                formulae[i])));
                        } catch (Exception ex) {
                            // for some reason we couldn't get a String
                            // representation of the canonical form of the
                            // formula, store what the user entered
                            formattedFormulae[i] = formulae[i];
                        }
                    }
                    return formattedFormulae;
                }
            default:
                return objectFromField;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws JspException,
            IOException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        
        assert (this.control != null);
        
        HtmlFormatter formatter = getFormatter();
        
        if (formatter != null) {
            this.control.setFormatter(formatter);
        }
        
        if (this.displayValueOnly) {
            
            /*
             * We don't want to render persistence code, only the value itself;
             * we'll handle that part manually, after the body
             */ 
            unregisterOwnedElement(this.control);
        }
        
        return rc;
    }

    /**
     * A method to determine the desired formatting for this tag. This tag must
     * not be called before the {@code FETCHING_PHASE}.
     * 
     * @return an {@code HtmlFormatter} that may be applied to a single value
     *         for the given fieldCode.
     */
    private HtmlFormatter getFormatter() {
        Collection<FieldMatchInfo> fieldMatchInfos = null;
        
        if (this.formatFieldForSearchResults) {
            SampleInfo si = this.sampleContext.getSampleInfo();
            SearchParams sp
                    = ((SearchResultsPage) getPage()).getSearchParams();
            SearchMatches sm = RequestCache.getSearchMatches(
                    pageContext.getRequest(), sp, si);
            
            if (sm == null) {
                sm = new SearchMatches(sp, si);
                RequestCache.putSearchMatches(
                        pageContext.getRequest(), sm, sp, si);
            }
            if (this.fieldCode == AUTO_DETECT_FIELD_CODE) {
                if (SampleTextBL.isAnnotation(this.resolvedFieldCode)
                        || SampleTextBL.isAttribute(this.resolvedFieldCode)) {
                    if (this.sampleFieldContext != null) {
                        fieldMatchInfos = sm.getMatchesForField(
                                this.sampleFieldContext.getSampleTextInfo());
                    } else if (this.sampleTextContext != null) {
                        fieldMatchInfos = sm.getMatchesForField(
                                this.sampleTextContext.getSampleTextInfo());
                    } else {
                        assert false;
                    }
                } else {
                    fieldMatchInfos = sm.getMatchesForField(this.fieldCode);
                }
            } else {
                fieldMatchInfos = sm.getMatchesForField(this.fieldCode);
            }
        }
        
        switch ((this.fieldCode == AUTO_DETECT_FIELD_CODE)
                    ? this.resolvedFieldCode : this.fieldCode) {
            case SampleTextBL.EMPIRICAL_FORMULA:
            case SampleTextBL.EMPIRICAL_FORMULA_DERIVED:
            case SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION:
            case SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT:
            case SampleTextBL.STRUCTURAL_FORMULA:
            case SampleTextBL.MOIETY_FORMULA:
                if ((fieldMatchInfos != null) && !fieldMatchInfos.isEmpty()) {
                    return new ChemicalFormulaHtmlFormatter(fieldMatchInfos,
                            this.styleClassForSearchMatch);
                } else {
                    return CHEMICAL_FORMULA_FORMATTER;
                }
            case SampleTextBL.IUPAC_NAME:
                if ((fieldMatchInfos != null) && !fieldMatchInfos.isEmpty()) {
                    return new IupacNameWrapHtmlFormatter(fieldMatchInfos,
                            this.styleClassForSearchMatch);
                } else {
                    return IUPAC_NAME_WRAP_FORMATTER;
                }
            case SampleTextBL.RAW_DATA_URL:
                if ((fieldMatchInfos != null) && !fieldMatchInfos.isEmpty()) {
                    return new UrlHtmlFormatter(fieldMatchInfos,
                            this.styleClassForSearchMatch);
                } else {                    
                    return URL_FORMATTER;
                }
            case SampleDataInfo.SPGP_FIELD:
                if ((fieldMatchInfos != null) && !fieldMatchInfos.isEmpty()) {
                    return new SpaceGroupSymbolHtmlFormatter(fieldMatchInfos,
                            this.styleClassForSearchMatch);
                } else {
                    return SPGP_FORMATTER;
                }
            default:
                if ((fieldMatchInfos != null) && !fieldMatchInfos.isEmpty()) {
                    return new FieldMatchFormatter(fieldMatchInfos,
                            this.styleClassForSearchMatch);
                } else {
                    return null;
                }
        }
    }

    /**
     * {@inheritDoc}; this version outputs the value of the field represented by
     * the field code if 'displayValueOnly' is {@code true}.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws JspException,
            IOException {
        if (this.displayValueOnly) {
            out.print(this.control.generateHtmlToDisplayValueOnly(
                    this.control.getValue()));
        }
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient
     * fields to this object from {@code source} if it is a
     * {@code SampleField}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose
     *     transient fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        SampleField src = (SampleField) source;
        
        // to ensure that values are propagated to owned element, each setter
        // method has to be called
        this.setFieldCode(src.fieldCode);
        if (this.displayAsLabel != src.displayAsLabel) {
            this.setDisplayAsLabel(src.displayAsLabel);
        }
        if (this.required != src.required) {
            // if the value of 'required' changed
            if (src.required && (this.prohibited != src.prohibited)) {
                // if 'required' is changing to true, and the value of
                // 'prohibited' is changing as well, be sure that 'prohibited'
                // gets changed first because changing 'required' to true will
                // throw an exception if 'prohibited' is also true
                this.setProhibited(src.prohibited);
            }
            this.setRequired(src.required);
        }
        if (this.prohibited != src.prohibited) {
            this.setProhibited(src.prohibited);
        }
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation
     * delegates to the superclass then updates any references to 'owned'
     * controls or referenced ancestor tags using the 'map' parameter that was
     * populated by the superclass' implementation as well as the caller, then
     * makes a deep copy of any complex modifiable member variables before
     * returning the deep copy.
     * 
     * @param newId {@inheritDoc}
     * @param map {@inheritDoc}
     * 
     * @return {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleField dc = (SampleField) super.generateCopy(newId, map);
        
        dc.control = (HtmlControl) map.get(this.control);
        dc.doubleAlternativeControl = (DoubleTextboxHtmlControl)
                map.get(this.doubleAlternativeControl);
        dc.intAlternativeControl = (IntegerTextboxHtmlControl)
                map.get(this.intAlternativeControl);
        
        if (dc.doubleAlternativeControl != null) {
            dc.doubleAlternativeControl.setId(dc.control.getId());
        }
        if (dc.intAlternativeControl != null) {
            dc.intAlternativeControl.setId(dc.control.getId());
        }
        
        dc.labContext = (LabContext) map.get(this.labContext);
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);
        dc.sampleFieldContext
                = (SampleFieldContext) map.get(this.sampleFieldContext);
        
        return dc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode
                | SampleField.translateOwnedErrorCode(this.control);
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Returns an errorCode that represents the errorCode reported by the
     * 'owned' control.  Equivalent error codes are determined by matching
     * code names, so that, for example, if an owned
     * {@code DoubleTextboxHtmlControl} reports an errorCode containing
     * the bit for }DoubleTextBoxHtmlControl.VALUE_IS_TOO_HIGH},
     * the errorCode {@code SampleField.VALUE_IS_TOO_HIGH} is returned,
     * regardless of whether its value is identical.
     * 
     * @param control the 'owned' control whose error codes are being
     *     translated to SampleField error codes
     *     
     * @return an int, representing all the error flags on the 'owned' control
     *     translated to the corresponding error codes for
     *     {@code SampleField}.
     */
    private static int translateOwnedErrorCode(HtmlControl control) {
        int errorCode = NO_ERROR_REPORTED;
        
        errorCode |= translateErrorBit(control.getErrorCode(),
            HtmlControl.REQUIRED_VALUE_IS_MISSING,
            REQUIRED_VALUE_IS_MISSING);
        errorCode |= translateErrorBit(control.getErrorCode(),
            HtmlControl.PROHIBITED_VALUE_IS_PRESENT,
            PROHIBITED_VALUE_IS_PRESENT);
        errorCode |= translateErrorBit(control.getErrorCode(),
            HtmlControl.VALIDATOR_REJECTED_VALUE,
            VALIDATOR_REJECTED_VALUE);

        if (control instanceof AbstractTextHtmlControl) {
            errorCode |= translateErrorBit(control.getErrorCode(),
                    AbstractTextHtmlControl.VALUE_IS_TOO_LONG,
                    VALUE_IS_TOO_LONG);
            if (control instanceof DoubleTextboxHtmlControl) {
                errorCode |= translateErrorBit(control.getErrorCode(),
                        DoubleTextboxHtmlControl.VALUE_IS_NOT_A_NUMBER,
                        VALUE_IS_NOT_A_NUMBER);
                errorCode |= translateErrorBit(control.getErrorCode(),
                        DoubleTextboxHtmlControl.VALUE_IS_TOO_LOW,
                        VALUE_IS_TOO_LOW);
                errorCode |= translateErrorBit(control.getErrorCode(),
                        DoubleTextboxHtmlControl.VALUE_IS_TOO_HIGH,
                        VALUE_IS_TOO_HIGH);
            } else if (control instanceof IntegerTextboxHtmlControl) {
                errorCode |= translateErrorBit(control.getErrorCode(),
                        IntegerTextboxHtmlControl.VALUE_IS_NOT_A_NUMBER,
                        VALUE_IS_NOT_A_NUMBER);
                errorCode |= translateErrorBit(control.getErrorCode(),
                        IntegerTextboxHtmlControl.VALUE_IS_TOO_LOW,
                        VALUE_IS_TOO_LOW);
                errorCode |= translateErrorBit(control.getErrorCode(),
                        IntegerTextboxHtmlControl.VALUE_IS_TOO_HIGH,
                        VALUE_IS_TOO_HIGH);
            }
        } else if (control instanceof MultiboxHtmlControl) {
            errorCode |= translateErrorBit(control.getErrorCode(),
                    MultiboxHtmlControl.VALUE_IS_TOO_LONG,
                    VALUE_IS_TOO_LONG);
        }
            
        return errorCode;
    }

    /**
     * Translates an error bit from the specified tag-dependent error code to
     * the specified bit
     * 
     * @param  sourceCode the error code bearing the error bit to translate
     * @param  sourceFlag the error flag to translate; assumed to have exactly
     *         one non-zero bit
     * @param  targetFlag the error flag to translate to
     * 
     * @return either {@code targetFlag} or zero, depending on whether the
     *         specified {@code sourceFlag} bit is on in {@code sourceCode}
     */
    private static int translateErrorBit(int sourceCode, int sourceFlag,
            int targetFlag) {
        return (targetFlag * ((sourceCode & sourceFlag) / sourceFlag));
    }
}
