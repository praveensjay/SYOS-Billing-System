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
    <title>Customer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="assets/styles/customer-dashboard.css">

    <script>
        function logout() {
            sessionStorage.clear();
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>

<!-- Header Section (Navbar) -->
<header class="customer-navbar">
    <div class="customer-navbar-content">
        <h2>Welcome, <span id="customer-name"></span></h2>
        <p>Role: <span id="customer-role"></span></p>
    </div>

    <!-- Dropdown Menu for Quick Access (Right-aligned) -->
    <div class="dropdown">
        <button class="dropbtn">Quick Access <span class="down-arrow">▼</span></button>
        <div class="dropdown-content">
            <a href="shop.jsp">Grocery Shop 🥦</a>
            <a href="cart.jsp">View Cart 🛒</a>
            <a href="checkout.jsp">Checkout 💳</a>
            <a href="orders.jsp">My Orders 📦</a>
        </div>
    </div>

    <button class="logout-btn" onclick="logout()">Logout</button>
</header>

<!-- Main Content -->
<main class="customer-container">
    <section class="welcome-section">
        <h3>Welcome Back to SYOS Store!</h3>
        <p>We're excited to have you back! Browse fresh groceries, manage your orders, and more!</p>
    </section>

    <section class="grocery-info">
        <h3>Fresh Groceries, Delivered to Your Doorstep</h3>
        <p>We offer a wide selection of fresh produce, dairy, snacks, and more. Our goal is to provide you with the best quality groceries at affordable prices, all delivered straight to your home. Enjoy quick delivery times and secure shopping with SYOS Store.</p>

        <h4>Why Choose SYOS Store?</h4>
        <ul>
            <li><strong>Fast Delivery:</strong> Get your groceries delivered in as little as 24 hours.</li>
            <li><strong>Fresh Produce:</strong> Handpicked, high-quality fruits and vegetables.</li>
            <li><strong>Affordable Prices:</strong> Great deals on all your grocery essentials.</li>
            <li><strong>Secure Shopping:</strong> Enjoy a safe, hassle-free shopping experience with us.</li>
        </ul>
    </section>

    <!-- Banner Image Section -->
    <section class="grocery-banner">
        <img src="assets/images/grocery_store_banner.jpg" alt="Grocery Store" class="banner-image">
    </section>
</main>

<!-- Footer Section -->
<footer class="customer-footer">
    <p>&copy; 2025 SYOS Store. All Rights Reserved.</p>
    <p>Praveen Sanjana</p>
</footer>

<script>
    document.getElementById("customer-name").textContent = sessionStorage.getItem("name") || "Customer";
    document.getElementById("customer-role").textContent = sessionStorage.getItem("role") || "CUSTOMER";
</script>

</body>
</html>




