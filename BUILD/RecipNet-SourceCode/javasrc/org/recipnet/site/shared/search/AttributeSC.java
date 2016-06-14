/*
 * Reciprocal Net project
 * 
 * AttributeSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added getMatches()
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.Collection;
import java.util.List;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A {@code TextComparisonSearchConstraint} to limit search results to those
 * samples with attributes of a given type having a given value.
 */
public class AttributeSC extends TextComparisonSC {

    /**
     * The value (or value fragment) required by this {@code AttributeSC}.
     */
    private final String value;

    /**
     * The operator that relates the provided 'value' with values stored in the
     * database. This must be set to one of the constant operator codes defined
     * on {@code TextComparisonSC}.
     */
    private final int operator;

    /** The type of attribute that must have the given 'value'. */
    private final int textType;

    /**
     * A constructor that fully initializes a {@code AttributeSC}.
     * 
     * @param textType the text type of the attribute; must be a valid attribute
     *        type as defined by {@code SampleTextBL}.
     * @param value the {@code String} value (or value fragment) required to be
     *        the value for an attribute of the provided type
     * @param operator one of the operator codes defined on
     *        {@code TextComparisonSC}
     * @throws IllegalArgumentException if 'textType' is not a valid attribute
     *         type
     */
    public AttributeSC(int textType, String value, int operator) {
        if (!SampleTextBL.isAttribute(textType)) {
            throw new IllegalArgumentException();
        }
        this.textType = textType;
        this.value = value;
        this.operator = operator;
    }

    /** Gets the value that was set during the constructor. */
    public String getAttributeValue() {
        return this.value;
    }

    /**
     * {@inheritDoc}; this version generates a {@code String} that may be used
     * as a portion of the SQL WHERE clause to require that the sample have an
     * attribute of the given 'textType' with the given 'value'.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters,
            @SuppressWarnings("unused") SearchConstraintExtraInfo scei) {
        String attributeTable = tableTracker.getTableAlias("sampleAttributes",
                this);

        parameters.add(Integer.valueOf(textType));
        parameters.add(convertRawValueToExpression(this.value, this.operator));

        return "( " + attributeTable + ".type = ? AND " + attributeTable
                + ".value " + convertOperatorToString(operator) + " ?)";
    }

    /**
     * {@inheritDoc}; this version goes through all the attributes on the
     * sample and returns true when a match is found (adding that match the the
     * 'matches' collection) or false when all attributes have been compared and
     * no matches were found.
     */
    @Override
    public boolean getMatches(SampleInfo sample,
            Collection<FieldMatchInfo> matches,
            @SuppressWarnings("unused") Collection<FieldMatchInfo> mismatches) {
        for (SampleAttributeInfo attr : sample.attributeInfo) {
            if (attr.type == this.textType) {
                FieldMatchInfo.MatchingPart stringMatch
                        = TextComparisonSC.getStringMatch(
                                this.value, attr.value, this.operator);
                
                if (stringMatch != null) {
                    // this constraint has been met by the current attribute
                    matches.add(new FieldMatchInfo(attr, stringMatch, this,
                            sample));
                    return true;
                }
            }
        }
        return false;
    }

    /** Equality is based on class, textType, operation and value. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AttributeSC) {
            AttributeSC asc = (AttributeSC) obj;

            return ((getClass() == asc.getClass())
                    && (this.textType == asc.textType)
                    && (this.operator == asc.operator)
                    && (((this.value == null) && (asc.value == null))
                            || ((this.value != null)
                                    && this.value.equals(asc.value))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.getClass()) + String.valueOf(this.textType)
                + String.valueOf(this.operator) + this.value).hashCode();
    }
}
