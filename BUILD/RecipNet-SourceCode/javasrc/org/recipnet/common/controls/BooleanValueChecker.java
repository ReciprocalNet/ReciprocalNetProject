/*
 * Reciprocal Net Project
 *
 * BooleanValueChecker.java
 *
 * 09-Feb-2006: jobollin wrote first draft
 * 28-Mar-2006: jobollin added support for a 'checkedValue' attribute
 * 12-Jun-2006: jobollin fixed bug in generateCopy() that prevented use of this
 *              tag without a control
 */

package org.recipnet.common.controls;

import java.util.Map;

import javax.servlet.jsp.JspException;

/**
 * A Checker tag that conditionally suppresses its body on and after the
 * {@code FETCHING_PHASE} based on the the value of a specified control and,
 * optionally, an explicit boolean value.
 * 
 * @author jobollin
 * @version 1.0
 */
public class BooleanValueChecker extends AbstractChecker {

    /**
     * A required, non-transient tag attribute that establishes the
     * {@code HtmlControl} whose value is to be used during the
     * {@code FETCHING_PHASE} to determine whether this checker's inclusion
     * condition has been met. 
     */
    private HtmlControl control;

    /**
     * A primitive boolean value to use as this checker's criterion; exposed as
     * a transient tag attribute; default {@code false}
     */
    private boolean checkedValue;
    
    /**
     * {@inheritDoc}
     * 
     * @see AbstractChecker#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        control = null;
        checkedValue = true;
    }

    /**
     * Returns the value of the {@code control} property of this tag handler
     * 
     * @return the {@code HtmlControl} assigned as the source of the value on
     *         which this checker makes its suppression decision 
     */
    public HtmlControl getControl() {
        return control;
    }

    /**
     * Sets the value of the {@code control} property of this tag handler
     * 
     * @param  control the {@code HtmlControl} to serve as the source of the
     *         value on which this checker makes its suppression decision
     */
    public void setControl(HtmlControl control) {
        if (control == null) {
            throw new NullPointerException("Null control");
        }
        this.control = control;
    }

    /**
     * Retrieves the explicit value tested by this checker
     * 
     * @return the {@code boolean} value to test
     */
    public boolean isCheckedValue() {
        return checkedValue;
    }

    /**
     * Sets an explicit value for this checker to test
     * 
     * @param  checkedValue the {@code boolean} value to test
     */
    public void setCheckedValue(boolean checkedValue) {
        this.checkedValue = checkedValue;
    }

    /**
     * {@inheritDoc}.  This version determines whether the assigned control's
     * value is equal to {@code Boolean.TRUE}.  If so then this checker's
     * inclusion condition is met; otherwise, it isn't.
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        super.inclusionConditionMet
                = Boolean.valueOf(checkedValue).equals(control.getValue());
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see AbstractChecker#generateCopy(String, Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        BooleanValueChecker copy
                = (BooleanValueChecker) super.generateCopy(newId, map);
        
        /*
         * The map might not contain the referenced control as a key if it is
         * not an ancestor of this tag and it is not contained in a common
         * iterator with this tag.  In those cases, however, the reference copy
         * performed by HtmlPageElement is already produced the correct result.
         */
        if (map.containsKey(this.control)) {
            copy.setControl((HtmlControl) map.get(this.control));
        }
        
        return copy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        
        BooleanValueChecker checker = (BooleanValueChecker) source;

        setCheckedValue(checker.isCheckedValue());
    }
    
    
}
