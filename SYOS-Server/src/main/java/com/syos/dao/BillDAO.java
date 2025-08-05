package com.syos.dao;

import com.syos.model.Bill;
import com.syos.model.Transaction;
import java.sql.Connection;
import java.util.List;

public interface BillDAO {
    void saveBill(Bill bill, Connection connection);
    void updateBill(Bill bill, Connection connection);  // ✅ Added method
    Bill getBillById(int billId, Connection connection);  // ✅ Updated method
    List<Transaction> getTransactionsByBillId(int billId, Connection connection); // ✅ Added method
}
