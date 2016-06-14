/*
 * Reciprocal Net project
 * 
 * SampleHistoryTranslator.java
 * 
 * 05-Jul-2005: midurbin wrote first draft
 * 04-Aug-2005: midurbin added 'translateFromFileContext' property and
 *              fetchSampleHistoryInfo()
 * 17-Feb-2006: jobollin fixed bug #1731 in fetchSampleHistoryInfo(), requiring
 *              a fair amount of reorganization.  Took the opportunity to
 *              reformat the code at this point.
 */

package org.recipnet.site.content.rncontrols;


import java.rmi.RemoteException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * Provides a {@code SampleHistoryContext} that is derived from another
 * context.  Exactly one property must be set to indicate from where the
 * {@code SampleHistoryContext} will come.
 */
public class SampleHistoryTranslator extends HtmlPageElement
        implements SampleHistoryContext {

    /**
     * The most immediately surrounding {@code FileContext}; used when
     * the 'translateFromFileContext' property is set.
     */
    private FileContext fileContext;

    /**
     * The most immediately surrounding {@code SampleContext}; used when
     * the 'translateFromSampleContext' property is set.
     */
    private SampleContext sampleContext;

    /**
     * An optional property that when set to true indicates that this tag's
     * {@code SampleHistoryContext} should represent the sample version
     * supplied by the surrounding {@code SampleContext}.
     */
    private boolean translateFromSampleContext;

    /**
     * An optional property that, when set to true, enables one of this tag's
     * translation modes.  In this mode, the {@code SampleHistoryInfo}
     * exposed by this tag relates the the surrounding
     * {@code FileContext}'s file's 'originalHistoryId'.
     */
    private boolean translateFromFileContext;

    /** The {@code SampleHistoryInfo} supplied by this context. */
    private SampleHistoryInfo sampleHistoryInfo;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileContext = null;
        this.sampleContext = null;
        this.translateFromSampleContext = false;
        this.translateFromFileContext = false;
        this.sampleHistoryInfo = null;
    }

    /**
     * Sets the 'translateFromSampleContext' property.
     * @param translate indicates whether this tag's
     *     {@code SampleHistoryContext} should be derived from the
     *     surrounding {@code SampleContext}
     */
    public void setTranslateFromSampleContext(boolean translate) {
        this.translateFromSampleContext = translate;
    }

    /**
     * Gets the 'translateFromSampleContext' property.
     * @return a boolean that  indicates whether this tag's
     *     {@code SampleHistoryContext} should be derived from the
     *     surrounding {@code SampleContext}
     */
    public boolean getTranslateFromSampleContext() {
        return this.translateFromSampleContext;
    }

    /** Sets the 'translateFromFileContext' property. */
    public void setTranslateFromFileContext(boolean translate) {
        this.translateFromFileContext = translate;
    }

    /** Gets the 'translateFromFileContext' property. */
    public boolean getTranslateFromFileContext() {
        return this.translateFromFileContext;
    }

    /**
     * {@inheritDoc}.  This version checks to make sure that exactly one
     * 'translateFrom' property is set to {@code true}, and then finds
     * references to any surrounding tags that are needed.
     * 
     * @throws IllegalStateException if this tag is not nested in a required
     *         context, or if it is not the case that exactly one
     *         'translateFrom' property is set
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        // ensure that exactly one 'translateFrom' property is set
        if (!(this.translateFromSampleContext
                ^ this.translateFromFileContext)) {
            throw new IllegalStateException();
        }

        // find the context that will be translated to a SampleHistoryContext
        if (this.translateFromSampleContext) {
            this.sampleContext
                    = findRealAncestorWithClass(this, SampleContext.class);
            if (this.sampleContext == null) {
                throw new IllegalStateException();
            }
        } else if (this.translateFromFileContext) {
            this.fileContext
                    = findRealAncestorWithClass(this, FileContext.class);
            if (this.fileContext == null) {
                throw new IllegalStateException();
            }
        }
        return rc;
    }

    /**
     * {@inheritDoc}.  This version determines the {@code SampleHistoryInfo}
     * that is to be provided by this tag, which may be {@code null}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        if (this.translateFromSampleContext) {
            SampleInfo si = this.sampleContext.getSampleInfo();
            if (si != null) {
                this.sampleHistoryInfo
                        = fetchSampleHistoryInfo(si.id, si.historyId);
            }
        } else if (this.translateFromFileContext) {
            SampleDataFile sdf = this.fileContext.getSampleDataFile();
            if (sdf != null) {
                int historyId = sdf.getOriginalSampleHistoryId();

                // The file may not have any historical information (yet)
                if (historyId != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                    this.sampleHistoryInfo = fetchSampleHistoryInfo(
                            sdf.getSampleId(), historyId);
                }
            }
        }
        
        return rc;
    }

    /**
     * A private helper function that gets the {@code SampleHistoryInfo}
     * that corresponds to the given sample ID and sample history ID.
     * 
     * @param  sampleId the sampleId for which historical information is
     *         requested
     * @param  sampleHistoryId the sample history ID for which historical
     *         information is requested
     *         
     * @return a {@code SampleHistoryInfo} representing the requested history
     *         information 
     * 
     * @throws JspException wrapping any excetions thrown while fetching the
     *     {@code FullSampleInfo} from core.
     */
    private SampleHistoryInfo fetchSampleHistoryInfo(int sampleId,
            int sampleHistoryId) throws JspException {
        try {
            FullSampleInfo fullSampleInfo
                    = RequestCache.getFullSampleInfo(
                            this.pageContext.getRequest(), sampleId);
            
            if (fullSampleInfo == null) {
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());
        
                try {
                    fullSampleInfo
                            = cc.getSampleManager().getFullSampleInfo(sampleId);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw new JspException(ex);
                }
                RequestCache.putFullSampleInfo(
                        super.pageContext.getRequest(), fullSampleInfo);
            }
            
            for (SampleHistoryInfo historyInfo : fullSampleInfo.history) {
                if (historyInfo.id == sampleHistoryId) {
                    return historyInfo;
                }
            }
            
            /*
             * No appropriate SampleInfo was not found within the
             * FullSampleInfo.  
             */
            throw new JspException(
                    "No matching sample history record (sample ID = "
                    + sampleId + ", sample history ID = " + sampleHistoryId);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } 
    }

    /**
     * Implements {@code SampleHistoryContext}. 
     * @return the {@code SampleHistoryInfo} translated from another
     *     context
     * @throws IllegalStateException if called before the
     *     {@code FETCHING_PHASE}.
     */
    public SampleHistoryInfo getSampleHistoryInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        return this.sampleHistoryInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleHistoryTranslator dc
                = (SampleHistoryTranslator) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.fileContext = (FileContext) map.get(this.fileContext);
        
        return dc;
    }

}
