package com.fundit.fragmet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.adapter.NewsAdapter;
import com.fundit.apis.AdminAPI;
import com.fundit.apis.ServiceGenerator;
import com.fundit.helper.CustomDialog;
import com.fundit.model.CampaignListResponse;
import com.fundit.model.News_model;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsFragment extends Fragment {

    View view;
    RecyclerView news_recycle;
    AppPreference preference;
    NewsAdapter news_adapter;
    List<News_model.NewsData> newsDataList = new ArrayList<>();
    AdminAPI adminAPI;
    CustomDialog dialog;
    int totalPageCount = 0;
    int page = 1;

    boolean isPaginationTimes = false ;

    public NewsFragment() {
        // Required empty public constructor
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_news, container, false);
        adminAPI = ServiceGenerator.getAPIClass();
        preference = new AppPreference(getActivity());
        dialog = new CustomDialog(getActivity());
        fetchIds();
        return view;
    }

    private void fetchIds()
    {

        news_recycle = (RecyclerView) view.findViewById(R.id.news_recycler);
        news_adapter = new NewsAdapter(newsDataList, getActivity());
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        news_recycle.setLayoutManager(gridLayoutManager);
        news_recycle.setItemAnimator(new DefaultItemAnimator());
        news_recycle.setAdapter(news_adapter);


        GetNewsDatas();
        news_recycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int totalListItems = newsDataList.size();
                int lastVisiblePosition = gridLayoutManager.findLastVisibleItemPosition();

                Log.e("check",  "=-->" + totalListItems);
                Log.e("check1",  "=-->" + lastVisiblePosition);
                Log.e("check2",  "=-->" + page);
                Log.e("check3",  "=-->" + totalPageCount);

                if (lastVisiblePosition == (totalListItems - 1)) {
                    if (page < totalPageCount) {
                        page += 1;
                        isPaginationTimes = true ;
                        GetNewsDatas();

                    }
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });







    }

    private void GetNewsDatas() {
        dialog.show();
        Call<News_model> news_response = null;

        news_response = adminAPI.NEWS_MODEL(preference.getUserID(), String.valueOf(page));
        Log.e("userid","--->"+preference.getUserID() + "-->" + String.valueOf(page));
        news_response.enqueue(new Callback<News_model>() {
            @Override
            public void onResponse(Call<News_model> call, Response<News_model> response) {
                dialog.dismiss();
                if(isPaginationTimes==false){
                    newsDataList.clear();
                }
                News_model news_modelList = response.body();
                if (news_modelList != null) {
                    if (news_modelList.isStatus()) {
                        totalPageCount = Integer.parseInt(news_modelList.getTotal_page_count());
                        newsDataList.addAll(news_modelList.getData());
                    } else {

                        C.INSTANCE.showToast(getActivity(), news_modelList.getMessage());
                        // FOR_NOW_ITS_NOTHING
                    }

                    news_adapter.notifyDataSetChanged();

                    Log.e("checksize", "--->" + newsDataList.size());
                }
            }

            @Override
            public void onFailure(Call<News_model> call, Throwable t) {
                dialog.dismiss();
                C.INSTANCE.errorToast(getActivity(), t);

            }
        });


    }


}
