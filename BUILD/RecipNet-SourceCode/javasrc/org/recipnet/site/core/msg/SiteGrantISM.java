/*
 * Reciprocal Net project
 * 
 * SiteGrantISM.java
 *
 * 11-Jul-2002: ekoperda wrote first draft
 * 27-Sep-2002: ekoperda added support for serialization to/from XML and also
 *              moved the class into the core.msg package, from the container
 *              package
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 30-May-2006: jobollin reformatted the source
 * 10-Jan-2008: ekoperda altered extractFromDom() to match changes in DomUtil
 */

package org.recipnet.site.core.msg;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import org.recipnet.site.shared.DomUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;

/**
 * <p>
 * Inter-site message that is sent from the Reciprocal Net coordinator to a new
 * site when it is first created. It grants the new site "permission to operate"
 * and specifies the site id and private encryption key that are to be used. The
 * new site normally keeps this message handy for as long as it's active on
 * Reciprocal Net - the site id and encryption key need to be loaded every time
 * the server starts up.
 * </p><p>
 * This message normally is sent to a new site by the coordinator in conjunction
 * with an SiteActivationISM announcement to all sites. The SiteGrantISM must
 * have the higher sequence number (be transmitted later) then the
 * SiteActivationISM announcement, however.
 * </p><p>
 * For security reasons, messages of this type should not be transmitted from
 * the Coordinator to the new site via Reciprocal Net's usual network-based
 * message-passing scheme. Instead, the SiteGrantISM normally is included as the
 * last message in the site's startup.msgpak file, which is sent from the
 * Reiprocal Net coordinator to the new site's sysadmin by some out-of-band
 * means like e-mail.
 * </p>
 */
public class SiteGrantISM extends InterSiteMessage {

    /*
     * The new site's id is implicit in the destSiteId field in the parent
     * InterSiteMessage class. A site that merely receives a message of this
     * type may assume that it has been assigned the site id specified within.
     */

    /** private encryption the new site should use for signing future msgs */
    public PrivateKey privateKey;

    /** Default empty constructor */
    public SiteGrantISM() {
        super();
        privateKey = null;
    }

    /**
     * Creates a message from the Reciprocal Net coordinator to the specified
     * brand-new site. This constructor is useful only to the Reciprocal Net
     * Coordinator because it the only entity that may send this message.
     */
    public SiteGrantISM(int newSiteId, PrivateKey newSitesPrivateKey) {
        this.sourceSiteId = InterSiteMessage.RECIPROCAL_NET_COORDINATOR;
        this.sourceDate = new Date();

        this.destSiteId = newSiteId;
        this.privateKey = newSitesPrivateKey;
        this.deliverToSiteManager = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node insertIntoDom(Document doc, Node base) {
        Node realParent = super.insertIntoDom(doc, base);

        Element keyEl = DomUtil.createTextElWithBinaryData(realParent,
                "privateKey", this.privateKey.getEncoded());
        DomUtil.addAttrToEl(keyEl, "algorithm", this.privateKey.getAlgorithm());
        DomUtil.addAttrToEl(keyEl, "format", this.privateKey.getFormat());

        return realParent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("deprecation")
    public Node extractFromDom(Document doc, Node base) throws SAXException {
        Element realParent = (Element) super.extractFromDom(doc, base);

        Element keyEl = DomUtil.findSingleElement(realParent, "privateKey",
                true);
        String keyAlgorithm = DomUtil.getAttrForEl(keyEl, "algorithm", true);
        if (!keyAlgorithm.equals("DSA")) {
            throw new SAXNotSupportedException("Site key algorithm '"
                    + keyAlgorithm + "' not supported");
        }
        String keyFormat = DomUtil.getAttrForEl(keyEl, "format", true);
        if (!keyFormat.equals("PKCS#8")) {
            throw new SAXNotSupportedException("Site key format '" + keyFormat
                    + "' not supported");
        }
        byte[] rawKey = DomUtil.getTextForElAsBytes(keyEl);
        try {
            PKCS8EncodedKeySpec specKey = new PKCS8EncodedKeySpec(rawKey);
            KeyFactory factory = KeyFactory.getInstance("DSA");
            this.privateKey = factory.generatePrivate(specKey);
        } catch (NoSuchAlgorithmException ex) {
            throw new SAXException("Site key algorithm not available locally",
                    ex);
        } catch (InvalidKeySpecException ex) {
            throw new SAXException("Invalid site key", ex);
        }

        return realParent;
    }
}
