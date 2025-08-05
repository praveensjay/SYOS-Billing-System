package com.syos.dao;

import com.syos.config.ConnectionPool;
import com.syos.model.StoreInventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreInventoryDAOImpl implements StoreInventoryDAO {

    @Override
    public List<StoreInventory> findByItemCodeOrderedByExpiry(String itemCode, Connection connection) {
        String sql = "SELECT * FROM store_inventory WHERE item_code = ? ORDER BY expiry_date ASC";
        List<StoreInventory> inventories = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemCode);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                inventories.add(mapStoreInventory(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving store inventory: " + e.getMessage());
            e.printStackTrace();
        }

        return inventories;
    }

    @Override
    public void update(StoreInventory storeInventory, Connection connection) {
        String sql = "UPDATE store_inventory SET quantity_in_stock = ?, expiry_date = ? WHERE stock_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, storeInventory.getQuantityInStock());
            statement.setDate(2, java.sql.Date.valueOf(storeInventory.getExpiryDate()));
            statement.setInt(3, storeInventory.getStockId());
            statement.executeUpdate();
            System.out.println("✅ Store inventory updated successfully for stock ID: " + storeInventory.getStockId());
        } catch (SQLException e) {
            System.err.println("❌ Error updating store inventory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private StoreInventory mapStoreInventory(ResultSet resultSet) throws SQLException {
        StoreInventory inventory = new StoreInventory(
                resultSet.getString("item_code"),
                resultSet.getInt("quantity_in_stock"),
                resultSet.getDate("date_of_purchase").toLocalDate(),
                resultSet.getDate("expiry_date").toLocalDate()
        );
        inventory.setStockId(resultSet.getInt("stock_id"));
        return inventory;
    }
}
