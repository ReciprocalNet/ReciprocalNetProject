/*
 * Reciprocal Net Project
 *
 * RequestParameters.java
 *
 * 23-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

import java.util.Map;

/**
 * {@code RequestParameters} represents an abstract collection of named
 * parameters and their {@code String} values, along with mechanisms for
 * providing value conversions and default values.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
public class RequestParameters {
    
    /**
     * A map associating parameter names with arrays of the associated values
     */
    private final Map<String, String[]> parameterMap;

    /**
     * Initializes a {@code RequestParameters} with the specified parameters
     * 
     * @param  pmap a {@code Map} whose keys are parameter names and whose
     *         values are arrays of parameter value {@code Strings}, such as is
     *         provided by {@code ServletRequest.getParameterMap()}; this map
     *         is stored and used directly -- not copied.
     */
    /*
     * Note: A raw Map is accepted as the parameter to push the unchecked cast
     * down to this class.  Until the HttpServletRequest class is generified,
     * this reduces the scope of the warning / warning suppression that must be
     * applied
     */
    @SuppressWarnings("unchecked")
    public RequestParameters(Map pmap) {
        parameterMap = pmap;
    }

    /**
     * Determines whether these request parameters contain the named parameter.
     * 
     * @param  paramName the name of the parameter to test for
     * 
     * @return {@code true} if the specified parameter is present, {@code false}
     *         if it isn't.  The parameter's positive presence implies nothing
     *         about the the associated value; in particular, it does not imply
     *         that the value is non-{@code null} or is parseable as a value of
     *         any particular non-{@code String} type
     */
    public boolean containsParameter(String paramName) {
        return parameterMap.containsKey(paramName);
    }
    
    /**
     * Returns the first value of the named parameter in the internal parameter
     * map
     *
     * @param  paramName a String key into {@code m}
     *
     * @return the <i>first</i> value of the named parameter in the internal
     *         parameter map, or {@code null} if that parameter is not present
     *         in the map
     */
    public String getParameter(String paramName) {
        return extractParameterValue(paramName, 0);
    }
    
    /**
     * Retrieves the first value of the named parameter as a {@code double}, or
     * the specified default value if the named parameter is not present or if
     * its first value cannot be parsed as a {@code double}
     * 
     * @param paramName the name of the parameter to retrieve
     * @param defaultValue the {@code double} value to return if the specified
     *        parameter does not exist or if its first value cannot be parsed as
     *        a {@code double}
     * 
     * @return the {@code double} value of the parameter or the specified
     *         default value, as appropriate
     */
    public String getParameter(String paramName, String defaultValue) {
        String value = getParameter(paramName);
        
        return ((value == null) ? defaultValue : value);
    }

    /**
     * Retrieves the first value of the named parameter as a {@code double}, or
     * the specified default value if the named parameter is not present or if
     * its first value cannot be parsed as a {@code double}
     * 
     * @param paramName the name of the parameter to retrieve
     * @param defaultValue the {@code double} value to return if the specified
     *        parameter does not exist or if its first value cannot be parsed as
     *        a {@code double}
     * 
     * @return the {@code double} value of the parameter or the specified
     *         default value, as appropriate
     */
    public double getParameter(String paramName, double defaultValue) {
        return stringToDouble(getParameter(paramName), defaultValue);
    }

    /**
     * Retrieves the first value of the named parameter as an {@code int}, or
     * the specified default value if the named parameter is not present or if
     * its first value cannot be parsed as an {@code int}
     * 
     * @param paramName the name of the parameter to retrieve
     * @param defaultValue the {@code int} value to return if the specified
     *        parameter does not exist or if its first value cannot be parsed as
     *        a {@code int}
     * 
     * @return the {@code int} value of the parameter or the specified
     *         default value, as appropriate
     */
    public int getParameter(String paramName, int defaultValue) {
        return stringToInt(getParameter(paramName), defaultValue);
    }
    
    /**
     * Retrieves the first value of the named parameter as a {@code boolean}, or
     * the specified default value if the named parameter is not present or if
     * its first value cannot be parsed as a {@code boolean}
     * 
     * @param paramName the name of the parameter to retrieve
     * @param defaultValue the {@code boolean} value to return if the specified
     *        parameter does not exist or if its first value cannot be parsed as
     *        a {@code boolean}
     * 
     * @return the {@code boolean} value of the parameter or the specified
     *         default value, as appropriate
     */
    public boolean getParameter(String paramName, boolean defaultValue) {
        return stringToBoolean(getParameter(paramName), defaultValue);
    }
    
    /**
     * Returns the {@code index}<sup>th</sup> value of the named parameter in
     * the internal parameter map
     * 
     * @param  paramName the name of the requested parameter
     * @param  index the zero-based index of the requested parameter value
     * 
     * @return the specified parameter value, or {@code null} if the specified
     *         parameter name does not appear in the internal map
     *         
     * @throws IndexOutOfBoundsException if the parameter name appears in the
     *         map but has fewer than {@code index} + 1 associated values
     */
    private String extractParameterValue(String paramName, int index) {
        String[] values = parameterMap.get(paramName);

        // Can throw ArrayIndexOutOfBoundsException:
        return (values == null) ? null : values[index];
    }

    /**
     * Returns a {@code double} value corresponding to the specified string,
     * or the specified default value if the string cannot be parsed as a
     * double value
     * 
     * @param  in the {@code String} to parse as a double value
     * @param  defaultVal the {@code double} default value to return if the
     *         input string is {@code null} or cannot be parsed
     *         
     * @return the parsed or default {@code double} value resulting from
     *         evaluation of the input string
     */
    protected double stringToDouble(String in, double defaultVal) {
        if (in != null) {
            try {
                return Double.parseDouble(in);
            } catch (NumberFormatException nfe) {
                /* drop through */
            }
        }
        return defaultVal;
    }

    /**
     * Returns a {@code boolean} value corresponding to the specified string,
     * or the specified default value if the string cannot be parsed as a
     * boolean value
     * 
     * @param  in the {@code String} to parse as a boolean value
     * @param  defaultVal the {@code boolean} default value to return if the
     *         input string is {@code null} or cannot be parsed
     *         
     * @return the parsed or default {@code boolean} value resulting from
     *         evaluation of the input string
     */
    protected boolean stringToBoolean(String in, boolean defaultVal) {
        if (in != null) {
            in = in.trim();
            if (in.equals("1") || in.equalsIgnoreCase("yes")
                    || in.equalsIgnoreCase("true")) {
                return true;
            } else if (in.equals("0") || in.equalsIgnoreCase("no")
                    || in.equalsIgnoreCase("false")) {
                return false;
            }
        }
        return defaultVal;
    }

    /**
     * Returns a {@code int} value corresponding to the specified string,
     * or the specified default value if the string cannot be parsed as an
     * int value
     * 
     * @param  in the {@code String} to parse as an int value
     * @param  defaultVal the {@code int} default value to return if the
     *         input string is {@code null} or cannot be parsed
     *         
     * @return the parsed or default {@code int} value resulting from
     *         evaluation of the input string
     */
    protected int stringToInt(String in, int defaultVal) {
        if (in != null) {
            try {
                return Integer.parseInt(in.trim());
            } catch (NumberFormatException nfe) {
                /* drop through */
            }
        }
        return defaultVal;
    }
}
