/*
 * Reciprocal Net project
 * 
 * AuthorizationCheckerBL.java
 *
 * 01-Jun-2004: cwestnea wrote first draft based on AuthenticationController
 * 21-Jun-2004: cwestnea added canSeeLabSummary()
 * 29-Jun-2004: midurbin added consideration for
 *              'restrictLargeFilesAvailability' to
 *              canRequestUnavailableFiles()
 * 19-Aug-2004: cwestnea added canSeeProviderListForLab() and 
 *              canFilterResultsByOwnProvider()
 * 15-Nov-2004: midurbin added isAccessLevelValid(), setAccessLevel() and
 *              getUserAccessLevelForSample()
 * 15-Nov-2004: midurbin made isLabUser() and isProviderUser() public
 * 30-Nov-2004: midurbin added canEditSomethingAboutUser(),
 *              canChangeUserPassword() and canDeactivateUser()
 * 30-Nov-2004: midurbin added canEditProvider(), canAddProviderForLab()
 * 25-Feb-2005: midurbin added blessSearchParams(), reblessSearchParams() and
 *              revokeAllBlessings()
 * 28-Apr-2005: midurbin added canSeeSampleText()
 * 18-May-2005: midurbin added canChangeUserPreferences(), updated
 *              canEditSomethingAboutUser()
 * 10-Jun-2005: midurbin updated canSubmitSamples()
 * 06-Oct-2005: midurbin fixed bug #1633 in canEditUser(),
 *              canEditSomethingAboutUser(), canChangeUserPassword(),
 *              canChangeUserPreferences() and canDeactivateUser(); added
 *              canChangeOwnPreferences()
 * 06-Oct-2005: midurbin updated references to SampleInfo.providerId
 * 02-Nov-2005: midurbin added canSetSamplesProvider()
 * 30-May-2006: jobollin added generics and reformatted the source
 */

package org.recipnet.site.shared.bl;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;

import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.search.AuthorizationSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintGroup;

/**
 * This class contains all the business logic required to determine if a user is
 * authorized to perform an action. All the methods require a {@code UserInfo}
 * object, which contains the data on the user to authorize. Most of the methods
 * also require objects to authorize on. The user attempting to authorize may be
 * null, and in that case it is assumed that the user is not authenticated. The
 * objects that the user is attempting to access must not be null. Also, if the
 * user is not null, any arguments that provide additional information about the
 * user must not be null. In these cases, a {@code NullPointerException} will be
 * thrown if a null valued is passed to it.
 */
public abstract class AuthorizationCheckerBL {
    /**
     * Checks a user's authorization to administer a given lab.
     * 
     * @param userInfo the user to perform the check upon.
     * @param userLab the lab of the user we are performing the check on.
     * @param proposedLab the lab that the user wants to administer.
     * @return true if the user is a site admin or a lab admin and a member of
     *         the proposed lab, or if the user is a site admin for the site
     *         that the lab is on.
     */
    public static boolean canAdministerLab(UserInfo userInfo, LabInfo userLab,
            LabInfo proposedLab) {
        if (userInfo == null) {
            return false;
        }
        if ((isSiteAdmin(userInfo) || isLabAdmin(userInfo))
                && (userInfo.labId == proposedLab.id)) {
            /*
             * The current user apparently has administration privileges on his
             * own lab.
             */
            return true;
        }
        if (isSiteAdmin(userInfo)) {
            if (userLab.homeSiteId == proposedLab.homeSiteId) {
                /*
                 * The current user is a site admin and the lab he wants to
                 * administer is hosted at the local site (the site to which the
                 * logged-on user's account belongs).
                 */
                return true;
            }
        }
        return false;
    }

    /**
     * Checks a user's authorization to add a provider to a given lab.
     * 
     * @param userInfo the user to perform the check upon
     * @param userLab the lab to which the user belongs
     * @param labInfo the lab to which the user wishes to add a new provider
     * @return true if the user may add a new provider to the given lab
     */
    public static boolean canAddProviderForLab(UserInfo userInfo,
            LabInfo userLab, LabInfo labInfo) {
        return canAdministerLab(userInfo, userLab, labInfo);
    }

    /**
     * Checks a user's authorization to edit a provider.
     * 
     * @param userInfo the user to perform the check upon
     * @param userLab the lab to which the user belongs
     * @param providerLab the lab to which the provider belongs
     * @return true if the user may edit providers associated with the given lab
     */
    public static boolean canEditProvider(UserInfo userInfo, LabInfo userLab,
            LabInfo providerLab) {
        return canAdministerLab(userInfo, userLab, providerLab);
    }

    /**
     * Determines whether the {@code userInfo} can edit the proposed user in the
     * specified lab or site.
     * 
     * @param userInfo the user to perform the check upon
     * @param proposedUser the user that userInfo is trying to edit
     * @param proposedUsersProvider the provider associated with the proposed
     *        user if he/she is a provider user or null if he/she is a lab user
     * @return {@code true} if the logged on user can edit the user passed to
     *         the method, otherwise {@code false}
     * @throws IllegalArgumentException if the proposedUser is a provider user
     *         but the proposedUsersProvider does not describe the
     *         proposedUser's provider
     */
    public static boolean canEditUser(UserInfo userInfo, UserInfo proposedUser,
            ProviderInfo proposedUsersProvider) {
        if (isLabUser(proposedUser)) {
            return canCreateLabUser(userInfo, proposedUser.labId);
        } else {
            if ((proposedUsersProvider == null)
                    || (proposedUsersProvider.id != proposedUser.providerId)) {
                throw new IllegalArgumentException();
            }
            return canCreateProviderUser(userInfo, proposedUsersProvider.labId);
        }
    }

    /**
     * Determines whether the given user can create a user account in the
     * specified lab.
     * 
     * @param userInfo the user to perform the check upon
     * @param labId the lab the user is attempting to create a new user in
     * @return true if the user is a site admin or a lab admin on the given lab
     */
    public static boolean canCreateLabUser(UserInfo userInfo, int labId) {
        return isSiteAdmin(userInfo)
                || (isLabAdmin(userInfo) && (userInfo.labId == labId));
    }

    /**
     * Determines whether the given user can create a user account in the
     * specified lab. This implementation calls
     * {@code canCreateLabUser(UserInfo,int)} with the given lab's id.
     * 
     * @param userInfo the user to perform the check upon
     * @param lab the lab the user is attempting to create a new user in
     * @return true if the user is a site admin or a lab admin on the given lab
     */
    public static boolean canCreateLabUser(UserInfo userInfo, LabInfo lab) {
        return canCreateLabUser(userInfo, lab.id);
    }

    /**
     * Determines whether the given user can create a user account in the
     * specified provider.
     * 
     * @param userInfo the user to perform the check upon
     * @param providerLabId the lab id of the provider that the user is
     *        attempting to create a new user in
     * @return true if athe user is a site admin or a lab admin of the same lab
     *         that the provider is in
     */
    public static boolean canCreateProviderUser(UserInfo userInfo,
            int providerLabId) {
        return isSiteAdmin(userInfo)
                || (isLabAdmin(userInfo) && (userInfo.labId == providerLabId));
    }

    /**
     * Determines whether the given user can create a user account in the
     * specified provider. This implementation calls
     * {@code canCreateProviderUser(UserInfo,int)} with the given provider's lab
     * id.
     * 
     * @param userInfo the user to perform the check upon
     * @param provider the provider that the user is attempting to create a new
     *        user in
     * @return true if athe user is a site admin or a lab admin of the same lab
     *         that the provider is in
     */
    public static boolean canCreateProviderUser(UserInfo userInfo,
            ProviderInfo provider) {
        return canCreateProviderUser(userInfo, provider.labId);
    }

    /**
     * Determines whether the logged-on user has the ability edit at least one
     * lab on this site.
     * 
     * @param userInfo the user to perform the check on
     * @return true if a user can edit at least one lab at this site.
     */
    public static boolean canAdministerLabs(UserInfo userInfo) {
        return isLabAdmin(userInfo) || isSiteAdmin(userInfo);
    }

    /**
     * Determine whether can the given user can edit the given sample.
     * 
     * @param userInfo the user to perform the check on
     * @param sample the sample the user is trying to edit
     * @return true if the user is part of the lab that owns the sample or if
     *         the user has been given special access rights on the sample.
     */
    public static boolean canEditSample(UserInfo userInfo, SampleInfo sample) {
        if (userInfo == null) {
            return false;
        }
        if (userInfo.labId == sample.labId) {
            return true;
        }

        /*
         * a simple iteration should be more efficient than a sort followed by a
         * binary search for relatively small lists
         */
        for (SampleAccessInfo access : sample.accessInfo) {
            if (userInfo.id == access.userId) {
                return (access.accessLevel == SampleAccessInfo.READ_WRITE);
            }
        }

        return false;
    }

    /**
     * Determines whether the specified sample should be visible to the
     * specified user.
     * 
     * @param userInfo the user to perform the check on
     * @param sample the sample for which access is being attempted
     * @return true if the sample is public or if the user has the same lab or
     *         provider id as the sample, or if the user has been granted
     *         special access to the sample.
     */
    public static boolean canSeeSample(UserInfo userInfo, SampleInfo sample) {
        if (userInfo == null) {
            /*
             * The user is unauthenticated; only public samples are visible to
             * him.
             */
            return sample.isPublic();
        }

        // Check for cases of implicit access.
        if (sample.isPublic()
                || ((sample.labId == userInfo.labId)
                        && (userInfo.labId != LabInfo.INVALID_LAB_ID))
                || ((sample.mostRecentProviderId == userInfo.providerId)
                        && (userInfo.providerId != ProviderInfo.INVALID_PROVIDER_ID))) {
            return true;
        }

        /*
         * Check for cases of explicit access by iterating through the access
         * list. Use a simple iteration rather than a sort/binary search because
         * this will be quicker for small lists.
         */
        for (SampleAccessInfo access : sample.accessInfo) {
            if (userInfo.id == access.userId) {
                return (access.accessLevel >= SampleAccessInfo.READ_ONLY);
            }
        }

        // No access.
        return false;
    }

    /**
     * Determines whether the specified sample should be visible to the
     * specified user in metadata-only mode. Metadata-only mode allows a
     * sample's metadata to be displayed to a user on the local site even when
     * the local site has no holding for the sample (and thus lacks access to
     * its data files). Access to metadata-only is more restrictive than access
     * to the normal metadata/data mode, as regulated by canSeeSample(). The
     * current implementation permits metadata-only viewing only for samples
     * that are not public and always returns false when {@code canSeeSample()}
     * would return false.
     * 
     * @param userInfo the user to perform the check on
     * @param sample the sample for which access is being attempted
     * @return true if the user can see the sample and the sample is not public.
     */
    public static boolean canSeeSampleMetadataOnly(UserInfo userInfo,
            SampleInfo sample) {
        return canSeeSample(userInfo, sample) && !sample.isPublic();
    }

    /**
     * Determines whether the user is permitted to submit samples.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is a lab user
     */
    public static boolean canSubmitSamples(UserInfo userInfo) {
        if (isLabUser(userInfo)) {
            return true;
        }
        if (isProviderUser(userInfo)
                && ((userInfo.globalAccessLevel
                        & UserInfo.SUBMITTING_PROVIDER_ACCESS) != 0)) {
            return true;
        }
        return false;
    }

    /**
     * <p>
     * Determines whether the specified user is permitted to see the specified
     * annotation/attribute on the specified sample. This method always returns
     * false when {@code canSeeSample()} returns false for the same user and
     * sample.
     * </p>
     * <p>
     * In the current implementation, any user who may see the sample (as
     * determined by {@link AuthorizationCheckerBL#canSeeSample canSeeSample()})
     * may see all non-localtracking attributes except {@code TEXT_CONTRIBUTOR}.
     * Users that can edit the sample (as determined by
     * {@link AuthorizationCheckerBL#canEditSample canEditSample()}) may see any
     * text type.
     * </p>
     * 
     * @param user the user to perform the check on; this value may be null to
     *        indicate and anonymous user
     * @param sample the sample containing the annotation/attribute; this value
     *        may NOT be null.
     * @param sampleTextInfo the annotation/attribute for which the visibility
     *        to the given user is being checked; this value may NOT be null
     * @return true if the provided user may see the sample text, otherwise
     *         false
     * @throws IllegalArgumentException if either the 'sample' or
     *         'sampleTextInfo' is null
     */
    public static boolean canSeeSampleText(UserInfo user, SampleInfo sample,
            SampleTextInfo sampleTextInfo) {
        if ((sample == null) || (sampleTextInfo == null)) {
            throw new IllegalArgumentException();
        }
        if (!canSeeSample(user, sample)) {
            return false;
        }
        int textType = sampleTextInfo.type;
        if ((textType == SampleTextBL.TEXT_CONTRIBUTOR)
                && !canEditSample(user, sample)) {
            return false;
        }
        if (!SampleTextBL.isLocalAttribute(textType)) {
            return true;
        }
        return canEditSample(user, sample);
    }

    /**
     * Returns true if the user is the provider of the sample, or if the user is
     * associated with the lab where the sample originated or if the contxt
     * parameter 'restrictLargeFilesAvailability' is not equal to true.
     * 
     * @param userInfo the user to perform the check on
     * @param sample the sample whose information is being viewed by the user
     * @param servletContext the {@code ServletContext} from which web.xml
     *        parameters may be read
     * @return true if user is authorized to view large files of the sample else
     *         false
     */
    public static boolean canRequestUnavailableFiles(UserInfo userInfo,
            SampleInfo sample, ServletContext servletContext) {
        /*
         * Returns true if the user is provider of the sample or user is
         * aassociated with the same lab where the sample originated
         */
        return (userInfo != null) && (sample != null)
                && ((isProviderUser(userInfo)
                        && (userInfo.providerId == sample.mostRecentProviderId))
                        || (isLabUser(userInfo) && (userInfo.labId == sample.labId))
                        || !servletContext.getInitParameter(
                                "restrictLargeFilesAvailability").equals("true"));
    }

    /**
     * Determines whether the user is permitted to see the lab summary page.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is a lab user
     */
    public static boolean canSeeLabSummary(UserInfo userInfo) {
        return isLabUser(userInfo);
    }

    /**
     * Determines whether the user is permitted to see a list of providers for a
     * given lab.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is a lab user and a member of the lab given
     */
    public static boolean canSeeProviderListForLab(UserInfo userInfo, int labId) {
        return isLabUser(userInfo) && (userInfo.labId == labId);
    }

    /**
     * Determines whether the user can filter search results by their own
     * provider group.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is a provider user
     */
    public static boolean canFilterResultsByOwnProvider(UserInfo userInfo) {
        return isProviderUser(userInfo);
    }

    /**
     * Determines whether the given access level user is valid for the given
     * user on the given sample. This does NOT neccessarily indicate that the
     * access level is the current access level, just that it is possible.
     * 
     * @param accessLevel one of the access level constants defined on
     *        {@code SampleAccessInfo}
     * @param userInfo the user that would access the sample - may not be null
     * @param sampleInfo the sample for which accessibility is being checked -
     *        may not be null
     * @throws IllegalArgumentException if 'accessLevel' is not a valid access
     *         level as defined by {@code SampleAccessInfo}
     */
    public static boolean isAccessLevelValid(int accessLevel,
            UserInfo userInfo, SampleInfo sampleInfo) {
        switch (accessLevel) {
            case SampleAccessInfo.INVALID_ACCESS:
                return !(sampleInfo.isPublic()
                        || (userInfo.providerId == sampleInfo.mostRecentProviderId)
                        || (userInfo.labId == sampleInfo.labId));
            case SampleAccessInfo.READ_ONLY:
                return !(userInfo.labId == sampleInfo.labId);
            case SampleAccessInfo.READ_WRITE:
                return true;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the access level for a given user on a given sample to the provided
     * value. The provided {@code SampleInfo} object will be modified to reflect
     * the new level.
     * 
     * @param accessLevel one of the access level constants defined on
     *        {@code SampleAccessInfo}
     * @param userInfo the user that would access the sample - may not be null
     * @param sampleInfo the sample for which accessibility is being checked -
     *        may not be null
     * @throws IllegalArgumentException if 'accessLevel' is not a valid access
     *         level as defined by {@code SampleAccessInfo} or is not a valid
     *         access level for the provided user on the provided sample.
     */
    public static void setAccessLevel(int accessLevel, UserInfo userInfo,
            SampleInfo sampleInfo) {
        if (!isAccessLevelValid(accessLevel, userInfo, sampleInfo)) {
            throw new IllegalArgumentException();
        }

        // find the access info record if it exists
        SampleAccessInfo accessInfo = null;

        for (SampleAccessInfo sai : sampleInfo.accessInfo) {
            if (sai.userId == userInfo.id) {
                accessInfo = sai;
                break;
            }
        }

        switch (accessLevel) {
            case SampleAccessInfo.INVALID_ACCESS:
                /*
                 * this user has been set to have the lowest access level if any
                 * entry for this user is in the ACL, it should be removed
                 */
                if (accessInfo != null) {
                    sampleInfo.accessInfo.remove(accessInfo);
                } else {
                    // do nothing if missing
                }
                break;
            case SampleAccessInfo.READ_ONLY:
                if (sampleInfo.isPublic()
                        || ((sampleInfo.labId == userInfo.labId)
                                && (userInfo.labId != LabInfo.INVALID_LAB_ID))
                        || ((sampleInfo.mostRecentProviderId == userInfo.providerId)
                                && (userInfo.providerId != ProviderInfo.INVALID_PROVIDER_ID))) {
                    /*
                     * this user has been set to have the lowest possible access
                     * level for this user on this sample so the ACL entry may
                     * be removed if it exists
                     */
                    if (accessInfo != null) {
                        // remove record if present
                        sampleInfo.accessInfo.remove(accessInfo);
                    } else {
                        // do nothing if missing
                    }
                } else {
                    if (accessInfo != null) {
                        // update the access level if present
                        accessInfo.accessLevel = accessLevel;
                    } else {
                        // add a new access level record if missing
                        accessInfo = new SampleAccessInfo(userInfo.id,
                                accessLevel);
                        sampleInfo.accessInfo.add(accessInfo);
                    }
                }
                break;
            case SampleAccessInfo.READ_WRITE:
                if (userInfo.labId == sampleInfo.labId) {
                    /*
                     * this user has been set to have the lowest possible access
                     * level for this user on this sample so the ACL entry may
                     * be removed if it exists
                     */
                    if (accessInfo != null) {
                        // remove record if present
                        sampleInfo.accessInfo.remove(accessInfo);
                    } else {
                        // do nothing if missing
                    }
                } else {
                    if (accessInfo != null) {
                        // update the access level if present
                        accessInfo.accessLevel = accessLevel;
                    } else {
                        // add a new access level record if missing
                        accessInfo = new SampleAccessInfo(userInfo.id,
                                accessLevel);
                        sampleInfo.accessInfo.add(accessInfo);
                    }
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the access level for a particular user on a particular sample. This
     * method should not be called to determine visibility of a sample or for
     * use in any comparisons. Instead methods such as {@code canEditSample()}
     * or {@code canSeeSample()} should be used. This method may be used by
     * {@code SampleAccessSelector} to set its value.
     * 
     * @param userInfo the user whose access level is being checked
     * @param sampleInfo the sample to be accessed
     * @return the access level as defined by {@code SampleAccessInfo}, that
     *         the provided user has on the provided sample
     * @throws IllegalArgumentException if 'sampleInfo' is null
     */
    public static int getUserAccessLevelForSample(UserInfo userInfo,
            SampleInfo sampleInfo) {
        if (canEditSample(userInfo, sampleInfo)) {
            return SampleAccessInfo.READ_WRITE;
        } else if (canSeeSample(userInfo, sampleInfo)) {
            return SampleAccessInfo.READ_ONLY;
        } else {
            return SampleAccessInfo.INVALID_ACCESS;
        }
    }

    /**
     * Determines whether the user account is associated with a lab (and
     * therefore not a provider). This method may be used internally to
     * determine whether a user may perform a particular action, but when
     * another class must determine something more specific (though possibly
     * identical in value) than whether the user is a lab user, it must call
     * methods like {@code canSeeLabSummary()} or {@code canSubmitSamples()}.
     * {@code SampleChecker} on the other hand can be used simply to check the
     * affiliation, and may therefore call {@code isLabUser()} or
     * {@code isProviderUser()}.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is not null and they are assigned to a lab.
     */
    public static boolean isLabUser(UserInfo userInfo) {
        return (userInfo != null) && (userInfo.labId != LabInfo.INVALID_LAB_ID);
    }

    /**
     * Determines whether the user account is associated with a provider (and
     * therefore not a lab). This method may be used internally to determine
     * whether a user may perform a particular action, but when another class
     * must determine something more specific (though possibly identical in
     * value) than whether the user is a lab user, it must call methods like
     * {@code canFilterResultsByOwnProvider()}. {@code SampleChecker} on the
     * other hand can be used simply to check the affiliation, and may therefore
     * call {@code isLabUser()} or {@code isProviderUser()}.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is not null and they are assigned to a provider.
     */
    public static boolean isProviderUser(UserInfo userInfo) {
        return (userInfo != null)
                && (userInfo.providerId != ProviderInfo.INVALID_PROVIDER_ID);
    }

    /**
     * Determines whether the user specified by the 'userInfo' parameter can
     * edit at least one aspect of the 'proposedUser'. If 'userInfo' is null,
     * this method will return false; 'proposedUser' may not be null. This
     * method will return true if any of the following method would return true:
     * {@code canChangeUserPassword()}, {@code canDeactivateUser()},
     * {@code canChangeUserPreferences()} or {@code canEditUser()}.
     * 
     * @param userInfo the user to perform the check upon
     * @param proposedUser the user that is to be edited
     * @param proposedUsersProvider the provider associated with the proposed
     *        user if he/she is a provider user or null if he/she is a lab user
     * @return true if the user specified by the 'userInfo' param may edit some
     *         aspect of the user specified by 'proposedUser'
     */
    public static boolean canEditSomethingAboutUser(UserInfo userInfo,
            UserInfo proposedUser, ProviderInfo proposedUsersProvider) {
        if (userInfo == null) {
            // anonymous changes to users are forbidden
            return false;
        }
        /*
         * if any action can be performed on the sample, then the password can
         * be changed, therefore this method may delegate to
         * canChangeUserPassword()
         */
        return canChangeUserPassword(userInfo, proposedUser,
                proposedUsersProvider);
    }

    /**
     * Determines whether the user specified by the 'userInfo' parameter can
     * change the password of the 'proposedUser'. If 'userInfo' is null, this
     * method will return false; 'proposedUser' may not be null.
     * 
     * @param userInfo the user to perform the check upon
     * @param proposedUser the user whose password is to be changed
     * @param proposedUsersProvider the provider associated with the proposed
     *        user if he/she is a provider user or null if he/she is a lab user
     * @return true if the user specified by the 'userInfo' param may change the
     *         password of the user specified by 'proposedUser'
     */
    public static boolean canChangeUserPassword(UserInfo userInfo,
            UserInfo proposedUser, ProviderInfo proposedUsersProvider) {
        if (userInfo == null) {
            // anonymous changes to user passwords are forbidden
            return false;
        }
        return (canEditUser(userInfo, proposedUser, proposedUsersProvider)
                || (userInfo.id == proposedUser.id));

    }

    /**
     * Determines whether the user specified by the 'userInfo' parameter can
     * change the user preferences of the 'proposedUser'. The current
     * implementation enforces identical requirements as
     * {@code canChangeUserPassword()}.
     * 
     * @param userInfo the user to perform the check upon
     * @param proposedUser the user whose preferences are to be changed
     * @param proposedUsersProvider the provider associated with the proposed
     *        user if he/she is a provider user or null if he/she is a lab user
     * @return true if the user specified by the 'userInfo' param may change the
     *         preferences of the user specified by 'proposedUser'
     */
    public static boolean canChangeUserPreferences(UserInfo userInfo,
            UserInfo proposedUser, ProviderInfo proposedUsersProvider) {
        return canChangeUserPassword(userInfo, proposedUser,
                proposedUsersProvider);
    }

    /**
     * Determines whether the given user may change his/her own preferences.
     * 
     * @param userInfo a {@code UserInfo} representing the user to perform the
     *        check upon
     * @return {@code true} if the specified user may change his own
     *         preferences, {@code false if not}; this version always returns
     *         {@code true}
     */
    public static boolean canChangeOwnPreferences(@SuppressWarnings("unused")
    UserInfo userInfo) {
        return true;
    }

    /**
     * Determines whether the user specified by the 'userInfo' parameter can
     * deactivate the 'proposedUser'.
     * 
     * @param userInfo the user to perform the check upon
     * @param proposedUser the user to be deactivated
     * @param proposedUsersProvider the provider associated with the proposed
     *        user if he/she is a provider user or null if he/she is a lab user
     * @return true if the user specified by the 'userInfo' param may deactivate
     *         the user specified by 'proposedUser'
     */
    public static boolean canDeactivateUser(UserInfo userInfo,
            UserInfo proposedUser, ProviderInfo proposedUsersProvider) {
        return (canEditUser(userInfo, proposedUser, proposedUsersProvider)
                && (userInfo.id != proposedUser.id));
    }

    /**
     * Updates a {@code SearchParams} to ensure that it does not return any
     * results that may not be viewed by the given user.
     * 
     * @param userInfo the user that wishes to perform the search, may be null
     *        for unknown/unauthenticated users
     */
    public static void blessSearchParams(SearchParams sp, UserInfo userInfo) {
        sp.addToHeadWithAnd(new AuthorizationSC(userInfo));
    }

    /**
     * Determines whether the user is permitted to set a sample's originating
     * provider to a given value.
     * 
     * @param userInfo the user to perform the check on
     * @param sampleInfo the sample whose provider is being updated
     * @param samplesProvider the provider to which the user intends to
     *        associate the sample
     * @return true if the user may set the sample's provider to the given value
     * @throws IllegalArgumentException if
     *         {@code sampleInfo.dataInfo.providerId} does not match
     *         {@code samplesProvider.id}
     */
    public static boolean canSetSamplesProvider(UserInfo userInfo,
            SampleInfo sampleInfo, ProviderInfo samplesProvider) {
        if (sampleInfo.dataInfo.providerId != samplesProvider.id) {
            throw new IllegalArgumentException();
        }
        if (isProviderUser(userInfo)
                && (userInfo.providerId == samplesProvider.id)
                && ((userInfo.globalAccessLevel
                        & UserInfo.SUBMITTING_PROVIDER_ACCESS) != 0)) {
            return true;
        } else if (isLabUser(userInfo)
                && (userInfo.labId == samplesProvider.labId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Updates a {@code SearchParams} to undo a previous call to
     * {@code blessSearchParams()} and redo it for the provided user instead of
     * whichever user was supplied before. If no previous calls to
     * {@code blessSearchParams()} have been made with {@code sp} as an
     * argument, this method is equivalent to {@code blessSearchParams()}.
     * 
     * @param userInfo the user that wishes to perform the search, may be null
     *        for unknown/unauthenticated users
     */
    public static void reblessSearchParams(SearchParams sp, UserInfo userInfo) {
        SearchConstraint head = sp.getHead();

        if (head == null) {
            // no SearchConstraints; add the AuthorizationSC
            sp.setHead(new AuthorizationSC(userInfo));
        } else if (head.getClass() == AuthorizationSC.class) {
            // only an AuthorizationSC; replace it
            sp.setHead(new AuthorizationSC(userInfo));
        } else if (head.getClass() == SearchConstraintGroup.class) {
            /*
             * remove any (all) AuthorizationSC's in the SearchConstraint tree
             * and then bless what's left
             */
            sp.setHead(revokeAllBlessings((SearchConstraintGroup) head));
            blessSearchParams(sp, userInfo);
        } else {
            /*
             * there is no AuthorizationSC objects that are members of normal
             * SearchConstraintGroups; bless this apparently unblessed
             * SearchParams
             */
            sp.addToHeadWithAnd(new AuthorizationSC(userInfo));
        }
    }

    /**
     * A recursive helper method that removes every {@code AuthorizationSC} from
     * a {@code SearchConstraintGroup} Because {@code SearchConstraintGroup}
     * objects are immutable this method returns a replacement
     * {@code SearchConstraintGroup} for the one passed to it if any
     * modifications needed to be made.
     * 
     * @param scg a {@code SearchConstraintGroup} that is the head of a tree or
     *        subtree
     * @return a {@code SearchConstraintGroup} identical to the supplied one
     *         except that it no longer contains any {@code AuthorizationSC}
     *         objects. In cases where no changes were needed, a reference to
     *         the passed 'scg' is returned.
     */
    private static SearchConstraintGroup revokeAllBlessings(
            SearchConstraintGroup scg) {
        if (scg == null) {
            return scg;
        }
        Collection<SearchConstraint> unblessedChildren
                = new ArrayList<SearchConstraint>();
        boolean haveChildrenChanged = false;

        for (SearchConstraint sc : scg.getChildren()) {
            if (sc.getClass() == AuthorizationSC.class) {
                // a 'blessing'; don't add it to the unblessedChildren list
                haveChildrenChanged = true;
            } else if (sc.getClass() == SearchConstraintGroup.class) {
                SearchConstraint unholySC
                        = revokeAllBlessings((SearchConstraintGroup) sc);

                if (unholySC != sc) {
                    // the child group must be replaced
                    haveChildrenChanged = true;
                }
                unblessedChildren.add(unholySC);
            } else {
                // some irrelevant SearchConstraint; save it
                unblessedChildren.add(sc);
            }
        }

        return (haveChildrenChanged ? new SearchConstraintGroup(
                scg.getOperator(), unblessedChildren) : scg);
    }

    /**
     * Internal helper; determines whether the user is a laboratory
     * administrator.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is not null and they have the
     *         {@code UserInfo.LAB_ADMIN_ACCESS} right.
     */
    private static boolean isLabAdmin(UserInfo userInfo) {
        return (userInfo != null)
                && ((userInfo.globalAccessLevel & UserInfo.LAB_ADMIN_ACCESS) != 0);
    }

    /**
     * Internal helper; determines whether the user is a site administrator.
     * 
     * @param userInfo the user to perform the check on
     * @return true if the user is not null and they have the
     *         {@code UserInfo.SITE_ADMIN_ACCESS} right.
     */
    private static boolean isSiteAdmin(UserInfo userInfo) {
        return (userInfo != null)
                && ((userInfo.globalAccessLevel & UserInfo.SITE_ADMIN_ACCESS) != 0);
    }
}
