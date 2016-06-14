/*
 * Reciprocal Net Project
 *
 * ScanState.java
 *
 * 26-Jan-2005: jobollin wrote first draft
 */

package org.recipnet.common.files.cif;

/**
 * An interface representing the current state of a lexical scanner: its
 * position in the input, the current and previous characters considered,
 * and the token currently being constructed
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public interface ScanState {

    /**
     * Returns the current character being considered by the scanner
     *
     * @return the current character, as an {@code int}; -1 for end of
     *         file, or otherwise within the integer range of the
     *         {@code char} type
     */
    int getCurrentChar();

    /**
     * Sets the scanner's character to the specified character.  The effect
     * of this operation will depend on the scanner and on (unexposed)
     * details of its state; consult the scanner's documentation regarding
     * how and when it is appropriate to use this method
     *
     * @param  newChar the character to set as the new current character
     *
     * @throws IllegalArgumentException if {@code newChar} is not
     *         permitted by the scanner at this point in the scan
     * @throws IllegalStateException if the current character is not
     *         permitted to be modified at this point in the scan, including
     *         if this ScanState never permits it
     */
    void setCurrentChar(int newChar);

    /**
     * Returns the character considered by the scanner most recently before
     * the current one
     *
     * @return the previous character, as an {@code int}; -1 for end of
     *         file (if the scanner attempted to read past EOF), or
     *         otherwise within the integer range of the {@code char}
     *         type.  Value is undefined if the scanner is currently on
     *         the first character of its input
     */
    int getLastChar();

    /**
     * Returns the current line of its input on which the scanner is
     * reading, relative to the line termination semantics specific to the
     * scanner.  Line terminators appear on the line they terminate for the
     * purposes of this method.
     *
     * @return the 1-based line number of the input on which the current
     *         character appears
     */
    int getLineNumber();

    /**
     * Returns the current position on the current input line at which the
     * scanner is reading, relative to the line termination semantics
     * specific to the scanner.
     *
     * @return the 1-based index of the current charecter on the current
     *         line
     */
    int getCharacterNumber();

    /**
     * Returns a string containing the characters so far accepted for the
     * token currently being scanned.  This does <em>not</em> include the
     * scanner's current character (which is under consideration)
     *
     * @return a {@code String} containing the current token being
     *         scanned, up to this point in the scan
     */
    String getCurrentToken();
}

