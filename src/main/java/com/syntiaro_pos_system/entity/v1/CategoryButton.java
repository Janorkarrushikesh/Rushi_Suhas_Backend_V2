package com.syntiaro_pos_system.entity.v1;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "CategoryButton")
public class CategoryButton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String Name;

    private String storeId;

    @Column(name = "update_date", length = 50)
    private String updateDate;

    @Column(name = "update_by", length = 50)
    private String updateBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "created_by")
    private String createdBy;



    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.updateDate = formattedDate;
        this.createdDate = formattedDate;
    }
}
