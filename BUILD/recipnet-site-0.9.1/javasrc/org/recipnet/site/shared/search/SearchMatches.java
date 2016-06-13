/*
 * Reciprocal Net project
 * 
 * SearchMatches.java
 *
 * 15-Aug-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import org.recipnet.site.shared.SearchParams;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * An object that contains all of the {@code FieldMatchInfo} objects associated
 * with a single search and sample. The sample provided to the constructor of
 * this object must be known to match the provided search params due to its
 * inclusion in the search results returned from core.
 */
public class SearchMatches {

    /** All of the {@code FieldMatchInfo}s. */
    private final Collection<FieldMatchInfo> allMatches;

    /**
     * Initializes a new {@code SearchMatches} with the specified parameters and
     * sample info
     * 
     * @param searchParams a {@code SearchParams} describing the search for
     *        which this object maintains a collection of matching fields
     * @param sampleInfo a {@code SampleInfo} representing one sample whose
     *        fields match the search; this sample MUST have been returned as a
     *        search result for a search performed using the provided
     *        {@code searchParams}.
     * @throws IllegalArgumentException if it is determined that the sample did
     *         not match the criteria in the 'searchParams'.
     */
    public SearchMatches(SearchParams searchParams, SampleInfo sampleInfo) {
        this.allMatches = new ArrayList<FieldMatchInfo>();
        /*
         * because we know the sample matched the searchParams we may pass null
         * as the 'mismatches' param so that getMatches() may infer whether
         * certain umimplemented children match
         */
        if (!searchParams.getHead().getMatches(sampleInfo, this.allMatches,
                null)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the matches for a particular 'fieldCode' or an empty collection if
     * no specific matches were found.
     * 
     * @param fieldCode a code for a field that is not an annotation or
     *        attribute
     */
    public Collection<FieldMatchInfo> getMatchesForField(int fieldCode) {
        Collection<FieldMatchInfo> matchesForField
                = new ArrayList<FieldMatchInfo>();
        
        for (FieldMatchInfo matchInfo : allMatches) {
            if (matchInfo.getFieldCode() == fieldCode) {
                matchesForField.add(matchInfo);
            }
        }
        
        return matchesForField;
    }

    /**
     * Gets the matches for a particular {@code SampleTextInfo} or an empty
     * collection if no specific matches were found.
     * 
     * @param sti an annotation or attribute
     */
    public Collection<FieldMatchInfo> getMatchesForField(SampleTextInfo sti) {
        Collection<FieldMatchInfo> matchesForField
                = new ArrayList<FieldMatchInfo>();
        
        for (FieldMatchInfo matchInfo : allMatches) {
            if (sti.equals(matchInfo.getSampleTextInfo())) {
                matchesForField.add(matchInfo);
            }
        }
        
        return matchesForField;
    }
}
