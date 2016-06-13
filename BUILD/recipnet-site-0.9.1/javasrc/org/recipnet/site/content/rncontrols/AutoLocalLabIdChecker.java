/*
 * Reciprocal Net project
 * 
 * AutoLocalLabIdChecker.java
 *
 * 27-Sep-2005: midurbin wrote first draft
 * 13-Jun-2006: jobolllin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourcesExhaustedException;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A 'checker' that evaluates its body based on whether local lab id
 * autogeneration is enabled and functioning for the lab defined by the
 * surrounding {@code LabContext}. The various 'include' properties determine
 * the conditions required for inclusion. Only one inclusion property may be
 * set.
 */
public class AutoLocalLabIdChecker extends AbstractChecker {

    /**
     * The most immediately surrounding {@code LabContext}. This is determined
     * by {@code onRegistrationPhaseBeforeBody()} and used to determine the lab
     * whose localLabId auto assignment status is being checked.
     */
    private LabContext labContext;

    /**
     * An optional property that when set to true indicates that this tag should
     * evaluate its body only if local lab id autogeneration has been configured
     * for the lab. (Whether all of the numbers have been used or not is not
     * considered.)
     */
    private boolean includeOnlyIfAutoNumberingIsConfigured;

    /**
     * An optional property that when set to true indicates that this tag should
     * evaluate its body only if local lab id autogeneration is enabled but all
     * of the possible values have already been used.
     */
    private boolean includeOnlyIfNumbersAreExhausted;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.labContext = null;
        this.includeOnlyIfAutoNumberingIsConfigured = false;
        this.includeOnlyIfNumbersAreExhausted = false;
    }

    /** Setter for the 'includeOnlyIfAutoNumberingIsConfigured' property. */
    public void setIncludeOnlyIfAutoNumberingIsConfigured(boolean include) {
        this.includeOnlyIfAutoNumberingIsConfigured = include;
    }

    /** Getter for the 'includeOnlyIfAutoNumberingIsConfigured' property. */
    public boolean getIncludeOnlyIfAutoNumberingIsConfigured() {
        return this.includeOnlyIfAutoNumberingIsConfigured;
    }

    /** Setter for the 'includeOnlyIfNumbersAreExhausted' property. */
    public void setIncludeOnlyIfNumbersAreExhausted(boolean include) {
        this.includeOnlyIfNumbersAreExhausted = include;
    }

    /** Getter for the 'includeOnlyIfNumbersAreExhausted' property. */
    public boolean getIncludeOnlyIfNumbersAreExhausted() {
        return this.includeOnlyIfNumbersAreExhausted;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the innermost
     * surrounding {@code LabContext}
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code LabContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }
        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether auto-numbering is enabled
     * for the lab, so that this 'checker' tag can determine whether to suppress
     * its body.
     * 
     * @throws JspException wrapping any {@code OperationFailedException} or
     *         {@code RemoteException} thrown by core.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        CoreConnector cc
                = CoreConnector.extract(pageContext.getServletContext());

        try {
            String suggestedId = RequestCache.getNextAutoLocalLabId(
                    pageContext.getRequest(), this.labContext.getLabInfo().id);
            if (suggestedId == null) {
                // cache miss
                try {
                    suggestedId = cc.getSampleManager().getNextUnusedLocalLabId(
                            this.labContext.getLabInfo().id);
                } catch (ResourcesExhaustedException ex) {
                    suggestedId = RequestCache.AUTO_LOCAL_LAB_IDS_EXHAUSTED;
                }
                if (suggestedId == null) {
                    suggestedId = RequestCache.AUTO_LOCAL_LAB_IDS_DISABLED;
                }
                RequestCache.putNextAutoLocalLabId(pageContext.getRequest(),
                        this.labContext.getLabInfo().id, suggestedId);
            }
            if (this.includeOnlyIfAutoNumberingIsConfigured) {
                super.inclusionConditionMet = (suggestedId
                        != RequestCache.AUTO_LOCAL_LAB_IDS_DISABLED);

            } else if (this.includeOnlyIfNumbersAreExhausted) {
                inclusionConditionMet = (suggestedId
                        == RequestCache.AUTO_LOCAL_LAB_IDS_EXHAUSTED);
            }
        } catch (NullPointerException ex) {
            /*
             * fall through, leaving the inclusionConditionMet as false because
             * no lab was provided by the LabContext
             */
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }
        
        return rc;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AutoLocalLabIdChecker dc
                = (AutoLocalLabIdChecker) super.generateCopy(newId, map);
        
        dc.labContext = (LabContext) map.get(this.labContext);
        
        return dc;
    }
}
