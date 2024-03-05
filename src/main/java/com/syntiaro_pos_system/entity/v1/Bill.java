package com.syntiaro_pos_system.entity.v1;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


//@NamedQuery(name="Bill.getAllBill", query = ("select new com.SYNTIARO_POS_SYSTEM.Response.BillResponse (b.id,b.billdate,b.paymentmode,b.email,b.contact,b.tranid, b.total , o.orddate ,o.tblno, o.ordstatus ,o.oid) from com.SYNTIARO_POS_SYSTEM.Entity.Bill b JOIN b.order o"))

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "Bill")
public class Bill implements Serializable {  // ALL TABEL AND FILED MENTION
    // ----ADDED NEW CODE-----BY RUSHIKESH
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "Serial_no"
    private Integer SerialNo;
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Bill_No")
    private Integer BillId;

    @Column(name = "Bill_date")
    private LocalDate billDate = LocalDate.now();
    // @Size(max = 10, min = 10)
    @Column(name = "contact")
    private String contact;
    @Column(name = "update_Date")
    private String updateDate;
    @Column(name = "crtdate")
    private String createDate;
    @Column(name = "upbyname")
    private String updatedBy;
    @Column(name = "crtbyname")
    private String createdBy;
    @Column(name = "payment_mode")
    private String paymentMode;
    @Column(name = "transaction_id")
    private String transactionId;
    @Column(name = "gst")
    private String gst;
    @Column(name = "discount")
    private Integer discount;
    @Column(name = "total")
    private Float total;
    @Column(name = "store_id")
    private Integer storeId;
    // Bill Status added  to know the bill type that is kot/quick bill/ placed order
    @Column(name = "bill_status")
    private String billStatus;

    // FOR ORDER AND BILL TABEL JOIN
    @OneToMany(targetEntity = Orders.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Bill_no_fk", referencedColumnName = "SerialNo")
    private List<Orders> order;

    public Bill(LocalDate date, Float totalBalance, String paymentMode, Integer store_id) {
        this.billDate = date;
        this.total = totalBalance;
        this.paymentMode = paymentMode;
        this.storeId = store_id;
    }

    public Integer getBillId() {
        return BillId;
    }

    public void setBillId(Integer billId) {
        this.BillId = billId;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public List<Orders> getOrder() {
        return order;
    }

    public void setOrder(List<Orders> order) {
        this.order = order;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.updateDate = formattedDate;
        this.createDate = formattedDate;
    }
}
