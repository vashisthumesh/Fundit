package com.fundit.model;

/**
 * Created by prince on 7/17/2017.
 */

public class RegisterResponse extends AppModel {

    Data data=new Data();

    public Data getData() {
        return data;
    }

    public class Data{
        String user_id="";

        public String getUser_id() {
            return user_id;
        }
    }
}
