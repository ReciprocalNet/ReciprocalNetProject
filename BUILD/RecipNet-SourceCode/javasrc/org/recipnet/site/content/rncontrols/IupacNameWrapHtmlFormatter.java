/*
 * Reciprocal Net project
 * 
 * IupacNameWrapHtmlFormatter.java
 * 
 * 08-Apr-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added basic support for FieldMatchInfo-specific
 *              highlighting
 * 28-Feb-2006: jobollin made this class perform HTML-escaping on the value to
 *              be formatted; fixed mismatched HTML tags in formatObject()
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.site.shared.search.FieldMatchInfo;

/**
 * An {@code HtmlFormatter} class that is meant to add HTML to suggest line
 * breaks to browsers for long IUPAC names. This algorithm and implementation
 * was originally written for {@code HtmlHelper.addLogicalBreaksToIUPACName()}.
 */
public class IupacNameWrapHtmlFormatter implements HtmlFormatter {

    /**
     * A collection of relevent {@code FieldMatchInfo} object, that when set
     * cause the formatted HTML to highlight the value or portion of the value
     * that matched the {@code SearchParams} that was used to generate the
     * provided {@code FieldMatchInfo} objects.
     */
    private Collection<FieldMatchInfo> fieldMatchInfos;

    /**
     * The CSS class for the &lt;span&gt; tag(s) that will surround the matching
     * part of the formatted formula if {@code fileMatchInfos} are provided.
     */
    private String styleClass;

    /** The default constructor. */
    public IupacNameWrapHtmlFormatter() {
        this.fieldMatchInfos = null;
    }

    /**
     * A constructor that allows {@code FieldMatchInfo} objects to be provided
     * to indicate whether/how the formatted field should be highlighted.
     */
    public IupacNameWrapHtmlFormatter(
            Collection<FieldMatchInfo> fieldMatchInfos, String styleClass) {
        this.fieldMatchInfos = fieldMatchInfos;
        this.styleClass = styleClass;
    }

    /**
     * Adds &lt;nobr&gt; and &lt;wbr&gt; tags within the provided object if if
     * is a {@code String} so that the value of an {@link
     * org.recipnet.site.shared.bl.SampleTextBL#IUPAC_NAME IUPAC_NAME}
     * annotation name may be wrapped but still readable. If the provided object
     * is null, the empty string is returned.
     * 
     * @param obj a {@code String} that does NOT contain HTML code
     * @throws IllegalArgumentException if the 'obj' is not a String
     */
    public String formatObject(Object obj) {
        if (obj == null) {
            return "";
        }
        if (!(obj instanceof String)) {
            throw new IllegalArgumentException();
        }
        String iupacName = HtmlControl.escapeNestedValue((String) obj);

        // The following array indicates the number of characters for which
        // lower-level (higher numbered/indexed) rules should be ignored
        // For example: if rules[0] is greater than zero, no other rules
        // may be applied
        int rules[] = { 0, 0, 0, 0, 0 };
        StringBuffer sb = new StringBuffer(iupacName.length() + 128);
        sb.append("<nobr>");
        char[] characters = iupacName.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            // decrement the rule-suppression counters
            for (int x = 0; x < rules.length; x++) {
                rules[x]--;
            }

            // append the current character to the marked up response
            sb.append(characters[i]);

            // Rule 0: after " ", suppress all other rules for 12 characters
            if (characters[i] == ' ') {
                sb.append("</nobr><wbr /><nobr>");
                rules[0] = 12;
            }
            if (rules[0] >= 0) {
                continue;
            }

            // Rule 1: after ")-", insert a <wbr /> and suppress higher
            // numbered rules for 10 characters
            if ((characters[i] == ')') && (characters.length > (i + 1))
                    && (characters[i + 1] == '-')) {
                i++;
                sb.append("-</nobr><wbr /><nobr>");
                rules[1] = 10;
            }
            if (rules[1] >= 0) {
                continue;
            }

            // Rule 2: after "]-", insert a <wbr /> and suppress higher
            // numbered rules for 10 characters
            if ((characters[i] == ']') && (characters.length > (i + 1))
                    && (characters[i + 1] == '-')) {
                i++;
                sb.append("-</nobr><wbr /><nobr>");
                rules[2] = 10;
            }
            if (rules[2] >= 0) {
                continue;
            }

            // Rule 3: after ")", insert a <wbr /> and suppress higher
            // numbered rules for 10 characters
            if (characters[i] == ')') {
                sb.append("</nobr><wbr /><nobr>");
                rules[3] = 10;
            }
            if (rules[3] >= 0) {
                continue;
            }

            // Rule 4: after "]", insert a <wbr /> and suppress higher
            // numbered rules for 10 characters
            if (characters[i] == ']') {
                sb.append("</nobr><wbr /><nobr>");
                rules[4] = 10;
            }
            if (rules[4] >= 0) {
                continue;
            }

            // Rule 5: after "-", insert a <wbr />
            if (characters[i] == '-') {
                sb.append("</nobr><wbr /><nobr>");
            }
        }
        sb.append("</nobr>");
        if ((this.fieldMatchInfos != null) && !this.fieldMatchInfos.isEmpty()) {
            return "<span class=\"" + this.styleClass + "\">" + sb.toString()
                    + "</span>";
        } else {
            return sb.toString();
        }
    }

}
