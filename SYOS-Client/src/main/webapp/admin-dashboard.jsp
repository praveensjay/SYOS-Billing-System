<%--
  Created by IntelliJ IDEA.
  User: praveen
  Date: 2025-04-04
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/admin-dashboard.css">

    <script>
        // Redirect if no role is found in session storage or if role is not ADMIN


        function logout() {
            sessionStorage.clear();
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<div class="container">
    <header class="admin-navbar">
        <div class="admin-navbar-content">
            <h2>Welcome, <span id="admin-name"></span></h2>
            <p>Role: <span id="admin-role"></span></p>
        </div>
        <button class="logout-btn" onclick="logout()">Logout</button>
    </header>

    <script>
        document.getElementById("admin-name").textContent = sessionStorage.getItem("name") || "Admin";
        document.getElementById("admin-role").textContent = sessionStorage.getItem("role") || "ADMIN";
    </script>

    <nav>
        <ul>
            <li><a href="manage-items.jsp">Manage Items</a></li>
            <li><a href="manage-customers.jsp">Manage Customers</a></li>
            <li><a href="manage-stock.jsp">Manage Stock</a></li>
            <li><a href="manage-billing.jsp">Manage Billing</a></li>
            <li><a href="manage-reports.jsp">Generate Reports</a></li>
        </ul>
    </nav>
</div>
<!-- Footer Section -->
<footer class="admin-footer">
    <p>&copy; 2025 SYOS Store. All Rights Reserved.</p>
    <p>Praveen Sanjana</p>
</footer>
</body>
</html>
