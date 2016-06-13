/*
 * Reciprocal Net project
 * 
 * SearchParams.java
 *
 * 10-Jun-2002: rlauer wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 01-Aug-2002: ekoperda wrote first draft
 * 08-Aug-2002: ekoperda fixed bug #302 in getSqlQuery()
 * 09-Aug-2002: ekoperda fixed bug #317 in getSqlQuery()
 * 12-Aug-2002: ekoperda fixed bug #339 in getSqlQuery()
 * 13-Aug-2002: ekoperda fixed bug #363 in getSqlQuery()
 * 16-Aug-2002: ekoperda fixed bug #340 in getSqlQuery()
 * 15-Oct-2002: ekoperda added criteria fields requireLocalHolding and
 *              requireAuthoritative
 * 06-Nov-2002: ekoperda added actionDateAfter and actionDateBefore criteria
 *              fields
 * 07-Nov-2002: eisiorho added sorting of search results, added 5 constants
 * 04-Feb-2003: ekoperda added setForSystemFindAllAuthoritative() and facility
 *              for bypassing user-level security
 * 18-Feb-2003: ekoperda fixed bug #735 in getSqlQuery()
 * 20-Feb-2003: ekoperda added Cloneable interface
 * 06-Mar-2003: adharurk added fields requireTerminal and requireNonRetracted;
 *              added support for them to getSqlQuery()
 * 06-Mar-2003: yli added criteria field sampleProviderName and support to
 *              getSqlQuery()
 * 19-Mar-2003: ekoperda fixed bug #793 in getSqlQuery()
 * 20-Mar-2003: ekoperda replaced getSqlQuery() with generateDbQuery();
 *              added sanitizeForLike()
 * 02-Apr-2003: ekoperda fixed bug #852 in generateDbQuery()
 * 05-May-2003: midurbin added sorting by current_sampleHistory_id and
 *              searching for samples of a specified status.
 * 24-Jun-2003: midurbin added member variables keywords, matchAnyKeyword
 *              and modified constructor and generateDbQuery() to support
 *              keyword searches.
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared
 * 27-Feb-2004: cwestnea fixed bug #1120 in constructor and generateDbQuery()
 * 29-Mar-2004: cwestnea fixed bug #1127 in generateDbQuery()
 * 29-Mar-2004: cwestnea fixed bug #1128 in generateDbQuery()
 * 08-Aug-2004: cwestnea modified generateDbQuery() to use SampleWorkflowBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 11-Jan-2005: ekoperda fixed bug #1495 in generateDbQuery()
 * 25-Feb-2005: midurbin rewrote entire class so that the bulk of the search
 *              query logic is spread over various SearchConstraint classes
 * 01-Jun-2006: jobollin reformatted the source and removed unused imports
 */

package org.recipnet.site.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintGroup;

/**
 * <p>
 * A class that stores {@code SearchConstraint}s, and sort order information so
 * that it may generate an SQL {@code PreparedStatement} to perform a particular
 * search.
 * </p><p>
 * Every {@code SearchParams} has a single head {@code SearchConstraintGroup}
 * which may or may not contain other {@code SearchConstraint} objects. This
 * head group requires that all of its child {@code SearchConstraint} objects be
 * satisfied. The most basic search structures involve adding various
 * constraints to this group.
 * </p>
 */
public class SearchParams implements Cloneable, Serializable {

    /** Default sort - db engine chooses order */
    public static final int DEFAULT_SORT_ORDER = 0;

    /** Sorts the sample results by local lab id. */
    public static final int SORTBY_LOCALLABID = 100;

    /** Sorts the sample results by reversing the local lab id results. */
    public static final int SORTBY_LOCALLABID_REV = 200;

    /** Sorts the sample results by lab id. */
    public static final int SORTBY_LABID_LOCALLABID = 300;

    /** Sorts the sample results by reversing the lab id results. */
    public static final int SORTBY_LABID_LOCALLABID_REV = 400;

    /** Sorts the sample results by current sample history id. */
    public static final int SORTBY_CURRENT_SAMPLEHISTORY_ID = 500;

    /**
     * Sorts the sample results by reversing the current sample history id
     * results.
     */
    public static final int SORTBY_CURRENT_SAMPLEHISTORY_ID_REV = 600;

    /**
     * A reference to the head search constraint. This is initialized to null
     * but may be set or updated by calls to {@code addToHeadWithAnd()} or
     * {@code addToHeadWithOr()}.
     */
    private SearchConstraint head;

    /**
     * The sort order; defaults to {@code DEFAULT_SORT_ORDER}; set by
     * {@code setSortOrder()}.
     */
    private int sortOrder;

    /**
     * A constructor that initializes the {@code sortOrder} to
     * {@code DEFAULT_SORT_ORDER} and creates an empty head
     * {@code SearchConstraintGroup}.
     */
    public SearchParams() {
        this.sortOrder = DEFAULT_SORT_ORDER;
        this.head = null;
    }

    /**
     * Sets the sortOrder.
     * 
     * @param sortOrder one of the sort order constants defined by this class
     * @throws IllegalArgumentException if the 'sortOrder' is not one of those
     *         defined by this page
     */
    public void setSortOrder(int sortOrder) {
        switch (sortOrder) {
            case DEFAULT_SORT_ORDER:
            case SORTBY_LOCALLABID:
            case SORTBY_LOCALLABID_REV:
            case SORTBY_LABID_LOCALLABID:
            case SORTBY_LABID_LOCALLABID_REV:
            case SORTBY_CURRENT_SAMPLEHISTORY_ID:
            case SORTBY_CURRENT_SAMPLEHISTORY_ID_REV:
                this.sortOrder = sortOrder;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the sort order.
     * 
     * @return the sortOrder as set by the constructor or a call to
     *         {@code setSortOrder()}
     */
    public int getSortOrder() {
        return this.sortOrder;
    }

    /**
     * Sets the 'head' {@code SearchConstraint}. This completely replaces the
     * current search criteria, but leaves the sort order unchanged.
     */
    public void setHead(SearchConstraint newHead) {
        this.head = newHead;
    }

    /**
     * Gets the 'head' {@code SearchConstraint}. This represents the current
     * search criteria but not the sort order. Because {@code SearchConstriant}
     * objects are immutable, this reference may be used as the head of another
     * {@code SearchParams} or even as the child of another
     * {@code SearchConstraint}
     */
    public SearchConstraint getHead() {
        return this.head;
    }

    /**
     * A convenience method that adds a single {@code SearchConstraint} to this
     * {@code SearchParams} in a manner that requires both the existing head
     * search constraint AND the provided constraint to be true of samples
     * returned by the search. This action can be undone by invoking
     * {@code setHead()} with a reference acquired by a call to
     * {@code getHead()} made right before this call. When more than one
     * constraint needs to be added at a time, the version of this method that
     * accepts a {@code Collection} of {@code SearchConstraint} objects is much
     * more efficient.
     */
    public void addToHeadWithAnd(SearchConstraint sc1) {
        /*
         * TODO: improve efficiency by maintaining an internal collection of
         * added constraints and add them all at once when needed (ie, calls to
         * getHead()). This will cut down on object creation time.
         */
        addToHeadWithAnd(Collections.singleton(sc1));
    }

    /**
     * A convenience method that adds multiple {@code SearchConstraint} objects
     * to this {@code SearchParams} in a manner that requires that the existing
     * head search constraint as well as each {@code SearchConstraint} objects
     * included in the provided {@code Collection} to be to be true of samples
     * returned by the search.
     */
    public void addToHeadWithAnd(
            Collection<? extends SearchConstraint> constraintsToAdd) {
        if (this.head == null) {
            // There is no head; create a new group containing the provided
            // SearchConstraints
            this.head = new SearchConstraintGroup(SearchConstraintGroup.AND,
                    constraintsToAdd);
        } else if (((this.head.getClass() == SearchConstraintGroup.class) && (((SearchConstraintGroup) this.head).getOperator() == SearchConstraintGroup.AND))) {
            /*
             * The head is an AND group; create a new group the contains all of
             * the current members of the head AND group as well as all of the
             * SearchConstraints to add
             */
            /*
             * Note: the classes are compared rather than using the instanceof
             * operator to preserve the atomic nature of SearchConstraintGroup
             * subclasses
             */
            this.head = new SearchConstraintGroup((SearchConstraintGroup) head,
                    constraintsToAdd);
        } else {
            /*
             * the head is not and AND group; create a new one containing all
             * the provided SearchConstraints as well as the current head
             */
            this.head = new SearchConstraintGroup(SearchConstraintGroup.AND,
                    this.head, constraintsToAdd);
        }

    }

    /**
     * Exposes the protected {@code clone()} method to the public.
     */
    @Override
    public SearchParams clone() {
        try {
            return (SearchParams) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // can't happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }
}
