/*
 * Reciprocal Net project
 * 
 * ActionDateSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A {@code SearchConstraint} to limit search results to those samples that have
 * had actions performed on them either before or after the given date based on
 * the operator.
 */
public class ActionDateSC extends SearchConstraint {

    /**
     * In order for MySQL to parse dates for comparison, they must be formatted
     * in the following way.
     */
    /*
     * TODO: determine whether this date format is parsible by other database
     * engines.
     */
    public static final String MYSQL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * An operator that may be selected if a sample must have had an action
     * performed on it BEFORE the given date in order to be included in the
     * search results.
     */
    public static final int REQUIRE_ACTION_BEFORE = 300;

    /**
     * An operator that may be selected if a sample must have had an action
     * performed on it AFTER the given date in order to be included in the
     * search results
     */
    public static final int REQUIRE_ACTION_AFTER = 301;

    /**
     * The operator to indicate whether a sample must have had an action
     * performed before or after the given date.
     */
    private int operator;

    /** Indicates the threshold date. */
    private Date date;

    /**
     * A constructor that fully initializes an {@code ActionDateSC}.
     * 
     * @param date a date before which (or after which) a sample must have had
     *        an action performed on it to be included in the search results
     * @param operator one of the two operators defined by this class
     */
    public ActionDateSC(Date date, int operator) {
        this.date = date;
        this.operator = operator;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation generates
     * a {@code String} that may be used as a portion of the SQL WHERE clause to
     * require that the sample have an action performed on it before (or after)
     * the given 'date' to be included in the search results.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        SimpleDateFormat dform = new SimpleDateFormat(MYSQL_DATE_FORMAT);

        parameters.add(dform.format(date));
        if (this.operator == REQUIRE_ACTION_BEFORE) {
            System.out.println("firstActionDate < " + date);
            return tableTracker.getTableAlias("samples", this)
                    + ".firstActionDate < ?";
        } else if (this.operator == REQUIRE_ACTION_AFTER) {
            System.out.println("lastActionDate > " + date);
            return tableTracker.getTableAlias("samples", this)
                    + ".lastActionDate > ?";
        } else {
            throw new IllegalStateException();
        }
    }

    /** Equality is based on class, operation and date. */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ActionDateSC) {
            ActionDateSC adsc = (ActionDateSC) obj;

            return ((this.getClass() == adsc.getClass())
                    && (this.operator == adsc.operator)
                    && (((this.date == null) && (adsc.date == null))
                            || ((this.date != null)
                                    && this.date.equals(adsc.date))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new String(String.valueOf(getClass())
                + String.valueOf(this.operator)
                + String.valueOf(this.date)).hashCode();
    }
}
