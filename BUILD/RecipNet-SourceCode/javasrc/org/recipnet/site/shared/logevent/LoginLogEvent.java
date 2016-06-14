/**
 * Reciprocal Net project 
 *
 * LoginLogEvent.java
 *
 * 11-Jun-2002: hclin wrote skeleton
 * 25-Jun-2002: ekoperda added serialization code
 * 03-Sep-2002: ekoperda revamped class to better support Site Manager's 
 *              logging mechanism.
 * 14-Apr-2003: ekoperda revamped class again to better support Site Manager's
 *              logging mechanism
 * 31-Jul-2003: nsanghvi fixed bug #976 in constructor
 * 07-Jan-2004: ekoperda moved class from org.recipnet.site.container package
 *              to org.recipnet.site.shared.logevent
 * 11-Jan-2005: jobollin modified the name and documentation for the attribute
 *              that was called "serverName" -- it is now "contextName" to
 *              reflect the fact that it refers to a ServletContext, not to the
 *              server itself
 */

package org.recipnet.site.shared.logevent;

import java.util.logging.Level;

/** 
 * A subclass of LogEvent used to record a web app login attempt, whether 
 * successful or unsuccessful.  The severity Level.INFO is assumed.
 */
public class LoginLogEvent extends LogEvent {
    /** current JSP session id */
    public final String jspSessionId;
 
    /** the name of the servlet context on which the event occurred */
    public final String contextName;
   
    /** The username the user specified (may be invalid) */
    public final String userName;

    /** user's authentication result; true if login successfully; else false */
    public final boolean authenticationResult; 

    /** The id of the user, if his login was successful */ 
    public final int userId;
	
    /** 
     * The only constructor. 
     *
     * @param  jspSessionId JSP session id (a string) associated with the user
     *         session with which the attempt was/is associated.
     * @param  contextName the name of the servlet context on which the event
     *         occurred
     * @param  userName user name sent in the login attempt
     * @param  authenticationResult <code>true</code> if the login attempt was
     *         successful, or <code>false</code> if not.  A login attempt might
     *         fail if the username does not match a known username on the site
     *         or if the password does not match that of the specified user.
     * @param  userId if <code>authenticationResult</code> is true, this is the
     *         id of the user who just logged in; otherwise this value is
     *         ignored.
     */
    public LoginLogEvent(String jspSessionId, String contextName,
            String userName, boolean authenticationResult, int userId) {
	super();
	if (authenticationResult) {
	    super.createLogRecord(Level.INFO, 
	            "webapp session {0} on {1}: user {2} (id {3}) logged in",
                    new Object[] {jspSessionId, contextName, userName, 
                      new Integer(userId)}, 
                    null);
	} else {
	    super.createLogRecord(Level.INFO, 
	            "webapp session {0} on {1}: unsucessful login attempt"
                      + " for user {2}",
		    new Object[] {jspSessionId, contextName, userName},
                    null);
	}

	this.jspSessionId = jspSessionId;
	this.contextName = contextName;
        this.userName = userName;
	this.authenticationResult = authenticationResult;
	this.userId = userId;
    }
}
 
