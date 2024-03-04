package com.syntiaro_pos_system.entity.v1;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


//@NamedQuery(name="Orders.getAllList", query = ("select new com.SYNTIARO_POS_SYSTEM.Response.OrderResponse (o.id,o.orddate,o.tblno,o.ordstatus) from com.SYNTIARO_POS_SYSTEM.Entity.Orders o"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "Orders")
public class Orders implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer SerialNo;

    @Column(name = "Order_Id")
    private int orderId;

    @Column(name = "Order_Date")
    private LocalDate orderDate = LocalDate.now();


    @Column(name = "tblno")
    private String tableNo;

    @Column(name = "Order_Status")
    private String orderStatus;

    @Column(name = "ordertype")
    private String orderType;

    @Column(name = "Create_Date")
    private String createdDate;

    @Column(name = "Update_Date")
    private String updatedDate;

    @Column(name = "Create_By_Name")
    private String createdName;

    @Column(name = "Update_by_Name")
    private String updatedBy;

    @Column(name = "Store_Id")
    private String storeId;


    @OneToMany(targetEntity = OrderFood.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_idfk", referencedColumnName = "SerialNo")
    private List<OrderFood> orderFoods;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<OrderFood> getOrderFoods() {
        return orderFoods;
    }

    public void setOrderFoods(List<OrderFood> orderFoods) {
        this.orderFoods = orderFoods;
    }


    public void setQuantity(int requestedQuantity) {
    }

    public void setOrderDate(LocalDate calculationDate) {
    }

    @PostPersist
    public void generateStoreCode() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = dateFormat.format(date);
        this.updatedDate = formattedDate;
        this.createdDate = formattedDate;
        // this.orddate = formattedDate;
    }

    public LocalDate getOrderDate() {return orderDate;}
}
