package com.fundit.model;

/**
 * Created by Nivida6 on 12-02-2018.
 */

public class NotificationCountModel extends AppModel {

    String fundspot_product_count = "";
    String total_unread_msg = "";
    String user_status = "";
    String total_request_count = "";
    String total_member_request_count ="";
    String total_coupon_count = "";
    String is_redeemer = "";
    String is_seller = "";

    public String getFundspot_product_count() {
        return fundspot_product_count;
    }

    public String getTotal_unread_msg() {
        return total_unread_msg;
    }

    public String getUser_status() {
        return user_status;
    }

    public String getTotal_request_count() {
        return total_request_count;
    }

    public String getTotal_member_request_count() {
        return total_member_request_count;
    }

    public String getTotal_coupon_count() {
        return total_coupon_count;
    }

    public String getIs_redeemer() {
        return is_redeemer;
    }

    public String getIs_seller() {
        return is_seller;
    }
}
