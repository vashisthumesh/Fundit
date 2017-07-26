package com.fundit.model;

import java.io.Serializable;

/**
 * Created by prince on 7/17/2017.
 */

public class Fundspot extends Member implements Serializable {

    String category_id="";
    String fundraise_split="";
    String description="";

    public String getCategory_id() {
        return category_id;
    }

    public String getFundraise_split() {
        return fundraise_split;
    }

    public String getDescription() {
        return description;
    }
}
