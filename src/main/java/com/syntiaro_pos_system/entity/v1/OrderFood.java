package com.syntiaro_pos_system.entity.v1;

import lombok.Data;

import javax.persistence.*;


//@NamedQuery(name="OrderFood.getOrderFood",query="select new com.SYNTIARO_POS_SYSTEM.Wrapper.FoodWrapper (f.food_id,f.category,f.subcategory,f.food_name,f.quantity,f.store_id,f.image) from com.SYNTIARO_POS_SYSTEM.Entity.OrderFood f")
@Entity
@Data
//@DynamicUpdate
//@DynamicInsert
//@AllArgsConstructor
//@NoArgsConstructor
@Table(name = "orderfood")

public class OrderFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SerialNo;

    @Column(name = "food_id", length = 45)
    private int foodId;

    @Column(name = "food_name", length = 255)
    private String foodName;

    @Column(name = "category", length = 255)
    private String category;

    @Column(name = "subcategory", length = 255)
    private String subCategory;

    @Column(name = "store_id", length = 255)
    private String storeId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private int price;

    public OrderFood(String foodName, int quantity) {
        this.foodName = foodName;
        this.quantity = quantity;
    }

    public OrderFood() {
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
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

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

