/*
 * Reciprocal Net project
 *
 * EmpiricalFormulaValidator.java
 * 
 * 11-Nov-2005: midurbin wrote first draft
 */

package org.recipnet.site.shared.validation;

import org.recipnet.common.Validator;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.ChemicalFormulaBL;

/**
 * Implements {@code Validator} to validate empirical formulae.  All of
 * the validation logic is in {@link ChemicalFormulaBL}.
 */
public class EmpiricalFormulaValidator implements Validator {

    /**
     * Tests the validity of the empirical formula.
     *
     * @param obj an empirical formula, ({@code String})
     *
     * @return true if it is valid, otherwise false
     *
     * @throws NullPointerException if obj is {@code null}
     */
    public boolean isValid(Object obj) {
        if (obj instanceof String) {
            try {
                return ChemicalFormulaBL.isValidEmpiricalFormula(
                    ChemicalFormulaBL.parseFormula((String) obj));
            } catch (InvalidDataException ex) {
                return false;
            }
        } else if (obj == null) {
            throw new NullPointerException();
        } else {
            return false;
        }
    }
}

