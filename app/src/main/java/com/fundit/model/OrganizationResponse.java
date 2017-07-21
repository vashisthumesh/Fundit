package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 21-Jul-17.
 */

public class OrganizationResponse extends AppModel {
    List<Organization> data = new ArrayList<>();

    public List<Organization> getData() {
        return data;
    }

    public ArrayList<String> getOrganizationNames() {
        ArrayList<String> organizationNames = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            organizationNames.add(data.get(i).getTitle());
        }

        return organizationNames;
    }
}
