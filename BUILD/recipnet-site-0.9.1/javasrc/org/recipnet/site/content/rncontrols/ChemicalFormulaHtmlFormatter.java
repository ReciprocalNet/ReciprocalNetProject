/*
 * Reciprocal Net project
 * 
 * ChemicalFormulaHtmlFormatter.java
 * 
 * 08-Apr-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added basic support for FieldMatchInfo-specific
 *              highlighting
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.recipnet.common.HtmlFormatter;
import org.recipnet.site.shared.search.FieldMatchInfo;

/**
 * A {@code Formatter} class that is meant to mark up chemical formulae using
 * HTML, including making a best effort with invalid formulae.
 */
public class ChemicalFormulaHtmlFormatter implements HtmlFormatter {

    private static final Pattern nonnumericPattern
            = Pattern.compile("\\A[^-+0-9.]");

    private static final Pattern chargePattern
            = Pattern.compile("\\A[0-9.]+[-+]");

    /**
     * A collection of relevent {@code FieldMatchInfo} object, that when set
     * cause the formatted HTML to highlight the value or portion of the value
     * that matched the {@code SearchParams} that was used to generate the
     * provided {@code FieldMatchInfo} objects.
     */
    private Collection<? extends FieldMatchInfo> fieldMatchInfos;

    /**
     * The CSS class for the &lt;span&gt; tag(s) that will surround the matching
     * part of the formatted formula if {@code fileMatchInfos} are provided.
     */
    private String styleClass;

    /** The default constructor. */
    public ChemicalFormulaHtmlFormatter() {
        this.fieldMatchInfos = null;
        this.styleClass = null;
    }

    /**
     * A constructor that allows {@code FieldMatchInfo} objects to be provided
     * to indicate whether/how the formatted field should be highlighted.
     */
    public ChemicalFormulaHtmlFormatter(
            Collection<? extends FieldMatchInfo> fieldMatchInfos,
            String styleClass) {
        this.fieldMatchInfos = fieldMatchInfos;
        this.styleClass = styleClass;
    }

    /**
     * Marks up the supplied chemical formula in HTML.
     * 
     * @param obj a {@code String} representing a plain-text chemical formula
     *        (not already marked up)
     * @throws IllegalArgumentException if the 'obj' parameter is not a
     *         {@code String}
     */
    public String formatObject(Object obj) {
        if (obj == null) {
            return "";
        } else  if (!(obj instanceof String)) {
            throw new IllegalArgumentException();
        }
        
        String rawFormula = (String) obj;
        if (rawFormula.trim().equals("")) {
            return "";
        }
    
        StringBuilder formattedFormula = new StringBuilder();
        boolean isFirstTerm = true;
    
        for (StringTokenizer st = new StringTokenizer(rawFormula, ";,");
                st.hasMoreTokens(); ) {
            String currentTerm = st.nextToken();
            boolean isNewTerm = true;
            boolean isInCoefficientZone = true;
            boolean isBlank = false;
            FormatMode currentMode = FormatMode.NORMAL;
    
            for (int i = 0; i < currentTerm.length(); i++) {
                FormatMode previousMode = currentMode;
                boolean wasBlank = isBlank;
                char currentChar = currentTerm.charAt(i);
                String tail;
    
                isBlank = ((currentChar == ' ') || (currentChar == '\t'));
                if (isBlank) {
                    continue;
                }
    
                /*
                 * look ahead for a charge symbol or a letter; if charge symbol
                 * is found first then we know the current character belongs in
                 * a superscript, if the first character is a letter then the
                 * current character is part of an element symbol or is
                 * punctuation, and otherwise the current character is either a
                 * term multiplier or a subscript.
                 */
                tail = currentTerm.substring(i);
                if ((currentChar == '+')
                        || (currentChar == '-')
                        || (wasBlank && chargePattern.matcher(tail).lookingAt())) {
                    currentMode = FormatMode.SUPER;
                } else if (nonnumericPattern.matcher(tail).lookingAt()) {
                    currentMode = FormatMode.NORMAL;
                    isInCoefficientZone = false;
                } else if ((currentMode == FormatMode.NORMAL)
                        && !isInCoefficientZone) {
                    currentMode = FormatMode.SUB;
                }
    
                // output the term delimiter string ahead of a new term
                if (isNewTerm) {
                    if (!isFirstTerm) {
                        formattedFormula.append(", ");
                    }
                    isNewTerm = false;
                }
                
                /*
                 * create appropriate markup when our internal state changes
                 * (e.g. when switching from normal text to subscripted or
                 * superscripted text)
                 */
                if (currentMode != previousMode) {
                    formattedFormula.append(previousMode.getEndText());
                    formattedFormula.append(currentMode.getStartText());
                }
    
                // add the current character
                formattedFormula.append(currentChar);
            }
            // end of term cleanup
            formattedFormula.append(currentMode.getEndText());
            isFirstTerm = false;
        }
        if ((this.fieldMatchInfos != null) && !this.fieldMatchInfos.isEmpty()) {
            return "<span class=\"" + this.styleClass + "\">"
                    + formattedFormula.toString() + "</span>";
        } else {
            return (formattedFormula.toString());
        }
    }

    private enum FormatMode {
        NORMAL("", ""), SUPER("<sup>", "</sup>"), SUB("<sub>", "</sub>");
        private final String startText;
        private final String endText;
        
        FormatMode(String start, String end) {
            startText = start;
            endText = end;
        }
        
        public String getStartText() {
            return startText;
        }
        
        public String getEndText() {
            return endText;
        }
    }
}
