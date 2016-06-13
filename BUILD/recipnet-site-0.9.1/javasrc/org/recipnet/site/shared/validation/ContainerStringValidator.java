/*
 * Reciprocal Net project
 * 
 * ContainerStringValidator.java
 * 
 * 07-May-2004: cwestnea wrote first draft
 * 11-Jan-2005: jobollin moved Validator to org.recipnet.common
 * 12-Jan-2005: ekoperda clarified JavaDoc on isValid()
 * 24-May-2006: jobollin reformatted the source
 * 03-Jan-2008: ekoperda fixed bug #1848 by rearranging validity rules
 */

package org.recipnet.site.shared.validation;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.recipnet.common.Validator;

/**
 * Implements {@code Validator}; guards against invalid characters. This 
 * implementation ensures that characters in a given string are valid in the 
 * <a href="http://www.w3.org/TR/2004/REC-xml-20040204">XML specification</a>.
 */
public class ContainerStringValidator implements Validator {
    /**
     * These bit sets embody the Unicode rules about which characters are valid
     * and which are not.  A character is valid if it matches any one of the
     * three rules below.
     *
     * Each "rule" object below contains one bit for every possible character
     * code: true if the corresponding code is valid and false if it is not.
     *
     * Some of the rules below have two parts labeled "a" and "b".  These
     * composite rules are designed to evaluate pairs of characters.  The "a"
     * rule tests the first character of a pair and the "b" rule tests the
     * second character of the same pair.
     */
    private static ExtendedBitSet rule1
  	    = new ExtendedBitSet(Character.MAX_VALUE);
    private static ExtendedBitSet rule2a
  	    = new ExtendedBitSet(Character.MAX_VALUE);
    private static ExtendedBitSet rule2b
  	    = new ExtendedBitSet(Character.MAX_VALUE);
    private static ExtendedBitSet rule3a
  	    = new ExtendedBitSet(Character.MAX_VALUE);
    private static ExtendedBitSet rule3b
  	    = new ExtendedBitSet(Character.MAX_VALUE);

    /**
     * The static block below initializes the rule objects to match those
     * characters allowed by the XML specification, not including the
     * compatibility characters.  The rules are expressed in this fashion in
     * order to match the structure of the specification and also for
     * historical reasons.
     *
     * Characters that have 32 bit codes are represented in their UTF-16 form,
     * which is a two-byte representation.  The first byte must be in the range
     * xD800-xDBFF and the second must be in the range xDC00-xDFFF.
     * Compatibility characters in these supplementary characters always have
     * the second byte in the range xDFFE-xDFFF, and the first byte of the form
     * xDnmF, where n is between 8 and B, and m is either 3, 7, B, or F. Empty
     * strings are not valid.  This is implemented by breaking up the
     * characters into three groups. The first is the 16 bit characters with
     * the compatability characters removed. The second is a group of two
     * characters which represent all supplementary characters that aren't
     * potentially compatability characters. The third is a group of two
     * characters which include the remaining non-compatibility supplementary
     * characters, but which exclude the compatability characters.
     */
    static {
	rule1.set('\t');
	rule1.set('\n');
	rule1.set('\r');
	rule1.setInclusive(0x0020, 0xD7FF);
	rule1.setInclusive(0xE000, 0xFFFD);
	rule1.clearInclusive(0x007F, 0x0084);
	rule1.clearInclusive(0x0086, 0x009F);
	rule1.clearInclusive(0xFDD0, 0xFDDF);

	rule2a.setInclusive(0xD800, 0xDBFF);

	rule2b.setInclusive(0xDC00, 0xDFFD);

	rule3a.setInclusive(0xD800, 0xDBFF);
	rule3a.clear(0xD83F);
	rule3a.clear(0xD87F);
	rule3a.clear(0xD8BF);
	rule3a.clear(0xD8FF);
	rule3a.clear(0xD93F);
	rule3a.clear(0xD97F);
	rule3a.clear(0xD9BF);
	rule3a.clear(0xD9FF);
	rule3a.clear(0xDA3F);
	rule3a.clear(0xDA7F);
	rule3a.clear(0xDABF);
	rule3a.clear(0xDAFF);
	rule3a.clear(0xDB3F);
	rule3a.clear(0xDB7F);
	rule3a.clear(0xDBBF);
	rule3a.clear(0xDBFF);

	rule3b.setInclusive(0xDFFE, 0xDFFF);
    }

    /**
     * In order to be valid, the object must be a {@code String} and match
     * the validating pattern. 
     *
     * @param obj the {@code Object} to test for validity
     * @return true if the object is a valid container string, false if it
     *     contains characters not specified in the pattern.
     * @throws NullPointerException if obj is null
     */
    public boolean isValid(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
	if (!(obj instanceof String)) {
	    return false;
	}
	String str = (String) obj;
	
	// Iterate through the characters of the string.  A character is valid
	// if it matches any of the three rules.
	for (int i = 0; i < str.length(); i ++) {
	    char c = str.charAt(i);
	    if (rule1.get(c)) {
		continue;
	    }
	    if (rule2a.get(c) && str.length() > i + 1) {
		if (rule2b.get(str.charAt(i + 1))) {
		    i ++;
		    continue;
		}
	    }
	    if (rule3a.get(c) && str.length() > i + 1) {
		if (rule3b.get(str.charAt(i + 1))) {
		    i ++;
		    continue;
		}
	    }

	    // No rules matched.  Apparently this character is invalid.
	    return false;
	}

	// If control reaches here, we know all characters were valid.
	return true;
    }

    /**
     * A quick extension of the BitSet class that adds two useful methods.
     */
    private static class ExtendedBitSet extends BitSet {
        public ExtendedBitSet() {
	    super();
	}

	public ExtendedBitSet(int nbits) {
	    super(nbits);
	}

	public void setInclusive(int fromIndex, int toIndex) {
	    super.set(fromIndex, toIndex);
	    super.set(toIndex);
	}

	public void clearInclusive(int fromIndex, int toIndex) {
	    super.clear(fromIndex, toIndex);
	    super.clear(toIndex);
	}
    }
}
