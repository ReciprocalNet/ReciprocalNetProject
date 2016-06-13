/*
 * Reciprocal Net project
 * 
 * PaginationLink.java
 * 
 * 02-Nov-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 14-Jun-2006: jobollin reformmatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * <p>
 * A custom tag that can display a page navigation link when used within a
 * {@code PaginationContext}. The body of this tag is included as the body of
 * the HTML &lt;a&gt; tag for the link. If the 'includePageNumbers' property is
 * set to true, then the page number is included in the output right before the
 * body content. Furthermore, the entire output, including the body and the
 * optional number may be suppressed if the page number to which this link would
 * target is an invalid page number.
 * </p><p>
 * The only information that needs to be provided by the JSP author is the
 * 'pageOffset', which indicates the page (relative to the current one) to which
 * the link should direct the browser. Any time the 'pageOffset' resolves to an
 * invalid page (less than 1, or greater than the last page) this control simply
 * outputs nothing, as if the 'visible' property on the superclass were set to
 * {@code false}.
 * </p>
 */
public class PaginationLink extends LinkHtmlElement {

    /**
     * The most immeidate {@code PaginationContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()} and used by
     * {@code onFetchingPhaseBeforeBody()} to get paging information.
     */
    private PaginationContext paginationContext;

    /**
     * An optional property that defaults to "0" and indicates to which page,
     * relative to the current one, this link should target. For example 0,
     * would simply link to this page, 1 would link to the next page and -2
     * would link the page 2 pages before this one.
     */
    private int pageOffset;

    /**
     * An optional property, that when set to true causes the page number
     * referenced in this link to be prepended to the text within the body of
     * this tag.
     */
    private boolean includePageNumber;

    /**
     * An optional property that defaults to "page" and indicates the name of
     * the request parameter that contains the page number. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String pageNumberParamName;

    /**
     * An optional property that defaults to "pageSize" and indicates the name
     * of the request parameter that contains the page size. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String pageSizeParamName;

    /**
     * An internal variable that stores page resolved by adding the 'pageOffset'
     * to the current page supplied by the {@code PaginationContext}. This
     * variable is set by {@code onFetchingPhaseBeforeBody()} for use later.
     */
    private int requestedPage;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.pageOffset = 0;
        this.includePageNumber = false;
        this.pageNumberParamName = "page";
        this.pageSizeParamName = "pageSize";
    }

    /**
     * @param bool a boolean that when true, indicates that the page number
     *        should be written to the output stream before evaluating the body
     *        during the {@code RENDERING_PHASE}
     */
    public void setIncludePageNumber(boolean bool) {
        this.includePageNumber = bool;
    }

    /**
     * @return boolean that when true, indicates that the page number should be
     *         written to the output stream before evaluating the body during
     *         the {@code RENDERING_PHASE}
     */
    public boolean getIncludePageNumber() {
        return this.includePageNumber;
    }

    /**
     * @param offset the number of pages from the current one this link should
     *        should lead. Negative values indicate pages back, positive values
     *        indicate pages forward and zero indicates the current page.
     */
    public void setPageOffset(int offset) {
        this.pageOffset = offset;
    }

    /**
     * @return the number of pages from the current one this link should should
     *         lead. Negative values indicate pages back, positive values
     *         indicate pages forward and zero indicates the current page.
     */
    public int getPageOffset() {
        return this.pageOffset;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        page number
     */
    public void setPageNumberParamName(String name) {
        this.pageNumberParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         page number
     */
    public String getPageNumberParamName() {
        return this.pageNumberParamName;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        page size
     */
    public void setPageSizeParamName(String name) {
        this.pageSizeParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         page size
     */
    public String getPageSizeParamName() {
        return this.pageSizeParamName;
    }

    /**
     * {@inheritDoc}; this version gets the {@code PaginationContext} and sets
     * the 'href' property to equal the current page.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get the PaginationContext
        this.paginationContext = findRealAncestorWithClass(this,
                PaginationContext.class);
        if (this.paginationContext == null) {
            throw new IllegalStateException();
        }

        // set href
        setHref(((HttpServletRequest) this.pageContext.getRequest()
                ).getServletPath());

        return rc;
    }

    /**
     * {@inheritDoc}; this version adds the needed parameters to the underlying
     * {@code LinkHtmlElement}.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        int pageSize = this.paginationContext.getPageSize();
        int pageCount = this.paginationContext.getPageCount();

        this.requestedPage = this.paginationContext.getPageNumber()
                + this.pageOffset;
        if ((requestedPage < 1) || (requestedPage > pageCount)) {
            setVisible(false);
        } else {
            addParameter(this.pageSizeParamName, String.valueOf(pageSize));
            addParameter(this.pageNumberParamName,
                    String.valueOf(requestedPage));
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version writes the page number to the
     * {@code JspWriter} if the 'includePageNumber' property is {@code true}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        if (this.includePageNumber && getVisible()) {
            out.print(String.valueOf(this.requestedPage));
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        PaginationLink dc = (PaginationLink) super.generateCopy(newId, map);

        dc.paginationContext
                = (PaginationContext) map.get(this.paginationContext);

        return dc;
    }
}
