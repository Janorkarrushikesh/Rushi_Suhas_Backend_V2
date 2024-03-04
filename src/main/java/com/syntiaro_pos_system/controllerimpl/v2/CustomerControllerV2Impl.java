package com.syntiaro_pos_system.controllerimpl.v2;

import com.syntiaro_pos_system.controller.v2.CustomerController;
import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.service.v2.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerControllerV2Impl implements CustomerController {

    @Autowired
    CustomerService customerService;

    @Override
    public ResponseEntity<ApiResponse> saveCustomer(CustomerDetails customer) {
        return customerService.saveCustomer(customer);
    }

    @Override
    public ResponseEntity<ApiResponse> getById(Integer SerialNo) {
        return customerService.getById(SerialNo);
    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Integer storeId , Integer page , Integer size,String startDate,String endDate) {
        return customerService.getByStoreId(storeId , page,size,startDate,endDate);
    }
}
