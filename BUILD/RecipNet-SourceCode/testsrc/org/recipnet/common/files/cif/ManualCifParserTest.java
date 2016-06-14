/*
 * IUMSC Reciprocal Net Project
 *
 * ManualCifParserTest.java
 *
 * Copyright (c) 2005 The Trustees of Indiana University.  All rights reserved.
 *
 * Sep 30, 2005: jobollin wrote first draft
 */
package org.recipnet.common.files.cif;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.CifFile.DataCell;
import org.recipnet.common.files.CifFile.DataLoop;
import org.recipnet.common.files.CifFile.SaveFrame;

/**
 * @author jobollin
 * @version 1.0
 *
 * TODO Write type description
 */
public class ManualCifParserTest {
    
    void performTest(String cifName) throws IOException {
        InputStream cifInput =
                new BufferedInputStream(new FileInputStream(cifName));
        CifParser parser = new CifParser();
        CifErrorRecorder recorder = new CifErrorRecorder();
        
        System.out.print("For CIF ");
        System.out.print(cifName);
        System.out.println(':');
        parser.setErrorHandler(recorder);
        try {
            CifFile result = parser.parseCif(cifInput);
            
            reportErrors(recorder);
            summarizeCif(result);
        } catch (CifParseException cpe) {
            System.err.println("Unhandled CIF parsing exception:");
            cpe.printStackTrace(System.err);
        }
        System.out.println();
    }
    
    /**
     * @param result
     * TODO Write method description
     */
    private void summarizeCif(CifFile result) {
        System.out.println("CIF content summary:");
        for (Iterator<DataBlock> blocks = result.blockIterator();
                blocks.hasNext(); ) {
            DataBlock block = blocks.next();
            
            System.out.print("Data block '");
            System.out.print(block.getName());
            System.out.println('\'');
            
            summarizeCell(block, "    ");
            for (Iterator<SaveFrame> frames = block.saveFrameIterator();
                    frames.hasNext(); ) {
                SaveFrame frame = frames.next();
                
                System.out.print("    Save frame '");
                System.out.print(frame.getName());
                System.out.println('\'');
                summarizeCell(frame, "        ");
            }
        }
        System.out.println("=== end of CIF content ===");
    }

    private void summarizeCell(DataCell cell, String indent) {
        List<String> allDataNames = new ArrayList<String>(cell.getDataNames());
        List<String> unloopedNames = new ArrayList<String>(allDataNames);
        List<String> loopedNames = new ArrayList<String>();

        for (String name : allDataNames) {
            if (cell.containsNameInLoop(name)) {
                unloopedNames.remove(name);
                loopedNames.add(name);
            }
        }
        System.out.print(indent);
        System.out.print("unlooped data names:");
        for (String name : unloopedNames) {
            System.out.print(' ');
            System.out.print(name);
        }
        System.out.println();
        while (loopedNames.size() > 0) {
            DataLoop loop = cell.getLoopForName(loopedNames.get(0));
            Collection<String> names = loop.getDataNames();

            loopedNames.removeAll(names);
            System.out.print(indent);
            System.out.print("loop:");
            for (String name : names) {
                System.out.print(' ');
                System.out.print(name);
            }
            System.out.println();
            System.out.print(indent);
            System.out.print("    ");
            System.out.print(loop.getRecordCount());
            System.out.println(" records");
        }
    }
    
    /**
     * @param recorder
     * TODO Write method description
     */
    private void reportErrors(CifErrorRecorder recorder) {
        System.out.println("CIF error summary:");
        for (CifError error : recorder.getErrors()) {
            System.out.print("    ");
            System.out.println(error.getMessage());
        }
        System.out.println("=== end of CIF errors ===");
    }

    /**
     * @param args
     * TODO Write method description
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        ManualCifParserTest tester = new ManualCifParserTest();
        
        for (String cif : args) {
            tester.performTest(cif);
        }
    }

}
