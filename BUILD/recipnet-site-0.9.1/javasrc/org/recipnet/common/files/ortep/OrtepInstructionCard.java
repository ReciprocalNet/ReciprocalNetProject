/*
 * Reciprocal Net Project
 *
 * OrtepInstructionCard.java
 *
 * Dec 12, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.ortep;

import java.util.Formatter;

/**
 * <p>
 * An interface describing classes that represent ORTEP instruction "cards"
 * (formatted logical lines).  ORTEP defines four distinct card formats; see
 * <i>ORTEP-III: Oak Ridge Thermal Ellipsoid Plot Program for Crystal Structure
 * Illustrations</i>, ORNL-6895 by Carroll K. Johnson and Michael N. Burnett,
 * page 31.
 * </p><p>
 * Implementations can report the card type they represent and can format their
 * content to a provided {@code Formatter} object.
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public interface OrtepInstructionCard {
    
    /**
     * Obtains the card type code for this {@code OrtepInstructionCard}; the
     * currently defined codes are the integers between zero and three,
     * inclusive.
     * 
     * @return the card type code for this {@code OrtepInstructionCard}
     */
    int getCardType();
    
    /**
     * Produce a formatted version of this card's data content on the specified
     * {@code Formatter}, in the appropriate standard ORTEP format for this
     * card's type.
     * 
     * @param  formatter the {@code Formatter} to which the output should be
     *         directed
     * @param  nextCardType the card type code of the <i>next</i> card to be
     *         formatted after this one, for use in outputting a look-ahead code
     *         
     * @throws IllegalArgumentException if the look-ahead code is invalid
     */
    void formatTo(Formatter formatter, int nextCardType);
}