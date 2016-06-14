/*
 * Reciprocal Net project
 * rendering software
 * 
 * CrtRenderResponse.java
 *
 * 17-Aug-2004: eisiorho wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.servlet;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 * RenderResponse takes a finished rendered image from a crt job request and
 * stores the image in a SOAPMessage.
 */

public class CrtRenderResponse {

    /**
     * Holds the requested rendered image
     */
    public byte[] pictureFileData;

    /**
     * Contains the user who sent the render request
     */
    public String username;

    /**
     * How long it took to render the requested image.
     */
    public double renderTime;

    /**
     * Contains the type of image rendered.
     */
    public int imageType;

    /**
     * Default constructor
     */
    public CrtRenderResponse() {
        this.pictureFileData = null;
        this.username = null;
        this.renderTime = 0;
        this.imageType = 0;
    }

    /**
     * Encodes the <i>pictureFileData</i> into a SOAPMessage to be sent back to
     * the client.
     * 
     * @return a SOAPMessage with the rendered picture as an attachment
     */
    public SOAPMessage encode() throws SOAPException {
        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage message = mf.createMessage();
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        SOAPBody body = envelope.getBody();
        addItem(body, "username", envelope, username);
        addItem(body, "renderTime", envelope, String.valueOf(renderTime));
        addItem(body, "imageType", envelope, String.valueOf(imageType));
        AttachmentPart ap = message.createAttachmentPart(pictureFileData,
                "application/binary");

        message.addAttachmentPart(ap);

        message.saveChanges();
        return message;
    }

    /**
     * Adds elements and values into the SOAPMessage.
     * 
     * @param body the {@code SOAPBody} of the message
     * @param elementName the name of the element to add, as a {@code String}
     * @param envelope the {@code SOAPEnvelope} of the message
     * @param value the value of the element to add, as a {@code String}
     */
    private void addItem(SOAPBody body, String elementName,
            SOAPEnvelope envelope, String value) throws SOAPException {
        Name element = envelope.createName(elementName);
        Name attributeName = envelope.createName("value");
        SOAPBodyElement bodyElement = body.addBodyElement(element);
        bodyElement.addAttribute(attributeName, value);
    }

    /**
     * Decodes a SOAPMessage that contains the requested rendered picture data.
     * Stores the value into the field byte array <i>pictureFileData</i>.
     * 
     * @param message contains a {@code SOAPMessage} with a rendered image as an
     *        attachment.
     */
    public void decode(SOAPMessage message) throws SOAPException {
        SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
        AttachmentPart ap = (AttachmentPart) message.getAttachments().next();
        
        this.username = retrieve(envelope, "username");
        this.renderTime = Double.parseDouble(retrieve(envelope, "renderTime"));
        this.imageType = Integer.parseInt(retrieve(envelope, "imageType"));
        try {
            this.pictureFileData = ap.getContent().toString().getBytes();
        } catch (SOAPException soape) {
            soape.printStackTrace();
        }
    }

    /**
     * Retrieves elements from the requested SOAPMessage.
     * 
     * @return a {@code String} object representing the decoded value.
     */
    public String retrieve(SOAPEnvelope envelope, String valueDesired)
            throws SOAPException {
        SOAPBody b = envelope.getBody();
        SOAPElement element = (SOAPElement) b.getChildElements(
                envelope.createName(valueDesired)).next();
        
        return element.getAttributeValue(envelope.createName("value"));
    }
}
