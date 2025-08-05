package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.ReportingDAO;
import com.syos.dao.ReportingDAOImpl;
import com.syos.model.Stock;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ReportingManagerImpl implements ReportingManager {

    private final ReportingDAO reportingRepository;
    private final ReentrantLock lock = new ReentrantLock(true);

    public ReportingManagerImpl() {
        this.reportingRepository = new ReportingDAOImpl();
    }

    @Override
    public List<Map<String, Object>> generateTotalSalesReport(LocalDate date) {
        lock.lock();
        Connection connection = null;
        List<Map<String, Object>> reportData = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnection();

            List<Object[]> salesReport = reportingRepository.getTotalSalesReport(date, connection);
            BigDecimal totalSalesToday = salesReport.stream()
                    .map(row -> (BigDecimal) row[2])
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            for (Object[] row : salesReport) {
                Map<String, Object> data = new HashMap<>();
                data.put("itemCode", row[0]);
                data.put("quantitySold", row[1]);
                data.put("totalPrice", row[2]);
                reportData.add(data);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("date", date);
            summary.put("totalSales", totalSalesToday);
            reportData.add(summary);

        } catch (Exception e) {
            System.err.println("❌ Error generating sales report: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) ConnectionPool.releaseConnection(connection);
        }

        return reportData;
    }

    @Override
    public List<Map<String, Object>> generateReshelvingReport(LocalDate date) {
        lock.lock();
        Connection connection = null;
        List<Map<String, Object>> reportData = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnection();
            List<Stock> reshelvingReport = reportingRepository.getReshelvingReport(date, connection);

            for (Stock stock : reshelvingReport) {
                Map<String, Object> data = new HashMap<>();
                data.put("itemCode", stock.getItemCode());
                data.put("quantityInStock", stock.getQuantityInStock());
                data.put("reshelfQuantity", stock.getReshelfQuantity());
                data.put("shelfCapacity", stock.getShelfCapacity());
                data.put("batchCode", stock.getBatchCode());
                data.put("expiryDate", stock.getExpiryDate());
                reportData.add(data);
            }

        } catch (Exception e) {
            System.err.println("❌ Error generating reshelving report: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) ConnectionPool.releaseConnection(connection);
        }

        return reportData;
    }

    @Override
    public List<Map<String, Object>> generateReorderLevelReport(LocalDate date) {
        lock.lock();
        Connection connection = null;
        List<Map<String, Object>> reportData = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnection();
            List<Stock> reorderReport = reportingRepository.getReorderLevelReport(date, connection);

            for (Stock stock : reorderReport) {
                Map<String, Object> data = new HashMap<>();
                data.put("itemCode", stock.getItemCode());
                data.put("quantityInStock", stock.getQuantityInStock());
                data.put("reorderLevel", stock.getReorderLevel());
                data.put("batchCode", stock.getBatchCode());
                reportData.add(data);
            }

        } catch (Exception e) {
            System.err.println("❌ Error generating reorder level report: " + e.getMessage());

        } finally {
            lock.unlock();
            if (connection != null) ConnectionPool.releaseConnection(connection);
        }

        return reportData;
    }

    @Override
    public List<Map<String, Object>> generateStockReport(LocalDate date) {
        lock.lock();
        Connection connection = null;
        List<Map<String, Object>> reportData = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnection();
            List<Stock> stockReport = reportingRepository.getStockReport(date, connection);

            for (Stock stock : stockReport) {
                Map<String, Object> data = new HashMap<>();
                data.put("itemCode", stock.getItemCode());
                data.put("quantityInStock", stock.getQuantityInStock());
                data.put("dateOfPurchase", stock.getDateOfPurchase());
                data.put("expiryDate", stock.getExpiryDate());
                data.put("batchCode", stock.getBatchCode());
                reportData.add(data);
            }

        } catch (Exception e) {
            System.err.println("❌ Error generating stock report: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) ConnectionPool.releaseConnection(connection);
        }

        return reportData;
    }

    @Override
    public List<Map<String, Object>> generateBillReport(LocalDate date) {
        lock.lock();
        Connection connection = null;
        List<Map<String, Object>> reportData = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnection();
            List<Object[]> billReport = reportingRepository.getBillReport(date, connection);
            BigDecimal totalRevenue = BigDecimal.ZERO;
            int totalBills = billReport.size();

            for (Object[] row : billReport) {
                Map<String, Object> data = new HashMap<>();
                data.put("billId", row[0]);
                data.put("customerId", row[1]);
                data.put("totalPrice", row[2]);
                totalRevenue = totalRevenue.add((BigDecimal) row[2]);
                reportData.add(data);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalBills", totalBills);
            summary.put("totalRevenue", totalRevenue);
            if (totalBills > 0) {
                summary.put("avgTransactionValue", totalRevenue.divide(BigDecimal.valueOf(totalBills), BigDecimal.ROUND_HALF_UP));
            }
            reportData.add(summary);

        } catch (Exception e) {
            System.err.println("❌ Error generating bill report: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) ConnectionPool.releaseConnection(connection);
        }

        return reportData;
    }
}
