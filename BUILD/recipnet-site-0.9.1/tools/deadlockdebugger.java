/**
 * Reciprocal Net project
 * @(#)deadlockdebugger.java
 * 
 * 28-Oct-2003: midurbin wrote the first draft
 * 11-Jun-2004: ekoperda fixed bug 1240 in main()
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is a command-line utility program that reads and parses the system
 * log file, detects the most recent deadlock debugging information and
 * displays it in an easy-to-read format.
 */
public class deadlockdebugger {

    /**
     * a String that exists between the date and the message, for every
     * recipnet log message.
     */
    //public static String logStamp = " bl-chem-iumsc24 recipnetd: ";
    public static String logStamp = " recipnetd: ";

    public static String logLocation = "/var/log/messages";

    /**
     * No parameters are requred, though the default <code>logStamp</code> and
     * <code>logLocation</code> values can be overridden.
     */
    public static void main(String args[]) throws Exception {

        // if parameters are given, use to override logStamp, and logLocation
        // respectively.
        if (args.length == 2) {
            logStamp = args[0];
            logLocation = args[1];
        }

        // Open syslog
        File logfile = new File(logLocation);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(logfile));
        } catch (IOException ioe) {
            System.err.println("Unable to read from \"" + logLocation + "\".");
            System.exit(1);
        }

        // find instance of deadlock
        LogEntry le = null;
        LogEntry le2 = null;
        LogEntry lastDeadlockInstance = null;
        do {
            lastDeadlockInstance = le;
            do {
                // read entries until you find one containing deadlock
                // debugging info
                le = LogEntry.parseLogEntry(reader);
            } while (le != null && !le.isDeadlockDebugInfo());

            if (le != null) {
                do {
		    le2 = LogEntry.parseLogEntry(reader);
                    if (le2 != null && le2.dateStr.equals(le.dateStr)) {
                        // append all subsequent entries that have the same
                        // time-stamp to the last entry
                        le.message = le.message + le2.message;
                    } else if (le2 != null && le2.isDeadlockDebugInfo()) {
                        // replace current deadlock message with a new, more
                        // recent one.
                        le = le2;
                    }
                } while (le2 != null 
                        && (le.dateStr.equals(le2.dateStr)));
            }
        } while (le != null);

        System.out.println("Last Deadlock Detected at: "
                + lastDeadlockInstance.dateStr);
        lastDeadlockInstance.printFormattedDebugInfo();

    }

    public static class LogEntry {

        /** The date of the log message  */
        String dateStr;

        /** The text of the message */
        String message;

        public LogEntry() {
            dateStr = null;
            message = null;
        }

        /**
         * determines if this LogEntry is one containing deadlock
         * debugging info
         */
        public boolean isDeadlockDebugInfo() {
            return (message.indexOf("debugging info:") != -1); 
        }

        /**
         * formats and prints the debugging info contained in this
         * <code>LogEntry</code> to System.out
         * @throws IllegalStateException if there is no debugging info 
         */
        public String printFormattedDebugInfo() {
            if (!isDeadlockDebugInfo()) {
                throw new IllegalStateException();
            }

            System.out.println();
            printPair("Free DB Connections",
                    this.message.substring(
                            this.message.indexOf("freeDbConn=") + 11,
                            this.message.indexOf(" ",
                                    this.message.indexOf("freeDbConn"))), 20);
            printPair("Busy DB Connections",
                    this.message.substring(
                            this.message.indexOf("busyDbConn=") + 11,
                            this.message.indexOf(" ",
                                    this.message.indexOf("busyDbConn"))), 20);

            int dindex = this.message.indexOf("desired=[");
            int iindex = this.message.indexOf("ignore=[");
            int aindex = this.message.indexOf("active=[");
            int pindex = this.message.indexOf("pending=[");

            String desiredStr = this.message.substring(
                dindex + 8, iindex -1);
            System.out.println("\n\nDESIRED:");
            printElements(desiredStr, 10);

            String ignoreStr = this.message.substring(
                iindex + 7, this.message.indexOf("freeDbConn") - 1);
            System.out.println("\n\nIGNORE:");
            printElements(ignoreStr, 10);

            String activeStr = this.message.substring(
                aindex + 7, pindex -1);
            System.out.println("\n\nACTIVE:");
            printElements(activeStr, 10);
            
            String pendingStr = this.message.substring(
                pindex + 8, this.message.length() - 1);
            System.out.println("\n\nPENDING:"); 
            printElements(pendingStr, 10);
            return null;
        }

        /**
         * This function determines the 'depth' of substrings based on the
         * location and direction of brackets and calls
         * <code>printValues()</code> with appropriately 'indent' values to
         * make the nesting of brackets easily readable.
         * @param a <code>String</code> to be formatted
         * @param indent the column number at which the value should be
         *     printed  (must be greater than the length of any field name)
         */
        private void printElements(String str, int indent) {
            int index = 0;
            while (index < str.length() - 1) {
                int nextOpen = str.indexOf("[", index + 1);
                int nextClose = str.indexOf("]", index + 1);
                int nextBreak = (nextOpen < nextClose && nextOpen != -1
                        ? nextOpen : nextClose);
                if (nextBreak != -1 && ((nextBreak - index) > 1)) {
                    // parse the name/value pairs
                    printValues(str.substring(index + 1, nextBreak), indent);
                }

                if (nextBreak == -1) {
                    // end of parsible data
                    return;
                } else if (nextBreak == nextOpen) {
                    // the next group is a child
                    indent += 5;
                } else if (nextBreak == nextClose) {
                    // back to the parent
                    indent -= 5;
                } else {
                    // will never happen
                }
                index = nextBreak;
            }
        }

        /**
         * A helper function that extracts listed values and displays them
         * @param str a list of name=value pairs separated by spaces
         * @param indent the column number at which the value should be
         *     printed  (must be greater than the length of any field name)
         */         
        private void printValues(String currentStr, int indent) {

                    String pairs[] = currentStr.split(" ");
                    for (int j = 0; j < pairs.length; j ++) {
                        if (pairs[j].indexOf("=") == -1) {
                            System.out.println();
                            printPair("locktype", pairs[j], indent);
                        } else if (pairs[j].indexOf("=")
                                == pairs[j].length() - 1 ) {
                            printPair("children", "", indent);
                        } else {
                            String nv[] = pairs[j].split("=");
                            printPair(nv[0], nv[1], indent);
                        }
                    }
        
        }

        /**
         * Prints a formatted label and it's value.  The label is right-aligned
         * to a ':' (colon) that is positioned in column <code>indent</code>.
         * The value is left-aligned to the same colon.
         * @param label the left field
         * @param value the right field
         */
        public static void printPair(String label, String value,
                int indent) {
            for (int i=0;i<indent - label.length();i++) {
                System.out.print(" ");
            }
            System.out.println(label + ": " + value);
        }

        /**
         * Reads a line from the reader and converts it into a recipnet
         * LogEntry.  Non-recipnet log entries are skipped.  If no recipnet
         * log entries exist, null is returned.
         * @param reader a <code>Reader</code> reading the log.  Reader
         *     will be updated to point to the next line after the end of the
         *     last recipnet log record.
         */
        public static LogEntry parseLogEntry(BufferedReader reader)
                throws IOException {
            LogEntry le = new LogEntry();
            String firstline = null;
            do {
                firstline = reader.readLine();
                if (firstline == null) {
                    return null;
                }
	    } while (firstline.indexOf(logStamp) == -1);
            String parts[] = firstline.split(logStamp);
            le.dateStr = parts[0];
            le.message = parts[1];
            return le;
        }
    }
}
