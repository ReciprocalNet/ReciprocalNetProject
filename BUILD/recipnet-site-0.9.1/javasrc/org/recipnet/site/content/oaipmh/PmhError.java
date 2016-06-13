/*
 * Reciprocal Net Project
 *
 * PmhError.java
 *
 * 08-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.content.oaipmh;

/**
 * An enum of the protocol errors defined by OAI-PMH version 2.0
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public enum PmhError {
    
    /**
     * An PmhError representing a case where an illegal protocol argument
     * was detected
     */
    BAD_ARGUMENT("badArgument", "Invalid, duplicate, or missing argument"),
    
    /**
     * An PmhError representing a case where an incorrect resumption token
     * was detected
     */
    BAD_RESUMPTION_TOKEN("badResumptionToken",
            "Illegal or expired resumption token"),
    
    /**
     * An PmhError representing a case where the verb could not be
     * determined -- either because it was not present, because it was not
     * among the known verbs, or because multiple verbs were specified
     */
    BAD_VERB("badVerb", "Unrecognized, duplicate, or missing OAI-PMH verb"),
    
    /**
     * An PmhError representing a case where the requested record cannot be
     * provided in the requested metadata format
     */
    CANNOT_DISSEMINATE_FORMAT("cannotDisseminateFormat",
            "Unsupported metadata format"),
            
    /**
     * An PmhError representing a case where the requested record cannot be
     * provided because the specified ID does not correspond to any item in
     * the repository
     */
    ID_DOES_NOT_EXIST("idDoesNotExist",
            "No such identifier in this repository"),
    
    /**
     * An PmhError representing a case where no records satisfy the criteria
     * for inclusion in a requested list
     */
    NO_RECORDS_MATCH("noRecordsMatch",
            "No records match all the specified criteria"),
    
    /**
     * An PmhError representing a where the requested item exists but no
     * metadata formats for it are available (which will never happen in the
     * current implementation)
     */
    NO_METADATA_FORMATS("noMetadataFormats",
            "Metadata is not available for the specified item in any format"),
            
    /**
     * An PmhError representing a where a set was specified, but sets are
     * not supported by the repository (which will never happen in the
     * current implementation)
     */
    NO_SET_HIERARCHY("noSetHierarchy", "This repository does not support sets");

    /**
     * The error code for this verb, as required for the code attribute of an
     * OAI-PMH error element
     */
    private final String errorCode;
    
    /**
     * A text description of this error, to be used as part or all of the error
     * message delivered in an OAI-PMH error element
     */
    private final String errorText;
    
    /**
     * Initializes a {@code PmhError} with the specified error code and text
     * 
     * @param  code the error code for this error
     * @param  description the base error text for this error
     */
    PmhError(String code, String description) {
        errorCode = code;
        errorText = description;
    }
    
    /**
     * Returns the error code for this error
     *  
     * @return the {@code String} error code for this verb, as required for the
     *         code attribute of an OAI-PMH error element
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Returns the base error message for this error
     *  
     * @return the {@code String} text description of this error, suitable for
     *         use as part or all of an error message delivered in an OAI-PMH
     *         error element
     */
    public String getErrorDescription() {
        return errorText;
    }
}
