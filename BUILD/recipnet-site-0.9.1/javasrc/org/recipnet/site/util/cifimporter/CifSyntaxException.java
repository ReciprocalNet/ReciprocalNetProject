/*
 * Reciprocal Net project
 * @(#)CifSyntaxReader.java
 *
 * 10-Jun-2002: jobollin wrote first draft
 * 16-Feb-2004: jobollin updated version number format in class javadocs
 */

package org.recipnet.site.util.cifimporter;

import java.io.IOException;

/**
 * an <code>IOException</code> subclass that may be thrown to indicate a
 * CIF syntax error. {@link CifSyntaxReader CifSyntaxReader}s in particular may
 * throw these exceptions from their <code>read</code> methods.  Instances of
 * this class have exactly the same properties and behaviors as instances
 * of <code>IOException</code>; this class exists so that
 * <code>CifSyntaxReader</code>s can conform to the contract imposed by their
 * <code>Reader</code> ancestor class while still enabling their clients to
 * distinguish CIF syntax errors from other errors.
 *
 * @author John C. Bollinger
 * @version 0.6.2
 *
 * @see CifSyntaxReader
 */
public class CifSyntaxException extends IOException {

    CifError cifError;

    /**
     * constructs a <code>CifSyntaxException</code> with no message
     */
    public CifSyntaxException() {
        super();
        cifError = null;
    }

    /**
     * constructs a <code>CifSyntaxException</code> with <code>s</code> as
     * its message
     *
     * @param  s a <code>String</code> containing the message this exception
     *         should report
     */
    public CifSyntaxException(String s) {
        super(s);
        cifError = null;
    }

    /**
     * constructs a <code>CifSyntaxException</code> based on a
     * <code>CifError</code> object
     *
     * @param  ce a <code>CifError</code> representing the condition that
     *         produced this exception
     */
    public CifSyntaxException(CifError ce) {
        super(CifError.messageForCode(ce.getCode()));
        cifError = ce; // CifErrors are immutable
    }

    /**
     * returns the <code>CifError</code> carried by this exception
     *
     * @return the <code>CifError</code>; will be <code>null</code> unless
     *         this <code>CifSyntaxException</code> was constructed based on
     *         a <code>CifError</code>
     */
    public CifError getCifError() {
        return cifError;
    }
}

