/*
 * Reciprocal Net Project
 *
 * SimpleChecker.java
 *
 * 12-Jun-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

/**
 * A checker tag that suppresses its body or not depending only on the value of
 * a tag parameter and on the suppression state of any surrounding suppression
 * context.  More complex checking implementations should extend
 * {@link AbstractChecker} rather than this class.
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public final class SimpleChecker extends AbstractChecker {
    
    /*
     * Implementation Note: this tag works by exposing the superclass's
     * 'inclusionConditionMet' field as a transient tag attribute.  As such, it
     * doesn't need to implement any phase behavior of its own, nor even provide
     * customized reseting or copying behavior.
     */
    
    /**
     * Determines whether this checker's inclusion condition is met
     * 
     * @return {@code true} if the condition is met, {@code false} if not
     */
    public boolean isConditionMet() {
        return inclusionConditionMet;
    }

    /**
     * Sets whether this checker's inclusion condition has been met
     * 
     * @param conditionMet {@code true} if the condition is met, {@code false}
     *        if not
     */
    public void setConditionMet(boolean conditionMet) {
        inclusionConditionMet = conditionMet;
    }

    /**
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        
        setConditionMet(((SimpleChecker) source).isConditionMet());
    }

}
