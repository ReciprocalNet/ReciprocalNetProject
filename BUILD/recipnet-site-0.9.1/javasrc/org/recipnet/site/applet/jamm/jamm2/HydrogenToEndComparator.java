/*
 * Reciprocal Net Project
 * 
 * HydrogenToEndComparator.java
 * 
 * xx-xxx-1999: jobollin wrote first draft
 * 27-Feb-2003: jobollin reformatted the source and revised the javadoc
 *              comments as part of task #749
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.jamm.jamm2 package
 *              to org.recipnet.site.applet.jamm.jamm2
 * 23-Jun-2006: jobollin removed the equals() method instead of providing a
 *              hashCode() method consistent with it; reformatted the source
 */

package org.recipnet.site.applet.jamm.jamm2;

import java.util.Comparator;

/**
 * A <code>Comparator</code> for sorting hydrogen-containing bonds to the end
 * of a bond list. Each instance of this comparator is specific to the bonds
 * among a specific set of atoms, and depends on type information provided at
 * construction time to perform its comparisons.
 */
final class HydrogenToEndComparator implements Comparator {

    /**
     * The atom types corresponding to the atoms referenced by the connection
     * array within which to compare
     */
    protected int[] types;

    /**
     * Constructs a new <code>HydrogenToEndComparator</code> based on the
     * provided atom types array
     * 
     * @param t the atom types array corresponding to the atoms referenced by
     *        the array to sort
     */
    public HydrogenToEndComparator(int[] t) {
        types = t;
    }

    /**
     * Implementation method of the <code>Comparator</code> interface.
     * <p>
     * Compares <code>o1</code> and <code>o2</code> according to this
     * comparator's criteria
     * 
     * @return an <code>int</code> less than, equal to, or greater than zero
     *         depending on whether <code>o1</code> is less than, equal to, or
     *         greater than <code>o2</code>, respectively
     * @throws ClassCastException if <code>o1</code> and / or <code>o2</code>
     *         is not suitable for comparison with this comparator
     */
    public int compare(Object o1, Object o2) {
        int[] a1 = (int[]) o1;
        int[] a2 = (int[]) o2;
        
        if ((a1.length < 2) || (a2.length < 2)) {
            throw new ClassCastException(
                    "Arguments to HydrogenToEndComparator.compare() must be "
                    + "arrays of length >= 2.");
        }
        try {
            int a1H = (containsHydrogen(a1) ? 1 : 0);
            int a2H = (containsHydrogen(a2) ? 1 : 0);
            
            return a1H - a2H;
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new ClassCastException(
                    "Argument indicates a point of unknown type. ("
                            + aioobe.getMessage() + ")");
        }
    }

    /**
     * Determines whether array <code>a</code> contains the index of a
     * hydrogen atom in position 0 or 1
     * 
     * @param a an <code>int[]</code> of length >= 2 to check for the index of
     *        a hydrogen atom
     * @return <code>true</code> if either of the first two positions of
     *         <code>a</code> is the index of a hydrogen atom (as determined
     *         by reference to <code>types</code>) or if <code>a[0] &lt;
     *         0</code>
     *         (indicating an unused array slot); otherwise <code>false</code>
     */
    private boolean containsHydrogen(int[] a) {
        return ((a[0] < 0) || (types[a[1]] == 1) || (types[a[0]] == 1));
    }

    /**
     * Returns the number of leading elements of <code>a</code> that do not
     * contain hydrogen atoms, with reference to this comparator's internal atom
     * type list
     * 
     * @param a the <code>int[][]</code> to test
     * @return the number of leading elements of <code>a</code> that do not
     *         reference hydrogen atoms
     */
    public int numNonHCon(int[][] a) {
        int rval = 0;
        
        for (int i = 0; i < a.length; i++) {
            if (containsHydrogen(a[i])) {
                break;
            }
            rval++;
        }
        
        return rval;
    }
}
