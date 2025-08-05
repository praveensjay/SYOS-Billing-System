<%--
  Created by IntelliJ IDEA.
  User: praveen
  Date: 2025-04-04
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SYOS Store</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/index.css">
</head>
<body class="bg-gray-100">

<!-- Header Section -->
<header class="bg-blue-600 p-6 text-white text-center">
    <h1 class="text-3xl font-bold">Welcome to SYOS Store</h1>
    <p class="text-lg">Your one-stop shop for all things amazing!</p>

    <!-- Login/Register Links -->
    <div class="auth-links">
        <a href="login.jsp" class="bg-green-600 text-white py-2 px-6 rounded-lg mr-4">Login</a>
        <a href="register.jsp" class="bg-blue-600 text-white py-2 px-6 rounded-lg">Register</a>
    </div>
</header>

<!-- Hero Section -->
<section class="bg-white p-8">
    <div class="mt-8 text-center">
        <h3 class="text-xl font-semibold mb-4">Why Shop with SYOS Store?</h3>
        <p class="text-gray-700 mb-4">We prioritize quality, convenience, and customer satisfaction. Here’s why customers love us:</p>

        <ul class="list-disc text-left mx-auto max-w-xl text-gray-600">
            <li><strong>Fast Delivery:</strong> Get your groceries delivered in as little as 24 hours, ensuring your convenience and satisfaction.</li>
            <li><strong>100% Fresh Produce:</strong> We handpick every item to ensure only the best quality, delivered fresh to your door.</li>
            <li><strong>Affordable Prices:</strong> Enjoy great deals and discounts on all your favorite items, making shopping affordable for everyone.</li>
            <li><strong>Secure Shopping:</strong> Shop with peace of mind, knowing your data is safe with us, thanks to our top-notch security systems.</li>
        </ul>

        <!-- CTA: Start Shopping (redirect to login) -->
        <a href="login.jsp" class="bg-blue-600 text-white py-2 px-6 rounded-lg mt-6 inline-block">Start Shopping</a>

        <div class="mt-12 text-center">
            <h3 class="text-xl font-semibold mb-4">Additional Reasons to Choose SYOS</h3>
            <p class="text-gray-700 mb-4">We're not just about groceries, we're about making your life easier. Here's how:</p>
            <ul class="list-disc text-left mx-auto max-w-xl text-gray-600">
                <li><strong>24/7 Customer Support:</strong> Our dedicated team is available around the clock to assist with any inquiries.</li>
                <li><strong>Exclusive Offers:</strong> As a SYOS member, you get access to exclusive discounts, promotions, and giveaways.</li>
                <li><strong>Easy Returns & Exchanges:</strong> If you’re not happy with a product, we make returns and exchanges hassle-free.</li>
                <li><strong>Loyalty Program:</strong> Earn reward points on every purchase and redeem them for discounts and special offers.</li>
            </ul>
        </div>
    </div>
</section>

<!-- Footer Section -->
<footer class="bg-blue-600 text-white text-center p-4 mt-8">
    <p>&copy; 2025 SYOS Store. All Rights Reserved.</p>
    <p>Praveen Sanjana</p>
</footer>

</body>
</html>
