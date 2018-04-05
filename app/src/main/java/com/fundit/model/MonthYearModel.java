package com.fundit.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 07-03-2018.
 */

public class MonthYearModel extends AppModel {


    Data data = new Data();

    public Data getData() {
        return data;
    }

    public class Data {

        ArrayList<String> months = new ArrayList<>();

        ArrayList<String> years = new ArrayList<>();

        public ArrayList<String> getMonths() {
            return months;
        }

        public ArrayList<String> getYears() {
            return years;
        }
    }


}
