/*
 * Reciprocal Net project
 * 
 * SampleTextBL.java
 *
 * 13-Dec-2004: eisiorho wrote first draft
 * 10-Mar-2005: midurbin added PREFERRED_NAME and updated
 *              getSamplePreferredName()
 * 23-Mar-2005: midurbin replaced getSamplePreferredName() and getSampleNames()
 *              with a more general-purpose getSampleNames() method and altered
 *              getSamplePreferredFormula() to return a SampleTextInfo
 * 12-Apr-2005: midurbin added RAW_DATA_URL annotation
 * 12-Jul-2005: ekoperda added sanitizeSampleForExport()
 * 16-Aug-2005: midurbin removed HtmlHelper import statement
 * 28-Oct-2005: jobollin removed a try/catch in sanitizeSampleForExport because
 *              the catch was no longer reachable; removed unused imports
 * 17-Mar-2006: jobollin made getAllTextTypes() return a shared, unmodifiable
 *              Collection instead of constructing a new, modifiable one each
 *              time; reformatted the source
 */

package org.recipnet.site.shared.bl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.recipnet.site.shared.db.SampleAnnotationInfo;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * SampleTextBL is a container class that contains the texttype values for
 * samples and retrieval methods for text fields.
 */
public abstract class SampleTextBL {

    /**
     * This constant is used when it is not a valid SampleAnnotationInfo or
     * SampleAttributeInfo class.
     */
    public static final int INVALID_TYPE = 0;

    /** The following types are used for SampleAttributeInfo classes */
    public static final int EMPIRICAL_FORMULA = 10100;

    public static final int STRUCTURAL_FORMULA = 10200;

    public static final int MOIETY_FORMULA = 10300;

    public static final int COMMON_NAME = 10400;

    public static final int TRADE_NAME = 10500;

    public static final int KEYWORD = 10600;

    public static final int CRYSTALLOGRAPHER_NAME = 10700;

    public static final int SAMPLE_PROVIDER_NAME = 10800;

    public static final int OTHER = 10900;

    public static final int CSD_REFCODE = 11000;

    public static final int ICSD_COLLECTION_CODE = 11100;

    public static final int PDB_ENTRY_NUMBER = 11200;

    public static final int TEXT_CONTRIBUTOR = 11300;

    public static final int SHORT_DESCRIPTION = 11400;

    public static final int CAS_REGISTRY_NUMBER = 11500;

    public static final int EMPIRICAL_FORMULA_DERIVED = 11600;

    public static final int EMPIRICAL_FORMULA_SINGLE_ION = 11700;

    public static final int EMPIRICAL_FORMULA_LESS_SOLVENT = 11800;

    public static final int PROVIDER_REFERENCE_NUMBER = 11900;

    /** The following types are used for SampleAnnotationInfo classes */
    public static final int CITATION_OF_A_PUBLICATION = 100;

    public static final int LAYMANS_EXPLANATION = 300;

    public static final int SMILES_FORMULA = 400;

    public static final int IUPAC_NAME = 500;

    public static final int MISC_COMMENTS = 600;

    public static final int COPYRIGHT_NOTICE = 700;

    public static final int CHANGE_TO_DATA_FILES_IN_REPOSITORY = 800;

    public static final int SUPERSEDED_BY_ANOTHER_SAMPLE = 900;

    public static final int SUPERSEDES_ANOTHER_SAMPLE = 1000;

    public static final int DUPLICATE_STRUCTURE_OF_ANOTHER_SAMPLE = 1100;

    public static final int MISC_REFERENCE_TO_ANOTHER_SAMPLE = 1200;

    public static final int DETERMINATION_PROCEDURE = 1300;

    public static final int INELIGIBLE_FOR_RELEASE = 1400;

    public static final int INCOMPLETENESS_EXPLANATION = 1500;

    public static final int DATA_QUALITY = 1600;

    public static final int TWIN_EXPLANATION = 1700;

    public static final int PREFERRED_NAME = 1800;

    public static final int RAW_DATA_URL = 1900;

    /**
     * A collection of all the text types that correspond to known attributes;
     * used in determining whether a text type is an attribute
     */
    private final static Collection<Integer> attributeTypes;
    
    /**
     * A collection of all the text types that correspond to known annotations;
     * used to determine whether a text type is an attribute
     */
    private final static Collection<Integer> annotationTypes;
    
    /**
     * A collection of all the defined text types, except local tracking;
     * returned by getAllTextTypes()
     */
    private final static Collection<Integer> allTextTypes;

    /*
     * This static initializer DEFINES which text types are attributes and
     * which are annotations:
     */
    static {

        // Note: initial capacity * load factor = #elements without rehashing
        Collection<Integer> c = new HashSet<Integer>(25, 0.75f);

        // Add all the attributes to the set
        c.add(EMPIRICAL_FORMULA);
        c.add(STRUCTURAL_FORMULA);
        c.add(MOIETY_FORMULA);
        c.add(COMMON_NAME);
        c.add(TRADE_NAME);
        c.add(KEYWORD);
        c.add(CRYSTALLOGRAPHER_NAME);
        c.add(SAMPLE_PROVIDER_NAME);
        c.add(OTHER);
        c.add(CSD_REFCODE);
        c.add(ICSD_COLLECTION_CODE);
        c.add(PDB_ENTRY_NUMBER);
        c.add(TEXT_CONTRIBUTOR);
        c.add(SHORT_DESCRIPTION);
        c.add(CAS_REGISTRY_NUMBER);
        c.add(EMPIRICAL_FORMULA_DERIVED);
        c.add(EMPIRICAL_FORMULA_SINGLE_ION);
        c.add(EMPIRICAL_FORMULA_LESS_SOLVENT);
        c.add(PROVIDER_REFERENCE_NUMBER);
        
        attributeTypes = Collections.unmodifiableCollection(c);

        c = new HashSet<Integer>(25, 0.75f);
        
        // Add all the annotations to the set
        c.add(CITATION_OF_A_PUBLICATION);
        c.add(LAYMANS_EXPLANATION);
        c.add(SMILES_FORMULA);
        c.add(IUPAC_NAME);
        c.add(MISC_COMMENTS);
        c.add(COPYRIGHT_NOTICE);
        c.add(CHANGE_TO_DATA_FILES_IN_REPOSITORY);
        c.add(SUPERSEDED_BY_ANOTHER_SAMPLE);
        c.add(SUPERSEDES_ANOTHER_SAMPLE);
        c.add(DUPLICATE_STRUCTURE_OF_ANOTHER_SAMPLE);
        c.add(MISC_REFERENCE_TO_ANOTHER_SAMPLE);
        c.add(DETERMINATION_PROCEDURE);
        c.add(INELIGIBLE_FOR_RELEASE);
        c.add(INCOMPLETENESS_EXPLANATION);
        c.add(DATA_QUALITY);
        c.add(TWIN_EXPLANATION);
        c.add(PREFERRED_NAME);
        c.add(RAW_DATA_URL);
        
        annotationTypes = Collections.unmodifiableCollection(c);

        // combine them all into one big set
        c = new HashSet<Integer>(50, 0.75f);
        c.addAll(attributeTypes);
        c.addAll(annotationTypes);
        
        allTextTypes = Collections.unmodifiableCollection(c);
    }

    /**
     * Type values equal to this number and higher are reserved for attributes
     * whose meaning is locally defined, i.e. local tracking fields.
     */
    public static final int LOCAL_TRACKING_BASE = 100000;

    /**
     * Identifies if a submitted type is an annotation.
     * 
     * @param type an {@code int}
     * @return {@code true} if the {@code int} is an annotation, {@code false}
     *         otherwise.
     */
    public static boolean isAnnotation(int type) {
        return annotationTypes.contains(type);
    }

    /**
     * Identifies if a submitted type is an attribute.
     * 
     * @param type an {@code int}
     * @return {@code true} if the {@code int} is an attribute, {@code false}
     *         otherwise.
     */
    public static boolean isAttribute(int type) {
        return (SampleTextBL.isLocalAttribute(type)
                || attributeTypes.contains(type));
    }

    /**
     * Determines whether the specified sample text type represents a local
     * tracking attribute (irrespective of whether a local tracking attribute of
     * that type is actually configured on this site)
     * 
     * @param type the text type code to evaluate
     * @return {@code true} if the specified type code is among those reserved
     *         for use with local tracking attributes
     */
    public static boolean isLocalAttribute(int type) {
        return type >= LOCAL_TRACKING_BASE;
    }

    /**
     * Returns a set that contains a bunch of Integer objects, each of which
     * represents a valid (static) text type. The set does not include text
     * types for any localtracking attributes.
     * 
     * @return a {@code Collection} of text types.
     */
    public static Collection<Integer> getAllTextTypes() {
        return allTextTypes;
    }

    /**
     * A helper method that returns the first annotation of type
     * {@code PREFERRED_NAME} or null if none exist.
     * 
     * @param si the sample from which to get the annotation
     * @return the first {@code PREFERRED_NAME} annotation on the sample or null
     *         if there is none
     */
    public static SampleAnnotationInfo getExplicitlyPreferredName(
            SampleInfo si) {
        for (SampleAnnotationInfo ann : si.annotationInfo) {
            if (ann.type == PREFERRED_NAME) {
                return ann;
            }
        }
        return null;
    }

    /**
     * Gets a {@code Collection} of {@code SampleTextInfo} objects representing
     * each sample name for the given sample. This may include attributes of
     * type {@code COMMON_NAME} or {@code TRADE_NAME} as well as annotation of
     * type {@code IUPAC_NAME}. If the 'sortByPreference' parameter is set to
     * true, the resulting {@code Collection} will be in order from most
     * preferred sample name to least preferred sample name.
     * 
     * @param si the {@code SampleInfo} for the sample whose names are being
     *        returned
     * @param sortByPreference if true, causes the returned names to be in order
     *        of decreasing preference. In the current implementation this means
     *        that the previously designated {@code PREFERRED_NAME} will be the
     *        first {@code SampleTextInfo} followed by all other
     *        {@code COMMON_NAME} attributes (in their natural ordering), all
     *        other {@code TRADE_NAME} attributes (in their natural ordering)
     *        and all other {@code IUPAC_NAME} annotations (in their natural
     *        ordering).
     * @return a {@code Collection} of {@code SampleTextInfo} objects
     *         representing sample names (possibly sorted by preference); if
     *         there are no sample names for the provided sample this may be an
     *         empty {@code Collection}
     */
    public static Collection<SampleTextInfo> getSampleNames(SampleInfo si,
            boolean sortByPreference) {
        List<SampleTextInfo> sampleNames = new ArrayList<SampleTextInfo>();

        for (SampleAttributeInfo attribute : si.attributeInfo) {
            if ((attribute.type == COMMON_NAME) || (attribute.type == TRADE_NAME)) {
                sampleNames.add(attribute);
            }
        }
        
        for (SampleAnnotationInfo annotation : si.annotationInfo) {
            if (annotation.type == IUPAC_NAME) {
                sampleNames.add(annotation);
            }
        }
        
        if (sortByPreference) {
            Collections.sort(sampleNames, new SampleNameComparator(
                    SampleTextBL.getExplicitlyPreferredName(si)));
        }
        
        return sampleNames;
    }

    /**
     * Returns the {@code SampleTextInfo} for the preferred chemical formula for
     * the {@code SampleInfo} The return value may be null if there is no
     * chemical formula for this sample.
     * <p>
     * The current implementation returns the first
     * {@code EMPIRICAL_FORMULA_DERIVED} (if available), or the first
     * {@code EMPIRICAL_FORMULA_SINGLE_ION} (if available), or the first
     * {@code EMPIRICAL_FOMRULA_LESS_SOLVENT} (if available), or the first
     * {@code EMPIRICAL_FORMULA} (if available), or the first
     * {@code STRUCTURAL_FORMULA} (if available), or the first
     * {@code MOIETY_FORMULA} (if available), or null.
     * 
     * @param si a {@code SampleInfo}
     * @return a {@code SampleTextInfo} that is the preferred formula or null if
     *         no formlae exist for the given sample
     */
    public static SampleTextInfo getSamplePreferredFormula(SampleInfo si) {
        SampleTextInfo preferredFormula = null;
        boolean foundDerivedEmpirical = false;
        boolean foundSingleIonEmpirical = false;
        boolean foundLessSolventEmpirical = false;
        boolean foundObservedEmpirical = false;
        boolean foundStructural = false;
        boolean foundMoiety = false;

        for (SampleAttributeInfo attribute : si.attributeInfo) {
            if (attribute.value != null) {
                switch (attribute.type) {
                    case EMPIRICAL_FORMULA_DERIVED:
                        preferredFormula = attribute;
                        foundDerivedEmpirical = true;
                        break;
                    case EMPIRICAL_FORMULA_SINGLE_ION:
                        if (!foundDerivedEmpirical) {
                            preferredFormula = attribute;
                        }
                        foundSingleIonEmpirical = true;
                        break;
                    case EMPIRICAL_FORMULA_LESS_SOLVENT:
                        if (!foundDerivedEmpirical
                                && !foundSingleIonEmpirical) {
                            preferredFormula = attribute;
                        }
                        foundLessSolventEmpirical = true;
                        break;
                    case EMPIRICAL_FORMULA:
                        if (!foundDerivedEmpirical && !foundSingleIonEmpirical
                                && !foundLessSolventEmpirical) {
                            preferredFormula = attribute;
                        }
                        foundObservedEmpirical = true;
                        break;
                    case STRUCTURAL_FORMULA:
                        if (!foundDerivedEmpirical && !foundSingleIonEmpirical
                                && !foundLessSolventEmpirical
                                && !foundObservedEmpirical) {
                            preferredFormula = attribute;
                        }
                        foundStructural = true;
                        break;
                    case MOIETY_FORMULA:
                        if (!foundDerivedEmpirical && !foundSingleIonEmpirical
                                && !foundLessSolventEmpirical
                                && !foundObservedEmpirical
                                && !foundStructural) {
                            preferredFormula = attribute;
                        }
                        foundMoiety = true;
                        break;
                }
            }
        }
        return preferredFormula;
    }

    /**
     * Returns a "sanitized" copy of a specified {@code SampleInfo} object and
     * its child objects. Such a sanitized copy would be suitable for export to
     * another Reciprocal Net site. In the current implementation, the sanitized
     * object returned by this function differs from the {@code rawSample} in
     * the following aspects. 1. Its {@code mostRecentStatus} and
     * {@code mostRecentHistoryId} values are cleared because their only purpose
     * is to summarize the outcome of the sample's historical workflow. The
     * entire historical workflow generally is not available to remote sites. 2.
     * Its {@code historyId} field is cleared because it simply stores a foreign
     * key for the local site's {@code sampleHistory} db table. The foreign key
     * is useless to a remote site, whose data in its {@code sampleHistory}
     * table is undoubtedly different. 3. Its {@code isMoreRecentThanSearch}
     * field is cleared because a {@code true} value would make sense only in
     * the context of a call to {@code SampleManager.getSearchResults()}. 4.
     * All of the {@code SampleAccessInfo} records in its {@code accessInfo}
     * collection are removed from the collection. This is because the
     * {@code SampleAccessInfo} records contain a foreign key into the
     * {@code users} db table, and because users are scoped to individual sites
     * and the remote site's user records are undoubtedly different. 5. Within
     * the {@code attributeInfo} collection of {@code SampleAttributeInfo}
     * objects, each record whose {@code SampleAttributeInfo.type} field
     * identifies it is a local tracking attribute is removed. This is because
     * LTA's are scoped to individual sites (or, more properly, individual labs)
     * and there is no mechanism for replicating {@code LocalTrackingConfig}
     * objects among sites.
     * 
     * @return a deep copy of {@code rawSample} and child objects that has been
     *         sanitized.
     * @param rawSample the sample to be copied.
     */
    public static SampleInfo sanitizeSampleForExport(SampleInfo rawSample) {
        SampleInfo newSample = rawSample.clone();

        // Sanitize select single fields.
        newSample.mostRecentStatus = SampleWorkflowBL.INVALID_STATUS;
        newSample.historyId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        newSample.mostRecentHistoryId
                = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        newSample.isMoreRecentThanSearch = false;
        newSample.accessInfo.clear();

        // Remove all localtracking attributes.
        for (Iterator<SampleAttributeInfo> it
                = newSample.attributeInfo.iterator(); it.hasNext(); ) {
            SampleAttributeInfo attr = it.next();
            
            if (SampleTextBL.isLocalAttribute(attr.type)) {
                it.remove();
            }
        }

        return newSample;
    }

    /**
     * This {@code Comparator} may be used to sort sample name annotations and
     * attributes in order of preference. It is only for use between well
     * initialized {@code SampleTextInfo} objects that are of a type that is
     * known to be a sample name. This particular {@code Comparator} will throw
     * assertion failures if used improperly.
     */
    private static class SampleNameComparator implements
            Comparator<SampleTextInfo> {

        private SampleTextInfo explicitlyPreferredName;

        public SampleNameComparator(SampleTextInfo explicitlyPreferredName) {
            this.explicitlyPreferredName = explicitlyPreferredName;
        }

        public int compare(SampleTextInfo st1, SampleTextInfo st2) {

            assert ((st1 != null) && (st2 != null)
                    && (st1.value != null) && (st2.value != null)
                    && ((st1.type == COMMON_NAME) || (st1.type == TRADE_NAME)
                            || (st1.type == IUPAC_NAME))
                    && ((st2.type == COMMON_NAME) || (st2.type == TRADE_NAME)
                            || (st2.type == IUPAC_NAME)));
            if (st1.equals(st2)) {
                return 0;
            }
            if (this.explicitlyPreferredName != null) {
                if (st1.value.equals(explicitlyPreferredName.value)) {
                    return -100000;
                } else if (st2.value.equals(explicitlyPreferredName.value)) {
                    return 100000;
                }
            }
            if (st1.type == st2.type) {
                return st1.compareTo(st2);
            }
            switch (st1.type) {
                case COMMON_NAME:
                    return -1000;
                case TRADE_NAME:
                    if (st2.type == COMMON_NAME) {
                        return 1000;
                    } else {
                        return -1000;
                    }
                case IUPAC_NAME:
                    return 1000;
                default:
                    // can't happen because we allready
                    // asserted that this comparator will
                    // only be used on a small subset of
                    // possible SampleTextInfo objects
                    throw new AssertionError();
            }
        }
    }
}
