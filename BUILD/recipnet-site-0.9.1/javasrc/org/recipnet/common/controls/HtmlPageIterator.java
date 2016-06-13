/*
 * Reciprocal Net project
 * 
 * HtmlPageIterator.java
 * 
 * 22-Jun-2004: midurbin wrote the first draft
 * 29-Jun-2004: midurbin added scripting variable support to nested elements by
 *              modifying registerNestedElement(), initializeIteration() to
 *              include calls to new methods on IterationInfo to enable
 *              scripting variables.
 * 23-Jul-2004: midurbin added getIterationCountSinceThisPhaseBegan()
 * 30-Jul-2004: midurbin added generateCopy() methods to this class an
 *              IterationInfo as part of fixing bug #1255.
 * 05-Aug-2004: modified the spec of generateId() and degenerateId() and
 *              updated all calling methods to conform, fixing bug #1312
 * 13-Aug-2004: midurbin fixed bug #1322 by moving iterationIndex to the
 *              'realElement' and updating IterationInfo.generateCopy() to
 *              ensure that parent values were properly updated
 * 20-Aug-2004: midurbin fixed bug #1342
 * 30-Sep-2004: jobollin fixed bug #1405 by removing unused import of
 *              org.recipnet.site.UnexpectedExceptionException
 * 10-Mar-2005: midurbin fixed bug #1462 in doEndTag()
 * 01-Apr-2005: midurbin updated generateCopy(), IterationInfo.generateCopy()
 *              and IterationInfo.createIterationInfoForNextIteration() to fix
 *              bug #1455
 * 10-Jun-2005: midurbin changed spec of beforeIteration(), afterIteration() to
 *              throw a JSPException, added fuller support for subclass
 *              doStartTag() return codes
 * 21-Jun-2005: midurbin added isCurrentIterationLast() and an ErrorSupplier
 *              implementation with the NO_ITERATIONS error flag
 * 24-Jun-2005: midurbin modified doStartTag() to ensure at least one iteration
 *              during the PARSING_PHASE whether or not a
 *              'postedIterationCount' was parsed.
 * 28-Jun-2005: ekoperda fixed bug #1621 in createIterationInfoForIndex() of
 *              nested class
 * 13-Mar-2006: jobollin added isCurrentIterationFirst(); removed unused
 *              imports; reformatted the source
 * 27-Mar-2006: jobollin added deleteCurrentIteration() and support
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.recipnet.common.StrictMapWrapper;

/**
 * <p>
 * An {@code HtmlPageElement} extension that supports multiple evaluations of
 * its body. This class may be easily extended to allow various types of
 * iterator tags without consideration of complex implementation details.
 * Normally, a phase-recognizing tag will have undefined behvavior if for some
 * reason it is not evaluated during the same page phases as all other tags on a
 * given page. This problem manifests inself in iterator tags when the number of
 * iterations is unknown until the {@code PARSING_PHASE} or the
 * {@code FETCHING_PHASE}. To overcome this problem, the implementation of this
 * class guarantees that during the {@code REGISTRATION_PHASE} every element
 * within the body is evaluated exactly once, and a reference to each object is
 * maintained. If the number of body evaluations (iterations) increases at a
 * later time, these elements can be cloned to create the 'real' elements that
 * would have been generated during previous phases. As a side effect of this
 * behavior, if the number of iterations is reduced, it may not later be
 * increase as the 'real' elements for some iterations might have missed
 * evaluation for a phase.
 * </p><p>
 * This class is meant to be extended and provides methods to control the number
 * of times the body is evaluated:
 * <ol>
 * <li>if a phase on______PhaseBeforeBody() method returns
 * {@code EVAL_BODY_INCLUDE} the the following methods will be invoked.
 * {@code beforeIteration()}, {@code onIterationBeforeBody()} and
 * {@code afterIteration()}.</li>
 * <li>if {@code onIterationBeforeBody()} returns true, the body will be
 * evaluated and then {@code onIterationAfterBody()} will be called. If
 * {@code onIterationAfterBody()} returns true, {@code onIterationBeforeBody()}
 * will be called again, and its return value will determine whether the body
 * will be evaluated again. These method will be alternately called, surrounding
 * an evaluation of the body until one of them returns false, causing iteration
 * of the body to cease and {@code afterIteration()} to finally be called.</li>
 * </ol>
 * </p><p>
 * It is a requirement that either {@code onIterationBeforeBody()} or
 * {@code onIterationAfterBody()} is overridden to control the number of
 * iterations. The joint behavior of the two unmodified methods results in an
 * infinite loop.
 * </p>
 */
public abstract class HtmlPageIterator extends HtmlPageElement implements
        IdGenerator, ErrorSupplier {

    /**
     * An error flag that indicates that this tag has no {@code SampleTextInfo}
     * objects over which to iterate because none exist that match the given
     * restriction properties.
     */
    public static final int NO_ITERATIONS = 1 << 0;

    /**
     * Used by the proxy element during {@code doStartTag()} and
     * {@code doAfterBody()} to indicate whether {@code doBeforeIteration()} and
     * thus {@code doAfterIteration()} need to be called. Because of this narrow
     * scope, this variable may be used on the 'proxy' element and will never be
     * used on the 'real' element.
     */
    private boolean considerEvaluatingBody;

    /**
     * Set by {@code doStartTag()} in the even that the subclass did not request
     * an evaluation of the body but one was needed to allow nested elements to
     * register. If set, this variable will prevent
     * {@code onPhaseEvaluationBeforeBody()} and {@code onIterationAfterBody()}
     * from being invoked by {@code doStartTag()} and {@code doAfterBody()}.
     * Because of this narrow scope, this variable may be used on the 'proxy'
     * element and will never be used on the 'real' element.
     */
    private boolean forcedEvaluationToAllowNestedElementsToRegister;

    /**
     * A count, indicating the current iteration number. Initialized to zero by
     * {@code doStartTag()} and incremented before {@code doAfterBody()} returns
     * {@code EVAL_BODY_AGAIN}, this variable is not specified for the 'proxy'
     * element, but is maintained and available on the 'real' element.
     */
    private int iterationIndex;

    /**
     * The number of times {@code getNextId()} has been called. Used in the
     * generation of unique (between iterations) id values.
     */
    private int autoId;

    /**
     * An ordered {@code List} of {@code IterationInfo} objects whose 'index'
     * value matches their index in the {@code List} and corresponds their
     * iteration index. No items are deleted from this {@code List} which
     * results in the requirement that the number of terations each phase may
     * increase or decrease any number of times but may not increase after it
     * has decreased.
     */
    private List<IterationInfo> iterationInfoList;

    /**
     * Represents a {@code IterationInfo} object for the current iteration. This
     * variable, maintained by the 'real' element is set and modified by calls
     * to {@code initializeIteration()} and should not be modified in any other
     * place.
     */
    private IterationInfo currentIterationInfo;

    /**
     * Serves as an {@code IterationInfo} that could be cloned to form the
     * {@code currentIterationInfo} for the next iteration in the event that it
     * wouild be the highest iteration index so far. In such cases it is
     * important that this copy was made from a {@code IterationInfo} for an
     * iteration that has not yet taken place during this phase. Like
     * {@code currentIterationInfo} this object is maintained by
     * {@code initializeIteration()}.
     */
    private IterationInfo potentialNextIterationInfo;

    /**
     * Initialized by {@code reset()} and updated at the end of
     * {@code doEndTag()} this indicator of the last phase that was fully
     * evaluated is useful to check that {@code RepositoryInfo} objects were
     * evaluated during every possible phase.
     */
    private int lastPhaseEvaluated;

    /**
     * The number of iterations of the body of this tag that were performed
     * during the {@code RENDERING_PHASE} before the form was posted. This value
     * is updated by {@code doStartTag()} on the {@code PARSING_PHASE} and
     * returned by {@code getPostedIterationCount()}.
     */
    private int postedIterationCount;

    /**
     * A variable that indicates whether this tag is in the process of iterating
     * over its body. This variable (on the 'real' element) is set to true at
     * the start of the 'proxy' element's method {@code doBeforeBody()} and set
     * to false at the end of {@code doAfterBody()}. Other sections of code may
     * check this variable to determine whether certain state variables have
     * meaningful values. (ie, {@code currentIterationInfo},
     * {@code potentialNextIterationInfo} and {@code iterationIndex})
     */
    private boolean iterationInProgress;
    
    /**
     * A flag raised by {@link #deleteCurrentIteration()} to signal that the
     * iteration info for the current iteration should be deleted at the end of
     * the current phase 
     */
    private boolean deleteIterationInfo;

    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();

        // single phase scope - may be used only on proxy element
        this.considerEvaluatingBody = false;
        this.forcedEvaluationToAllowNestedElementsToRegister = false;

        // entire tag scope - must be used only on real element
        this.autoId = 0;
        this.iterationInfoList = null;
        this.iterationIndex = 0;
        this.currentIterationInfo = null;
        this.potentialNextIterationInfo = null;
        this.lastPhaseEvaluated = HtmlPage.INVALID_PHASE;
        this.postedIterationCount = 0;
        this.iterationInProgress = false;
        this.deleteIterationInfo = false;

        // ErrorSupplier implementation
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation ensures
     * that a couple conditions neccessary for {@code HtmlPageIterator} to work
     * properly are met.
     * 
     * @return {@code EVAL_BODY_INCLUDE} if the superclass' implementation
     *         returned {@code EVAL_BODY_INCLUDE} and
     *         {@code onIterationBeforeBody()} returned true, or if this would
     *         be the first evaluation of the body ever (which would imply that
     *         it is the {@code REGISTRATION_PHASE}.
     * @throws JspException if tag initialization fails
     * @throws IllegalStateException if it is the {@code PARSING_PHASE} and
     *         there is no valid request parameter indicating the posted
     *         iteration count
     */
    @Override
    public int doStartTag() throws JspException {
        int evalCode = super.doStartTag();
        HtmlPageIterator realElement = (HtmlPageIterator) getRealElement();
        
        this.considerEvaluatingBody = (evalCode != SKIP_BODY);
        realElement.iterationInProgress = true;
        realElement.iterationIndex = 0;
    
        if (getPage().getPhase() == HtmlPage.PARSING_PHASE) {
            String itCountStr = this.pageContext.getRequest().getParameter(
                    getId() + "_iterationCount");
            if (itCountStr == null) {
                /*
                 * If for some reason the number of iterations wasn't posted we
                 * will not be able to evaluate the body the correct number of
                 * times during the parsing phase to parse all nested fields but
                 * we must evaluate the body at least once to ensure that later
                 * increases are possible.
                 */
                realElement.postedIterationCount = 1;
            } else {
                realElement.postedIterationCount = Integer.parseInt(itCountStr);
            }
        }
    
        if (this.considerEvaluatingBody) {
            realElement.beforeIteration();
            /*
             * the final decision about whether the body should be evaluated is
             * left up to the overridable method, onIterationBeforeBody() even
             * if the superclass' implementation of doStartTag() returned
             * EVAL_BODY_INCLUDE (as indicated by this.considerEvaluatingBody)
             */
            if (realElement.onIterationBeforeBody()) {
                this.forcedEvaluationToAllowNestedElementsToRegister = false;
                realElement.initializeIteration();
                return evalCode;
            }
        }
    
        if (realElement.iterationInfoList == null) {
            /*
             * the body must evaluated at least once during the
             * REGISTRATION_PHASE regardless of whether the superclass'
             * implementation returned SKIP_BODY or onIterationBeforeBody()
             * returns false. forcedEvaluationToAllowNestedElementsToRegister is
             * set to indicate that none of the iteration-related methods should
             * be invoked.
             */
            this.forcedEvaluationToAllowNestedElementsToRegister = true;
            realElement.initializeIteration();
            return evalCode;
        } else {
            // Otherwise, if we got here then don't process the body
            return SKIP_BODY;
        }
        
        // Control cannot reach this point
    }

    /**
     * Overrides {@code BodyTagSupport}; the current implementation updates
     * {@code lastPhaseEvaluated} on the 'real' element and if needed calls
     * {@code onIterationAfterBody()} and {@code onIterationBeforeBody()} on the
     * 'real' element to determine whether to return {@code SKIP_BODY} or
     * {@code EVAL_BODY_AGAIN}. In the event that the body is to be evaluated
     * again, {@code initializeIteration()} is called on the 'real' element
     * using an incremented {@code iterationIndex}.
     * 
     * @return SKIP_BODY or EVAL_BODY_AGAIN to indicate whether the body should
     *         be evaluated again based on the the results of
     *         {@code onIterationAfterBody()} and
     *         {@code onIterationBeforeBody()}.
     */
    @Override
    public int doAfterBody() {
        HtmlPageIterator realElement = (HtmlPageIterator) getRealElement();
        
        if (this.forcedEvaluationToAllowNestedElementsToRegister) {
            // evaluation was forced to ensure a single evaluation during the
            // registration phase, but because the evaluation was not requested
            // by the subclass, the iteration methods need not be called
            
            assert !realElement.deleteIterationInfo;
            
            realElement.iterationIndex++;
            realElement.currentIterationInfo.lastPhaseEvaluated
                    = getPage().getPhase();
            
            return SKIP_BODY;
        } else {
            // Perform the afterBody code first
            boolean again = realElement.onIterationAfterBody();
            
            if (realElement.deleteIterationInfo) {
                
                // Handle iteration deletion
                
                realElement.deleteIterationInfo = false;
                
                assert (realElement.iterationIndex
                        < realElement.iterationInfoList.size());
                
                ListIterator<IterationInfo> it
                        = realElement.iterationInfoList.listIterator(
                                realElement.iterationIndex);
                
                for (HtmlPageElement element : it.next().realElements) {
                    element.unregisterId();
                }
                
                it.remove();
                while (it.hasNext()) {
                    it.next().setNewIndex(it.previousIndex());
                }
                if (realElement.potentialNextIterationInfo != null) {
                    realElement.potentialNextIterationInfo.setNewIndex(
                            realElement.iterationInfoList.size());
                }
                
                // the iterationIndex and currentIterationInfo are NOT updated
                
            } else {
                realElement.iterationIndex++;
                realElement.currentIterationInfo.lastPhaseEvaluated
                        = getPage().getPhase();
            }
            
            // If necessary, perform beforeBody test for a new iteration
            again = (again && realElement.onIterationBeforeBody());
            
            if (again) {
                realElement.initializeIteration();
                return EVAL_BODY_AGAIN;
            } else {
                return SKIP_BODY;
            }
        }
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation updates
     * {@code lastPhaseEvaluated} on the 'real' element and makes sure that
     * {@code afterIteration()} is called (if it needs to be) before delegating
     * back to the superclass.
     * 
     * @return SKIP_PAGE or EVAL_PAGE to indicate whether the rest of the page
     *         should be evaluated; typically EVAL_PAGE
     *         
     * @throws JspException wrapping an IOException if an error is encountered
     *         while outputting HTML during the {@code RENDERING_PHASE}
     */
    @Override
    public int doEndTag() throws JspException {
        HtmlPageIterator realElement = (HtmlPageIterator) getRealElement();
        
        if (this.considerEvaluatingBody) {
            realElement.afterIteration();
        }
        if (realElement.iterationIndex == 0) {
            realElement.setErrorFlag(NO_ITERATIONS);
        }
        if (getPage().getPhase() == HtmlPage.RENDERING_PHASE) {
            // output a hidden field that will be parsed after the POST to set
            // the value of 'postedIterationCount'
            try {
                this.pageContext.getOut().print(
                        "<input type=\"hidden\"" + " id=\"" + getId()
                                + "_iterationCount\" name=\"" + getId()
                                + "_iterationCount\" value=\""
                                + realElement.iterationIndex + "\" />");
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
        realElement.lastPhaseEvaluated = getPage().getPhase();
        realElement.iterationInProgress = false;
        
        return super.doEndTag();
    }

    /**
     * Gets the number of iterations this tag performed during the
     * {@code RENDERING_PHASE} before the last post. This method may only be
     * called during the {@code PARSING_PHASE} or the {@code PROCESSING_PHASE}.
     * 
     * @return the number of iterations of this tag before the POST
     * @throws IllegalStateException if it is not currently the
     *         {@code PARSING_PHASE} or the {@code PROCESSING_PHASE}.
     */
    public int getPostedIterationCount() {
        assert getRealElement() == this;
        
        if ((getPage().getPhase() != HtmlPage.PARSING_PHASE)
                && (getPage().getPhase() != HtmlPage.PROCESSING_PHASE)) {
            throw new IllegalStateException();
        }
        
        return this.postedIterationCount;
    }

    /**
     * Gets the number of times the body has been evaluated so far this phase.
     * Initialized to zero by {@code reset()} and updated during
     * {@code onIterationAfterBody()}.
     * 
     * @return the number of iterations performed so far this phase
     */
    public int getIterationCountSinceThisPhaseBegan() {
        return this.iterationIndex;
    }

    /**
     * A helper method that must be called on the 'realElement' before each
     * evaluation of the body (in response to {@code onIterationBeforeBody()}
     * returning true). This method manages {@code iterationInfoList} and sets
     * the values of {@code currentIterationInfo} and
     * {@code potentialNextIterationInfo}.
     * 
     * @throws IllegalStateException in the event that the number of iterations
     *         increased following a decrease in the number of iterations which
     *         is not supported behavior.
     */
    private void initializeIteration() {
        assert getRealElement() == this;
        this.autoId = 0;
        
        if (getPage().getPhase() == HtmlPage.REGISTRATION_PHASE) {
            
            // during the REGISTRATION_PHASE IterationInfo object from the
            // default constructor can be used with little modification because
            // its realElements collection will be populated by calls to
            // registerNestedElement()
            if (this.iterationInfoList == null) {
                assert iterationIndex == 0;
                this.iterationInfoList = new ArrayList<IterationInfo>();
            }
            this.currentIterationInfo = new IterationInfo();
            this.currentIterationInfo.index = iterationIndex;
            this.currentIterationInfo.lastPhaseEvaluated
                    = HtmlPage.INVALID_PHASE;
            this.currentIterationInfo.iteratorElement = this;
            this.iterationInfoList.add(this.currentIterationInfo);
            this.potentialNextIterationInfo = null;
        } else if (this.iterationInfoList.size() > iterationIndex) {
            
            // an IterationInfo already exists for this iteration, ensure that
            // it had its last phase evaluated and use it
            this.currentIterationInfo
                    = this.iterationInfoList.get(iterationIndex);
            if (this.currentIterationInfo.lastPhaseEvaluated
                    != this.lastPhaseEvaluated) {
                
                // if there is a mismatch here, it is the result of there being
                // fewer evalations of the body last time than this time, but
                // more the time before that (explaining the presence of the
                // IterationInfo for this index).
                throw new IllegalStateException();
            } else if (this.iterationInfoList.size() <= (iterationIndex + 1)) {
                // if there is a next iteration, it will be a new one
                this.potentialNextIterationInfo = 
                        this.currentIterationInfo.createIterationInfoForIndex(
                                (iterationIndex + 1));
            } else {
                // if there is a next iteration, it will be for an existing
                // IterationInfo and no potentialNextIterationInfo will be
                // needed
            }
            
            // the following call allows the real elements for this iteration
            // to be accessible as scripting variables
            this.currentIterationInfo.enableScriptingVariablesForThisIteration(
                    super.pageContext);
        } else {
            
//          assert (iterationIndex != 0) : "zeroth iteration is new on phase "
//          + getPage().getPhase();

            // This iteration is a new high index and requires a the
            // IterationInfo object that was generated and referenced as
            // potentialNextIterationInfo. In case of future iterations, a
            // new potentialNextIterationInfo should be generated.
            assert (this.potentialNextIterationInfo != null)
                    : ("no prepared next iteration info on phase "
                            + getPage().getPhase());
            this.currentIterationInfo = this.potentialNextIterationInfo;
            this.potentialNextIterationInfo
                    = this.currentIterationInfo.createIterationInfoForIndex(
                            (iterationIndex + 1));
            this.iterationInfoList.add(this.currentIterationInfo);
            
            // the following call allows the real elements for this iteration
            // to be accessible as scripting variables
            this.currentIterationInfo.enableScriptingVariablesForThisIteration(
                    super.pageContext);
        }
    }

    /**
     * Determines whether this tag is currently processing iterations
     * 
     * @return the {@code true} if this tag is currently iterating;
     *         {@code false} otherwise
     */
    protected boolean isIterationInProgress() {
        return iterationInProgress;
    }

    /**
     * Determines whether this is the first iteration of this iterator in the
     * current phase
     * 
     * @return {@code true} if the iteration count in this phase is 0,
     *         {@code false} otherwise
     */
    public boolean isCurrentIterationFirst() {
        return (getIterationCountSinceThisPhaseBegan() == 0);
    }
    
    /**
     * Determines whether the current iteration is the last iteration (during
     * the current phase). This method is meant to be available to be
     * overridden by subclasses that have strict rules about when and how the
     * iteration count increases. This method is unimplemented in this abstract
     * base class and throws an {@code UnsupportedOperationException}.
     * 
     * @return {@code true} if the current iteration of this tag is the last one
     *         this phase; {@code false} if not 
     * 
     * @throws UnsupportedOperationException if it is not possible for this tag
     *         to determine if this is the last iteration or not.
     */
    public boolean isCurrentIterationLast() {
        throw new UnsupportedOperationException();
    }

    /**
     * A dummy implementation meant to be overridden by subclasses. This method
     * will be called after {@code on____PhaseBeforeBody()} if it returns
     * {@code EVAL_BODY_INCLUDE} and before {@code onIterationBeforeBody()}.
     * This method exists to be overridden to include pre-iteration
     * initializations.
     * 
     * @throws JspException wrapping any checked exceptions thrown during the
     *         beforeIteration() implementation
     */
    @SuppressWarnings("unused")
    protected void beforeIteration() throws JspException {
        // this version does nothing
    }

    /**
     * A dummy implementation meant to be overridden by subclasses. This method
     * will be called right after {@code beforeIteration()} or right after
     * {@code onIterationAfterBody()} (when true is returned) to determine
     * whether the body should be evaluated (again). If this method returns
     * true, it will initiate a body evaluation and then
     * {@code onIterationAfterBody()} will be called. If this method returns
     * false, {@code afterIteration()} will be called next and there will be no
     * more iterations this phase.
     * 
     * @return true to indicate that the body should be evaluated or false to
     *         prevent the body from being evaluated. The current implementation
     *         always returns true.
     */
    protected boolean onIterationBeforeBody() {
        return true;
    }

    /**
     * A dummy implementation meant to be overridden by subclasses. This method
     * will be called right after {@code onIterationBeforeBody()} and the
     * resulting evaluation of the body to determine whether another evaluation
     * of the body should be considered. If this method returns true,
     * {@code onIterationBeforeBody()} will be called next, otherwise
     * {@code afterIteration()} will be called.
     * 
     * @return this dummy implementation always returns true, effectively
     *         passing the decision about whether to evaluate the body again to
     *         the {@code onIterationBeforeBody()} method.
     */
    protected boolean onIterationAfterBody() {
        return true;
    }

    /**
     * A dummy implementation meant to be overridden by subclasses. This method
     * is aways invoked if {@code beforeIteration()} was invoked whether or not
     * any body evaluations were performed after the first of
     * {@code onIterationBeforeBody()} or {@code onIterationAfterBody()} to
     * return false.
     * 
     * @throws JspException wrapping any checked exceptions thrown during the
     *         afterIteration() implementation
     */
    @SuppressWarnings("unused")
    protected void afterIteration() throws JspException {
        // This version does nothing
    }

    /**
     * Causes the iteration currently in progress to be deleted at the end of
     * the current body evaluation; real elements registered for this iteration
     * number are discarded, and those of subsequent iterations are assigned new
     * IDs; must not be invoked during the {@code REGISTRATION_PAHSE}.  This
     * method may not be appropriate for use on all subclasses.
     * 
     * @throws JspException if this tag is not currently evaluating its body
     */
    protected void deleteCurrentIteration() throws JspException {
        if (!isIterationInProgress()
                || (getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)) {
            throw new JspException();
        } else {
            deleteIterationInfo = true;
        }
    }
    
    /**
     * This method is called by all {@code HtmlPageElement} objects during the
     * {@code REGISTRATION_PHASE} so that the characteristics of the nested
     * elements for a single iteration of the body are known and can later be
     * used to generate the state that would be consistant with any number of
     * iterations of the body. Note: any subclasses that have owned elements
     * should overload this method to intercept and ignore the calls by owned
     * elements that are meant to pertain to the ENTIRE iterator rather than for
     * each iteration.
     * 
     * @param nestedElement any {@code HtmlPageElement} that finds itself within
     *        a {@code FieldIterator} during the {@code REGISTRATION_PHASE}.
     */
    public void registerNestedElement(HtmlPageElement nestedElement) {
        assert getRealElement() == this;
        assert getPage().getPhase() == HtmlPage.REGISTRATION_PHASE;
        this.currentIterationInfo.realElements.add(nestedElement);
    
        /*
         * HtmlPageElement has already stored this element as an attribute to
         * the PageContext which would make it available as a scripting variable
         * except that the id value has been altered. initializeForIteration()
         * may not serve to register this element under its scripting variable
         * id because each element had not yet been included (via calls to
         * registerNestedElements()) in the IterationInfo object at the
         * beginning of the body evaluation. Therefore, this call is needed to
         * make the given tag available as a scripting variable immediately.
         */
        IterationInfo.enableScriptingVariable(nestedElement,
                degenerateId(nestedElement.getId()), super.pageContext);
    }

    /**
     * Determines the actual ID assigned during this iteration to the copy of a
     * nested element that declares the specified ID. The element referenced by
     * the specified ID may be hypothetical; in that case this method returns
     * the ID that <em>would</em> have been assigned to it during this
     * iteration if it in fact existed.
     * 
     * @param declaredId the ID {@code String} declared by the element of
     *        interest; should not be {@code null} (i.e. automatic IDs are not
     *        supported by this method)
     * @return the ID {@code String} appropriate for the copy of a nested
     *         element of the specified ID that is (or would be) generated
     *         during the current iteration
     */
    public String getNestedElementId(String declaredId) {
        return generateId(declaredId, this.currentIterationInfo.index);
    }

    /**
     * Implements {@code IdGenerator}. Augments the provided prefix to create
     * an id value that will be unique in this {@code PageContext} given that
     * {@code prefix} is either null or a unique id.
     * 
     * @param prefix a unique id for a nested control, or null indicating that
     *        an id should be automatically generated
     * @return the next ID provided by this ID generator
     */
    public String getNextId(String prefix) {
        assert getRealElement() == this;
        return generateId(
                ((prefix == null) ? ("autoId" + (this.autoId++)) : prefix),
                this.currentIterationInfo.index);
    }

    /**
     * Appends a {@code String} based on the provided {@code iterationIndex} to
     * the provided prefix to generate a unique id.
     * 
     * @param  givenName a unique (at least within the body of this
     *         {@code FieldIterator}) id
     * @param  forIteration the index of the body iteration for which an ID is
     *         requested
     * @return a derived id, that will not conflict with those generated during
     *         previous and subsequent iterations
     */
    protected String generateId(String givenName, int forIteration) {
        return givenName + "-nested_in_" + getId() + "-for_iteration"
                + forIteration;
    }

    /**
     * Determines the {@code prefix} sent to a previous call to
     * {@code generateId()}.
     * 
     * @param generatedId the value returned from {@code generateId()}
     * @return a {@code String} equal to the 'prefix' passed to
     *         {@code generateId()} that created this 'id'
     */
    protected String degenerateId(String generatedId) {
        return generatedId.substring(
                0, generatedId.indexOf("-nested_in_" + getId()));
    }

    /**
     * {@inheritDoc}
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     *
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return NO_ITERATIONS;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation delegates
     * to the superclass then updates any references to 'owned' controls or
     * referenced ancestor tags using the 'map' parameter that was populated by
     * the superclass' implementation as well as the caller, then makes a deep
     * copy of any complex modifiable member variables before returning the deep
     * copy.
     * 
     * @param newId {@inheritDoc}
     * @param map a {@code Map} pre-populated to contain mappings from all of
     *        the references to other tags that are maintained by this class to
     *        the corresponding tags that should be referenced by the copy
     *        generated by this method. This {@code Map} is passed to the
     *        superclass' implementation first and entries are added for all
     *        registered owned elements as well so that references maintained by
     *        this subclass may be updated.
     * @return {@inheritDoc}
     * @throws IllegalStateException if this method is called while this tag is
     *         in the process of iterating over its body (specifically after
     *         {@code doStartTag()} is called on the 'proxy' element and before
     *         {@code doEndTag()} is called for a given phase)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        if (isIterationInProgress()) {
            // this value is only true between doStartTag() and doEndTag() at
            // which point it would be inappropriate to generate a deep copy
            // (and impossible, given that this tag and its subclasses do NOT
            // invoke generateCopy() on themselves)
            throw new IllegalStateException();
        }
        
        HtmlPageIterator deepCopy
                = (HtmlPageIterator) super.generateCopy(newId, map);
    
        // deep copy IterationInfos
        if (this.iterationInfoList != null) {
            deepCopy.iterationInfoList = new ArrayList<IterationInfo>();
            for (IterationInfo iterInfo : this.iterationInfoList) {
                deepCopy.iterationInfoList.add(
                        iterInfo.generateCopy(map));
            }
        }
        
        return deepCopy;
    }

    /**
     * A nested class that contains data appropriate for a single iteration of
     * the body of this tag.
     */
    private static class IterationInfo implements Cloneable {

        /**
         * Set by the constructor, or by {@code createIterationInfoForIndex()},
         * this value indicates to which iteration of the body this
         * {@code IterationInfo} is associated. Iteration indexes start at zero
         * and increment by one.
         */
        private int index;

        /**
         * A {@code Collection} of real elements, initialially populated by
         * calls to {@code HtmlPageIterator.registerNestedElement()}. Order
         * must be maintained to ensure that those elements that are assigned
         * automatic id values will have the same id values as their 'real'
         * elements.
         */
        private Collection<HtmlPageElement> realElements;

        /** Used to enforce the rule that phases may not be skipped. */
        private int lastPhaseEvaluated;

        /**
         * A reference to the {@code HtmlPageIterator} to which this
         * {@code IterationInfo} applies. This is used in the regeneration of id
         * values for later iterations.
         */
        private HtmlPageIterator iteratorElement;

        /** The default constructor, ideal for use during the first phase */
        public IterationInfo() {
            this.index = 0;
            this.realElements = new ArrayList<HtmlPageElement>();
            this.lastPhaseEvaluated = HtmlPage.INVALID_PHASE;
            this.iteratorElement = null;
        }

        /**
         * Updates this {@code IterationInfo} with a new index, including
         * setting corresponding new IDs for all registered nested elements
         * 
         * @param  newIndex the new index for this {@code IterationInfo}
         */
        public void setNewIndex(int newIndex) {
            index = newIndex;

            for (HtmlPageElement nextElement : realElements) {
                String newId = iteratorElement.generateId(
                        iteratorElement.degenerateId(nextElement.getId()),
                        newIndex);
                
                nextElement.unregisterId();
                nextElement.setId(newId);
                nextElement.registerId(true);
            }
        }
        
        /**
         * <p>
         * Creates a deep copy of this {@code IterationInfo} and stores its
         * 'real' elements in the provided {@code PageContext}.
         * </p><p>
         * The returned {@code IterationInfo} object differs from this one in
         * the following ways:
         * <ul>
         * <li>The <i>index<i> field is set to the 'index' parameter.</li>
         * <li> Each nested control is a deep copy whose 'id' has been updated
         * and whose references to other tags have been updated in cases where
         * the original references were in the {@code HtmlPageIterator} whose
         * {@code IterationInfo} this is. </li>
         * </ul>
         * </p>
         * 
         * @param targetIndex the iteration index for the new IterationInfo
         * @return the new IterationInfo
         */
        public IterationInfo createIterationInfoForIndex(int targetIndex) {
            IterationInfo copy = new IterationInfo();
            
            copy.index = targetIndex;
            copy.iteratorElement = this.iteratorElement;
            copy.realElements = new ArrayList<HtmlPageElement>();
            
            /*
             * Create a map that will be passed to all of the nested elements'
             * generateCopy() method. This class stipulates that nested elements
             * will be listed in the order they appear on the page and
             * therefore, any tags that surround other tags will be copied first
             * and update the map to include a mapping from their original to
             * their deep copy so that later if any nested tags have a reference
             * to them, they will be able to update that reference to the
             * appropriate copy.
             */
            Map origToCopyMap = new StrictMapWrapper(new HashMap());

            for (HtmlPageElement nextElement : this.realElements) {
                String updatedId = iteratorElement.generateId(
                        iteratorElement.degenerateId(nextElement.getId()),
                        targetIndex);
                HtmlPageElement newElement
                        = HtmlPageElement.generateRealElement(
                                nextElement, updatedId, true, origToCopyMap);
                
                copy.realElements.add(newElement);
            }
            copy.lastPhaseEvaluated = this.lastPhaseEvaluated;
            
            return copy;
        }

        /**
         * Takes all the 'real' elements and makes them accessible as scripting
         * variables using their given id values (before they were altered to
         * guarantee uniqueness). For these attributes to be used as scripting
         * variables support must also be in place in the TLD file.
         * 
         * @param pageContext the current {@code PageContext} to which all
         *        scripting variables are attributes
         */
        public void enableScriptingVariablesForThisIteration(
                PageContext pageContext) {
            for (HtmlPageElement nextElement : this.realElements) {
                enableScriptingVariable(
                        nextElement,
                        iteratorElement.degenerateId(nextElement.getId()),
                        pageContext);
            }
        }

        /**
         * Take the given {@code HtmlPageElement} and makes it accessible as a
         * scripting variable (assuming that appropriate supporting code exists
         * in the TLD file).
         * 
         * @param realElement the 'real' element for an {@code HtmlPageElement}
         * @param variableName the name (which should correspond to the given
         *        id) by which this variable may be accessed.
         * @param pageContext the current {@code PageContext} to which all
         *        scripting variables are attributes
         */
        public static void enableScriptingVariable(HtmlPageElement realElement,
                String variableName, PageContext pageContext) {
            pageContext.setAttribute(variableName, realElement);
        }

        /**
         * Creates a deep copy of this {@code IterationInfo}, making sure to
         * update the id values of nested elements to correspond with the new
         * iterator name and storing them to the page context so they will be
         * available as 'real elements'.
         * 
         * @param origToCopyMap a map of original Tag objects as
         *        keys for their copies. This map must contain the
         *        {@code HtmlPageIterator} object to which this
         *        {@code IterationInfo} belongs so that the nested elements may
         *        update their parents. During the course of this method, each
         *        element that is copied will be added to the map in case
         *        another element is nested within it, so that its parent can be
         *        set to the new copy.
         * 
         * @return a deep copy of this {@code IterationInfo}
         */
        public IterationInfo generateCopy(Map<Tag, Tag> origToCopyMap) {
            IterationInfo deepCopy = null;
            
            try {
                deepCopy = (IterationInfo) clone();
            } catch (CloneNotSupportedException cnse) {
                throw new RuntimeException(cnse);
            }
            deepCopy.iteratorElement = (HtmlPageIterator) origToCopyMap.get(
                    this.iteratorElement);
            assert deepCopy.iteratorElement != null;
            if (this.realElements != null) {
                deepCopy.realElements = new ArrayList<HtmlPageElement>();
                
                for (HtmlPageElement nextElement : this.realElements) {
                    String newIdForNestedElement
                            = deepCopy.iteratorElement.generateId(
                                    this.iteratorElement.degenerateId(
                                            nextElement.getId()),
                                    this.index);
                    HtmlPageElement copy = HtmlPageElement.generateRealElement(
                            nextElement, newIdForNestedElement, true,
                            origToCopyMap);

                    // update the parent
                    Tag oldParent = nextElement.getParent();
                    if (oldParent instanceof HtmlPageElement) {
                        // special handling for HtmlPageElement because the
                        // origToCopyMap contains only references to 'real'
                        // elements and the current parent may be a reference
                        // to a 'proxy' element
                        oldParent = ((HtmlPageElement) oldParent).getRealElement();
                    }
                    Tag newParent = origToCopyMap.get(oldParent);
                    assert newParent != null;
                    copy.setParentWithoutResetting(newParent);

                    deepCopy.realElements.add(copy);
                }
            }
            return deepCopy;
        }
    }

}
