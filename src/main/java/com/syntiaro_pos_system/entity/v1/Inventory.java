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
//@NamedQuery(name="Inventory.getAllInventory" , query="select new com.SYNTIARO_POS_SYSTEM.Response.InventoryWrapper(i.id ,i.inventorydate ,i.quantity ,i.productname ,i.category,i.price ,i.expirydate) from  Inventory i")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "inventory")
public class Inventory implements Serializable{


// ----ADDED NEW CODE-----BY RUSHIKESH
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SerialNo ;

    @Column(name = "id")
    private Integer id;

    @Column(name = "quantity")
    private Float quantity;


    @Column(name = "name")
    private String name;

    @Column(name ="inventory_date")
    private Date inventorydate = new Date();

    @Column(name = "category")
    private String category;


    @Column(name = "price")
    private Integer price;

    @Column(name = "expirydate")
    private LocalDate expiryDate;

    @Column(name = "minlevel")
    private Integer minLevel;

    @Column(name = "minlevelunit")
    private String minLevelUnit;

    @Column(name = "updatedate")
    private String updateDate;

    @Column(name = "createddate")
    private String createdDate;

    @Column(name = "createdby")
    private String createdBy;

    @Column(name = "updatedby")
    private String updatedBy;

    @Column(name = "storeid")
    private String storeId;

    @Column(name = "unit")
    private String unit;

    @Column(name = "total")
    private Float total;

    @Column(name="gstno")
    private String gstNo;

    @Column(name="inventory_code")
    private String inventoryCode;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getInventorydate() {
        return inventorydate;
    }

    public void setInventorydate(Date inventorydate) {
        this.inventorydate = inventorydate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public String getMinLevelUnit() {
        return minLevelUnit;
    }

    public void setMinLevelUnit(String minLevelUnit) {
        this.minLevelUnit = minLevelUnit;
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getInventoryCode() {
        return inventoryCode;
    }

    public void setInventoryCode(String inventoryCode) {
        this.inventoryCode = inventoryCode;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDates = dateFormats.format(date);
        this.updateDate = formattedDate;
        this.createdDate = formattedDates;
    }
}







