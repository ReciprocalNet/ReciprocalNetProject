/*
 * Reciprocal Net project
 * 
 * ExtraHtmlAttribute.java
 * 
 * 19-Feb-2004: midurbin wrote first draft
 * 16-Nov-2004: midurbin renamed this class from HtmlControlExtraAttribute to
 *              ExtraHtmlAttribute and made it more general to recognize
 *              ExtraHtmlAttributeAccepter
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 10-Nov-2005: midurbin fixed bug #1692 by removing generateCopy()
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * During the {@code REGISTRATION_PHASE}, this tag passes an attribute
 * name/value pair to the supplied {@code ExtraHtmlAttributeAccepter}. If no
 * {@code ExtraHtmlAttributeAccepter} was supplied as an attribute to this tag,
 * it seeks the first implementing tag from its ancestors. Therefore this tag
 * may be either nested within or located after the tag with which it interacts.
 * In the case that it is not nested, the 'attributeAccepter' property must be
 * set to reference the {@code ExtraHtmlAttributeAccepter} implementation.
 */
public class ExtraHtmlAttribute extends HtmlPageElement {

    /**
     * A required attribute that indicates the {@code String} name of this
     * {@code ExtraHtmlAttribute}.
     */
    private String name;

    /**
     * A required attribute that indicates the {@code String} value of this
     * {@code ExtraHtmlAttribute}.
     */
    private String value;

    /**
     * An optional property indicating the {@code ExtraHtmlAttributeAccepter} to
     * which this class provides an extra attribute. If set, during the
     * {@code REGISTRATION_PHASE} the name/value pair will be supplied to the
     * indicated tag. If {@code attributeAccepter} is null, this tag must be
     * nested within an {@code HtmlAttributeAccepter} implementing tag.
     */
    private ExtraHtmlAttributeAccepter attributeAccepter;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.name = null;
        this.value = null;
        this.attributeAccepter = null;
    }

    /** @param name the name of the attribute to be added */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the name of the attribute to be added */
    public String getName() {
        return this.name;
    }

    /** @param value the value of the attribute to be added */
    public void setValue(String value) {
        this.value = value;
    }

    /** @return the value of the attribute to be added */
    public String getValue() {
        return this.value;
    }

    /**
     * @param accepter The {@code ExtraHtmlAttributeAccepter} to which this
     *        attribute will be added, or null to indicate that this attribute
     *        name/value pair should be added to an
     *        {@code ExtraHtmlAttributeAccepter} tag that surrounds this tag.
     */
    public void setAttributeAccepter(ExtraHtmlAttributeAccepter accepter) {
        this.attributeAccepter = accepter;
    }

    /**
     * @return The {@code ExtraHtmlAttributeAccepter} to which this attribute
     *         will be added, or null to indicate that this attribute name/value
     *         pair should be added to an {@code ExtraHtmlAttributeAccepter} tag
     *         that surrounds this tag.
     */
    public ExtraHtmlAttributeAccepter getAttributeAccepter() {
        return this.attributeAccepter;
    }

    /**
     * {@inheritDoc}. This version passes the attribute name/value pair to the
     * tag indicated by the 'AttributeAccepter' property or the most immediately
     * enclosing {@code ExtraHtmlAttributeAccepter} tag if the property is
     * unset.
     * 
     * @throws IllegalStateException if no {@code AttributeAccepter} was
     *         provided and none can be found in this Tag's ancestry.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        if (this.attributeAccepter == null) {
            ExtraHtmlAttributeAccepter surroundingAccepter
                    = findRealAncestorWithClass(
                            this, ExtraHtmlAttributeAccepter.class);

            if (surroundingAccepter == null) {
                throw new IllegalStateException();
            }
            surroundingAccepter.addExtraHtmlAttribute(this.name, this.value);
        } else {
            attributeAccepter.addExtraHtmlAttribute(this.name, this.value);
        }

        return super.onRegistrationPhaseBeforeBody(pageContext);
    }
}
