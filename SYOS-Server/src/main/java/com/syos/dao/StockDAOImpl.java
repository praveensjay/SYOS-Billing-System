package com.syos.dao;

import com.syos.config.ConnectionPool;
import com.syos.model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockDAOImpl implements StockDAO {

    @Override
    public void save(Stock stock, Connection connection) {
        String sql = "INSERT INTO stock (batch_code, item_code, quantity_in_stock, date_of_purchase, expiry_date, reshelf_quantity, shelf_capacity, stock_location) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, stock.getBatchCode());
            statement.setString(2, stock.getItemCode());
            statement.setInt(3, stock.getQuantityInStock());
            statement.setDate(4, java.sql.Date.valueOf(stock.getDateOfPurchase()));
            statement.setDate(5, java.sql.Date.valueOf(stock.getExpiryDate()));
            statement.setInt(6, stock.getReshelfQuantity());
            statement.setInt(7, stock.getShelfCapacity());
            statement.setString(8, stock.getStockLocation());

            statement.executeUpdate();
            System.out.println("✅ Stock record inserted successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error saving stock record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Stock findByBatchCode(String batchCode, Connection connection) {
        String sql = "SELECT * FROM stock WHERE batch_code = ?";
        Stock stock = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, batchCode);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                stock = mapStock(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding stock by batch code: " + e.getMessage());
            e.printStackTrace();
        }
        return stock;
    }

    @Override
    public void update(Stock stock, Connection connection) {
        String sql = "UPDATE stock SET quantity_in_stock = ?, date_of_purchase = ?, expiry_date = ?, reshelf_quantity = ?, shelf_capacity = ?, stock_location = ? WHERE batch_code = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, stock.getQuantityInStock());
            statement.setDate(2, java.sql.Date.valueOf(stock.getDateOfPurchase()));
            statement.setDate(3, java.sql.Date.valueOf(stock.getExpiryDate()));
            statement.setInt(4, stock.getReshelfQuantity());
            statement.setInt(5, stock.getShelfCapacity());
            statement.setString(6, stock.getStockLocation());
            statement.setString(7, stock.getBatchCode());

            statement.executeUpdate();
            System.out.println("✅ Stock updated successfully for batch: " + stock.getBatchCode());
        } catch (SQLException e) {
            System.err.println("❌ Error updating stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String batchCode, Connection connection) {
        String sql = "DELETE FROM stock WHERE batch_code = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, batchCode);
            statement.executeUpdate();
            System.out.println("✅ Stock with batch code " + batchCode + " deleted successfully.");
        } catch (SQLException e) {
            System.err.println("❌ Error deleting stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Stock> findAll(Connection connection) {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM stock";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                stocks.add(mapStock(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving stocks: " + e.getMessage());
            e.printStackTrace();
        }
        return stocks;
    }

    private Stock mapStock(ResultSet resultSet) throws SQLException {
        Stock stock = new Stock(
                resultSet.getString("batch_code"),
                resultSet.getString("item_code"),
                resultSet.getInt("quantity_in_stock"),
                resultSet.getDate("date_of_purchase").toLocalDate(),
                resultSet.getDate("expiry_date").toLocalDate(),
                resultSet.getInt("reshelf_quantity"),
                resultSet.getInt("shelf_capacity"),
                resultSet.getString("stock_location")
        );
        stock.setStockId(resultSet.getInt("stock_id"));
        return stock;
    }
}
