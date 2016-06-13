/*
 * Reciprocal Net project
 * 
 * SpaceGroupSymbolHtmlFormatter.java
 * 
 * 11-Jul-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added basic support for FieldMatchInfo-specific
 *              highlighting
 * 27-Apr-2006: jobollin updated formatObject() to not insert a space between
 *              the centering symbol and operators, but to pad the operators
 *              on the left if the first one has a rotation component
 * 19-May-2006: jobolllin modified the default overbar styling to raise it
 *              a bit
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import org.recipnet.common.HtmlFormatter;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL;
import org.recipnet.site.shared.search.FieldMatchInfo;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.Operator;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL.SpaceGroupSymbol;

/**
 * <p>
 * A {@code Formatter} class that is meant to mark up a space group symbol. This
 * class relies heavily on {@link org.recipnet.site.shared.bl.SpaceGroupSymbolBL
 * SpaceGroupSymbolBL}.
 * </p><p>
 * The following rules are used for formatting the {@code SpaceGroupSymbol}
 * returned from {@link
 * org.recipnet.site.shared.bl.SpaceGroupSymbolBL#digestSymbol(String)
 * SpaceGroupSymbolBL.digestSymbol()}:
 * <ul>
 *   <li>The centering symbol is put in &lt;i&gt; tags</li>
 *   <li>If the first operator contains a rotation component, then the operators
 *   are enclosed in a &lt;span&gt; that applies a small amount of left
 *   padding</li>
 *   <li>If an operator is a rotoinversion, a hyphen '-' is output within
 *   &lt;span&gt; tags containing the style indicated by the 'overbarStyle'
 *   member variable, and all subsequent operators are output within a
 *   &lt;span&gt; tag with the style indicated by the 'undertextStyle' member
 *   variable.</li>
 *   <li>Screw translation components are put in &lt;sub&gt; tags.</li>
 *   <li>Mirror components are put within &lt;i&gt; tags and if they follow
 *   rotation components they are preceded by a '/' character.</li>
 * </ul>
 * </p>
 */
public class SpaceGroupSymbolHtmlFormatter implements HtmlFormatter {

    /** The style for the hyphen that becomes the overbar. */
    private String overbarStyle;

    /** The style for all characters after the overbar. */
    private String undertextStyle;

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

    /**
     * A default constructor that sets the style for the overbar so that it is
     * shifted up and the undertext is shifted left (under the overbar).  The
     * centering symbol will be italicized as will any reflection symbol.  The
     * screw translation will be written in subscript.
     */
    public SpaceGroupSymbolHtmlFormatter() {
        this(null, null);
    }

    /**
     * A constructor that sets the style for the overbar so that it is shifted
     * up and for the following text so that it is shifted left (under the
     * overbar).  The specified style class is applied to the whole formatted
     * representation (by wrapping it in a &lt;span&gt;) if the provided
     * {@code fieldMatchInfos} collection is not null or empty.
     * 
     * @param fieldMatchInfos a Collection containing all of the
     *        {@code FieldMatchInfo} objects that pertain to the space group
     *        symbol that will be formatted by this {@code HtmlFormatter}
     * @param styleClass the style class to assign to this field if it matched a
     *        search (as described by the {@code fieldMatchInfos})
     */
    public SpaceGroupSymbolHtmlFormatter(
            Collection<FieldMatchInfo> fieldMatchInfos, String styleClass) {
        this("position: relative; top: -0.75em;",
             "position: relative; left: -0.45em;", fieldMatchInfos,
             styleClass);
    }

    /**
     * Constructs a formatter that uses the specified styling for the overbar
     * hyphen and for the following text. The specified style class is applied
     * to the whole formatted representation (by wrapping it in a &lt;span&gt;)
     * if the provided {@code fieldMatchInfos} collection is not null or empty.
     * 
     * @param overbarStyle indicates the style for the hyphen ('-') that
     *        precedes any rotoinverted operations
     * @param undertextStyle indicates the style for all characters that follow
     *        the rotoinversion hyphen
     * @param fieldMatchInfos a Collection containing all of the
     *        {@code FieldMatchInfo} objects that pertain to the space group
     *        symbol that will be formatted by this {@code HtmlFormatter}
     * @param styleClass the style class to assign to this field if it matched a
     *        search (as described by the {@code fieldMatchInfos})
     */
    public SpaceGroupSymbolHtmlFormatter(String overbarStyle,
            String undertextStyle,
            Collection<FieldMatchInfo> fieldMatchInfos, String styleClass) {
        this.overbarStyle = overbarStyle;
        this.undertextStyle = undertextStyle;
        this.fieldMatchInfos = fieldMatchInfos;
        this.styleClass = styleClass;
    }

    /**
     * Marks up the supplied space group symbol in HTML by surrounding the
     * specific portions of the 'digested' symbol (as obtained by a call to
     * {@link org.recipnet.site.shared.bl.SpaceGroupSymbolBL#digestSymbol(
     * String) SpaceGroupSymbolBL.digestSymbol()}) with &lt;span&gt; tags whose
     * style are set in accordance with the style member variables of this
     * instance. If the symbol is invalid or cannot be formatted or digested, it
     * is returned unmodified.
     * 
     * @param obj a {@code String} representing a space group symbol that does
     *        NOT contain HTML code
     * @return a {@code String} containing the formatted version of the object
     * @throws IllegalArgumentException if the 'obj' parameter is not a
     *         {@code String}
     */
    public String formatObject(Object obj) {
        SpaceGroupSymbol digestedSymbol;

        if (obj == null) {
            return "";
        } else if (obj instanceof SpaceGroupSymbol) {
            digestedSymbol = (SpaceGroupSymbol) obj;
        } else if (obj instanceof String) {
            String sgString = (String) obj;
            
            try {
                String formattedSymbol
                        = SpaceGroupSymbolBL.createFormattedSymbol(sgString);

                if (SpaceGroupSymbolBL.isSymbolValid(formattedSymbol)) {
                    digestedSymbol
                            = SpaceGroupSymbolBL.digestSymbol(formattedSymbol);
                } else {
                    /*
                     * the symbol is invalid; rather than have it potentially
                     * trucated just display the invalid String without
                     * formatting
                     */
                    return sgString;
                }
            } catch (InvalidDataException ex) {
                /*
                 * the formatted string could not be "digested"; just display it
                 * without formatting
                 */
                return sgString;
            }
        } else {
            throw new IllegalArgumentException();
        }

        // generate the marked-up version
        StringBuilder formattedSymbol = new StringBuilder();
        boolean isFirstOp = true;
        int openSpans = 0;

        if ((this.fieldMatchInfos != null) && !this.fieldMatchInfos.isEmpty()) {
            formattedSymbol.append("<span class=\"");
            formattedSymbol.append(this.styleClass);
            formattedSymbol.append("\">");
            openSpans++;
        }
        formattedSymbol.append("<i>");
        formattedSymbol.append(digestedSymbol.getCentering());
        formattedSymbol.append("</i>");
        for (Operator op : digestedSymbol.getOperators()) {
            if (op.hasRotationComponent()) {
                if (isFirstOp) {
                    formattedSymbol.append(
                            "<span style=\"padding-left: 0.125em;\">");
                    openSpans++;
                }
                if (op.isRotoInversion()) {
                    formattedSymbol.append("<span style=\"");
                    formattedSymbol.append(this.overbarStyle);
                    formattedSymbol.append("\">-</span><span style=\"");
                    formattedSymbol.append(this.undertextStyle);
                    formattedSymbol.append("\">");
                    openSpans++;
                }
                formattedSymbol.append(op.getRotationOrder());
                if (op.getScrewTranslation() > 0) {
                    formattedSymbol.append("<sub>");
                    formattedSymbol.append(op.getScrewTranslation());
                    formattedSymbol.append("</sub>");
                }
                if (op.hasMirrorComponent()) {
                    formattedSymbol.append("/<i>");
                    formattedSymbol.append(op.getMirrorComponent());
                    formattedSymbol.append("</i>");
                }
            } else {
                assert op.hasMirrorComponent()
                        : "operator has neither rotation nor mirror component";
                formattedSymbol.append("<i>");
                formattedSymbol.append(op.getMirrorComponent());
                formattedSymbol.append("</i>");
            }
            isFirstOp = false;
        }
        
        for (; openSpans > 0; openSpans--) {
            formattedSymbol.append("</span>");
        }

        return formattedSymbol.toString();
    }
}
