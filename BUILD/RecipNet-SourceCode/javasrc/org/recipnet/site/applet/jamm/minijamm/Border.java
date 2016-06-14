/*
 * Reciprocal Net PRoject
 *
 * miniJaMM.java
 *
 * 1996:        jhuffma wrote first draft
 * 13-Dec-2002: jobollin created initial comment and revised class-level
 *              javadocs
 * 21-Feb-2003: jobollin reformatted the source and extended the javadocs as
 *              part of task #742
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.minijamm
 *              package to org.recipnet.site.applet.jamm.minijamm
 */

package org.recipnet.site.applet.jamm.minijamm;
import java.awt.Color;
import java.awt.Graphics;

/**
 * On demand, draws a raised border inside a rectange; intended to be of use as
 * a border
 */
public class Border {

    /** The darker color used for the border */
    Color dark;

    /** The lighter color used for the border */
    Color light;

    /** The graphics context with which to draw the border */
    Graphics g;

    /** the height of the border */
    int h;

    /** the width of the border */
    int w;

    /** the x coordinate of the top left corner of the border */
    int x;

    /** the y coordinate of the top left corner of the border */
    int y;

    /**
     * Constructs a new <code>Border</code>
     *
     * @param  gr the <code>Graphics</code> with which to draw the border
     * @param  width the width of the border
     * @param  height the height of the border
     */
    Border(Graphics gr, int width, int height) {
        light = new Color(224, 224, 224);
        dark = new Color(128, 128, 128);
        g = gr;
        w = width;
        h = height;
        x = 0;
        y = 0;
    }

    /**
     * Constructs a new <code>Border</code>
     *
     * @param  gr the <code>Graphics</code> with which to draw the border
     * @param  x the x coordinate of the top left corner of the border
     * @param  y the y coordinate of the top left corner of the border
     * @param  width the width of the border
     * @param  height the height of the border
     */
    Border(Graphics gr, int x, int y, int width, int height) {
        light = new Color(224, 224, 224);
        dark = new Color(128, 128, 128);
        g = gr;
        w = width;
        h = height;
        this.x = x;
        this.y = y;
    }

    /**
     * draws this <code>Border</code>
     */
    public void draw() {
        g.setColor(light);
        g.drawLine(x, h, x, y);
        g.drawLine(x, y, w, y);
        g.drawLine(w - 6, y + 5, w - 6, h - 6);
        g.drawLine(w - 6, h - 6, x + 5, h - 6);
        g.setColor(Color.white);
        g.drawLine(x + 1, h - 1, x + 1, y + 1);
        g.drawLine(x + 1, y + 1, w - 1, y + 1);
        g.drawLine(w - 5, x + 4, w - 5, h - 5);
        g.drawLine(w - 5, h - 5, x + 4, h - 5);
        g.setColor(dark);
        g.drawLine(x + 4, h - 6, x + 4, y + 4);
        g.drawLine(x + 4, y + 4, w - 6, y + 4);
        g.drawLine(w - 2, y + 2, w - 2, h - 2);
        g.drawLine(w - 2, h - 2, x + 2, h - 2);
        g.setColor(Color.black);
        g.drawLine(x + 5, h - 7, x + 5, y + 5);
        g.drawLine(x + 5, y + 5, w - 7, y + 5);
        g.drawLine(w - 1, y + 1, w - 1, h - 1);
        g.drawLine(w - 1, h - 1, x + 1, h - 1);
    }
}
