package com.fundit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL6 on 1/18/2018.
 */

public class News_model extends AppModel {

    String total_page_count = "";

    List<NewsData> data = new ArrayList<>();


    public String getTotal_page_count() {
        return total_page_count;
    }

    public List<NewsData> getData() {
        return data;
    }

    public class NewsData implements Serializable {

        NewsFeedDataModel NewsFeed = new NewsFeedDataModel();

        CampaignDetails CampaignDetails = new CampaignDetails();


        public NewsFeedDataModel getNewsFeed() {
            return NewsFeed;
        }

        public News_model.CampaignDetails getCampaignDetails() {
            return CampaignDetails;
        }
    }


    public class CampaignDetails implements Serializable {

        News_campaign Campaign=new News_campaign();
        ReceiveUser ReceiveUser=new ReceiveUser();
        CreateUser CreateUser=new CreateUser();
        List<Product> CampaignProduct = new ArrayList<>();



        public ReceiveUser getReceiveUser() {
            return ReceiveUser;
        }

        public CreateUser getCreateUser() {
            return CreateUser;
        }

        public News_campaign getNews_Campaign() {
            return Campaign;
        }

        public List<Product> getCampaignProduct() {
            return CampaignProduct;
        }
    }


    public class Product implements Serializable{

        String id="";
        String campaign_id="";
        String product_id="";
        String price="";
        String name="";
        String description="";
        String fine_print="";
        String image="";
        String type_id="";
        String created="";
        String modified="";

        public String getId() {
            return id;
        }

        public String getCampaign_id() {
            return campaign_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public String getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getFine_print() {
            return fine_print;
        }

        public String getImage() {
            return image;
        }

        public String getType_id() {
            return type_id;
        }

        public String getCreated() {
            return created;
        }

        public String getModified() {
            return modified;
        }
    }




}
