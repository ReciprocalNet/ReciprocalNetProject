/*
 * Reciprocal Net project
 * 
 * JammAppletPreservingLink.java
 * 
 * 18-Aug-2004: cwestnea wrote first draft
 * 12-Apr-2005: midurbin fixed bugs #1442 in onFetchingPhaseBeforeBody()
 * 02-May-2005: ekoperda modified onFetchingPhaseBeforeBody() to accommodate
 *              spec changes in FileContext
 * 13-Mar-2006: jobollin fixed bug #1763 in onFetchingPhaseBeforeBody(); removed
 *              unused imports; reformatted the source
 * 23-Jun-2006: jobollin escaped output text, updated some docs
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.recipnet.common.controls.HtmlControl;

/**
 * This tag represents a {@code JammAppletTag} modifying link. It automatically
 * gets the {@code applet} and {@code crtFile} values it needs to preserve from
 * {@code JammAppletTag} and if {@code disableWhenLinkMatchesAppletTag} is set
 * to true, it automatically disables the link if the given parameters match the
 * current parameters of the {@code JammAppletTag}. The link created preserves
 * the sample context and the given parameters. By default, the current URL is
 * used as the href, but this may be overridden.
 */
public class JammAppletPreservingLink extends ContextPreservingLink {
    /**
     * Required attribute; the object representing the JaMM applet whose
     * parameters are to be modified.
     */
    private JammAppletTag jammAppletTag;

    /**
     * Required attribute; specifies whether or not to disable this link when
     * the {@code switchCrtFile} and switchApplet} specified are the same as the
     * current parameters of the {@code JammAppletTag}.
     */
    private boolean disableWhenLinkMatchesAppletTag;

    /**
     * Optional attribute, defaults to the value stored in the
     * {@code JammAppletTag} provided; the filename of the CRT to display, for
     * example 'samplefile.crt'.
     */
    private String switchCrtFile;

    /**
     * Optional attribute, defaults to the value stored in the
     * {@code JammAppletTag} provided; the version of JaMM to use, for example
     * 'JaMM1', 'JaMM2', or 'miniJaMM'.
     */
    private String switchApplet;

    /**
     * Optional attribute, defaults to null; if this is set to a valid
     * {@code FileContext}, the {@code switchCrtFile} will be gotten from the
     * {@code FileContext} given.
     */
    private FileContext switchCrtFileUsingFileContext;

    /**
     * Optional attribute, defaults to false; specfiies whether or not the
     * filename should be displayed as the body of the link.
     */
    private boolean useCrtFileAsLinkText;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.jammAppletTag = null;
        this.disableWhenLinkMatchesAppletTag = false;
        this.switchCrtFile = null;
        this.switchApplet = null;
        this.switchCrtFileUsingFileContext = null;
        this.useCrtFileAsLinkText = false;
    }

    /**
     * @param jammAppletTag the JammAppletTag to use when contructing the link
     */
    public void setJammAppletTag(JammAppletTag jammAppletTag) {
        this.jammAppletTag = jammAppletTag;
    }

    /**
     * @param disableWhenLinkMatchesAppletTag whether or not the link should be
     *        disabled if it matches the current parameters of the
     *        {@code JammAppletTag}.
     */
    public void setDisableWhenLinkMatchesAppletTag(
            boolean disableWhenLinkMatchesAppletTag) {
        this.disableWhenLinkMatchesAppletTag = disableWhenLinkMatchesAppletTag;
    }

    /** @param switchApplet the version of JaMM used */
    public void setSwitchApplet(String switchApplet) {
        this.switchApplet = switchApplet;
    }

    /** @param switchCrtFile the CRT file displayed */
    public void setSwitchCrtFile(String switchCrtFile) {
        this.switchCrtFile = switchCrtFile;
    }

    /**
     * @param switchCrtFileUsingFileContext the file context that will provide
     *        the file to edit.
     */
    public void setSwitchCrtFileUsingFileContext(
            FileContext switchCrtFileUsingFileContext) {
        this.switchCrtFileUsingFileContext = switchCrtFileUsingFileContext;
    }

    /**
     * @param useCrtFileAsLinkText if set to true the crt file will be output as
     *        the body of this tag.
     */
    public void setUseCrtFileAsLinkText(boolean useCrtFileAsLinkText) {
        this.useCrtFileAsLinkText = useCrtFileAsLinkText;
    }

    /**
     * {@inheritDoc}; this version retrieves the file information
     * from the file context if the file context is not null.  It adds the
     * correct parameter to the link, or disables the link as necessary.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        // Make a best effort to ensure that a non-null switchCrtFile is set
        if (this.switchCrtFile == null) {
            if (this.switchCrtFileUsingFileContext != null) {
                FileContext fc = this.switchCrtFileUsingFileContext;

                this.switchCrtFile = fc.getSampleDataFile().getName();
            } else {
                this.switchCrtFile = this.jammAppletTag.getCrtFileDisplayed();
            }
        }

        // Ensure that a switchApplet is set
        if (this.switchApplet == null) {
            this.switchApplet = this.jammAppletTag.getApplet();
        }

        if (this.disableWhenLinkMatchesAppletTag
                && ((this.switchCrtFile != null)
                        && this.switchCrtFile.equals(
                                this.jammAppletTag.getCrtFileDisplayed())
                        && (this.switchApplet != null)
                        && this.switchApplet.equals(
                                jammAppletTag.getApplet()))) {
            setDisabled(true);
        } else {
            if (getHref() == null) {
                // No page is set; use the current page
                HttpServletRequest req = (HttpServletRequest)
                        this.pageContext.getRequest();

                setHref(req.getServletPath().toString());
            }
            if (this.switchCrtFile != null) {
                addParameter("crtFile", this.switchCrtFile);
            }
            addParameter("jamm", this.switchApplet);
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version outputs the crt file as the link text
     * if {@code useCrtFileAsLinkText} is set to true.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws JspException,
            IOException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        if (this.useCrtFileAsLinkText) {
            /*
             * the first part of the link is output by the parent; this is the
             * part between the open and close <a> tags:
             */
            out.print(HtmlControl.escapeNestedValue(this.switchCrtFile));
        }

        return rc;
    }
}
