package com.fundit.model;

import java.io.Serializable;

/**
 * Created by prince on 7/17/2017.
 */

public class User implements Serializable {
    String id="";
    String role_id="";
    String title="";
    String email_id="";
    String first_name="";
    String last_name="";
    String status="";
    String otp="";
    String anroid_device_id="";
    String tokenhash="";
    String is_verified="";

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

    public String getStatus() {
        return status;
    }

    public String getOtp() {
        return otp;
    }

    public String getAnroid_device_id() {
        return anroid_device_id;
    }

    public String getTokenhash() {
        return tokenhash;
    }

    public String getIs_verified() {
        return is_verified;
    }
}
