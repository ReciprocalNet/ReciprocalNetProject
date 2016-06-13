/*
 * Reciprocal Net project
 * 
 * SiteManagerRemote.java
 *
 * 10-Jun-2002: ekoperda wrote first draft
 * 18-Jun-2002: hclin add interfaces for formal methods
 * 08-Aug-2002: ekoperda added getLocalLabs2() function
 * 23-Aug-2002: ekoperda added getLocalTrackingConfig() function
 * 08-Oct-2002: ekoperda added passIsmBundle() and pullIsmBundle() functions
 * 18-Oct-2002: ekoperda added getLocalSiteInfo() function
 * 31-Oct-2002: ekoperda added writeUpdatedSiteInfo() method
 * 21-Feb-2003: ekoperda reorganized file, added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 11-May-2004: cwestnea modified throws clauses of writeUpdatedLabInfo(), and 
 *              writeUpdatedProviderInfo()
 * 06-Oct-2005: midurbin removed getProviderForSample()
 * 20-Oct-2005: ekoperda renamed passIsmBundle() to exchangeInterSiteMessages()
 *              and removed pullIsmBundle()
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 * 11-May-2006: jobollin switched from getLocalLabs2() to getLocalLabs()
 */

package org.recipnet.site.core;
import java.rmi.Remote;
import java.rmi.RemoteException;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.LogEvent;

/**
 * Remote interface for the Site Manager core object.  This is what calls
 * from the wrapper modules see - they use RMI to connect to this interface.
 */
public interface SiteManagerRemote extends Remote {
    public boolean isAlive(int milliseconds) throws RemoteException;

    public SiteInfo getSiteInfo(int siteId) 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public SiteInfo getLocalSiteInfo() 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public LabInfo getLabInfo(int labId) 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public ProviderInfo getProviderInfo(int providerId) 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public UserInfo getUserInfo(int userId) 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;
    public UserInfo getUserInfo(String username)
	    throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public LabInfo getLabForSample(int sampleId) 
            throws InconsistentDbException, OperationFailedException, 
            RemoteException, ResourceNotFoundException;

    public UserInfo[] getUsersForLab(int labId) 
            throws OperationFailedException, RemoteException, 
            ResourceNotFoundException, WrongSiteException;

    public UserInfo[] getUsersForProvider(int providerId) 
            throws OperationFailedException, RemoteException,
            ResourceNotFoundException, WrongSiteException;

    public SiteInfo[] getAllSiteInfo() 
	    throws OperationFailedException, RemoteException;

    public LabInfo[] getAllLabInfo() 
            throws OperationFailedException, RemoteException; 

    public ProviderInfo[] getAllProviderInfo(int labId) 
            throws OperationFailedException, RemoteException,
            ResourceNotFoundException; 

    public UserInfo[] getAllUserInfo() 
            throws OperationFailedException, RemoteException;

    public ProviderInfo getEmptyProviderInfo() throws RemoteException;

    public UserInfo getEmptyUserInfo() throws RemoteException;

    public void writeUpdatedLabInfo(LabInfo newLabInfo) 
            throws InvalidDataException, OperationFailedException, 
            OperationNotPermittedException, OptimisticLockingException, 
            RemoteException, ResourceNotFoundException, WrongSiteException;
   
    public int writeUpdatedProviderInfo(ProviderInfo provider) 
	    throws InvalidDataException, InvalidModificationException, 
            OperationFailedException, OptimisticLockingException, 
            RemoteException, ResourceNotFoundException, WrongSiteException;

    public int writeUpdatedUserInfo(UserInfo user) 
            throws DuplicateDataException, InvalidDataException, 
		   InvalidModificationException, OperationFailedException,
                   OptimisticLockingException, RemoteException, 
                   ResourceNotFoundException, WrongSiteException;
 
    public int[] getLocalLabs() 
            throws OperationFailedException, RemoteException;

    public LocalTrackingConfig getLocalTrackingConfig(int labId) 
            throws RemoteException, WrongSiteException;

    public void recordLogEvent(LogEvent ev) throws RemoteException;

    public String[] exchangeInterSiteMessages(String messageStrings[], 
            String ipAddr) throws OperationFailedException, RemoteException;
}
