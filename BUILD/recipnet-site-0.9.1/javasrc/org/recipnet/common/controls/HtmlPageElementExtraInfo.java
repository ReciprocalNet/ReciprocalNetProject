/*
 * Reciprocal Net project
 * 
 * HtmlPageElementExtraInfo.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 19-Feb-2004: midurbin wrote second draft
 * 12-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.common.controls;

import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * This class is used at translation time by tomcat to add the code to access
 * scripting variables from the servlet. If there is no id field present, the
 * current implementation simply disallows use of that particular tag as a
 * scripting variable.
 */
public class HtmlPageElementExtraInfo extends TagExtraInfo {

    /**
     * Provides information about the scripting variables exposed by the tag
     * instance described by {@code data}. This version provides information
     * describing a variable referring to the tag instance itself and named
     * according to the tag's user-specified ID, provided that the user in fact
     * specified an ID at all.
     * 
     * @param data a {@code TagData} describing the tag instance about which
     *        scripting variable information is requested
     * @return a {@code VariableInfo[]} describing some or all of the scripting
     *        variables exposed by this tag
     */
    @Override
    public VariableInfo[] getVariableInfo(TagData data) {
        if (data.getId() == null) {
            /*
             * if no id has been set by the user, (s)he is declaring that the
             * tag should not be accessible as a scripting variable
             */
            return null;
        }
        return new VariableInfo[] { new VariableInfo(data.getId(),
                getTagInfo().getTagClassName(), true, VariableInfo.AT_BEGIN) };
    }

    /**
     * Performs translation-time a tag instance described by this info. This
     * version performs no translation-time validation, therefore it always
     * returns {@code true}.
     * 
     * @param data a {@code TagData} containing information about the tag to
     *        validate
     * @return {@code true} if the tag described by {@code data} is valid,
     *         {@code false} if it isn't
     */
    @Override
    public boolean isValid(@SuppressWarnings("unused") TagData data) {
        return true;
    }
}
