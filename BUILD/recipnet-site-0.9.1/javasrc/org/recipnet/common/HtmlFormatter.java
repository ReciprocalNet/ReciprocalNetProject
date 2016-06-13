/*
 * Reciprocal Net project
 * 
 * HtmlFormatter.java
 * 
 * 08-Mar-2005: midurbin wrote first draft
 * 28-Feb-2006: jobollin updated docs
 */

package org.recipnet.common;

/**
 * An interface defining the behavior of objects that can generate
 * HTML-formatted {@code String} representations of other objects.
 * Implementations will generally be capable of formatting objects of one or
 * more specific types; they generally will not be applicable to all possible
 * types.  Implementations are fully responsible for the correctness of the
 * HTML they produce; in particular, they are responsible for properly escaping
 * output text so that only those portions that are intended to be interpreted
 * as markup will in fact be so interpreted.
 */
public interface HtmlFormatter {

    /**
     * Produces an HTML formatted representation of the specified object.
     * 
     * @param obj an object to be formatted
     * @return an HTML string representing the formatted object; possibly the
     *         empty string but never null
     * @throws IllegalArgumentException if the 'obj' is not of a type that the
     *         {@code Formatter} implementation can format
     */
    public String formatObject(Object obj);
}
