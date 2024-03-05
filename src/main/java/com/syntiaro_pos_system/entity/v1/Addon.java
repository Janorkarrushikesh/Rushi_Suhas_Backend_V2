package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//@NamedQuery(name="Addon.getAddon",query="select new com.SYNTIARO_POS_SYSTEM.Response.AddonWrapper(a.itemid,a.itemname,a.quantity,a.price,a.storeid)from Addon a")

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "addon")
public class Addon {

    @Id
    @Column(name = "item_id", length = 45)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;
    @Column(name = "item_name", length = 255)
    private String itemName;
    @Column(name = "GST_no", length = 255)
    private String gstNo;

    @Column(name = "price", length = 255)
    private String price;

    @Column(name = "update_date", length = 50)
    private String updateDate;

    @Column(name = "update_by", length = 50)
    private String updateBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "store_id", length = 255)
    private String storeId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "addoncode")
    private String addonCode;

    public int getId() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAddonCode() {
        return addonCode;
    }

    public void setAddonCode(String addonCode) {
        this.addonCode = addonCode;
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.updateDate = formattedDate;
        this.createdDate = formattedDate;
    }

}
