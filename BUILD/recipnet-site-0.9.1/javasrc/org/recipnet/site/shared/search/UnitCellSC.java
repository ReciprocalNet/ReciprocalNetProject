/*
 * Reciprocal Net project
 * 
 * UnitCellSC.java
 *
 * 22-Mar-2005: ekoperda wrote first draft
 * 30-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.search;

import org.recipnet.site.shared.bl.SampleMathBL;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SearchUnitCellsInfo;

/**
 * <p>
 * A search constraint that allows crystallographers to find samples having
 * particular unit cell dimensions. This is useful because unit cells tend to be
 * rather unique and are useful in identifying unknown but previously-analyzed
 * crystal structures. In the {@code SampleDataInfo} object, the fields
 * {@code a}, {@code b}, {@code c}, {@code alpha}, {@code beta}, and
 * {@code gamma} constitute a complete set of unit cell dimensions for the
 * associated sample.
 * </p><p>
 * This constraint presently supports two modes. The mode
 * {@code MATCH_UNIT_CELLS_AS_ENTERED} enables literal searches, where samples
 * that have specific values in one or more cell dimension fields are matched. A
 * facility for fuzzy-matching within a specified percent error tolerance is
 * available.
 * </p><p>
 * The mode {@code MATCH_REDUCED_CELLS} matches the "reduced" form of samples'
 * unit cell dimensions. See {@code SampleMathBL} for a description of what a
 * reduced cell is and why it is useful. In this mode, the user describes the
 * unit cell he wishes to find by the usual set of three lengths, three angles,
 * and centering symbol. This description is converted to a primitive cell and
 * then to reduced-cell form (by {@code SampleMathBL}). Then samples having a
 * similar reduced-cell form are matched. A facility for fuzzy-matching within a
 * specified percent error tolerance is available. For efficiency's sake, Sample
 * Manager precomputes samples' reduced cells and stores them in the db table
 * named {@code searchUnitCells}. This constraint class relies upon that db
 * table.
 * </p><p>
 * The term "reduced cell search" is not rigorously defined nor universally
 * understood among crystallographers; thus, this constraint provides a number
 * of user-configurable options when it is in {@code MATCH_REDUCED_CELLS} mode.
 * The obvious use case is matching the six specified-and-reduced cell
 * dimensions to the a', b', c', alpha', beta', and gamma' fields in the
 * database. The problem with this is that small perturbations to a sample's
 * unit cell dimensions can induce radical changes in angle values; thus, any
 * test that includes angles (i.e. alpha', beta', gamma') is generally not
 * useful. Another meaning of reduced cell search is suggested by Andrews,
 * Berstein, and Pelletier: cell angles should be ignored but the reduced cell
 * lengths, reduced cell volume, and reciprocal-reduced cell lengths should be
 * matched against. (See their paper in _Acta_Crystallographica_, year 1980,
 * volume A36, pages 248-252.) A user could achieve this by setting the
 * {@code checkReducedLengths}, {@code checkReducedVolume}, and
 * {@code checkReciprocalLengths} options.
 * </p><p>
 * A third meaning of reduced cell search, and the one apparently employed by
 * the Cambridge Structural Database publication, is to match against reduced
 * cell lengths only. A user could achieve this by setting the
 * {@code matchReducedLengths} option.
 * </p><p>
 * The present implementation of this search constraint is a subclass of
 * {@code SearchConstraintGroup} and generates a number of simple search
 * constraints that live within the group. Future implementations may utilize
 * other approaches.
 * </p>
 */
public class UnitCellSC extends SearchConstraintGroup {
    /** Supported modes are defined here */
    public enum Mode {
        MATCH_UNIT_CELLS_AS_ENTERED, MATCH_REDUCED_CELLS
    }

    private final Mode mode;

    private final Double a;

    private final Double b;

    private final Double c;

    private final Double alpha;

    private final Double beta;

    private final Double gamma;

    private final Double percentErrorTolerance;

    private final SampleMathBL.CellCentering centering;

    private final boolean checkReducedLengths;

    private final boolean checkReducedVolume;

    private final boolean checkReciprocalLengths;

    /**
     * <p>
     * Creates a new search constraint object that will limit search results to
     * those samples having the specified unit cell or reduced cell dimensions.
     * </p><p>
     * Implementation note: this class does not require an {@code equals()}
     * method because the superclass's implementation is sufficient. This
     * constraint is just a simple AND group; the magic is in the
     * sub-constraints this class creates for itself.
     * </p>
     * 
     * @param mode either {@code MATCH_UNIT_CELLS_AS_ENTERED} or
     *        {@code MATCH_REDUCED_CELLS}, the meanings of which are documented
     *        at the class level.
     * @param a one of the unit cell length criteria, in Angstroms, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param b one of the unit cell length criteria, in Angstroms, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param c one of the unit cell length criteria, in Angstroms, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param alpha one of the unit cell angle criteria, in degrees, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param beta one of the unit cell angle criteria, in degrees, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param gamma one of the unit cell angle criteria, in degrees, or null if
     *        this criterion is not specified. Note that this criterion must be
     *        specified in {@code MATCH_REDUCED_CELLS} mode in order to avoid an
     *        exception.
     * @param percentErrorTolerance the maximum percentage by which samples'
     *        unit cell dimensions may differ from specified criteria values and
     *        the samples still match, or null if this is not specified. Note
     *        that this criterion must be specified in all modes in order to
     *        avoid an exception.
     * @param centering when {@code mode} is {@code MATH_REDUCED_CELLS}, this
     *        field indicates the centering of the unit cell whose dimensions
     *        were passed in the {@code a}, {@code b}, {@code c},
     *        {@code alpha}, {@code beta}, and {@code gamma} fields. This
     *        option has no effect in other modes.
     * @param checkReducedLengths if this option is true and {@code mode} is
     *        {@code MATCH_REDUCED_CELLS}, a sample's precomputed {@code a'},
     *        {@code b'}, and {@code c'} fields are considered as this
     *        constraint makes matching decisions. This option has no effect in
     *        other modes.
     * @param checkReducedVolume if this option is true and {@code mode} is
     *        {@code MATCH_REDUCED_CELLS}, a sample's precomputed
     *        {@code volume'} fields are considered as this constraint makes
     *        matching decisions. This option has no effect in other modes.
     * @param checkReciprocalLengths if this option is true and {@code mode} is
     *        {@code MATCH_REDUCED_CELLS}, a sample's precomputed {@code a*'},
     *        {@code b*'}, and {@code c*'} fields are considered as this
     *        constraint makes matching decisions. This option has no effect in
     *        other modes.
     * @throws IllegalArgumentException if the combination of input arguments is
     *         not valid.
     */
    public UnitCellSC(Mode mode, Double a, Double b, Double c, Double alpha,
            Double beta, Double gamma, Double percentErrorTolerance,
            SampleMathBL.CellCentering centering, boolean checkReducedLengths,
            boolean checkReducedVolume, boolean checkReciprocalLengths) {
        super(AND);
        this.mode = mode;
        this.a = a;
        this.b = b;
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;
        this.percentErrorTolerance = percentErrorTolerance;
        this.centering = centering;
        this.checkReducedLengths = checkReducedLengths;
        this.checkReducedVolume = checkReducedVolume;
        this.checkReciprocalLengths = checkReciprocalLengths;

        switch (this.mode) {
            case MATCH_UNIT_CELLS_AS_ENTERED:
                if (a != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.A_FIELD, a,
                            percentErrorTolerance));
                }
                if (b != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.B_FIELD, b,
                            percentErrorTolerance));
                }
                if (c != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.C_FIELD, c,
                            percentErrorTolerance));
                }
                if (alpha != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.ALPHA_FIELD,
                            alpha, percentErrorTolerance));
                }
                if (beta != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.BETA_FIELD,
                            beta, percentErrorTolerance));
                }
                if (gamma != null) {
                    addChild(new SampleDataRangeSC(SampleDataInfo.GAMMA_FIELD,
                            gamma, percentErrorTolerance));
                }

                break;
            case MATCH_REDUCED_CELLS:
                SearchUnitCellsInfo suci = SampleMathBL.generateSearchUnitCellsInfo(
                        new SampleMathBL.CellDimensions(a, b, c, alpha, beta,
                                gamma), centering, SampleInfo.INVALID_SAMPLE_ID);
                if (checkReducedLengths) {
                    addChild(new UnitCellSearchIndexSC("aprime", suci.aPrime,
                            percentErrorTolerance));
                    addChild(new UnitCellSearchIndexSC("bprime", suci.bPrime,
                            percentErrorTolerance));
                    addChild(new UnitCellSearchIndexSC("cprime", suci.cPrime,
                            percentErrorTolerance));
                }
                if (checkReducedVolume) {
                    addChild(new UnitCellSearchIndexSC("vprime", suci.vPrime,
                            percentErrorTolerance));
                }
                if (checkReciprocalLengths) {
                    addChild(new UnitCellSearchIndexSC("astarprime",
                            suci.aStarPrime, percentErrorTolerance));
                    addChild(new UnitCellSearchIndexSC("bstarprime",
                            suci.bStarPrime, percentErrorTolerance));
                    addChild(new UnitCellSearchIndexSC("cstarprime",
                            suci.cStarPrime, percentErrorTolerance));
                }
                break;
        }
    }

    /** Simple getter */
    public Mode getMode() {
        return this.mode;
    }

    /** Simple getter */
    public Double getA() {
        return this.a;
    }

    /** Simple getter */
    public Double getB() {
        return this.b;
    }

    /** Simple getter */
    public Double getC() {
        return this.c;
    }

    /** Simple getter */
    public Double getAlpha() {
        return this.alpha;
    }

    /** Simple getter */
    public Double getBeta() {
        return this.beta;
    }

    /** Simple getter */
    public Double getGamma() {
        return this.gamma;
    }

    /** Simple getter */
    public Double getPercentErrorTolerance() {
        return this.percentErrorTolerance;
    }

    public SampleMathBL.CellCentering getCentering() {
        return this.centering;
    }

    /** Simple getter */
    public boolean getCheckReducedLengths() {
        return this.checkReducedLengths;
    }

    /** Simple getter */
    public boolean getCheckReducedVolume() {
        return this.checkReducedVolume;
    }

    /** Simple getter */
    public boolean getCheckReciprocalLengths() {
        return this.checkReciprocalLengths;
    }
}
