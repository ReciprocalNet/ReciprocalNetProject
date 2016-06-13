/*
 * Reciprocal Net project
 * 
 * RepositoryFileOutputStream.java
 *
 * 18-Jun-2004: ekoperda wrote first draft
 * 11-Feb-2005: ekoperda added closeInUnison() and copyFromStream(), modified 
 *              close() to support unison closures, and made
 *              newIOExceptionWithCause() static
 * 15-Jun-2005: ekoperda fixed closed-detection bug in flush() and added 
 *              toString()
 * 17-May-2006: jobollin reformatted the source; removed unused imports
 * 23-May-2006: jobollin added touchStreams(); updated docs
 */

package org.recipnet.site.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;
import org.recipnet.site.core.RepositoryManager;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * An {@code OutputStream} that directs data to a file in the repository by
 * invoking methods on the Repository Manager over RMI. The user is responsible
 * for opening his desired file by invoking
 * {@code RepositoryManager.beginWritingDataFile()} before initializing an
 * instance of this class. For efficiency, write operations on this stream are
 * buffered in memory and sent to the repository in large chunks (so little is
 * gained by buffering the data externally). Users should invoke exactly one of
 * {@link #close()}, {@link #abort()}, or
 * {@link #closeInUnison(Collection)} before abandoning an instance. This class
 * is NOT thread-safe.
 */
public class RepositoryFileOutputStream extends OutputStream {

    /**
     * The default buffer size; used by instances initialized via the binary
     * constructor
     */
    /*
     * The specific choice of buffer size (128 KB) is a result of server
     * profiling done by midurbin in September 2003.  He determined
     * that best performance across RMI to core was achieved with a buffer
     * size of around 128 KB.
     */
    private final static int DEFAULT_BUFFER_SIZE = 131072;
    
    /**
     * A reference to the webapp's {@code CoreConnector}, set by the
     * constructor.
     */
    private CoreConnector cc;

    /**
     * The active ticket number within {@code RepositoryManager}, set by the
     * constructor.
     */
    private int ticketId;

    /**
     * A fixed buffer of data that has accumulated from calls to {@code write()}
     * but has not yet been {@code flush()}ed to core. The buffer does not
     * revolve; it always begins at index 0 for efficiency.
     */
    private byte[] buffer;

    /**
     * Index into {@code buffer} that identifies the first byte to be
     * overwritten in the next invocation of {@code write()}. Because
     * {@code buffer} is 0-based, this value is also the number of data bytes
     * presently stored in the buffer. Incremented by the two {@code write()}'s
     * and cleared by {@code flush()}.
     */
    private int nextIndexInBufferToWrite;

    /**
     * Flag that is set by {@code close()} and {@code abort()} once one of them
     * has been invoked; checked by other methods to guarantee state-safety.
     */
    private boolean isClosed;

    /**
     * Initializes a new {@code RepositoryFileOutputStream} with the specified
     * {@code CoreConnector} and repository ticket ID, and using the default
     * buffer size
     * 
     * @param cc the {@code CoreConnector} to use for communicating with the
     *        core components
     * @param ticketId the repository ticket ID representing the file to which
     *        this stream's bytes are directed; typically obtained via a
     *        previous invocation of
     *        {@link RepositoryManager#beginWritingDataFile(SampleInfo, String,
     *        boolean, boolean, int, int, String, String)
     *        RepositoryManager.beginWritingDataFile()}
     */
    public RepositoryFileOutputStream(CoreConnector cc, int ticketId) {
        this(cc, ticketId, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Initializes a new {@code RepositoryFileOutputStream} with the specified
     * {@code CoreConnector}, repository ticket ID, and buffer size
     * 
     * @param cc the {@code CoreConnector} to use for communicating with the
     *        core components
     * @param ticketId the repository ticket ID representing the file to which
     *        this stream's bytes are directed; typically obtained via a
     *        previous invocation of
     *        {@link RepositoryManager#beginWritingDataFile(SampleInfo, String,
     *        boolean, boolean, int, int, String, String)
     *        RepositoryManager.beginWritingDataFile()}
     * @param bufferSize the size of the internal buffer in which this stream
     *        will accumulate bytes before sending them to the Repository
     *        Manager in a single block
     */
    public RepositoryFileOutputStream(CoreConnector cc, int ticketId,
            int bufferSize) {
        this.cc = cc;
        this.ticketId = ticketId;
        this.buffer = new byte[bufferSize];
        this.nextIndexInBufferToWrite = 0;
        this.isClosed = false;
    }

    /**
     * Overrides {@code OutputStream} but behaves according to that
     * specification.
     * 
     * @param b the byte to be written; only the lowest 8 bits are significant.
     * @throws IOException with:
     *         <ul>
     *         <li>a nested {@code IllegalStateException} if {@code close()} or
     *         {@code abort()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,</li>
     *         <li>a nested {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to write,</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         {@code CoreConnector} has been notified of the error already,
     *         or</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined that the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     */
    @Override
    public void write(int b) throws IOException {
        if (this.isClosed) {
            // This stream has already been closed.
            throw newIOExceptionWithCause(
                    new IllegalStateException("Stream is not open"));
        }
        if (isBufferFull()) {
            // Our buffer is full; flush it before proceeding.
            flush();
        }

        // Store b in our buffer.
        byte bAsByte = (byte) (b & 0xFF);
        this.buffer[this.nextIndexInBufferToWrite++] = bAsByte;
    }

    /**
     * Writes bytes from the specified array to this output stream
     * 
     * @param b a byte[] containing the data to write
     * @param off the index into {@code b} of the first byte to write
     * @param len the number of bytes to write.
     * @throws IndexOutOfBoundsException iff {@code off} is negative or
     *         {@code len} is negative or {@code off+len} is greater than the
     *         length of the {@code b}.
     * @throws IOException with:
     *         <ul>
     *         <li>a nested {@code IllegalStateException} if {@code close()} or
     *         {@code abort()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,</li>
     *         <li>a nested {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to write,</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         {@code CoreConnector} has been notified of the error already,
     *         or</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined that the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     * @throws NullPointerException if {@code b} is {@code null}.
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        }
        if ((off < 0) || (len < 0) || (off + len > b.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (this.isClosed) {
            // This stream has already been closed.
            throw newIOExceptionWithCause(new IllegalStateException());
        }

        // Copy data from the caller's buffer until there's no more.
        int countBytesCopied = 0;
        while (countBytesCopied < len) {
            // Flush our own buffer by writing to core if necessary.
            if (isBufferFull()) {
                flush();
            }

            // Copy some bytes from the caller's buffer to our own.
            int c = Math.min(len - countBytesCopied,
                    countSpaceForWrittenBytes());
            System.arraycopy(b, off + countBytesCopied, this.buffer,
                    this.nextIndexInBufferToWrite, c);
            this.nextIndexInBufferToWrite += c;
            countBytesCopied += c;
        }
    }

    /**
     * Overrides {@code OutputStream} but behaves according to that
     * specification.
     * 
     * @throws IOException with:
     *         <ul>
     *         <li>a nested {@code IllegalStateException} if {@code close()} or
     *         {@code abort()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,</li>
     *         <li>a nested {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to write,</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         {@code CoreConnector} has been notified of the error already,
     *         or</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined that the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     */
    @Override
    public void flush() throws IOException {
        if (this.isClosed) {
            // This stream has already been closed.
            throw newIOExceptionWithCause(new IllegalStateException());
        }

        // Decide how much data to send.
        byte[] bufferToSend;

        if (isBufferFull()) {
            // Just send our whole buffer directly for efficiency.
            bufferToSend = this.buffer;
        } else {
            // Can't send our own buffer because it's the wrong size; create a
            // new one that's the proper size.
            bufferToSend = new byte[this.nextIndexInBufferToWrite];
            System.arraycopy(this.buffer, 0, bufferToSend, 0,
                    this.nextIndexInBufferToWrite);
        }

        // Send the data to core.
        try {
            this.cc.getRepositoryManager().writeToDataFile(this.ticketId,
                    bufferToSend);
        } catch (OperationFailedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (OperationNotPermittedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (RemoteException ex) {
            this.cc.reportRemoteException(ex);
            throw newIOExceptionWithCause(ex);
        }

        // Update our state.
        this.nextIndexInBufferToWrite = 0;
    }

    /**
     * Overrides {@code OutputStream} but behaves according to that
     * specification. Either this method or {@code abort()} or
     * {@code closeInUnison()}> should be invoked by the caller to terminate
     * his involvement with this stream object.
     * 
     * @throws IOException with:
     *         <ul>
     *         <li>a nested {@code IllegalStateException} if {@code close()} or
     *         {@code abort()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         {@code CoreConnector} has been notified of the error already,
     *         or</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined that the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     */
    @Override
    public void close() throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }
        try {
            flush();
            this.cc.getRepositoryManager().closeDataFile(this.ticketId);
            this.isClosed = true;
        } catch (OperationFailedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (RemoteException ex) {
            this.cc.reportRemoteException(ex);
            throw newIOExceptionWithCause(ex);
        }
    }

    /**
     * Similar in concept to {@code close()}, but this method serves a
     * Reciprocal Net-specific purpose. Any core-side temporary resources that
     * might have been allocated to the file-writing operation are cleared, and
     * any data that might have been written to core as a result of calls to
     * {@code write()} are discarded. Either this method or {@code close()} or
     * {@code closeInUnison()} should be invoked by the caller to terminate his
     * involvement with this stream object.
     * 
     * @throws IOException with:
     *         <ul>
     *         <li>a nested {@code IllegalStateException} if {@code close()} or
     *         {@code abort()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         {@code CoreConnector} has been notified of the error already,
     *         or</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined that the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     */
    public void abort() throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }
        try {
            this.cc.getRepositoryManager().abortDataFile(this.ticketId);
            this.isClosed = true;
        } catch (OperationFailedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (RemoteException ex) {
            this.cc.reportRemoteException(ex);
            throw newIOExceptionWithCause(ex);
        }
    }

    /**
     * Copies data from a supplied input stream to this output stream until the
     * end of the input is reached.
     * 
     * @param in an {@code InputStream} from which to copy data
     * @throws IOException if an I/O error occurs
     */
    public void copyFromStream(InputStream in) throws IOException {
        for (;;) {

            // Flush our own buffer by writing to core if necessary.
            if (isBufferFull()) {
                flush();
            }

            // Copy some bytes from the caller's strean to our buffer.
            int c = in.read(this.buffer, this.nextIndexInBufferToWrite,
                    countSpaceForWrittenBytes());

            if (c >= 0) {
                this.nextIndexInBufferToWrite += c;
            } else {
                break;
            }
        }
    }

    /**
     * Provides a {@code String} representation of this stream; this is meant
     * for debugging purposes only
     * 
     * @return a {@code String} representation of this stream
     */
    @Override
    public String toString() {
        return getClass().getName() + "(ticket id " + this.ticketId + ")";
    }

    /**
     * Determines whether the internal is full
     * 
     * @return {@code true} if the buffer is full (in which case it should be
     *         flushed to the Repository Manager), or {@code false} if not
     */
    private boolean isBufferFull() {
        return this.buffer.length <= this.nextIndexInBufferToWrite;
    }

    /**
     * Returns the number of bytes that may be written to the internal
     * {@code buffer} before it fills
     * 
     * @return the number of bytes of available space in the internal buffer
     */
    private int countSpaceForWrittenBytes() {
        return this.buffer.length - this.nextIndexInBufferToWrite;
    }

    /**
     * <p>
     * Instructs the Repository Manager to close one or more
     * tickets bound to {@code RepositoryFileOutputStream} objects in unison.
     * This has the effect of grouping several file writes into a single
     * workflow action.
     * </p><p>
     * Upon successful return, all the specified
     * {@code RepositoryFileOutputStream}s will have been closed, therefore
     * they should not be closed again (via their {@code close()} methods, for
     * instance).  If an exception is thrown then the closure statuses of the
     * individual streams are not defined.
     * </p>
     * 
     * @param rfosCollection a {@code Collection} of zero or more
     *        {@code RepositoryFileOutputStream} objects that this method will
     *        close.
     * @throws IOException on error, with a nested:
     *         <ul>
     *         <li>{@code IllegalStateException} if any caller-specified output
     *         streams have already been closed;</li>
     *         <li>{@code OperationFailedException} on a low-level error from
     *         core. Note that in this case all
     *         {@code RepositoryFileOutputStream}s within
     *         {@code rfosCollection} will indicate that they have been closed,
     *         but the status of the underlying tickets (within
     *         {@code RepositoryManager}) is undefined: any of the underlying
     *         tickets may be open, or closed, or somewhere in between.</li>
     *         <li>{@code OperationNotPermittedException} if core determines
     *         that any of the tickets do not support unison closures, or if the
     *         specified set of tickets cannot be closed as a group;</li>
     *         <li>{@code RemoteException} if an RMI error occurs;</li>
     *         <li>{@code ResourceNotFoundException} if core determines that
     *         one of the tickets has expired or otherwise does not exist.</li>
     *         </ul>
     * @see RepositoryManager#closeDataFiles(int[])
     */
    public static void closeInUnison(
            Collection<RepositoryFileOutputStream> rfosCollection)
            throws IOException {
        if (rfosCollection.isEmpty()) {
            // Exit early; nothing to do.
            return;
        }

        int[] ticketIds = new int[rfosCollection.size()];
        int i = 0;
        CoreConnector cc = null;

        for (RepositoryFileOutputStream rfos : rfosCollection) {
            if (rfos.isClosed) {
                throw newIOExceptionWithCause(new IllegalStateException());
            }
            ticketIds[i++] = rfos.ticketId;
            rfos.flush();
            rfos.isClosed = true;
            cc = rfos.cc;
        }

        if (cc != null) {
            try {
                cc.getRepositoryManager().closeDataFiles(ticketIds);
            } catch (ResourceNotFoundException ex) {
                throw newIOExceptionWithCause(ex);
            } catch (OperationFailedException ex) {
                throw newIOExceptionWithCause(ex);
            } catch (OperationNotPermittedException ex) {
                throw newIOExceptionWithCause(ex);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw newIOExceptionWithCause(ex);
            }
        }
    }

    /**
     * "Touches" the specified {@code RepositoryFileOutputStream}s so that
     * their underlying data files will not expire
     * 
     * @param streams a {@code Collection} of those
     *        {@code RepositoryFileOutputStream}s that should be touched; none
     *        should be {@code null} or closed
     * @throws IOException on error, with a nested:
     *         <ul>
     *         <li>{@code IllegalStateException} if any caller-specified output
     *         streams have already been closed;</li>
     *         <li>{@code OperationFailedException} on a low-level error from
     *         core; in this case some of the {@code RepositoryFileOutputStream}s
     *         may have been touched, at least one will not have been</li>
     *         <li>{@code RemoteException} if an RMI error occurs;</li>
     *         <li>{@code ResourceNotFoundException} if core determines that
     *         one of the tickets has expired or otherwise does not exist.</li>
     *         </ul>
     */
    public static void touchStreams(
            Collection<RepositoryFileOutputStream> streams) throws IOException {
        if (streams.size() > 0) {
            CoreConnector cc = null;
            int[] ticketIds = new int[streams.size()];
            int i = 0;

            for (RepositoryFileOutputStream rfos : streams) {
                if (rfos.isClosed) {
                    throw newIOExceptionWithCause(new IllegalStateException());
                }
                ticketIds[i++] = rfos.ticketId;
                cc = rfos.cc;
            }

            if (cc != null) {
                try {
                    cc.getRepositoryManager().renewTickets(ticketIds);
                } catch (ResourceNotFoundException ex) {
                    throw newIOExceptionWithCause(ex);
                } catch (OperationFailedException ex) {
                    throw newIOExceptionWithCause(ex);
                } catch (RemoteException ex) {
                    cc.reportRemoteException(ex);
                    throw newIOExceptionWithCause(ex);
                }
            }
        }
    }

    /**
     * Generates {@code IOException} objects configured with nested
     * {@code Throwable} causes, as a convenience for working around the absence
     * of an IOException constructor that would accomplish the same result.
     * 
     * @param cause the {@code Throwable} cause of the exception to generate
     * @return an IOException configured with the specified cause
     */
    private static IOException newIOExceptionWithCause(Throwable cause) {
        IOException ex = new IOException();

        ex.initCause(cause);

        return ex;
    }
}
