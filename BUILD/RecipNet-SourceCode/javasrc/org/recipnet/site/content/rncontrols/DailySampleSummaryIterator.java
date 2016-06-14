/*
 * Reciprocal Net project
 * 
 * DailySampleSummaryIterator.java
 * 
 * 14-Jun-2005: midurbin wrote the first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;

/**
 * A custom tag that iterates through the action codes (representing actions
 * performed on a sample for one day) provided by a
 * {@code SampleDailyVersionIterator} in which this tag must be nested. The
 * information is then made avilable to nested tags through the methods:
 * {@link DailySampleSummaryIterator#getActionCode() getActionCode()} and
 * {@link DailySampleSummaryIterator#getActionCount() getActionCount()}.
 */
public class DailySampleSummaryIterator extends HtmlPageIterator {

    /**
     * A reference to the {@code SampleDailyVersionIterator} tag; determined
     * during the {@code REGISTRATION_PHASE}.
     */
    private SampleDailyVersionIterator dayIt;

    /**
     * A map of action codes to the number of times they were performed. This
     * variable is set during the {@code FETCHING_PHASE} to a value from the
     * {@code SampleDailyVersionIterator}.
     */
    private Map<Integer, Integer> actionToCountMap;

    /** An iterator over the keys of the 'actionToCountMap'. */
    private Iterator<Integer> actionIterator;

    /**
     * The action type represented by the current evaluation of this tag's body.
     */
    private Integer currentActionCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.dayIt = null;
        this.actionToCountMap = null;
    }

    /**
     * {@inheritDoc}; this version looks up a refererence to the surrounding
     * {@code SampleDailyVersionIterator}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleDailyVersionIterator}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        this.dayIt = findRealAncestorWithClass(this,
                SampleDailyVersionIterator.class);
        if (this.dayIt == null) {
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version looks up the 'actionToCountMap'.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        this.actionToCountMap = this.dayIt.getActionMap();
        
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() throws JspException {
        if (this.actionToCountMap != null) {
            this.actionIterator = this.actionToCountMap.keySet().iterator();
        }
    }

    /**
     * {@inheritDoc}; this version ensures that the body is evaluated once
     * for each action.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.actionToCountMap == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                    && (getPostedIterationCount()
                            > getIterationCountSinceThisPhaseBegan())) {
                /*
                 * evaluate the body for the benefit of nested elements, even
                 * though those nested elements may not yet get the
                 * SampleHistoryContext. This ensures that the number of
                 * iterations never decreases before the fetching phase
                 */
                return true;
            }
            return false;
        } else if (this.actionIterator.hasNext()) {
            this.currentActionCode = this.actionIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the action code for the action represented by the current iteration.
     */
    public int getActionCode() {
        return this.currentActionCode;
    }

    /**
     * Gets the number of times the current action was performed for the day
     * represented by the surrounding {@code SampleDailyVersionIterator}.
     */
    public int getActionCount() {
        return this.actionToCountMap.get(this.currentActionCode);
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        DailySampleSummaryIterator dc
                = (DailySampleSummaryIterator) super.generateCopy(newId, map);
        
        dc.dayIt = (SampleDailyVersionIterator) map.get(this.dayIt);
        
        return dc;
    }
}
