/*
 * Reciprocal Net project
 * 
 * UnitCellSearchIndexSC.java
 *
 * 11-May-2005: ekoperda wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.List;

/**
 * A simple search constraint that matches a numeric value in an arbitrary
 * column of the database table {@code searchUnitCells}. This kind of search
 * constraint class is not terribly useful by itself and normally is wrapped
 * within a larger, more functional constraint.
 */
public class UnitCellSearchIndexSC extends SearchConstraint {
    private final String columnName;

    private final double value;

    private final double percentErrorTolerance;

    /**
     * Constructor.
     * 
     * @param columnName the name of the particular database column whose
     *        numeric values are to be matched.
     * @param value the desired numeric value.
     * @param percentErrorTolerance the maximum percentage by which a value in
     *        the database may differ from the desired value and still be
     *        considered a match.
     */
    public UnitCellSearchIndexSC(String columnName, double value,
            double percentErrorTolerance) {
        this.columnName = columnName;
        this.value = value;
        this.percentErrorTolerance = percentErrorTolerance;
    }

    /** {@inheritDoc} */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        String tableAlias = tableTracker.getTableAlias("searchUnitCells", this);
        double margin = this.value * this.percentErrorTolerance / 100;
        StringBuilder sb = new StringBuilder(128);
        
        sb.append("(");
        sb.append(tableAlias);
        sb.append(".");
        sb.append(this.columnName);
        sb.append(" > ? AND ");
        sb.append(tableAlias);
        sb.append(".");
        sb.append(this.columnName);
        sb.append(" < ?)");
        parameters.add(this.value - margin);
        parameters.add(this.value + margin);
        
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
            UnitCellSearchIndexSC x = (UnitCellSearchIndexSC) obj;
            
            return this.columnName.equals(x.columnName)
                    && (this.value == x.value)
                    && (this.percentErrorTolerance == x.percentErrorTolerance);
        }
    }

    /** @inheritDoc */
    @Override
    public int hashCode() {
        return (this.columnName + this.value
                + this.percentErrorTolerance).hashCode();
    }
}
