package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/23/2018.
 */

public class App_Single_Fundspot extends AppModel {

    App_Fund_Data data= new App_Fund_Data();

    public App_Fund_Data getData() {
        return data;
    }

    public class App_Fund_Data implements Serializable {

        Fund_Data Fundspot=new Fund_Data();
        User_Data User=new User_Data();
        State_Data State =new State_Data();
        City_Data City =new City_Data();


        public Fund_Data getFundspot() {
            return Fundspot;
        }

        public User_Data getUser() {
            return User;
        }

        public State_Data getState() {
            return State;
        }

        public City_Data getCity() {
            return City;
        }

        public  class Fund_Data implements Serializable{
                    String id="";
            String  title="";
            String  user_id="";
            String  state_id="";
            String  city_id="";
            String  city_name="";
            String  location="";
            String  zip_code="";
            String  category_id="";
            String  fundraise_split="";
            String  description="";
            String  contact_info="";
            String  image="";
            String  fundspot_percent="";
            String  organization_percent="";
            String  campaign_duration="";
            String  max_limit_of_coupon_price="";
            String  coupon_expire_day="";
            String contact_info_mobile="";
            String contact_info_email="";
            String split_visibility="";
            String created="";
            String modified="";


            public String getId() {
                return id;
            }

            public String getTitle() {
                return title;
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

            public String getCity_name() {
                return city_name;
            }

            public String getLocation() {
                return location;
            }

            public String getZip_code() {
                return zip_code;
            }

            public String getCategory_id() {
                return category_id;
            }

            public String getFundraise_split() {
                return fundraise_split;
            }

            public String getDescription() {
                return description;
            }

            public String getContact_info() {
                return contact_info;
            }

            public String getImage() {
                return image;
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

            public String getContact_info_mobile() {
                return contact_info_mobile;
            }

            public String getContact_info_email() {
                return contact_info_email;
            }

            public String getSplit_visibility() {
                return split_visibility;
            }

            public String getCreated() {
                return created;
            }

            public String getModified() {
                return modified;
            }
        }


        public class User_Data implements Serializable{

            String id="";
            String role_id="";
            String title="";
            String email_id="";
            String first_name="";
            String last_name="";
            String password="";
            String status="";
            String sort_order="";
            String otp="";
            String anroid_device_id="";
            String ios_device_id="";
            String tokenhash="";
            String reset_date="";
            String is_verified="";
            String is_subscribe="";
            String profile_status="";
            String requested_user_name="";
            String auth_cust_profile_id="";
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

            public String getAuth_cust_profile_id() {
                return auth_cust_profile_id;
            }

            public String getCreated() {
                return created;
            }

            public String getModified() {
                return modified;
            }
        }


        public class State_Data implements Serializable
        {
            String   id="";
            String   name="";
            String  country_id="";
            String  state_code="";
            String sort_order="";
            String status="";
            String created="";
            String modified="";


            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getCountry_id() {
                return country_id;
            }

            public String getState_code() {
                return state_code;
            }

            public String getSort_order() {
                return sort_order;
            }

            public String getStatus() {
                return status;
            }

            public String getCreated() {
                return created;
            }

            public String getModified() {
                return modified;
            }
        }


        public class City_Data implements Serializable
        {
            String  id="";
            String name="";
            String city_code="";
            String state_id="";
            String sort_order="";
            String status="";
            String created="";
            String modified="";


            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getCity_code() {
                return city_code;
            }

            public String getState_id() {
                return state_id;
            }

            public String getSort_order() {
                return sort_order;
            }

            public String getStatus() {
                return status;
            }

            public String getCreated() {
                return created;
            }

            public String getModified() {
                return modified;
            }
        }






    }


}
