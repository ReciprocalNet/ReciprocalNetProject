/*
 * Reciprocal Net project
 * 
 * SpaceGroupSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 12-Jul-2005: ekoperda modified class to search on canonical space groups
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL;

/**
 * A {@code SearchConstraint} to limit search results to those samples that have
 * the specified space group. The "raw" space group symbols entered as search
 * criteria and stored in the 'sampleData' table are not matched against one
 * another directly because precise space group symbol syntax may vary from one
 * crystallographer to another. Instead, the "canonical form" of the raw
 * criteria symbol and the "canonical form" of the samples' stored symbols are
 * matched. The job of converting a raw space group symbol to a canonical one is
 * delegated to {@code SpaceGroupSymbolBL}. This class makes use of the search
 * index table called 'searchSpaceGroups' that {@code SampleManager} maintains
 * in order to improve search performance.
 */
public class SpaceGroupSC extends SearchConstraint {
    private final String rawSymbol;

    private final String canonicalSymbol;

    /**
     * A constructor that fully initializes a {@code SpaceGroupSC}. The
     * specified {@code rawSymbol} is converted to canonical form. The canonical
     * form is used later, in a WHERE clause, as a search key into the
     * 'searchSpaceGroups' table.
     * 
     * @param rawSymbol the raw space group symbol that is used as the basis for
     *        the search key. It must not be null.
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         provided space group symbol does not represent a valid space
     *         group
     */
    public SpaceGroupSC(String rawSymbol) throws InvalidDataException {
        this.rawSymbol = rawSymbol;
        String formattedSymbol
                = SpaceGroupSymbolBL.createFormattedSymbol(rawSymbol);
        this.canonicalSymbol
                = SpaceGroupSymbolBL.createCanonicalSymbol(formattedSymbol);
    }

    /** simple getter */
    public String getRawSymbol() {
        return this.rawSymbol;
    }

    /** @inheritDoc */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        String tableAlias
                = tableTracker.getTableAlias("searchSpaceGroups", this);
        StringBuilder sb = new StringBuilder(64);
        
        sb.append(tableAlias);
        sb.append(".canonicalsymbol = ?");
        parameters.add(this.canonicalSymbol);
        
        return sb.toString();
    }

    /** @inheritDoc */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if ((obj == null) || (this.getClass() != obj.getClass())) {
            return false;
        } else {
            return this.rawSymbol.equals(((SpaceGroupSC) obj).rawSymbol);
        }
    }

    /** @inheritDoc */
    @Override
    public int hashCode() {
        return this.rawSymbol.hashCode();
    }
}
