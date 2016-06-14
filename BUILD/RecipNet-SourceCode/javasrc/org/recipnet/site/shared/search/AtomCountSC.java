/*
 * Reciprocal Net project
 * 
 * AtomCountSC.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 15-Aug-2005: midurbin added getMatches()
 * 29-Nov-2005: jobollin updated getMatches() to use the new ChemicalFormulaBL
 *              and Element classes, and to use Java's new enhanced for loops;
 *              removed unusued imports
 * 30-May-2006: jobollin reformatted the source, changed the second argument to
 *              getWhereClauseFragment() into a List<Object>
 */

package org.recipnet.site.shared.search;

import java.util.Collection;
import java.util.List;

import org.recipnet.common.Element;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.ChemicalFormulaBL;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.ChemicalFormulaBL.ChemicalFormula;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;

/**
 * A {@code SearchConstraint} that limits a search to those samples that contain
 * the given element in the given quantity on the given formula type. Note: even
 * if the operator is {@code LESS_THAN} or {@code LESS_THAN_OR_EQUAL_TO},
 * samples will not be included if they do not have an entry in the
 * 'searchAtoms' table for the given element and formula type.
 */
public class AtomCountSC extends NumberComparisonSC {

    /** A two-letter atomic symbol. */
    private String atomSymbol;

    /**
     * The operator that relates the provided 'count' with the counts stored in
     * the database; This must be set to one of the constant operator codes
     * defined on {@code NumberComparisonSC}.
     */
    private int operator;

    /** The desired atom count (or atom count limit). */
    private double count;

    /**
     * Indicates whether the given atom count should be matched against
     * {@code EMPIRICAL_FORMULA} attributes.
     */
    private boolean considerEF;

    /**
     * Indicates whether the given atom count should be matched against
     * {@code EMPIRICAL_FORMULA_DERIVED} attributes.
     */
    private boolean considerEFD;

    /**
     * Indicates whether the given atom count should be matched against
     * {@code EMPIRICAL_FORMULA_LESS_SOLVENT} attributes.
     */
    private boolean considerEFLS;

    /**
     * Indicates whether the given atom count should be matched against
     * {@code EMPIRICAL_FORMULA_SINGLE_ION} attributes.
     */
    private boolean considerEFSI;

    /**
     * A constructor that fully initializes a {@code AtomCountSC}.
     * 
     * @param atomSymbol a two-letter element symbol
     * @param count the desired number (or limit) of atoms of the given type
     * @param operator one of the operator codes defined on
     *        {@code NumberComparisonSC}
     * @param considerEF indicates whether the given atom count should be
     *        matched against {@code EMPIRICAL_FORMULA} attributes
     * @param considerEFD indicates whether the given atom count should be
     *        matched against {@code EMPIRICAL_FORMULA_DERIVED} attributes
     * @param considerEFLS indicates whether the given atom count should be
     *        matched against {@code EMPIRICAL_FORMULA_LESS_SOLVENT} attributes
     * @param considerEFSI indicates whether the given atom count should be
     *        matched against {@code EMPIRICAL_FORMULA_SINGLE_ION} attributes
     * @throws IllegalArgumentException if none of the boolean parameters are
     *         true
     */
    public AtomCountSC(String atomSymbol, double count, int operator,
            boolean considerEF, boolean considerEFD, boolean considerEFLS,
            boolean considerEFSI) {
        if (!considerEF && !considerEFD && !considerEFLS && !considerEFSI) {
            throw new IllegalArgumentException();
        }
        this.atomSymbol = atomSymbol;
        this.operator = operator;
        this.count = count;
        this.considerEF = considerEF;
        this.considerEFD = considerEFD;
        this.considerEFLS = considerEFLS;
        this.considerEFSI = considerEFSI;
    }

    /** Gets the operator that was set by the constructor. */
    public int getOperator() {
        return this.operator;
    }

    /** Gets the atom symbol that was set by the constructor. */
    public String getAtomSymbol() {
        return this.atomSymbol;
    }

    /** Gets the atom count that was set by the constructor. */
    public double getCount() {
        return this.count;
    }

    /**
     * Gets a boolean indicating whether the {@code EMPIRICAL_FORMULA} will be
     * considered for atom count matching.
     */
    public boolean getConsiderEF() {
        return this.considerEF;
    }

    /**
     * Gets a boolean indicating whether the {@code EMPIRICAL_FORMULA_DERIVED}
     * will be considered for atom count matching.
     */
    public boolean getConsiderEFD() {
        return this.considerEFD;
    }

    /**
     * Gets a boolean indicating whether the
     * {@code EMPIRICAL_FORMULA_LESS_SOLVENT} will be considered for atom count
     * matching.
     */
    public boolean getConsiderEFLS() {
        return this.considerEFLS;
    }

    /**
     * Gets a boolean indicating whether the
     * {@code EMPIRICAL_FORMULA_SINGLE_ION} will be considered for atom count
     * matching.
     */
    public boolean getConsiderEFSI() {
        return this.considerEFSI;
    }

    /**
     * {@inheritDoc}; this version generates a {@code String}. that may be
     * used as a portion of the SQL WHERE clause to require that the sample have
     * an entry in 'searchAtoms' for the specified 'atomSymbol' that has the
     * specified count.
     */
    @Override
    public String getWhereClauseFragment(SearchTableTracker tableTracker,
            List<Object> parameters, @SuppressWarnings("unused")
            SearchConstraintExtraInfo scei) {
        String tableAlias = tableTracker.getTableAlias("searchAtoms", this);

        StringBuffer formulaTypePart = new StringBuffer();
        int consideredFormulaCount = 0;
        if (this.considerEF) {
            formulaTypePart.append((consideredFormulaCount > 0 ? " OR " : "")
                    + tableAlias + ".type = ?");
            parameters.add(new Integer(SampleTextBL.EMPIRICAL_FORMULA));
            consideredFormulaCount++;
        }
        if (this.considerEFD) {
            formulaTypePart.append((consideredFormulaCount > 0 ? " OR " : "")
                    + tableAlias + ".type = ?");
            parameters.add(new Integer(SampleTextBL.EMPIRICAL_FORMULA_DERIVED));
            consideredFormulaCount++;
        }
        if (this.considerEFLS) {
            formulaTypePart.append((consideredFormulaCount > 0 ? " OR " : "")
                    + tableAlias + ".type = ?");
            parameters.add(new Integer(
                    SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT));
            consideredFormulaCount++;
        }
        if (this.considerEFSI) {
            formulaTypePart.append((consideredFormulaCount > 0 ? " OR " : "")
                    + tableAlias + ".type = ?");
            parameters.add(new Integer(
                    SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION));
            consideredFormulaCount++;
        }
        parameters.add(new Double(count));
        parameters.add(atomSymbol);
        return "(" + (consideredFormulaCount > 1 ? "(" : "") + formulaTypePart
                + (consideredFormulaCount > 1 ? ") AND " : " AND ")
                + tableAlias + ".count "
                + convertOperatorToString(this.operator) + " ? AND "
                + tableAlias + ".element = ? )";
    }

    /**
     * {@inheritDoc}; this version parses each empirical formula that is to be
     * considered in the same way they were parsed to generate the search atoms
     * database table and checks whether any of the counts match the required
     * count. The first match is added to the 'matches' collection, or if no
     * matches are found, false is returned.
     */
    @Override
    public boolean getMatches(SampleInfo sample,
            Collection<FieldMatchInfo> matches, @SuppressWarnings("unused")
            Collection<FieldMatchInfo> mismatches) {
        Element element = Element.forSymbol(getAtomSymbol());

        if (element == null) {
            // Not a valid element symbol
            return false;
        }

        for (SampleAttributeInfo attr : sample.attributeInfo) {
            if (((attr.type == SampleTextBL.EMPIRICAL_FORMULA) && this.considerEF)
                    || ((attr.type == SampleTextBL.EMPIRICAL_FORMULA_DERIVED)
                            && this.considerEFD)
                    || ((attr.type == SampleTextBL.EMPIRICAL_FORMULA_LESS_SOLVENT)
                            && this.considerEFLS)
                    || ((attr.type == SampleTextBL.EMPIRICAL_FORMULA_SINGLE_ION)
                            && this.considerEFSI)) {
                ChemicalFormula formula;
                Number observedCount;

                try {
                    formula = ChemicalFormulaBL.parseFormula(attr.value);
                } catch (InvalidDataException ide) {
                    // Unparseable; just move on to the next attribute
                    continue;
                }
                observedCount
                        = ChemicalFormulaBL.getAtomCounts(formula, false).get(
                                element);

                if (observedCount != null) {
                    double numAtoms = observedCount.doubleValue();

                    switch (this.operator) {
                        case EQUALS:
                            if (numAtoms == this.count) {
                                matches.add(new FieldMatchInfo(attr, this,
                                        sample));
                                return true;
                            }
                            break;
                        case GREATER_THAN:
                            if (numAtoms > this.count) {
                                matches.add(new FieldMatchInfo(attr, this,
                                        sample));
                                return true;
                            }
                            break;
                        case GREATER_THAN_OR_EQUAL_TO:
                            if (numAtoms >= this.count) {
                                matches.add(new FieldMatchInfo(attr, this,
                                        sample));
                                return true;
                            }
                            break;
                        case LESS_THAN:
                            if (numAtoms < this.count) {
                                matches.add(new FieldMatchInfo(attr, this,
                                        sample));
                                return true;
                            }
                            break;
                        case LESS_THAN_OR_EQUAL_TO:
                            if (numAtoms <= this.count) {
                                matches.add(new FieldMatchInfo(attr, this,
                                        sample));
                                return true;
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Equality is based on class, atomSymbol, count, operation and considered
     * formula types.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AtomCountSC) {
            AtomCountSC acsc = (AtomCountSC) obj;

            return ((this.getClass() == acsc.getClass())
                    && (this.count == acsc.count)
                    && (this.operator == acsc.operator)
                    && (this.considerEF == acsc.considerEF)
                    && (this.considerEFD == acsc.considerEFD)
                    && (this.considerEFLS == acsc.considerEFLS)
                    && (this.considerEFSI == acsc.considerEFSI)
                    && (((this.atomSymbol == null) && (acsc.atomSymbol == null))
                            || ((this.atomSymbol != null)
                                    && this.atomSymbol.equals(acsc.atomSymbol))));
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (String.valueOf(getClass())
                + this.atomSymbol
                + String.valueOf(this.count)
                + String.valueOf(this.operator)
                + String.valueOf(this.considerEF)
                + String.valueOf(this.considerEFD)
                + String.valueOf(this.considerEFLS)
                + String.valueOf(this.considerEFSI)).hashCode();
    }
}
