/*
 * Reciprocal Net project
 * 
 * ProviderSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 06-Oct-2005: midurbin updated reference to "provider_id" column in the
 *              samples table to reflect DB update
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code SearchConstraint} to limit search results to those samples that came
 * from a given provider.
 */
public class ProviderSC extends SearchConstraint {

    /** An {@code Integer} representation of the id for the provider. */
    private int providerId;

    /**
     * A constructor that fully initializes a {@code ProviderSC}.
     */
    public ProviderSC(int providerId) {
        this.providerId = providerId;
    }

    /** @return the providerId that was provided to the constructor */
    public int getProviderId() {
        return this.providerId;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation returns a
     * where clause fragment comparing the sample table's "current_provider_id"
     * to the parameter ({@code providerId}) added to the {@code parameters}
     * {@code Collection}.
     * 
     * @param tableTracker needed to get an alias for the 'samples' table
     * @param parameters a {@code Collection} to which the providerId value is
     *        added
     * @return a {@code String} equating the sample's providerId with the labId
     *         assigned to this {@code ProviderSC}
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters,
            @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(providerId));
        
        return tableTracker.getTableAlias("samples", this)
                + ".current_provider_id = ?";
    }

    /** Equality is based on class and providerId. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ProviderSC) {
            return ((this.getClass() == obj.getClass())
                    && (this.providerId == ((ProviderSC) obj).providerId));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + String.valueOf(this.providerId)).hashCode();
    }
}
