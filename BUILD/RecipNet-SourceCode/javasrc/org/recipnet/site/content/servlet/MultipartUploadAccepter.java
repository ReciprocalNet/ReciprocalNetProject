/*
 * Reciprocal Net Project
 * 
 * MultipartUploadAccepter.java
 *
 * 04-Aug-2005: midurbin wrote first draft
 * 21-Oct-2005: midurbin added support for file descriptions
 * 11-Nov-2005: midurbin added filename and description validation
 * 16-May-2006: jobollin updated this class for consistency with
 *              MultipartMimeFormParser
 * 08-Jan-2008: ekoperda fixed bug #1866 in doPost() by improving error 
 *              handling throughout
 * 09-Jan-2008: ekoperda fixed bug #1879 in doPost()
 */

package org.recipnet.site.content.servlet;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.recipnet.common.MultipartMimeFormParser;
import org.recipnet.common.Validator;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.validation.ContainerStringValidator;
import org.recipnet.site.shared.validation.FilenameValidator;
import org.recipnet.site.wrapper.OperationPersister;
import org.recipnet.site.wrapper.UploaderOperation;

/**
 * <p>
 * A servlet that accepts a "multipart/form-data" POST containing known fields
 * in order to upload one or more files.
 * </p><p>
 * Valid posts should contain the following fields in the following order:
 * <ol>
 *   <li>
 *     An integer representing the id value for an
 *     {@code UploaderOperation} that has been registered with the
 *     {@code OperationPersister}.  The name of this field is unimportant
 *     as long as the value is parsible as an id value for an active operation.
 *   </li>
 *   <li>
 *     A field with the name {@code COMMENTS_PARAM_NAME} representing the
 *     comments associated with the upload action.  This field is optional.
 *   </li>
 *   <li>
 *     A filename field whose name begins with the {@code FILE_PREFIX} and
 *     whose value is the suggested remote filename.
 *   </li>
 *   <li>
 *     A description field whose name begins with the
 *     {@code DESCRIPTION_PREFIX} and whose value is a description of the
 *     file or its significance.
 *   </li>
 *   <li>
 *     A file field whose name begins with the {@code FILE_PREFIX}.  This
 *     file will be given the name indicated by the previous filename.
 *   </li>
 * </ol>
 * Note: fields 3 and 4 may be repeated any number of times representing
 *       multiple files for upload.
 * </p>
 */
@SuppressWarnings("serial")
public class MultipartUploadAccepter extends HttpServlet {   
    /** Validates uploaded file names. */
    private static final Validator filenameValidator
            = new FilenameValidator();

    /** Validates uploaded file descriptions. */
    private static final Validator descriptionValidator
            = new ContainerStringValidator();

    /** The expected name for the comments field. */
    public final static String COMMENTS_PARAM_NAME = "comments";

    /** A prefix identifying filename and file fields. */
    public final static String FILE_NAME_PREFIX = "filename";

    /** A prefix identifying filename and file fields. */
    public final static String FILE_PREFIX = "file";

    /** A prefix identifying description fields. */
    public final static String DESCRIPTION_PREFIX = "description";

    /**
     * A reference to the {@code OperationPersister} object that lives in
     * application scope.  Set by {@code init()}.
     */
    private OperationPersister operationPersister;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
    	super.init();
    	ServletContext context = getServletConfig().getServletContext();
    	this.operationPersister = OperationPersister.extract(context);
    }

    /**
     * {@inheritDoc}. This version processes the POSTed data containing one or
     * more files as well as other recognized fields, then redirects the 
     * browser based on whether there were any held files and on the persisted
     * operations's configuration.
     */
    @Override
    public void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException, 
            ServletException {
        if (!MultipartMimeFormParser.isParsable(request)) {
	    this.handleErrorBadRequest(null, response);
	    return;
        }
        MultipartMimeFormParser parser = new MultipartMimeFormParser(request,
                Charset.forName("UTF-8"));

        /*
         * get the persisted operation so that we can defer to some JSP's prior
         * authorization check
         */
        UploaderOperation op = null;      
        try {
            parser.moveNextFormField();
            op = (UploaderOperation) this.operationPersister.getOperation(
                    Integer.parseInt(parser.getCurrentFieldValue()));
        } catch (NumberFormatException nfe) {
	    this.handleErrorBadRequest(null, response);
            return;
        } catch (ResourceNotFoundException rnfe) {
	    this.handleErrorForbidden(null, response);
            return;
        }

	try {
	    /*
	     * Iterate through the multipart MIME form, parsing each field in
	     * turn.
	     */
	    boolean firstFileParsed = false;
	    String filename = null;
	    String description = null;
	    while (parser.moveNextFormField()) {
		String parameterName = parser.getCurrentFieldName();
            
		if (parameterName.equals(COMMENTS_PARAM_NAME)) {
		    if (!firstFileParsed && op.getComments() == null) {
			op.setComments(blankToNull(
		                parser.getCurrentFieldValue()));
		    } else {
			// Comments must appear only once before the file or
			// not at all.
			this.handleErrorBadRequest(op, response);
			return;
		    }
		} else if (parameterName.startsWith(DESCRIPTION_PREFIX)) {
		    description = blankToNull(parser.getCurrentFieldValue());
		    if (description != null
                            && !descriptionValidator.isValid(description)) {
			this.handleErrorPretty("invalidDescription",
			        description, op, request, response);
			return;
		    }
                } else if (parameterName.startsWith(FILE_NAME_PREFIX)) {
		    // This is a filename for the next file; store it.
		    if (filename == null) {
			filename = blankToNull(parser.getCurrentFieldValue());

			if (filename != null
                                && !filenameValidator.isValid(filename)) {
			    // invalid filename, cancel the operation and
			    // forward to the error page
			    this.handleErrorPretty("invalidFilename", filename,
			            op, request, response);
			    return;
			}
			// Accept a null filename for right now.  A null
			// filename POSTed from the web form is acceptable so
			// long as there is no file data associated with it.
		    } else {
			// two filenames without a file
			this.handleErrorBadRequest(op, response);
			return;
		    }
		} else if (parameterName.startsWith(FILE_PREFIX)) {
		    // This is a file field.
		    String originalFileName = parser.getFileName();
		    if ((originalFileName != null)
                            && (originalFileName.length() > 0)) {
			// a file was selected
			firstFileParsed = true;

			// Verify that a server-side filename was parsed
			// previously.
			if (filename == null) {
			    this.handleErrorPretty("invalidFilename", filename,
			            op, request, response);
			    return;
			}

			// Handle the uploaded file.
			try {
			    op.acceptUpload(parser, filename, description);
			    filename = null;
			    description = null;
			} catch (IllegalArgumentException ex) {
			    // Duplicated file name.
			    this.handleErrorPretty("duplicateFilename", 
                                    filename, op, request, response);
			    return;
			} catch (InvalidDataException ex) {
			    // Invalid file name.
			    this.handleErrorPretty("invalidFilename", filename,
			            op, request, response);
			    return;
			}
		    }
                }
            }
        
	    /*
	     * Begin post-processing of the upload.
	     */
	    op.startManagingHeldFiles();
	    if (op.isAnyFileHeld()) {
		// redirect to the 'filenameConflictsPageUrl'
		response.sendRedirect(request.getContextPath()
                        + getInitParameter("filenameConflictsPageUrl")
                        + "?sampleId=" + op.getSampleInfo().id
                        + "&sampleHistoryId="
                        + op.getSampleInfo().historyId
                        + "&persistedOpId=" + op.getId());
		return;
	    } else {
		op.commitFiles();
		operationPersister.closeOperation(op.getId());
		if (op.getDisplayConfirmationPage()) {
		    // forward to the 'uploadConfirmationPageUrl'
		    RequestDispatcher rd
                            = getServletContext().getRequestDispatcher(
                            getInitParameter("uploadConfirmationPageUrl"));
		    request.setAttribute("persistedOp", op);
		    rd.forward(request, response);
		    return;
		} else {
		    // redirect to the 'fileManagementPageUrl'
		    response.sendRedirect(request.getContextPath()
                            + getInitParameter("fileManagementPageUrl")
                            + "?sampleId=" + op.getSampleInfo().id);
		}
	    }
	} catch (IOException ex) {
	    this.operationPersister.closeOperation(op.getId());
	    throw ex;
	} catch (ServletException ex) {
	    this.operationPersister.closeOperation(op.getId());
	    throw ex;
	} catch (Exception ex) {
	    // This catch-all is necessary to ensure that the persisted
	    // operation is closed no matter what error may occur.
	    this.operationPersister.closeOperation(op.getId());
	    throw new ServletException(ex);
	}
    }

    private void handleErrorBadRequest(UploaderOperation op,
            HttpServletResponse response) throws IOException, 
            ServletException {
	if (op != null) {
	    this.operationPersister.closeOperation(op.getId());
	}
	response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    private void handleErrorForbidden(UploaderOperation op,
            HttpServletResponse response) throws IOException, 
            ServletException {
	if (op != null) {
	    this.operationPersister.closeOperation(op.getId());
	}
	response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    private void handleErrorPretty(String reason, String data, 
            UploaderOperation op, HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
	if (op != null) {
	    this.operationPersister.closeOperation(op.getId());
	}
        RequestDispatcher rd = super.getServletContext().getRequestDispatcher(
                super.getInitParameter("invalidFilePageUrl"));
        request.setAttribute("sampleId",
                 String.valueOf(op.getSampleInfo().id));
	request.setAttribute(reason, data != null ? data : "");
	rd.forward(request, response);
    }

    /**
     * A simple helper method that returns the value passed to it unless it was
     * a blank or empty string, in which case it returns {@code null} instead.
     * 
     * @param value the {@code String} to test
     * @return {@code null} if {@code value} is {@code null} or contains no
     *         non-whitespace characters, otherwise {@code value}
     */
    private static String blankToNull(String value) {
        return (value == null || value.trim().length() == 0) ? null : value;
    }
}
