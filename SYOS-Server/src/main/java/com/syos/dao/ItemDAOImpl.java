package com.syos.dao;

import com.syos.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public void save(Item item, Connection connection) {
        String sql = "INSERT INTO item (item_code, item_name, item_price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getItemCode());
            statement.setString(2, item.getItemName());
            statement.setBigDecimal(3, item.getItemPrice());
            statement.executeUpdate();
            System.out.println("✅ Item inserted successfully: " + item.getItemName());
        } catch (SQLException e) {
            System.err.println("❌ Error saving item: " + e.getMessage());
        }
    }

    @Override
    public Item findByCode(String itemCode, Connection connection) {
        String sql = "SELECT * FROM item WHERE item_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemCode);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding item by code: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void update(Item item, Connection connection) {
        String sql = "UPDATE item SET item_name = ?, item_price = ? WHERE item_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getItemName());
            statement.setBigDecimal(2, item.getItemPrice());
            statement.setString(3, item.getItemCode());
            statement.executeUpdate();
            System.out.println("✅ Item updated successfully: " + item.getItemName());
        } catch (SQLException e) {
            System.err.println("❌ Error updating item: " + e.getMessage());
        }
    }

    @Override
    public void delete(String itemCode, Connection connection) {
        String sql = "DELETE FROM item WHERE item_code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemCode);
            statement.executeUpdate();
            System.out.println("✅ Item deleted successfully: " + itemCode);
        } catch (SQLException e) {
            System.err.println("❌ Error deleting item: " + e.getMessage());
        }
    }

    @Override
    public List<Item> findAll(Connection connection) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM item";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                items.add(mapItem(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving items: " + e.getMessage());
        }
        return items;
    }

    @Override
    public Item findById(int itemId, Connection connection) {
        String sql = "SELECT * FROM item WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return mapItem(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding item by ID: " + e.getMessage());
        }
        return null;
    }

    private Item mapItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setItemId(rs.getInt("item_id"));
        item.setItemCode(rs.getString("item_code"));
        item.setItemName(rs.getString("item_name"));
        item.setItemPrice(rs.getBigDecimal("item_price"));
        return item;
    }
}
