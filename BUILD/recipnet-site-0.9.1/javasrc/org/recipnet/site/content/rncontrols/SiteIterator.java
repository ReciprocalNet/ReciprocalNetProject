/*
 * Reciprocal Net project
 * SiteIterator.java
 *
 * 19-Mar-2009: ekoperda wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A Custom tag that evaluates its body once for each site in the Site Network.
 * This tag provides a {@code SiteContext} to any nested tags.
 */
public class SiteIterator extends HtmlPageIterator implements SiteContext {

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code SiteInfo} objects whose {@code isActive} field is set
     * to 'true' should be provided by this {@code SiteContext}
     * implementation to its body. Defaults to false.
     */
    private boolean restrictToActiveSites;

    /**
     * A {@code List} of {@code SiteInfo} objects that
     * will be provided for each iteration of this tag.  Populated by
     * {@code onFetchingPhaseBeforeBody()} from the database.
     */
    private List<SiteInfo> siteInfos;

    /**
     * An {@code Iterator} over the {@code SiteInfos},
     * initialized by {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<SiteInfo> siteInfoIterator;

    /**
     * A member of the {@code siteInfos} {@code Collection} that
     * represents the {@code SiteInfo} provided by this
     * {@code SiteContext} implementation for the current iteration.
     * This value is set by {@code onIterationBeforeBody()} and returned
     * by calls to {@code getLabInfo()}.
     */
    private SiteInfo currentSiteInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.restrictToActiveSites = false;
        this.siteInfos = null;
        this.siteInfoIterator = null;
        this.currentSiteInfo = null;
    }

    /**
     * @param restrictToActiveSites indicates whether only active sites
     *     indicated by the most immediate {@code SiteContext} will be
     *     included.
     */
    public void setRestrictToActiveSites(boolean restrictToActiveSites) {
        this.restrictToActiveSites = restrictToActiveSites;
    }

    /**
     * @return a boolean indicating whether only active sites by the most
     *     immediate {@code SiteContext} will be included.
     */
    public boolean getRestrictToActiveSites() {
        return this.restrictToActiveSites;
    }

    /**
     * Implements {@code SiteContext}.  This method may not be called
     * before the {@code FETCHING_PHASE}.
     * @throws IllegalStateException if called before the
     *     {@code FETCHING_PHASE}
     */
    public SiteInfo getSiteInfo() {
        if ((this.getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (this.getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }

        return currentSiteInfo;
    }

    /**
     * {@inheritDoc}; this version fetches the {@code SiteInfo} objects over
     * which the iteration will be performed
     * @throws IllegalStateException if {@code SiteContext} returns
     * {@code null}
     * @throws JspException wrapping other exceptions thrown by core during
     *     the fetching of container objects.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        this.siteInfos = new ArrayList<SiteInfo>();

        try {
            for (SiteInfo site : cc.getSiteManager().getAllSiteInfo()) {
                RequestCache.putSiteInfo(this.pageContext.getRequest(), site);
                if (!restrictToActiveSites || site.isActive) {
                    /* the current SiteInfo should not be added to the
                     collection of SiteInfo's because it does not match
                     the criteria for inclusion; move on to the next*/
                    this.siteInfos.add(site);
                }
            }
	    Collections.sort(this.siteInfos);
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version verifies that the number of posted iterations
     * is equal to the number of iterations calculated during the
     * {@code FETCHING_PHASE}.
     * @throws IllegalStateException if there is a different number of
     *     {@code SiteInfo} objects than the
     *     {@code postedIterationCount} maintained by the superclass.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        if (this.siteInfos.size() != getPostedIterationCount()) {
            // the number of SiteInfo objects that match the criteria of this
            // control is inconsistent with the value from the previous request
            throw new IllegalStateException();
        }
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.siteInfos != null) {
            this.siteInfoIterator = this.siteInfos.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version ensures that the body is
     * evaluated once for each site that will be displayed
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.siteInfoIterator == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                 && (getPostedIterationCount()
                         > getIterationCountSinceThisPhaseBegan())) {
                /* evaluate the body for the benefit of nested elements,
                 even though those nested elements may not yet get the
                 SampleTextContext.  This ensures that the SampleFields can
                 parse values that might have been posted for this request*/
                return true;
            }
            return false;
        }
        if (this.siteInfoIterator.hasNext()) {
            this.currentSiteInfo = this.siteInfoIterator.next();
            return true;
        } else {
            return false;
        }
    }
}
