/*
 * Reciprocal Net project
 * 
 * SampleDataInfo.java
 *
 * 30-May-2002: leqian wrote skeleton
 * 06-Jun-2002: ekoperda added comments
 * 25-Jun-2002: ekoperda added serialization code
 * 26-Jun-2002: ekoperda added db access code
 * 30-Aug-2002: ekoperda added formulaWeight field and supporting code
 * 03-Sep-2002: jobollin added calculateVolume and calculateDensity as part of
 *              task #301
 * 10-Nov-2002: nisheth added XML serialization code
 * 14-Apr-2003: ekoperda added field code constants
 * 25-Apr-2003: ekoperda added getFieldsMap()
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.db; also changed package references
 *              to match source tree reorganization
 * 03-Jun-2004: midurbin added isDataField(), getField() and setField()
 * 07-Jun-2004: midurbin changed values of field code constants so that they
 *              could be more sensibly ordered in ResourceBundle property files
 * 24-Aug-2004: midurbin modified setField() and getField() to handle null
 *              values for double and int fields
 * 13-Dec-2004: eisiorho moved calculateDensity() and calculateFormulaWeight()
 *              to reflect new class SampleMathBL
 * 23-Nov-2004: eisiorho added field 'originalSampleHistoryId'
 * 06-Oct-2005: midurbin added field 'providerId'
 * 26-Oct-2005: jobollin modified insertIntoDom() to have the 'providerId' be
 *              output first
 * 27-Oct-2005: jobollin updated clone() to declare that it returns
 *              SampleDataInfo, to remove unnecessary String duplication, and
 *              to avoid having to declare that it throws
 *              CloneNotSupportedException; also added type parameters to the
 *              return value of getFieldsMap() and switched to autoboxing the
 *              keys inserted into it
 * 31-May-2006: jobollin added a hashCode() method, moved the MOLE and
 *              ANG3_TO_CM3 constants to SampleMathBL, and performed minor
 *              cleanup
 */

package org.recipnet.site.shared.db;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.recipnet.site.UnexpectedExceptionException;
import org.recipnet.site.shared.DomTreeParticipant;
import org.recipnet.site.shared.DomUtil;
import org.recipnet.site.shared.db.ProviderInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * SampleDataInfo is a container class that maps very cleanly onto
 * the database table named 'sampleData'.
 */
public class SampleDataInfo implements Cloneable, DomTreeParticipant,
        Serializable {
    /**
     *  These constants are used when the correspondent double value of
     *  a, b, c, alpha, beta, gamma, dcalc, z, t, v, rf, rwf,
     *  rf2, rwf2 and goof in the table is of value null.
     */
    public static final double INVALID_DOUBLE_VALUE = Double.NaN;
    public static final int INVALID_INT_VALUE = -1;

    /**
     * Field code constants for fields stored within this container.  Constant
     * values must not conflict with attributes/annotation codes defined on
     * SampleTextInfo or with the localtracking range.
     */
    public static final int A_FIELD = 50;
    public static final int B_FIELD = 51;
    public static final int C_FIELD = 52;
    public static final int ALPHA_FIELD = 53;
    public static final int BETA_FIELD = 54;
    public static final int GAMMA_FIELD = 55;
    public static final int SPGP_FIELD = 56;
    public static final int DCALC_FIELD = 57;
    public static final int COLOR_FIELD = 58;
    public static final int Z_FIELD = 59;
    public static final int T_FIELD = 60;
    public static final int V_FIELD = 61;
    public static final int RF_FIELD = 62;
    public static final int RWF_FIELD = 63;
    public static final int RF2_FIELD = 64;
    public static final int RWF2_FIELD = 65;
    public static final int GOOF_FIELD = 66;
    public static final int SUMMARY_FIELD = 67;
    public static final int FORMULAWEIGHT_FIELD = 68;
    public static final int PROVIDER_ID_FIELD = 69;

    /**
     *  one dimension (length/width/height) of the core unit of the
     *  sample, in angstroms. If its value is null in the table,
     *  its value in class should be INVALID_DOUBLE_VALUE. This rule also
     *  applies to all double values below: b, c, alpha, beta, gamm,
     *  dcalc, z, t, v, rf, rwf, rf2, rwf2 and goof.
     */
    public double a;

    /** The second dimension of the sample's cell */
    public double b;

    /** The third dimension of the sample's cell */
    public double c;

    /** The angle between measurement axes b and c, in radians */
    public double alpha;

    /** The angle between measurement axes a and c, in radians */
    public double beta;

    /** The angle between measurement axes a and b, in radians */
    public double gamma;

    /**
     * "Space group" is a short string code that describes how the sample's
     * unit cell will tessellate.
     */
    public String spgp;

    /**
     * calculated density of the sample in g/cm3; can be computed from a, b,
     * c, alpha, beta, gamma, and the number/type of atoms present in a
     * unit cell
     */
    public double dcalc;

    /** a text string describing any coloration of the sample */
    public String color;

    /** the number of molecules present in each unit cell. */
    public int z;

    /**
     * ambient temperature at which the data was collected, in degrees
     * Celsius
     */
    public double t;

    /**
     * volume of the sample's unit cell, in cubic angstroms.  This can be
     * determined from a, b, c, alpha, beta, and gamma.
     */
    public double v;

    /** Agreement index */
    public double rf;

    /** another agreement index */
    public double rwf;

    /** another agreement index */
    public double rf2;

    /** another agreement index */
    public double rwf2;

    /** goodness of fit, a statistical measurement */
    public double goof;

    /** A textual summary of the sample's physical properties */
    public String summary;

    /**
     * total molecular weight (in terms of H) of one unit of the empirical
     * formula for the unit cell.
     */
    public double formulaWeight;

    /** the id for the 'originating provider' */
    public int providerId;
    
    /**
     * May be used to check whether a fieldCode references a field within
     * <code>SampleDataInfo</code>.
     * @param fieldCode an integer known to reference a field within a
     *     <code>SampleInfo</code> object or one of its members.
     * @return true if the provided fieldCode references a valid field
     */
    public static boolean isDataField(int fieldCode) {
        switch (fieldCode) {
            case SampleDataInfo.A_FIELD:
            case SampleDataInfo.B_FIELD:
            case SampleDataInfo.C_FIELD:
            case SampleDataInfo.ALPHA_FIELD:
            case SampleDataInfo.BETA_FIELD:
            case SampleDataInfo.GAMMA_FIELD:
            case SampleDataInfo.SPGP_FIELD:
            case SampleDataInfo.DCALC_FIELD:
            case SampleDataInfo.COLOR_FIELD:
            case SampleDataInfo.Z_FIELD:
            case SampleDataInfo.T_FIELD:
            case SampleDataInfo.V_FIELD:
            case SampleDataInfo.RF_FIELD:
            case SampleDataInfo.RWF_FIELD:
            case SampleDataInfo.RF2_FIELD:
            case SampleDataInfo.RWF2_FIELD:
            case SampleDataInfo.GOOF_FIELD:
            case SampleDataInfo.SUMMARY_FIELD:
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
            case SampleDataInfo.PROVIDER_ID_FIELD:
                return true;
            default:
                return false;
        }
    }


    /** Create an empty object */
    public SampleDataInfo() {
        firstSampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
        originalSampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        sampleId = SampleInfo.INVALID_SAMPLE_ID;
        a = INVALID_DOUBLE_VALUE;
        b = INVALID_DOUBLE_VALUE;
        c = INVALID_DOUBLE_VALUE;
        alpha = INVALID_DOUBLE_VALUE;
        beta = INVALID_DOUBLE_VALUE;
        gamma = INVALID_DOUBLE_VALUE;
        spgp = null;
        dcalc = INVALID_DOUBLE_VALUE;
        color = null;
        z = INVALID_INT_VALUE;
        t = INVALID_DOUBLE_VALUE;
        v = INVALID_DOUBLE_VALUE;
        rf = INVALID_DOUBLE_VALUE;
        rwf = INVALID_DOUBLE_VALUE;
        rf2 = INVALID_DOUBLE_VALUE;
        rwf2 = INVALID_DOUBLE_VALUE;
        goof = INVALID_DOUBLE_VALUE;
        summary = null;
        formulaWeight = INVALID_DOUBLE_VALUE;
        providerId = ProviderInfo.INVALID_PROVIDER_ID;
    }

    /**
     * Gets the value of the indicated field.  The object type of the value
     * will depend on the type associated with the field and will be the same
     * as expected by <code>setField()</code>.  If the field code refers to a
     * java primitive type, the complex equivalent will be returned (except in
     * the case of <code>INVALID_DOUBLE_VALUE</code> or
     * <code>INVALID_INT_VALUE</code> in which case null will be returned),
     * otherwise the value will be returned as is.
     * @return an <code>Object</code> representation of the value of the field
     *     indicated by <code>fieldCode</code> or null if the field has the
     *     value <code>INVALID_DOUBLE_VALUE</code> or
     *     <code>INVALID_INT_VALUE</code>.
     * @throws IllegalArgumentException if the provided fieldCode is invalid
     *     (this may be verified by a call to <code>isDataField()</code>)
     */
    public Object getField(int fieldCode) {
        assert Double.isNaN(INVALID_DOUBLE_VALUE);
        switch (fieldCode) {
            case SampleDataInfo.A_FIELD:
                return Double.isNaN(this.a) ? null : new Double(this.a);
            case SampleDataInfo.B_FIELD:
                return Double.isNaN(this.b) ? null : new Double(this.b);
            case SampleDataInfo.C_FIELD:
                return Double.isNaN(this.c) ? null : new Double(this.c);
            case SampleDataInfo.ALPHA_FIELD:
                return Double.isNaN(this.alpha)
                        ? null : new Double(this.alpha);
            case SampleDataInfo.BETA_FIELD:
                return Double.isNaN(this.beta)
                        ? null : new Double(this.beta);
            case SampleDataInfo.GAMMA_FIELD:
                return Double.isNaN(this.gamma)
                        ? null : new Double(this.gamma);
            case SampleDataInfo.SPGP_FIELD:
                return this.spgp;
            case SampleDataInfo.DCALC_FIELD:
                return Double.isNaN(this.dcalc)
                        ? null : new Double(this.dcalc);
            case SampleDataInfo.COLOR_FIELD:
                return this.color;
            case SampleDataInfo.Z_FIELD:
                return this.z == INVALID_INT_VALUE ? null
                        : new Integer(this.z);
            case SampleDataInfo.T_FIELD:
                return Double.isNaN(this.t) ? null : new Double(this.t);
            case SampleDataInfo.V_FIELD:
                return Double.isNaN(this.v) ? null : new Double(this.v);
            case SampleDataInfo.RF_FIELD:
                return Double.isNaN(this.rf) ? null : new Double(this.rf);
            case SampleDataInfo.RWF_FIELD:
                return Double.isNaN(this.rwf) ? null : new Double(this.rwf);
            case SampleDataInfo.RF2_FIELD:
                return Double.isNaN(this.rf2) ? null :new Double(this.rf2);
            case SampleDataInfo.RWF2_FIELD:
                return Double.isNaN(this.rwf2)
                        ? null : new Double(this.rwf2);
            case SampleDataInfo.GOOF_FIELD:
                return Double.isNaN(this.goof)
                        ? null : new Double(this.goof);
            case SampleDataInfo.SUMMARY_FIELD:
                return this.summary;
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                return Double.isNaN(this.formulaWeight)
                        ? null : new Double(this.formulaWeight);
            case SampleDataInfo.PROVIDER_ID_FIELD:
                return new Integer(this.providerId);
            default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the value of the indicated field.  The indicated value must be the
     * same type of object as would be returned by <code>getField()</code> for
     * the same fieldCode but may be null.
     * @param fieldCode indicates the field whose value is to be replaced
     * @param value the value to be stored for the given fieldCode.  When
     *     appropriate the java primitive value corresponding to provided
     *     object is used, and <code>INVALID_DOUBLE_VALUE</code> or
     *     <code>INVALID_INT_VALUE</code> is substituted for null.
     * @throws IllegalArgumentException if the provided fieldCode is invalid
     *     (this may be verified by a call to <code>isDataField()</code>)
     */
    public void setField(int fieldCode, Object value) {
            switch(fieldCode) {
            case SampleDataInfo.A_FIELD:
                this.a = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.B_FIELD:
                this.b = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.C_FIELD:
                this.c = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.ALPHA_FIELD:
                this.alpha = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.BETA_FIELD:
                this.beta = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.GAMMA_FIELD:
                this.gamma = value == null ? INVALID_DOUBLE_VALUE
                        : ((Double) value).doubleValue();
                break;
            case SampleDataInfo.SPGP_FIELD:
                this.spgp = (String) value;
                break;
            case SampleDataInfo.DCALC_FIELD:
                this.dcalc = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.COLOR_FIELD:
                this.color = (String) value;
                break;
            case SampleDataInfo.Z_FIELD:
                this.z = value == null ? INVALID_INT_VALUE :
                        ((Integer) value).intValue();
                break;
            case SampleDataInfo.T_FIELD:
                this.t = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.V_FIELD:
                this.v = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.RF_FIELD:
                this.rf = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.RWF_FIELD:
                this.rwf = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.RF2_FIELD:
                this.rf2 = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.RWF2_FIELD:
                this.rwf2 = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.GOOF_FIELD:
                this.goof = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.SUMMARY_FIELD:
                this.summary = (String) value;
                break;
            case SampleDataInfo.FORMULAWEIGHT_FIELD:
                this.formulaWeight = value == null ? INVALID_DOUBLE_VALUE :
                        ((Double) value).doubleValue();
                break;
            case SampleDataInfo.PROVIDER_ID_FIELD:
                this.providerId = value == null
                        ? ProviderInfo.INVALID_PROVIDER_ID
                        : ((Integer) value).intValue();
                break;
            default:
                // invalid fieldCode
                throw new IllegalArgumentException();
        }
    }

    /**
     * Determines whether this {@code SampleDataInfo} is equal to the specified
     * object, which is the case if the other object is also a
     * {@code SamplDataInfo} and has equal values of all its fields
     * 
     * @param x the {@code Object} to compare to this one
     * @return {@code true} if the specified object is equal to this one;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object x) {
        if (x == this) {
            return true;
        } else if (x instanceof SampleDataInfo) {
            SampleDataInfo y = (SampleDataInfo) x;
            
            if (Double.compare(this.a, y.a) != 0) {
                return false;
            }
            if (Double.compare(this.b, y.b) != 0) {
                return false;
            }
            if (Double.compare(this.c, y.c) != 0) {
                return false;
            }
            if (Double.compare(this.alpha, y.alpha) != 0) {
                return false;
            }
            if (Double.compare(this.beta, y.beta) != 0) {
                return false;
            }
            if (Double.compare(this.gamma, y.gamma) != 0) {
                return false;
            }
            if (!SampleInfo.compareReferences(this.spgp, y.spgp)) {
                return false;
            }
            if (Double.compare(this.dcalc, y.dcalc) != 0) {
                return false;
            }
            if (!SampleInfo.compareReferences(this.color, y.color)) {
                return false;
            }
            if (this.z != y.z) {
                return false;
            }
            if (Double.compare(this.t, y.t) != 0) {
                return false;
            }
            if (Double.compare(this.v, y.v) != 0) {
                return false;
            }
            if (Double.compare(this.rf, y.rf) != 0) {
                return false;
            }
            if (Double.compare(this.rwf, y.rwf) != 0) {
                return false;
            }
            if (Double.compare(this.rf2, y.rf2) != 0) {
                return false;
            }
            if (Double.compare(this.rwf2, y.rwf2) != 0) {
                return false;
            }
            if (Double.compare(this.goof, y.goof) != 0) {
                return false;
            }
            if (!SampleInfo.compareReferences(this.summary, y.summary)) {
                return false;
            }
            if (Double.compare(this.formulaWeight, y.formulaWeight) != 0) {
                return false;
            }
            if (this.providerId != y.providerId) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code for this {@code SampleDataInfo} that is consistent
     * with its {@link #equals(Object)} method. Note in particular that the hash
     * computation involves the values of this class's fields, so that it
     * changes if the field values change; note also that it is comparatively
     * expensive to compute. As a result, it is not recommended that instances
     * of this class be stored in hash-based data structures such as
     * {@code HashSet}s or {@code HashMap}s (keys only, values are OK).
     * 
     * @return the (current) hash code for this object
     */
    @Override
    public int hashCode() {
        long hash = 0;

        hash += hashDouble(a) * 3;
        hash += hashDouble(b) * 5;
        hash += hashDouble(c) * 7;
        hash += hashDouble(alpha) * 11;
        hash += hashDouble(beta) * 13;
        hash += hashDouble(gamma) * 17;
        hash += ((spgp == null) ? 0 : spgp.hashCode());
        hash += hashDouble(dcalc) * 19;
        hash += ((color == null) ? 0 : color.hashCode());
        hash += (z << 20);
        hash += hashDouble(t) * 23;
        hash += hashDouble(v) * 29;
        hash += hashDouble(rf) * 31;
        hash += hashDouble(rwf) * 37;
        hash += hashDouble(rf2) * 41;
        hash += hashDouble(rwf2) * 43;
        hash += hashDouble(goof) * 47;
        hash += ((summary == null) ? 0 : summary.hashCode());
        hash += hashDouble(formulaWeight) * 53;
        hash += providerId;

        return (int) (hash ^ (hash >>> 32));
    }
    
    /**
     * Returns a characteristic int for the specified double value, suitable for
     * use in constructing a hash. This method does not distinguish between
     * distinct NaN values.
     * 
     * @param d the {@code double} for which an {@code int} code is requested
     * @return an int characteristic of the specified {@code double}, albeit
     *         not unique to it.
     */
    private int hashDouble(double d) {
        long bits = Double.doubleToLongBits(d);
        
        return (int) (bits ^ (bits >>> 32));
    }
    
    /**
     * Returns a copy of this {@code SampleDataInfo} that is distinct from it
     * but equal to it according to {@link #equals(Object)}
     */
    @Override
    public SampleDataInfo clone() {
        try {
            return (SampleDataInfo) super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new UnexpectedExceptionException(cnse);
        }
    }

    /**
     * Returns a Map that contains one entry for every populated field value
     * on this object.  Keys in the map are of type <code>Integer</code> and
     * are taken from the "field code constants" defined on this class.  Values
     * in the map are of type <code>Integer</code>, <code>Double</code>, and
     * <code>String</code>, depending on the corresponding key's field code.
     * Non-populated fields are not represented in the map.
     * 
     * @return a {@code Map<Integer, Object>} from field codes to field values;
     *         {@code String} values are inserted as-is, {@code double}s and
     *         {@code int}s as {@code Double}s and {@code Integer}s respectively
     */
    public Map<Integer, Object> getFieldsMap() {
        Map<Integer, Object> fields = new HashMap<Integer, Object>();

        if (Double.compare(this.a, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(A_FIELD, new Double(this.a));
        }
        if (Double.compare(this.b, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(B_FIELD, new Double(this.b));
        }
        if (Double.compare(this.c, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(C_FIELD, new Double(this.c));
        }
        if (Double.compare(this.alpha, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(ALPHA_FIELD, new Double(this.alpha));
        }
        if (Double.compare(this.beta, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(BETA_FIELD, new Double(this.beta));
        }
        if (Double.compare(this.gamma, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(GAMMA_FIELD, new Double(this.gamma));
        }
        if (this.spgp != null) {
            fields.put(SPGP_FIELD, this.spgp);
        }
        if (Double.compare(this.dcalc, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(DCALC_FIELD, new Double(this.dcalc));
        }
        if (this.color != null) {
            fields.put(COLOR_FIELD, this.color);
        }
        if (this.z != INVALID_INT_VALUE) {
            fields.put(Z_FIELD, new Integer(this.z));
        }
        if (Double.compare(this.t, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(T_FIELD, new Double(this.t));
        }
        if (Double.compare(this.v, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(V_FIELD, new Double(this.v));
        }
        if (Double.compare(this.rf, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(RF_FIELD, new Double(this.rf));
        }
        if (Double.compare(this.rwf, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(RWF_FIELD, new Double(this.rwf));
        }
        if (Double.compare(this.rf2, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(RF2_FIELD, new Double(this.rf2));
        }
        if (Double.compare(this.rwf2, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(RWF2_FIELD, new Double(this.rwf2));
        }
        if (Double.compare(this.goof, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(GOOF_FIELD, new Double(this.goof));
        }
        if (this.summary != null) {
            fields.put(SUMMARY_FIELD, this.summary);
        }
        if (Double.compare(this.formulaWeight, INVALID_DOUBLE_VALUE) != 0) {
            fields.put(FORMULAWEIGHT_FIELD, new Double(this.formulaWeight));
        }
        if (this.providerId != ProviderInfo.INVALID_PROVIDER_ID) {
            fields.put(PROVIDER_ID_FIELD, new Integer(this.providerId));
        }

        return fields;
    }

    /**
     * Store this object in the specified portion of a DOM tree.  From
     * interface DomTreeParticipant.  This function does not create a parent
     * element; thus it's really only practical for a single one of these
     * objects to be included in a DOM tree underneath its caller-inserted
     * element.
     */
    public Node insertIntoDom(@SuppressWarnings("unused") Document doc,
            Node base) {
        DomUtil.createTextEl(base, "providerId", Integer.toString(providerId));
        if (Double.compare(a, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "a" , Double.toString(a));
        }
        if (Double.compare(b, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "b" , Double.toString(b));
        }
        if (Double.compare(c, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "c" , Double.toString(c));
        }
        if (Double.compare(alpha, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "alpha" , Double.toString(alpha));
        }
        if (Double.compare(beta, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "beta" , Double.toString(beta));
        }
        if (Double.compare(gamma, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "gamma" , Double.toString(gamma));
        }
        if (spgp != null) {
            DomUtil.createTextEl(base, "spgp" ,spgp);
        }
        if (Double.compare(dcalc, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "dcalc" , Double.toString(this.dcalc));
        }
        if (color != null) {
            DomUtil.createTextEl(base, "color" ,color);
        }
        if (z != INVALID_INT_VALUE)  {
            DomUtil.createTextEl(base, "z" , Integer.toString(z));
        }
        if (Double.compare(t, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "t" , Double.toString(t));
        }
        if (Double.compare(v, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "v" , Double.toString(v));
        }
        if (Double.compare(rf, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "rf" , Double.toString(rf));
        }
        if (Double.compare(rwf, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "rwf" , Double.toString(rwf));
        }
        if (Double.compare(rf2, INVALID_DOUBLE_VALUE) != 0) {
           DomUtil.createTextEl(base, "rf2" , Double.toString(rf2));
        }
        if (Double.compare(rwf2, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "rwf2" , Double.toString(rwf2));
        }
        if (Double.compare(goof, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "goof" , Double.toString(goof));
        }
        if (summary != null) {
            DomUtil.createTextEl(base, "summary" , summary);
        }
        if (Double.compare(formulaWeight, INVALID_DOUBLE_VALUE) != 0) {
            DomUtil.createTextEl(base, "formulaWeight",
                    Double.toString(formulaWeight));
        }
        return base;
    }

    /**
     * Replace the member variables of this object with those obtained from
     * the specified portion of a DOM tree.  From interface DomTreeParticipant.
     */
    public Node extractFromDom(@SuppressWarnings("unused") Document doc,
            Node base) throws SAXException {
        
        /*
         * The elements that store data for this class have no explicit parent
         * element -- in practice, this means that the parent <sample> element
         * generated by our parent class SampleInfo is the parent (base)
         * to be used for extraction.  Therefore, there's no need to verify
         * that the base element has the expected name because SampleInfo did
         * that for us.
         */
        Element sampleEl = (Element) base;
        
        /*
         * The providerId field will not be present in pre-0.9.0 ISMs and no
         * value should be supplied here as in such cases the value will have
         * been set by SampleInfo.extractFromDom()
         */
        if (DomUtil.isElPresent(sampleEl, "providerId")) {
            providerId = DomUtil.getTextForElAsInt(sampleEl, "providerId");
        }
        
        a = DomUtil.getTextForElAsDouble(sampleEl, "a", INVALID_DOUBLE_VALUE);
        b = DomUtil.getTextForElAsDouble(sampleEl, "b", INVALID_DOUBLE_VALUE);
        c = DomUtil.getTextForElAsDouble(sampleEl, "c", INVALID_DOUBLE_VALUE);
        alpha = DomUtil.getTextForElAsDouble(sampleEl, "alpha",
                INVALID_DOUBLE_VALUE);
        beta = DomUtil.getTextForElAsDouble(sampleEl, "beta",
                INVALID_DOUBLE_VALUE);
        gamma = DomUtil.getTextForElAsDouble(sampleEl, "gamma",
                INVALID_DOUBLE_VALUE);
        spgp = DomUtil.getTextForEl(sampleEl, "spgp", false);
        dcalc = DomUtil.getTextForElAsDouble(sampleEl, "dcalc",
                INVALID_DOUBLE_VALUE);
        color = DomUtil.getTextForEl(sampleEl, "color", false);
        z = DomUtil.getTextForElAsInt(sampleEl, "c",INVALID_INT_VALUE);
        t = DomUtil.getTextForElAsDouble(sampleEl, "t", INVALID_DOUBLE_VALUE);
        v = DomUtil.getTextForElAsDouble(sampleEl, "v", INVALID_DOUBLE_VALUE);
        rf = DomUtil.getTextForElAsDouble(sampleEl, "rf",
                INVALID_DOUBLE_VALUE);
        rwf = DomUtil.getTextForElAsDouble(sampleEl, "rwf",
                INVALID_DOUBLE_VALUE);
        rf2 = DomUtil.getTextForElAsDouble(sampleEl, "rf2",
                INVALID_DOUBLE_VALUE);
        rwf2 = DomUtil.getTextForElAsDouble(sampleEl, "rwf2",
                INVALID_DOUBLE_VALUE);
        goof = DomUtil.getTextForElAsDouble(sampleEl, "goof",
                INVALID_DOUBLE_VALUE);
        summary = DomUtil.getTextForEl(sampleEl, "summary", false);
        formulaWeight = DomUtil.getTextForElAsDouble(sampleEl, "formulaWeight",
                INVALID_DOUBLE_VALUE);

        return base;
    }

    /*
     * TODO: create a new class called CoreSiteInfo that extends SiteInfo.
     *       Then, take everything that appears below this line and move
     *       it to that class.  Only Site Manager needs access to the
     *       variables and methods found below.
     */
    public int firstSampleHistoryId;
    public int lastSampleHistoryId;
    public int originalSampleHistoryId;
    public int sampleId;

    /**
     * Constructor to create this object from the current record in a db
     * resultset
     */
    public SampleDataInfo(ResultSet rs) throws SQLException {
        firstSampleHistoryId = rs.getInt("first_sampleHistory_id");
        lastSampleHistoryId = rs.getInt("last_sampleHistory_id");
        originalSampleHistoryId = rs.getInt("original_sampleHistory_id");
        if (rs.wasNull()) {
            lastSampleHistoryId = SampleHistoryInfo.STILL_ACTIVE;
        }
        sampleId = rs.getInt("sample_id");
        a = rs.getDouble("a");
        if (rs.wasNull()) {
            a = INVALID_DOUBLE_VALUE;
        }
        b = rs.getDouble("b");
        if (rs.wasNull()) {
            b = INVALID_DOUBLE_VALUE;
        }
        c = rs.getDouble("c");
        if (rs.wasNull()) {
            c = INVALID_DOUBLE_VALUE;
        }
        alpha = rs.getDouble("alpha");
        if (rs.wasNull()) {
            alpha = INVALID_DOUBLE_VALUE;
        }
        beta = rs.getDouble("beta");
        if (rs.wasNull()) {
            beta = INVALID_DOUBLE_VALUE;
        }
        gamma = rs.getDouble("gamma");
        if (rs.wasNull()) {
            gamma = INVALID_DOUBLE_VALUE;
        }
        spgp = rs.getString("spgp");
        dcalc = rs.getDouble("dcalc");
        if (rs.wasNull()) {
            dcalc = INVALID_DOUBLE_VALUE;
        }
        color = rs.getString("color");
        z = rs.getInt("z");
        if (rs.wasNull()) {
            z = INVALID_INT_VALUE;
        }
        t = rs.getDouble("t");
        if (rs.wasNull()) {
            t = INVALID_DOUBLE_VALUE;
        }
        v = rs.getDouble("v");
        if (rs.wasNull()) {
            v = INVALID_DOUBLE_VALUE;
        }
        rf = rs.getDouble("rf");
        if (rs.wasNull()) {
            rf = INVALID_DOUBLE_VALUE;
        }
        rwf = rs.getDouble("rwf");
        if (rs.wasNull()) {
            rwf = INVALID_DOUBLE_VALUE;
        }
        rf2 = rs.getDouble("rf2");
        if (rs.wasNull()) {
            rf2 = INVALID_DOUBLE_VALUE;
        }
        rwf2 = rs.getDouble("rwf2");
        if (rs.wasNull()) {
            rwf2 = INVALID_DOUBLE_VALUE;
        }
        goof = rs.getDouble("goof");
        if (rs.wasNull()) {
            goof = INVALID_DOUBLE_VALUE;
        }
        summary = rs.getString("summary");
        formulaWeight = rs.getDouble("formulaWeight");
        if (rs.wasNull()) {
            formulaWeight = INVALID_DOUBLE_VALUE;
        }
        providerId = rs.getInt("provider_id");
       
    }

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("first_sampleHistory_id", firstSampleHistoryId);
        if (lastSampleHistoryId != SampleHistoryInfo.STILL_ACTIVE) {
            rs.updateInt("last_sampleHistory_id", lastSampleHistoryId);
        } else {
            rs.updateNull("last_sampleHistory_id");
        }
        rs.updateInt("original_sampleHistory_id", originalSampleHistoryId);
        rs.updateInt("sample_id", sampleId);
        if (Double.compare(a, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("a", a);
        } else {
            rs.updateNull("a");
        }
        if (Double.compare(b, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("b", b);
        } else {
            rs.updateNull("b");
        }
        if (Double.compare(c, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("c", c);
        } else {
            rs.updateNull("c");
        }
        if (Double.compare(alpha, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("alpha", alpha);
        } else {
            rs.updateNull("alpha");
        }
        if (Double.compare(beta, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("beta", beta);
        } else {
            rs.updateNull("beta");
        }
        if (Double.compare(gamma, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("gamma", gamma);
        } else {
            rs.updateNull("gamma");
        }
        if (spgp != null) {
            rs.updateString("spgp", spgp);
        } else {
            rs.updateNull("spgp");
        }
        if (Double.compare(dcalc, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("dcalc", dcalc);
        } else {
            rs.updateNull("dcalc");
        }
        if (color != null) {
            rs.updateString("color", color);
        } else {
            rs.updateNull("color");
        }
        if (z != INVALID_INT_VALUE) {
            rs.updateInt("z", z);
        } else {
            rs.updateNull("z");
        }
        if (Double.compare(t, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("t", t);
        } else {
            rs.updateNull("t");
        }
        if (Double.compare(v, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("v", v);
        } else {
            rs.updateNull("v");
        }
        if (Double.compare(rf, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("rf", rf);
        } else {
            rs.updateNull("rf");
        }
        if (Double.compare(rwf, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("rwf", rwf);
        } else {
            rs.updateNull("rwf");
        }
        if (Double.compare(rf2, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("rf2", rf2);
        } else {
            rs.updateNull("rf2");
        }
        if (Double.compare(rwf2, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("rwf2", rwf2);
        } else {
            rs.updateNull("rwf2");
        }
        if (Double.compare(goof, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("goof", goof);
        } else {
            rs.updateNull("goof");
        }
        if (summary != null) {
            rs.updateString("summary", summary);
        } else {
            rs.updateNull("summary");
        }
        if (Double.compare(formulaWeight, INVALID_DOUBLE_VALUE) != 0) {
            rs.updateDouble("formulaWeight", formulaWeight);
        } else {
            rs.updateNull("formulaWeight");
        }
        rs.updateInt("provider_id", providerId);
    }
}
