package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 07-03-2018.
 */

public class MemberRequestModel extends AppModel {

    List<MemberRequestData> data = new ArrayList<>();

    public List<MemberRequestData> getData() {
        return data;
    }

    public class MemberRequestData {

        Member Member = new Member();
        User User = new User();
        State State = new State();
        City City = new City();

        public com.fundit.model.Member getMember() {
            return Member;
        }

        public com.fundit.model.User getUser() {
            return User;
        }

        public State getState() {
            return State;
        }

        public City getCity() {
            return City;
        }


    }


    public class State {
        String id = "";
        String name = "";
        String state_code = "";

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

    public class City {
        String id = "";
        String name = "";
        String city_code = "";
        String state_id = "";


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
