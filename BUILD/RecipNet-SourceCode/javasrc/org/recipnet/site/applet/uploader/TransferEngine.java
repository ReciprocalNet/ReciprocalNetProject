/*
 * Reciprocal Net project
 * 
 * TransferEngine.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 * 11-Nov-2005: midurbin added filename validation
 * 16-May-2006: jobollin changed the media type with which files are transfered
 *              from application/binary (which is not registered) to
 *              application/octet-stream; reformatted the source; switched to
 *              the new RepositoryFileTransfer API
 * 03-Nov-2006: jobollin fixed reversion bug related to the engineThread not
 *              being initialized; performed source cleanup
 * 27-Dec-2007: ekoperda removed extraneous debugging println()'s
 */

package org.recipnet.site.applet.uploader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.event.EventListenerList;

import org.recipnet.site.shared.RepositoryFileTransfer;
import org.recipnet.site.shared.validation.FilenameValidator;

/**
 * An engine used by the {@code Uploader} applet to transfer files to an
 * {@code UploaderSupport} servlet running on a remote web server. This
 * functionality is implemented as its own thread so that the applet's user
 * interface can remain responsive. This class communicates progress information
 * to its registered listeners via their {@code TransferEventListener}
 * interfaces. Listeners are expected to process the messages asynchronously.
 * See the various {@code Transfer...Event} classes for descriptions of the
 * various kinds of notifications supported.
 * <p>
 * This class is thread-safe.
 */
public class TransferEngine implements Runnable {

    /** Validates filenames dropped onto applet. */
    private static final FilenameValidator filenameValidator
            = new FilenameValidator();

    /**
     * A constant that controls the maximum number of bytes transferred to the
     * web server in a single chunk. This value also influences the interval at
     * which progress notifications are sent to registered listeners. Changing
     * this value may affect transfer performance.
     */
    private static final int HTTP_CHUNK_SIZE = 32768;

    /**
     * A number that identifies the particular upload session. This is specified
     * by the caller at construction time and passed opaquely to the constructor
     * of {@code RepositoryFileTransfer}.
     */
    private final int persistedOperationId;

    /**
     * The URL of the remote {@code UploaderSupport} servlet, as specified by
     * the caller at construction time.
     */
    private final URL uploaderSupportUrl;

    /**
     * The collection of files awaiting transfer whose transfer has not yet
     * begun. All {@code BlockingQueue} implementations are required to be
     * thread-safe, so no extra synchronization is needed.
     */
    private final BlockingQueue<File> filesToTransfer;

    /**
     * Used to signal to the thread that it should terminate. Initialized to
     * false by the constructor and set to true upon a call to
     * {@code terminateThread()}.
     */
    private final AtomicBoolean threadTerminationSignal;

    /**
     * Used to signal to the thread that the current batch of files being
     * transferred and queued for transfer should be cleared. Initialized to
     * false by the constructor and set to true upon a call to
     * {@code cancelQueuedFiles()}.
     */
    private final AtomicBoolean cancelFileSignal;

    /**
     * Maintains a list of all listeners that have registered for notifications
     * from this class.
     */
    private final EventListenerList listeners;

    /**
     * A reference to the {@code Thread} object that presently is executing this
     * class's {@code run()} method. This is set by {@code run()}.
     */
    private final AtomicReference<Thread> engineThread;

    /** Constructor. */
    public TransferEngine(int persistedOperationId, URL uploaderSupportUrl) {
        this.persistedOperationId = persistedOperationId;
        this.uploaderSupportUrl = uploaderSupportUrl;
        filesToTransfer = new LinkedBlockingQueue<File>();
        threadTerminationSignal = new AtomicBoolean(false);
        cancelFileSignal = new AtomicBoolean(false);
        listeners = new EventListenerList();
        engineThread = new AtomicReference<Thread>();
    }

    /**
     * Registers a {@code TransferEventListener} to receive event notifications
     * from this class.
     */
    public void addTransferEventListener(TransferEventListener listener) {
        listeners.add(TransferEventListener.class, listener);
    }

    /**
     * Unregisters a previously-registered {@code TransferEventListner} that had
     * been receiving event notifications from this class.
     */
    public void removeTransferEventListener(TransferEventListener listener) {
        listeners.remove(TransferEventListener.class, listener);
    }

    /**
     * Causes the specified file to be transferred to the server. The file is
     * enqueued and the method returns quickly. Later, the worker thread
     * performs the actual transfer and reports the results to the registered
     * listeners.
     */
    public void queueFileTransfer(File file) {
        filesToTransfer.add(file);
    }

    /**
     * Signals to the worker thread that it should terminate. Any transfers
     * presently in progress or queued are cancelled. This method returns
     * quickly, possibly before the worker thread has finished shutting down.
     */
    public void terminateThread() {
        threadTerminationSignal.set(true);
        cancelFileSignal.set(true);
        engineThread.get().interrupt();
    }

    /**
     * Signals to the worker thread that the file presently being transferred,
     * and any files queued for transfer, should be cancelled. This method
     * returns quickly, possibly while the worker thread is still transferring.
     * The worker thread later will confirm the cancellations by sending events
     * to the registered listeners.
     */
    public void cancelQueuedFiles() {

        /*
         * Clear the queue of any pending files. It is not possible for us to
         * invoke filesToTransfer.clear() because that method is not
         * thread-safe. That behavior is hinted at by the JavaDoc for the
         * BlockingQueue interface but was also discovered in practice. Thus, we
         * use a bunch of poll()s instead.
         */
        while (filesToTransfer.poll() != null) {
            // do nothing
        }

        cancelFileSignal.set(true);
    }

    /** The worker thread implementation. */
    public void run() {
        Collection<String> namesFilesTransferred = new ArrayList<String>();
        engineThread.set(Thread.currentThread());

        while (!threadTerminationSignal.get()) {
            File file = filesToTransfer.poll();

            if (file == null) {
                /*
                 * Notify listeners that we are about to become idle as we wait
                 * on the queue.
                 */
                fireTransferThreadIdleEvent(
                        new TransferThreadIdleEvent(this));

                try {
                    /*
                     * Pop the next file from the queue. This call may block for
                     * a long time if the queue is empty.
                     */
                    file = filesToTransfer.take();
                } catch (InterruptedException ex) {
                    /*
                     * We might have been sent a termination signal. Loop again
                     * and let the while() condition check for that.
                     */
                    continue;
                }
            }

            if (cancelFileSignal.compareAndSet(true, false)) {
                /*
                 * We were instructed to cancel the transfer of this file. Good
                 * thing we hadn't really started yet.
                 */
                continue;
            }
            if (namesFilesTransferred.contains(file.getName())) {

                /*
                 * This file we were asked to transfer has the same name as a
                 * file we've already transferred. We can't transfer the file a
                 * second time. Notify the listeners.
                 */
                fireTransferFailedEvent(new TransferFailedEvent(this,
                        TransferFailedEvent.Reason.DUPLICATE_FILENAME,
                        file.getName(), 0, null, null, null));
                continue;
            }
            if (!TransferEngine.filenameValidator.isValid(file.getName())) {
                /*
                 * This file we were asked to transfer has an invalid name. We
                 * shouldn't transfer such a file because core will throw an
                 * exception. Notify the listeners.
                 */
                fireTransferFailedEvent(new TransferFailedEvent(this,
                        TransferFailedEvent.Reason.INVALID_FILENAME,
                        file.getName(), 0, null, null, null));
                continue;
            }

            /*
             * If control reaches here then we have been instructed to transfer
             * a file. Notify the listeners.
             */
            fireTransferProgressEvent(new TransferProgressEvent(this,
                    file.getName(), 0, file.length(), false));

            try {

                /*
                 * Initiate an HTTP connection to the server. It is conceivable
                 * that we could use HttpURLConnection's fixed-length streaming
                 * mode because we know the name of the file we're uploading in
                 * advance. However, doing so would subject uploaded files to a
                 * 2-gigabyte size limit. We use chunked streaming mode instead
                 * in order to avoid imposing such a limit on uploaded files.
                 */
                HttpURLConnection httpConnection = (HttpURLConnection)
                        uploaderSupportUrl.openConnection();

                httpConnection.setRequestMethod("POST");
                httpConnection.setRequestProperty("Content-Type",
                        "application/octet-stream");
                httpConnection.setChunkedStreamingMode(TransferEngine.HTTP_CHUNK_SIZE);
                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);
                httpConnection.connect();

                OutputStream httpStream = httpConnection.getOutputStream();
                boolean transferSucceeded;

                try {
                    /*
                     * Transfer the file. This will cause our anonymous callback
                     * class (below) to be invoked repeatedly during the
                     * transfer.
                     */
                    RepositoryFileTransfer transfer
                            = RepositoryFileTransfer.forUpload(
                                        persistedOperationId, file);

                    transferSucceeded
                            = transfer.doTransfer(httpStream, new Regulator());

                    // Close our HTTP connection.
                    httpStream.flush();
                } finally {
                    httpStream.close();
                }
                if (!transferSucceeded) {
                    /*
                     * Somebody set the cancel signal while our file transfer
                     * was in progress. Notify the listeners.
                     */
                    fireTransferFailedEvent(new TransferFailedEvent(this,
                            TransferFailedEvent.Reason.USER_CANCELLED,
                            file.getName(), 0, null, null, null));
                } else if (httpConnection.getResponseCode()
                        == HttpURLConnection.HTTP_NO_CONTENT) {
                    // The server accepted our files. Notify the listeners.
                    fireTransferProgressEvent(
                            new TransferProgressEvent(this, file.getName(),
                                    file.length(), file.length(), true));
                    httpConnection.getInputStream().close();
                    namesFilesTransferred.add(file.getName());
                } else {
                    /*
                     * The server rejected our files for some reason. Read the
                     * server's error data for possible usefulness during
                     * debugging. Notify the listeners.
                     */
                    String errorDataStr = null;
                    InputStream is = httpConnection.getErrorStream();
                    if (is != null) {
                        String charset = httpConnection.getContentEncoding();
                        InputStreamReader r = new InputStreamReader(is,
                                (charset == null) ? "ISO-8859-1" : charset);
                        StringBuilder sb = new StringBuilder();
                        char b[] = new char[1024];
                        int charsRead;
                        do {
                            charsRead = r.read(b);
                            if (charsRead > 0) {
                                sb.append(b, 0, charsRead);
                            }
                        } while (charsRead >= 0);
                        errorDataStr = sb.toString();
                    }
                    fireTransferFailedEvent(new TransferFailedEvent(this,
                            TransferFailedEvent.Reason.SERVER_ERROR,
                            file.getName(), httpConnection.getResponseCode(),
                            httpConnection.getResponseMessage(), errorDataStr,
                            null));

                    // Clear the queue of any pending files.
                    while (filesToTransfer.poll() != null) {
                        // do nothing
                    }
                }
            } catch (IOException ex) {
                fireTransferFailedEvent(new TransferFailedEvent(this,
                        TransferFailedEvent.Reason.JAVA_EXCEPTION,
                        file.getName(), 0, null, null, ex));

                // Clear the queue of any pending files.
                while (filesToTransfer.poll() != null) {
                    // do nothing
                }
            }
        }
    }

    /**
     * Internal helper method that just sprays a single event to all of the
     * registered listeners, however many there may be.
     */
    private void fireTransferProgressEvent(TransferProgressEvent event) {
        for (TransferEventListener listener
                : listeners.getListeners(TransferEventListener.class)) {
            listener.receiveTransferProgressEvent(event);
        }
    }

    /**
     * Internal helper method that just sprays a single event to all of the
     * registered listeners, however many there may be.
     */
    private void fireTransferFailedEvent(TransferFailedEvent event) {
        for (TransferEventListener listener
                : listeners.getListeners(TransferEventListener.class)) {
            listener.receiveTransferFailedEvent(event);
        }
    }

    /**
     * Internal helper method that just sprays a single event to all of the
     * registered listeners, however many there may be.
     */
    private void fireTransferThreadIdleEvent(TransferThreadIdleEvent event) {
        for (TransferEventListener listener
                : listeners.getListeners(TransferEventListener.class)) {
            listener.receiveTransferThreadIdleEvent(event);
        }
    }

    /**
     * An inner class that receives callbacks from the
     * {@code RepositoryFileTransfer} class as transfers are in-progress.
     */
    private class Regulator implements RepositoryFileTransfer.Regulator {
        public boolean shouldRepositoryFileTransferContinue(String fileName,
                long bytesTransferred, long bytesTotal) {
            fireTransferProgressEvent(
                    new TransferProgressEvent(this, fileName, bytesTransferred,
                            bytesTotal, false));
            return !cancelFileSignal.compareAndSet(true,
                    false);
        }
    }
}
