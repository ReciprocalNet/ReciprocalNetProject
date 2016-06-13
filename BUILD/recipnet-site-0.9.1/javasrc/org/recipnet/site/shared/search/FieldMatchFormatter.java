/*
 * Reciprocal Net project
 * 
 * FieldMatchFormatter.java
 * 
 * 15-Aug-2005: midurbin wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import java.util.BitSet;
import java.util.Collection;
import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.controls.HtmlControl;

/**
 * <p>
 * An {@code HtmlFormatter} class that is meant to add HTML to highlight
 * the given {@code String} representation if, according to the supplied
 * {@code FieldMatchInfo} objects, it matches.
 * </p><p>
 * This initial implementation does not highlight a portion of the string but
 * but instead hightlights the entire string even if only a portion of it
 * matches.
 * </p>
 */
public class FieldMatchFormatter implements HtmlFormatter {

    /** A collection of matches pertaining to the field being formatted. */
    private Collection<FieldMatchInfo> fieldMatchInfos;

    /**
     * The CSS class for the &lt;span&gt; tag(s) that will surround the
     * matching part of the formatted formula if {@code fileMatchInfos}
     * are provided.
     */
    private String styleClass;

    /**
     * Initializes a {@code FieldMatchFormatter} with the specified parameters
     * 
     * @param  fieldMatchInfos a {@code Collection} of {@code FieldMatchInfo}
     *         from which to determine which regions of the string to be
     *         formatted should be highlighted
     * @param  styleClass the name of the CSS class to assign to highlighted
     *         regions
     */
    public FieldMatchFormatter(
            Collection<FieldMatchInfo> fieldMatchInfos, String styleClass) {
        this.fieldMatchInfos = fieldMatchInfos;
        this.styleClass = styleClass;
    }

    /**
     * Implements {@code HtmlFormatter} to return HTML that highlights the
     * String representation of the object being formatted in accordance with
     * the 'fieldMatchInfos'.
     * 
     * @param  obj the {@code Object} to format
     * 
     * @return a marked-up {@code String} representation of the specified object
     */
    public String formatObject(Object obj) {
        if (obj == null) {
            return "";
        }

        // Obtain the string representation
        String stringRepresentation = String.valueOf(obj);
        
        // Determine which regions of the string should be highlighted
        
        BitSet highlightedChars = new BitSet(stringRepresentation.length());
        
        for (FieldMatchInfo currentMatch : this.fieldMatchInfos) {
            FieldMatchInfo.MatchingPart match = currentMatch.getMatchingPart();
            
            if (match.matchesWhole()) {
                highlightedChars.set(0, highlightedChars.size());
                break;
            } else {
                highlightedChars.set(match.getBeginIndex(),
                        match.getBeginIndex() + match.getMatchLength());
            }
        }
        
        /* 
         * construct the HTML fragment reflecting the highlighting and HTML
         * character escapes
         */
        
        StringBuilder result = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        boolean isHighlighting = false;
        
        for (int i = 0; i < stringRepresentation.length(); i++) {
            if (highlightedChars.get(i) != isHighlighting) {
                appendResultFragment(sb, isHighlighting, result);
                sb.setLength(0);
                isHighlighting = !isHighlighting;
            }
            sb.append(stringRepresentation.charAt(i));
        }
        appendResultFragment(sb, isHighlighting, result);

        // done
        return result.toString();
    }
    
    /**
     * Appends the characters from the specified sequence to the specified
     * {@code StrignBuilder}, performing HTML escaping on them as necessary and
     * optionally wrapping them in a &lt;span&gt; element appropriate for
     * conveying that the characters are highlighted.
     * 
     * @param  chars a {@code CharSequence} containing the characters to
     *         transfer to {@code result}
     * @param  highlight {@code true} if the specified characters should
     *         constitute a highlighted region
     * @param  result the {@code StringBuilder} to which the characters should
     *         be appended
     */
    private void appendResultFragment(CharSequence chars, boolean highlight,
            StringBuilder result) {
        String escaped = HtmlControl.escapeNestedValue(chars.toString());
        
        if (highlight) {
            result.append("<span class=\"" + this.styleClass + "\">");
        }
        result.append(escaped);
        if (highlight) {
            result.append("</span>");
        }
    }
}
