/*
 * Reciprocal Net project
 * @(#)EditSamplePage.java
 *
 * 09-Jun-2005: midurbin wrote first draft
 * 24-Jun-2005: midurbin added support to store updated user preferences
 * 05-Jul-2005: midurbin added calls to removeFormField() to prevent the
 *              'unsetSuppression' request parameter from overriding completed
 *              preference changes
 * 13-Jul-2005: midurbin updated checkAuthorization() to return an int
 * 27-Jul-2005: midurbin updated doAfterBody() to reflect name and spec changes
 * 11-Aug-2005: midurbin factored ErrorSupplier implementing code out to
 *              HtmlPage
 * 06-Oct-2005: midurbin updated checkAuthorization() to accomodate a
 *              specification change in AuthorizationCheckerBL
 * 27-Oct-2005: midurbin added support to unset suppression of skipped actions;
 *              moved preference modifications to doBeforePageBody()
 */

package org.recipnet.site.content.rncontrols;

import java.rmi.RemoteException;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.EvaluationAbortedException;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.InvalidModificationException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.core.WrongSiteException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.AuthorizationCheckerBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A custom tag that extends <code>SamplePage</code> to expose a data directory
 * creation mechanism and a user preferences updating mechanism. <p>
 *
 * Nested tags that wish to initiate the creation of a data directory may
 * interact with this tag to provide an extension path and to trigger the
 * creation of a data directory for the sample defined by the superclass'
 * <code>SampleContext</code> by user defined by the superclass'
 * <code>UserContext</code>. <p>
 *
 * Nested tags that wish to update the user preferences, may simply alter
 * the <code>UserPreferences</code> within the <code>UserInfo</code> provided
 * by this tag and then trigger this tag to store the altered
 * <code>UserPreferences</code> back into to session.<p>
 *
 * This tag will result in redirection to the login page if the user is not
 * authorized to edit the sample or update his/her own user preferences
 * regardless of whether nested tags actually intend to use those features. 
 * <p>
 *
 * Upon completion of the data directory creation or user preference
 * modification, this tag triggers a page reevaluation to accomodate distinct
 * display modes for samples based on the existence of data directories or the
 * preference settings.
 */
public class EditSamplePage extends SamplePage {

    /**
     * An optional property that when set indicates the name of a URL query
     * parameter that when present and set to the <code>String</code>
     * representation of a {@link org.recipnet.site.shared.bl.UserBL.Pref
     * UserBL.PREF} type for a suppression preference, causes
     * the given preference for the currently logged-in user to be set to
     * false.  This is a very special-purpose feature and only supports
     * unsuppressing empty file actions, empty correction actions and empty
     * other actions to enable a desired UI feature on sample.jsp.
     */
    private String unsetSuppressionPreferenceParamName;

    /**
     * The extension path requested for the new data directory.  This value is
     * set by a call to <code>setExtensionPath()</code> which is most likely
     * invoked by a nested {@link DataDirectoryField DataDirectoryField} tag
     * during the <code>PROCESSING_PHASE</code>.  This value may be null to
     * indicate that no extension path is to be used.
     */
    private String extensionPath;

    /**
     * An internal variable that indicates whether a data directory should be
     * created for the sample at the end of the <code>PROCESSING_PHASE</code>
     * or not.  Set by <code>triggerCreateDirectory()</code>.
     */
    private boolean triggerCreateDirectory;

    /**
     * An internal variable that indicates whether modifications made to the
     * provided <code>UserInfo</code> object's <code>UserPreference</code>
     * object should be applied to the session (with the expectation that they
     * will be stored to the database when the session ends).  Set by
     * <code>triggerStorePreferences()</code>.
     */
    private boolean triggerStorePreferences;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.unsetSuppressionPreferenceParamName = null;
        this.extensionPath = null;
        this.triggerCreateDirectory = false;
        this.triggerStorePreferences = false;
    }

    /** Sets the 'unsetSuppressionPreferenceParamName' proprety. */
    public void setUnsetSuppressionPreferenceParamName(String paramName) {
        this.unsetSuppressionPreferenceParamName = paramName;
    }

    /** Gets the 'unsetSuppressionPreferenceParamName' proprety. */
    public String getUnsetSuppressionPreferenceParamName() {
        return this.unsetSuppressionPreferenceParamName;
    }

    /**
     * Sets the value that will be used as the extension path if a data
     * directory is created.  This value may be null, but when not null is
     * expected to be a valid extension path name.
     * @param extensionPath a suggested extension path
     */
    public void setExtensionPath(String extensionPath) {
        this.extensionPath = extensionPath;
    }

    /**
     * Overrides <code>SamplePage</code> in order unset certain suppression
     * preferences in accordance with the 'unsetSuppressionPreferenceParamName'
     * property and the current request.
     */
    @Override
    protected void doBeforePageBody() throws JspException,
            EvaluationAbortedException {
        super.doBeforePageBody();
        if (this.getPhase() == HtmlPage.REGISTRATION_PHASE) {
            // process URL parameters

            // NOTE: the URL parameters recognized are not very general
            // purpose but instead only included to enable a specific UI
            // feature.  If that feature is change or removed this code
            // should be removed or made more general.
            if (this.unsetSuppressionPreferenceParamName != null) {
                String value = this.pageContext.getRequest().getParameter(
                        this.getUnsetSuppressionPreferenceParamName());
                UserPreferences prefs = (UserPreferences)
                    this.pageContext.getSession().getAttribute("preferences");
                if (UserBL.Pref.SUPPRESS_EMPTY_CORRECTION.toString(
                        ).equals(value)) {
                    UserBL.setPreference(
                            UserBL.Pref.SUPPRESS_EMPTY_CORRECTION,
                            prefs, false);
                } else if (UserBL.Pref.SUPPRESS_EMPTY_FILE.toString(
                        ).equals(value)) {
                    UserBL.setPreference(UserBL.Pref.SUPPRESS_EMPTY_FILE,
                                         prefs, false);
                } else if (UserBL.Pref.SUPPRESS_EMPTY_OTHER.toString(
                        ).equals(value)) {
                    UserBL.setPreference(UserBL.Pref.SUPPRESS_EMPTY_OTHER,
                                         prefs, false);
                } else if (UserBL.Pref.SUPPRESS_SKIPPED.toString(
                        ).equals(value)) {
                    UserBL.setPreference(UserBL.Pref.SUPPRESS_SKIPPED,
                                         prefs, false);
                }
                // notify the superclass not to include the
                // unsetSuppressionPreference parameter in reinvocation
                // URLs or as hidden form fields
                this.removeFormField(unsetSuppressionPreferenceParamName);
            }
        }
    }

    /**
     * Overrides <code>HtmlPage</code>; the current implementation
     * invokes <code>createDirectory()</code> at the end of the
     * <code>PROCESSING_PHASE</code> if <code>triggerCreateDirectory()</code>
     * has been called.
     * @throws JspException wraps any exception that may be thrown while
     *     creating the data directory
     */
    @Override
    protected void doAfterPageBody() throws JspException,
            EvaluationAbortedException {
        switch (this.getPhase()) {
            case HtmlPage.PROCESSING_PHASE:
                if (this.triggerCreateDirectory) {
                    this.createDirectory();
                } else if (this.triggerStorePreferences) {
                    this.storePreferences();
                }
                break;
        }
        super.doAfterPageBody();
    }

    /**
     * {@inheritDoc} <p>
     *
     * The current implementation then verifies that the user may edit the
     * sample or change his/her own preferences ({@link
     * org.recipnet.site.shared.bl.AuthorizationCheckerBL#canEditSample
     * AuthorizationChecker.canEditSample()}, {@link
     * org.recipnet.site.shared.bl.AuthorizationCheckerBL#canChangeOwnPreferences(
     * UserInfo) AuthorizationChecker.canChangeOwnPreferences()}).
     */
    @Override
    protected int checkAuthorization() {
        int auth = super.checkAuthorization();
        if (auth != AuthorizationReasonMessage.USER_IS_AUTHORIZED) {
            return auth;
        } else if (!AuthorizationCheckerBL.canEditSample(this.getUserInfo(),
                this.getSampleInfo())) {
            return AuthorizationReasonMessage.CANNOT_EDIT_SAMPLE;
        } else if (!AuthorizationCheckerBL.canChangeOwnPreferences(
                this.getUserInfo())) {
            return AuthorizationReasonMessage.CANNOT_CHANGE_PREFERENCES;
        } else {
            return auth;
        }
    }

    /**
     * Triggers the creation of a data directory for the current sample by the
     * current user at the end of this tag's <code>PROCESSING_PHASE</code>.
     * @throws IllegalStateException if called during the
     *     <code>RENDERING_PHASE</code> because it is too late to create the
     *     data directory or if 'triggerStorePreferences' has already been set
     */
    public void triggerCreateDirectory() {
        if ((this.getPhase() == HtmlPage.RENDERING_PHASE)
                || this.triggerStorePreferences) {
            throw new IllegalStateException();
        }
        this.triggerCreateDirectory = true;
    }

    /**
     * Triggers the storage of the updated user preferences for the current
     * user at the end of this tag's <code>PROCESSING_PHASE</code>.
     * @throws IllegalStateException if called during the
     *     <code>RENDERING_PHASE</code> because it is too late to update the
     *     preferences or if 'triggerCreateDirectory' has already been set
     */
    public void triggerStorePreferences() {
        if ((this.getPhase() == HtmlPage.RENDERING_PHASE)
                || this.triggerCreateDirectory) {
            throw new IllegalStateException();
        }
        this.triggerStorePreferences = true;
    }

    /**
     * Attemps to create a data directory for the sample provided by the
     * superclass' <code>SampleContext</code>.
     * @throws JspException wrapping the various exceptions possibly thrown by
     *     {@link
     *     org.recipnet.site.core.RepositoryManager#createDataDirectory(int,
     *     java.lang.String, int) RepositoryManager.createDataDirectory()}
     */
    private void createDirectory() throws JspException {
        if (!this.areAllFieldsValid()) {
            return;
        }
        CoreConnector cc
                = CoreConnector.extract(pageContext.getServletContext());
        try {
            cc.getRepositoryManager().createDataDirectory(
                    this.getSampleInfo().id, this.extensionPath,
                    this.getUserInfo().id);
            this.reevaluatePage();
        } catch (RemoteException ex) {
            cc.reportRemoteException(ex);
            throw new JspException(ex);
        } catch (InconsistentDbException ex) {
            throw new JspException(ex);
        } catch (InvalidDataException ex) {
            throw new JspException(ex);
        } catch (InvalidModificationException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        } catch (OperationFailedException ex) {
            throw new JspException(ex);
        } catch (WrongSiteException ex) {
            throw new JspException(ex);
        }
    }

    /**
     * Updates the <code>UserInfo</code> for the currently logged-in user in
     * the session to reflect the updated preference values.
     */
    private void storePreferences() throws JspException {
        if (!this.areAllFieldsValid()) {
            return;
        }
        ((UserInfo) this.pageContext.getSession().getAttribute(
                "userInfo")).preferences = this.getUserInfo().preferences;
        this.pageContext.getSession().setAttribute("preferences",
                this.getUserInfo().preferences);
        this.reevaluatePage();
    }

    /**
     * Overrides <code>HtmlPage</code>; the current implementation unsets
     * 'triggerCreateDirectory' then delegates back to the superclass.
     */
    @Override
    protected void onReevaluation() {
        this.triggerCreateDirectory = false;
        this.triggerStorePreferences = false;
        super.onReevaluation();
    }
}
