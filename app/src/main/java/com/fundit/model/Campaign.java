package com.fundit.model;

/**
 * Created by Nivida new on 24-Jul-17.
 */

public class Campaign extends ProductListResponse.Product {

    String fundspot_id = "";
    String product_id = "";
    String status = "";
    String action_status = "";

    public String getFundspot_id() {
        return fundspot_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getStatus() {
        return status;
    }

    public String getAction_status() {
        return action_status;
    }
}
