package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.CustomerDetails;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.CustomerRepository;
import com.syntiaro_pos_system.service.v2.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public ResponseEntity<ApiResponse> saveCustomer(CustomerDetails customerDetails) {

        try {
            if (customerRepository.existsByCustomerNameAndStoreId(customerDetails.getCustomerName(), customerDetails.getStoreId())) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(null, false, "A Customer with the same name already exists.", 208));
            }
            if (customerRepository.existsByContactAndStoreId(customerDetails.getContact(), customerDetails.getStoreId())) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(null, false, "A Customer with the same contact  already exists.", 208));
            }
            if (customerRepository.existsByEmailAndStoreId(customerDetails.getEmail(), customerDetails.getStoreId())) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new ApiResponse(null, false, "A Customer with the same email  already exists.", 208));
            }
            Integer lastBillNumbers = customerRepository.findMaxVendorIdByStoreId(customerDetails.getStoreId());
            customerDetails.setCustomerId(lastBillNumbers != null ? lastBillNumbers + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(customerRepository.save(customerDetails), true, "Added Successfully", 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "... ",500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> getById(Integer serialNo) {
        try {
            Optional<CustomerDetails> cData = customerRepository.findById(serialNo);
            if (cData.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(cData, true, "Found Successfulyy", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(cData, true, "Id Not Found", 404));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Integer storeId , Integer page , Integer size) {
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<CustomerDetails> sData = customerRepository.findByStore_id(storeId ,pageable);
            if(sData.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,true,"Id Not Found",404));
            }
            else {
                List<Map<String,Object>> customerList = new ArrayList<>();
                for (CustomerDetails customerDetails : sData){
                    Map<String,Object> customerMap = new LinkedHashMap<>();
                    customerMap.put("id",customerDetails.getCustomerId());
                    customerMap.put("name",customerDetails.getCustomerName());
                    customerMap.put("contact",customerDetails.getContact());
                    customerMap.put("email",customerDetails.getEmail());
                    customerMap.put("dob",customerDetails.getDateOfBirth());
                    customerList.add(customerMap);
                }
                return ResponseEntity.ok().body(new ApiResponse(customerList, true,  200));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }


}
