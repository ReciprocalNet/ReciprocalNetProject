/*
 * Reciprocal Net project
 * 
 * UnitCellSearchField.java
 * 
 * 11-May-2005: ekoperda wrote first draft
 * 01-Feb-2006: jobollin updated onRegistrationPhaseBeforeBody() to accommodate
 *              changed to RadioButtonHtmlControl
 * 31-Mar-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.DoubleTextboxHtmlControl;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.RadioButtonHtmlControl;

/**
 * A custom tag intended for use within a {@code SearchPage} that exposes user
 * interface fields for specifying search criteria related to unit-cell and
 * reduced-cell searches. The functionality exposed by this control mirrors that
 * implemented by {@code UnitCellSC}. This control defines a number of possible
 * field codes. Depending upon the field code selected by the caller, this
 * control may render itself as a text box, a radio button, or a check box. It
 * is expected that a complete interface for searching on unit cells would
 * include exactly one of these controls per field code defined by this class.
 * All of the {@code UnitCellSearchField} tags that comprise the user interface
 * are expected to be nested within a single {@code UnitCellSearchFieldGroup}
 * tag for consistency's sake. This tag communicates with the
 * {@code UnitCellSearchFieldGroup} tag, and through it to the nearest
 * {@code SearchPage} tag, but does not interface with the {@code SearchPage}
 * tag directly.
 */
public class UnitCellSearchField extends HtmlPageElement {

    public enum FieldCode {
        /** a FieldCode representing searching against the 'a' cell axis */
        A,
        
        /** a FieldCode representing searching against the 'b' cell axis */
        B,
        
        /** a FieldCode representing searching against the 'c' cell axis */
        C,
        
        /** a FieldCode representing searching against the 'alpha' cell angle */
        ALPHA,
        
        /** a FieldCode representing searching against the 'beta' cell angle */
        BETA,
        
        /** a FieldCode representing searching against the 'gamma' cell angle */
        GAMMA,
        
        /** a FieldCode representing the error tolerance in cell parameters */
        PERCENT_ERROR_TOLERANCE,
        
        /** a FieldCode representing matching cell parameters as specified */
        MATCH_UNIT_CELLS_AS_ENTERED,
        
        /** a FieldCode representing matching reduced cell parameters */
        MATCH_REDUCED_CELLS,
        
        /** a FieldCode representing reduced primitive cell parameters */
        P_CENTERING,
        
        /** a FieldCode representing reduced I-centered cell parameters */
        I_CENTERING,
        
        /** a FieldCode representing reduced F-centered cell parameters */
        F_CENTERING,
        
        /** a FieldCode representing reduced A-centered cell parameters */
        A_CENTERING,
        
        /** a FieldCode representing reduced B-centered cell parameters */
        B_CENTERING,
        
        /** a FieldCode representing reduced C-centered cell parameters */
        C_CENTERING,
        
        /** a FieldCode representing reduced R-centered cell parameters */
        R_CENTERING
    }

    /**
     * This is a reference to the nearest {@code UnitCellSearchFieldGroup} tag
     * that encloses this one. It is set during
     * {@link #onRegistrationPhaseBeforeBody(PageContext)}.
     */
    private UnitCellSearchFieldGroup group;

    /**
     * This is a reference to the owned {@code HtmlControl}. Its precise type
     * depends upon the choice of {@code fieldCode}. It is set during
     * {@code onRegistrationPhaseBeforeBody()}.
     */
    private HtmlControl control;

    /**
     * Required tag attribute; possible values are defined by the
     * {@code FieldCode} enum.
     */
    private FieldCode fieldCode;

    /**
     * Optional tag attribute, defaults to null; specifies the {@code tabIndex}
     * attribute on the {@code &lth;input&gt;} tag.
     */
    private String tabIndex;

    /**
     * Overrides {@code HtmlPageElement}. Invoked by {@code HtmlPageElement}
     * whenever this instance begins to represent a custom tag. Resets all
     * member variables to their initial state. Subclasses may override this
     * method but must delegate back to the superclass.
     */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = null;
        this.tabIndex = null;
        this.control = null;
        this.group = null;
    }

    /** Simple getter */
    public FieldCode getFieldCode() {
        return this.fieldCode;
    }

    /** Simple setter */
    public void setFieldCode(FieldCode fieldCode) {
        if ((this.control != null) && (this.fieldCode != fieldCode)) {
            // Don't let the user try to change the type of field after it has
            // already been set.
            throw new IllegalStateException();
        }
        this.fieldCode = fieldCode;
    }

    /** Simple setter */
    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    /** Simple getter */
    public String getTabIndex() {
        return this.tabIndex;
    }

    /**
     * {@inheritDoc} This version instantiates its 'owned' element, finds the
     * nearest {@code UnitCellSearchFieldGroup}, and delegates back to the
     * superclass.
     * 
     * @throws IllegalStateException if this tag is not nested within a
     *         {@code UnitCellSearchFieldGroup} tag.
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // Bind to the UnitCellSearchFieldGroup.
        this.group = findRealAncestorWithClass(
                this, UnitCellSearchFieldGroup.class);
        if (this.group == null) {
            throw new IllegalStateException();
        }

        // Instantiate and register an appropriate owned control.
        switch (this.fieldCode) {
            case A:
            case B:
            case C:
                DoubleTextboxHtmlControl c = new DoubleTextboxHtmlControl();
                
                c.setMinValue(0);
                c.setSize(5);
                c.setRequired(false);
                this.control = c;
                break;
                
            case ALPHA:
            case BETA:
            case GAMMA:
                DoubleTextboxHtmlControl d = new DoubleTextboxHtmlControl();
                
                d.setMinValue(0);
                d.setMaxValue(180);
                d.setSize(5);
                d.setRequired(false);
                this.control = d;
                break;
                
            case PERCENT_ERROR_TOLERANCE:
                DoubleTextboxHtmlControl e = new DoubleTextboxHtmlControl();
                
                e.setMinValue(0);
                e.setSize(5);
                e.setMinFractionalDigits(1);
                e.setRequired(false);
                e.setInitialValue(3.0d);
                this.control = e;
                break;
                
            case MATCH_UNIT_CELLS_AS_ENTERED:
            case MATCH_REDUCED_CELLS:
            case P_CENTERING:
            case I_CENTERING:
            case F_CENTERING:
            case A_CENTERING:
            case B_CENTERING:
            case C_CENTERING:
            case R_CENTERING:
                RadioButtonHtmlControl n = new RadioButtonHtmlControl();
                
                n.setOption(this.fieldCode.toString());
                this.control = n;
                break;
            default:
                throw new IllegalStateException("Unknown field code");
        }
        if (this.tabIndex != null) {
            this.control.addExtraHtmlAttribute("tabIndex", this.tabIndex);
        }
        this.registerOwnedElement(this.control);

        return rc;
    }

    /**
     * The current implementation relays the user-specified value (that was
     * parsed by this tag's owned control) to the nearest enclosing
     * {@code UnitCellSearchFieldGroup} tag.
     * <p>
     * 
     * @inheritDoc
     */
    @Override
    public int onParsingPhaseAfterBody(ServletRequest request)
            throws JspException {
        this.group.reportParsedValueForNestedField(this.fieldCode,
                this.control.getValue());
        
        return super.onParsingPhaseAfterBody(request);
    }

    /**
     * The current implementation retrieves an appropriate value for this tag's
     * owned control from the nearest enclosing {@code UnitCellSearchFieldGroup}
     * tag.
     * <p>
     * 
     * @inheritDoc
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();
        
        this.control.setValue(
                this.group.fetchValueForNestedField(this.fieldCode),
                HtmlControl.EXISTING_VALUE_PRIORITY,
                HtmlControl.LOWEST_PRIORITY);
        
        return rc;
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
        UnitCellSearchField x
                = (UnitCellSearchField) super.generateCopy(newId, map);
        
        x.group = (UnitCellSearchFieldGroup) map.get(this.group);
        x.control = (HtmlControl) map.get(this.control);
        
        return x;
    }
}
