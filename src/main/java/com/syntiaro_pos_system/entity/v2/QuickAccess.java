package com.syntiaro_pos_system.entity.v2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "QuickAccess")
public class QuickAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private String icon;

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
