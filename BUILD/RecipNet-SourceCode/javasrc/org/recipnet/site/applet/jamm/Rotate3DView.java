/*
 * Reciprocal Net Project
 *
 * @(#) Rotate3DView.java
 *
 * 20-Nov-2002: jobollin added an import for Rotate3DModel
 * 24-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm; changed package references to
 *              match source tree reorganization
 */

package org.recipnet.site.applet.jamm;
import java.awt.Color;
import java.awt.Graphics;
import org.recipnet.common.Matrix3D;
import org.recipnet.site.applet.jamm.Rotate3DModel;

/**
 * A view of a <code>Rotate3DModel</code>; the two classes require a third to
 * act as the controller to form a complete model-view-controller architecture.
 */
public class Rotate3DView {

    /** the default model height, in pixels */
    private static final int DEFAULT_HEIGHT = 200;

    /** the default model width, in pixels */
    private static final int DEFAULT_WIDTH = 200;

    /** the conversion factor from radians to degrees */
    private static double R = 180.0d / 3.14159265d;

    /** the current model orientation represented by this view */
    protected Matrix3D orien;

    /** a transformation matrix with which to modify the current orientation */
    protected Matrix3D tMat;

    /** a reference to the model of which this is a view */
    protected Rotate3DModel mod;

    /** an array containing the vertex colors used by this view */
    protected Color[] colors;

    /** an array containing the atomic radii used by this view */
    protected float[] radii;

    /**
     * the coordinates of the vertices of the model, transformed according to
     * the current viewing orientation
     */
    protected int[][] tvert;

    /** A flag indicating whether or not to display vertices of type 1 */
    protected boolean hydro = false;

    /** A flag indicating whether or not to display vertex labels */
    protected boolean labels = false;

    /**
     * A flag indicating whether or not <code>tvert</code> is up to date
     * with respect to the current orientation
     */
    protected boolean transformed;

    /** The initial, automatically calculated, scale factor */
    protected float initialScale;

    /** The current scale factor */
    protected float scale;

    /** The diameter of the model representation, in pixels */
    protected float scaledDiameter;

    /** The height of the view, in pixels */
    protected int winHeight;

    /** the width of the view, in pixels */
    protected int winWidth;

    /**
     * Constructs a new <code>Rotate3DView</code>
     */
    public Rotate3DView() {
        this(null, null);
    }

    /**
     * Constructs a new <code>Rotate3DView</code> with the specified model and
     * element data tables
     *
     * @param  m a <code>Rotate3DModel</code> containing the model
     * @param  tc a <code>TableContainer</code> containing the element data
     */
    public Rotate3DView(Rotate3DModel m, TableContainer tc) {
        orien = new Matrix3D();
        tMat = new Matrix3D();
        transformed = false;
        scaledDiameter = 1.0f;
        initialScale = 1.0f;
        scale = 1.0f;
        winWidth = DEFAULT_WIDTH;
        winHeight = DEFAULT_HEIGHT;
        setModel(m);
        if (tc == null) {
            colors = new Color[100];
            for (int i = 0; i < 100; i++) {
                colors[i] = Color.black;
            }
            radii = new float[100];
            for (int i = 0; i < 100; i++) {
                radii[i] = 0.85f;
            }
        } else {
            setColors(tc.colorTab);
            setRadii(tc.radii);
        }
    }

    /**
     * Calculates and returns the orientation angles corresponding to this view
     *
     * @return a <code>double</code> containing the three orientation angles
     */
    public double[] getAngles() {
        double[] rval = new double[3];
        double cosy;
        double sinx;
        rval[0] = Math.atan2(orien.zy, orien.zz);
        sinx = Math.sin(rval[0]);
        if (Math.abs(sinx) <= .70711) {
            cosy = orien.zz / Math.cos(rval[0]);
        } else {
            cosy = orien.zy / sinx;
        }
        rval[0] *= R;
        rval[1] = R * Math.atan2(-orien.zx, cosy);
        rval[2] = R * Math.atan2(orien.yx, orien.xx);
        return rval;
    }

    /**
     * Sets the colors to use for this view
     *
     * @param  c a <code>Color[]</code> containing the colors to use for each
     *         vertex type
     */
    public void setColors(Color[] c) {
        if (c != null) {
            colors = new Color[c.length];
            System.arraycopy(c, 0, colors, 0, c.length);
        }
    }

    /**
     * Sets the height of the view
     *
     * @param  h the new height, in pixels
     */
    public void setHeight(int h) {
        if (h > 0) {
            winHeight = h;
            rescale();
        }
    }

    /**
     * Sets the initial scale factor for this view
     *
     * @param  s the initial scale factor
     */
    public void setInitialScale(float s) {
        if (s != 0f) {
            initialScale = s;
        }
    }

    /**
     * Sets the model for this view to display
     *
     * @param  m the <code>Rotate3DModel</code> displayed by this view
     */
    public void setModel(Rotate3DModel m) {
        if (m != null) {
            mod = m;
            transformed = false;
            rescale();
        }
    }

    /**
     * Sets the radius table used by this view
     *
     * @param  r a <code>float[]</code> containing the vertex radii, by vertex
     *         type
     */
    public void setRadii(float[] r) {
        if (r != null) {
            radii = new float[r.length];
            System.arraycopy(r, 0, radii, 0, r.length);
        }
    }

    /**
     * Sets the current scale factor for this view
     *
     * @param  s the scale factor as a <code>float</code>
     */
    public void setScale(float s) {
        if (s != 0f) {
            scale = s;
            transformed = false;
        }
    }

    /**
     * Returns the current scale factor
     *
     * @return the current scale factor as a <code>float</code>
     */
    public float getScale() {
        return scale;
    }

    /**
     * Sets the color and radius tables from the supplied
     * <code>TableContainer</code>
     *
     * @param  tc a <code>TableContainer</code> containing the new color and
     *         radius containers
     */
    public void setTable(TableContainer tc) {
        setColors(tc.colorTab);
        setRadii(tc.radii);
    }

    /**
     * Sets the width of the view
     *
     * @param  w the new width, in pixels
     */
    public void setWidth(int w) {
        if (w > 0) {
            winWidth = w;
            rescale();
        }
    }

    /**
     * Renders the model according to this view
     *
     * @param  g the <code>Graphics</code> with which to render the model
     * @param  back the background <code>Color</code> on which to render the
     *         model
     * @param  text the text <code>Color</code> to use
     */
    public void paint(Graphics g, Color back, Color text) {
        if (mod == null) {
            return;
        }
        if (!transformed) {
            tMat.unit();
            tMat.translate(-mod.getCenterX(), -mod.getCenterY(),
                -mod.getCenterZ());
            tMat.mult(orien);
            tMat.scale(scale, -scale, scale);
            tMat.translate(winWidth / 2, winHeight / 2, 0);
            transform();
        }
        int[][] c = mod.getConnections();
        int nc = mod.getNumConnections();
        int[] types = mod.getPointTypes();
        scaledDiameter = 2 * mod.getRadius() * scale;

        for (int i = 0; i < nc; i++) {
            int i1 = c[i][0];
            int i2 = c[i][1];
            int midx;
            int midy;
            float z1;
            float z2;

            z1 = ((3 * tvert[i1][2]) + tvert[i2][2]) / 4f;
            z2 = (tvert[i1][2] + (3 * tvert[i2][2])) / 4f;

            midx = (tvert[i1][0] + tvert[i2][0]) / 2;
            midy = (tvert[i1][1] + tvert[i2][1]) / 2;

            g.setColor(Blender.blend(colors[types[i1]], back, cueLevel(z1)));
            g.drawLine(tvert[i1][0], tvert[i1][1], midx, midy);

            g.setColor(Blender.blend(colors[types[i2]], back, cueLevel(z2)));
            g.drawLine(tvert[i2][0], tvert[i2][1], midx, midy);

        }
        if (labels) {
            g.setColor(text);
            String[] labelText = mod.getPointLabels();
            int np = mod.getNumPoints();
            for (int j = 0; j < np; j++) {
                if ((types[j] != 1) || hydro) {
                    g.drawString(labelText[j], tvert[j][0], tvert[j][1]);
                }
            }
        }
    }

    /**
     * Computes a new scale factor with which the whole model will be visible
     * in the view area
     */
    public void rescale() {
        int dim = Math.min(winWidth, winHeight);
        if (mod != null) {
            scale = (0.4f * initialScale * dim) / mod.getRadius();
            transformed = false;
        }
    }

    /**
     * Resets the view to its initial orientation and scale factor
     */
    public void reset() {
        orien.unit();
        rescale();
        transformed = false;
    }

    /**
     * Toggles the display of vertices of type 1
     */
    public void toggleHydro() {
        hydro = !hydro;
    }

    /**
     * Toggles the display of vertex labels
     */
    public void toggleLabels() {
        labels = !labels;
    }

    /**
     * Rotates the view orientation around the <strong>x</strong> (horizontal,
     * in plane) axis
     *
     * @param  theta the rotation angle, in degrees
     */
    public void xRot(double theta) {
        orien.xrot(theta);
        transformed = false;
    }

    /**
     * Rotates the view orientation around the <strong>y</strong> (vertical,
     * in plane) axis
     *
     * @param  theta the rotation angle, in degrees
     */
    public void yRot(double theta) {
        orien.yrot(theta);
        transformed = false;
    }

    /**
     * Rotates the view orientation around the <strong>x</strong> (horizontal,
     * perpendicular) axis
     *
     * @param  theta the rotation angle, in degrees
     */
    public void zRot(double theta) {
        orien.zrot(theta);
        transformed = false;
    }

    /**
     * Calculates a multiplicative factor controlling how much a vertex with
     * the specified <strong>z</strong> coordinate should be blended into the
     * background for depth-cueing purposes
     *
     * @param  z the z coordinate for which depth-cueing information is
     *         desired
     *
     * @return the blending factor as a <code>float</code>
     */
    protected float cueLevel(float z) {
        return (0.0199999f + (0.98f * ((z / scaledDiameter) + 0.5f)));
    }

    /**
     * Calculates the transformed coordinates of the model
     */
    protected void transform() {
        if (mod == null) {
            return;
        }
        int n = mod.getNumPoints();
        if ((tvert == null) || (tvert.length < n)) {
            tvert = new int[n][3];
        }
        tMat.transform(mod.getPoints(), tvert, n);
        transformed = true;
    }
}
