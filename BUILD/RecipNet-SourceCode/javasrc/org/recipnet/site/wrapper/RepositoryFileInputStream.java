/*
 * Reciprocal Net project
 * 
 * RepositoryFileInputStream.java
 *
 * 18-Jun-2004: ekoperda wrote first draft
 * 04-Jan-2006: jobollin fixed bug #1717 in read(byte[], int, int); removed
 *              unusued imports; formatted the source to remove tab characters
 */

package org.recipnet.site.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.OperationNotPermittedException;

/**
 * An extension of {@code InputStream} that reads the contents of a sample data
 * file in the repository by invoking methods on {@code RepositoryManager} via
 * RMI. The caller is responsible for opening his desired file by invoking
 * {@code RepositoryManager.beginReadingDataFile()} first before using this
 * class. The caller should invoke {@code close()} once his need for this stream
 * is finished. This class is NOT thread-safe and does not support mark()-ing.
 */
public class RepositoryFileInputStream extends InputStream {
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
     * The preferred number of bytes to be fetched in each round-trip to core,
     * set by the constructor.
     */
    private int preferredBufferSize;

    /**
     * A fixed buffer of data that has been fetched from core by
     * {@code fetchData()} but has not yet been delivered to the caller via
     * {@code read()}. The buffer does not revolve; it always begins at index 0
     * for efficiency.
     */
    private byte[] buffer;

    /**
     * Index into {@code buffer} that identifies the first byte to be returned
     * in the next invocation of {@code read()}. Incremented by the two
     * {@code read()}'s and {@code skip()} and cleared by {@code fetchData()}.
     */
    private int nextIndexInBufferToRead;

    /**
     * Flag that is set by {@code fetchData()} once the last chunk of data in
     * the file is read from {@code RepositoryManager}. Note that data may
     * remain in this class's {@code buffer} even after this flag is set, so
     * callers are not informed of the end-of-file condition until after our
     * {@code buffer} is empty also.
     */
    private boolean eofReached;

    /**
     * Flag that is set by {@code close()} once it has been invoked; checked by
     * other methods to guarantee state-safety.
     */
    private boolean isClosed;

    /**
     * Initializes a {@code RepositoryFileInputStream} with the specified
     * CoreConnector and ticket ID, and a default buffer size
     * 
     * @param cc a reference to the web application's {@code CoreConnector}
     *        object.
     * @param ticketId a ticket number as issued by the caller's previous
     *        invocation of {@code RepositoryManager.beginReadingDataFile()}.
     *        The caller must relinquish control of the ticket to this class and
     *        not use it again.
     */
    public RepositoryFileInputStream(CoreConnector cc, int ticketId) {
        /*
         * The default buffer size of 128 KB hard-coded here is a result of
         * server profiling done by midurbin in September 2003. He determined
         * that best performance across RMI to core was achieved with a buffer
         * size of around 128 KB.
         */
        this(cc, ticketId, 131072);
    }

    /**
     * Constructs a new {@code RepositoryFileInputStream} object that the caller
     * may then use to read the contents of a repository data file.
     * 
     * @param cc a reference to the web application's {@code CoreConnector}
     *        object.
     * @param ticketId a ticket number as issued by the caller's previous
     *        invocation of {@code RepositoryManager.beginReadingDataFile()}.
     *        The caller must relinquish control of the ticket to this class and
     *        not use it again.
     * @param preferredBufferSize an optional argument; this is the number of
     *        bytes that this class will attempt to fetch from
     *        {@code RepositoryManager} in each round-trip to core.
     */
    public RepositoryFileInputStream(CoreConnector cc, int ticketId,
            int preferredBufferSize) {
        this.cc = cc;
        this.ticketId = ticketId;
        this.preferredBufferSize = preferredBufferSize;
        this.buffer = null;
        this.nextIndexInBufferToRead = 0;
        this.eofReached = false;
        this.isClosed = false;
    }

    /**
     * Overrides {@code InputStream} but behaves according to that
     * specification.
     * 
     * @return the next byte of data or -1 if end-of-file has been reached.
     * 
     * @throws IllegalStateException if {@code close()} has already been
     *         invoked.
     * @throws IOException with: * a nested {@code IllegalStateException} if
     *         {@code close()} has already been invoked. * a nested
     *         {@code OperationFailedException} if {@code RepositoryManager}
     *         encountered a low-level error, * a nested
     *         {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to read, * a nested {@code RemoteException} on RMI
     *         error; in this case case {@code CoreConnector} has been notified
     *         of the error already, or * a nested
     *         {@code ResourceNotFoundException} if {@code RepositoryManager}
     *         determined the ticket specified at construction time is not
     *         valid.
     */
    @Override
    public int read() throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }
        if (available() == 0) {
            fetchData();
        }
        if (available() >= 1) {
            int rc = this.buffer[this.nextIndexInBufferToRead];
            this.nextIndexInBufferToRead++;
            return rc;
        }

        assert this.eofReached;
        return -1;
    }

    /**
     * Overrides {@code InputStream} but behaves according to that
     * specification.
     * 
     * @param b the buffer into which the data is read.
     * @param off the start offset in array {@code b} at which the data is
     *        written.
     * @param len the maximum number of bytes to read.
     * 
     * @return the number of bytes read or -1 if end-of-file has been reached.
     * 
     * @throws IndexOutOfBoundsException if {@code off} is negative or
     *         {@code len} is negative or {@code off} and {@code len} combined
     *         is greater than the size of {@code b}.
     * @throws IOException with: <ul>
     *         <li>a nested {@code IllegalStateException} if
     *         {@code close()} has already been invoked.</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error,
     *         <li>a nested {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to read</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         case {@code CoreConnector} has been notified of the error
     *         already</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined the ticket specified at
     *         construction time is not valid.</li>
     *         </ul>
     * @throws NullPointerException if {@code b} is {@code null}.
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int countBytesCopied;
        
        if (b == null) {
            throw new NullPointerException();
        }
        if ((off < 0) || (len < 0) || (off + len > b.length)) {
            throw new IndexOutOfBoundsException();
        }
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }
        if (len == 0) {  // the only case where it is correct to return 0
            return 0;
        }
        
        /*
         * Copy data to the caller's buffer until it fills up or we run out
         * of data.
         */
        countBytesCopied = 0;
        while ((countBytesCopied < len)
                && !((available() == 0) && this.eofReached)) {
            // Fetch more data from core if necessary.
            if (available() == 0) {
                fetchData();
            }

            // Copy some bytes from our buffer to the caller's.
            int c = Math.min(len - countBytesCopied, available());
            System.arraycopy(buffer, this.nextIndexInBufferToRead, b, off
                    + countBytesCopied, c);
            this.nextIndexInBufferToRead += c;
            countBytesCopied += c;
        }

        return ((countBytesCopied == 0) ? -1 : countBytesCopied);
    }

    /**
     * Overrides {@code InputStream} but behaves according to that
     * specification.
     * 
     * @param n the number of bytes to be skipped.
     * 
     * @return the number of bytes actually skipped, as a {@code long}
     * 
     * @throws IOException with a nested {@code IllegalStateException} if
     *         {@code close()} has already been invoked.
     */
    @Override
    public long skip(long n) throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }

        // Decide how many bytes we can safety skip before we reached the
        // end of our buffer. This value is always expressable as a 31-bit
        // integer because this.available() is a 31-bit integer.
        int bytesToAdvance = (int) Math.min(available(), n);

        // Advance our read pointer and return.
        this.nextIndexInBufferToRead += bytesToAdvance;
        return bytesToAdvance;
    }

    /**
     * Overrides {@code InputStream} but behaves according to that
     * specification.
     * 
     * @return the number of bytes that can be read (or skipped over) from this
     *         input stream without blocking by the next caller of a method for
     *         this input stream.
     *         
     * @throws IOException with a nested {@code IllegalStateException} if
     *         {@code close()} has already been invoked.
     */
    @Override
    public int available() throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }
        return ((this.buffer == null) ? 0
                : (this.buffer.length - this.nextIndexInBufferToRead));
    }

    /**
     * Overrides {@code InputStream} but behaves according to that
     * specification.
     * 
     * @throws IOException with: <ul>
     *         <li>a nested {@code IllegalStateException} if
     *         {@code close()} has already been invoked</li>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         case {@code CoreConnector} has been notified of the error
     *         already</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined the ticket specified at
     *         construction time is not valid</li>
     *         </ul>
     */
    @Override
    public void close() throws IOException {
        if (this.isClosed) {
            throw newIOExceptionWithCause(new IllegalStateException());
        }

        try {
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
     * Internal function that fetches data from {@code RepositoryManager} and
     * replaces the {@code buffer}, resetting the read pointer for the buffer
     * to position 0.
     * 
     * @throws IOException with: <ul>
     *         <li>a nested {@code OperationFailedException} if
     *         {@code RepositoryManager} encountered a low-level error</li>
     *         <li>a nested {@code OperationNotPermittedException} if
     *         {@code RepositoryManager} determined the specified ticket is not
     *         permitted to read</li>
     *         <li>a nested {@code RemoteException} on RMI error; in this case
     *         case {@code CoreConnector} has been notified of the error
     *         already</li>
     *         <li>a nested {@code ResourceNotFoundException} if
     *         {@code RepositoryManager} determined the ticket specified at
     *         construction time is not valid</li>
     *         </ul>
     */
    private void fetchData() throws IOException {
        try {
            
            // Sanity check.
            if (this.eofReached) {
                return;
            }

            // Do the actual fetch from core.
            this.buffer = this.cc.getRepositoryManager().readFromDataFile(
                    this.ticketId, this.preferredBufferSize);

            // Test to discover if EOF might have been reached.
            if (buffer.length < preferredBufferSize) {
                this.eofReached = true;
            }
        } catch (OperationFailedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (OperationNotPermittedException ex) {
            throw newIOExceptionWithCause(ex);
        } catch (RemoteException ex) {
            this.cc.reportRemoteException(ex);
            throw newIOExceptionWithCause(ex);
        }
    }

    /**
     * Generates exception objects for error reporting, with specified causes.
     * This class can't throw exceptions specific to Reciprocal Net (descended
     * from RecipnetException) because these don't extend {@code IOException}.
     * Instead it throws plain {@code IOException}s with Reciprocal Net
     * exceptions as their causes.  This convenience method creates such
     * exceptions, covering the absence of an accessible {@code IOException}
     * constructor appropriate to the task.
     * 
     * @param  cause the {@code Throwable} cause with which to initialize the
     *         requested exception
     *         
     * @return an {@code IOException} having the specified cause
     */
    private IOException newIOExceptionWithCause(Throwable cause) {
        IOException ex = new IOException();
        ex.initCause(cause);
        return ex;
    }
}
