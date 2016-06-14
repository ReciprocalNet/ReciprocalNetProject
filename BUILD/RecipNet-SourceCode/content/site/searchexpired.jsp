<%--
   - Reciprocal Net project
   - searchexpired.jsp
   -
   - 30-Jun-2005: ekoperda wrote first draft
  --%>
<%@ taglib uri="/WEB-INF/controls.tld" prefix="ctl" %>
<%@ taglib uri="/WEB-INF/rncontrols.tld" prefix="rn" %>

<rn:page title="Search expired">
  <ctl:selfForm>
    <p class="errorMessage">
      The search results you attempted to view have expired and are no longer
      available.  This error could have occurred if you initiated your search a
      long time ago.  Alternately, the error might have occured if this sever
      were operating under an unusually heavy load.  Please return to the
      <ctl:a href="/search.jsp">search criteria page</ctl:a> and begin your
      search again.
    </p>
    <blockquote>
      <rn:buttonLink label="Begin search again" 
          target="/search.jsp" />
    </blockquote>
  </ctl:selfForm>
  <ctl:styleBlock>
    .errorMessage  { color: red; font-weight: bold;}
  </ctl:styleBlock>
</rn:page>
