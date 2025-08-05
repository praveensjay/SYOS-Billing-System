package com.syos.dao;

import com.syos.model.Transaction;
import com.syos.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    @Override
    public void save(Transaction transaction, Connection connection) {
        String sql = "INSERT INTO transaction (bill_id, item_id, quantity, total_price, transaction_date, transaction_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getBill().getBillId());
            statement.setInt(2, transaction.getItem().getItemId());
            statement.setInt(3, transaction.getQuantity());
            statement.setBigDecimal(4, transaction.getTotalPrice());
            statement.setDate(5, java.sql.Date.valueOf(transaction.getTransactionDate()));
            statement.setString(6, transaction.getTransactionType());

            statement.executeUpdate();
            System.out.println("✅ Transaction saved successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Error saving transaction: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findByBillId(int billId, Connection connection) {
        String sql = """
            SELECT t.transaction_id, t.quantity, t.total_price, t.transaction_date, t.transaction_type, i.item_name
            FROM transaction t
            JOIN item i ON t.item_id = i.item_id
            WHERE t.bill_id = ?
        """;

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                transactions.add(mapTransaction(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error retrieving transactions by Bill ID: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transaction_id"));
        transaction.setQuantity(resultSet.getInt("quantity"));
        transaction.setTotalPrice(resultSet.getBigDecimal("total_price"));
        transaction.setTransactionDate(resultSet.getDate("transaction_date").toLocalDate());
        transaction.setTransactionType(resultSet.getString("transaction_type"));

        Item item = new Item();
        item.setItemName(resultSet.getString("item_name"));
        transaction.setItem(item);

        return transaction;
    }
}
