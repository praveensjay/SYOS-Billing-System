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
    <title>Shopping Cart - Customer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/cart.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            loadCartItems();
            updateCartCount();
        });

        function loadCartItems() {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            var cartContainer = document.getElementById("cart-items");
            cartContainer.innerHTML = "";

            if (cart.length === 0) {
                cartContainer.innerHTML = "<p class='empty-cart'>Your cart is empty. <a href='shop.jsp'>Shop now</a></p>";
                document.getElementById("total-price").textContent = "LKR0.00";
                return;
            }

            let total = 0;

            cart.forEach(function(item, index) {
                var cartRow = document.createElement("div");
                cartRow.classList.add("cart-item");

                var itemDetails = document.createElement("div");
                itemDetails.classList.add("item-details");

                var itemName = document.createElement("h3");
                itemName.textContent = item.itemName;

                var itemCode = document.createElement("p");
                itemCode.textContent = "Code: " + item.itemCode;

                var itemPrice = document.createElement("p");
                itemPrice.textContent = "Price: LKR" + item.itemPrice.toFixed(2);

                itemDetails.appendChild(itemName);
                itemDetails.appendChild(itemCode);
                itemDetails.appendChild(itemPrice);

                var quantityControls = document.createElement("div");
                quantityControls.classList.add("quantity-controls");

                var decreaseBtn = document.createElement("button");
                decreaseBtn.textContent = "- ";
                decreaseBtn.onclick = function () {
                    updateQuantity(index, item.quantity - 1);
                };

                var quantityDisplay = document.createElement("span");
                quantityDisplay.textContent = item.quantity;

                var increaseBtn = document.createElement("button");
                increaseBtn.textContent = "+ ";
                increaseBtn.onclick = function () {
                    updateQuantity(index, item.quantity + 1);
                };

                quantityControls.appendChild(decreaseBtn);
                quantityControls.appendChild(quantityDisplay);
                quantityControls.appendChild(increaseBtn);

                var removeBtn = document.createElement("button");
                removeBtn.textContent = "❌ Remove";
                removeBtn.classList.add("remove-btn");
                removeBtn.onclick = function () {
                    removeItem(index);
                };

                cartRow.appendChild(itemDetails);
                cartRow.appendChild(quantityControls);
                cartRow.appendChild(removeBtn);

                cartContainer.appendChild(cartRow);

                total += item.itemPrice * item.quantity;
            });

            document.getElementById("total-price").textContent = "LKR" + total.toFixed(2);
        }

        function updateQuantity(index, newQuantity) {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            if (newQuantity < 1) {
                cart.splice(index, 1);
            } else {
                cart[index].quantity = newQuantity;
            }
            localStorage.setItem("cart", JSON.stringify(cart));
            loadCartItems();
            updateCartCount();
        }

        function removeItem(index) {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            cart.splice(index, 1);
            localStorage.setItem("cart", JSON.stringify(cart));
            loadCartItems();
            updateCartCount();
        }

        function updateCartCount() {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            document.getElementById("cart-count").textContent = cart.length;
        }

        function proceedToCheckout() {
            window.location.href = "checkout.jsp";
        }
    </script>
</head>
<body>

<!-- Header Section (Navbar) -->
<header class="customer-navbar">
    <div class="customer-navbar-content">
        <h2>Shopping Cart</h2>
        <p>Welcome, <span id="customer-name"></span></p>
    </div>
    <div class="nav-links">
        <a href="shop.jsp" class="cart-link">🛍️ Back to Shop</a>
        <a href="customer-dashboard.jsp" class="dashboard-link">🏠 Back to Dashboard</a>
    </div>
</header>

<!-- Main Content -->
<main class="cart-container">
    <h2>Your Cart</h2>

    <!-- Cart Items -->
    <div id="cart-items" class="cart-items-container">
        <!-- Cart Items will be populated here dynamically -->
    </div>

    <!-- Cart Summary -->
    <div class="cart-summary">
        <h3>Total: <span id="total-price">LKR0.00</span></h3>
        <button onclick="proceedToCheckout()">Proceed to Checkout</button>
    </div>
</main>

<!-- Footer Section -->
<footer class="customer-footer">
    <p>&copy; 2025 SYOS Store. All Rights Reserved.</p>
    <p>Praveen Sanjana</p>
</footer>

<script>
    document.getElementById("customer-name").textContent = sessionStorage.getItem("name") || "Customer";
</script>

</body>
</html>

