package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fundit.AddMembersActivity;
import com.fundit.R;
import com.fundit.a.AppPreference;
import com.fundit.a.C;
import com.fundit.a.W;
import com.fundit.model.GetDataResponses;
import com.fundit.model.GetSearchPeople;
import com.fundit.model.VerifyResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GetDataAdapter extends BaseAdapter {

    List<GetDataResponses.Data> fundSpotList=new ArrayList<>();
    List<GetSearchPeople.People> people_search=new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    Context context;

    AppPreference preference;
    boolean isStatus = false;

    public GetDataAdapter(List<GetDataResponses.Data> fundSpotList, Activity activity , boolean isStatus) {
        this.fundSpotList = fundSpotList;
        this.activity = activity;
        this.isStatus = isStatus;
        this.inflater = activity.getLayoutInflater();
        preference = new AppPreference(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return fundSpotList.size();
    }

    @Override
    public Object getItem(int position) {
        return fundSpotList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {


        View view=inflater.inflate(R.layout.layout_fundspot_list_item,parent,false);

        TextView txt_Name = (TextView) view.findViewById(R.id.txt_Name);
        TextView txt_location = (TextView) view.findViewById(R.id.txt_location);
        LinearLayout layout_main = (LinearLayout) view.findViewById(R.id.layout_main);

        CircleImageView img_profileImage = (CircleImageView) view.findViewById(R.id.img_profileImage);



        if (isStatus){

            String imagePath = W.FILE_URL+fundSpotList.get(position).getFundspot().getImage();
            Log.e("path" ,imagePath);

            Picasso.with(activity)
                    .load(imagePath)
                    .into(img_profileImage);

            txt_Name.setText(fundSpotList.get(position).getFundspot().getTitle());
            txt_location.setText(fundSpotList.get(position).getFundspot().getLocation());



        }

        else {

            String imagePath = W.FILE_URL+fundSpotList.get(position).getOrganization().getImage();
            Log.e("pathsss" ,imagePath);



            Picasso.with(activity)
                    .load(imagePath)
                    .into(img_profileImage);

            txt_Name.setText(fundSpotList.get(position).getOrganization().getTitle());
            txt_location.setText(fundSpotList.get(position).getOrganization().getLocation());


        }


        layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity , AddMembersActivity.class);
                intent.putExtra("details" , fundSpotList.get(position));
                intent.putExtra("profileMode" , true);
                intent.putExtra("isStatus" , isStatus);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);




            }
        });




        return view;
    }
}
