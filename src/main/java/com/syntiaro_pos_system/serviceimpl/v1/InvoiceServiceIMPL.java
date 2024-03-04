package com.syntiaro_pos_system.serviceimpl.v1;


import com.syntiaro_pos_system.entity.v1.Invoice;
import com.syntiaro_pos_system.repository.v1.InvoiceRepo;
import com.syntiaro_pos_system.service.v1.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceIMPL implements InvoiceService {


	@Autowired
	InvoiceRepo invoiceRepo;


	//THIS METHOD IS USE FOR ADD INVOICE
	@Override
	public String addInvoice(Invoice invoice) {
		Integer lastBillNumber = invoiceRepo.findLastNumberForStore(invoice.getStoreId());
		invoice.setInvoiceId(lastBillNumber != null ? lastBillNumber + 1 : 1);
		invoiceRepo.save(invoice);
		return invoice.getItemName();
	}


	//THIS METHOD IS USE FOR GET ALL LIST OF INVOICE
	@Override
	public List<Invoice> getInvoice() {
		return this.invoiceRepo.findAll();
	}


	//THIS METHOD IS USE FOR UPDATE INVOICE
	@Override
	public Invoice updateInvoice(Invoice invoice) {
		invoiceRepo.save(invoice);
		return invoice;
	}



	// THIS METHOD IS USE FOR FETCH INVOICE BY STOREID
	@Override
	public List<Invoice> fetchInvoicesByStoreId(Integer storeId) {
		return invoiceRepo.findByStore_id(storeId);
	}

	@Override
	public void deleteInvoiceById(Integer invoiceId) {
		Optional<Invoice> invoiceOptional = invoiceRepo.findById(invoiceId);
		if (invoiceOptional.isPresent()) {
			invoiceRepo.deleteById(invoiceId);
		} else {
			throw new EntityNotFoundException("Invoice with ID " + invoiceId + " not found");
		}
	}

	// THIS METHOD IS USE FOR UPDATE INVOICE
	@Override
	public Invoice updateInvoice(Integer invoice_id, Invoice invoice) {
		Optional<Invoice> existingInvoice = invoiceRepo.findById((int) Integer.parseInt(String.valueOf((invoice_id))));
		if (existingInvoice.isPresent()) {
			Invoice updateinvoice = existingInvoice.get();
			if (invoice.getStoreId() != null) {
				updateinvoice.setStoreId(invoice.getStoreId());
			}
			if (invoice.getVendorName() != null) {
				updateinvoice.setVendorName(invoice.getVendorName());
			}
			if (invoice.getItemName() != null) {
				updateinvoice.setItemName(invoice.getItemName());
			}
			if (invoice.getInvoiceDate() != null) {
				updateinvoice.setInvoiceDate(invoice.getInvoiceDate());
			}
			if (invoice.getPrice() != null) {
				updateinvoice.setPrice(invoice.getPrice());
			}
			if (invoice.getQuantity() != null) {
				updateinvoice.setQuantity(invoice.getQuantity());
			}
			if (invoice.getDiscount() != null) {
				updateinvoice.setDiscount(invoice.getDiscount());
			}
			if (invoice.getPaymentStatus() != null) {
				updateinvoice.setPaymentStatus(invoice.getPaymentStatus());
			}
			if (invoice.getTotal() != null) {
				updateinvoice.setTotal(invoice.getTotal());
			}
			if (invoice.getGstNo() != null) {
				updateinvoice.setGstNo(invoice.getGstNo());
			}
			if (invoice.getCreateDate() != null) {
				updateinvoice.setCreatedBy(invoice.getCreatedBy());
			}
			if (invoice.getUpdatedBy() != null) {
				updateinvoice.setUpdatedBy(invoice.getUpdatedBy());
			}
			if (invoice.getInventoryCode() != null) {
				updateinvoice.setInventoryCode(invoice.getInventoryCode());
			}
			invoiceRepo.save(updateinvoice);
			return updateinvoice;
		} else {
			return null;
		}
	}
}