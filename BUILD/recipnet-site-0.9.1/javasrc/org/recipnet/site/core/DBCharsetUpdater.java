/*
 * Reciprocal Net project
 * @(#)DBCharsetUpdater.java
 *
 * 02-Apr-2004: jobollin wrote first draft
 */
 
package org.recipnet.site.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A class containing a static method used only during a software update to
 * Reciprocal Net site software version 0.6.2 from an earlier version.  In the
 * case of such an update, the character data in the database need to be
 * checked, and those characters that are not in the ASCII set must be recoded.
 * 
 * @author Reciprocal Net
 * @version 0.6.2  
 */
class DBCharsetUpdater {
    
    /**
     * This class cannot be instantiated.
     */
    private DBCharsetUpdater() {}

    /**
     * Performs the charset recoding on all relevant tables.  The recoding is
     * performed mostly by the JDBC driver; this class just reads the character
     * data via a connection configured for the original encoding scheme and
     * writes it back, if necessary, via a connection configured for the new
     * one. 
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */    
    static void updateDBCharacters(Connection fromCon, Connection toCon)
            throws SQLException {
        updateLabs(fromCon, toCon);
        updateProviders(fromCon, toCon);
        updateSampleAnnotations(fromCon, toCon);
        updateSampleAttributes(fromCon, toCon);
        updateSampleData(fromCon, toCon);
        updateSampleHistory(fromCon, toCon);
        updateSamples(fromCon, toCon);
        updateSites(fromCon, toCon);
        updateUsers(fromCon, toCon);
    }

    /**
     * Performs the character encoding update on any Lab records that require
     * it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateLabs(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery("select * from labs;");
                
                while(rs.next()) {
                    String name = rs.getString("name");
                    String shortName = rs.getString("shortName");
                    String directoryName = rs.getString("directoryName");
                    String homeUrl = rs.getString("homeUrl");
                    String copyright = rs.getString("defaultCopyrightNotice");
                    
                    if (checkString(name)
                            || checkString(shortName)
                            || checkString(directoryName)
                            || checkString(homeUrl)
                            || checkString(copyright)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from labs where id=" + id + ";");
                            
                        rs2.next();
                        rs2.updateString("name", name);
                        rs2.updateString("shortName", shortName);
                        rs2.updateString("directoryName", directoryName);
                        rs2.updateString("homeUrl", homeUrl);
                        rs2.updateString("defaultCopyrightNotice", copyright);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any Provider records that
     * require it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateProviders(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery("select * from providers;");
                
                while(rs.next()) {
                    String name = rs.getString("name");
                    String contact = rs.getString("headContact");
                    String comments = rs.getString("comments");
                    
                    if (checkString(name)
                            || checkString(contact)
                            || checkString(comments)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from providers where id=" + id + ";");
                            
                        rs2.next();
                        rs2.updateString("name", name);
                        rs2.updateString("headContact", contact);
                        rs2.updateString("comments", comments);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any SampleAnnotation records
     * that require it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSampleAnnotations(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery(
                        "select * from sampleAnnotations;");
                
                while(rs.next()) {
                    String value = rs.getString("value");
                    
                    if (checkString(value)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from sampleAnnotations where id=" + id
                            + ";");
                            
                        rs2.next();
                        rs2.updateString("value", value);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any SampleAttribute records
     * that require it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSampleAttributes(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery(
                        "select * from sampleAttributes;");
                
                while(rs.next()) {
                    String value = rs.getString("value");
                    
                    if (checkString(value)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from sampleAttributes where id=" + id
                            + ";");
                            
                        rs2.next();
                        rs2.updateString("value", value);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any SampleData records
     * that require it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSampleData(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery(
                        "select * from sampleData;");
                
                while(rs.next()) {
                    String spgp = rs.getString("spgp");
                    String color = rs.getString("color");
                    String summary = rs.getString("summary");
                    
                    if (checkString(spgp)
                            || checkString(color)
                            || checkString(summary)) {
                        int id = rs.getInt("first_SampleHistory_Id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from sampleData where first_SampleHistory_Id="
                            + id + ";");
                            
                        rs2.next();
                        rs2.updateString("spgp", spgp);
                        rs2.updateString("color", color);
                        rs2.updateString("summary", summary);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any SampleHistory records
     * that require it (history comments only)
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSampleHistory(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery(
                        "select * from sampleHistory;");
                
                while(rs.next()) {
                    String comments = rs.getString("comments");
                    
                    if (checkString(comments)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from sampleHistory where id=" + id
                            + ";");
                            
                        rs2.next();
                        rs2.updateString("comments", comments);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any Sample records that require
     * it; only the localLabId field might be affected, and it is highly
     * unlikely that there are any instances where correction will be needed,
     * but things would break badly for any sample that was affected so we do
     * the test.
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSamples(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery(
                        "select * from samples;");
                
                while(rs.next()) {
                    String localLabId = rs.getString("localLabId");
                    
                    if (checkString(localLabId)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from samples where id=" + id + ";");
                            
                        rs2.next();
                        rs2.updateString("localLabId", localLabId);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    
    /**
     * Performs the character encoding update on any Site records that require
     * it.  There shouldn't be any, as we have a pretty firm hold on site
     * information, but all the same, it doesn't hurt to be safe.
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateSites(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery("select * from sites;");
                
                while(rs.next()) {
                    String name = rs.getString("name");
                    String shortName = rs.getString("shortName");
                    String baseUrl = rs.getString("baseUrl");
                    String repUrl = rs.getString("repositoryUrl");
                    
                    if (checkString(name)
                            || checkString(shortName)
                            || checkString(baseUrl)
                            || checkString(repUrl)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from sites where id=" + id + ";");
                            
                        rs2.next();
                        rs2.updateString("name", name);
                        rs2.updateString("shortName", shortName);
                        rs2.updateString("baseUrl", baseUrl);
                        rs2.updateString("repositoryUrl", repUrl);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }
    
    /**
     * Performs the character encoding update on any User records that require
     * it
     * 
     * @param fromCon a JDBC Connection with which to read characters according
     *        to the original encoding scheme
     * @param toCon a JDBC Connection with which to write characters according
     *        to the new encoding scheme
     */
    static void updateUsers(Connection fromCon, Connection toCon)
            throws SQLException {
        Statement fromStmt = fromCon.createStatement();
        
        try {
            Statement toStmt = 
                    toCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          ResultSet.CONCUR_UPDATABLE);
            
            try {
                ResultSet rs = fromStmt.executeQuery("select * from users;");
                
                while(rs.next()) {
                    String fullName = rs.getString("fullname");
                    String userName = rs.getString("username");
                    
                    if (checkString(fullName)
                            || checkString(userName)) {
                        int id = rs.getInt("id");
                        ResultSet rs2 = toStmt.executeQuery(
                            "select * from users where id=" + id + ";");
                            
                        rs2.next();
                        rs2.updateString("fullname", fullName);
                        rs2.updateString("username", userName);
                        rs2.updateRow();
                        rs2.close();
                    }
                }
            } finally {
                toStmt.close();
            }
        } finally {
            fromStmt.close();  // will close ResultSet rs also
        }
    }

    /**
     * Checks a string to determine whether it has any characters with Unicode
     * code point greater than 127; such characters are not encoded by ASCII
     * and are encoded differently by the many other character encodings.  Most
     * notably, they are encoded differently by ISO-8859-1 (aka Latin-1) than
     * they are by Unicode/UTF-8
     * 
     * @param s the String to check
     * 
     * @return true if <code>s</code> contains any characters with number
     *         greater than 127; false otherwise
     */
    private static boolean checkString(String s) {
        if (s != null) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) > 127) {
                    return true;
                }
            }
        }
        return false;     
    }
}

