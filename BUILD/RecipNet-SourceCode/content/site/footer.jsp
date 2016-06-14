<%--
  - Reciprocal Net project
  - footer.jsp
  -
  - 06-Mar-2003: jobollin added a copyright notice per task #766
  - 29-Mar-2004: cwestnea added line about NSF per task #1172
  - 21-Jun-2004: cwestnea replaced style classes with inline styles
  - 18-Jan-2006: jobollin converted to custom tags for the context-relative
  -              <img/> element
  - 11-Jan-2008: ekoperda updated copyright notice
  - 20-Mar-2009: ekoperda udpated copyright notice
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<ctl:page suppressGeneratedHtml="true">
<hr size="1" style="color: #9E9E9E; clear: both;"/>
<div style="font-family: sans-serif; font-size: x-small; color: #9E9E9E;
      text-align: center; ">
  Reciprocal Net site software @BUILDNUMBER@,
  copyright (c) 2002-2009, The Trustees of Indiana University<br />
  Files and data presented via this software are property of their
  respective owners.<br />
  Reciprocal Net is funded by the U.S. National Science Foundation as part of
  the National Science Digital Library project.
  <a href="http://www.nsdl.org" style="padding-left: 0.5em;"><ctl:img
    height="11" border="0" src="/images/nsdl_logo_small.gif" alt="NSDL"/></a>
</div>
</ctl:page>
