/*
 * Reciprocal Net project
 * 
 * RelativeDayField.java
 *
 * 22-Jun-2005: midurbin wrote the first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A field that displays a marked-up, localized representation of either the
 * period of time represented by the currently session's {@link
 * org.recipnet.site.shared.bl.UserBL.Pref#SUMMARY_DAYS SUMMARY_DAYS} preference
 * or the day on which the sample provided by a surrounding
 * {@code SampleContext} was modified relative to the current day.
 */
public class RelativeDayField extends HtmlPageElement {

    /**
     * An optional property that indicates that this tag should display the
     * localized marked-up representation of the day (relative to today) on
     * which the sample provided by the nearest {@code SampleContext} was last
     * modified. This must not be set to true when 'displySampleDaysPreference'
     * is also true.
     */
    private boolean displayRelativeDayWhenSampleWasModified;

    /**
     * An optional property that indicates that this tag should display the
     * localized marked-up representation of time period represented by the
     * 'summary days' preference for the current session. This must not be set
     * to true when 'displayRelativeDayWhenSampleWasModified' is also true.
     */
    private boolean displaySampleDaysPreference;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.displayRelativeDayWhenSampleWasModified = false;
        this.displaySampleDaysPreference = false;
    }

    /**
     * Sets the 'displayRelativeDayWhenSampleWasModified' property.
     * 
     * @throws IllegalStateException if setting this property to true when
     *         'displaySampleDaysPreference' is also true
     */
    public void setDisplayRelativeDayWhenSampleWasModified(boolean display) {
        if (display && this.displaySampleDaysPreference) {
            throw new IllegalStateException();
        }
        this.displayRelativeDayWhenSampleWasModified = display;
    }

    /** Gets the 'displayRelativeDayWhenSampleWasModified' property. */
    public boolean getDisplayRelativeDayWhenSampleWasModified() {
        return this.displayRelativeDayWhenSampleWasModified;
    }

    /**
     * Sets the 'displaySampleDaysPreference' property.
     * 
     * @throws IllegalStateException if setting this property to true when
     *         'displayRelativeDayWhenSampleWasModified' is also true
     */
    public void setDisplaySampleDaysPreference(boolean display) {
        this.displaySampleDaysPreference = display;
    }

    /** Gets the 'displaySampleDaysPreference' property. */
    public boolean getDisplaySampleDaysPreference() {
        return this.displaySampleDaysPreference;
    }

    /**
     * {@inheritDoc}; this version outputs the localized, human-readable
     * version of the relative day or timespan.
     * 
     * @throws IllegalStateException if neither diplay property was set to true
     *         failing to indicate what should be displayed by this tag
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        try {
            LanguageHelper lh = LanguageHelper.extract(
                    this.pageContext.getServletContext());

            if (this.displayRelativeDayWhenSampleWasModified) {
                SampleContext sampleContext
                        = findRealAncestorWithClass(this, SampleContext.class);

                out.print(lh.getDaysAgoString(getAbsDayDifference(new Date(),
                        sampleContext.getSampleInfo().lastActionDate),
                        this.pageContext.getRequest().getLocales(), true));
            } else if (this.displaySampleDaysPreference) {
                UserPreferences prefs = (UserPreferences)
                        this.pageContext.getSession().getAttribute(
                                "preferences");

                out.print(lh.getSinceDaysAgoString(UserBL.getPreference(
                        UserBL.Pref.SUMMARY_DAYS, prefs),
                        this.pageContext.getRequest().getLocales(), true));
            } else {
                throw new IllegalStateException();
            }
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * Determines the number of days between the two dates. This method pays
     * attention only to the dates and ignores any finer-grained time
     * information. Another way to think of the count returned is the number of
     * midnights between the two dates (exclusive of the first date and
     * includive of the second, if either or both happen to represent midnight
     * on their respective dates). The relative order of the two dates does not
     * affect the result.
     * 
     * @param first a {@code Date} representing one of the dates
     * @param second a {@code Date} representing the other date
     * @return the calendar day difference between the dates
     */
    private int getAbsDayDifference(Date first, Date second) {
        Calendar firstCal = Calendar.getInstance();
        Calendar secondCal = Calendar.getInstance();

        if (first.compareTo(second) < 0) {
            firstCal.setTime(first);
            secondCal.setTime(second);
        } else {
            firstCal.setTime(second);
            secondCal.setTime(first);
        }

        int daysDifference = 0;

        /*
         * advance the first calendar towards the second calendar, recording the
         * offset in days
         */
        while (firstCal.get(Calendar.YEAR) < secondCal.get(Calendar.YEAR)) {
            int daysToAdd = firstCal.getActualMaximum(Calendar.DAY_OF_YEAR) + 1
                    - firstCal.get(Calendar.DAY_OF_YEAR);
            daysDifference += daysToAdd;
            firstCal.add(Calendar.DAY_OF_YEAR, daysDifference);
        }

        daysDifference += secondCal.get(Calendar.DAY_OF_YEAR)
                - firstCal.get(Calendar.DAY_OF_YEAR);

        return daysDifference;
    }
}
