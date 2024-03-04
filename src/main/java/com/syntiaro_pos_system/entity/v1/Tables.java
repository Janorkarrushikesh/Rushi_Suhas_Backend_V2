package com.syntiaro_pos_system.entity.v1;

import javax.persistence.*;

@Entity
@Table(name = "Tables")
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long serialNo;
  private String tableName;

  private long chair;

  private String storeid;


    public Tables(long serial_no, String tablename, long chair, String storeid) {
        this.serialNo = serial_no;
        this.tableName = tablename;
        this.chair = chair;
        this.storeid = storeid;
    }

    public Tables() {
    }

    public long getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(long serialNo) {
        this.serialNo = serialNo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public long getChair() {
        return chair;
    }

    public void setChair(long chair) {
        this.chair = chair;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }
}
