/*
 * Reciprocal Net project
 * 
 * FieldMatchInfo.java
 *
 * 15-Aug-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */
package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A container for information to describe the smallest possible match between
 * a SearchConstraint and a field on a SampleInfo. 
 * Any possible match may be expressed with one or more of these objects.
 */
public class FieldMatchInfo {

    /**
     * Indicates the matched field (for non multi-valued fields).  This will be
     * set to SampleTextBL.INVALID_TYPE if a specific field is
     * indicated by the 'textInfo' variable.
     */
    private final int fieldCode;

    /**
     * Indicates the matching SampleTextInfo.  This will be set
     * to null if a 'fieldCode' is specified.
     */
    private final SampleTextInfo textInfo;

    /** Indicates the matching part. */
    private final MatchingPart matchingPart;

    /**
     * The SearchConstraint that generated this
     * FieldMatchInfo either as a 'match' or a 'mismatch' in the 
     * context of that single SearchConstraint that
     * SearchMatches knows to be contributing to a match.
     */
    @SuppressWarnings("unused")
    private final SearchConstraint searchConstraint;

    /** Indicates the sample being matched. */
    @SuppressWarnings("unused")
    private final SampleInfo sampleInfo;

    public FieldMatchInfo(int fieldCode, SearchConstraint sc, SampleInfo si) {
        this.fieldCode = fieldCode;
        this.textInfo = null;
        this.matchingPart = new MatchingPart();
        this.searchConstraint = sc;
        this.sampleInfo = si;
    }

    public FieldMatchInfo(int fieldCode, MatchingPart matchingPart,
            SearchConstraint sc, SampleInfo si) {
        this.fieldCode = fieldCode;
        this.textInfo = null;
        this.matchingPart = matchingPart;
        this.searchConstraint = sc;
        this.sampleInfo = si;
    }

    public FieldMatchInfo(SampleTextInfo sampleTextInfo, SearchConstraint sc,
            SampleInfo si) {
        this.fieldCode = SampleTextBL.INVALID_TYPE;
        this.textInfo = sampleTextInfo;
        this.matchingPart = new MatchingPart();
        this.searchConstraint = sc;
        this.sampleInfo = si;
    }

    public FieldMatchInfo(SampleTextInfo sampleTextInfo,
            MatchingPart matchingPart, SearchConstraint sc, SampleInfo si) {
        this.fieldCode = SampleTextBL.INVALID_TYPE;
        this.textInfo = sampleTextInfo;
        this.matchingPart = matchingPart;
        this.searchConstraint = sc;
        this.sampleInfo = si;
    }

    /** Gets the matching part. */
    public MatchingPart getMatchingPart() {
        return this.matchingPart;
    }

    /** Gets the 'fieldCode'. */
    public int getFieldCode() {
        return this.fieldCode;
    }

    /** Gets the 'sampleTextInfo'. */
    public SampleTextInfo getSampleTextInfo() {
        return this.textInfo;
    }

    /** 
     * A class whose members contain information describing a match between two
     * objects.
     */
    public static class MatchingPart {
        private final int beginMatchIndex;
        private final int matchLength;

        public MatchingPart() {
            this.beginMatchIndex = -1;
            this.matchLength = -1;
        }

        public MatchingPart(int begin, int length) {
            this.beginMatchIndex = begin;
            this.matchLength = length;
        }

        public boolean matchesWhole() {
            return (this.beginMatchIndex == -1) && (this.matchLength == -1);
        }

        public int getBeginIndex() {
            return this.beginMatchIndex;
        }

        public int getMatchLength() {
            return this.matchLength;
        }
    }
}
