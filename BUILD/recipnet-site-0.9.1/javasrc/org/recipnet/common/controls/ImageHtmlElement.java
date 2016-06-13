/*
 * Reciprocal Net project
 * 
 * ImageHtmlElement.java
 * 
 * 20-Oct-2004: midurbin wrote first draft
 * 15-Aug-2005: midurbin maodified the behavior of the 'src' property, added
 *              'srcIsAbsolute' property
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

/**
 * A phase-recognizing custom tag has comparable functionality as the
 * &lt;img&gt; HTML tag. This tag simply outputs HTML to display an image during
 * the {@code RENDERING_PHASE} and does nothing during other phases.
 */
public class ImageHtmlElement extends HtmlPageElement {

    /**
     * A possible value for the 'border', 'height' and 'width' attribute that
     * indicates that they were not set by the JSP author.
     */
    private static int UNDEFINED = -1;

    /**
     * This required property is used for the 'src' attribute for the HTML
     * &lt;img&gt; tag and is the URL (relative to the servlet context root)
     * where the image is located. This value is not URL-encoded before used as
     * the 'src' attribute, if any encoding is needed, the jsp author is
     * responsible for doing so.
     */
    private String src;

    /**
     * An optional property that maps to the 'alt' attribute in the HTML
     * &lt;img&gt; tag that will be rendered by this tag.
     */
    private String alt;

    /**
     * An optional property that maps to the 'border' attribute in the HTML
     * &lt;img&gt; tag that will be rendered by this tag. This may be set to
     * {@code UNDEFINED}, which indicates that no 'border' attribute will be
     * included in the HTML.
     */
    private int border;

    /**
     * An optional property that maps to the 'height' attribute in the HTML
     * &lt;img&gt; tag that will be rendered by this tag. This may be set to
     * {@code UNDEFINED}, which indicates that no 'height' attribute will be
     * included in the HTML.
     */
    private int height;

    /**
     * An optional property that maps to the 'width' attribute in the HTML
     * &lt;img&gt; tag that will be rendered by this tag. This may be set to
     * {@code UNDEFINED}, which indicates that no 'width' attribute will be
     * included in the HTML.
     */
    private int width;

    /**
     * An optional property that when set to true indicates that the 'src'
     * property is an absolute URL and does not need to have the servlet context
     * path prepended to it before output. This property defaults to false.
     */
    private boolean srcIsAbsolute;

    /**
     * An optional property that maps to the 'class' attribute in the HTML
     * &lt;img&gt; tag that will be rendered by this tag.
     */
    private String styleClass;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.src = null;
        this.alt = null;
        this.border = UNDEFINED;
        this.height = UNDEFINED;
        this.width = UNDEFINED;
        this.srcIsAbsolute = false;
        this.styleClass = null;
    }

    /** @param src the location of the image */
    public void setSrc(String src) {
        this.src = src;
    }

    /** @return the location of the image */
    public String getSrc() {
        return this.src;
    }

    /** @param alt alternative text for non-graphical browsers */
    public void setAlt(String alt) {
        this.alt = alt;
    }

    /** @return the alternative text for non-graphical browsers */
    public String getAlt() {
        return this.alt;
    }

    /** @param border the number of pixels thick the border should be */
    public void setBorder(int border) {
        this.border = border;
    }

    /** @return the number of pixels thick the border should be */
    public int getBorder() {
        return this.border;
    }

    /**
     * @param height the height, in pixels, to which the image will be scaled
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /** @return the height, in pixels, to which the image will be scaled */
    public int getHeight() {
        return this.height;
    }

    /**
     * @param width the width, in pixels, to which the image will be scaled
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /** @return the width, in pixels, to which the image will be scaled */
    public int getWidth() {
        return this.width;
    }

    /** @param isAbsolute indicates whether the 'src' is the full URL */
    public void setSrcIsAbsolute(boolean isAbsolute) {
        this.srcIsAbsolute = isAbsolute;
    }

    /** @return a boolean indicating whether the 'src' is the full URL */
    public boolean getSrcIsAbsolute() {
        return this.srcIsAbsolute;
    }

    /** @param styleClass indicates the 'class' attribute for the img tag */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /** @return the 'class' attribute for the img tag */
    public String getStyleClass() {
        return this.styleClass;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation outputs an
     * HTML &lt;img&gt; tag with the characteristics described by this tag's
     * properties.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.print("<img src=\""
                + (srcIsAbsolute ? "" : HtmlControl.escapeAttributeValue(
                        getPage().getContextPath()))
                + HtmlControl.escapeAttributeValue(src) + "\" "
                + (alt != null ? "alt=\""
                        + HtmlControl.escapeAttributeValue(alt) + "\" " : "")
                + (border != UNDEFINED ? "border=\"" + border + "\" " : "")
                + (height != UNDEFINED ? "height=\"" + height + "\" " : "")
                + (width != UNDEFINED ? "width=\"" + width + "\" " : "")
                + (styleClass != null ? "class=\"" + styleClass + "\" " : "")
                + "/>");
        return super.onRenderingPhaseAfterBody(out);
    }

}
