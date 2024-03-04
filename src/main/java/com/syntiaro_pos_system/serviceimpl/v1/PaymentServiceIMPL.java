package com.syntiaro_pos_system.serviceimpl.v1;

import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.repository.v1.PaymentRepo;
import com.syntiaro_pos_system.service.v1.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceIMPL implements PaymentService {


	@Autowired
	private PaymentRepo paymentRepo;

	//THIS METHOD IS USE FOR ADD PAYMENT
	@Override
	public String addPayment(Payment payment) {

		Integer lastBillNumber = paymentRepo.findLastNumberForStore(payment.getStoreId());
		System.out.println("pay" +payment.getStoreId());
		System.out.println("hi" +lastBillNumber);
		payment.setPaymentId(lastBillNumber != null ? lastBillNumber + 1 : 1);
		paymentRepo.save(payment);
		return payment.getVendorName();
	}

	//THIS METHOD IS USE FOR GET LIST OF PAYMENT
	@Override
	public List<Payment> getPayment() {
		return paymentRepo.findAll();
	}

    //THIS METHOD IS USE FOR UPDATE PAYMENT
	@Override
	public Payment updatedPayment(Payment payment) {
		paymentRepo.save(payment);
		return payment;
	}

	public Payment getPaymentById(Integer Serial_no) {
		return paymentRepo.findByPaymentId(Serial_no);
	}


	// THIS METHOD IS USE FOR FETCH PAYMENT BY STOREID
	@Override
	public List<Payment> fetchPaymentsByStoreId(Integer storeId) {
		return paymentRepo.findByStore_id(storeId);
	}

	// THIS METHOD IS USE FOR UPDATE PAYMENT
	@Override
	public Payment updatePayment(Integer payment_id, Payment payment) {
		Optional<Payment> existingPayment = paymentRepo.findById((int) Integer.parseInt(String.valueOf((payment_id))));
		if (existingPayment.isPresent()) {
			Payment updatepayment = existingPayment.get();

			if (payment.getStoreId() != null) {
				updatepayment.setStoreId(payment.getStoreId());
			}
			if (payment.getVendorName() != null) {
				updatepayment.setVendorName(payment.getVendorName());
			}
			if (payment.getGst() != null) {
				updatepayment.setGst(payment.getGst());
			}
			if (payment.getPaymentMode() != null) {
				updatepayment.setPaymentMode(payment.getPaymentMode());
			}
			if (payment.getDueDate() != null) {
				updatepayment.setDueDate(payment.getDueDate());
			}
			if (payment.getBankName() != null) {
				updatepayment.setBankName(payment.getBankName());
			}
			if (payment.getBranch() != null) {
				updatepayment.setBranch(payment.getBranch());
			}
			if (payment.getAccountNo() != null) {
				updatepayment.setAccountNo(payment.getAccountNo());
			}
			if (payment.getIfscCode() != null) {
				updatepayment.setIfscCode(payment.getIfscCode());
			}
//			if (payment.getUpi_id() != null) {
//				updatepayment.setUpi_id("upi://pay?pa="+payment.getUpi_id());
//			}

			if (payment.getUpiId() != null) {
				updatepayment.setUpiId(payment.getUpiId());
			}

			if (payment.getTotal() != null) {
				updatepayment.setTotal(payment.getTotal());
			}
			if (payment.getCreatedBy() != null) {
				updatepayment.setCreatedBy(payment.getCreatedBy());
			}
			if (payment.getUpdatedBy() != null) {
				updatepayment.setUpdatedBy(payment.getUpdatedBy());
			}
			if (payment.getPaymentStatus() != null) {
				updatepayment.setPaymentStatus(payment.getPaymentStatus());
			}
			if(payment.getPaymentDate()!= null){
				updatepayment.setPaymentDate(payment.getPaymentDate());
			}

			paymentRepo.save(updatepayment);
			return updatepayment;
		} else {
			return null;
		}
	}


	@Override
	public Payment PaymentGatway(Integer payment_id, Payment payment) {
		Optional<Payment> existingPayment = paymentRepo.findById((int) Integer.parseInt(String.valueOf((payment_id))));
		if (existingPayment.isPresent()) {
			Payment updatepayment = existingPayment.get();

			if (payment.getStoreId() != null) {
				updatepayment.setStoreId(payment.getStoreId());
			}
			if (payment.getVendorName() != null) {
				updatepayment.setVendorName(payment.getVendorName());
			}
			if (payment.getGst() != null) {
				updatepayment.setGst(payment.getGst());
			}
			if (payment.getPaymentMode() != null) {
				updatepayment.setPaymentMode(payment.getPaymentMode());
			}
			if (payment.getDueDate() != null) {
				updatepayment.setDueDate(payment.getDueDate());
			}
			if (payment.getBankName() != null) {
				updatepayment.setBankName(payment.getBankName());
			}
			if (payment.getBranch() != null) {
				updatepayment.setBranch(payment.getBranch());
			}
			if (payment.getAccountNo() != null) {
				updatepayment.setAccountNo(payment.getAccountNo());
			}
			if (payment.getIfscCode() != null) {
				updatepayment.setIfscCode(payment.getIfscCode());
			}
			if (payment.getTotal() != null) {
				updatepayment.setTotal(payment.getTotal());
			}
			if (payment.getCreatedBy() != null) {
				updatepayment.setCreatedBy(payment.getCreatedBy());
			}
			if (payment.getUpdatedBy() != null) {
				updatepayment.setUpdatedBy(payment.getUpdatedBy());
			}
			if (payment.getPaymentStatus() != null) {
				updatepayment.setPaymentStatus(payment.getPaymentStatus());
			}

			paymentRepo.save(updatepayment);
			return updatepayment;
		} else {
			return null;
		}
	}

	/*-------THIS METHOD IS USE FOR UPDATE VENDOR PAYMENT--------*/
	/*---------CHANGES MADE BY TRUPTI-------*/
	@Override
	public ResponseEntity<?> updateVendorPaymentWithValidation(Long payment_id, Payment payment) {
		Optional<Payment> existingPaymentOpt = paymentRepo.findById(Math.toIntExact(payment_id));
		if (existingPaymentOpt.isPresent()) {
			Payment existingPayment = existingPaymentOpt.get();
			Integer storeId = existingPayment.getStoreId();

			// Check if the updated account number already exists for another vendor in the same store
			if (payment.getAccountNo() != null && !payment.getAccountNo().equals(existingPayment.getAccountNo())) {
				Payment existingVendorPaymentWithSameAccount = paymentRepo.findByStoreIdAndAccountNo(storeId, payment.getAccountNo());
				if (existingVendorPaymentWithSameAccount != null) {
					return ResponseEntity.badRequest().body("Account number already exists !!!");
				}
				existingPayment.setAccountNo(payment.getAccountNo());
			}
			// Check if the updated UPI ID already exists for another vendor in the same store
			if (payment.getUpiId() != null && !payment.getUpiId().equals(existingPayment.getUpiId())) {
				Payment existingVendorPaymentWithSameUpiId = paymentRepo.findByStoreIdAndUpiId(storeId, payment.getUpiId());
				if (existingVendorPaymentWithSameUpiId != null) {
					return ResponseEntity.badRequest().body("UPI ID already exists !!!!");
				}
				existingPayment.setUpiId(payment.getUpiId());
			}

			if (payment.getVendorName() != null) {
				existingPayment.setVendorName(payment.getVendorName());
			}
			if (payment.getPaymentMode() != null) {
				existingPayment.setPaymentMode(payment.getPaymentMode());
			}

			if (payment.getStoreId() != null) {
				existingPayment.setStoreId(payment.getStoreId());
			}
			if (payment.getBranch() != null) {
				existingPayment.setBranch(payment.getBranch());
			}
			if (payment.getBankName() != null) {
				existingPayment.setBankName(payment.getBankName());
			}
			if (payment.getPaymentDate() != null) {
				existingPayment.setPaymentDate(payment.getPaymentDate());
			}
			if (payment.getIfscCode() != null) {
				existingPayment.setIfscCode(payment.getIfscCode());
			}
			if (payment.getTotal() != null) {
				existingPayment.setTotal(payment.getTotal());
			}
			if (payment.getDueDate() != null) {
				existingPayment.setDueDate(payment.getDueDate());
			}
			// Save the changes if any
			paymentRepo.save(existingPayment);
			return ResponseEntity.ok(existingPayment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
//    /*----------TRUPTI CODE END HERE-------*/


}
