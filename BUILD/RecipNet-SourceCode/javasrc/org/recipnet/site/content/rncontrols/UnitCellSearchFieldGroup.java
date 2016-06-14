/*
 * Reciprocal Net project
 * 
 * UnitCellSearchField.java
 * 
 * 11-May-2005: ekoperda wrote first draft
 * 30-May-2006: jobollin reformatted the source, implemented generics
 */

package org.recipnet.site.content.rncontrols;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.ValidationContext;
import org.recipnet.site.shared.bl.SampleMathBL;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.shared.search.SearchConstraintGroup;
import org.recipnet.site.shared.search.UnitCellSC;

/**
 * <p>
 * A custom tag that is intended for use within a {@code SearchPage} tag and is
 * intended to enclose one or more {@code UnitCellSearchField} tags. This tag
 * does not expose any user interface functionality itself but merely serves to
 * group one or more {@code UnitCellSearchField}'s. The nested
 * {@code UnitCellSearchField}'s expose various user interface controls and
 * report their values to this tag, then this tag collects the reported search
 * criteria and may generate a {@code UnitCellSC}, then the {@code UnitCellSC}
 * is passed up to the nearest enclosing {@code SearchPage} and gets appended to
 * the set of criteria.
 * </p><p>
 * Certain combinations of {@code UnitCellSearchField} values are not usable as
 * criteria for a unit cell or reduced cell search. (See {@code UnitCellSC} for
 * the precise requirements.) This tag implements the {@code ErrorSupplier}
 * interface and defines a number of error flags. One or more error flags may be
 * set in the event of an invalid combination of {@code UnitCellSearchField}
 * values. A validation failure is reported to the nearest enclosing
 * {@code ValidationContext} tag (if any) in such a case also.
 * </p>
 */
public class UnitCellSearchFieldGroup extends HtmlPageElement implements
        ErrorSupplier {
    /*
     * Below are possible error flags that are caused by values not being
     * specified in fields when required.
     */
    public static final int A_MISSING = 1 << 0;

    public static final int B_MISSING = 1 << 1;

    public static final int C_MISSING = 1 << 2;

    public static final int ALPHA_MISSING = 1 << 3;

    public static final int BETA_MISSING = 1 << 4;

    public static final int GAMMA_MISSING = 1 << 5;

    public static final int PERCENT_ERROR_TOLERANCE_MISSING = 1 << 6;

    public static final int MODE_MISSING = 1 << 7;

    public static final int CENTERING_MISSING = 1 << 8;

    /** Allows subclases to extend ErrorSupplier */
    protected static int getHighestErrorFlag() {
        return CENTERING_MISSING;
    }

    /**
     * A reference to the nearest enclosing {@code SearchPage} tag. Set by
     * onRegistrationPhaseBeforeBody().
     */
    private SearchPage searchPage;

    /**
     * A reference to the nearest enclosing {@code ValidationContext} tag, or
     * null if there is not one. Set by {@code onRegistrationPhaseBeforeBody()}.
     */
    private ValidationContext validationContext;

    /** Used to implement {@code ErrorSupplier}. */
    private int errorCode;

    /**
     * The search constraint object presently being constructed or displayed.
     * May be null if there is none. Set by {@code onParsingPhaseAfterBody()} if
     * a constraint is being constructed or {@code onFetchingPhaseBeforeBody()}
     * if a constraint is being displayed.
     */
    private UnitCellSC searchConstraint;

    /**
     * Stores field values that were reported to this tag by nested
     * {@code UnitCellSearchField} tags. Appended to by
     * {@code setValueForField()}.
     */
    private Map<UnitCellSearchField.FieldCode, Object> reportedValues;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.searchPage = null;
        this.validationContext = null;
        this.errorCode = NO_ERROR_REPORTED;
        this.searchConstraint = null;
        this.reportedValues = new HashMap<UnitCellSearchField.FieldCode, Object>();
    }

    /**
     * Implements {@code ErrorSupplier}.
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /** Implements {@code ErrorSupplier}. */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
        if ((errorCode != NO_ERROR_REPORTED) && (this.validationContext != null)) {
            validationContext.reportValidationError();
        }
    }

    /**
     * One of the two methods that nested {@code UnitCellSearchField} tags
     * invoke when communicating with this tag. The method is expected to be
     * invoked during FETCHING_PHASE. The value returned is extracted from the
     * {@code UnitCellSC} that was fetched from the {@code SearchPage}, that
     * presumably would have been constructed during a previous HTTP round-trip.
     * 
     * @return an appropriate value for the named field (type {@code Double} or
     *         type {@code Boolean}, depending upon {@code fieldCode}), or
     *         null if no {@code UnitCellSC} was fetched or if the fetched
     *         constraint contains no value for the specified field.
     * @param fieldCode identifies the intended use of the answer sought. Valid
     *        values are defined by {@code UnitCellSearchField.FieldCode}.
     * @throws IllegalArgumentException if {@code fieldCode} is not a valid
     *         value.
     */
    public Object fetchValueForNestedField(
            UnitCellSearchField.FieldCode fieldCode) {
        if (this.searchConstraint == null) {
            return null;
        }

        switch (fieldCode) {
            case A:
                return this.searchConstraint.getA();
            case B:
                return this.searchConstraint.getB();
            case C:
                return this.searchConstraint.getC();
            case ALPHA:
                return this.searchConstraint.getAlpha();
            case BETA:
                return this.searchConstraint.getBeta();
            case GAMMA:
                return this.searchConstraint.getGamma();
            case PERCENT_ERROR_TOLERANCE:
                return this.searchConstraint.getPercentErrorTolerance();
            case MATCH_UNIT_CELLS_AS_ENTERED:
                return Boolean.valueOf(this.searchConstraint.getMode()
                        == UnitCellSC.Mode.MATCH_UNIT_CELLS_AS_ENTERED);
            case MATCH_REDUCED_CELLS:
                return Boolean.valueOf(this.searchConstraint.getMode()
                        == UnitCellSC.Mode.MATCH_REDUCED_CELLS);
            case P_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.PRIMITIVE);
            case I_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.BODY);
            case F_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.ALL_FACES);
            case A_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.A_FACE);
            case B_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.B_FACE);
            case C_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.C_FACE);
            case R_CENTERING:
                return Boolean.valueOf(this.searchConstraint.getCentering()
                        == SampleMathBL.CellCentering.RHOMBOHEDRAL);
        }

        /*
         * can't reach here because the switch enumerates all the possible cases
         * and returns from each
         */
        assert false;
        return null;
    }

    /**
     * Another of the two methods that nested {@code UnitCellSearchField} tags
     * invoke when communicating with this tag. This method allows them to
     * report a value they parsed from the user. This method is expected to be
     * invoked on PARSING_PHASE.
     * 
     * @param fieldCode identifies the kind of value that was parsed. Valid
     *        values are defined by {@code UnitCellSearchField.FieldCode}.
     * @param value either a {@code Double} or a {@code Boolean}, depending
     *        upon {@code fieldCode}, or null if no value was parsed for the
     *        field.
     */
    public void reportParsedValueForNestedField(
            UnitCellSearchField.FieldCode fieldCode, Object value) {
        this.reportedValues.put(fieldCode, value);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code SearchPage} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(@SuppressWarnings("hiding")
    PageContext pageContext) throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // Bind to the nearest SearchPage.
        this.searchPage = findRealAncestorWithClass(this, SearchPage.class);
        if (this.searchPage == null) {
            throw new IllegalStateException();
        }

        // Bind to the nearest ValidationContext, if any.
        this.validationContext = findRealAncestorWithClass(this,
                ValidationContext.class);

        return rc;
    }

    /**
     * {@inheritDoc}; this version reviews the field values reported during
     * previous invocations of {@code reportParsedValueForNestedField()}, does
     * some math, and populates {@code searchConstraint} if it can.
     */
    @Override
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {
        // Retrieve criteria that may have been passed to us previously by
        // nested tags.
        Double a = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.A);
        Double b = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.B);
        Double c = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.C);
        Double alpha = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.ALPHA);
        Double beta = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.BETA);
        Double gamma = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.GAMMA);
        Double percentErrorTolerance = (Double) this.reportedValues.get(
                UnitCellSearchField.FieldCode.PERCENT_ERROR_TOLERANCE);
        Boolean matchUnitCellsAsEntered = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.MATCH_UNIT_CELLS_AS_ENTERED);
        Boolean matchReducedCells = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.MATCH_REDUCED_CELLS);
        Boolean pCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.P_CENTERING);
        Boolean iCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.I_CENTERING);
        Boolean fCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.F_CENTERING);
        Boolean aCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.A_CENTERING);
        Boolean bCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.B_CENTERING);
        Boolean cCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.C_CENTERING);
        Boolean rCentering = (Boolean) this.reportedValues.get(
                UnitCellSearchField.FieldCode.R_CENTERING);

        if ((a != null) || (b != null) || (c != null) || (alpha != null)
                || (beta != null) || (gamma != null)) {
            // At least one of the empty-by-default fields was populated, so
            // apparently the user expects us to do *something*.

            if (matchUnitCellsAsEntered == Boolean.TRUE) {
                // MATCH_UNIT_CELLS_AS_ENTERED mode selected.

                if (percentErrorTolerance == null) {
                    // The user specified at least one cell dimension but did
                    // not specify an error tolerance. We can't search.
                    setErrorFlag(PERCENT_ERROR_TOLERANCE_MISSING);
                } else {
                    // The information we have is sufficient. Create a search
                    // constraint.
                    this.searchConstraint = new UnitCellSC(
                            UnitCellSC.Mode.MATCH_UNIT_CELLS_AS_ENTERED, a, b,
                            c, alpha, beta, gamma, percentErrorTolerance, null,
                            false, false, false);
                }
            } else if (matchReducedCells == Boolean.TRUE) {
                // MATCH_REDUCED_CELLS mode selected.

                // Validate the presence of most fields.
                if (a == null) {
                    setErrorFlag(A_MISSING);
                }
                if (b == null) {
                    setErrorFlag(B_MISSING);
                }
                if (c == null) {
                    setErrorFlag(C_MISSING);
                }
                if (alpha == null) {
                    setErrorFlag(ALPHA_MISSING);
                }
                if (beta == null) {
                    setErrorFlag(BETA_MISSING);
                }
                if (gamma == null) {
                    setErrorFlag(GAMMA_MISSING);
                }
                if (percentErrorTolerance == null) {
                    setErrorFlag(PERCENT_ERROR_TOLERANCE_MISSING);
                }

                // Validate the presence of the centering choices.
                SampleMathBL.CellCentering centering;
                if (pCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.PRIMITIVE;
                } else if (iCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.BODY;
                } else if (fCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.ALL_FACES;
                } else if (aCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.A_FACE;
                } else if (bCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.B_FACE;
                } else if (cCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.C_FACE;
                } else if (rCentering == Boolean.TRUE) {
                    centering = SampleMathBL.CellCentering.RHOMBOHEDRAL;
                } else {
                    setErrorFlag(CENTERING_MISSING);
                    centering = null;
                }

                if (getErrorCode() == NO_ERROR_REPORTED) {
                    /*
                     * The information we have is sufficient. Create a search
                     * constraint. We always match for reduced cell lengths,
                     * reduced reciprocal cell lengths, and reduced volumes
                     * because these parameters are not user-configurable.
                     */
                    this.searchConstraint = new UnitCellSC(
                            UnitCellSC.Mode.MATCH_REDUCED_CELLS, a, b, c,
                            alpha, beta, gamma, percentErrorTolerance,
                            centering, true, true, true);
                }
            }

            /*
             * Not sure what mode to the user expected us to use. We shouldn't
             * get here if the UI works properly.
             */
            if (this.searchConstraint == null) {
                setErrorFlag(MODE_MISSING);
            }
        }

        return super.onParsingPhaseAfterBody(request);
    }

    /**
     * {@inheritDoc}; this version consults the enclosing {@code SearchPage}
     * and fetches the first {@code UnitCellSC} it finds within the set of
     * constraints.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        /*
         * TODO: A more sophisticated interface with SearchPage and probably a
         * better lookup algorithm will be needed in the future to support more
         * complicated arrangements of search constraints. This is pending a new
         * API on SearchPage.
         */
        int rc = super.onFetchingPhaseBeforeBody();
        if (this.searchConstraint == null) {
            SearchConstraintGroup scg = this.searchPage.getSearchConstraintGroup();
            if (scg != null) {
                for (SearchConstraint sc : scg.getChildren()) {
                    if (sc instanceof UnitCellSC) {
                        this.searchConstraint = (UnitCellSC) sc;
                        break;
                    }
                }
            }
        }
        return rc;
    }

    /**
     * {@inheritDoc}; this version passes the constructed
     * {@code SearchConstraint}, if any, up to the {@code SearchPage} for
     * inclusion in the list of search criteria.
     */
    @Override
    public int onProcessingPhaseAfterBody(
            @SuppressWarnings("hiding") PageContext pageContext)
            throws JspException {
        if (this.searchConstraint != null) {
            this.searchPage.addSearchConstraint(this.searchConstraint);
        }
        return super.onProcessingPhaseAfterBody(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        UnitCellSearchFieldGroup x
                = (UnitCellSearchFieldGroup) super.generateCopy(newId, map);
        
        x.searchPage = (SearchPage) map.get(this.searchPage);
        if (this.validationContext != null) {
            x.validationContext
                    = (ValidationContext) map.get(this.validationContext);
        }
        
        return x;
    }
}
