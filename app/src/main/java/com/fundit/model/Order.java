package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class Order implements Serializable {

    String id = "";
    String campaign_id = "";
    String invoice_no = "";
    String user_id = "";
    String role_id = "";
    String firstname = "";
    String lastname = "";
    String email = "";
    String mobile = "";
    String payment_address_1 = "";
    String payment_city = "";
    String payment_postcode = "";
    String payment_state = "";
    String payment_method = "";
    String payment_code = "";
    String total = "";
    String organization_id = "";
    String fundspot_id = "";
    String created = "";
    String total_coupon_count = "";
    String coupon_expiry_date="";
    String payment_refno="";
    String order_request = "";

    public String getCoupon_expiry_date() {
        return coupon_expiry_date;
    }

    public String getId() {
        return id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPayment_address_1() {
        return payment_address_1;
    }

    public String getPayment_city() {
        return payment_city;
    }

    public String getPayment_postcode() {
        return payment_postcode;
    }

    public String getPayment_state() {
        return payment_state;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public String getPayment_refno() {
        return payment_refno;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public String getTotal() {
        return total;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public String getFundspot_id() {
        return fundspot_id;
    }

    public String getCreated() {
        return created;
    }

    public String getTotal_coupon_count() {
        return total_coupon_count;
    }

    public String getOrder_request() {
        return order_request;
    }
}
