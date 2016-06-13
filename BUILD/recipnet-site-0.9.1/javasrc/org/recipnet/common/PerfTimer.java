/*
 * Reciprocal Net project
 * 
 * PerfTimer.java
 *
 * 05-Nov-2002: ekoperda wrote first draft
 * 31-Dec-2003: ekoperda moved class from the org.recipnet.site.misc package
 *              to org.recipnet.common
 * 30-Jan-2004: ekoperda added elapsed() and clone()
 * 24-May-2006: jobollin reformatted the source, added generics
 */

package org.recipnet.common;

import java.util.ArrayList;
import java.util.List;

/**
 * This simple utility class can be used to time operations for the purpose of
 * performance monitoring. It supports a tree-like structure of subobjects that
 * are useful when timing suboperations as part of a larger operation.
 */
public class PerfTimer implements Cloneable {
    
    public static final long NOT_REPORTED = -1;

    public String name;

    public long startTime;

    public long stopTime;

    public long duration;

    public List<PerfTimer> children;

    public PerfTimer currentChild;

    public PerfTimer(String name) {
        this.duration = NOT_REPORTED;
        this.name = name;
        this.children = null;
        this.currentChild = null;
        this.stopTime = NOT_REPORTED;
        this.startTime = System.currentTimeMillis();
    }

    public long elapsed() {
        return (stopTime == NOT_REPORTED)
                ? (System.currentTimeMillis() - startTime) : duration;
    }

    public void stop() {
        if (stopTime != NOT_REPORTED) {
            throw new IllegalStateException("Already stopped");
        } else {
            stopTime = System.currentTimeMillis();
            duration = stopTime - startTime;
        }
    }

    public void newChild(String childName) {
        if (children == null) {
            children = new ArrayList<PerfTimer>();
        }
        currentChild = new PerfTimer(childName);
        children.add(currentChild);
    }

    public void stopChild() {
        if (currentChild == null) {
            throw new IllegalStateException("No current subop");
        } else {
            currentChild.stop();
            currentChild = null;
        }
    }

    @Override
    public PerfTimer clone() {
        try {
            PerfTimer x = (PerfTimer) super.clone();

            if (this.children != null) {
                x.children = new ArrayList<PerfTimer>(this.children.size());

                for (PerfTimer child : this.children) {
                    x.children.add(child.clone());
                }
            }

            x.currentChild = null;
            return x;
        } catch (CloneNotSupportedException cnse) {
            throw new IllegalStateException("Can't happen", cnse);
        }
    }
}
