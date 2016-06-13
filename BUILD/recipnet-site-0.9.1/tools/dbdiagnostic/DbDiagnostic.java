/*
 * Reciprocal Net project
 * @(#)DbDiagnostic.java
 *
 * 15-Jul-2003: ekoperda wrote first draft
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbDiagnostic {
    public static void main(String args[]) {
	try {
	    System.err.println("Reciprocal Net site software");
	    System.err.println("database diagnostic utility");
	    System.err.println();

	    // Obtain the db root password.
	    System.err.print("Please enter the database root password: ");
	    BufferedReader reader 
                    = new BufferedReader(new InputStreamReader(System.in));
	    String rootPassword = reader.readLine();
	    System.out.println("");

	    // Instantiate the db driver.
	    System.out.print("  instantiating com.mysql.jdbc.Driver... ");
	    Class.forName("com.mysql.jdbc.Driver");
	    System.out.println("ok");

	    // Attempt a bunch of connections.
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/recipnet?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/mysql?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/recipnet",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/mysql",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/recipnet",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/mysql",
                    "root", rootPassword);

	    // Attempt a bunch of connections again.
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/recipnet?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection(
                    "jdbc:mysql://localhost:3306/mysql?autoReconnect=true",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/recipnet",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost:3306/mysql",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/recipnet",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/",
                    "root", rootPassword);
	    attemptConnection("jdbc:mysql://localhost/mysql",
                    "root", rootPassword);

	    System.err.println("  Done!");
	} catch (Throwable ex) {
	    ex.printStackTrace();
	}
    }

    public static void attemptConnection(String dbUrl, String username, 
            String password) {
	System.out.print("  attempting connection to '" + dbUrl + "'... ");
	try {
	    Connection conn 
                    = DriverManager.getConnection(dbUrl, username, password);
	    System.out.println("ok");
	} catch (SQLException ex) {
	    System.out.println("failed -- " + ex.getMessage());
	}
    }
}
