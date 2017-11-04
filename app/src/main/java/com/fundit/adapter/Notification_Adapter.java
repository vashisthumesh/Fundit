package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fundit.Bean.Bean_Notification_history;
import com.fundit.Fundit;
import com.fundit.NotificationActivity;
import com.fundit.NotificationDetailActivity;
import com.fundit.R;
import com.fundit.SignInActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by prince on 4/14/2017.
 */

public class Notification_Adapter extends BaseAdapter {

    ArrayList<Bean_Notification_history> result = new ArrayList<>();
    Context context;
    Activity activity;
    AppPreference pref;
    Date date1;
    String product_id, customer_id;
    //AppPref prefs;
    // List<ProductItem> result;
    int[] imageId;
    boolean checked = false;
    String role_id="";
    private static LayoutInflater inflater = null;

    public Notification_Adapter(NotificationActivity add_to_cartActivity, ArrayList<Bean_Notification_history> productItemList, NotificationActivity activity) {
        // TODO Auto-generated constructor stub
        result = productItemList;
        context = add_to_cartActivity;
        this.activity = activity;

        pref = new AppPreference(activity.getApplicationContext());
        //  prefs=new AppPref(activity.getApplicationContext());
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {

        TextView notification;
        LinearLayout lv_notification_layer;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Bean_Notification_history companyProfile = result.get(position);

        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_notification_history, null);

         final int pos=position+1;
        holder.notification=(TextView)rowView.findViewById(R.id.notification);
        holder.lv_notification_layer=(LinearLayout)rowView.findViewById(R.id.lv_notification_layer);
        holder.notification.setText(result.get(position).getMsg());

        holder.lv_notification_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id=result.get(position).getReceive_user();
                String tokenhash=pref.getTokenHash();
                String notificationId=result.get(position).getId();
                int read= Integer.parseInt(result.get(position).getRead_status());
                  role_id=result.get(position).getRole_id();


             if(pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                 if (read == 1) {
                     Intent i=new Intent(activity, NotificationDetailActivity.class);
                     i.putExtra("role_id",role_id);
                     i.putExtra("sent_user",result.get(position).getSent_user());
                     i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                     activity.startActivity(i);
                     //showdialog(position);
                 } else {
                     read_notification(user_id, tokenhash, notificationId, position);

                 }
             }
             else {
                 if (read == 1) {
                     showdialog(position);
                 } else {
                     read_notification(user_id, tokenhash, notificationId, position);

                 }
             }
            }
        });
        return rowView;
    }

    private void read_notification(final String user_id, final String tokenhash, final String notificationId,final int position ) {
         final CustomDialog loadingView = new CustomDialog(context, "");
         loadingView.setCancelable(false);
         loadingView.show();
        final StringRequest request = new StringRequest(W.POST, W.BASE_URL + "Notification/app_read_notification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.e("JSON", json);
                        try {
                            JSONObject object = new JSONObject(json);
                            String  message=object.getString("message");
                            Toast.makeText(context,""+message, Toast.LENGTH_SHORT).show();
                            loadingView.dismiss();


                            if(pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                Intent i=new Intent(activity, NotificationDetailActivity.class);
                                i.putExtra("role_id",role_id);
                                i.putExtra("sent_user",result.get(position).getSent_user());
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(i);
                            }
                            else {
                                showdialog(position);
                            }

                        }catch(JSONException j){
                            j.printStackTrace();
                            Log.e("Exception",""+j.getMessage());
                             loadingView.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                         loadingView.dismiss();
                        Log.e("ERROR",error.getMessage());
                        if (error instanceof NetworkError) {
                            //noInternet(context);
                        } else {
                           // serverError(context);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("tokenhash",tokenhash);
                params.put("notification_id",notificationId);

                Log.e("params", params.toString());
                return params;
            }
        };
        Fundit.getInstance().addToRequestQueue(request);

    }

    private void showdialog(int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_forget_password, null);
        builder.setView(dialogView);
        builder.setTitle("Notification");
        final AlertDialog dialogB = builder.create();
        //setting custom layout to dialog


        final TextView Email = (TextView) dialogView.findViewById(R.id.tv_message);
        final Button bt_cancel = (Button) dialogView.findViewById(R.id.bt_cancel);

        Email.setText(result.get(position).getMsg());

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogB.dismiss();
            }
        });

        dialogB.show();


    }
}