package com.fundit.model;

/**
 * Created by Nivida6 on 01-02-2018.
 */

public class QRScanModel extends AppModel {

   String left_qty = "";
   String order_product_id = "";
   String product_type_id = "";
   String product_name = "";
   String left_money = "";
   String pop_up_title = "";

    public String getLeft_qty() {
        return left_qty;
    }

    public String getOrder_product_id() {
        return order_product_id;
    }

    public String getProduct_type_id() {
        return product_type_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getLeft_money() {
        return left_money;
    }

    public String getPop_up_title() {
        return pop_up_title;
    }
}
