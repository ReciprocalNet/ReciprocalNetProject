/*
 * Reciprocal Net Project
 * 
 * ListSetsHandler.java
 * 
 * 25-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.rmi.RemoteException;
import java.util.Map;

import org.recipnet.common.XmlWriter;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A VerbHandler that handles the "ListSets" verb by returning a list of all
 * the sets currently available in the repository.  The requests
 * handled by this handler should not have any arguments.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ListSetsHandler extends VerbHandler {
    
    /**
     * An {@code SetContext} for this handler's use
     */
    private final SetContext setContext;
    
    /**
     * Initializes a {@code ListSetsHandler} with default configuration
     * 
     * @param  connector a {@code CoreConnector} with which to retrieve
     *         information from the Reciprocal Net core
     * @param  setContext an {@code SetContext} for this handler's use in
     *         fulfilling requests
     */
    public ListSetsHandler(CoreConnector connector,
            SetContext setContext) {
        super("ListSets", connector);
        this.setContext = setContext;
    }

    /**
     * {@inheritDoc}
     * 
     * @see VerbHandler#handleRequest(PmhRequest, XmlWriter)
     */
    @Override
    public void handleRequest(@SuppressWarnings("unused") PmhRequest request,
            XmlWriter writer)
            throws OperationFailedException, RemoteException {
        
        /*
         *  argument validation assumed to have already been performed via
         *  validateArguments(RequestContext)
         */
        
        // Generate the response.  The strings hard-coded here conform to the
        // OAI spec.
        writer.openElement("ListSets");
        for (Map.Entry<String, String> entry
                : setContext.getSets().entrySet()) {
            writer.openElement("set");

            writer.openElement("setSpec", -1);
            writer.addText(entry.getKey());
            writer.closeElement();
            
            writer.openElement("setName", -1);
            writer.addText(entry.getValue());
            writer.closeElement();
            
            writer.closeElement();
        }
        writer.closeElement();
    }
}