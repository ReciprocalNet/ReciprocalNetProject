/*
 * Reciprocal Net Project
 *
 * DeleteIterationButton.java
 *
 * 28-Mar-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * A button control for the specific purpose of allowing users to delete an
 * iteration of the SimpleIterator containing this button
 * 
 * @author jobollin
 * @version 1.0
 */
public class DeleteIterationButton extends ButtonHtmlControl {

    /**
     * The innermost {@code SimpleIterator} containing this button; the one that
     * will be affected when this button is clicked
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
        super.setLabel("Delete");
        hostIterator = null;
    }

    /**
     * {@inheritDoc}.  This version looks up the innermost containing
     * SimpleIterator; this is the iterator that it will affect (if clicked),
     * and it must exist.
     * 
     * @see ButtonHtmlControl#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        hostIterator = findRealAncestorWithClass(this, SimpleIterator.class);
        
        if (hostIterator == null) {
            throw new JspException("No applicable host iterator");
        }
            
        return rc;
    }

    /**
     * {@inheritDoc}.  This version instructs the host SimpleIterator to delete
     * the current iteration
     * 
     * @see ButtonHtmlControl#onClick(PageContext)
     */
    @Override
    protected void onClick(PageContext pageContext) throws JspException {
        super.onClick(pageContext);
        
        hostIterator.deleteCurrentIteration();
    }

    /**
     * {@inheritDoc}
     * 
     * @see ButtonHtmlControl#generateCopy(java.lang.String, java.util.Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        DeleteIterationButton copy
                = (DeleteIterationButton) super.generateCopy(newId, map);
        
        copy.hostIterator = (SimpleIterator) map.get(this.hostIterator);
        
        return copy;
    }
}
