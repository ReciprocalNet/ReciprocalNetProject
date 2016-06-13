/*
 * Reciprocal Net project
 * 
 * Uploader.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 * 11-Nov-2005: midurbin added support for the TransferFailedEvent with a
 *              reason of INVALID_FILENAME
 * 26-May-2006: jobollin reformatted the source, updated docs, and performed
 *              minor cleanup
 * 03-Nov-2006: jobollin fixed bug #1810 and performed further source cleanup
 */

package org.recipnet.site.applet.uploader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * <p>
 * An applet that allows files to be dragged-and-dropped onto it and then
 * transfers the dropped files to an {@code UploaderServlet} on some remote web
 * server. The precise applet-to-servlet communications mechanism is delegated
 * to the {@code RepositoryFileTransfer} class.
 * </p><p>
 * The applet parameters supported are:
 * <dl>
 * <dt>operationId</dt>
 * <dd>an integer value that is passed opaquely to the constructor of
 * {@code RepositoryFileTransfer}. This parameter is required.</dd>
 * <dt>supportServletHref</dt>
 * <dd>a string that is a relative URL at which the {@code UploaderServlet} is
 * accessible. The URL is assumed to be relative to this applet's "document
 * base", as returned by {@code Applet.getDocumentBase()}. This parameter is
 * required.</dd>
 * </dl>
 * </p><p>
 * This applet utilizes some Java features that were introduced in the 1.5
 * platform. It is recommended that web applications require browsers using this
 * applet to have a Java VM supporting Java 1.5 or higher. The most notable 1.5
 * feature utilized is {@code HttpUrlConnection}'s support for HTTP
 * chunked-stream encoding. By contrast, J2SDK 1.4 had a nasty habit of
 * buffering all posted data in memory and later sending it when it felt like
 * it, with the buffer hard-limited to 32 MB in size. J2SDK 1.4's behavior in
 * this respect is not useful for this applet's purposes.
 * </p><p>
 * It is recommended that the .jar file containing this applet that browsers are
 * expected to download and execute be digitally signed. Many browsers and Java
 * VMs will refuse to execute some of this applet's operations for security
 * reasons unless the applet is signed.
 * </p>
 */
public class Uploader extends JApplet implements TransferEventListener {
    
    /**
     * A Swing control that displays a list of transferred files. Set by
     * {@code createGUI()}.
     */
    private JTextArea textArea;

    /**
     * A Swing control that displays the progress of the file presently being
     * transferred, or null if none is presently being displayed. Set by
     * {@code processTransferEvent()} and cleared by
     * {@code hideProgressDialog()}.
     */
    private ProgressDialog progressDialog;

    /**
     * A reference to the {@code TransferEngine} engine that transfers files to
     * the server in the background. Set by {@code start()}.
     */
    private TransferEngine transferEngine;

    /**
     * A reference to the {@code DropTargetConnector} object that will receive
     * file drops and forward them to the {@code TransferThread}.
     */
    private DropTargetConnector dropTargetConnector;

    /**
     * A state variable that is initialized to false and is set to true by
     * {@code processTransferProgressEvent()} once the first file has been
     * transferred.
     */
    private boolean hasTransferredFile;
    
    /**
     * Overrides {@code Applet}; the current implementation just creates the UI
     * elements from within the AWT event dispatching thread.
     */
    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    createGUI();
                }
            });
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        hasTransferredFile = false;
    }

    /**
     * Overrides {@code Applet}; the current implementation initializes the
     * {@code TransferThread} and begins waiting for drag-and-drop events.
     */
    @Override
    public void start() {
        try {
            transferEngine = new TransferEngine(
                    Integer.parseInt(getParameter("operationId")),
                    new URL(getDocumentBase(),
                            getParameter("supportServletHref")));
            transferEngine.addTransferEventListener(this);
            dropTargetConnector.setTransferEngine(transferEngine);
            new Thread(transferEngine).start();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Overrides {@code Applet}; the current implementation stops listenging
     * for drag-and-drop events and then encourages the {@code TransferThread}
     * to stop. All member variables that comprise applet state are cleared so
     * in order to support this applet's reuse on another page.
     */
    @Override
    public void stop() {
        if (transferEngine != null) {
            dropTargetConnector.setTransferEngine(null);
            transferEngine.terminateThread();
            transferEngine = null;
        }
        hasTransferredFile = false;
    }

    /**
     * Implements the {@code TransferEventListener} interface and designed to be
     * invoked by the {@code TransferThread} only. This implementation receives
     * a {@code TransferProgressEvent} message and causes
     * {@code processTransferProgressEvent()} to be invoked later, from the AWT
     * event dispatching thread, with the same event as its argument.
     * 
     * @param event the {@code TransferProgressEvent} to handle
     */
    public void receiveTransferProgressEvent(final TransferProgressEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                processTransferProgressEvent(event);
            }
        });
    }

    /**
     * Implements the {@code TransferEventListener} interface and designed to be
     * invoked by the {@code TransferThread} only. This implementation receives
     * a {@code TransferFailedEvent} message and causes
     * {@code processTransferFailedEvent()} to be invoked later, from the AWT
     * event dispatching thread, with the same event as its argument.
     * 
     * @param event the {@code TransferFailedEvent} to handle
     */
    public void receiveTransferFailedEvent(final TransferFailedEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                processTransferFailedEvent(event);
            }
        });
    }

    /**
     * Implements the {@code TransferEventListener} interface and designed to be
     * invoked by the {@code TransferThread} only. This implementation receives
     * a {@code TransferThreadIdleEvent} message and causes
     * {@code processTransferThreadIdelEvent()} to be invoked later, from the
     * AWT event dispatching thread, with the same event as its argument.
     * 
     * @param event the {@code TransferThreadIdleEvent} to handle
     */
    public void receiveTransferThreadIdleEvent(
            final TransferThreadIdleEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                processTransferThreadIdleEvent(event);
            }
        });
    }

    /**
     * An internal method that processes event messages received from the
     * {@code TransferEngine}. In order to assure thread safety, this method
     * should be invoked from only the AWT event dispatching thread.
     */
    private void processTransferProgressEvent(TransferProgressEvent event) {
        if (!event.transferComplete) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(
                        findParentFrame(getContentPane()),
                        transferEngine);
                progressDialog.pack();
            }
            progressDialog.processTransferProgressEvent(event);
            if (!progressDialog.isVisible()) {
                progressDialog.setVisible(true);
            }
        } else {
            if (!hasTransferredFile) {
                textArea.setText(null);
                hasTransferredFile = true;
            }
            textArea.append(event.filename + "\n");
        }
    }

    /**
     * An internal method that processes event messages received from the
     * {@code TransferEngine}. In order to assure thread safety, this method
     * should be invoked from only the AWT event dispatching thread.
     */
    private void processTransferThreadIdleEvent(
            @SuppressWarnings("unused") TransferThreadIdleEvent event) {
        hideProgressDialog();
    }

    /**
     * An internal method that processes event messages received from the
     * {@code TransferEngine}. In order to assure thread safety, this method
     * should be invoked from only the AWT event dispatching thread.
     */
    private void processTransferFailedEvent(TransferFailedEvent event) {
        switch (event.reason) {
            case SERVER_ERROR:
                hideProgressDialog();
                System.err.println("Server error code "
                        + event.httpResponseCode + " message "
                        + event.httpResponseMessage + " data "
                        + event.httpResponseData + " for file "
                        + event.filename);
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "The server returned error code \n'"
                                + event.httpResponseCode
                                + " "
                                + event.httpResponseMessage
                                + "'.  \nThe file '"
                                + event.filename
                                + "' and any other queued files were"
                                + " not transferred.\nAdditional debugging"
                                + " information may have been written to the console.",
                        "Transfer error", JOptionPane.ERROR_MESSAGE);
                break;
            case JAVA_EXCEPTION:
                hideProgressDialog();
                event.exception.printStackTrace();
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "An exception, \n'"
                                + event.exception.getMessage()
                                + "', \nhas occurred.  The file '"
                                + event.filename
                                + "' and any other queued files were not transferred."
                                + "\nAdditional debugging information may have been"
                                + " written to the console.", "Transfer error",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case USER_CANCELLED:
                // Nothing we need to do in this case. The transfer thread
                // will send us an Idle event soon so we'll hide the progress
                // dialog then.
                break;
            case DUPLICATE_FILENAME:
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "The file '"
                                + event.filename
                                + "' was not transferred"
                                + " because another file \nwith the same name was"
                                + " transferred to the server earlier.  To \noverwrite"
                                + " the file that was transferred to the server"
                                + " earlier, click \nthe OK button and begin"
                                + " again.", "Transfer not possible",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case INVALID_FILENAME:
                JOptionPane.showMessageDialog(
                        getContentPane(),
                        "The file '"
                                + event.filename
                                + "\' was not"
                                + " transferrred because its\n filename was not valid"
                                + " or contained illegal characters.",
                        "Invalid filename", JOptionPane.ERROR_MESSAGE);
                break;
            default:
                // Can't happen because all reason codes were addressed above.
                assert false;
        }
    }

    /**
     * Internal helper method that creates the elements of the user interface.
     * To assure thread safety, this method should be called from only the AWT
     * event dispatching thread.
     */
    private void createGUI() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBackground(Color.lightGray);
        textArea.append("(drag and drop files into this area)\n");
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        dropTargetConnector = new DropTargetConnector();
        new DropTarget(textArea, DnDConstants.ACTION_COPY, dropTargetConnector);
    }

    /**
     * Helper method that causes the {@code ProgressDialog}, if there is one,
     * to be hidden from view in the user interface.
     */
    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.setVisible(false);
        }
    }

    /**
     * Internal function that recursively determines the parent {@code Frame}
     * for a given Swing control. The returned "ultimate ancestor" can be used
     * as the parent of pop-up dialog boxes, for example.
     */
    private Frame findParentFrame(Container child) {
        
        for (Container c = child; c != null; c = c.getParent()) {
            if (c instanceof Frame) {
                return (Frame) c;
            }
        }
        
        return null;
    }
}
