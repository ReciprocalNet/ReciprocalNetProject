/*
 * Reciprocal Net project
 * 
 * SampleManagerRemote.java
 *
 * 17-Jun-2002: leqian wrote first draft
 * 19-Jun-2002: ekoperda added the 'extends Remote' clause to the interface
 * 29-Jun-2002: ekoperda added verifySampleNumber() and verifySampleNumbers() 
 *              interfaces.  Also getAllKnownSamples(), getAllHostedSamples(),
 *              and getAllOwnedSamples()
 * 23-Jul-2002: ekoperda coded revertSampleToVersion() method
 * 16-Aug-2002: ekoperda added a second version of getSearchResults()
 * 18-Oct-2002: ekoperda changed return type of verifySampleNumbers(), renamed
 *              getAllOwnedSamples() to getAllAuthoritativeSampleS(), added
 *              second version of putSampleInfo() that takes action date as a
 *              parameter
 * 21-Oct-2002: ekoperda added function getEarliestAuthoritativeHistoryDate()
 * 04-Feb-2003: ekoperda removed getAllAuthoritativeSamples(), 
 *              getAllHostedSamples(), and getAllKnownSamples(), cleaned code
 * 14-Feb-2003: adharurk added lookupSampleId()
 * 20-Feb-2003: ekoperda added retrieveSearchParams()
 * 21-Feb-2003: ekoperda added exception support throughout
 * 12-Mar-2003: nsanghvi imported InconsistentDbException
 * 22-Apr-2003: ekoperda removed clientIp field from argument list of
 *              pustSampleInfo() and revertSampleToVersion()
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganizations
 * 27-Sep-2005: midurbin added getNextUnusedLocalLabId()
 * 07-Apr-2006: jobollin removed isAlive() (but not isAlive(int))
 */

package org.recipnet.site.core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Set;

import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.shared.SampleStats;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * This is an interface for the Sample Manager core object.  
 */
public interface SampleManagerRemote extends Remote {
    public boolean isAlive(int milliseconds) throws RemoteException;
     
    public SampleInfo getSampleInfo() throws RemoteException;
    public SampleInfo getSampleInfo(int sampleId) 
            throws InconsistentDbException, OperationFailedException,
            RemoteException, ResourceNotFoundException;
    public SampleInfo getSampleInfo(int sampleId, int sampleHistoryId) 
	    throws InconsistentDbException, OperationFailedException, 
            RemoteException, ResourceNotFoundException;

    public FullSampleInfo getFullSampleInfo(int sampleId) 
            throws InconsistentDbException, OperationFailedException, 
            RemoteException, ResourceNotFoundException;

    public int lookupSampleId(int labId, String localLabId) 
	    throws OperationFailedException, RemoteException;
    
    public boolean verifySampleNumber(int sampleId) 
            throws OperationFailedException, RemoteException;

    public Set<Integer> verifySampleNumbers(Set<Integer> sampleIds) 
            throws OperationFailedException, RemoteException;

    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode, 
            int userId, String comments) 
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException, 
            OperationFailedException, OptimisticLockingException, 
            RemoteException, ResourceNotFoundException, WrongSiteException;
    public SampleInfo putSampleInfo(SampleInfo sample, int actionCode, 
            Date actionDate, int userId, String comments) 
            throws DuplicateDataException, InconsistentDbException,
            InvalidDataException, InvalidModificationException, 
            OperationFailedException, OptimisticLockingException, 
            RemoteException, ResourceNotFoundException, WrongSiteException;

    public void revertSampleToVersion(int sampleId, int currentHistoryId,
            int desiredHistoryId, int userId, String comments) 
            throws InconsistentDbException, InvalidDataException, 
            InvalidModificationException, OperationFailedException, 
            OptimisticLockingException, RemoteException, 
            ResourceNotFoundException, WrongSiteException;
    
    public SearchParams getEmptySearchParams() throws RemoteException;
    
    public int storeSearchParams(SearchParams searchParams) 
            throws OperationFailedException, RemoteException;
    
    public SearchParams getSearchParams(int searchId)
	    throws OperationFailedException, RemoteException, 
            ResourceNotFoundException;

    public SampleInfo[] getSearchResults(int searchId) 
            throws InconsistentDbException, OperationFailedException, 
            RemoteException, ResourceNotFoundException;
    public SampleInfo[] getSearchResults(int searchId, int startIndex, 
            int maxSamples) throws InconsistentDbException, 
            OperationFailedException, RemoteException, 
            ResourceNotFoundException;
    
    public SampleStats getStats() throws RemoteException;
    
    public void resetStats() throws RemoteException;

    public Date getEarliestAuthoritativeHistoryDate() 
            throws OperationFailedException, RemoteException;

    public String getNextUnusedLocalLabId(int labId)
            throws OperationFailedException, RemoteException;
}
