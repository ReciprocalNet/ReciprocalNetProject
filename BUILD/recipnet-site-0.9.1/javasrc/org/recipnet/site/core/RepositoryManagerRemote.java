/*
 * Reciprocal Net project
 * 
 * RepositoryManagerRemote.java
 *
 * 17-Jun-2002: leqian wrote first draft
 * 28-Jun-2002: ekoperda added getHostedSamples() function
 * 09-Jul-2002: ekoperda updated interface to match first draft of Repository 
 *              Manager
 * 07-Nov-2002: ekoperda added functions createDataFile() and readDataFile()
 * 30-Jan-2003: ekoperda removed comments and cleaned up file
 * 21-Feb-2003: ekoperda added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 29-May-2003: ekoperda removed getHostedSamples(), 
 *              getPreferredSiteForSample(), getHolding(), 
 *              getHoldingsForSample(), getLocalHoldingForSample(),
 *              createDataFile(), and readDataFile(); added 
 *              getRepositoryFiles(), beginWritingDataFile(), 
 *              beginReadingDataFile(), readFromDataFile(), writeToDataFile(), 
 *              closeDataFile(), abortDataFile(), eradicateDataFile(), 
 *              removeDataFile(), renameDataFile(), and 
 *              getDataFileAggregateSize(); changed spec of 
 *              createDataDirectory() and grantNewTicket(), verifyTicket(), 
 *              and verifyTicketWithTtl()
 * 17-Jul-2003: midurbin modified spec for beginWritingDataFile() to accomodate
 *              InvalidDataException's being thrown for illegal filenames
 * 07-Aug-2003: midurbin added revertSampleToVersionIncludingFiles()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 15-Jul-2004: ekoperda added closeDataFiles()
 * 04-Aug-2005: midurbin replaced removeDataFile() and eradicateDataFile() with
 *              removeDataFiles() and eradicateDataFiles() respectively
 * 17-Oct-2005: midurbin added getAllRepositoryFileInfosForSample()
 * 21-Oct-2005: midurbin added  modifyDataFileDescription() and updated various
 *              method specifications to include file descriptions
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 */

package org.recipnet.site.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.shared.RepositoryFiles;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * RMI remote interface for Repository Manager.  The methods listed
 * here are accessible to the wrapper modules and command-line utilities.
 */
public interface RepositoryManagerRemote extends Remote {
    public boolean isAlive(int milliseconds) throws RemoteException;

    public int getLocalHoldingLevel(int sampleId) 
            throws OperationFailedException, RemoteException;

    public List<RepositoryHoldingInfo> getHoldingsForSample(int sampleId) 
            throws OperationFailedException, RemoteException;

    public RepositoryFiles getRepositoryFiles(int sampleId, 
            int sampleHistoryId, boolean shouldIgnoreFileSizeLimit) 
            throws InconsistentDbException, OperationFailedException,  
            RemoteException, ResourceNotFoundException, WrongSiteException;

    public void createDataDirectory(int sampleId, String extensionPath, 
            int userId) throws InconsistentDbException, InvalidDataException, 
            InvalidModificationException, OperationFailedException, 
            RemoteException, ResourceNotFoundException, WrongSiteException;

    public int grantNewTicket(RepositoryFiles files, int userId) 
            throws OperationFailedException, RemoteException;

    public boolean verifyTicket(int ticket, String urlFragment) 
	    throws OperationFailedException, RemoteException;

    public int verifyTicketWithTtl(int ticket, String urlFragment) 
            throws OperationFailedException, RemoteException;

    public void renewTickets(int... ticketIds) throws ResourceNotFoundException,
            OperationFailedException, RemoteException;
        
    public int beginWritingDataFile(SampleInfo sample, String fileName,
	    boolean allowCreation, boolean allowOverwrite, int actionCode, 
            int userId, String comments, String description)
            throws InvalidDataException, OperationFailedException,
            OperationNotPermittedException, RemoteException,
            ResourceNotAccessibleException, ResourceNotFoundException,
            WrongSiteException;

    public int beginReadingDataFile(int sampleId, int sampleHistoryId, 
            String fileName, int userId) throws InconsistentDbException, 
            OperationFailedException, RemoteException,
            ResourceNotAccessibleException, ResourceNotFoundException;

    public byte[] readFromDataFile(int ticketId, int maxBytesToRead)
	    throws OperationFailedException, OperationNotPermittedException, 
            RemoteException, ResourceNotFoundException;

    public void writeToDataFile(int ticketId, byte[] dataToWrite)
	    throws OperationFailedException, OperationNotPermittedException, 
            RemoteException, ResourceNotFoundException;

    public void closeDataFile(int ticketId) throws OperationFailedException, 
            RemoteException, ResourceNotFoundException;

    public void closeDataFiles(int ticketIds[]) 
 	    throws OperationFailedException, OperationNotPermittedException,
	    RemoteException, ResourceNotFoundException;

    public void abortDataFile(int ticketId) throws OperationFailedException, 
            RemoteException, ResourceNotFoundException;

    public void eradicateDataFiles(SampleInfo sample, String[] fileNames,
	    int userId, int actionCode, String comments)
            throws InconsistentDbException, InvalidDataException,
            OperationFailedException, OperationNotPermittedException,
            RemoteException, RepositoryDirectoryNotFoundException, 
            ResourceNotAccessibleException, ResourceNotFoundException, 
            WrongSiteException;

    public void removeDataFiles(SampleInfo sample, String[] fileNames,
            int userId, int actionCode, String comments)
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, OperationFailedException,
            OperationNotPermittedException, RemoteException,
            RepositoryDirectoryNotFoundException,
            ResourceNotAccessibleException, ResourceNotFoundException, 
	    WrongSiteException;

    public void renameDataFile(SampleInfo sample, String oldFileName, 
            String newFileName, int userId, String comments) 
            throws DuplicateDataException, InconsistentDbException, 
            InvalidDataException, OperationFailedException, 
            OperationNotPermittedException, RemoteException, 
            RepositoryDirectoryNotFoundException, 
            ResourceNotAccessibleException, ResourceNotFoundException, 
 	    WrongSiteException;

    public void modifyDataFileDescription(SampleInfo sample, String fileName, 
            String description, int userId, String comments) 
            throws DuplicateDataException, InconsistentDbException, 
            InvalidDataException, OperationFailedException, 
            OperationNotPermittedException, RemoteException, 
            RepositoryDirectoryNotFoundException, 
            ResourceNotAccessibleException, ResourceNotFoundException, 
 	    WrongSiteException;

    public void revertSampleToVersionIncludingFiles(int sampleId,
            int currentHistoryId, int desiredHistoryId, int userId,
            String comments) throws InconsistentDbException,
            InvalidDataException, InvalidModificationException,
            OperationFailedException, OptimisticLockingException,
            RemoteException, ResourceNotFoundException, WrongSiteException;

    public long getDataFileAggregateSize(int sampleId, String fileName)
            throws InconsistentDbException, OperationFailedException, 
	    RemoteException, RepositoryDirectoryNotFoundException, 
            ResourceNotFoundException;

    public RepositoryFileInfo[] getAllRepositoryFileInfosForSample(
            int sampleId) throws InconsistentDbException,
            OperationFailedException, RemoteException, WrongSiteException;
}
