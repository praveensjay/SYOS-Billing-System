package com.syos.dao;

import com.syos.config.ConnectionPool;
import com.syos.model.Bill;
import com.syos.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDAOImpl implements BillDAO {

    @Override
    public void saveBill(Bill bill, Connection connection) {
        String sql = "INSERT INTO bill (bill_date, total_price, customer_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, java.sql.Date.valueOf(bill.getBillDate()));
            statement.setBigDecimal(2, bill.getTotalPrice());
            statement.setInt(3, bill.getCustomer().getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBill(Bill bill, Connection connection) {
        String sql = "UPDATE bill SET total_price = ?, discount_amount = ?, tax_amount = ?, final_price = ?, cash_tendered = ?, change_amount = ? WHERE bill_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, bill.getTotalPrice());
            statement.setBigDecimal(2, bill.getDiscountAmount());
            statement.setBigDecimal(3, bill.getTaxAmount());
            statement.setBigDecimal(4, bill.getFinalPrice());
            statement.setBigDecimal(5, bill.getCashTendered());
            statement.setBigDecimal(6, bill.getChangeAmount());
            statement.setInt(7, bill.getBillId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bill getBillById(int billId, Connection connection) {
        String sql = "SELECT * FROM bill WHERE bill_id = ?";
        Bill bill = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bill = new Bill();
                bill.setBillId(resultSet.getInt("bill_id"));
                bill.setBillDate(resultSet.getDate("bill_date").toLocalDate());
                bill.setTotalPrice(resultSet.getBigDecimal("total_price"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bill;
    }

    @Override
    public List<Transaction> getTransactionsByBillId(int billId, Connection connection) {
        String sql = "SELECT * FROM transaction WHERE bill_id = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, billId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(resultSet.getInt("transaction_id"));
                transaction.setQuantity(resultSet.getInt("quantity"));
                transaction.setTotalPrice(resultSet.getBigDecimal("total_price"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}
