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
    <title>Manage Customers - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/manage-customers.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            checkAdminAccess();
            fetchCustomersFromDB();  // Fetch customers from API
        });

        function checkAdminAccess() {
            var role = sessionStorage.getItem("role");

            var adminName = sessionStorage.getItem("name") || "Admin";
            document.getElementById("admin-name").textContent = adminName;
        }

        function fetchCustomersFromDB() {
            fetch("http://localhost:8080/SYOS-Server/api/customers")
                .then(function(response) {
                    if (!response.ok) {
                        // If response is not OK, reject the promise and log the error
                        return Promise.reject("Error: " + response.statusText);
                    }
                    return response.json(); // Parse the JSON if successful
                })
                .then(function(customers) {
                    if (Array.isArray(customers) && customers.length > 0) {
                        localStorage.setItem("customers", JSON.stringify(customers));
                        displayAllCustomers(customers);
                    } else {
                        console.warn("No customers found from API.");
                        document.getElementById("error-message").innerText = "No customers found.";
                    }
                })
                .catch(function(error) {
                    console.error("Error fetching customers from API:", error);
                    // You could display an error message to the user
                    document.getElementById("error-message").innerText = "Error fetching customers. Please try again later.";
                });
        }


        function displayAllCustomers(customers) {
            var tableBody = document.getElementById("customers-table-body");
            tableBody.innerHTML = "";

            customers.forEach(function(customer) {
                var row = document.createElement("tr");

                var idCell = document.createElement("td");
                var nameCell = document.createElement("td");
                var phoneCell = document.createElement("td");
                var emailCell = document.createElement("td");
                var actionCell = document.createElement("td");

                idCell.textContent = customer.customerId;
                nameCell.textContent = customer.name;
                phoneCell.textContent = customer.phoneNumber;
                emailCell.textContent = customer.email;

                var editButton = document.createElement("button");
                editButton.className = "edit-btn";
                editButton.textContent = "✏️ Edit";
                editButton.onclick = function () {
                    editCustomer(customer.customerId, customer.name, customer.phoneNumber, customer.email);
                };

                var deleteButton = document.createElement("button");
                deleteButton.className = "delete-btn";
                deleteButton.textContent = "❌ Delete";
                deleteButton.onclick = function () {
                    deleteCustomer(customer.customerId);
                };

                actionCell.appendChild(editButton);
                actionCell.appendChild(deleteButton);

                row.appendChild(idCell);
                row.appendChild(nameCell);
                row.appendChild(phoneCell);
                row.appendChild(emailCell);
                row.appendChild(actionCell);

                tableBody.appendChild(row);
            });
        }

        function deleteCustomer(customerId) {
            if (!confirm("Are you sure you want to delete this customer?")) return;

            fetch("http://localhost:8080/SYOS-Server/api/customers/" + customerId, {
                method: "DELETE"
            })
                .then(function(response) { return response.text(); })
                .then(function(message) {
                    console.log("Delete response:", message);
                    var customers = JSON.parse(localStorage.getItem("customers")) || [];
                    customers = customers.filter(function(customer) {
                        return customer.customerId !== customerId;
                    });
                    localStorage.setItem("customers", JSON.stringify(customers));
                    displayAllCustomers(customers);
                })
                .catch(function(error) {
                    console.error("Error deleting customer:", error);
                });
        }

        function editCustomer(customerId, name, phoneNumber, email) {
            document.getElementById("updateCustomerId").value = customerId;
            document.getElementById("updateCustomerName").value = name;
            document.getElementById("updateCustomerPhone").value = phoneNumber;
            document.getElementById("updateCustomerEmail").value = email;
            document.getElementById("editForm").style.display = "block";
        }

        function addCustomer(event) {
            event.preventDefault();
            var name = document.getElementById("customerName").value;
            var phoneNumber = document.getElementById("customerPhone").value;
            var email = document.getElementById("customerEmail").value;

            fetch("http://localhost:8080/SYOS-Server/api/customers", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ name: name, phoneNumber: phoneNumber, email: email })
            })
                .then(function(response) { return response.text(); })
                .then(function(message) {
                    console.log("Add response:", message);
                    fetchCustomersFromDB(); // Refresh data from API
                })
                .catch(function(error) {
                    console.error("Error adding customer:", error);
                });
        }

        function updateCustomer(event) {
            event.preventDefault();
            var customerId = parseInt(document.getElementById("updateCustomerId").value);
            var name = document.getElementById("updateCustomerName").value;
            var phoneNumber = document.getElementById("updateCustomerPhone").value;
            var email = document.getElementById("updateCustomerEmail").value;

            fetch("http://localhost:8080/SYOS-Server/api/customers/" + customerId, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ customerId: customerId, name: name, phoneNumber: phoneNumber, email: email })
            })
                .then(function(response) { return response.text(); })
                .then(function(message) {
                    console.log("Update response:", message);
                    fetchCustomersFromDB(); // Refresh data from API
                    document.getElementById("editForm").style.display = "none";
                })
                .catch(function(error) {
                    console.error("Error updating customer:", error);
                });
        }

        function logout() {
            sessionStorage.clear();
            localStorage.removeItem("customers");
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<header>
    <h1>Manage Customers</h1>
    <div class="user-info">
        <span id="admin-name"></span>
        <button onclick="logout()">Logout</button>
    </div>
</header>

<main class="container">
    <a href="admin-dashboard.jsp" class="back-btn">⬅️ Back to Dashboard</a>

    <h3>Add New Customer</h3>
    <form onsubmit="addCustomer(event)">
        <input type="text" id="customerName" placeholder="Customer Name" required>
        <input type="text" id="customerPhone" placeholder="Phone Number" required>
        <input type="email" id="customerEmail" placeholder="Email" required>
        <button type="submit">Add Customer</button>
    </form>

    <h3>Edit Customer</h3>
    <form id="editForm" style="display:none;" onsubmit="updateCustomer(event)">
        <input type="hidden" id="updateCustomerId">
        <input type="text" id="updateCustomerName" required>
        <input type="text" id="updateCustomerPhone" required>
        <input type="email" id="updateCustomerEmail" required>
        <button type="submit">Update Customer</button>
    </form>

    <h2>Customers List</h2>
    <div id="customers-container" class="manage-container">
        <table>
            <thead>
            <tr>
                <th>Customer ID</th>
                <th>Name</th>
                <th>Phone</th>
                <th>Email</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="customers-table-body"></tbody>
        </table>
    </div>
</main>
</body>
</html>

