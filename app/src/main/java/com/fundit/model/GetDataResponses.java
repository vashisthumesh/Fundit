package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 07-Sep-17.
 */

public class GetDataResponses extends AppModel {


    List<Data> data = new ArrayList<>();


    public List<Data> getData() {
        return data;
    }


    public class Data implements Serializable{

        Fundspot Fundspot = new Fundspot();
        Organization Organization = new Organization();
        User User = new User();


        public com.fundit.model.Fundspot getFundspot() {
            return Fundspot;
        }

        public com.fundit.model.User getUser() {
            return User;
        }

        public com.fundit.model.Organization getOrganization() {
            return Organization;
        }
    }


}
