/*
 * IUMSC Reciprocal Net Project
 *
 * NumberRun.java
 *
 * Oct 31, 2006: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

class NumberRun {
    private final int start;
    private int end;
    
    public NumberRun(int start) { 
        this.start = start;
        this.end = start;
    }
    
    public boolean overlaps(NumberRun other) {
        return ((this.start <= other.end) && (other.start <= this.end)); 
    }
    
    public boolean contains(int number) {
        return ((start <= number) && (number <= end));
    }

    /**
     * Returns the starting index of this number run
     *
     * @return the starting index
     */
    public int getStart() {
        return start;
    }

    /**
     * Sets the end to the specified value
     *
     * @param end the int to set as the end
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Returns the end
    
     * @return the end as a int
     */
    public int getEnd() {
        return end;
    }
}