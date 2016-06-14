/*
 * Reciprocal Net project
 * 
 * IdNumberSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

/**
 * A {@code SearchConstraint} to limit search results to those samples whose
 * sampleId matches the given 'idBeginning' or whose localLabId begins with the
 * given 'idBeginning'.
 */
public class IdNumberSC extends SearchConstraintGroup {

    /**
     * This reference to the 'idBeginning' supplied to the constructor is to
     * simplify the method {@code getIdBeginning()}.
     */
    private final String idBeginning;

    /**
     * A constructor that fully initializes an {@code IdNumberSC}. The current
     * implementation dictates that this {@code SearchConstraintGroup} subclass
     * is an 'OR' group that contains individual {@code SearchConstraint}
     * objects.
     * 
     * @param idBeginning a {@code String} representing either a sample id or
     *        the beginning of a sample's localLabId
     */
    public IdNumberSC(String idBeginning) {
        super(OR);
        this.idBeginning = idBeginning;
        try {
            addChild(new SampleIdSC(Integer.parseInt(idBeginning)));
        } catch (NumberFormatException nfe) {
            // the provided value cannot be a SampleId as it is not an integer
            // fall through and consider it as a LocalLabId
        }
        addChild(new LocalLabIdSC(idBeginning,
                TextComparisonSC.MATCHES_BEGINNING));
    }

    /** @return the 'idBeginning' supplied to the constructor. */
    public String getIdBeginning() {
        return this.idBeginning;
    }

    /** Equality is based on class and idBeginning. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof IdNumberSC) {
            IdNumberSC gsisc = (IdNumberSC) obj;
            return ((this.getClass() == gsisc.getClass())
                    && (((this.idBeginning == null) && (gsisc.idBeginning == null))
                            || ((this.idBeginning != null)
                                    && this.idBeginning.equals(
                                            gsisc.idBeginning))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass()) + this.idBeginning).hashCode();
    }
}
