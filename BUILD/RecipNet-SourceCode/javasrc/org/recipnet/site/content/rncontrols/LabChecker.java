/*
 * Reciprocal Net project
 * 
 * LabChecker.java
 * 
 * 30-Nov-2004: midurbin wrote first draft
 * 14-Dec-2004: midurbin added 'requireInactive', 'requireNonlocal'
 *              properties and changed semantics to support the use of multiple
 *              requirements for inclusion at once
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 13-Jun-2006: jobollin made this class extend AbstractChecker and
 *              reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that must be nested within a {@code LabContext} and bases
 * whether to evaluate its body on the characteristics of that
 * {@code LabContext}'s lab, one of various inclusion requirements and the
 * 'invert' property. The JSP author may indicate any number of the possible
 * inclusion requirements ('requireActive', 'requireLocal') and the body will be
 * included (rather than suppressed) if the lab meets the required condition (if
 * multiple requirements are indicated, ALL of them must be met for inclusion).
 * If 'invert' is set as well, the body will be suppressed when it would
 * otherwise be included and vice versa. If no inclusion requirements are set,
 * the behavior of this tag is undefined. This tag implements the
 * {@code SuppressionContext} so that when the body is not included, no nested
 * tags will perform any response-altering actions as well as having their
 * output excluded.
 */
public class LabChecker extends AbstractChecker {

    /**
     * A reference to the most immediate {@code LabContext} in which this tag is
     * nested. This reference is set by {@code onRegistrationPhaseBeforeBody()}
     * and used by {@code onFetchingPhaseBeforeBody()} to get the
     * {@code LabInfo} for the lab whose characteristics are used to determine
     * whether this tag's body will be suppressed or not.
     */
    private LabContext labContext;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) when the lab is active. If
     * {@code invert} is set to true, the body will be suppressed when this is
     * set to true and the lab is active. This property should not be set to
     * true when 'requireInactive' is also set to true. If this tag is left as
     * false, its default value, whether a lab is active will not be a factor in
     * determining whether the body of this tag will be included.
     */
    private boolean requireActive;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) when the lab is inactive. If
     * {@code invert} is set to true, the body will be suppressed when this is
     * set to true and the lab is inactive. This property should not be set to
     * true when 'requireActive' is also set to true. If this tag is left as
     * false, its default value, whether a lab is inactive will not be a factor
     * in determining whether the body of this tag will be included.
     */
    private boolean requireInactive;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) when the lab is local. If
     * {@code invert} is set to true, the body will be suppressed when this is
     * set to true and the lab is local. This tag should not be set to true when
     * 'requireNonlocal' is also set to true. If this tag is left as false, its
     * default value, whether a lab is local will not be a factor in determining
     * whether the body of this tag will be included.
     */
    private boolean requireLocal;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) when the lab is nonlocal. If
     * {@code invert} is set to true, the body will be suppressed when this is
     * set to true and the lab is nonlocal. This tag should not be set to true
     * when 'requireLocal' is also set to true. If this tag is left as false,
     * its default value, whether a lab is nonlocal will not be a factor in
     * determining whether the body of this tag will be included.
     */
    private boolean requireNonlocal;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.labContext = null;
        this.requireActive = false;
        this.requireInactive = false;
        this.requireLocal = false;
        this.requireNonlocal = false;
    }

    /**
     * @param require indicates whether the decision to include or suppress the
     *        body of this tag should be based on whether the lab is active
     * @throws IllegalArgumentException if 'requireInactive' has already been
     *         set to true
     */
    public void setRequireActive(boolean require) {
        if (require && this.requireInactive) {
            throw new IllegalArgumentException();
        }
        this.requireActive = require;
    }

    /**
     * @return a boolean indicating whether the decision to include or suppress
     *         the body of this tag should be based on whether the lab is active
     */
    public boolean getRequireActive() {
        return this.requireActive;
    }

    /**
     * @param require indicates whether the decision to include or suppress the
     *        body of this tag should be based on whether the lab is inactive
     * @throws IllegalArgumentException if 'requireActive' has already been set
     *         to true
     */
    public void setRequireInactive(boolean require) {
        if (require && this.requireActive) {
            throw new IllegalArgumentException();
        }
        this.requireInactive = require;
    }

    /**
     * @return a boolean indicating whether the decision to include or suppress
     *         the body of this tag should be based on whether the lab is
     *         inactive
     */
    public boolean getRequireInactive() {
        return this.requireInactive;
    }

    /**
     * @param require indicates whether the decision to include or suppress the
     *        body of this tag should be based on whether the lab is local
     * @throws IllegalArgumentException if 'requireNonlocal' has already been
     *         set to true
     */
    public void setRequireLocal(boolean require) {
        if (require && this.requireNonlocal) {
            throw new IllegalArgumentException();
        }
        this.requireLocal = require;
    }

    /**
     * @return a boolean indicating whether the decision to include or suppress
     *         the body of this tag should be based on whether the lab is local
     */
    public boolean getRequireLocal() {
        return this.requireLocal;
    }

    /**
     * @param require indicates whether the decision to include or suppress the
     *        body of this tag should be based on whether the lab is nonlocal
     * @throws IllegalArgumentException if 'requireLocal' has already been set
     *         to true
     */
    public void setRequireNonlocal(boolean require) {
        if (require && this.requireLocal) {
            throw new IllegalArgumentException();
        }
        this.requireNonlocal = require;
    }

    /**
     * @return a boolean indicating whether the decision to include or suppress
     *         the body of this tag should be based on whether the lab is
     *         nonlocal
     */
    public boolean getRequireNonlocal() {
        return this.requireNonlocal;
    }

    /**
     * {@inheritDoc}; this version delegates to the superclass, then looks up a
     * reference to the innermost {@code LabContext} in which this tag is nested
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code LabContext}, or if the superclass's version of this
     *         method throws it
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find LabContext
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether the body of this tag
     * should be included or suppressed based on the inclusion properties set by
     * the JSP author and the characteristics of the {@code LabInfo} returned by
     * the surrounding {@code LabContext}.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        LabInfo labInfo = this.labContext.getLabInfo();

        if (labInfo == null) {
            this.inclusionConditionMet = false;
            return rc;
        } else {
            this.inclusionConditionMet = true;
        }
        if (this.requireActive) {
            // setter methods throw an exception to ensure the following
            assert !this.requireInactive;

            this.inclusionConditionMet &= labInfo.isActive;
        }
        if (this.requireInactive) {
            // setter methods throw an exception to ensure the following
            assert !this.requireActive;

            this.inclusionConditionMet &= !labInfo.isActive;
        }
        if (this.requireLocal || this.requireNonlocal) {
            
            /*
             * fetch the SiteInfo object that is needed to determine if the lab
             * is local or nonlocal
             */
            SiteInfo localSite = RequestCache.getLocalSiteInfo(
                    this.pageContext.getRequest());

            if (localSite == null) {
                // cache miss
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());

                try {
                    localSite = cc.getSiteManager().getLocalSiteInfo();
                    RequestCache.putLocalSiteInfo(
                            this.pageContext.getRequest(), localSite);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
            }

            if (this.requireLocal) {
                // setter methods throw an exception to ensure the following
                assert !this.requireNonlocal;

                this.inclusionConditionMet
                        &= (localSite.id == labInfo.homeSiteId);
            }
            if (this.requireNonlocal) {
                // setter methods throw an exception to ensure the following
                assert !this.requireLocal;

                this.inclusionConditionMet
                        &= (localSite.id != labInfo.homeSiteId);
            }
        }
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        LabChecker dc = (LabChecker) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);

        return dc;
    }
}
