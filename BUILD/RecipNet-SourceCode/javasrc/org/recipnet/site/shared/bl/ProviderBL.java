/*
 * Reciprocal Net Project
 * @(#) ProviderBL.java
 *
 * 10-Jun-2005: midurbin wrote first draft
 */

package org.recipnet.site.shared.bl;

import org.recipnet.site.shared.db.ProviderInfo;

/**
 * This class defines all the business logic related to provider actions and
 * the manipulation of {@link org.recipnet.site.shared.db.ProviderInfo
 * ProviderInfo} objects.
 */
public final class ProviderBL {

    /**
     * Deactivates the specified provider.
     * @param provider the <code>ProviderInfo</code> for the provider to be
     *     deactivated
     */
    public static void deactivateProvider(ProviderInfo provider) {
        provider.isActive = false;
    }
}
