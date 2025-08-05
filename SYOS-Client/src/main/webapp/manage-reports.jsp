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
    <title>Manage Reports - Admin</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/styles/manage-reports.css">

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            checkAdminAccess();
            loadStoredReports(); // Load reports from localStorage
        });

        function checkAdminAccess() {
            var role = sessionStorage.getItem("role");

            var adminName = sessionStorage.getItem("name") || "Admin";
            document.getElementById("admin-name").textContent = adminName;
        }

        function generateReport(reportType) {
            var date = document.getElementById("reportDate").value;
            if (!date) {
                alert("Please select a date for the report.");
                return;
            }

            fetch("http://localhost:8080/SYOS-Server/api/reports/" + reportType + "?date=" + date)
                .then(function(response) { return response.json(); })
                .then(function(reportData) {
                    if (reportData && reportData.length > 0) {
                        saveReport(reportType, date, reportData);
                        displayReport(reportData);
                    } else {
                        console.warn("No data found for report.");
                        alert("No data available for the selected date.");
                    }
                })
                .catch(function(error) {
                    console.error("Error generating report:", error);
                    alert("Error generating the report. Please try again.");
                });
        }

        function saveReport(reportType, date, reportData) {
            var reports = JSON.parse(localStorage.getItem("reports")) || {};
            reports[reportType + "_" + date] = reportData;
            localStorage.setItem("reports", JSON.stringify(reports));
        }

        function loadStoredReports() {
            var reports = JSON.parse(localStorage.getItem("reports")) || {};
            if (Object.keys(reports).length > 0) {
                console.log("Loaded stored reports:", reports);
                displayReport(Object.values(reports)[0]); // Show the first stored report
            }
        }

        function displayReport(reportData) {
            var tableBody = document.getElementById("reports-table-body");
            tableBody.innerHTML = "";

            reportData.forEach(function(reportItem) {
                var row = document.createElement("tr");

                for (var key in reportItem) {
                    var cell = document.createElement("td");
                    cell.textContent = reportItem[key];
                    row.appendChild(cell);
                }

                tableBody.appendChild(row);
            });

            document.getElementById("reportContainer").style.display = "block";
        }

        function logout() {
            sessionStorage.clear();
            localStorage.removeItem("reports");
            window.location.href = "login.jsp";
        }
    </script>
</head>
<body>
<header>
    <h1>Manage Reports</h1>
    <div class="user-info">
        <span id="admin-name"></span>
        <button onclick="logout()">Logout</button>
    </div>
</header>

<main class="container">
    <a href="admin-dashboard.jsp" class="back-btn">⬅️ Back to Dashboard</a>

    <h3>Generate Report</h3>

    <div class="report-options">
        <label for="reportDate">Select Date:</label>
        <input type="date" id="reportDate" required>

        <div class="report-buttons">
            <button onclick="generateReport('sales')">Generate Total Sales Report</button>
            <button onclick="generateReport('reshelving')">Generate Reshelving Report</button>
            <button onclick="generateReport('reorder-level')">Generate Reorder Level Report</button>
            <button onclick="generateReport('stock')">Generate Stock Report</button>
            <button onclick="generateReport('bill')">Generate Bill Report</button>
        </div>
    </div>

    <h2>Report Data</h2>
    <div id="reportContainer" class="manage-container" style="display:none;">
        <table>
            <thead id="reports-table-header">
            <tr>
                <!-- Dynamic headers will be added here -->
            </tr>
            </thead>
            <tbody id="reports-table-body">
            <!-- Table rows will be dynamically added here -->
            </tbody>
        </table>
    </div>
</main>
</body>
</html>
