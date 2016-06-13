/*
 * Reciprocal Net project
 * @(#)LocalLabsChangedCM.java
 *
 * 26-Jun-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda moved the class into the core.msg package, from the
 *              container package
 */

package org.recipnet.site.core.msg;
import java.io.Serializable;

/**
 * LocalLabsChangedCoreMessage is a message that a Site Manager sends to the
 * other core modules whenever the list of labs hosted at the local site
 * changes.  This might happen while the core is running if a lab is 
 * deactivated, if a lab's home site is changed, and so forth.
 *
 * The message carries no information; the modules that receive this
 * message should call Site Manager's getLocalLabs() function to
 * retrieve the updated list.
 */
public class LocalLabsChangedCM extends CoreMessage implements Serializable {
    /** 
     * There aren't any members of this class because no
     * additional data is required for this request.
     */
}

