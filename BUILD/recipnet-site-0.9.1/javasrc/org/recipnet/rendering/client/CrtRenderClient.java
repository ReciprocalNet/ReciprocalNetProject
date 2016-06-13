/*
 * Reciprocal Net project
 * rendering software
 * 
 * CrtRenderClient.java
 *
 * 17-Aug-2004: eisiorho wrote first draft
 * 06-Jun-2006: jobolllin reformatted the source
 */

package org.recipnet.rendering.client;

import java.io.IOException;
import java.net.URL;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.recipnet.rendering.servlet.CrtRenderRequest;
import org.recipnet.rendering.servlet.CrtRenderResponse;

/**
 * This class is used for negotationing rendering jobs of .crt files between the
 * client and the servlet.
 */

public abstract class CrtRenderClient {

    /**
     * This method readies the .crt job request into a SOAPMessage object, and
     * sends the message to a serlvet to complete the render request. The
     * parameters managed by this method are used either to determine how the
     * requested job will be rendered or used for authentication.
     * 
     * @param imageSizeX contains the width of the requested image
     * @param imageSizeY contains the length of the requestd image
     * @param jpegQuality contains the quality of the requested image if
     *        <code>requestedImageFormat</code> is set to
     *        <i>CrtRenderRequest.REQUEST_FOR_JPG</i>
     * @param bgColorRed contains the amount of red hues the render image
     *        request should have in the background.
     * @param bgColorBlue contains the amount of blue hues the render image
     *        request should have in the background.
     * @param bgColorGreen contains the amount of green hues the render image
     *        request should have in the background.
     * @param drawingType contains the drawing mode of requested image
     * @param includeHydrogens contains whether or not the requested image will
     *        include the hydrogen atom in rendering
     * @param scale contains the scaling factor of the requested image
     * @param xRotation contains angle of rotation of the model about the
     *        coordinate x-axis for requested image
     * @param yRotation contains angle of rotation of the model about the
     *        coordinate y-axis for requested image
     * @param zRotation contains angle of rotation of the model about the
     *        coordinate z-axis for requested image
     * @param ballSize contains the size of the atoms to be rendered
     * @param rodSize contains the size of the connections between atoms to be
     *        rendered.
     * @param distance contains the distance of the molecule's center from the
     *        camera's view.
     * @param requestedImageFormat contains the
     * @param requestedPriority
     * @param username contains the user's alias on the rendering server.
     * @param password contains the user's password on the rendering server.
     * @param crtFileData contains the file that will be used to render an
     *        image.
     * @param serverUrl the url path to the servlet that will perform the
     *        rendering job the client is requesting.
     * @return the rendered image as an byte array.
     * @throws SOAPException if an error occurs while handling SOAP elements
     * @throws IOException
     */
    public static byte[] doRender(int imageSizeX, int imageSizeY,
            int jpegQuality, int bgColorRed, int bgColorBlue, int bgColorGreen,
            int drawingType, boolean includeHydrogens, float scale,
            float xRotation, float yRotation, float zRotation, float ballSize,
            float rodSize, float distance, int requestedImageFormat,
            int requestedPriority, String username, String password,
            byte[] crtFileData, String serverUrl) throws SOAPException,
            IOException {

        CrtRenderRequest crtRequest = new CrtRenderRequest();
        crtRequest.imageSizeX = imageSizeX;
        crtRequest.imageSizeY = imageSizeY;
        crtRequest.jpegQuality = jpegQuality;
        crtRequest.bgColorRed = bgColorRed;
        crtRequest.bgColorBlue = bgColorBlue;
        crtRequest.bgColorGreen = bgColorGreen;
        crtRequest.drawingType = drawingType;
        crtRequest.includeHydrogens = includeHydrogens;
        crtRequest.scale = scale;
        crtRequest.xRotation = xRotation;
        crtRequest.yRotation = yRotation;
        crtRequest.zRotation = zRotation;
        crtRequest.ballSize = ballSize;
        crtRequest.rodSize = rodSize;
        crtRequest.distance = distance;
        crtRequest.requestedImageFormat = requestedImageFormat;
        crtRequest.requestedPriority = requestedPriority;
        crtRequest.username = username;
        crtRequest.password = password;
        crtRequest.crtFileData = crtFileData;
        SOAPMessage reply = null;

        // Next, establish an HTTP connection with the specified
        // destination URL and POST some data to it.
        URL url = new URL(serverUrl);

        SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
        SOAPConnection con = scf.createConnection();
        reply = con.call(crtRequest.encode(), url);

        CrtRenderResponse response = new CrtRenderResponse();
        response.decode(reply);
        
        return response.pictureFileData;
    }
}
