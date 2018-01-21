<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<%@ page import="org.keycloak.common.util.KeycloakUriBuilder" %>
<%@ page import="org.keycloak.constants.ServiceUrlConstants" %>
<%@ page import="org.keycloak.example.CustomerDatabaseClient" %>
<%@ page import="org.keycloak.representations.IDToken" %>
<%@ page session="false" %>
<html>
<head>
    <title>Customer View Page</title>
</head>
<body bgcolor="#E3F6CE">

    <div>
        <button name="logoutBtn" onclick="location.href = '../index.jsp?logout=true'" type="button">Logout</button>
    </div>
    
<%
    String logoutUri = KeycloakUriBuilder.fromUri("/auth").path(ServiceUrlConstants.TOKEN_SERVICE_LOGOUT_PATH)
            .queryParam("redirect_uri", "/customer-portal").build("demo").toString();
    String acctUri = KeycloakUriBuilder.fromUri("/auth").path(ServiceUrlConstants.ACCOUNT_SERVICE_PATH)
            .queryParam("referrer", "customer-portal").build("demo").toString();
    IDToken idToken = CustomerDatabaseClient.getIDToken(request);
%>
<p>Goto: <a href="/product-portal">Product Portal</a></p>
Servlet User Principal <b><%=request.getUserPrincipal().getName()%></b> made this request.

<p><b>Caller IDToken values</b> (<i>You can specify what is returned in IDToken in the customer-portal claims page in the admin console</i>:</p>
<p>Username: <%=idToken.getPreferredUsername()%></p>
<p>Email: <%=idToken.getEmail()%></p>
<p>Full Name: <%=idToken.getName()%></p>
<p>First: <%=idToken.getGivenName()%></p>
<p>Last: <%=idToken.getFamilyName()%></p>

<h2>Customer Listing</h2>
<%
    java.util.List<String> list = null;
    try {
        list = CustomerDatabaseClient.getCustomers(request);
    } catch (CustomerDatabaseClient.Failure failure) {
        out.println("Status from database service invocation was: " + failure.getStatus());
        return;
    }
    for (String cust : list) {
        out.print("<p>");
        out.print(cust);
        out.println("</p>");

    }
%>
<br><br>
</body>
</html>