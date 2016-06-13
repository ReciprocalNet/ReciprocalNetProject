/*
 * Reciprocal Net Project
 *  
 * JammSupportLogEvent.java
 *
 * 25-Jul-2003: jrhanna wrote first draft
 * 12-Aug-2003: midurbin added GS_TIMEOUT, GS_ORT_TIMEOUT, RENDER_TIMEOUT and
 *              RENDER_EXCEPTION reason codes and changed ORTEP_INTERUPTED to
 *              ORTEP_EXCEPTION
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 06-Dec-2005: jobollin added generic reason codes CRT_ERROR and
 *              ORTEP_NO_ATOMIC_PARAMETERS; added ORTEP-specific reason codes
 *              ORTEP_CIF_ERROR, ORTEP_EMPTY_CIF, and ORTEP_INADEQUATE_CIF;
 *              deprecated ORTEP_SAVE_STUB_ERROR, and performed some
 *              reformatting
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used to record JammSupport events. The constructors are
 * overloaded to account for multiple types of {@code JammSupport }
 * failures as well as success log events.
 */
public class JammSupportLogEvent extends LogEvent {

    /*
     * Generic reason codes follow:
     */

    /**
     * Warning: No child process path specified in the initialization parameters
     * of ServletConfig.
     */
    public static final int NO_CHILD_PATH = 1;

    /** Warning: no file tracker obtained */
    public static final int NO_FILE_TRACKER = 2;

    /**
     * Malformed url parameters, either no function was specified or the
     * sampleId/sampleHistoryId was invalid/missing";
     */
    public static final int URL_ERROR = 3;

    /** Ticket expired */
    public static final int TICKET_ERROR = 4;

    /**
     * Request dispatcher error: cannot forward to one of the following
     * jammline, jammortep, jammrender
     */
    public static final int REQUEST_DISPATCHER_ERROR = 5;

    /**
     * CRT-reading error: could not parse the specified .crt file into a CrtFile
     * object. All of JammSupport's functions rely on reading a CRT, so this is
     * a general error code
     */
    public static final int CRT_ERROR = 6;

    /*
     * ORTEP sdt/stub reason codes follow:
     */

    /** Encountered exception while retrieving SDT, could not generate ORTEP */
    public static final int ORTEP_SDT_ERROR = 10;

    /** Encountered an exception while retrieving ORTEP stub file */
    public static final int ORTEP_STUB_ERROR = 11;

    /**
     * Encountered an exception trying to create an ORTEP input stub
     * 
     * @deprecated {@code JammSupport} no longer produces this error
     */
    @Deprecated
    public static final int ORTEP_CREATE_STUB_ERROR = 12;

    /** Encountered an exception trying to create an ORTEP input file */
    public static final int ORTEP_INPUT_ERROR = 13;

    /**
     * Could not save new ORTEP stub
     * 
     * @deprecated {@code JammSupport} no longer saves ortep stub files
     */
    @Deprecated
    public static final int ORTEP_SAVE_STUB_ERROR = 14;

    /** ORTEP failed with code: */
    public static final int ORTEP_FAIL_CODE = 15;

    /**
     * Could not generate ORTEP image, ORTEP was interrupted prior to completion
     */
    public static final int ORTEP_EXCEPTION = 16;

    /**
     * No atomic parameter file (SDT or CIF at this version) suitable for
     * creating an ORTEP input file could be found.
     */
    public static final int ORTEP_NO_ATOMIC_PARAMETERS = 17;
    
    /** Encountered exception while retrieving CIF, could not generate ORTEP */
    public static final int ORTEP_CIF_ERROR = 18;
    
    /**
     * Attempted to use CIF atomic parameters, but the CIF file had no data
     * block
     */
    public static final int ORTEP_EMPTY_CIF = 19;
    
    /**
     * Attempted to use CIF atomic parameters, but the CIF file's first data
     * block had no bond list
     */
    public static final int ORTEP_INADEQUATE_CIF = 110;

    /*
     * GhostScript reason codes follow:
     */

    /** Encountered an exception trying to create PostScript */
    public static final int GS_EXCEPTION_ERROR = 20;

    /** Linedraw error */
    public static final int GS_LINEDRAW_ERROR = 21;

    /** Ghostscript interupted */
    public static final int GS_INTERUPTED = 22;

    /** Ghostscript failed to complete in allotted time */
    public static final int GS_TIMEOUT = 23;

    /** Ghostscript job (ortep rendering) was interupted */
    public static final int GS_ORT_INTERUPTED = 24;

    /**
     * Ghostscript job (ortep rendering) failed to complete in allotted time
     */
    public static final int GS_ORT_TIMEOUT = 25;

    /** Linedraw bounding box calculation returned code: */
    public static final int GS_CALCULATION = 26;

    /** GS linedraw returned code: */
    public static final int GS_RETURN_CODE = 27;

    /*
     * JammRender reason codes follow:
     */

    /** Recipnet renderclient exited with code: */
    public static final int RENDER_EXIT_CODE = 30;

    /** Recipnet renderclient was interupted */
    public static final int RENDER_INTERUPTED = 31;

    /** Recipnet renderclient failed to complete in allotted time */
    public static final int RENDER_TIMEOUT = 32;

    /**
     * An IOException was thrown while reading from or writing to Recipnet
     * renderclient.
     */
    public static final int RENDER_EXCEPTION = 33;

    /*
     * SceneMaker reason codes follow:
     */

    /** Encountered an exception trying to generate a scene description */
    public static final int SCENE_MAKER_ERROR = 40;

    /**
     * The success constructor
     * 
     * @param  crtName The name of the crt file used there are multiple crt
     *         files.
     * @param  imageType The type of image rendering requested, as specified to
     *         JammSupport.
     * @param  params The parameters used in rendering the current sample.
     * @param  sampleId The id of the sample Jamm is currently rendering.
     * @param  sampleHistoryId The history id of the sample Jamm is currently
     *         rendering.
     */
    public JammSupportLogEvent(String crtName, String imageType, String params,
            int sampleId, int sampleHistoryId) {
        super.createLogRecord(Level.INFO, "crt file {0} was displayed using"
                + " a/an {1} for sample {2}.", new Object[] { crtName,
                imageType, Integer.valueOf(sampleId) }, null);
    }

    /**
     * Constructor for failures during the init() call of JammSupport where the
     * servlet parameters have not been initialized.
     * 
     * @param  reasonCode Valid reason codes for this constructor are:
     *         {@code NO_CHILD_PATH}, {@code NO_FILE_TRACKER}.
     * @throws IllegalArgumentException if the reasonCode submitted is not valid
     */
    public JammSupportLogEvent(int reasonCode) {
        switch (reasonCode) {
            case NO_CHILD_PATH:
                super.createLogRecord(Level.INFO, "No child process"
                        + " path specified in the initialization parameters"
                        + " of ServletConfig.", new Object[] {}, null);
                break;
            case NO_FILE_TRACKER:
                super.createLogRecord(Level.INFO, "No file tracker obtained,"
                        + " in the initialization parameters of ServletConfig.",
                        new Object[] {}, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor for failures in external programs where an exit code is
     * available.
     * 
     * @param  reasonCode Valid reason codes for this constructor are:
     *         {@code ORTEP_FAIL_CODE}, {@code GS_CALCULATION},
     *         {@code GS_RETURN_CODE}, {@code RENDER_EXIT_CODE}
     * @param  crtName The name of the crt file used there are multiple crt
     *         files.
     * @param  imageType The type of image rendering requested, as specified to
     *         JammSupport.
     * @param  params The parameters used in rendering the current sample.
     * @param  sampleId The id of the sample Jamm is currently rendering.
     * @param  sampleHistoryId The history id of the sample Jamm is currently
     *         rendering.
     * @param  exitCode The exit code for external programs.
     * 
     * @throws IllegalArgumentException if the reasonCode submitted is not valid
     */
    public JammSupportLogEvent(int reasonCode, String crtName,
            String imageType, String params, int sampleId, int sampleHistoryId,
            int exitCode) {
        switch (reasonCode) {
            case ORTEP_FAIL_CODE:
                super.createLogRecord(Level.WARNING,
                        "ORTEP failed with code: {0}"
                                + ", while attempting {1} image display, for"
                                + " sample {2} using crt file {3}.",
                        new Object[] { Integer.valueOf(exitCode), imageType,
                                Integer.valueOf(sampleId), crtName }, null);
                break;
            case GS_CALCULATION:
                super.createLogRecord(Level.INFO, "Linedraw bounding box"
                        + " calculation returned code: {0}, while"
                        + " attempting {1} image display, for sample"
                        + " {2} using crt file {3}", new Object[] {
                        Integer.valueOf(exitCode), imageType,
                        Integer.valueOf(sampleId), crtName }, null);
                break;
            case GS_RETURN_CODE:
                super.createLogRecord(Level.INFO, "GS linedraw returned"
                        + " code: {0}, while attempting {1} image display,"
                        + " for sample {2} using crt file {3}.", new Object[] {
                        Integer.valueOf(exitCode), imageType,
                        Integer.valueOf(sampleId), crtName }, null);
                break;
            case RENDER_EXIT_CODE:
                super.createLogRecord(Level.WARNING, "Recipnet renderclient"
                        + " exited with code: {0}, while attempting {1} image"
                        + " display, for sample {2} using crt file {3}.",
                        new Object[] { Integer.valueOf(exitCode), imageType,
                                Integer.valueOf(sampleId), crtName }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Generic failure/warning constructor for use when no other constructor is
     * more appropriate.
     * 
     * @param  reasonCode Valid reason codes for this constructor are:
     *         {@code URL_ERROR}, {@code TICKET_ERROR},
     *         {@code ORTEP_NO_ATOMIC_PARAMETERS}, {@code ORTEP_EMPTY_CIF},
     *         {@code ORTEP_INADEQUATE_CIF}
     *         {@code REQUEST_DISPATCHER_ERROR}, {@code GS_LINEDRAW_ERROR}.
     * @param  crtName The name of the crt file used there are multiple crt
     *         files.
     * @param  imageType The type of image rendering requested, as specified to
     *         JammSupport.
     * @param  params The parameters used in rendering the current sample.
     * @param  sampleId The id of the sample Jamm is currently rendering.
     * @param  sampleHistoryId The history id of the sample Jamm is currently
     *         rendering.
     *            
     * @throws IllegalArgumentException if the reasonCode submitted is not valid
     */
    public JammSupportLogEvent(int reasonCode, String crtName, String imageType,
            String params, int sampleId, int sampleHistoryId) {
        switch (reasonCode) {
            case URL_ERROR:
                super.createLogRecord(Level.WARNING, "Malformed url"
                        + " parameters, either no function was specified or"
                        + " the sampleId/sampleHistoryId was invalid or"
                        + " missing, while attempting {0} image"
                        + " display, for sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, null);
                break;
            case TICKET_ERROR:
                super.createLogRecord(Level.INFO, "ticket expired,"
                        + " while attempting {0} image" + " display, for"
                        + " sample {1} using crt file {2}.", new Object[] {
                        imageType, Integer.valueOf(sampleId), crtName }, null);
                break;
            case REQUEST_DISPATCHER_ERROR:
                super.createLogRecord(Level.WARNING, "Request dispatcher error:"
                        + " cannot forward to one of the following"
                        + " jammline, jammortep, jammrender, while"
                        + " attempting {0} image" + " display, for"
                        + " sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, null);
                break;
            case ORTEP_NO_ATOMIC_PARAMETERS:
                super.createLogRecord(Level.INFO, "Could not locate atomic "
                        + "parameter file for {0} image rendering, for sample "
                        + "{1} (history ID {3}), using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                        crtName, Integer.valueOf(sampleHistoryId) }, null);
                break;
            case ORTEP_EMPTY_CIF:
                super.createLogRecord(Level.INFO, "CIF chosen for {0} image "
                        + "rendering contained no data blocks, for sample "
                        + "{1} (history ID {3}), using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                        crtName, Integer.valueOf(sampleHistoryId) }, null);
                break;
            case ORTEP_INADEQUATE_CIF:
                super.createLogRecord(Level.INFO, "CIF chosen for {0} image "
                        + "rendering did not contain enough information in its "
                        + "first data block to define the base model, for "
                        + "sample {1} (history ID {3}), using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                        crtName, Integer.valueOf(sampleHistoryId) }, null);
                break;
            case GS_LINEDRAW_ERROR:
                super.createLogRecord(Level.WARNING, "Linedraw error, while"
                        + " attempting {0} image display, for sample {1}"
                        + " using crt file {2}.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Constructor for JammSupport failures with exceptions
     * 
     * @param  reasonCode Valid reason codes for this constructor are:
     *         {@code CRT_ERROR}, {@code ORTEP_SDT_ERROR},
     *         {@code ORTEP_STUB_ERROR}, {@code ORTEP_CREATE_STUB_ERROR},
     *         {@code ORTEP_INPUT_ERROR}, {@code ORTEP_SAVE_STUB_ERROR},
     *         {@code ORTEP_EXCEPTION}, 
     *         {@code GS_EXCEPTION_ERROR}, {@code GS_LINEDRAW_ERROR},
     *         {@code GS_INTERUPTED}, {@code GS_TIMEOUT},
     *         {@code GS_ORT_INTERUPTED}, {@code GS_ORT_TIMEOUT},
     *         {@code RENDER_INTERUPTED}, {@code RENDER_TIMEOUT},
     *         {@code RENDER_EXCEPTION} or {@code SCENE_MAKER_ERROR}.
     * @param  crtName The name of the crt file used there are multiple crt
     *         files.
     * @param  imageType The type of image rendering requested, as specified to
     *         JammSupport.
     * @param  params The parameters used in rendering the current sample.
     * @param  sampleId The id of the sample Jamm is currently rendering.
     * @param  sampleHistoryId The history id of the sample Jamm is currently
     *         rendering.
     * @param  exception If specified then the param consists of exceptions that
     *         are caught locally.
     *            
     * @throws IllegalArgumentException if the reasonCode submitted is not valid
     */
    public JammSupportLogEvent(int reasonCode, String crtName,
            String imageType, String params, int sampleId, int sampleHistoryId,
            Throwable exception) {
        switch (reasonCode) {
            case CRT_ERROR:
                super.createLogRecord(Level.WARNING,
                        "Encountered exception while parsing CRT"
                        + ", while attempting {0} image display, for"
                        + " sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case ORTEP_SDT_ERROR:
                super.createLogRecord(Level.WARNING,
                        "Encountered exception while"
                                + " retrieving SDT, could not generate ORTEP"
                                + ", while attempting {0} image display, for"
                                + " sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case ORTEP_STUB_ERROR:
                super.createLogRecord(Level.WARNING, "Encountered an exception"
                        + " while retrieving ORTEP stub file"
                        + ", while attempting {0} image display, for"
                        + " sample {1} using crt file {2}.", new Object[] {
                                imageType, Integer.valueOf(sampleId), crtName },
                                exception);
                break;
            case ORTEP_CREATE_STUB_ERROR:
                super.createLogRecord(Level.WARNING,
                        "Encountered an  exception"
                                + " trying to create an ORTEP input stub"
                                + ", while attempting {0} image display, for"
                                + " sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case ORTEP_INPUT_ERROR:
                super.createLogRecord(Level.WARNING, "Encountered an exception"
                        + " trying to create an ORTEP input file"
                        + ", while attempting {0} image display, for"
                        + " sample {1} using crt file {2}.", new Object[] {
                        imageType, Integer.valueOf(sampleId), crtName },
                        exception);
                break;
            case ORTEP_SAVE_STUB_ERROR:
                super.createLogRecord(Level.INFO, "Encountered an exception"
                        + " saving new ORTEP stub, while attempting {0}"
                        + " image display, for sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case ORTEP_EXCEPTION:
                super.createLogRecord(Level.WARNING, "Encountered an"
                        + " exception generating ORTEP image, prior to"
                        + " completion, while attempting {0} image display,"
                        + " for sample {1} using crt file {2}.", new Object[] {
                        imageType, Integer.valueOf(sampleId), crtName },
                        exception);
                break;
            case ORTEP_CIF_ERROR:
                super.createLogRecord(Level.WARNING,
                        "Encountered exception while"
                                + " retrieving CIF, could not generate ORTEP"
                                + ", while attempting {0} image display, for"
                                + " sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case GS_EXCEPTION_ERROR:
                super.createLogRecord(Level.WARNING, "Encountered an exception"
                        + " trying to create a PostScript, while attempting"
                        + " {0} image display, for sample {1} using crt file"
                        + " {2}.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, exception);
                break;
            case GS_LINEDRAW_ERROR:
                super.createLogRecord(Level.INFO, "Linedraw error, while"
                        + " attempting {0} image display, for sample {1}"
                        + " using crt file {2}.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, exception);
                break;
            case GS_INTERUPTED:
                super.createLogRecord(Level.INFO, "Ghostscript interupted,"
                        + " while attempting {0} image display, for sample"
                        + " {1} using crt file {2}.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, exception);
                break;
            case GS_TIMEOUT:
                super.createLogRecord(Level.INFO, "Ghostscript failed to"
                        + " complete {0} image display, for sample {1} using"
                        + " crt file {2} in a timely manner.", new Object[] {
                        imageType, Integer.valueOf(sampleId), crtName },
                        exception);
                break;
            case GS_ORT_INTERUPTED:
                super.createLogRecord(Level.INFO, "Ghostscript job"
                        + " (ortep rendering) was interupted, while"
                        + " attempting {0} image display, for sample"
                        + " {1} using crt file {2}.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, exception);
                break;
            case GS_ORT_TIMEOUT:
                super.createLogRecord(Level.INFO, "Ghostscript job"
                        + " (ortep rendering) failed to complete {0} image"
                        + " display, for sample {1} using crt file {2} in a"
                        + " timely manner.", new Object[] { imageType,
                        Integer.valueOf(sampleId), crtName }, exception);
                break;
            case RENDER_INTERUPTED:
                super.createLogRecord(Level.WARNING, "Recipnet renderclient"
                        + " was interupted, while attempting {0} image"
                        + " display, for sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case RENDER_TIMEOUT:
                super.createLogRecord(Level.WARNING, "Recipnet renderclient"
                        + " failed to complete {0} image display for sample"
                        + " {1} using crt file {2} in a timely manner.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case RENDER_EXCEPTION:
                super.createLogRecord(Level.WARNING, "Recipnet renderclient"
                        + " encountered an exception while attempting {0}"
                        + " image display for sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            case SCENE_MAKER_ERROR:
                super.createLogRecord(Level.WARNING, "Encountered an exception"
                        + " trying to generate a scene description"
                        + ", while attempting {0} image"
                        + " display, for sample {1} using crt file {2}.",
                        new Object[] { imageType, Integer.valueOf(sampleId),
                                crtName }, exception);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
