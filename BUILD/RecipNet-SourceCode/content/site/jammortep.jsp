<%--
  -- Reciprocal Net Project
  --
  -- jammortep.jsp
  --
  -- 22-Oct-2002: jobollin wrote first draft
  -- 12-Dec-2002: jobollin inserted the context path into the stylesheet link
  --              (task #642)
  -- 15-Aug-2005: midurbin rewrote using custom tags
  -- 18-Jun-2008: ekoperad fixed spelling errors in UI
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>
<rn:page title="ORTEP Image">
  <ctl:requestAttributeChecker attributeName="suppressNavigationLinks"
          includeIfAttributeIsPresent="false">
    <ctl:redirect target="/index.jsp" />
  </ctl:requestAttributeChecker>
  <br />
  <h1>
    ORTEP<sup>*</sup> rendition of
    <ctl:displayRequestAttributeValue attributeName="labShortName" /> sample
    <ctl:displayRequestAttributeValue attributeName="sampleLocalLabId" />
  </h1>
  <rn:fileRetrieverImg alt="thermal ellipsoid plot"
          servletUrl="/servlet/fileretrieve"
          keyRequestAttributeName="jpegKey" />
  <hr />
  <ul>
    <li>
      <ctl:a href="/servlet/fileretrieve/jamm-ortep.eps">
        <ctl:requestAttributeParam name="key" attributeName="epsKey" />
        Postscript Version
      </ctl:a>
    </li>
    <li>
      <ctl:a href="/servlet/fileretrieve">
        <ctl:requestAttributeParam name="key" attributeName="jpegKey" />
        JPEG Version
      </ctl:a>
    </li>
    <li>
      <ctl:a href="/servlet/fileretrieve/jamm-ortep.ort">
        <ctl:requestAttributeParam name="key" attributeName="ortinKey" />
        Ortep Input File
      </ctl:a>
    </li>
  </ul>
  <hr />
  <p class="footnote">
    *    Michael N. Burnett and Carroll K. Johnson,
    ORTEP-III: Oak Ridge Thermal Ellipsoid Plot Program for Crystal
    Structure Illustrations, Oak Ridge National Laboratory Report
    ORNL-6895, 1996
  </p>
</rn:page>
