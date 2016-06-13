/*
 * Reciprocal Net Project
 * 
 * @(#)AWTRatioSliderField.java
 * 
 * xx-xxx-1999: jobollin wrote first draft
 * 25-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;
import java.awt.Label;

/**
 * An <code>AWTSliderField</code> for which the text field
 * displays the decimal ratio representing the position of the thumb
 * along the track.
 */
public class AWTRatioSliderField
        extends AWTSliderField {

    /** the maximum integer value the slider position may have */
    private int maxVal;

    /**
     * Constructs a new <code>AWTRatioSliderField</code>
     */
    public AWTRatioSliderField() {
        this(3, null);
    }

    /**
     * Constructs a new <code>AWTRatioSliderField</code> with the specified
     * decimal precision
     *
     * @param  prec the number of decimal digits in the fractional part of the
     *         field value
     */
    public AWTRatioSliderField(int prec) {
        this(prec, null);
    }

    /**
     * Constructs a new <code>AWTRatioSliderField</code> with the specified
     * label
     *
     * @param  l the label to use
     */
    public AWTRatioSliderField(Label l) {
        this(3, l);
    }

    /**
     * Constructs a new <code>AWTRatioSliderField</code> with the specified
     * precision and label
     *
     * @param  prec the number of decimal digits in the fractional part of the
     *         field value
     * @param  l the label to use
     */
    public AWTRatioSliderField(int prec, Label l) {
        super(l);
        maxVal = (int) Math.round(Math.pow(10, ((prec > 6) ? 6 : prec)));
        setSliderRange(maxVal, 0);
        text.setColumns(prec + 2);
        syncText();
    }

    /**
     * Returns the integer value of the slider position corresponding to the
     * current text field value
     *
     * @return the slider position value corresponding to the text field value
     */
    public int getFVal() {
        return Math.round(Float.valueOf(text.getText()).floatValue() * maxVal);
    }

    /**
     * Returns the current ratio value based on the slider position and maximum
     *
     * @return the ratio of the slider position to the maximum slider position,
     *         as a <code>float</code>
     */
    public float getRatioValue() {
        return ((float) getValue()) / ((float) maxVal);
    }

    /**
     * Sets the slider and field to the closest representable values to
     * <code>value</code>
     *
     * @param  value the <code>float</code> target value
     */
    public void setValue(float value) {
        slider.setValue(Math.round(value * maxVal));
        syncText();
    }

    /**
     * Returns the maximum number of columns needed to represent the ratio
     * value of an <code>AWTRatioSliderField</code> as text if the maximum
     * slider value is <code>max</code>.  This method is non-static for
     * polymorphism purposes; it does not use or modify the state of this
     * <code>AWTRatioSliderField</code>.
     *
     * @param  max the maximum slider value to test
     *
     * @return the number of columns required for the text field if the slider
     *         maximum is <code>max</code>
     */
    public int maxDigits(int max) {
        return (int) (Math.log(max + 0.5d) / 2.302585092d) + 2;
    }

    protected void syncText() {
        text.setText(String.valueOf(getRatioValue()));
    }
}
