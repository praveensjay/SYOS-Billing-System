package com.syos.service;

import com.syos.model.Customer;
import java.util.List;

public interface CustomerManager {
    void addCustomer(Customer customer);
    Customer findCustomerById(int customerId);
    Customer findCustomerByPhoneNumber(String phoneNumber);
    void updateCustomer(Customer customer);
    void removeCustomer(int customerId);
    List<Customer> findAllCustomers();
    Customer findCustomerByEmail(String email);
}
