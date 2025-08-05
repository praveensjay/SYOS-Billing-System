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
    <title>Manage Items - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/manage-items.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            checkAdminAccess();
            fetchItemsFromDB();  // Fetch items from the API
        });

        function checkAdminAccess() {
            var role = sessionStorage.getItem("role");

            var adminName = sessionStorage.getItem("name") || "Admin";
            document.getElementById("admin-name").textContent = adminName;
        }

        function fetchItemsFromDB() {
            fetch("http://localhost:8080/SYOS-Server/api/items")
                .then(function(response) { return response.json(); })
                .then(function(items) {
                    if (Array.isArray(items) && items.length > 0) {
                        localStorage.setItem("items", JSON.stringify(items));
                        displayAllItems(items);
                    } else {
                        console.warn("No items found from API.");
                    }
                })
                .catch(function(error) {
                    console.error("Error fetching items from API:", error);
                });
        }

        function displayAllItems(items) {
            var tableBody = document.getElementById("items-table-body");
            tableBody.innerHTML = "";

            items.forEach(function(item) {
                var row = document.createElement("tr");

                var itemCodeCell = document.createElement("td");
                var itemNameCell = document.createElement("td");
                var itemPriceCell = document.createElement("td");
                var actionCell = document.createElement("td");

                itemCodeCell.textContent = item.itemCode;
                itemNameCell.textContent = item.itemName;
                itemPriceCell.textContent = "LKR" + item.itemPrice;

                var editButton = document.createElement("button");
                editButton.className = "edit-btn";
                editButton.textContent = "✏️ Edit";
                editButton.onclick = function () {
                    editItem(item.itemId, item.itemCode, item.itemName, item.itemPrice);
                };

                var deleteButton = document.createElement("button");
                deleteButton.className = "delete-btn";
                deleteButton.textContent = "❌ Delete";
                deleteButton.onclick = function () {
                    deleteItem(item.itemId);
                };

                actionCell.appendChild(editButton);
                actionCell.appendChild(deleteButton);

                row.appendChild(itemCodeCell);
                row.appendChild(itemNameCell);
                row.appendChild(itemPriceCell);
                row.appendChild(actionCell);

                tableBody.appendChild(row);
            });
        }

        function deleteItem(itemId) {
            if (!confirm("Are you sure you want to delete this item?")) return;

            fetch("http://localhost:8080/SYOS-Server/api/items/" + itemId, {
                method: "DELETE"
            })
                .then(function(response) {
                    return response.text(); // Handle text response instead of JSON
                })
                .then(function(message) {
                    console.log("Delete response:", message);
                    var items = JSON.parse(localStorage.getItem("items")) || [];
                    items = items.filter(function(item) {
                        return item.itemId !== itemId;
                    });
                    localStorage.setItem("items", JSON.stringify(items));
                    displayAllItems(items);
                })
                .catch(function(error) {
                    console.error("Error deleting item:", error);
                });
        }

        function editItem(itemId, itemCode, itemName, itemPrice) {
            document.getElementById("updateItemId").value = itemId;
            document.getElementById("updateItemCode").value = itemCode;
            document.getElementById("updateItemName").value = itemName;
            document.getElementById("updateItemPrice").value = itemPrice;
            document.getElementById("editForm").style.display = "block";
        }

        function addItem(event) {
            event.preventDefault();
            var itemCode = document.getElementById("itemCode").value;
            var itemName = document.getElementById("itemName").value;
            var itemPrice = parseFloat(document.getElementById("itemPrice").value);

            fetch("http://localhost:8080/SYOS-Server/api/items", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ itemCode: itemCode, itemName: itemName, itemPrice: itemPrice })
            })
                .then(function(response) { return response.text(); }) // Handle non-JSON response
                .then(function(message) {
                    console.log("Add response:", message);
                    fetchItemsFromDB(); // Refresh data from API
                })
                .catch(function(error) {
                    console.error("Error adding item:", error);
                });
        }

        function updateItem(event) {
            event.preventDefault();
            var itemId = parseInt(document.getElementById("updateItemId").value);
            var itemCode = document.getElementById("updateItemCode").value;
            var itemName = document.getElementById("updateItemName").value;
            var itemPrice = parseFloat(document.getElementById("updateItemPrice").value);

            fetch("http://localhost:8080/SYOS-Server/api/items/" + itemId, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ itemId: itemId, itemCode: itemCode, itemName: itemName, itemPrice: itemPrice })
            })
                .then(function(response) { return response.text(); }) // Handle text response
                .then(function(message) {
                    console.log("Update response:", message);
                    fetchItemsFromDB(); // Refresh data from API
                    document.getElementById("editForm").style.display = "none";
                })
                .catch(function(error) {
                    console.error("Error updating item:", error);
                });
        }

        function logout() {
            sessionStorage.clear();
            localStorage.removeItem("items");
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<header>
    <h1>Manage Items</h1>
    <div class="user-info">
        <span id="admin-name"></span>
        <button onclick="logout()">Logout</button>
    </div>
</header>

<main class="container">
    <a href="admin-dashboard.jsp" class="back-btn">⬅️ Back to Dashboard</a>

    <h3>Add New Item</h3>
    <form onsubmit="addItem(event)">
        <input type="text" id="itemCode" placeholder="Item Code" required>
        <input type="text" id="itemName" placeholder="Item Name" required>
        <input type="number" id="itemPrice" placeholder="Price" required>
        <button type="submit">Add Item</button>
    </form>

    <h3>Edit Item</h3>
    <form id="editForm" style="display:none;" onsubmit="updateItem(event)">
        <input type="hidden" id="updateItemId">
        <input type="text" id="updateItemCode" required>
        <input type="text" id="updateItemName" required>
        <input type="number" id="updateItemPrice" required>
        <button type="submit">Update Item</button>
    </form>

    <h2>Items List</h2>
    <div id="items-container" class="manage-container">
        <table>
            <thead>
            <tr>
                <th>Item Code</th>
                <th>Item Name</th>
                <th>Item Price</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="items-table-body"></tbody>
        </table>
    </div>
</main>
</body>
</html>
