/*
 * Reciprocal Net Project
 *
 * ValuePriorityOverrideContext.java
 *
 * 21-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import org.recipnet.common.controls.HtmlControl;

/**
 * An interface describing the behavior of tags that provide hints to nested
 * controls about priorities to use when setting values.  It is up to the nested
 * controls as to whether or how to make use of such hints.  This version
 * provides hints about priorities to use during the {@code FETCHING_PHASE}
 * only.  One application of the behavior hinted by this tag would be to cause
 * nested tags to allow existing values read during the {@code FETCHING_PHASE}
 * to override values parsed from user input.
 * 
 * @author jobollin
 * @version 1.0
 */
public interface ValuePriorityOverrideContext {

    /**
     * Returns the recommended priority code with which the specified control
     * should set values during the {@code FETCHING_PHASE}.  The normal priority for
     * such values is {@code EXISTING_VALUE_PRIORITY}.
     * 
     * @param  targetControl the {@code HtmlControl} for which a value priority
     *         hint is requested
     * 
     * @return the code for the recommended priority to use when setting values
     *         during the {@code FETCHING_PHASE} 
     */
    int getFetchedValuePriority(HtmlControl targetControl);
    
    /**
     * Returns the recommended priority code with which the specified control
     * should set {@code null} values during the {@code FETCHING_PHASE}, if they
     * use a different priority for {@code null}s than for other values.  The
     * typical priority for such values is {@code DEFAULT_VALUE_PRIORITY}.
     * 
     * @param  targetControl the {@code HtmlControl} for which a value priority
     *         hint is requested
     *         
     * @return the code for the recommended priority to use when setting 
     *         {@code null} values during the {@code FETCHING_PHASE} 
     */
    int getFetchedNullPriority(HtmlControl targetControl);
    
    /**
     * Evaluates whether this context is "enabled" so that it is issuing
     * overriding priorities
     * 
     * @return {@code true} if this context is overriding priorities;
     *         {@code false} if not
     */
    boolean isEnabled();
}
