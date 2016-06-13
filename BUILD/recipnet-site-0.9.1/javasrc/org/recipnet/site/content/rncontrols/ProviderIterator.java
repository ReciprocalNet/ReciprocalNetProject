/*
 * Reciprocal Net project
 * 
 * ProviderIterator.java
 *
 * 05-Jan-2005: midurbin completed eisiorho's first draft of this class
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 21-Jun-2005: midurbin moved the ErrorSupplier implementation to the
 *              superclass
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A Custom tag that evaluates its body once for each provider that matches the
 * critera indicated by various tag attributes. This tag needs to be nested
 * within a {@code LabContext} tag to provide information as to which lab's
 * providers it is iterating through. This tag provides a
 * {@code ProviderContext} to any nested tags.
 */
public class ProviderIterator extends HtmlPageIterator
        implements ProviderContext {

    /**
     * An optional property that, if true, indicates that only the
     * {@code ProviderInfo} objects whose {@code isActive} field is set to
     * 'true' should be provided by this {@code ProviderContext} implementation
     * to its body. Defaults to false. This property should not be set to true
     * if {@code restrictToInactiveProviders} is also true.
     */
    private boolean restrictToActiveProviders;

    /**
     * An optional property that, if true, indicates that only the
     * {@code ProviderInfo} objects whose {@code isActive} field is set to
     * 'false' should be provided by this {@code ProviderContext} implementation
     * to its body. Defaults to false. This property should not be set to true
     * if {@code restrictToActiveProviders} is also true.
     */
    private boolean restrictToInactiveProviders;

    /**
     * A {@code Collection} of {@code ProviderInfo} objects that will be
     * provided for each iteration of this tag. Populated by
     * {@code onFetchingPhaseBeforeBody()} from the database.
     */
    private Collection<ProviderInfo> providerInfos;

    /**
     * An {@code Iterator} over the {@code ProviderInfos}, initialized by
     * {@code beforeIteration()} and accessed by {@code onIterationBeforeBody()}.
     */
    private Iterator<ProviderInfo> providerInfoIterator;

    /**
     * A member of the {@code ProviderInfos} {@code Collection} that represents
     * the {@code ProviderInfo} provided by this {@code ProviderContext}
     * implementation for the current iteration. This value is set by
     * {@code onIterationBeforeBody()} and returned by calls to
     * {@code getProviderInfo()}.
     */
    private ProviderInfo currentProviderInfo;

    /**
     * The {@code LabContext} which this control sits in. Initialized in reset()
     * and discovered in {@code onRegistrationPhaseBeforeBody()}.
     */
    private LabContext labContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.restrictToActiveProviders = false;
        this.restrictToInactiveProviders = false;
        this.providerInfos = null;
        this.providerInfoIterator = null;
        this.currentProviderInfo = null;
        this.labContext = null;
    }

    /**
     * @param restrict indicates whether only active providers indicated by the
     *        most immediate {@code LabContext} will be included.
     * @throws IllegalArgumentException if this call attempts to set
     *         {@code restrictToActiveProviders} to true when
     *         {@code restrictToInactiveProviders} is also set to true.
     */
    public void setRestrictToActiveProviders(boolean restrict) {
        if (restrict && this.restrictToInactiveProviders) {
            throw new IllegalArgumentException();
        }
        this.restrictToActiveProviders = restrict;
    }

    /**
     * @return a boolean indicating whether only active providers by the most
     *         immediate {@code LabContext} will be included.
     */
    public boolean getRestrictToActiveProviders() {
        return this.restrictToActiveProviders;
    }

    /**
     * @param restrict indicates whether only inactive providers indicated by
     *        the most immediate {@code LabContext} will be included.
     * @throws IllegalArgumentException if this call attempts to set
     *         {@code restrictToInactiveProviders} to true when
     *         {@code restrictToActiveProviders} is also set to true.
     */
    public void setRestrictToInactiveProviders(boolean restrict) {
        if (restrict && this.restrictToActiveProviders) {
            throw new IllegalArgumentException();
        }
        this.restrictToInactiveProviders = restrict;
    }

    /**
     * @return a boolean indicating whether only inactive providers by the most
     *         immediate {@code LabContext} will be included.
     */
    public boolean getRestrictToInactiveProviders() {
        return this.restrictToInactiveProviders;
    }

    /**
     * Implements {@code ProviderContext}. This method may not be called before
     * the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public ProviderInfo getProviderInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return currentProviderInfo;
    }

    /**
     * {@inheritDoc}; this version instantiates its 'owned' element, finds the
     * nearest {@code ProviderContext} and delegates back to the superclass.
     * 
     * @param pageContext the current {@code PageContext}; may be useful to
     *        superclass or 'owned' elements.
     * @return the return code is taken from the superclass' implementation of
     *         this method.
     * @throws IllegalStateException if this tag is not within a
     *         {@code ProviderContext}.
     * @throws JspException if an exception is encountered during processing for
     *         this phase.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            // This tag can't be used without a ProviderContext
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version fetches {@code ProviderInfo} objects to
     * populate {@code providerInfos}.
     * 
     * @throws IllegalStateException if {@code ProviderContext} returns null
     * @throws JspException wrapping other exceptions thrown by core during the
     *         fetching of container objects.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        this.providerInfos = new ArrayList<ProviderInfo>();
        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());
        try {
            for (ProviderInfo provider : cc.getSiteManager().getAllProviderInfo(
                    this.labContext.getLabInfo().id)) {
                if ((provider.isActive && !this.restrictToInactiveProviders)
                        || (!provider.isActive
                                && !this.restrictToActiveProviders)) {
                    providerInfos.add(provider);
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
     * {@inheritDoc}; this version performs a simple check to verify that the
     * number of posted iterations is equal to the number of iterations
     * calculated during the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if there is a different number of
     *         {@code ProviderInfo} objects than the
     *         {@code postedIterationCount} maintained by the superclass.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        if (this.providerInfos.size() != getPostedIterationCount()) {
            /*
             * the number of ProviderInfo objects that match the criteria of
             * this control is inconsistent with the value from the previous
             * request
             */
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.providerInfos != null) {
            this.providerInfoIterator = this.providerInfos.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version causes the body to be evaluated once for
     * each provider associated with the surrounding lab context
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.providerInfoIterator == null) {
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
        if (this.providerInfoIterator.hasNext()) {
            this.currentProviderInfo = this.providerInfoIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ProviderIterator dc = (ProviderIterator) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);
        if (this.providerInfos != null) {
            dc.providerInfos = new ArrayList<ProviderInfo>(this.providerInfos);
        }

        return dc;
    }
}
