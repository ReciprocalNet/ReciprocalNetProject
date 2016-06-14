/*
 * Reciprocal Net project
 * 
 * SiteField.java
 * 
 * 03-Jun-2004: cwestnea wrote first draft modeled on UserField
 * 21-Jun-2004: cwestnea made miscellaneous changes to conform to conventions 
 *              for Fields
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 16-Nov-2004: midurbin removed addExtraHtmlAttribute()
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 14-Jun-2006: jobollin reformatted the source
 * 19-Mar-2009: ekoperda exposed additional SiteInfo fields publicSeqNum,
 *              privateSeqNum, finalSeqNum, and isActive
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LabelHtmlControl;
import org.recipnet.site.shared.db.SiteInfo;

/**
 * This Tag, coupled with an implementation of {@code SiteContext}, allows
 * fields from a {@code SiteInfo} object to be exposed on a JSP. These fields
 * are never editable.
 */
public class SiteField extends HtmlPageElement {

    /** Possible field codes, representing data within {@code SiteInfo} */
    public static final int ID = 1;
    public static final int NAME = 2;
    public static final int SHORT_NAME = 3;
    public static final int BASE_URL = 4;
    public static final int REPOSITORY_URL = 5;
    public static final int PUBLIC_SEQ_NUM = 6;
    public static final int PRIVATE_SEQ_NUM = 7;
    public static final int FINAL_SEQ_NUM = 8;
    public static final int IS_ACTIVE = 9;

    /**
     * Indicates which field of the {@code SiteInfo} object this control
     * represents. This variable is initialized to {@code NAME} by
     * {@code reset()} and may be set by its 'setter' method,
     * {@code setFieldCode()}. Valid values for this method are limited to
     * those static field codes defined by this class. This variable may not
     * change from phase to phase.
     */
    private int fieldCode;

    /**
     * This is a reference to the owned {@code HtmlControl}. Its type is always
     * {@code LabelHtmlControl} and it is cleared by {@code reset()}, and set
     * by {@code onRegistrationPhaseBeforeBody()}.
     */
    private HtmlControl control;

    /**
     * The {@code SiteContext} which this control sits in. Initialized in
     * reset() and discovered in {@code onRegistrationPhaseBeforeBody()}.
     */
    private SiteContext siteContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = SiteField.NAME;
        this.control = null;
        this.siteContext = null;
    }

    /**
     * Sets the fieldCode.
     * 
     * @param fieldCode one of the static field codes defined on
     *        {@code SiteField}, indicating which field within the
     *        {@code SiteInfo} object this tag will expose.
     * @throws IllegalArgumentException if an unknown fieldCode has been given.
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case SiteField.ID:
            case SiteField.NAME:
            case SiteField.SHORT_NAME:
            case SiteField.BASE_URL:
            case SiteField.REPOSITORY_URL:
	    case SiteField.PUBLIC_SEQ_NUM:
	    case SiteField.PRIVATE_SEQ_NUM:
	    case SiteField.FINAL_SEQ_NUM:
	    case SiteField.IS_ACTIVE:
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.fieldCode = fieldCode;
    }

    /**
     * @return one of the static field codes defined on {@code SiteField},
     *         indicating which field within the {@code SiteInfo} object this
     *         tag will expose.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * {@inheritDoc}; this version instantiates its 'owned' element, finds the
     * nearest {@code SiteContext} and delegates back to the superclass.
     * 
     * @throws IllegalStateException if this tag is not within a
     *         {@code SiteContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.siteContext = findRealAncestorWithClass(this, SiteContext.class);
        if (this.siteContext == null) {
            // This tag can't be used without SiteContext
            throw new IllegalStateException();
        }
        this.control = new LabelHtmlControl();
        super.registerOwnedElement(this.control);

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the {@code SiteInfo} from the most
     * immediate {@code SiteContext}, then uses the value of its field that
     * corresponds to {@code fieldCode} to update the 'owned' contol's value.
     * Finally the return value from the superclass' method (invoked at the
     * BEGINNING of this method) is returned.
     * 
     * @return the return value of the superclass' implementation of this
     *         method.
     * @throws IllegalStateException if the {@code fieldCode} is not one of
     *         those declared by this class or if this {@code SiteField} is
     *         {@code null}
     * @throws JspException if any other error is encountered while executing
     *         this method
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SiteInfo siteInfo = this.siteContext.getSiteInfo();

        if (siteInfo == null) {
            // SiteContext doesn't have a site for us
            throw new IllegalStateException();
        }

        switch (this.fieldCode) {
            case SiteField.ID:
                this.control.setValue(String.valueOf(siteInfo.id));
                break;
            case SiteField.NAME:
                this.control.setValue(siteInfo.name);
                break;
            case SiteField.SHORT_NAME:
                this.control.setValue(siteInfo.shortName);
                break;
            case SiteField.BASE_URL:
                this.control.setValue(siteInfo.baseUrl);
                break;
            case SiteField.REPOSITORY_URL:
                this.control.setValue(siteInfo.repositoryUrl);
                break;
	    case SiteField.PUBLIC_SEQ_NUM:
		this.control.setValue(Long.toString(siteInfo.publicSeqNum));
		break;
	    case SiteField.PRIVATE_SEQ_NUM:
		this.control.setValue(Long.toString(siteInfo.privateSeqNum));
		break;
	    case SiteField.FINAL_SEQ_NUM:
		this.control.setValue(Long.toString(siteInfo.finalSeqNum));
		break;
	    case SiteField.IS_ACTIVE:
		this.control.setValue(Boolean.toString(siteInfo.isActive));
		break;
            default:
                // unknown fieldCode
                throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SiteField dc = (SiteField) super.generateCopy(newId, map);

        dc.control = (HtmlControl) map.get(this.control);
        dc.siteContext = (SiteContext) map.get(this.siteContext);

        return dc;
    }
}
