/*
 * Reciprocal Net Project
 *
 * @(#) Atom.java
 *
 * By John N. Huffman and John C. Bollinger
 *
 * xx-xxx-1997: jnhuffma wrote first draft
 * 09-Jan-2003: jobollin added file comment and class-level Javadoc comment and
 *              removed unused imports
 * 10-Jan-2003: jobollin reformatted the source according code conventions and
 *              added Javadoc comments (retroactively assigned to task #749)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package
 *              references to match source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm2;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import org.recipnet.site.applet.jamm.Blender;

/**
 * A class for calculating, storing, and scaling ball images
 *
 * @author John N. Huffman and John C. Bollinger
 */
public class Atom {

    /** A scale factor for converting centimeter units into pixel units */
    private static float cmToPixels = 37.5f;

    /** the distance from the stereo origin to the image plane in centimeters */
    private static float imagePositionCm = 40.0f;

    /** the distance from the stereo origin to the image plane in pixels */
    private static float imagePositionPixels = 1500f;

    /** the radius of the image in pixels */
    private final static int R = 40;

    /** the diameter of the image in pixels */
    private final static int D = R * 2;

    /** the number of images stored for each atom (for depth cueing) */
    private final static int N_BALLS = 16;

    /** contains the image data */
    private static WritableRaster ras;

    /** the greatest value in the raster */
    private static int maxr;

    /** A scale factor for converting Angstroms to pixels */
    private static float ballScale;

    static {
        /* parameters of the highlite */
        int hx = -15;
        int hy = -15;
        float gamma = 3.0f;

        /* image data */
        byte[] data = new byte[D * D];

        /* working variables */
        int rowWidth;
        int p;
        double r1;
        double r2;
        double x1;
        double y1;
        double yc;
        double maxval = 1.0d;
        double mr = Math.sqrt((hx * hx) + (hy * hy)) + R;

        /* calculate the image */
        for (int Y = D; Y-- > 0;) {
            yc = (0.5d + Y) - R;
            y1 = yc - hy;
            rowWidth = (int) Math.round(Math.sqrt((R * R) - (yc * yc)));
            p = (Y * D) + R;
            for (int X = -rowWidth; X < rowWidth; X++) {
                x1 = X - hx + 0.5d;
                r1 = Math.sqrt((x1 * x1) + (y1 * y1));
                r2 = mr * Math.pow(r1 / mr, 1.0 / gamma);
                maxval = Math.max(maxval, r2);
                data[p + X] = (byte) Math.max(r2, 1.0d);
            }
        }
        maxr = (int) Math.round(maxval);
        ras = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, D, D, 1, null);
        ras.setDataElements(0, 0, D, D, data);
        ballScale = 1.0f;
    }

    /**
     * The different <code>Image</code>s of for different depth cueing levels
     */
    private Image[] balls;

    /** the effective radius of the image */
    private float radius;

    /** the alpha component of the base color of this atom */
    private int baseAlpha;

    /** the blue component of the base color of this atom */
    private int baseBlue;

    /** the green component of the base color of this atom */
    private int baseGreen;

    /** the red component of the base color of this atom */
    private int baseRed;

    /** the blue component of the background */
    private int bgBlue;

    /** the green component of the background */
    private int bgGreen;

    /** the red component of the background */
    private int bgRed;

    /**
     * Constructs a new <code>Atom</code> with the specified color, background
     * color, and radius
     *
     * @param c the base atom color as a <code>Color</code>
     * @param bg the background color as a <code>Color</code>
     * @param rad the atom radius as a <code>float</code>
     */
    public Atom(Color c, Color bg, float rad) {
        baseRed = c.getRed();
        baseGreen = c.getGreen();
        baseBlue = c.getBlue();
        baseAlpha = c.getAlpha();
        radius = rad;
        setBackground(bg);
    }

    /**
     * Constructs a new Atom with the specified color and background color and
     * a default radius
     *
     * @param c the base atom color as a <code>Color</code>
     * @param bg the background color as a <code>Color</code>
     */
    public Atom(Color c, Color bg) {
        this(c, bg, 1.0f);
    }

    /**
     * Constructs a new Atom with the specified color and radius and a default
     * background color
     *
     * @param c the base atom color as a <code>Color</code>
     * @param rad the atom radius as a <code>float</code>
     */
    public Atom(Color c, float rad) {
        this(c, new Color(50, 50, 50), rad);
    }

    /**
     * Constructs a new Atom of the specified color and default background and
     * radius
     *
     * @param c the base atom color as a <code>Color</code>
     */
    public Atom(Color c) {
        this(c, new Color(50, 50, 50), 1.0f);
    }

    /**
     * Constructs a new <code>Atom</code> with all default attributes
     */
    public Atom() {
        this(new Color(127, 127, 127), new Color(50, 50, 50), 1.0f);
    }

    /**
     * Sets this Atom's ratio of pixels per centimeter
     *
     * @param ratio the new ratio as a <code>float</code>
     */
    public static void setPixelRatio(float ratio) {
        if (ratio > 0f) {
            cmToPixels = ratio;
            imagePositionPixels = cmToPixels * imagePositionCm;
        }
    }

    /**
     * Sets this Atom's background color; as a result of this action this
     * Atom's cache  of images for depth cueing will be regenerated upon next
     * access
     *
     * @param bg the new background color as a <code>Color</code>
     */
    public void setBackground(Color bg) {
        bgRed = bg.getRed();
        bgGreen = bg.getGreen();
        bgBlue = bg.getBlue();
        balls = null;
    }

    /**
     * Sets this Atom's scale factor for rendering
     *
     * @param s the new scale factor as a <code>float</code>
     */
    public static void setBallScale(float s) {
        ballScale = Math.abs(s);
    }

    /**
     * returns the appropriate image for this Atom with the specified position
     * relative to the origin and depth-cueing number
     *
     * @param bn the base image number from the depth-cueing array
     * @param r the atom's depth in pixels relative to the stereo origin; used
     *        to enhance depth cueing by slightly shrinking or expanding the
     *        image
     *
     * @return the appropriate Image for the depth-cueing number, depth, and
     *         internal atomic radius
     */
    public Image render(int bn, float r) {
        return render(bn, r, radius);
    }

    /**
     * returns the appropriate image for this Atom with the specified atomic
     * radius, position relative to the origin, and depth-cueing number
     *
     * @param bn the base image number from the depth-cueing array
     * @param r the atom's depth in pixels relative to the stereo origin; used
     *        to enhance depth cueing by slightly shrinking or expanding the
     *        image
     * @param rad the atomic radius of the atom
     *
     * @return the appropriate Image for the depth-cueing number, depth, and
     *         atomic radius
     */
    public Image render(int bn, float r, float rad) {
        if (balls == null) {
            Setup();
        }
        int size =
            Math.max((int) (rad * ballScale * (1f + (r / imagePositionPixels))),
                2);
        return balls[bn].getScaledInstance(size, -1, Image.SCALE_DEFAULT);
    }

    /**
     * Prepares this <code>Atom</code> for (re-)use by recalculating the images
     * for the various depth-cueing levels; normally invoked only on first use
     * and on each use immediately following a background color change
     */
    private void Setup() {
        float b;
        float d;
        float s;
        float s1;
        IndexColorModel model;
        balls = new Image[N_BALLS];
        byte[] red = new byte[256];
        red[0] = (byte) bgRed;
        byte[] green = new byte[256];
        green[0] = (byte) bgGreen;
        byte[] blue = new byte[256];
        blue[0] = (byte) bgBlue;
        byte[] alpha = new byte[256];
        alpha[0] = 0;
        for (int r = 0; r++ < N_BALLS;) {
            b = ((float) r) / N_BALLS;
            for (int i = maxr; i >= 1; --i) {
                d = ((float) i) / ((float) maxr);
                s1 = ((float) (i - 1)) / ((float) (maxr - 1));
                s = 1f - (0.25f * (s1 * s1 * s1 * s1));
                red[i] =
                    (byte) Blender.blend(Blender.blend(Blender.blend(baseRed,
                                0, s), 255, d), bgRed, b);
                green[i] =
                    (byte) Blender.blend(Blender.blend(Blender.blend(
                                baseGreen, 0, s), 255, d), bgGreen, b);
                blue[i] =
                    (byte) Blender.blend(Blender.blend(Blender.blend(baseBlue,
                                0, s), 255, d), bgBlue, b);
                alpha[i] = (byte) baseAlpha;
            }
            model = new IndexColorModel(8, maxr + 1, red, green, blue, alpha);
            balls[r - 1] = new BufferedImage(model, ras, false, null);
        }
    }
}
