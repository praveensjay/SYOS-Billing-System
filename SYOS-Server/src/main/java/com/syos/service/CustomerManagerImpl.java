package com.syos.service;

import com.syos.config.ConnectionPool;
import com.syos.dao.CustomerDAO;
import com.syos.dao.CustomerDAOImpl;
import com.syos.model.Customer;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class CustomerManagerImpl implements CustomerManager {

    private final CustomerDAO customerDAO;
    private final ReentrantLock lock = new ReentrantLock(true); // Thread safety for concurrent operations

    public CustomerManagerImpl() {
        this.customerDAO = new CustomerDAOImpl();
    }

    @Override
    public void addCustomer(Customer customer) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            customerDAO.save(customer, connection);
            System.out.println("✅ Customer added: " + customer.getName());
        } catch (Exception e) {
            System.err.println("❌ Error adding customer: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Customer findCustomerById(int customerId) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return customerDAO.findById(customerId, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding customer by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Customer findCustomerByPhoneNumber(String phoneNumber) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return customerDAO.findByPhoneNumber(phoneNumber, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding customer by phone number: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            customerDAO.update(customer, connection);
            System.out.println("✅ Customer updated: " + customer.getName());
        } catch (Exception e) {
            System.err.println("❌ Error updating customer: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void removeCustomer(int customerId) {
        lock.lock();
        try (Connection connection = ConnectionPool.getConnection()) {
            customerDAO.delete(customerId, connection);
            System.out.println("✅ Customer removed: ID " + customerId);
        } catch (Exception e) {
            System.err.println("❌ Error removing customer: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Customer> findAllCustomers() {
        try (Connection connection = ConnectionPool.getConnection()) {
            return customerDAO.findAll(connection);
        } catch (Exception e) {
            System.err.println("❌ Error retrieving customers: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        try (Connection connection = ConnectionPool.getConnection()) {
            return customerDAO.findByEmail(email, connection);
        } catch (Exception e) {
            System.err.println("❌ Error finding customer by email: " + e.getMessage());
            return null;
        }
    }
}
