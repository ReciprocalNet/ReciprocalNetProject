/*
 * Reciprocal Net project
 * 
 * ErrorSupplier.java
 * 
 * 27-Feb-2004: midurbin wrote first draft
 * 25-Jun-2004: cwestnea added NO_ERROR_REPORTED constant
 * 23-Aug-2004: midurbin added jgetHighestErrorCode() and setErrorFlag() and
 *              extended the class level javadocs
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * <p>
 * An interface defining behaviors for accepting and exposing error flags. An
 * error code is the logical OR of various error bit flags, typically defined on
 * the implementing class. Up to 32 distinct error flags are supported, and they
 * do not need to correspond to other classes' so long as zero (the absence of
 * any error flags) indicates that no error has occurred.
 * </p><p>
 * Convention for implementing this interface holds that
 * <ol>
 * <li>any internal storage for error flags should be private, accessed only
 * via the methods defined by this interface</li>
 * <li>error flags should be assigned as powers of two starting at 1
 * (2<sup>0</sup>), and each should be double the previous one.  Note that zero
 * is not a power of two.</li>
 * <li>implementation classes should each provide a static method
 * {@code getHighestErrorFlag()} to support subclasses in adding new error flags
 * without introducing conflicts.</li>
 * <li>where a tag 'owns' an {@code HtmlPageElement} that implements
 * {@code ErrorSupplier}, it should redefine those error flags that it wishes
 * to propagate from its owned element and translate them. It should not merely
 * assert that the error codes on the 'owned' element are the same as the error
 * codes it defines, and other classes should not rely on any correspondence
 * between the two classes' error codes.</li>
 * </ol>
 * </p>
 */
public interface ErrorSupplier {
    /**
     * This is a possible error code as reported by the {@code ErrorSupplier}
     * object to indicate that no error occured and that the body of this object
     * need not be evaluated.
     */
    public static final int NO_ERROR_REPORTED = 0;

    /**
     * Returns an integer representing the logical OR of all error flags that
     * have occurred. Zero is reserved to mean that no error occurred.
     * 
     * @return an integer that can be interprited as an array of bits that can
     *         be compared with error codes defined on the implementing class.
     */
    public int getErrorCode();

    /**
     * Updates the existing error code by setting it to the logical OR of the
     * existing error code and the provided error flag.
     * 
     * @param errorFlag the error flag(s) that should be set. Valid error flags
     *        are defined on implementing classes.
     */
    public void setErrorFlag(int errorFlag);
}
