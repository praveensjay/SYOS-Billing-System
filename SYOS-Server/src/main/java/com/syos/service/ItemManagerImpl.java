package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.ItemDAO;
import com.syos.dao.ItemDAOImpl;
import com.syos.model.Item;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ItemManagerImpl implements ItemManager {

    private final ItemDAO itemRepository;
    private final ReentrantLock lock = new ReentrantLock(true);

    public ItemManagerImpl() {
        this.itemRepository = new ItemDAOImpl();
    }

    @Override
    public void addItem(Item item) {
        lock.lock();
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            itemRepository.save(item, connection);
            System.out.println("✅ Item added: " + item.getItemName());
        } catch (Exception e) {
            System.err.println("❌ Error adding item: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public Item findById(int itemId) { // ✅ Implemented this method
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            return itemRepository.findById(itemId, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding item by ID: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public Item findByCode(String itemCode) {
        lock.lock();
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            return itemRepository.findByCode(itemCode, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding item: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
        return null;
    }

    @Override
    public void updateItem(Item item) {
        lock.lock();
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            itemRepository.update(item, connection);
            System.out.println("✅ Item updated: " + item.getItemName());
        } catch (Exception e) {
            System.err.println("❌ Error updating item: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public void removeItem(String itemCode) {
        lock.lock();
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            itemRepository.delete(itemCode, connection);
            System.out.println("✅ Item removed: " + itemCode);
        } catch (Exception e) {
            System.err.println("❌ Error removing item: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

    @Override
    public List<Item> getAllItems() {
        lock.lock();
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnection();
            return itemRepository.findAll(connection);
        } catch (Exception e) {
            System.err.println("❌ Error retrieving all items: " + e.getMessage());
        } finally {
            lock.unlock();
            if (connection != null) {
                ConnectionPool.releaseConnection(connection);
            }
        }
        return null;
    }
}
