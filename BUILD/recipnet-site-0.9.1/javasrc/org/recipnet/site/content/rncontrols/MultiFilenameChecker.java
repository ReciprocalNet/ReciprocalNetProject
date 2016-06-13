/*
 * Reciprocal Net Project
 *
 * MultiFilenameChecker.java
 *
 * 12-Jan-2006: jobollin wrote first draft
 * 09-Mar-2006: jobollin made the tag attributes transient, corrected a bug
 *              in regex metacharacter quoting in createPattern(), and changed
 *              the NOTHING_PATTERN to something less likely to trip up a
 *              regex engine
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.AbstractChecker;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * This class is a custom tag handler for a checker tag that evaluates whether
 * the surrounding {@code MultiFilenameContext} contains files matching required
 * and / or forbidden glob patterns.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class MultiFilenameChecker extends AbstractChecker {

    /**
     * A reference to the innermost {@code MultiFilenameContext} containing
     * this tag.
     */
    private MultiFilenameContext filenameContext;
    
    /**
     * An optional, transient tag attribute whose value is interpreted as a
     * shell-style glob specifying the filenames of which one must be present
     * in the containing context for this checker's condition to be satisfied.
     * Only the asterisk (*) and question mark (?) are recognized as glob
     * metacharacters.  Users should keep in mind that the forbidden filename
     * glob (if specified) is also taken into account in determining whether
     * this checker's condition is satisfied.
     */
    private String requiredFilenameGlob;
    
    /**
     * A regex pattern that matches the filenames represented by the
     * {@code requiredFilenameGlob}; it is derived from the glob, and is the
     * direct mechanism by which filenames are checked for satisfying the
     * required file criterion
     */
    private Pattern requiredFilenamePattern;
    
    /**
     * An optional, transient tag attribute whose value is interpreted as a
     * shell-style glob specifying the filenames of which none may be present
     * in the containing context for this checker's condition to be satisfied.
     * Only the asterisk (*) and question mark (?) are recognized as glob
     * metacharacters.  Users should keep in mind that the required filename
     * glob (if specified) is also taken into account in determining whether
     * this checker's condition is satisfied.
     */
    private String forbiddenFilenameGlob;
    
    /**
     * A regex pattern that matches the filenames represented by the
     * {@code forbiddenFilenameGlob}; it is derived from the glob, and is the
     * direct mechanism by which filenames are checked for satisfying the
     * forbidden file criterion
     */
    private Pattern forbiddenFilenamePattern;
    
    /**
     * A regex {@code Pattern} that matches individual characters that function
     * as metacharacters in a general regex context (i.e. excluding characters
     * that are metacharacters only in restricted contexts, such as character
     * classes or groups 
     */
    private final static Pattern PATTERN_METACHARACTER_PATTERN
            = Pattern.compile("[\\\\.\\[\\]{}()|^$+*?]");
    
    /**
     * A regex {@code Pattern} that matches every possible input string
     */
    private final static Pattern ANYTHING_PATTERN = Pattern.compile(".*");
    
    /**
     * A regex {@code Pattern} that doesn't match any input string
     */
    private final static Pattern NOTHING_PATTERN = Pattern.compile("0(?<!0)");

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        filenameContext = null;
        requiredFilenameGlob = null;
        requiredFilenamePattern = ANYTHING_PATTERN;
        forbiddenFilenameGlob = null;
        forbiddenFilenamePattern = NOTHING_PATTERN;
    }

    /**
     * @return the {@code forbiddenFilenameGlob} {@code String}
     */
    public String getForbiddenFilenameGlob() {
        return forbiddenFilenameGlob;
    }

    /**
     * @param  forbiddenFilenameGlob the {@code String} to set as the
     *  {@code forbiddenFilenameGlob}
     */
    public void setForbiddenFilenameGlob(String forbiddenFilenameGlob) {
//        if (((this.forbiddenFilenameGlob == null)
//                    && (forbiddenFilenameGlob != null))
//                || ((this.forbiddenFilenameGlob != null)
//                        && !this.forbiddenFilenameGlob.equals(
//                                forbiddenFilenameGlob))) {
            this.forbiddenFilenameGlob = forbiddenFilenameGlob;
            forbiddenFilenamePattern = (forbiddenFilenameGlob == null)
                    ? NOTHING_PATTERN : createPattern(forbiddenFilenameGlob);
//        }
    }

    /**
     * @return the {@code requiredFilenameGlob} {@code String}
     */
    public String getRequiredFilenameGlob() {
        return requiredFilenameGlob;
    }

    /**
     * @param  requiredFilenameGlob the {@code String} to set as the
     *  {@code requiredFilenameGlob}
     */
    public void setRequiredFilenameGlob(String requiredFilenameGlob) {
//        if (((this.requiredFilenameGlob == null)
//                && (requiredFilenameGlob != null))
//            || ((this.requiredFilenameGlob != null)
//                    && !this.requiredFilenameGlob.equals(
//                            requiredFilenameGlob))) {
            this.requiredFilenameGlob = requiredFilenameGlob;
            requiredFilenamePattern = (requiredFilenameGlob == null)
                    ? ANYTHING_PATTERN : createPattern(requiredFilenameGlob);
//        }
    }

    /**
     * {@inheritDoc}.  This implementation finds a reference to the innermost
     * surrounding {@code MultiFilenameContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *     {@code MultiFilenameContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        filenameContext
                = findRealAncestorWithClass(this, MultiFilenameContext.class);
        if (filenameContext == null) {
            throw new IllegalStateException("No MultiFilenameContext");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version gets the file names from the surrounding
     * {@code MultiFilenameContext} and uses them to determine whether the body
     * of this tag should be suppressed.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        boolean requiredFilePredicate = false;
        boolean forbiddenFilePredicate = false;
        
        for (String filename : filenameContext.getFilenames()) {
            requiredFilePredicate = (requiredFilePredicate
                    || requiredFilenamePattern.matcher(filename).matches());
            forbiddenFilePredicate = (forbiddenFilePredicate
                    || forbiddenFilenamePattern.matcher(filename).matches());
        }
        
        super.inclusionConditionMet
                = (requiredFilePredicate && !forbiddenFilePredicate);
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This implementation delegates to the superclass then
     * updates references to ancestor tags using the 'map' parameter that was
     * populated by the superclass' implementation as well as the caller.
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        MultiFilenameChecker mfc
                = (MultiFilenameChecker) super.generateCopy(newId, map);
        
        mfc.filenameContext
                = (MultiFilenameContext) map.get(this.filenameContext);
        
        return mfc;
    }
    
    /**
     * @see org.recipnet.common.controls.HtmlPageElement#copyTransientPropertiesFrom(org.recipnet.common.controls.HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        MultiFilenameChecker checker = (MultiFilenameChecker) source;
        
        super.copyTransientPropertiesFrom(source);
        this.setForbiddenFilenameGlob(checker.getForbiddenFilenameGlob());
        this.setRequiredFilenameGlob(checker.getRequiredFilenameGlob());
    }

    /**
     * Creates a regex pattern that matches exactly the same strings matched by
     * the specified shell-style glob.  Only the asterisk (*) and question mark
     * (?) are recognized as metacharacters in the glob syntax supported by this
     * method. 
     *
     * @param  glob a {@code String} containing the shell-style glob for which
     *         a corresponding regex pattern is requested; may be empty, but
     *         should not be {@code null}
     * 
     * @return a regex {@code Pattern} that matches exactly the same set of
     *         strings that is matched by the specified glob
     */
    private Pattern createPattern(String glob) {
        
        /*
         * Convert glob syntax to regex syntax
         */
        Matcher matcher = PATTERN_METACHARACTER_PATTERN.matcher(glob);
        StringBuffer patternBuffer = new StringBuffer();
        
        while (matcher.find()) {
            switch (matcher.group().charAt(0)) {
                case '?':
                    matcher.appendReplacement(patternBuffer, ".");
                    break;
                case '*':
                    matcher.appendReplacement(patternBuffer, ".*");
                    break;
                default:
                    matcher.appendReplacement(patternBuffer, "\\\\$0");
                    break;
            }
        }
        matcher.appendTail(patternBuffer);
        
        // Create and return the Pattern
        return Pattern.compile(patternBuffer.toString());
    }
}
