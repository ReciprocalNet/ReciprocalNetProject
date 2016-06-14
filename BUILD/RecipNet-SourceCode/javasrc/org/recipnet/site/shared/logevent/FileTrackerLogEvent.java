/*
 * Reciprocal Net project
 *  
 * FileTrackerLogEvent.java
 *
 * 20-Jun-2003: ajooloor wrote first draft
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.io.File;
import java.util.logging.Level;
import java.lang.IllegalArgumentException;

/**
 * Subclass of LogEvent used to record file tracking events The severity
 * Level.FINE and Level.INFO are used
 */
public class FileTrackerLogEvent extends LogEvent {

    /**
     * The constants used for specifying the type of logging event that has
     * occured.
     */
    public static final int REQUEST_RECEIVED = 0;

    public static final int ASSIGN_KEY = 1;

    public static final int TRACKING_REQUEST_RECEIVED = 2;

    public static final int FILE_REMOVED = 3;

    public static final int METADATA = 4;

    public static final int INVALID = 5;

    public static final int SHUTDOWN = 6;

    public static final int ATTRIBUTE_REMOVED = 7;

    public static final int PURGE = 8;

    public static final int INSTALL = 9;

    public static final int TEMP_DIR = 10;

    public static final int CACHE_SIZE = 11;

    public static final int INACTIVITY = 12;

    public static final int ACCESSIBLE = 13;

    public static final int RETRIEVE_SUCCESS = 14;

    public static final int RETRIEVE_FAILURE = 15;

    /**
     * The constructor.of log events that take a filename and a mimetype as
     * argument.
     * 
     * @param logEventType The integer constant which denotes type of event that
     *        is to be logged. Must be {@code REQUEST_RECEIVED}
     * @param fileName the name of the temporary file created for line drawing
     *        or ORTEP diagram.
     * @param type a string containing the type information. The possible types
     *        are jpeg, postcript or text.
     * @param deleteOnInvalidation indicates whether or not file should be
     *        deleted when it is removed from tracking.
     */
    public FileTrackerLogEvent(int logEventType, String fileName, String type,
            boolean deleteOnInvalidation) {
        super();
        switch (logEventType) {
            case REQUEST_RECEIVED:
                super.createLogRecord(
                        Level.INFO,
                        "Filetracker: File tracking request received for {0}"
                                + " with type = {1}  deleteOnInvalidation = {2}",
                        new Object[] { fileName, type,
                                new Boolean(deleteOnInvalidation) }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The constructor.of log events that takes a key as an argument
     * 
     * @param logEventType The integer constant which denotes type of event that
     *        is to be logged. Must be one of the type {@code ASSIGN_KEY} or
     *        {@code CACHE_SIZE}
     * @param fileName the name of the temporary file created for line drawing
     *        or ORTEP diagram
     * @param keyOrCacheValue this variable takes the values of key or cache
     *        size
     */
    public FileTrackerLogEvent(int logEventType, String fileName,
            long keyOrCacheValue) {
        super();
        switch (logEventType) {
            case ASSIGN_KEY:
                super.createLogRecord(Level.INFO,
                        "FileTracker: {0} assigned key {1}", new Object[] {
                                fileName, new Long(keyOrCacheValue) }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The constructor that takes key valu , time out and file cache size as an
     * argument.
     * 
     * @param logEventType The integer constant which denotes type of event that
     *        is to be logged. Must be of the type {@code METADATA},
     *        {@code METADATA} {@code INVALID} {@code TRACKING_REQUEST_RECEIVED}
     *        {@code CACHE_SIZE} {@code INACTIVITY} {@code RETRIEVE_SUCCESS}
     *        {@code RETRIEVE_FAILURE}
     * @param param1 an interger whose value must be key value, file size or
     *        time in milli seconds
     */
    public FileTrackerLogEvent(int logEventType, long param1) {
        super();
        switch (logEventType) {
            case METADATA:
                super.createLogRecord(Level.INFO,
                        "FileTracker: updating metadata for key {0}",
                        new Object[] { new Long(param1) }, null);
                break;
            case INVALID:
                super.createLogRecord(Level.INFO,
                        "FileTracker: Update requested for invalid or expired"
                                + "key {0}", new Object[] { new Long(param1) },
                        null);
                break;
            case TRACKING_REQUEST_RECEIVED:
                super.createLogRecord(
                        Level.INFO,
                        "FileTracker: Received end tracking request for key {0}",
                        new Object[] { new Long(param1) }, null);
                break;
            case CACHE_SIZE:
                super.createLogRecord(Level.INFO,
                        "FileTracker: file cache size limit {0} bytes",
                        new Object[] { new Long(param1) }, null);
                break;
            case INACTIVITY:
                super.createLogRecord(Level.INFO,
                        "FileTracker: inactivity timeout {0} milliseconds",
                        new Object[] { new Long(param1) }, null);
                break;
            case RETRIEVE_SUCCESS:
                super.createLogRecord(Level.INFO,
                        "FileTracker: Retrieving tracked file for key {0}",
                        new Object[] { new Long(param1) }, null);
                break;
            case RETRIEVE_FAILURE:
                super.createLogRecord(Level.INFO,
                        "FileTracker: Retrieval requested for invalid or "
                                + "expired key {0}", new Object[] { new Long(
                                param1) }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The constructor which takes a file as an argument
     * 
     * @param logEventType The integer constant which denotes type of event that
     *        is to be logged. Must be of the type {@code TEMP_DIR}.
     * @param tempDir a path to a directory that may be used to store temporary
     *        files
     */
    public FileTrackerLogEvent(int logEventType, File tempDir) {
        super();
        switch (logEventType) {
            case TEMP_DIR:
                super.createLogRecord(Level.FINE,
                        "FileTracker: using temp directory {0}",
                        new Object[] { tempDir == null ? "(default)"
                                : tempDir.toString() }, null);
                break;
            default:
                throw new IllegalArgumentException("Argument not set");
        }
    }

    /**
     * The constructor which takes default attribute name and file name as an
     * argument
     * 
     * @param logEventType The integer constant which specifies type of event
     *        that is to be logged. Must be of the type {@code ACCESSIBLE}
     *        {@code FILE_REMOVED}
     * @param string the name of the file to be tracked or the default attribute
     *        name of the file tracker
     */
    public FileTrackerLogEvent(int logEventType, String string) {
        super();
        switch (logEventType) {
            case ACCESSIBLE:
                super.createLogRecord(Level.FINE,
                        "FileTracker: accessible as {0}",
                        new Object[] { string }, null);
                break;
            case FILE_REMOVED:
                super.createLogRecord(Level.FINE,
                        "FileTracker: tracking of {0} removed",
                        new Object[] { string }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * The constructor tha takes the type of event as an argument.
     * 
     * @param logEventType The integer constant which specifies type of event
     *        that is to be logged. Must be {@code INSTALL} {@code SHUTDOWN}
     *        {@code ATTRIBUTE_REMOVED} {@code PURGE }
     */
    public FileTrackerLogEvent(int logEventType) {
        super();
        switch (logEventType) {
            case INSTALL:
                super.createLogRecord(Level.INFO,
                        "FileTracker: FileTracker installed",
                        new Object[] { null }, null);
                break;
            case SHUTDOWN:
                super.createLogRecord(Level.INFO,
                        "FileTracker: FileTracker shutting down",
                        new Object[] { null }, null);
                break;
            case ATTRIBUTE_REMOVED:
                super.createLogRecord(Level.FINE,
                        "FileTracker: FileTracker attribute removed",
                        new Object[] { null }, null);
                break;
            case PURGE:
                super.createLogRecord(Level.FINE,
                        "FileTracker: FileTracker files purged",
                        new Object[] { null }, null);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
