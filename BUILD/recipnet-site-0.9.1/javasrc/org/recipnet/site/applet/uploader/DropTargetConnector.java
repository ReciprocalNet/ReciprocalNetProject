/*
 * Reciprocal Net project
 * 
 * DropTargetConnector.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 * 03-Nov-2006: jobollin preformed source cleanup
 */

package org.recipnet.site.applet.uploader;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * A helper class that supports the user interface of the uploader applet. This
 * class receives drag-and-drop events from the Java runtime that consist of
 * references to one or more files on the local filesystem. These file
 * references are then forwarded to the {@code TransferThread}, if one has been
 * set, for further processing.
 */
public class DropTargetConnector extends DropTargetAdapter {
    
    /**
     * The {@code TransferEngine} to use to transfer files dropped on the
     * associated drop target
     */
    private TransferEngine transferEngine;

    /**
     * Initializes a new {@code DropTargetConnector}
     */
    public DropTargetConnector() {
        transferEngine = null;
    }

    /**
     * Supplies a reference to the {@code TransferThread} object that this one
     * will notify as files are dropped. A null value causes the property to be
     * unset and subsequent file-drop attempts to be rejected.
     */
    public synchronized void setTransferEngine(TransferEngine transferEngine) {
        this.transferEngine = transferEngine;
    }

    /**
     * Implements the {@code DropTargetListener} interface. The current
     * implementation receives file-drop events and forwards those file
     * references to the {@code TransferEngine}.
     */
    public synchronized void drop(DropTargetDropEvent ev) {
        Transferable tr = ev.getTransferable();

        if ((transferEngine == null)
                || !tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            ev.rejectDrop();
            return;
        }
        ev.acceptDrop(DnDConstants.ACTION_COPY);
        try {
            Collection<? extends File> files = (Collection<? extends File>)
                    tr.getTransferData(DataFlavor.javaFileListFlavor);
            
            for (File file : files) {
                if (!file.isFile()) {
                    // The file we were passed might be a directory of symlink
                    // or some other odd thing. Skip it.
                    continue;
                }
                transferEngine.queueFileTransfer(file);
            }
            ev.dropComplete(true);
        } catch (IOException ex) {
            ex.printStackTrace();
            ev.dropComplete(false);
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
            ev.dropComplete(false);
        }
    }
}
