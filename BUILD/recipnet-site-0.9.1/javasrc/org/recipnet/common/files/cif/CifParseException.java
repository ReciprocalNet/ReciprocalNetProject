/*
 * Reciprocal Net project
 *
 * CifParseException.java
 *
 * 26-Jan-2004: jobollin copied this class from
 *              org.recipnet.site.util.cifimporter.CifParseException
 */

package org.recipnet.common.files.cif;

/**
 * an {@code Exception} that represents a CIF syntax or semantic error.
 * {@link CifScanner}s and {@link CifParser}s in particular may
 * throw these exceptions.  Instances may carry {@code CifError} objects
 * describing the problem in more detail.
 *
 * @author John C. Bollinger
 * @version 0.9.0
 * 
 * @see CifErrorHandler
 */
public class CifParseException extends Exception {
    
    private final static long serialVersionUID = 1; 

    /**
     * A {@code CifError} describing the problem giving rise to this exception
     */
    private CifError cifError;

    /**
     * constructs a {@code CifParseException} with no message or CifError
     */
    public CifParseException() {
        super();
        cifError = null;
    }

    /**
     * constructs a {@code CifParseException} with {@code s} as
     * its message
     *
     * @param  s a {@code String} containing the message this exception
     *         should report
     */
    public CifParseException(String s) {
        super(s);
        cifError = null;
    }

    /**
     * constructs a {@code CifParseException} based on a
     * {@code CifError} object; the exception detail message will be derived
     * from the {@code CifError} via its {@code getMessage()} method
     *
     * @param  ce a {@code CifError} representing the condition that
     *         produced this exception
     */
    public CifParseException(CifError ce) {
        super(ce.getMessage());
        cifError = ce; // CifErrors are immutable
    }

    /**
     * returns the {@code CifError} carried by this exception
     *
     * @return the {@code CifError}; will be {@code null} unless
     *         this {@code CifParseException} was constructed based on
     *         a {@code CifError}
     */
    public CifError getCifError() {
        return cifError;
    }
}
