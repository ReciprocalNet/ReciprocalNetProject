/*
 * Reciprocal Net Project
 *
 * Vector.java
 * 
 * 07-Nov-2005: jobollin adopted this class from existing IUMSC code
 */

package org.recipnet.common.geometry;

/**
 * A class representing vectors in 3-D Cartesian space.  Methods for several
 * vector arithmetic operations are provided, as well as a constructor for
 * forming the vector between two points (among others) and a method for
 * evaluating the images of Cartesian points.  Instances are immutable.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public final class Vector extends AbstractCoordinates {
    
    /*
     * Note: immutability of instances is, strictly speaking, a promise of the
     * package, not the class, because the AbstractCoordinates base class
     * provides package-private access to the internal coordinate array.  Client
     * code can never cause an existing Vector to change state, directly or
     * indirectly, but this class may sometimes create a new Vector (or Point)
     * and mutate it one or more times before handing it off to external code. 
     */

    /**
     * A {@code Vector} with all coordinates zero
     */
    public final static Vector ZERO = new Vector(0.0, 0.0, 0.0);
    
    /**
     * A cache of the length of this vector
     */
    private double length = Double.NaN;
    
    /**
     * Initializes a new {@code Vector} with all coordinates zero
     */
    private Vector() {
        this(0.0, 0.0, 0.0);
    }
    
    /**
     * Initializes a new {@code Vector} with the specified coordinates.  The
     * provided coordinate array is copied; no reference to it is retained by
     * the new object.
     * 
     * @param  coordinates a {@code double[]} of length 3 containing the
     *         coordinates for this {@code Vector}
     * 
     * @throws IllegalArgumentException if the length of the coordinate array is
     *         not 3
     */
    public Vector(double[] coordinates) {
        
        // Array length check provided by the superclass
        
        super(coordinates.clone());
    }

    /**
     * Initializes a new {@code Vector} with the specified coordinates
     * 
     * @param  x the x coordinate for this {@code Vector}
     * @param  y the y coordinate for this {@code Vector}
     * @param  z the z coordinate for this {@code Vector}
     */
    public Vector(double x, double y, double z) {
        super(new double[] {x, y, z} );
    }
    
    /**
     * Initializes a new {@code Vector} to represent the vector between the
     * specfied points
     * 
     * @param  from a {@code Point} representing the starting point by which to
     *         define this vector
     * @param  to a {@code Point} representing the ending point by which to
     *         define this vector
     */
    public Vector(Point from, Point to) {
        super(to.getCoordinates());
        
        // must not modify this array:
        double[] cFrom = from.getInternalCoordinates();
        
        // need to modify this array:
        double[] c = this.getInternalCoordinates();
        
        for (int i = 0; i < 3; i++) {
            c[i] -= cFrom[i];
        }
    }
    
    /**
     * Computes the Cartesian length of this vector
     * 
     * @return the length of this vector as a {@code double}
     */
    public double length() {
        if (Double.isNaN(length)) {
            length = Math.sqrt(this.dotProduct(this));
        }
        return length;
    }
    
    /**
     * Computes the scalar product of this vector with the specified scalar, and
     * returns the result as a new {@code Vector}
     * 
     * @param  factor the scalar by which to multiply this vector
     * 
     * @return the scalar product of this {@code Vector} with {@code factor}
     */
    public Vector scale(double factor) {
        if (factor == 0.0) {
            return Vector.ZERO;
        } else if (factor == 1.0) {
            return this;
        } else {
            Vector rval = new Vector(this.getInternalCoordinates());
            
            // need to modify this array:
            double[] c = rval.getInternalCoordinates();
            
            for (int i = 0; i < 3; i++) {
                c[i] *= factor;
            }
            
            return rval;
        }
    }
    
    /**
     * Returns a vector parallel to this one and having length 1.0
     * 
     * @return a {@code Vector} parallel to this vector and having length 1.0
     *         (or as close as reasonably possible)
     */
    public Vector normalize() {
        double len = this.length();
        
        /*
         * It is recognized that for general vectors, the exact floating-point
         * test below is unlikely to ever be true.  Nevertheless, it costs
         * almost nothing to perform the test as an optimization, and it *will*
         * be true for some special vectors, including, notably, unit vectors
         * along the coordinate axes. 
         */
        return (len == 1.0) ? this : this.scale(1.0 / len);
    }
    
    /**
     * Computes the dot (scalar) product of this vector with the specified one
     * 
     * @param  v the {@code Vector} with which to compute a dot product
     * 
     * @return the dot product of this {@code Vector} with {@code v}
     */
    public double dotProduct(Vector v) {
        
        // must not modify this array:
        double[] c1 = this.getInternalCoordinates();
        
        // must not modify this array:
        double[] c2 = v.getInternalCoordinates();
        
        return ((c1[0] * c2[0]) + (c1[1] * c2[1]) + (c1[2] * c2[2]));
    }
    
    /**
     * Computes the cross (vector) product of this vector with the specified one
     * 
     * @param  v the {@code Vector} with which to compute a cross product
     * 
     * @return a {@code Vector} representing the cross (vector) product of this
     *         {@code Vector} (left-hand factor) with {@code v} (right-hand
     *         factor)
     */
    public Vector crossProduct(Vector v) {
        Vector rval = new Vector();
        
        // must not modify this array:
        double[] c1 = this.getInternalCoordinates();
        
        // must not modify this array:
        double[] c2 = v.getInternalCoordinates();
        
        // need to modify this array:
        double[] c = rval.getInternalCoordinates();
        
        for (int i = 0; i < 3; i++) {
            int i1 = (i + 1) % 3;
            int i2 = (i + 2) % 3;
            
            c[i] = c1[i1] * c2[i2] - c1[i2] * c2[i1];
        }
        
        return rval;
    }
    
    /**
     * Computes the vector sum of this {@code Vector} with the specified one and
     * returns a {@code Vector} representing it
     * 
     * @param  v the {@code Vector} with which to compute a vector sum
     * 
     * @return a {@code Vector} representing the vector sum of this
     *         {@code Vector} with {@code v}
     */
    public Vector sum(Vector v) {
        Vector rval = new Vector(v.getInternalCoordinates());
        
        this.addTo(rval);
        
        return rval;
    }

    /**
     * The end point of this vector when its starting point is the specified one
     * 
     * @param  origin a {@code Point} representing the start point for which
     *         to compute the end point of this vector 
     * 
     * @return a {@code Point} representing the endpoint of this {@code Vector}
     *         when its origin is {@code origin}
     */
    public Point endPoint(Point origin) {
        Point rval = new Point(origin.getInternalCoordinates());
        
        this.addTo(rval);

        return rval;
    }
    
    /**
     * Adds the coordinates of this vector to those of the specified
     * {@code AbstractCoordinates}, thus modifying that object.  This is
     * a generalization of the vector sum and vector endpoint procedures, and
     * is intended only for use on cartesian entities that have not yet been
     * exposed outside this {@code Vector} instance (because clients are
     * promised that {@code Point}s and {@code Vector}s are immutable).
     * 
     * @param  ace the {@code AbstractCoordinates} to modify
     */
    private void addTo(AbstractCoordinates ace) {
        
        // need to modify this array:
        double[] c = ace.getInternalCoordinates();
        
        // must not modify this array:
        double[] c1 = this.getInternalCoordinates();

        for (int i = 0; i < 3; i++) {
            c[i] += c1[i];
        }
    }
    
    /**
     * Computes and returns the angle between this vector and the specified one
     * 
     * @param v the {@code Vector} with which to compute an angle
     * 
     * @return the angle, in radians, between this {@code Vector} and the
     *         specified one, in the range 0.0 through <em>&pi;</em>
     */
    public double angleWith(Vector v) {
        double cos = this.dotProduct(v) / (this.length() * v.length());
        
        return Math.acos(Math.min(1.0, Math.max(-1.0, cos)));
    }
}
