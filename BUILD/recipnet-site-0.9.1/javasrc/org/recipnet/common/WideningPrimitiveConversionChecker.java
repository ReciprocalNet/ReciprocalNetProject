/*
 * Reciprocal Net Project
 * 
 * WideningPrimitiveConversionChecker.java
 *
 * 11-Mar-2003: jobollin wrote first draft
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package
 *              to org.recipnet.common
 * 25-May-2006: jobollin reformatted the source
 */

package org.recipnet.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that provides the means to check whether a particular primitive type
 * can be subjected to a widening primitive conversion to another specified
 * primitive type. (See the Java Language Specification, 2nd edition, JLS
 * 5.1.2.)
 * 
 * @author John C. Bollinger
 * @version 0.6.0
 */
public final class WideningPrimitiveConversionChecker {
    
    // Note: this source must maintain compatability with Java 1.2

    /**
     * A {@code List} of the {@code Class}es representing the primitive types,
     * from narrowest to widest, with the exception of Character.TYPE; the types
     * represented here are completely ordered with respect to width. This list
     * is used to back the sublists that define legal widening conversions.
     */
    private final static List primitiveTypes = Arrays.asList(
            new Class[] {
                Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE,
                Double.TYPE
            });

    /**
     * a {@code Map} that associates {@code Class}es representing primitive
     * types with {@code List}s of the {@code Class}es representing the types
     * to which they can be widened
     */
    private final static Map widerTypeMap = new HashMap();

    static {
        int nPrimitives = primitiveTypes.size();

        for (int i = 1; i < nPrimitives; i++) {
            widerTypeMap.put(primitiveTypes.get(i - 1),
                primitiveTypes.subList(i, nPrimitives));
        }

        // chars are subject to the same widening conversions as shorts
        widerTypeMap.put(Character.TYPE, widerTypeMap.get(Short.TYPE));
    }

    /**
     * Nominally, initializes a new {@code WideningPrimitiveConversionChecker};
     * in practice, however, this constructor exists to prevent a default
     * constructor from being generated, and has the effect of preventing this
     * class from being instantiated
     */
    private WideningPrimitiveConversionChecker() {
        // does nothing
    }

    /**
     * Checks whether the primitive type represented by {@code type1} can be
     * widened to the primitive type represented by {@code type2}
     * 
     * @param type1 the {@code Class} representing a primitive type that may be
     *        subject to conversion
     * @param type2 the {@code Class} representing a primitive type that is in
     *        question as a wider type
     * @return {@code true} if {@code type1} and {@code type2} both represent
     *         primitive types and the type represented by {@code type1} is
     *         subject to a widening primitive conversion to the type
     *         represented by {@code type2}; {@code false} otherwise
     */
    public static boolean canWidenTo(Class type1, Class type2) {
        List widerTypes = (List) widerTypeMap.get(type1);
        
        return (widerTypes == null) ? false : widerTypes.contains(type2);
    }
}
