/*
 * Reciprocal Net project
 * 
 * CumulativeByteCount.java
 * 
 * 04-Aug-2005: midurbin wrote the first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import org.recipnet.common.controls.HtmlPageElement;

/**
 * <p>
 * A custom tag that outputs the cumulative byte count for a provided
 * {@code FileIterator}. The cumulative byte count is the total number of bytes
 * in all files over which the {@code FileIterator} iterated. The output does
 * not contain units but is always in bytes. Files with indeterminite sizes do
 * not contribute to the total count.
 * </p><p>
 * If '{@code useAggregateSize}' is set to true, the value displayed by this
 * tag is the aggregate file size for all files in the iterator. This is only
 * relevent when considering whether to eradicate one or more files as it
 * represents the total disk space that would be freed upon eradication.
 * </p>
 */
public class CumulativeByteCount extends HtmlPageElement {

    /*
     * TODO: no caching is done for the aggregate sizes; if this information is
     * ever to be displayed more than once on a page, changes should be made to
     * improve efficiency
     */

    /**
     * A required property that is set to a reference of a peer
     * {@code FileIterator}.
     */
    private FileIterator fileIterator;

    /**
     * An optional property that when set to true indicates that the file size
     * displayed by this tag should represent the aggregate size (size taken up
     * for all versions of the file in the CVS repository). This property
     * defaults to false.
     */
    private boolean useAggregateSize;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fileIterator = null;
        this.useAggregateSize = false;
    }

    /** Setter for the 'useAggregateSize' property. */
    public void setUseAggregateSize(boolean use) {
        this.useAggregateSize = use;
    }

    /** Getter for the 'useAggregateSize' property. */
    public boolean getUseAggregateSize() {
        return this.useAggregateSize;
    }

    /** Sets the 'fileIterator' property. */
    public void setFileIterator(FileIterator it) {
        this.fileIterator = it;
    }

    /** Gets the 'fileIterator' property. */
    public FileIterator getFileIterator() {
        return this.fileIterator;
    }

    /** {@inheritDoc}; this version outputs the byte count. */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        out.print(new DecimalFormat("###,###,###,###").format(
                this.useAggregateSize
                        ? fileIterator.getCumulativeAggregateByteCount()
                        : fileIterator.getCumulativeByteCount()));
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /** {@inheritDoc} */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        CumulativeByteCount src = (CumulativeByteCount) source;

        super.copyTransientPropertiesFrom(source);
        setFileIterator(src.getFileIterator());
    }
}
