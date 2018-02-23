package com.fundit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fundit.Onboarding.OnboardingFragment1;
import com.fundit.Onboarding.OnboardingFragment2;
import com.fundit.Onboarding.OnboardingFragment3;
import com.fundit.Onboarding.OnboardingFragment4;
import com.fundit.a.AppPreference;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

public class WalkThroughActivity extends AppCompatActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;
    TextView txt_skip;

    AppPreference preference ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_through);

        preference = new AppPreference(getApplicationContext());
        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (SmartTabLayout)findViewById(R.id.indicator);
        skip = (Button) findViewById(R.id.skip);
        next = (Button)findViewById(R.id.next);

        txt_skip = (TextView) findViewById(R.id.txt_skip);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    case 3 : return new OnboardingFragment4();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };

        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(position == 3){
                    txt_skip.setText("Done");
                } else {
                    txt_skip.setText("Skip");
                }
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pager.getCurrentItem() == 3){
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                }
            }
        });

        txt_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preference.setFirstTime(true);
                finishOnboarding();
            }
        });
    }

    private void finishOnboarding() {
        Intent main = new Intent(this, SignInActivity.class);
        preference.setFirstTime(true);
        startActivity(main);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.gc();
    }


}
