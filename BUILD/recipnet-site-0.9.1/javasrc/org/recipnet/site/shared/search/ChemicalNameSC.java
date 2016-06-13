/*
 * Reciprocal Net project
 * 
 * ChemicalNameSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples that have
 * one or more chemical names that contain the given name fragment.
 */
public class ChemicalNameSC extends SearchConstraintGroup {

    /**
     * This reference to the 'nameFragment' supplied to the constructor is to
     * simplify the method {@code getNameFragment()}.
     */
    private final String nameFragment;

    /**
     * A constructor that fully initializes a {@code ChemicalNameSC}. The
     * current implementation dictates that this {@code SearchConstraintGroup}
     * subclass is an 'OR' group that contains other {@code SearchConstraints}
     * that compare the given 'nameFragment' against IUPAC names, common names
     * and trade names.
     */
    public ChemicalNameSC(String nameFragment) {
        super(OR);
        this.nameFragment = nameFragment;
        addChild(new AnnotationSC(SampleTextBL.IUPAC_NAME, nameFragment,
                TextComparisonSC.MATCHES_PART));
        addChild(new AttributeSC(SampleTextBL.COMMON_NAME, nameFragment,
                TextComparisonSC.MATCHES_PART));
        addChild(new AttributeSC(SampleTextBL.TRADE_NAME, nameFragment,
                TextComparisonSC.MATCHES_PART));
    }

    /** @return the 'nameFragment' supplied to the constructor. */
    public String getNameFragment() {
        return this.nameFragment;
    }

    /** Equality is based on class and nameFragment. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ChemicalNameSC) {
            ChemicalNameSC cnsc = (ChemicalNameSC) obj;

            return ((this.getClass() == cnsc.getClass())
                    && (((this.nameFragment == null) && (cnsc.nameFragment == null))
                            || ((this.nameFragment != null)
                                    && this.nameFragment.equals(
                                            cnsc.nameFragment))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(this.getClass()) + this.nameFragment).hashCode();
    }
}
