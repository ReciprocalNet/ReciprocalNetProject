/*
 * Reciprocal Net project
 * 
 * AutoLocalLabIdField.java
 *
 * 27-Sep-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.TextboxHtmlControl;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourcesExhaustedException;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A custom tag that displays the most recent automatically generated local lab
 * id for the lab defined by the surrounding {@code LabContext}. If The
 * required 'selected' property is set to true, the sample provided by the
 * {@code SampleContext} in which this tag is nested will have its localLabId
 * field updated to reflect the displayed value.
 * </p><p>
 * Care must be taken by the JSP author to ensure that this tag does not
 * conflict with other tags (possible {@link SampleField SampleField}) that
 * could also change the sample's LocalLabId. Furthermore, any higher level
 * rules about when it is appropriate or inappropriate to modify a localLabId
 * must be handled by the JSP author.
 * </p>
 */
public class AutoLocalLabIdField extends TextboxHtmlControl {

    /**
     * An error flag that indicates that the parsed value is not the same as the
     * current suggested name.
     */
    public static final int PARSED_VALUE_IS_NOT_MOST_RECENT
            = TextboxHtmlControl.getHighestErrorFlag() << 1;

    /**
     * An error flag that indicates that the parsed value is null but 'selected'
     * has been set to true. This reflects the case when no auto local lab is
     * supplied (possibly because no more numbers exist) but the user (or
     * default setting) has selected the option for an auto-id.
     */
    public static final int SELECTED_WITH_NULL_VALUE
            = TextboxHtmlControl.getHighestErrorFlag() << 2;

    /**
     * Allows subclasses to extend the {@code ErrorSupplier} implementation.
     */
    public static int getHighestErrorFlag() {
        return PARSED_VALUE_IS_NOT_MOST_RECENT;
    }

    /** The {@code SampleContext} in which this tag is nested. */
    private SampleContext sampleContext;

    /** The {@code LabContext} in which this tag is nested. */
    private LabContext labContext;

    /**
     * A required transient property that when set to true indicates that this
     * tag should assign the localLabId for the sample indicated by the
     * {@code SampleContext} to that which was automatically generated for the
     * lab indicated by the {@code LabContext}. This value may change from
     * phase to phase, but the value during the {@code PROCESSING_PHASE} is used
     * to determine whether the lab's automatic localLabId will be used. If this
     * value is true during the {@code FETCHING_PHASE} but the value of this
     * control is null, a validation error and error flag is reported.
     */
    private boolean selected;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.labContext = null;
        this.selected = false;
        setDisplayAsLabel(true);
    }

    /** Setter for the 'selected' property. */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /** Getter for the 'selected' property. */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the surrounding
     * {@code SampleContext} and {@code LabContext}.
     * 
     * @throws IllegalStateException if this tag is not nested in the required
     *         contexts
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        this.labContext = findRealAncestorWithClass(this, LabContext.class);
        if (this.labContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version sets a validation error if this field is
     * 'selected' but has a {@code null} parsed value.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        if (this.selected && (getValue() == null)) {
            setErrorFlag(SELECTED_WITH_NULL_VALUE);
            setFailedValidation(true);
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}; this version updates the sample provided by the
     * {@code SampleContext} if 'selected' is true and an auto-assigned
     * LocalLabId is available for the lab.
     */
    @Override
    public int onProcessingPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseBeforeBody(pageContext);

        if ((getValue() != null) && this.selected && !getFailedValidation()
                && (this.sampleContext.getSampleInfo() != null)) {
            this.sampleContext.getSampleInfo().localLabId = (String) getValue();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the auto-generated localLabId if one is
     * available.
     * 
     * @throws JspException wrapping a {@code RemoteException} or a
     *         {@code OperationFailedException} that resulted from the call to
     *         core
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        int rc = super.onRenderingPhaseBeforeBody(out);
        CoreConnector cc
                = CoreConnector.extract(pageContext.getServletContext());
        
        try {
            String suggestedId = RequestCache.getNextAutoLocalLabId(
                    pageContext.getRequest(), this.labContext.getLabInfo().id);
            
            if (suggestedId == null) {
                // cache miss
                try {
                    suggestedId = cc.getSampleManager().getNextUnusedLocalLabId(
                            this.labContext.getLabInfo().id);
                } catch (ResourcesExhaustedException ex) {
                    suggestedId = RequestCache.AUTO_LOCAL_LAB_IDS_EXHAUSTED;
                }
                if (suggestedId == null) {
                    suggestedId = RequestCache.AUTO_LOCAL_LAB_IDS_DISABLED;
                }
                RequestCache.putNextAutoLocalLabId(pageContext.getRequest(),
                        this.labContext.getLabInfo().id, suggestedId);
            }
            
            if ((getValue() != null) && !getValue().equals(suggestedId)) {
                /*
                 * this case is always accompanied by a DuplicateDataException
                 * thrown from core that results in a page validation error
                 */
                setErrorFlag(PARSED_VALUE_IS_NOT_MOST_RECENT);
            }
            if (suggestedId.equals(RequestCache.AUTO_LOCAL_LAB_IDS_EXHAUSTED)
                    || suggestedId.equals(
                            RequestCache.AUTO_LOCAL_LAB_IDS_DISABLED)) {
                setValue(null);
            } else {
                setValue(suggestedId);
            }
        } catch (NullPointerException ex) {
            /*
             * the LabContext failed to provide a LabInfo, simply leave the
             * autoLabId as null
             */
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        this.selected = ((AutoLocalLabIdField) source).selected;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AutoLocalLabIdField dc
                = (AutoLocalLabIdField) super.generateCopy(newId, map);
        
        dc.labContext = (LabContext) map.get(this.labContext);
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        
        return dc;
    }
}
