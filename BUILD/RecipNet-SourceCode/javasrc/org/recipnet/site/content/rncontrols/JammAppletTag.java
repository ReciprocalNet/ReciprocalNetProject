/*
 * Reciprocal Net project
 * 
 * JammAppletTag.java
 * 
 * 29-Jul-2004: cwestnea wrote first draft
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 18-Aug-2004: cwestnea added appletParam and preferredCrtFileParam
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 01-Apr-2005: midurbin fixed bug #1455 by adding generateCopy()
 * 28-Apr-2005: midurbin added support to use and update existing
 *              UserPreferences whenever an applet is specified
 * 02-May-2005: ekoperda modified onFetchingPhaseBeforeBody() to accommodate
 *              spec changes in RepositoryFiles and FileContext
 * 10-Jun-2005: midurbin updated class to reflect UserPreferencesBL name change
 * 07-Jul-2005: midurbin added call to HtmlPage.removeFormField() to prevent
 *              jamm applet modifications from being propagated
 * 08-Jul-2005: ekoperda removed inner class AppletInfo and made it a proper
 *              class instead
 * 28-Oct-2005: midurbin updated onRegistrationPhaseBeforeBody() to respect the
 *              ALLOW_IMPLICIT_PREF_CHANGES preference
 * 25-Jan-2006: jobollin made this class implement DynamicAttributes;
 *              reformatted the source; removed imports; updated docs
 * 03-Feb-2006: jobollin moved CRT file specification to the new
 *              JammModelElement tag
 * 16-Feb-2006: jobollin caused this tag to emit a codebase URL that is relative
 *              to the server's document root instead of to the page in which
 *              the tag appears (i.e. one that contains the context path), and
 *              inverted this tag's nesting order with respect to the
 *              JammModelElement tag.
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.site.shared.UserPreferences;
import org.recipnet.site.shared.bl.UserBL;

/**
 * This tag outputs HTML required to view a CRT file using one of the JaMM
 * family of applets.  The URL of the specific CRT to view is provided by a
 * cooperating tag (typically a JammModelElement) nested within this one (or by
 * script that explicitly sets the 'modelUrl' property).  If a sample context is
 * available then it is used to set the sampleId and sampleHistoryId parameters
 * used by some of the applets.
 */
public class JammAppletTag extends HtmlPageElement
        implements DynamicAttributes {

    /**
     * Optional attribute specifies the version of JaMM to use. Valid options
     * are 'JaMM1', 'JaMM2', 'miniJaMM', and 'JaMMed', case insensitive. If this
     * property is not set, the jamm applet will be chosen by consulting the
     * preferences associated with the current session.
     */
    private String applet;

    /**
     * Optional attribute, defaults to null; specifies the request parameter
     * from which to get the version of JaMM to use. Furthermore, the specified
     * applet is preserved in the {@code UserPreferences} associated with the
     * current JSP session. If this parameter is not defined, the value of the
     * 'applet' property is used. It is illegal to specify both
     * {@code appletParam} and {@code applet}.
     */
    private String appletParam;

    /**
     * Optional attribute, defaults to '400'; the string representation of the
     * height of the applet in pixels.
     */
    private String height;

    /**
     * Optional attribute, defaults to '400'; the string representation of the
     * width of the applet in pixels.
     */
    private String width;

    /**
     * The name of the CRT file that was displayed. Set in
     * {@code onFetchingPhaseBeforeBody()}.
     */
    private String displayedCrtFile;

    /**
     * The url of the model to display.  Should be set by cooperating tags or
     * script prior to the {@code RENDERING_PHASE}
     */
    private String modelUrl;

    /**
     * A map of the dynamic attributes set on this tag, with attribute names as
     * the keys and attribute values as the values
     */
    private Map<String, Object> dynamicAttributes;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.applet = null;
        this.appletParam = null;
        this.height = "400";
        this.width = "400";
        this.displayedCrtFile = null;
        this.modelUrl = null;
        this.dynamicAttributes = new HashMap<String, Object>();
    }

    /** @return the version of JaMM to use */
    public String getApplet() {
        return this.applet;
    }

    /** @param applet the version of JaMM to use, case insensitive */
    public void setApplet(String applet) {
        if (applet == null) {
            return;
        }
        String lowerApplet = applet.toLowerCase();
        if (!lowerApplet.equals("jamm2") && !lowerApplet.equals("jamm1")
                && !lowerApplet.equals("minijamm")
                && !lowerApplet.equals("jammed")) {
            throw new IllegalArgumentException();
        }
        this.applet = applet;
    }

    /** @param appletParam the parameter containing the applet version */
    public void setAppletParam(String appletParam) {
        this.appletParam = appletParam;
    }

    /** @return the crtfile actually displayed */
    public String getCrtFileDisplayed() {
        return this.displayedCrtFile;
    }

    /** @param height the height of the applet */
    public void setHeight(String height) {
        this.height = height;
    }

    /** @param width the width of the applet */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     * Returns the model URL
     * 
     * @return the model URL as a {@code String}
     */
    public String getModelUrl() {
        return modelUrl;
    }

    /**
     * Sets the URL of the model to display, and extracts from it the name of
     * the displayed CRT file
     * 
     * @param  modelUrl the model URL as a {@code String}
     */
    private void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
        
        /*
         * Parse the file name out of the URL.  This is not foolproof, but it is
         * good enough for our purposes.  The 'displayedCrtFile' variable
         * corresponds to this class's 'crtFileDisplay' property, whose only
         * current use is in JammAppletPreservingLink, to determine whether to
         * display a hyperlink or label.
         */
        if (modelUrl == null) {
            this.displayedCrtFile = null;
        } else {
            this.displayedCrtFile
                   = modelUrl.replaceFirst("\\?.*", "").replaceFirst(".*/", "");
        }
    }

    /**
     * Implements {@code DynamicAttributes} by accepting the specified attribute
     * for use as an additional applet parameter
     * 
     * @see DynamicAttributes#setDynamicAttribute(String, String, Object)
     */
    @SuppressWarnings("unused")
    public void setDynamicAttribute(String uri, String name, Object value)
            throws JspException {
        dynamicAttributes.put(name, value);
    }

    /**
     * {@inheritDoc}. This version finds the enclosing sample and user
     * contexts.
     * 
     * @throws IllegalStateException if no sample or user context can be found
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        UserPreferences prefs
                = (UserPreferences) pageContext.getSession().getAttribute(
                        "preferences");

        // process request parameters
        if ((this.applet != null) && (this.appletParam != null)) {
            // we can't have it both ways
            throw new IllegalStateException();
        } else if (this.appletParam != null) {
            setApplet(pageContext.getRequest().getParameter(
                    this.appletParam));
            getPage().removeFormField(this.appletParam);
            if ((this.applet != null)
                    && UserBL.getPreferenceAsBoolean(
                            UserBL.Pref.ALLOW_IMPLICIT_PREF_CHANGES, prefs)) {
                // set the currently specified applet as the preferred applet
                if (getApplet().equalsIgnoreCase("JaMM1")) {
                    UserBL.setAppletPreference(prefs, UserBL.AppletPref.JAMM1);
                } else if (getApplet().equalsIgnoreCase("JaMM2")) {
                    UserBL.setAppletPreference(prefs, UserBL.AppletPref.JAMM2);
                } else if (getApplet().equalsIgnoreCase("miniJaMM")) {
                    UserBL.setAppletPreference(prefs,
                            UserBL.AppletPref.MINIJAMM);
                } else {
                    // not a valid preference, do nothing...
                }
            }
        }
        if (this.applet == null) {
            // use the preferred applet
            UserBL.AppletPref appletPref = UserBL.getAppletPreference(prefs);
            if (appletPref == UserBL.AppletPref.JAMM1) {
                this.applet = "JaMM1";
            } else if (appletPref == UserBL.AppletPref.JAMM2) {
                this.applet = "JaMM2";
            } else if (appletPref == UserBL.AppletPref.MINIJAMM) {
                this.applet = "miniJaMM";
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}.  This version looks up the surrounding
     * {@code JammModelElement} and obtains from it its effective model URL
     *  
     * @see HtmlPageElement#onFetchingPhaseBeforeBody()
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        JammModelElement modelElement
                = findRealAncestorWithClass(this, JammModelElement.class);
        
        if (modelElement == null) {
            throw new IllegalStateException("No model");
        } else {
            setModelUrl(modelElement.getEffectiveModelUrl());
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int onRenderingPhaseBeforeBody(JspWriter out) throws JspException,
            IOException {
        int rc = super.onRenderingPhaseBeforeBody(out);

        if (shouldRender()) {
            String lowerApplet = getApplet().toLowerCase();
            AppletInfo appletInfo = new AppletInfo();
            SampleContext sampleContext
                    = findRealAncestorWithClass(this, SampleContext.class);
    
            appletInfo.setWidth(this.width);
            appletInfo.setHeight(this.height);
            appletInfo.setArchivePrefix(
                    getPage().getContextPath() + "/applets/jamm/");
            appletInfo.addParam("model", getModelUrl());
            if (lowerApplet.equals("jamm2")) {
                appletInfo.setPluginVersion(
                        AppletInfo.PluginVersion.JAVA_1_2_OR_HIGHER);
                appletInfo.setCode(
                        "org.recipnet.site.applet.jamm.jamm2.JaMM.class");
                appletInfo.setArchive("JaMM2.jar");
                if (sampleContext != null) {
                    appletInfo.addParam("sampleId",
                            String.valueOf(sampleContext.getSampleInfo().id));
                    appletInfo.addParam("sampleHistoryId", String.valueOf(
                            sampleContext.getSampleInfo().historyId));
                }
                appletInfo.addParam("color", "0x9F9F9F");
                appletInfo.addParam("line", getPage().getContextPath()
                        + "/servlet/jammsupport");
                appletInfo.addParam("ortep", getPage().getContextPath()
                        + "/servlet/jammsupport");
                appletInfo.addParam("render", getPage().getContextPath()
                        + "/servlet/jammsupport");
            } else if (lowerApplet.equals("jamm1")) {
                appletInfo.setPluginVersion(AppletInfo.PluginVersion.ANY);
                appletInfo.setCode(
                        "org.recipnet.site.applet.jamm.jamm1.JaMM.class");
                appletInfo.setArchive("JaMM1.jar");
                if (sampleContext != null) {
                    appletInfo.addParam("sampleId", String.valueOf(
                            sampleContext.getSampleInfo().id));
                    appletInfo.addParam("sampleHistoryId", String.valueOf(
                            sampleContext.getSampleInfo().historyId));
                }
                appletInfo.addParam("ortep",
                        getPage().getContextPath() + "/servlet/jammsupport");
                appletInfo.addParam("render",
                        getPage().getContextPath() + "/servlet/jammsupport");
            } else if (lowerApplet.equals("minijamm")) {
                appletInfo.setPluginVersion(AppletInfo.PluginVersion.ANY);
                appletInfo.setCode(
                        "org.recipnet.site.applet.jamm.minijamm.miniJaMM.class");
                appletInfo.setArchive("miniJaMM.jar");
                appletInfo.addParam("background", "eeeeee");
                appletInfo.addParam("border", "true");
                appletInfo.addParam("textcolor", "000000");
            } else if (lowerApplet.equals("jammed")) {
                appletInfo.setPluginVersion(
                        AppletInfo.PluginVersion.JAVA_1_2_OR_HIGHER);
                appletInfo.setCode(
                        "org.recipnet.site.applet.jamm.jamm2.JaMMed.class");
                appletInfo.setArchive("JaMMed.jar");
                appletInfo.addParam("color", "0x9F9F9F");
                appletInfo.addParam("border", "true");
            } else {
                throw new IllegalArgumentException();
            }
            for (Entry<String, Object> entry : dynamicAttributes.entrySet()) {
                appletInfo.addParam(entry.getKey(), entry.getValue().toString());
            }
    
            out.println(appletInfo);
        }

        return rc;
    }

    /**
     * Consulted during the {@code RENDERING_PHASE} to determine whether this
     * tag should produce any output.  This version simply tests whether the
     * configured model URL is {@code null}
     * 
     * @return {@code false} if {@link #getModelUrl()} returns {@code null};
     *         otherwise, {@code true}
     */
    protected boolean shouldRender() {
        return (getModelUrl() != null);
    }

    /**
     * Overrides {@code HtmlPageElement}; the current implementation delegates
     * to the superclass then updates any references to 'owned' controls or
     * referenced ancestor tags using the 'map' parameter that was populated by
     * the superclass' implementation as well as the caller, then makes a deep
     * copy of any complex modifiable member variables before returning the deep
     * copy.
     * 
     * @param newId {@inheritDoc}
     * @param map {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        JammAppletTag dc = (JammAppletTag) super.generateCopy(newId, map);

        dc.dynamicAttributes = new HashMap<String, Object>(
                this.dynamicAttributes);

        return dc;
    }

    /**
     * {@inheritDoc}. This version copies any dynamic attributes assigned to
     * this tag
     * 
     * @see HtmlPageElement#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        this.dynamicAttributes.putAll(
                ((JammAppletTag) source).dynamicAttributes);
    }
}
