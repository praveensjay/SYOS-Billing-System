package com.syos.dao;

import com.syos.model.Shelf;
import java.sql.Connection;

public interface ShelfDAO {
    Shelf findByItemCode(String itemCode, Connection connection);
    void update(Shelf shelf, Connection connection);
    void save(Shelf shelf, Connection connection);
}
