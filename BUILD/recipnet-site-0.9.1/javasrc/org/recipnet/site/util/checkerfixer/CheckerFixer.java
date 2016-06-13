/*
 * Reciprocal Net project
 * 
 * CheckerFixer.java
 *
 * 17-Dec-2002: ekoperda wrote first draft
 * 18-Feb-2002: eisiorho added more checks, function
 *              generateWhereClauseForTextType()
 * 26-Jun-2003: midurbin renamed RAW_EDITED constant.
 * 18-Jul-2003: ekoperda modified main() to utilize new config directive
 *              DbUrlForBootstrap
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 08-Aug-2004: cwestnea modified main() to use SampleWorkflowBL
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 05-Jan-2005: midurbin modified main() to use new REVERTED action codes
 * 06-Oct-2005: midurbin replaced references to the sample table's
 *              'provider_id' column with 'current_provider_id'
 * 07-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.util.checkerfixer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.RepositoryHoldingInfo;
import org.recipnet.site.shared.db.SampleAccessInfo;
import org.recipnet.site.shared.db.UserInfo;

/**
 * A command-line utility that runs <em>off-line</em> by talking to the
 * database engine directly. The core modules must not be running when this
 * utility is launched! The utility runs a number of scans against the recipnet
 * database and reports any erroreous or possibly corrupt data.
 */
public class CheckerFixer {
    public static void main(String args[]) {
        try {
            
            /*
             * The first command-line parameter tells us where to find our
             * configuration file. It should always be of the form:
             * 
             *     --configfile=/etc/filename
             */
            if (args.length != 1 || !args[0].startsWith("--configfile=")) {
                displaySyntax();
                System.exit(1);
            }
            String configFile = args[0].substring("--configfile=".length());

            // Display a banner message and obtain user confirmation
            System.out.println("Reciprocal Net project - checkerfixer utility");
            System.out.println("");
            System.out.println("This program will connect to the database used"
                    + " by recipnetd and");
            System.out.println("scan it for errors or inconsistencies. "
                    + " recipnetd must not be");
            System.out.println("running or database corruption might"
                    + " result.  Are you sure");
            System.out.print("you want to proceed (y, n)? ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            String answer = reader.readLine();
            if (!"y".equals(answer)) {
                System.exit(1); // why exit(1), instead of exit(0)
            }
            System.out.println("");

            // Read and parse the configuration file.
            System.out.print("Reading config file... ");
            BufferedInputStream is = new BufferedInputStream(
                    new FileInputStream(configFile));
            Properties properties = new Properties();
            properties.load(is);
            is.close();
            System.out.println("ok");

            // Connect to the database engine using Site Manager's credentials
            System.out.print("Initializing db driver... ");
            Class.forName(properties.getProperty("DbDriverClassName"));
            System.out.println("ok");
            System.out.print("Connecting to database... ");
            Connection conn = DriverManager.getConnection(
                    properties.getProperty("DbUrlForBootstrap"),
                    properties.getProperty("SitDbUsername"),
                    properties.getProperty("SitDbPassword"));
            System.out.println("ok");

            boolean errorsDetected = false;
            errorsDetected |= reportIfExists(
                    conn,
                    "Checking for corruption in sites table",
                    "CORRUPT SITE detected - id",
                    "SELECT id FROM sites"
                            + " WHERE name='' OR shortName='' OR baseUrl=''"
                            + " OR repositoryUrl='' OR publicKey IS NULL OR ts IS NULL"
                            + " OR publicSeqNum < 0 OR privateSeqNum < 0"
                            + " OR ts=0 OR ts IS NULL;");
            /*
             * TODO: also use ISMs received to verify that sites table is valid,
             * also try connecting to remote sites to ensure that URL's stored
             * are valid.
             */
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in labs table",
                    "CORRUPT LAB detected - id", "SELECT id FROM labs"
                            + " WHERE name='' OR name IS NULL"
                            + " OR shortName='' OR shortName IS NULL"
                            + " OR directoryName='' OR directoryName IS NULL"
                            + " OR homeUrl='' OR defaultCopyrightNotice=''"
                            + " OR homeSite_id IS NULL OR ts=0 OR ts IS NULL;");
            errorsDetected |= reportIfExists(conn, "Matching labs to sites",
                    "NONEXISTENT SITE for lab id",
                    "SELECT l.id FROM labs l LEFT JOIN sites s"
                            + " ON l.homeSite_id=s.id" + " WHERE s.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in providers table",
                    "CORRUPT PROVIDER detected - id",
                    "SELECT id FROM providers"
                            + " WHERE lab_id IS NULL OR active IS NULL"
                            + " OR name='' OR headContact='' OR comments=''"
                            + " OR ts=0 OR ts IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching providers to labs",
                    "NONEXISTENT LAB for provider id",
                    "SELECT p.id FROM providers p LEFT JOIN labs l"
                            + " ON p.lab_id=l.id" + " WHERE l.id IS NULL;");
            errorsDetected |= reportIfExists(
                    conn,
                    "Checking for corruption in users table",
                    "CORRUPT USER detected - id",
                    "SELECT id FROM users"
                            + " WHERE (lab_id IS NULL AND provider_id IS NULL)"
                            + " OR (lab_id IS NOT NULL AND provider_id IS NOT NULL)"
                            + " OR active IS NULL OR creationDate IS NULL"
                            + " OR (active=0 AND inactiveDate IS NULL)"
                            + " OR fullname='' OR username=''"
                            + " OR globalAccessLevel IS NULL"
                            + " OR globalAccessLevel < 0 OR globalAccessLevel > "
                            + (UserInfo.LAB_SCIENTIFIC_USER_ACCESS
                                    + UserInfo.LAB_ADMIN_ACCESS + UserInfo.SITE_ADMIN_ACCESS)
                            + " OR ts=0 OR ts IS NULL;");
            errorsDetected |= reportIfExists(conn, "Matching users to labs",
                    "NONEXISTENT LAB for user id",
                    "SELECT u.id FROM users u LEFT JOIN labs l"
                            + " ON u.lab_id=l.id"
                            + " WHERE u.lab_id IS NOT NULL AND l.id IS NULL;");
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching users to providers",
                    "NONEXISTENT PROVIDER for user id",
                    "SELECT u.id FROM users u LEFT JOIN providers p"
                            + " ON u.provider_id=p.id"
                            + " WHERE u.provider_id IS NOT NULL AND p.id IS NULL;");
            errorsDetected |= reportIfExists(
                    conn,
                    "Checking for corruption in samples table",
                    "CORRUPT SAMPLE detected - id",
                    "SELECT id FROM samples"
                            + " WHERE lab_id IS NULL OR current_provider_id IS NULL"
                            + " OR localLabId IS NULL OR localLabId=''"
                            + " OR current_sampleHistory_id IS NULL OR NOT ("
                            + "     status=" + SampleWorkflowBL.PENDING_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.REFINEMENT_PENDING_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.SUSPENDED_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.COMPLETE_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.COMPLETE_PUBLIC_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.INCOMPLETE_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.NON_SCS_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.NON_SCS_PUBLIC_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.RETRACTED_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.RETRACTED_PUBLIC_STATUS
                            + "     OR status="
                            + SampleWorkflowBL.WITHDRAWN_STATUS
                            + "     OR status=" + SampleWorkflowBL.NOGO_STATUS
                            + ");");
            errorsDetected |= reportIfExists(conn, "Matching samples to labs",
                    "NONEXISTENT LAB for sample id",
                    "SELECT s.id FROM samples s LEFT JOIN labs l"
                            + " ON s.lab_id=l.id" + " WHERE l.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching samples to providers",
                    "NONEXISTENT PROVIDER for sample id",
                    "SELECT s.id FROM samples s LEFT JOIN providers p"
                            + " ON s.current_provider_id=p.id"
                            + " WHERE p.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching samples to providers",
                    "NONEXISTENT PROVIDER for sample id",
                    "SELECT s.id FROM samples s LEFT JOIN providers p"
                            + " ON s.current_provider_id=p.id"
                            + " WHERE p.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching samples to versions",
                    "MISSING CURRENT HISTORY RECORD for sample id",
                    "SELECT s.id FROM samples s LEFT JOIN sampleHistory sh"
                            + " ON s.current_sampleHistory_id=sh.id"
                            + "   AND s.id=sh.sample_id"
                            + " WHERE sh.id IS NULL;");
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching current versions of samples to highest versions",
                    "MISMATCHED CURRENT VERSION for sample id",
                    "SELECT s.id, s.current_sampleHistory_id, MAX(sh.id)"
                            + " FROM samples s"
                            + " LEFT JOIN sampleHistory sh ON s.id=sh.sample_id"
                            + " GROUP BY s.id, s.current_sampleHistory_id"
                            + " HAVING NOT (s.current_sampleHistory_id=MAX(sh.id));");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in sampleHistory table",
                    "CORRUPT SAMPLEHISTORY detected - id",
                    "SELECT id FROM sampleHistory"
                            + " WHERE sample_id IS NULL OR action IS NULL"
                            + " OR newStatus IS NULL OR date IS NULL"
                            + " OR clientIp='' OR comments='' OR NOT ("
                            + "     newStatus="
                            + SampleWorkflowBL.PENDING_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.REFINEMENT_PENDING_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.SUSPENDED_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.COMPLETE_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.COMPLETE_PUBLIC_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.INCOMPLETE_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.INCOMPLETE_PUBLIC_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.NON_SCS_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.NON_SCS_PUBLIC_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.RETRACTED_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.RETRACTED_PUBLIC_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.WITHDRAWN_STATUS
                            + "     OR newStatus="
                            + SampleWorkflowBL.NOGO_STATUS + " ) OR NOT ("
                            + "     action=" + SampleWorkflowBL.SUBMITTED
                            + "     OR action="
                            + SampleWorkflowBL.PRELIMINARY_DATA_COLLECTED
                            + "     OR action="
                            + SampleWorkflowBL.STRUCTURE_REFINED
                            + "     OR action="
                            + SampleWorkflowBL.RELEASED_TO_PUBLIC
                            + "     OR action="
                            + SampleWorkflowBL.MODIFIED_TEXT_FIELDS
                            + "     OR action="
                            + SampleWorkflowBL.MODIFIED_LTAS
                            + "     OR action="
                            + SampleWorkflowBL.CITATION_ADDED
                            + "     OR action=" + SampleWorkflowBL.RETRACTED
                            + "     OR action=" + SampleWorkflowBL.SUSPENDED
                            + "     OR action=" + SampleWorkflowBL.RESUMED
                            + "     OR action="
                            + SampleWorkflowBL.DEPRECATED_RAW_EDITED
                            + "     OR action=" + SampleWorkflowBL.CHANGED_ACL
                            + "     OR action="
                            + SampleWorkflowBL.REVERTED_WITHOUT_FILES
                            + "     OR action="
                            + SampleWorkflowBL.REVERTED_INCLUDING_FILES
                            + "     OR action=" + SampleWorkflowBL.IMPORTED
                            + "     OR action=" + SampleWorkflowBL.DB_REBUILT
                            + "     OR action="
                            + SampleWorkflowBL.DECLARED_INCOMPLETE
                            + "     OR action="
                            + SampleWorkflowBL.DECLARED_NON_SCS
                            + "     OR action="
                            + SampleWorkflowBL.FAILED_TO_COLLECT
                            + "     OR action="
                            + SampleWorkflowBL.WITHDRAWN_BY_PROVIDER + ");");
            errorsDetected |= reportIfExists(conn,
                    "Matching sample versions to samples",
                    "NONEXISTANT SAMPLE for sample version id",
                    "SELECT sh.id FROM sampleHistory sh"
                            + " LEFT JOIN samples s ON sh.sample_id=s.id"
                            + " WHERE s.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in searchAtoms table",
                    "CORRUPT atom detected - id", "SELECT id FROM searchAtoms"
                            + " WHERE element='' OR element IS NULL"
                            + " OR count=0 OR count IS NULL;");
            errorsDetected |= reportIfExists(conn, "Matching atoms to samples",
                    "NONEXISTANT SAMPLE for atom id",
                    "SELECT sa.id FROM searchAtoms sa"
                            + " LEFT JOIN samples s ON sa.sample_id=s.id"
                            + " WHERE s.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in searchLocalHoldings table",
                    "CORRUPT local holding detected - sample id",
                    "SELECT sample_id FROM searchLocalHoldings"
                            + " WHERE NOT (repositoryHoldings_replicaLevel="
                            + RepositoryHoldingInfo.FULL_DATA
                            + " OR repositoryHoldings_replicaLevel="
                            + RepositoryHoldingInfo.BASIC_DATA + ");");
            errorsDetected |= reportIfExists(conn,
                    "Matching local holdings to samples",
                    "NONEXISTANT SAMPLE for local holding, sample id",
                    "SELECT slh.sample_id FROM searchLocalHoldings slh"
                            + " LEFT JOIN samples s ON slh.sample_id=s.id"
                            + " WHERE s.id IS NULL;");
            /*
             * TODO: for consistency's sake in the query below, we ought to
             * cosntrain the join to those rows in repositoryHoldings where
             * site_id is the local site. This currently is impractical, though,
             * because we lack easy access to the local site's id.
             */
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching local holdings to repository holdings",
                    "NONEXISTANT HOLDINGS for local holding sample id",
                    "SELECT slh.sample_id FROM searchLocalHoldings slh"
                            + " LEFT JOIN repositoryHoldings rh"
                            + " ON slh.sample_id=rh.sample_id"
                            + " AND slh.repositoryHoldings_replicaLevel=rh.replicaLevel"
                            + " WHERE rh.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in sampleData table",
                    "CORRUPT sample data detected - sample version",
                    "SELECT first_sampleHistory_id FROM sampleData"
                            + " WHERE spgp='' OR color='' OR summary='';");
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching sample data to sample versions",
                    "NONEXISTANT SAMPLE HISTORY ID for sample data version",
                    "SELECT sd.first_sampleHistory_id FROM sampleData sd"
                            + " LEFT JOIN sampleHistory sh1"
                            + "   ON sd.sample_id=sh1.sample_id"
                            + "     AND sd.first_sampleHistory_id=sh1.id"
                            + " LEFT JOIN sampleHistory sh2"
                            + "   ON sd.sample_id=sh2.sample_id"
                            + "     AND sd.last_sampleHistory_id=sh2.id"
                            + " WHERE sh1.id IS NULL"
                            + "   OR (sh2.id IS NULL"
                            + "       AND sd.last_sampleHistory_id IS NOT NULL);");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in sampleAcls table",
                    "CORRUPT sample ACL entry detected - id",
                    "SELECT id FROM sampleAcls" + " WHERE user_id IS NULL"
                            + " OR NOT (accessLevel="
                            + SampleAccessInfo.READ_ONLY
                            + "         OR accessLevel="
                            + SampleAccessInfo.READ_WRITE + ");");
            errorsDetected |= reportIfExists(conn,
                    "Matching sample ACL entries to samples",
                    "NONEXISTANT SAMPLE for sample ACL entry id",
                    "SELECT sacl.id FROM sampleAcls sacl"
                            + " LEFT JOIN samples s ON sacl.sample_id=s.id"
                            + " WHERE s.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching sample ACL entries to users",
                    "NONEXISTANT USER for sample ACL entry id",
                    "SELECT sacl.id FROM sampleAcls sacl"
                            + " LEFT JOIN users u ON sacl.user_id=u.id"
                            + " WHERE u.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in sample annotations table",
                    "CORRUPT sample annotation detected - id",
                    "SELECT id FROM sampleAnnotations"
                            + " WHERE value='' OR NOT ("
                            + generateWhereClauseForTextType(false, true)
                            + ");");
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching sample annotation to sample versions",
                    "NONEXISTANT SAMPLE HISTORY ID for sample annotation id",
                    "SELECT sa.id FROM sampleAnnotations sa"
                            + " LEFT JOIN sampleHistory sh1"
                            + "   ON sa.sample_id=sh1.sample_id"
                            + "     AND sa.first_sampleHistory_id=sh1.id"
                            + " LEFT JOIN sampleHistory sh2"
                            + "   ON sa.sample_id=sh2.sample_id"
                            + "     AND sa.last_sampleHistory_id=sh2.id"
                            + " WHERE sh1.id IS NULL"
                            + "   OR (sh2.id IS NULL"
                            + "       AND sa.last_sampleHistory_id IS NOT NULL);");
            errorsDetected |= reportIfExists(conn,
                    "Matching sample annotation references to samples",
                    "NONEXISTANT SAMPLE for sample annotation id",
                    "SELECT sa.id FROM sampleAnnotations sa"
                            + " LEFT JOIN samples s ON sa.referenceSample=s.id"
                            + " WHERE s.id IS NULL"
                            + " AND sa.referenceSample IS NOT NULL;");
            /*
             * TODO: The query below should check for localtracking types
             * explicitly rather than assuming all types above
             * LOCAL_TRACKING_BASE are valid.
             */
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in sampleAttributes table",
                    "CORRUPT sample attribute detected - id",
                    "SELECT id FROM sampleAttributes"
                            + " WHERE value='' OR NOT ("
                            + generateWhereClauseForTextType(true, false)
                            + " OR type>=" + SampleTextBL.LOCAL_TRACKING_BASE
                            + ");");
            errorsDetected |= reportIfExists(
                    conn,
                    "Matching sample attributes to sample versions",
                    "NONEXISTANT SAMPLE HISTORY ID for sample attribute id",
                    "SELECT sa.id FROM sampleAttributes sa"
                            + " LEFT JOIN sampleHistory sh1"
                            + "   ON sa.sample_id=sh1.sample_id"
                            + "     AND sa.first_sampleHistory_id=sh1.id"
                            + " LEFT JOIN sampleHistory sh2"
                            + "   ON sa.sample_id=sh2.sample_id"
                            + "     AND sa.last_sampleHistory_id=sh2.id"
                            + " WHERE sh1.id IS NULL"
                            + "   OR (sh2.id IS NULL"
                            + "       AND sa.last_sampleHistory_id IS NOT NULL);");
            errorsDetected |= reportIfExists(conn,
                    "Checking for corruption in repository holding table",
                    "CORRUPT repository holding detected - id",
                    "SELECT id FROM repositoryHoldings" + " WHERE urlPath=''"
                            + " OR NOT (replicaLevel="
                            + RepositoryHoldingInfo.FULL_DATA
                            + " OR replicaLevel="
                            + RepositoryHoldingInfo.BASIC_DATA + ");");
            errorsDetected |= reportIfExists(conn,
                    "Matching repository holdings to samples",
                    "NONEXISTANT SAMPLE for repository holding id",
                    "SELECT rh.id FROM repositoryHoldings rh"
                            + " LEFT JOIN samples s ON rh.sample_id=s.id"
                            + " WHERE s.id IS NULL;");
            errorsDetected |= reportIfExists(conn,
                    "Matching repository holdings to sites",
                    "NONEXISTANT SITE for repository holding id",
                    "SELECT rh.id FROM repositoryHoldings rh"
                            + " LEFT JOIN sites s ON rh.site_id=s.id"
                            + " WHERE s.id IS NULL;");

            if (!errorsDetected) {
                System.out.println("Check finished.  No errors detected.");
            } else {
                System.out.println("Check finished with errors.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(2);
        }
    }

    private static void displaySyntax() {
        System.out.println("Bad syntax.");
        System.out.println("Correct syntax:"
                + " recipnet-checkerfixer --configfile=...");
    }

    private static boolean reportIfExists(Connection conn,
            String statusMessage, String errorMessage, String sql)
            throws SQLException {
        System.out.print(statusMessage + "...");
        Statement cmd = conn.createStatement();
        ResultSet rs = cmd.executeQuery(sql);
        boolean gotRows = false;
        while (rs.next()) {
            /*
             * Just display the value of the first column in the resultset;
             * we'll assume this is the primary key
             */
            long id = rs.getLong(1);
            if (!gotRows) {
                System.out.println("");
            }
            System.out.println("  " + errorMessage + " " + id);
            gotRows = true;
        }
        cmd.close();
        System.out.println("  done");
        return gotRows;
    }

    /**
     * Returns a string suitable for use inside a WHERE clause of a SQL query
     * where the 'type' field is equal to one of the defined attribute or
     * annotation text types (defined by {@code SampleTextInfo}). Such a query
     * might be used by the caller to match rows in the {@code sampleAttributes}
     * or {@code sampleAnnotations} table that have invalid values in the 'type'
     * column.
     * 
     * @param matchAttribute set to {@code true} if the query string returned
     *        should match texttype values that represent attributes.
     * @param matchAnnotation set to {@code true} if the query string returned
     *        should match texttype values that represent annotations.
     */
    private static String generateWhereClauseForTextType(
            boolean matchAttribute, boolean matchAnnotation) {
        StringBuilder whereClauseFragment = new StringBuilder();
        boolean isFirstTime = true;
        
        for (int type : SampleTextBL.getAllTextTypes()) {
            if (matchAnnotation && SampleTextBL.isAnnotation(type)
                    || matchAttribute && SampleTextBL.isAttribute(type)) {
                if (!isFirstTime) {
                    whereClauseFragment.append(" OR ");
                } else {
                    isFirstTime = false;
                }
                whereClauseFragment.append("type=" + type);
            }
        }
        
        return whereClauseFragment.toString();
    }
}
