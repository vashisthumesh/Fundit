package com.fundit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fundit.FinalOrderPlace;
import com.fundit.R;
import com.fundit.Search_fundituserActivity;
import com.fundit.SerchPeopleActivity;
import com.fundit.a.AppPreference;
import com.fundit.a.W;
import com.fundit.model.GetSearchPeople;
import com.squareup.picasso.Picasso;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;


public class PeopleSerchAdapter extends BaseAdapter  {

    List<GetSearchPeople.People> people_search=new ArrayList<>();

    updateinterface onupdateinterface;
    LayoutInflater inflater = null;
    Context context;
    Activity activity;
    AppPreference preference;
    boolean isStatus = false;

    public PeopleSerchAdapter(List<GetSearchPeople.People> people_search, Context context, Activity activity, Search_fundituserActivity onupdateinterface) {
        this.people_search = people_search;
        this.context = context;
        this.activity = activity;
        this.onupdateinterface=onupdateinterface;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        final View view=inflater.inflate(R.layout.layout_fundspot_list_item,parent,false);

        TextView txt_Name = (TextView) view.findViewById(R.id.txt_Name);
        TextView txt_location = (TextView) view.findViewById(R.id.txt_location);
        LinearLayout layout_main = (LinearLayout) view.findViewById(R.id.layout_main);

        CircleImageView img_profileImage = (CircleImageView) view.findViewById(R.id.img_profileImage);





            String imagePath = W.FILE_URL+people_search.get(position).getImage();
            Log.e("path" ,imagePath);

            Picasso.with(activity)
                    .load(imagePath)
                    .into(img_profileImage);

            txt_Name.setText(people_search.get(position).getFirst_name()+""+people_search.get(position).getLast_name());
            txt_location.setText(people_search.get(position).getLocation());



        layout_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onupdateinterface.updateintent(people_search.get(position));


            }
        });




        return view;
    }




    public interface updateinterface{

        void updateintent(GetSearchPeople.People position);
    }


}
