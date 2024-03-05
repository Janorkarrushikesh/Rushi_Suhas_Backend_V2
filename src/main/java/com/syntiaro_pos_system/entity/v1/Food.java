package com.syntiaro_pos_system.entity.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;


//@NamedQuery(name="Food.getFood",query="select new com.SYNTIARO_POS_SYSTEM.Wrapper.FoodWrapper (f.food_id,f.category,f.subcategory,f.food_name,f.quantity,f.store_id,f.image) from com.SYNTIARO_POS_SYSTEM.Entity.Food f")
@Entity
@Data
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Serial_no;

    @Column(name = "food_id")
    private Integer foodId;

    @Column(name = "food_name", length = 255)
    private String foodName;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "subcategory", length = 255)
    private String subCategory;

    @Column(name = "gst_no", length = 255)
    private String gstNo;

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

    @Lob
    @Column(name = "image")
    private String image;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "foodcode")
    private String foodCode;

    @Column(name = "status")
    private Boolean status;

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(String foodCode) {
        this.foodCode = foodCode;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

