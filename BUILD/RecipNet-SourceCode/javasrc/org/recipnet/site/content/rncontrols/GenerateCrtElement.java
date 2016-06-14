/*
 * Reciprocal Net Project
 *
 * GenerateCrtElement.java
 *
 * 09-Feb-2006: jobollin wrote first draft
 */

package org.recipnet.site.content.rncontrols;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.SuppressionContext;
import org.recipnet.common.files.CifFile;
import org.recipnet.common.files.CrtFile;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.molecule.FractionalAtom;
import org.recipnet.common.molecule.ModelBuilder;
import org.recipnet.site.wrapper.FileTracker;

/**
 * <p>
 * A phase-recognizing tag that generates a CRT file during the
 * {@code FETCHING_PHASE}, provided that this tag is not contained in a
 * SuppressionContext that is suppressing its body during that phase. When this
 * tag has generated a CRT it registers it with the application's file tracker,
 * and assigns the resulting key as the (string) value of the specified
 * {@code HtmlControl}. This tag needs to be nested within a
 * {@code CifFileContext} to establish the CIF from which CRT files will be
 * generated, and the provided CIF must contain at least one data block (only
 * the first of which is used). This tag recognizes the following attributes,
 * not all combinations of which are valid:
 * </p>
 * <dl>
 * <dt>id</dt>
 * <dd>An optional unique ID by which this tag handler can be referenced</dd>
 * <dt>keyControl</dt>
 * <dd>a required attribute, whose value is expected to be an
 * {@code HtmlControl}. If this tag handler successfully generates and tracks a
 * CRT file then the value of the control specified via {@code keyControl} is
 * updated with a {@code String} representation of the key.</dd>
 * <dt>seedSites</dt>
 * <dd>an optional attribute, whose value is expected to be a
 * {@code Collection} of {@code String} objects with no {@code null}s.
 * Specifying this attribute implies that the 'grown' style of model building is
 * requested; the elements of {@code seedSites} are taken as the labels of the
 * atom sites from which the model should be grown</dd>
 * <dt>boxSize</dt>
 * <dd>an optional attribute, whose value is expected to be a comma-,
 * semicolon-, or space-delimited list of the three relative dimensions of a
 * packing box. This attribute should be specified along with the 'boxCenter' or
 * 'boxCoordinates' attribute; it is meaningless without one of those</dd>
 * <dt>boxCenter</dt>
 * <dd>an optional attribute, whose value is expected to be a {@code String}
 * matching an atom site label from the first data block of the CIF provided by
 * the surrounding context. Specifying this attribute implies that the 'packed'
 * style of model building is requested; the atom site specified by this
 * attribute is used as the center of the packing box. The 'boxSize' attribute
 * should be specified along with this attribute.</dd>
 * <dt>boxCoordinates</dt>
 * <dd>an optional attribute, whose value is expected to be a comma-,
 * semicolon-, or space-delimited list of the three fractional coordinates of
 * the center of a packing box. Specifying this attribute implies that the
 * 'packed' style of model building is requested; the coordinates derived from
 * this attribute represent the center of the packing box. The 'boxSize'
 * attribute should be specified along with this attribute.</dd>
 * </dl>
 * <p>
 * At most one of the attributes 'seedSites', 'boxCenter', and 'boxCoordinates'
 * should be given, and behavior is undefined if more than one is specified. It
 * is valid to not specify any of them, however, which results in the 'simple'
 * style of model generation.
 * </p>
 * 
 * @author jobollin
 * @version 1.0
 */
public class GenerateCrtElement extends HtmlPageElement
        implements ErrorSupplier {
    
    /**
     * An enum of the supported types of CRT generation
     * 
     * @author jobollin
     * @version 0.9.0
     */
    private enum GenerationType {
        
        /**
         * An enum constant representing the "simple" CRT-generation style
         */
        SIMPLE {
            @Override
            public CrtFile<?> generateCrt(
                    @SuppressWarnings("unused") GenerateCrtElement element,
                    ModelBuilder builder) {
                return new CrtFile<FractionalAtom>(builder.buildSimpleModel());
            }
        },
        
        /**
         * An enum constant representing the "grown" CRT-generation style
         */
        GROWN {
            @Override
            public CrtFile<?> generateCrt(
                    GenerateCrtElement element, ModelBuilder builder) {
                return new CrtFile<FractionalAtom>(
                        builder.buildGrownModel(element.seedSites));
            }
        },
        
        /**
         * An enum constant representing the "packed" CRT-generation style, with
         * an atom site designating the center of the packing box
         */
        PACKED_AROUND_ATOM {
            @Override
            public CrtFile<?> generateCrt(
                    GenerateCrtElement element, ModelBuilder builder) {
                return new CrtFile<FractionalAtom>(builder.buildPackedModel(
                        element.parseTriple(element.getBoxSize()),
                        element.boxCenter));
            }
        },
        
        /**
         * An enum constant representing the "packed" CRT-generation style, with
         * the center of the packing box expressed in fractional coordinates
         */
        PACKED_AROUND_COORDS {
            @Override
            public CrtFile<?> generateCrt(
                    GenerateCrtElement element, ModelBuilder builder) {
                return new CrtFile<FractionalAtom>(builder.buildPackedModel(
                        element.parseTriple(element.getBoxSize()),
                        element.parseTriple(element.getBoxCoordinates())));
            }
        };

        /**
         * Implements the model generation style represented by this
         * {@code GenerationType} with use of the specified {@code ModelBuilder}
         * and data from the specified {@code GenerateCrtElement}
         * 
         * @param  element a {@code GeneratCrtElement} whose properties
         *         include the model generation details for the requested model 
         * @param  builder a {@code ModelBuilder} to use for the model
         *         generation
         *         
         * @return a {@code CrtFile&lt;?&gt;} representing the specified model
         */
        public abstract CrtFile<?> generateCrt(
                GenerateCrtElement element, ModelBuilder builder);
    }
    
    /**
     * The media type used for CRT files
     */
    public final static String CRT_MEDIA_TYPE = "text/x-recipnet-crt";
    
    /**
     * The {@code ErrorSupplier} error flag raised by this tag if the first
     * data block obtained from the context CIF is rejected by the
     * {@code ModelBuilder} constructor.
     */
    public final static int INCOMPLETE_CIF = 1 << 0;
    
    /**
     * A reference to this web application's {@code FileTracker} instance
     */
    private FileTracker fileTracker;
    
    /**
     * The model generation mode configured for this tag
     */
    private GenerationType generationType;
    
    /**
     * A required, non-transient tag attribute specifying an
     * {@code HtmlControl} on which the {@code FileTracker} key assigned to the
     * CRT generated by this tag should be set as a {@code String} value
     */
    private HtmlControl keyControl;
    
    /**
     * The optional, transient {@code seedSites} attribute of this tag, which
     * specifies the labels of the atom sites that should serve as seeds for a
     * grown-type CRT generation 
     */
    private Collection<String> seedSites;
    
    /**
     * The optional, transient {@code boxSize} attribute of this tag, which
     * specifies the fractional dimensions of a triclinic packing box as a
     * space-, comma-, or semi-colon-delimited list of three numbers
     */
    private String boxSize;
    
    /**
     * The optional, transient {@code boxCenter} attribute of this tag, which
     * specifies the center of a triclinic packing box by means of an atom site
     * label
     */
    private String boxCenter;
    
    /**
     * The optional, transient {@code boxCoordinates} attribute of this tag,
     * which specifies the fractional coordinates of the center of a triclinic
     * packing box as a space-, comma-, or semi-colon-delimited list of three
     * numbers
     */
    private String boxCoordinates;
    
    /**
     * The current error code set on this {@code ErrorSupplier}
     */
    private int errorCode;

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#reset()
     */
    @Override
    protected void reset() {
        super.reset();
        
        generationType = GenerationType.SIMPLE;
        keyControl = null;
        seedSites = null;
        boxSize = null;
        boxCenter = null;
        boxCoordinates = null;
    }

    /**
     * Returns the control designated to be assigned the file tracker key of
     * the generated CRT
     * 
     * @return the {@code HtmlControl} whose value should be set to the CRT's
     *         file tracking key (as a {@code String})
     */
    public HtmlControl getKeyControl() {
        return keyControl;
    }

    /**
     * Sets the control designated to be assigned the file tracker key of
     * the generated CRT
     * 
     * @param  keyControl the {@code HtmlControl} whose value should be set to
     *         the CRT's file tracking key (as a {@code String}); should not be
     *         {@code null}
     */
    public void setKeyControl(HtmlControl keyControl) {
        if (keyControl == null) {
            throw new NullPointerException("Null control");
        }
        this.keyControl = keyControl;
    }

    /**
     * Returns the 'boxCenter' property, a string containing the label of an
     * atom site around which the requested packing box should be centered
     * 
     * @return the {@code String} label of the atom site to use as the packing
     *         box center; may be {@code null}
     */
    public String getBoxCenter() {
        return boxCenter;
    }

    /**
     * Sets the 'boxCenter' property, which is expected to be a string
     * containing the CIF atom site label of an atom site to use as the center
     * of the requested packing box
     * 
     * @param boxCenter the {@code String} label for the box center atom site
     */
    public void setBoxCenter(String boxCenter) {
        this.boxCenter = boxCenter;
        if (boxCenter != null) {
            generationType = GenerationType.PACKED_AROUND_ATOM;
        }
    }

    /**
     * Returns the 'boxCoordinates' property;
     * 
     * @return a {@code String} containing the crystallographic coordinates of
     *         the center of the requested packing box, or {@code null} if none
     *         has been set
     */
    public String getBoxCoordinates() {
        return boxCoordinates;
    }

    /**
     * Sets the 'boxCoordinates' property, a string containing the
     * crystallographic coordinates of the center of the requested packing box
     * 
     * @param  boxCoordinates the {@code String} value for the box center;
     *         coordinates should be expressed in decimal form and delimited by
     *         spaces, commas, or semicolons
     * 
     * @throws IllegalArgumentException if the specified {@code String} does
     *         not contain exactly three space-, comma-, or semicolon-delimited
     *         substrings
     * @throws NumberFormatException if any of the three substrings cannot be
     *         parsed as a decimal number
     */
    public void setBoxCoordinates(String boxCoordinates) {
        this.boxCoordinates = boxCoordinates;
        if (boxCoordinates != null) {
            generationType = GenerationType.PACKED_AROUND_COORDS;
        }
    }

    /**
     * Returns the 'boxSize' property;
     * 
     * @return a {@code String} containing the relative dimensions of
     *         the requested packing box, or {@code null} if none has been set
     */
    public String getBoxSize() {
        return boxSize;
    }

    /**
     * Sets the 'boxSize' property, a string containing the
     * relative dimensions of the requested packing box
     * 
     * @param  boxSize the {@code String} value for the box dimensions;
     *         coordinates should be expressed in decimal form and delimited by
     *         spaces, commas, or semicolons
     * 
     * @throws IllegalArgumentException if the specified {@code String} does
     *         not contain exactly three space-, comma-, or semicolon-delimited
     *         substrings
     * @throws NumberFormatException if any of the three substrings cannot be
     *         parsed as a decimal number
     */
    public void setBoxSize(String boxSize) {
        this.boxSize = boxSize;
    }

    /**
     * Returns the value of the 'seedSites' property, a collection of the labels
     * of atom sites from which a model should be grown
     * 
     * @return a {@code Collection&lt;String&gt;} of the seed site atom labels;
     *         users should not assume object identity between this collection
     *         and any collection previously passed to
     *         {@link #setSeedSites(Collection)}, nor that the returned
     *         collection is modifiable.  May be {@code null}.
     */
    public Collection<String> getSeedSites() {
        return ((seedSites == null) ? null
                : Collections.unmodifiableCollection(seedSites));
    }

    /**
     * Sets the contents of the specified collection as the value of the
     * 'seedSites' property, a collection of the labels of atom sites from which
     * a model should be grown; no reference is retained to the argument itself
     * 
     * @param seedSites a {@code Collection&lt;String&gt;} containing the labels
     *        of the seed sites, or {@code null}
     */
    public void setSeedSites(Collection<String> seedSites) {
        if (seedSites != null) {
            if (seedSites.contains(null)) {
                throw new IllegalArgumentException("Null site label");
            } else {
                this.seedSites = new ArrayList<String>(seedSites);
                generationType = GenerationType.GROWN;
            }
        } else {
            this.seedSites = null;
        }
    }

    /**
     * {@inheritDoc}.  This version obtains the application's
     * {@code FileTracker} for later use.
     * 
     * @see HtmlPageElement#onRegistrationPhaseBeforeBody(PageContext)
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        
        fileTracker
                = FileTracker.getFileTracker(pageContext.getServletContext());
        
        return rc;
    }

    /**
     * {@inheritDoc}.  Provided that this tag is not suppressed by a surrounding
     * {@code SuppressionContext}, generates a CRT file according to the tag's
     * current properties, saves a temporary copy, and updates the configured
     * control with the file tracking key by which the new CRT can subsequently
     * be retrieved.
     * 
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        SuppressionContext sc
                = findRealAncestorWithClass(this, SuppressionContext.class);
        CifFileContext cifContext
                = findRealAncestorWithClass(this, CifFileContext.class); 
        
        if ((sc == null) || !sc.isTagsBodySuppressedThisPhase()) {
            if (cifContext == null) {
                throw new IllegalStateException("No CIF context");
            }
            CifFile cif = cifContext.getCifFile();
            Iterator<DataBlock> blocks = cif.blockIterator();
            
            if (blocks.hasNext()) {
                CrtFile<?> crt;
                        
                try {
                    crt = generationType.generateCrt(
                            this, new ModelBuilder(blocks.next()));
                } catch (IllegalArgumentException iae) {
                    setErrorFlag(INCOMPLETE_CIF);
                    return rc;
                }
                
                try {
                    File tempFile = fileTracker.createTempFile("gen", ".crt");
                    
                    try {
                        OutputStream out = new FileOutputStream(tempFile);
                        long key;
                    
                        // write the CRT data to the specified file
                        try {
                            Writer w = new OutputStreamWriter(out, "US-ASCII");
                            
                            crt.writeTo(w);
                            w.flush();
                        } finally {
                            try {
                                out.close();
                            } catch (IOException ioe) {
                                // discard it
                            }
                        }
                        
                        // track the resulting file
                        key = fileTracker.trackFile(
                                tempFile, CRT_MEDIA_TYPE, true);
                        
                        // record the tracking key in the specified control
                        getKeyControl().setValue(String.valueOf(key));
                    } catch (IOException ioe) {
                        tempFile.delete();
                        throw new JspException(ioe);
                    }
                } catch (IOException ioe) {
                    throw new JspException(ioe);
                }
            } else {
                throw new IllegalStateException("CIF has no data block");
            }
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     * 
     * @see HtmlPageElement#generateCopy(String, Map)
     */
    @Override
    protected HtmlPageElement generateCopy(String newId, Map origToCopyMap) {
        GenerateCrtElement copy
                = (GenerateCrtElement) super.generateCopy(newId, origToCopyMap);
        HtmlControl control = this.getKeyControl();
        
        /*
         * The map might not contain the referenced control as a key if it is
         * not an ancestor of this tag and it is not contained in a common
         * iterator with this tag.  In those cases, however, the reference copy
         * performed by HtmlPageElement results in the correct value, and
         * nothing need be done.
         */
        if (origToCopyMap.containsKey(control)) {
            copy.setKeyControl((HtmlControl) origToCopyMap.get(control));
        }
        copy.setSeedSites(this.getSeedSites());
        copy.setBoxSize(this.getBoxSize());
        copy.setBoxCoordinates(this.getBoxCoordinates());
        
        return copy;
    }

    /**
     * Parses a space-, comma-, or semi-colon-delimited string to extract three
     * values of type {@code double}
     * 
     * @param  s the {@code String} to parse
     * 
     * @return a {@code double[]} containing the three parsed doubles in the
     *         order ther appeared in {@code s}.
     * 
     * @throws IllegalArgumentException if the specified {@code String} cannot
     *         be split into exactly three comma-, semicolon-, or
     *         space-delimited elements
     * @throws NumberFormatException if any of the elements of the string
     *         cannot be parsed as a floating-point number in decimal format
     */
    double[] parseTriple(String s) {
        String[] substrings = s.split("(?s:\\s*[,;\\s]\\s*)", -1);
        double[] rval;
        
        if (substrings.length != 3) {
            throw new IllegalArgumentException("Wrong number of elements");
        }
        
        rval = new double[3];
        for (int i = 0; i < rval.length; i++) {
            rval[i] = Double.parseDouble(substrings[i]);
        }
        
        return rval;
    }
    
    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        errorCode |= errorFlag;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return INCOMPLETE_CIF;
    }
}
