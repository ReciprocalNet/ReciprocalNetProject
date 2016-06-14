/*
 * Reciprocal Net project
 * 
 * DailySampleSummaryField.java
 * 
 * 14-Jun-2005: midurbin wrote the first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A custom tag that is meant to be nested within a
 * {@code DailySampleSummaryIterator} to expose information about the action
 * type that is represented by a single iteration of that tag.
 */
public class DailySampleSummaryField extends HtmlPageElement {

    /** An enumeration of fieldCodes. */
    public static enum FieldCode {
        /**
         * Indicates that the number of times that the current action was
         * performed should be displayed.
         */
        COUNT,

        /** Indicates that the localized action name should be displayed. */
        ACTION,

        /**
         * Indicates that a lowercase "s" should be displayed if the count was
         * plural.
         */
        S_IF_PLURAL
    }

    /**
     * The {@code DailySampleSummaryIterator} in which this tag must be nested;
     * determined during the {@code REGISTRATION_PHASE}.
     */
    private DailySampleSummaryIterator summaryIt;

    /**
     * A required property that indicates what data is to be displayed by this
     * field.
     */
    private FieldCode fieldCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.summaryIt = null;
        this.fieldCode = null;
    }

    /**
     * Sets the 'fieldCode' property.
     * 
     * @param fc one of the enumerated field codes
     */
    public void setFieldCode(FieldCode fc) {
        this.fieldCode = fc;
    }

    /**
     * Getst the 'fieldCode' property.
     * 
     * @return one of the enumerated field codes.
     */
    public FieldCode getFieldCode() {
        return this.fieldCode;
    }

    /**
     * {@inheritDoc}; this version finds a reference to the innermost
     * surrounding {@code DailySampleSummaryIterator}
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code DailySampleSummaryIterator}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        this.summaryIt = findRealAncestorWithClass(this,
                DailySampleSummaryIterator.class);
        if (this.summaryIt == null) {
            throw new IllegalStateException();
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version gets and displays the information indicated
     * by the 'fieldCode' property.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        switch (this.fieldCode) {
            case COUNT:
                out.print(this.summaryIt.getActionCount());
                break;
            case ACTION:
                LanguageHelper lh = LanguageHelper.extract(
                        this.pageContext.getServletContext());
                
                try {
                    out.print(lh.getActionString(
                            this.summaryIt.getActionCode(),
                            this.pageContext.getRequest().getLocales(), true));
                } catch (ResourceNotFoundException ex) {
                    throw new JspException(ex);
                }
                
                break;
            case S_IF_PLURAL:
                out.print(this.summaryIt.getActionCount() > 1 ? "s" : "");
                break;

        }
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        DailySampleSummaryField dc
                = (DailySampleSummaryField) super.generateCopy(newId, map);
        
        dc.summaryIt = (DailySampleSummaryIterator) map.get(summaryIt);
        
        return dc;
    }
}
