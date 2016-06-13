/*
 * Reciprocal Net project
 * 
 * LabActionButton.java
 *
 * 07-Dec-2004: midurbin wrote first draft
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
 * {@code LabPage} in which this tag must be nested to perform one of various
 * possible functions.
 * </p><p>
 * If the lab function is set to a function that may not be performed, this
 * control will be invisible.
 * </p>
 */
public class LabActionButton extends ButtonHtmlControl {

    /**
     * An ErrorSupplier flag that may be set if this button represents a
     * provider function that may not be performed by the currently logged in
     * user for this page.
     */
    public static final int PROHIBITED_FUNCTION
            = ButtonHtmlControl.getHighestErrorFlag() << 1;

    /**
     * The {@code LabPage} in which this tag is nested. This reference is set by
     * {@code onFetchingPhaseBeforeBody()}.
     */
    private LabPage labPage;

    /**
     * A required property that indicates which function on the lab will be
     * triggered when this button is clicked. This must be set to one of the
     * constant provider function codes defined on {@code LabPage}.
     */
    private int labFunction;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        super.reset();
        this.labPage = null;
        this.labFunction = LabPage.NO_LAB_FUNCTION;
    }

    /**
     * @param code the function code (defined on {@code LabPage} that is to be
     *        triggered when this button is clicked
     * @throws IllegalArgumentException if 'code' is not a valid function code
     */
    public void setLabFunction(int code) {
        switch (code) {
            case LabPage.NO_LAB_FUNCTION:
            case LabPage.EDIT_EXISTING_LAB:
            case LabPage.CANCEL_LAB_FUNCTION:
                this.labFunction = code;
                return;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return an int, representing the function code (from those defined on
     *         {@code LabPage}) that is to be performed when this button is
     *         clicked.
     */
    public int getLabFunction() {
        return this.labFunction;
    }

    /**
     * {@inheritDoc}; this version ensures that it resides in a {@code LabPage}
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code LabPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get a reference to the LabPage tag
        try {
            this.labPage = (LabPage) getPage();
        } catch (ClassCastException cce) {
            throw new IllegalStateException(cce);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version checks whether the user is trying to perform
     * a prohibited function, and if so, makes this button invisible and sets an
     * error flag.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        if (!this.labPage.isFunctionValid(this.labFunction)) {
            setVisible(false);
            setErrorFlag(PROHIBITED_FUNCTION);
        }
        
        return super.onFetchingPhaseBeforeBody();
    }

    /**
     * {@inheritDoc}; this version invokes
     * {@link LabPage#triggerLabAction(int)}, but does not invoke the
     * superclass's version
     */
    @Override
    protected void onClick(@SuppressWarnings("unused") PageContext pageContext) {
        this.labPage.triggerLabAction(this.labFunction);
    }

    /**
     * {@inheritDoc}; this version delegates back to the superclass but ensures
     * that the 'visible' property is not modified.
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

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return PROHIBITED_FUNCTION;
    }
}
