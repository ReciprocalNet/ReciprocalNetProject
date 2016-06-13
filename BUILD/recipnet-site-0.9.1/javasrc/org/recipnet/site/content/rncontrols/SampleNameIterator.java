/*
 * Reciprocal Net project
 * 
 * SampleNameIterator.java
 *
 * 01-Apr-2005: midurbin wrote first draft
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * <p>
 * An custom tag that evaluates its body for each {@code SampleTextInfo} that
 * represents a sample name. The sample names included are those returned by
 * {@link org.recipnet.site.shared.bl.SampleTextBL#getSampleNames
 * SampleTextBL.getSampleNames()} less those eliminated by the various
 * properties exposed on this tag.
 * </p><p>
 * This tag should not expose the superclass' sorting properties as the sort
 * order is dictated by {@code getFilteredTextInfoCollection()} to be the order
 * of preference.
 * </p>
 */
public class SampleNameIterator extends AbstractSampleTextIterator {

    /**
     * An optional property that if set excludes the first sample name ordered
     * by decreasing preference. This excluded name may be of any texttype
     * regardless of whether other properties would have explicitly excluded it.
     * In other words, this exclusion property takes effect before any
     * type-based exclusion properties. This property defaults to false.
     */
    private boolean excludePreferredName;

    /**
     * An optional property that if set excludes all but the first sample name
     * ordered by decreasing preference. The remaining name may be of any type
     * but is subject to explicit exclusion if it is of one of the types
     * excluded by the various properties. Like 'excludePreferredName' this
     * property take effect before any type-based exclusions. This property
     * defaults to false.
     */
    private boolean excludeAllButPreferredName;

    /**
     * An optional property that defaults to false. If set to true, none of the
     * sample's {@code COMMON_NAME} attributes will be included in those
     * {@code SampleTextInfo}s over which this tag iterates. If true they will
     * be included unless excluded by another property.
     */
    private boolean excludeCommonNames;

    /**
     * An optional property that defaults to false. If set to true, none of the
     * sample's {@code TRADE_NAME} attributes will be included in those
     * {@code SampleTextInfo}s over which this tag iterates. If true they will
     * be included unless excluded by another property.
     */
    private boolean excludeTradeNames;

    /**
     * An optional property that defaults to false. If set to true, none of the
     * sample's {@code IUPAC_NAME} attributes will be included in those
     * {@code SampleTextInfo}s over which this tag iterates. If true they will
     * be included unless excluded by another property.
     */
    private boolean excludeIUPACNames;

    /** {@inheritDoc} */
    @Override
    public void reset() {
        super.reset();
        this.excludePreferredName = false;
        this.excludeAllButPreferredName = false;
        this.excludeCommonNames = false;
        this.excludeTradeNames = false;
        this.excludeIUPACNames = false;
    }

    /**
     * @param exclude indicates that the most preferred sample name is to be
     *        excluded from consideration
     */
    public void setExcludePreferredName(boolean exclude) {
        this.excludePreferredName = exclude;
    }

    /**
     * @return a boolean that indicates that the most preferred sample name is
     *         to be excluded from consideration
     */
    public boolean getExcludePreferredName() {
        return this.excludePreferredName;
    }

    /**
     * @param exclude indicates that all names other than the most preferred
     *        sample name are to be excluded from consideration
     */
    public void setExcludeAllButPreferredName(boolean exclude) {
        this.excludeAllButPreferredName = exclude;
    }

    /**
     * @return a boolean that indicates that all names other than the most
     *         preferred sample name are to be excluded from consideration
     */
    public boolean getExcludeAllButPreferredName() {
        return this.excludeAllButPreferredName;
    }

    /**
     * @param exclude indicates that all {@code COMMON_NAME}s are to be
     *        excluded from consideration
     */
    public void setExcludeCommonNames(boolean exclude) {
        this.excludeCommonNames = exclude;
    }

    /**
     * @return a boolean that indicates whether all {@code COMMON_NAME}s are to
     *         be excluded from consideration
     */
    public boolean getExcludeCommonNames() {
        return this.excludeCommonNames;
    }

    /**
     * @param exclude indicates that all {@code TRADE_NAME}s are to be excluded
     *        from consideration
     */
    public void setExcludeTradeNames(boolean exclude) {
        this.excludeTradeNames = exclude;
    }

    /**
     * @return a boolean that indicates whether all {@code TRADE_NAME}s are to
     *         be excluded from consideration
     */
    public boolean getExcludeTradeNames() {
        return this.excludeTradeNames;
    }

    /**
     * @param exclude indicates that all {@code IUPAC_NAME}s are to be excluded
     *        from consideration
     */
    public void setExcludeIUPACNames(boolean exclude) {
        this.excludeIUPACNames = exclude;
    }

    /**
     * @return a boolean that indicates whether all {@code IUPAC_NAME}s are to
     *         be excluded from consideration
     */
    public boolean getExcludeIUPACNames() {
        return this.excludeIUPACNames;
    }

    /**
     * {@inheritDoc}; this version returns a {@code Collection} of
     * {@code SampleTextInfo} objects for sample name annotations and attributes
     * that were not explicitly excluded by the various properties of this tag.
     * This {@code Collection} is sorted by preference and but does not
     * neccessarily include any entries.
     */
    @Override
    public Collection<SampleTextInfo> getFilteredTextInfoCollection(
            SampleInfo sampleInfo) {
        Collection<SampleTextInfo> textInfos = new ArrayList<SampleTextInfo>();

        if (sampleInfo != null) {
            boolean isFirstIteration = true;
            
            for (SampleTextInfo nextName
                    : SampleTextBL.getSampleNames(sampleInfo, true)) {
                if (this.excludePreferredName && isFirstIteration) {
                    /*
                     * to exclude the preferred name, we know that the
                     * collection of names is sorted by preference and skip the
                     * first (most preferred) name.
                     */
                } else if (this.excludeAllButPreferredName && !isFirstIteration) {
                    break;
                } else {
                    if (((nextName.type == SampleTextBL.COMMON_NAME)
                            && !this.excludeCommonNames)
                            || ((nextName.type == SampleTextBL.TRADE_NAME)
                                    && !this.excludeTradeNames)
                            || ((nextName.type == SampleTextBL.IUPAC_NAME)
                                    && !this.excludeIUPACNames)) {
                        textInfos.add(nextName);
                    }
                }
                isFirstIteration = false;
            }
        }
        
        return textInfos;
    }
}
