/*
 * Reciprocal Net project
 * 
 * SampleFieldUnits.java
 *
 * 05-Aug-2005: midurbin wrote first draft
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
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A custom tag that displays the localized text/html units for a particular
 * {@code fieldCode}. Like {@code SampleField} this tag may determine the
 * {@code fieldCode} from a {@code SampleFieldContext} or
 * {@code SampleTextContext}. This tag will output nothing in the event that
 * there are no units for a particular 'fieldCode'. This tag should follow
 * {@code SampleField} tags in order to output the appropriate text for the
 * units.
 */
public class SampleFieldUnits extends HtmlPageElement {

    /**
     * Indicates which field's units will be displayed by this control. This
     * attribute is initialized by {@code reset()} and may be altered by its
     * 'setter' method, {@code setFieldCode()} but must not be altered after the
     * {@code REGISTRATION_PHASE}.
     */
    private int fieldCode;

    /**
     * A reference to the most immediate {@code SampleFieldContext} for use when
     * {@code fieldCode} has been set to
     * {@code SampleField.AUTO_DETECT_FIELD_CODE}.
     */
    private SampleFieldContext fieldContext;

    /**
     * A reference to the most immediate {@code SampleTextContext} for use when
     * {@code fieldCode} has been set to
     * {@code SampleField.AUTO_DETECT_FIELD_CODE} and no
     * {@code SampleFieldContext} could be found.
     */
    private SampleTextContext textContext;

    /** {@inheritDoc} */
    @Override
    public void reset() {
        super.reset();
        this.fieldCode = SampleField.AUTO_DETECT_FIELD_CODE;
        this.fieldContext = null;
        this.textContext = null;
    }

    /**
     * @param fieldCode a constant value indicating which element of the
     *        {@code SampleInfo} this {@code SampleFieldLabel} is describing.
     * @throws IllegalArgumentException if the fieldcode is not valid
     */
    public void setFieldCode(int fieldCode) {
        if (!SampleTextBL.isAttribute(fieldCode)
                && !SampleTextBL.isAnnotation(fieldCode)
                && !SampleDataInfo.isDataField(fieldCode)
                && !SampleInfo.isSampleField(fieldCode)
                && (fieldCode != SampleField.AUTO_DETECT_FIELD_CODE)) {
            // invalid fieldCode
            throw new IllegalArgumentException();
        }
        this.fieldCode = fieldCode;
    }

    /**
     * @return a constant value indicating which element of the
     *         {@code SampleInfo} this {@code SampleFieldUnits} is describing.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        if (this.fieldCode == SampleField.AUTO_DETECT_FIELD_CODE) {
            this.fieldContext = findRealAncestorWithClass(this,
                    SampleFieldContext.class);
            if (this.fieldContext == null) {
                this.textContext = findRealAncestorWithClass(this,
                        SampleTextContext.class);
                if (this.textContext == null) {
                    throw new IllegalStateException();
                }
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this versio writes the units for the field to the
     * {@code JspWriter} before delegating back to the superclass's
     * implementation.
     * 
     * @param out a {@code JspWriter} to which any output should be written
     * @throws IOException if an error is encountered while writing to the
     *         {@code JspWriter}.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        LanguageHelper lh
                = LanguageHelper.extract(pageContext.getServletContext());

        try {
            if (this.fieldCode == SampleField.AUTO_DETECT_FIELD_CODE) {
                if (this.fieldContext != null) {
                    out.print(lh.getFieldUnits(
                            this.fieldContext.getSampleField().getFieldCode(),
                            pageContext.getRequest().getLocales(), true));
                } else if (this.textContext != null) {
                    out.print(lh.getFieldUnits(this.textContext.getTextType(),
                            pageContext.getRequest().getLocales(), true));
                } else {
                    /*
                     * we already ensured that this tag was nested in either a
                     * SampleFieldContext or a SampleTextContext
                     */
                    assert false;
                }
            } else {
                out.print(lh.getFieldUnits(this.fieldCode,
                        pageContext.getRequest().getLocales(), true));
            }
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }

        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleFieldUnits dc = (SampleFieldUnits) super.generateCopy(newId, map);
        
        dc.fieldContext = (SampleFieldContext) map.get(this.fieldContext);
        dc.textContext = (SampleTextContext) map.get(this.textContext);
        
        return dc;
    }
}
