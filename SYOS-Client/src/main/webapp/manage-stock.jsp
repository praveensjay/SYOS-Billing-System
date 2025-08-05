<%--
  Created by IntelliJ IDEA.
  User: praveen
  Date: 2025-04-04
  Time: 23:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="javax.servlet.http.HttpSession" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Stocks - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/manage-stock.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            checkAdminAccess();
            fetchStocksFromDB();  // Fetch stocks from API
        });

        function checkAdminAccess() {
            var role = sessionStorage.getItem("role");

            var adminName = sessionStorage.getItem("name") || "Admin";
            document.getElementById("admin-name").textContent = adminName;
        }

        function fetchStocksFromDB() {
            fetch("http://localhost:8080/SYOS-Server/api/stock/all")
                .then(function(response) { return response.json(); })
                .then(function(stocks) {
                    if (Array.isArray(stocks) && stocks.length > 0) {
                        localStorage.setItem("stocks", JSON.stringify(stocks));
                        displayAllStocks(stocks);
                    } else {
                        console.warn("No stocks found from API.");
                    }
                })
                .catch(function(error) {
                    console.error("Error fetching stocks from API:", error);
                });
        }

        function displayAllStocks(stocks) {
            var tableBody = document.getElementById("stocks-table-body");
            tableBody.innerHTML = "";

            stocks.forEach(function(stock) {
                var row = document.createElement("tr");

                var batchCodeCell = document.createElement("td");
                var itemCodeCell = document.createElement("td");
                var quantityCell = document.createElement("td");
                var shelfCapacityCell = document.createElement("td");
                var purchaseDateCell = document.createElement("td");
                var expiryDateCell = document.createElement("td");
                var locationCell = document.createElement("td");
                var actionCell = document.createElement("td");

                batchCodeCell.textContent = stock.batchCode;
                itemCodeCell.textContent = stock.itemCode;
                quantityCell.textContent = stock.quantityInStock;
                shelfCapacityCell.textContent = stock.shelfCapacity;
                purchaseDateCell.textContent = stock.purchaseDate;
                expiryDateCell.textContent = stock.expiryDate;
                locationCell.textContent = stock.stockLocation;

                var editButton = document.createElement("button");
                editButton.className = "edit-btn";
                editButton.textContent = "✏️ Edit";
                editButton.onclick = function () {
                    editStock(stock.batchCode, stock.itemCode, stock.quantityInStock, stock.shelfCapacity, stock.purchaseDate, stock.expiryDate, stock.stockLocation);
                };

                var deleteButton = document.createElement("button");
                deleteButton.className = "delete-btn";
                deleteButton.textContent = "❌ Delete";
                deleteButton.onclick = function () {
                    deleteStock(stock.batchCode);
                };

                actionCell.appendChild(editButton);
                actionCell.appendChild(deleteButton);

                row.appendChild(batchCodeCell);
                row.appendChild(itemCodeCell);
                row.appendChild(quantityCell);
                row.appendChild(shelfCapacityCell);
                row.appendChild(purchaseDateCell);
                row.appendChild(expiryDateCell);
                row.appendChild(locationCell);
                row.appendChild(actionCell);

                tableBody.appendChild(row);
            });
        }

        function deleteStock(batchCode) {
            if (!confirm("Are you sure you want to delete this stock?")) return;

            fetch("http://localhost:8080/SYOS-Server/api/stock/" + batchCode, {
                method: "DELETE"
            })
                .then(function(response) { return response.text(); })
                .then(function(message) {
                    console.log("Delete response:", message);
                    var stocks = JSON.parse(localStorage.getItem("stocks")) || [];
                    stocks = stocks.filter(function(stock) {
                        return stock.batchCode !== batchCode;
                    });
                    localStorage.setItem("stocks", JSON.stringify(stocks));
                    displayAllStocks(stocks);
                })
                .catch(function(error) {
                    console.error("Error deleting stock:", error);
                });
        }

        function editStock(batchCode, itemCode, quantity, shelfCapacity, purchaseDate, expiryDate, location) {
            document.getElementById("updateBatchCode").value = batchCode;
            document.getElementById("updateItemCode").value = itemCode;
            document.getElementById("updateQuantity").value = quantity;
            document.getElementById("updateShelfCapacity").value = shelfCapacity;
            document.getElementById("updatePurchaseDate").value = purchaseDate;
            document.getElementById("updateExpiryDate").value = expiryDate;
            document.getElementById("updateStockLocation").value = location;
            document.getElementById("editForm").style.display = "block";
        }

        function addStock(event) {
            event.preventDefault();
            var batchCode = document.getElementById("batchCode").value;
            var itemCode = document.getElementById("itemCode").value;
            var quantity = parseInt(document.getElementById("quantityInStock").value);
            var shelfCapacity = parseInt(document.getElementById("shelfCapacity").value);
            var purchaseDate = document.getElementById("purchaseDate").value;
            var expiryDate = document.getElementById("expiryDate").value;
            var location = document.getElementById("stockLocation").value;

            fetch("http://localhost:8080/SYOS-Server/api/stock/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ batchCode: batchCode, itemCode: itemCode, quantityInStock: quantity, shelfCapacity: shelfCapacity, purchaseDate: purchaseDate, expiryDate: expiryDate, stockLocation: location })
            })
                .then(function(response) { return response.text(); })
                .then(function(message) {
                    console.log("Add response:", message);
                    fetchStocksFromDB(); // Refresh data from API
                })
                .catch(function(error) {
                    console.error("Error adding stock:", error);
                });
        }

        function logout() {
            sessionStorage.clear();
            localStorage.removeItem("stocks");
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<header>
    <h1>Manage Stocks</h1>
    <div class="user-info">
        <span id="admin-name"></span>
        <button onclick="logout()">Logout</button>
    </div>
</header>

<main class="container">
    <a href="admin-dashboard.jsp" class="back-btn">⬅️ Back to Dashboard</a>

    <h3>Add New Stock</h3>
    <form onsubmit="addStock(event)">
        <input type="text" id="batchCode" placeholder="Batch Code" required>
        <input type="text" id="itemCode" placeholder="Item Code" required>
        <input type="number" id="quantityInStock" placeholder="Quantity in Stock" required>
        <input type="number" id="shelfCapacity" placeholder="Shelf Capacity" required>
        <input type="date" id="purchaseDate" required>
        <input type="date" id="expiryDate" required>
        <input type="text" id="stockLocation" placeholder="Stock Location" required>
        <button type="submit">Add Stock</button>
    </form>

    <h3>Edit Stock</h3>
    <form id="editForm" style="display:none;" onsubmit="updateStock(event)">
        <input type="hidden" id="updateBatchCode">
        <input type="text" id="updateItemCode" required>
        <input type="number" id="updateQuantity" required>
        <input type="number" id="updateShelfCapacity" required>
        <input type="date" id="updatePurchaseDate" required>
        <input type="date" id="updateExpiryDate" required>
        <input type="text" id="updateStockLocation" required>
        <button type="submit">Update Stock</button>
    </form>

    <h2>Stocks List</h2>
    <div id="stocks-container" class="manage-container">
        <table>
            <thead>
            <tr>
                <th>Batch Code</th>
                <th>Item Code</th>
                <th>Quantity</th>
                <th>Shelf Capacity</th>
                <th>Purchase Date</th>
                <th>Expiry Date</th>
                <th>Stock Location</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="stocks-table-body"></tbody>
        </table>
    </div>
</main>
</body>
</html>

