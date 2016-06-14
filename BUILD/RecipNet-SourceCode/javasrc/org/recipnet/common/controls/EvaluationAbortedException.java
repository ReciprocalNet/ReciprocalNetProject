/*
 * Reciprocal Net project
 * 
 * EvaluationAbortedException.java
 *
 * 27-Jul-2005: midurbin wrote first draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * An exception for use by {@code HtmlPage} that is thrown to halt subclasses
 * evaluation of a paricular method and to end evaluation for the tag and page.
 * This class differs only from {@code Exception} in name.
 */
public class EvaluationAbortedException extends Exception {
    // nothing to see here; move along
}
