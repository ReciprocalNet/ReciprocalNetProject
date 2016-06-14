/*
 * Reciprocal Net project
 * 
 * ComplexFilenameValidator.java
 * 
 * 10-May-2004: cwestnea wrote first draft
 * 12-Jan-2005: ekoperda clarified JavaDoc on isValid()
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.validation;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Extends FilenameValidator; ensures that the given string is a legal filename,
 * and allows subdirectories to be used.
 */
public class ComplexFilenameValidator extends FilenameValidator {
    
    /**
     * Overrides {@code FilenameValidator}; breaks up the string into chunks,
     * using the {@code File.separator} as the delimiter, and then uses
     * {@code FilenameValidator} to do the actual validation.
     * 
     * @param obj the {@code Object} to test for validity
     * @return true if the object is a valid complex filename, false if the
     *         object is not a string, or if one of the substrings is not valid
     *         as defined by {@code FilenameValidator}.
     * @throws NullPointerException if obj is null
     */
    @Override
    public boolean isValid(Object obj) {
        if (obj.equals("") || !(obj instanceof String)) {
            return false;
        }
        StringTokenizer subDirs = new StringTokenizer((String) obj,
                File.separator);
        
        while (subDirs.hasMoreTokens()) {
            if (!super.isValid(subDirs.nextToken())) {
                return false;
            }
        }
        
        return true;
    }
}
