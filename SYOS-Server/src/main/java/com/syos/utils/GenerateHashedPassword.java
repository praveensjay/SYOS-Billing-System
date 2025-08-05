package com.syos.utils;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHashedPassword {
    public static void main(String[] args) {
        String rawPasswordAdmin = "adminpassword";
        String rawPasswordCustomer = "customerpassword";

        String hashedPasswordAdmin = BCrypt.hashpw(rawPasswordAdmin, BCrypt.gensalt(12));
        String hashedPasswordCustomer = BCrypt.hashpw(rawPasswordCustomer, BCrypt.gensalt(12));

        System.out.println("Admin Password Hash: " + hashedPasswordAdmin);
        System.out.println("Customer Password Hash: " + hashedPasswordCustomer);
    }
}
