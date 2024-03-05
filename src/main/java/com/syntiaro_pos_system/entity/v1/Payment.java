package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

//@NamedQuery(name = "Payment.getAllPayment",query = "Select new com.SYNTIARO_POS_SYSTEM.Response.PaymentWrapper(p.payment_id, p.store_id, p.vendor_name, p.payment_date, p.payment_mode, p.due_date, v.vendor_address, v.mobile_no, v.bank_name, v.Branch, v.account_no, v.IFSC_code, v.UPI_id)from Payment p JOIN p.vendor v")
@Entity
@Table(name = "Payment")
@Data
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SerialNo;


    @Column(name = "Payment_id", length = 45)
    private Integer paymentId;

    @Column(name = "store_id", length = 255)
    private Integer storeId;

    @Column(name = "Vendor_name")
    private String vendorName;

    @Column(name = "payment_date")
    private String paymentDate;

    @Column(name = "gst")
    private String gst;


    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch")
    private String branch;

    @Column(name = "account_no", length = 255)
    private String accountNo;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "upi_id")
    private String upiId;

    @Column(name = "total")
    private String total;

    @Column(name = "creatby")
    private String createdBy;

    @Column(name = "updateby")
    private String updatedBy;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "update_date")
    private String updateDate;

    @Column(name = "payment_status")
    private String paymentStatus;


    @OneToMany(targetEntity = Vendor.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id_fk", referencedColumnName = "SerialNo")
    private List<Vendor> vendor;

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<Vendor> getVendor() {
        return vendor;
    }

    public void setVendor(List<Vendor> vendor) {
        this.vendor = vendor;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.paymentDate = formattedDate;
        this.updateDate = formattedDate;
        this.createDate = formattedDate;
    }


}