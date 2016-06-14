/*
 * Reciprocal Net project
 * 
 * LabIterator.java
 *
 * 17-Nov-2004: eisiorho wrote first draft
 * 14-Dec-2004: eisiorho fixed bug #1482 in onFetchingPhaseBeforeBody() and
 *              reset()
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A Custom tag that evaluates its body once for each lab that matches the
 * critera indicated by various tag attributes.  This tag provides a
 * {@code LabContext} to any nested tags.
 */
public class LabIterator extends HtmlPageIterator implements LabContext {

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code LabInfo} objects whose {@code isActive} field is set
     * to 'true' should be provided by this {@code LabContext}
     * implementation to its body. Defaults to false.
     */
    private boolean restrictToActiveLabs;

    /**
     * A {@code Collection} of {@code LabInfo} objects that
     * will be provided for each iteration of this tag.  Populated by
     * {@code onFetchingPhaseBeforeBody()} from the database.
     */
    private Collection<LabInfo> labInfos;

    /**
     * An {@code Iterator} over the {@code LabInfos},
     * initialized by {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<LabInfo> labInfoIterator;

    /**
     * A member of the {@code LabInfos} {@code Collection} that
     * represents the {@code LabInfo} provided by this
     * {@code LabContext} implementation for the current iteration.
     * This value is set by {@code onIterationBeforeBody()} and returned
     * by calls to {@code getLabInfo()}.
     */
    private LabInfo currentLabInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.restrictToActiveLabs = false;
        this.labInfos = null;
        this.labInfoIterator = null;
        this.currentLabInfo = null;
    }

    /**
     * @param restrictToActiveLabs indicates whether only active labs
     *     indicated by the most immediate {@code LabContext} will be
     *     included.
     */
    public void setRestrictToActiveLabs(boolean restrictToActiveLabs) {
        this.restrictToActiveLabs = restrictToActiveLabs;
    }

    /**
     * @return a boolean indicating whether only active labs by the most
     *     immediate {@code LabContext} will be included.
     */
    public boolean getRestrictToActiveLabs() {
        return this.restrictToActiveLabs;
    }

    /**
     * Implements {@code LabContext}.  This method may not be called
     * before the {@code FETCHING_PHASE}.
     * @throws IllegalStateException if called before the
     *     {@code FETCHING_PHASE}
     */
    public LabInfo getLabInfo() {
        if ((this.getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (this.getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }

        return currentLabInfo;
    }

    /**
     * {@inheritDoc}; this version fetches the {@code LabInfo} objects over
     * which the iteration will be performed
     * @throws IllegalStateException if {@code LabContext} returns {@code null}
     * @throws JspException wrapping other exceptions thrown by core during
     *     the fetching of container objects.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        this.labInfos = new ArrayList<LabInfo>();

        try {
            for (LabInfo lab : cc.getSiteManager().getAllLabInfo()) {
                RequestCache.putLabInfo(this.pageContext.getRequest(), lab);
                if (!restrictToActiveLabs || lab.isActive) {
                    /* the current LabInfo should not be added to the
                     collection of LabInfo's because it does not match
                     the criteria for inclusion; move on to the next*/
                    this.labInfos.add(lab);
                }
            }
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
     *     {@code LabInfo} objects than the
     *     {@code postedIterationCount} maintained by the superclass.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        if (this.labInfos.size() != getPostedIterationCount()) {
            // the number of LabInfo objects that match the criteria of this
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
        if (this.labInfos != null) {
            this.labInfoIterator = this.labInfos.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version ensures that the body is
     * evaluated once for each lab that will be displayed
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.labInfoIterator == null) {
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
        if (this.labInfoIterator.hasNext()) {
            this.currentLabInfo = this.labInfoIterator.next();
            return true;
        } else {
            return false;
        }
    }
}
