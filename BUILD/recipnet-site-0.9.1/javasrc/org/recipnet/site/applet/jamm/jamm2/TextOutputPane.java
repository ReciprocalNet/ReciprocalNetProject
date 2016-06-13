/*
 * Reciprocal Net Project
 *
 * TextOutputPane.java
 *
 * Copyright (c) 1998, 2000, 2003 The Trustees of Indiana University
 *
 * 13-Jan-2003: jobollin made all imports explicit
 * 13-Jan-2003: jobollin inserted a file comment and added Javadoc comments;
 *              retroactively assigned to task #749
 * 13-Jan-2003: jobollin tweaked the internal DocumentWriter for a slight
 *              efficiency boost in some of its write methods
 * 21-Feb-2003: jobollin modified the stopListening method to correctly handle
 *              the initial startup case (which recently broke without
 *              explanation) (task #745)
 * 21-Feb-2003: jobollin removed the finalize method as it would never have 
 *              been called if necessary, and would never have been necessary
 *              if called
 * 27-Feb-2003: jobollin reformatted the source as part of task #749
 * 26-Mar-2003: jobollin fixed bug #807 to do with incorrect scrolling
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 * 26-May-2006: jobollin performed minor cleanup
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.awt.Component;
import java.awt.Point;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * An extension of <code>JScrollPane</code>, intended for use as a scrollable
 * output-only window.  The viewport scrolls as new text is added so that the
 * latest text is always visible unless the view is manually scrolled. Also
 * provided is a handle, in the form of a <code>PrintWriter</code>, for
 * appending data to the text area.  By default, this component uses only the
 * vertical scrollbar. The scrollable client of a <code>TextOutputPane</code>
 * must be of class <code>JTextArea</code> or a subclass.  This is necessary
 * because to implement autoscrolling, <code>TextOutputPane</code> instances
 * need to use the <code>JTextArea</code>'s getLineOfOffset() method and to
 * depend on its getScrollableUnitIncrement() method being position
 * independant.  <code>TextOutputPane</code>s go to some effort to prevent
 * other kinds of clients from being connected, but those efforts stop short
 * of denying access via the getViewport() method to the
 * <code>TextOutputPane</code>'s <code>JViewport</code>.  Runtime exceptions
 * or errors are likely to be generated and/or incorrect behavior is likely to
 * be observed if the view managed by this component is set to any other kind,
 * or if the document serving as the view's model is <code>null</code>.
 *
 * @author John C. Bollinger, Indiana University Molecular Structure Center
 * @version 0.6.0
 *
 * @see javax.swing.JScrollPane JScrollPane
 * @see javax.swing.JTextArea JTextArea
 */
public class TextOutputPane
        extends JScrollPane
        implements DocumentListener {

    /**
     * Constructs a new <code>TextOutputPane</code> with default parameters and
     * no text.
     */
    public TextOutputPane() {
        this("", VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Constructs a new <code>TextOutputPane</code> with specifed scroll bar
     * policies and no text
     *
     * @param vsbPolicy the vertical scrollbar policy
     * @param hsbPolicy the horizontal scrollbar policy
     *
     * @see javax.swing.JScrollPane JScrollPane
     */
    public TextOutputPane(int vsbPolicy, int hsbPolicy) {
        this("", vsbPolicy, hsbPolicy);
    }

    /**
     * Constructs a new <code>TextOutputPane</code> with default parameters and
     * the specified initial text
     *
     * @param initialText a <code>String</code> containing the initial text to
     *        load into the new <code>TextOutputPane</code>
     */
    public TextOutputPane(String initialText) {
        this(initialText, VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * Constructs a new <code>TextOutputPane</code> with the specified initial
     * text and scrollbar policies
     *
     * @param initialText a <code>String</code> containing the initial text to
     *        load into the new <code>TextOutputPane</code>
     * @param vsbPolicy the vertical scrollbar policy
     * @param hsbPolicy the horizontal scrollbar policy
     *
     * @see javax.swing.JScrollPane JScrollPane
     */
    public TextOutputPane(String initialText, int vsbPolicy, int hsbPolicy) {
        super(vsbPolicy, hsbPolicy);
        JTextArea ta = new JTextArea(initialText, 0, 0);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        setViewportView(ta);
    }

    /**
     * Sets the viewport for this <code>TextOutputPane</code>; the view of the
     * provided JViewport should be a <code>JTextArea</code> for the
     * configuration to succeed fully
     *
     * @param vp the new viewport as a <code>JViewport</code>
     */
    public void setViewport(JViewport vp) {
        stopListening();
        if (vp == null) {
            super.setViewport(null);
        } else {
            Component view = vp.getView();

            if (view == null) {
                super.setViewport(vp);
            } else if (view instanceof javax.swing.JTextArea) {
                super.setViewport(vp);
                ((JTextArea) view).setEditable(false);
                startListening((JTextArea) view);
            }
        }
    }

    /**
     * Sets the view for this <code>TextOutputPane</code>'s viewport; this
     * component only supports viewport views that are instances of JTextArea --
     * attempts to set any other kind of view are silently ignored
     *
     * @param view the <code>Component</code> to be managed inside the viewport;
     *        its runtime type should be assignable to <code>JTextArea</code>
     */
    public void setViewportView(Component view) {
        if (view instanceof javax.swing.JTextArea) {
            stopListening();
            super.setViewportView(view);
            ((JTextArea) view).setEditable(false);
            startListening((JTextArea) view);
        }
    }

    /**
     * Implementation method of the <code>DocumentListener</code> interface;
     * does nothing
     *
     * @param e a <code>DocumentEvent</code> describing the change
     */
    public void changedUpdate(DocumentEvent e) {
        // does nothing
    }

    /**
     * Implementation method of the <code>DocumentListener</code> interface;
     * scrolls the viewport to the bottom of the <code>Document</code> when
     * new text is inserted
     *
     * @param  e a <code>DocumentEvent</code> object describing the insertion
     *
     * @throws ClassCastException if the viewport's view is not castable to
     *         type JTextArea
     */
    public void insertUpdate(DocumentEvent e) {
        JViewport vp = getViewport();
        JTextArea ta = (JTextArea) vp.getView();

        try {
            int line = ta.getLineOfOffset(e.getOffset() + e.getLength() - 1);
            int unit =
                ta.getScrollableUnitIncrement(vp.getViewRect(),
                    SwingConstants.VERTICAL, 1);
            int maxY = ((line + 1) * unit) - 1;
            int viewHeight = (int) vp.getExtentSize().getHeight();

            if (viewHeight > 0) {
                vp.setViewPosition(
                    new Point(0, Math.max(0, maxY - viewHeight + 1)) );
            }
        } catch (BadLocationException ble) {
            // ignore it
        }
    }

    /**
     * Implementation method of the <code>DocumentListener</code> interface;
     * does nothing
     *
     * @param e a <code>DocumentEvent</code> describing the removal
     */
    public void removeUpdate(DocumentEvent e) {
        // does nothing
    }

    /**
     * Returns a <code>PrintWriter</code> that may be used to write to this
     * <code>TextOutputPane</code>
     *
     * @return a <code>PrintWriter</code> that may be used to write to this
     *         <code>TextOutputPane</code>, or <code>null</code> if the
     *         viewport's view is <code>null</code> or not a
     *         <code>JTextArea</code>
     */
    public PrintWriter out() {
        PrintWriter pw = null;
        try {
            JViewport vp = getViewport();
            if (vp != null) {
                JTextArea ta = (JTextArea) vp.getView();
                if (ta != null) {
                    pw = new PrintWriter(new DocumentWriter(ta.getDocument()));
                }
            }
        } catch (ClassCastException cce) {
            /* do nothing */
        }
        return pw;
    }

    /**
     * Registers this TextOutputPane as a listener on the
     * <code>JTextArea</code> presented within it (so that it can
     * automatically scroll when text is added)
     *
     * @param view the <code>JTextArea</code> to listen to for changes
     */
    protected void startListening(JTextArea view) {
        Document doc = view.getDocument();
        doc.addDocumentListener(this);
    }

    /**
     * Causes this component to stop listening for events on its internal
     * objects; in particular, makes it stop listening for document events on
     * the viewport's view's (if any) model
     */
    protected void stopListening() {
        JViewport vp = getViewport();

        if (vp != null) {
            JTextArea ta = (JTextArea) vp.getView();

            if (ta != null) {
                Document d = ta.getDocument();

                if (d != null) {
                    d.removeDocumentListener(this);
                }
            }
        }
    }

    /**
     * A <code>Writer</code> that appends anything written to it to a
     * <code>javax.swing.text.Document</code>.  All the other
     * <code>write</code> methods delegate to <code>write(String)</code>
     * because that maps best onto <code>Document</code>'s
     * <code>insertString</code> method.
     */
    protected class DocumentWriter
            extends Writer {
        /*
         * Note that it is not necessary to synchronize the write methods of
         * this writer because all writes are ultimately forced to occur on the
         * AWT event handling thread.
         */

        /**
         * a reference to the <code>Document</code> to which this
         * <code>Writer</code> writes
         */
        protected Document doc;

        /**
         * Constructs a new, closed DocumentWriter; this constructor is
         * equivalent to <code>DocumentWriter(null)</code>
         */
        public DocumentWriter() {
            this(null);
        }

        /**
         * Constructs a new <code>DocumentWriter</code> configured to write to
         * the specified <code>Document</code>
         *
         * @param doc the <code>Document</code> to which this
         *        <code>Writer</code> should write; if <code>null</code> then
         *        the <code>Writer</code> is created in the closed state
         */
        public DocumentWriter(Document doc) {
            super();
            this.doc = doc;
        }

        /**
         * Closes this <code>DocumentWriter</code>, flushing it first;
         * subsequent close() invocations have no effect
         */
        public void close() {
            synchronized (lock) {
                flush();
                doc = null;
            }
        }

        /**
         * Flushes this <code>Writer</code>; this version has no effect because
         * writes are never buffered and this <code>Writer</code> never wraps
         * another <code>Writer</code> or an <code>OutputStream</code>
         */
        public void flush() {
            // does nothing
        }

        /**
         * Writes characters from a <code>char</code> array
         *
         * @param cbuf a <code>char[]</code> containing characters to write
         *
         * @throws IOException if an I/O error ocurrs
         */
        public void write(char[] cbuf)
                throws IOException {
            write(new String(cbuf));
        }

       /**
         * Writes characters from a portion of a <code>char</code> array
         *
         * @param cbuf a <code>char[]</code> containing characters to write
         * @param off the offset of the first character to write
         * @param len the number of characters to write
         *
         * @throws IndexOutOfBoundsException if <code>off</code> +
         *         <code>len</code> is greater than the length of
         *         <code>cbuf</code>
         * @throws IOException if an I/O error ocurrs
         */
        public void write(char[] cbuf, int off, int len)
                throws IOException {
            write(new String(cbuf, off, len));
        }

        /**
         * Writes a single character, represented by the 16 low-order bits of
         * <code>c</code>.  Writing single characters with a
         * <code>DocumentWriter</code> is very inefficient because each
         * character must be wrapped in a <code>String</code> and written via
         * <code>write(String)</code>.
         *
         * @param c contains the character to be written in its 16 low-order
         *        bits
         *
         * @throws IOException if an I/O error ocurrs
         */
        public void write(int c)
                throws IOException {
            write(String.valueOf((char) c));
        }

        /**
         * Writes a <code>String</code>, forcing the actual
         * <code>Document</code> modification to occur in the AWT/Swing event
         * dispatch thread; used by all other <code>write</code> methods to
         * perform <code>Document</code> updates
         *
         * @param str the <code>String</code> to write
         *
         * @throws IOException if a checked exception is thrown during the
         *         string insertion
         */
        public void write(String str)
                throws IOException {
            if (str == null) {
                throw new NullPointerException("The input string is null");
            }
            if (SwingUtilities.isEventDispatchThread()) {
                eventWrite(str);
            } else {
                safeWrite(str);
            }
        }

        /**
         * Writes a substring of a supplied <code>String</code>
         *
         * @param str a <code>String</code> containing characters to write
         * @param off the offset of the first character to write
         * @param len the number of characters to write
         *
         * @throws IndexOutOfBoundsException if <code>off</code> +
         *         <code>len</code> is greater than the length of
         *         <code>str</code>
         * @throws IOException if an I/O error ocurrs
         */
        public void write(String str, int off, int len)
                throws IOException {
            write(str.substring(off, len));
        }

        /**
         * Updates the <code>Document</code> directly.  It is only safe to
         * invoke this method within the event dispatch thread
         *
         * @param str the <code>String</code> to write
         *
         * @throws IOException if the string insertion causes the
         *         <code>Document</code> to throw an exception
         */
        protected void eventWrite(String str)
                throws IOException {
            if (doc == null) {
                throw new IOException("Writer closed");
            }
            try {
                doc.insertString(doc.getLength(), str, null);
            } catch (BadLocationException e) {
                throw new IOException(e.getMessage());
            }
        }

        /**
         * Submits a request for the provided <code>String</code>  to be added
         * to the <code>Document</code> in the event dispatch thread
         *
         * @param str the <code>String</code> to write
         *
         * @throws IOException if any exception is thrown, or if the current
         *         thread is interrupted while waiting for the write to
         *         actually ocurr
         */
        protected void safeWrite(String str)
                throws IOException {
            DocInserter ins = new DocInserter(str);
            try {
                SwingUtilities.invokeAndWait(ins);
                if (ins.ok != 1) {
                    throw new IOException(ins.s);
                }
            } catch (InvocationTargetException ite) {
                throw new IOException(ite.getMessage());
            } catch (InterruptedException ie) {
                throw new IOException(ie.getMessage());
            }
        }

        /**
         * The DocInserter inner class encapsulates a string to be inserted
         * into the document with a status flag and the code to perform the
         * actual update so that the insert can be performed in the event
         * dispatch thread via invokeAndWait()
         */
        protected class DocInserter
                implements Runnable {

            /** A <code>String</code> containing any error message generated */
            public String msg;

            /**
             * A <code>String</code> to be inserted into the
             * <code>Document</code>
             */
            public String s;

            /**
             * a status code; -1 if insertion failed, +1 if it succeeded, 0
             * otherwise
             */
            public int ok;

            /**
             * Constructs a new <code>DocInserter</code> to insert the
             * specified <code>String</code>
             *
             * @param str the <code>String</code> to insert
             */
            public DocInserter(String str) {
                s = str;
                ok = 0;
                msg = "";
            }

            /**
             * Implementation method of the <code>Runnable</code> interface;
             * performs the insertion and sets <code>ok</code> appropriately
             */
            public void run() {
                if (doc == null) {
                    msg = new String("Writer closed");
                    ok = -1;
                } else {
                    try {
                        doc.insertString(doc.getLength(), s, null);
                        ok = 1;
                    } catch (BadLocationException e) {
                        msg = e.getMessage();
                        ok = -1;
                    }
                }
            }
        }
    }
}
