/*
 * Reciprocal Net project
 * 
 * PaginationContext.java
 * 
 * 01-Nov-2004: midurbin wrote first draft
 * 15-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

/**
 * An interface for providing information about partition of numbered elements
 * over multiple pages. Methods expose page number, page size, total page count,
 * and total element count.
 */
public interface PaginationContext {

    /**
     * Provides the current page number relative to this
     * {@code PaginationContext}. By convention, page numbers start at one (not
     * zero).
     * 
     * @return the page number
     */
    public int getPageNumber();

    /**
     * Provides the current number of elements per page for this
     * {@code PaginationContext}
     * 
     * @return the number of elements per page
     */
    public int getPageSize();

    /**
     * Provides the current total number of elements; that is, the count of all
     * elements on all pages
     * 
     * @return the total number of elements
     */
    public int getElementCount();

    /**
     * Provides the total number of pages.
     * 
     * @return the number of pages given the current page size
     */
    public int getPageCount();
}
