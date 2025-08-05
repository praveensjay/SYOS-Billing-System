package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.StockDAO;
import com.syos.dao.StockDAOImpl;
import com.syos.model.Stock;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class StockManagerImpl implements StockManager {

    private final StockDAO stockDAO;
    private final ReentrantLock lock = new ReentrantLock(true);

    public StockManagerImpl() {
        this.stockDAO = new StockDAOImpl();
    }

    @Override
    public void addStock(Stock stock) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            stockDAO.save(stock, connection);
            System.out.println("✅ Stock added: " + stock.getItemCode());
        } catch (Exception e) {
            System.err.println("❌ Error adding stock: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Stock findByBatchCode(String batchCode) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            return stockDAO.findByBatchCode(batchCode, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding stock: " + e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public List<Stock> findAllStock() {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            return stockDAO.findAll(connection);
        } catch (Exception e) {
            System.err.println("❌ Error retrieving all stock: " + e.getMessage());
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void updateStock(Stock stock) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            stockDAO.update(stock, connection);
            System.out.println("✅ Stock updated: " + stock.getItemCode());
        } catch (Exception e) {
            System.err.println("❌ Error updating stock: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deleteStock(String batchCode) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            stockDAO.delete(batchCode, connection);
            System.out.println("✅ Stock removed: " + batchCode);
        } catch (Exception e) {
            System.err.println("❌ Error removing stock: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}
