/*
 * Reciprocal Net project
 * rendering software
 * 
 * CrtRenderRequest.java
 *
 * 19-Aug-2004: eisiorho and ekoperda wrote first draft
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.recipnet.common.Base64InputStream;
import org.recipnet.common.Base64OutputStream;
import org.recipnet.rendering.dispatcher.ArtJob;

/**
 * This class takes its fields, which are then used to construct a SOAPMessage.
 * It also deconstructs a SOAPMessage to populate this class fields from the
 * SOAPMessage body.
 */
public class CrtRenderRequest {
    /**
     * imageSizeX and imageSizeY determine the height and width, respectively,
     * of the returned render image. The acceptable range of values are higher
     * than 0. The default values are 300.
     */
    public int imageSizeX = 300;

    public int imageSizeY = 300;

    /**
     * Determines the quality of the requested picture if
     * <code>requestedImageFormat</code> is set to <i>REQUEST_FOR_JPG</i>
     * format. Default value is 75% quality. Valid range is 0% to 100%. Ignored
     * if <b>requestedImageFormat</b> is not <i>REQUEST_FOR_JPG</i>
     */
    public int jpegQuality = 75;

    /**
     * bgColorRed determines the amount of red hues the render request should
     * have in the background. The default value is 255. Valid range is 0-255.
     */
    public int bgColorRed = 255;

    /**
     * bgColorBlue determines the amount of blue hues the render request should
     * have in the background. The default value is 255. Valid range is 0-255.
     */
    public int bgColorBlue = 255;

    /**
     * bgColorGreen determines the amount of green hues the render request
     * should have in the background. The default value is 255. Valid range is
     * 0-255.
     */
    public int bgColorGreen = 255;

    /**
     * Determines whether the rendered picture is drawn in <i>BALL_STICK_MODE</i>,
     * <i>SPACE_FILLED_MODE</i>, or <i>LINE_DRAWING_MODE</i> mode.
     */
    public int drawingType;

    public static final int BALL_STICK_MODE = 0;

    public static final int SPACE_FILLED_MODE = 1;

    public static final int LINE_DRAWING_MODE = 2;

    /**
     * Determines whether the 3-D model generated should include hydrogen atoms
     * in the picture or not.
     */
    public boolean includeHydrogens = false;

    /** Scaling factor used in generating the 3-D model. The default is 1. */
    public float scale = 1;

    /**
     * xRotation determines the viewing orientation of the model that is
     * requested for rendering. The default is 0. Valid range is -180 to 180.
     */
    public float xRotation = 0;

    /**
     * yRotation determines the viewing orientation of the model that is
     * requested for rendering. The default is 0. Valid range is -90 to 90.
     */
    public float yRotation = 0;

    /**
     * zRotation determines the viewing orientation of the model that is
     * requested for rendering. The default is 0. Valid range is -180 to 180.
     */
    public float zRotation = 0;

    /**
     * ballSize determines the radius of spheres in the rendered picture. The
     * default is 1.0.
     */
    public float ballSize = 1;

    /**
     * roadSize determines the radius of cylinders in the rendered picture. The
     * default is 1.
     */
    public float rodSize = 1;

    /**
     * The requested camera to molecular center distance, from distance, default
     * 50.
     */
    public float distance = 50;

    /**
     * File format the client wants the image file to be returned as. Possible
     * values are: <i>REQUEST_FOR_PIX</I>, <i>REQUEST_FOR_PPM</i>,
     * <i>REQUEST_FOR_PCX</i>, and <i>REQUEST_FOR_JPG</i>.
     */
    public int requestedImageFormat;

    public static final int REQUEST_FOR_PIX = ArtJob.RENDER_TO_PIX;

    public static final int REQUEST_FOR_PPM = ArtJob.RENDER_TO_PPM;

    public static final int REQUEST_FOR_PCX = ArtJob.RENDER_TO_PCX;

    public static final int REQUEST_FOR_JPG = ArtJob.RENDER_TO_JPG;

    /**
     * The suggested priority for the requested job, with higher-priority jobs
     * more likely to be executed sooner. Valid range is 1-1024.
     */
    public int requestedPriority = 0;

    /** The client's name that is requesting the .crt render job */
    public String username;

    /** The client's password for the user requesting the .crt render job */
    public String password;

    /** Contains the file information of the .crt that needs rendering */
    public byte[] crtFileData;

    /**
     * This encodes <b>this</b> class fields into a SOAPMessage.
     * 
     * @return a SOAPMessage
     */
    public SOAPMessage encode() throws SOAPException, IOException {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage message = mf.createMessage();
        SOAPPart sp = message.getSOAPPart();
        sp.removeAllMimeHeaders();
        SOAPEnvelope envelope = sp.getEnvelope();
        SOAPBody body = envelope.getBody();
        addItem(body, "username", envelope, username);
        addItem(body, "password", envelope, password);
        addItem(body, "requestedPriority", envelope,
                String.valueOf(requestedPriority));
        addItem(body, "imageSizeX", envelope, String.valueOf(imageSizeX));
        addItem(body, "imageSizeY", envelope, String.valueOf(imageSizeY));
        addItem(body, "jpegQuality", envelope, String.valueOf(jpegQuality));
        addItem(body, "bgColorRed", envelope, String.valueOf(bgColorRed));
        addItem(body, "bgColorBlue", envelope, String.valueOf(bgColorBlue));
        addItem(body, "bgColorGreen", envelope, String.valueOf(bgColorGreen));
        addItem(body, "drawingType", envelope, String.valueOf(drawingType));
        addItem(body, "includeHydrogens", envelope,
                String.valueOf(includeHydrogens));
        addItem(body, "scale", envelope, String.valueOf(scale));
        addItem(body, "xRotation", envelope, String.valueOf(xRotation));
        addItem(body, "yRotation", envelope, String.valueOf(yRotation));
        addItem(body, "zRotation", envelope, String.valueOf(zRotation));
        addItem(body, "ballSize", envelope, String.valueOf(ballSize));
        addItem(body, "rodSize", envelope, String.valueOf(rodSize));
        addItem(body, "distance", envelope, String.valueOf(distance));
        addItem(body, "requestedImageFormat", envelope,
                String.valueOf(requestedImageFormat));

        StringWriter sw = new StringWriter();
        Base64OutputStream b64out = new Base64OutputStream(sw, false);
        b64out.write(crtFileData, 0, crtFileData.length);
        b64out.close();

        AttachmentPart ap = message.createAttachmentPart(sw.toString(),
                "text/plain");
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
     * Decodes the received SOAPMessage and populates the fields present in
     * <b>this</b> object.
     */
    public void decode(String message) throws SOAPException {
        try {
            String[] elements = message.split("<*>");
            String[] pairs = new String[2];
            for (int i = 0; i < elements.length; i++) {
                if (-1 != elements[i].indexOf("value=")) {
                    pairs = elements[i].split("value=");
                    if (pairs[0].matches("\\S*password\\s*")) {
                        this.password = stripQuotes(pairs[1]);
                    } else if (pairs[0].matches("\\S*username\\s*")) {
                        this.username = stripQuotes(pairs[1]);
                    } else if (pairs[0].matches("\\S*imageSizeX\\s*")) {
                        this.imageSizeX = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*imageSizeY\\s*")) {
                        this.imageSizeY = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*jpegQuality\\s*")) {
                        this.jpegQuality = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*bgColorRed\\s*")) {
                        this.bgColorRed = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*bgColorBlue\\s*")) {
                        this.bgColorBlue = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*bgColorGreen\\s*")) {
                        this.bgColorGreen = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*drawingType\\s*")) {
                        this.drawingType = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*includeHydrogens\\s*")) {
                        this.includeHydrogens = new Boolean(
                                stripQuotes(pairs[1])).booleanValue();
                    } else if (pairs[0].matches("\\S*scale\\s*")) {
                        this.scale = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*xRotation\\s*")) {
                        this.xRotation = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*yRotation\\s*")) {
                        this.yRotation = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*zRotation\\s*")) {
                        this.zRotation = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*ballSize\\s*")) {
                        this.ballSize = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*rodSize\\s*")) {
                        this.rodSize = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*distance\\s*")) {
                        this.distance = Float.parseFloat(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*requestedImageFormat\\s*")) {
                        this.requestedImageFormat
                                = Integer.parseInt(stripQuotes(pairs[1]));
                    } else if (pairs[0].matches("\\S*requestedPriority\\s*")) {
                        this.requestedPriority
                                = Integer.parseInt(stripQuotes(pairs[1]));
                    }
                } else {
                    if (-1 != elements[i].indexOf("Content")) {
                        pairs = elements[i].split("Content-Type: text/plain");
                        pairs = pairs[1].split("--*");
                        Base64InputStream b64in = new Base64InputStream(
                                new StringReader(pairs[0]));
                        this.crtFileData = readEntireInputStream(b64in);
                    } else {
                        throw new Exception();
                    }
                }
            }
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe.getMessage());
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * Semi-temp method to strip Quotes from decoding 'String' SOAP objects.
     */
    public String stripQuotes(String string) {
        String[] tmp;
        tmp = string.split("\"/");
        tmp = tmp[0].split("\"");
        return tmp[1];
    }

    /**
     * Decodes the received SOAPMessage and populates the fields present in
     * <b>this</b> object.
     */
    public void decode(SOAPMessage message) throws SOAPException, IOException {

        SOAPPart sp = message.getSOAPPart();
        SOAPEnvelope envelope = null;
        envelope = sp.getEnvelope();
        this.username = retrieve(envelope, "username");
        this.password = retrieve(envelope, "password");
        this.imageSizeX = Integer.parseInt(retrieve(envelope, "imageSizeX"));
        this.imageSizeY = Integer.parseInt(retrieve(envelope, "imageSizeY"));
        this.jpegQuality = Integer.parseInt(retrieve(envelope, "jpegQuality"));
        this.bgColorRed = Integer.parseInt(retrieve(envelope, "bgColorRed"));
        this.bgColorBlue = Integer.parseInt(retrieve(envelope, "bgColorBlue"));
        this.bgColorGreen = Integer.parseInt(retrieve(envelope, "bgColorGreen"));
        this.drawingType = Integer.parseInt(retrieve(envelope, "drawingType"));
        Boolean bool = new Boolean(retrieve(envelope, "includeHydrogens"));
        this.includeHydrogens = bool.booleanValue();
        this.scale = Float.parseFloat(retrieve(envelope, "scale"));
        this.xRotation = Float.parseFloat(retrieve(envelope, "xRotation"));
        this.yRotation = Float.parseFloat(retrieve(envelope, "yRotation"));
        this.zRotation = Float.parseFloat(retrieve(envelope, "zRotation"));
        this.ballSize = Float.parseFloat(retrieve(envelope, "ballSize"));
        this.rodSize = Float.parseFloat(retrieve(envelope, "rodSize"));
        this.distance = Float.parseFloat(retrieve(envelope, "distance"));
        this.requestedImageFormat = Integer.parseInt(retrieve(envelope,
                "requestedImageFormat"));
        this.requestedPriority = Integer.parseInt(retrieve(envelope,
                "requestedPriority"));
        AttachmentPart ap = (AttachmentPart) message.getAttachments().next();
        Base64InputStream b64in = new Base64InputStream(
                new StringReader(ap.getContent().toString()));
        this.crtFileData = readEntireInputStream(b64in);
    }

    /**
     * Retrieves elements from the requested SOAPMessage.
     */
    private String retrieve(SOAPEnvelope envelope, String valueDesired)
            throws SOAPException {
        SOAPBody b = envelope.getBody();
        SOAPElement element = (SOAPElement) b.getChildElements(
                envelope.createName(valueDesired)).next();
        
        return element.getAttributeValue(envelope.createName("value"));
    }

    private byte[] readEntireInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[16384];
        int bytesRead;
        
        do {
            bytesRead = is.read(buf, 0, 16384);
            if (bytesRead > 0) {
                baos.write(buf, 0, bytesRead);
            }
        } while (bytesRead != -1);
        
        return baos.toByteArray();
    }
}
