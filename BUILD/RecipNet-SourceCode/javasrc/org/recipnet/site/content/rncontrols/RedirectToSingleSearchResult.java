/*
 * Reciprocal Net project
 * 
 * RedirectToSingleSearchResult.java
 *
 * 12-Apr-2005: midurbin wrote first draft
 * 26-Apr-2005: midurbin updated reference to UserInfo.preference in
 *              onFetchingPhaseBeforeBody() to accomodate architecture change
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 23-Sep-2005: midurbin added support to redirect to the appropriate one of
 *              the various showsample pages
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlRedirect;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * <p>
 * A custom tag that redirects the client to a sample page when nested in a
 * {@code SearchResultsPage} for which there is exactly one search result. The
 * decision to redirect to either the provided 'editSamplePageUrl' or
 * 'viewSamplePageUrl' is made using the following rule.
 * </p><p>
 * If there is no currently logged in user, or the currently logged in user does
 * not have permission to edit the single sample, the client is redirected to
 * the 'showSamplePageUrl', otherwise the decision is made based on the user's
 * preferences.
 * </p><p>
 * The page to which this tag might redirect the browser will be passed the the
 * request parameter 'sampleId' with a value equalling the id of the single
 * sample included in the search results.
 * </p>
 */
public class RedirectToSingleSearchResult extends HtmlRedirect {

    /**
     * A reference to the {@code SearchResultsPage} in which this tag is nested.
     * This is the same objects returned by the superclass' {@code getPage()}
     * method.
     */
    private SearchResultsPage searchResultsPage;

    /**
     * A required property that must be set to the path of the view sample page.
     * This page will be provided with the requiest parameter "sampleId" whose
     * value will be set to that of the single sample returned by the
     * {@code SearchResultsPage} that caused the redirect condition to be met.
     * The value must be relative to the servlet context path.
     */
    private String editSamplePageUrl;

    /**
     * A required proprety representing the URL, relative to the servlet context
     * root, for the page that is to display the BASIC view of the sample.
     */
    private String basicPageUrl;

    /**
     * A required property representing the URL, relative to the servlet context
     * root, for the page that is to display the DETAILED view of the sample.
     */
    private String detailedPageUrl;

    /**
     * A required property representing the URL, relative to the servlet context
     * root, for the page that is to display the JUMP SITE view of the sample.
     */
    private String jumpSitePageUrl;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.setCondition(false);
        this.searchResultsPage = null;
    }

    /** Setter for the 'basicPageUrl' property. */
    public void setBasicPageUrl(String url) {
        this.basicPageUrl = url;
    }

    /** Getter for the 'basicPageUrl' property. */
    public String getBasicPageUrl() {
        return this.basicPageUrl;
    }

    /** Setter for the 'detailedPageUrl' property. */
    public void setDetailedPageUrl(String url) {
        this.detailedPageUrl = url;
    }

    /** Getter for the 'detailedPageUrl' property. */
    public String getDetailedPageUrl() {
        return this.detailedPageUrl;
    }

    /** Setter for the 'jumpSitePageUrl' property. */
    public void setJumpSitePageUrl(String url) {
        this.jumpSitePageUrl = url;
    }

    /** Getter for the 'jumpSitePageUrl' property. */
    public String getJumpSitePageUrl() {
        return this.jumpSitePageUrl;
    }

    /**
     * @param url the path, relative to the servlet context path, of an edit
     *        sample page
     */
    public void setEditSamplePageUrl(String url) {
        this.editSamplePageUrl = url;
    }

    /**
     * @return a {@code String} representing the path, relative to the servlet
     *         context path, of an edit sample page
     */
    public String getEditSamplePageUrl() {
        return this.editSamplePageUrl;
    }

    /**
     * {@inheritDoc}; this version simply gets a reference to the
     * {@code SearchResultsPage} in which it is nested.
     * 
     * @throws IllegalStateException if the {@code HtmlPage} in which this tag
     *         is nested is not a {@code SearchResultsPage}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        try {
            this.searchResultsPage = (SearchResultsPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether there was exactly one
     * search result supplied by the {@code SearchResultPage} and if so, sets
     * the 'condition' property to {@code true} (to trigger redirection at the
     * end of the phase) and the target property to either the
     * 'showSamplePageUrl' or the 'editSamplePageUrl' based on the currently
     * logged in user's authorization and preferred page.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        UserInfo userInfo = this.searchResultsPage.getUserInfo();
        UserInfo sessionUserInfo
                = (UserInfo) this.pageContext.getSession().getAttribute(
                        "userInfo");
        UserPreferences prefs
                = (UserPreferences) this.pageContext.getSession().getAttribute(
                        "preferences");

        /*
         * SearchResultsPage is required to provide the UserInfo for the
         * currently logged-in user; the following code asserts that this is
         * true and that the preferences for the session are the user's
         * preferences
         */
        if (userInfo != null) {
            assert (sessionUserInfo != null)
                    && (userInfo.id == sessionUserInfo.id);
            assert userInfo.preferences.equals(prefs);
        } else {
            assert (sessionUserInfo == null);
        }

        int firstResultIndex = 1 + (this.searchResultsPage.getPageSize()
                * (this.searchResultsPage.getPageNumber() - 1));
        int lastResultIndex = firstResultIndex
                + this.searchResultsPage.getPageSize() - 1;
        if ((firstResultIndex != 1)
                || (this.searchResultsPage.getElementCount() != 1)) {
            /*
             * the Search has more than one result; leave the 'condition' as
             * false so that this tag does nothing.
             */
            return rc;
        }
        Collection<SampleInfo> samples = this.searchResultsPage.getSearchResults(
                firstResultIndex, lastResultIndex - firstResultIndex + 1);
        for (SampleInfo firstSample : samples) {
            if (AuthorizationCheckerBL.canEditSample(userInfo, firstSample)
                    && (UserBL.getSampleViewPreference(prefs)
                            == UserBL.SampleViewPref.SAMPLE)) {
                setTarget(this.editSamplePageUrl);
            } else {
                CoreConnector cc = CoreConnector.extract(
                        super.pageContext.getServletContext());

                try {
                    if ((cc.getRepositoryManager().getLocalHoldingLevel(
                            firstSample.id) < RepositoryHoldingInfo.BASIC_DATA)
                            && !AuthorizationCheckerBL.canSeeSampleMetadataOnly(
                                    userInfo, firstSample)) {
                        setTarget(this.jumpSitePageUrl);
                    } else if (UserBL.getShowsampleViewPreference(prefs)
                            == UserBL.ShowsampleViewPref.BASIC) {
                        setTarget(this.basicPageUrl);
                        addRequestParam("sampleHistoryId",
                                String.valueOf(firstSample.historyId));
                    } else {
                        setTarget(this.detailedPageUrl);
                        addRequestParam("sampleHistoryId",
                                String.valueOf(firstSample.historyId));
                    }
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                }
            }
            addRequestParam("sampleId", String.valueOf(firstSample.id));
            setCondition(true);
            break;
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version preserves the existing value of the
     * superclass's 'condition' property (which is NOT exposed in the TLD entry
     * for this subclass) but otherwise just delegates to the superclass.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        boolean preservedCondition = this.getCondition();

        super.copyTransientPropertiesFrom(source);
        this.setCondition(preservedCondition);
    }
}
