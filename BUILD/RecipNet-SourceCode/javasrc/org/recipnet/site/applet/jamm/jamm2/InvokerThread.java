/*
 * Reciprocal Net Project
 * 
 * InvokerThread.java
 *
 * Copyright (c) 2002-2006, The Trustees of Indiana University
 *
 * 08-Aug-2002: jobollin wrote first draft (as a replacement for ForkObject)
 * 10-Jan-2003: jobollin added file comment and Javadoc comments and
 *              reformatted the source (retroactively assigned to task #749)
 * 13-Mar-2003: jobollin moved this class from org.recipnet.site.jamm to
 *              org.recipnet.site.jamm.jamm2 (task #743)
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2; changed package 
 *              references due to source tree reorganization
 */

package org.recipnet.site.applet.jamm.jamm2;

import org.recipnet.common.WideningPrimitiveConversionChecker;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * a <code>Thread</code> subclass that invokes a named method on a provided
 * object as the body of its <code>run</code> method.  If the specified method
 * throws an exception then the exception is stored in the InvokerThread and
 * can be retrieved for examination later.
 *
 * @author John C. Bollinger
 * @version 0.6.0
 */
class InvokerThread extends Thread {

    /** the object on which to invoke <code>method</code> */
    protected Object target;

    /** a <code>Method</code> representing the method to be invoked */
    protected Method method;

    /** the arguments to <code>method</code> */
    protected Object[] args = null;

    /** an object in which to store the return value of <code>method</code> */
    protected Object result = null;

    /** the <code>Throwable</code> thrown by <code>method</code>, if any */
    protected Throwable exception;

    /**
     * constructs an <code>InvokerThread</code> that when started will invoke a
     * method named by <code>mName</code> on object <code>obj</code> with the
     * elements of <code>ia</code> as arguments
     *
     * @param  obj the <code>Object</code> on which a method is to be invoked
     *         in this thread
     * @param  mName a <code>String</code> containing the name of the method to
     *         invoke, the specified method must be public in
     *         <code>obj</code>'s class
     * @param  ia an array of <code>Object</code>s containing the arguments to
     *         be passed to the method.  The runtime types of these objects are
     *         used to determine which of <code>obj</code>'s methods to invoke
     *         if there is more than one compatible one
     *
     * @throws NoSuchMethodException if <code>obj</code> does not have an
     *         accessible method with a suitable signature
     * @throws SecurityException if reflective access to the class of
     *         <code>obj</code> is denied
     */
    public InvokerThread(Object obj, String mName, Object[] ia)
            throws NoSuchMethodException {
        super();

        Class[] types;

        target = obj;

        if (ia == null) {
            args = new Object[0];
        } else {
            args = new Object[ia.length];
            System.arraycopy(ia, 0, args, 0, ia.length);
        }

        types = new Class[args.length];

        for (int i = 0; i < args.length; i++) {
            types[i] = (args[i] == null) ? null : args[i].getClass();
        }

        try {
            /* try for an exact match */
            method = target.getClass().getMethod(mName, types);
        } catch (NoSuchMethodException nsme) {
            /* try for another match */
            method =
                InvokerThread.findMatchingMethod(target.getClass().getMethods(),
                    mName, types);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected Exception");
        }
        if (method == null) {
            throw new IllegalArgumentException(
                "Could not match arguments to a method" );
        }

        exception = null;
    }

    /**
     * returns whether or not the method invorcation in this
     * <code>Thread</code>'s <code>run()</code> method threw an exception
     *
     * @return <code>true</code> if and only if the method invocation in this
     *         <code>Thread</code>'s <code>run()</code> method threw an
     *         exception.  In particular, will always return false until this
     *         <code>Thread</code> is dead
     */
    public boolean threwException() {
        return (exception != null);
    }

    /**
     * returns the stored exception for this <code>InvokerThread</code>, which
     * will be null unless <code>threwException()</code> returns
     * <code>true</code>
     *
     * @return the exception as a <code>Throwable</code>
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * returns the result of the method invocation if this
     * <code>InvokerThread</code> has completed successfully, or
     * <code>null</code> otherwise
     *
     * @return the <code>Object</code> returned by the method invocation.  This
     *         will be <code>null</code> until this thread has died, and will
     *         also be <code>null</code> if the method has a void return type,
     *         returns the <code>null</code> value, or throws an exception. If
     *         the method returns a primitive type, this method returns it
     *         wrapped in an object of the appropriate wrapper class.
     */
    public Object getResult() {
        return result;
    }

    /**
     * invokes a method according to this <code>InvokerThread</code>'s
     * construction-time configuration (that is, the the named method on the
     * provided object with the specified arguments) and stores the result if
     * successful or the relevant exception if not
     */
    public void run() {
        try {
            result = method.invoke(target, args);
        } catch (IllegalAccessException iae) {
            /*
             * this should never happen here -- we already know we have access
             * to target's Class because we obtained Method method from it
             */
            exception = iae;
        } catch (InvocationTargetException ite) {
            Throwable cause = ite.getTargetException();

            if (cause instanceof Error) {
                throw (Error) cause;
            }

            exception = ite.getTargetException();
        }
    }

    static Method findMatchingMethod(Method[] methods, String name,
            Class[] types) throws NoSuchMethodException {
        List candidates = new LinkedList(Arrays.asList(methods));
        Iterator it = candidates.iterator();

        /* find all accessible and applicable matches */
        while (it.hasNext()) {
            Method m = (Method) it.next();
            int modifiers = m.getModifiers();
            Class[] parameterTypes = m.getParameterTypes();

            if (!name.equals(m.getName())) {
                it.remove();
            } else if (((modifiers & Modifier.ABSTRACT) != 0)
                    || ((modifiers & Modifier.PUBLIC) == 0)
                    || (parameterTypes.length != types.length)) {
                it.remove();
            } else if (!typesAreCompatible(parameterTypes, types)) {
                it.remove();
            }
        }

        if (candidates.size() == 0) {
            throw new NoSuchMethodException(
                "No applicable and accesible method found");
        } else if (candidates.size() > 1) {
            /* find the most specific method */
            SpecificityComparator comp = new SpecificityComparator();
            Collections.sort(candidates, comp);

            if (comp.compare(candidates.get(0), candidates.get(1)) >= 0) {
                /* there is no most specific method */
                throw new NoSuchMethodException(
                    "Ambiguous method specification");
            }
        }

        return (Method) candidates.get(0);
    }

    static boolean typesAreCompatible(Class[] targets, Class[] sources) {
        if (targets.length != sources.length) {
            return false;
        } else {
            for (int i = 0; i < targets.length; i++) {
                if (sources[i] == null) {
                    if (targets[i].isPrimitive()) {
                        return false;
                    }
                } else if (!targets[i].isAssignableFrom(sources[i])) {
                    return (targets[i].isPrimitive())
                    ? WideningPrimitiveConversionChecker.canWidenTo(sources[i],
                        targets[i]) : false;
                }
            }
        }

        return true;
    }

    /**
     * A Comparator implementation that compares the specificity of Method
     * objects according to the rules in the Java Language Specification (JLS
     * 15.11.2.2).
     */
    public final static class SpecificityComparator
            implements Comparator {

        /**
         * A <code>Comparator</code> implementation method; compares two
         * <code>java.lang.reflect.Method</code> objects and returns a value
         * less than  zero if the first is more specific than the second,
         * greater than zero if the second is more specific than the first, or
         * equal to zero if neither is more specific than the other.  Note
         * that this <code>Comparator</code> does not in general impose  a
         * total ordering on any particular collection of
         * <code>Method</code>s.
         *
         * @param o1 the first <code>Object</code> to compare
         * @param o2 the second <code>Object</code> to compare
         *
         * @return &lt; 0 if <code>o1</code> is more specific than
         *         <code>o2</code>, &gt; 0 if <code>o2</code> is more specific
         *         than <code>o2</code>, or 0 otherwise
         *
         * @throws ClassCastException if either <code>o1</code> or
         *         <code>o2</code> is not an instance of <code>Method</code>
         */
        public int compare(Object o1, Object o2) {
            Method m1 = (Method) o1;
            Method m2 = (Method) o2;
            Class c1 = m1.getDeclaringClass();
            Class c2 = m2.getDeclaringClass();
            Class[] types1 = m1.getParameterTypes();
            Class[] types2 = m2.getParameterTypes();
            int rval = 0;

            if (c1.equals(c2)) {
                if (typesAreCompatible(types2, types1)) {
                    rval--;
                }

                if (typesAreCompatible(types1, types2)) {
                    rval++;
                }
            } else if (c2.isAssignableFrom(c1)) {
                rval--;

                if (!typesAreCompatible(types2, types1)) {
                    rval++;
                }
            } else if (c1.isAssignableFrom(c2)) {
                rval++;

                if (!typesAreCompatible(types1, types2)) {
                    rval--;
                }
            }

            return rval;
        }
    }
}
