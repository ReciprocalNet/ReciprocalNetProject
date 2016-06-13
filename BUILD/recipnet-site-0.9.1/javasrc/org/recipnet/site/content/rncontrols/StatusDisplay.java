/*
 * Reciprocal Net project
 * 
 * StatusDisplay.java
 *
 * 24-Jun-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A very special purpose custom tag that displays one of two indicated
 * formatted {@code String}s for each sample status code. This tag must be
 * nested within a {@code SampleContext}. A line for each status is displayed
 * in an order such that no status is displayed before all prerquisite statuses
 * are already displayed. For each status code that does not represent the
 * current status of the sample provided by the {@code SampleContext}, the
 * value of'unselectedStatusPatternHtml' is included in the output (with all
 * instances of the 'statusNameReplacementToken' replaced with the localized
 * representation of the status). Otherwise the 'selectedStatusPatternHtml' is
 * used.
 */
public class StatusDisplay extends HtmlPageElement {

    /**
     * The most immediately enclosing {@code SampleContext}, used to determine
     * for which status code the 'selectedStatusPatternHtml' should be used.
     */
    private SampleContext sampleContext;

    /**
     * A required property that will be output for each status other than the
     * current status of the sample defined by the {@code SampleConext}. All
     * instances of the value of the 'statusNameReplacementToken' will be
     * replaced by the localized name of the status.
     */
    private String unselectedStatusPatternHtml;

    /**
     * A required property that will be output for the current status of the
     * sample defined by the {@code SampleConext}. All instances of the value
     * of the 'statusNameReplacementToken' will be replaced by the localized
     * name of the status.
     */
    private String selectedStatusPatternHtml;

    /**
     * An optional property that defaults to "\\[statusName\\]" and indicates a
     * {@code String} regular expression that if present as a substring of
     * either 'unselectedStatusPatternHtml' or 'selectedStatusPatternHtml' will
     * be replaced with the localized status name when they are output.
     */
    private String statusNameReplacementToken;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.selectedStatusPatternHtml = null;
        this.statusNameReplacementToken = "\\[statusName\\]";
    }

    /**
     * Sets the 'UnselectedStatusPatternHtml' property.
     */
    public void setUnselectedStatusPatternHtml(String pattern) {
        this.unselectedStatusPatternHtml = pattern;
    }

    /**
     * Gets the 'UnselectedStatusPatternHtml' property.
     */
    public String getUnselectedStatusPatternHtml() {
        return unselectedStatusPatternHtml;
    }

    /**
     * Sets the 'SelectedStatusPatternHtml' property.
     */
    public void setSelectedStatusPatternHtml(String pattern) {
        this.selectedStatusPatternHtml = pattern;
    }

    /**
     * Gets the 'SelectedStatusPatternHtml' property.
     */
    public String getSelectedStatusPatternHtml() {
        return selectedStatusPatternHtml;
    }

    /**
     * Sets the 'statusNameReplacementToken' property.
     */
    public void setStatusNameReplacementToken(String token) {
        this.statusNameReplacementToken = token;
    }

    /**
     * Gets the 'statusNameReplacementToken' property.
     */
    public String getStatusNameReplacementToken() {
        return this.statusNameReplacementToken;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets a
     * reference to the {@code SampleContext} that surrounds this tag.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleContext = findRealAncestorWithClass(this,
                SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version outputs the specified HTML for each status,
     * in roughly chronological order for a typical workflow.
     * 
     * @throws JspException if the {@code ResourceBundle}s used for status
     *         strings do not contain every status code.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        SampleInfo si = this.sampleContext.getSampleInfo();

        try {
            for (Integer code : SampleWorkflowBL.getAllStatusCodes()) {
                outputStatusString(code.intValue(), si, out);
            }
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }

        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * Writes a string for a single status to the provided {@code JspWriter}.
     * First it is determined whether the selected or unselected {@code String}
     * should be used. Then the tokens are replaced with the status strings
     * localized for the current request.
     * 
     * @param status the status code to be used
     * @param sample the sample whose current status is considered to be
     *        selected
     * @param out a {@code JspWriter} onto which output should be written
     * @throws IOException if one is thrown while accessing the
     *         {@code JspWriter}
     * @throws ResourceNotFoundException if the status code provided is invalid
     *         or for some other reason does not have a localized version
     *         according to {@code LanguageHelper}.
     */
    private void outputStatusString(int status, SampleInfo sample,
            JspWriter out) throws IOException, ResourceNotFoundException {
        LanguageHelper lh
                = LanguageHelper.extract(this.pageContext.getServletContext());
        String stringToUse = ((sample.status == status)
                ? this.selectedStatusPatternHtml
                : this.unselectedStatusPatternHtml);
        
        out.print(stringToUse.replaceAll(this.statusNameReplacementToken,
                lh.getStatusString(status,
                        this.pageContext.getRequest().getLocales(), true)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        StatusDisplay dc = (StatusDisplay) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        
        return dc;
    }
}
