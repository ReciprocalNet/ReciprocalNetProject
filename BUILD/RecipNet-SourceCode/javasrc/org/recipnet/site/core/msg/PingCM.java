/*
 * Reciprocal Net project
 * @(#)PingCM.java
 *
 * 07-Jun-2002: ekoperda wrote first draft
 * 25-Jun-2002: ekoperda added serialization code
 * 27-Sep-2002: ekoperda moved the class into the core.msg package, from the
 *              container package
 */

package org.recipnet.site.core.msg;
import java.io.Serializable;

/**
 * PingCoreMessage is a message that a core module sends to itself - from one
 * thread to another.  It instructs the worker thread to signal the Event 
 * Signal named pingSignal in that class.  Higher-level code uses this function
 * to determine whether the worker thread is currently running/responsive.
 */
public class PingCM extends CoreMessage implements Serializable {
    /** 
     * There aren't any members of this class because no
     * additional data is required for this request.
     */
}

