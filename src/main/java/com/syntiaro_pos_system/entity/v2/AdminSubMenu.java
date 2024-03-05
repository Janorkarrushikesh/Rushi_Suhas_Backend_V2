package com.syntiaro_pos_system.entity.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "AdminSubMenu")
public class AdminSubMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Admin_SubMenuId")
    private Long adminSubMenuId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Path")
    private String path;

    @Column(name = "icon")
    private String icon;

    @Column(name = "StoreId")
    private Long storeId;

    @Column(name = "Status")
    private Boolean status;

    @Column(name = "CreatedDate")
    private String createdDate;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "UpdatedDate")
    private String updatedDate;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @PostPersist
    public void genereateDate() {
        Date date = new Date();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormate.format(date);
        this.createdDate = formattedDate;
        this.updatedDate = formattedDate;

    }

}
