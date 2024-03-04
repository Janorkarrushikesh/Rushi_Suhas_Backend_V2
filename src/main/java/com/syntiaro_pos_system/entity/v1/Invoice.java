package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

//@NamedQuery(name="Invoice.getAllInvoice", query = "Select new com.SYNTIARO_POS_SYSTEM.Response.InvoiceWrapper(i.store_id, i.invoice_id,  i.item_name, i.invoice_date,   i.price, i.Quantity, i.discount, i.invoice_status, i.payment_Status) from Invoice i")
@Entity

@Data
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vendor_inventory")
public class Invoice implements Serializable{

	@Serial
	private static final long SerialVersion =1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Serial_No;

	@Column(name = "invoice_id")
	private Integer invoiceId;

	@Column(name = "store_id", length = 255)
	private Integer storeId;

	@Column(name = "vendor_name")
	private String vendorName;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "invoice_date")
	private Date invoiceDate = new Date();

	@Column(name = "price")
	private String price;

	@Column(name = "quantity")
	private String quantity;

	@Column(name = "discount")
	private String discount;


	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "total")
	private String total;

	@Column(name = "gstno")
	private String gstNo;

	@Column(name = "createby")
	private String createdBy;

	@Column(name = "updtaeby")
	private String updatedBy;

	@Column(name = "create_date")
	private String createDate;

	@Column(name = "update_date")
	private String updateDate;

	@Column(name = "inventory_code")
	private String inventoryCode;

	@Column(name = "unit")
	private String unit;


	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
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

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}


	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getGstNo() {
		return gstNo;
	}

	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
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

	public String getInventoryCode() {
		return inventoryCode;
	}

	public void setInventoryCode(String inventoryCode) {
		this.inventoryCode = inventoryCode;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@PostPersist
	public void generateStoreCode() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		String formattedDate = dateFormat.format(date);
		SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDates = dateFormats.format(date);
		this.updateDate = formattedDate;
		this.createDate = formattedDates;
	}
}
	

