package com.syntiaro_pos_system.service.v1;


import com.syntiaro_pos_system.entity.v1.Invoice;

import java.util.List;

public interface InvoiceService {


	Invoice updateInvoice(Invoice invoice);


	List<Invoice> getInvoice();


	String addInvoice(Invoice invoice);


	// THIS METHOD IS USE FOR UPDATE INVOICE
	Invoice updateInvoice(Integer invoice_id, Invoice updateinvoice);


	// THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
	List<Invoice> fetchInvoicesByStoreId(Integer storeId);


	//THIS METHOD IS USE FOR DELETE INVOICE
	void deleteInvoiceById(Integer invoiceId);
}
