package com.fundit.Onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fundit.R;

/**
 * Created by NWSPL-17 on 06-Sep-17.
 */

public class OnboardingFragment1 extends Fragment {


    View view;

    ImageView image ;
    TextView txt_title , txt_message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.onboarding_fragment1 , container , false);
        fetchIds();

        return view;
    }

    private void fetchIds() {

        image = (ImageView) view.findViewById(R.id.image);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_message = (TextView) view.findViewById(R.id.txt_message);


        image.setImageResource(R.drawable.stars);
        txt_title.setText("Create Your Own Campaign");
        txt_message.setText("Organizations can partner up with local fundsopt to raise money");



    }
}
