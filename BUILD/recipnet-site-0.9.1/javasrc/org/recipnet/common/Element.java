/*
 * Reciprocal Net Project
 * 
 * ElementsBL.java
 *
 * 03-Sep-2002: jobollin extracted this enum from ElementsBL
 */

package org.recipnet.common;

import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An Enumeration of the chemical elements from atomic number 1 through atomic
 * number 103; each member has a chemical symbol, atomic number, and atomic
 * mass, and this class provides means to look up members by atomic number, by
 * element symbol, or by element name.
 * 
 * @author jobollin
 * @version 0.9.0
 */
public enum Element {

    /** The Element constant for Hydrogen */
    HYDROGEN("H", 1, 1.00794d),

    /** The Element constant for Helium */
    HELIUM("He", 2, 4.002602d),

    /** The Element constant for Lithium */
    LITHIUM("Li", 3, 6.941d),

    /** The Element constant for Berylium */
    BERYLIUM("Be", 4, 9.012182d),

    /** The Element constant for Boron */
    BORON("B", 5, 10.811d),

    /** The Element constant for Carbon */
    CARBON("C", 6, 12.011d),

    /** The Element constant for Nitrogen */
    NITROGEN("N", 7, 14.00674d),

    /** The Element constant for Oxygen */
    OXYGEN("O", 8, 15.9994d),

    /** The Element constant for Fluorine */
    FLUORINE("F", 9, 18.9984032d),

    /** The Element constant for Neon */
    NEON("Ne", 10, 20.1797d),

    /** The Element constant for Sodium */
    SODIUM("Na", 11, 22.989768d),

    /** The Element constant for Magnesium */
    MAGNESIUM("Mg", 12, 24.3050d),

    /** The Element constant for Aluminum */
    ALUMINUM("Al", 13, 26.981539d),

    /** The Element constant for Silicon */
    SILICON("Si", 14, 28.0855d),

    /** The Element constant for Phosphorous */
    PHOSPHOROUS("P", 15, 30.973762d),

    /** The Element constant for Sulfur */
    SULFUR("S", 16, 32.066d),

    /** The Element constant for Chlorine */
    CHLORINE("Cl", 17, 35.4527d),

    /** The Element constant for Argon */
    ARGON("Ar", 18, 39.948d),

    /** The Element constant for Potassium */
    POTASSIUM("K", 19, 39.0983d),

    /** The Element constant for Calcium */
    CALCIUM("Ca", 20, 40.078d),

    /** The Element constant for Scandium */
    SCANDIUM("Sc", 21, 44.955910d),

    /** The Element constant for Titanium */
    TITANIUM("Ti", 22, 47.88d),

    /** The Element constant for Vanadium */
    VANADIUM("V", 23, 50.9415d),

    /** The Element constant for Chromium */
    CHROMIUM("Cr", 24, 51.9961d),

    /** The Element constant for Manganese */
    MANGANESE("Mn", 25, 54.93805d),

    /** The Element constant for Iron */
    IRON("Fe", 26, 55.847d),

    /** The Element constant for Cobalt */
    COBALT("Co", 27, 58.93320d),

    /** The Element constant for Nickel */
    NICKEL("Ni", 28, 58.6934d),

    /** The Element constant for Copper */
    COPPER("Cu", 29, 63.546d),

    /** The Element constant for Zinc */
    ZINC("Zn", 30, 65.39d),

    /** The Element constant for Gallium */
    GALLIUM("Ga", 31, 69.723d),

    /** The Element constant for Germanium */
    GERMANIUM("Ge", 32, 72.61d),

    /** The Element constant for Arsenic */
    ARSENIC("As", 33, 74.92159d),

    /** The Element constant for Selenium */
    SELENIUM("Se", 34, 78.96d),

    /** The Element constant for Bromine */
    BROMINE("Br", 35, 79.904d),

    /** The Element constant for Krypton */
    KRYPTON("Kr", 36, 83.80d),

    /** The Element constant for Rubidium */
    RUBIDIUM("Rb", 37, 85.4678d),

    /** The Element constant for Strontium */
    STRONTIUM("Sr", 38, 87.62d),

    /** The Element constant for Yttrium */
    YTTRIUM("Y", 39, 88.90585d),

    /** The Element constant for Zirconium */
    ZIRCONIUM("Zr", 40, 91.224d),

    /** The Element constant for Niobium */
    NIOBIUM("Nb", 41, 92.90638d),

    /** The Element constant for Molybdenum */
    MOLYBDENUM("Mo", 42, 95.94d),

    /** The Element constant for Technetium */
    TECHNETIUM("Tc", 43, 97d),

    /** The Element constant for Ruthenium */
    RUTHENIUM("Ru", 44, 101.07d),

    /** The Element constant for Rhodium */
    RHODIUM("Rh", 45, 102.90550d),

    /** The Element constant for Palladium */
    PALLADIUM("Pd", 46, 106.42d),

    /** The Element constant for Silver */
    SILVER("Ag", 47, 107.8682d),

    /** The Element constant for Cadmium */
    CADMIUM("Cd", 48, 112.411d),

    /** The Element constant for Indium */
    INDIUM("In", 49, 114.818d),

    /** The Element constant for Tin */
    TIN("Sn", 50, 118.710d),

    /** The Element constant for Antimony */
    ANTIMONY("Sb", 51, 121.757d),

    /** The Element constant for Tellurium */
    TELLURIUM("Te", 52, 127.60d),

    /** The Element constant for Iodine */
    IODINE("I", 53, 126.90447d),

    /** The Element constant for Xenon */
    XENON("Xe", 54, 131.29d),

    /** The Element constant for Cesium */
    CESIUM("Cs", 55, 132.90543d),

    /** The Element constant for Barium */
    BARIUM("Ba", 56, 137.327d),

    /** The Element constant for Lanthanum */
    LANTHANUM("La", 57, 138.9055d),

    /** The Element constant for Cerium */
    CERIUM("Ce", 58, 140.115d),

    /** The Element constant for Praeseodymium */
    PRASEODYMIUM("Pr", 59, 140.90765d),

    /** The Element constant for Neodymium */
    NEODYMIUM("Nd", 60, 144.24d),

    /** The Element constant for Promethium */
    PROMETHIUM("Pm", 61, 145d),

    /** The Element constant for Samarium */
    SAMARIUM("Sm", 62, 150.36d),

    /** The Element constant for Europium */
    EUROPIUM("Eu", 63, 151.965d),

    /** The Element constant for Gadolinium */
    GADOLINIUM("Gd", 64, 157.25d),

    /** The Element constant for Terbium */
    TERBIUM("Tb", 65, 158.92534d),

    /** The Element constant for Dysprosium */
    DYSPROSIUM("Dy", 66, 162.50d),

    /** The Element constant for Holmium */
    HOLMIUM("Ho", 67, 164.93032d),

    /** The Element constant for Erbium */
    ERBIUM("Er", 68, 167.26d),

    /** The Element constant for Thulium */
    THULIUM("Tm", 69, 168.93421d),

    /** The Element constant for Ytterbium */
    YTTERBIUM("Yb", 70, 173.04d),

    /** The Element constant for Lutetium */
    LUTETIUM("Lu", 71, 174.967d),

    /** The Element constant for Halfnium */
    HALFNIUM("Hf", 72, 178.49d),

    /** The Element constant for Tantalum */
    TANTALUM("Ta", 73, 180.9479d),

    /** The Element constant for Tungsten */
    TUNGSTEN("W", 74, 183.84d),

    /** The Element constant for Rhenium */
    RHENIUM("Re", 75, 186.207d),

    /** The Element constant for Osmium */
    OSMIUM("Os", 76, 190.23d),

    /** The Element constant for Iridium */
    IRIDIUM("Ir", 77, 192.22d),

    /** The Element constant for Platinum */
    PLATINUM("Pt", 78, 195.08d),

    /** The Element constant for Gold */
    GOLD("Au", 79, 196.96654d),

    /** The Element constant for Mercury */
    MERCURY("Hg", 80, 200.59d),

    /** The Element constant for Thalium */
    THALIUM("Tl", 81, 204.3833d),

    /** The Element constant for Lead */
    LEAD("Pb", 82, 207.2d),

    /** The Element constant for Bismuth */
    BISMUTH("Bi", 83, 208.98037d),

    /** The Element constant for Polonium */
    POLONIUM("Po", 84, 209d),

    /** The Element constant for Astatine */
    ASTATINE("At", 85, 210d),

    /** The Element constant for Radon */
    RADON("Rn", 86, 222d),

    /** The Element constant for Francium */
    FRANCIUM("Fr", 87, 223d),

    /** The Element constant for Radium */
    RADIUM("Ra", 88, 226.0254d),

    /** The Element constant for Actinium */
    ACTINIUM("Ac", 89, 227.0278d),

    /** The Element constant for Thorium */
    THORIUM("Th", 90, 232.0381d),

    /** The Element constant for Protactinium */
    PROTACTINIUM("Pa", 91, 231.03588d),

    /** The Element constant for Uranium */
    URANIUM("U", 92, 238.0289d),

    /** The Element constant for Neptunium */
    NEPTUNIUM("Np", 93, 237.0482d),

    /** The Element constant for Plutonium */
    PLUTONIUM("Pu", 94, 244d),

    /** The Element constant for Americium */
    AMERICIUM("Am", 95, 243d),

    /** The Element constant for Curium */
    CURIUM("Cm", 96, 247d),

    /** The Element constant for Berkelium */
    BERKELIUM("Bk", 97, 247d),

    /** The Element constant for Californium */
    CALIFORNIUM("Cf", 98, 251d),

    /** The Element constant for Einsteinium */
    EINSTEINIUM("Es", 99, 254d),

    /** The Element constant for Fermium */
    FERMIUM("Fm", 100, 257d),

    /** The Element constant for Mendelevium */
    MENDELEVIUM("Md", 101, 258d),

    /** The Element constant for Nobelium */
    NOBELIUM("No", 102, 259d),

    /** The Element constant for Lawrencium */
    LAWRENCIUM("Lw", 103, 260d);

    /**
     * A map from chemical symbol to {@code Element}, for use in looking up
     * {@code Element}s by chemical symbol
     */
    private static Map<String, Element> symbolMap = null;

    /**
     * This element's chemical symbol
     */
    private final String chemicalSymbol;

    /**
     * This element's atomic number
     */
    private final int atomicNumber;

    /**
     * This element's atomic mass
     */
    private final double atomicMass;

    /**
     * Initializes a {@code Element} with the specified parameters
     * 
     * @param symbol the element's chemical symbol
     * @param number the element's atomic number
     * @param weight the element's atomic mass
     */
    private Element(String symbol, int number, double weight) {
        chemicalSymbol = symbol;
        atomicNumber = number;
        atomicMass = weight;
    }

    /**
     * Returns this {@code Element}'s chemical symbol
     * 
     * @return this {@code Element}'s chemical symbol as a {@code String}
     */
    public String getSymbol() {
        return chemicalSymbol;
    }

    /**
     * Returns this {@code Element}'s atomic number
     * 
     * @return this {@code Element}'s atomic number
     */
    public int getAtomicNumber() {
        return atomicNumber;
    }

    /**
     * Returns this {@code Element}'s atomic mass
     * 
     * @return the mass of one atom of this {@code Element} in atomic mass
     *         units, as a {@code double}. Equivalently, the mass of one mole
     *         of atoms of this element in grams
     */
    public double getAtomicMass() {
        return atomicMass;
    }

    /**
     * Returns the Element having the specified atomic number, if it is one of
     * those in this enumeration
     * 
     * @param number the atomic number of the desired {@code Element}
     * @return the {@code Element} having the specified atomic number, or
     *         {@code null} if no such element is contained in this enumeration
     */
    public static Element forAtomicNumber(int number) {
        try {
            return values()[number - 1];
        } catch (IndexOutOfBoundsException ioobe) {
            return null;
        }
    }

    /**
     * Determines whether the specified chemical element symbol is known to the
     * enumeration
     * 
     * @param symbol the chemical symbol of interest as a one- or two-letter
     *            {@code String}, with the first letter upper case and the
     *            second, if present, lower case
     * @return {@code true} if the symbol is supported by this enumeration, in
     *         which case {@link #forSymbol(String)} will return a
     *         non-{@code null}
     *         value when passed {@code symbol} as its argument
     */
    public static boolean isKnownSymbol(String symbol) {
        return getSymbolMap().containsKey(symbol);
    }

    /**
     * Returns the {@code Element} corresponding to the specified chemical
     * symbol, if it exists in this enumeration
     * 
     * @param symbol the chemical symbol of the desired element as a one- or
     *            two-letter {@code String}, with the first letter upper case
     *            and the second, if present, lower case
     * @return the {@code Element} corresponding to the specified chemical
     *         symbol, if it exists in this enumeration; otherwise {@code null}
     */
    public static Element forSymbol(String symbol) {
        return getSymbolMap().get(symbol);
    }

    /**
     * Returns the {@code Element} having the specified name
     * 
     * @param name the full name of the desired element as a {@code String}
     *        (<i>e.g.</i> "Carbon"); not case-sensitive
     * @return the {@code Element} having the specified name, if it exists in
     *         this enumeration; otherwise {@code null}
     */
    public static Element forName(String name) {
        try {
            return Enum.valueOf(Element.class, name.toUpperCase());
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    /**
     * Returns the element symbol map, creating it first if necessary
     * 
     * @return a {@code Map<String, Element>} from chemical element symbols
     *         to the corresponding {@code Element}
     */
    private static Map<String, Element> getSymbolMap() {

        // Create an index for the next time, if necessary
        if (symbolMap == null) {
            Map<String, Element> tempMap = new HashMap<String, Element>();

            for (Element e : values()) {
                tempMap.put(e.getSymbol(), e);
            }
            symbolMap = tempMap;
        }

        // Return the Element as determined from the index
        return symbolMap;

    }

    /**
     * Returns an unmodifiable {@code List} of the chemical symbols of all the
     * members of this enumeration
     * 
     * @return a {@code List<String>} of the chemical symbols of all the members
     *         of this enumeration, in atomic number order
     */
    public static List<String> getAllSymbols() {
        return new AbstractList<String>() {
            Element[] elements = Element.values();

            @Override
            public int size() {
                return elements.length;
            }

            @Override
            public String get(int i) {
                return elements[i].getSymbol();
            }
        };
    }
}
