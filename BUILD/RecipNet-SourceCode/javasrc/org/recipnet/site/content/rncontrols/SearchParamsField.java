/*
 * Reciprocal Net project
 * 
 * SearchParamsField.java
 * 
 * 19-Aug-2004: cwestnea wrote first draft
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 25-Feb-2005: midurbin rewrote onFetchingPhaseBeforeBody() and
 *              onProcessingPhaseAfterBody(), added various helper methods
 *              to support rewritten SearchParams
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 14-Mar-2005: midurbin added search-by-atom support
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 01-Apr-2005: jobollin changed Integer.parseInt() + autoboxing to
 *              Integer.valueOf() for converting a String to an Integer
 *              (task 1566)
 * 26-Apr-2005: midurbin added initial values based on user preferences for
 *              some search fields in onRegistrationPhaseBeforeBody() and
 *              updated onFetchingPhaseBeforeBody() not to overwrite then when
 *              no SearchParms object exists
 * 04-May-2005: ekoperda removed obsolete search-by-unit-cell functionality
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 12-Jul-2005: ekoperda updated class to match spec changes in SpaceGroupSC
 *              and improved error flag propagation from owned controls
 * 28-Oct-2005: midurbin added preference based initialization when fieldCode
 *              is USE_USER_PROVIDER
 * 29-Nov-2005: jobollin changed onRegistrationPhaseBeforeBody() to use Element
 *              instead of ElementsBL and removed unused imports
 * 01-Feb-2006: jobollin accommodated changes to RadioButtonHtmlControl
 * 30-May-2006: jobollin added generics and reformatted
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.Element;
import org.recipnet.common.controls.AbstractTextHtmlControl;
import org.recipnet.common.controls.CheckboxHtmlControl;
import org.recipnet.common.controls.DoubleTextboxHtmlControl;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.IntegerTextboxHtmlControl;
import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.common.controls.RadioButtonHtmlControl;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.search.AnnotationSC;
import org.recipnet.site.shared.search.AtomCountSC;
import org.recipnet.site.shared.search.AttributeSC;
import org.recipnet.site.shared.search.ChemicalNameSC;
import org.recipnet.site.shared.search.CrystallographerSC;
import org.recipnet.site.shared.search.EmpiricalFormulaSC;
import org.recipnet.site.shared.search.FinishedStatusSC;
import org.recipnet.site.shared.search.FormulaAtomSC;
import org.recipnet.site.shared.search.IdNumberSC;
import org.recipnet.site.shared.search.KeywordSC;
import org.recipnet.site.shared.search.LabSC;
import org.recipnet.site.shared.search.LocalHoldingSC;
import org.recipnet.site.shared.search.LocalLabIdSC;
import org.recipnet.site.shared.search.MaxZSC;
import org.recipnet.site.shared.search.MinZSC;
import org.recipnet.site.shared.search.MoietyFormulaSC;
import org.recipnet.site.shared.search.NonRetractedStatusSC;
import org.recipnet.site.shared.search.NumberComparisonSC;
import org.recipnet.site.shared.search.NumericSampleDataSC;
import org.recipnet.site.shared.search.ProviderSC;
import org.recipnet.site.shared.search.SampleDataRangeSC;
import org.recipnet.site.shared.search.SampleIdSC;
import org.recipnet.site.shared.search.SampleProviderNameSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintGroup;
import org.recipnet.site.shared.search.SmilesFormulaSC;
import org.recipnet.site.shared.search.SpaceGroupSC;
import org.recipnet.site.shared.search.StructuralFormulaSC;
import org.recipnet.site.shared.search.TextComparisonSC;
import org.recipnet.site.shared.validation.SpaceGroupSymbolValidator;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * This tag, coupled with an implementation of {@code SearchParamsContext},
 * allows fields from a {@code SearchParams} object to be exposed on a JSP. This
 * tag is responsible for determining the control type, validation rules and
 * some other properties for the field it is assigned to expose. The field is
 * indicated by the {@code fieldCode} parameter.
 */
public class SearchParamsField extends HtmlPageElement implements ErrorSupplier {
    
    /**
     * Possible field codes, representing data within {@code SearchParams}
     */
    public static final int SORT_ORDER = 1;

    public static final int REQUIRE_LOCAL_HOLDING = 2;

    public static final int REQUIRE_TERMINAL_STATUS = 3;

    public static final int REQUIRE_NON_RETRACTED = 4;

    public static final int LAB_SEARCH = 5;

    public static final int PROVIDER = 6;

    public static final int SAMPLE_ID = 7;

    public static final int LOCAL_SAMPLE_ID = 8;

    public static final int GENERIC_SAMPLE_ID = 9;

    public static final int CHEM_NAME = 10;

    public static final int STRUCT_FORMULA = 11;

    public static final int EMPIR_FORMULA = 12;

    public static final int MOIETY_FORMULA = 13;

    public static final int SMILES = 14;

    public static final int CRYSTALLOGRAPHER = 15;

    public static final int SAMPLE_PROVIDER_NAME = 16;

    public static final int KEYWORD = 17;

    public static final int MATCH_ANY_KEYWORD = 18;

    public static final int MATCH_ALL_KEYWORD = 19;

    public static final int SPACE_GROUP = 32;

    public static final int MIN_Z = 33;

    public static final int MAX_Z = 34;

    public static final int TEMP = 35;

    public static final int RANGE_TEMP = 36;

    public static final int VOLUME = 37;

    public static final int RANGE_VOLUME = 38;

    public static final int DENSITY = 39;

    public static final int RANGE_DENSITY = 40;

    public static final int USE_USER_PROVIDER = 41;

    public static final int SEARCH_ATOM_TYPE = 42;

    public static final int SEARCH_ATOM_OPERATOR = 43;

    public static final int SEARCH_ATOM_COUNT = 44;

    public static final int SEARCH_ATOMS_FROM_EF = 45;

    public static final int SEARCH_ATOMS_FROM_EFD = 46;

    public static final int SEARCH_ATOMS_FROM_EFLS = 47;

    public static final int SEARCH_ATOMS_FROM_EFSI = 48;

    /**
     * Error flags defined for an {@code ErrorSupplier} implementation. These
     * flags match those on the textbox, checkbox, and listbox controls.
     */
    public static final int REQUIRED_VALUE_IS_MISSING = 1 << 0;

    public static final int PROHIBITED_VALUE_IS_PRESENT = 1 << 1;

    public static final int VALIDATOR_REJECTED_VALUE = 1 << 2;

    public static final int VALUE_IS_TOO_LONG = 1 << 3;

    public static final int DISABLED_VALUE_SELECTED = 1 << 4;

    public static final int UNKNOWN_VALUE_SELECTED = 1 << 5;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return UNKNOWN_VALUE_SELECTED;
    }

    /**
     * Indicates which field of the {@code SearchParams} object this control
     * represents. This variable is initialized to {@code SORT_ORDER} by
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
     * updated by {@code copyTransientPropertiesFrom()}. Defaults to
     * {@code true}.
     */
    private boolean editable;

    /**
     * Indicates that this field may not be editable and should be displayed as
     * plain text. This variable is initialized by {@code reset()} and may be
     * set by its 'setter' method, {@code setDisplayAsLabel()} or altered when
     * {@code fieldCode} is set. This variable is 'transient' in that its value
     * may change from phase to phase and may be updated by
     * {@code copyTransientPropertiesFrom()}. Defaults to {@code false}
     */
    private boolean displayAsLabel;

    /**
     * This is a reference to the owned {@code HtmlControl}. Its type is
     * derived from the {@code fieldCode} and it is initialized during the
     * {@code REGISTRATION_PHASE}.
     */
    private HtmlControl control;

    /**
     * Optional attribute, defaults to null; specifies the {@code tabIndex}
     * attribute on the {@code &lt;input&gt;} tag.
     */
    private String tabIndex;

    /**
     * An optional property that is only useful when the fieldCode is
     * {@code SEARCH_ATOM_TYPE}, {@code SEARCH_ATOM_OPERATOR} or
     * {@code SEARCH_ATOM_COUNT}. To match up these three controls to a single
     * atom count search they must be provided the same 'groupKey'. For all
     * other field codes this property is ignored.
     */
    private String groupKey;

    /**
     * The {@code SearchPage} in which this control is nested. Initialized in
     * {@code reset()} and discovered in {@code onRegistrationPhaseBeforeBody()}.
     */
    private SearchPage searchPage;

    /**
     * If the {@code USE_USER_PROVIDER} checkbox is checked then we will need to
     * get the provider id of the current user.
     */
    private UserContext userContext;

    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = SORT_ORDER;
        this.editable = true;
        this.displayAsLabel = false;
        this.control = null;
        this.tabIndex = null;
        this.groupKey = null;
        this.searchPage = null;
        this.userContext = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * @param tabIndex the value to put in the tabIndex attribute of this
     *        control.
     */
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    /**
     * @param key the 'groupKey' to group {@code SEARCH_ATOM_TYPE},
     *        {@code SEARCH_ATOM_OPERATOR} and {@code SEARCH_ATOM_COUNT}
     *        {@code SearchParamsField} objects.
     */
    public void setGroupKey(String key) {
        this.groupKey = key;
    }

    /**
     * @return the 'groupKey' to group {@code SEARCH_ATOM_TYPE},
     *         {@code SEARCH_ATOM_OPERATOR} and {@code SEARCH_ATOM_COUNT}
     *         {@code SearchParamsField} objects.
     */
    public String getGroupKey() {
        return this.groupKey;
    }

    /**
     * Sets the fieldCode. This attribute should come before 'editable' or
     * 'displayAsLabel' as it will set default values based on the fieldCode. By
     * default, all fieldCodes result in 'displayAsLabel' being set to true.
     * 
     * @param fieldCode one of the static field codes defined on
     *        {@code SearchParamsField}, indicating which field within the
     *        {@code SearchParams} object this tag will expose.
     * @throws IllegalArgumentException if an unknown fieldCode has been given.
     * @throws IllegalStateException if the control is already been set and the
     *         user tries to change the field code
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case SORT_ORDER:
            case REQUIRE_LOCAL_HOLDING:
            case REQUIRE_TERMINAL_STATUS:
            case REQUIRE_NON_RETRACTED:
            case LAB_SEARCH:
            case PROVIDER:
            case SAMPLE_ID:
            case LOCAL_SAMPLE_ID:
            case GENERIC_SAMPLE_ID:
            case CHEM_NAME:
            case STRUCT_FORMULA:
            case EMPIR_FORMULA:
            case MOIETY_FORMULA:
            case SMILES:
            case CRYSTALLOGRAPHER:
            case SAMPLE_PROVIDER_NAME:
            case KEYWORD:
            case MATCH_ANY_KEYWORD:
            case MATCH_ALL_KEYWORD:
            case SPACE_GROUP:
            case MIN_Z:
            case MAX_Z:
            case TEMP:
            case RANGE_TEMP:
            case VOLUME:
            case RANGE_VOLUME:
            case DENSITY:
            case RANGE_DENSITY:
            case USE_USER_PROVIDER:
            case SEARCH_ATOM_TYPE:
            case SEARCH_ATOM_OPERATOR:
            case SEARCH_ATOM_COUNT:
            case SEARCH_ATOMS_FROM_EF:
            case SEARCH_ATOMS_FROM_EFD:
            case SEARCH_ATOMS_FROM_EFLS:
            case SEARCH_ATOMS_FROM_EFSI:
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
    }

    /**
     * @return one of the static field codes defined on
     *         {@code SearchParamsField}, indicating which field within the
     *         {@code SearchParams} object this tag will expose.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * Sets the 'editable' property, indicating whether this field may be
     * edited. If set to true, {@code displayAsLabel} is set to false.
     * 
     * @param editable whether or not this field should be editable
     * @throws IllegalArgumentException if this {@code SearchParamsField} has
     *         been assigned a {@code fieldCode} that may not be editable and
     *         this call attempts to set {@code editable} to true or if
     *         {@code displayAsLabel} is true.
     */
    public void setEditable(boolean editable) {
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
     * Implements {@code ErrorSupplier}. This implementation propagates error
     * flags that might be present on owned controls to their equivalent error
     * flags on this control.
     */
    public int getErrorCode() {
        if (this.control != null) {
            int ec = this.control.getErrorCode();

            if ((ec & HtmlControl.REQUIRED_VALUE_IS_MISSING) != 0) {
                setErrorFlag(REQUIRED_VALUE_IS_MISSING);
            }
            if ((ec & HtmlControl.PROHIBITED_VALUE_IS_PRESENT) != 0) {
                setErrorFlag(PROHIBITED_VALUE_IS_PRESENT);
            }
            if ((ec & HtmlControl.VALIDATOR_REJECTED_VALUE) != 0) {
                setErrorFlag(VALIDATOR_REJECTED_VALUE);
            }
            if (this.control instanceof TextboxHtmlControl) {
                if ((ec & AbstractTextHtmlControl.VALUE_IS_TOO_LONG) != 0) {
                    setErrorFlag(VALUE_IS_TOO_LONG);
                }
            }
            if (this.control instanceof ListboxHtmlControl) {
                if ((ec & ListboxHtmlControl.DISABLED_VALUE_SELECTED) != 0) {
                    setErrorFlag(DISABLED_VALUE_SELECTED);
                }
                if ((ec & ListboxHtmlControl.UNKNOWN_VALUE_SELECTED) != 0) {
                    setErrorFlag(UNKNOWN_VALUE_SELECTED);
                }
            }
        }
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation
     * instantiates its 'owned' element, finds the nearest {@code SearchPage}
     * and delegates back to the superclass.
     * 
     * @param pageContext the current {@code PageContext}; may be useful to
     *        superclass or 'owned' elements.
     * @return the return code is taken from the superclass' implementation of
     *         this method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class.
     * @throws IllegalStateException if this tag is not within a
     *         {@code SearchPage}.
     * @throws JspException if an exception is encountered during processing for
     *         this phase.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(
            @SuppressWarnings("hiding") PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        UserPreferences preferences
                = (UserPreferences) pageContext.getSession().getAttribute(
                        "preferences");
        DoubleTextboxHtmlControl doubleControl;
        IntegerTextboxHtmlControl integerControl;
        TextboxHtmlControl textControl;
        RadioButtonHtmlControl radioControl;
        ListboxHtmlControl listboxControl;

        this.searchPage = findRealAncestorWithClass(this, SearchPage.class);
        if (this.searchPage == null) {
            // This tag can't be used without SearchPage
            throw new IllegalStateException();
        }
        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        
        switch (this.fieldCode) {
            case SORT_ORDER:
                this.control = new ListboxHtmlControl();
                break;
            case REQUIRE_LOCAL_HOLDING:
                this.control = new CheckboxHtmlControl();
                if (UserBL.getPreferenceAsBoolean(UserBL.Pref.SEARCH_LOCAL,
                        preferences)) {
                    this.control.setInitialValue(Boolean.TRUE);
                }
                break;
            case REQUIRE_TERMINAL_STATUS:
                this.control = new CheckboxHtmlControl();
                if (UserBL.getPreferenceAsBoolean(UserBL.Pref.SEARCH_FINISHED,
                        preferences)) {
                    this.control.setInitialValue(Boolean.TRUE);
                }
                break;
            case REQUIRE_NON_RETRACTED:
                this.control = new CheckboxHtmlControl();
                if (UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_NON_RETRACTED, preferences)) {
                    this.control.setInitialValue(Boolean.TRUE);
                }
                break;
            case LAB_SEARCH:
                this.control = new LabSelector();
                break;
            case PROVIDER:
                this.control = new ProviderSelector();
                break;
            case SAMPLE_ID:
                integerControl = new IntegerTextboxHtmlControl();
                integerControl.setRequired(false);
                this.control = integerControl;
                break;
            case LOCAL_SAMPLE_ID:
            case GENERIC_SAMPLE_ID:
                textControl = new TextboxHtmlControl();
                textControl.setMaxLength(32);
                textControl.setSize(10);
                textControl.setRequired(false);
                this.control = textControl;
                break;
            case CHEM_NAME:
            case STRUCT_FORMULA:
            case EMPIR_FORMULA:
            case MOIETY_FORMULA:
            case SMILES:
                textControl = new TextboxHtmlControl();
                textControl.setSize(15);
                textControl.setRequired(false);
                this.control = textControl;
                break;
            case CRYSTALLOGRAPHER:
                textControl = new TextboxHtmlControl();
                textControl.setSize(25);
                textControl.setRequired(false);
                this.control = textControl;
                break;
            case SAMPLE_PROVIDER_NAME:
                textControl = new TextboxHtmlControl();
                textControl.setMaxLength(32);
                textControl.setSize(20);
                textControl.setRequired(false);
                this.control = textControl;
                break;
            case KEYWORD:
                textControl = new TextboxHtmlControl();
                textControl.setSize(15);
                textControl.setRequired(false);
                this.control = textControl;
                break;
            case MATCH_ANY_KEYWORD:
                radioControl = new RadioButtonHtmlControl();
                radioControl.setOption("any");
                this.control = radioControl;
                break;
            case MATCH_ALL_KEYWORD:
                radioControl = new RadioButtonHtmlControl();
                radioControl.setOption("all");
                this.control = radioControl;
                break;
            case VOLUME:
            case DENSITY:
                doubleControl = new DoubleTextboxHtmlControl();
                doubleControl.setMinValue(0);
                doubleControl.setSize(5);
                doubleControl.setRequired(false);
                this.control = doubleControl;
                break;
            case RANGE_TEMP:
            case RANGE_VOLUME:
            case RANGE_DENSITY:
                doubleControl = new DoubleTextboxHtmlControl();
                doubleControl.setMinValue(0);
                doubleControl.setSize(5);
                doubleControl.setRequired(false);
                this.control = doubleControl;
                this.control.setInitialValue(new Double(3.0));
                break;
            case SPACE_GROUP:
                textControl = new TextboxHtmlControl();
                textControl.setMaxLength(15);
                textControl.setRequired(false);
                textControl.setValidator(new SpaceGroupSymbolValidator());
                this.control = textControl;
                break;
            case MIN_Z:
            case MAX_Z:
                integerControl = new IntegerTextboxHtmlControl();
                integerControl.setMaxLength(11);
                integerControl.setSize(5);
                integerControl.setRequired(false);
                this.control = integerControl;
                break;
            case TEMP:
                doubleControl = new DoubleTextboxHtmlControl();
                doubleControl.setMinValue(-273.15);
                doubleControl.setSize(5);
                doubleControl.setRequired(false);
                this.control = doubleControl;
                break;
            case USE_USER_PROVIDER:
                if (userContext == null) {
                    throw new IllegalStateException();
                }
                this.control = new CheckboxHtmlControl();
                if (UserBL.getPreferenceAsBoolean(
                        UserBL.Pref.SEARCH_MY_PROVIDER, preferences)) {
                    this.control.setInitialValue(Boolean.TRUE);
                }
                break;
            case SEARCH_ATOM_TYPE:
                this.control = new ListboxHtmlControl();
                break;
            case SEARCH_ATOM_OPERATOR:
                this.control = new ListboxHtmlControl();
                break;
            case SEARCH_ATOM_COUNT:
                doubleControl = new DoubleTextboxHtmlControl();
                doubleControl.setMinValue(0);
                doubleControl.setSize(5);
                doubleControl.setRequired(false);
                this.control = doubleControl;
                break;
            case SEARCH_ATOMS_FROM_EF:
                this.control = new CheckboxHtmlControl();
                this.control.setInitialValue(Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFD:
                this.control = new CheckboxHtmlControl();
                this.control.setInitialValue(Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFLS:
                this.control = new CheckboxHtmlControl();
                this.control.setInitialValue(Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFSI:
                this.control = new CheckboxHtmlControl();
                this.control.setInitialValue(Boolean.TRUE);
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        this.control.setEditable(this.editable);
        this.control.setDisplayAsLabel(this.displayAsLabel);
        if (this.tabIndex != null) {
            this.control.addExtraHtmlAttribute("tabIndex", this.tabIndex);
        }
        super.registerOwnedElement(this.control);

        // we can't add options to a listbox until it is registered
        switch (fieldCode) {
            case SORT_ORDER:
                listboxControl = (ListboxHtmlControl) this.control;
                listboxControl.addOption(true, "Sample number",
                        String.valueOf(SearchParams.SORTBY_LOCALLABID));
                listboxControl.addOption(true, "Sample number (reverse)",
                        String.valueOf(SearchParams.SORTBY_LOCALLABID_REV));
                listboxControl.addOption(true, "Lab, then sample number",
                        String.valueOf(SearchParams.SORTBY_LABID_LOCALLABID));
                listboxControl.addOption(true,
                        "Lab, then sample number (reverse)",
                        String.valueOf(SearchParams.SORTBY_LABID_LOCALLABID_REV));
                break;
            case PROVIDER:
                ((ProviderSelector) this.control).addOption(true, "any group",
                        String.valueOf(ProviderInfo.INVALID_PROVIDER_ID));
                break;
            case LAB_SEARCH:
                ((LabSelector) this.control).addOption(true, "All Labs",
                        String.valueOf(LabInfo.INVALID_LAB_ID));
                break;
            case SEARCH_ATOM_TYPE:
                listboxControl = (ListboxHtmlControl) this.control;
                listboxControl.addOption(true, "", "");
                for (String symbol : Element.getAllSymbols()) {
                    listboxControl.addOption(true, symbol, symbol);
                }
                break;
            case SEARCH_ATOM_OPERATOR:
                LanguageHelper lh = LanguageHelper.extract(
                        this.pageContext.getServletContext());
                
                try {
                    listboxControl = (ListboxHtmlControl) this.control;
                    
                    listboxControl.addOption(true,
                            lh.getNumberComparisonOperatorString(
                                    NumberComparisonSC.GREATER_THAN,
                                    LanguageHelper.getDefaultLocales(), false),
                            String.valueOf(NumberComparisonSC.GREATER_THAN));
                    listboxControl.addOption(true,
                            lh.getNumberComparisonOperatorString(
                                    NumberComparisonSC.GREATER_THAN_OR_EQUAL_TO,
                                    LanguageHelper.getDefaultLocales(), false),
                            String.valueOf(NumberComparisonSC.GREATER_THAN_OR_EQUAL_TO));
                    listboxControl.addOption(true,
                            lh.getNumberComparisonOperatorString(
                                    NumberComparisonSC.EQUALS,
                                    LanguageHelper.getDefaultLocales(), false),
                            String.valueOf(NumberComparisonSC.EQUALS));
                    listboxControl.addOption(true,
                            lh.getNumberComparisonOperatorString(
                                    NumberComparisonSC.LESS_THAN_OR_EQUAL_TO,
                                    LanguageHelper.getDefaultLocales(), false),
                            String.valueOf(
                                    NumberComparisonSC.LESS_THAN_OR_EQUAL_TO));
                    listboxControl.addOption(true,
                            lh.getNumberComparisonOperatorString(
                                    NumberComparisonSC.LESS_THAN,
                                    LanguageHelper.getDefaultLocales(), false),
                            String.valueOf(NumberComparisonSC.LESS_THAN));

                    listboxControl.setInitialValue(
                            String.valueOf(NumberComparisonSC.EQUALS));
                } catch (ResourceNotFoundException ex) {
                    throw new JspException(ex);
                } catch (IOException ex) {
                    throw new JspException(ex);
                }
                break;
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets the
     * {@code SearchParams} from the most immediate {@code SearchPage}, then
     * uses the value of its field that corresponds to {@code fieldCode} to try
     * to update the 'owned' field's value with a priority that would override
     * default values but not user provided values. Finally the return value
     * from the superclass' method (invoked at the BEGINNING of this method) is
     * returned.
     * 
     * @return the return value of the superclass' implementation of this
     *         method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class, or if the {@code SearchPage} has no
     *         {@code SearchParams}.
     * @throws JspException if any other error is encountered while executing
     *         this method.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (this.fieldCode == SORT_ORDER) {
            // populate this SortOrder listbox if possible
            this.control.setValue(String.valueOf(searchPage.getSortOrder()),
                    HtmlControl.EXISTING_VALUE_PRIORITY);
            return rc;
        }

        SearchConstraintGroup searchConstraintGroup
                = this.searchPage.getSearchConstraintGroup();
        if (searchConstraintGroup == null) {
            // there is no group from which to populate the controls; exit
            // early
            return rc;
        }
        SearchConstraint sc = getSearchConstraintForFieldCode(this.fieldCode,
                searchConstraintGroup);

        switch (this.fieldCode) {
            // the following fields' values are booleans based on the presence
            // (or absence) of the SearchConstraint associated with them
            case REQUIRE_LOCAL_HOLDING:
            case REQUIRE_TERMINAL_STATUS:
            case REQUIRE_NON_RETRACTED:
                this.control.setValue(Boolean.valueOf(sc != null),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
        }

        /*
         * the remaining fields' values are extracted from the SearchConstraint
         * associated with them and therefore should be unmodified from their
         * default value if there is no such SearchConstraint
         */
        if (sc == null) {
            return rc;
        }

        switch (this.fieldCode) {
            case LAB_SEARCH:
                this.control.setValue(String.valueOf(((LabSC) sc).getLabId()),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case PROVIDER:
                this.control.setValue(
                        String.valueOf(((ProviderSC) sc).getProviderId()),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case SAMPLE_ID:
                this.control.setValue(new Integer(
                        ((SampleIdSC) sc).getSampleId()),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case LOCAL_SAMPLE_ID:
                this.control.setValue(
                        ((LocalLabIdSC) sc).getLocalLabIdFragment(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case GENERIC_SAMPLE_ID:
                this.control.setValue(((IdNumberSC) sc).getIdBeginning(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case CHEM_NAME:
                this.control.setValue(((ChemicalNameSC) sc).getNameFragment(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case STRUCT_FORMULA:
            case EMPIR_FORMULA:
            case MOIETY_FORMULA:
            case CRYSTALLOGRAPHER:
            case SAMPLE_PROVIDER_NAME:
                this.control.setValue(((AttributeSC) sc).getAttributeValue(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case SMILES:
                this.control.setValue(((AnnotationSC) sc).getAnnotationValue(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case KEYWORD:
                AttributeSC asc = getConstraintByIndex(sc.getChildren(),
                        this.searchPage.getKeywordIndex(), AttributeSC.class);

                if (asc != null) {
                    this.control.setValue(asc.getAttributeValue());
                }
                break;
            case MATCH_ANY_KEYWORD:
                this.control.setValue(
                        Boolean.valueOf(((KeywordSC) sc).getOperator()
                                == SearchConstraintGroup.OR),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case MATCH_ALL_KEYWORD:
                this.control.setValue(
                        Boolean.valueOf(((KeywordSC) sc).getOperator()
                                == SearchConstraintGroup.AND),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case TEMP:
            case VOLUME:
            case DENSITY:
                this.control.setValue(new Double(
                        ((SampleDataRangeSC) sc).getValue()),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case RANGE_TEMP:
            case RANGE_VOLUME:
            case RANGE_DENSITY:
                this.control.setValue(new Double(
                        ((SampleDataRangeSC) sc).getMarginOfError()),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case SPACE_GROUP:
                this.control.setValue(((SpaceGroupSC) sc).getRawSymbol(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case MIN_Z:
            case MAX_Z:
                this.control.setValue(((NumericSampleDataSC) sc).getValue(),
                        HtmlControl.EXISTING_VALUE_PRIORITY);
                break;
            case USE_USER_PROVIDER:
                if ((this.searchPage.getUserInfo() != null)
                        && (((ProviderSC) sc).getProviderId()
                                == this.searchPage.getUserInfo().providerId)) {
                    /*
                     * if the previous search restricted the results to the
                     * provider to which the currently logged in user belongs -
                     * it may be interprited to mean that the search is for the
                     * samples submitted by the user's provider.
                     */
                    this.control.setValue(Boolean.TRUE);
                }
                break;
            case SEARCH_ATOM_TYPE: {
                AtomCountSC acsc = getConstraintByIndex(sc.getChildren(),
                        this.searchPage.getAtomCountIndex(this.groupKey),
                        AtomCountSC.class);

                if (acsc != null) {
                    this.control.setValue(acsc.getAtomSymbol());
                }
            }
                break;
            case SEARCH_ATOM_OPERATOR: {
                AtomCountSC acsc = getConstraintByIndex(sc.getChildren(),
                        this.searchPage.getAtomCountIndex(this.groupKey),
                        AtomCountSC.class);

                if (acsc != null) {
                    this.control.setValue(String.valueOf(acsc.getOperator()));
                }
            }
                break;
            case SEARCH_ATOM_COUNT: {
                AtomCountSC acsc = getConstraintByIndex(sc.getChildren(),
                        this.searchPage.getAtomCountIndex(this.groupKey),
                        AtomCountSC.class);

                if (acsc != null) {
                    this.control.setValue(new Double(acsc.getCount()));
                }
            }
                break;
            case SEARCH_ATOMS_FROM_EF:
                this.control.setValue(Boolean.valueOf(((AtomCountSC)
                        ((FormulaAtomSC) sc).getChildren().iterator().next()
                        ).getConsiderEF()));
                break;
            case SEARCH_ATOMS_FROM_EFD:
                this.control.setValue(Boolean.valueOf(((AtomCountSC)
                        ((FormulaAtomSC) sc).getChildren().iterator().next()
                        ).getConsiderEFD()));
                break;
            case SEARCH_ATOMS_FROM_EFLS:
                this.control.setValue(Boolean.valueOf(((AtomCountSC)
                        ((FormulaAtomSC) sc).getChildren().iterator().next()
                        ).getConsiderEFLS()));
                break;
            case SEARCH_ATOMS_FROM_EFSI:
                this.control.setValue(Boolean.valueOf(((AtomCountSC)
                        ((FormulaAtomSC) sc).getChildren().iterator().next()
                        ).getConsiderEFSI()));
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        
        /*
         * NOTE: do not add code here that is expected to apply to all cases
         * because this method uses early return statements for various cases.
         */
        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets the
     * {@code SearchParams} from the most immediate {@code SearchParamsContext},
     * then attempts to update the value of its member variable that corresponds
     * to {@code fieldCode} based on the current value of the owned
     * {@code HtmlControl}. Then it delegates back to the superclass'
     * implementation.
     * 
     * @return the return value of the superclass' implementation of this
     *         method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class, or if the
     *         {@code SearchParamsContext} has no {@code SearchParams}.
     * @throws JspException if any other error is encountered while executing
     *         this method.
     */
    @Override
    public int onProcessingPhaseAfterBody(
            @SuppressWarnings("hiding") PageContext pageContext)
            throws JspException {
        SearchConstraintGroup scg = this.searchPage.getSearchConstraintGroup();
        SearchConstraint sc
                = getSearchConstraintForFieldCode(this.fieldCode, scg);

        switch (this.fieldCode) {
            case SORT_ORDER:
                this.searchPage.setSortOrder(
                        Integer.parseInt((String) this.control.getValue()));
                break;
            case REQUIRE_LOCAL_HOLDING:
                if (this.control.getValueAsBoolean()) {
                    this.searchPage.addSearchConstraint(
                            sc == null ? new LocalHoldingSC() : sc);
                }
                break;
            case REQUIRE_TERMINAL_STATUS:
                if (this.control.getValueAsBoolean()) {
                    this.searchPage.addSearchConstraint(
                            sc == null ? new FinishedStatusSC() : sc);
                }
                break;
            case REQUIRE_NON_RETRACTED:
                if (this.control.getValueAsBoolean()) {
                    this.searchPage.addSearchConstraint(
                            sc == null ? new NonRetractedStatusSC() : sc);
                }
                break;
            case LAB_SEARCH:
                if (String.valueOf(LabInfo.INVALID_LAB_ID).equals(
                        this.control.getValue())) {
                    // no value selected
                    break;
                } else if ((sc != null)
                        && this.control.getValue().equals(
                                String.valueOf(((LabSC) sc).getLabId()))) {
                    // same labId; reuse LabSC
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new LabSC(
                            Integer.parseInt(this.control.getValueAsString())));
                }
                break;
            case PROVIDER:
                if (String.valueOf(ProviderInfo.INVALID_PROVIDER_ID).equals(
                        this.control.getValue())) {
                    // no value selected
                    break;
                } else if ((sc != null)
                        && this.control.getValue().equals(
                                String.valueOf(((ProviderSC) sc).getProviderId()))) {
                    // same providerId; reuse ProviderSC
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new ProviderSC(
                            Integer.parseInt(this.control.getValueAsString())));
                }
                break;
            case SAMPLE_ID:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && (this.control.getValueAsInt()
                                == ((SampleIdSC) sc).getSampleId())) {
                    // same sampleId; reuse SampleIdSC
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new SampleIdSC(
                            this.control.getValueAsInt()));
                }
                break;
            case LOCAL_SAMPLE_ID:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && (((LocalLabIdSC) sc).getOperator()
                                == TextComparisonSC.MATCHES_BEGINNING)
                        && this.control.getValueAsString().equals(
                                ((LocalLabIdSC) sc).getLocalLabIdFragment())) {
                    // same localLabIdFragment and operator; reuse
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new LocalLabIdSC(
                            this.control.getValueAsString(),
                            TextComparisonSC.MATCHES_BEGINNING));
                }
                break;
            case GENERIC_SAMPLE_ID:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((IdNumberSC) sc).getIdBeginning().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new IdNumberSC(
                            this.control.getValueAsString()));
                }
                break;
            case CHEM_NAME:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((ChemicalNameSC) sc).getNameFragment().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new ChemicalNameSC(
                            this.control.getValueAsString()));
                }
                break;
            case STRUCT_FORMULA:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((StructuralFormulaSC) sc).getAttributeValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new StructuralFormulaSC(
                            this.control.getValueAsString()));
                }
                break;
            case EMPIR_FORMULA:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((EmpiricalFormulaSC) sc).getAttributeValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new EmpiricalFormulaSC(
                            this.control.getValueAsString()));
                }
                break;
            case MOIETY_FORMULA:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((MoietyFormulaSC) sc).getAttributeValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new MoietyFormulaSC(
                            this.control.getValueAsString()));
                }
                break;
            case CRYSTALLOGRAPHER:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((CrystallographerSC) sc).getAttributeValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new CrystallographerSC(
                            this.control.getValueAsString()));
                }
                break;
            case SAMPLE_PROVIDER_NAME:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((SampleProviderNameSC) sc).getAttributeValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new SampleProviderNameSC(
                            this.control.getValueAsString()));
                }
                break;
            case SMILES:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((SmilesFormulaSC) sc).getAnnotationValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new SmilesFormulaSC(
                            this.control.getValueAsString()));
                }
                break;
            case KEYWORD:
                if (this.control.getValue() != null) {
                    this.searchPage.addKeyword(this.control.getValueAsString());
                }
                break;
            case MATCH_ANY_KEYWORD:
                if (this.control.getValueAsBoolean()) {
                    this.searchPage.setKeywordOperator(SearchConstraintGroup.OR);
                }
                break;
            case MATCH_ALL_KEYWORD:
                if (this.control.getValueAsBoolean()) {
                    this.searchPage.setKeywordOperator(SearchConstraintGroup.AND);
                }
                break;
            case SPACE_GROUP:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((SpaceGroupSC) sc).getRawSymbol().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    try {
                        this.searchPage.addSearchConstraint(new SpaceGroupSC(
                                this.control.getValueAsString()));
                    } catch (InvalidDataException ex) {
                        /*
                         * This exception can't really happen because any
                         * invalid space group symbols would have been detected
                         * earlier, on PARSING_PHASE, by the validator on the
                         * owned control. We'll propagate the exception just the
                         * same.
                         */
                        throw new JspException(ex);
                    }
                }
                break;
            case MIN_Z:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((MinZSC) sc).getValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new MinZSC(
                            this.control.getValueAsInt()));
                }
                break;
            case MAX_Z:
                if (this.control.getValue() == null) {
                    break;
                } else if ((sc != null)
                        && ((MaxZSC) sc).getValue().equals(
                                this.control.getValueAsString())) {
                    this.searchPage.addSearchConstraint(sc);
                } else {
                    this.searchPage.addSearchConstraint(new MaxZSC(
                            this.control.getValueAsInt()));
                }
                break;
            case TEMP:
                this.searchPage.setRange(this.control.getValueAsDouble(), null,
                        SampleDataInfo.T_FIELD);
                break;
            case RANGE_TEMP:
                this.searchPage.setRange(null, this.control.getValueAsDouble(),
                        SampleDataInfo.T_FIELD);
                break;
            case VOLUME:
                this.searchPage.setRange(this.control.getValueAsDouble(), null,
                        SampleDataInfo.V_FIELD);
                break;
            case RANGE_VOLUME:
                this.searchPage.setRange(null, this.control.getValueAsDouble(),
                        SampleDataInfo.V_FIELD);
                break;
            case DENSITY:
                this.searchPage.setRange(this.control.getValueAsDouble(), null,
                        SampleDataInfo.DCALC_FIELD);
                break;
            case RANGE_DENSITY:
                this.searchPage.setRange(null, this.control.getValueAsDouble(),
                        SampleDataInfo.DCALC_FIELD);
                break;
            case USE_USER_PROVIDER:
                if ((this.control.getValue() == null)
                        || !this.control.getValueAsBoolean()) {
                    break;
                } else {
                    // existing constraints may not be reused because the user
                    // may have changed, and thus the user's local provider may
                    // have changed
                    this.searchPage.addSearchConstraint(new ProviderSC(
                            this.searchPage.getProviderInfo().id));
                }
                break;
            case SEARCH_ATOM_TYPE:
                this.searchPage.addAtomCountSearch(this.groupKey,
                        ("".equals(this.control.getValue()) ? null
                                : (String) this.control.getValue()), null, null);
                break;
            case SEARCH_ATOM_OPERATOR:
                this.searchPage.addAtomCountSearch(
                        this.groupKey,
                        null,
                        this.control.getValue() == null ? null
                                : Integer.valueOf(this.control.getValueAsString()),
                        null);
                break;
            case SEARCH_ATOM_COUNT:
                this.searchPage.addAtomCountSearch(this.groupKey, null, null,
                        this.control.getValueAsDouble());
                break;
            case SEARCH_ATOMS_FROM_EF:
                this.searchPage.setConsiderEFAtoms(
                        this.control.getValue() == Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFD:
                this.searchPage.setConsiderEFDAtoms(
                        this.control.getValue() == Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFLS:
                this.searchPage.setConsiderEFLSAtoms(
                        this.control.getValue() == Boolean.TRUE);
                break;
            case SEARCH_ATOMS_FROM_EFSI:
                this.searchPage.setConsiderEFSIAtoms(
                        this.control.getValue() == Boolean.TRUE);
                break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code SearchParamsField}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        SearchParamsField src = (SearchParamsField) source;
        super.copyTransientPropertiesFrom(source);
        // to ensure that values are propagated to owned elements, each setter
        // method has to be called rather than directly setting its variable
        this.setEditable(src.editable);
        this.setDisplayAsLabel(src.displayAsLabel);
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation delegates
     * to the superclass then updates any references to 'owned' controls or
     * referenced ancestor tags using the 'map' parameter that was populated by
     * the superclass' implementation as well as the caller, then makes a deep
     * copy of any complex modifiable member variables before returning the deep
     * copy.
     * 
     * @param newId {@inheritDoc}
     * @param map {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SearchParamsField dc
                = (SearchParamsField) super.generateCopy(newId, map);

        dc.control = (HtmlControl) map.get(this.control);
        dc.userContext = (UserContext) map.get(this.userContext);

        return dc;
    }

    /**
     * Returns the {@code SearchConstraint} at the specified index in the
     * specified collection's iteration
     * 
     * @param <T> the specific type of {@code SearchConstraint} requested
     * @param constraints a {@code Collection} of search constraints consistent
     *        with the specified type
     * @param index the index in the iteration of the desired constraint
     * @param constraintClass a {@code Class} object establishing the type
     *        {@code T}
     * @return the {@code SearchConstraint} at the specified index in the
     *         collection's iteration, or {@code null} if there is none
     */
    private <T extends SearchConstraint> T getConstraintByIndex(
            Collection<? extends SearchConstraint> constraints, int index,
            Class<T> constraintClass) {
        int i = 0;

        for (SearchConstraint constraint : constraints) {
            if (i == index) {
                return constraintClass.cast(constraint);
            } else {
                i++;
            }
        }

        return null;
    }

    /**
     * A helper method that returns the {@code SearchConstraint} that contains
     * the data exposed by the {@code SearchParamsField} with the given field
     * code, if one exits on the given {@code SearchParams} object. Otherwise
     * null is returned.
     */
    private static SearchConstraint getSearchConstraintForFieldCode(
            int fieldCode, SearchConstraintGroup scg) {
        switch (fieldCode) {
            case SORT_ORDER:
                // this does not map to a SearchConstraint
                return null;
            case REQUIRE_LOCAL_HOLDING:
                return findConstraint(scg, LocalHoldingSC.class);
            case REQUIRE_TERMINAL_STATUS:
                return findConstraint(scg, FinishedStatusSC.class);
            case REQUIRE_NON_RETRACTED:
                return findConstraint(scg, NonRetractedStatusSC.class);
            case LAB_SEARCH:
                return findConstraint(scg, LabSC.class);
            case PROVIDER:
                return findConstraint(scg, ProviderSC.class);
            case SAMPLE_ID:
                return findConstraint(scg, SampleIdSC.class);
            case LOCAL_SAMPLE_ID:
                return findConstraint(scg, LocalLabIdSC.class);
            case GENERIC_SAMPLE_ID:
                return findConstraint(scg, IdNumberSC.class);
            case CHEM_NAME:
                return findConstraint(scg, ChemicalNameSC.class);
            case STRUCT_FORMULA:
                return findConstraint(scg, StructuralFormulaSC.class);
            case EMPIR_FORMULA:
                return findConstraint(scg, EmpiricalFormulaSC.class);
            case MOIETY_FORMULA:
                return findConstraint(scg, MoietyFormulaSC.class);
            case SMILES:
                return findConstraint(scg, SmilesFormulaSC.class);
            case CRYSTALLOGRAPHER:
                return findConstraint(scg, CrystallographerSC.class);
            case SAMPLE_PROVIDER_NAME:
                return findConstraint(scg, SampleProviderNameSC.class);
            case KEYWORD:
            case MATCH_ANY_KEYWORD:
            case MATCH_ALL_KEYWORD:
                return findConstraint(scg, KeywordSC.class);
            case SPACE_GROUP:
                return findConstraint(scg, SpaceGroupSC.class);
            case MIN_Z:
                return findConstraint(scg, MinZSC.class);
            case MAX_Z:
                return findConstraint(scg, MaxZSC.class);
            case TEMP:
            case RANGE_TEMP:
                return findDataRangeConstraint(scg, SampleDataInfo.T_FIELD);
            case VOLUME:
            case RANGE_VOLUME:
                return findDataRangeConstraint(scg, SampleDataInfo.V_FIELD);
            case DENSITY:
            case RANGE_DENSITY:
                return findDataRangeConstraint(scg, SampleDataInfo.DCALC_FIELD);
            case USE_USER_PROVIDER:
                return findConstraint(scg, ProviderSC.class);
            case SEARCH_ATOM_TYPE:
            case SEARCH_ATOM_OPERATOR:
            case SEARCH_ATOM_COUNT:
            case SEARCH_ATOMS_FROM_EF:
            case SEARCH_ATOMS_FROM_EFD:
            case SEARCH_ATOMS_FROM_EFLS:
            case SEARCH_ATOMS_FROM_EFSI:
                return findConstraint(scg, FormulaAtomSC.class);
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Returns the first {@code SearchConstraint} with the given class that is a
     * member of the head {@code SearchConstraintGroup} of the given
     * {@code SearchParams}.
     */
    private static SearchConstraint findConstraint(SearchConstraintGroup scg,
            Class<? extends SearchConstraint> classType) {
        if (scg == null) {
            return null;
        }
        for (SearchConstraint sc : scg.getChildren()) {
            if (sc.getClass() == classType) {
                return sc;
            }
        }
        return null;
    }

    /**
     * Returns the first {@code SampleDataRangeSC} with the given
     * sampleDataFieldCode that is a member of the head
     * {@code SearchConstraintGroup} of the given {@code SearchParams}.
     */
    private static SampleDataRangeSC findDataRangeConstraint(
            SearchConstraintGroup scg, int sampleDataFieldCode) {
        if (scg == null) {
            return null;
        }

        for (SearchConstraint sc : scg.getChildren()) {
            if ((sc.getClass() == SampleDataRangeSC.class)
                    && (((SampleDataRangeSC) sc).getFieldCode()
                            == sampleDataFieldCode)) {
                return (SampleDataRangeSC) sc;
            }
        }
        return null;
    }
}
