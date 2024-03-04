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
            Integer lastCustomerId = customerRepository.findMaxCustomerIdByStoreId(customerDetails.getStoreId());
            customerDetails.setCustomerId(lastCustomerId != null ? lastCustomerId + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(customerRepository.save(customerDetails), true, "Added Successfully", 200));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "... ",500));
        }
    }


    @Override
    public ResponseEntity<ApiResponse> getById(Integer serialNo) {
        try {
            Optional<CustomerDetails> existingCustomerDetails = customerRepository.findById(serialNo);
            if (existingCustomerDetails.isPresent()) {
                return ResponseEntity.ok().body(new ApiResponse(existingCustomerDetails, true, "Found Successfulyy", 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }

    }

    @Override
    public ResponseEntity<ApiResponse> getByStoreId(Integer storeId , Integer page , Integer size,String startDate,String endDate) {
        try {
            List<CustomerDetails> existingCustomer = customerRepository.findByStoreId(storeId);
            if (existingCustomer.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null,false,"Id Not Found",404));
            }else {
                if (page!=null && size!=null){
                    return ResponseEntity.ok().body(new ApiResponse(getByPageAndSize(storeId,page,size),true,200));
                } else if (startDate!=null && endDate!=null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByDate(storeId,startDate,endDate),true,200));
                }else  return ResponseEntity.ok().body(new ApiResponse(existingCustomer,true,200));
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null,false,"....",500));
        }
    }

    private Object getByDate(Integer storeId, String startDate, String endDate) {
        List<CustomerDetails> customerList = customerRepository.findByStoreIdAndDateRange(storeId,startDate,endDate);
        List<Map<String ,Object>> customerMapList= new ArrayList<>();
        if (customerList!=null) {
            for (CustomerDetails customerDetails : customerList) {
                Map<String, Object> customerMap = new HashMap<>();
                customerMap.put("id", customerDetails.getCustomerId());
                customerMap.put("name", customerDetails.getCustomerName());
                customerMap.put("contact", customerDetails.getContact());
                customerMap.put("email", customerDetails.getEmail());
                customerMap.put("dob", customerDetails.getDateOfBirth());
                customerMapList.add(customerMap);
            }
        }
        return customerMapList;
    }

    private Object getByPageAndSize(Integer storeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<CustomerDetails> existingCustomerDetails = customerRepository.findByStore_id(storeId ,pageable);
        List<Map<String,Object>> customerList = new ArrayList<>();
        for (CustomerDetails customerDetails : existingCustomerDetails) {
            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("id", customerDetails.getCustomerId());
            customerMap.put("name", customerDetails.getCustomerName());
            customerMap.put("contact", customerDetails.getContact());
            customerMap.put("email", customerDetails.getEmail());
            customerMap.put("dob", customerDetails.getDateOfBirth());
            customerList.add(customerMap);
        }
        return customerList;
    }


}
