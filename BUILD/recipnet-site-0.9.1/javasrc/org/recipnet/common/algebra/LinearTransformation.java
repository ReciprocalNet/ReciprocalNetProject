/*
 * Reciprocal Net Project
 * 
 * LinearTransformation.java
 *
 * 22-Dec-2005: jobollin extracted this class from CrtFile
 */

package org.recipnet.common.algebra;

/**
 * A class representing a linear transformation; at this version it is
 * merely an immutable container for the constituent rotation and
 * translation components
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class LinearTransformation {
    
    /** This transformation's rotation matrix */
    private final SimpleMatrix rotation;
    
    /** This transformation's translation vector (in matrix form) */
    private final SimpleMatrix translation;
    
    /**
     * Initializes a {@code LinearTransformation} with the specified
     * rotation matrix and translation vector
     * 
     * @param  rotationMatrix a {@code SimpleMatrix} representing the
     *         rotation component of this transformation; should be square
     * @param  translationVector a {@code SimpleMatrix} representing the
     *         translation component of this transformation; should have
     *         exactly one column, and the same number of rows as the
     *         rotation matrix has (i.e. column matrix form)
     *         
     * @throws IllegalArgumentException if the arguments' dimensions are
     *         incorrect
     */
    public LinearTransformation(SimpleMatrix rotationMatrix,
            SimpleMatrix translationVector) {
        int dimension = rotationMatrix.getRowCount();
        
        if (rotationMatrix.getColumnCount() != dimension) {
            throw new IllegalArgumentException(
                    "Rotation Matrix is not square");
        } else if (translationVector.getRowCount() != dimension) {
            throw new IllegalArgumentException(
                    "Translation vector has the wrong length");
        } else if (translationVector.getColumnCount() != 1) {
            throw new IllegalArgumentException(
                    "Translation vector is not really a vector");
        }
        
        rotation = rotationMatrix;
        translation = translationVector;
    }

    /**
     * Returns a matrix representing the rotation component of this linear
     * transformation
     *  
     * @return the rotation component of this transformation, as a
     *         {@code SimpleMatrix}
     */
    public SimpleMatrix getRotation() {
        return rotation;
    }

    /**
     * Returns a matrix representing the translation component of this
     * linear transformation
     * 
     * @return the translation component of this transformation, as a
     *         {@code SimpleMatrix} containing exactly one column
     */
    public SimpleMatrix getTranslation() {
        return translation;
    }
    
    /**
     * Returns the dimension of the coordinate systems on which this
     * transformation operates
     * 
     * @return the dimension of the coordinate system on which this
     *         transformation is defined
     */
    public int getDimension() {
        return rotation.getRowCount();
    }
    
    /**
     * Transforms the specified coordinates according to this transformation
     * 
     * @param  coordinates a {@code double[]} containing the coordinates to
     *         transform; should have length equal to this transformation's
     *         dimension
     *         
     * @return a {@code double[]} of the same length as {@code coordinates},
     *         containing the transformed coordinates
     */
    public double[] transformPoint(double[] coordinates) {
        double[][] transformed = rotation.times(
                SimpleMatrix.createColumnMatrix(coordinates)).plus(
                        getTranslation()).getElements();
        
        return new double[] {transformed[0][0],
                transformed[1][0], transformed[2][0]};
    }
    
    /**
     * Given a target linear transformation <b><i>T</i></b> defined for the
     * coordinate system, <b>C</b>, that this transformation is defined for,
     * and taking this transformation as describing a change of
     * coordinates from <b>C</b> to an alternative coordinate system
     * <b>D</b>, returns a representation of <b><i>T</i></b>,
     * <b><i>T'</i></b>, referred to coordinate system <b>D</b>.  That is,
     * if this transformation is designated <b><i>R</i></b>, this method
     * returns a transformation <b><i>T'</i></b> such that
     * <b><i>T'</i></b>(<b><i>R</i></b>(<i>X</i>))&nbsp;=&nbsp;<b><i
     * >R</i></b>(<b><i>T</i></b>(<i>X</i>)) for all <i>X</i>.
     *  
     * @param  targetTransform a {@code LinearTransformation} to convert
     *          
     * @return a {@code LinearTransformation} representing the converted
     *         transform
     * 
     * @throws IllegalArgumentException if the target transform has a different
     *         dimension than this transformation
     */
    public LinearTransformation convertTransformation(
            LinearTransformation targetTransform) {
        
        if (targetTransform.getDimension()
                != this.getDimension()) {
            throw new IllegalArgumentException("Incompatible transforms");
        }
        
        SimpleMatrix coordRotation = this.getRotation();
        SimpleMatrix convertedRotation = coordRotation.times(
                targetTransform.getRotation()).times(
                        coordRotation.getInverse());
        SimpleMatrix convertedTranslation = convertedRotation.times(
                this.getTranslation()).times(-1);
        
        convertedTranslation = convertedTranslation.plus(
                coordRotation.times(targetTransform.getTranslation()));
        convertedTranslation = convertedTranslation.plus(
                this.getTranslation());
        
        return new LinearTransformation(
                convertedRotation, convertedTranslation);
    }
    
}