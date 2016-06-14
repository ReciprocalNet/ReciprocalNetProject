/*
 * Reciprocal Net Project
 *
 * FileFormatException.java
 *
 * 20-Nov-2002: jobollin moved this class from org.recipnet.site.jamm to *.misc
 * 07-Jan-2003: jobollin modified javadoc comments
 * 18-Feb-2003: jobollin moved this exception into the Reciprocal Net exception
 *              tree (task #733) and changed the name of the constructor's
 *              parameter
 * 21-Feb-2003: jobollin reformatted the source as part of task #746
 * 21-Mar-2003: jobollin added a new constructor to support task #629
 * 24-Mar-2003: jobollin modified the two-arg constructor as part of task #808
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.site.wrapper
 * 09-Jan-2004: ekoperda added 1-arg constructor for cause
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.wrapper;

import org.recipnet.site.RecipnetException;

/**
 * An exception indicating that a file being examined did not conform to the
 * expected format
 * 
 * @author John C. Bollinger
 * @version 0.6.0
 */
public class FileFormatException extends RecipnetException {

    /**
     * Constructs a new FileFormatException with the specified detail message
     * 
     * @param message a {@code String} containing the detail message for this
     *        exception
     */
    public FileFormatException(String message) {
        super(message);
    }

    /**
     * Constructs a new FileFormatException with the specified detail message
     * and cause
     * 
     * @param message a {@code String} containing the detail message for this
     *        exception
     * @param cause the {@code Throwable} cause of this exception
     */
    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new FileFormatException from the specified cause.
     * 
     * @param cause the {@code Throwable} cause of this exception.
     */
    public FileFormatException(Throwable cause) {
        super(cause);
    }
}
