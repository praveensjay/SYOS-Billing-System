package com.syos.service;

import com.syos.model.Stock;
import java.util.List;

public interface StockManager {
    void addStock(Stock stock);
    Stock findByBatchCode(String batchCode);
    List<Stock> findAllStock();
    void updateStock(Stock stock);
    void deleteStock(String batchCode);
}
