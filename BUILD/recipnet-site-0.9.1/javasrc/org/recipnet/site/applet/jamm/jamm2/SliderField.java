/*
 * Reciprocal Net Project
 *
 * @(#) SliderField.java
 *
 * 09-Jan-2003: jobollin added file comment, removed unused imports, made all
 *              imports explicit, and added Javadoc comments; retroactively
 *              assigned to task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.GridLayout;
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * a Swing component that provides a linked <code>WholeNumberField</code>
 * and <code>JSlider</code> with an optional label
 *
 * @author John C. Bollinger
 */
public class SliderField
        extends JPanel {

    /** A label for this component */
    private JLabel lab;

    /** The slider part of this component */
    private JSlider sl;

    /** the whole number field part of this component */
    private WholeNumberField wnf;

    /**
     * constructs a new <code>SliderField</code> with default parameters: range
     * 0 to 255, initial value 255, and no label
     */
    public SliderField() {
        this(255, 0, 255, null);
    }

    /**
     * constructs a new <code>SliderField</code> with the specified maximum
     * value, minimum value zero, initial value equal to the maximum, and no
     * label
     *
     * @param max the maximum allowed value of the slider and field
     */
    public SliderField(int max) {
        this(max, 0, max, null);
    }

    /**
     * constructs a new <code>SliderField</code> with the specified maximum
     * and initial values, minimum value zero, and no label
     *
     * @param max the maximum value of the SliderField
     * @param val the initial value of the SliderField
     */
    public SliderField(int max, int val) {
        this(max, 0, val, null);
    }

    /**
     * constructs a new <code>SliderField</code> with the specified maximum,
     * minimum, and initial values, and the specified label
     *
     * @param max the maximum value of the SliderField
     * @param min the minimum value of the SliderField
     * @param val the initial value of the SliderField
     * @param l a JLabel to use as a label; may be <code>null</code> for no
     *        label
     */
    public SliderField(int max, int min, int val, JLabel l) {
        super(new GridLayout(2, 1));
        int maxDigits = 1;
        for (int i = max; i > 9; i /= 10) {
            maxDigits++;
        }
        wnf = new WholeNumberField(maxDigits);
        wnf.setMaximumSize(wnf.getPreferredSize());
        BoundedRangeModel brm = (BoundedRangeModel) wnf.getDocument();
        brm.setRangeProperties(val, 0, min, max, false);
        sl = new JSlider(brm);
        sl.setOrientation(SwingConstants.HORIZONTAL);
        if (l == null) {
            lab = new JLabel();
        } else {
            lab = l;
        }
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        Box b = new Box(BoxLayout.X_AXIS);
        b.add(lab);
        b.add(Box.createGlue());
        b.add(wnf);
        add(b);
        add(sl);
    }

    /**
     * sets the label text from the provided <code>String</code>
     *
     * @param s a <code>String</code> containing the new text for the label; if
     *        <code>null</code> the label text is cleared
     */
    public void setLabel(String s) {
        lab.setText(s);
    }

    /**
     * returns the <code>BoundedRangeModel</code> used by this component's
     * slider
     *
     * @return the BoundedRangeModel used by this SliderField's JSlider
     */
    public BoundedRangeModel getModel() {
        return sl.getModel();
    }

    /**
     * programmatically sets the value of this SliderField to the specified
     * value, subject to the limits of this SliderField's range
     *
     * @param value the new value
     */
    public void setValue(int value) {
        sl.setValue(value); // The field is updated automatically
    }

    /**
     * return the current value of this <code>SliderField</code>
     *
     * @return the current value as an <code>int</code>
     */
    public int getValue() {
        return sl.getValue();
    }
}
