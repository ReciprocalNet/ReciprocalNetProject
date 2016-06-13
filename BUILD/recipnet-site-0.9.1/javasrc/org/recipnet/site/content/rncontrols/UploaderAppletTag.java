/*
 * Reciprocal Net project
 * 
 * UploaderAppletTag.java
 * 
 * 16-Jun-2005: ekoperda wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * A custom tag that renders itself as HTML that will cause web browsers to
 * invoke the {@code Uploader} applet. The tag must be nested within an
 * {@code UploaderPage} tag. The ongoing {@code UploaderOperation} is fetched
 * from the {@code UploaderPage} tag and its id is passed to the applet to
 * provide for continuity in the persisted operation across HTTP round-trips. If
 * no {@code UploaderOperation} is available from the {@code UploaderPage} for
 * some unknown reason, this tag is silent and does not render any HTML.
 */
public class UploaderAppletTag extends HtmlPageElement {

    /**
     * A reference to the nearest {@code UploaderPage} tag that encloses this
     * one. Set during {@code onRegistrationPhaseBeforeBody().}
     */
    private UploaderPage uploaderPage;

    /**
     * The ongoing {@code UploaderOperation} that the uploader applet will be
     * participating in. Set during {@code onFetchingPhaseBeforeBody()}. Is
     * null if there is none.
     */
    private UploaderOperation uploaderOperation;

    /**
     * Required attribute that must be the URL of the .jar archive that contains
     * the uploader applet, relative to the web application context root.
     */
    private String archiveHref;

    /**
     * Required attribute that must be the URL of the support servlet
     * {@code UploaderSupprt}, relative to the web application context root.
     */
    private String supportServletHref;

    /**
     * Optional attribute, defaults to '400'; the string representation of the
     * height of the applet in pixels.
     */
    private String width;

    /**
     * Optional attribute, defaults to '400'; the string representation of the
     * height of the applet in pixels.
     */
    private String height;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.uploaderPage = null;
        this.uploaderOperation = null;
        this.archiveHref = null;
        this.supportServletHref = null;
        this.width = "400";
        this.height = "400";
    }

    /** Simple getter */
    public String getArchiveHref() {
        return this.archiveHref;
    }

    /** Simple setter */
    public void setArchiveHref(String archiveHref) {
        this.archiveHref = archiveHref;
    }

    /** Simple getter */
    public String getSupportServletHref() {
        return this.supportServletHref;
    }

    /** Simple setter */
    public void setSupportServletHref(String supportServletHref) {
        this.supportServletHref = supportServletHref;
    }

    /** Simple getter. */
    public String getHeight() {
        return this.height;
    }

    /** Simple setter. */
    public void setHeight(String height) {
        this.height = height;
    }

    /** Simple getter. */
    public String getWidth() {
        return this.width;
    }

    /** Simple setter. */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * {@inheritDoc}; this version looks up the {@code UploaderPage} in which
     * this tag is nested.
     * 
     * @throws IllegalStateException if this tag is not nested in an
     *         {@code UploaderPage}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.uploaderPage = (UploaderPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version fetches the current
     * {@code UploaderOperation} from the {@code UploaderPage} and delegates
     * back to the superclass.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        this.uploaderOperation = this.uploaderPage.getUploaderOperation();

        return rc;
    }

    /**
     * {@inheritDoc}; this version outputs the appropriate HTML tag describing
     * the applet to use. If there is an error then the HTML is skipped.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws JspException,
            IOException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        if (this.uploaderOperation != null) {
            AppletInfo applet = new AppletInfo();

            applet.setPluginVersion(AppletInfo.PluginVersion.JAVA_1_5_OR_HIGHER);
            applet.setCode("org.recipnet.site.applet.uploader.Uploader.class");
            applet.setArchivePrefix(this.uploaderPage.getContextPath());
            applet.setArchive(this.archiveHref);
            applet.setHeight(this.height);
            applet.setWidth(this.width);
            applet.addParam("operationId",
                    String.valueOf(this.uploaderOperation.getId()));
            applet.addParam("supportServletHref",
                    this.uploaderPage.getContextPath()
                            + this.supportServletHref);
            out.println(applet);
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UploaderAppletTag x = (UploaderAppletTag) super.generateCopy(newId, map);

        x.uploaderPage = (UploaderPage) map.get(this.uploaderPage);

        return x;
    }
}
