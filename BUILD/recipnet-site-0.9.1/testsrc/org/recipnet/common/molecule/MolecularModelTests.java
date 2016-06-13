/*
 * Reciprocal Net Project
 *
 * MolecularModelTests.java
 *
 * Dec 19, 2005: jobollin wrote first draft
 */
package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.recipnet.common.Element;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.geometry.CoordinateSystem;
import org.recipnet.common.geometry.Point;

/**
 * A JUnit {@code TestCase} that exercises the behavior of the
 * {@code MolecularModel} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class MolecularModelTests extends TestCase {

    /**
     * Initializes a {@code MolecularModelTests} to run the named test
     * 
     * @param  testName the name of the test to run
     */
    public MolecularModelTests(String testName) {
        super(testName);
    }

    /**
     * Tests the behavior of the nullary constructor; it should create an
     * empty model with no cell or symmetry operations
     */
    public void testConstructor() {
        MolecularModel<?, ?> model = new MolecularModel<Atom, Bond<Atom>>();

        assertTrue("The model has initial atoms", model.getAtoms().isEmpty());
        assertTrue("The model has initial bonds", model.getBonds().isEmpty());
        assertNull("The model has an initial cell", model.getCell());
        assertTrue("The model has initial symmetry",
                model.getSymmetryOperations().isEmpty());
    }

    /**
     * Tests the normal operation of the binary constructor
     */
    public void testConstructor__Collection_Collection() {
        List<Atom> atoms = new ArrayList<Atom>();
        Set<Bond<Atom>> bonds = new HashSet<Bond<Atom>>();
        MolecularModel<Atom, Bond<Atom>> model;
        
        atoms.add(new Atom("Atom1", Element.CARBON, 0, 0, 0, null));
        atoms.add(new Atom("Atom2", Element.CARBON, 0, 0, 0, null));
        atoms.add(new Atom("Atom3", Element.CARBON, 0, 0, 0, null));
        atoms.add(new Atom("Atom4", Element.CARBON, 0, 0, 0, null));
        atoms.add(new Atom("Atom5", Element.CARBON, 0, 0, 0, null));
        bonds.add(new Bond<Atom>(atoms.get(0), atoms.get(1)));
        bonds.add(new Bond<Atom>(atoms.get(0), atoms.get(2)));
        bonds.add(new Bond<Atom>(atoms.get(0), atoms.get(3)));
        bonds.add(new Bond<Atom>(atoms.get(1), atoms.get(4)));
        bonds.add(new Bond<Atom>(atoms.get(2), atoms.get(3)));
        
        model = new MolecularModel<Atom, Bond<Atom>>(atoms, bonds);
        assertEquals("The model has the wrong initial atoms (or the right "
                + "ones in the wrong order)", atoms,
                new ArrayList<Atom>(model.getAtoms()));
        assertEquals("The model has the wrong number of initial bonds",
                bonds.size(), model.getBonds().size());
        assertTrue("The model has the wrong initial bonds",
                model.getBonds().containsAll(bonds));
        assertNull("The model has an initial cell", model.getCell());
        assertTrue("The model has initial symmetry",
                model.getSymmetryOperations().isEmpty());
    }

    /**
     * Tests the behavior of the binary constructor when its first argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__nullCollection_Collection() {
        try {
            new MolecularModel<Atom, Bond<Atom>>(
                    null, new ArrayList<Bond<Atom>>());
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the behavior of the binary constructor when its second argument is
     * {@code null}; a {@code NullPointerException} is expected
     */
    public void testConstructor__Collection_nullCollection() {
        try {
            new MolecularModel<Atom, Bond<Atom>>(
                    new ArrayList<Atom>(), null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior as the {@code addAtom(Atom)} method; the
     * atoms added via this method should be present in the same sequence in
     * the model's atom list
     */
    public void testFeature__addAtom__getAtoms() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        List<Atom> atoms = new ArrayList<Atom>();
        Atom atom;
        
        atom = new Atom("Atom 1", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        assertEquals("Atoms were not added correctly", atoms, model.getAtoms());
        
        atom = new Atom("Atom 2", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        assertEquals("Atoms were not added correctly", atoms, model.getAtoms());
        
        atom = new Atom("Atom 3", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        assertEquals("Atoms were not added correctly", atoms, model.getAtoms());
        
        atom = new Atom("Atom 4", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        assertEquals("Atoms were not added correctly", atoms, model.getAtoms());
        
        atom = new Atom("Atom 5", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        assertEquals("Atoms were not added correctly", atoms, model.getAtoms());
    }

    /**
     * Tests the behavior of the {@code addAtom(Atom)} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__addAtom_nullAtom() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        
        try {
            model.addAtom(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code addBond(Bond)} method; all bonds
     * added to the model should be subsequently found in the model's
     * bond collection 
     */
    public void testFeature__addBond__getBonds() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        List<Atom> atoms = new ArrayList<Atom>();
        Atom atom;
        List<Bond<Atom>> bonds = new ArrayList<Bond<Atom>>();
        Bond<Atom> bond;
        
        atom = new Atom("Atom 1", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        atom = new Atom("Atom 2", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        atom = new Atom("Atom 3", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        atom = new Atom("Atom 4", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        atom = new Atom("Atom 5", Element.CARBON, 0, 0, 0, null);
        model.addAtom(atom);
        atoms.add(atom);
        
        bond = new Bond<Atom>(atoms.get(0), atoms.get(1));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
        
        bond = new Bond<Atom>(atoms.get(0), atoms.get(2));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
        
        bond = new Bond<Atom>(atoms.get(0), atoms.get(2));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
        
        bond = new Bond<Atom>(atoms.get(2), atoms.get(3));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
        
        bond = new Bond<Atom>(atoms.get(2), atoms.get(4));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
        
        bond = new Bond<Atom>(atoms.get(3), atoms.get(4));
        model.addBond(bond);
        bonds.add(bond);
        assertEquals("Wrong number of bonds", bonds.size(),
                model.getBonds().size());
        assertTrue("Wrong bonds", bonds.containsAll(model.getBonds()));
    }

    /**
     * Tests the behavior of the {@code addBond(Bond)} method when its argument
     * is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__addBond_nullBond() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        
        try {
            model.addBond(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code setCell()} method; the
     * CoordinateSystem set on a model should be subsequently retrieved from
     * it, including if it is null 
     */
    public void testFeature__getCell__setCell() {
        MolecularModel<?, ?> model = new MolecularModel<Atom, Bond<Atom>>();
        CoordinateSystem system = new CoordinateSystem(5, 6, 7, .05, .1, .15);
        
        model.setCell(system);
        assertTrue("The unit cell was not correctly set",
                system == model.getCell());
        model.setCell(null);
        assertNull("The unit cell was not correctly set", model.getCell());
    }

    /**
     * Tests the normal behavior as the
     * {@code addSymmetryOperation(SymmetryMatrix)} method; the SymmetryMatrix
     * operations added via this method should be present in the same sequence
     * in the model's symmetry operation list
     */
    public void testFeature__addSymmetryOperation__getSymmetryOperations() {
        MolecularModel<?, ?> model = new MolecularModel<Atom, Bond<Atom>>();
        List<SymmetryMatrix> symmetry = new ArrayList<SymmetryMatrix>();
        SymmetryMatrix symop;
        
        symop = new SymmetryMatrix(new int[] {2, 3, 4});
        model.addSymmetryOperation(symop);
        symmetry.add(symop);
        assertEquals("Symmetry operations were not added correctly",
                symmetry, model.getSymmetryOperations());
        
        symop = new SymmetryMatrix(new int[] {3, 4, 6});
        model.addSymmetryOperation(symop);
        symmetry.add(symop);
        assertEquals("Symmetry operations were not added correctly",
                symmetry, model.getSymmetryOperations());
        
        symop = new SymmetryMatrix(new int[] {2, 4, 8});
        model.addSymmetryOperation(symop);
        symmetry.add(symop);
        assertEquals("Symmetry operations were not added correctly",
                symmetry, model.getSymmetryOperations());
        
        symop = new SymmetryMatrix(new int[] {3, 6, 9});
        model.addSymmetryOperation(symop);
        symmetry.add(symop);
        assertEquals("Symmetry operations were not added correctly",
                symmetry, model.getSymmetryOperations());
    }

    /**
     * Tests the behavior of the {@code addSymmetryOperation(SymmetryMatrix)}
     * method when its argument is {@code null}; a {@code NullPointerException}
     * is expected
     */
    public void testMethod__addSymmetryOperation_nullSymmetryMatrix() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        
        try {
            model.addSymmetryOperation(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }
    
    /**
     * Tests the normal behavior of the {@code computeRadius(Point)} method 
     */
    public void testMethod__computeRadius_Point() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        Point center = new Point(3, 5, 7);
        Point p;
        double radius = 0;
        
        p = center;
        model.addAtom(new Atom("Atom1", Element.CARBON, p, null));
        
        p = new Point(9, 2, 11);
        model.addAtom(new Atom("Atom2", Element.CARBON, p, null));
        radius = Math.max(radius, center.distanceTo(p));
        
        p = new Point(-2, 2, 5);
        model.addAtom(new Atom("Atom3", Element.CARBON, p, null));
        radius = Math.max(radius, center.distanceTo(p));
        
        p = new Point(5, 17, 11);
        model.addAtom(new Atom("Atom4", Element.CARBON, p, null));
        radius = Math.max(radius, center.distanceTo(p));
        
        p = new Point(4, 3, 2);
        model.addAtom(new Atom("Atom5", Element.CARBON, p, null));
        radius = Math.max(radius, center.distanceTo(p));
        
        p = new Point(15, -1, 6);
        model.addAtom(new Atom("Atom6", Element.CARBON, p, null));
        radius = Math.max(radius, center.distanceTo(p));
        
        assertEquals("Wrong radius computed", radius,
                model.computeRadius(center));
    }
    
    /**
     * Tests the behavior of the {@code computeRadius(Point)} method when its
     * argument is {@code null}; a {@code NullPointerException} is expected
     */
    public void testMethod__computeRadius_nullPoint() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        
        model.addAtom(new Atom("Atom1", Element.CARBON, 0, 0, 0, null));
        try {
            model.addAtom(null);
            fail("Expected a NullPointerException");
        } catch (NullPointerException npe) {
            // The expected case
        }
    }

    /**
     * Tests the normal behavior of the {@code getCentroid()} method
     */
    public void testMethod__computeCentroid() {
        MolecularModel<Atom, Bond<Atom>> model
                = new MolecularModel<Atom, Bond<Atom>>();
        Point center = new Point(3, 5, 7);
        List<Point> points = new ArrayList<Point>();
        Point p;
        
        p = center;
        model.addAtom(new Atom("Atom1", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
        
        p = new Point(9, 2, 11);
        model.addAtom(new Atom("Atom2", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
        
        p = new Point(-2, 2, 5);
        model.addAtom(new Atom("Atom3", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
        
        p = new Point(5, 17, 11);
        model.addAtom(new Atom("Atom4", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
        
        p = new Point(4, 3, 2);
        model.addAtom(new Atom("Atom5", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
        
        p = new Point(15, -1, 6);
        model.addAtom(new Atom("Atom6", Element.CARBON, p, null));
        points.add(p);
        assertEquals("Wrong centroid computed", 0,
                model.computeCentroid().distanceTo(
                        Point.midPoint(points.toArray(new Point[0]))),
                1e-10);
    }
    
    /**
     * Creates and returns a {@code Test} suitable for running all the tests
     * defined by this class
     * 
     * @return a {@code Test} representing all of the tests defined by this
     *         class
     */
    public static Test suite() {
        TestSuite tests = new TestSuite(MolecularModelTests.class);
        
        tests.setName("MolecularModel Tests");
        
        return tests;
    }
}
