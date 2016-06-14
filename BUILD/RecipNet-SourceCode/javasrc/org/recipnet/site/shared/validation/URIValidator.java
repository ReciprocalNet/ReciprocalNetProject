/*
 * Reciprocal Net Project
 *
 * URIValidator.java
 *
 * 25-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.shared.validation;

import java.net.URI;
import java.net.URISyntaxException;

import org.recipnet.common.Validator;

/**
 * A {@code Validator} that validates a string as conforming to the standard URI
 * syntax.
 *
 * @author jobollin
 * @version 0.9.0
 */
public class URIValidator implements Validator {

    /**
     * In order to be valid, the object must be a {@code String} and conform to
     * the URI syntax of RFC 2396, as amended by RFC 2732.  The URI class is
     * used to perform the validation.
     *
     * @param  obj the object to validate
     * 
     * @return {@code true} if the object is a valid URI string,
     *         {@code false} if it isn't
     *     
     * @throws NullPointerException if the argument is {@code null}
     */
    public boolean isValid(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        } else if (! (obj instanceof String)) {
            return false;
        }
        try {
            /*
             * The URI(String) constructor tests the string for correct syntax;
             * we don't need to retain the reference when it's done.
             */
            new URI((String) obj);
            
            // If we reach this point, the String is OK
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
