/*
 * Reciprocal Net Project
 *
 * StringValueChecker.java
 *
 * 09-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.common.controls;

import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;

/**
 * A Checker tag whose inclusion condition is based on the {@code String} value
 * of some specified {@code HtmlControl} during the {@code FETCHING_PHASE}
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class StringValueChecker extends AbstractChecker {
    
    /**
     * A required, non-transient tag attribute that establishes the
     * {@code HtmlControl} whose value is to be used during the
     * {@code FETCHING_PHASE} to determine whether this checker's inclusion
     * condition has been met. 
     */
    private HtmlControl control;
    
    /**
     * An optional, non-transient tag attribute that specifies a regular
     * expression in Java regex format which the specified control's string
     * value must match -- in its entirety -- in order for this checker's
     * inclusion condition to be met.  Defaults to a pattern that matches any
     * string containing at least one non-whitespace character.
     */
    private String pattern;
    
    /**
     * A regex pattern compiled from the {@code pattern} string
     */
    private Pattern compiledPattern;

    /**
     * {@inheritDoc}
     * 
     * @see AbstractChecker#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        
        control = null;
        pattern = ".*\\S.*";
        compiledPattern = Pattern.compile(pattern);
    }

    /**
     * Returns the value of this tag's {@code control} property
     * 
     * @return the {@code HtmlControl} whose string value will be used to
     *         evaluate this checker's inclusion condition
     */
    public HtmlControl getControl() {
        return control;
    }

    /**
     * Sets the value of this tag's {@code control} property
     * 
     * @param  control the {@code HtmlControl} whose string value should be used
     *         to evaluate this checker's inclusion condition
     */
    public void setControl(HtmlControl control) {
        if (control == null) {
            throw new NullPointerException("Null control");
        }
        this.control = control;
    }

    /**
     * Gets the string representation of the regular expression against which
     * the configured control's value is to be tested
     * 
     * @return the pattern {@code String}
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * Sets the string representation of the regular expression against which
     * the configured control's value is to be tested
     * 
     * @param  pattern the pattern {@code String}; should not be {@code null},
     *         and should comply with Java regular expression syntax
     */
    public void setPattern(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Null pattern");
        }
        this.pattern = pattern;
        this.compiledPattern = Pattern.compile(pattern);
    }

    /**
     * {@inheritDoc}.  This version tests whether this checker's inclusion
     * condition has been satisfied
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        super.inclusionConditionMet
                = compiledPattern.matcher(control.getValueAsString()).matches();
        
        return rc;
    }

    /**
     * {@inheritDoc}.
     * 
     * @see AbstractChecker#generateCopy(String, Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        StringValueChecker copy
                = (StringValueChecker)  super.generateCopy(newId, map);
        
        /*
         * The map might not contain the referenced control as a key if it is
         * not an ancestor of this tag and it is not contained in a common
         * iterator with this tag.  In those cases, however, the reference copy
         * performed by HtmlPageElement results in the correct value for the
         * copy, and nothing need be done.
         */
        if (map.containsKey(this.control)) {
            copy.setControl((HtmlControl) map.get(this.control));
        }
        
        return copy;
    }
}
