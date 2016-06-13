/*
 * Reciprocal Net project
 * rendering software
 * 
 * ArtJob.java
 *
 * 10-Feb-2003: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.dispatcher;

import java.io.File;
import java.io.IOException;

import org.recipnet.common.PerfTimer;
import org.recipnet.rendering.util.PixImageConverter;

/**
 * <p>
 * Subclass of {@code AbstractJob} that invokes either 'art' or 'dart',
 * ray-tracing engines from VORT (a Very Ordinary Rendering Toolkit), to convert
 * a 3-D scene description to a 2-D image. Art is the non-distributed version of
 * the program that executes entirely on the local computer, while dart is
 * capable of spawning itself onto other nodes in a computing cluster.
 * </p><p>
 * The 3-D scene description must be in the .scn file format defined by VORT.
 * The 2-D output image may be in either .pix format (defined by VORT), or .ppm,
 * .pcx, or .jpg format (industry standards).
 * </p>
 */
public class ArtJob extends AbstractJob {
    public static final int RENDER_WITH_ART = 1;

    public static final int RENDER_WITH_DART = 2;

    public static final int RENDER_TO_PIX = 1;

    public static final int RENDER_TO_PPM = 2;

    public static final int RENDER_TO_PCX = 3;

    public static final int RENDER_TO_JPG = 4;

    // Populated by the caller at construction time.
    private int programToUse;

    private String executableFileName;

    private String commandLineOptions[];

    private int outputFormat;

    private int imageSizeX;

    private int imageSizeY;

    private int jpegQuality;

    private byte scnFileData[];

    private PixImageConverter pixImageConverter;

    // Populated during setBaseDirectory():
    public File scnFile;

    public File hostsFile;

    public File pixFile;

    public File logFile;

    // Populated during readOutputFiles():
    private byte pixFileData[];

    private byte finalFileData[];

    /**
     * The one and only constructor. The cost of this job is estimated to be
     * {@code scnFileData.length} times {@code imageSizeX} times
     * {@code imageSizeY}.
     * 
     * @param programToUse specifies which ray-tracing program should be used to
     *        render the 3-D scene. Possible choices are {@code RENDER_WITH_ART}
     *        and {@code RENDER_WITH_DART}.
     * @param executableFileName the name (and path, optionally) to either the
     *        'art' or 'dart' executable file, as appropriate. This process is
     *        invoked when the dispatcher executes this job object.
     * @param commandLineOptions an array of zero or more strings that should be
     *        included in the command line that's passed to either 'art' or
     *        'dart'.
     * @param outputFormat specifies the format of the final 2-D image. Possible
     *        values are {@code RENDER_TO_PIX}, {@code RENDER_TO_PPM},
     *        {@code RENDER_TO_PCX}, and {@code RENDER_TO_JPG}.
     * @param imageSizeX the number of pixels in width the rendered 2-D image
     *        should be. Must be greater than 0.
     * @param imageSizeY the number of pixels in height the rendered 2-D image
     *        should be. Must be greater than 0.
     * @param jpegQuality ignored unless {@code outputFormat} is
     *        {@code RENDER_TO_JPG}. Specifies the degree of lossy compression
     *        the JPEG image should employ: 0 indicates highest compression
     *        (least quality) and 100 indicates lowest compression (highest
     *        quality).
     * @param scnFileData an array of bytes that are the contents of a valid
     *        .scn 3-D scene description file. The caller must not modify the
     *        contents of this array during this object's lifetime.
     * @param pixImageConverter an initialized {@code PixImageConverter} object
     *        that this object should utilize to convert 2-D images from one
     *        format to another.
     * @param priority priority that should be assigned to this job, as defined
     *        by {@code AbstractJob}.
     * @throws IllegalArgumentException
     */
    public ArtJob(int programToUse, String executableFileName,
            String commandLineOptions[], int outputFormat, int imageSizeX,
            int imageSizeY, int jpegQuality, byte scnFileData[],
            PixImageConverter pixImageConverter, int priority) {
        super(priority, (long) scnFileData.length * imageSizeX * imageSizeY);
        if ((programToUse != RENDER_WITH_ART) && (programToUse != RENDER_WITH_DART)) {
            throw new IllegalArgumentException();
        }
        this.programToUse = programToUse;
        this.executableFileName = executableFileName;
        this.commandLineOptions = commandLineOptions;
        if ((outputFormat != RENDER_TO_PIX) && (outputFormat != RENDER_TO_PPM)
                && (outputFormat != RENDER_TO_PCX)
                && (outputFormat != RENDER_TO_JPG)) {
            throw new IllegalArgumentException();
        }
        this.outputFormat = outputFormat;
        if (imageSizeX <= 0) {
            throw new IllegalArgumentException();
        }
        this.imageSizeX = imageSizeX;
        if (imageSizeY <= 0) {
            throw new IllegalArgumentException();
        }
        this.imageSizeY = imageSizeY;
        if ((outputFormat != RENDER_TO_JPG)
                && ((jpegQuality < 0) || (jpegQuality > 100))) {
            throw new IllegalArgumentException();
        }
        this.jpegQuality = jpegQuality;
        this.scnFileData = scnFileData;
        this.pixImageConverter = pixImageConverter;

        this.scnFile = null;
        this.hostsFile = null;
        this.pixFile = null;
        this.logFile = null;
        this.pixFileData = null;
        this.finalFileData = null;
    }

    /**
     * After this job object has been sent to a dispatcher and executed, this
     * function returns the resulting 2-D image in the requested format.
     * 
     * @throws IllegalStateException if the job has not been rendered yet and
     *         thus no image is available.
     */
    public byte[] getFinalFileData() {
        if (finalFileData == null) {
            throw new IllegalStateException();
        }
        return finalFileData;
    }

    /** Called by the dispatcher. Overrides from {@code AbstractJob}. */
    @Override
    void setBaseDirectory(File baseDirectory) {
        super.setBaseDirectory(baseDirectory);
        this.scnFile = new File(baseDirectory, "input.scn");
        this.hostsFile = new File(baseDirectory, "input.hostlist");
        this.pixFile = new File(baseDirectory, "output.pix");
        this.logFile = new File(baseDirectory, "output.log");
    }

    /** Called by the dispatcher. Overrides from {@code AbstractJob}. */
    @Override
    String[] getInvocationInfo() {
        // Sanity checking.
        if ((this.scnFile == null) || (this.hostsFile == null)
                || (this.pixFile == null)) {
            throw new IllegalStateException();
        }

        String args[];
        switch (this.programToUse) {
            case RENDER_WITH_DART:
                args = new String[8 + this.commandLineOptions.length];
                args[0] = this.executableFileName;
                for (int i = 0; i < this.commandLineOptions.length; i++) {
                    args[1 + i] = this.commandLineOptions[i];
                }
                args[1 + this.commandLineOptions.length] = "-h";
                args[2 + this.commandLineOptions.length] = this.hostsFile.getPath();
                args[3 + this.commandLineOptions.length] = "-o";
                args[4 + this.commandLineOptions.length] = this.pixFile.getPath();
                args[5 + this.commandLineOptions.length] = this.scnFile.getPath();
                args[6 + this.commandLineOptions.length] = String.valueOf(this.imageSizeX);
                args[7 + this.commandLineOptions.length] = String.valueOf(this.imageSizeY);
                break;
            case RENDER_WITH_ART:
                args = new String[6 + this.commandLineOptions.length];
                args[0] = this.executableFileName;
                args[1] = this.scnFile.getPath();
                args[2] = String.valueOf(this.imageSizeX);
                args[3] = String.valueOf(this.imageSizeY);
                args[4] = "-o";
                args[5] = this.pixFile.getPath();
                for (int i = 0; i < this.commandLineOptions.length; i++) {
                    args[6 + i] = this.commandLineOptions[i];
                }
                break;
            default:
                throw new IllegalStateException();
        }

        return args;
    }

    /** Called by the dispatcher. Overrides from {@code AbstractJob}. */
    @Override
    void writeInputFiles(@SuppressWarnings("unused") PerfTimer timer)
            throws IOException {
        // Sanity checking.
        if (this.scnFile == null) {
            throw new IllegalStateException();
        }

        // Create the scene file that art/dart will render.
        super.bytesToFile(this.scnFile, this.scnFileData);

        // Create the hosts file that will tell dart which compute nodes to
        // utilize. This file will be ignored by art if art is invoked.
        StringBuilder hostsFileData = new StringBuilder();
        for (ComputeNode node : super.nodesAssignedTo) {
            hostsFileData.append(node.networkName);
            hostsFileData.append(" ");
            hostsFileData.append(Long.toString(node.speedFactor));
            hostsFileData.append("\n");
        }
        super.bytesToFile(this.hostsFile, hostsFileData.toString().getBytes());
    }

    /** Called by the dispatcher. Overrides from {@code AbstractJob}. */
    @Override
    void readOutputFiles(PerfTimer timer) throws IOException {
        // Sanity checking.
        if (pixFile == null) {
            throw new IllegalStateException();
        }

        // Read the .pix file that art/dart generated for us.
        this.pixFileData = super.bytesFromFile(pixFile);

        // Possibly convert the .pix file to an alternate format, as specified
        // when this object was created.
        timer.newChild("image format conversion");
        switch (outputFormat) {
            case RENDER_TO_PIX:
                this.finalFileData = this.pixFileData;
                // Nothing more to do... PIX is art's native format.
                break;
            case RENDER_TO_PPM:
                this.finalFileData = pixImageConverter.convertPixToPpm(this.pixFileData);
                break;
            case RENDER_TO_PCX:
                this.finalFileData = pixImageConverter.convertPixToPcx(this.pixFileData);
                break;
            case RENDER_TO_JPG:
                this.finalFileData = pixImageConverter.convertPixToJpg(
                        this.pixFileData, jpegQuality);
                break;
        }
        timer.stopChild();
    }

    /** Called by the dispatcher. Overrides from {@code AbstractJob}. */
    @Override
    void deleteFiles(@SuppressWarnings("unused")
    PerfTimer timer) throws IOException {
        // Sanity checking.
        if ((scnFile == null) || (hostsFile == null) || (pixFile == null)) {
            throw new IllegalStateException();
        }

        // Delete all temporary files.
        boolean rc = scnFile.delete();
        if (!rc) {
            throw new IOException("Could not delete " + scnFile.getPath());
        }
        rc = hostsFile.delete();
        if (!rc) {
            throw new IOException("Could not delete " + hostsFile.getPath());
        }
        rc = pixFile.delete();
        if (!rc) {
            throw new IOException("Could not delete " + pixFile.getPath());
        }
        rc = logFile.delete();
        if (!rc) {
            throw new IOException("Could not delete " + logFile.getPath());
        }
    }
}
