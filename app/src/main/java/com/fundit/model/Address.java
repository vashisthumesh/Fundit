package com.fundit.model;

import java.io.Serializable;

/**
 * Created by NWSPL6 on 1/22/2018.
 */

public class Address extends AppModel {
    AddressData data=new AddressData();

    public AddressData getData() {
        return data;
    }

    public class AddressData implements Serializable {

        FundspotData Fundspot;
        StateData State;

        public FundspotData getFundspot() {
            return Fundspot;
        }

        public StateData getState() {
            return State;
        }

        public class FundspotData implements Serializable{

            String location="";
            String zip_code="";
            String contact_info="";


            public String getLocation() {
                return location;
            }

            public String getZip_code() {
                return zip_code;
            }

            public String getContact_info() {
                return contact_info;
            }
        }

        public class  StateData implements Serializable{

            String id="";
            String name="";
            String country_id="";
            String state_code="";
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



    }

}
