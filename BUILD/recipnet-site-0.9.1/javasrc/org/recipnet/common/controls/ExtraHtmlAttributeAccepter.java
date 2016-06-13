/*
 * Reciprocal Net project
 * 
 * ExtraHtmlAttributeAccepter.java
 * 
 * 16-Nov-2004: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * <p>
 * Implementors of {@code ExtraHtmlAttributeAccepter} will accept any number of
 * name/value pairs which it will use as attributes to the main underlying HTML
 * code.
 * </p><p>
 * For example, a custom tag that was meant to represent a textbox and
 * implemented {@code ExtraHtmlAttributeAccepter} would probably add the
 * attributes to the HTML textbox input unless documentation for the class
 * indicated otherwise.
 * </p>
 */
public interface ExtraHtmlAttributeAccepter {

    /**
     * Adds an attribute with the given name and value to the HTML element
     * represented by the implementing class. Typically only one attribute with
     * a given name may be added and subsequent calls with the same name will
     * overrule previous calls, or result in a runtime exception, but this
     * behavior is implementation specific.
     * 
     * @param name the name of the attribute to be added
     * @param value the value of the attribute to be added
     */
    public void addExtraHtmlAttribute(String name, String value);
}
