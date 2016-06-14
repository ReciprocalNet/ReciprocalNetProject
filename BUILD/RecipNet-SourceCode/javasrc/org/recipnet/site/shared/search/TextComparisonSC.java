/*
 * Reciprocal Net project
 * 
 * TextComparisonSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added getStringMatch()
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

/**
 * An abstract class that extends {@code SearchConstraint} to include basic
 * constants and helper methods associated with text comparisons.
 */
public abstract class TextComparisonSC extends SearchConstraint {

    /**
     * An operator indicating that the provided value is an exact
     * (case-insensitive) match with the value in the database.
     */
    public static final int MATCHES_WHOLE = 200;

    /**
     * An operator indicating that the provided value matches (case-insensitive)
     * the beginning part of the value in the database.
     */
    public static final int MATCHES_BEGINNING = 201;

    /**
     * An operator indicating that the provided value matches (case-insensitive)
     * some part of the value in the database.
     */
    public static final int MATCHES_PART = 202;

    /**
     * A convenience method for use by subclasses that converts an operator code
     * into a {@code String} that represents the operator in SQL syntax. When
     * this method is used to generate an SQL fragment,
     * {@code convertRawValueToExpression()} should be used to generate the
     * corresponding parameter.
     * 
     * @param operator one of the statically defined operator codes
     * @return a {@code String} representing the given operator with no trailing
     *         or leading spaces (ie "=", "LIKE")
     * @throws IllegalArgumentException if an unknown operator code is supplied
     */
    protected static String convertOperatorToString(int operator) {
        switch (operator) {
            case MATCHES_WHOLE:
                return "=";
            case MATCHES_BEGINNING:
            case MATCHES_PART:
                return "LIKE";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * A convenience method for use by subclasses that sanitizes and updates the
     * raw value to reflect the operator. In the case of a direct comparision
     * ({@code MATCHES_WHOLE}) this method will return the raw value. In other
     * cases, a general expression to work with the SQL LIKE operator is
     * returned.
     */
    protected static String convertRawValueToExpression(String raw,
            int operator) {
        switch (operator) {
            case MATCHES_WHOLE:
                /*
                 * the '=' will be used rather than any taxing regular
                 * expression comparison
                 */
                return raw;
            case MATCHES_BEGINNING:
                return (sanitizeForLike(raw) + "%");
            case MATCHES_PART:
                return ("%" + sanitizeForLike(raw) + "%");
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * A helper function that determines whether two strings match according to
     * the given operator. This method does it's comparison without regard to
     * capitalization.
     * 
     * @param operator the operator use to compare the strings
     * @param searchString the {@code String} that is being searched for
     * @param containerString the string that is the value of the field from the
     *        sample; must match the 'searchString' for this value to return a
     *        non-null value
     */
    protected static FieldMatchInfo.MatchingPart getStringMatch(
            String searchString, String containerString, int operator) {
        String containerStringLower = containerString.toLowerCase();
        String searchStringLower = searchString.toLowerCase();
        switch (operator) {
            case MATCHES_WHOLE:
                if (searchStringLower.equals(containerStringLower)) {
                    return new FieldMatchInfo.MatchingPart();
                }
                return null;
            case MATCHES_BEGINNING:
                if (containerStringLower.startsWith(searchStringLower)) {
                    return new FieldMatchInfo.MatchingPart(0,
                            searchString.length());
                }
                return null;
            case MATCHES_PART:
                int index = containerStringLower.indexOf(searchStringLower);
                if (index != -1) {
                    return new FieldMatchInfo.MatchingPart(index,
                            searchString.length());
                }
                return null;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Helper function that returns a sanitized version of the specified string
     * that can be included as a static value inside a SQL 'LIKE' argument
     * without unexpected behavior. The current implementation escapes the '%'
     * and '_' characters.
     */
    private static String sanitizeForLike(String raw) {
        return raw.replaceAll("\\%", "\\\\%").replaceAll("\\_", "\\\\_");
    }
}
