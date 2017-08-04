package com.fundit.Bean;

/**
 * Created by NWSPL-17 on 08-Aug-17.
 */

public class GetProductsFundspotBean {

    /*String title = "";
    String fundraise_split = "";
    String description = "";
    String fundspot_percent = "";
    String organization_percent = "";
    String campaign_duration = "";
    String max_limit_of_coupon_price = "";*/
    String productId = "";
    String type_id = "";
    String name = "";
    String productDescription = "";
    String image = "";
    String price = "";
    String fine_print = "";

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFine_print() {
        return fine_print;
    }

    public void setFine_print(String fine_print) {
        this.fine_print = fine_print;
    }
}
