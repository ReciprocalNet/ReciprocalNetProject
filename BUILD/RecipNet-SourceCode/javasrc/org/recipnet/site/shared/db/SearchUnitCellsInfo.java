/*
 * Reciprocal Net project
 * 
 * SearchUnitCellsInfo.java
 *
 * 18-May-2005: ekoperda wrote first draft
 * 31-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.shared.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SearchUnitCellsInfo is a database container class that represents the
 * contents of a single row in the table named 'searchUnitCells'.
 */
public class SearchUnitCellsInfo {
    /** Foreign key that identifies the sample this object pertains to. */
    public int sampleId;

    /**
     * One of the cell lengths (in Angstroms) of the reduced cell that was
     * calculated from the sample's unit cell dimensions.
     */
    public double aPrime;

    /**
     * One of the cell lengths (in Angstroms) of the reduced cell that was
     * calculated from the sample's unit cell dimensions.
     */
    public double bPrime;

    /**
     * One of the cell lengths (in Angstroms) of the reduced cell that was
     * calculated from the sample's unit cell dimensions.
     */
    public double cPrime;

    /**
     * The volume of the reduced cell (in cubic Angstroms) of the reduced cell
     * that was calculated from the sample's unit cell dimensions.
     */
    public double vPrime;

    /**
     * One of the cell lengths (in reciprocal Angstroms) of the
     * reciprocal-reduced cell trhat was calculated from the sample's unit cell
     * dimensions.
     */
    public double aStarPrime;

    /**
     * One of the cell lengths (in reciprocal Angstroms) of the
     * reciprocal-reduced cell trhat was calculated from the sample's unit cell
     * dimensions.
     */
    public double bStarPrime;

    /**
     * One of the cell lengths (in reciprocal Angstroms) of the
     * reciprocal-reduced cell trhat was calculated from the sample's unit cell
     * dimensions.
     */
    public double cStarPrime;

    /** Store this object in the current row of the provided db resultset */
    public void dbStore(ResultSet rs) throws SQLException {
        rs.updateInt("sample_id", this.sampleId);
        rs.updateDouble("aprime", this.aPrime);
        rs.updateDouble("bprime", this.bPrime);
        rs.updateDouble("cprime", this.cPrime);
        rs.updateDouble("vprime", this.vPrime);
        rs.updateDouble("astarprime", this.aStarPrime);
        rs.updateDouble("bstarprime", this.bStarPrime);
        rs.updateDouble("cstarprime", this.cStarPrime);
    }
}
