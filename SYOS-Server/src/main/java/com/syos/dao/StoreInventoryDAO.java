package com.syos.dao;

import com.syos.model.StoreInventory;
import java.sql.Connection;
import java.util.List;

public interface StoreInventoryDAO {
    List<StoreInventory> findByItemCodeOrderedByExpiry(String itemCode, Connection connection);
    void update(StoreInventory storeInventory, Connection connection);
}
