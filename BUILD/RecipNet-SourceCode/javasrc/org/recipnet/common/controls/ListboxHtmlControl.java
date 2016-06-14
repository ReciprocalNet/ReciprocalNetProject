/*
 * Reciprocal Net project
 * 
 * ListboxHtmlControl.java
 * 
 * 25-Jun-2004: cwestnea wrote the first draft
 * 04-Aug-2004: cwestnea fixed bug #1306 in setValue()
 * 05-Aug-2004: midurbin altered optionAdditionDone() to compensate for the new
 *     handling of 'failedValidation' by HtmlControl
 * 16-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 02-Nov-2004: midurbin added generateCopy() to fix bug #1439
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 08-Apr-2005: midurbin renamed generateHtmlToDisplay() to
 *              generateHtmlToDisplayAndPersistValue() and updated
 *              generateHtmlToDisplayAsLable() to conform to new specification
 * 26-Jul-2005: midurbin replaced all the special handling for late validation
 *              with isParsedValueValid()
 * 26-Jan-2006: jobollin updated docs; removed unusued imports; formatted
 *              source; added support for multiple selection
 */

package org.recipnet.common.controls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;

/**
 * A {@code ListboxHtmlControl} maintains a {@code String} value that may be
 * selected in a dropdown listbox corresponding to the &lt;select&gt; HTML tag.
 * Constraints for the display of the listbox and the value may be provided as
 * attributes to the tag. In order to chose an initial selection, its value
 * should be put into the {@code initialValue} attribute.
 */
public class ListboxHtmlControl extends HtmlControl {

    /**
     * An error flag that may be reported when the parsed value corresponds to
     * an option that is not a valid selection. This is used in the
     * implementation of {@code ErrorSupplier}.
     */
    public static final int DISABLED_VALUE_SELECTED
            = HtmlControl.getHighestErrorFlag() << 1;

    /**
     * An error flag that may be reported when the parsed value does not
     * correspond to an option. This is used in the implementation of
     * {@code ErrorSupplier}.
     */
    public static final int UNKNOWN_VALUE_SELECTED
            = HtmlControl.getHighestErrorFlag() << 2;

    /**
     * A {@code boolean} flag indicating whether this control allows the user to
     * select multiple options; default {@code false}
     */
    private boolean allowMultipleSelection;

    /**
     * A {@code String} with which {@link #getRawValue(ServletRequest)} will
     * separate multiple selected values if they occur; default ";"
     */
    private String multiSelectionDelimiter;

    /**
     * The list of {@code Option} objects that may be selected in this listbox.
     */
    private List<ListboxHtmlControl.Option> addedOptions;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        setFailedValidationHtml("<font color=\"RED\">*</font>");
        this.addedOptions = new ArrayList<Option>();
        allowMultipleSelection = false;
        multiSelectionDelimiter = ", ";
    }

    /**
     * Determines whether this control is configured to recognize multiple
     * selected values
     * 
     * @return {@code true} if this control will recognize multiple values;
     *         {@code false} if it will not
     */
    public boolean isMultiple() {
        return allowMultipleSelection;
    }

    /**
     * Configures whether or not this control will recognize multiple selected
     * values
     * 
     * @param allowMultipleSelection {@code true} if this control should accept
     *        multiple values; {@code false} if it should only recognize one
     */
    public void setMultiple(boolean allowMultipleSelection) {
        this.allowMultipleSelection = allowMultipleSelection;
    }

    /**
     * Retrieves the delimiter string used to seperate multiple distinct values
     * in the raw value string and in the label html format of this control
     * 
     * @return the delimiter {@code String}
     */
    public String getMultiSelectionDelimiter() {
        return multiSelectionDelimiter;
    }

    /**
     * Sets the delimiter string used to seperate multiple distinct values in
     * the raw value string and in the label html format of this control
     * 
     * @param multiSelectionDelimiter the delimiter {@code String}
     */
    public void setMultiSelectionDelimiter(String multiSelectionDelimiter) {
        if ((multiSelectionDelimiter == null)
                || (multiSelectionDelimiter.length() == 0)) {
            throw new IllegalArgumentException(
                    "Delimiter must be a nonempty string");
        } else {
            this.multiSelectionDelimiter = multiSelectionDelimiter;
        }
    }

    /**
     * Populates the {@code options} list.
     * 
     * @param enabled a boolean indicating whether the option to be added should
     *        be enabled
     * @param label the label of the option
     * @param value the value of the option
     * @throws IllegalStateException if an option is added after the
     *         {@code FETCHING_PHASE}
     */
    public void addOption(boolean enabled, String label, String value) {
        if ((getPage().getPhase() == HtmlPage.PROCESSING_PHASE)
                || (getPage().getPhase() == HtmlPage.RENDERING_PHASE)) {
            throw new IllegalStateException();
        }
        this.addedOptions.add(new Option(enabled, label, value));
    }

    /**
     * {@inheritDoc}. This version packs all submitted values into a single
     * String if this control is configured to accept multiple selections.
     * Values will be delimited by the configured
     * {@code multiSelectionDelimiter} string.
     * 
     * @see HtmlControl#getRawValue(ServletRequest)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected String getRawValue(ServletRequest request) {

        /*
         * FIXME: this business of packing multiple values into a single String
         * is a hack around the problem that HtmlControl has no built-in support
         * for controls that may have multiple associated values. A far-reaching
         * redesign will be necessary to smooth it out.
         */
        if (isMultiple()) {
            Map<String, String[]> pmap
                    = request.getParameterMap();
            String[] values = pmap.get(getId());

            if (values == null) {
                return null;
            } else {
                String delim = getMultiSelectionDelimiter();
                StringBuilder sb = new StringBuilder();

                for (String s : values) {
                    sb.append(delim).append(s);
                }

                return sb.toString().substring(delim.length());
            }
        } else {
            return super.getRawValue(request);
        }
    }

    /**
     * {@inheritDoc}; this version returns the raw value if multiple selection
     * is disabled, or a (possibly empty) set of strings parsed from the raw
     * value if multiple selection is enabled
     * 
     * @param rawValue a {@code String} computed from the POST parameter(s) that
     *        correspond(s) to the value of this control
     *        
     * @return if multiple selection is disabled for this control then
     *         {@code rawValue}; otherwise, if the raw value is {@code null}
     *         then an empty collection, else the result of spliting the value
     *         around the configured multiple selection delimiter
     *         
     * @see HtmlControl#parseValue(String)
     * @see #getRawValue(ServletRequest)
     */
    @Override
    protected Object parseValue(String rawValue) {
        if (isMultiple()) {
            return ((rawValue == null) ? Collections.<String> emptySet()
                    : rawValue.split(
                            Pattern.quote(getMultiSelectionDelimiter()), -1));
        } else {
            return rawValue;
        }
    }

    /**
     * {@inheritDoc}; in addition to the checks performed by the superclass,
     * this method returns false unless the 'parsedValue' represents one or more
     * of the previously added and enabled options, with no unknown options.
     * 
     * @param parsedValue
     */
    @Override
    protected boolean isParsedValueValid(Object parsedValue) {
        boolean isSuperValid = super.isParsedValueValid(parsedValue);
        Collection<String> values = new ArrayList<String>(
                convertValueToStringCollection(parsedValue));

        // make sure the option(s) corresponding to the value exists and is
        // enabled
        for (Option option : this.addedOptions) {
            if (values.contains(option.value)) {
                if (!option.enabled) {
                    setErrorFlag(DISABLED_VALUE_SELECTED);
                    return false;
                } else {
                    values.remove(option.value);
                }
            }
        }

        if (values.isEmpty()) {
            return isSuperValid;
        } else {
            setErrorFlag(UNKNOWN_VALUE_SELECTED);
            return false;
        }
    }

    /**
     * Overrides {@code HtmlControl} to generate HTML for this control.
     * 
     * @param editable indicates whether the field should be editable. If the
     *        field is not editable the value is displayed and a hidden field is
     *        used.
     * @param failedValidation indicates that the field has an invalid value.
     * @param value the value of the control.
     * @param rawValue the value from the POST parameters, this won't differ
     *        from the value in most cases because it is impossible to know at
     *        parse time whether or not a value is valid.
     * @return a {@code String} of HTML code for a textbox or plain text,
     *         depending on the editability of the field.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            @SuppressWarnings("unused")
            boolean failedValidation, Object value, @SuppressWarnings("unused")
            String rawValue) {
        if (getPage().isBrowserNetscape4x() && !editable) {
            // Netscape 4.8 does not support the 'disabled' attribute for
            // input tags. The best approximation in this case is to display
            // the value in 'label' mode and to generate invisible value
            // persistence HTML
            return generateHtmlToDisplayForLabel(value).concat(
                    generateHtmlToInvisiblyPersistValue(value));
        } else {
            StringBuilder htmlCode = new StringBuilder("<select name=\"");
            Collection<String> valuesToDisplay
                    = convertValueToStringCollection(value);

            htmlCode.append(getId());
            htmlCode.append("\"");
            htmlCode.append(" id=\"");
            htmlCode.append(getId());
            htmlCode.append("\"");
            htmlCode.append(editable ? "" : " disabled=\"disabled\"");
            htmlCode.append(allowMultipleSelection ? " multiple=\"true\"" : "");
            htmlCode.append(getExtraHtmlAttributesAsString());
            htmlCode.append("> ");
            for (Option option : this.addedOptions) {
                htmlCode.append(" <option value=\"");
                htmlCode.append(option.value);
                htmlCode.append("\"");
                if (valuesToDisplay.contains(option.value)) {
                    htmlCode.append(" selected=\"true\"");
                }
                htmlCode.append(">");
                htmlCode.append(option.label);
                htmlCode.append("</option>");
            }
            htmlCode.append("</select>");

            return htmlCode.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateHtmlToDisplayForLabel(Object value) {
        Collection<String> valuesToDisplay
                = convertValueToStringCollection(value);

        if (valuesToDisplay.isEmpty() || getFailedValidation()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            String delim = getMultiSelectionDelimiter();

            for (Option option : this.addedOptions) {
                if (valuesToDisplay.contains(option.value)) {
                    sb.append(delim).append(option.label);
                }
            }

            if (sb.length() > 0) {
                return "<span " + getExtraHtmlAttributesAsString() + ">"
                        + sb.toString().substring(delim.length()) + "</span>";
            } else {
                return "";
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String generateHtmlToInvisiblyPersistValue(Object value) {
        Collection<String> valuesToDisplay
                = convertValueToStringCollection(value);

        if (valuesToDisplay.isEmpty() || getFailedValidation()) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for (Option option : this.addedOptions) {
                if (valuesToDisplay.contains(option.value)) {
                    sb.append(super.generateHtmlToInvisiblyPersistValue(
                            option.value));
                }
            }

            return sb.toString();
        }
    }

    /** {@inheritDoc} */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        ListboxHtmlControl dc
                = (ListboxHtmlControl) super.generateCopy(newId, map);

        dc.addedOptions = new ArrayList<Option>(this.addedOptions);

        return dc;
    }

    /**
     * Returns the numerically largest error code defined by this
     * {@code ErrorSupplier}, to facilitate subclasses defining their own error
     * codes
     * 
     * @return the numerically greatest error code implemented by this error
     *         supplier
     */
    protected static int getHighestErrorFlag() {
        return UNKNOWN_VALUE_SELECTED;
    }

    /**
     * Converts an object such as is returned by {@link #parseValue(String)} to
     * a collection of strings so as to enable uniform handling of different
     * types of values
     * 
     * @param value the {@code Object} to convert; expected to be of the type
     *        and form returned by {@code parseValue()}
     * @return a {@code Collection&lt;String&gt;)} of the zero or more distinct
     *         {@code String} values representing the selected options of this
     *         listbox control; users should not assume that the returned
     *         collection is modifiable
     */
    @SuppressWarnings("unchecked")
    protected Collection<String> convertValueToStringCollection(Object value) {
        Collection<String> valueCollection;

        if (value == null) {
            valueCollection = Collections.<String> emptySet();
        } else if (value instanceof Collection) {
            valueCollection = (Collection<String>) value;
        } else if (value instanceof String[]) {
            valueCollection = Arrays.asList((String[]) value);
        } else {
            valueCollection = Collections.singleton(value.toString());
        }

        return valueCollection;
    }

    /**
     * An internal state class which holds data on an option registered with
     * this listbox.
     */
    private class Option {

        /** Flag indicating whether or not this option may be selected */
        public boolean enabled;

        /** The label of this option */
        public String label;

        /** The value of this option */
        public String value;

        /** Creates an fully filled option */
        public Option(boolean enabled, String label, String value) {
            this.enabled = enabled;
            this.label = label;
            this.value = value;
        }
    }
}
