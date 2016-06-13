/*
 * Reciprocal Net project
 * 
 * PreferenceChecker.java
 *
 * 24-Jun-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin made this class extend AbstractChecker and reformatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;

/**
 * This {@code SuppressionContext} implementation will suppress its body based
 * on preferences for the current session.
 */
public class PreferenceChecker extends AbstractChecker {

    /**
     * A required property that indicates a boolean preference type that must be
     * true in order for this tag's body to be included (as opposed to
     * suppressed).
     */
    private UserBL.Pref includeIfBooleanPrefIsTrue;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.includeIfBooleanPrefIsTrue = null;
    }

    /**
     * Sets the 'includeIfBooleanPrefIsTrue' property.
     * 
     * @param pref a preference that accepts a boolean value
     */
    public void setIncludeIfBooleanPrefIsTrue(UserBL.Pref pref) {
        this.includeIfBooleanPrefIsTrue = pref;
    }

    /**
     * Gets the 'includeIfBooleanPrefIsTrue' property.
     * 
     * @return a preference that accepts a boolean value
     */
    public UserBL.Pref getIncludeIfBooleanPrefIsTrue() {
        return this.includeIfBooleanPrefIsTrue;
    }

    /**
     * {@inheritDoc}; this version determines whether the inclusion condition
     * has been met, based on preferences for the current session.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        inclusionConditionMet = UserBL.getPreferenceAsBoolean(
                this.includeIfBooleanPrefIsTrue,
                (UserPreferences) this.pageContext.getSession().getAttribute(
                        "preferences"));

        return rc;
    }
}
