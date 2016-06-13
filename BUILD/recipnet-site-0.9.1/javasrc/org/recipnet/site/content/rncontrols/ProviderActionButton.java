/*
 * Reciprocal Net project
 * 
 * ProviderActionButton.java
 *
 * 30-Nov-2004: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.ButtonHtmlControl;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * <p>
 * An extension of {@code ButtonHtmlControl} that when clicked, causes the
 * {@code ProviderPage} in which this tag must be nested to perform one of
 * various possible functions.
 * </p><p>
 * If the provider function is set to an function that may not be performed,
 * this control will be invisible.
 * </p>
 */
public class ProviderActionButton extends ButtonHtmlControl {

    /**
     * An ErrorSupplier flag that may be set if this button represents a
     * provider function that may not be performed by the currently logged in
     * user for this page.
     */
    public static final int PROHIBITED_FUNCTION
            = ButtonHtmlControl.getHighestErrorFlag() << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return PROHIBITED_FUNCTION;
    }

    /**
     * The {@code ProviderPage} in which this tag is nested. This reference is
     * set by {@code onFetchingPhaseBeforeBody()}.
     */
    private ProviderPage providerPage;

    /**
     * A required property that indicates which function on the provider will be
     * triggered when this button is clicked. This must be set to one of the
     * constant provider function codes defined on {@code ProviderPage}.
     */
    private int providerFunction;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.providerPage = null;
        this.providerFunction = ProviderPage.NO_PROVIDER_FUNCTION;
    }

    /**
     * @param code the function code (defined on {@code ProviderPage} that is to
     *        be triggered when this button is clicked
     * @throws IllegalArgumentException if 'code' is not a valid function code
     */
    public void setProviderFunction(int code) {
        switch (code) {
            case ProviderPage.NO_PROVIDER_FUNCTION:
            case ProviderPage.ADD_NEW_PROVIDER:
            case ProviderPage.EDIT_EXISTING_PROVIDER:
            case ProviderPage.CONSIDER_DEACTIVATION:
            case ProviderPage.DEACTIVATE_PROVIDER:
            case ProviderPage.CANCEL_DEACTIVATION:
            case ProviderPage.CANCEL_PROVIDER_FUNCTION:
                this.providerFunction = code;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return an int, representing the function code (from those defined on
     *         {@code ProviderPage}) that is to be performed when this button
     *         is clicked.
     */
    public int getProviderFunction() {
        return this.providerFunction;
    }

    /**
     * {@inheritDoc}; this version sets the {@code providerPage}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code ProviderPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the ProviderPage tag
        try {
            this.providerPage = (ProviderPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version tests whether the user is authorized to
     * perform the configured function, and if not, makes this button invisible
     * and sets an error flag.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if (!this.providerPage.isFunctionValid(this.providerFunction)) {
            setVisible(false);
            setErrorFlag(PROHIBITED_FUNCTION);
        }
        return super.onFetchingPhaseBeforeBody();
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link ProviderPage#triggerProviderAction(int)}.
     */
    @Override
    protected void onClick(@SuppressWarnings("unused") PageContext pageContext) {
        this.providerPage.triggerProviderAction(this.providerFunction);
    }

    /**
     * {@inheritDoc}; this version delegates back to the sueprclass but ensures
     * that the 'visible' property is not modified.
     * 
     * @param source a {@code ProviderActionButton} whose transient fields are
     *        being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        boolean overrideVisibility = this.getVisible();

        super.copyTransientPropertiesFrom(source);
        /*
         * The 'visible' property on the superclass is transient. This means
         * that every phase whatever value was set by the JSP author is copied
         * to this 'real element'. In this case, we set the visibility
         * internally so we must prevent the value from being overwritten by
         * this method each phase.
         */
        this.setVisible(overrideVisibility);
    }
}
