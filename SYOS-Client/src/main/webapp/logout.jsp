<%--
  Created by IntelliJ IDEA.
  User: sumuditha
  Date: 2025-02-07
  Time: 23:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%
    // Invalidate server-side session
    session.invalidate();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Logging out...</title>
    <script>
        // Clear client-side session storage
        sessionStorage.clear();

        // Redirect to login page after clearing everything
        window.location.href = "login.jsp";
    </script>
</head>
<body>
<p>Logging out... Please wait.</p>
</body>
</html>



