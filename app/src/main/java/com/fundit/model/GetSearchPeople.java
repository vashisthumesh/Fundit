package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.GET;

/**
 * Created by NWSPL6 on 10/16/2017.
 */

public class GetSearchPeople extends  AppModel {
    List<People> data=new ArrayList<>();

    public List<People> getData() {
        return data;
    }
    public class People implements Serializable{
        String id="";
        String user_id="";
        String first_name="";
        String last_name="";
        String state_id="";
        String city_id="";
        String location="";
        String zip_code="";
        String organization_id="";
        String fundspot_id="";
        String contact_info="";
        String image="";
        String status="";
        String redeemer="";
        String contact_info_mobile="";
        String contact_info_email="";
        String created="";
        String modified="";
        String email_id="";

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getState_id() {
            return state_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getLocation() {
            return location;
        }

        public String getZip_code() {
            return zip_code;
        }

        public String getOrganization_id() {
            return organization_id;
        }

        public String getFundspot_id() {
            return fundspot_id;
        }

        public String getContact_info() {
            return contact_info;
        }

        public String getImage() {
            return image;
        }

        public String getStatus() {
            return status;
        }

        public String getRedeemer() {
            return redeemer;
        }

        public String getContact_info_mobile() {
            return contact_info_mobile;
        }

        public String getContact_info_email() {
            return contact_info_email;
        }

        public String getCreated() {
            return created;
        }

        public String getModified() {
            return modified;
        }


        public String getEmail_id() {
            return email_id;
        }
    }
}
