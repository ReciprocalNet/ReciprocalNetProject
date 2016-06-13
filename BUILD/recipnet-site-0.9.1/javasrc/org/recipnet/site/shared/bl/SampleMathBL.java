/*
 * Reciprocal Net Project
 * 
 * SampleMathBL.java
 *
 * 12-Nov-2004: eisiorho wrote first draft
 * 11-May-2005: ekoperda added generateSearchUnitCellsInfo() and supporting
 *              code, CellCentering enum, and CellDimensions class
 * 09-Nov-2005: jobollin updated calculateFormulaWeight to use the new formula
 *              parsing methods of ChemicalFormulaBL and fixed three small
 *              problems in Javadoc tags
 * 31-May-2006: jobollin partially reworked the CellCentering enum,
 *              reformatted the source, and moved the MOLE and ANG3_TO_CM3
 *              constants into this class
 * 16-Nov-2006: jobollin updated the SearchUnitCell computation to check that
 *              the returned cell parameters are all numeric (finite and
 *              non-NaN)
 * 05-Jan-2008: ekoperda fixed bug #1861 in calculateFormulaWeight()
 */

package org.recipnet.site.shared.bl;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.recipnet.common.Element;
import org.recipnet.common.Matrix3D;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleDataInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SearchUnitCellsInfo;

/**
 * SampleMathBL is a container class that contains calculations done for
 * samples. It may also include any future methods that may not exactly pertain
 * directly to samples, but any method that involves complex calculations.
 */
public abstract class SampleMathBL {

    /**
     * A factor that describes the floating-point error that this class will
     * tolerate as it calculates a reduced cell and tests for (in)equality
     * between two scalars. The value here, 10<sup>-5</sup>, was suggested by
     * R. W. Grosse-Kunstleve and others in their paper publishinged in <i>Acta
     * Crystallographica</i>, volume A60, year 2004, pages 1-6.
     */
    private static final double RUC_EPSILON_RELATIVE = 0.00001;

    /**
     * The maximum number of algorithm iterations this class will execute as it
     * attempts to calculate a reduced unit cell. This mitigates the risk that
     * the algorithm might not finish in finite time.
     */
    private static final int RUC_ITERATION_LIMIT = 64;

    /**
     * The 3x3 transformation matrix for converting an I-centered unit cell to
     * an equivalent primitive one; taken from <i>International Tables for
     * Crystallography</i>, edited by Theo Hahn, volume A, year 1983.
     */
    private static final Matrix3D I_TO_P_TRANSFORMATION = new Matrix3D(-1 / 2f,
            1 / 2f, 1 / 2f, 1 / 2f, -1 / 2f, 1 / 2f, 1 / 2f, 1 / 2f, -1 / 2f);

    /**
     * The 3x3 transformation matrix for converting an F-centered unit cell to
     * an equivalent primitive one; taken from <i>International Tables for
     * Crystallography</i>, edited by Theo Hahn, volume A, year 1983.
     */
    private static final Matrix3D F_TO_P_TRANSFORMATION = new Matrix3D(0,
            1 / 2f, 1 / 2f, 1 / 2f, 0, 1 / 2f, 1 / 2f, 1 / 2f, 0);

    /**
     * The 3x3 transformation matrix for converting an A-centered unit cell to
     * an equivalent I-centered one; taken from <i>International Tables for
     * Crystallography</i>, edited by Theo Hahn, volume A, year 1983.
     */
    private static final Matrix3D A_TO_I_TRANSFORMATION = new Matrix3D(-1, 0,
            1, 0, 1, 0, -1, 0, 0);

    /**
     * The 3x3 transformation matrix for converting a B-centered unit cell to an
     * equivalent I-centered one; taken from <i>International Tables for
     * Crystallography</i>, edited by Theo Hahn, volume A, year 1983.
     */
    private static final Matrix3D B_TO_I_TRANSFORMATION = new Matrix3D(0, -1,
            0, 1, -1, 0, 0, 0, 1);

    /**
     * The 3x3 transformation matrix for converting a C-centered unit cell to an
     * equivalent I-centered one; taken from <i>International Tables for
     * Crystallography</i>, edited by Theo Hahn, volume A, year 1983.
     */
    private static final Matrix3D C_TO_I_TRANSFORMATION = new Matrix3D(1, 0, 0,
            0, 0, -1, 0, 1, -1);

    /**
     * The 3x3 transformation matrix for converting an R-centered unit cell
     * referred to hexagonal axes to an equivalent primitive one; taken from
     * <i>International Tables for Crystallography</i>, edited by Theo Hahn,
     * volume A, year 1983.
     */
    private static final Matrix3D R_OBVERSE_TO_P_TRANSFORMATION = new Matrix3D(
            2 / 3f, -1 / 3f, -1 / 3f, 1 / 3f, 1 / 3f, -2 / 3f, 1 / 3f, 1 / 3f,
            1 / 3f);

    /**
     * Avogadro's constant, the number of items in a mole; also the number
     * of atomic mass units in one gram
     */
    private static final double MOLE = 6.02214e23d;

    /**
     * the multiplicative conversion factor from cubic Angstroms to cubic
     * centimeters
     */
    private static final double ANG3_TO_CM3 = 1e-24d;

    /**
     * Calculates the formula weight corresponding to this sample's empirical
     * formula attribute, if any, and stores it in the associated
     * {@code SampleDataInfo} object
     * 
     * @param si a {@code SampleInfo} representing the sample for which a
     *        formula weight is desirted
     */
    public static void calculateFormulaWeight(SampleInfo si) {
        SampleDataInfo sdi = si.dataInfo;
        SampleAttributeInfo attr
                = si.getFirstAttributeOfType(SampleTextBL.EMPIRICAL_FORMULA);
        if (attr != null) {
            try {
                Map<Element, ? extends Number> counts
                        = ChemicalFormulaBL.getAtomCounts(
                                ChemicalFormulaBL.parseFormula(attr.value),
                                false);
                double weight = 0d;

                for (Map.Entry<Element, ? extends Number> entry
                        : counts.entrySet()) {
                    weight += (entry.getKey().getAtomicMass()
                            * entry.getValue().doubleValue());
                }

                sdi.formulaWeight = weight;
            } catch (InvalidDataException ide) {
                sdi.formulaWeight = SampleDataInfo.INVALID_DOUBLE_VALUE;
            } catch (IllegalArgumentException ex) {
		sdi.formulaWeight = SampleDataInfo.INVALID_DOUBLE_VALUE;
	    }
        } else {
            sdi.formulaWeight = SampleDataInfo.INVALID_DOUBLE_VALUE;
        }
    }

    /**
     * calculates the unit cell volume from this sample's current unit cell
     * parameters
     * 
     * @param sdi a {@code SampleDataInfo} object
     */
    public static void calculateVolume(SampleDataInfo sdi) {
        if ((Double.compare(sdi.a, SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.b,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.c,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.alpha,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.beta,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.gamma,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)) {
            sdi.v = SampleDataInfo.INVALID_DOUBLE_VALUE;
        } else {
            sdi.v = new CellDimensions(sdi).calculateVolume();
        }
    }

    /**
     * calculates this sample's density from its unit cell volume and formula
     * weight
     * 
     * @param sdi a {@code SampleDataInfo} object
     */
    public static void calculateDensity(SampleDataInfo sdi) {
        if ((Double.compare(sdi.formulaWeight,
                SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sdi.v,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (sdi.z == SampleDataInfo.INVALID_INT_VALUE)) {
            sdi.dcalc = SampleDataInfo.INVALID_DOUBLE_VALUE;
        } else {
            sdi.dcalc = ((sdi.formulaWeight * sdi.z)
                    / (sdi.v * (MOLE * ANG3_TO_CM3)));
        }
    }

    /**
     * Examines the crystallographic unit cell that is described by select
     * fields within the supplied {@code SampleInfo} object, calculates the
     * cell's primitive form, reduced form, and reciprocal-reduced form, and
     * returns some of these results. A caller might use the returned
     * {@code SearchUnitCellsInfo} object to populate a database table or to
     * match against rows in the same table.
     * 
     * @return a SearchUnitCellsInfo that contains select results of the
     *         calculations, or null if the calculations could not be completed
     *         for some reason.
     * @param sample a {@code SampleInfo} object that describes the unit cell to
     *        be examined. The fields {@code sample.dataInfo.a},
     *        {@code sample.dataInfo.b}, {@code sample.dataInfo.c},
     *        {@code sample.dataInfo.alpha}, {@code sample.dataInfo.beta}, and
     *        {@code sample.dataInfo.gamma}, and {@code sample.dataInfo.spgp}
     *        all should contain meangingful values or else this function will
     *        return null.
     */
    public static SearchUnitCellsInfo generateSearchUnitCellsInfo(
            SampleInfo sample) {
        
        // Extract the unit cell dimensions from the SampleInfo.
        
        if ((sample.dataInfo == null)
                || (Double.compare(sample.dataInfo.a,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sample.dataInfo.b,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sample.dataInfo.c,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sample.dataInfo.alpha,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sample.dataInfo.beta,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)
                || (Double.compare(sample.dataInfo.gamma,
                        SampleDataInfo.INVALID_DOUBLE_VALUE) == 0)) {
            
            // insufficient information about the unit cell is avaiable.
            
            return null;
        }
        CellDimensions unitCell = new CellDimensions(sample.dataInfo);

        // Divine the sample's centering symbol.
        CellCentering centering;

        if (sample.dataInfo.spgp != null) {
            try {
                SpaceGroupSymbolBL.SpaceGroupSymbol spaceGroup
                        = SpaceGroupSymbolBL.digestSymbol(
                                SpaceGroupSymbolBL.createFormattedSymbol(
                                        sample.dataInfo.spgp));

                centering = CellCentering.forSymbol(spaceGroup.getCentering());
            } catch (InvalidDataException ex) {
                /*
                 * the sample's spgp field is not parseable or is not valid.
                 * Just set the centering to null
                 */
                centering = null;
            }
        } else {
            /*
             * TODO: add additional centering detection methods here. (Task
             * #1603)
             */
            centering = null;
        }

        return generateSearchUnitCellsInfo(unitCell, centering, sample.id);
    }

    /**
     * Examines the specified crystallographic unit cell, calculates its
     * primitive form, reduced form, and reciprocal-reduced form, and returns
     * some of these results. A caller might use the returned
     * {@code SearchUnitCellsInfo} object to populate a database table or to
     * match against rows in the same table.
     * 
     * @return a SearchUnitCellsInfo that contains select results of the
     *         calculations, or null if the calculations could not be completed
     *         for some reason.
     * @param unitCell the dimensions of unit cell to be examined. If this is
     *        null then the function returns null.
     * @param centering the centering symbol for the unit cell being examined.
     *        If this is null then the function returns null.
     * @param sampleId this integer value is used opaquely to populate the field
     *        {@code SearchUnitCellsInfo.sampleId} in the returned object as a
     *        convenience to the caller. The value specified here has no effect
     *        on this function's operation.
     */
    public static SearchUnitCellsInfo generateSearchUnitCellsInfo(
            CellDimensions unitCell, CellCentering centering, int sampleId) {
        // Quick sanity check.
        if ((unitCell == null) || (centering == null)) {
            return null;
        }

        // Do some calculations on the unit cell.
        CellDimensions primitiveCell = calculatePrimitiveCell(unitCell,
                centering);
        CellDimensions primitiveCellReduced
                = calculateReducedCell(primitiveCell);
        CellDimensions primitiveCellReciprocalReduced
                = calculateReducedCell(calculateReciprocalCell(primitiveCell));
        if ((primitiveCellReduced == null)
                || (!primitiveCellReduced.isWellDetermined())
                || (primitiveCellReciprocalReduced == null)
                || (!primitiveCellReciprocalReduced.isWellDetermined())) {
            /*
             * Our calculations failed for some reason. We have nothing to
             * return to the caller.
             */
            return null;
        }

        // Populate a SearchUnitCellsInfo object and return it to the caller.
        SearchUnitCellsInfo suci = new SearchUnitCellsInfo();
        
        suci.sampleId = sampleId;
        suci.aPrime = primitiveCellReduced.a;
        suci.bPrime = primitiveCellReduced.b;
        suci.cPrime = primitiveCellReduced.c;
        suci.vPrime = primitiveCellReduced.calculateVolume();
        suci.aStarPrime = primitiveCellReciprocalReduced.a;
        suci.bStarPrime = primitiveCellReciprocalReduced.b;
        suci.cStarPrime = primitiveCellReciprocalReduced.c;
        
        return suci;
    }

    /**
     * Converts a unit cell in a specified setting to a primitive cell, (i.e.
     * one whose centering symbol is 'P'). There is no guarantee that the
     * returned primitive cell is in a standard setting and no guarantee that it
     * is in reduced form.
     * 
     * @return the dimensions of a primitive cell.
     * @param unitCell the dimensions of the unit cell.
     * @param centering the centering symbol that describes the unit cell.
     */
    private static CellDimensions calculatePrimitiveCell(
            CellDimensions unitCell, CellCentering centering) {
        Matrix3D matrix = unitCell.calculateBasisVectors();
        
        switch (centering) {
            case PRIMITIVE:
                // Do nothing; the cell is already primitive.
                break;
            case BODY:
                matrix.mult(I_TO_P_TRANSFORMATION);
                break;
            case ALL_FACES:
                matrix.mult(F_TO_P_TRANSFORMATION);
                break;
            case A_FACE:
                matrix.mult(A_TO_I_TRANSFORMATION);
                matrix.mult(I_TO_P_TRANSFORMATION);
                break;
            case B_FACE:
                matrix.mult(B_TO_I_TRANSFORMATION);
                matrix.mult(I_TO_P_TRANSFORMATION);
                break;
            case C_FACE:
                matrix.mult(C_TO_I_TRANSFORMATION);
                matrix.mult(I_TO_P_TRANSFORMATION);
                break;
            case RHOMBOHEDRAL:
                /*
                 * It doesn't much matter whether the unit cell is in an obverse
                 * setting or a reverse setting. We'll guess that the obverse
                 * setting was used. If our guess is incorrect, then the
                 * primitive cell we calculate may not be properly oriented.
                 * However, orientation is irrelevant to this function because
                 * the function returns only the dimensions of the primitive
                 * cell. Dimensions of the primitive cell are identical between
                 * the obverse and reverse settings.
                 */
                matrix.mult(R_OBVERSE_TO_P_TRANSFORMATION);
                break;
            default:
                // All enumeration values were addressed above.
                assert false;
        }
        return new CellDimensions(matrix);
    }

    /**
     * Calculates the reduced form of the supplied primitive cell. This is also
     * called the Niggli cell.
     * 
     * @param primitiveCell serves as the starting point for the calculation. A
     *        null reference here results in a null return value.
     * @return a {@code CellDimensions} object that describes the reduced cell
     *         that resulted from the calculation, or null if the calculation
     *         could not be completed for some reason.
     */
    private static CellDimensions calculateReducedCell(
            CellDimensions primitiveCell) {
        // Exit early in the trivial case.
        if (primitiveCell == null) {
            return null;
        }

        /*
         * This implementation computes the Niggli form of the unit cell using
         * the algorithm described by I. Krivy and B. Gruber in
         * _Acta_Crystallographica_, year 1976, volume A32, pages 297-298.
         * Another paper was used as a secondary reference: B. Gruber, _Acta_
         * Crystallographica_, year 1973, volume A29, pages 433-440.
         * Additionally, the Krivy and Gruber algorithm is modified in
         * accordance with the recommendations of R. W. Grosse-Kunstleve and
         * others in _Acta_Crystallographica_, year 2004, volume A60, pages 1-6
         * to ensure its numeric stability with the floating-point numeric
         * representations that we use.
         */
        double A = primitiveCell.a * primitiveCell.a;
        double B = primitiveCell.b * primitiveCell.b;
        double C = primitiveCell.c * primitiveCell.c;
        double xi = 2 * primitiveCell.b * primitiveCell.c
                * Math.cos(Math.toRadians(primitiveCell.alpha));
        double eta = 2 * primitiveCell.a * primitiveCell.c
                * Math.cos(Math.toRadians(primitiveCell.beta));
        double zeta = 2 * primitiveCell.a * primitiveCell.b
                * Math.cos(Math.toRadians(primitiveCell.gamma));
        FuzzyComparator fc = new FuzzyComparator(RUC_EPSILON_RELATIVE
                * Math.pow(primitiveCell.calculateVolume(), 1 / 3f));
        boolean done = false;
        for (int i = 0; i < RUC_ITERATION_LIMIT; i++) {
            /*
             * The for loop that encloses this algorithm guards against a
             * possible runaway condition. It would be difficult to prove that
             * this algorithm always finishes in a finite number of iterations
             * otherwise.
             */

            // Step 1.
            if (fc.gt(A, B)
                    || (fc.eq(A, B) && fc.gt(Math.abs(xi), Math.abs(eta)))) {
                double temp = A;
                A = B;
                B = temp;
                temp = xi;
                xi = eta;
                eta = temp;
            }

            // Step 2.
            if (fc.gt(B, C)
                    || (fc.eq(B, C) && fc.gt(Math.abs(eta), Math.abs(zeta)))) {
                double temp = B;
                B = C;
                C = temp;
                temp = eta;
                eta = zeta;
                zeta = temp;
                continue;
            }

            /*
             * Steps 3 and 4. Note that this test is slightly different from the
             * one purportedly used by Krivy and Gruber (1976). We use zeta
             * where their paper says they used xi (in step 3). This likely was
             * a typographical error by the journal, as the Gruber (1973) paper
             * contains the same form we use here.
             */
            int countPositive = 0;
            int countZero = 0;
            if (fc.lt(0, xi)) {
                countPositive++;
            } else if (!fc.lt(xi, 0)) {
                countZero++;
            }
            if (fc.lt(0, eta)) {
                countPositive++;
            } else if (!fc.lt(eta, 0)) {
                countZero++;
            }
            if (fc.lt(0, zeta)) {
                countPositive++;
            } else if (!fc.lt(zeta, 0)) {
                countZero++;
            }
            if ((countPositive == 3) || ((countPositive == 1) && (countZero == 0))) {
                // The quantity xi*eta*zeta must be positive. Do step 3.
                xi = Math.abs(xi);
                eta = Math.abs(eta);
                zeta = Math.abs(zeta);
            } else {
                // The quantity xi*eta*zeta must be negative or zero. Do step 4
                xi = -Math.abs(xi);
                eta = -Math.abs(eta);
                zeta = -Math.abs(zeta);
            }

            // Step 5.
            if (fc.gt(Math.abs(xi), B)
                    || (fc.eq(xi, B) && fc.lt(eta + eta, zeta))
                    || (fc.eq(xi, -B) && fc.lt(zeta, 0))) {
                C = B + C - xi * Math.signum(xi);
                eta = eta - zeta * Math.signum(xi);
                xi = xi - (B + B) * Math.signum(xi);
                continue;
            }

            // Step 6.
            if (fc.gt(Math.abs(eta), A)
                    || (fc.eq(eta, A) && fc.lt(xi + xi, zeta))
                    || (fc.eq(eta, -A) && fc.lt(zeta, 0))) {
                C = A + C - eta * Math.signum(eta);
                xi = xi - zeta * Math.signum(eta);
                eta = eta - (A + A) * Math.signum(eta);
                continue;
            }

            // Step 7.
            if (fc.gt(Math.abs(zeta), A)
                    || (fc.eq(zeta, A) && fc.lt(xi + xi, eta))
                    || (fc.eq(zeta, -A) && fc.lt(eta, 0))) {
                B = A + B - zeta * Math.signum(zeta);
                xi = xi - eta * Math.signum(zeta);
                zeta = zeta - (A + A) * Math.signum(zeta);
                continue;
            }

            // Step 8.
            double sum = xi + eta + zeta + A + B;
            if (fc.lt(sum, 0)
                    || (fc.eq(sum, 0) && fc.gt(A + A + eta + eta + zeta, 0))) {
                C = sum + C;
                xi = B + B + xi + zeta;
                eta = A + A + eta + zeta;
                continue;
            }

            // If control reaches here then the algorithm is complete.
            done = true;
            break;
        }
        if (!done) {
            // Panic! The algorithm failed to finish within finite time. I
            // hope this never happens.
            return null;
        }

        // Prepare a data structure and return it.
        double sqrtA = Math.sqrt(A);
        double sqrtB = Math.sqrt(B);
        double sqrtC = Math.sqrt(C);
        return new CellDimensions(sqrtA, sqrtB, sqrtC,
                Math.toDegrees(Math.acos(xi / (2 * sqrtB * sqrtC))),
                Math.toDegrees(Math.acos(eta / (2 * sqrtA * sqrtC))),
                Math.toDegrees(Math.acos(zeta / (2 * sqrtA * sqrtB))));
    }

    /**
     * Calculates the reciprocal cell given a specified unit cell in real space.
     * That is, the crystallographic unit cell described by {@code unitCell} in
     * terms of "real space" is transformed to "reciprocal space" and the
     * resulting reciprocal-space cell is returned.
     * 
     * @return the dimensions of the unit cell in terms of reciprocal space, or
     *         null if the calculation could not be completed for some reason.
     * @param unitCell the dimensions of the unit cell in terms of real space.
     *        Passing a value of null here results in a null being returned.
     */
    private static CellDimensions calculateReciprocalCell(
            CellDimensions unitCell) {
        // Quick sanity check.
        if (unitCell == null) {
            return null;
        }

        double volume = unitCell.calculateVolume();

        double alphaRadians = Math.toRadians(unitCell.alpha);
        double betaRadians = Math.toRadians(unitCell.beta);
        double gammaRadians = Math.toRadians(unitCell.gamma);

        double sinAlpha = Math.sin(alphaRadians);
        double sinBeta = Math.sin(betaRadians);
        double sinGamma = Math.sin(gammaRadians);
        double cosAlpha = Math.cos(alphaRadians);
        double cosBeta = Math.cos(betaRadians);
        double cosGamma = Math.cos(gammaRadians);

        double alphaStarRadians = Math.acos((cosBeta * cosGamma - cosAlpha)
                / (sinBeta * sinGamma));
        double betaStarRadians = Math.acos((cosAlpha * cosGamma - cosBeta)
                / (sinAlpha * sinGamma));
        double gammaStarRadians = Math.acos((cosAlpha * cosBeta - cosGamma)
                / (sinAlpha * sinBeta));

        return new CellDimensions(unitCell.b * unitCell.c * sinAlpha / volume,
                unitCell.a * unitCell.c * sinBeta / volume, unitCell.a
                        * unitCell.b * sinGamma / volume,
                Math.toDegrees(alphaStarRadians),
                Math.toDegrees(betaStarRadians),
                Math.toDegrees(gammaStarRadians));
    }

    /**
     * An enumeration for the various centering symbols that may describe unit
     * cells that callers may use when interfacing with some of this class's
     * functions.
     */
    public static enum CellCentering {
        PRIMITIVE('P'),
        BODY('I'),
        ALL_FACES('F'),
        A_FACE('A'),
        B_FACE('B'),
        C_FACE('C'),
        RHOMBOHEDRAL('R');

        private char symbol;

        private static Map<Character, CellCentering> centeringMap = null;

        private static Map<Character, CellCentering> getCenteringMap() {
            if (centeringMap == null) {
                centeringMap = new HashMap<Character, CellCentering>();
                for (CellCentering centering : CellCentering.values()) {
                    centeringMap.put(Character.valueOf(centering.getSymbol()),
                            centering);
                }
            }

            return centeringMap;
        }

        CellCentering(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

        public static CellCentering forSymbol(char centeringSymbol) {
            return getCenteringMap().get(Character.valueOf(centeringSymbol));
        }
    }

    /**
     * A container object that callers may use when interfacing with some of
     * this class's functions.
     */
    public static class CellDimensions {
        public double a;

        public double b;

        public double c;

        public double alpha;

        public double beta;

        public double gamma;

        /**
         * Constructor based on explicit dimensions.
         */
        public CellDimensions(double a, double b, double c, double alpha,
                double beta, double gamma) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.alpha = alpha;
            this.beta = beta;
            this.gamma = gamma;
        }

        /**
         * Constructor based on the cell dimensions contained within the
         * supplied {@code SampleDataInfo} object.
         */
        public CellDimensions(SampleDataInfo sdi) {
            this(sdi.a, sdi.b, sdi.c, sdi.alpha, sdi.beta, sdi.gamma);
        }

        /**
         * Constructor based on three explicit basis vectors <strong>a</strong>,
         * <strong>b</strong>, and <strong>c</strong>. The vectors are
         * expressed as columns in the {@code matrix}. The "translation
         * component" of the {@code matrix} is ignored.
         * 
         * @param matrix a description of the three basis vectors in columns, of
         *        the sort that the caller might have obtained by a previous
         *        call to {@code calculateBasisVectors()}.
         */
        public CellDimensions(Matrix3D matrix) {
            this.a = Math.sqrt(matrix.xx * matrix.xx + matrix.xy * matrix.xy
                    + matrix.xz * matrix.xz);
            this.b = Math.sqrt(matrix.yx * matrix.yx + matrix.yy * matrix.yy
                    + matrix.yz * matrix.yz);
            this.c = Math.sqrt(matrix.zx * matrix.zx + matrix.zy * matrix.zy
                    + matrix.zz * matrix.zz);
            this.alpha = Math.toDegrees(Math.acos((matrix.yx * matrix.zx
                    + matrix.yy * matrix.zy + matrix.yz * matrix.zz)
                    / (this.b * this.c)));
            this.beta = Math.toDegrees(Math.acos((matrix.xx * matrix.zx
                    + matrix.xy * matrix.zy + matrix.xz * matrix.zz)
                    / (this.a * this.c)));
            this.gamma = Math.toDegrees(Math.acos((matrix.yx * matrix.xx
                    + matrix.yy * matrix.xy + matrix.yz * matrix.xz)
                    / (this.a * this.b)));
        }

        /** @return calculated volume of the cell. */
        public double calculateVolume() {
            double cosAlpha = Math.cos(Math.toRadians(this.alpha));
            double cosBeta = Math.cos(Math.toRadians(this.beta));
            double cosGamma = Math.cos(Math.toRadians(this.gamma));

            return this.a * this.b * this.c
                    * calculateSkewingFactor(cosAlpha, cosBeta, cosGamma);
        }

        /**
         * Converts the unit cell whose dimensions are described by this object
         * to a set of three vectors <strong>a</strong>, <strong>b</strong>,
         * and <strong>c</strong> that describe the unit cell equivalently.
         * Each vector is expressed by one column in the returned matrix. This
         * basis complies with the convention that <strong>a</strong> is always
         * parallel to the x-axis and <strong>b</strong> always lies in the
         * xy-plane.
         * 
         * @return a {@code Matrix3D} object, where each column of the "rotation
         *         matrix" portion of the object describes a single basis
         *         vector. For instance, the basis vector <strong>a</strong> is
         *         described by the fields {@code Matrix3D.xx},
         *         {@code Matrix3D.xy}, and {@code Matrix3D.xz}. The
         *         "translation component" feature of the {@code Matrix3D}
         *         object is not utilized and its values are set to 0.
         */
        public Matrix3D calculateBasisVectors() {
            double cosAlpha = Math.cos(Math.toRadians(this.alpha));
            double cosBeta = Math.cos(Math.toRadians(this.beta));
            double cosGamma = Math.cos(Math.toRadians(this.gamma));
            double sinGamma = Math.sin(Math.toRadians(this.gamma));

            /*
             * A downcast from double to float is necessary because Matrix3D
             * doesn't support any better. Hopefully float precision is good
             * enough for our purposes.
             */
            return new Matrix3D(
                    (float) this.a,
                    0,
                    0,
                    (float) (this.b * cosGamma),
                    (float) (this.b * sinGamma),
                    0,
                    (float) (this.c * cosBeta),
                    (float) (this.c
                            * (cosAlpha - cosBeta * cosGamma) / sinGamma),
                    (float) (this.c
                            * calculateSkewingFactor(cosAlpha, cosBeta,
                                    cosGamma) / sinGamma));
        }

        /**
         * Computes a "skewing factor" that contributes to volume and basis
         * vector computations. This factor describes the volume reduction
         * effected by the specified combination of angles between cell vectors,
         * relative to the volume when they are mutually perpendicular.
         * 
         * @param cosAlpha the cosine of the angle between the <i>b</i> and
         *        <i>c</i> cell vectors
         * @param cosBeta the cosine of the angle between the <i>a</i> and
         *        <i>c</i> cell vectors
         * @param cosGamma the cosine of the angle between the <i>a</i> and
         *        <i>b</i> cell vectors
         * @return the skewing factor
         */
        private double calculateSkewingFactor(double cosAlpha, double cosBeta,
                double cosGamma) {
            return Math.sqrt(1d - cosAlpha * cosAlpha - cosBeta * cosBeta
                    - cosGamma * cosGamma + 2d * cosAlpha * cosBeta
                    * cosGamma);
        }

        /**
         * Tests whether this {@code CellDimensions} is 'well determined', which
         * is the case if all its constituent cell parameters are numeric (as
         * opposed to infinite or not-a-number)
         *
         * @return {@code true} if this {@code CellDimensions} is well
         *         determined, {@code false} if not
         */
        public boolean isWellDetermined() {
            return (isNumber(a) && isNumber(b) && isNumber(c)
                    && isNumber(alpha) && isNumber(beta) && isNumber(gamma));
        }
        
        /** For debugging only. */
        @Override
        public String toString() {
            NumberFormat nf = NumberFormat.getInstance();
            
            nf.setMinimumFractionDigits(3);
            nf.setMaximumFractionDigits(3);
            
            return nf.format(a) + " " + nf.format(b) + " " + nf.format(c) + " "
                    + nf.format(alpha) + " " + nf.format(beta) + " "
                    + nf.format(gamma);
        }

        static boolean isNumber(double d) {
             return !(Double.isNaN(d) || Double.isInfinite(d));
        }
    }

    /**
     * A simple internal class that performs fuzzy comparisons between
     * {@code double} values. The error factor {@code epsilon} is defined at
     * construction time and subsequently used in all evaluations. The
     * comparisons contained in this class are modeled after those proposed by
     * R. W. Grosse-Kuntsleeve and others in <i>Acta Crystallographica</i>,
     * volume A60, year 2004, pages 1-6.
     */
    private static class FuzzyComparator {
        private double epsilon;

        public FuzzyComparator(double epsilon) {
            this.epsilon = epsilon;
        }

        /** @return true if {@code x < y} or false otherwise. */
        public boolean lt(double x, double y) {
            return x < y - epsilon;
        }

        /** @return true if {@code x > y} or false otherwise. */
        public boolean gt(double x, double y) {
            return y < x - epsilon;
        }

        /** @return true if {@code x <= y} or false otherwise. */
        public boolean lte(double x, double y) {
            return !(y < x - epsilon);
        }

        /** @return true if {@code x >= y} or false otherwise. */
        public boolean gte(double x, double y) {
            return !(x < y - epsilon);
        }

        /** @return true if {@code x == y} or false otherwise. */
        public boolean eq(double x, double y) {
            return !((x < y - epsilon) || (y < x - epsilon));
        }
    }
}
