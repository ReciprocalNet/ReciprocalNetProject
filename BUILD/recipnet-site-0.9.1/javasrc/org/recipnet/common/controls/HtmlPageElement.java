/*
 * Reciprocal Net project
 * 
 * HtmlPageElement.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 19-Feb-2004: midurbin wrote second draft
 * 27-Feb-2004: midurbin made page private and added getPage()
 * 09-Mar-2004: midurbin added support for 'owned' elements by adding 
 *              registerOwnedElements(), unregisterOwnedElements(),
 *              setParentWithoutResetting(),
 *              getRegisteredOwnedElementsOfType(),
 *              delegateToPhaseBeforeBody() and delegateToPhaseAfterBody()
 * 16-Jun-2004: midurbin fixed bug 1245 in delegateToPhaseAfterBody() and 
 *              onParsingPhaseAfterBody()
 * 22-Jun-2004: midurbin added support for tags that evaluate their body
 *              multiple times, involving the creation of generateRealElement()
 *              and storeRealElement(), factoring code out of doStartTag() as 
 *              well as the addition of the method getRealElement()
 * 30-Jul-2004: midurbin added generateCopy() and updated generateRealElement()
 *              to use this method instead of clone(), fixing bug #1255
 * 04-Aug-2004: cwestnea fixed bug #1307 in registerOwnedElement()
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody(), added onFetchingPhaseAfterBody()
 * 27-Jan-2005: midurbin added support for HtmlPageElement objects to
 *              gracefully overwrite existing 'real' elements when the
 *              REGISTRATION_PHASE is reevaluated in accordance with HtmlPage's
 *              new feature
 * 25-Feb-2005: midurbin added {@code reassignOwnedElementIds()},
 *              {@code setId()} and updated {@code generateCopy()},
 *              {@code registerOwnedElement()} to fix bug #1323
 * 01-Apr-2005: midurbin updated generateRealElement() and generateCopy() to
 *              fix bug #1455
 * 08-Apr-2005: midurbin fixed bug #1570 in generateCopy()
 * 24-Jun-2005: ekoperda fixed bug #1621 in generateRealElement(), 
 *              generateCopy(), and doStartTag(), also added findRealParent() 
 *              and toString()
 * 01-Nov-2005: jobollin updated getRealAncestorWithClass() to make it generic,
 *              removed unused imports, and eliminated multiple unnecessary
 *              casts.  Added a wildcard type parameter to the parameter of
 *              getRegisteredOwnedElementsOfType().
 * 19-Jan-2006: jobollin updated copyTransientPropertiesFrom() to use setter
 *              methods, and to specify that subclasses should do the same
 * 01-Mar-2006: jobollin added allowIdUpdate()
 * 27-Mar-2006: jobollin reorganized the source, removed resetOwnedElement,
 *              factored registerId() out of doStartTag() and added
 *              unregisterId()
 * 15-Jun-2006: jobollin updated docs
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.recipnet.common.StrictMapWrapper;

/**
 * <p>
 * This is the base class for all phase-recognizing custom tags. All logic for
 * determining phase from the encapsulating {@code HtmlPage} (or extention of)
 * is handled in the implementation of Tag methods called by Tomcat or the
 * servlet container.
 * </p><p>
 * Implicit invocations on this class for a single JSP-level tag declaration by
 * the servlet container may be made on different instances of this class for
 * each phase. These instances will from now on be referred to as "Proxy
 * Elements". While there is one proxy element for each phase, there must be a
 * single element to maintain values between phases. This element will from now
 * on be referred to as the "real element" and be referenced from the member
 * variable {@code realElement} when it is available.
 * </p><p>
 * The internal implementation of this class serves to abstract away the concept
 * of the proxy element from sub classes by creating new methods corresponding
 * to the separate phases of execution of any potential tag. Furthermore, the
 * implicit calls to attribute setter functions on the proxy elements are
 * replicated to the real element at the beginning of every phase. All implicit
 * calls that fall before this sychronization do not vary from phase to phase
 * and are copied in the clone operation that instantiates the real element. All
 * implicit calls that happen after the synchronization are immediately relayed
 * to the real element.
 * </p><p>
 * For the real element to be stored, and then retrieved for each other phase
 * there must be a unique identifer linking corresponding proxy elements so that
 * they may only be associated with their one real element. In cases where
 * elements are to be accessible as scripting variables they are provided with a
 * unique 'id' attribute that is available and may serve to link proxy elements
 * with real elements. For those elements where no unique identifer is provided
 * through hard-coded attributes, a scheme of systematic id generation is used.
 * For this scheme to work, the following two conditions must be met:
 * </p>
 * <ol>
 * <li>Elements without uniqe id attributes must appear in the same amount and
 * order for each phase. This becomes relevant if a JSP has any logic that would
 * skip, or iterate bodies that contain custom tags in a manner that could
 * change between phases.</li>
 * <li>The same sequence of ids must be granted in each Phase. This is the
 * documented behavior of the method {@code HtmlPage.getNextId}.
 * </li>
 * </ol>
 * <p>
 * In some cases sublcasses will have instances of other HtmlPageElement
 * subclasses as member variables. These 'owned' elements may be considered to
 * be analogous to nested elements except that the servlet container does not
 * manage them. Instead, they must be registered with this base class through
 * calls to {@code registerOwnedElement()}. Once registered, they have their
 * phase method invoked as if they were nested right before the body of the
 * 'owning' element. There are no 'proxy' elements for 'owned' elements as they
 * are more similar to the 'real' elements. Because they are not declared in the
 * JSP, any required attributes must be set by the 'owning' control. The 'id'
 * attribute is no longer needed to match it with a 'real' element but may still
 * be used in the HTML generated by the control. For this reason 'owning'
 * elements have the option as to whether they wish to manage the 'id' attribute
 * of their 'owned' controls or have it managed by this base class. Simply put,
 * if the 'owning' class sets the 'id' attribute before registering an 'owned'
 * element it is indicating that it wishes to manage the id value.
 * </p>
 */
public abstract class HtmlPageElement extends BodyTagSupport implements
        Cloneable {
    
    /**
     * This variable retains a valid reference to the encapsulating
     * {@code HtmlPage} object from when it is set at the beginning of
     * {@code doStartTag} to the end of {@code doEndTag}. This reference is
     * cleared by {@code reset()}.
     */
    private HtmlPage page;

    /**
     * This object refers to an {@code HtmlPageElement} that is identical to
     * this object, except that the state of its member variables persist as
     * long as the current {@code PageContext} is in scope. This object is also
     * the object with which scripting variable references interact. This object
     * is set during {@code doStartTag()} after being initialized to null by
     * {@code reset()}.
     */
    private HtmlPageElement realElement;

    /**
     * A {@code Collection} of {@code HtmlPageElement}'s that are 'owned' by a
     * subclass of this control. Entries are added to or removed from this
     * {@code List} via calls to {@link #registerOwnedElement(HtmlPageElement)}
     * and {@link #unregisterOwnedElement(HtmlPageElement)} respectively.
     * Before the body of this {@code HtmlPageElement} is evaluated, each
     * 'owned' element on this list has its phase methods called.
     */
    private Collection<HtmlPageElement> ownedElements;

    /**
     * A {@code Collection} of {@code HtmlPageElement}'s that are 'owned' by a
     * subclass of this control for which the subclass wishes to exercise
     * exclusive control over the 'id' property. This is a subset of
     * 'ownedElements'. When subclasses register their owned elements they are
     * included in this {@code Collection} based on whether their 'id' property
     * has been set to a non-null value or not. Elements in this set are not
     * affected by calls to {@code reassignOwnedElementIds()}.
     */
    private Collection<HtmlPageElement> ownedElementsPreserveIds;

    /**
     * A default constructor. The current implementation simply delegates to
     * {@code reset()}. It cannot be assumed that a constructor will be called
     * on an {@code HtmlPageElement} before it is used to represent a custom
     * tag. On the other hand this constructor usefully resets variables on new
     * 'owned' elements when they are created.
     */
    public HtmlPageElement() {
        reset();
    }

    /**
     * This function is called before the attributes are set by the servlet
     * container. Due to the nature of custom tags, the contstructor for a given
     * tag will only be called for the first instance of that tag. Attribute
     * setters will be called for each tag as neccessary, but optional fields
     * may retain the value from a previous instance rather than the initial
     * value set by a constructor. For this reason, any initialization required
     * for optional fields, or any other dependance on initialation of variables
     * must be accounted for here. Subclasses that override this method, MUST
     * delegate back to thier superclass.
     */
    protected void reset() {
        super.setId(null);
        this.realElement = null;
        this.page = null;
        this.ownedElements = null;
        this.ownedElementsPreserveIds = null;
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container
     * <strong>on the proxy element</strong> just before the attributes
     * (including any id) are set for this custom tag. Because there is no
     * guarantee that the constructor will be called for each tag instance, in
     * the current implementation, a call to {@code reset()} is made here to
     * initialize member variables. Any subclasses may override this method, but
     * MUST delegate back to its superclass.
     * 
     * @param t the parent {@code Tag}
     */
    @Override
    public void setParent(Tag t) {
        super.setParent(t);
        reset();
    }

    /**
     * Overrides {@code TagSupport}; the current implementation delegates back
     * to the superclass then sets the id values of all of the registered owned
     * elements to a value derived from the supplied id.
     */
    @Override
    public void setId(String id) {
        super.setId(id);
        reassignOwnedElementIds();
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container
     * <strong>on the proxy element</strong> as it begins to evaluate this tag.
     * The current implementation sets the {@code realElement} either by
     * retrieving one stored during the {@code REGISTRATION_PHASE} or by cloning
     * this element. In the case of a retrieval, any transient attributes
     * (meaning ones that may change from phase to phase) on the proxy element
     * are propagated to the {@code realElement}. Finally, the appropriate
     * method for this phase is called (ie,
     * {@code onRegistrationPhaseBeforeBody()}).
     * 
     * @return either {@code EVAL_BODY_INCLUDE} or {@code SKIP_BODY}, depending
     *         on the result of the phase method for this phase.
     * @throws JspException if any errors are encountered during the evaluation
     *         for this phase.
     * @throws IllegalArgumentException if this {@code HtmlPageElement} has an
     *         {@code Id} value that is identical to the name of another tag, or
     *         another attribute on the {@code PageContext}.
     */
    @Override
    public int doStartTag() throws JspException {
        // Generate a valid unique id from that which was provided
        // note: in many cases the existing id will remain unchanged
        IdGenerator idGen = findRealAncestorWithClass(this, IdGenerator.class);
        
        setId(idGen.getNextId(getId()));
    
        if (getPage().getPhase() == HtmlPage.REGISTRATION_PHASE) {
            // if this class is nested within an iterator tag, register it
            // with the iterator.
            HtmlPageIterator htmlPageIterator
                    = findRealAncestorWithClass(this, HtmlPageIterator.class);
            
            // during the registraion phase create and store a real element
            this.realElement = HtmlPageElement.generateRealElement(this,
                    getId(), getPage().wasRestarted(), null);
            if (htmlPageIterator != null) {
                htmlPageIterator.registerNestedElement(this.realElement);
            }
        } else {
            // During any other phase, retrieve the real element and update
            // its attributes to match those values just set on the proxy
            // element. Also update its parent just in case we're not the same
            // proxy element that served that real element last time.
            this.realElement
                    = (HtmlPageElement) pageContext.getAttribute(getId());
            this.realElement.copyTransientPropertiesFrom(this);
            this.realElement.setParentWithoutResetting(findRealParent());
        }
        return this.realElement.delegateToPhaseBeforeBody(super.pageContext);
    }

    /**
     * Overrides {@code BodyTagSupport}; is called by the servlet container
     * <strong>on the proxy element</strong> before evaluation of the body. The
     * current implementation delegates back to {@code BodyTagSupport} and if
     * this object is a proxy object, propagates the call to the corresponding
     * real object.
     */
    @Override
    public void setBodyContent(BodyContent bc) {
        super.setBodyContent(bc);
        if (this.realElement != null) {
            this.realElement.setBodyContent(bc);
        }
    }

    /**
     * Overrides {@code BodyTagSupport}; is called by the servlet container
     * <strong>on the proxy element</strong> at the end of the tag. The current
     * implementation delegates to {@code delegateToPhaseAfterBody()} which
     * calls phase-based methods. This method should not be overridden, but
     * instead the phase- implementation methods may be overridden.
     * 
     * @return {@code EVAL_PAGE} or whatever code is returned by the phase
     *         method for the real element.
     * @throws JspException if any {@code IOException}s are encountered while
     *         writing output
     */
    @Override
    public int doEndTag() throws JspException {
        return this.realElement.delegateToPhaseAfterBody(pageContext);
    }

    /**
     * In cases where the parent must be set for an 'owned' tag but it may be
     * non-intuative or even destructive to call {@code reset()} this method may
     * be used in place of {@code setParent()}.
     * 
     * @param t the parent {@code Tag}
     */
    protected void setParentWithoutResetting(Tag t) {
        super.setParent(t);
    }

    /**
     * This function can be used by subclasses to get a reference to the
     * {@code HtmlPage} object from which this class determines the phase.
     * 
     * @return a {@code HtmlPage} object representing the root page tag
     * @throws IllegalStateException if no root {@code HtmlPage} tag can be
     *         found.
     */
    public HtmlPage getPage() {
        if (page == null) {
            // Find the HtmlPage tag that encloses this one.
            this.page = (HtmlPage) TagSupport.findAncestorWithClass(
                    this, HtmlPage.class);
            if (this.page == null) {
                throw new IllegalStateException("HtmlPageElement "
                        + getId()
                        + " could not locate a parent HtmlPage");
            }
        }
        return this.page;
    }

    /**
     * <p>
     * Generates a new "real element" based upon either a) a caller-supplied
     * "proxy element" that does not already have an associated real element, or
     * b) another "real element". The newly-created real element contains a deep
     * copy of {@code sourceElement}'s key fields and is assigned the
     * {@code id} value specified by the caller. The parent tag referenced by
     * the newly-created real element is either the parent tag of the
     * {@code sourceElement}, or the "real element" associated with the parent
     * tag of the {@code sourceElement} if there is one.
     * </p>
     * <p>
     * This method invokes {@code generateCopy()} internally. The
     * {@code origToCopyMap} argument passed to that function may optionally be
     * supplied by this function's caller. If {@code origToCopyMap} is not
     * specified, this function constructs an empty map and passes that as an
     * argument to {@code generateCopy()}.
     * </p>
     * 
     * @param sourceElement a reference to the element that will be used as a
     *        template during the copy operation. This may be either a proxy
     *        element constructed by the servlet container during page
     *        evaluation or a real element created by a previous invocation of
     *        this function.
     * @param id the unique id string to be assigned to the newly-created "real
     *        element".
     * @param ignoreDuplicateIds if false, the specified {@code id} value for
     *        the new real element is checked against the list of all tags' id
     *        values stored as attributes in the {@code PageContext} and an
     *        exception is thrown if a match is found. This is useful in
     *        preventing inadvertantly duplicate {@code id} values. If this
     *        argument is true then the check for duplicates is not performed.
     * @param origToCopyMap may be null. If this argument is non-null, the
     *        caller-specified map is passed opaquely to {@code generateCopy()}
     *        and the map should conform to that function's expectations.
     * @return a reference to the newly-created "real element"
     * @throws IllegalArgumentException if the specified {@code id} is not
     *         unique and {@code ignoreDuplicateIds} is false. Also thrown if
     *         {@code sourceElement} is a "proxy element" that already has been
     *         associated with a "real element".
     */
    protected static HtmlPageElement generateRealElement(
            HtmlPageElement sourceElement, String id,
            boolean ignoreDuplicateIds, Map origToCopyMap) {
        // Do some sanity checks.
        if (sourceElement.realElement != null) {
            throw new IllegalArgumentException();
        }
    
        // Make a raw copy.
        Map map = (origToCopyMap != null) ? origToCopyMap
                : new StrictMapWrapper(new HashMap());
        HtmlPageElement newRealElement = sourceElement.generateCopy(id, map);
    
        // Patch up the raw copy to reference a sensical parent. This is
        // necessary in order to avoid problems later if Tomcat were to switch
        // proxy elements around on us.
        newRealElement.setParentWithoutResetting(
                sourceElement.findRealParent());
    
        // Register the id of the new real element.
        newRealElement.registerId(ignoreDuplicateIds);
    
        return newRealElement;
    }

    /**
     * Registers this element's ID with the applicable {@code PageContext} by
     * setting this element as a page attribute referenced by its ID.  This
     * method should be invoked only on real elements.
     *  
     * @param  allowDuplicateId if {@code true}, allows this method to replace
     *         any page attribute already assigned to the specified ID;
     *         otherwise, this method will throw an exception in that case
     *
     * @throws IllegalArgumentException if this element's ID is already
     *         associated with a page attribute
     */
    public void registerId(boolean allowDuplicateId) {
        assert getRealElement() == this;
    
        if (!allowDuplicateId && (pageContext.getAttribute(getId()) != null)) {
            throw new IllegalArgumentException(
                    "The id, \"" + getId() + "\", is already in use!");
        }
        pageContext.setAttribute(getId(), this);
    }

    /**
     * Unregisters this element's ID with the applicable {@code PageContext} by
     * removing the page attribute referenced by its ID, provided that that the
     * value of that attribute is this element. This method should be invoked
     * only on real elements.
     */
    public void unregisterId() {
        assert getRealElement() == this;
        
        if (pageContext.getAttribute(getId()) == this) {
            pageContext.removeAttribute(getId());
        }
    }
    
    /**
     * Returns a reference to the real element. The 'real' element (as opposed
     * to the 'proxy' element) exists as an attribute to the {@code PageContext}
     * througout the evaluation of the page (over many phase evaluations). On
     * the other hand, a new 'proxy' element is created during each evaluation
     * of the body (phases). When this method is called on a real-element it
     * will return a reference to {@code this}. This method only needs to be
     * used by subclasses that recognize 'proxy' and 'real' elements, and should
     * never be called before {@code HtmlPageElement.doStartTag()} or after
     * {@code HtmlPageElement.doEndTag()} have been called.
     * 
     * @return a reference the the 'real' element (can be compared with
     *         {@code this} to determine if a method has been called on the
     *         'real' element)
     */
    protected HtmlPageElement getRealElement() {
        return ((this.realElement == null) ? this : this.realElement);
    }

    /**
     * Analagous to the function {@code Tag.getParent()}, this function returns
     * the "real" parent of this tag. The "real" parent is defined to be the
     * same tag that would be returned by {@code getParent()}, or if that
     * parent tag happens to be an {@code HtmlPageElement} proxy element, then
     * real element corresponding to that proxy element. This function may be
     * invoked on either a proxy element or a real element.
     * 
     * @return the "real" parent tag of this element, or null if this tag has no
     *         parent tag.
     */
    private Tag findRealParent() {
        // Patch up the raw copy to reference a sensical parent. This is
        // necessary in order to avoid problems later if Tomcat were to switch
        // proxy elements around on us.
        Tag parent = getParent();
    
        if (parent instanceof HtmlPageElement) {
            return ((HtmlPageElement) parent).getRealElement();
        } else {
            // No translation required.
            return parent;
        }
    }

    /**
     * Similar to {@code findAncestorWithClass()} except when this version finds
     * an {@code HtmlPageElement}, it returns its {@code realElement} if it is
     * available. This method MUST be used in stead of
     * {@code findAncestorWithClass()} when dealing with {@code HtmlPageElement}
     * or its subclasses.
     * 
     * @param <T> the type of object returned by this method; inferred from the
     *        {@code klass} parameter
     * @param from The instance from where to start looking.
     * @param klass The subclass of Tag or interface to be matched.
     * 
     * @return a reference to a {@code Tag} that represents either the first
     *         ancestor of the given type, or its {@code realElement} if it is
     *         an {@code HtmlPageElement} and has a valid {@code realElement}.
     */
    protected <T> T findRealAncestorWithClass(Tag from, Class<T> klass) {
        Tag t = TagSupport.findAncestorWithClass(from, klass);
    
        if ((t instanceof HtmlPageElement)
                && (((HtmlPageElement) t).realElement != null)) {
            t = ((HtmlPageElement) t).realElement;
        }
        
        return klass.cast(t);
    }

    /**
     * This method invokes the current phase implementation method before the
     * body is evaluated and if the body is to be evaluated, is called
     * recursively on any owned elements that may have been registered by calls
     * to {@code registerAndInitializeOwnedElements()}. This method must only
     * be called on the {@code realElement}, by {@code doStartTag()}.
     * 
     * @param pageContext the {@code PageContext} that was supplied to the proxy
     *        element.
     * @return the code returned by the appropriate phase evaluation method,
     *         typically one of {@code EVAL_BODY_INCLUDE} or
     *         {@code EVAL_BODY_BUFFERED}
     * @throws JspException if any unchecked exception is encountered
     */
    private int delegateToPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        
        assert getRealElement() == this;
        
        int rc = EVAL_BODY_INCLUDE;
        
        switch (getPage().getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                rc = onRegistrationPhaseBeforeBody(pageContext);
                break;
            case HtmlPage.PARSING_PHASE:
                rc = onParsingPhaseBeforeBody(pageContext.getRequest());
                break;
            case HtmlPage.FETCHING_PHASE:
                rc = onFetchingPhaseBeforeBody();
                break;
            case HtmlPage.PROCESSING_PHASE:
                rc = onProcessingPhaseBeforeBody(pageContext);
                break;
            case HtmlPage.RENDERING_PHASE:
                try {
                    rc = onRenderingPhaseBeforeBody(pageContext.getOut());
                    break;
                } catch (IOException ex) {
                    throw new JspException(ex);
                }
        }
        if ((this.ownedElements != null) && (rc != SKIP_BODY)) {
            for (HtmlPageElement element : this.ownedElements) {
                element.delegateToPhaseBeforeBody(pageContext);
                element.delegateToPhaseAfterBody(pageContext);
            }
        }
        
        return rc;
    }

    /**
     * This method invokes the current phase implementation method after the
     * body is evaluated. This method may only be called on the
     * {@code realElement}, by {@code doEndTag()}.
     * 
     * @param pageContext the {@code PageContext} that was supplied to the proxy
     *        element.
     * @return the code returned by the appropriate phase evaluation method,
     *         typically one of {@code EVAL_BODY_INCLUDE} or
     *         {@code EVAL_BODY_BUFFERED}
     * @throws JspException if any checked exception is encountered
     */
    private int delegateToPhaseAfterBody(PageContext pageContext)
            throws JspException {
        
        assert getRealElement() == this;
        
        int rc = EVAL_PAGE;

        // Do processing appropriate for the current phase.
        switch (getPage().getPhase()) {
            case HtmlPage.REGISTRATION_PHASE:
                rc = onRegistrationPhaseAfterBody(pageContext);
                break;
            case HtmlPage.PARSING_PHASE:
                rc = onParsingPhaseAfterBody(pageContext.getRequest());
                break;
            case HtmlPage.FETCHING_PHASE:
                rc = onFetchingPhaseAfterBody();
                break;
            case HtmlPage.PROCESSING_PHASE:
                rc = onProcessingPhaseAfterBody(pageContext);
                break;
            case HtmlPage.RENDERING_PHASE:
                try {
                    rc = onRenderingPhaseAfterBody(pageContext.getOut());
                    break;
                } catch (IOException ex) {
                    throw new JspException(ex);
                }
        }
        
        return rc;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class before executing their own code.
     * 
     * @param pageContext the current {@code PageContext} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doStartTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class after executing their own code.
     * 
     * @param pageContext the current {@code PageContext} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doEndTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onRegistrationPhaseAfterBody(PageContext pageContext)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_PAGE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class before executing their own code.
     * 
     * @param request the current {@code ServletRequest} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doStartTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onParsingPhaseBeforeBody(ServletRequest request)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class after executing their own code.
     * 
     * @param request the current {@code ServletRequest} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doEndTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_PAGE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class before executing their own code.
     * 
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doStartTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onFetchingPhaseBeforeBody() throws JspException {
        assert getRealElement() == this;
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class after executing their own code.
     * 
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doEndTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onFetchingPhaseAfterBody() throws JspException {
        assert getRealElement() == this;
        return EVAL_PAGE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class before executing their own code.
     * 
     * @param pageContext the current {@code PageContext} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doStartTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class after executing their own code.
     * 
     * @param pageContext the current {@code PageContext} may be used by
     *        overriding methods.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doEndTag()} method, though it may be overridden by
     *         subclasses 
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        assert getRealElement() == this;
        return EVAL_PAGE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class before executing their own code.
     * 
     * @param out a {@code JspWriter} that may be used by subclasses to output
     *        HTML generated by this {@code HtmlPageElement}.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doStartTag()} method, though it may be overridden by
     *         subclasses 
     * @throws IOException if there is a problem writing to the
     *         {@code JspWriter}.
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        assert getRealElement() == this;
        return EVAL_BODY_INCLUDE;
    }

    /**
     * Phase implementation; subclasses may override this so that their code
     * will be invoked during the appropriate phase. In this case, subclasses
     * must delegate back to this class after executing their own code.
     * 
     * @param out a {@code JspWriter} that may be used by subclasses to output
     *        HTML generated by this {@code HtmlPageElement}.
     * @return the code to be returned during this phase by this tag handler's
     *         {@code doEndTag()} method, though it may be overridden by
     *         subclasses 
     * @throws IOException if there is a problem writing to the
     *         {@code JspWriter}.
     * @throws JspException if an exception is encountered during this method.
     */
    @SuppressWarnings("unused")
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        assert getRealElement() == this;
        return EVAL_PAGE;
    }

    /**
     * This method should be called by subclasses that instantiate 'owned'
     * elements. Each 'owned' element that is registered with this method will
     * have its phase implementation methods called on it for each phase where
     * the owning element evaluates its body. If an 'owning' class wishes to
     * have more specific control over the calling sequence it may return
     * {@code SKIP_BODY}. This method sets the parent of the 'owned' element to
     * be this element, and if the id value is unset (null), sets the id to be
     * one derived from the id of the parent, but the caller is responsible for
     * all other initializations, included a call to {@code reset()}. The
     * current implementation will invoke phase methods on the 'owned' element
     * in the order they were registered. If the caller has set the 'id'
     * property on the 'owned' element before registering it with this method,
     * its value will not be modified by any other source, an all copies of this
     * element made by {@code generateCopy()} will have 'owned' elements with
     * identical names. This is useful in the case where the 'id' value is used
     * in generated HTML and must be a certain value, but the implications must
     * be considered. (ie, if the element whose 'owned' element has a fixed
     * value is nested in an {@code HtmlPageIterator} the HTML output by each
     * iteration may be identical, causing multiple sources for a single form
     * POST parameter)
     * 
     * @param owned the {@code HtmlPageElement} that is 'owned' by this control.
     *        It is logically equivalent to a nested tag, except that it's
     *        methods are called by {@code HtmlPageElement} rather than the
     *        servlet container.
     */
    protected void registerOwnedElement(HtmlPageElement owned) {
        if (this.ownedElements == null) {
            this.ownedElements = new ArrayList<HtmlPageElement>();
            this.ownedElementsPreserveIds = new ArrayList<HtmlPageElement>();
        }
        owned.setParentWithoutResetting(this);
        owned.setPageContext(this.pageContext);
        if (owned.getId() == null) {
            owned.setId(getId() + "-" + (this.ownedElements.size()));
        } else {
            this.ownedElementsPreserveIds.add(owned);
        }
        this.ownedElements.add(owned);
    }

    /**
     * Gets a {@code Collection} of all 'owned' elements with the class
     * {@code klass}. Elements must have been registered by
     * {@code registerOwnedElements()} to be included on the returned
     * collection.
     * 
     * @param <T> the type of owned elements requested; this type parameter can
     *        always be inferred from the {@code klass} argument
     * @param klass the {@code Class<T>} representing the class or interface to
     *        which all returned elements must belong.
     * @return a {@code Collection<T>} of the owned elements registered with
     *         this tag that have the specified type. The list may be empty, but
     *         it will never be {@code null}.
     */
    protected <T> Collection<T>
            getRegisteredOwnedElementsOfType(Class<T> klass) {
        Collection<T> result = new ArrayList<T>();
        
        if (this.ownedElements != null) {
            for (HtmlPageElement element : this.ownedElements) {
                if (klass.isAssignableFrom(element.getClass())) {
                    result.add(klass.cast(element));
                }
            }
        }
    
        return result;
    }

    /**
     * Configures this element to allow the ID of the specified owned element to
     * be updated, even if it was not originally auto-assigned; has no effect if
     * the specified element is not owned by this one or originally did have its
     * ID auto-assigned, provided that this element has (or ever did have) any
     * owned elements at all.
     *   
     * @param  owned the owned {@code HtmlPageElement} whose ID should
     *         henceforth be auto-assignable
     */
    protected void allowIdUpdate(HtmlPageElement owned) {
        if (this.ownedElementsPreserveIds == null) {
            throw new IllegalStateException();
        } else {
            ownedElementsPreserveIds.remove(owned);
        }
    }
    
    /**
     * A private helper function that reassigns id values for all registered
     * 'owned' controls that don't have fixed ids based on the current id of the
     * owning control. This method should be called whenever an 'owned' control
     * is unregistered or whenever the owning control has its id value changed.
     * Note: this method will NOT alter id values on 'owned' controls that were
     * set before {@code registerOwnedElement()} was called.
     */
    private void reassignOwnedElementIds() {
        if (this.ownedElements != null) {
            int i = 0;
            
            for (HtmlPageElement next : this.ownedElements) {
                if (!this.ownedElementsPreserveIds.contains(next)) {
                    next.setId(getId() + "-" + (i++));
                }
            }
        }
    }

    /**
     * This method negates a previous call to
     * {@code registerAndInitializedOwnedElement()}.
     * 
     * @param owned an {@code HtmlPageElement} that has already been passed to
     *        {@code registerAndInitializeOwnedElement()}.
     * @throws IllegalStateException if no 'owned' elements have been registered
     * @throws IllegalArgumentException if {@code owned} has not been registered
     */
    protected void unregisterOwnedElement(HtmlPageElement owned) {
        if (this.ownedElements == null) {
            throw new IllegalStateException();
        }
        if (!this.ownedElements.remove(owned)) {
            throw new IllegalArgumentException();
        }
        this.ownedElementsPreserveIds.remove(owned);
        reassignOwnedElementIds();
    }

    /**
     * Copies properties, identified in the TLD, from the source control to this
     * one. This method is used to update the {@code realElement} with
     * attributes that may have been reset on the proxy element. Therefore only
     * member variables that are exposed as attributes need to be copied by this
     * method. Subclasses should override this function, but must delegate back
     * to to this class. The current implementation is an empty implementation
     * because no transient elements exists for {@code HtmlPageElement}.
     * Implementations are expected to set values via their setter methods to
     * ensure that subclasses are correctly initialized.
     * 
     * @param source an {@code HtmlPageElement} or subclass whose transient
     *        fields are being copied to this object.
     */
    protected void copyTransientPropertiesFrom(
            @SuppressWarnings("unused") HtmlPageElement source) {
        // this version does nothing
    }

    /**
     * <p>
     * Generates a deep copy of this {@code HtmlPageElement} and assigns it the
     * specified id value. {@code HtmlPageElement}'s base implementation
     * updates the provided map with a mapping from the element on which this
     * method was called to it's deep copy. All ancestors of this
     * {@code HtmlPageElement} that are not currently in the map will be
     * inserted into the map with values equal to the keys. Furthermore, if this
     * {@code HtmlPageElement} has any 'owned' elements, deep copies of them are
     * made, added to the provided map and their id values are updated through a
     * call to {@code reassignOwnedElementIds()}. The resulting map may be used
     * by subclass implementations to reset references to 'owned' controls or
     * tags in the ancestry.
     * </p><p>
     * Subclasses that maintain a reference to their owned elements must not
     * create deep copies of them, but instead should delegate back to this
     * implementation first, so that the provided map may be populated.
     * </p><p>
     * Subclasses that have member variables that reference other tags as
     * discovered through calls to
     * {@code HtmlPageElement.findRealAncestorWithClass()} must update those
     * references on the deep copy with ones with which the caller was required
     * to pre-populate the {@code Map} passed to this method.
     * </p>
     * 
     * @param newId the id value that should be assigned to the deep copy. In
     *        the case of creating a real element from a proxy element this
     *        value will be the same as the existing id. This value may be null
     *        if there is an assurance that {@code setId()} will be called
     *        before the id could be used.
     * @param origToCopyMap a {@code Map} (which must support null keys),
     *        populated to contain mappings from any tags that have been copied
     *        as part of a larger copy operation to their new copies. The
     *        {@code HtmlPageElement} implementation of this method (to which
     *        all subclasses should first delegate) will further populate it to
     *        contain mappings for this tag, its owned elements and all
     *        currently missing ancestor tags that may be referenced by
     *        subclasses. References to {@code HtmlPageElement} objects in this
     *        map must never be 'proxy' elements, but instead must be the
     *        corresponding 'real' element.
     * @return a deep copy of this {@code HtmlPageElement}, possibly with an
     *         updated id value.
     */
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        HtmlPageElement deepCopy = null;

        try {
            deepCopy = (HtmlPageElement) clone();
        } catch (CloneNotSupportedException cnse) {
            // this can't happen because HtmlPageElements are cloneable
            throw new RuntimeException(cnse);
        }

        origToCopyMap.put(this, deepCopy);
        for (Tag parent = getParent(); parent != null;
                parent = parent.getParent()) {
            Tag refToStore = parent;

            if ((refToStore instanceof HtmlPageElement)
                    || (refToStore instanceof HtmlPage)) {
                if (refToStore instanceof HtmlPageElement) {
                    // the origToCopyMap should contain references to 'real'
                    // elements only, so if the tag in question is an
                    // HtmlPageElement make sure that it is the 'real' element
                    refToStore
                            = ((HtmlPageElement) refToStore).getRealElement();
                }
                if (!origToCopyMap.containsKey(refToStore)) {
                    origToCopyMap.put(refToStore, refToStore);
                }
            }
        }

        if (this.ownedElements != null) {
            // deep copy all 'owned' elements
            deepCopy.ownedElements = new ArrayList<HtmlPageElement>();
            for (HtmlPageElement owned : this.ownedElements) {
                HtmlPageElement copyOfOwnedElementForDeepCopy
                        = owned.generateCopy(null, origToCopyMap);

                copyOfOwnedElementForDeepCopy.setParentWithoutResetting(
                        deepCopy);
                deepCopy.ownedElements.add(copyOfOwnedElementForDeepCopy);
                origToCopyMap.put(owned, copyOfOwnedElementForDeepCopy);
            }

            // translate the collection of references to 'owned' elements whose
            // id values are not meant to be updated
            deepCopy.ownedElementsPreserveIds
                    = new ArrayList<HtmlPageElement>();
            for (HtmlPageElement owned : this.ownedElementsPreserveIds) {
                deepCopy.ownedElementsPreserveIds.add(
                        (HtmlPageElement) origToCopyMap.get(owned));
            }
        }
        // set the id of the deep copy, which will in turn set the id of all
        // copied 'owned' elements
        deepCopy.setId(newId);

        return deepCopy;
    }

    /**
     * Creates and returns a String representation of this element; intended for
     * debugging only
     * 
     * @return a String representation of this element
     */
    @Override
    public String toString() {
        String rawName = super.toString();
        StringBuilder newName = new StringBuilder();

        for (int i = 0; i < rawName.length(); i++) {
            if (Character.isUpperCase(rawName.charAt(i))) {
                newName.append(rawName.charAt(i));
            }
        }
        newName.append(rawName.substring(rawName.length() - 3));

        return newName.toString();
    }
}
