/*
 * Reciprocal Net project
 * 
 * UserActionButton.java
 *
 * 30-Nov-2004: midurbin wrote first draft
 * 18-May-2005: midurbin changed type of 'userFunction' property
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ButtonHtmlControl;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * <p>
 * An extension of {@code ButtonHtmlControl} that when clicked, causes the
 * {@code UserPage} in which this tag must be nested to perform one of various
 * possible functions.
 * </p>
 * <p>
 * If the user function is set to an action that may not be performed, this
 * control will be invisible.
 * </p>
 */
public class UserActionButton extends ButtonHtmlControl {

    /**
     * An ErrorSupplier flag that may be set if this button represents a user
     * function that may not be performed by the currently logged in user for
     * this page.
     */
    public static final int PROHIBITED_FUNCTION
            = ButtonHtmlControl.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return PROHIBITED_FUNCTION;
    }

    /**
     * The {@code UserPage} in which this tag is nested. This reference is set
     * by {@code onFetchingPhaseBeforeBody()}.
     */
    private UserPage userPage;

    /**
     * A required property that indicates which function on the user will be
     * triggered when this button is clicked.
     */
    private UserPage.UserFunction userFunction;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.userPage = null;
        this.userFunction = null;
    }

    /**
     * @param function the function that is to be triggered when this button is
     *        clicked
     */
    public void setUserFunction(UserPage.UserFunction function) {
        this.userFunction = function;
    }

    /**
     * @return the user functionthat is to be performed when this button is
     *         clicked.
     */
    public UserPage.UserFunction getUserFunction() {
        return this.userFunction;
    }

    /**
     * {@inheritDoc}; this version sets the {@code userPage} field.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code UserPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the UserPage tag
        try {
            this.userPage = (UserPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version verifies that the user is authorized to
     * perform the requested function, and if not, it makes this button
     * invisible and sets an error flag.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if (!this.userPage.isFunctionValid(this.userFunction)) {
            setVisible(false);
            setErrorFlag(PROHIBITED_FUNCTION);
        }

        return super.onFetchingPhaseBeforeBody();
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link UserPage#triggerUserAction(UserPage.UserFunction)}.
     */
    @Override
    protected void onClick(@SuppressWarnings("unused")
    PageContext pageContext) {
        this.userPage.triggerUserAction(this.userFunction);
    }

    /**
     * {@inheritDoc}; this version prevents this tag's visibility from being
     * transient
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {

        /*
         * The 'visible' property on the superclass is transient. This means
         * that every phase, whatever value was set by the JSP author is copied
         * to this 'real element'. This class sets the visibility internally,
         * however, so we must prevent the value from being overwritten by this
         * method.
         */

        boolean overrideVisibility = getVisible();

        super.copyTransientPropertiesFrom(source);
        setVisible(overrideVisibility);
    }
}
