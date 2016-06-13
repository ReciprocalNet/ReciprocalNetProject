/*
 * Reciprocal Net project
 * 
 * NumberComparisonSC.java
 *
 * 25-Jan-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

/**
 * An abstract class that extends {@code SearchConstraint} to include basic
 * constants and helper methods associated with number comparisons.
 */
public abstract class NumberComparisonSC extends SearchConstraint {

    /**
     * An operator requiring that the value in the database be exactly equal to
     * the provided value in order for it to be included in the search results.
     */
    public static final int EQUALS = 100;

    /**
     * An operator requiring that the value in the database be greater than the
     * provided value in order for it to be included in the search results.
     */
    public static final int GREATER_THAN = 101;

    /**
     * An operator indicating that the value in the database be greater than or
     * equal to the provided value in order for it to be included in the search
     * results.
     */
    public static final int GREATER_THAN_OR_EQUAL_TO = 102;

    /**
     * An operator requiring that the value in the database be less than the
     * provided value in order for it to be included in the search results.
     */
    public static final int LESS_THAN = 103;

    /**
     * An operator indicating that the value in the database be lessthan or
     * equal to the provided value in order for it to be included in the search
     * results.
     */
    public static final int LESS_THAN_OR_EQUAL_TO = 104;

    /**
     * A convenience method for use by subclasses that Converts an operator code
     * into a {@code String} that represents the operator in SQL syntax.
     * 
     * @param operator one fo the statically defined operator codes
     * @return a {@code String} representing the given operator with no trailing
     *         or leading spaces (ie, "=", ">", ...)
     * @throws IllegalArgumentException if an unknown operator code is supplied
     */
    protected static String convertOperatorToString(int operator) {
        switch (operator) {
            case EQUALS:
                return "=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUAL_TO:
                return ">=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUAL_TO:
                return "<=";
            default:
                throw new IllegalArgumentException();
        }
    }
}
