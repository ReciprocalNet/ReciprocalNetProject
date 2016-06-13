/*
 * Reciprocal Net Project
 *
 * OrtepInstructionGenerator.java
 *
 * 23-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.OrtFile;
import org.recipnet.common.files.SdtFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.CifParseException;
import org.recipnet.common.files.cif.CifParser;
import org.recipnet.common.molecule.Atom;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.ResourceException;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.OrtepGenerationLogEvent;
import org.recipnet.site.shared.logevent.OrtepGenerationLogEvent.EventType;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.OrtepInputMaker;
import org.recipnet.site.wrapper.RepositoryFileInputStream;
import org.recipnet.site.wrapper.RequestParameters;

/**
 * <p>
 * {@code OrtepInstructionGenerator} is a servlet class that creates ORTEP
 * instruction stub files based on existing files in the file repository, and
 * serves these to the client as a download. The class uses the same mechanism
 * to generate instructions as does {@link JammSupport}. It is never necessary
 * to store the result in the file repository because JammSupport creates the
 * same thing on the fly, but the user may want to <em>modify</em> the
 * resulting instructions and upload them, in which case JammSupport will use
 * the revised version (provided that its name properly corresponds to the CRT
 * file for which an ORTEP is generated).  The servlet recognizes these request
 * parameters:
 * </p>
 * <dl>
 * <dt>sampleId</dt>
 * <dd>The Reciprocal Net sample ID of the sample to which the request
 * pertains</dd>
 * <dt>sampleHistoryId</dt>
 * <dd>The Reciprocal Net sample history ID of the version of the sample to
 * which the request pertains (optional)</dd>
 * <dt>crtName</dt>
 * <dd>The name of the repository data file containing the CRT-format model
 * for which ORTEP instructions are requested</dd>
 * <dt>sdtName</dt>
 * <dd>The name of the repository data file containing the SDT-format
 * crystallographic data from which ORTEP instructions are requested; this
 * parameter should be omitted if 'cifName' is specified and it is desired that
 * the crystallographic data be drawn from the CIF</dd>
 * <dt>cifName</dt>
 * <dd>The name of the repository data file containing the CIF-format
 * crystallographic data from which ORTEP instructions are requested; this
 * parameter is ignored if 'sdtName' is specified</dd>
 * </dl>
 * 
 * @author jobollin
 * @version 1.0
 */
public class OrtepInstructionGenerator extends HttpServlet {

    /**
     * The serialization version of this class
     */
    private static final long serialVersionUID = 1L;

    private CoreConnector coreConnector;

    /**
     * Performs one-time initialization of this servlet instance
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() {
        coreConnector
                = CoreConnector.extract(getServletConfig().getServletContext());
    }

    /**
     * Handles an HTTP GET request by passing it to
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)}.
     * 
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles an HTTP POST request by passing it to
     * {@link #processRequest(HttpServletRequest, HttpServletResponse)}.
     * 
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Performs request processing common to GET and POST requests
     * 
     * @param request an {@code HttpServletRequest} representing the client HTTP
     *        request for which a response should be generated
     * @param response an {@code HttpServletResponse} representing the response
     *        to the client
     * @throws ServletException
     * @throws IOException
     */
    private void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        RequestParameters parameters = new RequestParameters(
                request.getParameterMap());
        int sampleId = parameters.getParameter("sampleId",
                SampleInfo.INVALID_SAMPLE_ID);
        String crtName = parameters.getParameter("crtName");
        boolean useSdt = parameters.containsParameter("sdtName");

        if (sampleId == SampleInfo.INVALID_SAMPLE_ID) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request: no (or invalid) sample ID specified");
            return;
        } else if (crtName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request: no sample CRT name specified");
        } else if (!useSdt && !parameters.containsParameter("cifName")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid request: No CIF or SDT name specified");
            return;
        } else {
            int sampleHistoryId = parameters.getParameter("sampleHistoryId",
                    SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
            UserInfo user = (UserInfo) request.getSession().getAttribute(
                    "userInfo");
            CrtFile<? extends Atom> crtFile;

            try {
                SampleManagerRemote sampleManager
                        = coreConnector.getSampleManager();
                RepositoryManagerRemote repositoryManager
                        = coreConnector.getRepositoryManager();
                SampleInfo sample = sampleManager.getSampleInfo(sampleId,
                        sampleHistoryId);
                Reader crtReader;
                int ticket;

                if (!AuthorizationCheckerBL.canSeeSample(user, sample)) {
                    coreConnector.getSiteManager().recordLogEvent(
                            new OrtepGenerationLogEvent(
                                    EventType.UNAUTHORIZED_REQUEST, crtName,
                                    parameters.getParameter("sdtName"),
                                    parameters.getParameter("cifName"),
                                    sampleId, sampleHistoryId, getUserId(user),
                                    null));
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                try {
                    ticket = repositoryManager.beginReadingDataFile(sampleId,
                            sampleHistoryId, crtName, user.id);
                } catch (ResourceException re) {
                    coreConnector.getSiteManager().recordLogEvent(
                            new OrtepGenerationLogEvent(
                                    EventType.UNAUTHORIZED_REQUEST, crtName,
                                    parameters.getParameter("sdtName"),
                                    parameters.getParameter("cifName"),
                                    sampleId, sampleHistoryId, getUserId(user),
                                    re));
                    throw new ServletException(re);
                }
                try {
                    crtReader = new InputStreamReader(
                            new RepositoryFileInputStream(
                                    coreConnector, ticket),
                            "UTF-8");
                } catch (IOException ioe) {
                    // Exception while creating the Reader
                    try {
                        repositoryManager.abortDataFile(ticket);
                    } catch (RecipnetException re) {
                        // do nothing
                    } catch (RemoteException re) {
                        // do nothing
                    }

                    throw ioe;
                }
                try {
                    crtFile = CrtFile.readFrom(crtReader);
                } catch (IOException ioe) {
                    coreConnector.getSiteManager().recordLogEvent(
                            new OrtepGenerationLogEvent(
                                    EventType.CRT_IO_EXCEPTION, crtName,
                                    parameters.getParameter("sdtName"),
                                    parameters.getParameter("cifName"),
                                    sampleId, sampleHistoryId, getUserId(user),
                                    ioe));
                    throw new ServletException(ioe);
                } catch (ParseException pe) {
                    coreConnector.getSiteManager().recordLogEvent(
                            new OrtepGenerationLogEvent(
                                    EventType.CRT_PARSE_EXCEPTION, crtName,
                                    parameters.getParameter("sdtName"),
                                    parameters.getParameter("cifName"),
                                    sampleId, sampleHistoryId, getUserId(user),
                                    pe));
                    throw new ServletException(pe);
                } finally {
                    try {
                        crtReader.close();
                    } catch (IOException ioe) {
                        // do nothing
                    }
                }
                if (useSdt) {
                    generateInstructionsFromSdt(sample, user, crtFile,
                            parameters, response);
                } else {
                    generateInstructionsFromCif(sample, user, crtFile,
                            parameters, response);
                }
            } catch (RecipnetException re) {
                throw new ServletException(re);
            } catch (RemoteException re) {
                coreConnector.reportRemoteException(re);
                throw new ServletException(re);
            }
        }
    }

    /**
     * Generates an ORTEP instruction stub for the specified sample / sample
     * version, on behalf of the specified user, according to the specified
     * parameters. The result is delivered to the user as a download.
     * 
     * @param sample a {@code SampleInfo} representing the sample and sample
     *        version for which ORTEP instructions are requested
     * @param user a {@code UserInfo} representing the user on whose behalf the
     *        ORTEP instructions are requested
     * @param crtFile a {@code CrtFile} representing the model for which the
     *        instructions are requested
     * @param parameters the request parameters; of special note among these is
     *        the "cifName" parameter specifying the name of the CIF on which
     *        the instructions should be based
     * @param response an {@code HttpServletResponse} with which to return the
     *        result
     * @throws IOException in case of an I/O error retrieving data from core or
     *         writing the ORTEP instruction data to the provided response
     * @throws ServletException if there a low-level error is processed
     * @throws RecipnetException if there is a low-level error that is not
     *         otherwise handled
     */
    private void generateInstructionsFromCif(SampleInfo sample, UserInfo user,
            CrtFile<? extends Atom> crtFile, RequestParameters parameters,
            HttpServletResponse response) throws IOException,
            RecipnetException, ServletException {
        RepositoryManagerRemote repositoryManager
                = coreConnector.getRepositoryManager();
        CifFile cifFile;
        Reader cifReader;
        int ticket;

        try {
            ticket = repositoryManager.beginReadingDataFile(sample.id,
                    sample.historyId, parameters.getParameter("cifName"),
                    user.id);
            try {
                cifReader = new InputStreamReader(
                        new RepositoryFileInputStream(coreConnector, ticket),
                        "UTF-8");
            } catch (IOException ioe) {
                // Exception while creating the Reader
                try {
                    repositoryManager.abortDataFile(ticket);
                } catch (RecipnetException re) {
                    // do nothing
                } catch (RemoteException re) {
                    // do nothing
                }

                throw ioe;
            }
            try {
                Iterator<DataBlock> blockIterator;

                cifFile = new CifParser().parseCif(cifReader);
                blockIterator = cifFile.blockIterator();

                if (blockIterator.hasNext()) {
                    OrtepInputMaker maker
                            = OrtepInputMaker.forCIF(blockIterator.next());
                    OrtFile ortFile
                            = maker.createOrtepInstructions(crtFile.getModel());

                    deliverOrtFile(ortFile, response,
                            parameters.getParameter("crtName"));
                } else {
                    coreConnector.getSiteManager().recordLogEvent(
                            new OrtepGenerationLogEvent(EventType.CIF_EMPTY,
                                    null, parameters.getParameter("sdtName"),
                                    parameters.getParameter("cifName"),
                                    sample.id, sample.historyId,
                                    getUserId(user), null));
                    response.sendError(HttpServletResponse.SC_CONFLICT,
                            "The CIF file in the repository contains no data "
                                    + "blocks");
                    return;
                }
            } catch (IOException ioe) {
                coreConnector.getSiteManager().recordLogEvent(
                        new OrtepGenerationLogEvent(EventType.CIF_IO_EXCEPTION,
                                null, parameters.getParameter("sdtName"),
                                parameters.getParameter("cifName"), sample.id,
                                sample.historyId, getUserId(user), ioe));
                throw ioe;
            } catch (CifParseException cpe) {
                coreConnector.getSiteManager().recordLogEvent(
                        new OrtepGenerationLogEvent(
                                EventType.CIF_PARSE_EXCEPTION, null,
                                parameters.getParameter("sdtName"),
                                parameters.getParameter("cifName"), sample.id,
                                sample.historyId, getUserId(user), cpe));
            } finally {
                try {
                    cifReader.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        } catch (ResourceException re) {
            coreConnector.getSiteManager().recordLogEvent(
                    new OrtepGenerationLogEvent(
                            EventType.CIF_RESOURCE_EXCEPTION, null,
                            parameters.getParameter("sdtName"),
                            parameters.getParameter("cifName"), sample.id,
                            sample.historyId, getUserId(user), re));
            throw new ServletException(re);
        }
    }

    /**
     * Generates an ORTEP instruction stub for the specified sample / sample
     * version, on behalf of the specified user, according to the specified
     * parameters. The result is delivered to the user as a download.
     * 
     * @param sample a {@code SampleInfo} representing the sample and sample
     *        version for which ORTEP instructions are requested
     * @param user a {@code UserInfo} representing the user on whose behalf the
     *        ORTEP instructions are requested
     * @param crtFile a {@code CrtFile} representing the model for which the
     *        instructions are requested
     * @param parameters the request parameters; of special note among these is
     *        the "sdtName" parameter specifying the name of the SDT on which
     *        the instructions should be based
     * @param response an {@code HttpServletResponse} with which to return the
     *        result
     * @throws IOException in case of an I/O error retrieving data from core or
     *         writing the ORTEP instruction data to the provided response
     * @throws RecipnetException if there is a low-level error
     * @throws ServletException
     */
    private void generateInstructionsFromSdt(SampleInfo sample, UserInfo user,
            CrtFile<? extends Atom> crtFile, RequestParameters parameters,
            HttpServletResponse response) throws IOException,
            RecipnetException, ServletException {
        RepositoryManagerRemote repositoryManager
                = coreConnector.getRepositoryManager();
        Reader sdtReader;
        int ticket;

        try {
            ticket = repositoryManager.beginReadingDataFile(sample.id,
                    sample.historyId, parameters.getParameter("sdtName"),
                    user.id);
            try {
                sdtReader = new InputStreamReader(
                        new RepositoryFileInputStream(coreConnector, ticket),
                        "UTF-8");
            } catch (IOException ioe) {
                // Exception while creating the Reader
                try {
                    repositoryManager.abortDataFile(ticket);
                } catch (RecipnetException re) {
                    // do nothing
                } catch (RemoteException re) {
                    // do nothing
                }

                throw ioe;
            }
            try {
                OrtepInputMaker maker
                        = OrtepInputMaker.forSDT(SdtFile.readFrom(sdtReader));
                OrtFile ortFile
                        = maker.createOrtepInstructions(crtFile.getModel());

                deliverOrtFile(ortFile, response,
                        parameters.getParameter("crtName"));
            } catch (IOException ioe) {
                coreConnector.getSiteManager().recordLogEvent(
                        new OrtepGenerationLogEvent(EventType.SDT_IO_EXCEPTION,
                                null, parameters.getParameter("sdtName"),
                                parameters.getParameter("cifName"), sample.id,
                                sample.historyId, getUserId(user), ioe));
                throw ioe;
            } catch (ParseException pe) {
                coreConnector.getSiteManager().recordLogEvent(
                        new OrtepGenerationLogEvent(
                                EventType.SDT_PARSE_EXCEPTION, null,
                                parameters.getParameter("sdtName"),
                                parameters.getParameter("cifName"), sample.id,
                                sample.historyId, getUserId(user), pe));
                throw new ServletException(pe);
            } finally {
                try {
                    sdtReader.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        } catch (ResourceException re) {
            coreConnector.getSiteManager().recordLogEvent(
                    new OrtepGenerationLogEvent(
                            EventType.SDT_RESOURCE_EXCEPTION, null,
                            parameters.getParameter("sdtName"),
                            parameters.getParameter("cifName"), sample.id,
                            sample.historyId, getUserId(user), re));
            throw new ServletException(re);
        }
    }

    /**
     * Delivers the ORTEP instructions represented by the specified
     * {@code OrtFile} to a client via the provided response object, specifying
     * that they are to be handled as a file download.
     * 
     * @param ortFile an {@code OrtFile} representing the ORTEP instructions to
     *        return
     * @param response the {@code HttpServletResponse} to which the instruction
     *        data should be directed
     * @param crtName the name of the CRT file for which the instructions were
     *        generated; used to compute a suggested name for the downloaded
     *        file
     * @throws IOException if an I/O error occurs while writing the instructions
     *         to the response
     */
    private void deliverOrtFile(OrtFile ortFile, HttpServletResponse response,
            String crtName) throws IOException {
        Writer out;

        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename="
                + crtName.replaceFirst("\\.crt$", "").concat(".ort"));

        out = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
        ortFile.writeTo(out);
        out.flush();
    }

    /**
     * Returns the id of the specified user, or {@code INVALID_USER_ID} if the
     * user is {@code null}
     * 
     * @param user a {@code UserInfo} representing the user whose ID is
     *        requested
     *        
     * @return the ID of the specified user, or {@code INVALID_USER_ID} if the
     *         user is {@code null}
     */
    private int getUserId(UserInfo user) {
        return ((user == null) ? UserInfo.INVALID_USER_ID : user.id);
    }
}
