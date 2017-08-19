package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL-17 on 09-Aug-17.
 */

public class MultipleProductResponse implements Serializable{


    String campaign_id = "";
    String product_id = "";
    String price = "";
    String name = "";
    String description = "";
    String fine_print = "";
    String image = "";
    String type_id = "";
    String total_price="";
    boolean isChecked = false;
    int qty = 0;

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFine_print() {
        return fine_print;
    }

    public String getImage() {
        return image;
    }

    public String getType_id() {
        return type_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFine_print(String fine_print) {
        this.fine_print = fine_print;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }
}
