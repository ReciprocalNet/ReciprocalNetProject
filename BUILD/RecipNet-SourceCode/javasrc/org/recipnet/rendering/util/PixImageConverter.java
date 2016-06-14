/*
 * Reciprocal Net project
 * rendering software
 * 
 * PixImageConverter.java
 *
 * 13-Feb-2003: ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.recipnet.common.ProcessWrapper;

/**
 * Utility class that converts PIX-format 2-D images (as produced by programs
 * from the VORT library such as art and dart) to other industry-standard image
 * file formats like .PPM, .PCX, and .JPG. The current implementation invokes
 * external conversion programs on the command line; thus, its constructor
 * requires that these programs' locations be specified. This class is
 * thread-safe.
 */
public class PixImageConverter {
    private String vort2ppmFileName;

    private String vort2pcxFileName;

    private String cjpegFileName;

    /**
     * The one and only constructor.
     * 
     * @param vort2ppmFileName file name, optionally including a complete path,
     *        to the 'vort2ppm' utility from the VORT package.
     * @param vort2pcxFileName file name, optionally including a complete path,
     *        to the 'vort2pcx' utility from the VORT package.
     * @param cjpegFileName file name, optionally including a complete path, to
     *        the 'cjpeg' utility from the NETPBM package.
     */
    public PixImageConverter(String vort2ppmFileName, String vort2pcxFileName,
            String cjpegFileName) {
        this.vort2ppmFileName = vort2ppmFileName;
        this.vort2pcxFileName = vort2pcxFileName;
        this.cjpegFileName = cjpegFileName;
    }

    /**
     * Takes an existing .pix-format image passed by the caller in
     * {@code pixFileData} and returns a .ppm-format image. The current
     * implementation invokes 'vort2ppm' (from the VORT package) on the command
     * line to perform the conversion.
     * 
     * @throws IOException if the conversion failed.
     */
    public byte[] convertPixToPpm(byte pixFileData[]) throws IOException {
        /*
         * TODO: improve performance by doing this conversion in-process by
         * calling the VORT libraries via JNI.
         */
        ByteArrayOutputStream vort2ppmErrorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream vort2ppmOutputStream = new ByteArrayOutputStream();

        // Set up the vort2ppm process.
        Process proc = new ProcessWrapper(Runtime.getRuntime().exec(
                this.vort2ppmFileName), false, true, vort2ppmOutputStream,
                true, vort2ppmErrorStream);

        // Feed PIX data to the vort2ppm process.
        OutputStream os = proc.getOutputStream();
        os.write(pixFileData);
        os.close();

        // Wait for the process to finish.
        int vort2ppmExitCode;
        try {
            vort2ppmExitCode = proc.waitFor();
        } catch (InterruptedException ex) {
            throw new IOException("Conversion interrupted");
        }
        String vort2ppmErrorMessage = new String(
                vort2ppmErrorStream.toByteArray());
        if (vort2ppmExitCode != 0) {
            throw new IOException("vort2ppm returned " + vort2ppmExitCode
                    + ": '" + vort2ppmErrorMessage + "'");
        }

        return vort2ppmOutputStream.toByteArray();
    }

    /**
     * Takes an existing .pix-format image passed by the caller in
     * {@code pixFileData} and returns a .pcx-format image. The current
     * implementation invokes 'vort2pcx' (from the VORT package) on the command
     * line to perform the conversion.
     * 
     * @throws IOException if the conversion failed.
     */
    public byte[] convertPixToPcx(byte pixFileData[]) throws IOException {
        /*
         * TODO: improve performance by doing this conversion in-process by
         * calling the VORT libraries via JNI.
         */
        
        ByteArrayOutputStream vort2pcxErrorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream vort2pcxOutputStream = new ByteArrayOutputStream();

        // Set up the vort2pcx process.
        Process proc = new ProcessWrapper(Runtime.getRuntime().exec(
                this.vort2pcxFileName), false, true, vort2pcxOutputStream,
                true, vort2pcxErrorStream);

        // Feed PIX data to the vort2pcx process.
        OutputStream os = proc.getOutputStream();
        os.write(pixFileData);
        os.close();

        // Wait for the process to finish.
        int vort2pcxExitCode;
        try {
            vort2pcxExitCode = proc.waitFor();
        } catch (InterruptedException ex) {
            throw new IOException("Conversion interrupted");
        }
        String vort2pcxErrorMessage = new String(
                vort2pcxErrorStream.toByteArray());
        if (vort2pcxExitCode != 0) {
            throw new IOException("vort2pcx returned " + vort2pcxExitCode
                    + ": '" + vort2pcxErrorMessage + "'");
        }

        return vort2pcxOutputStream.toByteArray();
    }

    /**
     * Takes an existing .pix-format image passed by the caller in
     * {@code pixFileData} and returns a .jpg-format image. The lossy
     * compression ratio is determined by the {@code jpegQuality}
     * argument, where 0 is minimal quality and 100 is maximal quality. The
     * current implementation invokes 'vort2ppm' (from the VORT package) and
     * 'cjpeg' (from the NETPBM package) on the command line to perform the
     * conversion.
     */
    public byte[] convertPixToJpg(byte pixFileData[], int jpegQuality)
            throws IOException {
        /*
         * TODO: improve performance by doing this conversion in-process by
         * calling the VORT libraries via JNI.
         */
        
        // Simple validation.
        if (jpegQuality < 0 || jpegQuality > 100) {
            throw new IllegalArgumentException();
        }

        ByteArrayOutputStream vort2ppmErrorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream cjpegErrorStream = new ByteArrayOutputStream();
        ByteArrayOutputStream cjpegOutputStream = new ByteArrayOutputStream();

        // Set up the two processes so that vort2ppm pipes its output to
        // cjpeg and cjpeg's output is captured.
        String cjpegArguments[] = { this.cjpegFileName, "-quality",
                Integer.toString(jpegQuality) };
        Process cjpegProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                cjpegArguments), false, true, cjpegOutputStream, true,
                cjpegErrorStream);
        Process vort2ppmProcess = new ProcessWrapper(Runtime.getRuntime().exec(
                this.vort2ppmFileName), false, true,
                cjpegProcess.getOutputStream(), true, vort2ppmErrorStream);

        // Feed PIX data to the vort2ppm process.
        OutputStream os = vort2ppmProcess.getOutputStream();
        
        os.write(pixFileData);
        os.close();

        // Wait for the processes to finish.
        int vort2ppmExitCode;
        int cjpegExitCode;
        try {
            vort2ppmExitCode = vort2ppmProcess.waitFor();
            cjpegExitCode = cjpegProcess.waitFor();
        } catch (InterruptedException ex) {
            throw new IOException("Conversion interrupted");
        }
        String vort2ppmErrorMessage = new String(
                vort2ppmErrorStream.toByteArray());
        String cjpegErrorMessage = new String(cjpegErrorStream.toByteArray());
        if (vort2ppmExitCode != 0) {
            throw new IOException("vort2ppm returned " + vort2ppmExitCode
                    + ": '" + vort2ppmErrorMessage + "'");
        }
        if (cjpegExitCode != 0) {
            throw new IOException("cjpeg returned " + cjpegExitCode + ": '"
                    + cjpegErrorMessage + "'");
        }

        // Capture output generated by the processes.
        return cjpegOutputStream.toByteArray();
    }
}
