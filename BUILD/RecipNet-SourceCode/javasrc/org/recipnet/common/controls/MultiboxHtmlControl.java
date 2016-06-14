/*
 * Reciprocal Net project
 *
 * MultiboxHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 09-Mar-2004: midurbin wrote second draft
 * 17-May-2004: midurbin modified setValue() and calls to setValue() to 
 *              accomodate new value prioritization
 * 17-May-2004: midurbin added setDisplayAsLabel()
 * 18-May-2004: midurbin changed return type for setValue() to void
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 23-Aug-2004: midurbin added ErrorSupplier implementation
 * 24-Aug-2004: midurbin added support for 'prohibited' attribute
 * 01-Oct-2004: midurbin fixed bug #1407 in initializeAndRegisterOwnedElement()
 * 05-Oct-2004: midurbin fixed bug #1397 in setValue(),
 *              onParsingPhaseBeforeBody(), onParsingPhaseAfterBody() and added
 *              setValue()
 * 16-Nov-2004: midurbin fully implemented the superclass'
 *              ExtraHtmlAttributeAccepter
 * 25-Feb-2005: midurbin removed the calls to setId() for 'owned' controls as 
 *              well as reassignIdsBasedOnIndex() because 'owned' control id
 *              management is now entirely handled by HtmlPageElement
 * 22-Mar-2005: midurbin updated onRenderingPhaseAfterBody() to write a simpler
 *              representation of this control when 'displayAsLabel' is true
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 08-Apr-2005: midurbin added support to propagate the 'formatter' property to
 *              'owned' boxes, and added generateHtmlUsingFormatter(),
 *              generateHtmlToInvisiblyPersistValue(),
 *              generateHtmlToDisplayForLabel()
 * 30-Jun-2005: midurbin eliminated an extra line from the output when the
 *              "add another" button is hidden
 * 08-Nov-2005: midurbin added 'boxValidator' property
 * 11-Jan-2006: jobollin updated javadocs, inserted override annotations, and
 *              organized imports
 * 19-Jan-2006: jobollin implemented parseValue(String) (which is now abstract
 *              in the superclass); updated getErrorCode() to make use of
 *              the generics provided in the superclass
 * 23-Feb-2006: jobollin added special handling for propagating a "tabindex"
 *              extra HTML attribute to "delete" and "add another" buttons when
 *              it is provided to this control; moved creation of the owned
 *              "persistedBoxCount" and "addAnotherButton" controls into
 *              onRegistrationPhaseBeforeBody();
 * 07-Mar-2006: jobollin moved validation from onParsingPhaseAfterBody() to
 *              isParsedValueValid() for consistency with the superclass and to
 *              override the superclass's treatment of the 'prohibited'
 *              property, which is inappropriate for this class
 * 12-Jun-2006: jobollin suppressed various unused argument warnings
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.Validator;

/**
 * <p>
 * This is the class for a custom Tag that represents a text input that allows
 * multiple values to be inputted. It is displayed as one or more text input
 * fields (textboxes or textareas) with a button that allows the user to add
 * another box. Optionally delete buttons are present for each box allowing
 * individual boxes and their values to be removed.
 * </p><p>
 * The member {@code HtmlPageElement} objects are considered 'owned' elements.
 * They are fully encapsulated by this class and have no methods that are
 * invoked by Tomcat or the servlet container. These owned elements must be
 * registered by calls to {@code HtmlPageElement.registerOwnedElement()} so that
 * their phase implementation methods will be called at the appropriate time.
 * Furthermore any attributes that they get from their 'owning' class must be
 * set during the 'setter' method for that class. For convenience, initalization
 * and registration of 'owned' elements is grouped in a helper method,
 * {@code initializeAndRegisterOwnedElement()} for use by this class.
 * </p><p>
 * The value that is persisted by this control is an array of {@code String}
 * objects, each element representing the value of a text input. An integer is
 * also retained, representing the number of values present.
 * </p><p>
 * When {@code setValue()} is called to set the value to null, the field
 * {@code HtmlControl.value} is actually set to an array containing
 * 'initialBoxCount' null {@code String}s. Furthermore, {@code getValue()} will
 * return simply null if the actual value is an array continaing no non-null
 * elements. This is to conform to the expectations by {@code HtmlControl} for
 * non-set values.
 * </p><p>
 * This class implements {@code ExtraHtmlAttributeAccepter} by maintaining a map
 * of all added attributes and then, right before rendering each 'owned' box,
 * copying those attributes to them.
 * </p>
 */
public class MultiboxHtmlControl extends HtmlControl implements Cloneable,
        ErrorSupplier {

    /**
     * An error flag that may be reported when the parsed value is invalid
     * because it contains more than {@code maxLength} characters. This is used
     * in the implementation of {@code ErrorSupplier}.
     */
    public static final int VALUE_IS_TOO_LONG
            = HtmlControl.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return VALUE_IS_TOO_LONG;
    }

    /**
     * A constant value that can be used to indicate that no limit should be
     * applied to the number of boxes that can be created.
     */
    public static final int NO_LIMIT = Integer.MAX_VALUE;

    /** A constant value that serves to indicate an uninitialized int value. */
    public static final int UNDEFINED = -1;

    /**
     * A contstant that may be used to signify a textbox as opposed to a
     * textarea.
     */
    public static final int TEXTBOX = 0;

    /**
     * A contstant that may be used to signify a textarea as opposed to a
     * textbox.
     */
    public static final int TEXTAREA = 1;

    /**
     * Stores {@code HtmlControl} objects, representing input boxes. Cleared by
     * {@code reset()} and populated during the phase evaluation.
     */
    private List<AbstractTextHtmlControl> ownedBoxes;

    /**
     * Stores {@code ButtonHtmlControl} objects, representing box removal
     * buttons; one for each child box. Whether these buttons are displayed is
     * controlled by the 'displayDeleteButtons' parameter, but they are
     * maintained whether or not they will be displayed.
     */
    private List<ButtonHtmlControl> deleteButtons;

    /**
     * An 'owned' control; the 'add another' button. It is included at the
     * bottom of the boxes to allow the user to add another box for input when
     * such an action is not prohibited. To disable this button, set
     * {@code maxBoxCount} to the number of boxes that will be displayed. Even
     * if this button is supressed, an internal representation of it exists.
     */
    private ButtonHtmlControl addAnotherButton;

    /**
     * An 'owned' control that maintains the count of boxes represented by this
     * {@code MultiboxHtmlControl}.
     */
    private HiddenIntHtmlControl persistedBoxCount;

    /**
     * A mapping of attribute name {@code String}s to attribute value
     * {@code String}s representing extra attributes that should be included in
     * the appropriate HTML tag for this control. Attribute/value pairs are all
     * cleared by {@code reset()} and are added by calls to
     * {@code addExtraAttribute()}.
     */
    private Map<String, String> extraAttributes;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setInitialBoxCount()}. This value
     * may not be set to a value less than 1. This attribute and
     * {@code initialValue} (as well as an initial value generated from the body
     * content) are mutually exclusive. When {@code value} is set, it implicitly
     * sets the current boxCount, overruling this variable.
     */
    private int initialBoxCount;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setMaxBoxCount()}. It serves to
     * enforce a limit on the number of boxes, but may be set to
     * {@code NO_LIMIT} if no restriction is needed. This value must be 1 or
     * greater. This attribute is 'transient'.
     */
    private int maxBoxCount;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setRows()}. Serves no purpose if
     * this {@code MultiboxHtmlControl} is composed of
     * {@code TextboxHtmlControl}s, as indicated by {@code boxType}. This
     * attribute is 'transient' and must be propagated to the owned boxes.
     */
    private int rows;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setColumns()}. If this
     * {@code MultiboxHtmlControl} is composed of {@code TextboxHtmlControl}s
     * then this affects the {@code size} attribute for those boxes. This
     * attribute is 'transient' and must be propagated to the owned boxes.
     */
    private int columns;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setMaxLength()}. This attribute is
     * 'transient' and must be propagated to the owned boxes. To indicate no
     * maximum length, it may be set to {@code NO_LIMIT}.
     */
    private int maxLength;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setShouldTrim()}. This attribute is
     * 'transient' and must be propagated to the owned boxes.
     */
    private boolean shouldTrim;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setShouldConvertBlankToNull()}.
     * This attribute is 'transient' and must be propagated to the owned boxes.
     */
    private boolean shouldConvertBlankToNull;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setWrapSetting()}. Serves no
     * purpose if this {@code MultiboxHtmlControl} is composed of
     * {@code TextboxHtmlControl}s, as inidcated by {@code boxType}. This
     * attribute is 'transient' and may be propagated to the owned boxes.
     */
    private String wrapSetting;

    /**
     * Indicates whether this {@code MultiboxHtmlControl} is made up of
     * {@code TextboxHtmlControl} or {@code TextareaHtmlControl} objects
     * depending on whether the value is set to {@code TEXTBOX} or
     * {@code TEXTAREA} respectively. This value is a required attribute that is
     * initialized by {@code reset()} and may be set by its 'setter' method,
     * {@code setBoxType()}. This variable may not be changed after the
     * {@code REGISTRATION_PHASE} because at that point the type of box is
     * fixed.
     */
    private int boxType;

    /**
     * An optional attribute, initialized by {@code reset()} and modified by
     * calls to its 'setter' method, {@code setDisplayDeleteButtons()}. This
     * attribute indicates whether delete buttons will be available to delete
     * individual owned boxes.
     */
    private boolean displayDeleteButtons;

    /**
     * An optional property that defaults to null that represents a
     * {@code Validator} that will be applied to each of the box controls for
     * this multibox. This validator must be of a type that may be legally
     * attached to either a {@code TextboxHtmlControl} or a
     * {@code TextareaHtmlControl} depending on whether 'boxType' is
     * {@code TEXTAREA} or {@code TEXTBOX}.
     */
    private Validator boxValidator;

    /** The current {@code ErrorSupplier} error code */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        setFailedValidationHtml("");
        this.initialBoxCount = 1;
        this.maxBoxCount = MultiboxHtmlControl.NO_LIMIT;
        this.columns = 45;
        this.rows = 5;
        this.maxLength = MultiboxHtmlControl.NO_LIMIT;
        this.shouldTrim = true;
        this.shouldConvertBlankToNull = true;
        this.wrapSetting = "virtual";
        this.displayDeleteButtons = false;
        this.boxType = MultiboxHtmlControl.UNDEFINED;
        this.errorCode = NO_ERROR_REPORTED;
        this.ownedBoxes = new ArrayList<AbstractTextHtmlControl>();
        this.deleteButtons = new ArrayList<ButtonHtmlControl>();
        this.addAnotherButton = null;
        this.persistedBoxCount = null;
        this.extraAttributes = null;
        this.boxValidator = null;
    }

    /** @return the starting number of input boxes to be drawn */
    public int getInitialBoxCount() {
        return this.initialBoxCount;
    }

    /**
     * @param boxCount a starting number of input boxes to be drawn
     * @throws IllegalArgumentException if boxCount is less than 1, the minimum
     *         allowed number of boxes.
     */
    public void setInitialBoxCount(int boxCount) {
        if (boxCount < 1) {
            throw new IllegalArgumentException();
        }
        this.initialBoxCount = boxCount;
    }

    /**
     * @return the maximum number of boxes allowed, or {@code NO_LIMIT}.
     */
    public int getMaxBoxCount() {
        return this.maxBoxCount;
    }

    /**
     * @param maxBoxCount the maximum number of boxes allowed, or
     *        {@code NO_LIMIT} to indicate that there should be no limit.
     * @throws IllegalArgumentException if maxBoxCount is lower than the current
     *         number of boxes
     */
    public void setMaxBoxCount(int maxBoxCount) {
        if (((this.ownedBoxes != null)
                    && (this.ownedBoxes.size() > maxBoxCount))
                || (maxBoxCount < 1)) {
            throw new IllegalArgumentException();
        }
        this.maxBoxCount = maxBoxCount;
    }

    /**
     * @return the number of rows for each box. If 1, then the boxes will be
     *         {@code TextboxHtmlControl} objects instead of
     *         {@code TextareaHtmlControl} objects.
     */
    public int getRows() {
        return this.rows;
    }

    /**
     * @param rows the number of rows for each box. This attribute may not be
     *        set if {@code boxType} is set to {@code TEXTBOX}.
     * @throws IllegalStateException if {@code boxType} is set to
     *         {@code TEXTBOX}
     */
    public void setRows(int rows) {
        if (this.boxType == MultiboxHtmlControl.TEXTBOX) {
            throw new IllegalStateException();
        }
        this.rows = rows;
        // iterate through all owned textareas and set their rows
        for (TextareaHtmlControl area : getRegisteredOwnedElementsOfType(
                TextareaHtmlControl.class)) {
            area.setRows(rows);
        }
    }

    /**
     * @return the number of columns for each box (or size in the case where
     *         {@code TextboxHtmlControl}s are used)
     */
    public int getColumns() {
        return this.columns;
    }

    /**
     * @param columns the number of columns for each box. (or size in the case
     *        where {@code TextboxHtmlControl}s are used)
     */
    public void setColumns(int columns) {
        this.columns = columns;
        if (this.boxType == MultiboxHtmlControl.TEXTAREA) {
            // if 'owned' boxes are textareas set their 'columns'
            for (TextareaHtmlControl area : getRegisteredOwnedElementsOfType(
                    TextareaHtmlControl.class)) {
                area.setColumns(columns);
            }
        } else if (this.boxType == MultiboxHtmlControl.TEXTBOX) {
            // if 'owned' boxes are textboxes set thier 'size'
            for (TextboxHtmlControl box : getRegisteredOwnedElementsOfType(
                    TextboxHtmlControl.class)) {
                box.setSize(columns);
            }
        }
    }

    /**
     * @return the maximum length allowed for each entry, or {@code NO_LIMIT}
     *         indicating that no limit is enforced on entry length.
     */
    public int getMaxLength() {
        return this.maxLength;
    }

    /**
     * @param maxLength indicates the maximum allowed length for the input to
     *        any of the multiple text boxes, or {@code NO_LIMIT} indicating
     *        that no limit should be enforced.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        
        // Set the owned boxes' corresponding attributes
        for (AbstractTextHtmlControl textControl : ownedBoxes) {
            textControl.setMaxLength(maxLength);
        }
    }

    /**
     * @return whether text input entries should be trimmed of leading and
     *         trailing whitespace
     */
    public boolean getShouldTrim() {
        return this.shouldTrim;
    }

    /**
     * @param shouldTrim whether text input entries should be trimmed of leading
     *        and trailing whitespace
     */
    public void setShouldTrim(boolean shouldTrim) {
        this.shouldTrim = shouldTrim;
        
        // Set the owned boxes' corresponding attributes
        for (AbstractTextHtmlControl textControl : ownedBoxes) {
            textControl.setShouldTrim(shouldTrim);
        }
    }

    /** @return whether or not empty fields will be stored as null */
    public boolean getShouldConvertBlankToNull() {
        return this.shouldConvertBlankToNull;
    }

    /**
     * @param shouldConvertBlankToNull indicates whether or not blank fields
     *        will be stored as null
     */
    public void setShouldConvertBlankToNull(boolean shouldConvertBlankToNull) {
        this.shouldConvertBlankToNull = shouldConvertBlankToNull;
        
        // Set the owned boxes' corresponding attributes
        for (AbstractTextHtmlControl textControl : ownedBoxes) {
            textControl.setShouldConvertBlankToNull(shouldConvertBlankToNull);
        }
    }

    /** @return the wrap setting that will be applied to multirow boxes */
    public String getWrapSetting() {
        return this.wrapSetting;
    }

    /**
     * @param wrapSetting the wrap setting that will be applied to the 'owned'
     *        boxes if {@code boxType} is {@code TEXTAREA}.
     * @throws IllegalStateException if {@code boxType} is set to
     *         {@code TEXTBOX}.
     */
    public void setWrapSetting(String wrapSetting) {
        if (this.boxType == MultiboxHtmlControl.TEXTBOX) {
            throw new IllegalStateException();
        }
        this.wrapSetting = wrapSetting;
        if (this.boxType == MultiboxHtmlControl.TEXTAREA) {
            // if 'owned' boxes are textareas, set their 'wrapSetting'
            // attribute
            for (TextareaHtmlControl area : getRegisteredOwnedElementsOfType(
                    TextareaHtmlControl.class)) {
                area.setWrapSetting(wrapSetting);
            }
        }
    }

    /**
     * @param displayDeleteButtons indicates whether buttons to remove
     *        individual boxes should be displayed
     */
    public void setDisplayDeleteButtons(boolean displayDeleteButtons) {
        this.displayDeleteButtons = displayDeleteButtons;
    }

    /**
     * @return whether or not buttons to remove individual boxes will be
     *         displayed
     */
    public boolean getDisplayDeleteButtons() {
        return this.displayDeleteButtons;
    }

    /**
     * Setter for the 'boxValidator' property.
     * 
     * @param validator a {@code Validator} that will be applied to every
     *        'owned' box
     */
    public void setBoxValidator(Validator validator) {
        this.boxValidator = validator;
        for (HtmlControl control : ownedBoxes) {
            control.setValidator(validator);
        }
    }

    /**
     * Getter for the 'boxValidator' property.
     * 
     * @return the {@code Validator} that will be applied to every 'owned' box
     */
    public Validator getBoxValidator() {
        return this.boxValidator;
    }

    /**
     * Overrides {@code HtmlControl} to propagate attribute to owned element and
     * delegate back to the superclass.
     * 
     * @param editable whether this control is editable
     */
    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        
        for (HtmlControl control
                : getRegisteredOwnedElementsOfType(HtmlControl.class)) {
            control.setEditable(editable);
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation applies the
     * provided formatter to each owned box. This means that the
     * {@code HtmlFormatter} must be able to format values of the type
     * associated with {@code TextboxHtmlControl} or {@code TextareaHtmlControl}
     * objects ({@code String} objects in both cases) instead of that
     * assoicated with the whole {@code MultiboxHtmlControl} ({@code String[]}).
     */
    @Override
    public void setFormatter(HtmlFormatter formatter) {
        for (HtmlControl control : ownedBoxes) {
            control.setFormatter(formatter);
        }
        super.setFormatter(formatter);
    }

    /**
     * @param boxType either {@code TEXTAREA} or {@code TEXTBOX} indicating that
     *        this {@code MultiboxHtmlControl} is is made up of
     *        {@code TextareaHtmlControl} or {@code TextboxHtmlControl} objectes
     *        respectively.
     * @throws IllegalArgumentException if {@code boxType} has already been set
     *         to something else or if an invalid value has been supplied.
     */
    public void setBoxType(int boxType) {
        if ((boxType != MultiboxHtmlControl.TEXTBOX)
                && (boxType != MultiboxHtmlControl.TEXTAREA)) {
            throw new IllegalArgumentException();
        } else if ((this.boxType != MultiboxHtmlControl.UNDEFINED)
                && (boxType != this.boxType)) {
            throw new IllegalArgumentException();
        } else {
            this.boxType = boxType;
        }

    }

    /**
     * @return either {@code TEXTAREA} or {@code TEXTBOX} indicating that this
     *         {@code MultiboxHtmlControl} is is made up of
     *         {@code TextareaHtmlControl} or {@code TextboxHtmlControl}
     *         objectes respectively.
     */
    public int getBoxType() {
        return this.boxType;
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation delegates back
     * to the superclass before calling this method on all 'owned' elements of
     * type {@code HtmlControl}.
     * 
     * @param displayAsLabel indicates whether this control should be displayed
     *        as a text label or as an HTML input.
     */
    @Override
    public void setDisplayAsLabel(boolean displayAsLabel) {
        super.setDisplayAsLabel(displayAsLabel);
        
        for (HtmlControl control
                : getRegisteredOwnedElementsOfType(HtmlControl.class)) {
            control.setDisplayAsLabel(displayAsLabel);
        }
    }

    /** Implements {@code ErrorSupplier}. */
    @Override
    public int getErrorCode() {
        for (ErrorSupplier control
                : getRegisteredOwnedElementsOfType(ErrorSupplier.class)) {
            /*
             * this method only propagates the VALUE_IS_TOO_LONG and
             * VALIDATOR_REJECTED_VALUE error flags but not the
             * REQUIRED_VALUE_IS_MISSING or PROHIBITED_VALUE_IS_PRESENT error
             * flag, because the latter flags are handled specially during
             * onParsingPhaseAfterBody()
             */
            if (((this.boxType == TEXTAREA) && ((control.getErrorCode()
                            & AbstractTextHtmlControl.VALUE_IS_TOO_LONG) != 0))
                    || ((this.boxType == TEXTBOX) && ((control.getErrorCode()
                            & AbstractTextHtmlControl.VALUE_IS_TOO_LONG) != 0))) {
                setErrorFlag(VALUE_IS_TOO_LONG);
            }
            if ((control.getErrorCode() & HtmlControl.VALIDATOR_REJECTED_VALUE)
                    != 0) {
                setErrorFlag(VALIDATOR_REJECTED_VALUE);
            }
        }
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    @Override
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Overrides {@code HtmlControl}'s {@code ExtraHtmlAttributeAccepter}
     * implementation to maintain its own map of attribute names to values that
     * will be added to 'owned' boxes before they are rendered.
     * 
     * @param name the name of the attribute
     * @param value the value of the attribute
     */
    @Override
    public void addExtraHtmlAttribute(String name, String value) {
        if (this.extraAttributes == null) {
            this.extraAttributes = new HashMap<String, String>();
        }
        this.extraAttributes.put(name, value);
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation exists to
     * ensure that null is returned when all textboxes are empty even though for
     * implementation purposes, {@code HtmlControl.value} may be set to an array
     * containing more than one null value.
     * 
     * @return an array of {@code String} objects representing the values of
     *         this control or simply null if there are no values
     */
    @Override
    public Object getValue() {
        String value[] = (String[]) super.getValue();
        
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (value[i] != null) {
                    // at least one non-null value exists, return the whole
                    // array of values
                    return value;
                }
            }
        }
        
        return null;
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation updates the
     * value of this multibox if the given priority is higher than the current
     * priority of the value. This, in turn, may require the number of boxes and
     * their individual values to be updated. This method delegates back to the
     * superclass.
     * 
     * @param value an array of {@code String} objects, each representing a
     *        text-input box.
     * @param priority the priority value of this attempt to set the value of
     *        this multibox to a non-null value
     * @param priorityIfNull the priority value of this attempt to set the value
     *        of this multibox to null
     * @throws IllegalStateException if called before the {@code boxType} has
     *         been set.
     */
    @Override
    public boolean setValue(Object value, int priority, int priorityIfNull) {
        if ((this.boxType != MultiboxHtmlControl.TEXTBOX)
                && (this.boxType != MultiboxHtmlControl.TEXTAREA)) {
            throw new IllegalStateException();
        }

        // verify that this change had high enough priority to take effect
        if (!super.setValue(value, priority, priorityIfNull)) {
            return false;
        }
        
        // FIXME: should this transformation be done before super.setValue()? 
        if (value == null) {
            // null resets the value to an array of initialBoxCount null strings
            value = new String[initialBoxCount];
        }
        String[] strValue = (String[]) value;

        // update persistedBoxCount to the desired number of boxes
        this.persistedBoxCount.setValue(
                strValue.length, HtmlControl.PARSED_VALUE_PRIORITY);
        
        // remove and unregister boxes (and their delete buttons) while there
        // are more than needed for the new value
        while (this.ownedBoxes.size() > strValue.length) {
            unregisterOwnedElement(ownedBoxes.remove(0));
            unregisterOwnedElement(deleteButtons.remove(0));
        }

        // add and register new boxes (and their delete buttons) until there
        // are enough for the new value
        while (this.ownedBoxes.size() < strValue.length) {
            AbstractTextHtmlControl newBox = createNewBox();
            ButtonHtmlControl deleteButton = new ButtonHtmlControl();
            
            initializeAndRegisterOwnedElement(newBox);
            this.ownedBoxes.add(newBox);

            initializeAndRegisterOwnedElement(deleteButton);
            this.deleteButtons.add(deleteButton);
            deleteButton.setLabel("Delete");
        }

        // set the value of each box
        for (int i = 0; i < this.ownedBoxes.size(); i++) {
            this.ownedBoxes.get(i).setValue(strValue[i]);
        }
        
        return true;
    }

    /**
     * A dummy implementation that always returns {@code null}.  This control's
     * value is not parsed directly from a request parameter; rather, it is
     * established by its owned controls parsing <em>their</em> values
     * 
     * @see HtmlControl#parseValue(String)
     */
    @Override
    protected Object parseValue(@SuppressWarnings("unused") String rawValue) {
        return null;
    }

    /**
     * <p>
     * {@inheritDoc}; this version checks the owned boxes for non-null values
     * to evaluate this control's validity vis-a-vis the {@code required} and
     * {@code prohibited} properties.
     * </p><p>
     * <strong>Note:</strong> this version does <em>not</em> delegate to the
     * superclass because its evaluation of the {@code prohibited} property is
     * wrong for this class. It does not refer to the tag's own
     * {@code Validator}, if any, because this tag does not support direct
     * validation (use the {@code boxValidator} property instead to validate
     * individual boxes).
     * </p>
     */
    @Override
    protected boolean isParsedValueValid(
            @SuppressWarnings("unused") Object parsedValue) {
        // check for validation errors
        boolean valueEntered = false;
        
        for (HtmlControl box : this.ownedBoxes) {
            if (!box.getFailedValidation()) {
                if (box.getValue() != null) {
                    valueEntered = true;
                    break;
                }
            }
        }
        
        if (!valueEntered && getRequired()) {
            setErrorFlag(REQUIRED_VALUE_IS_MISSING);
            return false;
        } else if (valueEntered && getProhibited()) {
            setErrorFlag(PROHIBITED_VALUE_IS_PRESENT);
            return false;
        } else {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     * @see HtmlControl#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        // create hidden int 'persistedBoxCount'
        this.persistedBoxCount = new HiddenIntHtmlControl();
        initializeAndRegisterOwnedElement(this.persistedBoxCount);
        this.persistedBoxCount.setValue(0, HtmlControl.PARSED_VALUE_PRIORITY);

        // create 'add another' button
        this.addAnotherButton = new ButtonHtmlControl();
        initializeAndRegisterOwnedElement(this.addAnotherButton);
        this.addAnotherButton.setLabel("Add another");
        
        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation overrides
     * default handling of 'owned' elements for this phase to assure that
     * {@code persistedBoxCount} is parsed first and the number of 'owned' boxes
     * is updated to reflect the number from the post, ensuring that all posted
     * values are parsed.
     * 
     * @param request the current {@code HttpServletRequest} object is used to
     *        get the post parameters by each of the 'owned' elements.
     * @return {@code SKIP_BODY}
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        boolean valueSet;

        // Note: super.onParsingPhaseBeforeBody() is not invoked
        
        this.persistedBoxCount.onParsingPhaseBeforeBody(request);
        this.persistedBoxCount.onParsingPhaseAfterBody(request);

        // Any number of boxes may be present (the count is indicated by
        // boxCount which was preserved as a post parameter), and their current
        // values are meaningless because they are about to get values from the
        // POST during their own parsing phase.
        while (this.persistedBoxCount.getValueAsInt()
                < this.ownedBoxes.size()) {
            unregisterOwnedElement(ownedBoxes.remove(0));
            unregisterOwnedElement(deleteButtons.remove(0));
        }
        while (this.persistedBoxCount.getValueAsInt()
                > this.ownedBoxes.size()) {
            AbstractTextHtmlControl newBox = createNewBox();
            initializeAndRegisterOwnedElement(newBox);
            this.ownedBoxes.add(newBox);

            ButtonHtmlControl deleteButton = new ButtonHtmlControl();
            initializeAndRegisterOwnedElement(deleteButton);
            this.deleteButtons.add(deleteButton);
            deleteButton.setLabel("Delete");
        }

        // manually invoke the parsing phase implementation on all owned
        // elements since we will return SKIP_BODY, preventing the default
        // handling.
        for (HtmlControl ownedElement
                : getRegisteredOwnedElementsOfType(HtmlControl.class)) {
            if (ownedElement != this.persistedBoxCount) {
                // call the phase method on all owned elements except
                // persistedBoxCount on which it was already called
                ownedElement.onParsingPhaseBeforeBody(request);
                ownedElement.onParsingPhaseAfterBody(request);
            }
        }

        // make sure that the internal representation of 'value' is consistent
        // in case the superclass uses it.
        String[] newValue = new String[this.ownedBoxes.size()];
        for (int i = 0; i < this.ownedBoxes.size(); i++) {
            HtmlControl box = this.ownedBoxes.get(i);
            
            assert ((box instanceof TextboxHtmlControl)
                    || (box instanceof TextareaHtmlControl));
            /*
             * we know that the 'value' of a TextboxHtmlControl or a
             * TextAreaHtmlControl has the type String.  Because the method
             * HtmlControl.getValueAsString() will convert null to blank, we
             * must use the method HtmlControl.getValue() and cast the value as
             * a String to preserve null values.
             */
            newValue[i] = (String) box.getValue();
        }

        valueSet = super.setValue(newValue, HtmlControl.PARSED_VALUE_PRIORITY);
        assert valueSet;

        return SKIP_BODY;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation checks to
     * determine if any of its owned buttons were pressed and to perform the
     * appropriate action. This method delegates to its superclass for the
     * return value. If this control is not 'editable' this method does nothing
     * but delegate back to the superclass.
     * 
     * @param pageContext the {@code PageContext} may be used by the 'owned
     *        elements' or the superclass.
     * @return the value returned by the superclass' implementation is returned.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        if (!getEditable()) {
            // if the content of this control is not editable, then none of the
            // buttons should have been clickable and therefore no more
            // processing is required.
            return rc;
        }

        // Check to see if any delete buttons were clicked
        Iterator<ButtonHtmlControl> delButIt = this.deleteButtons.iterator();
        Iterator<? extends HtmlControl> boxIt = this.ownedBoxes.iterator();
        while (boxIt.hasNext()) {
            HtmlControl box = boxIt.next();
            ButtonHtmlControl button = delButIt.next();
            
            if (button.getValueAsBoolean()) {
                unregisterOwnedElement(box);
                boxIt.remove();
                unregisterOwnedElement(button);
                delButIt.remove();
                this.persistedBoxCount.setValue(
                        this.persistedBoxCount.getValueAsInteger() - 1,
                        HtmlControl.PARSED_VALUE_PRIORITY);
                break;
            }
        }

        // Check to see if the 'add another' button was clicked
        if (this.addAnotherButton.getValueAsBoolean()) {
            AbstractTextHtmlControl newBox = createNewBox();
            
            initializeAndRegisterOwnedElement(newBox);
            this.ownedBoxes.add(newBox);

            ButtonHtmlControl deleteButton = new ButtonHtmlControl();
            
            initializeAndRegisterOwnedElement(deleteButton);
            this.deleteButtons.add(deleteButton);
            deleteButton.setLabel("Delete");
            this.persistedBoxCount.setValue(
                    this.persistedBoxCount.getValueAsInteger() + 1,
                    HtmlControl.PARSED_VALUE_PRIORITY);
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implentation does nothing
     * and returns {@code SKIP_BODY} to ensure that the {@code RENDERING_PHASE}
     * methods will not be called on the owned elements. Instead they will be
     * called by the implementation of {@code onRenderingPhaseAfterBody()} to
     * insert appropriate HTML formatting between the output of each element.
     * 
     * @return SKIP_BODY
     */
    @Override
    public int onRenderingPhaseBeforeBody(
            @SuppressWarnings("unused") JspWriter out) throws IOException,
            JspException {
        return SKIP_BODY;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation outputs
     * some formatting HTML and delegates to
     * {@code onRenderingPhaseBeforeBody()} and
     * {@code onRenderigPhaseAfterBody()} for each owned element if they are
     * visible. This method delegates back to its superclass.
     * 
     * @param out a {@code JspWriter} to which output should be written
     * @return the value returned by the superclass' implementation is returned.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        
        if (getDisplayAsLabel()) {
            // display just text
            for (Iterator<? extends HtmlControl> it 
                    = this.ownedBoxes.iterator();
                    it.hasNext(); ) {
                HtmlControl box = it.next();
                
                box.onRenderingPhaseBeforeBody(out);
                box.onRenderingPhaseAfterBody(out);
                if (it.hasNext()) {
                    out.print(", ");
                }
            }
        } else {
            
            /*
             * FIXME: special handling for the tabindex extra attribute is a
             * convenient hack.  A better solution should be found for setting
             * the attributes of the button controls, and even of the individual
             * boxes.
             */
            String tabIndex = (this.extraAttributes == null) ? null
                    : this.extraAttributes.get("tabindex");
            Iterator<? extends HtmlControl> itB = ownedBoxes.iterator();
            Iterator<ButtonHtmlControl> itD = deleteButtons.iterator();
            
            // Generate code for each value
            out.println("<table border=\"0\">");
            while (itB.hasNext()) {
                HtmlControl nextBox = itB.next();
                
                out.println("<tr>");
                out.println("<td style=\"text-align: left;\">");
                if (this.extraAttributes != null) {
                    for (Entry<String, String> entry
                            : this.extraAttributes.entrySet()) {
                        nextBox.addExtraHtmlAttribute(
                                entry.getKey(), entry.getValue());
                    }
                }
                nextBox.onRenderingPhaseBeforeBody(out);
                nextBox.onRenderingPhaseAfterBody(out);
                
                out.println("</td><td valign=\"top\">");
                if ((this.displayDeleteButtons) 
                        && (this.persistedBoxCount.getValueAsInt() > 1)) {
                    HtmlControl nextButton = itD.next();

                    nextButton.onRenderingPhaseBeforeBody(out);
                    nextButton.onRenderingPhaseAfterBody(out);
                    if (tabIndex != null) {
                        nextButton.addExtraHtmlAttribute(
                                "tabindex", tabIndex);
                    }
                } else {
                    out.println("&nbsp;");
                }
                out.println("</td></tr>");
            }
            
            // Generate "Add Another" button, if warranted
            if (this.persistedBoxCount.getValueAsInt() < this.maxBoxCount) {
                out.println("<tr><td style=\"text-align: right;\">");
                if (tabIndex != null) {
                    this.addAnotherButton.addExtraHtmlAttribute(
                            "tabindex", tabIndex);
                }
                this.addAnotherButton.onRenderingPhaseBeforeBody(out);
                this.addAnotherButton.onRenderingPhaseAfterBody(out);
                out.println("</td><td>&nbsp;</td></tr>");
            }
            
            out.println("</table>");
        }
        
        this.persistedBoxCount.onRenderingPhaseBeforeBody(out);
        this.persistedBoxCount.onRenderingPhaseAfterBody(out);
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}.  This version returns an array containing the specified
     * content string as its only element
     * 
     * @return an {@code String[]} containing the specified content string as
     *         its only element
     */
    @Override
    protected Object convertBodyContentToValue(String content) {
        return new String[] { content };
    }

    /**
     * This method creates a new {@code TextboxHtmlControl} or
     * {@code TextareaHtmlControl} and sets its attributes based on the current
     * state of this {@code MultiboxHtmlControl}. The decision to return a
     * {@code TextboxHtmlControl} as opposed to a {@code TextareaHtmlControl} is
     * based on the value of {@code boxType}.
     * 
     * @throws IllegalStateException if {@code boxType} is not valid.
     */
    private AbstractTextHtmlControl createNewBox() {
        if (this.boxType == MultiboxHtmlControl.TEXTBOX) {
            return new TextboxHtmlControl();
        } else if (this.boxType == MultiboxHtmlControl.TEXTAREA) {
            return new TextareaHtmlControl();
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code TextboxHtmlControl}. The
     * current implementation makes sure that all the setter methods (even on
     * the superclass) are called on this element for every attribute whose
     * value is reflected in any owned elements.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    public void copyTransientPropertiesFrom(HtmlPageElement source) {
        MultiboxHtmlControl src = (MultiboxHtmlControl) source;

        /*
         * to ensure that values are propagated to owned elements, each
         * setter method has to be called, but sometimes such a call would
         * result in an IllegalStateException, therefore, only values that
         * changed will be propagated.
         */
        
        // Box counts should be set correctly before any other setters are used
        if (this.initialBoxCount != src.initialBoxCount) {
            setInitialBoxCount(src.initialBoxCount);
        }
        if (this.maxBoxCount != src.maxBoxCount) {
            setMaxBoxCount(src.maxBoxCount);
        }
        
        super.copyTransientPropertiesFrom(source);
        
        if (this.rows != src.rows) {
            setRows(src.rows);
        }
        if (this.columns != src.columns) {
            setColumns(src.columns);
        }
        if (this.maxLength != src.maxLength) {
            setMaxLength(src.maxLength);
        }
        if (this.shouldTrim != src.shouldTrim) {
            setShouldTrim(src.shouldTrim);
        }
        if (this.shouldConvertBlankToNull != src.shouldConvertBlankToNull) {
            setShouldConvertBlankToNull(src.shouldConvertBlankToNull);
        }
        if (this.wrapSetting != src.wrapSetting) {
            setWrapSetting(src.wrapSetting);
        }
        if (this.displayDeleteButtons != src.displayDeleteButtons) {
            this.displayDeleteButtons = src.displayDeleteButtons;
        }
    }

    /**
     * A helper method that translates the currently set attributes for this
     * {@code MultiboxHtmlControl} to the supplied 'owned' element. This method
     * should be called immediately after creation of an 'owned' element and
     * will call {@code reset()} on the 'owned' element before translating the
     * attributes and finally registering the element by calling
     * {@code HtmlPageElement.registerOwnedElement()}. After this call is
     * complete the 'owned' element will have its phase-implementation methods
     * automatically called until either this tag completes evalutation or
     * {@code HtmlPageElement.unregisterOwnedElement()} is called.
     * 
     * @param owned an 'owned' element
     */
    private void initializeAndRegisterOwnedElement(HtmlPageElement owned) {
        owned.reset();
        if (owned instanceof HtmlControl) {
            HtmlControl ownedControl = (HtmlControl) owned;
            
            ownedControl.setVisible(getVisible());
            ownedControl.setEditable(getEditable());
            ownedControl.setDisplayAsLabel(getDisplayAsLabel());
            
            if (ownedControl instanceof AbstractTextHtmlControl) {
                AbstractTextHtmlControl textControl
                        = (AbstractTextHtmlControl) ownedControl;
                
                textControl.setMaxLength(this.maxLength);
                textControl.setShouldTrim(this.shouldTrim);
                textControl.setShouldConvertBlankToNull(
                        this.shouldConvertBlankToNull);
                textControl.setFormatter(getFormatter());
                textControl.setValidator(this.boxValidator);
                
                if (textControl instanceof TextboxHtmlControl) {
                    TextboxHtmlControl box = (TextboxHtmlControl) textControl;
                    
                    box.setSize(this.columns);
                } else if (textControl instanceof TextareaHtmlControl) {
                    TextareaHtmlControl box = (TextareaHtmlControl) textControl;
                    
                    box.setRows(this.rows);
                    box.setColumns(this.columns);
                    box.setWrapSetting(this.wrapSetting);
                }
            }
            
            /*
             * we must not propagate prohibited or required here because they
             * are enforced by this class and may not be meaningful if applied
             * to all the owned boxes
             */
        }

        super.registerOwnedElement(owned);
    }

    /**
     * Overrides {@code HtmlControl}; because this class overrides
     * {@code onRenderingPhaseAfterBody()} this method (and all other
     * convenience methods invoked by the superclass) is not needed and may
     * simply return an empty string.
     * 
     * @return an empty {@code String} because an overridden version of
     *         {@code onRenderingPhaseAfterBody()} handles display of this
     *         control when it is invisible.
     */
    @Override
    protected String generateHtmlToDisplayForLabel(
            @SuppressWarnings("unused") Object value) {
        return "";
    }

    /**
     * {@inheritDoc}; this version outputs nothing; because this class overrides
     * {@code onRenderingPhaseAfterBody()} this method (and all other
     * convenience methods invoked by the superclass) is not needed and may
     * simply return an empty string.
     * 
     * @see HtmlControl#generateHtmlToDisplayAndPersistValue(boolean, boolean,
     *      Object, String)
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused") boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            @SuppressWarnings("unused") Object value,
            @SuppressWarnings("unused") String rawValue) {
        return "";
    }

    /**
     * Overrides {@code HtmlControl}; because this class overrides
     * {@code onRenderingPhaseAfterBody()} this method (and all other
     * convenience methods invoked by the superclass) is not needed and may
     * simply return an empty string.
     * 
     * @return an empty {@code String} because an overridden version of
     *         {@code onRenderingPhaseAfterBody()} handles display of this
     *         control when it is invisible.
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(
            @SuppressWarnings("unused") Object value) {
        return "";
    }

    /**
     * {@inheritDoc}.  Because this class overrides
     * {@link #onRenderingPhaseAfterBody(JspWriter)}, this method (and all other
     * convenience methods invoked by the superclass) is not needed and may
     * simply return an empty string.
     * 
     * @return an empty {@code String}
     */
    @Override
    protected String generateHtmlUsingFormatter(
            @SuppressWarnings("unused") Object value) {
        return "";
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        MultiboxHtmlControl dc
                = (MultiboxHtmlControl) super.generateCopy(newId, map);
        
        dc.ownedBoxes = new ArrayList<AbstractTextHtmlControl>(
                this.ownedBoxes.size());
        for (AbstractTextHtmlControl nextBox : this.ownedBoxes) {
            dc.ownedBoxes.add((AbstractTextHtmlControl) map.get(nextBox));
        }
        dc.deleteButtons = new ArrayList<ButtonHtmlControl>(
                this.deleteButtons.size());
        for (ButtonHtmlControl nextButton : this.deleteButtons) {
            dc.deleteButtons.add((ButtonHtmlControl) map.get(nextButton));
        }
        dc.addAnotherButton
                = (ButtonHtmlControl) map.get(this.addAnotherButton);
        dc.persistedBoxCount
                = (HiddenIntHtmlControl) map.get(this.persistedBoxCount);
        
        return dc;
    }
}
