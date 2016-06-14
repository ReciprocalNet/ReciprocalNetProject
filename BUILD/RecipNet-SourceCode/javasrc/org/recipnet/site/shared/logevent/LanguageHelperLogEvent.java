/* 
 * Reciprocal Net Project
 *  
 * LanguageHelperLogEvent.java 
 * 
 * 07-Jun-2004: mdurbin wrote first draft 
 * 05-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/**
 * Subclass of LogEvent used by {@code LanguageHelper} listener class. Due to
 * the nature of the listener, errors may result in non-optimal behavior without
 * actually preventing it from running, so log messages are essential for the
 * site administrator to diagnose apparent malfunctions.
 */
public class LanguageHelperLogEvent extends LogEvent {

    /**
     * A constructor that generates a LogEvent about an error that occured while
     * initializing the user-specified language pack jar file. This event is not
     * fatal, but is important to note because it indicates that the language
     * pack will not be used. The {@code LogRecord} created by this constructor
     * has the level {@code WARNING}.
     * 
     * @param path the path for the language pack, read from the context
     *        parameter defined in web.xml.
     * @param exception The exception encountered.
     */
    public LanguageHelperLogEvent(String path, Throwable exception) {
        super();
        super.createLogRecord(Level.WARNING, "LanguageHelper encountered an"
                + " exception while initializing the user-specified language"
                + " pack, {0}.  LanguageHelper will continue to run, but will"
                + " not have access to property files contained in the"
                + " specified Jar file.", new Object[] { path }, exception);
    }
}
