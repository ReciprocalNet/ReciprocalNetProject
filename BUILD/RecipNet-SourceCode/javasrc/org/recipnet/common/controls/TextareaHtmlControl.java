/*
 * Reciprocal Net project
 * 
 * TextareaHtmlControl.java
 * 
 * 09-Dec-2003: ekoperda wrote first draft
 * 19-Feb-2004: midurbin wrote second draft
 * 27-Feb-2004: midurbin added an implementation of ErrorSupplier
 * 17-May-2004: midurbin added generateHtmlToDisplayForLabel() to add support
 *              for HtmlControl's 'displayAsLabel' attribute
 * 24-Jun-2004: cwestnea modified generateHtmlToDisplay() to reflect name 
 *              change of HtmlPage.isBrowserNetscape4x()
 * 25-Jun-2004: cwestnea changed the style of referring to error constants
 * 23-Aug-2004: midurbin added getHighestErrorFlag() and setErrorFlag()
 * 24-Sep-2004: midurbin moved error supplier implementation and 'required'
 *              validation check to HtmlControl
 * 08-Apr-2005: midurbin updated generateHtmlToDisplayForLabel() to meet new
 *              specification
 * 26-Jul-2005: midurbin moved the validation checking code out of parseValue()
 *              and into isParsedValueValid()
 * 19-Jan-2006: jobollin changed the value of NO_LIMIT to Integer.MAX_VALUE
 * 23-Jan-2006: jobollin moved considerable code to the new superclass
 *              AbstractTextHtmlControl
 */

package org.recipnet.common.controls;

/**
 * This is the class for a phase-recognizing custom tag that persists a
 * {@code String} that is exposed to the user as a textbox. Various attributes
 * are made available to control the appearance and validation options for the
 * textarea.
 */
public class TextareaHtmlControl extends AbstractTextHtmlControl {
    
    /**
     * This is an optional attribute that is initialized by {@code reset()} and
     * may be altered by its 'setter' method, {@code setRows()}. It is a
     * 'transient' variable in that it may change from phase to phase and may be
     * copied by a call to {@code copyTransientPropertiesFrom()}.
     */
    private int rows;

    /**
     * This is an optional attribute that is initialized by {@code reset()} and
     * may be altered by its 'setter' method, {@code setRows()}. It is a
     * 'transient' variable in that it may change from phase to phase and may be
     * copied by a call to {@code copyTransientPropertiesFrom()}.
     */
    private int columns;

    /**
     * This is an optional attribute that is initialized by {@code reset()} and
     * may be altered by its 'setter' method, {@code setRows()}. It is a
     * 'transient' variable in that it may change from phase to phase and may be
     * copied by a call to {@code copyTransientPropertiesFrom()}.
     */
    private String wrapSetting;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        super.setFailedValidationHtml("<font color=\"red\">*</font>");
        this.rows = 5;
        this.columns = 75;
        this.wrapSetting = "virtual";
    }

    /** @return the number of rows for this textarea */
    public int getRows() {
        return this.rows;
    }

    /** @param rows the number of rows for this textarea */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /** @return the number of columns for this textarea */
    public int getColumns() {
        return this.columns;
    }

    /** @param columns the number of columns for this textarea */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    /** @return the wrap setting for this control */
    public String getWrapSetting() {
        return this.wrapSetting;
    }

    /** @param wrapSetting the wrap setting for this control */
    public void setWrapSetting(String wrapSetting) {
        this.wrapSetting = wrapSetting;
    }

    /**
     * Overrides {@code HtmlControl} to generate HTML for this control.
     * 
     * @param editable indicates whether the field should be editable. If the
     *        field is not editable the value is displayed and a hidden field is
     *        used.
     * @param failedValidation indicates that the field has an invalid value.
     * @param value the value of the control.
     * @param rawValue the value from the POST parameters, this will differ from
     *        the value in cases of validation failures to preserve the validity
     *        of the {@code value}.
     * @return a {@code String} of HTML code for a textarea or plain text,
     *         depending on the editability of the field.
     */
    @Override
    protected String generateHtmlToDisplayAndPersistValue(boolean editable,
            boolean failedValidation, Object value, String rawValue) {
        String valueToDisplay = (failedValidation ? rawValue : (String) value);
        
        if (valueToDisplay == null) {
            valueToDisplay = "";
        }
        if (isShouldTrim()) {
            valueToDisplay = valueToDisplay.trim();
        }
        if (super.getPage().isBrowserNetscape4x() && !editable) {
            // Netscape 4.8 does not support the 'disabled' attribute for
            // button input tags. Therefore, the equivalent disabled button
            // for this browser is simply not to display the button.
            return "<input type=\"hidden\" name=\"" + super.getId() + "\""
                    + super.getExtraHtmlAttributesAsString() + " value=\""
                    + HtmlControl.escapeAttributeValue(valueToDisplay)
                    + "\" />" + valueToDisplay;
        } else {
            return "<textarea name=\"" + super.getId() + "\"" + " id=\""
                    + super.getId() + "\"" + " rows=\"" + this.rows + "\""
                    + " cols=\"" + this.columns + "\"" + " wrap=\""
                    + this.wrapSetting + "\""
                    + (!editable ? " readonly=\"readonly\"" : "")
                    + super.getExtraHtmlAttributesAsString() + ">"
                    + HtmlControl.escapeNestedValue(valueToDisplay)
                    + "</textarea>";
        }
    }

    /**
     * Overrides {@code HtmlControl}; the current implementation returns an
     * escaped textual representation of {@code value}.
     * 
     * @param value the current value of this text area
     */
    @Override
    protected String generateHtmlToDisplayForLabel(Object value) {
        String valueToDisplay = (String) value;
        
        if (valueToDisplay == null) {
            valueToDisplay = "";
        }
        if (isShouldTrim()) {
            valueToDisplay = valueToDisplay.trim();
        }
        return "<span " + getExtraHtmlAttributesAsString() + ">"
                + HtmlControl.escapeNestedValue(valueToDisplay) + "</span>";
    }

    /**
     * Overrides {@code HtmlPageElement} to copy all transient fields to this
     * object from {@code source} if it is a {@code TextareaHtmlControl}.
     * 
     * @param source an {@code HtmlPageElement} or child class whose transient
     *        fields are being copied to this object.
     */
    @Override
    protected void copyTransientPropertiesFrom(HtmlPageElement source) {
        super.copyTransientPropertiesFrom(source);
        TextareaHtmlControl src = (TextareaHtmlControl) source;
        this.rows = src.rows;
        this.columns = src.columns;
        this.wrapSetting = src.wrapSetting;
    }
}
