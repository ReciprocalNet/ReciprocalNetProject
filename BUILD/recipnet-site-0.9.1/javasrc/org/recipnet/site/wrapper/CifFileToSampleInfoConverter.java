/*
 * Reciprocal Net Project
 *
 * CifFileToSampleInfoConverter.java
 *
 * 27-Oct-2005: jobollin wrote first draft
 * 27-Feb-2006: jobollin fixed bug #1735 in update()
 * 27-Feb-2006: jobollin fixed bug #1736 in update()
 */

package org.recipnet.site.wrapper;

import java.util.Collection;
import java.util.Iterator;

import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.CifFileUtil;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * <p>
 * A {@code DataConverter} implementation that converts data from CIF format
 * (core CIF, not mmCIF) to {@code SampleInfo} format.  At user option, this
 * version also fixes up SHELXL's occasional insertion of a quoted question mark
 * ('?') into an output CIF where it really means an unquoted one (?).
 * Specific data converted include:
 * </p>
 * <table>
 * <tr><th>CIF Item</th><th>Reciprocal Net Item</th><th>Notes</th></tr>
 * <tr><td>Data block name</td><td>SampleInfo.localLabId</td></tr>
 * <tr><td>_cell_length_a</td><td>SampleDataInfo.a</td></tr>
 * <tr><td>_cell_length_b</td><td>SampleDataInfo.b</td></tr>
 * <tr><td>_cell_length_c</td><td>SampleDataInfo.c</td></tr>
 * <tr><td>_cell_angle_alpha</td><td>SampleDataInfo.alpha</td></tr>
 * <tr><td>_cell_angle_beta</td><td>SampleDataInfo.beta</td></tr>
 * <tr><td>_cell_angle_gamma</td><td>SampleDataInfo.gamma</td></tr>
 * <tr><td>_space_group_name_H-M_alt (preferred) or<br/>
 *         _symmetry_space_group_H-M</td><td>SampleDataInfo.spgp</td></tr>
 * <tr><td>_exptl_crystal_density_diffrn</td><td>SampleDataInfo.dcalc</td></tr>
 * <tr><td>_exptl_crystal_colour</td><td>SampleDataInfo.color</td></tr>
 * <tr><td>_cell_formula_units_Z</td><td>SampleDataInfo.z</td></tr>
 * <tr><td>_diffrn_ambient_temperature</td><td>SampleDataInfo.t</td>
 *     <td>Conversion from Kelvin to Celsius is required</td></tr>
 * <tr><td>_cell_volume</td><td>SampleDataInfo.v</td></tr>
 * <tr><td>_chemical_formula_weight</td>
 *     <td>SampleDataInfo.formulaWeight</td></tr>
 * <tr><td>_refine_ls_goodness_of_fit_ref (preferred) or<br/>
 *         _refine_ls_goodness_of_fit_all</td>
 *     <td>SampleDataInfo.goof</td></tr>
 * <tr><td>_refine_ls_r_factor_gt (most preferred), or<br/>
 *         _refine_ls_r_factor_obs (preferred), or<br/>
 *         _refine_ls_r_factor_all</td>
 *     <td>SampleDataInfo.rf</td></tr>
 * <tr><td>_refine_ls_wR_factor_ref (most preferred), or<br/>
 *         _refine_ls_wR_factor_all (preferred), or<br/>
 *         _refine_ls_wR_factor_gt, or<br/>
 *         _refine_ls_wR_factor_obs (least preferred),<br/>
 *         together with _refine_ls_structure_factor_coef</td>
 *     <td>SampleDataInfo.rwf or SampleDataInfo.rwf2</td>
 *     <td>Which field is populated, if either, depends on the structure
 *         coefficient specified in the CIF</td></tr>
 * <tr><td>_refine_ls_R_Fsqd_factor</td><td>SampleDataInfo.rf2</td>
 *     <td>This data item not provided in SHELX-produced CIFs</td></tr>
 * <tr><td>_chemical_formula_sum</td><td>attribute (EMPIRICAL_FORMULA)</td></tr>
 * <tr><td>_chemical_formula_structural</td>
 *     <td>attribute (STRUCTURAL_FORMULA)</td></tr>
 * <tr><td>_chemical_formula_moiety</td><td>attribute (MOIETY_FORMULA)</td></tr>
 * <tr><td>_chemical_name_common</td><td>attribute (COMMON_NAME)</td></tr>
 * <tr><td>_chemical_name_systematic</td><td>annotation (IUPAC_NAME)</td></tr>
 * </table>
 * <p>
 * Users may be interested in the data definitions in the
 * <a href="http://www.iucr.org/iucr-top/cif/cifdic_html/1/cif_core.dic/index.html"
 * >Core CIF Dictionary</a>
 * </p><p>
 * Note: This class simply transfers raw CIF data into a SampleInfo object
 * without any kind of validation or reformatting.  Thus there are some
 * SampleDataInfo fields that this class loads with data from the CIF, but that
 * are normally computed by the site software when sample data are entered
 * through the webapp's forms (<i>e.g.</i> {@code SampleDataInfo.v}).
 * Furthermore, although the webapp may coerce some {@code String}-valued fields
 * into approved CIF formats (<i>i.e.</i> chemical formulae), this class makes
 * no attempt to do the same.  This approach aims to keep the class focused,
 * reduces duplicate code, and support comparison of the actual CIF content with
 * internal Reciprocal Net data.  On the other hand, a {@code SampleInfo} object
 * created or updated by an instance of this class may not be suitable for
 * immediate submission to {@code SampleManager}; it first should be submitted
 * to {@link SampleWorkflowBL#alterSampleForWorkflowAction(SampleInfo, int)}
 * (with an appropriate action code), just as with any other {@code SampleInfo}
 * created or modified in the course of a workflow action.
 * </p><p>
 * Thread Safety: Instances of this class are technically not thread safe
 * because of their option to fix up SHELX unknown values.  An instance may
 * safely be shared among threads without synchronication, however, provided
 * that <strong>no</strong> thread invokes its
 * {@link #setFixingShelxUnknownValues(boolean)} method.  There are other
 * ways of ensuring that the effect of invoking that method is appropriately
 * observed by all threads to which the affected instance is visible without
 * synchronization, but that is well beyond the scope of these comments.
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CifFileToSampleInfoConverter {
    
    /**
     * A flag indicating whether CIF string values of the form '?' are
     * interpreted as bare question marks (?), the CIF placeholder for an
     * unknown value. The SHELXL structure refinement program sometimes inserts
     * the first where it means the second; while this flag is {@code true},
     * all occurences of '?' or "?" as a string value will be interpreted as the
     * unknown value
     */
    private boolean fixingShelxUnknownValues;

    /*
     * TODO: it would be cleaner and more forward-looking to use a CIF
     * dictionary to find synonyms / alternative data items where necessary,
     * instead of hard-coding alternatives
     */

    /**
     * Creates a new {@code SampleInfo} based on data from the provided
     * {@code CifFile.DataBlock}. Users should be aware that some very important
     * fields of the new {@code SampleInfo} are not populated by this method --
     * including, but not limited to, the sample, lab, and provider IDs.
     * 
     * @param  source the {@code CifFile.DataBlock} from which sample
     *         information should be obtained; should not be {@code null}
     *          
     * @return a new {@code SampleInfo} configured with data from the specified
     *         CIF data block
     * 
     * @throws IllegalArgumentException if the source object, though of the
     *         correct type, does not contain enough information to create a new
     *         object of the target type
     */
    public SampleInfo convert(DataBlock source) {
        SampleInfo info = new SampleInfo();

        update(source, info);

        return info;
    }

    /**
     * Transfers information from the provided {@code CifFile.DataBlock} to the
     * provided {@code SampleInfo}.  Users should note that this converter
     * distinguishes between items absent from the CIF on one hand and items
     * present but having an explicitly unknown or not applicable placeholder
     * value on the other: when the CIF item corresponding to a particular
     * {@code SampleInfo} field is missing then that field is left unchanged,
     * but if a placeholder value is present in the CIF then that field is set
     * to the appropriate invalid value placeholder in the {@code SampleInfo}. 
     * 
     * @param  dataBlock the {@code CifFile.DataBlock} from which sample
     *         information should be obtained; should not be {@code null}
     * @param  sample the {@code SampleInfo} to which data from the CIF should
     *         be transferred 
     */
    public void update(DataBlock dataBlock, SampleInfo sample) {
        SampleDataInfo data = sample.dataInfo;
        CifValue value;

        sample.localLabId = dataBlock.getName();

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_length_a");
        if (value != null) {
            data.a = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_length_b");
        if (value != null) {
            data.b = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_length_c");
        if (value != null) {
            data.c = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_angle_alpha");
        if (value != null) {
            data.alpha = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_angle_beta");
        if (value != null) {
            data.beta = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_angle_gamma");
        if (value != null) {
            data.gamma = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_space_group_name_H-M_alt", "_symmetry_space_group_name_H-M");
        if (value != null) {
            data.spgp = CifFileUtil.getCifString(value, null,
                    fixingShelxUnknownValues);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_exptl_crystal_density_diffrn");
        if (value != null) {
            data.dcalc = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_exptl_crystal_colour");
        if (value != null) {
            data.color = CifFileUtil.getCifString(value, null,
                    fixingShelxUnknownValues);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_formula_units_Z");
        if (value != null) {
            data.z = CifFileUtil.getCifInt(value,
                    SampleDataInfo.INVALID_INT_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_diffrn_ambient_temperature");
        if (value != null) {
            data.t = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);

            if (Double.compare(data.t, SampleDataInfo.INVALID_DOUBLE_VALUE)
                    != 0) {
                data.t -= 273.0;
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_cell_volume");
        if (value != null) {
            data.v = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_chemical_formula_weight");
        if (value != null) {
            data.formulaWeight = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_refine_ls_goodness_of_fit_ref",
                "_refine_ls_goodness_of_fit_all");
        if (value != null) {
            data.goof = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_refine_ls_r_factor_gt",
                "_refine_ls_r_factor_obs", "_refine_ls_r_factor_all");
        if (value != null) {
            data.rf = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_refine_ls_wR_factor_ref", "_refine_ls_wR_factor_all",
                "_refine_ls_wR_factor_gt", "_refine_ls_wR_factor_obs");
        if (value != null) {
            double r = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);

            // Need to determine which datainfo field applies
            value = CifFileUtil.lookupCifValue(dataBlock,
                    "_refine_ls_structure_factor_coef");
            if (value != null) {
                String coef = CifFileUtil.getCifString(value, null,
                        fixingShelxUnknownValues);

                if ("F".equals(coef) || "Inet".equals(coef)) {
                    data.rwf = r;
                } else if ("Fsqd".equals(coef)) {
                    data.rwf2 = r;
                }
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_refine_ls_R_Fsqd_factor");
        if (value != null) {
            data.rf2 = CifFileUtil.getCifDouble(value,
                    SampleDataInfo.INVALID_DOUBLE_VALUE);
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_chemical_formula_sum");
        if (value != null) {
            String s = CifFileUtil.getCifString(value, "",
                    fixingShelxUnknownValues).trim();

            if (s.length() > 0) {
                addOrReplaceText(sample.attributeInfo,
                        new SampleAttributeInfo(
                                SampleTextBL.EMPIRICAL_FORMULA, s));
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_chemical_formula_structural");
        if (value != null) {
            String s = CifFileUtil.getCifString(value, "",
                    fixingShelxUnknownValues).trim();

            if (s.length() > 0) {
                addOrReplaceText(sample.attributeInfo,
                        new SampleAttributeInfo(
                                SampleTextBL.STRUCTURAL_FORMULA, s));
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_chemical_formula_moiety");
        if (value != null) {
            String s = CifFileUtil.getCifString(value, "",
                    fixingShelxUnknownValues).trim();

            if (s.length() > 0) {
                addOrReplaceText(sample.attributeInfo,
                        new SampleAttributeInfo(
                                SampleTextBL.MOIETY_FORMULA, s));
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock, "_chemical_name_common");
        if (value != null) {
            String s = CifFileUtil.getCifString(value, "",
                    fixingShelxUnknownValues).trim();

            if (s.length() > 0) {
                addOrReplaceText(sample.attributeInfo,
                        new SampleAttributeInfo(
                                SampleTextBL.COMMON_NAME, s));
            }
        }

        value = CifFileUtil.lookupCifValue(dataBlock,
                "_chemical_name_systematic");
        if (value != null) {
            String s = CifFileUtil.getCifString(value, "",
                    fixingShelxUnknownValues).trim();

            if (s.length() > 0) {
                addOrReplaceText(sample.annotationInfo,
                        new SampleAnnotationInfo(
                                SampleTextBL.IUPAC_NAME, s));
            }
        }
    }
    
    /**
     * Adds the specified {@code SampleTextInfo} to the specified, collection
     * of such, replacing a {@code SampleTextInfo} of the same type code if one
     * already exists in the collection.  At most one existing
     * {@code SampleTextInfo} will be removed. 
     *  
     * @param  <T> the specific type of {@code SampleTextInfo} supported by the
     *         provided collection
     * @param  sampleText the {@code Collection} of the specified type of
     *         {@code SampleTextInfo} to which the specified info should be
     *         added 
     * @param  textInfo the {@code SampleTextInfo} to add to the specified
     *         collection
     */
    private <T extends SampleTextInfo> void addOrReplaceText(
            Collection<T> sampleText, T textInfo) {
        for (Iterator<T> textIt = sampleText.iterator(); textIt.hasNext(); ) {
            T info = textIt.next();
            
            if (info.type == textInfo.type) {
                textIt.remove();
                break;
            }
        }
        
        sampleText.add(textInfo);
    }

    /**
     * Returns the current state of the {@code fixingShelxUnknownValues} flag
     * 
     * @return {@code true} if the {@code fixingShelxUnknownValues} flag is
     *         enabled, {@code false} if it is disabled
     */
    public boolean isFixingShelxUnknownValues() {
        return fixingShelxUnknownValues;
    }

    /**
     * Sets the boolean value of the {@code fixingShelxUnknownValues} flag
     * 
     * @param fixingShelxUnknownValues the {@code true} to enable the flag
     *            {@code false} to disable it
     */
    public void setFixingShelxUnknownValues(boolean fixingShelxUnknownValues) {
        this.fixingShelxUnknownValues = fixingShelxUnknownValues;
    }
}
