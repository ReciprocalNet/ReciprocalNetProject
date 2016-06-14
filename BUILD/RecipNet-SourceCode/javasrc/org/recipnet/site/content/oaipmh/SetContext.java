/*
 * Reciprocal Net Project
 * 
 * SetContext.java
 *  
 * 25-Oct-2005: jobollin wrote first draft by extracting methods from
 *              OaiPmhResponder 
 * 18-Nov-2005: jobollin removed support for the "SampleHostedOnSite" and 
 *              "Authoritative" sets; they produced an inconsistent view of
 *              the site network, and were never fully implemented anyway 
 * 11-May-2006: jobollin switched a reference from
 *              SiteManagerRemote.getLocalLabs2()
 *              to SiteManagerRemote.getLocalLabs()
 */

package org.recipnet.site.content.oaipmh;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.SiteManagerRemote;
import org.recipnet.site.shared.db.LabInfo;
import org.recipnet.site.shared.db.ProviderInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.search.LabSC;
import org.recipnet.site.shared.search.ProviderSC;
import org.recipnet.site.shared.search.SearchConstraint;
import org.recipnet.site.wrapper.CoreConnector;

/**
 * A utility class than manages OAI sets
 *   
 * @author jobollin
 * @version 0.9.0
 */
public class SetContext {

    /**
     * The {@code CoreConnector} this context should use to obtain set
     * information from the Reciprocal Net core
     */
    private final CoreConnector coreConnector;
    
    /**
     * Initializes a {@code SetContext} with the specified
     * {@code CoreConnector}
     * 
     * @param  connector the {@code CoreConnector} this context should use to
     *         obtain set information from the Reciprocal Net core
     */
    public SetContext(CoreConnector connector) {
        coreConnector = connector;
    }
    
    /**
     * <p>
     * Returns a map of set identifiers to descriptions that represents a
     * complete list of OAI "sets" supported by this servlet.  Each set in the
     * list falls into one of the following categories:
     * </p>
     * <ol>
     *   <li>one set for each known lab.</li>
     *   <li>one set for each known provider.</li>
     * </ol>
     * <p>
     * Identifiers for sets from category 1 are prefaced with 'L' and sets
     * from category 2 are prefaced with 'P'.  This function requires two
     * round-trips to core.
     * </p>
     * 
     * @return a {@code Map&lt;String, String&gt;} from supported set names to
     *         corresponding descriptions
     *  
     * @throws OperationFailedException if the operation could not be completed
     *      because of a low-level error.
     * @throws RemoteException on RMI error.
     */
    public Map<String, String> getSets() throws OperationFailedException,
            RemoteException {
        HashMap<String, String> sets = new HashMap<String, String>();
        SiteManagerRemote siteManager = coreConnector.getSiteManager();

        // Iterate through every known lab to discover sets in category 1.
        int[] localLabIds = siteManager.getLocalLabs();
        for (int i = 0; i < localLabIds.length; i++) {
            LabInfo localLab = siteManager.getLabInfo(localLabIds[i]);
            sets.put(createLabSetSpec(localLab.id),
                    "Samples associated with laboratory " + localLab.name);

            // Iterate through every known provider under the current lab to
            // discover sets in category 2.
            ProviderInfo providers[] =
                    siteManager.getAllProviderInfo(localLabIds[i]);
            for (int j = 0; j < providers.length; j++) {
                sets.put(createProviderSetSpec(providers[j].id),
                        "Samples provided by " + localLab.name + ":"
                        + providers[j].name);
            }
        }

        return sets;
    }
    
    /**
     * Returns {@code true} if the specified {@code set} identifier represents a
     * set recognized by this repository.  A list of all valid sets can be
     * obtained via {@link #getSets()}.
     * 
     * @param  set the set identifier to test 
     * 
     * @return {@code true} if the set identifier is valid, {@code false} if not
     *  
     * @throws OperationFailedException if an attempt to retrieve required
     *         information from the Reciprocal Net core fails with this
     * @throws RemoteException if an attempt to communicate with core fails
     *         with this exception
     */
    public boolean isSetValid(String set) throws OperationFailedException,
            RemoteException {
        return getSets().containsKey(set);
    }

    /**
     * Returns an array of the set specification strings that apply to the
     * specified sample
     * 
     * @param  sample a {@code SampleInfo} representing the sample for which
     *         set specs are requested
     *         
     * @return a {@code String[]} containing the set specs for the specified
     *         sample
     */
    public String[] getSetSpecsForSample(SampleInfo sample) {
        return new String[] {
                getLabSetSpec(sample), getProviderSetSpec(sample)
        };
    }
    
    /**
     * Constructs a {@code SearchConstraint} approrpiate for constraining search
     * results to those belonging to the specified OAI set
     *  
     * @param  set the name of the set for which a constraint is requested
     * 
     * @return a {@code SearchConstraint} for restricting a search to members of
     *         the specified set
     * 
     * @throws IllegalArgumentException if the specified set name does not
     *         correspond to a set supported by this set context
     */
    public SearchConstraint getConstraintForSet(String set) {
        try {
            if (set.charAt(0) == 'L') {
                
                // the 'set' specified tells us to filter by lab
                return new LabSC(Integer.parseInt(set.substring(1)));
            } else if (set.charAt(0) == 'P') {
                
                // the 'set' specified tells us to filter by provider
                return new ProviderSC(Integer.parseInt(set.substring(1)));
            } else {
                throw new IllegalArgumentException("Invalid set");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid set");
        } catch (IndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Invalid set");
        }
    }
    
    /**
     * Returns a set specification string for the laboratory to which the
     * specified sample is assigned
     * 
     * @param  sampleInfo a {@code SampleInfo} representing the sample for which
     *         a laboratory set specification is requested
     *         
     * @return the set specification as a {@code String}
     */
    public String getLabSetSpec(SampleInfo sampleInfo) {
        return createLabSetSpec(sampleInfo.labId);
    }
    
    /**
     * Creates a lab set specification string for the specified lab ID
     * 
     * @param  labId the id of the laboratory for which a set spec is requested
     * 
     * @return the set specification {@code String} for the specified lab ID
     */
    private String createLabSetSpec(int labId) {
        
        /*
         * Note: if this set specification format is modified then corresponding
         * modifications will be needed in getConstraintForSet(String)
         */
        return ("L" + labId);
    }
    
    /**
     * Returns a set specification string for the sample provider which the
     * specified sample is associated
     * 
     * @param  sampleInfo a {@code SampleInfo} representing the sample for which
     *         a provider set specification is requested
     *         
     * @return the set specification as a {@code String}
     */
    public String getProviderSetSpec(SampleInfo sampleInfo) {
        return createProviderSetSpec(sampleInfo.dataInfo.providerId);
    }
    
    /**
     * Creates a provider set specification string for the specified provider ID
     * 
     * @param  providerId the id of the provider for which a set spec is
     *         requested
     * 
     * @return the set specification {@code String} for the specified provider
     *         ID
     */
    private String createProviderSetSpec(int providerId) {
        
        /*
         * Note: if this set specification format is modified then corresponding
         * modifications will be needed in getConstraintForSet(String)
         */
        return ("P" + providerId);
    }
}
