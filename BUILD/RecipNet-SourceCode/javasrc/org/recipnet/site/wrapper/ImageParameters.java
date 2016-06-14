/*
 * Reciprocal Net Project
 *
 * ImageParameters.java
 *
 * 22-Oct-2002: jobollin wrote first draft
 * 22-Nov-2002: jobollin fixed minor Javadoc bug
 * 17-Dec-2002: jobollin fixed a typo in the initialization of the rsize field
 * 30-May-2003: midurbin removed defaultBasePath and its supporting methods,
 *              added crtName, sampleHistoryInfo, userId.
 * 10-Jun-2003: ekoperda fixed bug #932 in constructor
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.content.servlet
 *              package to org.recipnet.site.wrapper; also changed package
 *              references to match source tree reorganization; made class,
 *              most methods, and most variables public
 * 03-Jan-2005: encapsulated fields, and made most resulting accessors load
 *              values dynamically from the parameter map instead of loading
 *              them once in the constructor; converted private static methods
 *              to private instance methods; added javadoc comments
 * 23-Jan-2006: extracted the general request-parsing logic to a new superclass,
 *              RequestParameters
 */

package org.recipnet.site.wrapper;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.StringTokenizer;

import org.recipnet.site.content.servlet.JammSupport;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * <p>
 * ImageParameters represents a collection of parameters describing how to
 * prepare a particular image of a Reciprocal Net sample -- which sample,
 * which view of that sample, what type of image, and the image details.  It
 * derives these values from a Map of the type provided by
 * {@code javax.servlet.http.HttpServletRequest.getParameterMap()}, and by
 * internal rules for default values.
 * </p><p>
 * Some of the parameters provided by this class are unused in some of the image
 * preparation modes.  The {@code JammSupport} documentation describes the
 * image parameters obtained directly from request parameters and which
 * parameters are used in which modes.  This class also provides a small number
 * of additional and / or pre-digested fields.
 * </p>
 *
 * @see JammSupport
 *
 * @version 0.9.0
 */
public class ImageParameters extends RequestParameters {
    
    /**
     * the requested background color; lazilly extracted from "r", "g" and "b"
     */
    private Color backgroundColor;

    /** the filename portion of the CRT URL, extracted from "name" */
    private final String crtName;

    /** a repository ticket number, extracted from "name" */
    private final int ticket;
    
    /**
     * Initializes a new {@code ImageParameters} with the specified context URL
     * and name to value(s) map
     * 
     * @param  urlContext the context {@code URL} against which to resolve
     *         relative URLs among the parameter values
     * @param  pmap a {@code Map} whose keys are parameter names and whose
     *         values are arrays of parameter value {@code Strings}, such as is
     *         provided by {@code ServletRequest.getParameterMap()}
     *
     * @throws MalformedURLException if the "name" parameter is present in
     *         {@code pmap}, but its first value is not a well-formed URL
     */
    /*
     * Note: A raw Map is accepted as the parameter to push the unchecked cast
     * down to the superclass.  Until the HttpServletRequest class is
     * generified, this reduces the scope of the warning / warning suppression
     * that must be applied.
     */
    public ImageParameters(URL urlContext, Map pmap) 
            throws MalformedURLException {
        super(pmap);
        String crtURLString;
        
        // Must come after assignment of the parameterMap:
        crtURLString = getParameter("name");

        // CRT access information
        if (crtURLString != null) {
            URL crtURL = new URL(urlContext, crtURLString);
            String temp;
            int ticketNumber;
            
            // Extract CRT name
            temp = crtURL.getPath();
            crtName = temp.substring(temp.lastIndexOf("/") + 1);

            // extract any ticket number from the query string
            ticketNumber = 0;
            temp = crtURL.getQuery();
            if (temp != null) {
                StringTokenizer st = new StringTokenizer(temp, "&");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token.startsWith("ticket=")) {
                        ticketNumber = stringToInt(
                                token.substring("ticket=".length()), 0);
                        break;
                    }
                }
            }
            ticket = ticketNumber;
        } else {
            crtName = null;
            ticket = 0;
        }
    }

    /**
     * Returns the background color configured among these
     * {@code ImageParameters}
     * 
     * @return the background color as a {@code Color}
     */
    public Color getBackgroundColor() {
        if (backgroundColor == null) {
            backgroundColor = new Color(
                    (float) getParameter("r", 1.0d),
                    (float) getParameter("g", 1.0d),
                    (float) getParameter("b", 1.0d));
        }
        
        return backgroundColor;
    }

    /**
     * Returns the requested ball scale factor
     * 
     * @return the {@code double} value of the "bsize" parameter, or 1.0 if it
     *         is not present
     */
    public double getBallScaleFactor() {
        return getParameter("bsize", 1.0d);
    }

    /**
     * Returns the CRT file name specified among the request parameters
     * 
     * @return the {@code String} value of the CRT name, extracted from the
     *         "name" parameter
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * Returns the requested drawing type
     * 
     * @return the {@code String} value of the "drawType" parameter, or
     *         {@code null} if it is not present
     */
    public String getDrawType() {
        String drawType = getParameter("drawtype");
        
        return ("".equals(drawType) ? null : drawType);
    }

    /**
     * Returns the requested drawing function
     * 
     * @return the {@code String} value of the "function" parameter, or
     *         an empty string if it is not present
     */
    public String getFunction() {
        return getParameter("function");
    }

    /**
     * Returns the requested image size in pixels (for a square image)
     * 
     * @return the {@code int} value of the "size" parameter, or a default
     *         value if it is not present
     */
    public int getImageSize() {
        return getParameter("size", 100);
    }

    /**
     * Returns the requested model scale factor
     * 
     * @return the {@code double} value of the "scale" parameter, or 1.0 if it
     *         is not present
     */
    public double getModelScaleFactor() {
        return getParameter("scale", 1d);
    }

    /**
     * Returns the requested JPEG image quality
     * 
     * @return the {@code double} value of the "quality" parameter, or a default
     *         value if it is not present
     */
    public double getQualityFactor() {
        return getParameter("quality", 70d);
    }

    /**
     * Returns the requested rod (radial) scale factor
     * 
     * @return the {@code double} value of the "rsize" parameter, or 1.0 if it
     *         is not present
     */
    public double getRodScaleFactor() {
        return getParameter("rsize", 1.0d);
    }

    /**
     * Returns the sample history ID specified among these parameters
     * 
     * @return the {@code int} value of the "sampleHistoryId" parameter, or the
     *         designated invalid sample history ID if no other sample history
     *         ID is specified
     */
    public int getSampleHistoryId() {
        return getParameter("sampleHistoryId", 
                SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID);
    }

    /**
     * Returns the sample ID specified among these parameters
     * 
     * @return the {@code int} value of the "sampleId" parameter, or the
     *         designated invalid sample ID if no other sample ID is specified
     */
    public int getSampleId() {
        return getParameter("sampleId", SampleInfo.INVALID_SAMPLE_ID);
    }

    /**
     * Returns the ticket number specified among these parameters
     * 
     * @return the ticket number parsed from the "name" parameter, or 0 if no
     *         ticket number is specified
     */
    public int getTicket() {
        return ticket;
    }

    /**
     * Returns the user ID specified among these parameters
     * 
     * @return the {@code int} value of the "userId" parameter, or the
     *         designated invalid user ID if no other user ID is specified
     */
    public int getUserId() {
        return getParameter("userId", UserInfo.INVALID_USER_ID);
    }

    /**
     * Returns the requested magnification (i.e. zoom) factor for the image;
     * larger values mean greater magnification of the model relative to the
     * overall size of the image
     * 
     * @return the {@code double} value of the inappropriately-named "distance"
     *         parameter, or a default value if it is not present
     */
    public double getViewMagnification() {
        return getParameter("distance", 1.0d);
    }

    /**
     * Returns the requested rotation about the x axis
     * 
     * @return the {@code double} value of the "x" parameter, or 0.0 if it
     *         is not present
     */
    public double getXRotation() {
        return getParameter("x", 0d);
    }

    /**
     * Returns the requested rotation about the y axis
     * 
     * @return the {@code double} value of the "y" parameter, or 0.0 if it
     *         is not present
     */
    public double getYRotation() {
        return getParameter("y", 0d);
    }

    /**
     * Returns the requested rotation about the z axis
     * 
     * @return the {@code double} value of the "z" parameter, or 0.0 if it
     *         is not present
     */
    public double getZRotation() {
        return getParameter("z", 0d);
    }

    /**
     * Returns the whether inclusion of hydrogen atoms is requested
     * 
     * @return the {@code boolean} value of the "hydro" parameter, or 
     *         {@code false} if it is not present
     */
    public boolean shouldIncludeHydrogen() {
        return getParameter("hydro", false);
    }
}

