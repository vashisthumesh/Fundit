package com.fundit.model;

/**
 * Created by NWSPL6 on 10/31/2017.
 */

public class GeneralMemberProfileResponse extends AppModel {
    Data data=new Data();


    public Data getData() {
        return data;
    }

    public class Data
    {
        Member Member=  new Member();
        User User=new User();
        State State=new State();
        City City=new City();

        public com.fundit.model.Member getMember() {
            return Member;
        }

        public com.fundit.model.User getUser() {
            return User;
        }

        public GeneralMemberProfileResponse.State getState() {
            return State;
        }

        public GeneralMemberProfileResponse.City getCity() {
            return City;
        }





    }


    public  class State
    {
        String id="";
        String name="";
        String state_code="";

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getState_code() {
            return state_code;
        }
    }

    public class  City
    {
        String id="";
        String name="";
        String city_code="";
        String state_id="";


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
    }




}
