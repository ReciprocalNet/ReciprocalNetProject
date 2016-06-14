/*
 * Reciprocal Net Project
 *
 * TestPdbFile.java
 *
 * Dec 21, 2005: jobollin wrote first draft
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

import org.recipnet.common.molecule.Atom;

/**
 * A program for testing PDB file creation via the PdbFile class
 * 
 * @author jobollin
 * @version 1.0
 */
public class TestPdbFile {

    /**
     * The entry point to this program; reads the specified CRT, uses its
     * model to create a PdbFile, and uses the PdbFile to create PDB output to
     * the specified file (or standard output)
     * 
     * @param  args a {@code String[]} containing the program arguments; the
     *         first argument is expected to be the name of the CRT file to
     *         read, the second, if present, the name of the PDB file to which
     *         the output should be directed
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
                CrtFile<? extends Atom> crt = CrtFile.readFrom(r);
                PdbFile<? extends Atom> pdb
                        = new PdbFile(crt.getModel(), crt.getName());
                Writer w;
                
                if (args.length < 2) {
                    w = new OutputStreamWriter(
                            new BufferedOutputStream(System.out), "US-ASCII");
                } else {
                    w = new OutputStreamWriter(new BufferedOutputStream(
                            new FileOutputStream(args[1])), "US-ASCII");
                }

                try {
                    pdb.writeTo(w);
                } finally {
                    w.close();
                }
            } catch (ParseException pe) {
                System.err.println("CRT file could not be parsed:");
                pe.printStackTrace(System.err);
            } finally {
                r.close();
            }
        }
    }
}
