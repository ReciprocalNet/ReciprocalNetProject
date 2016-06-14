/*
 * Reciprocal Net Project
 *
 * @(#) WholeNumberField.java
 *
 * By John C. Bollinger, Indiana University Molecular Structure Center
 *
 * This class derived from one shipped with Sun's Java tutorial; the
 * following copyright and license pertain:
 *
 * Copyright (c) 1997, 1998, 1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 *
 * -- end of license --
 * 
 * 09-Jan-2003: jobollin added file comment, removed unused imports, and added
 *              Javadoc comments to the publicliy accessible methods
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 */

package org.recipnet.site.applet.jamm.jamm2;
import javax.swing.BoundedRangeModel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * Implements a text field constrained to contain whole numbers only. The
 * custom <code>Document</code> subclass used by this class implements the
 * <code>BoundedRangeModel</code> interface, and via that interface can be
 * constrained to accept only a subset of the whole numbers.
 *
 * @author John C. Bollinger
 */
public class WholeNumberField
        extends JTextField {

    /**
     * creates a new <code>WholeNumberField</code> with the specified number of
     * columns
     *
     * @param columns the number of columns for this field
     */
    public WholeNumberField(int columns) {
        super(columns);
    }

    /**
     * sets the current value of this field, subject to its range constraints
     *
     * @param value the new value for this field
     */
    public void setValue(int value) {
        ((BoundedRangeModel) getDocument()).setValue(value);
    }

    /**
     * returns the current value of this <code>WholeNumberField</code> as an
     * <code>int</code>
     *
     * @return the current value of this field
     */
    public int getValue() {
        return ((BoundedRangeModel) getDocument()).getValue();
    }

    /**
     * Creates a suitable <code>Document</code> for use as a model for this
     * field
     *
     * @return a <code>Document</code> suitable that can serve as a model for
     *         this field
     */
    protected Document createDefaultModel() {
        return new WholeNumberDocument();
    }

    /**
     * a <code>Document</code> that permits only text that represents a whole
     * number; moreover, this <code>Document</code> implements the
     * <code>BoundedRangeModel</code> interface whereby the allowable values
     * that the text may represent can be restricted.  The default and maximum
     * upper limit is <code>Integer.MAX_VALUE</code>.
     */
    protected class WholeNumberDocument
            extends PlainDocument
            implements BoundedRangeModel {

        /** the one <code>ChangeEvent</code> that this document throws */
        private transient ChangeEvent changeEvent;

        /**
         * A flag tracking whether the value of this document is in the
         * process of being adjusted (externally controlled)
         */
        private boolean adjusting;

        /** The maximum integer value that this field may have */
        private int maxValue;

        /** The minimum integer value that this field may have */
        private int minValue;

        /** The current integer value of this field */
        private int value;
        
        /** The current extent of this field */
        private int extent;

        /**
         * Constructs a new <code>WholeNumberDocument</code>; by default the
         * value is zero and the range parameters default to minimum 0,
         * extent 0, and maximum <code>Integer.MAX_VALUE</code>.
         */
        public WholeNumberDocument() {
            super();
            setRangeProperties(0, 0, 0, Integer.MAX_VALUE, false, true);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets the extent of this document's value.
         *
         * @param newExtent the new extent
         */
        public void setExtent(int newExtent) {
            if (newExtent < 0) {
                newExtent = 0;
            } else {
                int maxExtent = maxValue - value;
                
                if (newExtent > maxExtent) {
                    newExtent = maxExtent;
                }
            }
            setRangeProperties(value, newExtent, minValue, maxValue, adjusting,
                true);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Gets the extent of this document's value.
         *
         * @return the extent
         */
        public int getExtent() {
            return extent;
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets the maximum of this document's range.
         *
         * @param newMaximum the new maximum
         */
        public void setMaximum(int newMaximum) {
            if (newMaximum < 0) {
                newMaximum = 0;
            }
            int newMinimum = (minValue > newMaximum) ? newMaximum : minValue;
            int maxExtent = newMaximum - newMinimum;
            int newExtent = (extent > maxExtent) ? maxExtent : extent;
            int maxval = newMaximum - newExtent;
            int newValue = (value > maxval) ? maxval : value;
            setRangeProperties(newValue, newExtent, newMinimum, newMaximum,
                adjusting, true);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Gets the maximum of this document's range
         *
         * @return the maximum
         */
        public int getMaximum() {
            return maxValue;
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets the minimum of this document's range
         *
         * @param newMinimum the new minimum
         */
        public void setMinimum(int newMinimum) {
            if (newMinimum < 0) {
                newMinimum = 0;
            }
            int newMaximum = (maxValue < newMinimum) ? newMinimum : maxValue;
            int newValue = (value < newMinimum) ? newMinimum : value;
            int maxExtent = newMaximum - newValue;
            int newExtent = (extent > maxExtent) ? maxExtent : extent;
            setRangeProperties(newValue, newExtent, newMinimum, newMaximum,
                adjusting, true);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Gets the minimum of this document's range
         *
         * @return the minimum
         */
        public int getMinimum() {
            return minValue;
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets all the range properties of this document
         *
         * @param val the new value
         * @param ext the new extent
         * @param min the new minimum
         * @param max the new maximum
         * @param adj the new value of the valueIsAdjusting property
         */
        public void setRangeProperties(int val, int ext, int min, int max,
            boolean adj) {
            if (max < 0) {
                max = 0;
            }
            if (min < 0) {
                min = 0;
            } else if (min > max) {
                min = max;
            }
            if (val > max) {
                val = max;
            } else if (val < min) {
                val = min;
            }
            int maxExt = max - val;
            if (ext > maxExt) {
                ext = maxExt;
            }
            setRangeProperties(val, ext, min, max, adj, true);
        }

        /**
         * Sets new values for all this document's range properties, with a 
	 * flag that indicates whether to update the document text (if the 
	 * value was changed).  This method does not check or adjust its 
	 * arguments -- it relies on the invoking method to perform whatever 
	 * such checks are required.
         *
         * @param val the new value
         * @param ext the new extent
         * @param min the new minimum
         * @param max the new maximum
         * @param adj the new value of the valueIsAdjusting property
         * @param update <code>true</code> if the document text should be
         *        updated or <code>false</code> if it should not be
         */
        protected void setRangeProperties(int val, int ext, int min, int max,
                boolean adj, boolean update) {
            boolean changeOccurred = false;

            if (minValue != min) {
                minValue = min;
                changeOccurred = true;
            }
            if (maxValue != max) {
                maxValue = max;
                changeOccurred = true;
            }
            if (extent != ext) {
                extent = ext;
                changeOccurred = true;
            }
            if (adjusting != adj) {
                adjusting = adj;
                changeOccurred = true;
            }
            if (value != val) {
                value = val;
                if (update) {
                    setText(Integer.toString(value));
                }
                changeOccurred = true;
            }
            if (changeOccurred) {
                fireStateChanged();
            }
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets a new value for this document
         *
         * @param newValue the new value
         */
        public void setValue(int newValue) {
            setValue(newValue, true);
        }

        /**
         * Sets a new value for the model and optionally does not update the
         * document text.  This can avoid an infinite loop when the value is
         * updated as a result of the text changing.
         *
         * @param newValue the new value
         * @param update <code>true</code> to update the text,
         *        <code>false</code> to not do so
         */
        protected void setValue(int newValue, boolean update) {
            if (newValue < minValue) {
                newValue = minValue;
            } else {
                int effectiveMax = maxValue - extent;
                
                if (newValue > effectiveMax) {
                    newValue = effectiveMax;
                }
            }
            setRangeProperties(newValue, extent, minValue, maxValue, adjusting,
                    update);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Gets the current (integer) value of this document
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Sets the value of the ValueIsAdjusting property of this model
         *
         * @param b the new value
         */
        public void setValueIsAdjusting(boolean b) {
            setRangeProperties(value, extent, minValue, maxValue, b, true);
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Gets the current value of the ValueIsAdjusting property of this
         * model
         * 
         * @return the ValueIsAdjusting property
         */
        public boolean getValueIsAdjusting() {
            return adjusting;
        }

        /**
         * Implementation method of the <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Registers <code>l</code> to receive <code>ChangeEvent</code>s
         * broadcast by this document
         *
         * @param l the <code>ChangeListener</code> to register
         */
        public void addChangeListener(ChangeListener l) {
            listenerList.add(ChangeListener.class, l);
        }

        /**
         * Attempts to insert a string into this document; fails with a message
         * to <code>System.err</code> if the resulting document text would not
         * represent a whole number or if the number represented would be
         * outside the range to which this document is constrained
         *
         * @param offs the offset from the beginning of the document at which
         *        to insert <code>str</code>
         * @param str a <code>String</code> containing the text to insert
         * @param a the <code>AttributeSet</code> for the new text
         *
         * @throws BadLocationException if <code>offs</code> is negative or
         *         greater than the length of this document
         */
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {
            StringBuffer sb = new StringBuffer(getText(0, getLength()));
            
            sb.insert(offs, str);
            try {
                int newValue = Integer.parseInt(sb.toString());
                
                if ((newValue < minValue) || (newValue > maxValue)) {
                    System.err.println("insertString: " + newValue
                        + " is out of range");
                } else {
                    setValue(newValue, false);
                    super.insertString(offs, str, a);
                }
            } catch (NumberFormatException nfe) {
                /*
                 * Control reaches here if the string resulting from this
                 * insertion would not be a whole number
                 */
                System.err.println("insertString: " + nfe);
            }
        }

        /**
         * Removes some or all of the content of this document
         *
         * @param offs the offset of the beginning of the range to remove
         * @param len the length of the range to remove
         *
         * @throws BadLocationException if <code>offs</code> is negative or
         *         greater than the length of this document, or if
         *         <code>code + len</code> is greater than the length of this
         *         document
         */
        public void remove(int offs, int len)
                throws BadLocationException {
            super.remove(offs, len);
            String s = getText(0, getLength());
            setValue(((s.length() > 0) ? Integer.parseInt(s) : 0), false);
        }

        /**
         * Implementation method of <code>BoundedRangeModel</code> 
	 * interface. <p>
	 *
         * Removes <code>l</code> as a <code>ChangeListener</code> on this
         * model
         *
         * @param l the <code>ChangeListener</code> to add
         */
        public void removeChangeListener(ChangeListener l) {
            listenerList.remove(ChangeListener.class, l);
        }

        /**
         * Broadcasts <code>ChangeEvent</code>s to registered listeners
         */
        protected void fireStateChanged() {
            Object[] listeners = listenerList.getListenerList();
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ChangeListener.class) {
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
                }
            }
        }
    }
}
