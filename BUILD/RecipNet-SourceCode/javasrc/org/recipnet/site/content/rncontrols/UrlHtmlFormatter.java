/*
 * Reciprocal Net project
 * 
 * UrlHtmlFormatter.java
 * 
 * 12-Apr-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added basic support for FieldMatchInfo-specific
 *              highlighting
 * 28-Feb-2006: jobollin fixed mismatched start and end tags in output HTML
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import org.recipnet.common.HtmlFormatter;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.site.shared.search.FieldMatchInfo;

/**
 * <p>
 * An {@code HtmlFormatter} that takes a given {@code String} that represents a
 * URL, and makes it into a hypertext link. The target and the text of the link
 * are both the value of the provided {@code String}.
 * </p><p>
 * Example: "http://java.sun.com/" would be formatted as "&lt;a
 * href="http://java.sun.com/"&gt;http://java.sun.com/&lt;/a&gt;"
 * </p>
 */
public class UrlHtmlFormatter implements HtmlFormatter {

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
    public UrlHtmlFormatter() {
        this.fieldMatchInfos = null;
        this.styleClass = null;
    }

    /**
     * A constructor that allows {@code FieldMatchInfo} objects to be provided
     * to indicate whether/how the formatted field should be highlighted.
     */
    public UrlHtmlFormatter(Collection<FieldMatchInfo> fieldMatchInfos,
            String styleClass) {
        this.fieldMatchInfos = fieldMatchInfos;
        this.styleClass = styleClass;
    }

    /**
     * Uses the provided String as the 'href' attribute and the body of an HTML
     * &lt;a&gt; tag. If the 'obj' is null, this method returns an empty string.
     * The supplied {@code String} will be escaped via a calls to
     * {@link org.recipnet.common.controls.HtmlControl#escapeAttributeValue
     * HtmlControl.escapeAttributeValue()} and
     * {@link org.recipnet.common.controls.HtmlControl#escapeNestedValue
     * HtmlControl.escapeNestedValue()} respectively.
     * 
     * @param  obj a {@code String} that must be (possibly after some escaping)
     *         a valid value for the HTML &lt;a&gt; tag's 'href' attribute.
     *         
     * @return the provided {@code String} within a hypertext link with the
     *         provided {@code String} as the target
     *         
     * @throws IllegalArgumentException if the 'obj' is not a {@code String}
     */
    public String formatObject(Object obj) {
        if (obj == null) {
            return "";
        } else if (!(obj instanceof String)) {
            throw new IllegalArgumentException();
        } else {
            String returnValue = new String("<a href=\""
                    + HtmlControl.escapeAttributeValue((String) obj) + "\">"
                    + HtmlControl.escapeNestedValue((String) obj) + "</a>");
            
            if ((this.fieldMatchInfos != null)
                    && !this.fieldMatchInfos.isEmpty()) {
                return "<span class=\"" + this.styleClass + "\">" + returnValue
                        + "</span>";
            } else {
                return returnValue;
            }
        }
    }
}
