/*
 * Reciprocal Net project
 * 
 * SearchTableTracker.java
 *
 * 25-Feb-2005: midurbin wrote first draft
 * 22-Mar-2005: ekoperda updated constructor to support new db table
 *              'searchUnitCells'
 * 12-Jul-2005: ekoperda updated constructor to support new db table 
 *              'searchSpaceGroups'
 * 30-May-2006: jobollin reformatted the source, added generics
 */

package org.recipnet.site.shared.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * A class that manages the use and reuse of aliased joined tables for a
 * database query generated for various {@code SearchConstraint} objects.
 * </p><p>
 * The rule for determining whether two separate constraints may use the same
 * table alias is: If a table alias exists for a table that has multiple rows
 * for a given sample version, that has not been used by another constraint that
 * has a common AND (or NAND) group in its ancestry, it may be reused for this
 * constraint. This ensures that when the AND and OR operations are distributed
 * (flattening the hierarchy), any constraints that share the same table that
 * are ANDed together will result in another LEFT JOIN of that particular table.
 * </p>
 */
public class SearchTableTracker {

    /**
     * A {@code Map} whose keys are {@code SearchConstraint}s and whose values
     * are those keys' parent {@code SearchConstraint}s. This map is populated
     * by the constructor to include every descendant of the the provided
     * {@code SearchConstraint}.
     */
    private Map<SearchConstraint, SearchConstraint> childToParentMap;

    /**
     * A mapping of known table names (String objects) to {@code TableRecord}
     * objects for that table. This map does not deal with aliases, but simply
     * contains static information about the database. Alias information is
     * maintained by the {@code TableRecord} objects contained as the values in
     * this map. This {@code Map} is populated by the constructor with all the
     * tables known to be used by searches except the "samples" table which is
     * included in every search by default.
     */
    private Map<String, TableRecord> tableNamesToRecordsMap;

    /**
     * The constructor; instantiates {@code TableRecord} objects and inserts
     * them into the 'tableNamesToRecordsMap'.
     */
    public SearchTableTracker(SearchConstraint head) {
        this.tableNamesToRecordsMap = new HashMap<String, TableRecord>();
        this.tableNamesToRecordsMap.put("sampleAcls", new TableRecord(
                "sampleAcls", "acl", true, "sample_id", null));
        this.tableNamesToRecordsMap.put("sampleData",
                new TableRecord("sampleData", "sd", false, "sample_id",
                        "last_sampleHistory_id"));
        this.tableNamesToRecordsMap.put("searchLocalHoldings", new TableRecord(
                "searchLocalHoldings", "shold", false, "sample_id", null));
        this.tableNamesToRecordsMap.put("searchAtoms", new TableRecord(
                "searchAtoms", "satoms", true, "sample_id", null));
        this.tableNamesToRecordsMap.put("sampleAnnotations", new TableRecord(
                "sampleAnnotations", "san", true, "sample_id",
                "last_sampleHistory_id"));
        this.tableNamesToRecordsMap.put("sampleAttributes", new TableRecord(
                "sampleAttributes", "sat", true, "sample_id",
                "last_sampleHistory_id"));
        this.tableNamesToRecordsMap.put("searchUnitCells", new TableRecord(
                "searchUnitCells", "sruc", false, "sample_id", null));
        this.tableNamesToRecordsMap.put("searchSpaceGroups", new TableRecord(
                "searchSpaceGroups", "ssg", false, "sample_id", null));

        /*
         * we must use the IdentityHashMap because nodes that are equal and have
         * equal hashcodes may have different positions in the tree and thus
         * different parents.
         */
        this.childToParentMap
                = new IdentityHashMap<SearchConstraint, SearchConstraint>();
        SearchTableTracker.addParentMappingsForChildren(head,
                this.childToParentMap);
    }

    /**
     * Gets an alias that may be used to reference the given table. If the
     * provided table is "samples", "s" is returned without consulting the
     * {@code tableNamesToRecordsMap}
     * 
     * @param tableName the name of the database table to be accessed
     * @param self a reference to the calling {@code SearchConstraint}; used to
     *        determine whether the alias needed may be reused by other
     *        {@code SearchConstraint} objects.
     * @throws IllegalArgumentException if the indicated table has not been
     *         added to the {@code tableNamesToRecordsMap} map, meaning it it is
     *         unknown to this class.
     */
    public String getTableAlias(String tableName, SearchConstraint self) {
        if (tableName.equals("samples")) {
            return "s";
        }
        TableRecord tr = this.tableNamesToRecordsMap.get(tableName);

        if (tr == null) {
            throw new IllegalArgumentException();
        } else {
            return tr.getAlias(self, childToParentMap);
        }
    }

    /**
     * Generates a {@code String} representing a portion of an SQL query that
     * indicates the tables from which to select entries. The {@code String}
     * returned by this method may be inserted after the word "FROM" and before
     * the "WHERE" clause in an SQL statement, and all alias names returned by
     * previous invocations of {@code getTableAlias()} will be reflected. The
     * table "samples" is always included first with the alias "s".
     * 
     * @return a {@code String} that may be used as part of an SQL query
     */
    public String getSqlFromClause() {
        StringBuilder sb = new StringBuilder("samples s");
        
        for (TableRecord currentTable : this.tableNamesToRecordsMap.values()) {
            for (String currentAlias : currentTable.getAllUsedAliases()) {
                sb.append(" LEFT JOIN " + currentTable.tableName + " "
                        + currentAlias + " ON s.id=" + currentAlias + "."
                        + currentTable.sampleIdColumnName);
                if (currentTable.sampleHistoryIdColumnName != null) {
                    sb.append(" AND " + currentAlias + "."
                            + currentTable.sampleHistoryIdColumnName
                            + " IS NULL");
                }
            }
        }
        
        return sb.toString();
    }

    /**
     * A helper method to determine whether both of the constraints must be
     * true. Note that this method is somewhat misnamed, as it returns
     * {@code true} in the case of constraints joined by NAND
     * 
     * @param sc1 the first {@code SearchConstraint}
     * @param sc2 the second {@code SearchConstraint}
     * @return true if the two parameters are connected by an AND/NAND group,
     *         otherwise false
     * @throws IllegalStateException if the most immediate
     *         {@code SearchConstraint} from which both nodes descend is not a
     *         known group or relates its peers in an unknown way. The current
     *         implementation knows only {@code SearchConstraintGroup} and its 4
     *         operators.
     */
    private static boolean mustBothConstraintsBeMet(
            Map<SearchConstraint, SearchConstraint> childToParentMap,
            SearchConstraint sc1, SearchConstraint sc2) {
        /*
         * create a sequence of nodes from the head to first constraint The list
         * is populated starting with sc1, but we add to the beginning of the
         * list in order to reverse the order.
         */
        LinkedList<SearchConstraint> headToSc1
                = new LinkedList<SearchConstraint>();
        for (SearchConstraint current = sc1; current != null;
                current = childToParentMap.get(current)) {
            headToSc1.addFirst(current);
        }

        /*
         * create a sequence of nodes from the head to the second constraint The
         * list is populated starting with sc2, but we add to the beginning of
         * the list in order to reverse the order.
         */
        LinkedList<SearchConstraint> headToSc2
                = new LinkedList<SearchConstraint>();
        for (SearchConstraint current = sc2; current != null;
                current = childToParentMap.get(current)) {
            headToSc2.addFirst(current);
        }

        // find the deepest common node

        // Both chains should start at the head
        assert headToSc2.getFirst() == headToSc1.getFirst();

        SearchConstraint nearestCommonAncestor = headToSc1.getFirst();
        Iterator<SearchConstraint> it1 = headToSc1.iterator();
        Iterator<SearchConstraint> it2 = headToSc2.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            SearchConstraint next = it1.next();

            if (it2.next() == next) {
                nearestCommonAncestor = next;
            } else {
                if (nearestCommonAncestor instanceof SearchConstraintGroup) {
                    switch (((SearchConstraintGroup) nearestCommonAncestor).getOperator()) {
                        case SearchConstraintGroup.OR:
                        case SearchConstraintGroup.NOR:
                            return false;
                        case SearchConstraintGroup.AND:
                        case SearchConstraintGroup.NAND:
                            return true;
                        default:
                            throw new IllegalStateException();
                    }
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        return false;
    }

    /**
     * A recursive method to populate a suplied map with mappings from child
     * nodes to their parent. If the head of a tree is supplied as the 'parent',
     * then every node in the tree will added as keys, with their parent as a
     * value except for the head. In such cases a future call to
     * {@code Map.get()} will return null for the head node or any nodes not
     * entered into the {@code Map}.
     * 
     * @param parent a {@code SearchConstraint} that will be the value for the
     *        key representing each child.
     * @param childToParentMap a {@code Map} that will contain the mappings
     */
    private static void addParentMappingsForChildren(SearchConstraint parent,
            Map<SearchConstraint, SearchConstraint> childToParentMap) {
        if (parent == null) { // FIXME: when should this ever happen?
            return;
        } else {
            for (SearchConstraint child : parent.getChildren()) {
                childToParentMap.put(child, parent);
                addParentMappingsForChildren(child, childToParentMap);
            }
        }
    }

    /**
     * A class that describes a database table and keeps usage information so
     * that it may provide an alias for use by an arbitrary
     * {@code SearchConstraint} that must access the table.
     */
    private static class TableRecord {

        /** The name of the table. */
        private String tableName;

        /**
         * A unique abbreviation of the table name; used to generate alias
         * names.
         */
        private String tableAliasBase;

        /**
         * Indicates whether the particular table described by this
         * {@code TableRecord} may have more than one row associated with a
         * particular sample version.
         */
        private boolean hasMultipleRecordsPerSampleVersion;

        /**
         * Indicates the name of the column in this table that contains the
         * sample Id, which must be linked to the "id" column in the "samples"
         * table.
         */
        private String sampleIdColumnName;

        /**
         * If non-null, this value indicates that a table contains versioned
         * data and that the value of this variable must be null for the current
         * version.
         */
        private String sampleHistoryIdColumnName;

        /**
         * A {@code Collection} of {@code TableUsageRecord} objects, each
         * containing an alias for this table and all the constraints that
         * reference it.
         */
        private List<TableUsageRecord> currentUsageRecords;

        /** Used to provide unique ids to each table alias. */
        private int autoIncrement;

        /** A constructor that initializes all member variables. */
        public TableRecord(String name, String aliasBase,
                boolean hasMultipleRecordsPerSampleVersion,
                String sampleIdColumnName, String sampleHistoryIdColumnName) {
            this.autoIncrement = 0;
            this.tableName = name;
            this.tableAliasBase = aliasBase;
            this.hasMultipleRecordsPerSampleVersion
                    = hasMultipleRecordsPerSampleVersion;
            this.sampleIdColumnName = sampleIdColumnName;
            this.sampleHistoryIdColumnName = sampleHistoryIdColumnName;
            this.currentUsageRecords = new ArrayList<TableUsageRecord>();
        }

        /**
         * A method that returns a {@code Collection} of all the alias names
         * provided by previous calls to {@code getAlias()}. This method may
         * return an empty {@code Collection} if no aliases have been assigned.
         * 
         * @return a {@code Collection<String>} of the used aliases.
         */
        public Collection<String> getAllUsedAliases() {
            Collection<String> aliasNames = new ArrayList<String>(
                    this.currentUsageRecords.size());

            for (TableUsageRecord record : this.currentUsageRecords) {
                aliasNames.add(record.alias);
            }

            return aliasNames;
        }

        /**
         * Gets an alias for the table represented by this {@code TableRecord}
         * that may be used by the given {@code SearchConstraint} to access this
         * table. If an existing alias may be reused by the given
         * {@code SearchConstraint} it is returned, otherwise a new one is
         * created.
         * 
         * @param constraint a {@code SearchConstraint} that references the
         *        table described by this {@code TableRecord}.
         * @return the alias by which the table represented by this
         *         {@code TableRecord} may be referenced
         */
        public String getAlias(SearchConstraint constraint,
                Map<SearchConstraint, SearchConstraint> childToParentMap) {
            if (this.currentUsageRecords.size() == 0) {
                // this is the first request to use this table
                TableUsageRecord usageRec = new TableUsageRecord(nextAlias(),
                        constraint);

                this.currentUsageRecords.add(usageRec);

                return usageRec.alias;
            } else if (!this.hasMultipleRecordsPerSampleVersion) {
                // the one existing alias will work
                return (this.currentUsageRecords).get(0).alias;
            } else {
                // check if any existing aliases will work, otherwise add a
                // new one
                for (TableUsageRecord usageRec : this.currentUsageRecords) {
                    if (!usageRec.isUsedByConflictingConstraint(constraint,
                            childToParentMap)) {
                        // this alias may be used by the constraint; indicate
                        // that it is now being used and return it
                        usageRec.constraintsUsingAlias.add(constraint);
                        return usageRec.alias;
                    }
                }

                // no usable alias was found; create a new one
                TableUsageRecord usageRec = new TableUsageRecord(nextAlias(),
                        constraint);
                this.currentUsageRecords.add(usageRec);
                return usageRec.alias;
            }
        }

        /**
         * Gets an alias for this table.
         * 
         * @return the next unique alias name
         */
        public String nextAlias() {
            return this.tableAliasBase + (this.autoIncrement++);
        }

        /**
         * Contains usage information for one alias of a given table and methods
         * to determine reusability. Each {@code TableUsageRecord} results in an
         * SQL LEFT JOIN in the final query.
         */
        public static class TableUsageRecord {

            /** The alias represented by this record. */
            String alias;

            /**
             * A {@code Collection} of {@code SearchConstraint} objects that use
             * this alias in their WHERE clause fragment.
             */
            Collection<SearchConstraint> constraintsUsingAlias;

            /**
             * Creates a new {@code TableRecord} with the given alias that will
             * be used by the given constraint. Before invoking this constructor
             * the caller should ensure that all previously created
             * {@code TableUsageRecords} disallow reuse by the given constraint
             * by using the {@code useForConstraint()} or
             * {@code isUsedByConflictingConstraint()} methods.
             * 
             * @param alias the alias
             * @param constraint the {@code SearchConstraint} whose SQL WHERE
             *        clause references this alias
             */
            public TableUsageRecord(String alias, SearchConstraint constraint) {
                this.alias = alias;
                this.constraintsUsingAlias = new ArrayList<SearchConstraint>();
                this.constraintsUsingAlias.add(constraint);
            }

            /**
             * Determines whether this {@code TableUsageRecord} is used in a way
             * that prevents it from being reused by the given constraint. Note:
             * if the given {@code SearchConstraint} is to actually use this
             * alias, the constraint must be added to the
             * {@code constraintsUsingAlias} collection to prevent later
             * conflicts.
             */
            public boolean isUsedByConflictingConstraint(
                    SearchConstraint constraint,
                    Map<SearchConstraint, SearchConstraint> childToParentMap) {
                for (SearchConstraint sc : constraintsUsingAlias) {
                    if (SearchTableTracker.mustBothConstraintsBeMet(
                            childToParentMap, sc, constraint)) {
                        return true;
                    }
                }

                return false;
            }
        }
    }
}
