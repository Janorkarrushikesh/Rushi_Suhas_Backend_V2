package com.syntiaro_pos_system.entity.v1;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Floor_Table")
public class FloorTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serialNo;

    private String floorname;

    @OneToMany(targetEntity = Tables.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "floor_table_id", referencedColumnName = "serialNo")
    private List<Tables> tables;

    @Column(name = "store_id")
    private long storeid;

    public FloorTable() {
    }

    public FloorTable(long serialNo, String floorname, List<Tables> tables, long storeid) {
        this.serialNo = serialNo;
        this.floorname = floorname;
        this.tables = tables;
        this.storeid = storeid;
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public String getFloorname() {
        return floorname;
    }

    public void setFloorname(String floorname) {
        this.floorname = floorname;
    }

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public long getStoreid() {
        return storeid;
    }

    public void setStoreid(long storeid) {
        this.storeid = storeid;
    }
}
