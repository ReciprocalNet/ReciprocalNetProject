/*
 * Reciprocal Net Project
 * 
 * IdentifyHandler.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.rmi.RemoteException;
import java.util.Date;

import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A {@code VerbHandler} implementation for the OAI-PMH {@code Identify}
 * verb, which requests information about the repository.  Identify requests
 * should have no arguments.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class IdentifyHandler extends VerbHandler {

    /**
     * The fixed admin e-mail address configured for this handler, if any,
     * or {@code null} for on-the-fly generation of admin addresses
     */
    private final String adminEmail;

    /**
     * A utility object for parsing and formatting OAI-PMH protocol date
     * strings
     */
    private final DateUtility dateUtility;
    
    /**
     * Initializes a {@code IdentifyHandler} with the specified admin e-mail
     * address (other repository characteristics are hardcoded or determined
     * from the environment at the time of service)
     * 
     * @param  adminEmail the e-mail address of the repository
     *         administrator, as a {@code String}.  {@code null} is
     *         acceptable; it will cause a suitable root@localhost type
     *         address to be invented in later response to service requests
     * @param  dateUtility an {@code DateUtility} with which this handler
     *         should format dates
     * @param  connector a {@code CoreConnector} with which to retrieve
     *         information from the Reciprocal Net core
     */
    public IdentifyHandler(String adminEmail, DateUtility dateUtility,
            CoreConnector connector) {
        super("Identify", connector);
        if ((adminEmail != null) && (adminEmail.trim().length() == 0)) {
            // The string was blank, so just change it to null here for
            // convenience later.
            this.adminEmail = null;
        } else {
            this.adminEmail = adminEmail;
        }
        this.dateUtility = dateUtility;
    }

    /**
     * Handles an OAI-PMH Identify request by producing an appropriate
     * response on the specified XmlWriter
     * 
     * @param request an {@code PmhRequest} representing the request to be
     *            handled
     * @param writer an {@code XmlWriter} to which to direct the response
     * @throws RemoteException if one is encountered while gathering the
     *             information required to produce the response
     * @throws OperationFailedException if one is encountered while
     *             gathering the information required to produce the
     *             response
     */
    @Override
    public void handleRequest(PmhRequest request, XmlWriter writer)
            throws RemoteException, OperationFailedException {

        /*
         * argument validation assumed to have already been performed via
         * validateRequest(PmhRequest)
         */
        
        // Invent an administrator's e-mail address if one wasn't specified
        // in a config parameter, otherwise use the configured address
        String localAdminEmail = (adminEmail == null)
                    ? ("root@" + request.getServerName())
                    : adminEmail;
                    
        CoreConnector connector = getCoreConnector();
        
        // Either of the calls below might throw an exception
        SiteInfo localSiteInfo =
                connector.getSiteManager().getLocalSiteInfo();
        Date earliestDate =
                connector.getSampleManager().getEarliestAuthoritativeHistoryDate();
        if (earliestDate == null) {
            // No samples exist yet, so just substitute the current date.
            earliestDate = new Date();
        }

        // Generate the response. The strings hard-coded here conform to the
        // OAI spec.
        writer.openElement("Identify");

        writer.openElement("repositoryName", -1);
        writer.addText(localSiteInfo.name);
        writer.closeElement();

        writer.openElement("baseURL", -1);
        writer.addText(request.getRequestUrl());
        writer.closeElement();

        writer.openElement("protocolVersion", -1);
        writer.addText("2.0");
        writer.closeElement();

        writer.openElement("adminEmail", -1);
        writer.addText(localAdminEmail);
        writer.closeElement();

        writer.openElement("earliestDatestamp", -1);
        writer.addText(dateUtility.formatDate(earliestDate));
        writer.closeElement();

        writer.openElement("deletedRecord", -1);
        writer.addText("persistent");
        writer.closeElement();

        writer.openElement("granularity", -1);
        writer.addText("YYYY-MM-DDThh:mm:ssZ");
        writer.closeElement();

        writer.closeElement("Identify");
    }
}