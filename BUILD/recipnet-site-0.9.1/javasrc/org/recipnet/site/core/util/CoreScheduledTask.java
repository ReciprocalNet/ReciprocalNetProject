/*
 * Reciprocal Net project
 * 
 * CoreScheduledTask.java
 *
 * 27-Aug-2002: ekoperda wrote first draft
 * 26-Sep-2002: ekoperda moved class to the core.util package; used to be in
 *              the core package
 * 26-May-2006: jobollin reformatted the source and implemented generics
 */

package org.recipnet.site.core.util;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A subclass of {@code java.util.TimerTask} used to schedule periodic tasks in
 * the core modules. Pass one of these objects to
 * {@code SiteManager.schedulePeriodicTask(CoreScheduledTask)} in order to
 * activate it.
 */
public class CoreScheduledTask extends TimerTask {
    // These flags may be used in the characteristicFlags parameter
    public static final int QUICK_TASK = 0;

    public static final int PROCESSOR_INTENSIVE = 1;

    public static final int DATABASE_INTENSIVE = 2;

    public static final int REPOSITORY_INTENSIVE = 4;

    public static final int NETWORK_INTENSIVE = 8;

    private long approxInterval;

    private long maxDuration;

    private int characteristicFlags;

    private Runnable task;

    private Timer timer;

    private Set<CoreScheduledTask> currentTasks;

    private long startTime;

    /**
     * Constructor for use when defining a core task that will run periodically.
     * 
     * @param approxInterval the approximate number of milliseconds that should
     *        elapse between successive task executions
     * @param maxDuration the maximum number of milliseconds this task may take
     *        to execute. After this time has passed, the task will be assumed
     *        to have stalled/deadlocked and may be preempted.
     * @param characteristicFlags a combination of bit flags that allows the
     *        scheduler to make intelligent determinations about which tasks can
     *        run concurrently with other ones without penalty.
     * @param task a Runnable object whose run() method contains the code that
     *        should be executed periodically.
     */
    public CoreScheduledTask(long approxInterval, long maxDuration,
            int characteristicFlags, Runnable task) {
        this.approxInterval = approxInterval;
        this.maxDuration = maxDuration;
        this.characteristicFlags = characteristicFlags;
        this.task = task;

        this.timer = null;
        this.currentTasks = null;
        this.startTime = 0;
    }

    /**
     * Similar to the first constructor, except this one takes its
     * approxInterval, maxDuration, and characteristicFlags from the string.
     * String format is approxInterval,maxDuration,characteristicFlags
     */
    public CoreScheduledTask(String params, Runnable task) {
        this(0, 0, 0, task);
        try {
            StringTokenizer t = new StringTokenizer(params, ",");
            
            approxInterval = Long.parseLong(t.nextToken());
            maxDuration = Long.parseLong(t.nextToken());
            characteristicFlags = Integer.parseInt(t.nextToken());
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException();
        }
    }

    /** Copy constructor */
    public CoreScheduledTask(CoreScheduledTask cst) {
        this(cst.approxInterval, cst.maxDuration, cst.characteristicFlags,
                cst.task);
        setScheduleInfo(cst.timer, cst.currentTasks);
    }

    /**
     * Overrides TimerTask.run() . Does some basic clearance checking to
     * determine if this task is clear to run. If/when it is, invokes run() on
     * the task that was specified at creation time.
     */
    @Override
    public void run() {
        boolean clearToRun = true;

        /*
         * Read the set of current tasks to determine if our execution would
         * conflict with any of them. Ignore "current" tasks that have run
         * longer than their stated maximum duration. If no conflicts exist, add
         * ourselves to the list of current tasks.
         */
        synchronized (currentTasks) {
            long now = System.currentTimeMillis();

            for (CoreScheduledTask cst : currentTasks) {
                if (cst.conflictsWith(this)
                        && ((cst.startTime + cst.maxDuration) < now)) {
                    clearToRun = false;
                }
            }

            if (clearToRun) {
                currentTasks.add(this);
                startTime = now;
            }
        }

        if (clearToRun) {
            // This task is clear to run. Do the caller-specified task.
            task.run();

            /*
             * Update the set of current tasks to indicate that we're no longer
             * running.
             */
            synchronized (currentTasks) {
                currentTasks.remove(this);
            }
            this.startTime = 0;

            /*
             * Schedule the next execution of this task (including a random
             * factor). We must give the timer object a copy of ourself because
             * it would throw an IllegalStateException if we attempted to give
             * it ourself.
             */
            timer.schedule(new CoreScheduledTask(this), getApproxInterval());
        } else {
            /*
             * Another currently running task would conflict with this one if we
             * were to start. Reschedule this task for a one-time execution in
             * the near future.
             */
            timer.schedule(this, getInitialDelay());
        }
    }

    /**
     * Called by CoreScheduler at schedule time; this object will need this
     * reference later when the task is triggered to run.
     */
    public void setScheduleInfo(Timer timer, Set<CoreScheduledTask> currentTasks) {
        this.timer = timer;
        this.currentTasks = currentTasks;
    }

    /**
     * Returns the periodic interval this task was configured with, plus or
     * minus a 10% random offset.
     */
    public long getApproxInterval() {
        return approxInterval
                + (long) ((Math.random() - 0.5) * approxInterval * 0.2);
    }

    /**
     * Returns the approximate number of milliseconds that should elapse before
     * the first execution of this task on a new server instance. This value is
     * flexible and relatively unimportant. Includes a +- 10% random offset.
     */
    public long getInitialDelay() {
        return approxInterval / 10
                + (long) ((Math.random() - 0.5) * approxInterval * 0.02);
    }

    /**
     * Returns true if the current task would conflict with another periodic
     * task. Current implementation says that any task conflicts with any other
     * task, and thus only one periodic task can run at once.
     */
    public boolean conflictsWith(CoreScheduledTask cst) {
        /*
         * TODO: change this implementation so that each task's
         * characteristicFlags are considered.
         */
        return true;
    }
}
