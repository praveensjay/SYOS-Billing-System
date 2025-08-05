package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.BillDAO;
import com.syos.dao.BillDAOImpl;
import com.syos.model.Bill;
import com.syos.model.Customer;
import com.syos.model.Item;
import com.syos.model.Transaction;
import com.syos.utils.BillFormatter;
import com.syos.utils.DiscountCalculator;
import com.syos.utils.TaxCalculator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class BillingManagerImpl implements BillingManager {

    private final BillDAO billDAO;
    private final DiscountCalculator discountCalculator;
    private final TaxCalculator taxCalculator;
    private final ReentrantLock lock = new ReentrantLock(true); // Ensures fairness in multi-threading

    public BillingManagerImpl() {
        this.billDAO = new BillDAOImpl();
        this.discountCalculator = new DiscountCalculator();
        this.taxCalculator = new TaxCalculator();
    }

    @Override
    public Bill createBill(Customer customer) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            Bill bill = new Bill(LocalDate.now(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, customer);
            billDAO.saveBill(bill, connection);
            System.out.println("✅ New bill created for customer: " + customer.getName());
            return bill;
        } catch (Exception e) {
            System.err.println("❌ Error creating bill: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Bill getBillById(int billId) {  // ✅ Implement the missing method
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            Bill bill = billDAO.getBillById(billId, connection);
            if (bill != null) {
                System.out.println("✅ Retrieved bill ID: " + billId);
            } else {
                System.out.println("❌ Bill not found for ID: " + billId);
            }
            return bill;
        } catch (Exception e) {
            System.err.println("❌ Error retrieving bill: " + e.getMessage());
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void addItemToBill(Bill bill, Item item, int quantity) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            if (item != null && item.getItemId() > 0) {
                BigDecimal itemPrice = item.getItemPrice().multiply(BigDecimal.valueOf(quantity));
                bill.setTotalPrice(bill.getTotalPrice().add(itemPrice));

                System.out.println("✅ Item added to the bill: " + item.getItemName() + " - Quantity: " + quantity);
                billDAO.updateBill(bill, connection);
            } else {
                System.out.println("❌ Error: Item is invalid or not found.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error adding item to bill: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void applyDiscount(Bill bill, double discountRate) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            BigDecimal discountAmount = discountCalculator.calculateDiscount(bill.getTotalPrice(), discountRate);
            bill.setDiscountAmount(discountAmount);

            BigDecimal finalPrice = bill.getTotalPrice().subtract(discountAmount).add(bill.getTaxAmount());
            bill.setFinalPrice(finalPrice);

            System.out.println("✅ Discount applied to the bill.");
            billDAO.updateBill(bill, connection);
        } catch (Exception e) {
            System.err.println("❌ Error applying discount: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void finalizeBill(Bill bill, double cashTendered, boolean useLoyaltyPoints) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            BigDecimal taxAmount = taxCalculator.calculateTax(bill.getTotalPrice());
            bill.setTaxAmount(taxAmount);

            BigDecimal finalPrice = bill.getTotalPrice().add(taxAmount).subtract(bill.getDiscountAmount());

            if (useLoyaltyPoints && bill.getCustomer().getLoyaltyPoints() > 0) {
                BigDecimal loyaltyPointsValue = BigDecimal.valueOf(bill.getCustomer().getLoyaltyPoints());
                if (loyaltyPointsValue.compareTo(finalPrice) > 0) {
                    loyaltyPointsValue = finalPrice; // Prevent negative total
                }
                finalPrice = finalPrice.subtract(loyaltyPointsValue);
                bill.getCustomer().setLoyaltyPoints(0); // Reset loyalty points
            }

            bill.setFinalPrice(finalPrice);
            bill.setCashTendered(BigDecimal.valueOf(cashTendered));
            BigDecimal change = BigDecimal.valueOf(cashTendered).subtract(bill.getFinalPrice());
            bill.setChangeAmount(change.compareTo(BigDecimal.ZERO) >= 0 ? change : BigDecimal.ZERO);

            billDAO.updateBill(bill, connection);
            System.out.println("✅ Bill finalized. Change: " + change);
        } catch (Exception e) {
            System.err.println("❌ Error finalizing bill: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Transaction> getTransactionsByBillId(int billId) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return billDAO.getTransactionsByBillId(billId, connection);
        } catch (Exception e) {
            System.err.println("❌ Error retrieving transactions: " + e.getMessage());
            return null;
        }
    }
}
