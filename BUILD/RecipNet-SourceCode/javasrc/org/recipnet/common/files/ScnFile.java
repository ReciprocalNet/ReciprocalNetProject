/*
 * Reciprocal Net Project
 * 
 * ScnFile.java
 *
 * 19-Aug-2004: ekoperda wrote first draft
 * 23-Nov-2005: jobollin removed references to the nested Vector
 *              class in favor of org.recipnet.common.geometry.Vector and / or
 *              org.recipnet.common.geometry.Point, as appropriate;
 *              encapsulated fields; renamed ObjectInScene to SceneObject;
 *              moved SceneObject.ambientLightEffect to
 *              SceneFile.ambientLightColor; removed unused imports; reworked
 *              output code to use Formatters instead of invoking toString() on
 *              everything
 */

package org.recipnet.common.files;

import java.awt.Color;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.Locale;

import org.recipnet.common.geometry.AbstractCoordinates;
import org.recipnet.common.geometry.Point;
import org.recipnet.common.geometry.Vector;

/**
 * <p>
 * Encapsulates the contents of a file that describes a 3-dimensional scene with
 * detail appropriate for rendering applications. The .SCN format was originally
 * defined by a man named Antonio Costa; <a href=
 * "http://astronomy.swin.edu.au/~pbourke/geomformats/scn/">his specification is
 * available online</a>.  This class is written to the adaptation of Costa's
 * specification defined by the public domain ray-tracing package VORT (a Very
 * Ordinary Rendering Toolkit). The VORT distributable is included in the 
 * {@code 3rdparty} directory of the Reciprocal Net source tree: look for the
 * file called {@code vort.tar.gz}. Within that archive, the file
 * {@code art/docs/art.doc} contains specification with which this class
 * complies.
 * </p><p>
 * At this version, this class supports only a subset of the expressive
 * capabilities of the SCN format -- no more than the site software uses to
 * model molecules.  General fetures supported include a title, background
 * color, number of rays to trace per pixel, field of view, ambient and
 * positional (omnidirectional only) lighting, world orientation, and camera
 * position and direction.  Sphere and cylinder objects are supported, each
 * with color and material properties and a shadow-casting flag.  The SCN format
 * has a wide variety of general rendering attributes, object
 * </p><p>
 * The current implementation supports the generation of new .scn files based on
 * an instance's member variables but but does not support reading from .scn
 * files.
 * </p><p>
 * This class is not thread-safe.
 * </p>
 */
public class ScnFile {

    /**
     * The title of the scene; corresponds to the {@code title} directive in the
     * file.
     */
    private String title = null;

    /**
     * The number of rays to trace per pixel; corresponds to the
     * {@code raysperpixel} directive in the file.
     */
    private Integer raysPerPixel = null;

    /**
     * The background color for the scene; corresponds to the {@code background}
     * directive in the file.
     */
    private Color backgroundColor = null;

    /**
     * The ambient light color in the scene; corresponds to the {@code ambient}
     * directive in the file.
     */
    private Color ambientLightColor = null;

    /**
     * The custom camera characteristics for this {@code ScnFile}, if any;
     * comprises the several attributes of a {@code lookat} directive in the
     * file.
     */
    private Camera camera = null;
    
    /**
     * The direction considered to be "up" in the scene; corresponds to the
     * {@code up} directive in the file. 
     */
    private Vector upVector = null;

    /**
     * The field of view on the scene; corresponds to the {@code fieldofview}
     * directive in the file.
     */
    private Float fieldOfView = null;

    /**
     * A collection of zero or more {@code Light} objects, each of which
     * corresponds to one {@code light} directive in the file.
     */
    private final Collection<Light> lights = new ArrayList<Light>();

    /**
     * A collection of zero or more {@code SceneObject} objects, each of which
     * corresponds to a three-dimensional object in the scene described by this
     * {@code ScnFile}.
     */
    private final Collection<SceneObject> objects
            = new ArrayList<SceneObject>();

    /**
     * Sets the title of this scene file
     * 
     * @param  title the {@code String} to set as the scene title, or
     *         {@code null} for no title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the color of the ambient light in the scene, if any
     * 
     * @param  color the {@code Color} to set as the scene-wide ambient light
     *         color; may be {@code null} to remove any ambient lighting
     *         definition at this level
     */
    public void setAmbientLightColor(Color color) {
        this.ambientLightColor = color;
    }
    
    /**
     * Sets the number of rays to trace per pixel of the image; more rays
     * produce a higher quality image (to a point)
     *  
     * @param  raysPerPixel the number of rays to trace per pixel; should be
     *         positive
     */
    public void setRaysPerPixel(int raysPerPixel) {
        if (raysPerPixel < 1) {
            throw new IllegalArgumentException(
                    "The number of rays must be positive");
        }
        this.raysPerPixel = raysPerPixel;
    }

    /**
     * Sets the scene's background color
     * 
     * @param  backgroundColor the {@code Color} to set as the background color
     *         for the scene, or {@code null} for the renderer's default
     *         background
     */
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Sets a custom camera configuration for this scene
     * 
     * @param  camera a {@code Camera} defining the camera parameters for this
     *         scene, or {@code null} for the renderer's default
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Sets a vector defining the "up" direction in the virtual world of this
     * scene
     * 
     * @param  upVector a {@code Vector} defining the "up" direction for this
     *         scene
     */
    public void setUpVector(Vector upVector) {
        if (upVector.length() == 0.0) {
            throw new IllegalArgumentException(
                    "The up vector cannot be a zero vector");
        } else {
            this.upVector = upVector;
        }
    }

    /**
     * Sets the field of view for this scene
     * 
     * @param  fieldOfView the {@code float} field of view, in degrees
     */
    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
    }
    
    /**
     * Adds a point light source to this scene
     * 
     * @param  light a {@code Light} representing a point light source in this
     *         scene; should not be {@code null}
     */
    public void addLight(Light light) {
        if (light == null) {
            throw new NullPointerException("The light may not be null.");
        } else {
            lights.add(light);
        }
    }
    
    /**
     * Adds an object to this scene
     * 
     * @param  object a {@code SceneObject} representing the object to add to
     *         the scene; should not be {@code null}
     */
    public void addObject(SceneObject object) {
        if (object == null) {
            throw new NullPointerException("The object may not be null.");
        } else {
            objects.add(object);
        }
    }

    /**
     * Writes an SCN-format representation of the scene defined by this
     * {@code ScnFile} to the specified writer.
     * 
     * @param  out the {@code Writer} to which data should be written
     * 
     * @throws IOException if an I/O error occurs while writing the SCN data
     */
    public void writeTo(Writer out) throws IOException {
        Formatter formatter = new Formatter(out, Locale.US);
        IOException exception;
        
        formatTo(formatter);
        formatter.flush();
        
        exception = formatter.ioException();
        if (exception != null) {
            throw exception;
        }
    }
    
    /**
     * Creates a SCN-format representation of the scene described by this
     * {@code ScnFile} with use of the specified {@code Formatter}
     * 
     * @param  formatter a {@code Formatter} with which to create the output
     */
    public void formatTo(Formatter formatter) {
        if (title != null) {
            formatter.format("title \"%s\"%n", title);
        }
        if (raysPerPixel != null) {
            formatter.format("raysperpixel %d%n", raysPerPixel);
        }
        if (this.backgroundColor != null) {
            formatColor("background", this.backgroundColor, formatter);
        }
        if (this.ambientLightColor != null) {
            formatColor("ambient", this.ambientLightColor, formatter);
        }
        if (this.upVector != null) {
            formatCoordinates("up", upVector, formatter);
        }
        if (this.camera != null) {
            camera.formatTo(formatter);
        }
        if (this.fieldOfView != null) {
            formatter.format("fieldofview %f%n", this.fieldOfView);
        }

        for (Light light : lights) {
            light.formatTo(formatter);
        }

        for (SceneObject object : objects) {
            object.formatTo(formatter);
        }
    }
    
    /**
     * A helper method that formats a set of three-dimensional coordinates with
     * use of the specified formatter
     * 
     * @param  label a label {@code String} to put in front of the coordinates;
     *         typically an attribute or directive keyword
     * @param  coords the coordinates to format as an
     *         {@code AbstractCoordinates}
     * @param  formatter the {@code Formatter} with which the coordinates should
     *         be formatted 
     */
    private static void formatCoordinates(String label,
            AbstractCoordinates coords, Formatter formatter) {
        double[] coordinates = coords.getCoordinates();
        
        formatter.format("%s (%10.6f, %10.6f, %10.6f)%n", label, coordinates[0],
                coordinates[1], coordinates[2]);
    }

    /**
     * A helper method that formats a color specification with use of the
     * specified formatter
     * 
     * @param  label a label {@code String} to put in front of the color;
     *         typically an attribute or directive keyword
     * @param  color the {@code Color} to format
     * @param  formatter the {@code Formatter} with which the color should
     *         be formatted 
     */
    private static void formatColor(String label, Color color,
            Formatter formatter) {
        float[] components = color.getRGBColorComponents(null);
        
        formatter.format("%s %10.6f, %10.6f, %10.6f%n", label, components[0],
                components[1], components[2]);
    }

    /**
     * A class representing the virtual camera defining the view of a virtual
     * world represented by a particular scene.  Instances correspond to
     * {@code lookat} instructions in an SCN file.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public static class Camera {
        
        /**
         * The position of this camera in the coordinate system of the virtual
         * world.
         */
        private final Point position;
    
        /**
         * The virtual position at which this camera is pointed, in the
         * coordinate system of the virtual world.
         */
        private final Point lookat;
    
        /**
         * The twist, in degrees, of the virtual camera about its line of site
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatTo() method anyway
         */
        private final Float twist;
        
        /**
         * Initializes a {@code Camera} with the specified parameters
         * 
         * @param  position a {@code Point} representing the position of this
         *         camera in the coordinate system of the virtual world
         * @param  lookAt a {@code Point} representing the position at which
         *         this camera is pointed, in the coordinate system of the
         *         virtual world
         * @param  twist the {@code float} twist, in degrees, of the virtual
         *         camera about its line of site
         */
        public Camera(Point position, Point lookAt, float twist) {
            if ((position == null) || (lookAt == null)) {
                throw new NullPointerException("null argument");
            }
            
            this.position = position;
            this.lookat = lookAt;
            this.twist = twist;
        }
        
        /**
         * Formats this camera information as an SCN {@code lookat} directive,
         * with use of the specified formatter
         * 
         * @param  formatter a {@code Formatter} with which to create the output
         */
        public void formatTo(Formatter formatter) {
            double[] viewpointCoords = position.getCoordinates();
            double[] lookatCoords = lookat.getCoordinates();
            
            formatter.format("lookat (%f, %f, %f, %f, %f, %f, %f)%n",
                    viewpointCoords[0], viewpointCoords[1], viewpointCoords[2],
                    lookatCoords[0], lookatCoords[1], lookatCoords[2],
                    twist);
        }
    }

    /**
     * Nested class that corresponds to a {@code light} directive in the
     * file.
     */
    public static class Light {
        
        /**
         * The color of this light
         */
        private final Color color;

        /**
         * The position of this light 
         */
        private final Point location;

        /**
         * Initializes a white {@code Light} at the specified position
         * 
         * @param  location a {@code Point} representing the position of the
         *         light
         */
        public Light(Point location) {
            this(Color.WHITE, location);
        }

        /**
         * Initializes a {@code Light} with the specified color and position
         * 
         * @param  color the {@code Color} of the light
         * @param  location a {@code Point} representing the position of the
         *         light
         */
        public Light(Color color, Point location) {
            if ((color == null) || (location == null)) {
                throw new NullPointerException("null argument");
            }
            this.color = color;
            this.location = location;
        }

        /**
         * Outputs a representation of this light in SCN format with use of the
         * specified {@code Formatter}
         * 
         * @param formatter the {@code Formatter} with which the light should be
         *        output
         */
        public void formatTo(Formatter formatter) {
            formatter.format("light {%n");
            ScnFile.formatColor("  color", color, formatter);
            ScnFile.formatCoordinates("  location", location, formatter);
            formatter.format("}%n");
        }
    }

    /**
     * A class representing the optical properties of a material, as used in the
     * SCN scene description format
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public static class Material {
        
        /**
         * The refractive index of the material
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatTo() method anyway
         */
        private final Float refractiveIndex;

        /**
         * The proportion of the material's reflectivity that produces diffuse
         * reflection
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatTo() method anyway
         */
        private final Float diffuseComponent;

        /**
         * The proportion of the material's reflectivity that produces specular
         * reflection
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatTo() method anyway
         */
        private final Float specularComponent;

        /**
         * The specular exponent of the material, which governs the size of the
         * specular hilight
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatTo() method anyway
         */
        private final Float specularExponent;

        /**
         * Initializes a {@code Material} with the specified parameters
         * 
         * @param  refractiveIndex the refractive index of the material as a
         *         {@code float}
         * @param  diffuseComponent the diffuse component of the material's
         *         reflectivity, as a {@code float}
         * @param  specularComponent the specular component of the material's
         *         reflectivity, as a {@code float}
         * @param  specularExponent the exponent of the specular component of
         *         the material's reflectivity, as a {@code float}
         */
        public Material(float refractiveIndex, float diffuseComponent,
                float specularComponent, float specularExponent) {
            this.refractiveIndex = refractiveIndex;
            this.diffuseComponent = diffuseComponent;
            this.specularComponent = specularComponent;
            this.specularExponent = specularExponent;
        }
        
       /**
        * Formats this material information as an SCN {@code material}
        * attribute, with use of the specified formatter
        * 
        * @param  formatter a {@code Formatter} with which to create the output
        */
        public void formatTo(Formatter formatter) {
            formatter.format("  material %f, %f, %f, %f%n", refractiveIndex,
                    diffuseComponent, specularComponent, specularExponent);
        }
    }
    
    /**
     * A nested base class for classes representing geometric privitive objects
     * in SCN files.
     */
    public static abstract class SceneObject {

        /**
         * The name of this object's type -- e.g. "sphere" or "cylinder" -- used
         * in creating the SCN-format representation of this object
         */
        private final String type; 
        
        /**
         * The color of this object
         */
        private Color color = null;

        /**
         * The material properties applicable to this object
         */
        private Material material = null;

        /**
         * A flag indicating whether or not this object casts shadows on the
         * other parts of the model
         */
        private boolean castsShadows = true;

        /**
         * Initializes a {@code SceneObject} with the specified type label; the
         * new object initially has no explicitly-specified color or material
         * properties, and is set to cast shadows
         * 
         * @param  type the name of this object's type, as a {@code String};
         *         used in formatting this object to specify what kind of object
         *         it is.  Values need to correspond to object types supported
         *         by the SCN format, such as "sphere" and "cylinder"
         */
        protected SceneObject(String type) {
            if (type == null) {
                throw new NullPointerException("The type must not be null");
            } else {
                this.type = type;
            }
        }
        
        /**
         * Sets whether or not this object should cast shadows on the rest of
         * the model
         * 
         * @param  castsShadows {@code true} if this object should cast shadows,
         *         {@code false} if it should not
         */
        public void setCastsShadows(boolean castsShadows) {
            this.castsShadows = castsShadows;
        }

        /**
         * Sets the color of this object
         * 
         * @param  color the {@code Color} of this object
         */
        public void setColor(Color color) {
            this.color = color;
        }

        /**
         * Sets the material properties of this object
         * 
         * @param  material a {@code Material} describing the material
         *         properties of this object
         */
        public void setMaterial(Material material) {
            this.material = material;
        }

        /**
         * Formats this object in SCN format, as a geometric object of the
         * specific type represented by this object.  This method produces the
         * openning and closing of an attribute block; the attributes, both
         * required and optional, are output via an invocation of
         * {@link #formatObjectAttributes(Formatter)}. 
         * 
         * @param  formatter a {@code Formatter} with which to create the output
         */
        public void formatTo(Formatter formatter) {
            formatter.format("%s {%n", type);
            formatObjectAttributes(formatter);
            formatter.format("}%n");
        }

        /**
         * Invoked by {@link #formatTo(Formatter)} to cause this object's
         * attributes to be output via the specified {@code Formatter}.  This
         * version outputs the attributes common to all scene objects;
         * subclasses should override it to output their own attributes, but
         * should not neglect to invoke this version to output the common
         * attributes as well.
         * 
         * @param  formatter the {@code Formatter} with which the attributes
         *         should be written
         */
        protected void formatObjectAttributes(Formatter formatter) {
            if (this.material != null) {
                material.formatTo(formatter);
            }
            formatter.format("  shadows %s%n", (castsShadows ? "on" : "off"));
            if (this.color != null) {
                formatColor("  color ", color, formatter);
            }
        }
    }

    /**
     * A nested class representing a @code sphere} object in an SCN file
     */
    public static class Sphere extends SceneObject {

        /**
         * The center of this sphere
         */
        private final Point center;

        /**
         * The radius of this sphere
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatObjectAttributes() method anyway
         */
        private final Float radius;

        /**
         * Initializes a {@code Sphere} with the specified center and radius
         * 
         * @param  center the center of the sphere as a {@code Point}
         * @param  radius the radius of the sphere as a {@code float}
         */
        public Sphere (Point center, float radius) {
            super("sphere");
            if (center == null) {
                throw new NullPointerException("null center");
            } else if (radius <= 0) {
                throw new IllegalArgumentException("nonpositive radius");
            }
            
            this.center = center;
            this.radius = radius;
        }

        /**
         * {@inheritDoc}.  This version outputs this sphere's center and radius
         * in addition to the common object attributes
         */
        @Override
        protected void formatObjectAttributes(Formatter formatter) {
            super.formatObjectAttributes(formatter);
            ScnFile.formatCoordinates("  center", center, formatter);
            formatter.format("  radius %f%n", radius);
        }
    }

    /**
     * A nested class representing a {@code cylinder} object in an SCN file
     */
    public static class Cylinder extends SceneObject {

        /**
         * The center of one face of this cylinder
         */
        private final Point center1;

        /**
         * The center of the other face of this cylinder
         */
        private final Point center2;

        /**
         * The radius of this cylinder
         */
        /*
         * as a Float wrapper object because it woukd otherwise be converted to
         * one in the formatObjectAttributes() method anyway
         */
        private final Float radius;

        /**
         * Initializes a {@code Cylinder} with the specified end centers and
         * radius
         * 
         * @param  center1 one center of the cylinder as a {@code Point}
         * @param  center2 the other center of the cylinder as a {@code Point}
         * @param  radius the radius of the cylinder as a {@code float}
         */
        public Cylinder(Point center1, Point center2, float radius) {
            super("cylinder");
            if ((center1 == null) || (center2 == null)) {
                throw new NullPointerException("Null argument");
            } else if (radius <= 0f) {
                throw new IllegalArgumentException(
                        "The radius must be positive");
            }
            this.center1 = center1;
            this.center2 = center2;
            this.radius = radius;
        }
        
        /**
         * {@inheritDoc}.  This version outputs this cylinder's two face centers
         * and its radius in addition to the common object attributes
         */
        @Override
        protected void formatObjectAttributes(Formatter formatter) {
            super.formatObjectAttributes(formatter);
            ScnFile.formatCoordinates("  center", center1, formatter);
            ScnFile.formatCoordinates("  center", center2, formatter);
            formatter.format("  radius %f%n", radius);
        }
    }
}
