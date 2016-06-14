/*
 * Reciprocal Net Project
 *
 * SimpleMultiFilenameContext.java
 *
 * 20-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A standalone MultiFilenameContext that simply provides the names of all
 * repository files applicable to the surrounding {@code SampleContext} (which
 * must be present and must not return a {@code null} {@code SampleInfo}). This
 * tag is active only during the {@code FETCHING_PHASE}; until then it will
 * return an empty filename collection.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SimpleMultiFilenameContext extends HtmlPageElement
        implements MultiFilenameContext {

    private Collection<String> filenames;

    /**
     * {@inheritDoc}
     * 
     * @see org.recipnet.common.controls.HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        filenames = new HashSet<String>();
    }

    /**
     * {@inheritDoc}
     * 
     * @see MultiFilenameContext#getFilenames()
     */
    public Collection<String> getFilenames() {
        return Collections.unmodifiableCollection(filenames);
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleContext sc = findRealAncestorWithClass(this, SampleContext.class);

        if (sc == null) {
            throw new IllegalStateException("No sample context");
        } else {

            // Get the RepositoryFiles
            RepositoryFiles rf = RequestCache.getRepositoryFiles(
                    pageContext.getRequest(), sc.getSampleInfo().id,
                    sc.getSampleInfo().historyId);

            if (rf == null) {
                // Cache miss; fetch a RepositoryFiles object from
                // RepositoryManager
                CoreConnector cc = CoreConnector.extract(
                        pageContext.getServletContext());

                try {
                    RepositoryManagerRemote repositoryManager
                            = cc.getRepositoryManager();

                    rf = repositoryManager.getRepositoryFiles(
                            sc.getSampleInfo().id,
                            sc.getSampleInfo().historyId, false);
                    RequestCache.putRepositoryFiles(
                            super.pageContext.getRequest(), rf);
                } catch (RemoteException re) {
                    cc.reportRemoteException(re);
                    throw new JspException(re);
                } catch (RepositoryDirectoryNotFoundException rdnfe) {

                    /*
                     * this exception is more of an indicator than an exception
                     * and does not need to be thrown
                     */

                    return rc;
                } catch (InconsistentDbException ide) {
                    throw new JspException(ide);
                } catch (OperationFailedException ofe) {
                    throw new JspException(ofe);
                } catch (WrongSiteException wse) {
                    throw new JspException(wse);
                }
            }
            
            for (SampleDataFile dataFile : rf.getRecords()) {
                filenames.add(dataFile.getName());
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        SimpleMultiFilenameContext mfc =(SimpleMultiFilenameContext)
                super.generateCopy(newId, origToCopyMap);
        
        mfc.filenames = new HashSet<String>(this.filenames);
        
        return mfc;
    }

}
