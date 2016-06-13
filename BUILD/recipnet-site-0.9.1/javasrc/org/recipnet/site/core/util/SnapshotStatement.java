/*
 * Reciprocal Net project
 * 
 * SnapshotStatement.java
 *
 * 23-May-2003: ekoperda wrote first draft
 * 12-May-2006: jobollin reformatted the source and updated the docs
 * 07-May-2008: ekoperda added isClose(), setPoolable(), isPoolable(), 
 *              isWrapperFor(), and unwrap() to comply with Java 1.6
 * 09-Dec-2015: yuma added isCloseOnCompletion(), closeOnCompletion() 
 *		to comply with Java 1.7
 */

package org.recipnet.site.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * <p>
 * A wrapper for a {@code java.sql.Statement} object that lets the caller work
 * with a "snapshot" of his data. The {@code ResultSet} object returned by
 * {@code executeQuery()} is a snapshot of the data, as of the time of
 * invocation, and will not change while the {@code ResultSet} remains open,
 * even if the underlying database tables specified in the user's query string
 * are modified. This functionality makes sense only for {@code SELECT}-type
 * query strings passed to {@code executeQuery()}; other kinds of database
 * queries are not supported. Modifications a caller might make to the returned
 * {@code ResultSet} object (i.e. the snapshot) are not supported and will be
 * lost.
 * </p><p>
 * The current implementation creates a snapshot by executing a SQL query (as
 * passed to {@code executeQuery()}) and storing the results in a
 * uniquely-named temporary table. The {@code ResultSet} given to the caller is
 * backed by the temporary table. Thus, modifications a caller might make within
 * the {@code ResultSet} will be lost once the temporary table is dropped. The
 * temporary table is dropped from the database when the caller invokes
 * {@code close()}, when the {@code SnapshotStatement} object is finalized by
 * the Java VM, or when {@code executeQuery()} is invoked again. Users are
 * advised to call {@code close()} explicitly because finalization is not
 * guaranteed to be performed in a timely manner (or at all).
 * </p><p>
 * This class is <strong>not</strong> thread-safe.
 * <p>
 */
public class SnapshotStatement implements Statement {

    /** Used to generate unique names for temporary database tables. */
    private static SerialNumber serialNumber = new SerialNumber();

    private Statement s;

    private boolean tempTableExists;

    private String tempTableName;

    private ResultSet tempResultSet;

    private boolean isClosed = false;

    /**
     * Initializes a new {@code SnapshotStatement} object that wraps the
     * specified {@code Statement}. The behavior of this class is undefined if
     * the user invokes methods of the provided {@code Statement} after
     * initializing this {@code SnapshotStatement}.
     * 
     * @param statement the JDBC {@code Statement} wrapped by this
     *        {@code SnapshotStatement}, as might have been obtained via a
     *        prior invocation of {@code Connection.createStatement()}
     */
    public SnapshotStatement(Statement statement) {
        s = statement;
        tempTableExists = false;
        tempTableName = null;
    }

    /**
     * Releases any resources retained by this object after it becomes
     * unreachable; in particular, it invokes {@link #close()} if that method
     * has not already been invoked, so as to drop any temporary table and close
     * the underlying statement.
     * 
     * @throws SQLException if a database error occurs 
     */
    @Override
    protected void finalize() throws SQLException {
        if (!isClosed) {
            close();
        }
    }

    /**
     * {@inheritDoc}.  This version returns a {@code ResultSet} backed by a
     * snapshot of the data, rather than by the raw database tables.
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        if (tempTableExists) {
            
            /*
             * executeQuery() has been invoked previously. Clean up from the
             * last time and prepare this Statement object for reuse.
             */
            tempResultSet.close();
            s.executeUpdate("DROP TABLE " + tempTableName + ";");
            tempTableExists = false;
        }

        tempTableName = "snapshot" + serialNumber.get();
        s.executeUpdate("CREATE TABLE " + tempTableName + " " + sql);
        tempTableExists = true;
        tempResultSet = s.executeQuery("SELECT * FROM " + tempTableName + ";");
        
        return tempResultSet;
    }

    /**
     * {@inheritDoc}.  This version Drops the temporary table if it exists
     * and closes the underlying {@code Statement}
     */
    public void close() throws SQLException {
        if (tempTableExists) {
            s.executeUpdate("DROP TABLE " + tempTableName + ";");
            tempTableExists = false;
        }
        s.close();
        isClosed = true;
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void cancel() throws SQLException {
        s.cancel();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void clearWarnings() throws SQLException {
        s.clearWarnings();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public Connection getConnection() throws SQLException {
        return s.getConnection();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getFetchDirection() throws SQLException {
        return s.getFetchDirection();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getFetchSize() throws SQLException {
        return s.getFetchSize();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        return s.getGeneratedKeys();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getMaxFieldSize() throws SQLException {
        return s.getMaxFieldSize();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getMaxRows() throws SQLException {
        return s.getMaxRows();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public boolean getMoreResults() throws SQLException {
        return s.getMoreResults();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public boolean getMoreResults(int current) throws SQLException {
        return s.getMoreResults(current);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getQueryTimeout() throws SQLException {
        return s.getQueryTimeout();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public ResultSet getResultSet() throws SQLException {
        return s.getResultSet();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getResultSetConcurrency() throws SQLException {
        return s.getResultSetConcurrency();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getResultSetHoldability() throws SQLException {
        return s.getResultSetHoldability();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getResultSetType() throws SQLException {
        return s.getResultSetType();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public int getUpdateCount() throws SQLException {
        return s.getUpdateCount();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public SQLWarning getWarnings() throws SQLException {
        return s.getWarnings();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setCursorName(String name) throws SQLException {
        s.setCursorName(name);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        s.setEscapeProcessing(enable);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setFetchDirection(int direction) throws SQLException {
        s.setFetchDirection(direction);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setFetchSize(int rows) throws SQLException {
        s.setFetchSize(rows);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setMaxFieldSize(int max) throws SQLException {
        s.setMaxFieldSize(max);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setMaxRows(int max) throws SQLException {
        s.setMaxRows(max);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        s.setQueryTimeout(seconds);
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public void addBatch(@SuppressWarnings("unused") String sql)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public void clearBatch() throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public boolean execute(@SuppressWarnings("unused") String sql)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public boolean execute(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") int autoGeneratedKeys)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public boolean execute(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") int[] columnIndexes)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public boolean execute(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") String[] columnNames)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public int[] executeBatch() throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public int executeUpdate(@SuppressWarnings("unused") String sql)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public int executeUpdate(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") int autoGeneratedKeys)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public int executeUpdate(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") int[] columnIndexes)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This is nonsensical in a snapshot context, so this
     * version always throws an {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public int executeUpdate(@SuppressWarnings("unused") String sql,
            @SuppressWarnings("unused") String[] columnNames)
            throws SQLException {
        throw new SQLException(
                "Only SELECT queries are supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public boolean isClosed() throws SQLException {
	return s.isClosed();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void setPoolable(boolean poolable) throws SQLException {
        s.setPoolable(poolable);
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public boolean isPoolable() throws SQLException {
	return s.isPoolable();
    }

    /**
     * {@inheritDoc}.  This functionality is not supported by 
     * SnapshotStatement; thus, the present implementation always throws a 
     * {@code SQLException}.
     * 
     * @throws SQLException every time
     */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(
                "Wrapper functionality is not supported by SnapshotStatement");
    }

    /**
     * {@inheritDoc}.  This functionality is not supported by 
     * SnapshotStatement; thus, the present implementation always returns
     * false. 
     */
    public boolean isWrapperFor(Class iface) throws SQLException {
	return false;
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public boolean isCloseOnCompletion() throws SQLException {
	return s.isCloseOnCompletion();
    }

    /**
     * {@inheritDoc}.  This version delegates to the underlying
     * {@code Statement}.
     */
    public void closeOnCompletion() throws SQLException {
        s.closeOnCompletion();
    }
}
