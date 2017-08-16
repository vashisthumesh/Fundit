package com.fundit.model;

import com.fundit.a.AppPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 16-Aug-17.
 */

public class MemberResponse extends AppModel {


    List<MemberData> data = new ArrayList<>();


    public List<MemberData> getData() {
        return data;
    }

    public class MemberData{

        String id = "";
        String user_id = "";
        String first_name = "";
        String last_name = "";
        String location = "";
        String contact_info = "";
        String image = "";


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

        public String getLocation() {
            return location;
        }

        public String getContact_info() {
            return contact_info;
        }

        public String getImage() {
            return image;
        }
    }



}
