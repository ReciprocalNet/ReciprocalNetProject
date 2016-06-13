/*
 * Reciprocal Net project
 * 
 * AuthorizationChecker.java
 * 
 * 12-May-2004: cwestnea wrote first draft
 * 21-Jun-2004: cwestnea added canSeeLabSummary attribute
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 18-Aug-2004: cwestnea added suppressRenderingOnly and canEditSample 
 *              attributes
 * 19-Aug-2004: cwestnea added canFilterResultsByOwnProvider and 
 *              canSeeProviderListForLab
 * 26-Aug-2004: midurbin added SuppressionContext implementation
 * 24-Sep-2004: midurbin fixed bug #1394 in isTagsBodySuppressedThisPhase()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 28-Apr-2005: midurbin added 'canSeeSampleText' and 'requireAuthentication'
 * 29-Jun-2005: midurbin removed the UserContext implementation
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * This tag will skip or suppress its body, depending on
 * {@code suppressRenderingOnly}, if the user is not authorized to view it.
 * Attributes corresponding to methods on {@code AuthorizationCheckerBL} may be
 * set to true and this tag will call the appropriate method with the session
 * user. All checks are logically ANDed together. There is also an invert
 * attribute which will perform a logical NOT after all the checks are ANDed
 * together.
 * </p><p>
 * Some checks relate to objects in addition to the curently logged-in user's
 * {@code UserInfo} object. For instance, any check about the user's
 * authorization to access some other container object requires that this tag be
 * nested within a context implementation to provide that container object
 * ({@link SampleContext} for example). Because these contexts don't fetch and
 * make their container objects available until the {@code FETCHING_PHASE}, this
 * tag cannot determine whether to evaluate its body during the
 * {@code REGISTRATION_PHASE}.  This is a problem because a tag must evaluate
 * its body during all phases or during no phases, but may not evaluate its body
 * during SOME phases. This is because nested tags will not be granted their
 * whole phase lifcycle if some incomplete subset of the phases are evaluated.
 * Therefore, in such cases this tag should SUPPRESS its body (using a
 * {@link org.recipnet.common.controls.SuppressionContext SuppressionContext}
 * implementation). <strong>To ensure proper behavior and to avoid exceptions,
 * the 'suppressRenderingOnly' property must be set to true whenever any other
 * properties require that other container context implementations be
 * consulted.</strong>  See the property variables' javadocs to verify which
 * properties come with this requirement.
 * </p>
 */
public class AuthorizationChecker extends HtmlPageElement implements
        SuppressionContext {

    /**
     * Optional attribute which when {@code true} adds the requirement that the
     * current session must be associated with a specific user. (ie, someone is
     * logged in) Unlike many other properties, this defaults to {@code true}.
     */
    private boolean requireAuthentication;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canSubmitSamples(UserInfo)} to be checked.
     */
    private boolean canSubmitSamples;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canAdministerLabs(UserInfo)} to be checked.
     */
    private boolean canAdministerLabs;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canSeeLabSummary(UserInfo)} to be checked.
     */
    private boolean canSeeLabSummary;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canEditSample(UserInfo, SampleInfo)} to be
     * checked. {@code suppressRenderingOnly} must also be set to {@code true},
     * as this cannot be checked until the fetching phase. If this is true, this
     * tag must be placed within a {@code SampleContext}.
     */
    private boolean canEditSample;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canSeeProviderListForLab(UserInfo, int)} to
     * be checked.
     */
    private boolean canSeeProviderListForLab;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canFilterResultsByOwnProvider(UserInfo)} to
     * be checked.
     */
    private boolean canFilterResultsByOwnProvider;

    /**
     * Optional attribute which when {@code true} causes
     * {@link AuthorizationCheckerBL#canSeeSampleText(UserInfo, SampleInfo,
     * SampleTextInfo)} to be checked. When this property is set to
     * {@code true}, this tag must be nested within a {@code SampleTextContext}
     * and a {@code SampleContext} and 'suppressRenderingOnly' must also be set
     * to {@code true} because the container objects needed to perform this
     * check will not be available from their contexts until the fetching phase.
     * In most cases 'requireAuthentication' should be set to {@code false}
     * because 'requireAuthentication' is often more restrictive than this
     * check.
     */
    private boolean canSeeSampleText;

    /**
     * Optional attribute which when {@code true} performs a logical NOT after
     * all the checks have been ANDed together to determine whether the body is
     * evaluated, skipped or suppressed.
     */
    private boolean invert;

    /**
     * Optional attribute which when {@code true} will cause this tag to
     * suppress instead of skip its body. This means that custom tags with side
     * effects may not be safely used in the body when this is {@code true},
     * but that tests that must be done during the fetching phase can be used.
     * If this is {@code false}, the tag will skip its body on each phase if
     * the user is not authorized.
     */
    private boolean suppressRenderingOnly;

    /** The currently logged in user. */
    private UserInfo user;

    /**
     * The enclosing {@code SampleContext} is retrieved if a selected check
     * requires it.
     */
    private SampleContext sampleContext;

    /**
     * The enclosing {@code SampleContext} is retrieved if a selected check
     * requires it.
     */
    private SampleTextContext sampleTextContext;

    /**
     * Internal state variable used to keep track of whether or not all of the
     * conditions are met. The body of this tag is evaluated normally if this is
     * true and 'invert' is false, or if this is false and 'invert' is true.
     * This variable is cleared by {@code reset()}, set by
     * {@code onRegistrationPhaseBeforeBody()} and possibly updated by
     * {@code onRenderingPhaseBeforeBody()}.
     */
    private boolean isAuthorized;

    /**
     * The most immediate {@code SuppressionContext} implementation; determined
     * during {@code onRegistrationPhaseBeforeBody()} and used to ensure that
     * this tag propagates suppression indicators from its ancestry along with
     * its own suppression indicators.
     */
    private SuppressionContext suppressionContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.isAuthorized = false;
        this.requireAuthentication = true;
        this.canSubmitSamples = false;
        this.canAdministerLabs = false;
        this.canSeeLabSummary = false;
        this.canEditSample = false;
        this.canSeeProviderListForLab = false;
        this.canFilterResultsByOwnProvider = false;
        this.canSeeSampleText = false;
        this.invert = false;
        this.suppressRenderingOnly = false;
        this.user = null;
        this.sampleContext = null;
        this.sampleTextContext = null;
        this.suppressionContext = null;
    }

    /** @param invert should the tests be inverted */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /** @return should the tests be inverted */
    public boolean getInvert() {
        return this.invert;
    }

    /**
     * @param suppressRenderingOnly should the body be suppressed instead of
     *        skipped
     */
    public void setSuppressRenderingOnly(boolean suppressRenderingOnly) {
        this.suppressRenderingOnly = suppressRenderingOnly;
    }

    /** @return should the body be suppressed instead of skipped. */
    public boolean getSuppressRenderingOnly() {
        return this.suppressRenderingOnly;
    }

    /** @param requireAuthentication is authentication required */
    public void setRequireAuthentication(boolean requireAuthentication) {
        this.requireAuthentication = requireAuthentication;
    }

    /** @return is this authorization required */
    public boolean getRequireAuthentication() {
        return this.requireAuthentication;
    }

    /** @param canSubmitSamples is this authorization required */
    public void setCanSubmitSamples(boolean canSubmitSamples) {
        this.canSubmitSamples = canSubmitSamples;
    }

    /** @return is this authorization required */
    public boolean getCanSubmitSamples() {
        return this.canSubmitSamples;
    }

    /** @param canAdministerLabs is this authorization required */
    public void setCanAdministerLabs(boolean canAdministerLabs) {
        this.canAdministerLabs = canAdministerLabs;
    }

    /** @return is this authorization required */
    public boolean getCanAdministerLabs() {
        return this.canAdministerLabs;
    }

    /** @param canSeeLabSummary is this authorization required */
    public void setCanSeeLabSummary(boolean canSeeLabSummary) {
        this.canSeeLabSummary = canSeeLabSummary;
    }

    /** @return is this authorization required */
    public boolean getCanSeeLabSummary() {
        return this.canSeeLabSummary;
    }

    /** @param canEditSample is this authorization required */
    public void setCanEditSample(boolean canEditSample) {
        this.canEditSample = canEditSample;
    }

    /** @return is this authorization required */
    public boolean getCanEditSample() {
        return this.canEditSample;
    }

    /** @param canSeeProviderListForLab is this authorization required */
    public void setCanSeeProviderListForLab(boolean canSeeProviderListForLab) {
        this.canSeeProviderListForLab = canSeeProviderListForLab;
    }

    /** @return is this authorization required */
    public boolean getCanSeeProviderListForLab() {
        return this.canSeeProviderListForLab;
    }

    /** @param canFilterResultsByOwnProvider is this authorization required */
    public void setCanFilterResultsByOwnProvider(
            boolean canFilterResultsByOwnProvider) {
        this.canFilterResultsByOwnProvider = canFilterResultsByOwnProvider;
    }

    /** @return is this authorization required */
    public boolean getCanFilterResultsByOwnProvider() {
        return this.canFilterResultsByOwnProvider;
    }

    /** @param canSeeSampleText is this authorization required */
    public void setCanSeeSampleText(boolean canSeeSampleText) {
        this.canSeeSampleText = canSeeSampleText;
    }

    /** @return is this authorization required */
    public boolean getCanSeeSampleText() {
        return this.canSeeSampleText;
    }

    /** Implements {@code SuppressionContext}. */
    public boolean isTagsBodySuppressedThisPhase() {
        if ((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase()) {
            return true;
        }
        return !(this.isAuthorized ^ this.invert);
    }

    /**
     * {@inheritDoc}; this version makes some authorization decisions about
     * user and possibly skips evaluation of this tag's body.
     * 
     * @throws IllegalStateException if 'canEditSample' is true and either
     *         'suppressRenderingOnly' is false or this tag is not nested in a
     *         {@code SampleContext}, or if 'canSeeSampleText' is true and
     *         either 'suppressRenderingOnly' is false or this tag is not nested
     *         in a {@code SampleTextContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onRegistrationPhaseBeforeBody(pageContext);

        /*
         * get the SuppressionContext if one exists if one exists, a reference
         * is needed so that its isTagsBodySuppressedThisPhase() return value
         * may be propagated
         */
        this.suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);

        // Get the UserInfo object from the session
        this.user = (UserInfo) super.pageContext.getSession().getAttribute(
                "userInfo");

        this.isAuthorized = true;

        // Call appropriate methods based on attributes and perform logical AND
        if (this.requireAuthentication) {
            this.isAuthorized &= (this.user != null);
        }
        if (this.canSeeLabSummary) {
            this.isAuthorized
                    &= AuthorizationCheckerBL.canSeeLabSummary(this.user);
        }
        if (this.canSubmitSamples) {
            this.isAuthorized
                    &= AuthorizationCheckerBL.canSubmitSamples(this.user);
        }
        if (this.canAdministerLabs) {
            this.isAuthorized
                    &= AuthorizationCheckerBL.canAdministerLabs(this.user);
        }
        if (this.canFilterResultsByOwnProvider) {
            this.isAuthorized
                    &= AuthorizationCheckerBL.canFilterResultsByOwnProvider(
                            this.user);
        }
        if (this.canSeeProviderListForLab) {
            this.isAuthorized &= (this.user != null)
                    && AuthorizationCheckerBL.canSeeProviderListForLab(
                            this.user, this.user.labId);
        }

        // Prepare for late checks
        if (this.canEditSample) {
            if (!this.suppressRenderingOnly) {
                throw new IllegalStateException();
            }
            this.sampleContext
                    = findRealAncestorWithClass(this, SampleContext.class);
            if (this.sampleContext == null) {
                throw new IllegalStateException();
            }
        }
        if (this.canSeeSampleText) {
            if (!this.suppressRenderingOnly) {
                throw new IllegalStateException();
            }
            this.sampleContext
                    = findRealAncestorWithClass(this, SampleContext.class);
            this.sampleTextContext = findRealAncestorWithClass(this,
                    SampleTextContext.class);
            if ((this.sampleTextContext == null) || (this.sampleContext == null)) {
                throw new IllegalStateException();
            }
        }

        /*
         * Possibly skip this tag's body, depending on our authorization
         * decision
         */
        return (this.suppressRenderingOnly || (this.isAuthorized ^ this.invert))
                ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version skips the body if the user is not
     * authorized and {@code suppressRenderingOnly} is {@code false}. When
     * {@code suppressRenderingOnly} is to {@code false}, authorization must be
     * finalized in the registration phase in order to allow custom tags within
     * the body of this tag.
     */
    @Override
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        super.onParsingPhaseBeforeBody(request);
        return (this.suppressRenderingOnly || (this.isAuthorized ^ this.invert))
                ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version skips the body if the user is not
     * authorized and {@code suppressRenderingOnly} is {@code false}. When
     * {@code suppressRenderingOnly} is to {@code false}, authorization must be
     * finalized in the registration phase in order to allow custom tags within
     * the body of this tag.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        super.onFetchingPhaseBeforeBody();
        return (this.suppressRenderingOnly || (this.isAuthorized ^ this.invert))
                ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version skips the body if the user is not
     * authorized and {@code suppressRenderingOnly} is {@code false}. When
     * {@code suppressRenderingOnly} is to {@code false}, authorization must be
     * finalized in the registration phase in order to allow custom tags within
     * the body of this tag.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        super.onProcessingPhaseBeforeBody(pageContext);
        return (this.suppressRenderingOnly || (this.isAuthorized ^ this.invert))
                ? EVAL_BODY_INCLUDE : SKIP_BODY;
    }

    /**
     * {@inheritDoc}; this version skips the body if the user is not authorized
     * and {@code suppressRenderingOnly} is {@code false}. When
     * {@code suppressRenderingOnly} is to {@code false}, authorization must be
     * finalized in the registration phase in order to allow custom tags within
     * the body of this tag.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);

        // Perform late checks
        if (this.canEditSample) {
            // guaranteed by onRegistrationPhaseBeforeBody()
            assert this.suppressRenderingOnly == true;
            this.isAuthorized &= AuthorizationCheckerBL.canEditSample(
                    this.user, this.sampleContext.getSampleInfo());
        }
        if (this.canSeeSampleText) {
            // guaranteed by onRegistrationPhaseBeforeBody()
            assert this.suppressRenderingOnly;
            this.isAuthorized &= AuthorizationCheckerBL.canSeeSampleText(
                    this.user, this.sampleContext.getSampleInfo(),
                    this.sampleTextContext.getSampleTextInfo());
        }

        return (this.isAuthorized ^ this.invert)
                ? EVAL_BODY_INCLUDE
                : (this.suppressRenderingOnly ? EVAL_BODY_BUFFERED : SKIP_BODY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AuthorizationChecker dc
                = (AuthorizationChecker) super.generateCopy(newId, map);
        
        dc.suppressionContext
                = (SuppressionContext) map.get(this.suppressionContext);
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        
        return dc;
    }
}
