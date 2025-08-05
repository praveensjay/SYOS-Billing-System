package com.syos.dao;

import com.syos.model.Transaction;
import java.sql.Connection;
import java.util.List;

public interface TransactionDAO {
    void save(Transaction transaction, Connection connection);
    List<Transaction> findByBillId(int billId, Connection connection);
}
