package com.fundit.model;

/**
 * Created by prince on 7/29/2017.
 */

public class ForgotPasswordEmailResponse extends AppModel {

    ForgotData data = new ForgotData();

    public ForgotData getData() {
        return data;
    }

    public class ForgotData{
        String user_id="";

        public String getUser_id() {
            return user_id;
        }
    }

}
