/*
 * Reciprocal Net project
 * 
 * SampleChecker.java
 *
 * 26-Aug-2004: midurbin wrote first draft
 * 14-Sep-2004: midurbin added the property 'includeIfNoVisibleLtas'
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 10-Mar-2005: midurbin added the property 'includeIfRetracted'
 * 22-Mar-2005: midurbin added the property 'includeIfValueIsPresent'
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 24-Jun-2005: midurbin added 'includeIfActionIsValid', 'invert' and
 *              'includeIfSampleFieldContextProvidesValue' properties
 * 30-Jun-2005: midurbin added 'includeIfNewSample' property
 * 05-Jul-2005: midurbin added 'includeIfUnpublishedVersionOfPublicSample'
 *              property
 * 14-Jun-2006: jobollin made this class extend AbstractChecker and reformatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * This tag will suppress its body in all cases except when the indicated
 * condition(s) is satisfied. The JSP author indicates which condition(s) must
 * be satisfied by setting one or more of the various inclusion attributes; if
 * multiple conditions are enabled on the same tag instance then the body is
 * included if ANY of the conditions is satisfied. Otherwise, any output during
 * the {@code RENDERING_PHASE} is discarded, and this tag's
 * {@code SuppressionContext} implementation will indicate that nested tags
 * should not perform any response-altering operations. Inclusion condition
 * evaluation is performed during the {@code FETCHING_PHASE}.
 * </p><p>
 * Depending on the condition indicated for this {@code SampleChecker}, it may
 * have to be nested within a {@link SampleContext}, {@link SampleFieldContext}
 * {@link WapPage}; these requirements are documented with the associated
 * inclusion conditions.
 * </p>
 */
public class SampleChecker extends AbstractChecker implements
        SuppressionContext {

    /**
     * The most immediate {@code SampleContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()} and used in many cases by
     * {@code onFetchingPhaseBeforeBody()} to determine whether the criteria for
     * body inclusion have been met.
     */
    private SampleContext sampleContext;

    /**
     * The most immediate {@code SampleFieldContext}; determined by
     * {@code onRegistrationPhaseBeforeBody()} if the
     * 'includeIfSampleFieldContextProvidesValue' property is set, and used by
     * {@code onFetchingPhaseBeforeBody()} in that case to determine whether
     * that body inclusion criterion has been met.
     */
    private SampleFieldContext sampleFieldContext;

    /**
     * The {@code WapPage} tag in which this tag is nested. This is not required
     * for all conditions, but when it is required it is determined by
     * {@code onRegistrationPhaseBeforeBody()} and used in some cases by
     * {@code onFetchingPhaseBeforeBody()} to determine whether the criteria for
     * body inclusion have been met.
     */
    private WapPage wapPage;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normaly if the sample defined by the most immediate {@code SampleContext}
     * is not held at the local site. Otherwise the body will be suppressed.
     * This attribute should only be set if no other condition attributes are
     * set.
     */
    private boolean includeIfNotHeldLocally;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the sample defined by the most immediate
     * {@code SampleContext} contains any annotations of the type
     * {@code SampleTextInfo.INELIGIBLE_FOR_RELEASE}. This attribute should
     * only be set if no other condition attributes are set.
     */
    private boolean includeIfIneligibleForRelease;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if there are no local tracking attributes defined for the lab
     * provided by the {@code WapPage}'s {@code LabContext} that are visible
     * (checked via a call to {@code SampleWorkflowBL.isLtaVisibleForAction()})
     * for the action code provided to the {@code WapPage}. When this attribute
     * is true, this tag must be nested within a {@code WapPage} tag. This
     * attribute should only be set if no other condition attributes are set.
     */
    private boolean includeIfNoVisibleLtas;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the sample defined by the most immediate
     * {@code SampleContext} is retracted (as determined by
     * {@code SampleWorkflowBL.isRetractedStatusCode()}). Otherwise the body
     * will be suppressed.
     */
    private boolean includeIfRetracted;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the field referenced by the fieldCode to which this property
     * has been set is set to a non-null value. This property may be set to
     * {@code SampleTextBL.INVALID_TYPE} to indicate that this particular
     * inclusion property is not to be used. This may only be set to another
     * value if no other inclusion attributes are set.
     */
    private int includeIfValueIsPresent;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the {@code SampleFieldContext} in which this tag must be
     * nested supplied a {@code SampleFieldRecord} whose value is non-null.
     */
    private boolean includeIfSampleFieldContextProvidesValue;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the action referenced by the action code to which this
     * property is set is a valid action to be performed on the sample. This
     * property may be set to {@code SampleWorkflowBL.INVALID_ACTION} to
     * indicate that this particular inclusion property is not to be used. This
     * may only be set to another value if no other inclusion attributes are
     * set.
     */
    private int includeIfActionIsValid;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if sample provided by the {@code SampleContext} is a new sample
     * that has not yet been stored to the database. This is determined by
     * checking whether the sampleId is {@code INVALID_SAMPLE_ID} or not. This
     * may only be set to true if no other inclusion attributes are set.
     */
    private boolean includeIfNewSample;

    /**
     * An optional attribute that indicates that the body should be evaluated
     * normally if the sample-version returned by the {@code SampleContext} is
     * not public but the most current version of the same sample is public.
     * This may only be set to true when no other inclusion attributes are set
     * to true.
     */
    private boolean includeIfUnpublishedVersionOfPublicSample;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.sampleFieldContext = null;
        this.wapPage = null;
        this.includeIfNotHeldLocally = false;
        this.includeIfIneligibleForRelease = false;
        this.includeIfNoVisibleLtas = false;
        this.includeIfRetracted = false;
        this.includeIfValueIsPresent = SampleTextBL.INVALID_TYPE;
        this.includeIfActionIsValid = SampleWorkflowBL.INVALID_ACTION;
        this.includeIfSampleFieldContextProvidesValue = false;
        this.includeIfNewSample = false;
        this.includeIfUnpublishedVersionOfPublicSample = false;
    }

    /**
     * @return a boolean that indicates that the body of this tag should be
     *         included rather than suppressed if the sample is not held at the
     *         local site.
     */
    public boolean getIncludeIfNotHeldLocally() {
        return this.includeIfNotHeldLocally;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the sample is not held at the local
     *        site.
     */
    public void setIncludeIfNotHeldLocally(boolean include) {
        this.includeIfNotHeldLocally = include;
    }

    /**
     * @return a boolean that indicates that the body of this tag should be
     *         included rather than suppressed if the sample has any
     *         {@code SampleTextInfo.INELIGIBLE_FOR_RELASE} annotations.
     */
    public boolean getIncludeIfIneligibleForRelease() {
        return this.includeIfIneligibleForRelease;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the sample has any
     *        {@code SampleTextInfo.INELIGIBLE_FOR_RELASE} annotations.
     */
    public void setIncludeIfIneligibleForRelease(boolean include) {
        this.includeIfIneligibleForRelease = include;
    }

    /**
     * @return a boolean that indicates whether the body of this tag should be
     *         included rather than suppressed if there are no locally defined
     *         attributes that are visible for the most immediate
     *         {@code WapPage} tag.
     */
    public boolean getIncludeIfNoVisibleLtas() {
        return this.includeIfNoVisibleLtas;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if there are no locally defined attributes
     *        that are visible for the most immediate {@code WapPage} tag.
     */
    public void setIncludeIfNoVisibleLtas(boolean include) {
        this.includeIfNoVisibleLtas = include;
    }

    /**
     * @return a boolean that indicates that the body of this tag should be
     *         included rather than suppressed if the sample is retracted.
     */
    public boolean getIncludeIfRetracted() {
        return this.includeIfRetracted;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the sample is retracted.
     */
    public void setIncludeIfRetracted(boolean include) {
        this.includeIfRetracted = include;
    }

    /**
     * @return the int sample fieldCode representing the field that must be set
     *         for this tag's body to be evaluated or
     *         {@code SampleTextBL.INVALID_TYPE} if this inclusion property is
     *         not to be considered
     */
    public int getIncludeIfValueIsPresent() {
        return this.includeIfValueIsPresent;
    }

    /**
     * @param fieldCode the int sample fieldCode representing the field that
     *        must be set for this tag's body to be evaluated or
     *        {@code SampleTextBL.INVALID_TYPE} if this inclusion property is
     *        not to be considered
     */
    public void setIncludeIfValueIsPresent(int fieldCode) {
        this.includeIfValueIsPresent = fieldCode;
    }

    /**
     * @return the int action code represeting the action that must be valid for
     *         this tag's body to be evaluated or
     *         {@code SampleWorkflowBL.INVALID_ACTION} if this inclusion
     *         property is not to be considered.
     */
    public int getIncludeIfActionIsValid() {
        return this.includeIfActionIsValid;
    }

    /**
     * @param actionCode the int action code representing the action that must
     *        be valid for this tag's body to be evaluated or
     *        {@code SampleWorkflowBL.INVALID_ACTION} if this inclusion property
     *        is not to be considered
     */
    public void setIncludeIfActionIsValid(int actionCode) {
        this.includeIfActionIsValid = actionCode;
    }

    /**
     * @return a boolean indicating whether the body should be included rather
     *         than suppressed if the {@code SampleFieldRecord} provided by the
     *         {@code SampleFieldContext} has a non-null value
     */
    public boolean getIncludeIfSampleFieldContextProvidesValue() {
        return this.includeIfSampleFieldContextProvidesValue;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the {@code SampleFieldRecord} provided
     *        by the {@code SampleFieldContext} has a non-null value
     */
    public void setIncludeIfSampleFieldContextProvidesValue(boolean include) {
        this.includeIfSampleFieldContextProvidesValue = include;
    }

    /**
     * @return a boolean that indicates that the body of this tag should be
     *         included rather than suppressed if the sample is new.
     */
    public boolean getIncludeIfNewSample() {
        return this.includeIfNewSample;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the sample is new.
     */
    public void setIncludeIfNewSample(boolean include) {
        this.includeIfNewSample = include;
    }

    /**
     * @return a boolean indicating whether the body should be included rather
     *         than suppressed if the sample is in a non-public state but it is
     *         known that the most recent state is public.
     */
    public boolean getIncludeIfUnpublishedVersionOfPublicSample() {
        return this.includeIfUnpublishedVersionOfPublicSample;
    }

    /**
     * @param include indicates that the body of this tag should be included
     *        rather than suppressed if the current sample version is not in a
     *        public state but the most recent status is a public status.
     */
    public void setIncludeIfUnpublishedVersionOfPublicSample(boolean include) {
        this.includeIfUnpublishedVersionOfPublicSample = include;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost surrounding
     * {@code SampleContext} and {@code SuppressionContext}, as needed to
     * support the configured includion condition(s)
     * 
     * @throws IllegalStateException if this tag is not nested within the
     *         required context
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.includeIfNoVisibleLtas) {
            
            /*
             * find the WapPage -- needed for the includeIfNoVisibleLtas
             * inclusion property
             */
            try {
                this.wapPage = (WapPage) getPage();
            } catch (ClassCastException cce) {
                throw new IllegalStateException("Host page is not a WAP page",
                        cce);
            }
        }
        if (this.includeIfSampleFieldContextProvidesValue) {
            
            /*
             * find the SampleFieldContext -- needed for the
             * includeIfSampleFieldContextProvidesValue inclusion property
             */
            this.sampleFieldContext = findRealAncestorWithClass(this,
                    SampleFieldContext.class);
            if (this.sampleFieldContext == null) {
                // Can't find the required context from ancestry
                throw new IllegalStateException("No sample field context");
            }
        }
        
        /*
         * find the SampleContext -- needed for all other inclusion properties,
         * and necessarilly available if either of the other two contexts was
         * available
         */
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            // Can't find the required context from ancestry
            throw new IllegalStateException("No sample context");
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether this tag should include
     * its body evaluation or whether it should suppress it based on the
     * specified condition(s).
     * 
     * @throws IllegalStateException if {@code includeIfNoVisibleLtas} is true
     *         but {@code WapPage.getLabInfo()} returns null.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo sample;

        if (this.includeIfNoVisibleLtas) {
            
            /*
             * onRegistrationPhaseBeforeBody() already ensured that WapPage is
             * not null
             */
            assert this.wapPage != null;

            LocalTrackingConfig ltc;
            int actionCode = this.wapPage.getWorkflowActionCode();
            LabInfo labInfo = this.wapPage.getLabInfo();
            boolean ltaIsVisible;

            if (labInfo == null) {
                throw new IllegalStateException();
            }

            // get ltc
            try {
                ltc = RequestCache.getLTC(this.pageContext.getRequest(),
                        labInfo.id);
                if (ltc == null) {
                    // cache miss
                    CoreConnector cc = CoreConnector.extract(
                            this.pageContext.getServletContext());
                    
                    ltc = cc.getSiteManager().getLocalTrackingConfig(labInfo.id);
                    RequestCache.putLTC(this.pageContext.getRequest(), ltc);
                }
            } catch (RemoteException ex) {
                // FIXME: Oughtn't this be reported to core, as it is elsewhere?
                throw new JspException(ex);
            } catch (WrongSiteException ex) {
                throw new JspException(ex);
            }

            ltaIsVisible = false;
            for (LocalTrackingConfig.Field field : ltc.fields) {
                if (SampleWorkflowBL.isLtaVisibleDuringAction(field, actionCode)) {
                    // at least one visible field exists
                    ltaIsVisible = true;
                    break;
                }
            }
            if (!ltaIsVisible) {
                inclusionConditionMet = true;
                return rc;
            }
        }

        if (this.includeIfSampleFieldContextProvidesValue) {
            /*
             * onRegistrationPhaseBeforeBody() already ensured that
             * SampleFieldContext is not null
             */
            assert this.sampleFieldContext != null;

            if ((this.sampleFieldContext.getSampleField() != null)
                    && (this.sampleFieldContext.getSampleField().getValue()
                            != null)) {
                inclusionConditionMet = true;
                return rc;
            }
        }

        sample = this.sampleContext.getSampleInfo();

        if (sample == null) {
            /*
             * the SampleContext currently provides no sample this occurs in
             * some cases when the SampleContext determines its sample based on
             * user input and is not known until after a form has been posted.
             * In such cases it is appropriate to do nothing, leaving
             * 'inclusionConditionMet' as true
             */
            inclusionConditionMet = true;
            return rc;
        } else {
            if (this.includeIfNotHeldLocally) {
                CoreConnector cc = CoreConnector.extract(
                        super.pageContext.getServletContext());
                
                try {
                    if (cc.getRepositoryManager().getLocalHoldingLevel(
                            this.sampleContext.getSampleInfo().id)
                            < RepositoryHoldingInfo.BASIC_DATA) {
                        inclusionConditionMet = true;
                        return rc;
                    }
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (RemoteException ex) {
                    throw new JspException(ex);
                }
            }
            
            if (this.includeIfIneligibleForRelease) {
                for (SampleTextInfo text : sample.annotationInfo) {
                    if (text.type == SampleTextBL.INELIGIBLE_FOR_RELEASE) {
                        // condition for inclusion was met
                        inclusionConditionMet = true;
                        return rc;
                    }
                }
            }
            
            if (this.includeIfRetracted) {
                if (SampleWorkflowBL.isRetractedStatusCode(
                        this.sampleContext.getSampleInfo().status)) {
                    inclusionConditionMet = true;
                    return rc;
                }
            }
            
            if (this.includeIfValueIsPresent != SampleTextBL.INVALID_TYPE) {
                if (this.sampleContext.getSampleInfo().extractValue(
                        this.includeIfValueIsPresent) != null) {
                    inclusionConditionMet = true;
                    return rc;
                }
            }
            
            if (this.includeIfActionIsValid != SampleWorkflowBL.INVALID_ACTION) {
                if (SampleWorkflowBL.isActionValid(
                        this.sampleContext.getSampleInfo().status,
                        this.includeIfActionIsValid)) {
                    inclusionConditionMet = true;
                    return rc;
                }
            }
            
            if (this.includeIfNewSample) {
                if (this.sampleContext.getSampleInfo().id
                        == SampleInfo.INVALID_SAMPLE_ID) {
                    inclusionConditionMet = true;
                    return rc;
                }
            }
            
            if (this.includeIfUnpublishedVersionOfPublicSample) {
                if (!this.sampleContext.getSampleInfo().isPublic()
                        && SampleWorkflowBL.isPublicStatusCode(
                                this.sampleContext.getSampleInfo().mostRecentStatus)) {
                    inclusionConditionMet = true;
                    return rc;
                }
            }
        }
        
        // If control reaches here then no inclusion condition was met
        
        inclusionConditionMet = false;
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleChecker dc = (SampleChecker) super.generateCopy(newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.sampleFieldContext
                = (SampleFieldContext) map.get(this.sampleFieldContext);

        return dc;
    }
}
