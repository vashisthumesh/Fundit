package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/18/2018.
 */

public class NewsFeedDataModel implements Serializable {


  String id="";
  String msg="";
  String type="";
  String role_id="";
  String user_id="";
  String campaign_id="";
  String fundspot_id="";
  String org_id="";
  String owner_id="";
  String title="";
  String order_id="";
  String zipcode="";
  String location="";
  String created="";
  String modified="";

    public String getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public String getType() {
        return type;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getFundspot_id() {
        return fundspot_id;
    }

    public String getOrg_id() {
        return org_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getTitle() {
        return title;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getLocation() {
        return location;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }
}
