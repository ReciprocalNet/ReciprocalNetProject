/*
 * Reciprocal Net project
 * 
 * RadioButtonHtmlControl.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 17-May-2004: midurbin added generateHtmlToDisplayForLabel() to add support
 *              for HtmlControl's 'displayAsLabel' attribute
 * 30-Aug-2004: midurbin fixed bug #1288 in onParsingPhaseBeforeBody()
 * 15-Nov-2004: midurbin fixed spec of onParsingPhaseBeforeBody() to throw
 *              JspException
 * 08-Apr-2005: midurbin added generateHtmlToInvisiblyPersistValue()
 * 26-Jul-2005: midurbin replaced onParsingPhaseBeforeBody() with getRawValue()
 * 25-Jan-2006: jobollin updated docs, removed unused imports, and formatted
 *              the source; updated generateHtmlToDisplayAndPersistValue() and
 *              generateHtmlToInvisiblyPersistValue to assign the control's
 *              ID to the generated radio button instead of using the group name
 *              for both name and ID
 * 01-Feb-2006: jobollin modified this tag to obtain its value by reference to
 *              a surrounding RadioButtonGroup
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * This class is a tag handler for a phase-recognizing tag representing a
 * radio button.  It maintains a boolean value indicating its selection state,
 * indirectly through a button group to which it is assigned.  Only one radio
 * button per group may be selected, and that selected control will maintain the
 * {@code Boolean} value TRUE.  Every radio button must belong to a button
 * group, either explicitly assigned via its 'group' attribute or implicitly
 * assigned via containment (the group tag containing this tag).
 */
public class RadioButtonHtmlControl extends HtmlControl {

    /**
     * This is a required attribute that is meant to differentiate this button
     * from others in its {@code group}. It is set by {@link #reset()} and may
     * be altered by its 'setter' method {@code setOption()}. This is a
     * 'transient' property in that it may change from phase to phase and may be
     * copied by calls to {@link #copyTransientPropertiesFrom(HtmlPageElement)}.
     */
    private String option;

    /**
     * An optional attribute that specifies the radio button group to which this
     * radio button belongs. It is set by {@link #reset()} and may be altered by
     * {@link #getGroup() its 'setter' method}.  This attribute is not
     * transient.
     */
    private RadioButtonGroupHtmlControl group;

    /**
     * The radio button group with which this radio button interacts to
     * determine its value.  Will be the same as {@link #group} if that is
     * specified, otherwise will be the innermost button group containing this
     * radio button.
     */
    private RadioButtonGroupHtmlControl effectiveGroup;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        setFailedValidationHtml("<font color=\"red\">*</font>");
        this.option = null;
        this.group = null;
        this.effectiveGroup = null;
    }

    /**
     * @return a {@code String} representing the option defined by this radio
     *         button.
     */
    public String getOption() {
        return this.option;
    }

    /**
     * @param option a {@code String} representing the option defined by this
     *        radio button.
     */
    public void setOption(String option) {
        this.option = option;
    }

    /**
     * @return the group to which this radio button belongs. Only one option
     *         from a particular group may be selected.
     */
    public RadioButtonGroupHtmlControl getGroup() {
        return this.group;
    }

    /**
     * @param group the group to which this radio button belongs. Only one
     *        option from a particular group may be selected.
     */
    public void setGroup(RadioButtonGroupHtmlControl group) {
        this.group = group;
    }

    /**
     * {@inheritDoc}.  This version returns an initial value based on the
     * surrounding radio button group's current value.
     * 
     * @see HtmlControl#getInitialValue()
     */
    @Override
    public Object getInitialValue() {
        return parseValue(null);
    }

    /**
     * {@inheritDoc}.  This version always returns {@code null}, because this
     * button's group owns the raw value, not the individual buttons
     */
    @Override
    protected String getRawValue(
            @SuppressWarnings("unused") ServletRequest request) {
        return null;
    }

    /**
     * Overrides {@code HtmlControl}, the current implementation ignores the
     * specified raw value, and instead asks the configured button group whether
     * or not this button is the selected one
     * 
     * @param  rawValue nominally, a {@code String} retrieved from the POST
     *         parameters that corresponds to this control; ignored by this
     *         implementation
     * @return the parsed value, a {@code Boolean} corresponding to whether or
     *         not this radio button is the selected one
     */
    @Override
    protected Object parseValue(@SuppressWarnings("unused") String rawValue) {
        return effectiveGroup.isSelectedOption(this);
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlControl#generateCopy(String, Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        RadioButtonHtmlControl copy
                = (RadioButtonHtmlControl) super.generateCopy(newId, map);
        
        copy.group = (RadioButtonGroupHtmlControl) map.get(this.group);
        copy.effectiveGroup
                = (RadioButtonGroupHtmlControl) map.get(this.effectiveGroup);
        
        return copy;
    }

    /**
     * {@inheritDoc}.  This version determines the button group for this button
     * 
     * @see HtmlControl#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        effectiveGroup = ((group != null) ? group
                : findRealAncestorWithClass(
                        this, RadioButtonGroupHtmlControl.class));

        if (effectiveGroup == null) {
            throw new IllegalStateException("No button group");
        }
        
        /*
         * The body evaluation must NOT be buffered in this phase because it
         * interferes with the atypical way in which this tag determines its
         * initial value
         */
        
        return ((rc == EVAL_BODY_BUFFERED) ? EVAL_BODY_INCLUDE : rc);
    }

    /**
     * Overrides {@code HtmlControl} to generate HTML for this control.
     * 
     * @param editable indicates whether the field should be editable. If the
     *        field is not editable the value is displayed and a hidden field is
     *        used.
     * @param failedValidation indicates that the field has an invalid value.
     * @param value the value of the control.
     * @param rawValue the value from the POST parameters, this will differ from
     *        the value in cases of validation failures to preserve the validity
     *        of the {@code value}.
     * @return a {@code String} of HTML code for a textbox or plain text,
     *         depending on the editability of the field.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            @SuppressWarnings("unused") boolean failedValidation, Object value,
            @SuppressWarnings("unused") String rawValue) {
        if (editable) {
            return "<input type=\"radio\" name=\""
                    + effectiveGroup.getId()
                    + "\" id=\""
                    + getId()
                    + "\" value=\""
                    + getOption()
                    + "\""
                    + (Boolean.TRUE.equals(value) ? " checked=\"true\"" : "")
                    + getExtraHtmlAttributesAsString()
                    + " />";
        } else {
            return generateHtmlToDisplayForLabel(value)
                    + generateHtmlToInvisiblyPersistValue(value);
        }
    }

    /**
     * {@inheritDoc}.  This version returns an '*' if the value is {@code true},
     * indicating that this radio button is selected, or an empty string if the
     * value is {@code false}.
     * 
     * @param value the current value of this radio button
     */
    @Override
    protected String generateHtmlToDisplayForLabel(Object value) {
        return (Boolean.TRUE.equals(value) ? "*" : "");
    }

    /**
     * {@inheritDoc}. This version returns HTML containing a hidden form field
     * with an id/name equal to this control's 'group' property and a value of
     * if the 'option' property if 'value' is {@code TRUE}; otherwise it
     * returns an empty {@code String}.
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        if (Boolean.TRUE.equals(value)) {
            return "<input type=\"hidden\" name=\"" + effectiveGroup.getId()
                    + "\" id=\"" + getId() + "\" value=\"" + getOption()
                    + "\" />";
        } else {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);

        RadioButtonHtmlControl src = (RadioButtonHtmlControl) source;

        this.option = src.option;
    }
}
