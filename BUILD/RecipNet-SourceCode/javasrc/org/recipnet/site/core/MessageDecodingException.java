/*
 * Reciprocal Net Project
 * 
 * MessageDecodingException.java
 *
 * 12-Feb-2003: jobollin autogenerated skeleton source from UML model
 * 12-Feb-2003: jobollin cleaned up and completed the autogenerated source
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.core;

import org.recipnet.site.shared.db.SiteInfo;

/**
 * An {@code IntersiteException} subclass for use when a communication from the
 * remote system is malformed (at the application level)
 */
public class MessageDecodingException extends IntersiteException {

    /**
     * Creates a new {@code MessageFormatException}
     */
    public MessageDecodingException() {
        this(null, SiteInfo.INVALID_SITE_ID, null, null, null);
    }

    /**
     * Creates an {@code MessageFormatException} with the specified site id,
     * remote system address, malformed message, and cause
     * 
     * @param siteId the id of the remote site; may be
     *        org.recipnet.site.container.SiteInfo.INVALID_SITE_ID if the site
     *        id is unknown or does not exist
     * @param address a {@code String} containing the IP address or host name of
     *        the remote system with which communication failed; may be
     *        {@code null} if unknown
     * @param badMessage a {@code String} containing the malformed message
     * @param cause the {@code Throwable} cause of this exception; may be
     *        {@code null}
     * @see org.recipnet.site.shared.db.SiteInfo SiteInfo
     */
    public MessageDecodingException(int siteId, String address,
            String badMessage, Throwable cause) {
        this(null, siteId, address, badMessage, cause);
    }

    /**
     * Creates a new {@code MessageFormatException} with the specified detail
     * message, site id, remote system address, malformed message, and cause
     * 
     * @param message the detail message for this exception
     * @param siteId the id of the Reciprocal Net site with which communication
     *        failed, or org.recipnet.site.container.SiteInfo.INVALID_SITE_ID if
     *        not known or non-existant
     * @param address a {@code String} containing the IP address or host name of
     *        the remote system with which communication failed
     * @param badMessage a {@code String} containing the malformed message
     * @param cause the {@code Throwable} cause of this exception; may be
     *        {@code null}
     * @see org.recipnet.site.shared.db.SiteInfo SiteInfo
     */
    public MessageDecodingException(String message, int siteId, String address,
            String badMessage, Throwable cause) {
        super(message, siteId, address, badMessage);
        
        // TODO: this isn't really right; a null cause is still a cause:
        if (cause != null) {
            initCause(cause);
        }
    }

    /**
     * Returns the bad message that caused this exception. This is the same
     * object returned by {@code getForeignObject()}, but typed as a
     * {@code String}.
     * 
     * @return a {@code String} containing the bad message
     */
    public String getBadMessage() {
        return (String) getForeignObject();
    }

}