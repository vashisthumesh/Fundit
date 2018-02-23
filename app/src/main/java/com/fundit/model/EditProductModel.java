package com.fundit.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 16-02-2018.
 */

public class EditProductModel extends AppModel {


    List<ProductListResponse.Product> product_data = new ArrayList<>();

    public List<ProductListResponse.Product> getProduct_data() {
        return product_data;
    }
}
