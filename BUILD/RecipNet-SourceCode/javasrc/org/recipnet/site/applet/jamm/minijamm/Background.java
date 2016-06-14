/*
 * Reciprocal Net project
 * 
 * Background.java
 * 
 * 1996:        jchuffma wrote first draft
 * 13-Dec-2002: jobollin added initial comment and javadocs, and made all
 *              imports explicit
 * 21-Feb-2003: jobollin reformatted the source and extended the javadocs
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.minijamm
 *              package to org.recipnet.site.applet.jamm.minijamm
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.minijamm;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

/**
 * encapsulates an <code>Image</code>, a graphics context, and some sizing
 * parameters, and can draw a tiled version of the image on the graphics
 * context; intended for use as a background
 */
public class Background {

    /** the <code>Image</code> to use for the background */
    Image backgroundImage = null;

    /**
     * a <code>MediaTracker</code> with which to monitor the loading of the
     * background image
     */
    MediaTracker tracker;

    /** the height of the area that this background must cover */
    int appheight;

    /** the width of the area that this background must cover */
    int appwidth;

    /** the native height of the background image */
    int bheight;

    /** the native width of the background image */
    int bwidth;

    /**
     * Constructs a new <code>Background</code>
     *
     * @param  parent the <code>Applet</code> for which to create a 
     *         <code>Background</code>
     */
    Background(Applet parent) {
        tracker = new MediaTracker(parent);
        Dimension parentSize = parent.getSize();
        appwidth = parentSize.width;
        appheight = parentSize.height;

        backgroundImage = getBackgroundImage("image", parent);
        if (backgroundImage != null) {
            tracker.addImage(backgroundImage, 1);
            try {
                tracker.waitForID(1);
            } catch (InterruptedException e) {
                // ignore it
            }
            bwidth = backgroundImage.getWidth(parent);
            bheight = backgroundImage.getHeight(parent);
        }

        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            // ignore it
        }
    }

    /**
     * Draws the background image on <code>parent</code> with the provided
     * graphics context
     *
     * @param  g the <code>Graphics</code> with which to draw
     * @param  parent the <code>Applet</code> on which to draw
     */
    public void drawBackground(Graphics g, Applet parent) {
        if ((backgroundImage == null) || (tracker.isErrorID(1))) {
            return;
        }
        for (int j = 0; j < appheight; j += bheight) {
            for (int i = 0; i < appwidth; i += bwidth) {
                g.drawImage(backgroundImage, i, j, parent);
            }
        }
    }

    /**
     * Attempts to load the image named by <code>parent</code>'s parameter of
     * name <code>name</code>
     *
     * @param  name a <code>String</code> containing the name of the applet
     *         parameter from which to obtain the name of the image to load
     * @param  parent the <code>Applet</code> from which to read the named
     *         parameter and with which to attempt to load the image
     *
     * @return An <code>Image</code> obtained by means of
     *         <code>parent.getImage</code>, or <code>null</code> if for any
     *         reason none could be loaded; it may not be fully loaded when
     *         this method returns, per the contract of
     *         <code>parent.getImage</code>
     * 
     * @see java.applet.Applet#getImage(java.net.URL, java.lang.String)
     */
    protected Image getBackgroundImage(String name, Applet parent) {
        try {
            return parent.getImage(parent.getCodeBase(),
                    parent.getParameter(name));
        } catch (Exception e) {
            return null;
        }
    }
}
