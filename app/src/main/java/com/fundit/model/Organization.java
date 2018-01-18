package com.fundit.model;

import java.io.Serializable;

/**
 * Created by prince on 7/17/2017.
 */

public class Organization implements Serializable{

    String type_id="";
    String sub_type_id="";
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
    String contact_info_email="";
    String contact_info_mobile="";
    String city_name="";


  /*  City City = new City();*/
    State State = new State();
   /* Type Type=new Type();
    SubType SubType=new SubType();
*/
    /*public Organization.Type getType() {
        return Type;
    }

    public Organization.SubType getSubType() {
        return SubType;
    }
*/
    public String getType_id() {
        return type_id;
    }

    public String getSub_type_id() {
        return sub_type_id;
    }

    public String getDescription() {
        return description;
    }

    public Organization setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
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

    public String getContact_info_email() {
        return contact_info_email;
    }

    public String getContact_info_mobile() {
        return contact_info_mobile;
    }

    public String getFundspot_id() {
        return fundspot_id;
    }

    public String getCity_name() {
        return city_name;
    }

    /*public Organization.City getCity() {
        return City;
    }*/

    public Organization.State getState() {
        return State;
    }


    public class City implements Serializable{

        String id = "";
        String name = "";
        String state_code="";

        public String getId() {
            return id;
        }

        public String getState_code() {
            return state_code;
        }

        public String getName() {
            return name;
        }

    }

    public  class  Type implements Serializable{

        String id="";
        String name="";

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class SubType implements  Serializable{
        String id="";
        String name="";

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

        public String getId() {
            return id;
        }

        public String getState_code() {
            return state_code;
        }

        public String getName() {
            return name;
        }
    }

    /*public  class  Type implements Serializable{

        String id="";
        String name="";

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class SubType implements  Serializable{
        String id="";
        String name="";

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
*/


}
