/*
 * Reciprocal Net project
 * 
 * CoreProcessWrapper.java
 *
 * 23-May-2003: ekoperda wrote first draft
 * 10-Jul-2003: midurbin added getOutput()
 * 28-Jul-2003: nsanghvi added second version of constructor and exec()
 * 07-Jan-2004: ekoperda changed package references due to source tree
 *              reorganization
 * 26-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import org.recipnet.common.ProcessWrapper;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ProcessAbnormalExitException;
import org.recipnet.site.core.ProcessIncompleteException;

/**
 * Utility class used by core modules to invoke external processes. Though it
 * does not descend from {@code Process} for technical reasons, this class's
 * arrangement of methods was inspired by {@code Process}. It builds on the
 * functionality of {@code ProcessWrapper} by adding core-specific exception
 * handling and logging.
 */
public class CoreProcessWrapper {
    
    /** Represents the external spawned process */
    private final Process proc;

    /** Arguments with which the process was invoked originally */
    private final String args[];

    /** Stores text the process sent to its output stream */
    private final ByteArrayOutputStream inputStream;

    /** Stores text the process sent to its error stream */
    private final ByteArrayOutputStream errorStream;

    /**
     * A timestamp recorded when this {@code CoreProcessWrapper} notices that
     * its external process has terminated 
     */
    private long timeEnded;

    /**
     * A timestamp recorded when this {@code CoreProcessWrapper} is initialized
     */
    private final long timeBegan;

    /**
     * Constructor; invokes the specified external process and initializes it
     * for logging.
     * 
     * @param args arguments that should be used to invoke the external process
     *        on the system's command line, as would be passed to
     *        {@code Runtime.exec()}.
     * @throws OperationFailedException if the process could not be invoked for
     *         some reason.
     */
    public CoreProcessWrapper(String args[]) throws OperationFailedException {
        this.args = args;
        this.inputStream = new ByteArrayOutputStream();
        this.errorStream = new ByteArrayOutputStream();
        this.timeBegan = System.currentTimeMillis();
        this.timeEnded = 0;

        try {
            this.proc = new ProcessWrapper(Runtime.getRuntime().exec(args),
                    true, true, this.inputStream, true, this.errorStream);
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Constructor; invokes the specified external process and initializes it
     * for logging.
     * 
     * @param commandLine that should be used to invoke the external process on
     *        the system's command line, as would be passed to
     *        {@code Runtime.exec()}.
     * @throws OperationFailedException if the process could not be invoked for
     *         some reason.
     */
    public CoreProcessWrapper(String commandLine)
            throws OperationFailedException {
        this.args = new String[] { commandLine };
        this.inputStream = new ByteArrayOutputStream();
        this.errorStream = new ByteArrayOutputStream();
        this.timeBegan = System.currentTimeMillis();
        this.timeEnded = 0;

        try {
            this.proc = new ProcessWrapper(Runtime.getRuntime().exec(
                    commandLine), true, true, this.inputStream, true,
                    this.errorStream);
        } catch (IOException ex) {
            throw new OperationFailedException(ex);
        }
    }

    /**
     * Like {@code Process.waitFor()}, waits for the external process to
     * terminate and returns its exit code.
     * 
     * @return the external process's exit code.
     * @throws ProcessIncompleteException if the calling thread was interrupted
     *         while waiting for the external process to terminate.
     */
    public int waitFor() throws ProcessIncompleteException {
        try {
            int x = proc.waitFor();
            
            if (this.timeEnded == 0) {
                this.timeEnded = System.currentTimeMillis();
            }
            
            return x;
        } catch (InterruptedException ex) {
            throw new ProcessIncompleteException(this.proc, ex);
        }
    }

    /**
     * Like {@code waitFor()} above, except this version examines the external
     * process's exit code and optionally throws an exception if it's nonzero.
     * (By convention, a nonzero exit code typically indicates an error of some
     * sort.)
     * 
     * @return the external process's exit code.
     * @param throwExceptionOnNonzeroExit if true and the external process
     *        terminates with a nonzero exit code, an exception is thrown. If
     *        false, no exception is thrown and the function returns normally.
     * @throws ProcessAbnormalExitException if
     *         {@code throwExceptionOnNonzeroExit} is true and the external
     *         process terminates with an exit code other than 0. The exception
     *         object thrown contains the raw {@code Process} object and the
     *         process's captured output text and error text.
     * @throws ProcessIncompleteException if the calling thread was interrupted
     *         while waiting for the external process to terminate.
     */
    public int waitFor(boolean throwExceptionOnNonzeroExit)
            throws ProcessIncompleteException, ProcessAbnormalExitException {
        int exitCode = waitFor();
        
        if (throwExceptionOnNonzeroExit && (exitCode != 0)) {
            throw new ProcessAbnormalExitException(this.proc,
                    this.inputStream.toString(), this.errorStream.toString());
        }
        
        return exitCode;
    }

    /**
     * After the external process has terminated, returns a {@code LogRecord}
     * object that describes its life. Typically the log message would include
     * the command/arguments by which the process was invoked, its exit code,
     * its running time, its captured output text, and its captured error text.
     * If the process has not yet terminated then this function blocks until it
     * has. Any captured text included in the log record is interpreted as
     * though it were encoded using the platform's default character encoding.
     * 
     * @return a {@code LogRecord} object suitable to be written to a log that
     *         describes the life of the external process.
     * @throws ProcessIncompleteException if the calling thread was interrupted
     *         while waiting for the external process to terminate (and the
     *         external process had not terminated already).
     */
    public LogRecord getLogRecord() throws ProcessIncompleteException {
        // Ensure that the process has terminated already.
        int exitCode = waitFor();

        return LogRecordGenerator.processTermination(this.args, exitCode,
                this.timeEnded - this.timeBegan, this.inputStream.toString(),
                this.errorStream.toString());
    }

    /**
     * After the external process has terminated, returns the output from the
     * process. If the process has not yet terminated then this function blocks
     * until it has.
     * 
     * @return a {@code String} containing all that was written to the standard
     *         output.
     */
    public String getOutput() throws ProcessIncompleteException {
        waitFor();
        
        return this.inputStream.toString();
    }

    /**
     * Static convenience function that spawns an external process, waits for it
     * to finish, and returns the text output by that process.
     * 
     * @return a String containing all text captured from the process's output
     *         stream during it's lifetime. If no text was captured then the
     *         string is empty.
     * @param args arguments that should be used to invoke the external process
     *        on the system's command line, as would be passed to
     *        {@code Runtime.exec()}.
     * @throws OperationFailedException if the process could not be invoked for
     *         some reason.
     * @throws ProcessIncompleteException if the calling thread was interrupted
     *         while waiting for the external process to terminate.
     */
    public static String exec(String args[],
            boolean throwExceptionOnNonzeroExit, Logger logger)
            throws OperationFailedException, ProcessIncompleteException {
        CoreProcessWrapper proc = new CoreProcessWrapper(args);
        
        if (logger != null) {
            logger.log(proc.getLogRecord());
        }
        proc.waitFor(throwExceptionOnNonzeroExit);
        
        return proc.inputStream.toString();
    }

    /**
     * Like {@code exec()} above except that this method takes as an input a
     * String representing the external process to be run.
     * 
     * @return a String containing all text captured from the process's output
     *         stream during it's lifetime. If no text was captured then the
     *         string is empty.
     * @param commandLine that should be used to invoke the external process on
     *        the system's command line, as would be passed to
     *        {@code Runtime.exec()}.
     * @throws OperationFailedException if the process could not be invoked for
     *         some reason.
     * @throws ProcessIncompleteException if the calling thread was interrupted
     *         while waiting for the external process to terminate.
     */
    public static String exec(String commandLine,
            boolean throwExceptionOnNonzeroExit, Logger logger)
            throws OperationFailedException, ProcessIncompleteException {
        CoreProcessWrapper proc = new CoreProcessWrapper(commandLine);
        
        if (logger != null) {
            logger.log(proc.getLogRecord());
        }
        proc.waitFor(throwExceptionOnNonzeroExit);
        
        return proc.inputStream.toString();
    }
}
