package com.syos.dao;

import com.syos.model.Stock;
import java.sql.Connection;
import java.util.List;

public interface StockDAO {
    void save(Stock stock, Connection connection);
    Stock findByBatchCode(String batchCode, Connection connection);
    List<Stock> findAll(Connection connection);
    void update(Stock stock, Connection connection);
    void delete(String batchCode, Connection connection);
}
