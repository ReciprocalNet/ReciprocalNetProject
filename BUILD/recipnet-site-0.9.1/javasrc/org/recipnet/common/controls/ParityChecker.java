/*
 * Reciprocal Net project
 * 
 * ParityChecker.java
 * 
 * 19-Nov-2004: midurbin wrote first draft
 * 30-Nov-2004: midurbin added 'includeOnAllButFirst' inclusion property
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 12-Apr-2005: midurbin added 'includeOnlyOnFirst' inclusion property
 * 28-Apr-2005: midurbin added 'includeOnlyOnMultiplesOf' inclusion property
 * 21-Jun-2005: midurbin added 'includeOnlyOnLast', 'includeOnAllButLast'
 *              inclusion properties
 * 28-Mar-2006: jobollin added 'includeOnOnlyIteration', reformatted code,
 *              switched over to subclassing AbstractChecker, removed some
 *              attributes in favor of inverting their complements; removed
 *              the restriction to a single type of checking
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A custom tag that must be nested with an {@code HtmlPageIterator} and
 * evaluates its body only on certain iterations, as indicated by the various
 * inclusion properties. (Note: the first iteration is considered to be the 0th
 * iteration and is thus an even number and a multiple of any number.) This tag
 * implements the {@code SuppressionContext} so that when the body is not to be
 * included, no nested tags will perform any response-altering actions as well
 * as having their output excluded.
 */
public class ParityChecker extends AbstractChecker {

    /**
     * A reference to the most immediate {@code HtmlPageIterator} in which this
     * tag is nested. This reference is set by
     * {@code onRegistrationPhaseBeforeBody()} and used whenever it must be
     * determined whether the body of this tag is suppressed or included.
     */
    private HtmlPageIterator iterator;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) during just the first iteration. This
     * may not be set to true if any other inclusion property is set to true.
     */
    private boolean includeOnlyOnFirst;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) only on the last iteration during the
     * {@code  RENDERING_PHASE} . Check to make sure that
     * {@link org.recipnet.common.controls.HtmlPageIterator#isCurrentIterationLast
     * HtmlPageIterator.isCurrentIterationLast()}
     * has been usefully overridden for the tag in which this tag is nested
     * because some subclasses of {@code  HtmlPageIterator} will throw
     * {@code  UnsupportedOperationException} s when attempting to determine if
     * a given iteration is the last.
     */
    private boolean includeOnlyOnLast;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) during EVEN iterations. This may not
     * be set to true if any other inclusion property is set to true.
     */
    private boolean includeOnlyOnEven;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) during all iterations that are
     * multiples of this property's integer value. (Note: the first iteration is
     * zero and thus a multiple of every integer) If this property is set to
     * zero (its default) it is considered to be unset and has no effect. When
     * this property is set to a non-zero value, this tag always includes its
     * body every nth iteration, where n is the value of this property.
     */
    private int includeOnlyOnMultiplesOf;

    /**
     * An optional property that indicates whether this tag's body should be
     * included (as opposed to suppressed) during the {@code RENDERING_PHASE} on
     * any iteration that is <em>either</em> not the first or not the last.
     * That is, on every iteration if there is more than one, and otherwise not
     * on any iteration. Check to make sure that {@link
     * org.recipnet.common.controls.HtmlPageIterator#isCurrentIterationLast
     * HtmlPageIterator.isCurrentIterationLast()} has been usefully overridden
     * for the tag in which this tag is nested because some subclasses of
     * {@code HtmlPageIterator} will throw {@code UnsupportedOperationException}s
     * when attempting to determine if a given iteration is the last.
     */
    private boolean includeOnOnlyIteration;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.iterator = null;
        this.includeOnlyOnFirst = false;
        this.includeOnlyOnLast = false;
        this.includeOnlyOnEven = false;
        this.includeOnlyOnMultiplesOf = 0;
        this.includeOnOnlyIteration = false;
    }

    /**
     * @param include indicates whether the body should be included (not
     *        suppressed) if this tag finds itself to be on the first iteration.
     * @throws IllegalArgumentException if any other inclusion conditions are
     *         set while trying to set this one to true
     */
    public void setIncludeOnlyOnFirst(boolean include) {
        this.includeOnlyOnFirst = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included (not
     *         suppressed) if this tag finds itself to be on the first
     *         iteration.
     */
    public boolean isIncludeOnlyOnFirst() {
        return this.includeOnlyOnFirst;
    }

    /**
     * @param include indicates whether the body should be included (not
     *        suppressed) if this tag finds itself to be on the last iteration.
     * @throws IllegalArgumentException if any other inclusion conditions are
     *         set while trying to set this one to true
     */
    public void setIncludeOnlyOnLast(boolean include) {
        this.includeOnlyOnLast = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included (not
     *         suppressed) if this tag finds itself to be on the last iteration.
     */
    public boolean isIncludeOnlyOnLast() {
        return this.includeOnlyOnLast;
    }

    /**
     * @param include indicates whether the body should be included (not
     *        suppressed) if this tag finds itself to be in an even iteration
     * @throws IllegalArgumentException if any other inclusion conditions are
     *         set while trying to set this one to true
     */
    public void setIncludeOnlyOnEven(boolean include) {
        this.includeOnlyOnEven = include;
    }

    /**
     * @return a boolean that indicates whether the body should be included (not
     *         suppressed) if this tag finds itself to be in an even iteration
     */
    public boolean isIncludeOnlyOnEven() {
        return this.includeOnlyOnEven;
    }

    /**
     * @param number an integer that the current iteration must be a multiple of
     *        in order to be included (rather than suppressed). If this
     *        parameter value is zero, it will 'unset' this property.
     * @throws IllegalArgumentException if any other inclusion conditions are
     *         set while trying to set this one
     */
    public void setIncludeOnlyOnMultiplesOf(int number) {
        this.includeOnlyOnMultiplesOf = number;
    }

    /**
     * @return an integer indicating a number by which iteration indices must be
     *         evenly divisible by in order for this tag's body to be included
     *         (rather than suppressed)
     */
    public int getIncludeOnlyOnMultiplesOf() {
        return this.includeOnlyOnMultiplesOf;
    }

    /**
     * Determines whether this tag is set to include (not suppress) its body if
     * its associated iterator is executing more than one iteration
     * 
     * @return the {@code true} if this tag is configured as described,
     *         {@code false} otherwise
     */
    public boolean isIncludeOnOnlyIteration() {
        return includeOnOnlyIteration;
    }

    /**
     * Sets whether this tag should include (not suppress) its body if its
     * associated iterator is executing more than one iteration
     * 
     * @param include {@code true} to configure this tag as described
     */
    public void setIncludeOnOnlyIteration(boolean include) {
        this.includeOnOnlyIteration = include;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the
     * {@code HtmlPageIterator} in which this tag is nested.
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code HtmlPageIterator}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get reference to the enclosing iterator
        this.iterator = findRealAncestorWithClass(this, HtmlPageIterator.class);
        if (this.iterator == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {

        /*
         * Normally the first thing in an on___BeforeBody() method would be an
         * invocation of the superclass's version. In this case, some of the
         * suppression conditions can only be tested in the RENDERING_PHASE, and
         * they must be tested before the superclass's version of the method
         * runs.
         */

        boolean onlyFirstPredicate = (!isIncludeOnlyOnFirst()
                || iterator.isCurrentIterationFirst());
        boolean onlyLastPredicate = (!isIncludeOnlyOnLast()
                || iterator.isCurrentIterationLast());
        boolean onlyEvenPredicate = (!isIncludeOnlyOnEven()
                || ((iterator.getIterationCountSinceThisPhaseBegan() % 2) == 0));
        boolean singleIterationPredicate = (!isIncludeOnOnlyIteration()
                || (iterator.isCurrentIterationFirst()
                        && iterator.isCurrentIterationLast()));
        boolean multiplePredicate = ((getIncludeOnlyOnMultiplesOf() == 0)
                || ((iterator.getIterationCountSinceThisPhaseBegan()
                        % getIncludeOnlyOnMultiplesOf()) == 0));

        inclusionConditionMet = (onlyFirstPredicate && onlyLastPredicate
                && onlyEvenPredicate && multiplePredicate
                && singleIterationPredicate);

        return super.onRenderingPhaseBeforeBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ParityChecker dc = (ParityChecker) super.generateCopy(newId, map);

        dc.iterator = (HtmlPageIterator) map.get(this.iterator);

        return dc;
    }
}
