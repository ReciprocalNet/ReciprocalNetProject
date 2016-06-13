/*
 * Reciprocal Net project
 * 
 * AnnotationSC.java
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
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A {@code TextComparisonSC} to limit search results to those samples with
 * annotations of a given type having a given value.
 */
public class AnnotationSC extends TextComparisonSC {

    /**
     * The value (or value fragment) required by this {@code AnnotationSC}.
     */
    private final String value;

    /**
     * The operator that relates the provided 'value' with values stored in the
     * database. This must be set to one of the constant operator codes defined
     * on {@code TextComparisonSC}.
     */
    private final int operator;

    /** The type of annotation that must have the given 'value'. */
    private final int textType;

    /**
     * A constructor that fully initializes a {@code AnnotationSC}.
     * 
     * @param textType the text type of the annotation; must be a valid
     *        annotation type as defined by {@code SampleTextBL}.
     * @param value the {@code String} value (or value fragment) required to be
     *        the value for an annotation of the provided type
     * @param operator one of the operator codes defined on
     *        {@code TextComparisonSC}
     * @throws IllegalArgumentException if 'textType' is not a valid annotation
     *         type
     */
    public AnnotationSC(int textType, String value, int operator) {
        if (!SampleTextBL.isAnnotation(textType)) {
            throw new IllegalArgumentException();
        }
        this.textType = textType;
        this.value = value;
        this.operator = operator;
    }

    /** Gets the annotation value that was supplied to the constructor. */
    public String getAnnotationValue() {
        return this.value;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation generates
     * a {@code String} that may be used as a portion of the SQL WHERE clause to
     * require that the sample have an annotation of the given 'textType' with
     * the given 'value'.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        parameters.add(Integer.valueOf(textType));
        parameters.add(convertRawValueToExpression(this.value, this.operator));
        String annotationTable = tableTracker.getTableAlias(
                "sampleAnnotations", this);
        return "( " + annotationTable + ".type = ? AND " + annotationTable
                + ".value " + convertOperatorToString(operator) + " ?)";
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation goes
     * through all the annotations on the sample and returns true when a match
     * is found (adding that match the the 'matches' collection) or false when
     * all annotations have been compared and no matches were found.
     */
    @Override
    public boolean getMatches(SampleInfo sample,
            Collection<FieldMatchInfo> matches, @SuppressWarnings("unused")
            Collection<FieldMatchInfo> mismatches) {
        for (SampleAnnotationInfo anno : sample.annotationInfo) {
            if (anno.type == this.textType) {
                FieldMatchInfo.MatchingPart stringMatch
                        = TextComparisonSC.getStringMatch(
                                this.value, anno.value, this.operator);
                
                if (stringMatch != null) {
                    // this constraint has been met by the current annotation
                    matches.add(new FieldMatchInfo(anno, stringMatch, this,
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
        } else if (obj instanceof AnnotationSC) {
            AnnotationSC asc = (AnnotationSC) obj;

            return ((this.getClass() == asc.getClass())
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
        return new String(String.valueOf(getClass())
                + String.valueOf(this.textType) + String.valueOf(this.operator)
                + this.value).hashCode();
    }
}
