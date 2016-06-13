/*
 * Reciprocal Net Project
 *
 * SymmetryMatrix_TypeTests.java
 *
 * Dec 6, 2005: jobollin wrote first draft
 */
package org.recipnet.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author jobollin
 * @version 1.0
 *
 * TODO Write type description
 */
public class SymmetryMatrix_TypeTests extends TestCase {

    private final SymmetryMatrix.Type typeToTest;
    private final int expectedMult;
    
    public SymmetryMatrix_TypeTests(SymmetryMatrix.Type type, int mult) {
        super("test matrix type " + type.toString() + ":");
        
        this.typeToTest = type;
        this.expectedMult = mult;
    }
    
    public void runTest() throws Throwable {
        assertEquals("Wrong multiplicity", expectedMult,
                typeToTest.getMultiplicity());
    }
    
    public static Test suite() {
        TestSuite tests = new TestSuite("SymmetryMatrix.Type tests");
        
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.TRANSLATION, 2));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.INVERSION, 2));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.TWOFOLD, 2));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.REFLECTION, 2));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.THREEFOLD, 3));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.THREE_BAR, 6));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.FOURFOLD, 4));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.FOUR_BAR, 4));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.SIXFOLD, 6));
        tests.addTest(new SymmetryMatrix_TypeTests(
                SymmetryMatrix.Type.SIX_BAR, 6));
        return tests;
    }
}
