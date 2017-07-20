package com.fundit.model;

import java.util.ArrayList;

/**
 * Created by prince on 7/19/2017.
 */

public class CategoryResponse extends AppModel {

    ArrayList<AreaItem> data=new ArrayList<>();

    public ArrayList<AreaItem> getData() {
        return data;
    }

    public ArrayList<String> getIDList(){
        ArrayList<String> idList=new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            idList.add(data.get(i).getId());
        }

        return idList;
    }

    public ArrayList<String> getNameList(){
        ArrayList<String> nameList=new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            nameList.add(data.get(i).getName());
        }

        return nameList;
    }
}
