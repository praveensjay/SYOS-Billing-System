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
    <title>Order Receipt</title>
    <link rel="stylesheet" type="text/css" href="assets/styles/bill.css">
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            var billData = JSON.parse(localStorage.getItem("billData")) || {};
            var billContainer = document.getElementById("bill-container");

            if (!billData.items || billData.items.length === 0) {
                billContainer.innerHTML = "<h2>No order details available.</h2>";
                return;
            }

            var today = new Date().toLocaleDateString();
            var billHTML = `
                <h2>🧾 Order Receipt</h2>
                <p><strong>Date:</strong> ` + today + `</p>
                <p><strong>Customer Name:</strong> ` + sessionStorage.getItem("name") + `</p>
                <p><strong>Payment Method:</strong> ` + billData.paymentMethod + `</p>
                <p><strong>Address:</strong> ` + billData.address + `</p>
                <hr>
                <h3>Items Purchased:</h3>
                <ul>
            `;

            billData.items.forEach(item => {
                billHTML += `<li>` + item.itemName + ` - ` + item.quantity + ` x LKR` + item.itemPrice + `</li>`;
            });

            billHTML += `</ul><hr><h3>Total: LKR` + billData.total + `</h3>`;

            billHTML += `
                <div class="bill-actions">
                    <button onclick="printBill()">🖨️ Print Bill</button>
                    <button onclick="goToDashboard()">🏠 Back to Dashboard</button>
                </div>
            `;

            billContainer.innerHTML = billHTML;
        });

        function printBill() {
            window.print();
            localStorage.removeItem("billData"); // ✅ Clear stored bill data after printing
        }

        function goToDashboard() {
            window.location.href = "customer-dashboard.jsp";
        }
    </script>
</head>
<body>
<div id="bill-container"></div>
</body>
</html>
