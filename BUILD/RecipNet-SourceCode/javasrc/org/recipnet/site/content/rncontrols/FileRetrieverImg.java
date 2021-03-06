/*
 * Reciprocal Net project
 * 
 * FileRetrieverImg.java
 * 
 * 15-Aug-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.recipnet.common.controls.ImageHtmlElement;

/**
 * A custom tag that displays an image retrieved from the 'fileretriever'
 * servlet. The full URL is generated by appending a key, to the 'servletUrl'
 * property's value. The key is gotten from a request attribute whose name is
 * supplied as the 'keyRequestAttributeName' property.
 */
public class FileRetrieverImg extends ImageHtmlElement {

    /**
     * A required property that indicates the URL relative to the servlet
     * context root of the 'fileretrieve' servlet. This URL should not contain
     * the query part. This value, with a query containing the 'key' parameter
     * will be used to generate the 'src' property for the underlying
     * {@code ImageHtmlElement} tag.
     */
    private String servletUrl;

    /**
     * A required property indicating the name of the request attribute that
     * contains the (String) 'key' value that must be passed to the
     * 'fileretrieve' servlet.
     */
    private String keyRequestAttributeName;

    /** Setter for the 'servletUrl' property. */
    public void setServletUrl(String url) {
        this.servletUrl = url;
    }

    /** Getter for the 'servletUrl' property. */
    public String getServletUrl() {
        return this.servletUrl;
    }

    /** Setter for the 'keyRequestAttributeName' property. */
    public void setKeyRequestAttributeName(String name) {
        this.keyRequestAttributeName = name;
    }

    /** Getter for the 'keyRequestAttributeName' property. */
    public String getKeyRequestAttributeName() {
        return this.keyRequestAttributeName;
    }

    /**
     * {@inheritDoc}; this version sets the 'src' property for this tag based
     * on the 'servletUrl' and 'keyRequestAttributeName' properties.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        try {
            setSrc(this.servletUrl
                    + "?key="
                    + URLEncoder.encode(
                            String.valueOf(pageContext.getRequest().getAttribute(
                                    this.keyRequestAttributeName)), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            // can't happen because UTF-8 is always supported
        }

        return super.onRenderingPhaseAfterBody(out);
    }
}
