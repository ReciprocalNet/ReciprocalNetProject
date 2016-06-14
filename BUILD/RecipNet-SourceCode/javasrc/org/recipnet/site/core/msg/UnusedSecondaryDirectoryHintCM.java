/*
 * Reciprocal Net project
 * @(#)UnusedDirectoryHintCM.java
 *
 * 18-Aug-2004: cwestnea wrote first draft
 */
package org.recipnet.site.core.msg;

/**
 * This message tells repository manager that a secondary directory has no more
 * tickets associated to it, and may possibly be removed if no tickets have 
 * been added.
 */
public class UnusedSecondaryDirectoryHintCM extends CoreMessage {
    /**
     * An object that may be used as a key to get the directory which is no 
     * longer being used. The key is gotten from 
     * <code>DirectoryRecord.getKey()</code>.
     */
    public Object directoryKey;

    public UnusedSecondaryDirectoryHintCM(Object directoryKey) {
        this.directoryKey = directoryKey;
    }
}
