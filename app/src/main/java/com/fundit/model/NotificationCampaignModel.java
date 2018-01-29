package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 27-01-2018.
 */

public class NotificationCampaignModel extends AppModel implements Serializable {

    CampaignData data = new CampaignData();

    public CampaignData getData() {
        return data;
    }

    public class CampaignData implements Serializable {


        News_campaign Campaign=new News_campaign();
        ReceiveUser ReceiveUser=new ReceiveUser();
        CreateUser CreateUser=new CreateUser();
        List<News_model.Product> CampaignProduct = new ArrayList<>();



        public ReceiveUser getReceiveUser() {
            return ReceiveUser;
        }

        public CreateUser getCreateUser() {
            return CreateUser;
        }

        public News_campaign getNews_Campaign() {
            return Campaign;
        }

        public List<News_model.Product> getCampaignProduct() {
            return CampaignProduct;
        }

    }


}
