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
import com.fundit.AddMemberFudActivity;
import com.fundit.AddMembersActivity;
import com.fundit.Bean.Bean_Notification_history;
import com.fundit.FundOrganizationRequestActivity;
import com.fundit.Fundit;
import com.fundit.HomeActivity;
import com.fundit.NewsDetailActivity;
import com.fundit.NewsadapterclickActivity;
import com.fundit.NotificationActivity;
import com.fundit.NotificationDetailActivity;
import com.fundit.NotificationOrderActivity;
import com.fundit.OrderPlaceActivity;
import com.fundit.R;
import com.fundit.SerchPeopleActivity;
import com.fundit.SignInActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.AppModel;
import com.fundit.model.NotificationCampaignModel;


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
    String role_id = "";
    private static LayoutInflater inflater = null;
    AdminAPI adminAPI;

    public Notification_Adapter(NotificationActivity add_to_cartActivity, ArrayList<Bean_Notification_history> productItemList, NotificationActivity activity) {
        // TODO Auto-generated constructor stub
        result = productItemList;
        context = add_to_cartActivity;
        this.activity = activity;

        pref = new AppPreference(activity.getApplicationContext());
        //  prefs=new AppPref(activity.getApplicationContext());
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        adminAPI = ServiceGenerator.getAPIClass();
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

        final int pos = position + 1;
        holder.notification = (TextView) rowView.findViewById(R.id.notification);
        holder.lv_notification_layer = (LinearLayout) rowView.findViewById(R.id.lv_notification_layer);
        holder.notification.setText(result.get(position).getMsg());


        if(result.get(position).getRead_status().equalsIgnoreCase("0")){
            holder.lv_notification_layer.setBackgroundColor(context.getResources().getColor(R.color.color_gray));
        }else {
            holder.lv_notification_layer.setBackgroundColor(context.getResources().getColor(R.color.white));
        }


        holder.lv_notification_layer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String user_id = result.get(position).getReceive_user();
                final String tokenhash = pref.getTokenHash();
                final String notificationId = result.get(position).getId();
                final String campaignId = result.get(position).getCampaign_id();
                int read = Integer.parseInt(result.get(position).getRead_status());
                role_id = result.get(position).getRole_id();
                final String typeId = result.get(position).getType_id();

                if (read == 0) {
                 //   read_notification(user_id, tokenhash, notificationId, position);
                    final CustomDialog loadingView = new CustomDialog(context, "");
                    loadingView.setCancelable(false);
                    loadingView.show();
                    final StringRequest request = new StringRequest(W.POST, W.ASYNC_BASE_URL + "Notification/app_read_notification",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String json) {
                                    Log.e("JSON", json);
                                    try {
                                        JSONObject object = new JSONObject(json);
                                        String message = object.getString("message");
                                       // Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                                        loadingView.dismiss();
                                        if (typeId.equalsIgnoreCase("1")) {

                                            if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                                                Intent intent = new Intent(context, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("notificationTimes", true);
                                                intent.putExtra("typeId", typeId);
                                                context.startActivity(intent);

                                            } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                                Intent i = new Intent(activity, FundOrganizationRequestActivity.class);
                            /*i.putExtra("role_id", role_id);
                            i.putExtra("sent_user", result.get(position).getSent_user());*/
                                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                activity.startActivity(i);
                                            }


                                        } else if (typeId.equalsIgnoreCase("2")) {

                                            if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                                Intent intent = new Intent(activity, SerchPeopleActivity.class);
                                                intent.putExtra("id", result.get(position).getSent_user());
                                                intent.putExtra("flag", true);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);

                                            } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {

                                                Intent intent = new Intent(context, AddMemberFudActivity.class);



                                                if(result.get(position).getRole_id().equalsIgnoreCase(C.ORGANIZATION)){
                                                    intent.putExtra("Flag","org");
                                                }else if (result.get(position).getRole_id().equalsIgnoreCase(C.FUNDSPOT)){
                                                    intent.putExtra("Flag","fund");
                                                }
                                                intent.putExtra("memberId", result.get(position).getSent_user());
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                context.startActivity(intent);



                                            }

                                        } else if (typeId.equalsIgnoreCase("3")) {
                                            Intent intent = new Intent(context, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("notificationTimes", true);
                                            intent.putExtra("typeId", typeId);
                                            context.startActivity(intent);

                                        } else if (typeId.equalsIgnoreCase("4") || typeId.equalsIgnoreCase("6")) {

                                            GetCampaignDetails(campaignId);

                                        } else if (typeId.equalsIgnoreCase("12") || typeId.equalsIgnoreCase("13") || typeId.equalsIgnoreCase("14")) {
                                            Intent intent = new Intent(context, HomeActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.putExtra("notificationTimes", true);
                                            intent.putExtra("typeId", typeId);
                                            context.startActivity(intent);
                                        } else if (typeId.equalsIgnoreCase("16")) {
                                            Intent i = new Intent(activity, NotificationDetailActivity.class);
                                            i.putExtra("role_id", role_id);
                                            i.putExtra("sent_user", result.get(position).getSent_user());
                                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            activity.startActivity(i);

                                        } else {
                                            showdialog(position);
                                        }
                                    } catch (JSONException j) {
                                        j.printStackTrace();
                                        Log.e("Exception", "" + j.getMessage());
                                        loadingView.dismiss();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loadingView.dismiss();
                                    Log.e("ERROR", error.getMessage());
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
                            params.put("tokenhash", tokenhash);
                            params.put("notification_id", notificationId);

                            Log.e("params", params.toString());
                            return params;
                        }
                    };
                    Fundit.getInstance().addToRequestQueue(request);






                } else {

                    if (typeId.equalsIgnoreCase("1")) {

                        if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                            Intent intent = new Intent(context, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("notificationTimes", true);
                            intent.putExtra("typeId", typeId);
                            context.startActivity(intent);

                        } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            Intent i = new Intent(activity, FundOrganizationRequestActivity.class);
                            /*i.putExtra("role_id", role_id);
                            i.putExtra("sent_user", result.get(position).getSent_user());*/
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            activity.startActivity(i);
                        }


                    } else if (typeId.equalsIgnoreCase("2")) {

                        if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                            Intent intent = new Intent(activity, SerchPeopleActivity.class);
                            intent.putExtra("id", result.get(position).getSent_user());
                            intent.putExtra("flag", true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {

                            Intent intent = new Intent(context, AddMemberFudActivity.class);



                            if(result.get(position).getRole_id().equalsIgnoreCase(C.ORGANIZATION)){
                                intent.putExtra("Flag","org");
                            }else if (result.get(position).getRole_id().equalsIgnoreCase(C.FUNDSPOT)){
                                intent.putExtra("Flag","fund");
                            }
                            intent.putExtra("memberId", result.get(position).getSent_user());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);



                        }

                    } else if (typeId.equalsIgnoreCase("3")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationTimes", true);
                        intent.putExtra("typeId", typeId);
                        context.startActivity(intent);

                    } else if (typeId.equalsIgnoreCase("4") || typeId.equalsIgnoreCase("6")) {

                        GetCampaignDetails(campaignId);

                    } else if (typeId.equalsIgnoreCase("12") || typeId.equalsIgnoreCase("13") || typeId.equalsIgnoreCase("14")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("notificationTimes", true);
                        intent.putExtra("typeId", typeId);
                        context.startActivity(intent);
                    } else if (typeId.equalsIgnoreCase("16")) {
                        Intent i = new Intent(activity, NotificationDetailActivity.class);
                        i.putExtra("role_id", role_id);
                        i.putExtra("sent_user", result.get(position).getSent_user());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(i);

                    } else {
                        showdialog(position);
                    }

                }


            }
        });
        return rowView;
    }

    private void GetCampaignDetails(final String campaignId) {
        final CustomDialog loadingView = new CustomDialog(activity, "");
        loadingView.setCancelable(false);
        loadingView.show();
        Call<NotificationCampaignModel> notificationCampaignModelCall = adminAPI.NOTIFICATION_CAMPAIGN_MODEL_CALL(campaignId);
        Log.e("adapterParameters" , "--->" + campaignId);
        notificationCampaignModelCall.enqueue(new Callback<NotificationCampaignModel>() {
            @Override
            public void onResponse(Call<NotificationCampaignModel> call, retrofit2.Response<NotificationCampaignModel> response) {
                loadingView.dismiss();
                NotificationCampaignModel campaignModel = response.body();
                if (campaignModel != null) {
                    if (campaignModel.isStatus()) {
                        /*Intent intent = new Intent(context, OrderPlaceActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("Details", campaignModel.getData());
                        context.startActivity(intent);*/

                        if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                            Intent intent = new Intent(context, NewsDetailActivity.class);
                            intent.putExtra("details", campaignModel.getData());
                            pref.setCampaignBack(true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, OrderPlaceActivity.class);
                            intent.putExtra("Details", campaignModel.getData());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }





                    } else {
                        C.INSTANCE.showToast(context, campaignModel.getMessage());
                    }
                } else {
                    C.INSTANCE.defaultError(context);
                }
            }

            @Override
            public void onFailure(Call<NotificationCampaignModel> call, Throwable t) {
                loadingView.dismiss();
                C.INSTANCE.errorToast(context, t);
            }
        });


    }

    private void read_notification(final String user_id, final String tokenhash, final String notificationId, final int position) {
        final CustomDialog loadingView = new CustomDialog(context, "");
        loadingView.setCancelable(false);
        loadingView.show();
        final StringRequest request = new StringRequest(W.POST, W.ASYNC_BASE_URL + "Notification/app_read_notification",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        Log.e("JSON", json);
                        try {
                            JSONObject object = new JSONObject(json);
                            String message = object.getString("message");
                            Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            loadingView.dismiss();


                            /*if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                Intent i = new Intent(activity, NotificationDetailActivity.class);
                                i.putExtra("role_id", role_id);
                                i.putExtra("sent_user", result.get(position).getSent_user());
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(i);
                            } else {
                                showdialog(position);
                            }*/



                                if (result.get(position).getType_id().equalsIgnoreCase("1")) {

                                    if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {

                                        Intent intent = new Intent(context, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("notificationTimes", true);
                                        intent.putExtra("typeId", result.get(position).getType_id());
                                        context.startActivity(intent);

                                    } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {
                                        Intent i = new Intent(activity, NotificationDetailActivity.class);
                                        i.putExtra("role_id", role_id);
                                        i.putExtra("sent_user", result.get(position).getSent_user());
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        activity.startActivity(i);
                                    }


                                } else if (result.get(position).getType_id().equalsIgnoreCase("2")) {

                                    if (pref.getUserRoleID().equalsIgnoreCase(C.ORGANIZATION) || pref.getUserRoleID().equalsIgnoreCase(C.FUNDSPOT)) {
                                        Intent intent = new Intent(context, AddMembersActivity.class);
                                        intent.putExtra("memberId", result.get(position).getReceive_user());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);

                                    } else if (pref.getUserRoleID().equalsIgnoreCase(C.GENERAL_MEMBER)) {

                                        Intent intent = new Intent(activity, SerchPeopleActivity.class);
                                        intent.putExtra("id", result.get(position).getReceive_user());
                                        intent.putExtra("flag", true);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }

                                } else if (result.get(position).getType_id().equalsIgnoreCase("3")) {
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("notificationTimes", true);
                                    intent.putExtra("typeId", result.get(position).getType_id());
                                    context.startActivity(intent);

                                } else if (result.get(position).getType_id().equalsIgnoreCase("4") || result.get(position).getType_id().equalsIgnoreCase("6")) {

                                    GetCampaignDetails(result.get(position).getCampaign_id());

                                } else if (result.get(position).getType_id().equalsIgnoreCase("12") || result.get(position).getType_id().equalsIgnoreCase("13") || result.get(position).getType_id().equalsIgnoreCase("14")) {
                                    Intent intent = new Intent(context, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("notificationTimes", true);
                                    intent.putExtra("typeId", result.get(position).getType_id());
                                    context.startActivity(intent);
                                } else if (result.get(position).getType_id().equalsIgnoreCase("16")) {
                                    Intent i = new Intent(activity, NotificationDetailActivity.class);
                                    i.putExtra("role_id", role_id);
                                    i.putExtra("sent_user", result.get(position).getSent_user());
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    activity.startActivity(i);

                                } else {
                                    showdialog(position);
                                }









                        } catch (JSONException j) {
                            j.printStackTrace();
                            Log.e("Exception", "" + j.getMessage());
                            loadingView.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingView.dismiss();
                        Log.e("ERROR", error.getMessage());
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
                params.put("tokenhash", tokenhash);
                params.put("notification_id", notificationId);

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