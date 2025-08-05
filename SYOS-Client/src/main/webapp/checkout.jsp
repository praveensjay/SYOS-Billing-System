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
    <title>Checkout - Customer</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/checkout.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            loadCartItems();
            var cartCountElement = document.getElementById("cart-count");
            if (cartCountElement) {
                updateCartCount();
            }
            document.getElementById("address-section").style.display = "none";
        });

        function loadCartItems() {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            var cartContainer = document.getElementById("checkout-items");
            cartContainer.innerHTML = "";

            if (cart.length === 0) {
                cartContainer.innerHTML = "<p class='empty-cart'>Your cart is empty. <a href='shop.jsp'>Shop now</a></p>";
                document.getElementById("total-price").textContent = "LKR0.00";
                return;
            }

            var total = 0;
            cart.forEach(function(item) {
                var cartRow = document.createElement("div");
                cartRow.classList.add("checkout-item");

                cartRow.innerHTML = `
                    <div class="item-details">
                        <span class="item-name">` + item.itemName + `</span>
                        <span class="item-code">Code: ` + item.itemCode + `</span>
                        <span class="item-price">Price: LKR` + item.itemPrice.toFixed(2) + `</span>
                        <span class="item-quantity">Quantity: ` + item.quantity + `</span>
                    </div>
                `;

                cartContainer.appendChild(cartRow);
                total += item.itemPrice * item.quantity;
            });

            document.getElementById("total-price").textContent = "LKR" + total.toFixed(2);
        }

        function updateCartCount() {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            var cartCountElement = document.getElementById("cart-count");
            if (cartCountElement) {
                cartCountElement.textContent = cart.length;
            }
        }

        function toggleAddressField() {
            var selectedMethod = document.querySelector('input[name="payment-method"]:checked');
            document.getElementById("address-section").style.display = (selectedMethod && selectedMethod.value === "cash-on-delivery") ? "block" : "none";
        }

        function processCheckout(event) {
            event.preventDefault();

            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            if (cart.length === 0) {
                alert("Your cart is empty.");
                return;
            }

            var paymentMethod = document.querySelector('input[name="payment-method"]:checked');
            if (!paymentMethod) {
                alert("Please select a payment method.");
                return;
            }

            var customerAddress = document.getElementById("customer-address").value;
            if (paymentMethod.value === "cash-on-delivery" && customerAddress.trim() === "") {
                alert("Please enter your delivery address.");
                return;
            }

            var customerId = parseInt(sessionStorage.getItem("userid"), 10);
            if (!customerId || isNaN(customerId) || customerId <= 0) {
                alert("Error: Customer ID is missing. Please log in again.");
                return;
            }

            var orderData = {
                items: cart,
                total: parseFloat(document.getElementById("total-price").textContent.replace("₤", "")),
                paymentMethod: paymentMethod.value,
                address: paymentMethod.value === "cash-on-delivery" ? customerAddress : "Not Required",
                customerId: customerId
            };

            console.log("📢 Sending order data:", JSON.stringify(orderData));

            fetch("http://localhost:8080/SYOS-Server/api/checkout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(orderData)
            })
                .then(response => response.json())
                .then(data => {
                    localStorage.setItem("billData", JSON.stringify(orderData));
                    window.location.href = "bill.jsp";
                })
                .catch(error => {
                    console.error("🚨 Error during checkout:", error);
                    localStorage.setItem("billData", JSON.stringify(orderData));
                    window.location.href = "bill.jsp";
                });
        }
    </script>
</head>
<body>

<!-- Header Section (Navbar) -->
<header class="customer-navbar">
    <div class="customer-navbar-content">
        <h2>Checkout</h2>
        <p>Welcome, <span id="customer-name"></span></p>
    </div>
    <a href="cart.jsp" class="cart-link">🛒 Back to Cart</a>
</header>

<!-- Main Content -->
<main class="checkout-container">
    <h2>Order Summary</h2>
    <div id="checkout-items" class="checkout-items-list"></div>

    <div class="checkout-summary">
        <h3>Total: <span id="total-price">LKR0.00</span></h3>

        <h4>Select Payment Method</h4>
        <label><input type="radio" name="payment-method" value="credit-card" onchange="toggleAddressField()"> Credit Card</label>
        <label><input type="radio" name="payment-method" value="cash-on-delivery" onchange="toggleAddressField()"> Cash On Delivery</label>

        <div id="address-section">
            <h4>Delivery Address</h4>
            <textarea id="customer-address" placeholder="Enter your delivery address"></textarea>
        </div>

        <button onclick="processCheckout(event)">Confirm & Pay</button>
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


