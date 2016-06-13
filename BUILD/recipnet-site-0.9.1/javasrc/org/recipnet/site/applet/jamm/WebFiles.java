/*
 * Reciprocal Net Project
 *
 * @(#)WebFiles.java
 *
 * 16-Oct-2002: jobollin factored most of getData out into a more general
 *              static method on Rotate3DModel as part of task 510
 * 20-Nov-2002: jobollin added an import statement for Rotate3DModel
 * 20-Feb-2003: jobollin removed all reliance on Java 2 classes and created a
 *              version of getTable that accepts an InputStream from which to
 *              obtain the data (part of task #682)
 * 24-Feb-2003: jobollin reformatted the source and extended the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm package to
 *              org.recipnet.site.applet.jamm; changed package references to
 *              match source tree reorganization
 * 06-Jan-2005: jobollin removed usage of the deprecated StreamTokenizer
 *              constructor
 */

package org.recipnet.site.applet.jamm;
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.net.URL;
import java.net.URLConnection;
import org.recipnet.site.applet.jamm.Rotate3DModel;
import org.recipnet.site.wrapper.FileFormatException;

/**
 * A class containing static methods for obtaining information from specific
 * types of files addressable and accessible via URL
 */
public class WebFiles {

    /**
     * Creates a <code>Rotate3DModel</code> from the <code>URL</code> specified
     * by <code>modelURL</code>
     *
     * @param  modelURL a <code>URL</code> from which to read the model data
     * @param  outStream a<code>PrintStream</code> to which any messages
     *         produced should be written
     *
     * @return a <code>Rotate3DModel</code> constructed from the data obtained
     *         from <code>modelURL</code>
     */
    public static Rotate3DModel getData(URL modelURL, PrintStream outStream) {
        Rotate3DModel mod = null;
        Reader reader = null;

        try {
            URLConnection uc = modelURL.openConnection();

            uc.setAllowUserInteraction(true);
            uc.setDoInput(true);
            uc.setDoOutput(false);
            uc.setUseCaches(false);
            uc.connect();

            reader = new InputStreamReader(
                    new BufferedInputStream(uc.getInputStream()), "US-ASCII");
            mod = Rotate3DModel.readModel(reader);
        } catch (FileFormatException e) {
            outStream.println("Error encountered on CRT file: "
                + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace(outStream);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // discard it
                }
            }
        }
        return mod;
    }

    /**
     * Builds a color table and a radius table from the data specified by
     * <code>tableURL</code>, and returns them in a <code>TableContainer</code>
     *
     * @param  tableURL the <code>URL</code> from which to obtain the color and
     *         radius data
     * @param  outStream a<code>PrintStream</code> to which any messages
     *         produced should be written
     *
     * @return a <code>TableContainer</code> containing the element data
     *         obtained from <code>tableURL</code>
     */
    public static TableContainer getTable(URL tableURL, PrintStream outStream) {
        try {
            InputStream is = tableURL.openStream();
            TableContainer tc = getTable(is, outStream);

            is.close();
            return tc;
        } catch (IOException ioe) {
            outStream.println(ioe.getMessage());
            return null;
        }
    }

    /**
     * Builds a color table and a radius table from the data on <code>is</code>,
     * and returns them in a <code>TableContainer</code>
     *
     * @param  is the <code>InputStream</code> from which to obtain the color
     *         and radius data
     * @param  outStream a<code>PrintStream</code> to which any messages
     *         produced should be written
     *
     * @return a <code>TableContainer</code> containing the element data
     *         obtained from <code>tableURL</code>
     */
    public static TableContainer getTable(InputStream is, PrintStream outStream) {
        TableContainer tc;
        Reader reader = null;

        try {
            reader = new InputStreamReader(new BufferedInputStream(is),
                                           "US-ASCII");

            StreamTokenizer st = new StreamTokenizer(reader);

            st.nextToken();
            tc = new TableContainer((int) st.nval + 1);

            while (st.nextToken() != StreamTokenizer.TT_EOF) {
                int index = (int) st.nval;
                float r = 0f;
                float g = 0f;
                float b = 0f;
                float rad = 0f;

                if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                    r = (float) st.nval;
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        g = (float) st.nval;
                        if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                            b = (float) st.nval;
                        }
                    }
                    if (st.nextToken() == StreamTokenizer.TT_NUMBER) {
                        rad = (float) st.nval;
                    }
                }
                tc.colorTab[index] = new Color(r, g, b);
                tc.radii[index] = rad;
            }
        } catch (IOException e) {
            outStream.println("IO Exception: " + e.getMessage());
            tc = null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    // discard it
                }
            }
        }

        return tc;
    }
}
