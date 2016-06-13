/*
 * Reciprocal Net project
 * rendering software
 * 
 * RequestAuthenticator.java
 *
 * 18-Aug-2004: eisiorho wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.dispatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class is used to validate all requests coming for any type of service
 * currently being offered. Every time a request comes in,
 * {@code RequestAuthenticator} will load in user.xml, where every user's
 * profile is stored.
 */
public class RequestAuthenticator implements ServletContextListener {

    /**
     * Used to bind RequestAuthenticator to <i>DEFAULT_ATTRIBUTE_NAME</i> for
     * use by Tomcat
     */
    private static final String DEFAULT_ATTRIBUTE_NAME
            = "org.recipnet.rendering.servlet.RequestAuthenticator";

    /**
     * A value {@code xmlFile} uses to represent what type of service a
     * client is allowed to use.
     */
    public static final String CRT_REQUEST = "crtRender";

    /**
     * Holds every {@code userProfile} object. The key is the client's
     * username, and the value is a {@code userProfile} object.
     */
    private Collection<UserProfile> userProfiles;

    /**
     * Is used to pass information between the serlvet container and
     * {@code RequestAuthenticator}.
     */
    private ServletContext context;

    /**
     * This file holds every client's information concerning the rendering
     * service.
     */
    private String xmlFile;

    /**
     * Contains a client's information.
     */
    private UserParser userParser;

    /**
     * Default constructor.
     */
    public RequestAuthenticator() {
        this.userProfiles = null;
        this.context = null;
        this.xmlFile = null;
        this.userParser = null;
    }

    /**
     * From interface <i>ServletContextListener</i>. Removes this object from
     * the webapp's Application context.
     */
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute(DEFAULT_ATTRIBUTE_NAME);
        this.context = null;
    }

    /**
     * From interface <i>ServletContextListener</i>. Reads and parses
     * configuration directives specified in web.xml. Causes this object to be
     * persisted in the webapp's Application context.
     */
    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
        context.setAttribute(DEFAULT_ATTRIBUTE_NAME, this);
        this.xmlFile = context.getInitParameter("Acl");
        this.userParser = new UserParser(xmlFile);
        this.userProfiles = userParser.getProfiles();
    }

    /**
     * Binds {@code RequestAuthenticator} object to Tomcat
     * 
     * @return returns the {@code RequestAuthenticator} binded to Tomcat
     */
    public static RequestAuthenticator getRequestAuthenticator(
            ServletContext context) {
        return (RequestAuthenticator) context.getAttribute(
                DEFAULT_ATTRIBUTE_NAME);
    }

    /**
     * Checks the client's permission to use these services.
     * 
     * @param username
     * @param password
     * @return <b>true</b> if the credentials are valid, <b>false</b>
     *         otherwise.
     */
    public boolean authenticateCredentials(String username, String password) {
        for (UserProfile currentUser : userProfiles) {
            if ((currentUser != null) && username.equals(currentUser.username)
                    && password.equals(currentUser.password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the client's permission to submit requests to a particular engine.
     * 
     * @param username the name of the user, as a {@code String}
     * @param typeOfJob identifies the engine that received the request
     * @return {@code true} if the request is allowed, {@code false} otherwise
     */
    public boolean checkAuthorizationForRequestType(String username,
            String typeOfJob) {
        for (UserProfile currentUser : userProfiles) {
            if ((currentUser != null)
                    && currentUser.authorizedRequestTypes.containsKey(typeOfJob)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks a user's permission to submit a job with the specified priority.
     * 
     * @param username
     * @param requestedPriority determines where the requested job can be
     *        inserted into the job queue. Acceptable range is 1-1024.
     * @return {@code true} if the job is allowed, {@code false} otherwise
     */
    public boolean checkAuthorizationForRequestedPriority(String username,
            int requestedPriority) {
        for (UserProfile currentUser : userProfiles) {
            if ((currentUser != null)
                    && (requestedPriority <= currentUser.maximumJobPriority)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if the client is allowed to submit the specified job, based
     * on the job's estimated cost. (as reported by job.getCost)
     * 
     * @param username
     * @param job the job about to be executed by the engine.
     * @return {@code true} if the estimated cost is less than client's allotted
     *         cost, {@code false} otherwise
     */
    public boolean checkAuthorizationForJobCost(String username, AbstractJob job) {
        for (UserProfile currentUser : userProfiles) {
            if ((currentUser != null)
                    && (job.cost <= currentUser.maximumJobCost)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Holds each client's profile.
     */
    public class UserProfile {

        /** The client's name that is requesting a render job */
        private String username;

        /** The client's password for the user requesting a render job */
        private String password;

        /**
         * The client's priority with requesting a render job in relation with
         * other requests.
         */
        private int maximumJobPriority;

        /**
         * Contains the ceiling with how much resources a client's render job
         * can request.
         */
        private double maximumJobCost;

        /** Holds a list of what render services a client has access to. */
        private Map<String, String> authorizedRequestTypes;

        /**
         * Constructor
         * 
         * @param username
         * @param password
         * @param maximumJobPriority the priority of the requested render job
         * @param maximumJobCost the most resources the requested render job
         *        could take
         * @param authorizedRequestTypes
         */
        public UserProfile(String username, String password,
                int maximumJobPriority, double maximumJobCost,
                Map<String, String> authorizedRequestTypes) {
            this.username = username;
            this.password = password;
            this.maximumJobPriority = maximumJobPriority;
            this.maximumJobCost = maximumJobCost;
            this.authorizedRequestTypes = authorizedRequestTypes;
        }
    }

    /**
     * Required for parsing <i>xmlFile</i>.
     */
    private class UserParser extends DefaultHandler {

        /**
         * Used to hold parsed {@code UserProfile} objects.
         */
        Collection<UserProfile> profiles;

        /**
         * Constructor
         * 
         * @param xmlFile contains the location of the file that contains the
         *        rendering clients' information
         */
        public UserParser(String xmlFile) {
            this.profiles = new HashSet<UserProfile>();
            try {
                SAXParserFactory parseFactory = SAXParserFactory.newInstance();
                parseFactory.setValidating(true);
                SAXParser saxParser = parseFactory.newSAXParser();
                XMLReader xmlReader = saxParser.getXMLReader();
                xmlReader.setContentHandler(this);
                xmlReader.setErrorHandler(new StdoutErrorHandler());
                xmlReader.parse(xmlFile);
            } catch (ParserConfigurationException pce) {
                pce.printStackTrace();
                System.exit(1);
            } catch (SAXException saxe) {
                saxe.printStackTrace();
                System.exit(1);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }
        }

        /**
         * Returns parsed {@code UserProfile} objects from {@code xmlFile}.
         * 
         * @return profiles
         */
        public Collection<UserProfile> getProfiles() {
            return this.profiles;
        }

        /**
         * Receive notification of the beginning of an element. The Parser will
         * invoke this method at the beginning of every element in the XML
         * document; there will be a corresponding endElement event for every
         * startElement event (even when the element is empty). All of the
         * element's content will be reported, in order, before the
         * corresponding endElement event.
         * 
         * @param namespaceURI the location of the element's name that will be
         *        parsed.
         * @param localName is the name of the specific element that will be
         *        parsed.
         * @param qualifiedName type of element that will be parsed.
         * @param elementAttributes contains the attributes attached to the
         *        element.
         */
        @Override
        public void startElement(
                @SuppressWarnings("unused") String namespaceURI,
                @SuppressWarnings("unused") String localName,
                @SuppressWarnings("unused") String qualifiedName,
                Attributes elementAttributes) {
            if (-1 != elementAttributes.getIndex("name")) {
                Map<String, String> hm = new HashMap<String, String>(10);
                String requestType = elementAttributes.getValue("requestType");
                StringTokenizer st = new StringTokenizer(requestType, ",");
                String renderService = "";
                
                while (st.hasMoreTokens()) {
                    renderService = st.nextToken();
                    hm.put(renderService, renderService);
                }
                RequestAuthenticator.UserProfile client
                        = new RequestAuthenticator.UserProfile(
                                elementAttributes.getValue("name"),
                                elementAttributes.getValue("password"),
                                Integer.parseInt(elementAttributes.getValue(
                                        "maxPriority")),
                                Double.parseDouble(elementAttributes.getValue(
                                        "maxCost")),
                                hm);
                this.profiles.add(client);
            }
        }

        /**
         * Required to throw exceptions that may occur during parsing an xml.
         */
        private class StdoutErrorHandler implements ErrorHandler {
            /**
             * Sends out a fatal error about parsing an xml file.
             */
            public void fatalError(SAXParseException s) {
                System.out.println("Fatal Error occurred " + s);
            }

            /**
             * Sends out a recoverable error about parsing an xml file.
             */
            public void error(SAXParseException s) {
                System.out.println("Error occurred " + s);
            }

            /**
             * Sends out a warning about parsing an xml file.
             */
            public void warning(SAXParseException s) {
                System.out.println("warning occurred " + s);
            }
        }
    }
}
