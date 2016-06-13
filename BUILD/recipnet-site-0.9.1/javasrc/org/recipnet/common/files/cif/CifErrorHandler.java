/*
 * Reciprocal Net Project
 *
 * CifErrorHandler.java
 *
 * 26-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

/**
 * an interface defining the contract for objects that can be notified of errors
 * encountered while processing a CIF
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public interface CifErrorHandler {

    /**
     * Receives notification of a CIF processing error.  Applications that
     * notify handlers of CIF errors are expected to interrupt their processing
     * if the handler throws a {@code CifParseException}, and to continue
     * as well as possible it doesn't
     *
     * @param  error a {@code CifError} representing the error that
     *         occurred
     *
     * @throws CifParseException to signal the invoker to interrupt processing
     * @throws NullPointerException if the argument is {@code null}
     */
    void handleError(CifError error) throws CifParseException;
}

