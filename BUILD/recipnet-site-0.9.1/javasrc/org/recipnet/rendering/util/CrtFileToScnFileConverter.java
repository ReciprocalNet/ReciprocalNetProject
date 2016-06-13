/*
 * Reciprocal Net project
 * rendering software
 * 
 * CrtToScnConverter.java
 *
 * 19-Aug-2004: ekoperda wrote first draft, borrowing heavily from class
 *              org.recipnet.site.wrapper.SceneMaker
 * 07-Nov-2005: jobollin updated this class for compatibility with revisions to
 *              CrtFile; formatted the source according to typical conventions
 *              (including especially removal of tabs); removed unused imports
 * 23-Nov-2005: jobollin removed references to the (deprecated) ScnFile.Vector
 *              class in favor of org.recipnet.common.geometry.Vector, and to
 *              remove references to ScnFile.Rgb in favor of java.awt.Color;
 *              applied other changes to account for changes in ScnFile; renamed
 *              ElementDataTableEntry to ElementRenderingProperties, removed its
 *              atomicNumber member, changed its three color component fields to
 *              a single Color object, encapsulated its fields; changed the
 *              nullary constructor to use the context classloader instead of
 *              (directly) the system classloader; changed the unary constructor
 *              to take an InputStream instead of a File; modified convert() to
 *              move the camera and lights relative to the model instead of the
 *              other way around
 */

package org.recipnet.rendering.util;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.EnumMap;
import java.util.Map;

import org.recipnet.common.Element;
import org.recipnet.common.Matrix3D;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.ScnFile;
import org.recipnet.common.files.ScnFile.Camera;
import org.recipnet.common.files.ScnFile.Light;
import org.recipnet.common.geometry.Point;
import org.recipnet.common.geometry.Vector;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.MolecularModel;
import org.recipnet.rendering.servlet.CrtRenderRequest;

/**
 * Conversion class that transforms a {@code CrtFile} object representing
 * a molecular model into an {@code ScnFile} object representing a
 * three-dimensional scene suitable for ray-traced rendering. In the generated
 * scene, spheres correspond with atoms from the molecular model, cylinders
 * correspond with bonds from the molecular model, or both. A number ofs scene
 * details are configurable via the {@code CrtRenderRequest} object that
 * {@code convert()} takes as an argument.
 * <p>
 * This class requires that an <i>element data table</i> file be available at
 * runtime. The contents of this text file relate each element of the periodic
 * table to a sphere of a particular color and radius. The first line of the
 * file contains a single integer that is a count of the number of elements
 * described by the file. Each subsequent line contains five numeric fields
 * (separated by whitespace): the first field is an integer that is the atomic
 * number, the second field is a real number that is the red component of the
 * sphere's color (ranging from 0 to 1), the third field is the green component,
 * the fourth field is the blue component, and the fifth field is a real number
 * that is the radius of the sphere. See the parsing algorithm in
 * {@code readElementDataFile()} for more information.
 * <p>
 * This class is thread-safe.
 */
public class CrtFileToScnFileConverter {
    
    /**
     * Maps atomic numbers ({@code Integer}'s) to
     * {@code ElementRenderingProperties} objects that describe the spheres
     * that should represents these atoms. Populated by
     * {@code readElementDataFile()} at construction time.
     */
    private final Map<Element, ElementRenderingProperties> elementDataTable
            = new EnumMap<Element, ElementRenderingProperties>(Element.class);

    /**
     * No-arg constructor.  The <i>element data file</i> is presumed to be
     * accessible to the current thread's context class loader via the name
     * 'element_data.txt'.  This would be the case if this class were packaged
     * in a JAR file, and the same JAR file also contained a file of the
     * specified name in the root of the archive.  The <i>element data file</i>
     * is read at initialization and its contents cached.
     * 
     * @throws FileNotFoundException if no file called 'element_data.txt'
     *             could be found by the classloader.
     * @throws IOException if the file could not be read for some other reason.
     */
    public CrtFileToScnFileConverter() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(
                "element_data.txt");
        if (is == null) {
            // Resource file not found.
            throw new FileNotFoundException();
        }
        this.readElementDataFile(is);
    }

    /**
     * Constructor that uses element data obtained from the specified input
     * stream.
     * 
     * @param  elementData an {@code InputStream} from which the element data
     *         should be read 
     * 
     * @throws IOException if the <i>element data file</i> could not be read or
     *         parsed for some reason.
     */
    public CrtFileToScnFileConverter(InputStream elementData)
            throws IOException {
        this.readElementDataFile(elementData);
    }

    /**
     * Generates a scene from a molecular model according to caller
     * specifications.
     * 
     * @param  <A> the type of atom characterizing the CRT file; it should
     *         normally be possible to infer this from the {@code crtFile}
     *         argument
     * 
     * @return an {@code ScnFile} object that represents that contents of
     *         an .scn file that the caller may proceed to write to the
     *         filesystem or elsewhere. The file expresses a geometric scene
     *         that is suitable for ray-tracing.
     * @param crtFile a {@code CrtFile} object representing the contents
     *            of a .crt file. The file expresses a molecular model. The
     *            atoms and bonds within this model correspond to spheres and
     *            cylinders (respectively) in the generated scene, depending
     *            upon drawing options. Fields within this object may be
     *            manipulated by this method during the transformation, so the
     *            caller should not attempt to reuse {@code crtFile}
     *            after this method returns.
     * @param req values within this {@code CrtRenderRequest} object
     *            describe the orientation of the molecular model within the
     *            scene and the drawing style desired. The fields
     *            {@code drawingType}, {@code xCoordinate},
     *            {@code yCoordinate}, {@code zCoordinate},
     *            {@code distance}, {@code ballSize},
     *            {@code rodSize}, {@code bgColorRed},
     *            {@code bgColorGreen}, and {@code bdColorBlue}
     *            are significiant. The fields {@code username},
     *            {@code password}, {@code requestedPriority},
     *            and {@code requestedImageFormat} are ignored.
     * @throws IllegalArgumentException if {@code crtFile.drawingType} is
     *             not one of {@code CrtRenderRequest.BALL_STICK_MODE},
     *             {@code CrtRenderRequst.SPACE_FILLED_MODE}, or
     *             {@code LINE_DRAWING_MODE}. Also thrown if an atom
     *             record within {@code crtFile} specifies an atomic
     *             number that is not recognized by this converter.
     */
    public <A extends Atom> ScnFile convert(CrtFile<A> crtFile,
            CrtRenderRequest req) {
        
        // Quick sanity check.
        if (req.drawingType != CrtRenderRequest.BALL_STICK_MODE
                && req.drawingType != CrtRenderRequest.SPACE_FILLED_MODE
                && req.drawingType != CrtRenderRequest.LINE_DRAWING_MODE) {
            throw new IllegalArgumentException();
        }

        MolecularModel<A, Bond<A>> model = crtFile.getModel();
        ScnFile scnFile = new ScnFile();
        Point centroid = model.computeCentroid();
        double[] centroidCoords = centroid.getCoordinates();
        double viewingDistance = (600 / req.distance);
        Vector worldUpVector;

        /*
         * Initialize a translation / rotation matrix that will orient the
         * camera and lights correctly relative to the model's internal
         * coordinate system according to caller-specified values.
         */
        Matrix3D transMatrix = new Matrix3D();
        
        transMatrix.zrot(-req.zRotation);
        transMatrix.yrot(-req.yRotation);
        transMatrix.xrot(req.xRotation);
        
        // The up vector's transformation does not involve a translation
        worldUpVector
               = new Vector(Point.ORIGIN, pointFor(0, 1, 0, transMatrix));

        // All point transformations do involve a translation:
        transMatrix.translate((float) centroidCoords[0],
                (float) centroidCoords[1], (float) centroidCoords[2]);

        // Prepare the scene file.
        scnFile.setTitle("Molecule");
        scnFile.setUpVector(worldUpVector);
        scnFile.setCamera(new Camera(
                pointFor(0.0, 0.0, viewingDistance, transMatrix), centroid, 0));
        scnFile.setRaysPerPixel(2);
        scnFile.setBackgroundColor(new Color(req.bgColorRed,
                req.bgColorGreen, req.bgColorBlue));
        scnFile.setFieldOfView(40);
        scnFile.setAmbientLightColor(new Color(0.2f, 0.2f, 0.2f));
        scnFile.addLight(new Light(Color.WHITE, pointFor(
                0.0, 2 * viewingDistance, 3 * viewingDistance, transMatrix)));
        scnFile.addLight(new Light(Color.WHITE, pointFor(
                -3 * viewingDistance, -2 * viewingDistance, 0.0, transMatrix)));
        scnFile.addLight(new Light(Color.WHITE, pointFor(
                3 * viewingDistance, viewingDistance, 0.0, transMatrix)));

        // Add spheres to the scene to represent atoms.
        if (req.drawingType == CrtRenderRequest.SPACE_FILLED_MODE
                || req.drawingType == CrtRenderRequest.BALL_STICK_MODE) {
            float ballSize =
                    (req.drawingType == CrtRenderRequest.SPACE_FILLED_MODE)
                            ? req.ballSize : req.ballSize * 0.25f;
            ScnFile.Material atomMaterial
                    = new ScnFile.Material(0, 0.4f, 0.6f, 200);
            
            for (Atom atom : model.getAtoms()) {
                double[] coords = atom.getPosition().getCoordinates();

                // Look up data about this atom in our element data table.
                ElementRenderingProperties dataEntry =
                        elementDataTable.get(atom.getElement());
                if (dataEntry == null) {
                    // There is no data in our element table for this atom.
                    // Its atomic number must be invalid.
                    throw new IllegalArgumentException();
                }

                // Construct a sphere to represent the atom.
                ScnFile.Sphere sphere = new ScnFile.Sphere(
                        new Point(coords[0], coords[1], coords[2]),
                        ballSize * dataEntry.getRadius());
                sphere.setMaterial(atomMaterial);
                sphere.setColor(dataEntry.getColor());

                // Append the sphere to the scene.
                scnFile.addObject(sphere);
            }
        }

        // Add cylinders to the scene to represent bonds.
        if (req.drawingType == CrtRenderRequest.BALL_STICK_MODE
                || req.drawingType == CrtRenderRequest.LINE_DRAWING_MODE) {
            ScnFile.Material bondMaterial
                    = new ScnFile.Material(0, 0.25f, 0.75f, 6);
            
            for (Bond<? extends Atom> bond : model.getBonds()) {

                // Constructor a cylinder to represent the bond.
                ScnFile.Cylinder cylinder = new ScnFile.Cylinder(
                        bond.getAtom1().getPosition(),
                        bond.getAtom2().getPosition(),
                        req.rodSize);
                cylinder.setMaterial(bondMaterial);

                // Append the cylinder to the scene.
                scnFile.addObject(cylinder);
            }
        }

        return scnFile;
    }
    
    /**
     * A helper method that generates a Point corresponding to the specified
     * 3D coordinates, as transformed according to the specified transformation
     * matrix
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
     * Helper function that reads and parses an <i>element data file</i> from
     * the specified source. Expected file format is described by class-level
     * comments. Populates the {@code elementDataTable}.
     * 
     * @param is the {@code InputStream} from which file data should be
     *            read. The stream is closed by this method on successful
     *            completion.
     * @throws IOException if the file could not be read or parsed for some
     *             reason.
     */
    private void readElementDataFile(InputStream is) throws IOException {
        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StreamTokenizer st = new StreamTokenizer(reader);
        int countEntries;
        
        st.eolIsSignificant(false);
        st.parseNumbers();
        countEntries = (int) Math.round(readNumberFromTokenizer(st));
        for (int i = 0; i < countEntries; i++) {
            Element el = Element.forAtomicNumber(
                    (int) readNumberFromTokenizer(st));
            ElementRenderingProperties entry = new ElementRenderingProperties(
                    (float) readNumberFromTokenizer(st),
                    (float) readNumberFromTokenizer(st),
                    (float) readNumberFromTokenizer(st),
                    (float) readNumberFromTokenizer(st));
            
            this.elementDataTable.put(el, entry);
        }
        reader.close();
    }

    /**
     * Internal utility function that reads the next token from the specified
     * {@code StreamTokenizer} and returns it as a floating-point value.
     * 
     * @param st the {@code StreamTokenizer} from which the next token
     *            will be read.
     * @return the floating-point value read from the stream.
     * @throws IOException if an error occurred while reading the stream,
     *             including the case where the next token is not numeric.
     */
    private static double readNumberFromTokenizer(StreamTokenizer st)
            throws IOException {
        if (st.nextToken() != StreamTokenizer.TT_NUMBER) {
            throw new IOException();
        }
        return st.nval;
    }

    /**
     * Internal nested class that describes the sphere (in a scene) that should
     * represent an atom (in a molecular model) of a particular type.
     */
    private static class ElementRenderingProperties {
        private final Color color;

        private final float radius;

        public ElementRenderingProperties(float red, float green,
                float blue, float radius) {
            this.color = new Color(red, green, blue);
            this.radius = radius;
        }

        /**
         * @return the {@code color} {@code Color}
         */
        public Color getColor() {
            return color;
        }

        /**
         * @return the {@code radius} {@code float}
         */
        public float getRadius() {
            return radius;
        }
    }
}
