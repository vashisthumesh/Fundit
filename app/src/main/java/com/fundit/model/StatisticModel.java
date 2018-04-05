package com.fundit.model;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivida6 on 07-03-2018.
 */

public class StatisticModel extends AppModel {

    StatisticData data = new StatisticData();

    public StatisticData getData() {
        return data;
    }

    public class StatisticData {

        StatisticCampaign Campaign = new StatisticCampaign();

        public StatisticCampaign getCampaign() {
            return Campaign;
        }
    }


    public class StatisticCampaign {


        String total_earning = "";
        String target_amount = "";
        String total_coupon_sold = "";
        String fundspot_percent = "";
        String organization_percent = "";
        String total_coupon_redeem = "";


        public String getTotal_earning() {
            return total_earning;
        }

        public String getTarget_amount() {
            return target_amount;
        }

        public String getTotal_coupon_sold() {
            return total_coupon_sold;
        }

        public String getFundspot_percent() {
            return fundspot_percent;
        }

        public String getOrganization_percent() {
            return organization_percent;
        }

        public String getTotal_coupon_redeem() {
            return total_coupon_redeem;
        }
    }

}
