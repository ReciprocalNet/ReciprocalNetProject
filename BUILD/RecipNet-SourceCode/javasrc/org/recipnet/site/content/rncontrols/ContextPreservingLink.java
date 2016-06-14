/*
 * Reciprocal Net project
 * 
 * ContextPreservingLink.java
 * 
 * 22-Mar-2004: cwestnea wrote first draft
 * 21-Jun-2004: cwestnea modified to extend LinkHtmlElement
 * 10-Aug-2004: cwestnea added code to preserve sampleHistoryId from a 
 *              SampleContext and moved a lot of code to 
 *              onFetchingPhaseBeforeBody()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 08-Apr-2005: midurbin fixed bug #1573 in generateCopy()
 * 26-Apr-2005: midurbin added support for inter-site linking
 * 04-May-2005: midurbin added code to prevent invalid cross-site context
 *              preservation
 * 14-Jun-2005: midurbin added persistence of the "sampleId" parameter for the
 *              SampleHistoryContext
 * 06-Oct-2005: midurbin fixed bug #1655 in onRegistrationPhaseBeforeBody() and
 *              onFetchingPhaseBeforeBody()
 * 13-Mar-2006: jobollin removed unused imports; reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LinkHtmlElement;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * A ContextPreservingLink builds a link either by the most specific context it
 * is in, by a given context, or by the first ancestor of a given context type.
 * It wraps the body with the appropriate link tag by adding the correct
 * parameter to the parent {@code LinkHtmlElement}. Contexts are recognized in
 * this order: SampleHistoryContext, SampleContext, ProviderContext, LabContext,
 * and UserContext.
 * </p><p>
 * This tag can also optionally handle the {@code SiteContext}, but in a
 * different way. When the property 'considerSiteContext' is set and this tag is
 * also nested within a {@code SiteContext}, instead of adding the current
 * servlet context path to the beginning of the supplied relative 'href'
 * property, the {@code SiteInfo.baseUrl} is used. This has the effect of
 * linking to a certain page on a different reciprocal net site while
 * maintaining another context. Obviously this feature requires that the context
 * that is to be preserved has some meaning on the other site. This is well
 * suited for the {@code SampleContext} because sample identifying information
 * is global rather than site-specific.
 * </p>
 */
public class ContextPreservingLink extends LinkHtmlElement {

    /**
     * Optional attribute that specifies the context object this tag is to use
     * when generating the link.
     */
    private Object context;

    /**
     * Optional attribute containing the {@code Class} object that represents
     * the type of context this link should preserve.
     */
    private Class<?> contextType;

    /**
     * An optional property that when set, indicates that this tag should be a
     * link to the provided 'href' on the site for the {@code SiteContext} in
     * which this tag is nested. When this property is set, and a
     * {@code SiteContext} is found it will be assumed that the site is not
     * local and therefore some site-specific parameters used to preserve the
     * context will be excluded from the URL, causing potentially undesireable
     * behavior if the site is the local site. To avoid undesirable behavior
     * this property should only be set when offsite linking is intended. This
     * property defaults to false and MUST NOT be set to true when the
     * 'hrefIsAbsolute' property is also true.
     */
    private boolean considerSiteContext;

    /**
     * A reference to the most immediate {@code SiteContext} in which this tag
     * is nested. This is only set during the {@code REGISTRATION_PHASE} and
     * only when 'considerSiteContext' has been set to true.
     */
    private SiteContext siteContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();

        this.contextType = null;
        this.context = null;
        this.considerSiteContext = false;
        this.siteContext = null;
    }

    /**
     * @param context {@code Object} that is the context to preserve
     */
    public void setContext(Object context) {
        this.context = context;
    }

    /**
     * @return {@code Object} that is the context to preserve
     */
    public Object getContext() {
        return this.context;
    }

    /**
     * @param contextType {@code Class} representing the type of context to
     *        preserve
     */
    public void setContextType(Class<?> contextType) {
        this.contextType = contextType;
    }

    /**
     * @return {@code Class} representing the type of context to preserve
     */
    public Class<?> getContextType() {
        return this.contextType;
    }

    /**
     * @param consider indicates whether to adjust this link to be targeted at
     *        the site specified by a surrounding {@code SiteContext} if one
     *        exists.
     */
    public void setConsiderSiteContext(boolean consider) {
        this.considerSiteContext = consider;
    }

    /**
     * @return a boolean that indicates whether to adjust this link to be
     *         targeted at the site specified by a surrounding
     *         {@code SiteContext} if one exists.
     */
    public boolean getConsiderSiteContext() {
        return this.considerSiteContext;
    }

    /**
     * {@inheritDoc}; this version gets the correct context and context type.
     * If both the context and type are defined by the user, then this method
     * does nothing. If the context is undefined, but the type is defined, then
     * the first context with that type is found. If the type is undefined, but
     * the context is defined, then the most specific context type that the
     * context is an instance of is used. If they are both undefined, then the
     * first context is used and its most specific type is used.
     * 
     * @throws IllegalStateException if both 'hrefIsAbsolute' is {@code true}
     *         and 'considerSiteContext' is {@code true}, if
     *         'considerSiteContext' is {@code true} but the context to be
     *         preserved turns out to only be valid at the local site, if no
     *         context can be found for a given context type, or if the context
     *         given has an unknown type
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.considerSiteContext) {
            if (getHrefIsAbsolute()) {
                // the user indicated that the link should consider the site
                // context but gave a full URL, rather than one that could be
                // used for any site
                throw new IllegalStateException();
            }
            this.siteContext
                    = findRealAncestorWithClass(this, SiteContext.class);
        }

        // make sure we know what context we're using and what type it is
        if (this.context == null) {
            if (this.contextType != null) {
                this.context
                        = findRealAncestorWithClass(this, this.contextType);
                if (this.context == null) {
                    // this tag is not nested within a context of the
                    // specified contextType
                    throw new IllegalStateException();
                }
            } else {
                // find most specific context
                if ((this.context = findRealAncestorWithClass(
                        this,SampleHistoryContext.class)) != null) {
                    this.contextType = SampleHistoryContext.class;
                } else if ((this.context = findRealAncestorWithClass(
                        this, SampleContext.class)) != null) {
                    this.contextType = SampleContext.class;
                } else if ((this.context = findRealAncestorWithClass(
                        this, ProviderContext.class)) != null) {
                    this.contextType = ProviderContext.class;
                } else if ((this.context = findRealAncestorWithClass(
                        this, LabContext.class)) != null) {
                    this.contextType = LabContext.class;
                } else if ((this.context = findRealAncestorWithClass(
                        this, UserContext.class)) != null) {
                    this.contextType = UserContext.class;
                }
            }
        } else if (this.contextType == null) {
            // if context type is unspecified, use most specific type
            if (this.context instanceof SampleHistoryContext) {
                this.contextType = SampleHistoryContext.class;
            } else if (this.context instanceof SampleContext) {
                this.contextType = SampleContext.class;
            } else if (this.context instanceof ProviderContext) {
                this.contextType = ProviderContext.class;
            } else if (this.context instanceof LabContext) {
                this.contextType = LabContext.class;
            } else if (this.context instanceof UserContext) {
                this.contextType = UserContext.class;
            } else {
                // the contextType indicated was unknown/unsupported
                throw new IllegalStateException();
            }
        }
        if ((this.contextType == UserContext.class) && this.considerSiteContext
                && (this.siteContext != null)) {
            // users are not globally unique
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the appropriate information from
     * the selected context and adds the preserving parameter.
     * 
     * @throws IllegalStateException if the context type is unknown
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        if (this.context != null) {
            boolean isLinkingOffsite = ((this.siteContext != null)
                    && (this.siteContext.getSiteInfo() != null));
            String paramName;
            int paramValue;

            if (this.contextType == SampleHistoryContext.class) {
                SampleHistoryInfo sampleHistory = ((SampleHistoryContext)
                        this.context).getSampleHistoryInfo();

                addParameter(
                        "sampleId", String.valueOf(sampleHistory.sampleId));
                paramName = "sampleHistoryId";
                paramValue = ((sampleHistory == null)
                        ? SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID
                        : sampleHistory.id);
            } else if (this.contextType == SampleContext.class) {
                SampleInfo sample
                        = ((SampleContext) this.context).getSampleInfo();

                paramName = "sampleId";
                paramValue = (sample == null ? SampleInfo.INVALID_SAMPLE_ID
                        : sample.id);
                if ((sample != null) && !isLinkingOffsite) {
                    // do not include the site-specific sample history id
                    addParameter("sampleHistoryId",
                            String.valueOf(sample.historyId));
                }
            } else if (this.contextType == ProviderContext.class) {
                ProviderInfo provider
                        = ((ProviderContext) this.context).getProviderInfo();

                paramName = "providerId";
                paramValue = ((provider == null)
                        ? ProviderInfo.INVALID_PROVIDER_ID
                        : provider.id);
            } else if (this.contextType == LabContext.class) {
                LabInfo lab = ((LabContext) this.context).getLabInfo();

                paramName = "labId";
                paramValue = ((lab == null) ? LabInfo.INVALID_LAB_ID : lab.id);
            } else if (this.contextType == UserContext.class) {
                UserInfo user = ((UserContext) this.context).getUserInfo();

                paramName = "userId";
                paramValue = ((user == null) ? UserInfo.INVALID_USER_ID
                        : user.id);
            } else {
                // the contextType was unknown/unsupported
                throw new IllegalStateException();
            }
            
            addParameter(paramName, String.valueOf(paramValue));
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version delegates to the superclass's implementation
     * unless 'considerSiteContext' is {@code true}, this tag is nested within
     * a {@code SiteContext}, and that context provides a non-{@code null}
     * {@code SiteInfo}, in which case the current 'href' property value is
     * appended to the {@code baseUrl} for the site supplied by the
     * {@code SiteContext} and returned.
     */
    @Override
    protected String getWholeHrefForATag() {
        if (this.considerSiteContext && (this.siteContext != null)) {
            assert !getHrefIsAbsolute();

            SiteInfo si = this.siteContext.getSiteInfo();

            if (si != null) {
                // Note: SiteInfo.baseUrl ends with a slash (/) and the 'href'
                // begins with a slash (/) so we use a substring of the
                // 'href'
                return si.baseUrl + getHref().substring(1);
            }
        }

        return super.getWholeHrefForATag();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ContextPreservingLink dc
                = (ContextPreservingLink) super.generateCopy(newId, map);

        dc.context = map.get(this.context);
        dc.siteContext = (SiteContext) map.get(this.siteContext);

        return dc;
    }
}
