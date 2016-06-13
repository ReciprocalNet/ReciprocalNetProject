/*
 * Reciprocal Net Project
 *
 * TestOrtFile.java
 *
 * Dec 14, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.Formatter;
import java.util.Locale;

import org.recipnet.common.files.ortep.OrtepInstructionCard;

/**
 * A driver class exercising the OrtFile class for testing purposes
 * 
 * @author jobollin
 * @version 1.0
 */
public class TestOrtFile {

    /**
     * TODO Write method description
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("-- no program arguments --");
        } else {
            Reader r = new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(args[0])), "US-ASCII");
            
            try {
                OrtFile ort = OrtFile.readFrom(r);
                
                printOrtFile(ort, System.out);
                
                if (args.length < 2) {
                    System.out.println("-- no output file specified --");
                } else {
                    Writer w = new OutputStreamWriter(new BufferedOutputStream(
                            new FileOutputStream(args[1])), "US-ASCII");
                    
                    try {
                        ort.writeTo(w);
                    } finally {
                        w.close();
                    }
                }
            } catch (ParseException pe) {
                System.err.println("ORT file could not be parsed:");
                pe.printStackTrace(System.err);
            } finally {
                r.close();
            }
        }
    }

    /**
     * TODO Write method description
     * 
     * @param ort
     * @param out 
     */
    private static void printOrtFile(OrtFile ort, PrintStream out) {
        StringBuilder sb = new StringBuilder(72);
        int index = 0;
        
        for (OrtepInstructionCard card : ort.getInstructions()) {
            Formatter f = new Formatter(sb, Locale.US);
            
            out.print("card type: ");
            out.println(card.getCardType());
            card.formatTo(f, index++ % 4);
            f.close();
            out.println(sb.toString());
            sb.setLength(0);
            out.println("=====");
        }
    }

}
