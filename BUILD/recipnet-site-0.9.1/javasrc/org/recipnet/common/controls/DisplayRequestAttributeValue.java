/*
 * Reciprocal Net project
 * 
 * DisplayRequestAttributeValue.java
 *
 * 15-Aug-2005: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * A custom tag that displays the {@code String} representation of the value of
 * the request attribute with the name indicated by the 'attributeName'
 * property. The {@code String} representation of the passed object is escaped
 * so that no HTML code will be interpreted.
 */
public class DisplayRequestAttributeValue extends HtmlPageElement {

    /**
     * A required property indicating the name of an attribute assigned to the
     * {@code ServletRequest} representing the request being serviced by this
     * tag.
     */
    private String attributeName;

    /** Setter for the 'attributeName' property. */
    public void setAttributeName(String name) {
        this.attributeName = name;
    }

    /** Getter for the 'attributeName' property. */
    public String getAttributeName() {
        return this.attributeName;
    }

    /**
     * {@inheritDoc}; this version writes the value of the request attribute
     * named by the 'attributeName' property to the {@code JspWriter}.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws JspException,
            IOException {
        out.print(HtmlControl.escapeNestedValue(String.valueOf(
                pageContext.getRequest().getAttribute(this.attributeName))));
        return super.onRenderingPhaseAfterBody(out);
    }
}
