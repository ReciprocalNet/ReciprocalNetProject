/*
 * Reciprocal Net project
 * 
 * SyslogHandler.java
 *
 * 03-Sep-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 03-Oct-2002: ekoperda changed the default formatter to output nested
 *              exceptions in addition to the topmost ones
 * 21-Oct-2002: ekoperda added getVerboseFormatter() function and changed
 *              constructor logic so that the "verbose" formatter is the
 *              default for loglevels FINE, FINER, FINEST, and ALL
 * 15-Nov-2002: ekoperda modified code in publish() to utilize new class
 *              ProcessWrapper; helped eliminate a file descriptor leak
 * 21-Feb-2003: ekoperda exceptionized publish()
 * 30-Jun-2003: ekoperda added reaper thread support throughout to fix bug #950
 * 14-Jul-2003: ekoperda rewrote implementation to utilize the syslog() 
 *              function defined by POSIX via JNI
 * 07-Jan-2004: ekoperda removed unncessary import of ProcessWrapper class
 * 12-May-2006: jobollin formatted the source and removed unused imports
 */

package org.recipnet.site.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * <p>
 * A "log handler" as defined by the J2SE 1.4 logging framework that writes log
 * messages to Linux's syslog using the user-specified facility and tag. This
 * object should never be instantiated directly by a user but instead should be
 * enabled by appropriate entries in the logging.properties file that's owned by
 * the JRE. (The actual configuration properties used by this class are read via
 * the global LogManager object and are not read from logging.properties
 * directly.)
 * </p><p>
 * Java does not natively support logging to syslog (or much other POSIX
 * functionality, for that matter) so native code is employed. See the file
 * {@code SyslogHandler.cpp} for implementations of some behavior described
 * herein. Because of the native code dependency, the dynamic shared library
 * {@code librecipnet_server.so} (on Linux systems) must be accessible to the
 * Java VM's library loader at runtime. Some Java VM's seem to require that the
 * system property {@code java.library.path} be set to include {@code /usr/lib}
 * (or similar) before they'll be able to load {@code librecipnet_server.so}
 * from that location.
 * </p><p>
 * Each {@code LogRecord} passed to this class to be published (that matches any
 * configured filter or level criteria) is sent to the local system's syslog
 * facility with a priority code derived from the {@code LogRecord}'s "level"
 * value. The precise semantics of this mapping from Java logging level to
 * syslog priority are defined by {@code decodeJavaLoggingLevel()} in
 * {@code SyslogHandler.cpp}.
 * </p><p>
 * The following parameters from logging.properties are recognized by this
 * class:
 * <ul>
 * <li> {@code LogLevel} is the minimum priority level that {@code LogRecord}s
 * must meet to be logged. Possible values are defined by {@code Level.parse()}.
 * The value {@code Level.ALL} is assumed if none is specified. </li>
 * <li> {@code LogFilter} is the name of a Java class (which implements
 * {@code java.util.logging.Filter}) that should be used to filter
 * {@code LogRecord}s before they are logged. No filter class is used if this
 * property is not specified, or is blank. </li>
 * <li> {@code LogFormatter} is the name of the Java class (which implements
 * {@code java.util.logging.Formatter}) that should be used to format the
 * {@code LogRecord}s into the text "message" that gets sent to syslog. The
 * default formatter is used if this property is not specified, or blank. </li>
 * <li> {@code LogFacility} is the syslog facility name to which messages should
 * be written. Possible values are defined by the function
 * {@code decodeSyslogFacility()} in {@code SyslogHandler.cpp}. The default
 * value 'user' is assumed if none is specified. </li>
 * <li> {@code LogTag} is the "tag" or "identifier" string that should be
 * prepended to each message as it's sent to syslog. Maximum length is 31
 * characters. The default value 'recipnetd' is assumed if none is specified.
 * </li>
 * </ul>.
 * </p><p>
 * A limitation of the current implementation is that only a single "facility"
 * and "tag" may be used at the same time within the same VM. Attempting to set
 * a new "facility" and/or "tag", for instance by creating new instances of this
 * class with different constructor arguments, will cause the new "facility" and
 * "tag" to be used by all instances of this class. Such use has not been tested
 * thoroughly, however, and is not recommended.
 * </p>
 */
public class SyslogHandler extends Handler {

    /**
     * Maximum number of characters to send to syslog in a single message. The
     * value selected is a conservative hard limit. Syslog's actual limit is
     * somewhat less than 1024 characters, depending upon the logfile format and
     * the message's tag. Messages longer than this limit are sent to syslog in
     * multiple chunks.
     */
    private static final int SYSLOG_MESSAGE_LIMIT = 950;

    /*
     * Class initialization routine that loads native code from
     * {@code librecipnet_server.so}. Depending upon the Java VM's
     * configuration, it may be necessary to have set the system property
     * {@code java.library.path} to include {@code /usr/lib} (or similar) prior
     * to this code being executed.
     */
    static {
        System.loadLibrary("recipnet_server");
    }

    /**
     * Initializes a new SyslogHandler based on properties obtained from the
     * system-wide {@code LogManager}.
     */
    public SyslogHandler() {

        /*
         * Configure ourselves based on properties that the LogManager read
         * from logging.properties. We need a level, a filter class, a formatter
         * class, and a facility name.
         */
        LogManager logManager = LogManager.getLogManager();

        String levelStr = logManager.getProperty("LogLevel");
        Level ourLevel
                = ((levelStr != null) ? Level.parse(levelStr) : Level.ALL);
        
        setLevel(ourLevel);

        String filterStr = logManager.getProperty("LogFilter");
        if ((filterStr != null) && !filterStr.trim().equals("")) {
            try {
                setFilter((Filter) Class.forName(filterStr).newInstance());
            } catch (ClassNotFoundException ex) {
                reportError("Unable to instantiate log filter", ex,
                        ErrorManager.OPEN_FAILURE);
            } catch (InstantiationException ex) {
                reportError("Unable to instantiate log filter", ex,
                        ErrorManager.OPEN_FAILURE);
            } catch (IllegalAccessException ex) {
                reportError("Unable to instantiate log filter", ex,
                        ErrorManager.OPEN_FAILURE);
            }
        }

        String formatterStr = logManager.getProperty("LogFormatter");
        if ((formatterStr != null) && !formatterStr.trim().equals("")) {
            try {
                setFormatter(
                        (Formatter) Class.forName(formatterStr).newInstance());
            } catch (ClassNotFoundException ex) {
                reportError("Unable to instantiate formatter", ex,
                        ErrorManager.OPEN_FAILURE);
            } catch (InstantiationException ex) {
                reportError("Unable to instantiate log formatter", ex,
                        ErrorManager.OPEN_FAILURE);
            } catch (IllegalAccessException ex) {
                reportError("Unable to instantiate log filter", ex,
                        ErrorManager.OPEN_FAILURE);
            }
        } else {
            if (ourLevel.intValue() > Level.FINE.intValue()) {
                // Enable normal-format user-level logging
                setFormatter(getDefaultFormatter());
            } else {
                // Enable verbose-format logging for debugging purposes
                setFormatter(getVerboseFormatter());
            }
        }

        String facility = logManager.getProperty("LogFacility");
        if (facility == null) {
            facility = "user";
        }

        String tag = logManager.getProperty("LogTag");
        if (tag == null) {
            tag = "recipnetd";
        }

        // Invoke native code to initialize our connection to syslog.
        initLogger(tag, facility);
    }

    /**
     * Used to write a log record to syslog - overrides corresponding method
     * from the Handler class. This implementation is synchronized in order to
     * avoid thread-safety problems in the native code used within. This
     * implementation breaks messages longer than {@code SYSLOG_MESSAGE_LIMIT}
     * into two or more messages to overcome an undocumented message length
     * limitation in {@code syslogd} on Linux when accessed via
     * {@code syslog()}.
     */
    @Override
    public synchronized void publish(LogRecord record) {
        
        /*
         * TODO: remove the code for message splitting once {@code syslog()}
         * allows long message bodies.
         */
        if (!isLoggable(record)) {
            return;
        }

        // Format the log record into a flat string.
        String wholeMessage = getFormatter().format(record);

        /*
         * Break the string up into one or more fragments, each no more than
         * SYSLOG_MESSAGE_LIMIT characters long. This works around an
         * undocumented log message length limitation in syslog().
         */
        for (int startingPos = 0; startingPos < wholeMessage.length();
                startingPos += SYSLOG_MESSAGE_LIMIT) {
            int endingPos = Math.min(startingPos + SYSLOG_MESSAGE_LIMIT,
                    wholeMessage.length());

            // Invoke native code to write this message fragment.
            doLogger(record.getLevel().intValue(),
                    wholeMessage.substring(startingPos, endingPos));
        }
    }

    /**
     * Overrides corresponding method from the Handler class. This
     * implementation is synchronized in order to avoid thread-safety problems
     * in the native code used within.
     */
    @Override
    public synchronized void close() {
        closeLogger();
    }

    /**
     * Overrides corresponding method from the Handler class. This
     * implementation is a no-op because {@code publish()} always writes log
     * messages synchronously.
     */
    @Override
    public synchronized void flush() {
        // Do nothing.
    }

    /**
     * Native method - initializes the native logging code. Should be called
     * only once per Java VM.
     * 
     * @param tag the "tag" or "identifier" string that should be prepended to
     *        all messages as they're sent to syslog. Maximum length is 31
     *        characters. May be null, in which case no tag is used.
     * @param facility is the syslog facility name to which all subsequent
     *        messages should be sent. Possible values are defined by the
     *        function {@code decodeSyslogFacility()} in
     *        {@code SyslogHandler.cpp}.
     */
    private native void initLogger(String tag, String facility);

    /**
     * Native method - writes the specified message to syslog. Calls to this
     * method must be serialized -- no more than one thread may call it at any
     * given time. Must follow a call to {@code initLogger()} and precede any
     * call to {@code closeLogger()}.
     * 
     * @param level the integer equivalent of the log message's desired logging
     *        level, as returned by {@code Level.intValue()}.
     * @param message the message to be written. Should contain ASCII characters
     *        only, otherwise the results may be undefined.
     */
    private native void doLogger(int level, String message);

    /**
     * Native method - deinitializes the native logging code. Should be called
     * only once per Java VM, and only after a prior call to
     * {@code initLogger()}.
     */
    private native void closeLogger();

    /** Returns a simple formatter for {@code LogRecord} objects. */
    private static Formatter getDefaultFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                StringBuilder message
                        = new StringBuilder(formatMessage(record));

                for (Throwable ex = record.getThrown(); ex != null;
                        ex = ex.getCause()) {
                    message.append("  Exact error=").append(ex);
                }
                return message.toString();
            }
        };
    }

    /** Returns a verbose formatter for {@code LogRecord} objects. */
    private static Formatter getVerboseFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw, true);

                pw.write(formatMessage(record));
                for (Throwable ex = record.getThrown(); ex != null;
                        ex = ex.getCause()) {
                    pw.write("  Exact error=");
                    ex.printStackTrace(pw);
                }
                pw.flush();

                return sw.toString();
            }
        };
    }
}
