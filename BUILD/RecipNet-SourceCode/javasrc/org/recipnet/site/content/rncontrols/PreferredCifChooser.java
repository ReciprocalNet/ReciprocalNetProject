/*
 * Reciprocal Net Project
 *
 * PreferredCifChooser.java
 *
 * 13-Jan-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.SampleDataFile;

/**
 * <p>
 * A custom tag that chooses a preferred CIF from among those available in the
 * innermost containing {@code MultiFilenameContext}, taking the innermost
 * {@code FileContext} into account if there is one.  The chosen CIF name, if
 * any, is thereafter available via the {@link #getCifName()} method.  Typically
 * the corresponding tag would be used by assigning it an {@code id} so that
 * it can be referred to as a scripting variable.  This tag must be nested
 * within a {@code SampleContext} and a {@code MultiFilenameContext}. 
 * </p><p>
 * This chooser looks first for a CIF named as {@code <i>localLabId<i>.cif}.
 * If there is no such file in the surrounding context but there is a context
 * file then this chooser next tries deriving a cif name by removing the last
 * extension, if any, from the context file's name, and appending ".cif".  If
 * no CIF is chosen by this point then as a last resort, this chooser falls back
 * on searching the available file names for any that end in ".cif".
 * </p>
 * 
 * @author jobollin
 * @version 1.0
 */
public class PreferredCifChooser extends HtmlPageElement {
    
    /**
     * The preferred CIF name chosen by this tag handler.  This value is
     * <em>not</em> exposed as a tag attribute, but is available via the
     * {@link #getCifName()} method.  Its value will be {@code null} until
     * it is determined during the {@code FETCHING_PHASE}.
     */
    private String cifName = null;
    
    /**
     * Returns the preferred CIF name chosen by this chooser, which will be
     * {@code null} before the {@code FETCHING_PHASE} or if there is no CIF in
     * the innermost surrounding {@code MultiFilenameContext}.  CIF files are
     * identified by the extension ".cif" (case-sensitive).
     * 
     * @return the name of the chosen CIF, as a {@code String}
     */
    public String getCifName() {
        return cifName;
    }

    /**
     * {@inheritDoc}.  This version chooses the preferred CIF name that this tag
     * will henceforth report
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SampleContext sampleContext
                = findRealAncestorWithClass(this, SampleContext.class);
        MultiFilenameContext filenameContext
                = findRealAncestorWithClass(this, MultiFilenameContext.class);
        FileContext fileContext
                = findRealAncestorWithClass(this, FileContext.class);
        
        chooseCifName(sampleContext, filenameContext, fileContext);
        
        return rc;
    }

    /**
     * Chooses the preferred CIF name that will be exposed by this tag based on
     * the specified sample, multi-filename, and file contexts, and assigns it
     * to the {@code cifName} variable
     * 
     * @param  sampleContext the surrounding {@code SampleContext}; used in
     *         searching for possible CIF names; should not be {@code null}
     * @param  filenameContext a {@code MultiFilenameContext} defining the
     *         available file names from which a suitable CIF file name may be
     *         chosen; should not be {@code null}
     * @param  fileContext a {@code FileContext} that, if non-{@code null}, is
     *         used to derive an extra CIF filename possibility to try
     */
    private void chooseCifName(SampleContext sampleContext,
            MultiFilenameContext filenameContext, FileContext fileContext) {
        if ((sampleContext == null) || (filenameContext == null)) {
            throw new IllegalStateException();
        }
        Collection<String> filenames = filenameContext.getFilenames();
        String proposedName;
        
        // First try adding ".cif" to the local lab ID 
        proposedName = sampleContext.getSampleInfo().localLabId + ".cif";
        if (filenames.contains(proposedName)) {
            cifName = proposedName;
            return;
        }
        
        /*
         * If a context file is available then try a CIF name derived from the
         * context file's name by removing the last extenstion, if any, and
         * appending ".cif"
         */ 
        if (fileContext != null) {
            SampleDataFile file = fileContext.getSampleDataFile();
            
            if (file != null) {
                proposedName = file.getName().replaceAll(
                        "(?:\\.[^.]*)?$", ".cif");
                if (filenames.contains(proposedName)) {
                    cifName = proposedName;
                    return;
                }
            }
        }
        
        /*
         * As a last resort, look through all the file names for any one that
         * ends with ".cif", and choose the first one found
         */
        for (String filename : filenames) {
            if (filename.endsWith(".cif")) {
                cifName = filename;
                return;
            }
        }
        
        // No suitable CIF name
        cifName = null;
    }
}
