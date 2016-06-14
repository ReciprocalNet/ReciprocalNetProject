/*
 * Reciprocal Net Project
 *
 * ExtraParameterCard.java
 *
 * Dec 12, 2005: jobollin wrote first draft
 */
package org.recipnet.common.files.ortep;

import java.util.Formatter;

/**
 * An {@code OrtepInstructionCard} implementation representing ORTEP type 1 and
 * type 2 (extra parameter) cards.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ExtraParameterCard extends ParameterBearingCard {
    
    /**
     * The card type of this card (1 or 2)
     */
    final int cardType;

    /**
     * Initializes a {@code ExtraParameterCard} with the specified type code
     * and parameter string
     * 
     * @param  type the card type for this card, either 1 or 2
     * @param  parms the parameter {@code String} for this card; should be
     *         formatted according to ORTEP conventions; may be {@code null} if
     *         there are no parameters
     */
    public ExtraParameterCard(int type, String parms) {
        super(parms);
        
        if ((type < 1) || (type > 3)) {
            throw new IllegalArgumentException(
                    "Invalid card type for an extra parameter card: " + type);
        } else {
            cardType = type;
        }
    }
    
    /**
     * An implementation method of the {@code OrtepInstructionCard} interface;
     * returns the type code of this card.
     * 
     * @return this card's type code (1 or 2)
     *  
     * @see OrtepInstructionCard#getCardType()
     */
    public int getCardType() {
        return cardType;
    }

    /**
     * Outputs the appropriate data for this card to the "instruction field";
     * in this case, the instruction field is filled with blanks.
     * 
     * @see ParameterBearingCard#formatInstructionField(Formatter)
     */
    @Override
    protected void formatInstructionField(Formatter formatter) {
        formatter.format("      ");
    }
}