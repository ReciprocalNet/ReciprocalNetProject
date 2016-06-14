/*
 * Reciprocal Net Project
 *
 * SceneMaker.java
 *
 * 23-Oct-2002: jobollin wrote first draft
 * 09-Jan-2003: jobollin removed unused imports
 * 21-Feb-2003: jobollin made consistent with changes to Rotate3DModel applied
 *              as part of task #682
 * 20-Mar-2003: jobollin made makeScene throw exceptions instead of eating
 *              them, and to not throw IOExceptions (part of task #629)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper; made class and most
 *              methods public
 * 09-Jan-2004: ekoperda modified makeScene(), sceneHeader(), sceneAtoms(),
 *              and sceneBonds() to utilize new CrtFile class; formatted the
 *              source according to typical conventions (especially removal of
 *              tabs)
 * 05-Dec-2005: jobollin modified makeScene(), sceneAtoms(), and sceneBonds() to
 *              accommodate changes to CrtFile; modified makeScene() to return
 *              a ScnFile object instead of a String; made makeScene() accept
 *              a MolecularModel object instead of raw CRT data; renamed and
 *              repurposed sceneAtoms() to addAtoms() and sceneBonds() to
 *              addBonds(); moved most of makeScene()'s work into helper
 *              methods; changed paradigm to move camera and lights instead of
 *              model atoms when generating the scene; accommodated API changes
 *              to the ImageParameters class; removed unused imports
 * 25-May-2006: jobollin updated addBonds() to produce a dual-color bonds in
 *              the same material and colors as the atoms involved
 * 30-Aug-2006: jobollin added support for suppressing rendering of hydrogen
 *              atoms when the parameters so specify
 */

package org.recipnet.site.wrapper;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Collection;

import org.recipnet.common.Matrix3D;
import org.recipnet.common.files.ScnFile;
import org.recipnet.common.files.ScnFile.Camera;
import org.recipnet.common.files.ScnFile.Cylinder;
import org.recipnet.common.files.ScnFile.Light;
import org.recipnet.common.files.ScnFile.Material;
import org.recipnet.common.files.ScnFile.Sphere;
import org.recipnet.common.geometry.Point;
import org.recipnet.common.geometry.Vector;
import org.recipnet.common.molecule.AnhydrogenousModel;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;

/**
 * A class providing a VORT scene file construction service
 */
public abstract class SceneMaker {
    
    private static final Material atomMaterial
            = new Material(0.0f, 0.4f, 0.6f, 200.0f);
    
    /**
     * <p>
     * Generates a "scene" file (suitable for rendering by a ray-tracing engine)
     * that depicts a specific molecule in a specific orientation with a
     * specific drawing style. Available drawing styles include <i>solid</i>
     * <i>ball & stick</i>, and <i>line</i>. The caller describes the molecule
     * to be drawn by supplying the data from its {@code .crt} file. See
     * the {@code CrtFile} class for a detailed explanation of this file
     * format. The caller controls the orientation and size of the scene via
     * values in {@code parameters}. The caller controls the color and
     * size of atoms within the scene via the color/size table he supplies via
     * {@code tableReader}.
     * </p><p>
     * The generated scene includes the following components:
     * <ul>
     * <li> one sphere for each atom in the .crt file if the drawing style is
     * <i>solid</i> or <i>ball & stick</i>. In the former case the sphere's
     * radius is {@code parameters.bsize} times an element-specific size
     * read from the color/size table. In the latter case the sphere's radius is
     * {@code parameters.bsize} divided by four times an element-specific
     * size read from the color/size table. The sphere's color is read from the
     * color/size table according to the atom's atomic number. The sphere's
     * ambient light factor is 0.2 in all three bands. The sphere's refractive
     * index is 0, its diffuse component is 0.4, its specular component is 0.6,
     * and its specular exponent is 200.
     * <li> one (cylinder) for each bond in the .crt file if the drawing style
     * is <i>ball & stick</i> or <i>line</i>. The length of the cylinder is
     * the distance from one atom to the other. The radius of the cylinder is
     * {@code parameters.rsize}. The color is pure white. The ambient
     * light factor is 0.2 across all three bands. The refractive index is 0,
     * the diffuse component is 0.25, the specular component is 0.75, and the
     * specular exponent is 6.0.
     * <li> three pure white, omnidirectional light sources positioned at (0, 5,
     * 3d), (3d, d, 0), and (-3d, -d, 0), where d is the value of
     * {@code parameters.distance}.
     * <li> one viewpoint situated along the z-axis, at a distance from the
     * origin of 600 divided by {@code parameters.distance}, pointing
     * toward the origin, with a viewport of 40 degrees.
     * </ul>
     * The molecule's center is situated at the scene's origin. One distance
     * unit in the .crt file equals one distance unit in the rendered scene.
     * </p>
     * 
     * @param  <A> the type of {@code Atom} characterizing the molecular model;
     *         normally inferred from the {@code model} argument
     * @param  parameters values within this object describe the orientation and
     *         drawing style desired:
     *         <ul>
     *           <li>{@code parameters.drawType} - determines the
     *           drawing style to be used in generating the scene. Valid values
     *           are {@code "s"} for <i>solid</i> style,
     *           {@code "b"} for <i>ball & stick</i> style, and
     *           {@code "l"} for <i>line</i> style.
     *           <li> {@code parameters.x} - number of degrees to rotate
     *           the model about its x-axis prior to conversion.
     *           <li> {@code parameters.y} - number of degrees to rotate
     *           the model about its y-axis prior to conversion.
     *           <li> {@code parameters.z} - number of degrees to rotate
     *           the model about its z-axis prior to conversion.
     *           <li> {@code parameters.distance} - an arbitrary
     *           "distance indicator" whose value has several effects described
     *           previously.
     *           <li> {@code parameters.bsize} - size factor that
     *           affects the radius of each generated sphere that represents an
     *           atom. See above for an explanation of how the radius is
     *           actually computed. This value is ignored unless the drawing
     *           style is <i>solid</i> or <i>ball & stick</i>.
     *           <li> {@code parameters.rsize} the radius to use for
     *           each generated cylinder that represents a bond. This value is
     *           ignored unless the drawing style is <i>ball & stick</i> or
     *           <i>line</i>.
     *           <li> {@code parameters.r} - the red component of the
     *           background color to be used in any image rendered from the
     *           generated scene; range 0 to 1.
     *           <li> {@code parameters.g} - the green component of the
     *           background color to be used in any image rendered from the
     *           generated scene; range 0 to 1.
     *           <li> {@code parameters.b} - the blue component of the
     *           background color to be used in any image rendered from the
     *           generated scene, range 0 to 1.
     *           </ul>. Any other values in {@code parameters} are
     *           ignored.
     * @param  tableReader a caller-supplied {@code Reader} object from which an
     *         atom color/size table may be read.  Commonly this file is named
     *         {@code element_data.txt}.
     * @param  model a {@code
     *         MolecularModel&lt;A, ? extends Bond&lt;? extends A&gt;&gt;}
     *         representing the model to be rendered
     *             
     * @return a {@code ScnFile} describing the requested image, or {@code null}
     *         if the drawing parameters specify a {@code null} drawing type.
     *         Commonly this data would be stored in a file with the .scn
     *         extension.
     *         
     * @throws IOException if one is encountered while reading the element data
     * @throws FileFormatException if the element data read from the specified
     *         reader is not in the expected format
     */
    public static <A extends Atom> ScnFile makeScene(ImageParameters parameters,
            Reader tableReader, MolecularModel<A, Bond<A>> model)
            throws IOException, FileFormatException {
        
        // Early exit if no drawing type is specified
        if ((parameters.getDrawType() == null)
                || (parameters.getDrawType().length() == 0)) {
            return null;
        }
        
        // Optionally derive a model without hydrogen atoms
        if (!parameters.shouldIncludeHydrogen()) {
            model = new AnhydrogenousModel<A, Bond<A>>(model);
        }
        
        // Read the relevant element data
        Intrinsic[] intrinsics = readIntrinsics(tableReader);
        
        // Create the .scn file.
        ScnFile scn = createEmptyScene(parameters, model.computeCentroid());
        switch (parameters.getDrawType().toLowerCase().charAt(0)) {
            case 's': // Solid drawing style.
                addAtoms(scn, model.getAtoms(), intrinsics,
                        parameters.getBallScaleFactor());
                break;
            case 'b': // Ball & stick drawing style
                addAtoms(scn, model.getAtoms(), intrinsics,
                        parameters.getBallScaleFactor() * 0.25d);
                addBonds(scn, model.getBonds(), intrinsics,
                        parameters.getRodScaleFactor());
                break;
            case 'l': // Line style
                addRodCaps(scn, model.getAtoms(), intrinsics,
                        parameters.getRodScaleFactor());
                addBonds(scn, model.getBonds(), intrinsics,
                        parameters.getRodScaleFactor());
                break;
        }

        return scn;
    }

    /**
     * Reads the table of element data "instrinsics" from the specified reader
     * 
     * @param  tableReader the {@code Reader} from which to read the element
     *         data
     * 
     * @return the element data as an {@code Intrinsics[]}
     * 
     * @throws IOException if one is encountered while reading the element data
     * @throws FileFormatException if the element data read from the specified
     *         reader is not in the expected format
     */
    private static Intrinsic[] readIntrinsics(Reader tableReader) 
            throws IOException, FileFormatException {
        
        // Prepare to read the color/size table.
        StreamTokenizer st = new StreamTokenizer(tableReader);
        int nIntrinsics;
        Intrinsic[] intrinsics;
        
        st.eolIsSignificant(false);
        st.parseNumbers();
        nIntrinsics = (int) Math.round(nextNumber(st)) + 1;
        intrinsics = new Intrinsic[nIntrinsics];
    
        // Read the color/size table.
        for (int i = 1; i < nIntrinsics; i++) {
            intrinsics[(int) Math.round(nextNumber(st))]
                       = new Intrinsic(nextNumber(st), nextNumber(st),
                               nextNumber(st), nextNumber(st));
        }
    
        return intrinsics;
    }

    /**
     * Reads a numeric token from the specified tokenizer and returns its value
     *
     * @param  st the {@code StreamTokenizer} from which to read
     * 
     * @return the numeric value of the token read
     * 
     * @throws FileFormatException if the token read from the tokenizer is not
     *         numeric
     * @throws IOException if an I/O error occurs
     */
    private static double nextNumber(StreamTokenizer st)
            throws FileFormatException, IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new FileFormatException(
                    "Unexpected non-numeric data among the element properties");
        }
        return st.nval;
    }

    /**
     * Creates a {@code ScnFile} containing general scene setup, including
     * camera and lighting (among other things), but no objects.  Camera and
     * lights are positioned and directed according to the specified parameters
     * and scene center
     * 
     * @param  parameters an {@code ImageParameters} specifying the key details
     *         of the desired scene, including those needed for relative
     *         camera positioning and for specifying the background color 
     * @param  center a {@code Point} representing the center of the scene (at
     *         which the camera should be directed)
     *         
     * @return a new {@code ScnFile} configured according to the specified
     *         camera and scene center
     */
    private static ScnFile createEmptyScene(ImageParameters parameters,
            Point center) {
        ScnFile scn = new ScnFile();
        double[] centerCoords = center.getCoordinates();
        double viewingDistance = (600 / parameters.getViewMagnification());
        Matrix3D transMatrix = new Matrix3D();
        Vector worldUpVector;
        Point cameraPosition;

        transMatrix.zrot(-parameters.getZRotation());
        transMatrix.yrot(-parameters.getYRotation());
        transMatrix.xrot(parameters.getXRotation());

        // The up vector's transformation does not involve a translation
        worldUpVector
               = new Vector(Point.ORIGIN, pointFor(0, 1, 0, transMatrix));

        // All point transformations do involve a translation:
        transMatrix.translate((float) centerCoords[0],
                (float) centerCoords[1], (float) centerCoords[2]);

        cameraPosition = pointFor(0.0, 0.0, viewingDistance, transMatrix);

        scn.setTitle("Molecule");
        scn.setUpVector(worldUpVector);
        scn.setCamera(new Camera(cameraPosition, center, 0));
        scn.setRaysPerPixel(2);
        scn.setBackgroundColor(parameters.getBackgroundColor());
        scn.setFieldOfView(40);
        scn.setAmbientLightColor(new Color(0.2f, 0.2f, 0.2f));
        scn.addLight(new Light(Color.WHITE, pointFor(
                0.0, 2 * viewingDistance, 3 * viewingDistance, transMatrix)));
        scn.addLight(new Light(Color.WHITE, pointFor(
                -3 * viewingDistance, -2 * viewingDistance, 0.0, transMatrix)));
        scn.addLight(new Light(Color.WHITE, pointFor(
                3 * viewingDistance, viewingDistance, 0.0, transMatrix)));

        return scn;
    }

    /**
     * Generates a Point corresponding to the specified 3D coordinates, as
     * transformed according to the specified transformation matrix
     *  
     * @param  x the x coordinate as a {@code double}
     * @param  y the y coordinate as a {@code double}
     * @param  z the z coordinate as a {@code double}
     * @param  matrix the transformation matrix as a {@code Matrix3D}
     * 
     * @return a {@code Point} representing the transformed coordinates
     */
    private static Point pointFor(double x, double y, double z,
            Matrix3D matrix) {
        float[] coords = new float[] {(float) x, (float) y, (float) z};
        
        matrix.transform(coords, coords, 1);
        
        return new Point(coords[0], coords[1], coords[2]);
    }

    /**
     * Adds the content of the "atoms" section to an .scn file.  This section
     * consists of zero or more {@code sphere} directives.
     * 
     * @param  scn the {@code ScnFile} to which the atom information should be
     *         added
     * @param  atoms a {@code List<? extends Atom>} of the atoms in the scene
     * @param  intrinsics an {@code Intrinsic[]} containing the atom rendering
     *         parameters
     * @param  scale the atomic scale factor
     */
    private static void addAtoms(ScnFile scn, Collection<? extends Atom> atoms,
            Intrinsic[] intrinsics, double scale) {
        for (Atom atom : atoms) {
            int atomicNumber = atom.getElement().getAtomicNumber();
            Sphere sphere = new Sphere(atom.getPosition(),
                    (float) scale * intrinsics[atomicNumber].getRadius());
            
            sphere.setColor(intrinsics[atomicNumber].getColor());
            sphere.setMaterial(atomMaterial);
            scn.addObject(sphere);
        }
    }

    /**
     * Adds spherical rod caps to an .scn file.  These are very similar to
     * the atoms added by
     * {@link #addAtoms(ScnFile, Collection, Intrinsic[], double)}, except that
     * their radii are all the same as the (uniform) rod radii. This has the
     * effect of giving the rod ends a rounded look and, more importantly,
     * filling in gaps between rod ends at atom positions
     * 
     * @param scn the {@code ScnFile} to which the atom information should be
     *        added
     * @param atoms a {@code List<? extends Atom>} of the atoms in the scene
     * @param intrinsics an {@code Intrinsic[]} containing the atom rendering
     *        parameters
     * @param rscale the bond radius scale
     */
    private static void addRodCaps(ScnFile scn,
            Collection<? extends Atom> atoms, Intrinsic[] intrinsics,
            double rscale) {
        float rsize = computeRodSize(rscale);
        
        for (Atom atom : atoms) {
            Sphere sphere = new Sphere(atom.getPosition(), rsize);
            
            sphere.setColor(intrinsics[atom.getElement().getAtomicNumber()].getColor());
            sphere.setMaterial(atomMaterial);
            scn.addObject(sphere);
        }
    }
    
    /**
     * Adds the content of the "bonds" section to an .scn file.  This section
     * consists of zero or more {@code cylinder} directives.
     * 
     * @param  <A> the type of atom characterizing the bonds in the collection;
     *         normally inferred from the argument
     * @param  scn the {@code ScnFile} to which bond information should be
     *         added
     * @param  bonds a {@code List<? extends Bond>} of the bonds in the scene
     * @param  intrinsics an {@code Intrinsic[]} containing the atom rendering
     *         parameters
     * @param  rscale the bond radius scale
     */
    private static <A extends Atom> void addBonds(ScnFile scn,
            Collection<Bond<A>> bonds, Intrinsic[] intrinsics, double rscale) {
        float rsize = computeRodSize(rscale);
        // Material rodMaterial = new Material(0.0f, 0.25f, 0.75f, 6.0f);

        for (Bond<A> bond : bonds) {
            Atom atom1 = bond.getAtom1();
            Atom atom2 = bond.getAtom2();
            
            if (atom1.getElement() == atom2.getElement()) {
                Cylinder rod = new Cylinder(atom1.getPosition(),
                        atom2.getPosition(), rsize);
                
                rod.setMaterial(atomMaterial);
                rod.setColor(intrinsics[atom1.getElement().getAtomicNumber()].getColor());
                scn.addObject(rod);
            } else {
                Point midpoint = Point.midPoint(atom1.getPosition(),
                        atom2.getPosition());
                Cylinder rod1
                        = new Cylinder(atom1.getPosition(), midpoint, rsize);
                Cylinder rod2
                        = new Cylinder(midpoint, atom2.getPosition(), rsize);
        
                rod1.setMaterial(atomMaterial);
                rod2.setMaterial(atomMaterial);
                rod1.setColor(intrinsics[atom1.getElement().getAtomicNumber()].getColor());
                rod2.setColor(intrinsics[atom2.getElement().getAtomicNumber()].getColor());
                scn.addObject(rod1);
                scn.addObject(rod2);
            }
        }
    }

    /**
     * Computes the diameter of the rods to be rendered in a stick or ball &
     * stick model
     * 
     * @param rodScale the bond radius scale
     * @return the rod diameter
     */
    private static float computeRodSize(double rodScale) {
        return (float) (0.08d * rodScale);
    }
    
    /**
     * A class representing the "intrinsic" properties of the atoms of some
     * element, as necessary to create a scene model
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class Intrinsic {
        
        /**
         * The color in which to render atoms
         */
        private final Color color;
        
        /**
         * The radius of atoms
         */
        private final float radius;
        
        /**
         * Initializes an {@code Intrinsic} with the specified parameters
         * 
         * @param  red the red component of this {@code Intrinsic}'s color, as
         *         a {@code double} between zero and one, inclusive
         * @param  green the green component of this {@code Intrinsic}'s color,
         *         as a {@code double} between zero and one, inclusive
         * @param  blue the blue component of this {@code Intrinsic}'s color, as
         *         a {@code double} between zero and one, inclusive
         * @param  r the radius recorded in this {@code Intrinsic}, as a
         *         positive {@code double}
         */
        public Intrinsic(double red, double green, double blue, double r) {
            color = new Color((float) red, (float) green, (float) blue);
            radius = (float) r;
        }
        
        /**
         * Returns the color recorded in this {@code Intrinsic}
         * 
         * @return the color as a {@code Color}
         */
        public Color getColor() {
            return color;
        }
    
        /**
         * Returns the sphere radius recorded in this {@code Intrinsic}
         * 
         * @return the radius as a {@code float}
         */
        public float getRadius() {
            return radius;
        }
    }
}
