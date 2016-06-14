/*
 * Reciprocal Net project
 * 
 * LabDirNameValidator.java
 * 
 * 14-Dec-2004: midurbin wrote first draft
 * 12-Jan-2005: ekoperda clarified JavaDoc on isValid()
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.validation;

/**
 * <p>
 * Extends {@code FilenameValidator} to validate potential lab directory names.
 * Besides the standard filename validation provided by the superclass, this
 * validator explicitly forbids values equal to "cvs" and "temp" because they
 * are reserved directory names.
 * </p><p>
 * This validator is guaranteed to be at least as strict as
 * {@code ContainerStringValidator} because the superclass is guaranteed to be,
 * and may be used in place of it when a lab directory name container object
 * string must be validated.
 * </p>
 */
public class LabDirNameValidator extends FilenameValidator {

    /**
     * Overrides {@code FilenameValidator}; the current implementation
     * delegates back to the superclass and then validates the object against
     * reserved values.
     * 
     * @param obj A {@code String} that is to be validated as a possible lab
     *        directory name
     * @return true if the object is a valid lab directory name, false if it
     *         contains illegal characters or is equal to a reserved filename.
     * @throws NullPointerException if obj is null
     */
    @Override
    public boolean isValid(Object obj) {
        if (super.isValid(obj)) {
            return (!obj.equals("cvs") && !obj.equals("temp"));
        } else {
            return false;
        }
    }
}
