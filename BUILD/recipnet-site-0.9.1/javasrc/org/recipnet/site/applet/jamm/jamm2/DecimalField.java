/*
 * Reciprocal Net Project
 *
 * @(#) DecimalField.java
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
 * 25-Feb-2003: jobollin reformatted the source and added javadoc comments as
 *              part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.Toolkit;
import java.text.*;
import javax.swing.*;

/**
 * A <code>JTextField</code> that only accepts decimal numbers
 */
public class DecimalField
        extends JTextField {

    /** A <code>NumberFormat</code> with which to format and verify the
     *  text
     */
    private NumberFormat format;

    /**
     * Constructs a new <code>DecimalField</code> with the specified initial
     * value, number of columns, and associated format
     *
     * @param  value a <code>double</code> containing the initial value for
     *         the field
     * @param  columns the number of columns for the field
     * @param  f the <code>NumberFormat</code> that this field should use
     */
    public DecimalField(double value, int columns, NumberFormat f) {
        super(columns);
        setDocument(new FormattedDocument(f));
        format = f;
        setValue(value);
    }

    /**
     * Sets the value of this field according <code>value</code>
     *
     * @param value the <code>double</code> to display in this field
     */
    public void setValue(double value) {
        setText(format.format(value));
    }

    /**
     * Returns the value of this field as a <code>double</code>
     *
     * @return the <code>double</code> represented by this field
     */
    public double getValue() {
        double retVal = 0.0;

        try {
            retVal = format.parse(getText()).doubleValue();
        } catch (ParseException e) {
            // This should never happen because insertString allows
            // only properly formatted data to get in the field.
            Toolkit.getDefaultToolkit().beep();
            System.err.println("getValue: could not parse: " + getText());
        }
        return retVal;
    }
}
