/*
 * Reciprocal Net Project
 * 
 * CifWarning.java
 * 
 * 02-02-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

/**
 * A CifError subclass that represents an unambiguously recoverable CIF syntax,
 * parse, or semantic error.  It may contain an internal {@code CifError} object
 * that precisely describes the nature of the problem.
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifWarning extends CifError {
    private final CifError baseError;
    private final String errorMessage;

    /**
     * Initializes a new {@code CifWarning} with the specified message and no
     * internal {@code CifError} 
     * 
     * @param  message the warning message for this {@code CifWarning}; may be
     *         {@code null}
     */
    public CifWarning(String message) {
        this(message, null);
    }

    /**
     * Initializes a new {@code CifWarning} with the specified base
     * {@code CifError} and no warning message
     * 
     * @param  baseError the base {@code CifError} for this {@code CifWarning};
     *         may be {@code null}
     */
    public CifWarning(CifError baseError) {
        this(null, baseError);
    }
    
    /**
     * Initializes a new {@code CifWarning} with the specified warning message
     * and base {@code CifError}
     * 
     * @param  message the warning message for this {@code CifWarning}; may be
     *         {@code null}
     * @param  baseError the base {@code CifError} for this {@code CifWarning};
     *         may be {@code null}
     */
    public CifWarning(String message, CifError baseError) {
        StringBuffer sb = new StringBuffer();
        
        this.baseError = baseError;
        sb.append((message == null) ? "CIF Warning" : message);
        if ((baseError != null) && (baseError.getCode() != 0)) {
            sb.append("; root problem: ");
            sb.append(baseError.getMessage());
        }
        errorMessage = sb.toString();
    }

    /**
     * {@inheritDoc}.  This version returns the error code of the base
     * {@code CifError} if there is one, or zero otherwise
     *
     * @return {@inheritDoc} 
     * 
     * @see org.recipnet.common.files.cif.CifError#getCode()
     */
    @Override
    public int getCode() {
        return (baseError == null) ? 0 : baseError.getCode();
    }

    /**
     * {@inheritDoc}
     * 
     * @return {@inheritDoc}
     * 
     * @see org.recipnet.common.files.cif.CifError#getMessage()
     */
    @Override
    public String getMessage() {
        return errorMessage;
    }
}
