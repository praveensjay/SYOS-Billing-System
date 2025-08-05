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
    <title>My Orders - Customer</title>
    <link rel="stylesheet" type="text/css" href="assets/styles/orders.css">

    <script>


        document.addEventListener("DOMContentLoaded", function () {
            loadOrders();
        });

        function loadOrders() {
            var customerId = parseInt(sessionStorage.getItem("userid"), 10);
            if (!customerId || isNaN(customerId) || customerId <= 0) {
                alert("Error: Customer ID is missing. Please log in again.");
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/orders/customer/" + customerId)
                .then(response => response.json())
                .then(orders => {
                    if (!Array.isArray(orders) || orders.length === 0) {
                        document.getElementById("orders-list").innerHTML = "<p class='empty-orders'>No orders found.</p>";
                        return;
                    }
                    displayOrders(orders);
                })
                .catch(error => console.error("Error fetching orders:", error));
        }

        function displayOrders(orders) {
            var ordersContainer = document.getElementById("orders-list");
            ordersContainer.innerHTML = "";

            orders.forEach(order => {
                var orderRow = document.createElement("div");
                orderRow.classList.add("order-card");

                orderRow.innerHTML = `
                    <h3>Order ID: ` + order.orderId + `</h3>
                    <p><strong>Date:</strong> ` + order.orderDate + `</p>
                    <p><strong>Total:</strong> ₤` + order.totalAmount.toFixed(2) + `</p>
                    <p><strong>Payment Method:</strong> ` + order.paymentMethod + `</p>
                    <button onclick="viewOrderDetails(` + order.orderId + `)">View Details</button>
                `;

                ordersContainer.appendChild(orderRow);
            });
        }

        function viewOrderDetails(orderId) {
            fetch("http://localhost:8080/SYOS-Server/api/orders/" + orderId)
                .then(response => response.json())
                .then(order => {
                    displayOrderDetails(order);
                })
                .catch(error => console.error("Error fetching order details:", error));
        }

        function displayOrderDetails(order) {
            var orderDetailsContainer = document.getElementById("order-details");
            orderDetailsContainer.innerHTML = `
                <h2>Order Details</h2>
                <p><strong>Order ID:</strong> ` + order.orderId + `</p>
                <p><strong>Date:</strong> ` + order.orderDate + `</p>
                <p><strong>Total:</strong> ₤` + order.totalAmount.toFixed(2) + `</p>
                <p><strong>Payment Method:</strong> ` + order.paymentMethod + `</p>
                <h3>Items:</h3>
                <ul>
            `;

            order.items.forEach(item => {
                orderDetailsContainer.innerHTML += `
                    <li>` + item.itemName + ` - ` + item.quantity + ` x ₤` + item.itemPrice.toFixed(2) + `</li>
                `;
            });

            orderDetailsContainer.innerHTML += "</ul>";
            orderDetailsContainer.style.display = "block";
        }
    </script>
</head>
<body>

<header class="customer-navbar">
    <div class="customer-navbar-content">
        <h2>My Orders</h2>
        <p>Welcome, <span id="customer-name"></span></p>
    </div>
    <div class="nav-links">
        <a href="shop.jsp" class="cart-link">🛍️ Back to Shop</a>
        <a href="customer-dashboard.jsp" class="dashboard-link">🏠 Back to Dashboard</a>
    </div>
</header>

<main class="orders-container">
    <h2>Your Orders</h2>
    <div id="orders-list"></div>
    <div id="order-details" class="order-details-container" style="display: none;"></div>
</main>

<script>
    document.getElementById("customer-name").textContent = sessionStorage.getItem("name") || "Customer";
</script>

</body>
</html>

