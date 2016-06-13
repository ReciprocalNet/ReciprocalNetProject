/*
 * Reciprocal Net Project
 *
 * CifSampleContextFilter.java
 *
 * 20-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CifFileToSampleInfoConverter;

/**
 * Similar to a {@code SampleContextTranslator}, this tag must be nested within
 * a {@code SampleContext} and it provides a {@code SampleContext}.  This tag
 * is more specialized, however: it is designed so that it <em>optionally</em>
 * provides a filtered view of the surrounding {@code SampleContext} during the
 * {@code FETCHING_PHASE} (only).  When applied, the filter provides a view of
 * the sample data that is consistent with the first data block or a specified
 * CIF, inasmuch as that data block contains any pertinent information.  Because
 * it filters only during the {@code FETCHING_PHASE}, this tag allows a user to
 * update the surrounding sample context by means of an HTTP POST:
 * {@code SampleField}s contained inside this filter update their values from
 * the filtered sample context during the {@code FETCHING_PHASE}, then update
 * the unfiltered surrounding context during the {@code PROCESSING_PHASE}.  The
 * filtered view provided by this tag is not available outside the tag body.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CifSampleContextFilter extends HtmlPageElement
        implements SampleContext, ValuePriorityOverrideContext, ErrorSupplier {
    
    /**
     * An {@code ErrorSupplier} error flag raised at the beginning of the
     * {@code FETCHING_PHASE} if this filter is enabled but no {@code CifFile}
     * has been provided to it.
     */
    private final static int NO_CIF = 1 << 0;
    
    /**
     * An {@code ErrorSupplier} error flag raised at the beginning of the
     * {@code FETCHING_PHASE} if this filter is enabled but the provided
     * {@code CifFile} contains no data blocks.
     */
    private final static int EMPTY_CIF = 1 << 1;
    
    /**
     * A required, transient tag attribute representing the CIF with which this
     * filter will filter the host context's sample information
     */
    private CifFile cif;
    
    /**
     * An optional, transient tag attribute with which the operation of this
     * filter can be made conditional.  Defaults to {@code true}.
     */
    private boolean enabled;
    
    /**
     * An optional, transient tag attribute directing whether this tag should
     * handle quoted question marks in some CIF data items as bare question
     * marks; this violates the CIF standard, but nevertheless correctly
     * interprets CIFs produced by some versions of SHELXL.  It is fairly safe
     * to set this attribute to {@code true}, but it defaults to {@code false}. 
     */
    private boolean fixShelxCifs;
    
    /**
     * The innermost {@code SampleContext} surrounding this one; the 
     * {@code SampleInfo} to be filtered (if any) is obtained from this context.
     */
    private SampleContext hostContext;
    
    /**
     * The filtered {@code SampleInfo}, or {@code null} if filtering is
     * disabled, failed, or has not yet been performed.
     */
    private SampleInfo filteredSample;
    
    /**
     * The {@code ErrorSupplier} error code currently set on this context
     */
    private int errorCode;

    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        cif = null;
        enabled = true;
        fixShelxCifs = false;
        hostContext = null;
        filteredSample = null;
        errorCode = 0;
    }

    /**
     * Gets the {@code CifFile} object configured on this filter
     * 
     * @return the {@code CifFile} value of this tag's 'cif' property
     */
    public CifFile getCif() {
        return cif;
    }

    /**
     * Configures the {@code CifFile} object to be used by this filter
     * 
     * @param  cif the {@code CifFile} to set as the value of this tag's 'cif'
     *         property
     */
    public void setCif(CifFile cif) {
        this.cif = cif;
    }

    /**
     * Determines whether this tag's filtering behavior is enabled.  This may
     * change from phase to phase, but it is only relevant during the
     * {@code FETCHING_PHASE}.
     * 
     * @return {@code true} if this tag is configured to filter, {@code false}
     *         if not
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Configures whether this tag handler should in fact filter the surrounding
     * sample context.
     * 
     * @param  enabled {@code true} (the default) to enable filtering,
     *         {@code false} to disable it
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Determines whether this tag handler is configured to accommodate
     * certain semantic irregularities in CIFs produced by some versions of
     * SHELXL
     *  
     * @return {@code true} if this tag is configured to interpret the
     *         SHELX-style CIFs as is meant by SHELXL, instead of strictly
     *         according to the CIF specs, {@code false} if it is configured
     *         for strict compliance with CIF
     */
    public boolean isFixShelxCifs() {
        return fixShelxCifs;
    }

    /**
     * Determines whether this tag handler is configured to accommodate
     * certain semantic irregularities in CIFs produced by some versions of
     * SHELXL
     *  
     * @param  fixShelxCifs {@code true} if this tag should interpret the
     *         SHELX-style CIFs as is meant by SHELXL, instead of strictly
     *         according to the CIF specs, {@code false} if it should comply
     *         strictly with CIF
     */
    public void setFixShelxCifs(boolean fixShelxCifs) {
        this.fixShelxCifs = fixShelxCifs;
    }

    /**
     * {@inheritDoc}.
     * 
     * @see SampleContext#getSampleInfo()
     */
    public SampleInfo getSampleInfo() {
        return (filteredSample == null) ? hostContext.getSampleInfo()
                : filteredSample;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ValuePriorityOverrideContext#getFetchedValuePriority(HtmlControl)
     */
    public int getFetchedValuePriority(HtmlControl targetControl) {
        if (isEnabled()) {
            return (targetControl.getEditable()
                        && !targetControl.getDisplayAsLabel())
                    ? HtmlControl.PARSED_VALUE_PRIORITY
                    : HtmlControl.LOWEST_PRIORITY;
        } else {
            return HtmlControl.EXISTING_VALUE_PRIORITY;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see ValuePriorityOverrideContext#getFetchedNullPriority(HtmlControl)
     */
    public int getFetchedNullPriority(HtmlControl targetControl) {
        if (isEnabled()) {
            return (targetControl.getEditable()
                        && !targetControl.getDisplayAsLabel())
                    ? HtmlControl.PARSED_VALUE_PRIORITY
                    : HtmlControl.LOWEST_PRIORITY;
        } else {
            return HtmlControl.DEFAULT_VALUE_PRIORITY;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        hostContext = findRealAncestorWithClass(this, SampleContext.class);
        if (hostContext == null) {
            throw new IllegalStateException("No surrounding sample context");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo originalSample = hostContext.getSampleInfo();
        
        if (isEnabled() && (originalSample != null)) {
            if (cif == null) {
                setErrorFlag(NO_CIF);
            } else {
                Iterator<DataBlock> blockIterator = cif.blockIterator();
                
                if (!blockIterator.hasNext()) {
                    setErrorFlag(EMPTY_CIF);
                } else {
                    CifFileToSampleInfoConverter converter
                            = new CifFileToSampleInfoConverter();

                    converter.setFixingShelxUnknownValues(
                            this.isFixShelxCifs());
                    filteredSample = originalSample.clone();
                    converter.update(blockIterator.next(), filteredSample);
                }
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#onFetchingPhaseAfterBody()
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        filteredSample = null;
        return super.onFetchingPhaseAfterBody();
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        CifSampleContextFilter copy = (CifSampleContextFilter)
                super.generateCopy(newId, origToCopyMap);
        
        copy.hostContext = (SampleContext) origToCopyMap.get(this.hostContext);
        
        return copy;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        CifSampleContextFilter filter = (CifSampleContextFilter) source;
        
        super.copyTransientPropertiesFrom(source);
        this.setCif(filter.getCif());
        this.setEnabled(filter.isEnabled());
        this.setFixShelxCifs(filter.isFixShelxCifs());
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
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return EMPTY_CIF;
    }
}
