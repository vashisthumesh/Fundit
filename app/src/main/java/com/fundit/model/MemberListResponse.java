package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 25-Jul-17.
 */

public class MemberListResponse extends AppModel {
    List<Member> data = new ArrayList<>();

    public List<Member> getData() {
        return data;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            names.add(data.get(i).getFirst_name() + " " + data.get(i).getLast_name());
        }

        return names;
    }
}
