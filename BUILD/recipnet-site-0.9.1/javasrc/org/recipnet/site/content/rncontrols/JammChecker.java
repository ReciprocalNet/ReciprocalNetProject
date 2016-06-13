/*
 * Reciprocal Net project
 * 
 * JammChecker.java
 * 
 * 04-May-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin made this tag extend AbstractChecker and reformatted
 *              the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.AbstractChecker;

/**
 * A custom tag that interfaces with a {@link JammAppletTag}} and evaluates or
 * suppresses its body based on the 'inclusion' condition properties that are
 * set. This tag will suppress its body until the {@code FETCHING_PHASE} at
 * which point it will determine whether to continue suppressing the body or not
 * based on the inclusion properties and the characteristics of the
 * {@code JammAppletTag}.
 */
public class JammChecker extends AbstractChecker {

    /**
     * A required property; this is a reference to a {@code JammAppletTag} whose
     * characteristics are compared with the supplied 'inclusion' condition.
     * Note that in this versionm this property is <em>not</em> transient and
     * <em>not</em> modified when this tag is copied (in an iteration,
     * for instance); these characteristics may change in a future version.
     */
    private JammAppletTag jammAppletTag;

    /**
     * An 'inclusion' condition property that when set bases the suppression of
     * the body of this tag on whether the current applet (as determined by
     * {@link JammAppletTag#getApplet getApplet()}) matches the value of this
     * property. The comparison is case insensitive.
     */
    private String includeOnlyIfCurrentAppletIs;

    /** {@inheritDoc} */
    @Override
    public void reset() {
        super.reset();
        this.jammAppletTag = null;
        inclusionConditionMet = false; // in the superclass
    }

    /**
     * @param tag a reference to the JammAppletTag that this tag is checking
     */
    public void setJammAppletTag(JammAppletTag tag) {
        this.jammAppletTag = tag;
    }

    /**
     * @return a reference to the JammAppletTag that this tag is checking
     */
    public JammAppletTag getJammAppletTag() {
        return this.jammAppletTag;
    }

    /**
     * @param appletName one of the valid applet names as defined by
     *        {@code JammAppletTag}
     */
    public void setIncludeOnlyIfCurrentAppletIs(String appletName) {
        this.includeOnlyIfCurrentAppletIs = appletName;
    }

    /**
     * @return one of the valid applet names as defined by {@code JammAppletTag}
     */
    public String getIncludeOnlyIfCurrentAppletIs() {
        return this.includeOnlyIfCurrentAppletIs;
    }

    /**
     * {@inheritDoc}; this version determines whether the condition for
     * inclusion has been met, based on the configured {@code JammAppletTag}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        /*
         * the JammAppletTag will have decided its 'applet' type by now, so we
         * can make the comparison and set 'inclusionConditionMet'
         */
        if (this.includeOnlyIfCurrentAppletIs != null) {
            inclusionConditionMet
                    = this.includeOnlyIfCurrentAppletIs.equalsIgnoreCase(
                            this.jammAppletTag.getApplet());
        }
        
        return rc;
    }
}
