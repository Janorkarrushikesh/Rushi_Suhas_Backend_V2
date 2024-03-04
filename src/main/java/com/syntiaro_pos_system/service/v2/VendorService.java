package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.entity.v1.Vendor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
public interface VendorService {
    ResponseEntity<ApiResponse> saveVendor(Vendor vendor);

    ResponseEntity<ApiResponse> getVendorById(Long serialNo);

    ResponseEntity<ApiResponse> getByStoreId(Integer storeId ,Integer size , Integer page,String startDate,String endDate);

    ResponseEntity<ApiResponse> updateById(Long serialNo, Vendor vendor);

    ResponseEntity<ApiResponse> deleteVendorById(Long serialNo);
}
