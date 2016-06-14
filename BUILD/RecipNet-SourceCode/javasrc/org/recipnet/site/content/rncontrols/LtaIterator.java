/*
 * Reciprocal Net project
 * 
 * LtaIterator.java
 * 
 * 23-Jul-2004: midurbin wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 08-Aug-2004: cwestnea moved getVisibilityFlagForActionCode() to 
 *              org.recipnet.site.shared.bl.SampleWorkflowBL and modified calls
 *              to it to reflect that.
 * 30-Aug-2004: midurbin added getTextType()
 * 28-Sep-2004: midurbin updated onFetchingPhaseBeforeBody() to use the new
 *              SampleWorkflowBL method, isVisibleDuringAction()
 * 20-Jan-2005: midurbin updated class to cause new SampleTextInfo objects
 *              to replace existing ones in the SampleInfo when modified rather
 *              than just changing their values
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 30-Jun-2005: midurbin fixed onFetchingPhase() to gracefully handle a
 *              SampleContext that does not provide a sample
 * 28-Oct-2005: jobollin updated onProcessingPhaseAfterBody() to accommodate
 *              SampleInfo's switch to use of typed Lists instead of raw ones;
 *              removed unused imports; added type parameters to the internal
 *              filteredLtaMap and filteredLtaIterator variables; switched
 *              to Java 5 enhanced for loops in several places; and removed
 *              some unnecessary casts
 * 15-Jun-2006: jobollin reformatted the source  
 * 11-Jan-2008: ekoperda added new modes 'considerShowSampleVisibility' and 
 *              'skipBlankValues'
 */

package org.recipnet.site.content.rncontrols;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A custom tag that evaluates its body multiple times to provide a
 * {@code SampleTextContext} for each LocalTrackingAttribute that should be
 * exposed.  A <code>SampleTextInfo</code> is made available to nested tags for
 * each local tracking field that should be visibile.  In the case where a
 * field value already exists in the <code>SampleInfo</code>, that field is
 * exposed.  Otherwise, a new sample <code>SampleTextInfo</code> is created
 * that may be inserted into the <code>SampleInfo</code> during
 * <code>onProcessingPhaseAfterBody()</code> if such a value is set by the 
 * user. <p>
 *
 * The sense of which LTA's should be accessible via this iterator is
 * determined in one of two ways.
 *    1. If this tag is nexted within a <code>WapPage</code>, the pertinent
 *       workflow action underway is fetched from the WapPage.  The workflow
 *       action code then is presented to <code>SampleWorkflowBL</code> for a
 *       decision as to whether the LTA should be exposed or not.
 *    2. If considerShowSampleVisibility is true, then the currently logged-on
 *       user record is considered and evaluated against the
 *       LocalTrackingConfig modes named VISIBLE_ON_SHOWSAMPLE_TO_LAB_USERS,
 *       VISIBLE_ON_SHOWSAMPLE_TO_PROVIDER_USERS, and 
 *       VISIBLE_ON_SHOWSAMPLE_TO_UNAUTHENTICATED_USERS.
 * Additionally, optionally, LTA's whose values are blank may be skipped by
 * this iterator by setting the <code>skipBlankValues</code> flag.
 */
public class LtaIterator extends HtmlPageIterator
        implements SampleTextContext {
    /**
     * The {@code WapPage} tag that surrounds this tag. This is set by
     * {@code onRegistrationPhaseBeforeBody()} and used during by
     * {@code onFetchingPhaseBeforeBody()} to determine which LTAs should be
     * visible on this page.
     */
    private WapPage wapPage;

    /**
     * The most immediate {@code SampleContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * The most immediate {@code LabContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()} and used to fetch the
     * appropriate {@code LocalTrackingConfig} by 
     * {@code onFetchingPhaseBeforeBody()}.
     */
    private LabContext labContext;

    /**
     * Configuration attribute set by Tomcat.  If this flag is true, then
     * LTA visibility decisions are made by evaluating the currently logged-on
     * UserInfo against the various VISIBLE_ON_SHOWSAMPLE... flags on
     * LocalTrackingConfig.  If false, then LTA decisions are made in
     * conjunction with WapPage and SampleWorkflowBL.
     */
    private boolean considerShowSampleVisibility;

    /**
     * Configuration attribute set by Tomcat.  If true, LTA's with blank values
     * are skipped by this iterator.  If false, then LTA's with blank values
     * are exposed by the iterator.  In those cases getSampleTextInfo() 
     * returns an object whose value is null.
     */
    private boolean skipBlankValues;

    /**
     * A {@code SortedMap} whose keys correspond to {@code SampleTextInfo}
     * objects for each type of LTA that should be displayed on the current
     * {@code WapPage} and whose values are the original value ({@code String})
     * to which the LTA was set if any value exists. They keys are exposed
     * though this class' {@code SampleTextContext} implementation and the
     * values are used to determine if a particular LTA was changed. This map
     * is populated by {@code onFetchingPhaseBeforeBody()} and is ordered by
     * texttype.
     */
    private SortedMap<SampleAttributeInfo, String> filteredLtaMap;

    /**
     * An {@code Iterator} over the keys of {@code filteredLtaMap}, initialized
     * by {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<SampleAttributeInfo> filteredLtaIterator;

    /**
     * The {@code SampleTextInfo} that will be provided to implement
     * {@code SampleTextContext} during this body evaluation. Set by
     * {@code onIterationBeforeBody()} to a key value from
     * {@code filteredLtaMap}.
     */
    private SampleTextInfo currentLta;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.wapPage = null;
        this.sampleContext = null;
        this.labContext = null;
	this.considerShowSampleVisibility = false;
	this.skipBlankValues = false;
        this.filteredLtaMap = null;
        this.filteredLtaIterator = null;
        this.currentLta = null;
    }

    /** Simple getter method */
    public boolean getConsiderShowSampleVisibility() {
	return this.considerShowSampleVisibility;
    }

    /** Simple setter method */
    public void setConsiderShowSampleVisibility(
            boolean considerShowSampleVisibility) {
	this.considerShowSampleVisibility = considerShowSampleVisibility;
    }

    /** Simple getter method */
    public boolean getSkipBlankValues() {
	return this.skipBlankValues;
    }

    /** Simple setter method */
    public void setSkipBlankValues(boolean skipBlankValues) {
	this.skipBlankValues = skipBlankValues;
    }

    /**
     * {@inheritDoc}; this version gets the reference to {@code WapPage},
     * {@code SampleContext} and {@code LabContext} that will be needed during
     * the {@code FETCHING_PHASE}.
     * 
     * @param pageContext the current PageContext
     * @throws JspException if an exception is encountered during evaluation
     * @throws IllegalStateException if this tag is not nested within a
     *         required context or tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get WapPage
	HtmlPage parentPage = getPage();
	if (parentPage instanceof WapPage) {
	    this.wapPage = (WapPage) parentPage;
	}

        // get SampleContext
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        // get LabContext
        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }
        
	// Verify that we have either a WapPage or a ShowSample mode to go on.
	if (this.wapPage == null && !this.considerShowSampleVisibility) {
	    throw new IllegalStateException();
	}

        return rc;
    }

    /**
     * {@inheritDoc}; this version populates {@code filteredLtaMap} with
     * {@code SampleTextInfo} objects for each local tracking attribute that
     * should be exposed on this {@code WapPage}.
     * 
     * @throws JspException with the root cause of a {@code RemoteException} or
     *         {@code WrongSiteException} in the event that such an exception
     *         is encountered while fetching the {@code LocalTrackingConfig}
     *         object.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        // get SampleInfo
        SampleInfo sampleInfo = this.sampleContext.getSampleInfo();

        // get LabInfo
        LabInfo labInfo = this.labContext.getLabInfo();

        // get ltc
        LocalTrackingConfig ltc = null;
        try {
            ltc = RequestCache.getLTC(this.pageContext.getRequest(),
                    labInfo.id);
            if (ltc == null) { // cache miss
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());

                ltc = cc.getSiteManager().getLocalTrackingConfig(labInfo.id);
                RequestCache.putLTC(this.pageContext.getRequest(), ltc);
            }
        } catch (RemoteException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }
	assert ltc != null;

	// Decide which Localtracking text types should be exposed by this
	// iterator.
	Collection<Integer> visibleTextTypes;
	if (this.wapPage != null) {
	    visibleTextTypes 
                    = this.decideTextTypesVisibleOnWap(this.wapPage, ltc);
	} else {
	    visibleTextTypes = this.decideTextTypesVisibleOnShowSample(
                    (UserInfo) super.pageContext.getSession().getAttribute(
                    "userInfo"), ltc);
	}

        /*
	 * Fetch LTA values for all the text types to be exposed by our
	 * iterator.  We may need these values again during PROCESSING_PHASE.
	 */
        this.filteredLtaMap = new TreeMap<SampleAttributeInfo, String>();
	for (Integer textType : visibleTextTypes) {
	    /*
	     * Find the first LTA of this type in the SampleInfo and copy
	     * it to the filteredLtaMap if a sample is available
	     */
	    SampleAttributeInfo firstMatchingLta = null;
	    if (sampleInfo != null) {
		for (SampleAttributeInfo info : sampleInfo.attributeInfo) {
		    if (info.type == textType) {
			firstMatchingLta = info;
			break;
		    }
		}
	    }
	    if (firstMatchingLta == null && !this.skipBlankValues) {
		/*
		 * no matching lta was found, create a new one with no
		 * value that will serve as a placeholder. After the
		 * processing phase when the map's keys are compared to its
		 * values to determine if changes were made, any value
		 * entered will be considered a change and trigger
		 * insertion into the SampleInfo.attributes collection.
		 */
		firstMatchingLta = new SampleAttributeInfo(textType, null);
	    }
	    if (firstMatchingLta != null) {
		this.filteredLtaMap.put(firstMatchingLta, 
                        firstMatchingLta.value);
	    }
	}

        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the context sample to reflect any
     * LTA changes that were applied, removing any LTAs whose values were
     * cleared, adding LTAs where the user provided new local tracking values,
     * and replacing existing LTAs with new ones where the original LTA values
     * were modified.
     * 
     * @param pageContext the current PageContext
     * @throws JspException if an exception is encountered during evaluation
     * @throws IllegalStateException if the number of LTAs in
     *         {@code filteredLtaMap} is not equal to the number posted.
     *         (should never occur unless the post values were corrupted or the
     *         LTC for the lab has changed)
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        if (this.filteredLtaMap.size() != getPostedIterationCount()) {
            SuppressionContext suppression = findRealAncestorWithClass(this,
                    SuppressionContext.class);

            if ((suppression == null)
                    || !suppression.isTagsBodySuppressedThisPhase()) {
                throw new IllegalStateException();
            } else {
                return rc;
            }
        }

        // get sampleInfo
        SampleInfo sampleInfo = this.sampleContext.getSampleInfo();

        for (Map.Entry<SampleAttributeInfo, String> entry
                : this.filteredLtaMap.entrySet()) {
            SampleAttributeInfo attr = entry.getKey();
            String oldValue = entry.getValue();

            if (attr.value == null) {
                // delete the cleared LTA
                sampleInfo.attributeInfo.remove(attr);
            } else if (oldValue == null) {
                // add the new LTA
                sampleInfo.attributeInfo.add(attr);
            } else if (!attr.value.equals(oldValue)) {
                // replace the existing LTA that was modified
                sampleInfo.attributeInfo.remove(attr);
                sampleInfo.attributeInfo.add(
                        new SampleAttributeInfo(attr.type, attr.value));
            }
        }

        return rc;
    }

    /**
     * Implements {@code SampleTextContext}.
     * 
     * @throws IllegalStateException if sampleTextInfo is null because it is
     *         not yet the {@code FETCHING_PHASE}.
     */
    public SampleTextInfo getSampleTextInfo() {
        if (this.currentLta == null) {
            throw new IllegalStateException();
        }
        return this.currentLta;
    }

    /**
     * Implements {@code SampleTextContext}. This method will return
     * {@code SampleTextInfo.INVALID_TEXT_TYPE} until the
     * {@code FETCHING_PHASE} when it will return the text type of the
     * {@code SampleTextInfo} returned by {@code getSampleTextInfo()}.
     */
    public int getTextType() {
        if (this.currentLta == null) {
            return SampleTextInfo.INVALID_TYPE;
        }
        return this.currentLta.type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.filteredLtaMap != null) {
            this.filteredLtaIterator = filteredLtaMap.keySet().iterator();
        }
    }

    /**
     * {@inheritDoc}; this version causes the body to be evaluated once for
     * each local tracking attribute type that should be displayed on the
     * current {@code WapPage}.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.filteredLtaMap == null) {
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
        if (this.filteredLtaIterator.hasNext()) {
            this.currentLta = filteredLtaIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        LtaIterator dc = (LtaIterator) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        if (this.filteredLtaMap != null) {
            dc.filteredLtaMap = new TreeMap<SampleAttributeInfo, String>(
                    this.filteredLtaMap);
        }
        
        return dc;
    }

    /** Helper method. */
    private Collection<Integer> decideTextTypesVisibleOnWap(WapPage wapPage,
	    LocalTrackingConfig ltc) {
	int actionCode = wapPage.getWorkflowActionCode();
	Collection<Integer> visibleTextTypes = new ArrayList<Integer>();
	for (LocalTrackingConfig.Field field : ltc.fields) {
	    if (SampleWorkflowBL.isLtaVisibleDuringAction(field, actionCode)) {
		visibleTextTypes.add(field.textType);
	    }
	}
	return visibleTextTypes;
    }

    /** Helper method.  UserInfo may be null. */
    private Collection<Integer> decideTextTypesVisibleOnShowSample(
	    UserInfo user, LocalTrackingConfig ltc) {
	Collection<Integer> visibleTextTypes = new ArrayList<Integer>();
	for (LocalTrackingConfig.Field field : ltc.fields) {
	    if (user == null && (field.visibility & LocalTrackingConfig.VISIBLE_ON_SHOWSAMPLE_TO_UNAUTHENTICATED_USERS) != 0) {
		visibleTextTypes.add(field.textType);
	    } else if (AuthorizationCheckerBL.isLabUser(user)
                    && (field.visibility 
	    	    & LocalTrackingConfig.VISIBLE_ON_SHOWSAMPLE_TO_LAB_USERS) 
		    != 0) {
		visibleTextTypes.add(field.textType);
	    } else if (AuthorizationCheckerBL.isProviderUser(user)
	            && (field.visibility & LocalTrackingConfig.VISIBLE_ON_SHOWSAMPLE_TO_PROVIDER_USERS) != 0) {
		visibleTextTypes.add(field.textType);
	    } else {
		// All cases should have been covered above.
		assert false;
	    }
	}
	return visibleTextTypes;
    }
}
