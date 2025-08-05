package com.syos.dao;

import com.syos.model.Customer;
import java.sql.Connection;
import java.util.List;

public interface CustomerDAO {
    void save(Customer customer, Connection connection);
    Customer findById(int customerId, Connection connection);
    Customer findByPhoneNumber(String phoneNumber, Connection connection);
    Customer findByEmail(String email, Connection connection); // Missing method added
    void update(Customer customer, Connection connection);
    void delete(int customerId, Connection connection);
    List<Customer> findAll(Connection connection);
}
