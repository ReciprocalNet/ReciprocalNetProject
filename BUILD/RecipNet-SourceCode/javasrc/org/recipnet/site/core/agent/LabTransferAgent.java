/*
 * Reciprocal Net project
 * LabTransferAgent.java
 *
 * 02-Jan-2009: ekoperda wrote first draft
 * 20-Mar-2009: ekoperda fixed bug #1925 in dismissDepartingLab() and 
 *              greetArrivingLab()
 */

package org.recipnet.site.core.agent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.InvalidModificationException;
import org.recipnet.site.core.IsmProcessingException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.SampleManager;
import org.recipnet.site.core.SiteManager;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.core.msg.InterSiteMessage;
import org.recipnet.site.core.msg.JoinISM;
import org.recipnet.site.core.msg.LabTransferInitiateISM;
import org.recipnet.site.core.msg.LabTransferCompleteISM;
import org.recipnet.site.core.msg.LabUpdateISM;
import org.recipnet.site.core.msg.LocalLabsChangedCM;
import org.recipnet.site.core.msg.ProcessedIsmCM;
import org.recipnet.site.core.msg.ProviderUpdateISM;
import org.recipnet.site.core.msg.SampleStatusHintCM;
import org.recipnet.site.core.msg.SampleUpdateISM;
import org.recipnet.site.core.msg.SendIsmCM;
import org.recipnet.site.core.util.LogRecordGenerator;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.LabTransferInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SiteInfo;
import org.recipnet.site.shared.search.LabSC;
import org.recipnet.site.shared.search.LocalHoldingSC;
import org.recipnet.site.shared.search.PublicStatusSC;

/**
 * Lab Transfer Agent keeps track of all transfers of lab records (with
 * associated providers and samples) between sites of the Site Network.  It
 * also takes special actions when the local site is either the previous home
 * site or the new home site for the lab being transferred.
 *
 * Lab transfers are a complex, multi-step process.  The multiple steps are
 * necessary to enable asynchronous processing of ISM's throughout the life of
 * the Site Network.  New sites being brought online may need to process
 * multiple lab transfers, perhaps even repeated transfers for the same lab,
 * before they are up-to-date with respect to the Site Network.
 *
 * The sequence of actions follows.  Site A refers to the lab's previous home
 * site, Site B refers to the lab's new home site, and Site C refers to any of
 * the other sites in the Site Network.
 *    1. Either Site A or the Coordinator sends a LabUpdateISM indicating that
 *       the lab is inactive.  This prevents further user-level alterations to
 *       the lab record, the providers, and any associated samples.  (Transfer
 *       of active labs is not supported at this time, because Site B lacks
 *       detailed workflow history information about all the samples being
 *       transferred and thus those samples cannot be edited subsequently.)
 *    2. The Coordinator generates a LabTransferInitiateISM that identifies
 *       Site A, Site B, and contains a unique labTransferId.
 *    3. In response to #2, all sites insert a new row into the labTransfers db
 *       table.  Sites A and B do the same.
 *    4. Site B generates a JoinISM against the Coordinator's ISM stream and a
 *       second JoinISM against Site A's ISM stream.
 *    5. Site B generates a LabTransferCompleteISM.
 *    6. In response to #5, all sites update the pertinent row in the 
 *       labTransfers db table to indicate that the transfer is complete.
 *       Also, they update the pertinent row in the labs db table to reflect
 *       a new homeSite_id.
 *    7. Site A ceases generation of any ISM's regarding the lab, its
 *       providers, or its samples.
 *    8. If Site A is still online, it generates a JoinISM against Site B's
 *       ISM stream.  Then it generates several RepositoryHoldingISM's to
 *       divest itself of its holdings related to the lab's samples.  This step
 *       is not necessary if Site A is inactive.
 *    9. Site B generates a LabUpdateISM, several ProviderUpdateISM's, and
 *       several SampleUpdateISM's.  These messages effectively restate Site
 *       B's sense regarding these records.  They also have the effect of
 *       overriding any last-minute changes that Site might have attempted to
 *       transmit.
 *   10. If desired, system administrators transfer data files from Site A's
 *       repository to Site B's.  Such file transfers are beyond the scope of
 *       this agent.
 *
 * This class is not guaranteed to be thread-safe.  Its methods are designed to
 * be called by Site Manager's worker thread, and only by that thread.
 */
public class LabTransferAgent {
    /**
     * The database connection provided by {@code SiteManager} during the
     * constructor. Synchronize on this in deference to Site Manager's own
     * thread management scheme.
     */
    private Connection conn;

    /**
     * A reference to the {@code SiteManager} object to which this agent 
     * belongs.
     */
    private SiteManager siteManager;

    /**
     * A reference to the {@code SampleManager}.  Calls are made to this
     * object whenever the set of local labs has changed.
     */
    private SampleManager sampleManager;

    /**
     * A reference to the {@code RepositoryManager}. Calls are made to
     * this object whenever the set of local labs has changed.
     */
    private RepositoryManager repositoryManager;

    /** Constructor. */
    public LabTransferAgent(Connection conn, SiteManager siteManager,
	    SampleManager sampleManager, RepositoryManager repositoryManager) {
        this.conn = conn;
        this.siteManager = siteManager;
	this.sampleManager = sampleManager;
        this.repositoryManager = repositoryManager;
    }

    /**
     * Processes LabTransferInitiateISM's that arrive at the local site.  These
     * messages originate from the Coordinator and indicate that a lab is
     * being transferred from one site to another.
     */
    public void processLabTransferInitiateISM(LabTransferInitiateISM msg) 
	    throws InconsistentDbException, InvalidDataException, 
	    InvalidModificationException, IsmProcessingException, 
	    OperationFailedException, WrongSiteException {
	// Validate the ISM and fetch containers.
	if (!msg.isFromCoordinator()) {
	    throw new IsmProcessingException(msg,
	            IsmProcessingException.SENDER_NOT_AUTHORIZED);
	}
	LabInfo lab = this.siteManager.getLabInfo(msg.labId);
	SiteInfo previousHomeSite 
                = this.siteManager.getSiteInfo(msg.previousHomeSiteId);
	SiteInfo newHomeSite 
                = this.siteManager.getSiteInfo(msg.newHomeSiteId);

	// Record a new lab transfer.
	LabTransferInfo lti = new LabTransferInfo();
	lti.id = msg.sourceSeqNum;
	lti.labId = msg.labId;
	lti.previousHomeSiteId = msg.previousHomeSiteId;
	lti.newHomeSiteId = msg.newHomeSiteId;
	this.dbAddLabTransferInfo(lti);

	// Special handling for a lab being transferred to the local site.
	if (msg.newHomeSiteId == this.siteManager.localSiteId) {
	    this.greetArrivingLab(msg);
	}

	// Wrap up.
	this.siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded initiation of transfer of lab id " + msg.labId 
		+ " from site id " + msg.previousHomeSiteId + " to site id "
		+ msg.newHomeSiteId));
    }

    /**
     * Processed LabTransferCompleteISM's that arrive at the local site.  These
     * messages originate from the recipient of the transfer and indicate that
     * the transfer is accepted.  (Transfers begin with 
     * LabTransferInitiateISM's as described above.)
     */
    public void processLabTransferCompleteISM(LabTransferCompleteISM msg)
	    throws InconsistentDbException, InvalidDataException, 
            InvalidModificationException, IsmProcessingException, 
            OperationFailedException, WrongSiteException {
	// Validate the ISM and fetch containers.
	LabTransferInfo lti = this.dbFetchLabTransferInfo(msg.labTransferId);
	LabInfo lab = this.siteManager.getLabInfo(lti.labId);
	if (lti == null) {
	    throw new ResourceNotFoundException();
	}
	if (!msg.isFrom(lti.newHomeSiteId) && !msg.isFromCoordinator()) {
	    throw new IsmProcessingException(msg,
	            IsmProcessingException.SENDER_NOT_AUTHORIZED);
	}
	if (lti.previousHomeSiteId != lab.homeSiteId) {
	    throw new IsmProcessingException(msg,
		    IsmProcessingException.RESOURCE_NOT_ADVERTISED);
	}

	// Update database records.
	lti.complete = true;
	this.dbModifyLabTransferInfo(lti);
	lab.homeSiteId = lti.newHomeSiteId;
	this.siteManager.writeUpdatedLabInfo2(lab);

	// Special handling for a lab lab being transferred away from the local
	// site.
	if (lti.previousHomeSiteId == this.siteManager.localSiteId) {
	    this.dismissDepartingLab(msg);
	}

	// Wrap up.
	this.siteManager.passCoreMessage(ProcessedIsmCM.success(msg,
                "Recorded completion of transfer of lab id " + lti.labId 
		+ " from site id " + lti.previousHomeSiteId + " to site id "
		+ lti.newHomeSiteId));
    }

    /**
     * Internal method to handle the special case where a lab is being
     * transferred to the local site (i.e. we are the recipient).
     *
     * The current implementation updates the local state and generates
     * multiple new ISM's.
     */
    private void greetArrivingLab(LabTransferInitiateISM msg)
	    throws InconsistentDbException, InvalidDataException, 
	    InvalidModificationException, OperationFailedException,
            WrongSiteException {
	// Update database records to indicate the transfer is complete.
	long labTransferId = msg.sourceSeqNum;
	LabTransferInfo lti = this.dbFetchLabTransferInfo(labTransferId);
	if (lti == null) {
	    throw new ResourceNotFoundException();
	}
	lti.complete = true;
	this.dbModifyLabTransferInfo(lti);
	LabInfo lab = this.siteManager.getLabInfo(msg.labId);
	lab.homeSiteId = this.siteManager.localSiteId;
	this.siteManager.writeUpdatedLabInfo2(lab);
	
	// Notify other core modules that the set of local labs has changed.
	this.siteManager.passCoreMessage(new LocalLabsChangedCM());
	this.sampleManager.passCoreMessage(new LocalLabsChangedCM());
	this.repositoryManager.passCoreMessage(new LocalLabsChangedCM());

	// Join with the Coordinator's ISM stream.
	this.siteManager.passCoreMessage(new SendIsmCM(new JoinISM(
		this.siteManager.localSiteId, msg.sourceSiteId, 
                msg.sourceSeqNum)));

	// Join with the transferring site's ISM stream.	
	SiteInfo previousHomeSite = this.siteManager.getSiteInfo(
		msg.previousHomeSiteId);
	if (previousHomeSite.publicSeqNum 
                != InterSiteMessage.INVALID_SEQ_NUM) {
	    this.siteManager.passCoreMessage(new SendIsmCM(new JoinISM(
		    this.siteManager.localSiteId, msg.previousHomeSiteId, 
                    previousHomeSite.publicSeqNum)));
	}

	// Accept the lab transfer.
        this.siteManager.passCoreMessage(new SendIsmCM(
                new LabTransferCompleteISM(this.siteManager.localSiteId, 
		labTransferId)));

	// Restate our sense of the lab record.
	this.siteManager.passCoreMessage(new SendIsmCM(new LabUpdateISM(
		lab, this.siteManager.localSiteId)));

	// Restate our sense of all the associated provider records.
	for (ProviderInfo provider 
                : this.siteManager.getAllProviderInfo(msg.labId)) {
	    this.siteManager.passCoreMessage(new SendIsmCM(
                    new ProviderUpdateISM(provider, 
		    this.siteManager.localSiteId)));
	}

	// Restate our sense of all the associated sample records.  (This
	// needs to happen synchronously, within Site Manager's worker thread,
	// in order to avoid the risk that another ISM might be processed 
	// before we generate our SampleUpdateISM's.)  Also cue Repository
	// Manager to create repository directories and announce holdings.
	SearchParams search = this.sampleManager.getEmptySearchParams();
	search.addToHeadWithAnd(new LabSC(msg.labId));
	search.addToHeadWithAnd(new PublicStatusSC());
	int searchId = this.sampleManager.storeSearchParams(search);
	for (SampleInfo sample 
                : this.sampleManager.getSearchResults(searchId)) {
	    this.sampleManager.readvertiseSample(sample.id);
	    this.repositoryManager.passCoreMessage(new SampleStatusHintCM(
	            sample.id, 
                    SampleStatusHintCM.Trigger.LOCAL_SITE_NEWLY_AUTHORITATIVE,
                    null));
	}

	// Log a special completion message.
	this.siteManager.recordLogRecord(LogRecordGenerator.labTransferArrived(
	        msg.labId, msg.previousHomeSiteId));
    }

    /**
     * Internal method to handle the special case where a lab is transferring
     * away from the local site.  Our dismissal occurs only after the recipient
     * of the lab has accepted the lab transfer.  
     *
     * The current implementation updates the local state and generates
     * multiple new ISM's.
     * 
     * Note that this is an optional operation primarily for the benefit of the
     * local site.  Other sites in the Site Network will function acceptably
     * well even if they do not receive the ISM's generated by this method.
     * Our cooperation with the lab transfer operation is helpful but our
     * noncooperation would not halt the operation.  The Coordinator has the
     * final say.
     */
    private void dismissDepartingLab(LabTransferCompleteISM msg)
	    throws InconsistentDbException, OperationFailedException {
	// Notify other core modules that the set of local labs has changed.
	this.siteManager.passCoreMessage(new LocalLabsChangedCM());
	this.sampleManager.passCoreMessage(new LocalLabsChangedCM());
	this.repositoryManager.passCoreMessage(new LocalLabsChangedCM());

	// Join with the accepting site's ISM stream.
	LabTransferInfo lti = this.dbFetchLabTransferInfo(msg.labTransferId);
	if (lti == null) {
	    throw new ResourceNotFoundException();
	}
	SiteInfo newHomeSite = this.siteManager.getSiteInfo(
		lti.newHomeSiteId);
	if (newHomeSite.publicSeqNum != InterSiteMessage.INVALID_SEQ_NUM) {
	    this.siteManager.passCoreMessage(new SendIsmCM(new JoinISM(
		    this.siteManager.localSiteId, lti.newHomeSiteId, 
                    newHomeSite.publicSeqNum)));
	}

	// Reduce the local holding level for every sample that was
	// transferred.  This cues Repository Manager to generate ISM's 
	// accordingly.
	SearchParams search = this.sampleManager.getEmptySearchParams();
	search.addToHeadWithAnd(new LabSC(lti.labId));
	search.addToHeadWithAnd(new LocalHoldingSC());
	int searchId = this.sampleManager.storeSearchParams(search);
	for (SampleInfo sample 
                : this.sampleManager.getSearchResults(searchId)) {
	    this.repositoryManager.passCoreMessage(new SampleStatusHintCM(
		    sample.id, 
		  SampleStatusHintCM.Trigger.LOCAL_SITE_NEWLY_NONAUTHORITATIVE,
                    null));
	}

	// Log a special completion message.
	this.siteManager.recordLogRecord(
	        LogRecordGenerator.labTransferDeparted(lti.labId, 
                lti.newHomeSiteId));
    }

    /**
     * Inserts a new row into the labTransfers db table.
     */
    private void dbAddLabTransferInfo(LabTransferInfo lti) 
            throws OperationFailedException {
	String sql = "SELECT * FROM labTransfers WHERE id=0;";
	synchronized (conn) {
	    try {
		Statement cmd = conn.createStatement(
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
		try {
		    ResultSet rs = cmd.executeQuery(sql);
		    rs.moveToInsertRow();
		    lti.dbStore(rs);
		    rs.insertRow();
		} finally {
		    cmd.close();
		}
	    } catch (SQLException ex) {
		throw new OperationFailedException(ex);
	    }
	}
    }

    /**
     * Updates an existing row in the labTransfers db table.
     */
    private void dbModifyLabTransferInfo(LabTransferInfo lti)
	    throws OperationFailedException, ResourceNotFoundException {
	String sql = "SELECT * FROM labTransfers WHERE id=" + lti.id + ";";
	synchronized (conn) {
	    try {
		Statement cmd = conn.createStatement(
                        ResultSet.TYPE_FORWARD_ONLY,
                        ResultSet.CONCUR_UPDATABLE);
		try {
		    ResultSet rs = cmd.executeQuery(sql);
		    if (!rs.next()) {
			// There is no existing record for the caller's
			// LabTransferInfo.
			throw new ResourceNotFoundException();
		    }
		    lti.dbStore(rs);
		    rs.updateRow();
		} finally {
		    cmd.close();
		}
	    } catch (SQLException ex) {
		throw new OperationFailedException(ex);
	    }
	}
    }

    /**
     * Returns the LabTransferInfo object associated with the specified
     * labTransferId, or null if none was found.
     */
    private LabTransferInfo dbFetchLabTransferInfo(long labTransferId)
            throws OperationFailedException {
	String sql = "SELECT * FROM labTransfers WHERE id=" + labTransferId 
	        + ";";
	synchronized (this.conn) {
	    try {
		Statement cmd = conn.createStatement();
		try {
		    ResultSet rs = cmd.executeQuery(sql);
		    return rs.next() ? new LabTransferInfo(rs) : null;
		} finally {
		    cmd.close();
		}
	    } catch (SQLException ex) {
		throw new OperationFailedException(ex);
	    }
	}
    }
}
