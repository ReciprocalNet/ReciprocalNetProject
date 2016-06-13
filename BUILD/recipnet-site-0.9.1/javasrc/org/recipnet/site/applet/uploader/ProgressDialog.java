/*
 * Reciprocal Net project
 * 
 * ProgressDialog.java
 *
 * 20-Jun-2005: ekoperda wrote first draft
 * 26-May-2006: jobollin reformatted the source
 * 03-Nov-2006: jobollin performed source cleanup
 */

package org.recipnet.site.applet.uploader;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

/**
 * An extension of {@code JDialog} that displays progress information regarding
 * a file transfer in progress. Progress reports are conveyed to this user
 * interface element via {@code TransferProgressEvent} objects. A "cancel"
 * button is part of the user interface. In the event the user clicks it, this
 * class communicates with the {@code TransferThread} to abort all ongoing and
 * queued file transfers.
 */
public class ProgressDialog extends JDialog {
    
    /**
     * One of the controls utilized in the user interface. This label appears
     * above the progress bar.
     */
    private final JLabel topLabel;

    /**
     * One of the controls utilized in the user interface. This label appears
     * below the progress bar.
     */
    private final JLabel bottomLabel;

    /** One of the controls utilized in the user interface. */
    private final JProgressBar progressBar;

    /** A reference to the {@code TransferEngine}. */
    private final TransferEngine transferEngine;

    /** Used when displaying numbers. */
    private final NumberFormat numberFormat;

    /**
     * Initializes a new {@code ProgressDialog} with the specified owner and
     * associated {@code TransferEngine}
     * 
     * @param owner defined by {@code JDialog} and passed to the superclass
     *        opaquely.
     * @param transferEngine a reference to the application's
     *        {@code TransferEngine}. That class's {@code cancelQueuedFiles()}
     *        method is invoked if this dialog box's "cancel" button is clicked.
     */
    public ProgressDialog(Frame owner, TransferEngine transferEngine) {
        super(owner);
        
        JPanel pane;
        Box box;
        Box innerBox;
        
        // Non-visible members
        this.transferEngine = transferEngine;
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);

        // General dialog properties
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(owner);
        setTitle("Transfer in progress");
        
        /*
         * Dialog content
         */
        
        // This will be the content pane
        pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
        pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Current filename label
        topLabel = new JLabel(" ");
        topLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        box = Box.createHorizontalBox();
        box.add(topLabel);
        box.add(Box.createHorizontalGlue());
        pane.add(box);
        
        // Progress bar and numeric progress indicator
        box = Box.createVerticalBox();
        
        // progress bar
        progressBar = new JProgressBar(0, 1);
        progressBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 0, 0, 0),
                progressBar.getBorder()));
        progressBar.setPreferredSize(
                new Dimension(400, progressBar.getPreferredSize().height));
        box.add(progressBar);
        
        // KB count label
        bottomLabel = new JLabel(" ");
        innerBox = Box.createHorizontalBox();
        innerBox.add(Box.createHorizontalGlue());
        innerBox.add(bottomLabel);
        box.add(innerBox);
        
        pane.add(box);

        // Cancel button
        JButton button = new JButton("Cancel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(
                    @SuppressWarnings("unused") ActionEvent event) {
                ProgressDialog.this.transferEngine.cancelQueuedFiles();
            }
        });
        box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(button);
        box.add(Box.createHorizontalGlue());
        pane.add(box);

        // Set the configured JPanel as the content pane
        
        setContentPane(pane);
    }

    /**
     * Other classes may invoke this method to influence the progress
     * information that this dialog box displays. This dialog box's child
     * controls are updated with the information in {@code event} accordingly.
     */
    public void processTransferProgressEvent(TransferProgressEvent event) {
        int bytesTransferred = bytesToKilobytes(event.bytesTransferred);
        int totalBytes = bytesToKilobytes(event.bytesTotal);
        
        topLabel.setText("Transferring " + event.filename
                + " to a holding area on the server...");
        progressBar.setMaximum(totalBytes);
        progressBar.setValue(bytesTransferred);
        bottomLabel.setText(numberFormat.format(bytesTransferred)
                + " of " + numberFormat.format(totalBytes) + " KB");
    }

    /** Internal function that converts a byte-count to kilobytes. */
    private int bytesToKilobytes(long bytes) {
        return (int) (bytes / 1024);
    }
}
