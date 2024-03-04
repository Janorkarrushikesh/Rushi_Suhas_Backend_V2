package com.syntiaro_pos_system.service.v2;

import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<ApiResponse> saveCustomer(CustomerDetails customer);

    ResponseEntity<ApiResponse> getById(Integer serialNo);

    ResponseEntity<ApiResponse> getByStoreId(Integer storeId , Integer page , Integer size);
}
