package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.Inventory;

import java.util.List;

public interface InventoryService {
    Inventory addInventoryItem(Inventory newItem);

    Inventory updateInventory(Inventory inventory);


    List<Inventory> getinvo();


    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    Inventory getInventoryDetailsById(int id);


    // THIS METHOD IS USE FOR UPDATE INVENTORY
    Inventory updateInventory(String id, Inventory inventory);


    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    List<Inventory> fetchInventoryByStoreId(String storeId);

    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    List<Inventory> fetchInventoryByStoreId(Integer storeId);


    boolean existsInventory(int inventoryId);

    void deleteinventory(int inventoryId);
}
