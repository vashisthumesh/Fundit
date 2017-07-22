package com.fundit.model;

/**
 * Created by prince on 7/17/2017.
 */

public class Organization extends Member {

    String type_id="";
    String sub_type_id="";
    String description="";

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
}
