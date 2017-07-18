package com.fundit.model;

/**
 * Created by Nivida new on 18-Jul-17.
 */

public class UniqueEmailResponse extends AppModel {

    EmailData data=new EmailData();

    public EmailData getData() {
        return data;
    }

    public class EmailData{
        int is_verified = 0;
        String user_id="0";

        public String getIs_verified() {
            return String.valueOf(is_verified);
        }

        public String getUser_id() {
            return user_id;
        }
    }

}
