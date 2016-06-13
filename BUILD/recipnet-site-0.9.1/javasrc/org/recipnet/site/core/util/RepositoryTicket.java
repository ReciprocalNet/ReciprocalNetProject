/*
 * Reciprocal Net project
 * @(#)RepositoryTicket.java
 *
 * 08-Jul-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 */

package org.recipnet.site.core.util;

/**
 * RepositoryTicket is an interal container class used by Repository
 * Manager to keep track of file-access "tickets" that have been
 * issued.
 */
public class RepositoryTicket {

    /** 
     * The id of this ticket number.  Guaranteed-unique, cryptographically
     * random ticket numbers can be obtained by calling
     * RepositoryManager.generateTicket().
     */
    public int id;

    /** The sample id to which access has been granted */
    public int sampleId;

    /** 
     * The filesystem directory name that corresponds with the
     * data base directory for the specified sample.
     */
    public String urlFragment;

    /** 
     * The time at which this ticket will expire.  Compare this
     * value to the time obtained by calling System.currentTimeMillis().
     */
    public long expirationTime;
}
