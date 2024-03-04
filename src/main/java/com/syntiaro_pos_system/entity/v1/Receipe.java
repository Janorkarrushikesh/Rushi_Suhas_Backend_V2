package com.syntiaro_pos_system.entity.v1;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@Entity
public class Receipe implements Serializable {

    @Serial
    private static final long serialVersionUID =1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Serial_no")
    private Long SerialNo;

    // ----ADDED NEW CODE-----BY RUSHIKESH

    private Long id;

    private String name;
    private String storeId;

    private String createdBy;

    private String updatedBy;

    private String createdDate;

    private String updatedDate;



    @OneToMany(targetEntity = Ingredient.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "ingredients_id",referencedColumnName = "Serial_no")
    private List<Ingredient> ingredients;

    public Long getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(Long serialNo) {
        SerialNo = serialNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
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

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        this.updatedDate = formattedDate;
        this.createdDate = formattedDate;
    }
}
