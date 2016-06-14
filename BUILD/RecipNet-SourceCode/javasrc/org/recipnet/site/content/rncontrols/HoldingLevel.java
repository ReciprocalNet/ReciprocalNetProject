/*
 * Reciprocal Net project
 * 
 * HoldingsIterator.java
 * 
 * 20-Oct-2004: midurbin wrote first draft
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A custom tag that simply displays a localized representation of the holding
 * level associated with the {@code RepositoryHoldingContext} that encloses this
 * tag.
 */
public class HoldingLevel extends HtmlPageElement {

    /**
     * The {@code RepositoryHoldingContext} in which this tag is nested. This is
     * set by {@code onFetchingPhaseBeforeBody()}.
     */
    private RepositoryHoldingContext holdingContext;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.holdingContext = null;
    }

    /**
     * {@inheritDoc}; this version looks up a reference to the innermost
     * surrounding {@code RepositoryHoldingContext}.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code RepositoryHoldingContext}.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // get RepositoryHoldingContext
        this.holdingContext = findRealAncestorWithClass(this,
                RepositoryHoldingContext.class);
        if (this.holdingContext == null) {
            throw new IllegalStateException();
        }

        return rc;
    }

    /**
     * {@inheritDoc}; this version writes the localized {@code String}
     * representation of the holding level as determined by a call to
     * {@link LanguageHelper#getHoldingLevelString(int, Enumeration, boolean)},
     * to the {@code JspWritrer}.
     * 
     * @throws IllegalStateException if {@code RepositoryHoldingInfo} is null or
     *         returns null
     * @throws JspException wrapping a {@code ResourceNotFoundException} in the
     *         event that {@code LanguageHelper} could not find a {@code String}
     *         for the given holding level
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        RepositoryHoldingInfo rhi
                = this.holdingContext.getRepositoryHoldingInfo();
        
        try {
            out.print(LanguageHelper.extract(
                    this.pageContext.getServletContext()).getHoldingLevelString(
                            rhi.replicaLevel,
                            this.pageContext.getRequest().getLocales(), true));
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }
        
        return super.onRenderingPhaseBeforeBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        HoldingLevel dc = (HoldingLevel) super.generateCopy(newId, map);
        
        dc.holdingContext
                = (RepositoryHoldingContext) map.get(this.holdingContext);
        
        return dc;
    }
}
