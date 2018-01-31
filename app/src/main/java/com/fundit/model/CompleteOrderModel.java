package com.fundit.model;

import java.io.Serializable;

/**
 * Created by Nivida6 on 31-01-2018.
 */

public class CompleteOrderModel extends AppModel {

    OrderData data = new OrderData();

    public OrderData getData() {
        return data;
    }

    public class OrderData implements Serializable {

        String campaign_name = "";
        String customer_name = "";
        String organization_name = "";
        String fundspot_name = "";
        String total = "";
        String expiry_date = "";


        public String getCampaign_name() {
            return campaign_name;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getOrganization_name() {
            return organization_name;
        }

        public String getFundspot_name() {
            return fundspot_name;
        }

        public String getTotal() {
            return total;
        }

        public String getExpiry_date() {
            return expiry_date;
        }
    }
}
