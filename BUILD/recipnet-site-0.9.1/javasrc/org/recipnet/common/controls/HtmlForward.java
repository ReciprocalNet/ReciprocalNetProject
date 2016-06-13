/*
 * Reciprocal Net project
 * 
 * HtmlForward.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 19-Jan-2006: jobollin updated copyTransientPropertiesFrom() to use setter
 *              methods; formatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * This is a Tag that conditionally forwards the Request if the indicated
 * condition is met. If the condition is true when this control is evaluated
 * during the {@code REGISTRATION_PHASE}, or {@code PROCESSING_PHASE} the
 * request is forwarded at that time. Otherwise this control does nothing.
 */
public class HtmlForward extends HtmlPageElement {

    /**
     * An optional attribute that is evaluated during the
     * {@code REGISTRATION_PHASE} and {@code PROCESSING_PHASE} to determine
     * whether or not the Request should be forwarded to the {@code target}.
     * Initialized by {@code reset()} and modified by its 'setter' method,
     * {@code setCondition}. This is a 'transient' variable in that its value
     * may change from phase to phase and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private boolean condition;

    /**
     * A required attribute that indicates the relative URL of the page to which
     * the Request may be forwarded. Initialized by {@code reset()} and modified
     * by its 'setter' method, {@code setTarget()}. This is a 'transient'
     * variable in that its value may change form phase to phase and is copied
     * by {@code copyTransientPropertiesFrom()}.
     */
    private String target;

    /**
     * An optional attribute that indicates the reason to forward. Before the
     * Request is forwarded, if this attribute is set, an attribute with the
     * name 'reason' on the ServletContext's Request object is set to the
     * specified value. Initialized by {@code reset()} and modified by its
     * 'setter' method, {@code setReason()}. This is a 'transient' variable in
     * that its value may change from phase to phase and is copied by
     * {@code copyTransientPropertiesFrom()}.
     */
    private String reason;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this isntance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.reason = null;
        this.condition = true;
    }

    /**
     * @param condition the condition upon which the browser should be forwarded
     */
    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    /**
     * @return the current evaluation of the condition that would indicate that
     *         the browser should be forwarded.
     */
    public boolean getCondition() {
        return this.condition;
    }

    /**
     * @param target the relative URL to which the browser should be redirected
     *        The value of target must begin with a "/".
     */
    public void setTarget(String target) {
        // FIXME: What is the purpose of the test below?
        if (this.target == null) {
            this.target = target;
        }
    }

    /** @return the relative URL to which the browser should be redirected */
    public String getTarget() {
        return this.target;
    }

    /** @param reason a reason {@code String} */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /** @return the reason the request was forwarded */
    public String getReason() {
        return this.reason;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation will
     * forward the browser to the {@code target} if {@code condition} is true.
     * If a {@code reason} is provided, the context attribute with the key
     * 'reason' will be set. If {@code condition} is not true, this
     * implementation will delegate back to the superclass.
     * 
     * @param pageContext the current {@code PageContext} whose {@code 
     *        ServletContext} object may be used to forward the browser.
     *        
     * @return {@code SKIP_PAGE} is returned when the {@code condition} is true,
     *         otherwise the return value from the superclass' implementation is
     *         used.
     *         
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.condition) {
            try {
                HtmlForward.forwardRequest(pageContext, this.reason,
                        this.target);
                return SKIP_PAGE;
            } catch (ServletException ex) {
                throw new JspException(ex);
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation will
     * forward the browser to the {@code target} if {@code condition} is true.
     * If a {@code reason} is provided, the context attribute with the key
     * 'reason' will be set. If {@code condition} is not true, this
     * implementation will delegate back to the superclass.
     * 
     * @param pageContext the current {@code PageContext} whose {@code 
     *        ServletContext} object may be used to forward the browser.
     *        
     * @return {@code SKIP_PAGE} is returned when the {@code condition} is true,
     *         otherwise the return value from the superclass' implementation is
     *         used.
     *         
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        if (this.condition) {
            try {
                HtmlForward.forwardRequest(pageContext, this.reason,
                        this.target);
                return SKIP_PAGE;
            } catch (ServletException ex) {
                throw new JspException(ex);
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        return super.onRegistrationPhaseAfterBody(pageContext);
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields from
     * {@code source} if it is an {@code HtmlForward} to this object.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        HtmlForward src = (HtmlForward) source;

        setCondition(src.condition);
        setReason(src.reason);

        /*
         * FIXME: the target is set both ways because of the wierd condition in
         * setTarget(). Doing it both ways ensures both that the attribute is
         * copied and that subclasses are updated.
         */
        this.target = src.target;
        setTarget(src.target);
    }

    /**
     * A helper function that forwards the request associated with the supplied
     * {@code PageContext} to the indicated {@code target} If a non-null
     * {@code reason} is provided, an attribute with the name 'reason' and the
     * indicated value will be put on the Request object.
     * 
     * @param pageContext the PageContext for the current request.
     * @param reason a value that will be attached to the request object as an
     *        attribute named 'reason'.
     * @param target the relative URL to which the Request will be forwarded
     * 
     * @throws ServletException if the target resource throws this exception
     * @throws IOException if the target resource throws this exception
     * @throws IllegalStateException if the response was already committed
     */
    public static void forwardRequest(PageContext pageContext, String reason,
            String target) throws ServletException, IOException {
        ServletRequest request = pageContext.getRequest();
        ServletResponse response = pageContext.getResponse();
        if (reason != null) {
            request.setAttribute("reason", reason);
        }
        RequestDispatcher dispatcher
                = pageContext.getServletContext().getRequestDispatcher(target);
        dispatcher.forward(request, response);
    }
}
