package com.fundit.model;

import com.fundit.Bean.GetProductsFundspotBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 07-03-2018.
 */

public class GetAllCampaignModel extends AppModel {

    CampaignData data = new CampaignData();

    public CampaignData getData() {
        return data;
    }

    public class CampaignData {

        Fundspot Fundspot = new Fundspot();

        List<GetProductsFundspotBean> Product = new ArrayList<>();

        public com.fundit.model.Fundspot getFundspot() {
            return Fundspot;
        }

        public List<GetProductsFundspotBean> getProduct() {
            return Product;
        }
    }







}
