/*
 * Reciprocal Net project
 * 
 * SampleActionIterator.java
 *
 * 10-Jun-2004: midurbin wrote first draft
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 24-Jun-2005: midurbin added methods to expose all fields in the
 *              ActionRecord, fixed bug that caused distinguished and
 *              correctable actions to be suppressable,
 *              added ACTION_WAS_CORRECTED_ONCE,
 *              ACTION_WAS_CORRECTED_MORE_THAN_ONCE,
 *              SOME_FILE_ACTION_WAS_SUPPRESSED,
 *              SOME_CORRECTION_ACTION_WAS_SUPPRESSED and
 *              SOME_OTHER_ACTION_WAS_SUPPRESSEED error flags
 * 17-Oct-2005: midurbin added getFilesAddedDuringAction(),
 *              getFilesRemovedDuringAction() and supporting code
 * 27-Oct-2005: midurbin added getFilesModifiedDuringAction()
 * 28-Oct-2005: midurbin altered onIterationAfterBody() to suppress actions
 *              skipped by reversion according to the user's preferences; added
 *              getSuppressedSkippedActionCount()
 * 10-Nov-2005: midurbin clarified the suppression logic in
 *              onIterationAfterBody()
 * 27-Apr-2006: jobollin reformatted the source, removed unused imports
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.RecipnetException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.LocalTrackingConfig;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.SampleFieldRecord;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.RepositoryFileInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.RequestCache;
import org.recipnet.site.shared.bl.SampleWorkflowBL.ActionRecord;

/**
 * <p>
 * A custom tag that iterates through the actions on a {@code FullSampleInfo} in
 * order of increasing sampleHistoryId. This tag must be nested in a
 * {@code SampleConext} representing the sample whose actions are to be iterated
 * over.
 * </p><p>
 * This tag provides the {@code SampleHistoryContext} to nested tags.
 * Furthermore, a {@code Collection} of {@code SampleFieldRecord} objects
 * representing the fields associated with the action are made available.
 * </p><p>
 * This tag implements the {@code SuppressionContext} and may suppress certain
 * body evaluations based on:
 * <ol>
 * <li>The type of action.</li>
 * <li>The currently logged-in user's preferenced.</li>
 * <li>Whether any fields have been displayed for the action ({@link
 * SampleActionIterator#notifyFieldDisplayed() notifyFieldDisplayed()}.</li>
 * </ol>
 * </p>
 */
public class SampleActionIterator extends HtmlPageIterator implements
        SampleHistoryContext, SuppressionContext {

    /**
     * An error flag that indicates that the action was corrected exactly one
     * time. This flag only applies to a single evaluation of the body of this
     * tag and not all iterations.
     */
    public static final int ACTION_WAS_CORRECTED_ONCE
            = HtmlPageIterator.getHighestErrorFlag() << 1;

    /**
     * An error flag that indicates that the action was corrected more than one
     * time. This flag only applies to a single evaluation of the body of this
     * tag and not all iterations.
     */
    public static final int ACTION_WAS_CORRECTED_MORE_THAN_ONCE
            = HtmlPageIterator.getHighestErrorFlag() << 2;

    /**
     * An error flag that indicates that the effects of the current action were
     * undone by a reversion action. This flag only applies to a single
     * evaluation of the body of this tag and not all iterations.
     */
    public static final int CURRENT_ACTION_WAS_SKIPPED_BY_REVERSION
            = HtmlPageIterator.getHighestErrorFlag() << 3;

    /**
     * An error flag that indicates that exactly one iteration was suppressed
     * because it referred to an empty file action when the user's preferences
     * indicated that such actions should be suppressed.
     */
    public static final int ONE_FILE_ACTION_WAS_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 4;

    /**
     * An error flag that indicates that more than one iterations were
     * suppressed because they referred to empty file actions when the user's
     * preferences indicated that such actions should be suppressed.
     */
    public static final int SOME_FILE_ACTIONS_WERE_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 5;

    /**
     * An error flag that indicates that exactly one iteration was suppressed
     * because it referred to an empty correction action when the user's
     * preferences indicated that such actions should be suppressed.
     */
    public static final int ONE_CORRECTION_ACTION_WAS_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 6;

    /**
     * An error flag that indicates that more than one iterations were
     * suppressed because they referred to empty correction actions when the
     * user's preferences indicated that such actions should be suppressed.
     */
    public static final int SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 7;

    /**
     * An error flag that indicates that exactly one iteration was suppressed
     * because it referred to an empty action that was neither distinguished,
     * correctable, a file action nor a correction action when the user's
     * preferences indicated that such actions should be suppressed.
     */
    public static final int ONE_OTHER_ACTION_WAS_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 8;

    /**
     * An error flag that indicates that more than one iterations were
     * suppressed because they referred to empty actions that were neither
     * distinguished, correctable, a file action nor a correction action when
     * the user's preferences indicated that such actions should be suppressed.
     */
    public static final int SOME_OTHER_ACTIONS_WERE_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 9;

    /**
     * An error flag that indicates that exactly one iteration was suppressed
     * because it referred to an action that was made irrelevent (skipped) by a
     * reversion to a previous version when the user's preferences indicated
     * that such actions should be suppressed.
     */
    public static final int ONE_SKIPPED_ACTION_WAS_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 10;

    /**
     * An error flag that indicates that more than one iterations were
     * suppressed because they referred to actions that were made irrelevent
     * (skipped) by one or more reversions to a previous version when the user's
     * preferences indicated that such actions should be suppressed.
     */
    public static final int SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 11;

    /**
     * The most immediately enclosing {@code SampleContext}. This reference is
     * set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * An ordered {@code Collection} of the {@code ActionRecord}s for each
     * iteration of this tag. This {@code Collection} is populated during the
     * {@code FETCHING_PHASE} with values from {@code SampleWorkflowBL}.
     * Besides the {@code SampleHistoryInfo} provided by this tag through its
     * {@code SampleHistoryContext} implementation}, each {@code ActionRecord}
     * contains information that may be useful in determining whether the action
     * matches any of the critera to suppress its output as indicated by the
     * currently logged-in user's preferences.
     */
    private Collection<ActionRecord> actions;

    /**
     * An {@code Iterator} over the '{@link SampleActionIterator#actions
     * actions}'.
     */
    private Iterator<ActionRecord> actionIterator;

    /**
     * The 'action' whose {@code SampleHistoryInfo} is exposed for the current
     * evaluation of this tag's body.
     */
    private ActionRecord currentActionRecord;

    /**
     * An internal variable that is set to true once
     * {@code notifyFieldDisplayed()} has been called at least once for the
     * current iteration.
     */
    private boolean atLeastOneFieldWasDisplayedForCurrentAction;

    /**
     * The most immediate {@code SuppressionContext} implementation; determined
     * during {@code onRegistrationPhaseBeforeBody()} and used to ensure that
     * this tag propagates suppression indicators from its ancestry along with
     * its own suppression indicators.
     */
    private SuppressionContext suppressionContext;

    /**
     * A {@code Collection} of {@code SampleWorkflowBL.ActionRecord} objects
     * representing actions that have been determined (during the
     * {@code FETCHING_PHASE}) to be suppressed. This tag will still evaluated
     * the body for these actions, but the output will be discarded rather than
     * written to the buffer and the method
     * {@code isTagsBodySuppressedThisPhase()} will return true for the
     * iteration that would provided that {@code ActionRecord}.
     */
    private Collection<ActionRecord> suppressedActions;

    /**
     * A temporary buffer used to store the output of body evaluations that are
     * NOT suppressed during the {@code RENDERING_PHASE}. After all iterations
     * are completed, this buffer is written to the {@code JspWriter} for the
     * page.
     */
    private StringWriter unsuppressedBody;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.actions = null;
        this.actionIterator = null;
        this.currentActionRecord = null;
        this.atLeastOneFieldWasDisplayedForCurrentAction = false;
        this.suppressionContext = null;
        this.suppressedActions = new ArrayList<ActionRecord>();
        this.unsuppressedBody = null;
    }

    /** Implements {@code SuppressionContext}. */
    public boolean isTagsBodySuppressedThisPhase() {
        if ((this.suppressionContext != null)
                && this.suppressionContext.isTagsBodySuppressedThisPhase()) {
            return true;
        } else {
            return suppressedActions.contains(this.currentActionRecord);
        }
    }

    /**
     * {@inheritDoc}; this version prevents iteration-specific error flags from
     * being set.
     * 
     * @throws IllegalArgumentException if an error flag that is
     *         iteration-specific is supplied
     */
    @Override
    public void setErrorFlag(int errorFlag) {
        if ((errorFlag & (ACTION_WAS_CORRECTED_ONCE
                | ACTION_WAS_CORRECTED_MORE_THAN_ONCE
                | CURRENT_ACTION_WAS_SKIPPED_BY_REVERSION)) != 0) {
            // the specified error flag relates to a specific iteration and
            // should not be set with this method because this method will
            // affect ALL iterations
            // Note: if there were any way to unset error flags, this would be
            // implemented differently
            throw new IllegalArgumentException();
        }
        super.setErrorFlag(errorFlag);
    }

    /**
     * {@inheritDoc}; this version provides support for iteration-specific (not
     * general) error flags being returned.
     */
    @Override
    public int getErrorCode() {
        if ((getPage().getPhase() != HtmlPage.REGISTRATION_PHASE)
                && (getPage().getPhase() != HtmlPage.PARSING_PHASE)) {
            if (getCurrentActionCorrectionCount() != 0) {
                // return the iteration-specific error flag along with any
                // general ones already set
                return super.getErrorCode()
                        | ((getCurrentActionCorrectionCount() == 1)
                                ? ACTION_WAS_CORRECTED_ONCE
                                : ACTION_WAS_CORRECTED_MORE_THAN_ONCE);
            }
            if (this.currentActionRecord.wasSkippedByReversion) {
                // return the iteration-specific error flag along with any
                // general ones already set
                return super.getErrorCode()
                        | CURRENT_ACTION_WAS_SKIPPED_BY_REVERSION;
            }
        }
        
        return super.getErrorCode();
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED;
    }

    /**
     * {@inheritDoc}; this version gets a reference to the
     * {@code SampleContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SampleContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        // get the SuppressionContext if one exists
        // if one exists, a reference is needed so that its
        // isTagsBodySuppressedThisPhase() return value may be propagated
        this.suppressionContext
                = findRealAncestorWithClass(this, SuppressionContext.class);

        return rc;
    }

    /**
     * {@inheritDoc}; this version uses {@code SampleWorkflowBL} to generate a
     * sorted {@code Collection} of actions and the fields for them.
     * 
     * @throws JspException wrapping any exceptions thrown while retrieving data
     *         from core
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleInfo si = this.sampleContext.getSampleInfo();

        if (si == null) {
            // return early, before setting fullSampleInfo, which will result
            // in no body evaluations
            return rc;
        }

        CoreConnector cc
                = CoreConnector.extract(this.pageContext.getServletContext());

        // get FullSampleInfo
        FullSampleInfo fullSampleInfo = RequestCache.getFullSampleInfo(
                this.pageContext.getRequest(), si.id);

        if (fullSampleInfo == null) {
            try {
                fullSampleInfo = cc.getSampleManager().getFullSampleInfo(si.id);
                RequestCache.putFullSampleInfo(
                        this.pageContext.getRequest(), fullSampleInfo);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (RecipnetException ex) {
                throw new JspException(ex);
            }
        }

        // get LocalTrackingConfig
        LocalTrackingConfig currentLtc
                = RequestCache.getLTC(pageContext.getRequest(), si.labId);
        if (currentLtc == null) {
            // cache miss
            try {
                currentLtc
                        = cc.getSiteManager().getLocalTrackingConfig(si.labId);
                RequestCache.putLTC(pageContext.getRequest(), currentLtc);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (RecipnetException ex) {
                throw new JspException(ex);
            }
        }

        // get file history information
        Collection<RepositoryFileInfo> sampleFiles = null;
        try {
            sampleFiles = Arrays.asList(
                    cc.getRepositoryManager().getAllRepositoryFileInfosForSample(
                            si.id));
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }

        this.actions = SampleWorkflowBL.getActionRecords(
                fullSampleInfo, currentLtc, sampleFiles);

        return rc;
    }

    /**
     * {@inheritDoc}; this version overrides the superclass' return code to
     * always return {@code EVAL_BODY_BUFFERED}.
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws IOException,
            JspException {
        super.onRenderingPhaseBeforeBody(out);
        
        return EVAL_BODY_BUFFERED;
    }

    /**
     * {@inheritDoc}; this version initializes iteration over the 'actions' if
     * it has been created yet (it is created during the
     * {@code FETCHING_PHASE}).
     */
    @Override
    @SuppressWarnings("unused")
    protected void beforeIteration() throws JspException {
        if (this.actions != null) {
            this.actionIterator = actions.iterator();
            this.unsuppressedBody = new StringWriter();
        }
    }

    /**
     * {@inheritDoc}; this version sets the 'currentActionRecord' to the next
     * one available on the 'actionIterator'.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.actions == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                    && (getPostedIterationCount()
                            > getIterationCountSinceThisPhaseBegan())) {
                // evaluate the body for the benefit of nested elements,
                // even though those nested elements may not yet get the
                // SampleTextContext. This ensures that the SampleFields can
                // parse values that might have been posted for this request
                return true;
            }
            return false;
        }
        if (!this.actionIterator.hasNext()) {
            return false;
        }
        this.currentActionRecord = this.actionIterator.next();
        this.atLeastOneFieldWasDisplayedForCurrentAction = false;
        
        return true;
    }

    /**
     * {@inheritDoc}; this version determines during the {@code FETCHING_PHASE}
     * whether any of the conditions for suppression are satisfied, and if so,
     * updates the 'suppressedActions'. During the {@code RENDERING_PHASE} any
     * output for a suppressed iteration is cleared from the
     * {@code BodyContent}; all other output is written to the
     * 'unsuppressedBody' buffer.
     */
    @Override
    protected boolean onIterationAfterBody() {
        if (getPage().getPhase() == HtmlPage.FETCHING_PHASE) {
            // During the fetching phase, we determine whether each action will
            // be suppressed or not and populate the 'suppressedActions' list.

            // get the preferences for the current session
            UserPreferences prefs = (UserPreferences)
                    this.pageContext.getSession().getAttribute("preferences");

            if (this.currentActionRecord.isDistinguished
                    || this.currentActionRecord.isCorrectable) {
                // distinguished or correctable actions may never be suppressed
            } else if (this.currentActionRecord.wasSkippedByReversion
                    && UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.SUPPRESS_SKIPPED, prefs)) {
                // suppress this action that was skipped by reversion
                this.suppressedActions.add(this.currentActionRecord);
            } else if (this.currentActionRecord.isFileAction()
                    && !atLeastOneFieldWasDisplayedForCurrentAction
                    && UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.SUPPRESS_EMPTY_FILE, prefs)) {
                // suppress this empty file action
                this.suppressedActions.add(this.currentActionRecord);
            } else if ((this.currentActionRecord.correctsAction != null)
                    && !atLeastOneFieldWasDisplayedForCurrentAction
                    && UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.SUPPRESS_EMPTY_CORRECTION, prefs)) {
                // suppress this empty correction action
                this.suppressedActions.add(this.currentActionRecord);
            } else if ((this.currentActionRecord.correctsAction == null)
                    && !this.currentActionRecord.isFileAction()
                    && !atLeastOneFieldWasDisplayedForCurrentAction
                    && !this.currentActionRecord.wasSkippedByReversion
                    && UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.SUPPRESS_EMPTY_OTHER, prefs)) {
                // suppress this empty action that is neither a file action,
                // correction action nor an action skipped by reversion
                this.suppressedActions.add(this.currentActionRecord);
            }
        } else if (getPage().getPhase() == HtmlPage.RENDERING_PHASE) {
            if (this.suppressedActions.contains(this.currentActionRecord)) {
                // clear any output generated during this iteration
                getBodyContent().clearBody();
            } else {
                try {
                    getBodyContent().writeOut(this.unsuppressedBody);
                    getBodyContent().clearBody();
                } catch (IOException ex) {
                    // can't happen because a StringWriter doesn't throw
                    // IOExceptoins
                }
            }
        }
        
        return true;
    }

    /**
     * {@inheritDoc}; this version writes the 'unsuppressedBody' buffer to the
     * {@code JspWriter}.
     * 
     * @throws JspException wrapping an IOException if there is an error writing
     *         to the buffer
     */
    @Override
    protected void afterIteration() throws JspException {
        if (getPage().getPhase() == HtmlPage.RENDERING_PHASE) {
            // set error flags based on suppressed fields

            // TODO: imporove efficiency by iterating over the list once (not
            // using helper methods) or by having helper method cache
            UserPreferences prefs = (UserPreferences)
                    this.pageContext.getSession().getAttribute("preferences");
            
            if (UserBL.getPreferenceAsBoolean(
                    UserBL.Pref.SUPPRESS_EMPTY_FILE, prefs)) {
                switch (getSuppressedEmptyFileActionCount()) {
                    case 0:
                        // do nothing
                        break;
                    case 1:
                        setErrorFlag(ONE_FILE_ACTION_WAS_SUPPRESSED);
                        break;
                    default:
                        setErrorFlag(SOME_FILE_ACTIONS_WERE_SUPPRESSED);
                        break;
                }
            }
            if (UserBL.getPreferenceAsBoolean(
                    UserBL.Pref.SUPPRESS_EMPTY_CORRECTION, prefs)) {
                switch (getSuppressedEmptyCorrectionActionCount()) {
                    case 0:
                        // do nothing
                        break;
                    case 1:
                        setErrorFlag(ONE_CORRECTION_ACTION_WAS_SUPPRESSED);
                        break;
                    default:
                        setErrorFlag(SOME_CORRECTION_ACTIONS_WERE_SUPPRESSED);
                        break;
                }
            }
            if (UserBL.getPreferenceAsBoolean(
                    UserBL.Pref.SUPPRESS_EMPTY_OTHER, prefs)) {
                switch (getSuppressedEmptyOtherActionCount()) {
                    case 0:
                        // do nothing
                        break;
                    case 1:
                        setErrorFlag(ONE_OTHER_ACTION_WAS_SUPPRESSED);
                        break;
                    default:
                        setErrorFlag(SOME_OTHER_ACTIONS_WERE_SUPPRESSED);
                        break;
                }
            }
            if (UserBL.getPreferenceAsBoolean(
                    UserBL.Pref.SUPPRESS_SKIPPED, prefs)) {
                switch (getSuppressedSkippedActionCount()) {
                    case 0:
                        // do nothing
                        break;
                    case 1:
                        setErrorFlag(ONE_SKIPPED_ACTION_WAS_SUPPRESSED);
                        break;
                    default:
                        setErrorFlag(SOME_SKIPPED_ACTIONS_WERE_SUPPRESSED);
                        break;
                }
            }
        }
        if (this.unsuppressedBody != null) {
            try {
                this.pageContext.getOut().write(
                        this.unsuppressedBody.toString());
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }
    }

    /** Implements {@code SampleHistoryContext}. */
    public SampleHistoryInfo getSampleHistoryInfo() {
        if ((getPage().getPhase() == HtmlPage.REGISTRATION_PHASE)
                || (getPage().getPhase() == HtmlPage.PARSING_PHASE)) {
            throw new IllegalStateException();
        }
        
        return this.currentActionRecord.historyInfo;
    }

    /**
     * Gets a {@code Collection} of {@code SampleFieldRecord} objects that
     * represent the fields that may be displayed for the action associated with
     * this tag's current iteration. All {@code SampleFieldRecord} objects
     * returned will have a valid 'fieldCode' but might have a null value. Such
     * null value's indicate fields that are editable for the action, but
     * currently unset.
     * 
     * @return a collection of {@code SampleFieldRecords}; never null but
     *         possibly an empty collection
     */
    public Collection<SampleFieldRecord> getFieldsForAction() {
        return new ArrayList<SampleFieldRecord>(
                this.currentActionRecord.fields);
    }

    /** Gets the files added during the current action. */
    public Collection<? extends SampleDataFile> getFilesAddedDuringAction() {
        return this.currentActionRecord.filesAdded;
    }

    /** Gets the files removed during the current action. */
    public Collection<? extends SampleDataFile> getFilesRemovedDuringAction() {
        return this.currentActionRecord.filesRemoved;
    }

    /** Gets the files modified during the current action. */
    public Collection<? extends SampleDataFile> getFilesModifiedDuringAction() {
        return this.currentActionRecord.filesModified;
    }

    /**
     * Gets the number of times the current action was corrected, either by an
     * explicit correction, or by a reversion that changed the values.
     * 
     * @throws NullPointerException if there is not yet a current action
     */
    public int getCurrentActionCorrectionCount() {
        return this.currentActionRecord.correctingActions.size();
    }

    /**
     * Gets whether the current action is correctable.
     * 
     * @throws NullPointerException if there is not yet a current action
     */
    public boolean isCurrentActionCorrectable() {
        return this.currentActionRecord.isCorrectable;
    }

    /**
     * Gets the uncorrectable fields constant for the current action.
     * 
     * @throws NullPointerException if there is not yet a current action
     * @throws IllegalStateException if the current action is not correctable.
     */
    public int getCurrentActionUncorrectableFields() {
        if (!isCurrentActionCorrectable()) {
            throw new IllegalStateException();
        }
        
        return this.currentActionRecord.uncorrectableFields;
    }

    /** Returns the number of empty file actions suppressed. */
    public int getSuppressedEmptyFileActionCount() {
        int count = 0;
        
        for (SampleWorkflowBL.ActionRecord action : this.suppressedActions) {
            if (action.isFileAction()) {
                count++;
            }
        }
        
        return count;
    }

    /** Returns the number of empty correction actions suppressed. */
    public int getSuppressedEmptyCorrectionActionCount() {
        int count = 0;
        
        for (SampleWorkflowBL.ActionRecord action : this.suppressedActions) {
            if (action.correctsAction != null) {
                count++;
            }
        }
        
        return count;
    }

    /** Returns the number of empty other actions suppressed. */
    public int getSuppressedEmptyOtherActionCount() {
        int count = 0;
        
        for (SampleWorkflowBL.ActionRecord action : this.suppressedActions) {
            if ((action.correctsAction == null) && !action.isFileAction()) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * Returns the number of suppressed actions that were skipped by reversion.
     */
    public int getSuppressedSkippedActionCount() {
        int count = 0;
        
        for (SampleWorkflowBL.ActionRecord action : this.suppressedActions) {
            if (action.wasSkippedByReversion) {
                count++;
            }
        }
        
        return count;
    }

    /**
     * A method that must be called by nested tags that display either fields or
     * comments. If no calls are made to this method during a particular
     * iteration, the action will be considered 'empty' and may be suppressed
     * depending on the currently logged-in user's preferences. This method must
     * be called during the {@code FETCHING_PHASE} in order for suppression to
     * be possible during the {@code RENDING_PHASE}.
     * 
     * @throws IllegalStateException if called on any phase other than the
     *         {@code FETCHING_PHASE}
     */
    public void notifyFieldDisplayed() {
        if (getPage().getPhase() != HtmlPage.FETCHING_PHASE) {
            throw new IllegalStateException();
        }
        this.atLeastOneFieldWasDisplayedForCurrentAction = true;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleActionIterator dc
                = (SampleActionIterator) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        if (this.actions != null) {
            dc.actions = new ArrayList<ActionRecord>(this.actions);
        }
        
        return dc;
    }
}
