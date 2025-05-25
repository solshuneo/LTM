<%@ page import="java.util.*" %>
<%@ page import="com.example.model.Task, com.example.service.TaskManager" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!"user".equals(session.getAttribute("role"))) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = (String) session.getAttribute("username");
    List<Task> tasks = TaskManager.getUserTasks(username);
%>
<html>
<head>
    <title>Home</title>
    <style>
        .finished { color: green; }
        .processing { color: orange; }
    </style>
</head>
<%
    String msg = (String) session.getAttribute("uploadMessage");
    if (msg != null) {
%>
    <div style="color: green; font-weight: bold;"><%= msg %></div>

    <% if ("Uploaded, Processing".equals(msg)) { %>
        <script>
            // Tự động reload sau 3 giây
            setTimeout(() => {
                window.location.reload();
            }, 3000);
        </script>
    <% } %>

<%
        // Optional: clear sau khi hiển thị
        session.removeAttribute("uploadMessage");
    }
%>

<body>
<h2>Welcome, <%= username %> | <a href="logout.jsp">Logout</a></h2>

<h3>Upload Image</h3>
<form method="post" action="upload" enctype="multipart/form-data">
    <input type="file" name="picture" required>
    <input type="submit" value="Upload">
</form>

<h3>Your Tasks</h3>
<table border="1">
    <tr>
        <th>#</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
<%
    int index = 1;
    for (Task task : tasks) {
        String colorClass = "Finished".equals(task.getStatus()) ? "finished" : "processing";
%>
    <tr>
        <td>Task <%= index++ %></td>
        <td><span class="<%= colorClass %>">&#9679;</span> <%= task.getStatus() %></td>
        <td>
            <% if ("Finished".equals(task.getStatus())) { %>
                <a href="download?file=<%= task.getFileResult() %>">Download</a>
            <% } else { %>
                Not ready
            <% } %>
        </td>
    </tr>
<% } %>
</table>
</body>
</html>
