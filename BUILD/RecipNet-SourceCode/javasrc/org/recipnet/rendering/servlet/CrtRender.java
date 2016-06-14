/*
 * Reciprocal Net project
 * centralized rendering software
 * 
 * CrtRender.java
 *
 * 05-Dec-2002: ekoperda wrote first draft
 * 07-Jan-2004: ekoperda changed package references to match source tree
 *              reorganization
 * 13-Feb-2004: jobollin changed the package statement to match the source
 *              tree reorganization
 * 06-Jun-2006: jobollin reformatted the source
 */

package org.recipnet.rendering.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrtRender extends HttpServlet {
    public static final int DRAWINGMODE_LINE = 1;
    public static final int DRAWINGMODE_BALLSTICK = 2;
    public static final int DRAWINGMODE_SPACEFILLED = 3;

    private int maxImageSize;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
	
	maxImageSize = Integer.parseInt(
                config.getServletContext().getInitParameter("maxImageSize"));
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException, ServletException {
        /* Dependecies on SoapUtil and DomUtil are too difficult to resolve
           for now; simpler to just comment this code out.

	try {
	    // Verify that we received XML data in the post
	    String contentType = req.getHeader("Content-Type");
	    if (contentType != null && !contentType.equals("text/xml")) {
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, 
		        "Expected MIME Content-Type:text/xml but got '"
			+ contentType + "'");
		return;
	    }
	    
	    // Read the posted data into a string
	    String docAsXml = 
                    SoapUtil.readEntireInputStream(req.getInputStream());

	    // Parse the XML document into a DOM tree, then validate the SOAP
	    // headers contained within it.  Extract values for all the
	    // parameters that affect rendering.
	    Document doc = DomUtil.xmlToDomTree(docAsXml);
	    Element baseEl = SoapUtil.decodeSoapDocument(doc, "CrtRender", 
  	            "recipnet", "http://www.reciprocalnet.org/master/");
	    String infoForLog = 
                    DomUtil.getTextForEl(baseEl, "infoForLog", true);
	    int imageSize = 
		    DomUtil.getTextForElAsInt(baseEl, "imageSize", 100);
	    int jpegQuality = 
                    DomUtil.getTextForElAsInt(baseEl, "jpeqQuality", 75);
	    String drawingModeAsString = 
                    DomUtil.getTextForEl(baseEl, "drawingMode", true);
	    int drawingMode;
	    if (drawingModeAsString.equals("line")) {
		drawingMode = DRAWINGMODE_LINE;
	    } else if (drawingModeAsString.equals("ballstick")) {
		drawingMode = DRAWINGMODE_BALLSTICK;
	    } else if (drawingModeAsString.equals("spacefilled")) {
		drawingMode = DRAWINGMODE_SPACEFILLED;
	    } else {
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                        "Unrecognized drawing mode '" + drawingModeAsString 
                        + "'");
		return;
	    }
	    double bgcolorR = 
                    DomUtil.getTextForElAsDouble(baseEl, "bgcolor-r", 1);
	    double bgcolorG = 
                    DomUtil.getTextForElAsDouble(baseEl, "bgcolor-g", 1);
	    double bgcolorB = 
                    DomUtil.getTextForElAsDouble(baseEl, "bgcolor-b", 1);
	    double ballSize = 
                    DomUtil.getTextForElAsDouble(baseEl, "ballSize", 1);
	    double rodSize = 
                    DomUtil.getTextForElAsDouble(baseEl, "rodSize", 1);
	    double orientationX = DomUtil.getTextForElAsDouble(baseEl, 
                    "orientation-x", 0);
	    double orientationY = DomUtil.getTextForElAsDouble(baseEl,
		    "orientation-y", 0);
	    double orientationZ = DomUtil.getTextForElAsDouble(baseEl, 
                    "orientation-z", 0);
	    double distance = 
                    DomUtil.getTextForElAsDouble(baseEl, "distance", 50);

	    // Validate the rendering request as best we're able.  
	    // TODO: perhaps we should attempt to determine here if the
	    //       rendering request came from a valid Reciprocal Net site.
	    if (imageSize > maxImageSize) {
		resp.sendError(HttpServletResponse.SC_FORBIDDEN, 
                        "Requested image size is too large - this server's"
                        + " configured maximum is " + maxImageSize);
		return;
	    }
	    
	    // Pass the request off to be rendered.
	    

	} catch (SAXException ex) {
	    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.toString());
	    return;
	} catch (RuntimeException ex) {
	    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    ex.toString());
	    return;
	}

	*/
    }
}
