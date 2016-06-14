<%--
  -- Reciprocal Net project
  -- logout.jsp
  --
  -- 18-Jul-2002: jobollin wrote first draft
  -- 26-Jun-2003: dfeng replaced include file common.inc with common.jspf
  -- 02-Jul-2004: cwestnea removed reference to AuthenticationController
  -- 11-Jan-2005: jobollin added logging via the new AuthenticationLogger
  -- 13-Jul-2005: midurbin rewrote using custom tags
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<ctl:page>
  <rn:logout />
  <ctl:redirect target="/index.jsp" />
</ctl:page>
