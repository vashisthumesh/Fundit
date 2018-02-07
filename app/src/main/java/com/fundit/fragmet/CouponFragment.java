package com.fundit.fragmet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.OrderHistoryAdapter;
import com.fundit.adapter.ShowOrderDetailsAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.OrderHistoryResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NWSPL-17 on 18-Aug-17.
 */

public class CouponFragment extends Fragment {


    View view;
    ListView listOrders;
    AppPreference preference;
    AdminAPI adminAPI;
    CustomDialog dialog;
   // RecyclerView listOrders;

    ShowOrderDetailsAdapter historyAdapter;

    List<OrderHistoryResponse.OrderList> orderHistoryResponse = new ArrayList<>();


    public CouponFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order_history, container, false);

        preference = new AppPreference(getActivity());
        adminAPI = ServiceGenerator.getAPIClass();
        dialog = new CustomDialog(getActivity());


        fetchIDs();

        return view;
    }

    private void fetchIDs() {

        listOrders = (ListView) view.findViewById(R.id.list_orders);
        //listOrders= (RecyclerView) view.findViewById(R.id.list_orders);


        historyAdapter = new ShowOrderDetailsAdapter(orderHistoryResponse , getActivity());
        listOrders.setAdapter(historyAdapter);

        GetAllDatas();


    }

    private void GetAllDatas() {
        dialog.show();
        Call<OrderHistoryResponse> orderReponse = adminAPI.GetCoupon(preference.getUserID() , preference.getTokenHash() , "0");
        Log.e("params" , "-->" + preference.getUserID());
        Log.e("hash","--->"+preference.getTokenHash());
        orderReponse.enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                dialog.dismiss();
                OrderHistoryResponse order = response.body();
                if(order!=null){
                    orderHistoryResponse.addAll(order.getData());
                }else{

                    C.INSTANCE.defaultError(getActivity());
                }
                historyAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity() , t);

            }
        });





    }


}
