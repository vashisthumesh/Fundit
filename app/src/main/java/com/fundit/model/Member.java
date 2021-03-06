package com.fundit.model;

import java.io.Serializable;

/**
 * Created by prince on 7/17/2017.
 */

public class Member implements Serializable {
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
    String redeemer = "";
    String contact_info_email="";
    String contact_info_mobile="";
    String organization_names="";
    String fundspot_names="";
    String seller="";
    String city_name="";




    Fundspot Fundspot = new Fundspot();
    Organization Organization = new Organization();

   // City City = new City();
    State State = new State();

    public String getOrganization_names() {
        return organization_names;
    }

    public String getFundspot_names() {
        return fundspot_names;
    }

    public String getId() {
        return id;
    }

    public String getContact_info_email() {
        return contact_info_email;
    }

    public String getSeller() {
        return seller;
    }

    public String getContact_info_mobile() {
        return contact_info_mobile;
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

    public String getLocation() {
        return location;
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

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
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

    public String getRedeemer() {
        return redeemer;
    }

    public String getCity_name() {
        return city_name;
    }

    public com.fundit.model.Fundspot getFundspot() {
        return Fundspot;
    }

    public com.fundit.model.Organization getOrganization() {
        return Organization;
    }

  /*  public Member.City getCity() {
        return City;
    }*/

    public Member.State getState() {
        return State;
    }

    public class City{

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

    public class State{

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
