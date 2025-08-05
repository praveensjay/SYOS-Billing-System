package com.syos.dao;

import com.syos.model.Stock;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface ReportingDAO {
    List<Object[]> getTotalSalesReport(LocalDate date, Connection connection);
    List<Stock> getReshelvingReport(LocalDate date, Connection connection);
    List<Stock> getReorderLevelReport(LocalDate date, Connection connection);
    List<Stock> getStockReport(LocalDate date, Connection connection);
    List<Object[]> getBillReport(LocalDate date, Connection connection);
    List<Object[]> getMostSoldCategories(LocalDate date, Connection connection);
}
