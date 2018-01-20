package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/18/2018.
 */

public class News_campaign implements Serializable {

    String   id="";
    String   title="";
    String   description="";
    String   user_id="";
    String   receiver_id="";
    String   role_id="";
    String   fundspot_percent="";
    String   organization_percent="";
    String   campaign_duration="";
    String   max_limit_of_coupons="";
    String   coupon_expire_day="";
    String   start_date="";
    String   end_date="";
    String   all_member="";
    String   message="";
    String   status="";
    String   action_status="";
    String   target_amount="";
    String   msg="";
    String   total_earning="";
    String   total_coupon_sold="";
    String   msg_seller="";
    String   created="";
    String   modified="";


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getFundspot_percent() {
        return fundspot_percent;
    }

    public String getOrganization_percent() {
        return organization_percent;
    }

    public String getCampaign_duration() {
        return campaign_duration;
    }

    public String getMax_limit_of_coupons() {
        return max_limit_of_coupons;
    }

    public String getCoupon_expire_day() {
        return coupon_expire_day;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getAll_member() {
        return all_member;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public String getAction_status() {
        return action_status;
    }

    public String getTarget_amount() {
        return target_amount;
    }

    public String getMsg() {
        return msg;
    }

    public String getTotal_earning() {
        return total_earning;
    }

    public String getTotal_coupon_sold() {
        return total_coupon_sold;
    }

    public String getMsg_seller() {
        return msg_seller;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }
}
