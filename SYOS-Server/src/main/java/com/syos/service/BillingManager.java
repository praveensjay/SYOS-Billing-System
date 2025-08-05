package com.syos.service;

import com.syos.model.Bill;
import com.syos.model.Customer;
import com.syos.model.Item;
import com.syos.model.Transaction;
import java.util.List;

public interface BillingManager {
    Bill createBill(Customer customer);
    void addItemToBill(Bill bill, Item item, int quantity);
    void applyDiscount(Bill bill, double discountRate);
    void finalizeBill(Bill bill, double cashTendered, boolean useLoyaltyPoints);
    List<Transaction> getTransactionsByBillId(int billId);
    Bill getBillById(int billId);
}
