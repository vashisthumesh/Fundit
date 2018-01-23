package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/23/2018.
 */

public class App_Single_Organization extends AppModel {

    App_Org_Data data= new App_Org_Data();

    public App_Org_Data getData() {
        return data;
    }

    public class App_Org_Data implements Serializable{

        Org_Data Organization=new Org_Data();
        User_Data User=new User_Data();
        State_Data State =new State_Data();
        City_Data City =new City_Data();

        public User_Data getUser() {
            return User;
        }

        public State_Data getState() {
            return State;
        }

        public City_Data getCity() {
            return City;
        }

        public Org_Data getOrganization() {
            return Organization;
        }

        public  class Org_Data implements Serializable{
        String id="";
        String user_id="";
        String title="";
        String state_id="";
        String city_id="";
        String city_name="";
        String location="";
        String zip_code="";
        String type_id="";
        String sub_type_id="";
        String description="";
        String contact_info="";
        String image="";
        String contact_info_email="";
        String contact_info_mobile="";
        String created="";
        String modified="";


            public String getId() {
                return id;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getTitle() {
                return title;
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

            public String getType_id() {
                return type_id;
            }

            public String getSub_type_id() {
                return sub_type_id;
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

            public String getContact_info_email() {
                return contact_info_email;
            }

            public String getContact_info_mobile() {
                return contact_info_mobile;
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
