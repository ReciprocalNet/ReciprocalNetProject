/*
 * Reciprocal Net project
 * 
 * LocalLabIdSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */
package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A {@code TextComparisonSC} to limit search results to
 * those samples with localLabId's that are equal or similar to the given
 * 'localLabIdFragment'.
 */
public class LocalLabIdSC extends TextComparisonSC {

    /** A Sample's localLabId, or localLabId fragment for comparison. */
    private final String localLabIdFragment;

    /**
     * The operator that relates the provided 'localLabIdFragment' with
     * values stored in the database.  This must be set to one of the constant
     * operator codes defined on {@code TextComparisonSC}.
     */
    private final int operator;

    /**
     * A constructor that fully initializes a {@code LocalLabIdSC}.
     */
    public LocalLabIdSC(String localLabIdFragment, int operator) {
        this.localLabIdFragment = localLabIdFragment;
        this.operator = operator;
    }

    /**
     * @return the 'localLabIdFragment' that was supplied to the constructor.
     */
    public String getLocalLabIdFragment() {
        return this.localLabIdFragment;
    }

    public int getOperator() {
        return this.operator;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation
     * generates a {@code String} that may be used as a portion of the SQL
     * WHERE clause to require that a sample have the given id value.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters,
            @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
        parameters.add(convertRawValueToExpression(
                this.localLabIdFragment, this.operator));
        return tableTracker.getTableAlias("samples", this) + ".localLabId "
                + convertOperatorToString(operator) + " ?";
    }

    /** Equality is based on class, localLabIdFragment and operator. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof LocalLabIdSC) {
            LocalLabIdSC llisc = (LocalLabIdSC) obj;
            return ((this.getClass() == llisc.getClass())
                    && (this.operator == llisc.operator)
                    && (((this.localLabIdFragment == null)
                          && (llisc.localLabIdFragment == null))
                      || ((this.localLabIdFragment != null)
                          && this.localLabIdFragment.equals(
                              llisc.localLabIdFragment))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + String.valueOf(this.operator)
                + this.localLabIdFragment).hashCode();
    }
}
