/*
 * Reciprocal Net Project
 * 
 * @(#)Matrix3D.java
 *
 * Matrix3D is derived from copyrighted code licensed from Sun Microsystems.
 * The following copyright notice and license pertain:
 *
 * -- Begin copyright notice and license --
 * 
 * @(#)Matrix3D.java        1.3 97/07/28
 *
 * Copyright (c) 1995-1997 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * -- end copyright notice and license --
 *
 * 24-Oct-2002: jobollin made a few purely cosmetic modifications
 * 30-Oct-2002: jobollin made cosmetic changes
 * 20-Nov-2002: jobollin moved this class to org.recipnet.site.misc from *.jamm
 * 21-Feb-2003: jobollin reformatted the source and added additional javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.misc package to
 *              org.recipnet.common
 * 16-May-2005: ekoperda added nine-param constructor
 */

package org.recipnet.common;

/**
 * a class implementing a three-dimensional rotation/translation matrix.  The
 * intended use is to load the desired transformation into the Matrix3D by
 * applying a series of rotations, translations, and scalings, and perhaps by
 * right multiplying one Matrix3D by another Matrix3D.  The Matrix3D can be
 * used to apply its current transformation to a collection of 3D coordinates.
 * <p>
 * The rotation component of this object is stored as nine scalar fields.
 * These nine fields can be written in the form of a 3-by-3 matrix as:
 *  /  xx  yx  zx  \ 
 *  |  xy  yy  zy  |
 *  \  xz  yz  zz  /
 * .
 * <p>
 * This class is not thread-safe.  If it is accessed concurrently from multiple
 * threads then that access must be externally synchronized.
 * </p>
 */
public class Matrix3D
        implements Cloneable {

    /** The conversion factor from degrees to radians */
    public static final double RAD = Math.PI / 180d;

    /** The x translation component of this matrix */
    public float xo;

    /** the xx component of the rotation matrix */
    public float xx;

    /** the xy component of the rotation matrix */
    public float xy;

    /** the xz component of the rotation matrix */
    public float xz;

    /** The y translation component of this matrix */
    public float yo;

    /** the yx component of the rotation matrix */
    public float yx;

    /** the yy component of the rotation matrix */
    public float yy;

    /** the yz component of the rotation matrix */
    public float yz;

    /** The z translation component of this matrix */
    public float zo;

    /** the zx component of the rotation matrix */
    public float zx;

    /** the zy component of the rotation matrix */
    public float zy;

    /** the zz component of the rotation matrix */
    public float zz;

    /**
     * Create a new <code>Matrix3D</code> representing the identity operation
     */
    public Matrix3D() {
        unit();
    }

    /**
     * Constructor that takes the nine components of the rotation matrix as
     * arguments and sets the translation component to 0's.  Note that the
     * order of arguments mirrors the order of fields in this class; elements
     * are listed top-to-bottom columns with the order of columns going left to
     * right.
     */
    public Matrix3D(float xx, float xy, float xz, float yx, float yy, float yz,
            float zx, float zy, float zz) {
	this.xo = 0;
	this.xx = xx;
	this.xy = xy;
	this.xz = xz;
	this.yo = 0;
	this.yx = yx;
	this.yy = yy;
	this.yz = yz;
	this.zo = 0;
	this.zx = zx;
	this.zy = zy;
	this.zz = zz;
    }

    /**
     * Creates and returns a copy of this <code>Matrix3D</code>
     *
     * @return a copy of this <code>Matrix3D</code>
     */
    public Matrix3D copy() {
        Matrix3D c = null;
        try {
            c = (Matrix3D) clone();
        } catch (CloneNotSupportedException e) {
            /* this can never be thrown in a conforming JVM */
        }
        return c;
    }

    /**
     * Right multiplies this <code>Matrix3D</code> by <code>rhs</code>;
     * <i>i.e.</i> <strong>M</strong> = <strong>MR</strong>
     *
     * @param  rhs the <code>Matrix3D</code> factor by which to multiply this
     *         <code>Matrix3D</code>
     */
    public void mult(Matrix3D rhs) {
        float Nxx = (xx * rhs.xx) + (yx * rhs.xy) + (zx * rhs.xz);
        float Nxy = (xy * rhs.xx) + (yy * rhs.xy) + (zy * rhs.xz);
        float Nxz = (xz * rhs.xx) + (yz * rhs.xy) + (zz * rhs.xz);
        float Nxo = (xo * rhs.xx) + (yo * rhs.xy) + (zo * rhs.xz) + rhs.xo;

        float Nyx = (xx * rhs.yx) + (yx * rhs.yy) + (zx * rhs.yz);
        float Nyy = (xy * rhs.yx) + (yy * rhs.yy) + (zy * rhs.yz);
        float Nyz = (xz * rhs.yx) + (yz * rhs.yy) + (zz * rhs.yz);
        float Nyo = (xo * rhs.yx) + (yo * rhs.yy) + (zo * rhs.yz) + rhs.yo;

        float Nzx = (xx * rhs.zx) + (yx * rhs.zy) + (zx * rhs.zz);
        float Nzy = (xy * rhs.zx) + (yy * rhs.zy) + (zy * rhs.zz);
        float Nzz = (xz * rhs.zx) + (yz * rhs.zy) + (zz * rhs.zz);
        float Nzo = (xo * rhs.zx) + (yo * rhs.zy) + (zo * rhs.zz) + rhs.zo;

        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
        xo = Nxo;

        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        yo = Nyo;

        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
        zo = Nzo;
    }

    /**
     * Scales the matrix by f in all dimensions; equivalent to
     * <code>scale(f, f, f)</code>
     *
     * @param  f the scale factor as a <code>float</code>
     */
    public void scale(float f) {
        scale(f, f, f);
    }

    /**
     * Scales the matrix anisotropically, with one scale factor for each
     * dimension
     *
     * @param  xf the <code>float</code> scale factor for the x dimension
     * @param  yf the <code>float</code> scale factor for the y dimension
     * @param  zf the <code>float</code> scale factor for the z dimension
     */
    public void scale(float xf, float yf, float zf) {
        xx *= xf;
        xy *= xf;
        xz *= xf;
        xo *= xf;
        yx *= yf;
        yy *= yf;
        yz *= yf;
        yo *= yf;
        zx *= zf;
        zy *= zf;
        zz *= zf;
        zo *= zf;
    }

    /**
     * Returns a <code>String</code> representation of this
     * <code>Matrix3D</code> showing the current values of all the metrix
     * elements
     *
     * @return a <code>String</code> representation of this
     * <code>Matrix3D</code>
     */
    public String toString() {
        return ("[" + xx + "," + xy + "," + xz + "," + xo + ";" + yx + "," + yy
        + "," + yz + "," + yo + ";" + zx + "," + zy + "," + zz + "," + zo + "]"
        );
    }

    /**
     * Transforms <code>nvert</code> points from <code>v</code> and puts the
     * results into the corresponding elements of <code>tv</code>.
     * <code>tv</code> must have enough space to store all the results.
     *
     * @param  v a <code>float[]</code> containing the coordinates to transform;
     *         each three successive positions constitute a coordinate triple
     * @param  tv a <code>float[]</code> into which to store the transformed
     *         coordinates; each three successive positions constitute a
     *         coordinate triple
     * @param  nvert the number of coordinate triples to transform; must not
     *         exceed <code>v.length / 3</code> or <code>tv.length / 3</code>
     */
    public void transform(float[] v, float[] tv, int nvert) {
        for (int i = nvert * 3; (i -= 3) >= 0;) {
            float x = v[i];
            float y = v[i + 1];
            float z = v[i + 2];
            tv[i] = ((x * xx) + (y * xy) + (z * xz) + xo);
            tv[i + 1] = ((x * yx) + (y * yy) + (z * yz) + yo);
            tv[i + 2] = ((x * zx) + (y * zy) + (z * zz) + zo);
        }
    }

    /**
     * Transforms <code>nvert</code> points from <code>v</code> and puts the
     * results into the corresponding elements of <code>tv</code>.
     * <code>tv</code> must have enough space to store all the results.
     *
     * @param  v a <code>float[]</code> containing the coordinates to transform;
     *         each three successive positions constitute a coordinate triple
     * @param  tv an <code>int[]</code> into which to store the transformed
     *         coordinates; each three successive positions constitute a
     *         coordinate triple, and the results are subject to standard Java
     *         assignment conversion from <code>float</code> to <code>int</code>
     * @param  nvert the number of coordinate triples to transform; must not
     *         exceed <code>v.length / 3</code> or <code>tv.length / 3</code>
     */
    public void transform(float[] v, int[] tv, int nvert) {
        for (int i = nvert * 3; (i -= 3) >= 0;) {
            float x = v[i];
            float y = v[i + 1];
            float z = v[i + 2];
            tv[i] = (int) ((x * xx) + (y * xy) + (z * xz) + xo);
            tv[i + 1] = (int) ((x * yx) + (y * yy) + (z * yz) + yo);
            tv[i + 2] = (int) ((x * zx) + (y * zy) + (z * zz) + zo);
        }
    }

    /**
     * Transforms <code>nvert</code> points from <code>v</code> and puts the
     * results into the corresponding elements of <code>tv</code>.
     * <code>tv</code> must have enough space to store all the results.
     *
     * @param  v a <code>float[][]</code> containing the coordinates to
     *         transform; each (<code>v[i][0], v[i][1], v[i][2]</code>)
     *         constitutes a coordinate triple
     * @param  tv an <code>int[][]</code> into which to store the transformed
     *         coordinates; the coordinates are arranged in <code>tv</code>
     *         in a manner parallel to the arrangement of <code>v</code>, and
     *         the results are subject to standard Java assignment conversion
     *         from <code>float</code> to <code>int</code>
     * @param  nvert the number of coordinate triples to transform; must not
     *         exceed <code>v.length</code> or <code>tv.length</code>
     */
    public void transform(float[][] v, int[][] tv, int nvert) {
        for (int i = nvert; --i >= 0;) {
            float x = v[i][0];
            float y = v[i][1];
            float z = v[i][2];
            tv[i][0] = (int) ((x * xx) + (y * xy) + (z * xz) + xo);
            tv[i][1] = (int) ((x * yx) + (y * yy) + (z * yz) + yo);
            tv[i][2] = (int) ((x * zx) + (y * zy) + (z * zz) + zo);
        }
    }

    /**
     * Transforms <code>nvert</code> points from <code>v</code> and puts the
     * results into the corresponding elements of <code>tv</code>.
     * <code>tv</code> must have enough space to store all the results.
     *
     * @param  v a <code>float[][]</code> containing the coordinates to
     *         transform; each (<code>v[i][0], v[i][1], v[i][2]</code>)
     *         constitutes a coordinate triple
     * @param  tv a <code>float[][]</code> into which to store the transformed
     *         coordinates; the coordinates are arranged in <code>tv</code>
     *         in a manner parallel to the arrangement of <code>v</code>
     * @param  nvert the number of coordinate triples to transform; must not
     *         exceed <code>v.length</code> or <code>tv.length</code>
     */
    public void transform(float[][] v, float[][] tv, int nvert) {
        for (int i = nvert; --i >= 0;) {
            float x = v[i][0];
            float y = v[i][1];
            float z = v[i][2];
            tv[i][0] = ((x * xx) + (y * xy) + (z * xz) + xo);
            tv[i][1] = ((x * yx) + (y * yy) + (z * yz) + yo);
            tv[i][2] = ((x * zx) + (y * zy) + (z * zz) + zo);
        }
    }

    /**
     * Reinitializes this <code>Matrix3D</code> to represent the identity
     * operation
     */
    public void unit() {
        xo = 0f;
        xx = 1f;
        xy = 0f;
        xz = 0f;
        yo = 0f;
        yx = 0f;
        yy = 1f;
        yz = 0f;
        zo = 0f;
        zx = 0f;
        zy = 0f;
        zz = 1f;
    }

    /**
     * Applies a translation operation to the matrix
     *
     * @param  x the x coordinate of the translation vector
     * @param  y the y coordinate of the translation vector
     * @param  z the z coordinate of the translation vector
     */
    public void translate(float x, float y, float z) {
        xo += x;
        yo += y;
        zo += z;
    }

    /**
     * Applies a rotation of <code>theta</code> degrees about the x axis to this
     * <code>Matrix3D</code>
     *
     * @param  theta the rotation angle in degrees
     */
    public void xrot(double theta) {
        theta *= RAD;
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nyx = (float) ((yx * ct) + (zx * st));
        float Nyy = (float) ((yy * ct) + (zy * st));
        float Nyz = (float) ((yz * ct) + (zz * st));
        float Nyo = (float) ((yo * ct) + (zo * st));

        float Nzx = (float) ((zx * ct) - (yx * st));
        float Nzy = (float) ((zy * ct) - (yy * st));
        float Nzz = (float) ((zz * ct) - (yz * st));
        float Nzo = (float) ((zo * ct) - (yo * st));

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /**
     * Applies a rotation of <code>theta</code> degrees about the y axis to this
     * <code>Matrix3D</code>
     *
     * @param  theta the rotation angle in degrees
     */
    public void yrot(double theta) {
        theta *= RAD;
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nxx = (float) ((xx * ct) + (zx * st));
        float Nxy = (float) ((xy * ct) + (zy * st));
        float Nxz = (float) ((xz * ct) + (zz * st));
        float Nxo = (float) ((xo * ct) + (zo * st));

        float Nzx = (float) ((zx * ct) - (xx * st));
        float Nzy = (float) ((zy * ct) - (xy * st));
        float Nzz = (float) ((zz * ct) - (xz * st));
        float Nzo = (float) ((zo * ct) - (xo * st));

        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /**
     * Applies a rotation of <code>theta</code> degrees about the z axis to this
     * <code>Matrix3D</code>
     *
     * @param  theta the rotation angle in degrees
     */
    public void zrot(double theta) {
        theta *= RAD;
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        float Nyx = (float) ((yx * ct) + (xx * st));
        float Nyy = (float) ((yy * ct) + (xy * st));
        float Nyz = (float) ((yz * ct) + (xz * st));
        float Nyo = (float) ((yo * ct) + (xo * st));

        float Nxx = (float) ((xx * ct) - (yx * st));
        float Nxy = (float) ((xy * ct) - (yy * st));
        float Nxz = (float) ((xz * ct) - (yz * st));
        float Nxo = (float) ((xo * ct) - (yo * st));

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
    }
}
