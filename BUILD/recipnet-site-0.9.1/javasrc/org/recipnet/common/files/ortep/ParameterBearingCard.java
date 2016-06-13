/*
 * Reciprocal Net Project
 *
 * ParameterBearingCard.java
 *
 * Dec 12, 2005: jobollin wrote first draft
 */
package org.recipnet.common.files.ortep;

import java.util.Formatter;

/**
 * An abstract base implementation of {@code OrtepInstructionCard} appropriate
 * for all card types except type 3.  The supported card types all provide a
 * look-ahead code in the same part of the line and a parameter list starting
 * at the same place on the line.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public abstract class ParameterBearingCard implements OrtepInstructionCard {
    
    /**
     * The parameters borne by this card, as a plain {@code String}
     */
    final String parameters;
    
    /**
     * Initializes a {@code ParameterBearingCard} with the specified parameter
     * string
     * 
     * @param  parms the parameter {@code String} for this card; should be
     *         formatted according to ORTEP conventions; may be {@code null} if
     *         there are no parameters
     */
    protected ParameterBearingCard(String parms) {
        parameters = parms;
    }
    
    /**
     * Obtains the parameters carried by this card, as a plain string
     * 
     * @return a {@code String} containing the parameters carried by this
     *         card; may be {@code null} if there are no parameters (or
     *         alternatively, empty or blank)
     */
    public String getParameters() {
        return parameters;
    }
    
    /**
     * An implementation method of the {@code OrtepInputCard} interface.
     * Instructs this card to output its data content in ORTEP format via the
     * specified formatter, assuming the specified next card type for producing
     * a look-ahead code. 
     * 
     * @param  formatter the {@code Formatter} to which the output should be
     *         directed
     * @param  nextCardType the type code to assume for the next card to be
     *         output after this one 
     *         
     * @throws IllegalArgumentException if the look-ahead code is invalid, which
     *         in this case means less then zero or greater than three
     */
    public void formatTo(Formatter formatter, int nextCardType) {
        
        // Output the look-ahead code
        if ((nextCardType < 0) || (nextCardType > 3)) {
            throw new IllegalArgumentException("Invalid look ahead code: "
                    + nextCardType);
        } else if (nextCardType == 0) {
            formatter.format("   ");
        } else {
            formatter.format("%3d", nextCardType);
        }
        
        // output the instruction field
        formatInstructionField(formatter);
        
        // output the parameters, if any
        formatter.format((parameters == null) ? "%n" : "%s%n",
                parameters);
        
    }

    /**
     * Invoked by {@link #formatTo(Formatter, int)} to outputs the appropriate
     * data for this card to the "instruction field" (the fourth through ninth
     * characters) of the formatted representation of this card.  Concrete
     * subclasses must implement this method appropriately.
     * 
     * @param  formatter the {@code Formatter} to which output should be
     *         directed
     */
    protected abstract void formatInstructionField(Formatter formatter);
}