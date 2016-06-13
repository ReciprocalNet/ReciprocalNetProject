/*
 * Reciprocal Net project
 * 
 * ListboxOption.java
 * 
 * 15-Jun-2004: cwestnea wrote first draft
 * 31-Jan-2006: jobollin reformatted the source, fixed unnecessary munging of
 *              tag attributes, removed unused imports; moved this tag's main
 *              operations to the FETCHING_PHASE so that it will interact better
 *              with iterator tags; caused this tag to discard the result of its
 *              body evaluation during the rendering phase.
 */

package org.recipnet.common.controls;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * This tag must be nested inside a {@code ListboxHtmlControl} and acts in a
 * simliar manner to the HTML &lt;option&gt; tag. The label may be specified as
 * either an attribute or in the body, and the body evaluation is captured
 * during the {@code FETCHING_PHASE} for the purpose of supplying the label.
 * The body evaluation is also buffered during the {@code RENDERING_PHASE}, in
 * this case simply so that it can be discarded.  Options may be present but
 * disabled, in which case the containing listbox will not accept their values
 * as valid selections during form processing.
 */
public class ListboxOption extends HtmlPageElement {
    /**
     * Optional attribute, defaults to true; if true, then the user may submit
     * this option as their selection. This is a transient property.
     */
    private boolean enabled;

    /**
     * Optional attribute, null by default; the label to display for the option.
     * If it is not specified as an attribute, then it must be specified in the
     * body. This is a transient property.
     */
    private String label;

    /**
     * Optional attribute, null by default; the value of this option.
     * Corresponds to the value attribute on the HTML &lt;option&gt; tag. This
     * is a transient property.
     */
    private String value;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.enabled = true;
        this.label = null;
        this.value = null;
    }

    /** @param enabled is this a valid selection */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** @return is this a valid selection */
    public boolean getEnabled() {
        return this.enabled;
    }

    /** @param value the value of this option */
    public void setValue(String value) {
        this.value = value;
    }

    /** @return the value of this option */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the label of this option
     * 
     * @param label the label of this option as a {@code String}
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /** @return the label of this option */
    public String getLabel() {
        return this.label;
    }

    /**
     * {@inheritDoc}. This version simply causes this tag's body evaluation to
     * be buffered for possible use as an option label / value.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();
        return EVAL_BODY_BUFFERED;
    }

    /**
     * {@inheritDoc}. This version adds an option to the surrounding listbox
     * control corresponding to this tag's attributes and body. The
     * {@code label} property is used for the option label if it is provided;
     * otherwise, the result of the body evaluation is used as the option label.
     * Similarly, the {@code value} property is used as the value if it is
     * provided; otherwise, the same string is used for the value as for the
     * label.
     * 
     * @throws IllegalStateException if it is not inside a listbox control
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        super.onFetchingPhaseAfterBody();

        String labelText = (this.label == null) ? getBodyContent().getString()
                : this.label;
        String valueText = (this.value == null) ? labelText : this.value;
        ListboxHtmlControl parent
                = findRealAncestorWithClass(this, ListboxHtmlControl.class);

        if (parent == null) {
            throw new IllegalStateException();
        }
        parent.addOption(enabled, labelText.trim(), valueText.trim());

        return EVAL_PAGE;
    }

    /**
     * {@inheritDoc}. This version causes its body evaluation to be buffered;
     * later, the result will be discarded.
     * 
     * @see HtmlPageElement#onRenderingPhaseBeforeBody(JspWriter)
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);

        return EVAL_BODY_BUFFERED;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        ListboxOption opt = (ListboxOption) source;

        this.enabled = opt.enabled;
        this.label = opt.label;
        this.value = opt.value;
    }
}
