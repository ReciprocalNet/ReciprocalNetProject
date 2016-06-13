/*
 * Reciprocal Net Project
 *
 * NewInstructionCard.java
 *
 * Dec 12, 2005: jobollin wrote first draft
 */
package org.recipnet.common.files.ortep;

import java.util.Formatter;

/**
 * An {@code OrtepInstructionCard} implementation representing ORTEP type 0
 * (new instruction) instruction cards.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class NewInstructionCard extends ParameterBearingCard {
    
    /**
     * The instruction code for this card, as an {@code Integer}
     */
    final Integer instruction;
    
    /**
     * Initializes a {@code NewInstructionCard} with the specified instruction
     * number and parameter string.
     * 
     * @param  instr the instruction number for this card; an {@code int}
     *         between -9 and 9999, inclusive.  (Though only a few numbers in
     *         that range are currently used by ORTEP.)
     * @param  parms the parameter {@code String} for this card; should be
     *         formatted according to ORTEP conventions; may be {@code null} if
     *         there are no parameters
     */
    public NewInstructionCard(int instr, String parms) {
        super(parms);
        
        /*
         * This range is rather greater than required by the current version of
         * ORTEP:
         */
        if ((instr < -9) || (instr > 9999)) {
            throw new IllegalArgumentException(
                    "Instruction number out of range: " + instr);
        } else {
            instruction = instr;
        }
    }
    
    /**
     * An implementation method of the {@code OrtepInstructionCard} interface;
     * returns the type code of this card.
     * 
     * @return this card's type code (0)
     *  
     * @see OrtepInstructionCard#getCardType()
     */
    public int getCardType() {
        return 0;
    }
    
    /**
     * Returns the instruction code represented by this
     * {@code NewInstructionCard}
     * 
     * @return the instruction code for this card
     */
    public int getInstruction() {
        return instruction.intValue();
    }
    
    /**
     * Outputs the appropriate data for this card to the "instruction field";
     * in this case, this card's instruction code is formatted as an integer,
     * right-justified in the field.
     * 
     * @see ParameterBearingCard#formatInstructionField(Formatter)
     */
    @Override
    protected void formatInstructionField(Formatter formatter) {
        formatter.format("%6d", instruction);
    }
}