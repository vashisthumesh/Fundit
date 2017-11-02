package com.fundit;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.ServiceHandler;
import com.fundit.helper.CustomDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    AppPreference preference;
    CustomDialog dialog;

    String campaignId = "";

    TextView txt_raised, txt_targetAmt, txt_itemSold, txt_couponLeft, txt_netMoney, txt_couponRedeemed;

    LinearLayout layout_left ;

    String raised = "";
    String targetAmt = "";
    String itemSold = "";
    String couponLeft = "";
    String netMoney = "";
    String redeem = "";
    String fundspotPercent = "";
    String organizationPercent = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_statistics);

        Intent intent = getIntent();
        campaignId = intent.getStringExtra("campaignId");


        preference = new AppPreference(getApplicationContext());
        dialog = new CustomDialog(StatisticActivity.this, "");

        setupToolBar();
        fetchIds();


    }

    private void fetchIds() {

        txt_raised = (TextView) findViewById(R.id.txt_raised);
        txt_targetAmt = (TextView) findViewById(R.id.txt_targetAmt);
        txt_itemSold = (TextView) findViewById(R.id.txt_itemSold);
        txt_couponLeft = (TextView) findViewById(R.id.txt_couponLeft);
        txt_netMoney = (TextView) findViewById(R.id.txt_netMoney);
        txt_couponRedeemed = (TextView) findViewById(R.id.txt_couponRedeemed);

        layout_left = (LinearLayout) findViewById(R.id.layout_left);
        layout_left.setVisibility(View.GONE);





        new GetStatisticDetails().execute();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
    private void setupToolBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);
        actionTitle.setText("Statistics");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public class GetStatisticDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {

                dialog.setCancelable(false);
                dialog.show();


            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            List<NameValuePair> pairs = new ArrayList<>();


            pairs.add(new BasicNameValuePair("campaign_id", campaignId));

            String json = new ServiceHandler().makeServiceCall(W.BASE_URL + "Campaign/app_get_campaign_stats", ServiceHandler.POST, pairs);

            Log.e("parameters", "-->" + pairs);
            Log.e("json", json);


            return json;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            if (s.isEmpty()) {

                C.INSTANCE.defaultError(getApplicationContext());
            } else {

                try {
                    JSONObject mainObject = new JSONObject(s);

                    boolean status = false;
                    String message = "";

                    status = mainObject.getBoolean("status");
                    message = mainObject.getString("message");

                    if (status) {

                        JSONObject dataObject = mainObject.getJSONObject("data");
                        JSONObject campaignObject = dataObject.getJSONObject("Campaign");

                        raised = campaignObject.getString("total_earning");
                        Log.e("raised",raised);
                        targetAmt = campaignObject.getString("target_amount");
                        Log.e("targetamt",targetAmt);
                        itemSold = campaignObject.getString("total_coupon_sold");
                        fundspotPercent = campaignObject.getString("fundspot_percent");
                        organizationPercent = campaignObject.getString("organization_percent");
                        redeem = campaignObject.getString("total_coupon_redeem");


                        double finallength=0;
                        finallength=(int) (getScreenWidth()/3);

                        double initiallength=0;
                        initiallength= (getScreenWidth()/3.5);

                        double remaininglength=0;
                        remaininglength= (getScreenWidth()-(int)(getScreenWidth()/3)-(getScreenWidth()/3.5));


                        txt_targetAmt.getLayoutParams().width= (int) finallength;
                        Log.e("final width-->",""+finallength);

                        txt_raised.getLayoutParams().width= (int) initiallength;
                        Log.e("initial width-->",""+initiallength);
                        Log.e("remaining width-->",""+remaininglength);

                        double initialno=0,finalno=0;
                        double ibyf=0.0,multiply=0.0,toadd=0.0;

                        initialno= Double.parseDouble(raised);
                        finalno= Double.parseDouble(targetAmt);

                        if(finalno == 0)
                        {

                            toadd=initiallength ;
                            txt_raised.getLayoutParams().width= (int) toadd;
                            
                        }
                        else {
                            ibyf=(double) initialno/(double)finalno;
                            if(ibyf >=1)
                            {
                                multiply=remaininglength*1;
                            }
                            else {
                                multiply = remaininglength * ibyf;
                            }

                            toadd=initiallength+multiply;
                            txt_raised.getLayoutParams().width= (int) toadd;
                        }



                        txt_raised.setText("Raised"+"\t"+"$"+raised);
                        txt_targetAmt.setText("$" + String.format("%.2f", Double.parseDouble(targetAmt)));
                        txt_itemSold.setText(itemSold);
                        txt_couponRedeemed.setText(redeem);

                        if (preference.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                            Double targetAmount = 0.0, fundspots = 0.0, earningAmount = 0.0;
                            targetAmount = Double.parseDouble(raised);
                            fundspots = Double.parseDouble(fundspotPercent);


                            earningAmount = (targetAmount * fundspots) / 100;

                            netMoney = String.valueOf(earningAmount);

                            txt_netMoney.setText("$" + netMoney);


                        }

                        if (preference.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION)) {


                            Double targetAmount = 0.0, organizations = 0.0, earningAmount = 0.0;
                            targetAmount = Double.parseDouble(raised);
                            organizations = Double.parseDouble(organizationPercent);


                            earningAmount = (targetAmount * organizations) / 100;

                            netMoney = String.valueOf(earningAmount);

                            txt_netMoney.setText("$" + netMoney);


                        }


                    } else {


                        C.INSTANCE.showToast(getApplicationContext(), message);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
    }
}
