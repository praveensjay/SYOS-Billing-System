package com.syos.model;

public class Admin extends User {

    public Admin(int userId, String name, String email, String passwordHash) {
        super(userId, name, email, passwordHash, "ADMIN"); // ✅ Call the correct User constructor
    }
}
