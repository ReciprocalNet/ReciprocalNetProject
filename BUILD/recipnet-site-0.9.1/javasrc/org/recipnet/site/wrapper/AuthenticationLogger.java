/*
 * Reciprocal Net Project
 * 
 * AuthenticationLogger.java
 * 
 * 10-Jan-2005: jobollin wrote first draft
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.wrapper;

import java.rmi.RemoteException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.shared.logevent.LoginLogEvent;
import org.recipnet.site.shared.logevent.LogoutLogEvent;
import org.recipnet.site.shared.logevent.SessionBeginLogEvent;
import org.recipnet.site.shared.logevent.SessionEndLogEvent;

/**
 * {@code AuthenticationLogger} is an web application lifecycle listener that
 * observes and logs session creation and destruction events, and that provides
 * methods for logging login and logout events
 * 
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class AuthenticationLogger implements ServletContextListener,
        HttpSessionListener {

    /**
     * The attribute name that an instance assigns to itself in its host servlet
     * context
     */
    private final static String ATTRIBUTE_NAME
            = "org.recipnet.site.wrapper.AuthenticationLogger";

    /**
     * The context's CoreConnector instance, cached here to avoid repeated
     * lookups
     */
    private CoreConnector connector;

    /**
     * An implementation method of {@code ServletContextListener}. Used by this
     * class to add itself as an application attribute and to retrieve the
     * context's CoreConnector. The CoreConnector should have already been
     * installed before this listener.
     * 
     * @param sce a {@code ServletContextEvent} describing the context
     *        initialization
     */
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        context.log("AuthenticationLogger starting up");
        synchronized (context) {
            if (context.getAttribute(ATTRIBUTE_NAME) == null) {
                context.setAttribute(ATTRIBUTE_NAME, this);
            }
        }
        try {
            connector = CoreConnector.extract(context);
        } catch (IllegalArgumentException iae) {
            context.log("AuthenticationLogger: no CoreConnector found", iae);
        }
    }

    /**
     * An implementation method of {@code ServletContextListener}. This
     * listener does nothing special with this event, but does avail itself of
     * the opportunity to log its own shutdown to the context's log.
     * 
     * @param sce a {@code ServletContextEvent} describing the imminent context
     *        destruction
     */
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("AuthenticationLogger shutting down");
    }

    /**
     * An implementation method of the {@code HttpSessionListener} interface.
     * Used by this listener to log session creation events to the webapp's
     * associated Reciprocal Net core, with use of the context's
     * {@code CoreConnector}
     * 
     * @param se an {@code HttpSessionEvent} describing the session created
     */
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        try {
            connector.getSiteManager().recordLogEvent(
                    new SessionBeginLogEvent(session.getId(),
                            session.getServletContext().getServletContextName()));
        } catch (RemoteException re) {
            connector.reportRemoteException(re);
            session.getServletContext().log(
                    "Exception while logging session creation", re);
        }
    }

    /**
     * An implementation method of the {@code HttpSessionListener} interface.
     * Used by this listener to log session destruction events to the webapp's
     * associated Reciprocal Net core, with use of the context's
     * {@code CoreConnector}
     * 
     * @param se an {@code HttpSessionEvent} describing the session created
     */
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();

        try {
            connector.getSiteManager().recordLogEvent(
                    new SessionEndLogEvent(session.getId(),
                            session.getServletContext().getServletContextName()));
        } catch (RemoteException re) {
            connector.reportRemoteException(re);
            session.getServletContext().log(
                    "Exception while logging session destruction", re);
        }
    }

    /**
     * A method to record information about webapp login attempts to the
     * webapp's associated Reciprocal Net core.
     * 
     * @param session the {@code HttpSession} in which the login attempt ocurred
     * @param username the user name provided in the login attempt
     * @param userid the user id associated with the named user, if the attempt
     *        was successful
     * @param success {@code true} if the login succeeded, {@code false} if it
     *        failed
     * @throws RemoteException if core throws this exception during the logging
     *         attempt; the context's CoreConnector is notified of this
     *         exception before it is propogated outward
     */
    public void logLoginEvent(HttpSession session, String username, int userid,
            boolean success) throws RemoteException {
        try {
            connector.getSiteManager().recordLogEvent(
                    new LoginLogEvent(
                            session.getId(),
                            session.getServletContext().getServletContextName(),
                            username, success, userid));
        } catch (RemoteException re) {
            connector.reportRemoteException(re);
            throw re;
        }
    }

    /**
     * A method to record information about purposeful webapp logouts (as
     * opposed to session timeouts) to the webapp;s associated Reciprocal Net
     * core.
     * 
     * @param session the {@code HttpSession} in which the logout occurred
     * @param userInfo a {@code UserInfo} describing the user who logged out
     * @throws RemoteException if core throws this exception during the logging
     *         attempt; the context's CoreConnector is notified of this
     *         exception before it is propogated outward
     */
    public void logLogoutEvent(HttpSession session, UserInfo userInfo)
            throws RemoteException {
        try {
            connector.getSiteManager().recordLogEvent(
                    new LogoutLogEvent(
                            session.getId(),
                            session.getServletContext().getServletContextName(),
                            userInfo.username, userInfo.id));
        } catch (RemoteException re) {
            connector.reportRemoteException(re);
            throw re;
        }
    }

    /**
     * A convenience method for obtaining the (single) AuthenticationLogger
     * instance associated with a specified web application
     * 
     * @param context the {@code ServletContext} for which to obtain an
     *        AuthenticationLogger; this class must be configured as a web
     *        application listener on the associated web application for this
     *        method to succeed
     * @return the {@code AuthenticationLogger} configured on the specified
     *         context
     * @throws IllegalArgumentException if the specified context does not
     *         contain an {@code AuthenticationLogger}
     */
    public static AuthenticationLogger getAuthenticationLogger(
            ServletContext context) {
        AuthenticationLogger logger;

        synchronized (context) {
            logger = (AuthenticationLogger) context.getAttribute(ATTRIBUTE_NAME);
        }
        if (logger == null) {
            throw new IllegalArgumentException(
                    "No AuthenticationLogger found in the provided context");
        } else {
            return logger;
        }
    }

}
