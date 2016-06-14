/*
 * Reciprocal Net project
 * 
 * SampleFieldLabel.java
 *
 * 16-Jun-2004: midurbin wrote first draft
 * 02-Jul-2004: midurbin added support for fieldCodes within the local tracking
 *              range to be provided by the SampleTextContext.
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 10-Mar-2005: midurbin fixed bug #1642 in onRenderingPhaseAfterBody()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 26-Apr-2005: midurbin added support for the case when SampleTextContext
 *              provides no SampleTextInfo
 * 10-Jun-2005: midurbin added support for SampleFieldContext
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A custom tag that displays the localized text/html name for a particular
 * {@code fieldCode}. Like {@code SampleField} this tag may determine the
 * {@code fieldCode} from a {@code SampleTextContext}. If this tag is supplied
 * a {@code fieldCode} from within the range reserved for local tracking
 * attributes, then it must be nested within a {@code LabContext} in order to
 * determine the lab whose {@code LocalTrackingConfig} applies.
 */
public class SampleFieldLabel extends HtmlPageElement {

    /**
     * Indicates which field's label will be displayed by this tag. This
     * attribute is initialized by {@code reset()} and may be altered by its
     * 'setter' method {@code setFieldCode()}, but must not be altered after
     * the {@code REGISTRATION_PHASE}. Valid values come from either text type
     * codes defined on {@code SampleTextInfo}, codes defined on
     * {@code SampleDataInfo}, local tracking attributes, field codes defined
     * on {@code SampleInfo} or its default value,
     * {@code SampleField.AUTO_DETECT_FIELD_CODE}.
     */
    private int fieldCode;

    /**
     * A reference to the most immediate {@code SampleTextContext} for use when
     * {@code fieldCode} has been set to
     * {@code SampleField.AUTO_DETECT_FIELD_CODE}. This context is located
     * during the {@code REGISTRATION_PHASE} in such cases.
     */
    private SampleTextContext sampleTextContext;

    /**
     * A reference to the most immediate {@code SampleFieldContext} for use when
     * {@code fieldCode} has been set to
     * {@code SampleField.AUTO_DETECT_FIELD_CODE}. If this value is set
     * (meaning that such a context was found) this tag will get its 'fieldCode'
     * from this context rather than a {@code SampleTextContext}.
     */
    private SampleFieldContext fieldContext;

    /**
     * When the {@code fieldCode} refers to a local tracking field, the
     * {@code Lab} must be known to get the correct {@code LocalTrackingConfig}
     * object. For this reason, this tag requires a {@code LabContext}, which
     * is located during the {@code REGISTRATION_PHASE}.
     */
    private LabContext labContext;

    /** {@inheritDoc} */
    @Override
    public void reset() {
        super.reset();
        this.fieldCode = SampleField.AUTO_DETECT_FIELD_CODE;
        this.sampleTextContext = null;
        this.fieldContext = null;
        this.labContext = null;
    }

    /**
     * @param fieldCode a constant value indicating which element of the
     *        {@code SampleInfo} this {@code SampleFieldLabel} is describing.
     * @throws IllegalArgumentException if an unknown fieldCode is provided
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
     *         {@code SampleInfo} this {@code SampleFieldLabel} is describing.
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
                this.sampleTextContext = findRealAncestorWithClass(this,
                        SampleTextContext.class);
                if (this.sampleTextContext == null) {
                    // Can't find the required context from ancestry
                    throw new IllegalStateException();
                }
            }
        }

        /*
         * The lab context is only needed if the field code resolves to a local
         * tracking attribute (which are lab-specific), but because there are
         * cases where the fieldCode is taken from a SampleTextInfo during the
         * fetching phase, it's not possible to determine whether we'll need the
         * LabInfo object at this time. Therefore, a reference to the LabContext
         * is found at this time, but until it's certain that it'll be needed,
         * no exception is thrown if it happens to be null, or provides a null
         * value for the LabInfo.
         */
        this.labContext = findRealAncestorWithClass(this, LabContext.class);

        return rc;
    }

    /**
     * {@inheritDoc}; this version writes the label for the field (determined
     * by a call to {@code getLabelForFieldCode()}) to the {@code JspWriter}
     * before delegating back to the superclass's implementation.
     * 
     * @param out a {@code JspWriter} to which any output should be written
     * @return an int, indicating whether the body should be evaluated
     * @throws IOException if an error is encountered while writing to the
     *         {@code JspWriter}.
     * @throws JspException fi an exception was encountered during evaluation
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        if (((this.sampleTextContext != null)
                && (this.sampleTextContext.getSampleTextInfo() == null))
                || ((this.fieldContext != null)
                        && (this.fieldContext.getSampleField() == null))) {
            // the field type is unknown, display nothing
            return super.onRenderingPhaseAfterBody(out);
        }
        
        int resolvedFieldCode
                = (this.fieldCode == SampleField.AUTO_DETECT_FIELD_CODE)
                        ? ((this.fieldContext == null)
                                ? this.sampleTextContext.getSampleTextInfo().type
                                : this.fieldContext.getSampleField().getFieldCode())
                        : this.fieldCode;
        String label = getLabelForFieldCode(resolvedFieldCode,
                super.pageContext);

        out.print(label == null ? "" : label);

        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * A private helper method that converts a {@code fieldCode} into a
     * {@code String} that represents the name of that field.
     * 
     * @param fieldCode an int, defined in either {@code SampleTextInfo},
     *        {@code SampleDataInfo}, {@code SampleInfo} or in the
     *        configuration file in the case of local tracking fields.
     * @param pageContext the current {@code PageContext} used to cache
     *        {@code CurrentLta} objects.
     * @return the localized name of the field, or null if it is an unknown
     *         field code in the local tracking range
     * @throws IllegalStateException if this tag is not nested within a null
     *         {@code LabContext} and represents a
     *         {@code LocalTrackingAttribute}.
     * @throws JspException if an exception is encountered during this method
     */
    private String getLabelForFieldCode(int fieldCode, PageContext pageContext)
            throws JspException {
        if (SampleTextBL.isLocalAttribute(fieldCode)) {
            // Special handling for local tracking fields
            if ((this.labContext == null) || (this.labContext.getLabInfo() == null)) {
                throw new IllegalStateException();
            }
            LabInfo labInfo = labContext.getLabInfo();

            try {
                LocalTrackingConfig currentLtc = RequestCache.getLTC(
                        pageContext.getRequest(), labInfo.id);

                if (currentLtc == null) {
                    // cache miss
                    CoreConnector cc = CoreConnector.extract(
                            pageContext.getServletContext());

                    currentLtc = cc.getSiteManager().getLocalTrackingConfig(
                            labInfo.id);
                    RequestCache.putLTC(pageContext.getRequest(), currentLtc);
                }
                return currentLtc.getFieldName(fieldCode);
            } catch (RemoteException ex) {
                throw new JspException(ex);
            } catch (WrongSiteException ex) {
                throw new JspException(ex);
            }
        }
        try {
            return LanguageHelper.extract(
                    pageContext.getServletContext()).getFieldString(
                            fieldCode, pageContext.getRequest().getLocales(),
                            true);
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleFieldLabel dc = (SampleFieldLabel) super.generateCopy(newId, map);

        dc.labContext = (LabContext) map.get(this.labContext);
        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);
        dc.fieldContext = (SampleFieldContext) map.get(this.fieldContext);

        return dc;
    }
}
