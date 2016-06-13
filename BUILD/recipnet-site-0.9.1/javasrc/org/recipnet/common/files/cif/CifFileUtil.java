/*
 * Reciprocal Net Project
 *
 * CifFileUtil.java
 *
 * 10-Nov-2005: jobollin wrote first draft
 * 07-Mar-2006: jobollin fixed bug in which getCifString() did not trim() the
 *              CIF value when exercising the SHELX hack
 */

package org.recipnet.common.files.cif;

import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.CifFile.DataLoop;

/**
 * A utility class containing convenience methods for extracting data from
 * {@code CifFile} objects.
 *  
 * @author jobollin
 * @version 0.9.0
 */
public class CifFileUtil {

    /**
     * Looks up the specified data names in the provided CIF data block,
     * returning the best available value for the group.  The best available
     * value is defined as that one among those that are present and not
     * placeholders that is associated with the earliest name in the list, if
     * there is such a value; if there is no such value then the best available
     * value is that one among all those present in the block that is associated
     * with the earliest name in the list.  If there is no value in the data
     * block for any of the names in the list then {@code null} is returned.
     *   
     * @param  block the {@code DataBlock} in which to look up the value
     * @param  dataNames the data names to look up, in priority order
     * 
     * @return the best available {@code CifValue} for the collection of data
     *         names, or {@code null} if none are available in the block
     */
    @SuppressWarnings("unchecked")
    public static CifValue lookupCifValue(DataBlock block, String... dataNames) {
        CifValue rval = null;
        boolean placeholderFound = false;
        
        /*
         * For the data names provided, find the first that has a
         * non-placeholder data value; if there is none then find the first that
         * has a placeholder value
         */
        for (String name : dataNames) {
            if (block.containsName(name) && !block.containsNameInLoop(name)) {
                
                /*
                 * This line generates a type safety warning, but it cannot be
                 * helped, and in any case it is safe all the same because we
                 * don't rely on the type parameter of the returned ScalarData.
                 */
                CifValue value = block.getScalarForName(name).getValue();
                
                // Test for the two placeholder values
                if (!(value instanceof UnknownValue)
                        && !(value instanceof NAValue)) {
                    
                    // This is necessarilly the best available result
                    return value;
                    
                // test whether a placeholder has already been found
                } else if (!placeholderFound) {
                    
                    /*
                     * This will be the best available result if no other one
                     * in the list is present with a non-placeholder value
                     */
                    rval = value;
                    placeholderFound = true;
                }
            }
        }
        
        return rval;
    }
    
    /**
     * Finds and returns a loop in the specified CIF data block that contains
     * <em>all</em> the specified data names, if one is present
     *   
     * @param  block the {@code DataBlock} in which to look up the value
     * @param  dataNames the data names that must be represented in the desired
     *         loop
     * 
     * @return a {@code DataLoop} from the specified block, containing the
     *         specified data names among its data names, if one exists;
     *         {@code null} if no such block exists or if not data names are
     *         specified
     */
    public static DataLoop findCifLoop(DataBlock block, String... dataNames) {
        DataLoop loop;
        
        // Find the loop, if any, containing the first name
        if (dataNames.length == 0) {
            return null;
        } else if (block.containsNameInLoop(dataNames[0])) {
            loop = block.getLoopForName(dataNames[0]);
        } else {
            return null;
        }
        
        // Check that all remaining names are in the loop
        for (int i = 1; i < dataNames.length; i++) {
            if (!loop.containsName(dataNames[i])) {
                return null;
            }
        }
        
        // Return the loop
        return loop;
    }

    /**
     * Extracts and returns an {@code int} from the specified {@code CifValue}
     * if possible, otherwise returns the specified default value
     * 
     * @param  value the {@code CifValue} from which to extract a value
     * @param  defaultValue the value to return if no numeric value is available
     *         from {@code value}
     *         
     * @return the value represented by {@code value}, if any, otherwise
     *         {@code defaultValue}
     */
    public static int getCifInt(CifValue value, int defaultValue) {
        if (value instanceof NumberValue) {
            return (int) Math.round(((NumberValue) value).getValue());
        } else {
            return defaultValue;
        }
    }

    /**
     * Extracts and returns a {@code double} from the specified {@code CifValue}
     * if possible, otherwise returns the specified default value
     * 
     * @param  value the {@code CifValue} from which to extract a value
     * @param  defaultValue the value to return if no numeric value is available
     *         from {@code value}
     *         
     * @return the value represented by {@code value}, if any, otherwise
     *         {@code defaultValue}
     */
    public static double getCifDouble(CifValue value, double defaultValue) {
        if (value instanceof NumberValue) {
            return ((NumberValue) value).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Extracts and returns a {@code String} from the specified {@code CifValue}
     * if possible, otherwise returns the specified default value
     * 
     * @param  value the {@code CifValue} from which to extract a value
     * @param  defaultValue the value to return if no string value is available
     *         from {@code value}
     * @param  useShelxCompatabilityHack a {@code boolean} flag indicating
     *         whether SHELXL's unfortunate behavior of sometimes outputting a
     *         quoted question mark ('?') into a CIF instead of a bare one (?)
     *         should be handled by treating string values matching the former
     *         (after trimming) as if they were really unknown value
     *         placeholders.
     *         
     * @return the value represented by {@code value}, if any, otherwise
     *         {@code defaultValue}
     */
    public static String getCifString(CifValue value, String defaultValue,
            boolean useShelxCompatabilityHack) {
        if (value instanceof StringValue) {
            String s = ((StringValue) value).getValue();
            
            if (useShelxCompatabilityHack && s.trim().equals("?")) {
                return defaultValue;
            } else {
                return s;
            }
        } else if (value instanceof NumberValue) {
            return String.valueOf(((NumberValue) value).getValue());
        } else {
            return defaultValue;
        }
    }

}
