package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.AddMembersActivity;
import com.fundit.R;
import com.fundit.SerchPeopleActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.W;
import com.fundit.model.GetDataResponses;
import com.fundit.model.GetSearchPeople;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class GetSearchAdapter extends BaseAdapter {

    List<GetSearchPeople.People> people_search=new ArrayList<>();
    Activity activity;
    LayoutInflater inflater;
    Context context;

    AppPreference preference;
    boolean isStatus = false;

    public GetSearchAdapter(List<GetSearchPeople.People> people_search, Activity activity , boolean isStatus) {
        this.people_search = people_search;
        this.activity = activity;
        this.isStatus = isStatus;
        this.inflater = activity.getLayoutInflater();
        preference = new AppPreference(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        return people_search.size();
    }

    @Override
    public Object getItem(int position) {
        return people_search.get(position);
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

            String imagePath = W.FILE_URL+people_search.get(position).getImage();
            Log.e("path" ,imagePath);

            Picasso.with(activity)
                    .load(imagePath)
                    .into(img_profileImage);

            txt_Name.setText(people_search.get(position).getFirst_name()+""+people_search.get(position).getLast_name());
            txt_location.setText(people_search.get(position).getLocation());



        }

        else {

            String imagePath = W.FILE_URL+people_search.get(position).getImage();
            Log.e("pathsss" ,imagePath);



            Picasso.with(activity)
                    .load(imagePath)
                    .into(img_profileImage);

            txt_Name.setText(people_search.get(position).getFirst_name()+"  "+people_search.get(position).getLast_name());
            txt_location.setText(people_search.get(position).getLocation());


        }


        layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(activity , SerchPeopleActivity.class);
                intent.putExtra("id" , people_search.get(position).getId());
                intent.putExtra("details" , people_search.get(position));
                Log.e("id",people_search.get(position).getId());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);




            }
        });




        return view;
    }
}
