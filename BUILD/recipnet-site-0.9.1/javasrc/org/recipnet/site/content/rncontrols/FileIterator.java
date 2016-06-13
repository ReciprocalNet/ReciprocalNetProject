/*
 * Reciprocal Net project
 * 
 * FileIterator.java
 * 
 * 29-Jun-2004: midurbin wrote the first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 09-Aug-2004: cwestnea added filterByExtension attribute
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 10-Mar-2005: midurbin fixed bug #1542 in onFetchingPhaseBeforeBody()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 06-Apr-2005: midurbin fixed bug #1574 in onIterationBeforeBody()
 * 02-May-2005: ekoperda made adjustments to accommodate spec changes in
 *              RepositoryFiles and FileContext
 * 04-May-2005: midurbin added new error flag: NO_FILES_IN_REPOSITORY
 * 21-Jun-2005: midurbin moved the ErrorSupplier implementation to the
 *              superclass and added isCurrentIterationLast()
 * 05-Jul-2005: ekoperda refactored existing code into new fetchFiles() method
 * 04-Aug-2005: midurbin added getCumulativeByteCount(),
 *              getCumulativeAggregateByteCount()
 * 16-Sep-2005: midurbin clarified purpose of error flags and fixed bug #1629
 * 26-Sep-2005: midurbin fixed bug #1659
 * 10-Nov-2005: midurbin added 'sortFilesByName' property
 * 11-Jan-2006: jobollin removed unused imports and made this tag implement
 *              MultiFilenameContext in as sensible a way as can be achieved
 *              while ensuring that existing pages don't break; reformatted the
 *              source to comply better with Reciprocal Net conventions --
 *              especially so that it does not contain tab characters
 * 05-May-2006: fixed bug #1789 in isCurrentIterationLast() by using an
 *              IteratorFilter instead of performing filtration manually
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.IteratorFilter;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryDirectoryNotFoundException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * <p>
 * A JSP custom {@code Tag} that obtains a bundle of sample data files
 * via a number of different mechanisms and then iterates over the bundle.
 * Each sample data file in the bundle is exposed to nested tags via the
 * {@code FileContext} during one iteration.
 * </p><p>
 * If this tag is enclosed within both a {@code SampleContext} and a
 * {@code UserContext}, then a bundle describing sample data files is
 * fetched from core via a call to 
 * {@code RepositoryManager.getRepositoryFiles()}.  The sample data files
 * fetched are those associated with the sample-version fetched from the
 * nearest enclosing {@code SampleContext}.  The nearest enclosing
 * {@code UserContext} is consulted to determine whether the current user
 * has authorization to request unavailable sample data files.
 * </p><p>
 * No other mechanisms for obtaining bundles of sample data files are
 * implemented at this time.
 * </p><p>
 * Some features are available for skipping the iterations of particular files
 * that may fulfill various criteria.
 * </p><pre>
 * Contexts Recognized:
 * SampleContext - provides the SampleInfo for the sample whoses files will be
 *                 made accessible to the body of this Tag
 * UserContext   - while this tag is not responsible for checking whether the
 *                 SampleInfo is accessible by the current user, a reference to
 *                 the current user is needed in cases when large files are
 *                 initially excluded to determine whether the current user
 *                 has authorization to perform the fairly taxing operation of
 *                 extracting old versions of files
 * <br/>
 * Contexts Provided:
 * FileContext   - the primary purpose of this Tag is to provide nested
 *                 {@code FileField} objects with a
 *                 {@code FileContext}.
 * ErrorSupplier - when an exception is thrown while attempting to fetch the
 *                 {@code RepositoryFiles} object from core, useful
 *                 information is provided to {@code ErrorSupplier}-
 *                 recognizing tags.
 * </pre><p>
 * This tag recognizes the post parameter "makeFilesAvailable" and when present
 * if the user is authorized to do so, the file size limit is ignored during
 * the fetch.
 * </p>
 */
public class FileIterator extends HtmlPageIterator
        implements FileContext, MultiFilenameContext {

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception
     * was thrown while fetching the repository files because no directory
     * existed on the filesystem and no holding record existed in the
     * database.
     */
    public static final int NO_DIRECTORY_NO_HOLDINGS
            = HtmlPageIterator.getHighestErrorFlag() << 1;

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception
     * was thrown while fetching the repository files because no directory
     * existed on the filesystem even though a holding record existed in the
     * database.
     */
    public static final int HOLDINGS_BUT_NO_DIRECTORY
            = HtmlPageIterator.getHighestErrorFlag() << 2;

    /**
     * An {@code ErrorSupplier} error flag indicating that an exception
     * was thrown while fetching the repository files because a directory
     * existed on the filesystem even though no holding record existed in the
     * database.
     */
    public static final int DIRECTORY_BUT_NO_HOLDINGS
            = HtmlPageIterator.getHighestErrorFlag() << 3;

    /**
     * An {@code ErrorSupplier} error flag indicating that one or more
     * files are not currently accessible to the currently logged-in user but
     * are available upon request.
     */
    public static final int SOME_FILES_AVAILABLE_UPON_REQUEST
            = HtmlPageIterator.getHighestErrorFlag() << 4;

    /**
     * An {@code ErrorSupplier} error flag indicating that one or more
     * files are not accessible to the currently logged-in user.
     */
    public static final int SOME_FILES_UNAVAILABLE
            = HtmlPageIterator.getHighestErrorFlag() << 5;

    /**
     * An {@code ErrorSupplier} error flag indicating that a repository
     * directory exists as does a holding record but that no files are present
     * in the otherwise valid repository.  This differs from 
     *{@code HtmlPageIterator.NO_ITERATIONS} because this will not be set
     * when a {@code RepositoryDirectoryNotFoundException} is thrown by
     * core.
     */
    public static final int NO_FILES_IN_REPOSITORY
            = HtmlPageIterator.getHighestErrorFlag() << 6;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return NO_FILES_IN_REPOSITORY;
    }

    /**
     * The bundle of one or more {@code SampleDataFile} objects that this
     * tag will iterate through.  This is set by 
     * {@code onFetchingPhaseBeforeBody()}.  It may be null if no bundle
     * was fetched for some reason.
     */
    private Collection<? extends SampleDataFile> sampleDataFiles;

    /**
     * An iterator into the {@code sampleDataFiles} collection.  This is
     * set by {@code beforeIteration()} and may be null.
     */
    private Iterator<? extends SampleDataFile> sampleDataFilesIterator;
    
    /**
     * A reference to one of the elements within the
     * {@code sampleDataFiles} collection.  This is set by 
     * {@code onIterationBeforeBody()} and may be null.
     */
    private SampleDataFile currentSampleDataFile;

    /**
     * The most immediate {@code SampleContext}, initialized to null by
     * {@code reset()} and set during the {@code REGISTRATION_PHASE},
     * it is used during the {@code FETCHING_PHASE} to determine the
     * sample whose files will be iterated by this {@code FileIterator}.
     */
    private SampleContext sampleContext;

    /**
     * The most immediate {@code UserContext}, initialized to null by
     * {@code reset()} and set during the {@code REGISTRATION_PHASE}.
     * The user is considered to be the currently logged in user, and may be
     * needed to determine whether inaccessible files may be made accessible.
     * It is important to note that this tag is NOT responsible for determining
     * whether this user may view these files, for that is the job of the
     * {@code SampleContext} implementation.
     */
    private UserContext userContext;
    
    /**
     * The closest enclosing {@code MultiFilenameContext}, or {@code null} if
     * there is none; initialized to {@code null} by {@link #reset()} and
     * assigned an appropriate value during the {@code REGISTRATION_PHASE}.  If
     * non-{@code null} then this {@code FileIterator} delegates to the delegate
     * to fulfill its own {@code MultiFilenameContext} contract.
     */
    private MultiFilenameContext multiFilenameContextDelegate;
    
    /**
     * A cached list of the names of the files to be delivered by this iterator;
     * initialized to {@code null} by {@link #reset()} and set only when
     * necessary by {@link #getFilenames()}
     */
    private List<String> filenames;

    /**
     * In the event that a {@code RepositoryDirectoryNotFoundException} is
     * thrown while this class was attempting to fetch a
     * {@code RepositoryFiles} object from core, a reference is kept so 
     * that information about the failure can be aquired by other
     * special-purpose tags.
     */
    private RepositoryDirectoryNotFoundException dirNotFoundException;

    /**
     * An optional property that defaults to false, indicating whether an
     * attempt should be made to request files that could be made available
     * by a request from the current user.  This property should not be
     * specified if {@code requestUnavailableFilesParamName} is also
     * specified.  This propery should not change from phase to phase.
     */
    private boolean requestUnavailableFiles;

    /**
     * The name of a parameter (possibly an {@code HtmlControl}'s id) that
     * if non-null, indicates that files that would be available upon requst
     * should be requested.  This is an optional property that should not
     * change from phase to phase and should not be set if
     * {@code requestUnavailableFiles} is set.  This is a request
     * parameter sent by the client.  If it has any value then unavailable
     * files will be requested (if the user is authorized to do so) otherwise
     * they will not be requested.
     */
    private String requestUnavailableFilesParamName;

    /**
     * Optional attribute, defaults to null; specifies the extension to filter
     * the files by.  This has the effect of skipping the iterations for any
     * files whose name ends in {@code .xxx}, where {@code xxx} is
     * the value of this field.  If this field is set to null, no filter will
     * be used.
     */
    private String filterByExtension;

    /**
     * Keeps track of the total number of bytes for all files whose sizes may
     * be determined that have been included so far in the iteration including
     * the file provided by the current iteration.  This value is made
     * available through the {@code getCumulativeByteCount()} method.
     */
    private long cumulativeByteCount;

    /**
     * An optional property that defaults to true and indicates that the
     * collection of files returned by {@code fetchFiles} should be sorted
     * by filename before being iterated over.
     */
    private boolean sortFilesByName;
    
    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleDataFiles = null;
        this.sampleDataFilesIterator = null;
        this.currentSampleDataFile = null;
        this.sampleContext = null;
        this.userContext = null;
        this.multiFilenameContextDelegate = null;
        this.filenames = null;
        this.dirNotFoundException = null;
        this.requestUnavailableFiles = false;
        this.requestUnavailableFilesParamName = null;
        this.filterByExtension = null;
        this.cumulativeByteCount = 0;
        this.sortFilesByName = true;
    }

    /**
     * @param requestFiles a boolean indicating whether unavailable files
     *     should be requested
     * @throws IllegalStateException if
     *     {@code requestUnavalableFilesParamName} is not null
     */
    public void setRequestUnavailableFiles(boolean requestFiles) {
        if (this.requestUnavailableFilesParamName != null) {
            throw new IllegalStateException();
        }
        this.requestUnavailableFiles = requestFiles;
    }

    /**
     * @param paramName a boolean indicating whether unavailable files
     *     should be requested
     * @throws IllegalStateException if {@code requestUnavalableFiles} is
     *     not false
     */
    public void setRequestUnavailableFilesParamName(String paramName) {
        if (this.requestUnavailableFiles) {
            throw new IllegalStateException();
        }
        this.requestUnavailableFilesParamName = paramName;
    }

    /** @param filterByExtension the extension to filter by */
    public void setFilterByExtension(String filterByExtension) {
        this.filterByExtension = filterByExtension;
    }


    /** Setter for the 'sortFilesByName' property. */
    public void setSortFilesByName(boolean sortFilesByName) {
        this.sortFilesByName = sortFilesByName;
    }

    /** Getter for the 'sortFilesByName' property. */
    public boolean getSortFilesByName() {
        return this.sortFilesByName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see FileContext
     */
    public SampleDataFile getSampleDataFile() {
        if (super.getPage().getPhase() < HtmlPage.FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        return this.currentSampleDataFile;
    }

    /**
     * {@inheritDoc}.  This version returns the list of the file names provided
     * by the innermost containing {@code MultiFilenameContext} if there is one,
     * or otherwise a list of all the file names that will be (or have been or
     * are being) iterated by this tag.
     * 
     * @see MultiFilenameContext#getFilenames()
     */
    public Collection<String> getFilenames() {
        if (this.multiFilenameContextDelegate != null) {
            return this.multiFilenameContextDelegate.getFilenames();
        } else if (filenames == null) {
            List<String> filenameList
                    = new ArrayList<String>(sampleDataFiles.size());
            
            for (SampleDataFile file : sampleDataFiles) {
                filenameList.add(file.getName());
            }
            
            filenames = Collections.unmodifiableList(filenameList);
        }
        
        return filenames;
    }

    /**
     * Gets the total number of bytes for all settled files that have been
     * provided by this tag for this phase.  If called from within this tag,
     * this value will include the size of the file provided for the current
     * iteration as well as all of those for previous iterations.
     */
    public long getCumulativeByteCount() {
        return this.cumulativeByteCount;
    }

    /**
     * Gets the total number of bytes used in the CVS repository for every 
     * file over which this {@code FileIterator} will iterate.  This
     * differs from {@link #getCumulativeByteCount()} in that it is only
     * useful for determining information about ALL files included in this 
     * iterator.  This method is relatively slow so callers should eliminate
     * unnecessary calls.
     * 
     * @throws IllegalStateException if called before the
     *     {@code FETCHING_PHASE}
     * @throws JspException wrapping any exceptions thrown by core
     */
    public long getCumulativeAggregateByteCount() throws JspException {
        if ((this.getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (this.getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        
        long totalSizeInBytes = 0;
        CoreConnector cc = CoreConnector.extract(
                this.pageContext.getServletContext());
        
        try {
            RepositoryManagerRemote repositoryManager
                    = cc.getRepositoryManager();
            int sampleId = this.sampleContext.getSampleInfo().id;
            for (SampleDataFile sdf : this.sampleDataFiles) {
                totalSizeInBytes += repositoryManager.getDataFileAggregateSize(
                        sampleId, sdf.getName());
            }
            return totalSizeInBytes;
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (RepositoryDirectoryNotFoundException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        }
    }

    /**
     * Gets the {@code RepositoryDirectoryNotFoundException} if one was
     * thrown by {@code RepositoryManager} during the
     * {@code FETCHING_PHASE} or returns null if none was thrown or if the
     * {@code FETCHING_PHASE} has not yet arrived.  One can use the return
     * codes from {@code getErrorCode()} to determine whether or not such
     * an exception was thrown.
     * 
     * @return the {@code RepositoryDirectoryNotFoundException} or {@code null}
     */
    public RepositoryDirectoryNotFoundException getDirNotFoundException() {
        return this.dirNotFoundException;
    }

    /**
     * Overrides {@code HtmlPageIterator} to return true during the last
     * iteration of the {@code RENDERING_PHASE}.
     * 
     * @throws IllegalStateException if called before the
     *     {@code RENDERING_PHASE}
     */
    @Override
    public boolean isCurrentIterationLast() {
        if (getPage().getPhase() != HtmlPage.RENDERING_PHASE) {
            throw new IllegalStateException();
        }
        return !this.sampleDataFilesIterator.hasNext();
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation
     * first determines the {@code SampleContext} and
     * {@code UserContext}  This method delegates back to its superclass
     * to get its return value.
     * 
     * @param pageContext the current {@code PageContext}
     * 
     * @return an int indicating whether the body should be evaluated
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // Find a SampleContext and UserContext.  The usual 
        // IllegalStateException's we might throw are postponed until 
        // fetchFiles() in order to offer more flexibility to subclasses.
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);

        // get UserContext
        this.userContext = findRealAncestorWithClass(this, UserContext.class);

        // gets the MultiFilenameContext to delegate to, if any
        this.multiFilenameContextDelegate
                = findRealAncestorWithClass(this, MultiFilenameContext.class);
        
        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets
     * the {@code SampleInfo} and {@code UserInfo} from their
     * repective contexts before fetching the appropriate
     * {@code RepositoryFiles} object from the core.
     * 
     * @return the return value is determined via a call to
     *     {@code shouldEvaluateBody()}.
     *     
     * @throws JspException any exception thrown while attempting to get data
     *     from the core will be set as the root cause of a new
     *     {@code JspException}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        class FilenameComparator implements Comparator<SampleDataFile> {
            public int compare(SampleDataFile f1, SampleDataFile f2) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        }
        
        int rc = super.onFetchingPhaseBeforeBody();

        if (!this.sortFilesByName) {
            this.sampleDataFiles = this.fetchFiles();
        } else {
            List<SampleDataFile> dataFiles
                    = new ArrayList<SampleDataFile>(this.fetchFiles());
            
            Collections.sort(dataFiles, new FilenameComparator());
            this.sampleDataFiles = dataFiles;
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageIterator}; the current implementation
     * simply calls {@code RepositoryFiles.initForIteration()} if
     * {@code respositoryFiles} is not {@code null}.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void beforeIteration() {
        if (this.sampleDataFiles != null) {
            this.sampleDataFilesIterator 
                    = new FileIteratorFilter(this.sampleDataFiles.iterator());
        }
        this.cumulativeByteCount = 0;
    }

    /**
     * Overrides {@code HtmlPageIterator}; the current implementation
     * repositions the {@code currentSampleDataFile} pointer and returns
     * true, or returns false if no more sample data files remain to be
     * iterated over or iteration is not possible.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if ((this.getPage().getPhase() == HtmlPage.PARSING_PHASE)
                && (this.getPostedIterationCount()
                        > this.getIterationCountSinceThisPhaseBegan())) {
            // evaluate the body for the benefit of nested elements,
            // even though those nested elements may not yet get the
            // RepositoryFileInfo. This ensures that the FileFields can
            // parse values that might have been posted for this request
            return true;
        }

        // Find a suitable sample data file to expose to nested tags during
        // the next iteration, skipping over any files that do not match our
        // filter criteria.
        if ((this.sampleDataFiles == null)
                || (this.sampleDataFilesIterator == null)
                || !this.sampleDataFilesIterator.hasNext()) {
            return false;
        }
        this.currentSampleDataFile = this.sampleDataFilesIterator.next();

        if (this.currentSampleDataFile.getSize()
                != SampleDataFile.INDETERMINATE_SIZE) {
            this.cumulativeByteCount += this.currentSampleDataFile.getSize();
        }

        // Do some checks on the sample data file we're about to expose and
        // possibly set some of our error flags.
        if (this.currentSampleDataFile instanceof RepositoryFiles.Record) {
            RepositoryFiles.Record rec
                    = (RepositoryFiles.Record) this.currentSampleDataFile;
            if (rec.getAvailability()
                    == RepositoryFiles.Availability.ON_REQUEST) {
                // The current file is available only upon request.
                if (AuthorizationCheckerBL.canRequestUnavailableFiles(
                        this.userContext.getUserInfo(),
                        this.sampleContext.getSampleInfo(),
                        super.pageContext.getServletContext())) {
                    // The user is authorize to request the file.
                    this.setErrorFlag(SOME_FILES_AVAILABLE_UPON_REQUEST);
                } else {
                    // The user is not authorized to request the file.
                    this.setErrorFlag(SOME_FILES_UNAVAILABLE);
                }
            }
        }

        return true;
    }

    /**
     * This function implements the file-filtering capabilities advertised by
     * this class.  It consults the {@code currentSampleDataFile} field
     * and returns true if all criteria are met or false otherwise.  Subclasses
     * may override this function but should return only false; they should
     * then delegate to this function, which may or may not return true.
     */
    protected boolean isCurrentSampleDataFileSuitable() {
        assert this.currentSampleDataFile != null;
        
        if ((this.filterByExtension != null)
                && !this.currentSampleDataFile.getName().endsWith(
                        "." + this.filterByExtension)) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileIterator dc = (FileIterator) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.userContext = (UserContext) map.get(this.userContext);
        
        return dc;
    }

    /**
     * A protected helper method that fetches a bunch of 
     * {@code SampleDataFile} objects that this tag will then iterate
     * through.  This method is invoked by 
     * {@code onFetchingPhaseBeforeBody()}.  Subclasses may override this
     * function in order to fetch file records from a particular source; there
     * is no need to delegate back to the superclass.  This function may return
     * an empty collection if there are no records but must not return null.
     */
    protected Collection<? extends SampleDataFile> fetchFiles() 
            throws JspException {
        
        // Verify that we have the contexts we need.
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }
        if (this.userContext == null) {
            throw new IllegalStateException();
        }

        // Exit early if the SampleContext is empty.
        if (this.sampleContext.getSampleInfo() == null) {
            return Collections.<SampleDataFile> emptyList();
        }

        // Check the cache and exit early if the object we need is there.
        RepositoryFiles rf = RequestCache.getRepositoryFiles(
                super.pageContext.getRequest(),
                this.sampleContext.getSampleInfo().id,
                this.sampleContext.getSampleInfo().historyId);
        if (rf != null) {
            if (rf.getRecords().isEmpty()) {
                // Set an error flag if there are no files
                this.setErrorFlag(NO_FILES_IN_REPOSITORY);
            }
            return rf.getRecords();
        }

        // Cache miss; fetch a RepositoryFiles object from RepositoryManager.
        CoreConnector coreConnector = CoreConnector.extract(
                super.pageContext.getServletContext());
        try {
            RepositoryManagerRemote repositoryManager
                    = coreConnector.getRepositoryManager();
            boolean canIgnoreFileSizeLimit
                    = AuthorizationCheckerBL.canRequestUnavailableFiles(
                            this.userContext.getUserInfo(),
                            this.sampleContext.getSampleInfo(),
                            super.pageContext.getServletContext());
            boolean shouldIgnoreFileSizeLimit = this.requestUnavailableFiles
                    || ((this.requestUnavailableFilesParamName != null)
                            && (super.pageContext.getRequest().getParameter(
                            this.requestUnavailableFilesParamName) != null));
            
            rf = repositoryManager.getRepositoryFiles(
                    this.sampleContext.getSampleInfo().id,
                    this.sampleContext.getSampleInfo().historyId,
                    shouldIgnoreFileSizeLimit && canIgnoreFileSizeLimit);
            RequestCache.putRepositoryFiles(super.pageContext.getRequest(), rf);
            if (rf.getRecords().isEmpty()) {
                // Set an error flag if there are no files
                this.setErrorFlag(NO_FILES_IN_REPOSITORY);
            }
            
            return rf.getRecords();
        } catch (RemoteException ex) {
            coreConnector.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (RepositoryDirectoryNotFoundException ex) {
            if (ex.doesSuggestedDirectoryExist()
                    && !ex.doesHoldingRecordExist()) {
                this.setErrorFlag(DIRECTORY_BUT_NO_HOLDINGS);
            } else if (!ex.doesSuggestedDirectoryExist()
                    && ex.doesHoldingRecordExist()) {
                this.setErrorFlag(HOLDINGS_BUT_NO_DIRECTORY);
            } else {
                this.setErrorFlag(NO_DIRECTORY_NO_HOLDINGS);
            }
            this.dirNotFoundException = ex;
            // this exception is more of an indicator than an exception and
            // does not need to be thrown but instead stored to be available
            // upon request
            return Collections.<SampleDataFile>emptyList();
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }
    }
    
    /**
     * An inner IteratorFilter class that implements the file filtering behavior
     * of {@code FileIterator}
     * 
     * @author jobollin
     * @version 0.9.0
     * @param <T> the type of {@code SampleDataFile} provided by this iterator
     */
    private class FileIteratorFilter<T extends SampleDataFile>
            extends IteratorFilter<T> {
        
        /**
         * Initializes a {@code FileIteratorFilter} that will filter the
         * {@code SampleDataFile}s returned by the specified Iterator
         * 
         * @param iterator
         */
        public FileIteratorFilter(Iterator<T> iterator) {
            super(iterator);
        }

        /**
         * {@inheritDoc}
         * 
         * @see org.recipnet.common.IteratorFilter#shouldPass(Object)
         */
        @Override
        public boolean shouldPass(T file) {
            return ((FileIterator.this.filterByExtension == null)
                    || file.getName().endsWith(
                            "." + FileIterator.this.filterByExtension));
        }
    }
}
