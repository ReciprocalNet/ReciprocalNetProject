/*
 * Reciprocal Net project
 *  
 * SampleStats.java
 *
 * 14-Jun-2002: hclin wrote empty class
 * 25-Jun-2002: ekoperda added serialization code
 * 08-Aug-2002: ekoperda wrote first draft
 * 30-Aug-2002: ekoperda changed spec of recordDbMultipleFetch to take times
 *              for only 6 subqueries instead of 7; this is necessary because
 *              the sampleAtoms table went away
 * 05-Nov-2002: ekoperda rewrote the implementation to support the new 
 *              PerfTimer class
 * 17-Dec-2002: ekoperda added the resetDate field and code to update it
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared; also changed package references
 *              due to source tree reorganization
 * 01-Jun-2006: jobollin reformatted the source and implemented generics
 */

package org.recipnet.site.shared;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.recipnet.common.PerfTimer;
import org.recipnet.site.UnexpectedExceptionException;

/**
 * Contains statistics and counters about Sample Manager's internal operations.
 * This class is thread-safe.
 */
public class SampleStats implements Serializable, Cloneable {
    private Map<String, Counter> rootCounters;

    private Date resetDate;

    public SampleStats() {
        rootCounters = new TreeMap<String, Counter>();
        resetDate = new Date();
    }

    public synchronized void record(PerfTimer perfTimer) {
        perfTimer.stop();
        Counter counter = rootCounters.get(perfTimer.name);
        if (counter == null) {
            counter = new Counter(perfTimer.name);
            rootCounters.put(counter.name, counter);
        }
        counter.updateWith(perfTimer);
    }

    public synchronized void record(PerfTimer perfTimer, String newName) {
        perfTimer.name = newName;
        record(perfTimer);
    }

    /**
     * Returns another SampleStats whose counters are set identically to this
     * one.
     */
    @Override
    public synchronized SampleStats clone() {
        try {
            SampleStats x = (SampleStats) super.clone();
            x.rootCounters = new TreeMap<String, Counter>(this.rootCounters);
            return x;
        } catch (CloneNotSupportedException cnse) {
            // Can't happen because this class is Cloneable
            throw new UnexpectedExceptionException(cnse);
        }
    }

    public synchronized void reset() {
        rootCounters.clear();
        resetDate = new Date();
    }

    /**
     * Writes a textual representation of the counters in this object to the
     * supplied PrintStream. This might be used to generate a pretty display for
     * the user, for instance.
     */
    public synchronized void printToStream(PrintStream out) {
        out.println("Sample Manager statistics - last reset " + resetDate);
        for (Counter counter : rootCounters.values()) {
            counter.printToStream("", out);
        }
    }

    private static class Counter implements Serializable, Comparable<Counter> {
        String name;

        Map<String, Counter> children;

        int count;

        long sum;

        long min;

        long max;

        public Counter(String name) {
            this.name = name;
            this.children = null;
            this.count = 0;
            this.sum = 0;
            this.min = -1;
            this.max = -1;
        }

        public void updateWith(PerfTimer perfTimer) {
            if (perfTimer.duration != PerfTimer.NOT_REPORTED) {
                // Take care of our own counters.
                count++;
                sum += perfTimer.duration;
                if ((perfTimer.duration < min) || (min == -1)) {
                    min = perfTimer.duration;
                }
                if ((perfTimer.duration > max) || (max == -1)) {
                    max = perfTimer.duration;
                }
            }

            // Create any necessary children and update their sub-counters.
            if (perfTimer.children != null) {
                for (PerfTimer childTimer : perfTimer.children) {
                    if (children == null) {
                        children = new TreeMap<String, Counter>();
                    }

                    Counter childCounter = children.get(childTimer.name);

                    if (childCounter == null) {
                        childCounter = new Counter(childTimer.name);
                        children.put(childCounter.name, childCounter);
                    }
                    childCounter.updateWith(childTimer);
                }
            }
        }

        public void printToStream(String prefix, PrintStream out) {
            // Display our counter's info
            out.println(prefix + name + ":");
            out.println(prefix + "    count=" + count);
            if (count > 0) {
                out.println(prefix + "    avg=" + sum / count);
            }
            if ((min != -1) && (count > 1)) {
                out.println(prefix + "    min=" + min);
            }
            if ((max != -1) && (count > 1)) {
                out.println(prefix + "    max=" + max);
            }

            // Let all our child counters display their info
            if (children != null) {
                for (Counter child : children.values()) {
                    child.printToStream(prefix + "  ", out);
                }
            }
        }

        public int compareTo(Counter o) {
            return this.name.compareTo(o.name);
        }
    }
}
