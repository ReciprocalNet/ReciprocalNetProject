/*
 * Reciprocal Net Project
 *
 * TestSdtFile.java
 *
 * Dec 21, 2005: jobollin wrote first draft
 */
package org.recipnet.common.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.geometry.Vector;
import org.recipnet.common.molecule.AtomicMotion;
import org.recipnet.common.molecule.FractionalAtom;

/**
 * A testing program that reads an SDT file via the {@code SdtFile} class and
 * writes a dump of the resulting molecular model to the standard output
 *   
 * @author jobollin
 * @version 1.0
 */
public class TestSdtFile {

    /**
     * The entry point to this program; reads the specified SDT and dispatches
     * it to {@link #printSdtFile(SdtFile, OutputStream)} for printing to the
     * standard output
     * 
     * @param  args a {@code String[]} containing the program arguments; the
     *         first argument is expected to be the name of the SDT file to
     *         read
     *         
     * @throws IOException if an I/O error occurs 
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("-- no program arguments --");
        } else {
            Reader r = new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(args[0])), "US-ASCII");
            
            try {
                SdtFile<? extends FractionalAtom> sdt = SdtFile.readFrom(r);
                
                printSdtFile(sdt, new BufferedOutputStream(System.out));
            } catch (ParseException pe) {
                System.err.println("SDT file could not be parsed:");
                pe.printStackTrace(System.err);
            } finally {
                r.close();
            }
        }
    }

    /**
     * Prints the title and atom information from the specified {@code SdtFile}
     * to the specified output stream (using the platform's default encoding).
     * 
     * @param  sdt an {@code SdtFile } containing the SDT data to print
     * @param  out the {@code OutputStream} to which the output should be
     *         directed
     */
    private static void printSdtFile(SdtFile<? extends FractionalAtom> sdt,
            OutputStream out) {
        Formatter formatter
                = new Formatter(new OutputStreamWriter(out), Locale.US);
        Vector[] cellVectors = sdt.getModel().getCell().getVectors();
        
        formatter.format("Title: %s%n%n", sdt.getTitle());
        formatter.format("Unit Cell:%n");
        formatter.format("%10.4f%10.4f%10.4f  %10.6f%10.6f%10.6f%n%n",
                cellVectors[0].length(),
                cellVectors[1].length(),
                cellVectors[2].length(),
                Math.cos(cellVectors[1].angleWith(cellVectors[2])),
                Math.cos(cellVectors[0].angleWith(cellVectors[2])),
                Math.cos(cellVectors[0].angleWith(cellVectors[1]))
                );
        formatter.format("Symmetry:%n");
        for (SymmetryMatrix symm : sdt.getModel().getSymmetryOperations()) {
            formatSymmetry(formatter, symm);
        }
        formatter.format("%nAtoms:%n");
        formatter.format("%10s%4s%5s%8s%8s     %8s%8s%8s%8s%8s%8s%n",
                "label", "El", "x", "y", "z", "U11", "U22", "U33", "U23", "U13",
                "U12");
        for (FractionalAtom atom : sdt.getModel().getAtoms()) {
            double[] coords = atom.getFractionalCoordinates();
            AtomicMotion motion = atom.getAtomicMotion();
            
            formatter.format("%10s%4s%8.4f%8.4f%8.4f    ", atom.getLabel(),
                    atom.getElement().getSymbol(),
                    coords[0], coords[1], coords[2]);
            if (motion == null) {
                formatter.format("[no displacement parameters]%n"); 
            } else if (motion.isAnisotropic()) {
                double[][] anisoU = motion.getAnisotropicU();
                
                formatter.format("%8.5f%8.5f%8.5f%8.5f%8.5f%8.5f%n",
                        anisoU[0][0], anisoU[1][1], anisoU[2][2],
                        anisoU[2][1], anisoU[2][0], anisoU[1][0]);
            } else {
                formatter.format("%8.5f%n", motion.getIsotropicU());
            }
        }
        
        formatter.flush();
    }

    /**
     * A helper method that outputs a formatted version of the specified
     * symmetry matrix on the provided {@code Formatter}
     * 
     * @param  formatter a {@code Formatter} with which to format the output
     * @param  symm the {@code SymmetryMatrix} to format
     */
    private static void formatSymmetry(Formatter formatter, SymmetryMatrix symm) {
        int[][] rotationElements = symm.getRotationMatrix();
        int[] translations = symm.getTranslationVector();
        
        for (int i = 0; i < 3; i++) {
            formatter.format("%15.10f%3d%3d%3d",
                    translations[i] / 12.0, rotationElements[i][0],
                    rotationElements[i][1], rotationElements[i][2]);
        }
        formatter.format("%n");
    }
}
