/*
 * Reciprocal Net project
 * 
 * HtmlPage.java
 *
 * 09-Dec-2003: ekoperda wrote first draft
 * 19-Feb-2004: midurbin wrote second draft
 * 03-Jun-2004: cwestnea added setParent() and modified doStartTag() to fix
 *              bug #1223
 * 04-Jun-2004: cwestnea added suppressGeneratedHtml attribute
 * 16-Jun-2004: cwestnea added addToStyleBlock() and member variable
 *              styleBlock, and modified doBeforeBody() to output styleBlock
 * 16-Jun-2004: midurbin added abort() method and support for it throughout
 * 22-Jun-2004: midurbin added an implements statement for IdGenerator and
 *              altered the spec of getNextId()
 * 22-Jun-2004: cwestnea renamed isBrowserNetscape48() to be
 *              isBrowserNetscape4x()
 * 25-Jun-2004: cwestnea added getContextPath()
 * 05-Aug-2004: midurbin modified areAllFieldsValid(), reportValidationError()
 *              to enforce ValidationContext conventions
 * 20-Sep-2004: eisiorho fixed bug #1382, added getServletPathAndQuery()
 * 02-Nov-2004: midurbin fixed bug #1450 in doBeforeBody()
 * 16-Nov-2004: midurbin added an ExtraHtmlAttributeAccepter implementation
 * 17-Nov-2004: midurbin fixed bug #1469 in doBeforeBody()
 * 13-Jan-2005: jobollin removed code for generating opening and closing form
 *              tags around every page body, and added methods for defining
 *              extra form content and exposing it to enclosed tags
 * 21-Jan-2005: jobollin added method includeResource(String) to adapt
 *              RequestDispatcher.include() behavior to use with phased tags
 * 27-Jan-2005: midurbin added support to restart phase-based evaluation of
 *              this page with the methods reevaluatePage(), onReevaluation()
 * 20-Apr-2005: midurbin added the 'styleClass' property
 * 05-Jul-2005: midurbin replaced addFormContent() with addFormField(),
 *              removeFormField() and replaced getServletPathAndQuery() with
 *              getServletPathAndQueryForReinvocation()
 * 07-Jul-2005: midurbin delayed the setting of 'disallowFormFieldAddition'
 *              until all subclasses have completed doBeforeBody()
 * 27-Jul-2005: midurbin added doAfterPageBody(), renamed doBeforeBody() to
 *              doBeforePageBody() and added EvaluationAbortedExceptions to the
 *              list of checked exceptions for abort(), doBeforePageBody() and
 *              doAfterPageBody()
 * 04-Aug-2005: midurbin added support for preserving parameters that have
 *              multiple values to the formFields map and added
 *              addToHeadContent() to allow arbitrary HTML to be inserted
 *              between the HTML head tags
 * 11-Aug-2005: midurbin added ErrorSupplier implementation
 * 17-Jan-2006: jobollin added support for a "stylesheetUrl" attribute; removed
 *              unused imports; updated docs; added type arguments to the
 *              extraBodyAttributes map
 * 13-Mar-2006: jobollin made tests against HTTP method types be
 *              case-insensitive to protect against buggy clients and
 *              containers
 * 15-Jun-2006: jobollin updated docs
 * 27-Dec-2007: ekoperda added sendRedirect() methods
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * <p>
 * This is the driving tag for the 'controls' library and is responsible for
 * the phase scheme. This tag replaces the "&lt;html&gt;", "&lt;head&gt;", and
 * "&lt;body&lt;" HTML tag and all other HTML or custom tags should be nested
 * within it.
 * </p><p>
 * The body of this tag is evaluated once for each visited phase and during the
 * final phase, has its output returned with the HTTP response. Output during
 * other phases is disregarded. The method {@code getPhase} is provided to
 * allow nested tags to determine the phase of their evaluation that should be
 * performed at a given time. Other helper methods are provided to phase-
 * recognizing tags, such as automatic id generation for naming.
 * </p><p>
 * If {@code reevaluatePage()} is invoked by a subclass, the phase-based
 * evaluation will start again at the {@code REGISTRATION_PHASE}. All
 * processing for nested tags will be discarded and will begin again without
 * knowledge of any previous phases. This class and subclasses will retain the
 * values of state variables by default, but may reset them in them method
 * {@code onReevaluation()} which will be invoked before the phases resetart.
 * </p><p>
 * The {@code ValidationContext} is implemented by this class to allow any
 * particular nested tag a way of determining whether any other tag has been
 * assigned an invalid value.
 * </p><p>
 * Because {@code HtmlPage} is present on every page and every nested tag
 * acquires a reference to it, it is an ideal place for some common useful
 * functions. These functions offer information about this particular HTTP
 * request.
 * </p><p>
 * This tag implements {@code ExtraHtmlAttributeAccepter} which allows any
 * number of attributes to be added to the HTML &lt;body&gt; tag. These
 * attributes are not added to the other HTML tags outputted by this tag (html,
 * head, etc). For example, this interface can allow an 'onLoad' attribute to
 * the &lt;body&gt; tag to allow the execution of a javascript function when
 * the page is loaded.
 * </p><p>
 * This tag provides a mechanism for subclasses to indicate parameters that are
 * required for a valid request and should be preserved as hidden form fields 
 * in self-posing forms or as query line parameters in link-back URLs. 
 * Subclasses should invoke
 * {@link HtmlPage#addFormField(String, String) addFormField()}
 * for each required parameter. The self-posting form tag should then include
 * the result of {@link HtmlPage#getFormContent() getFormContent()} in the body
 * of the HTML &lt;form&gt; tags. Furthermore the new
 * {@link HtmlPage#getServletPathAndQueryForReinvocation()
 * getServletPathAndQueryForReinvocation()} method will now return a URL
 * relative to the context path that is sufficient to reinvoke the current
 * page, even if the current request had no query parameters (as in an HTTP
 * POST operation). Subclasses or nested tags need not invoke
 * {@code addFormField()} for parameters that are known to be on the query line
 * as they will be automatically added by this class, though if they wish to
 * explicitly prevent them from being included in self-posting forms or
 * reinvocation URL's the method {@link HtmlPage#removeFormField(String)
 * removeFormField()} should be called.
 * </p>
 */
public class HtmlPage extends BodyTagSupport implements ErrorSupplier,
        ExtraHtmlAttributeAccepter, IdGenerator, ValidationContext {

    /**
     * The initial size of the buffer used by {@link #includeResource(String)}
     * to capture the result of a resource evaluation. This buffer is allocated
     * (and discarded) per-inclusion, and it grows as necessary to accommodate
     * the data, so this value is a tuning parameter.
     */
    private final static int INITIAL_INCLUDE_BUFFER_SIZE = 2048;

    public static final int INVALID_PHASE = 0;

    public static final int REGISTRATION_PHASE = 5;

    public static final int PARSING_PHASE = 15;

    public static final int FETCHING_PHASE = 25;

    public static final int PROCESSING_PHASE = 35;

    public static final int RENDERING_PHASE = 45;

    public static final int DONE = 40;

    /**
     * An ErrorSupplier error flag that may be set if an attempt to perform a
     * function fails because a nested tag reported a validation error.
     */
    public static final int NESTED_TAG_REPORTED_VALIDATION_ERROR = 1 << 0;

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return NESTED_TAG_REPORTED_VALIDATION_ERROR;
    }

    /**
     * Tracks which "phase" of this tag's body is currently being evaluated.
     * One of: {@code REGISTRATION_PHASE}, {@code PARSING_PHASE},
     * {@code FETCHING_PHASE}, {@code PROCESSING_PHASE} or
     * {@code RENDERING_PHASE}. Initialized by {@code reset()} and altered by
     * {@code doEndTag()}.
     */
    private int phase;

    /**
     * Tracks whether a validation error has been reported by a nested tag.
     * Cleared by {@code reset()} and set by {@code reportValidationError()}.
     */
    private boolean validationErrorReported;

    /**
     * {@code nextId} stores the number of automatically requested ids that
     * have been returned so far by calls to {@code getNextId()} and is used in
     * the generation of sequential ids. Initialized by {@code reset()} and
     * altered by {@code getNextId()}.
     */
    private int nextId;

    /**
     * A mapping of attribute name {@code String}s to attribute value
     * {@code String}s representing extra attributes that should be included in
     * the HTML body tag. Attribute/value pairs are all cleared by
     * {@code reset()} and are added by calls to
     * {@code addExtraHtmlAttribute()}.
     */
    private Map<String, String> extraBodyAttributes;

    /**
     * The URL of a stylesheet to which a link should be placed in the page
     * header.
     */
    private String stylesheetUrl;

    /**
     * {@code styleBlock} represents the text that will be put into the style
     * block in the &lt;head&gt; tag of this HTML page. The value is
     * initialized by {@code reset()} and added to by calls to
     * {@code addToStyleBlock()}
     */
    private StringBuffer styleBlock;

    /**
     * HTML that will be inserted between the &lt;head&gt; tags output by this
     * custom tag. This should not include title or style tags because they may
     * be added through different mechanisms. This value is initialized by
     * {@code reset()} and added to by calls to {@code addToHeadContent()}.
     */
    private StringBuffer headContent;

    /**
     * Represents the title for this HTML page. The {@code title} will be
     * included in the HTML &lt;head&gt;. This value is initialized by
     * {@code reset()} and modified by attribute 'setter' and 'getter'
     * functions. It is exposed as an optional attribute in the TLD file.
     */
    private String title;

    /**
     * When set to "true" this page tag will not output headers and footers.
     * The value is initialized in {@code reset()} and modified by the set and
     * get functions. It is an optional attribute in the TLD file.
     */
    private boolean suppressGeneratedHtml;

    /**
     * An optional property that when set is used as the 'class' attribute to
     * the HTML &lt;body&gt; tag. This style class must be declared either in
     * an included style sheet or in a {@code HtmlPageStyleBlock}.
     */
    private String styleClass;

    /**
     * When set to "true", no more calls to {@code doBeforeBody()} or
     * {@code doAfterBody()} will be made. This variable is set by the method
     * {@code abort()} and should be called whenever evaluation should be
     * halted before normal completion of all the phases. This should be used
     * when {@code HtmlPage} or a subclass redirects, forwards or returns an
     * {@code HttpServletResponse} error. The only other ways to guarantee that
     * phase-based evaluation halts is when another tag returns
     * {@code SKIP_PAGE} from its implementation of {@code doEndTag()} (as in
     * the case with redirect tags).
     */
    private boolean abortEvaluation;

    /**
     * All of the parameter name/value(s) pairs needed for a valid request of
     * this page. These values are used to generate the response from
     * {@code getServletPathAndQueryForReinvocation()} and
     * {@code getFormContent()}. This collection is populated during "GET"
     * requests to contain all query line parameters. Subclasses of
     * {@code HtmlPage} that require and parse POST parameters must add them to
     * this collection via a call to {@code addFormField()} and subclasses that
     * have fully used a query parameter and do not require it to be posted
     * again may remove it by calling {@code removeFormField()}.
     */
    private Map<String, String[]> formFields;

    /**
     * A private helper variable that indicates when form fields may no longer
     * be added. This variable is false until right before the body is
     * evaluated for the {@code RENDERING_PHASE}.
     */
    private boolean disallowFormFieldAddition;

    /**
     * An optional property that when set indicates the servlet path that will
     * be used for reinvocation and returned by
     * {@code getServletPathAndQueryForReinvocation()}.
     */
    private String overrideReinvocationServletPath;

    /**
     * An internal helper variable that is set by {@code reevaluatePage()} to
     * indicate that at the completion of this phase, phases processing should
     * start again at the {@code REGISTRATION_PHASE}.
     */
    private boolean triggerReevaluation;

    /**
     * An internal helper variable that is set by {@code doAfterBody()} in
     * response to {@code triggerReevaluation} having been set to true. This
     * value is returned by {@code wasRestarted()} and can be used to determine
     * whether an existing attribute to the {@code PageContext} may be left
     * over from a previous {@code REGISTRATION_PHASE}.
     */
    private boolean phasesWereRestarted;
    
    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /**
     * This method initializes all member variables and should be called at the
     * beginning of {@code setParent()}. Subclasses may override this method,
     * but MUST delegate back to their superclass.
     */
    protected void reset() {
        this.phase = HtmlPage.REGISTRATION_PHASE;
        this.validationErrorReported = false;
        this.nextId = 0;
        this.extraBodyAttributes = null;
        this.title = null;
        this.styleClass = null;
        this.styleBlock = null;
        this.stylesheetUrl = null;
        this.headContent = null;
        this.abortEvaluation = false;
        this.formFields = new HashMap<String, String[]>();
        this.disallowFormFieldAddition = false;
        this.triggerReevaluation = false;
        this.phasesWereRestarted = false;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * Sets the 'overrideReinvocationServletPath' property.
     * 
     * @param path the servlet path, or path relative to the context path that
     *        should be used in reinvocations of this page
     */
    public void setOverrideReinvocationServletPath(String path) {
        this.overrideReinvocationServletPath = path;
    }

    /**
     * Gets the 'overrideReinvocationServletPath' property.
     * 
     * @return the servlet path, or path relative to the context path that
     *         should be used in reinvocations of this page
     */
    public String getOverrideReinvocationServletPath() {
        return this.overrideReinvocationServletPath;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        errorCode |= errorFlag;
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container just
     * before the attributes are set for this custom tag. Because there is no
     * guarantee that the constructor will be called for each tag instance, in
     * the current implementation, a call to {@code reset()} is made here to
     * initialize member variables. Any subclasses may override this method,
     * but MUST delegate back to their superclass.
     * 
     * @param t the parent {@code Tag}
     */
    @Override
    public void setParent(Tag t) {
        super.setParent(t);
        reset();
    }

    /**
     * Simply gets the current phase of evaluation.
     * 
     * @return a phase code
     */
    public int getPhase() {
        return this.phase;
    }

    /**
     * Implements {@code IdGenerator}. This function supplies the next
     * sequantial id value. This can be used to choose unique id's for nested
     * elements, as the sequence is restarted by the call to {@code reset()} at
     * the beginning of each phase. To preseve the id values that may be used
     * to identify scripting variables, this method simply returns the prefix
     * when it is not-null.
     * 
     * @param prefix a {@code String} that, when non-null is returned. This
     *        implementation does not require modification of the prefix.
     * @return a {@code String} that is unique and consistent over each phase
     *         evaluation of the body.
     */
    public String getNextId(String prefix) {
        if (prefix != null) {
            return prefix;
        }
        return "sequentialId" + (this.nextId++);
    }

    /**
     * Implements {@code ValidationContext}.
     * 
     * @throws IllegalStateException if called after the
     * {@code FETCHING_PHASE}.
     */
    public void reportValidationError() {
        if ((this.phase == PROCESSING_PHASE) 
                || (this.phase == RENDERING_PHASE)) {
            throw new IllegalStateException();
        }
        ValidationContext parentValidationContext
                = (ValidationContext) TagSupport.findAncestorWithClass(
                        this, ValidationContext.class);
        if (parentValidationContext != null) {
            parentValidationContext.reportValidationError();
        }
        this.validationErrorReported = true;
        setErrorFlag(NESTED_TAG_REPORTED_VALIDATION_ERROR);
    }

    /**
     * Implements {@code ValidationContext}.
     * 
     * @throws IllegalStateException if called before the
     * {@code PARSING_PHASE}.
     */
    public boolean areAllFieldsValid() {
        if ((this.phase != PROCESSING_PHASE) 
                && (this.phase != RENDERING_PHASE)) {
            throw new IllegalStateException();
        }
        return !validationErrorReported;
    }

    /** @param title The title of this {@code HtmlPage}. */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return The title of this {@code HtmlPage} */
    public String getTitle() {
        return this.title;
    }

    /**
     * @param styleClass the name of a css class representing the desired style
     *        for the body element
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * @return the name of a css class representing the desired style for the
     *         body element
     */
    public String getStyleClass() {
        return this.styleClass;
    }

    /**
     * Gets the stylesheet URL configured for this {@code HtmlPage}
     * 
     * @return the stylesheet URL as a {@code String}; {@code null} if no
     *         stylesheet URL has been configured
     */
    public String getStylesheetUrl() {
        return stylesheetUrl;
    }

    /**
     * Sets the stylesheet URL for this {@code HtmlPage}
     * 
     * @param stylesheetUrl the {@code String} to use as a stylesheet URL; may
     *        be {@code null} to explicitly specify no stylesheet URL, or to
     *        remove a previously set stylesheet URL
     */
    public void setStylesheetUrl(String stylesheetUrl) {
        this.stylesheetUrl = stylesheetUrl;
    }

    /**
     * @param suppressGeneratedHtml whether or not this tag should output html
     */
    public void setSuppressGeneratedHtml(boolean suppressGeneratedHtml) {
        this.suppressGeneratedHtml = suppressGeneratedHtml;
    }

    /**
     * @return whether or not this tag should output html
     */
    public boolean getSuppressGeneratedHtml() {
        return this.suppressGeneratedHtml;
    }

    /**
     * Allows callers to know whether the current phase is occurring as the
     * result of a call to {@code reevaluatePage()}. This is useful for
     * {@code HtmlPageElement} when creating 'real' elements because if the
     * {@code REGISTRATION_PHASE} occurs twice for the same {@code PageContext}
     * there will already be attributes containing the 'real' elements from the
     * first evaluation.
     */
    public boolean wasRestarted() {
        return this.phasesWereRestarted;
    }

    /**
     * This method may be called by subclasses to restart phase evaluation for
     * this page. Each nested tag will not retain any state from any phases
     * that have been completed. After this method is inovked, when the current
     * phase ends, the method {@code onReevaluation()} will be invoked and a
     * new {@code REGISTRATION_PHASE} will begin. The reevalution of the page
     * is analogous to that that would result from an HTTP GET operation.
     * Therefore, nested fields will not be populated with the values POSTed
     * unless those values are stored (possibly in a 
     * {@code PersistedOperation}) and provided back to nested controls during
     * the {@code FETCHING_PHASE} (for they will experience no
     * {@code PARSING_PHASE}).
     * 
     * @throws IllegalStateException if called during the
     *         {@code RENDERING_PHASE} because the response has already been
     *         committed.
     */
    protected void reevaluatePage() {
        if (this.phase == RENDERING_PHASE) {
            throw new IllegalStateException();
        }
        this.triggerReevaluation = true;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * This method is invoked before the {@code REGISTRATION_PHASE} that occurs
     * as the result of a call to {@code reevaluatePage()} and is used to reset
     * some member variables. Subclasses should override this method to reset
     * those variables that should be reset in the event of a reevaluation and
     * must delegate back to the superclass.
     * <p>
     * Variables that should be reset include:
     * <ul>
     * <li>Member variables that may be added to in a cumulative fashion by
     * calls from one or more nested tags. (ie, extraBodyAttributes) </li>
     * <li>Member variables that reflect the status of nested tags. (ie,
     * 'validationErrorReported' or an errory supplier error code) </li>
     * <li>Member variables that were set by nested tags to trigger some sort
     * of processing. (ie, triggerReevaluation) </li>
     * <p>
     * Note: reset is NOT invoked before a reevaluation, so subclasses should
     * recognize that any member variables NOT reset by this method will retain
     * their values from the completion of the phase in which
     * {@code reevaluatePage()} was called.
     */
    protected void onReevaluation() {
        this.triggerReevaluation = false;
        this.validationErrorReported = false;
        this.extraBodyAttributes = null;
        this.styleBlock = null;
        this.formFields = new HashMap<String, String[]>();
        this.disallowFormFieldAddition = false;
    }

    /**
     * This method may be called to prevent further evaluation of this tag or
     * its body. This method is for use by subclasses, not nested elements.
     * Nested elements may return {@code SKIP_PAGE} from {@code doEndTag()} to
     * have a similar effect. This method should be overridden by classes that
     * need to do any cleanup in the event of evaluation abortion. Subclasses
     * should always delegate back to this method after their own
     * implementation.
     * 
     * @throws EvaluationAbortedException is always thrown at the end of the
     *         base class' implementation.
     * @throws JspException wrapping any exception thrown during clean-up
     */
    protected void abort() throws JspException, EvaluationAbortedException {
        this.abortEvaluation = true;
        throw new EvaluationAbortedException();
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container as it
     * begins to evaluate this tag. The current implementation sets the current
     * {@code phase} to {@code REGISTRATION_PHASE} and establishes the
     * scripting variable 'htmlPage'.
     * 
     * @throws JspException if an error is encountered by this method.
     * @return {@code EVAL_BODY_BUFFERED}
     */
    @Override
    public int doStartTag() throws JspException {
        this.phase = REGISTRATION_PHASE;
        // allow this Page to be accessible as scripting varable
        // Ideally, this variable would be called 'page' but the servlet
        // container arleady defines a variable with the name 'page'.
        super.pageContext.setAttribute("htmlPage", this);
        try {
            doBeforePageBody();
        } catch (EvaluationAbortedException ex) {
            return SKIP_BODY;
        }
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container after
     * each evaluation of the body. The current implementation advances the
     * {@code phase} and calls {@code doAfterPageBody()} and
     * {@code doBeforePageBody()}. Classes may not override this method but
     * instead should override {@code doAfterPageBody()} and then delegate back
     * to the superclass' implementation.
     * 
     * @return {@code EVAL_BODY_BUFFERED} for each phase up to the Rendering
     *         Phase at which point {@code SKIP_BODY} is returned.
     * @throws JspException if an IOException is encountered while writing to
     *         the output stream, if an invalid HTTP method was used in this
     *         request, or if an unsupported phase has been reached
     */
    @Override
    public final int doAfterBody() throws JspException {
        try {
            doAfterPageBody();
        } catch (EvaluationAbortedException ex) {
            return SKIP_BODY;
        }

        String httpMethod
                = ((HttpServletRequest) pageContext.getRequest()).getMethod();
        boolean wasPost = httpMethod.equalsIgnoreCase("POST");
        if (!wasPost && !httpMethod.equalsIgnoreCase("GET")) {
            throw new JspException("HTTP method " + httpMethod
                    + " not supported by Page control");
        }
        if (this.phasesWereRestarted) {
            // this is a reevaluation and must be analogous to an HTTP "GET"
            wasPost = false;
        }

        // Decide what the next phase will be.
        int nextPhase;
        switch (this.phase) {
            case REGISTRATION_PHASE:
                nextPhase = wasPost ? PARSING_PHASE : FETCHING_PHASE;
                break;
            case PARSING_PHASE:
                nextPhase = FETCHING_PHASE;
                break;
            case FETCHING_PHASE:
                nextPhase = wasPost ? PROCESSING_PHASE : RENDERING_PHASE;
                break;
            case PROCESSING_PHASE:
                nextPhase = RENDERING_PHASE;
                break;
            case RENDERING_PHASE:
                nextPhase = DONE;
                break;
            default:
                throw new JspException("Unsupported phase reached");
        }

        if (this.triggerReevaluation) {
            // after this phase, start over with the REGISTRATION_PHASE
            nextPhase = REGISTRATION_PHASE;
            this.phasesWereRestarted = true;
            onReevaluation();
        }

        this.phase = nextPhase;
        if (this.phase != DONE) {
            try {
                doBeforePageBody();
            } catch (EvaluationAbortedException ex) {
                return SKIP_BODY;
            }
            if (this.phase == RENDERING_PHASE) {
                this.disallowFormFieldAddition = true;
            }
        }
        return ((nextPhase != DONE) ? EVAL_BODY_AGAIN : SKIP_BODY);
    }

    /**
     * Overrides {@code BodyTagSupport}; called by the servlet container. The
     * current implementation writes the body to the http respose, completing
     * evaluation of this Tag.
     * 
     * @return EVAL_PAGE
     * @throws JspException if an {@code IOException} occurs
     */
    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute("htmlPage");
        if (this.abortEvaluation) {
            return SKIP_PAGE;
        }
        try {
            // Echo the output rendered from our body during the last phase
            // (the RENDERING_PHASE) to the "real" output.
            super.getBodyContent().writeOut(super.pageContext.getOut());
        } catch (IOException ex) {
            throw new JspException(ex);
        }
        return EVAL_PAGE;
    }

    /**
     * Akin to {@code doAfterBody()}, but this method does not override
     * {@code BodyTagSupport} and is not invoked directly by the servlet
     * container. Instead calls are made to this method by this class during
     * {@code doStartTag()} (before the {@code REGISTRATION_PHASE} and after
     * the phase is advanced in {@code doAfterBody()}. Subclasses may override
     * this method when they need to have code evaluated before their body
     * during a given {@code phase}. When doing so, they must delegate to their
     * superclass before returning. In the current implementation HTML is
     * outputted including a doctype header, the HTML &lt;head&gt; tag with a
     * title (configured by {@code setTitle()}), and a &lt;body&gt; tag
     * (configured by {@code addExtraHtmlAttribute()} during the
     * {@code RENDERING_PHASE}.
     * 
     * @throws JspException if an IOException is encountered while writing to
     *         the output stream.
     * @throws EvaluationAbortedException if the HttpServletResponse is
     *         finalized and evaluation of this page should be immediately
     *         halted.
     */
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        this.nextId = 0;
        if (this.phase == HtmlPage.REGISTRATION_PHASE) {
            // Prepopulate the formfields with the query parameters
            // Note: only the first value for each parameter name is included
            HttpServletRequest req
                    = (HttpServletRequest) super.pageContext.getRequest();
            if (req.getMethod().equalsIgnoreCase("GET")) {
                Enumeration parameters = req.getParameterNames();
                while (parameters.hasMoreElements()) {
                    String nextParameter = (String) parameters.nextElement();
                    assert (nextParameter != null);
                    addFormField(nextParameter,
                            req.getParameterValues(nextParameter));
                }
            }
        } else if (this.phase == HtmlPage.RENDERING_PHASE) {
            // The next phase is RENDERING_PHASE; we should clear the body now
            // so that the next pass can start rendering the body again from
            // scratch.
            super.getBodyContent().clearBody();
            if (!suppressGeneratedHtml) {
                JspWriter out = pageContext.getOut();

                try {
                    out.println("<!DOCTYPE html PUBLIC"
                            + " \"-//W3C//DTD XHTML 1.0 Strict//EN\""
                            + " \"http://www.w3.org/TR/xhtml1/DTD/"
                            + "xhtml1-strict.dtd\">");
                    out.println("<html xmlns=\"http://"
                            + "www.w3.org/1999/xhtml\" xml:lang=\"en\""
                            + " lang=\"en\">");
                    out.println("<head>");
                    out.println("  <title>" + (this.title == null ? "" : title)
                            + "</title>");
                    if (this.stylesheetUrl != null) {
                        out.println("<link rel=\"stylesheet\" href=\""
                                + stylesheetUrl + "\" type=\"text/css\"/>");
                    }
                    if (this.styleBlock != null) {
                        out.println("<style type=\"text/css\">");
                        out.println(this.styleBlock.toString());
                        out.println("</style>");
                    }
                    if (this.headContent != null) {
                        out.println(headContent.toString());
                    }
                    out.println("</head>");
                    out.println("<body"
                            + getExtraBodyAttributesAsString()
                            + (this.styleClass != null ? " class=\""
                                    + this.styleClass + "\"" : "") + " >");
                } catch (IOException ex) {
                    throw new JspException(ex);
                }
            }
        }
    }

    /**
     * Invoked when {@code doAfterBody()} is invoked. This method should be
     * overridden by subclasses instead of {@code doAfterBody()}. This method
     * differs in that it is invoked by {@code HtmlPage} and allows
     * {@code EvaluationAbortedException}s to be thrown that are caught by
     * {@code HtmlPage} and result in a smooth termination of page evalulation.
     * <p>
     * This base class implementation outputs HTML to close the &lt;body&gt;
     * and &lt;html&gt; tags during the {@code RENDERING_PHASE}
     * 
     * @throws JspException if an IOException is encountered while writing to
     *         the output stream.
     * @throws EvaluationAbortedException if the HttpServletResponse is
     *         finalized and evaluation of this page should be immediately
     *         halted.
     */
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        if ((this.phase == RENDERING_PHASE) && !suppressGeneratedHtml) {
            try {
                super.pageContext.getOut().println("</body>");
                super.pageContext.getOut().println("</html>");
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
    }

    /**
     * This method instructs the {@code HtmlPage} to add the given text to the
     * style block in this HTML page's &lt;head&gt; tag.
     * 
     * @param block the block of style code to add the the &lt;head&gt;
     */
    public void addToStyleBlock(String block) {
        if (this.styleBlock == null) {
            this.styleBlock = new StringBuffer();
        }
        this.styleBlock.append(block);
    }

    /**
     * This method instructs the {@code HtmlPage} to add the given HTML within
     * the HTML page's &lt;head&gt; tag.
     * 
     * @param html the HTML to add to the &lt;head&gt;
     */
    public void addToHeadContent(String html) {
        if (this.headContent == null) {
            this.headContent = new StringBuffer();
        }
        this.headContent.append(html);
    }

    /**
     * Implements {@code ExtraHtmlAttributeAccepter}. This method instructs the
     * {@code HtmlPage} to include an extra attribute in the body tag with the
     * indicated name and value.
     * 
     * @param name the name of the attribute
     * @param value the value of the attribute
     */
    public void addExtraHtmlAttribute(String name, String value) {
        if (this.extraBodyAttributes == null) {
            this.extraBodyAttributes = new HashMap<String, String>();
        }
        this.extraBodyAttributes.put(name, value);
    }

    /**
     * Converts the {@code extraBodyAttributes} {@code Map} into a
     * {@code String} of the format: name1="value1" name2="value2" for
     * insertion as attributes into the HTML &lt;body&gt; tag.
     * 
     * @return a formatted {@code String} of attribute/value pairs
     */
    protected String getExtraBodyAttributesAsString() {
        if (this.extraBodyAttributes == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
        
            for (Entry<String, String> entry 
                    : extraBodyAttributes.entrySet()) {
                sb.append(' ').append(entry.getKey()).append("=\"");
                sb.append(entry.getValue()).append("\"");
            }
            
            return sb.toString();
        }
    }

    /**
     * Provides the URL that was requested by the client
     * 
     * @return a {@code String} representation of the requested URL
     */
    public String getRequestedUrl() {
        HttpServletRequest req
                = (HttpServletRequest) super.pageContext.getRequest();
        
        return req.getRequestURL()
                + ((req.getQueryString() == null) ? "" : "?"
                        + req.getQueryString());
    }

    /**
     * Provides the context path of the current request
     * 
     * @return a {@code String} representation of the context path
     */
    public String getContextPath() {
        HttpServletRequest req
                = (HttpServletRequest) super.pageContext.getRequest();
        
        return req.getContextPath();
    }

    /**
     * Provides the reinvocation servlet path and query. The servlet path is
     * either the servlet path for the current request or the value of the
     * 'overrideReinvocationServletPath' property if it is set. The query
     * contains all of the parameters included on the current requests query
     * (when it was a GET) minus any removed by calls to
     * {@code removeFormField()} plus any added by calls to
     * {@code addFormField()}. The value returned is perfectly suited for tags
     * that require URL's relative to the context path that refer to this page.
     * 
     * @return a {@code String} representation of the reinvocation path and
     *         query
     */
    public String getServletPathAndQueryForReinvocation() {
        String servletPath = ((this.overrideReinvocationServletPath != null)
                ? this.overrideReinvocationServletPath
                : ((HttpServletRequest) pageContext.getRequest()
                        ).getServletPath());
        StringBuffer query = new StringBuffer();
        
        for (String name : this.formFields.keySet()) {
            for (String value : this.formFields.get(name)) {
                try {
                    query.append((query.length() == 0 ? "?" : "&")
                            + URLEncoder.encode(name, "UTF-8") + "="
                            + URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    // can't happen because UTF-8 is supported
                }
            }
        }
        return servletPath + query;
    }

    /**
     * Helpful function that returns a boolean indicating whether this request
     * was made using the Netscape 4.x browser.
     * 
     * @return true, if Netscape 4.x was used for the request, otherwise false.
     */
    public boolean isBrowserNetscape4x() {
        String browser
                = ((HttpServletRequest) pageContext.getRequest()).getHeader(
                        "USER-AGENT");
        // it should say 4 but not compatible, we want the real deal
        return (browser.indexOf("Mozilla/4") != -1)
                && (browser.indexOf("compatible") == -1);
    }

    /**
     * A method to add a parameter name and value that is required to mimic the
     * current request. These name/value pairs will be included in any page
     * form that posts to this page and will be preserved through multiple POST
     * rountrips so that certain links may successfully link back to this page.
     * If multiple fields are added with the same name, only the last one is
     * preserved.
     * 
     * @param paramName an unescaped/unencoded parameter name
     * @param paramValue an unescaped/unencoded parameter value
     * @throws IllegalStateException if called after {@code doBeforeBody()} has
     *         been completed for the {@code RENDERING_PHASE}
     */
    protected void addFormField(String paramName, String paramValue) {
        if (this.disallowFormFieldAddition) {
            throw new IllegalStateException();
        }
        assert (paramName != null);
        assert (paramValue != null) : "null value for " + paramName;
        this.formFields.put(paramName, new String[] { paramValue });
    }

    /**
     * A method to add a parameter name and values that is required to mimic
     * the current request. These name/value(s) pairs will be included in any
     * page form that posts to this page and will be preserved through multiple
     * POST rountrips so that certain links may successfully link back to this
     * page.  If multiple fields are added with the same name, only the last
     * one is preserved.
     * 
     * @param paramName an unescaped/unencoded parameter name
     * @param paramValues the unescaped/unencoded parameter values
     * @throws IllegalStateException if called after {@code doBeforeBody()} has
     *         been completed for the {@code RENDERING_PHASE}
     */
    protected void addFormField(String paramName, String[] paramValues) {
        if (this.disallowFormFieldAddition) {
            throw new IllegalStateException();
        }
        assert (paramName != null);
        assert (paramValues != null) : "null value for " + paramName;
        assert (!Arrays.asList(paramValues).contains(null))
                : "null value among those for " + paramName;
        this.formFields.put(paramName, paramValues.clone());
    }

    /**
     * A method to remove a parameter name and value that is not required but
     * may have been included on the current requst, from being included in
     * self-posting forms or link-back URLs. An example of values that should
     * be removed are those that affect preferences and are no longer useful
     * after their initial parsing.
     * 
     * @param paramName a parmeter that may have been included as a query
     *        parameter and automatically added to the 'formFields' map that
     *        does not need to be persisted
     */
    public void removeFormField(String paramName) {
        this.formFields.remove(paramName);
    }

    /**
     * Gets an HTML String containing hidden form fields representing each
     * name/value pair supplied to {@code addFormField()}, or an empty
     * {@code String} if none were added.  The resulting string is HTML code,
     * and is intended to be handled as such; normally it should not be
     * (further) escaped. 
     * 
     * @return a series of zero or more HTML hidden form fields
     */
    public String getFormContent() {
        StringBuffer formContent = new StringBuffer();
        for (String name : this.formFields.keySet()) {
            for (String value : this.formFields.get(name)) {
                formContent.append("<input type=\"hidden\" name=\""
                        + HtmlControl.escapeAttributeValue(name)
                        + "\" value=\""
                        + HtmlControl.escapeAttributeValue(value) + "\" />");
            }
        }
        return formContent.toString();
    }

    /**
     * Uses a RequestDispatcher to include the specified resource, capturing
     * the resource's output, then copies the output to this tag handler's
     * current JspWriter (obtained from its PageContext). This procedure, or
     * something similar, is required for RequestDispatchers to interact
     * correctly with the phased tags of this tag library.
     * 
     * @param resourceName the name of the resource to include, in the form
     *        required for constructing a RequestDispatcher
     * @throws JspException wrapping any ServletException or IOException thrown
     *         during evaluation of the specified resource or printing it to
     *         the JspWriter
     */
    public void includeResource(String resourceName) throws JspException {
        final StringWriter sw = new StringWriter(INITIAL_INCLUDE_BUFFER_SIZE);
        ServletRequest request = super.pageContext.getRequest();
        HttpServletResponse response
                = (HttpServletResponse) super.pageContext.getResponse();
        RequestDispatcher dispatcher
                = request.getRequestDispatcher(resourceName);

        try {
            dispatcher.include(request,
                    new HttpServletResponseWrapper(response) {
                        @Override
                        public PrintWriter getWriter() {
                            return new PrintWriter(sw);
                        }
                    });
            super.pageContext.getOut().print(sw);
        } catch (ServletException se) {
            throw new JspException(se);
        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
    }

    /**
     * Utility method that issues an HTTP redirect command to the browser.
     * Most callers will need to invoke <code>abort</code> after invoking this
     * method to achieve proper operation.
     * @param href a URL fragment to which the browser will be directed.  This
     *     path is relative to the webapp's context path, as returned by
     *     <code>getContextPath()</code>.
     * @param params a string of parameters that is appended to the uri that is
     *     passed to the browser.  This may be null, in which case no
     *     parameters are passed.
     */
    protected void sendRedirect(String href, String params)
           throws IOException {
	StringBuilder uri = new StringBuilder();
	uri.append(this.getContextPath());
	uri.append(href);
	if (params != null) {
	    uri.append("?");
	    uri.append(params);
	}
        ((HttpServletResponse) pageContext.getResponse()).sendRedirect(
                uri.toString());
    }

    /**
     * A second version of a utility method that issues an HTTP redirect
     * command to the browser.  Most callers will need to invoke
     * <code>abort()</code> after invoking this method to achieve proper
     * operation.
     * @param href a URL fragment to which the browser will be directed.  This
     *     path is relative to the webapp's context path, as returned by
     *     <code>getContextPath()</code>.
     * @param params an array of objects that are appended to the uri that is
     *     passed to the browser.  The array is presumed to contain an even
     *     number of elements, in key=value order.  The <code>toString()</code>
     *     method is invoked on each <code>Object</code> in the array.  The
     *     array reference may be null, in which case no parameters are passed
     *     to the browser.
     */
    protected void sendRedirect(String href, Object params[]) 
            throws IOException {
	if (params == null) {
	    this.sendRedirect(href, (String) null);
	}
	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < params.length; i += 2) {
	    if (i > 0) {
		sb.append("&");
	    }
	    sb.append(params[i].toString());
	    sb.append("=");
	    sb.append(params[i + 1].toString());
	}
	this.sendRedirect(href, sb.toString());
    }
}
