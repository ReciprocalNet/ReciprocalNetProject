/*
 * Reciprocal Net project
 * 
 * NameAndFormulaIterator.java
 *
 * 12-Apr-2005: midurbin wrote first draft
 * 13-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.site.content.rncontrols;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.jsp.JspException;

import org.recipnet.site.shared.bl.SampleTextBL;
import org.recipnet.site.shared.db.SampleAttributeInfo;
import org.recipnet.site.shared.db.SampleInfo;
import org.recipnet.site.shared.db.SampleTextInfo;

/**
 * A custom tag that evaluates its body between 0 and 2 times.  This tag will
 * supply the sample's preferred name (if one exists) followed by the sample's
 * empirical formula (if one exists).
 * 
 * This tag should not expose the superclass' sorting properties as the sort
 * order is dictated by {@code getFilteredTextInfoCollection()}.
 */
public class NameAndFormulaIterator extends AbstractSampleTextIterator {

    @Override
    public Collection<SampleTextInfo> getFilteredTextInfoCollection(
            SampleInfo sampleInfo) throws JspException {
        Collection<SampleTextInfo> textInfos = new ArrayList<SampleTextInfo>();
        
        if (sampleInfo == null) {
            // no SampleInfo was provided, therefore no SampleTextInfos will
            // be provided and this iterator will not evaluate its body.
            return textInfos;
        }

        // add the preferred name first if it exists
        for (SampleTextInfo firstName
                 : SampleTextBL.getSampleNames(sampleInfo, true)) {
            textInfos.add(firstName);
            break;
        }

        // add the empirical formula if it exists
        for (SampleAttributeInfo attr : sampleInfo.attributeInfo) {
            if (attr.type == SampleTextBL.EMPIRICAL_FORMULA) {
                textInfos.add(attr);
            }
        }

        return textInfos;
    }
}
