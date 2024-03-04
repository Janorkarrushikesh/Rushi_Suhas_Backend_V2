package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Vendor;
import com.syntiaro_pos_system.repository.v2.VendorRepository;
import com.syntiaro_pos_system.service.v2.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Service
public class VendorServiceImpl implements VendorService {
    @Autowired
    VendorRepository vendorRepository;

    @Override
    public ApiResponse saveVendor(Vendor vendor) {
        try {
            Integer lastBillNumbers = vendorRepository.findMaxVendorIdByStoreId(vendor.getStoreId());
            vendor.setVendorId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
            return new ApiResponse(vendorRepository.save(vendor), true, 200);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(null, false, 400);
        }

    }

    @Override
    public ApiResponse getVendorById(Long serialNo) {
        Optional<Vendor> vendorData = vendorRepository.findById(serialNo);
        if (vendorData.isPresent()) {
            return new ApiResponse(vendorData, true, 200);
        }
        return new ApiResponse(null, false, "Id not found ", 400);

    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Integer storeId ,Integer page ,Integer size) {
        try {
            Pageable pageable = PageRequest.of(page ,size);
            Page<Vendor> vendorData = vendorRepository.findByStoreId(storeId , pageable);
            if (vendorData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Store Id Not Found", 400));
            }
            else {
                List<Map<String, Object>> vendorList = new ArrayList<>();
                for (Vendor vendor : vendorData) {
                    Map<String, Object> vendorMap = new LinkedHashMap<>();
                    vendorMap.put("id", vendor.getVendorId());
                    vendorMap.put("name",vendor.getVendorName());
                    vendorMap.put("vendorCode",vendor.getVendorCode());
                    vendorMap.put("address",vendor.getVendorAddress());
                    vendorMap.put("mobileNumber",vendor.getMobileNo());
                    vendorMap.put("BankName",vendor.getBankName());
                    vendorMap.put("branch",vendor.getBranch());
                    vendorMap.put("accountNumber",vendor.getAccountNo());
                    vendorMap.put("ifsc",vendor.getIfscCode());
                    vendorMap.put("upi",vendor.getUpiId());
                    vendorMap.put("gst",vendor.getGstNo());
                    vendorMap.put("storeId",vendor.getStoreId());
                    vendorList.add(vendorMap);

                }
                return ResponseEntity.ok()
                        .body(new ApiResponse(vendorList, true, 200));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updateById(Long serialNo, Vendor vendor) {
        try{
            Optional<Vendor> existingVendor = vendorRepository.findById(serialNo);
            if (existingVendor.isPresent()) {
                Vendor updatevendor = existingVendor.get();

                if (vendor.getVendorName() != null) {
                    updatevendor.setVendorName(vendor.getVendorName());
                }
                if (vendor.getVendorAddress() != null) {
                    updatevendor.setVendorAddress(vendor.getVendorAddress());
                }
                if (vendor.getMobileNo() != null) {
                    updatevendor.setMobileNo(vendor.getMobileNo());
                }
                if (vendor.getGstNo() != null) {
                    updatevendor.setGstNo(vendor.getGstNo());
                }
                if (vendor.getUpdateDate() != null) {
                    updatevendor.setUpdateDate(vendor.getUpdateDate());
                }
                if (vendor.getUpdatedBy() != null) {
                    updatevendor.setUpdatedBy(vendor.getUpdatedBy());
                }
                if (vendor.getCreatedDate() != null) {
                    updatevendor.setCreatedDate(vendor.getCreatedDate());
                }
                if (vendor.getCreatedBy() != null) {
                    updatevendor.setCreatedBy(vendor.getCreatedBy());
                }
                if (vendor.getStoreId() != null) {
                    updatevendor.setStoreId(vendor.getStoreId());
                }
                if (vendor.getBankName() != null) {
                    updatevendor.setBankName(vendor.getBankName());
                }
                if (vendor.getBranch() != null) {
                    updatevendor.setBranch(vendor.getBranch());
                }
                if (vendor.getAccountNo() != null) {
                    updatevendor.setAccountNo(vendor.getAccountNo());
                }
                if (vendor.getIfscCode() != null) {
                    updatevendor.setIfscCode(vendor.getIfscCode());
                }
                if (vendor.getUpiId() != null) {
                    updatevendor.setUpiId(vendor.getUpiId());
                }
                if (vendor.getVendorCode() != null) {
                    updatevendor.setVendorCode(vendor.getVendorCode());
                }
                if (vendor.getVendorId()!=null)
                {
                    updatevendor.setVendorId(vendor.getVendorId());
                }
                return  ResponseEntity.ok().body(new ApiResponse(vendorRepository.save(updatevendor),true,"Updated Successfully",200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
            }
        } catch(Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"...",500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deleteVendorById(Long serialNo) {
        try{
            Optional<Vendor> data= vendorRepository.findById(serialNo);
            if(data.isPresent())
            {
                vendorRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null,true,"deleted Successfully",200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }
}
