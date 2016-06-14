package org.recipnet.site.content;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import org.recipnet.common.controls.HtmlControl;
import org.recipnet.site.shared.logevent.ExceptionLogEvent;
import org.recipnet.site.wrapper.CoreConnector;

public final class error_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  public String getServletInfo() {
    return "Exception report";
  }

  private static java.util.Vector _jspx_dependants;

  public java.util.List getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    JspFactory _jspxFactory = null;
    PageContext pageContext = null;
    HttpSession session = null;
    Throwable exception = org.apache.jasper.runtime.JspRuntimeLibrary.getThrowable(request);
    if (exception != null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      _jspxFactory = JspFactory.getDefaultFactory();
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\n');
      out.write('\n');

    // Report the exception to core, if possible.
    CoreConnector coreConnector = CoreConnector.extract(
            session.getServletContext());
    if (exception instanceof RemoteException) {
        coreConnector.reportRemoteException((RemoteException) exception);
    }
    try {
        coreConnector.getSiteManager().recordLogEvent(new ExceptionLogEvent(
                session.getId(), request.getServerName(),
                pageContext.getErrorData().getRequestURI(), exception));
    } catch (RemoteException ex) {
        // The exception could not be reported to core for some reason.  Don't
        // know why, but we're powerless to fix it at this point.  Just eat the
        // exception and continue on...
    }

    // Generate a detailed stack trace in memory.  Descend into "root causes"
    // recursively whenever possible.
    ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(stackTrace);
    Throwable parentException;
    do {
        exception.printStackTrace(ps);
        parentException = exception;
        if (exception instanceof ServletException) {
            exception = ((ServletException) exception).getRootCause();
        } else if (exception instanceof JspException) {
            exception = ((JspException) exception).getRootCause();
        }
    } while (exception != parentException);
    ps.close();

      out.write("\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n          \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n<head>\n<title>Reciprocal Net - ");
      out.print(getServletInfo() );
      out.write("</title>\n<link href=\"");
      out.print(request.getContextPath());
      out.write("/recipnet.css\" rel=\"stylesheet\"\n        type=\"text/css\" />\n<style type=\"text/css\">\n  body {\n   background: #FFFFFF;\n   font-size: small;\n   font-family: Georgia, serif;\n   margin: 0.2em;\n  }\n  \n  table {\n   margin: 0;\n   border: 0;\n   padding: 0;\n  }\n  \n  tr {\n   margin: 0;\n   border: 0;\n   padding: 0;\n  }\n  \n  th {\n   margin: 0;\n   border: 0;\n   padding: 0;\n  }\n  \n  td {\n   margin: 0;\n   border: 0;\n   padding: 0;\n  }\n  \n  form {\n   margin: 0;\n   border: 0;\n   padding: 0;\n  }\n  \n  .errorMessage {\n   color: red;\n   font-weight: bold;\n  }\n    </style>\n</head>\n<body>\n");

    request.setAttribute("pageTitle", getServletInfo());
 
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/header.jsp", out, false);
      out.write("\n<p class=\"errorMessage\">An exception has been encountered within the Reciprocal\nNet site software.</p>\n<form method=\"post\"\n  action=\"http://www.reciprocalnet.org/master/exceptionreport.cgi\">\n<center>\n<table>\n  <tr>\n    <td><strong>Exception class:</strong></td>\n    <td>");
      out.write(' ');
      out.print(exception.getClass().getName());
      out.write(" <input type=\"hidden\"\n      name=\"except\" value=\"");
      out.print(exception.getClass().getName());
      out.write("\" /></td>\n  </tr>\n  ");

          if (exception.getMessage() != null) { 
      out.write("\n  <tr>\n    <td><strong>Message:</strong></td>\n    <td>");
      out.print(exception.getMessage());
      out.write(" <input type=\"hidden\" name=\"msg\"\n      readonly=\"true\" value=\"");
      out.print(exception.getMessage());
      out.write("\" /></td>\n  </tr>\n  ");

          } 
      out.write("\n  <tr>\n    <td><strong>Originating URL:</strong></td>\n    <td>\n      ");
      out.print(pageContext.getErrorData().getRequestURI());
      out.write("\n      <input type=\"hidden\" name=\"url\" readonly=\"true\" value=\"");
      out.print(
          HtmlControl.escapeAttributeValue(
          pageContext.getErrorData().getRequestURI()));
      out.write("\" />\n    </td>\n  </tr>\n  <tr>\n    <td colspan=\"2\">&nbsp; &nbsp;</td>\n  </tr>\n  <tr>\n    <td align=\"center\" colspan=\"2\"><strong>Stack trace:</strong></td>\n  </tr>\n  <tr>\n    <td align=\"center\" colspan=\"2\">\n      <input type=\"hidden\" name=\"ip\"\n          value=\"");
      out.print(request.getRemoteAddr());
      out.write("\" />\n      <textarea name=\"trace\" readonly=\"true\" cols=\"80\" rows=\"10\" wrap=\"off\">");
      out.print(
          stackTrace.toString() );
      out.write("</textarea>\n      <input type=\"hidden\" name=\"params\" value=\"");

          Enumeration enumeration = request.getParameterNames();
          while (enumeration.hasMoreElements()) {
              String param = (String) enumeration.nextElement();
              out.print(param + "=");
              String values[] = request.getParameterValues(param);
              for (int it = 0; it < values.length; it ++) {
                  out.print(HtmlControl.escapeAttributeValue(values[it])
                          + ((it + 1 < values.length)
                          ? ", " : "\n"));
              }
          } 
      out.write("\" />\n    </td>\n  </tr>\n</table>\n</center>\n<p>&nbsp;</p>\n<p>&nbsp;</p>\n<table width=\"50%\">\n  <tr>\n    <td align=\"left\">You may optionally refer the details of this error message\n    to Reciprocal Net technical support personnel electronically. If you require\n    assistance, however, you should contact the administrator of the particular\n    Reciprocal Net site where the error occured.</td>\n  </tr>\n  <tr>\n    <td align=\"left\"><br />\n    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type=\"submit\"\n      value=\"Report to technical support\" /></td>\n  </tr>\n</table>\n</form>\n");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/footer.jsp", out, false);
      out.write("\n</body>\n</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      if (_jspxFactory != null) _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
