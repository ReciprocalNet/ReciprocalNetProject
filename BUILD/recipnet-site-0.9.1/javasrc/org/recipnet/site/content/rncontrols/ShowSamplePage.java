/*
 * Reciprocal Net project
 * 
 * ShowSamplePage.java
 *
 * 04-May-2004: midurbin wrote first draft
 * 06-Jun-2005: midurbin fixed bug #1607 in doBeforeBody()
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 27-Jul-2005: midurbin updated doBeforeBody() to reflect name and spec
 *              changes
 * 28-Oct-2005: midurbin updated doBeforePageBody() to respect the
 *              ALLOW_IMPLICIT_PREF_CHANGES preference
 * 05-Jul-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.io.IOException;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.logevent.SampleViewLogEvent;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * This extention of {@code SamplePage} has special handling to do the
 * following:
 * <ul>
 * <li><strong>Redirect the browser to a page with the appropriate user
 * interface</strong>: If 'jumpsitePageUrl' is set and the sample associated
 * with the underlying {@code SamplePage} is not hosted at the local site, the
 * browser is redirected to the given URL. Otherwise, when both 'basicPageUrl'
 * and 'detailedPageUrl' properties are set, the browser is redirected based on
 * the preferences associated with the current session. If just one of those two
 * properties is set, the brower is redirected the that URL. Otherwise the
 * browser is not redirected and evaluates this page normally.</li>
 * <li><strong>Set preferences for the current session</strong>: If the
 * browser is not redirected, the current session preferences allow implicit
 * preference changes and the 'setPreferenceTo' property is set to a valid
 * showsample page view preference (see {@link
 * org.recipnet.site.shared.bl.UserBL UserBL}) the {@code UserPreferences}
 * associated with the session will be updated.</li>
 * <li><strong>Set the page title</strong>: The title is constructed by
 * appending either the sample's preferred name (if
 * 'usePreferredNameAsTitlePrefix' is true) or the sample's lab followed by it's
 * local lab id (if 'useLabAndLocalLabIdAsTitlePrefix' is true) and the given
 * 'titleSuffix' property value.</li>
 * <li><strong>Report a {@code SampleViewLogEvent} to core</strong>: When this
 * tag is evaluated without resulting in a redirection, a
 * {@code SampleViewLogEvent} is reported to core to indicate that the sample
 * has been viewed.</li>
 * </ul>
 */
public class ShowSamplePage extends SamplePage {

    /**
     * An optional property indicating the URL (relative to the context path) of
     * the JSP that displays the jumpsite showsample page. If this property is
     * set, the browser will redirect to the given URL whenever the underlying
     * {@code SamplePage}'s sample is not held at the local site. If this
     * property is unset, the current page must either be written to display a
     * non-locally held sample or must not be able to be navigated to for such
     * samples.
     */
    private String jumpSitePageUrl;

    /**
     * An optional property, indicating the URL (relative to the context path)
     * of the jsp that displays the basic showsample page. If this property is
     * set, the browser will be redirected to the given URL unless either the
     * sample is not held locally and 'jumpSitePageUrl' is given, or the current
     * session's preferences indicate that the detailed view is preferred and
     * the 'detailedPageUrl' property is set. If this property is unset, either
     * 'detailedPageUrl' must be set, or this page must be able to display a
     * "basic" view user interface.
     */
    private String basicPageUrl;

    /**
     * An optional property, indicating the URL (relative to the context path)
     * of the jsp that displays the detailed showsample page. If this property
     * is set, the browser will be redirected to the given URL unless either the
     * sample is not held locally and 'jumpSitePageUrl' is given, or the
     * currrent session's preferences indicate that the basic view is preferred
     * and the 'basicPageUrl' property is set. If this property is unset, either
     * 'basicPageUrl' must be set, or this page must be able to display the
     * "basic" view user interface.
     */
    private String detailedPageUrl;

    /**
     * An optional property that, if set, indicates the showsample page view
     * preference value that will be set for a user's session when the page
     * containing this tag is viewed without redirection. It doesn't make sense
     * to set this value if either 'basicPageUrl' or 'detailedPageUrl' is set
     * because their presence will result in a redirection. This property
     * defaults to null and is ignored unless set to a non-null value.
     */
    private UserBL.ShowsampleViewPref setPreferenceTo;

    /**
     * An optional property that, when set, indicates that a title should be
     * generated, and that the first part of it should be the preferred name of
     * the sample provided by the underlying {@code SamplePage}. If no
     * preferred name exists, the title prefix is generated using the lab name
     * and sample local lab id separated by the 'labAndNumberSeparator' property
     * value. This property must not be set to true when
     * 'useLabAndNumberAsTitlePrefix' is also set to true.
     */
    private boolean usePreferredNameAsTitlePrefix;

    /**
     * An optional property that, when set, indicates that a title should be
     * generated, and that the first part of it should be the sample's lab's
     * short name followed by the 'labAndNumberSeparator' property and the
     * sample's local lab id. This property must not be set to true when
     * 'useLabAndNumberAsTitlePrefix' is also set to true.
     */
    private boolean useLabAndNumberAsTitlePrefix;

    /**
     * An optional property that is inserted between the lab name and the local
     * lab id in the generated title. This is only used if
     * 'useLabAndNumberAsTitlePrefix' is set to true, or if
     * 'usePreferredNameAsTitlePrefix' is used and no sample names exist.
     */
    private String labAndNumberSeparator;

    /**
     * An optional property that, when set, indicates that a title should be
     * generated and should end with the string value of this property. The
     * beginning may be specified by either 'userPreferredNameAsTitlePrefix',
     * 'useLabAndNumberAsTitlePrefix' or left blank, causing this 'titleSuffix'
     * to be the entire title.
     */
    private String titleSuffix;

    /** {@inheritDoc} */
    @Override
    public void reset() {
        super.reset();
        this.jumpSitePageUrl = null;
        this.basicPageUrl = null;
        this.detailedPageUrl = null;
        this.setPreferenceTo = null;
        this.usePreferredNameAsTitlePrefix = false;
        this.useLabAndNumberAsTitlePrefix = false;
        this.labAndNumberSeparator = null;
        this.titleSuffix = null;
    }

    /**
     * @param url the url path (relative to the servlet context path) of a page
     *        that can display a jump-site user interface, or {@code null} if
     *        the page containing this tag can do so.
     */
    public void setJumpSitePageUrl(String url) {
        this.jumpSitePageUrl = url;
    }

    /**
     * @return the url path (relative to the servlet context path) of a page
     *         that can display a jump-site user interface, or {@code null} if
     *         the page containing this tag can do so.
     */
    public String getJumpSitePageUrl() {
        return this.jumpSitePageUrl;
    }

    /**
     * @param url the url path (relative to the servlet context path) of a page
     *        that can display the basic user interface, or {@code null} if the
     *        page containing this tag can do so.
     */
    public void setBasicPageUrl(String url) {
        this.basicPageUrl = url;
    }

    /**
     * @return the url path (relative to the servlet context path) of a page
     *         that can display the basic user interface, or {@code null} if the
     *         page containing this tag can do so.
     */
    public String getBasicPageUrl() {
        return this.basicPageUrl;
    }

    /**
     * @param url the url path (relative to the servlet context path) of a page
     *        that can display the detailed user interface, or {@code null} if
     *        the page containing this tag can do so.
     */
    public void setDetailedPageUrl(String url) {
        this.detailedPageUrl = url;
    }

    /**
     * @return the url path (relative to the servlet context path) of a page
     *         that can display the detailed user interface, or {@code null} if
     *         the page containing this tag can do so.
     */
    public String getDetailedPageUrl() {
        return this.detailedPageUrl;
    }

    /**
     * @param pageViewPref one of the enumerated page view preferences defined
     *        by {@code UserBL} that will be the new preference if this page is
     *        evaluated without redirecting to another
     */
    public void setSetPreferenceTo(UserBL.ShowsampleViewPref pageViewPref) {
        this.setPreferenceTo = pageViewPref;
    }

    /**
     * @return one of the enumerated page view preferences defined by
     *         {@code UserBL} that will be the new preference if this page is
     *         evaluated without redirecting to another
     */
    public UserBL.ShowsampleViewPref getSetPreferenceTo() {
        return this.setPreferenceTo;
    }

    /**
     * @param use a boolean indicating that the the sample's preferred name
     *        should be used as the first part of a generated title.
     */
    public void setUsePreferredNameAsTitlePrefix(boolean use) {
        this.usePreferredNameAsTitlePrefix = use;
    }

    /**
     * @return a boolean indicating whether the sample's preferred name should
     *         be used as the first part of the generated title.
     */
    public boolean getUsePreferredNameAsTitlePrefix() {
        return this.usePreferredNameAsTitlePrefix;
    }

    /**
     * @param use a boolean that when {@code true}, indicates that the sample's
     *        lab and local lab id should be used to generate the first part of
     *        the title
     */
    public void setUseLabAndNumberAsTitlePrefix(boolean use) {
        this.useLabAndNumberAsTitlePrefix = use;
    }

    /**
     * @return a boolean that when {@code true}, indicates that the sample's
     *         lab and local lab id should be used to generate the first part of
     *         the title
     */
    public boolean getUseLabAndNumberAsTitlePrefix() {
        return this.useLabAndNumberAsTitlePrefix;
    }

    /**
     * @param separator a {@code String} to be inserted between the lab name and
     *        sample number if the title prefix is generated using them.
     */
    public void setLabAndNumberSeparator(String separator) {
        this.labAndNumberSeparator = separator;
    }

    /**
     * @return a {@code String} to be inserted between the lab name and sample
     *         number if the title prefix is generated using them.
     */
    public String getLabAndNumberSeparator() {
        return this.labAndNumberSeparator;
    }

    /**
     * @param suffix text that will be appended to the the suffix generated in
     *        response to either of the prefix properties being set. If neither
     *        are set this suffix may be the entire title.
     */
    public void setTitleSuffix(String suffix) {
        this.titleSuffix = suffix;
    }

    /**
     * @return a {@code String} that will be appended to the the suffix
     *         generated in response to either of the prefix properties being
     *         set. If neither are set this suffix may be the entire title.
     */
    public String getTitleSuffix() {
        return this.titleSuffix;
    }

    /**
     * {@inheritDoc}; this version determines whether to redirect to a
     * different page, sets the topic, reports a {@code SampleViewLogEvent} and
     * sometimes updates the page view preference.
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        if (getPhase() == FETCHING_PHASE) {
            // determine whether to redirect to another page
            String redirectPath = null;
            UserPreferences preferences =
                    (UserPreferences) this.pageContext.getSession()
                            .getAttribute("preferences");
            CoreConnector cc =
                    CoreConnector
                            .extract(super.pageContext.getServletContext());

            if (this.jumpSitePageUrl != null) {
                try {
                    if ((cc.getRepositoryManager().getLocalHoldingLevel(
                            getSampleInfo().id) < RepositoryHoldingInfo.BASIC_DATA)
                            && !AuthorizationCheckerBL
                                    .canSeeSampleMetadataOnly(getUserInfo(),
                                            getSampleInfo())) {
                        redirectPath = this.jumpSitePageUrl;
                    }
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                }
            }
            if (redirectPath == null) {
                // if we're not redirecting to the jumpsite page...
                if ((this.basicPageUrl != null)
                        && (this.detailedPageUrl != null)) {
                    if (UserBL.getShowsampleViewPreference(preferences) == UserBL.ShowsampleViewPref.BASIC) {
                        redirectPath = this.basicPageUrl;
                    } else {
                        redirectPath = this.detailedPageUrl;
                    }
                } else if (this.basicPageUrl != null) {
                    redirectPath = this.basicPageUrl;
                } else if (this.detailedPageUrl != null) {
                    redirectPath = this.detailedPageUrl;
                } else if ((this.setPreferenceTo != null)
                        && UserBL.getPreferenceAsBoolean(
                                UserBL.Pref.ALLOW_IMPLICIT_PREF_CHANGES,
                                preferences)) {
                    UserBL.setShowsampleViewPreference(preferences,
                            this.setPreferenceTo);
                } else {
                    // do nothing, no need to redirect nor any preference to
                    // set
                }
            }
            try {
                HttpServletRequest request =
                        (HttpServletRequest) this.pageContext.getRequest();

                // redirect if needed
                if (redirectPath != null) {
                    ((HttpServletResponse) this.pageContext.getResponse())
                            .sendRedirect(request.getContextPath()
                                    + redirectPath + "?"
                                    + request.getQueryString());
                    abort();
                    return;
                }

                // report sample view log event
                cc.getSiteManager().recordLogEvent(
                        new SampleViewLogEvent(getSampleInfo().id,
                                getLabInfo().shortName, getLabInfo().id,
                                getSampleInfo().localLabId, this.pageContext
                                        .getSession().getId(), request
                                        .getServerName(), false));
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (IOException ex) {
                throw new JspException(ex);
            }

            // set the title
            StringBuilder sb = new StringBuilder();
            if (this.usePreferredNameAsTitlePrefix) {
                Iterator<SampleTextInfo> namesIt =
                        SampleTextBL.getSampleNames(getSampleInfo(), true)
                                .iterator();

                if (namesIt.hasNext()) {
                    sb.append(namesIt.next().value);
                } else {
                    sb.append(getLabInfo().shortName
                            + (this.labAndNumberSeparator == null ? ""
                                    : this.labAndNumberSeparator)
                            + getSampleInfo().localLabId);
                }
            } else if (this.useLabAndNumberAsTitlePrefix) {
                sb.append(getLabInfo().shortName
                        + (this.labAndNumberSeparator == null ? ""
                                : this.labAndNumberSeparator)
                        + getSampleInfo().localLabId);
            }
            if (this.titleSuffix != null) {
                sb.append(this.titleSuffix);
            }
            if (sb.length() != 0) {
                setTitle(sb.toString());
            }
        }
    }
}
