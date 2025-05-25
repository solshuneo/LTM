<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%
    if (session != null) {
        java.util.Enumeration<String> attrNames = session.getAttributeNames();
%>
        <h3>Session attributes:</h3>
        <ul>
        <%
            while (attrNames.hasMoreElements()) {
                String name = attrNames.nextElement();
                Object value = session.getAttribute(name);
        %>
            <li><b><%= name %></b> : <%= value %></li>
        <%
            }
        %>
        </ul>
    <%
        } else {
    %>
        <p>Session is null</p>
<%
    }
    if (session != null && session.getAttribute("username") != null) {
        String role = (String) session.getAttribute("role");
        if ("admin".equals(role)) response.sendRedirect("admin.jsp");
        else response.sendRedirect("home.jsp");
        return;
    }
%>
<html>
<head><title>Login</title></head>
<body>
<h2>Login</h2>
<form action="login" method="post">
    Username: <input type="text" name="username" required><br/>
    Password: <input type="password" name="password" required><br/>
    <input type="submit" value="Login">
</form>
<% if (request.getAttribute("error") != null) { %>
    <p style="color:red;"><%= request.getAttribute("error") %></p>
<% } %>
</body>
</html>
