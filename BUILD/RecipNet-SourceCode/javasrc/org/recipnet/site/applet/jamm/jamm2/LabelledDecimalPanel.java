/*
 * Reciprocal Net Project
 * 
 * @(#)LabelledDecimalPanel.java
 * 
 * xx-xxx-1998: jobollin wrote first draft
 * 27-Feb-2003: jobollin reformatted the source and revised and extended the
 *              javadoc comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A <code>JPanel</code> containing a <code>DecimalField</code> with an
 * associated label.  This serves to group the field and label and to provide a
 * joint <code>setEnabled(boolean)</code> method.  Field accessor
 * methods permit individual manipulation of the label and field
 * members of an instance of this class, but they cannot be replaced with
 * different ones.
 */
public class LabelledDecimalPanel
        extends JPanel {

    /** The <code>DecimalField</code> contained by this panel */
    protected DecimalField data;

    /** The <code>JLabel</code> contained by this panel  */
    protected JLabel label;

    /**
     * Constructs a new <code>LabelledDecimalPanel</code> with the specified
     * field and label, using the default layout manager
     *
     * @param df the <code>DecimalField</code> to be contained by this panel
     * @param l te <code>JLabel</code> to be contained by this panel
     */
    public LabelledDecimalPanel(DecimalField df, JLabel l) {
        super();
        data = df;
        label = l;
        add(l);
        add(df);
    }

    /**
     * Sets whether the internal field and label are enabled
     *
     * @param enabled <code>true</code> to enable, <code>false</code> to disable
     */
    public void setEnabled(boolean enabled) {
        data.setEnabled(enabled);
        label.setEnabled(enabled);
    }

    /**
     * Gets a reference to the internal decimal field
     *
     * @return a reference to the internal <code>DecimalField</code>
     */
    public DecimalField getField() {
        return data;
    }

    /**
     * Gets a reference to the internal label
     *
     * @return a reference to the internal <code>JLabel</code>
     */
    public JLabel getLabel() {
        return label;
    }
}
