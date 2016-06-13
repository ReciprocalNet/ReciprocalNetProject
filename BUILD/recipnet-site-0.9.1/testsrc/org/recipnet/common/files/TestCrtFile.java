/*
 * Reciprocal Net Project
 *
 * TestCrtFile.java
 *
 * Dec 23, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Collections;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.Bond;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.MolecularModel;

/**
 * A program for use in manually testing the behavior of the CrtFile class
 *  
 * @author jobollin
 * @version 1.0
 */
public class TestCrtFile {

    /**
     * The program entry point
     * 
     * @param  args a {@code String[]} containing the arguments to the program
     * 
     * @throws IOException if an I/O error occurs 
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("-- no program arguments --");
        } else {
            Reader r = new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(args[0])), "US-ASCII");
            Writer w;
            
            try {
                CrtFile<? extends Atom> crt;
                
                if (args[0].endsWith(".sdt") || args[0].endsWith(".t21")) {
                    SdtFile<? extends FractionalAtom> sdt = SdtFile.readFrom(r);
                    MolecularModel<Atom, Bond<Atom>> model
                            = new MolecularModel<Atom, Bond<Atom>>(
                                    sdt.getModel().getAtoms(),
                                    Collections.<Bond<Atom>>emptySet());
                    
                    model.setCell(sdt.getModel().getCell());
                    for (SymmetryMatrix matrix
                            : sdt.getModel().getSymmetryOperations()) {
                        model.addSymmetryOperation(matrix);
                    }
                    
                    crt = new CrtFile<Atom>(model);
                } else {
                    crt = CrtFile.readFrom(r);
                }
                
                if (args.length > 1) {
                    w = new OutputStreamWriter(new BufferedOutputStream(
                            new FileOutputStream(args[1])), "US-ASCII");
                    
                } else {
                    w = new OutputStreamWriter(
                            new BufferedOutputStream(System.out), "US-ASCII");
                }
                
                try {
                    crt.writeTo(w);
                } finally {
                    w.close();
                }
            } catch (ParseException pe) {
                System.err.println("SDT file could not be parsed:");
                pe.printStackTrace(System.err);
            } finally {
                r.close();
            }
        }
    }

}
