package com.syos.dao;

import com.syos.model.User;
import java.sql.Connection;

public interface UserDAO {
    int saveUser(User user, Connection connection);
    User findByEmail(String email, Connection connection);
}
