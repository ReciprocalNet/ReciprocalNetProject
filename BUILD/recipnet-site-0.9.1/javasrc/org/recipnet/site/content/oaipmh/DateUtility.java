/*
 * Reciprocal Net Project
 *
 * DateUtility.java
 *
 * 07-Oct-2005: jobollin extracted this class from OaiPmhResponder
 */

package org.recipnet.site.content.oaipmh;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.recipnet.common.ParsedDate;
import org.recipnet.common.ParsedDate.Granularity;

/**
 * <p>
 * A class that manages parsing and formatting dates pursuant to the OAI-PMH
 * date-handling specifications.
 * </p><p>
 * <strong>Thread Safety:</p> instances of this class are thread-safe.  They may
 * safely be shared among multiple threads without external synchronization.
 * </p>
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class DateUtility {

    /**
     * The day-granularity date format specified by OAI-PMH 
     */
    private final DateFormat dateFormat;

    /**
     * The second-granularity date format specified by OAI-PMH 
     */
    private final DateFormat dateTimeFormat;

    /**
     * A Calendar for making date calculations 
     */
    private final Calendar calendar;

    /**
     * Initializes an {@code DateUtility}
     */
    public DateUtility() {
        TimeZone gmt = TimeZone.getTimeZone("GMT");

        dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
        dateFormat.setTimeZone(gmt);
        
        dateTimeFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" );
        dateTimeFormat.setTimeZone(gmt);
        
        calendar = Calendar.getInstance(gmt);
    }
    
    /**
     * Parses the provided date string with the correct date format (determined
     * from the string itself), returning a date that correctly bounds the
     * specified date string either above or below.
     *
     * @param  dateString a {@code String} representing the date to parse
     * @param  upperBound {@code true} if the returned date should be an
     *         exclusive upper bound; otherwise it is an exclusive lower bound
     * @return a {@code ParsedDate} suitably bounding the supplied date string
     *         according to its granularity
     *         
     * @throws ParseException if the specified string cannot be parsed as a date
     *         according to one of the supported OAI-PMH date formats
     */
    public ParsedDate parseDate(String dateString, boolean upperBound)
            throws ParseException {
        boolean withTime = (dateString.indexOf('T') >= 0);
        DateFormat format = (withTime ? dateTimeFormat : dateFormat);
        Date date;

        synchronized (format) {
            date = format.parse(dateString);
        }
        synchronized (calendar) {
            calendar.setTime(date);
            if (upperBound) {
                calendar.add((withTime ? Calendar.SECOND : Calendar.DATE), 1);
            } else {
                calendar.add(Calendar.SECOND, -1);
            }
            return new ParsedDate(calendar.getTime(),
                                  withTime ? Granularity.SECOND_GRANULARITY
                                           : Granularity.DAY_GRANULARITY);
        }
    }

    /**
     * Formats the specified date according to the seconds-granularity OAI date
     * format to produce a {@code String}
     * 
     * @param  d the {@code Date} to format
     * 
     * @return a {@code String} representation of the provided date, formatted
     *         according to the seconds-granularity OAI-PMH date format
     */
    public String formatDate(Date d) {
        synchronized (dateTimeFormat) {
            return dateTimeFormat.format(d);
        }
    }
}
