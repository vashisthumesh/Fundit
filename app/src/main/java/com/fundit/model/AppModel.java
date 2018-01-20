package com.fundit.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

/**
 * Created by Nivida new on 17-Jul-17.
 */

public class AppModel {
    boolean status=false;
    String message="";
    String user_id="";


    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUser_id() {
        return user_id;
    }


}
