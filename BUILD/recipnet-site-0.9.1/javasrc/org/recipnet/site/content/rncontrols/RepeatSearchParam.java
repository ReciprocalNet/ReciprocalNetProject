/*
 * Reciprocal Net project
 * 
 * RepeatSearchParam.java
 * 
 * 12-Apr-2005: midurbin wrote first draft
 * 30-Mar-2006: jobollin fixed bug #1771 by overriding
 *              copyTransientPropertiesFrom()
 * 30-Mar-2006: jobollin fixed bug #1771 by overriding
 *              copyTransientPropertiesFrom()
 */

package org.recipnet.site.content.rncontrols;

import javax.servlet.jsp.JspException;

import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LinkParam;

/**
 * <p>
 * A custom tag that adds the following parameter to a {@code LinkHtmlElement}
 * (property names are in single quotes):
 * <blockquote>
 * 'name'='searchPagePath'?repeatSearchId='searchIdParamName'
 * </blockquote>
 * where the properties are defined so:
 * <dl>
 * <dt>name</dt>
 * <dd>the name of the parameter expected by the login page to contain the URL
 * of the page to which the newly logged in user should be redirected (this
 * property is defined on the superclass)</dd>
 * <dt>searchPagePath</dt>
 * <dd>the path (relative to the servlet context path) of a search page that
 * will allow previously executed searches to be executed again when the
 * parameter "repeatSearchId" is set to a recently performed search.</dd>
 * <dt>searchIdParamName</dt>
 * <dd>the name of the parameter to the page continaing this tag that contains
 * the search id.</dd>
 * </dl>
 * the value of the 'searchIdParamName' will not be used here, but instead the
 * value of the request parameter whose name is that of the 'searchIdParamName'.
 * </p>
 */
public class RepeatSearchParam extends LinkParam {

    /**
     * A required property representing the name for the parameter that is
     * expected by the search page to be the search id.
     */
    private String searchIdParamName;

    /**
     * A required property representing the path (relative to the context path)
     * of the search page.
     */
    private String searchPagePath;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.searchIdParamName = null;
        this.searchPagePath = null;
    }

    /**
     * @param paramName the name of the parameter expected by the search page to
     *        contain the search id
     */
    public void setSearchIdParamName(String paramName) {
        this.searchIdParamName = paramName;
    }

    /**
     * @return the name of the parameter expected by the search page to contain
     *         the search id
     */
    public String getSearchIdParamName() {
        return this.searchIdParamName;
    }

    /**
     * @param path the path (relative to the context path) of the search page
     */
    public void setSearchPagePath(String path) {
        this.searchPagePath = path;
    }

    /** @return the path (relative to the context path) of the search page */
    public String getSearchPagePath() {
        return this.searchPagePath;
    }

    /**
     * Overrides {@code LinkParam}; the current implementation sets the 'value'
     * property of this {@code LinkParam}.
     */
    @Override
    public int onFetchingPhaseAfterBody() throws JspException {
        setValue(this.searchPagePath
                + "?repeatSearchId="
                + this.pageContext.getRequest().getParameter(
                        this.searchIdParamName));
        return super.onFetchingPhaseAfterBody();
    }

    /**
     * {@inheritDoc}. This version preserves the current 'value' (which is
     * computed, not set as a tag attribute on the proxy element).
     * 
     * @see LinkParam#copyTransientPropertiesFrom(HtmlPageElement)
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        String savedValue = getValue();

        super.copyTransientPropertiesFrom(source);
        setValue(savedValue);
    }
}
