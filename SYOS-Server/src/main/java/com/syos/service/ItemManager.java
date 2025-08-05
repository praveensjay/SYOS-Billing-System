package com.syos.service;

import com.syos.model.Item;
import java.util.List;

public interface ItemManager {
    void addItem(Item item);
    Item findByCode(String itemCode);
    Item findById(int itemId);
    void updateItem(Item item);
    void removeItem(String itemCode);
    List<Item> getAllItems();
}
