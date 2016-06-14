/*
 * Reciprocal Net project
 * 
 * SampleAccessSelector.java
 *
 * 15-Nov-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.RadioButtonHtmlControl;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * A custom tag that displays a radio button that represents a specific access
 * level for the user and sample defined by the most immediately enclosing
 * contexts. These radio buttons will be grouped by user and sample, meaning
 * only one radio button with a given access level should be placed in a given
 * {@code UserContext} and {@code SampleContext}. It is typical to have one of
 * each access level though. In the event that a particular access level is
 * invalid for the given sample and user, this control will be suppressed.
 * </p><p>
 * This control must be nested within a {@code SampleContext} and a
 * {@code UserContext} and neither context may return a null container from the
 * {@code FETCHING_PHASE} onward.
 * </p>
 */
public class SampleAccessSelector extends RadioButtonHtmlControl {

    /**
     * A required property that may be set to one of the access level constants
     * defined on {@code SampleAccessLevel}.
     */
    private int accessLevel;

    /**
     * The most immediately enclosing {@code UserContext}. This reference is
     * determined by {@code onRegistrationPhaseBeforeBody()}.
     */
    private UserContext userContext;

    /**
     * The most immediately enclosing {@code SampleContext}. This reference is
     * determined by {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.accessLevel = SampleAccessInfo.INVALID_ACCESS;
        setOption("INVALID_ACCESS");
        this.userContext = null;
        this.sampleContext = null;
    }

    /**
     * @param level the access level to be exposed by this control, must be set
     *        to one of the constants defined on {@code SampleAccessInfo}.
     * @throws IllegalArgumentException if 'level' is not a valid level as
     *         defined by {@code SampleAccessInfo}
     */
    public void setAccessLevel(int level) {
        switch (level) {
            case SampleAccessInfo.INVALID_ACCESS:
                setOption("INVALID_ACCESS");
                break;
            case SampleAccessInfo.READ_ONLY:
                setOption("READ_ONLY");
                break;
            case SampleAccessInfo.READ_WRITE:
                setOption("READ_WRITE");
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.accessLevel = level;
    }

    /**
     * @return the access level; one of the constants defined on
     *         {@code SampleAccessInfo}
     */
    public int getAccessLevel() {
        return this.accessLevel;
    }

    /**
     * {@inheritDoc}; this version looks up the innermost containing
     * {@code SampleContext} and {@code UserContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext} or {@code UserContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get UserContext
        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        if (this.userContext == null) {
            throw new IllegalStateException();
        }

        // get SampleContext
        this.sampleContext = findRealAncestorWithClass(this,
                SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version determines whether this control should be
     * visible, and sets its value based on context information
     * 
     * @throws IllegalStateException if one of the required contexts returns
     *         null
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo si = this.sampleContext.getSampleInfo();
        UserInfo ui = this.userContext.getUserInfo();

        if ((si == null) || (ui == null)) {
            throw new IllegalStateException();
        }

        setVisible(AuthorizationCheckerBL.isAccessLevelValid(this.accessLevel,
                ui, si));

        setValue(
                Boolean.valueOf(
                        AuthorizationCheckerBL.getUserAccessLevelForSample(
                                ui, si) == this.accessLevel),
                HtmlControl.EXISTING_VALUE_PRIORITY);

        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the context information to match
     * this control's current value.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (getValueAsBoolean()) {
            AuthorizationCheckerBL.setAccessLevel(this.accessLevel,
                    this.userContext.getUserInfo(),
                    this.sampleContext.getSampleInfo());
        }

        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}; this version delegates back to the superclass but ensures
     * that the 'visible' and 'option' properties are not modified.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        boolean overrideVisibility = getVisible();
        String overrideOption = getOption();

        super.copyTransientPropertiesFrom(source);
        setVisible(overrideVisibility);
        setOption(overrideOption);
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleAccessSelector dc = (SampleAccessSelector) super.generateCopy(
                newId, map);

        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.userContext = (UserContext) map.get(this.userContext);

        return dc;
    }
}
