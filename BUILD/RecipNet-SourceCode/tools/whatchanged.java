/*
 * Reciprocal Net project
 *
 * whatchanged.java
 *
 * 04-Sep-2002: ekoperda wrote first draft
 * 22-Oct-2004: ekoperda fixed bug #1448 in invokeCvs() and 
 *              DummyInputStreamServicer class
 * 02-Nov-2004: midurbin fixed bug #1456 in readRevisionRec()
 * 08-Jan-2008: ekoperda changed convertDate() to recognize dates with dashes
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a command-line utility program that invokes cvs to determine what
 * code has been changed (in CVS) between two specified release tags.  A
 * human-readable list of features added and deleted is written to stdout.
 *
 * Usage: whatchanged terse /usr/bin/cvs recipnet recipnet-0-5-0-45 recipnet-0-5-0-50
 */
public class whatchanged {
    static String cvsPath;
    static String moduleName;
    static String tag1;
    static String tag2;

    // contains a bunch of Strings representing every detected release tag
    static Set<String> releaseTags;

    // contains a bunch of FileRec's representing every file record read
    static Set<FileRec> files;
        
    // maps integer task numbers to TaskRec's that represent the tasks that 
    //   have been done (and undone) since tag1 and up to tag2
    static Map<Integer, TaskRec> tasksDone;
    static Map<Integer, TaskRec> tasksUndone;

    public static void main(String args[]) throws InterruptedException {
        if (args.length != 5) {
            showUsage();
            System.exit(1);
        }
        
        String mode = args[0];
        cvsPath = args[1];
        moduleName = args[2];
        tag1 = args[3];
        tag2 = args[4];
        releaseTags = new HashSet<String>();
        files = new HashSet<FileRec>();
        
        invokeCvs();

        if (!releaseTags.contains(tag1)) {
            System.out.println("Release tag '" + tag1 + "' is not known!");
            System.exit(2);
        }
        if (!releaseTags.contains(tag2)) {
            System.out.println("Release tag '" + tag2 + "' is not known!");
            System.exit(2);
        }
        
        extractTasks();

        if (mode.equals("terse")) {
            displayTasksTersely();
        } else {
            displayTasksVerbosely();
        }
    }

    public static void showUsage() {
        System.out.println("Reciprocal Net project");
        System.out.println("whatchanged tool");
        System.out.println("  Usage: whatchanged {terse|verbose} <cvspath> <module> <tag1> <tag2>");
        System.out.println("");
        System.out.println("Displays a human-readable listing of all the");
        System.out.println("code that changed (features being added");
        System.out.println("and/or deleted) in the specified module");
        System.out.println("between releases tag1 and tag2.  cvspath is");
        System.out.println("the full path to the CVS command-line utility");
    }

    public static void invokeCvs() throws InterruptedException {
        try {
            String args[] = new String[] { cvsPath, "rlog", moduleName };
            Process proc = Runtime.getRuntime().exec(args);

            // Spawn a thread that does nothing but service the process's
            // error stream.  There is no need to save that data.
            new DummyInputStreamServicer(proc.getErrorStream()).start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(proc.getInputStream()));

            for (; readFileRec(reader); ) {
                /* do nothing else */
            }

            reader.close();
            proc.waitFor();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static boolean readFileRec(BufferedReader reader) throws IOException {
        String line;
        int rc;
        FileRec rec = new FileRec();

        // Line 1 (of a record) is always blank
        if (reader.readLine() == null) {
            return false;
        }

        // Line 2 contains the filename
        line = reader.readLine();
        if (line == null) {
            return false;
        }
        rec.filename = line.substring(
                line.indexOf(moduleName) + moduleName.length() + 1, 
                line.length() - 2);

        // Line 3 contains the head revision
        line = reader.readLine();
        if (line == null) {
            return false;
        }
        rec.headRevision = line.substring(6).intern();

        // Ignore lines 4 through 7
        for (int i = 0; i < 4; i++) {
            if (reader.readLine() == null) {
                return false;
            }
        }

        // Beginning at line 8 is a list of release tags on this file.  Read
        // them all.
        for (;;) {
            line = reader.readLine();
            if (line == null) {
                return false;
            } else if (!line.startsWith("\t")) {
                break;
            }

            int pos = line.indexOf(":");
            String tag = line.substring(1, pos);
            String revision = line.substring(pos + 2).intern();

            rec.tags.put(tag, revision);
            releaseTags.add(tag);
        }

        // The current line and the next three can be ignored.
        for (int i = 0; i < 3; i++) {
            if (reader.readLine() == null) {
                return false;
            }
        }

        // Now read revision records until the end delimiter is reached
        do {
            rc = readRevisionRec(reader, rec);
        } while (rc > 0);
        if (rc < 0) {
            return false;
        }
        files.add(rec);

        return true;
    }

    static int readRevisionRec(BufferedReader reader, FileRec filerec)
             throws IOException {
        RevisionRec rec = new RevisionRec();
        int startpos, endpos;
        String line;

        // Line 1 contains the revision number
        line = reader.readLine();
        if (line == null || line.startsWith("========================")) {
            return -1;
        }    
        if (line.startsWith("----------------")) {
            // We got a bum line somehow; read another one
            line = reader.readLine();
            if (line == null) {
                return -1;
            }
        }        
        rec.revision = line.substring(9).intern();
        
        // Line 2 contains the date, author, state, and lines
        line = reader.readLine();
        if (line == null) {
            return -1;
        }
        startpos = line.indexOf("date: ") + 6;
        endpos = line.indexOf(";", startpos);
        rec.date = line.substring(startpos, endpos);

        startpos = line.indexOf("author: ") + 8;
        endpos = line.indexOf(";", startpos);
        rec.author = line.substring(startpos, endpos);
        
        startpos = line.indexOf("state: ") + 7;
        endpos = line.indexOf(";", startpos);
        rec.state = line.substring(startpos, endpos);

        startpos = line.indexOf("lines: ") + 7;
        if (startpos < 7) {
            rec.lines = "";
        } else {
            endpos = line.indexOf(";", startpos);
            if (endpos < 0) {
                endpos = line.length();
            }
            rec.lines = line.substring(startpos, endpos);
        }

        // The remaining lines contain comments
        rec.comment = "";
        for (;;) {
            line = reader.readLine();
            if (line == null) {
                return -1;
            }
            if (line.indexOf("--~~~~~~~~~") != -1) {
                // these lines appear sometimes right after the last revision
                // for a file that was originally on a separate branch and can
                // be skipped
                continue;
            }
            if (line.startsWith("================")) {
                filerec.revisions.put(rec.revision, rec);
                return 0;
            }
            if (line.startsWith("----------------")) {
                filerec.revisions.put(rec.revision, rec);
                return 1;
            }

            if (rec.comment.length() > 0) {
                rec.comment += "\n";
            }
            rec.comment += line;
        }
    } 

    static void extractTasks() {
        tasksDone = new HashMap<Integer, TaskRec>();
        tasksUndone = new HashMap<Integer, TaskRec>();

        // Iterate through every file that CVS told us about
        for (FileRec file : files) {
            String revision1 = file.tags.get(tag1);
            String revision2 = file.tags.get(tag2);

            // Iterate through each revision of the current file; only pay
            // attention to those revisions that fell in between release tags
            // 1 and 2.
            for (RevisionRec rev : file.revisions.values()) {

                if (rev.isWithin(revision1, revision2)) {
                    // Update the corresponding task record in the "done" list

                    for (RawTask rawTask : rev.getTasks()) {
                        TaskRec task = tasksDone.get(rawTask.id);

                        if (task == null) {
                            task = new TaskRec(rawTask.id);
                            tasksDone.put(task.id, task);
                        }

                        task.authors.add(rev.author);
                        task.dates.add(rev.date);
                        task.files.put(file.filename, rev.lines);
                        task.comments.add(rawTask.comments);
                    }
                }

                if (rev.isWithin(revision2, revision1)) {
                    // Update the corresponding task record in the "undone" list

                    for (RawTask rawTask : rev.getTasks()) {
                        TaskRec task = tasksUndone.get(rawTask.id);

                        if (task == null) {
                            task = new TaskRec(rawTask.id);
                            tasksUndone.put(task.id, task);
                        }

                        task.authors.add(rev.author);
                        task.dates.add(rev.date);
                        task.files.put(file.filename, rev.lines);
                        task.comments.add(rawTask.comments);
                    }
                }
            }
        }
    }

    static void displayTasksTersely() {
        List<TaskRec> tasksToDisplay
                = new ArrayList<TaskRec>(tasksDone.values());

        Collections.sort(tasksToDisplay);
        tasksToDisplay.addAll(tasksUndone.values());

        for (TaskRec task : tasksToDisplay) {
            StringBuffer line = new StringBuffer();

            line.append(task.id);
            line.append('\t');

            line.append(task.authors.iterator().next());
            line.append('\t');

            line.append(task.dates.iterator().next());
            line.append('\t');

            line.append(task.comments.iterator().next());

            for (Map.Entry<String, String> e : task.files.entrySet()) {
                String lines = e.getValue();

                line.append('\t');
                line.append(e.getKey());

                if (lines.trim().length() > 0) {
                    line.append(' ');
                    line.append(lines);
                }
            }

            System.out.println(line.toString());
        }
    }

    static void displayTasksVerbosely() {
        List<TaskRec> tasksToDisplay;

        System.out.println("Features gained:");
        System.out.println("================================");
        tasksToDisplay = new ArrayList<TaskRec>(tasksDone.values());
        Collections.sort(tasksToDisplay);
        for (TaskRec task : tasksToDisplay) {
            StringBuffer line = new StringBuffer();

            line.append("Task #");
            line.append(task.id);
            line.append(" by ");
            line.append(task.authors.iterator().next());
            line.append(" on ");
            line.append(new SimpleDateFormat("E d MMM h:mm aa").format(
                    convertDate(task.dates.iterator().next())));
            line.append(":\n");
            String comments = task.comments.iterator().next();
            line.append(comments.length() > 0 
                        ? comments 
                        : "(comments missing)");

            System.out.println(line);

            for (Map.Entry<String, String> e : task.files.entrySet()) {
                String filename = e.getKey();
                String lines = e.getValue();

                if (lines.length() == 0) {
                    // Detect when a file is newly-created
                    lines = "(new)";
                }
                if (filename.indexOf("/Attic/") > 0) {
                    // Detect when a file has been removed
                    int pos = filename.indexOf("/Attic/");
                    filename = filename.substring(0, pos)
                        + filename.substring(pos + 6);
                    lines = "(deleted)";
                }

                System.out.println("      " + filename + " " + lines);
            }
            System.out.println();
        }
        if (tasksToDisplay.isEmpty()) {
            System.out.println("  (none)");
        }

        System.out.println("\n\nFeatures lost:");
        System.out.println("================================");
        tasksToDisplay = new ArrayList<TaskRec>(tasksUndone.values());
        Collections.sort(tasksToDisplay);

        for (TaskRec task : tasksToDisplay) {
            StringBuffer line = new StringBuffer();

            line.append("Task #");
            line.append(task.id);
            line.append(" by ");
            line.append(task.authors.iterator().next());
            line.append(" on ");
            line.append(new SimpleDateFormat("E d MMM h:mm aa").format(
                    convertDate(task.dates.iterator().next())));
            line.append(":\n");
            String comments = task.comments.iterator().next();
            line.append(comments.length() > 0 
                        ? comments 
                        : "(comments missing)");

            System.out.println(line);

            for (Map.Entry<String, String> e : task.files.entrySet()) {
                String filename = e.getKey();
                String lines = e.getValue();

                if (lines.length() == 0) {
                    // Detect when a file is newly-created
                    lines = "(new)";
                }
                if (filename.indexOf("/Attic/") > 0) {
                    // Detect when a file has been removed
                    int pos = filename.indexOf("/Attic/");
                    filename = filename.substring(0, pos)
                        + filename.substring(pos + 6);
                    lines = "(deleted)";
                }

                System.out.println("      " + filename + " " + lines);
            }
            System.out.println();
        }
        if (tasksToDisplay.isEmpty()) {
            System.out.println("  (none)");
        }

        System.out.println("\n\nFiles modified:");
        System.out.println("================================");
        Map<String, Set<Integer>> files = new TreeMap<String, Set<Integer>>();
        for (TaskRec task : tasksDone.values()) {
            for (String filename : task.files.keySet()) {
                Set<Integer> tasklist = files.get(filename);

                if (tasklist == null) {
                    tasklist = new TreeSet<Integer>();
                    files.put(filename, tasklist);
                }
                tasklist.add(task.id);
            }
        }

        for (TaskRec task : tasksUndone.values()) {
            for (String filename : task.files.keySet()) {
                Set<Integer> tasklist = files.get(filename);

                if (tasklist == null) {
                    tasklist = new TreeSet<Integer>();
                    files.put(filename, tasklist);
                }
                tasklist.add(task.id);
            }
        }

        for (Map.Entry<String, Set<Integer>> e : files.entrySet()) {
            String filename = e.getKey();
            Set<Integer> tasklist = e.getValue();
            boolean isFirst = true;

            System.out.print("      " + filename + ": ");
            for (Integer taskId : tasklist) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    System.out.print(", ");
                }
                System.out.print("#");
                System.out.print(taskId);
            }
            System.out.println();
        }
    }

    public static Date convertDate(String dateStr) {
        Date date;
        Calendar cal = Calendar.getInstance();
        StringTokenizer t = new StringTokenizer(dateStr, "/ :-");
        int year = Integer.parseInt(t.nextToken());
        int month = Integer.parseInt(t.nextToken()) - 1;
        int day = Integer.parseInt(t.nextToken());
        int hour = Integer.parseInt(t.nextToken());
        int minute = Integer.parseInt(t.nextToken());
        int second = Integer.parseInt(t.nextToken());

        // The times we got from CVS were in GMT, so put the Calendar object
        // into that mode.  Why is it necessary to create a new instance of
        // SimpleTimeZone solely for this purpose?
        cal.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        cal.set(year, month, day, hour, minute, second);
        return cal.getTime();
    }
}

class DummyInputStreamServicer extends Thread {
    private final InputStream is;

    DummyInputStreamServicer(InputStream is) {
        this.is = is;
    }

    public void run() {
        try {
            for (byte[] buffer = new byte[1024]; is.read(buffer) >= 0; ) {}
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }        
}

class FileRec {
    public String filename;
    public String headRevision;

    // Maps release tags to revisions
    public final Map<String, String> tags;

    // Maps revision to RevisionRecs
    public final Map<String, RevisionRec> revisions;

    public FileRec() {
        filename = null;
        headRevision = null;
        tags = new HashMap<String, String>();
        revisions = new HashMap<String, RevisionRec>();
    }
}

class RevisionRec {
    static Pattern taskPattern = 
            Pattern.compile("(^#|^([Tt]ask ?#? ?))([0-9]+)[:,;]? ?(.*)");

    public String revision;
    public String date;
    public String author;
    public String state;
    public String lines;
    public String comment;
    
    public boolean isDead() {
        return state.equals("dead");
    }

    public RawTask[] getTasks() {
        List<RawTask> tasks = new ArrayList<RawTask>();
        RawTask currentTask = null;
        StringTokenizer lineBreaker = new StringTokenizer(comment, "\n");

        while (lineBreaker.hasMoreTokens()) {
            String line = lineBreaker.nextToken();
            Matcher matcher = taskPattern.matcher(line);

            if (matcher.matches()) {
                if (currentTask != null) {
                    tasks.add(currentTask);
                }

                currentTask = new RawTask();
                currentTask.id = Integer.valueOf(matcher.group(3));
                currentTask.comments = matcher.group(4).trim();
            } else {
                // Append this line's comments to the current task, if there
                // is one.
                if (currentTask != null) {
                    currentTask.comments += " " + line;
                }
            }
        }
        if (currentTask != null) {
            tasks.add(currentTask);
        }

        return tasks.toArray(new RawTask[tasks.size()]);
    }

    public boolean isWithin(String revision1, String revision2) {
        if (revision1 == null && revision2 == null) {
            return false;
        }
        if (revision1 == null) {
            return true;
        }
        return (compareRevision(revision1, this.revision) < 0
                && compareRevision(this.revision, revision2) <= 0);
    }

    /** 
     * Returns -1 if revision1 is older than (came before) revision2, 1 if
     * revision2 is older than revision 1, and 0 if the two revisions are
     * identical.
     */
   static int compareRevision(String revision1, String revision2) {
        if (revision1 == revision2) {
            return 0;
        } else if (revision1 == null) {
            return -1;
        } else if (revision2 == null) {
            return 1;
        }
        
        StringTokenizer t1 = new StringTokenizer(revision1, ".");
        StringTokenizer t2 = new StringTokenizer(revision2, ".");

        while (t1.hasMoreTokens() && t2.hasMoreTokens()) {
            int val1 = Integer.parseInt(t1.nextToken());
            int val2 = Integer.parseInt(t2.nextToken());

            if (val1 < val2) {
                return -1;
            } else if (val2 < val1) {
                return 1;
            }
        }

        if (t1.hasMoreTokens()) {
            return 1;
        } else if (t2.hasMoreTokens()) {
            return -1;
        } else {
            return 0;
        }
    }
}

class RawTask {
    public Integer id;
    public String comments;
}

class TaskRec implements Comparable<TaskRec> {
    public Integer id;

    // contains Strings representing the authors' usernames
    public Set<String> authors;

    // contains Strings representing the dates this thing was checked in
    public Set<String> dates;

    // maps Strings representing the filenames that were touched to Strings
    //   representing the number of lines that were altered
    public Map<String, String> files;

    // contains Strings representing the comments on each task
    public Set<String> comments;

    public TaskRec(Integer id) {
        this.id = id;
        authors = new HashSet<String>();
        dates = new HashSet<String>();
        files = new TreeMap<String, String>();
        comments = new HashSet<String>();
    }

    public int compareTo(TaskRec x) {
        String thisFirstDate = this.dates.iterator().next();
        String xFirstDate = x.dates.iterator().next();

        if (thisFirstDate == null && xFirstDate == null)
            return 0;
        if (thisFirstDate == null)
            return 1;
        if (xFirstDate == null)
            return -1;

        return thisFirstDate.compareTo(xFirstDate);
    }
}

