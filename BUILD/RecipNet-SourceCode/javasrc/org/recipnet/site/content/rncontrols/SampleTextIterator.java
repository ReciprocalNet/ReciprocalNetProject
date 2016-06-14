/*
 * Reciprocal Net project
 * 
 * SampleTextIterator.java
 *
 * 28-Jul-2004: midurbin wrote the first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 08-Aug-2004: cwestnea made changes throughout to use SampleWorkflowBL
 * 30-Aug-2004: midurbin added getTextType()
 * 05-Oct-2004: midurbin fixed bug #1395 in onProcessingPhaseAfterBody()
 * 15-Oct-2004: midurbin added 'restrictToAnnotations' and
 *              'restrictToAttributes' properties
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 13-Jan-2005: midurbin added 'sortByTextTypeName' property and supporting
 *              code in onFetchingPhaseBeforeBody()
 * 20-Jan-2005: midurbin updated class to cause new SampleTextInfo objects
 *              to replace existing ones in the SampleInfo when modified rather
 *              than just changing their values
 * 28-Jan-2005: midurbin added an ErrorSupplier implementation
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 01-Apr-2005: midurbin refactored most code into AbstractSampleTextIterator
 * 04-May-2005: midurbin added 'restrictToTextTypesOtherThan' property
 * 17-Mar-2006: jobollin updated this class to accommodate changes in
 *              SampleTextBL
 * 05-Jul-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A custom tag that evaluates its body for each {@code SampleTextInfo} on the
 * sample (supplied by a surrounding {@code SampleContext}) that matches the
 * given filter.
 * </p><p>
 * This tag must be nested in a {@code SampleContext} and a {@code LabContext}
 * for the lab from which the {@code SampleContext}'s sample originated. A
 * {@code SampleTextContext} is provided to all nested tags.
 * </p><p>
 * This tag should have the superclass' sorting properties exposed as attributes
 * in the TLD file.
 * </p>
 */
public class SampleTextIterator extends AbstractSampleTextIterator {

    /**
     * The most immediate {@code LabContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()} and used to fetch the appropriate
     * {@code LocalTrackingConfig} by {@code onFetchingPhaseBeforeBody()}.
     */
    private LabContext labContext;

    /**
     * An optional property that restricts the {@code SampleTextInfo} objects
     * provided by this Tag to only the given text type. By default no
     * restriction occurs and all text types will be included.
     */
    private int restrictByTextType;

    /**
     * An optional property that restricts the {@code SampleTextInfo} objects
     * provided by this Tag to any text type other than the one indicated by
     * this property.
     */
    private int restrictToTextTypesOtherThan;

    /**
     * An optional property that restricts the {@code SampleTextInfo} objects
     * provided by this tag's {@code SampleTextContext} implementation to those
     * that could be modified by the workflow action indicated by the supplied
     * workflow action code. This variable must be set to a valid workflow
     * action code as defined on {@code SampleWorkflowBL}. By default, no
     * restriction occurs and all statically defined text types (ie. not LTAs)
     * will be included.
     */
    private int restrictByWorkflowAction;

    /**
     * An optional property that restricts the {@code SampleTextInfo} objects
     * provided by this tag's {@code SampleTextContext} implementation to
     * annotations. This property defaults to false and should not be set to
     * true if 'restrictToAttributes' is set to true.
     */
    private boolean restrictToAnnotations;

    /**
     * An optional property that restricts the {@code SampleTextInfo} objects
     * provided by this tag's {@code SampleTextContext} implementation to
     * attributes. This property defaults to false and should not be set to true
     * if 'restrictToAnnotations' is set to true.
     */
    private boolean restrictToAttributes;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.restrictByTextType = SampleTextInfo.INVALID_TYPE;
        this.restrictToTextTypesOtherThan = SampleTextInfo.INVALID_TYPE;
        this.restrictByWorkflowAction = SampleWorkflowBL.INVALID_ACTION;
        this.restrictToAnnotations = false;
        this.restrictToAttributes = false;
    }

    /**
     * @param textType restricts the {@code SampleTextInfo} objects provided by
     *        this context to just those that have this text type.
     * @throws IllegalArgumentException if the texttype is not a valid attribute
     *         or annotation text type code.
     */
    public void setRestrictByTextType(int textType) {
        if (!SampleTextBL.isAttribute(textType)
                && !SampleTextBL.isAnnotation(textType)) {
            throw new IllegalArgumentException();
        }
        this.restrictByTextType = textType;
    }

    /**
     * @param textType restricts the {@code SampleTextInfo} objects provided by
     *        this context to exclude those that have this text type.
     * @throws IllegalArgumentException if the texttype is not a valid attribute
     *         or annotation text type code.
     */
    public void setRestrictToTextTypesOtherThan(int textType) {
        if (!SampleTextBL.isAttribute(textType)
                && !SampleTextBL.isAnnotation(textType)) {
            throw new IllegalArgumentException();
        }
        this.restrictToTextTypesOtherThan = textType;
    }

    /**
     * @param actionCode restricts the {@code SampleTextInfo} objects provided
     *        by this context to just those that are associated with the given
     *        workflow action code.
     * @throws IllegalArgumentException if the actionCode does not refer to a
     *         valid workflow action code.
     */
    public void setRestrictByWorkflowAction(int actionCode) {
        if (!SampleWorkflowBL.isValidActionCode(actionCode)) {
            throw new IllegalArgumentException();
        }
        this.restrictByWorkflowAction = actionCode;
    }

    /**
     * @param restrict indicates whether the {@code SampleTextInfo}'s returned
     *        by this {@code SampleTextContext} implementation may only contain
     *        those that are annotations.
     * @throws IllegalArgumentException if 'restrictToAttributes' is true and
     *         this method is attempting to set 'restrictToAnnotations' to true
     */
    public void setRestrictToAnnotations(boolean restrict) {
        if (restrict && this.restrictToAttributes) {
            throw new IllegalArgumentException();
        }
        this.restrictToAnnotations = restrict;
    }

    /**
     * @param restrict indicates whether the {@code SampleTextInfo}'s returned
     *        by this {@code SampleTextContext} implementation may only contain
     *        those that are attributes.
     * @throws IllegalArgumentException if 'restrictToAttributes' is true and
     *         this method is attempting to set 'restrictToAnnotations' to true
     */
    public void setRestrictToAttributes(boolean restrict) {
        if (restrict && this.restrictToAnnotations) {
            throw new IllegalArgumentException();
        }
        this.restrictToAttributes = restrict;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets the
     * reference to {@code LabContext} that will be needed during the
     * {@code FETCHING_PHASE} after delegating back to the superclass.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get LabContext
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code AbstractSampleTextIterator}; the current implementation
     * returns a {@code Collection} of all the {@code SampleTextInfo} objects
     * that meet the criteria specified by the 'restrictTo*" properties of this
     * class.
     * 
     * @throws JspException to wrap any {@code RemoteException} or
     *         {@code WrongSiteException} thrown while fetching the
     *         {@code LocalTrackingConfig} in the case that
     *         'restrictByWorkflowAction' is set.
     */
    @Override
    public Collection<SampleTextInfo> getFilteredTextInfoCollection(
            SampleInfo sampleInfo) throws JspException {
        Collection<SampleTextInfo> textInfos = new ArrayList<SampleTextInfo>();
        if (sampleInfo == null) {
            // no SampleInfo was provided, therefore no SampleTextInfos will
            // be provided and this iterator will not evaluate its body.
            return textInfos;
        }

        LocalTrackingConfig ltc = null;
        // get ltc if needed
        if (this.restrictByWorkflowAction != SampleWorkflowBL.INVALID_ACTION) {
            CoreConnector cc = CoreConnector.extract(
                    this.pageContext.getServletContext());
            try {
                LabInfo labInfo = labContext.getLabInfo();
                if ((labInfo == null) || (sampleInfo.labId != labInfo.id)) {
                    throw new IllegalStateException();
                }
                ltc = RequestCache.getLTC(this.pageContext.getRequest(),
                        labInfo.id);
                if (ltc == null) {
                    // cache miss
                    ltc = cc.getSiteManager().getLocalTrackingConfig(
                            labInfo.id);
                    RequestCache.putLTC(this.pageContext.getRequest(), ltc);
                }
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (WrongSiteException ex) {
                throw new JspException(ex);
            }
        }

        // create a Collection of eligible fields types that can be used to
        // filter existing annotations and attributes
        Collection<Integer> eligibleFields;

        if (this.restrictByWorkflowAction != SampleWorkflowBL.INVALID_ACTION) {
            eligibleFields = SampleWorkflowBL.getEligibleFieldCodes(
                    this.restrictByWorkflowAction, ltc);
        } else {
            eligibleFields = new HashSet<Integer>(
                    SampleTextBL.getAllTextTypes());
        }
        if (this.restrictToTextTypesOtherThan != SampleTextInfo.INVALID_TYPE) {
            eligibleFields.remove(this.restrictToTextTypesOtherThan);
        }
        if (this.restrictByTextType != SampleTextInfo.INVALID_TYPE) {
            if (eligibleFields.contains(this.restrictByTextType)) {
                // restrictByType eliminated all other types
                eligibleFields.clear();
                eligibleFields.add(this.restrictByTextType);
            } else {
                eligibleFields.clear();
                // an empty collection is acceptable because there is no
                // intersection between the two restrictions
            }
        }

        // add all attributes that match the filter if attributes are allowed
        if (!this.restrictToAnnotations) {
            for (SampleTextInfo sti : sampleInfo.attributeInfo) {
                if (eligibleFields.contains(sti.type)) {
                    textInfos.add(sti);
                }
            }
        }

        // add all annotations that match the filter if annotations are allowed
        if (!this.restrictToAttributes) {
            for (SampleTextInfo sti : sampleInfo.annotationInfo) {
                if (eligibleFields.contains(sti.type)) {
                    textInfos.add(sti);
                }
            }
        }
        return textInfos;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleTextIterator dc
                = (SampleTextIterator) super.generateCopy(newId, map);
        dc.labContext = (LabContext) map.get(this.labContext);
        return dc;
    }
}
