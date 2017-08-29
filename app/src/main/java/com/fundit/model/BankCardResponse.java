package com.fundit.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NWSPL-17 on 25-Aug-17.
 */

public class BankCardResponse extends AppModel {

    List<BankCardResponseData> data = new ArrayList<>();


    public List<BankCardResponseData> getData() {
        return data;
    }

    public class BankCardResponseData {

        /*CardData cardData = new CardData();

        public CardData getCardData() {
            return cardData;
        }*/

        String id = "";
        String user_id = "";
        String bcard_type = "";
        String bcard_number = "";
        String bexp_month = "";
        String bexp_year = "";
        String cvv_no = "";
        String zip_code = "";
        String created = "";


        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getBcard_type() {
            return bcard_type;
        }

        public String getBcard_number() {
            return bcard_number;
        }

        public String getBexp_month() {
            return bexp_month;
        }

        public String getBexp_year() {
            return bexp_year;
        }

        public String getCvv_no() {
            return cvv_no;
        }

        public String getZip_code() {
            return zip_code;
        }

        public String getCreated() {
            return created;
        }



    }

}
