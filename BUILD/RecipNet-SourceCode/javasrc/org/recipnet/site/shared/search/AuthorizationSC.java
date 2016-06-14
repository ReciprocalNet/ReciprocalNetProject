/*
 * Reciprocal Net project
 * 
 * AuthorizationSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A {@code SearchConstraint} to limit search results to those samples which the
 * given user may view.
 */
public class AuthorizationSC extends SearchConstraintGroup {

    /**
     * A constructor that fully initializes an {@code AuthorizationSC}. The
     * current implementation dictates that this {@code SearchConstraintGroup}
     * subclass is an 'OR' group that contains various conditions that may allow
     * a given sample to be viewed by the provided user.
     * <p>
     * These include:
     * <ul>
     * <li>The sample is in any of the four PUBLIC states.</li>
     * <li> The sample's ACL explicitly grants the given user READ privilages
     * </li>
     * <li>The sample belongs to the user's lab</li>
     * <li>The sample came from the user's provider</li>
     * </ul>
     */
    public AuthorizationSC(UserInfo userInfo) {
        super(OR);

        for (Integer sc : SampleWorkflowBL.getAllPublicStatusCodes()) {
            addChild(new StatusSC(sc.intValue()));
        }
        if (userInfo != null) {
            addChild(new AclSC(userInfo.id));
            if (userInfo.labId != LabInfo.INVALID_LAB_ID) {
                addChild(new LabSC(userInfo.labId));
            }
            if (userInfo.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
                addChild(new ProviderSC(userInfo.providerId));
            }
        }
    }

    /**
     * A {@code SearchConstraint} to limit search results to those samples for
     * which an ACL record grants the user with the given userId
     * {@code READ_ONLY} access or better.
     */
    private static class AclSC extends SearchConstraint {

        /** The userId of the user in question. */
        private final int userId;

        /**
         * A constructor to create a fully qualified {@code AclSC}.
         */
        public AclSC(int userId) {
            this.userId = userId;
        }

        /**
         * Overrides {@code SearchConstraint}; the current implementation
         * returns an SQL where clause frament requiring the presence of a
         * 'sampleAcls' table row where the 'user_id' matches that provided to
         * this class and the 'accessLevel' is greater than or equal to
         * {@code SampleAccessInfo.READ_ONLY}. The {@code userId} for this
         * class is added to the {@code parameters}.
         */
        @Override
        public String getWhereClauseFragment(SearchTableTracker tableTracker,
                List<Object> parameters,
                @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
            String aclTableAlias
                    = tableTracker.getTableAlias("sampleAcls", this);

            parameters.add(Integer.valueOf(userId));

            return "(" + aclTableAlias + ".user_id = ? AND " + aclTableAlias
                    + ".accessLevel >= " + SampleAccessInfo.READ_ONLY + ")";
        }

        /** Equality is based on userId and class. */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof AclSC) {
                AclSC asc = (AclSC) obj;

                return ((this.getClass() == asc.getClass())
                        && (this.userId == asc.userId));
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return new String(String.valueOf(getClass())
                    + String.valueOf(this.userId)).hashCode();
        }
    }
}
