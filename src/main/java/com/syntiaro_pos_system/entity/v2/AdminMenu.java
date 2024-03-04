package com.syntiaro_pos_system.entity.v2;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "AdminMenu")
@Entity
public class AdminMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Admin_MenuId")
    private Long adminMenuId;

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

    @OneToMany(targetEntity = AdminSubMenu.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "Admin_MenuId_fk", referencedColumnName = "Admin_MenuId")
    private List<AdminSubMenu> adminSubMenu;

    @PostPersist
    public void genereateDate() {
        Date date = new Date();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormate.format(date);
        this.createdDate = formattedDate;
        this.updatedDate = formattedDate;

    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
        if (adminSubMenu != null) {
            for (AdminSubMenu subMenu : adminSubMenu) {
                subMenu.setStoreId(storeId);
            }
        }
    }
}
