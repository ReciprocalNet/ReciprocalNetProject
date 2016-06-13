/*
 * Reciprocal Net Project
 *
 * SimpleIterator.java
 *
 * 27-Mar-2006: jobollin wrote first draft
 * 27-Dec-2007: ekoperda removed extraneous debug log emissions from
 *              beforeIteration()
 */

package org.recipnet.common.controls;

import javax.servlet.jsp.JspException;

/**
 * An HtmlPageIterator that iterates a specified number of times, without
 * consideration of any particular context or data.  The number of iterations
 * to perform can be incremented by other tags, and specific iterations can be
 * cancelled.
 * 
 * @author jobollin
 * @version 1.0
 */
public class SimpleIterator extends HtmlPageIterator {
    
    /**
     * The number of iterations this iterator is set to perform each phase;
     * it is exposed as a tag attribute via its accessor and mutator methods
     */
    private int iterations;
    
    /**
     * An internal variable used to track the number of times each phase that
     * the {@link #addAnotherIteration()} method is invoked; the cumulative
     * result is applied at the end of the phase, in the
     * {@link #afterIteration()} method
     */
    private int additionalIterations;

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        iterations = 0;
        additionalIterations = 0;
    }

    /**
     * Gets the number of iterations this iterator is currently configured to
     * perform each phase
     * 
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Sets the number of iterations this iterator should perform; the actual
     * number performed can be affected by subsequent invocations of
     * {@link #addAnotherIteration()} and {@link #deleteCurrentIteration()};
     * on form POST, this value is ignored in favor of the posted iteration
     * count
     * 
     * @param  iterations the number of iterations this iterator should perform
     *         each phase
     */
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Requests that this iterator add one iteration to the number it will
     * perform; during POST processing, this method should between the end of
     * the {@code PARSING_PHASE} (inclusive) and the beginning of iteration
     * during the {@code RENDERING_PHASE} (exclusive) to be effective.  It
     * should only be invoked on real elements (not proxies).
     */
    public void addAnotherIteration() {
        assert getRealElement() == this;
        
        if (isIterationInProgress()) {
            additionalIterations++;
        } else {
            iterations++;
        }
    }
    
    /**
     * {@inheritDoc}.  This version makes iteration deletion publicly
     * accessible, and provides appropriate front-end handling.  This method
     * should only be invoked on real elements (not proxies), and only during
     * evaluation of this iterator's body.
     * 
     * @see HtmlPageIterator#deleteCurrentIteration()
     */
    @Override
    public void deleteCurrentIteration() throws JspException {
        assert getRealElement() == this;

        super.deleteCurrentIteration();
        iterations--;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#isCurrentIterationLast()
     */
    @Override
    public boolean isCurrentIterationLast() {
        return (getIterationCountSinceThisPhaseBegan() >= (iterations - 1));
    }

    /**
     * {@inheritDoc}.  This version updates the number of iterations to perform
     * at the beginning of the parsing phase, based on the posted iteration
     * count
     * 
     * @see org.recipnet.common.controls.HtmlPageIterator#beforeIteration()
     */
    @Override
    protected void beforeIteration() throws JspException {
        super.beforeIteration();
        
        if (getPage().getPhase() == HtmlPage.PARSING_PHASE) {
            iterations = getPostedIterationCount();
        }
    }

    /**
     * {@inheritDoc}.  This version returns true if the number of iterations so
     * far this phase is less than the configured number of iterations (as
     * modified by use of {@link #addAnotherIteration()} and
     * {@link #deleteCurrentIteration()}).
     * 
     * @see HtmlPageIterator#onIterationBeforeBody()
     * @see HtmlPageIterator#getIterationCountSinceThisPhaseBegan()
     */
    @Override
    protected boolean onIterationBeforeBody() {
        
        /*
         * Iterators always evaluate their bodies once during the registration
         * phase.  If this iterator has a posted iteration count of zero, then
         * it must evaluate its body at least once on every phase up to the
         * processing phase in order to allow its iteration count to be
         * increased from zero.  (This may not account for deleting the last
         * iteration and subsequently attempting to increase the count on the
         * same request.)
         */
        
        int iterationIndex = getIterationCountSinceThisPhaseBegan();
        
        return ((iterationIndex < iterations)
                || ((iterationIndex == 0) && (iterations == 0)
                        && (getPage().getPhase() != HtmlPage.RENDERING_PHASE)));
    }

    /**
     * {@inheritDoc}.  This version handles updating the number of iterations
     * in response to invocations this phase of {@link #addAnotherIteration()}
     * 
     * @see HtmlPageIterator#afterIteration()
     */
    @Override
    protected void afterIteration() throws JspException {
        super.afterIteration();
        
        iterations += additionalIterations;
        additionalIterations = 0;
    }
}
