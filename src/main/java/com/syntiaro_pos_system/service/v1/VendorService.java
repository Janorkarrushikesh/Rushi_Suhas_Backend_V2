package com.syntiaro_pos_system.service.v1;

import com.syntiaro_pos_system.entity.v1.Vendor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VendorService {


   //THIS METHOD IS USE FOR GET ALL LIST OF VENDOR
	List<Vendor> getVendors();

   //THIS METHOD IS USE FOR UPDATE VENDOR
	Vendor updateVendor(Vendor vendor);

	//THIS METHOD IS USE FOR ADD VENDOR
	String addVendor(Vendor vendor);
	// THIS METHOD IS USE FOR FETCH VENDOR BY ID
	Vendor getVendorDetailsById(Integer id);
	//THIS METHOD IS USE FOR POST VEDOR
	Vendor saveDetails(Vendor vendor);
	// THIS METHOD IS USE FOR UPDATE VENDOR
	Vendor updateVendor(Integer vendor_id, Vendor updatevendor);
	// THIS METHOD IS USE FOR FETCH VENDOR BY STOREID
	List<Vendor> fetchVendorsBystoreId(Integer storeId);

	//THIS METHOD IS USE FOR UPDATE VENDOR
	ResponseEntity<?> updateVendorWithValidation(Long vendorId, Vendor vendor);
}