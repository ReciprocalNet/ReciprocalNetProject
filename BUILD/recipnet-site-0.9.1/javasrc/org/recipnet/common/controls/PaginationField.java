/*
 * Reciprocal Net project
 * 
 * PaginationField.java
 * 
 * 02-Nov-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 22-Jun-2005: midurbin added PAGE_SIZE
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import java.io.IOException;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A custom tag to expose various bits of information that are managed by the
 * {@code PaginationContext}.
 */
public class PaginationField extends HtmlPageElement {

    /**
     * A possible field code to indicate that the total element count should be
     * displayed by this tag.
     */
    public static final int TOTAL_ELEMENT_COUNT = 1;

    /**
     * A possible field code to indicate that the total page count should be
     * displayed by this tag.
     */
    public static final int TOTAL_PAGE_COUNT = 2;

    /**
     * A possible field code to indicate that the current page number should be
     * displayed by this tag.
     */
    public static final int CURRENT_PAGE_NUMBER = 3;

    /**
     * A possible field code to indicate that a listbox containing choices for
     * the page size should be displayed. This control will have an id set to
     * the value of the page size parameter as defined by
     * {@code PaginationContext} and therefore only ONE {@code PaginationField}
     * with this fieldCode should be included on a given page.
     */
    public static final int RESIZE_PAGE_SELECTOR = 4;

    /**
     * A possible field code to indicate that the current page size should be
     * displayed by this tag.
     */
    public static final int PAGE_SIZE = 5;

    /**
     * The {@code PaginationContext} that most immediately encloses this tag.
     * This reference is set by {@code onRegistrationPhaseBeforeBody()} and used
     * during the rendering phase to get values for display.
     */
    private PaginationContext paginationContext;

    /**
     * An optional propety that defaults to {@code TOTAL_ELEMENT_COUNT} and
     * indicates which piece of data maintained by the {@code PaginationContext}
     * is exposed by this tag.
     */
    private int fieldCode;

    /**
     * An 'owned' element that's used when 'fieldCode' is
     * {@code RESIZE_PAGE_SELECTOR}. In all other cases this is null and serves
     * no purpose. When it is used, it is initialized and registered by
     * {@code onRegistrationPhaseBeforeBody()} and given an id value equal to
     * {@code PaginationContext.PAGE_SIZE_PARAM_NAME} so that it interacts
     * indirectly with the {@code PaginationContext} implementation.
     */
    private ListboxHtmlControl listbox;

    /**
     * An optional property that defaults to "pageSize" and indicates the name
     * of the request parameter that contains the page size. This should be set
     * to be consistent with other controls, possibly on other pages.
     */
    private String pageSizeParamName;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.paginationContext = null;
        this.fieldCode = TOTAL_ELEMENT_COUNT;
        this.listbox = null;
        this.pageSizeParamName = "pageSize";
    }

    /**
     * @param fieldCode one of the field codes defined by this class
     * @throws IllegalArgumentException if an invalid field code is provided
     */
    public void setFieldCode(int fieldCode) {
        switch (fieldCode) {
            case TOTAL_ELEMENT_COUNT:
            case TOTAL_PAGE_COUNT:
            case CURRENT_PAGE_NUMBER:
            case RESIZE_PAGE_SELECTOR:
            case PAGE_SIZE:
                break;
            default:
                throw new IllegalArgumentException();
        }
        this.fieldCode = fieldCode;
    }

    /**
     * @param name the name of the request parameter that should be used for the
     *        page size
     */
    public void setPageSizeParamName(String name) {
        this.pageSizeParamName = name;
    }

    /**
     * @return the name of the request parameter that should be used for the
     *         page size
     */
    public String getPageSizeParamName() {
        return this.pageSizeParamName;
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation gets the
     * {@code PaginationContext} and if {@code fieldCode} is set to
     * {@code RESIZE_PAGE_SELECTOR}, it initializes {@code listbox} as an owned
     * element.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code PaginationContext}
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get the PaginationContext
        this.paginationContext = findRealAncestorWithClass(this,
                PaginationContext.class);
        if (this.paginationContext == null) {
            throw new IllegalStateException();
        }

        if (this.fieldCode == RESIZE_PAGE_SELECTOR) {
            this.listbox = new ListboxHtmlControl();
            this.listbox.setId(this.pageSizeParamName);
            registerOwnedElement(this.listbox);
            this.listbox.setFailedValidationHtml("");
            this.listbox.addOption(true, "5", "5");
            this.listbox.addOption(true, "10", "10");
            this.listbox.addOption(true, "25", "25");
            this.listbox.addOption(true, "50", "50");
            this.listbox.addOption(true, "100", "100");
            this.listbox.addOption(true, "500", "500");
            /*
             * the options must be added before setInitialValueFrom() is invoked
             * or it will be considered an invalid value
             */
            this.listbox.setInitialValueFrom(this.pageSizeParamName);
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version writes the information specified by
     * {@code fieldCode} to the {@code JspWriter}.
     * 
     * @throws IllegalStateException if {@code fieldCode} is not a known field
     *         code
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        switch (this.fieldCode) {
            case TOTAL_ELEMENT_COUNT:
                out.print(String.valueOf(
                        this.paginationContext.getElementCount()));
                break;
            case TOTAL_PAGE_COUNT:
                out.print(String.valueOf(
                        this.paginationContext.getPageCount()));
                break;
            case CURRENT_PAGE_NUMBER:
                out.print(String.valueOf(
                        this.paginationContext.getPageNumber()));
                break;
            case RESIZE_PAGE_SELECTOR:
                // do nothing, but know that the owned control will be rendered
                break;
            case PAGE_SIZE:
                out.print(String.valueOf(this.paginationContext.getPageSize()));
                break;
            default:
                throw new IllegalStateException();
        }

        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        PaginationField dc = (PaginationField) super.generateCopy(newId, map);

        // update references to 'owned' elements
        dc.listbox = (ListboxHtmlControl) map.get(this.listbox);

        // update references to contexts
        dc.paginationContext
                = (PaginationContext) map.get(this.paginationContext);

        return dc;
    }
}
