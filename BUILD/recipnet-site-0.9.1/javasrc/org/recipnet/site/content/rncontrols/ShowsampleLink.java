/*
 * Reciprocal Net project
 * 
 * ShowsampleLink.java
 * 
 * 15-Sep-2005: midurbin wrote first draft
 * 02-Feb-2006: ekoperda fixed bug #1728 in onFetchingPhaseBeforeBody()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LinkHtmlElement;
import org.recipnet.common.controls.SuppressionContext;
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
 * A custom tag that displays a link to the appropriate showsample page mode for
 * the current user and sample. The three choices are as follows:
 * <ol>
 * <li> <strong>basic view:</strong> used for samples hosted at the local site
 * when the "showsample view" preferences is BASIC for the preferences
 * associated with the current session. </li>
 * <li> <strong>detailed view:</strong> used for samples hosted at the local
 * site when the "showsample view" preference is DETAILED for the preferences
 * associated with the current session. </li>
 * <li> <strong>jump site view:</strong> used for samples not hosted at the
 * local site. </li>
 * </ol>
 * Note: any parameters needed by the pages specified in the properties must be
 * explicitly added (via a {@link org.recipnet.common.controls.LinkParam
 * LinkParam} tag).
 * </p>
 * <p>
 * This tag must be nested within a {@code SampleContext} unless
 * 'sampleIsKnownToBeLocal' is set to true, in order to determine whether this
 * tag should link to the jump-site page.
 * </p>
 */
public class ShowsampleLink extends LinkHtmlElement {

    /**
     * The {@code SampleContext} surrounding this tag. This reference is only
     * needed when it is not known whether the sample is hosted at the local
     * site or not, and is only determined when neccessary.
     */
    private SampleContext sampleContext;

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
     * An optional property (that must be set if 'sampleIsKnownToBeLocal' is
     * false) representing the URL, relative to the servlet context root, for
     * the page that is to display the JUMP SITE view of the sample.
     */
    private String jumpSitePageUrl;

    /**
     * An optional property that defaults to false and indicates whether the
     * {@code SampleInfo} from the surrounding {@code SampleContext} is known to
     * be hosted at the local site. When possible this should be set to true
     * because it saves time consuming calls to core.
     */
    private boolean sampleIsKnownToBeLocal;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.basicPageUrl = null;
        this.detailedPageUrl = null;
        this.jumpSitePageUrl = null;
        this.sampleIsKnownToBeLocal = false;
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

    /** Setter for the 'sampleIsKnownToBeLocal' property. */
    public void setSampleIsKnownToBeLocal(boolean local) {
        this.sampleIsKnownToBeLocal = local;
    }

    /** Getter for the 'sampleIsKnownToBeLocal' property. */
    public boolean getSampleIsKnownToBeLocal() {
        return this.sampleIsKnownToBeLocal;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the innermost
     * containing {@code SampleContext} if needed.
     * 
     * @throws IllegalStateException if this tag is not nested within a required
     *         context
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (!this.sampleIsKnownToBeLocal) {
            this.sampleContext = findRealAncestorWithClass(this,
                    SampleContext.class);
            if (this.sampleContext == null) {
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines and sets the 'href' property for
     * the underlying {@code LinkHtmlElement}, or throws an exception if the
     * appropriate link target cannot be determined and this tag isn't
     * suppressed.
     * 
     * @throws IllegalStateException if it cannot be determined whether the
     *         sample is local and this tag is not being suppressed
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        // Tentatively set the view preferred by the user.
        UserPreferences prefs
                = (UserPreferences) this.pageContext.getSession().getAttribute(
                        "preferences");
        if (UserBL.getShowsampleViewPreference(prefs)
                == UserBL.ShowsampleViewPref.BASIC) {
            setHref(this.basicPageUrl);
        } else {
            setHref(this.detailedPageUrl);
        }

        /*
         * Verify that the sample is hosted locally and override the view
         * preference if it is not.
         */
        if (!this.sampleIsKnownToBeLocal) {
            SampleInfo sample = this.sampleContext.getSampleInfo();

            if (sample == null) {
                /*
                 * The parent context is not supplying a sample to us for some
                 * reason.
                 */
                SuppressionContext sc = findRealAncestorWithClass(this,
                        SuppressionContext.class);
                
                if ((sc != null) && sc.isTagsBodySuppressedThisPhase()) {
                    /*
                     * We didn't need a sample anyway because our tag is being
                     * suppressed. Fail silently.
                     */
                    return rc;
                } else {
                    // We really do need a sample. This is a bug.
                    throw new IllegalStateException();
                }
            }
            
            UserInfo user = (UserInfo) pageContext.getSession().getAttribute(
                    "userInfo");
            CoreConnector cc = CoreConnector.extract(
                    super.pageContext.getServletContext());
            
            try {
                if ((cc.getRepositoryManager().getLocalHoldingLevel(sample.id)
                        < RepositoryHoldingInfo.BASIC_DATA)
                        && !AuthorizationCheckerBL.canSeeSampleMetadataOnly(
                                user, sample)) {
                    /*
                     * The sample is not accessible locally; override the user's
                     * view preference.
                     */
                    setHref(jumpSitePageUrl);
                }
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version prevents 'href' from being treated as a
     * transient property.
     */
    @Override
    public void copyTransientPropertiesFrom(HtmlPageElement source) {
        String preservedHref = this.getHref();

        super.copyTransientPropertiesFrom(source);
        this.setHref(preservedHref);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ShowsampleLink dc = (ShowsampleLink) super.generateCopy(newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);

        return dc;
    }
}
