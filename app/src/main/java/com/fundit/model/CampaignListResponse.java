package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 24-Jul-17.
 */

public class CampaignListResponse extends AppModel {

    List<CampaignList> data = new ArrayList<>();

    public List<CampaignList> getData() {
        return data;
    }

    public class CampaignList implements Serializable {
        Campaign Campaign = new Campaign();
        ProductListResponse.Product Product = new ProductListResponse.Product();
        UserFundspot ReceiverUser = new UserFundspot();
        UserOrganization CreateUser = new UserOrganization();

        public com.fundit.model.Campaign getCampaign() {
            return Campaign;
        }

        public ProductListResponse.Product getProduct() {
            return Product;
        }

        public CampaignListResponse.UserFundspot getUserFundspot() {
            return ReceiverUser;
        }

        public CampaignListResponse.UserOrganization getUserOrganization() {
            return CreateUser;
        }
    }

    public class UserFundspot extends User implements Serializable {
        Fundspot Fundspot = new Fundspot();

        public com.fundit.model.Fundspot getFundspot() {
            return Fundspot;
        }
    }

    public class UserOrganization extends User implements Serializable {
        Organization Organization = new Organization();

        public com.fundit.model.Organization getOrganization() {
            return Organization;
        }
    }
}
