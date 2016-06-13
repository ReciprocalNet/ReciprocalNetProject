/*
 * Reciprocal Net project
 * 
 * UploadFilesButton.java
 * 
 * 28-Oct-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;

import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;

/**
 * An extension of {@code ContextPreservingButton} providing a link to the file
 * upload page that corresponds to the current session's preferences.
 */
public class UploadFilesButton extends ContextPreservingButton {

    /** A required property; indicates the "form-based" file upload page. */
    private String formBasedPageUrl;

    /** A required property; indicates the "drag-and-drop" file upload page. */
    private String dragAndDropPageUrl;

    /** Sets the 'formBasedPageUrl' property. */
    public void setFormBasedPageUrl(String url) {
        this.formBasedPageUrl = url;
    }

    /** Gets the 'formBasedPageUrl' property. */
    public String getFormBasedPageUrl() {
        return this.formBasedPageUrl;
    }

    /** Sets the 'dragAndDropPageUrl' property. */
    public void setDragAndDropPageUrl(String url) {
        this.dragAndDropPageUrl = url;
    }

    /** Gets the 'dragAndDropPageUrl' property. */
    public String getDragAndDropPageUrl() {
        return this.dragAndDropPageUrl;
    }

    /**
     * {@inheritDoc}; this version sets the 'target' property of the superclass
     * based on the current session's preferences and the this tag's properties.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        UserPreferences preferences
                = (UserPreferences) this.pageContext.getSession().getAttribute(
                        "preferences");

        if (UserBL.getUploadMechanismPreference(preferences)
                == UserBL.UploadMechanismPref.FORM_BASED) {
            setTarget(getFormBasedPageUrl());
        } else {
            setTarget(getDragAndDropPageUrl());
        }

        return rc;
    }
}
