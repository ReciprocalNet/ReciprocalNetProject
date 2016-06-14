/*
 * Reciprocal Net Project
 *
 * ChemicalFormulaBLManualTests.java
 *
 * 03-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.site.shared.bl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;

import org.recipnet.common.Element;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.shared.bl.ChemicalFormulaBL.ChemicalFormula;

/**
 * TODO Write type description
 * 
 * @author jobollin
 * @version 1.0
 */
public class ChemicalFormulaBLManualTests {

    /**
     * TODO Write method description
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        BufferedReader in
                = new BufferedReader(new InputStreamReader(System.in));
        
        for (;; System.out.println()) {
            ChemicalFormula formula;
            String formulaString;
            boolean canonicalize;

            System.out.print("Input formula: ");
            formulaString = in.readLine();

            if (formulaString.length() == 0) {
                break;
            }
            
            System.out.print("Input formula: ");
            System.out.println(formulaString);
            
            try {
                formula = ChemicalFormulaBL.parseFormula(formulaString);
            } catch (InvalidDataException ide) {
                System.out.print("The formula cannot be parsed: ");
                System.out.print(ide.getMessage());
                continue;
            }
            
            System.out.print("The parsed formula's String representation is '");
            System.out.print(ChemicalFormulaBL.getFormulaString(formula));
            System.out.println('\'');
            
            System.out.print("It is ");
            if (!ChemicalFormulaBL.isValidEmpiricalFormula(formula)) {
                System.out.print("not ");
            }
            System.out.println("a valid empirical formula.");
            
            System.out.print("It is ");
            if (!ChemicalFormulaBL.isValidMoietyFormula(formula)) {
                System.out.print("not ");
                canonicalize = false;
            } else {
                canonicalize = true;
            }
            
            System.out.println("a valid moiety formula.");
            
            if (canonicalize) {
                System.out.print("Its canonical String representation is '");
                System.out.print(ChemicalFormulaBL.getFormulaString(
                        ChemicalFormulaBL.getCanonicalFormula(formula)));
                System.out.println('\'');
            } else {
                System.out.println(
                        "Its canonical String representation is not available");
            }
            
            System.out.print("Its total atom counts are: ");
            try {
                for (Map.Entry<Element, BigDecimal> entry
                        : ChemicalFormulaBL.getAtomCounts(formula, false).entrySet()) {
                    System.out.print(entry.getKey().getSymbol());
                    System.out.print(entry.getValue().toPlainString());
                    System.out.print(' ');
                }
            } catch (IllegalArgumentException iae) {
                System.out.println("<not available>");
            }
        }
    }

}
