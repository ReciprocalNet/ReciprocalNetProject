/*
 * Reciprocal Net Project
 *
 * PmhException.java
 *
 * 18-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.content.oaipmh;

import org.recipnet.site.OperationFailedException;

/**
 * An {@code OperationFailedException} indicating representing an OAI-PMH
 * protocol error.  The specific nature of the error is available as an error
 * type object of type {@code PmhError}.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class PmhException extends OperationFailedException {
    
    /**
     * A default serialVersionUID because this class inherits
     * {@code Serializable} from {@code Exception}
     */
    private static final long serialVersionUID = 1L;

    /**
     * The error type represented by this exception
     */
    private final PmhError errorType;
    
    /**
     * Initializes an {@code PmhException} with the specified error type and
     * detail message
     * 
     * @param  error an {@code PmhError} representing the specific type of OAI
     *         protocol error that this exception represents 
     * @param  message the detail message for this exception
     */
    public PmhException(PmhError error, String message) {
        super(message);
        errorType = error;
    }

    /**
     * Returns the OAI error type represented by this exception
     * 
     * @return the error type as an {@code PmhError}
     */
    public PmhError getErrorType() {
        return errorType;
    }
}
