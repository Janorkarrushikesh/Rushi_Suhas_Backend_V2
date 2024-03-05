package com.syntiaro_pos_system.serviceimpl.v2;

import com.syntiaro_pos_system.entity.v1.Payment;
import com.syntiaro_pos_system.entity.v2.ApiResponse;
import com.syntiaro_pos_system.repository.v2.PaymentRepository;
import com.syntiaro_pos_system.service.v2.PaymentService;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentRepository paymentRepository;


    @Override
    public ResponseEntity<ApiResponse> getPaymentByStoreId(Integer storeId, Integer page, Integer size, String startDate, String endDate) {
        try {
            List<Payment> existingPayment = paymentRepository.findByStoreId(storeId);
            if (existingPayment.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Data Not found for given Id", 404));
            } else {
                if (page != null && size != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByPageAndSize(storeId, page, size), true, 200));
                } else if (startDate != null && endDate != null) {
                    return ResponseEntity.ok().body(new ApiResponse(getByDate(storeId, startDate, endDate), true, 200));
                } else return ResponseEntity.ok().body(new ApiResponse(existingPayment, true, 200));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }
    }

    private Object getByDate(Integer storeId, String startDate, String endDate) {
        List<Payment> existingPayment = paymentRepository.findBetweenDate(storeId, startDate, endDate);
        List<Map<String, Object>> listMap = new ArrayList<>();
        if (existingPayment != null) {
            Map<String, Object> paymentMap = new HashMap<>();
            for (Payment payment : existingPayment) {
                paymentMap.put("id", payment.getPaymentId());
                paymentMap.put("paymentDate", payment.getPaymentDate());
                paymentMap.put("vendorName", payment.getVendorName());
                paymentMap.put("paymentMode", payment.getPaymentMode());
                paymentMap.put("paymentStatus", payment.getPaymentStatus());
                paymentMap.put("dueDate", payment.getPaymentMode());
                paymentMap.put("bankName", payment.getBankName());
                paymentMap.put("branch", payment.getBranch());
                paymentMap.put("accountNo", payment.getAccountNo());
                paymentMap.put("ifscCode", payment.getIfscCode());
                paymentMap.put("upi", payment.getUpiId());
                paymentMap.put("storeId", payment.getStoreId());
                paymentMap.put("gst", payment.getGst());
                paymentMap.put("total", payment.getTotal());
                listMap.add(paymentMap);
            }
            return listMap;
        }
        return "Not Found ";
    }

    private Object getByPageAndSize(Integer storeId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Payment> pageList = paymentRepository.findByStoreId(storeId, pageable);
        List<Map<String, Object>> listMap = new LinkedList<>();
        if (pageList != null) {
            Map<String, Object> paymentMap = new HashMap<>();
            for (Payment payment : pageList) {
                paymentMap.put("id", payment.getPaymentId());
                paymentMap.put("paymentDate", payment.getPaymentDate());
                paymentMap.put("vendorName", payment.getVendorName());
                paymentMap.put("paymentMode", payment.getPaymentMode());
                paymentMap.put("paymentStatus", payment.getPaymentStatus());
                paymentMap.put("dueDate", payment.getPaymentMode());
                paymentMap.put("bankName", payment.getBankName());
                paymentMap.put("branch", payment.getBranch());
                paymentMap.put("accountNo", payment.getAccountNo());
                paymentMap.put("ifscCode", payment.getIfscCode());
                paymentMap.put("upi", payment.getUpiId());
                paymentMap.put("storeId", payment.getStoreId());
                paymentMap.put("gst", payment.getGst());
                paymentMap.put("total", payment.getTotal());
                listMap.add(paymentMap);
            }
            return listMap;
        }
        return "not found";

    }

    @Override
    public ResponseEntity<ApiResponse> getPaymenyById(Integer serialNo) {
        try {

            Optional<Payment> paymentlist = paymentRepository.findById(serialNo);
            if (paymentlist.isPresent()) {
                return ResponseEntity.ok(new ApiResponse(paymentlist.get(), true, 200));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(null, false, "Payment Detail Not Found", 404));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(null, false, "...", 500));
        }

    }

    @Override
    public ResponseEntity<byte[]> qrCodeForPayment(Integer SerialNo, HttpServletResponse response) {
        try {
            String upiId = getUpiIdForPayment(SerialNo);
            byte[] qrCodeBytes = QRCode.from("upi://pay?pa=" + upiId).to(ImageType.PNG).stream().toByteArray();
            response.setContentType("image/png");
            response.setContentLength(qrCodeBytes.length);
            response.getOutputStream().write(qrCodeBytes);
            response.getOutputStream().flush();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private String getUpiIdForPayment(Integer SerialNo) {
        Payment payment = paymentRepository.findById(SerialNo).orElse(null);
        return payment.getUpiId();
    }

    @Override
    public ResponseEntity<ApiResponse> savePayment(Payment payment) {
        try {
            Integer lastPaymentId = paymentRepository.findLastNumberForStore(payment.getStoreId());
            payment.setPaymentId(lastPaymentId != null ? lastPaymentId + 1 : 1);
            return ResponseEntity.ok().body(new ApiResponse(paymentRepository.save(payment), true, 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> deletePaymentById(Integer serialNo) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(serialNo);
            if (existingPayment.isPresent()) {
                paymentRepository.deleteById(serialNo);
                return ResponseEntity.ok().body(new ApiResponse(null, true, "deleted Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

    @Override
    public ResponseEntity<ApiResponse> updatePayment(Integer serialNo, Payment payment) {
        try {
            Optional<Payment> existingPayment = paymentRepository.findById(serialNo);
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
                if (payment.getPaymentDate() != null) {
                    updatepayment.setPaymentDate(payment.getPaymentDate());
                }
                return ResponseEntity.ok().body(new ApiResponse(paymentRepository.save(updatepayment), true, "Updated Successfully", 200));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(null, false, "Id Not Found", 404));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(null, false, "....", 500));
        }
    }

}
