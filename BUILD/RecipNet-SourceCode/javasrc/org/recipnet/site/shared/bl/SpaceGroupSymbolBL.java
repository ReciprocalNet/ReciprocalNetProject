/*
 * Reciprocal Net Project
 *
 * SpaceGroupSymbolBL.java
 *
 * Copyright (c) 2004 The Trustees of Indiana University.  All rights reserved.
 *
 * 07-Dec-2004: jobollin completed first draft
 * 05-Dec-2005: jobollin extracted OperatorMatrix to new common class
 *              SymmetryMatrix; added type arguments where appropriate;
 *              converted several for loops to the new syntax; accommodated the
 *              switch in SymmetryMatrix to an enum for operation types; updated
 *              docs
 */

package org.recipnet.site.shared.bl;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.recipnet.common.SymmetryMatrix;
import org.recipnet.site.InvalidDataException;
import org.recipnet.site.UnexpectedExceptionException;

/**
 * <p>
 * A business logic class encompassing knowledge of space group symbols.  In
 * particular, this class provides for converting among various string
 * representations of space group symbols, for creating a "digested"
 * version that has structure useful for marking up a space group symbol,
 * and for determining whether a particular symbol is valid (i.e. whether it
 * represents a real space group).
 * </p><p>
 * This class supports these categories of space group symbols:
 * <dl>
 * <dt>raw symbols</dt>
 * <dd>{@code String}s containing space group symbols in a form provided by
 * a system user.  {@code SampleDataInfo.spgp} contains raw symbols (by
 * definition), and any space group symbol entered by a user into any of the
 * system's web forms is likewise a raw symbol</dd>
 * <dt>formatted symbols</dt>
 * <dd>{@code String}s containing space group symbols in a form processed
 * into a standard format by the {@code createFormattedSymbol(String)}
 * method of this class.  Symbols of this type are not guaranteed to represent
 * valid space groups, or to represent standard settings or standard choices of
 * symbol when they do represent valid space groups.  They will, however,
 * contain a valid, capitalized, centering symbol followed by between zero and
 * three character combinations that can be interpreted as operators, seperated
 * by single spaces, with all letters lowercase, screw axes represented as
 * a pair of consecutive digits, and rotoinversion operators represented as
 * a minus sign followed by a digit.  The format is that specified by the CIF
 * core dictionary for Hermann-Mauguin space group symbols in CIF.</dd>
 * <dt>canonical symbols</dt>
 * <dd>{@code String}s containing space group symbols in the same format
 * as standard symbols, created from formatted symbols by the
 * {@code createCanonicalSymbol(String)} method of this class.  These
 * symbols are guaranteed to represent valid space groups, and each space group
 * has exactly one canonical symbol to represent it.  The two members of each
 * enantiomorphic pair of space groups share a canonical symbol; otherwise all
 * space groups have distinct canonical symbols.  Canonical symbols correspond
 * to international short space group symbols as defined in the International
 * Tables for X-ray Crystallography (4th edition, 1996).  The system's space
 * group search index records space group symbols in canonical form so that it
 * is independent of the form and setting provided by the user.</dd>
 * <dt>digested symbols</dt>
 * <dd>Objects of type {@code SpaceGroupSymbol}, created by the
 * {@code digestSymbol(String)} method of this class.  This kind of symbol
 * has a structured form suitable for analysis or for creating marked up symbol
 * representations for display.</dd>
 * </dl>
 * </p><p>
 * Other nested classes:<br/>
 * The content of a {@code SpaceGroupSymbol} object consists of a
 * {@code char} centering symbol and an {@code OperatorList} object.
 * {@code OperatorList} is a custom unmodifiable {@code List}
 * implementation that restricts elements to objects of type
 * {@code Operator}, and provides a specifically-typed
 * {@code getOperator(int)} method.  {@code Operator} provides a
 * symbolic representation of the symmetry along one symmetry direction of a
 * space group, compatible with the Hermann-Mauguin system of space group
 * symbols: it provides the order of symmetry, the order of rotational symmetry,
 * whether or not the symbol corresponds to a rotoinversion, the magnitude of
 * any screw translation, and the nature of any (glide) reflection.  (These
 * characteristics are not fully independent.)
 * </p>
 *
 * @author jobollin
 * @version 0.9.0
 */
public class SpaceGroupSymbolBL {

    /*
     * Implementation rationale with respect to assertions and exceptions:
     *
     * This code makes considerable use of assertions to check that the
     * expectations of program state at various points agree with the actual
     * state.  In particular, this is done at many points to check the
     * arguments to private and package-private methods: because only other
     * methods of this class can invoke this class' private methods, it is
     * reasonable to assume that the arguments received match those that are
     * supposed to be sent, and to check strictly via assertion.
     *
     * Wherever this class' code needs to check object properties or program
     * state that is not exclusive to this class, however, it performs normal
     * Java checks; it throws exceptions when such expectations are not met.
     * Generally the exception thrown in such a case is either an
     * IllegalArgumentException or (more common) an InvalidDataException.
     */

    /*
     * Additional implementation notes:
     * 
     * This is a rather hairy class, with a large number of named nested
     * classes, most of them intended for internal use only.  In order to do
     * its job, the code draws on elements of group theory (central to the
     * purpose of the class) and linear algebra / matrix theory (convenient
     * for implementation).  The mathematical details underpinning the logic
     * herein are discussed at length in volume A ("Space Group Symmetry") of
     * the canonical reference work for Crystallography: the International
     * Tables for Crystallography.  This code was developed with reference to
     * the fourth, revised edition, printed in 1996.  For a gentler, and much
     * briefer, introduction to symmetry and space groups, consult the
     * Reciprocal Net project space group symbol reference document.
     * 
     * The key functional Java class in this system is LaueClass, which
     * represents the characteristics defining the classes of space groups
     * resulting from the so-called Laue classification system, which
     * categorizes space groups according to the kind and relative orientation
     * of their constituent symmetry elements.  LaueClass provides a static
     * member correctly configured for each of the fourteen Laue classes for
     * three-dimensional space groups, plus a fifteenth instance that serves
     * the special cases needs of certain members of the high-symmetry cubic
     * Laue class.  Although the nature and orientation of the symmetry elements
     * are the key characteristics of a Laue class, LaueClass also contains
     * information about the types of lattice centering with which it is
     * consistent, about the details of the Hermann-Mauguin symbology
     * conventions for class members, and about matrix representations of
     * the symmetry operations of group members.  Some of these features are
     * supported by the OperatorComparator, AbstractSymbolAnalyzer (subclass),
     * and MatrixManager instances assigned to each LaueClass instance. 
     * Delegating some of the responsibilities to associated classes avoids a
     * complex hierarchy of LaueClass subclasses, and reduces code duplication.
     * 
     * The key data class in this system is SpaceGroup, which is mainly a
     * container for a related SpaceGroupSymbol object, LaueClass, and Set of
     * SymmetryMatrix objects representing the symmetry operations of a space
     * group.  LaueClass instances are, among other things, factories for
     * SpaceGroup objects, which they generate based on SpaceGroupSymbol
     * objects; this is the normal means for obtaining SpaceGroup objects, and
     * is the heaviest work that the space group symbol logic performs.
     * 
     * Other than subclasses of classes already mentioned, there remain the
     * OperatorSequence and ShelxOperatorEnumeration classes, which are used in
     * formatting raw symbols.  They simplify the code of createFormattedSymbol
     * to a remarkable degree, and allow all supported raw symbol styles to be
     * handled generically.
     * 
     * Access modifiers for methods and constructors in this business logic
     * subsystem have been carefully chosen with an eye towards both expressing
     * the intended use of the system and being resilient to possible future
     * code reorganization.  Most of the nested classes are package-private,
     * indicating that they are intended for internal use only (although they
     * are also accessible to other business logic classes).  These classes are
     * not private within SpaceGroupSymbolBL to enable them to be unit tested,
     * and in anticipation that some or all of them may in the future be raised
     * to top-level classes.  In all classes of this subsystem, methods and
     * constructors are assigned public access when they are intended to be used
     * by any class that has no inheritence relationship with the host class,
     * _but which has access to the host class_.  Methods are assigned protected
     * access only when they inherit it from an overridden method or sometimes
     * when the method is intended to be overridden by subclasses.  Methods and
     * constructors are assigned package-private (default) access when they
     * are intended for internal use within the space group symbol business
     * logic only, regardless of class access, especially including when they
     * have package-private classes of this subsystem for argument or return
     * types.  A few methods and constructors are private, with the normal
     * implications.
     */

    /**
     * A String containing the characters that are valid centering symbols;
     * no one of these is consistent with all possible combinations of symmetry
     * elements.  Code depends on the specific sequence of the characters.
     */
    private final static String VALID_CENTERING = "PABCIFR";

    /**
     * A String containing the characters that are valid mirror / glide symbols;
     * they are listed in order of precedence for constructing space group
     * symbols conforming to the international standard -- code depends on this
     * sequence
     */
    private final static String VALID_REFLECTIONS = "mabcnd";

    /**
     * A String containing the characters that are valid pure rotation symbols;
     * they are listed in order of precedence for constructing space group
     * symbols conforming to the international standard, but code does not rely
     * on the specific sequence of characters in this String
     */
    private final static String VALID_ROTATIONS = "64321";

    /**
     * A named constant designating the absence of a symmetry direction: either
     * no special direction is relevant (e.g. for a pure translation) or none
     * has yet been assigned
     */
    public final static int DIRECTION_NONE = -1;

    /*
     * The symmetry direction constants have consecutive values starting at zero
     * and increasing monotonically.  Code has some dependency on the
     * specific choices of these values, particularly those of the directions
     * parallel to unit cell axes.
     * 
     * Enough directions are symbolized to support the requirements of this
     * class, but by no means are all possible directions distinguished.  Most
     * of the diagonal directions, in particular, are represented by either
     * DIRECTION_FACE_DIAG or DIRECTION_BODY_DIAG; DIRECTION_FACE_DIAG_ALT is
     * a special case in that regard that is required only because certain
     * cubic groups require a special choice of representative face diagonal
     * direction.  
     */

    /**
     * A named constant used to designate the direction parallel to the 'a' axis
     * of a unit cell, for the purpose of designating or generating symmetry
     * operations related to that direction.
     */
    public final static int DIRECTION_A = 0;

    /**
     * A named constant used to designate the direction parallel to the 'b' axis
     * of a unit cell, for the purpose of designating or generating symmetry
     * operations related to that direction.
     */
    public final static int DIRECTION_B = 1;

    /**
     * A named constant used to designate the direction parallel to the 'c' axis
     * of a unit cell, for the purpose of designating or generating symmetry
     * operations related to that direction.
     */
    public final static int DIRECTION_C = 2;

    /**
     * A named constant used to designate the direction parallel to the body
     * diagonal of a unit cell, for the purpose of designating or generating
     * symmetry operations related to that direction.  Rhombohedral and cubic
     * groups are characterized by symmetry elements in this direction; it is
     * not a symmetry direction in other types of groups.
     */
    public final static int DIRECTION_BODY_DIAG = 3;

    /**
     * A named constant used to designate the directions parallel to [certain]
     * face diagonals of a unit cell, for the purpose of designating or
     * generating symmetry operations related to those axes.  Such symmetry
     * elements come in symmetry-related sets, but only one member of such a set
     * is required (along with the other symmetry elements) to generate any
     * space group having this kind of symmetry direction
     */
    public final static int DIRECTION_FACE_DIAG = 4;

    /**
     * A named constant used to designate the directions parallel to [certain]
     * face diagonals of a unit cell, for the purpose of designating or
     * generating symmetry operations related to those axes.  This code is used
     * only with certain cubic groups (those having point symmetry 432 or 43m),
     * where it is necessary to use a different characteristric member of the
     * face diagonal set in order to generate the full symmetry of the group.
     */
    public final static int DIRECTION_FACE_DIAG_ALT = 5;

    /**
     * A SpaceGroupSymbol containing the canonical symbol for space group
     * I b c a; useful because when analyzing a SpaceGroup to extract the
     * symbol, I b c a is a special case that does not follow the normal
     * standardization rules, and it is convenient in that event to shortcut
     * the symbol generation
     */
    private final static SpaceGroupSymbol IBCA_SYMBOL;

    /**
     * A SpaceGroupSymbol containing the canonical symbol for space group
     * I 2 2 2; useful because when analyzing a SpaceGroup to extract the
     * symbol, I 2 2 2 must be distinguished from I 21 21 21 as a special case,
     * and it is convenient in that event to shortcut the symbol generation
     */
    private final static SpaceGroupSymbol I222_SYMBOL;

    /**
     * A SpaceGroupSymbol containing the canonical symbol for space group
     * I 21 21 21; useful because when analyzing a SpaceGroup to extract the
     * symbol, I 2 2 2 must be distinguished from I 21 21 21 as a special case,
     * and it is convenient in that event to shortcut the symbol generation
     */
    private final static SpaceGroupSymbol I212121_SYMBOL;

    /**
     * A SpaceGroupSymbol containing the canonical symbol for space group
     * I 2 3; useful because when analyzing a SpaceGroup to extract the
     * symbol, I 2 3 must be distinguished from I 21 3 as a special case,
     * and it is convenient in that event to shortcut the symbol generation
     */
    private final static SpaceGroupSymbol I23_SYMBOL;

    /**
     * A SpaceGroupSymbol containing the canonical symbol for space group
     * I 21 3; useful because when analyzing a SpaceGroup to extract the
     * symbol, I 2 3 must be distinguished from I 21 3 as a special case,
     * and it is convenient in that event to shortcut the symbol generation
     */
    private final static SpaceGroupSymbol I213_SYMBOL;

    /*
     * The five SpaceGroupSymbol constants above must be initialized in a static
     * initializer block because their initialization is performed via the
     * digestSymbol method (and that method can throw a checked exception).
     */
    static {
        SpaceGroupSymbol ibca = null;
        SpaceGroupSymbol i222 = null;
        SpaceGroupSymbol i212121 = null;
        SpaceGroupSymbol i23 = null;
        SpaceGroupSymbol i213 = null;

        try {
            ibca = digestSymbol("I b c a");
            i222 = digestSymbol("I 2 2 2");
            i212121 = digestSymbol("I 21 21 21");
            i23 = digestSymbol("I 2 3");
            i213 = digestSymbol("I 21 3");
        } catch (InvalidDataException ide) {
            /*
             * this should never happen because the digestSymbol arguments
             * above are valid
             */
            assert false;
        }

        /*
         * Cannot directly initialize these in a try block because they are
         * final, and must be definitely initialized (exactly once)
         */
        IBCA_SYMBOL = ibca;
        I222_SYMBOL = i222;
        I212121_SYMBOL = i212121;
        I23_SYMBOL = i23;
        I213_SYMBOL = i213;
    }

    /**
     * Because this class' only constructor is private, it cannot be externally
     * instantiated or extended.  It is not internally instantiated or extended
     * either, so there will be no instances.
     */
    private SpaceGroupSymbolBL() {
        // nothing to do
    }

    /**
     * Interprets a raw space group symbol to create a formatted version.  It
     * is possible that the formatted version contains the same sequence of
     * characters as the raw version.
     *
     * @param  rawSymbol a string containing the raw symbol to format
     *
     * @return a string containing the formatted symbol corresponding to
     *         {@code rawSymbol}; may invalid or empty if
     *         {@code rawSymbol} is not recognized as a valid space group
     *         symbol, but will not be {@code null}
     */
    public static String createFormattedSymbol(String rawSymbol) {
        StringBuffer formatted = new StringBuffer();
        Enumeration<String> operators;
        String raw =
            rawSymbol.replaceAll("\\s+", " ").replaceAll(" ?/ ?", "/").trim();

        if (raw.length() == 0) {
            return "";
        }

        // The first character should be the centering symbol
        formatted.append(raw.substring(0, 1).toUpperCase());

        if (raw.indexOf(' ') < 0) {
            // SHELX format
            operators = new OperatorSequence(
                    new ShelxOperatorEnumeration(raw.substring(1)));
        } else {
            // CIF, LASL, or similar space-delimited format
            operators = new OperatorSequence(
                    new StringTokenizer(raw.substring(1)));
        }

        // extract per-direction symmetry descriptors
        while (operators.hasMoreElements()) {
            String op = operators.nextElement();

            formatted.append(' ');
            if (op.endsWith("bar")) {
                formatted.append('-').append(op.substring(0, op.length() - 3));
            } else {
                formatted.append(op);
            }
        }

        return formatted.toString();
    }

    /**
     * Determines whether the specified formatted symbol is a valid space group
     * symbol (i.e. it represents a real space group, albeit not necessarilly in
     * canonical form).  This is mainly a convenience method, as determining
     * whether a symbol is valid requires approximately the same work as
     * attempting to create the canonical version of it.  In fact, this
     * implementation just relies on {@link #createCanonicalSymbol(String)} to
     * throw an exception in the case of an invalid symbol.
     *
     * @param  formattedSymbol a String containing the formatted symbol to test,
     *         as returned by {@link #createFormattedSymbol(String)}
     *
     * @return {@code true} if the formatted symbol is valid, otherwise
     *         {@code false}
     */
    public static boolean isSymbolValid(String formattedSymbol) {
        try {
            createCanonicalSymbol(formattedSymbol);
            return true;
        } catch (InvalidDataException ide) {
            // Symbol is not valid
            return false;
        }
    }

    /**
     * Interprets a formatted space group symbol to create a canonical version.
     * It is possible that the canonical version contains the same sequence of
     * characters as the formatted version.  It is wasteful to validate the
     * input with {@link #isSymbolValid(String)} before passing it to this
     * method, as that method performs all the same (expensive) work that this
     * one does; this method will throw an InvalidDataException if the argument
     * is not a valid space group symbol.
     *
     * @param  formattedSymbol a string containing the formatted symbol, as
     *         returned by {@link #createFormattedSymbol(String)}
     *
     * @return a string containing the canonical symbol corresponding to
     *         {@code formattedSymbol}
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         provided space group symbol is not "formatted" or does not
     *         represent a valid space group
     */
    public static String createCanonicalSymbol(String formattedSymbol)
            throws InvalidDataException {
        SpaceGroupSymbol symbol = digestSymbol(formattedSymbol);
        LaueClass laueClass = determineLaueClass(symbol);

        // most of the hard work happens here:
        SpaceGroup group = laueClass.generateGroup(symbol);

        List<SymmetryMatrix> translations = new ArrayList<SymmetryMatrix>();
        List<Operator>[] axialOps = new List[6];
        char latticeCentering;
        SpaceGroupSymbol newSymbol;

        extractOperations(group, axialOps, translations);

        latticeCentering = determineCenteringSymbol(translations);
        if (!laueClass.allowsCentering(latticeCentering)) {
            throw new InvalidDataException(
                    "Invalid symbol (generates invalid lattice centering),"
                    + " interpreted as: " + symbol,
                    formattedSymbol,
                    InvalidDataException.ILLEGAL_SPGP);
        } else if (numLatticePoints(latticeCentering)
                    != numLatticePoints(symbol.getCentering())) {
            /*
             * If a different centering symbol was implied by the space group
             * translations or standard setting than was originally specified,
             * then it should represent the same number of lattice points
             * (i.e. it should not cause the size of the reduced cell to change)
             */
            throw new InvalidDataException(
                    "Invalid symbol (generates inconsistent lattice centering),"
                    + " interpreted as: " + symbol + "; computed centering: "
                    + latticeCentering,
                    formattedSymbol,
                    InvalidDataException.ILLEGAL_SPGP);
        }

        /*
         * verify the internal consistency of the symbol, derived symmetry
         * operations, and chosen Laue class
         */
        laueClass.getSymbolAnalyzer().verifySymmetry(symbol, axialOps);

        /*
         * The return value is produced in standard form by constructing a
         * corresponding SpaceGroupSymbol object and invoking its toString()
         * method
         */
        if (laueClass == LaueClass.TRICLINIC) {
            // The original symbol is fine
            newSymbol = symbol;
        } else {
            // Check for a member of one of the special pairs or Ibca
            newSymbol = identifySpecialGroup(latticeCentering, laueClass,
                                              axialOps);

            if (newSymbol == null) {
                // generate a canonical symbol from the symmetry operators
                try {
                    newSymbol = new SpaceGroupSymbol(latticeCentering,
                            chooseCanonicalOperators(axialOps, laueClass,
                                                     latticeCentering));
                } catch (IllegalArgumentException iae) {
                    throw new InvalidDataException(
                            "Invalid group (" + iae.getMessage()
                            + "), interpreted as: " + formattedSymbol,
                            formattedSymbol,
                            InvalidDataException.ILLEGAL_SPGP);
                }
            }
        }

        return newSymbol.toString();
    }

    /**
     * "Digests" a formatted symbol to create a structured object useful in
     * analyzing the symbol or creating a marked-up representation.  It is not
     * useful to test for validity with {@link #isSymbolValid(String)} prior to
     * invoking this method, as that method relies on this one and performs
     * considerable additional work as well.
     *
     * @param  formattedSymbol a string containing the formatted symbol, as
     *         returned by {@link #createFormattedSymbol(String)}
     *
     * @return a SpaceGroupSymbol corresponding to the provided formatted
     *         symbol; not guaranteed to correspond to a real space group or
     *         a standard setting
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         provided formatted symbol is syntactically invalid, including if
     *         it is incorrectly formatted or if it contains symbols that do
     *         not represent real operators valid for space groups
     */
    public static SpaceGroupSymbol digestSymbol(String formattedSymbol)
            throws InvalidDataException {
        try {
            List<Operator> operators = new ArrayList<Operator>(3);
            StringTokenizer st = new StringTokenizer(formattedSymbol, " ");
            char centering = getCentering(st.nextToken());

            // Process remaining tokens as operators
            while (st.hasMoreTokens()) {
                StringTokenizer opt =
                    new StringTokenizer(st.nextToken(), "/", true);
                String firstPart = opt.nextToken();
                char c0 = firstPart.charAt(0);

                if (c0 == '-') {

                    // looks like a rotoinversion

                    char rotation = firstPart.charAt(1);

                    if (opt.hasMoreTokens()
                            || (rotation == '2')
                            || (VALID_ROTATIONS.indexOf(rotation) == -1)
                            || (firstPart.length() > 2)) {
                        throw new InvalidDataException(
                                "Invalid rotoinversion in symbol: "
                                        + formattedSymbol,
                                formattedSymbol,
                                InvalidDataException.ILLEGAL_SPGP);
                    } else {
                        operators.add(
                                new Operator(rotation - '0', true, 0, ' '));
                    }
                } else if (VALID_ROTATIONS.indexOf(c0) != -1) {

                    // looks like a proper rotation, possibly a screw rotation

                    int order = c0 - '0';
                    int screw = (firstPart.length() == 1)
                            ? 0
                            : getScrewPart(firstPart, order);
                    char mirror;

                    if (opt.hasMoreTokens()) {
                        opt.nextToken();
                        mirror = getMirrorPart(opt.nextToken());
                    } else {
                        mirror = ' ';
                    }

                    operators.add(new Operator(order, false, screw, mirror));
                } else {

                    // if valid then it must be a mirror

                    if (opt.hasMoreTokens()) {
                        throw new InvalidDataException(
                                "Invalid symbol: " + formattedSymbol,
                                formattedSymbol,
                                InvalidDataException.ILLEGAL_SPGP);
                    } else {
                        operators.add(new Operator(0, false, 0,
                                                   getMirrorPart(firstPart)));
                    }
                }
            }

            return new SpaceGroupSymbol(centering,
                                        new OperatorList(operators));

        } catch (IndexOutOfBoundsException ioobe) {
            throw new InvalidDataException("Invalid symbol: " + formattedSymbol,
                    formattedSymbol,
                    InvalidDataException.ILLEGAL_SPGP);
        } catch (NoSuchElementException nsee) {
            throw new InvalidDataException("Invalid symbol: " + formattedSymbol,
                    formattedSymbol,
                    InvalidDataException.ILLEGAL_SPGP);
        } catch (NumberFormatException nfe) {
            throw new InvalidDataException("Invalid symbol: " + formattedSymbol,
                    formattedSymbol,
                    InvalidDataException.ILLEGAL_SPGP);
        }
    }

    /**
     * <p>
     * A helper method for {@link #createCanonicalSymbol(String)} that
     * categorizes the symmetry operations of the provided
     * {@code SpaceGroup}.  The members of the provided {@code List[]}
     * are initialized, and then populated with {@code Operator} objects
     * corresponding to those symmetry operations of the group that are neither
     * pure inversions nor pure translations.  Operators are assigned to lists
     * according to the symmetry directions of the operations they represent.
     * <em>All</em> lists will be assigned at least one operator; if there
     * otherwise is no symmetry along a particular direction then a one-fold
     * rotation will be assigned to the corresponding list.
     * </p><p>
     * For pure translations, including the identity operation, the
     * corresponding {@code SymmetryMatrix} objects are added to the
     * provided {@code List} of translations.  (These can be analyzed
     * together to determine a centering symbol; see
     * {@link #determineCenteringSymbol(List)}.)
     * </p>
     *
     * @param  group the {@code SpaceGroup} from which to draw symmetry
     *         operations
     * @param  axialOps a {@code List[]} at least as long as the largest
     *         numeric value {@code group}'s characteristic symmetry
     *         direction codes, plus one; this method fills the array with
     *         {@code List}s of {@code Operator}s for the symmetry
     *         operations aligned along the direction for which the array index
     *         corresponds to the direction code; all elements will be replaced
     *         with new {@code List}s
     * @param  translations a {@code List} into which all translation
     *         {@code SymmetryMatrix} objects from {@code group} will
     *         be placed; not otherwise manipulated by this method
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if any of the
     *         matrices of the provided group cannot be interpreted by the
     *         group's {@code LaueClass}
     */
    static void extractOperations(SpaceGroup group, List<Operator>[] axialOps,
            List<SymmetryMatrix> translations) throws InvalidDataException {
        LaueClass laueClass = group.getLaueClass();

        // Create lists
        for (int i = 0; i < axialOps.length; i++) {
            axialOps[i] = new ArrayList<Operator>();
        }

        // categorize symbols according to their types and symmetry directions
        for (SymmetryMatrix matrix : group.getMatrixSet()) {
            SymmetryMatrix.Type type = matrix.getType();

            if (type == SymmetryMatrix.Type.TRANSLATION) {

                // a pure translation (including the identity)
                translations.add(matrix);
            } else if (type == SymmetryMatrix.Type.INVERSION) { // an inversion
                
                /*
                 * ignore it; it has no inherent direction, and it only needs to
                 * be symbolized in the triclinic case, which receives special
                 * handling anyway
                 */
                 
            } else {
                try {
                    Operator op =
                            laueClass.getMatrixManager().determineOperator(matrix);

                    axialOps[op.getDirection()].add(op);
                } catch (IllegalArgumentException iae) {
                    throw new InvalidDataException(
                            "Invalid symbol (" + iae.getMessage() + ")",
                            null,
                            InvalidDataException.ILLEGAL_SPGP);
                }
            }
        }

        // Provide for symmetry 1 along any directions without higher symmetry
        for (int i = 0; i < axialOps.length; i++) {
            if (axialOps[i].isEmpty()) {
                axialOps[i].add(new Operator(1, false, 0, ' ', i));
            }
        }
    }

    /**
     * Analyzes an array of {@code SymmetryMatrix} instances representing
     * the pure translations present in a space group, and determines from them
     * the corresponding centering symbol
     *
     * @param  translations a {@code List} containing
     *         {@code SymmetryMatrix} instances for all the representative
     *         non-lattice translations of a space group, such as is populated
     *         by {@link #extractOperations(SpaceGroup, List[], List)}.  The
     *         identity translation may be included; it will be ignored if
     *         present.
     *
     * @return the {@code char} symbol for the lattice centering
     *         represented by the provided translations
     *
     * @throws InvalidDataException with reason ILLEGAL_SPGP if any of the
     *         provided translations are not suitable centering translations,
     *         or if the combination of translations does not correspond to a
     *         valid lattice centering type
     */
    static char determineCenteringSymbol(List<SymmetryMatrix> translations)
            throws InvalidDataException {
                
        /*
         * The requirement to recognize R centering considerably complicates
         * this method.  For R centering we expect to see exactly two
         * non-trivial translations, one with two elements 4 and the other 8,
         * and the other one with one element 4 and the others 8, such that
         * the component-wise sum is {12, 12, 12} 
         */

        // determine which centering vectors are present
        int centeringMask = 0;
        int rcount = 0;
        int rmask = 0;

        for (SymmetryMatrix matrix : translations) {
            if (!matrix.equals(SymmetryMatrix.IDENTITY)) {
                int[] vector = matrix.getTranslationVector();
                int axis = 3;
                int thirdsMask = 0;

                for (int j = 0; j < 3; j++) {
                    if (vector[j] == 0) {
                        if ((axis == 3) && (thirdsMask == 0)) {
                            axis = j;
                        } else {
                            throw new InvalidDataException(
                                    "Invalid symbol (generates subcell)",
                                    null,
                                    InvalidDataException.ILLEGAL_SPGP);
                        }
                    } else if (((vector[j] % 4) == 0) && (axis == 3)) {
                        thirdsMask = (thirdsMask << 2) + (vector[j] / 4);
                    } else if (vector[j] != 6) {
                        throw new InvalidDataException(
                                "Invalid symbol (generates incorrect "
                                + "lattice translations)",
                                null,
                                InvalidDataException.ILLEGAL_SPGP);
                    }
                }
                
                if (thirdsMask == 0) {
                    centeringMask += (1 << axis);
                } else {
                    rcount++;
                    rmask ^= thirdsMask;
                    centeringMask += (1 << (3 + countBits(thirdsMask & 0x15)));
                }
            }
        }

        // choose the centering type based on the centering vectors
        switch (centeringMask) {
            case 0:    // primitive
                return 'P';
            case 0x1:  // A centered
                return 'A';
            case 0x2:  // B centered
                return 'B';
            case 0x4:  // C centered
                return 'C';
            case 0x7:  // face centered
                return 'F';
            case 0x8:  // body centered
                return 'I';
            case 0x30: // rhombohedral
                if ((rcount == 2) && (rmask == 0x3f)) {
                    return 'R';
                }
                
                // otherwise fall through
            default:
                throw new InvalidDataException(
                        "Invalid symbol (generates incorrect set of"
                        + "lattice translations)",
                        null,
                        InvalidDataException.ILLEGAL_SPGP);
        }
    }

    /**
     * Returns the number of lattice points per unit cell corresponding to the
     * specified lattice centering symbol.  Asserts that the centering symbol is
     * one of the valid ones.
     *
     * @param  centering a {@code char} containing the centering symbol of
     *         interest
     *
     * @return the number of lattice points in a unit cell having the specified
     *         centering
     */
    static int numLatticePoints(char centering) {
        switch (centering) {
            case 'P':
                return 1;
            case 'A':
            case 'B':
            case 'C':
            case 'I':
                return 2;
            case 'R':  // (hexagonal axes assumed)
                return 3;
            case 'F':
                return 4;
            default:
                assert false;
                return 0;
        }
    }


    /**
     * Analyzes the provided space group details to determine whether the group
     * is a member of one of the two "special" pairs of space groups that
     * contain the same kinds of symmetry elements in the same directions (and
     * differ only in the location of those elements), or whether it is the
     * special case Ibca; if the group is one of those then the appropriate
     * space group symbol returned
     *
     * @param  centering the lattice centering symbol for the space group as a
     *         {@code char}
     * @param  laueClass the {@code LaueClass} to which the space group
     *         belongs
     * @param  axialOps a {@code List[]} where the element at each index
     *         {@code i} contains {@code Operator}s corresponding to
     *         the symmetry operations aligned along the symmetry direction for
     *         which {@code i} is the direction code, such as can be
     *         populated by {@link #extractOperations(SpaceGroup, List[], List)};
     *         the Operators should have matrices already assigned to them,
     *         and will be the primary basis of the symmetry evaluation by which
     *         this method reaches its decision   
     *
     * @return if the group corresponding to the provided symmetry information
     *         is a member of one of the special pairs or is Ibca, then a
     *         SpaceGroupSymbol corresponding to the canonical symbol for that
     *         group; otherwise {@code null}
     */
    static SpaceGroupSymbol identifySpecialGroup(char centering,
            LaueClass laueClass, List<Operator>[] axialOps) {

        // The special groups are all I-centered
        if (centering == 'I') {

            // One of the special pairs is orthorhombic, and so is Ibca
            if (laueClass == LaueClass.ORTHORHOMBIC) {
                boolean isIBCA = true;

                for (int i = 0; i < 3; i++) {
                    Operator op = new Operator(
                            0, false, 0, (char) ('a' + ((i + 1) % 3)), i);
                    int direction = laueClass.getRepresentativeDirection(i);

                    if (!axialOps[direction].contains(op)) {
                        isIBCA = false;
                        break;
                    }
                }

                return isIBCA ? IBCA_SYMBOL
                              : doAxialTwofoldsIntersect(laueClass, axialOps,
                                        I222_SYMBOL, I212121_SYMBOL);

            // The other special pair is low-symmetry cubic
            } else if (laueClass == LaueClass.CUBIC_LOW) {
                return doAxialTwofoldsIntersect(laueClass, axialOps,
                        I23_SYMBOL, I213_SYMBOL);
            }
        }

        // Wrong centering or Laue class for any special group
        return null;
    }

    /**
     * Determines whether three twofold axes aligned along the three unit cell
     * axis directions intersect; they do if the composition of any two is
     * equal to the third.  This method uses the supplied Laue class'
     * OperatorComparator for I-centered groups to find the operators to
     * compare among the provided ones.
     *
     * @param  laueClass the {@code LaueClass} applicable to the collection
     *         of operators presented; should specify symmetry order 2 along
     *         each axial direction
     * @param  axialOps a {@code List[]} where the element at each index
     *         {@code i} contains {@code Operator}s corresponding to
     *         the symmetry operations aligned along the symmetry direction for
     *         which {@code i} is the direction code, such as can be
     *         populated by {@link #extractOperations(SpaceGroup, List[], List)};
     *         it is expected that the {@code Operator}s have previously
     *         had corresponding {@code SymmetryMatrix} objects assigned to
     *         them, and that each of the axial directions has maximum symmetry
     *         order of exactly two
     * @param  ifYes a {@code SpaceGroupSymbol} to return if the axial
     *         twofold axes all exist and do intersect, and there are no
     *         axial reflections; may not be {@code null}
     * @param  ifNo a {@code SpaceGroupSymbol} to return if the axial
     *         twofold axes all exist but do not mutually intersect, and if
     *         also there are no axial reflections; may not be {@code null}
     *
     * @return {@code ifYes} if the three twofold axes along the unit cell
     *         axis directions mutually intersect; {@code ifNo} if they
     *         don't; or {@code null} if there are mirrors along any of the
     *         axial directions or if there is no pure twofold rotation
     *         along one or more of them
     */
    static SpaceGroupSymbol doAxialTwofoldsIntersect(LaueClass laueClass,
            List<Operator>[] axialOps, SpaceGroupSymbol ifYes,
            SpaceGroupSymbol ifNo) {
        SymmetryMatrix[] matrices = new SymmetryMatrix[3];

        assert (ifYes != null);
        assert (ifNo != null);
        assert (laueClass.getExpectedOrder(DIRECTION_A) == 2);
        assert (laueClass.getExpectedOrder(DIRECTION_B) == 2);
        assert (laueClass.getExpectedOrder(DIRECTION_C) == 2);

        Comparator<Operator> comp = laueClass.getOperatorComparator('I');

        for (int dir = 0; dir < 3; dir++) {
            assert (axialOps[dir].size() > 0);

            Operator op = Collections.min(axialOps[dir], comp);

            assert (op.getOrder() == 2);
            assert (!op.isRotoInversion());

            if (op.hasMirrorComponent() || (op.getScrewTranslation() != 0)) {
                return null;
            } else {
                matrices[dir] = op.getMatrix();
            }
        }

        return matrices[0].times(matrices[1], true).equals(matrices[2])
                ? ifYes : ifNo;
    }

    /**
     * A helper method for {@link #chooseCanonicalOperators(List[], LaueClass,
     * char)} that creates a combined list of all {@code Operator}s from
     * the provided lists that are associated with directions for which the
     * specified direction is representative (relative to the provided
     * Laue class)
     *
     * @param  axialOps a {@code List[]} where the element at each index
     *         {@code i} contains {@code Operator}s corresponding to
     *         the symmetry operations aligned along the symmetry direction for
     *         which {@code i} is the direction code, such as can be
     *         populated by {@link #extractOperations(SpaceGroup, List[], List)}
     * @param  direction the symmetry direction representative of those for
     *         which a merged list of operators is desired; normally this
     *         should be one of the DIRECTION_* constants
     * @param  laueClass a {@code LaueClass} representing the Laue class
     *         which should be used to evaluate symmetry direction equivalence
     *
     * @return a {@code List} containing all the members of the lists
     *         indexed by directions equivalent to {@code direction},
     *         including the one indexed by {@code direction} itself
     */
    static List<Operator> mergeLists(List<Operator>[] axialOps, int direction,
            LaueClass laueClass) {
        assert laueClass.hasRepresentativeDirection(direction);
        
        List<Operator> rval = new ArrayList<Operator>();

        for (int dir = 0; dir < axialOps.length; dir++) {
            if (dir == direction) {
                rval.addAll(axialOps[dir]);
            } else if (laueClass.getRepresentativeDirection(dir) == direction) {
                for (Operator op : axialOps[dir]) {
                    op = op.clone();
                    op.setDirection(direction);
                    rval.add(op);
                }
            }
        }

        return rval;
    }

    /**
     * A helper method for {@link #createCanonicalSymbol(String)} that creates
     * an {@code OperatorList} of the appropriate {@code Operator}s
     * for the canonical symbol of a space group of the specified Laue class.
     *
     * @param  axialOps a {@code List[]} where the element at each index
     *         {@code i} contains {@code Operator}s corresponding to
     *         the symmetry operations aligned along the symmetry direction for
     *         which {@code i} is the direction code, such as can be
     *         populated by {@link #extractOperations(SpaceGroup, List[], List)},
     *         containing the {@code Operator}s pertaining to the space
     *         group to symbolize
     * @param  laueClass a {@code LaueClass} representing the Laue class
     *         of the group to symbolize
     * @param  centering the {@code char} centering symbol for the group
     *         for which canonical operators are desired
     *
     * @return an {@code OperatorList} containing the characteristic
     *         {@code Operator}s, from among those in
     *         {@code axialOps}, for a group of the specified Laue class
     *
     * @throws IllegalArgumentException in some cases where one of the
     *         {@code axialOps} lists contains an operator not consistent
     *         with the specified Laue class
     */
    static OperatorList chooseCanonicalOperators(List<Operator>[] axialOps,
            LaueClass laueClass, char centering) {
        Operator[] canonicalOps = new Operator[laueClass.getNumDirections()];
        Comparator<Operator> comp = laueClass.getOperatorComparator(centering);
        List<Operator> ops;
        Operator minOp;
        Operator firstRotation;
        int direction;

        /*
         * The first operator requires special handling: in some groups
         * it will require a compound symbol, whereas other operators
         * never will do.
         */
        direction = laueClass.getSymmetryDirection(0);
        ops = mergeLists(axialOps, direction, laueClass);

        firstRotation = null;
        minOp = null;
        for (Operator op : ops) {
            if (!op.hasMirrorComponent()
                    && ((firstRotation == null) ||
                        (comp.compare(firstRotation, op) > 0))) {
                firstRotation = op;
            }
            if ((minOp == null) || (comp.compare(minOp, op) > 0)) {
                minOp = op;
            }
        }

        if (firstRotation != null) {

            // There is a rotation in this direction:

            /*
             * If there is no mirror in the first direction OR if the first
             * rotation is a rotoinversion (i.e. -6) then use the first rotation
             * operator alone
             */
            if ((firstRotation == minOp) || firstRotation.isRotoInversion()) {
                canonicalOps[0] = firstRotation;

            /*
             * Otherwise, whether or not a compound symbol for the first
             * axis should be used depends on the Laue class
             */
            } else if (laueClass.requiresFullFirstOperator()) {
                // use a compound symbol
                canonicalOps[0] = new Operator(firstRotation, minOp);
            } else {
                // use only the first mirror
                canonicalOps[0] = minOp;
            }

        /*
         * No rotation along the first direction, so just use the
         * highest-precedence mirror
         */
        } else {
            canonicalOps[0] = minOp;
        }

        /*
         * Symbols for the remaining directions (if any) come straight
         * from the highest-precedence operator along that direction
         */
        for (int i = 1; i < laueClass.getNumDirections(); i++) {
            direction = laueClass.getSymmetryDirection(i);
            ops = mergeLists(axialOps, direction, laueClass);
            canonicalOps[i] = Collections.min(ops, comp);
        }

        return new OperatorList(canonicalOps);
    }

    /**
     * Analyzes the specified {@code SpaceGroupSymbol} to determine the
     * applicable Laue class; for classes supporting nonstandard settings, the
     * provided symbol is modified as needed to put it into the standard one
     * for its class
     *
     * @param  symbol the {@code SpaceGroupSymbol} for which to determine
     *         the Laue class; may be modified by this method to put it into
     *         the standard setting for its class
     *
     * @return a {@code LaueClass} representing the Laue class indicated
     *         by the specified symbol
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the symbol
     *         does not correspond to a valid Laue class
     */
    static LaueClass determineLaueClass(SpaceGroupSymbol symbol)
            throws InvalidDataException {

        /*
         * This method is almost entirely made up of a collection of nested
         * switch statements representing a decision tree for choosing a Laue
         * classes based on the number and type of the symbol's symmetry
         * operators.  It is almost always the case that the number of operators
         * and the nature of at most the first two operators is sufficient to
         * narrow the possible Laue classes to at most one; if there is a third
         * operator, however, then it is always checked to ensure that it is
         * consistent with whatever class is otherwise indicated.  The only
         * case where the first two operators are insufficient is in
         * distinguishing low-symmetry trigonal from low-symmetry rhombohedral;
         * in that case it is necessary to consult the centering symbol.
         *
         * This method returns from the nested switches at many points, wherever
         * it successfully identifies a Laue class; this permits it to throw an
         * exception (indicating no matching LaueClass) only if execution drops
         * out of the outermost switch.
         */

        OperatorList operators = symbol.getOperators();
        Operator op0;
        Operator op1;
        Operator op2;

        if (operators.size() < 1) {
            throw new InvalidDataException(
                    "Cannot determine Laue class for '" + symbol + "'",
                    symbol.toString(),
                    InvalidDataException.ILLEGAL_SPGP);
        }

        op0 = operators.getOperator(0);
        
        switch (operators.size()) {
            case 1:  // -1; 2/m; -3; 4/m; 6/m;
                switch(op0.getOrder()) {
                    case 1:  // triclinic
                        return LaueClass.TRICLINIC;
                    case 2:  // monclinic, b unique
                        standardizeMonoclinic(symbol);
                        return LaueClass.MONOCLINIC;
                    case 3:  // trigonal or rhombohedral
                        return (symbol.getCentering() == 'R')
                                ? LaueClass.RHOMBOHEDRAL_LOW
                                : LaueClass.TRIGONAL_LOW;
                    case 4:  // low-symmetry tetragonal
                        return LaueClass.TETRAGONAL_LOW;
                    case 6:  // low-symmetry hexagonal
                        return LaueClass.HEXAGONAL_LOW;
                }
                break;
                
            case 2:  // -3m; m-3
                op1 = operators.getOperator(1);
                switch(op0.getOrder()) {
                    case 2:
                        if (op1.getOrder() == 3) {
                            return LaueClass.CUBIC_LOW;
                        }
                        break;
                    case 3:
                        if (op1.getOrder() == 2) {
                            return LaueClass.RHOMBOHEDRAL_HIGH;
                        }
                        break;
                }
                break;
                
            case 3:  // 1 2/m 1, etc; -3m1; -31m; 4/mmm; 6/mmm; m-3m (and -43m)
                op1 = operators.getOperator(1);
                op2 = operators.getOperator(2);
                switch(op0.getOrder()) {
                    case 1:  // monoclinic full symbol (b or c unique)
                        if ((op1.getOrder() + op2.getOrder()) == 3) {
                            standardizeMonoclinic(symbol);
                            return LaueClass.MONOCLINIC;
                        }
                        break;
                    case 2:  // monoclinic (a unique), orthorhombic, or cubic
                        switch (op1.getOrder()) {
                            case 1:
                                if (op2.getOrder() == 1) {
                                    standardizeMonoclinic(symbol);
                                    return LaueClass.MONOCLINIC;
                                }
                                break;
                            case 2:
                                if (op2.getOrder() == 2) {
                                    standardizeOrthorhombic(symbol);
                                    return LaueClass.ORTHORHOMBIC;
                                }
                                break;
                            case 3:
                                /*
                                 * m 3 m, m -3 m, but NOT 2 3 m or 2 3 2
                                 */
                                if ((op2.getOrder() == 2)
                                        && !op1.hasMirrorComponent()
                                        && (op0.getRotationOrder() == 0)) {
                                    return LaueClass.CUBIC_HIGH;
                                }
                                break;
                        }
                        break;
                    case 3:  // either high-symmetry trigonal Laue class
                        if ((op1.getOrder() == 2) && (op2.getOrder() == 1)) {
                            return LaueClass.TRIGONAL_HIGH_1;
                        } else if ((op1.getOrder() == 1) && (op2.getOrder() == 2)) {
                            return LaueClass.TRIGONAL_HIGH_2;
                        }
                        break;
                    case 4:  // high-symmetry tetragonal or cubic
                        if (op2.getOrder() == 2) {
                            switch (op1.getOrder()) {
                                case 2:
                                    return LaueClass.TETRAGONAL_HIGH;
                                case 3:
                                    return (op0.hasMirrorComponent())
                                            ? LaueClass.CUBIC_HIGH
                                            : LaueClass.CUBIC_HIGH_SPECIAL;
                            }
                        }
                        break;
                    case 6:  // high-symmetry hexagonal
                        if ((op1.getOrder() == 2) && (op2.getOrder() == 2)) {
                            return LaueClass.HEXAGONAL_HIGH;
                        }
                        break;
                }
                break;
        }

        throw new InvalidDataException(
                "Cannot determine Laue class for '" + symbol + "'",
                symbol.toString(),
                InvalidDataException.ILLEGAL_SPGP);
    }

    /**
     * Converts a full or short monoclinic symbol to a short symbol in the
     * standard setting ('b' axis unique, primitive or C-centered).  Short
     * symbols are assumed to already be set with the 'b' axis unique; only
     * centering conversion is applied to such
     *
     * @param  symbol the monoclinic space group symbol to standardize
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if this
     *         method detects that the provided symbol, though correct in form,
     *         is not a valid monoclinic symbol
     */
    static void standardizeMonoclinic(SpaceGroupSymbol symbol)
            throws InvalidDataException {
        OperatorList operators = symbol.getOperators();
        Operator op;

        if (operators.size() == 3) {
            int uniqueAxis = -1;

            // Find the unique axis
            op = null;
            for (int i = 0; i < 3; i++) {
                Operator testOp = operators.getOperator(i);

                if (testOp.isRotoInversion()) {
                    throw new InvalidDataException(
                             "Invalid space group symbol: " + symbol,
                             symbol.toString(),
                             InvalidDataException.ILLEGAL_SPGP);
                } else if (testOp.getOrder() == 2) {
                    assert (uniqueAxis < 0);         // otherwise not monoclinic
                    uniqueAxis = i;
                    op = testOp;
                } else {
                    assert (testOp.getOrder() == 1); // otherwise not monoclinic
                }
            }
            assert (uniqueAxis >= 0);                // otherwise not monoclinic

            // Switch to b unique if not already there
            symbol.swapAxes(uniqueAxis, DIRECTION_B);

            // assign new (reduced) operator list
            operators = new OperatorList(new Operator[] {op});
            symbol.setOperators(operators);
        }

        assert (operators.size() == 1);

        // To standard setting:
        switch (symbol.getCentering()) {
            case 'A':
                symbol.swapAxes(DIRECTION_A, DIRECTION_C);
                assert (symbol.getCentering() == 'C');
                break;
            case 'I':
                op = operators.getOperator(0);
                switch (op.getMirrorComponent()) {
                    case 'a':
                        op.setMirrorComponent('c');
                        break;
                    case 'c':
                        op.setMirrorComponent('n');
                        break;
                    case 'n':
                        op.setMirrorComponent('a');
                        break;
                }
                symbol.setCentering('C');
                break;
            case 'P':
                op = operators.getOperator(0);
                if ((op.getMirrorComponent() == 'a')
                        || (op.getMirrorComponent() == 'n')) {
                    op.setMirrorComponent('c');
                }
                break;
            case 'C':
                // nothing to do
                break;
            default:
                throw new InvalidDataException("Invalid centering: " + symbol,
                        symbol.toString(),
                        InvalidDataException.ILLEGAL_SPGP);
        }
    }

    /**
     * Converts an orthorhombic space group symbol to the standard setting.
     * The orthorhombic system has particularly many and particularly complex
     * standardization rules, in part because orthorhombic groups may be set in
     * up to six different ways.
     *
     * @param  symbol an orthorhombic {@code SpaceGroupSymbol} to
     *         standardize
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         provided symbol is correct in general form, but this method
     *         detects that it does not correspond to a valid point group
     */
    static void standardizeOrthorhombic(SpaceGroupSymbol symbol)
            throws InvalidDataException {

        assert (symbol.getOperators().size() == 3);

        /*
         * Important note: the members of array ops are shared with the space
         * group symbol object.  Of particular import in this regard is that
         * the symbol's swapAxes() method may mutate them (in place).
         */
        Operator[] ops = symbol.getOperators().toArray(new Operator[3]);

        int nmirrors = 0;
        int n21 = 0;

        /*
         * Find the number of mirrors in the symbol, which directly determines
         * the point group; in the process, count the number of 21 screw axes
         * along directions without mirrors
         */
        for (int i = 0; i < 3; i++) {
            assert (ops[i].getOrder() == 2);
            ops[i].setDirection(i);
            if (ops[i].hasMirrorComponent()) {
                nmirrors++;
            } else if (ops[i].getScrewTranslation() == 1) {
                n21++;
            }
        }

        /*
         * Select the standard setting according to the point group, centering,
         * and symmetry operation details
         */
        switch (nmirrors) {
            case 0: // Point group 222
                switch (symbol.getCentering()) {
                    case 'A':
                        symbol.swapAxes(DIRECTION_A, DIRECTION_C);
                        break;
                    case 'B':
                        symbol.swapAxes(DIRECTION_B, DIRECTION_C);
                        break;
                    case 'C':
                        // nothing to do
                        break;
                    case 'P':
                        // fall through
                    case 'I':
                        // fall through
                    case 'F':
                        int uniqueScrew = (2 - n21);

                        /*
                         * If there are two screw axes in the symbol then the
                         * unique axis has screw translation zero.  If there is
                         * one then the unique axis has screw translation one.
                         * Otherwise all axes have equivalent symmetry.
                         */

                        for (int i = 0; i < 2; i++) {
                            if (ops[i].getScrewTranslation() == uniqueScrew) {
                                symbol.swapAxes(i, DIRECTION_C);
                            }
                        }

                        break;
                }
                break;

            case 1: // Point group 22m
                // this point group does not exist
                throw new InvalidDataException(
                         "Impossible point group: " + symbol,
                         symbol.toString(),
                         InvalidDataException.ILLEGAL_SPGP);

            case 2: // Point group mm2
                // the twofold axis is defined to be the unique direction
                for (int i = 0; i < 2; i++) {
                    if (! ops[i].hasMirrorComponent()) {
                        symbol.swapAxes(i, DIRECTION_C);
                        break;
                    }
                }

                // 'A' centering is favored over 'B' centering
                if (symbol.getCentering() == 'B') {
                    symbol.swapAxes(DIRECTION_A, DIRECTION_B);

                /*
                 * In non-side-centered mm2 groups, the types of the reflections
                 * matter for standardization
                 */
                } else if (symbol.getCentering() >= 'C') {
                    char[] mirrorTypes = new char[] {' ', ' ', ' '};
                    boolean[] hasType1 = new boolean[3];

                    for (int i = 0; i < 3; i++) {
                        char mirror = ops[i].getMirrorComponent();

                        mirrorTypes[ops[i].getDirection()] = mirror;
                        hasType1[ops[i].getDirection()] =
                            ((mirror == 'm') || (mirror == 'n'));
                    }

                    /*
                     * There are supposed to be mirrors in the a and b
                     * directions, but not in the c direction
                     */
                    assert (!hasType1[DIRECTION_C]);
                    assert (mirrorTypes[DIRECTION_C] == ' ');
                    assert (mirrorTypes[DIRECTION_B] != ' ');
                    assert (mirrorTypes[DIRECTION_A] != ' ');

                    if (hasType1[DIRECTION_A]) {
                        if (mirrorTypes[DIRECTION_B] == 'm') {
                            symbol.swapAxes(DIRECTION_A, DIRECTION_B);
                        }
                    } else if (mirrorTypes[DIRECTION_B]
                               > mirrorTypes[DIRECTION_A]) {
                        symbol.swapAxes(DIRECTION_A, DIRECTION_B);
                    }
                }

                break;

            case 3: // point group mmm

                /*
                 * uniqueAxis will be the index into the ops array of the
                 * Operator that is aligned along the unique axis
                 */
                int uniqueAxis = -1;
                boolean uniqueIsType1 = false;

                // First, convert A and B centering to C centering
                int centerAxis = symbol.getCentering() - 'A';

                if (centerAxis < DIRECTION_C) {
                    symbol.swapAxes(centerAxis, DIRECTION_C);
                }

                // further standardization depends on the centering

                if (symbol.getCentering() == 'C') {

                    // C centering is fairly easy

                    uniqueAxis = centerAxis;

                    /*
                     * If there is a plain mirror along just one of A and B,
                     * then it should be along A
                     */
                    for (Operator op : ops) {
                        if (op.getDirection() == DIRECTION_B) {
                            if (op.getMirrorComponent() == 'm') {
                                symbol.swapAxes(DIRECTION_A, DIRECTION_B);
                            }
                            break;
                        }
                    }
                } else {

                    // P, I, and F centerings are harder
                    final int C_AXIS_MASK = 4;
                    final int ALL_AXES_MASK = 7;
                    int[] mirrors = new int[VALID_REFLECTIONS.length()];

                    // Identify directions of all the mirror types
                    for (Operator op : ops) {
                        int reflectionType = VALID_REFLECTIONS.indexOf(
                                op.getMirrorComponent());

                        mirrors[reflectionType] |= (1 << op.getDirection());
                    }

                    /*
                     * look for two (or three) mirrors with the same glide
                     * vector
                     */
                    for (int mask : mirrors) {
                        if (mask == ALL_AXES_MASK) {
                            // all axes have the same glide
                            uniqueAxis = C_AXIS_MASK;
                            break;
                        } else if (countBits(mask) == 2) {
                            // Two axes have the same glide
                            uniqueAxis = (ALL_AXES_MASK ^ mask);
                            break;
                        }
                    }

                    if (uniqueAxis < 0) {
                        /*
                         * look for two mirrors, either both axial glides or
                         * neither one an axial glide
                         */

                        // type1 is a combined bitmask for 'm' and 'n' mirrors
                        int type1 = (mirrors[VALID_REFLECTIONS.indexOf('m')]
                                     | mirrors[VALID_REFLECTIONS.indexOf('n')]);

                        switch (countBits(type1)) {
                            case 0: // "bca" or "cab"
                                uniqueAxis = C_AXIS_MASK;  // no change needed
                            case 1: // the single n or m is on the unique axis
                                uniqueAxis = type1;
                                uniqueIsType1 = true;
                                break;
                            case 2: // the single axial glide is on the unique axis
                                uniqueAxis = (ALL_AXES_MASK ^ type1);
                                break;
                            default:
                                /*
                                 * Should never happen: should not be in this
                                 * if block at all in this case
                                 */
                                assert false;
                        }
                    }

                    // From bitmask to direction code (4 -> 2, 2 -> 1, 1 -> 0)
                    uniqueAxis /= 2;

                    assert ((0 <= uniqueAxis) && (uniqueAxis < 3));

                    // swap the unique axis into the c position
                    symbol.swapAxes(uniqueAxis, DIRECTION_C);

                    if (uniqueIsType1) {
                        for (int i = 1; i < 3; i++) {
                            Operator op = ops[(uniqueAxis + i) % 3];

                            if (op.getDirection() == DIRECTION_A) {
                                if (op.getMirrorComponent() == 'c') {

                                    /*
                                     * Prefer to not have a c glide along the
                                     * A direction
                                     */
                                    symbol.swapAxes(DIRECTION_A, DIRECTION_B);
                                }
                                break;
                            }
                        }
                    } else if (ops[uniqueAxis].getMirrorComponent() == 'b') {

                        /*
                         * Prefer an 'a' glide to a 'b' glide for the unique
                         * direction
                         */
                        symbol.swapAxes(DIRECTION_A, DIRECTION_B);
                    }
                }

                break;
        }

        /*
         * We may have changed the operators' assigned directions, perhaps even
         * more than once; put them into the correct order for Laue class mmm
         * based on their revised directions, and update the symbol
         */
        sortByDirection(ops);
        symbol.setOperators(new OperatorList(ops));
    }

    /**
     * A helper method for {@link #standardizeOrthorhombic(SpaceGroupSymbol)}
     * that sorts an array of {@code Operator}s according to the symmetry
     * directions configured on them.  This special-purpose sort assumes,
     * therefore, that the {@code Operator}s have already had their
     * directions set.  It also assumes that every direction value from zero to
     * {@code ops.length - 1} is used.
     *
     * @param  ops the {@code Operator[]} to sort
     */
    static void sortByDirection(Operator[] ops) {
        Operator[] temp = new Operator[ops.length];

        for (Operator op : ops) {
            int direction = op.getDirection();

            assert((0 <= direction) && (direction < ops.length));
            assert (temp[direction] == null);

            temp[direction] = op;
        }

        System.arraycopy(temp, 0, ops, 0, ops.length);
    }

    /**
     * Efficiently counts the number of 1 bits in the binary representation of
     * the specified number (note that arguments of type {@code byte} and
     * {@code short} will be sign-extended when passed to this method) 
     *
     * @param  i the number whose 1 bits should be counted
     *
     * @return the number of 1 bits in the binary representation of the argument
     */
    static int countBits(int i) {
        i = (i & (0x55555555)) + ((i >> 1) & 0x55555555);
        i = (i & (0x33333333)) + ((i >> 2) & 0x33333333);
        i = (i & (0x0f0f0f0f)) + ((i >> 4) & 0x0f0f0f0f);
        i = (i & (0x00ff00ff)) + ((i >> 8) & 0x00ff00ff);
        i = (i & (0x0000ffff)) + ((i >> 16) & 0x0000ffff);
        return i;
    }

    /**
     * A helper method for {@link #digestSymbol(String)} that processes a
     * String to determine the centering symbol (if any) that it represents
     *
     * @param  s the string from which to extract a centering symbol
     *
     * @return a {@code char} centering symbol corresponding to the
     *         provided string: the first character of that string if it is a
     *         valid centering symbol and the string has length 1
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         specified string is longer than one character or if its first
     *         (and only) character is not a valid centering symbol
     * @throws IndexOutOfBoundsException if the specified string is empty
     *
     * @see #getMirrorPart(String)
     * @see #getScrewPart(String, int)
     */
    static char getCentering(String s) throws InvalidDataException {
        char rval = s.charAt(0);

        if ((VALID_CENTERING.indexOf(rval) < 0) || (s.length() > 1)) {
            throw new InvalidDataException("Invalid centering symbol: " + s,
                    s,
                    InvalidDataException.ILLEGAL_SPGP);
        } else {
            return rval;
        }
    }

    /**
     * A helper method for {@link #digestSymbol(String)} that processes a
     * String to determine the reflection symbol (if any) that it represents
     *
     * @param  s the string from which to extract a reflection symbol
     *
     * @return a {@code char} reflection symbol corresponding to the
     *         provided string: the first character of that string if it is a
     *         valid reflection symbol and the string has length 1
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the
     *         specified string is longer than one character or if its first
     *         character is not a valid reflection symbol
     * @throws IndexOutOfBoundsException if the specified string is empty
     *
     * @see #getCentering(String)
     * @see #getScrewPart(String, int)
     */
    static char getMirrorPart(String s) throws InvalidDataException {
        char mirror = s.charAt(0);

        if ((s.length() > 1) || (VALID_REFLECTIONS.indexOf(mirror) == -1)) {
            throw new InvalidDataException(
                    "Invalid mirror symbol: " + s,
                    null,
                    InvalidDataException.ILLEGAL_SPGP);
        }

        return mirror;
    }

    /**
     * A helper method for {@link #digestSymbol(String)} that extracts the
     * screw part of a screw rotation symbol
     *
     * @param s the operator symbol
     * @param order the rotation order of the operator being parsed
     *
     * @return the screw translation part of the symbol
     *
     * @throws InvalidDataException with reason code ILLEGAL_SPGP if the screw
     *         part is less than one or greater than or equal to the rotation
     *         order
     * @throws NumberFormatException if the tail (from index 1) of the operator
     *         string cannot be parsed as an int
     * @throws IndexOutOfBoundsException if the specified string is empty
     *
     * @see #getCentering(String)
     * @see #getMirrorPart(String)
     */
    static int getScrewPart(String s, int order) throws InvalidDataException {
        int screw = Integer.parseInt(s.substring(1));

        if ((screw < 1) || (screw >= order)) {
            throw new InvalidDataException(
                    "Invalid screw translation: " + s,
                    null,
                    InvalidDataException.ILLEGAL_SPGP);
        }

        return screw;
    }

    /**
     * An object representing the Hermann-Mauguin symbology of a (possibly
     * compound) axial symmetry operator.  An Operator has a rotation component
     * and/or a reflection component.  A rotation component may be improper (a
     * rotoinversion) or proper (a pure rotation or screw rotation).  A
     * reflection component may be a pure reflection or a glide reflection.
     */
    public static class Operator implements Cloneable {

        /**
         * The order of the pure rotation operation from which the rotation
         * component of this operator is derived; the order of a rotation is
         * smallest positive integer n such that the nth power of the operation
         * is the identity operation.
         */
        private int rotationOrder;

        /**
         * {@code true} if the rotation component of this operator is a
         * rotoinversion
         */
        private boolean rotoInversion;

        /**
         * The screw translation index of the rotation component of this
         * operator
         */
        private int screwTranslation;

        /**
         * The code for the reflection component of this operator
         */
        private char mirrorComponent;

        /**
         * The code for symmetry direction assigned to this operator, if any, as
         * dictated by the operator's position in its space group symbol and
         * the corresponding Laue class
         */
        private int assignedDirection = DIRECTION_NONE;

        /**
         * The {@code SymmetryMatrix} assigned to this operator, if any
         */
        private SymmetryMatrix assignedMatrix = null;

        /**
         * Initializes a new {@code Operator} instance based on the
         * specified properties
         *
         * @param  order the order of the rotation component of this operator or
         *         0 for no explicit rotation component
         * @param  inversion {@code true} if the rotation component of this
         *         operator is improper (i.e. is a rotoinversion), otherwise,
         *         including if there is no explicit rotation component,
         *         {@code false}
         * @param  screw the screw translation index of this rotation; 0 for a
         *         pure rotation or if no rotation part is explicitly specfied
         * @param  mirror the char code for the mirror component of this
         *         operator; blank (' ') if none
         */
        Operator(int order, boolean inversion, int screw, char mirror) {
            this(order, inversion, screw, mirror, DIRECTION_NONE);
        }

        /**
         * Initializes a new {@code Operator} instance based on the
         * specified properties
         *
         * @param  order the order of the rotation component of this operator or
         *         0 for no explicit rotation component
         * @param  inversion {@code true} if the rotation component of this
         *         operator is improper (i.e. is a rotoinversion), otherwise,
         *         including if there is no explicit rotation component,
         *         {@code false}
         * @param  screw the screw translation index of this rotation; 0 for a
         *         pure rotation or if no rotation part is explicitly specfied
         * @param  mirror the char code for the mirror component of this
         *         operator; blank (' ') if none
         * @param  direction the symmetry direction along which the operator is
         *         aligned; normally this should be one of the DIRECTION_*
         *         constants
         */
        Operator(int order, boolean inversion, int screw, char mirror,
                int direction) {
            rotationOrder = order;
            rotoInversion = inversion;
            screwTranslation = screw;
            mirrorComponent = mirror;
            assignedDirection = direction;
        }

        /**
         * Initializes a new {@code Operator} instance with the rotation
         * component from one operator and the mirror component from another.
         * The two {@code Operator}s must be configured with the same
         * symmetry direction.
         *
         * @param  rotation an {@code Operator} from which to draw the
         *         rotation component for this {@code Operator}
         * @param  mirror an {@code Operator} from which to draw the
         *         mirror component for this {@code Operator}
         *
         * @throws IllegalArgumentException if the two specified operators are
         *         configured with different symmetry directions
         */
        Operator(Operator rotation, Operator mirror) {
            this(rotation.getRotationOrder(), false,
                rotation.getScrewTranslation(), mirror.getMirrorComponent());
            assignedDirection = rotation.getDirection();
            if (mirror.getDirection() != assignedDirection) {
                throw new IllegalArgumentException(
                "rotation and mirror are not coaxial");
            }
        }

        /**
         * Returns the code for the mirror component of this operator
         *
         * @return the {@code char} code for the mirror component
         *         of this operator (normally 'm', 'a', 'b', 'c', 'n', or 'd'),
         *         or a blank character (' ') if there is no mirror component
         */
        public char getMirrorComponent() {
            return mirrorComponent;
        }

        /**
         * Sets the code for the mirror component of this operator.  This is
         * only intended to be used internally, in particular when changing
         * the setting of a space group symbol
         *
         * @param  mirror the {@code char} code for the mirror component
         *         of this operator ('m', 'a', 'b', 'c', 'n', and 'd' are
         *         standard), or a blank character (' ') if there is no mirror
         *         component
         */
        void setMirrorComponent(char mirror) {
            mirrorComponent = mirror;
        }

        /**
         * A convenience method to determine whether this operator has a mirror
         * component
         *
         * @return {@code true} if this operator has a mirror component
         */
        public boolean hasMirrorComponent() {
            return (mirrorComponent != ' ');
        }

        /**
         * Returns the order of the pure rotation operation from which the
         * rotation component of this operator is derived (i.e. the smallest
         * positive integer n such that the nth power of that operation is the
         * identity operation).
         *
         * @return the order of the pure rotation operation from which the
         *         rotation component of this operator is derived; zero if no
         *         rotation component is specified for this
         *         {@code Operator}
         */
        public int getRotationOrder() {
            return rotationOrder;
        }

        /**
         * A convenience method to determine whether this operator has a
         * rotation component
         *
         * @return {@code true} if this operator has a rotation component
         */
        public boolean hasRotationComponent() {
            return (rotationOrder != 0);
        }

        /**
         * Determines whether the rotation component of this operator is
         * improper (a rotoinversion)
         *
         * @return {@code true} if the rotation component of this operator
         *         is a rotoinversion
         */
        public boolean isRotoInversion() {
            return rotoInversion;
        }

        /**
         * Returns the screw translation index of the rotation component of this
         * operator
         *
         * @return the screw translation index
         */
        public int getScrewTranslation() {
            return screwTranslation;
        }

        /**
         * Returns the direction code assigned to this operator; the direction
         * is normally set by a LaueClass during space group construction
         *
         * @return the direction code; one of the the DIRECTION_* constants
         */
        int getDirection() {
            return assignedDirection;
        }

        /**
         * Assigns a symmetry direction code to this operator.  This is intended
         * for internal use only.
         *
         * @param direction the symmetry direction code; one of the DIRECTION_*
         *        constants
         */
        void setDirection(int direction) {
            assignedDirection = direction;
        }

        /**
         * Returns the {@code SymmetryMatrix} assigned to this operator, if
         * any.  Typically, matrices are assigned during construction of symbols
         * from {@code SpaceGroup} objects
         *
         * @return the {@code SymmetryMatrix} assigned to this operator, or
         *         {@code null} if none has been assigned
         */
        SymmetryMatrix getMatrix() {
            return assignedMatrix;
        }

        /**
         * Assigns an {@code SymmetryMatrix} to this operator.  This is
         * intended for internal use only.
         *
         * @param  matrix the {@code SymmetryMatrix} to assign; may be
         *         {@code null} to remove any previous matrix assignment
         */
        void setMatrix(SymmetryMatrix matrix) {
            assignedMatrix = matrix;
        }

        /**
         * Returns the maximum of the orders of the rotation and reflection
         * components of this operator
         *
         * @return the maximum order of the symmetry operators represented by
         *         this {@code Operator}
         */
        int getOrder() {
            return Math.max(((mirrorComponent == ' ') ? 1 : 2), rotationOrder);
        }

        /**
         * Creates and returns a {@code List} of one or two
         * {@code Operator}s, each having either a rotation component or
         * a reflection component but not both, the combination of which
         * describes all components of this {@code Operator}.
         *
         * @return a {@code List} of distinct single-component
         *         {@code Operator}s representing, among them, all
         *         components of this {@code Operator}
         */
        List<Operator> split() {
            List<Operator> rval = new ArrayList<Operator>(2);

            if (hasMirrorComponent()) {
                rval.add(new Operator(0, false, 0, mirrorComponent,
                                      assignedDirection));
            }
            if (rotationOrder != 0) {
                rval.add(new Operator(rotationOrder, rotoInversion,
                                      screwTranslation, ' ', assignedDirection));
            }

            // There should be at least one
            assert (rval.size() > 0);

            return rval;
        }

        /**
         * Creates and returns a string representation of this
         * {@code Operator}.  The representation chosen corresponds to the
         * format used in a standardized or canonical space group symbol.
         *
         * @return a {@code String} representation of this {@code Operator}
         */
        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();

            if (rotoInversion) {
                sb.append('-');
            }
            if (rotationOrder > 0) {
                sb.append(rotationOrder);
                if (screwTranslation > 0) {
                    sb.append(screwTranslation);
                }
                if (mirrorComponent != ' ') {
                    sb.append('/').append(mirrorComponent);
                }
            } else if (mirrorComponent != ' ') {
                sb.append(mirrorComponent);
            }

            return sb.toString();
        }

        /**
         * Determines whether this {@code Operator} is equal to the
         * specified object, which is the case if that object is an
         * {@code Operator} with the same rotation order, rotoinversion
         * flag, screw translation index, mirror component code, and assigned
         * direction.
         *
         * @param  o an {@code Object} to compare with this one
         *
         * @return {@code true} if {@code o} is equal to this
         *         {@code Operator}; otherwise {@code false}
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof Operator) {
                Operator other = (Operator) o;

                return ((this.rotationOrder == other.rotationOrder)
                        && (this.rotoInversion == other.rotoInversion)
                        && (this.screwTranslation == other.screwTranslation)
                        && (this.mirrorComponent == other.mirrorComponent)
                        && (this.assignedDirection == other.assignedDirection));
            } else {
                return false;
            }
        }

        /**
         * Returns a hashCode for this {@code Operator} that is consistent
         * with {@code Operator.equals()}.  Considering only operators that
         * may appear in valid space group symbols, two operators have the same
         * hash code if and only if they are equal (per
         * {@link #equals(Object)}).
         *
         * @return the hash code
         */
        @Override
        public int hashCode() {
            return ((rotoInversion ? -rotationOrder : rotationOrder) << 24)
                   + (screwTranslation << 16)
                   + (mirrorComponent << 8)
                   + (assignedDirection);
        }

        /**
         * Creates a clone of this {@code Operator}: a distinct instance
         * that {@code equals()} this one.  Note that this class'
         * {@code equals(Object)} implementation does not depend on the
         * assigned matrix, and that this method explicitly nullifies the
         * clone's assigned matrix (via {@link #setMatrix(SymmetryMatrix)}) so
         * that the clone does not share the matrix of the original.
         *
         * @return a clone of this {@code Operator}
         */
        @Override
        protected Operator clone() {
            try {
                Operator op = (Operator) super.clone();

                op.assignedMatrix = null;

                return op;
            } catch (CloneNotSupportedException cnse) {
                // Should never happen because this class is Cloneable
                throw new UnexpectedExceptionException(cnse);
            }
        }
    }

    /**
     * A Comparator implementation intended for use as the base class for
     * Laue-class-specific Operator Comparators.  This class and its subclasses
     * define the precedence order by which the representative operator for
     * each symmetry direction is chosen to form the canonical space group
     * symbol.  Subclasses can modify the relative precedence of various glide
     * plane operators by overriding {@link #getSequence(int)}.
     */
    static class OperatorComparator implements Comparator<Operator> {

        /**
         * <p>
         * Compares two objects and returns an int indicating whether the first
         * is less than, equal to, or greater than the second.  This
         * implementation works only on {@code Operator} instances.  See
         * the standardization rules for short symbols in section 12.3 of the
         * International Tables for X-ray Crystallography (4th ed., 1996) for
         * general information.  Subclasses will provide specific rules
         * encoding the quirks of sorting reflection symbols for various
         * combinations of Laue class and centering by implementing the
         * {@link #getSequence(int)} method.
         * </p><p>
         * This {@code Comparator} is intended for one-part
         * {@code Operator}s only (either a rotation or a mirror, but not
         * both) oriented along the same direction, and it asserts that its
         * arguments meet those criteria.
         * </p>
         *
         * @param  op1 the first {@code Operator} to compare
         * @param  op2 the second {@code Operator} to compare
         *
         * @return an int less than, equal to, or greater than zero
         *         corresponding to whether the first argument is less than
         *         equal to, or greater than the second in the ordering implied
         *         by this {@code Comparator}
         *
         * @throws ClassCastException if either argument is not an
         *         {@code Operator}
         * @throws IllegalArgumentException if either {@code Operator}
         *         argument has a mirror component that is not in the sequence
         *         defined for the corresponding direction by this comparator
         */
        final public int compare(Operator op1, Operator op2) {
            char m1 = op1.getMirrorComponent();
            char m2 = op2.getMirrorComponent();
            int r1 = op1.getRotationOrder();
            int r2 = op2.getRotationOrder();

            // This comparator is for one-part Operators only
            assert ((m1 == ' ') || (r1 == 0));
            assert ((m2 == ' ') || (r2 == 0));
            assert (op1.getDirection() == op2.getDirection());

            String sequence = getSequence(op1.getDirection());

            if (m1 != ' ') {
                // op1 has a mirror component
                int i1 = sequence.indexOf(m1);

                if (i1 < 0) {
                    throw new IllegalArgumentException(
                            "Invalid '" + m1 + "' reflection");
                } else if (m2 == ' ') {
                    // op2 does not have a mirror component
                    return -1;
                } else {
                    // op2 also has a mirror component
                    int i2 = sequence.indexOf(m2);

                    if (i2 < 0) {
                        throw new IllegalArgumentException(
                            "Invalid '" + m2 + "' reflection");
                    }

                    return (i1 - i2);
                }
            } else if (m2 != ' ') {
                // op2 has a mirror component but op1 does not

                if (sequence.indexOf(m2) < 0) {
                    throw new IllegalArgumentException(
                            "Invalid '" + m2 + "' reflection");
                } else {
                    return 1;
                }
            } else if (r1 != r2) {
                // op1 and op2 have different rotation orders
                return (r2 - r1);
            } else if (op1.isRotoInversion() != op2.isRotoInversion()) {
                /*
                 * exactly one of the two is a rotoinversion; the rotoinversion
                 * sorts first if and only if the rotation order is odd
                 */
                return ((op1.isRotoInversion() == ((r1 % 2) != 0))
                        ? -1
                        : 1);
            } else {
                // All else is the same -- discriminate by screw translation
                return op1.getScrewTranslation() - op2.getScrewTranslation();
            }
        }

        /**
         * Subclasses may override this method to define different precedence
         * sequences for reflections
         *
         * @param  direction the direction code for the direction along which
         *         the symmetry operations to be sorted are aligned; not used by
         *         this version
         *
         * @return a String containing the refelction symbols accepted in the
         *         specified direction, in the sequence defining their relative
         *         precedence; this version always returns
         *         {@code SpaceGroupSymbolBL.VALID_REFLECTIONS}
         */
        protected String getSequence(
                @SuppressWarnings("unused") int direction) {
            return VALID_REFLECTIONS;
        }
    }

    /**
     * A type-safe, immutable {@code List} of non-{@code null}
     * {@code Operator}s.
     */
    public static class OperatorList extends AbstractList<Operator>
            implements RandomAccess {

        /**
         * An internal array of {@code Operator}s backing this list
         */
        private Operator[] operators;

        /**
         * Initializes a new {@code OperatorList} with the
         * {@code Operator}s from the provided array, in the order they
         * appear in the array
         *
         * @param  operators an {@code Operator[]} containing the
         *         {@code Operator}s for this list
         *
         * @throws NullPointerException if {@code operators} or any of
         *         its elements is {@code null}
         */
        OperatorList(Operator[] operators) {
            if (operators == null) {
                throw new NullPointerException("null operator array");
            } else {
                for (Operator op : operators) {
                    if (op == null) {
                        throw new NullPointerException("null operator");
                    }
                }
                this.operators = operators.clone();
            }
        }

        /**
         * Initializes a new {@code OperatorList} with the
         * {@code Operator}s from the provided collection, in the order
         * they are returned by the collection's iterator
         *
         * @param  operators a {@code Collection} containing the
         *         {@code Operator}s for this list
         */
        OperatorList(Collection<Operator> operators) {
            this(operators.toArray(new Operator[operators.size()]));
        }

        /**
         * An implementation method of the {@code List} interface; returns
         * the number of {@code Operator}s in this list
         *
         * @return the number of {@code Operator}s in this list
         */
        @Override
        public int size() {
            return operators.length;
        }

        /**
         * An implementation method of the {@code List} interface; returns
         * the {@code Operator} at the specified index in this list, as an
         * {@code Object}
         *
         * @param  i the index into this list of the desired {@code Operator}
         * 
         * @return the {@code Operator} at the specified index in this list
         *
         * @throws IndexOutOfBoundsException if the specified index is less than
         *         zero or greater than or equal to this size of this list
         */
        @Override
        public Operator get(int i) {
            return getOperator(i);
        }

        /**
         * A type-specific version of {@link #get(int)}; returns
         * the {@code Operator} at the specified index in this list, as an
         * {@code Operator}
         *
         * @param  i the index into this list of the desired {@code Operator}
         * 
         * @return the {@code Operator} at the specified index in this list
         *
         * @throws IndexOutOfBoundsException if the specified index is less than
         *         zero or greater than or equal to this size of this list
         */
        public Operator getOperator(int i) {
            return operators[i];
        }
    }

    /**
     * A structured representation of a space group symbol, suitable for
     * manipulation, detailed analysis, or markup
     */
    public static class SpaceGroupSymbol {

        /**
         * The centering symbol for this space group
         */
        private char centering;

        /**
         * a list of the symmetry operators of this space group symbol
         */
        private OperatorList operators;

        /**
         * Initializes a {@code SpaceGroupSymbol} with the specified
         * centering symbol and operator list
         *
         * @param  centering the {@code char} centering code for this
         *         symbol; standard codes are 'P', 'A', 'B', 'C', 'I', 'R', and
         *         'F'
         * @param  operators an {@code OperatorList} containing the
         *         operators for this symbol; standard symbols have between one
         *         and three operators, and not all combinations represent
         *         real space groups
         */
        SpaceGroupSymbol(char centering, OperatorList operators) {
            this.centering = centering;
            this.operators = operators;
        }

        /**
         * Returns the centering code for this symbol
         *
         * @return the {@code char} centering code for this symbol
         */
        public char getCentering() {
            return centering;
        }

        /**
         * Sets the centering code for this symbol.  Intended for internal use
         * only; in particular for changing the space group setting
         *
         * @param  centering the new centering code for this symbol
         */
        void setCentering(char centering) {
            this.centering = centering;
        }

        /**
         * Returns the operator list for this symbol
         *
         * @return the {@code OperatorList} containing the operators for
         *         this space group symbol
         */
        public OperatorList getOperators() {
            return operators;
        }

        /**
         * Sets the operator list for this symbol.  Intended for internal use
         * only; in particular for standardizing the symbol
         *
         * @param  list the new {@code OperatorList} for this symbol
         */
        void setOperators(OperatorList list) {
            operators = list;
        }

        /**
         * Modifies the {@code Operators} of this space group symbol to
         * reflect a relabelling of the coordinate axes.  A swap of two axes is
         * handled directly by this method; cycling the axes will require two
         * invocations.  This method checks early and returns without action if
         * {@code axis1} is the same as {@code axis2}.  This method
         * should not be invoked on a symbol whose operators have already had
         * matrices assigned to them.  <strong>Note:</strong> this method
         * modifies the operators, but it does not change their sequence in the
         * symbol.
         *
         * @param  axis1 the code (DIRECTION_A, DIRECTION_B, or DIRECTION_C) for
         *         the first axis to swap
         * @param  axis2 the code (DIRECTION_A, DIRECTION_B, or DIRECTION_C) for
         *         the second axis to swap
         *
         * @throws IllegalArgumentException if axis1 or axis2 is not a valid
         *         unit cell axis code
         */
        void swapAxes(int axis1, int axis2) {
            // verify axis arguments
            if ((axis1 < 0) || (axis1 > 2) || (axis2 < 0) || (axis2 > 2)) {
                throw new IllegalArgumentException("Invalid axis code in ("
                        + axis1 + "," + axis2 +")");
            } else if (axis1 == axis2) {  // nothing to do
                return;
            }

            OperatorList myOperators = getOperators();
            char glide1 = VALID_REFLECTIONS.charAt(axis1 + 1);
            char glide2 = VALID_REFLECTIONS.charAt(axis2 + 1);
            char myCentering = getCentering();

            // Adjust centering symbol as necessary
            if (myCentering == VALID_CENTERING.charAt(axis1 + 1)) {
                setCentering(VALID_CENTERING.charAt(axis2 + 1));
            } else if (myCentering == VALID_CENTERING.charAt(axis2 + 1)) {
                setCentering(VALID_CENTERING.charAt(axis1 + 1));
            }

            // adjust glide vectors and symmetry directions as necessary
            for (Operator op : myOperators) {
                if (op.getMirrorComponent() == glide1) {
                    op.setMirrorComponent(glide2);
                } else if (op.getMirrorComponent() == glide2) {
                    op.setMirrorComponent(glide1);
                }

                if (op.getDirection() == axis1) {
                    op.setDirection(axis2);
                } else if (op.getDirection() == axis2) {
                    op.setDirection(axis1);
                }
            }
        }

        /**
         * Creates and returns a string representation of this space group
         * symbol.  The representation may be treated as a "formatted" (but
         * not necessarilly "canonical") space group symbol.
         *
         * @return a {@code String} representation of this symbol
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append(centering);
            for (Operator op : getOperators()) {
                sb.append(' ').append(op);
            }

            return sb.toString();
        }

    }

    /**
     * Represents a crystallographic space group, as characterized by a space
     * group symbol, a corresponding Laue class, and a set of matrices defining
     * the characteristic symmetry operations of the group.  The primary purpose
     * of the class is to collect the internal information pertaining to a
     * particular space group into one place.  {@code SpaceGroup} instances
     * are intended to be created by {@code LaueClass} objects.
     */
    static class SpaceGroup {

        /**
         * A {@code SpaceGroupSymbol} corresponding to this space group
         */
        private SpaceGroupSymbol symbol;

        /**
         * The {@code LaueClass} instance corresponding to this space
         * group's Laue class; typically the one by which it was constructed
         */
        private LaueClass laueClass;

        /**
         * A {@code Set} of {@code OperatorMatrices} representing the
         * characteristic symmetry operations of the space group
         */
        private Set<SymmetryMatrix> matrixSet;

        /**
         * Initializes a new {@code SpaceGroup} instance with the specified
         * symbol and Laue class, and adds a matrix for the identity operation
         * to the matrix set.  Any remaining generator matrices for the group
         * must be added later.
         *
         * @param  symbol the {@code SpaceGroupSymbol} for this space
         *         group; should not be {@code null}
         * @param  laueClass the {@code LaueClass} associated with this
         *         space group, normally the one that instantiates it; should
         *         not be {@code null}
         */
        SpaceGroup(SpaceGroupSymbol symbol, LaueClass laueClass) {
            assert (symbol != null);
            assert (laueClass != null);
            this.symbol = symbol;
            this.laueClass = laueClass;
            this.matrixSet = new HashSet<SymmetryMatrix>();

            // Add the identity operation
            matrixSet.add(SymmetryMatrix.IDENTITY);
        }

        /**
         * Returns the space group symbol assigned to this space group; normally
         * that is the one which provided the basis for this
         * {@code SpaceGroup}'s configuration
         *
         * @return the SpaceGroupSymbol assigned to this space group
         */
        public SpaceGroupSymbol getSymbol() {
            return symbol;
        }

        /**
         * Returns the Laue class assigned to this space group.  Typically that
         * will be the one that constructed it.
         *
         * @return the {@code LaueClass} assigned to this
         *         {@code SpaceGroup}
         */
        LaueClass getLaueClass() {
            return laueClass;
        }

        /**
         * Returns a {@code Set} of {@code OperatorMatrices}
         * representing the characteristic symmetry operations of this space
         * group, exclusive of pure lattice translations other than centering
         * translations, and of products of such translations with other
         * symmetry operations.
         *
         * @return a {@code Set} of {@code OperatorMatrices}
         *         representing the characteristic symmetry operations of this
         *         space group
         */
        Set<SymmetryMatrix> getMatrixSet() {
            return Collections.unmodifiableSet(matrixSet);
        }

        /**
         * Generates new matrices in this space group's set of operator matrices
         * by application of the provided matrix.  All distinct matrices that
         * are positive powers of the provided matrix are applied.  No reference
         * to the specified matrix itself is retained, but an
         * {@code SymmetryMatrix} equal to it will be in the set after this
         * method finishes
         *
         * @param  matrix the {@code SymmetryMatrix} to apply
         *
         * @return {@code true} if and only if the internal matrix set
         *         changed as a result of this operation
         */
        boolean applyMatrix(SymmetryMatrix matrix) {
            SymmetryMatrix.Type matrixType = matrix.getType();
            Set<SymmetryMatrix> lastMatrices = matrixSet;
            boolean newMatricesAdded = false;

            /*
             * Apply the matrix enough times to generate all the necessary
             * additional matrices.  The size of the matrix set is increased by
             * a factor of the matrix' multiplicity if none of the distinct,
             * nontrivial powers of the matrix were already members.
             */
            for (int count = 1; count < matrixType.getMultiplicity(); count++) {
                Set<SymmetryMatrix> products = new HashSet<SymmetryMatrix>();

                for (SymmetryMatrix oldMatrix : lastMatrices) {
                    products.add(matrix.times(oldMatrix, true));
                }

                if (matrixSet.addAll(products)) {
                    newMatricesAdded = true;
                    lastMatrices = products;
                } else {
                    break;
                }
            }

            return newMatricesAdded;
        }

        /**
         * Causes this {@code SpaceGroup} to test itself to ensure that
         * it is valid, which for this purpose means that all products of
         * pairs of this group's operator matrices are among this group's
         * matrices themselves
         *
         * @throws InvalidDataException with reason code ILLEGAL_SPGP if this
         *         group is not valid
         */
        void testValidity() throws InvalidDataException {
            Set<SymmetryMatrix> matrices = getMatrixSet();

            for (SymmetryMatrix first : matrices) {
                for (SymmetryMatrix second : matrices) {
                    if (!matrices.contains(first.times(second, true))) {
                        throw new InvalidDataException(
                                "Invalid group (some group member products not "
                                + "members) interpreted as: " + getSymbol(),
                                getSymbol().toString(),
                                InvalidDataException.ILLEGAL_SPGP);
                    }
                }
            }
        }
    }

    /**
     * A class that encompasses knowledge of the symmetry matrices specific to
     * a particular category of lattice geometry.  The appropriate matrices for
     * most lattices are tabulated in <i>the International Tables for X-ray
     * Crystallography<i>, 4th Edition (1996), table 11.2 (p. 797); these are
     * the ones supported by this class, but subclasses may support different
     * ones
     */
    static class MatrixManager {
        
        /**
         * Adds to the provided space group the zero or more operations
         * corresponding to the lattice centering translations of the
         * specified group's symbol
         *
         * @param  group the {@code SpaceGroup} to which the centering
         *         operations should be added; the operations to add are
         *         determined by the group's symbol
         */
        void generateCenteringOps(SpaceGroup group) {
            char centering = group.getSymbol().getCentering();

            /*
             * Add centering translations; these are independant of the lattice
             * symmetry or cell setting
             */
            switch (centering) {
                case 'A':
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_A, 'n')));
                    break;
                case 'B':
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_B, 'n')));
                    break;
                case 'C':
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_C, 'n')));
                    break;
                case 'I':
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_FACE_DIAG, 'n')));
                    break;
                case 'F':
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_A, 'n')));
                    group.applyMatrix(new SymmetryMatrix(
                            createTranslationElements(DIRECTION_B, 'n')));
                    // Another centering vector is generated by the first two
                    break;
                case 'R':
                    SymmetryMatrix rmat =
                        new SymmetryMatrix(new int[] {8, 4, 4});

                    // apply it twice                        
                    group.applyMatrix(rmat);
                    group.applyMatrix(rmat);
                    
                    break;
                case 'P':
                    // nothing to do
                    break;
                default:

                    // The caller is responsible for checking the centering
                    assert false;
            }
        }

        /**
         * Creates an {@code SymmetryMatrix} representing a proper or
         * improper rotation of the specified order, aligned with the specified
         * symmetry direction, and having the specified screw component; this
         * method delegates to {@link #createRotationElements(int, int)} and
         * {@link #createTranslationElements(int, char)} to determine the
         * details of its return value
         *
         * @param  order the order of the rotation: 1, 2, 3, 4, or 6
         * @param  direction a code for the direction of the axis of rotation,
         *         one of the DIRECTION_* constants; only combinations of axis
         *         direction and rotation order that are seen in real space
         *         groups are supported
         * @param  screw the index of the screw translation for this rotation;
         *         must be zero if {@code isRotoinversion} is
         *         {@code true}
         * @param  isRotoinversion {@code true} if the desired matrix is
         *         for a rotoinversion of the specified order,
         *         {@code false} if it is for a proper rotation (with or
         *         without a screw translation)
         *
         * @return an {@code SymmetryMatrix} representing the specified
         *         rotation
         *
         * @throws IllegalArgumentException if the rotation order is not among
         *         those allowed; if the direction is not valid; or if the
         *         combination of arguments is not supported
         */
        SymmetryMatrix createRotationMatrix(int order, int direction,
                int screw, boolean isRotoinversion) {
            int[][] matrix = createRotationElements(order, direction);
            int[] vector;

            if (isRotoinversion) {
                if (screw != 0) {
                    throw new IllegalArgumentException(
                            "No such thing as a screw rotoinversion");
                } else if (order == 2) {
                    throw new IllegalArgumentException(
                            "A twofold rotoinversion is a mirror");
                } else {
                    vector = new int[3];
                    for (int i = 0; i < matrix.length; i++) {
                        for (int j = 0; j < matrix[0].length; j++) {
                            matrix[i][j] = -matrix[i][j];
                        }
                    }
                }
            } else if (screw > 0) {
                vector = createTranslationElements(
                        direction, (char) (order + '0'));

                for (int i = 0; i < vector.length; i++) {
                    vector[i] *= screw;
                }
            } else {
                vector = new int[3];
            }

            return new SymmetryMatrix(matrix, vector, true);
        }

        /**
         * Creates an {@code SymmetryMatrix} representing a reflection
         * through a plane perpendicular to the specified axis and passing
         * through the origin.  The method delegates to
         * {@link #createReflectionElements(int)} and
         * {@link #createTranslationElements(int, char)} to determine the
         * details of its return value
         *
         * @param  direction a code for the symmetry direction perpendicular to
         *         the plane of reflection; one of the DIRECTION_* constants
         * @param  type the reflection symbol representing the type of the
         *         desired reflection, one of: m, a, b, c, n, d 
         *
         * @return an {@code SymmetryMatrix} representing the specified
         *         reflection
         *
         * @throws IllegalArgumentException if {@code direction} is not
         *         a suitable direction code for a reflection operation, if
         *         {@code type} is not a valid reflection type symbol,
         *         or if the combination of direction and type is illegal
         */
        SymmetryMatrix createReflectionMatrix(int direction, char type) {

            switch (type) {
                case 'a':
                case 'b':
                case 'c':
                    if ((type - 'a') == direction) {
                        throw new IllegalArgumentException(
                                "glide vector perpendicular to reflection plane");
                    }

                    /* fall through */
                case 'm':
                case 'n':
                case 'd':
                    return new SymmetryMatrix(
                            createReflectionElements(direction),
                            createTranslationElements(direction, type),
                            true);

                // Unknown mirror type
                default:
                    throw new IllegalArgumentException("'" + type
                            + "' is not a valid reflection symbol");
            }
        }

        /**
         * Determines the {@code Operator} that corresponds to the
         * specified {@code SymmetryMatrix}
         *
         * @param  matrix the {@code SymmetryMatrix} for which an
         *         {@code Operator} is desired; pure translations are not
         *         supported
         *
         * @return an {@code Operator} representing the same symmetry
         *         operation encoded by {@code matrix}, with the
         *         corresponding symmetry axis specified and {@code matrix}
         *         assigned as the matrix
         *
         * @throws IllegalArgumentException if no {@code Operator} can be
         *         determined for the specified matrix
         */
        Operator determineOperator(SymmetryMatrix matrix) {

            /*
             * Implementation note: in the cases of operations that may have
             * inherent translation components, it is necessary to seperate
             * the inherent translation from any translation arising from the
             * location of the symmetry element relative to the coordinate
             * origin.  This is accomplished by composing the matrix with
             * itself enough times to produce a pure translation, which is then
             * a multiple of the inherent translation alone.  When such
             * compositions are performed, normalization of the resulting
             * translation must be disabled.
             */

            /*
             * TODO: Some potential confusion may be averted if this method
             * learns to distinguish between a 43+ and a 41-
             */
            Operator op;
            SymmetryMatrix composite;
            int[][] rotation;
            int[] translation;
            int direction;
            int tsum;

            switch(matrix.getType()) {
                case FOUR_BAR:
                    // four-fold rotoinversion
                    composite = matrix.times(matrix, false);
                    translation =
                            composite.times(composite, false).getTranslationVector();
                    rotation = matrix.getRotationMatrix();
                    direction = DIRECTION_NONE;
                    tsum = 0;
                    for (int i = 0; i < 3; i++) {
                        if (rotation[i][i] == -1) {
                            assert (direction == DIRECTION_NONE);
                            direction = i;
                        }
                        tsum += translation[i] / 12;
                    }
                    assert (direction >= 0);
                    assert ((tsum % 4) == 0);

                    op = new Operator(4, true, 0, ' ', direction);
                    break;
                case THREE_BAR:
                    // threefold rotoinversion
                    op = new Operator(3, true, 0, ' ', DIRECTION_BODY_DIAG);
                    break;
                case REFLECTION:
                    // reflection
                    rotation = matrix.getRotationMatrix();
                    translation =
                            matrix.times(matrix, false).getTranslationVector();
                    direction = determineTwofoldDirection(rotation, false);
                    
                    op = new Operator(0, false, 0,
                            determineMirrorType(direction, translation), direction);
                    break;
                case INVERSION:
                    // inversion : no axis
                    op = new Operator(1, true, 0, ' ');
                    break;
                case TWOFOLD:
                    // twofold rotation
                    boolean isScrew = false;

                    rotation = matrix.getRotationMatrix();
                    translation =
                            matrix.times(matrix, false).getTranslationVector();
                    direction = determineTwofoldDirection(rotation, true);

                    for (int t : translation) {
                        isScrew = (isScrew || (t != 0));
                    }

                    op = new Operator(2, false, (isScrew ? 1 : 0), ' ',
                                      direction);
                    break;
                case THREEFOLD:
                    // threefold rotation
                    op = new Operator(3, false, 0, ' ', DIRECTION_BODY_DIAG);
                    break;
                case FOURFOLD:
                    // fourfold rotation
                    composite = matrix.times(matrix, false);
                    rotation = matrix.getRotationMatrix();
                    translation =
                            composite.times(composite, false).getTranslationVector();
                    direction = DIRECTION_NONE;
                    tsum = 0;

                    for (int i = 0; i < 3; i++) {
                        if (rotation[i][i] == 1) {
                            assert (direction == DIRECTION_NONE);
                            direction = i;
                        }
                        assert ((translation[i] == 0) || (direction == i));
                        tsum += translation[i] / 12;
                    }
                    assert (direction >= 0);

                    if (tsum < 0) {
                        tsum += 4;
                    }
                    op = new Operator(4, false, tsum, ' ', direction);
                    break;
                default:
                    // other operation (should not happen)
                    assert false;
                    op = null;
            }

            op.setMatrix(matrix);
            return op;
        }

        /**
         * Returns the direction code for the twofold proper or improper
         * rotation represented by the provided rotation matrix.
         *
         * @param  matrix an {@code int[][]} containing the elements of
         *         a rotation matrix representing the twofold operation in
         *         question, in the same form as used and produced by the
         *         {@code SymmetryMatrix} class
         * @param  proper {@code true} if the matrix represents a proper
         *         twofold rotation, or {@code false} if it represents an
         *         improper one (i.e. a twofold rotoinversion a.k.a. a
         *         reflection)
         *
         * @return the direction code for the twofold proper or improper
         *         rotation represented by {@code matrix}
         *
         * @throws IllegalArgumentException if the provided {@code int[][]}
         *         is not recognized as the matrix of any twofold operation
         */
        int determineTwofoldDirection(int[][] matrix, boolean proper) {
            if (matrix[0][0] == 0) {
                for (int i = 1; i < 3; i++) {
                    if (matrix[0][i] != 0) {
                        return ((matrix[0][i] < 0) == proper)
                                ? DIRECTION_FACE_DIAG
                                : DIRECTION_FACE_DIAG_ALT;
                    }
                }
            } else if (matrix[1][1] == 0) {
                for (int i = 0; i < 3; i += 2) {
                    if (matrix[1][i] != 0) {
                        return ((matrix[1][i] < 0) == proper)
                                ? DIRECTION_FACE_DIAG
                                : DIRECTION_FACE_DIAG_ALT;
                    }
                }
            } else {
                int lookFor = (proper ? 1 : -1);
                for (int i = 0; i < 3; i++) {
                    if (matrix[i][i] == lookFor) {
                        return i;
                    }
                }
            }
            throw new IllegalArgumentException("Not a twofold matrix");
        }
        
        /**
         * Given the orientation of a reflection operation and the components
         * of the associated translation vector, determines the Hermann-Maugin
         * symbol for the (possibly glide) reflection
         *
         * @param  direction the direction code for the reflection operation in
         *         question; only consulted for potential 'n' glides
         * @param  translation an {@code int[]} containing the components
         *         of the translation, in units of <em>twenty-fourths</em>, such
         *         as is obtained as the translation component of the reflection
         *         composed with itself
         *
         * @return the {@code char} code for the reflection operation
         *
         * @throws IllegalArgumentException if the translation vector is not
         *         a valid glide translation for the specified direction
         */
        char determineMirrorType(int direction, int[] translation) {
            char mtype;
            int tsum = 0;
            
            /*
             * Check the translation vector and encode it into an integer
             */
            for (int i = 2; i >= 0; i--) {
                int x = Math.abs(translation[i]);

                if ((x % 6) != 0) {
                    throw new IllegalArgumentException(
                            "Invalid glide translation");
                } else {
                    tsum <<= 2;
                    tsum += (x / 6);
                }
            }

            /*
             * Determine the direction code based on the encoded vector
             */
            switch (tsum) {
                case 0:
                    mtype = 'm';
                    break;
                case 0x02:
                    mtype = 'a';
                    break;
                case 0x08:
                    mtype = 'b';
                    break;
                case 0x20:
                    mtype = 'c';
                    break;
                case 0x0a:
                case 0x22:
                case 0x28:
                    mtype = ((direction == DIRECTION_FACE_DIAG) ? 'g' : 'n');
                    break;
                case 0x2a:
                    if (direction == DIRECTION_FACE_DIAG) {
                        mtype = 'n';
                        break;
                    } else {
                        throw new IllegalArgumentException(
                                "Invalid glide translation for the given orientation"
                        );
                    }
                default:
                    int dTarget = 0x15;

                    if (direction < 3) {

                        /*
                         * mask off the bits corresponding to the symmetry
                         * direction of this mirror
                         */
                        dTarget &= ~(0x3 << (direction * 2));
                    } 
                    if ((tsum & 0x15) == dTarget) {
                        mtype = 'd';
                    } else {
                        mtype = 'g';
                    }

                    break;
            }

            return mtype;
        }

        /**
         * Returns an {@code int[][]} containing the elements of the
         * matrix for a rotation operation of the specified order along the
         * specified direction; delegates to createReflectionElements if the
         * requested order is 2 (and applies an inversion operation to the
         * resulting matrix)
         *
         * @param  order the rotational order of the desired rotation matrix;
         *         i.e. the number of distinct multiplicative powers of the
         *         matrix; this version supports values from 1 to 4 (inclusive)
         * @param  direction a code for the symmetry direction along which the
         *         desired rotation operation is aligned; one of the
         *         DIRECTION_* constants
         *
         * @return an {@code int[][]} containing the rotation matrix
         *         elements
         *
         * @throws IllegalArgumentException if the specified direction is not
         *         recognized or is inappropriate for a rotation of the
         *         specified order, or if the order of rotation is not among
         *         the supported ones
         */
        int[][] createRotationElements(int order, int direction) {

            /*
             * matrices derived from the International Tables for X-ray
             * Crystallography, 4th Edition (1996), table 11.2 (p. 797)
             */

            int[][] rval;

            if (((direction < 0) && (order != 1))
                    || (direction > DIRECTION_FACE_DIAG_ALT)) {
                throw new IllegalArgumentException(
                    "Invalid direction specified");
            }

            switch (order) {
                case 1:
                    rval = SymmetryMatrix.IDENTITY.getRotationMatrix();
                    break;
                case 2:
                    rval = createReflectionElements(direction);

                    for (int i = 0; i < rval.length; i++) {
                        for (int j = 0; j < rval[0].length; j++) {
                            rval[i][j] = -rval[i][j];
                        }
                    }
                    break;
                case 3:
                    if (direction == DIRECTION_BODY_DIAG) {
                        rval = new int[][] {{0, 0, 1}, {1, 0, 0}, {0, 1, 0}};
                    } else {
                        throw new IllegalArgumentException(
                                "Illegal direction for a threefold axis");
                    }
                    break;
                case 4:
                    if (direction < 3) {
                        int plus = (direction + 1) %3;
                        int minus = (direction + 2) % 3;

                        rval = new int[3][3];
                        rval[direction][direction] = 1;
                        rval[minus][plus] = 1;
                        rval[plus][minus] = -1;
                    } else {
                        throw new IllegalArgumentException(
                                "Illegal direction for a fourfold axis");
                    }
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unsupported rotation order");
            }

            return rval;
        }

        /**
         * Returns an {@code int[][]} containing the elements of the
         * rotation matrix for a reflection operation perpendicular to the
         * specified direction
         *
         * @param  direction a code for the symmetry direction perpendicular to
         *         the plane of reflection; one of the DIRECTION_* constants
         *
         * @return an {@code int[][]} containing the rotation matrix
         *         elements
         *
         * @throws IllegalArgumentException if the specified direction is not
         *         recognized or is inappropriate for mirror symmetry
         */
        int[][] createReflectionElements(int direction) {

            /*
             * matrices derived from the International Tables for X-ray
             * Crystallography, 4th Edition (1996), table 11.2  (p. 797)
             */

            int[][] matrix;

            switch (direction) {
                case DIRECTION_A:
                case DIRECTION_B:
                case DIRECTION_C:
                    matrix = SymmetryMatrix.IDENTITY.getRotationMatrix();
                    matrix[direction][direction] = -1;
                    break;
                case DIRECTION_FACE_DIAG:
                    matrix = new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}};
                    break;
                case DIRECTION_FACE_DIAG_ALT:
                    matrix = new int[][] {{0, -1, 0}, {-1, 0, 0}, {0, 0, 1}};
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Illegal direction for mirror symmetry");
            }

            return matrix;
        }

        /**
         * Creates an int[] containing the elements of a translation vector, in
         * units of twelfths, for a translation corresponding to the specified
         * base symmetry direction and translation type.  For mirrors (letters)
         * the translation is half a lattice vector, whereas for screws (e.g.
         * '2') it is 1/n of the corresponding lattice direction
         *
         * @param  direction for type 'n' and type 'd' translations, a code for
         *         a symmetry direction perpendicular to the translation; for
         *         types '2', '3', '4', and '6', the direction of the
         *         translation (which must be along a unit cell axis); ignored
         *         for other types except for verifying that it is valid.
         *         Should be one of the DIRECTION_* constants
         *
         * @param  type a code for the translation type, corresponding to one of
         *         the valid reflection symbols or non-trivial rotation orders.
         *         For types 'a', 'b', 'c', 'm', and ' ', this code fully
         *         specifies the translation vector (as a zero vector in the
         *         last two cases); for types 'n', 'd', '2', '3', '4', and '6',
         *         the {@code direction} argument fixes the translation
         *         direction among the possible choices.
         *
         * @return an {@code int[]} representing the specified translation
         *         in units of twelfths
         *
         * @throws IllegalArgumentException if the specified type is
         *         unrecognized or the combination of type and direction is
         *         disallowed
         */
        int[] createTranslationElements(int direction, char type) {
            int[] rval = new int[3];

            if ((direction < 0) || (direction > DIRECTION_FACE_DIAG_ALT)) {
                throw new IllegalArgumentException(
                    "Illegal symmetry direction");
            }

            switch (type) {
                case 'a':
                    // fall through
                case 'b':
                    // fall through
                case 'c':
                    rval[type - 'a'] = 6;
                    break;
                case 'n':
                    Arrays.fill(rval, 6);
                    if (direction < 3) {
                        rval[direction] = 0;
                    }
                    break;
                case 'd':
                    Arrays.fill(rval, 3);
                    if (direction < 3) {
                        rval[direction] = 0;
                    } else if (direction == DIRECTION_FACE_DIAG_ALT) {

                        // only happens for I -4 3 d (among valid groups)

                        rval[1] = 9;
                    }
                    break;
                case 'm':
                    // fall through
                case ' ':
                    // a zero translation; nothing to do
                    break;
                case '2':
                    if (direction == DIRECTION_FACE_DIAG) {
                        rval[DIRECTION_A] = 6;
                        rval[DIRECTION_B] = -6;

                        break;
                    } else if (direction == DIRECTION_FACE_DIAG_ALT) {
                        rval[DIRECTION_A] = 6;
                        rval[DIRECTION_B] = 6;

                        break;
                    }
                    // fall through
                case '3':
                    // fall through
                case '4':
                    // fall through
                case '6':
                    if (direction < 3) {
                        rval[direction] = (12 / (type - '0'));
                    } else {
                        throw new IllegalArgumentException(
                                "Illegal screw translation direction");
                    }

                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unrecognized translation type");
            }

            return rval;
        }
    }
    
    /**
     * A class that encompasses knowledge of the symmetry matrices specific to
     * hexagonal and trigonal lattice geometry.  The appropriate matrices for
     * these lattices are tabulated in <i>the International Tables for X-ray
     * Crystallography<i>, 4th Edition (1996), table 11.3 (p. 798)
     */
    static class HexCoordinateMatrixManager extends MatrixManager {

        /**
         * {@inheritDoc}.  This version handles matrices appropriate for
         * hexagonal coordinate systems.
         */
        @Override
        Operator determineOperator(SymmetryMatrix matrix) {

            /*
             * TODO: Some potential confusion may be averted if this method
             * learns to distinguish between a 65+ and a 61-, between a
             * 62+ and a 64-, and between a 32+ and a 31-
             */

            SymmetryMatrix matrixCubed;
            SymmetryMatrix matrixSixth;
            Operator rval;
            int shift;

            switch (matrix.getType()) {
                case THREE_BAR:
                    // threefold rotoinversion
                    rval = new Operator(3, true, 0, ' ', DIRECTION_C);
                    rval.setMatrix(matrix);
                    return rval;
                case THREEFOLD:
                    // threefold rotation
                    matrixCubed =
                            matrix.times(matrix.times(matrix, false), false);
                    shift = matrixCubed.getTranslationVector()[DIRECTION_C] / 12;
                    if (shift < 0) {
                        shift += 3;
                    }
                    rval = new Operator(3, false, shift, ' ', DIRECTION_C);
                    rval.setMatrix(matrix);
                    return rval;
                case SIX_BAR:
                    // sixfold rotoinversion
                    rval = new Operator(6, true, 0, ' ', DIRECTION_C);
                    rval.setMatrix(matrix);
                    return rval;
                case SIXFOLD:
                    // sixfold rotation
                    matrixCubed =
                            matrix.times(matrix.times(matrix, false), false);
                    matrixSixth = matrixCubed.times(matrixCubed, false);
                    shift = matrixSixth.getTranslationVector()[DIRECTION_C] / 12;
                    if (shift < 0) {
                        shift += 6;
                    }
                    rval = new Operator(6, false, shift, ' ', DIRECTION_C);
                    rval.setMatrix(matrix);
                    return rval;
                default:
                    return super.determineOperator(matrix);
            }
        }

        /**
         * {@inheritDoc}.  This version handles matrices appropriate for
         * hexagonal coordinate systems.
         *
         * @throws IllegalArgumentException if {@code matrix} is not
         *         recognized as a twofold matrix along one of the symmetry
         *         directions of this Laue class
         */
        @Override
        int determineTwofoldDirection(int[][] matrix, boolean proper) {

            /*
             * the relevant matrices are tabulated in the International Tables
             * for X-ray Crystallography, 4th Edition (1996), table 11.3
             * (p. 798)
             */
            int adir = matrix[0][1];
            int bdir = matrix[1][0];

            switch ((adir + bdir) * (proper ? -1 : 1)) {
                case -2:
                    return DIRECTION_A;
                case  1:
                    return (adir != 0) ? DIRECTION_A : DIRECTION_B;
                case  0:
                    return DIRECTION_C;
                case -1:
                case  2: // -1 and +2 go together
                    return DIRECTION_FACE_DIAG;
                default:
                    throw new IllegalArgumentException(
                        "Invalid operator matrix for this Laue class");
            }
        }

        /**
         * {@inheritDoc}.  This version recognizes the 'g' glides found in
         * some rhombohedral space groups, but otherwise delegates to the
         * superclass' implementation
         *
         * @param  direction {@inheritDoc}
         * @param  translation {@inheritDoc}
         *
         * @return {@inheritDoc}
         *
         * @throws IllegalArgumentException if the translation vector is not
         *         a valid glide translation for the specified direction
         */
        @Override
        char determineMirrorType(int direction, int[] translation) {

            /*
             * The superclass' handling is sufficient for the c-axis direction
             */
            if (direction == DIRECTION_C) {
                return super.determineMirrorType(direction, translation);
            }

            /*
             * Check the translation vector; if all elements are integral
             * numbers of sixths but none are integral numbers of fourths then
             * the mirror looks like a rhombohedral 'g' glide; otherwise
             * delegate to the superclass
             */
            for (int i = 0; i < 3; i++) {
                int x = Math.abs(translation[i]);

                if (((x % 4) != 0) || ((x % 6) == 0)) {
                    return super.determineMirrorType(direction, translation);
                }
            }

            return 'g';
        }

        /**
         * {@inheritDoc}.  This version provides support for operations defined
         * differently (or only) for hexagonal coordinate systems.
         *
         * @throws IllegalArgumentException if the specified direction is not
         *         recognized or is inappropriate for or rotation of the
         *         specified order
         */
        @Override
        int[][] createRotationElements(int order, int direction) {
            if (order == 4) {
                throw new IllegalArgumentException(
                        "Fourfold symmetry inconsistent with hexagonal axes.");
            }
            if (direction == DIRECTION_C) {
                if (order == 3) {
                    return new int[][] {{0, -1, 0}, {1, -1, 0}, {0, 0, 1}};
                } else if (order == 6) {
                    return new int[][] {{1, -1, 0}, {1, 0, 0}, {0, 0, 1}};
                }
            } else {
                if ((order % 3) == 0) {
                    throw new IllegalArgumentException(
                        "Three- and sixfold symmetry must lie along the c axis in the hexagonal system.");
                }
            }

            /*
             * twofold axes perpendicular to DIRECTION_C are supported
             * indirectly by getReflectionElements
             */

            return super.createRotationElements(order, direction);
        }

        /**
         * {@inheritDoc}.  This version generates matrix elements appropriate
         * for hexagonal coordinate systems.
         *
         * @throws IllegalArgumentException if {@code direction} is not
         *         recognized or is not an appropriate direction for mirror
         *         symmetry
         */
        @Override
        int[][] createReflectionElements(int direction) {

            /*
             * matrix derived from the International Tables for X-ray
             * Crystallography, 4th Edition (1996), table 11.3 (p. 798)
             */

            switch (direction) {
                case DIRECTION_A:
                    return new int[][] {{-1, 1, 0}, {0, 1, 0}, {0, 0, 1}};
                case DIRECTION_B:
                    return new int[][] {{1, 0, 0}, {1, -1, 0}, {0, 0, 1}};
                case DIRECTION_FACE_DIAG:
                    return new int[][] {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}};
                default:
                    return super.createReflectionElements(direction);
            }
        }
    }
    
    /**
     * <p>
     * An abstract class defining the interface and common behavior of
     * objects that analyze space group symbols per the specifications of
     * the <i>International Tables for X-ray Crystallography</i>; fourth
     * edition, revised (1996); chapter 12.
     * </p><p>
     * This class bases all analysis on the symmetry direction and symmetry
     * order information provided by an associated {@code LaueClass}
     * </p> 
     */
    static abstract class AbstractSymbolAnalyzer {
            
        /**
         * An array of indices into the operator list, used to select the
         * sequence in which the operators are considered for use as space group
         * generators.  In most cases the sequence is the same as that of the
         * list itself, but there can be exceptions.
         */
        int[] generatorSequence;

        /**
         * The LaueClass providing the basis for this analyzer's analysis
         */
        private LaueClass laueClass;
            
        /**
         * Initializes a new {@code AbstractSymbolAnalyzer} with the
         * specified Laue class
         *
         * @param  laueClass the {@code LaueClass} upon which this
         *         analyzer should base its analysis; must not be
         *         {@code null}
         */
        AbstractSymbolAnalyzer(LaueClass laueClass) {
            int directionCIndex = -1;
            int index;

            assert (laueClass != null);

            this.laueClass = laueClass;
            
            /*
             * Create the generator sequence by analysis of the Laue class'
             * symmetry directions.  The zeroth element should be the one
             * referring to the C-axis direction if that is a symmetry direction
             * for the Laue class.  The remaining directions follow in the
             * original relative sequence.
             */
            generatorSequence = new int[laueClass.getNumDirections()];
            for (int i = 0; i < generatorSequence.length; i++) {
                if (laueClass.getSymmetryDirection(i) == DIRECTION_C) {
                    directionCIndex = i;
                    break;
                }
            }
            if (directionCIndex >= 0) {
                generatorSequence[0] = directionCIndex;
                index = 1;
            } else {
                index = 0;
            }
            for (int i = 0; i < generatorSequence.length; i++) {
                if (i != directionCIndex) {
                    generatorSequence[index++] = i;
                }
            }
        }

        /**
         * Returns the Laue class used by this analyzer
         * 
         * @return the {@code LaueClass} used by this analyzer in its
         *         analyses
         */
        LaueClass getLaueClass() {
            return laueClass;         
        }
            
        /**
         * Creates and returns a list of generator matrices with which to
         * create the space group described by the specified symbol,
         * provided that the symbol describes a valid space group supported
         * by this analyzer.  This method creates the same generators for
         * the two members of each enantiomorphic pair because it is desired
         * that they have the same canonical symbol.  As a side effect, this
         * method assigns symmetry directions to the {@code Operators}
         * of the symbol.
         *
         * @param  symbol a {@code SpaceGroupSymbol} representing the
         *         space group of this Laue class for which a
         *         {@code SpaceGroup} object is desired; should be set
         *         such that the lattice centering is among the standard
         *         ones for this Laue class.
         *
         * @return a {@code List} of {@code SymmetryMatrix}
         *         objects corresponding to {@code symbol}
         *
         * @throws IllegalArgumentException if {@code symbol} encodes
         *         generating operations not supported by this analyzer's
         *         associated Laue class
         */
        abstract List<SymmetryMatrix> getGenerators(SpaceGroupSymbol symbol);
                    
        /**
         * Verifies that a collection of {@code Operator}s is
         * consistent with a specified {@code SpaceGroupSymbol} and
         * the Laue class associated with this analyzer.  Specifically, checks
         * that the symbol's operators are all represented among the provided
         * ones, that the provided operators do not include any reflection
         * operations along directions where none are symbolized, and that
         * the provided operations are consistent with the expected order of
         * symmetry along each direction.
         *
         * @param  symbol a {@code SpaceGroupSymbol} to be checked for
         *         consistency with the provided operators.  The symbol's
         *         own {@code Operator}s should have already been
         *         assigned symmetry directions consistent with this Laue
         *         class
         * @param  axialOps an array of {@code List}s of
         *         {@code Operator}s, such as is prepared by
         *         {@link SpaceGroupSymbolBL#extractOperations(SpaceGroup,
         *         List[], List)}
         *
         * @throws InvalidDataException with reason code ILLEGAL_SPGP if an
         *         inconsistency is detected
         */
        void verifySymmetry(SpaceGroupSymbol symbol, List<Operator>[] axialOps)
                throws InvalidDataException {
            OperatorList operators = symbol.getOperators();
            LaueClass lc = getLaueClass();

            /*
             * Check the symbol against the symmetry operator lists
             */
            for (ListIterator<Operator> it = operators.listIterator();
                    it.hasNext(); ) {
                Operator op = it.next();
                int direction = op.getDirection();

                // The direction should have been correctly set
                assert (direction ==
                        lc.getSymmetryDirection(it.previousIndex()));

                List<Operator> equivAxisOps = new ArrayList<Operator>();

                for (int i = 0; i < axialOps.length; i++) {
                    if (lc.getRepresentativeDirection(i) == direction) {
                        for (Operator op2 : axialOps[i]) {
                            op2 = op2.clone();
                            op2.setDirection(direction);
                            equivAxisOps.add(op2);
                        }
                    }
                }

                /*
                 * All symmetry operations symbolized in the provided symbol
                 * should be present, especially including those not used to
                 * generate the group
                 */
                if (!equivAxisOps.containsAll(op.split())) {
                    throw new InvalidDataException(
                            "Invalid symbol (does not generate itself),"
                            + " interpreted as: " + symbol,
                            symbol.toString(),
                            InvalidDataException.ILLEGAL_SPGP);
                }

                /*
                 * No mirrors should have been generated where they were not
                 * symbolized; exception: -6 is equivalent to 3/m, thus a
                 * mirror will accompany -6.
                 */
                if (!op.hasMirrorComponent()
                        && !((op.getOrder() == 6) && op.isRotoInversion())) {
                    for (Operator op2 : equivAxisOps) {
                        if (op2.hasMirrorComponent()) {
                            throw new InvalidDataException(
                                    "Invalid symbol (generates extra mirrors),"
                                    + " interpreted as: " + symbol,
                                    symbol.toString(),
                                    InvalidDataException.ILLEGAL_SPGP);
                        }
                    }
                }
            }

            /*
             * Check lists against Laue class
             */
            for (int direction = 0; direction < axialOps.length; direction++) {
                int expectedOrder = lc.getExpectedOrder(direction);
                int maxOrder = 1;

                for (Operator op : axialOps[direction]) {
                    int opOrder = op.getOrder();

                    if ((expectedOrder % opOrder) != 0) {
                        throw new InvalidDataException(
                                "Invalid symbol (generates unexpected symmetry),"
                                + " interpreted as: " + symbol,
                                symbol.toString(),
                                InvalidDataException.ILLEGAL_SPGP);
                    } else if (opOrder > maxOrder) {
                        maxOrder = opOrder;
                    }
                }

                /*
                 * Note: maxOrder cannot be greater than expectedOrder at this
                 * point; the above loop would have thrown an exception
                 */

                if (maxOrder != expectedOrder) {
                    throw new InvalidDataException(
                            "Invalid symbol (does not generate full expected symmetry),"
                            + " interpreted as: " + symbol,
                            symbol.toString(),
                            InvalidDataException.ILLEGAL_SPGP);
                }
            }
        }
    }
    
    /**
     * A SymbolAnalyzer implementation for most symmetry classes
     */
    static class BasicSymbolAnalyzer extends AbstractSymbolAnalyzer {
            
        /**
         * Initializes a new {@code BasicSymbolAnalyzer} with the
         * specified Laue class
         *
         * @param  laueClass the {@code LaueClass} upon which this
         *         analyzer should base its analysis; must not be
         *         {@code null}
         */
        BasicSymbolAnalyzer(LaueClass laueClass) {
            super(laueClass);
        }

        /**
         * {@inheritDoc}
         *
         * @throws IllegalArgumentException {@inheritDoc}
         */
        @Override
        List<SymmetryMatrix> getGenerators(SpaceGroupSymbol symbol) {
            LaueClass lc = getLaueClass();
            OperatorList operators = symbol.getOperators();

            /*
             * This is the wrong analyzer class if it's Laue class requires a
             * different number of operators than the symbol contains
             */
            assert(operators.size() == lc.getNumDirections());

            MatrixManager mm = lc.getMatrixManager();
            List<SymmetryMatrix> generators = new ArrayList<SymmetryMatrix>();
            boolean isRotationGroup = true;
            boolean hasIndicator = false;
            int locationPart = 0;
            Operator op;
            int direction;
            int rotation;
            int screw;
                
            // The first operator

            op = operators.getOperator(generatorSequence[0]);
            direction = lc.getSymmetryDirection(generatorSequence[0]);
            rotation = op.getRotationOrder();
            screw = op.getScrewTranslation();
            op.setDirection(direction);

            // Handle any rotation component
            if (rotation != 0) {

                assert ((0 <= screw) && (screw < rotation));

                /*
                 * Choose the same screw translation for both members of each
                 * enantiomeric pair, thus giving them the same canonical symbol
                 */
                if (screw > 0) {
                    screw = Math.min(screw, rotation - screw);
                }

                // Is it an indicator?
                if ((lc.getNumDirections() == 3)
                        && (direction == DIRECTION_C)
                        && ((rotation % 2) == 0)) {
                    hasIndicator = true;
                    locationPart = -((12 * screw) / rotation);
                } else {  // a generator (not an indicator)
                    generators.add(mm.createRotationMatrix(
                            rotation, direction, screw, op.isRotoInversion()));
                }
            }

            // Reflections are always generators
            if (op.hasMirrorComponent()) {
                isRotationGroup = false;
                generators.add(mm.createReflectionMatrix(
                        direction, op.getMirrorComponent()));
            }

            if (lc.getNumDirections() > 1) {
                SymmetryMatrix firstTwo;

                // the second operator

                op = operators.getOperator(generatorSequence[1]);
                direction = lc.getSymmetryDirection(generatorSequence[1]);
                rotation = op.getRotationOrder();
                screw = op.getScrewTranslation();
                op.setDirection(direction);

                assert (op.getOrder() <= 2);

                // If there is a mirror, use it (only)
                if (op.hasMirrorComponent()) {
                    isRotationGroup = false;
                    firstTwo = mm.createReflectionMatrix(
                            direction, op.getMirrorComponent());
                } else {

                    assert ((0 <= screw) && (screw < rotation));

                    firstTwo = mm.createRotationMatrix(
                            rotation, direction, screw, op.isRotoInversion());
                }
                generators.add(firstTwo);

                if (lc.getNumDirections() > 2) {
                    SymmetryMatrix secondTwo;

                    // the third operator

                    op = operators.getOperator(generatorSequence[2]);
                    direction = lc.getSymmetryDirection(generatorSequence[2]);
                    rotation = op.getRotationOrder();
                    screw = op.getScrewTranslation();
                    op.setDirection(direction);

                    assert (op.getOrder() <= 2);

                    // If there is a mirror, use it (only)
                    if (op.hasMirrorComponent()) {
                        secondTwo = mm.createReflectionMatrix(
                                direction, op.getMirrorComponent());
                    } else {

                        assert ((0 <= screw) && (screw < rotation));

                        secondTwo = mm.createRotationMatrix(
                                rotation, direction, screw, op.isRotoInversion());

                        if (isRotationGroup && hasIndicator) {

                            /*
                             * a location vector applies to the current
                             * generator
                             */
                            secondTwo = secondTwo.plus(
                                    new int[] {0, 0, locationPart}, true);
                        }
                    }
                    
                    /*
                     * When the symbol contains an indicator, the product of
                     * firstTwo and secondTwo will be the matrix of the
                     * indicator operation, correctly positioned in the cell.
                     * When that matrix is added to the space group, the
                     * SpaceGroup is responsible for applying it the correct
                     * number of times to fully generate its symmetry.
                     * 
                     * All that is unnecessary when there's no indicator 
                     */
                    generators.add(hasIndicator
                            ? firstTwo.times(secondTwo, true)
                            : secondTwo);
                }
            }

            return generators;
        }
    }

    /**
     * A SymbolAnalyzer implementation for Triclinic symmetry, which is
     * special because it has no direction of special symmetry
     */
    static class TriclinicSymbolAnalyzer extends AbstractSymbolAnalyzer {
            
        /**
         * Initializes a new {@code TriclinicSymbolAnalyzer} with the
         * specified Laue class
         *
         * @param  laueClass the {@code LaueClass} upon which this
         *         analyzer should base its analysis; must not be
         *         {@code null}
         */
        TriclinicSymbolAnalyzer(LaueClass laueClass) {
            super(laueClass);
        }

        /**
         * {@inheritDoc}
         *
         * @throws IllegalArgumentException {@inheritDoc}
         */
        @Override
        List<SymmetryMatrix> getGenerators(SpaceGroupSymbol symbol) {
            OperatorList operators = symbol.getOperators();

            assert (operators.size() == 1);

            Operator op0 = operators.getOperator(0);

            assert (op0.getOrder() == 1);

            return Collections.singletonList(
                    op0.isRotoInversion()
                            ? SymmetryMatrix.INVERSION
                            : SymmetryMatrix.IDENTITY);
        }

        /**
         * {@inheritDoc}
         *
         * @param  symbol {@inheritDoc}
         * @param  axialOps {@inheritDoc}
         *
         * @throws InvalidDataException {@inheritDoc}
         */
        @Override
        void verifySymmetry(SpaceGroupSymbol symbol, List<Operator>[] axialOps)
                throws InvalidDataException {
            Comparator<Operator> comp
                    = getLaueClass().getOperatorComparator('P');

            /*
             * Where any operators are described, the highest precedence one
             * should have order 1.
             */            
            for (int i = 0; i < axialOps.length; i++) {
                if ((axialOps[i].size() > 0)
                        && ((Collections.min(axialOps[i], comp)).getOrder()
                            != 1)) {

                    /*
                     * This should in fact never happen.  No input symbol should
                     * be able to cause the Triclinic Laue class to generate
                     * axial symmetry.
                     */

                    throw new InvalidDataException(
                            "Invalid symbol (generates unexpected symmetry),"
                            + " interpreted as: " + symbol,
                            symbol.toString(),
                            InvalidDataException.ILLEGAL_SPGP);
                }
            }
        }
    }
        
    /**
     * A {@code SymbolAnalyzer} implementation for cubic symmetry,
     * which is special because it has more than one direction with
     * symmetry order greater than two.
     */
    static class CubicSymbolAnalyzer extends AbstractSymbolAnalyzer {
            
        /**
         * Initializes a new {@code CubicSymbolAnalyzer} with the
         * specified Laue class
         *
         * @param  laueClass the {@code LaueClass} upon which this
         *         analyzer should base its analysis; must not be
         *         {@code null}
         */
        CubicSymbolAnalyzer(LaueClass laueClass) {
            super(laueClass);
        }

        /**
         * {@inheritDoc}.  This version handles the special cases of
         * generators for cubic groups.
         *
         * @throws IllegalArgumentException {@inheritDoc}
         */
        @Override
        List<SymmetryMatrix> getGenerators(SpaceGroupSymbol symbol) {
            OperatorList operators = symbol.getOperators();

            assert (operators.size() >= 2);

            LaueClass lc = getLaueClass();
            MatrixManager mm = lc.getMatrixManager();
            List<SymmetryMatrix> generators = new ArrayList<SymmetryMatrix>();
            boolean isRotationGroup = true;
            int locationPart = 0;
            SymmetryMatrix threePlus;
            SymmetryMatrix threeMinus;
            SymmetryMatrix twoZ;
            Operator op;
            int direction;
            int rotation;
            int screw;
            
            // First Operator

            op = operators.getOperator(generatorSequence[0]);
            direction = lc.getSymmetryDirection(generatorSequence[0]);
            op.setDirection(direction);

            assert ((direction >= 0) && (direction < 3));

            if (op.hasMirrorComponent()) {

                /*
                 * Record the matrix as the twofold operation along the Z
                 * axis, and add it as a generator
                 */

                twoZ = mm.createReflectionMatrix(
                        direction, op.getMirrorComponent());
                isRotationGroup = false;
                generators.add(twoZ);
            } else {
                rotation = op.getOrder();
                screw = op.getScrewTranslation();

                if (operators.size() == 2) {
                    
                    // must be point group 23
                    
                    assert (rotation == 2);
                    
                    /*
                     * Record the matrix as the twofold operation along the Z
                     * axis, but it is not itself a generator
                     */

                    twoZ = mm.createRotationMatrix(
                            rotation, direction, screw, op.isRotoInversion());

                    // add in the location part of the operation
                    if (screw > 0) {
                        twoZ = twoZ.plus(mm.createTranslationElements(
                                (direction + 1) % 3, '2'), true);
                    }

                } else {
                    
                    // must be point group 432 or -43m
                    
                    assert (rotation == 4);

                    /*
                     * this operator is an indicator.  Cannot identify the
                     * correct twofold along Z at this point.
                     */
                    
                    twoZ = null;
                    locationPart = ((12 * screw) / rotation);
                }
            }

            // Second Operator

            op = operators.getOperator(generatorSequence[1]);
            direction = lc.getSymmetryDirection(generatorSequence[1]);
            rotation = op.getRotationOrder();
            op.setDirection(direction);

            assert (rotation == 3);
            assert (direction == DIRECTION_BODY_DIAG);

            if (op.hasMirrorComponent()) {
                throw new IllegalArgumentException(
                        "Reflections not allowed in body diagonal direction");
            } else {

                /*
                 * Choose the threefold matrices to use in this group, always
                 * using a proper 3 (even when there is a -3 present) because
                 * the correct location of the inversion point is not known
                 */
                threePlus = mm.createRotationMatrix(rotation, direction,
                        op.getScrewTranslation(), false);
                threeMinus = threePlus.times(threePlus, true);
            }

            // Third Operator, if present

            if (operators.size() > 2) {
                SymmetryMatrix tmatrix1;

                op = operators.getOperator(generatorSequence[2]);
                direction = lc.getSymmetryDirection(generatorSequence[2]);

                assert (direction > 3);
                assert (op.getOrder() == 2);

                op.setDirection(direction);

                if (op.hasMirrorComponent()) {
                    tmatrix1 = mm.createReflectionMatrix(
                            direction, op.getMirrorComponent());
                } else {
                    rotation = op.getRotationOrder();
                    screw = op.getScrewTranslation();

                    tmatrix1 = mm.createRotationMatrix(
                            rotation, direction, screw, op.isRotoInversion());
                    if (isRotationGroup) {
                        
                        // add in the location part of the operation
                        tmatrix1 = tmatrix1.plus(new int[] {
                                -locationPart, locationPart, locationPart},
                                true);
                    }
                }
                generators.add(tmatrix1);
                
                if (twoZ == null) {
                    
                    /*
                     * Compute the appropriate twofold operation along the Z
                     * direction as a product of the already-identified
                     * generators
                     */
                    
                    SymmetryMatrix tmatrix2 = threePlus.times(tmatrix1, true);

                    tmatrix2 = tmatrix2.times(tmatrix2, true);
                    tmatrix1 = threeMinus.times(tmatrix1, true);
                    tmatrix1 = tmatrix1.times(tmatrix1, true);
                    twoZ = tmatrix2.times(tmatrix1, true);
                }
            }

            assert (twoZ != null);
            
            /* 
             * Assign generators: twofold operations along the X and Y
             * directions (depending on the operators, a twofold along Z may
             * already have been assigned) and a threefold operation oriented
             * so as to generate all remaining positions without duplication 
             */
            generators.add(threePlus.times(twoZ.times(threeMinus), true));
            generators.add(threeMinus.times(twoZ.times(threePlus), true));
            generators.add(twoZ.times(threePlus.times(twoZ), true));

            return generators;
        }
    }
        
    /**
     * <p>
     * This is a key class of the space group symbol logic.  It represents
     * "Laue classes", which are categorizations of crystallographic space
     * groups, and it encompasses the Laue-class-specific rules for assigning
     * symmetry directions to the operators appearing in a space group symbol.
     * Instances of this class delegate to appropriate
     * {@code MatrixManager}, {@code AbstractSymbolAnalyzer}
     * and {@code OperatorComparator} objects to define most of their
     * functionality 
     * </p><p>
     * This class does not expose a public constructor; it is intended that
     * the static instances exposed by this class be used for all requirements.
     * </p>
     */
    static class LaueClass {
        
        /*
         * TODO: evaluate whether this class really needs to worry about centering symbols
         */

        /**
         * A {@code LaueClass} representing Laue class -1.
         */
        final static LaueClass TRICLINIC = 
                new LaueClass(new int[0], new int[0], "P");
                
        /*
         * Special configuration for the triclinic Laue class
         */
        static {
            TRICLINIC.setSymbolAnalyzer(new TriclinicSymbolAnalyzer(TRICLINIC));
        }

        /**
         * A {@code LaueClass} representing Laue class 2/m.
         */
        final static LaueClass MONOCLINIC =
                new LaueClass(new int[] {DIRECTION_B}, new int[] {2}, "PC");

        /**
         * A {@code LaueClass} representing Laue class mmm.
         */
        final static LaueClass ORTHORHOMBIC =
                new LaueClass(new int[] {DIRECTION_A, DIRECTION_B, DIRECTION_C},
                              new int[] {2, 2, 2}, "PACIF");

        /**
         * A {@code LaueClass} representing Laue class 4/m.
         */
        final static LaueClass TETRAGONAL_LOW =
                new LaueClass(new int[] {DIRECTION_C}, new int[] {4}, "PI");

        /**
         * A {@code LaueClass} representing Laue class 4/mmm.
         */
        final static LaueClass TETRAGONAL_HIGH =
                new LaueClass(new int[]
                              {DIRECTION_C, DIRECTION_A, DIRECTION_FACE_DIAG},
                              new int[] {4, 2, 2}, "PI");

        /*
         * Special configuration for tetragonal Laue classes
         */
        static {
            Comparator<Operator> comp =  new OperatorComparator() {
    
                /**
                 * A string containing the valid reflection symbols, in the
                 * special-case sorting order for the a and b directions
                 */
                private final static String AB_REFLECTIONS = "mcband";

                @Override
                protected String getSequence(int direction) {
                    switch (direction) {
                        case DIRECTION_A:
                        case DIRECTION_B:
                            return AB_REFLECTIONS;
                        default:
                            return super.getSequence(direction);
                    }
                }
            };
            
            TETRAGONAL_LOW.setOperatorComparator(comp);
            TETRAGONAL_HIGH.setOperatorComparator(comp);
            TETRAGONAL_HIGH.setRepresentativeDirection(
                    DIRECTION_B, DIRECTION_A);
            TETRAGONAL_HIGH.setRepresentativeDirection(
                    DIRECTION_FACE_DIAG_ALT, DIRECTION_FACE_DIAG);
        }
        
        /**
         * A {@code LaueClass} representing primitive Laue class -3.
         */
        final static LaueClass TRIGONAL_LOW =
                new LaueClass(new int[] {DIRECTION_C}, new int[] {3}, "P");

        /**
         * A {@code LaueClass} representing Laue class -3m1.
         */
        final static LaueClass TRIGONAL_HIGH_1 =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_A,
                                         DIRECTION_FACE_DIAG},
                              new int[] {3, 2, 1}, "P");

        /**
         * A {@code LaueClass} representing Laue class -31m.
         */
        final static LaueClass TRIGONAL_HIGH_2 =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_A,
                                         DIRECTION_FACE_DIAG},
                              new int[] {3, 1, 2}, "P");

        /**
         * A {@code LaueClass} representing rhombohedral Laue class -3.
         */
        final static LaueClass RHOMBOHEDRAL_LOW =
                new LaueClass(new int[] {DIRECTION_C}, new int[] {3}, "R");

        /**
         * A {@code LaueClass} representing rhombohedral Laue class -3m.
         */
        final static LaueClass RHOMBOHEDRAL_HIGH =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_A},
                              new int[] {3, 2}, "R");

        /**
         * A {@code LaueClass} representing Laue class 6/m.
         */
        final static LaueClass HEXAGONAL_LOW =
                new LaueClass(new int[] {DIRECTION_C}, new int[] {6}, "P");

        /**
         * A {@code LaueClass} representing Laue class 6/mmm.
         */
        final static LaueClass HEXAGONAL_HIGH =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_A,
                                         DIRECTION_FACE_DIAG},
                              new int[] {6, 2, 2}, "P");

        /*
         * Special configuration for Laue classes referred to hexagonal axes
         */
        static {
            MatrixManager mm = new HexCoordinateMatrixManager();

            /*
             * A {@code Comparator} that permits 'g' glides (at minimum
             * priority), for use by high-symmetry rhombohedral groups
             */
            Comparator<Operator> rcomp = new OperatorComparator() {
    
                /**
                 * A string containing the valid symbols for reflections through
                 * planes parallel to the c axis in a hexagonal coordinate
                 * system, including 'g' for certain glide planes observed in
                 * high-symmetry rhombohedral space groups, in the standard
                 * sorting order
                 */
                private final static String VERTICAL_REFLECTIONS = "mcg";
    
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected String getSequence(int direction) {
                    return ((direction == DIRECTION_C)
                                    ? VALID_REFLECTIONS
                                    : VERTICAL_REFLECTIONS);
                }
            };

            TRIGONAL_LOW.setMatrixManager(mm);
            
            TRIGONAL_HIGH_1.setMatrixManager(mm);
            TRIGONAL_HIGH_1.setRepresentativeDirection(
                    DIRECTION_B, DIRECTION_A);
                    
            TRIGONAL_HIGH_2.setMatrixManager(mm);
            TRIGONAL_HIGH_2.setRepresentativeDirection(
                    DIRECTION_B, DIRECTION_A);
                    
            RHOMBOHEDRAL_LOW.setMatrixManager(mm);
            
            RHOMBOHEDRAL_HIGH.setMatrixManager(mm);
            RHOMBOHEDRAL_HIGH.setRepresentativeDirection(
                    DIRECTION_B, DIRECTION_A);
            RHOMBOHEDRAL_HIGH.setOperatorComparator(rcomp);
                    
            HEXAGONAL_LOW.setMatrixManager(mm);
            
            HEXAGONAL_HIGH.setMatrixManager(mm);
            HEXAGONAL_HIGH.setRepresentativeDirection(DIRECTION_B, DIRECTION_A);
        }

        /**
         * A {@code LaueClass} representing Laue class m-3
         */
        final static LaueClass CUBIC_LOW =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_BODY_DIAG},
                              new int[] {2, 3}, "PIF");

        /**
         * A {@code LaueClass} representing Laue class m-3m and intended
         * for use with acentric space groups of that class (those having point
         * group 432 or -43m)
         */
        final static LaueClass CUBIC_HIGH_SPECIAL =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_BODY_DIAG,
                                         DIRECTION_FACE_DIAG_ALT},
                              new int[] {4, 3, 2}, "PIF");

        /**
         * A {@code LaueClass} representing Laue class m-3m and intended
         * for use with centric space groups of that class (those having point
         * group m-3m)
         */
        final static LaueClass CUBIC_HIGH =
                new LaueClass(new int[] {DIRECTION_C, DIRECTION_BODY_DIAG,
                                         DIRECTION_FACE_DIAG},
                              new int[] {4, 3, 2}, "PIF");

        /*
         * Special configuration for cubic Laue classes
         */
        static {
            
            /*
             * A {@code Comparator} that implements the standard priority order
             * for Operators used in construction of canonical space group symbols
             * for non-face-centered cubic groups
             */
            Comparator<Operator> opComparator = new OperatorComparator() {
    
                /**
                 * A string containing the valid reflection symbols, in the
                 * special-case sorting order for the face diagonal direction;
                 * includes the 'g' symbol for certain glides in face-centered
                 * cubic systems
                 */
                private final static String FACE_DIAG_REFLECTIONS = "mnabcdg";
    
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected String getSequence(int direction) {
                    return (((direction == DIRECTION_FACE_DIAG)
                            || (direction == DIRECTION_FACE_DIAG_ALT))
                                    ? FACE_DIAG_REFLECTIONS
                                    : VALID_REFLECTIONS);
                }
            };

            /*
             * A {@code Comparator} that implements the standard priority order
             * for Operators used in construction of canonical space group symbols
             * for face-centered cubic groups
             */
            Comparator<Operator> fcOpComparator = new OperatorComparator() {
    
                /**
                 * A string containing the valid reflection symbols, in the
                 * special-case sorting order for the face diagonal direction;
                 * includes the 'g' symbol for certain glides in face-centered
                 * cubic systems
                 */
                private final static String FACE_DIAG_REFLECTIONS = "mcnabdg";
    
                /**
                 * {@inheritDoc}
                 */
                @Override
                protected String getSequence(int direction) {
                    return (((direction == DIRECTION_FACE_DIAG)
                            || (direction == DIRECTION_FACE_DIAG_ALT))
                                    ? FACE_DIAG_REFLECTIONS
                                    : VALID_REFLECTIONS);
                }
            };

            CUBIC_LOW.setRepresentativeDirection(DIRECTION_A, DIRECTION_C);
            CUBIC_LOW.setRepresentativeDirection(DIRECTION_B, DIRECTION_C);
            CUBIC_LOW.setSymbolAnalyzer(new CubicSymbolAnalyzer(CUBIC_LOW));
            CUBIC_LOW.setOperatorComparator(opComparator);
            CUBIC_LOW.setOperatorComparator('F', fcOpComparator);
            
            CUBIC_HIGH_SPECIAL.setRepresentativeDirection(
                    DIRECTION_A, DIRECTION_C);
            CUBIC_HIGH_SPECIAL.setRepresentativeDirection(
                    DIRECTION_B, DIRECTION_C);
            CUBIC_HIGH_SPECIAL.setRepresentativeDirection(
                    DIRECTION_FACE_DIAG, DIRECTION_FACE_DIAG_ALT);
            CUBIC_HIGH_SPECIAL.setSymbolAnalyzer(
                    new CubicSymbolAnalyzer(CUBIC_HIGH_SPECIAL));
            CUBIC_HIGH_SPECIAL.setOperatorComparator(opComparator);
            CUBIC_HIGH_SPECIAL.setOperatorComparator('F', fcOpComparator);
            
            CUBIC_HIGH.setRepresentativeDirection(DIRECTION_A, DIRECTION_C);
            CUBIC_HIGH.setRepresentativeDirection(DIRECTION_B, DIRECTION_C);
            CUBIC_HIGH.setRepresentativeDirection(
                    DIRECTION_FACE_DIAG_ALT, DIRECTION_FACE_DIAG);
            CUBIC_HIGH.setSymbolAnalyzer(new CubicSymbolAnalyzer(CUBIC_HIGH));
            CUBIC_HIGH.setOperatorComparator(opComparator);
            CUBIC_HIGH.setOperatorComparator('F', fcOpComparator);
        }


        /**
         * An array containing the symmetry direction codes for this LaueClass,
         * in the order the operators are expected to appear in a symbol
         * corresponding to this Laue class
         */
        private int[] symmetryDirections;

        /**
         * An array containing the expected orders of symmetry along the
         * directions specified by {@code symmetryDirections}
         */
        private int[] symmetryOrders;

        /**
         * A string containing the centering symbols that are valid for a
         * space group of this Laue class in the standard setting
         */
        private String standardCenters;

        /**
         * An int array, indexed by direction code, containing at each position
         * the standard symmetry direction corresponding to the
         * not-necessarilly-standard direction code index   
         */        
        private int[] directionMap = new int[] {DIRECTION_A, DIRECTION_B,
                DIRECTION_C, DIRECTION_BODY_DIAG, DIRECTION_FACE_DIAG,
                DIRECTION_FACE_DIAG_ALT};
                
        /**
         * The Comparators to use to establish the precedence order of Operators
         * by which to choose the appropriate symbol for space groups of this
         * Laue class 
         */                
        @SuppressWarnings("unchecked")
        private Comparator<Operator>[] comparators =
                new Comparator[VALID_CENTERING.length()];
                
        /**
         * The SymbolAnalyzer by which this Laue class will obtain space group
         * generators from space group symbols
         */
        private AbstractSymbolAnalyzer analyzer;

        /**
         * The MatrixManager to be used for creating and evaluating symmetry
         * matrices
         */
        private MatrixManager matrixManager;

        /**
         * Initializes a new {@code LaueClass} with the specified
         * parameters; intended only for use by this class and any subclasses
         * (even though it is also accessible to other classes in this package)
         *
         * @param  directions an {@code int[]} containing the codes for
         *         the symmetry directions characteristic of this Laue class,
         *         in the conventional sequence for Hermann-Mauguin symbols
         *         pertaining to this Laue class (see the <em>International
         *         Tables for Crystallography</em>, fourth edition, table 2.4.1)
         * @param  orders an {@code int[]} containing the order of symmetry
         *         along each of the characteristic directions that, along with
         *         the directions themselves, characterize this Laue class
         * @param  standardCenters a string containing the lattice centering
         *         codes that are compatible with this Laue class and applicable
         *         to space groups of this Laue class in their standard settings
         */
        LaueClass(int[] directions, int[] orders, String standardCenters) {

            assert (directions != null);
            assert (orders != null);
            assert (standardCenters != null);
            assert (directions.length == orders.length);

            /*
             * Initialize those members that come straight from the arguments.
             * The directions and orders arrays are not cloned because this
             * constructor is intended only for use by this class itself and
             * its subclasses, which are presumed to know what they are doing
             */
            this.symmetryDirections = directions;
            this.symmetryOrders = orders;
            this.standardCenters = standardCenters;

            /*
             * Initialize the operator comparators
             */
            setOperatorComparator(new OperatorComparator());
            
            /*
             * Configure a default SymbolAnalyzer for this Laue class
             */
            setSymbolAnalyzer(new BasicSymbolAnalyzer(this));
            
            /*
             * Configure a default MatruxManager
             */
            setMatrixManager(new MatrixManager());
        }

        /**
         * Determines whether the lattice centering represented by the specified
         * centering symbol is exhibited by any space group of this Laue class
         * in its standard setting
         *
         * @param  c the {@code char} centering symbol for the lattice
         *         centering mode of interest
         *
         * @return {@code true} if and only if at least one space group of
         *         this Laue class exhibits the specified centering in its
         *         standard setting
         */
        public boolean allowsCentering(char c) {
            return (standardCenters.indexOf(c) >= 0);
        }

        /**
         * Returns the number of characteristic symmetry directions of this
         * Laue class
         *
         * @return the number of characteristic symmetry directions of this
         *         Laue class
         */
        public int getNumDirections() {
            return symmetryDirections.length;
        }

        /**
         * Returns the symmetry direction code for the particular symmetry
         * direction specified by the provided index; this is the code for one
         * of the representative symmetry directions of this Laue class
         *
         * @param  index the index of the desired direction in this Laue class'
         *         sequence of characteristic directions
         *
         * @return the symmetry direction code for the particular symmetry
         *         direction specified by the provided index
         *
         * @throws IndexOutOfBoundsException if {@code index} is less than
         *         zero or greater than or equal to the number of characteristic
         *         directions of this Laue class
         */
        public int getSymmetryDirection(int index) {
            return symmetryDirections[index];
        }
        
        /**
         * Returns the representative member of the class of symmetry directions
         * equivalent to the specified direction relative to this Laue class
         * The representative direction may be the same as the specified
         * direction.
         *  
         * @param  dir the direction code for which the representative direction
         *         is desired; one of the DIRECTION_* constants
         * 
         * @return the direction code (one of the DIRECTION_* constants) of the
         *         representative direction for the class of equivalent
         *         directions to which {@code dir} belongs; always
         *         {@code dir} itself if {@code dir} is not flagged as
         *         a symmetry direction for this Laue class (representative or
         *         otherwise)
         */
        int getRepresentativeDirection(int dir) {
            return (dir == DIRECTION_NONE) ? DIRECTION_NONE : directionMap[dir];
        }
        
        /**
         * Specifies that the direction {@code dir} is in the class of
         * symmetry directions for which direction {@code rep} is
         * representative in this Laue class.  By default each direction is in
         * its own class, and hence is the representative of its own class.
         *  
         * @param  dir the direction code to set a representative direction for;
         *         one of the DIRECTION_* constants; should not be any of the
         *         symmetry directions set for this Laue class at construction
         *         time
         * @param  rep the direction code for the representative direction for
         *         the class containing direction {@code dir}; one of the
         *         DIRECTION_* constants; should be one of the symmetry
         *         directions set for this Laue class at construction time
         */
        void setRepresentativeDirection(int dir, int rep) {
            assert hasRepresentativeDirection(rep);
            directionMap[dir] = rep;
        }

        /**
         * Checks whether the symmetry direction indicated by the provided
         * direction code is among the representative characteristic symmetry
         * directions of this Laue class.
         *
         * @param  direction the direction code (one of the DIRECTION_*
         *         constants) to be tested
         *
         * @return {@code true} if the specified direction is among the
         *         representative symmetry directions of this Laue class,
         *         otherwise {@code false}
         */
        public boolean hasRepresentativeDirection(int direction) {
            for (int dir : symmetryDirections) {
                if (dir == direction) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns the order of symmetry expected in the specified direction
         * for members of this Laue class
         *
         * @param  direction the code for the direction of interest; one of the
         *         DIRECTION_* constants
         *
         * @return the highest order of symmetry in the specified direction
         *         that is characteristic of members of this Laue class; in
         *         particular, 1 if the specified direction is not a symmetry
         *         direction of this Laue class
         */
        public int getExpectedOrder(int direction) {
            direction = getRepresentativeDirection(direction);

            for (int i = 0; i < symmetryDirections.length; i++) {
                if (symmetryDirections[i] == direction) {
                    return symmetryOrders[i];
                }
            }
            return 1;
        }

        /**
         * Returns a {@code Comparator} defining the appropriate precedence
         * of operators to use for constructing a canoncical symbol for a space
         * group of this Laue class.  See the standardization rules for short
         * symbols in section 12.3 of the International Tables for X-ray
         * Crystallography (4th ed., 1996) for general information on the
         * the required precedence.
         *
         * @param  centering a {@code char} centering symbol for the space
         *         group whose operators are to be compared by the returned
         *         comparator
         *
         * @return a {@code Comparator} defining the precedence of
         *         {@code Operator}s for this Laue class along each
         *         symmetry direction, for use, among other things, in choosing
         *         operators for canonical symbols
         */
        public Comparator<Operator> getOperatorComparator(char centering) {
            return comparators[VALID_CENTERING.indexOf(centering)];
        }
        
        /**
         * Sets the Comparator to use for prioritizing the operators of all
         * space groups of this Laue class (except where subsequently
         * overridden for a specific centering type by
         * {@link #setOperatorComparator(char, Comparator)}).  Note that this
         * method does <em>not</em> use
         * {@code setOperatorComparator(char, Comparator)} to do its work,
         * and that it is final so that it can safely be invoked by the
         * constructor.  This method is intended for use only by this class and
         * its subclasses, and only for initializing new
         * {@code LaueClass}es.
         *  
         * @param  comp the Comparator to use; typically an
         *         {@code OperatorComparator}.  Must enforce a precedence
         *         appropriate for this Laue class; see the standardization
         *         rules for short symbols in section 12.3 of the International
         *         Tables for X-ray Crystallography (4th ed., 1996) for general
         *         information, and the individual space group listings in that
         *         reference for details
         */
        final void setOperatorComparator(Comparator<Operator> comp) {
            assert (comp != null);
            Arrays.fill(comparators, comp);
        }
        
        /**
         * Sets the Comparator to use for prioritizing the operators of all
         * space groups of this Laue class that exhibit lattice centering as
         * specified by the provided centering code.  This method is intended
         * for use only by this class and its subclasses, and only for
         * initializing new instances.
         *  
         * @param  centering the {@code char} code for the lattice
         *         centering to which the specified Comparator applies
         * 
         * @param  comp the Comparator to use; typically an
         *         {@code OperatorComparator}.  Must enforce a precedence
         *         appropriate for this Laue class; see the standardization
         *         rules for short symbols in section 12.3 of the International
         *         Tables for X-ray Crystallography (4th ed., 1996) for general
         *         information, and the individual space group listings in that
         *         reference for details
         */
        void setOperatorComparator(char centering, Comparator<Operator> comp) {
            assert (comp != null);
            comparators[VALID_CENTERING.indexOf(centering)] = comp;
        }

        /**
         * Returns the {@code SymbolAnalyzer} used by this Laue class to
         * obtain space group generators from space group symbols
         * 
         * @return this class' {@code SymbolAnalyzer}
         */
        AbstractSymbolAnalyzer getSymbolAnalyzer() {
            return analyzer;
        }

        /**
         * Sets the {@code SymbolAnalyzer} used by this Laue class to
         * obtain space group generators from space group symbols.  This method
         * is intended for use only by this class and its subclasses, and only
         * for initializing new instances; it is final so that it may safely be
         * invoked by the constructor
         * 
         * @param  analyzer the {@code SymbolAnalyzer} that should be used
         *         by this Laue class
         */        
        final void setSymbolAnalyzer(AbstractSymbolAnalyzer analyzer) {
            assert (analyzer != null);
            this.analyzer = analyzer;
        }
        
        /**
         * Returns the {@code MatrixManager} used by this Laue class to
         * create and analyze symmetry matrices
         * 
         * @return this class' {@code MatrixManager}
         */
        MatrixManager getMatrixManager() {
            return matrixManager;
        }
        
        /**
         * Sets the {@code MatrixManager} used by this Laue class to
         * create and analyze symmetry matrices.  This method is intended for
         * use only by this class and its subclasses, and only for initializing
         * new instances; it is final so that it may safely be invoked by the
         * constructor
         * 
         * @param  manager the {@code MatrixManager} that should be used
         *         by this Laue class
         */        
        final void setMatrixManager(MatrixManager manager) {
            assert (manager != null);
            matrixManager = manager;
        }

        /**
         * Determines whether the international short symbols for space groups
         * of this Laue class must symbolize rotation operations in the
         * primary symmetry direction when there are also reflections
         * corresponding to that direction.  E.g. returns {@code true}
         * for Laue classes 2/m and 4/mmm, but {@code false} for mmm and
         * -3m.
         *
         * @return {@code true} if the rotation component along the primary
         *         symmetry direction must always be symbolized in the
         *         international short symbols for space groups of this Laue
         *         class
         */
        public boolean requiresFullFirstOperator() {
            switch (getNumDirections()) {
                case 1:
                    return true;
                case 3:
                    return ((symmetryOrders[0] > 2) && (symmetryOrders[1] < 3));
                default:
                    return false;
            }
        }
        
        /**
         * Generates a {@code SpaceGroup} object corresponding to the
         * specified symbol, provided that the symbol describes a valid space
         * group of this Laue class.  It uses the configured symbol analyzer
         * object to obtain generator matrices and applies them in order, then
         * instructs the configured matrix manager to generate the centering
         * operations implied by the provided symbol, and instructs the
         * newly configured group to test its own validity.  If the validity
         * test is successful then the new group is returned; otherwise an
         * InvalidDataException is thrown.
         *
         * @param  symbol a {@code SpaceGroupSymbol} representing the space
         *         group of this Laue class for which a {@code SpaceGroup}
         *         object is desired; should be set such that the lattice
         *         centering is among the standard ones for this Laue class.
         *
         * @return a {@code SpaceGroup} corresponding to
         *         {@code symbol}
         *
         * @throws InvalidDataException with reason code ILLEGAL_SPGP if
         *         {@code symbol} does not symbolize a valid space group
         *         of this Laue class
         */
        SpaceGroup generateGroup(SpaceGroupSymbol symbol)
                throws InvalidDataException {
            if (!allowsCentering(symbol.getCentering())) {
                throw new InvalidDataException("Invalid centering: " + symbol,
                        symbol.toString(),
                        InvalidDataException.ILLEGAL_SPGP);
            }

            SpaceGroup group = new SpaceGroup(symbol, this);

            try {
                // apply generators
                for (SymmetryMatrix matrix : 
                        getSymbolAnalyzer().getGenerators(symbol)) {
                    group.applyMatrix(matrix);
                }
            } catch (IllegalArgumentException iae) {
                throw new InvalidDataException("Invalid symbol (" + 
                        iae.getMessage() + "), interpreted as: " + symbol,
                        symbol.toString(),
                        InvalidDataException.ILLEGAL_SPGP);
            }

            // add centering operations
            getMatrixManager().generateCenteringOps(group);

            /*
             * Verify that a valid group was generated.  Must be performed after
             * _all_ operations have been added to the group, including
             * centering operations.   
             */
            group.testValidity();

            // It's OK
            return group;
        }
    }

    /**
     * An {@code Enumeration} that wraps another {@code}Enumeration},
     * formats its elements according to the standard format for space group
     * operators, and drops extra elements that are not valid space group parts
     * (e.g. a choice of origin comment or a null element).  Transforms
     * operators from the inner enumeration by removing parentheses and
     * converting all letters to lower case.  Regardless of the content of the
     * underlying enumeration, instances of this class will never return more
     * than three elements.
     */
    static class OperatorSequence implements Enumeration<String> {

        /**
         * The source of raw operator symbols for this enumeration
         */
        private Enumeration<?> rawSymbols;

        /**
         * Temporary storage for the next element of this enumeration;
         * populated by hasMoreElements() and cleared by nextElement()
         */
        private String nextElement = null;

        /**
         * A count of the number of elements (operators) returned by this
         * enumeration to date
         */
        private int operatorCount = 0;

        /**
         * A flag to signal that this Enumeration has reached its end (even if
         * the underlying one has not)
         */
        private boolean hasMore;

        /**
         * Constructs a new OperatorSequence around the specified Enumeration
         *
         * @param  e the Enumeration to be wrapped by this OperatorSequence
         */
        public OperatorSequence(Enumeration<?> e) {
            rawSymbols = e;
            hasMore = rawSymbols.hasMoreElements();
        }

        /**
         * Returns the next element from this enumeration, or throws
         * {@code NoSuchElementException} if there is none
         *
         * @return the next {@code Object} from this enumeration
         *
         * @throws NoSuchElementException if there are no more elements in this
         *         enumeration, which is to say if hasMoreElements() would
         *         return {@code false}
         */
        public String nextElement() {
            
            /*
             * hasMoreElements() has the additional effect of determining just
             * what the next element _is_ if there is one 
             */
            if (!hasMoreElements()) {
                throw new NoSuchElementException("No more operators");
            } else {
                String rval = nextElement;  // populated by hasMoreElements()

                nextElement = null;
                operatorCount++;

                return rval;
            }
        }

        /**
         * Determines whether this enumeration has any more elements.  May cause
         * an element to be read from the underlying enumeration.
         *
         * @return {@code true} if this enumeration contains any more
         *         elements; {@code false} otherwise
         */
        public boolean hasMoreElements() {

            /*
             * If the end was not previously reached then check for it now.
             * Retrieves and stores the next element (if necessary).
             */
            if (hasMore) {
                if (operatorCount >= 3) {
                    hasMore = false;
                } else {
                    if ((nextElement == null)
                            && (rawSymbols.hasMoreElements())) {
                        nextElement =
                                parseSymbol(rawSymbols.nextElement());
                    }

                    hasMore = (nextElement != null);
                }
            }

            return hasMore;
        }

        /**
         * Removes parentheses from the symbol, converts letters to lower case,
         * and performs a simple validity check on the initial character of the
         * supplied string; returns a formatted result if one can be obtained,
         * otherwise null.
         *
         * @param  o an object whose string representation is the symbol to
         *         parse (typically already a {@code String} itself)
         *
         * @return a {@code String} containing a parsed version of the
         *         symbol, or {@code null} if the symbol could not be
         *         parsed
         */
        private String parseSymbol(Object o) {
            if (o == null) {
                return null;
            } else {
                String sym = o.toString().toLowerCase();

                /*
                 * Unicode character number 0x2212 is a mathematical minus sign
                 */
                sym = sym.replaceAll("[()]", "").replaceAll("\u2212", "-");

                return (sym.matches("[-123456abcdmn].*") ? sym : null);
            }
        }
    }

    /**
     * An {@code Enumeration} implementation that enumerates the symmetry
     * operators from the operator portion of a ShelX-format space group symbol
     * (i.e. the part after the lattice type symbol).  If it encounters a symbol
     * subsequence that it cannot otherwise parse, it returns the entire tail
     * of the input as the next (and last) element
     */
    static class ShelxOperatorEnumeration implements Enumeration<String> {

        /**
         * A Pattern for ShelX-format operators.  This pattern is quite lenient
         * in that it will accept a variety of symbols that do not represent
         * valid symmetry operations.  It must, however, be certain to
         * effectively seperate operators (and not combine them in a single
         * token), which can be tricky because ShelX-format symbols are written
         * without delimiters between operators.
         */
        /*
         * Either
         * (1) optional '-', digit, optional '(' digits ')', optional ('/' letter)
         * or
         * (2) letter
         * with any amount of trailing, non-captured text
         */
        private final static Pattern operatorPattern =
            Pattern.compile(
                    "( (?: -? \\d (?: \\(\\d+\\) )? (?: /[A-Za-z] )? ) | [A-Za-z] ) .*",
                    Pattern.COMMENTS);

        /**
         * The ShelX-format symmetry operator sequence from which this
         * Enumerator returns operators
         */
        private String symbol;

        /**
         * The current parse position in the symbol
         */
        private int position = 0;

        /**
         * A holding spot for the next element of this Enumeration
         */
        private String nextElement = null;

        /**
         * Initializes a new ShelxOperatorEnumerator
         *
         * @param  s a String containing symmetry operators in the format
         *         used in ShelX space group symbols.
         */
        public ShelxOperatorEnumeration(String s) {
            if (s == null) {
                throw new NullPointerException("Null SHELX symbol");
            }
            symbol = s;
        }

        /**
         * Returns the next element of this Enumeration: the next symmetry
         * operator or the tail of the operator sequence if it cannot otherwise
         * be parsed
         *
         * @return the next element of this Enumeration
         */
        public String nextElement() {
            if (!hasMoreElements()) {
                throw new NoSuchElementException("No more operators");
            } else {
                String rval = nextElement;

                nextElement = null;
                return rval;
            }
        }

        /**
         * Determines whether this Enumeration contains any more elements
         *
         * @return true if this Enumeration contains at least one more element
         */
        public boolean hasMoreElements() {
            if ((nextElement == null) && (position < symbol.length())) {
                String tail = symbol.substring(position);
                Matcher matcher = operatorPattern.matcher(tail);

                if (matcher.matches()) {
                    nextElement = matcher.group(1);
                    position += matcher.end(1);
                } else {
                    nextElement = tail;
                    position = symbol.length();
                }
            }

            return (nextElement != null);
        }
    }
}
