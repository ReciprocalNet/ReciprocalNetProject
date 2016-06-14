/*
 * Reciprocal Net project
 * 
 * LabSelector.java
 * 
 * 02-Jul-2004: cwestnea wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseAfterBody()
 * 26-Jul-2005: midurbin removed calls to obsolete listbox methods
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;

/**
 * This tag provides a list of all the current labs in existence represented as
 * a {@code ListboxHtmlControl}.
 */
public class LabSelector extends ListboxHtmlControl {

    /**
     * {@inheritDoc}; this version retrieves the appropriate {@code LabInfo}
     * objects from the core and adds them to the list of options.
     * 
     * @throws JspException if an exception is encountered during this method.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        // Get reference to SiteManager
        SiteManagerRemote siteManager;
        CoreConnector coreConnector = CoreConnector.extract(pageContext.getServletContext());

        try {
            siteManager = coreConnector.getSiteManager();
        } catch (RemoteException ex) {
            throw new JspException(ex);
        }

        try {
            for (LabInfo lab : siteManager.getAllLabInfo()) {
                RequestCache.putLabInfo(pageContext.getRequest(), lab);

                // add this lab to the listbox
                addOption(true, lab.name, String.valueOf(lab.id));
            }
        } catch (RemoteException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        }

        return super.onFetchingPhaseAfterBody();
    }
}
