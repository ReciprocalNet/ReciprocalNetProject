/*
 * Reciprocal Net project
 * 
 * TextTypeSelector.java
 *
 * 15-Oct-2004: midurbin wrote the first draft
 * 14-Dec-2004: eisiorho changed texttype references to use new class
 *              SampleTextBL
 * 13-Jan-2005: midurbin updated onRegistrationPhaseBeforeBody() to sort fields
 *              by their localized names
 * 28-Jan-2005: midurbin added "Select Field Type" as the default option with a
 *              value of SampleTextInfo.INVALID_TYPE
 * 22-Feb-2005: ekoperda altered typecast in onRegistrationPhaseBeforeBody()
 *              to ensure compatability with JDK 1.5
 * 14-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ListboxHtmlControl;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.bl.SampleWorkflowBL;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.wrapper.LanguageHelper;

/**
 * A extension of {@code ListboxHtmlControl}. This tag lists all the
 * annotations and attributes (excluding local tracking attributes) that may be
 * modified for the action indicated by the required
 * {@code includeFieldsForAction} parameter. Once the user has selected and
 * submitted a field code, it may be easily aquired by a call to
 * {@code getTextType()}.
 */
public class TextTypeSelector extends ListboxHtmlControl {

    /**
     * A required parameter that indicates an action code as defined by
     * {@code SampleWorkflowBL}. This listbox will contain an option for each
     * attribute and annotation that may typically be modified for the action as
     * determined by {@code SampleWorkflowBL.getEligibleFieldCodes()} excluding
     * local tracking attributes.
     */
    private int includeFieldsForAction;

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        super.reset();
        this.includeFieldsForAction = SampleWorkflowBL.INVALID_ACTION;
    }

    /**
     * @param actionCode the action code for the action that determines which
     *        fields will be listed in this listbox
     * @throws IllegalStateException if the {@code actionCode} is not an
     *         attribute or annotation, or if it is a local tracking attribute.
     */
    public void setIncludeFieldsForAction(int actionCode) {
        if ((!SampleTextBL.isAnnotation(actionCode)
                && !SampleTextBL.isAttribute(actionCode))
                || SampleTextBL.isLocalAttribute(actionCode)) {
            throw new IllegalStateException();
        }
        this.includeFieldsForAction = actionCode;
    }

    /**
     * @return an int action code for the action that determines which fields
     *         will be listed in this listbox
     */
    public int getIncludeFieldsForAction() {
        return this.includeFieldsForAction;
    }

    /**
     * Gets the TextType that has been selected. If this method is called before
     * the {@code PARSING_PHASE} or when no value has been selected,
     * {@code SampleTextInfo.INVALID_TYPE} will be returned.
     * 
     * @return the texttype of the selected attribute or annotation or
     *         {@code SampleTextInfo.INVALID_TYPE} if no selection has been made
     *         yet
     */
    public int getTextType() {
        return (getValue() == null) ? SampleTextInfo.INVALID_TYPE
                : Integer.parseInt(getValueAsString());
    }

    /**
     * {@inheritDoc}; this version determines all the eligible annotations and
     * attributes for this listbox based on the {@code includeFieldsForAction}
     * property, and adds options for them.
     * 
     * @throws JspException wrapping other exceptions thrown by
     *         {@code LanguageHelper} while localizing the field names.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);
        List<Integer> eligibleFields = new ArrayList<Integer>(
                SampleWorkflowBL.getEligibleFieldCodes(
                        this.includeFieldsForAction, null));
        LanguageHelper lh
                = LanguageHelper.extract(pageContext.getServletContext());

        /*
         * pre-resolve the Strings for each text type to reduce the number of
         * expensive calls to LanguageHelper.getFieldString()
         */
        final Map<Integer, String> localizedFieldNames
                = new HashMap<Integer, String>();

        try {
            for (Integer fieldCode : eligibleFields) {
                if (localizedFieldNames.get(fieldCode) == null) {
                    localizedFieldNames.put(fieldCode, lh.getFieldString(
                            fieldCode.intValue(),
                            this.pageContext.getRequest().getLocales(), false));
                }
            }
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }

        // sort the eligible fields by localized field names
        Collections.sort(eligibleFields, new Comparator<Integer>() {
            public int compare(Integer obj1, Integer obj2) {
                return (localizedFieldNames.get(obj1)).compareTo(
                        localizedFieldNames.get(obj2));
            }
        });

        // add the default option that translates to "select field type..."
        try {
            addOption(true, lh.getFieldString(SampleTextBL.INVALID_TYPE,
                    this.pageContext.getRequest().getLocales(), false),
                    String.valueOf(SampleTextBL.INVALID_TYPE));
            setInitialValue(String.valueOf(SampleTextInfo.INVALID_TYPE));
        } catch (IOException ex) {
            throw new JspException(ex);
        } catch (ResourceNotFoundException ex) {
            throw new JspException(ex);
        }

        for (Integer fieldCode : eligibleFields) {
            // include only fields that are attributes or annotations
            if (SampleTextBL.isAnnotation(fieldCode.intValue())
                    || SampleTextBL.isAttribute(fieldCode.intValue())) {
                addOption(true, localizedFieldNames.get(fieldCode),
                        fieldCode.toString());
            }
        }

        return rc;
    }
}
