/*
 * Reciprocal Net Project
 * 
 * CoreConnector.java
 * 
 * 06-Feb-2004: ekoperda wrote first draft
 * 07-May-2004: cwestnea added support for parameter rmiPort throughout
 * 24-May-2006: jobollin reformatted the source
 */

package org.recipnet.site.wrapper;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.recipnet.site.core.RepositoryManagerRemote;
import org.recipnet.site.core.SampleManagerRemote;
import org.recipnet.site.core.SiteManagerRemote;

/**
 * <p>
 * {@code CoreConnector} is the web application's conduit to the three core
 * modules. It is designed to be persisted by the servlet container with
 * application (i.e. "servlet context") scope. That is, configuration directives
 * in web.xml should instruct the servlet container to instantiate one of these
 * objects at application startup time. The {@code CoreConnector} object then
 * persists itself inside the {@code ServletContext} object as an attribute
 * named {@code org.recipnet.site.wrapper.CoreConnector} .
 * </p><p>
 * Subsequently, web application code that wishing to contact core should
 * <ol>
 * <li>locate the web application's {@code CoreConnector} instance by invoking
 * {@link #extract(ServletContext)}.</li>
 * <li>invoke {@link #getSiteManager()} or {@link #getSampleManager()} or
 * {@link #getRepositoryManager()} to obtain a reference to the needed core
 * module.</li>
 * <li>make RMI calls to the core module.</li>
 * <li>catch any {@code RemoteException}s that occur and report them by
 * invoking {@link #reportRemoteException(RemoteException)}.</li>
 * </ol>
 * Users are encouraged to not persist the references returned by
 * {@code getSiteManager()}, {@code getSampleManager()}, and
 * {@code getRepositoryManager()} beyond the scope of a single request in order
 * to ensure that any reconnections take effect rapidly.
 * </p>
 * <p>
 * {@code CoreConnector} reads configuration directives from web.xml at
 * application startup time. These are:
 * <dl>
 * <dt>hostName</dt>
 * <dd>DNS or other hostname of the server running the core modules, or
 * {@code localhost}.</dd>
 * <dt>rmiPort</dt>
 * <dd>The port that the RMI registry is running on</dd>
 * <dt>siteManagerName</dt>
 * <dd>RMI name of core's Site Manager</dd>
 * <dt>sampleManagerName</dt>
 * <dd>RMI name of core's Sample Manager</dd>
 * <dt>repositoryManagerName</dt>
 * <dd>RMI name of core's Repository Manager</dd>
 * </dl>
 * </p>
 */
public class CoreConnector implements ServletContextListener {
    
    /** Configuration directive set by {@code contextInitialized()} */
    private String hostName;

    /** Configuration directive set by {@code contextInitialized()} */
    private String rmiPort;

    /** Configuration directive set by {@code contextInitialized()} */
    private String siteManagerName;

    /** Configuration directive set by {@code contextInitialized()} */
    private String sampleManagerName;

    /** Configuration directive set by {@code contextInitialized()} */
    private String repositoryManagerName;

    /** Reference to bound core module */
    private SiteManagerRemote siteManager = null;

    /** Reference to bound core module */
    private SampleManagerRemote sampleManager = null;

    /** Reference to bound core module */
    private RepositoryManagerRemote repositoryManager = null;

    /**
     * Static function that returns an instance of {@code CoreConnector}, given
     * the {@code ServletContext} object from the current web application.
     * 
     * @param sc the {@code ServletContext} object for the current web
     *        application, as obtained by a call to
     *        {@code ServletConfig.getServletContext()} .
     * @return the {@code CoreConnector} associated with the specified context
     * @throws IllegalArgumentException if no {@code CoreConnector} instance has
     *         been embedded within the specified {@code ServletContext}.
     */
    public static CoreConnector extract(ServletContext sc) {
        CoreConnector cc = (CoreConnector) sc.getAttribute(
                CoreConnector.class.getName());
        
        if (cc == null) {
            throw new IllegalArgumentException();
        }
        
        return cc;
    }

    /**
     * @return a reference to Site Manager, initiating a reconnect if necessary.
     * @throws IllegalStateException if, during a reconnection attempt,
     *         configuration directives are not specified or cannot be used to
     *         construct a valid URL for the RMI server.
     * @throws RemoteException if, during a reconnection attempt, a connection
     *         to the RMI server could not be established due to a connectivity
     *         problem.
     */
    public synchronized SiteManagerRemote getSiteManager()
            throws RemoteException {
        if (this.siteManager == null) {
            reconnect();
        }
        
        return this.siteManager;
    }

    /**
     * @return a reference to Sample Manager, initiating a reconnect if
     *         necessary.
     * @throws IllegalStateException if, during a reconnection attempt,
     *         configuration directives are not specified or cannot be used to
     *         construct a valid URL for the RMI server.
     * @throws RemoteException if, during a reconnection attempt, a connection
     *         to the RMI server could not be established due to a connectivity
     *         problem.
     */
    public synchronized SampleManagerRemote getSampleManager()
            throws RemoteException {
        if (this.sampleManager == null) {
            reconnect();
        }
        
        return this.sampleManager;
    }

    /**
     * @return a reference to Repository Manager, initiating a reconnect if
     *         necessary.
     * @throws IllegalStateException if, during a reconnection attempt,
     *         configuration directives are not specified or cannot be used to
     *         construct a valid URL for the RMI server.
     * @throws RemoteException if, during a reconnection attempt, a connection
     *         to the RMI server could not be established due to a connectivity
     *         problem.
     */
    public synchronized RepositoryManagerRemote getRepositoryManager()
            throws RemoteException {
        if (this.repositoryManager == null) {
            reconnect();
        }
        
        return this.repositoryManager;
    }

    /**
     * Clients should call this method to report that they received a
     * {@code RemoteException} when talking to core. Presumably this would be
     * because core was down or the connection had been broken. This method
     * invalidates the present connections and may cause a reconnection attempt
     * to be initiated subsequently.
     * 
     * @param ex the {@code RemoteException} to report
     */
    public synchronized void reportRemoteException(
            @SuppressWarnings("unused") RemoteException ex) {
        this.siteManager = null;
        this.sampleManager = null;
        this.repositoryManager = null;
    }

    /**
     * Helper function that attempts to (re)establish an RMI connection to core.
     * 
     * @throws IllegalStateException if configuration directives are not
     *         specified or cannot be used to construct a valid URL for the RMI
     *         server.
     * @throws RemoteException if a connection to the RMI server could not be
     *         established due to a connectivity problem.
     */
    private synchronized void reconnect() throws RemoteException {
        // Exit early if configuration directives have not been defined.
        if ((this.hostName == null) || (this.rmiPort == null)
                || (this.siteManagerName == null)
                || (this.sampleManagerName == null)
                || (this.repositoryManagerName == null)) {
            throw new IllegalStateException();
        }

        // Attempt a connection.
        try {
            this.siteManager = (SiteManagerRemote) Naming.lookup("//"
                    + this.hostName + ":" + rmiPort + "/"
                    + this.siteManagerName);
            this.sampleManager = (SampleManagerRemote) Naming.lookup("//"
                    + this.hostName + ":" + rmiPort + "/"
                    + this.sampleManagerName);
            this.repositoryManager = (RepositoryManagerRemote) Naming.lookup("//"
                    + this.hostName + ":" + rmiPort + "/"
                    + this.repositoryManagerName);
        } catch (NotBoundException ex) {
            this.siteManager = null;
            this.sampleManager = null;
            this.repositoryManager = null;
            
            throw new RemoteException(null, ex);
        } catch (MalformedURLException ex) {
            // No provision for nesting an exception.
            throw new IllegalStateException();
        }
    }

    /**
     * From interface {@code ServletContextListener}. Reads and parses
     * configuration directives specified in web.xml. Causes this object to be
     * persisted in the webapp's Application context in such a way that
     * {@code extract()} will work.
     * 
     * @param sce a {@code ServletContext} describing the web application being
     *        initialized
     */
    public void contextInitialized(ServletContextEvent sce) {
        // Read and parse configuration directives.
        this.hostName = sce.getServletContext().getInitParameter("hostName");
        this.rmiPort = sce.getServletContext().getInitParameter("rmiPort");
        this.siteManagerName
                = sce.getServletContext().getInitParameter("siteManagerName");
        this.sampleManagerName
                = sce.getServletContext().getInitParameter("sampleManagerName");
        this.repositoryManagerName = sce.getServletContext().getInitParameter(
                "repositoryManagerName");

        // Persist this object in the Application context.
        sce.getServletContext().setAttribute(CoreConnector.class.getName(),
                this);
    }

    /**
     * From interface {@code ServletContextListener}. Removes this object from
     * the webapp's Application context.
     * 
     * @param sce a {@code ServletContext} describing the web application being
     *        shut down
     */
    public void contextDestroyed(ServletContextEvent sce) {
        // Remove this object from the Application context.
        sce.getServletContext().removeAttribute(CoreConnector.class.getName());
    }
}
