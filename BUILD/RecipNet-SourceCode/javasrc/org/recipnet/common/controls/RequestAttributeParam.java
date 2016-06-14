/*
 * Reciprocal Net project
 * 
 * RequestAttributeParam.java
 * 
 * 15-Aug-2004: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * A {@code LinkParam} subclass that adds a parameter to the URL in the
 * surrounding {@code LinkHtmlElement} tag with the name indicated by the 'name'
 * property and a value equal to the value of the request parameter named by the
 * 'attributeName' property.
 */
public class RequestAttributeParam extends LinkParam {

    /**
     * A required property indicating the name of an attribute on the
     * {@code ServletRequest} object. (typically attributes are attached before
     * forwarding)
     */
    private String attributeName;

    /**
     * Setter for the 'attributeName' property; also sets the 'value' property.
     */
    public void setAttributeName(String name) {
        this.attributeName = name;
        setValue(String.valueOf(pageContext.getRequest().getAttribute(
                this.attributeName)));
    }

    /** Getter for the 'attributeName' property. */
    public String getAttributeName() {
        return this.attributeName;
    }
}
