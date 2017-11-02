package com.fundit.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class OrderHistoryResponse extends AppModel {

    List<OrderList> data = new ArrayList<>();

    public List<OrderList> getData() {
        return data;
    }

    public class OrderList implements Serializable{
        Order Order = new Order();
        Fundspot Fundspot = new Fundspot();
        Organization Organization = new Organization();
        Campaign Campaign = new Campaign();

        List<OrderedProducts> OrderProduct = new ArrayList<>();

        public com.fundit.model.Order getOrder() {
            return Order;
        }

        public com.fundit.model.Fundspot getFundspot() {
            return Fundspot;
        }

        public com.fundit.model.Organization getOrganization() {
            return Organization;
        }

        public com.fundit.model.Campaign getCampaign() {
            return Campaign;
        }

        public List<OrderedProducts> getOrderProduct() {
            return OrderProduct;
        }
    }


    public class OrderedProducts implements Serializable{

        String id ="";
        String order_id ="";
        String product_id ="";
        String fundspot_id ="";
        String name ="";
        String quantity ="";
        String selling_price ="";
        String item_total ="";
        String qr_code_img ="";
        String left_qty="";
        String coupon_status="";
        String type_id="";
        String left_money="";


        public String getId() {
            return id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getFundspot_id() {
            return fundspot_id;
        }

        public String getName() {
            return name;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getSelling_price() {
            return selling_price;
        }

        public String getLeft_qty() {
            return left_qty;
        }

        public String getCoupon_status() {
            return coupon_status;
        }

        public String getLeft_money() {
            return left_money;
        }

        public String getType_id() {
            return type_id;
        }

        public String getItem_total() {
            return item_total;
        }

        public String getQr_code_img() {
            return qr_code_img;
        }
    }


}
