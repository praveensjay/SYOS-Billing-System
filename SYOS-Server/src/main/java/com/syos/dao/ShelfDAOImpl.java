package com.syos.dao;

import com.syos.config.ConnectionPool;
import com.syos.model.Shelf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShelfDAOImpl implements ShelfDAO {

    @Override
    public Shelf findByItemCode(String itemCode, Connection connection) {
        String sql = "SELECT * FROM shelf WHERE item_code = ?";
        Shelf shelf = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, itemCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                shelf = new Shelf();
                shelf.setShelfId(resultSet.getInt("shelf_id"));
                shelf.setItemCode(resultSet.getString("item_code"));
                shelf.setQuantity(resultSet.getInt("quantity"));
                shelf.setMovedDate(resultSet.getDate("moved_date").toLocalDate());
                shelf.setExpiryDate(resultSet.getDate("expiry_date").toLocalDate());
                shelf.setBatchCode(resultSet.getString("batch_code"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding shelf by item code: " + e.getMessage());
            e.printStackTrace();
        }
        return shelf;
    }

    @Override
    public void update(Shelf shelf, Connection connection) {
        String sql = "UPDATE shelf SET quantity = ?, moved_date = ?, expiry_date = ?, batch_code = ? WHERE shelf_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, shelf.getQuantity());
            statement.setDate(2, java.sql.Date.valueOf(shelf.getMovedDate()));
            statement.setDate(3, java.sql.Date.valueOf(shelf.getExpiryDate()));
            statement.setString(4, shelf.getBatchCode());
            statement.setInt(5, shelf.getShelfId());
            statement.executeUpdate();
            System.out.println("✅ Shelf updated successfully for item: " + shelf.getItemCode());
        } catch (SQLException e) {
            System.err.println("❌ Error updating shelf: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void save(Shelf shelf, Connection connection) {
        String sql = "INSERT INTO shelf (item_code, quantity, moved_date, expiry_date, batch_code) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, shelf.getItemCode());
            statement.setInt(2, shelf.getQuantity());
            statement.setDate(3, java.sql.Date.valueOf(shelf.getMovedDate()));
            statement.setDate(4, java.sql.Date.valueOf(shelf.getExpiryDate()));
            statement.setString(5, shelf.getBatchCode());
            statement.executeUpdate();
            System.out.println("✅ New shelf record inserted for item: " + shelf.getItemCode());
        } catch (SQLException e) {
            System.err.println("❌ Error saving shelf: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
