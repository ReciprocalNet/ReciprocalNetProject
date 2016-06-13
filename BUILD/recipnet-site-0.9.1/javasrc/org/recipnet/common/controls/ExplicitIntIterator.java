/*
 * Reciprocal Net Project
 *
 * ExplicitStringIterator.java
 *
 * 30-Mar-2006: jobollin wrote first draft
 * 12-Jun-2006: jobollin removed an unused import
 */

package org.recipnet.common.controls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * An HtmlPageIterator that iterates over a specified (possibly empty) list of
 * ints
 * 
 * @author jobollin
 * @version 1.0
 */
public class ExplicitIntIterator extends HtmlPageIterator {
    
    private int[] ints;
    private List<Integer> intList;
    private Iterator<Integer> iterator;
    private Integer currentInt;
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        intList = null;
        ints = null;
        currentInt = null;
    }
    
    /**
     * Returns the ints over which this iterator will iterate, in the
     * delimited form in which they were specified to this iterator
     * 
     * @return the {@code ints} {@code String}
     */
    public int[] getInts() {
        return ints.clone();
    }

    /**
     * Sets the ints over which this iterator will iterate, in the form
     * of a delimited string; the delimiter may be explicitly set via the
     * 'delimiter' property, but otherwise it is single commas or semicolons
     * (or a mixture of the two)
     * 
     * @param  ints the {@code String} to set as the {@code ints}
     */
    public void setInts(int[] ints) {
        this.ints = ints.clone();
    }
    
    /**
     * Returns an unmodifiable {@code List} of those {@code String}s over which
     * this iterator will iterate
     *  
     * @return a {@code List<String>} of the Strings over which this iterator
     *         will iterate
     */
    protected List<Integer> getIntList() {
        return intList;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if the 'ints' property is {@code null} 
     * @throws NumberFormatException if the specified string of values cannot
     *         be parsed into a list of {@code int}s 
     *         
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        List<Integer> list = new ArrayList<Integer>(ints.length);
        
        for (int i : ints) {
            list.add(i);
        }
        
        intList = Collections.unmodifiableList(list);
        
        return rc;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#beforeIteration()
     */
    @Override
    protected void beforeIteration() throws JspException {
        super.beforeIteration();
        iterator = intList.iterator();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#onIterationBeforeBody()
     */
    @Override
    protected boolean onIterationBeforeBody() {
        boolean shouldIterate
                = (super.onIterationBeforeBody() && iterator.hasNext());
        
        if (shouldIterate) {
            currentInt = iterator.next();
        }
        
        return shouldIterate;
    }
    
    /**
     * {@inheritDoc}.
     * 
     * @see HtmlPageIterator#isCurrentIterationLast()
     */
    @Override
    public boolean isCurrentIterationLast() {
        return !iterator.hasNext();
    }

    /**
     * Returns the string that is the subject of the current iteration
     * 
     * @return the {@code String} that is the subject of the current iteration
     */
    public int getCurrentInt() {
        return currentInt.intValue();
    }
}
