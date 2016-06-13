/*
 * Reciprocal Net Project
 *
 * TestModelBuilder.java
 *
 * Dec 22, 2005: jobollin wrote first draft
 */

package org.recipnet.common.molecule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.CifParseException;
import org.recipnet.common.files.cif.CifParser;
import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.applet.jamm.TableContainer;
import org.recipnet.site.applet.jamm.WebFiles;
import org.recipnet.site.applet.jamm.jamm2.Rotate3DPanel;

/**
 * An application that tests the various ModelBuilder options on a specified
 * CIF
 * 
 * @author jobollin
 * @version 1.0
 */
public class TestModelBuilder {

    /**
     * The program entry point
     * 
     * @param  args a {@code String[]} containing the program arguments; the
     *         first argument is taken as the name of the CIF to read 
     *         
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("-- no program arguments --");
        } else {
            InputStream is
                    = new BufferedInputStream(new FileInputStream(args[0]));
            CifParser parser = new CifParser();
            
            try {
                CifFile cif = parser.parseCif(is);
                Iterator<DataBlock> blocks = cif.blockIterator();
                
                if (blocks.hasNext()) {
                    ModelBuilder builder = new ModelBuilder(blocks.next());
                    List<Rotate3DModel> models = new ArrayList<Rotate3DModel>();
                    List<String> modelTitles = new ArrayList<String>();
                    MolecularModel<FractionalAtom, Bond<FractionalAtom>> model
                            = builder.buildSimpleModel();
                    String modelName = args[0].replaceAll("\\.cif", "");
                    Color[] atomColors;
                    
                    models.add(create3DModel(model));
                    modelTitles.add("Simple Model");
                    writeModel(model, modelName + "simple.crt");
                    
                    model = builder.buildGrownModel(
                            Collections.<String>singletonList(
                                    model.getAtoms().get(0).getLabel().replaceAll("[() \t]+", "")));
                    models.add(create3DModel(model));
                    modelTitles.add("Grown Model (default seed)");
                    writeModel(model, modelName + "grown.crt");
                    
                    model = builder.buildPackedModel(
                            new double[] {1, 1, 1}, new double[] {0.5, 0.5, 0.5});
                    models.add(create3DModel(model));
                    modelTitles.add("Packed Model (One unit cell)");
                    writeModel(model, modelName + "packed.crt");
                    
                    if (args.length > 1) {
                        InputStream tableIn = new BufferedInputStream(
                                new FileInputStream(args[1]));
                        
                        try {
                            TableContainer tc
                                    = WebFiles.getTable(tableIn, System.err);
                            
                            atomColors = tc.colorTab;
                        } finally {
                            tableIn.close();
                        }
                    } else {
                        atomColors = null;
                    }
                    displayResult(models, modelTitles, atomColors);
                } else {
                    System.err.println("No data blocks");
                }
            } catch (CifParseException cpe) {
                cpe.printStackTrace();
            } finally {
                is.close();
            }
        }
    }

    /**
     * Writes the specified model to a file of the specified name in CRT format
     * 
     * @param  model the {@code MolecularModel} to write
     * @param  name the name of the file to write
     * 
     * @throws IOException if an I/O error occurs 
     */
    private static void writeModel(
            MolecularModel<FractionalAtom, Bond<FractionalAtom>> model,
            String name) throws IOException {
        Writer out = new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(name)));
        
        try {
            CrtFile<?> crt = new CrtFile<FractionalAtom>(model);
            
            crt.writeTo(out);
        } finally {
            out.close();
        }
    }

    /**
     * Displays the specified 3D models using the specified atom colors
     * 
     * @param  models a {@code List&lt;Rotate3DModel&gt;} of the models to
     *         display
     * @param  titles a {@code List&lt;String&gt;} of the titles corresponding
     *         to the models 
     * @param  atomColors a {@code Color[]} of the colors to use for each
     *         element
     */
    private static void displayResult(List<Rotate3DModel> models,
            List<String> titles, Color[] atomColors) {
        JFrame frame = new JFrame("Constructed models");
        JDesktopPane desktop = new JDesktopPane();
        
        frame.setPreferredSize(new Dimension(1208, 900));
        frame.setResizable(true);
        frame.setContentPane(desktop);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        desktop.setBackground(Color.BLACK);
        
        for (int i = 0; i < models.size(); i++) {
            JInternalFrame modelFrame = new JInternalFrame(titles.get(i));
            Rotate3DPanel panel = new Rotate3DPanel(models.get(i), atomColors);
            
            panel.setBackground(new Color(191, 191, 191));
            modelFrame.setBackground(desktop.getBackground());
            modelFrame.getContentPane().setLayout(new BorderLayout());
            modelFrame.getContentPane().add(panel, BorderLayout.CENTER);
            modelFrame.setSize(new Dimension(400, 400));
            modelFrame.setLocation(400 * (i % 3), 400 * (i / 3));
            modelFrame.setVisible(true);
            desktop.add(modelFrame);
        }
        
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates a {@code Rotate3DModel} from a {@code MolecularModel}
     * 
     * @param  fromModel the {@code MolecularModel} from which to create a
     *         {@code Rotate3DModel}
     * @param  <A> a parameter characterizing the type of atoms in the input
     *         molecular model
     *         
     * @return a {@code Rotate3DModel} containing atoms and bonds corresponding
     *         to those of the input molecular model
     */
    private static <A extends Atom> Rotate3DModel create3DModel(
            MolecularModel<A, ? extends Bond<? extends A>> fromModel) {
        Rotate3DModel toModel = new Rotate3DModel();
        Map<Atom, Integer> atomNumberMap = new HashMap<Atom, Integer>();
        int index = 0;
        
        for (Atom atom : fromModel.getAtoms()) {
            double[] coords = atom.getPosition().getCoordinates();
            
            toModel.addVert((float) coords[0], (float) coords[1],
                    (float) coords[2], atom.getElement().getAtomicNumber(),
                    atom.getLabel());
            atomNumberMap.put(atom, index++);
        }
        for (Bond<? extends A> bond : fromModel.getBonds()) {
            toModel.addCon(atomNumberMap.get(bond.getAtom1()),
                    atomNumberMap.get(bond.getAtom2()));
        }
        
        return toModel;
    }
}
