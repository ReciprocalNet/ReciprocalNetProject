/*
 * Reciprocal Net Project
 * 
 * CifErrorAlerter.java
 * 
 * 09-Feb-2005: jobollin wrote first draft
 */
 
package org.recipnet.common.files.cif;

/**
 * A simple CifErrorHandler implementation that throws a CifParseException
 * whenever it is notified of an error
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class CifErrorAlerter implements CifErrorHandler {

    /**
     * {@inheritDoc}.  This version raises an exception every time.
     * 
     * @throws CifParseException every time it is invoked
     * @throws NullPointerException if the argument is {@code null}
     */
    public void handleError(CifError error) throws CifParseException {
        throw new CifParseException(error);
    }

}
