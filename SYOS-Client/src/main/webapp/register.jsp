<%--
  Created by IntelliJ IDEA.
  User: praveen
  Date: 2025-04-04
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - SYOS</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/styles.css">
    <script>
        async function registerUser(event) {
            event.preventDefault(); // Prevent form submission

            const name = document.getElementById("name").value;
            const email = document.getElementById("email").value;
            const password = document.getElementById("password").value;
            const messageContainer = document.getElementById("message-container");

            const userData = {
                name: name,
                email: email,
                password: password,
                role: "CUSTOMER"
            };

            const response = await fetch("http://localhost:8080/SYOS-Server/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });

            const result = await response.json();
            if (response.ok) {
                messageContainer.innerHTML = "<p class='success-message'>✅ Registration successful! Redirecting to login...</p>";
                setTimeout(() => {
                    window.location.href = "login.jsp";
                }, 2000);
            } else {
                messageContainer.innerHTML = "<p class='error-message'>❌ " + result.error + "</p>";
            }
        }
    </script>
</head>
<body>
<div class="container">
    <h2>Register</h2>
    <form onsubmit="registerUser(event)">
        <label>Name:</label>
        <input type="text" id="name" required>

        <label>Email:</label>
        <input type="email" id="email" required>

        <label>Password:</label>
        <input type="password" id="password" required>

        <button type="submit">Register</button>
    </form>
    <p>Already have an account? <a href="login.jsp">Login here</a></p>

    <!-- Message container -->
    <div id="message-container"></div>
</div>
</body>
</html>



