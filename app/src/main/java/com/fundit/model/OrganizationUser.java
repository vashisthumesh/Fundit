package com.fundit.model;

/**
 * Created by NWSPL-17 on 02-Aug-17.
 */

public class OrganizationUser extends Member {

    String id ="";
    String title = "";


    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
