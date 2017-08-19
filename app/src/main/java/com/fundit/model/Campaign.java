package com.fundit.model;

import java.io.Serializable;

/**
 * Created by Nivida new on 24-Jul-17.
 */

public class Campaign extends ProductListResponse.Product implements Serializable {

    String title="";
    String receiver_id = "";
    String product_id = "";
    String status = "";
    String action_status = "";
    String start_date = "";
    String all_member = "0";
    String review_status = "";
    String target_amount = "";
    String end_date = "";
    String message = "";
    String role_id = "";

    public String getReceiver_id() {
        return receiver_id;
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

    public String getStart_date() {
        return start_date;
    }

    public String getAll_member() {
        return all_member;
    }

    public String getTitle() {
        return title;
    }

    public String getReview_status() {
        return review_status;
    }

    public String getTarget_amount() {
        return target_amount;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getMessage() {
        return message;
    }

    public String getRole_id() {
        return role_id;
    }
}
