/*
 * Reciprocal Net project
 * 
 * SuppressedActionCount.java
 * 
 * 24-Jun-2005: midurbin wrote the first draft
 * 28-Oct-2005: midurbin added 'includeSkippedActionsInCount' property
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

import org.recipnet.common.controls.HtmlPageElement;

/**
 * A simple class that displays the number of actions of the given types that
 * were suppressed by a specified {@code SampleActionIterator}
 */
public class SuppressedActionCount extends HtmlPageElement {

    /**
     * A required property indicating the {@code SampleActionFieldIterator}
     * whose suppressed field count is to be displayed by this tag.
     */
    private SampleActionIterator actionIterator;

    /**
     * An optional property that defaults to false but when set to true
     * indicates that the number output by this tag should include the number of
     * file actions suppressed by the provided 'actionIterator'.
     */
    private boolean includeFileActionsInCount;

    /**
     * An optional property that defaults to false but when set to true
     * indicates that the number output by this tag should include the number of
     * correction actions suppressed by the provided 'actionIterator'.
     */
    private boolean includeCorrectionActionsInCount;

    /**
     * An optional property that defaults to false but when set to true
     * indicates that the number output by this tag should include the number of
     * non-file, non-correction actions suppressed by the provided
     * 'actionIterator'.
     */
    private boolean includeOtherActionsInCount;

    /**
     * An optional property that defaults to false but when set to true
     * indicates that the number output by this tag should include the number of
     * actions suppressed by the provided 'actionIterator' because they were
     * skipped by reversion.
     */
    private boolean includeSkippedActionsInCount;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.actionIterator = null;
        this.includeFileActionsInCount = false;
        this.includeCorrectionActionsInCount = false;
        this.includeOtherActionsInCount = false;
        this.includeSkippedActionsInCount = false;
    }

    /** Sets the 'actionIterator' property. */
    public void setActionIterator(SampleActionIterator it) {
        this.actionIterator = it;
    }

    /** Gets the 'actionIterator' property. */
    public SampleActionIterator getActionIterator() {
        return this.actionIterator;
    }

    /** Sets the 'includeFileActionsInCount' property. */
    public void setIncludeFileActionsInCount(boolean include) {
        this.includeFileActionsInCount = include;
    }

    /** Gets the 'includeFileActionsInCount' property. */
    public boolean getIncludeFileActionInCount() {
        return this.includeFileActionsInCount;
    }

    /** Sets the 'includeCorrectionActionsInCount' property. */
    public void setIncludeCorrectionActionsInCount(boolean include) {
        this.includeCorrectionActionsInCount = include;
    }

    /** Gets the 'includeCorrectionActionsInCount' property. */
    public boolean getIncludeCorrectionActionsInCount() {
        return this.includeCorrectionActionsInCount;
    }

    /** Sets the 'includeOtherActionsInCount' property. */
    public void setIncludeOtherActionsInCount(boolean include) {
        this.includeOtherActionsInCount = include;
    }

    /** Gets the 'includeOtherActionsInCount' property. */
    public boolean getIncludeOtherActionsInCount() {
        return this.includeOtherActionsInCount;
    }

    /** Sets the 'includeSkippedActionsInCount' property. */
    public void setIncludeSkippedActionsInCount(boolean include) {
        this.includeSkippedActionsInCount = include;
    }

    /** Gets the 'includeSkippedActionsInCount' property. */
    public boolean getIncludeSkippedActionsInCount() {
        return this.includeSkippedActionsInCount;
    }

    /**
     * {@inheritDoc}; this version outputs the number of actions suppressed by
     * configured {@code SampleActionIterator} that match the types specified by
     * various properties to this tag.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        int count = (this.includeFileActionsInCount
                ? this.actionIterator.getSuppressedEmptyFileActionCount()
                : 0)
                + (this.includeCorrectionActionsInCount
                ? this.actionIterator.getSuppressedEmptyCorrectionActionCount()
                : 0)
                + (this.includeOtherActionsInCount
                ? this.actionIterator.getSuppressedEmptyOtherActionCount()
                : 0)
                + (this.includeSkippedActionsInCount
                ? this.actionIterator.getSuppressedSkippedActionCount()
                : 0);

        out.print(count);
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}; this version implements transiency for the
     * 'fieldIterator' attribute
     */
    @Override
    public void copyTransientPropertiesFrom(HtmlPageElement source) {
        // the actionIterator isn't strictly transient
        this.actionIterator = ((SuppressedActionCount) source).actionIterator;
    }
}
