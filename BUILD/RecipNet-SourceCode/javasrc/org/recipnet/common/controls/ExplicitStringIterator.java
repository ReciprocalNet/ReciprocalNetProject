/*
 * Reciprocal Net Project
 *
 * ExplicitStringIterator.java
 *
 * 30-Mar-2006: jobollin wrote first draft
 * 29-Jun-2006: jobollin removed an unused import
 */

package org.recipnet.common.controls;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * An HtmlPageIterator that iterates over a specified (possibly empty) list of
 * Strings
 * 
 * @author jobollin
 * @version 1.0
 */
public class ExplicitStringIterator extends HtmlPageIterator {
    
    private final static Pattern DEFAULT_DELIM_PATTERN
            = Pattern.compile("[,;]");
    
    private String strings;
    private String delimiter;
    private List<String> stringList;
    private Iterator<String> iterator;
    private String currentString;
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        stringList = null;
        strings = null;
        delimiter = null;
        currentString = null;
    }
    
    /**
     * Returns the strings over which this iterator will iterate, in the
     * delimited form in which they were specified to this iterator
     * 
     * @return the {@code strings} {@code String}
     */
    public String getStrings() {
        return strings;
    }

    /**
     * Sets the strings over which this iterator will iterate, in the form
     * of a delimited string; the delimiter may be explicitly set via the
     * 'delimiter' property, but otherwise it is single commas or semicolons
     * (or a mixture of the two)
     * 
     * @param  strings the {@code String} to set as the {@code strings}
     */
    public void setStrings(String strings) {
        this.strings = strings;
    }
    
    /**
     * Gets the explicitly-set delimiter pattern string configured on this tag
     * handler
     * 
     * @return the delimiter pattern string
     */
    public String getDelimiter() {
        return delimiter;
    }
    
    /**
     * Sets an explicit delimiter pattern with which the string list will be
     * parsed into individual strings
     * 
     * @param  delimiter the {@code Pattern} to set as the {@code delimiter}
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    /**
     * Returns an unmodifiable {@code List} of those {@code String}s over which
     * this iterator will iterate
     *  
     * @return a {@code List<String>} of the Strings over which this iterator
     *         will iterate
     */
    protected List<String> getStringList() {
        return stringList;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if either the 'strings' property or the
     *         'delimiter' property is {@code null} 
     *         
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        String delim = getDelimiter();
        Pattern pat = ((delim == null) ? DEFAULT_DELIM_PATTERN
                                       : Pattern.compile(delim));
        
        stringList = Collections.unmodifiableList(
                Arrays.asList(pat.split(getStrings(), -1)));
        
        return rc;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#beforeIteration()
     */
    @Override
    protected void beforeIteration() throws JspException {
        super.beforeIteration();
        iterator = stringList.iterator();
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#onIterationBeforeBody()
     */
    @Override
    protected boolean onIterationBeforeBody() {
        boolean shouldIterate
                = (super.onIterationBeforeBody() && iterator.hasNext());
        
        if (shouldIterate) {
            currentString = iterator.next();
        }
        
        return shouldIterate;
    }
    
    /**
     * {@inheritDoc}.
     * 
     * @see HtmlPageIterator#isCurrentIterationLast()
     */
    @Override
    public boolean isCurrentIterationLast() {
        return !iterator.hasNext();
    }

    /**
     * Returns the string that is the subject of the current iteration
     * 
     * @return the {@code String} that is the subject of the current iteration
     */
    public String getCurrentString() {
        return currentString;
    }
}
