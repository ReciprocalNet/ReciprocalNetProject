/*
 * Reciprocal Net project
 * 
 * OutOfBandReadTicket.java
 *
 * 27-May-2003: ekoperda wrote first draft
 * 08-Jul-2003: ekoperda added toString()
 * 17-Jul-2003: midurbin fixed bug #948 in constructor
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 02-May-2005: ekoperda modified constructor to match changes in 
 *              RepositoryFiles and utilized generics throughout class
 * 30-May-2006: reformatted the source and removed unused imports
 */

package org.recipnet.site.core.lock;

import java.util.Collection;
import java.util.HashSet;

import org.recipnet.site.shared.RepositoryFiles;

/**
 * <p>
 * A subclass of {@code RepositoryTicket} that enables ticket users to read from
 * particular files spread between a particular sample/version's primary
 * repository directory and secondary repository via some out-of-band means
 * (like direct filesystem access). This kind of ticket does not supply file
 * reading or writing services to ticket users.
 * </p><p>
 * This kind of lock is not disrupted by any other locks beyond those detected
 * by {@code AbstractLock}. This is because this kind of lock is low-priority
 * and should never prevent normal uploads, scans, etc. from taking place. In
 * the real world, a user accessing a primary directory through one of these
 * tickets might experience undesirable results from concurrent file removals,
 * etc., but this is an acceptable design tradeoff.
 * </p><p>
 * This class is thread-safe.
 * </p>
 */
public class OutOfBandReadTicket extends RepositoryTicket {

    /** Set at construction time. */
    private int sampleId;

    /** Set at construction time. */
    private int sampleHistoryId;

    /**
     * A {@code Collection} of {@code String}'s set at construction time.
     */
    private Collection<String> validUrlFragments;

    /**
     * Initializes a new {@code OutOfBandReadTicket} with the specified
     * parameters
     * 
     * @param userId the user ID granted read authority by this {@code Ticket}
     * @param timeUntilExpiration the approximate number of milliseconds after
     *        ticket granting that the ticket should expire. This value is used
     *        to invoke {@code AbstractLock.setExpiration()}.
     * @param files the {@code RepositoryFiles} object that was made available
     *        to the ticket user and that describes the set of data files for a
     *        particular sample/version. From this object the sample id, sample
     *        history id, and collection of valid URL fragments can be
     *        extracted.
     * @throws IllegalArgumentException if any of the URLs contained within
     *         {@code files} are unparsable.
     */
    public OutOfBandReadTicket(int userId, long timeUntilExpiration,
            RepositoryFiles files) {
        super(userId, timeUntilExpiration);
        this.sampleId = files.getSampleId();
        this.sampleHistoryId = files.getSampleHistoryId();
        this.validUrlFragments = new HashSet<String>();
        for (RepositoryFiles.Record file : files.getRecords()) {
            String urlString = file.getUrl();
            
            if (urlString == null) {
                /*
                 * The current file must not have been immediately available for
                 * download. Continue to the next one.
                 */
                continue;
            }
            try {
                /*
                 * cut off all but the path from the URL by finding the first
                 * '/' character that is not part of the protocol portion of the
                 * URL. This technique assumes that there are no query line
                 * parameters on the URL.
                 */
                int endOfProtocolIndex = urlString.indexOf("://");
                String urlFragment = urlString.substring(urlString.indexOf("/",
                        (endOfProtocolIndex == -1 ? 0 : endOfProtocolIndex + 3)));
                
                validUrlFragments.add(urlFragment);
            } catch (IndexOutOfBoundsException ex) {
                // there was no '/' to indicate the end of the hostname
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Evaluates a specified URL fragment for validity and renews the ticket.
     * 
     * @return true if {@code urlFragment} is valid, according to the
     *         {@code RepositoryFiles} object this ticket was passed at
     *         construction time; false otherwise.
     * @param urlFragment the URL fragment (excluding protocol, hostname, and
     *        port) to be evaluated.
     */
    public boolean isUrlFragmentValid(String urlFragment) {
        renew();
        return this.validUrlFragments.contains(urlFragment);
    }

    /** @return the sample id supplied at construction time. */
    public int getSampleId() {
        return this.sampleId;
    }

    /** @return the sample history id supplied at construction time. */
    public int getSampleHistoryId() {
        return this.sampleHistoryId;
    }

    /**
     * Overrides function on {@code AbstractLock} -- for debugging use only.
     */
    @Override
    public String toString() {
        return super.toString() + " sampleId=" + this.sampleId
                + " sampleHistoryId=" + this.sampleHistoryId + " validUrls="
                + this.validUrlFragments.size();
    }
}
