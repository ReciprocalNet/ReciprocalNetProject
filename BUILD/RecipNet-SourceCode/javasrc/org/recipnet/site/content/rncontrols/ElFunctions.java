/*
 * Reciprocal Net Project
 *
 * ElFunctions.java
 *
 * 12-Jan-2006: jobollin wrote first draft
 * 13-Jun-2006: jobollin removed an unused import
 */

package org.recipnet.site.content.rncontrols;

/**
 * A class containing static methods intended for exposure as EL functions in
 * the rncontrols tag library
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ElFunctions {
    
    /**
     * Replaces the tail of a specified input string with the specified
     * replacement string
     *  
     * @param  input the {@code String} in which the replacement is to be made;
     *         should not be {@code null}
     * @param  toReplace the {@code String} to replace; should not be
     *         {@code null}
     * @param  replacement the replacement {@code String}; should not be
     *         {@code null}
     * 
     * @return if {@code input} ends with the {@code toReplace} string then
     *         new {@code String} similar to {@code input} but in which the
     *         trailing substring equal to {@code toReplace} has been replaced
     *         by {@code replacement}; otherwise {@code input} 
     */
    public static String replaceTail(String input, String toReplace,
            String replacement) {
        if (input.endsWith(toReplace)) {
            return input.substring(
                    0, input.length() - toReplace.length()).concat(replacement);
        } else {
            return input;
        }
    }
    
    /**
     * Tests the parity of the specified {@code int} value, returning one of
     * two user-specified possible values depending on the result 
     * 
     * @param  val the value whose parity is to be evaluated
     * @param  ifEven the {@code String} to return if {@code val} is even
     * @param  ifOdd the {@code String} to return if {@code val} is odd
     * 
     * @return either {@code ifEven} or {@code ifOdd}, depending on the parity
     *         of {@code val}
     */
    public static String testParity(int val, String ifEven, String ifOdd) {
        return ((val & 0x1) == 0) ? ifEven : ifOdd;
    }
}
