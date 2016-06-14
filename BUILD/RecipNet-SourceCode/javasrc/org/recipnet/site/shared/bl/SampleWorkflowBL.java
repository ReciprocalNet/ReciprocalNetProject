/*
 * Reciprocal Net project
 * 
 * SampleWorkflowBL.java
 *
 * 03-Aug-2004: cwestnea wrote first draft
 * 29-Sep-2004: midurbin removed getVisibilityCodeForAction() and added
 *              isLtaVisibleDuringAction()
 * 29-Sep-2004: midurbin modified isLtaVisibleDuringAction() to reflect the
 *              name change of VISIBLE_ON_LOCALTRACKING to
 *              VISIBLE_ON_CUSTOMFIELDS
 * 05-Oct-2004: midurbin added support for the RETRACTED action to
 *              alterSampleForWorkflowAction()
 * 13-Dec-2004: eisiorho made modified alterSampleForWorkflowAction() to
 *              reflect new class SampleMathBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 05-Jan-2005: midurbin added REVERTED_WITHOUT_FILES, REVERTED_INCLUDING_FILES
 *              action codes to replace the REVERTED action code
 * 25-Feb-2005: midurbin added getAllPublicStatusCodes(),
 *              getAllFinishedStatusCodes(), getAllRetractedStatusCodes(),
 *              isFinishedStatus() and isRetractedStatus()
 * 10-Mar-2005: midurbin added PREFERRED_NAME to the eligible fields for
 *              RELEASED_TO_PUBLIC and updated alterSampleForWorkflowAction()
 *              to sometimes remove an invalidated PREFERRED_NAME annotation
 * 23-Mar-2005: midurbin updated alterSampleForWorkflowAction() to account for
 *              changes made to SampleTextBL.
 * 12-Apr-2005: midurbin added RAW_DATA_URL to the eligible fields for
 *              PRELIMINARY_DATA_COLLECTED
 * 06-Jun-2005: midurbin added SUPERSEDED_ANOTHER_SAMPLE
 * 10-Jun-2005: midurbin copied code from WorkflowHelper into this class
 * 14-Jun-2005: midurbin added getSampleSummaryByDay()
 *              wereActionsPerformedOnTheSameDay() and the SampleActionSummary
 *              nested container class.
 * 24-Jun-2005: midurbin made 'ActionRecord.isDistinguished' public and added
 *              getAllStatusCodes()
 * 30-Jun-2005: midurbin updated isActionValid() to allow samples with
 *              INVALID_STATUS to be SUBMITTED
 * 05-Jul-2005: ekoperda added action code MULTIPLE_FILES_ADDED_OR_REPLACED
 * 04-Aug-2005: midurbin renamed FILE_REMOVED, FILE_ERADICATED action codes to
 *              FILES_REMOVED and FILES_ERADICATED respectively and added
 *              FILE_RENAMED action code to alterSampleForWorkflowAction()
 * 06-Oct-2005: midurbin added doesActionRequireProviderUsers() and added a
 *              call to to the new method, updateSampleAcl(), to
 *              alterSampleForWorkflowAction() where needed
 * 17-Oct-2005: midurbin added bindFilesToActions(), added FileRecord class to 
 *              keep track of files added and removed for each ActionRecord
 * 21-Oct-2005: midurbin added FILE_DESCRIPTION_MODIFIED action code
 * 27-Oct-2005: midurbin added support for files modified during an action to
 *              bindFilesToActions() and FileRecord
 * 10-Nov-2005: midurbin improved handling of actions skipped by reversion
 * 31-Mar-2006: jobollin accommodated the addition of the Crystallographer field
 *              to failedtocollect.jsp; changed some type arguments
 * 01-Jun-2006: jobollin reformatted the source; implemented generics;
 *              significantly modified the internal logic of getActionRecords
 *              and the methods it invokes
 * 20-Nov-2006: jobollin updated javadoc tags where javadoc didn't understand
 *              references to nested classes
 */

package org.recipnet.site.shared.bl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.SampleFieldRecord;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * This class defines all the business logic related to the sample workflow
 * process. Included in this are the status codes for samples, the action codes,
 * and various static methods for manipulating and verifying these codes.
 * </p><p>
 * A typical workflow system is comprised of a set of <i>states</i> and a set
 * of <i>actions</i> representing transitions between pairs of states. In the
 * Reciprocal Net site software, every sample record (represented by a
 * {@code SampleInfo} object) has a {@code status} property whose possible
 * values are defined by constants on this class. Real-world sample actions
 * correspond to "action codes" whose possible values are also defined here.
 * Given a sample at a particular state, some actions are valid and others are
 * not. The class function {@link #isActionValid(int, int)} can be used to
 * discover which is the case. It should not be possible for users to alter
 * samples by performing an action which is invalid.
 * </p><p>
 * Most workflow actions are useful because they provide an opportunity for the
 * user to set values for particular fields on a sample. Some fields are
 * accessible (i.e. visible or editable) during some workflow actions; others
 * are accessible during different workflow actions. The static function
 * {@link #getEligibleFieldCodes(int, LocalTrackingConfig)} can be used to
 * discover which fields should be visible/editable to the user during
 * performance of a particular workflow action. Fields are identified by
 * constants defined on {@link SampleDataInfo} and {@link SampleTextBL}.
 * </p><p>
 * Some workflow action types are <i>distinguished</i> in the sense that it
 * makes sense to users to perform an action of such a type no more than once
 * per sample. Examples of distinguished actions types include Submit, Collect
 * Preliminary Data, Refine Structure, and so forth. Non-distinguished actions
 * are the norm -- it is common for users to perform these on a sample more than
 * once. Special support for distinguished actions is present in many instance
 * methods and is described in more detail below. All distinguished actions are
 * <i>correctable</i>, which means that once a user performs the workflow
 * action (presumably assigning values to some data fields in the process), at
 * any time later he may correct the data values he entered previously.
 * Typically the UI form used for performing the action is reused later for
 * correcting that action's data. During such a correction, some field values on
 * the form may need to be non-editable (but still visible) in order to
 * guarantee workflow integrity. UI forms in "correction mode" should discover
 * the fields for which this is the case by calling the class function
 * {@code isFieldCorrectable()}.
 * </p><p>
 * The method
 * {@link #getActionRecords(FullSampleInfo, LocalTrackingConfig, Collection)}
 * executes the complicated and time-consuming algorithm to match fields with
 * workflow actions. Exactly one "action record" is generated for every version
 * of the sample passed to the method.
 * </p><p>
 * Each action record also has a set of fields (including values for the fields)
 * that should be presented to the user as being logically associated with the
 * action. Every data value on the most recent version of the caller-supplied
 * sample (represented within the {@code SampleDataInfo} object, a
 * {@code SampleAttributeInfo} object, or a {@code SampleAnnotationInfo} object)
 * is bound to exactly one of the actions in the set -- see
 * {@code bindFieldsToActions()} for details about the algorithm that achieves
 * this. The set of fields is stored as a {@code Collection} of
 * {@code SampleFieldRecord} objects within the {@code ActionRecord} objects
 * returned by {@code getActionRecords()}.
 * </p><p>
 * Some field types are <i>repeatable</i>; that is, several field records on
 * the same sample may have identical field codes but differing values. (This
 * might be the case for data values stored as attributes or annotations.) These
 * fields of identical type may be bound to the same action, or they may be
 * distributed among several actions.
 * </p><p>
 * Some field types are <i>compulsory</i>, which means that a field heading for
 * the type must be bound to at least one action record, even if no value for
 * that field is present on the current version of the caller-supplied sample.
 * (This presumably would cause the UI to display a field heading whose data
 * value was empty.) These decisions are made by
 * {@link #getCompulsoryFieldCodes(ActionInfo, LocalTrackingConfig)}.
 * </p>
 */
public class SampleWorkflowBL {
    
    /*
     * This code isn't particularly speedy, nor was it designed to be:
     * {@code getActionRecords()} and supporting code in particular performs a
     * complex procedure, and readability was the prime concern.
     */
    
    
    /*
     * The following constants represent the possible values for
     * SampleInfo.status. SampleInfo objects are modified with these sample
     * status codes in alterSampleForWorkflowAction()
     */
    
    /**
     * The standard invalid status code
     */
    public static final int INVALID_STATUS = 0;
    
    /**
     * The code for "pending" status
     */
    public static final int PENDING_STATUS = 100;
    
    /**
     * The code for "refinement pending" status
     */
    public static final int REFINEMENT_PENDING_STATUS = 200;
    
    /**
     * The code for "suspended" status
     */
    public static final int SUSPENDED_STATUS = 210;
    
    /**
     * The code for "complete" status
     */
    public static final int COMPLETE_STATUS = 800;
    
    /**
     * The code for "complete, public" status
     */
    public static final int COMPLETE_PUBLIC_STATUS = 810;
    
    /**
     * The code for "incomplete" status
     */
    public static final int INCOMPLETE_STATUS = 850;
    
    /**
     * The code for "incomplete, public" status
     */
    public static final int INCOMPLETE_PUBLIC_STATUS = 860;
    
    /**
     * The code for "non-single crystalline sample" status
     */
    public static final int NON_SCS_STATUS = 870;
    
    /**
     * The code for "non-single crystalline sample, public" status
     */
    public static final int NON_SCS_PUBLIC_STATUS = 880;
    
    /**
     * The code for "retracted" status
     */
    public static final int RETRACTED_STATUS = 900;
    
    /**
     * The code for "retracted, public" status
     */
    public static final int RETRACTED_PUBLIC_STATUS = 910;
    
    /**
     * The code for "withdrawn" status
     */
    public static final int WITHDRAWN_STATUS = 920;
    
    /**
     * The code for "no-go" status
     */
    public static final int NOGO_STATUS = 950;

    /*
     * The following constants are invalid action codes. They are used,
     * however, as placeholders for actual action codes that are values for
     * {@code SampleHistoryInfo.action}.
     */
    
    /**
     * The normal invalid action code 
     */
    public static final int INVALID_ACTION = 0;
    
    /**
     * An invalid action code intended to be replaced during processing by
     * either {@link #FILE_ADDED} or {@link #FILE_REPLACED} as dictated by
     * sample state
     */
    public static final int SUBSTITUTE_FILE_ADDED_OR_FILE_REPLACED = -2;

    /*
     * The following constants are possible values for SampleHistoryInfo.action
     */
    
    /**
     * An action code for the sample being initially submitted
     */
    public static final int SUBMITTED = 100;

    /**
     * An action code for the submission information subsequently being
     * corrected
     */
    public static final int SUBMITTED_CORRECTED = 110;
    
    /**
     * An action code for the preliminary data collection information being
     * entered
     */
    public static final int PRELIMINARY_DATA_COLLECTED = 200;
    
    /**
     * An action code for the preliminary data collection information being
     * corrected
     */
    public static final int PRELIMINARY_DATA_COLLECTED_CORRECTED = 210;
    
    /**
     * An action code for the structure refinement information being entered
     */
    public static final int STRUCTURE_REFINED = 300;
    
    /**
     * An action code for the structure refinement information being corrected
     */
    public static final int STRUCTURE_REFINED_CORRECTED = 310;
    
    /**
     * An action code for the sample information being released to the public
     */
    public static final int RELEASED_TO_PUBLIC = 400;
    
    /**
     * An action code for the sample release information being corrected
     */
    public static final int RELEASED_TO_PUBLIC_CORRECTED = 410;
    
    /**
     * An action code for one of the various text fields being modified via the
     * generic modifytext page
     */
    public static final int MODIFIED_TEXT_FIELDS = 500;
    
    /**
     * An action code for one of the local tracking fields being modified via
     * the generic modifylta page
     */
    public static final int MODIFIED_LTAS = 550;
    
    /**
     * An action code for a citation of the sample being added
     */
    public static final int CITATION_ADDED = 600;
    
    /**
     * An action code for an existing citation being corrected
     */
    public static final int CITATION_ADDED_CORRECTED = 610;
    
    /**
     * An action code for a sample being retracted
     */
    public static final int RETRACTED = 800;
    
    /**
     * An action code for a sample being marked as superseding another
     * (specific) sample
     */
    public static final int SUPERSEDED_ANOTHER_SAMPLE = 850;
    
    /**
     * An action code for a sample being suspended
     */
    public static final int SUSPENDED = 900;
    
    /**
     * An action code for a sample's suspension information being corrected
     */
    public static final int SUSPENDED_CORRECTED = 905;
    
    /**
     * An action code for a sample being resumed
     */
    public static final int RESUMED = 910;
    
    /**
     * An action code for a sample's resumption information being corrected
     */
    public static final int RESUMED_CORRECTED = 915;
    
    /**
     * An action code for a sample's access information being changed
     */
    public static final int CHANGED_ACL = 1100;
    
    /*
     * The reverted action has been replaced by
     * REVERTED_WITHOUT_FILES and
     * REVERTED_INCLUDING_FILES.  The original REVERTED code should not be
     * reused. 
     */
    //public static final int REVERTED = 1200;

    /**
     * An action code for a sample's metadata being reverted to an earlier
     * state without modification of files 
     */
    public static final int REVERTED_WITHOUT_FILES = 1210;

    /**
     * An action code for a sample's metadata and files being reverted to an
     * earlier state 
     */
    public static final int REVERTED_INCLUDING_FILES = 1220;
    
    /**
     * An action code for a sample being imported from some previous database
     * or LIMS
     */
    public static final int IMPORTED = 1300;
    
    /**
     * An action code recording sample modification on account of a database
     * rebuild
     */
    public static final int DB_REBUILT = 1400;
    
    /**
     * An action code for a sample being declared incomplete
     */
    public static final int DECLARED_INCOMPLETE = 1500;
    
    /**
     * An action code for a sample's incompletion information being corrected
     */
    public static final int DECLARED_INCOMPLETE_CORRECTED = 1510;
    
    /**
     * An action code for a sample being declared a non-crystalline sample
     */
    public static final int DECLARED_NON_SCS = 1600;
    
    /**
     * An action code for a sample's non-crystalline sample information being
     * corrected
     */
    public static final int DECLARED_NON_SCS_CORRECTED = 1610;
    
    /**
     * An action code for declaring failure to collected data on a sample
     */
    public static final int FAILED_TO_COLLECT = 1700;
    
    /**
     * An action code for a sample's data collection failure information being
     * corrected
     */
    public static final int FAILED_TO_COLLECT_CORRECTED = 1710;
    
    /**
     * An action code for a sample being withdrawn from consideration before
     * data collection
     */
    public static final int WITHDRAWN_BY_PROVIDER = 1800;
    
    /**
     * An action code for a sample's withdrawl information being corrected
     */
    public static final int WITHDRAWN_BY_PROVIDER_CORRECTED = 1810;

    /**
     * An action code used by the system when it updates a sample in response
     * to detecting one or more files added to the repository by an external
     * means
     */
    public static final int DETECTED_PREEXISTING_FILES = 10000;

    /**
     * An action code used by the system when it updates a sample in response
     * to detecting repository file modifications performed by other means
     */
    public static final int DETECTED_FILE_CHANGES = 10100;
    
    /**
     * An action code representing addition of a file to a sample's repository
     * directory
     */
    public static final int FILE_ADDED = 10200;
    
    /**
     * An action code representing addition of a file to a sample's repository
     * directory implicitly, as part of some other user-requested action
     */
    public static final int FILE_ADDED_IMPLICITLY = 10300;
    
    /**
     * An action code representing the replacement of a file with a different
     * one with the same name
     */
    public static final int FILE_REPLACED = 10400;
    
    /**
     * An action code representing renaming of a file
     */
    public static final int FILE_RENAMED = 10500;
    
    /**
     * An action code representing removal of one or more files
     */
    public static final int FILES_REMOVED = 10600;
    
    /**
     * An action code representing removal of one or more files and all
     * historical records of those files
     */
    public static final int FILES_ERADICATED = 10700;
    
    /**
     * An action code representing addition and / or replacement of multiple
     * files as a single action
     */
    public static final int MULTIPLE_FILES_ADDED_OR_REPLACED = 10800;

    /**
     * An action code representing modification of a file description
     */
    public static final int FILE_DESCRIPTION_MODIFIED = 10900;

    /**
     * An action code representing replicating sample information from some
     * other Reciprocal Net site
     */
    public static final int REPLICATED_FROM_ELSEWHERE = 100000;

    /*
     * The following action codes are deprecated values for
     * {@code SampleHistoryInfo.action} and must not be reused. They must
     * nevertheless be defined here to prevent the old references from appearing
     * as 'invalid action codes' in the sample history.
     */
    
    /**
     * A sampleHistory action code representing addition of a reference to a
     * sample. This code is deprecated in the sense that the site software no
     * longer performs any action to which it assigns this code, but the code
     * must be retained to support samples on which this action was performed
     * when the site software <em>did</em> use it.
     */
    public static final int DEPRECATED_REFERENCE_ADDED = 700;
    
    /**
     * A sampleHistory action code representing "raw editing" of a sample. This
     * code is deprecated in the sense that the site software no longer performs
     * any action to which it assigns this code, but the code must be retained
     * to support samples on which this action was performed when the site
     * software <em>did</em> use it.
     */
    public static final int DEPRECATED_RAW_EDITED = 1000;

    /*
     * The next fields define uncorrectability flags.
     * {@code uncorrectableFields} passed to {@code isFieldCorrectable()} is
     * tested against these flags.
     */
    
    /**
     * An uncorrectability bitfield value indicating that nothing (on the page
     * in question) is uncorrectable
     */
    public static final int NOTHING_UNCORRECTABLE = 0;
    
    /**
     * An uncorrectability bit flag to indicate that the empirical formula is
     * uncorrectable on the page in question 
     */
    public static final int EMPIRICAL_FORMULA_UNCORRECTABLE = 1 << 1;
    
    /**
     * An uncorrectability bit flag to indicate that the unit cell parameters
     * are uncorrectable on the page in question 
     */
    public static final int UNIT_CELL_UNCORRECTABLE = 1 << 2;

    /**
     * A Map from action codes to the field codes of non-localtracking fields
     * that are editable as part of the corresponding action. With use of this
     * information, this class makes intelligent decisions about which data
     * values should "belong" to which prior sample action.
     */
    /*
     * This map must be updated whenever field-to-page bindings are changed
     * elsewhere in the application. No entry is necessary for pages/workflow
     * actions that do not affect any non-localtracking fields, and this class
     * provides special handling (elsewhere) for the
     * {@code MODIFIED_TEXT_FIELDS} action. (The {@code MODIFIED_TEXT_FIELDS}
     * action MUST NOT have an entry in this Map.)
     */
    private final static Map<Integer, List<Integer>> eligibleFieldsMap
            = new HashMap<Integer, List<Integer>>();
    
    static {
        // fields on submit.jsp:
        eligibleFieldsMap.put(SUBMITTED,
                Arrays.asList(new Integer[]{
                        SampleTextBL.EMPIRICAL_FORMULA,
                        SampleTextBL.SAMPLE_PROVIDER_NAME,
                        SampleTextBL.PROVIDER_REFERENCE_NUMBER,
                        SampleDataInfo.PROVIDER_ID_FIELD
                        }));
        
        // fields on collectpreliminarydata.jsp:
        eligibleFieldsMap.put(PRELIMINARY_DATA_COLLECTED,
                Arrays.asList(new Integer[]{ 
                        SampleTextBL.CRYSTALLOGRAPHER_NAME,
                        SampleDataInfo.A_FIELD,
                        SampleDataInfo.B_FIELD,
                        SampleDataInfo.C_FIELD,
                        SampleDataInfo.ALPHA_FIELD,
                        SampleDataInfo.BETA_FIELD,
                        SampleDataInfo.GAMMA_FIELD,
                        SampleTextBL.RAW_DATA_URL,
                        SampleDataInfo.T_FIELD,
                        SampleDataInfo.COLOR_FIELD,
                        SampleDataInfo.V_FIELD              /* autocomputed */
                        }));
        
        // fields on refinestructure.jsp:
        eligibleFieldsMap.put(STRUCTURE_REFINED,
                Arrays.asList(new Integer[] { 
                        SampleDataInfo.A_FIELD,
                        SampleDataInfo.B_FIELD,
                        SampleDataInfo.C_FIELD,
                        SampleDataInfo.ALPHA_FIELD,
                        SampleDataInfo.BETA_FIELD,
                        SampleDataInfo.GAMMA_FIELD,
                        SampleDataInfo.SUMMARY_FIELD,
                        SampleDataInfo.Z_FIELD,
                        SampleDataInfo.GOOF_FIELD,
                        SampleDataInfo.SPGP_FIELD,
                        SampleDataInfo.RF_FIELD,
                        SampleDataInfo.RF2_FIELD,
                        SampleDataInfo.RWF_FIELD,
                        SampleDataInfo.RWF2_FIELD,
                        SampleTextBL.EMPIRICAL_FORMULA,
                        SampleTextBL.STRUCTURAL_FORMULA,
                        SampleTextBL.MOIETY_FORMULA,
                        SampleTextBL.SMILES_FORMULA,
                        SampleTextBL.TRADE_NAME,
                        SampleTextBL.COMMON_NAME,
                        SampleTextBL.IUPAC_NAME,
                        SampleDataInfo.V_FIELD,             /* autocomputed */
                        SampleDataInfo.FORMULAWEIGHT_FIELD, /* autocomputed */
                        SampleDataInfo.DCALC_FIELD,         /* autocomputed */
                        }));
        
        // fields on releasetopublic.jsp:
        eligibleFieldsMap.put(RELEASED_TO_PUBLIC,
                Arrays.asList(new Integer[]{ 
                        SampleTextBL.COPYRIGHT_NOTICE,
                        SampleTextBL.LAYMANS_EXPLANATION,
                        SampleTextBL.PREFERRED_NAME
                        }));

        // fields on addcitation.jsp:
        eligibleFieldsMap.put(CITATION_ADDED,
                Arrays.asList(new Integer[]{
                        SampleTextBL.CITATION_OF_A_PUBLICATION
                        }));

        // fields on addcitation.jsp (in correction mode)
        eligibleFieldsMap.put(CITATION_ADDED_CORRECTED,
                Arrays.asList(new Integer[]{
                        SampleTextBL.CITATION_OF_A_PUBLICATION
                        }));

        // fields on retract.jsp:
        eligibleFieldsMap.put(RETRACTED,
                Arrays.asList(new Integer[]{
                        SampleTextBL.SUPERSEDED_BY_ANOTHER_SAMPLE,
                        SampleTextBL.SUPERSEDES_ANOTHER_SAMPLE 
                        }));

        // fields on declareincomplete.jsp:
        eligibleFieldsMap.put(DECLARED_INCOMPLETE,
                Arrays.asList(new Integer[]{
                        SampleTextBL.INCOMPLETENESS_EXPLANATION 
                        }));
        
        // fields on failedtocollect.jsp
        eligibleFieldsMap.put(FAILED_TO_COLLECT,
                Arrays.asList(new Integer[]{
                        SampleTextBL.CRYSTALLOGRAPHER_NAME
                        }));
    }

    /**
     * A static {@code List} containing {@code Integer} representations of all
     * the status codes in an order that tends to correspond with expected
     * chronological sequence.
     */
    private static final List<Integer> allStatusCodes
            = Collections.unmodifiableList(Arrays.asList(new Integer[] {
                    PENDING_STATUS,
                    REFINEMENT_PENDING_STATUS,
                    SUSPENDED_STATUS,
                    INCOMPLETE_STATUS,
                    NOGO_STATUS,
                    NON_SCS_STATUS,
                    RETRACTED_STATUS,
                    WITHDRAWN_STATUS,
                    COMPLETE_STATUS,
                    COMPLETE_PUBLIC_STATUS,
                    INCOMPLETE_PUBLIC_STATUS,
                    NON_SCS_PUBLIC_STATUS,
                    RETRACTED_PUBLIC_STATUS }));

    /**
     * A static {@code Set} containing {@code Integer}
     * representations of all of the status codes that correspond to public
     * states.
     */
    private static final Set<Integer> publicStatusCodes
            = Collections.unmodifiableSet(
                    new HashSet<Integer>(Arrays.asList(new Integer[] {
                            COMPLETE_PUBLIC_STATUS,
                            INCOMPLETE_PUBLIC_STATUS,
                            NON_SCS_PUBLIC_STATUS,
                            RETRACTED_PUBLIC_STATUS})));

    /**
     * A static {@code Set} containing {@code Integer} representations of all of
     * the status codes that correspond to retracted states.
     */
    private static final Set<Integer> retractedStatusCodes
            = Collections.unmodifiableSet(
                    new HashSet<Integer>(Arrays.asList(new Integer[] {
                            RETRACTED_STATUS,
                            RETRACTED_PUBLIC_STATUS})));

    /**
     * A static {@code Set} containing {@code Integer} representations of all of
     * the status codes that correspond to finished states.
     */
    private static final Set<Integer> finishedStatusCodes
            = Collections.unmodifiableSet(new HashSet<Integer>(
                    Arrays.asList(new Integer[] {
                            COMPLETE_STATUS,
                            COMPLETE_PUBLIC_STATUS,
                            INCOMPLETE_STATUS,
                            INCOMPLETE_PUBLIC_STATUS,
                            NON_SCS_STATUS,
                            NON_SCS_PUBLIC_STATUS,
                            RETRACTED_STATUS,
                            RETRACTED_PUBLIC_STATUS })));

    /**
     * <p>
     * Determines whether a particular workflow action may be performed on a
     * sample, based on the sample's current status and on knowledge of the
     * logical sample workflow.
     * </p><p>
     * Note: this function by itself is not capable of evaluating the validity
     * of any of the *_CORRECTED action codes against any particular sample and
     * always returns {@code true} if such an action code is specified.
     * Determining whether one of the *_CORRECTED action codes would be valid
     * for a particular sample is a high-cost operation that depends upon the
     * sample's complete history.
     * </p>
     * 
     * @param sampleStatus the {@code status} value from the current version of
     *        the sample upon which the workflow action would be performed.
     * @param actionCode identifies the kind of workflow action that would be
     *        performed if this function returns true.
     * 
     * @return {@code true} if the specified action code represents an action
     *        that is valid on a sample having the specified status,
     *        {@code false} if not
     */
    public static boolean isActionValid(int sampleStatus, int actionCode) {
        switch (actionCode) {
            // The "main" workflow actions are valid only at particular states.
            case SUBMITTED:
                return sampleStatus == INVALID_STATUS;
            case PRELIMINARY_DATA_COLLECTED:
                return sampleStatus == PENDING_STATUS;
            case STRUCTURE_REFINED:
                return sampleStatus == REFINEMENT_PENDING_STATUS;
            case RESUMED:
                return sampleStatus == SUSPENDED_STATUS;
            case SUSPENDED:
                return sampleStatus == REFINEMENT_PENDING_STATUS;
            case RELEASED_TO_PUBLIC:
                return ((sampleStatus == COMPLETE_STATUS)
                        || (sampleStatus == INCOMPLETE_STATUS)
                        || (sampleStatus == NON_SCS_STATUS));
            case DECLARED_INCOMPLETE:
                return sampleStatus == REFINEMENT_PENDING_STATUS;
            case DECLARED_NON_SCS:
                return sampleStatus == PENDING_STATUS;
            case FAILED_TO_COLLECT:
                return sampleStatus == PENDING_STATUS;
            case WITHDRAWN_BY_PROVIDER:
                return sampleStatus == PENDING_STATUS;

                // Some actions are valid most of the time, but not once the
                // sample has been retracted.
            case MODIFIED_TEXT_FIELDS:
            case MODIFIED_LTAS:
            case CITATION_ADDED:
            case RETRACTED:
            case SUPERSEDED_ANOTHER_SAMPLE:
            case SUBMITTED_CORRECTED:
            case PRELIMINARY_DATA_COLLECTED_CORRECTED:
            case STRUCTURE_REFINED_CORRECTED:
            case RELEASED_TO_PUBLIC_CORRECTED:
            case DECLARED_INCOMPLETE_CORRECTED:
            case DECLARED_NON_SCS_CORRECTED:
            case FAILED_TO_COLLECT_CORRECTED:
            case WITHDRAWN_BY_PROVIDER_CORRECTED:
            case CITATION_ADDED_CORRECTED:
            case SUSPENDED_CORRECTED:
            case RESUMED_CORRECTED:
            case FILE_ADDED:
            case FILE_REPLACED:
            case FILE_RENAMED:
            case FILES_REMOVED:
            case FILES_ERADICATED:
            case MULTIPLE_FILES_ADDED_OR_REPLACED:
            case FILE_DESCRIPTION_MODIFIED:
                return !isRetractedStatusCode(sampleStatus);

                // Some actions are valid all the time, regardless of workflow
                // state.
            case CHANGED_ACL:
            case REVERTED_WITHOUT_FILES:
            case REVERTED_INCLUDING_FILES:
            case IMPORTED:
            case DB_REBUILT:
            case FILE_ADDED_IMPLICITLY:
            case DETECTED_PREEXISTING_FILES:
            case DETECTED_FILE_CHANGES:
                return true;

                // Action codes that are no longer in use never are valid.
            case DEPRECATED_RAW_EDITED:

                // Unknown or unrecognized action codes never are valid.
            default:
                return false;
        }
    }

    /**
     * Static function that determined whether a given int is an action code as
     * defined in this class. A call to this method can verify that the JSP
     * author has provided an actual action code.
     * 
     * @param actionCode an int, intended to indicate one of the actions defined
     *        in this class.
     * @return {@code true} if it is a known action code, {@code false} if not
     */
    public static boolean isValidActionCode(int actionCode) {
        switch (actionCode) {
            case SUBMITTED:
            case SUBMITTED_CORRECTED:
            case PRELIMINARY_DATA_COLLECTED:
            case PRELIMINARY_DATA_COLLECTED_CORRECTED:
            case STRUCTURE_REFINED:
            case STRUCTURE_REFINED_CORRECTED:
            case RELEASED_TO_PUBLIC:
            case RELEASED_TO_PUBLIC_CORRECTED:
            case MODIFIED_TEXT_FIELDS:
            case MODIFIED_LTAS:
            case CITATION_ADDED:
            case CITATION_ADDED_CORRECTED:
            case RETRACTED:
            case SUPERSEDED_ANOTHER_SAMPLE:
            case SUSPENDED:
            case SUSPENDED_CORRECTED:
            case RESUMED:
            case RESUMED_CORRECTED:
            case CHANGED_ACL:
            case REVERTED_WITHOUT_FILES:
            case REVERTED_INCLUDING_FILES:
            case IMPORTED:
            case DB_REBUILT:
            case DECLARED_INCOMPLETE:
            case DECLARED_INCOMPLETE_CORRECTED:
            case DECLARED_NON_SCS:
            case DECLARED_NON_SCS_CORRECTED:
            case FAILED_TO_COLLECT:
            case FAILED_TO_COLLECT_CORRECTED:
            case WITHDRAWN_BY_PROVIDER:
            case WITHDRAWN_BY_PROVIDER_CORRECTED:
            case DETECTED_PREEXISTING_FILES:
            case DETECTED_FILE_CHANGES:
            case FILE_ADDED:
            case FILE_ADDED_IMPLICITLY:
            case FILE_REPLACED:
            case FILES_REMOVED:
            case FILES_ERADICATED:
            case MULTIPLE_FILES_ADDED_OR_REPLACED:
            case FILE_DESCRIPTION_MODIFIED:
            case REPLICATED_FROM_ELSEWHERE:
                return true;
            default:
                return false;
        }
    }

    /**
     * This method may be used to determine whether any {@code UserInfo} objects
     * must be passed to {@code alterSampleForWorkflowAction()}. If this method
     * returns false, the user may avoid fetching {@code UserInfo} objects.
     * 
     * @param sample the sample on which a workflow action will be performed
     * @param actionCode the action that will be performed
     * @param originalProvider the provider associated with the sample before it
     *        was modified
     * @return true if callers of {@code alterSampleForWorkflowAction()} need to
     *         provide {@code UserInfo} objects as normal, or false if they may
     *         be omitted in this particular case
     */
    public static boolean doesActionRequireProviderUsers(SampleInfo sample,
            @SuppressWarnings("unused") int actionCode,
            ProviderInfo originalProvider) {
        if ((originalProvider == null) 
                || (originalProvider.id == sample.dataInfo.providerId)) {
            return false;
        } else {
            for (SampleAccessInfo access : sample.accessInfo) {
                if (access.accessLevel == SampleAccessInfo.READ_ONLY) {
                    return true;
                }
            }
            
            return false;
        }
    }

    /**
     * If the specified action code demands it, sets a new status on the
     * specified sample, based on the action code and the sample's current
     * state. Generally, the status will be set if the action code is
     * distinguishable. This method may also update other aspects of the sample
     * as appropriate for the workflow action performed.
     * 
     * @param sample the sample to modify
     * @param actionCode the action being performed on the sample
     * @throws IllegalArgumentException in those cases when
     *         {@link #doesActionRequireProviderUsers(SampleInfo, int, ProviderInfo)
     *         doesActionRequireProviderUsers()} would return {@code true}.
     */
    public static void alterSampleForWorkflowAction(SampleInfo sample,
            int actionCode) {
        alterSampleForWorkflowAction(sample, actionCode, null);
    }
    
    /**
     * If the specified action code demands it, sets a new status on the
     * specified sample, based on the action code and the sample's current
     * state. Generally, the status will be set if the action code is
     * distinguishable. This method may also update other aspects of the sample
     * as appropriate for the workflow action performed.
     * 
     * @param sample the sample to modify
     * @param actionCode the action being performed on the sample
     * @param providerUsers a mapping from user IDs to the corresponding
     *        {@code UserInfo} objects for every provider user associated with
     *        the current (new) provider for the sample. For convenience,
     *        extranious provider users may be included. This argument may be
     *        {@code null} if {@link
     *        SampleWorkflowBL#doesActionRequireProviderUsers(SampleInfo, int,
     *        ProviderInfo) doesActionRequireProviderUsers()} returns
     *        {@code false}.
     * @throws IllegalArgumentException if no 'providerUsers' were given when
     *         {@code doesActionRequireProviderUsers()} returns {@code true}
     */
    public static void alterSampleForWorkflowAction(SampleInfo sample,
            int actionCode, Map<Integer, UserInfo> providerUsers) {

        /*
         * if during this workflow action, sample names were changed that cause
         * the 'PREFERRED_NAME' annotation to be invalid, the annotation will be
         * silently unset
         */
        SampleAnnotationInfo preferredSampleName
                = SampleTextBL.getExplicitlyPreferredName(sample);
        
        if (preferredSampleName != null) {
            boolean foundPreferredName = false;
            
            for (SampleTextInfo name
                    : SampleTextBL.getSampleNames(sample, false)) {
                if (name.value.equals(preferredSampleName.value)) {
                    foundPreferredName = true;
                    break;
                }
            }
            if (!foundPreferredName) {
                // silently remove the invalidated preferred name annotation
                sample.annotationInfo.remove(preferredSampleName);
            }
        }

        switch (actionCode) {
            case SUBMITTED:
                sample.status = PENDING_STATUS;
                break;
            case SUBMITTED_CORRECTED:
                break;
            case PRELIMINARY_DATA_COLLECTED:
                sample.status = REFINEMENT_PENDING_STATUS;
            case PRELIMINARY_DATA_COLLECTED_CORRECTED:
                SampleMathBL.calculateVolume(sample.dataInfo);
                break;
            case STRUCTURE_REFINED:
                sample.status = COMPLETE_STATUS;
            case STRUCTURE_REFINED_CORRECTED:
                /*
                 * order is important here: density must be calculated after
                 * volume and weight.
                 */
        	    SampleMathBL.calculateVolume(sample.dataInfo);
        	    SampleMathBL.calculateFormulaWeight(sample);
                SampleMathBL.calculateDensity(sample.dataInfo);
                break;
            case RELEASED_TO_PUBLIC:
                if (sample.status == COMPLETE_STATUS) {
                    sample.status = COMPLETE_PUBLIC_STATUS;
                } else if (sample.status == INCOMPLETE_STATUS) {
                    sample.status = INCOMPLETE_PUBLIC_STATUS;
                } else if (sample.status == NON_SCS_STATUS) {
                    sample.status = NON_SCS_PUBLIC_STATUS;
                }
                break;
            case RELEASED_TO_PUBLIC_CORRECTED:
                break;
            case MODIFIED_TEXT_FIELDS:
            case MODIFIED_LTAS:
            case CITATION_ADDED:
            case CITATION_ADDED_CORRECTED:
                break;
            case SUSPENDED:
                sample.status = SUSPENDED_STATUS;
            case SUSPENDED_CORRECTED:
                break;
            case RESUMED:
                sample.status = REFINEMENT_PENDING_STATUS;
            case RESUMED_CORRECTED:
                break;
            case RETRACTED:
                sample.status = (sample.isPublic()
                        ? RETRACTED_PUBLIC_STATUS : RETRACTED_STATUS);
                break;
            case REVERTED_WITHOUT_FILES:
            case REVERTED_INCLUDING_FILES:
            case SUPERSEDED_ANOTHER_SAMPLE:
            case CHANGED_ACL:
            case IMPORTED:
            case DB_REBUILT:
                break;
            case DECLARED_INCOMPLETE:
                sample.status = INCOMPLETE_STATUS;
            case DECLARED_INCOMPLETE_CORRECTED:
                break;
            case DECLARED_NON_SCS:
                sample.status = NON_SCS_STATUS;
            case DECLARED_NON_SCS_CORRECTED:
                break;
            case FAILED_TO_COLLECT:
                sample.status = NOGO_STATUS;
            case FAILED_TO_COLLECT_CORRECTED:
                break;
            case WITHDRAWN_BY_PROVIDER:
                sample.status = WITHDRAWN_STATUS;
            case WITHDRAWN_BY_PROVIDER_CORRECTED:
                break;
            case DETECTED_PREEXISTING_FILES:
            case DETECTED_FILE_CHANGES:
            case FILE_ADDED:
            case FILE_ADDED_IMPLICITLY:
            case FILE_RENAMED:
            case FILE_REPLACED:
            case FILES_REMOVED:
            case FILES_ERADICATED:
            case MULTIPLE_FILES_ADDED_OR_REPLACED:
            case FILE_DESCRIPTION_MODIFIED:
            case REPLICATED_FROM_ELSEWHERE:
                break;
            default:
                throw new IllegalArgumentException();
        }

        // The sample status has been updated, now update the ACL accordingly
        updateSampleAcl(sample, providerUsers);
    }

    /**
     * <p>
     * A helper function that should be called when a sample's state is
     * changed (but before it is written to the database) to update the
     * sample's ACL as needed.
     * </p><p>
     * In the current implementation for the case of a public sample, all users
     * with READ_ONLY access are removed from the list because the sample is
     * globally readable. For non-public samples, any supplied providerUsers
     * from the samples new provider with READ_ONLY access are removed.
     * </p>
     * 
     * @param sample the sample whose ACL is to be modified
     * @param providerUsers a mapping from userId's to their
     *     {@code UserInfo} object that minimally contains all provider
     *     users from the sample's provider.  This parameter may be null if
     *     there are no provider users or if it is known that there are no ACL
     *     entries that need to be removed.
     */
    private static void updateSampleAcl(SampleInfo sample,
                                   Map<Integer, UserInfo> providerUsers) {
        boolean isPublic = sample.isPublic();
        Iterator<SampleAccessInfo> accessInfoIterator
                = sample.accessInfo.iterator();
        
        while (accessInfoIterator.hasNext()){
            SampleAccessInfo accessInfo = accessInfoIterator.next();
            
            if (accessInfo.accessLevel == SampleAccessInfo.READ_ONLY) {
                if (isPublic) {
                    accessInfoIterator.remove();
                } else if (providerUsers != null) {
                    UserInfo user = providerUsers.get(accessInfo.userId);
                    
                    if (AuthorizationCheckerBL.isProviderUser(user)
                            && (user.providerId
                                   == sample.dataInfo.providerId)) {
                        accessInfoIterator.remove();
                    }
                }
            }
        }
    }

    /**
     * A helper method that determines whether a particular local tracking field
     * is visible for a given action.
     * 
     * @param field a {@code LocalTrackingConfig} field, defined by the site
     *        admin
     * @param actionCode an action code defined on {@code SampleWorkflowBL}
     * @throws IllegalArgumentException if the action code supplied is not a
     *         valid action code
     * @return a boolean indicating whether or not the particular field should
     *         be visible
     */
    public static boolean isLtaVisibleDuringAction(
            LocalTrackingConfig.Field field, int actionCode) {
        switch (actionCode) {
            case SUBMITTED:
            case SUBMITTED_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_SUBMIT
                        & field.visibility) != 0;
            case PRELIMINARY_DATA_COLLECTED:
            case PRELIMINARY_DATA_COLLECTED_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_COLLECTPRELIMINARYDATA
                        & field.visibility) != 0;
            case STRUCTURE_REFINED:
            case STRUCTURE_REFINED_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_REFINESTRUCTURE
                        & field.visibility) != 0;
            case RELEASED_TO_PUBLIC:
            case RELEASED_TO_PUBLIC_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_RELEASE_TO_PUBLIC
                        & field.visibility) != 0;
            case FAILED_TO_COLLECT:
            case FAILED_TO_COLLECT_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_FAILEDTOCOLLECT
                        & field.visibility) != 0;
            case DECLARED_NON_SCS:
            case DECLARED_NON_SCS_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_DECLARENONSCS
                        & field.visibility)!= 0;
            case WITHDRAWN_BY_PROVIDER:
            case WITHDRAWN_BY_PROVIDER_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_WITHDRAW
                        & field.visibility) != 0;
            case DECLARED_INCOMPLETE:
            case DECLARED_INCOMPLETE_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_DECLAREINCOMPLETE
                        & field.visibility) != 0;
            case SUSPENDED:
            case SUSPENDED_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_SUSPEND
                        & field.visibility) != 0;
            case RESUMED:
            case RESUMED_CORRECTED:
                return (LocalTrackingConfig.VISIBLE_ON_RESUME
                        & field.visibility) != 0;
            case RETRACTED:
                return (LocalTrackingConfig.VISIBLE_ON_RETRACT
                        & field.visibility) != 0;
            case MODIFIED_LTAS:
                return (LocalTrackingConfig.VISIBLE_ON_CUSTOMFIELDS
                        & field.visibility) != 0;
            case MODIFIED_TEXT_FIELDS:
            case CITATION_ADDED:
            case CITATION_ADDED_CORRECTED:
            case CHANGED_ACL:
            case REVERTED_WITHOUT_FILES:
            case REVERTED_INCLUDING_FILES:
            case IMPORTED:
            case DB_REBUILT:
            case DETECTED_PREEXISTING_FILES:
            case DETECTED_FILE_CHANGES:
            case FILE_ADDED:
            case FILE_ADDED_IMPLICITLY:
            case FILE_REPLACED:
            case FILES_REMOVED:
            case FILES_ERADICATED:
            case MULTIPLE_FILES_ADDED_OR_REPLACED:
            case FILE_DESCRIPTION_MODIFIED:
            case REPLICATED_FROM_ELSEWHERE:
                // currently there is no visibility constant associated with
                // these action codes
                return false;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Reveals which sample data field types are "eligible for display" as part
     * of a particular workflow action.  These are all the data fields that
     * "could be changed" during the workflow action (absent other
     * considerations like correctability, the sample's state, etc.); such
     * fields might be represented in the UI as editable text boxes on the
     * workflow action's form.
     *
     * @return a {@code Set} of {@code Integer}'s, each of which
     *     represents a field code.  Each value is a field code constant
     *     defined on either {@code SampleDataInfo} or
     *     {@code SampleTextInfo}.
     * @param actionCode identifies the workflow action being queried.  This
     *     value should be one of the workflow action constants defined on
     *     {@code SampleWorkflowBL}.
     * @param ltc the {@code LocalTrackingConfig} object for the lab
     *     associated with the sample being queried.  This is necessary so that
     *     run-time fields (i.e. localtracking fields or "custom fields") can
     *     be included (as appropriate) in the returned set of field codes.
     *     If this is null, no localtracking fields are represented in the
     *     returned set.
     */
    public static Set<Integer> getEligibleFieldCodes(int actionCode,
            LocalTrackingConfig ltc) {
        
        /*
         * FIXME: this method is employed by inner loops elsewhere. Performance
         * might be improved considerably if returned values were cached and
         * reused.
         */

        Set<Integer> fieldCodes = new HashSet<Integer>();

        /*
         * Search for any information about the specified actionCode that was
         * provided to this class at development time.
         */

        List<Integer> eligibleCodes = eligibleFieldsMap.get(actionCode);

        if (eligibleCodes != null) {
            fieldCodes.addAll(eligibleCodes);
        } else if (actionCode == MODIFIED_TEXT_FIELDS) {
            
            /*
             * Special handling if the specified action is MODIFIED_TEXT_FIELDS:
             * all attribute/annotation codes are eligible to be displayed on
             * this kind of action page unless they're displayed elsewhere.
             */
            
            fieldCodes.addAll(SampleTextBL.getAllTextTypes());

            for (Integer eligibleFieldsAction
                    : SampleWorkflowBL.eligibleFieldsMap.keySet()) {
                fieldCodes.removeAll(getEligibleFieldCodes(
                        eligibleFieldsAction, ltc));
            }
        }

        /*
         * If we have a LocalTrackingConfig object, insert some additional
         * fields into our set.
         */
        if (ltc != null) {
            fieldCodes.addAll(ltc.getTextTypesForAction(actionCode));
        }

        return fieldCodes;
    }

    /**
     * Determine whether or not the specified value is among the status codes
     * designating public samples
     * 
     * @param status the status code to consider
     * @return {@code true} if the specified value is a public status code,
     *         {@code false} if not
     */
    public static boolean isPublicStatusCode(int status) {
        return publicStatusCodes.contains(Integer.valueOf(status));
    }

    /**
     * Determine whether or not the specified value is among the status codes
     * designating finished samples
     * 
     * @param status the status code to consider
     * @return {@code true} if the specified value is a finished status code,
     *         {@code false} if not
     */
    public static boolean isFinishedStatusCode(int status) {
        return finishedStatusCodes.contains(Integer.valueOf(status));
    }

    /**
     * Determine whether or not the specified value is among the status codes
     * designating retracted samples
     * 
     * @param status the status code to consider
     * @return {@code true} if the specified value is a retracted status code,
     *         {@code false} if not
     */
    public static boolean isRetractedStatusCode(int status) {
        return retractedStatusCodes.contains(Integer.valueOf(status));
    }

    /**
     * Gets a {@code List} containing {@code Integer} representations of each
     * status code
     * 
     * @return an unmodifiable {@code List} of all the recognized status codes,
     *         in approximate order of the sequence in which samples typically
     *         progress throught them (though samples generally don't generally
     *         attain every inidividual code, but rather skip)
     */
    public static List<Integer> getAllStatusCodes() {
        return allStatusCodes;
    }

    /**
     * Gets a {@code Collection} containing {@code Integer} representations of
     * each public status code.
     * 
     * @return an unmodifiable {@code Collection} of all the public status codes
     */
    public static Collection<Integer> getAllPublicStatusCodes() {
        return publicStatusCodes;
    }

    /**
     * Gets a {@code Collection} containing {@code Integer} representations of
     * each public status code.
     * 
     * @return an unmodifiable {@code Collection} of all the finished status
     *         codes
     */
    public static Collection<Integer> getAllFinishedStatusCodes() {
        return finishedStatusCodes;
    }

    /**
     * Gets a {@code Collection} containing {@code Integer} representations of
     * each public status code.
     * 
     * @return an unmodifiable {@code Collection} of all the retracted status
     *         codes
     */
    public static Collection<Integer> getAllRetractedStatusCodes() {
        return retractedStatusCodes;
    }

    /**
     * Determines whether a particular UI form field should be editable or
     * non-editable in correction mode, given a specified bitfield
     * representation of the applicable correctability exceptions.
     * 
     * @param fieldCode identifies the kind of value that could be changed by
     *        the form field in question if it were editable. Should be one of
     *        the constants defined on {@code SampleDataInfo} or
     *        {@code SampleTextInfo}.
     * @param uncorrectableFields should be the a bitfield of field
     *        correctability exceptions, such as marked on a particular
     *        {@code ActionRecord} returned by {@link #getActionRecords
     *        getActionRecords()}. The meaning of this integer is opaque to the
     *        caller.
     * @return {@code true} if the field is correctable, {@code false} if not
     */
    public static boolean isFieldCorrectable(int fieldCode,
            int uncorrectableFields) {
        switch (fieldCode) {
            case SampleInfo.ID:
            case SampleInfo.LAB_ID:
            case SampleInfo.LOCAL_LAB_ID:
            case SampleInfo.MOST_RECENT_PROVIDER_ID:
            case SampleInfo.STATUS:
            case SampleInfo.MOST_RECENT_STATUS:
            case SampleInfo.HISTORY_ID:
            case SampleInfo.MOST_RECENT_HISTORY_ID:
            case SampleDataInfo.DCALC_FIELD:
                // the previous fields should never be editable in correction
                // mode
                return false;
            case SampleTextBL.EMPIRICAL_FORMULA:
                return (uncorrectableFields & EMPIRICAL_FORMULA_UNCORRECTABLE)
                        == 0;
            case SampleDataInfo.A_FIELD:
            case SampleDataInfo.B_FIELD:
            case SampleDataInfo.C_FIELD:
            case SampleDataInfo.ALPHA_FIELD:
            case SampleDataInfo.BETA_FIELD:
            case SampleDataInfo.GAMMA_FIELD:
                return (uncorrectableFields & UNIT_CELL_UNCORRECTABLE)
                        == 0;
            default:
                return true;
        }
    }

    /**
     * Using the {@code FullSampleInfo}, {@code LocalTrackingConfig}, and the
     * collection {@code RepositoryFileInfo} objects supplied, generates a
     * corresponding set of actions, initialized for subsequent querying and
     * iteration. This procedure is comparatively complicated and expensive,
     * though it does not require any (further) database access.
     * 
     * @param fsi a {@code FullSampleInfo} representing the complete metadata
     *        records for the sample to analyze
     * @param ltc the {@code LocalTrackingConfig} object for the specified
     *        sample's originating laboratory
     * @param files a {@code Collection} of {@code RepositoryFileInfo} objects
     *        representing all repository files ever associated with the
     *        specified sample; it is not sensible for this collection to
     *        contain duplicates, but this method is not adversely affected if
     *        it does
     * @return an unmodifiable {@code List} of {@code ActionRecord} objects
     *         describing all the actions ever performed on the specified
     *         sample, with no duplicates and in order by sample history ID
     */
    public static List<ActionRecord> getActionRecords(FullSampleInfo fsi,
            LocalTrackingConfig ltc, Collection<RepositoryFileInfo> files) {
        if ((fsi == null) || (ltc == null) || (files == null)) {
            throw new IllegalStateException();
        }

        ActionInfo actionInfo = getActions(fsi);
        
        markActionsSkippedByReversion(actionInfo);
        markDistinguishedActions(actionInfo);
        bindFieldsToActions(actionInfo, getFieldSetForCurrentVersion(fsi), ltc);
        bindFilesToActions(actionInfo, files);
        addMissingCompulsoryFields(actionInfo, ltc);
        
        // Nothing should be correctable if the sample is retracted
        if (!isRetractedStatusCode(fsi.mostRecentStatus)) {
            markCorrectableActions(actionInfo);
        }

        return actionInfo.getAllActions();
    }

    /**
     * Returns a {@code List} of {@code ActionRecord}s, with one entry for each
     * sample history entry within the specified {@code FullSampleInfo} object.
     * All fields of the created {@code ActionRecord}s are assigned their
     * default values, except the {@code historyInfo} field, which points to the
     * corresponding {@code SampleHistoryInfo} object within the specified
     * {@code FullSampleInfo} object. The List is ordered by sample history ID,
     * and will not contain any duplicates.
     * 
     * @param fullSample the {@code FullSampleInfo} for which a corresponding
     *        action list is requested
     * @return a {@code List} of {@code ActionRecord}s corresponding to the
     *         sample history entries, in order by sample history ID
     */
    private static ActionInfo getActions(
            FullSampleInfo fullSample) {
        Set<ActionRecord> actions = new HashSet<ActionRecord>();

        for (SampleHistoryInfo hist : fullSample.history) {
            actions.add(new ActionRecord(hist));
        }

        return new ActionInfo(actions);
    }

    /**
     * Turns on the {@code wasSkippedByReversion} flag of each
     * {@code ActionRecord} in the specified action information that was skipped
     * due to a subsequent reversion to a sample version prior to the action's
     * time stamp.
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far; its list of all relevant
     *        {@code ActionRecord}s must be populated in ascending order by
     *        sampleHistoryID, and only reversions represented within this list
     *        will be recognized
     */
    private static void markActionsSkippedByReversion(ActionInfo actionInfo) {
        
        /*
         * This implementation steps through the set of actions in order
         * from most recent to least recent and marks as skipped all those
         * between a reversion action and the action whose history ID is the
         * revert action's {@code SampleHistoryInfo.revertedToSampleHistoryid}.
         */
        
        List<ActionRecord> actions = actionInfo.getAllActions();
        int skipActionsUntil = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        boolean currentlySkippingActions = false;
        boolean skipFileActionsToo = false;
        
        for (ListIterator<ActionRecord> actionIterator
                = actions.listIterator(actions.size()); 
                actionIterator.hasPrevious(); ) {
            ActionRecord action = actionIterator.previous();
            
            if (currentlySkippingActions
                    && (action.historyInfo.id != skipActionsUntil)) {
                assert (action.historyInfo.id > skipActionsUntil)
                        : "Missed a reversion boundary";
                action.wasSkippedByReversion
                        = (skipFileActionsToo || !action.isFileAction());
            } else {
                // the default for an ActionRecord.wasSkippedByReversion is
                // false and should remain so in this case
                assert !action.wasSkippedByReversion;
                
                if (action.historyInfo.action
                        == SampleWorkflowBL.REVERTED_WITHOUT_FILES) {
                    // current action is a reversion without files --
                    // start skipping all non-file actions
                    skipActionsUntil
                            = action.historyInfo.revertedToSampleHistoryId;
                    assert skipActionsUntil
                            != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
                    currentlySkippingActions = true;
                    skipFileActionsToo = false;
                } else if (action.historyInfo.action
                        == SampleWorkflowBL.REVERTED_INCLUDING_FILES) {
                    // current action is a reversion including files --
                    // start skipping all actions
                    skipActionsUntil
                            = action.historyInfo.revertedToSampleHistoryId;
                    assert skipActionsUntil
                            != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
                    currentlySkippingActions = true;
                    skipFileActionsToo = true;
                } else {
                    currentlySkippingActions = false;
                }
            }
        }
    }

    /**
     * <p>
     * Iterates through all the {@code ActionRecord}s among the provided action
     * information to find those records that are 'distinguished'. Distinguished
     * records that are found have their {@code isDistinguished} field set to
     * true. Subsequent corrections to those distinguished actions are found and
     * bound to the appropriate distinguished action.
     * </p><p>
     * A 'distinguished' action record is one whose type would allow it to be
     * distinguished (as determined by
     * {@link ActionRecord#couldBeDistinguished()}), and that is also the first
     * action record of its type on its sample that hasn't been flagged as
     * 'skippedByReversion'. All subsequent actions of the same type, or of the
     * associated *_CORRECTION type, that aren't flagged as 'skippedByReversion'
     * are treated as subsequent corrections to the distinguished action rather
     * than <i>bona fide</i> actions in their own right. Contrast this
     * arrangement with normal (non-distinguished) actions, which are unrelated
     * to one another and are not correctable in the same sense.
     * </p><p>
     * The current implementation iterates through the list, detecting
     * distinguished action records as it goes. Each distinguished action's
     * {@code ActionRecord.isDistinguished} flag is set. Every other action
     * record in the set is evaluated to see if it could be a correction to a
     * distinguished action detected previously (according to
     * {@code ActionRecord.couldBeCorrection()}). Each such correction action
     * record is linked to the corresponding previous distinguished action
     * record that it corrects. For each such link, the
     * {@code ActionRecord.correctsAction} field on the correction action record
     * is set to point to the distinguished {@code ActionRecord}, and a pointer
     * to the correction {@code ActionRecord} is added to the distinguished
     * action record's {@code correctingActions} set.
     * </p>
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far. The list of all action records should
     *        be populated in ascending order by sampleHistory ID, and any
     *        actions skipped by subsequent reversion should already so marked
     *        (as by {@link #markActionsSkippedByReversion(ActionInfo)}. The
     *        action map and distinguished action list of this information are
     *        populated by this method, the latter in descending order by
     *        sample history ID; they should initially be empty.
     */
    private static void markDistinguishedActions(ActionInfo actionInfo) {
        
        /*
         * Temporary table for efficiency. Maps Integers (action codes) to the
         * distinguished ActionRecord object. There's at most one distinguished
         * ActionRecord per action code, but not every action code has a
         * distinguished action.
         */
        Map<Integer, ActionRecord> distinguishedActionMap
                = new HashMap<Integer, ActionRecord>();
        
        Map<Integer, ActionRecord> allActionMap = actionInfo.getActionMap();
        List<ActionRecord> distinguishedActions
                = actionInfo.getDistinguishedActions();

        for (ActionRecord action : actionInfo.getAllActions()) {
            allActionMap.put(Integer.valueOf(action.historyInfo.id), action);
            
            if (!action.wasSkippedByReversion) {
                ActionRecord distinguishedAction
                        = distinguishedActionMap.get(Integer.valueOf(
                                action.getActionCodeCorrected()));
                
                if (action.couldBeDistinguished()
                        && (distinguishedAction == null)) {
                    /*
                     * Flag the current action as being distinguished -- it's
                     * the first action of its type to be encountered.
                     */
                    action.isDistinguished = true;
                    distinguishedActionMap.put(
                            Integer.valueOf(action.historyInfo.action), action);
                    distinguishedActions.add(0, action);
                } else if (action.couldBeCorrection()
                        && (distinguishedAction != null)) {
                    /*
                     * The current action is a correction to a previous
                     * distinguished action. Create links between the two
                     * ActionRecords.
                     */
                    action.correctsAction = distinguishedAction;
                    distinguishedAction.correctingActions.add(action);
                }
            }
        }
    }

    /**
     * Returns a set of all the data field values populated on the most recent
     * version of the specified sample. This includes all populated fields from
     * the sample's {@code SampleDataInfo} object, all values in
     * {@code SampleAttributeInfo} objects in the sample's attribute list, and
     * all values in {@code SampleAnnotationInfo} objects in the sample's
     * annotation list.
     * 
     * @return a {@code Set} of {@code FieldRecord}s, each of which contains a
     *         field value from the most recent version of the sample and its
     *         corresponding field code.
     * @param fullSample the {@code FullSampleInfo} object from which field
     *        values are to be extracted. Only the {@code SampleInfo} object
     *        inside the last {@code SampleHistoryInfo} object in the object's
     *        {@code history} list is consulted.
     */
    private static Set<FieldRecord> getFieldSetForCurrentVersion(
            FullSampleInfo fullSample) {
        Set<FieldRecord> fields = new HashSet<FieldRecord>();

        // Find the most current SampleInfo within the FullSampleInfo object we
        // were provided.
        SampleHistoryInfo lastSampleHistory =
                fullSample.history.get(fullSample.history.size() - 1);
        SampleInfo sample = lastSampleHistory.sample;

        // Extract all the values from SampleDataInfo.
        for (Entry<Integer, Object> entry
                : sample.dataInfo.getFieldsMap().entrySet()) {
            fields.add(new FieldRecord(entry.getKey(), entry.getValue(),
                    sample.dataInfo.originalSampleHistoryId));
        }

        // Extract all values from the attributes list.
        for (SampleAttributeInfo attr : sample.attributeInfo) {
            fields.add(new FieldRecord(attr.type, attr,
                    attr.originalSampleHistoryId));
        }

        // Extract all values from the annotations list.
        for (SampleAnnotationInfo ann : sample.annotationInfo) {
            fields.add(new FieldRecord(ann.type, ann,
                    ann.originalSampleHistoryId));
        }

        return fields;
    }

    /**
     * <p>
     * Binds each field record in a supplied set of field records to the most
     * appropriate action record within the supplied action information.
     * Decisions are made within this function with regard to which actions are
     * "most appropriate" for which fields. (Presumably, when a field is bound
     * to an action this implies that field's value will be displayed in a UI as
     * "belonging" to the action to which it's bound.)
     * </p><p>
     * For each field, the current implementation scans the distinguished
     * actions linearly (from newest to oldest) in search of one with respect to
     * which the field is "eligible for display", and adds the field to the
     * first (most recent) one encountered. If no distinguished action record is
     * eligible to display the field, then it is assigned to the one
     * corresponding to its associated original sampleHistory ID.
     * </p>
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far. The action map from sampleHistory IDs
     *        to {@code ActionRecord}s and the list of {@code ActionRecord}s
     *        for distinguished actions (in descending order by sampleHistory
     *        ID) should both be populated. {@code FieldRecord} objects are
     *        added to the {@code ActionRecord.fields} set on select entries.
     * @param fields a {@code Set} of {@code FieldRecord} objects that
     *        represents all data values/attributes/annotations from the current
     *        version of the sample. References to these objects are inserted
     *        into the elements of the {@code actions} set as described above,
     *        but this {@code fields} set is not modified.
     * @param ltc the {@code LocalTrackingConfig} object by which to evaluate to
     *        which actions particular local tracking fields may be bound;
     *        this generally should be the config for the laboratory that
     *        originated the sample being analyzed
     */
    private static void bindFieldsToActions(ActionInfo actionInfo,
            Set<FieldRecord> fields, LocalTrackingConfig ltc) {
        List<ActionRecord> distinguishedActions
                = actionInfo.getDistinguishedActions();
        Map<Integer, ActionRecord> actionsMap = actionInfo.getActionMap();
        
        /*
         * Iterate through all fields (supplied by the caller) and bind each one
         * to an action.
         */
        each_field:
        for (FieldRecord field : fields) {
            Integer fieldCode = Integer.valueOf(field.getFieldCode());
            
            /*
             * Iterate through all distinguished actions (in reverse history ID
             * order), find the first one eligible to display the current
             * field, and bind the current field to it. This accounts for the
             * fact that some fields in SampleDataInfo are editable on multiple
             * JSPs.  If successful then we're done with this field.
             */
            for (ActionRecord distinguishedAction : distinguishedActions) {
                if (SampleWorkflowBL.getEligibleFieldCodes(
                        distinguishedAction.historyInfo.action,
                        ltc).contains(fieldCode)) {
                    distinguishedAction.fields.add(field);
                    continue each_field;
                }
            }

            /*
             * Couldn't find a distinguished action to bind the field to, so
             * bind it to the action corresponding to the moment the value
             * first came into existence (or to the action that action
             * corrects, if it's a correction).
             */
            ActionRecord actionToBindTo = actionsMap.get(
                    Integer.valueOf(field.originalSampleHistoryId));
                
            while (actionToBindTo.correctsAction != null) {
                actionToBindTo = actionToBindTo.correctsAction;
            }
            
            actionToBindTo.fields.add(field);
        }
    }

    /**
     * <p>
     * Binds each supplied file to the action that resulted in the file-version
     * being added, and to the action that resulted in the file-version being
     * removed if it's no longer present in the current version of the sample.
     * If a filename was added and removed during the same action, it is
     * considered to be neither added nor removed, but rather modified.
     * </p><p>
     * The current implementation iterates through the files and attaches a new
     * {@code FileRecord} to the {@code ActionRecord} for the action during
     * which the file version was added, removed or modified.
     * </p>
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far. At minimum, this information's action
     *        map from sampleHistory IDs to {@code ActionRecords} should have
     *        been populated.
     * @param files a {@code Collection} of {@code RepositoryFileInfo} objects
     *        that represents all file versions from all versions of the sample.
     *        References to these files are inserted into the
     *        {@code ActionRecord}s to provide useful information about the
     *        addition/removal of files.
     */
    private static void bindFilesToActions(ActionInfo actionInfo,
            Collection<RepositoryFileInfo> files) {
        Map<Integer, ActionRecord> actionMap = actionInfo.getActionMap();

        for (RepositoryFileInfo file : files) {
            ActionRecord fileAddAction = actionMap.get(
                    Integer.valueOf(file.firstSampleHistoryId));

            if (fileAddAction == null) {
                // the file was added since the FullSampleInfo was fetched and
                // cannot be bound to any of its actions
            } else {
                FileRecord addedFile = new FileRecord(fileAddAction, file);

                if (fileAddAction.filesRemoved.remove(addedFile)) {
                    // A file with the same name was removed during the action;
                    // it should be inserted into the filesModified collection.
                    fileAddAction.filesModified.add(addedFile);
                } else {
                    fileAddAction.filesAdded.add(addedFile);
                }
            }
            if (file.lastSampleHistoryId
                    != SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID) {
                ActionRecord fileRemoveAction = actionMap.get(
                        Integer.valueOf(file.lastSampleHistoryId));

                if (fileRemoveAction == null) {
                    // the file has been removed since the FullSampleInfo was
                    // fetched and cannot be bound to any of its action
                } else {
                    FileRecord removedFile = new FileRecord(fileRemoveAction,
                            file);

                    if (fileRemoveAction.filesAdded.remove(removedFile)) {
                        /*
                         * A file with the same name was added during the
                         * action; it should be inserted into the filesModified
                         * collection.
                         */
                        fileRemoveAction.filesModified.add(removedFile);
                    } else {
                        fileRemoveAction.filesRemoved.add(removedFile);
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Ensures that the actions in the specified list collectively provide
     * values for all the <i>compulsory</i> field types that they demand.
     * </p><p>
     * For each compulsory field code not already represented by a field within
     * the action list, a new 'empty' field record (one having a {@code value}
     * of {@code null}) is generated and appended to the field list of the last
     * distinguished action record eligible to display it.
     * </p>
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far. Its action records should already have
     *        had appropriate {@code FieldRecord}s bound to them, as by
     *        {@link #bindFieldsToActions(ActionInfo, Set, LocalTrackingConfig)},
     *        and its distinguished action list should have already been
     *        populated with the distinguished among its actions, in descending
     *        order by sampleHistory ID. {@code FieldRecord} objects may be
     *        added to the {@code ActionRecord.fields} sets on select
     *        distinguished action entries comprised by this information.
     * @param ltc the {@code LocalTrackingConfig} object for the lab associated
     *        with the sample being queried. This is necessary so that run-time
     *        fields (i.e. localtracking fields or "custom fields") can be
     *        appended as necessary. If this is {@code null}, it must also be
     *        the case that no localtracking field types are returned by
     *        {@code getCompulsoryFieldCodes()}.
     * @see #getCompulsoryFieldCodes(ActionInfo, LocalTrackingConfig)
     */
    private static void addMissingCompulsoryFields(
            ActionInfo actionInfo, LocalTrackingConfig ltc) {
        
        /*
         * Build a set that contains all compulsory field codes that have not
         * yet been bound. Initially, the set will contain all compulsory field
         * codes.
         */
        Set<Integer> unboundCompulsoryFieldCodes
                = getCompulsoryFieldCodes(actionInfo, ltc);

        /*
         * Iterate through all ActionRecords, removing entries from the
         * unboundCompulsoryFieldCodes set as we discover their field codes
         * represented on an ActionRecord.
         */
        for (ActionRecord action : actionInfo.getAllActions()) {
            for (FieldRecord field : action.fields) {
                unboundCompulsoryFieldCodes.remove(
                        Integer.valueOf(field.getFieldCode()));
            }
        }

        // Iterate over the remaining elements of unboundCompulsoryFieldCodes
        each_field:
        for (Integer fieldCode : unboundCompulsoryFieldCodes) {

            /*
             * Find the latest distinguished action eligible to display the
             * current field code.
             */
            for (ActionRecord action : actionInfo.getDistinguishedActions()) {
                if (SampleWorkflowBL.getEligibleFieldCodes(
                        action.historyInfo.action, ltc).contains(fieldCode)) {

                    /*
                     * Insert an empty "dummy" field record on the distinguished
                     * action. Presumably this will signal the UI to indicate
                     * that no value has been entered in this field yet.
                     */
                    action.fields.add(new FieldRecord(fieldCode));
                    continue each_field;
                }
            }
        }
    }

    /**
     * <p>
     * Determines which field codes that are considered <i>compulsory</i> for a
     * particular sample. A user interface that displayed a listing of prior
     * actions to a sample and associated particular fields with each action
     * would be required to display field headings for these compulsory field
     * types, even if no values of that type were available.
     * </p><p>
     * The current implementation assumes that all field types that are eligible
     * for display as part of at least one distinguished action are compulsory.
     * </p>
     * 
     * @return a modifiable {@code Set} of {@code Integer}s representing the
     *         field codes of fields that are compulsory for the specified list
     *         of actions and local tracking configuration; Possible values are
     *         defined by field code constants on {@code SampleDataInfo} and
     *         {@code SampleTextBL}.
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far; at minimum, its list of distinguished
     *        actions must be populated with the {@code ActionRecord}s for the
     *        distinguished actions that have occurred on the sample, in
     *        descending order by sampleHistory ID.
     * @param ltc the {@code LocalTrackingConfig} object for the lab associated
     *        with the sample being queried. This is necessary so that run-time
     *        fields (i.e. localtracking fields or "custom fields") can be
     *        included (as appropriate) in the returned set of field codes. If
     *        this is {@code null}, no localtracking fields are represented in
     *        the returned set.
     */
    private static Set<Integer> getCompulsoryFieldCodes(
            ActionInfo actionInfo, LocalTrackingConfig ltc) {
        Set<Integer> compulsoryFieldCodes = new HashSet<Integer>();
    
        for (ActionRecord action : actionInfo.getDistinguishedActions()) {
            compulsoryFieldCodes.addAll(
                    getEligibleFieldCodes(action.historyInfo.action, ltc));
        }
    
        return compulsoryFieldCodes;
    }

    /**
     * <p>
     * Marks some action records in the supplied action information as
     * being "correctable," in the sense that the UI should display "Correct"
     * buttons associated with them.  The decision is based upon current
     * workflow rules and the actions' action codes.  This method should not be
     * applied to action information for a sample that is (currently) retracted.
     * </p><p>
     * The current implementation:
     * <ol>
     * <li>checks each action record and sets its {@code isCorrectable} flag if
     * that kind of action generally is correctable (as determined by
     * {@code ActionRecord.isCorrectable()}), at least one field is bound to
     * the action record, and the record was not skipped by reversion.</li>
     * <li>for each correctable action record, possibly alters the record's
     * {@code uncorrectableFields} value:
     * <ol>
     * <li>{@code EMPIRICAL_FORMULA_UNCORRECTABLE} is set if the action has
     * type {@code SampleWorkflowBL.SUBMITTED} and there is no field of type
     * {@code SampleTextInfo.EMPIRICAL_FORMULA} bound to the record. This logic
     * is correct because {@code SampleTextInfo.EMPIRICAL_FORMULA} is a
     * compulsory field type, as returned by
     * {@code getCompulsoryFieldCodesSet()}, so if no such field is bound to
     * an action then it must instead be bound to a later action.</li>
     * <li>{@code UNIT_CELL_UNCORRECTABLE} is set if the action has type
     * {@code SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED} and there is no field
     * of type {@code SampleDataInfo.A_FIELD} bound to the record. (It is
     * sufficient to check for a alone because a, b, c, alpha, beta, and gamma
     * fields are always bound together. This logic is correct because
     * {@code A_FIELD}, {@code B_FIELD}, {@code C_FIELD}, {@code ALPHA_FIELD},
     * {@code BETA_FIELD}, and {@code GAMMA_FIELD} all are compulsory field
     * types, as returned by {@code getCompulsoryFieldcodesSet()}.</li>
     * </ol>
     * Special exceptions to the workflow field visibility rules are implemented
     * in this fashion.</li>
     * </ol>
     * </p>
     * 
     * @param actionInfo an {@code ActionInfo} representing the action
     *        information derived so far. At minimum, the {@code ActionRecord}s
     *        it contains should have been marked appropriately where they
     *        represent actions that were skipped by subsequent reversion. Those
     *        actions that are correctable have their {@code isCorrectable}
     *        fields are set {@code true} and may have their
     *        {@code uncorrectableFields} values altered.
     */
    private static void markCorrectableActions(ActionInfo actionInfo) {
        for (ActionRecord action : actionInfo.getAllActions()) {
            if (!action.fields.isEmpty() && action.couldBeCorrectable()
                    && !action.wasSkippedByReversion) {
                action.isCorrectable = true;

                /*
                 * The current action will be correctable.  Possibly prevent
                 * corrections to one or more of its fields, based on
                 * explicit exceptions to the normal workflow rules.
                 */
                
                if ((action.historyInfo.action == SampleWorkflowBL.SUBMITTED)
                        && !action.containsFieldOfType(
                                    SampleTextBL.EMPIRICAL_FORMULA)) {
                    /*
                     * The sample's empirical formula should not be editable
                     * on the submit form if the field type is bound to some
                     * later action record (and thus not bound to this action
                     * record.
                     * 
                     * NOTE: this logic assumes that EMPIRICAL_FORMULA is a
                     *       compulsory field type as returned by
                     *       getCompulsoryFieldCodesSet().
                     */
                    action.uncorrectableFields
                                |= EMPIRICAL_FORMULA_UNCORRECTABLE;
                }
                
                if ((action.historyInfo.action
                                == SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED)
                            && !action.containsFieldOfType(
                                    SampleDataInfo.A_FIELD)) {
                    /*
                     * The sample's unit cell parameters should not be editable
                     * on the collectpreliminarydata form if the field types
                     * are bound to some later action record (and thus not
                     * bound to this action record.
                     * 
                     * NOTE: this logic assumes that A_FIELD, B_FIELD, C_FIELD,
                     *       ALPHA_FIELD, BETA_FIELD, GAMMA_FIELD all are
                     *       compulsory field types as returned by
                     *       getCompusloryFieldCodesSet().
                     */
                    action.uncorrectableFields |= UNIT_CELL_UNCORRECTABLE;
                }
            }
        }
    }

    /**
     * Creates a list of summary information about actions performed on a sample
     * during a particular day (ordered chronologically). Only information about
     * sample versions that are visible by the supplied user are included, and
     * day breaks are based on the system's default time zone.
     * 
     * @param fsi a {@code FullSampleInfo} describing all historical information
     *        about the sample of interest
     * @param user a {@code UserInfo} representing the user for which the
     *        summary is being created
     * @return a {@code List} of {@code SampleActionSummary} objects, in
     *         chronological order. Each one may be considered to represent a
     *         day during which the sample was modified. (the date can be
     *         gleaned from the 'lastActionPerformed' field).
     */
    public static List<SampleActionSummary> getSampleSummaryByDay(
            FullSampleInfo fsi, UserInfo user) {
        List<SampleActionSummary> list = new ArrayList<SampleActionSummary>();
        SampleActionSummary currentDay = null;
        
        for (SampleHistoryInfo historyInfo : fsi.history) {
            if (AuthorizationCheckerBL.canSeeSample(
                    user, historyInfo.sample)) {
                if (currentDay == null) {
                    // the first valid action
                    currentDay = new SampleActionSummary(historyInfo);
                    list.add(currentDay);
                } else if (wereActionsPerformedOnTheSameDay(
                        currentDay.lastActionPerformed, historyInfo)) {
                    // a valid action on the same day as the last valid action
                    currentDay.addAction(historyInfo);
                } else {
                    // a valid action on a different day as the last valid
                    // action
                    currentDay = new SampleActionSummary(historyInfo);
                    list.add(currentDay);
                }
            } else {
                // an action that is not visible to the user
            }
        }

        return list;
    }

    /**
     * Determine whether two actions were performed on the same calendar day.
     * Dates are assumed referenced to the same time zone, and the particular
     * rules of the system's default time zone apply.
     * 
     * @param action1 a {@code SampleHistoryInfo} representing one action to
     *        compare
     * @param action2 a {@code SampleHistoryInfo} representing the other action
     *        to compare
     * @return {@code true} if the two actions occurred on the same day, or
     *         {@code false} if not
     */
    private static boolean wereActionsPerformedOnTheSameDay(
            SampleHistoryInfo action1, SampleHistoryInfo action2) {
        Calendar day1 = Calendar.getInstance();
        Calendar day2 = Calendar.getInstance();
        
        day1.setTime(action1.date);
        day2.setTime(action2.date);
        
        return ((day1.get(Calendar.DAY_OF_YEAR) == day2.get(Calendar.DAY_OF_YEAR))
                && (day1.get(Calendar.YEAR) == day2.get(Calendar.YEAR)));
    }

    /**
     * A class representing a single action performed on a sample and tracking
     * workflow-related information about that action beyond that contained by a
     * {@code SampleHistoryInfo}. This class incorporates workflow-specific
     * knowledge, especially in methods {@link #couldBeCorrectable()},
     * {@link #couldBeCorrection()}, {@link #couldBeDistinguished()},
     * {@link #isFileAction()}, and {@link #getActionCodeCorrected()}.
     */
    public static class ActionRecord implements Comparable<ActionRecord> {
        
        /**
         * A {@code SampleHistoryInfo} describing the action represented by this
         * {@code ActionRecord}
         */
        public final SampleHistoryInfo historyInfo;
        
        /**
         * A flag indicating whether this {@code ActionRecord} represents a
         * 'distinguished' action
         */
        public boolean isDistinguished = false;
        
        /**
         * A flag indicating whether this {@code ActionRecord} represents a
         * correctable action
         */
        public boolean isCorrectable = false;
        
        /**
         * A flag indicating whether this {@code ActionRecord} represents an
         * action that was effectively undone by a subsequent reversion
         */
        public boolean wasSkippedByReversion = false;
        
        /**
         * A bitfield encoding whether certain fields that might be associated
         * with this action should be correctable when this action is corrected
         */
        public int uncorrectableFields = NOTHING_UNCORRECTABLE;

        /**
         * The {@code ActionRecord}, if any, representing the action corrected
         * by this action 
         */
        public ActionRecord correctsAction = null;

        /**
         * A {@code Set} of the {@code ActionRecord}s that correct this action
         */
        public final Set<ActionRecord> correctingActions
                = new HashSet<ActionRecord>();
        
        /**
         * A {@code SortedSet} of {@code FieldRecord}s representing the fields
         * associated with this action; the {@code FieldRecord}s' natural order
         * is used
         */
        public final SortedSet<FieldRecord> fields = new TreeSet<FieldRecord>();

        /**
         * A {@code Collectoin} of {@code FileRecord} objects representing the
         * files added as part of this action
         */
        public final Collection<FileRecord> filesAdded
                = new ArrayList<FileRecord>();

        /**
         * A {@code Collectoin} of {@code FileRecord} objects representing the
         * files removed as part of this action
         */
        public final Collection<FileRecord> filesRemoved
                = new ArrayList<FileRecord>();

        /**
         * A {@code Collectoin} of {@code FileRecord} objects representing the
         * files modified as part of this action
         */
        public final Collection<FileRecord> filesModified
                = new ArrayList<FileRecord>();

        /**
         * Initializes a new {@code ActionRecord} representing the specified
         * sample history information
         * 
         * @param history a {@code SampleHistoryInfo} representing the
         *        historical action to which this action record corresponds
         */
        public ActionRecord(SampleHistoryInfo history) {
            if (history == null) {
                throw new NullPointerException(
                        "The sample history object must not be null");
            } else {
                historyInfo = history;
            }
        }
        
        /**
         * Implements the natural order of {@code ActionRecord}s, which is
         * ascending order by ID of the associated {@code SampleHistoryInfo}
         * 
         * @param x the {@code ActionRecord} to compare to this one
         * @return an {@code int} less than, equal to, or greater than zero as
         *         this {@code ActionRecord} is less than, equal to, or greater
         *         than the specified one
         */
        public int compareTo(ActionRecord x) {
            if (this.historyInfo.id < x.historyInfo.id) {
                return -1;
            } else if (this.historyInfo.id > x.historyInfo.id) {
                return 1;
            } else {
                return 0;
            }
        }

        /**
         * Determines whether this {@code ActionRecord} is equal to the
         * specified object, which is the case if it is also an
         * {@code ActionRecord} and its the sample history ID with which it is
         * associated is equal to the one with which this record is associated
         * 
         * @param o the {@code Object} to compare to this one
         * @return {@code true} if the specified {@code Object} is equal to this
         *         one, {@code false} if not
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ActionRecord) {
                return this.historyInfo.id == ((ActionRecord) o).historyInfo.id;
            } else {
                return false;
            }
        }

        /**
         * Computes and returns a hash code for this record in a manner
         * consistent with {@link #equals(Object)}
         * 
         * @return the hash code of this {@code ActionRecord}
         */
        @Override
        public int hashCode() {
            return this.historyInfo.id;
        }

        /**
         * Determines whether any of the fields associated with this record are
         * of the specified type
         * 
         * @param fieldCode the field code to search for
         * @return {@code true} if at least one field record within
         *         {@link #fields} has {@code fieldCode} as its {@code code},
         *         {@code false} if not
         */
        public boolean containsFieldOfType(int fieldCode) {
            for (FieldRecord field : this.fields) {
                if (field.getFieldCode() == fieldCode) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Determines whether the action represented by this record could be a
         * 'distinguished' action; in this version, the determination is based
         * entirely on the action code of the associated
         * {@code SampleHistoryInfo}.  A positive response from this method
         * should not be taken as conclusive that this record <em>does</em>
         * represent a distinguished action.
         * 
         * @return {@code true} if this record could represent a distinguished
         *         action, {@code false} if not
         */
        public boolean couldBeDistinguished() {
            switch (this.historyInfo.action) {
                case SampleWorkflowBL.SUBMITTED:
                case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED:
                case SampleWorkflowBL.STRUCTURE_REFINED:
                case SampleWorkflowBL.RELEASED_TO_PUBLIC:
                case SampleWorkflowBL.DECLARED_INCOMPLETE:
                case SampleWorkflowBL.DECLARED_NON_SCS:
                case SampleWorkflowBL.FAILED_TO_COLLECT:
                case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Determines whether the action represented by this record could be a
         * correction action; in this version, the determination is based
         * entirely on the action code of the associated
         * {@code SampleHistoryInfo}.  A positive response from this method
         * should not be taken as conclusive that this record <em>does</em>
         * represent a correction action.
         * 
         * @return {@code true} if this record could represent a correction
         *         action, {@code false} if not
         */
        public boolean couldBeCorrection() {
            if (couldBeDistinguished()) {
                // any action that could be distinguished might also be a
                // correction.
                return true;
            }
            switch (this.historyInfo.action) {
                case SampleWorkflowBL.SUBMITTED_CORRECTED:
                case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED_CORRECTED:
                case SampleWorkflowBL.STRUCTURE_REFINED_CORRECTED:
                case SampleWorkflowBL.RELEASED_TO_PUBLIC_CORRECTED:
                case SampleWorkflowBL.DECLARED_INCOMPLETE_CORRECTED:
                case SampleWorkflowBL.DECLARED_NON_SCS_CORRECTED:
                case SampleWorkflowBL.FAILED_TO_COLLECT_CORRECTED:
                case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER_CORRECTED:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Determines whether the action represented by this record could be a
         * correctable action; in this version, the determination is based
         * entirely on the action code of the associated
         * {@code SampleHistoryInfo}.  A positive response from this method
         * should not be taken as conclusive that this record <em>does</em>
         * represent a correctable action.
         * 
         * @return {@code true} if this record could represent a correctable
         *         action, {@code false} if not
         */
        public boolean couldBeCorrectable() {
            if (couldBeDistinguished()) {
                // any action that could be distinguished is correctable.
                return true;
            }
            switch (this.historyInfo.action) {
                // some other action box types also have "correct" buttons,
                // although their correction-like functionality is implemented
                // differently because these actions aren't distinguished.
                case SampleWorkflowBL.CITATION_ADDED:
                case SampleWorkflowBL.CITATION_ADDED_CORRECTED:
                case SampleWorkflowBL.SUSPENDED:
                case SampleWorkflowBL.SUSPENDED_CORRECTED:
                case SampleWorkflowBL.RESUMED:
                case SampleWorkflowBL.RESUMED_CORRECTED:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Determines whether the action represented by this record is an action
         * on the sample's files; in this version, the determination is based
         * entirely on the action code of the associated
         * {@code SampleHistoryInfo}.
         * 
         * @return {@code true} if this record represents an action on the
         *         affected sample's files, {@code false} if not
         */
        public boolean isFileAction() {
            switch (this.historyInfo.action) {
                case SampleWorkflowBL.DETECTED_PREEXISTING_FILES:
                case SampleWorkflowBL.DETECTED_FILE_CHANGES:
                case SampleWorkflowBL.FILE_ADDED:
                case SampleWorkflowBL.FILE_ADDED_IMPLICITLY:
                case SampleWorkflowBL.FILE_REPLACED:
                case SampleWorkflowBL.FILE_RENAMED:
                case SampleWorkflowBL.FILES_REMOVED:
                case SampleWorkflowBL.FILES_ERADICATED:
                case SampleWorkflowBL.MULTIPLE_FILES_ADDED_OR_REPLACED:
                case SampleWorkflowBL.FILE_DESCRIPTION_MODIFIED:
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Returns the action code for the type of action for which the one
         * represented by this record could have been a correction
         * 
         * @return the action code for the type of action for which the action
         *         represented by this record could have been a correction;
         *         {@code SampleWorkflowBL.INVALID_ACTION} if no type of action
         *         could have been corrected by this record's action
         */
        public int getActionCodeCorrected() {
            switch (this.historyInfo.action) {
                case SampleWorkflowBL.SUBMITTED:
                case SampleWorkflowBL.SUBMITTED_CORRECTED:
                    return SampleWorkflowBL.SUBMITTED;
                case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED:
                case SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED_CORRECTED:
                    return SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED;
                case SampleWorkflowBL.STRUCTURE_REFINED:
                case SampleWorkflowBL.STRUCTURE_REFINED_CORRECTED:
                    return SampleWorkflowBL.STRUCTURE_REFINED;
                case SampleWorkflowBL.RELEASED_TO_PUBLIC:
                case SampleWorkflowBL.RELEASED_TO_PUBLIC_CORRECTED:
                    return SampleWorkflowBL.RELEASED_TO_PUBLIC;
                case SampleWorkflowBL.DECLARED_INCOMPLETE:
                case SampleWorkflowBL.DECLARED_INCOMPLETE_CORRECTED:
                    return SampleWorkflowBL.DECLARED_INCOMPLETE;
                case SampleWorkflowBL.DECLARED_NON_SCS:
                case SampleWorkflowBL.DECLARED_NON_SCS_CORRECTED:
                    return SampleWorkflowBL.DECLARED_NON_SCS;
                case SampleWorkflowBL.FAILED_TO_COLLECT:
                case SampleWorkflowBL.FAILED_TO_COLLECT_CORRECTED:
                    return SampleWorkflowBL.FAILED_TO_COLLECT;
                case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER:
                case SampleWorkflowBL.WITHDRAWN_BY_PROVIDER_CORRECTED:
                    return SampleWorkflowBL.WITHDRAWN_BY_PROVIDER;
                default:
                    return SampleWorkflowBL.INVALID_ACTION;
            }
        }
    }

    /**
     * Internal nested class that represents a single field (to be displayed)
     * within the context of an {@code ActionRecord}.  An
     * {@code ActionRecord} normally would contain several such field
     * records to represent the fields that were changed during that action.
     */
    public static class FieldRecord extends SampleFieldRecord
            implements Comparable<FieldRecord> {

        final int originalSampleHistoryId;

        /**
         * Initializes a new {@code FieldRecord} with the specified field code,
         * a {@code null} value, and an invalid original sampleHistoryID
         * 
         * @param code the field code for this {@code FieldRecord}, as an
         *        {@code Integer}
         */
        public FieldRecord(Integer code) {
            this(code, null, SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
        }

        /**
         * Initializes a new {@code FieldRecord} with the specified parameters
         * 
         * @param code the field code for this {@code FieldRecord}, as an
         *        {@code Integer}
         * @param value the field value, as an {@code Object} of class
         *        appropriate for the specified field code
         * @param originalSampleHistoryId the original sampleHistory ID for this
         *        field / value combination, or
         *        {@code SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID} if this
         *        combination does not have a corresponding action in any
         *        sample's history
         */
        public FieldRecord(Integer code, Object value,
                int originalSampleHistoryId) {
            super(code.intValue(), value);
            this.originalSampleHistoryId = originalSampleHistoryId;
        }

        /**
         * The sort order for fields is implemented by this function. The
         * current implementation sorts fields by assending numeric order by
         * their field codes, then by their original sampleHistory IDs, then by
         * their values.  This order is consistent with {@link #equals(Object)
         * equals()} provided that all values' types are consistent with their
         * own {@code equals()} implementations.
         */
        public int compareTo(FieldRecord x) {

            if (this == x) {
                return 0;
            } else {

                /*
                 * FIXME: perhaps fields could be ordered more usefully -- this
                 * particular implementation has no special significance
                 */

                // Sort in order of increasing field codes first.
                int thisCode = getFieldCode();
                int xCode = x.getFieldCode();

                if (thisCode < xCode) {
                    return -1;
                } else if (thisCode > xCode) {
                    return 1;
                } else {
                    
                    /*
                     * sort by original sampleHistory ID next, putting records
                     * with the invalid history ID at the end within each field
                     * code
                     */

                    int thisHistId = ((this.originalSampleHistoryId
                            == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                            ? Integer.MAX_VALUE
                            : this.originalSampleHistoryId);
                    int xHistId = ((x.originalSampleHistoryId
                            == SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID)
                            ? Integer.MAX_VALUE
                            : x.originalSampleHistoryId);
                    
                    if (thisHistId < xHistId) {
                        return -1;
                    } else if (thisHistId > xHistId) {
                        return 1;
                    } else {

                        /*
                         * Sort by the values' natural order last, putting
                         * records with null values at the end of this field
                         * code / history ID group
                         */
                        
                        Comparable thisValue = (Comparable) getValue();
                        Comparable xValue = (Comparable) x.getValue();

                        if (thisValue == null) {
                            return ((xValue == null) ? 0 : 1);
                        } else if (xValue == null) {
                            return -1;
                        } else {
                            return thisValue.compareTo(xValue);
                        }
                    }
                }
            }
        }

        /**
         * Determines whether the specified object is equal to this one, in a
         * manner consistent with {@link #compareTo(SampleWorkflowBL.FieldRecord)
         * its natural order}
         * 
         * @return {@code true} if {@code o} is also a {@code FieldRecord}, its
         *         field code is the same as this record's, its value is
         *         equivalent to this one's, and its original sample history ID
         *         is equal to this one's; {@code false} otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof FieldRecord) {
                FieldRecord x = (FieldRecord) o;
    
                // Compare the code field
                return (getFieldCode() == x.getFieldCode())
                        && SampleInfo.compareReferences(getValue(),
                                x.getValue())
                        && (this.originalSampleHistoryId
                                == x.originalSampleHistoryId); 
            } else {
                return false;
            }
        }

        /**
         * Computes and returns a hash code for this {@code FieldRecord} in a
         * manner consistent with its {@link #equals(Object)} method
         * 
         * @return the hash code for this record
         */
        @Override
        public int hashCode() {
            return (((getFieldCode() << 16) + this.originalSampleHistoryId) 
                    ^ getValue().hashCode());
        }
    }

    /**
     * A class that represents a single file version for a
     * sample.  An {@link ActionRecord} may contain several such file
     * records to represent the files that were added/removed during the
     * action.  The 'URL' is never available so that instances of this class
     * may be generaged without need for any information except that contained
     * in a {@code RepositoryFileInfo}.
     */
    public static class FileRecord implements SampleDataFile {

        /** The action during which this file version was added or removed. */
        private final ActionRecord action;

        /** The information about the sample version from the database. */
        private final RepositoryFileInfo rfi;

        /**
         * Initializes a new {@code FileRecord} with the specified parameters
         * 
         * @param action and {@code ActionRecord} representing the action during
         *        which the file version was added or removed
         * @param rfi a {@code RepositoryFileInfo} describing a file version
         *        represented by this record
         * @throws IllegalArgumentException if the file wasn't added or removed
         *         during the indicated action
         */
        public FileRecord(ActionRecord action, RepositoryFileInfo rfi) {
            if ((rfi.sampleId != action.historyInfo.sampleId)
                    || !((rfi.firstSampleHistoryId == action.historyInfo.id)
                            || (rfi.lastSampleHistoryId == action.historyInfo.id))) {
                throw new IllegalArgumentException();
            } else {
                this.action = action;
                this.rfi = rfi;
            }
        }

        /**
         * Returns the file name
         * 
         * @return the name of the file represented by this {@code FileRecord}
         */
        public String getName() {
            return this.rfi.fileName;
        }

        /**
         * Returns the original sample history ID for the file version
         * represented by this record
         * 
         * @return the original sample history ID
         */
        public int getOriginalSampleHistoryId() {
            return this.rfi.originalSampleHistoryId;
        }

        /**
         * Returns the sample history ID of the action with which this record
         * is associated
         * 
         * @return the sample history ID of the associated action
         */
        public int getSampleHistoryId() {
            return this.action.historyInfo.id;
        }

        /**
         * Returns the sample ID of the sample with which this record is
         * associated
         * 
         * @return the sample ID
         */
        public int getSampleId() {
            return this.rfi.sampleId;
        }

        /**
         * Returns the size of the file represented by this record
         * 
         * @return the size, in bytes, as a {@code long}
         */
        public long getSize() {
            return this.rfi.fileBytes;
        }

        /** This method always returns null. */
        public String getUrl() {
            return null;
        }

        /**
         * Determines whether the file represented by this record is
         * 'provisional'
         * 
         * @return nominally, {@code true} if the file is provisional,
         *         {@code false} if not; in practice, this version always
         *         returns {@code false}
         */
        public boolean isProvisional() {
            return false;
        }

        /**
         * Determines whether the file represented by this record is
         * 'settled'
         * 
         * @return nominally, {@code true} if the file is settled,
         *         {@code false} if not; in practice, this version always
         *         returns {@code true}
         */
        public boolean isSettled() {
            return true;
        }

        /**
         * Returns the description of the file represented by this record
         * 
         * @return the file description as a {@code String}
         */
        public String getDescription() {
            return this.rfi.description;
        }

        /**
         * Determines whether the specified object is equal to this one
         * 
         * @return {@code true} if the specified object is also a
         *         {@code FileRecord}, and if the name of its associated file
         *         is the same as the name of this record's associated file
         */
        @Override
        public boolean equals(java.lang.Object o) {
            if (o instanceof FileRecord) {
                return getName().equals(
                        ((FileRecord) o).getName());
            } else {
                return false;
            }
        }

        /**
         * Computes and returns a hash code for this object in a manner
         * consistent with its {@link #equals(java.lang.Object)} method
         * 
         * @return the hash code for this object
         */
        @Override
        public int hashCode() {
            return getName().hashCode();
        }
    }

    /**
     * <p>
     * A container for summary information about one or more
     * {@code SampleHistoryInfo} objects. The information contained includes the
     * number of times a {@code SampleHistoryInfo} with any given action was
     * included well as the {@code SampleHistoryInfo} for the included action
     * with the highest sample history id.
     * </p><p>
     * The expected use of this container is to summarize actions performed
     * during a single day that is visible by a given user (as returned by
     * {@link SampleWorkflowBL#getSampleSummaryByDay(FullSampleInfo, UserInfo)
     * getSampleSummaryByDay()}) but this is genral enough to describe any
     * collection of one or more {@code SampleHistoryInfo} objects.
     * </p>
     */
    public static class SampleActionSummary {

        /** Action codes to number of times included; ordered by action code.*/
        public final SortedMap<Integer, Integer> actionCodeToCountMap;

        /** The {@code SampleHistoryInfo} with the highest id. */
        public SampleHistoryInfo lastActionPerformed;

        /**
         * Initializes a new {@code SampleActionSummary} containing the
         * specified sample history information
         * 
         * @param lastAction a {@code SampleHistoryInfo} representing to be
         *        added to this summary; intended (but not required) to be the
         *        one representing the latest action that will be included in
         *        the summary
         */
        public SampleActionSummary(SampleHistoryInfo  lastAction) {
            this.actionCodeToCountMap = new TreeMap<Integer, Integer>();
            addAction(lastAction);
        }

        /**
         * Adds summary information about the provided action to this
         * {@code SampleActionSummary}, and if the provided action has a higher
         * {@code sampleHistoryId} than the current 'lastActionPerformed' it
         * becomes the current value.
         * 
         * @param action a {@code SampleHistoryInfo} representing an action to
         *        add to this summary
         */
        public void addAction(SampleHistoryInfo action) {
            Integer actionCode = Integer.valueOf(action.action);
            Integer actionCount = this.actionCodeToCountMap.get(actionCode);
            
            if (actionCount == null) {
                this.actionCodeToCountMap.put(actionCode, 1);
            } else {
                this.actionCodeToCountMap.put(actionCode,
                        Integer.valueOf(actionCount.intValue() + 1));
            }
            if ((this.lastActionPerformed == null)
                    || (action.id > this.lastActionPerformed.id)) {
                this.lastActionPerformed = action;
            }
        }
    }
    
    /**
     * A class maintaining information about the current progress on preparing
     * a particular collection of action records.  Instances are exchanged among
     * various methods to avoid duplication of effort.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class ActionInfo {
        
        /**
         * An unmodifiable {@code List} of all the {@code ActionRecord}s
         * maintained by this {@code ActionInfo}, in ascending natural order
         */
        private final List<ActionRecord> allActions;
        
        /**
         * A modifiable {@code List} intended to contain those elements of
         * {@link #allActions} that have been flagged 'distinguished'. A
         * reference to this list can be obtained via
         * {@link #getDistinguishedActions()}, therefore its contents can be
         * externally modified
         */
        private final List<ActionRecord> distinguishedActions;
        
        /**
         * A modifiable {@code Map} intended to associate sample history IDs
         * with corresponding elements of {@link #allActions}. A reference to
         * this map can be obtained via {@link #getActionMap()}, therefore its
         * contents can be externally modified
         */
        private final Map<Integer, ActionRecord> actionMap;
        
        /**
         * Initializes a new {@code ActionInfo} representing the specified
         * actions
         * 
         * @param  actions a {@code Collection} of {@code ActionRecord}s
         *         representing all the actions that will ever be known to this
         *         {@code ActionInfo}.  The collection should not contain
         *         duplicates, but its iteration order is insignificant.  This
         *         object is not modified and no reference to it is retained
         *         past the end of this constructor, but the
         *         {@code ActionRecord} references it contains will be retained
         *         by this {@code ActionInfo} 
         */
        public ActionInfo(Collection<? extends ActionRecord> actions) {
            List<ActionRecord> actionList
                    = new ArrayList<ActionRecord>(actions);
            
            Collections.sort(actionList);
            allActions = Collections.unmodifiableList(actionList);
            distinguishedActions = new ArrayList<ActionRecord>();
            actionMap = new HashMap<Integer, ActionRecord>();
        }
        
        /**
         * Obtains an unmodifiable list of all the actions comprised by this
         * {@code ActionInfo}, without duplicates and in order of ascending
         * sampleHistory ID
         * 
         * @return an unmodifiable {@code List} of {@code ActionRecord}s
         *         representing the actions comprised by this {@code ActionInfo}
         */
        public List<ActionRecord> getAllActions() {
            return allActions;
        }
        
        /**
         * Provides a reference to the modifiable list of distinguished actions
         * maintained by this {@code ActionInfo}.
         * 
         * @return a reference to the {@code List} maintained by this
         *         {@code ActionInfo} of {@code ActionRecord} objects
         *         representing the distinguished actions among all those
         *         maintained by this {@code ActionInfo}. This list is merely
         *         held by this info, not modified by it itself, and it is
         *         initially empty.
         */
        public List<ActionRecord> getDistinguishedActions() {
            return distinguishedActions;
        }
        
        /**
         * Provides a reference to the modifiable map from sampleHistory IDs to
         * corresponding {@code ActionRecords} maintained by this
         * {@code ActionInfo}
         * 
         * @return a reference to the {@code Map} from {@code Integer} history
         *         IDs to {@code ActionRecord} objects. This map is merely held
         *         by this info, not modified by it itself, and it is initially
         *         empty.
         */
        public Map<Integer, ActionRecord> getActionMap() {
            return actionMap;
        }
    }
}
