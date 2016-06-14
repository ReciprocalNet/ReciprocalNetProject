/*
 * @(#) FormattedDocument.java
 *
 * This class derived from one shipped with Sun's Java tutorial; the
 * following copyright and license pertain:
 *
 * Copyright (c) 1997, 1998, 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * -- end of license --
 * 
 * 09-Jan-2003: jobollin removed unused imports
 * 27-Feb-2003: jobollin reformatted the source and revised the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.text.Format;
import java.text.ParseException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * A <code>Document</code> with an attached <code>Format</code> that controls
 * what text is permitted in the document.  (Only text that can be parsed by the
 * format is allowed.  This class is general enough to handle dates,
 * integers, percents, money, strings, or anything for which a
 * <code>Format</code> can be provided.  Text fields that use this type of
 * document are locale-sensitive, because the formats provided by the JDK
 * are locale-sensitive.
 */
public class FormattedDocument
        extends PlainDocument {

    /** The <code>Format</code> governing this document */
    private Format format;

    /**
     * Constructs a new <code>FormattedDocument</code> with the specified
     * <code>Format</code>
     *
     * @param f the <code>Format</code> to use for this document
     */
    public FormattedDocument(Format f) {
        format = f;
    }

    /**
     * Gets the <code>Format</code> governing this document
     *
     * @return the <code>Format</code>
     */
    public Format getFormat() {
        return format;
    }

    /**
     * Overrides <code>PlainDocument.insertString(int, String,
     * AttributeSet)</code>.  Attempts to insert the specified string into this
     * document at the specified permissions and with the specified attributes.
     * Fails with a message to System.err if the result of the insertion would
     * not be suitable for this document's format.
     *
     * @param  offs the offset from the beginning of this document at which to
     *         insert the string
     * @param  str the <code>String</code> to insert
     * @param  a an <code>AttributeSet</code> to apply to the new text
     *
     * @throws BadLocationException if <code>offs</code> does not correspond to
     *         a position in this document
     */
    public void insertString(int offs, String str, AttributeSet a)
            throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset = currentText.substring(offs, currentText.length());
        String proposedResult = beforeOffset + str + afterOffset;

        try {
            format.parseObject(proposedResult);
            super.insertString(offs, str, a);
        } catch (ParseException e) {
            System.err.println("insertString: could not parse: "
                + proposedResult);
        }
    }

    /**
     * Attempts to remove characters from the document; fails with a message to
     * <code>System.err</code> if the result would not be suitable for this
     * document's format
     *
     * @param offs the offset from the beginning of this document at which to
     *        remove characters
     * @param len the number of characters to remove
     *
     * @throws BadLocationException if <code>offs</code> does not correspond to
     *         a valid position in this document
     */
    public void remove(int offs, int len)
            throws BadLocationException {
        String currentText = getText(0, getLength());
        String beforeOffset = currentText.substring(0, offs);
        String afterOffset =
            currentText.substring(len + offs, currentText.length());
        String proposedResult = beforeOffset + afterOffset;

        try {
            if (proposedResult.length() != 0) {
                format.parseObject(proposedResult);
            }
            super.remove(offs, len);
        } catch (ParseException e) {
            System.err.println("remove: could not parse: " + proposedResult);
        }
    }
}
