/*
 * Reciprocal Net Project
 *
 * OrtFile.java
 *
 * 21-Nov-2005: jobollin wrote first draft
 */

package org.recipnet.common.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.recipnet.common.files.ortep.CharacterDataCard;
import org.recipnet.common.files.ortep.ExtraParameterCard;
import org.recipnet.common.files.ortep.NewInstructionCard;
import org.recipnet.common.files.ortep.OrtepInstructionCard;

/**
 * <p>
 * A class representing an ORTEP input file stub, containing drawing
 * instructions but not the actual model.  Reciprocal Net convention assigns
 * the extension ".ort" to files of this type.  This class provides a mechanism
 * for reading and writing such files.
 * </p><p>
 * Structurally, this class is a fairly thin wrapper around a list of
 * {@link OrtepInstructionCard} objects.  Card types and instruction numbers are
 * accessible via these objects; parameters are available as well, but only as
 * a single opaque {@code String} per card.
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class OrtFile {
    
    /**
     * The sequence of ORTEP instructions comprised by this {@code OrtFile}
     */
    private final List<OrtepInstructionCard> instructions
            = new ArrayList<OrtepInstructionCard>();
    
    /**
     * Adds a new instruction to the end of this OrtFile's list of instructions.
     * This operation is sensible only when initially configuring an instance:
     * an instance read from a file is likely to already include an exit
     * instruction, making addition of further instructions unavailing.
     *  
     * @param  instruction the {@code OrtepInstructionCard} to add
     */
    public void addInstruction(OrtepInstructionCard instruction) {
        instructions.add(instruction);
    }
    
    /**
     * Returns an unmodifiable view of the ORTEP instructions comprised by this
     * {@code OrtFile}
     * 
     * @return an unmodifiable {@code List&lt;OrtepInstructionCard&gt;} view of
     *         the ORTEP instructions comprised by this {@code OrtFile}
     */
    public List<OrtepInstructionCard> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }
    
    /**
     * Outputs the ORTEP instructions comprised by this {@code OrtFile} via the
     * specified formatter, making use of the individual
     * {@link OrtepInstructionCard}s'
     * {@link OrtepInstructionCard#formatTo(Formatter, int)} method; the
     * formatter is flushed at the end of this operation, but not closed.
     * 
     * @param  formatter the {@code Formatter} with which to output the ORTEP
     *         instructions
     *          
     * @throws IOException if an I/O error occurs
     */
    public void formatTo(Formatter formatter) throws IOException {
        
        /*
         * This method needs a two-card window on the instruction list so that
         * when formatting an instruction card, it can correctly specify the
         * type of the next card 
         */
        
        Iterator<OrtepInstructionCard> it = instructions.iterator();
        OrtepInstructionCard instruction = it.next();
        
        while (it.hasNext()) {
            OrtepInstructionCard nextInstruction = it.next();
            
            instruction.formatTo(formatter, nextInstruction.getCardType());
            instruction = nextInstruction;
        }
        
        // The last card specifies type 0 for its (non-existent) next card 
        instruction.formatTo(formatter, 0);
        
        // Finish up by flushing the formatter and checking for an exception
        formatter.flush();
        if (formatter.ioException() != null) {
            throw formatter.ioException();
        }
    }
    
    /**
     * Writes the ORTEP instructions represented by this {@code OrtFile} to the
     * specified writer, as if by creating a {@code Formatter} around the writer
     * and passing it to {@link #formatTo(Formatter)} 
     * 
     * @param  writer the {@code Writer} to which the output should be directed
     * 
     * @throws IOException if an {@code I/O error occurs}
     */
    public void writeTo(Writer writer) throws IOException {
        formatTo(new Formatter(writer, Locale.US));
        writer.flush();
    }
    
    /**
     * Reads ORTEP instructions from the specified character stream and creates
     * and returns an {@code OrtFile} object representing them.  Only the
     * instructions (not the model) should be available from the stream.  Only
     * limited validation of the data is performed.  Instructions are read
     * until the end of the stream or until an ORTEP exit instruction
     * (instruction code -1) is read.
     * 
     * @param  r the {@code Reader} from which the ORTEP instruction data should
     *         be read 
     * 
     * @return an {@code OrtFile} comprising the ORTEP instructions read from
     *         the reader
     * 
     * @throws IOException if an I/O error occurs
     * @throws ParseException if a malformation of the input ORTEP instructions
     *         is detected
     */
    public static OrtFile readFrom(Reader r) throws IOException,
            ParseException {
        BufferedReader in = new BufferedReader(r);
        OrtFile ort = new OrtFile();
        int nextCardType = 0;
        
        /*
         * Read one line at a time until end of stream or an exit instruction
         * (instruction code -1)
         */
        cards:
        for (String card = in.readLine(); card != null; card = in.readLine()) {
            try {
                String lookAheadString;
                
                /*
                 * Interpret the line based on the expected card type read from
                 * the previous line
                 */
                switch (nextCardType) {
                    case 0:  // A new instruction card is expected
                        if (card.length() < 9) {
                            throw new ParseException(
                                    "Truncated instruction code field",
                                    card.length());
                        }
                        
                        int instructionCode
                                = Integer.parseInt(card.substring(3, 9).trim());
                                
                        lookAheadString = card.substring(0, 3).trim();
                        ort.addInstruction(new NewInstructionCard(
                                instructionCode, card.substring(9)));
                        nextCardType = (lookAheadString.length() > 0) ?
                                Integer.parseInt(lookAheadString) : 0;
                        
                        if (instructionCode == -1) {
                            break cards;
                        }
                        
                        break;
                    case 1:  // A type-1 extra parameter card is expected
                        /* fall through */
                    case 2:  // A type-2 extra parameter card is expected
                        if (card.length() < 3) {
                            throw new ParseException(
                                    "Truncated look ahead code field",
                                    card.length());
                        }
                        
                        lookAheadString = card.substring(0, 3).trim();
                        ort.addInstruction(new ExtraParameterCard(
                                nextCardType, card.substring(9)));
                        nextCardType = (lookAheadString.length() > 0) ?
                                Integer.parseInt(lookAheadString) : 0;
                        break;
                    case 3:  // A character data card is expected
                        ort.addInstruction(new CharacterDataCard(card));
                        nextCardType = 0;
                        break;
                    default:

                        /*
                         * Should not happen: the nextCardType value is
                         * initially valid (0) and is tested (below) for
                         * validity after each change
                         */
                        assert false;
                }
                
                // Check for a valid look-ahead code
                if ((nextCardType < 0) || (nextCardType > 3)) {
                    throw new ParseException(
                            "Bad look-ahead on ORTEP instruction card: "
                            + card, 0);
                }
            } catch (NumberFormatException nfe) {
                
                /*
                 * a look-ahead flag or instruction code failed to be parsable
                 * as an integer
                 */
                
                throw new ParseException("Ill-formed ORTEP instruction card: "
                        + card, 0);
            }
        }
        
        // Return the constructed OrtFile
        return ort;
    }
}
