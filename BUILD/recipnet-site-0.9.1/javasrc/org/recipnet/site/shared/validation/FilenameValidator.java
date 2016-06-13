/*
 * Reciprocal Net project
 * 
 * FilenameValidator.java
 * 
 * 07-May-2004: cwestnea wrote first draft
 * 14-Dec-2004: midurbin added guarantee to be at least as strict as
 *              ContainerStringValidator
 * 11-Jan-2005: jobollin moved Validator to org.recipnet.common
 * 12-Jan-2005: ekoperda clarified JavaDoc on isValid()
 * 10-Mar-2006: jobollin made many previously-accepted characters invalid to
 *              avoid problems in external scripts; reformatted the source
 */

package org.recipnet.site.shared.validation;

import java.util.regex.Pattern;
import org.recipnet.common.Validator;

/**
 * <p>
 * Implements {@code Validator}; validates a {@code String} that will be used
 * in a file or directory name. Current implementation supports both Linux and
 * Win32 filesystems.
 * </p><p>
 * This validator is guaranteed to be at least as strict as
 * {@link ContainerStringValidator} and may be used in place of it to validate a
 * container string that is also a filename.
 * </p>
 */
public class FilenameValidator implements Validator {

    /**
     * References a compiled pattern that represents the regular expression
     * matching valid filesystem names on both Linux and Win32 platforms,
     * excluding names containing shell metacharacters on either system. This
     * pattern is good for both regular files and directories. Empty string are
     * not valid, nor are the special directory names "." and "..".
     */
    private static final Pattern PATTERN
            = Pattern.compile("(?!\\.\\.?$)[-A-Za-z0-9._]+");

    /**
     * In order to be valid, the object must be a {@code String} and match the
     * validating pattern.
     * 
     * @param  obj the {@code Object} to validate
     * 
     * @return true if the object is a valid filename string, false if it
     *         contains characters not specified in the pattern.
     *         
     * @throws NullPointerException if obj is null
     */
    public boolean isValid(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return ((obj instanceof String)
                && PATTERN.matcher((String) obj).matches());
    }
}
