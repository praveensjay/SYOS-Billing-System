package com.syos.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportingManager {
    List<Map<String, Object>> generateTotalSalesReport(LocalDate date);
    List<Map<String, Object>> generateReshelvingReport(LocalDate date);
    List<Map<String, Object>> generateReorderLevelReport(LocalDate date);
    List<Map<String, Object>> generateStockReport(LocalDate date);
    List<Map<String, Object>> generateBillReport(LocalDate date);
}
