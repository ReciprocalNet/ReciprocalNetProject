/*
 * Reciprocal Net Project
 * 
 * RepositoryFileTransfer.java
 *
 * 15-Jun-2005: ekoperda wrote first draft
 * 17-May-2006: jobollin rewrote parts of this class for better modularity;
 *              formatted the source
 */

package org.recipnet.site.shared;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProtocolException;

/**
 * <p>
 * A class embodying a simple, general, single-file file transfer protocol. To
 * send a file, the user should obtain an instance of this class via
 * {@link #forUpload(int, File)}, then direct it to transfer the file to an
 * appropriate output stream via {@link #doTransfer(OutputStream, Regulator)}.
 * To receive a file, the user should wrap the data in an input stream and
 * obtain an instance of this class via {@link #forDownload(InputStream)}, then
 * direct the result to an appropriate location via
 * {@link #doTransfer(OutputStream, Regulator)}.
 * </p><p>
 * Certain file-related metadata are transferred alongside the file: an
 * {@code operationId} number, opaque to the protocol, that identifies a
 * specific context in which the transfer occurs; the local name of the file on
 * the sending end, and the length of the file in bytes. Also, both ends can
 * monitor progress and/or interrupt an ongoing transfer by use of a callback
 * object represented by the {@link Regulator} interface.
 * </p>
 */
public abstract class RepositoryFileTransfer {

    /**
     * the version of the {@code RepositoryFileTransfer} interface specification
     * implemented by this class. Software developers should increment this
     * value whenever the over-the-wire data format changes. The value
     * facilitates graceful detection of version mismatch between the applet and
     * the servlet.
     */
    private static final int VERSION = 1;

    /**
     * the number of bytes that
     * {@link #doTransfer(OutputStream, Regulator) doTransfer()} will attempt to
     * transfer at each read. Changing this value can affect granularity of
     * transfer progress notifications and also may affect overall transfer
     * performance.
     */
    private static final int BUFFER_SIZE = 32768;

    /**
     * A {@code Regulator} that always indicates that transfers should continue;
     * intended for use where a {@code Regulator} is wanted for API consistency,
     * but no other one has been specified
     */
    private static final Regulator DUMMY_REGULATOR = new Regulator() {
        public boolean shouldRepositoryFileTransferContinue(
                @SuppressWarnings("unused")
                String fileName, @SuppressWarnings("unused")
                long bytesTransferred, @SuppressWarnings("unused")
                long bytesTotal) {
            return true;
        }
    };

    /**
     * The file-specific metadata associated with this transfer, in the form of
     * a {@code Header} object
     */
    private final Header header;

    /**
     * A reference to the stream, either local or remote, from which file data
     * will be read. Set at construction time.
     */
    private final InputStream dataStream;

    /**
     * Initializes a {@code RepositoryFileTransfer} with the specified header
     * and input data
     * 
     * @param header a {@code Header} representing the file-specific metadata
     *        pertinent to the input bytes
     * @param in an {@code InputStream} from which to obtain the file data
     */
    private RepositoryFileTransfer(Header header, InputStream in) {
        this.header = header;
        this.dataStream = in;
    }

    /**
     * Obtains the file-specific metadata pertinent to this transfer, in the
     * form of a {@code Header}
     * 
     * @return a {@code Header} containing all the file-specific metadata
     *         pertinent to this transfer
     */
    final Header getHeader() {
        return header;
    }

    /**
     * Retrieves the operation ID with which this transfer is associated
     * 
     * @return the operation ID, whether explicitly specified to this object or
     *         read among the file-specific metadata
     */
    public int getOperationId() {
        return header.getOperationId();
    }

    /**
     * Retrieves the sending-side local name of the transferred file  
     * 
     * @return the name of the file transferred by this
     *         {@code RepositoryFileTransfer}
     */
    public String getFileName() {
        return header.getFileName();
    }

    /**
     * Retrieves the length, in bytes, of the transferred file
     * 
     * @return the number of bytes of file data associated with this transfer
     */
    public long getFileLength() {
        return header.getFileLength();
    }

    /**
     * Transfers the data with which this {@code RepositoryFileTransfer} is
     * configured. This method should not be invoked more than once on any
     * {@code RepositoryFileTransfer} instance.
     * 
     * @param out the {@code OutputStream} to which the transferred bytes should
     *        be directed
     * @param regulator a {@code Regulator} with which the invoker should be
     *        informed about the progress of the transfer, and with which it is
     *        possible to cancel the transfer prior to completion
     * @return {@code true} if and only if the configured number of bytes were
     *         transferred successfully
     * @throws IOException if an I/O error occurs
     */
    public boolean doTransfer(OutputStream out, Regulator regulator)
            throws IOException {
        startTransfer(out);
        return copyStreamToStream(dataStream, out, getFileLength(),
                (regulator == null) ? DUMMY_REGULATOR : regulator);
    }

    /**
     * Performs any setup that must occur in advance of transferring the file's
     * bytes; concrete subclasses must implement this method appropriately
     * 
     * @param out the {@code OutputStream} to which the transferred bytes will
     *        ultimately be directed; protocol and file metadata may be
     *        delivered to this stream by this method, as appropriate
     * @throws IOException if an I/O error occurs
     */
    protected abstract void startTransfer(OutputStream out) throws IOException;

    /**
     * Copies the specified number of bytes between the two streams, executing
     * callbacks via the specified {@code Regulator} to apprise the invoker of
     * the progress of the transfer and afford him the opportunity to cancel the
     * transfer midstream
     * 
     * @return {@code true} if the specified number of bytes were transferred,
     *         or {@code false} if the transfer was cancelled prematurely upon
     *         request of the caller-specified {@code regulator}.
     * @param in the source stream from which bytes are read.
     * @param out the destination stream to which bytes are written.
     * @param byteCount the number of bytes to be copied.
     * @param regulator if specified, this {@code Regulator} object is consulted
     *        periodically during the transfer operation. The {@code Regulator}
     *        object receives ongoing status updates from this method and is
     *        empowered to terminate the copy operation prematurely. This
     *        argument may be null, in which case no {@code Regulator} is
     *        consulted.
     * @throws IOException if any I/O-related problem is encountered, including
     *         if the specified {@code InputStream} cannot provide as many bytes
     *         as were specified
     */
    private boolean copyStreamToStream(InputStream in, OutputStream out,
            long byteCount, Regulator regulator) throws IOException {
        assert regulator != null;

        byte[] buf = new byte[BUFFER_SIZE];

        for (long bytesRead = 0; bytesRead < byteCount;) {

            if (!regulator.shouldRepositoryFileTransferContinue(getFileName(),
                    bytesRead, byteCount)) {
                // The caller asked us to abort the copy and return early.
                return false;
            }

            /*
             * The typecast below is safe because buf.length cannot be greater
             * than Integer.MAX_VALUE
             */
            int bytesToCopy = (int) Math.min(buf.length, byteCount - bytesRead);
            int rc = in.read(buf, 0, bytesToCopy);

            if (rc >= 0) {
                out.write(buf, 0, rc);
                bytesRead += rc;
            } else {
                // End of file was reached unexpectedly.
                throw new EOFException();
            }
        }

        return true;
    }

    /**
     * Creates and returns a new {@code RepositoryFileTransfer} appropriate for
     * transferring the contents of the specified locally-accessible file, in
     * the context of the specified operation ID
     * 
     * @param operationId an opaque operation ID defining the context of this
     *        transfer
     * @param file a {@code File} designating the locally-accessible file that
     *        should be transferred; the file referenced by this object should
     *        exist and should be a regular file
     * @return a {@code RepositoryFileTransfer} for transferring the specified
     *         file
     * @throws FileNotFoundException if the specified file does not exist
     * @see #forDownload(InputStream)
     */
    public static RepositoryFileTransfer forUpload(int operationId, File file)
            throws FileNotFoundException {
        return new RepositoryFileUpload(operationId, file);
    }

    /**
     * Creates and returns a new {@code RepositoryFileTransfer} appropriate for
     * transferring bytes from the specified {@code InputStream}; protocol and
     * file metadata will be read from the specified stream within the scope of
     * this method
     * 
     * @param in an {@code InputStream} from which to read file transfer
     *        metadata and file data; the data should be formatted as by the
     *        action of the type of transfer object obtained from
     *        {@code forUpload()}
     * @return a {@code RepositoryFileTransfer} for transferring file data from
     *         the specified stream
     * @throws ProtocolException if the stream does not contain appropriate
     *         metadata
     * @throws IOException if an I/O error occurs
     * @see #forUpload(int, File)
     */
    public static RepositoryFileTransfer forDownload(InputStream in)
            throws ProtocolException, IOException {
        return new RepositoryFileDownload(in);
    }

    /**
     * A concrete {@code RepositoryFileTransfer} subclass for use in
     * transferring a local file to some, possibly remote, receiver
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class RepositoryFileUpload extends RepositoryFileTransfer {

        /**
         * Initializes a new {@code RepositoryFileUpload} configured to upload
         * the specified file in the context of the specified persisted
         * operation
         * 
         * @param operationId identifies a particular persisted operation on the
         *        remote host; opaque to the local system
         * @param file a {@code File} representing the local file to be uploaded
         * @throws FileNotFoundException if {@code file} does not exist on the
         *         filesystem or could not be opened
         */
        public RepositoryFileUpload(int operationId, File file)
                throws FileNotFoundException {
            super(new Header(operationId, file.getName(), file.length()),
                    new FileInputStream(file));
        }

        /**
         * Performs any setup that must occur in advance of transferring the
         * file's bytes from the configured {@code InputStream} to the specified
         * {@code OutputStream}; this version sends metadata about this version
         * of the transfer protocol and about the file to be transferred
         * 
         * @param out the {@code OutputStream} to which the transferred bytes
         *        will ultimately be directed
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void startTransfer(OutputStream out) throws IOException {
            getHeader().writeTo(out);
        }
    }

    /**
     * A concrete {@code RepositoryFileTransfer} subclass for use in
     * receiving a file transfer from some, possibly remote, sender
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private static class RepositoryFileDownload extends RepositoryFileTransfer {

        /**
         * Initializes a new {@code RepositoryFileDownload} for transferring
         * data from the specified stream
         * 
         * @param in an {@code InputStream} representing data received from the
         *        remote host.  The data should conform to the protocol defined
         *        by {@code RepositoryFileTransfer} and its members; in the
         *        usual usage of the protocol, the data will have been produced
         *        by a {@link RepositoryFileUpload} object
         * @throws ProtocolException if metadata read from {@code in} are
         *         inconsistent with the transfer protocol implemented by this
         *         class
         * @throws IOException if any other I/O-related problem is encountered.
         */
        public RepositoryFileDownload(InputStream in) throws ProtocolException,
                IOException {
            super(Header.readFrom(in), in);
        }

        /**
         * @see RepositoryFileTransfer#startTransfer(OutputStream)
         */
        @Override
        protected void startTransfer(
                @SuppressWarnings("unused") OutputStream out) {
            /*
             * does nothing; the metadata were already handled by the
             * constructor
             */
        }
    }

    /**
     * Represents the file-specific metadata transferred between sender and
     * receiver in this transfer protocol, and provides the protocol
     * implementation for reading, writing, and verifying that metadata
     * 
     * @author jobollin
     * @version 0.9.0
     */
    final static class Header {
        
        /**
         * The operation ID for a particular transfer
         */
        private final int operationId;

        /**
         * The name of the file transferred
         */
        private final String fileName;

        /**
         * The length, in bytes, of the file transferred
         */
        private final long fileLength;

        /**
         * Initializes a new {@code Header} with the specified parameters
         * 
         * @param operationId the (opaque) operation ID
         * @param fileName the name of the file transferred
         * @param fileLength the length of the file transferred, in bytes
         */
        public Header(int operationId, String fileName, long fileLength) {
            this.operationId = operationId;
            this.fileName = fileName;
            this.fileLength = fileLength;
        }

        /**
         * @return the {@code operationId} {@code int}
         */
        public int getOperationId() {
            return operationId;
        }

        /**
         * @return the {@code fileName} {@code String}
         */
        public String getFileName() {
            return fileName;
        }

        /**
         * @return the {@code fileLength} {@code long}
         */
        public long getFileLength() {
            return fileLength;
        }

        /**
         * Writes the protocol-specific metadata represented by this class and
         * the file-specific metadata represented by this object to the
         * specified stream
         * 
         * @param out the {@code OutputStream} to which the data should be
         *        directed
         * @throws IOException if an I/O error occurs
         */
        public void writeTo(OutputStream out) throws IOException {
            DataOutputStream dos = new DataOutputStream(out);

            // Write protocol metadata
            dos.writeUTF(RepositoryFileTransfer.class.getName());
            dos.writeInt(VERSION);

            // Write transfer-specific metadata
            dos.writeInt(getOperationId());
            dos.writeUTF(getFileName());
            dos.writeLong(getFileLength());

            // Ensure that the data are sent
            dos.flush();
        }

        /**
         * Reads protocol and file metadata from the specified stream,
         * confirming that they are consistent with the transfer protocol
         * supported by this class, and returning the file-specific metadata in
         * the form of a new {@code Header}
         * 
         * @param in the {@code InputStream} from which to read the data
         * @return a new {@code Header} representing the file-specific metadata
         *         read from the stream
         * @throws ProtocolException if the data read from the stream are not
         *         consistent with the protocol or protocol version supported by
         *         this class
         * @throws IOException if an I/O error occurs
         */
        public static Header readFrom(InputStream in) throws ProtocolException,
                IOException {
            DataInputStream dis = new DataInputStream(in);

            // Do sanity checks on the stream.
            if (!RepositoryFileTransfer.class.getName().equals(dis.readUTF())) {
                // Data not from a RepositoryFileTransfer object.
                throw new ProtocolException();
            } else if (dis.readInt() != VERSION) {
                // Version mismatch.
                throw new ProtocolException();
            } else {
                return new Header(dis.readInt(), dis.readUTF(), dis.readLong());
            }

            // Abandon the DataInputStream without closing it
        }
    }

    /**
     * An interface definining a callback API with which protocol users can
     * monitor file transfer progress and, if desired, cancel transfers
     * midstream
     */
    public interface Regulator {

        /**
         * A callback function that receives periodic status updates from
         * {@code RepositoryFileTransfer.copyStreamToStream()} and is empowered
         * to terminate copy operations prematurely.
         * 
         * @return true if the copy operation should continue as originally
         *         requested, or false if the copy operation should be aborted
         *         immediately.
         * @param fileName the name of the file embodied by the calling
         *        {@code RepositoryFileTransfer} class, as was sent by the
         *        remote host or as was read from the local filesystem.
         * @param bytesTransferred the number of bytes already copied during the
         *        present transfer operation.
         * @param bytesTotal the total number of bytes scheduled to be copied
         *        during the present transfer operation, assuming the operation
         *        is not aborted prematurely.
         */
        public boolean shouldRepositoryFileTransferContinue(String fileName,
                long bytesTransferred, long bytesTotal);
    }
}
