/*
 * Reciprocal Net Project
 *
 * ChemicalFormulaBL.java
 *
 * 31-Oct-2005: jobollin wrote first draft
 * 11-Jan-2005: jobollin fixed bug #1718 in ChemicalFormulaBL.parseCharge()
 * 20-Nov-2006: jobollin updated javadoc tags where javadoc didn't understand
 *              references to nested classes
 */

package org.recipnet.site.shared.bl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.recipnet.common.Element;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.UnexpectedExceptionException;

/**
 * <p>
 * A class containing methods for parsing and formatting chemical formula
 * strings, computing formula weights, and assisting with atom count indexing
 * and searching.  The nested {@code ChemicalFormula} class is a key piece of
 * this logic, as it, with its associated classes and interfaces, provides a
 * structured representation of a generic chemical formula.
 * {@code ChemicalFormulaBL} provides {@link #parseFormula(String)} a method for
 * obtaining {@code ChemicalFormula} objects from string representations},
 * but the remaining methods require {@code ChemicalFormula} objects as
 * arguments.
 * </p><p>
 * Perhaps the most straightforward way to examine or manipulate a
 * {@code ChemicalFormula} is by an implementation of the
 * {@link ChemicalFormulaVisitor} interface, especially a
 * {@link FormulaWalker} subclass.  {@code FormulaWalker} handles navigation
 * through the parts of a chemical formula, and subclasses can leverage it by
 * implementing only the logic specific to particular types of chemical formula
 * parts.  (This technique is used internally by most of the public methods.)
 * </p>
 * 
 * @author jobollin
 * @version 0.9.0
 */
public class ChemicalFormulaBL {
    
    /**
     * A regex pattern string for an element symbol or general chemical
     * abbreviation such as "tol", "DMF", "Me", or "tBu", including some
     * symbolic ones such as "\u03bc-OEt".  This string does not establish any
     * capturing groups.
     */
    /*
     * Matches a Unicode letter of any category, followed by any sequence of one
     * or more Unicode letters and combining marks and '+' and '-' characters 
     */
    final static String TEXT_SUBPATTERN =
        "[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}]"
        + "[\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Mc}+-]*";
    
    /**
     * A regex pattern string for an unsigned decimal number; does not
     * establish any capturing groups
     */
    final static String NUMBER_SUBPATTERN = "(?:\\d+(?:\\.\\d*)?)|(?:\\.\\d+)";
    
    /**
     * A regex {@code Pattern} matching a "counted component" -- an element
     * symbol or local chemical abbreviation with an associated count
     */
    private final static Pattern COUNTED_ITEM_PATTERN = Pattern.compile(
            "(" + TEXT_SUBPATTERN + ")(" + NUMBER_SUBPATTERN + ")?");
    
    /**
     * A regex {@code Pattern} matching the textual representation of a CHARGE
     * formula token
     */
    private final static Pattern CHARGE_PATTERN = Pattern.compile(
            "(" + NUMBER_SUBPATTERN + ")?([-+])");
    
    /**
     * A {@code Comparator<AtomCount>} that represents lexicographic order by
     * {@code AtomCount} element symbol.
     */
    private final static Comparator<AtomCount> ORDER_BY_SYMBOL
            = new Comparator<AtomCount>() {
        
        /**
         * Compares two {@code AtomCount}s to determine which precedes which in
         * the ordering represented by this comparator
         * 
         * @param  count1 the first {@code AtomCount} to compare
         * @param  count2 the second {@code AtomCount} to compare
         * 
         * @return less than zero if {@code count1} comes before {@code count2},
         *         greater than zero if {@code count2} comes before
         *         {@code count1}, or zero if the two counts are not ordered
         *         relative to each other (which happens if they have the same
         *         atom type per {@code AtomCount.getAtomType()})
         */
        public int compare(AtomCount count1, AtomCount count2) {
            return count1.getAtomType().getSymbol().compareTo(
                    count2.getAtomType().getSymbol());
        }
    };
    
    /**
     * A private constructor with a more restrictive access level than the
     * default constructor would have.  Presence of this constructor without any
     * others prevents this class from being instantiated.
     */
    private ChemicalFormulaBL() {
        // does nothing
    }
    
    /**
     * <p>
     * Creates a canonical version of the specified chemical formula.
     * Canonicalizing a formula involves putting each moiety into Hill
     * format, in which one count is given for each element, and the elements
     * are listed in one of these two orders:
     * </p>
     * <ul>
     * <li><strong>if the moiety contains carbon</strong>, first carbon, then
     * hydrogen, then any remaining elements in alphabetical order by chemical
     * symbol</li>
     * <li><strong>if the moiety does not contain carbon</strong>, all elements
     * in alphabetical order by chemical symbol</li>
     * </ul>
     * <p>
     * This method is not appropriate for general structural formulae,
     * and will in fact throw an exception if invoked with an argument that does
     * not pass the {@link #isValidMoietyFormula(ChemicalFormula)} test.
     * </p> 
     * 
     * @param  formula the {@code ChemicalFormula} for which a canonical version
     *         is requested
     *         
     * @return a new {@code ChemicalFormula}, distinct from the method argument,
     *         representing the same formula but in a canonical form
     *         
     * @throws IllegalArgumentException if {@code formula} is not a valid moiety
     *         formula (a test comprised by the 
     *         {@link #isValidEmpiricalFormula(ChemicalFormula)} test)
     */
    public static ChemicalFormula getCanonicalFormula(ChemicalFormula formula) {
        if (!isValidMoietyFormula(formula)) {
            throw new IllegalArgumentException(
                    "Only valid moiety formulae (including empirical formulae) "
                    + "can be canonicalized");
        } else {
            List<ChemicalFormulaMoiety> moietyList
                    = new ArrayList<ChemicalFormulaMoiety>(); 
            
            // Sort the atom counts in each moiety, building a new moiety list 
            for (ChemicalFormulaMoiety moiety : formula.getMoietyList()) {
                List<AtomCount> counts = new ArrayList<AtomCount>();
                
                /*
                 * sortedList is the List in which the sorted part of the
                 * formula is built.  It is always either the same object as
                 * the counts List, or a subList() of that List. 
                 */
                List<AtomCount> sortedList = counts;
                
                boolean containsCarbon = false;
                AtomCount hydrogenCount = null;
                
                /*
                 * Get the counts and use each one to insert a corresponding
                 * AtomCount into the correct position in the counts list.  The
                 * list is built in correct order from the get-go, excepting
                 * only that the hydrogen count may need to be moved, once.
                 */
                for (Map.Entry<Element, BigDecimal> entry
                        : getAtomCounts(moiety, true).entrySet()) {
                    Element el = entry.getKey();
                    AtomCount count = new AtomCount(el, entry.getValue());
                    
                    /*
                     * Carbon goes at the front if it is present, followed in
                     * that case by Hydrogen
                     */
                    if (el == Element.CARBON) {
                        
                        /*
                         * The two lists would be different objects only if
                         * a previous count for carbon had been seen 
                         */
                        assert (sortedList == counts);

                        // Carbon goes first when present
                        counts.add(0, count);
                        
                        // Carbon is not in the sorted part
                        sortedList = counts.subList(1, counts.size());
                        containsCarbon = true;
                        
                        // Hydrogen is known to be present; move it after Carbon
                        if (hydrogenCount != null) {
                            int index = Collections.binarySearch(
                                    sortedList, hydrogenCount, ORDER_BY_SYMBOL);
                            
                            /*
                             * The index would be less than zero only if no
                             * hydrogen count were actually in the list
                             */
                            assert (index >= 0);

                            // Move the hydrogen count to index 1 in counts
                            if (index > 0) {
                                sortedList.remove(index);
                                counts.add(1, hydrogenCount);
                            }
                            
                            // Carbon and hydrogen are not in the sorted part
                            sortedList = counts.subList(2, counts.size());
                        }
                        
                    /*
                     * If Carbon has already been handled then the position
                     * for hydrogen is exactly prescribed
                     */
                    } else if ((el == Element.HYDROGEN) && containsCarbon) {
                        
                        // Hydrogen goes right after carbon
                        counts.add(1, count);
                        
                        // Carbon and hydrogen are not in the sorted part
                        sortedList = counts.subList(2, counts.size());
                        
                        // For consistency; not necessary in this version
                        hydrogenCount = count;
                        
                    /*
                     * In all other cases, counts are inserted so as to be in
                     * alphabetic order by element symbol (after an initial
                     * C and optional H, if those are present)
                     */   
                    } else {
                        int index = -(1 + Collections.binarySearch(
                                sortedList, count, ORDER_BY_SYMBOL));
                        
                        /*
                         * The index would only be < 0 if a count for the
                         * element was already present in the list
                         */
                        assert (index >= 0);
                        
                        sortedList.add(index, count);
                        
                        /*
                         * Remember this object in case we need to move it later
                         */
                        if (el == Element.HYDROGEN) {
                            hydrogenCount = count;
                        }
                    }
                }
                
                /*
                 * The counts for this moiety have been properly ordered; now
                 * construct a ChemicalFormulaMoiety object reflecting the new
                 * order and add it to the list of canonicalized moieties
                 */
                moietyList.add(new ChemicalFormulaMoiety(
                        moiety.getPreMultiplier(), 
                        new ChemicalFormulaGroup(
                                counts,
                                moiety.getGroup().getCharge(),
                                BigDecimal.ONE)));
            }
            
            /*
             * All moieties of the input formula have been processed into
             * canonical versions; return a ChemicalFormula made up of the new
             * moieties. 
             */
            return new ChemicalFormula(moietyList);
        }
    }
    
    /**
     * Examines the specified chemical formula to determine whether it
     * represents a valid empirical formula, which is the case if it consists
     * of exactly one moiety with no multiplier, and that moiety's group has no
     * multiplier or charge and only {@code AtomCount} members (at least one).
     * 
     * @param  formula the {@code ChemicalFormula} to validate
     * 
     * @return {@code true} if the specified formula satisfies the criteria for
     *         an empirical formula, {@code false} if it does not
     */
    public static boolean isValidEmpiricalFormula(ChemicalFormula formula) {
        EmpiricalFormulaValidator validator = new EmpiricalFormulaValidator();
        
        formula.accept(validator);
        
        return validator.isValid();
    }
    
    /**
     * Examines the specified chemical formula to determine whether it
     * represents a valid empirical formula for an ion, which is the case if it
     * consists of exactly one moiety with no multiplier, and that moiety's
     * group has no multiplier and only {@code AtomCount} members
     * (at least one).  The group may bear a charge.
     * 
     * @param  formula the {@code ChemicalFormula} to validate
     * 
     * @return {@code true} if the specified formula satisfies the criteria for
     *         the empirical formula of an ion, {@code false} if it does not
     */
    public static boolean isValidEmpiricalIonFormula(ChemicalFormula formula) {
        MoietyFormulaValidator validator = new EmpiricalIonFormulaValidator();
        
        formula.accept(validator);
        
        return validator.isValid();
    }
    
    /**
     * Examines the specified chemical formula to determine whether it
     * represents a valid moiety formula, which is the case if it contains at
     * least one moiety, and each one's groups contains only {@code AtomCount}
     * members (at least one) and optionally a charge.
     * 
     * @param  formula the {@code ChemicalFormula} to validate
     * 
     * @return {@code true} if the specified formula satisfies the criteria for
     *         a moiety formula, {@code false} if it does not
     */
    public static boolean isValidMoietyFormula(ChemicalFormula formula) {
        MoietyFormulaValidator validator = new MoietyFormulaValidator();
        
        formula.accept(validator);
        
        return validator.isValid();
    }
    
    /**
     * Analyzes the specified chemical formula part to obtain a collection of
     * the atom counts it represents.  The returned value is cumulative over the
     * specified formula part, which must not contain any
     * {@code AbbreviationCount} objects at any depth.
     * 
     * @param  formulaPart the {@code ChemicalFormulaPart} over which to count
     *         atoms
     * @param  withoutMultiplier instructs the counter to ignore the group
     *         multiplier(s) of the top-most moieties or groups encountered
     *         while traversing the formula.  The typical value of this argument
     *         would be {@code false}, but it can be set to {@code true} to,
     *         for instance, get a count of the contents of one moiety from a
     *         formula without taking into account the count of the moiety
     *         itself in the formula.  
     * 
     * @return a {@code Map<Element, BigDecimal>} of the counts of each element
     *         present in the formula part
     *
     * @throws IllegalArgumentException if the formula part contains chemical
     *         abbreviations, either directly or indirectly
     */
    public static Map<Element, BigDecimal> getAtomCounts(
            ChemicalFormulaPart formulaPart, boolean withoutMultiplier) {
        CountingFormulaVisitor visitor = new CountingFormulaVisitor();
        
        visitor.setIgnoreNextMultiplier(withoutMultiplier);
        formulaPart.accept(visitor);
        
        return visitor.getCounts();
    }
    
    /**
     * Produces a chemical formula {@code String} from a
     * {@code ChemicalFormula} object.  No guarantee is provided that
     * {@code getFormulaString(parseFormula(s))} returns a {@code String} equal
     * to the original input, {@code s}, though if the sequence does not throw
     * an exception then the two strings will certainly represent the same
     * abstract chemical formula.
     * 
     * @param  formula the {@code ChemicalFormula} for which a string'
     *         representation is requested
     * 
     * @return a {@code String} representation of the provided formula,
     *         formatted so that the {@link #parseFormula(String)} method of
     *         this class could produce from it a formula object equivalent to
     *         the original.
     */
    public static String getFormulaString(ChemicalFormula formula) {
        StringBuildingVisitor visitor = new StringBuildingVisitor();

        formula.accept(visitor);
        
        return visitor.getString();
    }
    
    /**
     * <p>
     * Parses a {@code ChemicalFormula} object out of the specified string.  The
     * format supported is a superset of the CIF formats for structural
     * formulae, moiety formulae, and empirical formulae, conforming to the
     * following LL(\u221e) grammar:</p>
     * <b><i>formula</i></b>: <i>moiety</i>
     * | <i>moiety</i> ',' <i>formula</i><br/>
     * <b><i>moiety</i></b>: <i>group</i>
     * | NUMBER <i>group</i><br/>
     * <b><i>group</i></b>: <i>bare_group</i>
     * | <i>paren_group</i><br/>
     * <b><i>paren_group</i></b>: '(' <i>bare_group</i> ')'
     * | '(' <i>bare_group</i> ')' NUMBER
     * <b><i>bare_group</i></b>: <i>neutral_group</i> 
     * | <i>neutral_group</i> CHARGE
     * <b><i>neutral_group</i></b>: neutral_group_start
     * | neutral_group_start group_parts<br/>
     * <b><i>neutral_group_start</i></b>: COUNTED_ITEM
     * | <i>paren_group</i> <i>group_part</i><br/>
     * <b><i>group_parts</i></b>: <i>group_part</i>
     * | <i>group_part</i> <i>group_parts</i><br/>
     * <b><i>group_part</i></b>: COUNTED_ITEM
     * | <i>paren_group</i><br/>
     * <p>
     * Terminals of the grammar are punctuation characters ',', '(', and ')',
     * unsigned decimal numbers (NUMBER), electric charges as explicit or
     * implied unsigned numeric magnitude with an explicit sign indicator
     * (CHARGE), and sequences of letters with explicit or implied associated
     * counts (COUNTED_ITEM).  Counted items' counts must follow their items
     * without intervening whitespace, and counted items with implict counts
     * must be seperated by one or more space characters from any following
     * counted item or any charge with explicit magnitude.  Spaces are not
     * otherwise significant to the lexer.  The lexer makes a special attempt to
     * parse empirical and moiety formulae from which some or all of the
     * required space characters have been omitted: it breaks up text tokens
     * that start with valid one- or two-character chemical symbols (in standard
     * mixed-case format) into separate tokens so that they will be interpreted
     * as atom counts. 
     * </p>
     * 
     * @param  formulaString the string to parse
     * 
     * @return a {@code ChemicalFormula} representing the string's contents
     * 
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the specified string cannot be parsed as a chemical formula
     */
    public static ChemicalFormula parseFormula(String formulaString) 
            throws InvalidDataException {
        
        /*
         * The algorithm implemented collectively here and in the other
         * parseXXX() methods constitute a farily simple recursive-descent
         * parser based on the grammar presented above (a bit compressed).  The
         * parser avoids backtracking in the one place where it might otherwise
         * be needful (in parseMoiety()) by instead being able to change gears
         * in midstream
         */

        /*
         * construct the tokenizer based on a gently massaged version of the
         * submitted formula ('[' and '{' translated to '(',  ']' and '}'
         * translated to ')', and ';' translated to ',')
         */
        FormulaTokenizer tokenizer = new FormulaTokenizer(
                formulaString.replaceAll("[\\[\\{]", "(")
                             .replaceAll("[\\]\\}]", ")")
                             .replaceAll(";", ","));
        
        // Make sure the formula isn't blank
        if (tokenizer.peekNextToken().getType() == FormulaTokenType.END) {
            throw new InvalidDataException("Blank formula", formulaString,
                    InvalidDataException.MALFORMED_FORMULA);
        } else {
            
            // parse and return the formula
            return parseFormula(tokenizer);
        }
    }
    
    /**
     * Parses the token stream from the specified tokenizer into a
     * {@code ChemicalFormula} object according to the grammar production for
     * the <i>formula</i> non-terminal
     *  
     * @param  tokenizer the {@code FormulaTokenizer} providing the token stream
     * 
     * @return a {@code ChemicalFormula} object representing all tokens
     *         available from the tokenizer
     *          
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the token stream cannot be parsed in its entirety as a
     *         chemical formula
     */
    private static ChemicalFormula parseFormula(FormulaTokenizer tokenizer)
            throws InvalidDataException {
        
        // The moieties of this formula, in order 
        List<ChemicalFormulaMoiety> moietyList =
                new ArrayList<ChemicalFormulaMoiety>();

        // Parse all moieties
        moietyList.add(parseMoiety(tokenizer));
        if (tokenizer.peekNextToken().getType() == FormulaTokenType.COMMA) {
            tokenizer.getNextToken();  // Consume the comma
            moietyList.addAll(parseFormula(tokenizer).getMoietyList());
        }
        
        // Verify that the entire input has been parsed
        if (tokenizer.peekNextToken().getType() != FormulaTokenType.END) {
            throw new InvalidDataException("Unparseable formula at '"
                    + tokenizer.peekNextToken().getValue() + "'",
                    tokenizer.getFormulaString(),
                    InvalidDataException.MALFORMED_FORMULA);
        }
        
        // Create and return the formula
        return new ChemicalFormula(moietyList);
    }

    /**
     * Parses a {@code ChemicalFormulaMoiety} from the token stream provided by
     * the specified tokenizer, according to the grammar production for the
     * <i>moiety</i> non-terminal
     * 
     * @param  tokenizer a {@code FormulaTokenizer} that will provide the tokens
     *         from which to parse a moiety
     * 
     * @return the {@code ChemicalFormulaMoiety} parsed from the provided token
     *         stream
     * 
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the tokenizer throws this exception or if no moiety can be
     *         parsed from the head of its token stream
     */
    private static ChemicalFormulaMoiety parseMoiety(FormulaTokenizer tokenizer)
            throws InvalidDataException {
        BigDecimal premultiplier;
        ChemicalFormulaGroup group;
        
        if (tokenizer.peekNextToken().getType() == FormulaTokenType.NUMBER) {
            FormulaToken token = tokenizer.getNextToken();
            
            try {
                premultiplier = parseNumber(token.getValue());
            } catch (NumberFormatException nfe) {
                
                /*
                 * This would indicate a tokenizer failure
                 */
                
                throw new UnexpectedExceptionException(nfe);
            }
        } else {
            premultiplier = BigDecimal.ONE;
        }
        
        if (tokenizer.peekNextToken().getType()
                == FormulaTokenType.OPEN_PAREN) {
            group = parseParenGroup(tokenizer);
            switch (tokenizer.peekNextToken().getType()) {
                case COMMA:
                    // Fall through
                case END:
                    // End of moiety -- Everything is fine
                    break;
                default:
                    
                    /*
                     * Must have been the start of a bare group, not a
                     * parenthesized moiety; pretend we knew that all along
                     */
                    group = parseBareGroup(tokenizer, group);
            }
        } else {
            group = parseBareGroup(tokenizer);
        }
        
        return new ChemicalFormulaMoiety(premultiplier, group);
    }

    /**
     * Parses a parenthesized {@code ChemicalFormulaGroup} from the token stream
     * provided by the specified tokenizer, according to the grammar production
     * for the <i>paren_group</i> non-terminal
     * 
     * @param  tokenizer a {@code FormulaTokenizer} that will provide the tokens
     *         from which to parse a group
     * 
     * @return the {@code ChemicalFormulaGroup} parsed from the provided token
     *         stream
     * 
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the tokenizer throws this exception or if no group can be
     *         parsed from the head of its token stream
     */
    private static ChemicalFormulaGroup parseParenGroup(
            FormulaTokenizer tokenizer) throws InvalidDataException {
        if (tokenizer.peekNextToken().getType()
                == FormulaTokenType.OPEN_PAREN) {
            ChemicalFormulaGroup group;
            
            tokenizer.getNextToken();  // consume the opening parenthesis
            group = parseBareGroup(tokenizer);
            if (tokenizer.peekNextToken().getType()
                    == FormulaTokenType.CLOSE_PAREN) {
                tokenizer.getNextToken();  // consume the closing parenthesis
                
                // Is there a group count?
                if (tokenizer.peekNextToken().getType()
                        == FormulaTokenType.NUMBER) {
                    group = new ChemicalFormulaGroup(
                            group.getParts(),
                            group.getCharge(),
                            parseNumber(tokenizer.getNextToken()));
                }
                
                // Group was successfully parsed
                return group;
            }
        }
        
        // Something was wrong with the parentheses
        throw new InvalidDataException("Missing or unmatched parentheses",
                tokenizer.getFormulaString(),
                InvalidDataException.MALFORMED_FORMULA);
    }

    /**
     * Parses a "bare" {@code ChemicalFormulaGroup} from the token stream
     * provided by the specified tokenizer, according to the grammar production
     * for the <i>bare_group</i> non-terminal.  This method consumes the maximum
     * number of tokens consistent with the grammar before returning, and always
     * will have consumed at least one if it returns normally.
     * 
     * @param  tokenizer a {@code FormulaTokenizer} that will provide the tokens
     *         from which to parse a group
     * @param  firstParts zero or more {@code FormulaGroupPart}s (or an array of
     *         the same) that should be included at the beginning of this group;
     *         useful to avoid backtracking if the parser parses one or more
     *         group parts for another purpose before changing its mind and
     *         deciding that they really should be part of a <i>bare_group</i>
     * 
     * @return the {@code ChemicalFormulaGroup} parsed from the provided token
     *         stream
     * 
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the tokenizer throws this exception or if no group can be
     *         parsed from the head of its token stream
     */
    private static ChemicalFormulaGroup parseBareGroup(
            FormulaTokenizer tokenizer, FormulaGroupPart... firstParts)
            throws InvalidDataException {
        
        // The group parts, in order
        List<FormulaGroupPart> partsList =
                new ArrayList<FormulaGroupPart>(Arrays.asList(firstParts));
        
        boolean needTwoParts =
                ((firstParts.length == 0)
                && (tokenizer.peekNextToken().getType()
                        == FormulaTokenType.OPEN_PAREN));
        Charge charge;
        
        // parse group start
        partsList.add(parseGroupPart(tokenizer));
        if (needTwoParts) {
            partsList.add(parseGroupPart(tokenizer));
        }
        
        // parse additional parts
        while((tokenizer.peekNextToken().getType()
                    == FormulaTokenType.COUNTED_ITEM)
                || (tokenizer.peekNextToken().getType()
                        == FormulaTokenType.OPEN_PAREN)) {
            partsList.add(parseGroupPart(tokenizer));
        }
        
        //parse charge
        if ((tokenizer.peekNextToken().getType()
                    == FormulaTokenType.CHARGE)) {
            charge = parseCharge(tokenizer.getNextToken());
        } else {
            charge = Charge.NEUTRAL;
        }
        
        return new ChemicalFormulaGroup(partsList, charge, BigDecimal.ONE);
    }

    /**
     * Parses a {@code FormulaGroupPart} from the token stream provided by the
     * specified tokenizer, according to the grammar production for the
     * <i>group_part</i> non-terminal.
     * 
     * @param  tokenizer a {@code FormulaTokenizer} that will provide the tokens
     *         from which to parse a group part
     * 
     * @return the {@code FormulaGroupPart} parsed from the provided token
     *         stream
     * 
     * @throws InvalidDataException with reason code {@code MALFORMED_FORMULA}
     *         if the tokenizer throws this exception or if no group part can be
     *         parsed from the head of its token stream
     */
    private static FormulaGroupPart parseGroupPart(FormulaTokenizer tokenizer)
            throws InvalidDataException {
        switch (tokenizer.peekNextToken().getType()) {
            case COUNTED_ITEM:
                return parseCountedItem(tokenizer.getNextToken());
            case OPEN_PAREN:
                return parseParenGroup(tokenizer);
            default:
                throw new InvalidDataException("Invalid formula group start: "
                        + tokenizer.peekNextToken().getValue(),
                        tokenizer.getFormulaString(),
                        InvalidDataException.MALFORMED_FORMULA);
        }
    }

    /**
     * Parses an {@code AtomCount} or {@code AbbreviationCount} from the
     * provided COUNTED_ITEM token
     * 
     * @param  token a {@code FormulaToken} of type {@code COUNTED_ITEM} that
     *         carries the text to parse as its
     *         {@link FormulaToken#getValue() value}
     * 
     * @return the {@code FormulaGroupPart} parsed from the provided token's
     *         value
     */
    private static FormulaGroupPart parseCountedItem(FormulaToken token) {
        
        // The caller is responsible for passing a token of the correct type  
        assert(token.getType() == FormulaTokenType.COUNTED_ITEM);
        
        Matcher matcher = COUNTED_ITEM_PATTERN.matcher(token.getValue());
        String symbol;
        String countString;
        Element el;
        BigDecimal count;
        
        if (!matcher.matches()) {
            
            // This would indicate a tokenizer failure
            assert false;
        }
        
        symbol = matcher.group(1);
        countString = matcher.group(2);
        try {
            count = ((countString != null)
                    ? parseNumber(countString)
                    : BigDecimal.ONE);
        } catch (NumberFormatException nfe) {
            
            // Should never happen because the tokenizer is supposed to have
            // determined that the value is parseable as a BigDecimal
            throw new UnexpectedExceptionException(nfe);
        }
        
        el = Element.forSymbol(symbol);
        if (el != null) {
            return new AtomCount(el, count);
        } else {
            return new AbbreviationCount(symbol, count);
        }
    }


    /**
     * Parses a {@code Charge} from the provided CHARGE token
     * 
     * @param  token a {@code FormulaToken} of type CHARGE  that
     *         carries the text to parse as its
     *         {@link FormulaToken#getValue() value}
     * 
     * @return the {@code Charge} parsed from the token's value
     */
    private static Charge parseCharge(FormulaToken token) {
        
        // The caller is responsible for passing a token of the correct type  
        assert(token.getType() == FormulaTokenType.CHARGE);
        
        Matcher matcher = CHARGE_PATTERN.matcher(token.getValue());
        BigDecimal magnitude;
        
        if (!matcher.matches()) {
            
            // This would indicate a tokenizer failure
            assert false;
        }
        
        try {
            String number = matcher.group(1);
            magnitude = ((number == null)
                    ? BigDecimal.ONE
                    : parseNumber(number));
        } catch (NumberFormatException nfe) {
            
            // The pattern only matches strings that are parseable as BigDecimal
            throw new UnexpectedExceptionException(nfe);
        }
        
        return new Charge(magnitude, matcher.group(2));
    }

    /**
     * Parses a {@code BigDecimal} from the provided NUMBER token
     * 
     * @param  token a {@code FormulaToken} of type {@code NUMBER} that
     *         carries the text to parse as its
     *         {@link FormulaToken#getValue() value}
     * 
     * @return the {@code BigDecimal} parsed from the provided token's
     *         value
     */
    private static BigDecimal parseNumber(FormulaToken token) {
        
        // The caller is responsible for passing a token of the correct type  
        assert(token.getType() == FormulaTokenType.NUMBER);
        
        try {
            return parseNumber(token.getValue());
        } catch (NumberFormatException nfe) {
            throw new UnexpectedExceptionException(nfe);
        }
    }

    /**
     * Creates a {@code BigDecimal} representation of the specified numeric
     * (decimal) String, truncating any trailing zeroes after the decimal point
     *  
     * @param numericString a String containing a decimal representation of the
     *        desired result
     *        
     * @return a {@code BigDecimal} having nonnegative scale and no trailing
     *         zeroes after the decimal point 
     */
    private static BigDecimal parseNumber(String numericString) {
        BigDecimal bd;
        
        try {
            bd = new BigDecimal(numericString);
        } catch (NumberFormatException nfe) {
            // The invoker should have already verified that the string is OK
            throw new UnexpectedExceptionException(nfe);
        }
        
        /*
         * Trailing zeroes after the decimal point are stripped, even though
         * they are significant
         */
        if (bd.scale() > 0) {
            bd = bd.stripTrailingZeros();
        }
        
        /*
         * Stripping trailing zeroes may have removed too many, or the input
         * string may have been expressed in exponential format with less
         * than unit precision.  We want the output to have at least unit
         * precision. 
         */
        if (bd.scale() < 0) {
            bd.setScale(0);
        }
        
        return bd;
    }
    
    /**
     * A class providing a structured representation a chemical formula.  In
     * particular, a {@code ChemicalFormula} is a list of one or more
     * {@link ChemicalFormulaMoiety} objects.  Instances of this class are
     * immutable, and therefore thread-safe.
     *  
     * @author jobollin
     * @version 0.9.0
     */
    public static class ChemicalFormula implements ChemicalFormulaPart {
        
        /**
         * An unmodifiable {@code List<ChemicalFormulaMoiety>} of the moieties
         * that make up this formula, maintained in the same sequence that they
         * appear in the raw formula text
         */
        private final List<ChemicalFormulaMoiety> moietyList;
        
        /**
         * Initializes a {@code ChemicalFormula} with the specified list of
         * moieties.
         * 
         * @param  moieties a {@code List<ChemicalFormulaMoiety>} of the
         *         moieties that make up this formula
         * 
         * @throws IllegalArgumentException if the list is empty or contains
         *         {@code null}s
         */
        public ChemicalFormula(List<ChemicalFormulaMoiety> moieties) {
            if (moieties.isEmpty() || moieties.contains(null)) {
                throw new IllegalArgumentException(
                        "The moiety list must contain no nulls and at least one moiety");
            }
            moietyList = Collections.unmodifiableList(
                    new ArrayList<ChemicalFormulaMoiety>(moieties));
        }
        
        /**
         * Returns an unmodifiable list of the moieties comprised by this
         * formula
         * 
         * @return an unmodifiable {@code List<ChemicalFormulaMoiety>} of the
         *         moieties of this formula, in the order they appear in the
         *         formula
         */
        public List<ChemicalFormulaMoiety> getMoietyList() {
            return moietyList;
        }
        
        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this formula
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * A class providing a structured representation of one moiety of a chemical
     * formula; in particular, a moiety consists of a premultiplier (possibly 1)
     * and a {@link ChemicalFormulaGroup}.  Instances of this class are
     * immutable, and therefore thread-safe.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public static class ChemicalFormulaMoiety implements ChemicalFormulaPart {
        
        /**
         * The premultiplier of this moiety
         */
        private final BigDecimal preMultiplier;
        
        /**
         * The {@code ChemicalFormulaGroup} comprised by this group
         */
        private final ChemicalFormulaGroup group;
        
        /**
         * Initializes a {@code ChemicalFormulaMoiety} with the specified
         * multiplier and group
         * 
         * @param groupMultiplier the {@code BigDecimal} multiplier of this
         *        moiety
         * @param group the {@code ChemicalFormulaGroup} of this moiety
         */
        public ChemicalFormulaMoiety(BigDecimal groupMultiplier,
                ChemicalFormulaGroup group) {
            this.preMultiplier = groupMultiplier;
            this.group = group;
        }

        /**
         * Returns the formula group comprised by this moiety
         * 
         * @return the {@code ChemicalFormulaGroup} of this moiety
         */
        public ChemicalFormulaGroup getGroup() {
            return group;
        }

        /**
         * Returns the premultiplier of this moiety
         * 
         * @return the premultiplier as a {@code BigDecimal}; possibly unity,
         *         and not necessarilly integral
         */
        public BigDecimal getPreMultiplier() {
            return preMultiplier;
        }
        
        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this moiety
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * A class providing a structured representation of a portion of a chemical
     * formula moiety.  In particular, a group consists of a sequence of one or
     * more {@link FormulaGroupPart parts}, an electric charge (possibly zero),
     * and a (post) multiplier.  Instances of this class are immutable, and
     * therefore thread-safe.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public static class ChemicalFormulaGroup implements FormulaGroupPart {
        
        /**
         * An unmodifiable {@code List<FormulaGroupPart>} of the parts of this
         * group -- atom counts, abbreviation counts, and other groups -- in the
         * order they appear in this group
         */
        private final List<FormulaGroupPart> subgroups;
        
        /**
         * This group's {@code BigDecimal} (post)multiplier
         */
        private final BigDecimal postMultiplier;
        
        /**
         * This group's electric charge
         */
        private final Charge charge;
        
        /**
         * Initializes a {@code ChemicalFormulaGroup} with the specified
         * group parts, charge, and (post)multiplier
         * 
         * @param  parts a {@code List<FormulaGroupPart>} of the parts of this
         *         group (element counts and subgroups), in the desired order
         * @param  charge a {@code Charge} representing the group's electric
         *         charge
         * @param  multiplier a {@code BigDecimal} group multiplier, expressed
         *         as a subscript in the canonical representation
         */
        public ChemicalFormulaGroup(List<? extends FormulaGroupPart> parts,
                Charge charge, BigDecimal multiplier) {
            this.subgroups = Collections.unmodifiableList(
                    new ArrayList<FormulaGroupPart>(parts));
            this.postMultiplier = multiplier;
            this.charge = charge;
        }
        
        /**
         * Returns the electric charge assigned to this group
         * 
         * @return the charge as a {@code Charge}
         */
        public Charge getCharge() {
            return charge;
        }
        
        /**
         * Returns an unmodifiable list of the counted items and subgroups
         * comprised by this group
         * 
         * @return an unmodifiable {@code List<FormulaGroupPart>} of this
         *         group's members, in the order they appear in this group
         */
        public List<FormulaGroupPart> getParts() {
            return subgroups;
        }
        
        /**
         * Returns the group's assigned multiplier
         * 
         * @return the multiplier as a {@code BigDecimal}; possibly unity, and
         *         not necessarilly integral
         */
        public BigDecimal getPostMultiplier() {
            return postMultiplier;
        }
        
        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this group
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * A class providing a structured representation of an atom count,
     * comprising a chemical element and a count of that element.  Instances of
     * this class are immutable, and therefore thread-safe.
     *  
     * @author jobollin
     * @version 0.9.0
     */
    public static class AtomCount implements FormulaGroupPart {
        
        /**
         * The {@code Element} of the atoms represented by this count
         */
        private final Element atomType;
        
        /**
         * the {@code BigDecimal} number of atoms represented by this count
         */
        private final BigDecimal count;
        
        /**
         * Initializes a {@code AtomCount} with the specified parameters
         * 
         * @param  element the {@code Element} of the atoms represented by this
         *         count
         * @param  atomCount the {@code BigDecimal} number of atoms represented
         *         by this count
         */
        public AtomCount(Element element, BigDecimal atomCount) {
            atomType = element;
            count = atomCount;
        }
        
        /**
         * Returns the element to which this count pertains
         *  
         * @return the element of which this object is a count, as an
         *         {@code Element}
         */
        public Element getAtomType() {
            return atomType;
        }

        /**
         * Returns the numeric count represented by this object
         * 
         * @return the count as a {@code BigDecimal}; possibly unity, and not
         *         necessarilly integral
         */
        public BigDecimal getCount() {
            return count;
        }

        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this count
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * A class providing a structured representation of an abbreviation count,
     * comprising some local chemical abbreviation and a count of instances of
     * the group represented by that abbreviation.  Instances of this class are
     * immutable, and therefore thread-safe.
     *  
     * @author jobollin
     * @version 0.9.0
     */
    public static class AbbreviationCount implements FormulaGroupPart {
        
        /**
         * The {@code String} abbreviation represented by this count
         */
        private final String abbreviation;
        
        /**
         * The {@code BigDecimal} number of groups represented by this count
         */
        private final BigDecimal count;
        
        /**
         * Initializes a {@code AbbreviationCount} with the specified
         * abbreviation and count
         * 
         * @param  abbrev the {@code String} abbreviation represented by this
         *         count
         * @param  count the {@code BigDecimal} number of items represented by
         *         this count
         */
        public AbbreviationCount(String abbrev, BigDecimal count) {
            this.abbreviation = abbrev;
            this.count = count;
        }

        /**
         * Returns the abbreviation text to which this count pertains
         * 
         * @return the abbreviation as a {@code String}
         */
        public String getAbbreviation() {
            return abbreviation;
        }

        /**
         * Returns the numeric count represented by this object
         * 
         * @return the count as a {@code BigDecimal}; possibly unity, and not
         *         necessarilly integral
         */
        public BigDecimal getCount() {
            return count;
        }
        
        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this count
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * A class representing an electric charge on a molecule or molecular
     * fragment, comprising a sign and a magnitude.  Instances of this class
     * are immutable, and therefore thread-safe.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public static class Charge implements ChemicalFormulaPart {
        
        /**
         * A static {@code Charge} instance representing zero charge 
         */
        public final static Charge NEUTRAL = new Charge(BigDecimal.ZERO, "+");
        
        /**
         * The {@code BigDecimal} magnitude of this charge
         */
        private final BigDecimal magnitude;
        
        /**
         * The sign of this charge as a string
         */
        private final String sign;
        
        /**
         * Initializes a {@code Charge} with the specified magnitude and sign
         * 
         * @param  magnitude the {@code BigDecimal} magnitude of this charge
         * @param  sign the sign of this charge as a {@code String}
         */
        public Charge(BigDecimal magnitude, String sign) {
            if ((magnitude == null) || (sign == null)) {
                throw new NullPointerException(
                        "The arguments must not be null");
            }
            this.magnitude = magnitude;
            this.sign = sign;
        }

        /**
         * Returns the magnitude of this charge (possibly zero)
         * 
         * @return the magnitude as a {@code BigDecimal}
         */
        public BigDecimal getMagnitude() {
            return magnitude;
        }

        /**
         * Returns the sign of this charge as a string
         * 
         * @return the sign {@code String}
         */
        public String getSign() {
            return sign;
        }

        /**
         * Determines whether this {@code Charge} is neutral (i.e. zero charge}
         * 
         * @return {@code true} if this charge is neutral, {@code false} if not
         */
        public boolean isNeutral() {
            return magnitude.equals(BigDecimal.ZERO);
        }

        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this charge
         * 
         * @see ChemicalFormulaPart#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)
         */
        public void accept(ChemicalFormulaVisitor visitor) {
            visitor.visit(this);
        }
    }
    
    /**
     * An interface tagging and representing the behavior of objects that can be
     * constituent parts of chemical formulae
     *   
     * @author jobollin
     * @version 0.9.0
     */
    public interface ChemicalFormulaPart {
        
        /**
         * Accepts a {@code ChemicalFormulaVisitor} by directing the appropriate
         * one of its {@code visit()} methods to handle this formula part
         * 
         * @param  visitor the {@code ChemicalFormulaVisitor} to accept
         */
        void accept(ChemicalFormulaVisitor visitor);
    }
    
    /**
     * An interface tagging and representing the behavior of objects that can be
     * constituent parts of a chemical formula group
     *   
     * @author jobollin
     * @version 0.9.0
     */
    public interface FormulaGroupPart extends ChemicalFormulaPart {
        
        /*
         * Empty for now; this interface currently serves only to distinguish
         * certain types of ChemicalFormulaParts as being eligible to be part
         * of ChemicalFormulaGroups
         */
    }
    
    /**
     * An interface describing a <em>Visitor</em> to a chemical formula.  Such a
     * visitor will typically traverse some or all of the formula to extract
     * information of interest to its particular implementation and purpose,
     * such as constructing an alternative representation of the formula or
     * counting its atoms.  With the cooperation of the visited objects,
     * Visitors employ <em>double dispatch</em> to visit multiple different
     * object types in a type-safe way without employing any run-time type
     * information.
     * 
     * @author jobollin
     * @version 0.9.0
     */
    public interface ChemicalFormulaVisitor {
        
        /**
         * Visits the specified chemical formula; typically invoked by
         * {@link ChemicalFormula#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  formula the {@code ChemicalFormulaVisitor} to visit
         */
        void visit(ChemicalFormula formula);
        
        /**
         * Visits the specified chemical formula moiety; typically invoked by
         * {@link ChemicalFormulaMoiety#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  moiety the {@code ChemicalFormulaMoiety} to visit
         */
        void visit(ChemicalFormulaMoiety moiety);
        
        /**
         * Visits the specified chemical formula group; typically invoked by
         * {@link ChemicalFormulaGroup#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  group the {@code ChemicalFormulaGroup} to visit
         */
        void visit(ChemicalFormulaGroup group);
        
        /**
         * Visits the specified atom count; typically invoked by
         * {@link AtomCount#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  count the {@code AtomCount} to visit
         */
        void visit(AtomCount count);
        
        /**
         * Visits the specified abbreviation count; typically invoked by
         * {@link AbbreviationCount#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  count the {@code ChemicalFormulaMoiety} to visit
         */
        void visit(AbbreviationCount count);
        
        /**
         * Visits the specified chemical formula charge; typically invoked by
         * {@link Charge#accept(ChemicalFormulaBL.ChemicalFormulaVisitor)}
         * 
         * @param  charge the {@code Charge} to visit
         */
        void visit(Charge charge);
    }
    
    /**
     * An abstract base class for {@code ChemicalFormulaVisitor} implementations
     * that want to perform a depth-first traversal of an entire chemical
     * formula (sub)tree.  Implementations are provided for those
     * {@code ChemicalFormulaVisitor} methods that visit branch nodes of a
     * chemical formula tree, and these simply direct the visitor to traverse
     * those nodes' child nodes.  Subclasses would typically implement only the
     * behavior specific to particular node ({@code ChemicalFormulaPart}) types,
     * and delegate to this class for navigation. 
     *  
     * @author jobollin
     * @version 0.9.0
     */
    public static abstract class FormulaWalker
            implements ChemicalFormulaVisitor {

        /**
         * Visits a {@code ChemicalFormula} by visiting each of its component
         * moieties, in the order they appear in the formula's moiety list
         * 
         * @param  formula the {@code ChemicalFormula} to visit
         * 
         * @see ChemicalFormulaVisitor#visit(ChemicalFormulaBL.ChemicalFormula)
         */
        public void visit(ChemicalFormula formula) {
            for (ChemicalFormulaMoiety moiety : formula.getMoietyList()) {
                moiety.accept(this);
            }
        }

        /**
         * Visits a {@code ChemicalFormulaMoiety} by visiting its associated
         * {@code ChemicalFormulaGroup}
         * 
         * @param  moiety the {@code ChemicalFormulaMoiety} to visit
         * 
         * @see ChemicalFormulaVisitor#visit(ChemicalFormulaBL.ChemicalFormulaMoiety)
         */
        public void visit(ChemicalFormulaMoiety moiety) {
            moiety.getGroup().accept(this);
        }

        /**
         * Visits a {@code ChemicalFormulaGroup} by visiting each of its parts
         * in the order they appear in the group's parts list
         * 
         * @param  group the {@code ChemicalFormulaGroup} to visit
         * 
         * @see ChemicalFormulaVisitor#visit(ChemicalFormulaBL.ChemicalFormulaMoiety)
         */
        public void visit(ChemicalFormulaGroup group) {
            for (ChemicalFormulaPart part : group.getParts()) {
                part.accept(this);
            }
            group.getCharge().accept(this);
        }
    }
}


/**
 * An enumeration of the token types that a {@link FormulaTokenizer} may return
 * 
 * @author jobollin
 * @version 0.9.0
 */
enum FormulaTokenType {
    
    /**
     * The token type for a closing parenthesis
     */
    CLOSE_PAREN,
    
    /**
     * The token type for a comma
     */
    COMMA,
    
    /**
     * The token type for a chemical abbreviation or element symbol with
     * optional count; the tokenizer does not distinguish
     */
    COUNTED_ITEM,
    
    /**
     * The token type for a token designating the end of the input
     */
    END,
    
    /**
     * The token type for a bare number (one not associated with an element or
     * abbreviation)
     */
    NUMBER,
    
    /**
     * The token type for an opening parenthesis
     */
    OPEN_PAREN,
    
    /**
     * The token type for an electric charge
     */
    CHARGE
}

/**
 * A class representing the tokens returned by a {@link FormulaTokenizer}.  Each
 * has a {@link FormulaTokenType type} and a string value.  Instances of this
 * class are immutable, and therefore thread-safe.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class FormulaToken {
    
    /**
     * The type of this token, as determined by the tokenizer
     */
    private final FormulaTokenType type;
    
    /**
     * The string value of this token; i.e. that part of the input that this
     * token represents
     */
    private final String value;
    
    /**
     * Initializes a {@code FormulaToken} with the specified type and value
     * 
     * @param  tokenType the {@code FormulaTokenType} representing the type of
     *         this token
     * @param  tokenValue the {@code String} value of this token -- that is,
     *         that portion of the input text represented by this token
     */
    public FormulaToken(FormulaTokenType tokenType, String tokenValue) {
        type = tokenType;
        value = tokenValue;
    }

    /**
     * @return the {@code type} {@code FormulaTokenType}
     */
    public FormulaTokenType getType() {
        return type;
    }

    /**
     * @return the {@code value} {@code String}
     */
    public String getValue() {
        return value;
    }
}

/**
 * A class that can tokenize moiety strings into semantic tokens for
 * subsequent parsing.  Instances of this class are not thread-safe; if an
 * instance is shared among threads then all accesses must be properly
 * synchronized to avoid undefined behavior.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class FormulaTokenizer {
    
    /**
     * A regex {@code Pattern} for a plain, unsigned decimal number
     */
    private final static Pattern NUMBER_PATTERN =
            Pattern.compile(ChemicalFormulaBL.NUMBER_SUBPATTERN);
    
    /**
     * A regex {@code Pattern} for an unsigned decimal number with optional
     * trailing +/- sign; the sign is captured into the first (and only) group
     * defined by this pattern
     */
    private final static Pattern NUMBER_CHARGE_PATTERN =
            Pattern.compile("(?:" + ChemicalFormulaBL.NUMBER_SUBPATTERN
                    + ")([-+])?");
    
    /**
     * A regex {@code Pattern} for an element symbol or general chemical
     * abbreviation such as "tol", "DMF", "Me", or "tBu", including some
     * symbolic ones such as "\u03bc-OEt"
     */
    /*
     * Matches a Unicode letter of any category, followed by any sequence of
     * zero or more Unicode letters and combining marks and '+' and '-'
     * characters 
     */
    private final static Pattern TEXT_PATTERN =
            Pattern.compile(ChemicalFormulaBL.TEXT_SUBPATTERN);
    
    /**
     * The current position in the input at which this tokenizer is working
     */
    private int position;
    
    /**
     * The string that this tokenizer is tokenizing
     */
    private final String formulaString;
    
    /**
     * The next token to be returned by this tokenizer, or {@code null} if it
     * has not yet been determined
     */
    private FormulaToken nextToken;
    
    /**
     * Initializes a {@code FormulaTokenizer} with the specified formula
     * string to tokenize
     * 
     * @param  s the {@code String} to tokenize
     */
    public FormulaTokenizer(String s) {
        if (s == null) {
            throw new NullPointerException(
                    "The moiety string must not be null");
        } else {
            formulaString = s;
            position = 0;
            nextToken = null;
        }
    }
    
    /**
     * Returns the next token available from the input, or an end-of-input
     * token if the input has been exhausted.  The token is not consumed by
     * this action; the same one will be returned by the next invocation of
     * {@link #getNextToken()}, and by any further invocations of this
     * method before {@code getNextToken()} is next invoked.
     * 
     * @return the next token as a {@code FormulaToken}
     * 
     * @throws InvalidDataException if the next segment of this tokenizer's
     *         string cannot be parsed
     */
    public FormulaToken peekNextToken() throws InvalidDataException {
        if (nextToken == null) {
            computeNextToken();
        }
        
        return nextToken;
    }
    
    /**
     * Returns the next token available from the input, or an end-of-input
     * token if the input has been exhausted; the token is consumed by this
     * action
     * 
     * @return the next token as a {@code FormulaToken}
     * 
     * @throws InvalidDataException if the next segment of this tokenizer's
     *         string cannot be parsed
     */
    public FormulaToken getNextToken() throws InvalidDataException {
        FormulaToken token;
        
        if (nextToken == null) {
            computeNextToken();
        }
        
        token = nextToken;
        nextToken = null;
        
        return token;
    }
    
    /**
     * Determines the next token available from the input and stores it in
     * {@code nextToken}, or stores an end-of-input token if the input has
     * been exhausted
     * 
     * @throws InvalidDataException if the next segment of this tokenizer's
     *         string cannot be parsed
     */
    private void computeNextToken() throws InvalidDataException {
        Pattern numberPattern;
        
        // Eat leading whitespace
        while ((position < formulaString.length())
                && (Character.isWhitespace(formulaString.charAt(position)))) {
            position++;
        }
        
        // Is there anything left to tokenize?
        if (position >= formulaString.length()) {
            nextToken = new FormulaToken(FormulaTokenType.END, "");
        } else {
            
            // Determine the token type, extract the token, and return it
            switch (formulaString.charAt(position)) {
                case '(':  // An opening parenthesis
                    position++;
                    nextToken = new FormulaToken(
                            FormulaTokenType.OPEN_PAREN, "(");
                    break;
                case ')':  // A closing parenthesis
                    position++;
                    nextToken = new FormulaToken(
                            FormulaTokenType.CLOSE_PAREN, ")");
                    break;
                case ',':  // A comma
                    position++;
                    nextToken = new FormulaToken(FormulaTokenType.COMMA, ",");
                    break;
                case '+':  // A positive sign
                    position++;
                    nextToken = new FormulaToken(FormulaTokenType.CHARGE, "+");
                    break;
                case '-':  // A negative sign
                    position++;
                    nextToken = new FormulaToken(FormulaTokenType.CHARGE, "-");
                    break;
                default:   // A counted item, non-unit charge, number, or error
                    StringBuilder sb = new StringBuilder();
                    Matcher matcher = TEXT_PATTERN.matcher(
                            formulaString.substring(position));
                    FormulaTokenType type = null;
                
                    if (matcher.lookingAt()) {
                        
                        /*
                         * A chemical symbol of some kind 
                         */
                        
                        String matched = matcher.group();
                        
                        type = FormulaTokenType.COUNTED_ITEM;
                        
                        /*
                         * This tokenizer attempts to pick up lazy empirical
                         * and moiety formula instances (which may be missing
                         * some or all required space characters) by testing
                         * the initial substring against known element symbols
                         * and restricting the match to such a symbol if found.
                         * This will in no way interfere with successful parsing
                         * even if it turns out to break up chemical
                         * abbreviations in some structural formulae.  (Any
                         * affected formulae are illegal anyway.) 
                         */
                        if ((matched.length() > 1) && (Element.isKnownSymbol(
                                matched.substring(0, 2)))) {
                            matched = matched.substring(0, 2);
                        } else if (Element.isKnownSymbol(
                                matched.substring(0, 1))) {
                            matched = matched.substring(0, 1);
                        }
                        sb.append(matched);
                    }
                    
                    numberPattern = ((type == null)
                           ? NUMBER_CHARGE_PATTERN
                           : NUMBER_PATTERN);
                    matcher = numberPattern.matcher(
                            formulaString.substring(position + sb.length()));
                    if (matcher.lookingAt()) {
                        sb.append(matcher.group());
                        if (type == null) {
                            // Assign the token type
                            
                            if (matcher.group(1) != null) {
                                // the number had an appended +/- sign
                                type = FormulaTokenType.CHARGE;
                            } else {
                                type = FormulaTokenType.NUMBER;
                            }
                        }
                    }
                    
                    position += sb.length();

                    if (type == null) {
                        throw new InvalidDataException(
                                "Parse error near '"
                                + formulaString.charAt(position) + "'",
                                formulaString,
                                InvalidDataException.MALFORMED_FORMULA);
                    } else {
                        nextToken = new FormulaToken(type, sb.toString());
                    }
            }  // end switch
        }
    }

    /**
     * Returns the full string being tokenized by this tokenizer
     * 
     * @return the {@code String} being tokenized by this tokenizer
     */
    public String getFormulaString() {
        return formulaString;
    }
}

/**
 * A {@code ChemicalFormulaBL.ChemicalFormulaVisitor} implementation that builds
 * up a string representation of the formula parts it visits as it traverses
 * them.  Instances of this class are not thread-safe, and do not have defined
 * behavior if visiting multiple formulae concurrently.  It is recommended that
 * instances not be shared between threads, but careful synchronization could
 * nevertheless make it safe to do so.
 * 
 * @author  jobollin
 * @version 0.9.0
 */
class StringBuildingVisitor extends ChemicalFormulaBL.FormulaWalker {
    
    /**
     * The work space in which this visitor constructs its string
     * representation of the chemical formula parts it visits
     */
    private final StringBuilder currentString = new StringBuilder();
    
    /**
     * The current group nesting depth, used to determine whether or not to
     * parenthesize groups 
     */
    private int groupDepth = 0;
    
    /**
     * Returns the string built (so far) by this visitor
     * 
     * @return the {@code String} built (to this point) by this visitor
     */
    public String getString() {
        return currentString.toString();
    }

    /**
     * Resets this visitor to be ready to build a string for a new formula
     * or formula part.  This method should not be invoked while this
     * visitor is performing a visit.
     */
    public void reset() {
        currentString.setLength(0);
        groupDepth = 0;
    }
    
    /**
     * {@inheritDoc}.  This implementation visits a
     * {@code ChemicalFormulaBL.ChemicalFormula} object by adding a
     * representation of it to the internal string as a comma-delimited series
     * of moieties.
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormula)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormula formula) {
        super.visit(formula);
        if (currentString.length() > 1) {
            currentString.setLength(currentString.length() - 2);
        }
    }

    /**
     * {@inheritDoc}.  This implementation visits a
     * {@code ChemicalFormulaBL.ChemicalFormulaMoiety} object by adding a
     * representation of it to the internal string.  Its multiplier will be
     * explicitly represented only if it is not unity, in which case the group
     * will also be enclosed in parentheses.
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaMoiety)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaMoiety moiety) {
        boolean unitMultiplier
                = BigDecimal.ONE.equals(moiety.getPreMultiplier());
        int lastCharPos;
        
        if (!unitMultiplier) {
            currentString.append(moiety.getPreMultiplier().toPlainString());
            currentString.append('(');
        }
        
        super.visit(moiety);
        
        lastCharPos = currentString.length() - 1;
        if (currentString.charAt(lastCharPos) == ' ') {
            currentString.setLength(lastCharPos);
        }
        if (!unitMultiplier) {
            currentString.append(')');
        }
        currentString.append(", ");
    }

    /**
     * {@inheritDoc}.  This implementation visits a
     * {@code ChemicalFormulaBL.ChemicalFormulaGroup} object by adding a
     * representation of it to the internal string.  Its multiplier will be
     * explicitly represented only if it is not unity, in which case the group
     * will also be enclosed in parentheses.
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaGroup)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaGroup group) {
        boolean unitMultiplier
                = BigDecimal.ONE.equals(group.getPostMultiplier());
        boolean shouldParenthesize = (!unitMultiplier || (groupDepth > 0));

        if (shouldParenthesize) {
            currentString.append('(');
        }
        
        groupDepth++;
        super.visit(group);
        groupDepth--;
        
        if (shouldParenthesize) {
            int lastCharPos = currentString.length() - 1;
            
            if (currentString.charAt(lastCharPos) == ' ') {
                currentString.setLength(lastCharPos);
            }
            currentString.append(')');
            
            if (!unitMultiplier) {
                currentString.append(group.getPostMultiplier().toPlainString());
            }
        }
        
        if (currentString.charAt(currentString.length() - 1) != ' ') {
            currentString.append(' ');
        }
    }

    /**
     * {@inheritDoc}.  This implementation visits an
     * {@code ChemicalFormulaBL.AtomCount} object by adding a representation of
     * it to the internal string.  The numeric count will be explicitly
     * represented only if it is not unity.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AtomCount)
     */
    public void visit(ChemicalFormulaBL.AtomCount count) {
        currentString.append(count.getAtomType().getSymbol());
        if (!BigDecimal.ONE.equals(count.getCount())) {
            currentString.append(count.getCount().toPlainString());
        }
        currentString.append(' ');
    }

    /**
     * {@inheritDoc}.  This implementation visits an
     * {@code ChemicalFormulaBL.AbbreviationCount} object by adding a
     * representation of it to the internal string.  The numeric count will be
     * explicitly represented only if it is not unity.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AbbreviationCount)
     */
    public void visit(ChemicalFormulaBL.AbbreviationCount count) {
        currentString.append(count.getAbbreviation());
        if (!BigDecimal.ONE.equals(count.getCount())) {
            currentString.append(count.getCount().toPlainString());
        }
        currentString.append(' ');
    }

    /**
     * {@inheritDoc}.  This implementation visits a {@code Charge} object by
     * adding a representation of it to the internal string, provided that
     * it is not {@link ChemicalFormulaBL.Charge#isNeutral() neutral}.  The
     * magnitude of the charge will be included in the representation only if it
     * is not unity.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.Charge)
     */
    public void visit(ChemicalFormulaBL.Charge charge) {
        if (!charge.isNeutral()) {
            if (!BigDecimal.ONE.equals(charge.getMagnitude())) {
                currentString.append(charge.getMagnitude());
            }
            currentString.append(charge.getSign());
        }
    }
}

/**
 * A {@code ChemicalFormulaBL.ChemicalFormulaVisitor} that traverses chemical
 * formulae and their parts to accumulate a sum of the count of atoms of each
 * element.  Instances of this class are not thread-safe, and do not have
 * defined behavior if visiting multiple formulae concurrently.  It is
 * recommended that instances not be shared between threads, but careful
 * synchronization could nevertheless make it safe to do so.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class CountingFormulaVisitor extends ChemicalFormulaBL.FormulaWalker {
    
    /**
     * The current value of the "ignore next multiplier" flag
     */
    private boolean ignoreNextMultiplier = false;
    
    /**
     * A {@code Map} of the current counts for each element
     */
    private final Map<Element, BigDecimal> countMap
            = new EnumMap<Element, BigDecimal>(Element.class);
    
    /**
     * A {@code List} of copies of the counts map saved when this visitor
     * descended into a moiety's group or a group's subgroup; this list is used
     * as a stack, and allows group and moiety multipliers to be applied in a
     * fairly straightforward manner
     */
    private final List<Map<Element, BigDecimal>> countStack
            = new LinkedList<Map<Element, BigDecimal>>();
    
    /**
     * A {@code List} of values of the "ignore next multiplier" flag saved when
     * this visitor descended into a moiety's group or a group's subgroup; this
     * list is used as a stack to preserve the correct values of that flag for
     * the containing context while operating with a possibly different value
     * in the inner context 
     */
    private final List<Boolean> ignoreStack = new LinkedList<Boolean>();
    
    /**
     * Returns the current value of the "ignore next multiplier" flag
     * 
     * @return the {@code boolean} value of the flag
     */
    public boolean isIgnoreNextMultiplier() {
        return ignoreNextMultiplier;
    }

    /**
     * Sets the "ignore next multiplier" flag; the flag generally should not be
     * externally adjusted while a visit is in progress.  This flag is useful
     * for counting the contents of a moiety or group without applying the
     * moiety or group multiplier.
     * 
     * @param  ignoreNextMultiplier the {@code boolean} value for the
     *         "ignore next multiplier" flag
     */
    public void setIgnoreNextMultiplier(boolean ignoreNextMultiplier) {
        this.ignoreNextMultiplier = ignoreNextMultiplier;
    }

    /**
     * Returns a copy of this visitor's internal counts, as a map from
     * {@code Element} to corresponding {@code BugDecimal} count
     *  
     * @return a {@code Map<Element, BigDecimal>} of the count so far of
     *         each element
     */
    public Map<Element, BigDecimal> getCounts() {
        return new EnumMap<Element, BigDecimal>(countMap);
    }
    
    /**
     * Resets this visitor to be ready to count a new formula or formula
     * part.  This method should not be invoked while this visitor is
     * performing a visit.
     */
    public void reset() {
        ignoreStack.clear();
        countStack.clear();
        countMap.clear();
        setIgnoreNextMultiplier(false);
    }
    
    /**
     * {@inheritDoc}.  This version updates this visitor's internal counts
     * with appropriate subtotals for the specified group, accounting for
     * the group multiplier unless this visitor is set to ignore the next
     * multiplier. 
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaGroup)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaGroup group) {
        BigDecimal multiplier = group.getPostMultiplier();
        
        if (!multiplier.equals(BigDecimal.ZERO)) {
            pushState();
            super.visit(group);
            popState(multiplier);
        }
    }

    /**
     * {@inheritDoc}.  This version updates this visitor's internal counts
     * with appropriate subtotals for the specified moiety, accounting for
     * the moiety multiplier unless this visitor is set to ignore the next
     * multiplier. 
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaMoiety)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaMoiety moiety) {
        BigDecimal multiplier = moiety.getPreMultiplier();
        
        if (!multiplier.equals(BigDecimal.ZERO)) {
            pushState();
            super.visit(moiety);
            popState(multiplier);
        }
    }

    /**
     * Stores the current counting state (counts of each element and value
     * of the {@code ignoreNextMultiplier} flag) on a stack, then clears
     * their current values (to zero counts and not ignoring multipliers).
     * This is appropriate when descending the formula tree from a moiety
     * to its group, or from a group to a subgroup.  An invocation of this
     * method should always be paired with a subsequent invocation of
     * {@code popState()}.
     * 
     * @see #popState(BigDecimal)
     */
    private void pushState() {
        countStack.add(0, getCounts());  // makes a copy of the counts
        ignoreStack.add(0, Boolean.valueOf(ignoreNextMultiplier));
        countMap.clear();
        ignoreNextMultiplier = false;
    }

    /**
     * Combines the current counts with those popped from the counting state
     * stack. The specified multiplier is applied to the <em>current</em>
     * counts before adding in the saved ones, provided that the popped
     * value of the {@code ignoreNextMultiplier} flag is {@code false}. An
     * invocation of this method should always be paired with an preceding
     * invocation of {@code pushState()}.
     *  
     * @param  multiplier the {@code BigDecimal} multiplier to apply to the
     *         current counts before adding in the stored ones, provided
     *         that the stored {@code ignoreNextMultiplier} flag value
     *         does not specify that the multiplier be ignored
     *         
     * @see #pushState()
     */
    private void popState(BigDecimal multiplier) {
        try {
            
            /*
             * Pop the previous ignoreNextElement value; this is the one
             * that will apply to the formula sub-part that was just read
             */
            ignoreNextMultiplier = ignoreStack.remove(0);

            /*
             * Apply the specified multiplier unless set to ignore it
             */
            if (!multiplier.equals(BigDecimal.ONE)
                    && !ignoreNextMultiplier) {
                for (Map.Entry<Element, BigDecimal> entry
                        : countMap.entrySet()) {
                    BigDecimal value = entry.getValue();
                    
                    value = value.multiply(multiplier).stripTrailingZeros();
                    if (value.scale() < 0) {
                        value.setScale(0);
                    }
                    entry.setValue(value);
                }
            }
            
            /*
             * Add in the saved counts
             */
            for (Map.Entry<Element, BigDecimal> entry
                    : countStack.remove(0).entrySet()) {
                Element e = entry.getKey();
                BigDecimal savedCount = entry.getValue();
                BigDecimal currentCount = countMap.get(e);
                
                if (currentCount == null) {
                    countMap.put(e, savedCount);
                } else {
                    countMap.put(e, currentCount.add(savedCount));
                }
            }
        } catch (IndexOutOfBoundsException ioobe) {
            throw new IllegalStateException(
                    "Popped more states than were pushed");
        }
    }

    /**
     * {@inheritDoc}.  This version always throws an exception because atoms
     * in abbreviations cannot be counted
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AbbreviationCount)
     * 
     * @throws IllegalArgumentException every time
     */
    public void visit(@SuppressWarnings("unused")
            ChemicalFormulaBL.AbbreviationCount count) {
        throw new IllegalArgumentException(
                "Cannot count atoms in abbreviations");
    }

    /**
     * {@inheritDoc}.  This implementation adds the count to this visitor's
     * internal count for its element
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AtomCount)
     */
    public void visit(ChemicalFormulaBL.AtomCount count) {
        BigDecimal number = count.getCount();
        
        if (!number.equals(BigDecimal.ZERO)) {
            Element e = count.getAtomType();
            BigDecimal currentCount = countMap.get(e);
            
            if (currentCount == null) {
                countMap.put(e, number);
            } else {
                countMap.put(e, currentCount.add(number));
            }
        }
    }

    /**
     * {@inheritDoc}.  This implementation does nothing.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.Charge)
     */
    public void visit(@SuppressWarnings("unused")
            ChemicalFormulaBL.Charge charge) {
        // do nothing
    }
}

/**
 * A {@code ChemicalFormulaVisitor} implementation that examines a
 * {@code ChemicalFormula} (or {@code ChemicalFormulaMoiety}) to determine
 * whether it constitutes a valid empirical formula.  This test is stronger
 * than and inclusive of the empiricial ion formula validity test; in addition
 * to the conditions for validity of the empirical formula of an ion, an
 * "normal" empirical formula's one moiety must be electrically neutral.
 * Instances of this class are not thread-safe, and do not have defined
 * behavior if visiting multiple formulae concurrently.  It is recommended that
 * instances not be shared between threads, but careful synchronization could
 * nevertheless make it safe to do so.  
 * 
 * @author jobollin
 * @version 0.9.0
 */
class EmpiricalFormulaValidator extends EmpiricalIonFormulaValidator {

    /**
     * Visits a {@code ChemicalFormulaBL.Charge} to determine whether the
     * formula that contains it is invalid as an empirical formula.  That is the
     * case unless the charge is
     * {@link ChemicalFormulaBL.Charge#isNeutral() neutral}.
     * 
     * @param  charge the {@code ChemicalFormulaBL.Charge} to visit
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.Charge)
     */
    @Override
    public void visit(ChemicalFormulaBL.Charge charge) {
        if (!charge.isNeutral()) {
            setInvalid();
        }
    }
}

/**
 * A {@code ChemicalFormulaVisitor} implementation that examines a
 * {@code ChemicalFormula} (or {@code ChemicalFormulaMoiety}) to determine
 * whether it constitutes a valid empirical formula for an ion.  This test is
 * stronger than and inclusive of the moiety formula validity test; in addition
 * to the conditions for validity of a moiety formula, an empirical formula
 * must contain exactly one moiety, which must have no (non-unit) premultiplier.
 * Instances of this class are not thread-safe, and do not have defined
 * behavior if visiting multiple formulae concurrently.  It is recommended that
 * instances not be shared between threads, but careful synchronization could
 * nevertheless make it safe to do so.  
 * 
 * @author jobollin
 * @version 0.9.0
 */
class EmpiricalIonFormulaValidator extends MoietyFormulaValidator {
    
    /**
     * Determines whether the formula visited by this visitor is a valid
     * empirical formula (to this point in the visit)
     * 
     * @return {@code true} if the formula has so far not been determined to
     *         be invalid
     *         
     * @see MoietyFormulaValidator#isValid()
     */
    @Override
    public boolean isValid() {
        return (super.isValid() && (getMoietyCount() == 1));
    }

    /**
     * {@inheritDoc}.  This version ensures that the moiety premultiplier is
     * unity (the formula is otherwise invalid), and if so, updates the moiety
     * count and traverses the moiety's children
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaMoiety)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaMoiety moiety) {
        if (moiety.getPreMultiplier().equals(BigDecimal.ONE)) {
            super.visit(moiety);
        } else {
            setInvalid();
        }
    }
}

/**
 * A {@code ChemicalFormulaBL.ChemicalFormulaVisitor} implementation that
 * examines a {@code ChemicalFormulaBL.ChemicalFormula} (or 
 * @code ChemicalFormulaBL.ChemicalFormulaMoiety}) to determine
 * whether it constitutes a valid moiety formula.  Instances of this class are
 * not thread-safe, and do not have defined behavior if visiting multiple
 * formulae concurrently.  It is recommended that instances not be shared
 * between threads, but careful synchronization could nevertheless make it safe
 * to do so.
 * 
 * @author jobollin
 * @version 0.9.0
 */
class MoietyFormulaValidator extends ChemicalFormulaBL.FormulaWalker {
    
    /**
     * An internal flag indicating whether this visitor has yet encountered
     * an invalid formula part
     */
    private boolean valid = true;
    
    /**
     * The count of moieties visited bvy this visitor
     */
    private int moietyCount = 0;
    
    /**
     * An internal-only flag by which this visitor recognizes whether or not
     * it is visiting parts of a group
     */
    private boolean inGroup = false;
    
    /**
     * Returns the current value of this visitor's validity flag; once this
     * visitor has completed a visit of a {@code ChemicalFormula}, the value
     * of this flag determines whether or not the formula was found valid
     * 
     * @return {@code true} if the formula visited was valid or if no
     *         formula has yet been visited; {@code false} if a formula has
     *         been visited and found invalid; undefined if this visitor is
     *         currently performing a visitation
     */
    public boolean isValid() {
        return (valid && (getMoietyCount() > 0));
    }
    
    /**
     * Marks this visitor has having visited an invalid formula
     */
    public void setInvalid() {
        valid = false;
    }

    /**
     * Gets the number of moieties traversed by this visitor.  All moieties
     * are counted even if the formula is found invalid.
     * 
     * @return the number of moieties visited by this visitor
     */
    public int getMoietyCount() {
        return moietyCount;
    }

    /**
     * Resets this visitor to be ready to validate a new formula.  This
     * method should not be invoked while this visitor is performing a
     * visit.
     */
    public void reset() {
        valid = true;
        moietyCount = 0;
        inGroup = false;
    }
    
    /**
     * {@inheritDoc}.  This version tests whether the group is a top-level
     * group or a subgroup; if the former (and the formula appears valid so
     * far, and the group's multiplier is 1) then the group's parts are
     * traversed, but otherwise the formula is flagged invalid
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaGroup)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaGroup group) {
        if (isValid()) {
            if (inGroup
                    || !group.getPostMultiplier().equals(BigDecimal.ONE)) {
                setInvalid();
            } else {
                inGroup = true;
                super.visit(group);
                inGroup = false;
            }
        }
    }

    /**
     * {@inheritDoc}.  This version increments the moiety count and, if
     * this formula is valid so far, traverses the specified moiety's
     * children
     * 
     * @see ChemicalFormulaBL.FormulaWalker#visit(
     *      ChemicalFormulaBL.ChemicalFormulaMoiety)
     */
    @Override
    public void visit(ChemicalFormulaBL.ChemicalFormulaMoiety moiety) {
        moietyCount++;
        if (isValid()) {
            super.visit(moiety);
        }
    }

    /**
     * {@inheritDoc}.  This version does nothing.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AtomCount)
     */
    public void visit(@SuppressWarnings("unused")
            ChemicalFormulaBL.AtomCount count) {
        // do nothing
    }

    /**
     * {@inheritDoc}.  This version marks the formula invalid.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.AbbreviationCount)
     */
    public void visit(@SuppressWarnings("unused")
            ChemicalFormulaBL.AbbreviationCount count) {
        setInvalid();
    }

    /**
     * {@inheritDoc}.  This version does nothing.
     * 
     * @see ChemicalFormulaBL.ChemicalFormulaVisitor#visit(
     *      ChemicalFormulaBL.Charge)
     */
    public void visit(@SuppressWarnings("unused")
            ChemicalFormulaBL.Charge charge) {
        // do nothing
    }
}
