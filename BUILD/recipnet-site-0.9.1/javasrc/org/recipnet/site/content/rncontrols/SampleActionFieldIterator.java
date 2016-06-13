/*
 * Reciprocal Net project
 * 
 * SampleActionFieldIterator.java
 *
 * 10-Jun-2004: midurbin wrote first draft
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 24-Jun-2005: midurbin added isCurrentIterationLast(),
 *              getSuppressedFieldCount() and the SOME_FIELDS_WERE_SUPPRESSED
 *              error flag
 * 27-Apr-2006: jobollin updated and reformatted the source
 * 15-Jun-2006: jobollin performed further source formatting and doc updates
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.shared.SampleFieldRecord;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A custom tag that iterates through all of the fields associated with the
 * action currently described by the {@link SampleActionIterator
 * SampleActionIterator} in which this tag is nested. This tag's implementation
 * of {@code SampleFieldContext} does NOT process changes made to any of the
 * fields provided. Nested context-recognizing tags must be for display-only
 * purposes. The implementation of {@code SampleTextContext} returns useful
 * information only when the current field is an annotation or attribute.
 */
public class SampleActionFieldIterator extends HtmlPageIterator implements
        SampleFieldContext {

    /** An error flag indicating that some actions were suppressed. */
    public static final int SOME_FIELDS_WERE_SUPPRESSED
            = HtmlPageIterator.getHighestErrorFlag() << 1;

    /** Allows sublcass to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return SOME_FIELDS_WERE_SUPPRESSED;
    }

    /**
     * The {@code SampleActionIterator} that surrounds this tag; determined by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleActionIterator sampleActionIterator;

    /**
     * A subset of the {@code SampleFieldRecord}s provided by the
     * {@code SampleActionIterator}. This value is set and populated by
     * {@code onFetchingPhaseBeforeBody()} to include just those fields that are
     * not suppressed after the currently logged-in user's preferences are
     * applied.
     */
    private Collection<SampleFieldRecord> fields;

    /**
     * An iterator over the '{@link SampleActionFieldIterator#fields fields}'.
     */
    private Iterator<SampleFieldRecord> fieldIterator;

    /**
     * The {@code SampleFieldRecord} that is exposed by the
     * {@code SampleFieldContext} for this evaluation of this tag's body.
     */
    private SampleFieldRecord currentField;

    /**
     * Keeps track of the number of fields suppressed; made available through
     * {@code getSuppressedFieldCount()}.
     */
    private int suppressedFieldCount;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleActionIterator = null;
        this.fields = null;
        this.fieldIterator = null;
        this.currentField = null;
        this.suppressedFieldCount = 0;
    }

    /**
     * {@inheritDoc}; this verson looks up a
     * reference to the {@code SampleActionIterator} in which this tag must be
     * nested.
     * 
     * @throws IllegalStateException if this tag is not nested in a
     *         {@code SampleActionIterator}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        this.sampleActionIterator = findRealAncestorWithClass(this,
                SampleActionIterator.class);
        if (this.sampleActionIterator == null) {
            throw new IllegalStateException();
        }
        return rc;
    }

    /**
     * {@inheritDoc}; this version gets the
     * collection of fields from the surrounding {@code ActionIterator} and
     * removes any that that the currently logged-in user's preferences indicate
     * should be suppressed. These fields are then available to be used for the
     * iteration of this tag.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        Collection<SampleFieldRecord> allFields
                = this.sampleActionIterator.getFieldsForAction();
        boolean suppressBlankFields = UserBL.getPreferenceAsBoolean(
                UserBL.Pref.SUPPRESS_BLANK,
                (UserPreferences) this.pageContext.getSession().getAttribute(
                        "preferences"));

        if (suppressBlankFields) {
            this.fields = new ArrayList<SampleFieldRecord>();
            for (SampleFieldRecord field : allFields) {
                if (field.getValue() != null) {
                    this.fields.add(field);
                } else {
                    this.suppressedFieldCount++;
                }
            }
        } else {
            this.fields = allFields;
        }
        if (this.suppressedFieldCount != 0) {
            this.setErrorFlag(SOME_FIELDS_WERE_SUPPRESSED);
        }
        if (!this.fields.isEmpty()) {
            this.sampleActionIterator.notifyFieldDisplayed();
        }

        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void beforeIteration() {
        if (this.fields != null) {
            this.fieldIterator = fields.iterator();
        }
    }

    /**
     * {@inheritDoc}; this version sets the
     * next 'currentField' from the 'fieldIterator'.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.fields == null) {
            if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
                    && (Math.max(1, getPostedIterationCount())
                            > getIterationCountSinceThisPhaseBegan())) {
                /*
                 * it is not clear by the PARSING_PHASE how many times the body
                 * will be evaluated. In some cases, when suppression
                 * preferences change, the number of iterations posted will be
                 * zero when in fact the body should be evaluated one or more
                 * times. For the iteration count to fall to zero during the
                 * PARSING_PHASE would cause exceptions to be thrown by the
                 * superclass when the iteration count increased again, so we
                 * must always evaluate the body at least one time during the
                 * PARSING_PHASE just in case.
                 */
                return true;
            }
            return false;
        }
        if (!this.fieldIterator.hasNext()) {
            return false;
        }
        this.currentField = this.fieldIterator.next();

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCurrentIterationLast() {
        return !this.fieldIterator.hasNext();
    }

    /**
     * Provides the number of fields suppressed
     * 
     * @return the number of fields suppressed
     */
    public int getSuppressedFieldCount() {
        return this.suppressedFieldCount;
    }

    /** Implements {@code SampleFieldContext}. */
    public SampleFieldRecord getSampleField() {
        return this.currentField;
    }

    /** Implements {@code SampleTextContext}. */
    public int getTextType() {
        if (this.currentField == null) {
            return SampleTextBL.INVALID_TYPE;
        } else {
            int fieldCode = this.currentField.getFieldCode();
            
            return ((SampleTextBL.isAnnotation(fieldCode)
                    || SampleTextBL.isAnnotation(fieldCode)) ? fieldCode
                    : SampleTextBL.INVALID_TYPE);
        }
    }

    /** Implements {@code SampleTextContext}. */
    public SampleTextInfo getSampleTextInfo() {
        if (this.currentField == null) {
            throw new IllegalStateException();
        } else if (SampleTextBL.isAnnotation(this.currentField.getFieldCode())
                || SampleTextBL.isAttribute(this.currentField.getFieldCode())) {
            return (SampleTextInfo) this.currentField.getValue();
        } else {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleActionFieldIterator dc
                = (SampleActionFieldIterator) super.generateCopy(newId, map);
        
        dc.sampleActionIterator
                = (SampleActionIterator) map.get(this.sampleActionIterator);
        if (this.fields != null) {
            dc.fields = new ArrayList<SampleFieldRecord>(this.fields);
        }
        
        return dc;
    }
}
