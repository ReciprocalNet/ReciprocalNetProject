/*
 * Reciprocal Net project
 * 
 * ProviderSelector.java
 * 
 * 30-Jun-2004: cwestnea wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseAfterBody()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 30-Jun-2005: midurbin added 'restrictToUsersProvider' property
 * 26-Jul-2005: midurbin removed calls to obsolete listbox methods
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * This tag provides a listbox containing providers from a lab specified either
 * as an attribute to this tag, or with a {@code LabContext}.
 */
public class ProviderSelector extends ListboxHtmlControl {
    /**
     * Optional attribute; specifies whether or not to add inactive providers to
     * the list. Initialized to {@code false} in {@code reset()}.
     */
    private boolean excludeInactiveProviders;

    /**
     * An optional attribute that when true indicates that when the user is a
     * provider user, his/her provider should always be the only provider
     * listed. If there is no currently logged-in user or he/she is not a
     * provider user, this property has no effect.
     */
    private boolean restrictToUsersProvider;

    /**
     * Optional attribute; specifies the id of the lab to list providers from.
     * If this is not set, then the nearest {@code LabContext} will be
     * retrieved. If it is set, the {@code LabContext} will be ignored.
     */
    private int labId;

    /**
     * The {@code LabContext} of the lab to get the list of providers from.
     * Initialized in {@code reset()} and set in
     * {@code onRegistrationPhaseBeforeBody}.
     */
    private LabContext labContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.excludeInactiveProviders = false;
        this.restrictToUsersProvider = false;
        this.labId = LabInfo.INVALID_LAB_ID;
        this.labContext = null;
    }

    /** @return whether or not this tag only gets active providers */
    public boolean getOnlyActive() {
        return this.excludeInactiveProviders;
    }

    /**
     * @param excludeInactiveProviders whether or not this tag gets active
     *        providers
     */
    public void setOnlyActive(boolean excludeInactiveProviders) {
        this.excludeInactiveProviders = excludeInactiveProviders;
    }

    /**
     * @param include whether or not this tag should include only the provider
     *        to which the user belongs (if he/she is a provider user)
     */
    public void setRestrictToUsersProvider(boolean include) {
        this.restrictToUsersProvider = include;
    }

    /**
     * @return a boolean that indicates whether or not this tag should include
     *         just the provider to which the user belongs (if he/she is a
     *         provider user)
     */
    public boolean getRestrictToUsersProvider() {
        return this.restrictToUsersProvider;
    }

    /** @return the lab id of the providers to get */
    public int getLabId() {
        return this.labId;
    }

    /** @param labId the lab id of the providers to get */
    public void setLabId(int labId) {
        this.labId = labId;
    }

    /**
     * {@inheritDoc}; this version retrieves the {@code LabContext} if the lab
     * id is not defined
     * 
     * @throws IllegalStateException if no {@code LabContext} can be found and
     *         the {@code labId} attribute is not set
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.labId == LabInfo.INVALID_LAB_ID) {
            // find a lab context
            this.labContext = findRealAncestorWithClass(this, LabContext.class);
            if (this.labContext == null) {
                // Can't find a LabContext
                throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version retrieves the appropriate
     * {@code ProviderInfo} objects from the core and adds them to the list of
     * options.
     * 
     * @throws JspException wrapping an exception thrown by core
     * @throws IllegalStateException if the list could not be populated because
     *         neither the contexts nor the 'labId' property provided any useful
     *         information
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        ArrayList<ProviderInfo> providerList = new ArrayList<ProviderInfo>();
        UserInfo user = (UserInfo) this.pageContext.getSession().getAttribute(
                "userInfo");

        if ((this.labContext != null) && (this.labContext.getLabInfo() != null)) {
            this.labId = this.labContext.getLabInfo().id;
        }
        if (this.restrictToUsersProvider && (user != null)
                && (user.providerId != ProviderInfo.INVALID_PROVIDER_ID)) {
            /*
             * determine whether there is a user, whether he/she is associated
             * with a provider, and add it to the list if it's not already
             * there.
             */
            ProviderInfo usersProvider = RequestCache.getProviderInfo(
                    pageContext.getRequest(), user.providerId);

            if (usersProvider == null) {
                try {
                    usersProvider = CoreConnector.extract(
                                    this.pageContext.getServletContext()
                            ).getSiteManager().getProviderInfo(user.providerId);
                } catch (RemoteException ex) {
                    CoreConnector.extract(this.pageContext.getServletContext()
                            ).reportRemoteException(ex);
                } catch (ResourceNotFoundException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
            }
            providerList.add(usersProvider);
            RequestCache.putProviderInfo(pageContext.getRequest(),
                    usersProvider);
        } else if (this.labId != LabInfo.INVALID_LAB_ID) {
            // Get reference to SiteManager
            SiteManagerRemote siteManager;
            CoreConnector coreConnector
                    = CoreConnector.extract(pageContext.getServletContext());
            
            try {
                siteManager = coreConnector.getSiteManager();
            } catch (RemoteException ex) {
                throw new JspException(ex);
            }

            // get the list of providers
            try {
                for (ProviderInfo provider
                        : siteManager.getAllProviderInfo(this.labId)) {
                    providerList.add(provider);
                    RequestCache.putProviderInfo(pageContext.getRequest(),
                            provider);
                }
            } catch (RemoteException ex) {
                coreConnector.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            }
        }
        Collections.sort(providerList);
        
        for (ProviderInfo provider : providerList) {
            // filter out inactive providers if requested
            if (!this.excludeInactiveProviders || provider.isActive) {
                // add this provider to the listbox
                this.addOption(true, provider.name, String.valueOf(provider.id));
            }
        }

        return super.onFetchingPhaseAfterBody();
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ProviderSelector dc = (ProviderSelector) super.generateCopy(newId, map);
        
        dc.labContext = (LabContext) map.get(this.labContext);
        
        return dc;
    }
}
