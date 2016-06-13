/*
 * Reciprocal Net project
 * 
 * HtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 19-Feb-2004: midurbin wrote second draft
 * 17-May-2004: midurbin added value prioritization
 * 17-May-2004: midurbin added 'displayAsLabel' attribute and supporting
 *              methods
 * 18-May-2004: mdirubin changed return type for setValue() to void
 * 22-Jun-2004: cwestnea added UNEDITABLE_PARSED_VALUE_PRIORITY and modified
 *              onParsingPhaseBeforeBody() to use it
 * 05-Aug-2004: midurbin moved validation error reporting to the end of the
 *              the fetching phase in the new method onFetchingPhaseAfterBody()
 * 31-Aug-2004: ekoperda fixed bug #1376 in setInitialValueFrom()
 * 24-Sep-2004: midurbin added 'prohibited' attribute and supporting methods
 *              and added an associated ErrorSupplier implementation
 * 02-Nov-2004: midurbin added generateCopy() and cloneObject() to fix bug
 *              #1439
 * 16-Nov-2004: midurbin added an ExtraHtmlAttributeAccepter implementation
 * 23-Nov-2004: midurbin clarified documentation for getValueAsString()
 * 14-Dec-2004: midurbin added Validator attachment support
 * 11-Jan-2005: jobollin moved the Validator interface to org.recipnet.common
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 08-Apr-2005: midurbin added Formatter attachment support,
 *              generateHtmlToInvisiblyPersistValue() and
 *              generateHtmlUsingFormatter()
 * 26-Jul-2005: midurbin added isValueUserEntered() factored code out of
 *              onParsingPhaseBeforeBody() into getRawValue(),
 *              isParsedValueValid() and onFetchingPhaseAfterBody()
 * 19-Aug-2005: midurbin corrected the 'validator' property's documentation
 * 16-Sep-2005: midurbin fixed bug #1344 in setInitialValueFrom()
 * 28-Oct-2005: jobollin added escapeHtmlText(), HTML_MARKUP_PATTERN, and
 *              HTML_ESCAPE_MAP for general-purpose HTML escaping.  Made
 *              escapeAttributeValue() and escapeNestedValue() delegate to the
 *              new method
 * 19-Jan-2006: jobollin changed dummy implementation of parseValue() into an
 *              abstract method; reformatted the source; updated docs
 * 30-Jan-2006: jobollin added method getValueAsStringCollection() to support
 *              possibly-multivalued controls
 * 01-Mar-2006: jobollin made generateHtmlToDisplayAndPersistValue() abstract;
 *              made generateHtmlToDisplayForLabel() not output value
 *              persistence code (consistent with its docs and uses)
 * 07-Mar-2006: jobollin partially distinguished parse-time failures from
 *              general validation failures and made the two- and three-argument
 *              versions of setValue() return a boolean indicating whether or
 *              not they actually make an assignment 
 * 12-Jun-2006: modified getValueAsBoolean() to be more explicit about the
 *              typecast it performs
 */

package org.recipnet.common.controls;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.Validator;

/**
 * <p>
 * This is an exension of {@code HtmlPageElement} that is a base class for tags
 * that are characterized by maintaining a <em>value</em> between HTTP
 * round trips. The value is stored in a an HTML form field and therefore any
 * tag extending {@code HtmlPageElement} must be nested within a form. (Also,
 * because it's a phase-recognizing tag it must be nested within an
 * {@code HtmlPage} tag.) Attribute setter and getter methods are present and
 * may be included in the TLD file for child classes as needed.
 * </p><p>
 * Value may be set at any time by various mechanisms. Whether the current value
 * is retained or overridden depends on its {@code priority} and the priority of
 * the call. The priority of a call is clearly indicated though sometimes it has
 * a different priority if the value is {@code null}. If the priority of a call
 * is less than the priority of the current {@code value} the call will have no
 * effect. The following is a list of the priority levels:
 * </p><p>
 * Proiority levels: (lowest to highest)
 * </p>
 * <dl>
 * <dt>LOWEST_PRIORITY:</dt>
 * <dd>overruled by any other value-setting call; the initial priority
 *     level</dd>
 * <dt>DEFAULT_VALUE_PRIORITY:</dt>
 * <dd>a priority level reserved for default values, that should be overruled
 *     by any existing value, or any input {@link #setInitialValue(Object)}
 *     or {@link #setInitialValueFrom(String)}
 * <dt>UNEDITABLE_PARSED_VALUE_PRIORITY:</dt>
 * <dd>a priority level for values that has been parsed from the webapp, but
 *     are not meant to be editable from the webapp. Any existing value will
 *     overwrite this value.</dd>
 * <dt>EXISTING_VALUE_PRIORITY:</dt>
 * <dd>a priority level reserved for values from past user input, probably
 *     stored in a database and usually set during the FETCHING_PHASE. This
 *     value will overrule a value with DEFAULT_VALUE_PRIORITY.</dd>
 * <dt>PARSED_VALUE_PRIORITY:</dt>
 * <dd>a priority level reserved for input parsed from an HTTP post. This is
 *     the highest priority level.</dd>
 * <dt>IGNORE_PRIORITY:</dt>
 * <dd>not a priority level, but an indicator that the current attempt to set
 *     the value should succeed without checking or altering the current
 *     priority level of the value. Be aware that your value may take a minimal
 *     priority level and thus be overruled by the propagation of default values
 *     from phase to phase.</dd>
 * </dl>
 * <p>
 * In order to allow deep copies of {@code HtmlControl} objects, a method exists
 * that must be able to clone every type of object that could be used as the
 * 'value' of a given {@code HtmlControl}. This method
 * ({@link #cloneObject(Object)}) must be overridden if a new type of object is
 * used as the value of a control that descends from this class.
 * </p><p>
 * This class implements {@code ExtraHtmlAttributeAccepter} so that all
 * subclasses may have attributes added to the HTML tag they output. The
 * mechanisms for storing extra attributes have been fully implemented and
 * {@link #getExtraHtmlAttributesAsString()} has been made available so that
 * subclasses may simply include the attributes in their output.
 * </p><p>
 * A {@code Validator} may be attached to this control. In such cases, its
 * {@link Validator#isValid(Object)} method is invoked on the parsed 'rawValue'
 * for this control before the 'value' is updated, during the
 * {@code PARSING_PHASE}. If no value was parsed, or the value was already
 * deemed invalid, the validator is not invoked.
 * </p><p>
 * An {@code HtmlFormatter} may also be attached to this control. In such cases
 * its {@link HtmlFormatter#formatObject(Object)} method will be invoked on the
 * current value of this control during the {@code RENDERING_PHASE}, and
 * therefore {@code HtmlFormatter} objects must only be attached when the value
 * is of a type that they can format. Also note that any display-altering
 * properties defined by subclasses will not be applied when an
 * {@code HtmlFormatter} is present, but instead the formatter will be
 * responsible for all formatting with the exception of the escaping of HTML in
 * {@code String}-valued 'values'.
 * </p>
 */
public abstract class HtmlControl extends HtmlPageElement implements
        ExtraHtmlAttributeAccepter, ErrorSupplier {

    /**
     * A value priority code representing the lowest priority
     */
    public static int LOWEST_PRIORITY = 0;

    /**
     * A value priority code representing the priority of a control's default
     * value
     */
    public static int DEFAULT_VALUE_PRIORITY = 1;

    /**
     * A value priority code representing the priority of an uneditable
     * control's parsed value
     */
    public static int UNEDITABLE_PARSED_VALUE_PRIORITY = 2;

    /**
     * A value priority code representing the priority of an explicit value
     * obtained from persistent storage
     */
    public static int EXISTING_VALUE_PRIORITY = 3;

    /**
     * A value priority code representing the priority of a control value that
     * is parsed from user input
     */
    public static int PARSED_VALUE_PRIORITY = 4;

    /**
     * A value priority pseudo-code that instructs the {@code setValue()}
     * methods to set a specified value without regard to (and without changing)
     * the priority of the existing value
     */
    public static int IGNORE_PRIORITY = 100;

    /**
     * An error flag that may be reported when the parsed value was null and the
     * {@code required} attribute was set to true. This is used in the
     * implementation of {@code ErrorSupplier}.
     */
    public static final int REQUIRED_VALUE_IS_MISSING = 1 << 0;

    /**
     * An error flag that may be reported when the parsed value is non-null and
     * the {@code prohibited} attribute was set to true. This is used in the
     * implementation of {@code ErrorSupplier}.
     */
    public static final int PROHIBITED_VALUE_IS_PRESENT = 1 << 1;

    /**
     * An error flag that may be reported when an attached {@code Validator}
     * reports that this tag's value is not valid.
     */
    public static final int VALIDATOR_REJECTED_VALUE = 1 << 2;

    /**
     * A {@code Pattern} that matches individual characters that are significant
     * in HTML / XHTML markup, specifically the less-than sign (&lt;), the
     * greater-than sign (&gt;), the ampersand (&amp;) and the double quote (").
     * It does not match the apostrophe (') even though that character can be
     * used as an attribute value delimiter, because there is no portable way to
     * replace it.
     */
    private final static Pattern HTML_MARKUP_PATTERN
            = Pattern.compile("[<>&\"]");

    /**
     * A {@code Map} from characters significant in HTML / XHTML markup to safe,
     * portable, named entity replacements. All the characters matched by
     * {@link #HTML_MARKUP_PATTERN} are mapped to replacements.
     */
    private final static Map<String, String> HTML_ESCAPE_MAP
            = new HashMap<String, String>();

    /*
     * Initializes HTML_ESCAPE_MAP
     */
    static {
        HTML_ESCAPE_MAP.put("<", "&lt;");
        HTML_ESCAPE_MAP.put(">", "&gt;");
        HTML_ESCAPE_MAP.put("\"", "&quot;");
        HTML_ESCAPE_MAP.put("&", "&amp;");
    }

    /**
     * Stores the initial value of this field. This value may be set via the
     * 'initialValue' or 'initialValueFrom' attributes or parsed from the nested
     * text. At the end of the {@code REGISTRATION_PHASE} the value (it not set)
     * is set to equal this {@code initialValue}.
     */
    private Object initialValue;

    /**
     * Stores the value that is persisted by this {@code HtmlControl}. This
     * value is initialized to null by {@code reset()} and should only be
     * altered by calls that have a higher 'priority' than
     * {@code currentValuePriority}. To ensure this is the case, this field is
     * only altered by calls to {@code setValue()}. This is a transient
     * variable, in that it may be altered by calls to
     * {@code copyTransientPropertiesFrom()} for an object whose {@code value}
     * has an equal or higher {@code priority}.
     */
    private Object value;

    /**
     * Stores the priority of the current {@code value}. A value may not be
     * overwritten by a call with a lower {@code priority}. This variable is
     * initialized to the lowest priority by {@code reset()} and is updated
     * whenever {@code value} is overwritten.
     */
    private int priority;

    /**
     * Stores the {@code String} taken from the HTTP post parameters. This value
     * is initialized by {@code reset()} and set by
     * {@code onParsingPhaseBeforeBody()}.
     */
    private String rawValue;

    /**
     * This is an optional attribute that indicates whether a field should be
     * displayed. It is initialized to false by {@code reset()} and may be
     * altered by its setter method {@code setVisible()}. This variable is
     * 'transient' (meaning it may change from phase to phase) and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean visible;

    /**
     * This is an optional attribute that indicates whether the value of this
     * control may be edited by the user. It is initialized to true by
     * {@code reset()} and may be altered by its setter method
     * {@code setEditable()}. This variable is 'transient' (meaning it may
     * change from phase to phase) and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean editable;

    /**
     * This is an optional attribute that indicates whether this control should
     * be displayed as (escaped) plain text. When this is set to true,
     * {@code editable} is ignored. This variable is 'transient' (meaning it may
     * change from phase to phase) and is copied by
     * {@code copyTransientPropertiesfrom()}.
     */
    private boolean displayAsLabel;

    /**
     * This is an optional attribute that indicated whether a value is required
     * for this control. If a required field is empty after a post,
     * {@code failedValidation} will be {@code true}. This variable is
     * initialized to false by {@code reset()} and may be set by its setter
     * method {@code setRequired()}. This variable is 'transient' (meaning it
     * may change from phase to phase) and is copied by
     * {@code copyTransientPropertiesFrom()}. This variable is 'transient'
     * (meaning it may change from phase to phase) and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean required;

    /**
     * This is an optional attribute that indicates whether a value may be
     * entered for this control. If a prohibited field is non-empty after a
     * post, {@code failedValidation} will be set to {@code true}. This
     * variable is initialized to false by {@code reset()} and may be set by its
     * setter method {@code setProhibited()}. This variable is 'transient'
     * (meaning it may change from phase to phase) and is copied by
     * {@code copyTransientPropretiesFrom()}.
     */
    private boolean prohibited;

    /**
     * A mapping of attribute name {@code String}s to attribute value
     * {@code String}s representing extra attributes that should be included in
     * the appropriate HTML tag for this control. Attribute/value pairs are all
     * cleared by {@code reset()} and are added by calls to
     * {@code addExtraHtmlAttribute()}.
     */
    private Map<String, String> extraHtmlAttributes;

    /**
     * A flag raised to indicate that the raw control value parsed from the
     * request could not be parsed.  This is to be distinguished from validation
     * failure. 
     */
    private boolean unparseable;
    
    /**
     * {@code failedValidation} is not accessible as an attribute but is
     * accessible on scripting variables and to owning elements. It is
     * initialized to false by {@code reset()} and its value may be altered by
     * calls to {@code setFailedValidation()}.
     */
    private boolean failedValidation;

    /**
     * This is an optional attribute. When {@code failedValidation} is true, and
     * HTML is generated for a particular control, {@code faildValidationHtml}
     * is appended. This {@code String} is initialized to null and may be set by
     * calls to its 'setter' method, {@code setFailedValidationHtml()}.
     */
    private String failedValidationHtml;

    /**
     * An optional attribute that may be set to a {@code Validator} that will be
     * used during the {@code FETCHING_PHASE} right before the
     * {@code PROCESSING_PHASE} to validate the parsed value.
     */
    private Validator validator;

    /**
     * An optional propety that may be set to an {@code HtmlFormatter} that will
     * be used during the {@code RENDERING_PHASE} if 'displayAsLabel' is set to
     * true to mark up the current value in HTML. The formatter's
     * {@link org.recipnet.common.HtmlFormatter#formatObject formatObject()}
     * method must be able to handle objects of the type of the 'value' of this
     * {@code HtmlControl}. Note: whan this property is set, display side
     * properties defined on the subclass will be overrridden.
     */
    private HtmlFormatter formatter;

    /** used to implement {@code ErrorSupplier} */
    private int errorCode;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.initialValue = null;
        this.value = null;
        this.priority = LOWEST_PRIORITY;
        this.rawValue = null;
        this.editable = true;
        this.displayAsLabel = false;
        this.visible = true;
        this.required = false;
        this.prohibited = false;
        this.extraHtmlAttributes = null;
        this.unparseable = false;
        this.failedValidation = false;
        this.failedValidationHtml = null;
        this.validator = null;
        this.formatter = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /** @return The {@code initialValue}. */
    public Object getValue() {
        return this.value;
    }

    /**
     * Sets the value of this control while retaining the current value's
     * priority
     * 
     * @param  value an {@code Object} representing the new value
     */
    public void setValue(Object value) {
        boolean success = setValue(value, IGNORE_PRIORITY, IGNORE_PRIORITY);
        
        assert success;
    }

    /**
     * Attempts to set the value of this control to the specified value,
     * asserting the specified value priority.  This control's value is modified
     * only if the asserted priority is at least as great as the current value's
     * priority, or if priority is {@code IGNORE_PRIORITY}
     * 
     * @param  value an {@code Object} representing the new value
     * @param  priority the priority code associated with {@code value}
     * 
     * @return {@code true} if the specified value was set, {@code false} if
     *         not; whether the specified value differs from this control's
     *         prior value is not a consideration 
     */
    public boolean setValue(Object value, int priority) {
        return setValue(value, priority, priority);
    }

    /**
     * Attempts to set the value of this control to the specified value,
     * asserting possibly different priorities for {@code null} and
     * non-{@code null} values.  This control's value is modified only if the
     * asserted value-dependent priority is at least as great as the current
     * value's priority, or if {@code priority} is {@code IGNORE_PRIORITY}
     * (regardless of the value of {@code priorityIfNull} in that case).
     * 
     * @param  value an {@code Object} representing the new value
     * @param  priority the priority code associated with {@code value} if it
     *         is non-{@code null}
     * @param  priorityIfNull the priority code associated with {@code value} if
     *         it is {@code null}
     * 
     * @return {@code true} if the specified value was set, {@code false} if
     *         not; whether the specified value differs from this control's
     *         prior value is not a consideration 
     */
    public boolean setValue(Object value, int priority, int priorityIfNull) {
        boolean rval = false;
        
        if (priority == IGNORE_PRIORITY) {
            this.value = value;
            rval = true;
        } else {
            int effectivePriority
                    = ((value == null) ? priorityIfNull : priority);
            
            if (effectivePriority >= this.priority) {
                this.value = value;
                this.priority = effectivePriority;
                rval = true;
            }
        }
        
        return rval;
    }

    /**
     * Gets the value as an equivalent {@code String}. If the value is null, an
     * empty {@code String} is returned. This method should only be called for
     * display-side code and should not be used to get the value for storage in
     * a container object.
     * 
     * @return {@code value.toString()} or an empty String if the value is null.
     */
    public String getValueAsString() {
        return (this.value == null) ? "" : this.value.toString();
    }

    /**
     * Returns this control's value in the form of a string collection
     * 
     * @return this control's value as a {@code String} collection; if the value
     *         is a {@code Collection} then it is cast to
     *         {@code Collection&lt;String&gt;} (which is not a typesafe
     *         operation, so care is required); if it is a {@code String[]} then
     *         a {@code List&lt;String&gt;} view is constructed; if it is
     *         {@code null} then an empty {@code Collection} is constructed;
     *         otherwise the value is coerced to a {@code String} and wrapped in
     *         a {@code Collection}. It should not be assumed that the returned
     *         {@code Collection} is modifiable.
     */
    @SuppressWarnings("unchecked")
    public Collection<String> getValueAsStringCollection() {
        if (this.value instanceof Collection) {
            return (Collection<String>) this.value;
        } else if (this.value == null) {
            return Collections.<String>emptySet();
        } else if (this.value instanceof String[]) {
            return Arrays.asList((String[]) this.value);
        } else {
            return Collections.singleton(getValueAsString());
        }
    }
    
    /** @return The {@code value} cast as an {@code Integer}. */
    public Integer getValueAsInteger() {
        return (Integer) this.value;
    }

    /** @return The {@code value} cast as a {@code int}. */
    public int getValueAsInt() {
        return getValueAsInteger().intValue();
    }

    /** @return The {@code value} cast as a {@code Double}. */
    public Double getValueAsDouble() {
        return (Double) this.value;
    }

    /**
     * Returns the value of this control as a {@code boolean}.  The assigned
     * value object should be {@code null} or a {@code Boolean} if this method
     * is invoked.
     *   
     * @return the value as a {@code boolean}; {@code false} if the value is
     *         {@code null}
     */
    public boolean getValueAsBoolean() {
        /*
         * Note: the cast in the below expression is purposeful.  It causes a
         * ClassCastException if this control's value is not assignable to type
         * Boolean -- if the value is a String, for instance.  This behavior is
         * desirable for exposing programming errors.
         */
        return Boolean.TRUE.equals(Boolean.class.cast(this.value));
    }

    /**
     * Sets the {@code initialValue}. At the end of the
     * {@code REGISTRATION_PHASE}, {@code value} may be changed to reflect the
     * value of {@code initialValue} if the priority of value is still
     * sufficiently low.
     * 
     * @param initialValue If the {@code initialValue} is null, (hasn't been set
     *        by previous calls to this method or {@code setInitialValueFrom()})
     *        it is set to this parameter.
     */
    public void setInitialValue(Object initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * <p>
     * Sets the {@code initialValue} attribute of this control to the parsed
     * value of the query-line parameter with a caller-specified name. See also
     * the {@code onRegistrationPhaseAfterBody()} method of this class, which
     * may propagate the value of the {@code initialValue} attribute to the
     * {@code value} attribute under some circumstances. Because this
     * propagation is meant to be done during the {@code REGISTRATION_PHASE}
     * calls to this method during any other phase are ignored and have no
     * effect.
     * </p><p>
     * The current implementation of this method parses the query-line
     * parameter's value by invoking {@code parseValue()} behind the scenes.
     * </p>
     * 
     * @param parameterName the name of the query-line parameter whose value
     *        should be read.
     */
    public void setInitialValueFrom(String parameterName) {
        if (getPage().getPhase() == HtmlPage.REGISTRATION_PHASE) {
            this.initialValue = parseValue(
                    pageContext.getRequest().getParameter(parameterName));

            // It's important that the 'unparseable' state be preserved
            // during this method. Subclass implementations of parseValue()
            // are permitted by specification to set unparseable to true,
            // but that would be inappropriate in this case because we're not
            // parsing any actual user input. Thus, we need to undo any
            // setUnparseable() change that parseValue() may have effected.
            setUnparseable(false);
        }
    }

    /**
     * @return The current {@code initialValue}
     */
    public Object getInitialValue() {
        return this.initialValue;
    }

    /** @return Whether this {@code HtmlControl} is visible. */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * @param visible Indicates whether this {@code HtmlControl} should be
     *        visible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /** @return Whether this {@code HtmlControl} is editable. */
    public boolean getEditable() {
        return this.editable;
    }

    /**
     * @param editable Indicates whether this {@code HtmlControl} should be
     *        editable.
     */
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    /**
     * @return Whether this {@code HtmlControl} should be displayed as a label.
     */
    public boolean getDisplayAsLabel() {
        return this.displayAsLabel;
    }

    /**
     * @param displayAsLabel Indicates whether this {@code HtmlControl} should
     *        be displayed as a label.
     */
    public void setDisplayAsLabel(boolean displayAsLabel) {
        this.displayAsLabel = displayAsLabel;
    }

    /**
     * @return Whether this {@code HtmlControl} requires a value to be entered.
     */
    public boolean getRequired() {
        return this.required;
    }

    /**
     * @param required Indicates whether this {@code HtmlControl} should be
     *        required.
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    /**
     * @return Whether this {@code HtmlControl} disallows a value to be entered.
     */
    public boolean getProhibited() {
        return this.prohibited;
    }

    /**
     * @param prohibited Indicates whether this {@code HtmlControl} should be
     *        prohibited.
     */
    public void setProhibited(boolean prohibited) {
        this.prohibited = prohibited;
    }

    /**
     * Determines whether this control attempted unsuccessfully to parse a value
     * from the request
     * 
     * @return {@code true} if this control attempted to parse a value from
     *         the request (i.e. when handling a POST request) but was unable to
     *         successfully do so; otherwise {@code false}
     */
    public boolean isUnparseable() {
        return unparseable;
    }

    /**
     * Raises (or lowers) the {@code unparseable} flag for this control
     * 
     * @param  unparseable {@code true} to flag this control is having attempted
     *         to parse a value but failed; {@code false} to lower any such
     *         flag currently set on this control
     */
    public void setUnparseable(boolean unparseable) {
        this.unparseable = unparseable;
    }

    /** @return Whether this {@code HtmlControl} has failed validation. */
    public boolean getFailedValidation() {
        return this.failedValidation;
    }

    /**
     * @param failedValidation Indicates whether this {@code HtmlControl} has
     *        failed validation.
     * @throws IllegalStateException if called after the {@code FETCHING_PHASE}.
     */
    public void setFailedValidation(boolean failedValidation) {
        if ((getPage().getPhase() == HtmlPage.PROCESSING_PHASE)
                || (getPage().getPhase() == HtmlPage.RENDERING_PHASE)) {
            throw new IllegalStateException();
        }
        this.failedValidation = failedValidation;
    }

    /**
     * @return The HTML that is appended to the control to indicate a validation
     *         error.
     */
    public String getFailedValidationHtml() {
        return this.failedValidationHtml;
    }

    /**
     * @param failedValidationHtml The HTML that is to be appending to the
     *        control if a validation error occurs.
     */
    public void setFailedValidationHtml(String failedValidationHtml) {
        this.failedValidationHtml = failedValidationHtml;
    }

    /**
     * @return the {@code Validator} object that has been attached to validate
     *         the parsed value of this control.
     */
    public Validator getValidator() {
        return this.validator;
    }

    /**
     * @param validator a {@code Validator} object to attach to this control to
     *        validate the value before the {@code PROCESSING_PHASE}
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * @return the {@code HtmlFormatter} object that has been attached to format
     *         the display value of this control
     */
    public HtmlFormatter getFormatter() {
        return this.formatter;
    }

    /**
     * @param formatter an {@code HtmlFormatter} object to attach to this
     *        control to format the display value; this {@code HtmlFormatter}
     *        must be able to format objects of the type of this
     *        {@code HtmlControl}s value.
     */
    public void setFormatter(HtmlFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     *
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode;
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
     * Implements {@code ExtraHtmlAttributeAccepter}. This method instructs the
     * {@code HtmlControl} to include an extra attribute in the appropriate tag
     * for this control with the indicated name and value. For example, if this
     * is a {@code ButtonHtmlControl} the attributes will be added to the button
     * HTML input tag.
     * 
     * @param name the name of the attribute
     * @param value the value of the attribute
     */
    public void addExtraHtmlAttribute(String name, String value) {
        if (this.extraHtmlAttributes == null) {
            this.extraHtmlAttributes = new HashMap<String, String>();
        }
        this.extraHtmlAttributes.put(name, value);
    }

    /**
     * Converts the {@code Map} of extra attributes and their values into a
     * {@code String} of the format: name1="value1" name2="value2" This value
     * may be directly inserted into the HTML for the HTML control.
     * 
     * @return a formatted {@code String} of attribute/value pairs.
     */
    public String getExtraHtmlAttributesAsString() {
        if (this.extraHtmlAttributes == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            
            for (Entry<String, String> entry : extraHtmlAttributes.entrySet()) {
                sb.append(" ").append(entry.getKey());
                sb.append("=\"").append(entry.getValue()).append("\"");
            }
            
            return sb.toString();
        }
    }

    /**
     * Overrides {@code HtmlPageElement}; called before the body of this tag is
     * evaluated for the {@code REGISTRATION_PHASE}. The current implementation
     * simply returns {@code EVAL_BODY_BUFFERED} and delegates back to
     * {@code HtmlPageElement}.
     * 
     * @param pageContext The {@code PageContext} for this Tag.
     * @return {@code BodyTagSupport.EVAL_BODY_BUFFERED}
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);
        /*
         * Do nothing for now; just buffer any child content for later
         * processing in onRegistrationPhaseAfterBody().
         */
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Overrides {@code HtmlPageElement}; called after the body of this tag is
     * evaluated for the {@code REGISTRATION_PHASE}. In the current
     * implementation if the {@code value} is not set, it is set to the
     * {@code bodyContent}. Then it delegates back to the superclass.
     * 
     * @param pageContext The {@code PageContext} for this Tag.
     * 
     * @return the return value of the superclass' implementation is returned
     * 
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if ((super.bodyContent != null) && (this.initialValue == null)) {
            setInitialValue(convertBodyContentToValue(bodyContent.getString()));
        }
        this.setValue(getInitialValue(), DEFAULT_VALUE_PRIORITY,
                LOWEST_PRIORITY);
        
        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * Overrides {@code HtmlPageElement}; provides default parsing
     * functionality.  The 'rawValue' is determined by a call to
     * {@code getRawValue()} and then supplied to the {@code parseValue()}
     * method, whose return value is stored as the 'value'.
     * 
     * @param request the {@code ServletRequest} object for the HTTP post
     *        operation whose response is being generated
     *        
     * @return the return value of the superclass' implementation is returned
     * 
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        this.rawValue = getRawValue(request);
        
        setValue(parseValue(this.rawValue),
                getEditable()
                    ? PARSED_VALUE_PRIORITY
                    : UNEDITABLE_PARSED_VALUE_PRIORITY);
        
        return super.onParsingPhaseBeforeBody(request);
    }

    /**
     * Gets the raw value for this {@code HtmlControl} from the request. This
     * implementation simply gets the parameter with the name equal to the
     * current control's id value. Subclasses should override this if their raw
     * value must be determined in a different way, as this returned value will
     * be supplied to {@link #parseValue(String)}.
     * 
     * @param  request a {@code ServletRequest} from which to determine this
     *         control's raw value
     *         
     * @return the raw value of this control, as a {@code String}
     */
    protected String getRawValue(ServletRequest request) {
        return request.getParameter(getId());
    }
    
    /**
     * A method to determine whether the current 'value' is parsed (POSTed) user
     * input. This method is helpful because only POSTed user-input data
     * needs to be checked for validity.
     * 
     * @return true if the value is user input that requires validation
     *         checking, and false otherwise
     */
    private boolean isValueUserEntered() {
        // the current priority level of the value indicates how it was set, so
        // as long as the priority is set to a level indicative of a parsed
        // user-entered value we can assume that it must be validated
        return ((this.priority == UNEDITABLE_PARSED_VALUE_PRIORITY)
                || (this.priority == PARSED_VALUE_PRIORITY));
    }

    /**
     * This method is invoked by {@code HtmlControl} at the end of the
     * {@code FETCHING_PHASE} when the value is known to be user-entered. If
     * this method returns false indicating that the value is invalid, the
     * caller should set 'value' to null, set 'failedValidation' to true and
     * report the validation failure to surrounding {@code ValidationContext}s.
     * This method may set one or more {@code ErrorSupplier} error flags.
     * Subclasses may override this class to add more strict validation checks
     * but must delgate back to the superclass in all cases to ensure that its
     * validation checks are performed as well and that it gets a chance to set
     * any error flags.  This method does not make use of any stored information
     * about previously detected validation failures (i.e. the current value of
     * the {@code failedValidation} property).
     * 
     * @param parsedValue the current 'value' of this control, assumed to be of
     *        the runtime type and form returned by {@link #parseValue(String)}
     * @return true if the the 'parsedValue' is valid according to this and all
     *         the superclass' validation checks.
     */
    protected boolean isParsedValueValid(Object parsedValue) {
        if (this.required && (parsedValue == null)) {
            setErrorFlag(REQUIRED_VALUE_IS_MISSING);
            return false;
        } else if (this.prohibited && (parsedValue != null)) {
            setErrorFlag(PROHIBITED_VALUE_IS_PRESENT);
            return false;
        } else if ((this.validator != null) && (parsedValue != null)
                && !this.validator.isValid(parsedValue)) {
            setErrorFlag(VALIDATOR_REJECTED_VALUE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Overrides {@code HtmlPageElement}; in the current implementation if the
     * value was user-entered it is validated and any validation errors are
     * reported to the most immediate {@code ValidationContext} in which this
     * control is nested.
     * 
     * @return the return value of the superclass' implementation is returned
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        // if the value is successfully parsed user input, check it's validity
        if (isValueUserEntered()) {
            setFailedValidation(isUnparseable()
                    || !isParsedValueValid(getValue()));
        }

        // If a validation error was detected, report it to all
        // ValidationContexts in which this tag is nested and set the value to
        // null so that no illegal value will be processed
        if (getFailedValidation()) {
            ValidationContext vc
                    = findRealAncestorWithClass(this, ValidationContext.class);
            
            setValue(null);
            if (vc != null) {
                vc.reportValidationError();
            }
        }
        
        return super.onFetchingPhaseAfterBody();
    }

    /**
     * Overrides {@code HtmlPageElement}; determines whether the body should be
     * evaluated based on the {@code visible} attribute and delegates back to
     * the superclass.
     * 
     * @param out the {@code JspWriter} to which the HTML for the control is
     *        written
     * @return the return value of the superclass' implementation is returned
     * @throws IOException if an error occurs while writing to the JspWriter.
     * @throws JspException if any other exceptions are encountered during this
     *         method.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        return this.visible ? EVAL_BODY_BUFFERED : SKIP_BODY;
    }

    /**
     * Overrides {@code HtmlPageElement}; writes the result of
     * {@code generateHtmlToDisplayAndPersistValue()} (which may be overrided by
     * subclasses to accomodate the various element types) if the element is
     * {@code visible} and 'displayAsLabel' is false. Details about editability,
     * validation failures and value are passed to
     * {@code generateHtmlToDisplayAndPersistValue()}. If 'displayAsLabel' is
     * true, and no 'formatter' has been specified, the results of
     * {@code generateHtmlToDisplayForLabel()} and
     * {@code generateHtmlToInvisiblyPersistValue()} are written to the provided
     * {@code JspWriter} instead. If 'displayAsLabel' is true and a formatter
     * <strong>has</strong> been specified, the results of
     * {@code generateHtmlToDisplayUsingFormatter()} and
     * {@code generateHtmlToInvisiblyPersistValue()} are written to the provided
     * {@code JspWriter}. If 'visible' is set to false, then just the result of
     * {@code generateHtmlToInvisiblyPersistValue()} is written. This method
     * delegates back to the superclass to determine the return code.
     * 
     * @param out the {@code JspWriter} to which the HTML for the control is
     *        written
     *        
     * @return the return value of the superclass' implementation is returned
     * 
     * @throws IOException if an error occurs while writing to the JspWriter.
     * @throws JspException if any other exceptions are encountered during this
     *         method.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        if (!this.visible) {
            out.print(generateHtmlToInvisiblyPersistValue(getValue()));
        } else if (this.displayAsLabel) {
            if (getFormatter() != null) {
                out.print(generateHtmlUsingFormatter(getValue()));
            } else {
                out.print(generateHtmlToDisplayForLabel(getValue()));
            }
            out.print(generateHtmlToInvisiblyPersistValue(getValue()));
        } else {
            out.print(generateHtmlToDisplayAndPersistValue(this.editable,
                    getFailedValidation(), getValue(), this.rawValue)
                    + (((this.failedValidationHtml != null)
                            && getFailedValidation() && this.editable)
                            ? this.failedValidationHtml
                            : ""));
        }
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * Converts a raw string value parsed from request parameters 
     * to an object that represents this control's logical value. If there is
     * a parsing error then {@code unparseable} may be set to {@code true}
     * and an {@code ErrorSupplier} error flag may be set before returning.
     * 
     * @param rawValue a {@code String} retrieved from the POST parameters
     * 
     * @return the parsed value, or null if it could not be parsed (in such
     *         cases, {@code setUnparseable(true)} will have been called
     *         and possibly an {@code ErrorSupplier} error flag will have been
     *         set)
     */
    /*
     * TODO: Consider whether the same parsing should be applied however a value
     * is set
     */
    abstract protected Object parseValue(String rawValue);

    /**
     * Converts this control's body content to a logical value. This version
     * always returns {@code null}; subclasses that want meaningful
     * interpretations of their body content must override this method.
     * 
     * @param content a {@code String} containing the content to convert
     * 
     * @return the parsed value of the body content, or {@code null} if it is
     *         unparsable.
     */
    protected Object convertBodyContentToValue(
            @SuppressWarnings("unused") String content) {
        return null;
    }
    
    /**
     * Generates HTML code that exposes the value of this control in an HTML
     * form element for input (though if 'editable' is false, the form field
     * will be read-only). In the event that a value parsed during the
     * {@code PARSING_PHASE} was invalid, this method will generate HTML that
     * exposes the invalid parsed value with the value of the
     * 'failedValidationHtml' property appeneded to the output. Unlike
     * {@code generateHtmlToDisplayAsLabel()} and
     * {@code generateHtmlToDisplayUsingFormatter()} this method ensures the the
     * user-editable value is posted and no other HTML needs to be generated in
     * order to preserve it across an HTTP roundtrip. The base class
     * implementation simply returns an empty string and should be overridden by
     * all subclasses.
     * 
     * @param editable indicates whether the particular field should be
     *        editable. In some cases this will result in dramatically different
     *        HTML.
     * @param failedValidation indicates that the field has an invalid value
     * @param value the value of the control
     * @param rawValue the value from the POST parameters, this will differ from
     *        the value in cases of validation failures to preserve the validity
     *        of the {@code value}.
     *        
     * @return a {@code String} that represents an HTML rendering of this
     *         control. The base implementation simply returns am empty string
     *         and should be overridden as neccessary.
     */
    abstract protected String generateHtmlToDisplayAndPersistValue(
            @SuppressWarnings("unused") boolean editable,
            @SuppressWarnings("unused") boolean failedValidation,
            @SuppressWarnings("unused") Object value,
            @SuppressWarnings("unused") String rawValue);

    /**
     * <p>
     * Generates HTML code for the value of this control. This method should be
     * used in conjunction with {@code generateHtmlToInvisiblyPersistValue()} in
     * cases when the value must be displayed to the user in a way that does not
     * allow modification but is persisted over HTTP roundtrips.
     * </p><p>
     * This method is invoked by {@code HtmlControl} at the end of the
     * {@code RENDERING_PHASE} if the 'displayAsLabel' property have been set
     * but the 'formatter' property is unset.
     * </p>
     * 
     * @param  val the {@code Object} from which to generate HTML code
     * 
     * @return the escaped value of this control so that it may be displayed as
     *         part of an HTML document
     */
    protected String generateHtmlToDisplayForLabel(Object val) {
        return (val == null) ? "" : escapeNestedValue(val.toString());
    }

    /**
     * <p>
     * Generates HTML code that displays the result of having the
     * {@code HtmlFormatter} configured on this control format the specified
     * value. This method should be used in conjunction with
     * {@code generateHtmlToInvisiblyPersistValue()} in cases when the value
     * must be displayed using the {@code HtmlFormatter}, but the unformatted
     * value must be persisted over HTTP roundtrips.  This method should not
     * be used when no formatter has been configured.
     * </p><p>
     * This method is invoked by {@code HtmlControl} at the end of the
     * {@code RENDERING_PHASE} if the 'formatter' and 'displayAsLabel'
     * properties have been set.
     * </p>
     * 
     * @param val the value of this {@code HtmlControl}
     * 
     * @return a {@code String} containing an HTML representation of the value
     *         of this control
     *         
     * @throws IllegalStateException if no formatter is configured on this
     *         control
     */
    protected String generateHtmlUsingFormatter(Object val) {
        HtmlFormatter formatter = getFormatter();
        
        if (formatter == null) {
            throw new IllegalStateException("No formatter");
        } else {
            return formatter.formatObject(val);
        }
    }

    /**
     * Returns a representation of the specified value, formatted in HTML,
     * without any accompanying code to persist the value across an HTTP round
     * trip.  This version delegates to
     * {@link #generateHtmlUsingFormatter(Object)} or
     * {@link #generateHtmlToDisplayForLabel(Object)}, depending on whether or
     * not a formatter has been assigned to this control.
     * 
     * @param  val an {@code Object} representing the value of this control
     *  
     * @return a {@code String} containing the HTML code representing the
     *         specified value
     */
    public String generateHtmlToDisplayValueOnly(Object val) {
        return ((getFormatter() == null) ? generateHtmlToDisplayForLabel(val)
                : generateHtmlUsingFormatter(val));
    }
    
    /**
     * <p>
     * Generates HTML code that includes the current value of this control in an
     * HTML form field such that it may be preserved over an HTTP roundtrip
     * without it being visible in the rendered HTML. The format of the field
     * must be such that if included as part of a form POST, this control's
     * {@code parseValue()} method will accurately reproduce the current
     * 'value'. If a control is meant to be visible (but not user-editable) this
     * method should be used in conjunction with either
     * {@code generateHtmlToDisplayForLabel()} or
     * {@code generateHtmlToDisplayUsingFormatter()} to generate the HTML.
     * </p><p>
     * This method is invoked by {@code HtmlControl} at the end of the
     * {@code RENDERING_PHASE} whenever the value of this control must be
     * preserved over an HTTP roundtrip but not exposed to user input.
     * </p><p>
     * The base class implementation simply outputs a hidden form field with an
     * id/name set to this control's 'id' value and a value set to the escaped
     * {@code String} representation of the value of this control or an empty
     * {@code String} if the 'value' is null Subclasses that cannot persist
     * their values in this way must override this method.
     * </p>
     * 
     * @param value the 'value' of this control that will be escaped and
     *        included as the value of an invisible HTML form field
     *        
     * @return a {@code String} that contains enough HTML to preserve the
     *         'value' of this control but does NOT contain anything that would
     *         be rendered visibly by a standards-compliant browser (including
     *         whitespace)
     */
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        if (value == null) {
            return "";
        } else {
            return "<input type=\"hidden\" id=\"" + getId() + "\" name=\""
                    + getId() + "\" value=\""
                    + escapeAttributeValue(value.toString()) + "\" />";
        }
    }

    /**
     * A method that copies all transient fields from {@code source} which is
     * assumed to be of the same class as this {@code HtmlControl}.  Properties
     * for which setter methods are provided are set via those setter methods.
     * Subclasses that override this method must delegate back to their
     * superclass to ensure that <strong>all</strong> transient properties are
     * copied.
     * 
     * @param  source the {@code HtmlPageElement} from which to copy properties;
     *         should be of the same class as this {@code HtmlControl}, and may
     *         not be {@code null}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        HtmlControl src = (HtmlControl) source;

        super.copyTransientPropertiesFrom(source);
        setInitialValue(src.initialValue);
        setValue(src.value, src.priority, src.priority);
        setEditable(src.editable);
        setDisplayAsLabel(src.displayAsLabel);
        setVisible(src.visible);
        setRequired(src.required);
        setProhibited(src.prohibited);
    }

    /**
     * {@inheritDoc}.  This version delegates to the superclass, then creates a
     * deep copy of any assigned extra HTML attributes and clones the intial and
     * current values of this control.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HtmlControl dc = (HtmlControl) super.generateCopy(newId, map);
        
        if (this.extraHtmlAttributes != null) {
            dc.extraHtmlAttributes = new HashMap<String, String>(
                    this.extraHtmlAttributes);
        }
        dc.value = HtmlControl.cloneObject(this.value);
        dc.initialValue = HtmlControl.cloneObject(this.initialValue);
        
        return dc;
    }

    /**
     * <p>
     * Processes a {@code String} that is intended for use as an (X)HTML
     * attribute value to produce a version in which all<sup>*</sup>
     * characters that might otherwise be interpreted as markup are replaced by
     * suitable named entities. This method delegates to
     * {@link #escapeHtmlText(String)}, see that method for more information.
     * </p><p>
     * <sup>*</sup>More or less. The apostrophe (&apos;) is not replaced (see
     * {@code escapeHtmlText(String)}). This is no problem whatsoever as long
     * as the escaped attribute value provided by this method is always placed
     * in double quote (&quot;) delimiters and not apostrophes.  XML defines a
     * named entity for the apostrophe, but HTML (as of version 4.01) does not.
     * </p>
     * 
     * @param value the {@code String} to be escaped
     * 
     * @return an escaped version of the input string
     */
    public static String escapeAttributeValue(String value) {
        return escapeHtmlText(value);
    }

    /**
     * Processes a {@code String} that is intended for use as all or part of an
     * (X)HTML element's text content to produce a version in which all
     * characters that might otherwise be interpreted as markup are replaced by
     * suitable named entities. This method delegates to
     * {@link #escapeHtmlText(String)}, see that method for more information.
     * 
     * @param  value the {@code String} to be escaped
     * 
     * @return an escaped version of the input string
     */
    public static String escapeNestedValue(String value) {
        return escapeHtmlText(value);
    }

    /**
     * <p>
     * A method to escape HTML text intended for either attribute value content
     * or element character content. It performs more substitutions than are
     * necessary for element content, but the substitutions are nevertheless
     * perfectly valid, specified in the HTML 2.0 spec (and all subsequent HTML
     * specs) and also in XHTML. Users should be aware that although the
     * apostrophe character is a valid attribute value delimiter, it is not
     * escaped by this method. That is because none of the HTML specs define a
     * named entity for this character, and character (numeric) entities are
     * not compatible between HTML and XHTML (the former uses decimal numbers,
     * the latter hexadecimal). Since this method <em>does</em> appropriately
     * escape the double quote character, it is recommended that all attribute
     * values to be escaped by this method be delimited by double quote
     * characters. The specific substitutions performed by this method are:
     * </p>
     * <table>
     * <tr>
     * <th>character</th>
     * <th>substitution</th>
     * </tr>
     * <tr>
     * <td align="center">&lt;</td>
     * <td align="center">&amp;lt;</td>
     * </tr>
     * <tr>
     * <td align="center">&gt;</td>
     * <td align="center">&amp;gt;</td>
     * </tr>
     * <tr>
     * <td align="center">&amp;</td>
     * <td align="center">&amp;amp;</td>
     * </tr>
     * <tr>
     * <td align="center">&quot;</td>
     * <td align="center">&amp;quot;</td>
     * </tr>
     * </table>
     * 
     * @param  text the text to escape
     * 
     * @return a version of {@code text} in which all markup-significant
     *         characters have been replaced by corresponding named entities, or
     *         {@code null} if the supplied text is {@code null}
     */
    private static String escapeHtmlText(String text) {
        if (text == null) {
            return null;
        }
        Matcher matcher = HTML_MARKUP_PATTERN.matcher(text);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            matcher.appendReplacement(result,
                    HTML_ESCAPE_MAP.get(matcher.group()));
        }

        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * A helper method that returns a copy of an {@code Object} as long as that
     * {@code Object} is of a type recognized by this method. This method
     * recognizes: {@code String}, {@code Integer}, {@code Double},
     * {@code Boolean} and {@code String[]}. This method should be updated to
     * support every Object type that may be used as the 'value' of an
     * {@code HtmlControl} or subclass. If source is null, null is returned.
     * 
     * @param  source the {@code Object} to clone
     * 
     * @return a copy of the object such that
     *         {@code returnedvalue.equals(source)} if {@code source} is not
     *         {@code null}, otherwise {@code null}
     *         
     * @throws IllegalArgumentException if {@code source} is not one of the
     *         allowed types (listed above)
     */
    private static Object cloneObject(Object source) {
        if (source == null) {
            return null;
        } else if (source instanceof String) {
            
            // strings are immutable and therefore references may be shared
            return source;
        } else if (source instanceof Integer) {
            
            // Integers are immutable and therefore references may be shared
            return source;
        } else if (source instanceof Double) {
            
            // Doubles are immutable and therefore references may be shared
            return source;
        } else if (source instanceof Boolean) {
            
            // Booleans are immutable and therefore references may be shared
            return source;
        } else if (source instanceof String[]) {
            
            // The array elements are Strings, hence immutable, hence shareable
            return ((String[]) source).clone();
        } else {
            
            // this object type is not supported
            throw new IllegalArgumentException();
        }
    }
}
