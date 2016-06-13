/*
 * Reciprocal Net Project
 *
 * CifAtomSiteIterator.java
 *
 * 30-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.IteratorFilter;
import org.recipnet.common.controls.HtmlPage;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.HtmlPageIterator;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.AtomSiteIterator;
import org.recipnet.common.files.cif.AtomSiteRecord;

/**
 * An iterator tag that, when nested within a {@code CifFileContext}, iterates
 * over {@code AtomSiteRecords} representing the atoms in the CIF's first data
 * block.
 *  
 * @author jobollin
 * @version 1.0
 */
public class CifAtomSiteIterator extends HtmlPageIterator {
    
    /**
     * An error flag signaling that the CifFile obtained from the innermost
     * surrounding {@code CifFileContext} did not contain any data blocks
     */
    public final static int NO_DATA_BLOCK
            = HtmlPageIterator.getHighestErrorFlag() << 1;

    /**
     * The innermost {@code CifFileContext} containing this tag; this is the one
     * from which a {@code CifFile} will be obtained
     */
    private CifFileContext cifFileContext;
    
    /**
     * The {@code CifFile} obtained from the surrounding context during the
     * {@code FETCHING_PHASE}
     */
    private CifFile cifFile;
    
    /**
     * An {@code AtomSiteIterator} in use during the current phase for iterating
     * over the available atom sites
     */
    private Iterator<AtomSiteRecord> atomSites;
    
    /**
     * The {@code AtomSiteRecord} bearing the relevant atom site information for
     * this iteration of this tag's body
     */
    private AtomSiteRecord siteRecord;

    /**
     * A {@code String} representation of atom site type symbols of those atom
     * site records that should be provided by this iterator.  This is the
     * string with which the {@link #allowedTypeSymbols} {@code Collection}
     * was built. 
     */
    private String allowedTypeSymbolString;
    
    /**
     * A {@code Collection} of the type symbol {@code String}s for those atom
     * sites that should be presented by this iterator.  This optional attribute
     * takes precedence over the {@link #excludedTypeSymbols} if both are
     * specified
     */
    private Collection<String> allowedTypeSymbols;

    /**
     * A {@code String} representation of atom site type symbols of those atom
     * site records that should be excluded from those provided by this
     * iterator.  This is the string with which the {@link #excludedTypeSymbols}
     * {@code Collection} was built. 
     */
    private String excludedTypeSymbolString;
    
    /**
     * A {@code Collection} of the type symbol {@code String}s for those atom
     * sites that should be excluded by this iterator.  This optional attribute
     * is superceded by the {@link #allowedTypeSymbols} if both are specified.
     */
    private Collection<String> excludedTypeSymbols;
    
    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        cifFileContext = null;
        cifFile = null;
        atomSites = null;
        siteRecord = null;
        allowedTypeSymbolString = null;
        allowedTypeSymbols = null;
        excludedTypeSymbolString = null;
        excludedTypeSymbols = null;
    }

    /**
     * Retrieves a string representation of the list of atom type symbols of
     * those site records that should be provided by this iterator tag
     *  
     * @return the list of type symbols as a comma-delimited {@code String}
     */
    public String getAllowedTypeSymbols() {
        return allowedTypeSymbolString;
    }

    /**
     * Sets the allowedTypeSymbols property, parsing the allowed symbols from
     * the specified comma-delimited list
     * 
     * @param  allowedTypeSymbolString the {@code String} of comma-delimited
     *         type symbols of site records that should be provided by this
     *         iterator tag
     */
    public void setAllowedTypeSymbols(String allowedTypeSymbolString) {
        this.allowedTypeSymbolString = allowedTypeSymbolString;
        allowedTypeSymbols = new HashSet<String>();
        for (String symbol : allowedTypeSymbolString.split(",")) {
            String trimmedSymbol = symbol.trim();
            
            if (trimmedSymbol.length() > 0) {
                allowedTypeSymbols.add(trimmedSymbol);
            }
        }
    }

    /**
     * Retrieves a string representation of the list of atom type symbols of
     * those site records that should be excludeded by this iterator tag
     *  
     * @return the list of type symbols as a comma-delimited {@code String}
     */
    public String getExcludedTypeSymbols() {
        return excludedTypeSymbolString;
    }

    /**
     * Sets the excludedTypeSymbols property, parsing the excluded symbols from
     * the specified comma-delimited list
     * 
     * @param  excludedTypeSymbolString the {@code String} of comma-delimited
     *         type symbols of site records that should be filtered from those
     *         provided by this iterator tag
     */
    public void setExcludedTypeSymbols(String excludedTypeSymbolString) {
        this.excludedTypeSymbolString = excludedTypeSymbolString;
        excludedTypeSymbols = new HashSet<String>();
        for (String symbol : excludedTypeSymbolString.split(",")) {
            String trimmedSymbol = symbol.trim();
            
            if (trimmedSymbol.length() > 0) {
                excludedTypeSymbols.add(trimmedSymbol);
            }
        }
    }

    /**
     * Retrieves the atom site information relevant to this iteration of this
     * tag's body
     * 
     * @return the site information as an {@code AtomSiteRecord}
     */
    public AtomSiteRecord getSiteRecord() {
        return siteRecord;
    }

    /**
     * In principle, sets the atom site for the current iteration of this tag;
     * in practice, always throws an exception because the {@code siteRecord}
     * property is read-only.  This method is present only to work around
     * buggy introspectors.
     * 
     * @param  siteRecord the {@code AtomSiteRecord} to set
     * 
     * @throws IllegalStateException every time
     */
    public void setSiteRecord(
            @SuppressWarnings("unused") AtomSiteRecord siteRecord) {
        throw new IllegalStateException(
                "Cannot change the current atom site record");
    }

    protected static int getHighestErrorFlag() {
        return NO_DATA_BLOCK;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#beforeIteration()
     */
    @Override
    protected void beforeIteration() throws JspException {
        if (cifFile != null) {
            Iterator<DataBlock> blockIterator = cifFile.blockIterator();
            
            if (blockIterator.hasNext()) {
                atomSites = new SiteRecordFilter(
                        new AtomSiteIterator(blockIterator.next()));
            } else {
                setErrorFlag(NO_DATA_BLOCK);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#onIterationBeforeBody()
     */
    @Override
    protected boolean onIterationBeforeBody() {
        if ((getPage().getPhase() == HtmlPage.PARSING_PHASE)
             && (getPostedIterationCount()
                     > getIterationCountSinceThisPhaseBegan())) {
            /*
             * Evaluate the body for the benefit of nested elements,
             * even though those nested elements may not yet get the
             * AtomSiteRecord.
             */
            return true;
        } else if ((atomSites != null) && atomSites.hasNext()) {
            siteRecord = atomSites.next();
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}.  This version looks up the surrounding
     * {@code CifFileContext} and throws an exception if it doesn't find one
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        cifFileContext = findRealAncestorWithClass(this, CifFileContext.class);
        if (cifFileContext == null) {
            throw new IllegalStateException("No CifFileContext");
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version retrieves the {@code CifFile} from the
     * surrounding {@code CifFileContext}
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        cifFile = cifFileContext.getCifFile();
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageIterator#generateCopy(String, Map)
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        CifAtomSiteIterator copy
                = (CifAtomSiteIterator) super.generateCopy(newId, map);
        
        copy.cifFileContext = (CifFileContext) map.get(this.cifFileContext);
        
        if (this.allowedTypeSymbols != null) {
            copy.allowedTypeSymbols
                    = new HashSet<String>(this.allowedTypeSymbols);
        }
        if (this.excludedTypeSymbols != null) {
            copy.excludedTypeSymbols
                    = new HashSet<String>(this.excludedTypeSymbols);
        }
        
        return copy;
    }
    
    /**
     * A filtering iterator wrapper that applies the inclusion criteria for
     * atom site records to be provided by this iterator
     * 
     * @author jobollin
     * @version 1.0
     */
    private class SiteRecordFilter extends IteratorFilter<AtomSiteRecord> {

        /**
         * Initializes a {@code SiteRecordFilter} with the iterator
         * 
         * @param  source an {@code Iterator} over the site records to be
         *         filtered
         */
        public SiteRecordFilter(Iterator<? extends AtomSiteRecord> source) {
            super(source);
        }

        /**
         * {@inheritDoc}
         * 
         * @see IteratorFilter#shouldPass(Object)
         */
        @Override
        public boolean shouldPass(AtomSiteRecord record) {
            if (allowedTypeSymbols != null) {
                return allowedTypeSymbols.contains(record.getTypeSymbol());
            } else if (excludedTypeSymbols != null) {
                return !excludedTypeSymbols.contains(record.getTypeSymbol());
            } else {
                return true;
            }
        }
    }
}
