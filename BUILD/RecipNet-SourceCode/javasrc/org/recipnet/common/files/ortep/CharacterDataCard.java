/*
 * Reciprocal Net Project
 *
 * CharacterDataCard.java
 *
 * Dec 12, 2005: jobollin wrote first draft
 */

package org.recipnet.common.files.ortep;

import java.util.Formatter;

/**
 * An {@code OrtepInstructionCard} implementation for ORTEP type trailer cards,
 * which bear character data such as atom and bond labels
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class CharacterDataCard implements OrtepInstructionCard {
    
    /**
     * The character data represented by this object
     */
    final String characterData;

    /**
     * Initializes a {@code CharacterDataCard} with the specified string of
     * character data
     * 
     * @param  charData a {@code String} representing the character data of this
     *         card; only the first 72 characters are significant
     */
    public CharacterDataCard(String charData) {
        characterData = (charData.length() <= 72)
                ? charData : charData.substring(0, 72);
    }
    
    /**
     * An implementation method of the {@code OrtepInstructionCard} interface;
     * returns the type code of this card.
     * 
     * @return this card's type code (3)
     *  
     * @see OrtepInstructionCard#getCardType()
     */
    public int getCardType() {
        return 3;
    }

    /**
     * Returns the character data string assigned to this card
     * 
     * @return the character data assigned to this card, as a {@code String};
     *         will not be {@code null} or have length greater than 72
     *         characters
     */
    public String getCharacterData() {
        return characterData;
    }
    
    /**
     * An implementation method of the {@code OrtepInstructionCard} interface.
     * Outputs a formatted representation of this card to the specified
     * formatter, inserting the specified look-ahead code.
     * 
     * @param  formatter the {@code Formatter} to which the output should be
     *         directed
     * @param  nextCardType the card type code for the card following this one;
     *         must be 0 for this card type
     *         
     * @throws IllegalArgumentException if the next card type code is
     *         invalid, which in this case means that it is nonzero
     * 
     * @see OrtepInstructionCard#formatTo(Formatter, int)
     */
    public void formatTo(Formatter formatter, int nextCardType) {
        if (nextCardType != 0) {
            throw new IllegalArgumentException(
                    "Format 3 cards require a next card of type 0");
        }
        
        formatter.format((characterData == null) ? "%n" : "%s%n",
                characterData);
    }
}
