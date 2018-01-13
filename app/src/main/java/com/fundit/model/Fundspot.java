package com.fundit.model;

import java.io.Serializable;

/**
 * Created by prince on 7/17/2017.
 */

public class Fundspot implements Serializable {

    String category_id="";
    String fundraise_split="";
    String description="";
    String id="";
    String user_id="";
    String state_id="";
    String city_id="";
    String location="";
    String zip_code="";
    String associated_organization="";
    String associated_fundspot="";
    String contact_info="";
    String image="";
    String title="";
    String first_name="";
    String last_name="";
    String organization_id="";
    String fundspot_id = "";
    String fundspot_percent = "";
    String organization_percent = "";
    String campaign_duration = "";
    String max_limit_of_coupon_price = "";
    String coupon_expire_day = "";
    String Address="";
    String contact_info_email="";
    String contact_info_mobile="";


   City City = new City();
    State State = new State();



    public String getCategory_id() {
        return category_id;
    }

    public String getFundraise_split() {
        return fundraise_split;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public String getContact_info_email() {
        return contact_info_email;
    }

    public String getContact_info_mobile() {
        return contact_info_mobile;
    }

    public String getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getState_id() {
        return state_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getAssociated_organization() {
        return associated_organization;
    }

    public String getAssociated_fundspot() {
        return associated_fundspot;
    }

    public String getContact_info() {
        return contact_info;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getOrganization_id() {
        return organization_id;
    }

    public String getFundspot_id() {
        return fundspot_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFundspot_percent() {
        return fundspot_percent;
    }

    public String getOrganization_percent() {
        return organization_percent;
    }

    public String getCampaign_duration() {
        return campaign_duration;
    }

    public String getMax_limit_of_coupon_price() {
        return max_limit_of_coupon_price;
    }

    public String getCoupon_expire_day() {
        return coupon_expire_day;
    }

    public Fundspot.City getCity() {
        return City;
    }

    public String getAddress() {
        return Address;
    }

    public Fundspot.State getState() {
        return State;
    }



    public class City implements Serializable{

        String id = "";
        String name = "";
        String state_code="";

        public String getState_code() {
            return state_code;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class State implements Serializable{

        String id = "";
        String name = "";
        String state_code="";

        public String getState_code() {
            return state_code;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}
