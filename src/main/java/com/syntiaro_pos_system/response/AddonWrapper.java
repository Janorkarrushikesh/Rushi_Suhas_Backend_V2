package com.syntiaro_pos_system.response;

import lombok.Data;

@Data
public class AddonWrapper {

    private Integer itemid;

    private String itemname;

    private Integer quantity;

    private String price;

    private String storeid;

    public AddonWrapper(Integer itemid, String itemname, Integer quantity, String price, String storeid) {
        this.itemid = itemid;
        this.itemname = itemname;
        this.quantity = quantity;
        this.price = price;
        this.storeid = storeid;
    }
}
