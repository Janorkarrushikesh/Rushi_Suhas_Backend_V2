package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.VendorInventory;

import java.util.List;

public interface InvoiceService {


	VendorInventory updateInvoice(VendorInventory vendorInventory);


	List<VendorInventory> getInvoice();


	String addInvoice(VendorInventory vendorInventory);


	// THIS METHOD IS USE FOR UPDATE INVOICE
	VendorInventory updateInvoice(Integer invoice_id, VendorInventory updateinvoice);


	// THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
	List<VendorInventory> fetchInvoicesByStoreId(Integer storeId);


	//THIS METHOD IS USE FOR DELETE INVOICE
	void deleteInvoiceById(Integer invoiceId);
}
