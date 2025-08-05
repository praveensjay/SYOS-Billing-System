package com.syos.dao;

import com.syos.model.Item;
import java.sql.Connection;
import java.util.List;

public interface ItemDAO {
    void save(Item item, Connection connection);
    Item findByCode(String itemCode, Connection connection);
    void update(Item item, Connection connection);
    void delete(String itemCode, Connection connection);
    List<Item> findAll(Connection connection);
    Item findById(int itemId, Connection connection);
}
