<%--
  -- Reciprocal Net Project
  --
  -- jammrender.jsp
  --
  -- 22-Oct-2002: jobollin wrote first draft
  -- 12-Dec-2002: jobollin inserted the context path into the stylesheet link
  --              (task #642)
  -- 15-Aug-2005: midurbin rewrote using custom tags
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Rendered Image">
  <ctl:requestAttributeChecker attributeName="suppressNavigationLinks"
          includeIfAttributeIsPresent="false">
    <ctl:redirect target="/index.jsp" />
  </ctl:requestAttributeChecker>
  <br />
  <h1>
    Rendered Image of
    <ctl:displayRequestAttributeValue attributeName="labShortName" /> sample
    <ctl:displayRequestAttributeValue attributeName="sampleLocalLabId" />
  </h1>
  <rn:fileRetrieverImg alt="photorealistic molecule image"
          servletUrl="/servlet/fileretrieve"
          keyRequestAttributeName="jpegKey" />
  <hr />
  <ul>
    <li>
      <ctl:a href="/servlet/fileretrieve">
        <ctl:requestAttributeParam name="key" attributeName="jpegKey" />
        JPEG Version
      </ctl:a>
    </li>
  </ul>
</rn:page>
