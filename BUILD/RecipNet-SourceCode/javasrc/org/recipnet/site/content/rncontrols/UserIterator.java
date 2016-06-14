/*
 * Reciprocal Net project
 * 
 * UserIterator.java
 * 
 * 20-Aug-2004: midurbin wrote first draft
 * 16-Nov-2004: midurbin added 'sortByFullName' and 'sortByUsername' property
 * 23-Nov-2004: midurbin added an ErrorSupplier implementation
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 21-Jun-2005: midurbin moved the ErrorSupplier implementation to the
 *              superclass
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A Custom tag that evaluates its body once for each user that matches the
 * critera indicated by various tag attributes. This tag provides a
 * {@code UserContext} to any nested tags. Furthermore, a {@code LabContext} and
 * {@code ProviderContext} is provided and represents either the lab or provider
 * to which the user is affiliated. (Note: either {@code getLabInfo()} or
 * {@code getProviderInfo()} will return null depending on whether the user is a
 * lab user or a provider user) This tag may recognize a {@code LabContext} or
 * {@code ProviderContext} depending on whether the attributes
 * {@code restrictToLabUsers} or {@code restrictToProviderUsers} are set to
 * true.
 */
public class UserIterator extends HtmlPageIterator implements LabContext,
        ProviderContext, UserContext {

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code UserInfo} objects whose {@code isActive} field is set to 'true'
     * should be provided by this {@code UserContext} implementation to its
     * body. Defaults to true. May not be true if
     * {@code restrictToInactiveUsers} is true.
     */
    private boolean restrictToActiveUsers;

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code UserInfo} objects whose {@code isActive} field is set to 'false'
     * should be provided by this {@code UserContext} implementation to its
     * body. Defaults to false. May not be true if {@code restrictToActiveUsers}
     * is true.
     */
    private boolean restrictToInactiveUsers;

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code UserInfo} objects of users belonging to the lab indicated by the
     * most immediate {@code LabContext} should be provided by this
     * {@code UserContext} implementation to its body. This attribute defaults
     * to false.
     */
    private boolean restrictToLabUsers;

    /**
     * An optional attribute that, if true, indicates that only the
     * {@code UserInfo} objects of the users belonging to the provider indicated
     * by the most immediate {@code ProviderContext} should be provided by this
     * {@code UserContext} implementation to its body. This attribute defaults
     * to false.
     */
    private boolean restrictToProviderUsers;

    /**
     * An optional attribute that, if true, indicates that the users will be
     * sorted by their full names ({@code UserInfo.fullName}). This property
     * defaults to false and should not be set to true if any other sort orders
     * have been specified.
     */
    private boolean sortByFullName;

    /**
     * An optional attribute that, if true, indicates that the users will be
     * sorted by their user names ({@code UserInfo.username}). This property
     * defaults to false and should not be set to true if any other sort orders
     * have been specified.
     */
    private boolean sortByUsername;

    /**
     * If {@code restrictToLabUsers} is set, this variable is set to the most
     * immediate {@code LabContext} by {@code onRegistrationPhaseBeforeBody()}
     * and the lab provided by that context is the only lab whose users will be
     * included by this {@code UserIterator}.
     */
    private LabContext labContext;

    /**
     * If {@code restrictToProviderUsers} is set, this variable is set to the
     * most immeidate {@code ProviderContext} by
     * {@code onRegistrationPhaseBeforeBody()} and the provider provided by that
     * context represents the provider to which all the returned users will be
     * affiliated.
     */
    private ProviderContext providerContext;

    /**
     * A {@code Collection} of {@code UserInfo} objects that will be provided
     * for each iteration of this tag. Populated by
     * {@code onFetchingPhaseBeforeBody()} from the database.
     */
    private List<UserInfo> userInfos;

    /**
     * An {@code Iterator} over the {@code userInfos}, initialized by
     * {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<UserInfo> userInfoIterator;

    /**
     * A member of the {@code userInfos} {@code Collection} that represents the
     * {@code UserInfo} provided by this {@code UserContext} implementation for
     * the current iteration. This value is set by
     * {@code onIterationBeforeBody()} and returned by calls to
     * {@code getUserInfo()}.
     */
    private UserInfo currentUserInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.restrictToInactiveUsers = false;
        this.restrictToActiveUsers = true;
        this.restrictToLabUsers = false;
        this.restrictToProviderUsers = false;
        this.sortByFullName = false;
        this.sortByUsername = false;
        this.labContext = null;
        this.providerContext = null;
        this.userInfos = null;
        this.userInfoIterator = null;
        this.currentUserInfo = null;
    }

    /**
     * @param restrictToActiveUsers indicates whether users' {@code isActive}
     *        field must be 'true' to be included
     */
    public void setRestrictToActiveUsers(boolean restrictToActiveUsers) {
        this.restrictToActiveUsers = restrictToActiveUsers;
    }

    /**
     * @return a boolean indicating whether users' {@code isActive} field must
     *         be 'true' to be included
     */
    public boolean getRestrictToActiveUsers() {
        return this.restrictToActiveUsers;
    }

    /**
     * @param restrictToInactiveUsers indicates whether users' {@code isActive}
     *        field must be 'false' to be included
     */
    public void setRestrictToInactiveUsers(boolean restrictToInactiveUsers) {
        this.restrictToInactiveUsers = restrictToInactiveUsers;
    }

    /**
     * @return a boolean indicating whether users' {@code isActive} field must
     *         be 'false' to be included
     */
    public boolean getRestrictToInactiveUsers() {
        return this.restrictToInactiveUsers;
    }

    /**
     * @param restrictToLabUsers indicates whether only users belonging to the
     *        lab indicated by the most immediate {@code LabContext} will be
     *        included.
     */
    public void setRestrictToLabUsers(boolean restrictToLabUsers) {
        this.restrictToLabUsers = restrictToLabUsers;
    }

    /**
     * @return a boolean indicating whether only users belonging to the lab
     *         indicated by the most immediate {@code LabContext} will be
     *         included.
     */
    public boolean getRestrictToLabUsers() {
        return this.restrictToLabUsers;
    }

    /**
     * @param restrictToProviderUsers indicates whether only users belonging to
     *        the provider indicated by the most immediate
     *        {@code ProviderContext} will be included.
     */
    public void setRestrictToProviderUsers(boolean restrictToProviderUsers) {
        this.restrictToProviderUsers = restrictToProviderUsers;
    }

    /**
     * @return a boolean indicating whether only users belonging to the provider
     *         indicated by the most immediate {@code ProviderContext} will be
     *         included.
     */
    public boolean getRestrictToProviderUsers() {
        return this.restrictToProviderUsers;
    }

    /**
     * @param sort indicates whether this tag will provide {@code UserInfo}
     *        objects in order by full name to nested fields
     * @throws IllegalArgumentException if any other sort order has been
     *         specififed
     */
    public void setSortByFullName(boolean sort) {
        if (sort && this.sortByUsername) {
            throw new IllegalArgumentException();
        }
        this.sortByFullName = sort;
    }

    /**
     * @return a boolean indicating whether this tag will provide
     *         {@code UserInfo} objects in order by full name to nested tags
     */
    public boolean getSortByFullName() {
        return this.sortByFullName;
    }

    /**
     * @param sort indicates whether this tag will provide {@code UserInfo}
     *        objects in order by username to nested fields
     * @throws IllegalArgumentException if any other sort order has been
     *         specififed
     */
    public void setSortByUsername(boolean sort) {
        if (sort && this.sortByFullName) {
            throw new IllegalArgumentException();
        }
        this.sortByUsername = sort;
    }

    /**
     * @return a boolean indicating whether this tag will provide
     *         {@code UserInfo} objects in order by username to nested tags, or
     *         whether they will be unsorted
     */
    public boolean getSortByUsername() {
        return this.sortByUsername;
    }

    /**
     * Implements {@code UserContext}. This method may not be called before the
     * {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public UserInfo getUserInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.currentUserInfo;
    }

    /**
     * Implements {@code LabContext}. This method may not be called before the
     * {@code FETCHING_PHASE}. This method may return null if the {@code labId}
     * field of the {@code UserInfo} returned by this {@code UserContext} is
     * {@code INVALID_LAB_ID} or if the {@code UserInfo} is null.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public LabInfo getLabInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        if (this.currentUserInfo == null) {
            return null;
        }
        LabInfo labInfo = RequestCache.getLabInfo(
                this.pageContext.getRequest(), this.currentUserInfo.labId);
        assert (this.currentUserInfo.labId == LabInfo.INVALID_LAB_ID)
                || (labInfo != null);

        return labInfo;
    }

    /**
     * Implements {@code ProviderContext}. This method may not be called before
     * the {@code FETCHING_PHASE}. This method may return null if the
     * {@code providerId} field of the {@code UserInfo} returned by this
     * {@code UserContext} is {@code INVALID_PROVIDER_ID} or if {@code UserInfo}
     * is null.
     * 
     * @throws IllegalStateException if called before the {@code FETCHING_PHASE}
     */
    public ProviderInfo getProviderInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        if (this.currentUserInfo == null) {
            return null;
        }
        ProviderInfo providerInfo = RequestCache.getProviderInfo(
                this.pageContext.getRequest(), this.currentUserInfo.providerId);
        assert ((this.currentUserInfo.providerId
                == ProviderInfo.INVALID_PROVIDER_ID) || (providerInfo != null));

        return providerInfo;
    }

    /**
     * {@inheritDoc}; this version determines and sets the {@code LabContext}
     * and {@code ProviderContext} if they are needed to filter the users.
     * 
     * @throws IllegalStateException if a required context is missing or both
     *         {@code restrictToLabUsers} and {@code restrictToProviderUsers}
     *         are 'true' or both {@code restrictToActiveUsers} and
     *         {@code restrictToInactiveUsers} are 'true'.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get LabContext if needed
        if (this.restrictToLabUsers) {
            this.labContext = findRealAncestorWithClass(this, LabContext.class);
            if (this.labContext == null) {
                throw new IllegalStateException();
            }
        }

        // get ProviderContext if needed
        if (this.restrictToProviderUsers) {
            this.providerContext = findRealAncestorWithClass(this,
                    ProviderContext.class);
            if (this.providerContext == null) {
                throw new IllegalStateException();
            }
        }

        if ((this.restrictToLabUsers && this.restrictToProviderUsers)
                || (this.restrictToActiveUsers && this.restrictToInactiveUsers)) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version fetches {@code UserInfo} objects and their
     * corresponding {@code LabInfo} or {@code ProviderInfo} objects to populate
     * {@code userInfos}, {@code labInfos} and {@code providerInfos}.
     * 
     * @throws IllegalStateException if a required context returns {@code null}
     * @throws JspException wrapping other exceptions thrown by core during the
     *         fetching of container objects.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        this.userInfos = new ArrayList<UserInfo>();

        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());
        
        try {
            UserInfo userInfoArray[];

            if (this.restrictToLabUsers) {
                LabInfo li = this.labContext.getLabInfo();

                if (li == null) {
                    throw new IllegalStateException();
                }
                userInfoArray = cc.getSiteManager().getUsersForLab(li.id);
            } else if (this.restrictToProviderUsers) {
                ProviderInfo pi = this.providerContext.getProviderInfo();

                if (pi == null) {
                    throw new IllegalStateException();
                }
                userInfoArray = cc.getSiteManager().getUsersForProvider(pi.id);
            } else {
                userInfoArray = cc.getSiteManager().getAllUserInfo();
            }

            for (UserInfo user : userInfoArray) {
                RequestCache.putUserInfo(this.pageContext.getRequest(), user);
                if ((this.restrictToActiveUsers && !user.isActive)
                        || (this.restrictToInactiveUsers && user.isActive)) {
                    /*
                     * the current UserInfo should not be added to the
                     * collection of UserInfo's because it does not match the
                     * criteria for inclusion; move on to the next
                     */
                    continue;
                }
                this.userInfos.add(user);
                if (user.providerId == ProviderInfo.INVALID_PROVIDER_ID) {
                    // lab user
                    assert user.labId != LabInfo.INVALID_LAB_ID;

                    LabInfo labInfo = RequestCache.getLabInfo(
                            this.pageContext.getRequest(), user.labId);

                    if (labInfo == null) {
                        labInfo = cc.getSiteManager().getLabInfo(user.labId);
                        RequestCache.putLabInfo(this.pageContext.getRequest(),
                                labInfo);
                    }
                } else {
                    // provider user
                    assert user.labId == LabInfo.INVALID_LAB_ID;

                    ProviderInfo providerInfo = RequestCache.getProviderInfo(
                            this.pageContext.getRequest(), user.providerId);

                    if (providerInfo == null) {
                        providerInfo = cc.getSiteManager().getProviderInfo(
                                user.providerId);
                        RequestCache.putProviderInfo(
                                this.pageContext.getRequest(), providerInfo);
                    }
                }
            }

            if (this.sortByFullName) {
                /*
                 * the default search order for UserInfo objects is
                 * alphabetically by fullname
                 */
                Collections.sort(this.userInfos);
            } else if (this.sortByUsername) {
                /*
                 * inline Comparator is used to get the ordering by usernames,
                 * can't use natural ordering of UserInfo because it goes by
                 * their full names.
                 */
                Collections.sort(this.userInfos, new Comparator<UserInfo>() {
                    public int compare(UserInfo user1, UserInfo user2) {
                        return (user1.username.compareTo(user2.username));
                    }
                });
            }
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version performs a simple check to verify that the
     * number of posted iterations is equal to the number of iterations
     * calculated during the {@code FETCHING_PHASE}.
     * 
     * @throws IllegalStateException if there is a different number of
     *         {@code UserInfo} objects than the {@code postedIterationCount}
     *         maintained by the superclass.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        if (this.userInfos.size() != this.getPostedIterationCount()) {
            /*
             * the number or UserInfo objects that match the criteria of this
             * control is inconsistent with the value from the previous request
             */
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.userInfos != null) {
            this.userInfoIterator = this.userInfos.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version causes the body to be evaluated once for
     * each user meeting this tag's filter conditions
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.userInfoIterator == null) {
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
        if (this.userInfoIterator.hasNext()) {
            this.currentUserInfo = this.userInfoIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UserIterator dc = (UserIterator) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);
        dc.providerContext = (ProviderContext) map.get(this.providerContext);
        dc.labContext = (LabContext) map.get(this.labContext);
        if (this.userInfos != null) {
            dc.userInfos = new ArrayList<UserInfo>(this.userInfos);
        }

        return dc;
    }
}
