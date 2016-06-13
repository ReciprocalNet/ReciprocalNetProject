/*
 * Reciprocal Net project
 * 
 * SampleHistoryField.java
 * 
 * 27-Jul-2004: midurbin wrote first draft
 * 30-Jul-2004: midurbin added generateCopy() to fix bug #1255
 * 05-Aug-2004: midurbin renamed onFetchingPhase() to
 *              onFetchingPhaseBeforeBody()
 * 25-Feb-2005: midurbin removed the call to setId() on the owned control
 * 01-Mar-2005: ekoperda added recognition of nested FileContexts and function 
 *              selectSampleHistoryInfo()
 * 01-Apr-2005: midurbin fixed bug #1455 in generateCopy()
 * 02-May-2005: ekoperda updated selectSampleHistoryInfo() to accommodate
 *              spec changes in FileContext
 * 14-Jun-2005: midurbin added ACTION_DATE_ROUNDED_TO_DAY and replaced static
 *              field codes with an enum
 * 24-Jun-2005: midurbin added ACTION_CODE FieldCode
 * 17-Feb-2006: jobollin added support for the absence of historical information
 */

package org.recipnet.site.content.rncontrols;

import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.recipnet.common.controls.ErrorSupplier;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.common.controls.HtmlPageElement;
import org.recipnet.common.controls.LabelHtmlControl;
import org.recipnet.site.InconsistentDbException;
import org.recipnet.site.OperationFailedException;
import org.recipnet.site.core.ResourceNotFoundException;
import org.recipnet.site.shared.SampleDataFile;
import org.recipnet.site.shared.db.FullSampleInfo;
import org.recipnet.site.shared.db.SampleHistoryInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;
import org.recipnet.site.shared.db.UserInfo;
import org.recipnet.site.wrapper.CoreConnector;
import org.recipnet.site.wrapper.LanguageHelper;
import org.recipnet.site.wrapper.RequestCache;

/**
 * A control that displays data contained in the {@code SampleHistoryInfo}
 * objects in a {@code FullSampleInfo}. This tag must be nested within a tag
 * that implements either the {@code SampleHistoryContext} or the
 * {@code SampleTextContext} or the {@code FileContext}. In the case where this
 * tag is nested within just a {@code SampleTextContext} it fetches a
 * {@code FullSampleInfo} and determines the {@code SampleHistoryInfo} that
 * corresponds to the addition (or most recent modification) of the given
 * {@code SampleTextInfo}. In the case where this tag is nested within just a
 * {@code FileContext}, it fetches a {@code FullSampleInfo} and determines the
 * {@code SampleHistoryInfo} that corresponds to the addition (or most recent
 * modification) of the given sample data file.
 */
public class SampleHistoryField extends HtmlPageElement
        implements ErrorSupplier {

    /** An enumeration of field codes supported by this tag handler. */
    public static enum FieldCode {
        ACTION_PERFORMED,
        WORKFLOW_ACTION_COMMENTS,
        ACTION_DATE,
        ACTION_DATE_ONLY,
        NEW_STATUS,
        USER_FULLNAME_THAT_PERFORMED_ACTION,
        ACTION_CODE;
    }

    /** The format used to display the date for {@code ACTION_DATE}. */
    private static final SimpleDateFormat fullDateFormat
            = new SimpleDateFormat("dd-MMM-yyyy h:mm a z");

    /**
     * The format used to display the date for {@code ACTION_DATE_ONLY}.
     */
    private static final SimpleDateFormat shortDateFormat
            = new SimpleDateFormat("dd-MMM-yyyy z");
    
    /**
     * An {@code ErrorSupplier} error flag raised if this field cannot find any
     * history and is set to not require any.  (An exception is thrown instead
     * if there is no available history and history is required.)
     */
    public static final int NO_HISTORY_RECORD = 1 << 0;

    /**
     * Indicates which field of the {@code FullSampleInfo} or its
     * {@code SampleHistoryInfo} objects is exposed by this control. This
     * required property is set by its 'setter' method {@code setFieldCode()}.
     */
    private FieldCode fieldCode;
    
    /**
     * An optional attribute indicating whether this tag should require that
     * valid historical information be available to it; default {@code true}.
     * As of the time of this writing, the only recognized appropriate use of
     * this attribute is to accommodate the passible lack of history information
     * for repository files.
     * 
     * @see #noHistoryText
     */
    private boolean requireHistory;

    /**
     * An optional attribute containing text that should be emitted by this
     * tag if no historical information is available to it and the
     * {@code requireHistory} attribute is {@code false}.  Default "".
     * 
     * @see #requireHistory
     */
    private String noHistoryText;
    
    /**
     * A reference to the most immediate {@code SampleHistoryContext} or null if
     * no {@code SampleHistoryContext} is found. Set by
     * {@code onRegistrationPhaseBeforeBody()} and used by
     * {@code onFetchingPhaseBeforeBody()} or
     * {@code onRenderingPhaseAfterBody()} to get a {@code SampleHistoryInfo}
     * object and display one of its fields in accordance with the
     * {@code fieldCode}.
     */
    private SampleHistoryContext sampleHistoryContext;

    /**
     * A reference to the most immediate {@code SampleTextContext}; set by
     * {@code onRegistrationPhaseBeforeBody()} and used to determine the sample
     * and sampleHistoryId that describe the {@code SampleHistoryInfo} for this
     * {@code SampleHistoryField} in the event that {@code sampleHistoryContext}
     * is unavailable or returns a null value.
     */
    private SampleTextContext sampleTextContext;

    /**
     * A reference to the most immediate {@code FileContext}; set by
     * {@code onRegistrationPhaseBeforeBody()} and used to determine the
     * repository file that this tag should display information about, in the
     * event that {@code sampleHistoryContext} and {@code sampleTextContext} are
     * unavailable or return a null value.
     */
    private FileContext fileContext;

    /**
     * An 'owned' control to display the value that corresponds to the
     * {@code fieldCode} for the contextually defined {@code FullSampleInfo} or
     * {@code SampleHistoryInfo}.
     */
    private HtmlControl control;
    
    private int errorCode;

    /** {@inheritDoc} */
    @Override
    protected void reset() {
        super.reset();
        this.fieldCode = null;
        this.requireHistory = true;
        this.noHistoryText = "";
        this.sampleHistoryContext = null;
        this.sampleTextContext = null;
        this.fileContext = null;
        this.control = null;
        this.errorCode = 0;
    }

    /**
     * @return one of the enumerated codes indicating which element of the
     *         {@code SampleHistoryInfo} this {@code SampleHistoryField}
     *         represents.
     */
    public FieldCode getFieldCode() {
        return this.fieldCode;
    }

    /**
     * @param fieldCode one of the enumerated codes indicating which element of
     *        the {@code SampleHistoryInfo} this {@code SampleHistoryField}
     *        represents; should not be {@code null}
     */
    public void setFieldCode(FieldCode fieldCode) {
        if (fieldCode == null) {
            throw new NullPointerException("Null field code");
        } else {
            this.fieldCode = fieldCode;
        }
    }

    /**
     * Retrieves the text fragment to be output if no history information is
     * available and none is required
     * 
     * @return the text fragment as a {@code String}
     */
    public String getNoHistoryText() {
        return noHistoryText;
    }

    /**
     * Sets the text to be output if no history information is
     * available and none is required
     * 
     * @param  noHistoryText the text fragment as a {@code String}; should not
     *         be {@code null}
     */
    public void setNoHistoryText(String noHistoryText) {
        if (noHistoryText == null) {
            throw new NullPointerException("Null HTML fragment");
        } else {
            this.noHistoryText = noHistoryText;
        }
    }

    /**
     * Determines whether this tag requires that appropriate historical
     * information be available
     * 
     * @return {@code true} (the default) if this tag requires historical
     *         information to be available; {@code false} if not 
     */
    public boolean isRequireHistory() {
        return requireHistory;
    }

    /**
     * Sets whether this tag requires that appropriate historical information
     * be available
     * 
     * @param  requireHistory {@code true} (the default) if this tag should
     *         require historical information to be available; {@code false} if
     *         not
     */
    public void setRequireHistory(boolean requireHistory) {
        this.requireHistory = requireHistory;
    }

    /**
     * {@inheritDoc}.  This version gets references to the required contexts and
     * initializes and registers the 'owned' control if the fieldCode requries
     * one.
     * 
     * @throws IllegalStateException if required contexts cannot be found
     */
    @Override
    public int onRegistrationPhaseBeforeBody(PageContext pageContext)
            throws JspException {
        int rc = super.onRegistrationPhaseBeforeBody(pageContext);

        // find the SampleHistoryContext, if any
        this.sampleHistoryContext = super.findRealAncestorWithClass(
                this, SampleHistoryContext.class);

        // find the SampleTextContext, if any
        this.sampleTextContext = super.findRealAncestorWithClass(
                this, SampleTextContext.class);

        // find the FileContext, if any
        this.fileContext = super.findRealAncestorWithClass(
                this, FileContext.class);

        if ((this.sampleHistoryContext == null)
                && (this.sampleTextContext == null)
                && (this.fileContext == null)) {
            throw new IllegalStateException();
        }

        if (this.fieldCode != FieldCode.ACTION_CODE) {
            this.control = new LabelHtmlControl();
            super.registerOwnedElement(this.control);
        }
        
        return rc;
    }

    /**
     * {@inheritDoc}.  This version finds the {@code SampleHistoryInfo} whose
     * fields will be exposed by this control and sets the value of the 'owned'
     * control to reflect the field indicated by {@code fieldCode}.
     * 
     * @throws JspException with a nested {@code RemoteException},
     *         {@code InconsistentDbException},
     *         {@code OperationFailedException},
     *         {@code ResourceNotFoundException} or an {@code IOException} that
     *         was thrown by methods invoked by this method.
     */
    @Override
    public int onFetchingPhaseBeforeBody() throws JspException {
        int rc = super.onFetchingPhaseBeforeBody();

        SampleHistoryInfo shi = selectSampleHistoryInfo();

        if (shi == null) {
            if (requireHistory) {
                throw new JspException("No historical context available");
            } else if (this.control != null) {
                this.control.setValue(getNoHistoryText());
                setErrorFlag(NO_HISTORY_RECORD);
            }
        } else {
            switch (this.fieldCode) {
                case ACTION_PERFORMED:
                    try {
                        /*
                         * the 'owned' control's value is set to the localized
                         * textual representation of the action code performed
                         * for the SampleHistoryInfo
                         */
                        this.control.setValue(LanguageHelper.extract(
                                pageContext.getServletContext()).getActionString(
                                shi.action,
                                pageContext.getRequest().getLocales(), true));
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    } catch (ResourceNotFoundException ex) {
                        throw new JspException(ex);
                    }
                    break;
                case WORKFLOW_ACTION_COMMENTS:
                    this.control.setValue(shi.comments);
                    break;
                case ACTION_DATE:
                    synchronized (fullDateFormat) {
                        this.control.setValue(fullDateFormat.format(shi.date));
                    }
                    break;
                case ACTION_DATE_ONLY:
                    synchronized (shortDateFormat) {
                        this.control.setValue(shortDateFormat.format(shi.date));
                    }
                    break;
                case NEW_STATUS:
                    try {
                        // the 'owned' control's value is set to the localized
                        // textual representation of the status code for the
                        // newStatus field of SampleHistoryInfo
                        this.control.setValue(LanguageHelper.extract(
                                pageContext.getServletContext()).getStatusString(
                                shi.newStatus,
                                pageContext.getRequest().getLocales(), true));
                    } catch (IOException ex) {
                        throw new JspException(ex);
                    } catch (ResourceNotFoundException ex) {
                        throw new JspException(ex);
                    }
                    break;
                case USER_FULLNAME_THAT_PERFORMED_ACTION:
                    if (shi.userId == UserInfo.INVALID_USER_ID) {
                        this.control.setValue("(unknown user)");
                    } else {
                        // Fetch the user's UserInfo from core or the cache.
                        UserInfo user = RequestCache.getUserInfo(
                                super.pageContext.getRequest(), shi.userId);
                        if (user == null) {
                            // cache miss
                            CoreConnector cc = CoreConnector.extract(super.pageContext.getServletContext());

                            try {
                                user = cc.getSiteManager().getUserInfo(
                                        shi.userId);
                            } catch (RemoteException ex) {
                                cc.reportRemoteException(ex);
                                throw new JspException(ex);
                            } catch (OperationFailedException ex) {
                                throw new JspException(ex);
                            }

                            RequestCache.putUserInfo(
                                    super.pageContext.getRequest(), user);
                        }

                        this.control.setValue(user.fullName);
                    }
                    break;
                case ACTION_CODE:
                    // nothing needs to be done because no 'owned' control is
                    // used
                    break;
                default:
                    /*
                     * This should not happen because the above cases cover all
                     * the possible values of the FieldCode enumeration
                     */
                    throw new IllegalStateException();
            }
        }

        return rc;
    }

    /**
     * {@inheritDoc}.  This version outputs the value for those fieldCodes that
     * don't make use of the 'owned' control.
     * 
     * @throws JspException with a nested {@code RemoteException},
     *         {@code InconsistentDbException},
     *         {@code OperationFailedException},
     *         {@code ResourceNotFoundException} or an {@code IOException} that
     *         was thrown by methods invoked by this method.
     */
    @Override
    public int onRenderingPhaseAfterBody(JspWriter out) throws IOException,
            JspException {
        switch (this.fieldCode) {
            case ACTION_PERFORMED:
            case WORKFLOW_ACTION_COMMENTS:
            case ACTION_DATE:
            case ACTION_DATE_ONLY:
            case NEW_STATUS:
            case USER_FULLNAME_THAT_PERFORMED_ACTION:
                // nothing needs to be done
                break;
            case ACTION_CODE:
                SampleHistoryInfo shi = selectSampleHistoryInfo();
                String textToPrint;
                
                if (shi == null) {
                    if (requireHistory) {
                        throw new JspException(
                                "No historical context available");
                    } else  {
                        textToPrint = getNoHistoryText();
                        setErrorFlag(NO_HISTORY_RECORD);
                    }
                } else {
                    textToPrint = String.valueOf(shi.action);
                }
                out.print(textToPrint);
                break;
            default:
                /*
                 * This should not happen because the above cases cover all
                 * the possible values of the FieldCode enumeration
                 */
                throw new IllegalStateException();
        }
        
        return super.onRenderingPhaseAfterBody(out);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlPageElement generateCopy(String newId, Map map) {
        SampleHistoryField dc
                = (SampleHistoryField) super.generateCopy(newId, map);
        
        dc.control = (HtmlControl) map.get(this.control);
        dc.fileContext = (FileContext) map.get(this.fileContext);
        dc.sampleHistoryContext
                = (SampleHistoryContext) map.get(this.sampleHistoryContext);
        dc.sampleTextContext
                = (SampleTextContext) map.get(this.sampleTextContext);
        
        return dc;
    }

    /**
     * <p>
     * Internal helper method that decides which sample history record's
     * information should be displayed. The method determines this by consulting
     * a number of contexts that may enclose this tag. The priority order is:
     * </p>
     * <ol>
     * <li>the nearest {@code SampleHistoryContext}</li>
     * <li>the nearest {@code SampleTextContext}</li>
     * <li>the nearest {@code FileContext}</li>
     * </ol>
     * <p>
     * Note that in the latter two cases, a {@code FullSampleInfo} object is
     * fetched from core or the {@code RequestCache}.
     * </p>
     */
    /*
     * FIXME: consider whether additional contexts should be queried if the
     * first one found provides no history information
     */
    private SampleHistoryInfo selectSampleHistoryInfo() throws JspException {
        // Consult the SampleHistoryContext, if any, and possibly return early.
        if (this.sampleHistoryContext != null) {
            SampleHistoryInfo shi
                    = this.sampleHistoryContext.getSampleHistoryInfo();
            
            if (shi != null) {
                return shi;
            }
        }

        // Consult the SampleTextContext, if any.
        int sampleId = SampleInfo.INVALID_SAMPLE_ID;
        int sampleHistoryId = SampleHistoryInfo.INVALID_SAMPLE_HISTORY_ID;
        if (this.sampleTextContext != null) {
            SampleTextInfo sti = this.sampleTextContext.getSampleTextInfo();
            
            if (sti != null) {
                sampleId = sti.sampleId;
                sampleHistoryId = sti.originalSampleHistoryId;
            }
        }

        // Possibly consult the FileContext, if any.
        if ((sampleId == SampleInfo.INVALID_SAMPLE_ID)
                && (this.fileContext != null)) {
            SampleDataFile sdf = this.fileContext.getSampleDataFile();
            if (sdf != null) {
                sampleId = sdf.getSampleId();
                sampleHistoryId = sdf.getOriginalSampleHistoryId();
            }
        }

        if (sampleId == SampleInfo.INVALID_SAMPLE_ID) {
            if (requireHistory) {
                // Abort if we still can't figure out what to display.
                throw new JspException(new IllegalStateException());
            } else {
                // No history information is available
                return null;
            }
        }

        // Fetch a FullSampleInfo object from core or the cache.
        FullSampleInfo fsi = RequestCache.getFullSampleInfo(
                    super.pageContext.getRequest(), sampleId);
        
        if (fsi == null) {
            // cache miss
            CoreConnector cc = CoreConnector.extract(
                    super.pageContext.getServletContext());
            
            try {
                fsi = cc.getSampleManager().getFullSampleInfo(sampleId);
            } catch (RemoteException ex) {
                cc.reportRemoteException(ex);
                throw new JspException(ex);
            } catch (ResourceNotFoundException ex) {
                throw new JspException(ex);
            } catch (OperationFailedException ex) {
                throw new JspException(ex);
            } catch (InconsistentDbException ex) {
                throw new JspException(ex);
            }
            
            RequestCache.putFullSampleInfo(super.pageContext.getRequest(), fsi);
        }

        /*
         * Find the desired history record within the FullSampleInfo and
         * return it.
         */
        for (SampleHistoryInfo historyInfo : fsi.history) {
            if (historyInfo.id == sampleHistoryId) {
                return historyInfo;
            }
        }

        // No matching history record was found
        if (requireHistory) {
            /*
             * We should never get here because some context gave us a
             * sampleHistoryId that should always be legitimate, and this
             * implies that there must be a SampleHistoryInfo object within the
             * FullSampleInfo object for that historyId. If this is not the
             * case, then either database is corrupt or the context lied to us.
             * In either case we cannot proceed.
             */
            throw new JspException(new InconsistentDbException());
        } else {
            return null;
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * @return the logical OR of all errors codes that correspond to errors
     *         encountered during the parsing of this control's value.
     *
     * @see ErrorSupplier#getErrorCode()
     */
    public int getErrorCode() {
        return this.errorCode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see ErrorSupplier#setErrorFlag(int)
     */
    public void setErrorFlag(int errorFlag) {
        this.errorCode |= errorFlag;
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
        return NO_HISTORY_RECORD;
    }
}
