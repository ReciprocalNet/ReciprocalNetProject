/*
 * Reciprocal Net project
 * 
 * PaginationChecker.java
 *
 * 11-Apr-2004: midurbin wrote first draft
 * 12-Jun-2006: jobollin made this class extend AbstractChecker and reformatted
 *              the source
 */
package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * A custom tag whose body is evaluated only when the indicated conditions are
 * true of the {@code PaginationContext} in which this tag is nested.  The
 * JSP author may specify multiple conditions and the body is evaluated only if
 * all of the specified conditions are met.
 */
public class PaginationChecker extends AbstractChecker
        implements SuppressionContext {

    /**
     * A possible value for the 'requirement' properties to indicate that no
     * value has been specified.
     */
    public static final int UNSPECIFIED = -1;

    /**
     * The {@code PaginationContext} that surrounds this tag.  This
     * reference is determined by {@code onRegistrationPhaseBeforeBody()}.
     */
    private PaginationContext paginationContext;

    /**
     * An optional 'requirement' property that when set to a value other than
     * {@code UNSPECIFIED} indicates an inclusive lowerbound of the amount
     * of elements that must be contained in the surrounding
     * {@code PaginationContext} in order for this tags body to be
     * included rather than suppressed.  The number of elements is determined
     * by a call to {@link
     * org.recipnet.common.controls.PaginationContext#getElementCount
     * getElementCount()}.
     */
    private int requireElementCountNoMoreThan;

    /**
     * An optional 'requirement' property that when set to a value other than
     * {@code UNSPECIFIED} indicates an inclusive upperbound of the amount
     * of elements that must be contained in the surrounding
     * {@code PaginationContext} in order for this tags body to be
     * included rather than suppressed.  The number of elements is determined
     * by a call to {@link
     * org.recipnet.common.controls.PaginationContext#getElementCount
     * getElementCount()}.
     */
    private int requireElementCountNoLessThan;

    /**
     * An optional 'requirement' property that when set to a value other than
     * {@code UNSPECIFIED} indicates an inclusive lowerbound of the amount
     * of pages that must be contained in the surrounding
     * {@code PaginationContext} in order for this tags body to be
     * included rather than suppressed.  The number of elements is determined
     * by a call to {@link
     * org.recipnet.common.controls.PaginationContext#getPageCount
     * getPageCount()}.
     */
    private int requirePageCountNoMoreThan;

    /**
     * An optional 'requirement' property that when set to a value other than
     * {@code UNSPECIFIED} indicates an inclusive upperbound of the amount
     * of pages that must be contained in the surrounding
     * {@code PaginationContext} in order for this tags body to be
     * included rather than suppressed.  The number of elements is determined
     * by a call to {@link
     * org.recipnet.common.controls.PaginationContext#getPageCount
     * getPageCount()}.
     */
    private int requirePageCountNoLessThan;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.paginationContext = null;
        this.requireElementCountNoMoreThan = UNSPECIFIED;
        this.requireElementCountNoLessThan = UNSPECIFIED;
        this.requirePageCountNoMoreThan = UNSPECIFIED;
        this.requirePageCountNoLessThan = UNSPECIFIED;
    }

    /**
     * @param count the upperbound of element counts needed to trigger body
     *     evaluation
     * @throws IllegalArgumentException if count is negative
     */
    public void setRequireElementCountNoMoreThan(int count) {
        if ((count != UNSPECIFIED) && (count < 0)) {
            throw new IllegalArgumentException();
        }
        this.requireElementCountNoMoreThan = count;
    }

    /**
     * @return the upperbound of element counts needed to trigger body
     *     evaluation
     */
    public int getRequireElementCountNoMoreThan() {
        return this.requireElementCountNoMoreThan;
    }

    /**
     * @param count the lowerbound of element counts needed to trigger body
     *     evaluation
     * @throws IllegalArgumentException if count is negative
     */
    public void setRequireElementCountNoLessThan(int count) {
        if ((count != UNSPECIFIED) && (count < 0)) {
            throw new IllegalArgumentException();
        }
        this.requireElementCountNoLessThan = count;
    }

    /**
     * @return the lowerbound of element counts needed to trigger body
     *     evaluation
     */
    public int getRequireElementCountNoLessThan() {
        return this.requireElementCountNoLessThan;
    }

    /**
     * @param count the upperbound of page counts needed to trigger body
     *     evaluation
     * @throws IllegalArgumentException if count is negative
     */
    public void setRequirePageCountNoMoreThan(int count) {
        if ((count != UNSPECIFIED) && (count < 0)) {
            throw new IllegalArgumentException();
        }
        this.requirePageCountNoMoreThan = count;
    }

    /**
     * @return the upperbound of page counts needed to trigger body
     *     evaluation
     */
    public int getRequirePageCountNoMoreThan() {
        return this.requirePageCountNoMoreThan;
    }

    /**
     * @param count the lowerbound of page counts needed to trigger body
     *     evaluation
     * @throws IllegalArgumentException if count is negative
     */
    public void setRequirePageCountNoLessThan(int count) {
        if ((count != UNSPECIFIED) && (count < 0)) {
            throw new IllegalArgumentException();
        }
        this.requirePageCountNoLessThan = count;
    }

    /**
     * @return the lowerbound of page counts needed to trigger body
     *     evaluation
     */
    public int getRequirePageCountNoLessThan() {
        return this.requirePageCountNoLessThan;
    }

    /**
     * {@inheritDoc}; this version
     * finds the innermost containing {@code PaginationContext}
     * @throws IllegalStateException if this tag is not nested within a
     *     {@code PaginationContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get the PaginationContext
        this.paginationContext
                = findRealAncestorWithClass(this, PaginationContext.class);
        if (this.paginationContext == null) {
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version
     * determines whether this tag should include its body evaluation or
     * whether it should suppress it based on whether characteristics of the
     * {@code PaginationContext} fall into the range specified by the
     * 'requirement' properties.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException { 
        int rc = super.onFetchingPhaseBeforeBody();
        int elementCount = this.paginationContext.getElementCount();
        int pageCount = this.paginationContext.getPageCount();
        
        inclusionConditionMet
                = (((this.requireElementCountNoMoreThan == UNSPECIFIED)
                        || (elementCount <= this.requireElementCountNoMoreThan))
                    && ((this.requireElementCountNoLessThan == UNSPECIFIED)
                        || (elementCount >= this.requireElementCountNoLessThan))
                    && ((this.requirePageCountNoMoreThan == UNSPECIFIED)
                        || (pageCount <= this.requirePageCountNoMoreThan))
                    && ((this.requirePageCountNoLessThan == UNSPECIFIED)
                        || (pageCount >= this.requirePageCountNoLessThan))); 
        
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        PaginationChecker dc
                = (PaginationChecker) super.generateCopy(newId, map);
        
        dc.paginationContext
                = (PaginationContext) map.get(this.paginationContext);
        
        return dc;
    }
}
