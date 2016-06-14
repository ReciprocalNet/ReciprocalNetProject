/*
 * Reciprocal Net project
 * 
 * AbstractSampleTextIterator.java
 *
 * 01-Apr-2005: midurbin wrote the first draft by refactoring code from
 *              SampleTextIterator
 * 21-Jun-2005: midurbin moved the ErrorSupplier implementation to the
 *              superclass and added isCurrentIterationLast()
 * 23-Mar-2006: jobollin reformatted the source and removed unused imports; made
 *              getFilteredTextInfoCollection() abstract
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * <p>
 * An abstract base class that provides almost all of the required functionality
 * for an {@code HtmlPageIterator} that iterates over a sequence of
 * {@code SampleTextInfo} objects that are provided through the
 * {@code SampleTextContext} interface.
 * </p><p>
 * This class recognizes (and in fact must be nested within) a
 * {@code SampleContext}. That {@code SampleContext}, {@code SampleInfo} is
 * provided to the one method that subclasses must override,
 * {@code getFilteredTextInfoCollection()}.
 * </p><p>
 * If the {@code SampleTextInfo} provided by this {@code SampleTextContext} has
 * its value set to null, it will be removed from the {@code SampleInfo} at the
 * end of the {@code PROCESSING_PHASE}.
 * </p><p>
 * Basic sorting functionality is implemented by this class and subclasses may
 * either expose it as the optional attribute 'sortByTextTypeName' or simply
 * dictate the sort order. (in which case 'sortByTextTypeName' would not be
 * included as a tag attribute in the TLD file.
 * </p>
 */
public abstract class AbstractSampleTextIterator extends HtmlPageIterator
        implements SampleTextContext {

    /**
     * The most immediate {@code SampleContext}; determined and set by
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private SampleContext sampleContext;

    /**
     * The {@code SampleTextInfo} that will be provided to implement
     * {@code SampleTextContext} during this body evaluation. Set by
     * {@code onIterationBeforeBody()} to a value from
     * {@code filteredTextRecords}.
     */
    private SampleTextInfo currentSampleTextInfo;

    /**
     * A {@code List} of {@code SampleTextRecord} objects that correspond to
     * {@code SampleTextInfo}s for the attributes and annotations whose text
     * types are consistent with the {@code RestrictBy...} restriction
     * properties. These records contain the original value so that it may be
     * determined if their values were modified. If 'sortByTextTypeName' is set,
     * the 'localizedTextType' member of {@code SampleTextRecord} is set and
     * this collection is sorted by that field.
     */
    private List<SampleTextRecord> filteredTextRecords;

    /**
     * An {@code Iterator} over {@code filteredTextRecords}, initialized by
     * {@code beforeIteration()} and accessed by
     * {@code onIterationBeforeBody()}.
     */
    private Iterator<SampleTextRecord> textRecordIterator;

    /**
     * An optional property that causes the {@code SampleTextInfo} objects over
     * which this class iterates to be exposed in order by the localized
     * {@code String} that describes the text type. This property defaults to
     * false leaving the {@code SampleTextInfo} objects over which this tag
     * iterates in the order that was returned by
     * {@code getFilteredSampleTextInfoCollection()}.
     */
    private boolean sortByTextTypeName;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.sampleContext = null;
        this.currentSampleTextInfo = null;
        this.filteredTextRecords = null;
        this.textRecordIterator = null;
        this.sortByTextTypeName = false;
    }

    /**
     * @param sort indicated whether the {@code SampleTextInfo}'s returned by
     *        this {@code SampleTextIterator} will be sorted alphabetically by
     *        their localized text type names.
     */
    public void setSortByTextTypeName(boolean sort) {
        this.sortByTextTypeName = sort;
    }

    /**
     * @return a boolean that indicates whether the {@code SampleTextInfo}
     *         objects returned by this {@code SampleTextIterator} will be
     *         sorted alphabetically by their localized text type names.
     */
    public boolean getSortByTextTypeName() {
        return this.sortByTextTypeName;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets a
     * reference to the most immediately enclosing {@code SampleContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a required
     *         context.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get SampleContext
        this.sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        if (this.sampleContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation populates
     * {@code filteredTextRecords} with all the {@code SampleTextInfo} objects
     * returned by {@code getFilteredTextInfoCollection()}. If
     * 'sortByTextTypeName' is true, {@code filteredTextRecords} will then be
     * sorted.
     * 
     * @throws JspException either to propagate a {@code JspException} thrown by
     *         {@code getFilteredTextInfoCollection()} or to wrap an
     *         {@code IOException} or {@code ResourceNotFoundException} if any
     *         are thrown by the constructor for {@code SampleTextRecord}
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        this.filteredTextRecords = new ArrayList<SampleTextRecord>();
        for (SampleTextInfo sti : getFilteredTextInfoCollection(
                this.sampleContext.getSampleInfo())) {
            try {
                this.filteredTextRecords.add(new SampleTextRecord(sti,
                        getSortByTextTypeName() ? this.pageContext : null));
            } catch (IOException ex) {
                throw new JspException(ex);
            } catch (ResourceNotFoundException ex) {
                throw new JspException(ex);
            }
        }
        if (getSortByTextTypeName()) {
            Collections.sort(this.filteredTextRecords);
        }

        return rc;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation verifies
     * that the number of iterations posted is equal to the number of iterations
     * and removes any {@code SampleTextInfo} objects with a value of null from
     * the {@code SampleInfo} object.
     * 
     * @throws IllegalStateException if the number of iterations posted is not
     *         equal to the number of iterations, which indicates either an
     *         error in the POST data or a simultaneous modification to the
     *         sample. This exception may also be thrown if annotations or
     *         attributes that existed during the fetching phase no longer exist
     *         on the sample.
     */
    @Override
    public int onProcessingPhaseAfterBody(PageContext pageContext)
            throws JspException {
        int rc = super.onProcessingPhaseAfterBody(pageContext);

        // filteredTextRecords was initialized (maybe to an empty collection)
        // during the fetching phase and cannot be null here. Furthermore
        // it should have the same number of elements as were posted
        assert (this.filteredTextRecords != null);
        if (this.filteredTextRecords.size() != getPostedIterationCount()) {
            throw new IllegalStateException();
        }

        // get sampleInfo
        SampleInfo sampleInfo = this.sampleContext.getSampleInfo();

        // check for entries whose values were set to null and remove them from
        // the SampleInfo
        for (SampleTextRecord rec : this.filteredTextRecords) {
            if (rec.sampleTextInfo.value == null) {
                // delete the cleared text entry
                if (SampleTextBL.isAnnotation(rec.sampleTextInfo.type)) {
                    sampleInfo.annotationInfo.remove(rec.sampleTextInfo);
                } else {
                    sampleInfo.attributeInfo.remove(rec.sampleTextInfo);
                }
            } else if (!rec.sampleTextInfo.value.equals(rec.originalValue)) {
                // replace the existing text entry
                if (SampleTextBL.isAnnotation(rec.sampleTextInfo.type)) {
                    sampleInfo.annotationInfo.remove(rec.sampleTextInfo);
                    sampleInfo.annotationInfo.add(new SampleAnnotationInfo(
                            rec.sampleTextInfo.type, rec.sampleTextInfo.value));
                } else {
                    sampleInfo.attributeInfo.remove(rec.sampleTextInfo);
                    sampleInfo.attributeInfo.add(new SampleAttributeInfo(
                            rec.sampleTextInfo.type, rec.sampleTextInfo.value));
                }
            }
        }
        
        return rc;
    }

    /**
     * An abstract method that returns a {@code Collection} of all the
     * {@code SampleTextInfo} objects over which this tag should iterate. If no
     * sort order has been specified to the abstract base class, the order will
     * be preserved, otherwise the elements will be sorted as specified on
     * {@code AbstractSampleTextIterator}.
     * 
     * @param sampleInfo the {@code SampleInfo} provided by the most immediately
     *        enclosing {@code SampleContext}. This value may be null in the
     *        event that the {@code SampleTextContext} provided a null value.
     * @return a {@code Collection} that represents the annotations/attributes
     *         over which this tag should iterate. This may be an empy
     *         {@code Collection} but may not be null.
     *         
     * @throws JspException (not thrown by {@code AbstractSampleTextIterator})
     */
    public abstract Collection<SampleTextInfo> getFilteredTextInfoCollection(
            SampleInfo sampleInfo) throws JspException;

    /**
     * Implements {@code SampleTextContext}.
     * 
     * @throws IllegalStateException if sampleTextInfo is null because it is not
     *         yet the {@code FETCHING_PHASE}.
     */
    public SampleTextInfo getSampleTextInfo() {
        if (this.currentSampleTextInfo == null) {
            throw new IllegalStateException();
        }
        return this.currentSampleTextInfo;
    }

    /**
     * Implements {@code SampleTextContext}. This method will return
     * {@code SampleTextInfo.INVALID_TEXT_TYPE} until the {@code FETCHING_PHASE}
     * when it will return the text type of the {@code SampleTextInfo} returned
     * by {@code getSampleTextInfo()}.
     */
    public int getTextType() {
        if (this.currentSampleTextInfo == null) {
            return SampleTextInfo.INVALID_TYPE;
        }
        return this.currentSampleTextInfo.type;
    }

    /**
     * Overrides {@code HtmlPageIterator} to return true during the last
     * iteration of the {@code RENDERING_PHASE}.
     * 
     * @throws IllegalStateException if called before the
     *         {@code RENDERING_PHASE}
     */
    @Override
    public boolean isCurrentIterationLast() {
        if (getPage().getPhase() != HtmlPage.RENDERING_PHASE) {
            throw new IllegalStateException();
        }
        return !this.textRecordIterator.hasNext();
    }

    /**
     * Overrides {@code HtmlPageIterator} to perform initialization needed
     * before iteration of the body.
     */
    @Override
    protected void beforeIteration() {
        if (this.filteredTextRecords != null) {
            this.textRecordIterator = filteredTextRecords.iterator();
        }
    }

    /**
     * Overrides {@code HtmlPageIterator} to cause the body to be evaluated once
     * for each local tracking attribute type that should be displayed on the
     * current {@code WapPage}.
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if (this.filteredTextRecords == null) {
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
        if (this.textRecordIterator.hasNext()) {
            this.currentSampleTextInfo
                    = textRecordIterator.next().sampleTextInfo;
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        AbstractSampleTextIterator dc
                = (AbstractSampleTextIterator) super.generateCopy(newId, map);
        
        dc.sampleContext = (SampleContext) map.get(this.sampleContext);
        if (this.filteredTextRecords != null) {
            dc.filteredTextRecords = new ArrayList<SampleTextRecord>(
                    this.filteredTextRecords);
        }
        
        return dc;
    }

    /**
     * A nested class to facillitate determining whether a
     * {@code SampleTextInfo} was changed by a caller of
     * {@code getSampleTextInfo()} that may be sorted by the localized text type
     * name of the contained {@code SampleTextInfo}.
     */
    protected static class SampleTextRecord
            implements Comparable<SampleTextRecord> {

        public SampleTextInfo sampleTextInfo;

        public String originalValue;

        public String localizedTextTypeName;

        /**
         * A constructor. If {@code PageConext} is provided it will populates
         * {@code localizedTextTypeName} so that the comparator may be used,
         * otherwise the comparator should not be used.
         */
        public SampleTextRecord(SampleTextInfo sti, PageContext pageContext)
                throws IOException, ResourceNotFoundException {
            this.sampleTextInfo = sti;
            this.originalValue = sti.value;
            if (pageContext != null) {
                this.localizedTextTypeName = LanguageHelper.extract(
                        pageContext.getServletContext()).getFieldString(
                        sti.type, pageContext.getRequest().getLocales(), false);
            } else {
                this.localizedTextTypeName = null;
            }
        }

        /**
         * A comparator that may be used as long as
         * {@code localizedTextTypeName} has been populated on both objects.
         */
        public int compareTo(SampleTextRecord record) {
            return this.localizedTextTypeName.compareTo(
                    record.localizedTextTypeName);
        }
    }
}
