/*
 * Reciprocal Net Project
 * rendering software
 * 
 * Test.java
 * 
 * xx-Xxx-2004: eisiorho wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.recipnet.rendering.dispatcher.AbstractJobDispatcher;
import org.recipnet.rendering.dispatcher.ArtJob;
import org.recipnet.rendering.dispatcher.ComputeNode;
import org.recipnet.rendering.dispatcher.SimpleJobDispatcher;
import org.recipnet.rendering.util.PixImageConverter;

/**
 * A test program for the rendering server
 */
public class Test {
    public static void main(String args[]) throws IOException {
        // Create our set of available nodes.
        Set<ComputeNode> availableNodes = new TreeSet<ComputeNode>();
        availableNodes.add(new ComputeNode("node1", "node1"));
        availableNodes.add(new ComputeNode("node2", "node2"));
        availableNodes.add(new ComputeNode("node3", "node3"));

        // Instantiate the dispatcher.
        AbstractJobDispatcher dispatcher = new SimpleJobDispatcher(
                availableNodes, new File("/home/ekoperda/recipnet-rendering/"),
                false, 0, 5, 5);

        // Instantiate an image converter.
        PixImageConverter pixImageConverter = new PixImageConverter(
                "/home/ekoperda/recipnet-rendering/vort-jcb-1.2/tools/vort2ppm",
                "/home/ekoperda/recipnet-rendering/vort-jcb-1.2/tools/vort2pcx",
                "/usr/bin/cjpeg");

        // Read the scene input file.
        File scnFile = new File("/home/ekoperda/recipnet-rendering/eric.scn");
        FileInputStream fis = new FileInputStream(scnFile);
        byte scnFileData[] = new byte[fis.available()];
        fis.read(scnFileData);
        fis.close();

        // Instantiate the job.
        String cmdLineArgs[] = new String[1];
        cmdLineArgs[0] = "-n";
        ArtJob job = new ArtJob(ArtJob.RENDER_WITH_ART,
                "/home/ekoperda/recipnet-rendering/vort-jcb-1.2/art/src/art",
                cmdLineArgs, ArtJob.RENDER_TO_JPG, 1024, 1024, 100,
                scnFileData, pixImageConverter, 1);

        // Submit the job.
        System.out.println("Submitting job");
        dispatcher.doJob(job);

        System.out.println("Writing output file");
        File jpgFile = new File("/home/ekoperda/recipnet-rendering/eric.jpg");
        FileOutputStream fos = new FileOutputStream(jpgFile);
        fos.write(job.getFinalFileData());
        fos.close();

        System.out.println("Done");

        System.out.println(job.getPerfTimer().elapsed() + " milliseconds");
    }
}
