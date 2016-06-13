/*
 * Reciprocal Net Project
 *
 * AddIterationButton.java
 *
 * 27-Mar-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * A button that increments the number of iterations to be performed by the
 * conigured {@code SimpleIterator}
 * 
 * @author jobollin
 * @version 1.0
 */
public class AddIterationButton extends ButtonHtmlControl {
    
    /**
     * the explicitly set {@code SimpleIterator} that this button will increment
     * when clicked, if any; if this attribute is not specified then this tag
     * must be nested in a suitable iterator
     * 
     * @see #hostIterator
     */
    private SimpleIterator iterator;
    
    /**
     * the innermost containing {@code SimpleIterator}; if no iterator to update
     * is explicitly set then this is the iterator that is updated when this
     * button is clicked
     * 
     * @see #iterator
     */
    private SimpleIterator hostIterator;
    
    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.ButtonHtmlControl#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        setLabel("Add another");
        iterator = null;
        hostIterator = null;
    }
    
    /**
     * Returns the {@code SimpleIterator} this button is configured to increment
     * when clicked
     * 
     * @return the {@code SimpleIterator} configured to be updated by this
     *         button
     */
    public SimpleIterator getIterator() {
        return iterator;
    }
    
    /**
     * Explicitly sets the {@code SimpleIterator} for this button to increment
     * when clicked
     * 
     * @param  iterator the {@code SimpleIterator} to set
     */
    public void setIterator(SimpleIterator iterator) {
        this.iterator = iterator;
    }
    
    /**
     * {@inheritDoc}.  This version ensures that this tag has an iterator to
     * update.
     * 
     * @see ButtonHtmlControl#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        hostIterator = findRealAncestorWithClass(this, SimpleIterator.class);
        
        if ((hostIterator == null) && (iterator == null)) {
            throw new JspException("No iterator to increment");
        }
            
        return rc;
    }
    
    /**
     * {@inheritDoc}.  Provided that this control has no error flags raised,
     * this version instructs the configured {@code SimpleIterator} to increase
     * the number of iterations it performs.
     * 
     * @see ButtonHtmlControl#onClick(javax.servlet.jsp.PageContext)
     */
    @Override
    protected void onClick(PageContext pageContext) throws JspException {
        super.onClick(pageContext);
        
        ((iterator == null) ? hostIterator : iterator).addAnotherIteration();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see ButtonHtmlControl#generateCopy(java.lang.String, java.util.Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AddIterationButton copy
                = (AddIterationButton) super.generateCopy(newId, map);
        
        /*
         * The specified SimpleIterator might not be in the map if the two are
         * not contained within a common iterator (including the specified
         * iterator itself).  In that case, however, the reference copy behavior
         * provided by HtmlPageElement does the right thing.
         */
        if (map.containsKey(getIterator())) {
            copy.setIterator((SimpleIterator) map.get(getIterator()));
        }
        
        copy.hostIterator = (SimpleIterator) map.get(this.hostIterator);

        return copy;
    }

}
