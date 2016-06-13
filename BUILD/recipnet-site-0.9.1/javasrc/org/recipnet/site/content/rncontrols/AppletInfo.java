/*
 * Reciprocal Net project
 * 
 * AppletInfo.java
 * 
 * 15-Jun-2005: ekoperda wrote first draft, borrowing heavily from an
 *              identically-named inner class that had been part of
 *              JammAppletTag previously
 * 24-Jan-2006: jobollin formatted the source; updated docs
 * 23-Jun-2006: jobollin updated this class to properly escape attribute values
 *              and element content that it produces (with use of the
 *              HtmlControl.escapeXXX() static methods) 
 */

package org.recipnet.site.content.rncontrols;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.recipnet.common.controls.HtmlControl;

/**
 * Inner helper class which contains the data necessary to display an applet and
 * overrides {@code toString()} to return the HTML to display the applet.
 */
public class AppletInfo {
    
    /*
     * Note: the following string constants are inserted into HTML code without
     * escaping.  Their values may need to be suitably escaped if ever they
     * are changed to contain HTML metacharacters recognized in attribute values
     * (or element content).
     */
    
    private static final String SUN_JAVA_PLUGIN_DYNAMIC_VERSIONING_CLASS_ID
            = "clsid:8AD9C840-044E-11D1-B3E9-00805F499D93";

    private static final String SUN_JAVA_PLUGIN_NEWEST_CAB_URL
            = "http://java.sun.com/update/1.5.0/"
                    + "jinstall-1_5_0-windows-i586.cab";

    private static final String SUN_JAVA_PLUGIN_NEWEST_PLUGINS_PAGE
            = "http://java.sun.com/j2se/1.5.0/download.html";

    /**
     * An enum defining the various Java plugin levels supported by this class
     */
    public static enum PluginVersion {
        
        /**
         * An enum member representing support for all Java plugin levels
         */
        ANY,
        
        /**
         * An enum member representing support for Java 1.2 or higher plugins
         */
        JAVA_1_2_OR_HIGHER,
        
        /**
         * An enum member representing support for Java 1.5 or higher plugins
         */
        JAVA_1_5_OR_HIGHER
    }

    /**
     * The plugin version for which this {@code AppletInfo} is configured
     */
    private PluginVersion pluginVersion;

    /** the fully-qualified Java name of the applet's main class */
    private String code;

    /** the name of the jar file containing the applet */
    private String archive;

    /** the path to the archive */
    private String archivePrefix;

    /** the height of the applet, in pixels */
    private String height;

    /** the width of the applet, in pixels */
    private String width;

    /** extra parameters; takes a String key with a String value. */
    private Map<String, String> params;

    /**
     * Initializes a {@code AppletInfo} with default values for all fields
     */
    public AppletInfo() {
        this.pluginVersion = PluginVersion.ANY;
        this.code = null;
        this.archive = null;
        this.archivePrefix = null;
        this.height = "400";
        this.width = "400";
        this.params = new HashMap<String, String>();
    }

    /**
     * Sets the plugin version to be supported by this {@code AppletInfo}
     * 
     * @param pluginVersion the {@code PluginVersion} representing the version
     *        to support
     */
    public void setPluginVersion(PluginVersion pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    /**
     * Retrieves the plugin version for which this {@code AppletInfo} is
     * configured
     * 
     * @param code the {@code PluginVersion} representing the version
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the name of the archive in which the applet's classes are to be
     * found
     * 
     * @param archive the name of the archive as a {@code String}
     */
    public void setArchive(String archive) {
        this.archive = archive;
    }

    /**
     * Sets the path to the applet's archive file
     * 
     * @param archivePrefix the path to the archive as a {@code String}; if
     *        non-empty, should end with a slash (/) character
     */
    public void setArchivePrefix(String archivePrefix) {
        this.archivePrefix = archivePrefix;
    }

    /**
     * Sets the applet's height
     * 
     * @param height the desired height, in pixels
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     * Sets the applet's width
     * 
     * @param width the applet's width, in pixels
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Adds an (additional) applet parameter. Any existing parameter of the same
     * name is replaced. Upon output, this class will handle escaping HTML
     * metacharacters in the name and value as appropriate for the context, so
     * the user should not do this, but note that escaping is somewhat futile
     * with respect to the name because it may be used as an attribute name, to
     * which escaping conventions do not apply, and which cannot legally contain
     * any of the escaped characters in any case.  Indeed, there are other
     * ways in which the name could be invalid, as well; this method and this
     * class in general ignore all such potential problems.
     * 
     * @param name the parameter name as a {@code String}
     * @param value the parameter value as a {@code String}
     */
    public void addParam(String name, String value) {
        this.params.put(name, value);
    }

    /**
     * Returns a string representation of this {@code AppletInfo}.  In this
     * case, the representation consists of HTML code appropriate for inserting
     * the applet described by this info into a web page
     * 
     * @return HTML code corresponding to this {@code AppletInfo}, as a
     *         {@code String}
     */
    @Override
    public String toString() {
        switch (this.pluginVersion) {
            case ANY:
                StringBuffer sb = new StringBuffer();
                sb.append("<applet code=\"");
                sb.append(HtmlControl.escapeAttributeValue(this.code));
                sb.append("\" codebase=\"");
                sb.append(HtmlControl.escapeAttributeValue(this.archivePrefix));
                sb.append("\" archive=\"");
                sb.append(HtmlControl.escapeAttributeValue(this.archive));
                sb.append("\"");
                sb.append(" width=\"");
                sb.append(this.width);
                sb.append("\" height=\"");
                sb.append(this.height);
                sb.append("\">");
                for (Entry<String, String> entry : this.params.entrySet()) {
                    sb.append("<param name=\"");
                    sb.append(HtmlControl.escapeAttributeValue(entry.getKey()));
                    sb.append("\" value=\"");
                    sb.append(HtmlControl.escapeAttributeValue(
                            entry.getValue()));
                    sb.append("\" />");
                }
                sb.append("You will need to download and install a Java");
                sb.append(" plug-in in order to view this applet.  Download");
                sb.append(" Sun's Java plug-in from <a href=\"");
                sb.append(SUN_JAVA_PLUGIN_NEWEST_PLUGINS_PAGE);
                sb.append("\">here</a>.</applet>");
                return sb.toString();

            case JAVA_1_2_OR_HIGHER:
                return generateHtmlFor1_2Or1_5("1,2,0,0", "1.2");

            case JAVA_1_5_OR_HIGHER:
                return generateHtmlFor1_2Or1_5("1,5,0,0", "1.5");
        }

        // Can't happen because all cases were addressed previously.
        throw new IllegalStateException();
    }

    /**
     * Generates HTML supporting insertion of applets requiring browser support
     * for Java 2 or higher.  This HTML makes an effort to be compatible with
     * all Java-supporting browsers
     * 
     * @param cabUrlFragment version number codes by which the suitability
     *        of any available Java support can be determined, as a
     *        {@code String}
     * @param version a plain {@code String} version of the required Java level
     *        for this applet
     * 
     * @return HTML code corresponding to this {@code AppletInfo}, as a
     *         {@code String}
     */
    private String generateHtmlFor1_2Or1_5(String cabUrlFragment,
            String version) {
        StringBuffer sb = new StringBuffer();
        sb.append("<object classid=\"");
        sb.append(SUN_JAVA_PLUGIN_DYNAMIC_VERSIONING_CLASS_ID);
        sb.append("\" codebase=\"");
        sb.append(SUN_JAVA_PLUGIN_NEWEST_CAB_URL);
        sb.append("#Version=");
        sb.append(cabUrlFragment);
        sb.append("\"");
        addParam("code", this.code);
        addParam("archive", this.archivePrefix + this.archive);
        addParam("type", "application/x-java-applet;version=" + version);
        sb.append(" width=\"");
        sb.append(this.width);
        sb.append("\" height=\"");
        sb.append(this.height);
        sb.append("\">");
        for (Entry<String, String> entry : this.params.entrySet()) {
            sb.append("<param name=\"");
            sb.append(HtmlControl.escapeAttributeValue(entry.getKey()));
            sb.append("\" value=\"");
            sb.append(HtmlControl.escapeAttributeValue(entry.getValue()));
            sb.append("\" />");
        }
        sb.append("<comment><embed type=\"application/x-java-applet;");
        sb.append("version=");
        sb.append(version);
        sb.append("\" java_code=\"");
        sb.append(HtmlControl.escapeAttributeValue(this.code));
        sb.append("\" java_archive=\"");
        sb.append(HtmlControl.escapeAttributeValue(this.archivePrefix));
        sb.append(HtmlControl.escapeAttributeValue(this.archive));
        sb.append("\" width=\"");
        sb.append(this.width);
        sb.append("\" height=\"");
        sb.append(this.height);
        sb.append("\" pluginspage=\"");
        sb.append(SUN_JAVA_PLUGIN_NEWEST_PLUGINS_PAGE);
        sb.append("\"");
        for (Entry<String, String> entry : this.params.entrySet()) {
            sb.append(" ");
            sb.append(entry.getKey());
            sb.append("=\"");
            sb.append(HtmlControl.escapeAttributeValue(entry.getValue()));
            sb.append("\"");
        }
        sb.append("></embed></comment></object>");
        
        return sb.toString();
    }
}
