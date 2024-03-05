package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.VendorInventory;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.VendorInventoryRepository;
import com.syntiaro_pos_system.service.v2.VendorInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VendorInventoryServiceImpl implements VendorInventoryService {
    @Autowired
    VendorInventoryRepository vendorInventoryRepository;

    @Override
    public ResponseEntity<ApiResponse> saveInvoice(VendorInventory vendorInventory) {
        try {
            Integer lastInvoiceId = vendorInventoryRepository.findLastNumberForStore(vendorInventory.getStoreId());
            vendorInventory.setInvoiceId(lastInvoiceId != null ? lastInvoiceId + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(vendorInventoryRepository.save(vendorInventory), true, "Added Successfully", 200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "Failed to Add Invoice ", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @Override
    public ApiResponse getInvoiceById(Integer SerialNo) {
        Optional<VendorInventory> invoiceData = vendorInventoryRepository.findById(SerialNo);
        if (invoiceData.isPresent()) {
            return new ApiResponse(invoiceData, true, 200);
        }
        return new ApiResponse(null, false, "Id not found", 400);
    }

    @Override
    public ApiResponse getInvoiceByStoreId(Integer storeId) {
        List<VendorInventory> existingVendorInventory = vendorInventoryRepository.findInvoiceByStoreId(storeId);
        if (!existingVendorInventory.isEmpty()) {
            return new ApiResponse(existingVendorInventory, true, 200);
        }
        return new ApiResponse(null, false, "Id Not Found", 400);
    }

    @Override
    public ResponseEntity<ApiResponse> updateInvoice(Integer SerialNo, VendorInventory vendorInventory) {

        try {
            Optional<VendorInventory> existingInvoice = vendorInventoryRepository.findById((SerialNo));
            if (existingInvoice.isPresent()) {
                VendorInventory updateinvoice = existingInvoice.get();
                if (vendorInventory.getStoreId() != null) {
                    updateinvoice.setStoreId(vendorInventory.getStoreId());
                }
                if (vendorInventory.getVendorName() != null) {
                    updateinvoice.setVendorName(vendorInventory.getVendorName());
                }
                if (vendorInventory.getItemName() != null) {
                    updateinvoice.setItemName(vendorInventory.getItemName());
                }
                if (vendorInventory.getInvoiceDate() != null) {
                    updateinvoice.setInvoiceDate(vendorInventory.getInvoiceDate());
                }
                if (vendorInventory.getPrice() != null) {
                    updateinvoice.setPrice(vendorInventory.getPrice());
                }
                if (vendorInventory.getQuantity() != null) {
                    updateinvoice.setQuantity(vendorInventory.getQuantity());
                }
                if (vendorInventory.getDiscount() != null) {
                    updateinvoice.setDiscount(vendorInventory.getDiscount());
                }
                if (vendorInventory.getPaymentStatus() != null) {
                    updateinvoice.setPaymentStatus(vendorInventory.getPaymentStatus());
                }
                if (vendorInventory.getTotal() != null) {
                    updateinvoice.setTotal(vendorInventory.getTotal());
                }
                if (vendorInventory.getGstNo() != null) {
                    updateinvoice.setGstNo(vendorInventory.getGstNo());
                }
                if (vendorInventory.getCreateDate() != null) {
                    updateinvoice.setCreatedBy(vendorInventory.getCreatedBy());
                }
                if (vendorInventory.getUpdatedBy() != null) {
                    updateinvoice.setUpdatedBy(vendorInventory.getUpdatedBy());
                }
                if (vendorInventory.getInventoryCode() != null) {
                    updateinvoice.setInventoryCode(vendorInventory.getInventoryCode());
                }

                return ResponseEntity.ok().body(new ApiResponse(vendorInventoryRepository.save(updateinvoice), true, "Updated Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> deleteInvoiceByid(Integer serialNo) {
        try {
            Optional<VendorInventory> existingInvoice = vendorInventoryRepository.findById(serialNo);
            if (existingInvoice.isPresent()) {
                vendorInventoryRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "deleted Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> getStore(Integer storeId, Integer page, Integer size, String startDate, String endDate) {
        try {
            List<VendorInventory> existingVendorInventory = vendorInventoryRepository.findInvoiceByStoreId(storeId);
            if (existingVendorInventory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id not Found", 404));
            } else {
                if (page != null && size != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByPageAndSize(storeId, page, size), true, 200));
                } else if (startDate != null & endDate != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByDate(storeId, startDate, endDate), true, 200));
                } else return ResponseEntity.ok().body(new ApiResponse(existingVendorInventory, true, 200));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    private Object getByDate(Integer storeId, String startDate, String endDate) {
        List<VendorInventory> vendorInventoryList = vendorInventoryRepository.findBetweenDate(storeId, startDate, endDate);
        List<Map<String, Object>> invoiceMap = new ArrayList<>();
        if (vendorInventoryList != null) {
            for (VendorInventory vendorInventory : vendorInventoryList) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", vendorInventory.getInvoiceId());
                map.put("name", vendorInventory.getVendorName());
                map.put("itemName", vendorInventory.getItemName());
                map.put("price", vendorInventory.getPrice());
                map.put("quantity", vendorInventory.getQuantity());
                map.put("discount", vendorInventory.getDiscount());
                map.put("invoiceStatus", vendorInventory.getPaymentStatus());
                map.put("total", vendorInventory.getTotal());
                map.put("unit", vendorInventory.getUnit());
                invoiceMap.add(map);
            }
            return invoiceMap;
        }
        return " Data Not Found ";
    }

    private Object getByPageAndSize(Integer storeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorInventory> invoiceData = vendorInventoryRepository.findByStoreId(storeId, pageable);
        List<Map<String, Object>> invoiceList = new ArrayList<>();
        for (VendorInventory vendorInventory : invoiceData) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", vendorInventory.getInvoiceId());
            map.put("name", vendorInventory.getVendorName());
            map.put("itemName", vendorInventory.getItemName());
            map.put("price", vendorInventory.getPrice());
            map.put("quantity", vendorInventory.getQuantity());
            map.put("discount", vendorInventory.getDiscount());
            map.put("invoiceStatus", vendorInventory.getPaymentStatus());
            map.put("total", vendorInventory.getTotal());
            map.put("unit", vendorInventory.getUnit());
            invoiceList.add(map);
        }
        return invoiceList;
    }


}
