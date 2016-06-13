/*
 * Reciprocal Net Project
 *
 * @(#) Blender.java
 *
 * By John C. Bollinger, Indiana University Molecular Structure Center
 *
 * xx-xxx-1999: jobollin wrote first draft
 * 10-Jan-2003: jobollin added Javadoc comments and this file comment, and
 *              reformatted the source according to Reciprocal Net conventions
 *              (retroactively assigned to task #749)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm
 */

package org.recipnet.site.applet.jamm;
import java.awt.Color;

/**
 * A utility class containing static methods for computing weighted averages;
 * specifically intended for blending Colors, but useful for general ints,
 * floats, and doubles, too
 *
 * @author John C. Bollinger
 * @version 0.5.2
 */
public class Blender {

    /**
     * Computes a weighted average of two <code>int</code>s
     *
     * @param fg one of the <code>int</code>s to be averaged
     * @param bg one of the <code>int</code>s to be averaged
     * @param fgRatio the weight for <code>fg</code> in the average
     *
     * @return an <code>int</code> containing an approximation to the weighted
     *         average of <code>fg</code> and <code>bg</code> with weights
     *         <code>fgratio</code> and (1 - <code>fgratio</code>),
     *         respectively
     */
    public static int blend(int fg, int bg, float fgRatio) {
        return (int) (bg + ((fg - bg) * fgRatio));
    }

    /**
     * Computes a weighted average of two <code>float</code>s
     *
     * @param fg one of the <code>float</code>s to be averaged
     * @param bg one of the <code>float</code>s to be averaged
     * @param fgRatio the weight for <code>fg</code> in the average
     *
     * @return a <code>float</code> containing the weighted average of
     *         <code>fg</code> and <code>bg</code> with weights
     *         <code>fgratio</code> and (1 - <code>fgratio</code>),
     *         respectively
     */
    public static float blend(float fg, float bg, float fgRatio) {
        return (bg + ((fg - bg) * fgRatio));
    }

    /**
     * Computes a weighted average of two <code>double</code>s
     *
     * @param fg one of the <code>double</code>s to be averaged
     * @param bg one of the <code>double</code>s to be averaged
     * @param fgRatio the weight for <code>fg</code> in the average
     *
     * @return a <code>double</code> containing the weighted average of
     *         <code>fg</code> and <code>bg</code> with weights
     *         <code>fgratio</code> and (1 - <code>fgratio</code>),
     *         respectively
     */
    public static double blend(double fg, double bg, double fgRatio) {
        return (bg + ((fg - bg) * fgRatio));
    }

    /**
     * Blends two <code>Color</code>s RGB component-wise
     *
     * @param fg the <code>Color</code> to be blended as the foreground color
     * @param bg the <code>Color</code> to be blended as the background color
     * @param fgRatio the proprotion of the foreground color to use in the
     *        result
     *
     * @return a <code>Color</code> representing a blend, RGB component by RGB
     *         component, of <code>fg</code> and <code>bg</code> with weights
     *         <code>fgratio</code> and (1 - <code>fgratio</code>),
     *         respectively
     */
    public static Color blend(Color fg, Color bg, float fgRatio) {
        return new Color(blend(fg.getRed(), bg.getRed(), fgRatio),
            blend(fg.getGreen(), bg.getGreen(), fgRatio),
            blend(fg.getBlue(), bg.getBlue(), fgRatio));
    }
}
