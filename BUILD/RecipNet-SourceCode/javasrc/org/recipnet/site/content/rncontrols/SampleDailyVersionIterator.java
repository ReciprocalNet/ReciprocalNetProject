/*
 * Reciprocal Net project
 * 
 * SampleDailyVersionIterator.java
 * 
 * 14-Jun-2005: midurbin wrote the first draft
 * 16-Jun-2005: midurbin removed ErrorSupplier implementation and added
 *              isCurrentIterationLast()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A custom tag that evaluates its body once for every day during which a
 * workflow action was performed that is visible to the currently logged-in
 * user. This tag requires a {@code SampleContext} to determine the sample whose
 * history will be exposed.
 * </p><p>
 * Each evaluation of this tags body can be considered to be a 'day' in which
 * the sample was modified. This tag provides the {@code SampleHistoryInfo} for
 * the sample version at the end of the day using its
 * {@code SampleHistoryContext} implementation. Furthermore, information about
 * the actions that were performed on this sample during the day may be retrived
 * by calling {@link SampleDailyVersionIterator#getActionMap() getActionMap()}.
 * </p>
 */
public class SampleDailyVersionIterator extends HtmlPageIterator implements
        SampleHistoryContext {

    /**
     * The most immediate {@code SampleContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * An ordered {@code Collection} of
     * {@code SampleWorkflowBL.SampleActionSummary} objects retrieved by a call
     * to {@link SampleWorkflowBL#getSampleSummaryByDay( FullSampleInfo,
     * UserInfo) SampleWorkflowBL.getSampleSummaryByDay()} made during the
     * {@code FETCHING_PHASE}.
     */
    private Collection<SampleWorkflowBL.SampleActionSummary> actionDaysList;

    /**
     * An iterator over the 'actionDaysList'.
     */
    private Iterator<SampleWorkflowBL.SampleActionSummary> actionDaysIterator;

    /**
     * The daily summary for the day associated with the current iteration of
     * this tag; used to service calls to {@code getSampleHistoryInfo()} or
     * {@code getActionSummary()}.
     */
    private SampleWorkflowBL.SampleActionSummary currentDailySummary;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.actionDaysList = null;
        this.actionDaysIterator = null;
        this.currentDailySummary = null;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the innermost
     * containing {@code SampleContext} for use in the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if no {@code PageContext} is found to
     *         enclose this tag
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get SampleContext
        this.sampleContext = findRealAncestorWithClass(this,
                SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version retrieves the {@code FullSampleInfo} for the
     * sample identified by the {@code SampleContext}.
     * 
     * @throws JspException wrapping any exception thrown while attempting to
     *         fetch the {@code FullSampleInfo}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo si = this.sampleContext.getSampleInfo();
        
        if (si == null) {
            /*
             * return without doing anything; existing null values will indicate
             * that no iterations may be performed
             */
            return rc;
        }

        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());
        
        try {
            FullSampleInfo fsi = RequestCache.getFullSampleInfo(
                    this.pageContext.getRequest(), si.id);

            if (fsi == null) {
                fsi = cc.getSampleManager().getFullSampleInfo(si.id);
                RequestCache.putFullSampleInfo(super.pageContext.getRequest(),
                        fsi);
            }
            this.actionDaysList = SampleWorkflowBL.getSampleSummaryByDay(fsi,
                    (UserInfo) this.pageContext.getSession().getAttribute(
                            "userInfo"));
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        }

        return rc;
    }

    /**
     * Implements {@code SampleHistoryContext}. This method may not be called
     * before the {@code FETCHING_PHASE} and may return null if this tag is not
     * nested within a {@code SampleContext} or if the {@code SampleContext} in
     * which this tag is nested returns null.
     * 
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public SampleHistoryInfo getSampleHistoryInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.currentDailySummary.lastActionPerformed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentIterationLast() {
        return ((this.actionDaysIterator == null)
                || !this.actionDaysIterator.hasNext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.actionDaysList != null) {
            this.actionDaysIterator = this.actionDaysList.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version causes the body to be evaluated once for
     * each {@code SampleHistoryInfo}.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.actionDaysList == null) {
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
        }
        if (this.actionDaysIterator.hasNext()) {
            this.currentDailySummary = this.actionDaysIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleDailyVersionIterator dc
                = (SampleDailyVersionIterator) super.generateCopy(newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);

        return dc;
    }

    /**
     * Gets a map of action codes to counts (number of times the actions were
     * performed during the day represented by this iteration of this tag's
     * body).
     */
    public Map<Integer, Integer> getActionMap() {
        return this.currentDailySummary.actionCodeToCountMap;
    }
}
