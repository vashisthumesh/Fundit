package com.fundit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fundit.Bean.Bean_Notification_history;
import com.fundit.a.AppPreference;
import com.fundit.a.W;
import com.fundit.adapter.Notification_Adapter;
import com.fundit.helper.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationActivity extends AppCompatActivity {


     ListView List_notification;
    Notification_Adapter order_history_adapter1;
    ArrayList<Bean_Notification_history> array_order_history_list = new ArrayList<Bean_Notification_history>();

    AppPreference prefs;
    String user_id="",token_hash="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        prefs = new AppPreference(getApplicationContext());
        setupToolbar();


    }
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCenterText);
        TextView actionTitle = (TextView) findViewById(R.id.actionTitle);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        actionTitle.setText("Notification");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        List_notification = (ListView) findViewById(R.id.List_notification);


        order_history_adapter1 = new Notification_Adapter(NotificationActivity.this, array_order_history_list, NotificationActivity.this);
        order_history_adapter1.notifyDataSetChanged();
        List_notification.setAdapter(order_history_adapter1);
       user_id = prefs.getUserID();
        token_hash=prefs.getTokenHash();
        getNotifications(user_id,token_hash,user_id);
    }

    private void getNotifications(final String user_id,final String token_hash,final String receiver_id) {
        final CustomDialog loadingView = new CustomDialog(NotificationActivity.this, "");
        loadingView.setCancelable(false);
        loadingView.show();
        StringRequest request = new StringRequest(W.POST, W.BASE_URL + "Notification/app_get_notification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        array_order_history_list.clear();
                        Log.e("JSON", json);
                        try {
                            JSONObject jObj = new JSONObject(json);
                            //db = new DatabaseHandler(());

                            Log.e("666", "5555");
                            Log.e("666", "5555");
                            JSONArray jarray_data = jObj.getJSONArray("data");

                            for (int i = 0; i < jarray_data.length(); i++) {
                                JSONObject jobject = jarray_data.getJSONObject(i);
                                Bean_Notification_history bean_category = new Bean_Notification_history();
                                bean_category.setId(jobject.getString("id"));
                                bean_category.setSent_user(jobject.getString("sent_user"));
                                bean_category.setReceive_user(jobject.getString("receive_user"));
                                bean_category.setMsg(jobject.getString("msg"));
                                bean_category.setRead_status(jobject.getString("read_status"));

                                array_order_history_list.add(bean_category);

                                //  System.out.println("Category - "+array_categor.size()+" Quote - "+array_quote.size()+" card - "+array_card.size()+" clipart - "+array_clipart.size());

                            }


                        } catch (JSONException j) {
                            j.printStackTrace();
                            Log.e("Exception", "-->" + j.getMessage());
                        }
                        loadingView.dismiss();
                        if(array_order_history_list.size()==0){
                            Toast.makeText(NotificationActivity.this, "No Notification Available", Toast.LENGTH_LONG).show();
                        }
                        order_history_adapter1.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingView.dismiss();
                        Log.e("ERROR", error.getMessage());

                        if (error instanceof NetworkError) {
                           // noInternet(getApplicationContext());
                        } else {
                           // serverError(getApplicationContext());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("tokenhash", token_hash);
                params.put("receive_user",user_id);


                Log.e("params", params.toString());
                return params;
            }
        };
        Fundit.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext() , HomeActivity.class);
        startActivity(intent);
    }
}
