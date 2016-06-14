/*
 * Reciprocal Net project
 * 
 * SearchConstraintGroup.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added getMatches(), isHypotheticalChildKnownToMatch(),
 *              isHypotheticalChildKnownToMismatch()
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.recipnet.site.shared.db.SampleInfo;

/**
 * <p>
 * A {@code SearchConstraint} implementation that grenerates an SQL WHERE clause
 * fragment to represent the relationship of its children.
 * </p><p>
 * Subclasses that can determine equality without having to compare the equality
 * of their children should override {@code equals()} and {@code hashCode()} for
 * the sake of efficiency.
 * </p>
 */
public class SearchConstraintGroup extends SearchConstraint {

    /**
     * The logical OR operator; at least one of the child constraints must
     * apply.
     */
    public static final int OR = 0;

    /**
     * The logical AND operator; every one of the child constraints must apply.
     */
    public static final int AND = 1;

    /**
     * The logical NOT OR operator; not even one of the child constraints may
     * apply.
     */
    public static final int NOR = 2;

    /**
     * The logical NOT AND operator; none, or any of the child constraints must
     * apply, but not all of them.
     */
    public static final int NAND = 3;

    /**
     * A {@code Collection} of {@code SearchConstraint} objects representing the
     * members of this group.
     */
    private final Collection<SearchConstraint> children;

    /**
     * One of the constant values; describes the relationship of the children.
     */
    private final int operator;

    /**
     * A constructor that sets the operator. This constructor is only useful to
     * subclasses and may be used in conjunction with {@code addChild()}.
     * 
     * @param operator one of the statically defined operators for this class;
     *        represents the relationship between the children
     */
    protected SearchConstraintGroup(int operator) {
        switch (operator) {
            case OR:
            case AND:
            case NOR:
            case NAND:
                this.operator = operator;
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.children = new ArrayList<SearchConstraint>();
    }

    /**
     * A constructor that allows a {@code Collection} of
     * {@code SearchConstraint} objects to populate this group.
     * 
     * @param operator one of the statically defined operators for this class;
     *        represents the relationship between the children
     * @param children a {@code Collection} of {@code SearchConstraint} objects
     *        that will be the children for this {@code SearchConstraintGroup}.
     *        This collection may not contain null.
     */
    public SearchConstraintGroup(int operator,
            Collection<? extends SearchConstraint> children) {
        this(operator);
        addChildren(children);
    }

    /**
     * A constructor that creates a {@code SearchConstraintGroup} that has the
     * same operator as the given group and contains all of its children as well
     * as all of the {@code SearchConstraint}s in the given {@code Collection}.
     * 
     * @param group a {@code SearchConstraintGroup} that will be used as the
     *        model for the one being created
     * @param constraintsToAdd a {@code Collection} of {@code SearchConstraint}s
     *        that will be included along with all of those in the model group
     */
    public SearchConstraintGroup(SearchConstraintGroup group,
            Collection<? extends SearchConstraint> constraintsToAdd) {
        this(group.getOperator(), group.getChildren());
        addChildren(constraintsToAdd);
    }

    /**
     * A constructor that creates a {@code SearchConstraintGroup} with the given
     * operator containg the given {@code SearchConstraint} and all of the
     * {@code SearchConstraint} objects in the given {@code Collection}.
     * 
     * @param operator one of the statically defined operators for this class;
     *        represents the relationship between the children
     * @param singleConstraintToAdd a single {@code SearchConstraint} that will
     *        be included in the new group
     * @param constraintsToAdd a {@code Collection} of {@code SearchConstraint}s
     *        that will be included along with the 'singleConstraintToAdd'
     */
    public SearchConstraintGroup(int operator,
            SearchConstraint singleConstraintToAdd,
            Collection<? extends SearchConstraint> constraintsToAdd) {
        this(operator, constraintsToAdd);
        addChild(singleConstraintToAdd);
    }

    /**
     * A protected helper method that may be called by subclasses during their
     * constructor to populate this group. This method MUST NOT be called after
     * the constructor, as that would be a violation of the immutability
     * expectation for {@code SearchConstraint} objects and may cause unexpected
     * behavior. If {@code children} is null, a new {@code Collection} is
     * created.
     * 
     * @param child a {@code SearchConstraint} that is to be a child of this
     *        {@code SearchConstraintGroup}
     */
    protected void addChild(SearchConstraint child) {
        this.children.add(child);
    }

    /**
     * A private helper method that is called by various constructors to add a
     * {@code Collection} of {@code SearchRestraint} objects to this group. If
     * {@code children} is null, a new {@code Collection} is created.
     * 
     * @param childrenToAdd a {@code Collection} of {@code SearchConstraint}
     *        objects that will be members of this {@code SearchConstraintGroup}
     */
    private void addChildren(
            Collection<? extends SearchConstraint> childrenToAdd) {
        this.children.addAll(childrenToAdd);
    }

    /**
     * Gets an unmodifiable {@code Collection} containing all of the members of
     * this {@code SearchConstraintGroup}.
     * 
     * @return an unmodifiable {@code Collection} containing all the children of
     *         this {@code SearchConstraintGroup}.
     */
    @Override
    public Collection<SearchConstraint> getChildren() {
        return Collections.unmodifiableCollection(this.children);
    }

    /**
     * Gets the operator that relates the members of this group.
     * 
     * @return one of the int operator codes defined by this class
     */
    public int getOperator() {
        return this.operator;
    }

    /**
     * Delegates to children, with the operator applied. If this group has no
     * children that return values from their implementation of
     * {@code getWhereClauseFragment()} this method will return the empty
     * {@code String}. If more than one child generates a WHERE condition the
     * operator will be applied and paretheses will surround the group.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, SearchConstraintExtraInfo scei) {
        StringBuilder sb = new StringBuilder();

        /*
         * keep track of how many children contributed conditions to the WHERE
         * clause -- if there is more than one then they should be enclosed in
         * parentheses
         */
        int contributingChildCount = 0;

        /*
         * iterate through the children to construct the WHERE clause for the
         * group
         */
        for (SearchConstraint child : children) {
            String childsFragment = child.getWhereClauseFragment(tableTracker,
                    parameters, scei);

            if (childsFragment.length() > 0) {
                if (contributingChildCount != 0) {
                    /*
                     * a previous condition was written to the StringBuffer so
                     * the operator must be included between it and this new
                     * condition
                     */
                    if ((operator == OR) || (operator == NOR)) {
                        sb.append(" OR ");
                    } else {
                        sb.append(" AND ");
                    }
                }
                contributingChildCount++;
                sb.append(childsFragment);
            } else {
                /*
                 * for some reason the child constraint has nothing to
                 * contribute to the SQL query
                 */
            }
        }

        if (contributingChildCount > 0) {
            if (contributingChildCount > 1) {
                sb.insert(0, '(').append(')');
            }
            if ((operator == NAND) || (operator == NOR)) {
                sb.insert(0, " NOT ");
            }
        }

        return sb.toString();
    }

    /**
     * Two {@code SearchConstraintGroup}s are equal if they are instances of
     * the same class, have the same operator and if for each child in each
     * {@code SearchConstraintGroup} there is an equal child in the other.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof SearchConstraintGroup) {
            SearchConstraintGroup scg = (SearchConstraintGroup) obj;

            return ((this.getClass() == scg.getClass())
                    && (this.operator == scg.operator)
                    && this.children.containsAll(scg.children)
                    && scg.children.containsAll(this.children));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = getClass().hashCode() + operator;

        for (Object child : this.children) {
            hashCode += child.hashCode();
        }

        return hashCode;
    }

    /**
     * Overrides {@code SearchConstraint}; the current implementation delegates
     * to its children, consolidating the generated {@code FieldMatchInfo}
     * objects. Based on the success of the children and the 'operator' for this
     * group, the provided 'matches' or 'mismatches' are updated and a value is
     * returned that reflects the success or failure of the sample against
     * constraints defined by this {@code SearchConstraintGroup}. In the case
     * of NAND or NOR operators the 'mismatches' of the child are included in
     * the matches of the parent or the 'matches' of the child are included in
     * the mismatches of the parent.
     */
    @Override
    public boolean getMatches(SampleInfo sample,
            Collection<FieldMatchInfo> matches,
            Collection<FieldMatchInfo> mismatches) {
        if (this.children.isEmpty()) {
            return true;
        }

        /*
         * Determine if the return value is known and what is is. In cases where
         * child constraints do not yet support this feature we can sometimes
         * disregard them and still return useful match information about other
         * children.
         */
        Boolean knownReturnValue = (((matches != null) && (mismatches != null))
                ? null : Boolean.valueOf(matches != null));

        Collection<FieldMatchInfo> childMatches
                = new ArrayList<FieldMatchInfo>();
        Collection<FieldMatchInfo> childMismatches
                = new ArrayList<FieldMatchInfo>();
        int childrenMatched = 0;
        int childrenMismatched = 0;
        
        for (SearchConstraint child : this.children) {
            try {
                boolean currentChildMatches = child.getMatches(sample,
                        /*
                         * if it is known that the child will mismatch, pass the
                         * hint along by providing a null collection for match
                         * information
                         */
                        (isHypotheticalChildKnownToMismatch(matches != null,
                                mismatches != null) ? null : childMatches),
                        /*
                         * if it is known that the child will match, pass the
                         * hint along by providing a null collection for
                         * mismatch information
                         */
                        (isHypotheticalChildKnownToMatch(matches != null,
                                mismatches != null) ? null : childMismatches));
                
                if (currentChildMatches) {
                    childrenMatched++;
                } else {
                    childrenMismatched++;
                }
            } catch (UnsupportedOperationException ex) {
                if (knownReturnValue == null) {
                    /*
                     * To determine the return value of this SearchConstraint we
                     * need to know the return value of every child, but at
                     * least one child's return value is unknown, so we must
                     * also fail.
                     */
                    throw ex;
                }
            }
        }

        switch (this.operator) {
            case AND:
                if (knownReturnValue == Boolean.TRUE) {
                    assert (matches != null);
                    matches.addAll(childMatches);
                    return true;
                } else if (knownReturnValue == Boolean.FALSE) {
                    assert (mismatches != null);
                    mismatches.addAll(childMismatches);
                    return false;
                } else if (childrenMismatched != 0) {
                    // mismatches because at least one child mismatched
                    assert (mismatches != null);
                    mismatches.addAll(childMismatches);
                    return false;
                } else {
                    // matches because every child matched
                    assert (matches != null);
                    matches.addAll(childMatches);
                    return true;
                }
            case OR:
                if (knownReturnValue == Boolean.TRUE) {
                    assert (matches != null);
                    matches.addAll(childMatches);
                    return true;
                } else if (knownReturnValue == Boolean.FALSE) {
                    assert (mismatches != null);
                    mismatches.addAll(childMismatches);
                    return false;
                } else if (childrenMatched == 0) {
                    // mismatches because no child matched
                    assert (mismatches != null);
                    mismatches.addAll(childMismatches);
                    return false;
                } else {
                    // matches because at least one child matched
                    assert (matches != null);
                    matches.addAll(childMatches);
                    return true;
                }
            case NAND:
                if (knownReturnValue == Boolean.TRUE) {
                    assert (matches != null);
                    matches.addAll(childMismatches);
                    return true;
                } else if (knownReturnValue == Boolean.FALSE) {
                    assert (mismatches != null);
                    mismatches.addAll(childMatches);
                    return false;
                } else if (childrenMatched == 0) {
                    // matches because every child mismatched
                    assert (matches != null);
                    matches.addAll(childMismatches);
                    return true;
                } else {
                    // mismatches because at least one child matched
                    assert (mismatches != null);
                    mismatches.addAll(childMatches);
                    return false;
                }
            case NOR:
                if (knownReturnValue == Boolean.TRUE) {
                    assert (matches != null);
                    matches.addAll(childMismatches);
                    return true;
                } else if (knownReturnValue == Boolean.FALSE) {
                    assert (mismatches != null);
                    mismatches.addAll(childMatches);
                    return false;
                } else if (childrenMismatched == 0) {
                    // mismatches because not a single child mismatched
                    assert (mismatches != null);
                    mismatches.addAll(childMatches);
                    return false;
                } else {
                    // matches because every child mismatched
                    assert (matches != null);
                    matches.addAll(childMismatches);
                    return true;
                }
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * A helper function that returns true if it can be deduced that any given
     * child of this group must match.
     * 
     * @param matchesProvided a boolean indicating whether it is possible that
     *        this {@code SearchConstraint} could match the sample (indicated by
     *        a non-null 'matches' parameter passed to {@code getMatches()})
     * @param mismatchesProvided a boolean indicating whether it is possible
     *        that this {@code SearchConstraint} could mismatch the sample
     *        (indicated by a non-null 'mismatches' parameter passed to
     *        {@code getMatches()})
     */
    private boolean isHypotheticalChildKnownToMatch(boolean matchesProvided,
            boolean mismatchesProvided) {
        if (matchesProvided && mismatchesProvided) {
            // no hint was given by the caller so nothing is known
            return false;
        }
        /*
         * If it is known that ALL children must match for this to match and we
         * are told that this matches we know that ALL children (and thus any
         * hypothetical child) match.
         */
        /*
         * Likewise if we know that AT LEAST ONE child must mismatch for this to
         * mismatch and we are told that this mismatches, we know that ALL
         * children (and thus any hypothetical child) match.
         */
        return (((this.operator == AND) && matchesProvided)
                || ((this.operator == NAND) && mismatchesProvided));
    }

    /**
     * A helper function that returns true if it can be deduced that any given
     * child of this group must NOT match.
     * 
     * @param matchesProvided a boolean indicating whether it is possible that
     *        this {@code SearchConstraint} could match the sample (indicated by
     *        a non-null 'matches' parameter passed to {@code getMatches()})
     * @param mismatchesProvided a boolean indicating whether it is possible
     *        that this {@code SearchConstraint} could mismatch the sample
     *        (indicated by a non-null 'mismatches' parameter passed to
     *        {@code getMatches()})
     */
    private boolean isHypotheticalChildKnownToMismatch(boolean matchesProvided,
            boolean mismatchesProvided) {
        if (matchesProvided && mismatchesProvided) {
            // no hint was given by the caller so nothing is known
            return false;
        } else {
            /*
             * If it is known that AT LEAST ONE child must match for this to
             * match and we are told that this does not match we know that ALL
             * children must not match.
             */
            /*
             * Likewise if we know that ALL children must mismatch for this to
             * mismatch and we are told that this matches we know that ALL
             * children must not match.
             */
            return (((this.operator == OR) && mismatchesProvided)
                    || ((this.operator == NOR) && matchesProvided));
        }
    }
}
