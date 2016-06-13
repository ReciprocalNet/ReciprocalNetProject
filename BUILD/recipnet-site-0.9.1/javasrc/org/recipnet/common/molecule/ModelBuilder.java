/*
 * Reciprocal Net Project
 *
 * ModelBuilder.java
 *
 * 09-Nov-2005: jobollin wrote first draft
 * 01-Jan-2006: jobollin converted the argument to buildGrownModel() from
 *              List<String> to Collection<String>
 * 07-Mar-2006: jobollin added atomComparator and code in buildModel() to use it
 * 20-Apr-2006: jobollin fixed the default atom comparator
 * 02-Nov-2006: jobollin added support for associating atomic displacement
 *              information with the models created by this builder
 */

package org.recipnet.common.molecule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.recipnet.common.Element;
import org.recipnet.common.IteratorFilter;
import org.recipnet.common.SymmetryMatrix;
import org.recipnet.common.files.CifFile.CifValue;
import org.recipnet.common.files.CifFile.DataBlock;
import org.recipnet.common.files.cif.AtomSiteAnisoIterator;
import org.recipnet.common.files.cif.AtomSiteAnisoRecord;
import org.recipnet.common.files.cif.AtomSiteIterator;
import org.recipnet.common.files.cif.AtomSiteRecord;
import org.recipnet.common.files.cif.CifFileUtil;
import org.recipnet.common.files.cif.GeomBondIterator;
import org.recipnet.common.files.cif.GeomBondRecord;
import org.recipnet.common.geometry.CoordinateSystem;

/**
 * A factory class for {@code MolecularModel} objects, offering various model
 * construction approaches, several of which take crystallographic symmetry into
 * account.  Instances may be held and re-used multiple times for the same
 * underlying crystallographic data, including for the purpose of creating
 * multiple distinct (and independant) molecular models via different of the
 * available model building recipes.
 * 
 * @author jobollin
 * @version 0.9.0
 * 
 * @see MolecularModel
 */
public class ModelBuilder {
    
    /**
     * A {@code Pattern} used in parsing CIF-style symmetry operation
     * expressions; matches a single additive term of one coordinate of such
     * an expression; sign and magnitude are captured in two seperate groups   
     */
    private final static Pattern TERM_PATTERN
            = Pattern.compile("([-+]?)([^-+]+)");
    
    /**
     * A tolerance factor used in parsing CIF-style symmetry expressions; it is
     * used to determine whether a decimal number is close enough to a rational
     * fraction that it can be successfully interpreted as part of the
     * expression  
     */
    private final static float FRACTION_TOLERANCE = 0.0121f;
    
    /**
     * A {@code Pattern} used in parsing CIF atom site records to help determine
     * the chemical element that applies to each site; matches strings of one
     * or two Roman letters, capturing each in its own group.  (The second group
     * will be empty (not null) for one-character matches.
     */
    private final static Pattern ELEMENT_SYMBOL_PATTERN
            = Pattern.compile("([A-Za-z])([A-Za-z]?)");
    
    /**
     * A map from coordinate symbols (x/y/z) to corresponding {@code Integer}
     * indices; used internally to simplify the code for parsing CIF-style
     * symmetry operation expressions.
     */
    private final static Map<String, Integer> COORDINATE_INDICES;
    
    static {
        Map<String, Integer> tempMap = new HashMap<String, Integer>();
        
        tempMap.put("x", 0);
        tempMap.put("y", 1);
        tempMap.put("z", 2);
        
        COORDINATE_INDICES = Collections.unmodifiableMap(tempMap);
    }
    
    /**
     * A tolerance factor for use in judging whether two equivalent positions 
     * are in fact the <em>same</em> position for a particular atomic site
     * (which would by definition be a special position if that were the case) 
     */
    private final static double ATOM_DSQR_TOLERANCE = 0.01;
    
    /**
     * A scale factor used in adjusting the boundaries of cell packing plots to
     * ensure that atoms on the boundaries are not wrongly excluded
     */
    private final static double CELL_EXTENT_FACTOR = 1.01;
    
    /**
     * A comparator defining the default sequence in which the atoms of a model
     * built by an instance of this class are arranged; the default sequence is
     * descending by atomic number, and ascending by site tag within the same
     * atomic number
     */
    public final static Comparator<FractionalAtom> DEFAULT_ATOM_ORDER
            = new Comparator<FractionalAtom>() {

        public int compare(FractionalAtom atom1, FractionalAtom atom2) {
            Element e1 = atom1.getElement();
            Element e2 = atom2.getElement();
            
            if (e1 != e2) {
                return e2.getAtomicNumber() - e1.getAtomicNumber();
            } else {
                return atom1.getSiteTag().compareTo(atom2.getSiteTag());
            }
        }
    };
    
    /**
     * The symmetry context with which this builder will perform symmetry
     * computations
     */
    private final SymmetryContext symmetry;
    
    /**
     * The unit cell to which the underlying data of this builder are referred
     */
    private final CoordinateSystem cell;
    
    /**
     * An {@code AtomicMotionFactory} appropriate for use with the base model
     * for which this builder is configured
     */
    private final AtomicMotionFactory motionFactory;
    
    /**
     * A map from site labels to corresponding site details for the underlying
     * data of this builder
     */
    private final Map<String, AtomSiteRecord> atomRecordMap;

    /**
     * a map from site labels to corresponding atomic motion details for the
     * underlying data of this builder
     */
    private final Map<String, AtomicMotion> atomicMotionMap;
    
    /**
     * A connection table containing the bond information derived from the
     * supplied data; the keys are site labels, and the values are lists of the
     * site codes for connected sites.  The relation represented by this table
     * is symmetric. 
     */
    private final Map<String, List<SiteCode>> bondDetailsMap
            = new HashMap<String, List<SiteCode>>();
    
    /**
     * A {@code Comparator} establishing the sequence in which atoms will appear
     * in models constructed by this builder
     */
    private Comparator<FractionalAtom> atomComparator
            = DEFAULT_ATOM_ORDER;
    
    /*
     * TODO: other means of ModelBuilder initialization, e.g. SDT or RES with
     * auto-computation of bonds, CRT, PDB   
     */
    
    /**
     * Intializes a {@code ModelBuilder} based on information from the specified
     * CIF data block.  Unit cell, symmetry, atom site, and bond information are
     * read from the block
     * 
     * @param  block the {@code DataBlock} from which this model builder should
     *         initialize itself; should not be {@code null} 
     */
    public ModelBuilder(DataBlock block) {
        
        /*
         * An iterator over the bond records from the CIF; filters out any bonds
         * that don't refer to atoms that this builder has loaded (before the
         * iteration)
         */
        Iterator<GeomBondRecord> bondIterator
                = new IteratorFilter<GeomBondRecord>(
                            new GeomBondIterator(block)) {

            @Override
            public boolean shouldPass(GeomBondRecord bond) {
                return (atomRecordMap.containsKey(bond.getSiteLabel1())
                        && atomRecordMap.containsKey(bond.getSiteLabel2()));
            }
        };
        
        cell = getUnitCell(block);
        if (cell == null) {
            throw new IllegalArgumentException("No (or incomplete) unit cell "
                    + "information found in the data block");
        }
        
        motionFactory = new AtomicMotionFactory(cell);
        symmetry = new SymmetryContext(getSymmetryOperations(block));
        atomRecordMap = getValidAtoms(block);
        atomicMotionMap = getAtomicMotions(block, motionFactory);
        
        // read the bonds
        for (; bondIterator.hasNext(); ) {
            addBondRecords(bondIterator.next());
        }
    }
    
    /**
     * Obtains the atom comparator currently configured on this model builder.
     *  
     * @return the {@code Comparator<FractionalAtom>} that will be used by this
     *         model builder to order the atoms within any model it builds; will
     *         not be {@code null}
     */
    public Comparator<FractionalAtom> getAtomComparator() {
        return atomComparator;
    }

    /**
     * Sets the atom comparator that will be used by this model builder to order
     * the atoms within any model it builds.  (Atoms are arranged in ascending
     * order according to the specified comparator.)
     * 
     * @param  atomComparator the {@code Comparator<FractionalAtom>} with which
     *         to order the atoms
     */
    public void setAtomComparator(Comparator<FractionalAtom> atomComparator) {
        if (atomComparator == null) {
            throw new NullPointerException("Null comparator");
        } else {
            this.atomComparator = atomComparator;
        }
    }

    /**
     * Generates a {@code MolecularModel<FractionalAtom, Bond>} representing the
     * atoms present in a CIF data block.  No symmetry operations are taken into
     * account in determining the atoms that should appear in the model, but
     * symmetry codes in the bond list are still interpreted to determine which
     * bonds should be included.
     * 
     * @return the resulting {@code MolecularModel<FractionalAtom, Bond>}, or
     *         {@code null} if the data block does not contain enough
     *         information to create one (the most likely cause being
     *         missing or incomplete unit cell parameters)
     */
    public MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            buildSimpleModel() {
        Map<SiteCode, List<SiteCode>> connections
                = new HashMap<SiteCode, List<SiteCode>>();
        Set<String> siteLabels = new HashSet<String>(atomRecordMap.keySet());
        
        // Add the bonds to the connection table
        for (Map.Entry<String, List<SiteCode>> connectionEntry :
                bondDetailsMap.entrySet()) {
            SiteCode source = new SiteCode(connectionEntry.getKey(),
                    symmetry.getIdentityCode());
            List<SiteCode> connectedSites = new ArrayList<SiteCode>();
            
            for (SiteCode target : connectionEntry.getValue()) {
                if (target.getSymmetryCode().equals(
                        symmetry.getIdentityCode())) {
                    connectedSites.add(target);
                }
            }
            
            connections.put(source, connectedSites);
            siteLabels.remove(connectionEntry.getKey());
        }
        
        // Add non-bonded atoms to the connection table
        for (String label : siteLabels) {
            connections.put(new SiteCode(label, symmetry.getIdentityCode()),
                    Collections.<SiteCode>emptyList());
        }
        
        // Construct and return the MolecularModel
        return buildModel(connections);
    }
    
    /**
     * Generates a molecular model of the complete moiety (moieties) containing
     * the specified "seed" atoms; bond information held by this model builder
     * is used to expand the seeds into complete moieties.  Extended structures
     * are expanded out to one full unit cell span in any direction, but
     * terminated there.
     * 
     * @param  seedAtomLabels a {@code Collection<String>} of the labels of the
     *         atom sites from which to obtain the moieties for the resulting
     *         model
     *         
     * @return a {@code MolecularModel<FractionalAtom, Bond>} containing the
     *         specified moieties
     */
    public MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            buildGrownModel(Collection<String> seedAtomLabels) {

        // A queue of the site codes to traverse in the search
        LinkedHashSet<SiteCode> sitesToVisit = new LinkedHashSet<SiteCode>();
        
        // A set of the sites already visited, to avoid loops during the search
        Set<SiteCode> visitedSites = new HashSet<SiteCode>();
        
        /*
         * For each atom site label, the SymmetryCodes of the visited sites
         * having that label
         */ 
        Map<String, List<SymmetryCode>> labelSymmetryMap
                = new HashMap<String, List<SymmetryCode>>();
        
        // A connection table containing the connections discovered by searching
        Map<SiteCode, List<SiteCode>> connections
                = new HashMap<SiteCode, List<SiteCode>>();
        
        // Verify that sufficient symmetry information is available
        if (!symmetry.isComplete()) {
            return null;
        }

        // The specified seed labels must all be valid
        if (!atomRecordMap.keySet().containsAll(seedAtomLabels)) {
            throw new IllegalArgumentException(
                    "One or more seed atom labels do not correspond to valid "
                    + "atoms in the specified CIF data block");
        }

        /*
         * Discover the complete bond graph by breadth-first search
         */
        
        // Initialize the search
        for (String label : seedAtomLabels) {
            sitesToVisit.add(new SiteCode(label, symmetry.getIdentityCode()));
        }
        
        // Perform the search
        visit_sites:
        while (!sitesToVisit.isEmpty()) {
            
            // The next site to traverse in the search
            SiteCode site = sitesToVisit.iterator().next();
            List<SiteCode> baseConnectedSites;
            List<SiteCode> connectedSites;
            List<SymmetryCode> relatedSites;
            SymmetryCode thisSymmcode;
            
            // Remove the current site from the queue
            sitesToVisit.remove(site);
            
            // Avoid looping
            if (!visitedSites.add(site)) {
                
                // This site was visited previously
                continue visit_sites;
            }
            
            thisSymmcode = site.getSymmetryCode();
            
            /*
             * Avoid polymeric extension by requiring that all represented
             * symmetry codes for each site label have distinct operation
             * indices
             */
            relatedSites = labelSymmetryMap.get(site.getSiteLabel());
            if (relatedSites == null) {
                relatedSites = new ArrayList<SymmetryCode>();
                labelSymmetryMap.put(site.getSiteLabel(), relatedSites);
            } else {
                for (SymmetryCode symmcode : relatedSites) {
                    if (symmcode.getOperationIndex()
                            == thisSymmcode.getOperationIndex()) {
                        
                        // A site related by pure unit cell translations
                        continue visit_sites;
                    }
                }
            }
            relatedSites.add(thisSymmcode);
            
            /*
             * Add connections 
             */
            
            // Get or create the connected site list for this site
            connectedSites = connections.get(site);
            if (connectedSites == null) {
                connectedSites = new ArrayList<SiteCode>();
                connections.put(site, connectedSites);
            }
            
            // Get the connected site list for this site's "base" site
            baseConnectedSites = bondDetailsMap.get(site.getSiteLabel());
            if (baseConnectedSites != null) {
                
                /*
                 * Add connections based on the base site's connections and on
                 * this site's symmetry code
                 */
                if (symmetry.getIdentityCode().equals(thisSymmcode)) {
                        connectedSites.addAll(baseConnectedSites);
                } else {
                    // Connections to symmetry-generated sites are required
                    SymmetryMatrix matrix
                            = symmetry.getSymmetryMatrix(thisSymmcode);
                    
                    for (SiteCode target : baseConnectedSites) {
                        
                        /*
                         * Note: there might be a significant gain available in
                         * by caching the result of this symmetry composition: 
                         */
                        SymmetryMatrix baseMatrix = symmetry.getSymmetryMatrix(
                                target.getSymmetryCode());
                        SymmetryCode newCode = symmetry.getSymmetryCode(
                                matrix.times(baseMatrix, false));
                        
                        connectedSites.add(
                                new SiteCode(target.getSiteLabel(), newCode));
                    }
                }
            }
            
            // Extend the search to the connected sites just discovered
            sitesToVisit.addAll(connectedSites);
        }
        
        // Create and return the CrtFile
        return buildModel(connections);
    }

    /**
     * Generates a molecular model consisting of all the atomic sites and bonds
     * in a triclinic box centered at the site identified by the specified
     * label and having the specified dimensions 
     *  
     * @param  cellExtents a {@code double[]} of length 3, containing the size
     *         of the packing box as a number of unit cells in each
     *         crystallographic axis direction; each element must be greater
     *         than zero and less than 7
     * @param  atomLabelCenter the label for the atom site around which the box
     *         should be centered
     *         
     * @return a {@code MolecularModel<FractionalAtom, Bond>} representing the
     *         contents of the specified box
     */
    public MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            buildPackedModel(double[] cellExtents, String atomLabelCenter) {
        if (! atomRecordMap.containsKey(atomLabelCenter)) {
            throw new IllegalArgumentException("Unknown atom label: "
                    + atomLabelCenter);
        }
        
        return buildPackedModel(cellExtents, atomForRecord(
                atomRecordMap.get(atomLabelCenter)).getFractionalCoordinates());
    }

    /**
     * Generates a molecular model consisting of all the atomic sites and bonds
     * in a triclinic box centered at the specified fractional coordinates and
     * having the specified dimensions 
     *  
     * @param  cellExtents a {@code double[]} of length 3, containing the size
     *         of the packing box as a number of unit cells in each
     *         crystallographic axis direction; each element must be greater
     *         than zero and less than 7
     * @param  fractionalCenter a {@code double[]} of length 3, containing
     *         the fractional coordinates at which the box should be centered
     *         
     * @return a {@code MolecularModel<FractionalAtom, Bond>} representing the
     *         contents of the specified box
     */
    public MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            buildPackedModel(double[] cellExtents, double[] fractionalCenter) {
        
        // A list of the codes for atom sites included in the specified volume
        List<SiteCode> includedSiteCodes = new ArrayList<SiteCode>();

        // A connection table containing all the discovered connections
        Map<SiteCode, List<SiteCode>> connections
                = new HashMap<SiteCode, List<SiteCode>>();

        /*
         * The fractional coordinates of the lower, left, back corner of the
         * packing box
         */
        double[] llbCorner = new double[3];

        /*
         * The fractional coordinates of the upper, right, front corner of the
         * packing box
         */
        double[] urfCorner = new double[3];

        // Verify that sufficient symmetry information is available
        if (!symmetry.isComplete()) {
            return null;
        }

        // Check array lengths
        if ((cellExtents.length != 3) || (fractionalCenter.length != 3)){
            throw new IllegalArgumentException(
                    "three cell extents and three center coordinates required");
        }
        
        // Check the extents (up to seven cells in each dimension are supported
        for (double d : cellExtents) {
            if ((d <= 0.0) || (d >= 7.0)) {
                throw new IllegalArgumentException(
                        "Invalid number of cells requested: " + d);
            }
        }
        
        // Check the center
        for (double d : fractionalCenter) {
            if ((d < 0.0) || (d > 1.0)) {
                throw new IllegalArgumentException(
                        "The center must be in the unit cell");
            }
        }
        
        /*
         * Expand the cell extents to fudge for atoms exactly on the boundary,
         * and halve the result to yield distances from (new) boundary to center
         */
        for (int i = 0; i < cellExtents.length; i++) {
            double d = cellExtents[i] * CELL_EXTENT_FACTOR * 0.5;
            
            llbCorner[i] = fractionalCenter[i] - d;
            urfCorner[i] = fractionalCenter[i] + d;
        }
        
        // Build a list of all the codes for sites in the specified space
        for (Map.Entry<String, AtomSiteRecord> atomEntry
                : atomRecordMap.entrySet()) {
            
            // Find the base fractional coordinates for this atom 
            FractionalAtom atom = atomForRecord(atomEntry.getValue());
            int[] minTranslations = new int[3];
            int[] maxTranslations = new int[3];
            int[] workTranslations = new int[3];
            
            // For each base symmetry operation
            symmops:
            for (SymmetryMatrix matrix : symmetry.getBaseOperations()) {
                SymmetryCode code = symmetry.getSymmetryCode(matrix);
                
                // The fractional coordinates of the transformed atom:
                double[] targetCoords
                        = matrix.transformPoint(atom.getFractionalCoordinates()); 
                
                // Find the limiting cell translations to put this site into the
                // specified volume
                for (int i = 0; i < 3; i++) {
                    minTranslations[i] = 5
                            + (int) Math.ceil(llbCorner[i] - targetCoords[i]);
                    maxTranslations[i] = 5
                            + (int) Math.floor(urfCorner[i] - targetCoords[i]);
                    
                    if ((minTranslations[i] > 9) || (maxTranslations[i] < 1)) {
                        continue symmops;
                    } else {
                        minTranslations[i] = Math.max(minTranslations[i], 1);
                        maxTranslations[i] = Math.min(maxTranslations[i], 9);
                    }
                }
                
                // Add the site codes corresponding to all the discovered
                // translations, and the bonds from those sites to all other
                // sites
                for (int xtran = minTranslations[0];
                        xtran <= maxTranslations[0]; xtran++) {
                    workTranslations[0] = xtran;
                    for (int ytran = minTranslations[1];
                            ytran <= maxTranslations[1]; ytran++) {
                        workTranslations[1] = ytran;
                        for (int ztran = minTranslations[2];
                                ztran <= maxTranslations[2]; ztran++) {
                            workTranslations[2] = ztran;
                            SymmetryCode newCode
                                    = new SymmetryCode(
                                            code.getOperationIndex(),
                                            workTranslations.clone());
                            SiteCode site
                                    = new SiteCode(atomEntry.getKey(), newCode);
                            
                            includedSiteCodes.add(site);
                            addBondsForSite(site, connections);
                        }
                    }
                }
            }
        }
        
        // Create and return the CrtFile
        return buildModel(connections);
    }
    
    /**
     * Constructs a {@code MolecularModel<FractionalAtom, Bond>} object from
     * a connection table, based on site, unit cell, and symmetry details stored
     * in this builder.  All model-building method ultimately rely on this
     * method to generate their result.
     * 
     * @param  connections a connection table in the form of a
     *         {@code Map<SiteCode, List<SiteCode>>}, where the keys are the
     *         codes for the source sites and the values are lists of the codes
     *         of the target sites to which the source site forms bonds.  Some
     *         target sites may not be listed as source atoms, but to the extent
     *         that most are, the connection table should express a symmetric
     *         relation; all sites represented as keys in the table will appear
     *         as atoms in the resulting model, whether or not any connections
     *         are recorded for them
     * 
     * @return a {@code MolecularModel<FractionalAtom, Bond>} representing the
     *         model described by the specified connection table, atom site
     *         details, unit cell, and symmetry
     */
    private MolecularModel<FractionalAtom, Bond<FractionalAtom>>
            buildModel(Map<SiteCode, List<SiteCode>> connections) {
        
        // The MolecularModel being constructed
        MolecularModel<FractionalAtom, Bond<FractionalAtom>> model
                = new MolecularModel<FractionalAtom, Bond<FractionalAtom>>();
        
        // A cache of the atom computed for each site code
        Map<SiteCode, FractionalAtom> atomMap
                = new HashMap<SiteCode, FractionalAtom>();
        
        // A map from site labels to the distinct site codes for each label that
        // appears in the model
        Map<String, List<SiteCode>> distinctSitesMap
                = new HashMap<String, List<SiteCode>>();
        
        // A Map from site codes to equivalent site codes representing the
        // same absolute position; the keys are to be replaced by their
        // associated targets during the site merging step
        Map<SiteCode, SiteCode> siteMergeMap
                = new HashMap<SiteCode, SiteCode>();
        
        // A Set of the site codes whose associated bonds have already been
        // added to the model; used to avoid duplicate bonds
        Set<SiteCode> sitesHandled = new HashSet<SiteCode>();
        
        // A set of the site codes of valid bond targets -- that is, the site
        // codes that represent atoms included in the model
        Set<SiteCode> validTargets;
        
        // A List of the FractionalAtoms that will be represented in the model,
        // sorted according to the configured atom Comparator
        List<FractionalAtom> sortedAtoms;
        
        /*
         * Set the unit cell and symmetry operations on the model 
         */
        model.setCell(cell);
        for (SymmetryMatrix symmop : symmetry.getBaseOperations()) {
            model.addSymmetryOperation(symmop);
        }
        
        /*
         * Construct {@code FractionalAtom} instances for the distinct source
         * atoms in the connection table, and at the same time, record which
         * source atoms are duplicates of other source atoms (a result of being
         * at a special position of the space group)
         */
        each_connection_start:
        for (SiteCode code : connections.keySet()) {
            String siteLabel = code.getSiteLabel();
            SymmetryCode symmcode = code.getSymmetryCode();
            FractionalAtom atom = atomForRecord(atomRecordMap.get(siteLabel));
            List<SiteCode> distinctSitesList = distinctSitesMap.get(siteLabel);
            
            if (distinctSitesList == null) {
                distinctSitesList = new ArrayList<SiteCode>();
                distinctSitesMap.put(siteLabel, distinctSitesList);
            }
            
            // Apply the indicated symmetry transformation
            if (!symmcode.equals(symmetry.getIdentityCode())) {
                SymmetryMatrix matrix = symmetry.getSymmetryMatrix(symmcode);
                
                atom = transformAtom(atom, matrix, motionFactory);
            }
            atom.setSiteTag(code.toString());
            
            /*
             * Look for symmetry-equivalent sites among those for the same base
             * atom site
             */ 
            for (SiteCode otherSite : distinctSitesList) {
                FractionalAtom otherAtom = atomMap.get(otherSite);
                
                if (atom.getPosition().dSqrTo(otherAtom.getPosition())
                        < ATOM_DSQR_TOLERANCE) {
                    
                    // A duplicate position (the atom is on a special position)
                    siteMergeMap.put(code, otherSite);
                    continue each_connection_start;
                }
            }
            
            // Haven't seen this position before; add it
            distinctSitesList.add(code);
            atomMap.put(code, atom);
        }
        
        // Merge any equivalent atomic sites
        for (Map.Entry<SiteCode, SiteCode> mergeEntry
                : siteMergeMap.entrySet()) {
            SiteCode duplicate = mergeEntry.getKey();
            SiteCode mergeTo = mergeEntry.getValue();
            List<SiteCode> dupConnections = connections.remove(duplicate);
            List<SiteCode> mergeConnections = connections.get(mergeTo);
            
            /*
             * If any of the duplicate's connections are not already represented
             * among the merge target's connections, then add them
             */
            for (SiteCode target : dupConnections) {
                if (!mergeConnections.contains(target)) {
                    mergeConnections.add(target);
                }
            }
            
            /*
             * Replace the duplicate with the merge target among all the other
             * connections
             */
            for (List<SiteCode> otherConnections : connections.values()) {
                if (otherConnections.contains(duplicate)) {
                    otherConnections.remove(duplicate);
                    if (!otherConnections.contains(mergeTo)) {
                        otherConnections.add(mergeTo);
                    }
                }
            }
        }
        
        /*
         * Add the atoms to the model 
         */
        sortedAtoms = new ArrayList<FractionalAtom>(atomMap.values());
        Collections.sort(sortedAtoms, getAtomComparator());
        for (FractionalAtom atom : sortedAtoms) {
            model.addAtom(atom);
        }

        /*
         * Add the bonds to the model, in an order mostly reflecting that of the
         * sorted atoms list, taking care to avoid duplicates
         */
        validTargets = connections.keySet();
        for (FractionalAtom originatingAtom : sortedAtoms) {
            SiteCode originatingCode
                    = SiteCode.valueOf(originatingAtom.getSiteTag(), null);
            
            sitesHandled.add(originatingCode);
            for (SiteCode targetCode : connections.get(originatingCode)) {
                if (!sitesHandled.contains(targetCode)
                        && validTargets.contains(targetCode)) {
                    model.addBond(new Bond<FractionalAtom>(
                            originatingAtom, atomMap.get(targetCode)));
                }
            }
        }

        return model;
    }
    
    /**
     * Populates the specified connection table with all connections originating
     * at the specified site.  At this version, the connection table is updated
     * only if it doesn't already contain any entry for the specified source
     * site.  
     * 
     * @param  site the site for which connections are to be computed, as a
     *         {@code SiteCode}
     * @param  connections the connection table to which any computed
     *         connections should be added, in the form of a
     *         {@code Map<SiteCode, List<SiteCode>>} 
     */
    private void addBondsForSite(SiteCode site,
            Map<SiteCode, List<SiteCode>> connections) {
        if (!connections.containsKey(site)) {
            
            // Create the connection list for this site
            List<SiteCode> siteConnections = new ArrayList<SiteCode>();
            
            // Compute the site code to use to look up connections in the bond
            // table
            String siteLabel = site.getSiteLabel();

            // Nothing to do unless there are corresponding bonds in the table
            if (bondDetailsMap.containsKey(siteLabel)) {
                
                // Obtain the symmetry matrix for the source site
                SymmetryMatrix matrix
                        = symmetry.getSymmetryMatrix(site.getSymmetryCode());
                
                // Iterate over the appropriate connections in the bond table 
                for (SiteCode target : bondDetailsMap.get(siteLabel)) {
                    SymmetryCode baseTargetCode = target.getSymmetryCode();
                    SymmetryCode targetCode;
                    
                    // Often the target's symmetry code will correspond to the
                    // identity operation; in that case no computation or lookup
                    // is required
                    if (symmetry.getIdentityCode().equals(baseTargetCode)) {
                        targetCode = site.getSymmetryCode();
                    } else {
                        SymmetryMatrix targetMatrix
                                = symmetry.getSymmetryMatrix(baseTargetCode);
                        
                        targetCode = symmetry.getSymmetryCode(
                                matrix.times(targetMatrix, false));
                    }
                    
                    siteConnections.add(
                            new SiteCode(target.getSiteLabel(), targetCode));
                }
            }
            
            connections.put(site, siteConnections);
        }
    }

    /**
     * Updates the connection table with connections corresponding to the
     * specified bond record, accounting for any symmetry described by the
     * record
     * 
     * @param  record a {@code GeomBondRecord} containing the connection details 
     * 
     * @see #addBondRecord(SiteCode, SiteCode)
     */
    private void addBondRecords(GeomBondRecord record) {
        try {
            SymmetryCode code1
                    = (record.getSiteSymm1() == null)
                            ? symmetry.getIdentityCode()
                            : SymmetryCode.valueOf(record.getSiteSymm1());
            SymmetryCode code2
                    = (record.getSiteSymm2() == null)
                            ? symmetry.getIdentityCode()
                            : SymmetryCode.valueOf(record.getSiteSymm2());
            SiteCode site1 = new SiteCode(record.getSiteLabel1(), code1);
            SiteCode site2 = new SiteCode(record.getSiteLabel2(), code2);
            addBondRecord(site1, site2);
            addBondRecord(site2, site1);
            
        } catch (IllegalArgumentException iae) {
            // Just don't add this record
        }
    }

    /**
     * A helper method that accounts for symmetry in adding a connection to
     * the internal base map of connections.  The connection added by this
     * method will always originate from a site code with the same label as the
     * first specified site code, but with the identity symmetry operation code.
     * The symmetry code of the second site code is adjusted as necessary if the
     * first site code is changed.  Duplicate connections are silently dropped.
     * 
     * @param  site1 the first / left / originating {@code SiteCode} for the
     *         connection
     * @param  site2 the second / right / destination {@code SiteCode}
     */
    private void addBondRecord(SiteCode site1, SiteCode site2) {
        String label1 = site1.getSiteLabel();
        SymmetryCode code1 = site1.getSymmetryCode();
        List<SiteCode> connectionList;
        
        if (!symmetry.getIdentityCode().equals(code1)) {
            
            /*
             * Determine the symmetry-related bond endpoint that involves the
             * identity operation for site 1
             */
            SymmetryMatrix inverse1
                    = symmetry.getSymmetryMatrix(code1).inverse();
            SymmetryMatrix matrix2
                    = symmetry.getSymmetryMatrix(site2.getSymmetryCode());
            SymmetryCode newCode2
                    = symmetry.getSymmetryCode(inverse1.times(matrix2, false));
            
            site1 = new SiteCode(site1.getSiteLabel(),
                    symmetry.getIdentityCode());
            site2 = new SiteCode(site2.getSiteLabel(), newCode2);
        }

        /*
         * Add the connection, if it is not already present
         */
        connectionList = bondDetailsMap.get(label1);
        if (connectionList == null) {
            connectionList = new ArrayList<SiteCode>();
            bondDetailsMap.put(label1, connectionList);
        }
        
        if (!connectionList.contains(site2)) {
            connectionList.add(site2);
        }
    }

    /**
     * Analyzes the specified CIF data block to find the valid CIF atom site
     * data.  An atom site is considered valid if its label is not a placeholder
     * and not blank, and its fractional coordinates are all present and
     * numeric.
     * 
     * @param  block the {@code DataBlock} from which atom site data are to be
     *         obtained
     *          
     * @return a {@code Map} of {@code AtomSiteRecord} objects for the valid
     *         atomic site data; the map's Collection views' iteration order is
     *         the order that the corresponding data appear in the specified
     *         block
     * 
     * @throws IllegalArgumentException if the block does not contain a useable
     *         list of items from the CIF {@code _atom_site_[]} category, or if
     *         it contains records with duplicate labels
     */
    private Map<String, AtomSiteRecord> getValidAtoms(DataBlock block) {
        LinkedHashMap<String, AtomSiteRecord> map
                = new LinkedHashMap<String, AtomSiteRecord>();
        Iterator<AtomSiteRecord> iterator = new IteratorFilter<AtomSiteRecord>(
                new AtomSiteIterator(block)) {

                    @Override
                    public boolean shouldPass(AtomSiteRecord record) {
                        return record.isValid();
                    }
                };
        
        while (iterator.hasNext()) {
            AtomSiteRecord record = iterator.next();
            String siteLabel = record.getLabel();
            
            if (map.containsKey(siteLabel)) {
                throw new IllegalArgumentException(
                        "Duplicate atom site label: " + siteLabel);
            } else {
                map.put(siteLabel, record);
            }
        }
        
        return map;
    }
    
    /**
     * Extracts the available atomic (displacement) motion information from the
     * specified CIF data block and forms appropriate {@code AtomicMotion}s for
     * it, returning a map from site labels to atomic motions.  This method
     * expects the {@link #atomRecordMap} to have already been initialized.
     *
     * @param block the {@code DataBlock} from which to extract the information
     * @param factory an {@code AtomicMotionFactory} configured appropriately
     *        for use with data from the specified block
     *         
     * @return a {@code Map<String, AtomicMotion>} associating site labels with
     *        {@code AtomicMotion} objects for the atomic motion information
     *        available from the specified data block
     */
    private Map<String, AtomicMotion> getAtomicMotions(DataBlock block,
            AtomicMotionFactory factory) {
        Iterator<AtomSiteAnisoRecord> anisoMotionIterator
                = new AtomSiteAnisoIterator(block);
        Map<String, AtomicMotion> motionMap
                = new HashMap<String, AtomicMotion>();

        /*
         * Anisotropic motions
         */
        for (; anisoMotionIterator.hasNext(); ) {
            AtomSiteAnisoRecord record = anisoMotionIterator.next();
            double[] parameters;
            
            parameters = record.getAnisoU();
            if (parameters != null) {
                motionMap.put(record.getLabel(), factory.motionForAnisotropicU(
                        parameters[0], parameters[1], parameters[2],
                        parameters[3], parameters[4], parameters[5]));
            } else {
                parameters = record.getAnisoB();
                if (parameters != null) {
                    motionMap.put(
                            record.getLabel(), factory.motionForAnisotropicB(
                                    parameters[0], parameters[1], parameters[2],
                                    parameters[3], parameters[4], parameters[5]));
                }
            }
        }
        
        /*
         * Isotropic motions
         */
        for (AtomSiteRecord atomRecord : atomRecordMap.values()) {
            String label = atomRecord.getLabel();
            
            if (!motionMap.containsKey(label)) {
                double isoDisplacement = atomRecord.getIsoU();
                
                if (!Double.isNaN(isoDisplacement)) {
                    motionMap.put(label,
                            factory.motionForIsotropicU(isoDisplacement));
                } else {
                    isoDisplacement = atomRecord.getIsoB();
                    
                    if (!Double.isNaN(isoDisplacement)) {
                        motionMap.put(label,
                                factory.motionForIsotropicB(isoDisplacement));
                    }
                }
            }
        }
        
        return motionMap;
    }

    /**
     * Extracts unit cell information from a CIF {@code DataBlock} and uses it
     * to construct and return a corresponding {@code CoordinateSystem} object
     * 
     * @param  block the CIF {@code DataBlock} containing the unit cell
     *         information 
     *         
     * @return a {@code CoordinateSystem} representing the crystallographic unit cell
     *         described by the specified data block, or {@code null} if any of
     *         the unit cell parameters are missing from the block
     */
    private CoordinateSystem getUnitCell(DataBlock block) {
        double a;
        double b;
        double c;
        double alpha;
        double beta;
        double gamma;
        CifValue value;

        value = CifFileUtil.lookupCifValue(block, "_cell_length_a");
        a = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(a)) {
            return null;
        }

        value = CifFileUtil.lookupCifValue(block, "_cell_length_b");
        b = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(b)) {
            return null;
        }

        value = CifFileUtil.lookupCifValue(block, "_cell_length_c");
        c = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(c)) {
            return null;
        }

        value = CifFileUtil.lookupCifValue(block, "_cell_angle_alpha");
        alpha = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(alpha)) {
            return null;
        }

        value = CifFileUtil.lookupCifValue(block, "_cell_angle_beta");
        beta = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(beta)) {
            return null;
        }

        value = CifFileUtil.lookupCifValue(block, "_cell_angle_gamma");
        gamma = CifFileUtil.getCifDouble(value, Double.NaN);
        if (Double.isNaN(gamma)) {
            return null;
        }

        return new CoordinateSystem(a, b, c,
                Math.cos(Math.toRadians(alpha)),
                Math.cos(Math.toRadians(beta)),
                Math.cos(Math.toRadians(gamma)));
    }

    /**
     * Extracts a list symmetry operations from the specified CIF data block if
     * it contains one (either the items having data name
     * {@code _space_group_symop_operation_xyz}, or those having data name
     * {@code _symmetry_equiv_pos_as_xyz}).  The returned list has contains
     * {@code SymmetryMatrix} objects corresponding to the operations, in an
     * order corresponding to that in which the operations are listed in the
     * CIF.  This method requires that the values be inside a CIF {@code loop_}
     * construct.
     * 
     * @param  block the CIF {@code DataBlock} from which to extract symmetry
     *         operations
     *         
     * @return a {@code List<SymmetryMatrix>} of the symmetry operations listed
     *         in the CIF, or {@code null} if no operations are listed or if
     *         any of the listed operations are unparseable
     */
    private List<SymmetryMatrix> getSymmetryOperations(DataBlock block) {
        List<CifValue> symmValues;
        List<SymmetryMatrix> symmMatrices;
        
        // Get the list of values based on one of the two data names
        if (block.containsNameInLoop("_space_group_symop_operation_xyz")) {
            symmValues = block.getLoopForName(
                    "_space_group_symop_operation_xyz").getValuesForName(
                            "_space_group_symop_operation_xyz");
        } else if (block.containsNameInLoop("_symmetry_equiv_pos_as_xyz")) {
            symmValues = block.getLoopForName(
                    "_symmetry_equiv_pos_as_xyz").getValuesForName(
                            "_symmetry_equiv_pos_as_xyz");
        } else {
            return null;
        }

        // Parse the values and add corresponding matrices to the return list
        symmMatrices = new ArrayList<SymmetryMatrix>(symmValues.size());
        for (CifValue value : symmValues) {
            try {
                symmMatrices.add(parseSymmetryOperation(
                        CifFileUtil.getCifString(value, null, false)));
            } catch (IllegalArgumentException iae) {
                return null;
            } catch (NullPointerException npe) {
                return null;
            }
        }
        
        return symmMatrices;
    }
    
    /**
     * Parses a symmetry operation string formatted according to CIF convention,
     * producing as a result a SymmetryMatrix representing that operation
     *  
     * @param  symop the {@code String} to parse
     * 
     * @return a {@code SymmetryMatrix} representation of the symmetry operation
     *         described by the specified string
     * 
     * @throws IllegalArgumentException if the string is not a valid symmetry
     *         operation representation
     */
    private SymmetryMatrix parseSymmetryOperation(String symop) {
        StringTokenizer st = new StringTokenizer(
                symop.replaceAll(" +", "").toLowerCase(), ",");
        int[][] matrix = new int[3][3];
        int[] vector = new int[3];
        
        if (st.countTokens() != 3) {
            throw new IllegalArgumentException(
                    "Invalid symmetry operation string: + symop");
        }
        
        // for each coordinate expression:
        for (int i = 0; i < 3; i++) {
            String coordExpression = st.nextToken();
            Matcher termMatcher = TERM_PATTERN.matcher(coordExpression);
            boolean coordinateOK = false;

            for (int position = 0; position < coordExpression.length(); ) {
                int sign;
                String group;
                Integer coordIndex;
                
                if (!termMatcher.lookingAt()) {
                    throw new IllegalArgumentException(
                            "Invalid symmetry operation string: + symop");
                }
                
                // Extract the sign; no explicit sign means positive
                sign = (("-".equals(termMatcher.group(1))) ? -1 : 1);
                
                // Extract and analyze the rest of the group
                group = termMatcher.group(2);
                coordIndex = COORDINATE_INDICES.get(group);
                if (coordIndex != null) {  // a coordinate symbol: x, y, or z
                    if (matrix[i][coordIndex] != 0) {
                        throw new IllegalArgumentException(
                                "Invalid symmetry operation string: + symop");
                    }
                    matrix[i][coordIndex] = sign;
                    coordinateOK = true;
                } else {                   // a number (presumably)
                    if (vector[i] != 0) {
                        throw new IllegalArgumentException(
                                "Invalid symmetry operation string: + symop");
                    }
                    try {
                        vector[i] = sign * extractValueAsTwelfths(group);
                    } catch (NumberFormatException nfe) {
                        throw new IllegalArgumentException(
                                "Invalid symmetry operation string: + symop",
                                nfe);
                    }
                }
                    
                position += termMatcher.group().length();
                termMatcher.reset(coordExpression.substring(position));
            }
            if (!coordinateOK) {
                // no nonzero rotation component
                throw new IllegalArgumentException(
                        "Invalid symmetry operation string: + symop");
            }
        }
        return new SymmetryMatrix(matrix, vector, false);
    }
    
    /**
     * Creates a new {@code FractionalAtom} based on the data from the specified
     * {@code AtomSiteRecord} and {@code CoordinateSystem}, with atomic
     * displacement information as appropriate
     * 
     * @param  record the {@code AtomSiteRecord} from which to obtain the atom
     *         site data 
     *         
     * @return a {@code FractionalAtom} corresponding to the specified atom site
     *         data and unit cell; the atom's associated element will be
     *         {@code null} if it cannot be determined from the provided data
     */
    private FractionalAtom atomForRecord(AtomSiteRecord record) {
        Element element = determineElement(record);
        FractionalAtom atom = new FractionalAtom(
                formatAtomLabel(record.getLabel(), element),
                element, record.getFractX(), record.getFractY(),
                record.getFractZ(), cell,
                record.getLabel().replaceAll("\\s+", "") + '|'
                        + symmetry.getIdentityCode().toString());
        
        atom.setAtomicMotion(atomicMotionMap.get(record.getLabel()));
        
        return atom;
    }
    
    /**
     * Formats an atom label as an element symbol in standard format, with a
     * parenthesized number or tag, provided that the label already starts with
     * the symbol of the specified element (case insensitive) and contains at
     * least one more character.
     * 
     * @param  label the label to format
     * @param  element the {@code Element} of the atom to which the specified
     *         label refers
     *          
     * @return a {@code String} similar to label, but with the initial element
     *         symbol in standard upper/lowercase format and the remainder
     *         parenthesized; alternatively, the input label itself if the
     *         specified element was {@code null} or if the label did not start
     *         with the element's symbol 
     */
    private String formatAtomLabel(String label, Element element) {
        if (element == null) {
            return label;
        } else {
            String symbol = element.getSymbol();
            
            if ((label.length() > symbol.length())
                    && label.substring(0, symbol.length()).equalsIgnoreCase(
                            symbol)) {
                StringBuilder sb = new StringBuilder(symbol);
                
                sb.append('(');
                sb.append(label.substring(symbol.length()));
                sb.append(')');
                
                return sb.toString();
            } else {
                return label;
            }
        }
    }

    /**
     * Determines the element present at the atom site described by the provided
     * atom site record, first analyzing the site's type symbol, then falling
     * back to its label if necessary
     *  
     * @param  record the {@code AtomSiteRecord} containing the atom site data
     *         for which to determine a chemical element
     *         
     * @return the {@code Element} referred to by the specified record, or
     *         {@code null} if none can be determined
     */
    private Element determineElement(AtomSiteRecord record) {
        Element e = determineElement(record.getTypeSymbol());
        
        if (e == null) {
            e = determineElement(record.getLabel());
        }
        
        return e;
    }
    
    /**
     * Attempts to interpret the specified string to extract an element symbol
     * from its first one or two characters.  An initial two-character substring
     * is matched against known element symbols, and if no match is found then
     * the initial one-character substring is matched against the element
     * symbols.
     * 
     * @param  s the {@code String} from which to extract an element symbol
     *  
     * @return the {@code Element} representing the symbol found as the string's
     *         initial substring, or {@code null} if no such element exists
     */
    private Element determineElement(String s) {
        Matcher m = ELEMENT_SYMBOL_PATTERN.matcher(s);
        Element e = null;
        
        if (m.lookingAt()) {
            // Extract the symbol in standard form
            String symbol = m.group(1).toUpperCase() + m.group(2).toLowerCase();
            
            // Lookup the element via the full symbol
            e = Element.forSymbol(symbol);
            
            // If the symbol wasn't found then try the first part alone
            if ((e == null) && (symbol.length() > 1)) {
                e = Element.forSymbol(symbol.substring(0, 1));
            }
        }
        
        // Return the element discovered, which may be null
        return e;
    }

    /**
     * Analyzes the string representation of a decimal number or fraction to
     * determine the integral number of twelfths it represents.  This is
     * specifically intended for use with numbers used to express
     * crystallographic symmetry operations, which are <em>always</em> integral
     * numbers of twelfths for any space group in any conventional setting.
     * 
     * @param  s the {@code String} to analyze
     * 
     * @return the number of twelfths represented by the input string
     * 
     * @throws NumberFormatException if the specified string does not represent
     *         an integral number of twelfths, or does not represent a number at
     *         all
     */
    private int extractValueAsTwelfths(String s) {
        int slashPosition = s.indexOf('/');
        
        if (slashPosition >= 0) {
            
            // a fraction
            
            int numerator = 12 * Integer.parseInt(
                    s.substring(0, slashPosition));
            int denominator = Integer.parseInt(
                    s.substring(slashPosition + 1));
            
            if ((numerator % denominator) != 0) {
                throw new NumberFormatException("'" + s
                        + "' is not valid in a crystallographic symmetry "
                        + "operation");
            } else {
                return (numerator / denominator);
            }
        } else {
            
            // a decimal number
            
            float floatTwelfths = Float.parseFloat(s) * 12;
            int twelfths = Math.round(floatTwelfths);
            
            if (Math.abs(floatTwelfths - twelfths)
                    > FRACTION_TOLERANCE) {
                throw new NumberFormatException("'" + s
                        + "' is not valid in a crystallographic symmetry "
                        + "operation");
            } else {
                return twelfths;
            }
        }
    }
    
    /**
     * Computes the effect of a symmetry operation on an atom, including on its
     * atomic motion if it has any specified
     * 
     * @param  atom a {@code FractionalAtom} to transform
     * @param  transformation a {@code SymmetryMatrix} representing the symmetry
     *         operation to perform
     * @param  motionFactory an {@code AtomicMotionFactory} to use in preparing
     *         transformed atomic motions as necessary; should be based on the
     *         same (fractional) coordinate system that the atom is referred to
     *         
     * @return a new {@code FractionalAtom} representing the result of applying
     *         the specified symmetry transformation to the specified atom
     */
    public static FractionalAtom transformAtom(FractionalAtom atom,
            SymmetryMatrix transformation, AtomicMotionFactory motionFactory) {
        FractionalAtom tAtom = new FractionalAtom(atom.getLabel(),
                atom.getElement(),
                transformation.transformPoint(atom.getFractionalCoordinates()),
                atom.getReferenceCell(), atom.getSiteTag());

        tAtom.setAtomicMotion(transformMotion(atom.getAtomicMotion(),
                transformation, motionFactory));
        
        return tAtom;
    }

    /**
     * Computes the effect of a symmetry operation on an atomic motion tensor
     *  
     * @param  motion the {@code AtomicMotion} object to which a symmetry
     *         operation is to be applied
     * @param  transformation a {@code SymmetryMatrix} representing the symmetry
     *         operation to apply
     * @param  motionFactory an {@code AtomicMotionFactory} with which to
     *         obtain a new {@code AtomicMotion} if necessary
     * 
     * @return an {@code AtomicMotion} object representing the result of
     *         transforming the input atomic motion according to the specified
     *         symmetry operation; may be the same object as the input motion if
     *         no transformation is in fact required for the specified symmetry
     *         operation
     */
    private static AtomicMotion transformMotion(AtomicMotion motion,
            SymmetryMatrix transformation, AtomicMotionFactory motionFactory) {

        /*
         * Gracefully handle null motions
         */
        if (motion == null) {
            return null;

        /*
         * Pure translations do not modify atomic displacements, and no kind
         * of symmetry modifies isotropic motions
         */
        } else if ((transformation.getType() == SymmetryMatrix.Type.TRANSLATION)
                || !motion.isAnisotropic()) {
            return motion;

        /*
         * Create and return a transformed motion
         */
        } else {

            /*
             * Get the displacement parameter array and apply the transformation
             * to it, column by column. Displacements are relative to the
             * nominal atomic position, therefore the columns must be handled as
             * vector quantities (i.e. they are only affected by rotations,
             * not translations).
             */
            double[][] uij = motion.getAnisotropicU();
            double[] work;
            double u11, u22, u33, u12, u13, u23;

            work = transformation.transformVector(uij[2]);
            u13 = work[0];
            u23 = work[1];
            u33 = work[2];

            work[0] = uij[1][0];
            work[1] = uij[1][1];
            work[2] = uij[2][1];
            work = transformation.transformVector(work);
            u12 = work[0];
            u22 = work[1];

            work[0] = uij[0][0];
            work[1] = uij[1][0];
            work[2] = uij[2][0];
            work = transformation.transformVector(work);
            u11 = work[0];

            return motionFactory.motionForAnisotropicU(
                    u11, u22, u33, u12, u13, u23);
        }
    }
}