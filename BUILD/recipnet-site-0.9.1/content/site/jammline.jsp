<%--
  -- Reciprocal Net Project
  --
  -- jammline.jsp
  --
  -- 22-Oct-2002: jobollin wrote first draft
  -- 12-Dec-2002: jobollin inserted the context path into the stylesheet link
  --              (task #642)
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="Line Drawing">
  <ctl:requestAttributeChecker attributeName="suppressNavigationLinks"
          includeIfAttributeIsPresent="false">
    <ctl:redirect target="/index.jsp" />
  </ctl:requestAttributeChecker>
  <br />
  <h1>
    Line Drawing of
    <ctl:displayRequestAttributeValue attributeName="labShortName" /> sample
    <ctl:displayRequestAttributeValue attributeName="sampleLocalLabId" />
  </h1>
  <rn:fileRetrieverImg alt="chemical line drawing"
          servletUrl="/servlet/fileretrieve/jamm-line.jpeg"
          keyRequestAttributeName="jpegKey" />
  <hr />
  <ul>
    <li>
      <ctl:a href="/servlet/fileretrieve/jamm-line.eps">
        <ctl:requestAttributeParam name="key" attributeName="epsKey" />
        Postscript Version
      </ctl:a>
    </li>
    <li>
      <ctl:a href="/servlet/fileretrieve/jamm-line.jpeg">
        <ctl:requestAttributeParam name="key" attributeName="jpegKey" />
        JPEG Version
      </ctl:a>
    </li>
  </ul>
</rn:page>
