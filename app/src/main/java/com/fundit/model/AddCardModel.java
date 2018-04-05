package com.fundit.model;

/**
 * Created by Nivida6 on 07-03-2018.
 */

public class AddCardModel extends AppModel {

    CardData data = new CardData();

    public CardData getData() {
        return data;
    }

    public class CardData {


        String auth_cust_paymnet_profile_id = "";
        String profile_id = "";
        String card_id = "";


        public String getAuth_cust_paymnet_profile_id() {
            return auth_cust_paymnet_profile_id;
        }

        public String getProfile_id() {
            return profile_id;
        }

        public String getCard_id() {
            return card_id;
        }
    }
}
