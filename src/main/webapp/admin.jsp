<%@ page import="java.util.*" %>
<%@ page import="com.example.model.Task, com.example.service.TaskManager" %>
<%
    if (session == null || !"admin".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Task> tasks = TaskManager.getAllTasks();
%>
<html>
<head>
    <title>Admin</title>
    <style>
        .finished { color: green; }
        .processing { color: orange; }
    </style>
</head>
<body>
<h2>Admin Dashboard | <a href="logout.jsp">Logout</a></h2>

<h3>Task Queue</h3>
<table border="1">
    <tr>
        <th>#</th>
        <th>Username</th>
        <th>Status</th>
    </tr>
<%
    int index = 1;
    for (Task task : tasks) {
        String colorClass = "Finished".equals(task.getStatus()) ? "finished" : "processing";
%>
    <tr>
        <td>Task <%= index++ %></td>
        <td><%= task.getClientId() %></td>
        <td><span class="<%= colorClass %>">&#9679;</span> <%= task.getStatus() %></td>
    </tr>
<% } %>
</table>
</body>
</html>
