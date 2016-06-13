/*
 * Reciprocal Net Project
 * 
 * @(#) Model3D.java
 * 
 * 20-Nov-2002: jobollin changed the import statement for Matrix3D
 * 13-Dec-2002: jobollin corrected the faulty Zsort method (Task #651)
 * 09-Jan-2003: jobollin cleaned up unused variables
 * 24-Feb-2003: jobollin made the applet field non-static
 * 24-Feb-2003: jobollin reformatted the source as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm1 package 
 *              to org.recipnet.site.applet.jamm.jamm1; change package
 *              references to match source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm1;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import org.recipnet.common.Matrix3D;

/**
 * A class representing a three dimensional object as a collection of vertices
 * and connections among them.
 */
class Model3D {

    /** DOCUMENT ME! */
    private Applet applet; //this is used to pass the applet as an argument
    static Color[] gr; //This is the color of the wireframe, used for depth queing

    /** DOCUMENT ME! */
    Image ball;

    /** DOCUMENT ME! */
    Image bball;

    /** DOCUMENT ME! */
    Image blball;

    /** DOCUMENT ME! */
    Image cpball;

    /** DOCUMENT ME! */
    Image crball;

    /** DOCUMENT ME! */
    Image gball;

    /** DOCUMENT ME! */
    Image goldball;

   /** DOCUMENT ME! */
    Image rball;

   /** DOCUMENT ME! */
    Image wball;

    /** DOCUMENT ME! */
    Image yball;

    /** DOCUMENT ME! */
    Matrix3D mat; //this holds the matrix transformations for the molecule,
                  // as well as the stereo view

    /** DOCUMENT ME! */
    Matrix3D stereomat; //this holds the matrix transformations for the molecule,
                        // as well as the stereo view

    /** DOCUMENT ME! */
    int[] AtomicNum; //Atomic number of each atom

    /** DOCUMENT ME! */
    String[] Comp; //Compound name of molecule

    /** DOCUMENT ME! */
    int[] Zmap; //map the z values of each atom, so the spheres will overlap correctly

    /** DOCUMENT ME! */
    int[] con; //connections (or bonds)

    /** DOCUMENT ME! */
    int[] tvert; //Transformed vertices of the molecule

    /** DOCUMENT ME! */
    float[] vert; //verticies of the molecule

    /** DOCUMENT ME! */
    boolean transformed; //is the model transformed yet?

    /** DOCUMENT ME! */
    float xmax; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    float xmin; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    float ymax; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    float ymin; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    float zmax; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    float zmin; // limiting values of the model coordinates

    /** DOCUMENT ME! */
    int maxcon; //Number of connections and connection array size

    /** DOCUMENT ME! */
    int maxvert; //number of vertices and vertex array size

    /** DOCUMENT ME! */
    int ncon; //Number of connections and connection array size

    /** DOCUMENT ME! */
    int nvert; //number of vertices and vertex array size

    /**
     * Constructs a new <code>Model3D</code>
     */
    Model3D() {
        mat = new Matrix3D();
        stereomat = new Matrix3D();
    }

    /**
     * Finds the distance between two points in 3D space
     *
     * @param  p1 DOCUMENT ME!
     * @param  p2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double findDistance(int p1, int p2) {
        float dx = vert[p1] - vert[p2];
        float dy = vert[p1 + 1] - vert[p2 + 1];
        float dz = vert[p1 + 2] - vert[p2 + 2];
        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }

    /**
     * Finds the closest transformed point in the model; used for mouse
     * input when determining bond distance and angles
     *
     * @param  x DOCUMENT ME!
     * @param  y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int findPoint(int x, int y) {
        int d2;
        int min = 2000000000;
        int point = 0;
        int dx;
        int dy;
        for (int i = nvert * 3; (i -= 3) >= 0;) {
            dx = x - tvert[i];
            dy = y - tvert[i + 1];
            d2 = (dx * dx) + (dy * dy);
            if (d2 < min) {
                min = d2;
                point = i;
            }
        }
        return point;
    }

    void SetApplet(Applet app) {
        applet = app;
    }

    /**
     * Adds a line from vertex p1 to vertex p2
     *
     * @param  p1 DOCUMENT ME!
     * @param  p2 DOCUMENT ME!
     */
    void addLine(int p1, int p2) {
        int i = ncon;
        if ((p1 >= nvert) || (p2 >= nvert)) {
            return;
        }
        if (i >= maxcon) {
            if (con == null) {
                maxcon = 100;
                con = new int[maxcon];
            } else {
                maxcon *= 2;
                int[] nv = new int[maxcon];
                System.arraycopy(con, 0, nv, 0, con.length);
                con = nv;
            }
        }
        if (p1 > p2) {
            int t = p1;
            p1 = p2;
            p2 = t;
        }
        con[i] = (p1 << 16) | p2;
        ncon = i + 1;
    }

    /**
     * Adds a vertex to this model
     *
     * @param  name DOCUMENT ME!
     * @param  x DOCUMENT ME!
     * @param  y DOCUMENT ME!
     * @param  z DOCUMENT ME!
     * @param  num DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int addVert(String name, float x, float y, float z, int num) {
        int i = nvert;

        if (i >= maxvert) {
            if (vert == null) {
                maxvert = 100;
                vert = new float[maxvert * 3];
                Comp = new String[maxvert];
                AtomicNum = new int[maxvert];
            } else {
                maxvert *= 2;
                float[] nv = new float[maxvert * 3];
                String[] nn = new String[maxvert];
                int[] an = new int[maxvert];
                System.arraycopy(Comp, 0, nn, 0, Comp.length);
                System.arraycopy(vert, 0, nv, 0, vert.length);
                System.arraycopy(AtomicNum, 0, an, 0, AtomicNum.length);
                AtomicNum = an;
                vert = nv;
                Comp = nn;
            }
        }
        i *= 3;
        vert[i] = x;
        vert[i + 1] = y;
        vert[i + 2] = z;
        Comp[nvert] = name;
        AtomicNum[nvert] = num;
        return nvert++;
    }

    /**
     * Finds the bounding box of this model
     */
    void findBB() {
        if (nvert <= 0) {
            return;
        }
        float[] v = vert;
        float xmin = v[0];
        float xmax = xmin;
        float ymin = v[1];
        float ymax = ymin;
        float zmin = v[2];
        float zmax = zmin;
        float t;
        for (int i = nvert * 3; (i -= 3) > 0;) {
            t = v[i];
            if (t < xmin) {
                xmin = t;
            } else if (t > xmax) {
                xmax = t;
            }
            t = v[i + 1];
            if (t < ymin) {
                ymin = t;
            } else if (t > ymax) {
                ymax = t;
            }
            t = v[i + 2];
            if (t < zmin) {
                zmin = t;
            } else if (t > zmax) {
                zmax = t;
            }
        }
        this.xmax = xmax;
        this.xmin = xmin;
        this.ymax = ymax;
        this.ymin = ymin;
        this.zmax = zmax;
        this.zmin = zmin;
    }

    /**
     * Paints this model with the use of the provided graphics context and
     * in a manner controlled by the various flags
     *
     * @param  g DOCUMENT ME!
     * @param  label DOCUMENT ME!
     * @param  hydro DOCUMENT ME!
     * @param  balls DOCUMENT ME!
     * @param  stereo DOCUMENT ME!
     * @param  RG DOCUMENT ME!
     * @param  space DOCUMENT ME!
     * @param  scale DOCUMENT ME!
     */
    void paint(Graphics g, boolean label, boolean hydro, boolean balls,
        boolean stereo, boolean RG, boolean space, double scale) {
        int[] stereov = null;
        if ((vert == null) || (nvert <= 0)) {
            return;
        }
        transform();
        if (gr == null) {
            gr = new Color[16];
            for (int i = 0; i < 16; i++) {
                float grey =
                    (float) (0.66666666667 * (1d - Math.pow(i / 15.0, 2.3)));
                gr[i] = new Color(grey, grey, grey);
            }
        }

        int lim = ncon;
        int[] c = con;
        int[] v = tvert;

        if (RG || stereo) {
            stereov = new int[nvert * 3];
            System.arraycopy(tvert, 0, stereov, 0, tvert.length);
            stereomat.transform(vert, stereov, nvert);
        }

        if (!space) {
            int T;
            int p1;
            int p2;
            int grey;
            for (int i = 0; i < lim; i++) {
                T = c[i];
                p1 = ((T >> 16) & 0xFFFF) * 3;
                p2 = (T & 0xFFFF) * 3;
                if (RG) {
                    g.setColor(Color.green);
                } else {
                    grey = v[p1 + 2] + v[p2 + 2];
                    if (grey < 0) {
                        grey = 0;
                    }
                    if (grey > 15) {
                        grey = 15;
                    }
                    g.setColor(gr[grey]);
                }

                g.drawLine(v[p1], v[p1 + 1], v[p2], v[p2 + 1]);
                if (label) {
                    if ((AtomicNum[p1 / 3] != 1) || hydro) {
                        g.drawString(Comp[p1 / 3], v[p1], v[p1 + 1]);
                    }
                    if ((AtomicNum[p2 / 3] != 1) || hydro) {
                        g.drawString(Comp[p2 / 3], v[p2], v[p2 + 1]);
                    }
                }

                if (RG || stereo) {
                    if (RG) {
                        g.setColor(Color.red);
                    }

                    g.drawLine(stereov[p1], stereov[p1 + 1], stereov[p2],
                        stereov[p2 + 1]);
                    if (label) {
                        if ((AtomicNum[p1 / 3] != 1) || hydro) {
                            g.drawString(Comp[p1 / 3], stereov[p1],
                                stereov[p1 + 1]);
                        }
                        if ((AtomicNum[p2 / 3] != 1) || hydro) {
                            g.drawString(Comp[p2 / 3], stereov[p2],
                                stereov[p2 + 1]);
                        }
                    }
                }
            }
        }

        if (balls) {
            int j;
            int n;
            int size;
            Image ball;
            Zsort();
            for (int i = 0; i < nvert; i++) {
                j = Zmap[i];
                n = AtomicNum[j / 3];
                size = (int) Math.round((Math.min(n + 10, 30) * scale));
                if (space) {
                    size *= 4;
                }
                if ((n != 1) || hydro) {
                    ball = Ball(n);
                    if (stereo) {
                        g.drawImage(ball, stereov[j] - (size / 2),
                            stereov[j + 1] - (size / 2), size, size, applet);
                    }
                    g.drawImage(ball, v[j] - (size / 2), v[j + 1] - (size / 2),
                        size, size, applet);
                }
            }
        }
    }

    /**
     * Transforms all the points in this model according to the current matrix
     */
    void transform() {
        if (transformed || (nvert <= 0)) {
            return;
        }
        if ((tvert == null) || (tvert.length < (nvert * 3))) {
            tvert = new int[nvert * 3];
        }
        mat.transform(vert, tvert, nvert);
        transformed = true;
    }

    /**
     * Determines which ball to use for an atom of the specified type
     *
     * @param  num DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Image Ball(int num) {
        switch (num) {
        case 1:
            return (wball);
        case 6:
            return (blball);
        case 7:
            return (bball);
        case 8:
            return (rball);
        case 9:
            return (gball);
        case 16:
            return (yball);
        case 17:
            return (gball);
        case 29:
            return (cpball);
        case 79:
            return (goldball);
        default:
            return (crball);
        }
    }

    /**
     * Sorts the z values for each point after transformation, so that when the
     * balls are being displayed, the farthest away can be drawn first
     */
    private void Zsort() {
        int[] zs = Zmap;
        int[] v = tvert;
        int i;
        int j;
        int k;
        int mx;
        int mn;
        int imn;
        int imx;
        int n;
        if (zs == null) {
            Zmap = zs = new int[nvert];
            for (i = 0; i < nvert; i++) {
                zs[i] = i * 3;
            }
        }

        for (i = 0, j = nvert - 1; i < j; i++, j--) {
            imx = imn = j;
            mx = mn = v[zs[j] + 2];
            for (k = i; k < j; k++) {
                n = v[zs[k] + 2];
                if (n < mn) {
                    mn = n;
                    imn = k;
                } else if (n > mx) {
                    mx = n;
                    imx = k;
                }
            }
            if (mx == mn) {
                break;
            }
            if (imn != i) {
                k = zs[imn];
                zs[imn] = zs[i];
                zs[i] = k;
                if (imx == i) {
                    imx = imn;
                }
            }
            if (imx != j) {
                k = zs[imx];
                zs[imx] = zs[j];
                zs[j] = k;
            }
        }
    }
}
