package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida new on 20-Jul-17.
 */

public class ProductListResponse extends AppModel {

    List<Product> data = new ArrayList<>();

    public List<Product> getData() {
        return data;
    }

    public class Product implements Serializable {
        String id = "";
        String user_id = "";
        String type_id = "";
        String name = "";
        String description = "";
        String image = "";
        String price = "";
        String fundspot_percent = "";
        String organization_percent = "";
        String campaign_duration = "";
        String max_limit_of_coupons = "";
        String coupon_expire_day = "";
        String created = "";
        String fine_print = "";

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getType_id() {
            return type_id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getImage() {
            return image;
        }

        public String getPrice() {
            return price;
        }

        public String getFundspot_percent() {
            return fundspot_percent;
        }

        public String getOrganization_percent() {
            return organization_percent;
        }

        public String getCampaign_duration() {
            return campaign_duration;
        }

        public String getMax_limit_of_coupons() {
            return max_limit_of_coupons;
        }

        public String getCoupon_expire_day() {
            return coupon_expire_day;
        }

        public String getCreated() {
            return created;
        }

        public String getFine_print() {
            return fine_print;
        }
    }
}
