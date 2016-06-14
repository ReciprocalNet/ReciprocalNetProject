/*
 * Reciprocal Net project
 * 
 * FileField.java
 * 
 * 29-Jun-2004: midurbin wrote the first draft
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 10-Mar-2005: midurbin fixed bug #1642 in onRenderingPhaseAfterBody()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 04-Apr-2005: ekoperda added a 'label' property and the field code 'FILENAME'
 * 02-May-2005: ekoperda made adjustments to accommodate spec changes in
 *              RepositoryFiles and FileContext
 * 04-Aug-2005: midurbin added FILE_SIZE_EXACT, AGGREGATE_SIZE
 * 21-Oct-2005: midurbin added DESCRIPTION
 * 13-Jun-2006: jobollin reformatted the source
 * 23-Jun-2006: jobollin updated onRenderingPhaseBeforeBody() to properly
 *              escape file URLs and file names when generating text for a field
 *              of type LINKED_FILENAME or FILENAME
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Map;
import java.text.DecimalFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A Tag that exposes one aspect of the file identified by the most immediate
 * {@code FileContext}. This tag must be nested inside a {@code FileContext},
 * a {@code UserContext}, and a {@code SampleContext}.
 */
public class FileField extends HtmlPageElement implements ErrorSupplier {

    private final DecimalFormat decimalFormat = new DecimalFormat(
            "###,###,###,###");

    /** Error flags to implement {@code ErrorSupplier}. */
    public static final int FILE_AVAILABLE_UPON_REQUEST = 1 << 0;

    public static final int FILE_UNAVAILABLE = 1 << 1;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return FILE_UNAVAILABLE;
    }

    /** A field code constant to display a linked filename. */
    public static final int LINKED_FILENAME = 0;

    /** A field code constant to display the file size (possibly rounded). */
    public static final int FILE_SIZE = 1;

    /** A field code constant to display the file name. */
    public static final int FILENAME = 2;

    /** A field code constant to display the file size in bytes. */
    public static final int FILE_SIZE_EXACT = 3;

    /**
     * A field code constant to display the amount of space on the disk the used
     * by the CVS repository for the given file. This should only be used to
     * display the amount of space that would be freed if a file were eradicated
     * and requires a slow RMI call each time it is used.
     */
    public static final int AGGREGATE_SIZE = 4;

    /**
     * A field code constant to display the file description. If there is no
     * description for the file, this tag silently outputs nothing.
     */
    public static final int DESCRIPTION = 5;

    /**
     * Must be set to one of the constants defined by this class to indicate
     * which aspect of the current file this {@code FileField} displays. This is
     * an optional property that defaults to {@code LINKED_FILENAME}.
     */
    private int fieldCode;

    /**
     * Optional attribute that has an effect only when the {@code fieldCode} is
     * {@code LINKED_FILENAME}. Ordinarily, in {@code LINKED_FILENAME} mode,
     * the anchor text portion of the hyperlink rendered by this control is the
     * file nameL. When this property has been set to a non-null value, however,
     * this property's value is used as the anchor text instead.
     */
    private String label;

    /**
     * A reference to the most immediate {@code FileContext}, set during the
     * {@code REGISTRATION_PHASE} and used to get the current
     * {@code SampleDataFile} object during and after the {@code FETCHING_PHASE}.
     */
    private FileContext fileContext;

    /**
     * The most immediate {@code UserContext}, initialized to null by
     * {@code reset()} and set during the {@code REGISTRATION_PHASE}. The user
     * is considered to be the currently logged in user, and may be needed to
     * determine file accessibility and ticketing.
     */
    private UserContext userContext;

    /**
     * The most immediate {@code SampleContext}, initialized to null by
     * {@code reset()} and set during the {@code REGISTRATION_PHASE}. This
     * sample is the sample whose file is being exposed by this
     * {@code FileField} and is needed in the event that an out-of-band read
     * ticket is requested.
     */
    private SampleContext sampleContext;

    /** Keeps track of error codes, to implement {@code ErrorSupplier}. */
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = FileField.LINKED_FILENAME;
        this.label = null;
        this.fileContext = null;
        this.userContext = null;
        this.sampleContext = null;
        this.errorCode = NO_ERROR_REPORTED;
    }

    /**
     * @param fieldCode one of the static field codes defined by this class;
     *        used to indicate which detail about the current file will be
     *        exposed by this Tag.
     * @throws IllegalArgumentException if an invalid fieldCode is provided
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case LINKED_FILENAME:
            case FILE_SIZE:
            case FILENAME:
            case FILE_SIZE_EXACT:
            case AGGREGATE_SIZE:
            case DESCRIPTION:
                this.fieldCode = fieldCode;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return one of the static field codes defined by this class; used to
     *         indicate which detail about the current file will be exposed by
     *         this Tag.
     */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /** Simple setter. */
    public void setLabel(String label) {
        this.label = label;
    }

    /** Simple getter. */
    public String getLabel() {
        return this.label;
    }

    /** Implements {@code ErrorSupplier}. */
    public int getErrorCode() {
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the innermost
     * surrounding {@code FileContext}, {@code UserContext}, and
     * {@code SampleContext}
     * 
     * @throws IllegalStateException if there is no {@code FileContext}, or no
     *         {@code UserContext}, or no {@code SampleContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get FileContext
        this.fileContext = findRealAncestorWithClass(this, FileContext.class);
        if (this.fileContext == null) {
            throw new IllegalStateException();
        }

        // get UserContext
        this.userContext = findRealAncestorWithClass(this, UserContext.class);
        if (this.userContext == null) {
            throw new IllegalStateException();
        }

        // get SampleContext
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version writes an HTML representation of the data
     * for the current file that corresponds to the {@code fieldCode}.
     * 
     * @throws JspException wrapping any exceptions thrown by core
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        SampleDataFile file = this.fileContext.getSampleDataFile();
        
        if (file == null) {
            // There is no file. Exit early without displaying anything.
            return super.onRenderingPhaseAfterBody(out);
        }
        switch (this.fieldCode) {
            case FileField.LINKED_FILENAME:
                if (file.getUrl() != null) {
                    // Display a hyperlink tag to the file.
                    out.print("<a href=\""
                            + HtmlControl.escapeAttributeValue(
                                    generateCompleteUrlForFile(file)) + "\">"
                            + HtmlControl.escapeNestedValue(
                                    this.label != null ? label : file.getName())
                            + "</a>");
                } else {
                    // We won't be able to display a hyperlink to this file
                    // because apparently it's not web-accessible.
                    setFlagsForInaccessibleFile(file);
                    out.print(label != null ? label : file.getName());
                }
                break;
            case FileField.FILE_SIZE:
                out.print(decimalFormat.format(
                        Math.ceil((double) file.getSize() / 1024))
                        + " KB");
                break;
            case FileField.FILE_SIZE_EXACT:
                out.print(decimalFormat.format(file.getSize()) + " bytes");
                break;
            case FileField.FILENAME:
                out.print(HtmlControl.escapeNestedValue(file.getName()));
                break;
            case FileField.AGGREGATE_SIZE:
                CoreConnector cc = CoreConnector.extract(
                        this.pageContext.getServletContext());
                
                try {
                    out.print(decimalFormat.format(
                            cc.getRepositoryManager().getDataFileAggregateSize(
                                    sampleContext.getSampleInfo().id,
                                    file.getName()))
                            + " bytes");
                } catch (InconsistentDbException ex) {
                    throw new JspException(ex);
                } catch (OperationFailedException ex) {
                    throw new JspException(ex);
                }
                break;
            case FileField.DESCRIPTION:
                if (file.getDescription() != null) {
                    out.print(HtmlControl.escapeNestedValue(
                            file.getDescription()));
                }
                break;
        }
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * Internal function that returns a string containing the complete URL that
     * web surfers may use to access the specified {@code SampleDataFile}. In
     * some special cases this string may be subtly different from the one
     * returned by {@code SampleDataFile.getUrl()}. It may include additional
     * adornments including a repository manager ticket number, for instance.
     */
    private String generateCompleteUrlForFile(SampleDataFile file)
            throws JspException {
        if (file instanceof RepositoryFiles.Record) {
            try {
                // Special handling for files that are being served from
                // RepositoryManager. We may need to create a ticket first.
                int ticket = obtainRepositoryManagerTicket(
                        (RepositoryFiles.Record) file);
                return file.getUrl() + "?ticket=" + ticket;
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            } catch (RemoteException ex) {
                throw new JspException(ex);
            }
        } else {
            // No special handling required.
            return file.getUrl();
        }
    }

    /**
     * Internal function that interfaces with {@code RepositoryManager} to
     * obtain a ticket number that will be authorized for out-of-band access to
     * the specified repository data file. It is assumed that the caller would
     * have acquired {@code rec} during a previous call to
     * {@code RepositoryManager.getRepositoryFiles()}. The current
     * implementation caches tickets within the scope of a request for
     * efficiency.
     * 
     * @return a ticket number
     * @throws OperationFailedException on low-level error in core.
     * @throws RemoteException on RMI error.
     */
    private int obtainRepositoryManagerTicket(RepositoryFiles.Record rec)
            throws OperationFailedException, RemoteException {
        int ticket = 0;
        CoreConnector coreConnector
                = CoreConnector.extract(super.pageContext.getServletContext());
        RepositoryManagerRemote repositoryManager
                = coreConnector.getRepositoryManager();
        
        if (RequestCache.getTicket(super.pageContext.getRequest(),
                rec.getRepositoryFiles()) != null) {
            // cache hit
            ticket = RequestCache.getTicket(super.pageContext.getRequest(),
                    rec.getRepositoryFiles()).intValue();
        } else {
            // cache miss
            ticket = repositoryManager.grantNewTicket(
                    rec.getRepositoryFiles(),
                    (this.userContext.getUserInfo() != null
                            ? this.userContext.getUserInfo().id
                            : UserInfo.INVALID_USER_ID));
            RequestCache.putTicket(super.pageContext.getRequest(),
                    rec.getRepositoryFiles(), ticket);
        }
        return ticket;
    }

    /**
     * Internal function that sets various error flags on this object in the
     * event that the specified data file is not web-accessible but the JSP
     * author had requested that we display a hyperlink. The specific error
     * flags that get set may depend upon the type of {@code file} and its
     * origins.
     */
    private void setFlagsForInaccessibleFile(SampleDataFile file) {
        boolean hasFlagBeenSet = false;

        if (file instanceof RepositoryFiles.Record) {
            RepositoryFiles.Record rec = (RepositoryFiles.Record) file;
            
            if ((rec.getAvailability() == RepositoryFiles.Availability.ON_REQUEST)
                    && AuthorizationCheckerBL.canRequestUnavailableFiles(
                            this.userContext.getUserInfo(),
                            this.sampleContext.getSampleInfo(),
                            super.pageContext.getServletContext())) {
                setErrorFlag(FILE_AVAILABLE_UPON_REQUEST);
                hasFlagBeenSet = true;
            }
        }

        if (!hasFlagBeenSet) {
            setErrorFlag(FILE_UNAVAILABLE);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        FileField dc = (FileField) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        dc.userContext = (UserContext) map.get(this.userContext);
        dc.fileContext = (FileContext) map.get(this.fileContext);
        
        return dc;
    }
}
