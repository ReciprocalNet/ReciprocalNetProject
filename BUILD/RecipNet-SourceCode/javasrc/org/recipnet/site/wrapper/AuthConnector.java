/*
 * Reciprocal Net project
 * @(#)AuthConnector.java
 *
 * 17-Jul-2002: ekoperda wrote first draft
 * 30-May-2003: midurbin added handling for OperationFailedException during
 *              ticket validation.
 * 07-May-2004: cwestnea added port argument to connect()
 */

package org.recipnet.site.wrapper;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.RepositoryManagerRemote;


/**
 * This is the Java half of mod_recipnet_auth, the Apache web server module 
 * that authenticates file requests in the repository.  (The other half of 
 * mod_recipnet_auth is written in C so that it can be an Apache shared 
 * module.)  This class is instantiated by the C program in its own VM and 
 * connects to the Reciprocal Net core via RMI.  Once the connection has been 
 * established, individual HTTP requests into the file repository are
 * authenticated/verified by calling RepositoryManager.verifyTicket() or
 * RepositoryManager.verifyTicketWithTtl().
 */
public class AuthConnector {
    public static final int NO_ERROR = 0;
    public static final int NAME_NOT_BOUND = -1;
    public static final int REGISTRY_NOT_RUNNING = -2;
    public static final int BAD_NAME = -3;
    public static final int REMOTE_EXCEPTION = -4;
    public static final int NOT_CONNECTED = -5;
    public static final int OPERATION_FAILED_EXCEPTION = -6;
    public static final int TICKET_VALID = 1;
    public static final int TICKET_INVALID = 0;

    protected RepositoryManagerRemote repositoryManager;

    public AuthConnector() {
        repositoryManager = null;
    }  

    public int connect(String hostname, String port, 
            String repositoryManagerName) {
        String rmiName = "//" + hostname + ":" + port + "/" 
                + repositoryManagerName;
	try {
            repositoryManager = (RepositoryManagerRemote) 
                                        Naming.lookup(rmiName);
        } catch (NotBoundException ex) {
            return NAME_NOT_BOUND;
	} catch (RemoteException ex) {
	    return REGISTRY_NOT_RUNNING;
	} catch (MalformedURLException ex) {
	    return BAD_NAME;
	}
	return NO_ERROR;
    }

    public boolean isConnected() {
	return repositoryManager != null;
    }
    public int verifyTicket(int ticket, String urlFragment) {
	boolean rc;
	
	if (repositoryManager == null) {
	    return NOT_CONNECTED;
	}
	try {
	    rc = repositoryManager.verifyTicket(ticket, urlFragment);
	} catch (RemoteException ex) {
	    return REMOTE_EXCEPTION;
        } catch (OperationFailedException ex) {
            return OPERATION_FAILED_EXCEPTION;
        }
	return rc ? TICKET_VALID : TICKET_INVALID;
    }

    public int verifyTicketWithTtl(int ticket, String urlFragment) {
	int rc;

	if (repositoryManager == null) {
	    return NOT_CONNECTED;
	}
	try {
	    rc = repositoryManager.verifyTicketWithTtl(ticket, urlFragment);
	} catch (RemoteException ex) {
	    return REMOTE_EXCEPTION;
        } catch (OperationFailedException ex) {
            return OPERATION_FAILED_EXCEPTION;
        }
	return rc;
    }
}



