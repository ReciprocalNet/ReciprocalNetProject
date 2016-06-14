/*
 * Reciprocal Net project
 *  
 * LogEven.java
 *
 * 12-Jun-2002: hclin wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 03-Sep-2002: ekoperda revamped class to better support Site Manager's 
 *              logging mechanism
 * 14-Apr-2003: ekoperda revamped class again to better support Site Manager's
 *              logging mechanism
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 31-May-2006: jobollin reformatted the source, implemented generics
 */

package org.recipnet.site.shared.logevent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * <p>
 * LogEvent is a abstract class which represents an event message that should be
 * recorded in the site's log file; subclasses define events that are used for
 * particular purposes. These objects generally are passed from the content and
 * wrapper layers down to SiteManager to be written, but generally are not used
 * by the core modules themselves.
 * </p><p>
 * Each {@code LogEvent} is converted to one or more {@code LogRecord}s (from
 * the Java logging API) within core via the {@link #getLogRecords()} method.
 * </p><p>
 * A typical subclass will provide only a constructor (and override no other
 * methods). Code within the typical subclass's constructor will invoke
 * {@link #createLogRecord(Level, String, Object[], Throwable)
 * createLogRecord()} one or more times.
 * </p>
 */
abstract public class LogEvent implements Serializable {

    /** a List of LogRecord's */
    private final List<LogRecord> logRecords;

    /**
     * Returns an unmodifiable {@code List} of one or more {@code LogRecord}
     * objects that represents the log event embodied by this object. Normally
     * this function would be invoked by SiteManager.
     */
    public List<LogRecord> getLogRecords() {
        return Collections.unmodifiableList(logRecords);
    }

    /**
     * Protected constructor; subclass constructors should call super().
     */
    protected LogEvent() {
        logRecords = new ArrayList<LogRecord>(1);
    }

    /**
     * Protected function invoked from subclasses, typically by the subclasses'
     * constructors. May be invoked multiple times on an object if a single
     * object is to generate multiple entries in the log.
     * 
     * @param level the severity of this log message; should be one of the
     *        static constants defined in java.util.logging.Level.
     * @param message the textual part of the message string. The text for this
     *        string should be supplied in a static fashion by the caller, not
     *        generated dynamically. If any portion of the log entry that will
     *        be generated later should contain dynamic values available only at
     *        runtime, those dynamic values should be passed as part of the
     *        {@code parameters} array described below. Parameters are
     *        substituted into the message string for the substrings "{0}",
     *        "{1}", "{2}", and so forth by the logging subsystem. See
     *        documentation for the {@code MessageFormat} class (in the Java
     *        API) for more information about how the substitution is performed.
     *        If the caller employs resource bundles then this string should be
     *        a "key" into the bundle.
     * @param parameters an array of {@code Object}s whose {String}
     *        representations are inserted into the message string specified
     *        above by the logging subsystem. May be null, or if an array is
     *        provided, any element of the array may be null.
     * @param thrown if the LogEvent is being generated in response to an
     *        exception being caught, that exception (a {@code Throwable}
     *        object) should be specified here. May be null if there is no such
     *        exception.
     */
    protected void createLogRecord(Level level, String message,
            Object parameters[], Throwable thrown) {
        LogRecord logRecord = new LogRecord(level, message);
        
        if (parameters != null) {
            logRecord.setParameters(parameters);
        }
        if (thrown != null) {
            logRecord.setThrown(thrown);
        }

        logRecords.add(logRecord);
    }
}
