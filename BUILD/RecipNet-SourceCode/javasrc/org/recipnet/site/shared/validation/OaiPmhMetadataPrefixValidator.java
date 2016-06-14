/*
 * Reciprocal Net Project
 *
 * OaiPmhMetadataPrefixValidator.java
 *
 * 25-Oct-2005: jobollin wrote first draft
 */

package org.recipnet.site.shared.validation;

import java.util.regex.Pattern;

import org.recipnet.common.Validator;

/**
 * A {@code Validator} that validates a string as conforming to the OAI-PMH
 * metadata prefix syntax, as defined in the OAI-PMH specification.  This
 * validator's criteria do not involve any considerations specific to the
 * Reciprocal Net. 
 *
 * @author jobollin
 * @version 0.9.0
 */
public class OaiPmhMetadataPrefixValidator implements Validator {

    /**
     * The pattern for OAI-PMH metadata prefixes, adapted from <a
     * href="http://www.openarchives.org/OAI/openarchivesprotocol.html#OAIPMHschema">the
     * relevant XML schema</a>
     */
    private final static Pattern PATTERN = 
            Pattern.compile("[-A-Za-z0-9_.!~*'()]+");

    /**
     * In order to be valid, the object must be a {@code String} and match
     * the validating pattern. 
     *
     * @param  obj the object to validate
     * 
     * @return {@code true} if the object is a valid setSpec string,
     *     {@code false} if it isn't
     *     
     * @throws NullPointerException if the argument is {@code null}
     */
    public boolean isValid(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return ((obj instanceof String)
                && PATTERN.matcher((String)obj).matches());
    }
}
