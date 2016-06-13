/*
 * Reciprocal Net Project
 *
 * LineInputMaker.java
 *
 * 24-Oct-2002: jobollin wrote first draft
 * 09-Jan-2003: jobollin removed unused imports
 * 21-Feb-2003: jobollin made consistent with changes to Rotate3DModel applied
 *              as part of task #682
 * 20-Mar-2003: jobollin made makeLinePostscript throw exceptions instead of
 *              eating them, and no longer throw any IOExceptions, as part of
 *              task #629
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper; made class and most
 *              methods public
 * 09-Jan-2004: ekoperda modified makeLinePostscript(), removed 
 *              getAtomCoordinates(), and added projectAtomsOntoPlane() in
 *              order to utilize new CrtFile class
 * 07-Nov-2005: jobollin updated this class for compatibility with changes to
 *              CrtFile; formatted the source according to typical conventions
 *              (especially tab removal); updated the source to use Java 5
 *              enhanced for loops; made makeLinePostscript() take a
 *              MolecularModel instead of raw CRT data; accommodated API changes
 *              to the ImageParameters class; removed unused imports
 * 25-May-2006: jobollin updated docs
 */

package org.recipnet.site.wrapper;

import java.util.HashMap;
import java.util.Map;

import org.recipnet.common.Matrix3D;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;

/**
 * A utility class for constructing Postscript renditions of wireframe-style
 * molecular model drawings
 * 
 * @author jobollin
 * @version 1.0
 */
public abstract class LinePostscriptMaker {

    /**
     * The offset, in characters, of the position in the output Postscript code
     * where the BoundingBox directive should be inserted if desired
     */
    public static final int BB_OFFSET;

    private static final int FONT_SIZE_OFFSET;

    private static final String POSTSCRIPT_TOP;

    private static final String POSTSCRIPT_BOTTOM;

    static {
        StringBuilder sb = new StringBuilder();

        sb.append("%!PS-Adobe-3.0 EPSF-3.0\n");
        sb.append("%%DocumentData: Clean7Bit\n");
        sb.append("%%LanguageLevel: 1\n");
        sb.append("%%Creator: ReciprocalNet\n");

        BB_OFFSET = sb.length();

        sb.append("%%Pages: 1\n");
        sb.append("%%Orientation: Portrait\n");
        sb.append("%%EndComments\n");
        sb.append("%%Page: 1 1\n");
        sb.append("%%BeginPageSetup\n");
        sb.append("save\n");
        sb.append("newpath\n");
        sb.append("/Helvetica findfont ");

        FONT_SIZE_OFFSET = sb.length();

        sb.append(" scalefont setfont\n");
        sb.append("0.1 setlinewidth\n");
        sb.append("/m {moveto} def\n");
        sb.append("/l {lineto} def\n");
        sb.append("/s {show} def\n");
        sb.append("%%EndPageSetup\n");

        POSTSCRIPT_TOP = sb.toString();

        sb.setLength(0);
        sb.append("stroke\n");
        sb.append("restore\n");
        sb.append("showpage\n");
        sb.append("%%Trailer\n");
        sb.append("%%EOF\n");

        POSTSCRIPT_BOTTOM = sb.toString();
    }

    /**
     * Generates a PostScript file containing an image that is a "line drawing"
     * of a specific molecule in a specific orientation. The drawing consists of
     * a bunch of lines representing bonds within the molecule, with textual
     * labels at each vertex. The caller describes the molecule to be drawn by
     * supplying the data from its {@code .crt} file. See the {@code CrtFile}
     * class for a detailed explanation of this file format.
     * 
     * @param <A> the atom type characterizing the provided molecular model;
     *        normally inferred from the model argument
     * @param parameters values within this object identify the projection
     *        computation that is to be performed on the 3-D model of atoms. The
     *        values of {@code parameters.x}, {@code parameters.y}, and
     *        {@code parameters.z} describe how many degrees the model should be
     *        rotated about its x-axis, y-axis, and z-axis, respectively, prior
     *        to projection. The value of {@code parameters.scale} allows the
     *        model's size to be adjusted by an arbitrary constant prior to
     *        projection. Hydrogen atoms (atomicNumber==1) are omitted from the
     *        diagram unless {@code parameters.includeHydrogen} is {@code true}.
     *        Any other values within the {@code parameters} object are ignored.
     * @param model a {@code MolecularModel&lt;? extends Atom, ? extends
     *        Bond&gt;} representing the molecule to be drawn.
     * @return PostScript data that contains the generated image.
     */
    public static <A extends Atom> String makeLinePostscript(
            ImageParameters parameters, MolecularModel<A, Bond<A>> model) {
        float[][] planeCoordinates;
        Map<Atom, Integer> atomIndices = new HashMap<Atom, Integer>();
        int index = 0;

        for (Atom a : model.getAtoms()) {
            atomIndices.put(a, index);
            index++;
        }

        /*
         * Project all the atoms in the model onto a plane that we can render,
         * according to the viewpoint specified by the caller.
         */
        planeCoordinates = projectAtomsOntoPlane(model, parameters);

        // Write our standard header to the PostScript file.
        StringBuilder postScript = new StringBuilder();
        postScript.append(POSTSCRIPT_TOP);
        float fontSize = (float) (0.3d * parameters.getModelScaleFactor());
        postScript.insert(FONT_SIZE_OFFSET, fontSize);

        /*
         * Iterate through all the bonds described by the .crt file. Draw a line
         * to represent each of them, possibly omitting the bonds to hydrogen
         * atoms.
         */
        for (Bond<? extends Atom> bond : model.getBonds()) {
            if (parameters.shouldIncludeHydrogen() || !bond.includesHydrogen()) {
                float[] coords;

                coords = planeCoordinates[atomIndices.get(bond.getAtom1())];
                postScript.append(coords[0]);
                postScript.append(' ');
                postScript.append(coords[1]);
                postScript.append(" m\n");

                coords = planeCoordinates[atomIndices.get(bond.getAtom2())];
                postScript.append(coords[0]);
                postScript.append(' ');
                postScript.append(coords[1]);
                postScript.append(" l\n");
            }
        }

        /*
         * Iterate through all the atoms described by the .crt file. Draw a
         * label to represent each of them, possibly omitting the hydrogens.
         */
        for (Atom atom : model.getAtoms()) {
            if (parameters.shouldIncludeHydrogen() || !atom.isHydrogen()) {
                float[] coords = planeCoordinates[atomIndices.get(atom)];

                postScript.append(coords[0]);
                postScript.append(' ');
                postScript.append(coords[1]);
                postScript.append(" m\n");

                postScript.append('(');
                postScript.append(atom.getLabel());
                postScript.append(") s\n");
            }
        }

        // Write our standard footer to the PostScript file.
        postScript.append(POSTSCRIPT_BOTTOM);

        return postScript.toString();
    }

    /**
     * Helper function that projects a 3-D model of atoms, represented by
     * {@code CrtFile.Atom} objects, onto a plane. The model may be rotated and
     * scaled by caller-specified quantities prior to projection. The projected
     * 2-D model is translated in such a way that no coordinates are negative,
     * at least one atom has an x-coordinate of 0, and at least one atom has a
     * y-coordinate of 0. The atoms represented by the specified {@code crtFile}
     * are repositioned in place.
     * 
     * @param model a {@code MolecularModel&lt;? extends Atom, ? extends
     *        Bond&gt;} to be projected. On return, the model's atom positions
     *        will have been modified by parallel projection onto the x/y plane
     *        and translation to the boundary of the +x/+y coordinate quadrant
     * @param parameters values within this object identify the projection
     *        computation that is to be performed on the 3-D model of atoms. The
     *        values of {@code parameters.x}, {@code parameters.y}, and
     *        {@code parameters.z} describe how many degrees the model should be
     *        rotated about its x-axis, y-axis, and z-axis, respectively, prior
     *        to projection. The value of {@code parameters.scale} allows the
     *        model's size to be adjusted by an arbitrary constant prior to
     *        projection. Any other values within the {@code parameters} object
     *        are ignored.
     * @return a {@code float[][]} containing at each primary index a
     *         {@code float[]} of the coordinates of the atom at the same index
     *         in the model's atom list, as projected onto the plane described
     *         by the image parameters and relative to that plane's internal
     *         Cartesian coordinates
     */
    private static float[][] projectAtomsOntoPlane(
            MolecularModel<? extends Atom, ?> model, ImageParameters parameters) {
        float[][] coords = new float[model.getAtoms().size()][];
        int index = 0;

        for (Atom atom : model.getAtoms()) {
            double[] c = atom.getPosition().getCoordinates();

            coords[index++] = new float[] { (float) c[0], (float) c[1],
                    (float) c[2] };
        }

        /*
         * Initialize a transformation matrix according to the caller-specified
         * rotations and scale.
         */
        Matrix3D matrix = new Matrix3D();
        matrix.xrot(-parameters.getXRotation());
        matrix.yrot(parameters.getYRotation());
        matrix.zrot(parameters.getZRotation());
        matrix.scale((float) parameters.getModelScaleFactor());

        // Rotate and scale our 3-D coordinates
        matrix.transform(coords, coords, coords.length);

        /*
         * Decide how much the model should be translated so that all
         * coordinates are nonnegative, at least one atom has a x-coordinate of
         * 0, and at least one atom has a y-coordinate of 0. At the same time,
         * project the 3-D model down to 2-D by zeroing all z-coordinates.
         */
        double shiftX = Float.MAX_VALUE;
        double shiftY = Float.MAX_VALUE;

        for (float[] atomCoords : coords) {
            if (atomCoords[0] < shiftX) {
                shiftX = atomCoords[0];
            }
            if (atomCoords[1] < shiftY) {
                shiftY = atomCoords[1];
            }
        }

        /*
         * Initialize a translation matrix that will reposition the model
         * appropriately and project it onto the viewing plane.
         */
        matrix.unit();
        matrix.translate(1f - (float) shiftX, 1f - (float) shiftY, 0f);
        matrix.scale(1f, 1f, 0f);

        // Translate the 2D model.
        matrix.transform(coords, coords, coords.length);

        return coords;
    }
}
