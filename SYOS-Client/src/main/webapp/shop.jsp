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
    <title>Shop - Customer</title>
    <link rel="stylesheet" type="text/css" href="assets/styles/shop.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            loadItemsFromStorage();
            updateCartCount();
        });

        function loadItemsFromStorage() {
            var items = JSON.parse(localStorage.getItem("items")) || [];
            if (items.length > 0) {
                displayAllItems(items);
            } else {
                fetchItemsFromDB();
            }
        }

        function fetchItemsFromDB() {
            fetch("http://localhost:8080/SYOS-Server/api/items")
                .then(response => response.json())
                .then(items => {
                    if (Array.isArray(items) && items.length > 0) {
                        localStorage.setItem("items", JSON.stringify(items));
                        displayAllItems(items);
                    } else {
                        console.warn("No items found from API.");
                    }
                })
                .catch(error => console.error("Error fetching items:", error));
        }

        function displayAllItems(items) {
            var itemsContainer = document.getElementById("shop-items");
            itemsContainer.innerHTML = "";

            items.forEach(function(item) {
                var itemCard = document.createElement("div");
                itemCard.classList.add("item-card");

                var itemName = document.createElement("h3");
                itemName.textContent = item.itemName;

                var itemCode = document.createElement("p");
                itemCode.textContent = "Code: " + item.itemCode;

                var itemPrice = document.createElement("p");
                itemPrice.textContent = "Price: LKR" + item.itemPrice.toFixed(2);

                var addToCartButton = document.createElement("button");
                addToCartButton.textContent = "Add to Cart";
                addToCartButton.onclick = function () {
                    addToCart(item.itemCode, item.itemName, item.itemPrice);
                };

                itemCard.appendChild(itemName);
                itemCard.appendChild(itemCode);
                itemCard.appendChild(itemPrice);
                itemCard.appendChild(addToCartButton);

                itemsContainer.appendChild(itemCard);
            });
        }

        function addToCart(itemCode, itemName, itemPrice) {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            var existingItem = cart.find(item => item.itemCode === itemCode);

            if (existingItem) {
                existingItem.quantity += 1;
            } else {
                cart.push({ itemCode: itemCode, itemName: itemName, itemPrice: itemPrice, quantity: 1 });
            }

            localStorage.setItem("cart", JSON.stringify(cart));
            updateCartCount();
        }

        function updateCartCount() {
            var cart = JSON.parse(localStorage.getItem("cart")) || [];
            document.getElementById("cart-count").textContent = cart.length;
        }
    </script>
</head>
<body>

<header class="customer-navbar">
    <div class="customer-navbar-content">
        <h2>Shop</h2>
        <p>Welcome, <span id="customer-name"></span></p>
    </div>
    <div class="nav-links">
        <a href="cart.jsp" class="cart-link">🛒 View Cart (<span id="cart-count">0</span>)</a>
        <a href="customer-dashboard.jsp" class="dashboard-link">🏠 Back to Dashboard</a>
    </div>
</header>

<main class="shop-container">
    <h2>Available Items</h2>
    <div id="shop-items" class="items-grid"></div>
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
