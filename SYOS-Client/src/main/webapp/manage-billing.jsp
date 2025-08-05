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
    <title>Manage Billing - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/manage-billing.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            checkAdminAccess();
        });

        function checkAdminAccess() {
            var role = sessionStorage.getItem("role");

            var adminName = sessionStorage.getItem("name") || "Admin";
            document.getElementById("admin-name").textContent = adminName;
        }

        function displayMessage(message, isError = false) {
            var messageBox = document.getElementById("messageBox");
            messageBox.textContent = message;
            messageBox.style.color = isError ? "red" : "green";
            messageBox.style.display = "block";
            setTimeout(function() { messageBox.style.display = "none"; }, 3000);
        }

        function createBill(event) {
            event.preventDefault();
            var customerName = document.getElementById("customerName").value;
            var customerPhone = document.getElementById("customerPhone").value;
            var customerEmail = document.getElementById("customerEmail").value;

            if (!customerName || !customerPhone || !customerEmail) {
                displayMessage("Please enter customer details.", true);
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/billing/create", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: customerName, phoneNumber: customerPhone, email: customerEmail })
            })
                .then(response => response.json())
                .then(bill => {
                    localStorage.setItem("billId", bill.billId);
                    showBillDetails(bill);
                    displayMessage("Bill created successfully!");
                    document.getElementById("addItemSection").style.display = "block";
                })
                .catch(error => {
                    console.error("Error creating bill:", error);
                    displayMessage("An error occurred while creating the bill.", true);
                });
        }

        function showBillDetails(bill) {
            document.getElementById("billSection").style.display = "block";
            document.getElementById("billId").textContent = bill.billId;
            document.getElementById("billDate").textContent = bill.billDate;
            document.getElementById("billTotal").textContent = bill.totalPrice;
            document.getElementById("billDiscount").textContent = bill.discountAmount;
            document.getElementById("billFinalPrice").textContent = bill.finalPrice;
        }

        function addItemToBill(event) {
            event.preventDefault();
            var billId = localStorage.getItem("billId");
            var itemCode = document.getElementById("itemCode").value;
            var quantity = parseInt(document.getElementById("itemQuantity").value);

            if (!itemCode || isNaN(quantity)) {
                displayMessage("Please enter valid item details.", true);
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/billing/addItem/" + billId + "?quantity=" + quantity, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ itemCode: itemCode })
            })
                .then(response => response.json())
                .then(updatedBill => {
                    showBillDetails(updatedBill);
                    displayMessage("Item added successfully!");
                    document.getElementById("itemCode").value = "";
                    document.getElementById("itemQuantity").value = "";
                })
                .catch(error => {
                    console.error("Error adding item:", error);
                    displayMessage("An error occurred while adding the item.", true);
                });
        }

        function applyDiscount(event) {
            event.preventDefault();
            var billId = localStorage.getItem("billId");
            var discountRate = parseFloat(document.getElementById("discountRate").value);

            if (isNaN(discountRate)) {
                displayMessage("Please enter a valid discount rate.", true);
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/billing/applyDiscount/" + billId + "?discountRate=" + discountRate, {
                method: "POST"
            })
                .then(response => response.json())
                .then(updatedBill => {
                    showBillDetails(updatedBill);
                    displayMessage("Discount applied successfully!");
                })
                .catch(error => {
                    console.error("Error applying discount:", error);
                    displayMessage("An error occurred while applying the discount.", true);
                });
        }

        function finalizeBill(event) {
            event.preventDefault();
            var billId = localStorage.getItem("billId");
            var cashTendered = parseFloat(document.getElementById("cashTendered").value);
            var useLoyalty = document.getElementById("useLoyalty").checked;

            if (isNaN(cashTendered)) {
                displayMessage("Please enter a valid cash amount.", true);
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/billing/finalize/" + billId + "?cash=" + cashTendered + "&useLoyalty=" + useLoyalty, {
                method: "POST"
            })
                .then(response => response.json())
                .then(finalizedBill => {
                    showBillDetails(finalizedBill);
                    displayMessage("Bill finalized successfully!");
                    localStorage.removeItem("billId");
                })
                .catch(error => {
                    console.error("Error finalizing bill:", error);
                    displayMessage("An error occurred while finalizing the bill.", true);
                });
        }

        function logout() {
            sessionStorage.clear();
            localStorage.removeItem("billId");
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<header>
    <h1>Manage Billing</h1>
    <div class="user-info">
        <span id="admin-name"></span>
        <button onclick="logout()">Logout</button>
    </div>
</header>

<main class="container">
    <a href="admin-dashboard.jsp" class="back-btn">⬅️ Back to Dashboard</a>

    <div id="messageBox" style="display: none; padding: 10px; margin: 10px 0; border-radius: 5px; font-weight: bold;"></div>

    <h3>Create New Bill</h3>
    <form onsubmit="createBill(event)">
        <input type="text" id="customerName" placeholder="Customer Name" required>
        <input type="text" id="customerPhone" placeholder="Phone Number" required>
        <input type="email" id="customerEmail" placeholder="Email" required>
        <button type="submit">Create Bill</button>
    </form>

    <div id="billSection" style="display:none;">
        <h3>Bill Details</h3>
        <p>Bill ID: <span id="billId"></span></p>
        <p>Date: <span id="billDate"></span></p>
        <p>Total: <span id="billTotal"></span></p>
        <p>Discount: <span id="billDiscount"></span></p>
        <p>Final Price: <span id="billFinalPrice"></span></p>

        <div id="addItemSection" style="display:none;">
            <h4>Add Item</h4>
            <form onsubmit="addItemToBill(event)">
                <input type="text" id="itemCode" placeholder="Item Code" required>
                <input type="number" id="itemQuantity" placeholder="Quantity" required>
                <button type="submit">Add Item</button>
            </form>
        </div>

        <h4>Finalize Bill</h4>
        <form onsubmit="finalizeBill(event)">
            <input type="number" id="cashTendered" placeholder="Cash Tendered" required>
            <label><input type="checkbox" id="useLoyalty"> Use Loyalty Points</label>
            <button type="submit">Finalize</button>
        </form>
    </div>
</main>
</body>
</html>



