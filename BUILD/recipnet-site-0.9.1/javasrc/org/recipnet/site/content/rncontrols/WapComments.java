/*
 * Reciprocal Net project
 * 
 * WapComments.java
 *
 * 02-Jul-2004: midurbin wrote first draft
 * 27-Jan-2005: midurbin added fetching phase support
 * 10-Mar-2006: jobollin reduced the textarea size to 2 rows and reforamatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.TextareaHtmlControl;

/**
 * This special purpose text area is used to accept comments that are
 * communicated to the enclosing {@code WapPage} tag for inclusion when a
 * workflow action is performed. One of these should be present within every
 * {@code WapPage} tag. The default size of this TextAreaHtmlControl is 36
 * columns by 4 rows.
 */
public class WapComments extends TextareaHtmlControl {
    /**
     * A reference to the enclosing {@code WapPage} tag, set during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private WapPage wapPage;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        setColumns(36);
        setRows(2);
        this.wapPage = null;
    }

    /**
     * {@inheritDoc}; this version looks up and records the surrounding WapPage
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code WapPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the WapPage tag
        this.wapPage = findRealAncestorWithClass(this, WapPage.class);
        if (this.wapPage == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets this control's value to the String
     * obtained by invoking the {@link WapPage#getComments() getComments()}
     * method of the surrounding {@code WapPage}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        setValue(this.wapPage.getComments(),
                HtmlControl.EXISTING_VALUE_PRIORITY,
                HtmlControl.LOWEST_PRIORITY);

        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the comments on the surrounding
     * {@code WapPage} via its {@link WapPage#setComments(String)
     * setComments(String)} method
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        this.wapPage.setComments((String) getValue());

        return rc;
    }
}
