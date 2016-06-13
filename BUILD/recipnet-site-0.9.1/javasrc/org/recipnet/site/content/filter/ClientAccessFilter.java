/*
 * Reciprocal Net Project
 *
 * ClientAccessFilter.java
 *
 * 11-Jan-2005: jobollin wrote first draft
 * 24-May-2006: jobollin updated docs
 */

package org.recipnet.site.content.filter;

import java.io.IOException;

import java.rmi.RemoteException;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.shared.logevent.ClientAccessLogEvent;

/**
 * A simple {@code Filter} implementation that logs all unique client IPs
 * to access each webapp session.  The Servlet API provides no guarantee that
 * there will be only one such, and the information is only available from the
 * request objects.  Requests can therefore be passed through an instance of
 * this filter to keep track of which IPs have made requests in any particular
 * session.  This Filter's doFilter method creates a session for each request
 * that didn't already have one, and synchronizes on the request's session.
 *
 * @author John C. Bollinger
 * @version 0.9.0
 */
public class ClientAccessFilter implements Filter {

    /**
     * The attribute name by which this filter stores each session's Set of
     * client IPs
     */
    private final static String CLIENT_ACCESS_ATTRIBUTE =
        "org.recipnet.site.content.filter.ClientAccessFilter";

    /**
     * The ServletContext in which this filter is operating
     */
    private ServletContext context;

    /**
     * The CoreConnector instance for this filter's ServletContext; cached
     * here to avoid repeated lookups by this filter
     */
    private CoreConnector connector;

    /**
     * Implementation method of the Filter interface.  Initializes this
     * {@code Filter} based on the provided {@code FilterConfig}
     *
     * @param  config the {@code FilterConfig} from which this
     *         {@code Filter} should draw any necessary configuration
     *         information
     */
    public void init(FilterConfig config) {
        context = config.getServletContext();
        context.log("ClientAccessFilter starting up");
        try {
            connector = CoreConnector.extract(context);
        } catch (IllegalArgumentException iae) {
            context.log("ClientAccessFilter: no CoreConnector found", iae);
        }
    }

    /**
     * Implementation method of the {@code Filter} interface.  Notifies
     * this {@code Filter} that it is being taken out of service
     */
    public void destroy() {
        context.log("ClientAccessFilter shutting down");
    }

    /**
     * Implementation method of the {@code Filter} interface.  Causes this
     * {@code Filter} to filter the request / response; in particular, this
     * implementation keeps a record in the request's session of the client IP
     * numbers that have made requests in the session, and records a log event
     * each time a new one does (including for the first request in the
     * session).  Notifies the application's {@code CoreConnector} if
     * logging attempts fail with a {@code RemoteException}, but does not
     * propogate such exceptions
     *
     * @param  request the {@code ServletRequest} (assumed an
     *         {@code HttpServletRequest}) representing the request to
     *         to filter
     * @param  response the {@code ServletResponse} representing the
     *         response to filter
     * @param  chain a {@code FilterChain} to which this method should delegate
     *         if it chooses to not block or handle the request
     *
     * @throws IOException if the filter chain propogates this exception up to
     *         this filter
     * @throws ServletException if the filter chain propogates this exception
     *         up to this filter
     */
    @SuppressWarnings("unchecked")
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();

        synchronized(session) {
            Set<String> clientIps = (Set<String>) session.getAttribute(
                    CLIENT_ACCESS_ATTRIBUTE);
            String address = request.getRemoteAddr();

            if (clientIps == null) {
                clientIps = new HashSet<String>(2);
                session.setAttribute(CLIENT_ACCESS_ATTRIBUTE, clientIps);
            }

            if (clientIps.add(address)) {
                try {
                    connector.getSiteManager().recordLogEvent(
                            new ClientAccessLogEvent(session.getId(),
                                    context.getServletContextName(), address));
                } catch (RemoteException re) {
                    connector.reportRemoteException(re);
                }
            }
        }

        chain.doFilter(request, response);
    }
}

