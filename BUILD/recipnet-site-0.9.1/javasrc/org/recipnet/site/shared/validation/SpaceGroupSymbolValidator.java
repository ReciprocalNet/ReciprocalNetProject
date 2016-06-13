/*
 * Reciprocal Net project
 * 
 * SpaceGroupSymbolValidator.java
 * 
 * 11-Jul-2004: midurbin wrote first draft
 * 27-Apr-2006: jobollin fixed bug #1782 in isValid()
 */

package org.recipnet.site.shared.validation;

import org.recipnet.common.Validator;
import org.recipnet.site.shared.bl.SpaceGroupSymbolBL;

/**
 * Implements <code>Validator</code> to validate space group symbols.  All of
 * the validation logic is in {@link
 * org.recipnet.site.shared.bl.SpaceGroupSymbolBL SpaceGroupSymbolBL}.
 */
public class SpaceGroupSymbolValidator implements Validator {

    /**
     * Tests the validity of the space group symbol.
     * @param obj a space group symbol, (<code>String</code>)
     * @return true if it is valid, otherwise false
     * @throws NullPointerException if obj is null
     */
    public boolean isValid(Object obj) {
        if (obj instanceof String) {
            String sg = (String) obj;
            
            return "".equals(sg.trim()) || SpaceGroupSymbolBL.isSymbolValid(
                    SpaceGroupSymbolBL.createFormattedSymbol(sg));
        } else if (obj == null) {
            throw new NullPointerException();
        } else {
            return false;
        }
    }
}
