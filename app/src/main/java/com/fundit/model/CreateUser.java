package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/18/2018.
 */

public class CreateUser implements Serializable {
  String  id="";
  String  role_id="";
  String  title="";
  String email_id="";
  String first_name="";
  String last_name="";
  String password="";
  String status="";
  String sort_order="";
  String otp="";
  String anroid_device_id ="";
  String ios_device_id="";
  String tokenhash ="";
  String reset_date ="";
  String is_verified="";
  String is_subscribe="";
  String profile_status="";
  String requested_user_name="";
  String created="";
  String modified="";


    public String getId() {
        return id;
    }

    public String getRole_id() {
        return role_id;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }

    public String getSort_order() {
        return sort_order;
    }

    public String getOtp() {
        return otp;
    }

    public String getAnroid_device_id() {
        return anroid_device_id;
    }

    public String getIos_device_id() {
        return ios_device_id;
    }

    public String getTokenhash() {
        return tokenhash;
    }

    public String getReset_date() {
        return reset_date;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public String getIs_subscribe() {
        return is_subscribe;
    }

    public String getProfile_status() {
        return profile_status;
    }

    public String getRequested_user_name() {
        return requested_user_name;
    }

    public String getCreated() {
        return created;
    }

    public String getModified() {
        return modified;
    }
}
