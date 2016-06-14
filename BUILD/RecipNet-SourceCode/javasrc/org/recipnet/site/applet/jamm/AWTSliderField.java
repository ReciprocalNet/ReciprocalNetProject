/*
 * Reciprocal Net Project
 * 
 * @(#)AWTSliderField.java
 * 
 * xx-xxx-1999: jobollin wrote first draft
 * 25-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 25-Feb-2003: jobollin removed method buildMe and put its body into the
 *              three-arg constructor
 * 25-Feb-2003: jobollin made all imports explicit
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * An AWT component that provides a linked <code>TextField</code> and
 * <code>Scrollbar</code> with an optional label
 */
public class AWTSliderField
        extends Panel {

    /** the <code>Scrollbar</code> of this component */
    protected Scrollbar slider;

    /** the <code>TextField</code> of this component */
    protected TextField text;

    /**
     * Constructs a new <code>AWTSliderField</code>
     */
    public AWTSliderField() {
        this(255, 0, null);
    }

    /**
     * Constructs a new <code>AWTSliderField</code> with the specified maximum
     * value
     *
     * @param  max the maximum value representable by this
     *         <code>AWTSliderField</code> 
     */
    public AWTSliderField(int max) {
        this(max, 0, null);
    }

    /**
     * Constructs a new <code>AWTSliderField</code> with the specified label
     *
     * @param  l the label for this <code>AWTSliderField</code>
     */
    public AWTSliderField(Label l) {
        this(255, 0, l);
    }

    /**
     * Constructs a new <code>AWTSliderField</code> with the specified maximum
     * and minimum values and label
     *
     * @param  max the maximum value for this <code>AWTSliderField</code>
     * @param  min the minimum value for this <code>AWTSliderField</code>
     * @param  l the label for this <code>AWTSliderField</code>
     */
    public AWTSliderField(int max, int min, Label l) {
        super(new GridLayout(2, 1));
        
        Label lab = ((l == null) ? new Label() : l);
        
        lab.setAlignment(Label.CENTER);
        text = new TextField(String.valueOf(max), maxDigits(max));
        text.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        slider.setValue(getFVal());
                    } catch (NumberFormatException nfe) {
                        /*
                         * By catching these here and then synchronizing the 
                         * text field to the slider, any invalid characters
                         * in the text field are automatically (but silently)
                         * rejected immediately after input
                         */
                    } finally {
                        syncText();
                    }
                }
            });

        Panel p = new Panel(new GridLayout(1, 2));
        p.add(lab);
        p.add(text);
        add(p);
        p = null;
        slider = new Scrollbar(Scrollbar.HORIZONTAL);
        setSliderRange(max, min);
        slider.addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    syncText();
                }
            });
        add(slider);
    }

    /**
     * Returns the integer value the current text field value
     *
     * @return the slider position value corresponding to the text field value
     */
    public int getFVal() {
        return Integer.parseInt(text.getText());
    }

    /**
     * Sets a new range for the slider and adjusts the text field accordingly.
     * The new number of columns for the text field is determined via the
     * <code>maxDigits(int)</code> method, and after the field size is set its
     * value is updated via <code>syncText()</code>.
     *
     * @param  max the maximum value for the slider
     * @param  min the minumum value for the slider
     */
    public void setSliderRange(int max, int min) {
        int bubble = 1 + ((max - min) / 10);
        slider.setValues(max, bubble, min, max + bubble);
        text.setColumns(maxDigits(max));
        syncText();
    }

    /**
     * Sets the slider value to <code>value</code> and updates the text field
     *
     * @param  value the new value for the slider
     */
    public void setValue(int value) {
        slider.setValue(value);
        syncText();
    }

    /**
     * Gets the current value of the slider
     *
     * @return the current value of the slider
     */
    public int getValue() {
        return slider.getValue();
    }

    /**
     * Adds an <code>AdjustmentListener</code> to the slider
     *
     * @param  l the <code>AdjustmentListener</code> to add
     */
    public void addAdjustmentListener(AdjustmentListener l) {
        slider.addAdjustmentListener(l);
    }

    /**
     * Returns the maximum number of columns needed to represent the
     * value of an <code>AWTSliderField</code> as text if the maximum
     * slider value is <code>max</code>.  This method is non-static for
     * polymorphism purposes; it does not use or modify the state of this
     * <code>AWTSliderField</code>.
     *
     * @param  max the maximum slider value to test
     *
     * @return the number of columns required for the text field if the slider
     *         maximum is <code>max</code>
     */
    public int maxDigits(int max) {
        return (int) (Math.log(max + 0.5d) / 2.302585092d) + 1;
    }

    /**
     * Removes <code>l</code> as an <code>AdjustmentListener</code> on this
     * <code>AWTSliderField</code>'s slider
     *
     * @param  l the <code>AdjustmentListener</code> to remove
     */
    public void removeAdjustmentListener(AdjustmentListener l) {
        slider.removeAdjustmentListener(l);
    }

    /**
     * Sets the text field value to correspond to the slider position
     */
    protected void syncText() {
        text.setText(String.valueOf(slider.getValue()));
    }
}
