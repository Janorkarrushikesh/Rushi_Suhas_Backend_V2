package com.syntiaro_pos_system.entity.v2;

import javax.persistence.*;
import lombok.*;
import javax.validation.constraints.NotNull;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//@Entity
//@Table(name = "Food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Serial_Number")
    private Long serialNumber;

    @Column(name = "Food_Number")
    private Integer foodNumber;

    @NotNull(message = "Please enter the food name")
    @Column(name = "Food_Name")
    private String foodName;

    @NotNull(message = "Please enter the food category")
    @Column(name = "Food_Category")
    private String foodCategory;

    @NotNull(message = "Please enter the food subcategory")
    @Column(name = "Food_Subcategory")
    private String foodSubCategory;

    @Column(name = "Food_updatedate")
    private String foodUpdateDate;

    @Column(name = "Food_updatedBy")
    private String foodUpdatedBy;

    @Column(name = "Food_CreatedDate")
    private String foodCreatedDate;

    @Column(name = "Food_CreatedBy")
    private String foodCreatedBy;

    @NotNull
    @Column(name = "Store_Id")
    private Long storeId;

    @Column(name = "Food_Image")
    private String FoodImage;

    @Column(name = "Food_Description")
    private String FoodDescription;

    @NotNull(message = "Please enter the Food Price")
    @Column(name = "Food_Price")
    private Integer foodPrice;

    @Column(name = "Food_Code")
    private String foodCode;


}
