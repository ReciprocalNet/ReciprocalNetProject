/*
 * Reciprocal Net project
 * 
 * SampleHistoryIterator.java
 * 
 * 20-Oct-2004: midurbin wrote the first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 21-Jun-2005: midurbin added isCurrentIterationLast()
 * 05-Jul-2005: midurbin added SampleContext implementation
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that evaluates its body once for every {@code SampleHistoryInfo}
 * associated with the sample provided by the most immediate
 * {@code SampleContext}. This tag implements {@code SampleHistoryContext} to
 * make the {@code SampleHistoryInfo} objects available to nested tags. The
 * {@code SampleInfo} associated with each {@code SampleHistoryInfo} is exposed
 * through the {@code SampleContext} implementation.
 */
public class SampleHistoryIterator extends HtmlPageIterator implements
        SampleHistoryContext, SampleContext {

    /**
     * The most immediate {@code SampleContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * The {@code SampleHistoryInfo} that will be provided to implement
     * {@code SampleHistoryContext} during this body evaluation. Set by
     * {@code onIterationBeforeBody()} to a value from the fetched
     * {@code FullSampleInfo}'s {@code history} member variable.
     */
    private SampleHistoryInfo currentSampleHistoryInfo;

    /**
     * A reference to the {@code FullSampleInfo} that contains the
     * {@code SampleHistoryInfo} objects that will be iterated over by this tag.
     * This value is retrieved by {@code onFetchingPhaseBeforeBody()}.
     */
    private FullSampleInfo fullSampleInfo;

    /**
     * An {@code Iterator} over the {@code history} of the
     * {@code FullSampleInfo}.
     */
    private Iterator<SampleHistoryInfo> historyInfoIterator;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.currentSampleHistoryInfo = null;
        this.fullSampleInfo = null;
        this.historyInfoIterator = null;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost containing
     * {@code SampleContext}, for use during the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if no {@code SampleContext} encloses this
     *         tag
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
             * return without doing anything, existing null values will indicate
             * that no iterations may be performed
             */
            return rc;
        }

        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        try {
            this.fullSampleInfo = RequestCache.getFullSampleInfo(
                    this.pageContext.getRequest(), si.id);
            if (this.fullSampleInfo == null) {
                this.fullSampleInfo = cc.getSampleManager().getFullSampleInfo(
                        si.id);
                RequestCache.putFullSampleInfo(super.pageContext.getRequest(),
                        this.fullSampleInfo);
            }
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
     * {@inheritDoc}; this version verifies that the number of iterations
     * posted is equal to the number of {@code SampleHistoryInfo} objects over
     * which this tag iterates.
     * 
     * @throws IllegalStateException if the number of iterations posted is not
     *         equal to the number of {@code SampleHistoryInfo} objects over
     *         which this tag iterates.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if ((this.fullSampleInfo != null)
                && (fullSampleInfo.history.size() != getPostedIterationCount())) {
            throw new IllegalStateException();
        }

        return super.onProcessingPhaseAfterBody(pageContext);
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
        return this.currentSampleHistoryInfo;
    }

    /**
     * Implements {@code SampleContext}. This method may not be called before
     * the {@code FETCHING_PHASE} and may return null if this tag is not nested
     * within a {@code SampleContext} or if the {@code SampleContext} in which
     * this tag is nested returns null.
     * 
     * @throws IllegalStateException if this method is called before the
     *         {@code FETCHING_PHASE}
     */
    public SampleInfo getSampleInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        if (this.currentSampleHistoryInfo == null) {
            return null;
        }
        return this.currentSampleHistoryInfo.sample;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentIterationLast() {
        return ((this.historyInfoIterator == null)
                || !this.historyInfoIterator.hasNext());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.fullSampleInfo != null) {
            this.historyInfoIterator = fullSampleInfo.history.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version causes the body to be evaluated once for
     * each {@code SampleHistoryInfo}.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.fullSampleInfo == null) {
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
        if (this.historyInfoIterator.hasNext()) {
            this.currentSampleHistoryInfo = historyInfoIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleHistoryIterator dc = (SampleHistoryIterator) super.generateCopy(
                newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);

        return dc;
    }
}
