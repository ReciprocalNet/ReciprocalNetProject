/*
 * Reciprocal Net Project
 *
 * StringBean.java
 *
 * 13-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.wrapper;

/**
 * A simple JavaBean that serves as a holder for a {@code String}
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class StringBean {
    
    /**
     * The {@code String} value stored in this bean
     */
    private String stringValue = null;

    /**
     * Returns the {@code string} property of this bean
     * 
     * @return the {@code string} property of this bean (a {@code String})
     */
    public String getString() {
        return stringValue;
    }

    /**
     * Sets the {@code string} property of this bean
     * 
     * @param  stringValue the {@code String} to set as the value of the
     *         {@code string} property
     */
    public void setString(String stringValue) {
        this.stringValue = stringValue;
    }
    
    /**
     * Returns a string representation of this {@code StringBean}
     * 
     * @return the {@code String} value of this bean, which is the value of the
     *         {@code string} property if that is non-{@code null}, or the
     *         string "null" otherwise
     */
    @Override
    public String toString() {
        return (stringValue == null) ? "null" : stringValue;
    }
}
