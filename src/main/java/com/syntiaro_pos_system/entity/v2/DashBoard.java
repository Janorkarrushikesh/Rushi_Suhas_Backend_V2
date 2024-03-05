package com.syntiaro_pos_system.entity.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "DashBoard")
public class DashBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "StoreName")
    private String storeName;

    @Column(name = "StoreId")
    private Long storeId;

    @Column(name = "CreatedDate")
    private String createdDate;

    @Column(name = "CreatedBy")
    private String createdBy;

    @Column(name = "UpdatedDate")
    private String updatedDate;

    @Column(name = "UpdatedBy")
    private String updatedBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "quickaccess_id", referencedColumnName = "id")
    private List<QuickAccess> quickaccess;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "appmenu_id", referencedColumnName = "id")
    private List<AppMenu> appMenu;

    public DashBoard(List<QuickAccess> quickAccessItems, List<AppMenu> appMenuItems) {
        this.quickaccess = quickAccessItems;
        this.appMenu = appMenuItems;
    }

    @PostPersist
    public void genereateDate() {
        Date date = new Date();
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormate.format(date);
        this.createdDate = formattedDate;
        this.updatedDate = formattedDate;

    }

}
