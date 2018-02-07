package com.fundit.Onboarding;

import android.os.Bundle;
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

public class OnboardingFragment3 extends Fragment {


    View view;

    ImageView image;
    TextView txt_title, txt_message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.onboarding_fragment1, container, false);
        fetchIds();

        return view;
    }

    private void fetchIds() {

        image = (ImageView) view.findViewById(R.id.image);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_message = (TextView) view.findViewById(R.id.txt_message);


        image.setImageResource(R.drawable.user);
        txt_title.setText("Promote Your Business");
        txt_message.setText("Enjoy good PR, expand your customer base and gain advertising");

    }


}
