package com.syntiaro_pos_system.serviceimpl.v1;



import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.repository.v1.InventoryRepo;
import com.syntiaro_pos_system.service.v1.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InventoryServiceIMPL implements InventoryService {
    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public Inventory updateInventory(Inventory inventory) {
        inventoryRepo.save(inventory);
        return inventory;
    }

    //THIS METHOD IS USE FOR DELETE INVENTORY
    @Override
    public void deleteinventory(int inventoryId) {
        inventoryRepo.deleteById(inventoryId);
    }

    //THIS METHOD IS USE FOR GET ALL LIST OF INVENTORY
    @Override
    public List<Inventory> getinvo() {
        return inventoryRepo.findAll();
    }

    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    @Override
    public Inventory getInventoryDetailsById(int id) {
        return inventoryRepo.findById((int) id).orElse(null);
    }

    // THIS METHOD IS USE FOR FETCH INVENTORY BY STOREID
    @Override
    public List<Inventory> fetchInventoryByStoreId(String storeId) {
        return inventoryRepo.findByStoreId(storeId);
    }

    // THIS METHOD IS USE FOR UPDATE INVENTORY
    @Override
    public Inventory updateInventory(String id, Inventory inventory) {
        Optional<Inventory> existingInventory = inventoryRepo.findById(Integer.parseInt(id));
        if (existingInventory.isPresent()) {
            Inventory updatedInventory = existingInventory.get();
            // Update the properties of the updatedInventory
            if (inventory.getQuantity() != null) {
                updatedInventory.setQuantity(existingInventory.get().getQuantity()+inventory.getQuantity());  // bug_01 Add Calculation code
            }
            if (inventory.getName() != null) {
                updatedInventory.setName(inventory.getName());
            }
            if (inventory.getInventorydate() != null) {
                updatedInventory.setInventorydate(inventory.getInventorydate());
            }
            if (inventory.getCategory() != null) {
                updatedInventory.setCategory(inventory.getCategory());
            }
            if (inventory.getPrice() != null) {
                updatedInventory.setPrice(inventory.getPrice());
            }
            if (inventory.getExpiryDate() != null) {
                updatedInventory.setExpiryDate(inventory.getExpiryDate());
            }
            if (inventory.getMinLevel() != null) {
                updatedInventory.setMinLevel(inventory.getMinLevel());
            }
            if (inventory.getMinLevelUnit() != null) {
                updatedInventory.setMinLevelUnit(inventory.getMinLevelUnit());
            }
            if (inventory.getUpdateDate() != null) {
                updatedInventory.setUpdateDate(inventory.getUpdateDate());
            }
            if (inventory.getCreatedDate() != null) {
                updatedInventory.setCreatedDate(inventory.getCreatedDate());
            }
            if (inventory.getCreatedBy() != null) {
                updatedInventory.setCreatedBy(inventory.getCreatedBy());
            }
            if (inventory.getUpdatedBy() != null) {
                updatedInventory.setUpdatedBy(inventory.getUpdatedBy());
            }
            if (inventory.getStoreId() != null) {
                updatedInventory.setStoreId(inventory.getStoreId());
            }
            if (inventory.getUnit() != null) {
                updatedInventory.setUnit(inventory.getUnit());
            }
            if (inventory.getGstNo() != null) {
                updatedInventory.setGstNo(inventory.getGstNo());
            }
            if (inventory.getInventoryCode() != null) {
                updatedInventory.setInventoryCode(inventory.getInventoryCode());
            }
            if (inventory.getTotal() != null) {
                updatedInventory.setTotal(inventory.getTotal());
            }

            inventoryRepo.save(updatedInventory);
            return updatedInventory;
        } else {
            return null;
        }
    }

    @Override
    public Inventory addInventoryItem(Inventory newItem) {
        // Check if an item with the same name already exists for the given store ID
        Inventory existingItem = inventoryRepo.findByStoreIdAndName(newItem.getStoreId(), newItem.getName());
        if (existingItem != null) {
            throw new IllegalArgumentException("An item with the same name already exists for this store.");
        }
        return inventoryRepo.save(newItem);
    }

    @Override
    public List<Inventory> fetchInventoryByStoreId(Integer storeId) {
        return inventoryRepo.findByStoreId(String.valueOf(storeId));
    }

    @Override
    public boolean existsInventory(int inventoryId) {
        return inventoryRepo.existsById(inventoryId);
    }






}
