package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL6 on 1/31/2018.
 */

public class FundRequest  extends  AppModel {

    List<FundRequest_Data> data=new ArrayList<>();

    public List<FundRequest_Data> getData() {
        return data;
    }

    public class FundRequest_Data implements Serializable{

            String id="";
            String title="";
            String user_id="";
            String role_id="";
            String state="";
            String city_name="";
            String location="";
            String zip_code="";
            String image="";

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getRole_id() {
            return role_id;
        }

        public String getState() {
            return state;
        }

        public String getCity_name() {
            return city_name;
        }

        public String getLocation() {
            return location;
        }

        public String getZip_code() {
            return zip_code;
        }

        public String getImage() {
            return image;
        }
    }



}
