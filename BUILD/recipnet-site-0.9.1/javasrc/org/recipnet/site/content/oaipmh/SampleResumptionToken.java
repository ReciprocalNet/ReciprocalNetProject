/*
 * Reciprocal Net Project
 *
 * SampleResumptionToken.java
 *
 * 10-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.content.oaipmh;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class representing the resumption tokens used in support of OAI sample list
 * harvesting (i.e. via "ListIdentifiers" or "ListRecords").  Resumption token
 * instances are constructed from their component parts, or parsed from their
 * corresponding {@code String} representation, and then provide means for
 * obtaining the component parts and/or the standard string representation.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class SampleResumptionToken {
    
    /**
     * A Pattern describing the form of the resumption tokens produced by this
     * servlet, and providing capturing groups so as to extract its components
     */
    private final static Pattern RESUMPTION_TOKEN_PATTERN =
            Pattern.compile("([1-9]\\d*)-([1-9]\\d*)-(.+)");

    /**
     * The string representation of this resumption token
     */
    private String stringVal;

    /**
     * The search ID represented by this resumption token
     */
    private final int searchId;

    /**
     * The start index into the search results represented by this resumption
     * token
     */
    private final int startIndex;

    /**
     * The metadata prefix represented by this resumption token
     */
    private final String metadataPrefix;
    
    /**
     * Initializes a {@code SampleResumptionToken} representing the specified
     * search ID, start index, and metadata prefix
     * 
     * @param  searchId the search ID
     * @param  start the start index
     * @param  mdPrefix the metadata prefix
     * 
     * @throws IllegalArgumentException if the search ID or start index is
     *         negative
     * @throws NullPointerException if the metadata prefix is {@code null}
     */
    public SampleResumptionToken(int searchId, int start, String mdPrefix) {
        if ((searchId < 0) || (start < 0)) {
            throw new IllegalArgumentException(
                    "Search ID and start index must be nonnegative");
        } else if (mdPrefix == null) {
            throw new NullPointerException(
                    "The metadata prefix must not be null");
        }
        this.searchId = searchId;
        this.startIndex = start;
        this.metadataPrefix = mdPrefix;
    }
    
    /**
     * Parses a resumption token string to produce a corresponding
     * SampleResumptionToken object, or throws an exception if this is not
     * possible.
     * 
     * @param  tokenString the string to parse; should be in the format produced
     *         by the {@link #toString()} method
     * 
     * @return a {@code ResumptionToken} object corresponding to the string
     *         representation
     */
    public static SampleResumptionToken fromString(String tokenString) {
        Matcher tokenMatcher =
                RESUMPTION_TOKEN_PATTERN.matcher(tokenString);
    
        if (tokenMatcher.matches()) {
            try {
                SampleResumptionToken token = new SampleResumptionToken(
                        Integer.parseInt(tokenMatcher.group(1)),
                        Integer.parseInt(tokenMatcher.group(2)),
                        tokenMatcher.group(3));
                token.stringVal = tokenString;
                return token;
            } catch (NumberFormatException nfe) {
                // drop through
            }
        }
        
        throw new IllegalArgumentException(
                "Invalid resumption token '" + tokenString + "'");
    }

    /**
     * Returns the metadata prefix specified by this token
     * 
     * @return the metadata prefix {@code String}
     */
    public String getMetadataPrefix() {
        return metadataPrefix;
    }

    /**
     * Returns the search ID specified by this token
     * 
     * @return the search ID
     */
    public int getSearchId() {
        return searchId;
    }

    /**
     * Returns the start index specified by this token
     * 
     * @return the startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }
    
    /**
     * Returns a {@code String} representation of this resumption token in
     * the same format decoded by the {@link #fromString(String)} method
     * 
     * @return a {@code String} representation of this
     *         {@code SampleResumptionToken}
     */
    @Override
    public String toString() {
        if (stringVal == null) {
            StringBuilder builder = new StringBuilder();
            
            builder.append(searchId);
            builder.append('-');
            builder.append(startIndex);
            builder.append('-');
            builder.append(metadataPrefix);
            
            stringVal = builder.toString();
        }
        
        return stringVal;
    }
}
