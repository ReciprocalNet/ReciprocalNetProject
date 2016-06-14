<%--
  - Reciprocal Net project
  - error.jsp
  -
  - 10-Mar-2003: midurbin wrote first draft
  - 30-Apr-2003: jrhanna added a call to PageHelper.reportException()
  - 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  - 16-Jul-2003: eisiorho fixed bug #923 in UI
  - 09-Feb-2004: ekoperda added notification to CoreConnector in case of
  -              RemoteException's
  - 15-Apr-2004: midurbin fixed bug #1190
  - 22-Feb-2005: ekoperda renamed enum for compatability with JDK 1.5
  - 16-Aug-2005: midurbin removed reference to PageHelper, added the page name
  -              as a request attribute before including /header.jsp and
  -              escaped hidden field value attributes
  - 17-Jan-2006: jobollin brought styles into a style element in the header to
  -              avoid behavioral changes due to the reworked stylesheet
  - 27-Dec-2007: ekoperda enhanced error display and also robustness of error
  -              reporting to core
  - 09-Jan-2008: ekoperda enhanced contents of LogEvent written to core
  --%>
<%@page session="true" 
  isThreadSafe="true"
  isErrorPage="true"
  info="Exception report" 
  import="java.io.ByteArrayOutputStream"
  import="java.io.PrintStream"
  import="java.net.URL"
  import="java.rmi.RemoteException" import="java.util.Enumeration"
  import="javax.servlet.ServletException"
  import="javax.servlet.jsp.JspException"
  import="org.recipnet.common.controls.HtmlControl"
  import="org.recipnet.site.shared.logevent.ExceptionLogEvent"
  import="org.recipnet.site.wrapper.CoreConnector"%>
<%
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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Reciprocal Net - <%=getServletInfo() %></title>
<link href="<%=request.getContextPath()%>/recipnet.css" rel="stylesheet"
        type="text/css" />
<style type="text/css">
  body {
   background: #FFFFFF;
   font-size: small;
   font-family: Georgia, serif;
   margin: 0.2em;
  }
  
  table {
   margin: 0;
   border: 0;
   padding: 0;
  }
  
  tr {
   margin: 0;
   border: 0;
   padding: 0;
  }
  
  th {
   margin: 0;
   border: 0;
   padding: 0;
  }
  
  td {
   margin: 0;
   border: 0;
   padding: 0;
  }
  
  form {
   margin: 0;
   border: 0;
   padding: 0;
  }
  
  .errorMessage {
   color: red;
   font-weight: bold;
  }
    </style>
</head>
<body>
<%
    request.setAttribute("pageTitle", getServletInfo());
 %>
<jsp:include page="/header.jsp" />
<p class="errorMessage">An exception has been encountered within the Reciprocal
Net site software.</p>
<form method="post"
  action="http://www.reciprocalnet.org/master/exceptionreport.cgi">
<center>
<table>
  <tr>
    <td><strong>Exception class:</strong></td>
    <td><%--
                - 'exception' is declared by Tomcat for error pages and has
                - type Throwable.
                --%> <%=exception.getClass().getName()%> <input type="hidden"
      name="except" value="<%=exception.getClass().getName()%>" /></td>
  </tr>
  <%
          if (exception.getMessage() != null) { %>
  <tr>
    <td><strong>Message:</strong></td>
    <td><%=exception.getMessage()%> <input type="hidden" name="msg"
      readonly="true" value="<%=exception.getMessage()%>" /></td>
  </tr>
  <%
          } %>
  <tr>
    <td><strong>Originating URL:</strong></td>
    <td>
      <%=pageContext.getErrorData().getRequestURI()%>
      <input type="hidden" name="url" readonly="true" value="<%=
          HtmlControl.escapeAttributeValue(
          pageContext.getErrorData().getRequestURI())%>" />
    </td>
  </tr>
  <tr>
    <td colspan="2">&nbsp; &nbsp;</td>
  </tr>
  <tr>
    <td align="center" colspan="2"><strong>Stack trace:</strong></td>
  </tr>
  <tr>
    <td align="center" colspan="2">
      <input type="hidden" name="ip"
          value="<%=request.getRemoteAddr()%>" />
      <textarea name="trace" readonly="true" cols="80" rows="10" wrap="off"><%=
          stackTrace.toString() %></textarea>
      <input type="hidden" name="params" value="<%
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
          } %>" />
    </td>
  </tr>
</table>
</center>
<p>&nbsp;</p>
<p>&nbsp;</p>
<table width="50%">
  <tr>
    <td align="left">You may optionally refer the details of this error message
    to Reciprocal Net technical support personnel electronically. If you require
    assistance, however, you should contact the administrator of the particular
    Reciprocal Net site where the error occured.</td>
  </tr>
  <tr>
    <td align="left"><br />
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input type="submit"
      value="Report to technical support" /></td>
  </tr>
</table>
</form>
<jsp:include page="/footer.jsp" />
</body>
</html>
