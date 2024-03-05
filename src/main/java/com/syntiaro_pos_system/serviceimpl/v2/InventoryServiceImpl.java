package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Inventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.InventoryRepository;
import com.syntiaro_pos_system.service.v2.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;


    @Override
    public ResponseEntity<ApiResponse> getStoreId(String storeId, Integer page, Integer size, String startDate, String endDate) {
        try {
            List<Inventory> existingInventoryList = inventoryRepository.findByStoreId(storeId);
            if (existingInventoryList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Store Id Not Found ", 404));
            } else {
                if (size != null) {
                    return ResponseEntity.ok()
                            .body(new ApiResponse(getInventryByPageAndSize(storeId, page, size), true, 200));
                } else if (startDate != null && endDate != null) {
                    return ResponseEntity.ok()
                            .body(new ApiResponse(getByStartDateAndEndDate(storeId, startDate, endDate), true, 200));
                } else {
                    return ResponseEntity.ok()
                            .body(new ApiResponse(existingInventoryList, true, 200));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    // for date
    public List<Map<String, Object>> getByStartDateAndEndDate(String storeId, String startDate, String endDate) {
        List<Inventory> inventoryList = inventoryRepository.getInventoryByDate(storeId, startDate, endDate);
        List<Map<String, Object>> inventoryListMap = new ArrayList<>();
        if (inventoryList != null) {
            Map<String, Object> inventoryMap = null;
            for (Inventory inventory : inventoryList) {
                inventoryMap = new LinkedHashMap<>();
                inventoryMap.put("id", inventory.getId());
                inventoryMap.put("quantity", inventory.getQuantity());
                inventoryMap.put("name", inventory.getName());
                inventoryMap.put("inventoryDate", inventory.getInventorydate());
                inventoryMap.put("category", inventory.getCategory());
                inventoryMap.put("price", inventory.getPrice());
                inventoryMap.put("expiryDate", inventory.getExpiryDate());
                inventoryMap.put("minLevel", inventory.getMinLevel());
                inventoryMap.put("minLevelUnit", inventory.getMinLevelUnit());
                inventoryMap.put("storeId", inventory.getStoreId());
                inventoryMap.put("unit", inventory.getUnit());
                inventoryMap.put("total", inventory.getTotal());
                inventoryMap.put("inventoryCode", inventory.getInventoryCode());
                inventoryListMap.add(inventoryMap);
            }
        }
        return inventoryListMap;
    }

    // for page and size
    public List<Map<String, Object>> getInventryByPageAndSize(String storeId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Inventory> inventoryList = inventoryRepository.findByStoreId(storeId, pageable);
        List<Map<String, Object>> inventoryData = null;
        if (inventoryList != null) {
            inventoryData = new ArrayList<>();
            for (Inventory inventory : inventoryList) {
                Map<String, Object> inventoryMap = new LinkedHashMap<>();
                inventoryMap.put("id", inventory.getId());
                inventoryMap.put("date", inventory.getCreatedDate());
                inventoryMap.put("name", inventory.getName());
                inventoryMap.put("quantity", inventory.getQuantity());
                inventoryMap.put("unit", inventory.getUnit());
                inventoryMap.put("minimumLevel", inventory.getMinLevel());
                inventoryMap.put("minimumLevelUnit", inventory.getMinLevelUnit());
                inventoryMap.put("category", inventory.getCategory());
                inventoryMap.put("price", inventory.getPrice());
                inventoryMap.put("expiryDate", inventory.getExpiryDate());
                inventoryMap.put("storeId", inventory.getStoreId());
                inventoryMap.put("inventoryCode", inventory.getInventoryCode());
                inventoryData.add(inventoryMap);
            }
        }
        return inventoryData;
    }

    @Override
    public ResponseEntity<ApiResponse> saveInventory(Inventory inventory) {
        try {
            Integer lastId = inventoryRepository.findMaxIdByStoreId(inventory.getStoreId());
            inventory.setId(lastId != null ? lastId + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(inventoryRepository.save(inventory), true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getInventoryById(Integer serialNo) {
        try {
            Optional<Inventory> inventoryData = inventoryRepository.findById(serialNo);
            if (inventoryData.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(inventoryData.get(), true, "Inventory found", 200));
            } else {
                return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "..", 500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> updateinventoryById(Integer serialNo, Inventory inventory) {
        try {
            Optional<Inventory> existingInventory = inventoryRepository.findById(serialNo);
            if (existingInventory.isPresent()) {
                Inventory updatedInventory = existingInventory.get();
                if (inventory.getQuantity() != null) {
                    updatedInventory.setQuantity(existingInventory.get().getQuantity() + inventory.getQuantity());
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
                return ResponseEntity.ok().body(new ApiResponse(inventoryRepository.save(updatedInventory), true, "Updated SuccessFully", 200));
            } else {
                return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
            }
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getMessage(String storeId) {
        try {

            List<Inventory> inventory = inventoryRepository.findByStoreId(storeId);
            List<String> message = new ArrayList<>();
            for (Inventory inv : inventory) {
                if (inv.getQuantity() <= inv.getMinLevel()) {
                    message.add("Inventory level is low for " + inv.getName());
                }

            }
            return ResponseEntity.ok().body(new ApiResponse(message, true, 200));

        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteById(Integer SerialNo) {
        try {
            Optional<Inventory> existsInventory = inventoryRepository.findById(SerialNo);
            if (existsInventory.isPresent()) {
                inventoryRepository.deleteById(SerialNo);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "Deleted Succesfully", 200));
            }
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "..", 500));
        }
    }

}
