/*
 * Reciprocal Net project
 * 
 * HoldingsIterator.java
 * 
 * 20-Oct-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 10-May-2006: jobollin reformatted the source and switched to generics
 * 13-Jun-2006: jobollin removed an unnecessary typecast
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
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that evaluates its body once for each
 * {@code RepositoryHoldingInfo} associated with the sample provided by the
 * enclosing {@code SampleContext}. This tag provides a
 * {@code RepositoryHoldingContext} as well as a {@code SiteContext} that
 * corresponds to {@code RepositoryHoldingInfo.siteId}.
 */
public class HoldingsIterator extends HtmlPageIterator implements
        RepositoryHoldingContext, SiteContext {

    /**
     * The {@code SampleContext} that encloses this tag. This reference is set
     * by {@code onRegistrationBeforeBody()} and is used by
     * {@code onFetchingPhaseBeforeBody()} as the sample whose holdings records
     * will be exposed by this tag.
     */
    private SampleContext sampleContext;

    /**
     * A {@code Collection} of {@code RepositoryHoldingInfo} objects fetched by
     * {@code onFetchingPhaseBeforeBody()} with a call to
     * {@code RepositoryManager.getHoldingsForSample()}.
     */
    private Collection<RepositoryHoldingInfo> holdings;

    /**
     * An {@code Iterator} over the {@code holdings}, initialized by
     * {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<RepositoryHoldingInfo> holdingsIterator;

    /**
     * The {@code RepositoryHoldinInfo} whose siteId indicates the site that
     * will be provided by this {@code SiteContext} implementation during this
     * iteration.
     */
    private RepositoryHoldingInfo currentHolding;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.holdings = null;
        this.holdingsIterator = null;
        this.currentHolding = null;
    }

    /**
     * {@inheritDoc}; this version gets the innermost surrounding
     * {@code SampleContext}.
     * 
     * @throws IllegalStateException if no {@code SampleContext} is found that
     *         encloses this tag.
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
     * {@inheritDoc}; this version fetches the {@code RepositoryHoldingInfo}
     * objects associated with the sample provided by the surrounding
     * {@code SampleContext}.
     * 
     * @throws JspException wrapping any exceptions thrown by the call to core
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc = CoreConnector.extract(
                this.pageContext.getServletContext());

        try {
            SampleInfo si = this.sampleContext.getSampleInfo();
            if (si == null) {
                // provide the null context
                return rc;
            }
            this.holdings = 
                    cc.getRepositoryManager().getHoldingsForSample(si.id);
            for (RepositoryHoldingInfo rhi : this.holdings) {
                /*
                 * for each RepositoryHoldingsInfo, fetch the corresponding
                 * SiteInfo and cache it so that it may be provided by this
                 * SiteContext implementation
                 */
                SiteInfo site = RequestCache.getSiteInfo(
                        this.pageContext.getRequest(), rhi.siteId);
                if (site == null) {
                    site = cc.getSiteManager().getSiteInfo(rhi.siteId);
                    RequestCache.putSiteInfo(this.pageContext.getRequest(),
                            site);
                }
            }
        } catch (ResourceNotFoundException ex) {
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
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.holdings != null) {
            this.holdingsIterator = this.holdings.iterator();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.holdingsIterator == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                    && (getPostedIterationCount()
                            > getIterationCountSinceThisPhaseBegan())) {
                /*
                 * evaluate the body for the benefit of nested elements, even
                 * though those nested elements may not yet get the
                 * SampleTextContext. This ensures that the SampleFields can
                 * parse values that might have been posted for this request
                 */
                return true;
            }
            return false;
        }
        if (this.holdingsIterator.hasNext()) {
            this.currentHolding = this.holdingsIterator.next();
            return true;
        }
        return false;
    }

    /**
     * Implements {@code SiteContext}. This method may not be called before the
     * {@code FETCHING_PHASE} and may return null in the event that this tag is
     * nested within a {@code SampleContext} that returned null or is not nested
     * within a {@code SampleContext}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public SiteInfo getSiteInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        if (this.currentHolding == null) {
            return null;
        }
        return RequestCache.getSiteInfo(this.pageContext.getRequest(),
                this.currentHolding.siteId);
    }

    /**
     * Implements {@code RepositoryHoldingContext}. This method may not be
     * called before the {@code FETCHING_PHASE} and may return null in the event
     * that this tag is nested within a {@code SampleContext} that returned null
     * or is not nested within a {@code SampleContext}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public RepositoryHoldingInfo getRepositoryHoldingInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.currentHolding;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HoldingsIterator dc = (HoldingsIterator) super.generateCopy(newId, map);
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        return dc;
    }
}
