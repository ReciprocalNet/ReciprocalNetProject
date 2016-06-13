/*
 * Reciprocal Net Project
 *
 * TestOrtepInputMaker.java
 *
 * Jan 5, 2006: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.OrtFile;
import org.recipnet.common.files.SdtFile;
import org.recipnet.common.files.cif.CifParseException;
import org.recipnet.common.files.cif.CifParser;
import org.recipnet.common.molecule.Atom;
import org.recipnet.common.molecule.FractionalAtom;

/**
 * A program for manually testing the {@code OrtepInputMaker} class
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class TestOrtepInputMaker {

    /**
     * The program entry point
     * 
     * @param args the program arguments. The first is interpreted as the name
     *        of a CRT file from which the model to be rendered will be read;
     *        the second is interpreted as the name of an SDT or CIF file
     *        containing the crystallographic coordinates and thermal
     *        parameters; the third parameter is interpreted as the name of an
     *        output file if it does not contain an equals character ('='); and
     *        remaining arguments are interpreted as the key/value pairs for an
     *        {@code ImageParameters} object
     * @throws IOException if an I/O error occurs
     * @throws CifParseException if a parse error occurs while attempting to
     *         read model data from a CIF
     */
    public static void main(String[] args)
            throws IOException, CifParseException {
        if (args.length < 1) {
            System.err.println("-- no program arguments --");
        } else if (args.length < 2) {
            System.err.println("-- no SDT or CIF file specified --");
        } else {
            Reader crtReader = new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(args[0])), "US-ASCII");
            
            try {
                CrtFile<? extends Atom> crt = CrtFile.readFrom(crtReader);
                Reader baseModelReader = new InputStreamReader(
                        new BufferedInputStream(
                                new FileInputStream(args[1])), "US-ASCII");
                
                try {
                    OrtepInputMaker maker;
                    
                    if (args[1].endsWith(".cif")) {
                        CifFile cif = new CifParser().parseCif(baseModelReader);
                        
                        maker = OrtepInputMaker.forCIF(
                                cif.blockIterator().next());
                    } else {
                        SdtFile<FractionalAtom> sdt
                                = SdtFile.readFrom(baseModelReader);
                    
                        maker = OrtepInputMaker.forSDT(sdt);
                    }
                    
                    int parameterIndex
                            = ((args.length < 3) || (args[2].indexOf('=') >= 0))
                                    ? 2 : 3;
                    ImageParameters parameters = makeImageParameters(
                            args, parameterIndex);
                    OrtFile instructions
                            = maker.createOrtepInstructions(crt.getModel());
                    Writer w;
                    
                    if (parameterIndex == 2) {
                        w = new OutputStreamWriter(
                                new BufferedOutputStream(System.out),
                                "US-ASCII");
                    } else {
                        w = new OutputStreamWriter(new BufferedOutputStream(
                                new FileOutputStream(args[2])), "US-ASCII");
                    }
    
                    try {
                        maker.appendOrtepInput(w, instructions, parameters);
                    } finally {
                        w.close();
                    }
                } finally {
                    baseModelReader.close();
                }
            } catch (ParseException pe) {
                System.err.println("CRT file could not be parsed:");
                pe.printStackTrace(System.err);
            } finally {
                crtReader.close();
            }
        }
    }

    /**
     * Extracts parameter names and values from the specified argument list and
     * uses them to initialize an {@code ImageParameters} object
     * 
     * @param  args a {@code String[]} from which the parameters are to be
     *         drawn
     * @param  parameterIndex the index of the first element of {@code args}
     *         that should be interpreted as a parameter specification
     *         
     * @return an {@code ImageParameters} object representing the parameters
     *         specified among the arguments
     * 
     * @throws IndexOutOfBoundsException if {@code parameterIndex} is less than
     *         zero
     */
    private static ImageParameters makeImageParameters(String[] args,
            int parameterIndex) {
        Map<String, String[]> paramMap = new HashMap<String, String[]>();
        
        for (int i = parameterIndex; i < args.length; i++) {
            String[] split = args[i].split(
                    "(?:(?<=[^=]*)=)"
                    + "|(?:(?<=.*=.*),)",
                    -1);
            String[] values = new String[split.length - 1];
            
            System.arraycopy(split, 1, values, 0, values.length);
            paramMap.put(split[0], values);
        }
        
        try {
            return new ImageParameters(null, paramMap);
        } catch (MalformedURLException mue) {
            throw new RuntimeException(mue);
        }
    }
}
